CREATE OR REPLACE TRIGGER BU_INCARICHI_PROCEDURA
BEFORE Update
On INCARICHI_PROCEDURA
For each row
WHEN (
not (old.stato = 'PP' and new.stato in ('PP', 'PU', 'PD')) and
 (old.ESERCIZIO                != new.ESERCIZIO or
  old.PG_PROCEDURA             != new.PG_PROCEDURA or
  old.CD_CDS                   != new.CD_CDS or
  old.CD_UNITA_ORGANIZZATIVA   != new.CD_UNITA_ORGANIZZATIVA or
  old.STATO                    != new.STATO or
  Nvl(To_char(old.DT_REGISTRAZIONE,'yyyymmdd'),'18501231') !=
      Nvl(To_char(new.DT_REGISTRAZIONE,'yyyymmdd'),'18501231') or
  Nvl(To_char(old.DT_CANCELLAZIONE,'yyyymmdd'),'18501231') !=
      Nvl(To_char(new.DT_CANCELLAZIONE,'yyyymmdd'),'18501231') or
  Nvl(To_char(old.DT_RESPINTA,'yyyymmdd'),'18501231') !=
      Nvl(To_char(new.DT_RESPINTA,'yyyymmdd'),'18501231') or
  Nvl(old.ESERCIZIO_RICHIESTA,-1234) != Nvl(new.ESERCIZIO_RICHIESTA,-1234) or
  Nvl(old.PG_RICHIESTA,-1234)  != Nvl(new.PG_RICHIESTA,-1234) or
  old.CD_FIRMATARIO            != new.CD_FIRMATARIO or
  Nvl(old.CD_TERZO_RESP,-1234) != Nvl(new.CD_TERZO_RESP,-1234) or
  Nvl(old.CD_PROC_AMM,'**.*.*.**') != Nvl(new.CD_PROC_AMM,'**.*.*.**') or
  Nvl(old.CD_TIPO_ATTO,'**.*.*.**') != Nvl(new.CD_TIPO_ATTO,'**.*.*.**') or
  Nvl(old.DS_ATTO,'**.*.*.**') != Nvl(new.DS_ATTO,'**.*.*.**') or
  Nvl(old.CD_PROTOCOLLO,'**.*.*.**') != Nvl(new.CD_PROTOCOLLO,'**.*.*.**') or
  old.OGGETTO                  != new.OGGETTO or
  old.PG_COMUNE                != new.PG_COMUNE or
  old.CD_TIPO_INCARICO         != new.CD_TIPO_INCARICO or
  old.CD_TIPO_ATTIVITA         != new.CD_TIPO_ATTIVITA or
  old.TIPO_NATURA              != new.TIPO_NATURA or
  old.FL_MERAMENTE_OCCASIONALE != new.FL_MERAMENTE_OCCASIONALE or
  Nvl(old.ESERCIZIO_PADRE,-1234)          != Nvl(new.ESERCIZIO_PADRE,-1234) or
  Nvl(old.PG_PROCEDURA_PADRE,-1234)       != Nvl(new.PG_PROCEDURA_PADRE,-1234) or
  old.NR_CONTRATTI             != new.NR_CONTRATTI or
  old.IMPORTO_LORDO            != new.IMPORTO_LORDO or
  old.IMPORTO_COMPLESSIVO      != new.IMPORTO_COMPLESSIVO or
  Nvl(old.NR_CONTRATTI_INIZIALE,-1234)    != Nvl(new.NR_CONTRATTI_INIZIALE,-1234) or
  Nvl(To_char(old.DT_PUBBLICAZIONE,'yyyymmdd'),'18501231') !=
      Nvl(To_char(new.DT_PUBBLICAZIONE,'yyyymmdd'),'18501231') or
  Nvl(To_char(old.DT_FINE_PUBBLICAZIONE,'yyyymmdd'),'18501231') !=
      Nvl(To_char(new.DT_FINE_PUBBLICAZIONE,'yyyymmdd'),'18501231') or
  Nvl(To_char(old.DT_SCADENZA,'yyyymmdd'),'18501231') !=
      Nvl(To_char(new.DT_SCADENZA,'yyyymmdd'),'18501231') or
  old.FL_PUBBLICA_CONTRATTO    != new.FL_PUBBLICA_CONTRATTO or
  old.UTCR                     != new.UTCR or
  old.DACR                     != new.DACR or
  old.UTUV                     != new.UTUV)
      )
Declare
   aOldRowtype incarichi_procedura%rowtype;
Begin
   --
   -- Trigger attivato su aggiornamento della tabella INCARICHI_PROCEDURA (Before)
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
   CNRSTO070.sto_INCARICHI_PROCEDURA(:new.PG_VER_REC, 'STOPRC', aOldRowType);
End;
/


