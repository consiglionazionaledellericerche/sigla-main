--------------------------------------------------------
--  DDL for View V_CONS_ACCERTAMENTI_TERZO
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_CONS_ACCERTAMENTI_TERZO" ("CD_CDS", "ESERCIZIO", "CD_UNITA_ORGANIZZATIVA", "CD_CDS_ORIGINE", "CD_UO_ORIGINE", "CD_ELEMENTO_VOCE", "TI_APPARTENENZA", "TI_GESTIONE", "ESERCIZIO_ORIGINALE", "PG_ACCERTAMENTO", "FL_PGIRO", "IM_SCADENZA_COMP", "IM_SCADENZA_RES", "IM_ASSOCIATO_DOC_AMM_COMP", "IM_ASSOCIATO_DOC_AMM_RES", "IM_INCASSATO_COMP", "IM_INCASSATO_RES", "CD_TERZO", "DENOMINAZIONE_SEDE", "DS_ACCERTAMENTO") AS 
  SELECT
--
-- Date: 14/05/2007
-- Version: 1.1
--
-- History
--
-- Date: 19/07/2006
-- Version: 1.0
-- Gestione Impegni/Accertamenti Residui:
-- gestito il nuovo campo ESERCIZIO_ORIGINALE
--
--
-- Date: 14/05/2007
-- Version: 1.1
-- Modificata logica per distinguere
-- compenza e residui
--
-- Body
--
          accertamento.cd_cds, accertamento.esercizio,
          accertamento.cd_unita_organizzativa, accertamento.cd_cds_origine,
          accertamento.cd_uo_origine, accertamento.cd_elemento_voce,
          accertamento.ti_appartenenza, accertamento.ti_gestione,
          accertamento.esercizio_originale, accertamento.pg_accertamento,
          accertamento.fl_pgiro,
          NVL
             (DECODE
                 (accertamento.esercizio,
                  accertamento.esercizio_originale, (SELECT NVL
                                                               (SUM
                                                                   (accertamento_scadenzario.im_scadenza
                                                                   ),
                                                                0
                                                               )
                                                       FROM accertamento_scadenzario
                                                      WHERE accertamento_scadenzario.cd_cds =
                                                               accertamento.cd_cds
                                                        AND accertamento_scadenzario.esercizio =
                                                               accertamento.esercizio
                                                        AND accertamento_scadenzario.esercizio_originale =
                                                               accertamento.esercizio_originale
                                                        AND accertamento_scadenzario.pg_accertamento =
                                                               accertamento.pg_accertamento),
                  0
                 ),
              0
             ) im_scadenza_comp,
          NVL
             (DECODE
                  (accertamento.esercizio,
                   accertamento.esercizio_originale, 0,
                   (SELECT NVL (SUM (accertamento_scadenzario.im_scadenza), 0)
                      FROM accertamento_scadenzario
                     WHERE accertamento_scadenzario.cd_cds =
                                                           accertamento.cd_cds
                       AND accertamento_scadenzario.esercizio =
                                                        accertamento.esercizio
                       AND accertamento_scadenzario.esercizio_originale =
                                              accertamento.esercizio_originale
                       AND accertamento_scadenzario.pg_accertamento =
                                                  accertamento.pg_accertamento)
                  ),
              0
             ) im_scadenza_res,
          NVL
             (DECODE
                 (accertamento.esercizio,
                  accertamento.esercizio_originale, (SELECT NVL
                                                               (SUM
                                                                   (accertamento_scadenzario.im_associato_doc_amm
                                                                   ),
                                                                0
                                                               )
                                                       FROM accertamento_scadenzario
                                                      WHERE accertamento_scadenzario.cd_cds =
                                                               accertamento.cd_cds
                                                        AND accertamento_scadenzario.esercizio =
                                                               accertamento.esercizio
                                                        AND accertamento_scadenzario.esercizio_originale =
                                                               accertamento.esercizio_originale
                                                        AND accertamento_scadenzario.pg_accertamento =
                                                               accertamento.pg_accertamento),
                  0
                 ),
              0
             ) im_associato_doc_amm_comp,
          NVL
             (DECODE
                 (accertamento.esercizio,
                  accertamento.esercizio_originale, 0,
                  (SELECT NVL
                             (SUM
                                 (accertamento_scadenzario.im_associato_doc_amm
                                 ),
                              0
                             )
                     FROM accertamento_scadenzario
                    WHERE accertamento_scadenzario.cd_cds =
                                                           accertamento.cd_cds
                      AND accertamento_scadenzario.esercizio =
                                                        accertamento.esercizio
                      AND accertamento_scadenzario.esercizio_originale =
                                              accertamento.esercizio_originale
                      AND accertamento_scadenzario.pg_accertamento =
                                                  accertamento.pg_accertamento)
                 ),
              0
             ) im_associato_doc_amm_res,
          NVL
             (DECODE
                 (accertamento.esercizio,
                  accertamento.esercizio_originale, (SELECT NVL
                                                               (SUM
                                                                   (DECODE
                                                                       (reversale_riga.stato,
                                                                        'A', 0,
                                                                        reversale_riga.im_reversale_riga
                                                                       )
                                                                   ),
                                                                0
                                                               )
                                                       FROM reversale_riga
                                                      WHERE reversale_riga.cd_cds =
                                                               accertamento.cd_cds
                                                        AND reversale_riga.esercizio_accertamento =
                                                               accertamento.esercizio
                                                        AND reversale_riga.esercizio_ori_accertamento =
                                                               accertamento.esercizio_originale
                                                        AND reversale_riga.pg_accertamento =
                                                               accertamento.pg_accertamento),
                  0
                 ),
              0
             ) im_incassato_comp,
          NVL
             (DECODE
                 (accertamento.esercizio,
                  accertamento.esercizio_originale, 0,
                  (SELECT NVL (SUM (DECODE (reversale_riga.stato,
                                            'A', 0,
                                            reversale_riga.im_reversale_riga
                                           )
                                   ),
                               0
                              )
                     FROM reversale_riga
                    WHERE reversale_riga.cd_cds = accertamento.cd_cds
                      AND reversale_riga.esercizio_accertamento =
                                                        accertamento.esercizio
                      AND reversale_riga.esercizio_ori_accertamento =
                                              accertamento.esercizio_originale
                      AND reversale_riga.pg_accertamento =
                                                  accertamento.pg_accertamento)
                 ),
              0
             ) im_incassato_res, 
             terzo.cd_terzo,
             terzo.denominazione_sede,
             accertamento.ds_accertamento
     FROM accertamento, terzo
     where accertamento.cd_terzo = terzo.cd_terzo ;
