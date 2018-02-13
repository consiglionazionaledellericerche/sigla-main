--------------------------------------------------------
--  DDL for View V_ASSESTATO_MODULO_VAR_PDG
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_ASSESTATO_MODULO_VAR_PDG" ("ESERCIZIO", "PG_VARIAZIONE_PDG", "TI_GESTIONE", "DIPART", "PROGETTO", "COMMESSA", "MODULO", "DS_MODULO", "TIPO_PROGETTO", "SPESE_INI", "COSTI_INI", "PRE_SPESE_VAR_APP", "PRE_COSTI_VAR_APP", "PRE_SPESE_VAR_PRD", "PRE_COSTI_VAR_PRD", "SPESE_VAR_APP", "COSTI_VAR_APP", "SPESE_VAR_PRD", "COSTI_VAR_PRD", "POST_SPESE_VAR_APP", "POST_COSTI_VAR_APP", "POST_SPESE_VAR_PRD", "POST_COSTI_VAR_PRD", "SPESE_ASSESTATO_APP", "COSTI_ASSESTATO_APP", "ENTRATE_INI", "RICAVI_INI", "PRE_ENTRATE_VAR_APP", "PRE_RICAVI_VAR_APP", "PRE_ENTRATE_VAR_PRD", "PRE_RICAVI_VAR_PRD", "ENTRATE_VAR_APP", "RICAVI_VAR_APP", "ENTRATE_VAR_PRD", "RICAVI_VAR_PRD", "POST_ENTRATE_VAR_APP", "POST_RICAVI_VAR_APP", "POST_ENTRATE_VAR_PRD", "POST_RICAVI_VAR_PRD", "ENTRATE_ASSESTATO_APP", "RICAVI_ASSESTATO_APP") AS 
  SELECT
--
-- Date: 09/11/2006
-- Version: 1.2
--
-- Vista che, per ogni variazione PDG e per modulo, ritorna i valori di stanziamento iniziale,
-- variazioni approvate e definitive prima e dopo la variazione
--
-- History:
--
-- Date: 04/08/2005
-- Version: 1.0
-- Creazione
--
-- Date: 15/05/2005
-- Version: 1.1
-- Aggiunta delle tabelle PDG_VARIAZIONE_RIGA_GEST
--
-- Date: 09/11/2006
-- Version: 1.2
-- Aggiunta la selezione del progetto/commessa/modulo per anno
--
-- Body:
--
          a.esercizio, a.pg_variazione_pdg, a.ti_gestione, b.dipart,
          b.progetto, b.commessa, b.modulo, b.ds_modulo, b.tipo_progetto,
          b.spese_ini spese_ini, b.costi_ini costi_ini,
          CASE 
            WHEN a.STATO IN ('APP','APF') 
            THEN b.spese_var_app - a.var_spese_var_app
            ELSE 0
            END pre_spese_var_app,
          CASE 
            WHEN a.STATO IN ('APP','APF') 
            THEN b.costi_var_app - a.var_costi_var_app
            ELSE 0
            END pre_costi_var_app,
          DECODE (a.stato,
                  'PRD', b.spese_var_prd - a.var_spese_var_prd,
                  0
                 ) pre_spese_var_prd,
          DECODE (a.stato,
                  'PRD', b.costi_var_prd - a.var_costi_var_prd,
                  0
                 ) pre_costi_var_prd,
          CASE 
            WHEN a.STATO IN ('APP','APF') 
            THEN a.var_spese_var_app
            ELSE 0
            END spese_var_app,
          CASE 
            WHEN a.STATO IN ('APP','APF') 
            THEN var_costi_var_app
            ELSE 0
            END costi_var_app,
          DECODE (a.stato, 'PRD', a.var_spese_var_prd, 0) spese_var_prd,
          DECODE (a.stato, 'PRD', a.var_costi_var_prd, 0) costi_var_prd,
          b.spese_var_app post_spese_var_app,
          b.costi_var_app post_costi_var_app,
          b.spese_var_prd post_spese_var_prd,
          b.costi_var_prd post_costi_var_prd,
          b.spese_ini + b.spese_var_app spese_assestato_app,
          b.costi_ini + b.costi_var_app costi_assestato_app,
          b.entrate_ini entrate_ini, b.ricavi_ini ricavi_ini,
          CASE 
            WHEN a.STATO IN ('APP','APF') 
            THEN b.entrate_var_app - a.var_entrate_var_app
            ELSE 0
            END pre_entrate_var_app,
          CASE 
            WHEN a.STATO IN ('APP','APF') 
            THEN b.ricavi_var_app - a.var_ricavi_var_app
            ELSE 0
            END pre_ricavi_var_app,
          DECODE (a.stato,
                  'PRD', b.entrate_var_prd - a.var_entrate_var_prd,
                  0
                 ) pre_entrate_var_prd,
          DECODE (a.stato,
                  'PRD', b.ricavi_var_prd - a.var_ricavi_var_prd,
                  0
                 ) pre_ricavi_var_prd,
          DECODE (a.stato, 'APP', a.var_entrate_var_app, 0) entrate_var_app,
          DECODE (a.stato, 'APP', a.var_ricavi_var_app, 0) ricavi_var_app,
          DECODE (a.stato, 'PRD', a.var_entrate_var_prd, 0) entrate_var_prd,
          DECODE (a.stato, 'PRD', a.var_ricavi_var_prd, 0) ricavi_var_prd,
          entrate_var_app post_entrate_var_app,
          ricavi_var_app post_ricavi_var_app,
          entrate_var_prd post_entrate_var_prd,
          ricavi_var_prd post_ricavi_var_prd,
          b.entrate_ini + b.entrate_var_app entrate_assestato_app,
          b.ricavi_ini + b.ricavi_var_app ricavi_assestato_app
     FROM (SELECT   pdg_preventivo_spe_det.esercizio esercizio,
                    pdg_preventivo_spe_det.pg_variazione_pdg
                                                            pg_variazione_pdg,
                    pdg_preventivo_spe_det.ti_gestione ti_gestione,
                    pdg_variazione.stato stato, progetto.cd_progetto modulo,
                    -- spese variate e approvate anno 1
                    SUM(CASE 
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
                          END) var_spese_var_app,
                    SUM(CASE 
                          WHEN NVL (pdg_variazione.stato, '-1') IN ('APP','APF') 
                          THEN NVL(pdg_preventivo_spe_det.im_rm_css_ammortamenti,0)
                             + NVL(pdg_preventivo_spe_det.im_rn_css_rimanenze,0)
                             + NVL(pdg_preventivo_spe_det.im_ro_css_altri_costi,0)
                          ELSE 0
                          END) var_costi_var_app,
                    -- spese variate e definitivamente proposti anno 1
                    SUM
                       (DECODE
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
                       ) var_spese_var_prd,
                    -- costi variati e definitivamente proposti anno 1
                    SUM
                       (DECODE
                           (NVL (pdg_variazione.stato, '-1'),
                            'PRD', NVL(pdg_preventivo_spe_det.im_rm_css_ammortamenti,0)
                                 + NVL(pdg_preventivo_spe_det.im_rn_css_rimanenze,0)
                                 + NVL(pdg_preventivo_spe_det.im_ro_css_altri_costi,0),
                            0
                           )
                       ) var_costi_var_prd,
                    0 var_entrate_ini, 0 var_ricavi_ini,
                    0 var_entrate_var_app, 0 var_ricavi_var_app,
                    0 var_entrate_var_prd, 0 var_ricavi_var_prd
               FROM pdg_preventivo_spe_det,
                    pdg_variazione,
                    linea_attivita,
                    progetto_gest progetto
              WHERE pdg_preventivo_spe_det.esercizio_pdg_variazione IS NOT NULL
                AND pdg_preventivo_spe_det.pg_variazione_pdg IS NOT NULL
                AND pdg_preventivo_spe_det.ti_gestione = 'S'
                AND pdg_preventivo_spe_det.ti_appartenenza = 'D'
                AND pdg_preventivo_spe_det.categoria_dettaglio IN
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
                    --Join tra "LINEA_ATTIVITA" e "PROGETTO"
                    NVL (linea_attivita.pg_progetto, -1) = progetto.pg_progetto(+)
                AND (   progetto.esercizio IS NULL
                     OR progetto.esercizio = pdg_preventivo_spe_det.esercizio
                    )
           GROUP BY pdg_preventivo_spe_det.esercizio,
                    pdg_preventivo_spe_det.pg_variazione_pdg,
                    pdg_preventivo_spe_det.ti_gestione,
                    pdg_variazione.stato,
                    progetto.cd_progetto
           UNION ALL
           SELECT   pdg_preventivo_etr_det.esercizio,
                    pdg_preventivo_etr_det.pg_variazione_pdg,
                    pdg_preventivo_etr_det.ti_gestione,
                    pdg_variazione.stato stato, progetto.cd_progetto modulo,
                    0 var_uscite_ini, 0 var_costi_ini, 0 var_uscite_var_app,
                    0 var_costi_var_app, 0 var_uscite_var_prd,
                    0 var_costi_var_prd,
                    -- entrate variate e approvate anno 1
                    SUM(CASE 
                          WHEN NVL (pdg_variazione.stato, '-1') IN ('APP','APF') 
                          THEN NVL (pdg_preventivo_etr_det.im_ra_rce,0)
                             + NVL (pdg_preventivo_etr_det.im_rc_esr, 0)
                          ELSE 0
                          END) var_entrate_var_app,
                    -- ricavi variati e approvati anno 1
                    SUM(CASE 
                          WHEN NVL (pdg_variazione.stato, '-1') IN ('APP','APF') 
                          THEN NVL (pdg_preventivo_etr_det.im_rb_rse,0)
                          ELSE 0
                          END) var_ricavi_var_app,
                    -- entrate variate e definitivamente proposti anno 1
                    SUM
                       (DECODE (NVL (pdg_variazione.stato, '-1'),
                                'PRD', NVL (pdg_preventivo_etr_det.im_ra_rce,0)
                                     + NVL (pdg_preventivo_etr_det.im_rc_esr, 0),
                                0
                               )
                       ) var_entrate_var_prd,
                    -- ricavi variati e definitivamente proposti anno 1
                    SUM
                       (DECODE (NVL (pdg_variazione.stato, '-1'),
                                'PRD', NVL (pdg_preventivo_etr_det.im_rb_rse,0),
                                0
                               )
                       ) var_ricavi_var_prd
               FROM pdg_preventivo_etr_det,
                    pdg_variazione,
                    linea_attivita,
                    progetto_gest progetto
              WHERE pdg_preventivo_etr_det.esercizio_pdg_variazione IS NOT NULL
                AND pdg_preventivo_etr_det.pg_variazione_pdg IS NOT NULL
                AND pdg_preventivo_etr_det.ti_gestione = 'E'
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
                    NVL (linea_attivita.pg_progetto, -1) = progetto.pg_progetto(+)
                AND (   progetto.esercizio IS NULL
                     OR progetto.esercizio = pdg_preventivo_etr_det.esercizio
                    )
           GROUP BY pdg_preventivo_etr_det.esercizio,
                    pdg_preventivo_etr_det.pg_variazione_pdg,
                    pdg_preventivo_etr_det.ti_gestione,
                    pdg_variazione.stato,
                    progetto.cd_progetto
           UNION ALL
           SELECT   pdg_variazione_riga_gest.esercizio esercizio,
                    pdg_variazione_riga_gest.pg_variazione_pdg
                                                            pg_variazione_pdg,
                    pdg_variazione_riga_gest.ti_gestione ti_gestione,
                    pdg_variazione.stato stato, progetto.cd_progetto modulo,
                    
                    -- spese variate e approvate anno 1
                    SUM(CASE 
                          WHEN NVL (pdg_variazione.stato, '-1') IN ('APP','APF') 
                          THEN NVL(pdg_variazione_riga_gest.im_spese_gest_decentrata_int,0)
                             + NVL(pdg_variazione_riga_gest.im_spese_gest_decentrata_est,0)
                             + NVL(pdg_variazione_riga_gest.im_spese_gest_accentrata_int,0)
                             + NVL(pdg_variazione_riga_gest.im_spese_gest_accentrata_est,0)
                          ELSE 0
                          END) var_spese_var_app,
                    -- costi variati e approvati anno 1
                    0 var_costi_var_app,
                    -- spese variate e definitivamente proposti anno 1
                    SUM
                       (DECODE
                           (NVL (pdg_variazione.stato, '-1'),
                            'PRD', NVL(pdg_variazione_riga_gest.im_spese_gest_decentrata_int,0)
                                 + NVL(pdg_variazione_riga_gest.im_spese_gest_decentrata_est,0)
                                 + NVL(pdg_variazione_riga_gest.im_spese_gest_accentrata_int,0)
                                 + NVL(pdg_variazione_riga_gest.im_spese_gest_accentrata_est,0),
                            0
                           )
                       ) var_spese_var_prd,
                    -- costi variati e definitivamente proposti anno 1
                    0 var_costi_var_prd, 0 var_entrate_ini, 0 var_ricavi_ini,
                    -- entrate variate e approvate anno 1
                    SUM(CASE 
                          WHEN NVL (pdg_variazione.stato, '-1') IN ('APP','APF') 
                          THEN NVL(pdg_variazione_riga_gest.im_entrata,0)
                          ELSE 0
                          END) var_entrate_var_app,
                    -- ricavi variati e approvati anno 1
                    0 var_ricavi_var_app,
                    -- entrate variate e definitivamente proposti anno 1
                    SUM
                       (DECODE (NVL (pdg_variazione.stato, '-1'),
                                'PRD', NVL(pdg_variazione_riga_gest.im_entrata,0),
                                0
                               )
                       ) var_entrate_var_prd,
                    -- ricavi variati e definitivamente proposti anno 1
                    0 var_ricavi_var_prd
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
                    --Join tra "PDG_VARIAZIONE_RIGA_GEST" e "V_LINEA_ATTIVITA_VALIDA"
                    v_linea_attivita_valida.esercizio =
                                  pdg_variazione_riga_gest.esercizio
                AND v_linea_attivita_valida.cd_centro_responsabilita =
                                  pdg_variazione_riga_gest.cd_cdr_assegnatario
                AND v_linea_attivita_valida.cd_linea_attivita =
                                    pdg_variazione_riga_gest.cd_linea_attivita
                AND
                    --Join tra "V_LINEA_ATTIVITA_VALIDA" e "PROGETTO"
                    NVL (v_linea_attivita_valida.pg_progetto, -1) = progetto.pg_progetto(+)
                AND (   progetto.esercizio IS NULL
                     OR progetto.esercizio =
                                            pdg_variazione_riga_gest.esercizio
                    )
           GROUP BY pdg_variazione_riga_gest.esercizio,
                    pdg_variazione_riga_gest.pg_variazione_pdg,
                    pdg_variazione_riga_gest.ti_gestione,
                    pdg_variazione.stato,
                    progetto.cd_progetto) a,
          v_assestato_modulo_pdg b
    WHERE a.esercizio = b.esercizio
      AND a.ti_gestione = b.ti_gestione
      AND a.modulo = b.modulo ;

   COMMENT ON TABLE "V_ASSESTATO_MODULO_VAR_PDG"  IS 'Vista che, per ogni variazione PDG e per modulo, ritorna i valori
di stanziamento iniziale, variazioni approvate e definitive prima e dopo la variazione';
