--------------------------------------------------------
--  DDL for View V_ASSESTATO
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_ASSESTATO" ("ESERCIZIO", "ESERCIZIO_RES", "CD_CENTRO_RESPONSABILITA", "CD_LINEA_ATTIVITA", "DS_LINEA_ATTIVITA", "CD_NATURA", "TI_APPARTENENZA", "TI_GESTIONE", "CD_ELEMENTO_VOCE", "CD_VOCE", "CD_MODULO", "STANZIAMENTO_INIZIALE", "VARIAZIONI_POSITIVE", "VARIAZIONI_NEGATIVE", "VARIAZIONI_RESIDUI_PROPRI", "ASSESTATO_INIZIALE", "IMPORTO_UTILIZZATO", "IMPORTO_VINCOLI", "IMPORTO_DISPONIBILE", "VARIAZIONI_PROVVISORIE", "VARIAZIONI_DEFINITIVE", "ASSESTATO_FINALE", "IMPORTO_INI_RESIDUI_PROPRI", "IMPORTO_MANREV", "ASSESTATO_CASSA", "PROGETTO_DT_INIZIO", "PROGETTO_DT_FINE", "PROGETTO_DT_PROROGA") AS 
  SELECT
--
-- Date: 09/11/2006
-- Version: 1.1
--
-- Vista CONSULTAZIONE Assestato
--
-- History:
--
-- Date: 01/01/2006
-- Version: 1.0
-- Creazione
--
-- Date: 09/11/2006
-- Version: 1.1
-- Aggiunta la selezione del progetto/commessa/modulo per anno
--
-- Body:
--
          x.esercizio, x.esercizio_res, x.cd_centro_responsabilita,
          x.cd_linea_attivita, v_linea_attivita_valida.ds_linea_attivita,
          v_linea_attivita_valida.cd_natura, x.ti_appartenenza, x.ti_gestione,
          x.cd_elemento_voce,
          DECODE
               (x.cd_voce,
                NULL, cnrctb053.getvoce_fdaev (x.esercizio,
                                               x.ti_appartenenza,
                                               x.ti_gestione,
                                               x.cd_elemento_voce,
                                               x.cd_centro_responsabilita,
                                               x.cd_linea_attivita
                                              ),
                x.cd_voce
               ) cd_voce,
          DECODE (progetto.cd_progetto,
                  NULL, 'Modulo non definito',
                  progetto.cd_progetto
                 ) cd_modulo,
          x.stanziamento_iniziale, x.variazioni_positive,
          x.variazioni_negative, x.variazioni_residui_propri,
          x.assestato_iniziale, x.importo_utilizzato, x.importo_vincoli,
            x.assestato_iniziale
          - x.importo_utilizzato
          - x.importo_vincoli importo_disponibile,
          x.variazioni_provvisorie, x.variazioni_definitive,
            x.assestato_iniziale
          - x.importo_utilizzato
          + x.variazioni_provvisorie
          + x.variazioni_definitive assestato_finale,
          x.importo_ini_residui_propri, x.importo_manrev, x.assestato_cassa,
          otherField.dt_inizio progetto_dt_inizio,  
          otherField.dt_fine progetto_dt_fine, otherField.dt_proroga progetto_dt_proroga
     FROM (SELECT   a.esercizio, a.esercizio_res, a.cd_centro_responsabilita,
                    a.cd_linea_attivita, a.ti_appartenenza, a.ti_gestione,
                    a.cd_elemento_voce, a.cd_voce,
                    NVL (SUM (a.stanziamento_iniziale),
                         0
                        ) stanziamento_iniziale,
                    NVL (SUM (a.variazioni_positive), 0) variazioni_positive,
                    NVL (SUM (a.variazioni_negative), 0) variazioni_negative,
                    NVL
                       (SUM (a.variazioni_residui_propri),
                        0
                       ) variazioni_residui_propri,
                    NVL (SUM (a.assestato_iniziale), 0) assestato_iniziale,
                    NVL (SUM (a.importo_utilizzato), 0) importo_utilizzato,
                    NVL
                       (SUM (a.variazioni_provvisorie),
                        0
                       ) variazioni_provvisorie,
                    NVL (SUM (a.variazioni_definitive),
                         0
                        ) variazioni_definitive,
                    NVL
                       (SUM (a.importo_ini_residui_propri),
                        0
                       ) importo_ini_residui_propri,
                    NVL (SUM (a.importo_manrev), 0) importo_manrev,
                    NVL (SUM (a.assestato_cassa), 0) assestato_cassa,
                    cnrutl002.im_vincoli
                                 (a.esercizio,
                                  a.esercizio_res,
                                  a.cd_centro_responsabilita,
                                  a.cd_linea_attivita,
                                  a.ti_appartenenza,
                                  a.ti_gestione,
                                  a.cd_elemento_voce
                                 ) importo_vincoli
               FROM (SELECT voce_f_saldi_cdr_linea.esercizio,
                            voce_f_saldi_cdr_linea.esercizio_res,
                            voce_f_saldi_cdr_linea.cd_centro_responsabilita,
                            voce_f_saldi_cdr_linea.cd_linea_attivita,
                            voce_f_saldi_cdr_linea.ti_appartenenza,
                            voce_f_saldi_cdr_linea.ti_gestione,
                            voce_f_saldi_cdr_linea.cd_elemento_voce,
                            voce_f_saldi_cdr_linea.cd_voce,
                            NVL (im_stanz_iniziale_a1,
                                 0
                                ) stanziamento_iniziale,
                            NVL (variazioni_piu, 0) variazioni_positive,
                            NVL (variazioni_meno, 0) variazioni_negative,
                            0 variazioni_residui_propri,
                              NVL (im_stanz_iniziale_a1,
                                   0)
                            + NVL (variazioni_piu, 0)
                            - NVL (variazioni_meno, 0) assestato_iniziale,
                            NVL (im_obbl_acc_comp, 0) importo_utilizzato,
                            0 variazioni_provvisorie, 0 variazioni_definitive,
                            0 importo_ini_residui_propri,
                            NVL (im_mandati_reversali_pro, 0) importo_manrev,
                              NVL (im_stanz_iniziale_cassa,
                                   0)
                            + NVL (variazioni_piu_cassa, 0)
                            - NVL (variazioni_meno_cassa, 0) assestato_cassa
                       FROM voce_f_saldi_cdr_linea
                      WHERE voce_f_saldi_cdr_linea.esercizio =
                                          voce_f_saldi_cdr_linea.esercizio_res
                     UNION ALL
                     SELECT   a.esercizio, a.esercizio, a.cd_cdr_assegnatario,
                              a.cd_linea_attivita, a.ti_appartenenza,
                              a.ti_gestione, a.cd_elemento_voce, a.cd_voce,
                              0 stanziamento_iniziale, 0 variazioni_positive,
                              0 variazioni_negative,
                              0 variazioni_residui_propri,
                              0 assestato_iniziale, 0 importo_utilizzato,
                              SUM
                                 (a.variazioni_provvisorie
                                 ) variazioni_provvisorie,
                              SUM
                                 (a.variazioni_definitive
                                 ) variazioni_definitive,
                              0 importo_ini_residui_propri, 0 importo_manrev,
                              0 assestato_cassa
                         FROM (SELECT pdg_variazione_riga_gest.esercizio,
                                      pdg_variazione_riga_gest.cd_cdr_assegnatario,
                                      pdg_variazione_riga_gest.cd_linea_attivita,
                                      pdg_variazione_riga_gest.ti_appartenenza,
                                      pdg_variazione_riga_gest.ti_gestione,
                                      pdg_variazione_riga_gest.cd_elemento_voce,
                                      cnrctb053.getvoce_fdaev
                                         (pdg_variazione_riga_gest.esercizio,
                                          pdg_variazione_riga_gest.ti_appartenenza,
                                          pdg_variazione_riga_gest.ti_gestione,
                                          pdg_variazione_riga_gest.cd_elemento_voce,
                                          pdg_variazione_riga_gest.cd_cdr_assegnatario,
                                          pdg_variazione_riga_gest.cd_linea_attivita
                                         ) cd_voce,
                                      DECODE
                                         (pdg_variazione.stato,
                                          'PRP', NVL
                                                (pdg_variazione_riga_gest.im_spese_gest_accentrata_int,
                                                 0
                                                )
                                           + NVL
                                                (pdg_variazione_riga_gest.im_spese_gest_accentrata_est,
                                                 0
                                                )
                                           + NVL
                                                (pdg_variazione_riga_gest.im_spese_gest_decentrata_int,
                                                 0
                                                )
                                           + NVL
                                                (pdg_variazione_riga_gest.im_spese_gest_decentrata_est,
                                                 0
                                                )
                                           + NVL
                                                (pdg_variazione_riga_gest.im_entrata,
                                                 0
                                                ),
                                          0
                                         ) variazioni_provvisorie,
                                      DECODE
                                         (pdg_variazione.stato,
                                          'PRD', NVL
                                                (pdg_variazione_riga_gest.im_spese_gest_accentrata_int,
                                                 0
                                                )
                                           + NVL
                                                (pdg_variazione_riga_gest.im_spese_gest_accentrata_est,
                                                 0
                                                )
                                           + NVL
                                                (pdg_variazione_riga_gest.im_spese_gest_decentrata_int,
                                                 0
                                                )
                                           + NVL
                                                (pdg_variazione_riga_gest.im_spese_gest_decentrata_est,
                                                 0
                                                )
                                           + NVL
                                                (pdg_variazione_riga_gest.im_entrata,
                                                 0
                                                ),
                                          0
                                         ) variazioni_definitive
                                 FROM pdg_variazione_riga_gest,
                                      pdg_variazione
                                WHERE pdg_variazione_riga_gest.esercizio =
                                                      pdg_variazione.esercizio
                                  AND pdg_variazione_riga_gest.pg_variazione_pdg =
                                              pdg_variazione.pg_variazione_pdg
                                  AND pdg_variazione_riga_gest.categoria_dettaglio IN
                                                               ('DIR', 'STI')) a
                     GROUP BY a.esercizio,
                              a.esercizio,
                              a.cd_cdr_assegnatario,
                              a.cd_linea_attivita,
                              a.ti_appartenenza,
                              a.ti_gestione,
                              a.cd_elemento_voce,
                              a.cd_voce
                     UNION ALL
                     SELECT voce_f_saldi_cdr_linea.esercizio,
                            voce_f_saldi_cdr_linea.esercizio_res,
                            voce_f_saldi_cdr_linea.cd_centro_responsabilita,
                            voce_f_saldi_cdr_linea.cd_linea_attivita,
                            voce_f_saldi_cdr_linea.ti_appartenenza,
                            voce_f_saldi_cdr_linea.ti_gestione,
                            voce_f_saldi_cdr_linea.cd_elemento_voce,
                            voce_f_saldi_cdr_linea.cd_voce,
                            NVL
                               (im_stanz_res_improprio,
                                0
                               ) stanziamento_iniziale,
                            NVL (var_piu_stanz_res_imp,
                                 0) variazioni_positive,
                            NVL (var_meno_stanz_res_imp,
                                 0
                                ) variazioni_negative,
                              NVL
                                 (var_piu_obbl_res_pro,
                                  0
                                 )
                            - NVL (var_meno_obbl_res_pro, 0)
                                                    variazioni_residui_propri,
                              NVL (im_stanz_res_improprio,
                                   0
                                  )
                            + NVL (var_piu_stanz_res_imp, 0)
                            - NVL (var_meno_stanz_res_imp, 0)
                            - NVL (var_piu_obbl_res_pro, 0)
                            + NVL (var_meno_obbl_res_pro, 0)
                                                           assestato_iniziale,
                              NVL (im_obbl_res_imp, 0)
                            + NVL (var_piu_obbl_res_imp, 0)
                            - NVL (var_meno_obbl_res_imp, 0) tot_impegnato,
                            (SELECT NVL
                                       (SUM (var_stanz_res_riga.im_variazione),
                                        0
                                       )
                               FROM var_stanz_res_riga, var_stanz_res
                              WHERE var_stanz_res_riga.esercizio =
                                              voce_f_saldi_cdr_linea.esercizio
                                AND var_stanz_res_riga.esercizio_res =
                                          voce_f_saldi_cdr_linea.esercizio_res
                                AND var_stanz_res_riga.cd_cdr =
                                       voce_f_saldi_cdr_linea.cd_centro_responsabilita
                                AND var_stanz_res_riga.cd_linea_attivita =
                                       voce_f_saldi_cdr_linea.cd_linea_attivita
                                AND var_stanz_res_riga.ti_appartenenza =
                                        voce_f_saldi_cdr_linea.ti_appartenenza
                                AND var_stanz_res_riga.ti_gestione =
                                            voce_f_saldi_cdr_linea.ti_gestione
                                AND var_stanz_res_riga.cd_elemento_voce =
                                       voce_f_saldi_cdr_linea.cd_elemento_voce
                                AND var_stanz_res_riga.cd_voce =
                                                voce_f_saldi_cdr_linea.cd_voce
                                AND var_stanz_res_riga.esercizio =
                                                       var_stanz_res.esercizio
                                AND var_stanz_res_riga.pg_variazione =
                                                   var_stanz_res.pg_variazione
                                AND var_stanz_res.stato = 'PRP')
                                                       variazioni_provvisorie,
                            (SELECT NVL
                                       (SUM (var_stanz_res_riga.im_variazione),
                                        0
                                       )
                               FROM var_stanz_res_riga, var_stanz_res
                              WHERE var_stanz_res_riga.esercizio =
                                              voce_f_saldi_cdr_linea.esercizio
                                AND var_stanz_res_riga.esercizio_res =
                                          voce_f_saldi_cdr_linea.esercizio_res
                                AND var_stanz_res_riga.cd_cdr =
                                       voce_f_saldi_cdr_linea.cd_centro_responsabilita
                                AND var_stanz_res_riga.cd_linea_attivita =
                                       voce_f_saldi_cdr_linea.cd_linea_attivita
                                AND var_stanz_res_riga.ti_appartenenza =
                                        voce_f_saldi_cdr_linea.ti_appartenenza
                                AND var_stanz_res_riga.ti_gestione =
                                            voce_f_saldi_cdr_linea.ti_gestione
                                AND var_stanz_res_riga.cd_elemento_voce =
                                       voce_f_saldi_cdr_linea.cd_elemento_voce
                                AND var_stanz_res_riga.cd_voce =
                                                voce_f_saldi_cdr_linea.cd_voce
                                AND var_stanz_res_riga.esercizio =
                                                       var_stanz_res.esercizio
                                AND var_stanz_res_riga.pg_variazione =
                                                   var_stanz_res.pg_variazione
                                AND var_stanz_res.stato = 'PRD')
                                                        variazioni_definitive,
                            NVL (im_obbl_res_pro,
                                 0
                                ) importo_ini_residui_propri,
                              NVL (im_mandati_reversali_pro,
                                   0)
                            + NVL (im_mandati_reversali_imp, 0)
                                                               importo_manrev,
                              NVL (im_stanz_iniziale_cassa,
                                   0)
                            + NVL (variazioni_piu_cassa, 0)
                            - NVL (variazioni_meno_cassa, 0) assestato_cassa
                       FROM voce_f_saldi_cdr_linea
                      WHERE voce_f_saldi_cdr_linea.esercizio >
                                          voce_f_saldi_cdr_linea.esercizio_res) a
           GROUP BY a.esercizio,
                    a.esercizio_res,
                    a.cd_centro_responsabilita,
                    a.cd_linea_attivita,
                    a.ti_appartenenza,
                    a.ti_gestione,
                    a.cd_elemento_voce,
                    a.cd_voce) x,
          v_linea_attivita_valida,
          progetto_gest progetto,
          progetto_other_field otherField
    WHERE x.esercizio = v_linea_attivita_valida.esercizio
      AND x.cd_centro_responsabilita =
                              v_linea_attivita_valida.cd_centro_responsabilita
      AND x.cd_linea_attivita = v_linea_attivita_valida.cd_linea_attivita
      AND v_linea_attivita_valida.esercizio = progetto.esercizio(+)
      AND v_linea_attivita_valida.pg_progetto = progetto.pg_progetto(+)
      AND progetto.pg_progetto = otherField.pg_progetto(+);
