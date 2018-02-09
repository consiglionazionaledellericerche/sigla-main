CREATE OR REPLACE package IBMUTL010 as
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

-- Ritorna true se il loggine ? enabled
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
/


CREATE OR REPLACE package body IBMUTL010 is

 procedure clearLog is
  PRAGMA AUTONOMOUS_TRANSACTION;
 begin
   delete from IBMLOG where
     usid = DBMS_SESSION.unique_session_id;
   commit;
 end;

 function isLogEnabled return boolean is
  list DBMS_SESSION.AppCtxTabTyp;
  lsize number;
  aNum number(1);
 begin
  if not IBMUTL011.isLogEnabled then
   return false;
  end if;
  begin
   select 1 into aNum from IBMLOG_SESS_CTL where usid = DBMS_SESSION.unique_session_id;
  exception when NO_DATA_FOUND then
   return false;
  end;
  return true;
 end;

 procedure enableLog is
 begin
  execute immediate 'create or replace package body IBMUTL011 is function isLogEnabled return boolean is begin return true; end; end;';
 end;

 procedure disableLog is
 begin
  execute immediate 'create or replace package body IBMUTL011 is function isLogEnabled return boolean is begin return false; end; end;';
 end;

 procedure ins_IBMLOG_SESS_CTL is
  PRAGMA AUTONOMOUS_TRANSACTION;
 begin
   insert into IBMLOG_SESS_CTL (
    usid,
	dt
   ) values (
    DBMS_SESSION.unique_session_id,
    SYSDATE
   );
   commit;
 end;

 procedure enableSessionLog is
 begin
  if not IBMUTL011.isLogEnabled then
   IBMERR001.RAISE_ERR_GENERICO('Log non abilitato a livello di istanza! Eseguire prima IBMUTL010.enablelog');
  end if;
  begin
   ins_IBMLOG_SESS_CTL;
  exception when DUP_VAL_ON_INDEX then
   IBMERR001.RAISE_ERR_GENERICO('Log gi? abilitato per la sessione corrente');
  end;
 end;

 procedure disableSessionLog is
  PRAGMA AUTONOMOUS_TRANSACTION;
 begin
  delete from IBMLOG_SESS_CTL where usid = DBMS_SESSION.unique_session_id;
  commit;
 end;

 procedure ins_IBMLOG(aDest IBMLOG%rowtype) is
  PRAGMA AUTONOMOUS_TRANSACTION;
 begin
   insert into IBMLOG (
     PG
	,usid
    ,UK
	,PG_RIGA
    ,A
	,TI_LOG
	,NOTE
    ,TRANSID
    ,STEPID
    ,DT
   ) values (
     IBMSEQ00_IBMLOG.nextval
    ,DBMS_SESSION.unique_session_id
    ,aDest.UK
	,aDest.PG_RIGA
    ,aDest.A
	,aDest.TI_LOG
    ,aDest.NOTE
    ,aDest.TRANSID
    ,aDest.STEPID
    ,aDest.DT
    );
    commit;
 end;

 procedure log(aS1 varchar2, aKey varchar2 default DBMS_SESSION.UNIQUE_SESSION_ID) is
  aLog IBMLOG%rowtype;
 begin
  if not isLogEnabled then
   return;
  end if;
  aLog.uk:=aKey;
  aLog.a:=aS1;
  aLog.dt:=sysdate;
  aLog.transid:=DBMS_TRANSACTION.LOCAL_TRANSACTION_ID;
  aLog.stepid:=DBMS_TRANSACTION.STEP_ID;
  ins_IBMLOG(aLog);
 end;

 procedure logBase(aKey varchar2, aPgRiga number, aMsg varchar2, aTiLog varchar2, aNote varchar2 default null) is
  aLog IBMLOG%rowtype;
 begin
  if not isLogEnabled then
   return;
  end if;
  aLog.uk:=aKey;
  aLog.a:=aMsg;
  aLog.ti_log:=aTiLog;
  aLog.dt:=sysdate;
  aLog.note:=aNote;
  aLog.transid:=DBMS_TRANSACTION.LOCAL_TRANSACTION_ID;
  aLog.stepid:=DBMS_TRANSACTION.STEP_ID;
  ins_IBMLOG(aLog);
 end;

 procedure logErr(aKey varchar2, aPgRiga number, aMsg varchar2, aNote varchar2 default null) is
 begin
  logBase(aKey, aPgRiga, aMsg, TI_LOG_ERROR, aNote);
 end;

 procedure logWar(aKey varchar2, aPgRiga number, aMsg varchar2, aNote varchar2 default null) is
 begin
  logBase(aKey, aPgRiga, aMsg, TI_LOG_WARNING, aNote);
 end;

 procedure logInf(aKey varchar2, aPgRiga number, aMsg varchar2, aNote varchar2 default null) is
 begin
  logBase(aKey, aPgRiga, aMsg, TI_LOG_INFO, aNote);
 end;

 procedure logHead(aKey varchar2, aPgRiga number, aMsg varchar2, aNote varchar2 default null) is
 begin
  logBase(aKey, aPgRiga, aMsg, TI_LOG_HEAD, aNote);
 end;

 procedure logCols(aKey varchar2, aPgRiga number, aMsg varchar2, aNote varchar2 default null) is
 begin
  logBase(aKey, aPgRiga, aMsg, TI_LOG_COLS, aNote);
 end;

 procedure logFoot(aKey varchar2, aPgRiga number, aMsg varchar2, aNote varchar2 default null) is
 begin
  logBase(aKey, aPgRiga, aMsg, TI_LOG_FOOT, aNote);
 end;

 procedure logSeparator(aKey varchar2, aPgRiga number, aMsg varchar2, aNote varchar2 default null) is
 begin
  logBase(aKey, aPgRiga, aMsg, TI_LOG_SEP, aNote);
 end;

 procedure prim_scrivi(aS1 varchar2) is
 begin
  dbms_output.put_line(aS1);
 end;

 procedure scrivi(aS1 varchar2) is
 begin
  prim_scrivi(aS1);
 end;

end;
/


