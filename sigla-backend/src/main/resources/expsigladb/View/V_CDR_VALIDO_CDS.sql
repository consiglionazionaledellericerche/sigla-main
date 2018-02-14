--------------------------------------------------------
--  DDL for View V_CDR_VALIDO_CDS
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_CDR_VALIDO_CDS" ("ESERCIZIO", "ESERCIZIO_INIZIO", "CD_CENTRO_RESPONSABILITA", "CD_UNITA_ORGANIZZATIVA", "LIVELLO", "CD_PROPRIO_CDR", "DS_CDR", "CD_CDR_AFFERENZA", "CD_RESPONSABILE", "INDIRIZZO", "ESERCIZIO_FINE", "DACR", "UTCR", "DUVA", "UTUV", "PG_VER_REC", "CD_CDS") AS 
  SELECT
--
-- Date: 22/08/2005
-- Version: 1.0
--
-- View che estrae anche il cds a partire dalla view V_CDR_VALIDO
--
a.ESERCIZIO,
a.ESERCIZIO_INIZIO,
a.CD_CENTRO_RESPONSABILITA,
a.CD_UNITA_ORGANIZZATIVA,
a.LIVELLO,
a.CD_PROPRIO_CDR,
a.DS_CDR,
a.CD_CDR_AFFERENZA,
a.CD_RESPONSABILE,
a.INDIRIZZO,
a.ESERCIZIO_FINE,
a.DACR,
a.UTCR,
a.DUVA,
a.UTUV,
a.PG_VER_REC,
c.cd_unita_padre
FROM
V_CDR_VALIDO a,
V_UNITA_ORGANIZZATIVA_VALIDA c
Where
a.esercizio = c.esercizio
And  a.cd_unita_organizzativa = c.cd_unita_organizzativa
;
