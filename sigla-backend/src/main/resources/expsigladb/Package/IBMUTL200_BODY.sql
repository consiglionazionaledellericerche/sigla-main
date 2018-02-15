--------------------------------------------------------
--  DDL for Package Body IBMUTL200
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "IBMUTL200" is

 function getUserFromLog(aPgExec number) return varchar2 is
  aUser varchar2(20);
 begin
  if aPgExec is null then return null; end if;
  select utcr into aUser from batch_log_tsta where pg_esecuzione = aPgExec;
  return aUser;
 end;

 function logStart(
     aDesc varchar2
    ,aUtcr varchar2
    ,aPgJob number
	,aPgBatch number
 ) return number is
 Begin
  return logStart(Null,aDesc,null,aUtcr,aPgJob,aPgBatch);
 end;

-- DUPLICATA

 function logStart_TEMP(
     aDesc varchar2
    ,aUtcr varchar2
    ,aPgJob number
    ,aPgBatch number
 ) return number is
 Begin
  return logStart_TEMP(Null,aDesc,null,aUtcr,aPgJob,aPgBatch);
 end;

 Function logStart(
     aTipo varchar2
    ,aDesc varchar2
    ,aUtcr varchar2
    ,aPgJob NUMBER
    ,aPgBatch number
 ) return number is
 begin
  return logStart(aTipo,aDesc,null,aUtcr,aPgJob,aPgBatch);
 end;

-- DUPLICATA

 Function logStart_TEMP(
     aTipo varchar2
    ,aDesc varchar2
    ,aUtcr varchar2
    ,aPgJob NUMBER
    ,aPgBatch number
 ) return number is
 begin
  return logStart_TEMP(aTipo,aDesc,null,aUtcr,aPgJob,aPgBatch);
 end;

 function logStart(
     aDesc varchar2
    ,aNota varchar2
    ,aUtcr varchar2
    ,aPgJob number
    ,aPgBatch number
 ) return number is
 begin
  return logStart(Null,aDesc,aNota,aUtcr,aPgJob,aPgBatch);
 end;

-- DUPLICATA

 function logStart_TEMP(
     aDesc varchar2
    ,aNota varchar2
    ,aUtcr varchar2
    ,aPgJob number
    ,aPgBatch number
 ) return number is
 begin
  return logStart_TEMP(Null,aDesc,aNota,aUtcr,aPgJob,aPgBatch);
 end;

 Function logStart(
     aTipo varchar2
    ,aDesc varchar2
    ,aNota varchar2
    ,aUtcr varchar2
    ,aPgJob number
    ,aPgBatch number
 ) return number is
  aLT BATCH_LOG_TSTA%rowtype;
  aTSNow date;
  aPgEsecuzione number;
  PRAGMA AUTONOMOUS_TRANSACTION;
 begin
  select IBMSEQ00_BATCH_LOG.nextval into aPgEsecuzione from dual;
  aTSNow:=sysdate;
  aLT.PG_ESECUZIONE:=aPgEsecuzione;
  aLT.PG_JOB:=aPgJob;
  aLT.PG_BATCH:=aPgBatch;
  aLT.FL_ERRORI:='N';
  aLT.DS_LOG:=aDesc;
  if aTipo is null then
   aLT.CD_LOG_TIPO:=TIPO_LOG_INDEFINITO;
  else
   aLT.CD_LOG_TIPO:=aTipo;
  end if;
  aLT.NOTE:=aNota;
  aLT.UTCR:=aUtcr;
  aLT.UTUV:=aUtcr;
  aLT.DUVA:=aTSNow;
  aLT.DACR:=aTSNow;
  aLT.PG_VER_REC:=1;
  ins_BATCH_LOG_TSTA(aLT);
  commit;
  return aPgEsecuzione;
 end;

-- DUPLICATA

 Function logStart_TEMP(
     aTipo varchar2
    ,aDesc varchar2
    ,aNota varchar2
    ,aUtcr varchar2
    ,aPgJob number
    ,aPgBatch number
 ) return number is
  aTSNow        date;
  aPgEsecuzione number;
  PRAGMA        AUTONOMOUS_TRANSACTION;
  aBATCH_LOG_TSTA_TEMP BATCH_LOG_TSTA_TEMP;
 begin
  select IBMSEQ00_BATCH_LOG.nextval into aPgEsecuzione from dual;
  aTSNow        :=      sysdate;

  aBATCH_LOG_TSTA_TEMP(aBATCH_LOG_TSTA_TEMP.COUNT + 1).PG_ESECUZIONE     := aPgEsecuzione;
  aBATCH_LOG_TSTA_TEMP(aBATCH_LOG_TSTA_TEMP.COUNT + 1).PG_BATCH          := aPgBatch;
  aBATCH_LOG_TSTA_TEMP(aBATCH_LOG_TSTA_TEMP.COUNT + 1).PG_JOB            := aPgJob;
  aBATCH_LOG_TSTA_TEMP(aBATCH_LOG_TSTA_TEMP.COUNT + 1).FL_ERRORI         := 'N';
  aBATCH_LOG_TSTA_TEMP(aBATCH_LOG_TSTA_TEMP.COUNT + 1).DS_LOG            := aDesc;
  aBATCH_LOG_TSTA_TEMP(aBATCH_LOG_TSTA_TEMP.COUNT + 1).NOTE              := aNota;

  if aTipo is null then
   aBATCH_LOG_TSTA_TEMP(aBATCH_LOG_TSTA_TEMP.COUNT + 1).CD_LOG_TIPO      := TIPO_LOG_INDEFINITO;
  else
   aBATCH_LOG_TSTA_TEMP(aBATCH_LOG_TSTA_TEMP.COUNT + 1).CD_LOG_TIPO      := aTipo;
  end if;

  aBATCH_LOG_TSTA_TEMP(aBATCH_LOG_TSTA_TEMP.COUNT + 1).UTCR              := aUtcr;
  aBATCH_LOG_TSTA_TEMP(aBATCH_LOG_TSTA_TEMP.COUNT + 1).UTUV              := aUtcr;
  aBATCH_LOG_TSTA_TEMP(aBATCH_LOG_TSTA_TEMP.COUNT + 1).DUVA              := aTSNow;
  aBATCH_LOG_TSTA_TEMP(aBATCH_LOG_TSTA_TEMP.COUNT + 1).DACR              := aTSNow;
  aBATCH_LOG_TSTA_TEMP(aBATCH_LOG_TSTA_TEMP.COUNT + 1).PG_VER_REC        := 1;

  return aPgEsecuzione;
 end;

 Procedure logMsg(
     aPgEsecuzione number
    ,aTiMessaggio varchar2
    ,aMessaggio varchar2
    ,aTrace varchar2
    ,aNota VARCHAR2
    ,aCommit VARCHAR2
 ) is
  aLR    BATCH_LOG_RIGA%rowtype;
  aTSNow date;
  aMax   number;
  aUtcr  varchar2(20);
  PRAGMA AUTONOMOUS_TRANSACTION;
 Begin

     aTSNow:=sysdate;

	 select utcr into aUtcr
	 from   BATCH_LOG_TSTA
	 Where  pg_esecuzione = aPgEsecuzione;

	 select nvl(max(pg_riga),0) into aMax
	 from   BATCH_LOG_RIGA
	 Where  pg_esecuzione = aPgEsecuzione;

     aLR.PG_ESECUZIONE:=aPgEsecuzione;
     aLR.PG_RIGA:=aMax + 1;
     aLR.TI_MESSAGGIO:=aTiMessaggio;
     aLR.MESSAGGIO:=aMessaggio;
     aLR.TRACE:=aTrace;
     aLR.NOTE:=aNota;
     aLR.UTCR:=aUtcr;
     aLR.UTUV:=aUtcr;
     aLR.DUVA:=aTSNow;
     aLR.DACR:=aTSNow;
     aLR.PG_VER_REC:=1;

     ins_BATCH_LOG_RIGA(aLR);

     If (aCommit = 'S') Then
       commit;
     End If;
 end;

-- DUPLICATA PER TABELLE TEMP

 Procedure logMsg_TEMP(
     aPgEsecuzione number
    ,aTiMessaggio varchar2
    ,aMessaggio varchar2
    ,aTrace varchar2
    ,aNota VARCHAR2
    ,aCommit VARCHAR2
 ) is
  aTSNow date;
  aUtcr varchar2(20);
  last_rec      NUMBER;
  PRAGMA AUTONOMOUS_TRANSACTION;

 begin
     aTSNow := sysdate;

     select utcr into aUtcr
     from BATCH_LOG_TSTA
     Where pg_esecuzione = aPgEsecuzione;

     last_rec := aBATCH_LOG_RIGA_TEMP.COUNT;

     aBATCH_LOG_RIGA_TEMP(last_rec + 1).PG_ESECUZIONE := aPgEsecuzione;
     aBATCH_LOG_RIGA_TEMP(last_rec + 1).PG_RIGA       := last_rec + 1;
     aBATCH_LOG_RIGA_TEMP(last_rec + 1).TI_MESSAGGIO  := aTiMessaggio;
     aBATCH_LOG_RIGA_TEMP(last_rec + 1).MESSAGGIO     := aMessaggio;
     aBATCH_LOG_RIGA_TEMP(last_rec + 1).TRACE         := aTrace;
     aBATCH_LOG_RIGA_TEMP(last_rec + 1).NOTE          := aNota;
     aBATCH_LOG_RIGA_TEMP(last_rec + 1).UTCR          := aUtcr;
     aBATCH_LOG_RIGA_TEMP(last_rec + 1).UTUV          := aUtcr;
     aBATCH_LOG_RIGA_TEMP(last_rec + 1).DUVA          := aTSNow;
     aBATCH_LOG_RIGA_TEMP(last_rec + 1).DACR          := aTSNow;
     aBATCH_LOG_RIGA_TEMP(last_rec + 1).PG_VER_REC    := 1;

     If (aCommit = 'S') Then
       commit;
     End If;
 end;

 Procedure logMsg(
     aPgEsecuzione number
    ,aTiMessaggio varchar2
    ,aMessaggio varchar2
    ,aTrace varchar2
    ,aNota varchar2
 ) is
 Begin
   logMsg(aPgEsecuzione,aTiMessaggio,aMessaggio,aTrace,aNota,'S');
 End;

 procedure logErr(
     aPgEsecuzione number
    ,aMessaggio varchar2
    ,aTrace varchar2
    ,aNota varchar2
 ) is
begin
  logErr(aPgEsecuzione,aMessaggio,aTrace,aNota, 'S');
 end;

procedure logErr(
     aPgEsecuzione number
    ,aMessaggio varchar2
    ,aTrace varchar2
    ,aNota   VARCHAR2
    ,aCommit VARCHAR2
 ) Is

  aTemp  BATCH_LOG_TSTA%rowtype;
  PRAGMA AUTONOMOUS_TRANSACTION;

 Begin

  logMsg(aPgEsecuzione,TI_ERRORE,aMessaggio,aTrace,aNota,aCommit);

  Begin
   Select * into aTemp
   from   BATCH_LOG_TSTA
   Where  pg_esecuzione = aPgEsecuzione And
          fl_errori = 'N'
          for update nowait;

   update BATCH_LOG_TSTA
   Set    fl_errori = 'Y',
          duva = sysdate,
	  pg_ver_rec = pg_ver_rec + 1
   Where  pg_esecuzione = aPgEsecuzione;

   If (aCommit = 'S') Then
      commit;
   End If;

  exception when NO_DATA_FOUND then
   rollback;
  end;
 end;

-- DUPLICATA PER TABELLE TEMP

procedure logErr_TEMP(
     aPgEsecuzione number
    ,aMessaggio varchar2
    ,aTrace varchar2
    ,aNota VARCHAR2
    ,aCommit VARCHAR2
 ) is
  aTemp BATCH_LOG_TSTA%rowtype;
  PRAGMA AUTONOMOUS_TRANSACTION;
 begin
  logMsg_TEMP(aPgEsecuzione,TI_ERRORE,aMessaggio,aTrace,aNota,aCommit);

  begin
/* NON SERVE PIU', SI VEDE SOLO NELLA MIA SESSIONE
   select * into aTemp
   from BATCH_LOG_TSTA
   Where pg_esecuzione = aPgEsecuzione And
         fl_errori = 'N'
   for update nowait;
*/
/*
   update BATCH_LOG_TSTA
   Set fl_errori = 'Y',
       duva = sysdate,
       pg_ver_rec = pg_ver_rec + 1
   Where pg_esecuzione = aPgEsecuzione;
*/

   for i in 1 .. IBMUTL200.aBATCH_LOG_TSTA_TEMP.count loop
    If IBMUTL200.aBATCH_LOG_TSTA_TEMP(i).pg_esecuzione = aPgEsecuzione Then
        IBMUTL200.aBATCH_LOG_TSTA_TEMP(i).fl_errori := 'Y';
        IBMUTL200.aBATCH_LOG_TSTA_TEMP(i).duva := Sysdate;
        IBMUTL200.aBATCH_LOG_TSTA_TEMP(i).pg_ver_rec := IBMUTL200.aBATCH_LOG_TSTA_TEMP(i).pg_ver_rec + 1;
    End If;
   end loop;

   If (aCommit = 'S') Then
      commit;
   End If;

  exception when NO_DATA_FOUND then
   rollback;
  end;
 end;



 procedure logWar(
     aPgEsecuzione number
    ,aMessaggio varchar2
    ,aTrace varchar2
    ,aNota varchar2
 ) is
 begin
  logMsg(aPgEsecuzione,TI_WARNING,aMessaggio,aTrace,aNota,'S');
 end;

 procedure logWar_TEMP(
     aPgEsecuzione number
    ,aMessaggio varchar2
    ,aTrace varchar2
    ,aNota varchar2
 ) is
 begin
  logMsg_TEMP(aPgEsecuzione,TI_WARNING,aMessaggio,aTrace,aNota,'N');
 end;


 procedure logInf(
     aPgEsecuzione number
    ,aMessaggio varchar2
    ,aTrace varchar2
    ,aNota varchar2
 ) is
 begin
  logInf(aPgEsecuzione,aMessaggio,aTrace,aNota, 'S');
 end;

 procedure logInf(
     aPgEsecuzione number
    ,aMessaggio varchar2
    ,aTrace varchar2
    ,aNota VARCHAR2
    ,aCommit VARCHAR2
 ) is
 begin
  logMsg(aPgEsecuzione,TI_INFO,aMessaggio,aTrace,aNota,aCommit);
 end;

-- DUPLICATA PER TABELLE TEMP

 procedure logInf_TEMP(
     aPgEsecuzione number
    ,aMessaggio varchar2
    ,aTrace varchar2
    ,aNota VARCHAR2
    ,aCommit VARCHAR2
 ) is
 begin
  logMsg_TEMP(aPgEsecuzione,TI_INFO,aMessaggio,aTrace,aNota,aCommit);
 end;


 procedure ins_BATCH_LOG_TSTA (aDest BATCH_LOG_TSTA%rowtype) is
  begin
   insert into BATCH_LOG_TSTA (
     FL_ERRORI
    ,NOTE
	,DS_LOG
    ,UTCR
    ,UTUV
    ,DUVA
    ,DACR
    ,PG_VER_REC
    ,PG_ESECUZIONE
	,CD_LOG_TIPO
    ,PG_JOB
    ,PG_BATCH
   ) values (
     aDest.FL_ERRORI
    ,aDest.NOTE
	,aDest.DS_LOG
    ,aDest.UTCR
    ,aDest.UTUV
    ,aDest.DUVA
    ,aDest.DACR
    ,aDest.PG_VER_REC
    ,aDest.PG_ESECUZIONE
    ,aDest.CD_LOG_TIPO
    ,aDest.PG_JOB
    ,aDest.PG_BATCH
    );
 end;

 procedure ins_BATCH_LOG_RIGA (aDest BATCH_LOG_RIGA%rowtype) is
  begin
   insert into BATCH_LOG_RIGA (
     PG_ESECUZIONE
    ,PG_RIGA
    ,TI_MESSAGGIO
    ,MESSAGGIO
    ,TRACE
    ,NOTE
    ,UTCR
    ,UTUV
    ,DUVA
    ,DACR
    ,PG_VER_REC
   ) values (
     aDest.PG_ESECUZIONE
    ,aDest.PG_RIGA
    ,aDest.TI_MESSAGGIO
    ,aDest.MESSAGGIO
    ,aDest.TRACE
    ,aDest.NOTE
    ,aDest.UTCR
    ,aDest.UTUV
    ,aDest.DUVA
    ,aDest.DACR
    ,aDest.PG_VER_REC
    );
 end;

 procedure scarica_temp_in_BATCH_LOG_TSTA Is
 Begin
  if aBATCH_LOG_TSTA_TEMP.count = 0 then
       return;
  End if;

  for i in 1 .. aBATCH_LOG_TSTA_TEMP.count loop
   ins_BATCH_LOG_TSTA(aBATCH_LOG_TSTA_TEMP(i));
  end loop;
 END;

 procedure scarica_temp_in_BATCH_LOG_RIGA Is
 Begin
  if aBATCH_LOG_RIGA_TEMP.count = 0 then
       return;
  End if;

  for i in 1 .. aBATCH_LOG_RIGA_TEMP.count loop

aBATCH_LOG_RIGA_TEMP_da_ins(i).PG_ESECUZIONE := aBATCH_LOG_RIGA_TEMP(i).PG_ESECUZIONE;
aBATCH_LOG_RIGA_TEMP_da_ins(i).PG_RIGA       := aBATCH_LOG_RIGA_TEMP(i).PG_RIGA      ;
aBATCH_LOG_RIGA_TEMP_da_ins(i).TI_MESSAGGIO  := aBATCH_LOG_RIGA_TEMP(i).TI_MESSAGGIO ;
aBATCH_LOG_RIGA_TEMP_da_ins(i).MESSAGGIO     := aBATCH_LOG_RIGA_TEMP(i).MESSAGGIO    ;
aBATCH_LOG_RIGA_TEMP_da_ins(i).TRACE         := aBATCH_LOG_RIGA_TEMP(i).TRACE        ;
aBATCH_LOG_RIGA_TEMP_da_ins(i).NOTE          := aBATCH_LOG_RIGA_TEMP(i).NOTE         ;
aBATCH_LOG_RIGA_TEMP_da_ins(i).DACR          := aBATCH_LOG_RIGA_TEMP(i).DACR         ;
aBATCH_LOG_RIGA_TEMP_da_ins(i).DUVA          := aBATCH_LOG_RIGA_TEMP(i).DUVA         ;
aBATCH_LOG_RIGA_TEMP_da_ins(i).UTCR          := aBATCH_LOG_RIGA_TEMP(i).UTCR         ;
aBATCH_LOG_RIGA_TEMP_da_ins(i).UTUV          := aBATCH_LOG_RIGA_TEMP(i).UTUV         ;
aBATCH_LOG_RIGA_TEMP_da_ins(i).PG_VER_REC    := aBATCH_LOG_RIGA_TEMP(i).PG_VER_REC   ;

   ins_BATCH_LOG_RIGA(aBATCH_LOG_RIGA_TEMP_da_ins(i));
  end loop;

 END;


 procedure ins_BATCH_CONTROL (aDest BATCH_CONTROL%rowtype) is
  begin
   insert into BATCH_CONTROL (
     PG_BATCH
    ,DS_BATCH
    ,PG_JOB
    ,MESSAGE
    ,FL_EXEC_CONC
    ,FL_ATTIVATO
    ,UTENTE
	,INTERVALLO
	,DT_PARTENZA
    ,UTCR
    ,UTUV
    ,DACR
    ,DUVA
    ,PG_VER_REC
   ) values (
     aDest.PG_BATCH
    ,aDest.DS_BATCH
    ,aDest.PG_JOB
    ,aDest.MESSAGE
    ,aDest.FL_EXEC_CONC
    ,aDest.FL_ATTIVATO
    ,aDest.UTENTE
	,aDest.INTERVALLO
	,aDest.DT_PARTENZA
    ,aDest.UTCR
    ,aDest.UTUV
    ,aDest.DACR
    ,aDest.DUVA
    ,aDest.PG_VER_REC
    );
 end;

Procedure ins_EXT_CASSIERE00_SCARTI (aDest EXT_CASSIERE00_SCARTI%rowtype) Is
Begin
 Insert Into EXT_CASSIERE00_SCARTI (
         ESERCIZIO,
         NOME_FILE,
         PG_ESECUZIONE,
         PG_REC,
         DT_GIORNALIERA,
         DT_ELABORAZIONE,
         DT_ESECUZIONE,
         CD_CDS,
         CD_CDS_SR,
         ESERCIZIO_SR,
         TI_ENTRATA_SPESA_SR,
         TI_SOSPESO_RISCONTRO_SR,
         CD_SR,
         TIPO_MOV,
         CD_CDS_MANREV,
         ESERCIZIO_MANREV,
         PG_MANREV,
         ANOMALIA,
         DACR,
         UTCR,
         DUVA,
         UTUV,
         PG_VER_REC)
     Values
        (aDest.ESERCIZIO,
         aDest.NOME_FILE,
         aDest.PG_ESECUZIONE,
         aDest.PG_REC,
         aDest.DT_GIORNALIERA,
         aDest.DT_ELABORAZIONE,
         aDest.DT_ESECUZIONE,
         aDest.CD_CDS,
         aDest.CD_CDS_SR,
         aDest.ESERCIZIO_SR,
         aDest.TI_ENTRATA_SPESA_SR,
         aDest.TI_SOSPESO_RISCONTRO_SR,
         aDest.CD_SR,
         aDest.TIPO_MOV,
         aDest.CD_CDS_MANREV,
         aDest.ESERCIZIO_MANREV,
         aDest.PG_MANREV,
         aDest.ANOMALIA,
         aDest.DACR,
         aDest.UTCR,
         aDest.DUVA,
         aDest.UTUV,
         aDest.PG_VER_REC);
End;


 Function get_count_batch_log_riga_temp Return NUMBER Is
 Begin
  Return IBMUTL200.aBATCH_LOG_TSTA_TEMP.count;
 End;

 Function get_err_batch_log_riga_temp Return Boolean Is
 conta_err NUMBER := 0;
 Begin
   for i in 1 .. IBMUTL200.aBATCH_LOG_RIGA_TEMP.count loop
    If IBMUTL200.aBATCH_LOG_RIGA_TEMP(i).TI_MESSAGGIO = TI_ERRORE Then
     conta_err := Nvl(conta_err, 0) + 1;
    End If;
   end loop;

   If  conta_err > 0 Then
     Return True;
   Else
     Return False;
   End If;
 End;

end;
