CREATE OR REPLACE TRIGGER BD_VOCE_F
  BEFORE DELETE
  on VOCE_F
  for each row
declare
 aVoceF voce_f%rowtype;
begin
--
-- Trigger attivato su eliminazione di VOCE_F
--
-- Date: 18/01/2002
-- Version: 1.0
--
-- Dependency: CNRCTB055
--
-- History:
-- Date: 18/01/2002
-- Version: 1.0
-- Creazione
--
-- Body:
--
     aVoceF.ESERCIZIO:=:old.ESERCIZIO;
     aVoceF.TI_APPARTENENZA:=:old.TI_APPARTENENZA;
     aVoceF.TI_GESTIONE:=:old.TI_GESTIONE;
     aVoceF.CD_VOCE:=:old.CD_VOCE;
     aVoceF.CD_UNITA_ORGANIZZATIVA:=:old.CD_UNITA_ORGANIZZATIVA;
     aVoceF.TI_VOCE:=:old.TI_VOCE;
     aVoceF.CD_PROPRIO_VOCE:=:old.CD_PROPRIO_VOCE;
     aVoceF.DS_VOCE:=:old.DS_VOCE;
     aVoceF.CD_PARTE:=:old.CD_PARTE;
     aVoceF.CD_CATEGORIA:=:old.CD_CATEGORIA;
     aVoceF.CD_NATURA:=:old.CD_NATURA;
     aVoceF.CD_FUNZIONE:=:old.CD_FUNZIONE;
     aVoceF.FL_MASTRINO:=:old.FL_MASTRINO;
     aVoceF.LIVELLO:=:old.LIVELLO;
     aVoceF.CD_TITOLO_CAPITOLO:=:old.CD_TITOLO_CAPITOLO;
     aVoceF.CD_SEZIONE_CAPITOLO:=:old.CD_SEZIONE_CAPITOLO;
     aVoceF.CD_CDS:=:old.CD_CDS;
     aVoceF.CD_CENTRO_RESPONSABILITA:=:old.CD_CENTRO_RESPONSABILITA;
     aVoceF.DUVA:=:old.DUVA;
     aVoceF.UTUV:=:old.UTUV;
     aVoceF.DACR:=:old.DACR;
     aVoceF.UTCR:=:old.UTCR;
     aVoceF.PG_VER_REC:=:old.PG_VER_REC;
     aVoceF.CD_VOCE_PADRE:=:old.CD_VOCE_PADRE;

-- Aggiornamento della tabella VOCI_F_SALDI_CMP su eliminazione di VOCE_F
 CNRCTB055.ELIMINAESPLSALDI(aVoceF);
end;
/


