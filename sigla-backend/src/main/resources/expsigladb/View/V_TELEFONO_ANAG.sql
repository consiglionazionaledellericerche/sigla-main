--------------------------------------------------------
--  DDL for View V_TELEFONO_ANAG
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_TELEFONO_ANAG" ("CD_ANAG", "CD_TERZO", "PG_RIFERIMENTO", "TI_RIFERIMENTO", "DS_RIFERIMENTO", "RIFERIMENTO", "DACR", "UTCR", "DUVA", "UTUV", "PG_VER_REC") AS 
  SELECT
-- 
-- Date: 01/05/2001 
-- Version: 1.0 
--
-- Vista estrazione recapiti per entita anagrafica
--
-- History: 
-- Date: 01/05/2001 
-- Version: 1.0 
-- Creazione
-- 
-- Body:
--
ANAGRAFICO.cd_anag, TELEFONO.cd_terzo, TELEFONO.pg_riferimento, TELEFONO.ti_riferimento, TELEFONO.ds_riferimento, TELEFONO.riferimento, TELEFONO.dacr, TELEFONO.utcr, TELEFONO.duva, TELEFONO.utuv, TELEFONO.pg_ver_rec
       FROM TELEFONO, TERZO, ANAGRAFICO
       WHERE ANAGRAFICO.CD_ANAG =  TERZO.CD_ANAG AND TERZO.CD_TERZO =  TELEFONO.CD_TERZO;

   COMMENT ON TABLE "V_TELEFONO_ANAG"  IS 'Vista estrazione recapiti per entita anagrafica';
