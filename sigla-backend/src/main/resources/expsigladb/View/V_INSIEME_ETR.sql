--------------------------------------------------------
--  DDL for View V_INSIEME_ETR
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_INSIEME_ETR" ("ESERCIZIO", "CD_CENTRO_RESPONSABILITA", "CD_INSIEME_LA", "IM_A1", "IM_A2", "IM_A3") AS 
  (SELECT
--
-- Date: 26/06/2002
-- Version: 1.2
--
-- Vista degli importi di entrata, sommati per insieme di linea attivita
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
-- Estrae solo aggregati per insieme non nullo
--
-- Date: 26/06/2002
-- Version: 1.2
-- Estrae solo dettaggli di natura 1,2,3
--
-- Body:
--
a.ESERCIZIO,
a.CD_CENTRO_RESPONSABILITA,
a.CD_INSIEME_LA,
SUM(a.IM_RA + a.IM_RC),
SUM(a.IM_RE),
SUM(a.IM_RG)
FROM V_DPDG_ETR_LA_DET a
WHERE
     a.cd_insieme_la is not null
 and a.cd_natura in ('1','2','3')
GROUP BY a.CD_INSIEME_LA,a.ESERCIZIO,a.CD_CENTRO_RESPONSABILITA
)
;

   COMMENT ON TABLE "V_INSIEME_ETR"  IS 'Vista degli importi di entrata, sommati per insieme di linea attivita per costruzione vista delle discrepanze (solo su nature 1,2,3)';
