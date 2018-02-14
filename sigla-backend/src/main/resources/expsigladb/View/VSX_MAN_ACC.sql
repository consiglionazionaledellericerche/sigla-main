--------------------------------------------------------
--  DDL for View VSX_MAN_ACC
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VSX_MAN_ACC" ("PG_CALL", "PAR_NUM", "PROC_NAME", "MESSAGETOUSER", "CD_CDS", "ESERCIZIO", "PG_MANDATO", "PG_OBBLIGAZIONE", "PG_OBBLIGAZIONE_SCADENZARIO", "IM_RIGA", "PG_VER_REC_OBB_SCAD", "ESERCIZIO_ORI_OBBLIGAZIONE", "UTCR", "DACR", "UTUV", "DUVA", "PG_VER_REC") AS 
  select
--
-- Date: 18/07/2006
-- Version: 1.1
--
-- Vista VSX per gestione modifiche dei capitoli finanziari (impegni)
-- di mandati di accreditamento
--
-- History:
--
-- Date: 01/07/2003
-- Version: 1.0
-- Creazione
--
-- Date: 16/07/2003
-- Version: 1.1
-- Aggiunto PG_VER_REC_OBB_SCAD
--
-- Date: 18/07/2006
-- Version: 1.2
-- Aggiunto ESERCIZIO_ORI_OBBLIGAZIONE
--
-- Body:
--
 pg_call,
 par_num,
 proc_name,
 messageToUser,
 STR01,
 INT01,
 LONG01,
 LONG02,
 LONG03,
 DEC01,
 LONG04,
 INT02,
 utcr,
 dacr,
 utuv,
 duva,
 pg_ver_rec
from
 STP_CALL_EXTRA_PAR;

   COMMENT ON TABLE "VSX_MAN_ACC"  IS 'Vista VSX per gestione modifiche dei capitoli finanziari (impegni)
di mandati di accreditamento';
