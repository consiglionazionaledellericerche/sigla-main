--------------------------------------------------------
--  DDL for View V_ASSESTATO_MODULO_PDG
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_ASSESTATO_MODULO_PDG" ("ESERCIZIO", "TI_GESTIONE", "DIPART", "PROGETTO", "COMMESSA", "MODULO", "DS_MODULO", "TIPO_PROGETTO", "SPESE_INI", "COSTI_INI", "SPESE_VAR_APP", "COSTI_VAR_APP", "SPESE_VAR_PRD", "COSTI_VAR_PRD", "ENTRATE_INI", "RICAVI_INI", "ENTRATE_VAR_APP", "RICAVI_VAR_APP", "ENTRATE_VAR_PRD", "RICAVI_VAR_PRD") AS 
  SELECT
--
-- Date: 09/11/2006
-- Version: 1.3
--
-- Vista che per modulo ritorna i valori di stanziamento, variazioni approvate e variazioni definitive del PDG
--
-- History:
--
-- Date: 04/08/2005
-- Version: 1.0
-- Creazione
--
-- Date: 01/01/2006
-- Version: 1.1
-- Aggiunta delle tabelle PDG_MODULO_SPESE_GEST e PDG_MODULO_ENTRATE_GEST
--
-- Date: 15/05/2006
-- Version: 1.2
-- Aggiunta delle tabelle PDG_VARIAZIONE_RIGA_GEST
--
-- Date: 09/11/2006
-- Version: 1.3
-- Aggiunta la selezione del progetto/commessa/modulo per anno
--
-- Body:
--
            esercizio, ti_gestione, dipart, progetto, commessa, modulo,
            ds_modulo, tipo_progetto, SUM (spese_ini) spese_ini,
            SUM (costi_ini) costi_ini, SUM (spese_var_app) spese_var_app,
            SUM (costi_var_app) costi_var_app,
            SUM (spese_var_prd) spese_var_prd,
            SUM (costi_var_prd) costi_var_prd, SUM (entrate_ini) entrate_ini,
            SUM (ricavi_ini) ricavi_ini, SUM (entrate_var_app)
                                                              entrate_var_app,
            SUM (ricavi_var_app) ricavi_var_app,
            SUM (entrate_var_prd) entrate_var_prd,
            SUM (ricavi_var_prd) ricavi_var_prd
       FROM (SELECT pdg_preventivo_spe_det.esercizio esercizio,
                    pdg_preventivo_spe_det.ti_gestione,
                    progetto1.cd_dipartimento dipart,
                    progetto1.cd_progetto progetto,
                    progetto2.cd_progetto commessa,
                    progetto3.cd_progetto modulo,
                    progetto3.ds_progetto ds_modulo,
                    progetto3.cd_tipo_progetto tipo_progetto,
                    -- spese iniziali anno 1
                    DECODE
                       (pdg_preventivo_spe_det.pg_variazione_pdg,
                        NULL, NVL(pdg_preventivo_spe_det.im_ri_ccs_spese_odc,0)
                            + NVL(pdg_preventivo_spe_det.im_rj_ccs_spese_odc_altra_uo,0)
                            + NVL(pdg_preventivo_spe_det.im_rk_ccs_spese_ogc, 0)
                            + NVL(pdg_preventivo_spe_det.im_rl_ccs_spese_ogc_altra_uo,0)
                            + NVL(pdg_preventivo_spe_det.im_rq_ssc_costi_odc, 0)
                            + NVL(pdg_preventivo_spe_det.im_rr_ssc_costi_odc_altra_uo,0)
                            + NVL (pdg_preventivo_spe_det.im_rs_ssc_costi_ogc, 0)
                            + NVL(pdg_preventivo_spe_det.im_rt_ssc_costi_ogc_altra_uo,0),
                        0
                       ) spese_ini,
                    -- costi iniziali anno 1
                    DECODE
                       (pdg_preventivo_spe_det.pg_variazione_pdg,
                        NULL, NVL(pdg_preventivo_spe_det.im_rm_css_ammortamenti,0)
                            + NVL (pdg_preventivo_spe_det.im_rn_css_rimanenze, 0)
                            + NVL (pdg_preventivo_spe_det.im_ro_css_altri_costi,
                                0
                               ),
                        0
                       ) costi_ini,
                    -- spese variate e approvate anno 1
                    DECODE
                       (pdg_preventivo_spe_det.pg_variazione_pdg,
                        NULL, 0,
                        CASE 
                          WHEN NVL (pdg_variazione.stato, '-1') IN ('APP','APF') 
                          THEN NVL(pdg_preventivo_spe_det.im_ri_ccs_spese_odc,0)
                             + NVL(pdg_preventivo_spe_det.im_rj_ccs_spese_odc_altra_uo,0)
                             + NVL(pdg_preventivo_spe_det.im_rk_ccs_spese_ogc,0)
                             + NVL(pdg_preventivo_spe_det.im_rl_ccs_spese_ogc_altra_uo,0)
                             + NVL(pdg_preventivo_spe_det.im_rq_ssc_costi_odc,0)
                             + NVL(pdg_preventivo_spe_det.im_rr_ssc_costi_odc_altra_uo,0)
                             + NVL(pdg_preventivo_spe_det.im_rs_ssc_costi_ogc,0)
                             + NVL(pdg_preventivo_spe_det.im_rt_ssc_costi_ogc_altra_uo,0)
                          ELSE 0
                          END) spese_var_app,
                    -- costi variati e approvati anno 1
                    DECODE
                       (pdg_preventivo_spe_det.pg_variazione_pdg,
                        NULL, 0,
                        CASE 
                          WHEN NVL (pdg_variazione.stato, '-1') IN ('APP','APF') 
                          THEN NVL(pdg_preventivo_spe_det.im_rm_css_ammortamenti,0)
                             + NVL(pdg_preventivo_spe_det.im_rn_css_rimanenze,0)
                             + NVL(pdg_preventivo_spe_det.im_ro_css_altri_costi,0)
                          ELSE 0
                          END) costi_var_app,
                    -- spese variate e definitivamente proposti anno 1
                    DECODE
                       (pdg_preventivo_spe_det.pg_variazione_pdg,
                        NULL, 0,
                        DECODE
                           (NVL (pdg_variazione.stato, '-1'),
                            'PRD', NVL(pdg_preventivo_spe_det.im_ri_ccs_spese_odc,0)
                                 + NVL(pdg_preventivo_spe_det.im_rj_ccs_spese_odc_altra_uo,0)
                                 + NVL(pdg_preventivo_spe_det.im_rk_ccs_spese_ogc,0)
                                 + NVL(pdg_preventivo_spe_det.im_rl_ccs_spese_ogc_altra_uo,0)
                                 + NVL(pdg_preventivo_spe_det.im_rq_ssc_costi_odc,0)
                                 + NVL(pdg_preventivo_spe_det.im_rr_ssc_costi_odc_altra_uo,0)
                                 + NVL(pdg_preventivo_spe_det.im_rs_ssc_costi_ogc,0)
                                 + NVL(pdg_preventivo_spe_det.im_rt_ssc_costi_ogc_altra_uo,0),
                            0
                           )
                       ) spese_var_prd,
                    -- costi variati e definitivamente proposti anno 1
                    DECODE
                       (pdg_preventivo_spe_det.pg_variazione_pdg,
                        NULL, 0,
                        DECODE
                           (NVL (pdg_variazione.stato, '-1'),
                            'PRD', NVL(pdg_preventivo_spe_det.im_rm_css_ammortamenti,0)
                                 + NVL(pdg_preventivo_spe_det.im_rn_css_rimanenze,0)
                                 + NVL(pdg_preventivo_spe_det.im_ro_css_altri_costi,0),
                            0
                           )
                       ) costi_var_prd,
                    0 entrate_ini, 0 ricavi_ini, 0 entrate_var_app,
                    0 ricavi_var_app, 0 entrate_var_prd, 0 ricavi_var_prd
               FROM pdg_preventivo_spe_det,
                    pdg_variazione,
                    linea_attivita,
                    progetto_prev progetto1,
                    progetto_prev progetto2,
                    progetto_prev progetto3
              WHERE
--                PDG_PREVENTIVO_SPE_DET.STATO = 'Y' And
                    pdg_preventivo_spe_det.ti_gestione = 'S'
                AND pdg_preventivo_spe_det.ti_appartenenza = 'D'
                AND
--                PDG_PREVENTIVO_SPE_DET.CATEGORIA_DETTAGLIO='SIN' And
                    pdg_preventivo_spe_det.categoria_dettaglio IN
                                                        ('SIN', 'SCR', 'CAR')
                AND pdg_preventivo_spe_det.origine IN ('DIR', 'PDV')
                AND
                    --Join tra "PDG_PREVENTIVO_SPE_DET" e "PDG_VARIAZIONE"
                    NVL (pdg_preventivo_spe_det.esercizio_pdg_variazione, -1) =
                                                                           pdg_variazione.esercizio(+)
                AND NVL (pdg_preventivo_spe_det.pg_variazione_pdg, -1) = pdg_variazione.pg_variazione_pdg(+)
                AND
                    --Join tra "PDG_PREVENTIVO_SPE_DET" e "LINEA_ATTIVITA"
                    linea_attivita.cd_centro_responsabilita =
                               pdg_preventivo_spe_det.cd_centro_responsabilita
                AND linea_attivita.cd_linea_attivita =
                                      pdg_preventivo_spe_det.cd_linea_attivita
                AND
                    --Join tra "LINEA_ATTIVITA" e "PROGETTO3"
                    NVL (linea_attivita.pg_progetto, -1) = progetto3.pg_progetto(+)
                AND (   progetto3.esercizio IS NULL
                     OR progetto3.esercizio = pdg_preventivo_spe_det.esercizio
                    )
                AND NVL (progetto3.esercizio_progetto_padre, -1) = progetto2.esercizio(+)
                AND NVL (progetto3.pg_progetto_padre, -1) = progetto2.pg_progetto(+)
                AND NVL (progetto2.esercizio_progetto_padre, -1) = progetto1.esercizio(+)
                AND NVL (progetto2.pg_progetto_padre, -1) = progetto1.pg_progetto(+)
             UNION ALL
             SELECT pdg_preventivo_etr_det.esercizio,
                    pdg_preventivo_etr_det.ti_gestione,
                    progetto1.cd_dipartimento, progetto1.cd_progetto,
                    progetto2.cd_progetto, progetto3.cd_progetto,
                    progetto3.ds_progetto, progetto3.cd_tipo_progetto,
                    0 uscite_ini, 0 costi_ini, 0 uscite_var_app,
                    0 costi_var_app, 0 uscite_var_prd, 0 costi_var_prd,
                    -- entrate iniziali anno 1
                    DECODE
                       (pdg_preventivo_etr_det.pg_variazione_pdg,
                        NULL, NVL (pdg_preventivo_etr_det.im_ra_rce, 0)
                         + NVL (pdg_preventivo_etr_det.im_rc_esr, 0),
                        0
                       ) entrate_ini,
                    -- ricavi iniziali anno 1
                    DECODE
                        (pdg_preventivo_etr_det.pg_variazione_pdg,
                         NULL, NVL (pdg_preventivo_etr_det.im_rb_rse, 0),
                         0
                        ) ricavi_ini,
                    -- entrate variate e approvate anno 1
                    DECODE
                       (pdg_preventivo_etr_det.pg_variazione_pdg,
                        NULL, 0,
                        CASE 
                          WHEN NVL (pdg_variazione.stato, '-1') IN ('APP','APF') 
                          THEN NVL(pdg_preventivo_etr_det.im_ra_rce,0)
                             + NVL(pdg_preventivo_etr_det.im_rc_esr,0)
                          ELSE 0
                          END) entrate_var_app,
                    -- ricavi variati e approvati anno 1
                    DECODE
                       (pdg_preventivo_etr_det.pg_variazione_pdg,
                        NULL, 0,
                        CASE 
                          WHEN NVL (pdg_variazione.stato, '-1') IN ('APP','APF') 
                          THEN NVL(pdg_preventivo_etr_det.im_rb_rse,0)
                          ELSE 0
                          END) ricavi_var_app,
                    -- entrate variate e definitivamente proposti anno 1
                    DECODE
                       (pdg_preventivo_etr_det.pg_variazione_pdg,
                        NULL, 0,
                        DECODE (NVL (pdg_variazione.stato, '-1'),
                                'PRD', NVL (pdg_preventivo_etr_det.im_ra_rce,0)
                                     + NVL (pdg_preventivo_etr_det.im_rc_esr, 0),
                                0
                               )
                       ) entrate_var_prd,
                    -- ricavi variati e definitivamente proposti anno 1
                    DECODE
                       (pdg_preventivo_etr_det.pg_variazione_pdg,
                        NULL, 0,
                        DECODE (NVL (pdg_variazione.stato, '-1'),
                                'PRD', NVL (pdg_preventivo_etr_det.im_rb_rse,0),
                                0
                               )
                       ) ricavi_var_prd
               FROM pdg_preventivo_etr_det,
                    pdg_variazione,
                    linea_attivita,
                    progetto_prev progetto1,
                    progetto_prev progetto2,
                    progetto_prev progetto3
              WHERE
--                PDG_PREVENTIVO_ETR_DET.STATO = 'Y' And
                    pdg_preventivo_etr_det.ti_gestione = 'E'
                AND pdg_preventivo_etr_det.ti_appartenenza = 'C'
                AND pdg_preventivo_etr_det.origine IN ('DIR', 'PDV')
                AND
                    --Join tra "PDG_PREVENTIVO_ETR_DET" e "PDG_VARIAZIONE"
                    NVL (pdg_preventivo_etr_det.esercizio_pdg_variazione, -1) =
                                                                           pdg_variazione.esercizio(+)
                AND NVL (pdg_preventivo_etr_det.pg_variazione_pdg, -1) = pdg_variazione.pg_variazione_pdg(+)
                AND
                    --Join tra "PDG_PREVENTIVO_SPE_DET" e "LINEA_ATTIVITA"
                    linea_attivita.cd_centro_responsabilita =
                               pdg_preventivo_etr_det.cd_centro_responsabilita
                AND linea_attivita.cd_linea_attivita =
                                      pdg_preventivo_etr_det.cd_linea_attivita
                AND
                    --Join tra "LINEA_ATTIVITA" e "PROGETTO3"
                    NVL (linea_attivita.pg_progetto, -1) = progetto3.pg_progetto(+)
                AND (   progetto3.esercizio IS NULL
                     OR progetto3.esercizio = pdg_preventivo_etr_det.esercizio
                    )
                AND NVL (progetto3.esercizio_progetto_padre, -1) = progetto2.esercizio(+)
                AND NVL (progetto3.pg_progetto_padre, -1) = progetto2.pg_progetto(+)
                AND NVL (progetto2.esercizio_progetto_padre, -1) = progetto1.esercizio(+)
                AND NVL (progetto2.pg_progetto_padre, -1) = progetto1.pg_progetto(+)
             UNION ALL
             SELECT a.esercizio, a.ti_gestione, a.dipart, a.progetto, a.commessa, a.modulo, a.ds_modulo, a.tipo_progetto, 
                    a.spese_ini, a.costi_ini, a.spese_var_app, a.costi_var_app, a.spese_var_prd, a.costi_var_prd,
                    a.entrate_ini, a.ricavi_ini, a.entrate_var_app, a.ricavi_var_app, a.entrate_var_prd, a.ricavi_var_prd
             FROM (SELECT pdg_modulo_spese_gest.esercizio esercizio,
                          pdg_modulo_spese_gest.ti_gestione,
                          progetto1.cd_dipartimento dipart,
                          progetto1.cd_progetto progetto,
                          progetto2.cd_progetto commessa,
                          progetto3.cd_progetto modulo,
                          progetto3.ds_progetto ds_modulo,
                          progetto3.cd_tipo_progetto tipo_progetto,
                          -- spese iniziali anno 1
                            NVL
                               (pdg_modulo_spese_gest.im_spese_gest_decentrata_int,
                                0
                               )
                          + NVL (pdg_modulo_spese_gest.im_spese_gest_decentrata_est,
                                 0
                                )
                          + NVL (pdg_modulo_spese_gest.im_spese_gest_accentrata_int,
                                 0
                                )
                          + NVL (pdg_modulo_spese_gest.im_spese_gest_accentrata_est,
                                 0
                                ) spese_ini,
                          -- costi iniziali anno 1
                          0 costi_ini,
                          -- spese variate e approvate anno 1
                          0 spese_var_app,
                          -- costi variati e approvati anno 1
                          0 costi_var_app,
                          -- spese variate e definitivamente proposti anno 1
                          0 spese_var_prd,
                          -- costi variati e definitivamente proposti anno 1
                          0 costi_var_prd, 0 entrate_ini, 0 ricavi_ini,
                          0 entrate_var_app, 0 ricavi_var_app, 0 entrate_var_prd,
                          0 ricavi_var_prd
                     FROM pdg_modulo_spese_gest,
                          linea_attivita,
                          progetto_gest progetto1,
                          progetto_gest progetto2,
                          progetto_gest progetto3
                    WHERE pdg_modulo_spese_gest.ti_gestione = 'S'
                      AND pdg_modulo_spese_gest.ti_appartenenza = 'D'
                      AND pdg_modulo_spese_gest.categoria_dettaglio IN
                                                                     ('DIR', 'STI')
                      AND
                          --Join tra "PDG_MODULO_SPESE_GEST" e "LINEA_ATTIVITA"
                          linea_attivita.cd_centro_responsabilita =
                                           pdg_modulo_spese_gest.cd_cdr_assegnatario
                      AND linea_attivita.cd_linea_attivita =
                                             pdg_modulo_spese_gest.cd_linea_attivita
                      AND
                          --Join tra "LINEA_ATTIVITA" e "PROGETTO3"
                          NVL (linea_attivita.pg_progetto, -1) = progetto3.pg_progetto(+)
                      AND (   progetto3.esercizio IS NULL
                           OR progetto3.esercizio = pdg_modulo_spese_gest.esercizio
                          )
                      AND NVL (progetto3.esercizio_progetto_padre, -1) = progetto2.esercizio(+)
                      AND NVL (progetto3.pg_progetto_padre, -1) = progetto2.pg_progetto(+)
                      AND NVL (progetto2.esercizio_progetto_padre, -1) = progetto1.esercizio(+)
                      AND NVL (progetto2.pg_progetto_padre, -1) = progetto1.pg_progetto(+)
                   UNION ALL
                   SELECT pdg_modulo_entrate_gest.esercizio,
                          pdg_modulo_entrate_gest.ti_gestione,
                          progetto1.cd_dipartimento, progetto1.cd_progetto,
                          progetto2.cd_progetto, progetto3.cd_progetto,
                          progetto3.ds_progetto, progetto3.cd_tipo_progetto,
                          0 uscite_ini, 0 costi_ini, 0 uscite_var_app,
                          0 costi_var_app, 0 uscite_var_prd, 0 costi_var_prd,
                          -- entrate iniziali anno 1
                          NVL (pdg_modulo_entrate_gest.im_entrata, 0) entrate_ini,
                          -- ricavi iniziali anno 1
                          0 ricavi_ini,
                          -- entrate variate e approvate anno 1
                          0 entrate_var_app,
                          -- ricavi variati e approvati anno 1
                          0 ricavi_var_app,
                          -- entrate variate e definitivamente proposti anno 1
                          0 entrate_var_prd,
                          -- ricavi variati e definitivamente proposti anno 1
                          0 ricavi_var_prd
                     FROM pdg_modulo_entrate_gest,
                          linea_attivita,
                          progetto_gest progetto1,
                          progetto_gest progetto2,
                          progetto_gest progetto3
                    WHERE pdg_modulo_entrate_gest.ti_gestione = 'E'
                      AND pdg_modulo_entrate_gest.ti_appartenenza = 'C'
                      AND pdg_modulo_entrate_gest.categoria_dettaglio = 'DIR'
                      AND
                          --Join tra "PDG_MODULO_ENTRATE_GEST" e "LINEA_ATTIVITA"
                          linea_attivita.cd_centro_responsabilita =
                                         pdg_modulo_entrate_gest.cd_cdr_assegnatario
                      AND linea_attivita.cd_linea_attivita =
                                           pdg_modulo_entrate_gest.cd_linea_attivita
                      AND
                          --Join tra "LINEA_ATTIVITA" e "PROGETTO3"
                          NVL (linea_attivita.pg_progetto, -1) = progetto3.pg_progetto(+)
                      AND (   progetto3.esercizio IS NULL
                           OR progetto3.esercizio =
                                                   pdg_modulo_entrate_gest.esercizio
                          )
                      AND NVL (progetto3.esercizio_progetto_padre, -1) = progetto2.esercizio(+)
                      AND NVL (progetto3.pg_progetto_padre, -1) = progetto2.pg_progetto(+)
                      AND NVL (progetto2.esercizio_progetto_padre, -1) = progetto1.esercizio(+)
                      AND NVL (progetto2.pg_progetto_padre, -1) = progetto1.pg_progetto(+)
                   UNION ALL
                   SELECT pdg_variazione_riga_gest.esercizio esercizio,
                          pdg_variazione_riga_gest.ti_gestione,
                          progetto1.cd_dipartimento dipart,
                          progetto1.cd_progetto progetto,
                          progetto2.cd_progetto commessa,
                          progetto3.cd_progetto modulo,
                          progetto3.ds_progetto ds_modulo,
                          progetto3.cd_tipo_progetto tipo_progetto,
                          -- spese iniziali anno 1
                          0 spese_ini,
                          -- costi iniziali anno 1
                          0 costi_ini,
                          -- spese variate e approvate anno 1
                          CASE 
                            WHEN NVL (pdg_variazione.stato, '-1') IN ('APP','APF') 
                            THEN NVL(pdg_variazione_riga_gest.im_spese_gest_decentrata_int,0)
                               + NVL(pdg_variazione_riga_gest.im_spese_gest_decentrata_est,0)
                               + NVL(pdg_variazione_riga_gest.im_spese_gest_accentrata_int,0)
                               + NVL(pdg_variazione_riga_gest.im_spese_gest_accentrata_est,0)
                            ELSE 0
                            END spese_var_app,
                          -- costi variati e approvati anno 1
                          0 costi_var_app,
                          -- spese variate e definitivamente proposti anno 1
                          DECODE
                             (NVL (pdg_variazione.stato, '-1'),
                              'PRD', NVL(pdg_variazione_riga_gest.im_spese_gest_decentrata_int,0)
                                   + NVL(pdg_variazione_riga_gest.im_spese_gest_decentrata_est,0)
                                   + NVL(pdg_variazione_riga_gest.im_spese_gest_accentrata_int,0)
                                   + NVL(pdg_variazione_riga_gest.im_spese_gest_accentrata_est,0),
                              0
                             ) spese_var_prd,
                          -- costi variati e definitivamente proposti anno 1
                          0 costi_var_prd, 0 entrate_ini,
                          -- ricavi iniziali anno 1
                          0 ricavi_ini,
                          -- entrate variate e approvate anno 1
                          CASE 
                            WHEN NVL (pdg_variazione.stato, '-1') IN ('APP','APF') 
                            THEN NVL(pdg_variazione_riga_gest.im_entrata,0)
                            ELSE 0
                            END entrate_var_app,
                          -- ricavi variati e approvati anno 1
                          0 ricavi_var_app,
                          -- entrate variate e definitivamente proposti anno 1
                          DECODE
                             (NVL (pdg_variazione.stato, '-1'),
                              'PRD', NVL (pdg_variazione_riga_gest.im_entrata, 0),0) entrate_var_prd,
                          -- ricavi variati e definitivamente proposti anno 1
                          0 ricavi_var_prd
                   FROM pdg_variazione_riga_gest,
                        pdg_variazione,
                        linea_attivita,
                        progetto_gest progetto1,
                        progetto_gest progetto2,
                        progetto_gest progetto3
                   WHERE pdg_variazione_riga_gest.categoria_dettaglio = 'DIR'
                     AND
                          --Join tra "PDG_VARIAZIONE_RIGA_GEST" e "PDG_VARIAZIONE"
                         pdg_variazione_riga_gest.esercizio =
                                                            pdg_variazione.esercizio
                     AND pdg_variazione_riga_gest.pg_variazione_pdg =
                                                    pdg_variazione.pg_variazione_pdg
                     AND
                         --Join tra "PDG_VARIAZIONE_RIGA_GEST" e "LINEA_ATTIVITA"
                         linea_attivita.cd_centro_responsabilita =
                                       pdg_variazione_riga_gest.cd_cdr_assegnatario
                     AND linea_attivita.cd_linea_attivita =
                                         pdg_variazione_riga_gest.cd_linea_attivita
                     AND
                         --Join tra "LINEA_ATTIVITA" e "PROGETTO3"
                         NVL (linea_attivita.pg_progetto, -1) = progetto3.pg_progetto(+)
                     AND (   progetto3.esercizio IS NULL
                          OR progetto3.esercizio =
                                                 pdg_variazione_riga_gest.esercizio
                         )
                     AND NVL (progetto3.esercizio_progetto_padre, -1) = progetto2.esercizio(+)
                     AND NVL (progetto3.pg_progetto_padre, -1) = progetto2.pg_progetto(+)
                     AND NVL (progetto2.esercizio_progetto_padre, -1) = progetto1.esercizio(+)
                     AND NVL (progetto2.pg_progetto_padre, -1) = progetto1.pg_progetto(+)) a, parametri_cnr
             WHERE a.esercizio = parametri_cnr.esercizio
             and   parametri_cnr.fl_nuovo_pdg = 'N'
             UNION ALL
             SELECT a.esercizio, a.ti_gestione, a.dipart, a.progetto, a.commessa, a.modulo, a.ds_modulo, a.tipo_progetto, 
                    a.spese_ini, a.costi_ini, a.spese_var_app, a.costi_var_app, a.spese_var_prd, a.costi_var_prd,
                    a.entrate_ini, a.ricavi_ini, a.entrate_var_app, a.ricavi_var_app, a.entrate_var_prd, a.ricavi_var_prd
             FROM (SELECT pdg_modulo_spese_gest.esercizio esercizio,
                          pdg_modulo_spese_gest.ti_gestione,
                          (SELECT progetto_gest.cd_dipartimento
                             FROM progetto_gest
                            WHERE progetto_gest.esercizio = progetto.esercizio_progetto_padre
                              AND progetto_gest.pg_progetto = progetto.pg_progetto_padre) dipart,
                          NULL progetto,
                          (SELECT progetto_gest.cd_progetto
                             FROM progetto_gest
                            WHERE progetto_gest.esercizio = progetto.esercizio_progetto_padre
                              AND progetto_gest.pg_progetto = progetto.pg_progetto_padre) commessa,
                          progetto.cd_progetto modulo,
                          progetto.ds_progetto ds_modulo,
                          progetto.cd_tipo_progetto tipo_progetto,
                          -- spese iniziali anno 1
                            NVL
                               (pdg_modulo_spese_gest.im_spese_gest_decentrata_int,
                                0
                               )
                          + NVL (pdg_modulo_spese_gest.im_spese_gest_decentrata_est,
                                 0
                                )
                          + NVL (pdg_modulo_spese_gest.im_spese_gest_accentrata_int,
                                 0
                                )
                          + NVL (pdg_modulo_spese_gest.im_spese_gest_accentrata_est,
                                 0
                                ) spese_ini,
                          
                          -- costi iniziali anno 1
                          0 costi_ini,
                          -- spese variate e approvate anno 1
                          0 spese_var_app,
                          -- costi variati e approvati anno 1
                          0 costi_var_app,
                          -- spese variate e definitivamente proposti anno 1
                          0 spese_var_prd,
                          -- costi variati e definitivamente proposti anno 1
                          0 costi_var_prd, 0 entrate_ini, 0 ricavi_ini,
                          0 entrate_var_app, 0 ricavi_var_app, 0 entrate_var_prd,
                          0 ricavi_var_prd
                   FROM pdg_modulo_spese_gest,
                        v_linea_attivita_valida,
                        progetto_gest progetto
                   WHERE pdg_modulo_spese_gest.ti_gestione = 'S'
                    AND pdg_modulo_spese_gest.ti_appartenenza = 'D'
                    AND pdg_modulo_spese_gest.categoria_dettaglio IN
                                                                   ('DIR', 'STI')
                    AND
                        --Join tra "PDG_MODULO_SPESE_GEST" e "LINEA_ATTIVITA"
                        v_linea_attivita_valida.esercizio =
                                         pdg_modulo_spese_gest.esercizio
                    AND v_linea_attivita_valida.cd_centro_responsabilita =
                                         pdg_modulo_spese_gest.cd_cdr_assegnatario
                    AND v_linea_attivita_valida.cd_linea_attivita =
                                           pdg_modulo_spese_gest.cd_linea_attivita
                    AND
                        --Join tra "LINEA_ATTIVITA" e "PROGETTO3"
                        NVL (v_linea_attivita_valida.pg_progetto, -1) = progetto.pg_progetto(+)
                    AND (   progetto.esercizio IS NULL
                         OR progetto.esercizio = pdg_modulo_spese_gest.esercizio
                        )
                   UNION ALL
                   SELECT pdg_modulo_entrate_gest.esercizio,
                          pdg_modulo_entrate_gest.ti_gestione,
                          (SELECT progetto_gest.cd_dipartimento
                             FROM progetto_gest
                            WHERE progetto_gest.esercizio = progetto.esercizio_progetto_padre
                              AND progetto_gest.pg_progetto = progetto.pg_progetto_padre) dipart,
                          NULL progetto,
                          (SELECT progetto_gest.cd_progetto
                             FROM progetto_gest
                            WHERE progetto_gest.esercizio = progetto.esercizio_progetto_padre
                              AND progetto_gest.pg_progetto = progetto.pg_progetto_padre) commessa,
                          progetto.cd_progetto modulo,
                          progetto.ds_progetto ds_modulo,
                          progetto.cd_tipo_progetto tipo_progetto,
                          0 uscite_ini, 0 costi_ini, 0 uscite_var_app,
                          0 costi_var_app, 0 uscite_var_prd, 0 costi_var_prd,
                          -- entrate iniziali anno 1
                          NVL (pdg_modulo_entrate_gest.im_entrata, 0) entrate_ini,
                          -- ricavi iniziali anno 1
                          0 ricavi_ini,
                          -- entrate variate e approvate anno 1
                          0 entrate_var_app,
                          -- ricavi variati e approvati anno 1
                          0 ricavi_var_app,
                          -- entrate variate e definitivamente proposti anno 1
                          0 entrate_var_prd,
                          -- ricavi variati e definitivamente proposti anno 1
                          0 ricavi_var_prd
                     FROM pdg_modulo_entrate_gest,
                          v_linea_attivita_valida,
                          progetto_gest progetto
                    WHERE pdg_modulo_entrate_gest.ti_gestione = 'E'
                      AND pdg_modulo_entrate_gest.ti_appartenenza = 'C'
                      AND pdg_modulo_entrate_gest.categoria_dettaglio = 'DIR'
                      AND
                          --Join tra "PDG_MODULO_ENTRATE_GEST" e "LINEA_ATTIVITA"
                          v_linea_attivita_valida.esercizio =
                                         pdg_modulo_entrate_gest.esercizio
                      AND v_linea_attivita_valida.cd_centro_responsabilita =
                                         pdg_modulo_entrate_gest.cd_cdr_assegnatario
                      AND v_linea_attivita_valida.cd_linea_attivita =
                                           pdg_modulo_entrate_gest.cd_linea_attivita
                      AND
                          --Join tra "LINEA_ATTIVITA" e "PROGETTO3"
                          NVL (v_linea_attivita_valida.pg_progetto, -1) = progetto.pg_progetto(+)
                      AND (   progetto.esercizio IS NULL
                           OR progetto.esercizio =
                                                   pdg_modulo_entrate_gest.esercizio
                          )
                   UNION ALL
                   SELECT pdg_variazione_riga_gest.esercizio esercizio,
                          pdg_variazione_riga_gest.ti_gestione,
                          (SELECT progetto_gest.cd_dipartimento
                             FROM progetto_gest
                            WHERE progetto_gest.esercizio = progetto.esercizio_progetto_padre
                              AND progetto_gest.pg_progetto = progetto.pg_progetto_padre) dipart,
                          NULL progetto,
                          (SELECT progetto_gest.cd_progetto
                             FROM progetto_gest
                            WHERE progetto_gest.esercizio = progetto.esercizio_progetto_padre
                              AND progetto_gest.pg_progetto = progetto.pg_progetto_padre) commessa,
                          progetto.cd_progetto modulo,
                          progetto.ds_progetto ds_modulo,
                          progetto.cd_tipo_progetto tipo_progetto,
                          -- spese iniziali anno 1
                          0 spese_ini,
                          -- costi iniziali anno 1
                          0 costi_ini,
                          -- spese variate e approvate anno 1
                          CASE 
                            WHEN NVL (pdg_variazione.stato, '-1') IN ('APP','APF') 
                            THEN NVL(pdg_variazione_riga_gest.im_spese_gest_decentrata_int,0)
                               + NVL(pdg_variazione_riga_gest.im_spese_gest_decentrata_est,0)
                               + NVL(pdg_variazione_riga_gest.im_spese_gest_accentrata_int,0)
                               + NVL(pdg_variazione_riga_gest.im_spese_gest_accentrata_est,0)
                            ELSE 0
                            END spese_var_app,
                          -- costi variati e approvati anno 1
                          0 costi_var_app,
                          -- spese variate e definitivamente proposti anno 1
                          DECODE
                             (NVL (pdg_variazione.stato, '-1'),
                              'PRD', NVL(pdg_variazione_riga_gest.im_spese_gest_decentrata_int,0)
                                   + NVL(pdg_variazione_riga_gest.im_spese_gest_decentrata_est,0)
                                   + NVL(pdg_variazione_riga_gest.im_spese_gest_accentrata_int,0)
                                   + NVL(pdg_variazione_riga_gest.im_spese_gest_accentrata_est,0),
                              0
                             ) spese_var_prd,
                          -- costi variati e definitivamente proposti anno 1
                          0 costi_var_prd, 0 entrate_ini,
                          -- ricavi iniziali anno 1
                          0 ricavi_ini,
                          -- entrate variate e approvate anno 1
                          CASE 
                            WHEN NVL (pdg_variazione.stato, '-1') IN ('APP','APF') 
                            THEN NVL(pdg_variazione_riga_gest.im_entrata,0)
                            ELSE 0
                            END entrate_var_app,
                          -- ricavi variati e approvati anno 1
                          0 ricavi_var_app,
                          -- entrate variate e definitivamente proposti anno 1
                          DECODE
                             (NVL (pdg_variazione.stato, '-1'),
                              'PRD', NVL (pdg_variazione_riga_gest.im_entrata, 0),0) entrate_var_prd,
                          -- ricavi variati e definitivamente proposti anno 1
                          0 ricavi_var_prd
                   FROM pdg_variazione_riga_gest,
                        pdg_variazione,
                        v_linea_attivita_valida,
                        progetto_gest progetto
                   WHERE pdg_variazione_riga_gest.categoria_dettaglio = 'DIR'
                     AND
                          --Join tra "PDG_VARIAZIONE_RIGA_GEST" e "PDG_VARIAZIONE"
                         pdg_variazione_riga_gest.esercizio =
                                                            pdg_variazione.esercizio
                     AND pdg_variazione_riga_gest.pg_variazione_pdg =
                                                    pdg_variazione.pg_variazione_pdg
                     AND
                         --Join tra "PDG_VARIAZIONE_RIGA_GEST" e "LINEA_ATTIVITA"
                         v_linea_attivita_valida.esercizio =
                                         pdg_variazione_riga_gest.esercizio
                     AND v_linea_attivita_valida.cd_centro_responsabilita =
                                       pdg_variazione_riga_gest.cd_cdr_assegnatario
                     AND v_linea_attivita_valida.cd_linea_attivita =
                                         pdg_variazione_riga_gest.cd_linea_attivita
                     AND
                         --Join tra "LINEA_ATTIVITA" e "PROGETTO3"
                         NVL (v_linea_attivita_valida.pg_progetto, -1) = progetto.pg_progetto(+)
                     AND (   progetto.esercizio IS NULL
                          OR progetto.esercizio =
                                                 pdg_variazione_riga_gest.esercizio
                         )) a, parametri_cnr
             WHERE a.esercizio = parametri_cnr.esercizio
             and   parametri_cnr.fl_nuovo_pdg = 'Y')
   GROUP BY esercizio,
            ti_gestione,
            dipart,
            progetto,
            commessa,
            modulo,
            ds_modulo,
            tipo_progetto ;

   COMMENT ON TABLE "V_ASSESTATO_MODULO_PDG"  IS 'Vista che per modulo ritorna i valori di stanziamento,
variazioni approvate, variazioni definitive del PDG';
