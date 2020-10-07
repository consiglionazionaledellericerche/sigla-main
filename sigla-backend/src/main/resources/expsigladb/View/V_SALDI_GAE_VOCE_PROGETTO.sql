--------------------------------------------------------
--  DDL for View V_SALDI_GAE_VOCE_PROGETTO
--------------------------------------------------------

CREATE OR REPLACE FORCE VIEW "V_SALDI_GAE_VOCE_PROGETTO" ("PG_PROGETTO", "ESERCIZIO", "CD_CENTRO_RESPONSABILITA", "CD_LINEA_ATTIVITA", "ESERCIZIO_VOCE", "TI_APPARTENENZA", "TI_GESTIONE", "CD_ELEMENTO_VOCE", "STANZIAMENTO_FIN", "VARIAPIU_FIN", "VARIAMENO_FIN", "TRASFPIU_FIN", "TRASFMENO_FIN", "STANZIAMENTO_COFIN", "VARIAPIU_COFIN", "VARIAMENO_COFIN", "TRASFPIU_COFIN", "TRASFMENO_COFIN", "IMPACC_FIN", "IMPACC_COFIN", "MANRIS_FIN", "MANRIS_COFIN") AS
  (SELECT    x.pg_progetto, x.esercizio,
             x.cd_centro_responsabilita, x.cd_linea_attivita,
             x.esercizio, x.ti_appartenenza, x.ti_gestione, x.cd_elemento_voce,
             SUM (x.stanziamento_fin) stanziamento_fin,
             SUM (x.variapiu_fin) variapiu_fin,
             SUM (x.variameno_fin) variameno_fin,
             SUM (x.trasfpiu_fin) trasfpiu_fin,
             SUM (x.trasfmeno_fin) trasfmeno_fin,
             SUM (x.stanziamento_cofin) stanziamento_cofin,
             SUM (x.variapiu_cofin) variapiu_cofin,
             SUM (x.variameno_cofin) variameno_cofin,
             SUM (x.trasfpiu_cofin) trasfpiu_cofin,
             SUM (x.trasfmeno_cofin) trasfmeno_cofin,
             SUM (x.impacc_fin) impacc_fin,
             SUM (x.impacc_cofin) impacc_cofin,
             SUM (x.manris_fin) manris_fin,
             SUM (x.manris_cofin) manris_cofin
        FROM (SELECT a.pg_progetto, a.esercizio,
                     a.cd_cdr_assegnatario cd_centro_responsabilita, a.cd_linea_attivita,
                     a.esercizio esercizio_voce, a.ti_appartenenza, a.ti_gestione, a.cd_elemento_voce,
                     NVL(a.im_spese_gest_decentrata_est, 0) stanziamento_fin,
                     0 variapiu_fin, 0 variameno_fin,
                     0 trasfpiu_fin, 0 trasfmeno_fin,
                     NVL (a.im_spese_gest_decentrata_int, 0) stanziamento_cofin,
                     0 variapiu_cofin, 0 variameno_cofin,
                     0 trasfpiu_cofin, 0 trasfmeno_cofin,
                     0 impacc_fin, 0 impacc_cofin, 0 manris_fin, 0 manris_cofin
                FROM pdg_modulo_spese_gest a
              WHERE NVL (a.im_spese_gest_decentrata_est, 0) != 0
              OR    NVL (a.im_spese_gest_decentrata_int, 0) != 0
              UNION ALL
              SELECT c.pg_progetto, b.esercizio,
                     b.cd_cdr_assegnatario, b.cd_linea_attivita,
                     b.esercizio, b.ti_appartenenza, b.ti_gestione, b.cd_elemento_voce,
                     0 stanziamento_fin,
                     CASE
                       WHEN NVL(b.im_spese_gest_decentrata_est, 0) != 0
                       THEN CASE
                              WHEN NVL(b.im_spese_gest_decentrata_est, 0)>0
                              THEN NVL(b.im_spese_gest_decentrata_est, 0)
                              ELSE 0
                            END
                       ELSE CASE
                               WHEN NVL (b.im_spese_gest_accentrata_est, 0) > 0 AND
                                    b.cd_cdr_assegnatario_clgs is not null AND b.categoria_dettaglio!='SCR'
                               THEN NVL(b.im_spese_gest_accentrata_est, 0)
                               ELSE 0
                             END
                     END variapiu_fin,
                     CASE
                       WHEN NVL(b.im_spese_gest_decentrata_est, 0) != 0
                       THEN CASE
                              WHEN NVL(b.im_spese_gest_decentrata_est, 0)<0
                              THEN ABS(NVL(b.im_spese_gest_decentrata_est, 0))
                              ELSE 0
                            END
                       ELSE CASE
                              WHEN NVL (b.im_spese_gest_accentrata_est, 0) < 0 AND
                                   b.cd_cdr_assegnatario_clgs is not null AND b.categoria_dettaglio!='SCR'
                              THEN ABS(NVL(b.im_spese_gest_accentrata_est, 0))
                              ELSE 0
                            END
                     END variameno_fin,
                     CASE
                       WHEN NVL(a.ti_motivazione_variazione,'X') in ('BAN','PRG','ALT','TAE','TAU','INC','RAG')
                       THEN CASE
                              WHEN NVL(b.im_spese_gest_decentrata_est, 0) != 0
                              THEN CASE
                                     WHEN NVL(b.im_spese_gest_decentrata_est, 0)>0
                                     THEN NVL(b.im_spese_gest_decentrata_est, 0)
                                     ELSE 0
                                   END
                              ELSE CASE
                                     WHEN NVL (b.im_spese_gest_accentrata_est, 0) > 0 AND
                                          b.cd_cdr_assegnatario_clgs is not null AND b.categoria_dettaglio!='SCR'
                                      THEN NVL(b.im_spese_gest_accentrata_est, 0)
                                      ELSE 0
                                    END
                            END
                       ELSE 0
                     END trasfpiu_fin,
                     CASE
                       WHEN NVL(a.ti_motivazione_variazione,'X') in ('BAN','PRG','ALT','TAE','TAU','INC','RAG')
                       THEN CASE
                              WHEN NVL(b.im_spese_gest_decentrata_est, 0) != 0
                              THEN CASE
                                     WHEN NVL(b.im_spese_gest_decentrata_est, 0)<0
                                     THEN ABS(NVL(b.im_spese_gest_decentrata_est, 0))
                                     ELSE 0
                                   END
                              ELSE CASE
                                     WHEN NVL (b.im_spese_gest_accentrata_est, 0) < 0 AND
                                          b.cd_cdr_assegnatario_clgs is not null AND b.categoria_dettaglio!='SCR'
                                     THEN ABS(NVL(b.im_spese_gest_accentrata_est, 0))
                                     ELSE 0
                                   END
                            END
                       ELSE CASE --INDICO COME TRASFERIMENTO NEGATIVO LE SOMME ASSEGNATE A VOCI ACCENTRATE CHE VENGONO GIRATE
                              WHEN NVL(b.im_spese_gest_decentrata_est, 0) = 0 AND
                                   NVL (b.im_spese_gest_accentrata_est, 0) > 0 AND
                                   a.ti_motivazione_variazione is null AND
                                   (b.cd_cdr_assegnatario_clgs is null OR b.categoria_dettaglio='SCR')
                              THEN NVL(b.im_spese_gest_accentrata_est, 0)
                              ELSE 0
                            END
                     END trasfmeno_fin,
                     0 stanziamento_cofin,
                     CASE
                       WHEN NVL(b.im_spese_gest_decentrata_int, 0) != 0
                       THEN CASE
                              WHEN NVL(b.im_spese_gest_decentrata_int, 0)>0
                              THEN NVL(b.im_spese_gest_decentrata_int, 0)
                              ELSE 0
                            END
                       ELSE CASE
                               WHEN NVL (b.im_spese_gest_accentrata_int, 0) > 0 AND
                                    b.cd_cdr_assegnatario_clgs is not null AND b.categoria_dettaglio!='SCR'
                               THEN NVL(b.im_spese_gest_accentrata_int, 0)
                               ELSE 0
                             END
                     END variapiu_cofin,
                     CASE
                       WHEN NVL(b.im_spese_gest_decentrata_int, 0) != 0
                       THEN CASE
                              WHEN NVL(b.im_spese_gest_decentrata_int, 0)<0
                              THEN ABS(NVL(b.im_spese_gest_decentrata_int, 0))
                              ELSE 0
                            END
                       ELSE CASE
                              WHEN NVL (b.im_spese_gest_accentrata_int, 0) < 0 AND
                                   b.cd_cdr_assegnatario_clgs is not null AND b.categoria_dettaglio!='SCR'
                            THEN ABS(NVL(b.im_spese_gest_accentrata_int, 0))
                              ELSE 0
                            END
                     END variameno_cofin,
                     CASE
                       WHEN NVL(a.ti_motivazione_variazione,'X') in ('BAN','PRG','ALT','TAE','TAU','INC','RAG')
                       THEN CASE
                              WHEN NVL(b.im_spese_gest_decentrata_int, 0) != 0
                              THEN CASE
                                     WHEN NVL(b.im_spese_gest_decentrata_int, 0)>0
                                     THEN NVL(b.im_spese_gest_decentrata_int, 0)
                                     ELSE 0
                                   END
                              ELSE CASE
                                      WHEN NVL (b.im_spese_gest_accentrata_int, 0) > 0 AND
                                           b.cd_cdr_assegnatario_clgs is not null AND b.categoria_dettaglio!='SCR'
                                      THEN NVL(b.im_spese_gest_accentrata_int, 0)
                                      ELSE 0
                                    END
                            END
                       ELSE 0
                     END trasfpiu_cofin,
                     CASE
                       WHEN NVL(a.ti_motivazione_variazione,'X') in ('BAN','PRG','ALT','TAE','TAU','INC','RAG')
                       THEN CASE
                              WHEN NVL(b.im_spese_gest_decentrata_int, 0) != 0
                              THEN CASE
                                     WHEN NVL(b.im_spese_gest_decentrata_int, 0)<0
                                     THEN ABS(NVL(b.im_spese_gest_decentrata_int, 0))
                                     ELSE 0
                                   END
                              ELSE CASE
                                     WHEN NVL (b.im_spese_gest_accentrata_int, 0) < 0 AND
                                          b.cd_cdr_assegnatario_clgs is not null AND b.categoria_dettaglio!='SCR'
                                     THEN ABS(NVL(b.im_spese_gest_accentrata_int, 0))
                                     ELSE 0
                                   END
                            END
                       ELSE CASE --INDICO COME TRASFERIMENTO NEGATIVO LE SOMME ASSEGNATE A VOCI ACCENTRATE CHE VENGONO GIRATE
                              WHEN NVL(b.im_spese_gest_decentrata_int, 0) = 0 AND
                                   NVL (b.im_spese_gest_accentrata_int, 0) > 0 AND
                                   a.ti_motivazione_variazione is null AND
                                   (b.cd_cdr_assegnatario_clgs is null OR b.categoria_dettaglio='SCR')
                              THEN NVL(b.im_spese_gest_accentrata_int, 0)
                              ELSE 0
                            END
                     END trasfmeno_cofin,
                     0 impacc_fin, 0 impacc_cofin, 0 manris_fin, 0 manris_cofin
                FROM pdg_variazione a,
                     pdg_variazione_riga_gest b,
                     v_linea_attivita_valida c
               WHERE a.esercizio = b.esercizio
                 AND a.pg_variazione_pdg = b.pg_variazione_pdg
                 AND a.stato IN ('APP', 'APF', 'PRD')
                 AND b.ti_gestione = 'S'
                 AND b.esercizio = c.esercizio
                 AND b.cd_cdr_assegnatario = c.cd_centro_responsabilita
                 AND b.cd_linea_attivita = c.cd_linea_attivita
                 AND c.cd_natura not in (select val01 from configurazione_cnr e
                                         where e.esercizio = 0
                                         and   e.cd_unita_funzionale = '*'
                                         and   e.cd_chiave_primaria = 'PROGETTI'
                                         and   e.cd_chiave_secondaria = 'NATURA_REIMPIEGO')
              UNION ALL
              SELECT c.pg_progetto, b.esercizio,
                     b.cd_cdr_assegnatario, b.cd_linea_attivita,
                     b.esercizio, b.ti_appartenenza, b.ti_gestione, b.cd_elemento_voce,
                     0 stanziamento_fin,
                     0 variapiu_fin, 0 variameno_fin,
                     0 trasfpiu_fin, 0 trasfmeno_fin,
                     0 stanziamento_cofin,
                     CASE
                       WHEN NVL(b.im_spese_gest_decentrata_est, 0) != 0
                       THEN CASE
                              WHEN NVL(b.im_spese_gest_decentrata_est, 0)>0
                              THEN NVL(b.im_spese_gest_decentrata_est, 0)
                              ELSE 0
                            END
                       ELSE CASE
                               WHEN NVL (b.im_spese_gest_accentrata_est, 0) > 0 AND
                                    b.cd_cdr_assegnatario_clgs is not null AND b.categoria_dettaglio!='SCR'
                               THEN NVL(b.im_spese_gest_accentrata_est, 0)
                               ELSE 0
                             END
                     END variapiu_cofin,
                     CASE
                       WHEN NVL(b.im_spese_gest_decentrata_est, 0) != 0
                       THEN CASE
                              WHEN NVL(b.im_spese_gest_decentrata_est, 0)<0
                              THEN ABS(NVL(b.im_spese_gest_decentrata_est, 0))
                              ELSE 0
                            END
                       ELSE CASE
                              WHEN NVL (b.im_spese_gest_accentrata_est, 0) < 0 AND
                                   b.cd_cdr_assegnatario_clgs is not null AND b.categoria_dettaglio!='SCR'
                              THEN ABS(NVL(b.im_spese_gest_accentrata_est, 0))
                              ELSE 0
                            END
                     END variameno_cofin,
                     CASE
                       WHEN NVL(a.ti_motivazione_variazione,'X') in ('BAN','PRG','ALT','TAE','TAU','INC','RAG')
                       THEN CASE
                              WHEN NVL(b.im_spese_gest_decentrata_est, 0) != 0
                              THEN CASE
                                     WHEN NVL(b.im_spese_gest_decentrata_est, 0)>0
                                     THEN NVL(b.im_spese_gest_decentrata_est, 0)
                                     ELSE 0
                                   END
                              ELSE CASE
                                      WHEN NVL (b.im_spese_gest_accentrata_est, 0) > 0 AND
                                           b.cd_cdr_assegnatario_clgs is not null AND b.categoria_dettaglio!='SCR'
                                      THEN NVL(b.im_spese_gest_accentrata_est, 0)
                                      ELSE 0
                                    END
                              END
                       ELSE 0
                     END trasfpiu_cofin,
                     CASE
                       WHEN NVL(a.ti_motivazione_variazione,'X') in ('BAN','PRG','ALT','TAE','TAU','INC','RAG')
                       THEN CASE
                              WHEN NVL(b.im_spese_gest_decentrata_est, 0) != 0
                              THEN CASE
                                     WHEN NVL(b.im_spese_gest_decentrata_est, 0)<0
                                     THEN ABS(NVL(b.im_spese_gest_decentrata_est, 0))
                                     ELSE 0
                                   END
                              ELSE CASE
                                     WHEN NVL (b.im_spese_gest_accentrata_est, 0) < 0 AND
                                          b.cd_cdr_assegnatario_clgs is not null AND b.categoria_dettaglio!='SCR'
                                     THEN ABS(NVL(b.im_spese_gest_accentrata_est, 0))
                                     ELSE 0
                                   END
                              END
                       ELSE CASE --INDICO COME TRASFERIMENTO NEGATIVO LE SOMME ASSEGNATE A VOCI ACCENTRATE CHE VENGONO GIRATE
                              WHEN NVL(b.im_spese_gest_decentrata_est, 0) = 0 AND
                                   NVL (b.im_spese_gest_accentrata_est, 0) > 0 AND
                                   a.ti_motivazione_variazione is null AND
                                   (b.cd_cdr_assegnatario_clgs is null OR b.categoria_dettaglio='SCR')
                              THEN NVL(b.im_spese_gest_accentrata_est, 0)
                              ELSE 0
                            END
                     END trasfmeno_cofin,
                     0 impacc_fin, 0 impacc_cofin, 0 manris_fin, 0 manris_cofin
                FROM pdg_variazione a,
                     pdg_variazione_riga_gest b,
                     v_linea_attivita_valida c
               WHERE a.esercizio = b.esercizio
                 AND a.pg_variazione_pdg = b.pg_variazione_pdg
                 AND a.stato IN ('APP', 'APF', 'PRD')
                 AND b.ti_gestione = 'S'
                 AND b.esercizio = c.esercizio
                 AND b.cd_cdr_assegnatario = c.cd_centro_responsabilita
                 AND b.cd_linea_attivita = c.cd_linea_attivita
                 AND c.cd_natura in (select val01 from configurazione_cnr e
                                     where e.esercizio = 0
                                     and   e.cd_unita_funzionale = '*'
                                     and   e.cd_chiave_primaria = 'PROGETTI'
                                     and   e.cd_chiave_secondaria = 'NATURA_REIMPIEGO')
              UNION ALL
              SELECT c.pg_progetto, b.esercizio_res,
                     b.cd_cdr, b.cd_linea_attivita,
                     b.esercizio_voce, b.ti_appartenenza, b.ti_gestione, b.cd_elemento_voce,
                     0 stanziamento_fin,
                     CASE
                        WHEN a.tipologia_fin='FES' AND NVL(b.im_variazione, 0) > 0
                        THEN NVL(b.im_variazione, 0)
                        ELSE 0
                     END variapiu_fin,
                     CASE
                        WHEN a.tipologia_fin='FES' AND NVL(b.im_variazione, 0) < 0
                        THEN ABS(NVL (b.im_variazione, 0))
                        ELSE 0
                     END variameno_fin,
                     CASE
                        WHEN a.tipologia_fin='FES' and NVL(b.im_variazione, 0) > 0 AND
                             NVL(a.ti_motivazione_variazione,'X') in ('BAN','PRG','ALT','TAE','TAU','INC','RAG')
                        THEN NVL(b.im_variazione, 0)
                        ELSE 0
                     END trasfpiu_fin,
                     CASE
                        WHEN a.tipologia_fin='FES' and NVL(b.im_variazione, 0) < 0 AND
                             NVL(a.ti_motivazione_variazione,'X') in ('BAN','PRG','ALT','TAE','TAU','INC','RAG')
                        THEN ABS(NVL (b.im_variazione, 0))
                        ELSE 0
                     END trasfmeno_fin,
                     0 stanziamento_cofin,
                     CASE
                        WHEN a.tipologia_fin='FIN' AND NVL(b.im_variazione, 0) > 0
                        THEN NVL(b.im_variazione, 0)
                        ELSE 0
                     END variapiu_cofin,
                     CASE
                        WHEN a.tipologia_fin='FIN' AND NVL(b.im_variazione, 0) < 0
                        THEN ABS(NVL (b.im_variazione, 0))
                        ELSE 0
                     END variameno_cofin,
                     CASE
                        WHEN a.tipologia_fin='FIN' AND NVL(b.im_variazione, 0) > 0 AND
                             NVL(a.ti_motivazione_variazione,'X') in ('BAN','PRG','ALT','TAE','TAU','INC','RAG')
                        THEN NVL(b.im_variazione, 0)
                        ELSE 0
                     END trasfpiu_fin,
                     CASE
                        WHEN a.tipologia_fin='FIN' AND NVL(b.im_variazione, 0) < 0  AND
                             NVL(a.ti_motivazione_variazione,'X') in ('BAN','PRG','ALT','TAE','TAU','INC','RAG')
                        THEN ABS(NVL (b.im_variazione, 0))
                        ELSE 0
                     END trasfmeno_cofin,
                     0 impacc_fin, 0 impacc_cofin, 0 manris_fin, 0 manris_cofin
                FROM var_stanz_res a,
                     var_stanz_res_riga b,
                     v_linea_attivita_valida c
               WHERE a.esercizio = b.esercizio
                 AND a.pg_variazione = b.pg_variazione
                 AND a.stato IN ('APP', 'APF', 'PRD')
                 AND b.ti_gestione = 'S'
                 AND b.esercizio = c.esercizio
                 AND b.cd_cdr = c.cd_centro_responsabilita
                 AND b.cd_linea_attivita = c.cd_linea_attivita
                 AND c.cd_natura not in (select val01 from configurazione_cnr e
                                         where e.esercizio = 0
                                         and   e.cd_unita_funzionale = '*'
                                         and   e.cd_chiave_primaria = 'PROGETTI'
                                         and   e.cd_chiave_secondaria = 'NATURA_REIMPIEGO')
              UNION ALL
              SELECT c.pg_progetto, b.esercizio_res,
                     b.cd_cdr, b.cd_linea_attivita,
                     b.esercizio_voce, b.ti_appartenenza, b.ti_gestione, b.cd_elemento_voce,
                     0 stanziamento_fin,
                     0 variapiu_fin,
                     0 variameno_fin,
                     0 trasfpiu_fin,
                     0 trasfmeno_fin,
                     0 stanziamento_cofin,
                     CASE
                        WHEN NVL(b.im_variazione, 0) > 0
                        THEN NVL(b.im_variazione, 0)
                        ELSE 0
                     END variapiu_cofin,
                     CASE
                        WHEN NVL(b.im_variazione, 0) < 0
                        THEN ABS(NVL (b.im_variazione, 0))
                        ELSE 0
                     END variameno_cofin,
                     CASE
                        WHEN NVL(b.im_variazione, 0) > 0 AND
                             NVL(a.ti_motivazione_variazione,'X') in ('BAN','PRG','ALT','TAE','TAU','INC','RAG')
                        THEN NVL(b.im_variazione, 0)
                        ELSE 0
                     END trasfpiu_cofin,
                     CASE
                        WHEN NVL(b.im_variazione, 0) < 0 AND
                             NVL(a.ti_motivazione_variazione,'X') in ('BAN','PRG','ALT','TAE','TAU','INC','RAG')
                        THEN ABS(NVL (b.im_variazione, 0))
                        ELSE 0
                     END trasfmeno_cofin,
                     0 impacc_fin, 0 impacc_cofin, 0 manris_fin, 0 manris_cofin
                FROM var_stanz_res a,
                     var_stanz_res_riga b,
                     v_linea_attivita_valida c
               WHERE a.esercizio = b.esercizio
                 AND a.pg_variazione = b.pg_variazione
                 AND a.stato IN ('APP', 'APF', 'PRD')
                 AND b.ti_gestione = 'S'
                 AND b.esercizio = c.esercizio
                 AND b.cd_cdr = c.cd_centro_responsabilita
                 AND b.cd_linea_attivita = c.cd_linea_attivita
                 AND c.cd_natura in (select val01 from configurazione_cnr e
                                     where e.esercizio = 0
                                     and   e.cd_unita_funzionale = '*'
                                     and   e.cd_chiave_primaria = 'PROGETTI'
                                     and   e.cd_chiave_secondaria = 'NATURA_REIMPIEGO')
              UNION ALL
              SELECT b.pg_progetto, a.esercizio_res,
                     a.cd_centro_responsabilita, a.cd_linea_attivita,
                     a.esercizio_res, a.ti_appartenenza, a.ti_gestione, a.cd_elemento_voce,
                     0 stanziamento_fin,
                     0 variapiu_fin, 0 variameno_fin,
                     0 trasfpiu_fin, 0 trasfmeno_fin,
                     0 stanziamento_cofin,
                     0 variapiu_cofin, 0 variameno_cofin,
                     0 trasfpiu_cofin, 0 trasfmeno_cofin,
                     CASE
                        WHEN c.tipo ='FES'
                        THEN CASE
                                WHEN a.ESERCIZIO = a.ESERCIZIO_RES
                                THEN NVL(a.IM_OBBL_ACC_COMP, 0)
                                ELSE NVL(a.IM_OBBL_RES_IMP, 0) +
                                     NVL(a.VAR_PIU_OBBL_RES_IMP, 0) - NVL(a.VAR_MENO_OBBL_RES_IMP, 0) +
                                     NVL(a.VAR_PIU_OBBL_RES_PRO, 0) - NVL(a.VAR_MENO_OBBL_RES_PRO, 0)
                             END
                        ELSE 0
                     END impacc_fin,
                     CASE
                        WHEN c.tipo ='FIN'
                        THEN CASE
                                WHEN a.ESERCIZIO = a.ESERCIZIO_RES
                                THEN NVL(a.IM_OBBL_ACC_COMP, 0)
                                ELSE NVL(a.IM_OBBL_RES_IMP, 0) +
                                     NVL(a.VAR_PIU_OBBL_RES_IMP, 0) - NVL(a.VAR_MENO_OBBL_RES_IMP, 0) +
                                     NVL(a.VAR_PIU_OBBL_RES_PRO, 0) - NVL(a.VAR_MENO_OBBL_RES_PRO, 0)
                             END
                        ELSE 0
                     END impacc_cofin,
                     CASE
                        WHEN c.tipo ='FES'
                        THEN NVL(a.IM_MANDATI_REVERSALI_PRO, 0) + NVL(a.IM_MANDATI_REVERSALI_IMP, 0)
                        ELSE 0
                     END manris_fin,
                     CASE
                        WHEN c.tipo ='FIN'
                        THEN NVL(a.IM_MANDATI_REVERSALI_PRO, 0) + NVL(a.IM_MANDATI_REVERSALI_IMP, 0)
                        ELSE 0
                     END manris_cofin
                FROM voce_f_saldi_cdr_linea a,
                     v_linea_attivita_valida b,
                     natura c
               WHERE a.esercizio = b.esercizio
                 AND a.cd_centro_responsabilita = b.cd_centro_responsabilita
                 AND a.cd_linea_attivita = b.cd_linea_attivita
                 AND a.ti_gestione = 'S'
                 AND c.cd_natura = b.cd_natura
                 AND c.cd_natura not in (select val01 from configurazione_cnr e
                                         where e.esercizio = 0
                                         and   e.cd_unita_funzionale = '*'
                                         and   e.cd_chiave_primaria = 'PROGETTI'
                                         and   e.cd_chiave_secondaria = 'NATURA_REIMPIEGO')
                 AND (NVL(a.IM_OBBL_ACC_COMP, 0)!=0 OR
                      NVL(a.IM_OBBL_RES_IMP, 0)!=0 OR
                      NVL(a.VAR_PIU_OBBL_RES_IMP, 0)!=0 OR
                      NVL(a.VAR_MENO_OBBL_RES_IMP, 0)!=0 OR
                      NVL(a.VAR_PIU_OBBL_RES_PRO, 0)!=0 OR
                      NVL(a.VAR_MENO_OBBL_RES_PRO, 0)!=0 OR
                      NVL(a.IM_MANDATI_REVERSALI_PRO, 0)!=0 OR
                      NVL(a.IM_MANDATI_REVERSALI_IMP, 0)!=0)
              UNION ALL
              SELECT b.pg_progetto, a.esercizio_res,
                     a.cd_centro_responsabilita, a.cd_linea_attivita,
                     a.esercizio_res, a.ti_appartenenza, a.ti_gestione, a.cd_elemento_voce,
                     0 stanziamento_fin,
                     0 variapiu_fin, 0 variameno_fin,
                     0 trasfpiu_fin, 0 trasfmeno_fin,
                     0 stanziamento_cofin,
                     0 variapiu_cofin, 0 variameno_cofin,
                     0 trasfpiu_cofin, 0 trasfmeno_cofin,
                     0 impacc_fin,
                     CASE
                        WHEN a.ESERCIZIO = a.ESERCIZIO_RES
                        THEN NVL(a.IM_OBBL_ACC_COMP, 0)
                        ELSE NVL(a.IM_OBBL_RES_IMP, 0) +
                             NVL(a.VAR_PIU_OBBL_RES_IMP, 0) - NVL(a.VAR_MENO_OBBL_RES_IMP, 0) +
                             NVL(a.VAR_PIU_OBBL_RES_PRO, 0) - NVL(a.VAR_MENO_OBBL_RES_PRO, 0)
                     END impacc_cofin,
                     0 manris_fin,
                     NVL(a.IM_MANDATI_REVERSALI_PRO, 0) + NVL(a.IM_MANDATI_REVERSALI_IMP, 0) manris_cofin
                FROM voce_f_saldi_cdr_linea a,
                     v_linea_attivita_valida b,
                     natura c
               WHERE a.esercizio = b.esercizio
                 AND a.cd_centro_responsabilita = b.cd_centro_responsabilita
                 AND a.cd_linea_attivita = b.cd_linea_attivita
                 AND a.ti_gestione = 'S'
                 AND c.cd_natura = b.cd_natura
                 AND c.cd_natura in (select val01 from configurazione_cnr e
                                     where e.esercizio = 0
                                     and   e.cd_unita_funzionale = '*'
                                     and   e.cd_chiave_primaria = 'PROGETTI'
                                     and   e.cd_chiave_secondaria = 'NATURA_REIMPIEGO')
                 AND (NVL(a.IM_OBBL_ACC_COMP, 0)!=0 OR
                      NVL(a.IM_OBBL_RES_IMP, 0)!=0 OR
                      NVL(a.VAR_PIU_OBBL_RES_IMP, 0)!=0 OR
                      NVL(a.VAR_MENO_OBBL_RES_IMP, 0)!=0 OR
                      NVL(a.VAR_PIU_OBBL_RES_PRO, 0)!=0 OR
                      NVL(a.VAR_MENO_OBBL_RES_PRO, 0)!=0 OR
                      NVL(a.IM_MANDATI_REVERSALI_PRO, 0)!=0 OR
                      NVL(a.IM_MANDATI_REVERSALI_IMP, 0)!=0)) x
    GROUP BY x.pg_progetto,
             x.esercizio,
             x.cd_centro_responsabilita,
             x.cd_linea_attivita,
             x.esercizio,
             x.ti_appartenenza,
             x.ti_gestione,
             x.cd_elemento_voce);