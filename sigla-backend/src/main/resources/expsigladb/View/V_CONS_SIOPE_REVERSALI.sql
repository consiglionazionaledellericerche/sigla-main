--------------------------------------------------------
--  DDL for View V_CONS_SIOPE_REVERSALI
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_CONS_SIOPE_REVERSALI" ("CD_CDS", "ESERCIZIO", "PG_REVERSALE", "ESERCIZIO_ACCERTAMENTO", "ESERCIZIO_ORI_ACCERTAMENTO", "PG_ACCERTAMENTO", "PG_ACCERTAMENTO_SCADENZARIO", "CD_CDS_DOC_AMM", "CD_UO_DOC_AMM", "ESERCIZIO_DOC_AMM", "CD_TIPO_DOCUMENTO_AMM", "DS_TIPO_DOC_AMM", "PG_DOC_AMM", "ESERCIZIO_SIOPE", "TI_GESTIONE", "CD_SIOPE", "DS_SIOPE", "IMPORTO", "DT_EMISSIONE", "DT_INCASSO", "DT_TRASMISSIONE") AS 
  select
--
-- Date: 06/07/2007
-- Versione 1.0
--
-- Consultazione Riepilogo Codici Siope Entrate
--
-- History:
--
-- Date: 06/07/2007
-- Versione 1.0
-- Creazione
--
-- Body:
--
rs.cd_cds, rs.esercizio, rs.pg_reversale, rs.esercizio_accertamento,
rs.esercizio_ori_accertamento, rs.pg_accertamento, rs.pg_accertamento_scadenzario, rs.cd_cds_doc_amm, rs.cd_uo_doc_amm,
rs.esercizio_doc_amm,rs.cd_tipo_documento_amm,da.ds_tipo_documento_amm,rs.pg_doc_amm,
rs.esercizio_siope,
rs.ti_gestione, rs.cd_siope, s.descrizione, rs.importo, TRUNC(r.dt_emissione), TRUNC(r.dt_incasso), TRUNC(r.dt_trasmissione)
from reversale r, reversale_siope rs, codici_siope s, tipo_documento_amm da
where r.cd_cds = rs.cd_cds
and r.esercizio = rs.esercizio
and r.pg_reversale = rs.pg_reversale
and r.stato != 'A'
and rs.esercizio = s.esercizio
and rs.ti_gestione = s.ti_gestione
and rs.cd_siope = s.cd_siope
and rs.cd_tipo_documento_amm=da.cd_tipo_documento_amm ;
