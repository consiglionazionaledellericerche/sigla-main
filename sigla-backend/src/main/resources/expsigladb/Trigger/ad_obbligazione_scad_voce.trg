CREATE OR REPLACE TRIGGER AD_OBBLIGAZIONE_SCAD_VOCE
AFTER DELETE
on OBBLIGAZIONE_SCAD_VOCE
for each row
DISABLE
declare
 aOldRowtype obbligazione_scad_voce%rowtype;
begin
--
-- Trigger attivato su cancellazione della tabella OBBLIGAZIONE_SCAD_VOCE (After)
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
-- Version: 1.1
-- Gestione Impegni/Accertamenti Residui:
-- aggiornata la funzione per tener conto anche del campo Esercizio Originale Impegno/Accertamento
--
-- Body:
--
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
-- nel caso di modifiche di capitolo
   if aOldRowtype.PG_OBBLIGAZIONE > 0 then
   	 CNRCTB035.CREAVARIAZIONEFORMALEIMPAD(aOldRowtype);
   end if;

end;
/


