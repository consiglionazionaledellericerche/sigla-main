--------------------------------------------------------
--  DDL for View V_CDR_PDG_AGGREGATO
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_CDR_PDG_AGGREGATO" ("ESERCIZIO", "ESERCIZIO_INIZIO", "CD_CENTRO_RESPONSABILITA", "CD_UNITA_ORGANIZZATIVA", "LIVELLO", "CD_PROPRIO_CDR", "DS_CDR", "CD_CDR_AFFERENZA", "CD_RESPONSABILE", "INDIRIZZO", "ESERCIZIO_FINE", "DACR", "UTCR", "DUVA", "UTUV", "PG_VER_REC") AS 
  SELECT   
--  
-- Date: 09/11/2001  
-- Version: 1.3  
--  
-- Elenco dei CDR responsabili di un PDG_AGGREGATO (i CDR di I livello piu i CDR responsabili di un area di ricerca  
-- La vista effettua controlli di validita STO
--
-- History:  
--  
-- Date: 12/07/2001  
-- Version: 1.0
-- Creazione
-- Date: 09/11/2001  
-- Version: 1.1  
-- Correzione errore: mancava ESERCIZIO_INIZIO
--
-- Version: 1.2  
-- modifiche per eliminazione ESERCIZIO dalla chiave della struttura organizz.  
--
-- Version: 1.3  
-- Correzione errore: mancava join tra CDR.CD_UNITA_ORGANIZZATIVA e UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA
--
-- Body:  
--  
CDR.ESERCIZIO, CDR.ESERCIZIO_INIZIO,  
CDR.CD_CENTRO_RESPONSABILITA, CDR.CD_UNITA_ORGANIZZATIVA, CDR.LIVELLO, CDR.CD_PROPRIO_CDR,   
CDR.DS_CDR, CDR.CD_CDR_AFFERENZA, CDR.CD_RESPONSABILE, CDR.INDIRIZZO,   
CDR.ESERCIZIO_FINE, CDR.DACR, CDR.UTCR, CDR.DUVA,  
CDR.UTUV, CDR.PG_VER_REC   
FROM   
V_CDR_VALIDO CDR,   
V_UNITA_ORGANIZZATIVA_VALIDA UNITA_ORGANIZZATIVA   
WHERE   
CDR.CD_UNITA_ORGANIZZATIVA = UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA AND
CDR.ESERCIZIO = UNITA_ORGANIZZATIVA.ESERCIZIO AND
( CDR.LIVELLO = '1' OR   
UNITA_ORGANIZZATIVA.CD_TIPO_UNITA = 'AREA');

   COMMENT ON TABLE "V_CDR_PDG_AGGREGATO"  IS 'Elenco dei CDR responsabili di un PDG_AGGREGATO (i CDR di I livello piu i CDR responsabili di un area di ricerca  
La vista effettua controlli di validita STO';
