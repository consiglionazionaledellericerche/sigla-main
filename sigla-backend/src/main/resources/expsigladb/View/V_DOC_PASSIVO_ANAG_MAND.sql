--------------------------------------------------------
--  DDL for View V_DOC_PASSIVO_ANAG_MAND
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_DOC_PASSIVO_ANAG_MAND" ("DT_REGISTRAZIONE", "CD_CDS", "CD_UNITA_ORGANIZZATIVA", "ESERCIZIO", "CD_TIPO_DOCUMENTO_AMM", "PG_DOCUMENTO_AMM", "CD_CDS_ORIGINE", "CD_UO_ORIGINE", "TI_FATTURA", "STATO_COFI", "STATO_PAGAMENTO_FONDO_ECO", "DT_PAGAMENTO_FONDO_ECO", "DT_FATTURA_FORNITORE", "NR_FATTURA_FORNITORE", "CD_ANAG", "CD_TERZO", "CD_TERZO_CESSIONARIO", "COGNOME", "NOME", "RAGIONE_SOCIALE", "CD_MODALITA_PAG", "IM_TOTALE_DOC_AMM", "CODICE_FISCALE", "PARTITA_IVA", "DS_DOCUMENTO", "ESERCIZIO_MAN", "CDS_MANDATO", "NR_MANDATO") AS 
  SELECT   a.dt_registrazione, a.cd_cds, a.cd_unita_organizzativa,
            a.esercizio, 'FATTURA_P', a.pg_fattura_passiva, a.cd_cds_origine,
            a.cd_uo_origine, a.ti_fattura, a.stato_cofi,
            a.stato_pagamento_fondo_eco, a.dt_pagamento_fondo_eco,
            a.dt_fattura_fornitore, a.nr_fattura_fornitore, c.cd_anag,a.cd_terzo,
            b.cd_terzo_cessionario, a.cognome, a.nome, a.ragione_sociale,
            DECODE (b.cd_terzo_cessionario,
                    NULL, b.cd_modalita_pag,
                    SUBSTR (getmodpagcessionario (b.cd_terzo_cessionario,
                                                  d.ti_pagamento
                                                 ),
                            1,
                            10
                           )
                   ),
            SUM (DECODE (a.ti_fattura,
                         'C', ((b.im_imponibile + b.im_iva) * -1),
                         (b.im_imponibile + b.im_iva
                         )
                        )
                ),
            c.codice_fiscale, c.partita_iva, a.ds_fattura_passiva,
            mandato_riga.esercizio, mandato_riga.cd_cds,
            mandato_riga.pg_mandato
       FROM fattura_passiva a,
            fattura_passiva_riga b,
            anagrafico c,
            terzo t,
            banca d,
            mandato_riga
      WHERE t.cd_terzo = b.cd_terzo
        AND t.cd_anag = c.cd_anag
        AND b.cd_cds = a.cd_cds
        AND b.cd_unita_organizzativa = a.cd_unita_organizzativa
        AND b.esercizio = a.esercizio
        AND b.pg_fattura_passiva = a.pg_fattura_passiva
        AND b.dt_cancellazione IS NULL
        AND d.cd_terzo = b.cd_terzo
        AND d.pg_banca = b.pg_banca
        AND b.cd_cds_obbligazione IS NOT NULL
        AND b.cd_cds_obbligazione = mandato_riga.cd_cds(+)
        AND b.esercizio_obbligazione = mandato_riga.esercizio_obbligazione(+)
        AND b.pg_obbligazione = mandato_riga.pg_obbligazione(+)
        AND b.pg_obbligazione_scadenzario = mandato_riga.pg_obbligazione_scadenzario(+)
        AND b.esercizio_ori_obbligazione = mandato_riga.esercizio_ori_obbligazione(+)
        AND b.pg_fattura_passiva = mandato_riga.pg_doc_amm(+)
        AND b.esercizio = mandato_riga.esercizio_doc_amm(+)
        AND b.cd_unita_organizzativa = mandato_riga.cd_uo_doc_amm(+)
        AND b.cd_cds = mandato_riga.cd_cds_doc_amm(+)
        AND mandato_riga.cd_tipo_documento_amm(+) = 'FATTURA_P'
        AND mandato_riga.stato(+) != 'A'
   GROUP BY a.dt_registrazione,
            a.cd_cds,
            a.cd_unita_organizzativa,
            a.esercizio,
            'FATTURA_P',
            a.pg_fattura_passiva,
            a.cd_cds_origine,
            a.cd_uo_origine,
            a.ti_fattura,
            a.stato_cofi,
            a.stato_pagamento_fondo_eco,
            a.dt_pagamento_fondo_eco,
            a.dt_fattura_fornitore,
            a.nr_fattura_fornitore,
            c.cd_anag,
            a.cd_terzo,
            b.cd_terzo_cessionario,
            a.cognome,
            a.nome,
            a.ragione_sociale,
            DECODE (b.cd_terzo_cessionario,
                    NULL, b.cd_modalita_pag,
                    SUBSTR (getmodpagcessionario (b.cd_terzo_cessionario,
                                                  d.ti_pagamento
                                                 ),
                            1,
                            10
                           )
                   ),
            c.codice_fiscale,
            c.partita_iva,
            a.ds_fattura_passiva,
            mandato_riga.esercizio,
            mandato_riga.cd_cds,
            mandato_riga.pg_mandato
   UNION ALL
   SELECT   a.dt_registrazione, a.cd_cds, a.cd_unita_organizzativa,
            a.esercizio, 'FATTURA_A', a.pg_fattura_attiva, a.cd_cds_origine,
            a.cd_uo_origine, a.ti_fattura, a.stato_cofi, 'N', TO_DATE (NULL),
            TO_DATE (NULL), NULL, c.cd_anag,a.cd_terzo, TO_NUMBER (NULL), a.cognome,
            a.nome, a.ragione_sociale, a.cd_modalita_pag,
            SUM (b.im_imponibile + b.im_iva), c.codice_fiscale, c.partita_iva,
            a.ds_fattura_attiva, mandato_riga.esercizio, mandato_riga.cd_cds,
            mandato_riga.pg_mandato
       FROM fattura_attiva a,
            fattura_attiva_riga b,
            anagrafico c,
            terzo t,
            mandato_riga
      WHERE t.cd_terzo = a.cd_terzo
        AND t.cd_anag = c.cd_anag
        AND a.ti_fattura = 'C'
        AND b.cd_cds = a.cd_cds
        AND b.cd_unita_organizzativa = a.cd_unita_organizzativa
        AND b.esercizio = a.esercizio
        AND b.pg_fattura_attiva = a.pg_fattura_attiva
        AND b.cd_cds_obbligazione IS NOT NULL
        AND b.esercizio_obbligazione IS NOT NULL
        AND b.esercizio_ori_obbligazione IS NOT NULL
        AND b.pg_obbligazione IS NOT NULL
        AND b.pg_obbligazione_scadenzario IS NOT NULL
        AND b.dt_cancellazione IS NULL
        AND b.cd_cds_obbligazione = mandato_riga.cd_cds(+)
        AND b.esercizio_obbligazione = mandato_riga.esercizio_obbligazione(+)
        AND b.pg_obbligazione = mandato_riga.pg_obbligazione(+)
        AND b.pg_obbligazione_scadenzario = mandato_riga.pg_obbligazione_scadenzario(+)
        AND b.esercizio_ori_obbligazione = mandato_riga.esercizio_ori_obbligazione(+)
        AND b.pg_fattura_attiva = mandato_riga.pg_doc_amm(+)
        AND b.esercizio = mandato_riga.esercizio_doc_amm(+)
        AND b.cd_unita_organizzativa = mandato_riga.cd_uo_doc_amm(+)
        AND b.cd_cds = mandato_riga.cd_cds_doc_amm(+)
        AND mandato_riga.cd_tipo_documento_amm(+) = 'FATTURA_A'
        AND mandato_riga.stato(+) != 'A'
   GROUP BY a.dt_registrazione,
            a.cd_cds,
            a.cd_unita_organizzativa,
            a.esercizio,
            'FATTURA_A',
            a.pg_fattura_attiva,
            a.cd_cds_origine,
            a.cd_uo_origine,
            a.ti_fattura,
            a.stato_cofi,
            'N',
            TO_DATE (NULL),
            TO_DATE (NULL),
            NULL,
            c.cd_anag,
            a.cd_terzo,
            TO_NUMBER (NULL),
            a.cognome,
            a.nome,
            a.ragione_sociale,
            a.cd_modalita_pag,
            c.codice_fiscale,
            c.partita_iva,
            a.ds_fattura_attiva,
            mandato_riga.esercizio,
            mandato_riga.cd_cds,
            mandato_riga.pg_mandato
   UNION ALL
   SELECT   b.data_registrazione, a.cd_cds, a.cd_unita_organizzativa,
            a.esercizio, a.cd_tipo_documento_amm, a.pg_documento_generico,
            b.cd_cds_origine, b.cd_uo_origine, NULL, b.stato_cofi,
            b.stato_pagamento_fondo_eco, b.dt_pagamento_fondo_eco,
            TO_DATE (NULL), NULL, c.cd_anag,a.cd_terzo, a.cd_terzo_cessionario,
            a.cognome, a.nome, a.ragione_sociale,
            DECODE (a.cd_terzo_cessionario,
                    NULL, a.cd_modalita_pag,
                    SUBSTR (getmodpagcessionario (a.cd_terzo_cessionario,
                                                  d.ti_pagamento
                                                 ),
                            1,
                            10
                           )
                   ),
            SUM (a.im_riga), c.codice_fiscale, c.partita_iva,
            b.ds_documento_generico, mandato_riga.esercizio,
            mandato_riga.cd_cds, mandato_riga.pg_mandato
       FROM documento_generico_riga a,
            documento_generico b,
            banca d,
            anagrafico c,
            terzo t,
            mandato_riga
      WHERE t.cd_terzo = a.cd_terzo
        AND t.cd_anag = c.cd_anag
        AND a.cd_cds_obbligazione IS NOT NULL
        AND a.esercizio_obbligazione IS NOT NULL
        AND a.esercizio_ori_obbligazione IS NOT NULL
        AND a.pg_obbligazione IS NOT NULL
        AND a.pg_obbligazione_scadenzario IS NOT NULL
        AND a.dt_cancellazione IS NULL
        AND b.cd_cds = a.cd_cds
        AND b.cd_unita_organizzativa = a.cd_unita_organizzativa
        AND b.esercizio = a.esercizio
        AND b.cd_tipo_documento_amm = a.cd_tipo_documento_amm
        AND b.pg_documento_generico = a.pg_documento_generico
        AND d.cd_terzo = a.cd_terzo
        AND d.pg_banca = a.pg_banca
        AND a.cd_cds_obbligazione = mandato_riga.cd_cds(+)
        AND a.esercizio_obbligazione = mandato_riga.esercizio_obbligazione(+)
        AND a.pg_obbligazione = mandato_riga.pg_obbligazione(+)
        AND a.pg_obbligazione_scadenzario = mandato_riga.pg_obbligazione_scadenzario(+)
        AND a.esercizio_ori_obbligazione = mandato_riga.esercizio_ori_obbligazione(+)
        AND a.pg_documento_generico = mandato_riga.pg_doc_amm(+)
        AND a.esercizio = mandato_riga.esercizio_doc_amm(+)
        AND a.cd_unita_organizzativa = mandato_riga.cd_uo_doc_amm(+)
        AND a.cd_cds = mandato_riga.cd_cds_doc_amm(+)
        AND mandato_riga.cd_tipo_documento_amm(+) = 'GENERICO_S'
        AND mandato_riga.stato(+) != 'A'
        and a.cd_tipo_documento_amm = 'GENERICO_S'
   GROUP BY b.data_registrazione,
            a.cd_cds,
            a.cd_unita_organizzativa,
            a.esercizio,
            a.cd_tipo_documento_amm,
            a.pg_documento_generico,
            b.cd_cds_origine,
            b.cd_uo_origine,
            NULL,
            b.stato_cofi,
            b.stato_pagamento_fondo_eco,
            b.dt_pagamento_fondo_eco,
            TO_DATE (NULL),
            NULL,
            c.cd_anag,
            a.cd_terzo,
            a.cd_terzo_cessionario,
            a.cognome,
            a.nome,
            a.ragione_sociale,
            DECODE (a.cd_terzo_cessionario,
                    NULL, a.cd_modalita_pag,
                    SUBSTR (getmodpagcessionario (a.cd_terzo_cessionario,
                                                  d.ti_pagamento
                                                 ),
                            1,
                            10
                           )
                   ),
            c.codice_fiscale,
            c.partita_iva,
            b.ds_documento_generico,
            mandato_riga.esercizio,
            mandato_riga.cd_cds,
            mandato_riga.pg_mandato
   UNION ALL
   SELECT a.dt_registrazione, a.cd_cds, a.cd_unita_organizzativa, a.esercizio,
          'ANTICIPO', a.pg_anticipo, a.cd_cds_origine, a.cd_uo_origine, NULL,
          a.stato_cofi, a.stato_pagamento_fondo_eco, a.dt_pagamento_fondo_eco,
          TO_DATE (NULL), NULL,c.cd_anag, a.cd_terzo, TO_NUMBER (NULL), a.cognome,
          a.nome, a.ragione_sociale, a.cd_modalita_pag, a.im_anticipo,
          c.codice_fiscale, c.partita_iva, a.ds_anticipo,
          mandato_riga.esercizio, mandato_riga.cd_cds,
          mandato_riga.pg_mandato
     FROM anticipo a, rimborso b, anagrafico c, terzo t, mandato_riga
    WHERE t.cd_terzo = a.cd_terzo
      AND t.cd_anag = c.cd_anag
      AND a.cd_cds_obbligazione IS NOT NULL
      AND a.esercizio_obbligazione IS NOT NULL
      AND a.esercizio_ori_obbligazione IS NOT NULL
      AND a.pg_obbligazione IS NOT NULL
      AND a.pg_obbligazione_scadenzario IS NOT NULL
      AND a.dt_cancellazione IS NULL
      AND b.cd_cds_anticipo(+) = a.cd_cds
      AND b.cd_uo_anticipo(+) = a.cd_unita_organizzativa
      AND b.esercizio_anticipo(+) = a.esercizio
      AND b.pg_anticipo(+) = a.pg_anticipo
      AND a.cd_cds_obbligazione = mandato_riga.cd_cds(+)
      AND a.esercizio_obbligazione = mandato_riga.esercizio_obbligazione(+)
      AND a.pg_obbligazione = mandato_riga.pg_obbligazione(+)
      AND a.pg_obbligazione_scadenzario = mandato_riga.pg_obbligazione_scadenzario(+)
      AND a.esercizio_ori_obbligazione = mandato_riga.esercizio_ori_obbligazione(+)
      AND a.pg_anticipo = mandato_riga.pg_doc_amm(+)
      AND a.esercizio = mandato_riga.esercizio_doc_amm(+)
      AND a.cd_unita_organizzativa = mandato_riga.cd_uo_doc_amm(+)
      AND a.cd_cds = mandato_riga.cd_cds_doc_amm(+)
      AND mandato_riga.cd_tipo_documento_amm(+) = 'ANTICIPO'
      AND mandato_riga.stato(+) != 'A'
   UNION ALL
   SELECT a.dt_registrazione, a.cd_cds, a.cd_unita_organizzativa, a.esercizio,
          'COMPENSO', a.pg_compenso, a.cd_cds_origine, a.cd_uo_origine, NULL,
          a.stato_cofi, a.stato_pagamento_fondo_eco, a.dt_pagamento_fondo_eco,
          a.dt_fattura_fornitore, a.nr_fattura_fornitore,c.cd_anag, a.cd_terzo,
          TO_NUMBER (NULL), a.cognome, a.nome, a.ragione_sociale,
          a.cd_modalita_pag, sum(cr.im_totale_riga_compenso), c.codice_fiscale,
          c.partita_iva, a.ds_compenso, mandato_riga.esercizio,
          mandato_riga.cd_cds, mandato_riga.pg_mandato
     FROM compenso a, compenso_riga cr, anagrafico c, terzo t, mandato_riga
    WHERE t.cd_terzo = a.cd_terzo
      AND t.cd_anag = c.cd_anag
      AND a.cd_cds = cr.cd_cds
      AND a.cd_unita_organizzativa = cr.cd_unita_organizzativa
      AND a.esercizio = cr.esercizio
      AND a.pg_compenso = cr.pg_compenso
      AND a.dt_cancellazione IS NULL
      AND a.pg_compenso > 0
      AND cr.cd_cds_obbligazione = mandato_riga.cd_cds(+)
      AND cr.esercizio_obbligazione = mandato_riga.esercizio_obbligazione(+)
      AND cr.pg_obbligazione = mandato_riga.pg_obbligazione(+)
      AND cr.pg_obbligazione_scadenzario = mandato_riga.pg_obbligazione_scadenzario(+)
      AND cr.esercizio_ori_obbligazione = mandato_riga.esercizio_ori_obbligazione(+)
      AND cr.pg_compenso = mandato_riga.pg_doc_amm(+)
      AND cr.esercizio = mandato_riga.esercizio_doc_amm(+)
      AND cr.cd_unita_organizzativa = mandato_riga.cd_uo_doc_amm(+)
      AND cr.cd_cds = mandato_riga.cd_cds_doc_amm(+)
      AND mandato_riga.cd_tipo_documento_amm(+) = 'COMPENSO'
      AND mandato_riga.stato(+) != 'A'
   GROUP BY a.dt_registrazione, 
            a.cd_cds, 
            a.cd_unita_organizzativa, 
            a.esercizio,
            a.pg_compenso, 
            a.cd_cds_origine, 
            a.cd_uo_origine,
            a.stato_cofi, 
            a.stato_pagamento_fondo_eco, 
            a.dt_pagamento_fondo_eco,
            a.dt_fattura_fornitore, 
            a.nr_fattura_fornitore,
            c.cd_anag, 
            a.cd_terzo,
            a.cognome, 
            a.nome, 
            a.ragione_sociale,
            a.cd_modalita_pag, 
            c.codice_fiscale,
            c.partita_iva, 
            a.ds_compenso, 
            mandato_riga.esercizio,
            mandato_riga.cd_cds, mandato_riga.pg_mandato
   UNION ALL
   SELECT a.dt_registrazione, a.cd_cds, a.cd_unita_organizzativa, a.esercizio,
          'MISSIONE', a.pg_missione, a.cd_cds, a.cd_unita_organizzativa, NULL,
          a.stato_cofi, a.stato_pagamento_fondo_eco, a.dt_pagamento_fondo_eco,
          TO_DATE (NULL), NULL, c.cd_anag,a.cd_terzo, TO_NUMBER (NULL), a.cognome,
          a.nome, a.ragione_sociale, a.cd_modalita_pag,
          sum(mr.im_totale_riga_missione), c.codice_fiscale,
          c.partita_iva, a.ds_missione, mandato_riga.esercizio,
          mandato_riga.cd_cds, mandato_riga.pg_mandato
     FROM missione a, missione_riga mr, anagrafico c, terzo t, mandato_riga
    WHERE t.cd_terzo = a.cd_terzo
      AND t.cd_anag = c.cd_anag
      AND a.cd_cds = mr.cd_cds
      AND a.cd_unita_organizzativa = mr.cd_unita_organizzativa
      AND a.esercizio = mr.esercizio
      AND a.pg_missione = mr.pg_missione
      AND a.dt_cancellazione IS NULL
      AND mr.cd_cds_obbligazione = mandato_riga.cd_cds(+)
      AND mr.esercizio_obbligazione = mandato_riga.esercizio_obbligazione(+)
      AND mr.pg_obbligazione = mandato_riga.pg_obbligazione(+)
      AND mr.pg_obbligazione_scadenzario = mandato_riga.pg_obbligazione_scadenzario(+)
      AND mr.esercizio_ori_obbligazione = mandato_riga.esercizio_ori_obbligazione(+)
      AND mr.pg_missione = mandato_riga.pg_doc_amm(+)
      AND mr.esercizio = mandato_riga.esercizio_doc_amm(+)
      AND mr.cd_unita_organizzativa = mandato_riga.cd_uo_doc_amm(+)
      AND mr.cd_cds = mandato_riga.cd_cds_doc_amm(+)
      AND mandato_riga.cd_tipo_documento_amm(+) = 'MISSIONE'
      AND mandato_riga.stato(+) != 'A'
   GROUP BY a.dt_registrazione, 
            a.cd_cds, 
            a.cd_unita_organizzativa, 
            a.esercizio,
            a.pg_missione, 
            a.cd_cds, 
            a.cd_unita_organizzativa,
            a.stato_cofi, 
            a.stato_pagamento_fondo_eco, 
            a.dt_pagamento_fondo_eco,
            c.cd_anag,
            a.cd_terzo, 
            a.cognome,
            a.nome, 
            a.ragione_sociale, 
            a.cd_modalita_pag,
            c.codice_fiscale,
            c.partita_iva, 
            a.ds_missione, 
            mandato_riga.esercizio,
            mandato_riga.cd_cds, 
            mandato_riga.pg_mandato;
