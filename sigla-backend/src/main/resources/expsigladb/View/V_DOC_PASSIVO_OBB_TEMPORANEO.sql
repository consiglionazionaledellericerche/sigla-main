--------------------------------------------------------
--  DDL for View V_DOC_PASSIVO_OBB_TEMPORANEO
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_DOC_PASSIVO_OBB_TEMPORANEO" ("CD_CDS", "CD_UNITA_ORGANIZZATIVA", "ESERCIZIO", "CD_TIPO_DOCUMENTO_AMM", "PG_DOCUMENTO_AMM", "PG_VER_REC", "CD_CDS_ORIGINE", "CD_UO_ORIGINE", "TI_FATTURA", "STATO_COFI", "CD_CDS_OBBLIGAZIONE", "ESERCIZIO_OBBLIGAZIONE", "ESERCIZIO_ORI_OBBLIGAZIONE", "PG_OBBLIGAZIONE", "PG_OBBLIGAZIONE_SCADENZARIO", "DT_SCADENZA", "FL_PGIRO", "CD_TIPO_DOCUMENTO_CONT", "DT_FATTURA_FORNITORE", "NR_FATTURA_FORNITORE", "CD_TERZO", "CD_TERZO_CESSIONARIO", "DS_OBBLIGAZIONE", "PG_BANCA", "CD_MODALITA_PAG", "TI_PAGAMENTO", "IM_IMPONIBILE_DOC_AMM", "IM_IVA_DOC_AMM", "IM_TOTALE_DOC_AMM", "IM_SCADENZA", "IM_ASSOCIATO_DOC_CONTABILE", "PG_LETTERA", "TI_ENTRATA_SPESA", "TI_SOSPESO_RISCONTRO", "CD_SOSPESO", "FL_SELEZIONE", "FL_FAI_REVERSALE") AS 
  SELECT
            a.cd_cds, a.cd_unita_organizzativa, a.esercizio,
            a.cd_tipo_documento_amm, a.pg_documento_amm, a.pg_ver_rec,
            a.cd_cds_origine, a.cd_uo_origine, a.ti_fattura, a.stato_cofi,
            a.cd_cds_obbligazione, a.esercizio_obbligazione,
            a.esercizio_ori_obbligazione, a.pg_obbligazione,
            a.pg_obbligazione_scadenzario, b.dt_scadenza, c.fl_pgiro,
            c.cd_tipo_documento_cont, a.dt_fattura_fornitore,
            a.nr_fattura_fornitore, a.cd_terzo, a.cd_terzo_cessionario,
            --A.cognome,
            --A.nome,
            --A.ragione_sociale,
            c.ds_obbligazione,
            a.pg_banca, a.cd_modalita_pag, d.ti_pagamento,
            SUM (a.im_imponibile_doc_amm), SUM (a.im_iva_doc_amm),
            SUM (a.im_totale_doc_amm), b.im_scadenza,
            b.im_associato_doc_contabile, a.pg_lettera, a.ti_entrata_spesa,
            a.ti_sospeso_riscontro, a.cd_sospeso, a.fl_selezione,
            a.fl_fai_reversale
       FROM v_doc_passivo a,
            obbligazione_scadenzario b,
            obbligazione c,
            rif_modalita_pagamento d
      WHERE b.cd_cds = a.cd_cds_obbligazione
        AND b.esercizio = a.esercizio_obbligazione
        AND b.esercizio_originale = a.esercizio_ori_obbligazione
        AND b.pg_obbligazione = a.pg_obbligazione
        AND b.pg_obbligazione_scadenzario = a.pg_obbligazione_scadenzario
        AND c.cd_cds = b.cd_cds
        AND c.esercizio = b.esercizio
        AND c.esercizio_originale = b.esercizio_originale
        AND c.pg_obbligazione = b.pg_obbligazione
        AND d.cd_modalita_pag = a.cd_modalita_pag
   GROUP BY a.cd_cds,
            a.cd_unita_organizzativa,
            a.esercizio,
            a.cd_tipo_documento_amm,
            a.pg_documento_amm,
            a.pg_ver_rec,
            a.cd_cds_origine,
            a.cd_uo_origine,
            a.ti_fattura,
            a.stato_cofi,
            a.cd_cds_obbligazione,
            a.esercizio_obbligazione,
            a.esercizio_ori_obbligazione,
            a.pg_obbligazione,
            a.pg_obbligazione_scadenzario,
            b.dt_scadenza,
            c.fl_pgiro,
            c.cd_tipo_documento_cont,
            a.dt_fattura_fornitore,
            a.nr_fattura_fornitore,
            a.cd_terzo,
            a.cd_terzo_cessionario,
            --A.cognome,
            --A.nome,
            --A.ragione_sociale,
            c.ds_obbligazione,
            a.pg_banca,
            a.cd_modalita_pag,
            d.ti_pagamento,
            b.im_scadenza,
            b.im_associato_doc_contabile,
            a.pg_lettera,
            a.ti_entrata_spesa,
            a.ti_sospeso_riscontro,
            a.cd_sospeso,
            a.fl_selezione,
            a.fl_fai_reversale ;
