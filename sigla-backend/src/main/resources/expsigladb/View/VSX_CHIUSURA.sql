--------------------------------------------------------
--  DDL for View VSX_CHIUSURA
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VSX_CHIUSURA" ("PG_CALL", "PAR_NUM", "PROC_NAME", "MESSAGETOUSER", "CD_CDS", "CD_CDS_ORIGINE", "ESERCIZIO", "ESERCIZIO_ORI_ACC_OBB", "PG_ACC_OBB", "TI_GESTIONE", "PG_VER_REC_DOC", "TI_APPARTENENZA", "CD_ELEMENTO_VOCE", "CD_VOCE", "ESERCIZIO_RIBALTAMENTO", "UTCR", "DACR", "UTUV", "DUVA", "PG_VER_REC") AS 
  select
--
-- Date: 19/07/2006
-- Version: 1.4
--
-- Vista VSX per gestione processi massivi di annullamento,
-- riporto sul nuovo esercizio, riporto indientro sull'
-- esercizio precedente
--
-- History:
--
-- Date: 30/05/2003
-- Version: 1.0
-- Creazione
--
-- Date: 04/06/2003
-- Version: 1.1
-- Pg_acc_obb mappato su LONG1
--
-- Date: 12/06/2003
-- Version: 1.2
-- Aggiunto campo esercizio_ribaltamento
--
-- Date: 01/07/2003
-- Version: 1.3
-- Introdotta chiave elemento_voce e voce_f
-- Introdotto pg_ver_rec del documento contabile
--
-- Date: 19/07/2006
-- Version: 1.4
-- Gestione Impegni/Accertamenti Residui:
-- gestito il nuovo campo ESERCIZIO_ORIGINALE
--
-- Body:
--
 pg_call,
 par_num,
 proc_name,
 messageToUser,
 STR01,  -- CD_CDS
 STR06,  -- CD_CDS_ORIGINE
 INT01,  -- ESERCIZIO
 INT04,  -- ESERCIZIO_ORI_ACC_OBB
 LONG01, -- PG
 STR02,  -- TI_GESTIONE
 INT02,	 -- PG_VER_REC_DOC
 STR03,	 -- TI_APPARTENENZA
 STR04,	 -- CD_ELEMENTO_VOCE
 STR05,	 -- CD_VOCE
 INT03,  -- ESERCIZIO_RIBALTAMENTO
 utcr,
 dacr,
 utuv,
 duva,
 pg_ver_rec
from
 STP_CALL_EXTRA_PAR;

   COMMENT ON TABLE "VSX_CHIUSURA"  IS 'Vista VSX per gestione processi massivi di annullamento,
riporto sul nuovo esercizio, riporto indientro sull''
esercizio precedente';
