--------------------------------------------------------
--  DDL for View PRT_VPG_BIL_RICLASSIFICATO
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "PRT_VPG_BIL_RICLASSIFICATO" ("ID", "CHIAVE", "TIPO", "SEQUENZA", "ORDINE", "CONTO_RICLASS", "I_LIVELLO", "II_LIVELLO", "III_LIVELLO", "IV_LIVELLO", "DESCRIZIONE", "PARZIALE_I_ANNO", "TOTALE_I_ANNO", "PARZIALE_II_ANNO", "TOTALE_II_ANNO", "SN_TOTALE") AS 
  select

-- Date: 06/08/2004
-- Version: 1.1
--
-- Vista di stampa Bilancio Economico-Patrimoniale Riclassificato (comune a
-- Stato Patrimoniale e Conto Economico)
--
-- History:
--
-- Date: 28/07/2004
-- Version: 1.0
-- Creazione
--
-- Date: 06/08/2004
-- Version: 1.1
-- Modificati il nome della view e della procedure, eliminato il nome dello
-- schema, aggiunto history nella procedure
--
-- Body:
ID,	CHIAVE,	TIPO,	SEQUENZA,
IMPORTO_1,	-- ORDINE,
ATTRIBUTO_1,	-- COD_CONTO_RICLASSIFICATO
ATTRIBUTO_2,	-- I_LIVELLO,
ATTRIBUTO_3,	-- II_LIVELLO,
ATTRIBUTO_4,	-- III_LIVELLO,
ATTRIBUTO_5,	-- III_LIVELLO,
ATTRIBUTO_6,	-- DESCRIZIONE,
IMPORTO_2,	-- PARZIALE_I_ANNO,
IMPORTO_3,	-- TOTALE_I_ANNO,
IMPORTO_4,	-- PARZIALE_II_ANNO,
IMPORTO_5,	-- TOTALE_II_ANNO
ATTRIBUTO_7     -- SN_TOTALE
from tmp_report_generico
;

   COMMENT ON TABLE "PRT_VPG_BIL_RICLASSIFICATO"  IS 'Vista di stampa Bilancio Economico-Patrimoniale Riclassificato (comune a
Stato Patrimoniale e Conto Economico)';
