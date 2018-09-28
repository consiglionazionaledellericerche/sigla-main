--------------------------------------------------------
--  DDL for View V_SALDI_PIANO_ECONOM_PROGETTO
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_SALDI_PIANO_ECONOM_PROGETTO" ("PG_PROGETTO", "ESERCIZIO", "CD_UNITA_PIANO", "CD_VOCE_PIANO", "TI_GESTIONE", "DISP", "STANZIAMENTO", "VARIAPIU", "VARIAMENO", "IMPACC", "MANRIS") AS 
  (SELECT   x.pg_progetto, x.esercizio, x.cd_unita_piano, x.cd_voce_piano,
             x.ti_gestione, SUM (x.disp) disp,
             SUM (x.stanziamento) stanziamento,
             SUM (x.variapiu) variapiu,
             SUM (x.variameno) variameno,
             SUM (x.impacc) impacc,
             SUM (x.manris) manris
        FROM (SELECT a.pg_progetto, a.esercizio_piano esercizio,
                     a.cd_unita_organizzativa cd_unita_piano, a.cd_voce_piano,
                     'E' ti_gestione, NVL (a.im_entrata, 0) disp,
                     0 stanziamento, 0 variapiu, 0 variameno, 0 impacc, 0 manris
                FROM progetto_piano_economico a
              UNION ALL
              SELECT a.pg_progetto, a.esercizio_piano esercizio,
                     a.cd_unita_organizzativa cd_unita_piano, a.cd_voce_piano,
                     'S' ti_gestione, NVL(a.im_spesa_finanziato, 0)+NVL(a.im_spesa_cofinanziato, 0) disp,
                     0 stanziamento, 0 variapiu, 0 variameno, 0 impacc, 0 manris
                FROM progetto_piano_economico a
              UNION ALL
              --IMPORTI PER ANNO 0
              SELECT a.pg_progetto, 0 esercizio, a.cd_unita_piano,
                     a.cd_voce_piano, 'E' ti_gestione, 0 disp,
                     NVL (a.im_entrata, 0) stanziamento, 0 variapiu,
                     0 variameno, 0 impacc, 0 manris
                FROM pdg_modulo_entrate a
              UNION ALL
              SELECT a.pg_progetto, 0 esercizio, a.cd_unita_piano,
                     a.cd_voce_piano, 'S' ti_gestione, 0 disp,
                       NVL (a.im_spese_gest_decentrata_int, 0)
                     + NVL (a.im_spese_gest_decentrata_est, 0)
                     + NVL (a.im_spese_gest_accentrata_int, 0)
                     + NVL (a.im_spese_gest_accentrata_est, 0) stanziamento,
                     0 variapiu, 0 variameno, 0 impacc, 0 manris
                FROM pdg_modulo_spese a
              UNION ALL
              SELECT c.pg_progetto, 0 esercizio, d.cd_unita_organizzativa,
                     d.cd_voce_piano, b.ti_gestione, 0 disp, 0 stanziamento,
                     CASE
                        WHEN b.ti_gestione = 'E'
                           THEN CASE
                                  WHEN NVL (b.im_entrata, 0) > 0
                                     THEN NVL (b.im_entrata, 0)
                                  ELSE 0
                               END
                        ELSE CASE
                        WHEN   NVL (b.im_spese_gest_decentrata_int,
                                    0
                                   )
                             + NVL (b.im_spese_gest_decentrata_est, 0)
                             + NVL (b.im_spese_gest_accentrata_int, 0)
                             + NVL (b.im_spese_gest_accentrata_est, 0) > 0
                           THEN   NVL (b.im_spese_gest_decentrata_int, 0)
                                + NVL (b.im_spese_gest_decentrata_est, 0)
                                + NVL (b.im_spese_gest_accentrata_int, 0)
                                + NVL (b.im_spese_gest_accentrata_est, 0)
                        ELSE 0
                     END
                     END variapiu,
                     CASE
                        WHEN b.ti_gestione = 'E'
                           THEN CASE
                                  WHEN NVL (b.im_entrata, 0) < 0
                                     THEN NVL (b.im_entrata, 0)
                                  ELSE 0
                               END
                        ELSE CASE
                        WHEN   NVL (b.im_spese_gest_decentrata_int,
                                    0
                                   )
                             + NVL (b.im_spese_gest_decentrata_est, 0) < 0
                           THEN ABS (  NVL (b.im_spese_gest_decentrata_int, 0)
                                     + NVL (b.im_spese_gest_decentrata_est, 0)
                                    )
                        ELSE CASE
                        WHEN   NVL (b.im_spese_gest_accentrata_int, 0)
                             + NVL (b.im_spese_gest_accentrata_est, 0) > 0
                           THEN DECODE (b.cd_cdr_assegnatario_clgs,
                                        NULL, NVL
                                              (b.im_spese_gest_accentrata_int,
                                               0
                                              )
                                         + NVL
                                              (b.im_spese_gest_accentrata_est,
                                               0
                                              ),
                                        0
                                       )
                        ELSE 0
                     END
                     END
                     END variameno, 0 impacc, 0 manris
                FROM pdg_variazione a,
                     pdg_variazione_riga_gest b,
                     v_linea_attivita_valida c,
                     ass_progetto_piaeco_voce d
               WHERE a.esercizio = b.esercizio
                 AND a.pg_variazione_pdg = b.pg_variazione_pdg
                 AND a.stato IN ('APP', 'APF')
                 AND b.esercizio = c.esercizio
                 AND b.cd_cdr_assegnatario = c.cd_centro_responsabilita
                 AND b.cd_linea_attivita = c.cd_linea_attivita
                 AND c.pg_progetto = d.pg_progetto
                 AND d.esercizio_piano = 0
                 AND d.esercizio_voce = b.esercizio
                 AND d.ti_appartenenza = b.ti_appartenenza
                 AND d.ti_gestione = b.ti_gestione
                 AND d.cd_elemento_voce = b.cd_elemento_voce
              UNION ALL
              SELECT c.pg_progetto, 0 esercizio_res, d.cd_unita_organizzativa,
                     d.cd_voce_piano, b.ti_gestione, 0 disp, 0 stanziamento,
                     CASE
                        WHEN NVL (b.im_variazione, 0) > 0
                           THEN NVL (b.im_variazione, 0)
                        ELSE 0
                     END variapiu,
                     CASE
                        WHEN NVL (b.im_variazione, 0) < 0
                        THEN ABS (NVL (b.im_variazione, 0))
                        ELSE 0
                     END variameno, 0 impacc, 0 manris
                FROM var_stanz_res a,
                     var_stanz_res_riga b,
                     v_linea_attivita_valida c,
                     ass_progetto_piaeco_voce d
               WHERE a.esercizio = b.esercizio
                 AND a.pg_variazione = b.pg_variazione
                 AND a.stato IN ('APP', 'APF')
                 AND b.esercizio = c.esercizio
                 AND b.cd_cdr = c.cd_centro_responsabilita
                 AND b.cd_linea_attivita = c.cd_linea_attivita
                 AND c.pg_progetto = d.pg_progetto
                 AND d.esercizio_piano = 0
                 AND d.esercizio_voce = b.esercizio
                 AND d.ti_appartenenza = b.ti_appartenenza
                 AND d.ti_gestione = b.ti_gestione
                 AND d.cd_elemento_voce = b.cd_elemento_voce
              UNION ALL
              SELECT b.pg_progetto, 0 esercizio_res, c.cd_unita_organizzativa,
                     c.cd_voce_piano, a.ti_gestione, 0 disp, 0 stanziamento,
                     0 variapiu, 0 variameno,
                     CASE
                        WHEN a.ESERCIZIO = a.ESERCIZIO_RES
                        THEN NVL(a.IM_OBBL_ACC_COMP, 0)
                        ELSE NVL(a.IM_OBBL_RES_IMP, 0) +
                             NVL(a.VAR_PIU_OBBL_RES_IMP, 0) - NVL(a.VAR_MENO_OBBL_RES_IMP, 0) +
                             NVL(a.VAR_PIU_OBBL_RES_PRO, 0) - NVL(a.VAR_MENO_OBBL_RES_PRO, 0)
                     END impacc,
                     NVL(a.IM_MANDATI_REVERSALI_PRO, 0) + NVL(a.IM_MANDATI_REVERSALI_IMP, 0) manris
                FROM voce_f_saldi_cdr_linea a,
                     v_linea_attivita_valida b,
                     ass_progetto_piaeco_voce c
               WHERE a.esercizio = b.esercizio
                 AND a.cd_centro_responsabilita = b.cd_centro_responsabilita
                 AND a.cd_linea_attivita = b.cd_linea_attivita
                 AND b.pg_progetto = c.pg_progetto
                 AND c.esercizio_piano = 0
                 AND c.esercizio_voce = a.esercizio
                 AND c.ti_appartenenza = a.ti_appartenenza
                 AND c.ti_gestione = a.ti_gestione
                 AND c.cd_elemento_voce = a.cd_elemento_voce
              UNION ALL
              --IMPORTI PER ANNO != 0
              SELECT a.pg_progetto, a.esercizio, a.cd_unita_piano,
                     a.cd_voce_piano, 'E' ti_gestione, 0 disp,
                     NVL (a.im_entrata, 0) stanziamento, 0 variapiu,
                     0 variameno, 0 impacc, 0 manris
                FROM pdg_modulo_entrate a
              UNION ALL
              SELECT a.pg_progetto, a.esercizio, a.cd_unita_piano,
                     a.cd_voce_piano, 'S' ti_gestione, 0 disp,
                       NVL (a.im_spese_gest_decentrata_int, 0)
                     + NVL (a.im_spese_gest_decentrata_est, 0)
                     + NVL (a.im_spese_gest_accentrata_int, 0)
                     + NVL (a.im_spese_gest_accentrata_est, 0) stanziamento,
                     0 variapiu, 0 variameno, 0 impacc, 0 manris
                FROM pdg_modulo_spese a
              UNION ALL
              SELECT c.pg_progetto, a.esercizio, d.cd_unita_organizzativa,
                     d.cd_voce_piano, b.ti_gestione, 0 disp, 0 stanziamento,
                     CASE
                        WHEN b.ti_gestione = 'E'
                           THEN CASE
                                  WHEN NVL (b.im_entrata, 0) > 0
                                     THEN NVL (b.im_entrata, 0)
                                  ELSE 0
                               END
                        ELSE CASE
                        WHEN   NVL (b.im_spese_gest_decentrata_int,
                                    0
                                   )
                             + NVL (b.im_spese_gest_decentrata_est, 0)
                             + NVL (b.im_spese_gest_accentrata_int, 0)
                             + NVL (b.im_spese_gest_accentrata_est, 0) > 0
                           THEN   NVL (b.im_spese_gest_decentrata_int, 0)
                                + NVL (b.im_spese_gest_decentrata_est, 0)
                                + NVL (b.im_spese_gest_accentrata_int, 0)
                                + NVL (b.im_spese_gest_accentrata_est, 0)
                        ELSE 0
                     END
                     END variapiu,
                     CASE
                        WHEN b.ti_gestione = 'E'
                           THEN CASE
                                  WHEN NVL (b.im_entrata, 0) < 0
                                     THEN NVL (b.im_entrata, 0)
                                  ELSE 0
                               END
                        ELSE CASE
                        WHEN   NVL (b.im_spese_gest_decentrata_int,
                                    0
                                   )
                             + NVL (b.im_spese_gest_decentrata_est, 0) < 0
                           THEN ABS (  NVL (b.im_spese_gest_decentrata_int, 0)
                                     + NVL (b.im_spese_gest_decentrata_est, 0)
                                    )
                        ELSE CASE
                        WHEN   NVL (b.im_spese_gest_accentrata_int, 0)
                             + NVL (b.im_spese_gest_accentrata_est, 0) > 0
                           THEN DECODE (b.cd_cdr_assegnatario_clgs,
                                        NULL, NVL
                                              (b.im_spese_gest_accentrata_int,
                                               0
                                              )
                                         + NVL
                                              (b.im_spese_gest_accentrata_est,
                                               0
                                              ),
                                        0
                                       )
                        ELSE 0
                     END
                     END
                     END variameno, 0 impacc, 0 manris
                FROM pdg_variazione a,
                     pdg_variazione_riga_gest b,
                     v_linea_attivita_valida c,
                     ass_progetto_piaeco_voce d
               WHERE a.esercizio = b.esercizio
                 AND a.pg_variazione_pdg = b.pg_variazione_pdg
                 AND a.stato IN ('APP', 'APF')
                 AND b.esercizio = c.esercizio
                 AND b.cd_cdr_assegnatario = c.cd_centro_responsabilita
                 AND b.cd_linea_attivita = c.cd_linea_attivita
                 AND c.pg_progetto = d.pg_progetto
                 AND d.esercizio_piano = b.esercizio
                 AND d.esercizio_voce = b.esercizio
                 AND d.ti_appartenenza = b.ti_appartenenza
                 AND d.ti_gestione = b.ti_gestione
                 AND d.cd_elemento_voce = b.cd_elemento_voce
              UNION ALL
              SELECT c.pg_progetto, a.esercizio_res, d.cd_unita_organizzativa,
                     d.cd_voce_piano, b.ti_gestione, 0 disp, 0 stanziamento,
                     CASE
                        WHEN NVL (b.im_variazione, 0) > 0
                           THEN NVL (b.im_variazione, 0)
                        ELSE 0
                     END variapiu,
                     CASE
                        WHEN NVL (b.im_variazione, 0) < 0
                           THEN ABS (NVL (b.im_variazione, 0))
                        ELSE 0
                     END variameno, 0 impacc, 0 manris
                FROM var_stanz_res a,
                     var_stanz_res_riga b,
                     v_linea_attivita_valida c,
                     ass_progetto_piaeco_voce d
               WHERE a.esercizio = b.esercizio
                 AND a.pg_variazione = b.pg_variazione
                 AND a.stato IN ('APP', 'APF')
                 AND b.esercizio = c.esercizio
                 AND b.cd_cdr = c.cd_centro_responsabilita
                 AND b.cd_linea_attivita = c.cd_linea_attivita
                 AND c.pg_progetto = d.pg_progetto
                 AND d.esercizio_piano = b.esercizio
                 AND d.esercizio_voce = b.esercizio
                 AND d.ti_appartenenza = b.ti_appartenenza
                 AND d.ti_gestione = b.ti_gestione
                 AND d.cd_elemento_voce = b.cd_elemento_voce
              UNION ALL
              SELECT b.pg_progetto, a.esercizio_res, c.cd_unita_organizzativa,
                     c.cd_voce_piano, a.ti_gestione, 0 disp, 0 stanziamento,
                     0 variapiu, 0 variameno,
                     CASE
                        WHEN a.ESERCIZIO = a.ESERCIZIO_RES
                        THEN NVL(a.IM_OBBL_ACC_COMP, 0)
                        ELSE NVL(a.IM_OBBL_RES_IMP, 0) +
                             NVL(a.VAR_PIU_OBBL_RES_IMP, 0) - NVL(a.VAR_MENO_OBBL_RES_IMP, 0) +
                             NVL(a.VAR_PIU_OBBL_RES_PRO, 0) - NVL(a.VAR_MENO_OBBL_RES_PRO, 0)
                     END impacc,
                     NVL(a.IM_MANDATI_REVERSALI_PRO, 0) + NVL(a.IM_MANDATI_REVERSALI_IMP, 0) manris
                FROM voce_f_saldi_cdr_linea a,
                     v_linea_attivita_valida b,
                     ass_progetto_piaeco_voce c
               WHERE a.esercizio = b.esercizio
                 AND a.cd_centro_responsabilita = b.cd_centro_responsabilita
                 AND a.cd_linea_attivita = b.cd_linea_attivita
                 AND b.pg_progetto = c.pg_progetto
                 AND c.esercizio_piano = b.esercizio
                 AND c.esercizio_voce = a.esercizio
                 AND c.ti_appartenenza = a.ti_appartenenza
                 AND c.ti_gestione = a.ti_gestione
                 AND c.cd_elemento_voce = a.cd_elemento_voce) x
    GROUP BY x.pg_progetto,
             x.esercizio,
             x.cd_unita_piano,
             x.cd_voce_piano,
             x.ti_gestione);
