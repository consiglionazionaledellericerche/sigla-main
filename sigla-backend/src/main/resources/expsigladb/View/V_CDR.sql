--------------------------------------------------------
--  DDL for View V_CDR
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_CDR" ("CD_CDS", "ESERCIZIO_INIZIO", "CD_CENTRO_RESPONSABILITA", "CD_UNITA_ORGANIZZATIVA", "LIVELLO", "CD_PROPRIO_CDR", "DS_CDR", "CD_CDR_AFFERENZA", "CD_RESPONSABILE", "INDIRIZZO", "ESERCIZIO_FINE", "DACR", "UTCR", "DUVA", "UTUV", "PG_VER_REC") AS 
  SELECT  
--  
-- Date: 09/11/2001  
-- Version: 1.1  
--  
-- Elenco di tutti i CDR con in piu la colonna che riporta il codice del CDS  
-- La vista non effettua controlli di validita su STO  
--
-- History:  
--  
-- Date: 12/07/2001  
-- Version: 1.0 
-- Creazione 
-- Date: 09/11/2001  
-- Version: 1.1  
-- Modifiche per eliminazione ESERCIZIO da struttura organiz.  
-- Body:  
--  
UNITA_ORGANIZZATIVA.CD_UNITA_PADRE,  
CDR.ESERCIZIO_INIZIO,  
CDR.CD_CENTRO_RESPONSABILITA,  
CDR.CD_UNITA_ORGANIZZATIVA,  
CDR.LIVELLO,  
CDR.CD_PROPRIO_CDR,  
CDR.DS_CDR,  
CDR.CD_CDR_AFFERENZA,  
CDR.CD_RESPONSABILE,  
CDR.INDIRIZZO,  
CDR.ESERCIZIO_FINE,  
CDR.DACR,  
CDR.UTCR,  
CDR.DUVA,  
CDR.UTUV,  
CDR.PG_VER_REC  
FROM  
CDR,  
UNITA_ORGANIZZATIVA  
WHERE  
UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA = CDR.CD_UNITA_ORGANIZZATIVA;

   COMMENT ON TABLE "V_CDR"  IS 'Elenco di tutti i CDR con in piu la colonna che riporta il codice del CDS  
La vista non effettua controlli di validita su STO';
