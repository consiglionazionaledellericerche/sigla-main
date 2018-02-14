--------------------------------------------------------
--  DDL for View VP_UTENTE_GRANTS
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VP_UTENTE_GRANTS" ("CD_UTENTE", "TI_GRANT", "CD_UNITA_ORGANIZZATIVA", "CD_ACCESSO", "DS_ACCESSO", "DS_UNITA_ORGANIZZATIVA", "CD_CDS_RUOLO", "DS_CDS_RUOLO") AS 
  (
select
--
-- Date: 10/04/2002
-- Version: 1.1
--
-- Ritorna i grants in termini di ruoli ed accessi assegnati direttamente all'utente
--
-- History:
--
-- Date: 20/03/2002
-- Version: 1.0
-- Crezione
--
-- Date: 10/04/2002
-- Version: 1.1
-- Mancava l'outer join su ruolo
--
-- Body:
--
   b.CD_UTENTE,
  'A',
  b.CD_UNITA_ORGANIZZATIVA,
  b.CD_ACCESSO,
  bacc.DS_ACCESSO,
  buo.DS_UNITA_ORGANIZZATIVA,
  null,
  null
from
  utente_unita_accesso b
 ,accesso bacc
 ,unita_organizzativa buo
where
     bacc.cd_accesso = b.cd_accesso
 and buo.cd_unita_organizzativa = b.cd_unita_organizzativa
union all
select
  b.CD_UTENTE,
  'R',
  b.CD_UNITA_ORGANIZZATIVA,
  b.CD_RUOLO,
  bruo.DS_RUOLO,
  buo.DS_UNITA_ORGANIZZATIVA,
  bruo.CD_CDS,
  bcds.DS_UNITA_ORGANIZZATIVA
from
  utente_unita_ruolo b
 ,ruolo bruo
 ,unita_organizzativa buo
 ,unita_organizzativa bcds
where
     bruo.cd_ruolo = b.cd_ruolo
 and buo.cd_unita_organizzativa = b.cd_unita_organizzativa
 and bcds.cd_unita_organizzativa (+)= bruo.cd_cds
)
;
