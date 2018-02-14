--------------------------------------------------------
--  DDL for View V_CONS_OBBLIGAZIONI_GAE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_CONS_OBBLIGAZIONI_GAE" ("CD_CDS", "ESERCIZIO", "CD_UNITA_ORGANIZZATIVA", "CD_CDS_ORIGINE", "CD_UO_ORIGINE", "CD_ELEMENTO_VOCE", "TI_APPARTENENZA", "TI_GESTIONE", "ESERCIZIO_ORIGINALE", "PG_OBBLIGAZIONE", "DS_OBBLIGAZIONE", "CD_LINEA_ATTIVITA", "FL_PGIRO", "IM_SCADENZA_COMP", "IM_SCADENZA_RES", "IM_ASSOCIATO_DOC_AMM_COMP", "IM_ASSOCIATO_DOC_AMM_RES", "IM_PAGATO_COMP", "IM_PAGATO_RES") AS 
  SELECT
--
-- Date: 22/01/2015
-- Version: 1.0
--
-- View che visualizza gli importi per impegno/gae..recupera solo impegni con cd_tipo_documento_cont != 'IMP'
--
-- Body
--
            obbligazione.cd_cds, obbligazione.esercizio,
            obbligazione.cd_unita_organizzativa, obbligazione.cd_cds_origine,
            obbligazione.cd_uo_origine, obbligazione.cd_elemento_voce,
            obbligazione.ti_appartenenza, obbligazione.ti_gestione,
            obbligazione.esercizio_originale, obbligazione.pg_obbligazione,
            obbligazione.ds_obbligazione,
            obbligazione_scad_voce.cd_linea_attivita, obbligazione.fl_pgiro,
            NVL
               (DECODE
                   (obbligazione.esercizio,
                    obbligazione.esercizio_originale, (SELECT NVL
                                                                 (SUM
                                                                     (b.im_voce
                                                                     ),
                                                                  0
                                                                 )
                                                         FROM obbligazione_scadenzario,
                                                              obbligazione_scad_voce b
                                                        WHERE obbligazione_scadenzario.cd_cds =
                                                                 obbligazione.cd_cds
                                                          AND obbligazione_scadenzario.esercizio =
                                                                 obbligazione.esercizio
                                                          AND obbligazione_scadenzario.esercizio_originale =
                                                                 obbligazione.esercizio_originale
                                                          AND obbligazione_scadenzario.pg_obbligazione =
                                                                 obbligazione.pg_obbligazione
                                                          AND obbligazione_scadenzario.cd_cds =
                                                                      b.cd_cds
                                                          AND obbligazione_scadenzario.esercizio =
                                                                   b.esercizio
                                                          AND obbligazione_scadenzario.esercizio_originale =
                                                                 b.esercizio_originale
                                                          AND obbligazione_scadenzario.pg_obbligazione =
                                                                 b.pg_obbligazione
                                                          AND obbligazione_scadenzario.pg_obbligazione_scadenzario =
                                                                 b.pg_obbligazione_scadenzario
                                                          AND obbligazione_scad_voce.cd_linea_attivita =
                                                                 b.cd_linea_attivita),
                    0
                   ),
                0
               ) im_scadenza_comp,
            NVL
               (DECODE
                   (obbligazione.esercizio,
                    obbligazione.esercizio_originale, 0,
                    (SELECT NVL (SUM (b.im_voce), 0)
                       FROM obbligazione_scadenzario,
                            obbligazione_scad_voce b
                      WHERE obbligazione_scadenzario.cd_cds =
                                                           obbligazione.cd_cds
                        AND obbligazione_scadenzario.esercizio =
                                                        obbligazione.esercizio
                        AND obbligazione_scadenzario.esercizio_originale =
                                              obbligazione.esercizio_originale
                        AND obbligazione_scadenzario.pg_obbligazione =
                                                  obbligazione.pg_obbligazione
                        AND obbligazione_scadenzario.cd_cds = b.cd_cds
                        AND obbligazione_scadenzario.esercizio = b.esercizio
                        AND obbligazione_scadenzario.esercizio_originale =
                                                         b.esercizio_originale
                        AND obbligazione_scadenzario.pg_obbligazione =
                                                             b.pg_obbligazione
                        AND obbligazione_scadenzario.pg_obbligazione_scadenzario =
                                                 b.pg_obbligazione_scadenzario
                        AND obbligazione_scad_voce.cd_linea_attivita =
                                                           b.cd_linea_attivita)
                   ),
                0
               ) im_scadenza_res,
            NVL
               (DECODE
                   (obbligazione.esercizio,
                    obbligazione.esercizio_originale, (SELECT NVL
                                                                 (SUM
                                                                     (b.im_voce
                                                                     ),
                                                                  0
                                                                 )
                                                         FROM obbligazione_scadenzario,
                                                              obbligazione_scad_voce b
                                                        WHERE obbligazione_scadenzario.cd_cds =
                                                                 obbligazione.cd_cds
                                                          AND obbligazione_scadenzario.esercizio =
                                                                 obbligazione.esercizio
                                                          AND obbligazione_scadenzario.esercizio_originale =
                                                                 obbligazione.esercizio_originale
                                                          AND obbligazione_scadenzario.pg_obbligazione =
                                                                 obbligazione.pg_obbligazione
                                                          AND obbligazione_scadenzario.cd_cds =
                                                                      b.cd_cds
                                                          AND obbligazione_scadenzario.im_associato_doc_amm =
                                                                 obbligazione_scadenzario.im_scadenza
                                                          AND obbligazione_scadenzario.esercizio =
                                                                   b.esercizio
                                                          AND obbligazione_scadenzario.esercizio_originale =
                                                                 b.esercizio_originale
                                                          AND obbligazione_scadenzario.pg_obbligazione =
                                                                 b.pg_obbligazione
                                                          AND obbligazione_scadenzario.pg_obbligazione_scadenzario =
                                                                 b.pg_obbligazione_scadenzario
                                                          AND obbligazione_scad_voce.cd_linea_attivita =
                                                                 b.cd_linea_attivita),
                    0
                   ),
                0
               ) im_associato_doc_amm_comp,
            NVL
               (DECODE
                   (obbligazione.esercizio,
                    obbligazione.esercizio_originale, 0,
                    (SELECT NVL (SUM (b.im_voce), 0)
                       FROM obbligazione_scadenzario,
                            obbligazione_scad_voce b
                      WHERE obbligazione_scadenzario.cd_cds =
                                                           obbligazione.cd_cds
                        AND obbligazione_scadenzario.esercizio =
                                                        obbligazione.esercizio
                        AND obbligazione_scadenzario.esercizio_originale =
                                              obbligazione.esercizio_originale
                        AND obbligazione_scadenzario.pg_obbligazione =
                                                  obbligazione.pg_obbligazione
                        AND obbligazione_scadenzario.cd_cds = b.cd_cds
                        AND obbligazione_scadenzario.im_associato_doc_amm =
                                          obbligazione_scadenzario.im_scadenza
                        AND obbligazione_scadenzario.esercizio = b.esercizio
                        AND obbligazione_scadenzario.esercizio_originale =
                                                         b.esercizio_originale
                        AND obbligazione_scadenzario.pg_obbligazione =
                                                             b.pg_obbligazione
                        AND obbligazione_scadenzario.pg_obbligazione_scadenzario =
                                                 b.pg_obbligazione_scadenzario
                        AND obbligazione_scad_voce.cd_linea_attivita =
                                                           b.cd_linea_attivita)
                   ),
                0
               ) im_associato_doc_amm_res,
            NVL
               (DECODE
                   (obbligazione.esercizio,
                    obbligazione.esercizio_originale, (SELECT NVL
                                                                 (SUM
                                                                     (b.im_voce
                                                                     ),
                                                                  0
                                                                 )
                                                         FROM obbligazione_scadenzario,
                                                              obbligazione_scad_voce b
                                                        WHERE obbligazione_scadenzario.cd_cds =
                                                                 obbligazione.cd_cds
                                                          AND obbligazione_scadenzario.esercizio =
                                                                 obbligazione.esercizio
                                                          AND obbligazione_scadenzario.esercizio_originale =
                                                                 obbligazione.esercizio_originale
                                                          AND obbligazione_scadenzario.pg_obbligazione =
                                                                 obbligazione.pg_obbligazione
                                                          AND obbligazione_scadenzario.cd_cds =
                                                                      b.cd_cds
                                                          AND obbligazione_scadenzario.im_associato_doc_contabile =
                                                                 obbligazione_scadenzario.im_scadenza
                                                          AND obbligazione_scadenzario.esercizio =
                                                                   b.esercizio
                                                          AND obbligazione_scadenzario.esercizio_originale =
                                                                 b.esercizio_originale
                                                          AND obbligazione_scadenzario.pg_obbligazione =
                                                                 b.pg_obbligazione
                                                          AND obbligazione_scadenzario.pg_obbligazione_scadenzario =
                                                                 b.pg_obbligazione_scadenzario
                                                          AND obbligazione_scad_voce.cd_linea_attivita =
                                                                 b.cd_linea_attivita),
                    0
                   ),
                0
               ) im_associato_doc_cont_comp,
            NVL
               (DECODE
                   (obbligazione.esercizio,
                    obbligazione.esercizio_originale, 0,
                    (SELECT NVL (SUM (b.im_voce), 0)
                       FROM obbligazione_scadenzario,
                            obbligazione_scad_voce b
                      WHERE obbligazione_scadenzario.cd_cds =
                                                           obbligazione.cd_cds
                        AND obbligazione_scadenzario.esercizio =
                                                        obbligazione.esercizio
                        AND obbligazione_scadenzario.esercizio_originale =
                                              obbligazione.esercizio_originale
                        AND obbligazione_scadenzario.pg_obbligazione =
                                                  obbligazione.pg_obbligazione
                        AND obbligazione_scadenzario.cd_cds = b.cd_cds
                        AND obbligazione_scadenzario.im_associato_doc_contabile =
                                          obbligazione_scadenzario.im_scadenza
                        AND obbligazione_scadenzario.esercizio = b.esercizio
                        AND obbligazione_scadenzario.esercizio_originale =
                                                         b.esercizio_originale
                        AND obbligazione_scadenzario.pg_obbligazione =
                                                             b.pg_obbligazione
                        AND obbligazione_scadenzario.pg_obbligazione_scadenzario =
                                                 b.pg_obbligazione_scadenzario
                        AND obbligazione_scad_voce.cd_linea_attivita =
                                                           b.cd_linea_attivita)
                   ),
                0
               ) im_associato_doc_cont_res
       FROM obbligazione, obbligazione_scad_voce
      WHERE obbligazione.cd_cds = obbligazione_scad_voce.cd_cds
        AND obbligazione.esercizio = obbligazione_scad_voce.esercizio
        AND obbligazione.esercizio_originale =
                                    obbligazione_scad_voce.esercizio_originale
        AND obbligazione.pg_obbligazione =
                                        obbligazione_scad_voce.pg_obbligazione
        AND obbligazione.cd_tipo_documento_cont != 'IMP'
   GROUP BY obbligazione.cd_cds,
            obbligazione.esercizio,
            obbligazione.cd_unita_organizzativa,
            obbligazione.cd_cds_origine,
            obbligazione.cd_uo_origine,
            obbligazione.cd_elemento_voce,
            obbligazione.ti_appartenenza,
            obbligazione.ti_gestione,
            obbligazione.esercizio_originale,
            obbligazione.pg_obbligazione,
            obbligazione.ds_obbligazione,
            obbligazione.fl_pgiro,
            obbligazione_scad_voce.cd_linea_attivita ;
