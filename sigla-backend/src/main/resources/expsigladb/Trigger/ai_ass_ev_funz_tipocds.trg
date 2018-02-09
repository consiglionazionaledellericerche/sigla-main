CREATE OR REPLACE TRIGGER AI_ASS_EV_FUNZ_TIPOCDS
  AFTER INSERT
  on ASS_EV_FUNZ_TIPOCDS
  for each row
declare
aAss ASS_EV_FUNZ_TIPOCDS%rowtype;
begin
--
-- Trigger attivato su inserimento di record in ASS_EV_FUNZ_TIPOCDS
--
-- Date: 13/07/2001
-- Version: 1.0
--
-- Dependency: CNRCTB001
--
-- History:
-- Date: 13/07/2001
-- Version: 1.0
-- Creazione
--
-- Body:
--
aAss.esercizio:=:new.esercizio;
 aAss.cd_conto:=:new.cd_conto;
 aAss.cd_tipo_unita:=:new.cd_tipo_unita;
 aAss.cd_funzione:=:new.cd_funzione;
 aAss.utcr:=:new.utcr;
 aAss.dacr:=:new.dacr;
 aAss.utuv:=:new.utuv;
 aAss.duva:=:new.duva;
-- Aggiornamento della tabella VOCE_F su inserimento di CDR
 CNRCTB001.creaEsplIncroci(aAss);
end;
/


