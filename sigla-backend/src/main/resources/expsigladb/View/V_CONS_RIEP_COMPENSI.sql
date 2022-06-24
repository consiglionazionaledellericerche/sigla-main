--------------------------------------------------------
--  DDL for View V_CONS_RIEP_COMPENSI
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_CONS_RIEP_COMPENSI" ("CD_CDS", "CD_UNITA_ORGANIZZATIVA", "DS_UNITA_ORGANIZZATIVA", "ESERCIZIO", "PG_COMPENSO", "CODICE_FISCALE", "CD_TERZO", "COGNOME", "NOME", "DT_DA_COMPETENZA_COGE", "DT_A_COMPETENZA_COGE", "DT_TRASMISSIONE", "DT_PAGAMENTO", "IM_LORDO", "IM_FISCALE", "CD_TRATTAMENTO", "DS_TI_TRATTAMENTO", "TOT_COSTO", "IRAP_ENTE", "INPS_ENTE", "INPGI_ENTE", "ENPAPI_ENTE", "INAIL_ENTE", "IRPEF", "BONUSDL66", "INPS_PERCIPIENTE", "INPGI_PERCIPIENTE", "ENPAPI_PERCIPIENTE", "INAIL_PERCIPIENTE", "ADD_REG", "ADD_COM", "IMPONIBILE_IVA", "IMPORTO_IVA", "CASSA_RIVALSA", "CUNEODL320") AS
  SELECT   com.cd_cds, com.cd_unita_organizzativa, uo.ds_unita_organizzativa,
            com.esercizio, com.pg_compenso, ter.codice_fiscale, ter.cd_terzo,
            ter.cognome, ter.nome, com.dt_da_competenza_coge,
            com.dt_a_competenza_coge,
                        dc.dt_trasmissione,
            DECODE (com.stato_cofi,
                    'P', NVL (dc.dt_pagamento, com.dt_registrazione),
                    dc.dt_pagamento
                   ) dt_pagamento,
            NVL (com.im_lordo_percipiente, 0) im_lordo,
            DECODE (com.fl_compenso_conguaglio,
                    'Y', 0,
                    NVL (com.imponibile_fiscale, 0)
                   ) im_fiscale,
            tipo_t.cd_trattamento, tipo_t.ds_ti_trattamento,
                NVL (com.im_lordo_percipiente, 0) +
                NVL (SUM (DECODE (cr.ti_ente_percipiente,
                                              'P', 0,
                                              DECODE (tipo.cd_gruppo_cr,
                                                      'IRAP', cr.ammontare,
                                                      0
                                                     )
                                              )
                                       ),
                                 0
                                ) +
                NVL (SUM (DECODE (cr.ti_ente_percipiente,
                                               'P', 0,
                                               DECODE (tipo.cd_gruppo_cr,
                                                       'CXX', cr.ammontare,
                                                       'C10', cr.ammontare,
                                                       0
                                                      )
                                              )
                                      ),
                                  0
                                 ) +
                NVL (SUM (DECODE (cr.ti_ente_percipiente,
                                              'P', 0,
                                              DECODE (tipo.cd_gruppo_cr,
                                                      'CGS1', cr.ammontare,
                                                      'TGS1', cr.ammontare,
                                                      0
                                                     )
                                             )
                                     ),
                                 0
                                ) +
                NVL (SUM (DECODE (cr.ti_ente_percipiente,
                                              'P', 0,
                                              DECODE (tipo.cd_gruppo_cr,
                                                      'ENPAPI', cr.ammontare,
                                                      0
                                                     )
                                             )
                                     ),
                                 0
                                ) +
                NVL (SUM (DECODE (cr.ti_ente_percipiente,
                                              'P', 0,
                                              DECODE (tipo.cd_gruppo_cr,
                                                      'X', cr.ammontare,
                                                      0
                                                     )
                                             )
                                     ),
                                 0
                                )
            tot_costo,
            NVL (SUM (DECODE (cr.ti_ente_percipiente,
                              'P', 0,
                              DECODE (tipo.cd_gruppo_cr,
                                      'IRAP', cr.ammontare,
                                      0
                                     )
                             )
                     ),
                 0
                ) irap_ente,
            NVL (SUM (DECODE (cr.ti_ente_percipiente,
                              'P', 0,
                              DECODE (tipo.cd_gruppo_cr,
                                      'CXX', cr.ammontare,
                                      'C10', cr.ammontare,
                                      0
                                     )
                             )
                     ),
                 0
                ) inps_ente,
            NVL (SUM (DECODE (cr.ti_ente_percipiente,
                              'P', 0,
                              DECODE (tipo.cd_gruppo_cr,
                                      'CGS1', cr.ammontare,
                                      'TGS1', cr.ammontare,
                                      0
                                     )
                             )
                     ),
                 0
                ) inpgi_ente,
            NVL (SUM (DECODE (cr.ti_ente_percipiente,
                              'P', 0,
                              DECODE (tipo.cd_gruppo_cr,
                                      'ENPAPI', cr.ammontare,
                                      0
                                     )
                             )
                     ),
                 0
                ) enpapi_ente,
            NVL (SUM (DECODE (cr.ti_ente_percipiente,
                              'P', 0,
                              DECODE (tipo.cd_gruppo_cr,
                                      'X', cr.ammontare,
                                      0
                                     )
                             )
                     ),
                 0
                ) inail_ente,
            NVL
               (SUM (DECODE (cr.ti_ente_percipiente,
                             'E', 0,
                             DECODE (tipo.cd_gruppo_cr,
                                     '1040', cr.ammontare,
                                     '1004', DECODE
                                                 (tipo.cd_contributo_ritenuta,
                                                  'BONUSDL66', 0,
                                                  'CUNEODL320', 0,
                                                  cr.ammontare
                                                 ),
                                     0
                                    )
                            )
                    ),
                0
               ) irpef,
            NVL
               (SUM (DECODE (cr.ti_ente_percipiente,
                             'E', 0,
                             DECODE (tipo.cd_gruppo_cr,
                                     '1004', DECODE
                                                 (tipo.cd_contributo_ritenuta,
                                                  'BONUSDL66', cr.ammontare,
                                                  0
                                                 ),
                                     0
                                    )
                            )
                    ),
                0
               ) bonusdl66,
            NVL (SUM (DECODE (cr.ti_ente_percipiente,
                              'E', 0,
                              DECODE (tipo.cd_gruppo_cr,
                                      'CXX', cr.ammontare,
                                      'C10', cr.ammontare,
                                      0
                                     )
                             )
                     ),
                 0
                ) inps_percipiente,
            NVL (SUM (DECODE (cr.ti_ente_percipiente,
                              'E', 0,
                              DECODE (tipo.cd_gruppo_cr,
                                      'CGS1', cr.ammontare,
                                      'TGS1', cr.ammontare,
                                      0
                                     )
                             )
                     ),
                 0
                ) inpgi_percipiente,
            NVL (SUM (DECODE (cr.ti_ente_percipiente,
                              'E', 0,
                              DECODE (tipo.cd_gruppo_cr,
                                      'ENPAPI', cr.ammontare,
                                      0
                                     )
                             )
                     ),
                 0
                ) enpapi_percipiente,
            NVL (SUM (DECODE (cr.ti_ente_percipiente,
                              'E', 0,
                              DECODE (tipo.cd_gruppo_cr,
                                      'X', cr.ammontare,
                                      0
                                     )
                             )
                     ),
                 0
                ) inail_percipiente,
            NVL (SUM (DECODE (cr.ti_ente_percipiente,
                              'E', 0,
                              DECODE (tipo.cd_gruppo_cr,
                                      '3802', cr.ammontare,
                                      0
                                     )
                             )
                     ),
                 0
                ) add_reg,
            NVL (SUM (DECODE (cr.ti_ente_percipiente,
                              'E', 0,
                              DECODE (tipo.cd_gruppo_cr,
                                      '3816', cr.ammontare,
                                      0
                                     )
                             )
                     ),
                 0
                ) add_com,
            NVL (SUM (DECODE (cr.ti_ente_percipiente,
                              'P', 0,
                              DECODE (tipo.cd_gruppo_cr,
                                      'IVA', cr.imponibile,
                                      0
                                     )
                             )
                     ),
                 0
                ) imponibile_iva,
            NVL (SUM (DECODE (cr.ti_ente_percipiente,
                              'P', 0,
                              DECODE (tipo.cd_gruppo_cr,
                                      'IVA', cr.ammontare,
                                      0
                                     )
                             )
                     ),
                 0
                ) importo_iva,
            NVL
               (SUM (DECODE (cr.ti_ente_percipiente,
                             'P', 0,
                             DECODE (tipo.cd_gruppo_cr,
                                     NULL, DECODE
                                               (t_c_r.cd_classificazione_cori,
                                                'RV', cr.ammontare,
                                                0
                                               ),
                                     0
                                    )
                            )
                    ),
                0
               ) cassa_rivalsa,
                                          NVL
                                             (SUM (DECODE (cr.ti_ente_percipiente,
                                                           'E', 0,
                                                           DECODE (tipo.cd_gruppo_cr,
                                                                   '1004', DECODE
                                                                               (tipo.cd_contributo_ritenuta,
                                                                                'CUNEODL320', cr.ammontare,
                                                                                0
                                                                               ),
                                                                   0
                                                                  )
                                                          )
                                                  ),
                                              0
                                             ) cuneodl320
       FROM compenso com,
            v_anagrafico_terzo ter,
            contributo_ritenuta cr,
            tipo_cr_base tipo,
            v_doc_cont_comp dc,
            unita_organizzativa uo,
            tipo_contributo_ritenuta t_c_r,
            tipo_trattamento tipo_t
      WHERE com.cd_cds = cr.cd_cds
        AND com.cd_unita_organizzativa = cr.cd_unita_organizzativa
        AND com.esercizio = cr.esercizio
        AND com.pg_compenso = cr.pg_compenso
        AND com.cd_terzo = ter.cd_terzo
        AND com.dt_cancellazione IS NULL             -- compensi non annullati
        AND cr.cd_contributo_ritenuta = t_c_r.cd_contributo_ritenuta
        AND cr.dt_ini_validita = t_c_r.dt_ini_validita
        AND com.cd_trattamento = tipo_t.cd_trattamento
        AND tipo_t.dt_ini_validita <= com.dt_registrazione
        AND NVL (tipo_t.dt_fin_validita, com.dt_registrazione) >=
                                                          com.dt_registrazione
        AND cr.esercizio = tipo.esercizio(+)
        AND cr.cd_contributo_ritenuta = tipo.cd_contributo_ritenuta(+)
        AND com.cd_unita_organizzativa = uo.cd_unita_organizzativa
        AND com.cd_cds = dc.cd_cds_compenso(+)
        AND com.cd_unita_organizzativa = dc.cd_uo_compenso(+)
        AND com.esercizio = dc.esercizio_compenso(+)
        AND com.pg_compenso = dc.pg_compenso(+)
        AND 'Y' = dc.principale(+)
   GROUP BY com.cd_cds,
            com.cd_unita_organizzativa,
            uo.ds_unita_organizzativa,
            com.esercizio,
            com.pg_compenso,
            ter.codice_fiscale,
            ter.cd_terzo,
            ter.cognome,
            ter.nome,
            com.dt_da_competenza_coge,
            com.dt_a_competenza_coge,
            dc.dt_trasmissione,
            DECODE (com.stato_cofi,
                    'P', NVL (dc.dt_pagamento, com.dt_registrazione),
                    dc.dt_pagamento
                   ),
            NVL (com.im_lordo_percipiente, 0),
            DECODE (com.fl_compenso_conguaglio,
                    'Y', 0,
                    NVL (com.imponibile_fiscale, 0)
                   ),
            tipo_t.cd_trattamento,
            tipo_t.ds_ti_trattamento ;
