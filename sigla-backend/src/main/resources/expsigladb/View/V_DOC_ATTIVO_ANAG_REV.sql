--------------------------------------------------------
--  DDL for View V_DOC_ATTIVO_ANAG_REV
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_DOC_ATTIVO_ANAG_REV" ("DT_REGISTRAZIONE", "CD_CDS", "CD_UNITA_ORGANIZZATIVA", "ESERCIZIO", "CD_TIPO_DOCUMENTO_AMM", "PG_DOCUMENTO_AMM", "DT_EMISSIONE", "CD_CDS_ORIGINE", "CD_UO_ORIGINE", "TI_FATTURA", "STATO_COFI", "CD_ANAG", "CD_TERZO", "COGNOME", "NOME", "RAGIONE_SOCIALE", "CD_MODALITA_PAG", "IM_TOTALE_DOC_AMM", "CODICE_FISCALE", "PARTITA_IVA", "DS_DOCUMENTO", "ESERCIZIO_REV", "CDS_REVERSALE", "NR_REVERSALE") AS 
  SELECT
--==================================================================================================
--
-- Date: 19/11/2006
-- Version: 1.0
--
-- Pre view di estrazione delle righe di fatture attive, note di credito passive su accertamenti e
-- documenti generici attivi e rimborsi
--
-- Body:
--
--==================================================================================================
          a.DT_REGISTRAZIONE,a.cd_cds, a.cd_unita_organizzativa, a.esercizio, 'FATTURA_A',
          a.pg_fattura_attiva, a.dt_emissione, a.cd_cds_origine,
          a.cd_uo_origine, a.ti_fattura, a.stato_cofi,anag.cd_anag, a.cd_terzo,
          a.cognome, a.nome, a.ragione_sociale,
          a.cd_modalita_pag_uo_cds,
          sum(DECODE (a.ti_fattura,
                  'C', DECODE (fl_liquidazione_differita,
                               'Y', b.im_imponibile * -1,
                               ((b.im_imponibile + b.im_iva) * -1
                               )
                              ),
                  (DECODE (fl_liquidazione_differita,
                           'Y', b.im_imponibile,
                           b.im_imponibile + b.im_iva
                          )
                  )
                 ))tot_documento,
          	anag.codice_fiscale, anag.partita_iva, a.ds_fattura_attiva,
            reversale_riga.esercizio, reversale_riga.cd_cds,
            reversale_riga.pg_reversale
     FROM fattura_attiva a, fattura_attiva_riga b, configurazione_cnr c,anagrafico anag,terzo t,reversale_riga
    WHERE b.cd_cds = a.cd_cds
      AND b.cd_unita_organizzativa = a.cd_unita_organizzativa
      AND b.esercizio = a.esercizio
      AND b.pg_fattura_attiva = a.pg_fattura_attiva
      AND b.dt_cancellazione IS NULL
      AND c.esercizio = 0
      AND c.cd_chiave_primaria = 'SPLIT_PAYMENT'
      AND c.cd_chiave_secondaria = 'ATTIVA'
      AND c.cd_unita_funzionale = '*'
      AND NVL (a.dt_emissione, a.dt_registrazione) >=
                        NVL (c.dt01, NVL (a.dt_emissione, a.dt_registrazione))
				AND t.cd_terzo = a.cd_terzo
        AND t.cd_anag = anag.cd_anag
        AND b.cd_cds_accertamento IS NOT NULL
        AND b.cd_cds_accertamento = reversale_riga.cd_cds(+)
        AND b.esercizio_accertamento = reversale_riga.esercizio_accertamento(+)
        AND b.pg_accertamento = reversale_riga.pg_accertamento(+)
        AND b.pg_accertamento_scadenzario = reversale_riga.pg_accertamento_scadenzario(+)
        AND b.esercizio_ori_accertamento = reversale_riga.esercizio_ori_accertamento(+)
        AND b.pg_fattura_attiva = reversale_riga.pg_doc_amm(+)
        AND b.esercizio = reversale_riga.esercizio_doc_amm(+)
        AND b.cd_unita_organizzativa = reversale_riga.cd_uo_doc_amm(+)
        AND b.cd_cds = reversale_riga.cd_cds_doc_amm(+)
        AND reversale_riga.cd_tipo_documento_amm(+) = 'FATTURA_A'
        AND reversale_riga.stato(+) != 'A'
    GROUP BY
    				a.DT_REGISTRAZIONE,
            a.cd_cds,
            a.cd_unita_organizzativa,
            a.esercizio,
            'FATTURA_A',
            a.pg_fattura_attiva,
            a.dt_emissione,
            a.cd_cds_origine,
            a.cd_uo_origine,
            a.ti_fattura,
            a.stato_cofi,
            anag.cd_anag,
            a.cd_terzo,
            a.cognome,
            a.nome,
            a.ragione_sociale,
            a.cd_modalita_pag_uo_cds,
            anag.codice_fiscale,
            anag.partita_iva,
            a.ds_fattura_attiva,
            reversale_riga.esercizio,
            reversale_riga.cd_cds,
            reversale_riga.pg_reversale
   UNION ALL
    select  a.DT_REGISTRAZIONE,a.cd_cds, a.cd_unita_organizzativa, a.esercizio, 'FATTURA_A',
          a.pg_fattura_attiva, a.dt_emissione, a.cd_cds_origine,
          a.cd_uo_origine, a.ti_fattura, a.stato_cofi, anag.cd_anag,a.cd_terzo,
          a.cognome, a.nome, a.ragione_sociale,
          a.cd_modalita_pag_uo_cds,
          sum(DECODE (a.ti_fattura,
                  'C', ((b.im_imponibile + b.im_iva) * -1),
                  (b.im_imponibile + b.im_iva
                  )
                 )) tot_documento,
          	anag.codice_fiscale, anag.partita_iva, a.ds_fattura_attiva,
            reversale_riga.esercizio, reversale_riga.cd_cds,
            reversale_riga.pg_reversale
     FROM fattura_attiva a, fattura_attiva_riga b, configurazione_cnr c,anagrafico anag,terzo t,reversale_riga
    WHERE b.cd_cds = a.cd_cds
      AND b.cd_unita_organizzativa = a.cd_unita_organizzativa
      AND b.esercizio = a.esercizio
      AND b.pg_fattura_attiva = a.pg_fattura_attiva
      AND b.dt_cancellazione IS NULL
      AND c.esercizio = 0
      AND c.cd_chiave_primaria = 'SPLIT_PAYMENT'
      AND c.cd_chiave_secondaria = 'ATTIVA'
      AND c.cd_unita_funzionale = '*'
        AND NVL (a.dt_emissione, a.dt_registrazione) <
                        NVL (c.dt01, NVL (a.dt_emissione, a.dt_registrazione))
				AND t.cd_terzo = a.cd_terzo
        AND t.cd_anag = anag.cd_anag
        AND b.cd_cds_accertamento IS NOT NULL
        AND b.cd_cds_accertamento = reversale_riga.cd_cds(+)
        AND b.esercizio_accertamento = reversale_riga.esercizio_accertamento(+)
        AND b.pg_accertamento = reversale_riga.pg_accertamento(+)
        AND b.pg_accertamento_scadenzario = reversale_riga.pg_accertamento_scadenzario(+)
        AND b.esercizio_ori_accertamento = reversale_riga.esercizio_ori_accertamento(+)
        AND b.pg_fattura_attiva = reversale_riga.pg_doc_amm(+)
        AND b.esercizio = reversale_riga.esercizio_doc_amm(+)
        AND b.cd_unita_organizzativa = reversale_riga.cd_uo_doc_amm(+)
        AND b.cd_cds = reversale_riga.cd_cds_doc_amm(+)
        AND reversale_riga.cd_tipo_documento_amm(+) = 'FATTURA_A'
        AND reversale_riga.stato(+) != 'A'
    GROUP BY a.DT_REGISTRAZIONE,
            a.cd_cds,
            a.cd_unita_organizzativa,
            a.esercizio,
            'FATTURA_A',
            a.pg_fattura_attiva,
            a.dt_emissione,
            a.cd_cds_origine,
            a.cd_uo_origine,
            a.ti_fattura,
            a.stato_cofi,
            anag.cd_anag,
            a.cd_terzo,
            a.cognome,
            a.nome,
            a.ragione_sociale,
            a.cd_modalita_pag_uo_cds,
            anag.codice_fiscale,
            anag.partita_iva,
            a.ds_fattura_attiva,
            reversale_riga.esercizio,
            reversale_riga.cd_cds,
            reversale_riga.pg_reversale
   UNION ALL
   SELECT a.DT_REGISTRAZIONE,a.cd_cds, a.cd_unita_organizzativa, a.esercizio, 'FATTURA_P',
          a.pg_fattura_passiva, TO_DATE (NULL),
          a.cd_cds_origine, a.cd_uo_origine, a.ti_fattura, a.stato_cofi,anag.cd_anag,
          a.cd_terzo,
          a.cognome, a.nome, a.ragione_sociale,
          a.cd_modalita_pag_uo_cds,
          sum((b.im_imponibile + b.im_iva)),
          anag.codice_fiscale, anag.partita_iva, a.ds_fattura_passiva,
          reversale_riga.esercizio, reversale_riga.cd_cds,
          reversale_riga.pg_reversale
     FROM fattura_passiva a, fattura_passiva_riga b,anagrafico anag,terzo t,reversale_riga
    WHERE a.ti_fattura = 'C'
      AND b.cd_cds = a.cd_cds
      AND b.cd_unita_organizzativa = a.cd_unita_organizzativa
      AND b.esercizio = a.esercizio
      AND b.pg_fattura_passiva = a.pg_fattura_passiva
      AND b.cd_cds_accertamento IS NOT NULL
      AND b.esercizio_accertamento IS NOT NULL
      AND b.esercizio_ori_accertamento IS NOT NULL
      AND b.pg_accertamento IS NOT NULL
      AND b.pg_accertamento_scadenzario IS NOT NULL
      AND b.dt_cancellazione IS NULL
      AND t.cd_terzo = a.cd_terzo
      AND t.cd_anag = anag.cd_anag
      AND b.cd_cds_accertamento IS NOT NULL
      AND b.cd_cds_accertamento = reversale_riga.cd_cds(+)
      AND b.esercizio_accertamento = reversale_riga.esercizio_accertamento(+)
      AND b.pg_accertamento = reversale_riga.pg_accertamento(+)
      AND b.pg_accertamento_scadenzario = reversale_riga.pg_accertamento_scadenzario(+)
      AND b.esercizio_ori_accertamento = reversale_riga.esercizio_ori_accertamento(+)
      AND b.pg_fattura_passiva = reversale_riga.pg_doc_amm(+)
      AND b.esercizio = reversale_riga.esercizio_doc_amm(+)
      AND b.cd_unita_organizzativa = reversale_riga.cd_uo_doc_amm(+)
      AND b.cd_cds = reversale_riga.cd_cds_doc_amm(+)
      AND reversale_riga.cd_tipo_documento_amm(+) = 'FATTURA_P'
      AND reversale_riga.stato(+) != 'A'
      GROUP BY a.DT_REGISTRAZIONE,
            a.cd_cds,
            a.cd_unita_organizzativa,
            a.esercizio,
            'FATTURA_P',
            a.pg_fattura_passiva,
            null,
            a.cd_cds_origine,
            a.cd_uo_origine,
            a.ti_fattura,
            a.stato_cofi,
            anag.cd_anag,
            a.cd_terzo,
            a.cognome,
            a.nome,
            a.ragione_sociale,
            a.cd_modalita_pag_uo_cds,
            anag.codice_fiscale,
            anag.partita_iva,
            a.ds_fattura_passiva,
            reversale_riga.esercizio,
            reversale_riga.cd_cds,
            reversale_riga.pg_reversale
   UNION ALL
   SELECT b.DATA_REGISTRAZIONE,a.cd_cds, a.cd_unita_organizzativa, a.esercizio,
          a.cd_tipo_documento_amm, a.pg_documento_generico, TO_DATE (NULL),
          b.cd_cds_origine, b.cd_uo_origine, NULL, a.stato_cofi,anag.cd_anag,
          a.cd_terzo,
          a.cognome, a.nome, a.ragione_sociale,
          a.cd_modalita_pag_uo_cds,sum(a.im_riga), anag.codice_fiscale,
          anag.partita_iva,
          b.DS_DOCUMENTO_GENERICO,
          reversale_riga.esercizio,
          reversale_riga.cd_cds,
          reversale_riga.pg_reversale
     FROM documento_generico_riga a, documento_generico b,anagrafico anag,terzo t,reversale_riga
    WHERE a.dt_cancellazione IS NULL
      AND a.cd_cds_accertamento IS NOT NULL
      AND a.esercizio_accertamento IS NOT NULL
      AND a.esercizio_ori_accertamento IS NOT NULL
      AND a.pg_accertamento IS NOT NULL
      AND a.pg_accertamento_scadenzario IS NOT NULL
      AND b.cd_cds = a.cd_cds
      AND b.cd_unita_organizzativa = a.cd_unita_organizzativa
      AND b.esercizio = a.esercizio
      AND b.cd_tipo_documento_amm = a.cd_tipo_documento_amm
      AND b.pg_documento_generico = a.pg_documento_generico
      AND t.cd_terzo = a.cd_terzo
      AND t.cd_anag = anag.cd_anag
      AND a.cd_cds_accertamento IS NOT NULL
      AND a.cd_cds_accertamento = reversale_riga.cd_cds(+)
      AND a.esercizio_accertamento = reversale_riga.esercizio_accertamento(+)
      AND a.pg_accertamento = reversale_riga.pg_accertamento(+)
      AND a.pg_accertamento_scadenzario = reversale_riga.pg_accertamento_scadenzario(+)
      AND a.esercizio_ori_accertamento = reversale_riga.esercizio_ori_accertamento(+)
      AND a.pg_documento_generico = reversale_riga.pg_doc_amm(+)
      AND a.esercizio = reversale_riga.esercizio_doc_amm(+)
      AND a.cd_unita_organizzativa = reversale_riga.cd_uo_doc_amm(+)
      AND a.cd_cds = reversale_riga.cd_cds_doc_amm(+)
      AND reversale_riga.cd_tipo_documento_amm(+) = 'GENERICO_E'
      AND reversale_riga.stato(+) != 'A'
      and a.cd_tipo_documento_amm='GENERICO_E'
      GROUP BY b.DATA_REGISTRAZIONE,
            a.cd_cds,
            a.cd_unita_organizzativa,
            a.esercizio,
            a.cd_tipo_documento_amm,
            a.pg_documento_generico,
            null,
            b.cd_cds_origine,
            b.cd_uo_origine,
            null,
            a.stato_cofi,
            anag.cd_anag,
            a.cd_terzo,
            a.cognome,
            a.nome,
            a.ragione_sociale,
            a.cd_modalita_pag_uo_cds,
            anag.codice_fiscale,
            anag.partita_iva,
            b.DS_DOCUMENTO_GENERICO,
            reversale_riga.esercizio,
            reversale_riga.cd_cds,
            reversale_riga.pg_reversale
   UNION ALL
   SELECT a.DT_REGISTRAZIONE,a.cd_cds, a.cd_unita_organizzativa, a.esercizio, 'RIMBORSO',
          a.pg_rimborso, TO_DATE (NULL), a.cd_cds_origine,
          a.cd_uo_origine, NULL, a.stato_cofi,anag.cd_anag,a.cd_terzo,
          a.cognome, a.nome, a.ragione_sociale,
          a.cd_modalita_pag_uo_cds, sum(a.im_rimborso),
          anag.codice_fiscale,
          anag.partita_iva,
          a.DS_RIMBORSO,
          reversale_riga.esercizio,
          reversale_riga.cd_cds,
          reversale_riga.pg_reversale
     FROM rimborso a,anagrafico anag,terzo t,reversale_riga
    WHERE a.dt_cancellazione IS NULL
     AND t.cd_terzo = a.cd_terzo
      AND t.cd_anag = anag.cd_anag
      AND a.cd_cds_accertamento IS NOT NULL
      AND a.cd_cds_accertamento = reversale_riga.cd_cds(+)
      AND a.esercizio_accertamento = reversale_riga.esercizio_accertamento(+)
      AND a.pg_accertamento = reversale_riga.pg_accertamento(+)
      AND a.pg_accertamento_scadenzario = reversale_riga.pg_accertamento_scadenzario(+)
      AND a.esercizio_ori_accertamento = reversale_riga.esercizio_ori_accertamento(+)
      AND a.pg_rimborso = reversale_riga.pg_doc_amm(+)
      AND a.esercizio = reversale_riga.esercizio_doc_amm(+)
      AND a.cd_unita_organizzativa = reversale_riga.cd_uo_doc_amm(+)
      AND a.cd_cds = reversale_riga.cd_cds_doc_amm(+)
      AND reversale_riga.cd_tipo_documento_amm(+) = 'RIMBORSO'
      AND reversale_riga.stato(+) != 'A'
       GROUP BY
       			a.DT_REGISTRAZIONE,
            a.cd_cds,
            a.cd_unita_organizzativa,
            a.esercizio,
           	'RIMBORSO',
            a.pg_rimborso,
            null,
            a.cd_cds_origine,
            a.cd_uo_origine,
            null,
            a.stato_cofi,
            anag.cd_anag,
            a.cd_terzo,
            a.cognome,
            a.nome,
            a.ragione_sociale,
            a.cd_modalita_pag_uo_cds,
            anag.codice_fiscale,
            anag.partita_iva,
            a.DS_RIMBORSO,
            reversale_riga.esercizio,
            reversale_riga.cd_cds,
            reversale_riga.pg_reversale;
