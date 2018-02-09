CREATE OR REPLACE TRIGGER BD_INCARICHI_PROCEDURA_ANNO
BEFORE Delete
On INCARICHI_PROCEDURA_ANNO
For each row
Declare
   aOldRowtype incarichi_procedura_anno%rowtype;
   pStato      incarichi_procedura.stato%Type;
Begin
   Begin
     Select stato Into pStato
     From incarichi_procedura
     Where esercizio    = :Old.ESERCIZIO
     And   pg_procedura = :Old.PG_PROCEDURA;
   Exception
     When Others Then
       pStato := Null;
   End;
   If pStato Is Null Or pStato != 'PP' Then
      --
      -- Trigger attivato su cancellazione della tabella INCARICHI_PROCEDURA_ANNO (Before)
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
      aOldRowtype.ESERCIZIO           := :Old.ESERCIZIO;
      aOldRowtype.PG_PROCEDURA        := :Old.PG_PROCEDURA;
      aOldRowtype.ESERCIZIO_LIMITE    := :Old.ESERCIZIO_LIMITE;
      aOldRowtype.IMPORTO_INIZIALE    := :Old.IMPORTO_INIZIALE;
      aOldRowtype.IMPORTO_COMPLESSIVO := :Old.IMPORTO_COMPLESSIVO;
      aOldRowtype.UTCR                := :Old.UTCR;
      aOldRowtype.DACR                := :Old.DACR;
      aOldRowtype.UTUV                := :Old.UTUV;
      aOldRowtype.DUVA                := :Old.DUVA;
      aOldRowtype.PG_VER_REC          := :Old.PG_VER_REC;

      -- Scarico dello storico
      CNRSTO070.sto_INCARICHI_PROCEDURA_ANNO(:old.PG_VER_REC+1, 'STOPRCANNO', aOldRowType);
   End If;
End;
/


