CREATE OR REPLACE TRIGGER BU_INCARICHI_REPERTORIO
BEFORE Update
On INCARICHI_REPERTORIO
For each row
WHEN (
Not (old.stato = 'PP' and new.stato in ('PP', 'PD')) and
 (old.ESERCIZIO                != new.ESERCIZIO or
  old.PG_REPERTORIO            != new.PG_REPERTORIO or
  old.CD_CDS                   != new.CD_CDS or
  old.CD_UNITA_ORGANIZZATIVA   != new.CD_UNITA_ORGANIZZATIVA or
  old.STATO                    != new.STATO or
  old.ESERCIZIO_PROCEDURA      != new.ESERCIZIO_PROCEDURA or
  old.PG_PROCEDURA             != new.PG_PROCEDURA or
  old.CD_TERZO                 != new.CD_TERZO or
  Nvl(To_char(old.DT_REGISTRAZIONE,'yyyymmdd'),'18501231') !=
      Nvl(To_char(new.DT_REGISTRAZIONE,'yyyymmdd'),'18501231') or
  Nvl(To_char(old.DT_CANCELLAZIONE,'yyyymmdd'),'18501231') !=
      Nvl(To_char(new.DT_CANCELLAZIONE,'yyyymmdd'),'18501231') or
  Nvl(To_char(old.DT_STIPULA,'yyyymmdd'),'18501231') !=
      Nvl(To_char(new.DT_STIPULA,'yyyymmdd'),'18501231') or
  Nvl(To_char(old.DT_INIZIO_VALIDITA,'yyyymmdd'),'18501231') !=
      Nvl(To_char(new.DT_INIZIO_VALIDITA,'yyyymmdd'),'18501231') or
  Nvl(To_char(old.DT_FINE_VALIDITA,'yyyymmdd'),'18501231') !=
      Nvl(To_char(new.DT_FINE_VALIDITA,'yyyymmdd'),'18501231') or
  Nvl(To_char(old.DT_PROROGA,'yyyymmdd'),'18501231') !=
      Nvl(To_char(new.DT_PROROGA,'yyyymmdd'),'18501231') or
  Nvl(To_char(old.DT_PROROGA_PAGAM,'yyyymmdd'),'18501231') !=
      Nvl(To_char(new.DT_PROROGA_PAGAM,'yyyymmdd'),'18501231') or
  Nvl(old.TI_ISTITUZ_COMMERC,'**.*.*.**') != Nvl(new.TI_ISTITUZ_COMMERC,'**.*.*.**') or
  Nvl(old.CD_TIPO_RAPPORTO,'**.*.*.**') != Nvl(new.CD_TIPO_RAPPORTO,'**.*.*.**') or
  Nvl(old.CD_TRATTAMENTO,'**.*.*.**') != Nvl(new.CD_TRATTAMENTO,'**.*.*.**') or
  old.FL_PUBBLICA_CONTRATTO != new.FL_PUBBLICA_CONTRATTO or
  old.IMPORTO_LORDO         != new.IMPORTO_LORDO or
  old.IMPORTO_COMPLESSIVO   != new.IMPORTO_COMPLESSIVO or
  old.UTCR                  != new.UTCR or
  old.DACR                  != new.DACR or
  old.UTUV                  != new.UTUV or
  old.ID_SEDE_ACE           != new.ID_SEDE_ACE)
      )
Declare
   aOldRowtype incarichi_repertorio%rowtype;
Begin
   --
   -- Trigger attivato su aggiornamento della tabella INCARICHI_REPERTORIO (Before)
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
   aOldRowtype.ID_SEDE_ACE              := :Old.ID_SEDE_ACE;

   -- Scarico dello storico
   CNRSTO070.sto_INCARICHI_REPERTORIO(:new.PG_VER_REC, 'STOREP', aOldRowType);
End;
/


