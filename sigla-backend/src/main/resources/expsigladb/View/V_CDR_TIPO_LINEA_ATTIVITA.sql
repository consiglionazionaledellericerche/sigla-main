--------------------------------------------------------
--  DDL for View V_CDR_TIPO_LINEA_ATTIVITA
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_CDR_TIPO_LINEA_ATTIVITA" ("CD_TIPO_LINEA_ATTIVITA", "ESERCIZIO_INIZIO", "CD_CENTRO_RESPONSABILITA", "CD_UNITA_ORGANIZZATIVA", "LIVELLO", "CD_PROPRIO_CDR", "DS_CDR", "CD_CDR_AFFERENZA", "CD_RESPONSABILE", "INDIRIZZO", "ESERCIZIO_FINE", "DACR", "UTCR", "DUVA", "UTUV", "PG_VER_REC") AS 
  SELECT  
--  
-- Date: 09/11/2001  
-- Version: 2.1  
-- modifiche per eliminazione ESERCIZIO dalla chiave della struttura organizz.  
--  
-- Nella vista si entra con cd_tipo_linea_attivita ed esercizio  
-- Nel caso in ASS_TIPO_LA_CDR nel campo cd_centro_responsabilita ci sia "*",  la select  
-- sulla vista ritorna una lista con cd_centro_responsabilita = tutti cdr dell"esercizio  
-- La vista non verifica la validita dell'STO
--  
-- History:  
--  
-- Date: 01/07/2001  
-- Version: 1.0
-- Creazione
-- Date: 12/07/2001  
-- Version: 2.0
-- Fix errori di estrazione  
-- Date: 09/11/2001  
-- Version: 2.1  
-- modifiche per eliminazione ESERCIZIO dalla chiave della struttura organizz.  
--  
-- Body:  
--  
ASS_TIPO_LA_CDR.CD_TIPO_LINEA_ATTIVITA,CDR.ESERCIZIO_INIZIO, CDR.CD_CENTRO_RESPONSABILITA, CDR.CD_UNITA_ORGANIZZATIVA, CDR.LIVELLO, CDR.CD_PROPRIO_CDR, CDR.DS_CDR, CDR.CD_CDR_AFFERENZA, CDR.CD_RESPONSABILE, CDR.INDIRIZZO,  
CDR.ESERCIZIO_FINE, CDR.DACR, CDR.UTCR, CDR.DUVA, CDR.UTUV, CDR.PG_VER_REC  
FROM CDR,ASS_TIPO_LA_CDR  
WHERE  
CDR.CD_CENTRO_RESPONSABILITA = ASS_TIPO_LA_CDR.CD_CENTRO_RESPONSABILITA  
UNION  
SELECT  
ASS_TIPO_LA_CDR.CD_TIPO_LINEA_ATTIVITA,CDR.ESERCIZIO_INIZIO, CDR.CD_CENTRO_RESPONSABILITA, CDR.CD_UNITA_ORGANIZZATIVA, CDR.LIVELLO, CDR.CD_PROPRIO_CDR, CDR.DS_CDR, CDR.CD_CDR_AFFERENZA, CDR.CD_RESPONSABILE, CDR.INDIRIZZO,  
CDR.ESERCIZIO_FINE, CDR.DACR, CDR.UTCR, CDR.DUVA, CDR.UTUV, CDR.PG_VER_REC  
FROM CDR,ASS_TIPO_LA_CDR  
WHERE  
ASS_TIPO_LA_CDR.CD_CENTRO_RESPONSABILITA = '*';

   COMMENT ON TABLE "V_CDR_TIPO_LINEA_ATTIVITA"  IS 'Vista estrazione dei cdr associati ad una data linea di attivita: nel caso nella tabella di associazione sia presente "*" per il campo cd_centro_Responsabilita ritorna tutti i CDR esistenti
La vista non verifica la validita dell''STO';
