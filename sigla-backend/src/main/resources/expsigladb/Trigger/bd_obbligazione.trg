CREATE OR REPLACE TRIGGER BD_OBBLIGAZIONE
BEFORE DELETE
ON OBBLIGAZIONE for each row
declare
 aOldRowtype obbligazione%rowtype;
begin
--
-- Trigger attivato su cancellazione della tabella OBBLIGAZIONE (Before)
--
-- Date: 18/07/2006
-- Version: 1.3
--
-- Dependency: CNRSTO035
--
-- History:
--
-- Date: 17/09/2002
-- Version: 1.0
-- Creazione
--
-- Date: 18/09/2002
-- Version: 1.1
-- Esclusione temporanee da storico
--
-- Date: 18/09/2002
-- Version: 1.2
-- Utilizzo (pg_ver_rec +1 ) al posto di pg_ver_rec
--
-- Date: 18/07/2006
-- Version: 1.3
-- Gestione Impegni/Accertamenti Residui:
-- aggiunto il campo ESERCIZIO_ORIGINALE
--
-- Date: 21/04/2008
-- Version: 1.4
-- Aggiunto il campo FL_NETTO_SOSPESO
--
     aOldRowtype.ESERCIZIO:=:old.ESERCIZIO;
     aOldRowtype.ESERCIZIO_ORIGINALE:=:old.ESERCIZIO_ORIGINALE;
     aOldRowtype.CD_CDS:=:old.CD_CDS;
     aOldRowtype.PG_OBBLIGAZIONE:=:old.PG_OBBLIGAZIONE;
     aOldRowtype.CD_TIPO_DOCUMENTO_CONT:=:old.CD_TIPO_DOCUMENTO_CONT;
     aOldRowtype.CD_UNITA_ORGANIZZATIVA:=:old.CD_UNITA_ORGANIZZATIVA;
     aOldRowtype.CD_CDS_ORIGINE:=:old.CD_CDS_ORIGINE;
     aOldRowtype.CD_UO_ORIGINE:=:old.CD_UO_ORIGINE;
     aOldRowtype.CD_TIPO_OBBLIGAZIONE:=:old.CD_TIPO_OBBLIGAZIONE;
     aOldRowtype.TI_APPARTENENZA:=:old.TI_APPARTENENZA;
     aOldRowtype.TI_GESTIONE:=:old.TI_GESTIONE;
     aOldRowtype.CD_ELEMENTO_VOCE:=:old.CD_ELEMENTO_VOCE;
     aOldRowtype.DT_REGISTRAZIONE:=:old.DT_REGISTRAZIONE;
     aOldRowtype.DS_OBBLIGAZIONE:=:old.DS_OBBLIGAZIONE;
     aOldRowtype.NOTE_OBBLIGAZIONE:=:old.NOTE_OBBLIGAZIONE;
     aOldRowtype.CD_TERZO:=:old.CD_TERZO;
     aOldRowtype.IM_OBBLIGAZIONE:=:old.IM_OBBLIGAZIONE;
     aOldRowtype.IM_COSTI_ANTICIPATI:=:old.IM_COSTI_ANTICIPATI;
     aOldRowtype.ESERCIZIO_COMPETENZA:=:old.ESERCIZIO_COMPETENZA;
     aOldRowtype.STATO_OBBLIGAZIONE:=:old.STATO_OBBLIGAZIONE;
     aOldRowtype.DT_CANCELLAZIONE:=:old.DT_CANCELLAZIONE;
     aOldRowtype.CD_RIFERIMENTO_CONTRATTO:=:old.CD_RIFERIMENTO_CONTRATTO;
     aOldRowtype.DT_SCADENZA_CONTRATTO:=:old.DT_SCADENZA_CONTRATTO;
     aOldRowtype.FL_CALCOLO_AUTOMATICO:=:old.FL_CALCOLO_AUTOMATICO;
     aOldRowtype.CD_FONDO_RICERCA:=:old.CD_FONDO_RICERCA;
     aOldRowtype.FL_SPESE_COSTI_ALTRUI:=:old.FL_SPESE_COSTI_ALTRUI;
     aOldRowtype.FL_PGIRO:=:old.FL_PGIRO;
     aOldRowtype.DACR:=:old.DACR;
     aOldRowtype.UTCR:=:old.UTCR;
     aOldRowtype.DUVA:=:old.DUVA;
     aOldRowtype.UTUV:=:old.UTUV;
     aOldRowtype.PG_VER_REC:=:old.PG_VER_REC + 1;
     aOldRowtype.RIPORTATO:=:old.RIPORTATO;
     aOldRowtype.CD_CDS_ORI_RIPORTO:=:old.CD_CDS_ORI_RIPORTO;
     aOldRowtype.ESERCIZIO_ORI_RIPORTO:=:old.ESERCIZIO_ORI_RIPORTO;
     aOldRowtype.ESERCIZIO_ORI_ORI_RIPORTO:=:old.ESERCIZIO_ORI_ORI_RIPORTO;
     aOldRowtype.PG_OBBLIGAZIONE_ORI_RIPORTO:=:old.PG_OBBLIGAZIONE_ORI_RIPORTO;
     aOldRowtype.ESERCIZIO_CONTRATTO:=:old.ESERCIZIO_CONTRATTO;
     aOldRowtype.STATO_CONTRATTO:=:old.STATO_CONTRATTO;
     aOldRowtype.PG_CONTRATTO:=:old.PG_CONTRATTO;
     aOldRowtype.ESERCIZIO_REP:=:old.ESERCIZIO_REP;
     aOldRowtype.PG_REPERTORIO:=:old.PG_REPERTORIO;
     aOldRowtype.MOTIVAZIONE:=:old.MOTIVAZIONE;
     aOldRowtype.FL_NETTO_SOSPESO:=:old.FL_NETTO_SOSPESO;
     aOldRowtype.FL_GARA_IN_CORSO:=:old.FL_GARA_IN_CORSO;
     aOldRowtype.FL_DETERMINA_ALLEGATA:=:old.FL_DETERMINA_ALLEGATA;
     aOldRowtype.DT_DETERMINA_ALLEGATA:=:old.DT_DETERMINA_ALLEGATA;
     aOldRowtype.DS_GARA_IN_CORSO:=:old.DS_GARA_IN_CORSO;

-- Scarico dello storico
   if :old.pg_obbligazione >= 0 then
     CNRSTO035.SCARICASUSTORICO('STOOBB',aOldRowType, aOldRowType);
   end if;

-- Cancella OBBLIGAZIONE_SCAD_VOCE
   delete from obbligazione_scad_voce
   where
   esercizio = aOldRowType.esercizio
   and cd_cds = aOldRowType.cd_cds
   and esercizio_originale = aOldRowType.esercizio_originale
   and pg_obbligazione = aOldRowType.pg_obbligazione;

-- Cancella OBBLIGAZIONE_SCADENZARIO
   delete from obbligazione_scadenzario
   where
   esercizio = aOldRowType.esercizio
   and cd_cds = aOldRowType.cd_cds
   and esercizio_originale = aOldRowType.esercizio_originale
   and pg_obbligazione = aOldRowType.pg_obbligazione;

end;
/


