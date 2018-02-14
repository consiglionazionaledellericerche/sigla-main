--------------------------------------------------------
--  DDL for View V_CDR_VALIDO
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_CDR_VALIDO" ("ESERCIZIO", "ESERCIZIO_INIZIO", "CD_CENTRO_RESPONSABILITA", "CD_UNITA_ORGANIZZATIVA", "LIVELLO", "CD_PROPRIO_CDR", "DS_CDR", "CD_CDR_AFFERENZA", "CD_RESPONSABILE", "INDIRIZZO", "ESERCIZIO_FINE", "DACR", "UTCR", "DUVA", "UTUV", "PG_VER_REC") AS 
  SELECT  
--  
-- Date: 14/11/2001  
-- Version: 1.2
--  
-- Elenco di tutti i CDR con in piu la colonna che riporta il codice del CDS  
--  
-- History:  
--  
-- Date: 12/07/2001  
-- Version: 1.0  
-- Creazione
--
-- Date: 09/11/2001  
-- Version: 1.1  
-- aggiunto ESERCIZIO_FINE
--
-- Date: 14/11/2001  
-- Version: 1.2  
-- eliminato controllo ESERCIZIO_FINE IS NULL
-- Body:  
--  
b.ESERCIZIO,  
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
CDR a,  
V_ESERCIZI b  
WHERE  
b.esercizio >= a.esercizio_inizio  
and   
b.esercizio <= a.esercizio_fine;

   COMMENT ON TABLE "V_CDR_VALIDO"  IS 'Elenco di tutti i CDR con in piu la colonna che riporta il codice del CDS';
