--------------------------------------------------------
--  DDL for Package Body CNRSTO070
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "CNRSTO070" is
 procedure sto_INCARICHI_RICHIESTA (aPgStorico number, aDsStorico varchar2, aDest INCARICHI_RICHIESTA%rowtype) is
  begin
   insert into INCARICHI_RICHIESTA_S (
     pg_storico_
    ,ds_storico_
    ,ESERCIZIO
    ,PG_RICHIESTA
    ,CD_CDS
    ,CD_UNITA_ORGANIZZATIVA
    ,ATTIVITA
    ,ATTIVITA_BREVE
    ,COMPETENZE
    ,DURATA
    ,SEDE_LAVORO
    ,CD_TEMATICA_ATTIVITA
    ,NOTE
    ,STATO
    ,DATA_PUBBLICAZIONE
    ,DATA_FINE_PUBBLICAZIONE
    ,DATA_SCADENZA
    ,PERSONALE_INTERNO
    ,EMAIL_RISPOSTE
    ,NR_RISORSE_DA_TROVARE
    ,NR_RISORSE_TROVATE_SI
    ,NR_RISORSE_TROVATE_NO
    ,NR_RISORSE_TROVATE_NA
    ,UTCR
    ,DACR
    ,UTUV
    ,DUVA
    ,PG_VER_REC
   ) values (
     aPgStorico
    ,aDsStorico
    ,aDest.ESERCIZIO
    ,aDest.PG_RICHIESTA
    ,aDest.CD_CDS
    ,aDest.CD_UNITA_ORGANIZZATIVA
    ,aDest.ATTIVITA
    ,aDest.ATTIVITA_BREVE
    ,aDest.COMPETENZE
    ,aDest.DURATA
    ,aDest.SEDE_LAVORO
    ,aDest.CD_TEMATICA_ATTIVITA
    ,aDest.NOTE
    ,aDest.STATO
    ,aDest.DATA_PUBBLICAZIONE
    ,aDest.DATA_FINE_PUBBLICAZIONE
    ,aDest.DATA_SCADENZA
    ,aDest.PERSONALE_INTERNO
    ,aDest.EMAIL_RISPOSTE
    ,aDest.NR_RISORSE_DA_TROVARE
    ,aDest.NR_RISORSE_TROVATE_SI
    ,aDest.NR_RISORSE_TROVATE_NO
    ,aDest.NR_RISORSE_TROVATE_NA
    ,aDest.UTCR
    ,aDest.DACR
    ,aDest.UTUV
    ,aDest.DUVA
    ,aDest.PG_VER_REC
    );
 end;

 procedure sto_INCARICHI_PROCEDURA (aPgStorico number, aDsStorico varchar2, aDest INCARICHI_PROCEDURA%rowtype) is
  begin
   insert into INCARICHI_PROCEDURA_S (
     pg_storico_
    ,ds_storico_
    ,ESERCIZIO
    ,PG_PROCEDURA
    ,CD_CDS
    ,CD_UNITA_ORGANIZZATIVA
    ,STATO
    ,DT_REGISTRAZIONE
    ,DT_CANCELLAZIONE
    ,DT_RESPINTA
    ,ESERCIZIO_RICHIESTA
    ,PG_RICHIESTA
    ,CD_FIRMATARIO
    ,CD_TERZO_RESP
    ,CD_PROC_AMM
    ,CD_TIPO_ATTO
    ,DS_ATTO
    ,CD_PROTOCOLLO
    ,OGGETTO
    ,PG_COMUNE
    ,CD_TIPO_INCARICO
    ,CD_TIPO_ATTIVITA
    ,TIPO_NATURA
    ,FL_MERAMENTE_OCCASIONALE
    ,ESERCIZIO_PADRE
    ,PG_PROCEDURA_PADRE
    ,NR_CONTRATTI
    ,IMPORTO_LORDO
    ,IMPORTO_COMPLESSIVO
    ,NR_CONTRATTI_INIZIALE
    ,DT_PUBBLICAZIONE
    ,DT_FINE_PUBBLICAZIONE
    ,DT_SCADENZA
    ,FL_PUBBLICA_CONTRATTO
    ,UTCR
    ,DACR
    ,UTUV
    ,DUVA
    ,PG_VER_REC
   ) values (
     aPgStorico
    ,aDsStorico
    ,aDest.ESERCIZIO
    ,aDest.PG_PROCEDURA
    ,aDest.CD_CDS
    ,aDest.CD_UNITA_ORGANIZZATIVA
    ,aDest.STATO
    ,aDest.DT_REGISTRAZIONE
    ,aDest.DT_CANCELLAZIONE
    ,aDest.DT_RESPINTA
    ,aDest.ESERCIZIO_RICHIESTA
    ,aDest.PG_RICHIESTA
    ,aDest.CD_FIRMATARIO
    ,aDest.CD_TERZO_RESP
    ,aDest.CD_PROC_AMM
    ,aDest.CD_TIPO_ATTO
    ,aDest.DS_ATTO
    ,aDest.CD_PROTOCOLLO
    ,aDest.OGGETTO
    ,aDest.PG_COMUNE
    ,aDest.CD_TIPO_INCARICO
    ,aDest.CD_TIPO_ATTIVITA
    ,aDest.TIPO_NATURA
    ,aDest.FL_MERAMENTE_OCCASIONALE
    ,aDest.ESERCIZIO_PADRE
    ,aDest.PG_PROCEDURA_PADRE
    ,aDest.NR_CONTRATTI
    ,aDest.IMPORTO_LORDO
    ,aDest.IMPORTO_COMPLESSIVO
    ,aDest.NR_CONTRATTI_INIZIALE
    ,aDest.DT_PUBBLICAZIONE
    ,aDest.DT_FINE_PUBBLICAZIONE
    ,aDest.DT_SCADENZA
    ,aDest.FL_PUBBLICA_CONTRATTO
    ,aDest.UTCR
    ,aDest.DACR
    ,aDest.UTUV
    ,aDest.DUVA
    ,aDest.PG_VER_REC);
 end;

 procedure sto_INCARICHI_PROCEDURA_ANNO (aPgStorico number, aDsStorico varchar2, aDest INCARICHI_PROCEDURA_ANNO%rowtype) is
  begin
   insert into INCARICHI_PROCEDURA_ANNO_S (
     pg_storico_
    ,ds_storico_
    ,ESERCIZIO
    ,PG_PROCEDURA
    ,ESERCIZIO_LIMITE
    ,IMPORTO_INIZIALE
    ,IMPORTO_COMPLESSIVO
    ,UTCR
    ,DACR
    ,UTUV
    ,DUVA
    ,PG_VER_REC
   ) values (
     aPgStorico
    ,aDsStorico
    ,aDest.ESERCIZIO
    ,aDest.PG_PROCEDURA
    ,aDest.ESERCIZIO_LIMITE
    ,aDest.IMPORTO_INIZIALE
    ,aDest.IMPORTO_COMPLESSIVO
    ,aDest.UTCR
    ,aDest.DACR
    ,aDest.UTUV
    ,aDest.DUVA
    ,aDest.PG_VER_REC);
 end;


 procedure sto_INCARICHI_PROCEDURA_ARCH (aPgStorico number, aDsStorico varchar2, aDest INCARICHI_PROCEDURA_ARCHIVIO%rowtype) is
  begin
   insert into INCARICHI_PROCEDURA_ARCH_S (
     pg_storico_
    ,ds_storico_
    ,ESERCIZIO
    ,PG_PROCEDURA
    ,PROGRESSIVO_RIGA
    ,NOME_FILE
    ,DS_FILE
    ,TIPO_ARCHIVIO
--    , BDATA
    ,STATO
    ,UTCR
    ,DACR
    ,UTUV
    ,DUVA
    ,PG_VER_REC
   ) values (
     aPgStorico
    ,aDsStorico
    ,aDest.ESERCIZIO
    ,aDest.PG_PROCEDURA
    ,aDest.PROGRESSIVO_RIGA
    ,aDest.NOME_FILE
    ,aDest.DS_FILE
    ,aDest.TIPO_ARCHIVIO
--    ,aDest.BDATA
    ,aDest.STATO
    ,aDest.UTCR
    ,aDest.DACR
    ,aDest.UTUV
    ,aDest.DUVA
    ,aDest.PG_VER_REC);
  end;

  procedure sto_INCARICHI_REPERTORIO (aPgStorico number, aDsStorico varchar2, aDest INCARICHI_REPERTORIO%rowtype) is
  begin
   insert into INCARICHI_REPERTORIO_S (
     pg_storico_
    ,ds_storico_
    ,ESERCIZIO
    ,PG_REPERTORIO
    ,CD_CDS
    ,CD_UNITA_ORGANIZZATIVA
    ,STATO
    ,ESERCIZIO_PROCEDURA
    ,PG_PROCEDURA
    ,CD_TERZO
    ,DT_REGISTRAZIONE
    ,DT_CANCELLAZIONE
    ,DT_STIPULA
    ,DT_INIZIO_VALIDITA
    ,DT_FINE_VALIDITA
    ,DT_PROROGA
    ,DT_PROROGA_PAGAM
    ,TI_ISTITUZ_COMMERC
    ,CD_TIPO_RAPPORTO
    ,CD_TRATTAMENTO
    ,FL_PUBBLICA_CONTRATTO
    ,IMPORTO_LORDO
    ,IMPORTO_COMPLESSIVO
    ,UTCR
    ,DACR
    ,UTUV
    ,DUVA
    ,PG_VER_REC
    ,ID_SEDE_ACE
   ) values (
     aPgStorico
    ,aDsStorico
    ,aDest.ESERCIZIO
    ,aDest.PG_REPERTORIO
    ,aDest.CD_CDS
    ,aDest.CD_UNITA_ORGANIZZATIVA
    ,aDest.STATO
    ,aDest.ESERCIZIO_PROCEDURA
    ,aDest.PG_PROCEDURA
    ,aDest.CD_TERZO
    ,aDest.DT_REGISTRAZIONE
    ,aDest.DT_CANCELLAZIONE
    ,aDest.DT_STIPULA
    ,aDest.DT_INIZIO_VALIDITA
    ,aDest.DT_FINE_VALIDITA
    ,aDest.DT_PROROGA
    ,aDest.DT_PROROGA_PAGAM
    ,aDest.TI_ISTITUZ_COMMERC
    ,aDest.CD_TIPO_RAPPORTO
    ,aDest.CD_TRATTAMENTO
    ,aDest.FL_PUBBLICA_CONTRATTO
    ,aDest.IMPORTO_LORDO
    ,aDest.IMPORTO_COMPLESSIVO
    ,aDest.UTCR
    ,aDest.DACR
    ,aDest.UTUV
    ,aDest.DUVA
    ,aDest.PG_VER_REC
    ,aDest.ID_SEDE_ACE);
  end;

  procedure sto_INCARICHI_REPERTORIO_ANNO (aPgStorico number, aDsStorico varchar2, aDest INCARICHI_REPERTORIO_ANNO%rowtype) is
  begin
   insert into INCARICHI_REPERTORIO_ANNO_S (
     pg_storico_
    ,ds_storico_
    ,ESERCIZIO
    ,PG_REPERTORIO
    ,ESERCIZIO_LIMITE
    ,IMPORTO_INIZIALE
    ,IMPORTO_COMPLESSIVO
    ,UTCR
    ,DACR
    ,UTUV
    ,DUVA
    ,PG_VER_REC
   ) values (
     aPgStorico
    ,aDsStorico
    ,aDest.ESERCIZIO
    ,aDest.PG_REPERTORIO
    ,aDest.ESERCIZIO_LIMITE
    ,aDest.IMPORTO_INIZIALE
    ,aDest.IMPORTO_COMPLESSIVO
    ,aDest.UTCR
    ,aDest.DACR
    ,aDest.UTUV
    ,aDest.DUVA
    ,aDest.PG_VER_REC);
 end;


 procedure sto_INCARICHI_REPERTORIO_ARCH (aPgStorico number, aDsStorico varchar2, aDest INCARICHI_REPERTORIO_ARCHIVIO%rowtype) is
  begin
   insert into INCARICHI_REPERTORIO_ARCH_S (
     pg_storico_
    ,ds_storico_
    ,ESERCIZIO
    ,PG_REPERTORIO
    ,PROGRESSIVO_RIGA
    ,NOME_FILE
    ,DS_FILE
    ,TIPO_ARCHIVIO
--    , BDATA
    ,STATO
    ,UTCR
    ,DACR
    ,UTUV
    ,DUVA
    ,PG_VER_REC
   ) values (
     aPgStorico
    ,aDsStorico
    ,aDest.ESERCIZIO
    ,aDest.PG_REPERTORIO
    ,aDest.PROGRESSIVO_RIGA
    ,aDest.NOME_FILE
    ,aDest.DS_FILE
    ,aDest.TIPO_ARCHIVIO
--    ,aDest.BDATA
    ,aDest.STATO
    ,aDest.UTCR
    ,aDest.DACR
    ,aDest.UTUV
    ,aDest.DUVA
    ,aDest.PG_VER_REC);
  end;
end;
