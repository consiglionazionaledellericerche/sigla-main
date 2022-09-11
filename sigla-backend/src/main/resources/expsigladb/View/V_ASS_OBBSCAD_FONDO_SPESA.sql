--------------------------------------------------------
--  DDL for View V_ASS_OBBSCAD_FONDO_SPESA
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_ASS_OBBSCAD_FONDO_SPESA" ("CD_CDS", "ESERCIZIO", "CD_UNITA_ORGANIZZATIVA", "CD_CODICE_FONDO", "PG_FONDO_SPESA", "DS_SPESA", "DT_SPESA", "IM_AMMONTARE_SPESA", "IM_NETTO_SPESA", "FL_DOCUMENTATA", "FL_REINTEGRATA", "FL_FORNITORE_SALTUARIO", "CODICE_FISCALE", "PARTITA_IVA", "DENOMINAZIONE_FORNITORE", "INDIRIZZO_FORNITORE", "DS_FORNITORE", "TEL_FORNITORE", "CAP_FORNITORE", "PG_COMUNE", "CD_TERZO", "CD_TIPO_DOCUMENTO_AMM", "CD_CDS_DOC_AMM", "CD_UO_DOC_AMM", "ESERCIZIO_DOC_AMM", "PG_DOCUMENTO_AMM", "FL_OBBLIGAZIONE", "CD_CDS_OBBLIGAZIONE", "ESERCIZIO_OBBLIGAZIONE", "ESERCIZIO_ORI_OBBLIGAZIONE", "PG_OBBLIGAZIONE", "PG_OBBLIGAZIONE_SCADENZARIO", "CD_CDS_MANDATO", "ESERCIZIO_MANDATO", "PG_MANDATO", "DACR", "UTCR", "DUVA", "UTUV", "PG_VER_REC", "DT_A_COMPETENZA_COGE", "DT_DA_COMPETENZA_COGE", "IN_CD_CDS_OBBLIGAZIONE", "IN_ESERCIZIO_OBBLIGAZIONE", "IN_ESERCIZIO_ORI_OBBLIGAZIONE", "IN_PG_OBBLIGAZIONE", "IN_PG_OBBLIGAZIONE_SCADENZARIO") AS 
  SELECT
--==============================================================================
--
-- Date: 18/07/2006
-- Version: 1.2
--
-- Vista di estrazione delle spese di un fondo a partire dal riferimento alla
-- scadenza di obbligazione
--
-- History:
--
-- Date: 11/06/2002
-- Version: 1.0
-- Creazione vista
--
-- Date: 23/12/2002
-- Version: 1.1
-- Aggiunto campo netto spesa fondo
--
-- Date: 18/07/2006
-- Version: 1.2
-- Gestione Impegni/Accertamenti Residui:
-- gestito il nuovo campo ESERCIZIO_ORIGINALE
--
-- Body:
--
--==============================================================================
       A.cd_cds,
       A.esercizio,
       A.cd_unita_organizzativa,
       A.cd_codice_fondo,
       A.pg_fondo_spesa,
       A.ds_spesa,
       A.dt_spesa,
       A.im_ammontare_spesa,
	   A.IM_NETTO_SPESA,
       A.fl_documentata,
       A.fl_reintegrata,
       A.fl_fornitore_saltuario,
       A.codice_fiscale,
       A.partita_iva,
       A.denominazione_fornitore,
       A.indirizzo_fornitore,
       A.ds_fornitore,
       A.tel_fornitore,
       A.cap_fornitore,
       A.pg_comune,
       A.cd_terzo,
       A.cd_tipo_documento_amm,
       A.cd_cds_doc_amm,
       A.cd_uo_doc_amm,
       A.esercizio_doc_amm,
       A.pg_documento_amm,
       A.fl_obbligazione,
       A.cd_cds_obbligazione,
       A.esercizio_obbligazione,
       A.esercizio_ori_obbligazione,
       A.pg_obbligazione,
       A.pg_obbligazione_scadenzario,
       A.cd_cds_mandato,
       A.esercizio_mandato,
       A.pg_mandato,
       A.dacr,
       A.utcr,
       A.duva,
       A.utuv,
       A.pg_ver_rec,
       A.dt_a_competenza_coge,
       A.dt_da_competenza_coge,
       A.cd_cds_obbligazione,
       A.esercizio_obbligazione,
       A.esercizio_ori_obbligazione,
       A.pg_obbligazione,
       A.pg_obbligazione_scadenzario
FROM   FONDO_SPESA A
UNION ALL
SELECT A.cd_cds,
       A.esercizio,
       A.cd_unita_organizzativa,
       A.cd_codice_fondo,
       A.pg_fondo_spesa,
       A.ds_spesa,
       A.dt_spesa,
       A.im_ammontare_spesa,
	   A.IM_NETTO_SPESA,
       A.fl_documentata,
       A.fl_reintegrata,
       A.fl_fornitore_saltuario,
       A.codice_fiscale,
       A.partita_iva,
       A.denominazione_fornitore,
       A.indirizzo_fornitore,
       A.ds_fornitore,
       A.tel_fornitore,
       A.cap_fornitore,
       A.pg_comune,
       A.cd_terzo,
       A.cd_tipo_documento_amm,
       A.cd_cds_doc_amm,
       A.cd_uo_doc_amm,
       A.esercizio_doc_amm,
       A.pg_documento_amm,
       A.fl_obbligazione,
       A.cd_cds_obbligazione,
       A.esercizio_obbligazione,
       A.esercizio_ori_obbligazione,
       A.pg_obbligazione,
       A.pg_obbligazione_scadenzario,
       A.cd_cds_mandato,
       A.esercizio_mandato,
       A.pg_mandato,
       A.dacr,
       A.utcr,
       A.duva,
       A.utuv,
       A.pg_ver_rec,
       A.dt_a_competenza_coge,
       A.dt_da_competenza_coge,
       B.cd_cds_obbligazione,
       B.esercizio_obbligazione,
       B.esercizio_ori_obbligazione,
       B.pg_obbligazione,
       B.pg_obbligazione_scadenzario
FROM   FONDO_SPESA A,
       V_DOC_PASSIVO B
WHERE  B.dt_pagamento_fondo_eco IS NOT NULL AND
       B.cd_cds = A.cd_cds_doc_amm AND
       B.esercizio = A.esercizio_doc_amm AND
       B.cd_unita_organizzativa = A.cd_uo_doc_amm AND
       B.cd_tipo_documento_amm = A.cd_tipo_documento_amm AND
       B.pg_documento_amm = A.pg_documento_amm
       and b.cd_tipo_documento_amm != 'ORDINE';

   COMMENT ON TABLE "V_ASS_OBBSCAD_FONDO_SPESA"  IS 'Vista di estrazione delle spese di un fondo a partire dal riferimento alla
scadenza di obbligazione';
