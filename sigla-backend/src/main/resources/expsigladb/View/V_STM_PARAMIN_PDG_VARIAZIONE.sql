--------------------------------------------------------
--  DDL for View V_STM_PARAMIN_PDG_VARIAZIONE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_STM_PARAMIN_PDG_VARIAZIONE" ("ID_REPORT", "CHIAVE", "TIPO", "SEQUENZA", "PG_VARIAZIONE") AS 
  SELECT
--
-- Date: 07/07/2006
-- Version: 1.0
--
-- Vista di memorizzazione dei parametri in input per stampa
-- delle variazioni al PDG
--
-- History:
--
-- Date: 07/07/2006
-- Version: 1.0
-- Creazione
--
-- Body:
--
       id,
       chiave,
       tipo,
       sequenza,
       importo_1 -- PG_VARIAZIONE
FROM   REPORT_GENERICO
;
