--------------------------------------------------------
--  DDL for Package CNRCTB255
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRCTB255" AS
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
-- Inserita la gestione differenziata della LIQUIDAZIONE_IVA per attività commerciali ed istituzionali
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
-- delle chiavi delle fatture ad esigibilità differita divenute esigibili che siano state incluse in
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
-- La chiamata è fatta sempre con periodo dicembre esercizio ed esercizio = esercizio + 1.
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
