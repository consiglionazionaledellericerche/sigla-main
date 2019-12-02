--------------------------------------------------------
--  DDL for View PRT_VPG_SIT_CASSA
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "PRT_VPG_SIT_CASSA" ("ID", "CHIAVE", "TIPO", "SEQUENZA", "ORDINE", "CDS", "DESCRIZIONE", "IMPORTO_PARZ", "TOTALE", "FL_TOTALE", "SEGNO") AS 
  select
-- Date: 06/08/2004
-- Version: 1.1
--
-- Vista di stampa situazione cassa
--
-- History:
--
-- Date: 28/07/2004
-- Version: 1.0
-- Creazione
--
-- Date: 06/08/2004
-- Version: 1.1
-- Rinominati il nome del package e della procedure che lo richiama
--
-- Body:
ID,	CHIAVE,	TIPO,	SEQUENZA,
IMPORTO_1,	-- ORDINE
ATTRIBUTO_1,	-- CDS
ATTRIBUTO_2,	-- descrizione
IMPORTO_2,	-- importo PARZIALE
IMPORTO_3,	-- importo TOTALE
ATTRIBUTO_3,	-- TOTALE SI/NO
ATTRIBUTO_4     -- SEGNO
from tmp_report_generico
;

   COMMENT ON TABLE "PRT_VPG_SIT_CASSA"  IS 'Vista di stampa situazione cassa';
