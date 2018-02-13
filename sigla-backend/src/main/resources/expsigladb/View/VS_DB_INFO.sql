--------------------------------------------------------
--  DDL for View VS_DB_INFO
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VS_DB_INFO" ("ISTANZA", "UTENTE") AS 
  select distinct
--
-- Vista di estrazione di NOME ISTANZA e UTENTE DB
-- Questa vista non deve essere rilasciata all'utente finale
--
-- Version: 1.0
-- Date: 09/10/2001
--
-- History:
--
-- Body:
--
 b.global_name,
 a.username
from global_name b, user_users a;

   COMMENT ON TABLE "VS_DB_INFO"  IS 'Vista di estrazione di NOME ISTANZA e UTENTE DB
Questa vista non deve essere rilasciata all''utente finale';
