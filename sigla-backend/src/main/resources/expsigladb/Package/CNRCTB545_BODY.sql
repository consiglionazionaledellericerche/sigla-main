--------------------------------------------------------
--  DDL for Package Body CNRCTB545
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "CNRCTB545" AS

--==================================================================================================
-- Ritorna un record della tabella COMPENSO
--==================================================================================================
FUNCTION getCompenso
   (
    aCdCds COMPENSO.cd_cds%TYPE,
    aCdUnitaOrganizzativa COMPENSO.cd_unita_organizzativa%TYPE,
    aEsercizio COMPENSO.esercizio%TYPE,
    aPgCompenso COMPENSO.pg_compenso%TYPE,
    eseguiLock CHAR
   ) RETURN COMPENSO%ROWTYPE IS
   aRecCompenso COMPENSO%ROWTYPE;

BEGIN

    IF eseguiLock = 'Y' THEN

       SELECT * INTO aRecCompenso
       FROM   COMPENSO
       WHERE  cd_cds = aCdCds AND
              cd_unita_organizzativa = aCdUnitaOrganizzativa AND
              esercizio = aEsercizio AND
              pg_compenso = aPgCompenso
       FOR UPDATE NOWAIT;

   ELSE

      SELECT * INTO aRecCompenso
      FROM   COMPENSO
      WHERE  cd_cds = aCdCds AND
             cd_unita_organizzativa = aCdUnitaOrganizzativa AND
             esercizio = aEsercizio AND
             pg_compenso = aPgCompenso;

   END IF;

   RETURN aRecCompenso;

EXCEPTION

   WHEN no_data_found THEN
        IBMERR001.RAISE_ERR_GENERICO
                  ('Compenso U.O. ' || aCdUnitaOrganizzativa ||
                   ' numero ' || aEsercizio || '/' || aPgCompenso ||
                   ' non trovata');

END getCompenso;

--==================================================================================================
-- Ritorna un record della tabella COMPENSO che risulta collegatom a missione
--==================================================================================================
FUNCTION getCompensoDaMissione
   (
    aCdCds MISSIONE.cd_cds%TYPE,
    aCdUnitaOrganizzativa MISSIONE.cd_unita_organizzativa%TYPE,
    aEsercizio MISSIONE.esercizio%TYPE,
    aPgMissione MISSIONE.pg_missione%TYPE,
    eseguiLock CHAR
   ) RETURN COMPENSO%ROWTYPE IS
   aRecCompenso COMPENSO%ROWTYPE;

BEGIN

    IF eseguiLock = 'Y' THEN

       SELECT * INTO aRecCompenso
       FROM   COMPENSO
       WHERE  cd_cds_missione = aCdCds AND
              cd_uo_missione = aCdUnitaOrganizzativa AND
              esercizio_missione = aEsercizio AND
              pg_missione = aPgMissione AND
              pg_compenso > 0 AND
              stato_cofi != 'A'
       FOR UPDATE NOWAIT;

   ELSE

      SELECT * INTO aRecCompenso
      FROM   COMPENSO
       WHERE  cd_cds_missione = aCdCds AND
              cd_uo_missione = aCdUnitaOrganizzativa AND
              esercizio_missione = aEsercizio AND
              pg_missione = aPgMissione AND
              pg_compenso > 0 AND
              stato_cofi != 'A';

   END IF;

   RETURN aRecCompenso;

EXCEPTION

   WHEN no_data_found THEN
        IBMERR001.RAISE_ERR_GENERICO
           ('Non è stato trovato alcun compenso asociato alla missione U.O. ' || aCdUnitaOrganizzativa ||
            ' numero ' || aEsercizio || '/' || aPgMissione);

END getCompensoDaMissione;


-- =================================================================================================
-- Ritorna un record della tabella TIPO_TRATTAMENTO
-- =================================================================================================
FUNCTION getTipoTrattamento
   (
    aCdTrattamento VARCHAR2,
    aDataRif DATE
   ) RETURN TIPO_TRATTAMENTO%ROWTYPE IS
   aRecTipoTrattamento TIPO_TRATTAMENTO%ROWTYPE;

BEGIN

    SELECT * INTO aRecTipoTrattamento
    FROM   TIPO_TRATTAMENTO
    WHERE  cd_trattamento = aCdTrattamento AND
           dt_ini_validita <= aDataRif AND
           dt_fin_validita >= aDataRif;

   RETURN aRecTipoTrattamento;

EXCEPTION

   WHEN no_data_found THEN
        IBMERR001.RAISE_ERR_GENERICO
                  ('Tipo trattamento ' || aCdTrattamento ||
                   ' data riferimento ' || TO_CHAR(aDataRif,'DD/MM/YYYY') ||
                   ' non trovato');

END getTipoTrattamento;

-- =================================================================================================
-- Ritorna un record della tabella TIPO_TRATTAMENTO con gestione del trattamento chiuso
-- =================================================================================================
FUNCTION getTipoTrattamento
   (
    aCdTrattamento VARCHAR2,
    aDataRif DATE,
    aGestioneChiuso CHAR
   ) RETURN TIPO_TRATTAMENTO%ROWTYPE IS
   aRecTipoTrattamento TIPO_TRATTAMENTO%ROWTYPE;

BEGIN

    SELECT * INTO aRecTipoTrattamento
    FROM   TIPO_TRATTAMENTO
    WHERE  cd_trattamento = aCdTrattamento AND
           dt_ini_validita <= aDataRif AND
           dt_fin_validita >= aDataRif;

   RETURN aRecTipoTrattamento;

EXCEPTION

   WHEN no_data_found THEN

        IF (aGestioneChiuso IS NOT NULL AND
            aGestioneChiuso = 'Y') THEN

           BEGIN

              SELECT * INTO aRecTipoTrattamento
              FROM   TIPO_TRATTAMENTO A
              WHERE  A.cd_trattamento = aCdTrattamento AND
                     A.dt_fin_validita =
            (SELECT MAX(B.dt_fin_validita)
       FROM   TIPO_TRATTAMENTO B
                         WHERE  B.cd_trattamento = A.cd_trattamento);

              RETURN aRecTipoTrattamento;

            EXCEPTION

               WHEN no_data_found THEN

                    IBMERR001.RAISE_ERR_GENERICO
                        ('Tipo trattamento ' || aCdTrattamento ||
                         ' data riferimento ' || TO_CHAR(aDataRif,'DD/MM/YYYY') || ' non trovato');

            END;

        ELSE

           IBMERR001.RAISE_ERR_GENERICO
                     ('Tipo trattamento ' || aCdTrattamento ||
                      ' data riferimento ' || TO_CHAR(aDataRif,'DD/MM/YYYY') || ' non trovato');

        END IF;

END getTipoTrattamento;

--==================================================================================================
-- Ritorna un record della tabella SCAGLIONE
--==================================================================================================
FUNCTION getScaglione
   (
    aCdContributoRitenuta SCAGLIONE.cd_contributo_ritenuta%TYPE,
    aTiAnagrafico SCAGLIONE.ti_anagrafico%TYPE,
    aDataRif DATE,
    aImportoRif IN OUT SCAGLIONE.im_inferiore%TYPE,
    aAliquota IN OUT SCAGLIONE.aliquota%TYPE,
    aCdRegione SCAGLIONE.cd_regione%TYPE,
    aCdProvincia SCAGLIONE.cd_provincia%TYPE,
    aPgComune SCAGLIONE.pg_comune%TYPE
   ) RETURN V_PRE_SCAGLIONE%ROWTYPE IS
   chkEsisteScaglione INTEGER;
   isEnte SCAGLIONE.ti_ente_percipiente%TYPE;
   isPercipiente SCAGLIONE.ti_ente_percipiente%TYPE;
   aContatore BINARY_INTEGER;
   gen_cur GenericCurTyp;
   aRecTmpVPreScaglione V_PRE_SCAGLIONE%ROWTYPE;
   aRecVPreScaglione V_PRE_SCAGLIONE%ROWTYPE;

BEGIN
   -------------------------------------------------------------------------------------------------
   -- Entro per aliquota quando questa è valorizzata. Generalmente solo IRPEF a scaglioni se
   -- valorizzata in anagrafico

   IF aAliquota != 0 THEN

      SELECT COUNT(*) INTO chkEsisteScaglione
      FROM   DUAL
      WHERE  EXISTS
             (SELECT 1
              FROM   SCAGLIONE
              WHERE  cd_contributo_ritenuta = aCdContributoRitenuta AND
                     (ti_anagrafico = aTiAnagrafico OR
                      ti_anagrafico = '*') AND
                     dt_inizio_validita <=aDataRif AND
                     dt_fine_validita >= aDataRif AND
                     cd_regione = aCdRegione AND
                     cd_provincia = aCdProvincia AND
                     pg_comune = aPgComune AND
                     aliquota = aAliquota);

      IF chkEsisteScaglione = 0 Then
         IBMERR001.RAISE_ERR_GENERICO
                  ('L''aliquota presente nei dati Anagrafici del percipiente non è valida.');
         --aImportoRif:=0;
         --aAliquota:=0;
      END IF;

   END IF;

   -------------------------------------------------------------------------------------------------
   -- Lettura degli scaglioni e controllo consistenza di questi

   BEGIN

      aContatore:=0;
      isEnte:='N';
      isPercipiente:='N';

      IF aAliquota != 0 THEN

         OPEN gen_cur FOR

              SELECT *
              FROM   V_PRE_SCAGLIONE
              WHERE  cd_contributo_ritenuta = aCdContributoRitenuta AND
                     (ti_anagrafico = aTiAnagrafico OR
                      ti_anagrafico = '*') AND
                     dt_inizio_validita <=aDataRif AND
                     dt_fine_validita >= aDataRif AND
                     cd_regione = aCdRegione AND
                     cd_provincia = aCdProvincia AND
                     pg_comune = aPgComune AND
                     (aliquota_ente = aAliquota OR
                      aliquota_percip = aAliquota);

      ELSE

         OPEN gen_cur FOR

              SELECT *
              FROM   V_PRE_SCAGLIONE
              WHERE  cd_contributo_ritenuta = aCdContributoRitenuta AND
                     (ti_anagrafico = aTiAnagrafico OR
                      ti_anagrafico = '*') AND
                     dt_inizio_validita <=aDataRif AND
                     dt_fine_validita >= aDataRif AND
                     cd_regione = aCdRegione AND
                     cd_provincia = aCdProvincia AND
                     pg_comune = aPgComune AND
                     im_inferiore <= aImportoRif AND
                     im_superiore >= aImportoRif;

      END IF;

      LOOP

         FETCH gen_cur INTO
               aRecTmpVPreScaglione;

         EXIT WHEN gen_cur%NOTFOUND;

         aContatore:=aContatore + 1;

         IF aContatore = 1 THEN
            aRecVPreScaglione:=aRecTmpVPreScaglione;
         END IF;

         IF aRecTmpVPreScaglione.ti_ente_percipiente = 'E' THEN
            IF isEnte = 'Y' THEN
               IBMERR001.RAISE_ERR_GENERICO
                  ('Trovati più di un record riferito all''ente per il codice contributo ritenuta ' ||
                   aCdContributoRitenuta);
            END IF;
            isEnte:='Y';
            aRecVPreScaglione.aliquota_ente:=aRecTmpVPreScaglione.aliquota_ente;
            aRecVPreScaglione.base_calcolo_ente:=aRecTmpVPreScaglione.base_calcolo_ente;
         ELSE
            IF isPercipiente = 'Y' THEN
               IBMERR001.RAISE_ERR_GENERICO
                  ('Trovati più di un record riferito all percipiente per il codice contributo ritenuta ' ||
                   aCdContributoRitenuta);
            END IF;
            isPercipiente:='Y';
            aRecVPreScaglione.aliquota_percip:=aRecTmpVPreScaglione.aliquota_percip;
            aRecVPreScaglione.base_calcolo_percip:=aRecTmpVPreScaglione.base_calcolo_percip;
         END IF;

      END LOOP;

      CLOSE gen_cur;

      IF (isEnte = 'Y' AND
          isPercipiente = 'Y') THEN
         aRecVPreScaglione.ti_ente_percipiente:='*';
      END IF;

      -- Se non è stato trovato alcun record è sollevata un'eccezione

      IF aContatore = 0 THEN
         IBMERR001.RAISE_ERR_GENERICO
            ('Riferimento allo scaglione per contributo ritenuta ' || aCdContributoRitenuta || ' / Regione '||aCdRegione||' / Comune '||aPgComune||' non trovato'||aTiAnagrafico||' '||aDataRif||' '||aCdProvincia||' '||aImportoRif );
      END IF;

   END;

   RETURN aRecVPreScaglione;

END getScaglione;

--==================================================================================================
-- Ritorna 'Y' o 'N' a seconda che esista o meno il compenso
--==================================================================================================
FUNCTION chkEsisteCompenso
   (
    aCdCds VARCHAR2,
    aCdUnitaOrganizzativa VARCHAR2,
    aEsercizio NUMBER,
    aPgCompenso NUMBER
   ) RETURN CHAR IS
   flEsisteCompenso CHAR(1);

BEGIN

    SELECT DECODE(COUNT(*),0,'N','Y') INTO flEsisteCompenso
    FROM   DUAL
    WHERE  EXISTS
           (SELECT 1
            FROM   COMPENSO
            WHERE  cd_cds = aCdCds AND
                   cd_unita_organizzativa = aCdUnitaOrganizzativa AND
                   esercizio = aEsercizio AND
                   pg_compenso = aPgCompenso);

   RETURN flEsisteCompenso;

END chkEsisteCompenso;

--==================================================================================================
-- Ritorna l'imponibile base di un tipo contributo ritenuta
--==================================================================================================
FUNCTION getImponibileBaseCori
   (
    aRecCompenso COMPENSO%ROWTYPE,
    cdClassificazioneCori TIPO_CONTRIBUTO_RITENUTA.cd_classificazione_cori%TYPE
   ) RETURN NUMBER IS
   aImponibile NUMBER(15,2);

BEGIN

   IF    cdClassificazioneCori = isCoriIva THEN
         aImponibile:=aRecCompenso.im_lordo_percipiente - aRecCompenso.quota_esente_no_iva;
   ELSIF (cdClassificazioneCori = isCoriFiscale OR
          getIsAddTerritorio(cdClassificazioneCori) = 'Y') THEN
         aImponibile:=aRecCompenso.im_lordo_percipiente - aRecCompenso.quota_esente - aRecCompenso.im_no_fiscale;
   ELSIF getIsAddTerritorioRecRate(cdClassificazioneCori) = 'Y' THEN
         aImponibile:=0;
   Elsif cdClassificazioneCori = isCoriAddComAcconto Then
         aImponibile:=0;
   ELSE
         aImponibile:=aRecCompenso.im_lordo_percipiente - aRecCompenso.quota_esente;
   END IF;

   RETURN aImponibile;

END getImponibileBaseCori;

--==================================================================================================
-- Elimina un intero COMPENSO
--==================================================================================================
PROCEDURE eliminaFisicoCompenso
   (
    aCdCds COMPENSO.cd_cds%TYPE,
    aCdUnitaOrganizzativa COMPENSO.cd_unita_organizzativa%TYPE,
    aEsercizio COMPENSO.esercizio%TYPE,
    aPgCompenso COMPENSO.pg_compenso%TYPE
   ) IS

BEGIN

   DELETE FROM COMPENSO
   WHERE cd_cds = aCdCds AND
         cd_unita_organizzativa = aCdUnitaOrganizzativa AND
         esercizio = aEsercizio AND
         pg_compenso = aPgCompenso;

END eliminaFisicoCompenso;

--==================================================================================================
-- Elimina logicamente un COMPENSO
--==================================================================================================
PROCEDURE eliminaLogicoCompenso
   (
    aCdCds COMPENSO.cd_cds%TYPE,
    aCdUnitaOrganizzativa COMPENSO.cd_unita_organizzativa%TYPE,
    aEsercizio COMPENSO.esercizio%TYPE,
    aPgCompenso COMPENSO.pg_compenso%TYPE,
    aUtente COMPENSO.utuv%TYPE,
    aTiOrigineElimina CHAR
   ) IS
   dataOdierna DATE;

BEGIN

   dataOdierna:=SYSDATE;

   -- Origine eliminazione logica = MISSIONE

   IF    aTiOrigineElimina = 'MI' THEN

         UPDATE COMPENSO
         SET    stato_cofi = 'A',
                dt_cancellazione = dataOdierna,
                pg_missione = null,
                cd_cds_missione = null,
                esercizio_missione = null,
                cd_uo_missione = null,
                fl_diaria = 'N'
         WHERE  cd_cds = aCdCds AND
                cd_unita_organizzativa = aCdUnitaOrganizzativa AND
                esercizio = aEsercizio AND
                pg_compenso = aPgCompenso;

   -- Origine eliminazione logica = CONGUAGLIO

   ELSIF aTiOrigineElimina = 'GN' THEN

         UPDATE COMPENSO
         SET    stato_cofi = 'A',
                dt_cancellazione = dataOdierna,
                duva = dataOdierna,
                utuv = aUtente,
                pg_ver_rec = pg_ver_rec + 1
         WHERE  cd_cds = aCdCds AND
                cd_unita_organizzativa = aCdUnitaOrganizzativa AND
                esercizio = aEsercizio AND
                pg_compenso = aPgCompenso;

   ELSIF aTiOrigineElimina = 'G1' THEN

         UPDATE COMPENSO
         SET    stato_cofi = 'A',
                dt_cancellazione = dataOdierna,
                stato_coge = CNRCTB100.STATO_COEP_DA_RIP,
                duva = dataOdierna,
                utuv = aUtente,
                pg_ver_rec = pg_ver_rec + 1
         WHERE  cd_cds = aCdCds AND
                cd_unita_organizzativa = aCdUnitaOrganizzativa AND
                esercizio = aEsercizio AND
                pg_compenso = aPgCompenso;

   ELSIF aTiOrigineElimina = 'G2' THEN

         UPDATE COMPENSO
         SET    stato_cofi = 'A',
                dt_cancellazione = dataOdierna,
                stato_coan = CNRCTB100.STATO_COEP_DA_RIP,
                duva = dataOdierna,
                utuv = aUtente,
                pg_ver_rec = pg_ver_rec + 1
         WHERE  cd_cds = aCdCds AND
                cd_unita_organizzativa = aCdUnitaOrganizzativa AND
                esercizio = aEsercizio AND
                pg_compenso = aPgCompenso;

   ELSIF aTiOrigineElimina = 'GY' THEN

         UPDATE COMPENSO
         SET    stato_cofi = 'A',
                dt_cancellazione = dataOdierna,
                stato_coge = CNRCTB100.STATO_COEP_DA_RIP,
                stato_coan = CNRCTB100.STATO_COEP_DA_RIP,
                duva = dataOdierna,
                utuv = aUtente,
                pg_ver_rec = pg_ver_rec + 1
         WHERE  cd_cds = aCdCds AND
                cd_unita_organizzativa = aCdUnitaOrganizzativa AND
                esercizio = aEsercizio AND
                pg_compenso = aPgCompenso;

   END IF;

END eliminaLogicoCompenso;


--==================================================================================================
-- Elimina CONTRIBUTO_RITENUTA e CONTRIBUTO_RITENUTA_DET di un compenso
--==================================================================================================
PROCEDURE cancellaDetCompenso
   (
    aCdCds COMPENSO.cd_cds%TYPE,
    aCdUnitaOrganizzativa COMPENSO.cd_unita_organizzativa%TYPE,
    aEsercizio COMPENSO.esercizio%TYPE,
    aPgCompenso COMPENSO.pg_compenso%TYPE
   ) IS

BEGIN

   DELETE FROM CONTRIBUTO_RITENUTA
   WHERE cd_cds = aCdCds AND
         cd_unita_organizzativa = aCdUnitaOrganizzativa AND
         esercizio = aEsercizio AND
         pg_compenso = aPgCompenso;

END cancellaDetCompenso;


--==================================================================================================
-- Inserimento in COMPENSO
--==================================================================================================
PROCEDURE insCompenso
   (
    aRecCompenso COMPENSO%ROWTYPE
   ) IS

BEGIN

   INSERT INTO COMPENSO
          (CD_CDS,
           CD_UNITA_ORGANIZZATIVA,
           ESERCIZIO,
           PG_COMPENSO,
           CD_CDS_ORIGINE,
           CD_UO_ORIGINE,
           DT_REGISTRAZIONE,
           DS_COMPENSO,
           TI_ANAGRAFICO,
           CD_TERZO,
           CD_TERZO_UO_CDS,
           RAGIONE_SOCIALE,
           NOME,
           COGNOME,
           CODICE_FISCALE,
           PARTITA_IVA,
           CD_TERMINI_PAG,
           CD_TERMINI_PAG_UO_CDS,
           CD_MODALITA_PAG,
           CD_MODALITA_PAG_UO_CDS,
           PG_BANCA,
           PG_BANCA_UO_CDS,
           CD_TIPO_RAPPORTO,
           CD_TRATTAMENTO,
           FL_SENZA_CALCOLI,
           FL_DIARIA,
           DT_CANCELLAZIONE,
           STATO_COFI,
           STATO_COGE,
           STATO_COAN,
           TI_ASSOCIATO_MANREV,
           DT_EMISSIONE_MANDATO,
           DT_TRASMISSIONE_MANDATO,
           DT_PAGAMENTO_MANDATO,
           DT_DA_COMPETENZA_COGE,
           DT_A_COMPETENZA_COGE,
           STATO_PAGAMENTO_FONDO_ECO,
           DT_PAGAMENTO_FONDO_ECO,
           IM_TOTALE_COMPENSO,
           IM_LORDO_PERCIPIENTE,
           IM_NETTO_PERCIPIENTE,
           IM_CR_PERCIPIENTE,
           IM_CR_ENTE,
           QUOTA_ESENTE,
           QUOTA_ESENTE_NO_IVA,
           IM_NO_FISCALE,
           IMPONIBILE_FISCALE,
           CD_VOCE_IVA,
           IMPONIBILE_IVA,
           PG_COMUNE_ADD,
           CD_PROVINCIA_ADD,
           CD_REGIONE_ADD,
           CD_REGIONE_IRAP,
           CD_CDS_MISSIONE,
           ESERCIZIO_MISSIONE,
           PG_MISSIONE,
           CD_UO_MISSIONE,
           CD_CDS_OBBLIGAZIONE,
           ESERCIZIO_OBBLIGAZIONE,
           ESERCIZIO_ORI_OBBLIGAZIONE,
           PG_OBBLIGAZIONE,
           PG_OBBLIGAZIONE_SCADENZARIO,
           CD_CDS_ACCERTAMENTO,
           ESERCIZIO_ACCERTAMENTO,
           ESERCIZIO_ORI_ACCERTAMENTO,
           PG_ACCERTAMENTO,
           PG_ACCERTAMENTO_SCADENZARIO,
           DACR,
           UTCR,
           DUVA,
           UTUV,
           PG_VER_REC,
           CD_TIPOLOGIA_RISCHIO,
           DETRAZIONI_PERSONALI,
           DETRAZIONI_LA,
           DETRAZIONE_CONIUGE,
           DETRAZIONE_FIGLI,
           DETRAZIONE_ALTRI,
           DETRAZIONE_RIDUZIONE_CUNEO,
           DETRAZIONI_PERSONALI_NETTO,
           DETRAZIONI_LA_NETTO,
           DETRAZIONE_CONIUGE_NETTO,
           DETRAZIONE_FIGLI_NETTO,
           DETRAZIONE_ALTRI_NETTO,
           DETRAZIONE_RID_CUNEO_NETTO,
           CD_CDS_DOC_GENRC,
           CD_UO_DOC_GENRC,
           ESERCIZIO_DOC_GENRC,
           CD_TIPO_DOC_GENRC,
           PG_DOC_GENRC,
           CD_CDR_GENRC,
           CD_LINEA_ATTIVITA_GENRC,
           TI_ISTITUZ_COMMERC,
           IMPONIBILE_INAIL,
           ESERCIZIO_FATTURA_FORNITORE,
           DT_FATTURA_FORNITORE,
           NR_FATTURA_FORNITORE,
           FL_GENERATA_FATTURA,
           FL_COMPENSO_STIPENDI,
           FL_COMPENSO_CONGUAGLIO,
           FL_COMPENSO_MINICARRIERA,
           ALIQUOTA_IRPEF_DA_MISSIONE,
           FL_COMPENSO_MCARRIERA_TASSEP,
           ALIQUOTA_IRPEF_TASSEP,
           IM_DEDUZIONE_IRPEF,
           IMPONIBILE_FISCALE_NETTO,
           NUMERO_GIORNI,
           FL_ESCLUDI_QVARIA_DEDUZIONE,
           FL_INTERA_QFISSA_DEDUZIONE,
           IM_DETRAZIONE_PERSONALE_ANAG,
           FL_RECUPERO_RATE,
           ALIQUOTA_FISCALE,
           FL_ACCANTONA_ADD_TERR)
   VALUES (aRecCompenso.cd_cds,
           aRecCompenso.cd_unita_organizzativa,
           aRecCompenso.esercizio,
           aRecCompenso.pg_compenso,
           aRecCompenso.cd_cds_origine,
           aRecCompenso.cd_uo_origine,
           aRecCompenso.dt_registrazione,
           aRecCompenso.ds_compenso,
           aRecCompenso.ti_anagrafico,
           aRecCompenso.cd_terzo,
           aRecCompenso.cd_terzo_uo_cds,
           aRecCompenso.ragione_sociale,
           aRecCompenso.nome,
           aRecCompenso.cognome,
           aRecCompenso.codice_fiscale,
           aRecCompenso.partita_iva,
           aRecCompenso.cd_termini_pag,
           aRecCompenso.cd_termini_pag_uo_cds,
           aRecCompenso.cd_modalita_pag,
           aRecCompenso.cd_modalita_pag_uo_cds,
           aRecCompenso.pg_banca,
           aRecCompenso.pg_banca_uo_cds,
           aRecCompenso.cd_tipo_rapporto,
           aRecCompenso.cd_trattamento,
           aRecCompenso.fl_senza_calcoli,
           aRecCompenso.fl_diaria,
           aRecCompenso.dt_cancellazione,
           aRecCompenso.stato_cofi,
           aRecCompenso.stato_coge,
           aRecCompenso.stato_coan,
           aRecCompenso.ti_associato_manrev,
           aRecCompenso.dt_emissione_mandato,
           aRecCompenso.dt_trasmissione_mandato,
           aRecCompenso.dt_pagamento_mandato,
           aRecCompenso.dt_da_competenza_coge,
           aRecCompenso.dt_a_competenza_coge,
           aRecCompenso.stato_pagamento_fondo_eco,
           aRecCompenso.dt_pagamento_fondo_eco,
           aRecCompenso.im_totale_compenso,
           aRecCompenso.im_lordo_percipiente,
           aRecCompenso.im_netto_percipiente,
           aRecCompenso.im_cr_percipiente,
           aRecCompenso.im_cr_ente,
           aRecCompenso.quota_esente,
           aRecCompenso.quota_esente_no_iva,
           aRecCompenso.im_no_fiscale,
           aRecCompenso.imponibile_fiscale,
           aRecCompenso.cd_voce_iva,
           aRecCompenso.imponibile_iva,
           aRecCompenso.pg_comune_add,
           aRecCompenso.cd_provincia_add,
           aRecCompenso.cd_regione_add,
           aRecCompenso.cd_regione_irap,
           aRecCompenso.cd_cds_missione,
           aRecCompenso.esercizio_missione,
           aRecCompenso.pg_missione,
           aRecCompenso.cd_uo_missione,
           aRecCompenso.cd_cds_obbligazione,
           aRecCompenso.esercizio_obbligazione,
           aRecCompenso.esercizio_ori_obbligazione,
           aRecCompenso.pg_obbligazione,
           aRecCompenso.pg_obbligazione_scadenzario,
           aRecCompenso.cd_cds_accertamento,
           aRecCompenso.esercizio_accertamento,
           aRecCompenso.esercizio_ori_accertamento,
           aRecCompenso.pg_accertamento,
           aRecCompenso.pg_accertamento_scadenzario,
           aRecCompenso.dacr,
           aRecCompenso.utcr,
           aRecCompenso.duva,
           aRecCompenso.utuv,
           aRecCompenso.pg_ver_rec,
           aRecCompenso.cd_tipologia_rischio,
           aRecCompenso.detrazioni_personali,
           aRecCompenso.detrazioni_la,
           aRecCompenso.detrazione_coniuge,
           aRecCompenso.detrazione_figli,
           aRecCompenso.detrazione_altri,
           nvl(aRecCompenso.detrazione_riduzione_cuneo,0),
           aRecCompenso.detrazioni_personali_netto,
           aRecCompenso.detrazioni_la_netto,
           aRecCompenso.detrazione_coniuge_netto,
           aRecCompenso.detrazione_figli_netto,
           aRecCompenso.detrazione_altri_netto,
           nvl(aRecCompenso.detrazione_rid_cuneo_netto,0),
           aRecCompenso.cd_cds_doc_genrc,
           aRecCompenso.cd_uo_doc_genrc,
           aRecCompenso.esercizio_doc_genrc,
           aRecCompenso.cd_tipo_doc_genrc,
           aRecCompenso.pg_doc_genrc,
           aRecCompenso.cd_cdr_genrc,
           aRecCompenso.cd_linea_attivita_genrc,
           aRecCompenso.ti_istituz_commerc,
           aRecCompenso.imponibile_inail,
           aRecCompenso.esercizio_fattura_fornitore,
           aRecCompenso.dt_fattura_fornitore,
           aRecCompenso.nr_fattura_fornitore,
           aRecCompenso.fl_generata_fattura,
           aRecCompenso.fl_compenso_stipendi,
           aRecCompenso.fl_compenso_conguaglio,
           aRecCompenso.fl_compenso_minicarriera,
           aRecCompenso.aliquota_irpef_da_missione,
           aRecCompenso.fl_compenso_mcarriera_tassep,
           aRecCompenso.aliquota_irpef_tassep,
           aRecCompenso.im_deduzione_irpef,
           aRecCompenso.imponibile_fiscale_netto,
           aRecCompenso.numero_giorni,
           aRecCompenso.fl_escludi_qvaria_deduzione,
           aRecCompenso.fl_intera_qfissa_deduzione,
           aRecCompenso.im_detrazione_personale_anag,
           aRecCompenso.fl_recupero_rate,
           aRecCompenso.aliquota_fiscale,
           aRecCompenso.fl_accantona_add_terr);

END insCompenso;


--==================================================================================================
-- Inserimento in CONTRIBUTO_RITENUTA
--==================================================================================================
PROCEDURE insContributoRitenuta
   (
    aRecContributoRitenuta CONTRIBUTO_RITENUTA%ROWTYPE
   ) IS

BEGIN
   INSERT INTO CONTRIBUTO_RITENUTA
          (CD_CDS,
           CD_UNITA_ORGANIZZATIVA,
           ESERCIZIO,
           PG_COMPENSO,
           CD_CONTRIBUTO_RITENUTA,
           TI_ENTE_PERCIPIENTE,
           DT_INI_VALIDITA,
           MONTANTE,
           IMPONIBILE,
           ALIQUOTA,
           BASE_CALCOLO,
           AMMONTARE,
           STATO_COFI_CR,
           CD_CDS_OBBLIGAZIONE,
           ESERCIZIO_OBBLIGAZIONE,
           ESERCIZIO_ORI_OBBLIGAZIONE,
           PG_OBBLIGAZIONE,
           PG_OBBLIGAZIONE_SCADENZARIO,
           CD_CDS_ACCERTAMENTO,
           ESERCIZIO_ACCERTAMENTO,
           ESERCIZIO_ORI_ACCERTAMENTO,
           PG_ACCERTAMENTO,
           PG_ACCERTAMENTO_SCADENZARIO,
           DACR,
           UTCR,
           DUVA,
           UTUV,
           PG_VER_REC,
           AMMONTARE_LORDO,
           IMPONIBILE_LORDO,
           IM_DEDUZIONE_IRPEF,
           IM_DEDUZIONE_FAMILY,
           IM_CORI_SOSPESO,
           fl_credito_pareggio_detrazioni)
   VALUES (aRecContributoRitenuta.cd_cds,
           aRecContributoRitenuta.cd_unita_organizzativa,
           aRecContributoRitenuta.esercizio,
           aRecContributoRitenuta.pg_compenso,
           aRecContributoRitenuta.cd_contributo_ritenuta,
           aRecContributoRitenuta.ti_ente_percipiente,
           aRecContributoRitenuta.dt_ini_validita,
           aRecContributoRitenuta.montante,
           aRecContributoRitenuta.imponibile,
           aRecContributoRitenuta.aliquota,
           aRecContributoRitenuta.base_calcolo,
           aRecContributoRitenuta.ammontare,
           aRecContributoRitenuta.stato_cofi_cr,
           aRecContributoRitenuta.cd_cds_obbligazione,
           aRecContributoRitenuta.esercizio_obbligazione,
           aRecContributoRitenuta.esercizio_ori_obbligazione,
           aRecContributoRitenuta.pg_obbligazione,
           aRecContributoRitenuta.pg_obbligazione_scadenzario,
           aRecContributoRitenuta.cd_cds_accertamento,
           aRecContributoRitenuta.esercizio_accertamento,
           aRecContributoRitenuta.esercizio_ori_accertamento,
           aRecContributoRitenuta.pg_accertamento,
           aRecContributoRitenuta.pg_accertamento_scadenzario,
           aRecContributoRitenuta.dacr,
           aRecContributoRitenuta. utcr,
           aRecContributoRitenuta.duva,
           aRecContributoRitenuta.utuv,
           aRecContributoRitenuta.pg_ver_rec,
           aRecContributoRitenuta.ammontare_lordo,
           aRecContributoRitenuta.imponibile_lordo,
           aRecContributoRitenuta.im_deduzione_irpef,
           aRecContributoRitenuta.im_deduzione_family,
           aRecContributoRitenuta.im_cori_sospeso,
           aRecContributoRitenuta.fl_credito_pareggio_detrazioni);

END insContributoRitenuta;


--==================================================================================================
-- Inserimento in CONTRIBUTO_RITENUTA_DET
--==================================================================================================
PROCEDURE insContributoRitenutaDet
   (
    aRecContributoRitenutaDet CONTRIBUTO_RITENUTA_DET%ROWTYPE
   ) IS

BEGIN

   INSERT INTO CONTRIBUTO_RITENUTA_DET
          (CD_CDS,
           CD_UNITA_ORGANIZZATIVA,
           ESERCIZIO,
           PG_COMPENSO,
           CD_CONTRIBUTO_RITENUTA,
           TI_ENTE_PERCIPIENTE,
           PG_RIGA,
           IMPONIBILE,
           ALIQUOTA,
           BASE_CALCOLO,
           AMMONTARE,
           DACR,
           UTCR,
           DUVA,
           UTUV,
           PG_VER_REC)
   VALUES (aRecContributoRitenutaDet.cd_cds,
           aRecContributoRitenutaDet.cd_unita_organizzativa,
           aRecContributoRitenutaDet.esercizio,
           aRecContributoRitenutaDet.pg_compenso,
           aRecContributoRitenutaDet.cd_contributo_ritenuta,
           aRecContributoRitenutaDet.ti_ente_percipiente,
           aRecContributoRitenutaDet.pg_riga,
           aRecContributoRitenutaDet.imponibile,
           aRecContributoRitenutaDet.aliquota,
           aRecContributoRitenutaDet.base_calcolo,
           aRecContributoRitenutaDet.ammontare,
           aRecContributoRitenutaDet.dacr,
           aRecContributoRitenutaDet.utcr,
           aRecContributoRitenutaDet.duva,
           aRecContributoRitenutaDet.utuv,
           aRecContributoRitenutaDet.pg_ver_rec);

END insContributoRitenutaDet;


--==================================================================================================
-- Copia l'intera struttura dati di un compenso (utilizzato in modifica COMPENSO)
--==================================================================================================
PROCEDURE copiaCompenso
   (
    aCdCds VARCHAR2,
    aCdUnitaOrganizzativa VARCHAR2,
    aEsercizio NUMBER,
    aPgCompenso NUMBER,
    aCdCdsCopia VARCHAR2,
    aCdUnitaOrganizzativaCopia VARCHAR2,
    aEsercizioCopia NUMBER,
    aPgCompensoCopia NUMBER
   ) IS
   aRecCompenso COMPENSO%ROWTYPE;
   aRecContributoRitenuta CONTRIBUTO_RITENUTA%ROWTYPE;
   aRecContributoRitenutaDet CONTRIBUTO_RITENUTA_DET%ROWTYPE;
   gen_cur GenericCurTyp;

BEGIN

   BEGIN

      -- Copia del compenso

      INSERT INTO COMPENSO
             (cd_cds,
              cd_unita_organizzativa,
              esercizio,
              pg_compenso,
              cd_cds_origine,
              cd_uo_origine,
              dt_registrazione,
              ds_compenso,
              ti_anagrafico,
              cd_terzo,
              cd_terzo_uo_cds,
              ragione_sociale,
              nome,
              cognome,
              codice_fiscale,
              partita_iva,
              cd_termini_pag,
              cd_termini_pag_uo_cds,
              cd_modalita_pag,
              cd_modalita_pag_uo_cds,
              pg_banca,
              pg_banca_uo_cds,
              cd_tipo_rapporto,
              cd_trattamento,
              fl_senza_calcoli,
              fl_diaria,
              dt_cancellazione,
              stato_cofi,
              stato_coge,
              stato_coan,
              ti_associato_manrev,
              dt_emissione_mandato,
              dt_trasmissione_mandato,
              dt_pagamento_mandato,
              dt_da_competenza_coge,
              dt_a_competenza_coge,
              stato_pagamento_fondo_eco,
              dt_pagamento_fondo_eco,
              im_totale_compenso,
              im_lordo_percipiente,
              im_netto_percipiente,
              im_cr_percipiente,
              im_cr_ente,
              quota_esente,
              quota_esente_no_iva,
              im_no_fiscale,
              imponibile_fiscale,
              cd_voce_iva,
              imponibile_iva,
              pg_comune_add,
              cd_provincia_add,
              cd_regione_add,
              cd_regione_irap,
              cd_cds_missione,
              esercizio_missione,
              cd_uo_missione,
              pg_missione,
              cd_cds_obbligazione,
              esercizio_obbligazione,
              esercizio_ori_obbligazione,
              pg_obbligazione,
              pg_obbligazione_scadenzario,
              cd_cds_accertamento,
              esercizio_accertamento,
              esercizio_ori_accertamento,
              pg_accertamento,
              pg_accertamento_scadenzario,
              duva,
              utcr,
              dacr,
              utuv,
              pg_ver_rec,
              cd_tipologia_rischio,
              detrazioni_personali,
              detrazioni_la,
              detrazione_coniuge,
              detrazione_figli,
              detrazione_altri,
              detrazione_riduzione_cuneo,
              detrazioni_personali_netto,
              detrazioni_la_netto,
              detrazione_coniuge_netto,
              detrazione_figli_netto,
              detrazione_altri_netto,
              detrazione_rid_cuneo_netto,
              cd_cds_doc_genrc,
              cd_uo_doc_genrc,
              esercizio_doc_genrc,
              cd_tipo_doc_genrc,
              pg_doc_genrc,
              cd_cdr_genrc,
              cd_linea_attivita_genrc,
              ti_istituz_commerc,
              imponibile_inail,
              esercizio_fattura_fornitore,
              dt_fattura_fornitore,
              nr_fattura_fornitore,
              fl_generata_fattura,
              fl_compenso_stipendi,
              fl_compenso_conguaglio,
              fl_compenso_minicarriera,
              aliquota_irpef_da_missione,
              fl_compenso_mcarriera_tassep,
              aliquota_irpef_tassep,
              im_deduzione_irpef,
              imponibile_fiscale_netto,
              numero_giorni,
              fl_escludi_qvaria_deduzione,
              fl_intera_qfissa_deduzione,
              im_detrazione_personale_anag,
              fl_recupero_rate,
              aliquota_fiscale,
              fl_accantona_add_terr,
              quota_esente_inps,
              cd_rapporto_inps,
              cd_attivita_inps,
              cd_altra_ass_inps,
              pg_comune_inps,
              im_reddito_complessivo,
              im_reddito_abitaz_princ,
              esercizio_rep,
              pg_repertorio,
              esercizio_limite_rep,
              im_netto_da_trattenere,
              ti_prestazione,
              esercizio_bonus,
              pg_bonus,
              fl_liquidazione_differita,
              cd_terzo_pignorato,
              esercizio_contratto,
              stato_contratto,
              pg_contratto,
              im_tot_reddito_complessivo,
              PG_TROVATO,
              DATA_PROTOCOLLO,
              NUMERO_PROTOCOLLO,
              DT_SCADENZA,
              STATO_LIQUIDAZIONE,
              CAUSALE,
              fl_documento_ele  )
       SELECT aCdCdsCopia,
              aCdUnitaOrganizzativaCopia,
              aEsercizioCopia,
              aPgCompensoCopia,
              A.cd_cds_origine,
              A.cd_uo_origine,
              A.dt_registrazione,
              A.ds_compenso,
              A.ti_anagrafico,
              A.cd_terzo,
              A.cd_terzo_uo_cds,
              A.ragione_sociale,
              A.nome,
              A.cognome,
              A.codice_fiscale,
              A.partita_iva,
              A.cd_termini_pag,
              A.cd_termini_pag_uo_cds,
              A.cd_modalita_pag,
              A.cd_modalita_pag_uo_cds,
              A.pg_banca,
              A.pg_banca_uo_cds,
              A.cd_tipo_rapporto,
              A.cd_trattamento,
              A.fl_senza_calcoli,
              A.fl_diaria,
              A.dt_cancellazione,
              A.stato_cofi,
              A.stato_coge,
              A.stato_coan,
              A.ti_associato_manrev,
              A.dt_emissione_mandato,
              A.dt_trasmissione_mandato,
              A.dt_pagamento_mandato,
              A.dt_da_competenza_coge,
              A.dt_a_competenza_coge,
              A.stato_pagamento_fondo_eco,
              A.dt_pagamento_fondo_eco,
              A.im_totale_compenso,
              A.im_lordo_percipiente,
              A.im_netto_percipiente,
              A.im_cr_percipiente,
              A.im_cr_ente,
              A.quota_esente,
              A.quota_esente_no_iva,
              A.im_no_fiscale,
              A.imponibile_fiscale,
              A.cd_voce_iva,
              A.imponibile_iva,
              A.pg_comune_add,
              A.cd_provincia_add,
              A.cd_regione_add,
              A.cd_regione_irap,
              A.cd_cds_missione,
              A.esercizio_missione,
              A.cd_uo_missione,
              A.pg_missione,
              A.cd_cds_obbligazione,
              A.esercizio_obbligazione,
              A.esercizio_ori_obbligazione,
              A.pg_obbligazione,
              A.pg_obbligazione_scadenzario,
              A.cd_cds_accertamento,
              A.esercizio_accertamento,
              A.esercizio_ori_accertamento,
              A.pg_accertamento,
              A.pg_accertamento_scadenzario,
              A.duva,
              A.utcr,
              A.dacr,
              A.utuv,
              A.pg_ver_rec,
              A.cd_tipologia_rischio,
              A.detrazioni_personali,
              A.detrazioni_la,
              A.detrazione_coniuge,
              A.detrazione_figli,
              A.detrazione_altri,
              A.detrazione_riduzione_cuneo,
              A.detrazioni_personali_netto,
              A.detrazioni_la_netto,
              A.detrazione_coniuge_netto,
              A.detrazione_figli_netto,
              A.detrazione_altri_netto,
              A.detrazione_rid_cuneo_netto,
              A.cd_cds_doc_genrc,
              A.cd_uo_doc_genrc,
              A.esercizio_doc_genrc,
              A.cd_tipo_doc_genrc,
              A.pg_doc_genrc,
              A.cd_cdr_genrc,
              A.cd_linea_attivita_genrc,
              A.ti_istituz_commerc,
              A.imponibile_inail,
              A.esercizio_fattura_fornitore,
              A.dt_fattura_fornitore,
              A.nr_fattura_fornitore,
              A.fl_generata_fattura,
              A.fl_compenso_stipendi,
              A.fl_compenso_conguaglio,
              A.fl_compenso_minicarriera,
              A.aliquota_irpef_da_missione,
              A.fl_compenso_mcarriera_tassep,
              A.aliquota_irpef_tassep,
              A.im_deduzione_irpef,
              A.imponibile_fiscale_netto,
              A.numero_giorni,
              A.fl_escludi_qvaria_deduzione,
              A.fl_intera_qfissa_deduzione,
              A.im_detrazione_personale_anag,
              A.fl_recupero_rate,
              A.aliquota_fiscale,
              A.fl_accantona_add_terr,
              A.quota_esente_inps,
              A.cd_rapporto_inps,
              A.cd_attivita_inps,
              A.cd_altra_ass_inps,
              A.pg_comune_inps,
              A.im_reddito_complessivo,
              A.im_reddito_abitaz_princ,
              A.esercizio_rep,
              A.pg_repertorio,
              A.esercizio_limite_rep,
              A.im_netto_da_trattenere,
              A.ti_prestazione,
              A.esercizio_bonus,
              A.pg_bonus,
              A.fl_liquidazione_differita,
              A.cd_terzo_pignorato,
              A.esercizio_contratto,
              A.stato_contratto,
              A.pg_contratto,
              A.im_tot_reddito_complessivo,
              A.pg_trovato,
              A.data_protocollo,
              A.numero_protocollo,
              A.dt_scadenza,
              A.stato_liquidazione,
              A.causale,
              A.fl_documento_ele
       FROM   COMPENSO A
       WHERE  A.cd_cds = aCdCds AND
              A.esercizio = aEsercizio AND
              A.cd_unita_organizzativa = aCdUnitaOrganizzativa AND
              A.pg_compenso = aPgCompenso;

   END;

   BEGIN

      -- Copia del CONTRIBUTO_RITENUTA

      INSERT INTO CONTRIBUTO_RITENUTA
             (cd_cds,
              cd_unita_organizzativa,
              esercizio,
              pg_compenso,
              cd_contributo_ritenuta,
              ti_ente_percipiente,
              dt_ini_validita,
              montante,
              imponibile,
              aliquota,
              base_calcolo,
              ammontare,
              stato_cofi_cr,
              cd_cds_obbligazione,
              esercizio_obbligazione,
              esercizio_ori_obbligazione,
              pg_obbligazione,
              pg_obbligazione_scadenzario,
              cd_cds_accertamento,
              esercizio_accertamento,
              esercizio_ori_accertamento,
              pg_accertamento,
              pg_accertamento_scadenzario,
              dacr,
              utcr,
              duva,
              utuv,
              pg_ver_rec,
              ammontare_lordo,
              imponibile_lordo,
              im_deduzione_irpef,
              im_deduzione_family)
       SELECT aCdCdsCopia,
              aCdUnitaOrganizzativaCopia,
              aEsercizioCopia,
              aPgCompensoCopia,
              A.cd_contributo_ritenuta,
              A.ti_ente_percipiente,
              A.dt_ini_validita,
              A.montante,
              A.imponibile,
              A.aliquota,
              A.base_calcolo,
              A.ammontare,
              A.stato_cofi_cr,
              A.cd_cds_obbligazione,
              A.esercizio_obbligazione,
              A.esercizio_ori_obbligazione,
              A.pg_obbligazione,
              A.pg_obbligazione_scadenzario,
              A.cd_cds_accertamento,
              A.esercizio_accertamento,
              A.esercizio_ori_accertamento,
              A.pg_accertamento,
              A.pg_accertamento_scadenzario,
              A.dacr,
              A.utcr,
              A.duva,
              A.utuv,
              A.pg_ver_rec,
              A.ammontare_lordo,
              A.imponibile_lordo,
              A.im_deduzione_irpef,
              A.im_deduzione_family
       FROM   CONTRIBUTO_RITENUTA A
       WHERE  A.cd_cds = aCdCds AND
              A.esercizio = aEsercizio AND
              A.cd_unita_organizzativa = aCdUnitaOrganizzativa AND
              A.pg_compenso = aPgCompenso;

   END;

   BEGIN

      -- Copia del CONTRIBUTO_RITENUTA_DET

      INSERT INTO CONTRIBUTO_RITENUTA_DET
             (cd_cds,
              cd_unita_organizzativa,
              esercizio,
              pg_compenso,
              cd_contributo_ritenuta,
              ti_ente_percipiente,
              pg_riga,
              imponibile,
              aliquota,
              base_calcolo,
              ammontare,
              dacr,
              utcr,
              duva,
              utuv,
              pg_ver_rec)
       SELECT aCdCdsCopia,
              aCdUnitaOrganizzativaCopia,
              aEsercizioCopia,
              aPgCompensoCopia,
              A.cd_contributo_ritenuta,
              A.ti_ente_percipiente,
              A.pg_riga,
              A.imponibile,
              A.aliquota,
              A.base_calcolo,
              A.ammontare,
              A.dacr,
              A.utcr,
              A.duva,
              A.utuv,
              A.pg_ver_rec
       FROM   CONTRIBUTO_RITENUTA_DET A
       WHERE  A.cd_cds = aCdCds AND
              A.esercizio = aEsercizio AND
              A.cd_unita_organizzativa = aCdUnitaOrganizzativa AND
              A.pg_compenso = aPgCompenso;

   END;

   RETURN;

END copiaCompenso;

--==================================================================================================
-- Ritorna un record della tabella CONGUAGLIO
--==================================================================================================
FUNCTION getConguaglio
   (
    aCdCds CONGUAGLIO.cd_cds%TYPE,
    aCdUnitaOrganizzativa CONGUAGLIO.cd_unita_organizzativa%TYPE,
    aEsercizio CONGUAGLIO.esercizio%TYPE,
    aPgConguaglio CONGUAGLIO.pg_conguaglio%TYPE,
    eseguiLock CHAR
   ) RETURN CONGUAGLIO%ROWTYPE IS
   aRecConguaglio CONGUAGLIO%ROWTYPE;

BEGIN

    IF eseguiLock = 'Y' THEN

       SELECT * INTO aRecConguaglio
       FROM   CONGUAGLIO
       WHERE  cd_cds = aCdCds AND
              cd_unita_organizzativa = aCdUnitaOrganizzativa AND
              esercizio = aEsercizio AND
              pg_conguaglio = aPgConguaglio
       FOR UPDATE NOWAIT;

   ELSE

       SELECT * INTO aRecConguaglio
       FROM   CONGUAGLIO
       WHERE  cd_cds = aCdCds AND
              cd_unita_organizzativa = aCdUnitaOrganizzativa AND
              esercizio = aEsercizio AND
              pg_conguaglio = aPgConguaglio;

   END IF;

   RETURN aRecConguaglio;

EXCEPTION

   WHEN no_data_found THEN
        IBMERR001.RAISE_ERR_GENERICO
                  ('Conguaglio U.O. ' || aCdUnitaOrganizzativa ||
                   ' numero ' || aEsercizio || '/' || aPgConguaglio ||
                   ' non trovato');

END getConguaglio;

--==================================================================================================
-- Inserimento in ASS_COMPENSO_CONGUAGLIO
--==================================================================================================
PROCEDURE insAssCompensoConguaglio
   (
    aRecAssCompensoConguaglio ASS_COMPENSO_CONGUAGLIO%ROWTYPE
   ) IS

BEGIN

   INSERT INTO ASS_COMPENSO_CONGUAGLIO
          (CD_CDS_CONGUAGLIO,
           CD_UO_CONGUAGLIO,
           ESERCIZIO_CONGUAGLIO,
           PG_CONGUAGLIO,
           CD_CDS_COMPENSO,
           CD_UO_COMPENSO,
           ESERCIZIO_COMPENSO,
           PG_COMPENSO,
           DACR,
           UTCR,
           DUVA,
           UTUV,
           PG_VER_REC)
   VALUES (aRecAssCompensoConguaglio.cd_cds_conguaglio,
           aRecAssCompensoConguaglio.cd_uo_conguaglio,
           aRecAssCompensoConguaglio.esercizio_conguaglio,
           aRecAssCompensoConguaglio.pg_conguaglio,
           aRecAssCompensoConguaglio.cd_cds_compenso,
           aRecAssCompensoConguaglio.cd_uo_compenso,
           aRecAssCompensoConguaglio.esercizio_compenso,
           aRecAssCompensoConguaglio.pg_compenso,
           aRecAssCompensoConguaglio.dacr,
           aRecAssCompensoConguaglio.utcr,
           aRecAssCompensoConguaglio.duva,
           aRecAssCompensoConguaglio.utuv,
           aRecAssCompensoConguaglio.pg_ver_rec);

END insAssCompensoConguaglio;

--==================================================================================================
-- Costruisci intervallo date per detrazioni familiari
--==================================================================================================
PROCEDURE componiMatriceDate
   (aIntervalloDate IN OUT intervalloDateTab,
    aDataDa DATE,
    aDataA  DATE,
    aTipoData CHAR,
    flNoSchiacciaSovrapposti CHAR
   ) IS
   dtRifDa DATE;
   dtRifA DATE;
   scritto CHAR(1);
   i BINARY_INTEGER;
   k BINARY_INTEGER;

BEGIN

   -------------------------------------------------------------------------------------------------
   -- Valorizzazione variabili

   scritto:='N';

   -- Se il calcolo è per date di detrazioni familiari normalizzo portando da e a rispettivamente a
   -- inizio e fine mese

   IF aTipoData = 'F' THEN
      dtRifDa:=IBMUTL001.getFirstDayOfMonth(aDataDa);
      dtRifA:=IBMUTL001.getLastDayOfMonth(aDataA);
   ELSE
      dtRifDa:=aDataDa;
      dtRifA:=aDataA;
   END IF;

   -------------------------------------------------------------------------------------------------
   -- Ciclo memorizzazione su matrice degli intervalli date escludendo le intersezioni

   IF aIntervalloDate.FIRST IS NULL THEN

      aIntervalloDate(1).tDataDa:=dtRifDa;
      aIntervalloDate(1).tDataA:=dtRifA;
      scritto:='Y';

   ELSE

      IF flNoSchiacciaSovrapposti = 'Y' THEN

         scritto:='N';

      ELSE

         FOR i IN aIntervalloDate.FIRST .. aIntervalloDate.LAST

         LOOP

            IF    (dtRifDa >= aIntervalloDate(i).tDataDa AND
                   dtRifDa <= aIntervalloDate(i).tDataA) THEN
                  IF dtRifA > aIntervalloDate(i).tDataA THEN
                     aIntervalloDate(i).tDataA:=dtRifA;
                     scritto:='Y';
                     EXIT;
                  ELSE
                     scritto:='Y';
                     EXIT;
                  END IF;
            ELSIF (dtRifA >= aIntervalloDate(i).tDataDa AND
                   dtRifA <= aIntervalloDate(i).tDataA) THEN
                  IF dtRifDa < aIntervalloDate(i).tDataDa THEN
                     aIntervalloDate(i).tDataDa:=dtRifDa;
                     scritto:='Y';
                     EXIT;
                  ELSE
                     scritto:='Y';
                     EXIT;
                  END IF;
            ELSIF (dtRifDa < aIntervalloDate(i).tDataDa AND
                   dtRifA > aIntervalloDate(i).tDataA) THEN
                  aIntervalloDate(i).tDataDa:=dtRifDa;
                  aIntervalloDate(i).tDataA:=dtRifA;
                  scritto:='Y';
                  EXIT;
            END IF;

         END LOOP;

      END IF;

   END IF;

   -- Inserisco un nuovo intervallo solo se non esistono intersezioni con le date precedentemente memorizzate

   IF scritto = 'N' THEN
      k:=aIntervalloDate.COUNT + 1;
      aIntervalloDate(k).tDataDa:=dtRifDa;
      aIntervalloDate(k).tDataA:=dtRifA;
   END IF;

END componiMatriceDate;


-- ==================================================================================================
-- Ritorna il numero di giorni presenti in una matrice date
-- ==================================================================================================
FUNCTION getGiorniMatriceDate
   (
    aIntervalloDate intervalloDateTab
   ) RETURN INTEGER IS
   aNumeroGG INTEGER;
   i BINARY_INTEGER;

BEGIN

   aNumeroGG:=0;

   -- Abilito il calcolo solo se la matrice date è piena

   IF aIntervalloDate.COUNT > 0 THEN

      FOR i IN aIntervalloDate.FIRST .. aIntervalloDate.LAST

      LOOP

         aNumeroGG:=aNumeroGG + IBMUTL001.getDaysBetween(aIntervalloDate(i).tDataDa,
                                                         aIntervalloDate(i).tDataA);

     END LOOP;

   END IF;
   If (aNumeroGG>365) Then
     aNumeroGG:=365;
   End If;
   RETURN aNumeroGG;

END getGiorniMatriceDate;

-- ==================================================================================================
-- Ritorna il numero di Mesi presenti in una matrice date
-- ==================================================================================================
FUNCTION getMesiMatriceDate
   (
    aIntervalloDate intervalloDateTab
   ) RETURN INTEGER IS
   aNumeroMM INTEGER;
   i BINARY_INTEGER;

BEGIN

   aNumeroMM:=0;

   -- Abilito il calcolo solo se la matrice date è piena

   IF aIntervalloDate.COUNT > 0 THEN

      FOR i IN aIntervalloDate.FIRST .. aIntervalloDate.LAST

      LOOP

         aNumeroMM:=aNumeroMM + MONTHS_BETWEEN(aIntervalloDate(i).tDataA,aIntervalloDate(i).tDataDa);

     END LOOP;

   END IF;

   RETURN aNumeroMM;

END getMesiMatriceDate;

-- ==================================================================================================
-- Ritorna la minima data presente in una matrice date
-- ==================================================================================================
FUNCTION getMinimaMatriceDate
   (
    aIntervalloDate intervalloDateTab
   ) RETURN date IS
   dataMinima date := null;
   i BINARY_INTEGER;

BEGIN

   -- Abilito il calcolo solo se la matrice date è piena

   IF aIntervalloDate.COUNT > 0 THEN

      FOR i IN aIntervalloDate.FIRST .. aIntervalloDate.LAST

      LOOP

         if dataMinima is null then
            dataMinima := aIntervalloDate(i).tDataDa;
         else
            if dataMinima > aIntervalloDate(i).tDataDa then
                dataMinima := aIntervalloDate(i).tDataDa;
            end if;
         end if;

     END LOOP;

   END IF;

   RETURN dataMinima;

END getMinimaMatriceDate;
-- ==================================================================================================
-- Ritorna la massima data presente in una matrice date
-- ==================================================================================================
FUNCTION getMassimaMatriceDate
   (
    aIntervalloDate intervalloDateTab
   ) RETURN date IS
   dataMassima date := null;
   i BINARY_INTEGER;

BEGIN

   -- Abilito il calcolo solo se la matrice date è piena

   IF aIntervalloDate.COUNT > 0 THEN

      FOR i IN aIntervalloDate.FIRST .. aIntervalloDate.LAST

      LOOP

         if dataMassima is null then
            dataMassima := aIntervalloDate(i).tDataA;
         else
            if dataMassima < aIntervalloDate(i).tDataA then
                dataMassima := aIntervalloDate(i).tDataA;
            end if;
         end if;

     END LOOP;

   END IF;

   RETURN dataMassima;

END getMassimaMatriceDate;
-- ==================================================================================================
-- Ritorna il numero di Mesi presenti in una matrice date in un dato Esercizio calcolandoli
-- e schiacciando i duplicati
-- ==================================================================================================
FUNCTION getMesiMatriceDateEsForDays
   (
    aIntervalloDate intervalloDateTab,
    aEsercizio INTEGER
   ) RETURN INTEGER IS
   aNumeroMM INTEGER;
   i BINARY_INTEGER;
   j BINARY_INTEGER;
   y BINARY_INTEGER := 0;
   k BINARY_INTEGER := 0;
   tabella_mesi intervalloMesiTab;
   tabella_mesiFin intervalloMesiTab;
   Esci Exception;
   DaData DATE;
   AData  DATE;
Begin
   aNumeroMM:=0;
   tabella_mesi.delete;
   tabella_mesiFin.delete;
   -- Abilito il calcolo solo se la matrice date è piena
   If aIntervalloDate.COUNT > 0 THEN
     For i IN aIntervalloDate.FIRST .. aIntervalloDate.LAST Loop
       If (IBMUTL001.getYearOfDate(aIntervalloDate(i).tDataA) = aEsercizio Or
           IBMUTL001.getYearOfDate(aIntervalloDate(i).tDataDa) = aEsercizio) Then
         If IBMUTL001.getYearOfDate(aIntervalloDate(i).tDataDa) = aEsercizio Then
           k := k + 1;
           tabella_mesi(k).tMesi:=IBMUTL001.getMonthOfDate(aIntervalloDate(i).tDataDa);
         End If;
         If IBMUTL001.getYearOfDate(aIntervalloDate(i).tDataA) = aEsercizio Then
           k := k + 1;
           tabella_mesi(k).tMesi:=IBMUTL001.getMonthOfDate(aIntervalloDate(i).tDataA);
         End If;
         DaData := aIntervalloDate(i).tDataDa;
         AData := aIntervalloDate(i).tDataA;
         While Ceil(MONTHS_BETWEEN(AData,DaData)) > 1 And
           (IBMUTL001.getYearOfDate(DaData) = aEsercizio Or
            IBMUTL001.getYearOfDate(AData) = aEsercizio) Loop
           DaData := IBMUTL001.getAddMonth(DaData,1);
           If IBMUTL001.getYearOfDate(DaData) = aEsercizio Then
             k := k + 1;
             tabella_mesi(k).tMesi:=IBMUTL001.getMonthOfDate(DaData);
           End If;
         End Loop;
       End If;
     End Loop;
     i := 0;
     j := 0;
     --Schiaccio i duplicati
     If tabella_mesi.COUNT > 0 Then
       For i IN tabella_mesi.FIRST .. tabella_mesi.LAST Loop
         If tabella_mesiFin.COUNT > 0 Then
          Begin
           For j IN tabella_mesiFin.FIRST .. tabella_mesiFin.LAST Loop
             If tabella_mesiFin(j).tMesi = tabella_mesi(i).tMesi Then
               Raise Esci;
             End If;
           End Loop;
           y := y + 1;
           tabella_mesiFin(y).tMesi:= tabella_mesi(i).tMesi;
          Exception
            When Esci Then
              Null;
          End;
         Else
           y := y + 1;
           tabella_mesiFin(y).tMesi:= tabella_mesi(i).tMesi;
         End If;
       End Loop;
     End If;
   End If;
   aNumeroMM := tabella_mesiFin.COUNT;
   --pipe.send_message('aNumeroMM '||aNumeroMM);
   Return aNumeroMM;
End getMesiMatriceDateEsForDays;
-- ==================================================================================================
-- Ritorna il numero di Mesi presenti in una matrice date in un dato Esercizio
-- ==================================================================================================
FUNCTION getMesiMatriceDateEsercizio
   (
    aIntervalloDate intervalloDateTab,
    aEsercizio INTEGER
   ) RETURN INTEGER IS
   aNumeroMM INTEGER;
   i BINARY_INTEGER;

BEGIN

   aNumeroMM:=0;

   -- Abilito il calcolo solo se la matrice date è piena

   IF aIntervalloDate.COUNT > 0 THEN

      FOR i IN aIntervalloDate.FIRST .. aIntervalloDate.LAST
      Loop
         If (IBMUTL001.getYearOfDate(aIntervalloDate(i).tDataA) = aEsercizio Or
             IBMUTL001.getYearOfDate(aIntervalloDate(i).tDataDa) = aEsercizio) Then
           aNumeroMM:=aNumeroMM + MONTHS_BETWEEN(aIntervalloDate(i).tDataA,aIntervalloDate(i).tDataDa);
         End If;

     END LOOP;

   END IF;
   RETURN aNumeroMM;

END getMesiMatriceDateEsercizio;


--==================================================================================================
-- Ritorna l'indicazione di un tipo contributo ritenuta classificato come irpef a scaglioni
--==================================================================================================
FUNCTION getIsIrpefScaglioni
   (aCdClassifCori TIPO_CONTRIBUTO_RITENUTA.cd_classificazione_cori%TYPE,
    aPgClassifMontanti TIPO_CONTRIBUTO_RITENUTA.pg_classificazione_montanti%TYPE,
    aFlScriviMontanti TIPO_CONTRIBUTO_RITENUTA.fl_scrivi_montanti%TYPE
   ) RETURN CHAR IS
   isIrpefScaglioni CHAR(1);

BEGIN

   IF (aCdClassifCori = isCoriFiscale AND
       aPgClassifMontanti = 1 AND
       aFlScriviMontanti = 'Y') THEN
      isIrpefScaglioni:='Y';
   ELSE
      isIrpefScaglioni:='N';
   END IF;

   RETURN isIrpefScaglioni;

END getIsIrpefScaglioni;

-- =================================================================================================
-- Ritorna l'indicazione di un tipo contributo ritenuta classificato come addizionale territorio
-- =================================================================================================
FUNCTION getIsAddTerritorio
   (aCdClassifCori TIPO_CONTRIBUTO_RITENUTA.cd_classificazione_cori%TYPE
   ) RETURN CHAR IS
   isAddTerritorio CHAR(1);

BEGIN

   IF (aCdClassifCori = isCoriAddReg OR
       aCdClassifCori = isCoriAddPro OR
       aCdClassifCori = isCoriAddCom) THEN
      isAddTerritorio:='Y';
   ELSE
      isAddTerritorio:='N';
   END IF;

   RETURN isAddTerritorio;

END getIsAddTerritorio;

-- =================================================================================================
-- Ritorna l'indicazione di un tipo contributo ritenuta classificato come addizionale territorio
-- per recupero rate
-- =================================================================================================
FUNCTION getIsAddTerritorioRecRate
   (aCdClassifCori TIPO_CONTRIBUTO_RITENUTA.cd_classificazione_cori%TYPE
   ) RETURN CHAR IS
   isAddTerritorioRecRate CHAR(1);

BEGIN

   IF (aCdClassifCori = isCoriAddRegRecRate OR
       aCdClassifCori = isCoriAddProRecRate OR
       aCdClassifCori = isCoriAddComRecRate) THEN
      isAddTerritorioRecRate:='Y';
   ELSE
      isAddTerritorioRecRate:='N';
   END IF;

   RETURN isAddTerritorioRecRate;

END getIsAddTerritorioRecRate;

-- =================================================================================================
-- Ritorna l'indicazione di un tipo contributo ritenuta classificato come addizionale comunale acconto
-- =================================================================================================
FUNCTION getIsAddTerritorioAcconto
   (aCdClassifCori TIPO_CONTRIBUTO_RITENUTA.cd_classificazione_cori%TYPE
   ) RETURN CHAR IS
   isAddTerritorioAcconto CHAR(1);

BEGIN

   IF aCdClassifCori = isCoriAddComAcconto THEN
      isAddTerritorioAcconto:='Y';
   ELSE
      isAddTerritorioAcconto:='N';
   END IF;

   RETURN isAddTerritorioAcconto;

END getIsAddTerritorioAcconto;

-- =================================================================================================
-- Ritorna l'indicazione di un tipo contributo ritenuta classificato come previdenziale
-- =================================================================================================
FUNCTION getIsCoriPrevid
   (aCdClassifCori TIPO_CONTRIBUTO_RITENUTA.cd_classificazione_cori%TYPE
   ) RETURN CHAR IS
   isPrevidenziale CHAR(1);

BEGIN

   IF aCdClassifCori = isCoriPrevid THEN
      isPrevidenziale:='Y';
   ELSE
      isPrevidenziale:='N';
   END IF;

   RETURN isPrevidenziale;

END getIsCoriPrevid;

-- =================================================================================================
-- Ritorna l'indicazione di un tipo contributo ritenuta classificato come assicurativo
-- =================================================================================================
FUNCTION getIsCoriInail
   (aCdClassifCori TIPO_CONTRIBUTO_RITENUTA.cd_classificazione_cori%TYPE
   ) RETURN CHAR IS
   isInail CHAR(1);

BEGIN

   IF aCdClassifCori = isCoriInail THEN
      isInail:='Y';
   ELSE
      isInail:='N';
   END IF;

   RETURN isInail;

END getIsCoriInail;

-- =================================================================================================
-- Ritorna l'indicazione di un tipo contributo ritenuta classificato come Credito Irpef
-- =================================================================================================
FUNCTION IsCoriCreditoIrpef
   (aCdCori TIPO_CONTRIBUTO_RITENUTA.cd_contributo_ritenuta%TYPE
   ) RETURN CHAR IS
   isCoriCreditoIrpef CHAR(1);
   dataOdierna DATE;

BEGIN
   isCoriCreditoIrpef := 'N';
   dataOdierna:=SYSDATE;

   Begin
        Select fl_credito_irpef
        into isCoriCreditoIrpef
        from tipo_contributo_ritenuta
        where cd_contributo_ritenuta = aCdCori
          and Trunc(dt_ini_validita) <= Trunc(dataOdierna)
           And Trunc(dt_fin_validita) >= Trunc(dataOdierna);
   Exception
       when others then
       RETURN isCoriCreditoIrpef;
   End;

   RETURN isCoriCreditoIrpef;

END IsCoriCreditoIrpef;

--==================================================================================================
-- Ritorna un record della tabella IVA
--==================================================================================================
FUNCTION getVoceIva
   (
    aCdVoceIva VOCE_IVA.cd_voce_iva%TYPE
   ) RETURN VOCE_IVA%ROWTYPE IS
   aRecVoceIva VOCE_IVA%ROWTYPE;

BEGIN

   SELECT * INTO aRecVoceIva
   FROM   VOCE_IVA
   WHERE  cd_voce_iva = aCdVoceIva;

   RETURN aRecVoceIva;

EXCEPTION

   WHEN no_data_found THEN
        IBMERR001.RAISE_ERR_GENERICO
                  ('Codice iva ' || aCdVoceIva || ' non trovato');

END getVoceIva;

--==================================================================================================
-- Ritorna un record della tabella TIPOLOGIA_RISCHIO
--==================================================================================================
FUNCTION getTipologiaRischio
   (
    aCdTipologiaRischio TIPOLOGIA_RISCHIO.cd_tipologia_rischio%TYPE,
    aDataRif DATE
   ) RETURN TIPOLOGIA_RISCHIO%ROWTYPE IS
   aRecTipologiaRischio TIPOLOGIA_RISCHIO%ROWTYPE;

BEGIN

   SELECT * INTO aRecTipologiaRischio
   FROM   TIPOLOGIA_RISCHIO
   WHERE  cd_tipologia_rischio = aCdTipologiaRischio AND
          dt_inizio_validita <= aDataRif AND
          dt_fine_validita >= aDataRif;

   RETURN aRecTipologiaRischio;

EXCEPTION

   WHEN no_data_found THEN
        IBMERR001.RAISE_ERR_GENERICO
                  ('Codice tipologia rischio ' || aCdTipologiaRischio || ' non trovato');

END getTipologiaRischio;

--==================================================================================================
-- Ritorna la tipologia di origine di un compenso
--==================================================================================================
FUNCTION getTipoOrigineCompenso
   (
    aCdCds COMPENSO.cd_cds%TYPE,
    aCdUo COMPENSO.cd_unita_organizzativa%TYPE,
    aEsercizio COMPENSO.esercizio%TYPE,
    aPgCompenso COMPENSO.pg_compenso%TYPE
   ) RETURN INTEGER IS
   k BINARY_INTEGER;
   aContatore INTEGER;
   valoreRitorno INTEGER;

BEGIN

   FOR k IN 1 .. 5

   LOOP

      -- Compenso generato da CONGUAGLIO

      IF    k = 1 THEN

            valoreRitorno:=isCompensoConguaglio;

            SELECT COUNT(*) INTO aContatore
            FROM   DUAL
            WHERE  EXISTS
                   (SELECT 1
                    FROM   COMPENSO
                    WHERE  cd_cds = aCdCds AND
                           cd_unita_organizzativa = aCdUo AND
                           esercizio = aEsercizio AND
                           pg_compenso = aPgCompenso AND
                           fl_compenso_conguaglio = 'Y');

      -- Compenso associato a MISSIONE

      ELSIF k = 2 THEN

            valoreRitorno:=isCompensoMissione;

            SELECT COUNT(*) INTO aContatore
            FROM   DUAL
            WHERE  EXISTS
                   (SELECT 1
                    FROM   COMPENSO
                    WHERE  cd_cds = aCdCds AND
                           cd_unita_organizzativa = aCdUo AND
                           esercizio = aEsercizio AND
                           pg_compenso = aPgCompenso AND
                           cd_cds_missione IS NOT NULL);

      -- Compenso associato a MINICARRIERA

      ELSIF k = 3 THEN

            valoreRitorno:=isCompensoMinicarriera;

            SELECT COUNT(*) INTO aContatore
            FROM   DUAL
            WHERE  EXISTS
                   (SELECT 1
                    FROM   COMPENSO
                    WHERE  cd_cds = aCdCds AND
                           cd_unita_organizzativa = aCdUo AND
                           esercizio = aEsercizio AND
                           pg_compenso = aPgCompenso AND
                           fl_compenso_minicarriera = 'Y');

      -- Compenso senza calcoli

      ELSIF k = 4 THEN

            valoreRitorno:=isCompensoSenzaCalcoli;

            SELECT COUNT(*) INTO aContatore
            FROM   DUAL
            WHERE  EXISTS
                   (SELECT 1
                    FROM   COMPENSO
                    WHERE  cd_cds = aCdCds AND
                           cd_unita_organizzativa = aCdUo AND
                           esercizio = aEsercizio AND
                           pg_compenso = aPgCompenso AND
                           fl_senza_calcoli = 'Y');

      -- Compenso normale

      ELSIF k = 5 THEN

            aContatore:=1;
            valoreRitorno:=isCompensoNormale;

      END IF;

      IF aContatore = 1 THEN
         EXIT;
      END IF;

   END LOOP;

   -- Verifico se il compenso risulta essere anche associato ad un conguaglio. Tale gestione opera solo se
   -- il compenso non è da conguaglio

   IF valoreRitorno != isCompensoConguaglio THEN

      SELECT COUNT(*) INTO aContatore
      FROM   DUAL
      WHERE  EXISTS
             (SELECT 1
              FROM   ASS_COMPENSO_CONGUAGLIO
              WHERE  cd_cds_compenso = aCdCds AND
                     cd_uo_compenso = aCdUo AND
                     esercizio_compenso = aEsercizio AND
                     pg_compenso = aPgCompenso);

      IF aContatore > 0 THEN
         valoreRitorno:=(valoreRitorno * 10) + 1;
      END IF;

   END IF;

   RETURN valoreRitorno;

END getTipoOrigineCompenso;

--==================================================================================================
-- Ritorna la clessificazione di un contributo ritenuta da una occorrenza di CONTRIBUTO_RITENUTA
--==================================================================================================
FUNCTION getTipoCoriDaRigaCompenso
   (
    aRecContributoRitenuta CONTRIBUTO_RITENUTA%ROWTYPE
   ) RETURN VARCHAR2 IS
   aRecTipoCori TIPO_CONTRIBUTO_RITENUTA%ROWTYPE;

BEGIN

   SELECT * INTO aRecTipoCori
   FROM   TIPO_CONTRIBUTO_RITENUTA A
   WHERE  A.cd_contributo_ritenuta = aRecContributoRitenuta.cd_contributo_ritenuta AND
          A.dt_ini_validita = aRecContributoRitenuta.dt_ini_validita;

   RETURN aRecTipoCori.cd_classificazione_cori;

END getTipoCoriDaRigaCompenso;

--==================================================================================================
-- Compone la generazione della fattura per un compenso sia in inserimento che in modifica
--==================================================================================================
PROCEDURE generaFatturaPassiva
   (
    aRecCompenso COMPENSO%ROWTYPE,
    aRecCompensoOri COMPENSO%ROWTYPE,
    aRecAnagrafico ANAGRAFICO%ROWTYPE
   ) IS
   eseguiLock CHAR(1);
   isCancella CHAR(1);
   isInserimento CHAR(1);
   dataOdierna DATE;
   aCdBeneServizio FATTURA_PASSIVA_RIGA.cd_bene_servizio%TYPE;
   aRecFatturaPassiva FATTURA_PASSIVA%ROWTYPE;
   aRecFatturaPassivaRiga FATTURA_PASSIVA_RIGA%ROWTYPE;
   aRecContributoRitenuta CONTRIBUTO_RITENUTA%ROWTYPE;
   max_dt_registrazione date:=null;
   aListRigheFatt CNRCTB100.fattPassRigaList;

BEGIN

   -------------------------------------------------------------------------------------------------
   -- Variabili generali

   eseguiLock:='Y';
   isCancella:='N';
   isInserimento:='N';
   dataOdierna:=sysdate;

   -------------------------------------------------------------------------------------------------
   -- Non deve essere fatto nulla. Si tratta di inserimento e non è stata chiesta la generazione della fattura.

   IF (aRecCompensoOri.pg_compenso IS NULL AND
       aRecCompenso.fl_generata_fattura = 'N') THEN
      RETURN;
   END IF;

   -------------------------------------------------------------------------------------------------
   -- Controllo se devo solo inserire o anche aggiornare una precedente fattura

   -- Lettura della testata della fattura passiva associata al compenso origine in caso di modifica

   IF (aRecCompensoOri.pg_compenso IS NOT NULL AND
       aRecCompensoOri.fl_generata_fattura  = 'Y') THEN
      isCancella:='Y';
      Begin
          aRecFatturaPassiva:=CNRCTB120.getFatturaRiferimento(aRecCompensoOri.esercizio,
                                                              aRecCompensoOri.cd_cds,
                                                              aRecCompensoOri.cd_unita_organizzativa,
                                                              aRecCompensoOri.pg_compenso,
                                                              eseguiLock);
      exception when no_data_found then
      --  gestione pregressa all'inserimento degli estremi del compenso sulla fattura
        aRecFatturaPassiva:=CNRCTB120.getTstFatturaDaRifTerzo(aRecCompensoOri.cd_terzo,
                                                            aRecCompensoOri.esercizio_fattura_fornitore,
                                                            CNRCTB100.TI_FATT_FATTURA,
                                                            aRecCompensoOri.nr_fattura_fornitore,
                                                            eseguiLock);
      end;
   END IF;

   -- Lettura del cori IVA del compenso in elaborazione per decidere se fare o meno l'inserimento.

   IF (aRecCompensoOri.pg_compenso IS NOT NULL AND
       aRecCompensoOri.fl_generata_fattura  = 'Y' And
       aRecCompensoOri.pg_compenso = aRecCompenso.pg_compenso) THEN
      isInserimento:='N';
   Else
      IF aRecCompenso.fl_generata_fattura = 'Y' Then
         isInserimento:='Y';
         aRecContributoRitenuta:=getCoriIva(aRecCompenso.cd_cds,
                                            aRecCompenso.cd_unita_organizzativa,
                                            aRecCompenso.esercizio,
                                            aRecCompenso.pg_compenso);
      END IF;
   END IF;

   -- Se devo sia inserire che modificare verifico che qualcosa sia cambiato. Se tutto uguale non
   -- faccio nulla
   IF (isCancella='Y' AND
       isInserimento='Y') THEN
      IF (aRecFatturaPassiva.cd_terzo = aRecCompenso.cd_terzo AND
          aRecFatturaPassiva.esercizio_fattura_fornitore = aRecCompenso.esercizio_fattura_fornitore AND
          aRecFatturaPassiva.nr_fattura_fornitore  = aRecCompenso.nr_fattura_fornitore  AND
          aRecFatturaPassiva.dt_fattura_fornitore  = aRecCompenso.dt_fattura_fornitore  AND
          aRecFatturaPassiva.ti_istituz_commerc    = aRecCompenso.ti_istituz_commerc    AND
          aRecFatturaPassiva.cd_terzo              = aRecCompenso.cd_terzo              AND
          aRecFatturaPassiva.dt_da_competenza_coge = aRecCompenso.dt_da_competenza_coge AND
          aRecFatturaPassiva.dt_a_competenza_coge  = aRecCompenso.dt_a_competenza_coge  AND
          aRecFatturaPassiva.im_totale_imponibile  = aRecContributoRitenuta.imponibile  AND
          aRecFatturaPassiva.im_totale_iva = aRecContributoRitenuta.ammontare           And
          aRecFatturaPassiva.FL_LIQUIDAZIONE_DIFFERITA = aRecCompenso.FL_LIQUIDAZIONE_DIFFERITA) THEN
         RETURN;
      END IF;
   END IF;

   -- Non devo fare nulla

   IF (isCancella='N' AND
       isInserimento='N') THEN
      RETURN;
   END IF;

   -------------------------------------------------------------------------------------------------
   -- Test per cancellazione della fattura

   IF isCancella = 'Y' THEN

      -- Se devo cancellare la fattura verifico che non siano stati stampati i registri IVA

      IF aRecFatturaPassiva.protocollo_iva IS NOT NULL THEN
         IBMERR001.RAISE_ERR_GENERICO
            ('Impossibile eliminare una fattura  già stampata in modo definitivo sui registri IVA' ||
             'Fattura UO ' || aRecFatturaPassiva.cd_unita_organizzativa || ' numero ' ||
             aRecFatturaPassiva.esercizio || '/' || aRecFatturaPassiva.pg_fattura_passiva);
      END IF;
      IF aRecFatturaPassiva.progr_univoco IS NOT NULL THEN
         IBMERR001.RAISE_ERR_GENERICO
            ('Impossibile eliminare una fattura già inserita nel registro unico delle fatture - ' ||
             'Fattura UO ' || aRecFatturaPassiva.cd_unita_organizzativa || ' numero ' ||
             aRecFatturaPassiva.esercizio || '/' || aRecFatturaPassiva.pg_fattura_passiva);
      END IF;

      -- Cancellazione della fattura

      CNRCTB120.eliminaFatturaPassiva(aRecFatturaPassiva.cd_cds,
                                      aRecFatturaPassiva.cd_unita_organizzativa,
                                      aRecFatturaPassiva.esercizio,
                                      aRecFatturaPassiva.pg_fattura_passiva);

   END IF;

   -------------------------------------------------------------------------------------------------
   -- Inserimento della fattura

   IF isInserimento = 'Y' THEN

      aCdBeneServizio:=CNRCTB015.getVal01PerChiave('BENE_SERVIZIO_SPECIALE','FATTURA_DA_COMPENSO');

      -- Testata

      aRecFatturaPassiva:=NULL;
      aRecFatturaPassiva.cd_cds:=aRecCompenso.cd_cds;
      aRecFatturaPassiva.cd_unita_organizzativa:=aRecCompenso.cd_unita_organizzativa;
      aRecFatturaPassiva.esercizio:=aRecCompenso.esercizio;
      aRecFatturaPassiva.pg_fattura_passiva:=0;
      aRecFatturaPassiva.cd_cds_origine:=aRecCompenso.cd_cds;
      aRecFatturaPassiva.cd_uo_origine:=aRecCompenso.cd_unita_organizzativa;
      aRecFatturaPassiva.cd_tipo_sezionale:=CNRCTB120.getSezionaleOrdinarioAcq(aRecCompenso.cd_cds,
                                                                               aRecCompenso.cd_unita_organizzativa,
                                                                               aRecCompenso.ti_istituz_commerc,
                                                                               aRecCompenso.esercizio);
      aRecFatturaPassiva.ti_fattura:=CNRCTB100.TI_FATT_FATTURA;

--      aRecFatturaPassiva.dt_registrazione:=aRecCompenso.dt_registrazione;
--    la fattura, se emessa con data di registrazione uguale alla data di registrazione
--    del compenso, aggiungeva un'altra fattura ad un periodo già chiuso nel registro.
--    correzione, stani 3.5.2005
      aRecFatturaPassiva.dt_registrazione := Nvl(aRecCompenso.DT_EMISSIONE_MANDATO, aRecCompenso.dt_registrazione);

      aRecFatturaPassiva.nr_fattura_fornitore:=aRecCompenso.nr_fattura_fornitore;
      aRecFatturaPassiva.dt_fattura_fornitore:=aRecCompenso.dt_fattura_fornitore;
      aRecFatturaPassiva.esercizio_fattura_fornitore:=aRecCompenso.esercizio_fattura_fornitore;
      aRecFatturaPassiva.ds_fattura_passiva:=aRecCompenso.ds_compenso||' - FATTURA DA COMPENSO ';
      aRecFatturaPassiva.ti_istituz_commerc:=aRecCompenso.ti_istituz_commerc;
      aRecFatturaPassiva.fl_intra_ue:='N';
      aRecFatturaPassiva.fl_extra_ue:='N';
      aRecFatturaPassiva.fl_san_marino_con_iva:='N';
      aRecFatturaPassiva.fl_san_marino_senza_iva:='N';
      aRecFatturaPassiva.fl_autofattura:='N';
      aRecFatturaPassiva.fl_bolla_doganale:='N';
      aRecFatturaPassiva.fl_spedizioniere:='N';
      aRecFatturaPassiva.fl_fattura_compenso:='Y';
      aRecFatturaPassiva.cd_terzo:=aRecCompenso.cd_terzo;
      aRecFatturaPassiva.ragione_sociale:=aRecAnagrafico.ragione_sociale;
      aRecFatturaPassiva.nome:=aRecAnagrafico.nome;
      aRecFatturaPassiva.cognome:=aRecAnagrafico.cognome;
      aRecFatturaPassiva.codice_fiscale:=aRecAnagrafico.codice_fiscale;
      aRecFatturaPassiva.partita_iva:=aRecAnagrafico.partita_iva;
      aRecFatturaPassiva.cd_termini_pag:=aRecCompenso.cd_termini_pag;
      aRecFatturaPassiva.pg_banca:=aRecCompenso.pg_banca;
      aRecFatturaPassiva.cd_modalita_pag:=aRecCompenso.cd_modalita_pag;
      aRecFatturaPassiva.im_totale_imponibile_divisa:=aRecContributoRitenuta.imponibile;
      aRecFatturaPassiva.im_totale_imponibile:=aRecContributoRitenuta.imponibile;
      aRecFatturaPassiva.im_totale_iva:=aRecContributoRitenuta.ammontare;
      aRecFatturaPassiva.im_totale_fattura:=aRecContributoRitenuta.imponibile + aRecContributoRitenuta.ammontare;
      aRecFatturaPassiva.cd_divisa:='EURO';
      aRecFatturaPassiva.cambio:=1;
      aRecFatturaPassiva.stato_cofi:='C';
      aRecFatturaPassiva.stato_coge:=CNRCTB100.STATO_COEP_EXC;
      aRecFatturaPassiva.dt_da_competenza_coge:=aRecCompenso.dt_da_competenza_coge;
      aRecFatturaPassiva.dt_a_competenza_coge:=aRecCompenso.dt_a_competenza_coge;
      aRecFatturaPassiva.dacr:=dataOdierna;
      aRecFatturaPassiva.utcr:=aRecCompenso.utuv;
      aRecFatturaPassiva.duva:=dataOdierna;
      aRecFatturaPassiva.utuv:=aRecCompenso.utuv;
      aRecFatturaPassiva.pg_ver_rec:=1;
      aRecFatturaPassiva.ti_associato_manrev:='N';
      aRecFatturaPassiva.stato_coan:=CNRCTB100.STATO_COEP_EXC;
      aRecFatturaPassiva.stato_pagamento_fondo_eco:='N';
      aRecFatturaPassiva.im_totale_quadratura:=aRecContributoRitenuta.imponibile + aRecContributoRitenuta.ammontare;
      aRecFatturaPassiva.ti_bene_servizio:=CNRCTB100.TI_FT_ACQ_INDIFFERENTE;
      aRecFatturaPassiva.fl_congelata:='N';
      aRecFatturaPassiva.fl_liquidazione_differita:=aRecCompenso.fl_liquidazione_differita;
      aRecFatturaPassiva.dt_scadenza:=aRecCompenso.dt_scadenza;
      aRecFatturaPassiva.data_protocollo:=aRecCompenso.data_protocollo;
      aRecFatturaPassiva.numero_protocollo:=aRecCompenso.numero_protocollo;
      aRecFatturaPassiva.esercizio_compenso:=aRecCompenso.esercizio;
      aRecFatturaPassiva.cds_compenso:=aRecCompenso.cd_cds;
      aRecFatturaPassiva.uo_compenso:=aRecCompenso.cd_unita_organizzativa;
      aRecFatturaPassiva.pg_compenso:=aRecCompenso.pg_compenso;
      -- Righe

      aListRigheFatt.DELETE;
      aRecFatturaPassivaRiga:=NULL;
      aListRigheFatt(1):=aRecFatturaPassivaRiga;
      aListRigheFatt(1).cd_cds:=aRecCompenso.cd_cds;
      aListRigheFatt(1).cd_unita_organizzativa:=aRecCompenso.cd_unita_organizzativa;
      aListRigheFatt(1).esercizio:=aRecCompenso.esercizio;
      aListRigheFatt(1).pg_fattura_passiva:=0;
      aListRigheFatt(1).progressivo_riga:=1;
      aListRigheFatt(1).ti_istituz_commerc:=aRecCompenso.ti_istituz_commerc;
      aListRigheFatt(1).cd_bene_servizio:=aCdBeneServizio;
      aListRigheFatt(1).prezzo_unitario:=aRecContributoRitenuta.imponibile;
      aListRigheFatt(1).quantita:=1;
      aListRigheFatt(1).im_totale_divisa:=aRecContributoRitenuta.imponibile;
      aListRigheFatt(1).im_imponibile:=aRecContributoRitenuta.imponibile;
      aListRigheFatt(1).cd_voce_iva:=aRecCompenso.cd_voce_iva;
      aListRigheFatt(1).fl_iva_forzata:='N';
      aListRigheFatt(1).im_iva:=aRecContributoRitenuta.ammontare;
      aListRigheFatt(1).im_diponibile_nc:=0;
      aListRigheFatt(1).stato_cofi:='C';
      aListRigheFatt(1).dt_da_competenza_coge:=aRecCompenso.dt_da_competenza_coge;
      aListRigheFatt(1).dt_a_competenza_coge:=aRecCompenso.dt_a_competenza_coge;
      aListRigheFatt(1).dacr:=dataOdierna;
      aListRigheFatt(1).utcr:=aRecCompenso.utuv;
      aListRigheFatt(1).duva:=dataOdierna;
      aListRigheFatt(1).utuv:=aRecCompenso.utuv;
      aListRigheFatt(1).pg_ver_rec:=1;
      aListRigheFatt(1).ti_associato_manrev:='N';
      aListRigheFatt(1).cd_terzo:=aRecCompenso.cd_terzo;
      aListRigheFatt(1).cd_termini_pag:=aRecCompenso.cd_termini_pag;
      aListRigheFatt(1).pg_banca:=aRecCompenso.pg_banca;
      aListRigheFatt(1).cd_modalita_pag:=aRecCompenso.cd_modalita_pag;
      If(aRecCompenso.fl_liquidazione_differita='N') Then
        aListRigheFatt(1).data_esigibilita_iva:=aRecCompenso.dt_registrazione;
      End If;
-- 02.02.2007 Stani - Inserito check data registro IVA

cnrctb100.chkDtRegistrazPerIva (aRecFatturaPassiva.cd_cds_origine, aRecFatturaPassiva.cd_uo_origine, aRecFatturaPassiva.esercizio,
                                aRecFatturaPassiva.cd_tipo_sezionale, aRecFatturaPassiva.dt_registrazione);
    begin
        SELECT TRUNC(MAX(DT_REGISTRAZIONE)) into max_dt_registrazione
        from fattura_passiva
        where
        esercizio   = aRecFatturaPassiva.esercizio      and
        cd_cds      = aRecFatturaPassiva.cd_cds_origine and
        cd_unita_organizzativa= aRecFatturaPassiva.cd_uo_origine;
    exception when no_data_found then
      max_dt_registrazione:=null;
     end;
     if max_dt_registrazione is not null  and max_dt_registrazione > aRecFatturaPassiva.dt_registrazione then
      IBMERR001.RAISE_ERR_GENERICO
                ('Impossibile inserire una fattura passiva con ' ||
                 'data registrazione anteriore al ' || TO_CHAR(max_dt_registrazione,'DD/MM/YYYY') || ' .');


     end if;
    CNRCTB120.creaFattura(aRecFatturaPassiva, aListRigheFatt);

   END IF;

END generaFatturaPassiva;

--==================================================================================================
-- Ritorna il record di CONTRIBUTO_RITENUTA (uno solo) che rappresenta IVA
--==================================================================================================
FUNCTION getCoriIva
   (
    aCdCds COMPENSO.cd_cds%TYPE,
    aCdUo COMPENSO.cd_unita_organizzativa%TYPE,
    aEsercizio COMPENSO.esercizio%TYPE,
    aPgCompenso COMPENSO.pg_compenso%TYPE
   ) RETURN CONTRIBUTO_RITENUTA%ROWTYPE IS
   aRecContributoRitenuta CONTRIBUTO_RITENUTA%ROWTYPE;

BEGIN

   SELECT A.* INTO aRecContributoRitenuta
   FROM   CONTRIBUTO_RITENUTA A,
          TIPO_CONTRIBUTO_RITENUTA B
   WHERE  A.cd_cds = aCdCds AND
          A.cd_unita_organizzativa = aCdUo AND
          A.esercizio = aEsercizio AND
          A.pg_compenso = aPgCompenso AND
          B.cd_contributo_ritenuta = A.cd_contributo_ritenuta AND
          B.dt_ini_validita = A.dt_ini_validita AND
          B.cd_classificazione_cori = isCoriIva;

   RETURN aRecContributoRitenuta;

EXCEPTION
      WHEN NO_DATA_FOUND then
           IBMERR001.RAISE_ERR_GENERICO
              ('Impossibile trovare un CORI di tipo IVA per il compenso in elaborazione');

END getCoriIva;

-- ==================================================================================================
-- Ritorna la configurazione base della gestione deduzione IRPEF
-- ==================================================================================================
PROCEDURE getConfigDeduzioneIrpef
   (
    aEsercizio NUMBER,
    aBaseDeduzione IN OUT NUMBER,
    aQuotaFissaDeduzione IN OUT NUMBER,
    aQuotaVariabileDeduzione IN OUT NUMBER
   ) IS
   i BINARY_INTEGER;

BEGIN

   FOR i IN 1 .. 3

   LOOP

      IF    i = 1 THEN
            aBaseDeduzione:=CNRCTB015.getIm01PerChiave(aEsercizio,
                                                       isChiavePrimaria,
                                                       isChiaveSecondariaBase);
      ELSIF i = 2 THEN
            aQuotaFissaDeduzione:=CNRCTB015.getIm01PerChiave(aEsercizio,
                                                             isChiavePrimaria,
                                                             isChiaveSecondariaQuota);
      ELSIF i = 3 THEN
            aQuotaVariabileDeduzione:=CNRCTB015.getIm02PerChiave(aEsercizio,
                                                                 isChiavePrimaria,
                                                                 isChiaveSecondariaQuota);
      END IF;

   END LOOP;

END getConfigDeduzioneIrpef;

-- ==================================================================================================
-- Ritorna la configurazione base della gestione deduzione FAMILY
-- ==================================================================================================
 PROCEDURE getConfigDeduzioneFamily
 (
  aEsercizio NUMBER,
  glbBaseDeduzioneFamily IN OUT NUMBER,
  glbDFamilyConiuge IN OUT NUMBER,
  glbDFamilyFiglio IN OUT NUMBER,
  glbDFamilyAltro IN OUT NUMBER,
  glbDFamilyFiglioMenoTre IN OUT NUMBER,
  glbDFamilyFiglioSenzaConiuge IN OUT NUMBER,
  glbDFamilyFiglioHandicap IN OUT NUMBER
 ) IS
   i BINARY_INTEGER;

BEGIN
   glbBaseDeduzioneFamily:=CNRCTB015.getIm01PerChiave(aEsercizio,
                                                      isChiavePrimariaFamily,
                                                      isCSDFamilyBase);

   glbDFamilyConiuge:=CNRCTB015.getIm01PerChiave(aEsercizio,
                                                      isChiavePrimariaFamily,
                                                      isCSDFamilyConiuge);

   glbDFamilyFiglio:=CNRCTB015.getIm01PerChiave(aEsercizio,
                                                      isChiavePrimariaFamily,
                                                      isCSDFamilyFiglio);

   glbDFamilyAltro:=CNRCTB015.getIm01PerChiave(aEsercizio,
                                                      isChiavePrimariaFamily,
                                                      isCSDFamilyAltro);

   glbDFamilyFiglioMenoTre:=CNRCTB015.getIm01PerChiave(aEsercizio,
                                                      isChiavePrimariaFamily,
                                                      isCSDFamilyFiglioMenoTre);

   glbDFamilyFiglioSenzaConiuge:=CNRCTB015.getIm01PerChiave(aEsercizio,
                                                      isChiavePrimariaFamily,
                                                      isCSDFamilyFiglioSenzaConiuge);

   glbDFamilyFiglioHandicap:=CNRCTB015.getIm01PerChiave(aEsercizio,
                                                      isChiavePrimariaFamily,
                                                      isCSDFamilyFiglioHandicap);
END getConfigDeduzioneFamily;
-- ==================================================================================================
-- Ritorna il valore minimo e massimo dello scaglione IRPEF in caso di gestione per aliquota
-- massima in anagrafica
-- ==================================================================================================
PROCEDURE getValMinMaxSclAlqMaxIrpef
   (
    aDataRegistrazione DATE,
    aRecTipoTrattamento TIPO_TRATTAMENTO%ROWTYPE,
    aAliquotaFiscaleAnag NUMBER,
    aImportoscaglioneMin IN OUT NUMBER,
    aImportoscaglioneMax IN OUT NUMBER
   ) IS

   chkEsisteScaglione INTEGER;
   aCdRegione COMPENSO.cd_regione_add%TYPE;
   aCdProvincia COMPENSO.cd_provincia_add%TYPE;
   aPgComune COMPENSO.pg_comune_add%TYPE;
   aMontanteNetto NUMBER(15,2);
   aAliquotaIrpefAnag SCAGLIONE.aliquota%TYPE;

   aRecVTrattamento V_TIPO_TRATTAMENTO_TIPO_CORI%ROWTYPE;
   aRecVPreScaglione V_PRE_SCAGLIONE%ROWTYPE;

BEGIN

   --------------------------------------------------------------------------------------------------
   -- Inizializzazione variabili

   aCdRegione:='*';
   aCdProvincia:='*';
   aPgComune:=0;
   aMontanteNetto:=0;
   aAliquotaIrpefAnag:=aAliquotaFiscaleAnag;

   --------------------------------------------------------------------------------------------------
   -- Recupero del primo tipo contributo ritenuta di tipo irpef a scaglioni presente nel trattamento

   BEGIN

      SELECT * INTO aRecVTrattamento
      FROM   V_TIPO_TRATTAMENTO_TIPO_CORI
      WHERE  cd_trattamento = aRecTipoTrattamento.cd_trattamento AND
             dt_ini_val_trattamento = aRecTipoTrattamento.dt_ini_validita AND
             dt_fin_val_trattamento = aRecTipoTrattamento.dt_fin_validita AND
             dt_ini_val_tratt_cori <= aDataRegistrazione AND
             dt_fin_val_tratt_cori >= aDataRegistrazione AND
             dt_ini_val_tipo_cori <= aDataRegistrazione AND
             dt_fin_val_tipo_cori >= aDataRegistrazione AND
             cd_classificazione_cori = isCoriFiscale AND
             pg_classificazione_montanti = 1 AND
             fl_scrivi_montanti = 'Y';

   EXCEPTION

      WHEN no_data_found THEN
           aImportoscaglioneMin:=0;
           aImportoscaglioneMax:=0;
           RETURN;
   END;

   --------------------------------------------------------------------------------------------------
   -- Recupero dello scaglione di riferimento

   aRecVPreScaglione:=getScaglione(aRecVTrattamento.cd_cori,
                                   aRecVTrattamento.ti_anagrafico,
                                   aDataRegistrazione,
                                   aMontanteNetto,
                                   aAliquotaIrpefAnag,
                                   aCdRegione,
                                   aCdProvincia,
                                   aPgComune);

   aImportoscaglioneMin:=aRecVPreScaglione.im_inferiore;
   aImportoscaglioneMax:=aRecVPreScaglione.im_superiore;

END getValMinMaxSclAlqMaxIrpef;

-- ==================================================================================================
-- Ritorna il record di RATEIZZA_CLASSIFIC_CORI presente nell'esercizio per l'anagrafico in calcolo di
-- compensi o conguagli
-- ==================================================================================================
FUNCTION getRateizzaClassificCori
   (
    aEsercizio NUMBER,
    aCdAnag NUMBER,
    isTemporaneo CHAR,
    aCdClassificazioneCori CHAR,
    eseguiLock CHAR
   ) RETURN RATEIZZA_CLASSIFIC_CORI%ROWTYPE IS

   aRecRateizzaClassificCori RATEIZZA_CLASSIFIC_CORI%ROWTYPE;

BEGIN

    aRecRateizzaClassificCori:=NULL;

    IF eseguiLock = 'Y' THEN

       SELECT * INTO aRecRateizzaClassificCori
       FROM   RATEIZZA_CLASSIFIC_CORI
       WHERE  esercizio = aEsercizio AND
              cd_anag = aCdAnag AND
              cd_classificazione_cori = aCdClassificazioneCori AND
              fl_temporaneo = isTemporaneo
       FOR UPDATE NOWAIT;

    ELSE

       SELECT * INTO aRecRateizzaClassificCori
       FROM   RATEIZZA_CLASSIFIC_CORI
       WHERE  esercizio = aEsercizio AND
              cd_anag = aCdAnag AND
              cd_classificazione_cori = aCdClassificazioneCori AND
              fl_temporaneo = isTemporaneo;

   END IF;

   RETURN aRecRateizzaClassificCori;

EXCEPTION

   WHEN no_data_found THEN
        aRecRateizzaClassificCori.esercizio:=aEsercizio;
        aRecRateizzaClassificCori.cd_anag:=aCdAnag;
        aRecRateizzaClassificCori.cd_classificazione_cori:=aCdClassificazioneCori;
        aRecRateizzaClassificCori.im_da_rateizzare:=0;
        aRecRateizzaClassificCori.im_rateizzato:=0;
        RETURN aRecRateizzaClassificCori;

END getRateizzaClassificCori;

-- ==================================================================================================
-- Ritorna il record di RATEIZZA_CLASSIFIC_CORI_S presente nell'esercizio per l'anagrafico in calcolo di
-- compensi o conguagli
-- ==================================================================================================
FUNCTION getRateizzaClassificCoriStr
   (
    aEsercizio NUMBER,
    aCdsConguaglio CONGUAGLIO.cd_cds%TYPE,
    aCdUoConguaglio CONGUAGLIO.cd_unita_organizzativa%TYPE,
    aPgConguaglio CONGUAGLIO.pg_conguaglio%TYPE,
    aCdAnag NUMBER,
    isTemporaneo CHAR,
    aCdClassificazioneCori CHAR,
    eseguiLock CHAR
   ) RETURN RATEIZZA_CLASSIFIC_CORI%ROWTYPE IS

   aRecRateizzaClassificCori RATEIZZA_CLASSIFIC_CORI%ROWTYPE;

BEGIN

    aRecRateizzaClassificCori:=NULL;

    IF eseguiLock = 'Y' THEN

       SELECT * INTO aRecRateizzaClassificCori
       FROM   RATEIZZA_CLASSIFIC_CORI_S
       WHERE  esercizio = aEsercizio AND
              cd_anag = aCdAnag AND
              cd_classificazione_cori = aCdClassificazioneCori AND
              fl_temporaneo = isTemporaneo AND
              cd_cds_conguaglio = aCdsConguaglio AND
              cd_uo_conguaglio = aCdUoConguaglio AND
              pg_conguaglio = aPgConguaglio
       FOR UPDATE NOWAIT;

    ELSE

       SELECT * INTO aRecRateizzaClassificCori
       FROM   RATEIZZA_CLASSIFIC_CORI_S
       WHERE  esercizio = aEsercizio AND
              cd_anag = aCdAnag AND
              cd_classificazione_cori = aCdClassificazioneCori AND
              fl_temporaneo = isTemporaneo AND
              cd_cds_conguaglio = aCdsConguaglio AND
              cd_uo_conguaglio = aCdUoConguaglio AND
              pg_conguaglio = aPgConguaglio;

   END IF;

   RETURN aRecRateizzaClassificCori;

EXCEPTION

   WHEN no_data_found THEN
        aRecRateizzaClassificCori.esercizio:=aEsercizio;
        aRecRateizzaClassificCori.cd_anag:=aCdAnag;
        aRecRateizzaClassificCori.cd_classificazione_cori:=aCdClassificazioneCori;
        aRecRateizzaClassificCori.im_da_rateizzare:=0;
        aRecRateizzaClassificCori.im_rateizzato:=0;
        RETURN aRecRateizzaClassificCori;

END getRateizzaClassificCoriStr;

-- ==================================================================================================
-- Inserimento di un record in RATEIZZA_CLASSIFIC_CORI da conguaglio (sempre temporaneo)
-- ==================================================================================================
PROCEDURE generaRateizzaClassificCori
   (aRecConguaglio CONGUAGLIO%ROWTYPE,
    aAmmontareDaRateizzare NUMBER,
    aRecRateizzaClassificCoriIn RATEIZZA_CLASSIFIC_CORI%ROWTYPE
   ) IS

   isStorico CHAR(1);
   aDescAddizionale VARCHAR2(100);
   aRecRateizzaClassificCoriOut RATEIZZA_CLASSIFIC_CORI%ROWTYPE;

BEGIN

   isStorico:='N';
   aRecRateizzaClassificCoriOut:=aRecRateizzaClassificCoriIn;
   aRecRateizzaClassificCoriOut.fl_temporaneo:='Y';
   aRecRateizzaClassificCoriOut.cd_cds_conguaglio:=aRecConguaglio.cd_cds;
   aRecRateizzaClassificCoriOut.cd_uo_conguaglio:=aRecConguaglio.cd_unita_organizzativa;
   aRecRateizzaClassificCoriOut.pg_conguaglio:=aRecConguaglio.pg_conguaglio;

   -- Determino se si tratta di primo inserimento o se vi sono stati precedenti accantonamenti

   IF aRecRateizzaClassificCoriIn.cd_cds_conguaglio IS NULL THEN

      aRecRateizzaClassificCoriOut.im_da_rateizzare:=aAmmontareDaRateizzare;
      aRecRateizzaClassificCoriOut.im_rateizzato:=0;
      aRecRateizzaClassificCoriOut.dacr:=aRecConguaglio.duva;
      aRecRateizzaClassificCoriOut.utcr:=aRecConguaglio.utuv;
      aRecRateizzaClassificCoriOut.duva:=aRecConguaglio.duva;
      aRecRateizzaClassificCoriOut.utuv:=aRecConguaglio.utuv;
      aRecRateizzaClassificCoriOut.pg_ver_rec:=1;

   ELSE

      aRecRateizzaClassificCoriOut.im_da_rateizzare:=aRecRateizzaClassificCoriOut.im_da_rateizzare + aAmmontareDaRateizzare;
      aRecRateizzaClassificCoriOut.duva:=aRecConguaglio.duva;
      aRecRateizzaClassificCoriOut.utuv:=aRecConguaglio.utuv;
      aRecRateizzaClassificCoriOut.pg_ver_rec:=aRecRateizzaClassificCoriOut.pg_ver_rec + 1;

   END IF;

   -- Attivazione controlli di ammissione all'accantonamento

   IF (aRecRateizzaClassificCoriOut.im_da_rateizzare < 0 OR
       aRecRateizzaClassificCoriOut.im_da_rateizzare < aRecRateizzaClassificCoriOut.im_rateizzato )THEN

      IF    aRecRateizzaClassificCoriOut.cd_classificazione_cori = isCoriAddReg THEN
            aDescAddizionale:='ADDIZIONALE REGIONALE';
      ELSIF aRecRateizzaClassificCoriOut.cd_classificazione_cori = isCoriAddPro THEN
            aDescAddizionale:='ADDIZIONALE PROVINCIALE';
      ELSIF aRecRateizzaClassificCoriOut.cd_classificazione_cori = isCoriAddCom THEN
            aDescAddizionale:='ADDIZIONALE COMUNALE';
      END IF;

      IF aRecRateizzaClassificCoriOut.im_da_rateizzare < 0 THEN
         IBMERR001.RAISE_ERR_GENERICO
            ('Impossibile attivare il conguaglio in modalità accantonamento con ' || aDescAddizionale || ' ' ||
             'negativa. ( ' || TO_CHAR(aRecRateizzaClassificCoriOut.im_da_rateizzare) || ')');
      ELSE
         IBMERR001.RAISE_ERR_GENERICO
            ('Conguaglio non ammissibile. L''ammontare rateizzato messo in pagamento in esercizi successivi per ' ||
             aDescAddizionale || ' risulta maggiore dell''accantonamento stesso');
      END IF;

   END IF;

   -- Inserimento del record temporaneo in RATEIZZA_CLASSIFIC_CORI

   insRateizzaClassificCori(aRecRateizzaClassificCoriOut,
                            isStorico);

END generaRateizzaClassificCori;

-- ==================================================================================================
-- Inserimento di un record in RATEIZZA_CLASSIFIC_CORI
-- ==================================================================================================
PROCEDURE insRateizzaClassificCori
   (
    aRecRateizzaClassificCori RATEIZZA_CLASSIFIC_CORI%ROWTYPE,
    isStorico CHAR
   ) IS

BEGIN

   IF isStorico = 'N' THEN

      INSERT INTO RATEIZZA_CLASSIFIC_CORI
             (ESERCIZIO,
              CD_ANAG,
              CD_CLASSIFICAZIONE_CORI,
              FL_TEMPORANEO,
              CD_CDS_CONGUAGLIO,
              CD_UO_CONGUAGLIO,
              PG_CONGUAGLIO,
              IM_DA_RATEIZZARE,
              IM_RATEIZZATO,
              DACR,
              UTCR,
              DUVA,
              UTUV,
              PG_VER_REC)
      VALUES (aRecRateizzaClassificCori.esercizio,
              aRecRateizzaClassificCori.cd_anag,
              aRecRateizzaClassificCori.cd_classificazione_cori,
              aRecRateizzaClassificCori.fl_temporaneo,
              aRecRateizzaClassificCori.cd_cds_conguaglio,
              aRecRateizzaClassificCori.cd_uo_conguaglio,
              aRecRateizzaClassificCori.pg_conguaglio,
              aRecRateizzaClassificCori.im_da_rateizzare,
              aRecRateizzaClassificCori.im_rateizzato,
              aRecRateizzaClassificCori.dacr,
              aRecRateizzaClassificCori.utcr,
              aRecRateizzaClassificCori.duva,
              aRecRateizzaClassificCori.utuv,
              aRecRateizzaClassificCori.pg_ver_rec);

   ELSE

      INSERT INTO RATEIZZA_CLASSIFIC_CORI_S
             (ESERCIZIO,
              CD_ANAG,
              CD_CLASSIFICAZIONE_CORI,
              FL_TEMPORANEO,
              CD_CDS_CONGUAGLIO,
              CD_UO_CONGUAGLIO,
              PG_CONGUAGLIO,
              IM_DA_RATEIZZARE,
              IM_RATEIZZATO,
              DACR,
              UTCR,
              DUVA,
              UTUV,
              PG_VER_REC)
      VALUES (aRecRateizzaClassificCori.esercizio,
              aRecRateizzaClassificCori.cd_anag,
              aRecRateizzaClassificCori.cd_classificazione_cori,
              aRecRateizzaClassificCori.fl_temporaneo,
              aRecRateizzaClassificCori.cd_cds_conguaglio,
              aRecRateizzaClassificCori.cd_uo_conguaglio,
              aRecRateizzaClassificCori.pg_conguaglio,
              aRecRateizzaClassificCori.im_da_rateizzare,
              aRecRateizzaClassificCori.im_rateizzato,
              aRecRateizzaClassificCori.dacr,
              aRecRateizzaClassificCori.utcr,
              aRecRateizzaClassificCori.duva,
              aRecRateizzaClassificCori.utuv,
              aRecRateizzaClassificCori.pg_ver_rec);

   END IF;

END insRateizzaClassificCori;


FUNCTION getIsRegConAliqMax
   (aCdRegione REGIONE.cd_regione%TYPE
   ) RETURN CHAR IS
   isRegConAliqMax CHAR(1);

BEGIN
  Begin
    Select FL_ADDREG_ALIQMAX
    Into isRegConAliqMax
    From regione
    Where cd_regione = aCdRegione;
  Exception
    When No_Data_Found Then
      isRegConAliqMax := 'N';
  End;

  Return isRegConAliqMax;

END getIsRegConAliqMax;

FUNCTION getIsComConAliqMax
   (aPgComune COMUNE.pg_comune%TYPE
   ) RETURN CHAR IS
   isComConAliqMax CHAR(1);

BEGIN
  Begin
    Select FL_ADDCOM_ALIQMAX
    Into isComConAliqMax
    From comune
    Where pg_comune = aPgComune;
  Exception
    When No_Data_Found Then
      isComConAliqMax := 'N';
  End;

  Return isComConAliqMax;

END getIsComConAliqMax;

-- ==================================================================================================
-- Ritorna il record di ACCONTO_CLASSIFIC_CORI presente nell'esercizio per l'anagrafico in calcolo di
-- compensi o conguagli
-- ==================================================================================================
FUNCTION getAccontoClassificCori
   (
    aEsercizio NUMBER,
    aCdAnag NUMBER,
    isTemporaneo CHAR,
    aCdClassificazioneCori CHAR,
    eseguiLock CHAR
   ) RETURN ACCONTO_CLASSIFIC_CORI%ROWTYPE IS

   aRecAccontoClassificCori ACCONTO_CLASSIFIC_CORI%ROWTYPE;

BEGIN

    aRecAccontoClassificCori:=NULL;

    IF eseguiLock = 'Y' THEN

       SELECT * INTO aRecAccontoClassificCori
       FROM   ACCONTO_CLASSIFIC_CORI
       WHERE  esercizio = aEsercizio AND
              cd_anag = aCdAnag AND
              cd_classificazione_cori = aCdClassificazioneCori
       FOR UPDATE NOWAIT;

    ELSE

       SELECT * INTO aRecAccontoClassificCori
       FROM   ACCONTO_CLASSIFIC_CORI
       WHERE  esercizio = aEsercizio AND
              cd_anag = aCdAnag AND
              cd_classificazione_cori = aCdClassificazioneCori;

   END IF;

   RETURN aRecAccontoClassificCori;

EXCEPTION

   WHEN no_data_found THEN
        aRecAccontoClassificCori.esercizio:=aEsercizio;
        aRecAccontoClassificCori.cd_anag:=aCdAnag;
        aRecAccontoClassificCori.cd_classificazione_cori:=aCdClassificazioneCori;
        aRecAccontoClassificCori.im_acconto_calcolato:=0;
        aRecAccontoClassificCori.im_acconto_trattenuto:=0;
        RETURN aRecAccontoClassificCori;

END getAccontoClassificCori;

-- ==================================================================================================
-- Ritorna il record di ESENZIONI_ADDCOM relativo al comune di residenza e valido alla data odierna
-- ==================================================================================================
   FUNCTION getEsenzioniAddcom
      (
       aPgComune NUMBER,
       aDataOdierna DATE
      ) RETURN ESENZIONI_ADDCOM%Rowtype Is

   aRecEsenzioniAddcom ESENZIONI_ADDCOM%ROWTYPE;

BEGIN

    aRecEsenzioniAddcom:=NULL;

    Select *
    Into aRecEsenzioniAddcom
    From   ESENZIONI_ADDCOM
    Where  pg_comune = aPgComune
      And Trunc(dt_inizio_validita) <= Trunc(aDataOdierna)
      And Trunc(dt_fine_validita) >= Trunc(aDataOdierna);

   RETURN aRecEsenzioniAddcom;

EXCEPTION

   WHEN No_Data_Found THEN
        aRecEsenzioniAddcom.pg_comune:=aPgComune;
        aRecEsenzioniAddcom.dt_inizio_validita:=aDataOdierna;
        aRecEsenzioniAddcom.dt_fine_validita:=aDataOdierna;
        aRecEsenzioniAddcom.importo:=0;
        RETURN aRecEsenzioniAddcom;
   WHEN Too_Many_Rows THEN
        aRecEsenzioniAddcom.pg_comune:=aPgComune;
        aRecEsenzioniAddcom.dt_inizio_validita:=aDataOdierna;
        aRecEsenzioniAddcom.dt_fine_validita:=aDataOdierna;
        aRecEsenzioniAddcom.importo:=0;
        RETURN aRecEsenzioniAddcom;
END getEsenzioniAddcom;
FUNCTION getIsAddComunale
   (aCdClassifCori TIPO_CONTRIBUTO_RITENUTA.cd_classificazione_cori%TYPE
   ) RETURN CHAR IS

BEGIN
   IF    aCdClassifCori = isCoriAddComAcconto THEN
      Return 'Y';
   Elsif aCdClassifCori = isCoriAddComRecRate THEN
      Return 'Y';
   Elsif aCdClassifCori = isCoriAddCom THEN
      Return 'Y';
   ELSE
      Return 'N';
   END IF;
End getIsAddComunale;
END;
