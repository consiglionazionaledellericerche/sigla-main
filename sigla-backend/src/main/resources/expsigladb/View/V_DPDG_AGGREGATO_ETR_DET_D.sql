--------------------------------------------------------
--  DDL for View V_DPDG_AGGREGATO_ETR_DET_D
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_DPDG_AGGREGATO_ETR_DET_D" ("ESERCIZIO", "CD_CENTRO_RESPONSABILITA", "CD_NATURA", "TI_APPARTENENZA", "TI_GESTIONE", "CD_ELEMENTO_VOCE", "TI_AGGREGATO", "IM_RA_RCE", "IM_RB_RSE", "IM_RC_ESR", "IM_RD_A2_RICAVI", "IM_RE_A2_ENTRATE", "IM_RF_A3_RICAVI", "IM_RG_A3_ENTRATE", "DACR", "UTCR", "DUVA", "UTUV", "PG_VER_REC") AS 
  ( 
select 
-- 
-- Date: 24/12/2001 
-- Version: 1.3 
-- 
-- Ritorna le righe aggregato del PDG di un dato CDR che non soddisfano alla condizione di contrattazione con il centro 
-- 
-- History: 
-- 
-- Date: 15/09/2001 
-- Version: 1.0 
-- Creazione 
-- Date: 22/10/2001 
-- Version: 1.2 
-- Aggiunto ti_aggregato in interfaccia per compatibilita con PDG_AGGREGATO_ETR_DET = 'D' fisso 
-- Date: 24/12/2001 
-- Version: 1.3 
-- Nuova gestione iterativa delle aggregazioni con span sui parametri 
-- 
-- Body: 
-- 
R.ESERCIZIO 
,R.CD_CENTRO_RESPONSABILITA 
,R.CD_NATURA 
,R.TI_APPARTENENZA 
,R.TI_GESTIONE 
,R.CD_ELEMENTO_VOCE 
,'D' 
,R.IM_RA_RCE-V.IM_RA_RCE 
,R.IM_RB_RSE-V.IM_RB_RSE 
,R.IM_RC_ESR-V.IM_RC_ESR 
,R.IM_RD_A2_RICAVI-V.IM_RD_A2_RICAVI 
,R.IM_RE_A2_ENTRATE-V.IM_RE_A2_ENTRATE 
,R.IM_RF_A3_RICAVI-V.IM_RF_A3_RICAVI 
,R.IM_RG_A3_ENTRATE-V.IM_RG_A3_ENTRATE 
,NULL 
,NULL 
,NULL 
,NULL 
,0 
from 
PDG_AGGREGATO_ETR_DET R, -- Aggregato attuale 
PDG_AGGREGATO_ETR_DET V    -- Aggregato contrattato 
where 
V.esercizio = R.esercizio 
and V.cd_centro_responsabilita = R.cd_centro_responsabilita 
and V.cd_natura = R.cd_natura 
and V.ti_appartenenza = R.ti_appartenenza 
and V.ti_gestione = R.ti_gestione 
and V.cd_elemento_voce = R.cd_elemento_voce 
and V.ti_aggregato = 'M' 
and R.ti_aggregato = 'I' 
and 
( 
R.IM_RA_RCE!=V.IM_RA_RCE 
or R.IM_RB_RSE!=V.IM_RB_RSE 
or R.IM_RC_ESR!=V.IM_RC_ESR 
or R.IM_RD_A2_RICAVI!=V.IM_RD_A2_RICAVI 
or R.IM_RE_A2_ENTRATE!=V.IM_RE_A2_ENTRATE 
or R.IM_RF_A3_RICAVI!=V.IM_RF_A3_RICAVI 
or R.IM_RG_A3_ENTRATE!=V.IM_RG_A3_ENTRATE 
) 
);

   COMMENT ON TABLE "V_DPDG_AGGREGATO_ETR_DET_D"  IS 'Ritorna le righe aggregato del PDG di un dato CDR che non soddisfano alla condizione di contrattazione con il centro';
