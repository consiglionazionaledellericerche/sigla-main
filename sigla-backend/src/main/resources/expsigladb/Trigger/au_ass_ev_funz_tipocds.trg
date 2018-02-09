CREATE OR REPLACE TRIGGER AU_ASS_EV_FUNZ_TIPOCDS
  AFTER UPDATE
  on ASS_EV_FUNZ_TIPOCDS
  for each row
declare
 aAssoc ASS_EV_FUNZ_TIPOCDS%rowtype;
begin
--
-- Trigger attivato su aggiornamento di ASS_EV_FUNZ_TIPOCDS
--
-- Date: 13/07/2001
-- Version: 1.0
--
-- Dependency:
--
-- History:
-- Date: 13/07/2001
-- Version: 1.0
-- Creazione
--
-- Body:
--
-- Check che non cambi la PK
 if
        :old.esercizio!=:new.esercizio
     or :old.cd_conto!=:new.cd_conto
     or :old.cd_funzione!=:new.cd_funzione
     or :old.cd_tipo_unita!=:new.cd_tipo_unita
 then
  IBMERR001.raise_no_change_pk('Tabella incroci (ASS_EV_FUNZ_TIPOCDS)');
 end if;
end;
/


