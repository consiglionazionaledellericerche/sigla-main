--------------------------------------------------------
--  DDL for View V_CONTRATTI_TOTALI
--------------------------------------------------------

CREATE OR REPLACE FORCE VIEW v_contratti_totali (tipo,
                                                 terzo,
                                                 esercizio_contratto,
                                                 pg_contratto,
                                                 stato_contratto,
                                                 oggetto,
                                                 data_inizio,
                                                 data_fine,
                                                 tipo_contratto,
                                                 unita_organizzativa,
                                                 pg_progetto_contratto,
                                                 totale_entrate_contratto,
                                                 totale_spese_contratto,
                                                 totale_entrate,
                                                 totale_spese,
                                                 liquidato_entrate,
                                                 liquidato_spese,
                                                 totale_reversali,
                                                 totale_mandati
                                                )
AS
   SELECT   a.tipo, a.cd_terzo, a.esercizio_contratto, a.pg_contratto,
            a.stato_contratto, b.oggetto, b.dt_inizio_validita,
            b.dt_fine_validita, b.cd_tipo_contratto, a.cd_unita_organizzativa,
            b.pg_progetto, b.im_contratto_attivo, b.im_contratto_passivo,
            SUM (a.totale_entrate), SUM (a.totale_spese),
            SUM (a.liquidato_entrate), SUM (a.liquidato_spese),
            SUM (a.totale_reversale), SUM (a.totale_mandati)
       FROM (SELECT 'ETR' tipo, accertamento.cd_terzo,
                    accertamento.esercizio_contratto,
                    accertamento.pg_contratto, accertamento.stato_contratto,
                    accertamento.cd_uo_origine cd_unita_organizzativa,
                    accertamento_scad_voce.im_voce totale_entrate,
                    0 totale_spese, 0 liquidato_entrate, 0 liquidato_spese,
                    0 totale_reversale, 0 totale_mandati
               FROM accertamento, accertamento_scad_voce
              WHERE accertamento.esercizio_contratto IS NOT NULL
                AND accertamento.pg_contratto IS NOT NULL
                AND accertamento.cd_cds = accertamento_scad_voce.cd_cds
                AND accertamento.esercizio = accertamento_scad_voce.esercizio
                AND accertamento.esercizio_originale =
                                    accertamento_scad_voce.esercizio_originale
                AND accertamento.pg_accertamento =
                                        accertamento_scad_voce.pg_accertamento
                AND (   accertamento.esercizio_originale =
                                                        accertamento.esercizio
                     OR (    accertamento.cd_tipo_documento_cont = 'ACR_RES'
                         AND NOT EXISTS (
                                SELECT 1
                                  FROM accertamento acc
                                 WHERE accertamento.cd_cds = acc.cd_cds
                                   AND accertamento.esercizio > acc.esercizio
                                   AND accertamento.esercizio_originale =
                                                       acc.esercizio_originale
                                   AND accertamento.pg_accertamento =
                                                           acc.pg_accertamento
                                   AND accertamento.esercizio_contratto =
                                                       acc.esercizio_contratto
                                   AND accertamento.stato_contratto =
                                                           acc.stato_contratto
                                   AND accertamento.pg_contratto =
                                                              acc.pg_contratto)
                        )
                    )
             UNION ALL
             SELECT 'ETR' tipo, accertamento.cd_terzo,
                    accertamento.esercizio_contratto,
                    accertamento.pg_contratto, accertamento.stato_contratto,
                    accertamento.cd_uo_origine,
                    accertamento_mod_voce.im_modifica totale_entrate,
                    0 totale_spese, 0 liquidato_entrate, 0 liquidato_spese,
                    0 totale_reversale, 0 totale_mandati
               FROM accertamento, accertamento_modifica,
                    accertamento_mod_voce
              WHERE accertamento.esercizio_contratto IS NOT NULL
                AND accertamento.pg_contratto IS NOT NULL
                AND accertamento.cd_cds = accertamento_modifica.cd_cds
                AND accertamento.esercizio = accertamento_modifica.esercizio
                AND accertamento.esercizio_originale =
                                     accertamento_modifica.esercizio_originale
                AND accertamento.pg_accertamento =
                                         accertamento_modifica.pg_accertamento
                AND accertamento_modifica.cd_cds =
                                                  accertamento_mod_voce.cd_cds
                AND accertamento_modifica.esercizio =
                                               accertamento_mod_voce.esercizio
                AND accertamento_modifica.pg_modifica =
                                             accertamento_mod_voce.pg_modifica
                AND EXISTS (
                       SELECT 1
                         FROM accertamento acc
                        WHERE accertamento.cd_cds = acc.cd_cds
                          AND accertamento.esercizio_originale =
                                                       acc.esercizio_originale
                          AND accertamento.pg_accertamento =
                                                           acc.pg_accertamento
                          AND accertamento.esercizio_contratto =
                                                       acc.esercizio_contratto
                          AND accertamento.stato_contratto =
                                                           acc.stato_contratto
                          AND accertamento.pg_contratto = acc.pg_contratto
                          AND acc.esercizio = acc.esercizio_originale)
             UNION ALL
             SELECT 'ETR' tipo, accertamento.cd_terzo,
                    accertamento.esercizio_contratto,
                    accertamento.pg_contratto, accertamento.stato_contratto,
                    accertamento.cd_uo_origine, 0 totale_entrate,
                    0 totale_spese, 0 liquidato_entrate, 0 liquidato_spese,
                    DECODE
                       (NVL (accertamento_scadenzario.im_scadenza, 0),
                        0, 0,
                          (  accertamento_scad_voce.im_voce
                           / accertamento_scadenzario.im_scadenza
                          )
                        * riga.im_reversale_riga
                       ) totale_reversale,
                    0 totale_mandati
               FROM accertamento,
                    accertamento_scadenzario,
                    accertamento_scad_voce,
                    reversale_riga riga
              WHERE accertamento.esercizio_contratto IS NOT NULL
                AND accertamento.stato_contratto IS NOT NULL
                AND accertamento.pg_contratto IS NOT NULL
                AND accertamento.cd_cds = accertamento_scadenzario.cd_cds
                AND accertamento.esercizio =
                                            accertamento_scadenzario.esercizio
                AND accertamento.esercizio_originale =
                                  accertamento_scadenzario.esercizio_originale
                AND accertamento.pg_accertamento =
                                      accertamento_scadenzario.pg_accertamento
                AND accertamento_scadenzario.cd_cds = riga.cd_cds
                AND accertamento_scadenzario.esercizio =
                                                   riga.esercizio_accertamento
                AND accertamento_scadenzario.esercizio_originale =
                                               riga.esercizio_ori_accertamento
                AND accertamento_scadenzario.pg_accertamento =
                                                          riga.pg_accertamento
                AND accertamento_scadenzario.pg_accertamento_scadenzario =
                                              riga.pg_accertamento_scadenzario
                AND riga.stato <> 'A'
                AND accertamento_scadenzario.cd_cds =
                                                 accertamento_scad_voce.cd_cds
                AND accertamento_scadenzario.esercizio =
                                              accertamento_scad_voce.esercizio
                AND accertamento_scadenzario.esercizio_originale =
                                    accertamento_scad_voce.esercizio_originale
                AND accertamento_scadenzario.pg_accertamento =
                                        accertamento_scad_voce.pg_accertamento
                AND accertamento_scadenzario.pg_accertamento_scadenzario =
                            accertamento_scad_voce.pg_accertamento_scadenzario
             UNION ALL
             SELECT 'ETR' tipo, accertamento.cd_terzo,
                    accertamento.esercizio_contratto,
                    accertamento.pg_contratto, accertamento.stato_contratto,
                    accertamento.cd_uo_origine, 0 totale_entrate,
                    0 totale_spese, v.im_totale_doc_amm liquidato_entrate,
                    0 liquidato_spese, 0 totale_reversale, 0 totale_mandati
               FROM accertamento, accertamento_scadenzario, v_doc_attivo v
              WHERE accertamento.esercizio_contratto IS NOT NULL
                AND accertamento.stato_contratto IS NOT NULL
                AND accertamento.pg_contratto IS NOT NULL
                AND accertamento.cd_cds = accertamento_scadenzario.cd_cds
                AND accertamento.esercizio =
                                            accertamento_scadenzario.esercizio
                AND accertamento.esercizio_originale =
                                  accertamento_scadenzario.esercizio_originale
                AND accertamento.pg_accertamento =
                                      accertamento_scadenzario.pg_accertamento
                AND accertamento_scadenzario.cd_cds = v.cd_cds_accertamento
                AND accertamento_scadenzario.esercizio =
                                                      v.esercizio_accertamento
                AND accertamento_scadenzario.esercizio_originale =
                                                  v.esercizio_ori_accertamento
                AND accertamento_scadenzario.pg_accertamento =
                                                             v.pg_accertamento
                AND accertamento_scadenzario.pg_accertamento_scadenzario =
                                                 v.pg_accertamento_scadenzario
             UNION ALL
             SELECT 'SPE' tipo, obbligazione.cd_terzo,
                    obbligazione.esercizio_contratto,
                    obbligazione.pg_contratto, obbligazione.stato_contratto,
                    obbligazione.cd_uo_origine, 0 totale_entrate,
                    obbligazione_scad_voce.im_voce totale_spese,
                    0 liquidato_entrate, 0 liquidato_spese,
                    0 totale_reversale, 0 totale_mandati
               FROM obbligazione, obbligazione_scad_voce
              WHERE obbligazione.esercizio_contratto IS NOT NULL
                AND obbligazione.stato_contratto IS NOT NULL
                AND obbligazione.pg_contratto IS NOT NULL
                AND obbligazione.cd_cds = obbligazione_scad_voce.cd_cds
                AND obbligazione.esercizio = obbligazione_scad_voce.esercizio
                AND obbligazione.esercizio_originale =
                                    obbligazione_scad_voce.esercizio_originale
                AND obbligazione.pg_obbligazione =
                                        obbligazione_scad_voce.pg_obbligazione
                AND (   obbligazione.esercizio_originale =
                                                        obbligazione.esercizio
                     OR obbligazione.cd_tipo_documento_cont = 'OBB_RESIM'
                     OR (    obbligazione.cd_tipo_documento_cont = 'OBB_RES'
                         AND NOT EXISTS (
                                SELECT 1
                                  FROM obbligazione obb
                                 WHERE obbligazione.cd_cds = obb.cd_cds
                                   AND obbligazione.esercizio > obb.esercizio
                                   AND obbligazione.esercizio_originale =
                                                       obb.esercizio_originale
                                   AND obbligazione.pg_obbligazione =
                                                           obb.pg_obbligazione
                                   AND obbligazione.esercizio_contratto =
                                                       obb.esercizio_contratto
                                   AND obbligazione.stato_contratto =
                                                           obb.stato_contratto
                                   AND obbligazione.pg_contratto =
                                                              obb.pg_contratto)
                        )
                    )
             UNION ALL
             SELECT 'SPE' tipo, obbligazione.cd_terzo,
                    obbligazione.esercizio_contratto,
                    obbligazione.pg_contratto, obbligazione.stato_contratto,
                    obbligazione.cd_uo_origine, 0 totale_entrate,
                    obbligazione_mod_voce.im_modifica totale_spese,
                    0 liquidato_entrate, 0 liquidato_spese,
                    0 totale_reversale, 0 totale_mandati
               FROM obbligazione, obbligazione_modifica,
                    obbligazione_mod_voce
              WHERE obbligazione.esercizio_contratto IS NOT NULL
                AND obbligazione.stato_contratto IS NOT NULL
                AND obbligazione.pg_contratto IS NOT NULL
                AND obbligazione.cd_cds = obbligazione_modifica.cd_cds
                AND obbligazione.esercizio = obbligazione_modifica.esercizio
                AND obbligazione.esercizio_originale =
                                     obbligazione_modifica.esercizio_originale
                AND obbligazione.pg_obbligazione =
                                         obbligazione_modifica.pg_obbligazione
                AND obbligazione_modifica.cd_cds =
                                                  obbligazione_mod_voce.cd_cds
                AND obbligazione_modifica.esercizio =
                                               obbligazione_mod_voce.esercizio
                AND obbligazione_modifica.pg_modifica =
                                             obbligazione_mod_voce.pg_modifica
                -- per evitare che prende le variazioni su impegni già diminuiti nell'importo sull'obbligazione collegato al contratto
                AND EXISTS (
                       SELECT 1
                         FROM obbligazione obb
                        WHERE obbligazione.cd_cds = obb.cd_cds
                          AND obbligazione.esercizio_originale =
                                                       obb.esercizio_originale
                          AND obbligazione.pg_obbligazione =
                                                           obb.pg_obbligazione
                          AND obbligazione.esercizio_contratto =
                                                       obb.esercizio_contratto
                          AND obbligazione.stato_contratto =
                                                           obb.stato_contratto
                          AND obbligazione.pg_contratto = obb.pg_contratto
                          AND (   obb.esercizio = obb.esercizio_originale
                               OR obb.cd_tipo_documento_cont = 'OBB_RESIM'
                              ))
             UNION ALL
             SELECT 'SPE' tipo, obbligazione.cd_terzo,
                    obbligazione.esercizio_contratto,
                    obbligazione.pg_contratto, obbligazione.stato_contratto,
                    obbligazione.cd_uo_origine, 0 totale_entrate,
                    0 totale_spese, 0 liquidato_entrate, 0 liquidato_spese,
                    0 totale_reversale,
                    DECODE
                       (NVL (obbligazione_scadenzario.im_scadenza, 0),
                        0, 0,
                          (  obbligazione_scad_voce.im_voce
                           / obbligazione_scadenzario.im_scadenza
                          )
                        * riga.im_mandato_riga
                       ) totale_mandati
               FROM obbligazione,
                    obbligazione_scadenzario,
                    obbligazione_scad_voce,
                    mandato_riga riga
              WHERE obbligazione.esercizio_contratto IS NOT NULL
                AND obbligazione.stato_contratto IS NOT NULL
                AND obbligazione.pg_contratto IS NOT NULL
                AND obbligazione.cd_cds = obbligazione_scadenzario.cd_cds
                AND obbligazione.esercizio =
                                            obbligazione_scadenzario.esercizio
                AND obbligazione.esercizio_originale =
                                  obbligazione_scadenzario.esercizio_originale
                AND obbligazione.pg_obbligazione =
                                      obbligazione_scadenzario.pg_obbligazione
                AND obbligazione_scadenzario.cd_cds = riga.cd_cds
                AND obbligazione_scadenzario.esercizio =
                                                   riga.esercizio_obbligazione
                AND obbligazione_scadenzario.esercizio_originale =
                                               riga.esercizio_ori_obbligazione
                AND obbligazione_scadenzario.pg_obbligazione =
                                                          riga.pg_obbligazione
                AND obbligazione_scadenzario.pg_obbligazione_scadenzario =
                                              riga.pg_obbligazione_scadenzario
                AND riga.stato <> 'A'
                AND obbligazione_scadenzario.cd_cds =
                                                 obbligazione_scad_voce.cd_cds
                AND obbligazione_scadenzario.esercizio =
                                              obbligazione_scad_voce.esercizio
                AND obbligazione_scadenzario.esercizio_originale =
                                    obbligazione_scad_voce.esercizio_originale
                AND obbligazione_scadenzario.pg_obbligazione =
                                        obbligazione_scad_voce.pg_obbligazione
                AND obbligazione_scadenzario.pg_obbligazione_scadenzario =
                            obbligazione_scad_voce.pg_obbligazione_scadenzario
             UNION ALL
             SELECT 'SPE' tipo, obbligazione.cd_terzo,
                    obbligazione.esercizio_contratto,
                    obbligazione.pg_contratto, obbligazione.stato_contratto,
                    obbligazione.cd_uo_origine, 0 totale_entrate,
                    0 totale_spese, 0 liquidato_entrate,
                    v.im_totale_doc_amm liquidato_spese, 0 totale_reversale,
                    0 totale_mandati
               FROM obbligazione, obbligazione_scadenzario, v_doc_passivo v
              WHERE obbligazione.esercizio_contratto IS NOT NULL
                AND obbligazione.stato_contratto IS NOT NULL
                AND obbligazione.pg_contratto IS NOT NULL
                AND obbligazione.cd_cds = obbligazione_scadenzario.cd_cds
                AND obbligazione.esercizio =
                                            obbligazione_scadenzario.esercizio
                AND obbligazione.esercizio_originale =
                                  obbligazione_scadenzario.esercizio_originale
                AND obbligazione.pg_obbligazione =
                                      obbligazione_scadenzario.pg_obbligazione
                AND obbligazione_scadenzario.cd_cds = v.cd_cds_obbligazione
                AND obbligazione_scadenzario.esercizio =
                                                      v.esercizio_obbligazione
                AND obbligazione_scadenzario.esercizio_originale =
                                                  v.esercizio_ori_obbligazione
                AND obbligazione_scadenzario.pg_obbligazione =
                                                             v.pg_obbligazione
                AND obbligazione_scadenzario.pg_obbligazione_scadenzario =
                                                 v.pg_obbligazione_scadenzario) a,
            contratto b
      WHERE a.esercizio_contratto = b.esercizio
        AND a.stato_contratto = b.stato
        AND a.pg_contratto = b.pg_contratto
   GROUP BY a.tipo,
            a.cd_terzo,
            a.esercizio_contratto,
            a.pg_contratto,
            a.stato_contratto,
            b.oggetto,
            b.dt_inizio_validita,
            b.dt_fine_validita,
            b.cd_tipo_contratto,
            b.im_contratto_attivo,
            b.im_contratto_passivo,
            a.cd_unita_organizzativa,
            b.pg_progetto
   UNION ALL
   SELECT 'ETR' tipo, contratto.fig_giur_est, contratto.esercizio,
          contratto.pg_contratto, contratto.stato, contratto.oggetto,
          contratto.dt_inizio_validita, contratto.dt_fine_validita,
          contratto.cd_tipo_contratto, contratto.cd_unita_organizzativa,
          contratto.pg_progetto, contratto.im_contratto_attivo,
          contratto.im_contratto_passivo, 0 totale_entrate, 0 totale_spese,
          0 liquidato_entrate, 0 liquidato_spese, 0 totale_reversale,
          0 totale_mandati
     FROM contratto
    WHERE stato = 'D'
      AND natura_contabile IN ('E', 'A')
      AND NOT EXISTS (
             SELECT 1
               FROM accertamento
              WHERE accertamento.esercizio_contratto = contratto.esercizio
                AND accertamento.stato_contratto = contratto.stato
                AND accertamento.pg_contratto = contratto.pg_contratto)
   UNION ALL
   SELECT 'SPE' tipo, contratto.fig_giur_est, contratto.esercizio,
          contratto.pg_contratto, contratto.stato, contratto.oggetto,
          contratto.dt_inizio_validita, contratto.dt_fine_validita,
          contratto.cd_tipo_contratto, contratto.cd_unita_organizzativa,
          contratto.pg_progetto, contratto.im_contratto_attivo,
          contratto.im_contratto_passivo, 0 totale_entrate, 0 totale_spese,
          0 liquidato_entrate, 0 liquidato_spese, 0 totale_reversale,
          0 totale_mandati
     FROM contratto
    WHERE stato = 'D'
      AND natura_contabile IN ('E', 'P')
      AND NOT EXISTS (
             SELECT 1
               FROM obbligazione
              WHERE obbligazione.esercizio_contratto = contratto.esercizio
                AND obbligazione.stato_contratto = contratto.stato
                AND obbligazione.pg_contratto = contratto.pg_contratto);
