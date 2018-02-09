CREATE OR REPLACE TRIGGER BU_ACCERTAMENTO
BEFORE UPDATE
on ACCERTAMENTO
for each row
declare
 aRowtype accertamento%rowtype;
 aOldRowtype accertamento%rowtype;
begin
--
-- Trigger attivato su aggiornamento della tabella ACCERTAMENTO (Before)
--
-- Date: 19/07/2006
-- Version: 1.3
--
-- Dependency: CNRSTO040, CNRCTB035
--
-- History:
--
-- Date: 06/05/2002
-- Version: 1.0
-- Creazione
--
-- Date: 05/06/2003
-- Version: 1.1
-- Introduzione delle variazioni formali
--
-- Date: 12/01/2006
-- Version: 1.2
-- Gestione Residui - Aggiunto il campo ESERCIZIO_ORIGINALE
--
-- Date: 19/07/2006
-- Version: 1.3
-- Gestione Impegni/Accertamenti Residui:
-- gestito il nuovo campo ESERCIZIO_ORIGINALE
--
-- Date: 21/04/2008
-- Version: 1.4
-- Aggiunto il campo FL_NETTO_SOSPESO
--
     aRowtype.CD_CDS:=:new.CD_CDS;
     aRowtype.ESERCIZIO:=:new.ESERCIZIO;
     aRowtype.ESERCIZIO_ORIGINALE:=:new.ESERCIZIO_ORIGINALE;
     aRowtype.PG_ACCERTAMENTO:=:new.PG_ACCERTAMENTO;
     aRowtype.CD_TIPO_DOCUMENTO_CONT:=:new.CD_TIPO_DOCUMENTO_CONT;
     aRowtype.CD_UNITA_ORGANIZZATIVA:=:new.CD_UNITA_ORGANIZZATIVA;
     aRowtype.CD_CDS_ORIGINE:=:new.CD_CDS_ORIGINE;
     aRowtype.CD_UO_ORIGINE:=:new.CD_UO_ORIGINE;
     aRowtype.TI_APPARTENENZA:=:new.TI_APPARTENENZA;
     aRowtype.TI_GESTIONE:=:new.TI_GESTIONE;
     aRowtype.CD_ELEMENTO_VOCE:=:new.CD_ELEMENTO_VOCE;
     aRowtype.CD_VOCE:=:new.CD_VOCE;
     aRowtype.DT_REGISTRAZIONE:=:new.DT_REGISTRAZIONE;
     aRowtype.DS_ACCERTAMENTO:=:new.DS_ACCERTAMENTO;
     aRowtype.NOTE_ACCERTAMENTO:=:new.NOTE_ACCERTAMENTO;
     aRowtype.CD_TERZO:=:new.CD_TERZO;
     aRowtype.IM_ACCERTAMENTO:=:new.IM_ACCERTAMENTO;
     aRowtype.DT_CANCELLAZIONE:=:new.DT_CANCELLAZIONE;
     aRowtype.CD_RIFERIMENTO_CONTRATTO:=:new.CD_RIFERIMENTO_CONTRATTO;
     aRowtype.DT_SCADENZA_CONTRATTO:=:new.DT_SCADENZA_CONTRATTO;
     aRowtype.CD_FONDO_RICERCA:=:new.CD_FONDO_RICERCA;
     aRowtype.FL_PGIRO:=:new.FL_PGIRO;
     aRowtype.RIPORTATO:=:new.RIPORTATO;
     aRowtype.DACR:=:new.DACR;
     aRowtype.UTCR:=:new.UTCR;
     aRowtype.DUVA:=:new.DUVA;
     aRowtype.UTUV:=:new.UTUV;
     aRowtype.PG_VER_REC:=:new.PG_VER_REC;
     aRowtype.CD_CDS_ORI_RIPORTO:=:new.CD_CDS_ORI_RIPORTO;
     aRowtype.ESERCIZIO_ORI_RIPORTO:=:new.ESERCIZIO_ORI_RIPORTO;
     aRowtype.ESERCIZIO_ORI_ORI_RIPORTO:=:new.ESERCIZIO_ORI_ORI_RIPORTO;
     aRowtype.PG_ACCERTAMENTO_ORI_RIPORTO:=:new.PG_ACCERTAMENTO_ORI_RIPORTO;
     aRowtype.ESERCIZIO_COMPETENZA:=:new.ESERCIZIO_COMPETENZA;
     aRowtype.FL_CALCOLO_AUTOMATICO:=:new.FL_CALCOLO_AUTOMATICO;
     aRowtype.ESERCIZIO_CONTRATTO:=:new.ESERCIZIO_CONTRATTO;
     aRowtype.STATO_CONTRATTO:=:new.STATO_CONTRATTO;
     aRowtype.PG_CONTRATTO:=:new.PG_CONTRATTO;
     aRowtype.FL_NETTO_SOSPESO:=:new.FL_NETTO_SOSPESO;

     aOldRowtype.CD_CDS:=:old.CD_CDS;
     aOldRowtype.ESERCIZIO:=:old.ESERCIZIO;
     aOldRowtype.ESERCIZIO_ORIGINALE:=:old.ESERCIZIO_ORIGINALE;
     aOldRowtype.PG_ACCERTAMENTO:=:old.PG_ACCERTAMENTO;
     aOldRowtype.CD_TIPO_DOCUMENTO_CONT:=:old.CD_TIPO_DOCUMENTO_CONT;
     aOldRowtype.CD_UNITA_ORGANIZZATIVA:=:old.CD_UNITA_ORGANIZZATIVA;
     aOldRowtype.CD_CDS_ORIGINE:=:old.CD_CDS_ORIGINE;
     aOldRowtype.CD_UO_ORIGINE:=:old.CD_UO_ORIGINE;
     aOldRowtype.TI_APPARTENENZA:=:old.TI_APPARTENENZA;
     aOldRowtype.TI_GESTIONE:=:old.TI_GESTIONE;
     aOldRowtype.CD_ELEMENTO_VOCE:=:old.CD_ELEMENTO_VOCE;
     aOldRowtype.CD_VOCE:=:old.CD_VOCE;
     aOldRowtype.DT_REGISTRAZIONE:=:old.DT_REGISTRAZIONE;
     aOldRowtype.DS_ACCERTAMENTO:=:old.DS_ACCERTAMENTO;
     aOldRowtype.NOTE_ACCERTAMENTO:=:old.NOTE_ACCERTAMENTO;
     aOldRowtype.CD_TERZO:=:old.CD_TERZO;
     aOldRowtype.IM_ACCERTAMENTO:=:old.IM_ACCERTAMENTO;
     aOldRowtype.DT_CANCELLAZIONE:=:old.DT_CANCELLAZIONE;
     aOldRowtype.CD_RIFERIMENTO_CONTRATTO:=:old.CD_RIFERIMENTO_CONTRATTO;
     aOldRowtype.DT_SCADENZA_CONTRATTO:=:old.DT_SCADENZA_CONTRATTO;
     aOldRowtype.CD_FONDO_RICERCA:=:old.CD_FONDO_RICERCA;
     aOldRowtype.FL_PGIRO:=:old.FL_PGIRO;
     aOldRowtype.RIPORTATO:=:old.RIPORTATO;
     aOldRowtype.DACR:=:old.DACR;
     aOldRowtype.UTCR:=:old.UTCR;
     aOldRowtype.DUVA:=:old.DUVA;
     aOldRowtype.UTUV:=:old.UTUV;
     aOldRowtype.PG_VER_REC:=:old.PG_VER_REC;
     aOldRowtype.CD_CDS_ORI_RIPORTO:=:old.CD_CDS_ORI_RIPORTO;
     aOldRowtype.ESERCIZIO_ORI_RIPORTO:=:old.ESERCIZIO_ORI_RIPORTO;
     aOldRowtype.ESERCIZIO_ORI_ORI_RIPORTO:=:old.ESERCIZIO_ORI_ORI_RIPORTO;
     aOldRowtype.PG_ACCERTAMENTO_ORI_RIPORTO:=:old.PG_ACCERTAMENTO_ORI_RIPORTO;
     aOldRowtype.ESERCIZIO_COMPETENZA:=:old.ESERCIZIO_COMPETENZA;
     aOldRowtype.FL_CALCOLO_AUTOMATICO:=:old.FL_CALCOLO_AUTOMATICO;
     aOldRowtype.ESERCIZIO_CONTRATTO:=:old.ESERCIZIO_CONTRATTO;
     aOldRowtype.STATO_CONTRATTO:=:old.STATO_CONTRATTO;
     aOldRowtype.PG_CONTRATTO:=:old.PG_CONTRATTO;
     aOldRowtype.FL_NETTO_SOSPESO:=:old.FL_NETTO_SOSPESO;

-- Generazione delle variazioni formali, per accertamenti residui
-- nel caso di modifica di importo e/o capitolo

     CNRCTB035.CREAVARIAZIONEFORMALEACC(aOldRowtype,aRowtype);

-- Scarico dello storico
     CNRSTO040.SCARICASUSTORICO('STOACC',aOldRowType, aRowtype);
end;
/


