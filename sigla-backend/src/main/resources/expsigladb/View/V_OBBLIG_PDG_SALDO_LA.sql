--------------------------------------------------------
--  DDL for View V_OBBLIG_PDG_SALDO_LA
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_OBBLIG_PDG_SALDO_LA" ("ESERCIZIO", "CD_CENTRO_RESPONSABILITA", "CD_LINEA_ATTIVITA", "IM_SPESE_A1", "IM_SPESE_A2", "IM_SPESE_A3", "IM_OBB_A1", "IM_OBB_A2", "IM_OBB_A3") AS 
  SELECT
--
-- Date: 18/07/2006
-- Version: 1.10
--
-- Estrae i dettagli del piano di gestione parte spese aggregati
-- per linea attivita considerando solo le linee di attivita
-- con Categoria di dettaglio SINGOLO (senza dettaglio) e con categoria di dettaglio
-- SCARICO e stato APPROVATO
-- Inoltre la vista ritorna il totale delle obbligazioni prese sulla linea di attività specificata
-- e nel caso che tale linea di attività sia quella automatica per costi altrui, la somma dell'obbligato sotto tale linea di attività
-- Nel caso in cui la linea di attività del servito sia scaricata su diversi CDR (anche di altra uo), la quota di tale obbligazione
-- viene qui estratta per la sua parte percentuale derivata dalla totalizzazione di quanto presente nel PDG a livello di scarico
-- di tale linea di attività su altra uo
--
-- La vista non verifica la validita dell'STO
--
-- History:
--
-- Date: 13/12/2001
-- Version: 1.3
-- Creazione
--
-- Date: 28/03/2002
-- Version: 1.4
-- Aggiunta computazione per  IM_OBB_A2, IM_OBB_A3
--
-- Date: 12/06/2002
-- Version: 1.5
-- Distinti i casi di categoria SIN e categoria SCARICO
--
-- Date: 27/08/2002
-- Version: 1.6
-- Considerati solo i casi di categoria SIN e CAR
--
-- Date: 24/09/2002
-- Version: 1.7
-- Corretta ed integrata la gestione delle totalizzazioni provenienti da altra UO
--
-- Date: 24/09/2002
-- Version: 1.8
-- Fix filtro su proprie
--
-- Date: 17/02/2003
-- Version: 1.9
-- Ottimizzazioni della vista (creato indice su scad voce e hint di ingresso per chiave primaria su OBBLIGAZIONE)
--
-- Date: 18/07/2006
-- Version: 1.10
-- Gestione Impegni/Accertamenti Residui:
-- gestito il nuovo campo ESERCIZIO_ORIGINALE
--
-- Body:
--
ESERCIZIO, CD_CENTRO_RESPONSABILITA, CD_LINEA_ATTIVITA, SUM(IM_SPESE_A1), SUM(IM_SPESE_A2), SUM (IM_SPESE_A3), SUM(IM_OBB_A1),SUM(IM_OBB_A2),SUM(IM_OBB_A3)
from
(
select
a.ESERCIZIO ESERCIZIO,
a.CD_CENTRO_RESPONSABILITA CD_CENTRO_RESPONSABILITA,
a.CD_LINEA_ATTIVITA CD_LINEA_ATTIVITA,
sum(a.IM_RI_CCS_SPESE_ODC + a.IM_RK_CCS_SPESE_OGC + a.IM_RQ_SSC_COSTI_ODC + a.IM_RS_SSC_COSTI_OGC + a.IM_RU_SPESE_COSTI_ALTRUI) IM_SPESE_A1,
sum(a.IM_RAC_A2_SPESE_ODC + a.IM_RAE_A2_SPESE_OGC + a.IM_RAG_A2_SPESE_COSTI_ALTRUI) IM_SPESE_A2,
sum(a.IM_RAL_A3_SPESE_ODC + a.IM_RAN_A3_SPESE_OGC + a.IM_RAP_A3_SPESE_COSTI_ALTRUI) IM_SPESE_A3,
0 IM_OBB_A1,
0 IM_OBB_A2,
0 IM_OBB_A3
from
V_PDG_OBBLIGAZIONE_SPE a
where a.categoria_dettaglio = 'SIN'  or
a.categoria_dettaglio = 'CAR'
group by
a.ESERCIZIO,
a.CD_CENTRO_RESPONSABILITA,
a.CD_LINEA_ATTIVITA
union all
select /*+ index (a px_obbligazione) */
b.ESERCIZIO ESERCIZIO,
b.CD_CENTRO_RESPONSABILITA CD_CENTRO_RESPONSABILITA,
b.CD_LINEA_ATTIVITA CD_LINEA_ATTIVITA,
0 IM_SPESE_A1,
0 IM_SPESE_A2,
0 IM_SPESE_A3,
decode(a.esercizio,a.esercizio_competenza,b.IM_VOCE,0) IM_OBB_A1,
decode(a.esercizio,a.esercizio_competenza+1,b.IM_VOCE,0) IM_OBB_A2,
decode(a.esercizio,a.esercizio_competenza+2,b.IM_VOCE,0) IM_OBB_A3
from
OBBLIGAZIONE a,
OBBLIGAZIONE_SCAD_VOCE b
where
    b.cd_cds = a.cd_cds
and b.esercizio = a.esercizio
and b.esercizio_originale = a.esercizio_originale
and b.pg_obbligazione = a.pg_obbligazione
and exists ( select 1 from
cdr c
where
b.cd_centro_responsabilita = c.cd_centro_responsabilita and
a.CD_UNITA_ORGANIZZATIVA = c.CD_UNITA_ORGANIZZATIVA)
union all
select
b.ESERCIZIO ESERCIZIO,
c.CD_CDR_SERVENTE CD_CENTRO_RESPONSABILITA,
c.CD_LA_SERVENTE CD_LINEA_ATTIVITA,
0 IM_SPESE_A1,
0 IM_SPESE_A2,
0 IM_SPESE_A3,
decode(a.esercizio,a.esercizio_competenza,b.IM_VOCE*c.PRC_A1,0)  IM_OBB_A1,
decode(a.esercizio,a.esercizio_competenza+1,b.IM_VOCE*c.PRC_A2,0)  IM_OBB_A2,
decode(a.esercizio,a.esercizio_competenza+2,b.IM_VOCE*c.PRC_A3,0)  IM_OBB_A3
from
OBBLIGAZIONE a,
OBBLIGAZIONE_SCAD_VOCE b,
V_PDG_LA_SERVITO_SERVENTE c
where
    b.cd_cds = a.cd_cds
and b.esercizio = a.esercizio
and b.esercizio_originale = a.esercizio_originale
and b.pg_obbligazione = a.pg_obbligazione
and b.cd_centro_responsabilita = c.cd_cdr_servito
and a.cd_unita_organizzativa = c.cd_uo_servente
and b.cd_linea_Attivita = c.cd_la_servito
)
group by
ESERCIZIO,
CD_CENTRO_RESPONSABILITA,
CD_LINEA_ATTIVITA;

   COMMENT ON TABLE "V_OBBLIG_PDG_SALDO_LA"  IS 'Estrae i dettagli del piano di gestione parte spese aggregati
per linea attivita considerando solo le linee di attivita
con Categoria di dettaglio SINGOLO (senza dettaglio) e con categoria di dettaglio
SCARICO e stato APPROVATO
Inoltre la vista ritorna il totale delle obbligazioni prese sulla linea di attività specificata
e nel caso che tale linea di attività sia quella automatica per costi altrui, la somma dell''obbligato sotto tale linea di attività
Nel caso in cui la linea di attività del servito sia scaricata su diversi CDR (anche di altra uo), la quota di tale obbligazione
viene qui estratta per la sua parte percentuale derivata dalla totalizzazione di quanto presente nel PDG a livello di scarico
di tale linea di attività su altra uo
La vista non verifica la validita dell''STO';
