--------------------------------------------------------
--  DDL for View V_DISCREPANZE_INSIEME
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_DISCREPANZE_INSIEME" ("ESERCIZIO", "CD_CENTRO_RESPONSABILITA", "CD_INSIEME_LA", "IM_1_ETR", "IM_1_SPE", "IM_2_ETR", "IM_2_SPE", "IM_3_ETR", "IM_3_SPE") AS 
  (SELECT
--
-- Date: 26/06/2002
-- Version: 1.2
--
-- Vista per l'estrazione degli importi discrepanti per
-- insiemi di linee di attivita (nature 1,2,3)
--
-- History:
--
-- Date: 16/04/2002
-- Version: 1.0
-- Creazione
--
-- Date: 30/04/2002
-- Version: 1.1
-- Estrae le squadrature e/s su insiemi che collegano entrate a spese
--
-- Date: 26/06/2002
-- Version: 1.2
-- Cambiato il nome delle colonne
--
-- Body:
--
a.ESERCIZIO,
a.CD_CENTRO_RESPONSABILITA,
a.CD_INSIEME_LA,
a.IM_A1,
b.IM_A1,
a.IM_A2,
b.IM_A2,
a.IM_A3,
b.IM_A3
FROM V_INSIEME_ETR a,V_INSIEME_SPE b
WHERE a.CD_INSIEME_LA = b.CD_INSIEME_LA
AND a.ESERCIZIO = b.ESERCIZIO
AND a.CD_CENTRO_RESPONSABILITA = b.CD_CENTRO_RESPONSABILITA
union all
select
a.ESERCIZIO,
a.CD_CENTRO_RESPONSABILITA,
a.CD_INSIEME_LA,
a.IM_A1,
0,
a.IM_A2,
0,
a.IM_A3,
0
FROM V_INSIEME_ETR a
WHERE
 exists (select 1 from linea_attivita where
          esercizio = a.esercizio
		  and cd_centro_responsabilita = a.cd_centro_responsabilita
		  and cd_insieme_la = a.cd_insieme_la
		  and ti_gestione = 'S')
 and not exists (select 1 from V_INSIEME_SPE
 where
      CD_INSIEME_LA = a.CD_INSIEME_LA
  AND ESERCIZIO = a.ESERCIZIO
  AND CD_CENTRO_RESPONSABILITA = a.CD_CENTRO_RESPONSABILITA)
union all
select
a.ESERCIZIO,
a.CD_CENTRO_RESPONSABILITA,
a.CD_INSIEME_LA,
0,
a.IM_A1,
0,
a.IM_A2,
0,
a.IM_A3
FROM V_INSIEME_SPE a
WHERE
 exists (select 1 from linea_attivita where
          esercizio = a.esercizio
		  and cd_centro_responsabilita = a.cd_centro_responsabilita
		  and cd_insieme_la = a.cd_insieme_la
		  and ti_gestione = 'E')
 and not exists (select 1 from V_INSIEME_ETR
 where
      CD_INSIEME_LA = a.CD_INSIEME_LA
  AND ESERCIZIO = a.ESERCIZIO
  AND CD_CENTRO_RESPONSABILITA = a.CD_CENTRO_RESPONSABILITA)
)
;

   COMMENT ON TABLE "V_DISCREPANZE_INSIEME"  IS 'Vista per l''estrazione degli importi discrepanti per insiemi di linee di attivita (nature 1,2,3)';
