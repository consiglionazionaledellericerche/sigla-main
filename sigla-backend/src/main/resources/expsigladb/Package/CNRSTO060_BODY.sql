--------------------------------------------------------
--  DDL for Package Body CNRSTO060
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "CNRSTO060" is

 procedure scaricaSuStorico(aOldEs pdg_esercizio%rowtype, aEs pdg_esercizio%rowtype) is
     wSysdate DATE;
     sSysdate VARCHAR2(100);
     Cursor c (v_esercizio NUMBER, v_centro VARCHAR2) Is
        Select *
        From PDG_MODULO
        Where ESERCIZIO=v_esercizio
          And CD_CENTRO_RESPONSABILITA=v_centro;
     Cursor cc (v_esercizio NUMBER, v_centro VARCHAR2) Is
        Select *
        From PDG_MODULO_COSTI
        Where ESERCIZIO=v_esercizio
          And CD_CENTRO_RESPONSABILITA=v_centro;
     Cursor cs (v_esercizio NUMBER, v_centro VARCHAR2) Is
        Select *
        From PDG_MODULO_SPESE
        Where ESERCIZIO=v_esercizio
          And CD_CENTRO_RESPONSABILITA=v_centro;
     Cursor ce (v_esercizio NUMBER, v_centro VARCHAR2) Is
        Select *
        From PDG_MODULO_ENTRATE
        Where ESERCIZIO=v_esercizio
          And CD_CENTRO_RESPONSABILITA=v_centro;
 begin
    wSysdate := Sysdate;
    sSysdate := To_Char(wSysdate,'DD/MM/YYYY HH24:MI:SS');

    For crec In c (aOldEs.ESERCIZIO,aOldEs.CD_CENTRO_RESPONSABILITA)
    Loop

       insert into PDG_MODULO_S (
         pg_storico_
        ,ds_storico_
        ,ESERCIZIO
        ,CD_CENTRO_RESPONSABILITA
        ,PG_PROGETTO
        ,STATO
        ,UTCR
        ,DACR
        ,UTUV
        ,DUVA
        ,PG_VER_REC
       ) values (
        aOldEs.PG_VER_REC
        ,sSysdate
        ,crec.ESERCIZIO
        ,crec.CD_CENTRO_RESPONSABILITA
        ,crec.PG_PROGETTO
        ,crec.STATO
        ,crec.UTCR
        ,crec.DACR
        ,crec.UTUV
        ,crec.DUVA
        ,crec.PG_VER_REC
        );
    End Loop;

    For csrec In cs (aOldEs.ESERCIZIO,aOldEs.CD_CENTRO_RESPONSABILITA)
    Loop
       insert into PDG_MODULO_SPESE_S (
         pg_storico_
        ,ds_storico_
        ,ESERCIZIO
        ,CD_CENTRO_RESPONSABILITA
        ,PG_PROGETTO
        ,ID_CLASSIFICAZIONE
        ,CD_CDS_AREA
        ,cd_cofog
        ,cd_missione
        ,PG_DETTAGLIO
        ,IM_SPESE_GEST_DECENTRATA_INT
        ,IM_SPESE_GEST_DECENTRATA_EST
        ,IM_SPESE_GEST_ACCENTRATA_INT
        ,IM_SPESE_GEST_ACCENTRATA_EST
        ,IM_SPESE_A2
        ,IM_SPESE_A3
        ,UTCR
        ,DACR
        ,UTUV
        ,DUVA
        ,PG_VER_REC
       ) values (
        aOldEs.PG_VER_REC
        ,sSysdate
        ,csrec.ESERCIZIO
        ,csrec.CD_CENTRO_RESPONSABILITA
        ,csrec.PG_PROGETTO
        ,csrec.ID_CLASSIFICAZIONE
        ,csrec.CD_CDS_AREA
        ,csrec.CD_COFOG
        ,csrec.CD_missione
        ,csrec.pg_dettaglio
        ,csrec.IM_SPESE_GEST_DECENTRATA_INT
        ,csrec.IM_SPESE_GEST_DECENTRATA_EST
        ,csrec.IM_SPESE_GEST_ACCENTRATA_INT
        ,csrec.IM_SPESE_GEST_ACCENTRATA_EST
        ,csrec.IM_SPESE_A2
        ,csrec.IM_SPESE_A3
        ,csrec.UTCR
        ,csrec.DACR
        ,csrec.UTUV
        ,csrec.DUVA
        ,csrec.PG_VER_REC
        );
    End Loop;

    For ccrec In cc (aOldEs.ESERCIZIO,aOldEs.CD_CENTRO_RESPONSABILITA)
    Loop
       insert into PDG_MODULO_COSTI_S (
         pg_storico_
        ,ds_storico_
        ,ESERCIZIO
        ,CD_CENTRO_RESPONSABILITA
        ,PG_PROGETTO
        ,RIS_ES_PREC_TIT_I
        ,RIS_ES_PREC_TIT_II
        ,RIS_PRES_ES_PREC_TIT_I
        ,RIS_PRES_ES_PREC_TIT_II
        ,IM_COSTI_GENERALI
        ,IM_CF_TFR
        ,IM_CF_AMM_IMMOBILI
        ,IM_CF_AMM_ATTREZZ
        ,IM_CF_AMM_ALTRO
        ,UTCR
        ,DACR
        ,UTUV
        ,DUVA
        ,PG_VER_REC
        ,IM_CF_TFR_DET
       ) values (
        aOldEs.PG_VER_REC
        ,sSysdate
        ,ccrec.ESERCIZIO
        ,ccrec.CD_CENTRO_RESPONSABILITA
        ,ccrec.PG_PROGETTO
        ,ccrec.RIS_ES_PREC_TIT_I
        ,ccrec.RIS_ES_PREC_TIT_II
        ,ccrec.RIS_PRES_ES_PREC_TIT_I
        ,ccrec.RIS_PRES_ES_PREC_TIT_II
        ,ccrec.IM_COSTI_GENERALI
        ,ccrec.IM_CF_TFR
        ,ccrec.IM_CF_AMM_IMMOBILI
        ,ccrec.IM_CF_AMM_ATTREZZ
        ,ccrec.IM_CF_AMM_ALTRO
        ,ccrec.UTCR
        ,ccrec.DACR
        ,ccrec.UTUV
        ,ccrec.DUVA
        ,ccrec.PG_VER_REC
        ,ccrec.IM_CF_TFR_DET
        );
    End Loop;

    For cerec In ce (aOldEs.ESERCIZIO,aOldEs.CD_CENTRO_RESPONSABILITA)
    Loop
       insert into PDG_MODULO_ENTRATE_S (
         pg_storico_
        ,ds_storico_
        ,ESERCIZIO
        ,CD_CENTRO_RESPONSABILITA
        ,PG_PROGETTO
        ,CDR_LINEA
        ,CD_LINEA_ATTIVITA
        ,CD_NATURA
        ,ID_CLASSIFICAZIONE
        ,PG_DETTAGLIO
        ,CD_TERZO
        ,DS_DETTAGLIO
        ,IM_ENTRATA_TOT
        ,ESERCIZIO_INIZIO
        ,ESERCIZIO_FINE
        ,IM_ENTRATA
        ,IM_ENTRATA_APP
        ,IM_ENTRATA_A2
        ,IM_ENTRATA_A3
        ,IM_SPESE_VIVE
        ,DS_SPESE_VIVE
        ,UTCR
        ,DACR
        ,UTUV
        ,DUVA
        ,PG_VER_REC
        ,CD_CDS_AREA
       ) values (
        aOldEs.PG_VER_REC
        ,sSysdate
        ,cerec.ESERCIZIO
        ,cerec.CD_CENTRO_RESPONSABILITA
        ,cerec.PG_PROGETTO
        ,cerec.CDR_LINEA
        ,cerec.CD_LINEA_ATTIVITA
        ,cerec.CD_NATURA
        ,cerec.ID_CLASSIFICAZIONE
        ,cerec.PG_DETTAGLIO
        ,cerec.CD_TERZO
        ,cerec.DS_DETTAGLIO
        ,cerec.IM_ENTRATA_TOT
        ,cerec.ESERCIZIO_INIZIO
        ,cerec.ESERCIZIO_FINE
        ,cerec.IM_ENTRATA
        ,cerec.IM_ENTRATA_APP
        ,cerec.IM_ENTRATA_A2
        ,cerec.IM_ENTRATA_A3
        ,cerec.IM_SPESE_VIVE
        ,cerec.DS_SPESE_VIVE
        ,cerec.UTCR
        ,cerec.DACR
        ,cerec.UTUV
        ,cerec.DUVA
        ,cerec.PG_VER_REC
        ,cerec.CD_CDS_AREA
        );
    End Loop;
 End;
End;
