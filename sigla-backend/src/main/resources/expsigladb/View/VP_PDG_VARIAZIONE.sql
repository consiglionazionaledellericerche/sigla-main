/* Formatted on 2018/02/16 09:47 (Formatter Plus v4.8.8) */
CREATE OR REPLACE FORCE VIEW vp_pdg_variazione (esercizio,
                                                pg_variazione_pdg,
                                                cd_centro_responsabilita,
                                                ds_cdr,
                                                dt_apertura,
                                                dt_chiusura,
                                                dt_approvazione,
                                                dt_annullamento,
                                                ds_variazione,
                                                ds_delibera,
                                                stato,
                                                riferimenti,
                                                cd_causale_respinta,
                                                ds_causale_respinta,
                                                dacr,
                                                utcr,
                                                duva,
                                                utuv,
                                                pg_ver_rec,
                                                dt_app_formale,
                                                tipologia,
                                                tipologia_fin,
                                                ti_appartenenza,
                                                ti_gestione,
                                                cd_elemento_voce,
                                                fl_visto_dip_variazioni,
                                                stato_invio,
                                                dt_firma,
                                                ti_motivazione_variazione,
                                                id_matricola,
                                                id_bando,
                                                ds_causale,
                                                pg_progetto_rimodulazione,
                                                pg_rimodulazione,
                                                fl_cda,
                                                assegnazione,
                                                im_incassato,
                                                note
                                               )
AS
   SELECT
--
-- Date: 21/11/2006
-- Version: 1.0
--
-- Vista per la visualizzazione della descrizione del cdr nello stanziamento di competenza
--
-- History:
--
-- Date: 21/11/2006
-- Version: 1.0
-- Creazione
--
--
-- Body:
--
          v.esercizio, v.pg_variazione_pdg, v.cd_centro_responsabilita,
          c.ds_cdr, v.dt_apertura, v.dt_chiusura, v.dt_approvazione,
          v.dt_annullamento, v.ds_variazione, v.ds_delibera, v.stato,
          v.riferimenti, v.cd_causale_respinta, v.ds_causale_respinta, v.dacr,
          v.utcr, v.duva, v.utuv, v.pg_ver_rec, v.dt_app_formale, v.tipologia,
          v.tipologia_fin, v.ti_appartenenza, v.ti_gestione,
          v.cd_elemento_voce, v.fl_visto_dip_variazioni, v.stato_invio,
          v.dt_firma, v.ti_motivazione_variazione, v.id_matricola, v.id_bando,v.ds_causale,
          v.pg_progetto_rimodulazione, v.pg_rimodulazione, v.fl_cda, v.assegnazione,
          v.im_incassato, v.note
     FROM pdg_variazione v, cdr c
    WHERE v.cd_centro_responsabilita = c.cd_centro_responsabilita;
