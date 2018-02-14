--------------------------------------------------------
--  DDL for View V_TERMINI_PAGAMENTO_ANAG
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_TERMINI_PAGAMENTO_ANAG" ("CD_ANAG", "CD_TERZO", "CD_TERMINI_PAG", "DS_TERMINI_PAG", "DACR", "UTCR", "DUVA", "UTUV", "PG_VER_REC") AS 
  SELECT
-- 
-- Date: 01/05/2001 
-- Version: 1.0 
--
-- Vista estrazione termini pagamento per entita anagrafica
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
       TP.cd_termini_pag,
       RTP.ds_termini_pag,
       TP.dacr, 
       TP.utcr, 
       TP.duva, 
       TP.utuv, 
       TP.pg_ver_rec
FROM   TERZO T,
       TERMINI_PAGAMENTO TP, 
       RIF_TERMINI_PAGAMENTO RTP
WHERE  TP.cd_terzo =  T.cd_terzo AND 
       RTP.cd_termini_pag = TP.cd_termini_pag;

   COMMENT ON TABLE "V_TERMINI_PAGAMENTO_ANAG"  IS 'Vista estrazione termini pagamento per entita anagrafica';
