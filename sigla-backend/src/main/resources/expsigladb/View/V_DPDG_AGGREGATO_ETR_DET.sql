--------------------------------------------------------
--  DDL for View V_DPDG_AGGREGATO_ETR_DET
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_DPDG_AGGREGATO_ETR_DET" ("ESERCIZIO", "CD_CENTRO_RESPONSABILITA", "CD_NATURA", "TI_APPARTENENZA", "TI_GESTIONE", "CD_ELEMENTO_VOCE", "TI_AGGREGATO", "IM_RA_RCE", "IM_RB_RSE", "IM_RC_ESR", "IM_RD_A2_RICAVI", "IM_RE_A2_ENTRATE", "IM_RF_A3_RICAVI", "IM_RG_A3_ENTRATE", "DACR", "UTCR", "DUVA", "UTUV", "PG_VER_REC") AS 
  (select  
--  
-- Date: 08/11/2001  
-- Version: 1.3  
--  
-- Vista di aggregazione a primo livello della parte entrate PDG per natura e capitolo  
-- La vista non si preoccupa di verificare la validita STO  
--  
-- History:  
--  
-- Date: 15/09/2001  
-- Version: 1.0  
-- Creazione
-- Date: 02/10/2001  
-- Version: 1.1  
-- Introduzione delle aggregazioni per area  
-- Date: 22/10/2001  
-- Version: 1.2  
-- Aggiunto ti_aggregato='D' per compatibilita con PDG_AGGREGATO_ETR_DET  
-- Date: 08/11/2001  
-- Version: 1.3  
-- Eliminazione esercizio da STO  
--  
-- Body:  
--  
R.ESERCIZIO  
,C.CD_CENTRO_RESPONSABILITA  
,R.CD_NATURA  
,R.TI_APPARTENENZA  
,R.TI_GESTIONE  
,R.CD_ELEMENTO_VOCE  
,'D'  
,sum(IM_RA_RCE)  
,sum(IM_RB_RSE)  
,sum(IM_RC_ESR)  
,sum(IM_RD_A2_RICAVI)  
,sum(IM_RE_A2_ENTRATE)  
,sum(IM_RF_A3_RICAVI)  
,sum(IM_RG_A3_ENTRATE)  
,NULL  
,NULL  
,NULL  
,NULL  
,0  
from  
CDR C, -- CDR aggregatore  
UNITA_ORGANIZZATIVA UO, -- UO AGGREGATORE  
CDR CR, -- CDR collegato ad R  
UNITA_ORGANIZZATIVA UOR, -- UO CDR da aggregare  
V_DPDG_AGGREGATO_ETR_DETCDR R -- Vista di aggregazione entrate per natura/capitolo per ogni CDR  
where  
UO.cd_unita_organizzativa = C.cd_unita_organizzativa  
and UOR.cd_unita_organizzativa = CR.cd_unita_organizzativa  
and UOR.cd_unita_padre = UO.cd_unita_padre -- Condizione di comunanza del CDS (non suff. per SAC)  
and (  
(  
UO.cd_tipo_unita = 'AREA'  
and UO.fl_uo_cds = 'Y' -- deve essere il CDR responsabile dell'UO CDS dell'area  
and to_number(C.cd_proprio_cdr) = 0 -- il suo codice proprio e '0..0'  
and CR.cd_centro_responsabilita = C.cd_centro_responsabilita -- nell'area c'e un solo CDR  
)  
or  
(  
UO.cd_tipo_unita != 'AREA'  
and (CR.cd_cdr_afferenza = C.cd_centro_responsabilita or C.cd_centro_responsabilita = CR.cd_centro_responsabilita)  
and C.livello = 1  
)  
)  
and R.cd_centro_responsabilita=CR.cd_centro_responsabilita  
group by  
R.ESERCIZIO  
,C.CD_CENTRO_RESPONSABILITA  
,R.CD_NATURA  
,R.TI_APPARTENENZA  
,R.TI_GESTIONE  
,R.CD_ELEMENTO_VOCE  
);

   COMMENT ON TABLE "V_DPDG_AGGREGATO_ETR_DET"  IS 'Vista di aggregazione a primo livello della parte entrate PDG per natura e capitolo
La vista non si preoccupa di verificare la validita STO';
