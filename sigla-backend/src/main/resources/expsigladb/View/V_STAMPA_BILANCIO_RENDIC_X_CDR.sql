--------------------------------------------------------
--  DDL for View V_STAMPA_BILANCIO_RENDIC_X_CDR
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_STAMPA_BILANCIO_RENDIC_X_CDR" ("FONTE", "ESERCIZIO", "CD_CENTRO_RESPONSABILITA", "TIPO", "CD_ELEMENTO_VOCE", "CD_LIVELLO1", "CD_LIVELLO2", "CD_LIVELLO3", "CD_LIVELLO4", "CD_LIVELLO5", "CD_LIVELLO6", "CD_LIVELLO7", "CD_LIVELLO8", "CD_LIVELLO9", "DS_LIVELLO1", "DS_LIVELLO2", "DS_LIVELLO3", "DS_LIVELLO4", "DS_LIVELLO5", "DS_LIVELLO6", "DS_LIVELLO7", "DS_LIVELLO8", "DS_LIVELLO9", "PREVISIONE_INI", "SALDO_VARIAZIONI", "TOT_IMPACC_COMP", "TOT_IMPACC_RES", "TOT_MANREV_COMP", "TOT_MANREV_RES", "TOT_MOD_IMPACC_RES", "ASSESTATO_CASSA", "PREVISIONE_INI_ES_PREC", "SALDO_VARIAZIONI_ES_PREC", "TOT_IMPACC_COMP_ES_PREC", "TOT_IMPACC_RES_ES_PREC", "TOT_MANREV_COMP_ES_PREC", "TOT_MANREV_RES_ES_PREC", "TOT_MOD_IMPACC_RES_ES_PREC", "ASSESTATO_CASSA_ES_PREC") AS 
  (SELECT   e.fonte, e.esercizio, e.cd_centro_responsabilita,
             e.ti_gestione tipo, e.cd_elemento_voce cd_elemento_voce,
             e.cd_missione cd_livello1, e.cd_programma cd_livello2,
             e.cd_livello1 cd_livello3, e.cd_livello2 cd_livello4,
             e.cd_livello3 cd_livello5, e.cd_livello4 cd_livello6,
             e.cd_livello5 cd_livello7, e.cd_livello6 cd_livello8,
             e.cd_livello7 cd_livello9,
             NVL (g.ds_missione, 'NON DEFINITO') ds_livello1,
             NVL (h.ds_programma, 'NON DEFINITO') ds_livello2,
             e.ds_liv1 ds_livello3, e.ds_liv2 ds_livello4,
             e.ds_liv3 ds_livello5, e.ds_liv4 ds_livello6,
             e.ds_liv5 ds_livello7, e.ds_liv6 ds_livello8,
             e.ds_liv7 ds_livello9, SUM (e.previsione_ini),
             SUM (e.saldo_variazioni), SUM (e.tot_impacc_comp),
             SUM (e.tot_impacc_res), SUM (e.tot_manrev_comp),
             SUM (e.tot_manrev_res), SUM (e.tot_mod_impacc_res),
             SUM (e.assestato_cassa), SUM (e.previsione_ini_es_prec),
             SUM (e.saldo_variazioni_es_prec),
             SUM (e.tot_impacc_comp_es_prec), SUM (e.tot_impacc_res_es_prec),
             SUM (e.tot_manrev_comp_es_prec), SUM (e.tot_manrev_res_es_prec),
             SUM (e.tot_mod_impacc_res_es_prec),
             SUM (e.assestato_cassa_es_prec)
        FROM (                           --PARTE SPESE DECISIONALE SCIENTIFICO
              SELECT 'REASCI' fonte, a.esercizio, a.cd_centro_responsabilita,
                     a.ti_gestione, b.cd_elemento_voce,
                     NVL (d.cd_missione, 'NDF') cd_missione,
                     NVL (d.cd_programma, 'NDF') cd_programma, c.cd_livello1,
                     c.cd_livello2, c.cd_livello3, c.cd_livello4,
                     c.cd_livello5, c.cd_livello6, c.cd_livello7, c.ds_liv1,
                     c.ds_liv2, c.ds_liv3, c.ds_liv4, c.ds_liv5, c.ds_liv6,
                     c.ds_liv7,
                     DECODE (a.esercizio,
                             a.esercizio_res, NVL (a.stanziamento_iniziale, 0),
                             0
                            ) previsione_ini,
                     DECODE (a.esercizio,
                             a.esercizio_res, NVL (a.assestato_iniziale, 0)
                              - NVL (a.stanziamento_iniziale, 0),
                             0
                            ) saldo_variazioni,
                     DECODE (a.esercizio,
                             a.esercizio_res, NVL (a.importo_utilizzato, 0),
                             0
                            ) tot_impacc_comp,
                     DECODE
                           (a.esercizio,
                            a.esercizio_res, 0,
                              NVL (a.stanziamento_iniziale, 0)
                            + NVL (a.importo_ini_residui_propri, 0)
                           ) tot_impacc_res,
                     DECODE (a.esercizio,
                             a.esercizio_res, NVL (a.importo_manrev, 0),
                             0
                            ) tot_manrev_comp,
                     DECODE (a.esercizio,
                             a.esercizio_res, 0,
                             NVL (a.importo_manrev, 0)
                            ) tot_manrev_res,
                     DECODE (a.esercizio,
                             a.esercizio_res, 0,
                             DECODE(a.ti_gestione, 'E',
                               NVL (a.variazioni_residui_propri, 0),
                               NVL (a.variazioni_residui_propri, 0)
                             + NVL (a.variazioni_positive, 0)
                             - NVL (a.variazioni_negative, 0))
                            ) tot_mod_impacc_res,
                     DECODE (a.esercizio,
                             a.esercizio_res, NVL (a.assestato_cassa, 0),
                             0
                            ) assestato_cassa,
                     0 previsione_ini_es_prec, 0 saldo_variazioni_es_prec,
                     0 tot_impacc_comp_es_prec, 0 tot_impacc_res_es_prec,
                     0 tot_manrev_comp_es_prec, 0 tot_manrev_res_es_prec,
                     0 tot_mod_impacc_res_es_prec, 0 assestato_cassa_es_prec
                FROM v_assestato a,
                     elemento_voce b,
                     v_classificazione_voci_all c,
                     linea_attivita d
               WHERE a.ti_gestione = 'S'
                 AND a.esercizio = b.esercizio
                 AND a.ti_appartenenza = b.ti_appartenenza
                 AND a.ti_gestione = b.ti_gestione
                 AND a.cd_elemento_voce = b.cd_elemento_voce
                 AND a.cd_centro_responsabilita = d.cd_centro_responsabilita
                 AND a.cd_linea_attivita = d.cd_linea_attivita
                 AND b.id_classificazione = c.id_classificazione
              UNION ALL
              SELECT 'REASCI' fonte, a.esercizio + 1,
                     a.cd_centro_responsabilita, a.ti_gestione,
                     b.cd_elemento_voce,
                     NVL (d.cd_missione, 'NDF') cd_missione,
                     NVL (d.cd_programma, 'NDF') cd_programma, c.cd_livello1,
                     c.cd_livello2, c.cd_livello3, c.cd_livello4,
                     c.cd_livello5, c.cd_livello6, c.cd_livello7, c.ds_liv1,
                     c.ds_liv2, c.ds_liv3, c.ds_liv4, c.ds_liv5, c.ds_liv6,
                     c.ds_liv7, 0 previsione_ini, 0 saldo_variazioni,
                     0 tot_impacc_comp, 0 tot_impacc_res, 0 tot_manrev_comp,
                     0 tot_manrev_res, 0 tot_mod_impacc_res,
                     0 assestato_cassa,
                     DECODE
                        (a.esercizio,
                         a.esercizio_res, NVL (a.stanziamento_iniziale, 0),
                         0
                        ) previsione_ini_es_prec,
                     DECODE
                        (a.esercizio,
                         a.esercizio_res, NVL (a.assestato_iniziale, 0)
                          - NVL (a.stanziamento_iniziale, 0),
                         0
                        ) saldo_variazioni_es_prec,
                     DECODE
                         (a.esercizio,
                          a.esercizio_res, NVL (a.importo_utilizzato, 0),
                          0
                         ) tot_impacc_comp_es_prec,
                     DECODE
                        (a.esercizio,
                         a.esercizio_res, 0,
                           NVL (a.stanziamento_iniziale, 0)
                         + NVL (a.importo_ini_residui_propri, 0)
                        ) tot_impacc_res_es_prec,
                     DECODE (a.esercizio,
                             a.esercizio_res, NVL (a.importo_manrev, 0),
                             0
                            ) tot_manrev_comp_es_prec,
                     DECODE (a.esercizio,
                             a.esercizio_res, 0,
                             NVL (a.importo_manrev, 0)
                            ) tot_manrev_res_es_prec,
                     DECODE
                        (a.esercizio,
                         a.esercizio_res, 0,
                            DECODE(a.ti_gestione, 'E',
                               NVL (a.variazioni_residui_propri, 0),
                               NVL (a.variazioni_residui_propri, 0)
                             + NVL (a.variazioni_positive, 0)
                             - NVL (a.variazioni_negative, 0))
                        ) tot_mod_impacc_res_es_prec,
                     DECODE (a.esercizio,
                             a.esercizio_res, NVL (a.assestato_cassa, 0),
                             0
                            ) assestato_cassa_es_prec
                FROM v_assestato a,
                     elemento_voce b,
                     v_classificazione_voci_all c,
                     linea_attivita d
               WHERE a.ti_gestione = 'S'
                 AND a.esercizio + 1 = b.esercizio
                 AND a.ti_gestione = b.ti_gestione
                 AND ((a.esercizio = 2015
                       AND NVL(getcurrentcdelementovoce (a.esercizio + 1,
                                                         a.esercizio,
                                                         a.ti_gestione,
                                                         a.cd_elemento_voce),
                               a.cd_elemento_voce) = b.cd_elemento_voce)
                      OR
                      (a.esercizio != 2015
                       AND a.cd_elemento_voce = b.cd_elemento_voce))
                 AND a.cd_centro_responsabilita = d.cd_centro_responsabilita
                 AND a.cd_linea_attivita = d.cd_linea_attivita
                 AND b.id_classificazione = c.id_classificazione
              UNION ALL
              SELECT 'EXTSCI' fonte, a.esercizio, a.cd_centro_responsabilita,
                     a.ti_gestione, b.cd_elemento_voce,
                     NVL (a.cd_missione, 'NDF') cd_missione,
                     NVL (a.cd_programma, 'NDF') cd_programma, c.cd_livello1,
                     c.cd_livello2, c.cd_livello3, c.cd_livello4,
                     c.cd_livello5, c.cd_livello6, c.cd_livello7, c.ds_liv1,
                     c.ds_liv2, c.ds_liv3, c.ds_liv4, c.ds_liv5, c.ds_liv6,
                     c.ds_liv7, NVL (a.previsione_ini, 0) previsione_ini,
                     NVL (a.saldo_variazioni, 0) saldo_variazioni,
                     NVL (a.tot_impacc_comp, 0) tot_impacc_comp,
                     NVL (a.tot_impacc_res, 0) tot_impacc_res,
                     NVL (a.tot_manrev_comp, 0) tot_manrev_comp,
                     NVL (a.tot_manrev_res, 0) tot_manrev_res,
                     NVL (a.tot_mod_impacc_res, 0) tot_mod_impacc_res,
                     NVL (a.assestato_cassa, 0) assestato_cassa,
                     NVL (a.previsione_ini_es_prec, 0) previsione_ini_es_prec,
                     NVL
                        (a.saldo_variazioni_es_prec,
                         0
                        ) saldo_variazioni_es_prec,
                     NVL (a.tot_impacc_comp_es_prec,
                          0
                         ) tot_impacc_comp_es_prec,
                     NVL (a.tot_impacc_res_es_prec, 0) tot_impacc_res_es_prec,
                     NVL (a.tot_manrev_comp_es_prec,
                          0
                         ) tot_manrev_comp_es_prec,
                     NVL (a.tot_manrev_res_es_prec, 0) tot_manrev_res_es_prec,
                     NVL
                        (a.tot_mod_impacc_res_es_prec,
                         0
                        ) tot_mod_impacc_res_es_prec,
                     NVL (a.assestato_cassa_es_prec,
                          0
                         ) assestato_cassa_es_prec
                FROM pdg_dati_stampa_rendic_temp a,
                     elemento_voce b,
                     v_classificazione_voci_all c
               WHERE a.esercizio = b.esercizio
                 AND a.ti_gestione = b.ti_gestione
                 AND a.cd_elemento_voce = b.cd_elemento_voce
                 AND b.id_classificazione = c.id_classificazione) e,
             pdg_missione g,
             pdg_programma h
       WHERE e.cd_missione = g.cd_missione(+) AND e.cd_programma = h.cd_programma(+)
    GROUP BY e.fonte,
             e.esercizio,
             e.cd_centro_responsabilita,
             e.ti_gestione,
             e.cd_elemento_voce,
             e.cd_missione,
             e.cd_programma,
             e.cd_livello1,
             e.cd_livello2,
             e.cd_livello3,
             e.cd_livello4,
             e.cd_livello5,
             e.cd_livello6,
             e.cd_livello7,
             NVL (g.ds_missione, 'NON DEFINITO'),
             NVL (h.ds_programma, 'NON DEFINITO'),
             e.ds_liv1,
             e.ds_liv2,
             e.ds_liv3,
             e.ds_liv4,
             e.ds_liv5,
             e.ds_liv6,
             e.ds_liv7
    UNION ALL
    SELECT   e.fonte, e.esercizio, e.cd_centro_responsabilita,
             e.ti_gestione tipo, e.cd_elemento_voce,
             e.cd_livello1 cd_livello1, e.cd_livello2 cd_livello2,
             e.cd_livello3 cd_livello3, e.cd_livello4 cd_livello4,
             e.cd_livello5 cd_livello5, e.cd_livello6 cd_livello6,
             e.cd_livello7 cd_livello7, e.cd_livello8 cd_livello8,
             e.cd_livello9 cd_livello9, e.ds_liv1 ds_livello1,
             e.ds_liv2 ds_livello2, e.ds_liv3 ds_livello3,
             e.ds_liv4 ds_livello4, e.ds_liv5 ds_livello5,
             e.ds_liv6 ds_livello6, e.ds_liv7 ds_livello7,
             e.ds_liv8 ds_livello8, e.ds_liv9 ds_livello9,
             SUM (e.previsione_ini), SUM (e.saldo_variazioni),
             SUM (e.tot_impacc_comp), SUM (e.tot_impacc_res),
             SUM (e.tot_manrev_comp), SUM (e.tot_manrev_res),
             SUM (e.tot_mod_impacc_res), SUM (e.assestato_cassa),
             SUM (e.previsione_ini_es_prec), SUM (e.saldo_variazioni_es_prec),
             SUM (e.tot_impacc_comp_es_prec), SUM (e.tot_impacc_res_es_prec),
             SUM (e.tot_manrev_comp_es_prec), SUM (e.tot_manrev_res_es_prec),
             SUM (e.tot_mod_impacc_res_es_prec),
             SUM (e.assestato_cassa_es_prec)
        FROM (SELECT 'REAFIN' fonte, a.esercizio, a.cd_centro_responsabilita,
                     a.ti_gestione, b.cd_elemento_voce, c.cd_livello1,
                     c.cd_livello2, c.cd_livello3, c.cd_livello4,
                     c.cd_livello5, c.cd_livello6, c.cd_livello7,
                     NULL cd_livello8, NULL cd_livello9, c.ds_liv1, c.ds_liv2,
                     c.ds_liv3, c.ds_liv4, c.ds_liv5, c.ds_liv6, c.ds_liv7,
                     NULL ds_liv8, NULL ds_liv9,
                     DECODE (a.esercizio,
                             a.esercizio_res, NVL (a.stanziamento_iniziale, 0),
                             0
                            ) previsione_ini,
                     DECODE (a.esercizio,
                             a.esercizio_res, NVL (a.assestato_iniziale, 0)
                              - NVL (a.stanziamento_iniziale, 0),
                             0
                            ) saldo_variazioni,
                     DECODE (a.esercizio,
                             a.esercizio_res, NVL (a.importo_utilizzato, 0),
                             0
                            ) tot_impacc_comp,
                     DECODE
                           (a.esercizio,
                            a.esercizio_res, 0,
                              NVL (a.stanziamento_iniziale, 0)
                            + NVL (a.importo_ini_residui_propri, 0)
                           ) tot_impacc_res,
                     DECODE (a.esercizio,
                             a.esercizio_res, NVL (a.importo_manrev, 0),
                             0
                            ) tot_manrev_comp,
                     DECODE (a.esercizio,
                             a.esercizio_res, 0,
                             NVL (a.importo_manrev, 0)
                            ) tot_manrev_res,
                     DECODE (a.esercizio,
                             a.esercizio_res, 0,
                             DECODE(a.ti_gestione, 'E',
                               NVL (a.variazioni_residui_propri, 0),
                               NVL (a.variazioni_residui_propri, 0)
                             + NVL (a.variazioni_positive, 0)
                             - NVL (a.variazioni_negative, 0))
                            ) tot_mod_impacc_res,
                     DECODE (a.esercizio,
                             a.esercizio_res, NVL (a.assestato_cassa, 0),
                             0
                            ) assestato_cassa,
                     0 previsione_ini_es_prec, 0 saldo_variazioni_es_prec,
                     0 tot_impacc_comp_es_prec, 0 tot_impacc_res_es_prec,
                     0 tot_manrev_comp_es_prec, 0 tot_manrev_res_es_prec,
                     0 tot_mod_impacc_res_es_prec, 0 assestato_cassa_es_prec
                FROM v_assestato a,
                     elemento_voce b,
                     v_classificazione_voci_all c
               WHERE a.esercizio = b.esercizio
                 AND a.ti_appartenenza = b.ti_appartenenza
                 AND a.ti_gestione = b.ti_gestione
                 AND a.cd_elemento_voce = b.cd_elemento_voce
                 AND b.id_classificazione = c.id_classificazione
              UNION ALL
              SELECT 'REAFIN' fonte, a.esercizio + 1,
                     a.cd_centro_responsabilita, a.ti_gestione,
                     b.cd_elemento_voce, c.cd_livello1, c.cd_livello2,
                     c.cd_livello3, c.cd_livello4, c.cd_livello5,
                     c.cd_livello6, c.cd_livello7, NULL, NULL, c.ds_liv1,
                     c.ds_liv2, c.ds_liv3, c.ds_liv4, c.ds_liv5, c.ds_liv6,
                     c.ds_liv7, NULL, NULL, 0 previsione_ini,
                     0 saldo_variazioni, 0 tot_impacc_comp, 0 tot_impacc_res,
                     0 tot_manrev_comp, 0 tot_manrev_res,
                     0 tot_mod_impacc_res, 0 assestato_cassa,
                     DECODE
                        (a.esercizio,
                         a.esercizio_res, NVL (a.stanziamento_iniziale, 0),
                         0
                        ) previsione_ini_es_prec,
                     DECODE
                        (a.esercizio,
                         a.esercizio_res, NVL (a.assestato_iniziale, 0)
                          - NVL (a.stanziamento_iniziale, 0),
                         0
                        ) saldo_variazioni_es_prec,
                     DECODE
                         (a.esercizio,
                          a.esercizio_res, NVL (a.importo_utilizzato, 0),
                          0
                         ) tot_impacc_comp_es_prec,
                     DECODE
                        (a.esercizio,
                         a.esercizio_res, 0,
                           NVL (a.stanziamento_iniziale, 0)
                         + NVL (a.importo_ini_residui_propri, 0)
                        ) tot_impacc_res_es_prec,
                     DECODE (a.esercizio,
                             a.esercizio_res, NVL (a.importo_manrev, 0),
                             0
                            ) tot_manrev_comp_es_prec,
                     DECODE (a.esercizio,
                             a.esercizio_res, 0,
                             NVL (a.importo_manrev, 0)
                            ) tot_manrev_res_es_prec,
                     DECODE
                        (a.esercizio,
                         a.esercizio_res, 0,
                         DECODE(a.ti_gestione, 'E',
                               NVL (a.variazioni_residui_propri, 0),
                               NVL (a.variazioni_residui_propri, 0)
                             + NVL (a.variazioni_positive, 0)
                             - NVL (a.variazioni_negative, 0))
                        ) tot_mod_impacc_res_es_prec,
                     DECODE (a.esercizio,
                             a.esercizio_res, NVL (a.assestato_cassa, 0),
                             0
                            ) assestato_cassa_es_prec
                FROM v_assestato a,
                     elemento_voce b,
                     v_classificazione_voci_all c
               WHERE a.esercizio + 1 = b.esercizio
                 AND a.ti_gestione = b.ti_gestione
                 AND ((a.esercizio = 2015
                       AND NVL(getcurrentcdelementovoce (a.esercizio + 1,
                                                         a.esercizio,
                                                         a.ti_gestione,
                                                         a.cd_elemento_voce),
                               a.cd_elemento_voce) = b.cd_elemento_voce)
                      OR
                      (a.esercizio != 2015
                       AND a.cd_elemento_voce = b.cd_elemento_voce))
                 AND b.id_classificazione = c.id_classificazione) e
    GROUP BY e.fonte,
             e.esercizio,
             e.cd_centro_responsabilita,
             e.ti_gestione,
             e.cd_elemento_voce,
             e.cd_livello1,
             e.cd_livello2,
             e.cd_livello3,
             e.cd_livello4,
             e.cd_livello5,
             e.cd_livello6,
             e.cd_livello7,
             e.cd_livello8,
             e.cd_livello9,
             e.ds_liv1,
             e.ds_liv2,
             e.ds_liv3,
             e.ds_liv4,
             e.ds_liv5,
             e.ds_liv6,
             e.ds_liv7,
             e.ds_liv8,
             e.ds_liv9
    UNION ALL
    SELECT   'EXTFIN' fonte, a.esercizio, a.cd_centro_responsabilita,
             a.ti_gestione, b.cd_elemento_voce, c.cd_livello1, c.cd_livello2,
             c.cd_livello3, c.cd_livello4, c.cd_livello5, c.cd_livello6,
             c.cd_livello7, NULL, NULL, c.ds_liv1, c.ds_liv2, c.ds_liv3,
             c.ds_liv4, c.ds_liv5, c.ds_liv6, c.ds_liv7, NULL, NULL,
             NVL (SUM (a.previsione_ini), 0) previsione_ini,
             NVL (SUM (a.saldo_variazioni), 0) saldo_variazioni,
             NVL (SUM (a.tot_impacc_comp), 0) tot_impacc_comp,
             NVL (SUM (a.tot_impacc_res), 0) tot_impacc_res,
             NVL (SUM (a.tot_manrev_comp), 0) tot_manrev_comp,
             NVL (SUM (a.tot_manrev_res), 0) tot_manrev_res,
             NVL (SUM (a.tot_mod_impacc_res), 0) tot_mod_impacc_res,
             NVL (SUM (a.assestato_cassa), 0) assestato_cassa,
             NVL (SUM (a.previsione_ini_es_prec), 0) previsione_ini_es_prec,
             NVL (SUM (a.saldo_variazioni_es_prec),
                  0
                 ) saldo_variazioni_es_prec,
             NVL (SUM (a.tot_impacc_comp_es_prec), 0) tot_impacc_comp_es_prec,
             NVL (SUM (a.tot_impacc_res_es_prec), 0) tot_impacc_res_es_prec,
             NVL (SUM (a.tot_manrev_comp_es_prec), 0) tot_manrev_comp_es_prec,
             NVL (SUM (a.tot_manrev_res_es_prec), 0) tot_manrev_res_es_prec,
             NVL
                (SUM (a.tot_mod_impacc_res_es_prec),
                 0
                ) tot_mod_impacc_res_es_prec,
             NVL (SUM (a.assestato_cassa_es_prec), 0) assestato_cassa_es_prec
        FROM pdg_dati_stampa_rendic_temp a,
             elemento_voce b,
             v_classificazione_voci_all c
       WHERE a.esercizio = b.esercizio
         AND a.ti_gestione = b.ti_gestione
         AND a.cd_elemento_voce = b.cd_elemento_voce
         AND b.id_classificazione = c.id_classificazione
    GROUP BY a.esercizio,
             a.ti_gestione,
             a.cd_centro_responsabilita,
             b.cd_elemento_voce,
             c.cd_livello1,
             c.cd_livello2,
             c.cd_livello3,
             c.cd_livello4,
             c.cd_livello5,
             c.cd_livello6,
             c.cd_livello7,
             c.ds_liv1,
             c.ds_liv2,
             c.ds_liv3,
             c.ds_liv4,
             c.ds_liv5,
             c.ds_liv6,
             c.ds_liv7) ;
