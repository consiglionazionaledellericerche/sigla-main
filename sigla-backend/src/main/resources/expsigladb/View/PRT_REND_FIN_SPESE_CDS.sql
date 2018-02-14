--------------------------------------------------------
--  DDL for View PRT_REND_FIN_SPESE_CDS
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "PRT_REND_FIN_SPESE_CDS" ("ESERCIZIO", "CDS", "PARTE", "DS_PARTE", "TITOLO", "DS_TITOLO", "SEZIONE", "DS_SEZIONE", "RUBRICA", "DS_RUBRICA", "CAPITOLO", "DS_CAPITOLO", "INIZIALE", "VAR_PIU", "VAR_MENO", "ASSESTATO", "PAGAMENTI", "IN_PIU", "IN_MENO") AS 
  SELECT  "ESERCIZIO","CDS","PARTE","DS_PARTE","TITOLO","DS_TITOLO","SEZIONE","DS_SEZIONE","RUBRICA","DS_RUBRICA","CAPITOLO","DS_CAPITOLO","INIZIALE","VAR_PIU","VAR_MENO","ASSESTATO","PAGAMENTI","IN_PIU","IN_MENO"
--
-- Date: 27/10/2003
-- Version: 1.0
--
-- Vista di stampa Rendiconto Finanziario CDS Spese (solo importi DIVERSI DA  zero)
--
-- History:
--
-- Date: 27/10/2003
-- Version: 1.0
-- Creazione
-- (effettuate alcune modifiche per ottimizzazione-Cineca)
--
-- Body
--
FROM PRT_REND_FIN_SPESE_CDS_ALL
WHERE
(INIZIALE+VAR_PIU+VAR_MENO+PAGAMENTI)<>0
;

   COMMENT ON TABLE "PRT_REND_FIN_SPESE_CDS"  IS 'Vista di stampa Rendiconto Finanziario CDS Spese (solo importi DIVERSI DA  zero)';
