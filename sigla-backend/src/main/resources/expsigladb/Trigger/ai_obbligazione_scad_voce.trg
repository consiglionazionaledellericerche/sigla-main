CREATE OR REPLACE TRIGGER AI_OBBLIGAZIONE_SCAD_VOCE
AFTER INSERT
on OBBLIGAZIONE_SCAD_VOCE
for each row
DISABLE
declare
 aRowtype obbligazione_scad_voce%rowtype;
begin
--
-- Trigger attivato su inserimento nella tabella OBBLIGAZIONE_SCAD_VOCE (After)
--
-- Date: 18/07/2006
-- Version: 1.2
--
-- Dependency: CNRCTB035
--
-- History:
--
-- Date: 23/06/2003
-- Version: 1.0
-- Creazione
--
-- Date: 27/06/2003
-- Version: 1.1
-- Filtrati pg negativi documenti temporanei
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

-- Generazione della variazioni formali per impegni residui
-- nel caso di modifiche di capitolo
   if aRowtype.PG_OBBLIGAZIONE > 0 then
   	 CNRCTB035.CREAVARIAZIONEFORMALEIMPAI(aRowtype);
   end if;

end;
/


