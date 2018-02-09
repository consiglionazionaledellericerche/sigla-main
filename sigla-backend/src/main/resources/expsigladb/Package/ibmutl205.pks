CREATE OR REPLACE package IBMUTL205 is
--
-- IBMUTL205 - Package di servizio per gestione dei messaggi agli utenti attraverso TOOL bframe message tool
-- Date: 28/01/2003
-- Version: 1.1
--
-- Gestione tabelle di log dei jobs applicativi
--
-- History:
-- Date: 02/12/2002
-- Version: 1.0
-- Creazione
--
-- Date: 28/01/2003
-- Version: 1.1
-- Fix minore
--
-- Constants:

-- Tipi di messaggi
TI_ERRORE CONSTANT VARCHAR2(5) := 'E';
TI_WARNING CONSTANT VARCHAR2(5) := 'W';
TI_INFO CONSTANT VARCHAR2(5) := 'I';

-- Utenza messaggi interna
MESSAGE_USER CONSTANT VARCHAR2(20) := '$$$$$MESSG_USER$$$$$';

-- Functions e Procedures:

-- Crea un'entry di log errore

 procedure logErr(
	 aDsMessaggio varchar2
    ,aSoggetto varchar2
    ,aCorpo varchar2
	,aUser varchar2 default null
 );

-- Crea un'entry di log warning
 procedure logWar(
     aDsMessaggio varchar2
    ,aSoggetto varchar2
    ,aCorpo varchar2
    ,aUser varchar2 default null
 );

-- Crea un'entry di log info
 procedure logInf(
     aDsMessaggio varchar2
    ,aSoggetto varchar2
    ,aCorpo varchar2
    ,aUser varchar2 default null
 );


-- Procedure di inserimento MESSAGGIO
 procedure ins_MESSAGGIO (aDest MESSAGGIO%rowtype);

end;
/


CREATE OR REPLACE package BODY IBMUTL205 is

 procedure ins_MESSAGGIO (aDest MESSAGGIO%rowtype) is
  begin
   insert into MESSAGGIO (
     PG_MESSAGGIO
    ,DS_MESSAGGIO
    ,CD_UTENTE
    ,SERVER_URL
    ,PRIORITA
    ,SOGGETTO
    ,CORPO
    ,DT_INIZIO_VALIDITA
    ,DT_FINE_VALIDITA
    ,DACR
    ,UTCR
    ,DUVA
    ,UTUV
    ,PG_VER_REC
   ) values (
     aDest.PG_MESSAGGIO
    ,aDest.DS_MESSAGGIO
    ,aDest.CD_UTENTE
    ,aDest.SERVER_URL
    ,aDest.PRIORITA
    ,aDest.SOGGETTO
    ,aDest.CORPO
    ,aDest.DT_INIZIO_VALIDITA
    ,aDest.DT_FINE_VALIDITA
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.DUVA
    ,aDest.UTUV
    ,aDest.PG_VER_REC
    );
 end;


 procedure logMsg(
	 aTiMessaggio varchar2
	,aDsMessaggio varchar2
    ,aSoggetto varchar2
    ,aCorpo varchar2
	,aUser varchar2 default null
 ) is
  aMess MESSAGGIO%rowtype;
  aTSNow date;
  aPgMessaggio NUMBER;
  PRAGMA AUTONOMOUS_TRANSACTION;
 begin
  select CNRSEQ00_PG_MESSAGGIO.nextval into aPgMessaggio from dual;
  aTSNow:=sysdate;
  aMess.PG_MESSAGGIO:=aPgMEssaggio;
  if aTiMessaggio = TI_ERRORE then
   aMess.DS_MESSAGGIO:='ERR: ';
  elsif aTiMessaggio = TI_WARNING then
   aMess.DS_MESSAGGIO:='ATTN: ';
  else
   aMess.DS_MESSAGGIO:='INFO: ';
  end if;
  aMess.DS_MESSAGGIO:=aMess.DS_MESSAGGIO||aDsMessaggio;
  aMess.CD_UTENTE:=aUser;
  aMess.SERVER_URL:=null;
  aMess.PRIORITA:=1;
  aMess.SOGGETTO:=aSoggetto;
  aMess.CORPO:=aCorpo;
  aMess.DT_INIZIO_VALIDITA:=null;
  aMess.DT_FINE_VALIDITA:=null;
  aMess.DACR:=aTSNow;
  aMess.UTCR:=nvl(aUser,MESSAGE_USER);
  aMess.DUVA:=aTSNow;
  aMess.UTUV:=nvl(aUser,MESSAGE_USER);
  aMess.PG_VER_REC:=1;
  ins_MESSAGGIO(aMess);
  commit;
 end;

 procedure logErr(
	 aDsMessaggio varchar2
    ,aSoggetto varchar2
    ,aCorpo varchar2
	,aUser varchar2 default null
 ) is
  PRAGMA AUTONOMOUS_TRANSACTION;
 begin
  logMsg(TI_ERRORE,aDsMessaggio,aSoggetto,aCorpo,aUser);
 end;

 procedure logWar(
     aDsMessaggio varchar2
    ,aSoggetto varchar2
    ,aCorpo varchar2
    ,aUser varchar2 default null
 ) is
 begin
  logMsg(TI_WARNING,aDsMessaggio,aSoggetto,aCorpo,aUser);
 end;

 Procedure logInf(
	 aDsMessaggio varchar2
    ,aSoggetto varchar2
    ,aCorpo varchar2
	,aUser varchar2 default null
 ) is
 begin
  logMsg(TI_INFO,aDsMessaggio,aSoggetto,aCorpo,aUser);
 end;

End;
/


