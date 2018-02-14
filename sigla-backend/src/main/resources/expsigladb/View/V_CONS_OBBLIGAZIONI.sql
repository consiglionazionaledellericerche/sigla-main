--------------------------------------------------------
--  DDL for View V_CONS_OBBLIGAZIONI
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_CONS_OBBLIGAZIONI" ("CD_CDS", "ESERCIZIO", "CD_UNITA_ORGANIZZATIVA", "CD_CDS_ORIGINE", "CD_UO_ORIGINE", "CD_ELEMENTO_VOCE", "TI_APPARTENENZA", "TI_GESTIONE", "ESERCIZIO_ORIGINALE", "PG_OBBLIGAZIONE", "DS_OBBLIGAZIONE", "FL_PGIRO", "IM_SCADENZA_COMP", "IM_SCADENZA_RES", "IM_ASSOCIATO_DOC_AMM_COMP", "IM_ASSOCIATO_DOC_AMM_RES", "IM_PAGATO_COMP", "IM_PAGATO_RES") AS 
  SELECT
--
-- Date: 14/05/2007
-- Version: 1.1
--
-- History
--
-- Date: 18/07/2006
-- Version: 1.0
-- Gestione Impegni/Accertamenti Residui:
-- gestito il nuovo campo ESERCIZIO_ORIGINALE
--
-- Date: 14/05/2007
-- Version: 1.1
-- Modificata logica per distinguere
-- la competenza dai residui
--
-- Body
--
          obbligazione.cd_cds, obbligazione.esercizio,
          obbligazione.cd_unita_organizzativa, obbligazione.cd_cds_origine,
          obbligazione.cd_uo_origine, obbligazione.cd_elemento_voce,
          obbligazione.ti_appartenenza, obbligazione.ti_gestione,
          obbligazione.esercizio_originale, obbligazione.pg_obbligazione,
          obbligazione.ds_obbligazione,
          obbligazione.fl_pgiro,
          NVL
             (DECODE
                 (obbligazione.esercizio,
                  obbligazione.esercizio_originale, (SELECT NVL
                                                               (SUM
                                                                   (obbligazione_scadenzario.im_scadenza
                                                                   ),
                                                                0
                                                               )
                                                       FROM obbligazione_scadenzario
                                                      WHERE obbligazione_scadenzario.cd_cds =
                                                               obbligazione.cd_cds
                                                        AND obbligazione_scadenzario.esercizio =
                                                               obbligazione.esercizio
                                                        AND obbligazione_scadenzario.esercizio_originale =
                                                               obbligazione.esercizio_originale
                                                        AND obbligazione_scadenzario.pg_obbligazione =
                                                               obbligazione.pg_obbligazione),
                  0
                 ),
              0
             ) im_scadenza_comp,
          NVL
             (DECODE
                  (obbligazione.esercizio,
                   obbligazione.esercizio_originale, 0,
                   (SELECT NVL (SUM (obbligazione_scadenzario.im_scadenza), 0)
                      FROM obbligazione_scadenzario
                     WHERE obbligazione_scadenzario.cd_cds =
                                                           obbligazione.cd_cds
                       AND obbligazione_scadenzario.esercizio =
                                                        obbligazione.esercizio
                       AND obbligazione_scadenzario.esercizio_originale =
                                              obbligazione.esercizio_originale
                       AND obbligazione_scadenzario.pg_obbligazione =
                                                  obbligazione.pg_obbligazione)
                  ),
              0
             ) im_scadenza_res,
          NVL
             (DECODE
                 (obbligazione.esercizio,
                  obbligazione.esercizio_originale, (SELECT NVL
                                                               (SUM
                                                                   (obbligazione_scadenzario.im_associato_doc_amm
                                                                   ),
                                                                0
                                                               )
                                                       FROM obbligazione_scadenzario
                                                      WHERE obbligazione_scadenzario.cd_cds =
                                                               obbligazione.cd_cds
                                                        AND obbligazione_scadenzario.esercizio =
                                                               obbligazione.esercizio
                                                        AND obbligazione_scadenzario.esercizio_originale =
                                                               obbligazione.esercizio_originale
                                                        AND obbligazione_scadenzario.pg_obbligazione =
                                                               obbligazione.pg_obbligazione),
                  0
                 ),
              0
             ) im_associato_doc_amm_comp,
          NVL
             (DECODE
                 (obbligazione.esercizio,
                  obbligazione.esercizio_originale, 0,
                  (SELECT NVL
                             (SUM
                                 (obbligazione_scadenzario.im_associato_doc_amm
                                 ),
                              0
                             )
                     FROM obbligazione_scadenzario
                    WHERE obbligazione_scadenzario.cd_cds =
                                                           obbligazione.cd_cds
                      AND obbligazione_scadenzario.esercizio =
                                                        obbligazione.esercizio
                      AND obbligazione_scadenzario.esercizio_originale =
                                              obbligazione.esercizio_originale
                      AND obbligazione_scadenzario.pg_obbligazione =
                                                  obbligazione.pg_obbligazione)
                 ),
              0
             ) im_associato_doc_amm_res,
          NVL
             (DECODE
                   (obbligazione.esercizio,
                    obbligazione.esercizio_originale, (SELECT NVL
                                                                 (SUM
                                                                     (DECODE
                                                                         (mandato_riga.stato,
                                                                          'A', 0,
                                                                          mandato_riga.im_mandato_riga
                                                                         )
                                                                     ),
                                                                  0
                                                                 )
                                                         FROM mandato_riga
                                                        WHERE mandato_riga.cd_cds =
                                                                 obbligazione.cd_cds
                                                          AND mandato_riga.esercizio_obbligazione =
                                                                 obbligazione.esercizio
                                                          AND mandato_riga.esercizio_ori_obbligazione =
                                                                 obbligazione.esercizio_originale
                                                          AND mandato_riga.pg_obbligazione =
                                                                 obbligazione.pg_obbligazione),
                    0
                   ),
              0
             ) im_pagato_comp,
          NVL
             (DECODE
                    (obbligazione.esercizio,
                     obbligazione.esercizio_originale, 0,
                     (SELECT NVL (SUM (DECODE (mandato_riga.stato,
                                               'A', 0,
                                               mandato_riga.im_mandato_riga
                                              )
                                      ),
                                  0
                                 )
                        FROM mandato_riga
                       WHERE mandato_riga.cd_cds = obbligazione.cd_cds
                         AND mandato_riga.esercizio_obbligazione =
                                                        obbligazione.esercizio
                         AND mandato_riga.esercizio_ori_obbligazione =
                                              obbligazione.esercizio_originale
                         AND mandato_riga.pg_obbligazione =
                                                  obbligazione.pg_obbligazione)
                    ),
              0
             ) im_pagato_res
     FROM obbligazione ;
