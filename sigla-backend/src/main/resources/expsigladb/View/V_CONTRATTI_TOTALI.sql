--------------------------------------------------------
--  DDL for View V_CONTRATTI_TOTALI
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_CONTRATTI_TOTALI" ("TIPO", "TERZO", "ESERCIZIO_CONTRATTO", "PG_CONTRATTO", "STATO_CONTRATTO", "OGGETTO", "DATA_INIZIO", "DATA_FINE", "TIPO_CONTRATTO", "TOTALE_ENTRATE_CONTRATTO", "TOTALE_SPESE_CONTRATTO", "TOTALE_ENTRATE", "TOTALE_SPESE", "LIQUIDATO_ENTRATE", "LIQUIDATO_SPESE", "TOTALE_REVERSALI", "TOTALE_MANDATI", "UNITA_ORGANIZZATIVA") AS 
  SELECT   a.tipo, --a.esercizio_originale,
   a.cd_terzo, --a.cd_elemento_voce,
            a.esercizio_contratto, a.pg_contratto, a.stato_contratto,
            a.oggetto, a.dt_inizio_validita, a.dt_fine_validita,
            --a.cd_linea_attivita, a.cd_centro_responsabilita,
            a.cd_tipo_contratto, a.im_contratto_attivo,
            a.im_contratto_passivo, SUM (a.totale_entrate),
            SUM (a.totale_spese), SUM (a.liquidato_entrate),
            SUM (a.liquidato_spese), SUM (a.totale_reversale),
            SUM (a.totale_mandati), a.cd_unita_organizzativa
       FROM (SELECT   'ETR' tipo,-- accertamento.esercizio_originale,
                      accertamento.cd_terzo, --accertamento.cd_elemento_voce,
                      accertamento.esercizio_contratto,
                      accertamento.pg_contratto, accertamento.stato_contratto,
                      contratto.oggetto, contratto.dt_inizio_validita,
                      contratto.dt_fine_validita,
                      --accertamento_scad_voce.cd_linea_attivita,
                      --accertamento_scad_voce.cd_centro_responsabilita,
                      contratto.cd_tipo_contratto,
                      contratto.im_contratto_attivo,
                      contratto.im_contratto_passivo,
                      SUM (accertamento_scad_voce.im_voce) totale_entrate,
                      0 totale_spese, 0 liquidato_entrate, 0 liquidato_spese,
                      0 totale_reversale, 0 totale_mandati,accertamento.cd_uo_origine cd_unita_organizzativa
                 FROM contratto,
                      accertamento,
                      accertamento_scadenzario,
                      accertamento_scad_voce
                WHERE contratto.esercizio = accertamento.esercizio_contratto
                  AND contratto.pg_contratto = accertamento.pg_contratto
                  AND accertamento.cd_cds = accertamento_scadenzario.cd_cds
                  AND accertamento.esercizio =
                                            accertamento_scadenzario.esercizio
                  	AND (accertamento.esercizio_originale =accertamento.esercizio
                       or (accertamento.cd_tipo_documento_cont ='ACC_RES' and
                       not exists (select 1 from accertamento acc where
                		accertamento.CD_CDS = acc.CD_CDS
             			And accertamento.ESERCIZIO > acc.ESERCIZIO
             			And accertamento.ESERCIZIO_ORIGINALE = acc.ESERCIZIO_ORIGINALE
             			And accertamento.PG_accertamento = acc.PG_accertamento
             			and accertamento.ESERCIZIO_contratto = acc.ESERCIZIO_CONTRATTO
             			And accertamento.STATO_CONTRATTO = acc.STATO_CONTRATTO
             			And accertamento.PG_CONTRATTO = acc.PG_CONTRATTO)))
                  AND accertamento.esercizio_originale =
                                  accertamento_scadenzario.esercizio_originale
                  AND accertamento.pg_accertamento =
                                      accertamento_scadenzario.pg_accertamento
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
             GROUP BY 'ETR',
                      --accertamento.esercizio_originale,
                      accertamento.cd_terzo,
                      --accertamento.cd_elemento_voce,
                      accertamento.esercizio_contratto,
                      accertamento.pg_contratto,
                      accertamento.stato_contratto,
                      contratto.oggetto,
                      contratto.dt_inizio_validita,
                      contratto.dt_fine_validita,
                      --accertamento_scad_voce.cd_linea_attivita,
                      --accertamento_scad_voce.cd_centro_responsabilita,
                      contratto.cd_tipo_contratto,
                      contratto.im_contratto_attivo,
                      contratto.im_contratto_passivo,
                      accertamento.cd_uo_origine
             UNION
             SELECT   'ETR' tipo,-- accertamento.esercizio_originale,
                      accertamento.cd_terzo, --accertamento.cd_elemento_voce,
                      accertamento.esercizio_contratto,
                      accertamento.pg_contratto, accertamento.stato_contratto,
                      contratto.oggetto, contratto.dt_inizio_validita,
                      contratto.dt_fine_validita,
                      --accertamento_scad_voce.cd_linea_attivita,
                      --accertamento_scad_voce.cd_centro_responsabilita,
                      contratto.cd_tipo_contratto,
                      contratto.im_contratto_attivo,
                      contratto.im_contratto_passivo,
                      SUM (accertamento_mod_voce.IM_MODIFICA) totale_entrate,
                      0 totale_spese, 0 liquidato_entrate, 0 liquidato_spese,
                      0 totale_reversale, 0 totale_mandati,accertamento.cd_uo_origine cd_unita_organizzativa
                 FROM contratto,
                      accertamento,
                      accertamento_modifica,
                      accertamento_mod_voce
                WHERE contratto.esercizio = accertamento.esercizio_contratto
                  AND contratto.pg_contratto = accertamento.pg_contratto
                  and accertamento.cd_cds = accertamento_modifica.cd_cds
        					AND accertamento.esercizio = accertamento_modifica.esercizio
        					AND accertamento.esercizio_originale = accertamento_modifica.esercizio_originale
				          AND accertamento.pg_accertamento =  accertamento_modifica.pg_accertamento
                  AND accertamento_modifica.cd_cds = accertamento_mod_voce.cd_cds
                  AND accertamento_modifica.esercizio = accertamento_mod_voce.esercizio
                  AND accertamento_modifica.pg_modifica = accertamento_mod_voce.pg_modifica
                  AND exists(select 1 from ACCERTAMENTO acc where
                		ACCERTAMENTO.CD_CDS = acc.CD_CDS
             			--And ACCERTAMENTO.ESERCIZIO = acc.ESERCIZIO
	             			And ACCERTAMENTO.ESERCIZIO_ORIGINALE = acc.ESERCIZIO_ORIGINALE
  	           			And ACCERTAMENTO.pg_accertamento = acc.pg_accertamento
             				and ACCERTAMENTO.ESERCIZIO_contratto = acc.ESERCIZIO_CONTRATTO
             				And ACCERTAMENTO.STATO_contratto = acc.STATO_CONTRATTO
             				And ACCERTAMENTO.PG_CONTRATTO = acc.PG_CONTRATTO
             				and acc.ESERCIZIO = acc.ESERCIZIO_ORIGINALE )
             GROUP BY 'ETR',
                      --accertamento.esercizio_originale,
                      accertamento.cd_terzo,
                      --accertamento.cd_elemento_voce,
                      accertamento.esercizio_contratto,
                      accertamento.pg_contratto,
                      accertamento.stato_contratto,
                      contratto.oggetto,
                      contratto.dt_inizio_validita,
                      contratto.dt_fine_validita,
                      --accertamento_scad_voce.cd_linea_attivita,
                      --accertamento_scad_voce.cd_centro_responsabilita,
                      contratto.cd_tipo_contratto,
                      contratto.im_contratto_attivo,
                      contratto.im_contratto_passivo,
                      accertamento.cd_uo_origine
             UNION
             SELECT   'ETR' tipo, --accertamento.esercizio_originale,
                      accertamento.cd_terzo, --accertamento.cd_elemento_voce,
                      accertamento.esercizio_contratto,
                      accertamento.pg_contratto, accertamento.stato_contratto,
                      contratto.oggetto, contratto.dt_inizio_validita,
                      contratto.dt_fine_validita,
                      --accertamento_scad_voce.cd_linea_attivita,
                      --accertamento_scad_voce.cd_centro_responsabilita,
                      contratto.cd_tipo_contratto,
                      contratto.im_contratto_attivo,
                      contratto.im_contratto_passivo, 0 totale_entrate,
                      0 totale_spese, 0 liquidato_entrate, 0 liquidato_spese,
                      SUM
                         (DECODE (NVL (accertamento_scadenzario.im_scadenza,
                                       0),
                                  0, 0,
                                    (  accertamento_scad_voce.im_voce
                                     / accertamento_scadenzario.im_scadenza
                                    )
                                  * riga.im_reversale_riga
                                 )
                         ) totale_reversale,
                      0 totale_mandati,accertamento.cd_uo_origine cd_unita_organizzativa
                 FROM contratto,
                      accertamento,
                      accertamento_scadenzario,
                      accertamento_scad_voce,
                      reversale_riga riga
                WHERE contratto.esercizio = accertamento.esercizio_contratto
                  AND contratto.stato = accertamento.stato_contratto
                  AND contratto.pg_contratto = accertamento.pg_contratto
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
                  AND accertamento.cd_cds = accertamento_scadenzario.cd_cds
                  AND accertamento.esercizio =
                                            accertamento_scadenzario.esercizio
                  AND accertamento.esercizio_originale =
                                  accertamento_scadenzario.esercizio_originale
                  AND accertamento.pg_accertamento =
                                      accertamento_scadenzario.pg_accertamento
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
             GROUP BY 'ETR',
                     -- accertamento.esercizio_originale,
                      accertamento.cd_terzo,
                      --accertamento.cd_elemento_voce,
                      accertamento.esercizio_contratto,
                      accertamento.pg_contratto,
                      accertamento.stato_contratto,
                      contratto.oggetto,
                      contratto.dt_inizio_validita,
                      contratto.dt_fine_validita,
                      --accertamento_scad_voce.cd_linea_attivita,
                      --accertamento_scad_voce.cd_centro_responsabilita,
                      contratto.cd_tipo_contratto,
                      contratto.im_contratto_attivo,
                      contratto.im_contratto_passivo,accertamento.cd_uo_origine
             UNION
             SELECT   'ETR' tipo, --accertamento.esercizio_originale,
                      accertamento.cd_terzo, --accertamento.cd_elemento_voce,
                      accertamento.esercizio_contratto,
                      accertamento.pg_contratto, accertamento.stato_contratto,
                      contratto.oggetto, contratto.dt_inizio_validita,
                      contratto.dt_fine_validita,
                      --accertamento_scad_voce.cd_linea_attivita,
                      --accertamento_scad_voce.cd_centro_responsabilita,
                      contratto.cd_tipo_contratto,
                      contratto.im_contratto_attivo,
                      contratto.im_contratto_passivo, 0 totale_entrate,
                      0 totale_spese,
                      SUM (v.im_totale_doc_amm) liquidato_entrate,
                      0 liquidato_spese, 0 totale_reversale, 0 totale_mandati,accertamento.cd_uo_origine cd_unita_organizzativa
                 FROM contratto,
                      accertamento,
                      accertamento_scadenzario,
                      accertamento_scad_voce,
                      v_doc_attivo v
                WHERE contratto.esercizio = accertamento.esercizio_contratto
                  AND contratto.stato = accertamento.stato_contratto
                  AND contratto.pg_contratto = accertamento.pg_contratto
                  AND accertamento_scadenzario.cd_cds = v.cd_cds_accertamento
                  AND accertamento_scadenzario.esercizio =
                                                      v.esercizio_accertamento
                  AND accertamento_scadenzario.esercizio_originale =
                                                  v.esercizio_ori_accertamento
                  AND accertamento_scadenzario.pg_accertamento =
                                                             v.pg_accertamento
                  AND accertamento_scadenzario.pg_accertamento_scadenzario =
                                                 v.pg_accertamento_scadenzario
                  AND accertamento.cd_cds = accertamento_scadenzario.cd_cds
                  AND accertamento.esercizio =
                                            accertamento_scadenzario.esercizio
                  AND accertamento.esercizio_originale =
                                  accertamento_scadenzario.esercizio_originale
                  AND accertamento.pg_accertamento =
                                      accertamento_scadenzario.pg_accertamento
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
             GROUP BY 'ETR',
                      --accertamento.esercizio_originale,
                      accertamento.cd_terzo,
                      --accertamento.cd_elemento_voce,
                      accertamento.esercizio_contratto,
                      accertamento.pg_contratto,
                      accertamento.stato_contratto,
                      contratto.oggetto,
                      contratto.dt_inizio_validita,
                      contratto.dt_fine_validita,
                      --accertamento_scad_voce.cd_linea_attivita,
                      --accertamento_scad_voce.cd_centro_responsabilita,
                      contratto.cd_tipo_contratto,
                      contratto.im_contratto_attivo,
                      contratto.im_contratto_passivo,accertamento.cd_uo_origine
             UNION
             SELECT   'SPE' tipo, --obbligazione.esercizio_originale,
                      obbligazione.cd_terzo, --obbligazione.cd_elemento_voce,
                      obbligazione.esercizio_contratto,
                      obbligazione.pg_contratto, obbligazione.stato_contratto,
                      contratto.oggetto, contratto.dt_inizio_validita,
                      contratto.dt_fine_validita,
                      --obbligazione_scad_voce.cd_linea_attivita,
                      --obbligazione_scad_voce.cd_centro_responsabilita,
                      contratto.cd_tipo_contratto,
                      contratto.im_contratto_attivo,
                      contratto.im_contratto_passivo, 0 totale_entrate,
                      SUM (obbligazione_scad_voce.im_voce) totale_spese,
                      0 liquidato_entrate, 0 liquidato_spese,
                      0 totale_reversale, 0 totale_mandati,obbligazione.cd_uo_origine cd_unita_organizzativa
                 FROM contratto,
                      obbligazione,
                      obbligazione_scadenzario,
                      obbligazione_scad_voce
                WHERE contratto.esercizio = obbligazione.esercizio_contratto
                  AND contratto.stato = obbligazione.stato_contratto
                  AND contratto.pg_contratto = obbligazione.pg_contratto
                  AND obbligazione.cd_cds = obbligazione_scadenzario.cd_cds
                  AND obbligazione.esercizio =
                                            obbligazione_scadenzario.esercizio
                 AND (obbligazione.esercizio_originale =obbligazione.esercizio
                       or obbligazione.cd_tipo_documento_cont ='OBB_RESIM'
                       or (obbligazione.cd_tipo_documento_cont ='OBB_RES' and
                       not exists (select 1 from obbligazione obb where
                		OBBLIGAZIONE.CD_CDS = obb.CD_CDS
             			And OBBLIGAZIONE.ESERCIZIO > obb.ESERCIZIO
             			And OBBLIGAZIONE.ESERCIZIO_ORIGINALE = obb.ESERCIZIO_ORIGINALE
             			And OBBLIGAZIONE.PG_OBBLIGAZIONE = obb.PG_OBBLIGAZIONE
             			and OBBLIGAZIONE.ESERCIZIO_contratto = obb.ESERCIZIO_CONTRATTO
             			And OBBLIGAZIONE.STATO_CONTRATTO = obb.STATO_CONTRATTO
             			And OBBLIGAZIONE.PG_CONTRATTO = obb.PG_CONTRATTO)))
                  AND obbligazione.esercizio_originale =
                                  obbligazione_scadenzario.esercizio_originale
                  AND obbligazione.pg_obbligazione =
                                      obbligazione_scadenzario.pg_obbligazione
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
             GROUP BY 'SPE',
                      --obbligazione.esercizio_originale,
                      obbligazione.cd_terzo,
                      --obbligazione.cd_elemento_voce,
                      obbligazione.esercizio_contratto,
                      obbligazione.pg_contratto,
                      obbligazione.stato_contratto,
                      contratto.oggetto,
                      contratto.dt_inizio_validita,
                      contratto.dt_fine_validita,
                      --obbligazione_scad_voce.cd_linea_attivita,
                      --obbligazione_scad_voce.cd_centro_responsabilita,
                      contratto.cd_tipo_contratto,
                      contratto.im_contratto_attivo,
                      contratto.im_contratto_passivo,
                      obbligazione.cd_uo_origine
						 UNION
						 SELECT   'SPE' tipo, --obbligazione.esercizio_originale,
                      obbligazione.cd_terzo, --obbligazione.cd_elemento_voce,
                      obbligazione.esercizio_contratto,
                      obbligazione.pg_contratto, obbligazione.stato_contratto,
                      contratto.oggetto, contratto.dt_inizio_validita,
                      contratto.dt_fine_validita,
                      --obbligazione_scad_voce.cd_linea_attivita,
                      --obbligazione_scad_voce.cd_centro_responsabilita,
                      contratto.cd_tipo_contratto,
                      contratto.im_contratto_attivo,
                      contratto.im_contratto_passivo, 0 totale_entrate,
                      SUM (obbligazione_mod_voce.im_modifica) totale_spese,
                      0 liquidato_entrate, 0 liquidato_spese,
                      0 totale_reversale, 0 totale_mandati,obbligazione.cd_uo_origine cd_unita_organizzativa
                 FROM contratto,
                      obbligazione,
                      obbligazione_modifica,
                      obbligazione_mod_voce
                WHERE contratto.esercizio = obbligazione.esercizio_contratto
                  AND contratto.stato = obbligazione.stato_contratto
                  AND contratto.pg_contratto = obbligazione.pg_contratto
                  AND obbligazione.cd_cds = obbligazione_modifica.cd_cds
                  AND obbligazione.esercizio = obbligazione_modifica.esercizio
                  AND obbligazione.esercizio_originale =obbligazione_modifica.esercizio_originale
                  AND obbligazione.pg_obbligazione = obbligazione_modifica.pg_obbligazione
                  AND obbligazione_modifica.cd_cds = obbligazione_mod_voce.cd_cds
                  AND obbligazione_modifica.esercizio = obbligazione_mod_voce.esercizio
                  AND obbligazione_modifica.pg_modifica = obbligazione_mod_voce.pg_modifica
                  -- per evitare che prende le variazioni su impegni già diminuiti nell'importo sull'obbligazione collegato al contratto
  								AND exists(select 1 from obbligazione obb where
                		OBBLIGAZIONE.CD_CDS = obb.CD_CDS
             			--And OBBLIGAZIONE.ESERCIZIO = obb.ESERCIZIO
             				And OBBLIGAZIONE.ESERCIZIO_ORIGINALE = obb.ESERCIZIO_ORIGINALE
             				And OBBLIGAZIONE.PG_OBBLIGAZIONE = obb.PG_OBBLIGAZIONE
             				and OBBLIGAZIONE.ESERCIZIO_contratto = obb.ESERCIZIO_CONTRATTO
             				And OBBLIGAZIONE.STATO_CONTRATTO = obb.STATO_CONTRATTO
             				And OBBLIGAZIONE.PG_CONTRATTO = obb.PG_CONTRATTO
             				and (obb.ESERCIZIO = obb.ESERCIZIO_ORIGINALE or
								 			obb.cd_tipo_documento_cont ='OBB_RESIM'))
             GROUP BY 'SPE',
                      --obbligazione.esercizio_originale,
                      obbligazione.cd_terzo,
                      --obbligazione.cd_elemento_voce,
                      obbligazione.esercizio_contratto,
                      obbligazione.pg_contratto,
                      obbligazione.stato_contratto,
                      contratto.oggetto,
                      contratto.dt_inizio_validita,
                      contratto.dt_fine_validita,
                      --obbligazione_scad_voce.cd_linea_attivita,
                      --obbligazione_scad_voce.cd_centro_responsabilita,
                      contratto.cd_tipo_contratto,
                      contratto.im_contratto_attivo,
                      contratto.im_contratto_passivo,
                      obbligazione.cd_uo_origine
             UNION
             SELECT   'SPE' tipo, --obbligazione.esercizio_originale,
                      obbligazione.cd_terzo, --obbligazione.cd_elemento_voce,
                      obbligazione.esercizio_contratto,
                      obbligazione.pg_contratto, obbligazione.stato_contratto,
                      contratto.oggetto, contratto.dt_inizio_validita,
                      contratto.dt_fine_validita,
                      --obbligazione_scad_voce.cd_linea_attivita,
                      --obbligazione_scad_voce.cd_centro_responsabilita,
                      contratto.cd_tipo_contratto,
                      contratto.im_contratto_attivo,
                      contratto.im_contratto_passivo, 0 totale_entrate,
                      0 totale_spese, 0 liquidato_entrate, 0 liquidato_spese,
                      0 totale_reversale,
                      SUM
                         (DECODE (NVL (obbligazione_scadenzario.im_scadenza,
                                       0),
                                  0, 0,
                                    (  obbligazione_scad_voce.im_voce
                                     / obbligazione_scadenzario.im_scadenza
                                    )
                                  * riga.im_mandato_riga
                                 )
                         ) totale_mandati,obbligazione.cd_uo_origine cd_unita_organizzativa
                 FROM contratto,
                      obbligazione,
                      obbligazione_scadenzario,
                      obbligazione_scad_voce,
                      mandato_riga riga
                WHERE obbligazione_scadenzario.cd_cds = riga.cd_cds
                  AND obbligazione_scadenzario.esercizio =
                                                   riga.esercizio_obbligazione
                  AND obbligazione_scadenzario.esercizio_originale =
                                               riga.esercizio_ori_obbligazione
                  AND obbligazione_scadenzario.pg_obbligazione =
                                                          riga.pg_obbligazione
                  AND obbligazione_scadenzario.pg_obbligazione_scadenzario =
                                              riga.pg_obbligazione_scadenzario
                  AND riga.stato <> 'A'
                  AND contratto.esercizio = obbligazione.esercizio_contratto
                  AND contratto.stato = obbligazione.stato_contratto
                  AND contratto.pg_contratto = obbligazione.pg_contratto
                  AND obbligazione.cd_cds = obbligazione_scadenzario.cd_cds
                  AND obbligazione.esercizio =
                                            obbligazione_scadenzario.esercizio
                  AND obbligazione.esercizio_originale =
                                  obbligazione_scadenzario.esercizio_originale
                  AND obbligazione.pg_obbligazione =
                                      obbligazione_scadenzario.pg_obbligazione
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
             GROUP BY 'SPE',
                      --obbligazione.esercizio_originale,
                      obbligazione.cd_terzo,
                      --obbligazione.cd_elemento_voce,
                      obbligazione.esercizio_contratto,
                      obbligazione.pg_contratto,
                      obbligazione.stato_contratto,
                      contratto.oggetto,
                      contratto.dt_inizio_validita,
                      contratto.dt_fine_validita,
                      --obbligazione_scad_voce.cd_linea_attivita,
                      --obbligazione_scad_voce.cd_centro_responsabilita,
                      contratto.cd_tipo_contratto,
                      contratto.im_contratto_attivo,
                      contratto.im_contratto_passivo,
                      obbligazione.cd_uo_origine
             UNION
             SELECT   'SPE' tipo, --obbligazione.esercizio_originale,
                      obbligazione.cd_terzo, --obbligazione.cd_elemento_voce,
                      obbligazione.esercizio_contratto,
                      obbligazione.pg_contratto, obbligazione.stato_contratto,
                      contratto.oggetto, contratto.dt_inizio_validita,
                      contratto.dt_fine_validita,
                      --obbligazione_scad_voce.cd_linea_attivita,
                      --obbligazione_scad_voce.cd_centro_responsabilita,
                      contratto.cd_tipo_contratto,
                      contratto.im_contratto_attivo,
                      contratto.im_contratto_passivo, 0 totale_entrate,
                      0 totale_spese, 0 liquidato_entrate,
                      SUM (v.im_totale_doc_amm) liquidato_spese,
                      0 totale_reversale, 0 totale_mandati,obbligazione.cd_uo_origine cd_unita_organizzativa
                 FROM contratto,
                      obbligazione,
                      obbligazione_scadenzario,
                      obbligazione_scad_voce,
                      v_doc_passivo v
                WHERE contratto.esercizio = obbligazione.esercizio_contratto
                  AND contratto.stato = obbligazione.stato_contratto
                  AND contratto.pg_contratto = obbligazione.pg_contratto
                  AND obbligazione_scadenzario.cd_cds = v.cd_cds_obbligazione
                  AND obbligazione_scadenzario.esercizio =
                                                      v.esercizio_obbligazione
                  AND obbligazione_scadenzario.esercizio_originale =
                                                  v.esercizio_ori_obbligazione
                  AND obbligazione_scadenzario.pg_obbligazione =
                                                             v.pg_obbligazione
                  AND obbligazione_scadenzario.pg_obbligazione_scadenzario =
                                                 v.pg_obbligazione_scadenzario
                  AND obbligazione.cd_cds = obbligazione_scadenzario.cd_cds
                  AND obbligazione.esercizio =
                                            obbligazione_scadenzario.esercizio
                  AND obbligazione.esercizio_originale =
                                  obbligazione_scadenzario.esercizio_originale
                  AND obbligazione.pg_obbligazione =
                                      obbligazione_scadenzario.pg_obbligazione
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
             GROUP BY 'SPE',
                      --obbligazione.esercizio_originale,
                      obbligazione.cd_terzo,
                      --obbligazione.cd_elemento_voce,
                      obbligazione.esercizio_contratto,
                      obbligazione.pg_contratto,
                      obbligazione.stato_contratto,
                      contratto.oggetto,
                      contratto.dt_inizio_validita,
                      contratto.dt_fine_validita,
                      --obbligazione_scad_voce.cd_linea_attivita,
                      --obbligazione_scad_voce.cd_centro_responsabilita,
                      contratto.cd_tipo_contratto,
                      contratto.im_contratto_attivo,
                      contratto.im_contratto_passivo,
                      obbligazione.cd_uo_origine
             union
             SELECT   'ETR' tipo, --null,
                      contratto.FIG_GIUR_EST,--null,
                      contratto.esercizio,
                      contratto.pg_contratto, contratto.stato,
                      contratto.oggetto, contratto.dt_inizio_validita,
                      contratto.dt_fine_validita,
                      --null,
                      --null,
                      contratto.cd_tipo_contratto,
                      contratto.im_contratto_attivo,
                      contratto.im_contratto_passivo,
                      0 totale_entrate,
                      0 totale_spese, 0 liquidato_entrate, 0 liquidato_spese,
                      0 totale_reversale, 0 totale_mandati,contratto.cd_unita_organizzativa
                 FROM contratto
                WHERE  stato='D' and
                			natura_contabile in('E','A') and
                	not exists(select 1 from accertamento where
                      contratto.esercizio = accertamento.esercizio_contratto
                  and contratto.stato = accertamento.stato_contratto
                  AND contratto.pg_contratto = accertamento.pg_contratto)
             GROUP BY 'ETR',
                      --null,
                      contratto.FIG_GIUR_EST,
                      --null,
                      contratto.esercizio,
                      contratto.pg_contratto,
                      contratto.stato,
                      contratto.oggetto,
                      contratto.dt_inizio_validita,
                      contratto.dt_fine_validita,
                      --null,
                      --null,
                      contratto.cd_tipo_contratto,
                      contratto.im_contratto_attivo,
                      contratto.im_contratto_passivo,
                      contratto.cd_unita_organizzativa
                union
                SELECT   'SPE' tipo, --null,
                      contratto.FIG_GIUR_EST,--null,
                      contratto.esercizio,
                      contratto.pg_contratto, contratto.stato,
                      contratto.oggetto, contratto.dt_inizio_validita,
                      contratto.dt_fine_validita,
                      --null,
                      --null,
                      contratto.cd_tipo_contratto,
                      contratto.im_contratto_attivo,
                      contratto.im_contratto_passivo,
                      0 totale_entrate,
                      0 totale_spese, 0 liquidato_entrate, 0 liquidato_spese,
                      0 totale_reversale, 0 totale_mandati,
                      contratto.cd_unita_organizzativa
                 FROM contratto
                WHERE
                    stato='D' and
                    natura_contabile in('E','P') and
                    not exists(select 1 from obbligazione where
                      contratto.esercizio = obbligazione.esercizio_contratto
                  and contratto.stato = obbligazione.stato_contratto
                  AND contratto.pg_contratto = obbligazione.pg_contratto)
             GROUP BY 'SPE',
                      --null,
                      contratto.FIG_GIUR_EST,
                      --null,
                      contratto.esercizio,
                      contratto.pg_contratto,
                      contratto.stato,
                      contratto.oggetto,
                      contratto.dt_inizio_validita,
                      contratto.dt_fine_validita,
                      --null,
                      --null,
                      contratto.cd_tipo_contratto,
                      contratto.im_contratto_attivo,
                      contratto.im_contratto_passivo,
                      contratto.cd_unita_organizzativa
                      ) a
   GROUP BY a.tipo,
            --a.esercizio_originale,
            a.cd_terzo,
            --a.cd_elemento_voce,
            a.esercizio_contratto,
            a.pg_contratto,
            a.stato_contratto,
            a.oggetto,
            a.dt_inizio_validita,
            a.dt_fine_validita,
            --a.cd_linea_attivita,
            --a.cd_centro_responsabilita,
            a.cd_tipo_contratto,
            a.im_contratto_attivo,
            a.im_contratto_passivo,
            a.cd_unita_organizzativa;
