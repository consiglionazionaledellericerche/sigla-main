--------------------------------------------------------
--  DDL for Package Body CNRMAT001
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "CNRMAT001" is
  Procedure job_deleteBatch_log_riga(aJob number, aPg_exec number, aNext_date DATE) Is
    aTSNow          DATE;
    aUser           VARCHAR2(30);
    aStartT         DATE;
    aStart          VARCHAR2(250);
    tot_righe       NUMBER:=0;
  Begin
    aTSNow:=sysdate;
    aUser:=IBMUTL200.getUserFromLog(aPg_exec);
    aStartT:=sysdate;
    aStart:=to_char(sysdate,'YYYYMMDD HH:MI:SS');
    IBMUTL210.logStartExecutionUpd(aPg_exec, TIPO_LOG_DEL_BATCH, aJob , 'Batch di cancellazione BATCH_LOG_TSTA. Start:'||to_char(aTSNow,'YYYY/MM/DD HH-MI-SS'),TIPO_LOG_DEL_BATCH);
    IBMUTL200.logInf(aPg_exec,'Batch di cancellazione BATCH_LOG_TSTA START at: '||aStart,Null,Null);
    Begin
     For Rec In (Select * From BATCH_LOG_TSTA
                 Where dacr < Sysdate -30 and
                 CD_LOG_TIPO != 'ELAB_STIP00') Loop
        -- anche se la cancellazione è gestita in cascade in alcuni casi il numero delle
        -- righe è troppo alto
        Select Count(0) Into tot_righe From BATCH_LOG_RIGA
                Where Pg_esecuzione = rec.pg_esecuzione;
        While(tot_righe>0)Loop
                Delete BATCH_LOG_RIGA
                Where Pg_esecuzione = rec.pg_esecuzione And
                Rownum <10000;
                Commit;
            Select Count(0) Into tot_righe From BATCH_LOG_RIGA
                Where Pg_esecuzione = rec.pg_esecuzione;
        End Loop;
        Delete BATCH_LOG_TSTA
        Where Pg_esecuzione = rec.pg_esecuzione;
        Commit;
     End Loop;
     IBMUTL200.logInf(aPg_exec,'Batch di cancellazione BATCH_LOG_TSTA Completato.',TIPO_LOG_DEL_BATCH,TIPO_LOG_DEL_BATCH);
     Commit;
    Exception
      When Others Then
         IBMUTL200.logErr(aJob,Substr(Sqlerrm,1,4000),'Batch di cancellazione BATCH_LOG_TSTA',TIPO_LOG_DEL_BATCH);
    End;
  End;
End;
