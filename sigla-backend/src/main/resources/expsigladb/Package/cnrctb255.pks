CREATE OR REPLACE PACKAGE CNRCTB255 AS
-- =================================================================================================
--
-- CNRCTB255 - Funzioni di servizio per gestione della tabella REPORT_STATO e REPORT_DETTAGLIO
--
-- Date: 15/09/2003
-- Version: 4.8
--
-- Dependency: CNRCTB  IBMERR IBMUTL 001
--
-- History:
--
-- Date: 13/12/2002
-- Version: 1.0
--
-- Creazione package
--
-- Date: 17/12/2002
-- Version: 3.0
--
-- Inserita routine per inserimento in REPORT_DETTAGLIO
--
-- Date: 18/12/2002
-- Version: 3.1
--
-- Corretto recupero della REPORT_STATO per stampa riepilogativo per UO
-- Inserita routine per inserimento in REPORT_STATO
--
-- Date: 15/01/2003
-- Version: 3.2
--
-- Inserita la gestione differenziata della LIQUIDAZIONE_IVA per attivit? commerciali ed istituzionali
-- (San Marino senza IVA e Intra_ue).
--
-- Date: 03/02/2003
-- Version: 3.3
--
-- Controllo definitivo gestione corretta dell'id_report per liquidazione definitiva (id = 0)
--
-- Date: 06/02/2003
-- Version: 4.0
--
-- Valorizza, per ogni fattura, la data di inizio stampa registro IVA o di liquidazione se queste
-- sono state create in modo definitivo. Utilizzato per gestione iva differita
--
-- Date: 07/02/2003
-- Version: 4.1
--
-- Inserite nuove costanti per valorizzazione in REPORT_DETTAGLIO del tipo_report per l'identificazione
-- delle chiavi delle fatture ad esigibilit? differita divenute esigibili che siano state incluse in
-- una liquidazione
--
-- Date: 10/02/2003
-- Version: 4.2
--
-- Valorizza, per un dato sezionale e UO, la data di inizio stampa registro IVA o di liquidazione se
-- queste sono state create in modo definitivo.
--
-- Date: 13/02/2003
-- Version: 4.3
--
-- Fix errore in chiamata alla routine getIsSezionaleElaborataIva type NUMBER invece di VARCHAR2
--
-- Date: 21/02/2003
-- Version: 4.4
--
-- Inserita routine di lettura della tabella REPORT_STATO
--
-- Date: 26/02/2003
-- Version: 4.5
--
-- Inserita routine di lettura dei valori minimi dei protocolli iva per le fatture presenti in REPORT_DETTAGLIO.
-- Inserito controllo di no_data_found in caso di REPORT_DETTAGLIO vuota.
--
-- Date: 27/02/2003
-- Version: 4.6
--
-- Inserita gestione della liquidazione di massa al centro per le singole UO.
--
-- Date: 28/02/2003
-- Version: 4.7
--
-- Inserito metodo di aggiornamento di massa della REPORT_STATO per portare lo stato a C dei registri
-- IVA definitivi
--
-- Date: 15/09/2003
-- Version: 4.8
--
-- Attivazione gestione liquidazione IVA del mese di dicembre da eseguirsi sempre in esercizio
-- successivo. (chiusure).
-- La chiamata ? fatta sempre con periodo dicembre esercizio ed esercizio = esercizio + 1.
--
-- =================================================================================================
--
--
-- Constants:

   -- Identificativo elaborazione memorizzato in REPORT_STATO

   TI_REGISTRO_VENDITE CONSTANT VARCHAR2(50) :='REGISTRO_IVA';
   TI_REGISTRO_ACQUISTI CONSTANT VARCHAR2(50) :='REGISTRO_IVA';
   TI_RIEPILOGATIVO_ACQUISTI CONSTANT VARCHAR2(50) :='REGISTRO_RIEPILOGATIVO_ACQ';
   TI_RIEPILOGATIVO_VENDITE CONSTANT VARCHAR2(50) :='REGISTRO_RIEPILOGATIVO_VEN';
   TI_RIEPILOGATIVO_CENTRO CONSTANT VARCHAR2(50) :='REGISTRO_RIEPILOGATIVO_CENTRO';
   TI_ESIGIBILITA_DIFFERITA CONSTANT VARCHAR2(50) :='REGISTRO_ESIGIBILITA_IVA';
   TI_LIQUIDAZIONE CONSTANT VARCHAR2(50) :='LIQUIDAZIONE';

   -- Identificativo elaborazione memorizzato in REPORT_DETTAGLIO

   TI_LIQUIDAZIONE_VEN_ESIGIB CONSTANT VARCHAR2(50) :='LIQUIDAZIONE_VENDITE';
   TI_LIQUIDAZIONE_ACQ_ESIGIB CONSTANT VARCHAR2(50) :='LIQUIDAZIONE_ACQUISTI';


-- Variabili globali

   TYPE GenericCurTyp IS REF CURSOR;

-- Functions e Procedures:

----------------------------------------------------------------------------------------------------
-- FUNZIONI e PROCEDURE di servizio
----------------------------------------------------------------------------------------------------

-- Controllo entrate presenti in tabella REPORT_STATO valorizzando il parametro aCodiceSezionale

   PROCEDURE getStatoReportStatoSiCdSez
      (
       inCdCdsOrigine VARCHAR2,
       inCdUoOrigine VARCHAR2,
       aCodiceSezionale VARCHAR2,
       inDataInizio DATE,
       inDataFine DATE,
       inGruppoReport VARCHAR2,
       aTipoReportStato VARCHAR2,
       flEsistePeriodo IN OUT INTEGER,
       flEsistePeriodoPrecedente IN OUT INTEGER,
       flEsistePeriodoPrima IN OUT INTEGER,
       flEsistePeriodoDopo IN OUT INTEGER
      );

-- Controllo entrate presenti in tabella REPORT_STATO senza il parametro aCodiceSezionale

   PROCEDURE getStatoReportStatoNoCdSez
      (
       inCdCdsOrigine VARCHAR2,
       inCdUoOrigine VARCHAR2,
       inDataInizio DATE,
       inDataFine DATE,
       inGruppoReport VARCHAR2,
       aTipoReportStato VARCHAR2,
       flEsistePeriodo IN OUT INTEGER,
       flEsistePeriodoPrecedente IN OUT INTEGER,
       flEsistePeriodoPrima IN OUT INTEGER,
       flEsistePeriodoDopo IN OUT INTEGER
      );

-- Controllo entrate presenti in tabella REPORT_STATO senza il parametro gruppo report

   PROCEDURE getStatoReportStatoNoGruppo
      (
       inCdCdsOrigine VARCHAR2,
       inCdUoOrigine VARCHAR2,
       aCodiceSezionale VARCHAR2,
       inDataInizio DATE,
       inDataFine DATE,
       aTipoReportStato VARCHAR2,
       flEsistePeriodo IN OUT INTEGER,
       flEsistePeriodoPrecedente IN OUT INTEGER,
       flEsistePeriodoPrima IN OUT INTEGER,
       flEsistePeriodoDopo IN OUT INTEGER
      );

-- Inserimento record in REPORT_DETTAGLIO

   PROCEDURE insReportDettaglio
      (
       aRecReportDettaglio REPORT_DETTAGLIO%ROWTYPE
      );

-- Inserimento riga nella tabella REPORT STATO

   PROCEDURE inserisciInReportStato
      (
       inCdCdsOrigine VARCHAR2,
       inCdUoOrigine VARCHAR2,
       inEsercizio NUMBER,
       aCodiceSezionale VARCHAR2,
       inDataInizio DATE,
       inDataFine DATE,
       inTipoRegistro VARCHAR2,
       inGruppoReport VARCHAR2,
       aTipoReportStato VARCHAR2,
       idUtente VARCHAR2
      );

-- Valorizza la data di inizio stampa registro IVA o di liquidazione di una fattura

   PROCEDURE getIsFatturaElaborataIva
      (
       aCdCdsOrigine VARCHAR2,
       aCdUoOrigine VARCHAR2,
       aEsercizio NUMBER,
       aPgFattura NUMBER,
       aPgRiga    NUMBER,
       aTipoFattura VARCHAR2,
       aCdCds VARCHAR2,
       aCdUo VARCHAR2,
       aDtInizioStmRegistro IN OUT DATE,
       aDtInizioStmLiquidazione IN OUT DATE
      );

-- Valorizza la data di inizio stampa registro IVA o di liquidazione di un sezionale per una data UO

   PROCEDURE getIsSezionaleElaborataIva
      (
       aCdCds VARCHAR2,
       aCdUo VARCHAR2,
       aCdSezionale VARCHAR2,
       aDtInizio DATE,
       aDtFine DATE,
       aTipoDocumentoReportStato VARCHAR2,
       aDtInizioStmRegistro IN OUT DATE,
       aDtInizioStmLiquidazione IN OUT DATE
      );

-- Ritorna un record della REPORT_STATO

   FUNCTION getReportStato
      (
       aCdCds VARCHAR2,
       aCdUo VARCHAR2,
       aCdSezionale VARCHAR2,
       aTiDocumento VARCHAR2,
       aTipoReport VARCHAR2,
       aDtInizio DATE,
       aDtFine DATE,
       aStato VARCHAR2
      ) RETURN REPORT_STATO%ROWTYPE;

-- Restituisce il valore minimo dei protocolli iva delle fatture presenti in REPORT_DETTAGLIO
-- dato un record di REPORT_STATO

   PROCEDURE getMinProtocolloIva
      (
       aRecReportStato REPORT_STATO%ROWTYPE,
       aTipoReportStato VARCHAR2,
       aProtocolloFT IN OUT NUMBER,
       aProtocolloNC IN OUT NUMBER,
       aProtocolloND IN OUT NUMBER,
       aProtocolloPG IN OUT NUMBER,
       eseguiUpgSezionale IN OUT CHAR
      );

-- Aggiorna a C tutti i record di REPORT_STATO per stampa registri IVA

   PROCEDURE upgStatoRegistriIva
      (
       aEsercizio NUMBER,
       aDataInizio DATE,
       aDataFine DATE,
       aUtente VARCHAR2
      );

END CNRCTB255;


CREATE OR REPLACE PACKAGE BODY CNRCTB255 AS

-- =================================================================================================
-- Controllo entrate presenti in tabella REPORT_STATO valorizzando il parametro aCodiceSezionale
-- =================================================================================================
PROCEDURE getStatoReportStatoSiCdSez
   (
    inCdCdsOrigine VARCHAR2,
    inCdUoOrigine VARCHAR2,
    aCodiceSezionale VARCHAR2,
    inDataInizio DATE,
    inDataFine DATE,
    inGruppoReport VARCHAR2,
    aTipoReportStato VARCHAR2,
    flEsistePeriodo IN OUT INTEGER,
    flEsistePeriodoPrecedente IN OUT INTEGER,
    flEsistePeriodoPrima IN OUT INTEGER,
    flEsistePeriodoDopo IN OUT INTEGER
   ) IS
   aTipoDocumento CHAR(1);
   aDataInizioPrec DATE;
   aDataFinePrec DATE;

BEGIN

   aDataInizioPrec:=IBMUTL001.getAddMonth(inDataInizio, -1);
   aDataFinePrec:=IBMUTL001.getAddMonth(inDataFine, -1);
   flEsistePeriodo:=0;
   flEsistePeriodoPrecedente:=0;
   flEsistePeriodoPrima:=0;
   flEsistePeriodoDopo:=0;
   aTipoDocumento:='*';
   IF (aTipoReportStato = TI_RIEPILOGATIVO_ACQUISTI OR
       aTipoReportStato = TI_RIEPILOGATIVO_VENDITE OR
       aTipoReportStato = TI_LIQUIDAZIONE) THEN
      aTipoDocumento:=inGruppoReport;
   END IF;
--dbms_output.put_line('uo '||inCdUoOrigine);
--dbms_output.put_line('aTipoDocumento '||aTipoDocumento);
--dbms_output.put_line('aTipoReportStato '||aTipoReportStato);
--dbms_output.put_line('aCodiceSezionale '||aCodiceSezionale);
   -------------------------------------------------------------------------------------------------
   -- Esiste un record per il periodo selezionato

   SELECT COUNT(*) INTO flEsistePeriodo
   FROM   DUAL
   WHERE  EXISTS
             (SELECT 1
              FROM   REPORT_STATO
              WHERE  cd_cds = inCdCdsOrigine AND
                     cd_unita_organizzativa = inCdUoOrigine AND
                     cd_tipo_sezionale = aCodiceSezionale AND
                     ti_documento = aTipoDocumento AND
                     tipo_report = aTipoReportStato AND
                     dt_inizio = inDataInizio AND
                     dt_fine = inDataFine AND
                     stato IN ('B','C'));

   -------------------------------------------------------------------------------------------------
   -- Esiste un record per il periodo precedente

   SELECT COUNT(*) INTO flEsistePeriodoPrecedente
   FROM   DUAL
   WHERE  EXISTS
             (SELECT 1
              FROM   REPORT_STATO
              WHERE  cd_cds = inCdCdsOrigine AND
                     cd_unita_organizzativa = inCdUoOrigine AND
                     cd_tipo_sezionale = aCodiceSezionale AND
                     ti_documento = aTipoDocumento AND
                     tipo_report = aTipoReportStato AND
                     dt_inizio = aDataInizioPrec AND
                     dt_fine = aDataFinePrec AND
                     stato IN ('B','C'));

   -------------------------------------------------------------------------------------------------
   -- Esiste un record in qualsiasi periodo precedente al precedente

   SELECT COUNT(*) INTO flEsistePeriodoPrima
   FROM   DUAL
   WHERE  EXISTS
             (SELECT 1
              FROM   REPORT_STATO
              WHERE  cd_cds = inCdCdsOrigine AND
                     cd_unita_organizzativa = inCdUoOrigine AND
                     cd_tipo_sezionale = aCodiceSezionale AND
                     ti_documento = aTipoDocumento AND
                     tipo_report = aTipoReportStato AND
                     dt_fine < aDataInizioPrec AND
                     stato IN ('B','C'));

   -------------------------------------------------------------------------------------------------
   -- Esiste un record in qualsiasi periodo successivo al corrente

   SELECT COUNT(*) INTO flEsistePeriodoDopo
   FROM   DUAL
   WHERE  EXISTS
             (SELECT 1
              FROM   REPORT_STATO
              WHERE  cd_cds = inCdCdsOrigine AND
                     cd_unita_organizzativa = inCdUoOrigine AND
                     cd_tipo_sezionale = aCodiceSezionale AND
                     ti_documento = aTipoDocumento AND
                     tipo_report = aTipoReportStato AND
                     dt_inizio > inDataFine AND
                     stato IN ('B','C'));

END getStatoReportStatoSiCdSez;

-- =================================================================================================
-- Controllo entrate presenti in tabella REPORT_STATO senza il parametro aCodiceSezionale
-- =================================================================================================
PROCEDURE getStatoReportStatoNoCdSez
   (
    inCdCdsOrigine VARCHAR2,
    inCdUoOrigine VARCHAR2,
    inDataInizio DATE,
    inDataFine DATE,
    inGruppoReport VARCHAR2,
    aTipoReportStato VARCHAR2,
    flEsistePeriodo IN OUT INTEGER,
    flEsistePeriodoPrecedente IN OUT INTEGER,
    flEsistePeriodoPrima IN OUT INTEGER,
    flEsistePeriodoDopo IN OUT INTEGER
   ) IS
   aTipoDocumento CHAR(1);
   aDataInizioPrec DATE;
   aDataFinePrec DATE;

BEGIN

   aDataInizioPrec:=IBMUTL001.getAddMonth(inDataInizio, -1);
   aDataFinePrec:=IBMUTL001.getAddMonth(inDataFine, -1);
   flEsistePeriodo:=0;
   flEsistePeriodoPrecedente:=0;
   flEsistePeriodoPrima:=0;
   flEsistePeriodoDopo:=0;
   aTipoDocumento:='*';
   IF (aTipoReportStato = TI_RIEPILOGATIVO_ACQUISTI OR
       aTipoReportStato = TI_RIEPILOGATIVO_VENDITE OR
       aTipoReportStato = TI_LIQUIDAZIONE) THEN
      aTipoDocumento:=inGruppoReport;
   END IF;

   -------------------------------------------------------------------------------------------------
   -- Esiste un record per il periodo selezionato
--dbms_output.put_line('uo '||inCdUoOrigine);
--dbms_output.put_line('aTipoDocumento '||aTipoDocumento);
--dbms_output.put_line('aTipoReportStato '||aTipoReportStato);

   SELECT COUNT(*) INTO flEsistePeriodo
   FROM   DUAL
   WHERE  EXISTS
             (SELECT 1
              FROM   REPORT_STATO
              WHERE  cd_cds = inCdCdsOrigine AND
                     cd_unita_organizzativa = inCdUoOrigine AND
                     ti_documento = aTipoDocumento AND
                     tipo_report = aTipoReportStato AND
                     dt_inizio = inDataInizio AND
                     dt_fine = inDataFine AND
                     stato IN ('B','C'));

   -------------------------------------------------------------------------------------------------
   -- Esiste un record per il periodo precedente

   SELECT COUNT(*) INTO flEsistePeriodoPrecedente
   FROM   DUAL
   WHERE  EXISTS
             (SELECT 1
              FROM   REPORT_STATO
              WHERE  cd_cds = inCdCdsOrigine AND
                     cd_unita_organizzativa = inCdUoOrigine AND
                     ti_documento = aTipoDocumento AND
                     tipo_report = aTipoReportStato AND
                     dt_inizio = aDataInizioPrec AND
                     dt_fine = aDataFinePrec AND
                     stato IN ('B','C'));

   -------------------------------------------------------------------------------------------------
   -- Esiste un record in qualsiasi periodo precedente al precedente

   SELECT COUNT(*) INTO flEsistePeriodoPrima
   FROM   DUAL
   WHERE  EXISTS
             (SELECT 1
              FROM   REPORT_STATO
              WHERE  cd_cds = inCdCdsOrigine AND
                     cd_unita_organizzativa = inCdUoOrigine AND
                     ti_documento = aTipoDocumento AND
                     tipo_report = aTipoReportStato AND
                     dt_fine < aDataInizioPrec AND
                     stato IN ('B','C'));

   -------------------------------------------------------------------------------------------------
   -- Esiste un record in qualsiasi periodo successivo al corrente

   SELECT COUNT(*) INTO flEsistePeriodoDopo
   FROM   DUAL
   WHERE  EXISTS
             (SELECT 1
              FROM   REPORT_STATO
              WHERE  cd_cds = inCdCdsOrigine AND
                     cd_unita_organizzativa = inCdUoOrigine AND
                     ti_documento = aTipoDocumento AND
                     tipo_report = aTipoReportStato AND
                     dt_inizio > inDataFine AND
                     stato IN ('B','C'));

END getStatoReportStatoNoCdSez;

-- =================================================================================================
-- Controllo entrate presenti in tabella REPORT_STATO senza il parametro gruppo report
-- =================================================================================================
PROCEDURE getStatoReportStatoNoGruppo
   (
    inCdCdsOrigine VARCHAR2,
    inCdUoOrigine VARCHAR2,
    aCodiceSezionale VARCHAR2,
    inDataInizio DATE,
    inDataFine DATE,
    aTipoReportStato VARCHAR2,
    flEsistePeriodo IN OUT INTEGER,
    flEsistePeriodoPrecedente IN OUT INTEGER,
    flEsistePeriodoPrima IN OUT INTEGER,
    flEsistePeriodoDopo IN OUT INTEGER
   ) IS
   aDataInizioPrec DATE;
   aDataFinePrec DATE;

BEGIN

   aDataInizioPrec:=IBMUTL001.getAddMonth(inDataInizio, -1);
   aDataFinePrec:=IBMUTL001.getAddMonth(inDataFine, -1);
   flEsistePeriodo:=0;
   flEsistePeriodoPrecedente:=0;
   flEsistePeriodoPrima:=0;
   flEsistePeriodoDopo:=0;

   -------------------------------------------------------------------------------------------------
   -- Esiste un record per il periodo selezionato

   SELECT COUNT(*) INTO flEsistePeriodo
   FROM   DUAL
   WHERE  EXISTS
             (SELECT 1
              FROM   REPORT_STATO
              WHERE  cd_cds = inCdCdsOrigine AND
                     cd_unita_organizzativa = inCdUoOrigine AND
                     cd_tipo_sezionale = aCodiceSezionale AND
                     tipo_report = aTipoReportStato AND
                     dt_inizio = inDataInizio AND
                     dt_fine = inDataFine AND
                     stato IN ('B','C'));

   -------------------------------------------------------------------------------------------------
   -- Esiste un record per il periodo precedente

   SELECT COUNT(*) INTO flEsistePeriodoPrecedente
   FROM   DUAL
   WHERE  EXISTS
             (SELECT 1
              FROM   REPORT_STATO
              WHERE  cd_cds = inCdCdsOrigine AND
                     cd_unita_organizzativa = inCdUoOrigine AND
                     cd_tipo_sezionale = aCodiceSezionale AND
                     tipo_report = aTipoReportStato AND
                     dt_inizio = aDataInizioPrec AND
                     dt_fine = aDataFinePrec AND
                     stato IN ('B','C'));

   -------------------------------------------------------------------------------------------------
   -- Esiste un record in qualsiasi periodo precedente al precedente

   SELECT COUNT(*) INTO flEsistePeriodoPrima
   FROM   DUAL
   WHERE  EXISTS
             (SELECT 1
              FROM   REPORT_STATO
              WHERE  cd_cds = inCdCdsOrigine AND
                     cd_unita_organizzativa = inCdUoOrigine AND
                     cd_tipo_sezionale = aCodiceSezionale AND
                     tipo_report = aTipoReportStato AND
                     dt_fine < aDataInizioPrec AND
                     stato IN ('B','C'));

   -------------------------------------------------------------------------------------------------
   -- Esiste un record in qualsiasi periodo successivo al corrente

   SELECT COUNT(*) INTO flEsistePeriodoDopo
   FROM   DUAL
   WHERE  EXISTS
             (SELECT 1
              FROM   REPORT_STATO
              WHERE  cd_cds = inCdCdsOrigine AND
                     cd_unita_organizzativa = inCdUoOrigine AND
                     cd_tipo_sezionale = aCodiceSezionale AND
                     tipo_report = aTipoReportStato AND
                     dt_inizio > inDataFine AND
                     stato IN ('B','C'));

 END getStatoReportStatoNoGruppo;


-- =================================================================================================
-- Inserimento record in REPORT_DETTAGLIO
-- =================================================================================================
PROCEDURE insReportDettaglio
   (
    aRecReportDettaglio REPORT_DETTAGLIO%ROWTYPE
   ) IS

BEGIN

   INSERT INTO REPORT_DETTAGLIO
          (cd_cds,
           cd_unita_organizzativa,
           esercizio,
           tipo_report,
           cd_tipo_sezionale,
           data_inizio,
           data_fine,
           ti_documento,
           pg_documento,
           cd_cds_altro,
           cd_uo_altro,
           dacr,
           utcr,
           duva,
           utuv,
           pg_ver_rec)
   VALUES (aRecReportDettaglio.cd_cds,
           aRecReportDettaglio.cd_unita_organizzativa,
           aRecReportDettaglio.esercizio,
           aRecReportDettaglio.tipo_report,
           aRecReportDettaglio.cd_tipo_sezionale,
           aRecReportDettaglio.data_inizio,
           aRecReportDettaglio.data_fine,
           aRecReportDettaglio.ti_documento,
           aRecReportDettaglio.pg_documento,
           aRecReportDettaglio.cd_cds_altro,
           aRecReportDettaglio.cd_uo_altro,
           aRecReportDettaglio.dacr,
           aRecReportDettaglio.utcr,
           aRecReportDettaglio.duva,
           aRecReportDettaglio.utuv,
           aRecReportDettaglio.pg_ver_rec);

EXCEPTION

   WHEN DUP_VAL_ON_INDEX THEN
        IBMERR001.RAISE_ERR_GENERICO
           ('Chiave duplicata in inserimento dettaglio fatture su tabella REPORT_DETTAGLIO');

END insReportDettaglio;

-- =================================================================================================
-- Inserimento riga nella tabella REPORT STATO
-- =================================================================================================
PROCEDURE inserisciInReportStato
   (
    inCdCdsOrigine VARCHAR2,
    inCdUoOrigine VARCHAR2,
    inEsercizio NUMBER,
    aCodiceSezionale VARCHAR2,
    inDataInizio DATE,
    inDataFine DATE,
    inTipoRegistro VARCHAR2,
    inGruppoReport VARCHAR2,
    aTipoReportStato VARCHAR2,
    idUtente VARCHAR2
   ) IS
   aTipoDocumento CHAR(1);

BEGIN

   aTipoDocumento:='*';
   IF (aTipoReportStato = TI_RIEPILOGATIVO_ACQUISTI OR
       aTipoReportStato = TI_RIEPILOGATIVO_VENDITE OR
       aTipoReportStato = TI_LIQUIDAZIONE) THEN
      aTipoDocumento:=inGruppoReport;
   END IF;

   -- insert in REPORT_STATO

   INSERT INTO REPORT_STATO
          (cd_cds,
           cd_unita_organizzativa,
           esercizio,
           cd_tipo_Sezionale,
           ti_documento,
           tipo_report,
           dt_inizio,
           dt_fine,
           stato,
           dacr,
           duva,
           utcr,
           utuv,
           pg_ver_rec)
   VALUES (inCdCdsOrigine,
           inCdUoOrigine,
           inEsercizio,
           aCodiceSezionale,
           aTipoDocumento,
           aTipoReportStato,
           inDataInizio,
           inDataFine,
           'B',
           sysdate,
           sysdate,
           idUtente,
           idUtente,
           1);

EXCEPTION

   WHEN DUP_VAL_ON_INDEX THEN
        IBMERR001.RAISE_ERR_GENERICO
           ('Chiave duplicata in inserimento in tabella REPORT_STATO. ' ||
            'La stampa ' || aTipoReportStato || ' risulta gi? presente in stato definitivo');

END inserisciInReportStato;

-- =================================================================================================
-- Valorizza la data di inizio stampa registro IVA o di liquidazione di una fattura
-- =================================================================================================
PROCEDURE getIsFatturaElaborataIva
   (
    aCdCdsOrigine VARCHAR2,
    aCdUoOrigine VARCHAR2,
    aEsercizio NUMBER,
    aPgFattura NUMBER,
    aPgRiga NUMBER,
    aTipoFattura VARCHAR2,
    aCdCds VARCHAR2,
    aCdUo VARCHAR2,
    aDtInizioStmRegistro IN OUT DATE,
    aDtInizioStmLiquidazione IN OUT DATE
   ) IS
   i BINARY_INTEGER;
   aTipoReport VARCHAR2(50);
   aRecReportDettaglio REPORT_DETTAGLIO%ROWTYPE;

BEGIN

   FOR i IN 1 .. 3

   LOOP

      IF    i = 1 THEN
            aTipoReport:=TI_REGISTRO_VENDITE;
      ELSIF i = 2 THEN
            aTipoReport:=TI_LIQUIDAZIONE_VEN_ESIGIB;
      ELSIF i = 3 THEN
            aTipoReport:=TI_LIQUIDAZIONE_ACQ_ESIGIB;
      END IF;

      BEGIN

         SELECT * INTO aRecReportDettaglio
         FROM   REPORT_DETTAGLIO
         WHERE  cd_cds = aCdCdsOrigine AND
                cd_unita_organizzativa = aCdUoOrigine AND
                esercizio = aEsercizio AND
                tipo_report = aTipoReport And
                (aTipoReport =TI_REGISTRO_VENDITE Or
                pg_riga_documento =aPgRiga ) And
                ti_documento = aTipoFattura AND
                pg_documento = aPgFattura AND
                cd_cds_altro = aCdCds AND
                cd_uo_altro = aCdUo;

      EXCEPTION

         WHEN no_data_found THEN
              aRecReportDettaglio:=NULL;

      END;

      IF    i = 1 THEN
            aDtInizioStmRegistro:=aRecReportDettaglio.data_inizio;
      ELSIF i = 2 THEN
            aDtInizioStmLiquidazione:=aRecReportDettaglio.data_inizio;
       ELSIF i = 3 THEN
            aDtInizioStmLiquidazione:=aRecReportDettaglio.data_inizio;
      END IF;

   END LOOP;

END getIsFatturaElaborataIva;

-- =================================================================================================
-- Valorizza la data di inizio stampa registro IVA o di liquidazione di un sezionale per una data UO
-- =================================================================================================
PROCEDURE getIsSezionaleElaborataIva
   (
    aCdCds VARCHAR2,
    aCdUo VARCHAR2,
    aCdSezionale VARCHAR2,
    aDtInizio DATE,
    aDtFine DATE,
    aTipoDocumentoReportStato VARCHAR2,
    aDtInizioStmRegistro IN OUT DATE,
    aDtInizioStmLiquidazione IN OUT DATE
   ) IS
   i BINARY_INTEGER;
   aTipoReport VARCHAR2(50);
   aTipoDocumento CHAR(1);
   aTipoSezionale VARCHAR2(50);
   aRecReportStato REPORT_STATO%ROWTYPE;

BEGIN

   FOR i IN 1 .. 2

   LOOP

      IF    i = 1 THEN
            aTipoReport:=TI_REGISTRO_VENDITE;
            aTipoDocumento:='*';
            aTipoSezionale:=aCdSezionale;
      ELSIF i = 2 THEN
            aTipoReport:=TI_LIQUIDAZIONE;
            aTipoDocumento:=aTipoDocumentoReportStato;
            aTipoSezionale:='*';
      END IF;

      BEGIN

         SELECT * INTO aRecReportStato
         FROM   REPORT_STATO
         WHERE  cd_cds = aCdCds AND
                cd_unita_organizzativa = aCdUo AND
                cd_tipo_sezionale = aTipoSezionale AND
                ti_documento = aTipoDocumento AND
                tipo_report = aTipoReport AND
                dt_inizio = aDtInizio AND
                dt_fine = aDtFine;

      EXCEPTION

         WHEN no_data_found THEN
              aRecReportStato:=NULL;

      END;

      IF    i = 1 THEN
            aDtInizioStmRegistro:=aRecReportStato.dt_inizio;
      ELSIF i = 2 THEN
            aDtInizioStmLiquidazione:=aRecReportStato.dt_inizio;
      END IF;

   END LOOP;

END getIsSezionaleElaborataIva;

-- =================================================================================================
-- Ritorna un record della REPORT_STATO
-- =================================================================================================
FUNCTION getReportStato
   (
    aCdCds VARCHAR2,
    aCdUo VARCHAR2,
    aCdSezionale VARCHAR2,
    aTiDocumento VARCHAR2,
    aTipoReport VARCHAR2,
    aDtInizio DATE,
    aDtFine DATE,
    aStato VARCHAR2
   ) RETURN REPORT_STATO%ROWTYPE IS
   aRecReportStato REPORT_STATO%ROWTYPE;

BEGIN

   IF aStato IS NULL THEN

      SELECT * INTO aRecReportStato
      FROM   REPORT_STATO
      WHERE  cd_cds = aCdCds AND
             cd_unita_organizzativa = aCdUo AND
             cd_tipo_sezionale = aCdSezionale AND
             ti_documento = aTiDocumento AND
             tipo_report = aTipoReport AND
             dt_inizio = aDtInizio AND
             dt_fine = aDtFine
             FOR UPDATE NOWAIT;

   ELSE

      SELECT * INTO aRecReportStato
      FROM   REPORT_STATO
      WHERE  cd_cds = aCdCds AND
             cd_unita_organizzativa = aCdUo AND
             cd_tipo_sezionale = aCdSezionale AND
             ti_documento = aTiDocumento AND
             tipo_report = aTipoReport AND
             dt_inizio = aDtInizio AND
             dt_fine = aDtFine AND
             stato = aStato
             FOR UPDATE NOWAIT;

   END IF;

   RETURN aRecReportStato;

EXCEPTION

   WHEN NO_DATA_FOUND THEN
        IBMERR001.RAISE_ERR_GENERICO
           ('Non trovato alcun riferimento in tabella REPORT_STATO per il sezionale ' || aCdSezionale ||
            ' UO ' || aCdUo || ' tipo_report ' || aTipoReport);

END getReportStato;

-- =================================================================================================
-- Restituisce il valore minimo dei protocolli iva delle fatture presenti in REPORT_DETTAGLIO
-- dato un record di REPORT_STATO
-- =================================================================================================
PROCEDURE getMinProtocolloIva
   (
    aRecReportStato REPORT_STATO%ROWTYPE,
    aTipoReportStato VARCHAR2,
    aProtocolloFT IN OUT NUMBER,
    aProtocolloNC IN OUT NUMBER,
    aProtocolloND IN OUT NUMBER,
    aProtocolloPG IN OUT NUMBER,
    eseguiUpgSezionale IN OUT CHAR
   ) IS

   aTipoDocumentoAmm VARCHAR2(10);
   cv_ti_fattura CHAR(1);
   cv_protocollo_iva NUMBER(10);
   cv_protocollo_iva_generale NUMBER(10);
   aProtocolloGenerale NUMBER(10);

   gen_cv GenericCurTyp;

BEGIN

   aProtocolloGenerale:=0;

   -------------------------------------------------------------------------------------------------
   -- Lettura del tipo documento portato dal dettaglio

   BEGIN

      SELECT DISTINCT ti_documento INTO aTipoDocumentoAmm
      FROM   REPORT_DETTAGLIO
      WHERE  cd_cds = aRecReportStato.cd_cds AND
             cd_unita_organizzativa = aRecReportStato.cd_unita_organizzativa AND
             esercizio = aRecReportStato.esercizio AND
             tipo_report = aRecReportStato.tipo_report AND
             cd_tipo_Sezionale = aRecReportStato.cd_tipo_Sezionale AND
             data_inizio = aRecReportStato.dt_inizio AND
             data_fine = aRecReportStato.dt_fine;

   EXCEPTION

      WHEN no_data_found THEN
           RETURN;

   END;

   -- Uscita dalla routine se elaboro fatture attive

   IF aTipoDocumentoAmm = CNRCTB100.TI_FATTURA_ATTIVA THEN
      RETURN;
   END IF;

   -- Attivo il flag di aggiornamento dei numeratori sul sezionale

   eseguiUpgSezionale:='Y';

   -------------------------------------------------------------------------------------------------
   -- Lettura record della REPORT_DETTAGLIO

   BEGIN

      IF aTipoDocumentoAmm = CNRCTB100.TI_FATTURA_PASSIVA THEN

         OPEN gen_cv FOR

              SELECT B.ti_fattura, MIN(B.protocollo_iva), MIN(B.protocollo_iva_generale)
              FROM   REPORT_DETTAGLIO A, FATTURA_PASSIVA B
              WHERE  A.cd_cds = aRecReportStato.cd_cds AND
                     A.cd_unita_organizzativa = aRecReportStato.cd_unita_organizzativa AND
                     A.esercizio = aRecReportStato.esercizio AND
                     A.tipo_report = aTipoReportStato AND
                     A.cd_tipo_sezionale =  aRecReportStato.cd_tipo_sezionale AND
	             A.data_inizio = aRecReportStato.dt_inizio AND
                     A.data_fine = aRecReportStato.dt_fine AND
                     B.cd_cds = A.cd_cds_altro AND
                     B.cd_unita_organizzativa = A.cd_uo_altro AND
                     B.esercizio = A.esercizio AND
                     B.pg_fattura_passiva = A.pg_documento
              GROUP BY B.ti_fattura;

      ELSE

         OPEN gen_cv FOR

              SELECT B.ti_fattura, MIN(B.protocollo_iva), MIN(B.protocollo_iva_generale)
              FROM   REPORT_DETTAGLIO A, AUTOFATTURA B
              WHERE  A.cd_cds = aRecReportStato.cd_cds AND
                     A.cd_unita_organizzativa = aRecReportStato.cd_unita_organizzativa AND
                     A.esercizio = aRecReportStato.esercizio AND
                     A.tipo_report = aTipoReportStato AND
                     A.cd_tipo_sezionale =  aRecReportStato.cd_tipo_sezionale AND
	             A.data_inizio = aRecReportStato.dt_inizio AND
                     A.data_fine = aRecReportStato.dt_fine AND
                     B.cd_cds = A.cd_cds_altro AND
                     B.cd_unita_organizzativa = A.cd_uo_altro AND
                     B.esercizio = A.esercizio AND
                     B.pg_autofattura = A.pg_documento
              GROUP BY B.ti_fattura;

      END IF;

      LOOP

         FETCH gen_cv INTO
               cv_ti_fattura,
               cv_protocollo_iva,
               cv_protocollo_iva_generale;

         EXIT WHEN gen_cv%NOTFOUND;

         IF    cv_ti_fattura = CNRCTB100.TI_FATT_FATTURA THEN
               aProtocolloFT:=cv_protocollo_iva;
         ELSIF cv_ti_fattura = CNRCTB100.TI_FATT_NOTA_C THEN
               aProtocolloNC:=cv_protocollo_iva;
         ELSIF cv_ti_fattura = CNRCTB100.TI_FATT_NOTA_D THEN
               aProtocolloND:=cv_protocollo_iva;
         END IF;

         IF (aProtocolloGenerale = 0 OR
             aProtocolloGenerale > cv_protocollo_iva_generale) THEN
            aProtocolloGenerale:=cv_protocollo_iva_generale;
         END IF;

      END LOOP;

      aProtocolloPG:= aProtocolloGenerale;

   END;

END getMinProtocolloIva;

-- =================================================================================================
-- Aggiorna a C tutti i record di REPORT_STATO per stampa registri IVA
-- =================================================================================================
PROCEDURE upgStatoRegistriIva
   (
    aEsercizio NUMBER,
    aDataInizio DATE,
    aDataFine DATE,
    aUtente VARCHAR2
   ) IS

BEGIN

   UPDATE REPORT_STATO
   SET    stato = 'C',
          utuv = aUtente,
          duva = SYSDATE,
          pg_ver_rec = pg_ver_rec +1
   WHERE  esercizio = aEsercizio AND
          tipo_report = TI_REGISTRO_ACQUISTI AND
          dt_inizio = aDataInizio AND
          dt_fine = aDataFine AND
          stato = 'B';

END upgStatoRegistriIva;

END CNRCTB255;


