CREATE OR REPLACE TRIGGER ad_elemento_voce
AFTER DELETE   ON ELEMENTO_VOCE REFERENCING NEW AS NEW OLD AS OLD
FOR EACH ROW
declare
 aEV elemento_voce%rowtype;
 recParametriCNR PARAMETRI_CNR%Rowtype;
begin
 recParametriCNR := CNRUTL001.getRecParametriCnr(:old.esercizio);
 If recParametriCNR.fl_nuovo_pdg='Y' Then
   return;
 End If;
--
-- Trigger attivato su eliminazione di ELEMENTO_VOCE
--
-- Date: 19/11/2001
-- Version: 1.1
--
-- Dependency: CNRCTB001
--
-- History:
-- Date: 13/07/2001
-- Version: 1.0
-- Creazione
--
-- Date: 19/11/2001
-- Version: 1.1
-- Aggiunzione campi a elemento_voce
--
-- Body:
--
 aEV.esercizio:=:old.esercizio;
 aEV.cd_elemento_padre:=:old.cd_elemento_padre;
 aEV.cd_proprio_elemento:=:old.cd_proprio_elemento;
 aEV.cd_elemento_voce:=:old.cd_elemento_voce;
 aEV.ds_elemento_voce:=:old.ds_elemento_voce;
 aEV.ti_elemento_voce:=:old.ti_elemento_voce;
 aEV.ti_gestione:=:old.ti_gestione;
 aEV.ti_appartenenza:=:old.ti_appartenenza;
 aEV.FL_LIMITE_ASS_OBBLIG:= :old.FL_LIMITE_ASS_OBBLIG;
 aEV.FL_VOCE_PERSONALE:=:old.FL_VOCE_PERSONALE;
 aEV.FL_PARTITA_GIRO:=:old.FL_PARTITA_GIRO;
 aEV.CD_CAPOCONTO_FIN:=:old.CD_CAPOCONTO_FIN;
 aEV.cd_parte:=:old.cd_parte;
 aEV.utcr:=:old.utcr;
 aEV.dacr:=:old.dacr;
 aEV.duva:=:old.duva;
 aEV.utuv:=:old.utuv;
-- Aggiornamento della tabella VOCI_F su eliminazione di elemento voce
 CNRCTB001.eliminaEsplVoci(aEV);
end;
/


