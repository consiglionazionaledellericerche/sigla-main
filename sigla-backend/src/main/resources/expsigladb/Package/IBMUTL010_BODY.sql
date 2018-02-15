--------------------------------------------------------
--  DDL for Package Body IBMUTL010
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "IBMUTL010" is

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
   IBMERR001.RAISE_ERR_GENERICO('Log già abilitato per la sessione corrente');
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
