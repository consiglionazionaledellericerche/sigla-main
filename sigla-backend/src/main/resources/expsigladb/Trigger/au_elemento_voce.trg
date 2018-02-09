CREATE OR REPLACE TRIGGER au_elemento_voce
AFTER UPDATE OF  ESERCIZIO,
                   CD_ELEMENTO_VOCE,
                   TI_GESTIONE,
                   TI_APPARTENENZA,
                   CD_ELEMENTO_PADRE,
                   CD_PROPRIO_ELEMENTO,
                   TI_ELEMENTO_VOCE,
                   CD_PARTE,
                   DS_ELEMENTO_VOCE
  ON ELEMENTO_VOCE REFERENCING NEW AS NEW OLD AS OLD
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
-- Trigger attivato su aggiornamento di elemento voce
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
-- Check che non cambi la PK
 If  :old.esercizio != :new.esercizio
  or :old.cd_elemento_voce != :new.cd_elemento_voce
  or :old.ti_gestione != :new.ti_gestione
  or :old.ti_appartenenza != :new.ti_appartenenza Then
        IBMERR001.raise_no_change_pk('Elemento Voce');
 end if;

-- Check che non cambino altri dati (solo quelli di sistema e descrizione)
 If  :old.cd_elemento_padre != :new.cd_elemento_padre
  or :old.cd_proprio_elemento != :new.cd_proprio_elemento
  or :old.ti_elemento_voce != :new.ti_elemento_voce
  or :old.cd_parte != :new.cd_parte  then
  IBMERR001.raise_no_change('Solo la descrizione puo essere cambiata in Elemento Voce');
 end if;

 aEV.esercizio                  :=      :new.esercizio;
 aEV.cd_elemento_padre          :=      :new.cd_elemento_padre;
 aEV.cd_proprio_elemento        :=      :new.cd_proprio_elemento;
 aEV.cd_elemento_voce           :=      :new.cd_elemento_voce;
 aEV.ds_elemento_voce           :=      :new.ds_elemento_voce;
 aEV.ti_elemento_voce           :=      :new.ti_elemento_voce;
 aEV.ti_gestione                :=      :new.ti_gestione;
 aEV.ti_appartenenza            :=      :new.ti_appartenenza;
 aEV.cd_parte                   :=      :new.cd_parte;
 aEV.FL_LIMITE_ASS_OBBLIG       :=      :new.FL_LIMITE_ASS_OBBLIG;
 aEV.FL_VOCE_PERSONALE          :=      :new.FL_VOCE_PERSONALE;
 aEV.FL_PARTITA_GIRO            :=      :new.FL_PARTITA_GIRO;
 aEV.CD_CAPOCONTO_FIN           :=      :new.CD_CAPOCONTO_FIN;
 aEV.utcr                       :=      :new.utcr;
 aEV.dacr                       :=      :new.dacr;
 aEV.duva                       :=      :new.duva;
 aEV.utuv                       :=      :new.utuv;
-- Trigger di aggiornamento della tabella VOCI_F su aggiornamento di elemento voce
 CNRCTB001.creaEsplVoci(aEV);
end;
/


