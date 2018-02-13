--------------------------------------------------------
--  DDL for View VP_DPDG_SPE_CDR_PRIMO
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VP_DPDG_SPE_CDR_PRIMO" ("ESERCIZIO", "CD_CENTRO_RESPONSABILITA", "DS_CDR", "CD_UNITA_ORGANIZZATIVA", "DS_UNITA_ORGANIZZATIVA", "CD_CDS", "DS_CDS", "STATO", "ANNOTAZIONI", "FL_RIBALTATO_SU_AREA", "TI_APPARTENENZA", "TI_GESTIONE", "CD_ELEMENTO_VOCE", "DS_ELEMENTO_VOCE", "CD_TITOLO", "DS_TITOLO", "CD_CAPOCONTO", "DS_CAPOCONTO", "CD_FUNZIONE", "DS_FUNZIONE", "CD_NATURA", "DS_NATURA", "IM_RH_CCS_COSTI", "IM_RI_CCS_SPESE_ODC", "IM_RJ_CCS_SPESE_ODC_ALTRA_UO", "IM_RK_CCS_SPESE_OGC", "IM_RL_CCS_SPESE_OGC_ALTRA_UO", "IM_RM_CSS_AMMORTAMENTI", "IM_RN_CSS_RIMANENZE", "IM_RO_CSS_ALTRI_COSTI", "IM_RP_CSS_VERSO_ALTRO_CDR", "IM_RQ_SSC_COSTI_ODC", "IM_RR_SSC_COSTI_ODC_ALTRA_UO", "IM_RS_SSC_COSTI_OGC", "IM_RT_SSC_COSTI_OGC_ALTRA_UO", "IM_RU_SPESE_COSTI_ALTRUI", "IM_RV_PAGAMENTI", "IM_RAA_A2_COSTI_FINALI", "IM_RAB_A2_COSTI_ALTRO_CDR", "IM_RAC_A2_SPESE_ODC", "IM_RAD_A2_SPESE_ODC_ALTRA_UO", "IM_RAE_A2_SPESE_OGC", "IM_RAF_A2_SPESE_OGC_ALTRA_UO", "IM_RAG_A2_SPESE_COSTI_ALTRUI", "IM_RAH_A3_COSTI_FINALI", "IM_RAI_A3_COSTI_ALTRO_CDR", "IM_RAL_A3_SPESE_ODC", "IM_RAM_A3_SPESE_ODC_ALTRA_UO", "IM_RAN_A3_SPESE_OGC", "IM_RAO_A3_SPESE_OGC_ALTRA_UO", "IM_RAP_A3_SPESE_COSTI_ALTRUI") AS 
  (
select
--
-- Date: 24/02/2003
-- Version: 1.2
--
-- Vista di stampa dei dettagli di spesa CDR aggregati per CDR di primo livello con elisione dei ricavi figurativi
--
-- History:
--
-- Date: 07/09/2001
-- Version: 1.0
-- Creazione
--
-- Date: 23/12/2002
-- Version: 1.1
-- Fix per estrazione dati di scarico verso altra UO (errore 412)
--
-- Date: 24/02/2003
-- Version: 1.2
-- Ottimizzazione
--
-- Body:
--
 d.ESERCIZIO
,d.CD_CENTRO_RESPONSABILITA
,c_primo.DS_CDR
,c_primo.CD_UNITA_ORGANIZZATIVA
,b.DS_UNITA_ORGANIZZATIVA
,b.CD_UNITA_PADRE
,null
,d.STATO
,d.ANNOTAZIONI
,d.FL_RIBALTATO_SU_AREA
,e.TI_APPARTENENZA
,e.TI_GESTIONE
,e.CD_ELEMENTO_VOCE
,f.DS_ELEMENTO_VOCE
,ft.CD_ELEMENTO_VOCE
,ft.DS_ELEMENTO_VOCE
,cfin.CD_CAPOCONTO_FIN
,cfin.DS_CAPOCONTO_FIN
,e.cd_funzione
,i.ds_funzione
,e.cd_natura
,j.ds_natura
,sum(e.IM_RH_CCS_COSTI)
,sum(e.IM_RI_CCS_SPESE_ODC)
,sum(e.IM_RJ_CCS_SPESE_ODC_ALTRA_UO)
,sum(e.IM_RK_CCS_SPESE_OGC)
,sum(e.IM_RL_CCS_SPESE_OGC_ALTRA_UO)
,sum(e.IM_RM_CSS_AMMORTAMENTI)
,sum(e.IM_RN_CSS_RIMANENZE)
,sum(e.IM_RO_CSS_ALTRI_COSTI)
,sum(e.IM_RP_CSS_VERSO_ALTRO_CDR)
,sum(e.IM_RQ_SSC_COSTI_ODC)
,sum(e.IM_RR_SSC_COSTI_ODC_ALTRA_UO)
,sum(e.IM_RS_SSC_COSTI_OGC)
,sum(e.IM_RT_SSC_COSTI_OGC_ALTRA_UO)
,sum(e.IM_RU_SPESE_COSTI_ALTRUI)
,sum(e.IM_RV_PAGAMENTI)
,sum(e.IM_RAA_A2_COSTI_FINALI)
,sum(e.IM_RAB_A2_COSTI_ALTRO_CDR)
,sum(e.IM_RAC_A2_SPESE_ODC)
,sum(e.IM_RAD_A2_SPESE_ODC_ALTRA_UO)
,sum(e.IM_RAE_A2_SPESE_OGC)
,sum(e.IM_RAF_A2_SPESE_OGC_ALTRA_UO)
,sum(e.IM_RAG_A2_SPESE_COSTI_ALTRUI)
,sum(e.IM_RAH_A3_COSTI_FINALI)
,sum(e.IM_RAI_A3_COSTI_ALTRO_CDR)
,sum(e.IM_RAL_A3_SPESE_ODC)
,sum(e.IM_RAM_A3_SPESE_ODC_ALTRA_UO)
,sum(e.IM_RAN_A3_SPESE_OGC)
,sum(e.IM_RAO_A3_SPESE_OGC_ALTRA_UO)
,sum(e.IM_RAP_A3_SPESE_COSTI_ALTRUI)
from
PDG_PREVENTIVO d, --Testata pdg cd_di primo livello
CDR c_primo, -- Cdr di primo livello
PDG_PREVENTIVO_SPE_DET e, -- Dettaglio
CDR c, -- Cdr pdg
UNITA_ORGANIZZATIVA b, -- UO di afferenza del CDR di primo livello
ELEMENTO_VOCE f, -- Elemento voce del dettaglio
ELEMENTO_VOCE ft, -- Titolo del dettaglio
CAPOCONTO_FIN cfin, -- Capoconto finanziario
FUNZIONE i, -- Funzione
NATURA j -- Natura
where
    b.fl_cds = 'N'
and b.CD_UNITA_ORGANIZZATIVA = c_primo.CD_UNITA_ORGANIZZATIVA
and d.CD_CENTRO_RESPONSABILITA=c_primo.CD_CENTRO_RESPONSABILITA
and (
    c.cd_cdr_afferenza = c_primo.CD_CENTRO_RESPONSABILITA
 or c.cd_centro_responsabilita = c_primo.cd_centro_responsabilita
)
and e.CD_CENTRO_RESPONSABILITA=c.CD_CENTRO_RESPONSABILITA
and e.STATO = 'Y'
and e.esercizio = d.esercizio
and (
      e.categoria_dettaglio = 'SIN'	  -- Singoli
   or e.categoria_dettaglio = 'CAR' -- Di carico
   or e.categoria_dettaglio = 'SCR' and e.cd_centro_responsabilita_clgs is not null -- Di scarico verso altra UO
   or (
            e.categoria_dettaglio = 'SCR'
		and e.cd_centro_responsabilita_clge is not null -- Scarichi verso altro CDR (costi) solo fatti verso CDR al di fuori del proprio CDR di primo livello
        and e.cd_centro_responsabilita_clge <> c_primo.cd_centro_responsabilita
	    and not exists (select 1 from cdr c_cdr_coll where
              c_cdr_coll.cd_Centro_Responsabilita = e.cd_centro_responsabilita_clge
		  and c_cdr_coll.cd_cdr_afferenza = c_primo.cd_centro_responsabilita
	    )
   )
)
and f.ESERCIZIO=d.ESERCIZIO
and f.TI_APPARTENENZA=e.TI_APPARTENENZA
and f.TI_GESTIONE=e.TI_GESTIONE
and f.CD_ELEMENTO_VOCE=e.CD_ELEMENTO_VOCE
and ft.ESERCIZIO=f.ESERCIZIO
and ft.TI_APPARTENENZA=f.TI_APPARTENENZA
and ft.TI_GESTIONE=f.TI_GESTIONE
and ft.CD_ELEMENTO_VOCE=f.CD_ELEMENTO_PADRE
and cfin.CD_CAPOCONTO_FIN(+)=f.CD_CAPOCONTO_FIN
and i.cd_funzione = e.cd_funzione
and j.cd_natura = e.cd_natura
group by
 d.ESERCIZIO
,d.CD_CENTRO_RESPONSABILITA
,c_primo.DS_CDR
,c_primo.CD_UNITA_ORGANIZZATIVA
,b.DS_UNITA_ORGANIZZATIVA
,b.CD_UNITA_PADRE
,null
,d.STATO
,d.ANNOTAZIONI
,d.FL_RIBALTATO_SU_AREA
,e.TI_APPARTENENZA
,e.TI_GESTIONE
,e.CD_ELEMENTO_VOCE
,f.DS_ELEMENTO_VOCE
,ft.CD_ELEMENTO_VOCE
,ft.DS_ELEMENTO_VOCE
,cfin.CD_CAPOCONTO_FIN
,cfin.DS_CAPOCONTO_FIN
,e.cd_funzione
,i.ds_funzione
,e.cd_natura
,j.ds_natura
)
;

   COMMENT ON TABLE "VP_DPDG_SPE_CDR_PRIMO"  IS 'Vista di stampa dei dettagli di spesa CDR aggregati per CDR di primo livello con elisione dei ricavi figurativi';
