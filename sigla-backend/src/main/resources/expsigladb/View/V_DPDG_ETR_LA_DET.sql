--------------------------------------------------------
--  DDL for View V_DPDG_ETR_LA_DET
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_DPDG_ETR_LA_DET" ("ESERCIZIO", "CD_CENTRO_RESPONSABILITA", "CD_LINEA_ATTIVITA", "TI_APPARTENENZA", "CD_ELEMENTO_VOCE", "PG_ENTRATA", "CD_INSIEME_LA", "CD_NATURA", "IM_RA", "IM_RC", "IM_RE", "IM_RG") AS 
  (SELECT
--
-- Date: 09/07/2002
-- Version: 1.2
--
-- Vista dinamica delle entrate del pdg,
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
-- Aggiunta la natura
--
-- Date: 09/07/2002
-- Version: 1.2
-- Vengono estratti solo dettagli confermati
--
-- Body:
--
a.ESERCIZIO,
a.CD_CENTRO_RESPONSABILITA,
a.CD_LINEA_ATTIVITA,
a.TI_APPARTENENZA,
a.CD_ELEMENTO_VOCE,
a.PG_ENTRATA,
b.CD_INSIEME_LA,
b.CD_NATURA,
a.IM_RA_RCE,
a.IM_RC_ESR,
a.IM_RE_A2_ENTRATE,
a.IM_RG_A3_ENTRATE
FROM PDG_PREVENTIVO_ETR_DET a,LINEA_ATTIVITA b
WHERE a.CD_LINEA_ATTIVITA = b.CD_LINEA_ATTIVITA
AND a.CD_CENTRO_RESPONSABILITA = b.CD_CENTRO_RESPONSABILITA
AND b.TI_GESTIONE = 'E'
AND a.stato = 'Y'
)
;

   COMMENT ON TABLE "V_DPDG_ETR_LA_DET"  IS 'Vista dinamica delle entrate del pdg, con in piu la colonna del cd_insieme_la corrispondente';
