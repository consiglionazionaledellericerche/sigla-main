CREATE OR REPLACE TRIGGER BU_INCARICHI_PROCEDURA_ARCH
BEFORE Update
On INCARICHI_PROCEDURA_ARCHIVIO
For each row
WHEN (
old.ESERCIZIO           != new.ESERCIZIO or
  old.PG_PROCEDURA        != new.PG_PROCEDURA or
  old.PROGRESSIVO_RIGA    != new.PROGRESSIVO_RIGA or
  old.NOME_FILE           != new.NOME_FILE or
  Nvl(old.DS_FILE,'**.*.*.**') != Nvl(new.DS_FILE,'**.*.*.**') or
  old.TIPO_ARCHIVIO       != new.TIPO_ARCHIVIO or
  old.STATO               != new.STATO or
  old.UTCR                != new.UTCR or
  old.DACR                != new.DACR or
  old.UTUV                != new.UTUV
      )
Declare
   aOldRowtype incarichi_procedura_archivio%rowtype;
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
      -- Trigger attivato su aggiornamento della tabella INCARICHI_PROCEDURA_ARCHIVIO (Before)
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
      CNRSTO070.sto_INCARICHI_PROCEDURA_ARCH(:new.PG_VER_REC, 'STOPRCARCH', aOldRowType);
   End If;
End;
/


