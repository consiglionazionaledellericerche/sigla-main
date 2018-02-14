--------------------------------------------------------
--  DDL for View V_INSIEME_SPE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_INSIEME_SPE" ("ESERCIZIO", "CD_CENTRO_RESPONSABILITA", "CD_INSIEME_LA", "IM_A1", "IM_A2", "IM_A3") AS 
  (SELECT
--
-- Date: 26/06/2002
-- Version: 1.2
--
-- Vista degli importi di spesa, sommati per insieme di linea attivita
-- per costruzione vista delle discrepanze (solo su nature 1,2,3)
--
-- History:
--
-- Date: 16/04/2002
-- Version: 1.0
-- Creazione
--
-- Date: 30/04/2002
-- Version: 1.1
-- Estrae solo insiemi diversi da null
--
-- Date: 26/06/2002
-- Version: 1.2
-- Modifica colonne estratte
-- Estrae solo dettagli di natura 1,2,3
--
-- Body:
--
a.ESERCIZIO,
a.CD_CENTRO_RESPONSABILITA,
a.CD_INSIEME_LA,
SUM(a.IM_RH+a.IM_RQ+a.IM_RR+a.IM_RS+a.IM_RT+a.IM_RP),
SUM(a.IM_RAB+a.IM_RAC+a.IM_RAD+a.IM_RAE+a.IM_RAF),
SUM(a.IM_RAL+a.IM_RAL+a.IM_RAM+a.IM_RAN+a.IM_RAO)
FROM V_DPDG_SPE_LA_DET a
WHERE
      a.CD_INSIEME_LA IS NOT NULL
  and a.cd_natura in ('1','2','3')
GROUP BY a.CD_INSIEME_LA,a.ESERCIZIO,a.CD_CENTRO_RESPONSABILITA
)
;

   COMMENT ON TABLE "V_INSIEME_SPE"  IS 'Vista degli importi di spesa, sommati per insieme di linea attivita per costruzione vista delle discrepanze (solo su nature 1,2,3)';
