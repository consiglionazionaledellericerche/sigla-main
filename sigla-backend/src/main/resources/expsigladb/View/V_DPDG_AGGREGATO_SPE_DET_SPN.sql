--------------------------------------------------------
--  DDL for View V_DPDG_AGGREGATO_SPE_DET_SPN
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_DPDG_AGGREGATO_SPE_DET_SPN" ("ESERCIZIO", "CD_CENTRO_RESPONSABILITA", "CD_CDS", "TI_APPARTENENZA", "TI_GESTIONE", "CD_ELEMENTO_VOCE", "TI_AGGREGATO", "CD_FUNZIONE", "CD_NATURA", "IM_RH_CCS_COSTI", "IM_RI_CCS_SPESE_ODC", "IM_RJ_CCS_SPESE_ODC_ALTRA_UO", "IM_RK_CCS_SPESE_OGC", "IM_RL_CCS_SPESE_OGC_ALTRA_UO", "IM_RM_CSS_AMMORTAMENTI", "IM_RN_CSS_RIMANENZE", "IM_RO_CSS_ALTRI_COSTI", "IM_RP_CSS_VERSO_ALTRO_CDR", "IM_RQ_SSC_COSTI_ODC", "IM_RR_SSC_COSTI_ODC_ALTRA_UO", "IM_RS_SSC_COSTI_OGC", "IM_RT_SSC_COSTI_OGC_ALTRA_UO", "IM_RU_SPESE_COSTI_ALTRUI", "IM_RV_PAGAMENTI", "IM_RAA_A2_COSTI_FINALI", "IM_RAB_A2_COSTI_ALTRO_CDR", "IM_RAC_A2_SPESE_ODC", "IM_RAD_A2_SPESE_ODC_ALTRA_UO", "IM_RAE_A2_SPESE_OGC", "IM_RAF_A2_SPESE_OGC_ALTRA_UO", "IM_RAG_A2_SPESE_COSTI_ALTRUI", "IM_RAH_A3_COSTI_FINALI", "IM_RAI_A3_COSTI_ALTRO_CDR", "IM_RAL_A3_SPESE_ODC", "IM_RAM_A3_SPESE_ODC_ALTRA_UO", "IM_RAN_A3_SPESE_OGC", "IM_RAO_A3_SPESE_OGC_ALTRA_UO", "IM_RAP_A3_SPESE_COSTI_ALTRUI", "DACR", "UTCR", "DUVA", "UTUV", "PG_VER_REC") AS 
  (select
--
-- Date: 29/08/2002
-- Version: 1.4
--
-- Effettua lo span a tutte le combinazioni di parametri di aggregazione su V_DPDG_AGGREGATO_SPE_DET
--
-- History:
--
-- Date: 22/12/2001
-- Version: 1.0
-- Creazione
--
-- Date: 18/02/2002
-- Version: 1.1
-- Corretto errore su gestione aggregazioni per AREA
--
-- Date: 26/03/2002
-- Version: 1.2
-- Ottimizzazione parte SAC natura = 5 => eliminato l'uso delle V_XXX_VALIDX
--
-- Date: 19/06/2002
-- Version: 1.3
-- Aggregazione per tipologia di intervento nel SAC con natura diversa da 5 e per titolo nel caso della natura 5
--
-- Date: 29/08/2002
-- Version: 1.4
-- Ottimizzazione generale => eliminate viste V_XXX_VALIDX
--
-- Body:
--
    ESERCIZIO
   ,CD_CENTRO_RESPONSABILITA
   ,CD_CDS
   ,ti_appartenenza
   ,ti_gestione
   ,cd_elemento_voce
   ,'D'
   ,CD_FUNZIONE
   ,CD_NATURA
   ,IM_RH_CCS_COSTI
   ,IM_RI_CCS_SPESE_ODC
   ,IM_RJ_CCS_SPESE_ODC_ALTRA_UO
   ,IM_RK_CCS_SPESE_OGC
   ,IM_RL_CCS_SPESE_OGC_ALTRA_UO
   ,IM_RM_CSS_AMMORTAMENTI
   ,IM_RN_CSS_RIMANENZE
   ,IM_RO_CSS_ALTRI_COSTI
   ,IM_RP_CSS_VERSO_ALTRO_CDR
   ,IM_RQ_SSC_COSTI_ODC
   ,IM_RR_SSC_COSTI_ODC_ALTRA_UO
   ,IM_RS_SSC_COSTI_OGC
   ,IM_RT_SSC_COSTI_OGC_ALTRA_UO
   ,IM_RU_SPESE_COSTI_ALTRUI
   ,IM_RV_PAGAMENTI
   ,IM_RAA_A2_COSTI_FINALI
   ,IM_RAB_A2_COSTI_ALTRO_CDR
   ,IM_RAC_A2_SPESE_ODC
   ,IM_RAD_A2_SPESE_ODC_ALTRA_UO
   ,IM_RAE_A2_SPESE_OGC
   ,IM_RAF_A2_SPESE_OGC_ALTRA_UO
   ,IM_RAG_A2_SPESE_COSTI_ALTRUI
   ,IM_RAH_A3_COSTI_FINALI
   ,IM_RAI_A3_COSTI_ALTRO_CDR
   ,IM_RAL_A3_SPESE_ODC
   ,IM_RAM_A3_SPESE_ODC_ALTRA_UO
   ,IM_RAN_A3_SPESE_OGC
   ,IM_RAO_A3_SPESE_OGC_ALTRA_UO
   ,IM_RAP_A3_SPESE_COSTI_ALTRUI
   ,NULL
   ,NULL
   ,NULL
   ,NULL
   ,0
from
   V_DPDG_AGGREGATO_SPE_DET
union all
-- Seleziono lo span dei sotto articoli di Macoristituto propri (natura != 5)
select
-- seleziona lo span a 0 da aggiungere
    a.ESERCIZIO
   ,d.cd_centro_responsabilita
   ,e.cd_unita_padre
   ,a.ti_appartenenza
   ,a.ti_gestione
   ,a.cd_elemento_voce
   ,'D'
   ,b.CD_FUNZIONE
   ,c.CD_NATURA
   ,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
   ,NULL
   ,NULL
   ,NULL
   ,NULL
   ,0
from
    ELEMENTO_VOCE a
   ,FUNZIONE b
   ,NATURA c
   ,CDR d
   ,UNITA_ORGANIZZATIVA e -- UO CDS di appartenenza del CDR di I livello
where
     a.ti_appartenenza='D'
 and a.ti_gestione = 'S'
 and (
  a.ti_elemento_voce = 'T'
 )
 and c.cd_natura != 5 -- Va bene solo per natura != 5
 and d.livello = 1
 and d.cd_unita_organizzativa=e.cd_unita_organizzativa
 and e.cd_tipo_unita in ('IST','PNIR')
 and exists (select 1 from ass_ev_funz_tipocds where
               esercizio = a.esercizio
               and cd_conto like a.cd_elemento_voce||'%'
			   and cd_funzione=b.cd_funzione
			   and cd_tipo_unita=e.cd_tipo_unita)
 and not exists (select 1 from V_DPDG_AGGREGATO_SPE_DET where
                  esercizio=a.esercizio
				  and cd_centro_responsabilita = d.cd_centro_responsabilita
                  and ti_gestione = a.TI_GESTIONE
				  and ti_appartenenza = a.TI_APPARTENENZA
                  and cd_elemento_voce = a.cd_elemento_voce
				  and cd_funzione = b.cd_funzione
                  and cd_natura = c.cd_natura
                  and cd_cds = e.cd_unita_padre
				 )
union all
-- Seleziono lo span dei sotto articoli di Macoristituto relativi all'AREA (natura 5)
select distinct
-- seleziona lo span a 0 da aggiungere
    a.ESERCIZIO
   ,d.cd_centro_responsabilita
   ,f.cd_area_ricerca
   ,a.ti_appartenenza
   ,a.ti_gestione
   ,a.cd_elemento_voce
   ,'D'
   ,b.CD_FUNZIONE
   ,c.CD_NATURA
   ,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
   ,NULL
   ,NULL
   ,NULL
   ,NULL
   ,0
from
    ELEMENTO_VOCE a
   ,FUNZIONE b
   ,NATURA c
   ,CDR d
   ,UNITA_ORGANIZZATIVA e -- UO CDS di appartenenza del CDR di I livello
   ,UNITA_ORGANIZZATIVA f -- UO afferente ad area sotto il CDS di e
--   ,V_UNITA_ORGANIZZATIVA g -- CDS AREA a cui f e collegato
where
     a.ti_appartenenza='D'
 and a.ti_gestione = 'S'
 and (
  a.ti_elemento_voce = 'T'
 )
 and c.cd_natura = 5 -- Va bene solo per natura = 5
 and d.cd_unita_organizzativa=e.cd_unita_organizzativa
 and d.livello = 1
 and e.cd_tipo_unita in ('IST','PNIR')
 and f.cd_unita_padre = e.cd_unita_padre
 and f.cd_area_ricerca is not null
-- and g.esercizio = a.esercizio
-- and g.cd_unita_organizzativa = f.cd_area_ricerca
-- and g.fl_cds = 'Y' -- g e  il CDS Area
 and exists (select 1 from ass_ev_funz_tipocds where
               esercizio = a.esercizio
               and cd_conto like a.cd_elemento_voce||'%'
			   and cd_funzione=b.cd_funzione
			   and cd_tipo_unita=e.cd_tipo_unita)
 and not exists (select 1 from V_DPDG_AGGREGATO_SPE_DET where
                  esercizio=a.esercizio
				  and cd_centro_responsabilita = d.cd_centro_responsabilita
                  and ti_gestione = a.TI_GESTIONE
				  and ti_appartenenza = a.TI_APPARTENENZA
                  and cd_elemento_voce = a.cd_elemento_voce
				  and cd_funzione = b.cd_funzione
                  and cd_natura = c.cd_natura
                  and cd_cds = f.cd_area_ricerca
				 )
union all
 select
 -- seleziona lo span a 0 da aggiungere
    a.ESERCIZIO
   ,d.cd_centro_responsabilita
   ,e.cd_unita_padre
   ,a.ti_appartenenza
   ,a.ti_gestione
   ,a.cd_elemento_voce
   ,'D'
   ,b.CD_FUNZIONE
   ,c.CD_NATURA
   ,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
   ,NULL
   ,NULL
   ,NULL
   ,NULL
   ,0
from
    ELEMENTO_VOCE a
   ,FUNZIONE b
   ,NATURA c
   ,CDR d
   ,UNITA_ORGANIZZATIVA e -- UO di appartenenza del CDR
where
     a.ti_appartenenza='D'
 and a.ti_gestione = 'S'
 and (
  a.ti_elemento_voce = 'T'
 )
 and d.cd_unita_organizzativa=e.cd_unita_organizzativa
 and e.cd_tipo_unita = 'AREA'
 and c.cd_natura != 5 -- L'area non aggrega sulla natura 5
 and exists (select 1 from ass_ev_funz_tipocds where
               esercizio = a.esercizio
               and cd_conto like a.cd_elemento_voce||'%'
			   and cd_funzione=b.cd_funzione
			   and cd_tipo_unita=e.cd_tipo_unita)
 and not exists (select 1 from V_DPDG_AGGREGATO_SPE_DET where
                  esercizio=a.esercizio
				  and cd_centro_responsabilita = d.cd_centro_responsabilita
                  and ti_gestione = a.TI_GESTIONE
				  and ti_appartenenza = a.TI_APPARTENENZA
                  and cd_elemento_voce = a.cd_elemento_voce
				  and cd_funzione = b.cd_funzione
                  and cd_natura = c.cd_natura
                  and cd_cds = e.cd_unita_padre
				 )
union all
 select
 -- seleziona lo span a 0 da aggiungere PER IL SAC natura != 5
    a.ESERCIZIO
   ,d.cd_centro_responsabilita
   ,e.cd_unita_padre
   ,a.ti_appartenenza
   ,a.ti_gestione
   ,a.cd_elemento_voce
   ,'D'
   ,b.CD_FUNZIONE
   ,c.CD_NATURA
   ,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
   ,NULL
   ,NULL
   ,NULL
   ,NULL
   ,0
from
    ELEMENTO_VOCE a
   ,FUNZIONE b
   ,NATURA c
   ,CDR d -- CDR di primo livello
   ,UNITA_ORGANIZZATIVA e -- UO rubrica di appartenenza del CDR di primo livello
where
     a.ti_appartenenza='C' -- Per il SAC con natura diversa da 5 si estrae la tipologia di intervento
 and a.ti_gestione = 'S'
 and (
  a.ti_elemento_voce = 'C'
 )
 and d.cd_unita_organizzativa=e.cd_unita_organizzativa
 and d.livello = 1 -- estrae i CDR di primo livello
 and e.cd_tipo_unita = 'SAC'
 and cd_natura != 5 -- La natura deve essere diversa dalla natura 5
 and exists (select 1 from ass_ev_funz_tipocds xa, ass_ev_ev xb where
                   xb.esercizio = a.esercizio
               and xb.ti_appartenenza = 'D'
               and xb.ti_gestione = 'S'
               and xb.ti_elemento_voce = 'C'
               and xb.ti_appartenenza_coll = 'C'
               and xb.ti_gestione_coll = 'S'
               and xb.ti_elemento_voce_coll = 'C'
               and xb.cd_elemento_voce_coll = a.cd_elemento_voce -- Ttipologia di intervento
               and xa.esercizio = a.esercizio
               and xa.cd_conto = xb.cd_elemento_voce
			   and xa.cd_funzione=b.cd_funzione
			   and xa.cd_tipo_unita=e.cd_tipo_unita
			 )
 and not exists (select 1 from V_DPDG_AGGREGATO_SPE_DET where
                  esercizio=a.esercizio
				  and cd_centro_responsabilita = d.cd_centro_responsabilita
                  and ti_gestione = a.TI_GESTIONE
				  and ti_appartenenza = a.TI_APPARTENENZA
                  and cd_elemento_voce = a.cd_elemento_voce
				  and cd_funzione = b.cd_funzione
                  and cd_natura = c.cd_natura
                  and cd_cds = e.cd_unita_padre
				 )
union all
 select distinct
 -- seleziona lo span a 0 da aggiungere per il SAC con natura = 5
    a.ESERCIZIO
   ,d.cd_centro_responsabilita
   ,g.cd_area_ricerca
   ,a.ti_appartenenza
   ,a.ti_gestione
   ,a.cd_elemento_voce
   ,'D'
   ,b.CD_FUNZIONE
   ,c.CD_NATURA
   ,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
   ,NULL
   ,NULL
   ,NULL
   ,NULL
   ,0
from
    ELEMENTO_VOCE a
   ,FUNZIONE b
   ,NATURA c
   ,CDR d
   ,UNITA_ORGANIZZATIVA e -- UO rubrica di appartenenza del CDR di primo livello
   ,CDR f -- CDR = a d o afferente a d
   ,UNITA_ORGANIZZATIVA g -- UO  di appartenenza di f afferente ad AREA
where
     a.ti_appartenenza='D'
 and a.ti_gestione = 'S'
 and a.ti_elemento_voce = 'T' -- La natura 5 del SAC aggrega sul titolo come per ogni altro tipo di CDS
 and d.cd_unita_organizzativa=e.cd_unita_organizzativa
 and d.livello = 1 -- d e un CDR di primo livello
 and e.cd_tipo_unita = 'SAC'
 and cd_natura = 5 -- La natura deve essere = alla natura 5
 and (
      f.cd_cdr_afferenza = d.cd_centro_responsabilita
   or f.cd_centro_responsabilita = d.cd_centro_responsabilita
 )
 and g.cd_unita_organizzativa = f.cd_unita_organizzativa
 and g.cd_area_ricerca is not null
 and exists (select 1 from ass_ev_funz_tipocds where
               esercizio = a.esercizio
               and cd_conto like a.cd_elemento_voce||'%'
			   and cd_funzione=b.cd_funzione
			   and cd_tipo_unita=e.cd_tipo_unita)
 and not exists (select 1 from V_DPDG_AGGREGATO_SPE_DET where
                  esercizio=a.esercizio
				  and cd_centro_responsabilita = d.cd_centro_responsabilita
                  and ti_gestione = a.TI_GESTIONE
				  and ti_appartenenza = a.TI_APPARTENENZA
                  and cd_elemento_voce = a.cd_elemento_voce
				  and cd_funzione = b.cd_funzione
                  and cd_natura = c.cd_natura
                  and cd_cds = g.cd_area_ricerca
				 )
)
;

   COMMENT ON TABLE "V_DPDG_AGGREGATO_SPE_DET_SPN"  IS 'Effettua lo span a tutte le combinazioni di parametri di aggregazione su V_DPDG_AGGREGATO_SPE_DET';
