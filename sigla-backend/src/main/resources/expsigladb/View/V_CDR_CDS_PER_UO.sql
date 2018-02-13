--------------------------------------------------------
--  DDL for View V_CDR_CDS_PER_UO
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_CDR_CDS_PER_UO" ("CD_UO", "ESERCIZIO_INIZIO", "CD_CENTRO_RESPONSABILITA", "CD_UNITA_ORGANIZZATIVA", "LIVELLO", "CD_PROPRIO_CDR", "DS_CDR", "CD_CDR_AFFERENZA", "CD_RESPONSABILE", "INDIRIZZO", "ESERCIZIO_FINE", "DACR", "UTCR", "DUVA", "UTUV", "PG_VER_REC") AS 
  SELECT  
--  
--  
-- Date: 09/11/2001  
-- Version: 1.1  
--  
-- Elenco di tutti i CDR di un CDS a cui appartiene una UO; da usare con una  
-- clausola su CD_UO  
-- La vista non effettua controlli di validita dell'STO  
--
-- History:  
--  
-- Date: 12/07/2001  
-- Version: 1.0  
-- Creazione
--  
-- Date: 09/11/2001  
-- Version: 1.1  
-- modifiche per eliminazione ESERCIZIO dalla chiave della struttura organizz.  
-- Body:  
--  
UO_UTENTE.CD_UNITA_ORGANIZZATIVA,  
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
CDR,UNITA_ORGANIZZATIVA,  
UNITA_ORGANIZZATIVA CDS,  
UNITA_ORGANIZZATIVA UO_UTENTE  
WHERE  
UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA = CDR.CD_UNITA_ORGANIZZATIVA AND  
UNITA_ORGANIZZATIVA.CD_UNITA_PADRE = CDS.CD_UNITA_ORGANIZZATIVA AND  
UO_UTENTE.CD_UNITA_PADRE = CDS.CD_UNITA_ORGANIZZATIVA;

   COMMENT ON TABLE "V_CDR_CDS_PER_UO"  IS 'Elenco di tutti i CDR di un CDS a cui appartiene una UO; da usare con una clausola su CD_UO  
La vista non effettua controlli di validita dell''STO';
