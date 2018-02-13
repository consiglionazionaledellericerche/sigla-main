--------------------------------------------------------
--  DDL for View V_PRE_SCAGLIONE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_PRE_SCAGLIONE" ("CD_CONTRIBUTO_RITENUTA", "TI_ANAGRAFICO", "DT_INIZIO_VALIDITA", "IM_INFERIORE", "CD_REGIONE", "CD_PROVINCIA", "PG_COMUNE", "TI_ENTE_PERCIPIENTE", "IM_SUPERIORE", "ALIQUOTA_ENTE", "ALIQUOTA_PERCIP", "BASE_CALCOLO_ENTE", "BASE_CALCOLO_PERCIP", "DT_FINE_VALIDITA") AS 
  SELECT
--==============================================================================
--
-- Date: 02/09/2002
-- Version: 1.0
--
-- Estrazione scaglioni, mantengo il dettaglio di ogni singolo record
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
       DECODE(ti_ente_percipiente,'*','P',ti_ente_percipiente),
       im_superiore,
       aliquota,
       0,
       base_calcolo,
       0,
       dt_fine_validita
FROM   SCAGLIONE
WHERE  (ti_ente_percipiente = 'E' OR ti_ente_percipiente = '*')
UNION ALL
SELECT cd_contributo_ritenuta,
       ti_anagrafico,
       dt_inizio_validita,
       im_inferiore,
       cd_regione,
       cd_provincia,
       pg_comune,
       DECODE(ti_ente_percipiente,'*','E',ti_ente_percipiente),
       im_superiore,
       0,
       aliquota,
       0,
       base_calcolo,
       dt_fine_validita
FROM   SCAGLIONE
WHERE  (ti_ente_percipiente = 'P' OR ti_ente_percipiente = '*')
;
