--------------------------------------------------------
--  DDL for View V_CONS_SCAD_ACCERT
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_CONS_SCAD_ACCERT" ("CDS", "UO", "ESERCIZIO", "ESERCIZIO_ORIGINALE", "PG_ACC", "PG_ACC_SCAD", "VOCE_BILANCIO", "DATA_SCAD", "DS_SCAD", "IM_SCAD", "IM_ASS_DOC_AMM", "IMP_ASS_DOC_CONT", "DEBITORE") AS 
  SELECT DISTINCT
--
-- Version: 1.0
--
-- Vista per la consultazione Scadenzario accertamenti
--
--
-- Body
--
                   accertamento.cd_cds_origine,
                   accertamento.cd_uo_origine,
                   accertamento_scadenzario.esercizio,
                   accertamento_scadenzario.esercizio_originale,
                   accertamento_scadenzario.pg_accertamento,
                   accertamento_scadenzario.pg_accertamento_scadenzario,
                   accertamento.cd_elemento_voce,
                   accertamento_scadenzario.dt_scadenza_incasso,
                   accertamento_scadenzario.ds_scadenza,
                   accertamento_scadenzario.im_scadenza,
                   accertamento_scadenzario.im_associato_doc_amm,
                   accertamento_scadenzario.im_associato_doc_contabile,
                   terzo.denominazione_sede
              FROM accertamento_scadenzario,
                   accertamento,
                   terzo
             WHERE accertamento_scadenzario.esercizio_originale =
                                              accertamento.esercizio_originale
               AND accertamento_scadenzario.pg_accertamento =
                                                  accertamento.pg_accertamento
               AND accertamento_scadenzario.cd_cds 		= accertamento.cd_cds
               AND accertamento_scadenzario.esercizio = accertamento.esercizio
               AND terzo.cd_terzo = accertamento.cd_terzo
               AND (   accertamento.cd_tipo_documento_cont = 'ACR'
                    OR accertamento.cd_tipo_documento_cont = 'ACR_RES'
                   )
          ORDER BY esercizio,
                   accertamento_scadenzario.dt_scadenza_incasso,
                   accertamento_scadenzario.esercizio_originale,
                   accertamento_scadenzario.pg_accertamento,
                   accertamento_scadenzario.pg_accertamento_scadenzario;
