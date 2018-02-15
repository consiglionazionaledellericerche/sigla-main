--------------------------------------------------------
--  DDL for Package CNRCTB265
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRCTB265" AS
--==================================================================================================
--
-- Cnrctb265 - Package di gestione delle procedure finalizzate al calcolo e alla stampa della liquidazione IVA,
-- usate nel package CNRCTB250
--
-- Date: 19/07/2006
-- Version: 4.7
--
-- Dependency: CNRCTB 100/120/255 IBMERR 001
--
-- History:
--
-- Date: 10/02/2003
-- Version: 4.0
--
-- Completato inserimento gestione del riepilogativo per sezionale del centro
--
-- Date: 24/02/2003
-- Version: 4.1
--
-- Importata gestione per annullamento e conferma registro. Fix errori CINECA n. 492 e 497. Corrette
-- gestioni di conferma ed annullamento stampa registri svolte dal centro. Errore del client che non
-- eseguiva il commit e aggiunti controlli sull'ammissibilit�ell'operazione oltre all'azzeramento
-- dei progressivi iva (protocolli).
-- Richiesta CINECA n. 486. Modifica layout della stampa del riepilogativo CENTRO. Inserito il recupero
-- dell'informazione della ragione sociale e partita IVA dell'ente da esporre in intestazione per tutti
-- i report IVA
--
-- Date: 26/02/2003
-- Version: 4.2
--
-- Fix errore interno, non valorizzava correttamente l'aggiornamento del protocollo iva generale sul
-- sezionale in caso di annullamento di un registro.
--
-- Date: 27/02/2003
-- Version: 4.3
--
-- Inserita gestione della liquidazione di massa al centro per le singole UO.
--
-- Date: 03/04/2003
-- Version: 4.4
--
-- Fix errore chiamata a lockdocamm e updatedocamm in annullamento registro IVA
--
-- Date: 15/09/2003
-- Version: 4.5
--
-- Attivazione gestione liquidazione IVA del mese di dicembre da eseguirsi sempre in esercizio
-- successivo. (chiusure).
-- La chiamata �atta sempre con periodo dicembre esercizio ed esercizio = esercizio + 1.
--
-- Date: 24/08/2005
-- Version: 4.6
--
-- Date: 19/07/2006
-- Version: 4.7
-- Gestione Impegni/Accertamenti Residui:
-- gestito il nuovo campo ESERCIZIO_ORIGINALE
--
-- Nella stampa 'Riepilogativi IVA Ente' inizialmente le fatture estratte erano quelle emesse nel periodo
-- di riferimento + quelle differite, emesse in periodi precedenti, divenute esigibili nel periodo
-- di riferimento. La modifica fatta consiste nell'eliminare dalla stampa le seconde.
--==================================================================================================
--
--
-- Constants:

   -- Variabili globali

   -- variabili recupero fetch e definizione cursore variabile

   cv_cd_tipo_sezionale VARCHAR2(10);
   cv_gruppo_iva VARCHAR2(50);
   cv_descrizione_gruppo_iva VARCHAR2(200);
   cv_codice_iva VARCHAR2(10);
   cv_descrizione_iva VARCHAR2(200);
   cv_esigibilita_diff CHAR(1);
   cv_data_esigibilita_diff DATE;
   cv_imponibile NUMBER(15,2);
   cv_iva NUMBER(15,2);
   cv_iva_indetraibile NUMBER(15,2);
   cv_iva_esigibile NUMBER(15,2);
   cv_totale NUMBER(15,2);
   cv_acquisti_vendite CHAR(1);
   cv_imponibile_split_payment NUMBER(15,2);
   cv_iva_split_payment NUMBER(15,2);

   TYPE GenericCurTyp IS REF CURSOR;

-- Functions e Procedures:

-- Stampa RIEPILOGATIVO CENTRO con dettaglio per sezionale

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
      );

-- Inserimento record in VP_STM_RIEPILOGATIVO_CENTRO

   PROCEDURE insVpStmRiepilogoCentro
      (
       aRecVpStmRiepilogoCentro VP_STM_RIEPILOGATIVO_CENTRO%ROWTYPE
      );

-- Stampa del registro riepilogativo senza dettaglio per sezionale. Si esegue solo se risultano
-- elaborati pi� di un sezionale

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
      );

-- Conferma ed annullamento stampa registri IVA dal centro

   PROCEDURE confermaAnnullaRegistro
      (
       aCdCds VARCHAR2,
       aCdUo VARCHAR2,
       aEsercizio NUMBER,
       aCodiceSezionale VARCHAR2,
       aDataInizio DATE,
       aDataFine DATE,
       aUtente VARCHAR2,
       aTipoAzione CHAR
      );

-- Inserimento di una liquidazione

   PROCEDURE insLiquidazioneIva
      (
       aRecLiquidazioneIva LIQUIDAZIONE_IVA%ROWTYPE
      );


END CNRCTB265;
