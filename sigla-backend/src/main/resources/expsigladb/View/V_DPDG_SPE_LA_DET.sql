--------------------------------------------------------
--  DDL for View V_DPDG_SPE_LA_DET
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_DPDG_SPE_LA_DET" ("ESERCIZIO", "CD_CENTRO_RESPONSABILITA", "CD_LINEA_ATTIVITA", "TI_APPARTENENZA", "CD_ELEMENTO_VOCE", "PG_SPESA", "CD_INSIEME_LA", "CD_NATURA", "CD_FUNZIONE", "IM_RH", "IM_RQ", "IM_RR", "IM_RS", "IM_RT", "IM_RP", "IM_RAB", "IM_RAC", "IM_RAD", "IM_RAE", "IM_RAF", "IM_RAI", "IM_RAL", "IM_RAM", "IM_RAN", "IM_RAO") AS 
  (SELECT
--
-- Date: 09/07/2002
-- Version: 1.2
--
-- Vista dinamica delle spese del pdg,
-- con in piu la colonna del cd_insieme_la corrispondente
--
-- History:
--
-- Date: 19/10/2001
-- Version: 1.0
-- Creazione
--
-- Date: 26/06/2002
-- Version: 1.1
-- Aggiunta natura e funzione + colonne costi altro cdr sui tre anni
--
-- Date: 09/07/2002
-- Version: 1.2
-- Lo stato del dettaglio deve essere confermato altrimenti non viene considerato
--
-- Body:
--
a.ESERCIZIO,
a.CD_CENTRO_RESPONSABILITA,
a.CD_LINEA_ATTIVITA,
a.TI_APPARTENENZA,
a.CD_ELEMENTO_VOCE,
a.PG_SPESA,
b.CD_INSIEME_LA,
b.CD_NATURA,
b.CD_FUNZIONE,
a.IM_RH_CCS_COSTI,
a.IM_RQ_SSC_COSTI_ODC,
a.IM_RR_SSC_COSTI_ODC_ALTRA_UO,
a.IM_RS_SSC_COSTI_OGC,
a.IM_RT_SSC_COSTI_OGC_ALTRA_UO,
a.IM_RP_CSS_VERSO_ALTRO_CDR,
a.IM_RAB_A2_COSTI_ALTRO_CDR,
a.IM_RAC_A2_SPESE_ODC,
a.IM_RAD_A2_SPESE_ODC_ALTRA_UO,
a.IM_RAE_A2_SPESE_OGC,
a.IM_RAF_A2_SPESE_OGC_ALTRA_UO,
a.IM_RAI_A3_COSTI_ALTRO_CDR,
a.IM_RAL_A3_SPESE_ODC,
a.IM_RAM_A3_SPESE_ODC_ALTRA_UO,
a.IM_RAN_A3_SPESE_OGC,
a.IM_RAO_A3_SPESE_OGC_ALTRA_UO
FROM PDG_PREVENTIVO_SPE_DET a,LINEA_ATTIVITA b
WHERE a.CD_LINEA_ATTIVITA = b.CD_LINEA_ATTIVITA
AND a.CD_CENTRO_RESPONSABILITA = b.CD_CENTRO_RESPONSABILITA
AND b.TI_GESTIONE = 'S'
AND a.stato = 'Y'
)
;

   COMMENT ON TABLE "V_DPDG_SPE_LA_DET"  IS 'Vista dinamica delle spese del pdg, con in piu la colonna del cd_insieme_la corrispondente';
