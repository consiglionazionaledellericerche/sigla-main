--------------------------------------------------------
--  DDL for View V_CONS_CUP_MANDATI
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_CONS_CUP_MANDATI" ("CD_CDS", "UO", "ESERCIZIO", "PG_MANDATO", "ESERCIZIO_OBBLIGAZIONE", "ESERCIZIO_ORI_OBBLIGAZIONE", "PG_OBBLIGAZIONE", "PG_OBBLIGAZIONE_SCADENZARIO", "CD_CDS_DOC_AMM", "CD_UO_DOC_AMM", "ESERCIZIO_DOC_AMM", "CD_TIPO_DOCUMENTO_AMM", "DS_TIPO_DOC_AMM", "PG_DOC_AMM", "CD_CUP", "DS_CUP", "IMPORTO", "DT_EMISSIONE", "DT_PAGAMENTO", "DT_TRASMISSIONE") AS 
  SELECT
--
-- Date: 20/02/2013
-- Versione 1.0
--
-- Consultazione Riepilogo mandati - Codici Cup
--
          m.cd_cds_origine, m.cd_uo_origine, mc.esercizio, mc.pg_mandato,
          mc.esercizio_obbligazione, mc.esercizio_ori_obbligazione,
          mc.pg_obbligazione, mc.pg_obbligazione_scadenzario,
          mc.cd_cds_doc_amm, mc.cd_uo_doc_amm, mc.esercizio_doc_amm,
          mc.cd_tipo_documento_amm, da.ds_tipo_documento_amm, mc.pg_doc_amm,
          mc.cd_cup, c.descrizione, mc.importo, TRUNC (m.dt_emissione),
          TRUNC (m.dt_pagamento), TRUNC (m.dt_trasmissione)
     FROM mandato m, mandato_cup mc, cup c, tipo_documento_amm da
    WHERE m.cd_cds = mc.cd_cds
      AND m.esercizio = mc.esercizio
      AND m.pg_mandato = mc.pg_mandato
      AND m.stato != 'A'
      AND mc.cd_cup = c.cd_cup
      AND mc.cd_tipo_documento_amm = da.cd_tipo_documento_amm
   UNION
   -- Nuova tabella associazione Cup
   SELECT   m.cd_cds_origine, m.cd_uo_origine, mc.esercizio, mc.pg_mandato,
            mc.esercizio_obbligazione, mc.esercizio_ori_obbligazione,
            mc.pg_obbligazione, mc.pg_obbligazione_scadenzario,
            mc.cd_cds_doc_amm, mc.cd_uo_doc_amm, mc.esercizio_doc_amm,
            mc.cd_tipo_documento_amm, da.ds_tipo_documento_amm, mc.pg_doc_amm,
            mc.cd_cup, c.descrizione, SUM (mc.importo),
            TRUNC (m.dt_emissione), TRUNC (m.dt_pagamento),
            TRUNC (m.dt_trasmissione)
       FROM mandato m, mandato_siope_cup mc, cup c, tipo_documento_amm da
      WHERE m.cd_cds = mc.cd_cds
        AND m.esercizio = mc.esercizio
        AND m.pg_mandato = mc.pg_mandato
        AND m.stato != 'A'
        AND mc.cd_cup = c.cd_cup
        AND mc.cd_tipo_documento_amm = da.cd_tipo_documento_amm
   GROUP BY m.cd_cds_origine,
            m.cd_uo_origine,
            mc.esercizio,
            mc.pg_mandato,
            mc.esercizio_obbligazione,
            mc.esercizio_ori_obbligazione,
            mc.pg_obbligazione,
            mc.pg_obbligazione_scadenzario,
            mc.cd_cds_doc_amm,
            mc.cd_uo_doc_amm,
            mc.esercizio_doc_amm,
            mc.cd_tipo_documento_amm,
            da.ds_tipo_documento_amm,
            mc.pg_doc_amm,
            mc.cd_cup,
            c.descrizione,
            TRUNC (m.dt_emissione),
            TRUNC (m.dt_pagamento),
            TRUNC (m.dt_trasmissione);
