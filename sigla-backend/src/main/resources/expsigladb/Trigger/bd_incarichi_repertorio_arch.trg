CREATE OR REPLACE TRIGGER BD_INCARICHI_REPERTORIO_ARCH
BEFORE Delete
On INCARICHI_REPERTORIO_ARCHIVIO
For each row
Declare
   aOldRowtype incarichi_repertorio_archivio%rowtype;
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
      -- Trigger attivato su cancellazione della tabella INCARICHI_REPERTORIO_ARCHIVIO (Before)
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
      aOldRowtype.PROGRESSIVO_RIGA    := :Old.PROGRESSIVO_RIGA;
      aOldRowtype.NOME_FILE           := :Old.NOME_FILE;
      aOldRowtype.DS_FILE             := :Old.DS_FILE;
      aOldRowtype.TIPO_ARCHIVIO       := :Old.TIPO_ARCHIVIO;
      aOldRowtype.STATO               := :Old.STATO;
      aOldRowtype.UTCR                := :Old.UTCR;
      aOldRowtype.DACR                := :Old.DACR;
      aOldRowtype.UTUV                := :Old.UTUV;
      aOldRowtype.DUVA                := :Old.DUVA;
      aOldRowtype.PG_VER_REC          := :Old.PG_VER_REC;

      -- Scarico dello storico
      CNRSTO070.sto_INCARICHI_REPERTORIO_ARCH(:old.PG_VER_REC+1, 'STOREPARCH', aOldRowType);
   End If;
End;
/


