--------------------------------------------------------
--  DDL for Package Body CNRSTO080
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "CNRSTO080" is

 procedure STO_FATTURA_ATTIVA_INTRA (aPgStorico number, aDest FATTURA_ATTIVA_INTRA%rowtype) is
  begin
   insert into FATTURA_ATTIVA_INTRA_S (
        PG_STORICO_               ,
        CD_CDS                    ,
        CD_UNITA_ORGANIZZATIVA    ,
        ESERCIZIO                 ,
        PG_FATTURA_ATTIVA         ,
        PG_RIGA_INTRA             ,
        AMMONTARE_EURO            ,
        ID_NATURA_TRANSAZIONE     ,
        ID_NOMENCLATURA_COMBINATA ,
        MASSA_NETTA               ,
        UNITA_SUPPLEMENTARI       ,
        VALORE_STATISTICO         ,
        ESERCIZIO_COND_CONSEGNA   ,
        CD_INCOTERM               ,
        ESERCIZIO_MOD_TRASPORTO   ,
        CD_MODALITA_TRASPORTO     ,
        PG_NAZIONE_DESTINAZIONE   ,
        CD_PROVINCIA_ORIGINE      ,
        DS_BENE                   ,
        ID_CPA                    ,
        ESERCIZIO_MOD_INCASSO     ,
        CD_MODALITA_INCASSO       ,
        ESERCIZIO_MOD_EROGAZIONE  ,
        CD_MODALITA_EROGAZIONE    ,
        DACR                      ,
        UTCR                      ,
        DUVA                      ,
        UTUV                      ,
        PG_VER_REC                ,
        FL_INVIATO         ,
        nr_protocollo ,
        nr_progressivo)
        Values (
            aPgStorico,
            aDest.CD_CDS                    ,
            aDest.CD_UNITA_ORGANIZZATIVA    ,
            aDest.ESERCIZIO                 ,
            aDest.PG_FATTURA_ATTIVA         ,
            aDest.PG_RIGA_INTRA             ,
            aDest.AMMONTARE_EURO            ,
            aDest.ID_NATURA_TRANSAZIONE     ,
            aDest.ID_NOMENCLATURA_COMBINATA ,
            aDest.MASSA_NETTA               ,
            aDest.UNITA_SUPPLEMENTARI       ,
            aDest.VALORE_STATISTICO         ,
            aDest.ESERCIZIO_COND_CONSEGNA   ,
            aDest.CD_INCOTERM               ,
            aDest.ESERCIZIO_MOD_TRASPORTO   ,
            aDest.CD_MODALITA_TRASPORTO     ,
            aDest.PG_NAZIONE_DESTINAZIONE   ,
            aDest.CD_PROVINCIA_ORIGINE      ,
            aDest.DS_BENE                   ,
            aDest.ID_CPA                    ,
            aDest.ESERCIZIO_MOD_INCASSO     ,
            aDest.CD_MODALITA_INCASSO       ,
            aDest.ESERCIZIO_MOD_EROGAZIONE  ,
            aDest.CD_MODALITA_EROGAZIONE    ,
            aDest.DACR                      ,
            aDest.UTCR                      ,
            aDest.DUVA                      ,
            aDest.UTUV                      ,
            aDest.PG_VER_REC                ,
            aDest.FL_INVIATO ,
            aDest.nr_protocollo,
            aDest.nr_progressivo);
 end;

 procedure STO_FATTURA_PASSIVA_INTRA (aPgStorico number, aDest FATTURA_PASSIVA_INTRA%rowtype) is
  begin
   insert into FATTURA_PASSIVA_INTRA_S (
     PG_STORICO_
     ,CD_CDS
     ,CD_UNITA_ORGANIZZATIVA
     ,ESERCIZIO
     ,PG_FATTURA_PASSIVA
     ,PG_RIGA_INTRA
     ,AMMONTARE_EURO
     ,AMMONTARE_DIVISA
     ,ID_NATURA_TRANSAZIONE
     ,ID_NOMENCLATURA_COMBINATA
     ,MASSA_NETTA
     ,UNITA_SUPPLEMENTARI
     ,VALORE_STATISTICO
     ,ESERCIZIO_COND_CONSEGNA
     ,CD_INCOTERM
     ,ESERCIZIO_MOD_TRASPORTO
     ,CD_MODALITA_TRASPORTO
     ,PG_NAZIONE_PROVENIENZA
     ,PG_NAZIONE_ORIGINE
     ,CD_PROVINCIA_DESTINAZIONE
     ,DS_BENE
     ,ID_CPA
     ,ESERCIZIO_MOD_INCASSO
     ,CD_MODALITA_INCASSO
     ,ESERCIZIO_MOD_EROGAZIONE
     ,CD_MODALITA_EROGAZIONE
     ,DACR
     ,UTCR
     ,DUVA
     ,UTUV
     ,PG_VER_REC
     ,FL_INVIATO,
      nr_protocollo,
      nr_progressivo)
     Values (
     aPgStorico
    ,aDest.CD_CDS
    ,aDest.CD_UNITA_ORGANIZZATIVA
    ,aDest.ESERCIZIO
    ,aDest.PG_FATTURA_PASSIVA
    ,aDest.PG_RIGA_INTRA
    ,aDest.AMMONTARE_EURO
    ,aDest.AMMONTARE_DIVISA
    ,aDest.ID_NATURA_TRANSAZIONE
    ,aDest.ID_NOMENCLATURA_COMBINATA
    ,aDest.MASSA_NETTA
    ,aDest.UNITA_SUPPLEMENTARI
    ,aDest.VALORE_STATISTICO
    ,aDest.ESERCIZIO_COND_CONSEGNA
    ,aDest.CD_INCOTERM
    ,aDest.ESERCIZIO_MOD_TRASPORTO
    ,aDest.CD_MODALITA_TRASPORTO
    ,aDest.PG_NAZIONE_PROVENIENZA
    ,aDest.PG_NAZIONE_ORIGINE
    ,aDest.CD_PROVINCIA_DESTINAZIONE
    ,aDest.DS_BENE
    ,aDest.ID_CPA
    ,aDest.ESERCIZIO_MOD_INCASSO
    ,aDest.CD_MODALITA_INCASSO
    ,aDest.ESERCIZIO_MOD_EROGAZIONE
    ,aDest.CD_MODALITA_EROGAZIONE
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.DUVA
    ,aDest.UTUV
    ,aDest.PG_VER_REC
    ,aDest.FL_INVIATO,
    aDest.nr_protocollo,
    aDest.nr_progressivo);
 end;

End;
