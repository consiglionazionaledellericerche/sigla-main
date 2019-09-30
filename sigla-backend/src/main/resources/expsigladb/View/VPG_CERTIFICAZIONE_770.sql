--------------------------------------------------------
--  DDL for View VPG_CERTIFICAZIONE_770
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VPG_CERTIFICAZIONE_770" ("ID", "CHIAVE", "TIPO", "SEQUENZA", "ESERCIZIO", "TI_MODELLO", "CD_ANAG", "NOTA", "CD_QUADRO", "DS_QUADRO", "TI_RITENUTA", "NOME", "COGNOME", "RAGIONE_SOCIALE", "VIA_NUM_FISCALE", "CAP_COMUNE_FISCALE", "FRAZIONE_FISCALE", "DS_COMUNE_FISCALE", "DS_PROVINCIA_FISCALE", "CD_PROVINCIA_FISCALE", "DS_NAZIONE_FISCALE", "DT_NASCITA", "DS_COMUNE_NASCITA", "DS_PROVINCIA_NASCITA", "CD_PROVINCIA_NASCITA", "DS_NAZIONE_NASCITA", "CODICE_FISCALE", "PARTITA_IVA", "CD_TI_COMPENSO", "DS_TI_COMPENSO", "IM_LORDO", "IM_NON_SOGG_RIT", "IMPONIBILE_FI", "IMPONIBILE_PR", "ALIQUOTA", "IM_RITENUTE", "IM_NON_SOGG_CORI", "IM_CONTRIBUTI", "IM_CONTRIBUTI_ENTE", "IM_NETTO", "IM_NON_SOGG_INPS", "TI_ENTITA", "TI_SESSO", "ID_FISCALE_ESTERO", "CD_NAZIONE_770", "CD_TRATTAMENTO", "CF_PI_PIGNORATO") AS 
  SELECT
--
-- Date: 13/07/2004
-- Version: 1.0
--
-- Vista per la gestione del 770
--
-- History:
--
-- Date: 13/07/2004
-- Version: 1.0
-- Creazione
--
-- Date: 16/07/2005
-- Version: 1.1
-- Aggiunta colonna IM_CONTRIBUTI_ENTE per adeguamento 770/2004
--
-- Date: 20/09/2006
-- Version: 1.2
-- Aggiunta colonna IM_NON_SOGG_INPS per adeguamento 770/2006
--
-- Date: 11/07/2011
-- Version: 1.3
-- Gestione dei due modelli (semplificato ed ordinario) e dei relativi quadri
--
-- Date: 19/07/2012
-- Version: 1.3
-- Gestione del nuovo quadro SY con l'introduzione del terzo pignorato
--
-- Body:
--
ID,
CHIAVE,
TIPO,
SEQUENZA,
IMPORTO_1,     		  -- esercizio,
ATTRIBUTO_1,  		  -- ti_modello,
IMPORTO_2,	  	  -- cd_anag,
ATTRIBUTO_LONG_1,	  -- nota,
ATTRIBUTO_25,       -- quadro
ATTRIBUTO_LONG_2,	  -- ds_quadro,
ATTRIBUTO_26,       -- ti_ritenuta
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
DATA_1,			  -- dt_nascita,
ATTRIBUTO_11,		  -- ds_comune_nascita,
ATTRIBUTO_12,		  -- ds_provincia_nascita,
ATTRIBUTO_13,		  -- cd_provincia_nascita,
ATTRIBUTO_18,		  -- ds_nazione_nascita
ATTRIBUTO_14,		  -- codice_fiscale,
ATTRIBUTO_15,		  -- partita_iva,
ATTRIBUTO_16,		  -- cd_ti_compenso,
ATTRIBUTO_17,		  -- ds_ti_compenso,
IMPORTO_3,		  -- im_lordo,
IMPORTO_4,		  -- im_non_sogg_rit,
IMPORTO_9,		  -- imponibile_fi,
IMPORTO_10,		  -- imponibile_pr
IMPORTO_5,		  -- aliquota,
IMPORTO_6,		  -- im_ritenute,
IMPORTO_11,		  -- im_non_sogg_cori
IMPORTO_7,		  -- im_contributi,
IMPORTO_12,		  -- im_contributi_ente,
IMPORTO_8,		  -- im_netto,
IMPORTO_13,		  -- im_non_sogg_inps,
ATTRIBUTO_20,		  -- cd_ti_entita,
ATTRIBUTO_21,		  -- cd_ti_sesso,
ATTRIBUTO_22,		  -- cd_id_codice_fiscale_estero,
ATTRIBUTO_23,		  -- cd_nazione 770,
ATTRIBUTO_24,		  -- cd_trattamento,
ATTRIBUTO_27	  	-- cf_pi_pignorato
from tmp_report_generico;
