--------------------------------------------------------
--  DDL for Package CNRMAT001
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRMAT001" as
--
-- CNRMAT001 - Package di aggiornamento delle VIEW MATERIALIZZATE
-- Eliminate le view materializzate ora si esegue solo la cancellazione di BATCH_LOG_RIGA
-- Date: 05/01/2007
-- Version: 1.1
-- Date: 21/10/2005
-- Version: 1.0
-- Creazione
 Procedure job_deleteBatch_log_riga(aJob number, aPg_exec number, aNext_date DATE);
 TIPO_LOG_DEL_BATCH CONSTANT VARCHAR2(20):='LOG_DEL_BATCH';
End;
