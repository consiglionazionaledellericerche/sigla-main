--------------------------------------------------------
--  DDL for View V_DIPENDENTE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_DIPENDENTE" ("ESERCIZIO", "MESE", "TI_PREV_CONS", "ID_MATRICOLA", "CD_UNITA_ORGANIZZATIVA", "NOMINATIVO", "DT_SCAD_CONTRATTO", "CD_LIVELLO_1", "CD_LIVELLO_2", "CD_LIVELLO_3", "CD_PROFILO", "DS_PROFILO", "ORIGINE_FONTI") AS 
  SELECT DISTINCT
--
-- Date: 26/09/2007
-- Version: 1.3
--
-- Vista che restituisce l'elenco delle matricole per unita organizzativa
-- aggregando la tabella COSTI_DEL_DIPENDENTE
--
-- History:
--
-- Date: 22/03/2002
-- Version: 1.0
-- Creazione
--
-- Date: 27/03/2002
-- Version: 1.1
-- Aggiunti i campi NOMINATIVO, DT_SCAD_CONTRATTO, CD_LIVELLO_x, CD_PROFILO, DS_PROFILO
--
-- Date: 19/09/2002
-- Version: 1.2
-- Aggiunta colonna MESE
--
-- Date: 26/09/2007
-- Version: 1.3
-- Aggiunta colonna ORIGINE_FONTI
--
-- Body:
--
ESERCIZIO,
MESE,
TI_PREV_CONS,
ID_MATRICOLA,
CD_UNITA_ORGANIZZATIVA,
NOMINATIVO,
DT_SCAD_CONTRATTO,
CD_LIVELLO_1,
CD_LIVELLO_2,
CD_LIVELLO_3,
CD_PROFILO,
DS_PROFILO,
ORIGINE_FONTI
FROM
COSTO_DEL_DIPENDENTE;

   COMMENT ON TABLE "V_DIPENDENTE"  IS 'Vista che restituisce l''elenco delle matricole per unità organizzativa
aggregando la tabella COSTI_DEL_DIPENDENTE';
