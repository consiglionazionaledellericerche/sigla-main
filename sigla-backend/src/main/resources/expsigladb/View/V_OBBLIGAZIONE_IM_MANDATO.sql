--------------------------------------------------------
--  DDL for View V_OBBLIGAZIONE_IM_MANDATO
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_OBBLIGAZIONE_IM_MANDATO" ("ESERCIZIO", "ESERCIZIO_ORIGINALE", "CD_CDS", "PG_OBBLIGAZIONE", "CD_TIPO_DOCUMENTO_CONT", "CD_UNITA_ORGANIZZATIVA", "CD_CDS_ORIGINE", "CD_UO_ORIGINE", "CD_TIPO_OBBLIGAZIONE", "TI_APPARTENENZA", "TI_GESTIONE", "CD_ELEMENTO_VOCE", "DT_REGISTRAZIONE", "DS_OBBLIGAZIONE", "NOTE_OBBLIGAZIONE", "CD_TERZO", "IM_OBBLIGAZIONE", "IM_COSTI_ANTICIPATI", "ESERCIZIO_COMPETENZA", "STATO_OBBLIGAZIONE", "DT_CANCELLAZIONE", "CD_RIFERIMENTO_CONTRATTO", "DT_SCADENZA_CONTRATTO", "FL_CALCOLO_AUTOMATICO", "CD_FONDO_RICERCA", "FL_SPESE_COSTI_ALTRUI", "FL_PGIRO", "DACR", "UTCR", "DUVA", "UTUV", "PG_VER_REC", "RIPORTATO", "CD_CDS_ORI_RIPORTO", "ESERCIZIO_ORI_RIPORTO", "ESERCIZIO_ORI_ORI_RIPORTO", "PG_OBBLIGAZIONE_ORI_RIPORTO", "ESERCIZIO_CONTRATTO", "STATO_CONTRATTO", "PG_CONTRATTO", "MOTIVAZIONE", "ESERCIZIO_REP", "PG_REPERTORIO", "FL_NETTO_SOSPESO", "FL_GARA_IN_CORSO", "DS_GARA_IN_CORSO", "ESERCIZIO_EV_NEXT", "TI_APPARTENENZA_EV_NEXT", "TI_GESTIONE_EV_NEXT", "CD_ELEMENTO_VOCE_NEXT", "IM_MANDATO", "FL_DETERMINA_ALLEGATA", "DT_DETERMINA_ALLEGATA") AS
  (SELECT   /*+ optimizer_features_enable('10.1.0') */
--
-- Date: 12/07/2010
-- Version: 1.7
--
-- Vista di estrazione delle obbligazione con la totalizzione dell'importo associato ai mandati
--
-- History:
--
-- Date: 30/01/2002
-- Version: 1.0
-- Creazione
--
-- Date: 01/02/2002
-- Version: 1.1
-- Aggiunti i campi RIPORTATO, CD_CDS_ORI_RIPORTO, ESERCIZIO_ORI_RIPORTO, ESERCIZIO_ORI_ORI_RIPORTO, PG_OBBLIGAZIONE_ORI_RIPORTO
--
-- Date: 14/04/2005
-- Version: 1.2
-- Aggiunti i campi ESERCIZIO_CONTRATTO, STATO_CONTRATTO, PG_CONTRATTO
--
-- Date: 12/01/2006
-- Version: 1.3
-- Gestione Residui - Aggiunto il campo ESERCIZIO_ORIGINALE
--
-- Date: 18/07/2006
-- Version: 1.4
-- Gestione Impegni/Accertamenti Residui:
-- gestito il nuovo campo ESERCIZIO_ORIGINALE
--
-- Date: 29/11/2007
-- Version: 1.5
-- Gestione Impegni/Accertamenti Residui:
-- gestito i nuovi campi ESERCIZIO_REP, PG_REPERTORIO
--
-- Date: 12/05/2008
-- Version: 1.6
-- Gestione Impegni/Accertamenti: gestito il nuovo campo FL_NETTO_SOSPESO
--
-- Date: 12/07/2010
-- Version: 1.7
-- Gestione Impegni: gestito il nuovo campo FL_GARA_IN_CORSO e DS_GARA_IN_CORSO
--
-- Body:
--
             a.esercizio, a.esercizio_originale, a.cd_cds, a.pg_obbligazione,
             a.cd_tipo_documento_cont, a.cd_unita_organizzativa,
             a.cd_cds_origine, a.cd_uo_origine, a.cd_tipo_obbligazione,
             a.ti_appartenenza, a.ti_gestione, a.cd_elemento_voce,
             a.dt_registrazione, a.ds_obbligazione, a.note_obbligazione,
             a.cd_terzo, a.im_obbligazione, a.im_costi_anticipati,
             a.esercizio_competenza, a.stato_obbligazione, a.dt_cancellazione,
             a.cd_riferimento_contratto, a.dt_scadenza_contratto,
             a.fl_calcolo_automatico, a.cd_fondo_ricerca,
             a.fl_spese_costi_altrui, a.fl_pgiro, a.dacr, a.utcr, a.duva,
             a.utuv, a.pg_ver_rec, a.riportato, a.cd_cds_ori_riporto,
             a.esercizio_ori_riporto, a.esercizio_ori_ori_riporto,
             a.pg_obbligazione_ori_riporto, a.esercizio_contratto,
             a.stato_contratto, a.pg_contratto, a.motivazione,
             a.esercizio_rep, a.pg_repertorio, a.fl_netto_sospeso,
             a.fl_gara_in_corso, a.ds_gara_in_corso, a.fl_determina_allegata, a.dt_determina_allegata,
             a.esercizio_ev_next, a.ti_appartenenza_ev_next, a.ti_gestione_ev_next,
             a.cd_elemento_voce_next,
             SUM (b.im_associato_doc_contabile)
        FROM obbligazione a, obbligazione_scadenzario b
       WHERE a.cd_cds = b.cd_cds
         AND a.esercizio = b.esercizio
         AND a.esercizio_originale = b.esercizio_originale
         AND a.pg_obbligazione = b.pg_obbligazione
    GROUP BY a.esercizio,
             a.esercizio_originale,
             a.cd_cds,
             a.pg_obbligazione,
             a.cd_tipo_documento_cont,
             a.cd_unita_organizzativa,
             a.cd_cds_origine,
             a.cd_uo_origine,
             a.cd_tipo_obbligazione,
             a.ti_appartenenza,
             a.ti_gestione,
             a.cd_elemento_voce,
             a.dt_registrazione,
             a.ds_obbligazione,
             a.note_obbligazione,
             a.cd_terzo,
             a.im_obbligazione,
             a.im_costi_anticipati,
             a.esercizio_competenza,
             a.stato_obbligazione,
             a.dt_cancellazione,
             a.cd_riferimento_contratto,
             a.dt_scadenza_contratto,
             a.fl_calcolo_automatico,
             a.cd_fondo_ricerca,
             a.fl_spese_costi_altrui,
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
             a.pg_obbligazione_ori_riporto,
             a.esercizio_contratto,
             a.stato_contratto,
             a.pg_contratto,
             a.motivazione,
             a.esercizio_rep,
             a.pg_repertorio,
             a.fl_netto_sospeso,
             a.fl_gara_in_corso,
             a.ds_gara_in_corso,
             a.fl_determina_allegata,
             a.dt_determina_allegata,
             a.esercizio_ev_next,
             a.ti_appartenenza_ev_next,
             a.ti_gestione_ev_next,
             a.cd_elemento_voce_next);

   COMMENT ON TABLE "V_OBBLIGAZIONE_IM_MANDATO"  IS '-- Vista di estrazione delle obbligazione con la totalizzazione
dell''importo associato ai mandati';
