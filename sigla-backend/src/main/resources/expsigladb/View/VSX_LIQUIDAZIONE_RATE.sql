--------------------------------------------------------
--  DDL for View VSX_LIQUIDAZIONE_RATE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VSX_LIQUIDAZIONE_RATE" ("PG_CALL", "PAR_NUM", "PROC_NAME", "MESSAGETOUSER", "CD_CDS", "CD_UNITA_ORGANIZZATIVA", "ESERCIZIO", "PG_MINICARRIERA", "PG_RATA", "ESERCIZIO_COMPETENZA", "TI_APPARTENENZA", "TI_GESTIONE", "CD_ELEMENTO_VOCE", "CD_VOCE", "CD_CENTRO_RESPONSABILITA", "CD_LINEA_ATTIVITA", "DACR", "UTCR", "DUVA", "UTUV", "PG_VER_REC") AS 
  SELECT
-------------------------------------------------------------------------
-- Date: 08/01/2003
-- Version: 1.0
--
-- Vista VSX per per gestione passaggio parametri a stored procedure per
-- liquidazione rate minicarriera
--
-- History:
--
-- Date: 08/01/2003
-- Version: 1.0
-- Creazione
--
-- Body:
--
-------------------------------------------------------------------------
PG_CALL,
PAR_NUM,
PROC_NAME,
MESSAGETOUSER,
STR01,
STR02,
INT01,
LONG01,
LONG02,
INT02,
STR03,
STR04,
STR05,
STR06,
STR07,
STR08,
DACR,
UTCR,
DUVA,
UTUV,
PG_VER_REC
FROM
STP_CALL_EXTRA_PAR
WHERE
PROC_NAME = 'CNRCTB610.liquidazMassivaMinicarriere'
;

   COMMENT ON TABLE "VSX_LIQUIDAZIONE_RATE"  IS 'Vista VSX per per gestione passaggio parametri a stored procedure per
liquidazione rate minicarriera';
