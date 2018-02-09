CREATE OR REPLACE TRIGGER BD_ACCERTAMENTO
BEFORE DELETE
on ACCERTAMENTO
for each row
declare
 aOldRowtype accertamento%rowtype;
begin
--
-- Trigger attivato su cancellazione della tabella ACCERTAMENTO (Before)
--
-- Date: 19/07/2006
-- Version: 1.4
--
-- Dependency: CNRSTO040
--
-- History:
--
-- Date: 17/09/2002
-- Version: 1.0
-- Creazione
--
-- Date: 18/09/2002
-- Version: 1.1
-- Esclusione temporanei da storico
--
-- Date: 18/09/2002
-- Version: 1.2
-- Utilizzo (pg_ver_rec +1 ) al posto di pg_ver_rec
--
-- Date: 12/01/2006
-- Version: 1.3
-- Gestione Residui - Aggiunto il campo ESERCIZIO_ORIGINALE
--
-- Date: 19/07/2006
-- Version: 1.4
-- Gestione Impegni/Accertamenti Residui:
-- gestito il nuovo campo ESERCIZIO_ORIGINALE
--
-- Date: 21/04/2008
-- Version: 1.4
-- Aggiunto il campo FL_NETTO_SOSPESO
--
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
     aOldRowtype.PG_VER_REC:=:old.PG_VER_REC + 1;
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

-- Scarico dello storico
     if :old.pg_accertamento >= 0 then
      CNRSTO040.SCARICASUSTORICO('STOACC',aOldRowType, aOldRowType);
	 end if;

-- Cancella ACCERTAMENTO_SCAD_VOCE
   delete from accertamento_scad_voce
   where
   cd_cds = aOldRowType.cd_cds
   and esercizio = aOldRowType.esercizio
   and esercizio_originale = aOldRowType.esercizio_originale
   and pg_accertamento = aOldRowType.pg_accertamento;

-- Cancella ACCERTAMENTO_SCADENZARIO
   delete from accertamento_scadenzario
   where
   cd_cds = aOldRowType.cd_cds
   and esercizio = aOldRowType.esercizio
   and esercizio_originale = aOldRowType.esercizio_originale
   and pg_accertamento = aOldRowType.pg_accertamento;

end;
/


