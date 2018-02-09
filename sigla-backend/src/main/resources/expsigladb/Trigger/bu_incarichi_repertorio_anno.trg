CREATE OR REPLACE TRIGGER BU_INCARICHI_REPERTORIO_ANNO
BEFORE Update
On INCARICHI_REPERTORIO_ANNO
For each row
WHEN (
old.ESERCIZIO           != new.ESERCIZIO or
  old.PG_REPERTORIO       != new.PG_REPERTORIO or
  old.ESERCIZIO_LIMITE    != new.ESERCIZIO_LIMITE or
  old.IMPORTO_INIZIALE    != new.IMPORTO_INIZIALE or
  old.IMPORTO_COMPLESSIVO != new.IMPORTO_COMPLESSIVO or
  old.UTCR                != new.UTCR or
  old.DACR                != new.DACR or
  old.UTUV                != new.UTUV
      )
Declare
   aOldRowtype incarichi_repertorio_anno%rowtype;
   pStato      incarichi_procedura.stato%Type;
Begin
   Begin
     Select stato Into pStato
     From incarichi_repertorio
     Where esercizio     = :Old.ESERCIZIO
     And   pg_repertorio = :Old.PG_REPERTORIO;
   Exception
     When Others Then
       pStato := Null;
   End;
   If pStato Is Null Or pStato != 'PP' Then
      --
      -- Trigger attivato su aggiornamento della tabella INCARICHI_REPERTORIO_ANNO (Before)
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
      aOldRowtype.PG_REPERTORIO       := :Old.PG_REPERTORIO;
      aOldRowtype.ESERCIZIO_LIMITE    := :Old.ESERCIZIO_LIMITE;
      aOldRowtype.IMPORTO_INIZIALE    := :Old.IMPORTO_INIZIALE;
      aOldRowtype.IMPORTO_COMPLESSIVO := :Old.IMPORTO_COMPLESSIVO;
      aOldRowtype.UTCR                := :Old.UTCR;
      aOldRowtype.DACR                := :Old.DACR;
      aOldRowtype.UTUV                := :Old.UTUV;
      aOldRowtype.DUVA                := :Old.DUVA;
      aOldRowtype.PG_VER_REC          := :Old.PG_VER_REC;

      -- Scarico dello storico
      CNRSTO070.sto_INCARICHI_REPERTORIO_ANNO(:new.PG_VER_REC, 'STOREPANNO', aOldRowType);
   End If;
End;
/


