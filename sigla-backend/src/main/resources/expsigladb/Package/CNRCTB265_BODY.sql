--------------------------------------------------------
--  DDL for Package Body CNRCTB265
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "CNRCTB265" AS
-- =================================================================================================
-- Stampa RIEPILOGATIVO CENTRO con dettaglio per sezionale
-- =================================================================================================
PROCEDURE insFatturePerRiepilogoCentro
   (
    repID INTEGER,
    aSequenza IN OUT INTEGER,
    aSequenzaUo IN OUT INTEGER,
    aCdCdsOrigine VARCHAR2,
    aCdUoOrigine VARCHAR2,
    aDsUoOrigine VARCHAR2,
    aEsercizio NUMBER,
    aCdTipoSezionale VARCHAR2,
    aDsSezionale VARCHAR2,
    aTipoRegistro VARCHAR2,
    aTipoDocumentoReportStato VARCHAR2,
    aDataInizio DATE,
    aDataFine DATE,
    aTipoReport VARCHAR2,
    gruppoStm CHAR,
    sottoGruppoStm CHAR,
    descrizioneGruppoStm VARCHAR2,
    stringaErrore IN OUT VARCHAR2
   ) IS
   aTiIstituzCommerc CHAR(1);
   aDtInizioStampaRegistri DATE;
   aDtInizioLiquidazione DATE;
   trovatiRecord INTEGER;

   memCodiceIva VARCHAR2(10);
   memDescrizioneIva VARCHAR2(200);
   memCodiceGruppoIva VARCHAR2(50);
   memDescrizioneGruppoIva VARCHAR2(200);

   aRecVpStmRiepilogoCentroU VP_STM_RIEPILOGATIVO_CENTRO%ROWTYPE;
   aRecVpStmRiepilogoCentroD VP_STM_RIEPILOGATIVO_CENTRO%ROWTYPE;
   aRecVpStmRiepilogoCentroP VP_STM_RIEPILOGATIVO_CENTRO%ROWTYPE;
   aRecVpStmRiepilogoCentroT VP_STM_RIEPILOGATIVO_CENTRO%ROWTYPE;

   gen_cv GenericCurTyp;

BEGIN

   IF aTipoDocumentoReportStato = 'C' THEN
      aTiIstituzCommerc:='C';
   ELSE
      aTiIstituzCommerc:='I';
   END IF;
   trovatiRecord:=0;

   -------------------------------------------------------------------------------------------------
   -- Scrittura record di intestazione UO

   BEGIN

      -- Controllo se il sezionale risulta stampato in modo definitivo per la UO corrente

      aDtInizioStampaRegistri:=NULL;
      aDtInizioLiquidazione:=NULL;

      CNRCTB255.getIsSezionaleElaborataIva(aCdCdsOrigine,
                                           aCdUoOrigine,
                                           aCdTipoSezionale,
                                           aDataInizio,
                                           aDataFine,
                                           aTipoDocumentoReportStato,
                                           aDtInizioStampaRegistri,
                                           aDtInizioLiquidazione);

      -- Scrittura record di intestazione UO

      aSequenza:=aSequenza + 1;

      aRecVpStmRiepilogoCentroU:=NULL;
      aRecVpStmRiepilogoCentroU.id_report:=repID;
      aRecVpStmRiepilogoCentroU.gruppo:=gruppoStm;
      aRecVpStmRiepilogoCentroU.tipologia_riga:='I';
      aRecVpStmRiepilogoCentroU.sequenza:=aSequenza;
      aRecVpStmRiepilogoCentroU.sottogruppo:=sottoGruppoStm;
      aRecVpStmRiepilogoCentroU.cd_cds:=aCdCdsOrigine;
      aRecVpStmRiepilogoCentroU.cd_uo:=aCdUoOrigine;
      aRecVpStmRiepilogoCentroU.ds_uo:=aDsUoOrigine;
      aRecVpStmRiepilogoCentroU.cd_sezionale:=aCdTipoSezionale;
      IF aTipoReport = 'P' THEN
         IF aDtInizioStampaRegistri IS NULL THEN
            aRecVpStmRiepilogoCentroU.stato_registro:='REGISTRO PROVVISORIO';
         ELSE
            aRecVpStmRiepilogoCentroU.stato_registro:='REGISTRO DEFINITIVO';
         END IF;
      ELSE
        if (to_char(aDataInizio,'ddmmyyyy')!='0101'||to_char(aDataInizio,'yyyy') and
           to_char(aDataFine,'ddmmyyyy')!='3112'||to_char(aDataFine,'yyyy')) then
         IF aDtInizioStampaRegistri IS NULL THEN
            IF stringaErrore IS NULL THEN
               stringaErrore:=aCdUoOrigine;
            ELSE
               IF LENGTH(stringaErrore) < 1980 THEN
                  stringaErrore:=stringaErrore || '/' || aCdUoOrigine;
               END IF;
            END IF;
            RETURN;
         END IF;
        end if;
      END IF;
      aRecVpStmRiepilogoCentroU.imponibile:=0;
      aRecVpStmRiepilogoCentroU.iva:=0;
      aRecVpStmRiepilogoCentroU.iva_indetraibile:=0;
      aRecVpStmRiepilogoCentroU.iva_esigibile:=0;
      aRecVpStmRiepilogoCentroU.totale:=0;
      aRecVpStmRiepilogoCentroU.imponibile_split_payment:=0;
      aRecVpStmRiepilogoCentroU.iva_split_payment:=0;
      aRecVpStmRiepilogoCentroU.provvisorio_definitivo:=aTipoReport;
      aRecVpStmRiepilogoCentroU.acquisti_vendite:=aTipoRegistro;
      aRecVpStmRiepilogoCentroU.descrizione_gruppo:=descrizioneGruppoStm;

      -- Inserimento del protocollo per UO progressivo + data fine prospetto

      aSequenzaUo:=aSequenzaUo + 1;
      aRecVpStmRiepilogoCentroU.progressivo_uo:=aSequenzaUo || ' - ' || TO_CHAR(aDataFine,'DD/MM/YYYY');

      insVpStmRiepilogoCentro(aRecVpStmRiepilogoCentroU);

      -- Copia su record di stampa dettagli

      aRecVpStmRiepilogoCentroD:=aRecVpStmRiepilogoCentroU;
      aRecVpStmRiepilogoCentroD.tipologia_riga:='D';
      aRecVpStmRiepilogoCentroP:=aRecVpStmRiepilogoCentroU;
      aRecVpStmRiepilogoCentroP.tipologia_riga:='P';
      aRecVpStmRiepilogoCentroT:=aRecVpStmRiepilogoCentroU;
      aRecVpStmRiepilogoCentroT.tipologia_riga:='T';

   END;

   -------------------------------------------------------------------------------------------------
   -- Azzeramento variabili di memoria rottura per codice iva e gruppo

   memCodiceIva:=NULL;
   memDescrizioneIva:=NULL;
   memCodiceGruppoIva:=NULL;
   memDescrizioneGruppoIva:=NULL;

   -------------------------------------------------------------------------------------------------
   -- Ciclo principale lettura dettagli IVA

   -- Lettura dati ---------------------------------------------------------------------------------

   IF aTipoRegistro = 'A' THEN

      OPEN gen_cv FOR

           SELECT cd_tipo_sezionale,
                  gruppo_iva,
                  descrizione_gruppo_iva,
                  codice_iva,
                  descrizione_iva,
                  esigibilita_diff,
                  data_esigibilita_diff,
                  SUM(imponibile_dettaglio),
                  SUM(iva_dettaglio),
                  SUM(TRUNC(iva_indetraibile_dettaglio,2)),
                  0,
                  SUM(totale_dettaglio),
                  SUM(DECODE(FL_SPLIT_PAYMENT, 'Y', nvl(imponibile_dettaglio,0), 0)) imponibile_split_payment,
                  SUM(DECODE(FL_SPLIT_PAYMENT, 'Y', nvl(iva_dettaglio,0), 0)) iva_split_payment
           FROM   V_REGISTRO_IVA_ACQUISTI
           WHERE  cd_cds_origine = aCdCdsOrigine AND
                  cd_uo_origine = aCdUoOrigine AND
                  esercizio = aEsercizio AND
                  cd_tipo_sezionale = aCdTipoSezionale AND
                  (data_registrazione BETWEEN aDataInizio AND aDataFine) AND
                  comm_ist_dettaglio = aTiIstituzCommerc
           GROUP BY cd_tipo_sezionale,
                    gruppo_iva,
                    descrizione_gruppo_iva,
                    codice_iva,
                    descrizione_iva,
                    esigibilita_diff,
                    data_esigibilita_diff,
                    0
           ORDER BY cd_tipo_sezionale,
                    gruppo_iva,
                    codice_iva;

   ELSE

      OPEN gen_cv FOR

           SELECT cd_tipo_sezionale,
                  gruppo_iva,
                  descrizione_gruppo_iva,
                  codice_iva,
                  descrizione_iva,
                  esigibilita_diff,
                  data_esigibilita_diff,
                  SUM(imponibile_dettaglio),
                  SUM(iva_dettaglio),
                  0,
                  0,
                  SUM(totale_dettaglio),
                  SUM(DECODE(FL_SPLIT_PAYMENT, 'Y', nvl(imponibile_dettaglio,0), 0)) imponibile_split_payment,
                  SUM(DECODE(FL_SPLIT_PAYMENT, 'Y', nvl(iva_dettaglio,0), 0)) iva_split_payment
           FROM   V_REGISTRO_IVA_VENDITE
           WHERE  cd_cds_origine = aCdCdsOrigine AND
                  cd_uo_origine = aCdUoOrigine AND
                  esercizio = aEsercizio AND
                  cd_tipo_Sezionale = aCdTipoSezionale AND
                  (data_emissione BETWEEN aDataInizio AND aDataFine)
           GROUP BY cd_tipo_sezionale,
                    gruppo_iva,
                    descrizione_gruppo_iva,
                    codice_iva,
                    descrizione_iva,
                    esigibilita_diff,
                    data_esigibilita_diff,
                    0,
                    0
           /*
           UNION ALL
           SELECT cd_tipo_sezionale,
                  gruppo_iva,
                  descrizione_gruppo_iva,
                  codice_iva,
                  descrizione_iva,
                  esigibilita_diff,
                  data_esigibilita_diff,
                  SUM(imponibile_dettaglio),
                  SUM(iva_dettaglio),
                  0,
                  0,
                  SUM(totale_dettaglio),
                  SUM(DECODE(FL_SPLIT_PAYMENT, 'Y', nvl(imponibile_dettaglio,0), 0)) imponibile_split_payment,
                  SUM(DECODE(FL_SPLIT_PAYMENT, 'Y', nvl(iva_dettaglio,0), 0)) iva_split_payment
           FROM   V_REGISTRO_IVA_VENDITE
           WHERE  cd_cds_origine = aCdCdsOrigine AND
                  cd_uo_origine = aCdUoOrigine AND
                  esercizio = aEsercizio AND
                  cd_tipo_Sezionale = aCdTipoSezionale AND
                  data_emissione < aDataInizio AND
                  esigibilita_diff = 'Y' AND
                  data_esigibilita_diff IS NOT NULL AND
                  (data_esigibilita_diff BETWEEN aDataInizio AND aDataFine)
           GROUP BY cd_tipo_sezionale,
                    gruppo_iva,
                    descrizione_gruppo_iva,
                    codice_iva,
                    descrizione_iva,
                    esigibilita_diff,
                    data_esigibilita_diff,
                    0,
                    0*/
           ORDER BY cd_tipo_sezionale,
                    gruppo_iva,
                    codice_iva;

   END IF;

   -- Fetch ----------------------------------------------------------------------------------------

   BEGIN

      LOOP

         FETCH gen_cv INTO
               cv_cd_tipo_sezionale,
               cv_gruppo_iva,
               cv_descrizione_gruppo_iva,
               cv_codice_iva,
               cv_descrizione_iva,
               cv_esigibilita_diff,
               cv_data_esigibilita_diff,
               cv_imponibile,
               cv_iva,
               cv_iva_indetraibile,
               cv_iva_esigibile,
               cv_totale,
               cv_imponibile_split_payment,
               cv_iva_split_payment;

         EXIT WHEN gen_cv%NOTFOUND;

         trovatiRecord:=1;

         -- Calcolo la parte esigibile se elaboro sezionale di vendita

         IF aTipoRegistro = 'V' THEN
            IF cv_esigibilita_diff = 'N' THEN
               cv_iva_esigibile:=cv_iva;
            ELSE
               IF (cv_data_esigibilita_diff IS NOT NULL AND
                   (cv_data_esigibilita_diff BETWEEN aDataInizio AND aDataFine)) THEN
                  cv_iva_esigibile:=cv_iva;
               END IF;
            END IF;
         Elsif   aTipoRegistro = 'A' Then
                     IF cv_esigibilita_diff = 'N' THEN
                       cv_iva_esigibile:=cv_iva;
                    ELSE
                       IF (cv_data_esigibilita_diff IS NOT NULL AND
                           (cv_data_esigibilita_diff BETWEEN aDataInizio AND aDataFine)) THEN
                          cv_iva_esigibile:=cv_iva;
                       END IF;
                    END IF;
           End IF;

         -- E' la prima volta che entro nella procedura

         IF memCodiceIva IS NULL THEN
            memCodiceIva:=cv_codice_iva;
            memDescrizioneIva:=cv_descrizione_iva;
            memCodiceGruppoIva:=cv_gruppo_iva;
            memDescrizioneGruppoIva:=cv_descrizione_gruppo_iva;
         END IF;

         -- Codice iva in lettura diverso dal precedente stampo i totali per codice iva

         IF memCodiceIva != cv_codice_iva THEN
            aRecVpStmRiepilogoCentroD.cd_gruppo_iva:=memCodiceGruppoIva;
            aRecVpStmRiepilogoCentroD.cd_iva:=memCodiceIva;
            aRecVpStmRiepilogoCentroD.descrizione_riga:=SUBSTR('CODICE IVA ' || memCodiceIva || ' ' ||
                                                               memDescrizioneIva,1,200);
            aSequenza:=aSequenza + 1;
            aRecVpStmRiepilogoCentroD.sequenza:=aSequenza;
            insVpStmRiepilogoCentro(aRecVpStmRiepilogoCentroD);

            aRecVpStmRiepilogoCentroD.imponibile:=0;
            aRecVpStmRiepilogoCentroD.iva:=0;
            aRecVpStmRiepilogoCentroD.iva_indetraibile:=0;
            aRecVpStmRiepilogoCentroD.iva_esigibile:=0;
            aRecVpStmRiepilogoCentroD.totale:=0;
            aRecVpStmRiepilogoCentroD.imponibile_split_payment:=0;
            aRecVpStmRiepilogoCentroD.iva_split_payment:=0;

            memCodiceIva:=cv_codice_iva;
            memDescrizioneIva:=cv_descrizione_iva;
         END IF;

         -- Gruppo iva in lettura diverso dal precedente stampo i totali di gruppo

         IF memCodiceGruppoIva != cv_gruppo_iva THEN
            aRecVpStmRiepilogoCentroP.cd_gruppo_iva:=memCodiceGruppoIva;
            aRecVpStmRiepilogoCentroP.descrizione_riga:=SUBSTR('TOTALE GRUPPO' || memCodiceGruppoIva || ' ' ||
                                                               memDescrizioneGruppoIva,1,200);
            aSequenza:=aSequenza + 1;
            aRecVpStmRiepilogoCentroP.sequenza:=aSequenza;
            insVpStmRiepilogoCentro(aRecVpStmRiepilogoCentroP);

            aRecVpStmRiepilogoCentroP.imponibile:=0;
            aRecVpStmRiepilogoCentroP.iva:=0;
            aRecVpStmRiepilogoCentroP.iva_indetraibile:=0;
            aRecVpStmRiepilogoCentroP.iva_esigibile:=0;
            aRecVpStmRiepilogoCentroP.totale:=0;
            aRecVpStmRiepilogoCentroP.imponibile_split_payment:=0;
            aRecVpStmRiepilogoCentroP.iva_split_payment:=0;

            memCodiceGruppoIva:=cv_gruppo_iva;
            memDescrizioneGruppoIva:=cv_descrizione_gruppo_iva;
         END IF;


         -- Codice iva in lettura uguale al precedente

         aRecVpStmRiepilogoCentroD.imponibile:=aRecVpStmRiepilogoCentroD.imponibile + cv_imponibile;
         aRecVpStmRiepilogoCentroD.iva:=aRecVpStmRiepilogoCentroD.iva + cv_iva;
         aRecVpStmRiepilogoCentroD.iva_indetraibile:=aRecVpStmRiepilogoCentroD.iva_indetraibile + cv_iva_indetraibile;
         aRecVpStmRiepilogoCentroD.iva_esigibile:=aRecVpStmRiepilogoCentroD.iva_esigibile + cv_iva_esigibile;
         aRecVpStmRiepilogoCentroD.totale:=aRecVpStmRiepilogoCentroD.totale + cv_totale;
         aRecVpStmRiepilogoCentroD.imponibile_split_payment:=aRecVpStmRiepilogoCentroD.imponibile_split_payment + cv_imponibile_split_payment;
         aRecVpStmRiepilogoCentroD.iva_split_payment:=aRecVpStmRiepilogoCentroD.iva_split_payment + cv_iva_split_payment;
         aRecVpStmRiepilogoCentroP.imponibile:=aRecVpStmRiepilogoCentroP.imponibile + cv_imponibile;
         aRecVpStmRiepilogoCentroP.iva:=aRecVpStmRiepilogoCentroP.iva + cv_iva;
         aRecVpStmRiepilogoCentroP.iva_indetraibile:=aRecVpStmRiepilogoCentroP.iva_indetraibile + cv_iva_indetraibile;
         aRecVpStmRiepilogoCentroP.iva_esigibile:=aRecVpStmRiepilogoCentroP.iva_esigibile + cv_iva_esigibile;
         aRecVpStmRiepilogoCentroP.totale:=aRecVpStmRiepilogoCentroP.totale + cv_totale;
         aRecVpStmRiepilogoCentroP.iva_split_payment:=aRecVpStmRiepilogoCentroP.iva_split_payment + cv_iva_split_payment;
         aRecVpStmRiepilogoCentroP.imponibile_split_payment:=aRecVpStmRiepilogoCentroP.imponibile_split_payment + cv_imponibile_split_payment;
         aRecVpStmRiepilogoCentroT.imponibile:=aRecVpStmRiepilogoCentroT.imponibile + cv_imponibile;
         aRecVpStmRiepilogoCentroT.iva:=aRecVpStmRiepilogoCentroT.iva + cv_iva;
         aRecVpStmRiepilogoCentroT.iva_indetraibile:=aRecVpStmRiepilogoCentroT.iva_indetraibile + cv_iva_indetraibile;
         aRecVpStmRiepilogoCentroT.iva_esigibile:=aRecVpStmRiepilogoCentroT.iva_esigibile + cv_iva_esigibile;
         aRecVpStmRiepilogoCentroT.totale:=aRecVpStmRiepilogoCentroT.totale + cv_totale;
         aRecVpStmRiepilogoCentroT.imponibile_split_payment:=aRecVpStmRiepilogoCentroT.imponibile_split_payment + cv_imponibile_split_payment;
         aRecVpStmRiepilogoCentroT.iva_split_payment:=aRecVpStmRiepilogoCentroT.iva_split_payment + cv_iva_split_payment;

      END LOOP;

      -- In uscita stampo tutti i record residui ---------------------------------------------------

      IF trovatiRecord = 1 THEN

         -- Rigo codice IVA

         aRecVpStmRiepilogoCentroD.cd_gruppo_iva:=memCodiceGruppoIva;
         aRecVpStmRiepilogoCentroD.cd_iva:=memCodiceIva;
         aRecVpStmRiepilogoCentroD.descrizione_riga:=SUBSTR('CODICE IVA ' || memCodiceIva || ' ' ||
                                                            memDescrizioneIva,1,200);
         aSequenza:=aSequenza + 1;
         aRecVpStmRiepilogoCentroD.sequenza:=aSequenza;
         insVpStmRiepilogoCentro(aRecVpStmRiepilogoCentroD);

         -- Rigo gruppo IVA

         aRecVpStmRiepilogoCentroP.cd_gruppo_iva:=memCodiceGruppoIva;
         aRecVpStmRiepilogoCentroP.descrizione_riga:=SUBSTR('TOTALE GRUPPO' || memCodiceGruppoIva || ' ' ||
                                                            memDescrizioneGruppoIva,1,200);
         aSequenza:=aSequenza + 1;
         aRecVpStmRiepilogoCentroP.sequenza:=aSequenza;
         insVpStmRiepilogoCentro(aRecVpStmRiepilogoCentroP);

      END IF;

      -- Rigo totale per UO

      aRecVpStmRiepilogoCentroT.descrizione_riga:='TOTALE PER UO ' || aCdUoOrigine;
      aSequenza:=aSequenza + 1;
      aRecVpStmRiepilogoCentroT.sequenza:=aSequenza;
      insVpStmRiepilogoCentro(aRecVpStmRiepilogoCentroT);

   END;

END insFatturePerRiepilogoCentro;


-- =================================================================================================
-- Inserimento record in VP_STM_RIEPILOGATIVO_CENTRO
-- =================================================================================================
PROCEDURE insVpStmRiepilogoCentro
   (
    aRecVpStmRiepilogoCentro VP_STM_RIEPILOGATIVO_CENTRO%ROWTYPE
   ) IS

BEGIN

   INSERT INTO VP_STM_RIEPILOGATIVO_CENTRO
          (id_report,
           gruppo,
           tipologia_riga,
           sequenza,
           sottogruppo,
           cd_cds,
           cd_uo,
           ds_uo,
           cd_sezionale,
           stato_registro,
           cd_gruppo_iva,
           cd_iva,
           descrizione_riga,
           imponibile,
           iva,
           iva_indetraibile,
           iva_esigibile,
           totale,
           progressivo_uo,
           esercizio_rif,
           ragione_sociale_ente,
           partita_iva_ente,
           provvisorio_definitivo,
           acquisti_vendite,
           descrizione_gruppo,
           imponibile_split_payment,
           iva_split_payment)
   VALUES (aRecVpStmRiepilogoCentro.id_report,
           aRecVpStmRiepilogoCentro.gruppo,
           aRecVpStmRiepilogoCentro.tipologia_riga,
           aRecVpStmRiepilogoCentro.sequenza,
           aRecVpStmRiepilogoCentro.sottogruppo,
           aRecVpStmRiepilogoCentro.cd_cds,
           aRecVpStmRiepilogoCentro.cd_uo,
           aRecVpStmRiepilogoCentro.ds_uo,
           aRecVpStmRiepilogoCentro.cd_sezionale,
           aRecVpStmRiepilogoCentro.stato_registro,
           aRecVpStmRiepilogoCentro.cd_gruppo_iva,
           aRecVpStmRiepilogoCentro.cd_iva,
           aRecVpStmRiepilogoCentro.descrizione_riga,
           aRecVpStmRiepilogoCentro.imponibile,
           aRecVpStmRiepilogoCentro.iva,
           aRecVpStmRiepilogoCentro.iva_indetraibile,
           aRecVpStmRiepilogoCentro.iva_esigibile,
           aRecVpStmRiepilogoCentro.totale,
           aRecVpStmRiepilogoCentro.progressivo_uo,
           aRecVpStmRiepilogoCentro.esercizio_rif,
           aRecVpStmRiepilogoCentro.ragione_sociale_ente,
           aRecVpStmRiepilogoCentro.partita_iva_ente,
           aRecVpStmRiepilogoCentro.provvisorio_definitivo,
           aRecVpStmRiepilogoCentro.acquisti_vendite,
           aRecVpStmRiepilogoCentro.descrizione_gruppo,
           aRecVpStmRiepilogoCentro.imponibile_split_payment,
           aRecVpStmRiepilogoCentro.iva_split_payment);

EXCEPTION

   WHEN DUP_VAL_ON_INDEX THEN
        IBMERR001.RAISE_ERR_GENERICO
           ('Chiave duplicata in inserimento riepilogo iva centro su tabella REPORT_GENERICO');

END insVpStmRiepilogoCentro;


-- =================================================================================================
-- Stampa del registro riepilogativo senza dettaglio per sezionale. Si esegue solo se risultano
-- elaborati pi� di un sezionale
-- =================================================================================================
PROCEDURE insTotaliPerRiepilogoCentro
   (
    repID INTEGER,
    aCdCds VARCHAR2,
    aCdUo VARCHAR2,
    aEsercizio NUMBER,
    aSequenza IN OUT INTEGER,
    aTipoReport VARCHAR2,
    gruppoStm CHAR,
    sottoGruppoStm CHAR,
    descrizioneGruppoStm VARCHAR2
   ) IS

   cv_gruppo_iva_a VARCHAR2(50);
   cv_descrizione_riga_a VARCHAR2(200);
   cv_acquisti_vendite_a VARCHAR2(10);
   cv_imponibile_a NUMBER(15,2);
   cv_iva_a NUMBER(15,2);
   cv_iva_indetraibile_a NUMBER(15,2);
   cv_iva_esigibile_a NUMBER(15,2);
   cv_totale_a NUMBER(15,2);
   cv_imponibile_split_payment_a NUMBER(15,2);
   cv_iva_split_payment_a NUMBER(15,2);

   cv_cd_iva_b VARCHAR2(50);
   cv_descrizione_riga_b VARCHAR2(200);
   cv_acquisti_vendite_b VARCHAR2(10);
   cv_imponibile_b NUMBER(15,2);
   cv_iva_b NUMBER(15,2);
   cv_iva_indetraibile_b NUMBER(15,2);
   cv_iva_esigibile_b NUMBER(15,2);
   cv_totale_b NUMBER(15,2);
   cv_imponibile_split_payment_b NUMBER(15,2);
   cv_iva_split_payment_b NUMBER(15,2);

   gen_cv GenericCurTyp;
   gen_cv_a GenericCurTyp;
   gen_cv_b GenericCurTyp;

BEGIN


   -------------------------------------------------------------------------------------------------
   -- Ciclo principale - aggregazione per gruppi iva

   BEGIN

      OPEN gen_cv_a FOR

           SELECT cd_gruppo_iva,
                  descrizione_riga,
                  acquisti_vendite,
                  SUM(imponibile),
                  SUM(iva),
                  SUM(iva_indetraibile),
                  SUM(iva_esigibile),
                  SUM(totale),
                  SUM(imponibile_split_payment),
                  SUM(iva_split_payment)
           FROM   VP_STM_RIEPILOGATIVO_CENTRO
           WHERE  id_report = repID AND
                  gruppo = 'B' AND
                  tipologia_riga = 'P'
           GROUP BY cd_gruppo_iva,
                    descrizione_riga,
                    acquisti_vendite
           ORDER BY cd_gruppo_iva;

      LOOP

         FETCH gen_cv_a INTO
               cv_gruppo_iva_a,
               cv_descrizione_riga_a,
               cv_acquisti_vendite_a,
               cv_imponibile_a,
               cv_iva_a,
               cv_iva_indetraibile_a,
               cv_iva_esigibile_a,
               cv_totale_a,
               cv_imponibile_split_payment_a,
               cv_iva_split_payment_a;

         EXIT WHEN gen_cv_a%NOTFOUND;

         -- ciclo per recupero importi per codice iva in base al gruppo

         OPEN gen_cv_b FOR

              SELECT cd_iva,
                     descrizione_riga,
                     acquisti_vendite,
                     SUM(imponibile),
                     SUM(iva),
                     SUM(iva_indetraibile),
                     SUM(iva_esigibile),
                     SUM(totale),
                     SUM(imponibile_split_payment),
                     SUM(iva_split_payment)
              FROM   VP_STM_RIEPILOGATIVO_CENTRO
              WHERE  id_report = repID AND
                     gruppo = 'B' AND
                     tipologia_riga = 'D' AND
                     cd_gruppo_iva = cv_gruppo_iva_a
              GROUP BY cd_iva,
                       descrizione_riga,
                       acquisti_vendite
              ORDER BY cd_iva;

         -- Fetch cursore codici iva e inserimento su REPORT_GENERICO del dettaglio

         LOOP

            FETCH gen_cv_b INTO
                  cv_cd_iva_b,
                  cv_descrizione_riga_b,
                  cv_acquisti_vendite_b,
                  cv_imponibile_b,
                  cv_iva_b,
                  cv_iva_indetraibile_b,
                  cv_iva_esigibile_b,
                  cv_totale_b,
                  cv_imponibile_split_payment_b,
                  cv_iva_split_payment_b;

            EXIT WHEN gen_cv_b%NOTFOUND;

            aSequenza:=aSequenza + 20;

            BEGIN

               INSERT INTO VP_STM_RIEPILOGATIVO_CENTRO
                      (id_report,
                       gruppo,
                       tipologia_riga,
                       sequenza,
                       sottogruppo,
                       cd_cds,
                       cd_uo,
                       ds_uo,
                       cd_sezionale,
                       stato_registro,
                       cd_gruppo_iva,
                       cd_iva,
                       descrizione_riga,
                       imponibile,
                       iva,
                       iva_indetraibile,
                       iva_esigibile,
                       totale,
                       provvisorio_definitivo,
                       acquisti_vendite,
                       descrizione_gruppo,
                       imponibile_split_payment,
                       iva_split_payment)
               VALUES (repID,
                       gruppoStm,
                       'D',
                       aSequenza,
                       sottoGruppoStm,
                       aCdCds,
                       aCdUo,
                       NULL,
                       NULL,
                       NULL,
                       NULL,
                       cv_cd_iva_b,
                       cv_descrizione_riga_b,
                       cv_imponibile_b,
                       cv_iva_b,
                       cv_iva_indetraibile_b,
                       cv_iva_esigibile_b,
                       cv_totale_b,
                       aTipoReport,
                       cv_acquisti_vendite_b,
                       descrizioneGruppoStm,
                       cv_imponibile_split_payment_b,
                       cv_iva_split_payment_b);

            EXCEPTION

               WHEN DUP_VAL_ON_INDEX THEN
                    IBMERR001.RAISE_ERR_GENERICO
                       ('Chiave duplicata in inserimento riepilogo iva centro su tabella REPORT_GENERICO' );

            END;

         END LOOP;

         CLOSE gen_cv_b;

         -- Inserimento del totale di gruppo

         aSequenza:=aSequenza + 20;

         BEGIN

            INSERT INTO VP_STM_RIEPILOGATIVO_CENTRO
                   (id_report,
                    gruppo,
                    tipologia_riga,
                    sequenza,
                    sottogruppo,
                    cd_cds,
                    cd_uo,
                    ds_uo,
                    cd_sezionale,
                    stato_registro,
                    cd_gruppo_iva,
                    cd_iva,
                    descrizione_riga,
                    imponibile,
                    iva,
                    iva_indetraibile,
                    iva_esigibile,
                    totale,
                    provvisorio_definitivo,
                    acquisti_vendite,
                    descrizione_gruppo,
                    imponibile_split_payment,
                    iva_split_payment)
            VALUES (repID,
                    gruppoStm,
                    'P',
                    aSequenza,
                    sottoGruppoStm,
                    aCdCds,
                    aCdUo,
                    NULL,
                    NULL,
                    NULL,
                    cv_gruppo_iva_a,
                    NULL,
                    cv_descrizione_riga_a,
                    cv_imponibile_a,
                    cv_iva_a,
                    cv_iva_indetraibile_a,
                    cv_iva_esigibile_a,
                    cv_totale_a,
                    aTipoReport,
                    cv_acquisti_vendite_a,
                    descrizioneGruppoStm,
                    cv_imponibile_split_payment_a,
                    cv_iva_split_payment_a);

         EXCEPTION

            WHEN DUP_VAL_ON_INDEX THEN
                 IBMERR001.RAISE_ERR_GENERICO
                    ('Chiave duplicata in inserimento riepilogo iva su tabella REPORT_GENERICO' );

         END;

      END LOOP;

      CLOSE gen_cv_a;

      -- inserimento del totale generale

      OPEN gen_cv FOR

           SELECT acquisti_vendite,
                  SUM(imponibile),
                  SUM(iva),
                  SUM(iva_indetraibile),
                  SUM(iva_esigibile),
                  SUM(totale),
                  SUM(imponibile_split_payment),
                  SUM(iva_split_payment)
           FROM   VP_STM_RIEPILOGATIVO_CENTRO
           WHERE  id_report = repID AND
                  gruppo = 'C' AND
                  tipologia_riga = 'P'
           GROUP BY acquisti_vendite;

      LOOP

         FETCH gen_cv INTO
               cv_acquisti_vendite,
               cv_imponibile,
               cv_iva,
               cv_iva_indetraibile,
               cv_iva_esigibile,
               cv_totale,
               cv_imponibile_split_payment,
               cv_iva_split_payment;

         EXIT WHEN gen_cv%NOTFOUND;

         aSequenza:=aSequenza + 20;

         BEGIN

            INSERT INTO VP_STM_RIEPILOGATIVO_CENTRO
                   (id_report,
                    gruppo,
                    tipologia_riga,
                    sequenza,
                    sottogruppo,
                    cd_cds,
                    cd_uo,
                    ds_uo,
                    cd_sezionale,
                    stato_registro,
                    cd_gruppo_iva,
                    cd_iva,
                    descrizione_riga,
                    imponibile,
                    iva,
                    iva_indetraibile,
                    iva_esigibile,
                    totale,
                    provvisorio_definitivo,
                    acquisti_vendite,
                    descrizione_gruppo,
                    imponibile_split_payment,
                    iva_split_payment)
            VALUES (repID,
                    gruppoStm,
                    'T',
                    aSequenza,
                    sottoGruppoStm,
                    aCdCds,
                    aCdUo,
                    NULL,
                    NULL,
                    NULL,
                    NULL,
                    NULL,
                    'TOTALE_GENERALE',
                    cv_imponibile,
                    cv_iva,
                    cv_iva_indetraibile,
                    cv_iva_esigibile,
                    cv_totale,
                    aTipoReport,
                    cv_acquisti_vendite,
                    descrizioneGruppoStm,
                    cv_imponibile_split_payment,
                    cv_iva_split_payment);

         EXCEPTION

            WHEN DUP_VAL_ON_INDEX THEN
                 IBMERR001.RAISE_ERR_GENERICO
                    ('Chiave duplicata in inserimento riepilogo iva su tabella REPORT_GENERICO');

         END;

      END LOOP;

      CLOSE gen_cv;

   END;

END insTotaliPerRiepilogoCentro;

-- =================================================================================================
-- Conferma ed annullamento stampa registri IVA dal centro
-- =================================================================================================
PROCEDURE confermaAnnullaRegistro
   (
    aCdCds VARCHAR2,
    aCdUo VARCHAR2,
    aEsercizio NUMBER,
    aCodiceSezionale  VARCHAR2,
    aDataInizio DATE,
    aDataFine DATE,
    aUtente VARCHAR2,
    aTipoAzione CHAR
   ) IS
   aTipoReportStato VARCHAR2(100);
   aTiDocumento CHAR(1);
   aStato CHAR(1);
   aStatement VARCHAR2(200);
   aTipoReportStatoAltro VARCHAR2(50);
   aCodiceSezionaleAltro VARCHAR2(10);
   aGruppoReportAltro VARCHAR2(10);
   flEsistePeriodo INTEGER;
   flEsistePeriodoPrecedente INTEGER;
   flEsistePeriodoPrima INTEGER;
   flEsistePeriodoDopo INTEGER;
   aProtocolloFT NUMBER(10);
   aProtocolloNC NUMBER(10);
   aProtocolloND NUMBER(10);
   aProtocolloPG NUMBER(10);
   aProtocolloOriFT NUMBER(10);
   aProtocolloOriNC NUMBER(10);
   aProtocolloOriND NUMBER(10);
   aProtocolloOriPG NUMBER(10);

   eseguiUpgSezionale CHAR(1);
   eseguiLock CHAR(1);

   aRecTipoSezionale TIPO_SEZIONALE%ROWTYPE;
   aRecReportStato REPORT_STATO%ROWTYPE;

BEGIN

   -------------------------------------------------------------------------------------------------
   -- Lettura del TIPO_SEZIONALE

   aRecTipoSezionale:=CNRCTB120.getTipoSezionale(aCodiceSezionale);

   IF aRecTipoSezionale.ti_acquisti_vendite = 'A' THEN
      aTipoReportStato:=CNRCTB255.TI_REGISTRO_ACQUISTI;
   ELSE
      aTipoReportStato:=CNRCTB255.TI_REGISTRO_VENDITE;
   END IF;

   aTiDocumento:='*';
   aStato:=NULL;
   flEsistePeriodo:=0;
   flEsistePeriodoPrecedente:=0;
   flEsistePeriodoPrima:=0;
   flEsistePeriodoDopo:=0;
   aProtocolloFT:=0;
   aProtocolloNC:=0;
   aProtocolloND:=0;
   aProtocolloPG:=0;
   aProtocolloOriFT:=0;
   aProtocolloOriNC:=0;
   aProtocolloOriND:=0;
   aProtocolloOriPG:=0;

   eseguiUpgSezionale:='N';
   eseguiLock:='Y';

   -------------------------------------------------------------------------------------------------
   -- Lettura record di REPORT_STATO ed attivazione controlli alla conferma o all'eliminazione

   aRecReportStato:=CNRCTB255.getReportStato(aCdCds,
                                             aCdUo,
                                             aCodiceSezionale,
                                             aTiDocumento,
                                             aTipoReportStato,
                                             aDataInizio,
                                             aDataFine,
                                             aStato);

   -- Controlli in sede di conferma registro -------------------------------------------------------

   IF aTipoAzione = 'C' THEN

      -- Se lo stato del registro �C' l'operazione non �mmessa

      IF aRecReportStato.stato = 'C' THEN
         IBMERR001.RAISE_ERR_GENERICO
              ('Operazione non necessaria, il sezionale ' || aCodiceSezionale ||
               ' risulta gi�n stato definitivo per il periodo richiesto');
         RETURN;
      END IF;

   END IF;

   -- Controlli in sede di annulla registro --------------------------------------------------------

   IF aTipoAzione = 'A' THEN

      -- Se lo stato del registro �C' l'operazione non �mmessa

      IF aRecReportStato.stato = 'C' THEN
         IBMERR001.RAISE_ERR_GENERICO
            ('Operazione non consentita, il sezionale ' || aCodiceSezionale ||
             ' risulta gi�n stato definitivo per il periodo richiesto');
         RETURN;
      END IF;

      -- Verifico che la stampa definitiva del sezionale sia l'ultima per lo stesso

      CNRCTB255.getStatoReportStatoSiCdSez(aCdCds,
                                           aCdUo,
                                           aCodiceSezionale,
                                           aDataInizio,
                                           aDataFine,
                                           aTiDocumento,
                                           aTipoReportStato,
                                           flEsistePeriodo,
                                           flEsistePeriodoPrecedente,
                                           flEsistePeriodoPrima,
                                           flEsistePeriodoDopo);

      IF flEsistePeriodoDopo = 1 THEN
         IBMERR001.RAISE_ERR_GENERICO
            ('Operazione non consentita, il sezionale ' || aCodiceSezionale ||
             ' risulta essere in stato definitivo per un periodo successivo a quello selezionato');
         RETURN;
      END IF;

      -- Verifico che non esista alcuna liquidazione definitiva per il sezionale in elaborazione

      aCodiceSezionaleAltro:='*';
      aTipoReportStatoAltro:=CNRCTB255.TI_LIQUIDAZIONE;

      IF aRecTipoSezionale.ti_acquisti_vendite = 'A' THEN
         IF aRecTipoSezionale.ti_istituz_commerc = 'C' THEN
            aGruppoReportAltro:=CNRCTB250.TI_LIQ_IVA_COMMERC;
         Else
            IF    aRecTipoSezionale.fl_san_marino_senza_iva = 'Y' And aRecTipoSezionale.ti_bene_servizio != 'S' THEN
                  aGruppoReportAltro:=CNRCTB250.TI_LIQ_IVA_ISTSMSI;
            ELSIF aRecTipoSezionale.fl_intra_ue = 'Y' And aRecTipoSezionale.ti_bene_servizio = 'B' THEN
                  aGruppoReportAltro:=CNRCTB250.TI_LIQ_IVA_ISTINTR;
            Elsif (aRecTipoSezionale.fl_intra_ue = 'Y' Or aRecTipoSezionale.fl_extra_ue ='Y' Or aRecTipoSezionale.fl_san_marino_senza_iva = 'Y')
                   And aRecTipoSezionale.ti_bene_servizio = 'S' THEN
                  aGruppoReportAltro:=CNRCTB250.TI_LIQ_IVA_ISTSNR;
            END IF;
         END IF;
      ELSE
         aGruppoReportAltro:=CNRCTB250.TI_LIQ_IVA_COMMERC;
      END IF;

      CNRCTB255.getStatoReportStatoSiCdSez(aCdCds,
                                           aCdUo,
                                           aCodiceSezionaleAltro,
                                           aDataInizio,
                                           aDataFine,
                                           aGruppoReportAltro,
                                           aTipoReportStatoAltro,
                                           flEsistePeriodo,
                                           flEsistePeriodoPrecedente,
                                           flEsistePeriodoPrima,
                                           flEsistePeriodoDopo);

      IF (flEsistePeriodo = 1 OR
          flEsistePeriodoDopo = 1) THEN
         IBMERR001.RAISE_ERR_GENERICO
            ('Operazione non consentita, il sezionale ' || aCodiceSezionale || ' risulta essere compreso in una ' ||
             'liquidazione definitiva nel periodo selezionato o in uno successivo');
         RETURN;
      END IF;

   END IF;

   -------------------------------------------------------------------------------------------------
   -- Esecuzione delle procedure di aggiornamento

   -- Aggiornamento in sede di conferma registro ---------------------------------------------------

   IF aTipoAzione = 'C' THEN

      BEGIN

         UPDATE REPORT_STATO
         SET    stato = 'C',
                utuv = aUtente,
                duva = SYSDATE,
                pg_ver_rec = pg_ver_rec +1
         WHERE  cd_cds = aCdCds AND
                cd_unita_organizzativa = aCdUo AND
                esercizio = aEsercizio AND
                cd_tipo_sezionale = aCodiceSezionale AND
                ti_documento = aTiDocumento AND
                tipo_report = aTipoReportStato AND
                dt_inizio = aDataInizio AND
                dt_fine = aDataFine AND
                stato = 'B';

      END;

   END IF;


   -- Aggiornamento in sede di annulla registro -------------------------------------------------

   IF aTipoAzione = 'A' THEN

      -- Statement di update

      aStatement := ' protocollo_iva = NULL, ' || ' protocollo_iva_generale = NULL ';

      -- Estraggo il valore minimo dei protocolli per stornare la numerazione del sezionale

      CNRCTB255.getMinProtocolloIva(aRecReportStato,
                                    aTipoReportStato,
                                    aProtocolloFT,
                                    aProtocolloNC,
                                    aProtocolloND,
                                    aProtocolloPG,
                                    eseguiUpgSezionale);

      -- Estraggo il valore attuale del corrente sul sezionale in elaborazione

      CNRCTB120.getProtCorrenteSezionale(aCdCds,
                                         aCdUo,
                                         aEsercizio,
                                         aCodiceSezionale,
                                         eseguiLock,
                                         aProtocolloOriFT,
                                         aProtocolloOriNC,
                                         aProtocolloOriND,
                                         aProtocolloOriPG);

      -- Ciclo di lettura record in report dettaglio

      BEGIN

         FOR riga_det IN
             (SELECT *
              FROM   REPORT_DETTAGLIO
              WHERE  cd_cds = aCdCds AND
                     cd_unita_organizzativa = aCdUo AND
                     esercizio = aEsercizio AND
                     tipo_report = aTipoReportStato AND
                     cd_tipo_Sezionale = aCodiceSezionale AND
                     data_inizio = aDataInizio AND
                     data_fine = aDataFine)

         LOOP

            IF aRecTipoSezionale.ti_acquisti_vendite = 'A' THEN

               CNRCTB100.LOCKDOCAMM(CNRCTB100.TI_FATTURA_PASSIVA,
                                    riga_det.cd_cds_altro,
                                    riga_det.esercizio,
                                    riga_det.cd_uo_altro,
                                    riga_det.pg_documento);
               CNRCTB100.UPDATEDOCAMM(CNRCTB100.TI_FATTURA_PASSIVA,
                                      riga_det.cd_cds_altro,
                                      riga_det.esercizio,
                                      riga_det.cd_uo_altro,
                                      riga_det.pg_documento,
                                      aStatement,
                                      NULL,
                                      aUtente,
                                      SYSDATE);

            ELSE

               CNRCTB100.LOCKDOCAMM(CNRCTB100.TI_AUTOFATTURA,
                                    riga_det.cd_cds_altro,
                                    riga_det.esercizio,
                                    riga_det.cd_uo_altro,
                                    riga_det.pg_documento);
               CNRCTB100.UPDATEDOCAMM(CNRCTB100.TI_AUTOFATTURA,
                                      riga_det.cd_cds_altro,
                                      riga_det.esercizio,
                                      riga_det.cd_uo_altro,
                                      riga_det.pg_documento,
                                      aStatement,
                                      NULL,
                                      aUtente,
                                      SYSDATE);

            END IF;

         END LOOP;

         -- Eliminazione del dettaglio del registro in esame

         DELETE REPORT_DETTAGLIO
         WHERE  cd_cds = aCdCds AND
                cd_unita_organizzativa = aCdUo AND
                esercizio = aEsercizio AND
                cd_tipo_sezionale = aCodiceSezionale AND
                tipo_report = aTipoReportStato AND
                data_inizio = aDataInizio AND
                data_fine = aDataFine;

         DELETE REPORT_STATO
         WHERE  cd_cds = aCdCds AND
                cd_unita_organizzativa = aCdUo AND
                esercizio = aEsercizio AND
                cd_tipo_sezionale = aCodiceSezionale AND
                ti_documento = aTiDocumento AND
                tipo_report = aTipoReportStato AND
                dt_inizio = aDataInizio AND
                dt_fine = aDataFine AND
                stato = 'B';

         -- Aggiornamento del valore corrente su sezionale

         IF eseguiUpgSezionale ='Y' THEN

            IF aProtocolloFT = 0 THEN
               aProtocolloFT:=aProtocolloOriFT;
            ELSE
               aProtocolloFT:=aProtocolloFT -1;
            END IF;

            IF aProtocolloNC = 0 THEN
               aProtocolloNC:=aProtocolloOriNC;
            ELSE
               aProtocolloNC:=aProtocolloNC -1;
            END IF;

            IF aProtocolloND = 0 THEN
               aProtocolloND:=aProtocolloOriND;
            ELSE
               aProtocolloND:=aProtocolloND -1;
            END IF;

            IF aProtocolloPG = 0 THEN
               aProtocolloPG:=aProtocolloOriPG;
            ELSE
               aProtocolloPG:=aProtocolloPG -1;
            END IF;

            CNRCTB120.upgSezionalePgCorrente(aCdCds,
                                             aCdUo,
                                             aEsercizio,
                                             aCodiceSezionale,
                                             aProtocolloFT,
                                             aProtocolloNC,
                                             aProtocolloND,
                                             aProtocolloPG);

         END IF;

      END;

   END IF;

END confermaAnnullaRegistro;

-- =================================================================================================
-- Inserimento di una liquidazione
-- =================================================================================================
PROCEDURE insLiquidazioneIva
   (
    aRecLiquidazioneIva LIQUIDAZIONE_IVA%ROWTYPE
   ) IS

BEGIN

   INSERT INTO LIQUIDAZIONE_IVA
          (cd_cds,
           esercizio,
           cd_unita_organizzativa,
           dt_inizio,
           dt_fine,
           report_id,
           stato,
           annotazioni,
           dt_versamento,
           cod_azienda,
           cab,
           cd_cds_obb_accentr,
           esercizio_obb_accentr,
           esercizio_ori_obb_accentr,
           pg_obb_accentr,
           iva_vendite,
           iva_vendite_diff,
           iva_vend_diff_esig,
           iva_autofatt,
           iva_intraue,
           iva_debito,
           iva_acquisti,
           iva_acq_non_detr,
           iva_acquisti_diff,
           iva_acq_diff_esig,
           iva_credito,
           var_imp_per_prec,
           iva_non_vers_per_prec,
           iva_deb_cred_per_prec,
           cred_iva_comp_detr,
           iva_deb_cred,
           int_deb_liq_trim,
           cred_iva_spec_detr,
           acconto_iva_vers,
           iva_da_versare,
           iva_versata,
           cred_iva_infrann_rimb,
           cred_iva_infrann_comp,
           cd_tipo_documento,
           cd_cds_doc_amm,
           cd_uo_doc_amm,
           esercizio_doc_amm,
           pg_doc_amm,
           iva_credito_no_prorata,
           perc_prorata_detraibile,
           dacr,
           utcr,
           duva,
           utuv,
           pg_ver_rec,
           abi,
           tipo_liquidazione,
           iva_liq_esterna)
   VALUES (aRecLiquidazioneIva.cd_cds,
           aRecLiquidazioneIva.esercizio,
           aRecLiquidazioneIva.cd_unita_organizzativa,
           aRecLiquidazioneIva.dt_inizio,
           aRecLiquidazioneIva.dt_fine,
           aRecLiquidazioneIva.report_id,
           aRecLiquidazioneIva.stato,
           aRecLiquidazioneIva.annotazioni,
           aRecLiquidazioneIva.dt_versamento,
           aRecLiquidazioneIva.cod_azienda,
           aRecLiquidazioneIva.cab,
           aRecLiquidazioneIva.cd_cds_obb_accentr,
           aRecLiquidazioneIva.esercizio_obb_accentr,
           aRecLiquidazioneIva.esercizio_ori_obb_accentr,
           aRecLiquidazioneIva.pg_obb_accentr,
           aRecLiquidazioneIva.iva_vendite,
           aRecLiquidazioneIva.iva_vendite_diff,
           aRecLiquidazioneIva.iva_vend_diff_esig,
           aRecLiquidazioneIva.iva_autofatt,
           aRecLiquidazioneIva.iva_intraue,
           aRecLiquidazioneIva.iva_debito,
           aRecLiquidazioneIva.iva_acquisti,
           aRecLiquidazioneIva.iva_acq_non_detr,
           aRecLiquidazioneIva.iva_acquisti_diff,
           aRecLiquidazioneIva.iva_acq_diff_esig,
           aRecLiquidazioneIva.iva_credito,
           aRecLiquidazioneIva.var_imp_per_prec,
           aRecLiquidazioneIva.iva_non_vers_per_prec,
           aRecLiquidazioneIva.iva_deb_cred_per_prec,
           aRecLiquidazioneIva.cred_iva_comp_detr,
           aRecLiquidazioneIva.iva_deb_cred,
           aRecLiquidazioneIva.int_deb_liq_trim,
           aRecLiquidazioneIva.cred_iva_spec_detr,
           aRecLiquidazioneIva.acconto_iva_vers,
           aRecLiquidazioneIva.iva_da_versare,
           aRecLiquidazioneIva.iva_versata,
           aRecLiquidazioneIva.cred_iva_infrann_rimb,
           aRecLiquidazioneIva.cred_iva_infrann_comp,
           aRecLiquidazioneIva.cd_tipo_documento,
           aRecLiquidazioneIva.cd_cds_doc_amm,
           aRecLiquidazioneIva.cd_uo_doc_amm,
           aRecLiquidazioneIva.esercizio_doc_amm,
           aRecLiquidazioneIva.pg_doc_amm,
           aRecLiquidazioneIva.iva_credito_no_prorata,
           aRecLiquidazioneIva.perc_prorata_detraibile,
           aRecLiquidazioneIva.dacr,
           aRecLiquidazioneIva.utcr,
           aRecLiquidazioneIva.duva,
           aRecLiquidazioneIva.utuv,
           aRecLiquidazioneIva.pg_ver_rec,
           aRecLiquidazioneIva.abi,
           aRecLiquidazioneIva.tipo_liquidazione,
           aRecLiquidazioneIva.iva_liq_esterna);

END insLiquidazioneIva;

END CNRCTB265; -- PACKAGE END;
