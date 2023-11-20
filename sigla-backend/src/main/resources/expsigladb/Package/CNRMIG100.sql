--------------------------------------------------------
--  DDL for Package CNRMIG100
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRMIG100" as
--
-- CNRMIG100 - Package di gestione ribaltamento configurazione, struttura organizzativa,
-- anagrafica dei capitoli e piano dei conti economico/patrimoniale
--
-- Date: 02/03/2004
-- Version: 1.14
--
--
-- Dependency: IBMUTL 001 200, CNRCTB 001 015 018
--
-- History:
--
-- Date: 08/05/2003
-- Version: 1.0
-- Creazione
--
-- Date: 04/06/2003
-- Version: 1.1
-- Aggiunto ribaltamento ASS_PARTITA_GIRO in ribaltaStruttOrg()
--
-- Date: 13/06/2003
-- Version: 1.2
-- Ribaltamento record in LIQUID_CORI
--
-- Date: 18/06/2003
-- Version: 1.3
-- Aggiornamento dei numeratori dei documenti contabili
-- in NUMERAZIONE_DOC_CONT
--
-- Date: 08/07/2003
-- Version: 1.4
-- Ribaltamento ASS_ANAG_VOCE_EP
--
-- Date: 10/07/2003
-- Version: 1.5
-- Fix ordinamento ribaltamento VOCE_EP
--
-- Date: 14/07/2003
-- Version: 1.6
-- Ribaltamento LUNGHEZZA_CHIAVI
--
-- Date: 16/07/2003
-- Version: 1.7
-- Revisione processo di log
-- Ribaltamento CAUSALE_COGE
-- Verifica esistenza esercizio base o elemento voce sul nuovo esercizio
--
-- Date: 21/07/2003
-- Version: 1.8
-- Commento su LIQUID_CORI per adeguamento base dati successivo a
-- lancio ribaltamento
--
-- Date: 29/07/2003
-- Version: 1.9
-- Tolto commento su insert liquid_cori per rilascio evolutivo
--
-- Date: 11/09/2003
-- Version: 1.10
-- Fix aggiornamento numeratori doc cont per l'ente
--
-- Date: 22/09/2003
-- Version: 1.11
-- Inserito logging in creazione esercizi contabili per cds
--
-- Date: 03/12/2003
-- Version: 1.12
-- Aggiunto ribaltamento ASS_TIPO_AMM_CAT_GRUP_INV
--
-- Date: 23/01/2004
-- Version: 1.13
-- Fix ribaltamento sezionali, aggiunto ribaltamento di
-- EXT_CASSIERE_CDS, LIQUID_CORI_INTERF_CDS, LIQUID_IVA_INTERF_CDS,
-- STIPENDI_COFI_CORI_REG (segnalazione errore n. 733)
--
-- Date: 02/03/2004
-- Version: 1.14
-- Ribaltamento CATEGORIA_GRUPPO_VOCE_EP
-- (segnalazione errore n. 785)
--
-- Date: 14/12/2004
-- Version: 1.15
-- Aggiunto Ribaltamento archivi: PARAMETRI_CNR, CLASSIFICAZIONE_ENTRATE,
--				  CLASSIFICAZIONE_SPESE, PARAMETRI_CDS
--
-- Date: 24/10/2005
-- Version: 1.16
-- Aggiunto Ribaltamento archivi: PARAMETRI_LIVELLI, CLASSIFICAZIONE_VOCI,
--				  FIRME
--
-- Date: 08/11/2006
-- Version: 1.17
-- Adeguate le tabelle interessate al ribaltamento con l'aggiunta dei nuovi campi
--
-- Date: 23/10/2008
-- Version: 1.18
-- Sdoppiati i Ribaltamenti (Per il PDGP e Altro)
-- Constants:
--
-- Descrizione utente
cgUtente constant varchar2(20) := '$$$$RIBALTAMENTO$$$$';
-- Descrizione processo
dsProcesso_pdgp constant varchar2(300) := 'Ribaltamento configurazione, str.organizzativa, anagrafica capitoli, PdC EP per PDGP';
dsProcesso_altro constant varchar2(300) := 'Ribaltamento configurazione, str.organizzativa, anagrafica capitoli, PdC EP';
--
-- Tipo Log
TI_LOG_RIBALTAMENTO_PDGP CONSTANT VARCHAR2(20) := 'RIB_CONF_ES00';
TI_LOG_RIBALTAMENTO_ALTRO CONSTANT VARCHAR2(20) := 'RIB_CONF_ES01';
--
-- Functions e Procedures:
--
-- Procedura di lancio del processo di ribaltamento per il PDGP
-- aEs: esercizio di destinazione del ribaltamento
-- aMessage: messaggio stato conclusivo del processo
procedure init_ribaltamento_pdgp(aEs number, aMessage in out varchar2);
-- Procedura di lancio del processo di ribaltamento
-- aEs: esercizio di destinazione del ribaltamento
-- aMessage: messaggio stato conclusivo del processo
procedure init_ribaltamento_altro(aEs number, aMessage in out varchar2);
--
-- Procedura di ribaltamento dell'anagrafica dei capitoli e del piano
-- dei conti finanziario
-- aEsDest: esercizio di destinazione del ribaltamento
-- aEsOrig: esercizio di origine del ribaltamento
-- aPgEsec: progressivo di esecuzione del batch (per logging)
-- aStato: stato di esecuzione del ribaltamento
-- aMessage: messaggio per log
procedure ribaltaEV(aEsDest number, aEsOrig number, aPgEsec number, aStato in out char, aMessage in out varchar2);
--
-- Procedura di ribaltamento della struttura organizzativa
-- aEsDest: esercizio di destinazione del ribaltamento
-- aEsOrig: esercizio di origine del ribaltamento
-- aPgEsec: progressivo di esecuzione del batch (per logging)
-- aStato: stato di esecuzione del ribaltamento
-- aMessage: messaggio per log
procedure ribaltaStruttOrg(aEsDest number, aEsOrig number, aPgEsec number, aStato in out char, aMessage in out varchar2);
--
-- Procedura di ribaltamento dei limiti di spesa
-- aEsDest: esercizio di destinazione del ribaltamento
-- aEsOrig: esercizio di origine del ribaltamento
-- aPgEsec: progressivo di esecuzione del batch (per logging)
-- aStato: stato di esecuzione del ribaltamento
-- aMessage: messaggio per log
procedure ribaltaLimitiSpesa(aEsDest number, aEsOrig number, aPgEsec number, aStato in out char, aMessage in out varchar2);
--
-- Procedura di ribaltamento del piano dei conti E/P
-- aEsDest: esercizio di destinazione del ribaltamento
-- aEsOrig: esercizio di origine del ribaltamento
-- aPgEsec: progressivo di esecuzione del batch (per logging)
-- aStato: stato di esecuzione del ribaltamento
-- aMessage: messaggio per log
procedure ribaltaEP(aEsDest number, aEsOrig number, aPgEsec number, aStato in out char, aMessage in out varchar2);
--
-- Procedura di ribaltamento della configurazione CORI per il PDGP
-- aEsDest: esercizio di destinazione del ribaltamento
-- aEsOrig: esercizio di origine del ribaltamento
-- aPgEsec: progressivo di esecuzione del batch (per logging)
-- aStato: stato di esecuzione del ribaltamento
-- aMessage: messaggio per log
procedure ribaltaCORI_pdgp(aEsDest number, aEsOrig number, aPgEsec number, aStato in out char, aMessage in out varchar2);
-- Procedura di ribaltamento della configurazione CORI
-- aEsDest: esercizio di destinazione del ribaltamento
-- aEsOrig: esercizio di origine del ribaltamento
-- aPgEsec: progressivo di esecuzione del batch (per logging)
-- aStato: stato di esecuzione del ribaltamento
-- aMessage: messaggio per log
procedure ribaltaCORI_altro(aEsDest number, aEsOrig number, aPgEsec number, aStato in out char, aMessage in out varchar2);
--
--
procedure ribaltaINTRASTAT(aEsDest number, aEsOrig number, aPgEsec number, aStato in out char, aMessage in out varchar2);
--
-- Verifica se lo script sia già stato lanciato con successo
function isRibaltamentoPDGPEffettuato (aEs number, aPgEsec number) return boolean;
function isRibaltamentoAltroEffettuato (aEs number, aPgEsec number) return boolean;
--
-- Inserisce la riga di log per il processo di ribaltamento in RIBALTAMENTO_LOG
procedure startLogRibaltamento(aEs number, aPgEsec number, aDsProcesso varchar2, aUtcr varchar2);
--
-- Aggiorna lo stato conclusivo del ribaltamento in RIBALTAMENTO_LOG
procedure endLogRibaltamentoPerPDGP(aEs number, aPgEsec number, aStato in out char, aMessage in out varchar2);
procedure endLogRibaltamentoAltro(aEs number, aPgEsec number, aStato in out char, aMessage in out varchar2);
--
-- Insert in RIBALTAMENTO_LOG
procedure ins_RIBALTAMENTO_LOG (aDest RIBALTAMENTO_LOG%rowtype);
--
procedure JOB_RIBALTAMENTO_PDGP(job number, pg_exec number, next_date date, aEs number);
--
procedure JOB_RIBALTAMENTO_ALTRO_PDGP(job number, pg_exec number, next_date date, aEs number);
end;
