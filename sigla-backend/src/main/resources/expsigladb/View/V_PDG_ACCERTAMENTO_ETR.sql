--------------------------------------------------------
--  DDL for View V_PDG_ACCERTAMENTO_ETR
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_PDG_ACCERTAMENTO_ETR" ("ESERCIZIO", "ESERCIZIO_RES", "CD_CENTRO_RESPONSABILITA", "TI_APPARTENENZA", "TI_GESTIONE", "CD_ELEMENTO_VOCE", "CD_LINEA_ATTIVITA", "CD_PROGETTO", "CD_PROGETTO_PADRE", "CD_FUNZIONE", "CD_NATURA", "DS_LINEA_ATTIVITA", "IM_RA_RCE", "IM_RB_RSE", "IM_RC_ESR", "IM_RD_A2_RICAVI", "IM_RE_A2_ENTRATE", "IM_RF_A3_RICAVI", "IM_RG_A3_ENTRATE", "CD_CENTRO_RESPONSABILITA_CLGS", "CD_LINEA_ATTIVITA_CLGS", "TI_APPARTENENZA_CLGS", "TI_GESTIONE_CLGS", "CD_ELEMENTO_VOCE_CLGS", "STATO", "CATEGORIA_DETTAGLIO") AS 
  SELECT
--
-- Date: 09/11/2006
-- Version: 1.3
--
-- Estrae i dettagli del piano di gestione parte entrate aggregati
-- per linea attivita e elemento voce, considerando solo le linee di attivita
-- con stato APPROVATO
-- La vista non verifica la validita dell'STO
--
-- Date: 10/09/2001
-- Version: 1.0
-- Creazione
--
-- Date: 12/01/2006
-- Version: 1.1
-- Gestione Residui: Aggiunto il recupero dei dati da PDG_MODULO_ENTRATE_GEST se l'esercizio ? superiore al 2005
--
-- Date: 15/05/2006
-- Version: 1.2
-- Aggiunta delle tabelle PDG_VARIAZIONE_RIGA_GEST
--
-- Date: 09/11/2006
-- Version: 1.3
-- Aggiunta la selezione del progetto/commessa/modulo per anno
--
-- Date: 11/04/2017
-- Version: 1.4
-- Gestione Linea attivitא di tipo Entrata/Spesa (ti_gestione='X')
--
-- Body:
--
            pdg_preventivo_etr_det.esercizio,
            pdg_preventivo_etr_det.esercizio,
            pdg_preventivo_etr_det.cd_centro_responsabilita,
            pdg_preventivo_etr_det.ti_appartenenza,
            pdg_preventivo_etr_det.ti_gestione,
            pdg_preventivo_etr_det.cd_elemento_voce,
            pdg_preventivo_etr_det.cd_linea_attivita,
            MAX ((SELECT cd_progetto
                    FROM progetto
                   WHERE pdg_preventivo_etr_det.esercizio = progetto.esercizio
                     AND linea_attivita.pg_progetto = progetto.pg_progetto
                     AND progetto.tipo_fase='X')
                ) cd_progetto,
            MAX
               ((SELECT cd_progetto
                   FROM progetto progetto_padre
                  WHERE progetto_padre.esercizio =
                                              pdg_preventivo_etr_det.esercizio
                    AND progetto_padre.pg_progetto =
                           (SELECT pg_progetto
                              FROM progetto_prev progetto
                             WHERE pdg_preventivo_etr_det.esercizio =
                                                            progetto.esercizio
                               AND linea_attivita.pg_progetto =
                                                          progetto.pg_progetto
                               AND progetto.tipo_fase='X')
                    AND progetto_padre.tipo_fase='X')
               ) cd_progetto_padre,
            linea_attivita.cd_funzione, linea_attivita.cd_natura,
            linea_attivita.ds_linea_attivita, SUM (im_ra_rce),
            SUM (im_rb_rse), SUM (im_rc_esr), SUM (im_rd_a2_ricavi),
            SUM (im_re_a2_entrate), SUM (im_rf_a3_ricavi),
            SUM (im_rg_a3_entrate),
            pdg_preventivo_etr_det.cd_centro_responsabilita_clgs,
            pdg_preventivo_etr_det.cd_linea_attivita_clgs,
            pdg_preventivo_etr_det.ti_appartenenza_clgs,
            pdg_preventivo_etr_det.ti_gestione_clgs,
            pdg_preventivo_etr_det.cd_elemento_voce_clgs,
            pdg_preventivo_etr_det.stato,
            pdg_preventivo_etr_det.categoria_dettaglio
       FROM pdg_preventivo_etr_det,
            linea_attivita,
            cdr,
            unita_organizzativa,
            parametri_cds
      WHERE pdg_preventivo_etr_det.stato = 'Y'
        AND linea_attivita.cd_centro_responsabilita =
                               pdg_preventivo_etr_det.cd_centro_responsabilita
        AND linea_attivita.cd_linea_attivita =
                                      pdg_preventivo_etr_det.cd_linea_attivita
        AND linea_attivita.ti_gestione IN ('E', 'X')
        AND linea_attivita.cd_centro_responsabilita =
                                                  cdr.cd_centro_responsabilita
        AND cdr.cd_unita_organizzativa =
                                    unita_organizzativa.cd_unita_organizzativa
        AND parametri_cds.esercizio = pdg_preventivo_etr_det.esercizio
        AND parametri_cds.cd_cds = unita_organizzativa.cd_unita_padre
        AND (   (    parametri_cds.fl_commessa_obbligatoria = 'Y'
                 AND linea_attivita.pg_progetto IS NOT NULL
                )
             OR (parametri_cds.fl_commessa_obbligatoria = 'N')
            )
   GROUP BY pdg_preventivo_etr_det.esercizio,
            pdg_preventivo_etr_det.cd_centro_responsabilita,
            pdg_preventivo_etr_det.cd_linea_attivita,
            pdg_preventivo_etr_det.ti_appartenenza,
            pdg_preventivo_etr_det.ti_gestione,
            pdg_preventivo_etr_det.cd_elemento_voce,
            pdg_preventivo_etr_det.cd_centro_responsabilita_clgs,
            pdg_preventivo_etr_det.cd_linea_attivita_clgs,
            pdg_preventivo_etr_det.stato,
            pdg_preventivo_etr_det.categoria_dettaglio,
            pdg_preventivo_etr_det.ti_appartenenza_clgs,
            pdg_preventivo_etr_det.ti_gestione_clgs,
            pdg_preventivo_etr_det.cd_elemento_voce_clgs,
            linea_attivita.cd_funzione,
            linea_attivita.cd_natura,
            linea_attivita.ds_linea_attivita
   UNION ALL
   SELECT   a.esercizio, a.esercizio, a.cd_cdr_assegnatario,
            a.ti_appartenenza, a.ti_gestione, a.cd_elemento_voce,
            a.cd_linea_attivita, a.cd_progetto, a.cd_progetto_padre,
            a.cd_funzione, a.cd_natura, a.ds_linea_attivita,
            NVL (SUM (a.importo_entrata), 0), 0, 0, 0, 0, 0, 0,
            a.cd_cdr_assegnatario, a.cd_linea_attivita, a.ti_appartenenza,
            a.ti_gestione, a.cd_elemento_voce, 'Y', a.categoria_dettaglio
       FROM (SELECT   pdg_modulo_entrate_gest.esercizio,
                      pdg_modulo_entrate_gest.cd_cdr_assegnatario,
                      pdg_modulo_entrate_gest.ti_appartenenza,
                      pdg_modulo_entrate_gest.ti_gestione,
                      pdg_modulo_entrate_gest.cd_elemento_voce,
                      pdg_modulo_entrate_gest.cd_linea_attivita,
                      MAX
                         ((SELECT cd_progetto
                             FROM progetto_gest progetto
                            WHERE pdg_modulo_entrate_gest.esercizio =
                                                            progetto.esercizio
                              AND linea_attivita.pg_progetto =
                                                          progetto.pg_progetto)
                         ) cd_progetto,
                      MAX
                         ((SELECT cd_progetto
                             FROM progetto_gest progetto_padre
                            WHERE progetto_padre.esercizio =
                                             pdg_modulo_entrate_gest.esercizio
                              AND progetto_padre.pg_progetto =
                                     (SELECT pg_progetto
                                        FROM progetto_gest progetto
                                       WHERE pdg_modulo_entrate_gest.esercizio =
                                                            progetto.esercizio
                                         AND linea_attivita.pg_progetto =
                                                          progetto.pg_progetto))
                         ) cd_progetto_padre,
                      linea_attivita.cd_funzione, linea_attivita.cd_natura,
                      linea_attivita.ds_linea_attivita,
                      NVL (SUM (im_entrata), 0) importo_entrata,
                      pdg_modulo_entrate_gest.cd_cdr_assegnatario_clge,
                      pdg_modulo_entrate_gest.cd_linea_attivita_clge,
                      pdg_modulo_entrate_gest.categoria_dettaglio
                 FROM pdg_modulo_entrate_gest, v_linea_attivita_valida linea_attivita
                WHERE linea_attivita.esercizio = pdg_modulo_entrate_gest.esercizio
                  AND linea_attivita.cd_centro_responsabilita =
                                   pdg_modulo_entrate_gest.cd_cdr_assegnatario
                  AND linea_attivita.cd_linea_attivita =
                                     pdg_modulo_entrate_gest.cd_linea_attivita
                  AND linea_attivita.ti_gestione IN ('E', 'X')
             GROUP BY pdg_modulo_entrate_gest.esercizio,
                      pdg_modulo_entrate_gest.cd_cdr_assegnatario,
                      pdg_modulo_entrate_gest.cd_linea_attivita,
                      pdg_modulo_entrate_gest.ti_appartenenza,
                      pdg_modulo_entrate_gest.ti_gestione,
                      pdg_modulo_entrate_gest.cd_elemento_voce,
                      'Y',
                      pdg_modulo_entrate_gest.cd_cdr_assegnatario_clge,
                      pdg_modulo_entrate_gest.cd_linea_attivita_clge,
                      pdg_modulo_entrate_gest.categoria_dettaglio,
                      linea_attivita.cd_funzione,
                      linea_attivita.cd_natura,
                      linea_attivita.ds_linea_attivita
             UNION
             SELECT   pdg_variazione_riga_gest.esercizio,
                      pdg_variazione_riga_gest.cd_cdr_assegnatario,
                      pdg_variazione_riga_gest.ti_appartenenza,
                      pdg_variazione_riga_gest.ti_gestione,
                      pdg_variazione_riga_gest.cd_elemento_voce,
                      pdg_variazione_riga_gest.cd_linea_attivita,
                      MAX
                         ((SELECT cd_progetto
                             FROM progetto_gest progetto
                            WHERE pdg_variazione_riga_gest.esercizio =
                                                            progetto.esercizio
                              AND linea_attivita.pg_progetto =
                                                          progetto.pg_progetto)
                         ) cd_progetto,
                      MAX
                         ((SELECT cd_progetto
                             FROM progetto_gest progetto_padre
                            WHERE progetto_padre.esercizio =
                                            pdg_variazione_riga_gest.esercizio
                              AND progetto_padre.pg_progetto =
                                     (SELECT pg_progetto
                                        FROM progetto_gest progetto
                                       WHERE pdg_variazione_riga_gest.esercizio =
                                                            progetto.esercizio
                                         AND linea_attivita.pg_progetto =
                                                          progetto.pg_progetto))
                         ) cd_progetto_padre,
                      linea_attivita.cd_funzione, linea_attivita.cd_natura,
                      linea_attivita.ds_linea_attivita,
                      NVL (SUM (im_entrata), 0) importo_entrata,
                      pdg_variazione_riga_gest.cd_cdr_assegnatario_clgs,
                      pdg_variazione_riga_gest.cd_linea_attivita_clgs,
                      pdg_variazione_riga_gest.categoria_dettaglio
                 FROM pdg_variazione_riga_gest, v_linea_attivita_valida linea_attivita
                WHERE linea_attivita.esercizio = pdg_variazione_riga_gest.esercizio
                  AND linea_attivita.cd_centro_responsabilita =
                                  pdg_variazione_riga_gest.cd_cdr_assegnatario
                  AND linea_attivita.cd_linea_attivita =
                                    pdg_variazione_riga_gest.cd_linea_attivita
                  AND linea_attivita.ti_gestione IN ('E', 'X')
             GROUP BY pdg_variazione_riga_gest.esercizio,
                      pdg_variazione_riga_gest.cd_cdr_assegnatario,
                      pdg_variazione_riga_gest.cd_linea_attivita,
                      pdg_variazione_riga_gest.ti_appartenenza,
                      pdg_variazione_riga_gest.ti_gestione,
                      pdg_variazione_riga_gest.cd_elemento_voce,
                      'Y',
                      pdg_variazione_riga_gest.cd_cdr_assegnatario_clgs,
                      pdg_variazione_riga_gest.cd_linea_attivita_clgs,
                      pdg_variazione_riga_gest.categoria_dettaglio,
                      linea_attivita.cd_funzione,
                      linea_attivita.cd_natura,
                      linea_attivita.ds_linea_attivita) a
   GROUP BY a.esercizio,
            a.cd_cdr_assegnatario,
            a.cd_linea_attivita,
            a.ti_appartenenza,
            a.ti_gestione,
            a.cd_elemento_voce,
            a.cd_progetto,
            a.cd_progetto_padre,
            'Y',
            a.categoria_dettaglio,
            a.cd_funzione,
            a.cd_natura,
            a.ds_linea_attivita
   UNION
   SELECT   voce_f_saldi_cdr_linea.esercizio,
            voce_f_saldi_cdr_linea.esercizio_res,
            voce_f_saldi_cdr_linea.cd_centro_responsabilita,
            voce_f_saldi_cdr_linea.ti_appartenenza,
            voce_f_saldi_cdr_linea.ti_gestione,
            voce_f_saldi_cdr_linea.cd_elemento_voce,
            voce_f_saldi_cdr_linea.cd_linea_attivita,
            MAX ((SELECT cd_progetto
                    FROM progetto_gest progetto
                   WHERE voce_f_saldi_cdr_linea.esercizio = progetto.esercizio
                     AND linea_attivita.pg_progetto = progetto.pg_progetto)
                ) cd_progetto,
            MAX
               ((SELECT cd_progetto
                   FROM progetto_gest progetto_padre
                  WHERE progetto_padre.esercizio =
                                              voce_f_saldi_cdr_linea.esercizio
                    AND progetto_padre.pg_progetto =
                           (SELECT pg_progetto
                              FROM progetto_gest progetto
                             WHERE voce_f_saldi_cdr_linea.esercizio =
                                                            progetto.esercizio
                               AND linea_attivita.pg_progetto =
                                                          progetto.pg_progetto))
               ) cd_progetto_padre,
            linea_attivita.cd_funzione, linea_attivita.cd_natura,
            linea_attivita.ds_linea_attivita, SUM (im_stanz_res_improprio),
            SUM (0), SUM (0), SUM (0), SUM (0), SUM (0), SUM (0),
            voce_f_saldi_cdr_linea.cd_centro_responsabilita,
            voce_f_saldi_cdr_linea.cd_linea_attivita,
            voce_f_saldi_cdr_linea.ti_appartenenza,
            voce_f_saldi_cdr_linea.ti_gestione,
            voce_f_saldi_cdr_linea.cd_elemento_voce, 'Y', 'DIR'
       FROM voce_f_saldi_cdr_linea, v_linea_attivita_valida linea_attivita
      WHERE voce_f_saldi_cdr_linea.esercizio > voce_f_saldi_cdr_linea.esercizio_res
        AND linea_attivita.esercizio = voce_f_saldi_cdr_linea.esercizio
        AND linea_attivita.cd_centro_responsabilita =
                               voce_f_saldi_cdr_linea.cd_centro_responsabilita
        AND linea_attivita.cd_linea_attivita =
                                      voce_f_saldi_cdr_linea.cd_linea_attivita
        AND linea_attivita.ti_gestione IN ('E', 'X')
   GROUP BY voce_f_saldi_cdr_linea.esercizio,
            voce_f_saldi_cdr_linea.esercizio_res,
            voce_f_saldi_cdr_linea.cd_centro_responsabilita,
            voce_f_saldi_cdr_linea.cd_linea_attivita,
            voce_f_saldi_cdr_linea.ti_appartenenza,
            voce_f_saldi_cdr_linea.ti_gestione,
            voce_f_saldi_cdr_linea.cd_elemento_voce,
            voce_f_saldi_cdr_linea.cd_centro_responsabilita,
            voce_f_saldi_cdr_linea.cd_linea_attivita,
            'Y',
            'DIR',
            linea_attivita.cd_funzione,
            linea_attivita.cd_natura,
            linea_attivita.ds_linea_attivita ;
