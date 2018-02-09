CREATE OR REPLACE TRIGGER BU_INCARICHI_RICHIESTA
BEFORE Update
On INCARICHI_RICHIESTA
For each row
WHEN (
not (old.stato = 'P' and new.stato in ('P', 'D')) and
 (old.ESERCIZIO               != new.ESERCIZIO or
  old.PG_RICHIESTA            != new.PG_RICHIESTA or
  old.CD_CDS                  != new.CD_CDS or
  old.CD_UNITA_ORGANIZZATIVA  != new.CD_UNITA_ORGANIZZATIVA or
  old.ATTIVITA                != new.ATTIVITA or
  old.ATTIVITA_BREVE          != new.ATTIVITA_BREVE or
  old.DURATA                  != new.DURATA or
  old.SEDE_LAVORO             != new.SEDE_LAVORO or
  old.STATO                   != new.STATO or
  Nvl(old.NOTE,'**.*.*.**')                 != Nvl(new.NOTE,'**.*.*.**') or
  Nvl(old.COMPETENZE,'**.*.*.**')           != Nvl(new.COMPETENZE,'**.*.*.**') or
  Nvl(old.CD_TEMATICA_ATTIVITA,'**.*.*.**') != Nvl(new.CD_TEMATICA_ATTIVITA,'**.*.*.**') or
  Nvl(old.PERSONALE_INTERNO,'**.*.*.**')    != Nvl(new.PERSONALE_INTERNO,'**.*.*.**') or
  Nvl(To_char(old.DATA_PUBBLICAZIONE,'yyyymmdd'),'18501231') !=
        Nvl(To_char(new.DATA_PUBBLICAZIONE,'yyyymmdd'),'18501231') or
  Nvl(To_char(old.DATA_FINE_PUBBLICAZIONE,'yyyymmdd'),'18501231')  !=
        Nvl(To_char(new.DATA_FINE_PUBBLICAZIONE,'yyyymmdd'),'18501231') or
  Nvl(To_char(old.DATA_SCADENZA,'yyyymmdd'),'18501231') !=
        Nvl(To_char(new.DATA_SCADENZA,'yyyymmdd'),'18501231') or
  old.EMAIL_RISPOSTE          != new.EMAIL_RISPOSTE or
  old.NR_RISORSE_DA_TROVARE   != new.NR_RISORSE_DA_TROVARE or
  old.NR_RISORSE_TROVATE_SI   != new.NR_RISORSE_TROVATE_SI or
  old.NR_RISORSE_TROVATE_NO   != new.NR_RISORSE_TROVATE_NO or
  old.NR_RISORSE_TROVATE_NA   != new.NR_RISORSE_TROVATE_NA or
  old.UTCR                    != new.UTCR or
  old.DACR                    != new.DACR or
  old.UTUV                    != new.UTUV)
      )
Declare
   aOldRowtype incarichi_richiesta%rowtype;
Begin
   --
   -- Trigger attivato su aggiornamento della tabella INCARICHI_RICHIESTA (Before)
   --
   -- Date: 18/07/2006
   -- Version: 1.3
   --
   -- Dependency: CNRSTO070
   --
   -- History:
   --
   -- Date: 28/10/2008
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
   CNRSTO070.sto_INCARICHI_RICHIESTA(:new.PG_VER_REC, 'STORIC', aOldRowType);
End;
/


