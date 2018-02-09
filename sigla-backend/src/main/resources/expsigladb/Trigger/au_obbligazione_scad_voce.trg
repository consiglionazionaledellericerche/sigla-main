CREATE OR REPLACE TRIGGER AU_OBBLIGAZIONE_SCAD_VOCE
AFTER UPDATE
on OBBLIGAZIONE_SCAD_VOCE
for each row
DISABLE
declare
 aRowtype obbligazione_scad_voce%rowtype;
 aOldRowtype obbligazione_scad_voce%rowtype;
begin
--
-- Trigger attivato su aggiornamento della tabella OBBLIGAZIONE_SCAD_VOCE (After)
--
-- Date: 18/07/2006
-- Version: 1.1
--
-- Dependency: CNRCTB035
--
-- History:
--
-- Date: 01/07/2003
-- Version: 1.0
-- Creazione
--
-- Date: 18/07/2006
-- Version: 1.2
-- Gestione Impegni/Accertamenti Residui:
-- aggiornata la funzione per tener conto anche del campo Esercizio Originale Impegno/Accertamento
--
-- Body:
--
     aRowtype.CD_CDS:=:new.CD_CDS;
     aRowtype.ESERCIZIO:=:new.ESERCIZIO;
     aRowtype.ESERCIZIO_ORIGINALE:=:new.ESERCIZIO_ORIGINALE;
     aRowtype.PG_OBBLIGAZIONE:=:new.PG_OBBLIGAZIONE;
     aRowtype.PG_OBBLIGAZIONE_SCADENZARIO:=:new.PG_OBBLIGAZIONE_SCADENZARIO;
     aRowtype.TI_APPARTENENZA:=:new.TI_APPARTENENZA;
     aRowtype.TI_GESTIONE:=:new.TI_GESTIONE;
     aRowtype.CD_VOCE:=:new.CD_VOCE;
     aRowtype.CD_CENTRO_RESPONSABILITA:=:new.CD_CENTRO_RESPONSABILITA;
     aRowtype.CD_LINEA_ATTIVITA:=:new.CD_LINEA_ATTIVITA;
     aRowtype.IM_VOCE:=:new.IM_VOCE;
     aRowtype.CD_FONDO_RICERCA:=:new.CD_FONDO_RICERCA;
     aRowtype.DACR:=:new.DACR;
     aRowtype.UTCR:=:new.UTCR;
     aRowtype.DUVA:=:new.DUVA;
     aRowtype.UTUV:=:new.UTUV;
     aRowtype.PG_VER_REC:=:new.PG_VER_REC;

     aOldRowtype.CD_CDS:=:old.CD_CDS;
     aOldRowtype.ESERCIZIO:=:old.ESERCIZIO;
     aOldRowtype.ESERCIZIO_ORIGINALE:=:old.ESERCIZIO_ORIGINALE;
     aOldRowtype.PG_OBBLIGAZIONE:=:old.PG_OBBLIGAZIONE;
     aOldRowtype.PG_OBBLIGAZIONE_SCADENZARIO:=:old.PG_OBBLIGAZIONE_SCADENZARIO;
     aOldRowtype.TI_APPARTENENZA:=:old.TI_APPARTENENZA;
     aOldRowtype.TI_GESTIONE:=:old.TI_GESTIONE;
     aOldRowtype.CD_VOCE:=:old.CD_VOCE;
     aOldRowtype.CD_CENTRO_RESPONSABILITA:=:old.CD_CENTRO_RESPONSABILITA;
     aOldRowtype.CD_LINEA_ATTIVITA:=:old.CD_LINEA_ATTIVITA;
     aOldRowtype.IM_VOCE:=:old.IM_VOCE;
     aOldRowtype.CD_FONDO_RICERCA:=:old.CD_FONDO_RICERCA;
     aOldRowtype.DACR:=:old.DACR;
     aOldRowtype.UTCR:=:old.UTCR;
     aOldRowtype.DUVA:=:old.DUVA;
     aOldRowtype.UTUV:=:old.UTUV;
     aOldRowtype.PG_VER_REC:=:old.PG_VER_REC;

-- Generazione della variazioni formali per impegni residui
-- nel caso di modifiche di importo
   if aOldRowtype.PG_OBBLIGAZIONE > 0 then
   	  CNRCTB035.CREAVARIAZIONEFORMALEIMPAU(aOldRowtype,aRowtype);
   end if;

end;
/


