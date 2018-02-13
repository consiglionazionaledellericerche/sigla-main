--------------------------------------------------------
--  DDL for View V_UTENTE_CDR
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_UTENTE_CDR" ("CD_UTENTE", "DT_INIZIO_VALIDITA", "DS_UTENTE", "CD_GESTORE", "CD_CDR", "FL_PASSWORD_CHANGE", "PASSWORD", "CD_CDS_CONFIGURATORE", "DT_FINE_VALIDITA", "TI_UTENTE", "NOME", "COGNOME", "INDIRIZZO", "CD_UTENTE_TEMPL", "FL_UTENTE_TEMPL", "DUVA", "UTUV", "DACR", "UTCR", "PG_VER_REC", "DT_ULTIMA_VAR_PASSWORD", "ESERCIZIO") AS 
  SELECT  
--  
-- Date: 09/11/2001  
-- Version: 1.1  
--  
-- Ritorna tutti gli utenti comuni e gli esercizi per i quali sono  
-- definiti i CDR associati agli utenti  
-- Verifica la validita dell'STO
--  
-- History:  
-- Date: 13/07/2001  
-- Version: 1.0  
-- Creazione  
--  
-- Date: 09/11/2001  
-- Version: 1.1  
-- modifiche per eliminazione ESERCIZIO dalla chiave della struttura organizz.  
-- Body:  
--  
UTENTE.CD_UTENTE,  
UTENTE.DT_INIZIO_VALIDITA,  
UTENTE.DS_UTENTE,  
UTENTE.CD_GESTORE,  
UTENTE.CD_CDR,  
UTENTE.FL_PASSWORD_CHANGE,  
UTENTE.PASSWORD,  
UTENTE.CD_CDS_CONFIGURATORE,  
UTENTE.DT_FINE_VALIDITA,  
UTENTE.TI_UTENTE,  
UTENTE.NOME,  
UTENTE.COGNOME,  
UTENTE.INDIRIZZO,  
UTENTE.CD_UTENTE_TEMPL,  
UTENTE.FL_UTENTE_TEMPL,  
UTENTE.DUVA,  
UTENTE.UTUV,  
UTENTE.DACR,  
UTENTE.UTCR,  
UTENTE.PG_VER_REC,  
UTENTE.DT_ULTIMA_VAR_PASSWORD,  
CDR.ESERCIZIO  
FROM  
UTENTE,  
V_CDR_VALIDO CDR  
WHERE  
UTENTE.TI_UTENTE = 'U' AND  
UTENTE.CD_CDR = CDR.CD_CENTRO_RESPONSABILITA;

   COMMENT ON TABLE "V_UTENTE_CDR"  IS 'Ritorna tutti gli utenti comuni e gli esercizi per i quali sono
definiti i CDR associati agli utenti
Verifica la validita dell''STO';
