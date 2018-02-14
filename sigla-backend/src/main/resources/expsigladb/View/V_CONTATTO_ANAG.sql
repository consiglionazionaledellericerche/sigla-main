--------------------------------------------------------
--  DDL for View V_CONTATTO_ANAG
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_CONTATTO_ANAG" ("CD_ANAG", "CD_TERZO", "PG_CONTATTO", "DS_CONTATTO", "MOTIVO", "TELEFONO", "FAX", "EMAIL", "DACR", "DUVA", "UTUV", "UTCR", "PG_VER_REC") AS 
  SELECT
-- 
-- Date: 01/05/2001 
-- Version: 1.0 
--
-- Vista estrazione contatti per entita anagrafica
--
-- History: 
-- Date: 01/05/2001 
-- Version: 1.0 
-- Creazione
-- 
-- Body:
-- 
ANAGRAFICO.cd_anag, CONTATTO.cd_terzo, CONTATTO.pg_contatto, CONTATTO.ds_contatto, CONTATTO.motivo, CONTATTO.telefono, CONTATTO.fax, CONTATTO.email, CONTATTO.dacr, CONTATTO.duva, CONTATTO.utuv, CONTATTO.utcr, CONTATTO.pg_ver_rec
       FROM TERZO, CONTATTO, ANAGRAFICO
       WHERE ANAGRAFICO.cd_anag =  TERZO.cd_anag AND TERZO.cd_terzo =  CONTATTO.cd_terzo;

   COMMENT ON TABLE "V_CONTATTO_ANAG"  IS 'Vista estrazione contatti per entita anagrafica';
