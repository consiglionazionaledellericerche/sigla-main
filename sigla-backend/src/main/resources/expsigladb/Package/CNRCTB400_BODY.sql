--------------------------------------------------------
--  DDL for Package Body CNRCTB400
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "CNRCTB400" AS

-- =================================================================================================
-- Select record di INVENTARIO_BENI
-- =================================================================================================
FUNCTION getInventarioBeni
   (inPgInventario NUMBER,
    inNrInventario NUMBER,
    inProgressivo NUMBER,
    eseguiLock CHAR
   ) RETURN INVENTARIO_BENI%ROWTYPE IS

   aRecInventarioBeni INVENTARIO_BENI%ROWTYPE;

BEGIN

   IF eseguiLock = 'N' THEN

      SELECT * INTO aRecInventarioBeni
      FROM   INVENTARIO_BENI
      WHERE  pg_inventario = inPgInventario AND
             nr_inventario = inNrInventario AND
             progressivo = inProgressivo;

   ELSE

      SELECT * INTO aRecInventarioBeni
      FROM   INVENTARIO_BENI
      WHERE  pg_inventario = inPgInventario AND
             nr_inventario = inNrInventario AND
             progressivo = inProgressivo
      FOR UPDATE NOWAIT;

   END IF;

   RETURN aRecInventarioBeni;

EXCEPTION

   WHEN others THEN
        IBMERR001.RAISE_ERR_GENERICO
           ('Errore in recupero del bene. Inventario: ' || inPgInventario || ' ' ||
            'Bene: ' || inNrInventario || '-' || inProgressivo);

END getInventarioBeni;

-- =================================================================================================

FUNCTION getInventarioBeni
   (inIdBeneOrigine VARCHAR2,
    eseguiLock CHAR
   ) RETURN INVENTARIO_BENI%ROWTYPE IS

   aRecInventarioBeni INVENTARIO_BENI%ROWTYPE;

BEGIN

   IF eseguiLock = 'N' THEN

      SELECT * INTO aRecInventarioBeni
      FROM   INVENTARIO_BENI
      WHERE  id_bene_origine = inIdBeneOrigine;

   ELSE

      SELECT * INTO aRecInventarioBeni
      FROM   INVENTARIO_BENI
      WHERE  id_bene_origine = inIdBeneOrigine
      FOR UPDATE NOWAIT;

   END IF;

   RETURN aRecInventarioBeni;

EXCEPTION

   WHEN others THEN
        IBMERR001.RAISE_ERR_GENERICO
           ('Errore in recupero del bene. Identificativo bene origine: ' || inIdBeneOrigine);

END getInventarioBeni;

-- =================================================================================================
-- Torna Y/N se esiste o meno il bene in INVENTARIO_BENI
-- =================================================================================================
FUNCTION checkEsisteBene
   (inIdBeneOrigine VARCHAR2
   ) RETURN CHAR IS

   flEsiste INTEGER;

BEGIN

   SELECT COUNT(*) INTO flEsiste
   FROM   DUAL
   WHERE  EXISTS
          (SELECT 1
           FROM   INVENTARIO_BENI
           WHERE  id_bene_origine = inIdBeneOrigine);

   IF flEsiste = 0 THEN
      RETURN 'N';
   ELSE
      RETURN 'Y';
   END IF;

   RETURN flEsiste;

END checkEsisteBene;

-- =================================================================================================
-- Ritorna il progressivo identificativo dell'inventario
-- =================================================================================================
FUNCTION getPgInventario
   (inCdCds VARCHAR2,
    inCdUo VARCHAR2
   ) RETURN NUMBER IS

   aPgInventario NUMBER(10);

BEGIN

   SELECT pg_inventario INTO aPgInventario
   FROM   ASS_INVENTARIO_UO
   WHERE  cd_cds = inCdCds AND
          cd_unita_organizzativa = inCdUo;

   RETURN aPgInventario;

EXCEPTION

   WHEN others THEN
        IBMERR001.RAISE_ERR_GENERICO
           ('Errore in recupero del progressivo identificativo dell''inventario. Cds: ' || inCdCds || ' ' ||
            'Uo: ' || inCdUo);

END getPgInventario;

-- =================================================================================================
-- Ritorna un record di CATEGORIA_GRUPPO_INVENT
-- =================================================================================================
FUNCTION getCategoriaGruppoInvent
   (inCdCategoriaGruppo VARCHAR2
   ) RETURN CATEGORIA_GRUPPO_INVENT%ROWTYPE IS

   aRecCategoriaGruppoInvent CATEGORIA_GRUPPO_INVENT%ROWTYPE;

BEGIN

   SELECT * INTO aRecCategoriaGruppoInvent
   FROM   CATEGORIA_GRUPPO_INVENT
   WHERE  cd_categoria_gruppo = inCdCategoriaGruppo;

   RETURN aRecCategoriaGruppoInvent;

EXCEPTION

   WHEN others THEN
        IBMERR001.RAISE_ERR_GENERICO
           ('Errore in recupero della categoria gruppo del bene. Codice: ' || inCdCategoriaGruppo);

END getCategoriaGruppoInvent;

-- =================================================================================================
-- Lettura ubicazione di default
-- =================================================================================================
FUNCTION getUbicazioneDefault
   (inCdCds VARCHAR2,
    inUo VARCHAR2) RETURN UBICAZIONE_BENE%ROWTYPE IS

   aRecUbicazioneBene UBICAZIONE_BENE%ROWTYPE;

BEGIN

   SELECT * INTO aRecUbicazioneBene
   FROM   UBICAZIONE_BENE
   WHERE  cd_cds = inCdCds AND
          cd_unita_organizzativa = inUo AND
          fl_ubicazione_default = 'Y';

   RETURN aRecUbicazioneBene;

EXCEPTION

   WHEN others THEN
        IBMERR001.RAISE_ERR_GENERICO
           ('Errore in recupero ubicazione di default per Cds ' || inCdCds || ' Uo ' || inUo);

END getUbicazioneDefault;

-- =================================================================================================

FUNCTION getCdUbicazioneDefault
   (inCdCds VARCHAR2,
    inUo VARCHAR2
   ) RETURN VARCHAR2 IS

   aCdUbicazione UBICAZIONE_BENE.cd_ubicazione%TYPE;

BEGIN

   SELECT cd_ubicazione INTO aCdUbicazione
   FROM   UBICAZIONE_BENE
   WHERE  cd_cds = inCdCds AND
          cd_unita_organizzativa = inUo AND
          fl_ubicazione_default = 'Y';

   RETURN aCdUbicazione;

EXCEPTION

   WHEN others THEN
        IBMERR001.RAISE_ERR_GENERICO
           ('Errore in recupero ubicazione di default per Cds ' || inCdCds || ' Uo ' || inUo);

END getCdUbicazioneDefault;

-- =================================================================================================
-- Inserisce un record in NUMERATORE_BUONO_C_S
-- =================================================================================================
PROCEDURE insNumeratoreBuonoCS
   (aRecNumeratoreBuonoCS NUMERATORE_BUONO_C_S%ROWTYPE) IS

BEGIN

   INSERT INTO NUMERATORE_BUONO_C_S
          (pg_inventario,
           ti_carico_scarico,
           esercizio,
           iniziale,
           corrente,
           dacr,
           utcr,
           duva,
           utuv,
           pg_ver_rec)
   VALUES (aRecNumeratoreBuonoCS.pg_inventario,
           aRecNumeratoreBuonoCS.ti_carico_scarico,
           aRecNumeratoreBuonoCS.esercizio,
           aRecNumeratoreBuonoCS.iniziale,
           aRecNumeratoreBuonoCS.corrente,
           aRecNumeratoreBuonoCS.dacr,
           aRecNumeratoreBuonoCS.utcr,
           aRecNumeratoreBuonoCS.duva,
           aRecNumeratoreBuonoCS.utuv,
           aRecNumeratoreBuonoCS.pg_ver_rec);

END insNumeratoreBuonoCS;

-- =================================================================================================
-- Select record di NUMERATORE_BUONO_C_S
-- =================================================================================================
FUNCTION getNumeratoreBuonoCS
   (inPgInventario NUMBER,
    inTipoCaricoScarico CHAR,
    inEsercizio NUMBER,
    inUtente VARCHAR2,
    inData DATE,
    eseguiLock CHAR
   ) RETURN NUMERATORE_BUONO_C_S%ROWTYPE IS

   aRecNumeratoreBuonoCS NUMERATORE_BUONO_C_S%ROWTYPE;

BEGIN

   IF eseguiLock = 'N' THEN

      SELECT * INTO aRecNumeratoreBuonoCS
      FROM   NUMERATORE_BUONO_C_S
      WHERE  pg_inventario = inPgInventario AND
             ti_carico_scarico = inTipoCaricoScarico AND
             esercizio = inEsercizio;

   ELSE

      SELECT * INTO aRecNumeratoreBuonoCS
      FROM   NUMERATORE_BUONO_C_S
      WHERE  pg_inventario = inPgInventario AND
             ti_carico_scarico = inTipoCaricoScarico AND
             esercizio = inEsercizio
      FOR UPDATE NOWAIT;

   END IF;

   RETURN aRecNumeratoreBuonoCS;

EXCEPTION

   WHEN no_data_found THEN

        BEGIN

           aRecNumeratoreBuonoCS.pg_inventario:=inPgInventario;
           aRecNumeratoreBuonoCS.ti_carico_scarico:=inTipoCaricoScarico;
           aRecNumeratoreBuonoCS.esercizio:=inEsercizio;
           aRecNumeratoreBuonoCS.iniziale:=0;
           aRecNumeratoreBuonoCS.corrente:=0;
           aRecNumeratoreBuonoCS.dacr:=inData;
           aRecNumeratoreBuonoCS.utcr:=inUtente;
           aRecNumeratoreBuonoCS.duva:=inData;
           aRecNumeratoreBuonoCS.utuv:=inUtente;
           aRecNumeratoreBuonoCS.pg_ver_rec:=1;

           insNumeratoreBuonoCS(aRecNumeratoreBuonoCS);

           RETURN aRecNumeratoreBuonoCS;

        EXCEPTION

           WHEN others THEN
                IBMERR001.RAISE_ERR_GENERICO
                   ('Errore in creazione numeratore buoni di carico/scarico. Inventario: ' || inPgInventario || ' ' ||
                    'Esercizio: ' || inEsercizio || ' ' || 'Tipo CaricoScarico: ' || inTipoCaricoScarico);

        END;

   WHEN others THEN
        IBMERR001.RAISE_ERR_GENERICO
           ('Errore in recupero numeratore buoni di carico/scarico. Inventario: ' || inPgInventario || ' ' ||
            'Esercizio: ' || inEsercizio || ' ' || 'Tipo CaricoScarico: ' || inTipoCaricoScarico);

END getNumeratoreBuonoCS;

-- =================================================================================================
-- Lettura del valore iniziale di numerazione beni per un dato inventario
-- =================================================================================================
FUNCTION getNrInventarioIniziale
   (inPgInventario NUMBER) RETURN NUMBER IS

   aNrInventario INVENTARIO_BENI.nr_inventario%TYPE;

BEGIN

   SELECT nr_inventario_iniziale INTO aNrInventario
   FROM   ID_INVENTARIO
   WHERE  pg_inventario = inPgInventario;

   RETURN aNrInventario;

END getNrInventarioIniziale;

-- =================================================================================================
-- Lettura del valore massimo del numeratore bene per un dato inventario
-- =================================================================================================
FUNCTION getNewNrInventario
   (inPgInventario NUMBER) RETURN NUMBER IS

   aNrInventario INVENTARIO_BENI.nr_inventario%TYPE;

BEGIN

   -- Trovo il valore massimo del bene attualmente in archivio per un dato inventario

   SELECT NVL(MAX(nr_inventario),0) INTO aNrInventario
   FROM   INVENTARIO_BENI
   WHERE  pg_inventario = inPgInventario;

   -- Se questo vale zero significa che non sono mai stati creati beni per quell'inventario, quindi recupero
   -- il progressivo iniziale da INVENTARIO_BENI.

   IF aNrInventario = 0 Or aNrInventario< getNrInventarioIniziale(inPgInventario) THEN
      aNrInventario:=getNrInventarioIniziale(inPgInventario);
   ELSE
      aNrInventario:=aNrInventario + 1;
   END IF;

   RETURN aNrInventario;

EXCEPTION

   WHEN others THEN
        IBMERR001.RAISE_ERR_GENERICO
           ('Errore in recupero progressivo bene per carico. Inventario: ' || inPgInventario);

END getNewNrInventario;

-- =================================================================================================
-- Lettura del valore massimo di progressivo bene
-- =================================================================================================
FUNCTION getNewProgressivo
   (inPgInventario NUMBER,
    inNrInventario NUMBER) RETURN NUMBER IS

   aProgressivo INVENTARIO_BENI.progressivo%TYPE;

BEGIN

   SELECT MAX(progressivo) + 1 INTO aProgressivo
   FROM   INVENTARIO_BENI
   WHERE  pg_inventario = inPgInventario AND
          nr_inventario = inNrInventario;

   RETURN aProgressivo;

END getNewProgressivo;

-- =================================================================================================
-- Aggiorna valore corrente di NUMERATORE_BUONO_C_S
-- =================================================================================================
PROCEDURE upgCorrenteNumeratoreBuonoCS
   (inPgInventario NUMBER,
    inTipoCaricoScarico CHAR,
    inEsercizio NUMBER,
    inData DATE,
    inUtente VARCHAR2) IS

BEGIN

   UPDATE NUMERATORE_BUONO_C_S
   SET    corrente = corrente + 1,
          duva = inData,
          utuv = inUtente,
          pg_ver_rec = pg_ver_rec + 1
   WHERE  pg_inventario = inPgInventario AND
          ti_carico_scarico = inTipoCaricoScarico AND
          esercizio = inEsercizio;

END upgCorrenteNumeratoreBuonoCS;

-- =================================================================================================
-- Inserisce un record in ASS_TRASFERIMENTO_BENI_INV
-- =================================================================================================
PROCEDURE insAssTrasferimentoBeniInv
   (aRecAssTrasferimentoBeniInv ASS_TRASFERIMENTO_BENI_INV%ROWTYPE) IS

BEGIN

   INSERT INTO ASS_TRASFERIMENTO_BENI_INV
          (pg_inventario_origine,
           nr_inventario_origine,
           progressivo_origine,
           pg_inventario_dest,
           nr_inventario_dest,
           progressivo_dest,
           valore_iniziale,
           variazione_piu,
           variazione_meno,
           imponibile_ammortamento,
           valore_ammortizzato,
           dacr,
           utcr,
           duva,
           utuv,
           pg_ver_rec)
   VALUES (aRecAssTrasferimentoBeniInv.pg_inventario_origine,
           aRecAssTrasferimentoBeniInv.nr_inventario_origine,
           aRecAssTrasferimentoBeniInv.progressivo_origine,
           aRecAssTrasferimentoBeniInv.pg_inventario_dest,
           aRecAssTrasferimentoBeniInv.nr_inventario_dest,
           aRecAssTrasferimentoBeniInv.progressivo_dest,
           aRecAssTrasferimentoBeniInv.valore_iniziale,
           aRecAssTrasferimentoBeniInv.variazione_piu,
           aRecAssTrasferimentoBeniInv.variazione_meno,
           aRecAssTrasferimentoBeniInv.imponibile_ammortamento,
           aRecAssTrasferimentoBeniInv.valore_ammortizzato,
           aRecAssTrasferimentoBeniInv.dacr,
           aRecAssTrasferimentoBeniInv.utcr,
           aRecAssTrasferimentoBeniInv.duva,
           aRecAssTrasferimentoBeniInv.utuv,
           aRecAssTrasferimentoBeniInv.pg_ver_rec);

END insAssTrasferimentoBeniInv;

-- =================================================================================================
-- Inserisce un record in INVENTARIO_BENI
-- =================================================================================================
PROCEDURE insInventarioBeni
   (aRecInventarioBeni INVENTARIO_BENI%Rowtype) IS

BEGIN

   INSERT INTO INVENTARIO_BENI
          (pg_inventario,
           nr_inventario,
           progressivo,
           ds_bene,
           cd_categoria_gruppo,
           ti_ammortamento,
           fl_ammortamento,
           cd_condizione_bene,
           ti_commerciale_istituzionale,
           valore_iniziale,
           valore_ammortizzato,
           variazione_piu,
           variazione_meno,
           imponibile_ammortamento,
           valore_alienazione,
           fl_totalmente_scaricato,
           collocazione,
           cd_cds,
           cd_unita_organizzativa,
           cd_ubicazione,
           cd_assegnatario,
           dt_validita_variazione,
           dacr,
           utcr,
           duva,
           utuv,
           pg_ver_rec,
           etichetta,
           esercizio_carico_bene,
           id_bene_origine,
           fl_migrato,
           cd_barre,
           targa,
           seriale)
   VALUES (aRecInventarioBeni.pg_inventario,
           aRecInventarioBeni.nr_inventario,
           aRecInventarioBeni.progressivo,
           aRecInventarioBeni.ds_bene,
           aRecInventarioBeni.cd_categoria_gruppo,
           aRecInventarioBeni.ti_ammortamento,
           aRecInventarioBeni.fl_ammortamento,
           aRecInventarioBeni.cd_condizione_bene,
           aRecInventarioBeni.ti_commerciale_istituzionale,
           aRecInventarioBeni.valore_iniziale,
           aRecInventarioBeni.valore_ammortizzato,
           aRecInventarioBeni.variazione_piu,
           aRecInventarioBeni.variazione_meno,
           aRecInventarioBeni.imponibile_ammortamento,
           aRecInventarioBeni.valore_alienazione,
           aRecInventarioBeni.fl_totalmente_scaricato,
           aRecInventarioBeni.collocazione,
           aRecInventarioBeni.cd_cds,
           aRecInventarioBeni.cd_unita_organizzativa,
           aRecInventarioBeni.cd_ubicazione,
           aRecInventarioBeni.cd_assegnatario,
           aRecInventarioBeni.dt_validita_variazione,
           aRecInventarioBeni.dacr,
           aRecInventarioBeni.utcr,
           aRecInventarioBeni.duva,
           aRecInventarioBeni.utuv,
           aRecInventarioBeni.pg_ver_rec,
           aRecInventarioBeni.etichetta,
           aRecInventarioBeni.esercizio_carico_bene,
           aRecInventarioBeni.id_bene_origine,
           --aRecInventarioBeni.fl_migrato
           'N',
           aRecInventarioBeni.cd_barre,
           aRecInventarioBeni.targa,
           aRecInventarioBeni.seriale);

END insInventarioBeni;

-- =================================================================================================
-- Cancellazione tabella appoggio INVENTARIO_BENI_APG
-- =================================================================================================
PROCEDURE cancellaInventarioBeniApg
   (
    localTransId VARCHAR2
   ) IS

BEGIN

   DELETE INVENTARIO_BENI_APG
   WHERE  local_transaction_id=localTransId and
   	 ( pg_buono_c_s <0 OR PG_BUONO_C_S IS NULL);

END cancellaInventarioBeniApg;

-- =================================================================================================
-- Funzione di inserimento su tabella BUONO_CARICO_SCARICO
-- =================================================================================================
PROCEDURE insBuonoCaricoScarico
   (
    aBuonoCaricoScarico BUONO_CARICO_SCARICO%ROWTYPE
   ) IS

BEGIN

   INSERT INTO BUONO_CARICO_SCARICO
          (PG_INVENTARIO,
           TI_DOCUMENTO,
           ESERCIZIO,
           PG_BUONO_C_S,
           DS_BUONO_CARICO_SCARICO,
           DATA_REGISTRAZIONE,
           CD_TIPO_CARICO_SCARICO,
           PROVENIENZA,
           DACR,
           UTCR,
           DUVA,
           UTUV,
           PG_VER_REC)
   VALUES (aBuonoCaricoScarico.pg_inventario,
           aBuonoCaricoScarico.ti_documento,
           aBuonoCaricoScarico.esercizio,
           aBuonoCaricoScarico.pg_buono_c_s,
           aBuonoCaricoScarico.ds_buono_carico_scarico,
           aBuonoCaricoScarico.data_registrazione,
           aBuonoCaricoScarico.cd_tipo_carico_scarico,
           aBuonoCaricoScarico.provenienza,
           aBuonoCaricoScarico.dacr,
           aBuonoCaricoScarico.utcr,
           aBuonoCaricoScarico.duva,
           aBuonoCaricoScarico.utuv,
           aBuonoCaricoScarico.pg_ver_rec);

END insBuonoCaricoScarico;

-- =================================================================================================
-- Funzione di inserimento su tabella BUONO_CARICO_SCARICO_DETT
-- =================================================================================================
PROCEDURE insBuonoCaricoScaricoDett
   (
    aBuonoCaricoScaricoDett BUONO_CARICO_SCARICO_DETT%ROWTYPE
   ) IS

BEGIN

   INSERT INTO BUONO_CARICO_SCARICO_DETT
          (PG_INVENTARIO,
           TI_DOCUMENTO,
           ESERCIZIO,
           PG_BUONO_C_S,
           NR_INVENTARIO,
           PROGRESSIVO,
           INTERVALLO,
           QUANTITA,
           VALORE_UNITARIO,
           DACR,
           UTCR,
           DUVA,
           UTUV,
           PG_VER_REC,
           STATO_COGE,
           STATO_COGE_QUOTE)
   VALUES (aBuonoCaricoScaricoDett.pg_inventario,
           aBuonoCaricoScaricoDett.ti_documento,
           aBuonoCaricoScaricoDett.esercizio,
           aBuonoCaricoScaricoDett.pg_buono_c_s,
           aBuonoCaricoScaricoDett.nr_inventario,
           aBuonoCaricoScaricoDett.progressivo,
           aBuonoCaricoScaricoDett.intervallo,
           aBuonoCaricoScaricoDett.quantita,
           aBuonoCaricoScaricoDett.valore_unitario,
           aBuonoCaricoScaricoDett.dacr,
           aBuonoCaricoScaricoDett.utcr,
           aBuonoCaricoScaricoDett.duva,
           aBuonoCaricoScaricoDett.utuv,
           aBuonoCaricoScaricoDett.pg_ver_rec,
           aBuonoCaricoScaricoDett.stato_coge,
           aBuonoCaricoScaricoDett.stato_coge_quote);

END insBuonoCaricoScaricoDett;

-- =================================================================================================
-- Funzione di inserimento su tabella ASS_INV_BENE_FATTURA
-- =================================================================================================
PROCEDURE insAssInvBeneFattura
   (
    aAssInvBeneFattura ASS_INV_BENE_FATTURA%ROWTYPE
   ) IS

BEGIN

   INSERT INTO ASS_INV_BENE_FATTURA
          (CD_CDS_FATT_ATT,
           CD_UO_FATT_ATT,
           ESERCIZIO_FATT_ATT,
           PG_FATTURA_ATTIVA,
           PROGRESSIVO_RIGA_FATT_ATT,
           CD_CDS_FATT_PASS,
           CD_UO_FATT_PASS,
           ESERCIZIO_FATT_PASS,
           PG_FATTURA_PASSIVA,
           PROGRESSIVO_RIGA_FATT_PASS,
           PG_INVENTARIO,
           NR_INVENTARIO,
           PROGRESSIVO,
           DACR,
           UTCR,
           DUVA,
           UTUV,
           PG_VER_REC,
           PG_BUONO_C_S,
           ESERCIZIO,
           TI_DOCUMENTO,
           PG_RIGA,
           CD_CDS_DOC_GEN,
           CD_UO_DOC_GEN,
           ESERCIZIO_DOC_GEN,
           PG_DOCUMENTO_GENERICO,
           CD_TIPO_DOCUMENTO_AMM,
           PROGRESSIVO_RIGA_DOC_GEN)
   VALUES (aAssInvBeneFattura.cd_cds_fatt_att,
           aAssInvBeneFattura.cd_uo_fatt_att,
           aAssInvBeneFattura.esercizio_fatt_att,
           aAssInvBeneFattura.pg_fattura_attiva,
           aAssInvBeneFattura.progressivo_riga_fatt_att,
           aAssInvBeneFattura.cd_cds_fatt_pass,
           aAssInvBeneFattura.cd_uo_fatt_pass,
           aAssInvBeneFattura.esercizio_fatt_pass,
           aAssInvBeneFattura.pg_fattura_passiva,
           aAssInvBeneFattura.progressivo_riga_fatt_pass,
           aAssInvBeneFattura.pg_inventario,
           aAssInvBeneFattura.nr_inventario,
           aAssInvBeneFattura.progressivo,
           aAssInvBeneFattura.dacr,
           aAssInvBeneFattura.utcr,
           aAssInvBeneFattura.duva,
           aAssInvBeneFattura.utuv,
           aAssInvBeneFattura.pg_ver_rec,
           aAssInvBeneFattura.pg_buono_c_s,
           aAssInvBeneFattura.esercizio,
           aAssInvBeneFattura.ti_documento,
           aAssInvBeneFattura.pg_riga,
           aAssInvBeneFattura.cd_cds_doc_gen,
           aAssInvBeneFattura.cd_uo_doc_gen,
           aAssInvBeneFattura.esercizio_doc_gen,
           aAssInvBeneFattura.pg_documento_generico,
           aAssInvBeneFattura.cd_tipo_documento_amm,
           aAssInvBeneFattura.progressivo_riga_doc_gen);

END insAssInvBeneFattura;

-- =================================================================================================
-- Controllo stato apertura chiusura dell'inventario
-- =================================================================================================
FUNCTION checkStatoInventApCh
   (inPgInventario NUMBER,
    inEsercizio NUMBER,
    inData DATE) RETURN VARCHAR2 IS

  aStato CHAR(1);
  aRecInventarioApCh INVENTARIO_AP_CH%ROWTYPE;

BEGIN

   aStato:='C';

   BEGIN

      SELECT * INTO aRecInventarioApCh
      FROM   INVENTARIO_AP_CH
      WHERE  pg_inventario = inPgInventario AND
             esercizio = inEsercizio AND
             dt_apertura <= inData AND
             dt_chiusura >= inData;

      IF aRecInventarioApCh.stato = 'A' THEN
         aStato:='A';
      END IF;

      RETURN aStato;

   EXCEPTION

      WHEN no_data_found THEN
           RETURN aStato;

   END;

   RETURN aStato;

END checkStatoInventApCh;

-- =================================================================================================
-- Funzione di inserimento su tabella AMMORTAMENTO_BENE_INV
-- =================================================================================================
Procedure ins_AMMORTAMENTO_BENE_INV
   (aDest AMMORTAMENTO_BENE_INV%rowtype) IS

BEGIN

   INSERT INTO AMMORTAMENTO_BENE_INV
          (PG_INVENTARIO,
           NR_INVENTARIO,
           PROGRESSIVO,
           ESERCIZIO,
           CD_TIPO_AMMORTAMENTO,
           TI_AMMORTAMENTO,
           CD_CATEGORIA_GRUPPO,
           ESERCIZIO_COMPETENZA,
           IMPONIBILE_AMMORTAMENTO,
           IM_MOVIMENTO_AMMORT,
           PERC_AMMORTAMENTO,
           DACR,
           UTCR,
           DUVA,
           UTUV,
           PG_VER_REC,
           NUMERO_ANNI,
           NUMERO_ANNO,
           PERC_PRIMO_ANNO,
           PERC_SUCCESSIVI,
           CD_CDS_UBICAZIONE,
           CD_UO_UBICAZIONE,
           FL_STORNO,
           PG_RIGA,
           PG_BUONO_S)
   VALUES (aDest.PG_INVENTARIO,
           aDest.NR_INVENTARIO,
           aDest.PROGRESSIVO,
           aDest.ESERCIZIO,
           aDest.CD_TIPO_AMMORTAMENTO,
           aDest.TI_AMMORTAMENTO,
           aDest.CD_CATEGORIA_GRUPPO,
           aDest.ESERCIZIO_COMPETENZA,
           aDest.IMPONIBILE_AMMORTAMENTO,
           aDest.IM_MOVIMENTO_AMMORT,
           aDest.PERC_AMMORTAMENTO,
           aDest.DACR,
           aDest.UTCR,
           aDest.DUVA,
           aDest.UTUV,
           aDest.PG_VER_REC,
           aDest.NUMERO_ANNI,
           aDest.NUMERO_ANNO,
           aDest.PERC_PRIMO_ANNO,
           aDest.PERC_SUCCESSIVI,
           aDest.CD_CDS_UBICAZIONE,
           aDest.CD_UO_UBICAZIONE,
           aDest.FL_STORNO,
           aDest.PG_RIGA,
           aDest.PG_BUONO_S);

END ins_AMMORTAMENTO_BENE_INV;
-- =================================================================================================
-- Inserimento record INVENTARIO_UTILIZZATORI_LA
-- =================================================================================================
PROCEDURE insInventarioUtilizzatoriLa
   (aRecInventarioUtilizzatoriLa INVENTARIO_UTILIZZATORI_LA%ROWTYPE) IS

BEGIN

   INSERT INTO INVENTARIO_UTILIZZATORI_LA
          (pg_inventario,
           nr_inventario,
           progressivo,
           cd_utilizzatore_cdr,
           cd_linea_attivita,
           percentuale_utilizzo_cdr,
           percentuale_utilizzo_la,
           dacr,
           utcr,
           duva,
           utuv,
           pg_ver_rec)
   VALUES (aRecInventarioUtilizzatoriLa.pg_inventario,
           aRecInventarioUtilizzatoriLa.nr_inventario,
           aRecInventarioUtilizzatoriLa.progressivo,
           aRecInventarioUtilizzatoriLa.cd_utilizzatore_cdr,
           aRecInventarioUtilizzatoriLa.cd_linea_attivita,
           aRecInventarioUtilizzatoriLa.percentuale_utilizzo_cdr,
           aRecInventarioUtilizzatoriLa.percentuale_utilizzo_la,
           aRecInventarioUtilizzatoriLa.dacr,
           aRecInventarioUtilizzatoriLa.utcr,
           aRecInventarioUtilizzatoriLa.duva,
           aRecInventarioUtilizzatoriLa.utuv,
           aRecInventarioUtilizzatoriLa.pg_ver_rec);

END insInventarioUtilizzatoriLa;

-- =================================================================================================
-- Riporto dati utilizzatori (solo per trasferimento stessa UO)
-- =================================================================================================
PROCEDURE riportaUtilizzatoriLa
   (inPgInventarioOri NUMBER,
    inNrInventarioOri NUMBER,
    inNrInventarioDest NUMBER,
    inProgressivoDest NUMBER,
    inUtente VARCHAR2,
    inData DATE
   ) IS

   aRecInventarioUtilizzatoriLa INVENTARIO_UTILIZZATORI_LA%ROWTYPE;
   gen_cur_utl GenericCurTyp;

BEGIN

   OPEN gen_cur_utl FOR

        SELECT *
        FROM   INVENTARIO_UTILIZZATORI_LA
        WHERE  pg_inventario =  inPgInventarioOri AND
               nr_inventario =  inNrInventarioOri AND
               progressivo = 0
        ORDER BY pg_inventario,
                 nr_inventario,
                 progressivo,
                 cd_utilizzatore_cdr,
                 cd_linea_attivita;

   LOOP

      FETCH gen_cur_utl INTO
            aRecInventarioUtilizzatoriLa;

      EXIT WHEN gen_cur_utl%NOTFOUND;

      aRecInventarioUtilizzatoriLa.nr_inventario:=inNrInventarioDest;
      aRecInventarioUtilizzatoriLa.progressivo:=inProgressivoDest;
      aRecInventarioUtilizzatoriLa.dacr:=inData;
      aRecInventarioUtilizzatoriLa.utcr:=inUtente;
      aRecInventarioUtilizzatoriLa.duva:=inData;
      aRecInventarioUtilizzatoriLa.utuv:=inUtente;
      aRecInventarioUtilizzatoriLa.pg_ver_rec:=1;

      insInventarioUtilizzatoriLa(aRecInventarioUtilizzatoriLa);

   END LOOP;

   CLOSE gen_cur_utl;

END riportaUtilizzatoriLa;

-- =================================================================================================
-- Crea un documento di scarico di inventario, con i relativi dettagli e le eventuali associazioni
-- con le fatture di vendita.
-- =================================================================================================
PROCEDURE UpdScaricoInventarioBeni
   (
    localTransId VARCHAR2,
    aPgInventario NUMBER,
    aEsercizio NUMBER,
    aPgBuonoCaricoScarico NUMBER,
    aDsBuonoCaricoScarico VARCHAR2,
    aCdTipoCaricoScarico VARCHAR2,
    aUtente VARCHAR2,
    isDaFattura CHAR DEFAULT 'N',
    ti_fattura CHAR,
    aDtRegistrazione DATE,
    aMessaggio IN OUT VARCHAR2
   ) IS

   pg_inv NUMBER;
   cont INTEGER;
   aStatoCoge CHAR(1);
   eseguiLock CHAR(1);
   aMessaggioBase VARCHAR2(100);
   aMessaggioBaseSegnala VARCHAR2(500);
   aMessaggioDettSegnala VARCHAR2(3900);
   DaFattura CHAR(1);
   aBuonoCaricoScarico BUONO_CARICO_SCARICO%ROWTYPE;
   aBuonoCaricoScaricoDett BUONO_CARICO_SCARICO_DETT%ROWTYPE;
   aAssInvBeneFattura ASS_INV_BENE_FATTURA%ROWTYPE;
   aRecInventarioBeni INVENTARIO_BENI%ROWTYPE;
   tot_quote_storno NUMBER(17,2):=0;
   progressivo_riga NUMBER:=0;
   PERC_AMMORTAMENTO        NUMBER(5,2):=0;
   NUMERO_ANNI              NUMBER(4):=0;
   NUMERO_ANNO              NUMBER(4):=0;
   PERC_PRIMO_ANNO          NUMBER(5,2):=0;
   PERC_SUCCESSIVI          NUMBER(5,2):=0;
   CD_TIPO		    VARCHAR2(10);
   TI_AMM 	            CHAR(1);
   conta 		    number:=0;
   fl_storno       	    CHAR(1);
   fl_vendita       	    CHAR(1);
   fl_chiude_fondo 	    CHAR(1);
   tot_carichi_anno NUMBER(17,2):=0;
   aAmmBInv AMMORTAMENTO_BENE_INV%rowtype;
BEGIN

   eseguiLock:='Y';

   -------------------------------------------------------------------------------------------------
   -- Valorizzazione messaggi da esporre in output

   aMessaggioBase:='Operazione completata con successo. ';
   aMessaggioBaseSegnala:='Attenzione, per i seguenti beni non è stato modificato automaticamente ' ||
                          'l''imponibile ammortamento in quanto non risulta allineato al suo valore assestato, ' ||
                          'procedere manualmente.';
   aMessaggioDettSegnala:=NULL;

   -------------------------------------------------------------------------------------------------
   -- Inserimento testata BUONO_CARICO_SCARICO (TIPO = scarico)

   aBuonoCaricoScarico:=NULL;

   aBuonoCaricoScarico.pg_inventario:=aPgInventario;
   aBuonoCaricoScarico.ti_documento:=BUONO_SCARICO;
   aBuonoCaricoScarico.esercizio:=aEsercizio;
   aBuonoCaricoScarico.pg_buono_c_s:=aPgBuonoCaricoScarico;
   aBuonoCaricoScarico.ds_buono_carico_scarico:=aDsBuonoCaricoScarico;
   aBuonoCaricoScarico.data_registrazione:=aDtRegistrazione;
   aBuonoCaricoScarico.cd_tipo_carico_scarico:=aCdTipoCaricoScarico;
   aBuonoCaricoScarico.dacr:=SYSDATE;
   aBuonoCaricoScarico.utcr:=aUtente;
   aBuonoCaricoScarico.duva:=SYSDATE;
   aBuonoCaricoScarico.utuv:=aUtente;
   aBuonoCaricoScarico.pg_ver_rec:=1;

   insBuonoCaricoScarico(aBuonoCaricoScarico);

   -------------------------------------------------------------------------------------------------
   -- Ciclo inserimento dettaglio BUONO_CARICO_SCARICO_DETT e ASS_INV_BENE_FATTURA

   cont :=0;

   BEGIN


      FOR riga_group_invetario_beni_apg IN
          (SELECT PG_INVENTARIO, NR_INVENTARIO, PROGRESSIVO, FL_TOTALMENTE_SCARICATO,
                  VARIAZIONE_MENO, SUM(VALORE_ALIENAZIONE) tot_alienazione
           FROM   INVENTARIO_BENI_APG
           WHERE  TRIM(LOCAL_TRANSACTION_ID) = TRIM (localTransId)
           GROUP BY PG_INVENTARIO, NR_INVENTARIO, PROGRESSIVO, FL_TOTALMENTE_SCARICATO, VARIAZIONE_MENO)

      Loop
      tot_quote_storno:=0;
       -- Lettura del bene per verificare se aggiornare o meno il campo imponibile_ammortamento
         aRecInventarioBeni:=getInventarioBeni(riga_group_invetario_beni_apg.pg_inventario,
                                                  riga_group_invetario_beni_apg.nr_inventario,
                                                  riga_group_invetario_beni_apg.progressivo,
                                                  eseguiLock);

         cont := cont+1;

         -- inserimento su tabella BUONO_CARICO_SCARICO_DETT ---------------------------------------

         Begin
          Select fl_fatturabile,fl_storno_fondo,fl_chiude_fondo,FL_VENDITA Into DaFattura,fl_storno,fl_chiude_fondo,FL_VENDITA
          From tipo_carico_scarico
          Where
            	cd_tipo_carico_scarico =aCdTipoCaricoScarico;
	  If DaFattura = 'Y' Then
	   	aStatoCoge:='X';
	  Else
            If (isDaFattura='N' AND riga_group_invetario_beni_apg.FL_TOTALMENTE_SCARICATO='Y') Then
               -- r.p. 17/02/2006 recupero stato coge dalla tabella corrispondente
            	Begin
            		Select FL_ELABORA_BUONO_COGE Into aStatoCoge
            		From tipo_carico_scarico
            		Where
            	  	cd_tipo_carico_scarico =aCdTipoCaricoScarico;
            	EXCEPTION When Others Then
               		aStatoCoge:='N';
               	end;
            Else
             	aStatoCoge:='N';
            End  If;
          End If;


            aBuonoCaricoScaricoDett:=NULL;

            aBuonoCaricoScaricoDett.pg_inventario:=aPgInventario;
            aBuonoCaricoScaricoDett.ti_documento:=BUONO_SCARICO;
            aBuonoCaricoScaricoDett.esercizio:=aEsercizio;
            aBuonoCaricoScaricoDett.pg_buono_c_s:=aPgBuonoCaricoScarico;
            aBuonoCaricoScaricoDett.nr_inventario:=riga_group_invetario_beni_apg.NR_INVENTARIO;
            aBuonoCaricoScaricoDett.progressivo:=riga_group_invetario_beni_apg.PROGRESSIVO;
            aBuonoCaricoScaricoDett.intervallo:=cont || '-' || cont;
            aBuonoCaricoScaricoDett.quantita:=1;
            aBuonoCaricoScaricoDett.valore_unitario:=riga_group_invetario_beni_apg.VARIAZIONE_MENO;
            aBuonoCaricoScaricoDett.dacr:=SYSDATE;
            aBuonoCaricoScaricoDett.utcr:=aUtente;
            aBuonoCaricoScaricoDett.duva:=SYSDATE;
            aBuonoCaricoScaricoDett.utuv:=aUtente;
            aBuonoCaricoScaricoDett.pg_ver_rec:=1;
            aBuonoCaricoScaricoDett.stato_coge:=aStatoCoge;
	    If ((fl_chiude_fondo='Y' And aRecInventarioBeni.valore_ammortizzato!=0) Or
	    (fl_chiude_fondo='N' And FL_VENDITA = 'N' And aStatoCoge='X' And riga_group_invetario_beni_apg.FL_TOTALMENTE_SCARICATO='Y' And aRecInventarioBeni.valore_ammortizzato!=0)) Then
	    	aBuonoCaricoScaricoDett.stato_coge_quote:='N';
	    Else
	    	aBuonoCaricoScaricoDett.stato_coge_quote:='X';
	    End If;
            insBuonoCaricoScaricoDett(aBuonoCaricoScaricoDett);

         END;

         -- inserimento su tabella ASS_INV_BENE_FATTURA --------------------------------------------

         BEGIN

            IF isDaFattura ='Y' And ti_fattura = 'F' THEN

               FOR riga_invetario_beni_apg IN
                    (SELECT *
                     FROM   INVENTARIO_BENI_APG
                     where  TRIM(LOCAL_TRANSACTION_ID) =TRIM(localTransId)
                        and PG_INVENTARIO = riga_group_invetario_beni_apg.PG_INVENTARIO
                        and NR_INVENTARIO = riga_group_invetario_beni_apg.NR_INVENTARIO
                        and PROGRESSIVO = riga_group_invetario_beni_apg.PROGRESSIVO)

               LOOP
		  Select NVL(Max(PG_RIGA)+1,1) into progressivo_riga
                  FROM   ass_inv_bene_fattura;


                  aAssInvBeneFattura:=NULL;

                  aAssInvBeneFattura.cd_cds_fatt_att:=riga_invetario_beni_apg.CD_CDS;
                  aAssInvBeneFattura.cd_uo_fatt_att:=riga_invetario_beni_apg.CD_UNITA_ORGANIZZATIVA;
                  aAssInvBeneFattura.esercizio_fatt_att:=riga_invetario_beni_apg.ESERCIZIO;
                  aAssInvBeneFattura.pg_fattura_attiva:=riga_invetario_beni_apg.PG_FATTURA;
                  aAssInvBeneFattura.progressivo_riga_fatt_att:=riga_invetario_beni_apg.PROGRESSIVO_RIGA;
                  aAssInvBeneFattura.pg_inventario:=riga_invetario_beni_apg.PG_INVENTARIO;
                  aAssInvBeneFattura.nr_inventario:=riga_invetario_beni_apg.NR_INVENTARIO;
                  aAssInvBeneFattura.progressivo:=riga_invetario_beni_apg.PROGRESSIVO;
                  aAssInvBeneFattura.dacr:=SYSDATE;
                  aAssInvBeneFattura.utcr:=aUtente;
                  aAssInvBeneFattura.duva:=SYSDATE;
                  aAssInvBeneFattura.utuv:=aUtente;
                  aAssInvBeneFattura.pg_ver_rec:=1;
		  aAssInvBeneFattura.pg_riga:=progressivo_riga;
		  aAssInvBeneFattura.ESERCIZIO:=riga_invetario_beni_apg.ESERCIZIO;
		  aAssInvBeneFattura.PG_BUONO_C_S:=aPgBuonoCaricoScarico;
		  aAssInvBeneFattura.TI_DOCUMENTO:=riga_invetario_beni_apg.TI_DOCUMENTO;
                  insAssInvBeneFattura(aAssInvBeneFattura);

               END LOOP;
            Elsif (isDaFattura ='Y' And ti_fattura = 'D' ) THEN

               FOR riga_invetario_beni_apg IN
                    (SELECT *
                     FROM   INVENTARIO_BENI_APG
                     where  TRIM(LOCAL_TRANSACTION_ID) =TRIM(localTransId)
                        and PG_INVENTARIO = riga_group_invetario_beni_apg.PG_INVENTARIO
                        and NR_INVENTARIO = riga_group_invetario_beni_apg.NR_INVENTARIO
                        and PROGRESSIVO = riga_group_invetario_beni_apg.PROGRESSIVO)

               LOOP
		  Select NVL(Max(PG_RIGA)+1,1) into progressivo_riga
                  FROM   ass_inv_bene_fattura;


                  aAssInvBeneFattura:=NULL;

                  aAssInvBeneFattura.cd_cds_doc_gen:=riga_invetario_beni_apg.CD_CDS;
                  aAssInvBeneFattura.cd_uo_doc_gen:=riga_invetario_beni_apg.CD_UNITA_ORGANIZZATIVA;
                  aAssInvBeneFattura.esercizio_doc_gen:=riga_invetario_beni_apg.ESERCIZIO;
                  aAssInvBeneFattura.pg_documento_generico:=riga_invetario_beni_apg.PG_FATTURA;
                  aAssInvBeneFattura.progressivo_riga_doc_gen:=riga_invetario_beni_apg.PROGRESSIVO_RIGA;
                  aAssInvBeneFattura.cd_tipo_documento_amm:=riga_invetario_beni_apg.cd_tipo_documento_amm;
                  aAssInvBeneFattura.pg_inventario:=riga_invetario_beni_apg.PG_INVENTARIO;
                  aAssInvBeneFattura.nr_inventario:=riga_invetario_beni_apg.NR_INVENTARIO;
                  aAssInvBeneFattura.progressivo:=riga_invetario_beni_apg.PROGRESSIVO;
                  aAssInvBeneFattura.dacr:=SYSDATE;
                  aAssInvBeneFattura.utcr:=aUtente;
                  aAssInvBeneFattura.duva:=SYSDATE;
                  aAssInvBeneFattura.utuv:=aUtente;
                  aAssInvBeneFattura.pg_ver_rec:=1;
		  aAssInvBeneFattura.pg_riga:=progressivo_riga;
		  aAssInvBeneFattura.ESERCIZIO:=riga_invetario_beni_apg.ESERCIZIO;
		  aAssInvBeneFattura.PG_BUONO_C_S:=aPgBuonoCaricoScarico;
		  aAssInvBeneFattura.TI_DOCUMENTO:=riga_invetario_beni_apg.TI_DOCUMENTO;
                  insAssInvBeneFattura(aAssInvBeneFattura);

               END LOOP;
	    Elsif (isDaFattura ='Y' And ti_fattura != 'F' ) THEN

               FOR riga_invetario_beni_apg IN
                    (SELECT *
                     FROM   INVENTARIO_BENI_APG
                     where  TRIM(LOCAL_TRANSACTION_ID) =TRIM(localTransId)
                        and PG_INVENTARIO = riga_group_invetario_beni_apg.PG_INVENTARIO
                        and NR_INVENTARIO = riga_group_invetario_beni_apg.NR_INVENTARIO
                        and PROGRESSIVO = riga_group_invetario_beni_apg.PROGRESSIVO)

               LOOP
		  Select NVL(Max(PG_RIGA)+1,1) into progressivo_riga
                  FROM   ass_inv_bene_fattura;


                  aAssInvBeneFattura:=NULL;

                  aAssInvBeneFattura.cd_cds_fatt_PASS:=riga_invetario_beni_apg.CD_CDS;
                  aAssInvBeneFattura.cd_uo_fatt_PASS:=riga_invetario_beni_apg.CD_UNITA_ORGANIZZATIVA;
                  aAssInvBeneFattura.esercizio_fatt_PASS:=riga_invetario_beni_apg.ESERCIZIO;
                  aAssInvBeneFattura.pg_fattura_PASSIVA:=riga_invetario_beni_apg.PG_FATTURA;
                  aAssInvBeneFattura.progressivo_riga_fatt_PASS:=riga_invetario_beni_apg.PROGRESSIVO_RIGA;
                  aAssInvBeneFattura.pg_inventario:=riga_invetario_beni_apg.PG_INVENTARIO;
                  aAssInvBeneFattura.nr_inventario:=riga_invetario_beni_apg.NR_INVENTARIO;
                  aAssInvBeneFattura.progressivo:=riga_invetario_beni_apg.PROGRESSIVO;
                  aAssInvBeneFattura.dacr:=SYSDATE;
                  aAssInvBeneFattura.utcr:=aUtente;
                  aAssInvBeneFattura.duva:=SYSDATE;
                  aAssInvBeneFattura.utuv:=aUtente;
                  aAssInvBeneFattura.pg_ver_rec:=1;
		  aAssInvBeneFattura.pg_riga:=progressivo_riga;
		  aAssInvBeneFattura.ESERCIZIO:=riga_invetario_beni_apg.ESERCIZIO;
		  aAssInvBeneFattura.PG_BUONO_C_S:=aPgBuonoCaricoScarico;
		  aAssInvBeneFattura.TI_DOCUMENTO:=riga_invetario_beni_apg.TI_DOCUMENTO;
                  insAssInvBeneFattura(aAssInvBeneFattura);

               END LOOP;
            END IF;
         END;
         -- Aggiornamento INVENTARIO_BENI ----------------------------------------------------------

         BEGIN

            -- Lettura del bene per verificare se aggiornare o meno il campo imponibile_ammortamento

            aRecInventarioBeni:=getInventarioBeni(riga_group_invetario_beni_apg.pg_inventario,
                                                  riga_group_invetario_beni_apg.nr_inventario,
                                                  riga_group_invetario_beni_apg.progressivo,
                                                  eseguiLock);

            IF (aRecInventarioBeni.imponibile_ammortamento = aRecInventarioBeni.valore_iniziale +
                                                            aRecInventarioBeni.variazione_piu -
                                                            aRecInventarioBeni.variazione_meno) Then
                 -- inserimento gestione quote di storno

		if (riga_group_invetario_beni_apg.FL_TOTALMENTE_SCARICATO!='Y' And (aRecInventarioBeni.valore_ammortizzato!=0) And (aRecInventarioBeni.valore_ammortizzato!=(aRecInventarioBeni.valore_iniziale +
                                                            aRecInventarioBeni.variazione_piu -
                                                            aRecInventarioBeni.variazione_meno -
                                                            riga_group_invetario_beni_apg.variazione_meno)) And
                                                            fl_storno ='Y' )Then
			-- controllo che i carichi dell'anno siano inferiori allo scarico che si vuole inserire
			select Nvl(Sum(Nvl(valore_unitario,0)),0) Into tot_carichi_anno
			From buono_carico_scarico_dett dett,tipo_carico_scarico tipo,buono_Carico_Scarico buono
			where
			 dett.esercizio     = aEsercizio And
			 dett.ti_documento  = 'C' And
			 dett.pg_inventario = buono.pg_inventario And
			 dett.esercizio = buono.esercizio And
			 dett.ti_documento = buono.ti_documento And
			 dett.pg_buono_c_s = buono.pg_buono_c_s And
			 tipo.cd_tipo_carico_scarico = buono.cd_tipo_carico_scarico And
			 tipo.FL_BUONO_PER_TRASFERIMENTO ='N' And
			 dett.pg_inventario = aRecInventarioBeni.pg_inventario AND
	                 dett.nr_inventario = aRecInventarioBeni.nr_inventario AND
	                 dett.progressivo   = aRecInventarioBeni.progressivo;
	                 If(tot_carichi_anno < riga_group_invetario_beni_apg.VARIAZIONE_MENO )Then
		   		tot_quote_storno:=(aRecInventarioBeni.valore_ammortizzato*(riga_group_invetario_beni_apg.variazione_meno*100/(aRecInventarioBeni.valore_iniziale +
                                                            aRecInventarioBeni.variazione_piu -
                                                            aRecInventarioBeni.variazione_meno)))/100;
		 		/*Elsif ((aRecInventarioBeni.valore_ammortizzato!=0) And (aRecInventarioBeni.valore_ammortizzato=(aRecInventarioBeni.valore_iniziale +
                                                            aRecInventarioBeni.variazione_piu -
                                                            aRecInventarioBeni.variazione_meno  -
                                                            riga_group_invetario_beni_apg.variazione_meno)))Then
				tot_quote_storno:=riga_group_invetario_beni_apg.variazione_meno;
				*/
			end If;
		 End If;
		 If (TOT_QUOTE_STORNO!=0) Then
		 	Select NVL(Max(PG_RIGA)+1,1) into progressivo_riga
                        FROM   AMMORTAMENTO_BENE_INV
                        WHERE  pg_inventario = riga_group_invetario_beni_apg.pg_inventario AND
                               nr_inventario = riga_group_invetario_beni_apg.nr_inventario AND
                               progressivo = riga_group_invetario_beni_apg.progressivo and
                               esercizio = aEsercizio;
                        Begin
       		  	Select PERC_AMMORTAMENTO,NUMERO_ANNI,NUMERO_ANNO,PERC_PRIMO_ANNO,PERC_SUCCESSIVI,CD_TIPO_AMMORTAMENTO,TI_AMMORTAMENTO
			  Into PERC_AMMORTAMENTO,NUMERO_ANNI,NUMERO_ANNO,PERC_PRIMO_ANNO,PERC_SUCCESSIVI,CD_TIPO,TI_AMM
			  From AMMORTAMENTO_BENE_INV
			  Where
			  	pg_inventario = riga_group_invetario_beni_apg.pg_inventario AND
                                nr_inventario = riga_group_invetario_beni_apg.nr_inventario AND
                                progressivo = riga_group_invetario_beni_apg.progressivo and
                                esercizio < aEsercizio    And
                                Rownum =1;
                           Exception When No_Data_Found Then
                           Begin
                          	 Select PERC_AMMORTAMENTO,NUMERO_ANNI,NUMERO_ANNO,PERC_PRIMO_ANNO,PERC_SUCCESSIVI,CD_TIPO_AMMORTAMENTO,TI_AMMORTAMENTO
			  	Into PERC_AMMORTAMENTO,NUMERO_ANNI,NUMERO_ANNO,PERC_PRIMO_ANNO,PERC_SUCCESSIVI,CD_TIPO,TI_AMM
			  	From AMMORTAMENTO_BENE_INV,ASS_TRASFERIMENTO_BENI_INV trasf
			  	Where
			  	pg_inventario_DEST = riga_group_invetario_beni_apg.pg_inventario AND
                                nr_inventario_DEST = riga_group_invetario_beni_apg.nr_inventario AND
                                progressivo_DEST   = riga_group_invetario_beni_apg.progressivo And
                                pg_inventario      = pg_inventario_origine AND
                                nr_inventario      = nr_inventario_origine AND
                                progressivo        = progressivo_origine And
                                esercizio      < aEsercizio    And
                                Rownum =1;
                                Exception When No_Data_Found Then
                                     Select PERC_PRIMO_ANNO,NUMERO_ANNI,1,PERC_PRIMO_ANNO,PERC_SUCCESSIVI,CD_TIPO_AMMORTAMENTO,TI_AMMORTAMENTO
                                     Into PERC_AMMORTAMENTO,NUMERO_ANNI,NUMERO_ANNO,PERC_PRIMO_ANNO,PERC_SUCCESSIVI,CD_TIPO,TI_AMM
                                     From tipo_ammortamento
                                     Where
                                     cd_tipo_ammortamento =Substr(aRecInventarioBeni.cd_categoria_gruppo,1,1);
                                End;
                           End;
                            -- caso per cui non sono state create le associazioni tipo ammortamento per l'anno di scarico
                            Select Count(*) Into conta
                            From ASS_TIPO_AMM_CAT_GRUP_INV
                            Where
                              cd_tipo_ammortamento = cd_tipo And
                              ti_ammortamento      = ti_amm  And
                              esercizio_competenza = aEsercizio And
                              cd_categoria_gruppo  = aRecInventarioBeni.CD_CATEGORIA_GRUPPO;
                             If  conta = 0 Then
                               INSERT INTO ASS_TIPO_AMM_CAT_GRUP_INV(CD_TIPO_AMMORTAMENTO,TI_AMMORTAMENTO,CD_CATEGORIA_GRUPPO,
						 ESERCIZIO_COMPETENZA,DT_CANCELLAZIONE,UTCR,DACR,
						 UTUV,DUVA,PG_VER_REC)
						 Values
						 (substr(aRecInventarioBeni.CD_CATEGORIA_GRUPPO,1,1),ti_amm,aRecInventarioBeni.CD_CATEGORIA_GRUPPO,
						 aEsercizio, Null,'System',Sysdate,'System',Sysdate,1);
			    End If;
                           aAmmBInv.PG_INVENTARIO:= riga_group_invetario_beni_apg.pg_inventario;
                           aAmmBInv.NR_INVENTARIO:= riga_group_invetario_beni_apg.NR_inventario;
                           aAmmBInv.PROGRESSIVO:= riga_group_invetario_beni_apg.PROGRESSIVO;
                           aAmmBInv.ESERCIZIO:=aEsercizio;
                           aAmmBInv.CD_TIPO_AMMORTAMENTO:=CD_TIPO;
                           aAmmBInv.TI_AMMORTAMENTO:=TI_AMM;
                           aAmmBInv.CD_CATEGORIA_GRUPPO:=aRecInventarioBeni.CD_CATEGORIA_GRUPPO;
                           aAmmBInv.ESERCIZIO_COMPETENZA:=aEsercizio;
                           aAmmBInv.IMPONIBILE_AMMORTAMENTO:=(aRecInventarioBeni.imponibile_ammortamento - riga_group_invetario_beni_apg.variazione_meno);
                           aAmmBInv.IM_MOVIMENTO_AMMORT:=-TOT_QUOTE_STORNO;
                           aAmmBInv.PERC_AMMORTAMENTO:=PERC_AMMORTAMENTO;
                           aAmmBInv.DACR:=Sysdate;
                           aAmmBInv.UTCR:=aUtente;
                           aAmmBInv.DUVA:=Sysdate;
                           aAmmBInv.UTUV:=aUtente;
                           aAmmBInv.PG_VER_REC:=1;
                           aAmmBInv.NUMERO_ANNI:=NUMERO_ANNI;
                           aAmmBInv.NUMERO_ANNO:=NUMERO_ANNO;
                           aAmmBInv.PERC_PRIMO_ANNO:=PERC_PRIMO_ANNO;
                           aAmmBInv.PERC_SUCCESSIVI:=PERC_SUCCESSIVI;
                           aAmmBInv.CD_CDS_UBICAZIONE:=aRecInventarioBeni.CD_CDS;
                           aAmmBInv.CD_UO_UBICAZIONE:=aRecInventarioBeni.CD_UNITA_ORGANIZZATIVA;
                           aAmmBInv.FL_STORNO:='Y';
                           aAmmBInv.PG_RIGA:=PROGRESSIVO_RIGA;
                           aAmmBInv.PG_BUONO_S:=aPgBuonoCaricoScarico;

			  ins_AMMORTAMENTO_BENE_INV(aAmmBInv);
		 End If;

               UPDATE INVENTARIO_BENI
               SET    valore_ammortizzato = valore_ammortizzato - tot_quote_storno,
               	      variazione_meno = variazione_meno + riga_group_invetario_beni_apg.variazione_meno,
                      imponibile_ammortamento = imponibile_ammortamento - riga_group_invetario_beni_apg.variazione_meno,
                      valore_alienazione = riga_group_invetario_beni_apg.tot_alienazione,
                      fl_totalmente_scaricato = riga_group_invetario_beni_apg.fl_totalmente_scaricato,
                      dt_validita_variazione = aDtRegistrazione,
                      duva = SYSDATE,
                      utuv = aUtente,
                      pg_ver_rec = pg_ver_rec + 1
               WHERE  pg_inventario= riga_group_invetario_beni_apg.pg_inventario AND
                      nr_inventario= riga_group_invetario_beni_apg.nr_inventario AND
                      progressivo = riga_group_invetario_beni_apg.progressivo;

            ELSE

               UPDATE INVENTARIO_BENI
               SET    variazione_meno = variazione_meno + riga_group_invetario_beni_apg.VARIAZIONE_MENO,
                      valore_alienazione = riga_group_invetario_beni_apg.TOT_ALIENAZIONE,
                      fl_totalmente_scaricato = riga_group_invetario_beni_apg.FL_TOTALMENTE_SCARICATO,
                      dt_validita_variazione = aDtRegistrazione,
                      duva = SYSDATE,
                      utuv = aUtente,
                      pg_ver_rec = pg_ver_rec + 1
               WHERE  pg_inventario= riga_group_invetario_beni_apg.pg_inventario AND
                      nr_inventario= riga_group_invetario_beni_apg.nr_inventario AND
                      progressivo = riga_group_invetario_beni_apg.progressivo;
                 -- r.p. 13/02/2006 da verificare per segnalare i beni per cui non è stato possibile
                 -- aggiornare l'imponibile ammortamento
                 -- r.p. 7/09/2006 che non abbiano già completato l'ammortamento
	      If  aRecInventarioBeni.fl_ammortamento = 'Y' And aRecInventarioBeni.imponibile_ammortamento!=0  Then
               IF aMessaggioDettSegnala IS NULL THEN
                  aMessaggioDettSegnala:='Bene: ' || riga_group_invetario_beni_apg.pg_inventario || '-' ||
                                         riga_group_invetario_beni_apg.nr_inventario || '-' ||
                                         riga_group_invetario_beni_apg.progressivo || CHR(10);
               ELSE
                  aMessaggioDettSegnala:=aMessaggioDettSegnala ||
                                         'Bene: ' || riga_group_invetario_beni_apg.pg_inventario || '-' ||
                                         riga_group_invetario_beni_apg.nr_inventario || '-' ||
                                         riga_group_invetario_beni_apg.progressivo || CHR(10);

               END IF;
	    End If;
            END IF;

         END;

      END LOOP;

   END;

   -- Cancellazione della tabella di appoggio

   cancellaInventarioBeniApg(localTransId);

   -- Composizione del messaggio

   IF aMessaggioDettSegnala IS NULL THEN
      aMessaggio:=aMessaggioBase;
   ELSE
      aMessaggio:=SUBSTR(aMessaggioBase || CHR(10) || aMessaggioBaseSegnala || CHR(10) || aMessaggioDettSegnala, 1, 3900);
   END IF;

END UpdScaricoInventarioBeni;

-- =================================================================================================
-- Crea un documento di carico di inventario, con i relativi dettagli e le eventuali associazioni
-- con le fatture di acquisto.
-- =================================================================================================
PROCEDURE updCaricoBeniAumentoValFtPas
   (
    localTransId VARCHAR2,
    aPgInventario NUMBER,
    aEsercizio NUMBER,
    aPgBuonoCaricoScarico NUMBER,
    aDsBuonoCaricoScarico VARCHAR2,
    aCdTipoCaricoScarico VARCHAR2,
    aPgFattura number,
    aUtente VARCHAR2,
    aDtRegistrazione DATE,
    aMessaggio IN OUT VARCHAR2
   ) IS

   pg_inv NUMBER;
   cont INTEGER;
   aStatoCoge CHAR(1);
   eseguiLock CHAR(1);
   aMessaggioBase VARCHAR2(100);
   aMessaggioBaseSegnala VARCHAR2(500);
   aMessaggioDettSegnala VARCHAR2(3900);

   aBuonoCaricoScarico BUONO_CARICO_SCARICO%ROWTYPE;
   aBuonoCaricoScaricoDett BUONO_CARICO_SCARICO_DETT%ROWTYPE;
   aAssInvBeneFattura ASS_INV_BENE_FATTURA%ROWTYPE;
   aRecInventarioBeni INVENTARIO_BENI%ROWTYPE;
   progressivo_riga number:=0;
BEGIN

   eseguiLock:='Y';

   -------------------------------------------------------------------------------------------------
   -- Valorizzazione messaggi da esporre in output

   aMessaggioBase:='Operazione completata con successo. ';
   aMessaggioBaseSegnala:='Attenzione, per i seguenti beni non è stato modificato automaticamente ' ||
                          'l''imponibile ammortamento in quanto non risulta allineato al suo valore assestato, ' ||
                          'procedere manualmente.';
   aMessaggioDettSegnala:=NULL;

   -------------------------------------------------------------------------------------------------
   -- Inserimento testata BUONO_CARICO_SCARICO (TIPO = carico)

   aBuonoCaricoScarico:=NULL;

   aBuonoCaricoScarico.pg_inventario:=aPgInventario;
   aBuonoCaricoScarico.ti_documento:=BUONO_CARICO;
   aBuonoCaricoScarico.esercizio:=aEsercizio;
   aBuonoCaricoScarico.pg_buono_c_s:=aPgBuonoCaricoScarico;
   aBuonoCaricoScarico.ds_buono_carico_scarico:=aDsBuonoCaricoScarico;
   aBuonoCaricoScarico.data_registrazione:=aDtRegistrazione;
   aBuonoCaricoScarico.cd_tipo_carico_scarico:=aCdTipoCaricoScarico;
   aBuonoCaricoScarico.dacr:=SYSDATE;
   aBuonoCaricoScarico.utcr:=aUtente;
   aBuonoCaricoScarico.duva:=SYSDATE;
   aBuonoCaricoScarico.utuv:=aUtente;
   aBuonoCaricoScarico.pg_ver_rec:=1;

   insBuonoCaricoScarico(aBuonoCaricoScarico);

   -------------------------------------------------------------------------------------------------
   -- Ciclo inserimento dettaglio BUONO_CARICO_SCARICO_DETT e ASS_INV_BENE_FATTURA

   cont :=0;

   BEGIN

      FOR riga_group_invetario_beni_apg IN
          (SELECT PG_INVENTARIO, NR_INVENTARIO, PROGRESSIVO, SUM(VARIAZIONE_PIU) variazione_piu
           FROM   INVENTARIO_BENI_APG
           WHERE  TRIM(LOCAL_TRANSACTION_ID) = TRIM (localTransId)
               and pg_buono_c_s < 0
           GROUP BY PG_INVENTARIO, NR_INVENTARIO, PROGRESSIVO)

      LOOP

         cont := cont+1;

         -- inserimento su tabella BUONO_CARICO_SCARICO_DETT ---------------------------------------

         BEGIN

            aStatoCoge:='X';

            aBuonoCaricoScaricoDett:=NULL;

            aBuonoCaricoScaricoDett.pg_inventario:=aPgInventario;
            aBuonoCaricoScaricoDett.ti_documento:=BUONO_CARICO;
            aBuonoCaricoScaricoDett.esercizio:=aEsercizio;
            aBuonoCaricoScaricoDett.pg_buono_c_s:=aPgBuonoCaricoScarico;
            aBuonoCaricoScaricoDett.nr_inventario:=riga_group_invetario_beni_apg.NR_INVENTARIO;
            aBuonoCaricoScaricoDett.progressivo:=riga_group_invetario_beni_apg.PROGRESSIVO;
            aBuonoCaricoScaricoDett.intervallo:=cont || '-' || cont;
            aBuonoCaricoScaricoDett.quantita:=1;
            aBuonoCaricoScaricoDett.valore_unitario:=riga_group_invetario_beni_apg.VARIAZIONE_PIU;
            aBuonoCaricoScaricoDett.dacr:=SYSDATE;
            aBuonoCaricoScaricoDett.utcr:=aUtente;
            aBuonoCaricoScaricoDett.duva:=SYSDATE;
            aBuonoCaricoScaricoDett.utuv:=aUtente;
            aBuonoCaricoScaricoDett.pg_ver_rec:=1;
            aBuonoCaricoScaricoDett.stato_coge:=aStatoCoge;
	    aBuonoCaricoScaricoDett.stato_coge_quote:='X';
            insBuonoCaricoScaricoDett(aBuonoCaricoScaricoDett);

         END;

         -- inserimento su tabella ASS_INV_BENE_FATTURA --------------------------------------------

         BEGIN

            FOR riga_invetario_beni_apg IN
                    (SELECT *
                     FROM   INVENTARIO_BENI_APG
                     where  TRIM(LOCAL_TRANSACTION_ID) =TRIM(localTransId)
                        and pg_buono_c_s < 0
                        and PG_INVENTARIO = riga_group_invetario_beni_apg.PG_INVENTARIO
                        and NR_INVENTARIO = riga_group_invetario_beni_apg.NR_INVENTARIO
                        and PROGRESSIVO = riga_group_invetario_beni_apg.PROGRESSIVO)

               LOOP
		Select NVL(Max(PG_RIGA)+1,1) into progressivo_riga
                FROM   ass_inv_bene_fattura;
                  aAssInvBeneFattura:=NULL;

                  aAssInvBeneFattura.cd_cds_fatt_pass:=riga_invetario_beni_apg.CD_CDS;
                  aAssInvBeneFattura.cd_uo_fatt_pass:=riga_invetario_beni_apg.CD_UNITA_ORGANIZZATIVA;
                  aAssInvBeneFattura.esercizio_fatt_pass:=riga_invetario_beni_apg.ESERCIZIO;
                  aAssInvBeneFattura.pg_fattura_passiva:=aPgFattura;
                  aAssInvBeneFattura.progressivo_riga_fatt_pass:=riga_invetario_beni_apg.PROGRESSIVO_RIGA;
                  aAssInvBeneFattura.pg_inventario:=riga_invetario_beni_apg.PG_INVENTARIO;
                  aAssInvBeneFattura.nr_inventario:=riga_invetario_beni_apg.NR_INVENTARIO;
                  aAssInvBeneFattura.progressivo:=riga_invetario_beni_apg.PROGRESSIVO;
                  aAssInvBeneFattura.dacr:=SYSDATE;
                  aAssInvBeneFattura.utcr:=aUtente;
                  aAssInvBeneFattura.duva:=SYSDATE;
                  aAssInvBeneFattura.utuv:=aUtente;
                  aAssInvBeneFattura.pg_ver_rec:=1;
		  aAssInvBeneFattura.pg_riga:=progressivo_riga;
		  aAssInvBeneFattura.ESERCIZIO:=riga_invetario_beni_apg.ESERCIZIO;
		  aAssInvBeneFattura.PG_BUONO_C_S:=aPgBuonoCaricoScarico;
		  aAssInvBeneFattura.TI_DOCUMENTO:=riga_invetario_beni_apg.TI_DOCUMENTO;

                  insAssInvBeneFattura(aAssInvBeneFattura);

               END LOOP;

         END;

         -- Aggiornamento INVENTARIO_BENI ----------------------------------------------------------

         BEGIN

            -- Lettura del bene per verificare se aggiornare o meno il campo imponibile_ammortamento

            aRecInventarioBeni:=getInventarioBeni(riga_group_invetario_beni_apg.pg_inventario,
                                                  riga_group_invetario_beni_apg.nr_inventario,
                                                  riga_group_invetario_beni_apg.progressivo,
                                                  eseguiLock);

            IF (aRecInventarioBeni.imponibile_ammortamento = aRecInventarioBeni.valore_iniziale +
                                                             aRecInventarioBeni.variazione_piu -
                                                             aRecInventarioBeni.variazione_meno) THEN

               UPDATE INVENTARIO_BENI
               SET    variazione_piu = variazione_piu + riga_group_invetario_beni_apg.variazione_piu,
                      imponibile_ammortamento = imponibile_ammortamento + riga_group_invetario_beni_apg.variazione_piu,
                      dt_validita_variazione = aDtRegistrazione,
                      duva = SYSDATE,
                      utuv = aUtente,
                      pg_ver_rec = pg_ver_rec + 1
               WHERE  pg_inventario= riga_group_invetario_beni_apg.pg_inventario AND
                      nr_inventario= riga_group_invetario_beni_apg.nr_inventario AND
                      progressivo = riga_group_invetario_beni_apg.progressivo;
            ELSE

               UPDATE INVENTARIO_BENI
               SET    variazione_piu = variazione_piu + riga_group_invetario_beni_apg.variazione_piu,
                      dt_validita_variazione = aDtRegistrazione,
                      duva = SYSDATE,
                      utuv = aUtente,
                      pg_ver_rec = pg_ver_rec + 1
               WHERE  pg_inventario= riga_group_invetario_beni_apg.pg_inventario AND
                      nr_inventario= riga_group_invetario_beni_apg.nr_inventario AND
                      progressivo = riga_group_invetario_beni_apg.progressivo;
                      -- r.p. 13/02/2006 da verificare per segnalare i beni per cui non è stato possibile
                      -- aggiornare l'ammortamento
		if aRecInventarioBeni.fl_ammortamento = 'Y' Then
               		IF aMessaggioDettSegnala IS NULL THEN
                  		aMessaggioDettSegnala:='Bene: ' || riga_group_invetario_beni_apg.pg_inventario || '-' ||
                                         riga_group_invetario_beni_apg.nr_inventario || '-' ||
                                         riga_group_invetario_beni_apg.progressivo || CHR(10);
               		ELSE
                  		aMessaggioDettSegnala:=aMessaggioDettSegnala ||
                                         'Bene: ' || riga_group_invetario_beni_apg.pg_inventario || '-' ||
                                         riga_group_invetario_beni_apg.nr_inventario || '-' ||
                                         riga_group_invetario_beni_apg.progressivo || CHR(10);
               		END IF;
		end If;
            END IF;

         END;

      END LOOP;

   END;

   -- Cancellazione della tabella di appoggio

   cancellaInventarioBeniApg(localTransId);

   -- Composizione del messaggio

   IF aMessaggioDettSegnala IS NULL THEN
      aMessaggio:=aMessaggioBase;
   ELSE
      aMessaggio:=SUBSTR(aMessaggioBase || CHR(10) || aMessaggioBaseSegnala || CHR(10) || aMessaggioDettSegnala, 1, 3900);
   END IF;

END updCaricoBeniAumentoValFtPas;

-- =================================================================================================
-- Modifica l'associazione tra la categoria di un bene e il tipo di ammortamento
-- =================================================================================================
PROCEDURE updAssTipoAmmCatGruppo
   (
    localTransId VARCHAR2,
    cdTipoAmmortamento VARCHAR2,
    flOrdinario CHAR default 'N',
    flAnticipato CHAR default 'N',
    flAltro CHAR default 'N',
    flRiassociato CHAR default 'N',
    cdTipoRiassociato VARCHAR2,
    flOrdinarioRiassociato CHAR default 'N',
    flAnticipatoRiassociato CHAR default 'N',
    flAltroRiassociato CHAR default 'N',
    aEsercizio NUMBER,
    aUtente VARCHAR2
   ) IS

   cont INTEGER;
   tiTipoAmmortamento CHAR(1);
   flagInserimento BOOLEAN;

BEGIN

   cont:=0;

   -- cancellazione preventiva dei record da aggiornare

   BEGIN

      DELETE ASS_TIPO_AMM_CAT_GRUP_INV
      WHERE  (CD_TIPO_AMMORTAMENTO, CD_CATEGORIA_GRUPPO, ESERCIZIO_COMPETENZA)
             IN (SELECT CD_TIPO_AMMORTAMENTO, CD_CATEGORIA_GRUPPO, ESERCIZIO_COMPETENZA
                 FROM   ASS_TIPO_AMM_CAT_GRUP_INV_APG
                 WHERE  TRIM(LOCAL_TRANSACTION_ID) = TRIM(localTransId));

   END;

   -- inserimento dei record aggiornati

   BEGIN

      FOR riga_ass_ti_amm_cat_grup_apg IN
          (SELECT *
           FROM   ASS_TIPO_AMM_CAT_GRUP_INV_APG
           WHERE  TRIM(LOCAL_TRANSACTION_ID) = TRIM(localTransId))

      LOOP

         FOR cont IN 1 .. 3

         LOOP

            if flOrdinario = 'Y' and cont =1 then
               tiTipoAmmortamento := 'O';
               flagInserimento := TRUE;
            end if;
            if flOrdinario = 'N' and cont =1 then
               tiTipoAmmortamento := 'O';
               flagInserimento := FALSE;
            end if;
            if flAnticipato = 'Y' and cont =2 then
               tiTipoAmmortamento := 'A';
               flagInserimento := TRUE;
            end if;
            if flAnticipato = 'N' and cont =2 then
               tiTipoAmmortamento := 'A';
               flagInserimento := FALSE;
            end if;
            if flAltro = 'Y' and cont =3 then
               tiTipoAmmortamento := 'L';
               flagInserimento := TRUE;
            end if;
            if flAltro = 'N' and cont =3 then
               tiTipoAmmortamento := 'L';
               flagInserimento := FALSE;
            end if;

            if flagInserimento then
               INSERT INTO ASS_TIPO_AMM_CAT_GRUP_INV
                      (CD_TIPO_AMMORTAMENTO, TI_AMMORTAMENTO, CD_CATEGORIA_GRUPPO,
                       ESERCIZIO_COMPETENZA, DT_CANCELLAZIONE,
                       UTCR, DACR, UTUV, DUVA, PG_VER_REC)
               VALUES (cdTipoAmmortamento, tiTipoAmmortamento, riga_ass_ti_amm_cat_grup_apg.CD_CATEGORIA_GRUPPO,
                       aEsercizio, riga_ass_ti_amm_cat_grup_apg.DT_CANCELLAZIONE,
                       aUtente, sysdate, aUtente, sysdate, 1);
            end if;

         END LOOP;

      END LOOP;

   END;

   -- inserimento dei record relativi al tipo riassociato

   BEGIN

      IF flRiassociato = 'Y' THEN

         FOR lCdCategoria IN
             (SELECT distinct CD_CATEGORIA_GRUPPO
              FROM   ASS_TIPO_AMM_CAT_GRUP_INV_APG
              WHERE  TRIM(CD_TIPO_AMMORTAMENTO) =TRIM (cdTipoAmmortamento) AND
                     TRIM(LOCAL_TRANSACTION_ID) =TRIM (localTransId))

         LOOP

            FOR cont IN 1 .. 3

            LOOP

               if flOrdinarioRiassociato = 'Y' and cont =1 then
                  tiTipoAmmortamento := 'O';
                  flagInserimento := TRUE;
               end if;
               if flOrdinarioRiassociato = 'N' and cont =1 then
                  tiTipoAmmortamento := 'O';
                  flagInserimento := FALSE;
               end if;
               if flAnticipatoRiassociato = 'Y' and cont =2 then
                  tiTipoAmmortamento := 'A';
                  flagInserimento := TRUE;
               end if;
               if flAnticipatoRiassociato = 'N' and cont =2 then
                  tiTipoAmmortamento:= 'A';
                  flagInserimento := FALSE;
               end if;
               if flAltroRiassociato = 'Y' and cont =3 then
                  tiTipoAmmortamento:= 'L';
                  flagInserimento  := TRUE;
               end if;
               if flAltroRiassociato = 'N' and cont =3 then
                  tiTipoAmmortamento := 'L';
                  flagInserimento  := FALSE;
               end if;

               IF flagInserimento THEN

                  -- cancellazione preventiva dei record da aggiornare per riassocia

                  delete ASS_TIPO_AMM_CAT_GRUP_INV
                  where  (CD_TIPO_AMMORTAMENTO = cdTipoRiassociato AND
                          TI_AMMORTAMENTO = tiTipoAmmortamento AND
                          CD_CATEGORIA_GRUPPO = lCdCategoria.cd_categoria_gruppo AND
                          ESERCIZIO_COMPETENZA = aEsercizio);

                  INSERT INTO ASS_TIPO_AMM_CAT_GRUP_INV
                         (CD_TIPO_AMMORTAMENTO, TI_AMMORTAMENTO, CD_CATEGORIA_GRUPPO,
                          ESERCIZIO_COMPETENZA, DT_CANCELLAZIONE,
                          UTCR, DACR, UTUV, DUVA, PG_VER_REC)
                  VALUES (cdTipoRiassociato, tiTipoAmmortamento, lCdCategoria.cd_categoria_gruppo,
                          aEsercizio, null, aUtente, sysdate, aUtente, sysdate, 1);

               END IF;

            END LOOP;

         END LOOP;

      END IF;

   END;

   cancellaInventarioBeniApg(localTransId);

END updAssTipoAmmCatGruppo;

-- ====================================== AMMORTAMENTO BENI ========================================

-- =================================================================================================
-- Formatta la descrizione per log errori in ammortamento
-- =================================================================================================
FUNCTION getDesc
   (lRigaAmmBeni V_AMMORTAMENTO_BENI%rowtype) RETURN VARCHAR2 IS
BEGIN
   RETURN 'AMFIN-pg inv.:'||lRigaAmmBeni.PG_INVENTARIO||' nr inv.:'||lRigaAmmBeni.NR_INVENTARIO||
          ' progr.:'||lRigaAmmBeni.PROGRESSIVO;
END getDesc;

-- =================================================================================================
-- Normalizzo il record per escludere quanto movimentato in anni successivi.
-- I beni totalizzano saldi in modo indipendente dagli esercizi mentre l'ammortamento deve operare
-- sul valore definito al 31/12 dell'esercizio di riferimento
-- Si elimina il flag totalmente scaricato se l'azione è avvenuta in esercizi successivi
-- Si assestano i saldi (imponibile ammortamento) togliendo tutto quanto è stato movimentato da buoni
-- di carico e scarico in esercizio successivo. Questa funzione è fatta solo nel caso per i beni per
-- i quali risulta verificata l'uguaglianza valore_iniziale + variazioni più - variazioni meno = imponibile ammortamento
-- =================================================================================================
PROCEDURE upgRecBenePerAmmortamento
   (
    aRecVAmmortamentoBeni IN OUT V_AMMORTAMENTO_BENI%ROWTYPE,
    aEs NUMBER
   ) IS

   aAmmontare           NUMBER(15,2);
   aStorno              NUMBER(15,2);
   aAmmontare_car       NUMBER(15,2);
   aAmmontare_scar      NUMBER(15,2);
BEGIN

   -- Totalizzazione buoni di carico e scarico fatti in esercizio successivo

   SELECT SUM(DECODE(ti_documento, 'C', valore_unitario, (valore_unitario * -1))) INTO aAmmontare
   FROM   BUONO_CARICO_SCARICO_DETT
   WHERE  pg_inventario = aRecVAmmortamentoBeni.pg_inventario AND
          nr_inventario = aRecVAmmortamentoBeni.nr_inventarIO AND
          progressivo = aRecVAmmortamentoBeni.progressivo AND
          esercizio > aEs
   GROUP BY pg_inventario, nr_inventario, progressivo;

-- stani 13.06.2005 divisione tra carichi e scarichi

   SELECT SUM(DECODE(ti_documento, 'C', valore_unitario, 0)),
          SUM(DECODE(ti_documento, 'S', valore_unitario, 0))
   INTO   aAmmontare_car, aAmmontare_scar
   FROM   BUONO_CARICO_SCARICO_DETT
   WHERE  pg_inventario = aRecVAmmortamentoBeni.pg_inventario AND
          nr_inventario = aRecVAmmortamentoBeni.nr_inventarIO AND
          progressivo = aRecVAmmortamentoBeni.progressivo AND
          esercizio > aEs
   GROUP BY pg_inventario, nr_inventario, progressivo;
 -- r.p. 04/07/2006 totalizzatore storni fatti in esercizi successivi
 begin
  SELECT SUM(nvl(im_movimento_ammort,0))
   INTO   aStorno
   FROM   ammortamento_bene_inv
   WHERE  pg_inventario = aRecVAmmortamentoBeni.pg_inventario AND
          nr_inventario = aRecVAmmortamentoBeni.nr_inventarIO AND
          progressivo = aRecVAmmortamentoBeni.progressivo AND
          fl_storno = 'Y' and
          esercizio > aEs
   GROUP BY pg_inventario, nr_inventario, progressivo;
exception when no_data_found then
  aStorno:=0;
end;
   -- Normalizza record per calcolo inventario
   -- Se trovo record in esercizio successivo forzo fl_totalmente_scaricato a NO.
   -- Se l'uguaglianza valore_iniziale + variazioni più - variazioni meno = imponibile ammortamento è
   -- vera allora tolgo a imponibile ammortamento quanto totalizzato in esercizi successivi

   aRecVAmmortamentoBeni.fl_totalmente_scaricato := 'N';

   IF (aRecVAmmortamentoBeni.valore_iniziale + aRecVAmmortamentoBeni.variazione_piu -
       aRecVAmmortamentoBeni.variazione_meno) = aRecVAmmortamentoBeni.imponibile_ammortamento THEN
      aRecVAmmortamentoBeni.imponibile_ammortamento :=
                aRecVAmmortamentoBeni.imponibile_ammortamento - aAmmontare;

-- STANI 13.06.2005 NORMALIZZAZIONE ANCHE DEI CARICHI/SCARICHI ES. SUCCESSIVO
-- I CARICHI/SCARICHI DI ESERCIZIO SUCCESSIVO SONO ESTRATTI IN VALORE ASSOLUTO
-- E QUINDI SEMPRE SOTTRATTI ALL'ATTUALE PER RECUPERARE LA CIFRA PRESENTE ALL'ESERCIZIO PRECEDENTE

      aRecVAmmortamentoBeni.VARIAZIONE_PIU  := aRecVAmmortamentoBeni.VARIAZIONE_PIU - aAmmontare_CAR;
      aRecVAmmortamentoBeni.VARIAZIONE_MENO := aRecVAmmortamentoBeni.VARIAZIONE_MENO - aAmmontare_SCAR;
      aRecVAmmortamentoBeni.Valore_ammortizzato := aRecVAmmortamentoBeni.Valore_ammortizzato + abs(astorno);
   END IF;

EXCEPTION
   WHEN no_data_found Then
        RETURN;

END upgRecBenePerAmmortamento;

-- =================================================================================================
-- Ammortamento beni
-- =================================================================================================
PROCEDURE ammortamentoBeniInv
   (aPgExec NUMBER,
    aEs NUMBER,
    aCdCds VARCHAR2,
    aUser VARCHAR2,
    aTSNow DATE ) Is

   aRataAmmortamento number(15,2);
   aPrcAmmortamento number(5,2);
   isEseguiAmmortamento boolean;
   aNumAnno number(4);
   progressivo_riga number(3);

   aRecAssInventarioUo ASS_INVENTARIO_UO%ROWTYPE;
   aRecVAmmortamentoBeni V_AMMORTAMENTO_BENI%ROWTYPE;
   aAmmBInv AMMORTAMENTO_BENE_INV%rowtype;

   gen_cur_a GenericCurTyp;
   gen_cur_b GenericCurTyp;

BEGIN
    For aAmmInv In(Select * From AMMORTAMENTO_BENE_INV
  Where CD_CDS_UBICAZIONE       =aCdCds FOR UPDATE NOWAIT) Loop
  Null;
  End Loop;
 For aBeneInv In(Select * From INVENTARIO_BENI
  Where cd_cds =aCdCds For Update NOWAIT) Loop
  	Null;
  End Loop;

   -------------------------------------------------------------------------------------------------
   -- Controllo eseguibilità dell'ammortamento

   IF (aEs IS NULL OR
       aCdCds IS NULL OR
       aUser IS NULL OR
       aTSNow IS NULL) THEN
      IBMUTL200.logErr(aPgExec, DBMS_UTILITY.FORMAT_ERROR_STACK,'Parametri non definiti per ammortamento',null);
      RETURN;
   END IF;

  LOCK TABLE TIPO_AMMORTAMENTO IN EXCLUSIVE MODE NOWAIT;
  LOCK TABLE ASS_TIPO_AMM_CAT_GRUP_INV IN EXCLUSIVE MODE NOWAIT;

   -------------------------------------------------------------------------------------------------
   -- Carico tutti i beni che devono essere ammortizzati nell'esercizo in esame cioe, tutti i beni
   -- per cui risulta essere che il flag AMMORTAMENTO = 'Y' e per cui risulta esistere un associazione
   -- valida con un tipo di ammortamento.

   BEGIN

      -- Ciclo principale per inventario -----------------------------------------------------------

      OPEN gen_cur_a FOR

           SELECT DISTINCT pg_inventario
           FROM   ASS_INVENTARIO_UO
           WHERE  cd_cds = aCdCds
           ORDER BY pg_inventario;

      LOOP

         FETCH gen_cur_a INTO
               aRecAssInventarioUo.pg_inventario;

         EXIT WHEN gen_cur_a%NOTFOUND;

         BEGIN

            -- Ciclo secondario su V_AMMORTAMENTO_BENI ---------------------------------------------

            OPEN gen_cur_b FOR

                 SELECT *
                 FROM   V_AMMORTAMENTO_BENI
                 WHERE  pg_inventario = aRecAssInventarioUo.pg_inventario AND
                        (esercizio_competenza = aEs OR esercizio_competenza IS NULL) AND
                        esercizio_carico_bene <= aEs;

            LOOP

               savepoint CNRCTB400_SP_001;

               BEGIN

                  FETCH gen_cur_b INTO
                        aRecVAmmortamentoBeni;

                  EXIT WHEN gen_cur_b%NOTFOUND;

	     	  -- controlli per esecuzione dell'ammortamento -----------------------------------

                  -- Si processano i soli record che non hanno subito ammortamento nell'esercizio selezionato

	    	  IF ChkBeneAmmortato(aRecVAmmortamentoBeni, aEs) THEN

                     -- Controllo se esiste un tipo di ammortamento associato al gruppo categoria. Se
                     -- tale tipo non è definito per il bene si verifica che lo sia e univocamente per
                     -- il suo GRUPPO di appartenenza

                     chkEsisteTipoAmm (aRecVAmmortamentoBeni, aEs);

                     -- Stabilisco se devo usare aliquota del primo anno o di anni successivi.
                     -- Se vale 0 sollevo eccezione

                     IF aRecVAmmortamentoBeni.esercizio_carico_bene = aEs THEN
                        aPrcAmmortamento := aRecVAmmortamentoBeni.PERC_PRIMO_ANNO;
                     ELSE
                        aPrcAmmortamento := aRecVAmmortamentoBeni.PERC_SUCCESSIVI;
                     END IF;
                     IF (aPrcAmmortamento = 0 OR aPrcAmmortamento IS NULL) THEN
                        IBMERR001.RAISE_ERR_GENERICO('La percentuale di ammortamento per il bene risulta pari a 0');
                     END IF;

                     -- Normalizzo il record per escludere quanto movimentato in anni successivi

                     upgRecBenePerAmmortamento(aRecVAmmortamentoBeni, aEs);

                     -- Si processano i soli record che hanno:
                     -- imponibile_ammortamento > 0
                     -- fl_totalmente_scaricato = 'N'
                     -- valore_ammortizzato < imponibile_ammortamento

                     IF (aRecVAmmortamentoBeni.valore_ammortizzato > aRecVAmmortamentoBeni.imponibile_ammortamento) And
                      		aRecVAmmortamentoBeni.fl_totalmente_scaricato = 'N'  THEN
                        IBMERR001.RAISE_WRN_GENERICO('Il valore ammortizzato è maggiore dell''imponibile ammortamento');
                     END IF;

                     IF (aRecVAmmortamentoBeni.valore_iniziale + aRecVAmmortamentoBeni.variazione_piu -
                         aRecVAmmortamentoBeni.variazione_meno = 0 AND
                         aRecVAmmortamentoBeni.imponibile_ammortamento > 0 AND
                         aRecVAmmortamentoBeni.fl_totalmente_scaricato = 'N') THEN
                        IBMERR001.RAISE_WRN_GENERICO('L''assestato del bene vale 0 e risulta non totalmente scaricato e con imponibile ammortamento > 0');
                     END IF;


                     IF (aRecVAmmortamentoBeni.imponibile_ammortamento > 0 AND
                         aRecVAmmortamentoBeni.fl_totalmente_scaricato = 'N' AND
                         aRecVAmmortamentoBeni.valore_ammortizzato < aRecVAmmortamentoBeni.imponibile_ammortamento) THEN
		begin
                        -- Calcolo dell'importo della rata di ammortamento

                        aRataAmmortamento:=(aRecVAmmortamentoBeni.imponibile_ammortamento * aPrcAmmortamento /100 );

                        -- Controllare se il bene può essere ammortizzato dell'importo della rata calcolata precedentemente.
                        -- Si posssono verificare i seguenti casi:
                        --   a) la rata di ammortamento calcolata con la percentuale stabilita è troppo
                        --      alta VALORE_AMMORTIZZATO + rata_ammortamento > IMPONIBILE_AMMORTAMENTO
                        --      In questo caso il bene viene ammortizzato di un valore pari allo scarto cosi calcolato:
                        --      IMPONIBILE_AMMORTAMENTO - VALORE_AMMORTIZZATO
                        --   b) la rata di ammortamento calcolata con la percentuale stabilita è consistente in quanto risulta:
                        --      VALORE_AMMORTIZZATO + rata_ammortamento <= IMPONIBILE_AMMORTAMENTO (valore da ammortizzare),
                        --      in questo caso il bene viene ammortizzato con un importo pari alla rata di ammortamento in esame.
                        --  Per tutti i casi in cui il bene deve subire una rata di ammortamento si deve aggiornare il :
                        --  1) VALORE_AMMORTIZZATO del bene in esame
                        --  2) inserire un movimento di ammortamento relativo alla rata.

                        IF (aRataAmmortamento + aRecVAmmortamentoBeni.valore_ammortizzato >
                            aRecVAmmortamentoBeni.imponibile_ammortamento) THEN
                           aRataAmmortamento:=aRecVAmmortamentoBeni.imponibile_ammortamento -
                                              aRecVAmmortamentoBeni.valore_ammortizzato;
                        END IF;

                        -- esegue aggiornamento del valore_ammortizzato del bene in esame

                        updBene(aRecVAmmortamentoBeni.pg_inventario,
                                aRecVAmmortamentoBeni.nr_inventario,
                                aRecVAmmortamentoBeni.progressivo,
                                aRataAmmortamento,
                                aTSNow,
                                aUser);

                        -- Calcola il numero di anno di ammortamento

                        SELECT NVL(COUNT(*)+1,1) into aNumAnno
                        FROM   AMMORTAMENTO_BENE_INV
                        WHERE  pg_inventario = aRecVAmmortamentoBeni.pg_inventario AND
                               nr_inventario = aRecVAmmortamentoBeni.nr_inventario AND
                               progressivo = aRecVAmmortamentoBeni.progressivo And
                               fl_storno ='N';

                        -- esegue l'inserimento della rata di ammortamento

                        aAmmBInv.pg_inventario:=aRecVAmmortamentoBeni.pg_inventario;
                        aAmmBInv.nr_inventario:=aRecVAmmortamentoBeni.nr_inventario;
                        aAmmBInv.progressivo:=aRecVAmmortamentoBeni.progressivo;
                        aAmmBInv.esercizio:=aEs;
                        aAmmBInv.cd_tipo_ammortamento:=aRecVAmmortamentoBeni.cd_tipo_ammortamento;
                        aAmmBInv.ti_ammortamento:=aRecVAmmortamentoBeni.ti_ammortamento;
                        aAmmBInv.cd_categoria_gruppo:=aRecVAmmortamentoBeni.cd_categoria_gruppo;
                        aAmmBInv.esercizio_competenza:=aRecVAmmortamentoBeni.esercizio_competenza;
                        aAmmBInv.imponibile_ammortamento:=aRecVAmmortamentoBeni.imponibile_ammortamento;
                        aAmmBInv.im_movimento_ammort:=aRataAmmortamento;
                        aAmmBInv.perc_ammortamento:=aPrcAmmortamento;
                        aAmmBInv.numero_anni:=aRecVAmmortamentoBeni.numero_anni;
                        aAmmBInv.numero_anno:=aNumAnno;
                        aAmmBInv.perc_primo_anno:=aRecVAmmortamentoBeni.perc_primo_anno;
                        aAmmBInv.perc_successivi:=aRecVAmmortamentoBeni.perc_successivi;
                        aAmmBInv.cd_cds_ubicazione:=aRecVAmmortamentoBeni.cd_cds;
                        aAmmBInv.cd_uo_ubicazione:=aRecVAmmortamentoBeni.cd_unita_organizzativa;
                        aAmmBInv.duva:=aTSNow;
                        aAmmBInv.utuv:=aUser;
                        aAmmBInv.dacr:=aTSNow;
                        aAmmBInv.utcr:=aUser;
                        aAmmBInv.pg_ver_rec:=1;
                          -- inserimento gestione quote di storno
                        Begin
	                        Select NVL(Max(PG_RIGA)+1,1) into progressivo_riga
	                        FROM   AMMORTAMENTO_BENE_INV
	                        WHERE  pg_inventario = aRecVAmmortamentoBeni.pg_inventario AND
	                               nr_inventario = aRecVAmmortamentoBeni.nr_inventario AND
	                               progressivo = aRecVAmmortamentoBeni.progressivo and
	                               esercizio = aEs;
                        Exception When No_Data_Found Then
                           progressivo_riga:=1;
                        End;
			aAmmBInv.pg_riga:=progressivo_riga;
                        aAmmBInv.fl_storno :='N';

                        ins_AMMORTAMENTO_BENE_INV(aAmmBInv);
 		EXCEPTION  When others THEN

			dbms_output.put_line('errore '||aRecVAmmortamentoBeni.nr_inventario);
               	END;
                     END IF;

	    	  ELSE -- Controllo se il bene risulta già ammortizzato nell'esercio in esame;

                     IBMERR001.RAISE_ERR_GENERICO('Il bene risulta aver subito ammortamento nell''anno ' || aEs );

                  END IF; -- Controllo se il bene risulta già ammortizzato nell'esercio in esame;

               EXCEPTION

                  WHEN others THEN
                       Rollback to savepoint CNRCTB400_SP_001;
                       IF sqlCode = IBMERR001.WRN_GENERICO THEN
                          IBMUTL200.logWar(aPgExec, DBMS_UTILITY.FORMAT_ERROR_STACK,null,getDesc(aRecVAmmortamentoBeni));
                       ELSE
                          IBMUTL200.logErr(aPgExec, DBMS_UTILITY.FORMAT_ERROR_STACK,null,getDesc(aRecVAmmortamentoBeni));
                       END IF;

               END;

            END LOOP;

            CLOSE gen_cur_b;

         END;

      END LOOP;

      CLOSE gen_cur_a;
	IBMUTL200.logInf(aPgExec, 'FINE','Termine esecuzione','Termine esecuzione');
   END;


END ammortamentoBeniInv;

-- =================================================================================================
-- Controllo che il bene non risulti essere stato ammortato nell'esercizio
-- =================================================================================================
FUNCTION chkBeneAmmortato
   (
    aRecVAmmortamentoBeni V_AMMORTAMENTO_BENI%ROWTYPE,
    aEs NUMBER
   ) RETURN BOOLEAN IS

   aCount INTEGER;

BEGIN

   SELECT COUNT(*) INTO aCount
   FROM   DUAL
   WHERE  EXISTS
          (SELECT 1
           FROM   AMMORTAMENTO_BENE_INV
           WHERE  pg_inventario = aRecVAmmortamentoBeni.pg_inventario AND
                  nr_inventario = aRecVAmmortamentoBeni.nr_inventario AND
                  progressivo = aRecVAmmortamentoBeni.progressivo AND
                  esercizio = aEs  And
                  fl_storno = 'N');

   IF aCount = 0 THEN
      RETURN TRUE;
   ELSE
      RETURN FALSE;
   END IF;

END chkBeneAmmortato;

-- =================================================================================================
-- Controllo se esiste un tipo di ammortamento associato al gruppo categoria. Se tale tipo non è
-- definito per il bene si verifica che lo sia e univocamente per il suo GRUPPO di appartenenza
-- =================================================================================================
PROCEDURE chkEsisteTipoAmm
   (
    aRecVAmmortamentoBeni IN OUT V_AMMORTAMENTO_BENI%ROWTYPE,
    aEs NUMBER
    ) IS

   aRecTipoAmmortamento TIPO_AMMORTAMENTO%ROWTYPE;

BEGIN

   -- Nella vista V_AMMORTAMENTO_BENI se risulta che il campo CD_TIPO_AMMORTAMENTO IS NULL ->
   -- il bene non risulta associato ad un tipo di ammortamento

   IF aRecVAmmortamentoBeni.cd_tipo_ammortamento IS NULL THEN

      -- Cerca un tipo ammortamento collegato al gruppo inventariale del bene

      BEGIN

         IF aRecVAmmortamentoBeni.ti_ammortamento_bene IS NULL THEN

            SELECT * INTO aRecTipoAmmortamento
            FROM   TIPO_AMMORTAMENTO aTA
            WHERE  EXISTS
                      (SELECT 1
                       FROM   ASS_TIPO_AMM_CAT_GRUP_INV
                       WHERE  cd_categoria_gruppo = aRecVAmmortamentoBeni.cd_categoria_gruppo AND
                              cd_tipo_ammortamento = aTA.cd_tipo_ammortamento AND
                              ti_ammortamento = aTA.ti_ammortamento AND
                              esercizio_competenza = aEs);

         ELSE

            SELECT * INTO aRecTipoAmmortamento
            FROM   TIPO_AMMORTAMENTO aTA
            WHERE  aTA.ti_ammortamento = aRecVAmmortamentoBeni.ti_ammortamento_bene AND
                   EXISTS
                      (SELECT 1
                       FROM   ASS_TIPO_AMM_CAT_GRUP_INV
                       WHERE  cd_categoria_gruppo = aRecVAmmortamentoBeni.cd_categoria_gruppo AND
                              cd_tipo_ammortamento = aTA.cd_tipo_ammortamento AND
                              ti_ammortamento = aTA.ti_ammortamento AND
                              esercizio_competenza = aEs);

         END IF;

      EXCEPTION

         WHEN too_many_rows THEN
              IBMERR001.RAISE_ERR_GENERICO('Tipo ammortamento non definito per il bene: il gruppo inventariale a cui il bene appartiene ha più di un tipo di ammortamento definito');
         WHEN no_data_found THEN
              IBMERR001.RAISE_ERR_GENERICO('Tipo ammortamento non definito per il bene: il gruppo inventariale a cui il bene appartiene non ha tipo di ammortamento definito');
      END;

      aRecVAmmortamentoBeni.ESERCIZIO_COMPETENZA:=aEs;
      aRecVAmmortamentoBeni.CD_TIPO_AMMORTAMENTO:=aRecTipoAmmortamento.cd_tipo_ammortamento;
      aRecVAmmortamentoBeni.TI_AMMORTAMENTO:=aRecTipoAmmortamento.ti_ammortamento;
      aRecVAmmortamentoBeni.DT_CANCELLAZIONE:= aRecTipoAmmortamento.dt_cancellazione;
      aRecVAmmortamentoBeni.PERC_PRIMO_ANNO:=aRecTipoAmmortamento.perc_primo_anno;
      aRecVAmmortamentoBeni.PERC_SUCCESSIVI:=aRecTipoAmmortamento.perc_successivi;
      aRecVAmmortamentoBeni.NUMERO_ANNI:= aRecTipoAmmortamento.numero_anni;

   END IF;

END chkEsisteTipoAmm;

-- =================================================================================================
-- Annullamento ammortamento beni
-- =================================================================================================
PROCEDURE annullaAmmortBeniInv
   (aPgExec NUMBER,
    aEs NUMBER,
    aCdCds VARCHAR2,
    aUser VARCHAR2,
    aTSNow DATE ) IS

BEGIN
  For aAmmInv In(Select * From AMMORTAMENTO_BENE_INV
  Where CD_CDS_UBICAZIONE       =aCdCds FOR UPDATE NOWAIT) Loop
  Null;
  End Loop;
 For aBeneInv In(Select * From INVENTARIO_BENI
  Where cd_cds =aCdCds FOR UPDATE NOWAIT) Loop
  	Null;
  End Loop;

   IF aEs IS NULL OR aCdCds IS NULL OR aUser IS NULL OR aTSNow IS NULL THEN
      IBMUTL200.logErr(aPgExec, DBMS_UTILITY.FORMAT_ERROR_STACK,
                       'Parametri non definiti per annulamento ammortamento', NULL);
      RETURN;
   END IF;


   LOCK TABLE TIPO_AMMORTAMENTO IN EXCLUSIVE MODE NOWAIT;
   LOCK TABLE ASS_TIPO_AMM_CAT_GRUP_INV IN EXCLUSIVE MODE NOWAIT;

   FOR aAmmEs IN
       (SELECT *
        FROM   AMMORTAMENTO_BENE_INV
        WHERE  esercizio = aEs AND
               cd_cds_ubicazione = aCdCds And
               fl_storno = 'N')
   LOOP

      updBene(aAmmEs.pg_inventario,
              aAmmEs.nr_inventario,
              aAmmEs.progressivo,
              0 - aAmmEs.im_movimento_ammort,
              aTSNow,
              aUser);

      DELETE FROM AMMORTAMENTO_BENE_INV
      WHERE  pg_inventario = aAmmEs.pg_inventario AND
             nr_inventario = aAmmEs.nr_inventario AND
             progressivo = aAmmEs.progressivo AND
             esercizio = aEs  And
             fl_storno = 'N';

   END LOOP;
Exception When Others Then
  Dbms_Output.put_line ('errore '||sqlcode||' '||Sqlerrm);
END annullaAmmortBeniInv;

-- =================================================================================================
-- Aggiornamento bene per esecuzione o annullamento di ammortamento
-- =================================================================================================
PROCEDURE updBene
   (aPgInv NUMBER,
    aNrInv NUMBER,
    aProgressivo NUMBER,
    aDelta NUMBER,
    aTSNow DATE,
    aUser VARCHAR2) IS

BEGIN

   UPDATE inventario_beni
   SET    valore_ammortizzato = valore_ammortizzato + aDelta,
          duva = aTSNow,
          utuv = aUser,
          pg_ver_rec = pg_ver_rec +1
   WHERE  pg_inventario = aPgInv AND
          nr_inventario = aNrInv AND
          progressivo = aProgressivo;

END updBene;

-- ===================================== TRASFERIMENTO BENI ========================================

-- =================================================================================================
-- Esecuzione del trasferimento beni
-- =================================================================================================
PROCEDURE eseguiTrasferisciBeni
   (inPgInventarioOri NUMBER,
    aPgInventarioDest NUMBER,
    inEsercizio NUMBER,
    inCdTipoCarico VARCHAR2,
    aFlTrasferisciTutto VARCHAR2,
    inUtente VARCHAR2,
    aPgBuonoCarico IN OUT NUMBER,
    aPgBuonoScarico IN OUT NUMBER,
    aNumDettaglio IN OUT NUMBER,
    inRecBuonoScarico BUONO_CARICO_SCARICO%ROWTYPE,
    aCdCdsUbicazione VARCHAR2,
    aCdUoUbicazione VARCHAR2,
    aCdUbicazione VARCHAR2,
    inDtRegistrazione DATE) IS

   esisteBenePrincipale CHAR(1);
   eseguiLock CHAR(1);
   aTmpPgBuonoCarico NUMBER(10);
   aTmpPgBuonoScarico NUMBER(10);
   aTmpNumDettaglio NUMBER(10);
   dataOdierna DATE;

   i BINARY_INTEGER;
   j BINARY_INTEGER;

   aRecInventarioBeniOri INVENTARIO_BENI%ROWTYPE;
   aRecInventarioBeniDest INVENTARIO_BENI%ROWTYPE;
   aRecBuonoCarico BUONO_CARICO_SCARICO%ROWTYPE;
   aRecBuonoCaricoDett BUONO_CARICO_SCARICO_DETT%ROWTYPE;
   aRecBuonoScarico BUONO_CARICO_SCARICO%ROWTYPE;
   aRecBuonoScaricoDett BUONO_CARICO_SCARICO_DETT%ROWTYPE;
   aRecNumeratoreBuonoCS NUMERATORE_BUONO_C_S%ROWTYPE;
   aRecAssTrasferimentoBeniInv ASS_TRASFERIMENTO_BENI_INV%ROWTYPE;
   aRecCategoriaGruppoInvent CATEGORIA_GRUPPO_INVENT%ROWTYPE;
   tipo_amm char(1);
BEGIN

   IF beniDaTrasf_tab.COUNT = 0 THEN
      IBMERR001.RAISE_ERR_GENERICO ('Matrice gruppo beni da trasferire vuota');
   END IF;

   esisteBenePrincipale:='N';
   eseguiLock:='Y';
   dataOdierna:=SYSDATE;

   -- Si controlla se il primo record riguarda un bene principale

   IF beniDaTrasf_tab(1).tProgressivo = 0 THEN
      esisteBenePrincipale:='Y';
   END IF;

   -- Inizializzazione default per testata e dettagli buono di carico e scarico

   aRecBuonoScarico:=inRecBuonoScarico;
   If(inPgInventarioOri!=aPgInventarioDest ) Then
        aRecBuonoScarico.ds_buono_carico_scarico:=Substr(inRecBuonoScarico.ds_buono_carico_scarico,1,70)||' Trasferimento a Uo: '||aCdUoUbicazione;
   End If;
   aRecBuonoScarico.dacr:=dataOdierna;
   aRecBuonoScarico.duva:=dataOdierna;
   aRecBuonoScaricoDett.pg_inventario:=aRecBuonoScarico.pg_inventario;
   aRecBuonoScaricoDett.ti_documento:=aRecBuonoScarico.ti_documento;
   aRecBuonoScaricoDett.esercizio:=aRecBuonoScarico.esercizio;
   aRecBuonoScaricoDett.quantita:=1;
   aRecBuonoScaricoDett.dacr:=aRecBuonoScarico.dacr;
   aRecBuonoScaricoDett.utcr:=aRecBuonoScarico.utcr;
   aRecBuonoScaricoDett.duva:=aRecBuonoScarico.duva;
   aRecBuonoScaricoDett.utuv:=aRecBuonoScarico.utuv;
   aRecBuonoScaricoDett.pg_ver_rec:=aRecBuonoScarico.pg_ver_rec;
   aRecBuonoCarico:=inRecBuonoScarico;
   aRecBuonoCarico.pg_inventario:=aPgInventarioDest;
   aRecBuonoCarico.ti_documento:=BUONO_CARICO;
   aRecBuonoCarico.cd_tipo_carico_scarico:=inCdTipoCarico;
   aRecBuonoCarico.dacr:=dataOdierna;
   aRecBuonoCarico.duva:=dataOdierna;
   aRecBuonoCaricoDett.pg_inventario:=aRecBuonoCarico.pg_inventario;
   aRecBuonoCaricoDett.ti_documento:=aRecBuonoCarico.ti_documento;
   aRecBuonoCaricoDett.esercizio:=aRecBuonoCarico.esercizio;
   aRecBuonoCaricoDett.quantita:=1;
   aRecBuonoCaricoDett.dacr:=aRecBuonoCarico.dacr;
   aRecBuonoCaricoDett.utcr:=aRecBuonoCarico.utcr;
   aRecBuonoCaricoDett.duva:=aRecBuonoCarico.duva;
   aRecBuonoCaricoDett.utuv:=aRecBuonoCarico.utuv;
   aRecBuonoCaricoDett.pg_ver_rec:=aRecBuonoCarico.pg_ver_rec;

   -- Resetta il contatore per generare buoni di carico e scarico testata

   aTmpPgBuonoCarico:=aPgBuonoCarico;
   aTmpPgBuonoScarico:=aPgBuonoScarico;
   aTmpNumDettaglio:=aNumDettaglio;
   IF aTmpNumDettaglio > 150 THEN
      aTmpPgBuonoCarico:=0;
      aTmpPgBuonoScarico:=0;
      aTmpNumDettaglio:=0;
   END IF;

   -------------------------------------------------------------------------------------------------
   -- Ciclo di lettura dei beni da trasferire

   BEGIN

      FOR i IN 1 .. beniDaTrasf_tab.COUNT

      LOOP

         -- Leggo ed eseguo il lock sul bene in elaborazione

         aRecInventarioBeniOri:=getInventarioBeni(beniDaTrasf_tab(i).tPgInventario,
                                                  beniDaTrasf_tab(i).tNrInventario,
                                                  beniDaTrasf_tab(i).tProgressivo,
                                                  eseguiLock);

         -- Se il bene risulta totalmente scaricato o ha valore assestato = 0 segnalo errore

         IF (aRecInventarioBeniOri.fl_totalmente_scaricato = 'Y' OR
             aRecInventarioBeniOri.valore_iniziale + aRecInventarioBeniOri.variazione_piu -
             aRecInventarioBeniOri.variazione_meno = 0 ) THEN
            IBMERR001.RAISE_ERR_GENERICO
                ('E'' stato richiesto il trasferimento di un bene totalmente scaricato o con assestato = 0');
         END IF;

         -- Controlli consistenza ----------------------------------------------------------------

         -- Trasferimento stessa UO

         IF inPgInventarioOri = aPgInventarioDest THEN

            if(beniDaTrasf_tab(i).tCategoriaNew is not null) then
                IF (beniDaTrasf_tab(i).tCategoriaNew =aRecInventarioBeniOri.cd_categoria_gruppo) THEN
                  IBMERR001.RAISE_ERR_GENERICO
                     ('Trasferimento per cambio categoria non necessario per il bene '||aRecInventarioBeniOri.nr_inventario);
                END IF;
                if  (aRecInventarioBeniOri.FL_AMMORTAMENTO='Y'  And aRecInventarioBeniOri.valore_ammortizzato!=0) then
                   aRecCategoriaGruppoInvent:=getCategoriaGruppoInvent(beniDaTrasf_tab(i).tCategoriaNew);
                   if(aRecCategoriaGruppoInvent.fl_ammortamento='N') then
                   		 IBMERR001.RAISE_ERR_GENERICO
                     ('Trasferimento per cambio categoria su categoria non ammortizzabile, non coerente per il bene '||aRecInventarioBeniOri.nr_inventario);
                   end if;
                end if;
  					else  -- if(beniDaTrasf_tab(i).tCategoriaNew is not null) then

            -- Esiste bene principale

            IF esisteBenePrincipale = 'Y' THEN

               -- Tutti i beni devono presentare il flag trasferisci come bene principale = 'N'

               IF beniDaTrasf_tab(i).tFlTrasfComePrincipale = 'Y' THEN
                  IBMERR001.RAISE_ERR_GENERICO
                     ('Trasferimento nello stesso inventario. E'' indicato un bene principale e si richiede ' ||
                      'il trasferimento come bene principale');
               END IF;

               -- Tutti i beni devono diventare accessori dello stesso bene di destinazione e questo non può
               -- essere nullo

               IF (beniDaTrasf_tab(i).tPgInventarioPrincipale IS NULL OR
                   beniDaTrasf_tab(i).tNrInventarioPrincipale IS NULL) THEN
                  IBMERR001.RAISE_ERR_GENERICO
                     ('Trasferimento nello stesso inventario. E'' indicato un bene principale e non è richiesto ' ||
                      'il trasferimento come bene principale e non è indicato il bene di destinazione');
               END IF;

               IF (beniDaTrasf_tab(i).tPgInventarioPrincipale != beniDaTrasf_tab(1).tPgInventarioPrincipale OR
                   beniDaTrasf_tab(i).tNrInventarioPrincipale != beniDaTrasf_tab(1).tNrInventarioPrincipale) THEN
                  IBMERR001.RAISE_ERR_GENERICO
                     ('Trasferimento nello stesso inventario. E'' indicato un bene principale e non è richiesto ' ||
                      'il trasferimento come bene principale e il bene di destinazione non è lo stesso per tutti i records');
               END IF;

            -- Non esiste bene principale

            ELSE

               -- Se l'accessorio non è da trasferire come principale deve esistere l'indicazione del bene
               -- di destinazione

               IF beniDaTrasf_tab(i).tFlTrasfComePrincipale = 'N' THEN
                  IF (beniDaTrasf_tab(i).tPgInventarioPrincipale IS NULL OR
                      beniDaTrasf_tab(i).tNrInventarioPrincipale IS NULL) THEN
                     IBMERR001.RAISE_ERR_GENERICO
                        ('Trasferimento nello stesso inventario. Non è indicato un bene principale e non è richiesto ' ||
                         'il trasferimento come bene principale e non è indicato il bene di destinazione');
                  END IF;
               END IF;
            END IF;
           end if;-- else di if(beniDaTrasf_tab(i).tCategoriaNew is not null) then
         END IF;

         ------------------------------------  BUONO DI SCARICO ----------------------------------

         -- Creazione buono di scarico testata ---------------------------------------------------

         -- La testata del buono di scarico è generata solo se è la prima volta o se il numero delle
         -- righe di dettaglio è maggiore di 200

         IF aTmpPgBuonoScarico = 0 THEN
            aRecNumeratoreBuonoCS:=getNumeratoreBuonoCS(aRecBuonoScarico.pg_inventario,
                                                        aRecBuonoScarico.ti_documento,
                                                        aRecBuonoScarico.esercizio,
                                                        inUtente,
                                                        dataOdierna,
                                                        eseguiLock);
            aRecBuonoScarico.pg_buono_c_s:=aRecNumeratoreBuonoCS.corrente + 1;
            aTmpPgBuonoScarico:=aRecBuonoScarico.pg_buono_c_s;
            upgCorrenteNumeratoreBuonoCS(aRecBuonoScarico.pg_inventario,
                                         aRecBuonoScarico.ti_documento,
                                         aRecBuonoScarico.esercizio,
                                         dataOdierna,
                                         inUtente);

            insBuonoCaricoScarico(aRecBuonoScarico);
         ELSE
            aRecBuonoScarico.pg_buono_c_s:=aTmpPgBuonoScarico;
         END IF;

         -- Creazione buono di scarico dettaglio -------------------------------------------------

         aTmpNumDettaglio:=aTmpNumDettaglio + 1;
         aRecBuonoScaricoDett.pg_buono_c_s:=aRecBuonoScarico.pg_buono_c_s;
         aRecBuonoScaricoDett.nr_inventario:=beniDaTrasf_tab(i).tNrInventario;
         aRecBuonoScaricoDett.progressivo:=beniDaTrasf_tab(i).tProgressivo;
         aRecBuonoScaricoDett.intervallo:=aTmpNumDettaglio || '-' || aTmpNumDettaglio;
         aRecBuonoScaricoDett.valore_unitario:=aRecInventarioBeniOri.valore_iniziale +
                                               aRecInventarioBeniOri.variazione_piu -
                                               aRecInventarioBeniOri.variazione_meno;
         IF inPgInventarioOri = aPgInventarioDest THEN
            if(beniDaTrasf_tab(i).tCategoriaNew is not null) then
            	aRecBuonoScaricoDett.stato_coge:='N';
            else
            	aRecBuonoScaricoDett.stato_coge:='X';
            end if;
         ELSE
            aRecBuonoScaricoDett.stato_coge:='N';
         END IF;
         aRecBuonoScaricoDett.stato_coge_quote:='X';
         insBuonoCaricoScaricoDett(aRecBuonoScaricoDett);

         -- Aggiornamento del bene ---------------------------------------------------------------

         IF (aRecInventarioBeniOri.imponibile_ammortamento = aRecInventarioBeniOri.valore_iniziale +
                                                            aRecInventarioBeniOri.variazione_piu -
                                                            aRecInventarioBeniOri.variazione_meno) THEN

            UPDATE INVENTARIO_BENI
            SET    fl_totalmente_scaricato = 'Y',
                   dt_validita_variazione = inDtRegistrazione,-- r.p. 13/02/2006 vecchia versione erroneamente veniva imputata TRUNC(dataOdierna),
                   imponibile_ammortamento = 0,
                   variazione_meno = variazione_meno + aRecBuonoScaricoDett.valore_unitario,
                   duva = dataOdierna,
                   utuv = inUtente,
                   pg_ver_rec = pg_ver_rec + 1
            WHERE  pg_inventario = aRecBuonoScaricoDett.pg_inventario AND
                   nr_inventario = aRecBuonoScaricoDett.nr_inventario AND
                   progressivo = aRecBuonoScaricoDett.progressivo;

         ELSE

            UPDATE INVENTARIO_BENI
            SET    fl_totalmente_scaricato = 'Y',
                   dt_validita_variazione = inDtRegistrazione,-- r.p. 13/02/2006 vecchia versione erroneamente veniva imputata TRUNC(dataOdierna),
                   variazione_meno = variazione_meno + aRecBuonoScaricoDett.valore_unitario,
                   duva = dataOdierna,
                   utuv = inUtente,
                   pg_ver_rec = pg_ver_rec + 1
            WHERE  pg_inventario = aRecBuonoScaricoDett.pg_inventario AND
                   nr_inventario = aRecBuonoScaricoDett.nr_inventario AND
                   progressivo = aRecBuonoScaricoDett.progressivo;

         END IF;

         ------------------------------------  BUONO DI CARICO -----------------------------------

         -- Creazione buono di carico testata ----------------------------------------------------

         -- La testata del buono di scarico è generata solo se è la prima volta o se il numero delle
         -- righe di dettaglio è maggiore di 200

         IF aTmpPgBuonoCarico = 0 THEN
            aRecNumeratoreBuonoCS:=getNumeratoreBuonoCS(aRecBuonoCarico.pg_inventario,
                                                        aRecBuonoCarico.ti_documento,
                                                        aRecBuonoCarico.esercizio,
                                                        inUtente,
                                                        dataOdierna,
                                                        eseguiLock);
            aRecBuonoCarico.pg_buono_c_s:=aRecNumeratoreBuonoCS.corrente + 1;
            aTmpPgBuonoCarico:=aRecBuonoCarico.pg_buono_c_s;
            upgCorrenteNumeratoreBuonoCS(aRecBuonoCarico.pg_inventario,
                                         aRecBuonoCarico.ti_documento,
                                         aRecBuonoCarico.esercizio,
                                         dataOdierna,
                                         inUtente);
                If(inPgInventarioOri!=aPgInventarioDest) Then
                        aRecBuonoCarico.ds_buono_carico_scarico:=Substr(inRecBuonoScarico.ds_buono_carico_scarico,1,70)||' Trasferimento da Uo: '||aRecInventarioBeniOri.cd_unita_organizzativa;
                End If;
            insBuonoCaricoScarico(aRecBuonoCarico);
         ELSE
            aRecBuonoCarico.pg_buono_c_s:=aTmpPgBuonoCarico;
         END IF;

         -- Creazione buono di carico dettaglio --------------------------------------------------

         aRecBuonoCaricoDett.pg_buono_c_s:=aRecBuonoCarico.pg_buono_c_s;

         -- Numero e progressivo inventario

         IF inPgInventarioOri = aPgInventarioDest THEN

           if(beniDaTrasf_tab(i).tCategoriaNew is not null) Then
              if (beniDaTrasf_tab(i).tProgressivo=0 ) then
              		aRecBuonoCaricoDett.nr_inventario:=getNewNrInventario(aRecBuonoCaricoDett.pg_inventario);
              end if;		
              aRecBuonoCaricoDett.progressivo:=beniDaTrasf_tab(i).tProgressivo;
           else
            IF esisteBenePrincipale = 'Y' THEN
               aRecBuonoCaricoDett.nr_inventario:=beniDaTrasf_tab(i).tNrInventarioPrincipale;
               aRecBuonoCaricoDett.progressivo:=getNewProgressivo(aRecBuonoCaricoDett.pg_inventario,
                                                                  aRecBuonoCaricoDett.nr_inventario);
            ELSE
               IF beniDaTrasf_tab(i).tFlTrasfComePrincipale = 'N' THEN
                  aRecBuonoCaricoDett.nr_inventario:=beniDaTrasf_tab(i).tNrInventarioPrincipale;
                  aRecBuonoCaricoDett.progressivo:=getNewProgressivo(aRecBuonoCaricoDett.pg_inventario,
                                                                     aRecBuonoCaricoDett.nr_inventario);
               ELSE
                  aRecBuonoCaricoDett.nr_inventario:=getNewNrInventario(aRecBuonoCaricoDett.pg_inventario);
                  aRecBuonoCaricoDett.progressivo:=0;
               END IF;
            END IF;
           end if;
         ELSE
            IF esisteBenePrincipale = 'Y' THEN
               IF beniDaTrasf_tab(i).tProgressivo = 0 THEN
                  aRecBuonoCaricoDett.nr_inventario:=getNewNrInventario(aRecBuonoCaricoDett.pg_inventario);
                  aRecBuonoCaricoDett.progressivo:=0;
               ELSE
                  aRecBuonoCaricoDett.progressivo:=getNewProgressivo(aRecBuonoCaricoDett.pg_inventario,
                                                                     aRecBuonoCaricoDett.nr_inventario);
               END IF;
            ELSE
               aRecBuonoCaricoDett.nr_inventario:=getNewNrInventario(aRecBuonoCaricoDett.pg_inventario);
               aRecBuonoCaricoDett.progressivo:=0;
            END IF;
         END IF;
         aRecBuonoCaricoDett.intervallo:=aTmpNumDettaglio || '-' || aTmpNumDettaglio;
         aRecBuonoCaricoDett.valore_unitario:=aRecBuonoScaricoDett.valore_unitario;
         IF inPgInventarioOri = aPgInventarioDest THEN
              if(beniDaTrasf_tab(i).tCategoriaNew is not null) Then
              	aRecBuonoCaricoDett.stato_coge:='N';
              else
            		aRecBuonoCaricoDett.stato_coge:='X';
            	end if;
         ELSE
            aRecBuonoCaricoDett.stato_coge:='N';
         END IF;
         aRecBuonoCaricoDett.stato_coge_quote:='X';
         insBuonoCaricoScaricoDett(aRecBuonoCaricoDett);

         -- Creazione del bene -------------------------------------------------------------------

         aRecInventarioBeniDest:=aRecInventarioBeniOri;
         aRecInventarioBeniDest.pg_inventario:=aRecBuonoCaricoDett.pg_inventario;
         aRecInventarioBeniDest.nr_inventario:=aRecBuonoCaricoDett.nr_inventario;
         aRecInventarioBeniDest.progressivo:=aRecBuonoCaricoDett.progressivo;
         aRecInventarioBeniDest.fl_totalmente_scaricato:='N';
         aRecInventarioBeniDest.dt_validita_variazione:=inDtRegistrazione; -- r.p. 13/02/2006 vecchia versione erroneamente veniva imputata TRUNC(dataOdierna),
         IF inPgInventarioOri != aPgInventarioDest THEN
            aRecInventarioBeniDest.cd_cds:=aCdCdsUbicazione;
            aRecInventarioBeniDest.cd_unita_organizzativa:=aCdUoUbicazione;
            aRecInventarioBeniDest.cd_ubicazione:=aCdUbicazione;
         END IF;
         if(beniDaTrasf_tab(i).tCategoriaNew is not null) Then
            select ti_ammortamento into tipo_amm
              from ASS_TIPO_AMM_CAT_GRUP_INV
              where esercizio_competenza = aRecBuonoCaricoDett.esercizio   and
                    cd_categoria_gruppo = beniDaTrasf_tab(i).tCategoriaNew;
            aRecCategoriaGruppoInvent:=getCategoriaGruppoInvent(beniDaTrasf_tab(i).tCategoriaNew);
            aRecInventarioBeniDest.cd_categoria_gruppo:=beniDaTrasf_tab(i).tCategoriaNew;
          	aRecInventarioBeniDest.fl_ammortamento:=aRecCategoriaGruppoInvent.fl_ammortamento;
          	aRecInventarioBeniDest.ti_ammortamento:=tipo_amm;
         end if;
         aRecInventarioBeniDest.dacr:=dataOdierna;
         aRecInventarioBeniDest.utcr:=inUtente;
         aRecInventarioBeniDest.duva:=dataOdierna;
         aRecInventarioBeniDest.utuv:=inUtente;
         aRecInventarioBeniDest.pg_ver_rec:=1;
         aRecInventarioBeniDest.esercizio_carico_bene:=inEsercizio;
         insInventarioBeni(aRecInventarioBeniDest);

         -- Riporti dati utilizzatori (solo per trasferimento stessa UO e se la destinazione è bene principale)

         IF (inPgInventarioOri = aPgInventarioDest AND
             aRecBuonoCaricoDett.progressivo = 0) THEN
            riportaUtilizzatoriLa(aRecBuonoScaricoDett.pg_inventario,
                                  aRecBuonoScaricoDett.nr_inventario,
                                  aRecBuonoCaricoDett.nr_inventario,
                                  aRecBuonoCaricoDett.progressivo,
                                  inUtente,
                                  dataOdierna);
         END IF;

         -- Creazione associativa ----------------------------------------------------------------

         aRecAssTrasferimentoBeniInv.pg_inventario_origine:=aRecBuonoScaricoDett.pg_inventario;
         aRecAssTrasferimentoBeniInv.nr_inventario_origine:=aRecBuonoScaricoDett.nr_inventario;
         aRecAssTrasferimentoBeniInv.progressivo_origine:=aRecBuonoScaricoDett.progressivo;
         aRecAssTrasferimentoBeniInv.pg_inventario_dest:=aRecBuonoCaricoDett.pg_inventario;
         aRecAssTrasferimentoBeniInv.nr_inventario_dest:=aRecBuonoCaricoDett.nr_inventario;
         aRecAssTrasferimentoBeniInv.progressivo_dest:=aRecBuonoCaricoDett.progressivo;
         aRecAssTrasferimentoBeniInv.valore_iniziale:=aRecInventarioBeniDest.valore_iniziale;
         aRecAssTrasferimentoBeniInv.variazione_piu:=aRecInventarioBeniDest.variazione_piu;
         aRecAssTrasferimentoBeniInv.variazione_meno:=aRecInventarioBeniDest.variazione_meno;
         aRecAssTrasferimentoBeniInv.imponibile_ammortamento:=aRecInventarioBeniDest.imponibile_ammortamento;
         aRecAssTrasferimentoBeniInv.valore_ammortizzato:=aRecInventarioBeniDest.valore_ammortizzato;
         aRecAssTrasferimentoBeniInv.dacr:=dataOdierna;
         aRecAssTrasferimentoBeniInv.utcr:=inUtente;
         aRecAssTrasferimentoBeniInv.duva:=dataOdierna;
         aRecAssTrasferimentoBeniInv.utuv:=inUtente;
         aRecAssTrasferimentoBeniInv.pg_ver_rec:=1;

         insAssTrasferimentoBeniInv(aRecAssTrasferimentoBeniInv);

      END LOOP;

      COMMIT;
      aPgBuonoCarico:=aTmpPgBuonoCarico;
      aPgBuonoScarico:=aTmpPgBuonoScarico;
      aNumDettaglio:=aTmpNumDettaglio;

   END;

END eseguiTrasferisciBeni;

-- =================================================================================================
-- Procedura di trasferimento beni
-- =================================================================================================
PROCEDURE leggiTrasferisciBeni
   (
    inLocalTransId VARCHAR2,
    inPgInventarioOri NUMBER,
    aPgInventarioDest NUMBER,
    inEsercizio NUMBER,
    inCdTipoCarico VARCHAR2,
    inCdTipoScarico VARCHAR2,
    inDsBuonoCarScar VARCHAR2,
    inDtRegistrazione DATE,
    aFlTrasferisciTutto VARCHAR2,
    inUtente VARCHAR2,
    inCdsDest VARCHAR2,
    inUoDest VARCHAR2
   ) IS

   memPgInventario INVENTARIO_BENI.pg_inventario%TYPE;
   memNrInventario INVENTARIO_BENI.nr_inventario%TYPE;
   memProgressivo INVENTARIO_BENI.progressivo%TYPE;
   strBeneOrigine VARCHAR2(30);
   strBeneDestinazione VARCHAR2(30);
   aPgBuonoCarico NUMBER(10);
   aPgBuonoScarico NUMBER(10);
   aNumDettaglio NUMBER(10);
   aFlTrasferisci CHAR(1);

   j BINARY_INTEGER;
   i BINARY_INTEGER;

   aRecInventarioBeniApgOri INVENTARIO_BENI_APG%ROWTYPE;
   aRecBuonoScarico BUONO_CARICO_SCARICO%ROWTYPE;
   aRecUbicazioneBene UBICAZIONE_BENE%ROWTYPE;

   gen_cv GenericCurTyp;

BEGIN

   -------------------------------------------------------------------------------------------------
   -- Inizializza variabili

   memPgInventario:=NULL;
   memNrInventario:=NULL;
   memProgressivo:=NULL;
   strBeneOrigine:=NULL;
   strBeneDestinazione:=NULL;
   aPgBuonoCarico:=0;
   aPgBuonoScarico:=0;
   aNumDettaglio:=0;

   -- Base testata buono carico scarico

   aRecBuonoScarico:=NULL;
   aRecBuonoScarico.pg_inventario:=inPgInventarioOri;
   aRecBuonoScarico.ti_documento:=BUONO_SCARICO;
   aRecBuonoScarico.esercizio:=inEsercizio;
   aRecBuonoScarico.ds_buono_carico_scarico:=inDsBuonoCarScar;
   aRecBuonoScarico.data_registrazione:=inDtRegistrazione;
   aRecBuonoScarico.cd_tipo_carico_scarico:=inCdTipoScarico;
   aRecBuonoScarico.utcr:=inUtente;
   aRecBuonoScarico.utuv:=inUtente;
   aRecBuonoScarico.pg_ver_rec:=1;

   -------------------------------------------------------------------------------------------------
   -- Controlli di base

   -- Se l'esercizio di riferimento è 2003 sollevo errore

   IF inEsercizio < 2004 THEN
      IBMERR001.RAISE_ERR_GENERICO
         ('Errore generale. Non è possibile eseguire il trasferimento beni in esercizio anteriore al 2004');
   END IF;

   -- Recupero dati per ubicazione bene di default

   IF inPgInventarioOri != aPgInventarioDest THEN
      aRecUbicazioneBene:=getUbicazioneDefault(inCdsDest,
                                               inUoDest);
   END IF;


   -------------------------------------------------------------------------------------------------
   -- Elaborazione trasferimento

   BEGIN

      IF aFlTrasferisciTutto = 'Y' THEN

         OPEN gen_cv FOR

              SELECT pg_inventario,
                     nr_inventario,
                     progressivo,
                     TO_CHAR(NULL),
                     TO_NUMBER(NULL),
                     TO_NUMBER(NULL),
                     TO_NUMBER(NULL),
                     to_char(null)
              FROM   INVENTARIO_BENI
              WHERE  pg_inventario = inPgInventarioOri AND
                     fl_totalmente_scaricato = 'N' And
                     dt_validita_variazione <= inDtRegistrazione
              ORDER BY pg_inventario,
                       nr_inventario,
                       progressivo;

      ELSE

         OPEN gen_cv FOR

              SELECT DISTINCT pg_inventario,
                     nr_inventario,
                     progressivo,
                     fl_trasf_come_principale,
                     pg_inventario_principale,
                     nr_inventario_principale,
                     progressivo_principale,
                     cd_categoria_gruppo_new
              FROM   INVENTARIO_BENI_APG
              WHERE  local_transaction_id = inLocalTransId AND
                     pg_inventario = inPgInventarioOri
              ORDER BY pg_inventario,
                       nr_inventario,
                       progressivo;

      END IF;

      LOOP

         BEGIN

            FETCH gen_cv INTO
                  aRecInventarioBeniApgOri.pg_inventario,
                  aRecInventarioBeniApgOri.nr_inventario,
                  aRecInventarioBeniApgOri.progressivo,
                  aRecInventarioBeniApgOri.fl_trasf_come_principale,
                  aRecInventarioBeniApgOri.pg_inventario_principale,
                  aRecInventarioBeniApgOri.nr_inventario_principale,
                  aRecInventarioBeniApgOri.progressivo_principale,
									aRecInventarioBeniApgOri.cd_categoria_gruppo_new;
            EXIT WHEN gen_cv%NOTFOUND;

            IF memPgInventario IS NULL THEN
               memPgInventario:=aRecInventarioBeniApgOri.pg_inventario;
               memNrInventario:=aRecInventarioBeniApgOri.nr_inventario;
               memProgressivo:=aRecInventarioBeniApgOri.progressivo;
               beniDaTrasf_tab.DELETE;
               i:=0;
            END IF;

            -- Se il numero inventario è diverso da quello memorizzato allora indico l'esecuzione del
            -- trasferimento

            IF aRecInventarioBeniApgOri.nr_inventario != memNrInventario THEN
               aFlTrasferisci:='Y';
            END IF;

            -- Se ho già portato dei beni sulla tabella di appoggio e la prima occorrenza non è un bene
            -- principale, anche se il numero inventario è uguale al precedente, indico l'esecuzione del
            -- trasferimento: ogni accessorio vive di vita propria

            IF (i > 0 AND
                beniDaTrasf_tab(1).tProgressivo != 0) THEN
               aFlTrasferisci:='Y';
            END IF;

            -- Esecuzione del trasferimento e azzeramento della tabella per memorizzare la nuova occorrenza

            BEGIN

               IF aFlTrasferisci='Y' THEN
                  eseguiTrasferisciBeni(inPgInventarioOri,
                                        aPgInventarioDest,
                                        inEsercizio,
                                        inCdTipoCarico,
                                        aFlTrasferisciTutto,
                                        inUtente,
                                        aPgBuonoCarico,
                                        aPgBuonoScarico,
                                        aNumDettaglio,
                                        aRecBuonoScarico,
                                        aRecUbicazioneBene.cd_cds,
                                        aRecUbicazioneBene.cd_unita_organizzativa,
                                        aRecUbicazioneBene.cd_ubicazione,
                                        inDtRegistrazione);

                  beniDaTrasf_tab.DELETE;
                  i:=0;
               END IF;

            EXCEPTION

               WHEN others THEN
                    j:=(errori_tab.COUNT + 1);
                    errori_tab(j).tStrIdBeneOrigine:=strBeneOrigine;
                    errori_tab(j).tStrIdBeneDest:=strBeneDestinazione;
                    errori_tab(j).tStringaMsg:=SUBSTR(DBMS_UTILITY.FORMAT_ERROR_STACK,1,3900);
                    ROLLBACK;
                    beniDaTrasf_tab.DELETE;
                    i:=0;

            END;

            -- Memorizzo sulla tabella di appoggio i beni

            aFlTrasferisci:='N';
            i:=i + 1;
            IF i = 1 THEN
               strBeneOrigine:='Bene Ori = ' || aRecInventarioBeniApgOri.pg_inventario || '-' ||
                                aRecInventarioBeniApgOri.nr_inventario || '-' ||
                                aRecInventarioBeniApgOri.progressivo;
               strBeneDestinazione:='Bene Dest = ' || aRecInventarioBeniApgOri.pg_inventario_principale || '-' ||
                                aRecInventarioBeniApgOri.nr_inventario_principale || '-' ||
                                aRecInventarioBeniApgOri.progressivo_principale;
            END IF;
            memPgInventario:=aRecInventarioBeniApgOri.pg_inventario;
            memNrInventario:=aRecInventarioBeniApgOri.nr_inventario;
            memProgressivo:=aRecInventarioBeniApgOri.progressivo;
            beniDaTrasf_tab(i).tPgInventario:=aRecInventarioBeniApgOri.pg_inventario;
            beniDaTrasf_tab(i).tNrInventario:=aRecInventarioBeniApgOri.nr_inventario;
            beniDaTrasf_tab(i).tProgressivo:=aRecInventarioBeniApgOri.progressivo;
            beniDaTrasf_tab(i).tFlTrasfComePrincipale:=aRecInventarioBeniApgOri.fl_trasf_come_principale;
            beniDaTrasf_tab(i).tPgInventarioPrincipale:=aRecInventarioBeniApgOri.pg_inventario_principale;
            beniDaTrasf_tab(i).tNrInventarioPrincipale:=aRecInventarioBeniApgOri.nr_inventario_principale;
            beniDaTrasf_tab(i).tProgressivoPrincipale:=aRecInventarioBeniApgOri.progressivo_principale;
					  beniDaTrasf_tab(i).tCategoriaNew:=aRecInventarioBeniApgOri.cd_categoria_gruppo_new;

         EXCEPTION

            WHEN others THEN
                 j:=(errori_tab.COUNT + 1);
                 errori_tab(j).tStrIdBeneOrigine:=strBeneOrigine;
                 errori_tab(j).tStrIdBeneDest:=strBeneDestinazione;
                 errori_tab(j).tStringaMsg:=SUBSTR(DBMS_UTILITY.FORMAT_ERROR_STACK,1,3900);
                 ROLLBACK;
                 aFlTrasferisci:='N';
                 beniDaTrasf_tab.DELETE;
                 i:=0;

         END;

      END LOOP;

      CLOSE gen_cv;

      eseguiTrasferisciBeni(inPgInventarioOri,
                            aPgInventarioDest,
                            inEsercizio,
                            inCdTipoCarico,
                            aFlTrasferisciTutto,
                            inUtente,
                            aPgBuonoCarico,
                            aPgBuonoScarico,
                            aNumDettaglio,
                            aRecBuonoScarico,
                            aRecUbicazioneBene.cd_cds,
                            aRecUbicazioneBene.cd_unita_organizzativa,
                            aRecUbicazioneBene.cd_ubicazione,
                            inDtRegistrazione);

   EXCEPTION

      WHEN others THEN
           j:=(errori_tab.COUNT + 1);
           errori_tab(j).tStrIdBeneOrigine:=strBeneOrigine;
           errori_tab(j).tStrIdBeneDest:=strBeneDestinazione;
           errori_tab(j).tStringaMsg:=SUBSTR(DBMS_UTILITY.FORMAT_ERROR_STACK,1,3900);
           ROLLBACK;

   END;

END leggiTrasferisciBeni;

-- =================================================================================================
-- Guscio per gestione del trasferimento beni batch
-- =================================================================================================
PROCEDURE job_trasferisciBeni
   (job NUMBER,
    pg_exec NUMBER,
    next_date DATE,
    inLocalTransId VARCHAR2,
    inPgInventarioOri NUMBER,
    aPgInventarioDest NUMBER,
    inEsercizio NUMBER,
    inCdTipoCarico VARCHAR2,
    inCdTipoScarico VARCHAR2,
    inDsBuonoCarScar VARCHAR2,
    inDtRegistrazione DATE,
    aFlTrasferisciTutto VARCHAR2,
    inUtente VARCHAR2,
    inCdsDest VARCHAR2,
    inUoDest VARCHAR2) IS

   i BINARY_INTEGER;

BEGIN

   -- Lancio start esecuzione log

   IBMUTL210.logStartExecutionUpd(pg_exec, LOG_TIPO_TRASFBENI, job, 'Richiesta utente:' || inUtente,
                                  'Trasferimento beni. Start:' || TO_CHAR(sysdate,'YYYY/MM/DD HH-MI-SS'));

   BEGIN

      leggiTrasferisciBeni(inLocalTransId,
                           inPgInventarioOri,
                           aPgInventarioDest,
                           inEsercizio,
                           inCdTipoCarico,
                           inCdTipoScarico,
                           inDsBuonoCarScar,
                           inDtRegistrazione,
                           aFlTrasferisciTutto,
                           inUtente,
                           inCdsDest,
                           inUoDest);

      -- Cancellazione della tabella di appoggio

      IF aFlTrasferisciTutto = 'N' THEN
         cancellaInventarioBeniApg(inLocalTransId);
      END IF;

      -- Controllo se vi sono stati errori

      IF errori_tab.COUNT > 0 THEN
         IBMERR001.RAISE_ERR_GENERICO
            ('Operazione completata con successo ma con errori. Verificare dettaglio log per elenco errori');
      END IF;

      COMMIT;

      -- Messaggio di operazione completata ad utente

      IBMUTL205.LOGINF('Trasferimento beni',
                       'Trasferimento beni ' || TO_CHAR(sysdate,'DD/MM/YYYY HH:MI:SS'),
                       'Operazione completata con successo',inUtente);

   EXCEPTION

      WHEN others THEN
           ROLLBACK;

      -- Messaggio di attenzione ad utente

      IBMUTL205.LOGERR('Trasferimento beni ' || errori_tab.COUNT,
                       'Trasferimento beni ' || TO_CHAR(sysdate,'DD/MM/YYYY HH:MI:SS') ||
                       ' (pg_exec= ' || pg_exec || ')', DBMS_UTILITY.FORMAT_ERROR_STACK,inUtente);

      -- Scrittura degli eventuali altri errori

      IF errori_tab.COUNT > 0 THEN

         FOR i IN errori_tab.FIRST .. errori_tab.LAST

         LOOP

            IBMUTL200.LOGERR(pg_exec,
                             'Trasferimento beni - Dettaglio errori',
                             'Errore : ' || errori_tab(i).tStringaMsg,
                             'Bene Origine = ' || errori_tab(i).tStrIdBeneOrigine || ' ' ||
                             'Bene Destinazione ' || errori_tab(i).tStrIdBeneDest);

         END LOOP;

      END IF;

      -- Cancellazione della tabella di appoggio

      IF aFlTrasferisciTutto = 'N' THEN
         cancellaInventarioBeniApg(inLocalTransId);
      END IF;

   END;

END job_trasferisciBeni;

-- =================================================================================================
-- Main trasferimento beni
-- =================================================================================================
PROCEDURE trasferisciBeni
   (inLocalTransId VARCHAR2,
    inPgInventarioOri NUMBER,
    inPgInventarioDest NUMBER,
    inEsercizio NUMBER,
    inCdTipoCarico VARCHAR2,
    inCdTipoScarico VARCHAR2,
    inDsBuonoCarScar VARCHAR2,
    inDtRegistrazione DATE,
    inFlTrasferisciTutto VARCHAR2,
    inUtente VARCHAR2,
    inCdsDest VARCHAR2,
    inUoDest VARCHAR2) IS

   aPgInventarioDest NUMBER;
   aFlTrasferisciTutto VARCHAR2(1);
   aDtReg varchar2(50);
   aProcedure VARCHAR2(2000);

BEGIN

   -------------------------------------------------------------------------------------------------
   -- La funzione di trasferimento beni è attivata come batch

   aDtReg:='TO_DATE(' || '''' || TO_CHAR(inDtRegistrazione,'DDMMYYYY') || '''' || ',''DDMMYYYY'')';

   IF (inPgInventarioDest IS NULL OR
       inPgInventarioDest = 0) THEN
      aPgInventarioDest:=inPgInventarioOri;
   ELSE
      aPgInventarioDest:=inPgInventarioDest;
   END IF;

   IF inFlTrasferisciTutto IS NULL THEN
      aFlTrasferisciTutto:= 'N';
   ELSE
      aFlTrasferisciTutto:=inFlTrasferisciTutto;
   END IF;

   aProcedure:='CNRCTB400.job_trasferisciBeni(job, pg_exec, next_date, ' ||
                '''' || inLocalTransId || ''',' ||
                inPgInventarioOri || ',' || aPgInventarioDest || ',' ||
                inEsercizio || ',''' || inCdTipoCarico || ''',' ||
                '''' || inCdTipoScarico  || ''',' ||
                '''' || inDsBuonoCarScar  || ''',' ||
                aDtReg || ',''' || aFlTrasferisciTutto || ''',' ||
                '''' || inUtente  || ''',' ||
                '''' || inCdsDest  || ''',' ||
                '''' || inUoDest || ''');';

   IBMUTL210.CREABATCHDINAMICO('Trasferimento Beni',
                               aProcedure,
                               inUtente);

   IBMUTL001.deferred_commit;

   IBMERR001.RAISE_ERR_GENERICO
       ('Operazione in corso. Al completamento l''utente riceverà un messaggio di notifica ' ||
        'dello stato dell''operazione');

END trasferisciBeni;
-- =================================================================================================
-- Cancella un documento di scarico di inventario, con eventualmente la relativa testata ed il bene
-- =================================================================================================
PROCEDURE cancella_variazioni
   (p_esercizio 	number,
    p_inventario 	number,
    n_inventario 	number,
    p_progressivo	number,
    p_buono		number,
    p_utente	 	varchar2,
    p_data 		date,
    segno		varchar2,
    effettuato         In Out NUMBER) IS

   ALTRE_VARIAZIONI 	number;
   VARIAZIONI  		number;
   valore 		number;
   AMM			NUMBER;
   migrato 		char;
   tot_scaricato	char;
   tipo			char;
   DATA_VAR		DATE;
   p_var		number;
   quote_storno		number:=0;
   tipo_canc		char;
Begin
If segno = '-' Then
    tipo:='C';
    tipo_canc:='S';
Else
    tipo:='S';
    tipo_canc:='C';
End If;
   SELECT COUNT(*) INTO ALTRE_VARIAZIONI
   FROM BUONO_CARICO_SCARICO_DETT DETT ,BUONO_CARICO_SCARICO TEST
   WHERE
   DETT.PG_INVENTARIO = p_Inventario 		AND
   DETT.NR_INVENTARIO = n_Inventario 		AND
   DETT.PROGRESSIVO   = p_Progressivo  		AND
   --DETT.ESERCIZIO     = p_ESERCIZIO		AND
   DETT.PG_INVENTARIO = TEST.PG_INVENTARIO 	AND
   DETT.TI_DOCUMENTO  = TEST.TI_DOCUMENTO  	AND
   DETT.ESERCIZIO     = TEST.ESERCIZIO     	AND
   DETT.PG_BUONO_C_S  = TEST.PG_BUONO_C_S	AND
   ((tipo='C' And
   Trunc(DATA_REGISTRAZIONE) > Trunc(p_data)) Or
   (tipo='S' And
   Trunc(DATA_REGISTRAZIONE) >= Trunc(p_data)))   And
   (DETT.PG_BUONO_C_S  > P_BUONO Or
   test.ti_documento = tipo);
  IF ALTRE_VARIAZIONI >0 THEN
    effettuato:=1;
  Else
   Begin
   	SELECT COUNT(*),fl_migrato,fl_totalmente_scaricato INTO VARIAZIONI,migrato,tot_scaricato
   	FROM BUONO_CARICO_SCARICO_DETT DETT ,inventario_beni beni
   	WHERE
   	DETT.PG_INVENTARIO = p_inventario 		AND
   	DETT.NR_INVENTARIO = N_inventario 		AND
   	DETT.PROGRESSIVO   = p_progressivo  		AND
   	-- r.p. viene controllato prima della chiamata
   	--VALORE_AMMORTIZZATO  = 0 			AND
   	DETT.PG_INVENTARIO = beni.pg_Inventario	AND
   	DETT.NR_INVENTARIO = beni.Nr_Inventario	AND
   	DETT.PROGRESSIVO   = beni.Progressivo
   	group by fl_migrato,fl_totalmente_scaricato;
   Exception When No_Data_Found Then
   	variazioni:=0;
   	SELECT fl_migrato,fl_totalmente_scaricato Into migrato,tot_scaricato
   	FROM inventario_beni beni
   	WHERE
   	beni.PG_INVENTARIO = p_inventario 		AND
   	beni.NR_INVENTARIO = N_inventario 		AND
   	beni.PROGRESSIVO   = p_progressivo;
   End;
   Select Max(DATA_REGISTRAZIONE) Into DATA_VAR
   From BUONO_CARICO_SCARICO TEST,BUONO_CARICO_SCARICO_DETT DETT
   Where
   DETT.PG_INVENTARIO = p_Inventario 		AND
   DETT.NR_INVENTARIO = n_Inventario 		AND
   DETT.PROGRESSIVO   = p_Progressivo  		AND
   DETT.PG_INVENTARIO = TEST.PG_INVENTARIO 	AND
   DETT.TI_DOCUMENTO  = TEST.TI_DOCUMENTO  	AND
   DETT.ESERCIZIO     = TEST.ESERCIZIO     	AND
   DETT.PG_BUONO_C_S  = TEST.PG_BUONO_C_S	AND
   Trunc(DATA_REGISTRAZIONE) < Trunc(p_data);

  end if;
  Begin
  if variazioni > 1 Or migrato ='Y' Then
	Select valore_unitario,(valore_iniziale+variazione_piu-variazione_meno),IMPONIBILE_AMMORTAMENTO INTO p_var,valore,AMM
   	FROM BUONO_CARICO_SCARICO_DETT DETT ,inventario_beni beni
   	WHERE
   	DETT.PG_INVENTARIO = p_inventario 		AND
   	DETT.NR_INVENTARIO = N_inventario 		AND
   	DETT.PROGRESSIVO   = p_progressivo  		AND
   	dett.pg_buono_c_s  = p_buono			and
   	dett.TI_DOCUMENTO  = TIPO_CANC 			AND
   	DETT.ESERCIZIO 	   = P_ESERCIZIO		AND
   	DETT.PG_INVENTARIO = beni.pg_Inventario		AND
   	DETT.NR_INVENTARIO = beni.Nr_Inventario		AND
   	DETT.PROGRESSIVO   = beni.Progressivo;
   	if segno ='-' Then
   	   Begin
	   	   Select im_movimento_ammort*(-1) Into quote_storno
	   	   From ammortamento_bene_inv
	   	   Where
	   	     	PG_INVENTARIO = p_Inventario	AND
	   		NR_INVENTARIO = N_inventario	AND
	   		PROGRESSIVO   = p_Progressivo   And
	   		pg_buono_s    = p_buono         And
	   		esercizio     = p_esercizio     And
	   		fl_storno     = 'Y';
	   		delete From ammortamento_bene_inv
	   		where
	   		PG_INVENTARIO = p_Inventario	AND
	   		NR_INVENTARIO = N_inventario	AND
	   		PROGRESSIVO   = p_Progressivo   And
	   		pg_buono_s    = p_buono         And
	   		esercizio     = p_esercizio     And
	   		fl_storno     = 'Y';
   	    Exception When No_Data_Found Then
   	      quote_storno:=0;
   	    End;

	  If valore=0 And tot_scaricato='Y'  Then
               UPDATE INVENTARIO_BENI
               SET    variazione_meno = variazione_meno - P_var,
                      imponibile_ammortamento = imponibile_ammortamento +P_var,
                      valore_ammortizzato = valore_ammortizzato + quote_storno,
                      dt_validita_variazione = Nvl(DATA_VAR,DT_VALIDITA_VARIAZIONE),
                      valore_alienazione =0,
                      fl_totalmente_scaricato='N',
                      duva = SYSDATE,
                      utuv = P_Utente,
                      pg_ver_rec = pg_ver_rec + 1
               WHERE  pg_inventario = p_inventario AND
                      nr_inventario = n_inventario AND
                      progressivo   = p_progressivo;
            Elsif  tot_scaricato='N'  Then
               If AMM>0 Then
               		UPDATE INVENTARIO_BENI
               		SET    	variazione_meno = variazione_meno - P_var,
                      		imponibile_ammortamento = imponibile_ammortamento +P_var,
                      		valore_ammortizzato = valore_ammortizzato + quote_storno,
                      		dt_validita_variazione = Nvl(DATA_VAR,DT_VALIDITA_VARIAZIONE),
                      		duva = SYSDATE,
                      		utuv = P_Utente,
                      		pg_ver_rec = pg_ver_rec + 1
               		WHERE  	pg_inventario = p_inventario AND
                      		nr_inventario = n_inventario AND
                      		progressivo   = p_progressivo;
                 Else
                      UPDATE INVENTARIO_BENI
               		SET    	variazione_meno = variazione_meno - P_var,
                      		--imponibile_ammortamento = imponibile_ammortamento +P_var,
                      		dt_validita_variazione = Nvl(DATA_VAR,DT_VALIDITA_VARIAZIONE),
                      		duva = SYSDATE,
                      		utuv = P_Utente,
                      		pg_ver_rec = pg_ver_rec + 1
               		WHERE  	pg_inventario = p_inventario AND
                      		nr_inventario = n_inventario AND
                      		progressivo   = p_progressivo;
                  End If;
             Else
                     effettuato  :=1;
             End If;

         Else
            If AMM-P_VAR>0 Then
                  UPDATE INVENTARIO_BENI
               SET    variazione_piu = variazione_piu - P_var,
                      imponibile_ammortamento = imponibile_ammortamento -P_var,
                      dt_validita_variazione = Nvl(DATA_VAR,DT_VALIDITA_VARIAZIONE),
                      duva = SYSDATE,
                      utuv = P_Utente,
                      pg_ver_rec = pg_ver_rec + 1
               WHERE  pg_inventario = p_inventario AND
                      nr_inventario = n_inventario AND
                      progressivo   = p_progressivo;
            Else
           	UPDATE INVENTARIO_BENI
               SET    variazione_piu = variazione_piu - P_var,
                      dt_validita_variazione = Nvl(DATA_VAR,DT_VALIDITA_VARIAZIONE),
                      duva = SYSDATE,
                      utuv = P_Utente,
                      pg_ver_rec = pg_ver_rec + 1
               WHERE  pg_inventario = p_inventario AND
                      nr_inventario = n_inventario AND
                      progressivo   = p_progressivo;
            End If;
         end if;
     effettuato  :=0;
  Elsif variazioni =1 And migrato ='N' Then
     effettuato  :=0; -- caso che rimane solo il carico iniziale
  Else
      effettuato  :=1;
  end if;
Exception When No_Data_Found Then
            effettuato  :=1;
End;

END cancella_variazioni;
-- =================================================================================================
-- Main Ammortamento Beni
-- =================================================================================================
Procedure AmmortamentoBeni(
p_esercizio 	number,
p_cds		varchar2,
p_utente 	varchar2)Is

  aProcedure VARCHAR2(2000);

BEGIN



   aProcedure:='CNRCTB400.job_ammortamentoBeniInv(job, pg_exec, next_date, ' ||
                p_esercizio || ',' ||
                '''' ||  p_cds || ''',' ||
                 '''' || p_utente   || ''');';

   IBMUTL210.CREABATCHDINAMICO('Ammortamento beni',
                               aProcedure,
                               p_utente);

   IBMUTL001.deferred_commit;

  /* IBMERR001.RAISE_ERR_GENERICO
       ('Operazione sottomessa per esecuzione. Al completamento l''utente riceverà un messaggio di notifica ' ||
        'dello stato dell''operazione');*/
Exception When Others Then
  Dbms_Output.PUT_LINE('ERRORE '||SQLCODE||' '||Sqlerrm);
END AmmortamentoBeni;

Procedure job_ammortamentoBeniInv
  (job NUMBER,
   pg_exec NUMBER,
   next_date DATE,
   aEs NUMBER,
   aCdCds VARCHAR2,
   aUser VARCHAR2) IS
Begin

   IBMUTL210.logStartExecutionUpd(pg_exec, LOG_TIPO_AMMBENI, job, 'Richiesta utente:' || aUser,
                              'Calcolo Ammortamento beni. Start:' || TO_CHAR(sysdate,'YYYY/MM/DD HH-MI-SS'));
   -------------------------------------------------------------------------------------------------
   -- La funzione di ammortamento dei beni è attivata come batch
	CNRCTB400.annullaAmmortBeniInv(pg_exec,aEs,aCdCds,aUser,Sysdate);


   -------------------------------------------------------------------------------------------------
   -- La funzione di ammortamento dei beni è attivata come batch
	CNRCTB400.ammortamentoBeniInv(pg_exec,aEs,aCdCds,aUser,Sysdate);


      -- Messaggio di operazione completata ad utente
 /*     IBMUTL205.LOGINF('Calcolo Ammortamento beni',
                       'Calcolo Ammortamento beni ' ||' CDS: '||aCdCds||' '||To_Char(sysdate,'DD/MM/YYYY HH:MI:SS'),
                       'Operazione completata con successo',aUser);
*/
End job_ammortamentoBeniInv;
-- =================================================================================================
-- Crea un documento di carico di inventario, con i relativi dettagli e le eventuali associazioni
-- con i documenti.
-- =================================================================================================
PROCEDURE updCaricoBeniAumentoValDoc
   (
    localTransId VARCHAR2,
    aPgInventario NUMBER,
    aEsercizio NUMBER,
    aPgBuonoCaricoScarico NUMBER,
    aDsBuonoCaricoScarico VARCHAR2,
    aCdTipoCaricoScarico VARCHAR2,
    aPgDocumento number,
    aUtente VARCHAR2,
    aDtRegistrazione DATE,
    aMessaggio IN OUT VARCHAR2
   ) IS

   pg_inv NUMBER;
   cont INTEGER;
   aStatoCoge CHAR(1);
   eseguiLock CHAR(1);
   aMessaggioBase VARCHAR2(100);
   aMessaggioBaseSegnala VARCHAR2(500);
   aMessaggioDettSegnala VARCHAR2(3900);

   aBuonoCaricoScarico BUONO_CARICO_SCARICO%ROWTYPE;
   aBuonoCaricoScaricoDett BUONO_CARICO_SCARICO_DETT%ROWTYPE;
   aAssInvBeneFattura ASS_INV_BENE_FATTURA%ROWTYPE;
   aRecInventarioBeni INVENTARIO_BENI%ROWTYPE;
   progressivo_riga number:=0;
BEGIN

   eseguiLock:='Y';

   -------------------------------------------------------------------------------------------------
   -- Valorizzazione messaggi da esporre in output

   aMessaggioBase:='Operazione completata con successo. ';
   aMessaggioBaseSegnala:='Attenzione, per i seguenti beni non Š stato modificato automaticamente ' ||
                          'l''imponibile ammortamento in quanto non risulta allineato al suo valore assestato, ' ||
                          'procedere manualmente.';
   aMessaggioDettSegnala:=NULL;

   -------------------------------------------------------------------------------------------------
   -- Inserimento testata BUONO_CARICO_SCARICO (TIPO = carico)

   aBuonoCaricoScarico:=NULL;

   aBuonoCaricoScarico.pg_inventario:=aPgInventario;
   aBuonoCaricoScarico.ti_documento:=BUONO_CARICO;
   aBuonoCaricoScarico.esercizio:=aEsercizio;
   aBuonoCaricoScarico.pg_buono_c_s:=aPgBuonoCaricoScarico;
   aBuonoCaricoScarico.ds_buono_carico_scarico:=aDsBuonoCaricoScarico;
   aBuonoCaricoScarico.data_registrazione:=aDtRegistrazione;
   aBuonoCaricoScarico.cd_tipo_carico_scarico:=aCdTipoCaricoScarico;
   aBuonoCaricoScarico.dacr:=SYSDATE;
   aBuonoCaricoScarico.utcr:=aUtente;
   aBuonoCaricoScarico.duva:=SYSDATE;
   aBuonoCaricoScarico.utuv:=aUtente;
   aBuonoCaricoScarico.pg_ver_rec:=1;

   insBuonoCaricoScarico(aBuonoCaricoScarico);

   -------------------------------------------------------------------------------------------------
   -- Ciclo inserimento dettaglio BUONO_CARICO_SCARICO_DETT e ASS_INV_BENE_FATTURA

   cont :=0;

   BEGIN

      FOR riga_group_invetario_beni_apg IN
          (SELECT PG_INVENTARIO, NR_INVENTARIO, PROGRESSIVO, SUM(VARIAZIONE_PIU) variazione_piu
           FROM   INVENTARIO_BENI_APG
           WHERE  TRIM(LOCAL_TRANSACTION_ID) = TRIM (localTransId)
               and pg_buono_c_s < 0
           GROUP BY PG_INVENTARIO, NR_INVENTARIO, PROGRESSIVO)

      LOOP

         cont := cont+1;

         -- inserimento su tabella BUONO_CARICO_SCARICO_DETT ---------------------------------------

         BEGIN

            aStatoCoge:='X';

            aBuonoCaricoScaricoDett:=NULL;

            aBuonoCaricoScaricoDett.pg_inventario:=aPgInventario;
            aBuonoCaricoScaricoDett.ti_documento:=BUONO_CARICO;
            aBuonoCaricoScaricoDett.esercizio:=aEsercizio;
            aBuonoCaricoScaricoDett.pg_buono_c_s:=aPgBuonoCaricoScarico;
            aBuonoCaricoScaricoDett.nr_inventario:=riga_group_invetario_beni_apg.NR_INVENTARIO;
            aBuonoCaricoScaricoDett.progressivo:=riga_group_invetario_beni_apg.PROGRESSIVO;
            aBuonoCaricoScaricoDett.intervallo:=cont || '-' || cont;
            aBuonoCaricoScaricoDett.quantita:=1;
            aBuonoCaricoScaricoDett.valore_unitario:=riga_group_invetario_beni_apg.VARIAZIONE_PIU;
            aBuonoCaricoScaricoDett.dacr:=SYSDATE;
            aBuonoCaricoScaricoDett.utcr:=aUtente;
            aBuonoCaricoScaricoDett.duva:=SYSDATE;
            aBuonoCaricoScaricoDett.utuv:=aUtente;
            aBuonoCaricoScaricoDett.pg_ver_rec:=1;
            aBuonoCaricoScaricoDett.stato_coge:=aStatoCoge;
	    aBuonoCaricoScaricoDett.stato_coge_quote:='X';
            insBuonoCaricoScaricoDett(aBuonoCaricoScaricoDett);

         END;

         -- inserimento su tabella ASS_INV_BENE_FATTURA --------------------------------------------

         BEGIN

            FOR riga_invetario_beni_apg IN
                    (SELECT *
                     FROM   INVENTARIO_BENI_APG
                     where  TRIM(LOCAL_TRANSACTION_ID) =TRIM(localTransId)
                        and pg_buono_c_s < 0
                        and PG_INVENTARIO = riga_group_invetario_beni_apg.PG_INVENTARIO
                        and NR_INVENTARIO = riga_group_invetario_beni_apg.NR_INVENTARIO
                        and PROGRESSIVO = riga_group_invetario_beni_apg.PROGRESSIVO)

               LOOP
		Select NVL(Max(PG_RIGA)+1,1) into progressivo_riga
                FROM   ass_inv_bene_fattura;
                  aAssInvBeneFattura:=NULL;

                  aAssInvBeneFattura.cd_cds_doc_gen:=riga_invetario_beni_apg.CD_CDS;
                  aAssInvBeneFattura.cd_uo_doc_gen:=riga_invetario_beni_apg.CD_UNITA_ORGANIZZATIVA;
                  aAssInvBeneFattura.esercizio_doc_gen:=riga_invetario_beni_apg.ESERCIZIO;
                  aAssInvBeneFattura.pg_documento_generico:=aPgDocumento;
                  aAssInvBeneFattura.progressivo_riga_doc_gen:=riga_invetario_beni_apg.PROGRESSIVO_RIGA;
                  aAssInvBeneFattura.cd_tipo_documento_amm:=riga_invetario_beni_apg.cd_tipo_documento_amm;
                  aAssInvBeneFattura.pg_inventario:=riga_invetario_beni_apg.PG_INVENTARIO;
                  aAssInvBeneFattura.nr_inventario:=riga_invetario_beni_apg.NR_INVENTARIO;
                  aAssInvBeneFattura.progressivo:=riga_invetario_beni_apg.PROGRESSIVO;
                  aAssInvBeneFattura.dacr:=SYSDATE;
                  aAssInvBeneFattura.utcr:=aUtente;
                  aAssInvBeneFattura.duva:=SYSDATE;
                  aAssInvBeneFattura.utuv:=aUtente;
                  aAssInvBeneFattura.pg_ver_rec:=1;
		  aAssInvBeneFattura.pg_riga:=progressivo_riga;
		  aAssInvBeneFattura.ESERCIZIO:=riga_invetario_beni_apg.ESERCIZIO;
		  aAssInvBeneFattura.PG_BUONO_C_S:=aPgBuonoCaricoScarico;
		  aAssInvBeneFattura.TI_DOCUMENTO:=riga_invetario_beni_apg.TI_DOCUMENTO;

                  insAssInvBeneFattura(aAssInvBeneFattura);

               END LOOP;

         END;

         -- Aggiornamento INVENTARIO_BENI ----------------------------------------------------------

         BEGIN

            -- Lettura del bene per verificare se aggiornare o meno il campo imponibile_ammortamento

            aRecInventarioBeni:=getInventarioBeni(riga_group_invetario_beni_apg.pg_inventario,
                                                  riga_group_invetario_beni_apg.nr_inventario,
                                                  riga_group_invetario_beni_apg.progressivo,
                                                  eseguiLock);

            IF (aRecInventarioBeni.imponibile_ammortamento = aRecInventarioBeni.valore_iniziale +
                                                             aRecInventarioBeni.variazione_piu -
                                                             aRecInventarioBeni.variazione_meno) THEN

               UPDATE INVENTARIO_BENI
               SET    variazione_piu = variazione_piu + riga_group_invetario_beni_apg.variazione_piu,
                      imponibile_ammortamento = imponibile_ammortamento + riga_group_invetario_beni_apg.variazione_piu,
                      dt_validita_variazione = aDtRegistrazione,
                      duva = SYSDATE,
                      utuv = aUtente,
                      pg_ver_rec = pg_ver_rec + 1
               WHERE  pg_inventario= riga_group_invetario_beni_apg.pg_inventario AND
                      nr_inventario= riga_group_invetario_beni_apg.nr_inventario AND
                      progressivo = riga_group_invetario_beni_apg.progressivo;
            ELSE

               UPDATE INVENTARIO_BENI
               SET    variazione_piu = variazione_piu + riga_group_invetario_beni_apg.variazione_piu,
                      dt_validita_variazione = aDtRegistrazione,
                      duva = SYSDATE,
                      utuv = aUtente,
                      pg_ver_rec = pg_ver_rec + 1
               WHERE  pg_inventario= riga_group_invetario_beni_apg.pg_inventario AND
                      nr_inventario= riga_group_invetario_beni_apg.nr_inventario AND
                      progressivo = riga_group_invetario_beni_apg.progressivo;
                      -- r.p. 13/02/2006 da verificare per segnalare i beni per cui non è stato possibile
                      -- aggiornare l'ammortamento
		if aRecInventarioBeni.fl_ammortamento = 'Y' Then
               		IF aMessaggioDettSegnala IS NULL THEN
                  		aMessaggioDettSegnala:='Bene: ' || riga_group_invetario_beni_apg.pg_inventario || '-' ||
                                         riga_group_invetario_beni_apg.nr_inventario || '-' ||
                                         riga_group_invetario_beni_apg.progressivo || CHR(10);
               		ELSE
                  		aMessaggioDettSegnala:=aMessaggioDettSegnala ||
                                         'Bene: ' || riga_group_invetario_beni_apg.pg_inventario || '-' ||
                                         riga_group_invetario_beni_apg.nr_inventario || '-' ||
                                         riga_group_invetario_beni_apg.progressivo || CHR(10);
               		END IF;
		end If;
            END IF;

         END;

      END LOOP;

   END;

   -- Cancellazione della tabella di appoggio

   cancellaInventarioBeniApg(localTransId);

   -- Composizione del messaggio

   IF aMessaggioDettSegnala IS NULL THEN
      aMessaggio:=aMessaggioBase;
   ELSE
      aMessaggio:=SUBSTR(aMessaggioBase || CHR(10) || aMessaggioBaseSegnala || CHR(10) || aMessaggioDettSegnala, 1, 3900);
   END IF;

END updCaricoBeniAumentoValDoc;
end;
