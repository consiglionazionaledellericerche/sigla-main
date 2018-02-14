--------------------------------------------------------
--  DDL for View V_SCAGLIONE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_SCAGLIONE" ("CD_CONTRIBUTO_RITENUTA", "TI_ANAGRAFICO", "DT_INIZIO_VALIDITA", "IM_INFERIORE", "CD_REGIONE", "CD_PROVINCIA", "PG_COMUNE", "IM_SUPERIORE", "ALIQUOTA_ENTE", "ALIQUOTA_PERCIP", "BASE_CALCOLO_ENTE", "BASE_CALCOLO_PERCIP", "DT_FINE_VALIDITA") AS 
  SELECT
--==============================================================================
--
-- Date: 02/09/2002
-- Version: 1.0
--
-- Estrazione scaglioni
--
-- History:
--
-- Date: 02/09/2002
-- Version: 1.0
--
-- Creazione vista
--
--
-- Body:
--
--==============================================================================
       cd_contributo_ritenuta,
       ti_anagrafico,
       dt_inizio_validita,
       im_inferiore,
       cd_regione,
       cd_provincia,
       pg_comune,
       im_superiore,
       SUM(aliquota_ente),
       SUM(aliquota_percip),
       SUM(base_calcolo_ente),
       SUM(base_calcolo_percip),
       dt_fine_validita
FROM   V_PRE_SCAGLIONE
GROUP BY cd_contributo_ritenuta,
         ti_anagrafico,
         dt_inizio_validita,
         im_inferiore,
         cd_regione,
         cd_provincia,
         pg_comune,
         im_superiore,
         dt_fine_validita
;
