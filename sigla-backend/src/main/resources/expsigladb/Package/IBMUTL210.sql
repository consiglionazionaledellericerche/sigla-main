--------------------------------------------------------
--  DDL for Package IBMUTL210
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "IBMUTL210" is
--
-- IBMUTL210 - Package di servizio per controllo dei JOBS applicativi
-- Date: 12/03/2003
-- Version: 1.11
--
-- Gestione tabelle di log dei jobs applicativi
--
-- Dependency: IBMUTL200
--
-- History:
-- Date: 09/03/2002
-- Version: 1.0
-- Creazione
--
-- Date: 10/03/2002
-- Version: 1.1
-- Fix varie
--
-- Date: 11/03/2002
-- Version: 1.2
-- Riorganizzazione
--
-- Date: 21/06/2002
-- Version: 1.3
-- Modificato intervallo di attivaBatch(): ora deve essere
-- specificato in secondi.
--
-- Date: 06/07/2002
-- Version: 1.4
-- Fix minime
--
-- Date: 10/07/2002
-- Version: 1.5
-- Tolto il run immediato all'esecuzione
-- Aggiunti controlli di non risottomissione dello stesso batch
--
-- Date: 10/10/2002
-- Version: 1.6
-- Transazione autonoma su logStartExecutionUpd con commit al termine
--
-- Date: 11/10/2002
-- Version: 1.7
-- Tolto il commit al termine del wrapper di procedure in wrapMessage visto che il logErr è a transazione autonoma
--
-- Date: 18/10/2002
-- Version: 1.8
-- Fix su invocazione di sottomissione batch dinamico: next_date -> sysdate se il parametro passato è null
--
-- Date: 03/12/2002
-- Version: 1.9
-- Fix su aggiornamento testata log per batch dinamici in logStartExecutionUpd
--
-- Date: 28/01/2003
-- Version: 1.10
-- Modificata logStartExecutionUpdate per gestione tipologia log
--
-- Date: 12/03/2003
-- Version: 1.11
-- Tolti commit su metodi di creazione/attivazione/rimozione batch statici e dinamici
--
-- Constants:

-- Functions e Procedures:

-- aggiorna le informazioni di testata dell'esecuzione aPgExec con dati utente
--
-- Se il job e statico -> vengono utilizzate le informazioni descrizione presenti in BATCH_CONTROL
-- Se il job e dinamico -> vengono utilizzate le informazioni descrizione presenti in aDesc

 procedure logStartExecutionUpd(
  aPgExec number,
  aPgJob number,
  aNota varchar2 default null,
  aDesc varchar2 default null
 );

-- COPIATA MA CHE SCRIVE NELLE TABELLE PL/SQL TEMP

 procedure logStartExecutionUpd_TEMP(
  aPgExec number,
  aPgJob number,
  aNota varchar2 default null,
  aDesc varchar2 default null
 );


 -- Versione che permette di aggiornare l'informazione relativa al tipo di log generato

 procedure logStartExecutionUpd(
  aPgExec number,
  aTipo varchar2,
  aPgJob number,
  aNota varchar2 default null,
  aDesc varchar2 default null
 );

-- COPIATA MA CHE SCRIVE NELLE TABELLE PL/SQL TEMP

 procedure logStartExecutionUpd_TEMP(
  aPgExec number,
  aTipo varchar2,
  aPgJob number,
  aNota varchar2 default null,
  aDesc varchar2 default Null);

-- registra la testata di esecuzione di un job statico o dinamico in log all'avvio dell'esecuzione

 Function logStartExecution(
  aPgJob number,
  aUser varchar2
 ) return number;

-- registra un job statico in log
 function logRegisterSBatch(aPgBatch number) return number;

-- Rimuove un batch statico

 procedure rimuoviBatch(
  aPgBatch number,
  aUser varchar2
 );

--
-- Crea un nuovo batch statico e il corrispondente job nella coda di job di Oracle registrandolo sul LOG
-- aProcedura deve essere una procedura PLSQL con almeno i seguenti parametri:
--
--  job number	   		  -> n. di job (schedulatore Oracle)
--  pg_exec number		  -> n. esecuzione per logging
--  next_date number	  -> prossima esecuzione
--
-- Il nome della procedura aProcedura deve essere job_XXXXXXX
--

 procedure creaBatch(
  aDesc varchar2,
  aProcedura varchar2,
  aUser varchar2,
  isExecConc char default 'N',
  partenza date default sysdate,
  intervallo varchar2 default 'null'
 );

 procedure creaBatchDinamico(
  aDesc varchar2,
  aProcedura varchar2,
  aUser varchar2,
  partenza date default sysdate
 );

-- Attiva un batch statico aggiungendo il Job alla coda Oracle dei jobs e registrandosi nel log dei jobs

 procedure attivaBatch(
  aPgBatch number,
  aUser varchar2,
  partenza date default sysdate,
  intervallo varchar2 default 'null'
 );

-- Disattiva un batch statico togliendolo il job corrispondente dalla coda dei jobs e settando il flag di disattivo in BATCH_CONTROL

 procedure disattivaBatch(aPgBatch number, aUser varchar2);

-- Ritorna l'owner del batch ATTIVO dato il progressivo del batch (solo per batch statici)

 function getBatchOwner(aPgBatch number) return varchar2;

-- Ritorna l'owner del job ATTIVO (solo per batch statici)

 function getJobOwner(aPgJob number) return varchar2;

end;
