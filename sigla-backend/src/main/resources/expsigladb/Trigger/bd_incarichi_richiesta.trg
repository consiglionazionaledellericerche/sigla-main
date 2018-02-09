CREATE OR REPLACE TRIGGER BD_INCARICHI_RICHIESTA
BEFORE Delete
On INCARICHI_RICHIESTA
For each row
WHEN (
old.stato != 'P'
      )
Declare
   aOldRowtype incarichi_richiesta%rowtype;
Begin
   --
   -- Trigger attivato su cancellazione della tabella INCARICHI_RICHIESTA (Before)
   --
   -- Date: 28/11/2006
   -- Version: 1.0
   --
   -- Dependency: CNRSTO070
   --
   -- History:
   --
   -- Date: 28/11/2006
   -- Version: 1.0
   -- Creazione
   --
   aOldRowtype.ESERCIZIO               := :old.ESERCIZIO;
   aOldRowtype.PG_RICHIESTA            := :old.PG_RICHIESTA;
   aOldRowtype.CD_CDS                  := :old.CD_CDS;
   aOldRowtype.CD_UNITA_ORGANIZZATIVA  := :old.CD_UNITA_ORGANIZZATIVA;
   aOldRowtype.ATTIVITA                := :old.ATTIVITA;
   aOldRowtype.ATTIVITA_BREVE          := :old.ATTIVITA_BREVE;
   aOldRowtype.COMPETENZE              := :old.COMPETENZE;
   aOldRowtype.DURATA                  := :old.DURATA;
   aOldRowtype.SEDE_LAVORO             := :old.SEDE_LAVORO;
   aOldRowtype.CD_TEMATICA_ATTIVITA    := :old.CD_TEMATICA_ATTIVITA;
   aOldRowtype.NOTE                    := :old.NOTE;
   aOldRowtype.STATO                   := :old.STATO;
   aOldRowtype.DATA_PUBBLICAZIONE      := :old.DATA_PUBBLICAZIONE;
   aOldRowtype.DATA_FINE_PUBBLICAZIONE := :old.DATA_FINE_PUBBLICAZIONE;
   aOldRowtype.DATA_SCADENZA           := :old.DATA_SCADENZA;
   aOldRowtype.PERSONALE_INTERNO       := :old.PERSONALE_INTERNO;
   aOldRowtype.EMAIL_RISPOSTE          := :old.EMAIL_RISPOSTE;
   aOldRowtype.NR_RISORSE_DA_TROVARE   := :old.NR_RISORSE_DA_TROVARE;
   aOldRowtype.NR_RISORSE_TROVATE_SI   := :old.NR_RISORSE_TROVATE_SI;
   aOldRowtype.NR_RISORSE_TROVATE_NO   := :old.NR_RISORSE_TROVATE_NO;
   aOldRowtype.NR_RISORSE_TROVATE_NA   := :old.NR_RISORSE_TROVATE_NA;
   aOldRowtype.UTCR                    := :old.UTCR;
   aOldRowtype.DACR                    := :old.DACR;
   aOldRowtype.UTUV                    := :old.UTUV;
   aOldRowtype.DUVA                    := :old.DUVA;
   aOldRowtype.PG_VER_REC              := :old.PG_VER_REC;


   -- Scarico dello storico
   CNRSTO070.sto_INCARICHI_RICHIESTA(:old.PG_VER_REC+1, 'STORIC', aOldRowType);
end;
/


