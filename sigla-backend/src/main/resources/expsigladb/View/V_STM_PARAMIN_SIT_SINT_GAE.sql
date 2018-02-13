--------------------------------------------------------
--  DDL for View V_STM_PARAMIN_SIT_SINT_GAE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_STM_PARAMIN_SIT_SINT_GAE" ("ID_REPORT", "CHIAVE", "TIPO", "SEQUENZA", "ESERCIZIO", "CD_CDS", "UO", "CDR", "GAE") AS 
  SELECT
--
-- Date: 17/03/2008
-- Version: 1.0
--
-- Vista di memorizzazione dei parametri in input per stampa
-- della situazione sintetica per Gae
--
-- History:
--
-- Date: 17/03/2008
-- Version: 1.0
-- Creazione
--
-- Body:
--
       id,
       chiave,
       tipo,
       sequenza,
       importo_1,   -- esercizio
       attributo_1, -- cd_cds
       attributo_2, -- uo
	   attributo_3, -- cdr
	   attributo_4	-- gae
FROM   REPORT_GENERICO;
