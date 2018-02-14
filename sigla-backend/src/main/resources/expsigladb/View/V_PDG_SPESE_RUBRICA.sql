--------------------------------------------------------
--  DDL for View V_PDG_SPESE_RUBRICA
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_PDG_SPESE_RUBRICA" ("ESERCIZIO", "CD_CDS", "CD_UNITA_ORGANIZZATIVA", "CD_CENTRO_RESPONSABILITA", "TI_APPARTENENZA", "TI_GESTIONE", "CD_VOCE", "IM_STANZ_INIZIALE_A1", "IM_STANZ_INIZIALE_A2", "IM_STANZ_INIZIALE_A3", "DACR", "UTCR", "DUVA", "UTUV", "PG_VER_REC") AS 
  ( 
select 
-- 
-- Date: 27/02/2002 
-- Version: 1.4
-- 
-- Vista di aggregazione dei dati PDG CDR I livello per rubrica parte spese 
-- Estraggo in capitoli per il Macroistituto o area 
-- Non effettua controlli di validita su STO 
-- 
-- 
-- History: 
-- 
-- Date: 26/09/2001 
-- Version: 1.0 
-- Creazione 
-- 
-- Date: 08/11/2001 
-- Version: 1.1 
-- Eliminazione esercizio da STO 
-- 
-- Date: 11/01/2001 
-- Version: 1.2 
-- Nel caso di CDS SAC deve aggregare anche i dati delle UO non rubrica 
-- 
-- Date: 26/02/2002 
-- Version: 1.3 
-- Fix su estrazione aggregazione per rubrica: il CDR e di primo livello (secondo per aree) 
-- Viene estratta la lista dei capitoli spezzata per le UO rubriche afferenti al CDR di primo livello 
-- 
-- Date: 27/02/2002 
-- Version: 1.4
-- Nel caso del SAC viene estratto, a parita di CDR di primo livello, il dettaglio per articoli = CDR
--
-- Body: 
-- 
 R.ESERCIZIO 
,UO.CD_UNITA_PADRE 
,UO.CD_UNITA_ORGANIZZATIVA 
,CRPRIMO.CD_CENTRO_RESPONSABILITA 
,'D' 
,'S' 
,V.CD_VOCE 
,SUM(R.IM_RV_PAGAMENTI) 
,SUM(R.IM_RAA_A2_COSTI_FINALI) 
,SUM(R.IM_RAH_A3_COSTI_FINALI) 
,NULL 
,NULL 
,NULL 
,NULL 
,0 
from 
UNITA_ORGANIZZATIVA UO, -- UO COLLEGATA A CDR I LIVELLO o II per AREA 
CDR CRPRIMO, -- CDR di primo livello o II per AREA 
CDR CR, -- CDR collegato ad R 
LINEA_ATTIVITA LA, -- Liena di attivita del dettaglio 
PDG_PREVENTIVO_SPE_DET R, -- PDG spese per CDR CR 
VOCE_F V 
where 
    R.cd_centro_responsabilita=CR.cd_centro_responsabilita 
and LA.cd_centro_responsabilita = CR.cd_centro_responsabilita 
and LA.cd_linea_attivita = R.cd_linea_attivita 
and UO.cd_tipo_unita != 'SAC' 
and UO.cd_unita_organizzativa=CR.cd_unita_organizzativa 
and UO.fl_rubrica = 'Y' 
and ( 
         CRPRIMO.cd_centro_responsabilita = CR.cd_cdr_afferenza 
	  or CRPRIMO.cd_centro_responsabilita = CR.cd_centro_responsabilita 
) 
and R.stato = 'Y' 
and ( 
        CRPRIMO.livello = 1 
     or CRPRIMO.livello = 2 and UO.cd_tipo_unita = 'AREA' 
) 
and V.ti_voce = 'C' 
and V.esercizio = R.esercizio 
and V.ti_gestione = 'S' 
and V.ti_appartenenza = 'D' 
and V.cd_funzione = LA.cd_funzione 
and V.cd_titolo_capitolo = R.cd_elemento_voce 
and V.cd_cds = UO.cd_unita_padre 
and V.cd_unita_organizzativa = UO.cd_unita_organizzativa 
group by 
 R.ESERCIZIO 
,UO.CD_UNITA_PADRE 
,UO.CD_UNITA_ORGANIZZATIVA 
,CRPRIMO.CD_CENTRO_RESPONSABILITA 
,'D' 
,'S' 
,V.CD_VOCE 
UNION 
-- Estraggo in articoli per il SAC: le UO non rubrica vengono aggregate con quelle rubrica 
select 
 R.ESERCIZIO 
,UO.CD_UNITA_PADRE 
,UO.CD_UNITA_ORGANIZZATIVA 
,CR_RUBRICA.CD_CENTRO_RESPONSABILITA 
,'D' 
,'S' 
,V.CD_VOCE 
,SUM(R.IM_RV_PAGAMENTI) 
,SUM(R.IM_RAA_A2_COSTI_FINALI) 
,SUM(R.IM_RAH_A3_COSTI_FINALI) 
,NULL 
,NULL 
,NULL 
,NULL 
,0 
from 
UNITA_ORGANIZZATIVA UO, -- UO RUBRICA  a cui afferisce CR_RUBRICA (CDR di primo livello) 
CDR CR_RUBRICA, -- CDR responsabile della rubrica UO 
CDR CR, -- CDR collegato al PDG 
LINEA_ATTIVITA LA, -- Liena di attivita del dettaglio 
PDG_PREVENTIVO_SPE_DET R, -- PDG spese per CDR CR 
VOCE_F V 
where 
    LA.cd_centro_responsabilita = CR.cd_centro_responsabilita 
and LA.cd_linea_attivita = R.cd_linea_attivita 
and UO.cd_tipo_unita = 'SAC' 
and UO.cd_unita_organizzativa=CR_RUBRICA.cd_unita_organizzativa -- appartenenza del CDR di primo alla rubrica 
and ( 
       CR.cd_cdr_afferenza=CR_RUBRICA.cd_centro_responsabilita  -- afferenza a CDR di primo 
    or CR.cd_centro_responsabilita=CR_RUBRICA.cd_centro_responsabilita  -- uguaglianza a CDR di primo 
) 
and R.cd_centro_responsabilita=CR.cd_centro_responsabilita  -- link con PDG 
and UO.fl_rubrica = 'Y' 
and R.stato = 'Y' 
and V.ti_voce = 'A' 
and V.cd_centro_responsabilita=CR.cd_centro_responsabilita 
and V.esercizio = R.esercizio 
and V.ti_gestione = 'S' 
and V.ti_appartenenza = 'D' 
and V.cd_funzione = LA.cd_funzione 
and V.cd_titolo_capitolo = R.cd_elemento_voce 
and V.cd_cds = UO.cd_unita_padre 
and V.cd_unita_organizzativa = UO.cd_unita_organizzativa 
group by 
R.ESERCIZIO 
,UO.CD_UNITA_PADRE 
,UO.CD_UNITA_ORGANIZZATIVA 
,CR_RUBRICA.CD_CENTRO_RESPONSABILITA 
,'D' 
,'S' 
,V.CD_VOCE 
);

   COMMENT ON TABLE "V_PDG_SPESE_RUBRICA"  IS 'Vista di aggregazione dei dati PDG CDR I livello per rubrica parte spese
Non effettua controlli di validita su STO';
