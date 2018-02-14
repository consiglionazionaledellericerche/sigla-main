--------------------------------------------------------
--  DDL for View V_PDG_VARIAZIONE_SPE_DET
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_PDG_VARIAZIONE_SPE_DET" ("ESERCIZIO", "CD_CENTRO_RESPONSABILITA", "DS_CDR", "CD_LINEA_ATTIVITA", "DENOMINAZIONE", "DS_LINEA_ATTIVITA", "PG_PROGETTO", "TI_GESTIONE", "CD_ELEMENTO_VOCE", "DS_ELEMENTO_VOCE", "VARIAZIONE", "ASSESTATO_SPESA", "STATO", "CD_NATURA", "DS_NATURA", "PG_VARIAZIONE_PDG", "ESERCIZIO_VARIAZIONE_PDG", "CD_CENTRO_RESPONSABILITA_CLGS", "DS_CDR_CLGS", "ID_CLASSIFICAZIONE", "DS_CLASSIFICAZIONE", "TITOLO", "CATEGORIA", "CD_MODULO", "DS_MODULO", "LIVELLO_PROGETTO") AS 
  SELECT
--
-- Date: 13/04/2007
-- Version: 1.4
--
-- Vista di estrazione dei dati per la stampa Variazione al PdG
-- in particolare per il sub-report sulle spese
--
-- History:
--
-- Date: 06/10/2005
-- Version: 1.0
-- Creazione
--
-- Date: 03/11/2005
-- Version: 1.1
-- E' stata Aggiunta la funzione ASSESTATO_SPESA
--
-- Date: 16/05/2006
-- Version: 1.2
-- Aggiunte le nuove tabelle di gestione del PDG
--
-- Date: 09/11/2006
-- Version: 1.3
-- Aggiunta la selezione del progetto/commessa/modulo per anno
--
-- Date: 13/04/2007
-- Version: 1.4
-- Modifica: La view deve estrarre i dati da progetto_gest e non da progetto_prev
--
-- Body:
--
          g.esercizio, g.cd_centro_responsabilita, g.ds_cdr,
          g.cd_linea_attivita, g.denominazione, g.ds_linea_attivita,
          g.pg_progetto, g.ti_gestione, g.cd_elemento_voce,
          g.ds_elemento_voce, g.variazione,
          cal_assestato.assestato_spesa
                                 (g.esercizio,
                                  NULL,
                                  NULL,
                                  NULL,
                                  g.cd_centro_responsabilita,
                                  g.cd_elemento_voce,
                                  g.cd_linea_attivita,
                                  g.pg_variazione_pdg,
                                  g.titolo,
                                  g.categoria,
                                  NULL,
                                  NULL,
                                  NULL,
                                  NULL,
                                  NULL,
                                  NULL
                                 ) assestato_spesa,
          g.stato, g.cd_natura, g.ds_natura, g.pg_variazione_pdg,
          g.esercizio_pdg_variazione, g.cd_centro_responsabilita_clgs,
          g.ds_cdr_clgs, g.id_classificazione, g.ds_classificazione, g.titolo,
          g.categoria, modulo.cd_progetto cd_modulo,
          modulo.ds_progetto ds_modulo, modulo.livello livello_progetto
     FROM (SELECT f.esercizio, f.cd_centro_responsabilita, f.ds_cdr,
                  f.cd_linea_attivita,
                  SUBSTR (linea_attivita.denominazione, 1, 80) denominazione,
                  SUBSTR (linea_attivita.ds_linea_attivita,
                          1,
                          80
                         ) ds_linea_attivita,
                  linea_attivita.pg_progetto, f.ti_gestione,
                  f.cd_elemento_voce, f.ds_elemento_voce, f.variazione,
                  f.stato, f.cd_natura, f.ds_natura, f.pg_variazione_pdg,
                  f.esercizio_pdg_variazione, f.cd_centro_responsabilita_clgs,
                  f.ds_cdr_clgs, f.id_classificazione, f.ds_classificazione,
                  f.titolo, f.categoria
             FROM (SELECT e.esercizio, e.cd_centro_responsabilita, e.ds_cdr,
                          e.cd_linea_attivita, e.ti_gestione,
                          e.cd_elemento_voce, e.ds_elemento_voce,
                          e.variazione, e.stato, e.cd_natura, e.ds_natura,
                          e.pg_variazione_pdg, e.esercizio_pdg_variazione,
                          e.cd_centro_responsabilita_clgs, e.ds_cdr_clgs,
                          e.id_classificazione,
                          classificazione_voci.ds_classificazione,
                          classificazione_voci.cd_livello1 titolo,
                          classificazione_voci.cd_livello2 categoria
                     FROM (SELECT d.esercizio, d.cd_centro_responsabilita,
                                  d.ds_cdr, d.cd_linea_attivita,
                                  d.ti_gestione, d.cd_elemento_voce,
                                  elemento_voce.ds_elemento_voce,
                                  d.variazione, d.stato, d.cd_natura,
                                  d.ds_natura, d.pg_variazione_pdg,
                                  d.esercizio_pdg_variazione,
                                  d.cd_centro_responsabilita_clgs,
                                  d.ds_cdr_clgs,
                                  elemento_voce.id_classificazione
                             FROM (SELECT c.esercizio,
                                          c.cd_centro_responsabilita,
                                          c.ds_cdr, c.cd_linea_attivita,
                                          c.ti_gestione, c.cd_elemento_voce,
                                          c.variazione, c.stato, c.cd_natura,
                                          natura.ds_natura,
                                          c.pg_variazione_pdg,
                                          c.esercizio_pdg_variazione,
                                          c.cd_centro_responsabilita_clgs,
                                          c.ds_cdr_clgs
                                     FROM (SELECT b.esercizio,
                                                  b.cd_centro_responsabilita,
                                                  SUBSTR (cdr.ds_cdr,
                                                          1,
                                                          80
                                                         ) ds_cdr,
                                                  b.cd_linea_attivita,
                                                  b.ti_gestione,
                                                  b.cd_elemento_voce,
                                                  b.variazione, b.stato,
                                                  b.cd_natura,
                                                  b.pg_variazione_pdg,
                                                  b.esercizio_pdg_variazione,
                                                  b.cd_centro_responsabilita_clgs,
                                                  SUBSTR
                                                     (cdr_clgs.ds_cdr,
                                                      1,
                                                      80
                                                     ) ds_cdr_clgs
                                             FROM (SELECT DISTINCT a.esercizio,
                                                                   a.cd_centro_responsabilita,
                                                                   a.cd_linea_attivita,
                                                                   a.ti_gestione,
                                                                   a.cd_elemento_voce,
                                                                   a.variazione,
                                                                   e.stato,
                                                                   e.cd_natura,
                                                                   e.pg_variazione_pdg,
                                                                   e.esercizio_pdg_variazione,
                                                                   e.cd_centro_responsabilita_clgs
                                                              FROM (SELECT   pdg_preventivo_spe_det.esercizio,
                                                                             pdg_preventivo_spe_det.cd_centro_responsabilita,
                                                                             pdg_preventivo_spe_det.cd_linea_attivita,
                                                                             pdg_preventivo_spe_det.ti_appartenenza,
                                                                             pdg_preventivo_spe_det.ti_gestione,
                                                                             pdg_preventivo_spe_det.cd_elemento_voce,
                                                                             pdg_preventivo_spe_det.pg_spesa,
                                                                             NVL
                                                                                (SUM
                                                                                    (  NVL
                                                                                          (pdg_preventivo_spe_det.im_rh_ccs_costi,
                                                                                           0
                                                                                          )
                                                                                     + NVL
                                                                                          (pdg_preventivo_spe_det.im_rq_ssc_costi_odc,
                                                                                           0
                                                                                          )
                                                                                     + NVL
                                                                                          (pdg_preventivo_spe_det.im_rr_ssc_costi_odc_altra_uo,
                                                                                           0
                                                                                          )
                                                                                     + NVL
                                                                                          (pdg_preventivo_spe_det.im_rs_ssc_costi_ogc,
                                                                                           0
                                                                                          )
                                                                                     + NVL
                                                                                          (pdg_preventivo_spe_det.im_rt_ssc_costi_ogc_altra_uo,
                                                                                           0
                                                                                          )
                                                                                    ),
                                                                                 0
                                                                                )
                                                                                variazione
                                                                        FROM pdg_preventivo_spe_det,
                                                                             parametri_cnr
                                                                       WHERE pdg_preventivo_spe_det.esercizio =
                                                                                parametri_cnr.esercizio
                                                                         AND parametri_cnr.fl_regolamento_2006 =
                                                                                'N'
                                                                    GROUP BY pdg_preventivo_spe_det.esercizio,
                                                                             pdg_preventivo_spe_det.cd_centro_responsabilita,
                                                                             pdg_preventivo_spe_det.cd_linea_attivita,
                                                                             pdg_preventivo_spe_det.ti_appartenenza,
                                                                             pdg_preventivo_spe_det.ti_gestione,
                                                                             pdg_preventivo_spe_det.cd_elemento_voce,
                                                                             pdg_preventivo_spe_det.pg_spesa) a,
                                                                   pdg_preventivo_spe_det e
                                                             WHERE a.esercizio =
                                                                      e.esercizio
                                                               AND a.cd_centro_responsabilita =
                                                                      e.cd_centro_responsabilita
                                                               AND a.cd_linea_attivita =
                                                                      e.cd_linea_attivita
                                                               AND a.ti_appartenenza =
                                                                      e.ti_appartenenza
                                                               AND a.ti_gestione =
                                                                      e.ti_gestione
                                                               AND a.cd_elemento_voce =
                                                                      e.cd_elemento_voce
                                                               AND a.pg_spesa =
                                                                      e.pg_spesa
                                                               AND e.categoria_dettaglio !=
                                                                         'CAR'
                                                   UNION ALL
                                                   SELECT          -- Distinct
                                                            a.esercizio,
                                                            a.cd_cdr_assegnatario
                                                               cd_centro_responsabilita,
                                                            a.cd_linea_attivita,
                                                            a.ti_gestione,
                                                            a.cd_elemento_voce,
                                                            SUM
                                                               (  NVL
                                                                     (im_spese_gest_decentrata_int,
                                                                      0
                                                                     )
                                                                + NVL
                                                                     (im_spese_gest_decentrata_est,
                                                                      0
                                                                     )
                                                                + NVL
                                                                     (im_spese_gest_accentrata_int,
                                                                      0
                                                                     )
                                                                + NVL
                                                                     (im_spese_gest_accentrata_est,
                                                                      0
                                                                     )
                                                               ) variazione,
                                                            var.stato,
                                                            la.cd_natura,
                                                            a.pg_variazione_pdg,
                                                            a.esercizio
                                                               esercizio_pdg_variazione,
                                                            a.cd_cdr_assegnatario_clgs
                                                               cd_centro_responsabilita_clgs
                                                       FROM pdg_variazione_riga_gest a,
                                                            parametri_cnr,
                                                            pdg_variazione var,
                                                            linea_attivita la
                                                      WHERE a.esercizio =
                                                               parametri_cnr.esercizio
                                                        AND a.ti_gestione =
                                                                           'S'
                                                        AND a.categoria_dettaglio !=
                                                                         'SCR'
                                                        AND parametri_cnr.fl_regolamento_2006 =
                                                                           'Y'
                                                        AND a.esercizio =
                                                                 var.esercizio
                                                        AND a.pg_variazione_pdg =
                                                               var.pg_variazione_pdg
                                                        AND a.cd_cdr_assegnatario =
                                                               la.cd_centro_responsabilita
                                                        AND a.cd_linea_attivita =
                                                               la.cd_linea_attivita
                                                   GROUP BY a.esercizio,
                                                            a.cd_cdr_assegnatario,
                                                            a.cd_linea_attivita,
                                                            a.ti_gestione,
                                                            a.cd_elemento_voce,
                                                            var.stato,
                                                            la.cd_natura,
                                                            a.pg_variazione_pdg,
                                                            a.esercizio,
                                                            a.cd_cdr_assegnatario_clgs) b,
                                                  cdr,
                                                  cdr cdr_clgs
                                            WHERE b.cd_centro_responsabilita =
                                                     cdr.cd_centro_responsabilita
                                              AND b.cd_centro_responsabilita_clgs =
                                                                           cdr_clgs.cd_centro_responsabilita(+)) c,
                                          natura
                                    WHERE c.cd_natura = natura.cd_natura) d,
                                  elemento_voce
                            WHERE d.cd_elemento_voce =
                                                elemento_voce.cd_elemento_voce
                              AND d.esercizio_pdg_variazione =
                                                       elemento_voce.esercizio
                              AND elemento_voce.ti_gestione = 'S') e,
                          classificazione_voci
                    WHERE e.id_classificazione =
                                       classificazione_voci.id_classificazione) f,
                  v_linea_attivita_valida linea_attivita
            WHERE f.esercizio = linea_attivita.esercizio
              AND f.cd_linea_attivita = linea_attivita.cd_linea_attivita
              AND f.cd_centro_responsabilita =
                                       linea_attivita.cd_centro_responsabilita) g,
          progetto_gest modulo
    WHERE g.pg_progetto = modulo.pg_progetto(+)
      AND (modulo.esercizio IS NULL OR modulo.esercizio = g.esercizio) ;
