--------------------------------------------------------
--  DDL for View VSX_LIQUIDAZIONE_CORI
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VSX_LIQUIDAZIONE_CORI" ("PG_CALL", "PAR_NUM", "PROC_NAME", "MESSAGETOUSER", "CD_CDS", "ESERCIZIO", "CD_UNITA_ORGANIZZATIVA", "CD_CDS_ORIGINE", "CD_UO_ORIGINE", "PG_LIQUIDAZIONE", "PG_LIQUIDAZIONE_ORIGINE", "CD_GRUPPO_CR", "CD_REGIONE", "PG_COMUNE", "UTCR", "DACR", "UTUV", "DUVA", "PG_VER_REC") AS 
  select
--
-- Date: 23/07/2002
-- Version: 1.7
--
-- Vista VSX per per gestione passaggio parametri a stored procedure per liquidazione cori
--
-- History:
--
-- Date: 28/05/2002
-- Version: 1.0
-- Creazione
--
-- Date: 03/06/2002
-- Version: 1.2
-- Introduzione dt_liquidazione
--
-- Date: 06/06/2002
-- Version: 1.3
-- Ristrutturazione della liquidazione
--
-- Date: 17/06/2002
-- Version: 1.4
-- Fix errore
--
-- Date: 24/06/2002
-- Version: 1.5
-- Ristrutturazione liquidazione per gestione pagamento accentrato
--
-- Date: 25/06/2002
-- Version: 1.6
-- aggiunto pg_liquidazione_origine
--
-- Date: 23/07/2002
-- Version: 1.7
-- Aggiunto il pg_comune, e cd_regione
--
-- Body:
--
 pg_call,
 par_num,
 proc_name,
 messageToUser,
 str01,
 int01,
 str02,
 str03,
 str04,
 int02,
 int03,
 str05,
 str06,
 long01,
 utcr,
 dacr,
 utuv,
 duva,
 pg_ver_rec
from
 STP_CALL_EXTRA_PAR
;

   COMMENT ON TABLE "VSX_LIQUIDAZIONE_CORI"  IS 'Vista VSX per per gestione passaggio parametri a stored procedure per liquidazione cori';
