--------------------------------------------------------
--  DDL for View V_DPDG_AGGREGATO_SPE_DET
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_DPDG_AGGREGATO_SPE_DET" ("ESERCIZIO", "CD_CENTRO_RESPONSABILITA", "CD_CDS", "TI_APPARTENENZA", "TI_GESTIONE", "CD_ELEMENTO_VOCE", "TI_AGGREGATO", "CD_FUNZIONE", "CD_NATURA", "IM_RH_CCS_COSTI", "IM_RI_CCS_SPESE_ODC", "IM_RJ_CCS_SPESE_ODC_ALTRA_UO", "IM_RK_CCS_SPESE_OGC", "IM_RL_CCS_SPESE_OGC_ALTRA_UO", "IM_RM_CSS_AMMORTAMENTI", "IM_RN_CSS_RIMANENZE", "IM_RO_CSS_ALTRI_COSTI", "IM_RP_CSS_VERSO_ALTRO_CDR", "IM_RQ_SSC_COSTI_ODC", "IM_RR_SSC_COSTI_ODC_ALTRA_UO", "IM_RS_SSC_COSTI_OGC", "IM_RT_SSC_COSTI_OGC_ALTRA_UO", "IM_RU_SPESE_COSTI_ALTRUI", "IM_RV_PAGAMENTI", "IM_RAA_A2_COSTI_FINALI", "IM_RAB_A2_COSTI_ALTRO_CDR", "IM_RAC_A2_SPESE_ODC", "IM_RAD_A2_SPESE_ODC_ALTRA_UO", "IM_RAE_A2_SPESE_OGC", "IM_RAF_A2_SPESE_OGC_ALTRA_UO", "IM_RAG_A2_SPESE_COSTI_ALTRUI", "IM_RAH_A3_COSTI_FINALI", "IM_RAI_A3_COSTI_ALTRO_CDR", "IM_RAL_A3_SPESE_ODC", "IM_RAM_A3_SPESE_ODC_ALTRA_UO", "IM_RAN_A3_SPESE_OGC", "IM_RAO_A3_SPESE_OGC_ALTRA_UO", "IM_RAP_A3_SPESE_COSTI_ALTRUI", "DACR", "UTCR", "DUVA", "UTUV", "PG_VER_REC") AS 
  (select
--
-- Date: 10/04/2003
-- Version: 1.4
--
-- Aggrega i piani di gestione al primo livello ripartendo i dettagli
-- di natura 5 tra le diverse aree di afferenza (cd_cds)
-- La vista non si preoccupa di verificare la validita STO
--
-- History:
--
-- Date: 15/09/2001
-- Version: 1.0
-- Creazione
-- Date: 02/10/2001
-- Version: 1.1
-- Aggiunta la gestione di aggregazione delle aree di ricerca
-- Modifiche strutturali per introduzione del CD_CDS per gestione natura 5
-- Date: 22/10/2001
-- Version: 1.2
-- Aggiunto ti_aggregato = 'D' per compatibilita con PDG_AGGREGATO_SPE_DET
-- Date: 08/11/2001
-- Version: 1.3
-- Eliminazione eercizio da STO
-- Date: 10/04/2003
-- Version: 1.4
-- Ottimizzazione vista con uso di stored procedure
--
-- Body:
--
 R.ESERCIZIO
,getCdrPrimo(R.CD_CENTRO_RESPONSABILITA)
,R.CD_CDS
,R.ti_appartenenza
,R.ti_gestione
,R.cd_elemento_voce
,'D'
,R.CD_FUNZIONE
,R.CD_NATURA
,sum(R.IM_RH_CCS_COSTI)
,sum(R.IM_RI_CCS_SPESE_ODC)
,sum(R.IM_RJ_CCS_SPESE_ODC_ALTRA_UO)
,sum(R.IM_RK_CCS_SPESE_OGC)
,sum(R.IM_RL_CCS_SPESE_OGC_ALTRA_UO)
,sum(R.IM_RM_CSS_AMMORTAMENTI)
,sum(R.IM_RN_CSS_RIMANENZE)
,sum(R.IM_RO_CSS_ALTRI_COSTI)
,sum(R.IM_RP_CSS_VERSO_ALTRO_CDR)
,sum(R.IM_RQ_SSC_COSTI_ODC)
,sum(R.IM_RR_SSC_COSTI_ODC_ALTRA_UO)
,sum(R.IM_RS_SSC_COSTI_OGC)
,sum(R.IM_RT_SSC_COSTI_OGC_ALTRA_UO)
,sum(R.IM_RU_SPESE_COSTI_ALTRUI)
,sum(R.IM_RV_PAGAMENTI)
,sum(R.IM_RAA_A2_COSTI_FINALI)
,sum(R.IM_RAB_A2_COSTI_ALTRO_CDR)
,sum(R.IM_RAC_A2_SPESE_ODC)
,sum(R.IM_RAD_A2_SPESE_ODC_ALTRA_UO)
,sum(R.IM_RAE_A2_SPESE_OGC)
,sum(R.IM_RAF_A2_SPESE_OGC_ALTRA_UO)
,sum(R.IM_RAG_A2_SPESE_COSTI_ALTRUI)
,sum(R.IM_RAH_A3_COSTI_FINALI)
,sum(R.IM_RAI_A3_COSTI_ALTRO_CDR)
,sum(R.IM_RAL_A3_SPESE_ODC)
,sum(R.IM_RAM_A3_SPESE_ODC_ALTRA_UO)
,sum(R.IM_RAN_A3_SPESE_OGC)
,sum(R.IM_RAO_A3_SPESE_OGC_ALTRA_UO)
,sum(R.IM_RAP_A3_SPESE_COSTI_ALTRUI)
,NULL
,NULL
,NULL
,NULL
,0
from
 V_DPDG_AGGREGATO_SPE_DETCDR R -- Vista di aggregazione per CDR
group by
 R.ESERCIZIO
,getCdrPrimo(R.CD_CENTRO_RESPONSABILITA)
,R.CD_CDS
,R.TI_APPARTENENZA
,R.TI_GESTIONE
,R.CD_ELEMENTO_VOCE
,R.CD_FUNZIONE
,R.CD_NATURA
)
;

   COMMENT ON TABLE "V_DPDG_AGGREGATO_SPE_DET"  IS 'Vista di aggregazione a primo livello della parte spese PDG per titolo/funzione/natura
La vista non si preoccupa di verificare la validità STO';
