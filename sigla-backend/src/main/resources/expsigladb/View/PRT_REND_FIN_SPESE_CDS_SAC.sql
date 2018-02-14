--------------------------------------------------------
--  DDL for View PRT_REND_FIN_SPESE_CDS_SAC
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "PRT_REND_FIN_SPESE_CDS_SAC" ("ESERCIZIO", "CDS", "RUBRICA", "DS_RUBRICA", "ARTICOLO", "DS_ARTICOLO", "CAPITOLO", "DS_CAPITOLO", "INIZIALE", "VAR_PIU", "VAR_MENO", "ASSESTATO", "PAGAMENTI", "IN_PIU", "IN_MENO") AS 
  SELECT "ESERCIZIO","CDS","RUBRICA","DS_RUBRICA","ARTICOLO","DS_ARTICOLO","CAPITOLO","DS_CAPITOLO","INIZIALE","VAR_PIU","VAR_MENO","ASSESTATO","PAGAMENTI","IN_PIU","IN_MENO"
--
-- Date: 27/10/2003
-- Version: 1.0
--
-- Vista di stampa Rendiconto Finanziario CDS Spese per articolo, SOLO SAC (solo importi diversi da zero)
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
FROM
PRT_REND_FIN_SPESE_CDS_SAC_ALL
WHERE
(INIZIALE+VAR_PIU+VAR_MENO+PAGAMENTI)<>0
;

   COMMENT ON TABLE "PRT_REND_FIN_SPESE_CDS_SAC"  IS 'Vista di stampa Rendiconto Finanziario CDS Spese per articolo, SOLO SAC (solo importi diversi da zero)';
