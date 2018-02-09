CREATE OR REPLACE TRIGGER AI_VOCE_F
  AFTER INSERT
  on VOCE_F
  for each row
declare
 aVoceF voce_f%rowtype;
begin
--
-- Trigger attivato su inserimento di VOCE_F
--
-- Date: 16/01/2002
-- Version: 1.0
--
-- Dependency: CNRCTB055
--
-- History:
-- Date: 16/01/2002
-- Version: 1.0
-- Creazione
--
-- Body:
--
     aVoceF.ESERCIZIO:=:new.ESERCIZIO;
     aVoceF.TI_APPARTENENZA:=:new.TI_APPARTENENZA;
     aVoceF.TI_GESTIONE:=:new.TI_GESTIONE;
     aVoceF.CD_VOCE:=:new.CD_VOCE;
     aVoceF.CD_UNITA_ORGANIZZATIVA:=:new.CD_UNITA_ORGANIZZATIVA;
     aVoceF.TI_VOCE:=:new.TI_VOCE;
     aVoceF.CD_PROPRIO_VOCE:=:new.CD_PROPRIO_VOCE;
     aVoceF.DS_VOCE:=:new.DS_VOCE;
     aVoceF.CD_PARTE:=:new.CD_PARTE;
     aVoceF.CD_CATEGORIA:=:new.CD_CATEGORIA;
     aVoceF.CD_NATURA:=:new.CD_NATURA;
     aVoceF.CD_FUNZIONE:=:new.CD_FUNZIONE;
     aVoceF.FL_MASTRINO:=:new.FL_MASTRINO;
     aVoceF.LIVELLO:=:new.LIVELLO;
     aVoceF.CD_TITOLO_CAPITOLO:=:new.CD_TITOLO_CAPITOLO;
     aVoceF.CD_SEZIONE_CAPITOLO:=:new.CD_SEZIONE_CAPITOLO;
     aVoceF.CD_CDS:=:new.CD_CDS;
     aVoceF.CD_CENTRO_RESPONSABILITA:=:new.CD_CENTRO_RESPONSABILITA;
     aVoceF.DUVA:=:new.DUVA;
     aVoceF.UTUV:=:new.UTUV;
     aVoceF.DACR:=:new.DACR;
     aVoceF.UTCR:=:new.UTCR;
     aVoceF.PG_VER_REC:=:new.PG_VER_REC;
     aVoceF.CD_VOCE_PADRE:=:new.CD_VOCE_PADRE;

-- Aggiornamento della tabella VOCI_F_SALDI_CMP su inserimento di VOCE_F
 CNRCTB055.CREAESPLSALDI(aVoceF);
end;
/


