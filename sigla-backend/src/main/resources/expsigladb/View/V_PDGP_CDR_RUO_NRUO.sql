--------------------------------------------------------
--  DDL for View V_PDGP_CDR_RUO_NRUO
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_PDGP_CDR_RUO_NRUO" ("CD_CDR_ROOT", "ESERCIZIO", "ESERCIZIO_INIZIO", "ESERCIZIO_FINE", "CD_CENTRO_RESPONSABILITA", "CD_UNITA_ORGANIZZATIVA", "DS_CDR", "LIVELLO", "CD_PROPRIO_CDR", "CD_RESPONSABILE", "INDIRIZZO", "DACR", "UTCR", "DUVA", "UTUV", "PG_VER_REC", "CD_CDR_AFFERENZA") AS 
  SELECT CDR_ROOT.CD_CENTRO_RESPONSABILITA,
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
FROM CDR CDR, CDR CDR_ROOT, UNITA_ORGANIZZATIVA UO, ESERCIZIO E
WHERE UO.CD_UNITA_ORGANIZZATIVA = CDR_ROOT.CD_UNITA_ORGANIZZATIVA
And   substr(CDR.cd_unita_organizzativa,1,3) = substr(CDR_ROOT.cd_unita_organizzativa,1,3)
and   TO_NUMBER(CDR_ROOT.cd_proprio_cdr) = 0
and   e.cd_cds = substr(CDR_ROOT.cd_unita_organizzativa,1,3)
and   CDR.esercizio_inizio <= e.esercizio
and   CDR.esercizio_fine >= e.esercizio
;
