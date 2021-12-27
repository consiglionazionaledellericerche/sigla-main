--------------------------------------------------------
--  DDL for View V_BANCA_ANAG
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_BANCA_ANAG" ("CD_ANAG", "CD_TERZO", "PG_BANCA", "TI_PAGAMENTO", "ABI", "CAB", "DS_ABICAB", "NUMERO_CONTO", "INTESTAZIONE", "QUIETANZA", "CODICE_IBAN", "CODICE_SWIFT", "FL_CANCELLATO", "CD_TERZO_DELEGATO", "PG_BANCA_DELEGATO", "ORIGINE", "FL_CC_CDS", "CIN", "TIPO_POSTALIZZAZIONE", "TIPO_PAGAMENTO_SIOPE", "DACR", "UTCR", "DUVA", "UTUV", "PG_VER_REC") AS
  SELECT
--
-- Date: 23/04/2002
-- Version: 1.1
--
-- Vista estrazione informazioni bancarie per entita anagrafica
--
-- History:
--
-- Date: 01/05/2001
-- Version: 1.0
-- Creazione
--
-- Date: 23/04/2002
-- Version: 1.1
-- Introduzione campo fl_cancellato su BANCA
--
-- Body:
--
       T.cd_anag,
       T.cd_terzo,
       B.pg_banca,
       B.ti_pagamento,
       B.abi,
       B.cab,
       A.ds_abicab,
       B.numero_conto,
       B.intestazione,
       B.quietanza,
       B.codice_iban,
       B.codice_swift,
       B.fl_cancellato,
       B.cd_terzo_delegato,
       B.pg_banca_delegato,
       B.origine,
       B.fl_cc_cds,
       B.cin,
       B.tipo_postalizzazione,
       B.tipo_pagamento_siope,
       B.dacr,
       B.utcr,
       B.duva,
       B.utuv,
       B.pg_ver_rec
FROM   TERZO T,
       BANCA B,
       ABICAB A
WHERE  B.cd_terzo = T.cd_terzo AND
       A.abi (+) = B.abi AND
       A.cab (+) = B.cab
;
