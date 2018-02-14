--------------------------------------------------------
--  DDL for View V_UTENTE_CDS
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_UTENTE_CDS" ("CD_UTENTE", "DT_INIZIO_VALIDITA", "DS_UTENTE", "CD_GESTORE", "CD_CDR", "FL_PASSWORD_CHANGE", "PASSWORD", "CD_CDS_CONFIGURATORE", "DT_FINE_VALIDITA", "TI_UTENTE", "NOME", "COGNOME", "INDIRIZZO", "CD_UTENTE_TEMPL", "FL_UTENTE_TEMPL", "DUVA", "UTUV", "DACR", "UTCR", "PG_VER_REC", "DT_ULTIMA_VAR_PASSWORD", "ESERCIZIO") AS 
  (SELECT  
--  
-- Date: 09/11/2001  
-- Version: 1.1  
--  
-- Ritorna tutti gli utenti amministratori e gli esercizi per i quali sono  
-- definiti i CDS associati agli utenti  
-- Verifica la validita dell'STO
--  
-- History:  
--  
-- Date: 01/07/2001  
-- Version: 1.0  
-- Creazione
--
-- Date: 18/10/2001  
-- Version: 1.1  
-- Aggiunto il select distinct su seconda parte union per modifica chiave primaria tabella esercizio  
--  
-- Date: 09/11/2001  
-- Version: 1.2  
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
UNITA_ORGANIZZATIVA.ESERCIZIO  
FROM  
UTENTE,  
V_UNITA_ORGANIZZATIVA_VALIDA UNITA_ORGANIZZATIVA  
WHERE  
UTENTE.TI_UTENTE = 'A' AND  
UNITA_ORGANIZZATIVA.FL_CDS = 'Y' AND  
UTENTE.CD_CDS_CONFIGURATORE = UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA  
UNION  
SELECT DISTINCT  
-- Ritorna tutti gli utenti amministratori con CDS = '*' combinati con tutti i possibili  
-- esercizi contabili  
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
ESERCIZIO.ESERCIZIO  
FROM  
UTENTE,  
ESERCIZIO  
WHERE  
UTENTE.TI_UTENTE = 'A' AND  
UTENTE.CD_CDS_CONFIGURATORE = '*');

   COMMENT ON TABLE "V_UTENTE_CDS"  IS 'Ritorna tutti gli utenti amministratori e gli esercizi per i quali sono
definiti i CDS associati agli utenti
Verifica la validita dell''STO';
