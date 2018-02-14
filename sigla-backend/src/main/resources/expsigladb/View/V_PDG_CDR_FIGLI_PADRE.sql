--------------------------------------------------------
--  DDL for View V_PDG_CDR_FIGLI_PADRE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_PDG_CDR_FIGLI_PADRE" ("CD_CDR_ROOT", "ESERCIZIO", "ESERCIZIO_INIZIO", "ESERCIZIO_FINE", "CD_CENTRO_RESPONSABILITA", "CD_UNITA_ORGANIZZATIVA", "DS_CDR", "LIVELLO", "CD_PROPRIO_CDR", "CD_RESPONSABILE", "INDIRIZZO", "DACR", "UTCR", "DUVA", "UTUV", "PG_VER_REC", "CD_CDR_AFFERENZA") AS 
  SELECT 
-- 
-- Date: 12/02/2002 
-- Version: 1.0 
-- 
-- Vista di estrazione dei CDR subordinati a CDR_ROOT (con codice cd_cdr_root) secondo la gerarchia imposta dal PDG 
-- piu il CDR padre. Utilizza la vista V_PDG_CDR_FIGLI per l'estrazione dei figli e V_CDR_VALIDO per quella del padre 
-- 
-- History: 
-- 
-- Date: 12/02/2001 
-- Version: 1.0 
-- Creazione 
-- 
-- Body: 
-- 
CD_CDR_ROOT, 
ESERCIZIO, ESERCIZIO_INIZIO, ESERCIZIO_FINE, CD_CENTRO_RESPONSABILITA, 
CD_UNITA_ORGANIZZATIVA, DS_CDR, LIVELLO, CD_PROPRIO_CDR, 
CD_RESPONSABILE, INDIRIZZO, DACR, UTCR, 
DUVA, UTUV, PG_VER_REC, CD_CDR_AFFERENZA 
FROM 
 V_PDG_CDR_FIGLI 
union 
select 
CD_CENTRO_RESPONSABILITA, 
ESERCIZIO, ESERCIZIO_INIZIO, ESERCIZIO_FINE, CD_CENTRO_RESPONSABILITA, 
CD_UNITA_ORGANIZZATIVA, DS_CDR, LIVELLO, CD_PROPRIO_CDR, 
CD_RESPONSABILE, INDIRIZZO, DACR, UTCR, 
DUVA, UTUV, PG_VER_REC, CD_CDR_AFFERENZA 
from V_CDR_VALIDO;

   COMMENT ON TABLE "V_PDG_CDR_FIGLI_PADRE"  IS 'Vista di estrazione dei CDR subordinati a CDR_ROOT (con codice cd_cdr_root) secondo la gerarchia imposta dal PDG 
piu il CDR padre. Utilizza la vista V_PDG_CDR_FIGLI per l''estrazione dei figli e V_CDR_VALIDO per quella del padre';
