--------------------------------------------------------
--  DDL for View V_CDR_VALIDO_LIV1
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_CDR_VALIDO_LIV1" ("ESERCIZIO", "ESERCIZIO_INIZIO", "CD_CENTRO_RESPONSABILITA", "CD_UNITA_ORGANIZZATIVA", "LIVELLO", "CD_PROPRIO_CDR", "DS_CDR", "CD_CDR_AFFERENZA", "CD_RESPONSABILE", "INDIRIZZO", "ESERCIZIO_FINE", "DACR", "UTCR", "DUVA", "UTUV", "PG_VER_REC") AS 
  SELECT
--
-- Date: 17/11/2005
-- Version: 1.0
--
-- Elenco di tutti i CDR validi ma di livello 1 oppure di livello 2
-- per i CDR di tipo SAC o AREA
--
-- History:
--
-- Body:
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
a.PG_VER_REC
FROM
V_CDR_VALIDO a,
UNITA_ORGANIZZATIVA b
Where
a.cd_unita_organizzativa=b.cd_unita_organizzativa
And (a.livello = 1 Or b.cd_tipo_unita In ('SAC','AREA'))

;

   COMMENT ON TABLE "V_CDR_VALIDO_LIV1"  IS 'Elenco di tutti i CDR validi ma di livello 1 oppure di livello 2 per i CDR di tipo SAC o AREA';
