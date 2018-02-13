--------------------------------------------------------
--  DDL for View V_ESERCIZI
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_ESERCIZI" ("ESERCIZIO") AS 
  SELECT
--
-- Date: 13/11/2002
-- Version: 1.1
--
-- Estrae i codici degli esercizi definiti
--
-- History:
-- Date: 07/11/2001
-- Version: 1.0
-- Creazione
--
-- Date: 13/11/2002
-- Version: 1.1
-- Utilizzo tabella ESERCIZIO_BASE
--
-- Body:
--
ESERCIZIO
FROM
ESERCIZIO_BASE
;

   COMMENT ON TABLE "V_ESERCIZI"  IS 'Estrae i codici degli esercizi definiti';
