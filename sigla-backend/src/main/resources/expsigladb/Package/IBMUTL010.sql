--------------------------------------------------------
--  DDL for Package IBMUTL010
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "IBMUTL010" as
--
-- IBMUTL010 - Package di utilita per il logging BASELINE
-- Date: 27/05/2001
-- Version: 1.4
--
-- Procedure e Funzioni di utilita per il logging BASELINE
--
-- History:
--
-- Date: 08/09/2001
-- Version: 1.0
-- Creazione
--
-- Date: 17/05/2002
-- Version: 1.1
-- Aggiunto loggin extratransazionale su DB
--
-- Date: 18/05/2001
-- Version: 1.2
-- Aggiunto livello di abilitazione/disabilitazione del logging a livello di istanza
--
-- Date: 21/05/2001
-- Version: 1.3
-- Aggiunta baseline di logging applicativo
--
-- Date: 27/05/2001
-- Version: 1.4
-- Eliminata gestione abil. log via APPLICATION CONTEXT
-- Introdotta tabella IBMLOG_SESS_CTL
--
-- Constants:

 TI_LOG_ERROR CONSTANT VARCHAR2(5):='E';
 TI_LOG_INFO CONSTANT VARCHAR2(5):='I';
 TI_LOG_WARNING CONSTANT VARCHAR2(5):='W';

 TI_LOG_HEAD CONSTANT VARCHAR2(5):='HEAD';
 TI_LOG_COLS CONSTANT VARCHAR2(5):='COLS';
 TI_LOG_FOOT CONSTANT VARCHAR2(5):='FOOT';
 TI_LOG_SEP CONSTANT VARCHAR2(5):='SEP';

-- Functions & Procedures:

-- Stampa sull'stdout corrente

 procedure scrivi(aS1 varchar2);

-- Funzione di loggin extratransazionale su tabella di LOG
-- aS1 log di max 4k
-- aKey campo ident. opzionale del log in sessione corrente (di default = all'id di sessione)

 procedure log(aS1 varchar2, aKey varchar2 default DBMS_SESSION.UNIQUE_SESSION_ID);

-- Elimina in modo extra transazionale tutto il log della sesisone corrente
 procedure clearLog;

-- Abilita il logging per sessione corrente
 procedure enableSessionLog;

-- Abilita il logging sull'istanza
 procedure enableLog;

-- Disabilita il logging per sessione corrente
 procedure disableSessionLog;

-- Disabilita il logging sull'istanza
 procedure disableLog;

-- Ritorna true se il loggine è enabled
 function isLogEnabled return boolean;

-- Log baseline for application logging
 procedure logInf(aKey varchar2, aPgRiga number, aMsg varchar2, aNote varchar2 default null);
 procedure logWar(aKey varchar2, aPgRiga number, aMsg varchar2, aNote varchar2 default null);
 procedure logErr(aKey varchar2, aPgRiga number, aMsg varchar2, aNote varchar2 default null);
 procedure logHead(aKey varchar2, aPgRiga number, aMsg varchar2, aNote varchar2 default null);
 procedure logCols(aKey varchar2, aPgRiga number, aMsg varchar2, aNote varchar2 default null);
 procedure logFoot(aKey varchar2, aPgRiga number, aMsg varchar2, aNote varchar2 default null);
 procedure logSeparator(aKey varchar2, aPgRiga number, aMsg varchar2, aNote varchar2 default null);
end;
