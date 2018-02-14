--------------------------------------------------------
--  DDL for View VP_PDG_AGGREGATO_ETR
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VP_PDG_AGGREGATO_ETR" ("ESERCIZIO", "CD_CDS", "DS_CDS", "CD_CENTRO_RESPONSABILITA", "DS_CDR", "CD_NATURA", "DS_NATURA", "CD_ELEMENTO_VOCE", "DS_ELEMENTO_VOCE", "IM_RA_RCE", "IM_RB_RSE", "IM_RC_ESR", "IM_RD_A2_RICAVI", "IM_RE_A2_ENTRATE", "IM_RF_A3_RICAVI", "IM_RG_A3_ENTRATE", "MIM_RA_RCE", "MIM_RB_RSE", "MIM_RC_ESR", "MIM_RD_A2_RICAVI", "MIM_RE_A2_ENTRATE", "MIM_RF_A3_RICAVI", "MIM_RG_A3_ENTRATE", "DIM_RA_RCE", "DIM_RB_RSE", "DIM_RC_ESR", "DIM_RD_A2_RICAVI", "DIM_RE_A2_ENTRATE", "DIM_RF_A3_RICAVI", "DIM_RG_A3_ENTRATE") AS 
  (select 
-- 
-- Date: 20/01/2002 
-- Version: 1.2 
-- 
-- Vista di stampa del PDG cdr I livello parte entrate 
-- 
-- History: 
-- 
-- Date: 27/11/2001 
-- Version: 1.0 
-- Creazione 
-- 
-- Date: 29/12/2001 
-- Version: 1.1 
-- Legge dalle tabelle di aggregato solo righe con almeno un importo diverso da 0 
-- 
-- Date: 20/01/2002 
-- Version: 1.2 
-- Le colonne DXXX rappresentano la differenza tra MODIFICATO e INIZIALE 
-- 
-- Body: 
-- 
a1.ESERCIZIO 
,f.CD_UNITA_ORGANIZZATIVA 
,f.DS_UNITA_ORGANIZZATIVA 
,a1.CD_CENTRO_RESPONSABILITA 
,b.DS_CDR 
,a1.CD_NATURA 
,c.DS_NATURA 
,a1.CD_ELEMENTO_VOCE 
,d.DS_ELEMENTO_VOCE 
,sum(a1.IM_RA_RCE) 
,sum(a1.IM_RB_RSE) 
,sum(a1.IM_RC_ESR) 
,sum(a1.IM_RD_A2_RICAVI) 
,sum(a1.IM_RE_A2_ENTRATE) 
,sum(a1.IM_RF_A3_RICAVI) 
,sum(a1.IM_RG_A3_ENTRATE) 
,sum(a1.MIM_RA_RCE) 
,sum(a1.MIM_RB_RSE) 
,sum(a1.MIM_RC_ESR) 
,sum(a1.MIM_RD_A2_RICAVI) 
,sum(a1.MIM_RE_A2_ENTRATE) 
,sum(a1.MIM_RF_A3_RICAVI) 
,sum(a1.MIM_RG_A3_ENTRATE) 
,sum(a1.MIM_RA_RCE-a1.IM_RA_RCE) 
,sum(a1.MIM_RB_RSE-a1.IM_RB_RSE) 
,sum(a1.MIM_RC_ESR-a1.IM_RC_ESR) 
,sum(a1.MIM_RD_A2_RICAVI-a1.IM_RD_A2_RICAVI) 
,sum(a1.MIM_RE_A2_ENTRATE-a1.IM_RE_A2_ENTRATE) 
,sum(a1.MIM_RF_A3_RICAVI-a1.IM_RF_A3_RICAVI) 
,sum(a1.MIM_RG_A3_ENTRATE-a1.IM_RG_A3_ENTRATE) 
from 
( 
select 
ESERCIZIO 
,CD_CENTRO_RESPONSABILITA 
,CD_NATURA 
,CD_ELEMENTO_VOCE 
,IM_RA_RCE 
,IM_RB_RSE 
,IM_RC_ESR 
,IM_RD_A2_RICAVI 
,IM_RE_A2_ENTRATE 
,IM_RF_A3_RICAVI 
,IM_RG_A3_ENTRATE 
,0 MIM_RA_RCE 
,0 MIM_RB_RSE 
,0 MIM_RC_ESR 
,0 MIM_RD_A2_RICAVI 
,0 MIM_RE_A2_ENTRATE 
,0 MIM_RF_A3_RICAVI 
,0 MIM_RG_A3_ENTRATE 
,0 DIM_RA_RCE 
,0 DIM_RB_RSE 
,0 DIM_RC_ESR 
,0 DIM_RD_A2_RICAVI 
,0 DIM_RE_A2_ENTRATE 
,0 DIM_RF_A3_RICAVI 
,0 DIM_RG_A3_ENTRATE 
from 
PDG_AGGREGATO_ETR_DET 
where 
     ti_aggregato = 'I' 
 and ( 
      IM_RA_RCE!=0 
   or IM_RB_RSE!=0 
   or IM_RC_ESR!=0 
   or IM_RD_A2_RICAVI!=0 
   or IM_RE_A2_ENTRATE!=0 
   or IM_RF_A3_RICAVI!=0 
   or IM_RG_A3_ENTRATE!=0 
 ) 
union 
select 
ESERCIZIO 
,CD_CENTRO_RESPONSABILITA 
,CD_NATURA 
,CD_ELEMENTO_VOCE 
,0 IM_RA_RCE 
,0 IM_RB_RSE 
,0 IM_RC_ESR 
,0 IM_RD_A2_RICAVI 
,0 IM_RE_A2_ENTRATE 
,0 IM_RF_A3_RICAVI 
,0 IM_RG_A3_ENTRATE 
,IM_RA_RCE MIM_RA_RCE 
,IM_RB_RSE MIM_RB_RSE 
,IM_RC_ESR MIM_RC_ESR 
,IM_RD_A2_RICAVI MIM_RD_A2_RICAVI 
,IM_RE_A2_ENTRATE MIM_RE_A2_ENTRATE 
,IM_RF_A3_RICAVI MIM_RF_A3_RICAVI 
,IM_RG_A3_ENTRATE MIM_RG_A3_ENTRATE 
,0 DIM_RA_RCE 
,0 DIM_RB_RSE 
,0 DIM_RC_ESR 
,0 DIM_RD_A2_RICAVI 
,0 DIM_RE_A2_ENTRATE 
,0 DIM_RF_A3_RICAVI 
,0 DIM_RG_A3_ENTRATE 
from 
PDG_AGGREGATO_ETR_DET 
where 
     ti_aggregato = 'M' 
 and ( 
      IM_RA_RCE!=0 
   or IM_RB_RSE!=0 
   or IM_RC_ESR!=0 
   or IM_RD_A2_RICAVI!=0 
   or IM_RE_A2_ENTRATE!=0 
   or IM_RF_A3_RICAVI!=0 
   or IM_RG_A3_ENTRATE!=0 
 ) 
) a1, 
CDR b, 
NATURA c, 
ELEMENTO_VOCE d, 
UNITA_ORGANIZZATIVA f, -- uo pdg 
UNITA_ORGANIZZATIVA g -- cds pdg 
where 
b.cd_centro_responsabilita = a1.cd_centro_responsabilita 
and c.cd_natura = a1.cd_natura 
and d.esercizio = a1.esercizio 
and d.ti_appartenenza = 'C' 
and d.ti_gestione = 'E' 
and d.cd_elemento_voce = a1.cd_elemento_voce 
and f.cd_unita_organizzativa = b.cd_unita_organizzativa 
and g.cd_unita_organizzativa = f.cd_unita_padre 
group by 
a1.ESERCIZIO 
,f.CD_UNITA_ORGANIZZATIVA 
,f.DS_UNITA_ORGANIZZATIVA 
,a1.CD_CENTRO_RESPONSABILITA 
,b.DS_CDR 
,a1.CD_NATURA 
,c.DS_NATURA 
,a1.CD_ELEMENTO_VOCE 
,d.DS_ELEMENTO_VOCE 
);

   COMMENT ON TABLE "VP_PDG_AGGREGATO_ETR"  IS 'Vista di stampa del PDG cdr I livello parte entrate';
