--------------------------------------------------------
--  DDL for View V_DPDG_AGGREGATO_SPE_DETCDR
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_DPDG_AGGREGATO_SPE_DETCDR" ("ESERCIZIO", "CD_CENTRO_RESPONSABILITA", "CD_CDS", "TI_APPARTENENZA", "TI_GESTIONE", "CD_ELEMENTO_VOCE", "TI_AGGREGATO", "CD_FUNZIONE", "CD_NATURA", "IM_RH_CCS_COSTI", "IM_RI_CCS_SPESE_ODC", "IM_RJ_CCS_SPESE_ODC_ALTRA_UO", "IM_RK_CCS_SPESE_OGC", "IM_RL_CCS_SPESE_OGC_ALTRA_UO", "IM_RM_CSS_AMMORTAMENTI", "IM_RN_CSS_RIMANENZE", "IM_RO_CSS_ALTRI_COSTI", "IM_RP_CSS_VERSO_ALTRO_CDR", "IM_RQ_SSC_COSTI_ODC", "IM_RR_SSC_COSTI_ODC_ALTRA_UO", "IM_RS_SSC_COSTI_OGC", "IM_RT_SSC_COSTI_OGC_ALTRA_UO", "IM_RU_SPESE_COSTI_ALTRUI", "IM_RV_PAGAMENTI", "IM_RAA_A2_COSTI_FINALI", "IM_RAB_A2_COSTI_ALTRO_CDR", "IM_RAC_A2_SPESE_ODC", "IM_RAD_A2_SPESE_ODC_ALTRA_UO", "IM_RAE_A2_SPESE_OGC", "IM_RAF_A2_SPESE_OGC_ALTRA_UO", "IM_RAG_A2_SPESE_COSTI_ALTRUI", "IM_RAH_A3_COSTI_FINALI", "IM_RAI_A3_COSTI_ALTRO_CDR", "IM_RAL_A3_SPESE_ODC", "IM_RAM_A3_SPESE_ODC_ALTRA_UO", "IM_RAN_A3_SPESE_OGC", "IM_RAO_A3_SPESE_OGC_ALTRA_UO", "IM_RAP_A3_SPESE_COSTI_ALTRUI", "DACR", "UTCR", "DUVA", "UTUV", "PG_VER_REC") AS 
  (select
--
-- Date: 10/04/2003
-- Version: 1.10
--
-- Aggrega i dettagli di un certo PDG di un DATO CDR ripartendo i dettagli
-- di natura 5 tra le diverse aree di afferenza (cd_cds)
-- La vista non si preoccupa di verificare la validita STO
-- La vista non aggrega i dettagli di natura 5 dell'AREA
-- La vista gestisce l'aggregazione dei dettagli SAC con natura diversa da 5 per tipologia di intervento
-- La vista gestisce l'aggregazione dei dettagli SAC con natura = 5 per titolo (come per gli altri tipi di CDS)
--
-- History:
--
-- Date: 15/09/2001
-- Version: 1.0
-- Creazione
-- Date: 30/09/2001
-- Version: 1.1
-- Introdotto il CDS per la gestione corretta dei dettagli di natura 5
-- Date: 22/10/2001
-- Version: 1.2
-- Introdotto ti_aggregato = 'D' per compatibilita con PDG_AGGREGATO_SPE_DET
-- Date: 30/10/2001
-- Version: 1.3
-- Aggregato per titolo capitolo per CDR del SAC
-- Epurazione dei costi figurativi
-- Date: 08/11/2001
-- Version: 1.4
-- Eliminazione esercizio da STO
-- Date: 29/04/2002
-- Version: 1.5
-- Aggiunti i dettagli di carico confermati
-- Date: 19/06/2002
-- Version: 1.6
-- Aggregazione dei dettagli del SAC per tipologia di intervento
--
-- Date: 26/06/2002
-- Version: 1.7
-- I dettagli di natura 5 non vengono più estratti nell'aggregato dell'area
--
-- Date: 16/01/2003
-- Version: 1.8
-- Vista di riconciliazione dettagli di natura 5 già scaricati su area (valido per riapertura per modifiche pdg)
-- Il totale per titolo funzione natura di tali dettagli dopo il ribaltamento viene spostato su area: la nuova parte
-- aggiunta alla vista riporta tali dati sul CDR servito dall'area, perchè da un punto di vista  degli aggregati, il ribaltamento non
-- ha significato (la contrattazione sulla parte relativa all'area viene effettuata sul centro servito).
-- Questo vale per tutte le colonne tranne P AB e AI che devono essere stornate perchè modificate con il ribaltamento nel servito
--
-- Date: 20/01/2003
-- Version: 1.9
-- Fix errore estrazione dettagli da AREA per compensazione ribaltamenti su area
--
-- Date: 10/04/2003
-- Version: 1.10
-- Revisione per ottimizzazione performance: introduzione funtion isScrInterno per velisione ricavi figurativi
--
-- Date: 28/07/2005
-- Version: 1.11
-- Inseriti nel calcolo solo le Variazioni ai Piani di Gestione con  stato Approvato (APP)
--
-- Body:
--
R.ESERCIZIO
,CR.CD_CENTRO_RESPONSABILITA
,decode(R.cd_natura,5,UO.CD_AREA_RICERCA,UO.CD_UNITA_PADRE)
,EV.ti_appartenenza
,EV.ti_gestione
,EV.cd_elemento_padre
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
CDR CR, -- CDR collegato ad R
PDG_PREVENTIVO_SPE_DET R, -- PDG spese per CDR CR
PDG_VARIAZIONE V, -- PDG variazione per CDR CR
ELEMENTO_VOCE EV,
UNITA_ORGANIZZATIVA UO
where
    R.cd_centro_responsabilita=CR.cd_centro_responsabilita
and R.stato = 'Y'
and EV.ti_elemento_voce = 'C'
and EV.esercizio = R.esercizio
and EV.ti_gestione = R.ti_gestione
and EV.ti_appartenenza = R.ti_appartenenza
and EV.cd_elemento_voce = R.cd_elemento_voce
and R.esercizio_pdg_variazione = V.esercizio (+)
and R.pg_variazione_pdg = V.pg_variazione_pdg (+)
and (
     V.stato Is Null Or V.stato = 'APP'
)
and (
     UO.cd_tipo_unita <> 'AREA'
  or UO.cd_tipo_unita = 'AREA' and R.cd_natura <> '5' -- I dettagli di natura 5 dell'AREA non devono essere aggregati
)
and (
     UO.cd_tipo_unita <> 'SAC'
  or UO.cd_tipo_unita = 'SAC' and R.cd_natura = '5' -- I dettagli di natura 5 del SAC vengono aggregati secondo le logiche del TITOLO
)
and UO.cd_unita_organizzativa = CR.cd_unita_organizzativa
and
(
 (
      R.categoria_dettaglio = 'SCR' -- Epurazione dei costi figurativi
  and R.cd_centro_responsabilita_clge is not null -- serve per estrarre i dettagli verso altro CDR
  and (isScrInterno(R.cd_centro_responsabilita,R.cd_centro_responsabilita_clge)) = 0
 )
 or
 (
      R.categoria_dettaglio = 'SCR'
  and R.cd_centro_responsabilita_clgs is not null -- serve per estrarre i dettagli verso altra UO
 )
 or
 (
      R.categoria_dettaglio in ('CAR','SIN')
 )
)
Group by
R.ESERCIZIO
,CR.CD_CENTRO_RESPONSABILITA
,decode(R.cd_natura,5,UO.CD_AREA_RICERCA,UO.CD_UNITA_PADRE)
,EV.TI_APPARTENENZA
,EV.TI_GESTIONE
,EV.cd_elemento_padre
,R.CD_FUNZIONE
,R.CD_NATURA
union all
select
-- Parte relativa al SAC
-- Richiesta n. 140E - aggregazione per tipologia di intervento (voce del piano di spesa CNR)
 R.ESERCIZIO
,CR.CD_CENTRO_RESPONSABILITA
,decode(R.cd_natura,5,UO.CD_AREA_RICERCA,UO.CD_UNITA_PADRE)
,ASSEV.ti_appartenenza_coll
,ASSEV.ti_gestione_coll
,ASSEV.cd_elemento_voce_coll
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
CDR CR, -- CDR collegato ad R
PDG_PREVENTIVO_SPE_DET R, -- PDG spese per CDR CR
PDG_VARIAZIONE V, -- PDG variazione per CDR CR
UNITA_ORGANIZZATIVA UO,
ASS_EV_EV ASSEV -- Associazione tra conti per ricuperare la ipologia di intervento
where
    R.cd_centro_responsabilita=CR.cd_centro_responsabilita
and R.stato = 'Y'
and UO.cd_unita_organizzativa = CR.cd_unita_organizzativa
and UO.cd_tipo_unita = 'SAC'
and R.cd_natura <> '5' -- I dettagli devono essere di natura diversa da 5
and ASSEV.esercizio = R.esercizio
and ASSEV.ti_appartenenza = 'D'
and ASSEV.ti_gestione = 'S'
and ASSEV.ti_elemento_voce = 'C'
and ASSEV.cd_elemento_voce = R.cd_elemento_voce
and ASSEV.ti_appartenenza_coll = 'C'
and ASSEV.ti_gestione_coll = 'S'
and ASSEV.ti_elemento_voce_coll = 'C'
and R.esercizio_pdg_variazione = V.esercizio (+)
and R.pg_variazione_pdg = V.pg_variazione_pdg (+)
and (
     V.stato Is Null Or V.stato = 'APP'
)
and
(
 (
      R.categoria_dettaglio = 'SCR' -- Epurazione dei costi figurativi
  and R.cd_centro_responsabilita_clge is not null
  and (isScrInterno(R.cd_centro_responsabilita,R.cd_centro_responsabilita_clge)) = 0
 )
 or
 (
      R.categoria_dettaglio = 'SCR'
  and R.cd_centro_responsabilita_clgs is not null -- serve per estrarre i dettagli verso altra UO
 )
 or
 (
      R.categoria_dettaglio in ('SIN','CAR')
 )
)
group by
 R.ESERCIZIO
,CR.CD_CENTRO_RESPONSABILITA
,decode(R.cd_natura,5,UO.CD_AREA_RICERCA,UO.CD_UNITA_PADRE)
,ASSEV.ti_appartenenza_coll
,ASSEV.ti_gestione_coll
,ASSEV.cd_elemento_voce_coll
,R.CD_FUNZIONE
,R.CD_NATURA
union all
-- Vista di riconciliazione dettagli di natura 5 già scaricati su area
--
-- Il totale per titolo funzione natura di tali dettagli viene dopo il ribaltamento viene spostato su area a parte
-- le colonne P AB e AI che devono essere stornate  per ricostruire la situazione pre-ribaltamento
--
select
 SA.ESERCIZIO
,CR.CD_CENTRO_RESPONSABILITA
,UO.CD_AREA_RICERCA
,EV.ti_appartenenza
,EV.ti_gestione
,EV.cd_elemento_padre
,'D'
,sa.CD_FUNZIONE
,sa.CD_NATURA
,sum(sa.IM_RH_CCS_COSTI)
,sum(sa.IM_RI_CCS_SPESE_ODC)
,sum(sa.IM_RJ_CCS_SPESE_ODC_ALTRA_UO)
,sum(sa.IM_RK_CCS_SPESE_OGC)
,sum(sa.IM_RL_CCS_SPESE_OGC_ALTRA_UO)
,sum(sa.IM_RM_CSS_AMMORTAMENTI)
,sum(sa.IM_RN_CSS_RIMANENZE)
,sum(sa.IM_RO_CSS_ALTRI_COSTI)
/*           */ ,sum(0-sa.IM_RH_CCS_COSTI-sa.IM_RM_CSS_AMMORTAMENTI-sa.IM_RN_CSS_RIMANENZE-sa.IM_RO_CSS_ALTRI_COSTI)
,sum(sa.IM_RQ_SSC_COSTI_ODC)
,sum(sa.IM_RR_SSC_COSTI_ODC_ALTRA_UO)
,sum(sa.IM_RS_SSC_COSTI_OGC)
,sum(sa.IM_RT_SSC_COSTI_OGC_ALTRA_UO)
,sum(sa.IM_RU_SPESE_COSTI_ALTRUI)
,sum(sa.IM_RV_PAGAMENTI)
,sum(sa.IM_RAA_A2_COSTI_FINALI)
/*           */ ,sum(0-sa.IM_RAA_A2_COSTI_FINALI)
,sum(sa.IM_RAC_A2_SPESE_ODC)
,sum(sa.IM_RAD_A2_SPESE_ODC_ALTRA_UO)
,sum(sa.IM_RAE_A2_SPESE_OGC)
,sum(sa.IM_RAF_A2_SPESE_OGC_ALTRA_UO)
,sum(sa.IM_RAG_A2_SPESE_COSTI_ALTRUI)
,sum(sa.IM_RAH_A3_COSTI_FINALI)
/*           */ ,sum(0-sa.IM_RAH_A3_COSTI_FINALI)
,sum(sa.IM_RAL_A3_SPESE_ODC)
,sum(sa.IM_RAM_A3_SPESE_ODC_ALTRA_UO)
,sum(sa.IM_RAN_A3_SPESE_OGC)
,sum(sa.IM_RAO_A3_SPESE_OGC_ALTRA_UO)
,sum(sa.IM_RAP_A3_SPESE_COSTI_ALTRUI)
,NULL
,NULL
,NULL
,NULL
,0
from unita_organizzativa UO,
     cdr CR,
     pdg_preventivo_spe_det sa,
     pdg_variazione V, -- PDG variazione per CDR CR
     linea_attivita LA,
     elemento_voce EV,
     unita_organizzativa UOAREA,
     cdr CRAREA
Where  sa.cd_natura = '5'
And sa.stato = 'Y'
And EV.ti_elemento_voce = 'C'
And EV.esercizio = sa.esercizio
And EV.ti_gestione = sa.ti_gestione
And EV.ti_appartenenza = sa.ti_appartenenza
And EV.cd_elemento_voce = sa.cd_elemento_voce
And LA.cd_cdr_collegato = CR.cd_centro_responsabilita
And LA.cd_centro_responsabilita = sa.cd_centro_responsabilita
And LA.cd_linea_attivita = sa.cd_linea_attivita
And UO.cd_unita_organizzativa = CR.cd_unita_organizzativa
And CRAREA.cd_centro_responsabilita = sa.cd_centro_responsabilita
And UOAREA.cd_unita_organizzativa = CRAREA.cd_unita_organizzativa
And UOAREA.cd_tipo_unita = 'AREA'
And sa.esercizio_pdg_variazione = V.esercizio (+)
And sa.pg_variazione_pdg = V.pg_variazione_pdg (+)
And (
     V.stato Is Null Or V.stato = 'APP'
)
group by
 SA.ESERCIZIO
,CR.CD_CENTRO_RESPONSABILITA
,UO.CD_AREA_RICERCA
,EV.ti_appartenenza
,EV.ti_gestione
,EV.cd_elemento_padre
,SA.CD_FUNZIONE
,SA.CD_NATURA
);

   COMMENT ON TABLE "V_DPDG_AGGREGATO_SPE_DETCDR"  IS 'Aggrega i dettagli di un certo PDG di un DATO CDR ripartendo i dettagli
di natura 5 tra le diverse aree di afferenza (cd_cds)
La vista non si preoccupa di verificare la validita STO
La vista non aggrega i dettagli di natura 5 dell''AREA
La vista gestisce l''aggregazione dei dettagli SAC con natura diversa da 5 per tipologia di intervento
La vista gestisce l''aggregazione dei dettagli SAC con natura = 5 per titolo (come per gli altri tipi di CDS)
La vista non include le righe provenienti da Variazioni ai PDG con stato diverso da ''Approvato''';
