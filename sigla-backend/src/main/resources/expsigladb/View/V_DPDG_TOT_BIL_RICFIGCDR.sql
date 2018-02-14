--------------------------------------------------------
--  DDL for View V_DPDG_TOT_BIL_RICFIGCDR
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_DPDG_TOT_BIL_RICFIGCDR" ("ESERCIZIO", "CD_CENTRO_RESPONSABILITA", "CD_LINEA_ATTIVITA", "IM_ETR_A1", "IM_ETR_A2", "IM_ETR_A3", "IM_SPE_A1", "IM_SPE_A2", "IM_SPE_A3") AS 
  (
select
--
-- Date: 22/01/2003
-- Version: 1.3
--
-- Aggrega i dettagli di entrata ricavi figurativi di carico del centro servente e quelli di spesa corrispondenti
-- attraverso l'insieme definito sulla linea di attivita del ricavo figurativo e le linee di spesa destinate alla
-- ripartizione del ricavo figurativo in spesa.
-- Gli importi di entrata sono le colonne B per il 1 anno, D per il 2 anno, F per il 3 anno delle entrate PDG
-- Gli importi di spesa sono la somma di I+J+K+L+Q+R+S+T per il 1 anno, AC+AD+AE+AF per il 2 anno e AL+AM+AN+AO per il 3 anno
--
-- History:
-- Date: 31/10/2001
-- Version: 1.0
-- Creazione
--
-- Date: 09/11/2001
-- Version: 1.1
-- Fix errori
--
-- Date: 20/02/2002
-- Version: 1.2
-- Il controllo di quadratura ora viene fatto attraverso l'insieme che lega linee di attivita di tipo CSSAC (entrata) e linee di attivita
-- di spesa nel CDR servente
--
-- Date: 22/01/2003
-- Version: 1.3
-- Dal controllo sono esclusi i dettagli di natura 5
--
-- Body:
--
ESERCIZIO,
CD_CENTRO_RESPONSABILITA,
CD_LINEA_ATTIVITA,
sum(IM_ETR_A1),
sum(IM_ETR_A2),
sum(IM_ETR_A3),
sum(IM_SPE_A1),
sum(IM_SPE_A2),
sum(IM_SPE_A3)
from (
select
esercizio
,cd_centro_responsabilita
,cd_linea_attivita
,sum(IM_RB_RSE) IM_ETR_A1
,sum(IM_RD_A2_RICAVI) IM_ETR_A2
,sum(IM_RF_A3_RICAVI)IM_ETR_A3
,0 IM_SPE_A1
,0 IM_SPE_A2
,0 IM_SPE_A3
from
 PDG_PREVENTIVO_ETR_DET
where
     categoria_dettaglio = 'CAR' -- Il dettaglio e di carico (arriva da un'altro CDR): sicuramente il tipo LA e CSSAC
 and stato = 'Y'                 -- Il dettaglio e confermato
 and cd_natura != '5'
group by
 esercizio
,cd_centro_responsabilita
,cd_linea_attivita
union  all
select
 a.esercizio
,a.cd_centro_responsabilita
,c.cd_linea_attivita
,0 IM_ETR_A1
,0 IM_ETR_A2
,0 IM_ETR_A3
,sum(a.IM_RI_CCS_SPESE_ODC+a.IM_RJ_CCS_SPESE_ODC_ALTRA_UO+a.IM_RK_CCS_SPESE_OGC+a.IM_RL_CCS_SPESE_OGC_ALTRA_UO+
a.IM_RQ_SSC_COSTI_ODC+a.IM_RR_SSC_COSTI_ODC_ALTRA_UO+a.IM_RS_SSC_COSTI_OGC+a.IM_RT_SSC_COSTI_OGC_ALTRA_UO) IM_SPE_A1
,sum(a.IM_RAC_A2_SPESE_ODC+a.IM_RAD_A2_SPESE_ODC_ALTRA_UO+a.IM_RAE_A2_SPESE_OGC+a.IM_RAF_A2_SPESE_OGC_ALTRA_UO) IM_SPE_A2
,sum(a.IM_RAL_A3_SPESE_ODC+a.IM_RAM_A3_SPESE_ODC_ALTRA_UO+a.IM_RAN_A3_SPESE_OGC+a.IM_RAO_A3_SPESE_OGC_ALTRA_UO) IM_SPE_A3
from
 PDG_PREVENTIVO_SPE_DET a, V_LINEA_ATTIVITA_VALIDA b, V_LINEA_ATTIVITA_VALIDA c
where
     a.categoria_dettaglio in ('SIN','SCR')
 and a.stato = 'Y'
 and b.ESERCIZIO = a.ESERCIZIO
 and b.cd_centro_responsabilita = a.cd_centro_responsabilita
 and b.cd_linea_attivita = a.cd_linea_attivita
 and b.TI_GESTIONE = 'S'               -- E' una linea di attivita di spesa
 and c.ESERCIZIO = a.ESERCIZIO
 and c.cd_centro_responsabilita = a.cd_centro_responsabilita
 and c.CD_TIPO_LINEA_ATTIVITA = 'CSSAC'  -- Linea di attivita dei ricavi figurativi
 and c.TI_GESTIONE = 'E'               -- E' una linea di attivita di spesa
 and b.cd_insieme_la = c.cd_insieme_la -- Comunanza di inseme
 and a.cd_natura != '5' -- Tutto tranne la natura 5
group by
 a.esercizio
,a.cd_centro_responsabilita
,c.cd_linea_attivita
)
group by
esercizio
,cd_centro_responsabilita
,cd_linea_attivita
having
sum(IM_ETR_A1) != 0
or sum(IM_ETR_A2) != 0
or sum(IM_ETR_A3) != 0
)
;

   COMMENT ON TABLE "V_DPDG_TOT_BIL_RICFIGCDR"  IS 'Aggrega i dettagli di entrata ricavi figurativi di carico del centro servente e quelli di spesa corrispondenti
attraverso l''insieme definito sulla linea di attività del ricavo figurativo e le linee di spesa destinate alla
ripartizione del ricavo figurativo in spesa.
Gli importi di entrata sono le colonne B per il 1 anno, D per il 2 anno, F per il 3 anno delle entrate PDG
Gli importi di spesa sono la somma di I+J+K+L+Q+R+S+T per il 1 anno, AC+AD+AE+AF per il 2 anno e AL+AM+AN+AO per il 3 anno';
