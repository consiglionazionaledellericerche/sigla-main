--------------------------------------------------------
--  DDL for Package CNRCTB150
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRCTB150" AS
-- =================================================================================================
--
-- CNRCTB150 - Package di gestione protocollazione - stampa delle fatture
--
-- Date: 15/09/2003
-- Version: 1.17
--
-- Dependency: CNRCTB 100/120/255 IBMERR 001 IBMUTL 001
--
-- History:
--
-- Date: 14/01/2002
-- Version: 1.0
-- Creazione
--
-- Date: 15/01/2002
-- Version: 1.1
-- Modifica procedura per la gestione della tabella RIF_PROTOCOLLO_IVA
--
-- Date: 17/01/2002
-- Version: 1.2
-- Modifica struttura del pachetto
--
-- Date: 17/01/2002
-- Version: 1.3
-- Modifica della gestione degli errori
--
-- Date: 17/01/2002
-- Version: 1.4
-- Introduzione della gestione dei Sezionali
--
-- Date: 17/01/2002
-- Version: 1.5
-- Modifica della modalita per il trattamento degli errori
--
-- Date: 18/01/2002
-- Version: 1.6
-- Inversione del controlli : Prioma chkparametri poi chkrif_protocollo_iva
--
-- Date: 11/02/2002
-- Version: 1.7
-- Modifica dei campi per il join tra la tabella FATTURA_XXX (PASSIVA, ATTIVA) e SEZIONALE
--
-- Date: 25/02/2002
-- Version: 1.8
-- Eliminazione gestione protocollazione per fatture passive.
--
-- Date: 25/02/2002
-- Version: 1.9
-- Correzione JOIN sulla tipologia fattura della tabella sezionale.
--
-- Date: 05/03/2002
-- Version: 1.10
-- Correzione UPDATE della fattura per la gestione del PG_VER_REC e della DUVA
--
-- Date: 24/05/2002
-- Version: 1.11
-- AGGIORNAMENTO DOCUMENTI CONTABILI TRAMITE IL PACKAGE CNRCTB100
--
-- Date: 24/05/2002
-- Version: 1.12
-- Nuova gestione delle tabelle di rifeimento rif_protocollo_iva, tramite la vista
-- vsx_rif_protocollo_iva.
--
-- Date: 15/07/2002
-- Version: 1.13
-- Fix per l'estrazione corretta delle fatture da protocollare, aggiunta (+)
-- Modificata gestione dell'errore per la mancanza del sezionala relativo al protocollo iva generale
--
-- Date: 16/07/2002
-- Version: 1.14
-- Fix per l'estrazione corretta delle fatture da protocollare, eliminata (+)
--
-- Date: 26/08/2002
-- Version: 1.15
-- Inserimento controllo sulla data di stampa associata alle fatture,
-- per ogni fattura da protocollare, viene calcolato il mese relativo alla data di stampa
-- e viene ricercato un corrispondente registro in stato definitivo.
--
-- Date: 06/03/2003
-- Version: 1.16
-- Fix errore CINECA 521. Rifacimento della procedura di protocollazione. Errato calcolo nella
-- sequenza dei protocolli a rottura di tipo_fattura e sezionale
--
-- Date: 15/09/2003
-- Version: 1.17
--
-- Attivazione gestione liquidazione IVA del mese di dicembre da eseguirsi sempre in esercizio
-- successivo. (chiusure).
-- La chiamata è fatta sempre con periodo dicembre esercizio ed esercizio = esercizio + 1.
--
-- =================================================================================================
--
-- Constants:
--
   errMsg VARCHAR2(200);

-- Variabili Globali Cursore generico

   TYPE GenericCurTyp IS REF CURSOR;

-- Functions e Procedures:

----------------------------------------------------------------------------------------------------
-- Main PROCEDURE
----------------------------------------------------------------------------------------------------

-- Procedura che si occupa di valorizzare il protocollo IVA delle fatture Attive in stampa

   PROCEDURE vsx_protocollazione_doc
      (
       aPgCall VSX_RIF_PROTOCOLLO_IVA.PG_CALL%TYPE
      );

----------------------------------------------------------------------------------------------------
-- FUNZIONI e PROCEDURE di servizio
----------------------------------------------------------------------------------------------------
-- Funzione che restituisce
--    1 se il tipo di documento e valido
--    0 se il tipo di documento non e valido

   FUNCTION chkTipoDocumento
      (
       aPgCall vsx_rif_protocollo_iva.PG_CALL%TYPE
      ) RETURN VARCHAR2;

-- Funzione che restituisce
--    1 se la tabella VSX_RIF_PROTOCOLLO_IVA contiene almeno una riga per la sessione utente in esame
--    0 se la tabella VSX_RIF_PROTOCOLLO_IVA contiene 0 righe per la sessione utente in esame

   FUNCTION chkRif_Protocollo_Iva
      (
       aPgCall VSX_RIF_PROTOCOLLO_IVA.PG_CALL%TYPE,
       p_tipo_documento_amm VSX_RIF_PROTOCOLLO_IVA.tipo_documento_amm%TYPE
      ) RETURN NUMBER;

-- Lock dei documenti amministrativi oggetto di stampa

   PROCEDURE LOCK_TABELLE
      (
       aPgCall VSX_RIF_PROTOCOLLO_IVA.PG_CALL%TYPE,
       p_tipo_documento_amm VSX_RIF_PROTOCOLLO_IVA.tipo_documento_amm%TYPE
      );

-- Ritorna il cds e la uo di orgine delle fatture in stampa e la data di stampa e l'utente definiti in
-- VSX_RIF_PROTOCOLLO_IVA

   PROCEDURE getDatiBaseProtocolla
      (
       aPgCall VSX_RIF_PROTOCOLLO_IVA.PG_CALL%TYPE,
       aCdCdsOrigine IN OUT VARCHAR2,
       aCdUoOrigine IN OUT VARCHAR2,
       aEsercizio IN OUT NUMBER,
       aPgFattura IN OUT NUMBER,
       aDataStampa IN OUT DATE,
       aUtente IN OUT VARCHAR2
      );

END CNRCTB150;
