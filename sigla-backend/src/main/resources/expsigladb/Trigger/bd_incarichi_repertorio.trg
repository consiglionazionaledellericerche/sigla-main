CREATE OR REPLACE TRIGGER BD_INCARICHI_REPERTORIO
BEFORE Delete
On INCARICHI_REPERTORIO
For each row
WHEN (
old.stato != 'PP'
      )
Declare
   aOldRowtype incarichi_repertorio%rowtype;
Begin
   --
   -- Trigger attivato su cancellazione della tabella INCARICHI_REPERTORIO (Before)
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
   aOldRowtype.ESERCIZIO               := :Old.ESERCIZIO;
   aOldRowtype.PG_REPERTORIO           := :Old.PG_REPERTORIO;
   aOldRowtype.CD_CDS                  := :Old.CD_CDS;
   aOldRowtype.CD_UNITA_ORGANIZZATIVA  := :Old.CD_UNITA_ORGANIZZATIVA;
   aOldRowtype.STATO                   := :Old.STATO;
   aOldRowtype.ESERCIZIO_PROCEDURA     := :Old.ESERCIZIO_PROCEDURA;
   aOldRowtype.PG_PROCEDURA            := :Old.PG_PROCEDURA;
   aOldRowtype.CD_TERZO                := :Old.CD_TERZO;
   aOldRowtype.DT_REGISTRAZIONE        := :Old.DT_REGISTRAZIONE;
   aOldRowtype.DT_CANCELLAZIONE        := :Old.DT_CANCELLAZIONE;
   aOldRowtype.DT_STIPULA              := :Old.DT_STIPULA;
   aOldRowtype.DT_INIZIO_VALIDITA      := :Old.DT_INIZIO_VALIDITA;
   aOldRowtype.DT_FINE_VALIDITA        := :Old.DT_FINE_VALIDITA;
   aOldRowtype.DT_PROROGA              := :Old.DT_PROROGA;
   aOldRowtype.DT_PROROGA_PAGAM        := :Old.DT_PROROGA_PAGAM;
   aOldRowtype.TI_ISTITUZ_COMMERC      := :Old.TI_ISTITUZ_COMMERC;
   aOldRowtype.CD_TIPO_RAPPORTO        := :Old.CD_TIPO_RAPPORTO;
   aOldRowtype.CD_TRATTAMENTO          := :Old.CD_TRATTAMENTO;
   aOldRowtype.FL_PUBBLICA_CONTRATTO   := :Old.FL_PUBBLICA_CONTRATTO;
   aOldRowtype.IMPORTO_LORDO           := :Old.IMPORTO_LORDO;
   aOldRowtype.IMPORTO_COMPLESSIVO     := :Old.IMPORTO_COMPLESSIVO;
   aOldRowtype.UTCR                    := :Old.UTCR;
   aOldRowtype.DACR                    := :Old.DACR;
   aOldRowtype.UTUV                    := :Old.UTUV;
   aOldRowtype.DUVA                    := :Old.DUVA;
   aOldRowtype.PG_VER_REC              := :Old.PG_VER_REC;
   aOldRowtype.ID_SEDE_ACE             := :Old.ID_SEDE_ACE;

   -- Scarico dello storico
   CNRSTO070.sto_INCARICHI_REPERTORIO(:old.PG_VER_REC+1, 'STOREP', aOldRowType);
End;
/


