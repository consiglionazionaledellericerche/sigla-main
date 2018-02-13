--------------------------------------------------------
--  DDL for View PRT_REND_FIN_ENTRATE_CDS
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "PRT_REND_FIN_ENTRATE_CDS" ("ESERCIZIO", "CDS", "TITOLO", "DS_TITOLO", "CAPITOLO", "DS_CAPITOLO", "INIZIALE", "VAR_PIU", "VAR_MENO", "ASSESTATO", "RISCOSSIONI", "IN_PIU", "IN_MENO", "INIZ_CASSA", "VAR_PIU_CASSA", "VAR_MENO_CASSA", "ASSEST_CASSA") AS 
  SELECT   "ESERCIZIO","CDS","TITOLO","DS_TITOLO","CAPITOLO","DS_CAPITOLO","INIZIALE","VAR_PIU","VAR_MENO","ASSESTATO","RISCOSSIONI","IN_PIU","IN_MENO","INIZ_CASSA","VAR_PIU_CASSA","VAR_MENO_CASSA","ASSEST_CASSA"
--
-- Date: 27/10/2003
-- Version: 1.0
--
-- Vista di stampa Rendiconto Finanziario CDS Entrate (solo importi DIVERSI DA  zero)
--
-- History:
--
-- Date: 27/10/2003
-- Version: 1.0
-- Creazione
--(effettuate alcune modifiche per ottimizzazione-Cineca)
--
-- Body
--
FROM  PRT_REND_FIN_ENTRATE_CDS_ALL
WHERE
(INIZIALE+VAR_PIU+VAR_MENO+RISCOSSIONI)<>0
;

   COMMENT ON TABLE "PRT_REND_FIN_ENTRATE_CDS"  IS 'Vista di stampa Rendiconto Finanziario CDS Entrate (solo importi DIVERSI DA  zero)';
