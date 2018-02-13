--------------------------------------------------------
--  DDL for View V_RAPP_DIP
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_RAPP_DIP" ("CD_ANAG", "MATRICOLA_DIPENDENTE") AS 
  select distinct cd_anag, matricola_dipendente
from rapporto
where cd_tipo_rapporto = 'DIP';
