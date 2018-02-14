--------------------------------------------------------
--  DDL for View V_ACCERTAMENTO_IM_REVERSALE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_ACCERTAMENTO_IM_REVERSALE" ("ESERCIZIO", "ESERCIZIO_ORIGINALE", "CD_CDS", "PG_ACCERTAMENTO", "CD_TIPO_DOCUMENTO_CONT", "CD_UNITA_ORGANIZZATIVA", "CD_CDS_ORIGINE", "CD_UO_ORIGINE", "ESERCIZIO_COMPETENZA", "TI_APPARTENENZA", "TI_GESTIONE", "CD_ELEMENTO_VOCE", "CD_VOCE", "DT_REGISTRAZIONE", "DS_ACCERTAMENTO", "NOTE_ACCERTAMENTO", "CD_TERZO", "IM_ACCERTAMENTO", "DT_CANCELLAZIONE", "CD_RIFERIMENTO_CONTRATTO", "DT_SCADENZA_CONTRATTO", "CD_FONDO_RICERCA", "FL_PGIRO", "DACR", "UTCR", "DUVA", "UTUV", "PG_VER_REC", "RIPORTATO", "CD_CDS_ORI_RIPORTO", "ESERCIZIO_ORI_RIPORTO", "ESERCIZIO_ORI_ORI_RIPORTO", "PG_ACCERTAMENTO_ORI_RIPORTO", "PG_ACCERTAMENTO_ORIGINE", "ESERCIZIO_CONTRATTO", "STATO_CONTRATTO", "PG_CONTRATTO", "FL_CALCOLO_AUTOMATICO", "FL_NETTO_SOSPESO", "ESERCIZIO_EV_NEXT", "TI_GESTIONE_EV_NEXT", "TI_APPARTENENZA_EV_NEXT", "CD_ELEMENTO_VOCE_NEXT", "IM_ASSOCIATO_DOC_AMM", "IM_REVERSALE") AS 
  SELECT
--
-- Date: 19/07/2006
-- Version: 1.6
--
-- Vista di estrazione degli accertamenti con la totalizzione dell'importo associato alle reversali e
-- dell'importo associato ai documenti amministrativi
--
-- History:
--
-- Date: 18/02/2002
-- Version: 1.0
-- Creazione
--
-- Date: 20/05/2002
-- Version: 1.1
-- Aggiunto esercizio_competenza
--
-- Date: 09/10/2002
-- Version: 1.2
-- Aggiunto im_associato_doc_amm
--
-- Date: 25/02/2003
-- Version: 1.3
-- Aggiunto PG_ACCERTAMENTO_ORIGINE per gestire la segnalazione 426
--
-- Date: 25/02/2003
-- Version: 1.3
-- Aggiunto ESERCIZIO_CONTRATTO,STATO_CONTRATTO,PG_CONTRATTO
--
-- Date: 12/01/2006
-- Version: 1.5
-- Gestione Residui - Aggiunto il campo ESERCIZIO_ORIGINALE
--
-- Date: 19/07/2006
-- Version: 1.6
-- Gestione Impegni/Accertamenti Residui:
-- gestito il nuovo campo ESERCIZIO_ORIGINALE
--
-- Date: 12/05/2008
-- Version: 1.6
-- Gestione Impegni/Accertamenti: gestito il nuovo campo FL_NETTO_SOSPESO
--
-- Date: 11/11/2016
-- Version: 1.7
-- Gestione Impegni/Accertamenti: gestiti i campi voce_next
--
-- Body:
--
            a.esercizio, a.esercizio_originale, a.cd_cds, a.pg_accertamento,
            a.cd_tipo_documento_cont, a.cd_unita_organizzativa,
            a.cd_cds_origine, a.cd_uo_origine, a.esercizio_competenza,
            a.ti_appartenenza, a.ti_gestione, a.cd_elemento_voce, a.cd_voce,
            a.dt_registrazione, a.ds_accertamento, a.note_accertamento,
            a.cd_terzo, a.im_accertamento, a.dt_cancellazione,
            a.cd_riferimento_contratto, a.dt_scadenza_contratto,
            a.cd_fondo_ricerca, a.fl_pgiro, a.dacr, a.utcr, a.duva, a.utuv,
            a.pg_ver_rec, a.riportato, a.cd_cds_ori_riporto,
            a.esercizio_ori_riporto, a.esercizio_ori_ori_riporto,
            a.pg_accertamento_ori_riporto, a.pg_accertamento_origine,
            a.esercizio_contratto, a.stato_contratto, a.pg_contratto,
            a.fl_calcolo_automatico, a.fl_netto_sospeso,
            a.esercizio_ev_next, a.ti_gestione_ev_next,
            a.ti_appartenenza_ev_next, a.cd_elemento_voce_next,
            SUM (b.im_associato_doc_amm), SUM (b.im_associato_doc_contabile)
       FROM accertamento a, accertamento_scadenzario b
      WHERE a.cd_cds = b.cd_cds
        AND a.esercizio = b.esercizio
        AND a.esercizio_originale = b.esercizio_originale
        AND a.pg_accertamento = b.pg_accertamento
   GROUP BY a.esercizio,
            a.esercizio_originale,
            a.cd_cds,
            a.pg_accertamento,
            a.cd_tipo_documento_cont,
            a.cd_unita_organizzativa,
            a.cd_cds_origine,
            a.cd_uo_origine,
            a.esercizio_competenza,
            a.ti_appartenenza,
            a.ti_gestione,
            a.cd_elemento_voce,
            a.cd_voce,
            a.dt_registrazione,
            a.ds_accertamento,
            a.note_accertamento,
            a.cd_terzo,
            a.im_accertamento,
            a.dt_cancellazione,
            a.cd_riferimento_contratto,
            a.dt_scadenza_contratto,
            a.cd_fondo_ricerca,
            a.fl_pgiro,
            a.dacr,
            a.utcr,
            a.duva,
            a.utuv,
            a.pg_ver_rec,
            a.riportato,
            a.cd_cds_ori_riporto,
            a.esercizio_ori_riporto,
            a.esercizio_ori_ori_riporto,
            a.pg_accertamento_ori_riporto,
            a.pg_accertamento_origine,
            a.esercizio_contratto,
            a.stato_contratto,
            a.pg_contratto,
            a.fl_calcolo_automatico,
            a.fl_netto_sospeso,
            a.esercizio_ev_next,
            a.ti_gestione_ev_next,
            a.ti_appartenenza_ev_next,
            a.cd_elemento_voce_next;

   COMMENT ON TABLE "V_ACCERTAMENTO_IM_REVERSALE"  IS 'Vista di estrazione degli accertamenti con la totalizzazione dell''importo associato alle reversali';
