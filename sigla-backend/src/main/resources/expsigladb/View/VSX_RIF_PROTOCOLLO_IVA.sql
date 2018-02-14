--------------------------------------------------------
--  DDL for View VSX_RIF_PROTOCOLLO_IVA
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VSX_RIF_PROTOCOLLO_IVA" ("PG_CALL", "PAR_NUM", "PROC_NAME", "MESSAGETOUSER", "CD_CDS", "CD_UNITA_ORGANIZZATIVA", "ESERCIZIO", "PG_FATTURA", "TIPO_DOCUMENTO_AMM", "DT_STAMPA", "UTENTE", "UTCR", "DACR", "UTUV", "DUVA", "PG_VER_REC") AS 
  select
--
-- Date: 18/06/2002
-- Version: 1.1
--
-- Vista VSX per la gestione della protocollazione delle fatture ATTIVE
--
-- History:
--
-- Date: 24/05/2002
-- Version: 1.0
-- Creazione
--
-- Date: 18/06/2002
-- Version: 1.1
-- Il campo pg_fattura è un long
--
-- Body:
--
 pg_call,
 par_num,
 proc_name,
 messageToUser,
str01, --CD_CDS
str02, --CD_UNITA_ORGANIZZATIVA
int01, --ESERCIZIO
long01, --PG_FATTURA
str03, --TIPO_DOCUMENTO_AMM
dt01, --DT_STAMPA
str04, --utente
utcr,
dacr,
utuv,
duva,
pg_ver_rec
from
 STP_CALL_EXTRA_PAR
;

   COMMENT ON TABLE "VSX_RIF_PROTOCOLLO_IVA"  IS 'Vista VSX per la gestione della protocollazione delle fatture ATTIVE';
