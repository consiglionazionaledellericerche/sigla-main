--------------------------------------------------------
--  DDL for View V_PDG_OBBLIGAZIONE_SPE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_PDG_OBBLIGAZIONE_SPE" ("ESERCIZIO", "ESERCIZIO_RES", "CD_CENTRO_RESPONSABILITA", "TI_APPARTENENZA", "TI_GESTIONE", "CD_ELEMENTO_VOCE", "CD_LINEA_ATTIVITA", "CD_PROGETTO", "CD_PROGETTO_PADRE", "CD_FUNZIONE", "CD_NATURA", "DS_LINEA_ATTIVITA", "IM_RI_CCS_SPESE_ODC", "IM_RJ_CCS_SPESE_ODC_ALTRA_UO", "IM_RK_CCS_SPESE_OGC", "IM_RL_CCS_SPESE_OGC_ALTRA_UO", "IM_RQ_SSC_COSTI_ODC", "IM_RR_SSC_COSTI_ODC_ALTRA_UO", "IM_RS_SSC_COSTI_OGC", "IM_RT_SSC_COSTI_OGC_ALTRA_UO", "IM_RU_SPESE_COSTI_ALTRUI", "IM_RAC_A2_SPESE_ODC", "IM_RAD_A2_SPESE_ODC_ALTRA_UO", "IM_RAE_A2_SPESE_OGC", "IM_RAF_A2_SPESE_OGC_ALTRA_UO", "IM_RAG_A2_SPESE_COSTI_ALTRUI", "IM_RAL_A3_SPESE_ODC", "IM_RAM_A3_SPESE_ODC_ALTRA_UO", "IM_RAN_A3_SPESE_OGC", "IM_RAO_A3_SPESE_OGC_ALTRA_UO", "IM_RAP_A3_SPESE_COSTI_ALTRUI", "CD_CENTRO_RESPONSABILITA_CLGS", "CD_LINEA_ATTIVITA_CLGS", "TI_APPARTENENZA_CLGS", "TI_GESTIONE_CLGS", "CD_ELEMENTO_VOCE_CLGS", "STATO", "CATEGORIA_DETTAGLIO") AS 
  SELECT
--
-- Date: 09/11/2006
-- Version: 1.11
--
-- Estrae i dettagli del piano di gestione parte spese aggregati
-- per linea attivita e elemento voce, considerando solo le linee di attivita
-- con stato APPROVATO
-- La vista non verifica la validita dell'STO
--
-- History:
--
-- Date: 10/09/2001
-- Version: 1.0
-- Creazione
--
-- Date: 14/09/2001
-- Version: 1.1
-- Adattamento alle modifiche per gestire la categoria di dettaglio e
-- lo stato
--
-- Date: 08/11/2001
-- Version: 1.2
-- Eliminazione esercizio da STO
--
-- Date: 14/02/2002
-- Version: 1.3
-- Sostituito il filtro delle categorie di dettaglio da CAR a SCR
--
-- Date: 21/02/2002
-- Version: 1.4
-- Aggiunte le colonne relative a ALTRA_UO
--
-- Date: 26/02/2002
-- Version: 1.5
-- Introduzione insieme Linea Attivita e Gestione Linea attivita
--
-- Date: 13/06/2002
-- Version: 1.6
-- Modificato commento
--
-- Date: 27/08/2002
-- Version: 1.7
-- Aggiunta la categoria dettaglio CAR
--
-- Date: 12/01/2006
-- Version: 1.8
-- Gestione Residui: Aggiunto il recupero dei dati da PDG_MODULO_SPESE_GEST se l'esercizio ? superiore al 2005
--
-- Date: 17/01/2006
-- Version: 1.9
-- Ottimizzata la select per motivi prestazionali - Inserite le join con V_STRUTTURA_ORGANIZZATIVA e V_PROGETTO_PADRE
--
-- Date: 15/05/2006
-- Version: 1.10
-- Aggiunta delle tabelle PDG_VARIAZIONE_RIGA_GEST
--
-- Date: 09/11/2006
-- Version: 1.11
-- Aggiunta la selezione del progetto/commessa/modulo per anno
--
-- Date: 11/04/2017
-- Version: 1.12
-- Gestione Linea attivitא di tipo Entrata/Spesa (ti_gestione='X')
--
-- Body:
--
          b.esercizio, b.esercizio_res, b.cd_centro_responsabilita,
          b.ti_appartenenza, b.ti_gestione, b.cd_elemento_voce,
          b.cd_linea_attivita,
          (SELECT cd_progetto
             FROM progetto
            WHERE progetto.esercizio = b.esercizio
              AND progetto.pg_progetto = b.pg_progetto
              AND progetto.tipo_fase='X') cd_progetto,
          (SELECT cd_progetto
             FROM progetto progetto_padre
            WHERE progetto_padre.esercizio = b.esercizio
              AND progetto_padre.pg_progetto =
                     (SELECT pg_progetto
                        FROM progetto
                       WHERE progetto.esercizio = b.esercizio
                         AND progetto.pg_progetto = b.pg_progetto
                         AND progetto.tipo_fase='X')
              AND progetto_padre.tipo_fase='X') cd_progetto_padre,
          b.cd_funzione, b.cd_natura, b.ds_linea_attivita,
          b.im_ri_ccs_spese_odc, b.im_rj_ccs_spese_odc_altra_uo,
          b.im_rk_ccs_spese_ogc, b.im_rl_ccs_spese_ogc_altra_uo,
          b.im_rq_ssc_costi_odc, b.im_rr_ssc_costi_odc_altra_uo,
          b.im_rs_ssc_costi_ogc, b.im_rt_ssc_costi_ogc_altra_uo,
          b.im_ru_spese_costi_altrui, b.im_rac_a2_spese_odc,
          b.im_rad_a2_spese_odc_altra_uo, b.im_rae_a2_spese_ogc,
          b.im_raf_a2_spese_ogc_altra_uo, b.im_rag_a2_spese_costi_altrui,
          b.im_ral_a3_spese_odc, b.im_ram_a3_spese_odc_altra_uo,
          b.im_ran_a3_spese_ogc, b.im_rao_a3_spese_ogc_altra_uo,
          b.im_rap_a3_spese_costi_altrui, b.cd_centro_responsabilita_clgs,
          b.cd_linea_attivita_clgs, b.ti_appartenenza_clgs,
          b.ti_gestione_clgs, b.cd_elemento_voce_clgs, b.stato,
          b.categoria_dettaglio
     FROM (SELECT   pdg_preventivo_spe_det.esercizio,
                    pdg_preventivo_spe_det.esercizio esercizio_res,
                    pdg_preventivo_spe_det.cd_centro_responsabilita,
                    pdg_preventivo_spe_det.ti_appartenenza,
                    pdg_preventivo_spe_det.ti_gestione,
                    pdg_preventivo_spe_det.cd_elemento_voce,
                    pdg_preventivo_spe_det.cd_linea_attivita,
                    linea_attivita.cd_funzione, linea_attivita.cd_natura,
                    linea_attivita.ds_linea_attivita,
                    linea_attivita.pg_progetto,
                    SUM (im_ri_ccs_spese_odc) im_ri_ccs_spese_odc,
                    SUM
                       (im_rj_ccs_spese_odc_altra_uo
                       ) im_rj_ccs_spese_odc_altra_uo,
                    SUM (im_rk_ccs_spese_ogc) im_rk_ccs_spese_ogc,
                    SUM
                       (im_rl_ccs_spese_ogc_altra_uo
                       ) im_rl_ccs_spese_ogc_altra_uo,
                    SUM (im_rq_ssc_costi_odc) im_rq_ssc_costi_odc,
                    SUM
                       (im_rr_ssc_costi_odc_altra_uo
                       ) im_rr_ssc_costi_odc_altra_uo,
                    SUM (im_rs_ssc_costi_ogc) im_rs_ssc_costi_ogc,
                    SUM
                       (im_rt_ssc_costi_ogc_altra_uo
                       ) im_rt_ssc_costi_ogc_altra_uo,
                    SUM (im_ru_spese_costi_altrui) im_ru_spese_costi_altrui,
                    SUM (im_rac_a2_spese_odc) im_rac_a2_spese_odc,
                    SUM
                       (im_rad_a2_spese_odc_altra_uo
                       ) im_rad_a2_spese_odc_altra_uo,
                    SUM (im_rae_a2_spese_ogc) im_rae_a2_spese_ogc,
                    SUM
                       (im_raf_a2_spese_ogc_altra_uo
                       ) im_raf_a2_spese_ogc_altra_uo,
                    SUM
                       (im_rag_a2_spese_costi_altrui
                       ) im_rag_a2_spese_costi_altrui,
                    SUM (im_ral_a3_spese_odc) im_ral_a3_spese_odc,
                    SUM
                       (im_ram_a3_spese_odc_altra_uo
                       ) im_ram_a3_spese_odc_altra_uo,
                    SUM (im_ran_a3_spese_ogc) im_ran_a3_spese_ogc,
                    SUM
                       (im_rao_a3_spese_ogc_altra_uo
                       ) im_rao_a3_spese_ogc_altra_uo,
                    SUM
                       (im_rap_a3_spese_costi_altrui
                       ) im_rap_a3_spese_costi_altrui,
                    pdg_preventivo_spe_det.cd_centro_responsabilita_clgs,
                    pdg_preventivo_spe_det.cd_linea_attivita_clgs,
                    pdg_preventivo_spe_det.ti_appartenenza_clgs,
                    pdg_preventivo_spe_det.ti_gestione_clgs,
                    pdg_preventivo_spe_det.cd_elemento_voce_clgs,
                    pdg_preventivo_spe_det.stato,
                    pdg_preventivo_spe_det.categoria_dettaglio
               FROM pdg_preventivo_spe_det,
                    linea_attivita,
                    v_struttura_organizzativa,
                    parametri_cds
              WHERE pdg_preventivo_spe_det.stato = 'Y'
                AND linea_attivita.cd_centro_responsabilita =
                               pdg_preventivo_spe_det.cd_centro_responsabilita
                AND linea_attivita.cd_linea_attivita =
                                      pdg_preventivo_spe_det.cd_linea_attivita
                AND linea_attivita.ti_gestione IN ('S', 'X')
                AND pdg_preventivo_spe_det.esercizio =
                                           v_struttura_organizzativa.esercizio
                AND pdg_preventivo_spe_det.cd_centro_responsabilita =
                                             v_struttura_organizzativa.cd_root
                AND parametri_cds.esercizio = pdg_preventivo_spe_det.esercizio
                AND parametri_cds.cd_cds = v_struttura_organizzativa.cd_cds
                AND (   (    parametri_cds.fl_commessa_obbligatoria = 'Y'
                         AND linea_attivita.pg_progetto IS NOT NULL
                        )
                     OR (parametri_cds.fl_commessa_obbligatoria = 'N')
                    )
           GROUP BY pdg_preventivo_spe_det.esercizio,
                    pdg_preventivo_spe_det.cd_centro_responsabilita,
                    pdg_preventivo_spe_det.cd_linea_attivita,
                    pdg_preventivo_spe_det.ti_appartenenza,
                    pdg_preventivo_spe_det.ti_gestione,
                    pdg_preventivo_spe_det.cd_elemento_voce,
                    pdg_preventivo_spe_det.cd_centro_responsabilita_clgs,
                    pdg_preventivo_spe_det.cd_linea_attivita_clgs,
                    pdg_preventivo_spe_det.stato,
                    pdg_preventivo_spe_det.categoria_dettaglio,
                    pdg_preventivo_spe_det.ti_appartenenza_clgs,
                    pdg_preventivo_spe_det.ti_gestione_clgs,
                    pdg_preventivo_spe_det.cd_elemento_voce_clgs,
                    linea_attivita.cd_funzione,
                    linea_attivita.cd_natura,
                    linea_attivita.ds_linea_attivita,
                    linea_attivita.pg_progetto
           UNION ALL
           SELECT   a.esercizio, a.esercizio, a.cd_cdr_assegnatario,
                    a.ti_appartenenza, a.ti_gestione, a.cd_elemento_voce,
                    a.cd_linea_attivita, a.cd_funzione, a.cd_natura,
                    a.ds_linea_attivita, a.pg_progetto,
                    NVL (SUM (a.importo_spese_decentrate), 0), 0, 0, 0, 0, 0,
                    0, 0, NVL (SUM (a.importo_spese_accentrate), 0), 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, a.cd_cdr_assegnatario,
                    a.cd_linea_attivita, a.ti_appartenenza, a.ti_gestione,
                    a.cd_elemento_voce, 'Y', a.categoria_dettaglio
               FROM (SELECT pdg_modulo_spese_gest.esercizio,
                            pdg_modulo_spese_gest.cd_cdr_assegnatario,
                            pdg_modulo_spese_gest.ti_appartenenza,
                            pdg_modulo_spese_gest.ti_gestione,
                            pdg_modulo_spese_gest.cd_elemento_voce,
                            pdg_modulo_spese_gest.cd_linea_attivita,
                            linea_attivita.cd_funzione,
                            linea_attivita.cd_natura,
                            linea_attivita.ds_linea_attivita,
                            linea_attivita.pg_progetto,
                              NVL
                                 (im_spese_gest_decentrata_int,
                                  0
                                 )
                            + NVL (im_spese_gest_decentrata_est, 0)
                                                     importo_spese_decentrate,
                              NVL
                                 (im_spese_gest_accentrata_int,
                                  0
                                 )
                            + NVL (im_spese_gest_accentrata_est, 0)
                                                     importo_spese_accentrate,
                            pdg_modulo_spese_gest.cd_cdr_assegnatario_clgs,
                            pdg_modulo_spese_gest.cd_linea_attivita_clgs,
                            pdg_modulo_spese_gest.categoria_dettaglio
                       FROM pdg_modulo_spese_gest, v_linea_attivita_valida linea_attivita
                      WHERE linea_attivita.esercizio = pdg_modulo_spese_gest.esercizio
                        AND linea_attivita.cd_centro_responsabilita =
                                     pdg_modulo_spese_gest.cd_cdr_assegnatario
                        AND linea_attivita.cd_linea_attivita =
                                       pdg_modulo_spese_gest.cd_linea_attivita
                        AND linea_attivita.ti_gestione IN ('S', 'X')
                     UNION ALL
                     SELECT pdg_variazione_riga_gest.esercizio,
                            pdg_variazione_riga_gest.cd_cdr_assegnatario,
                            pdg_variazione_riga_gest.ti_appartenenza,
                            pdg_variazione_riga_gest.ti_gestione,
                            pdg_variazione_riga_gest.cd_elemento_voce,
                            pdg_variazione_riga_gest.cd_linea_attivita,
                            linea_attivita.cd_funzione,
                            linea_attivita.cd_natura,
                            linea_attivita.ds_linea_attivita,
                            linea_attivita.pg_progetto,
                              NVL
                                 (im_spese_gest_decentrata_int,
                                  0
                                 )
                            + NVL (im_spese_gest_decentrata_est, 0)
                                                     importo_spese_decentrate,
                              NVL
                                 (im_spese_gest_accentrata_int,
                                  0
                                 )
                            + NVL (im_spese_gest_accentrata_est, 0)
                                                     importo_spese_accentrate,
                            pdg_variazione_riga_gest.cd_cdr_assegnatario_clgs,
                            pdg_variazione_riga_gest.cd_linea_attivita_clgs,
                            pdg_variazione_riga_gest.categoria_dettaglio
                       FROM pdg_variazione_riga_gest,
                            pdg_variazione,
                            v_linea_attivita_valida linea_attivita
                      WHERE linea_attivita.esercizio = pdg_variazione_riga_gest.esercizio
                        AND linea_attivita.cd_centro_responsabilita =
                                  pdg_variazione_riga_gest.cd_cdr_assegnatario
                        AND linea_attivita.cd_linea_attivita =
                                    pdg_variazione_riga_gest.cd_linea_attivita
                        AND linea_attivita.ti_gestione IN ('S', 'X')
                        AND pdg_variazione_riga_gest.esercizio =
                                                      pdg_variazione.esercizio
                        AND pdg_variazione_riga_gest.pg_variazione_pdg =
                                              pdg_variazione.pg_variazione_pdg
                        AND pdg_variazione.stato IN ('APP', 'APF')) a
           GROUP BY a.esercizio,
                    a.cd_cdr_assegnatario,
                    a.cd_linea_attivita,
                    a.ti_appartenenza,
                    a.ti_gestione,
                    a.cd_elemento_voce,
                    'Y',
                    a.categoria_dettaglio,
                    a.cd_funzione,
                    a.cd_natura,
                    a.ds_linea_attivita,
                    a.pg_progetto
           UNION ALL
           SELECT   voce_f_saldi_cdr_linea.esercizio,
                    voce_f_saldi_cdr_linea.esercizio_res,
                    voce_f_saldi_cdr_linea.cd_centro_responsabilita,
                    voce_f_saldi_cdr_linea.ti_appartenenza,
                    voce_f_saldi_cdr_linea.ti_gestione,
                    voce_f_saldi_cdr_linea.cd_elemento_voce,
                    voce_f_saldi_cdr_linea.cd_linea_attivita,
                    linea_attivita.cd_funzione, linea_attivita.cd_natura,
                    linea_attivita.ds_linea_attivita,
                    linea_attivita.pg_progetto, SUM (im_stanz_res_improprio),
                    SUM (0), SUM (0), SUM (0), SUM (0), SUM (0), SUM (0),
                    SUM (0), SUM (0), SUM (0), SUM (0), SUM (0), SUM (0),
                    SUM (0), SUM (0), SUM (0), SUM (0), SUM (0), SUM (0),
                    voce_f_saldi_cdr_linea.cd_centro_responsabilita,
                    voce_f_saldi_cdr_linea.cd_linea_attivita,
                    voce_f_saldi_cdr_linea.ti_appartenenza,
                    voce_f_saldi_cdr_linea.ti_gestione,
                    voce_f_saldi_cdr_linea.cd_elemento_voce, 'Y', 'DIR'
               FROM voce_f_saldi_cdr_linea, v_linea_attivita_valida linea_attivita
              WHERE voce_f_saldi_cdr_linea.esercizio >
                                          voce_f_saldi_cdr_linea.esercizio_res
                AND linea_attivita.esercizio = voce_f_saldi_cdr_linea.esercizio
                AND linea_attivita.cd_centro_responsabilita =
                               voce_f_saldi_cdr_linea.cd_centro_responsabilita
                AND linea_attivita.cd_linea_attivita =
                                      voce_f_saldi_cdr_linea.cd_linea_attivita
                AND linea_attivita.ti_gestione IN ('S', 'X')
           GROUP BY voce_f_saldi_cdr_linea.esercizio,
                    voce_f_saldi_cdr_linea.esercizio_res,
                    voce_f_saldi_cdr_linea.cd_centro_responsabilita,
                    voce_f_saldi_cdr_linea.cd_linea_attivita,
                    voce_f_saldi_cdr_linea.ti_appartenenza,
                    voce_f_saldi_cdr_linea.ti_gestione,
                    voce_f_saldi_cdr_linea.cd_elemento_voce,
                    'Y',
                    'DIR',
                    linea_attivita.cd_funzione,
                    linea_attivita.cd_natura,
                    linea_attivita.ds_linea_attivita,
                    linea_attivita.pg_progetto) b ;
