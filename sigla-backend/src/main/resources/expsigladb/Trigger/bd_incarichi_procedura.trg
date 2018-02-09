CREATE OR REPLACE TRIGGER BD_INCARICHI_PROCEDURA
BEFORE Delete
On INCARICHI_PROCEDURA
For each row
WHEN (
old.stato != 'PP'
      )
Declare
   aOldRowtype incarichi_procedura%rowtype;
Begin
   --
   -- Trigger attivato su cancellazione della tabella INCARICHI_PROCEDURA (Before)
   --
   -- Date: 28/10/2008
   -- Version: 1.0
   --
   -- Dependency: CNRSTO070
   --
   -- History:
   --
   -- Date: 28/10/2008
   -- Version: 1.0
   -- Creazione
   --
   aOldRowtype.ESERCIZIO                := :Old.ESERCIZIO;
   aOldRowtype.PG_PROCEDURA             := :Old.PG_PROCEDURA;
   aOldRowtype.CD_CDS                   := :Old.CD_CDS;
   aOldRowtype.CD_UNITA_ORGANIZZATIVA   := :Old.CD_UNITA_ORGANIZZATIVA;
   aOldRowtype.STATO                    := :Old.STATO;
   aOldRowtype.DT_REGISTRAZIONE         := :Old.DT_REGISTRAZIONE;
   aOldRowtype.DT_CANCELLAZIONE         := :Old.DT_CANCELLAZIONE;
   aOldRowtype.DT_RESPINTA              := :Old.DT_RESPINTA;
   aOldRowtype.ESERCIZIO_RICHIESTA      := :Old.ESERCIZIO_RICHIESTA;
   aOldRowtype.PG_RICHIESTA             := :Old.PG_RICHIESTA;
   aOldRowtype.CD_FIRMATARIO            := :Old.CD_FIRMATARIO;
   aOldRowtype.CD_TERZO_RESP            := :Old.CD_TERZO_RESP;
   aOldRowtype.CD_PROC_AMM              := :Old.CD_PROC_AMM;
   aOldRowtype.CD_TIPO_ATTO             := :Old.CD_TIPO_ATTO;
   aOldRowtype.DS_ATTO                  := :Old.DS_ATTO;
   aOldRowtype.CD_PROTOCOLLO            := :Old.CD_PROTOCOLLO;
   aOldRowtype.OGGETTO                  := :Old.OGGETTO;
   aOldRowtype.PG_COMUNE                := :Old.PG_COMUNE;
   aOldRowtype.CD_TIPO_INCARICO         := :Old.CD_TIPO_INCARICO;
   aOldRowtype.CD_TIPO_ATTIVITA         := :Old.CD_TIPO_ATTIVITA;
   aOldRowtype.TIPO_NATURA              := :Old.TIPO_NATURA;
   aOldRowtype.FL_MERAMENTE_OCCASIONALE := :Old.FL_MERAMENTE_OCCASIONALE;
   aOldRowtype.ESERCIZIO_PADRE          := :Old.ESERCIZIO_PADRE;
   aOldRowtype.PG_PROCEDURA_PADRE       := :Old.PG_PROCEDURA_PADRE;
   aOldRowtype.NR_CONTRATTI             := :Old.NR_CONTRATTI;
   aOldRowtype.IMPORTO_LORDO            := :Old.IMPORTO_LORDO;
   aOldRowtype.IMPORTO_COMPLESSIVO      := :Old.IMPORTO_COMPLESSIVO;
   aOldRowtype.NR_CONTRATTI_INIZIALE    := :Old.NR_CONTRATTI_INIZIALE;
   aOldRowtype.DT_PUBBLICAZIONE         := :Old.DT_PUBBLICAZIONE;
   aOldRowtype.DT_FINE_PUBBLICAZIONE    := :Old.DT_FINE_PUBBLICAZIONE;
   aOldRowtype.DT_SCADENZA              := :Old.DT_SCADENZA;
   aOldRowtype.FL_PUBBLICA_CONTRATTO    := :Old.FL_PUBBLICA_CONTRATTO;
   aOldRowtype.UTCR                     := :Old.UTCR;
   aOldRowtype.DACR                     := :Old.DACR;
   aOldRowtype.UTUV                     := :Old.UTUV;
   aOldRowtype.DUVA                     := :Old.DUVA;
   aOldRowtype.PG_VER_REC               := :Old.PG_VER_REC;

   -- Scarico dello storico
   CNRSTO070.sto_INCARICHI_PROCEDURA(:old.PG_VER_REC+1, 'STOPRC', aOldRowType);
end;
/


