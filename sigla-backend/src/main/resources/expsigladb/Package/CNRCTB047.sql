--------------------------------------------------------
--  DDL for Package CNRCTB047
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRCTB047" as
--
-- CNRCTB047 - Package di gestione chiusura contabile e ribaltamento interesercizio
-- Date: 13/07/2006
-- Version: 1.16
--
-- Dependency: IBMUTL 001 205 210, IBMERR 001, CNRCTB 035 046
--
-- History:
--
-- Date: 03/06/2003
-- Version: 1.0
-- Creazione
-- (ribaltamento non completato)
--
-- Date: 11/06/2003
-- Version: 1.1
-- Completamento del ribaltamento
--
-- Date: 23/06/2003
-- Version: 1.2
-- Aggiunta interfaccia riporto su anno precedente
-- Riporto per OBB_PGIRO, riporto accertamenti, e ACC_PGIRO
--
-- Date: 30/06/2003
-- Version: 1.3
-- Introduzione savepoint
--
-- Date: 01/07/2003
-- Version: 1.4
-- Introduzione funzioni di riporto all'anno successivo con cambio di capitolo
--
-- Date: 08/07/2003
-- Version: 1.5
-- Deriporto automatico di doc cont riportati prima dell'annullamento
--
-- Date: 14/07/2003
-- Version: 1.6
-- Fix esercizio capitolo
--
-- Date: 15/07/2003
-- Version: 1.7
-- Introduzione log riassuntivo
--
-- Date: 23/07/2003
-- Version: 1.8
-- Log importo sul singolo documento riportato
--
-- Date: 01/08/2003
-- Version: 1.9
-- Controlli stato dell'esercizio
--
-- Date: 06/08/2003
-- Version: 1.10
-- Aggiunta documentazione: pre-post condizioni
--
-- Date: 04/09/2003
-- Version: 1.11
-- Fix posizionamento savepoint e commit
--
-- Date: 10/09/2003
-- Version: 1.12
-- Aggiunta procedura per ribaltamento massivo
--
-- Date: 10/09/2003
-- Version: 1.13
-- Fix sul controllo stato esercizio dei CdS nel ribaltamento massivo
--
-- Date: 11/09/2003
-- Version: 1.14
-- Aggiunto riga di log conclusivo per ribaltamento massivo
--
-- Date: 24/01/2005
-- Version: 1.15
-- Aggiunta nuova procedura job_ribalta_massivo_doc_cont
-- per lanciare il job di ribaltamento massivo di documenti contabili
--
-- Date: 13/07/2006
-- Version: 1.16
-- Gestione Impegni/Accertamenti Residui:
-- aggiornata la funzione per tener conto anche del campo Esercizio Originale Impegno/Accertamento
--
-- Constants:
--
-- Tipo Log
LOG_TIPO_ANN_DOCCONT CONSTANT VARCHAR2(20) := 'ANN_DOCCONT00';
LOG_TIPO_RIP_NEXT CONSTANT VARCHAR2(20)    := 'RIP_ES_DOCCONT00';
LOG_TIPO_RIP_PREV CONSTANT VARCHAR2(20)    := 'RIP_ES_DOCCONT01';
--
-- Descrizione processi
DS_ANNULLAMENTO CONSTANT VARCHAR2(200) := 'Annullamento massivo documenti contabili.';
DS_RIPORTO_NEXT CONSTANT VARCHAR2(200) := 'Riporto massivo documenti contabili sull''esercizio successivo';
DS_RIPORTO_PREV CONSTANT VARCHAR2(200) := 'Riporto massivo documenti contabili all''esercizio precedente';
DS_RIPORTO_NEXT_VOCE CONSTANT VARCHAR2(200) := 'Riporto massivo documenti contabili sull''esercizio successivo con cambio capitolo';
--
-- Costanti per log riassuntivo
DOC_RIPORTATO CONSTANT BOOLEAN := TRUE;
DOC_NON_RIPORTATO CONSTANT BOOLEAN := FALSE;
--
-- Variabili globali:
   totImp number;
   totImpRes number;
   totObb number;
   totObbResPro number;
   totObbPlur number;
   totAnnPGiro number;
   totAcc number;
   totAccPlur number;
   totAccRes number;
   totImpRip number;
   totImpResRip number;
   totObbRip number;
   totObbResProRip number;
   totObbPlurRip number;
   totAnnPGiroRip number;
   totAccRip number;
   totAccPlurRip number;
   totAccResRip number;
   imRiportatoSpesa number;
   imNonRiportatoSpesa number;
   imRiportatoEntrata number;
   imNonRiportatoEntrata number;


-- Functions e Procedures:
--
-- Annullamento massivo
--
-- annullamentoDocCont
-- -------------------
-- pre-post name: annullamento massivo di documenti contabili
-- pre: l'applicazione ha inserito le chiavi dei documenti contabili da processare
--      nella vista vsx_chiusura
-- post: viene creato e lanciato il batch dinamico del job, l'utente riceve il messaggio di
--       operazione sottomessa per esecuzione
--
-- Parametri:
-- aPgCall -> progressivo identificativo dei record nella vsx_chiusura
-- aUser -> utente che effettua l'operazione
procedure annullamentoDocCont(aPgCall number, aUser varchar2);
-- job_annullamento_doc_cont
-- -------------------------
-- pre-post name: job di annullamento massivo di documenti contabili
-- pre: ט stato creato un batch dinamico
-- post: viene aggiornata la testata del log in esecuzione, vengono processati i documenti,
--       eliminati i record dalla vista vsx_chiusura, l'utente riceve un messaggio di
--       operazione completata
--
-- Parametri:
-- job -> numero identificativo del job del batch
-- pg_exec -> numero progressivo del batch
-- next_date -> timestamp di esecuzione del batch
-- aPgCall -> progressivo identificativo dei record nella vsx_chiusura
-- aUser -> utente che effettua l'operazione
procedure job_annullamento_doc_cont(job number, pg_exec number, next_date date, aPgCall number, aUser varchar2);
--
-- Riporto documenti all'anno successivo
--
-- ribaltamentoMassivo
-- -------------------
procedure ribaltamentoMassivo(aEs number);
--
--
-- job_ribalta_massivo_doc_cont
-- -------------------------
-- pre-post name: job di ribaltamento massivo di documenti contabili
-- pre: ט stato creato un batch dinamico
-- post: viene aggiornata la testata del log in esecuzione, vengono processati i documenti,
--       eliminati i record dalla vista vsx_chiusura, l'utente riceve un messaggio di
--       operazione completata
--
-- Parametri:
-- job -> numero identificativo del job del batch
-- pg_exec -> numero progressivo del batch
-- next_date -> timestamp di esecuzione del batch
-- aPgCall -> progressivo identificativo dei record nella vsx_chiusura
-- aUser -> utente che effettua l'operazione
Procedure job_ribalta_massivo_doc_cont(job number, pg_exec number, next_date date, aPgCall number, aUser varchar2, aEs number);
-- riportoNextEsDocCont
-- --------------------
-- pre-post name: riporto massivo all'esercizio successivo di documenti contabili
-- pre: l'applicazione ha inserito le chiavi dei documenti contabili da processare
--      nella vista vsx_chiusura
-- post: viene creato e lanciato il batch dinamico del job, l'utente riceve il messaggio di
--       operazione sottomessa per esecuzione
--
-- Parametri:
-- aPgCall -> progressivo identificativo dei record nella vsx_chiusura
-- aUser -> utente che effettua l'operazione
procedure riportoNextEsDocCont(aPgCall number, aUser VARCHAR2, aCDS VARCHAR2);
--
-- job_riporto_next_doc_cont
-- -------------------------
-- pre-post name: job di riporto all'esercizio successivo di documenti contabili
-- pre: ט stato creato un batch dinamico
-- post: viene aggiornata la testata del log in esecuzione, viene inizializzato il log
--       riassuntivo dei documenti processati, vengono processati i documenti,
--       eliminati i record dalla vista vsx_chiusura, viene inserita la riga di log
--       di riassunto, l'utente riceve un messaggio di operazione completata
--
-- Parametri:
-- job -> numero identificativo del job del batch
-- pg_exec -> numero progressivo del batch
-- next_date -> timestamp di esecuzione del batch
-- aPgCall -> progressivo identificativo dei record nella vsx_chiusura
-- aUser -> utente che effettua l'operazione
procedure job_riporto_next_doc_cont(job number, pg_exec number, next_date date, aPgCall number, aUser VARCHAR2, aCDS VARCHAR2);
--
-- Riporto documenti all'anno precedente
--
-- riportoPrevEsDocCont
-- --------------------
-- pre-post name: riporto massivo all'esercizio origine di documenti contabili riportati
-- pre: l'applicazione ha inserito le chiavi dei documenti contabili da processare
--      nella vista vsx_chiusura
-- post: viene creato e lanciato il batch dinamico del job, l'utente riceve il messaggio di
--       operazione sottomessa per esecuzione
--
-- Parametri:
-- aPgCall -> progressivo identificativo dei record nella vsx_chiusura
-- aUser -> utente che effettua l'operazione
procedure riportoPrevEsDocCont(aPgCall number, aUser varchar2);

-- job_riporto_prev_doc_cont
-- -------------------------
-- pre-post name: job di riporto all'esercizio origine di documenti contabili riportati
-- pre: ט stato creato un batch dinamico
-- post: viene aggiornata la testata del log in esecuzione, vengono processati i documenti,
--       eliminati i record dalla vista vsx_chiusura, l'utente riceve un messaggio di
--       operazione completata
--
-- Parametri:
-- job -> numero identificativo del job del batch
-- pg_exec -> numero progressivo del batch
-- next_date -> timestamp di esecuzione del batch
-- aPgCall -> progressivo identificativo dei record nella vsx_chiusura
-- aUser -> utente che effettua l'operazione
procedure job_riporto_prev_doc_cont(job number, pg_exec number, next_date date, aPgCall number, aUser varchar2);
--
-- Riporto documenti all'anno successivo con cambio di voce
--
-- riportoNextEsDocContVoce
-- ------------------------
-- pre-post name: riporto massivo all'esercizio successivo di documenti contabili con cambio di
--                imputazione finanziaria
-- pre: l'applicazione ha inserito le chiavi dei documenti contabili da processare
--      nella vista vsx_chiusura, e la chiave dell'elemento voce/voce_f nuovo
-- post: viene creato e lanciato il batch dinamico del job, l'utente riceve il messaggio di
--       operazione sottomessa per esecuzione
--
-- Parametri:
-- aPgCall -> progressivo identificativo dei record nella vsx_chiusura
-- aUser -> utente che effettua l'operazione
procedure riportoNextEsDocContVoce(aPgCall number, aUser varchar2);

-- job_riporto_next_doc_cont_voce
-- ------------------------------
-- pre-post name: job di riporto all'esercizio successivo di documenti contabili con cambio di
--                imputazione finanziaria
-- pre: ט stato creato un batch dinamico
-- post: viene aggiornata la testata del log in esecuzione, vengono processati i documenti,
--       eliminati i record dalla vista vsx_chiusura, l'utente riceve un messaggio di
--       operazione completata
--
-- Parametri:
-- job -> numero identificativo del job del batch
-- pg_exec -> numero progressivo del batch
-- next_date -> timestamp di esecuzione del batch
-- aPgCall -> progressivo identificativo dei record nella vsx_chiusura
-- aUser -> utente che effettua l'operazione
procedure job_riporto_next_doc_cont_voce(job number, pg_exec number, next_date date, aPgCall number, aUser varchar2);

--metodo inserito per gestire la chiamata da JAVA
Procedure ribalta_disp_improprie (aEs NUMBER, aCDS VARCHAR2, aUser VARCHAR2,pg_esec number default null);

procedure ribalta_disp_improprie_int (aEs NUMBER, aCDS VARCHAR2, aUser VARCHAR2,pg_esec number default null,ERROREIMP IN OUT char);
end;
