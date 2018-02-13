--------------------------------------------------------
--  DDL for View V_CDP_MATRICOLA_UO
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_CDP_MATRICOLA_UO" ("ESERCIZIO", "MESE", "ID_MATRICOLA", "CD_UNITA_ORGANIZZATIVA") AS 
  (select distinct
--
-- Date: 19/09/2002
-- Version: 1.1
--
-- Vista di estrazione delle matricole per UO
--
-- History:
-- Date: 19/10/2001
-- Version: 1.0
-- Creazione
--
-- Date: 19/09/2002
-- Version: 1.1
-- Aggiunta colonna MESE
--
-- Body:
--
 esercizio,
 MESE,
 id_matricola,
 cd_unita_organizzativa
from
 costo_del_dipendente)
;

   COMMENT ON TABLE "V_CDP_MATRICOLA_UO"  IS 'Vista di estrazione delle matricole per UO';
