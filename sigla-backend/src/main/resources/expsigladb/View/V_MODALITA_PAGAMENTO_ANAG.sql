--------------------------------------------------------
--  DDL for View V_MODALITA_PAGAMENTO_ANAG
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_MODALITA_PAGAMENTO_ANAG" ("CD_ANAG", "CD_TERZO", "CD_TERZO_DELEGATO", "CD_MODALITA_PAG", "DS_MODALITA_PAG", "CODICE_CASSIERE", "TI_PAGAMENTO", "DACR", "UTCR", "DUVA", "UTUV", "PG_VER_REC") AS 
  SELECT
-- 
-- Date: 01/05/2001 
-- Version: 1.0 
--
-- Vista estrazione modalita di pagamento per entita anagrafica
--
-- History: 
-- Date: 01/05/2001 
-- Version: 1.0 
-- Creazione
-- 
-- Body:
--
       T.cd_anag, 
       T.cd_terzo, 
       MP.cd_terzo_delegato, 
       MP.cd_modalita_pag,
       RMP.ds_modalita_pag,
       RMP.codice_cassiere,
       RMP.ti_pagamento,
       MP.dacr, 
       MP.utcr, 
       MP.duva, 
       MP.utuv, 
       MP.pg_ver_rec 
FROM   TERZO T,
       MODALITA_PAGAMENTO MP,
       RIF_MODALITA_PAGAMENTO RMP
WHERE  MP.cd_terzo = T.cd_terzo AND
       RMP.cd_modalita_pag = MP.cd_modalita_pag;

   COMMENT ON TABLE "V_MODALITA_PAGAMENTO_ANAG"  IS 'Vista estrazione modalita di pagamento per entita anagrafica';
