CREATE OR REPLACE TRIGGER AI_CDR
AFTER INSERT
on CDR
for each row
declare
 aNum NUMBER;
 aCdr CDR%rowtype;
begin
--
-- Trigger definito sull'inserimento di record nella tabella CDR
--
-- Date: 15/11/2001
-- Version: 1.0
--
-- Il trigger aggiorna l'associazione tra tipologie di linee di attivita e CDR
-- per i tipi di sistema e propri
--
-- Dependency: CNRCTB010
--
-- History:
--
-- Date: 15/11/2001
-- Version: 1.0
-- Creazione
--
-- Body:
--
aCDR.cd_centro_responsabilita:=:new.cd_centro_responsabilita;
aCDR.esercizio_inizio:=:new.esercizio_inizio;
aCDR.esercizio_fine:=:new.esercizio_fine;
aCDR.cd_unita_organizzativa:=:new.cd_unita_organizzativa;
aCDR.ds_cdr:=:new.ds_cdr;
aCDR.cd_proprio_cdr:=:new.cd_proprio_cdr;
aCDR.cd_cdr_afferenza:=:new.cd_cdr_afferenza;
aCDR.livello:=:new.livello;
aCDR.utcr:=:new.utcr;
aCDR.dacr:=:new.dacr;
aCDR.utuv:=:new.utuv;
aCDR.duva:=:new.duva;
-- Aggiornamento della tabella ASS_TIPO_LA_CDR su aggiunzione di CDR
CNRCTB010.aggiornaAssTipoLaCdrOnCreaCdr(aCDR);
end;
/


