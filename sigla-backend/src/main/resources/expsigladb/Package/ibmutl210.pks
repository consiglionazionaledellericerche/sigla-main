CREATE OR REPLACE package IBMUTL210 is
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
-- Tolto il commit al termine del wrapper di procedure in wrapMessage visto che il logErr ? a transazione autonoma
--
-- Date: 18/10/2002
-- Version: 1.8
-- Fix su invocazione di sottomissione batch dinamico: next_date -> sysdate se il parametro passato ? null
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


CREATE OR REPLACE package BODY IBMUTL210 is
 function logStartExecution(
  aPgJob number,
  aUser varchar2
 ) return number is
  aPgExec number;
 begin
  aPgExec:=IBMUTL200.logStart(
   'Pre-esecuzione batch',
   aUser,
   aPgJob,
   null
  );
  return aPgExec;
 end;

 procedure logStartExecutionUpd(
  aPgExec number,
  aPgJob number,
  aNota varchar2 default null,
  aDesc varchar2 default null
 ) is
 begin
  logStartExecutionUpd(
   aPgExec,
   null,
   aPgJob,
   aNota,
   aDesc);
 end;

-- COPIATA MA CHE SCRIVE NELLE TABELLE PL/SQL TEMP

 procedure logStartExecutionUpd_TEMP(
  aPgExec number,
  aPgJob number,
  aNota varchar2 default null,
  aDesc varchar2 default null
 ) is
 begin
  logStartExecutionUpd_TEMP(
   aPgExec,
   null,
   aPgJob,
   aNota,
   aDesc);
 end;

 Procedure logStartExecutionUpd(
  aPgExec number,
  aTipo varchar2,
  aPgJob number,
  aNota varchar2 default null,
  aDesc varchar2 default null
 ) is
  aBC BATCH_CONTROL%rowtype;
  aLocTipo varchar2(20);
  PRAGMA AUTONOMOUS_TRANSACTION;
 begin
  if aTipo is null then
   aLocTipo:=IBMUTL200.TIPO_LOG_INDEFINITO;
  else
   aLocTipo:=aTipo;
  end if;
  begin
   select * into aBC from batch_control where
        pg_job = aPgJob
    and fl_attivato = 'Y';
   update batch_log_tsta set cd_log_tipo=aLocTipo, pg_batch = aBC.pg_batch, ds_log = aBC.ds_batch, note=aNota, duva=sysdate,pg_ver_rec=pg_ver_rec+1
   where
    pg_esecuzione = aPgExec;
  exception when NO_DATA_FOUND then
  -- Se il batch e dinamico utilizza i dati aDesc e aNota
   update batch_log_tsta set cd_log_tipo=aLocTipo, ds_log = aDesc, note=aNota, duva=sysdate, pg_ver_rec=pg_ver_rec+1
   where
    pg_esecuzione = aPgExec;
  end;
  commit;
 end;

-- COPIATA MA CHE SCRIVE NELLE TABELLE PL/SQL TEMP

 Procedure logStartExecutionUpd_TEMP(
  aPgExec number,
  aTipo varchar2,
  aPgJob number,
  aNota varchar2 default null,
  aDesc varchar2 default null
 ) is
  aBC BATCH_CONTROL%rowtype;
  aLocTipo varchar2(20);
  PRAGMA AUTONOMOUS_TRANSACTION;
 begin
  if aTipo is null then
   aLocTipo:=IBMUTL200.TIPO_LOG_INDEFINITO;
  else
   aLocTipo:=aTipo;
  end if;
  begin
   select * into aBC
   from batch_control where
   pg_job = aPgJob And
   fl_attivato = 'Y';

/*
   update batch_log_tsta
   set  cd_log_tipo = aLocTipo,
        pg_batch = aBC.pg_batch,
        ds_log = aBC.ds_batch,
        note = aNota,
        duva = sysdate,
        pg_ver_rec = pg_ver_rec + 1
    Where pg_esecuzione = aPgExec;
*/

   for i in 1 .. IBMUTL200.aBATCH_LOG_TSTA_TEMP.count loop
    If IBMUTL200.aBATCH_LOG_TSTA_TEMP(i).pg_esecuzione = aPgExec Then
        IBMUTL200.aBATCH_LOG_TSTA_TEMP(i).cd_log_tipo := aLocTipo;
        IBMUTL200.aBATCH_LOG_TSTA_TEMP(i).pg_batch := aBC.pg_batch;
        IBMUTL200.aBATCH_LOG_TSTA_TEMP(i).ds_log := aBC.ds_batch;
        IBMUTL200.aBATCH_LOG_TSTA_TEMP(i).note := aNota;
        IBMUTL200.aBATCH_LOG_TSTA_TEMP(i).duva := Sysdate;
        IBMUTL200.aBATCH_LOG_TSTA_TEMP(i).pg_ver_rec := IBMUTL200.aBATCH_LOG_TSTA_TEMP(i).pg_ver_rec + 1;
    End If;
   end loop;

  exception when NO_DATA_FOUND then
  -- Se il batch e dinamico utilizza i dati aDesc e aNota
/*
   update batch_log_tsta
   set  cd_log_tipo = aLocTipo,
        ds_log = aDesc,
        note = aNota,
        duva = sysdate,
        pg_ver_rec = pg_ver_rec + 1
   Where pg_esecuzione = aPgExec;
*/

   for i in 1 .. IBMUTL200.aBATCH_LOG_TSTA_TEMP.count loop
    If IBMUTL200.aBATCH_LOG_TSTA_TEMP(i).pg_esecuzione = aPgExec Then
        IBMUTL200.aBATCH_LOG_TSTA_TEMP(i).cd_log_tipo := aLocTipo;
        IBMUTL200.aBATCH_LOG_TSTA_TEMP(i).ds_log := aDesc;
        IBMUTL200.aBATCH_LOG_TSTA_TEMP(i).note := aNota;
        IBMUTL200.aBATCH_LOG_TSTA_TEMP(i).duva := Sysdate;
        IBMUTL200.aBATCH_LOG_TSTA_TEMP(i).pg_ver_rec := IBMUTL200.aBATCH_LOG_TSTA_TEMP(i).pg_ver_rec + 1;
    End If;
   end loop;

  end;
--  commit;
 end;


 function logRegisterSBatch(
  aPgBatch number
 ) return number is
  aBC BATCH_CONTROL%rowtype;
 begin
  begin
    select * into aBC from batch_control where
         pg_batch = aPgBatch
     and fl_attivato = 'N';
  exception when NO_DATA_FOUND then
    IBMERR001.RAISE_ERR_GENERICO('Il batch n.'||aPgBatch||' e gia attivo');
  end;
  return IBMUTL200.logStart(
    IBMUTL200.MSG_REG_BATCH,
    aBC.utcr,
    aBC.pg_job,
	aBC.pg_batch
  );
 end;

 function logDeregisterSBatch(
  aPgBatch number
 ) return number is
  aBC BATCH_CONTROL%rowtype;
 begin
  begin
    select * into aBC from batch_control where
         pg_batch = aPgBatch
     and fl_attivato = 'Y';
  exception when NO_DATA_FOUND then
    IBMERR001.RAISE_ERR_GENERICO('Il batch n.'||aPgBatch||' non e attivo');
  end;
  return IBMUTL200.logStart(
    IBMUTL200.MSG_DEREG_BATCH,
    aBC.utcr,
    aBC.pg_job,
	aBC.pg_batch
  );
 end;

 function logRemoveSBatch(
  aPgBatch number
 ) return number is
  aBC BATCH_CONTROL%rowtype;
 begin
  begin
    select * into aBC from batch_control where
         pg_batch = aPgBatch;
  exception when NO_DATA_FOUND then
    IBMERR001.RAISE_ERR_GENERICO('Il batch n.'||aPgBatch||' non esiste');
  end;
  return IBMUTL200.logStart(
    IBMUTL200.MSG_REMOVE_BATCH,
    aBC.utcr,
    null,
	aBC.pg_batch
  );
 end;

 function getBatchOwner(aPgBatch number) return varchar2 is
  aBC batch_control%rowtype;
 begin
  select * into aBC from BATCH_CONTROL where pg_batch = aPgBatch and fl_attivato = 'Y';
  return aBC.utente;
 end;

 function getJobOwner(aPgJob number) return varchar2 is
  aBC batch_control%rowtype;
 begin
  select * into aBC from BATCH_CONTROL where pg_job = aPgJob and fl_attivato = 'Y';
  return aBC.utente;
 end;

 -- Costruisce un wrapper di default per le procedure da chiamare via BATCH manager

 function wrapMessage(aMessage varchar2, aUser varchar2) return varchar2 is
  aProc varchar2(4000);
 begin
  aProc:=substr(aMessage,1,instr(aMessage,'(')-1);
  if(
       trim(upper(substr(aProc,instr(aProc,'.',-1)+1,4))) != 'JOB_'
  ) then
   IBMERR001.RAISE_ERR_GENERICO('Il nome della procedura non e conforme a specifica nome procedura per JOB');
  end if;
  if(
       instr(upper(aMessage),'PG_EXEC',instr(aMessage,'(')) = 0
    or instr(upper(aMessage),'JOB',instr(aMessage,'(')) = 0
    or instr(upper(aMessage),'NEXT_DATE',instr(aMessage,'(')) = 0
  ) then
   IBMERR001.RAISE_ERR_GENERICO('La procedura specificata non e conforme a specifica di JOB');
  end if;
  return 'DECLARE pg_exec number; BEGIN pg_exec:=IBMUTL210.logStartExecution(job,'''||aUser||'''); '||aMessage||' EXCEPTION WHEN OTHERS THEN ROLLBACK; IBMUTL200.logErr(pg_exec, SQLERRM(SQLCODE),DBMS_UTILITY.FORMAT_ERROR_STACK,'''||IBMUTL200.MSG_DEFAULT_HANDLER_ERRORI||'''); END;';
 end;

 procedure attivaBatch(
  aPgBatch number,
  aUser varchar2,
  partenza date default sysdate,
  intervallo varchar2 default 'null'
 ) is
  aBC batch_control%rowtype;
  aPgJob NUMBER;
  aExecNum number;
 begin
  select * into aBC from BATCH_CONTROL where pg_batch = aPgBatch for update nowait;
  begin
   for aBCTmp in (select * from batch_control where
        message = aBC.message
	and fl_attivato = 'Y')
   loop
    IBMERR001.RAISE_ERR_GENERICO('Batch gi? attivo!');
   end loop;
   aExecNum:=logRegisterSBatch(aPgBatch);
   select ibmseq00_stat_job.nextval into aPgJob from dual;
   dbms_job.isubmit(
     aPgJob,
     wrapMessage(aBC.message, aUser),
     partenza,
     ' SYSDATE + (' || intervallo || '/86400)'
   );
  exception when others then
   IBMERR001.RAISE_ERR_GENERICO('Errore di attivazione Job n.'||aPgJob||' in batch n.'||aPgBatch||' Stack: '||DBMS_UTILITY.format_error_stack);
  end;
  update BATCH_CONTROL set pg_job = aPgJob, fl_attivato = 'Y',duva=sysdate, utuv=aUser, pg_ver_rec=pg_ver_rec+1 where
   pg_batch = aBC.pg_batch;
 end;

 procedure disattivaBatch(
  aPgBatch number,
  aUser varchar2
 ) is
  aBC batch_control%rowtype;
  aExecNum number;
 begin
  select * into aBC from batch_control where pg_batch = aPgBatch for update nowait;
  if aBC.fl_attivato = 'N' then
   return;
  end if;
  begin
   dbms_job.remove(aBC.pg_job);
  end;
  aExecNum:=logDeregisterSBatch(aPgBatch);
  update BATCH_CONTROL set pg_job = null, fl_attivato = 'N', duva=sysdate, utuv=aUser, pg_ver_rec=pg_ver_rec+1 where
   pg_batch = aBC.pg_batch;
 end;

 procedure rimuoviBatch(
  aPgBatch number,
  aUser varchar2
 ) is
  aExecNum number;
 begin
  disattivaBatch(aPgBatch, aUser);
  aExecNum:=IBMUTL210.logRemoveSBatch(aPgBatch);
  delete from BATCH_CONTROL where
   pg_batch = aPgBatch;
 end;

 procedure creaBatch(
  aDesc varchar2,
  aProcedura varchar2,
  aUser varchar2,
  isExecConc char default 'N',
  partenza date default sysdate,
  intervallo varchar2 default 'null'
 ) is
  aPgBatch number;
  aTSNow date;
  aBC BATCH_CONTROL%rowtype;
  aBCTmp BATCH_CONTROL%rowtype;
  aTmp number;
 begin
  aTSNow:=sysdate;
  -- Legge il numero da assegnare al job dalla sequenza IBMSEQ00_STAT_JOB
  -- Appende il JOB
  -- Inizializza il log dei JOB con l'informazione di aggiunzione del JOB
  --
  for aBCTmp in (select * from batch_control where
        message = aProcedura) loop
    IBMERR001.RAISE_ERR_GENERICO('Il batch ? gi? presente in coda!');
  end loop;
  select ibmseq00_batch.nextval into aPgBatch from dual;
  aBC.PG_BATCH:=aPgBatch;
  aBC.DS_BATCH:=aDesc;
  aBC.PG_JOB:=null;
  aBC.MESSAGE:=aProcedura;
  aBC.FL_EXEC_CONC:=isExecConc;
  aBC.FL_ATTIVATO:='N';
  aBC.UTENTE:=aUser;
  aBC.INTERVALLO:=intervallo;
  aBC.DT_PARTENZA:=partenza;
  aBC.UTCR:=aUser;
  aBC.UTUV:=aUser;
  aBC.DACR:=aTSNow;
  aBC.DUVA:=aTSNow;
  aBC.PG_VER_REC:=1;
  IBMUTL200.ins_BATCH_CONTROL (aBC);
  attivaBatch(aPgBatch, aUser, partenza, intervallo);
 end;

 procedure creaBatchDinamico(
  aDesc varchar2,
  aProcedura varchar2,
  aUser varchar2,
  partenza date default sysdate
 ) is
   aPgJob NUMBER;
   aExecNum number;
 begin
  select ibmseq00_dyna_job.nextval into aPgJob from dual;
  if partenza is null then
   dbms_job.isubmit( -- Start a partenza e run once
      aPgJob,
      wrapMessage(aProcedura,aUser),
      sysdate);
  else
   dbms_job.isubmit( -- Start a partenza e run once
      aPgJob,
      wrapMessage(aProcedura,aUser),
      partenza);
  end if;
 exception when others then
  IBMERR001.RAISE_ERR_GENERICO('Errore di attivazione Job n.'||aPgJob||' in batch dinamico. Stack: '||DBMS_UTILITY.format_error_stack);
 end;
end;


