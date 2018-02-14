--------------------------------------------------------
--  DDL for View V_CONS_CUP_REVERSALI
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_CONS_CUP_REVERSALI" ("CD_CDS", "UO", "ESERCIZIO", "PG_REVERSALE", "ESERCIZIO_ACCERTAMENTO", "ESERCIZIO_ORI_ACCERTAMENTO", "PG_ACCERTAMENTO", "PG_ACCERTAMENTO_SCADENZARIO", "CD_CDS_DOC_AMM", "CD_UO_DOC_AMM", "ESERCIZIO_DOC_AMM", "CD_TIPO_DOCUMENTO_AMM", "DS_TIPO_DOC_AMM", "PG_DOC_AMM", "CD_CUP", "DS_CUP", "IMPORTO", "DT_EMISSIONE", "DT_INCASSO", "DT_TRASMISSIONE") AS 
  SELECT
--
-- Date: 20/02/2013
-- Versione 1.0
--
-- Consultazione Riepilogo Reversali - Codici Cup
--
          r.cd_cds_origine, r.CD_UO_ORIGINE,rc.esercizio, rc.pg_reversale, rc.esercizio_accertamento,
          rc.esercizio_ori_accertamento, rc.pg_accertamento,
          rc.pg_accertamento_scadenzario, rc.cd_cds_doc_amm, rc.cd_uo_doc_amm,
          rc.esercizio_doc_amm, rc.cd_tipo_documento_amm,
          da.ds_tipo_documento_amm, rc.pg_doc_amm, rc.cd_cup, c.descrizione, rc.importo,
          TRUNC (r.dt_emissione), TRUNC (r.dt_incasso),
          TRUNC (r.dt_trasmissione)
     FROM reversale r,
          reversale_cup rc,
          cup c,
          tipo_documento_amm da
    WHERE r.cd_cds = rc.cd_cds
      AND r.esercizio = rc.esercizio
      AND r.pg_reversale = rc.pg_reversale
      AND r.stato != 'A'
      AND rc.cd_cup = c.cd_cup
      AND rc.cd_tipo_documento_amm = da.cd_tipo_documento_amm
    union  -- Nuova tabella associazione Cup
    select   r.cd_cds_origine, r.CD_UO_ORIGINE,rc.esercizio, rc.pg_reversale, rc.esercizio_accertamento,
          rc.esercizio_ori_accertamento, rc.pg_accertamento,
          rc.pg_accertamento_scadenzario, rc.cd_cds_doc_amm, rc.cd_uo_doc_amm,
          rc.esercizio_doc_amm, rc.cd_tipo_documento_amm,
          da.ds_tipo_documento_amm, rc.pg_doc_amm, rc.cd_cup, c.descrizione, rc.importo,
          TRUNC (r.dt_emissione), TRUNC (r.dt_incasso),
          TRUNC (r.dt_trasmissione)
     FROM reversale r,
          reversale_siope_cup rc,
          cup c,
          tipo_documento_amm da
    WHERE r.cd_cds = rc.cd_cds
      AND r.esercizio = rc.esercizio
      AND r.pg_reversale = rc.pg_reversale
      AND r.stato != 'A'
      AND rc.cd_cup = c.cd_cup
      AND rc.cd_tipo_documento_amm = da.cd_tipo_documento_amm;
