--------------------------------------------------------
--  DDL for View VPG_FONDO_ECONOMALE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VPG_FONDO_ECONOMALE" ("ID", "CHIAVE", "SEQUENZA", "DESCRIZIONE", "CD_CDS", "ESERCIZIO", "CD_UNITA_ORGANIZZATIVA", "CD_CODICE_FONDO", "TI_RECORD_L1", "DS_FONDO", "DS_UNITA_ORGANIZZATIVA", "DENOMINAZIONE_SEDE", "CD_TERZO", "PG_FONDO_SPESA", "DT_SPESA", "DS_SPESA", "FL_FORNITORE_SALTUARIO", "DENOMINAZIONE_FORNITORE", "FL_DOCUMENTATA", "CD_CDS_OBBLIGAZIONE", "ESERCIZIO_OBBLIGAZIONE", "ESERCIZIO_ORI_OBBLIGAZIONE", "PG_OBBLIGAZIONE", "PG_OBBLIGAZIONE_SCAD", "CD_VOCE", "CD_TIPO_DOC_AMM", "CD_CDS_DOC_AMM", "CD_UO_DOC_AMM", "ESERCIZIO_DOC_AMM", "PG_DOC_AMM", "FL_REINTEGRATA", "PG_MANDATO_FEC", "DS_MANDATO", "IM_ENTRATE", "IM_SPESE", "IM_RESIDUO", "PG_MANDATO_FSP", "FL_APERTO", "PG_REVERSALE", "DT_EMISSIONE_REV", "DS_REVERSALE", "IM_REVERSALE", "IM_NETTO_SPESA", "UTCR", "DACR") AS 
  SELECT
--
-- Date: 18/07/2006
-- Version: 1.2
--
-- Vista per la stampa del fondo economale
-- Protocollo VPG
--
-- History:
--
-- Date: 26/02/2003
-- Version: 1.0
-- Creazione
--
-- Date: 28/02/2003
-- Version: 1.1
-- Eliminazione del campo TI_RECORD_L1 di tipo ATTRIBUTO_4
--
-- Date: 18/07/2006
-- Version: 1.2
-- Gestione Impegni/Accertamenti Residui:
-- aggiornata la funzione per tener conto anche del campo Esercizio Originale Impegno/Accertamento
--
-- Body:
--
ID,
CHIAVE,
SEQUENZA,
DESCRIZIONE,
ATTRIBUTO_1,        -- cd_cds
IMPORTO_1,	   		-- esercizio
ATTRIBUTO_2,   		-- cd_unita_organizzativa
ATTRIBUTO_3,  		-- cd_codice_fondo
TIPO,		  		-- ti_record L1
ATTRIBUTO_LONG_1,   -- ds_fondo
ATTRIBUTO_LONG_2,   -- ds_unita_organizzativa
ATTRIBUTO_4,   		-- denominazione_sede
IMPORTO_2,	   		-- cd_terzo
IMPORTO_3,	   		-- pg_fondo_spesa
DATA_1,		   		-- dt_spesa
ATTRIBUTO_LONG_3,   -- ds_spesa
ATTRIBUTO_5,   		-- fl_fornitore_saltuario
ATTRIBUTO_6,   		-- denominazione_fornitore
ATTRIBUTO_7,  		-- fl_documentata
ATTRIBUTO_8,  		-- cd_cds_obbligazione
IMPORTO_4,	   		-- esercizio_obbligazione
IMPORTO_17,	   		-- esercizio_ori_obbligazione
IMPORTO_5,	   		-- pg_obbligazione
IMPORTO_6,	   		-- pg_obbligazione_scad
ATTRIBUTO_9,  		-- cd_voce
ATTRIBUTO_10,  		-- cd_tipo_doc_amm
ATTRIBUTO_11,  		-- cd_cds_doc_amm
ATTRIBUTO_12,  		-- cd_uo_doc_amm
IMPORTO_7,	   		-- esercizio_doc_amm
IMPORTO_8,	   		-- pg_doc_amm
ATTRIBUTO_13,  		-- fl_reintegrata
IMPORTO_9,	   		-- pg_mandato_fec
ATTRIBUTO_LONG_4,  	-- ds_mandato
IMPORTO_10,	   		-- im_entrate
IMPORTO_11,	   		-- im_spese
IMPORTO_12,	   		-- im_residuo
IMPORTO_13,	   		-- pg_mandato_fsp
ATTRIBUTO_14,  		-- fl_aperto
IMPORTO_14,	   		-- pg_reversale
DATA_2,		   		-- dt_emissione_rev
ATTRIBUTO_LONG_5,  	-- ds_reversale
IMPORTO_15,	   		-- im_reversale
IMPORTO_16,	   		-- im_netto_spesa
ATTRIBUTO_15,		-- utcr
DATA_3				-- dacr
from TMP_REPORT_GENERICO trp;

   COMMENT ON TABLE "VPG_FONDO_ECONOMALE"  IS 'Vista per la stampa del fondo economale
Protocollo VPG';
