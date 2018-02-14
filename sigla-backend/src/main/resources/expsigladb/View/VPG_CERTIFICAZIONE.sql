--------------------------------------------------------
--  DDL for View VPG_CERTIFICAZIONE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VPG_CERTIFICAZIONE" ("ID", "CHIAVE", "TIPO", "SEQUENZA", "ESERCIZIO", "TI_CERTIFICAZIONE", "CD_ANAG", "NOTA", "DS_CERTIFICAZIONE", "NOME", "COGNOME", "RAGIONE_SOCIALE", "VIA_NUM_FISCALE", "CAP_COMUNE_FISCALE", "FRAZIONE_FISCALE", "DS_COMUNE_FISCALE", "DS_PROVINCIA_FISCALE", "CD_PROVINCIA_FISCALE", "DS_NAZIONE_FISCALE", "DT_NASCITA", "DS_COMUNE_NASCITA", "DS_PROVINCIA_NASCITA", "CD_PROVINCIA_NASCITA", "DS_NAZIONE_NASCITA", "CODICE_FISCALE", "PARTITA_IVA", "CD_TI_COMPENSO", "DS_TI_COMPENSO", "IM_LORDO", "IM_NON_SOGG_RIT", "IMPONIBILE_FI", "IMPONIBILE_PR", "ALIQUOTA", "IM_RITENUTE", "IM_NON_SOGG_CORI", "IM_CONTRIBUTI", "IM_NETTO") AS 
  SELECT
--
-- Date: 03/02/2004
-- Version: 1.2
--
-- Vista per la stampa delle certificazioni dei compensi
-- Protocollo VPG
--
-- History:
--
-- Date: 26/01/2004
-- Version: 1.0
-- Creazione
--
-- Date: 29/01/2004
-- Version: 1.1
-- Modifiche per gestione separata cori FI e PR, e trattamenti privi
-- dell'uno e dell'altro
--
-- Date: 03/02/2004
-- Version: 1.2
-- Aggiunta colonna im_non_sogg_cori
--
-- Body:
--
ID,
CHIAVE,
TIPO,
SEQUENZA,
IMPORTO_1,     		  -- esercizio,
ATTRIBUTO_1,  		  -- ti_certificazione,
IMPORTO_2,	  		  -- cd_anag,
ATTRIBUTO_LONG_1,	  -- nota,
ATTRIBUTO_LONG_2,	  -- ds_certificazione,
ATTRIBUTO_2,		  -- nome,
ATTRIBUTO_3,		  -- cognome,
ATTRIBUTO_4,		  -- ragione_sociale,
ATTRIBUTO_5,		  -- via_fiscale||num_civico_fiscale,
ATTRIBUTO_6,		  -- cap_comune_fiscale,
ATTRIBUTO_7,		  -- frazione_fiscale,
ATTRIBUTO_8,		  -- ds_comune_fiscale,
ATTRIBUTO_9,		  -- ds_provincia_fiscale,
ATTRIBUTO_10,		  -- cd_provincia_fiscale,
ATTRIBUTO_19,		  -- ds_nazione_fiscale
DATA_1,				  -- dt_nascita,
ATTRIBUTO_11,		  -- ds_comune_nascita,
ATTRIBUTO_12,		  -- ds_provincia_nascita,
ATTRIBUTO_13,		  -- cd_provincia_nascita,
ATTRIBUTO_18,		  -- ds_nazione_nascita
ATTRIBUTO_14,		  -- codice_fiscale,
ATTRIBUTO_15,		  -- partita_iva,
ATTRIBUTO_16,		  -- cd_ti_compenso,
ATTRIBUTO_17,		  -- ds_ti_compenso,
IMPORTO_3,			  -- im_lordo,
IMPORTO_4,			  -- im_non_sogg_rit,
IMPORTO_9,			  -- imponibile_fi,
IMPORTO_10,			  -- imponibile_pr
IMPORTO_5,			  -- aliquota,
IMPORTO_6,			  -- im_ritenute,
IMPORTO_11,			  -- im_non_sogg_cori
IMPORTO_7,			  -- im_contributi,
IMPORTO_8			  -- im_netto
from tmp_report_generico
;

   COMMENT ON TABLE "VPG_CERTIFICAZIONE"  IS 'Vista per la stampa delle certificazioni dei compensi
Protocollo VPG';
