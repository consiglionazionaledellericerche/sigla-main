--------------------------------------------------------
--  DDL for View V_STM_PARAMIN_FT_ATTIVA
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_STM_PARAMIN_FT_ATTIVA" ("ID_REPORT", "GRUPPO", "TIPOLOGIA_RIGA", "SEQUENZA", "DESCRIZIONE", "CD_CDS", "CD_UO", "ESERCIZIO", "PG_FATTURA_ATTIVA") AS 
  SELECT
-- =================================================================================================
--
-- Date: 28/11/2002
-- Version: 1.0
--
-- Vista di memorizzazione dei parametri in input per stampa fattura attiva
--
-- History:
--
-- Date: 28/11/2002
-- Version: 1.0
--
-- Creazione vista
--
-- Body:
--
-- =================================================================================================
       id,
       chiave,
       tipo,
       sequenza,
       descrizione,
       attributo_1,
       attributo_2,
       importo_1,
       importo_2
FROM   REPORT_GENERICO
;
