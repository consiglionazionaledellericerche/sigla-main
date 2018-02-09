CREATE OR REPLACE TRIGGER AD_ASS_EV_EV
  AFTER DELETE
  on ASS_EV_EV
  for each row
declare
aAss ASS_EV_EV%rowtype;
begin
--
-- Trigger scatenato da eliminazione da tabella ass_ev_ev
--
-- Date: 31/12/2001
-- Version: 1.0
--
-- Dependency: CNRPKG000
--
-- History:
-- Date: 31/12/2001
-- Version: 1.0
-- Creazione
--
-- Body:
 aAss.esercizio:=:old.esercizio;
 aAss.ti_gestione:=:old.ti_gestione;
 aAss.ti_appartenenza:=:old.ti_appartenenza;
 aAss.ti_elemento_voce:=:old.ti_elemento_voce;
 aAss.cd_elemento_voce:=:old.cd_elemento_voce;
 aAss.ti_gestione_coll:=:old.ti_gestione_coll;
 aAss.ti_appartenenza_coll:=:old.ti_appartenenza_coll;
 aAss.ti_elemento_voce_coll:=:old.ti_elemento_voce_coll;
 aAss.cd_elemento_voce_coll:=:old.cd_elemento_voce_coll;
 aAss.cd_natura:=:old.cd_natura;
 aAss.cd_cds:=:old.cd_cds;
 aAss.utcr:=:old.utcr;
 aAss.dacr:=:old.dacr;
 aAss.utuv:=:old.utuv;
 aAss.duva:=:old.duva;
 CNRCTB000.CHECKELIMINASSEVEV(aAss);
end;
/


