--------------------------------------------------------
--  DDL for View VSX_REINTEGRO_FONDO
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VSX_REINTEGRO_FONDO" ("PG_CALL", "PAR_NUM", "PROC_NAME", "MESSAGETOUSER", "CD_CDS", "CD_UO", "ESERCIZIO", "CD_CODICE_FONDO", "PG_FONDO_SPESA", "UTCR", "DACR", "UTUV", "DUVA", "PG_VER_REC") AS 
  select
--
-- Date: 20/05/2002
-- Version: 1.1
--
-- Vista VSX per per gestione passaggio parametri a stpred procedure per reintegro fondo economale
--
-- History:
--
-- Date: 14/05/2002
-- Version: 1.0
-- Creazione
--
-- Date: 20/05/2002
-- Version: 1.1
-- Fix errore
--
-- Body:
--
 pg_call,
 par_num,
 proc_name,
 messageToUser,
 str01,
 str02,
 int01,
 str03,
 long01,
 utcr,
 dacr,
 utuv,
 duva,
 pg_ver_rec
from
 STP_CALL_EXTRA_PAR
;

   COMMENT ON TABLE "VSX_REINTEGRO_FONDO"  IS 'Vista VSX per per gestione passaggio parametri a stpred procedure per reintegro fondo economale';
