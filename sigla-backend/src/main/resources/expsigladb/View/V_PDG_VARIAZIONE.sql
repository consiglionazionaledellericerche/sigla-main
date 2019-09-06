--------------------------------------------------------
--  DDL for View V_PDG_VARIAZIONE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_PDG_VARIAZIONE" ("ESERCIZIO", "PG_VARIAZIONE_PDG", "DS_VARIAZIONE_PDG", "RIFERIMENTI", "CD_CDR_PROPONENTE", "DS_CDR_PROPONENTE", "CD_CDR", "DS_CDR", "ENTRATA", "SPESA", "STATO", "QUOTA_RIPARTITA_SPE", "QUOTA_RIPARTITA_ETR","PG_PROGETTO_RIMODULAZIONE","PG_RIMODULAZIONE") AS 
  select /*+ RULE*/
--
-- Date: 04/10/2010
-- Version: 1.2
--
-- Vista di estrazione dei dati per la stampa Variazione al PdG
--
-- History:
--
-- Date: 06/10/2005
-- Version: 1.0
-- Creazione
--
-- Date: 16/05/2006
-- Version: 1.1
-- Aggiunte le nuove tabelle di gestione del PDG
--
-- Date: 04/10/2010
-- Version: 1.2
-- Aggiunte il campo Riferimeti per inserirlo nella stampa della variazione a seguito del rilascio della Firma Digitale
--
-- Body:
--
pdg.ESERCIZIO,
pdg.PG_VARIAZIONE_PDG,
pdg.DS_VARIAZIONE,
pdg.RIFERIMENTI,
pdg.CD_CENTRO_RESPONSABILITA CD_CDR_PROPONENTE,
cdr_prop.DS_CDR DS_CDR_PROPONENTE,
assPdg.CD_CENTRO_RESPONSABILITA CD_CDR,
cdr.DS_CDR DS_CDR,
Nvl(assPdg.IM_ENTRATA, 0) ENTRATA,
Nvl(assPdg.IM_SPESA, 0) SPESA,
pdg.STATO STATO_TESTATA,
Decode(par.fl_regolamento_2006, 'N',
          (Select Nvl(Sum(Nvl(Im_ri_ccs_spese_odc, 0) + Nvl(Im_rj_ccs_spese_odc_altra_uo, 0) +
                          Nvl(Im_rk_ccs_spese_ogc, 0) + Nvl(Im_rl_ccs_spese_ogc_altra_uo, 0) +
                          Nvl(Im_rq_ssc_costi_odc, 0) + Nvl(Im_rr_ssc_costi_odc_altra_uo, 0) +
                          Nvl(Im_rs_ssc_costi_ogc, 0) + Nvl(Im_rt_ssc_costi_ogc_altra_uo, 0)), 0)
           From pdg_preventivo_spe_det s
           Where s.esercizio = pdg.esercizio
           And   s.pg_variazione_pdg = pdg.pg_variazione_pdg
           And   s.cd_centro_responsabilita = assPdg.cd_centro_responsabilita),
          (Select Nvl(Sum(Nvl(Im_spese_gest_decentrata_int, 0) + Nvl(Im_spese_gest_decentrata_est, 0) +
                          Nvl(Im_spese_gest_accentrata_int, 0) + Nvl(Im_spese_gest_accentrata_est, 0)), 0)
           From pdg_variazione_riga_gest pdgVar
           Where pdgVar.esercizio = pdg.esercizio
           And   pdgVar.pg_variazione_pdg = pdg.pg_variazione_pdg
           And   pdgVar.cd_cdr_assegnatario = assPdg.cd_centro_responsabilita
           And   pdgVar.categoria_dettaglio != 'SCR'
           And   pdgVar.ti_gestione = 'S'))  QUOTA_RIPARTITA_SPE,
Decode(par.fl_regolamento_2006, 'N',
          (Select Nvl(Sum(Nvl(Im_ra_rce, 0) + Nvl(Im_rc_esr, 0)), 0) IMPORTO_ETR
           From pdg_preventivo_etr_det s
           Where s.esercizio = pdg.esercizio
           And   s.pg_variazione_pdg = pdg.pg_variazione_pdg
           And   s.cd_centro_responsabilita = assPdg.cd_centro_responsabilita),
          (Select Nvl(Sum(Im_entrata), 0)
           From pdg_variazione_riga_gest pdgVar
           Where pdgVar.esercizio = pdg.esercizio
           And   pdgVar.pg_variazione_pdg = pdg.pg_variazione_pdg
           And   pdgVar.cd_cdr_assegnatario = assPdg.cd_centro_responsabilita
           And   pdgVar.categoria_dettaglio != 'SCR'
           And   pdgVar.ti_gestione = 'E')) QUOTA_RIPARTITA_ETR,
pdg.pg_progetto_rimodulazione,
pdg.pg_rimodulazione
From pdg_variazione pdg, ass_pdg_variazione_cdr assPdg, cdr cdr_prop, cdr, parametri_cnr par
Where pdg.esercizio = assPdg.esercizio
And   pdg.pg_variazione_pdg = assPdg.pg_variazione_pdg
And   pdg.cd_centro_responsabilita = cdr_prop.cd_centro_responsabilita
And   assPdg.cd_centro_responsabilita = cdr.cd_centro_responsabilita
And   pdg.esercizio = par.esercizio;
