--------------------------------------------------------
--  DDL for View V_UTENTE_LDAP
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_UTENTE_LDAP" ("ID_UTENTE") AS 
  select
Distinct Lower(cd_utente_uid)
from utente
where cd_utente_uid is not Null
and fl_autenticazione_ldap='Y'
;
