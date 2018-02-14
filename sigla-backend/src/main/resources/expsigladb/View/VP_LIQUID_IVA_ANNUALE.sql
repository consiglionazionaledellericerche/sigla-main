--------------------------------------------------------
--  DDL for View VP_LIQUID_IVA_ANNUALE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VP_LIQUID_IVA_ANNUALE" ("ID", "CHIAVE", "TIPO", "SEQUENZA", "ESERCIZIO", "DS_MESE", "CD_VOCE_IVA", "DS_VOCE_IVA", "IMPONIBILE", "IM_IVA", "IM_TOTALE") AS 
  SELECT
--
-- Date: 27/11/2003
-- Version: 1.0
--
-- Vista per la stampa e visualizzazione on line delle
-- estrazioni annuali IVA:
--    - liquidazione iva
--    - tabella codici IVA fatture acquisti
-- 	  - tabella riepilogativa quadri IVA acquisti: VA-VF
-- 	  - tabella riepilogativa quadri IVA vendite: VA-VE
-- 	  - tabella codici IVA fatture emesse
--
-- History:
--
-- Date: 27/11/2003
-- Version: 1.0
-- Creazione
--
-- Body:
--
ID,
CHIAVE,
TIPO,
SEQUENZA,
importo_1,      -- esercizio,
attributo_3,	-- ds_mese
attributo_1,	-- cd_voce_iva,
attributo_2,	-- ds_voce_iva,
importo_2,		-- imponibile,
importo_3,		-- im_iva,
importo_4		-- im_totale
from report_generico
;

   COMMENT ON TABLE "VP_LIQUID_IVA_ANNUALE"  IS 'Vista per la stampa e visualizzazione on line delle
estrazioni annuali IVA:
    - liquidazione iva
    - tabella codici IVA fatture acquisti
 	- tabella riepilogativa quadri IVA acquisti: VA-VF
	- tabella riepilogativa quadri IVA vendite: VA-VE
 	- tabella codici IVA fatture emesse';
