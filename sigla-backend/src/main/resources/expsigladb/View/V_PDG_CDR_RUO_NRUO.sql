--------------------------------------------------------
--  DDL for View V_PDG_CDR_RUO_NRUO
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_PDG_CDR_RUO_NRUO" ("CD_CDR_ROOT", "ESERCIZIO", "ESERCIZIO_INIZIO", "ESERCIZIO_FINE", "CD_CENTRO_RESPONSABILITA", "CD_UNITA_ORGANIZZATIVA", "DS_CDR", "LIVELLO", "CD_PROPRIO_CDR", "CD_RESPONSABILE", "INDIRIZZO", "DACR", "UTCR", "DUVA", "UTUV", "PG_VER_REC", "CD_CDR_AFFERENZA") AS 
  SELECT
--
-- Date: 23/09/2002
-- Version: 1.3
--
-- Vista di estrazione dei CDR RUO/NRUO subordinati a CDR_ROOT (RUO) (con codice cd_cdr_root)
--
--   CDR di I livello o II livello RUO -> la vista estrae i CDR che appartengono all'UO di cui CDR_ROOT e responsabile
--   CDR di II livello di macroistituto NRUO -> la vista non estrae nulla
-- Filtra solo STO valide nell'esercizio specificato
--
-- History:
--
-- Date: 22/10/2001
-- Version: 1.0
-- Creazione
--
-- Date: 13/11/2001
-- Version: 1.1
-- Eliminazione esercizio da STO
--
-- Date: 08/08/2002
-- Version: 1.2
-- Fix su estrazione NRUO -> non deve ritornare CDR
--
-- Date: 23/09/2002
-- Version: 1.3
-- Ottimizzazione su cdr/uo validi
--
-- Body:
--
CDR_ROOT.CD_CENTRO_RESPONSABILITA,
e.ESERCIZIO,
CDR.ESERCIZIO_INIZIO,
CDR.ESERCIZIO_FINE,
CDR.CD_CENTRO_RESPONSABILITA,
CDR.CD_UNITA_ORGANIZZATIVA,
CDR.DS_CDR,
CDR.LIVELLO,
CDR.CD_PROPRIO_CDR,
CDR.CD_RESPONSABILE,
CDR.INDIRIZZO,
CDR.DACR,
CDR.UTCR,
CDR.DUVA,
CDR.UTUV,
CDR.PG_VER_REC,
CDR.CD_CDR_AFFERENZA
FROM
CDR CDR,
CDR CDR_ROOT,
UNITA_ORGANIZZATIVA UO,
ESERCIZIO E
WHERE
UO.CD_UNITA_ORGANIZZATIVA = CDR_ROOT.CD_UNITA_ORGANIZZATIVA
and CDR.CD_UNITA_ORGANIZZATIVA = CDR_ROOT.CD_UNITA_ORGANIZZATIVA
and TO_NUMBER(CDR_ROOT.cd_proprio_cdr) = 0
and e.cd_cds = substr(CDR_ROOT.cd_unita_organizzativa,1,3)
and CDR.esercizio_inizio <= e.esercizio
and CDR.esercizio_fine >= e.esercizio
;

   COMMENT ON TABLE "V_PDG_CDR_RUO_NRUO"  IS 'Vista di estrazione dei CDR RUO/NRUO subordinati a CDR_ROOT (RUO) (con codice cd_cdr_root)

   CDR di I livello o II livello RUO -> la vista estrae i CDR che appartengono all''UO di cui CDR_ROOT e responsabile
   CDR di II livello di macroistituto NRUO -> la vista non estrae nulla
 Filtra solo STO valide nell''esercizio specificato';
