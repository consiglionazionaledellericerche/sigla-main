--------------------------------------------------------
--  DDL for View VP_BATCH_LOG
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VP_BATCH_LOG" ("PG_ESECUZIONE", "PG_BATCH", "PG_JOB", "FL_ERRORI", "DS_LOG", "NOTE_TESTATA", "DUVA", "DACR", "UTCR", "UTUV", "PG_RIGA", "TI_MESSAGGIO", "MESSAGGIO", "TRACE", "NOTE_RIGA") AS 
  select
--
-- Date: 03/11/2002
-- Version: 1.0
--
-- Vista di stampa dei logs di batc applicativi
--
-- History:
--
-- Date: 03/11/2002
-- Version: 1.0
-- Creazione
--
-- Body:
--
 a.pg_esecuzione,
 a.pg_batch,
 a.pg_job,
 a.fl_errori,
 a.ds_log,
 a.note,
 a.duva,
 a.dacr,
 a.utcr,
 a.utuv,
 b.pg_riga,
 b.ti_messaggio,
 b.messaggio,
 b.trace,
 b.note
from batch_log_tsta a, batch_log_riga b where
 b.pg_esecuzione = a.pg_esecuzione
;

   COMMENT ON TABLE "VP_BATCH_LOG"  IS 'Vista di stampa dei logs di batc applicativi';
