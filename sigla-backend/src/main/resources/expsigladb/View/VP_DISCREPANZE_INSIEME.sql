--------------------------------------------------------
--  DDL for View VP_DISCREPANZE_INSIEME
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VP_DISCREPANZE_INSIEME" ("ESERCIZIO", "CD_CENTRO_RESPONSABILITA", "CD_INSIEME_LA", "IM_1_ETR", "IM_1_SPE", "IM_2_ETR", "IM_2_SPE", "IM_3_ETR", "IM_3_SPE", "DS_CDR", "DS_INSIEME_LA") AS 
  (SELECT
--
-- Date: 30/04/2002
-- Version: 1.2
--
-- Vista per la stampa degli importi discrepanti per
-- insiemi di linee di attivita
--
-- History:
--
-- Date: 17/04/2002
-- Version: 1.0
-- Creazione
--
-- Date: 18/04/2002
-- Version: 1.1
-- Correzione della join con tabella cdr e insieme_la
--
-- Date: 30/04/2002
-- Version: 1.2
-- La vista e costruita su V_DISCREPANZE_INSIEME
--
-- Body:
--
a.ESERCIZIO,
a.CD_CENTRO_RESPONSABILITA,
a.CD_INSIEME_LA,
a.IM_1_ETR,
a.IM_1_SPE,
a.IM_2_ETR,
a.IM_2_SPE,
a.IM_3_ETR,
a.IM_3_SPE,
c.DS_CDR,
d.DS_INSIEME_LA
FROM V_DISCREPANZE_INSIEME a, CDR c,INSIEME_LA d
WHERE
    a.CD_CENTRO_RESPONSABILITA = c.CD_CENTRO_RESPONSABILITA
AND a.CD_INSIEME_LA = d.CD_INSIEME_LA
AND a.CD_CENTRO_RESPONSABILITA = d.CD_CENTRO_RESPONSABILITA
)
;

   COMMENT ON TABLE "VP_DISCREPANZE_INSIEME"  IS 'Vista per la stampa degli importi discrepanti per insiemi di linee di attivita';
