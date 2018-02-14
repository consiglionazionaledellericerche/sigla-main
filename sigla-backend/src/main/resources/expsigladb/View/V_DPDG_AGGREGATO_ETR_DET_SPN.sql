--------------------------------------------------------
--  DDL for View V_DPDG_AGGREGATO_ETR_DET_SPN
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_DPDG_AGGREGATO_ETR_DET_SPN" ("ESERCIZIO", "CD_CENTRO_RESPONSABILITA", "CD_NATURA", "TI_APPARTENENZA", "TI_GESTIONE", "CD_ELEMENTO_VOCE", "TI_AGGREGATO", "IM_RA_RCE", "IM_RB_RSE", "IM_RC_ESR", "IM_RD_A2_RICAVI", "IM_RE_A2_ENTRATE", "IM_RF_A3_RICAVI", "IM_RG_A3_ENTRATE", "DACR", "UTCR", "DUVA", "UTUV", "PG_VER_REC") AS 
  (select
--
-- Date: 29/08/2002
-- Version: 1.4
--
-- Effettua lo span a tutte le combinazioni di parametri di aggregazione su V_DPDG_AGGREGATO_ETR_DET
--
-- History:
--
-- Date: 22/12/2001
-- Version: 1.0
-- Creazione
--
-- Date: 09/07/2002
-- Version: 1.1
-- La natura 5 non viene esplosa (non fa parte della contrattazione delle AREE e negli altri CDS non esiste in entrata)
--
-- Date: 08/08/2002
-- Version: 1.2
-- Filtrate le voce del piano fl_voce_sac ='Y' nel caso di cdr appartenenti ad UO non SAC
--
-- Date: 28/08/2002
-- Version: 1.3
-- Fix errore su span -> errore in not exists su v_pdg_aggregato_etr_det sneza cdr
--
-- Date: 29/08/2002
-- Version: 1.4
-- Ottimizzazione generale - eliminate viste V_XXX_VALIDX
--
-- Body:
--
 ESERCIZIO
,CD_CENTRO_RESPONSABILITA
,CD_NATURA
,TI_APPARTENENZA
,TI_GESTIONE
,CD_ELEMENTO_VOCE
,'D'
,IM_RA_RCE
,IM_RB_RSE
,IM_RC_ESR
,IM_RD_A2_RICAVI
,IM_RE_A2_ENTRATE
,IM_RF_A3_RICAVI
,IM_RG_A3_ENTRATE
,NULL
,NULL
,NULL
,NULL
,0
from
 V_DPDG_AGGREGATO_ETR_DET
union all
select
-- seleziona lo span a 0 da aggiungere
    a.ESERCIZIO
   ,d.cd_centro_responsabilita
   ,c.CD_NATURA
   ,a.ti_appartenenza
   ,a.ti_gestione
   ,a.cd_elemento_voce
   ,'D'
   ,0,0,0,0,0,0,0
   ,NULL
   ,NULL
   ,NULL
   ,NULL
   ,0
from
    ELEMENTO_VOCE a
   ,NATURA c
   ,CDR d
   ,UNITA_ORGANIZZATIVA e
where
     a.ti_appartenenza='C'
 and a.ti_gestione = 'E'
 and (
  a.ti_elemento_voce = 'C'
 )
 and e.cd_unita_organizzativa = d.cd_unita_organizzativa
 and (
     e.cd_tipo_unita = 'SAC'
  or a.fl_voce_sac = 'N'
 )
 and c.cd_natura <> '5'
 and exists (select 1 from ASS_EV_EV where
                      esercizio=a.esercizio
                  and cd_natura = c.cd_natura
                  and ti_gestione = a.TI_GESTIONE
				  and ti_appartenenza = a.TI_APPARTENENZA
                  and cd_elemento_voce = a.cd_elemento_voce
                  and ti_gestione_coll = '*'
				  and ti_appartenenza_coll = '*'
                  and cd_elemento_voce_coll = '*'
				 )
 and not exists (select 1 from V_DPDG_AGGREGATO_ETR_DET where
                      esercizio=a.esercizio
                  and cd_natura = c.cd_natura
                  and ti_gestione = a.TI_GESTIONE
				  and ti_appartenenza = a.TI_APPARTENENZA
                  and cd_elemento_voce = a.cd_elemento_voce
				  and cd_centro_responsabilita = d.cd_centro_responsabilita
 			    )
)
;

   COMMENT ON TABLE "V_DPDG_AGGREGATO_ETR_DET_SPN"  IS 'Effettua lo span a tutte le combinazioni di parametri di aggregazione su V_DPDG_AGGREGATO_ETR_DET';
