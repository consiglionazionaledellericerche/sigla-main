--------------------------------------------------------
--  DDL for View V_PDG_ENTRATE_UO
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_PDG_ENTRATE_UO" ("ESERCIZIO", "CD_CDS", "CD_UO_AGGREGANTE", "CD_UNITA_ORGANIZZATIVA", "TI_APPARTENENZA", "TI_GESTIONE", "CD_VOCE_AGG", "CD_VOCE", "IM_STANZ_INIZIALE_A1", "IM_STANZ_INIZIALE_A2", "IM_STANZ_INIZIALE_A3") AS 
  (
select
--
-- Date: 19/06/2002
-- Version: 1.1
--
-- Vista di aggregazione dei dati dei cdr per rubrica ai fine di determinazione della spalmatura entrata CNR con variazioni
-- Non effettua controlli di validita su STO
--
-- History:
--
-- Date: 18/06/2002
-- Version: 1.0
-- Creazione
--
-- Date: 19/06/2002
-- Version: 1.1
-- Aggiunta voce UO aggregante
--
-- Body:
--
 R.ESERCIZIO
,UO.CD_UNITA_PADRE
,UOAGG.CD_unita_organizzativa
,UO.CD_UNITA_ORGANIZZATIVA
,'C'
,'E'
,R.cd_elemento_voce||'.'||UOAGG.CD_unita_organizzativa
,V.CD_VOCE
,SUM(R.IM_RA_RCE + R.IM_RC_ESR)
,SUM(R.IM_RE_A2_ENTRATE)
,SUM(R.IM_RG_A3_ENTRATE)
from
UNITA_ORGANIZZATIVA UOAGG, -- UO che aggrega
CDR CDRAGG, -- CDR che aggrega
UNITA_ORGANIZZATIVA UO, -- UO COLLEGATA A CDR
CDR CR, -- CDR collegato ad R
PDG_PREVENTIVO_ETR_DET R, -- PDG spese per CDR CR
VOCE_F V
where
     R.cd_centro_responsabilita=CR.cd_centro_responsabilita
 and R.stato = 'Y'
 and UOAGG.cd_unita_padre = UO.cd_unita_padre
 and UOAGG.cd_tipo_unita = 'SAC'
 and UOAGG.cd_unita_organizzativa = CDRAGG.cd_unita_organizzativa
 and CDRAGG.cd_centro_responsabilita = CR.cd_cdr_afferenza
 and UO.cd_unita_organizzativa=CR.cd_unita_organizzativa
 and V.ti_voce = 'A'
 and V.esercizio = R.esercizio
 and V.ti_gestione = 'E'
 and V.ti_appartenenza = 'C'
 and V.cd_titolo_capitolo = R.cd_elemento_voce
 and V.cd_cds = UO.cd_unita_padre
 and V.cd_unita_organizzativa = UO.cd_unita_organizzativa
group by
  R.ESERCIZIO
 ,UO.CD_UNITA_PADRE
 ,UOAGG.CD_unita_organizzativa
 ,UO.CD_UNITA_ORGANIZZATIVA
 ,R.cd_elemento_voce||'.'||UOAGG.CD_unita_organizzativa
 ,V.CD_VOCE
union all
select
  R.ESERCIZIO
 ,UO.CD_UNITA_PADRE
 ,UOAGG.CD_unita_organizzativa
 ,UO.CD_UNITA_ORGANIZZATIVA
 ,'C'
 ,'E'
 ,R.cd_elemento_voce||'.'||UOAGG.CD_unita_organizzativa
 ,V.CD_VOCE
 ,SUM(R.IM_RA_RCE + R.IM_RC_ESR)
 ,SUM(R.IM_RE_A2_ENTRATE)
 ,SUM(R.IM_RG_A3_ENTRATE)
 from
 UNITA_ORGANIZZATIVA UOAGG, -- UO che aggrega
 UNITA_ORGANIZZATIVA UO, -- UO COLLEGATA A CDR
 CDR CR, -- CDR collegato ad R
 PDG_PREVENTIVO_ETR_DET R, -- PDG spese per CDR CR
 VOCE_F V
where
     R.cd_centro_responsabilita=CR.cd_centro_responsabilita
 and R.stato = 'Y'
 and UOAGG.cd_unita_padre = UO.cd_unita_padre
 and UOAGG.cd_tipo_unita <> 'SAC'
 and UOAGG.fl_uo_cds = 'Y'
 and UOAGG.cd_unita_organizzativa <> UO.cd_unita_organizzativa
 and UO.cd_unita_organizzativa=CR.cd_unita_organizzativa
 and V.ti_voce = 'A'
 and V.esercizio = R.esercizio
 and V.ti_gestione = 'E'
 and V.ti_appartenenza = 'C'
 and V.cd_titolo_capitolo = R.cd_elemento_voce
 and V.cd_cds = UO.cd_unita_padre
 and V.cd_unita_organizzativa = UO.cd_unita_organizzativa
group by
  R.ESERCIZIO
 ,UO.CD_UNITA_PADRE
 ,UOAGG.CD_unita_organizzativa
 ,UO.CD_UNITA_ORGANIZZATIVA
 ,R.cd_elemento_voce||'.'||UOAGG.CD_unita_organizzativa
 ,V.CD_VOCE
)
;

   COMMENT ON TABLE "V_PDG_ENTRATE_UO"  IS 'Vista di aggregazione importi pdg entrata a fini prev cnr entrata dei cdr che riportano ad UO non oggetto diretto di contrattazione col centro
 In particolare:
   per il SAC, tutti i CDR afferenti ad UO NON rubrica
   per CDS diverso da SAC, tutti i CDR afferenti ad UO diverse dall''UO CDS';
