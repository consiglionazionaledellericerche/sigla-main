CREATE OR REPLACE TRIGGER AD_ASS_EV_FUNZ_TIPOCDS
  AFTER DELETE
  on ASS_EV_FUNZ_TIPOCDS
  for each row
declare
aAss ASS_EV_FUNZ_TIPOCDS%rowtype;
begin
--
-- Trigger attivato su eliminazione di record da ASS_EV_FUNZ_TIPOCDS
--
-- Date: 13/07/2001
-- Version: 1.0
--
-- Dependency: CNRPKG001
--
-- History:
-- Date: 13/07/2001
-- Version: 1.0
-- Creazione
--
-- Body:

 aAss.esercizio:=:old.esercizio;
 aAss.cd_conto:=:old.cd_conto;
 aAss.cd_tipo_unita:=:old.cd_tipo_unita;
 aAss.cd_funzione:=:old.cd_funzione;
 aAss.utcr:=:old.utcr;
 aAss.dacr:=:old.dacr;
 aAss.utuv:=:old.utuv;
 aAss.duva:=:old.duva;
 CNRCTB001.eliminaEsplIncroci(aAss);
end;
/


