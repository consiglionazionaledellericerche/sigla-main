--------------------------------------------------------
--  DDL for View PRT_VPG_SIT_CASSA_PARIFICA
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "PRT_VPG_SIT_CASSA_PARIFICA" ("ID", "CHIAVE", "TIPO", "SEQUENZA", "ORDINE", "CDS", "DESCRIZIONE", "IMPORTO_PARZ", "TOTALE", "FL_TOTALE", "SEGNO") AS 
  select
-- Date: 18/03/2008
-- Version: 1.1
--
-- Vista di stampa situazione cassa per parifica con BNL di una giornata
--
-- History:
--
-- Date: 18/03/2008
-- Version: 1.0
-- Creazione
--
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
from tmp_report_generico;

   COMMENT ON TABLE "PRT_VPG_SIT_CASSA_PARIFICA"  IS 'Vista di stampa situazione cassa per parifica della giornaliera';
