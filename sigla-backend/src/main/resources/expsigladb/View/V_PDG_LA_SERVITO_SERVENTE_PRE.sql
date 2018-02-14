--------------------------------------------------------
--  DDL for View V_PDG_LA_SERVITO_SERVENTE_PRE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_PDG_LA_SERVITO_SERVENTE_PRE" ("CD_UO_SERVENTE", "CD_CDR_SERVENTE", "CD_LA_SERVENTE", "CD_CDR_SERVITO", "CD_LA_SERVITO", "IM_A1", "IM_A2", "IM_A3") AS 
  select
--
-- Date: 23/09/2002
-- Version: 1.0
--
-- Vista che estrae le quote previsionali scaricate da una la di servito su diversi serventi
-- La vista raggruppa logicamente i serventi per uo di appartenenza
--
-- History:
--
-- Date: 23/09/2002
-- Version: 1.0
-- Creazione
--
-- Body:
--
                 b.cd_unita_organizzativa cd_uo_servente,
				 a.cd_centro_responsabilita cd_cdr_servente,
				 a.cd_linea_attivita cd_la_servente,
                 a.cd_centro_responsabilita_clgs cd_cdr_servito,
                 a.cd_linea_attivita_clgs cd_la_servito,
                 sum(a.IM_RU_SPESE_COSTI_ALTRUI) im_a1,
                 sum(a.IM_RAG_A2_SPESE_COSTI_ALTRUI) im_a2,
                 sum(a.IM_RAP_A3_SPESE_COSTI_ALTRUI) im_a3
                from
                 pdg_preventivo_spe_det a, cdr b
                where
                     a.cd_centro_responsabilita = b.cd_centro_responsabilita
				 and a.cd_centro_responsabilita_clgs is not null
				 and a.categoria_dettaglio = 'CAR'
				 and a.stato = 'Y'
                group by
                 b.cd_unita_organizzativa,
				 a.cd_centro_responsabilita,
				 a.cd_linea_attivita,
                 a.cd_centro_responsabilita_clgs,
                 a.cd_linea_attivita_clgs
;

   COMMENT ON TABLE "V_PDG_LA_SERVITO_SERVENTE_PRE"  IS 'Vista che estrae le quote previsionali scaricate da una la di servito su diversi serventi
La vista raggruppa logicamente i serventi per uo di appartenenza';
