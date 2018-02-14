--------------------------------------------------------
--  DDL for View V_CONTRATTI_TOTALI_DET
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_CONTRATTI_TOTALI_DET" ("TIPO", "ESERCIZIO_ORIGINALE", "TERZO", "CD_ELEMENTO_VOCE", "ESERCIZIO_CONTRATTO", "PG_CONTRATTO", "STATO_CONTRATTO", "OGGETTO", "DATA_INIZIO", "DATA_FINE", "LINEA", "CDR", "ESERCIZIO_OBB_ACR", "PG_OBBLIGAZIONE_ACCERTAMENTO", "ESERCIZIO_MAN_REV", "PG_MAN_REV", "ES_DOC_AMM", "PG_DOC_AMM", "TIPO_DOC", "ESERCIZIO_CONTRATTO_PADRE", "PG_CONTRATTO_PADRE", "STATO_CONTRATTO_PADRE", "DESC_VOCE", "DESC_TERZO", "DESC_GAE", "CDS", "TIPO_CONTRATTO", "TOTALE_ENTRATE_CONTRATTO", "TOTALE_SPESE_CONTRATTO", "TOTALE_ENTRATE", "TOTALE_SPESE", "LIQUIDATO_ENTRATE", "LIQUIDATO_SPESE", "TOTALE_REVERSALI", "TOTALE_MANDATI", "TOTALE_ORDINI", "TOTALE_MANDATI_NETTO", "LIQUIDATO_SPESE_NETTO") AS 
  SELECT   a.tipo, a.esercizio_originale, a.cd_terzo, a.cd_elemento_voce,
            a.esercizio_contratto, a.pg_contratto, a.stato_contratto,
            a.oggetto, a.dt_inizio_validita, a.dt_fine_validita,
            a.cd_linea_attivita, a.cd_centro_responsabilita,
            esercizio_obb_acr, pg_obbligazione_accertamento,
            esercizio_man_rev, pg_man_rev, es_doc_amm, pg_doc_amm, tipo_doc,
            a.esercizio_contratto_padre,a.pg_contratto_padre,a.stato_contratto_padre,
            desc_voce,desc_terzo,desc_gae,a.cds,a.cd_tipo_contratto,
            nvl(a.im_contratto_attivo,0),nvl(a.im_contratto_passivo,0),
            SUM (a.totale_entrate), SUM (a.totale_spese),
            round(SUM (a.liquidato_entrate),2),round(SUM (a.liquidato_spese),2),
            SUM (a.totale_reversale), SUM (a.totale_mandati), SUM (a.totale_ordini),SUM (a.totale_mandati_netto),sum(a.liquidato_spese_netto)
       FROM (
-----------Entrate dettaglio accertamenti------------
             SELECT   'ETR' tipo, accertamento.esercizio_originale,
                      accertamento.cd_terzo, accertamento.cd_elemento_voce,
                      accertamento.esercizio_contratto,
                      accertamento.pg_contratto, accertamento.stato_contratto,
                      contratto.oggetto, contratto.dt_inizio_validita,
                      contratto.dt_fine_validita,
                      accertamento_scad_voce.cd_linea_attivita,
                      accertamento_scad_voce.cd_centro_responsabilita,
                      accertamento.esercizio esercizio_obb_acr,
                      accertamento.pg_accertamento
                                                 pg_obbligazione_accertamento,
                      NULL esercizio_man_rev, NULL pg_man_rev,
                      NULL es_doc_amm, NULL pg_doc_amm, NULL tipo_doc,
                      contratto_padre.esercizio esercizio_contratto_padre,
                      contratto_padre.pg_contratto pg_contratto_padre,
                      contratto_padre.stato stato_contratto_padre,
                      elemento_voce.ds_elemento_voce desc_voce,
                      terzo.denominazione_sede desc_terzo,
                      linea_attivita.ds_linea_attivita desc_gae,
                      accertamento.cd_cds_origine cds,
                      contratto.cd_tipo_contratto,
                      contratto.IM_CONTRATTO_ATTIVO,
                      contratto.IM_CONTRATTO_PASSIVO,
                      SUM (accertamento_scad_voce.im_voce) totale_entrate,
                      0 totale_spese, 0 liquidato_entrate, 0 liquidato_spese,
                      0 totale_reversale, 0 totale_mandati, 0 totale_ordini,0 totale_mandati_netto,0 liquidato_spese_netto
                 FROM contratto,
                      contratto contratto_padre,
                      accertamento,
                      accertamento_scadenzario,
                      accertamento_scad_voce,
                      terzo,
                      elemento_voce,
                      linea_attivita
                WHERE contratto.esercizio_padre = contratto_padre.esercizio(+)
                  AND contratto.pg_contratto_padre = contratto_padre.pg_contratto(+)
                  AND contratto.esercizio = accertamento.esercizio_contratto
                  AND contratto.stato = accertamento.stato_contratto
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
                  AND elemento_voce.esercizio 			= accertamento.esercizio
        					AND elemento_voce.ti_appartenenza = accertamento.ti_appartenenza
        					AND elemento_voce.ti_gestione 		= accertamento.ti_gestione
        					AND elemento_voce.cd_elemento_voce= accertamento.cd_elemento_voce
        					AND terzo.cd_terzo         								  = accertamento.cd_terzo
        					AND linea_attivita.cd_centro_responsabilita =	accertamento_scad_voce.cd_centro_responsabilita
									AND linea_attivita.cd_linea_attivita 				= accertamento_scad_voce.cd_linea_attivita
             GROUP BY 'ETR',
                      accertamento.esercizio_originale,
                      accertamento.cd_terzo,
                      accertamento.cd_elemento_voce,
                      accertamento.esercizio_contratto,
                      accertamento.pg_contratto,
                      accertamento.stato_contratto,
                      contratto.oggetto,
                      contratto.dt_inizio_validita,
                      contratto.dt_fine_validita,
                      accertamento_scad_voce.cd_linea_attivita,
                      accertamento_scad_voce.cd_centro_responsabilita,
                      accertamento.esercizio,
                      accertamento.pg_accertamento,
                      NULL,
                      NULL,
                      NULL,
                      NULL,
                      NULL,
                      contratto_padre.esercizio ,
                      contratto_padre.pg_contratto ,
                      contratto_padre.stato,
                      elemento_voce.ds_elemento_voce,
                      terzo.denominazione_sede,
                      linea_attivita.ds_linea_attivita,
                      accertamento.cd_cds_origine,
                      contratto.cd_tipo_contratto,
                      contratto.IM_CONTRATTO_ATTIVO,
                      contratto.IM_CONTRATTO_PASSIVO
             union
             --rp
              SELECT   'ETR' tipo, accertamento.esercizio_originale,
                      accertamento.cd_terzo, accertamento.cd_elemento_voce,
                      accertamento.esercizio_contratto,
                      accertamento.pg_contratto, accertamento.stato_contratto,
                      contratto.oggetto, contratto.dt_inizio_validita,
                      contratto.dt_fine_validita,
                      accertamento_mod_voce.cd_linea_attivita,
                      accertamento_mod_voce.cd_centro_responsabilita,
                      accertamento.esercizio esercizio_obb_acr,
                      accertamento.pg_accertamento
                                                 pg_obbligazione_accertamento,
                      NULL esercizio_man_rev, NULL pg_man_rev,
                      NULL es_doc_amm, NULL pg_doc_amm, NULL tipo_doc,
                      contratto_padre.esercizio esercizio_contratto_padre,
                      contratto_padre.pg_contratto pg_contratto_padre,
                      contratto_padre.stato stato_contratto_padre,
                      elemento_voce.ds_elemento_voce desc_voce,
                      terzo.denominazione_sede desc_terzo,
                      linea_attivita.ds_linea_attivita desc_gae,
                      accertamento.cd_cds_origine cds,
                      contratto.cd_tipo_contratto,
                      contratto.IM_CONTRATTO_ATTIVO,
                      contratto.IM_CONTRATTO_PASSIVO,
                      SUM (accertamento_mod_voce.im_modifica) totale_entrate,
                      0 totale_spese, 0 liquidato_entrate, 0 liquidato_spese,
                      0 totale_reversale, 0 totale_mandati, 0 totale_ordini,0 totale_mandati_netto, 0 liquidato_spese_netto
                 FROM contratto,
                      contratto contratto_padre,
                      accertamento,
                      accertamento_modifica,
                      accertamento_mod_voce,
                      terzo,
                      elemento_voce,
                      linea_attivita
                WHERE contratto.esercizio_padre = contratto_padre.esercizio(+)
                  AND contratto.pg_contratto_padre = contratto_padre.pg_contratto(+)
                  AND contratto.esercizio = accertamento.esercizio_contratto
                  AND contratto.stato = accertamento.stato_contratto
                  AND contratto.pg_contratto = accertamento.pg_contratto
                  and accertamento.cd_cds = accertamento_modifica.cd_cds
        					AND accertamento.esercizio = accertamento_modifica.esercizio
        					AND accertamento.esercizio_originale = accertamento_modifica.esercizio_originale
				          AND accertamento.pg_accertamento =  accertamento_modifica.pg_accertamento
                  AND accertamento_modifica.cd_cds = accertamento_mod_voce.cd_cds
                  AND accertamento_modifica.esercizio = accertamento_mod_voce.esercizio
                  AND accertamento_modifica.pg_modifica = accertamento_mod_voce.pg_modifica
                  AND elemento_voce.esercizio 			= accertamento.esercizio
        					AND elemento_voce.ti_appartenenza = accertamento.ti_appartenenza
        					AND elemento_voce.ti_gestione 		= accertamento.ti_gestione
        					AND elemento_voce.cd_elemento_voce= accertamento.cd_elemento_voce
        					AND terzo.cd_terzo         								  = accertamento.cd_terzo
        					AND linea_attivita.cd_centro_responsabilita =	accertamento_mod_voce.cd_centro_responsabilita
									AND linea_attivita.cd_linea_attivita 				= accertamento_mod_voce.cd_linea_attivita
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
                      accertamento.esercizio_originale,
                      accertamento.cd_terzo,
                      accertamento.cd_elemento_voce,
                      accertamento.esercizio_contratto,
                      accertamento.pg_contratto,
                      accertamento.stato_contratto,
                      contratto.oggetto,
                      contratto.dt_inizio_validita,
                      contratto.dt_fine_validita,
                      accertamento_mod_voce.cd_linea_attivita,
                      accertamento_mod_voce.cd_centro_responsabilita,
                      accertamento.esercizio,
                      accertamento.pg_accertamento,
                      NULL,
                      NULL,
                      NULL,
                      NULL,
                      NULL,
                      contratto_padre.esercizio ,
                      contratto_padre.pg_contratto ,
                      contratto_padre.stato,
                      elemento_voce.ds_elemento_voce,
                      terzo.denominazione_sede,
                      linea_attivita.ds_linea_attivita,
                      accertamento.cd_cds_origine,
                      contratto.cd_tipo_contratto,
                      contratto.IM_CONTRATTO_ATTIVO,
                      contratto.IM_CONTRATTO_PASSIVO
             --rp
             UNION
-----------Entrate dettaglio reversali------------
             SELECT   'ETR' tipo, accertamento.esercizio_originale,
                      accertamento.cd_terzo, accertamento.cd_elemento_voce,
                      accertamento.esercizio_contratto,
                      accertamento.pg_contratto, accertamento.stato_contratto,
                      contratto.oggetto, contratto.dt_inizio_validita,
                      contratto.dt_fine_validita,
                      accertamento_scad_voce.cd_linea_attivita,
                      accertamento_scad_voce.cd_centro_responsabilita,
                      riga.esercizio_ori_accertamento esercizio_obb_acr,
                      riga.pg_accertamento pg_obbligazione_accertamento,
                      riga.esercizio esercizio_man_rev,
                      riga.pg_reversale pg_man_rev,
                      riga.esercizio_doc_amm es_doc_amm,
                      riga.pg_doc_amm pg_doc_amm,
                			cnrctb002.getdestipodocamm(riga.cd_tipo_documento_amm) tipo_doc,
                			contratto_padre.esercizio esercizio_contratto_padre,
                      contratto_padre.pg_contratto pg_contratto_padre,
                      contratto_padre.stato stato_contratto_padre,
                      elemento_voce.ds_elemento_voce desc_voce,
                      terzo.denominazione_sede desc_terzo,
                      linea_attivita.ds_linea_attivita desc_gae,
                      accertamento.cd_cds_origine cds,
                      contratto.cd_tipo_contratto,
                      contratto.IM_CONTRATTO_ATTIVO,
                      contratto.IM_CONTRATTO_PASSIVO,
                      0 totale_entrate,
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
                      0 totale_mandati, 0 totale_ordini, 0 totale_mandati_netto,0 liquidato_spese_netto
                 FROM contratto,
                      contratto contratto_padre,
                      accertamento,
                      accertamento_scadenzario,
                      accertamento_scad_voce,
                      reversale_riga riga,
                      terzo,
                      elemento_voce,
                      linea_attivita
                WHERE contratto.esercizio_padre = contratto_padre.esercizio(+)
                  AND contratto.pg_contratto_padre = contratto_padre.pg_contratto(+)
                  AND contratto.esercizio = accertamento.esercizio_contratto
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
                  AND elemento_voce.esercizio 			= accertamento.esercizio
        					AND elemento_voce.ti_appartenenza = accertamento.ti_appartenenza
        					AND elemento_voce.ti_gestione 		= accertamento.ti_gestione
        					AND elemento_voce.cd_elemento_voce= accertamento.cd_elemento_voce
        					AND terzo.cd_terzo         								  = accertamento.cd_terzo
        					AND linea_attivita.cd_centro_responsabilita =	accertamento_scad_voce.cd_centro_responsabilita
									AND linea_attivita.cd_linea_attivita 				= accertamento_scad_voce.cd_linea_attivita
             GROUP BY 'ETR',
                      accertamento.esercizio_originale,
                      accertamento.cd_terzo,
                      accertamento.cd_elemento_voce,
                      accertamento.esercizio_contratto,
                      accertamento.pg_contratto,
                      accertamento.stato_contratto,
                      contratto.oggetto,
                      contratto.dt_inizio_validita,
                      contratto.dt_fine_validita,
                      accertamento_scad_voce.cd_linea_attivita,
                      accertamento_scad_voce.cd_centro_responsabilita,
                      riga.esercizio_ori_accertamento,
                      riga.pg_accertamento,
                      riga.esercizio,
                      riga.pg_reversale,
                      riga.esercizio_doc_amm,
                      riga.pg_doc_amm,
                      cnrctb002.getdestipodocamm(riga.cd_tipo_documento_amm),
                      contratto_padre.esercizio,
                      contratto_padre.pg_contratto,
                      contratto_padre.stato,
                      elemento_voce.ds_elemento_voce,
                      terzo.denominazione_sede,
                      linea_attivita.ds_linea_attivita,
                      accertamento.cd_cds_origine,
                      contratto.cd_tipo_contratto,
                      contratto.IM_CONTRATTO_ATTIVO,
                      contratto.IM_CONTRATTO_PASSIVO
             UNION
-----------Entrate dettaglio doc.amministrativi------
             SELECT   'ETR' tipo, accertamento.esercizio_originale,
                      accertamento.cd_terzo, accertamento.cd_elemento_voce,
                      accertamento.esercizio_contratto,
                      accertamento.pg_contratto, accertamento.stato_contratto,
                      contratto.oggetto, contratto.dt_inizio_validita,
                      contratto.dt_fine_validita,
                      accertamento_scad_voce.cd_linea_attivita,
                      accertamento_scad_voce.cd_centro_responsabilita,
                      v.esercizio_ori_accertamento esercizio_obb_acr,
                      v.pg_accertamento pg_obbligazione_accertamento,
                      NULL esercizio_man_rev, NULL pg_man_rev,
                      v.esercizio es_doc_amm, v.pg_documento_amm pg_doc_amm,
                      cnrctb002.getdestipodocamm(v.cd_tipo_documento_amm) tipo_doc,
                      contratto_padre.esercizio esercizio_contratto_padre,
                      contratto_padre.pg_contratto pg_contratto_padre,
                      contratto_padre.stato stato_contratto_padre,
                      elemento_voce.ds_elemento_voce desc_voce,
                      terzo.denominazione_sede desc_terzo,
                      linea_attivita.ds_linea_attivita desc_gae,
                      accertamento.cd_cds_origine cds,
                      contratto.cd_tipo_contratto,
                      contratto.IM_CONTRATTO_ATTIVO,
                      contratto.IM_CONTRATTO_PASSIVO,
                      0 totale_entrate,
                      0 totale_spese,
                      SUM
                         (DECODE (NVL (accertamento_scadenzario.im_scadenza,
                                       0),
                                  0, 0,
                                    (  accertamento_scad_voce.im_voce
                                     / accertamento_scadenzario.im_scadenza
                                    )
                                  *v.im_totale_doc_amm
                                 )
                         )  liquidato_entrate,
                      0 liquidato_spese, 0 totale_reversale, 0 totale_mandati, 0 totale_ordini,0 totale_mandati_netto,0 liquidato_spese_netto
                 FROM contratto,
                      contratto contratto_padre,
                      accertamento,
                      accertamento_scadenzario,
                      accertamento_scad_voce,
                      v_doc_attivo v,
                      terzo,
                      elemento_voce,
                      linea_attivita
                WHERE contratto.esercizio_padre = contratto_padre.esercizio(+)
                  AND contratto.pg_contratto_padre = contratto_padre.pg_contratto(+)
                  AND contratto.esercizio = accertamento.esercizio_contratto
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
                  AND elemento_voce.esercizio 			= accertamento.esercizio
        					AND elemento_voce.ti_appartenenza = accertamento.ti_appartenenza
        					AND elemento_voce.ti_gestione 		= accertamento.ti_gestione
        					AND elemento_voce.cd_elemento_voce= accertamento.cd_elemento_voce
        					AND terzo.cd_terzo         								  = accertamento.cd_terzo
        					AND linea_attivita.cd_centro_responsabilita =	accertamento_scad_voce.cd_centro_responsabilita
									AND linea_attivita.cd_linea_attivita 				= accertamento_scad_voce.cd_linea_attivita
             GROUP BY 'ETR',
                      accertamento.esercizio_originale,
                      accertamento.cd_terzo,
                      accertamento.cd_elemento_voce,
                      accertamento.esercizio_contratto,
                      accertamento.pg_contratto,
                      accertamento.stato_contratto,
                      contratto.oggetto,
                      contratto.dt_inizio_validita,
                      contratto.dt_fine_validita,
                      accertamento_scad_voce.cd_linea_attivita,
                      accertamento_scad_voce.cd_centro_responsabilita,
                      v.esercizio_ori_accertamento,
                      v.pg_accertamento,
                      NULL,
                      NULL,
                      v.esercizio,
                      v.pg_documento_amm,
                      cnrctb002.getdestipodocamm(v.cd_tipo_documento_amm),
                      contratto_padre.esercizio ,
                      contratto_padre.pg_contratto ,
                      contratto_padre.stato ,
                      elemento_voce.ds_elemento_voce,
                      terzo.denominazione_sede,
                      linea_attivita.ds_linea_attivita,
                      accertamento.cd_cds_origine,
                      contratto.cd_tipo_contratto,
                      contratto.IM_CONTRATTO_ATTIVO,
                      contratto.IM_CONTRATTO_PASSIVO
             UNION
-----------Spese dettaglio obbligazioni------------
             SELECT   'SPE' tipo, obbligazione.esercizio_originale,
                      obbligazione.cd_terzo, obbligazione.cd_elemento_voce,
                      obbligazione.esercizio_contratto,
                      obbligazione.pg_contratto, obbligazione.stato_contratto,
                      contratto.oggetto, contratto.dt_inizio_validita,
                      contratto.dt_fine_validita,
                      obbligazione_scad_voce.cd_linea_attivita,
                      obbligazione_scad_voce.cd_centro_responsabilita,
                      obbligazione.esercizio esercizio_obb_acr,
                      obbligazione.pg_obbligazione
                                                 pg_obbligazione_accertamento,
                      NULL esercizio_man_rev, NULL pg_man_rev,
                      NULL es_doc_amm, NULL pg_doc_amm, NULL tipo_doc,
                      contratto_padre.esercizio esercizio_contratto_padre,
                      contratto_padre.pg_contratto pg_contratto_padre,
                      contratto_padre.stato stato_contratto_padre,
                      elemento_voce.ds_elemento_voce desc_voce,
                      terzo.denominazione_sede desc_terzo,
                      linea_attivita.ds_linea_attivita desc_gae,
                      obbligazione.cd_cds cds,
                      contratto.cd_tipo_contratto,
                      contratto.IM_CONTRATTO_ATTIVO,
                      contratto.IM_CONTRATTO_PASSIVO,
                      0 totale_entrate,
                      SUM (obbligazione_scad_voce.im_voce) totale_spese,
                      0 liquidato_entrate, 0 liquidato_spese,
                      0 totale_reversale, 0 totale_mandati, 0 totale_ordini,0 totale_mandati_netto,0 liquidato_spese_netto
                 FROM contratto,
                      contratto contratto_padre,
                      obbligazione,
                      obbligazione_scadenzario,
                      obbligazione_scad_voce,
                      terzo,
                      elemento_voce,
                      linea_attivita
                WHERE contratto.esercizio_padre = contratto_padre.esercizio(+)
                  AND contratto.pg_contratto_padre = contratto_padre.pg_contratto(+)
                  AND contratto.esercizio = obbligazione.esercizio_contratto
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
                  AND elemento_voce.esercizio 			= obbligazione.esercizio
        					AND elemento_voce.ti_appartenenza = obbligazione.ti_appartenenza
        					AND elemento_voce.ti_gestione 		= obbligazione.ti_gestione
        					AND elemento_voce.cd_elemento_voce= obbligazione.cd_elemento_voce
        					AND terzo.cd_terzo         								  = obbligazione.cd_terzo
        					AND linea_attivita.cd_centro_responsabilita =	obbligazione_scad_voce.cd_centro_responsabilita
									AND linea_attivita.cd_linea_attivita 				= obbligazione_scad_voce.cd_linea_attivita
             GROUP BY 'SPE',
                      obbligazione.esercizio_originale,
                      obbligazione.cd_terzo,
                      obbligazione.cd_elemento_voce,
                      obbligazione.esercizio_contratto,
                      obbligazione.pg_contratto,
                      obbligazione.stato_contratto,
                      contratto.oggetto,
                      contratto.dt_inizio_validita,
                      contratto.dt_fine_validita,
                      obbligazione_scad_voce.cd_linea_attivita,
                      obbligazione_scad_voce.cd_centro_responsabilita,
                      obbligazione.esercizio,
                      obbligazione.pg_obbligazione,
                      NULL,
                      NULL,
                      NULL,
                      NULL,
                      NULL,
                      contratto_padre.esercizio ,
                      contratto_padre.pg_contratto ,
                      contratto_padre.stato ,
                      elemento_voce.ds_elemento_voce,
                      terzo.denominazione_sede,
                      linea_attivita.ds_linea_attivita,
                      obbligazione.cd_cds,
                      contratto.cd_tipo_contratto,
                      contratto.IM_CONTRATTO_ATTIVO,
                      contratto.IM_CONTRATTO_PASSIVO
             UNION
             --rp
             SELECT   'SPE' tipo, obbligazione.esercizio_originale,
                      obbligazione.cd_terzo, obbligazione.cd_elemento_voce,
                      obbligazione.esercizio_contratto,
                      obbligazione.pg_contratto, obbligazione.stato_contratto,
                      contratto.oggetto, contratto.dt_inizio_validita,
                      contratto.dt_fine_validita,
                      obbligazione_mod_voce.cd_linea_attivita,
                      obbligazione_mod_voce.cd_centro_responsabilita,
                      obbligazione.esercizio esercizio_obb_acr,
                      obbligazione.pg_obbligazione
                                                 pg_obbligazione_accertamento,
                      NULL esercizio_man_rev, NULL pg_man_rev,
                      NULL es_doc_amm, NULL pg_doc_amm, NULL tipo_doc,
                      contratto_padre.esercizio esercizio_contratto_padre,
                      contratto_padre.pg_contratto pg_contratto_padre,
                      contratto_padre.stato stato_contratto_padre,
                      elemento_voce.ds_elemento_voce desc_voce,
                      terzo.denominazione_sede desc_terzo,
                      linea_attivita.ds_linea_attivita desc_gae,
                      obbligazione.cd_cds cds,
                      contratto.cd_tipo_contratto,
                      contratto.IM_CONTRATTO_ATTIVO,
                      contratto.IM_CONTRATTO_PASSIVO,
                      0 totale_entrate,
                      SUM (obbligazione_mod_voce.im_modifica) totale_spese,
                      0 liquidato_entrate, 0 liquidato_spese,
                      0 totale_reversale, 0 totale_mandati, 0 totale_ordini,0 totale_mandati_netto,0 liquidato_spese_netto
                 FROM contratto,
                      contratto contratto_padre,
                      obbligazione,
                      obbligazione_modifica,
                      obbligazione_mod_voce,
                      terzo,
                      elemento_voce,
                      linea_attivita
                WHERE contratto.esercizio_padre = contratto_padre.esercizio(+)
                  AND contratto.pg_contratto_padre = contratto_padre.pg_contratto(+)
                  AND contratto.esercizio = obbligazione.esercizio_contratto
                  AND contratto.stato = obbligazione.stato_contratto
                  AND contratto.pg_contratto = obbligazione.pg_contratto
                  AND obbligazione.cd_cds = obbligazione_modifica.cd_cds
                  AND obbligazione.esercizio = obbligazione_modifica.esercizio
                  AND obbligazione.esercizio_originale =obbligazione_modifica.esercizio_originale
                  AND obbligazione.pg_obbligazione = obbligazione_modifica.pg_obbligazione
                  AND obbligazione_modifica.cd_cds = obbligazione_mod_voce.cd_cds
                  AND obbligazione_modifica.esercizio = obbligazione_mod_voce.esercizio
                  AND obbligazione_modifica.pg_modifica = obbligazione_mod_voce.pg_modifica
                  AND elemento_voce.esercizio 			= obbligazione.esercizio
        					AND elemento_voce.ti_appartenenza = obbligazione.ti_appartenenza
        					AND elemento_voce.ti_gestione 		= obbligazione.ti_gestione
        					AND elemento_voce.cd_elemento_voce= obbligazione.cd_elemento_voce
        					AND terzo.cd_terzo         								  = obbligazione.cd_terzo
        					AND linea_attivita.cd_centro_responsabilita =	obbligazione_mod_voce.cd_centro_responsabilita
									AND linea_attivita.cd_linea_attivita 				= obbligazione_mod_voce.cd_linea_attivita
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
                      obbligazione.esercizio_originale,
                      obbligazione.cd_terzo,
                      obbligazione.cd_elemento_voce,
                      obbligazione.esercizio_contratto,
                      obbligazione.pg_contratto,
                      obbligazione.stato_contratto,
                      contratto.oggetto,
                      contratto.dt_inizio_validita,
                      contratto.dt_fine_validita,
                      obbligazione_mod_voce.cd_linea_attivita,
                      obbligazione_mod_voce.cd_centro_responsabilita,
                      obbligazione.esercizio,
                      obbligazione.pg_obbligazione,
                      NULL,
                      NULL,
                      NULL,
                      NULL,
                      NULL,
                      contratto_padre.esercizio ,
                      contratto_padre.pg_contratto ,
                      contratto_padre.stato ,
                      elemento_voce.ds_elemento_voce,
                      terzo.denominazione_sede,
                      linea_attivita.ds_linea_attivita,
                      obbligazione.cd_cds,
                      contratto.cd_tipo_contratto,
                      contratto.IM_CONTRATTO_ATTIVO,
                      contratto.IM_CONTRATTO_PASSIVO
                 --rp
             UNION
-----------Spese dettaglio mandati------------
           SELECT   'SPE' tipo, obbligazione.esercizio_originale,
                      obbligazione.cd_terzo, obbligazione.cd_elemento_voce,
                      obbligazione.esercizio_contratto,
                      obbligazione.pg_contratto, obbligazione.stato_contratto,
                      contratto.oggetto, contratto.dt_inizio_validita,
                      contratto.dt_fine_validita,
                      obbligazione_scad_voce.cd_linea_attivita,
                      obbligazione_scad_voce.cd_centro_responsabilita,
                      riga.esercizio_ori_obbligazione esercizio_obb_acr,
                      riga.pg_obbligazione pg_obbligazione_accertamento,
                      riga.esercizio esercizio_man_rev,
                      riga.pg_mandato pg_man_rev,
                      riga.esercizio_doc_amm es_doc_amm,
                      riga.pg_doc_amm pg_doc_amm,
                      cnrctb002.getdestipodocamm(riga.cd_tipo_documento_amm) tipo_doc,
                      contratto_padre.esercizio esercizio_contratto_padre,
                      contratto_padre.pg_contratto pg_contratto_padre,
                      contratto_padre.stato stato_contratto_padre,
                      elemento_voce.ds_elemento_voce desc_voce,
                      terzo.denominazione_sede desc_terzo,
                      linea_attivita.ds_linea_attivita desc_gae,
                      obbligazione.cd_cds cds,
                      contratto.cd_tipo_contratto,
                      contratto.IM_CONTRATTO_ATTIVO,
                      contratto.IM_CONTRATTO_PASSIVO,
                      0 totale_entrate,
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
                         ) totale_mandati, 0 totale_ordini,
                          SUM(ROUND   ( (( obbligazione_scad_voce.im_voce / obbligazione_scadenzario.im_scadenza) * riga.im_mandato_riga)
                         *(decode (v.IM_IMPONIBILE_DOC_AMM,0,v.IM_totale_DOC_AMM,v.IM_IMPONIBILE_DOC_AMM)/v.IM_totale_DOC_AMM),2))  totale_mandati_netto ,0 liquidato_spese_netto
                 FROM contratto,
                      contratto contratto_padre,
                      obbligazione,
                      obbligazione_scadenzario,
                      obbligazione_scad_voce,
                      mandato_riga riga,
                      v_doc_passivo_obbligazione v,
                      terzo,
                      elemento_voce,
                      linea_attivita
                WHERE contratto.esercizio_padre = contratto_padre.esercizio(+)
                  AND contratto.pg_contratto_padre = contratto_padre.pg_contratto(+)
                  AND obbligazione_scadenzario.cd_cds = riga.cd_cds
                  AND obbligazione_scadenzario.esercizio =
                                                   riga.esercizio_obbligazione
                  AND obbligazione_scadenzario.esercizio_originale =
                                               riga.esercizio_ori_obbligazione
                  AND obbligazione_scadenzario.pg_obbligazione =
                                                          riga.pg_obbligazione
                  AND obbligazione_scadenzario.pg_obbligazione_scadenzario =
                                              riga.pg_obbligazione_scadenzario
                  AND obbligazione_scadenzario.cd_cds = v.cd_cds_obbligazione
                  AND obbligazione_scadenzario.esercizio =
                                                      v.esercizio_obbligazione
                  AND obbligazione_scadenzario.esercizio_originale =
                                                  v.esercizio_ori_obbligazione
                  AND obbligazione_scadenzario.pg_obbligazione =
                                                             v.pg_obbligazione
                  AND obbligazione_scadenzario.pg_obbligazione_scadenzario =
                                                 v.pg_obbligazione_scadenzario
                  and v.esercizio   = riga.ESERCIZIO_DOC_AMM
                  AND V.CD_TIPO_DOCUMENTO_AMM = RIGA.CD_TIPO_DOCUMENTO_AMM
                  and v.PG_DOCUMENTO_AMM   = riga.PG_DOC_AMM
                  AND riga.stato <> 'A'
                  and IM_totale_DOC_AMM >0
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
                  AND elemento_voce.esercizio 			= obbligazione.esercizio
        					AND elemento_voce.ti_appartenenza = obbligazione.ti_appartenenza
        					AND elemento_voce.ti_gestione 		= obbligazione.ti_gestione
        					AND elemento_voce.cd_elemento_voce= obbligazione.cd_elemento_voce
        					AND terzo.cd_terzo         								  = obbligazione.cd_terzo
        					AND linea_attivita.cd_centro_responsabilita =	obbligazione_scad_voce.cd_centro_responsabilita
									AND linea_attivita.cd_linea_attivita 				= obbligazione_scad_voce.cd_linea_attivita
             GROUP BY 'SPE',
                      obbligazione.esercizio_originale,
                      obbligazione.cd_terzo,
                      obbligazione.cd_elemento_voce,
                      obbligazione.esercizio_contratto,
                      obbligazione.pg_contratto,
                      obbligazione.stato_contratto,
                      contratto.oggetto,
                      contratto.dt_inizio_validita,
                      contratto.dt_fine_validita,
                      obbligazione_scad_voce.cd_linea_attivita,
                      obbligazione_scad_voce.cd_centro_responsabilita,
                      riga.esercizio_ori_obbligazione,
                      riga.pg_obbligazione,
                      riga.esercizio,
                      riga.pg_mandato,
                      riga.esercizio_doc_amm,
                      riga.pg_doc_amm,
                      cnrctb002.getdestipodocamm(riga.cd_tipo_documento_amm),
                      contratto_padre.esercizio ,
                      contratto_padre.pg_contratto ,
                      contratto_padre.stato,
                      elemento_voce.ds_elemento_voce,
                      terzo.denominazione_sede,
                      linea_attivita.ds_linea_attivita,
                      obbligazione.cd_cds,
                      contratto.cd_tipo_contratto,
                      contratto.IM_CONTRATTO_ATTIVO,
                      contratto.IM_CONTRATTO_PASSIVO
             UNION
-----------Spese dettaglio doc.amministrativi--------
             SELECT   'SPE' tipo, obbligazione.esercizio_originale,
                      obbligazione.cd_terzo, obbligazione.cd_elemento_voce,
                      obbligazione.esercizio_contratto,
                      obbligazione.pg_contratto, obbligazione.stato_contratto,
                      contratto.oggetto, contratto.dt_inizio_validita,
                      contratto.dt_fine_validita,
                      obbligazione_scad_voce.cd_linea_attivita,
                      obbligazione_scad_voce.cd_centro_responsabilita,
                      v.esercizio_ori_obbligazione esercizio_obb_acr,
                      v.pg_obbligazione pg_obbligazione_accertamento,
                      NULL esercizio_man_rev, NULL pg_man_rev,
                      v.esercizio es_doc_amm, v.pg_documento_amm pg_doc_amm,
                      cnrctb002.getdestipodocamm(v.cd_tipo_documento_amm) tipo_doc,
                      contratto_padre.esercizio esercizio_contratto_padre,
                      contratto_padre.pg_contratto pg_contratto_padre,
                      contratto_padre.stato stato_contratto_padre,
                      elemento_voce.ds_elemento_voce desc_voce,
                      terzo.denominazione_sede desc_terzo,
                      linea_attivita.ds_linea_attivita desc_gae,
                      obbligazione.cd_cds cds,
                      contratto.cd_tipo_contratto,
                      contratto.IM_CONTRATTO_ATTIVO,
                      contratto.IM_CONTRATTO_PASSIVO,
                      0 totale_entrate,
                      0 totale_spese, 0 liquidato_entrate,
                      round(SUM
                         (DECODE (NVL (obbligazione_scadenzario.im_scadenza,
                                       0),
                                  0, 0,
                                    (  obbligazione_scad_voce.im_voce
                                     / obbligazione_scadenzario.im_scadenza
                                    )
                                  *v.im_totale_doc_amm
                                 )
                         ),2) liquidato_spese,
                      0 totale_reversale, 0 totale_mandati, 0 totale_ordini,0 totale_mandati_netto,
                      round(SUM
                         (DECODE (NVL (obbligazione_scadenzario.im_scadenza,
                                       0),
                                  0, 0,
                                    (  obbligazione_scad_voce.im_voce
                                     / obbligazione_scadenzario.im_scadenza
                                    )
                                  *decode (v.IM_IMPONIBILE_DOC_AMM,0,v.IM_totale_DOC_AMM,v.IM_IMPONIBILE_DOC_AMM)
                                 )
                         ),2)  liquidato_spese_netto
                 FROM contratto,
                      contratto contratto_padre,
                      obbligazione,
                      obbligazione_scadenzario,
                      obbligazione_scad_voce,
                      v_doc_passivo v,
                      terzo,
                      elemento_voce,
                      linea_attivita
                WHERE contratto.esercizio_padre = contratto_padre.esercizio(+)
                  AND contratto.pg_contratto_padre = contratto_padre.pg_contratto(+)
                  AND contratto.esercizio = obbligazione.esercizio_contratto
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
                  and obbligazione_scadenzario.im_scadenza != 0
                  AND elemento_voce.esercizio 			= obbligazione.esercizio
        					AND elemento_voce.ti_appartenenza = obbligazione.ti_appartenenza
        					AND elemento_voce.ti_gestione 		= obbligazione.ti_gestione
        					AND elemento_voce.cd_elemento_voce= obbligazione.cd_elemento_voce
        					AND terzo.cd_terzo         								  = obbligazione.cd_terzo
        					AND linea_attivita.cd_centro_responsabilita =	obbligazione_scad_voce.cd_centro_responsabilita
									AND linea_attivita.cd_linea_attivita 				= obbligazione_scad_voce.cd_linea_attivita
             GROUP BY 'SPE',
                      obbligazione.esercizio_originale,
                      obbligazione.cd_terzo,
                      obbligazione.cd_elemento_voce,
                      obbligazione.esercizio_contratto,
                      obbligazione.pg_contratto,
                      obbligazione.stato_contratto,
                      contratto.oggetto,
                      contratto.dt_inizio_validita,
                      contratto.dt_fine_validita,
                      obbligazione_scad_voce.cd_linea_attivita,
                      obbligazione_scad_voce.cd_centro_responsabilita,
                      v.esercizio_ori_obbligazione,
                      v.pg_obbligazione,
                      NULL,
                      NULL,
                      v.esercizio,
                      v.pg_documento_amm,
                      cnrctb002.getdestipodocamm(v.cd_tipo_documento_amm),
                      contratto_padre.esercizio ,
                      contratto_padre.pg_contratto ,
                      contratto_padre.stato ,
                      elemento_voce.ds_elemento_voce,
                      terzo.denominazione_sede,
                      linea_attivita.ds_linea_attivita,
                      obbligazione.cd_cds,
                      contratto.cd_tipo_contratto,
                      contratto.IM_CONTRATTO_ATTIVO,
                      contratto.IM_CONTRATTO_PASSIVO
             UNION
-----------Ordini--------
             SELECT   'ORD' tipo, ordine_acq.esercizio,
                      ordine_acq.cd_terzo, obbligazione.cd_elemento_voce,
                      ordine_acq.esercizio_contratto,
                      ordine_acq.pg_contratto, ordine_acq.stato_contratto,
                      contratto.oggetto, contratto.dt_inizio_validita,
                      contratto.dt_fine_validita,
                      null cd_linea_attivita,
                      null cd_centro_responsabilita,
                      obbligazione.esercizio esercizio_obb_acr,
                      obbligazione.pg_obbligazione pg_obbligazione_accertamento,
                      NULL esercizio_man_rev, NULL pg_man_rev,
                      NULL es_doc_amm, NULL pg_doc_amm, NULL tipo_doc,
                      contratto_padre.esercizio esercizio_contratto_padre,
                      contratto_padre.pg_contratto pg_contratto_padre,
                      contratto_padre.stato stato_contratto_padre,
                      elemento_voce.ds_elemento_voce desc_voce,
                      terzo.denominazione_sede desc_terzo,
                      null desc_gae,
                      ordine_acq.cd_cds cds,
                      contratto.cd_tipo_contratto,
                      contratto.IM_CONTRATTO_ATTIVO,
                      contratto.IM_CONTRATTO_PASSIVO,
                      0 totale_entrate,
                      0 totale_spese, 0 liquidato_entrate, 0 liquidato_spese,
                      0 totale_reversale, 0 totale_mandati, SUM (ordine_acq_consegna.im_totale_consegna) totale_ordini,0 totale_mandati_netto, 0 liquidato_spese_netto
                 FROM contratto,
                      contratto contratto_padre,
                      obbligazione,
                      obbligazione_scadenzario,
                      ordine_acq,
                      ordine_acq_riga,
                      ordine_acq_consegna,
                      terzo,
                      elemento_voce
                WHERE contratto.esercizio_padre = contratto_padre.esercizio(+)
                  AND contratto.pg_contratto_padre = contratto_padre.pg_contratto(+)
                  AND contratto.esercizio = ordine_acq.esercizio_contratto
                  AND contratto.stato = ordine_acq.stato_contratto
                  AND contratto.pg_contratto = ordine_acq.pg_contratto
                  and ordine_acq_consegna.cd_cds_obbl = obbligazione_scadenzario.cd_cds(+) AND
                  ordine_acq_consegna.esercizio_obbl = obbligazione_scadenzario.esercizio(+) AND
                  ordine_acq_consegna.esercizio_orig_obbl = obbligazione_scadenzario.esercizio_originale(+) AND
                  ordine_acq_consegna.pg_obbligazione = obbligazione_scadenzario.pg_obbligazione(+) AND
                  ordine_acq_consegna.pg_obbligazione_scad = obbligazione_scadenzario.pg_obbligazione_scadenzario(+)
                  AND obbligazione_scadenzario.cd_cds = obbligazione.cd_cds(+)
                  AND obbligazione_scadenzario.esercizio = obbligazione.esercizio (+)
                  AND obbligazione_scadenzario.esercizio_originale = obbligazione.esercizio_originale (+)
                  AND obbligazione_scadenzario.pg_obbligazione = obbligazione.pg_obbligazione (+)
                  AND obbligazione.esercizio        = elemento_voce.esercizio (+)
        					AND obbligazione.ti_appartenenza  = elemento_voce.ti_appartenenza (+)
        					AND obbligazione.ti_gestione      = elemento_voce.ti_gestione (+)
        					AND obbligazione.cd_elemento_voce = elemento_voce.cd_elemento_voce (+)
        					AND terzo.cd_terzo         								  = ordine_acq.cd_terzo
                  AND ordine_acq_riga.esercizio = ordine_acq.esercizio
                  AND ordine_acq_riga.cd_cds = ordine_acq.cd_cds
                  AND ordine_acq_riga.cd_unita_operativa = ordine_acq.cd_unita_operativa
                  AND ordine_acq_riga.cd_numeratore = ordine_acq.cd_numeratore
                  AND ordine_acq_riga.numero = ordine_acq.numero
                  AND ordine_acq_riga.esercizio = ordine_acq_consegna.esercizio
                  AND ordine_acq_riga.cd_cds = ordine_acq_consegna.cd_cds
                  AND ordine_acq_riga.cd_unita_operativa = ordine_acq_consegna.cd_unita_operativa
                  AND ordine_acq_riga.cd_numeratore = ordine_acq_consegna.cd_numeratore
                  AND ordine_acq_riga.numero = ordine_acq_consegna.numero
                  AND ordine_acq_riga.riga = ordine_acq_consegna.riga
        					AND ordine_acq.stato != 'ANN'
             GROUP BY 'ORD',
                      ordine_acq.esercizio,
                      ordine_acq.cd_terzo, obbligazione.cd_elemento_voce,
                      ordine_acq.esercizio_contratto,
                      ordine_acq.pg_contratto, ordine_acq.stato_contratto,
                      contratto.oggetto, contratto.dt_inizio_validita,
                      contratto.dt_fine_validita,
                      null ,
                      null ,
                      obbligazione.esercizio,
                      obbligazione.pg_obbligazione,
                      NULL, NULL ,
                      NULL, NULL, NULL,
                      contratto_padre.esercizio,
                      contratto_padre.pg_contratto,
                      contratto_padre.stato,
                      elemento_voce.ds_elemento_voce,
                      terzo.denominazione_sede,
                      null,
                      ordine_acq.cd_cds,
                      contratto.cd_tipo_contratto,
                      contratto.IM_CONTRATTO_ATTIVO,
                      contratto.IM_CONTRATTO_PASSIVO) a
   GROUP BY a.tipo,
            a.esercizio_originale,
            a.cd_terzo,
            a.cd_elemento_voce,
            a.esercizio_contratto,
            a.pg_contratto,
            a.stato_contratto,
            a.oggetto,
            a.dt_inizio_validita,
            a.dt_fine_validita,
            a.cd_linea_attivita,
            a.cd_centro_responsabilita,
            a.esercizio_obb_acr,
            a.pg_obbligazione_accertamento,
            a.esercizio_man_rev,
            a.pg_man_rev,
            a.es_doc_amm,
            a.pg_doc_amm,
            a.tipo_doc,
            a.esercizio_contratto_padre,a.pg_contratto_padre,a.stato_contratto_padre,
            a.desc_voce,
            a.desc_terzo,
            a.desc_gae,
            a.cds,
            a.cd_tipo_contratto,
            nvl(a.IM_CONTRATTO_ATTIVO,0),
            nvl(a.IM_CONTRATTO_PASSIVO,0);
