CREATE OR REPLACE TRIGGER BU_OBBLIGAZIONE
BEFORE UPDATE
on OBBLIGAZIONE
for each row
declare
 aRowtype obbligazione%rowtype;
 aOldRowtype obbligazione%rowtype;
begin
--
-- Trigger attivato su aggiornamento della tabella OBBLIGAZIONE (Before)
--
-- Date: 18/07/2006
-- Version: 1.3
--
-- Dependency: CNRSTO035
--
-- History:
--
-- Date: 23/04/2002
-- Version: 1.0
-- Creazione
--
-- Date: 24/04/2002
-- Version: 1.1
-- Modifica interfaccia metodo di scarico su storico
--
-- Date: 17/09/2002
-- Version: 1.2
-- Lo scarico sullo storico viene fatto anche per obbligazioni provvisorio per nuova gestione saldi
--
-- Date: 18/07/2006
-- Version: 1.3
-- Gestione Impegni/Accertamenti Residui:
-- aggiunto il campo ESERCIZIO_ORIGINALE
--
-- Date: 21/04/2008
-- Version: 1.4
-- Aggiunto il campo FL_NETTO_SOSPESO

     aRowtype.ESERCIZIO:=:new.ESERCIZIO;
     aRowtype.CD_CDS:=:new.CD_CDS;
     aRowtype.ESERCIZIO_ORIGINALE:=:new.ESERCIZIO_ORIGINALE;
     aRowtype.PG_OBBLIGAZIONE:=:new.PG_OBBLIGAZIONE;
     aRowtype.CD_TIPO_DOCUMENTO_CONT:=:new.CD_TIPO_DOCUMENTO_CONT;
     aRowtype.CD_UNITA_ORGANIZZATIVA:=:new.CD_UNITA_ORGANIZZATIVA;
     aRowtype.CD_CDS_ORIGINE:=:new.CD_CDS_ORIGINE;
     aRowtype.CD_UO_ORIGINE:=:new.CD_UO_ORIGINE;
     aRowtype.CD_TIPO_OBBLIGAZIONE:=:new.CD_TIPO_OBBLIGAZIONE;
     aRowtype.TI_APPARTENENZA:=:new.TI_APPARTENENZA;
     aRowtype.TI_GESTIONE:=:new.TI_GESTIONE;
     aRowtype.CD_ELEMENTO_VOCE:=:new.CD_ELEMENTO_VOCE;
     aRowtype.DT_REGISTRAZIONE:=:new.DT_REGISTRAZIONE;
     aRowtype.DS_OBBLIGAZIONE:=:new.DS_OBBLIGAZIONE;
     aRowtype.NOTE_OBBLIGAZIONE:=:new.NOTE_OBBLIGAZIONE;
     aRowtype.CD_TERZO:=:new.CD_TERZO;
     aRowtype.IM_OBBLIGAZIONE:=:new.IM_OBBLIGAZIONE;
     aRowtype.IM_COSTI_ANTICIPATI:=:new.IM_COSTI_ANTICIPATI;
     aRowtype.ESERCIZIO_COMPETENZA:=:new.ESERCIZIO_COMPETENZA;
     aRowtype.STATO_OBBLIGAZIONE:=:new.STATO_OBBLIGAZIONE;
     aRowtype.DT_CANCELLAZIONE:=:new.DT_CANCELLAZIONE;
     aRowtype.CD_RIFERIMENTO_CONTRATTO:=:new.CD_RIFERIMENTO_CONTRATTO;
     aRowtype.DT_SCADENZA_CONTRATTO:=:new.DT_SCADENZA_CONTRATTO;
     aRowtype.FL_CALCOLO_AUTOMATICO:=:new.FL_CALCOLO_AUTOMATICO;
     aRowtype.CD_FONDO_RICERCA:=:new.CD_FONDO_RICERCA;
     aRowtype.FL_SPESE_COSTI_ALTRUI:=:new.FL_SPESE_COSTI_ALTRUI;
     aRowtype.FL_PGIRO:=:new.FL_PGIRO;
     aRowtype.DACR:=:new.DACR;
     aRowtype.UTCR:=:new.UTCR;
     aRowtype.DUVA:=:new.DUVA;
     aRowtype.UTUV:=:new.UTUV;
     aRowtype.PG_VER_REC:=:new.PG_VER_REC;
     aRowtype.RIPORTATO:=:new.RIPORTATO;
     aRowtype.CD_CDS_ORI_RIPORTO:=:new.CD_CDS_ORI_RIPORTO;
     aRowtype.ESERCIZIO_ORI_RIPORTO:=:new.ESERCIZIO_ORI_RIPORTO;
     aRowtype.ESERCIZIO_ORI_ORI_RIPORTO:=:new.ESERCIZIO_ORI_ORI_RIPORTO;
     aRowtype.PG_OBBLIGAZIONE_ORI_RIPORTO:=:new.PG_OBBLIGAZIONE_ORI_RIPORTO;
     aRowtype.ESERCIZIO_CONTRATTO:=:new.ESERCIZIO_CONTRATTO;
     aRowtype.STATO_CONTRATTO:=:new.STATO_CONTRATTO;
     aRowtype.PG_CONTRATTO:=:new.PG_CONTRATTO;
     aRowtype.ESERCIZIO_REP:=:New.ESERCIZIO_REP;
     aRowtype.PG_REPERTORIO:=:New.PG_REPERTORIO;
     aRowtype.MOTIVAZIONE:=:new.MOTIVAZIONE;
     aRowtype.FL_NETTO_SOSPESO:=:new.FL_NETTO_SOSPESO;
     aRowtype.FL_GARA_IN_CORSO:=:new.FL_GARA_IN_CORSO;
     aRowtype.DS_GARA_IN_CORSO:=:new.DS_GARA_IN_CORSO;
     aRowtype.FL_DETERMINA_ALLEGATA:=:new.FL_DETERMINA_ALLEGATA;


     aOldRowtype.ESERCIZIO:=:old.ESERCIZIO;
     aOldRowtype.CD_CDS:=:old.CD_CDS;
     aOldRowtype.ESERCIZIO_ORIGINALE:=:old.ESERCIZIO_ORIGINALE;
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
     aOldRowtype.PG_VER_REC:=:old.PG_VER_REC;
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
     aOldRowtype.DS_GARA_IN_CORSO:=:old.DS_GARA_IN_CORSO;
     aOldRowtype.FL_DETERMINA_ALLEGATA:=:old.FL_DETERMINA_ALLEGATA;

-- Scarico dello storico
  CNRSTO035.SCARICASUSTORICO('STOOBB',aOldRowType, aRowtype);
end;
/


