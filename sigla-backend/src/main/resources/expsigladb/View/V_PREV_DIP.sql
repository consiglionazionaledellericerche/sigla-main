--------------------------------------------------------
--  DDL for View V_PREV_DIP
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_PREV_DIP" ("MATRICOLA", "NOMINATIVO", "RAPPORTO", "ENTE", "CD_TERZO", "SEDE") AS 
  SELECT
-- Date: 19/06/2003
-- Version: 1.0
--
-- Elenco iscrizione previdenziale dipendenti
--
-- History:
--
-- Date: 19/06/2003
-- Version: 1.0
--
-- Body
--
   	 prt.MATRICOLA
	,prt.NOMINATIVO
	,prt.RAPPORTO
	,prt.ENTE
	,prt.CD_TERZO
	,prt.SEDE
FROM PRT_PREV_DIP prt
;

   COMMENT ON TABLE "V_PREV_DIP"  IS '-- Elenco iscrizione previdenziale dipendenti';
