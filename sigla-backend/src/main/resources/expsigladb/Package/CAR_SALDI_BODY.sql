--------------------------------------------------------
--  DDL for Package Body CAR_SALDI
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "CAR_SALDI" is

Procedure creasaldi(aDeltaSaldo voce_f_saldi_cdr_linea%Rowtype) Is

aOldSaldo voce_f_saldi_cdr_linea%Rowtype;

Begin

If aDeltaSaldo.ESERCIZIO Is Null Or
   aDeltaSaldo.ESERCIZIO_RES Is Null Or
   aDeltaSaldo.CD_CENTRO_RESPONSABILITA Is Null Or
   aDeltaSaldo.CD_LINEA_ATTIVITA Is Null Or
   aDeltaSaldo.TI_APPARTENENZA Is Null Or
   aDeltaSaldo.TI_GESTIONE Is Null Or
   aDeltaSaldo.CD_VOCE Is Null Or
   aDeltaSaldo.UTUV Is Null Then
     IBMERR001.RAISE_ERR_GENERICO('Impossibile aggiornare i Saldi per CdR/Voce/Linea, passaggio parametri mancanti !');
End If;

/*
If IM_STANZ_INIZIALE_A1 Is Not Null Then
Elsif IM_STANZ_INIZIALE_A2 Is Not Null Then
Elsif IM_STANZ_INIZIALE_A3 Is Not Null Then
Elsif VARIAZIONI_PIU Is Not Null Then
Elsif VARIAZIONI_MENO Is Not Null Then
Elsif IM_STANZ_INIZIALE_CASSA Is Not Null Then
Elsif VARIAZIONI_PIU_CASSA Is Not Null Then
Elsif VARIAZIONI_MENO_CASSA Is Not Null Then
Elsif IM_OBBL_ACC_COMP Is Not Null Then
Elsif IM_STANZ_RES_IMPROPRIO Is Not Null Then
Elsif VAR_PIU_STANZ_RES_IMP Is Not Null Then
Elsif VAR_MENO_STANZ_RES_IMP Is Not Null Then
Elsif IM_OBBL_RES_IMP Is Not Null Then
Elsif VAR_PIU_OBBL_RES_IMP Is Not Null Then
Elsif VAR_MENO_OBBL_RES_IMP Is Not Null Then
Elsif IM_OBBL_RES_PRO Is Not Null Then
Elsif VAR_PIU_OBBL_RES_PRO Is Not Null Then
Elsif VAR_MENO_OBBL_RES_PRO Is Not Null Then
Elsif IM_MANDATI_REVERSALI_PRO Is Not Null Then
Elsif IM_MANDATI_REVERSALI_IMP Is Not Null Then
Elsif IM_PAGAMENTI_INCASSI Is Not Null Then
End If;
*/


Select *
Into  aOldSaldo
From  voce_f_saldi_cdr_linea
Where ESERCIZIO                = aDeltaSaldo.ESERCIZIO And
      ESERCIZIO_RES            = aDeltaSaldo.ESERCIZIO_RES And
      CD_CENTRO_RESPONSABILITA = aDeltaSaldo.CD_CENTRO_RESPONSABILITA And
      CD_LINEA_ATTIVITA        = aDeltaSaldo.CD_LINEA_ATTIVITA And
      TI_APPARTENENZA          = aDeltaSaldo.TI_APPARTENENZA And
      TI_GESTIONE              = aDeltaSaldo.TI_GESTIONE And
      CD_VOCE                  = aDeltaSaldo.CD_VOCE
For Update;

Update voce_f_saldi_cdr_linea
Set    IM_STANZ_INIZIALE_A1     = nvl(aOldsaldo.IM_STANZ_INIZIALE_A1, 0) + nvl(aDeltasaldo.IM_STANZ_INIZIALE_A1, 0),
       IM_STANZ_INIZIALE_A2     = nvl(aOldsaldo.IM_STANZ_INIZIALE_A2, 0) + nvl(aDeltasaldo.IM_STANZ_INIZIALE_A2, 0),
       IM_STANZ_INIZIALE_A3     = nvl(aOldsaldo.IM_STANZ_INIZIALE_A3, 0) + nvl(aDeltasaldo.IM_STANZ_INIZIALE_A3, 0),
       VARIAZIONI_PIU           = nvl(aOldsaldo.VARIAZIONI_PIU, 0) + nvl(aDeltasaldo.VARIAZIONI_PIU, 0),
       VARIAZIONI_MENO          = nvl(aOldsaldo.VARIAZIONI_MENO, 0) + nvl(aDeltasaldo.VARIAZIONI_MENO, 0),
       IM_STANZ_INIZIALE_CASSA  = nvl(aOldsaldo.IM_STANZ_INIZIALE_CASSA, 0) + nvl(aDeltasaldo.IM_STANZ_INIZIALE_CASSA, 0),
       VARIAZIONI_PIU_CASSA     = nvl(aOldsaldo.VARIAZIONI_PIU_CASSA, 0) + nvl(aDeltasaldo.VARIAZIONI_PIU_CASSA, 0),
       VARIAZIONI_MENO_CASSA    = nvl(aOldsaldo.VARIAZIONI_MENO_CASSA, 0) + nvl(aDeltasaldo.VARIAZIONI_MENO_CASSA, 0),
       IM_OBBL_ACC_COMP         = nvl(aOldsaldo.IM_OBBL_ACC_COMP, 0) + nvl(aDeltasaldo.IM_OBBL_ACC_COMP, 0),
       IM_STANZ_RES_IMPROPRIO   = nvl(aOldsaldo.IM_STANZ_RES_IMPROPRIO, 0) + nvl(aDeltasaldo.IM_STANZ_RES_IMPROPRIO, 0),
       VAR_PIU_STANZ_RES_IMP    = nvl(aOldsaldo.VAR_PIU_STANZ_RES_IMP, 0) + nvl(aDeltasaldo.VAR_PIU_STANZ_RES_IMP, 0),
       VAR_MENO_STANZ_RES_IMP   = nvl(aOldsaldo.VAR_MENO_STANZ_RES_IMP, 0) + nvl(aDeltasaldo.VAR_MENO_STANZ_RES_IMP, 0),
       IM_OBBL_RES_IMP          = nvl(aOldsaldo.IM_OBBL_RES_IMP, 0) + nvl(aDeltasaldo.IM_OBBL_RES_IMP, 0),
       VAR_PIU_OBBL_RES_IMP     = nvl(aOldsaldo.VAR_PIU_OBBL_RES_IMP, 0) + nvl(aDeltasaldo.VAR_PIU_OBBL_RES_IMP, 0),
       VAR_MENO_OBBL_RES_IMP    = nvl(aOldsaldo.VAR_MENO_OBBL_RES_IMP, 0) + nvl(aDeltasaldo.VAR_MENO_OBBL_RES_IMP, 0),
       IM_OBBL_RES_PRO          = nvl(aOldsaldo.IM_OBBL_RES_PRO, 0) + nvl(aDeltasaldo.IM_OBBL_RES_PRO, 0),
       VAR_PIU_OBBL_RES_PRO     = nvl(aOldsaldo.VAR_PIU_OBBL_RES_PRO, 0) + nvl(aDeltasaldo.VAR_PIU_OBBL_RES_PRO, 0),
       VAR_MENO_OBBL_RES_PRO    = nvl(aOldsaldo.VAR_MENO_OBBL_RES_PRO, 0) + nvl(aDeltasaldo.VAR_MENO_OBBL_RES_PRO, 0),
       IM_MANDATI_REVERSALI_PRO = nvl(aOldsaldo.IM_MANDATI_REVERSALI_PRO, 0) + nvl(aDeltasaldo.IM_MANDATI_REVERSALI_PRO, 0),
       IM_MANDATI_REVERSALI_IMP = nvl(aOldsaldo.IM_MANDATI_REVERSALI_IMP, 0) + nvl(aDeltasaldo.IM_MANDATI_REVERSALI_IMP, 0),
       IM_PAGAMENTI_INCASSI     = nvl(aOldsaldo.IM_PAGAMENTI_INCASSI, 0) + nvl(aDeltasaldo.IM_PAGAMENTI_INCASSI, 0),
       DUVA                     = Sysdate,
       UTUV                     = aDeltaSaldo.utuv
Where ESERCIZIO                = aOldSaldo.ESERCIZIO                and
      ESERCIZIO_RES            = aOldSaldo.ESERCIZIO_RES            and
      CD_CENTRO_RESPONSABILITA = aOldSaldo.CD_CENTRO_RESPONSABILITA and
      CD_LINEA_ATTIVITA        = aOldSaldo.CD_LINEA_ATTIVITA        and
      TI_APPARTENENZA          = aOldSaldo.TI_APPARTENENZA          and
      TI_GESTIONE              = aOldSaldo.TI_GESTIONE              and
      CD_VOCE                  = aOldSaldo.CD_VOCE;

Exception

When No_Data_Found Then

Insert Into Voce_f_saldi_cdr_linea
(ESERCIZIO,
 ESERCIZIO_RES,
 CD_CENTRO_RESPONSABILITA,
 CD_LINEA_ATTIVITA,
 TI_APPARTENENZA,
 TI_GESTIONE,
 CD_VOCE,
 IM_STANZ_INIZIALE_A1,
 IM_STANZ_INIZIALE_A2,
 IM_STANZ_INIZIALE_A3,
 VARIAZIONI_PIU,
 VARIAZIONI_MENO,
 IM_STANZ_INIZIALE_CASSA,
 VARIAZIONI_PIU_CASSA,
 VARIAZIONI_MENO_CASSA,
 IM_OBBL_ACC_COMP,
 IM_STANZ_RES_IMPROPRIO,
 VAR_PIU_STANZ_RES_IMP,
 VAR_MENO_STANZ_RES_IMP,
 IM_OBBL_RES_IMP,
 VAR_PIU_OBBL_RES_IMP,
 VAR_MENO_OBBL_RES_IMP,
 IM_OBBL_RES_PRO,
 VAR_PIU_OBBL_RES_PRO,
 VAR_MENO_OBBL_RES_PRO,
 IM_MANDATI_REVERSALI_PRO,
 IM_MANDATI_REVERSALI_IMP,
 IM_PAGAMENTI_INCASSI,
 DACR,
 UTCR,
 DUVA,
 UTUV,
 PG_VER_REC)
Values
(aDeltaSaldo.ESERCIZIO,
 aDeltaSaldo.ESERCIZIO_RES,
 aDeltaSaldo.CD_CENTRO_RESPONSABILITA,
 aDeltaSaldo.CD_LINEA_ATTIVITA,
 aDeltaSaldo.TI_APPARTENENZA,
 aDeltaSaldo.TI_GESTIONE,
 aDeltaSaldo.CD_VOCE,
 nvl(aDeltaSaldo.IM_STANZ_INIZIALE_A1, 0),
 nvl(aDeltaSaldo.IM_STANZ_INIZIALE_A2, 0),
 nvl(aDeltaSaldo.IM_STANZ_INIZIALE_A3, 0),
 nvl(aDeltaSaldo.VARIAZIONI_PIU, 0),
 nvl(aDeltaSaldo.VARIAZIONI_MENO, 0),
 nvl(aDeltaSaldo.IM_STANZ_INIZIALE_CASSA, 0),
 nvl(aDeltaSaldo.VARIAZIONI_PIU_CASSA, 0),
 nvl(aDeltaSaldo.VARIAZIONI_MENO_CASSA, 0),
 nvl(aDeltaSaldo.IM_OBBL_ACC_COMP, 0),
 nvl(aDeltaSaldo.IM_STANZ_RES_IMPROPRIO, 0),
 nvl(aDeltaSaldo.VAR_PIU_STANZ_RES_IMP, 0),
 nvl(aDeltaSaldo.VAR_MENO_STANZ_RES_IMP, 0),
 nvl(aDeltaSaldo.IM_OBBL_RES_IMP, 0),
 nvl(aDeltaSaldo.VAR_PIU_OBBL_RES_IMP, 0),
 nvl(aDeltaSaldo.VAR_MENO_OBBL_RES_IMP, 0),
 nvl(aDeltaSaldo.IM_OBBL_RES_PRO, 0),
 nvl(aDeltaSaldo.VAR_PIU_OBBL_RES_PRO, 0),
 nvl(aDeltaSaldo.VAR_MENO_OBBL_RES_PRO, 0),
 nvl(aDeltaSaldo.IM_MANDATI_REVERSALI_PRO, 0),
 nvl(aDeltaSaldo.IM_MANDATI_REVERSALI_IMP, 0),
 nvl(aDeltaSaldo.IM_PAGAMENTI_INCASSI, 0),
 Trunc(Sysdate),
 aDeltaSaldo.UTUV, -- il creatore è lo stesso dell'ultimo che modifica
 Trunc(Sysdate),
 aDeltaSaldo.UTUV,
 1);
End;


/*
Function getVoce_FdaEV (aEV elemento_voce%Rowtype, aLinea_attivita linea_attivita%Rowtype) Return  voce_f%rowtype is

aVocef	voce_f%Rowtype;
aCDR    cdr%Rowtype;
UO_IN   unita_organizzativa%Rowtype;
CDS_IN  unita_organizzativa.cd_unita_organizzativa%Type;
cds_sac unita_organizzativa%Rowtype;

Begin

CDS_SAC := CNRCTB020.GETCDSSACVALIDO (aEv.esercizio);

Select *
Into  aCDR
From  cdr
Where cd_centro_responsabilita = aLinea_attivita.cd_centro_responsabilita;

CDS_IN := CNRUTL001.GETCDSFROMCDR(aCDR.cd_centro_responsabilita);

UO_IN := cnrctb020.getuo(aCDR);


If aEV.ti_gestione = 'S' Then -- SPESA

   If aEV.ti_appartenenza = 'C' Then -- SPESA ENTE

----- vedere logica bilancio dell'ente

      If CNRCTB020.ISCDRENTE(aCDR) Then -- spesa ente per SAC

         Select *
         Into   aVocef
         From   voce_f
         Where  esercizio = aEv.esercizio And
                ti_appartenenza = aEv.ti_appartenenza And
                ti_gestione = aEv.ti_gestione And
                cd_titolo_capitolo = aEv.cd_elemento_voce And
                cd_cds = '000' And
                cd_natura = aLinea_attivita.cd_natura And
                cd_funzione = aLinea_attivita.cd_funzione;

      Elsif Not CNRCTB020.ISCDRENTE(aCDR) Then -- spesa ente NON SAC

         Null;

      End If;

   Elsif aEV.ti_appartenenza = 'D' Then -- SPESA CDS

      If CDS_IN = CDS_SAC.cd_unita_organizzativa Then -- CDS SAC

         Select *
         Into   aVocef
         From   voce_f
         Where  esercizio = aEv.esercizio And
                ti_appartenenza = aEv.ti_appartenenza And
                ti_gestione = aEv.ti_gestione And
                cd_titolo_capitolo = aEv.cd_elemento_voce And
                cd_CENTRO_RESPONSABILITA = aLinea_attivita.cd_centro_responsabilita And
                cd_funzione = aLinea_attivita.cd_funzione;

      Elsif CDS_IN != CDS_SAC.cd_unita_organizzativa Then -- ALTRI CDS

         Select *
         Into   aVocef
         From   voce_f
         Where  esercizio = aEv.esercizio And
                ti_appartenenza = aEv.ti_appartenenza And
                ti_gestione = aEv.ti_gestione And
                cd_titolo_capitolo = aEv.cd_elemento_voce And
                cd_unita_organizzativa = UO_IN.CD_UNITA_ORGANIZZATIVA And
                cd_funzione = aLinea_attivita.cd_funzione;

      End If;

   End If;

Elsif aEV.ti_gestione = 'E' Then -- ENTRATA


   If aEV.ti_appartenenza = 'C' Then -- ENTRATA ENTE/di fatto cds

         Select *
         Into   aVocef
         From   voce_f
         Where  esercizio = aEv.esercizio And
                ti_appartenenza = aEv.ti_appartenenza And
                ti_gestione = aEv.ti_gestione And
                cd_titolo_capitolo = aEv.cd_elemento_voce And
                cd_unita_organizzativa = UO_IN.CD_UNITA_ORGANIZZATIVA;

   Elsif aEV.ti_appartenenza = 'D' Then -- ENTRATA CDS

         Null;

   End If;

End If; -- ENTRATA/SPESA

Return AVOCEF;

End;
*/

Function TIPO_VAR_PDG (aEsercizio NUMBER, aNumVar NUMBER)  Return  VARCHAR2 Is

Tipo                    VARCHAR2(20);
conta_prel              NUMBER;
CDS_PROP                unita_organizzativa.cd_unita_organizzativa%Type;
CDR_PROP                CDR%Rowtype;
CDS_DETTAGLI            unita_organizzativa.cd_unita_organizzativa%Type;
STORNO_SPESA            NUMBER;
STORNO_ENTRATA          NUMBER;
CONTA_STO_SPE_CDR       NUMBER;
CONTA_STO_SPE           NUMBER;
CONTA_STO_ETR_CDR       NUMBER;
CONTA_STO_ETR           NUMBER;
CONTA_ERRORE            NUMBER;

Begin

-- RECUPERO IL CDR PROPONENTE

Select CDR.*
Into   CDR_PROP
From   CDR, PDG_VARIAZIONE
Where  PDG_VARIAZIONE.ESERCIZIO = aESERCIZIO And
       PDG_VARIAZIONE.PG_VARIAZIONE_PDG = aNumVar And
       PDG_VARIAZIONE.CD_CENTRO_RESPONSABILITA = CDR.CD_CENTRO_RESPONSABILITA;

-- RECUPERO IL CDS DEL CDR PROPONENTE

CDS_PROP := CNRUTL001.GETCDSFROMCDR(CDR_PROP.cd_centro_responsabilita);

-- IMPOSSIBILE STABILIRE VARIAZIONE NON DEF E NON APP

Select Count(*)
Into   CONTA_ERRORE
From   PDG_VARIAZIONE
WHERE  ESERCIZIO = aESERCIZIO And
       PG_VARIAZIONE_PDG = aNumVar And
       STATO Not In ('PRD', 'APP');

If conta_ERRORE > 0 Then
  tipo := 'NO TIPO';
  Return TIPO;
End If;

-- PRELIEVO DAL FONDO

Select Count(*)
Into   conta_prel
From   PDG_VARIAZIONE
WHERE  ESERCIZIO = aESERCIZIO And
       PG_VARIAZIONE_PDG = aNumVar And
       STATO In ('PRD', 'APP') And Exists
        (Select ESERCIZIO, PG_VARIAZIONE_PDG
         From   ASS_PDG_VARIAZIONE_CDR
         Where  ESERCIZIO = aESERCIZIO And
                PG_VARIAZIONE_PDG = aNumvar
         Group By ESERCIZIO, PG_VARIAZIONE_PDG
         Having Sum(IM_ENTRATA) != Sum(IM_SPESA));

If conta_prel > 0 Then
  tipo := 'PREL_FON';
  Return TIPO;
End If;


-- STORNI DI SPESA

Select Count(*)
Into   STORNO_SPESA
From   PDG_VARIAZIONE
WHERE  ESERCIZIO = aESERCIZIO And
       PG_VARIAZIONE_PDG = aNumVar And
       STATO In ('PRD', 'APP') And Exists
                (Select ESERCIZIO, PG_VARIAZIONE_PDG
                 From   ASS_PDG_VARIAZIONE_CDR
                 Where  ESERCIZIO = aESERCIZIO And
                        PG_VARIAZIONE_PDG = aNumvar
                 Group By ESERCIZIO, PG_VARIAZIONE_PDG
                 Having Sum(IM_ENTRATA) = Sum(IM_SPESA)) And Not Exists
               (Select 1 FROM PDG_PREVENTIVO_ETR_DET
                WHERE ESERCIZIO_PDG_VARIAZIONE = PDG_VARIAZIONE.ESERCIZIO
                  AND PG_VARIAZIONE_PDG        = PDG_VARIAZIONE.PG_VARIAZIONE_PDG) And Exists
               (Select 1 FROM PDG_PREVENTIVO_SPE_DET
                WHERE ESERCIZIO_PDG_VARIAZIONE = PDG_VARIAZIONE.ESERCIZIO
                  AND PG_VARIAZIONE_PDG        = PDG_VARIAZIONE.PG_VARIAZIONE_PDG);

If STORNO_SPESA > 0 Then

   Begin
    Select Distinct CNRUTL001.GETCDSFROMCDR(cd_centro_responsabilita)
    Into   CDS_DETTAGLI
    From   PDG_PREVENTIVO_SPE_DET
    WHERE   ESERCIZIO_PDG_VARIAZIONE = aESERCIZIO
        AND PG_VARIAZIONE_PDG        = aNumvar;

    If CDS_DETTAGLI = CDS_PROP Then
           CONTA_STO_SPE_CDR := 1;
    End If;

   Exception
     When Too_Many_Rows Then
           CONTA_STO_SPE := 1;
   End;

End If;

If CONTA_STO_SPE_CDR > 0 Then
  tipo := 'STO_S_CDS';
  Return TIPO;
Elsif CONTA_STO_SPE > 0 Then
  tipo := 'STO_S_TOT';
  Return TIPO;
End If;


-- STORNI DI ENTRATA

Select Count(*)
Into   STORNO_ENTRATA
From   PDG_VARIAZIONE
WHERE  ESERCIZIO = aESERCIZIO And
       PG_VARIAZIONE_PDG = aNumVar And
       STATO In ('PRD', 'APP') And Exists
                (Select ESERCIZIO, PG_VARIAZIONE_PDG
                 From   ASS_PDG_VARIAZIONE_CDR
                 Where  ESERCIZIO = aESERCIZIO And
                        PG_VARIAZIONE_PDG = aNumvar
                 Group By ESERCIZIO, PG_VARIAZIONE_PDG
                 Having Sum(IM_ENTRATA) = Sum(IM_SPESA)) And Not Exists
               (Select 1 FROM PDG_PREVENTIVO_SPE_DET
                WHERE ESERCIZIO_PDG_VARIAZIONE = PDG_VARIAZIONE.ESERCIZIO
                  AND PG_VARIAZIONE_PDG        = PDG_VARIAZIONE.PG_VARIAZIONE_PDG) And Exists
               (Select 1 FROM PDG_PREVENTIVO_ETR_DET
                WHERE ESERCIZIO_PDG_VARIAZIONE = PDG_VARIAZIONE.ESERCIZIO
                  AND PG_VARIAZIONE_PDG        = PDG_VARIAZIONE.PG_VARIAZIONE_PDG);

If STORNO_ENTRATA > 0 Then

   Begin
    Select Distinct CNRUTL001.GETCDSFROMCDR(cd_centro_responsabilita)
    Into   CDS_DETTAGLI
    From   PDG_PREVENTIVO_ETR_DET
    WHERE   ESERCIZIO_PDG_VARIAZIONE = aESERCIZIO
        AND PG_VARIAZIONE_PDG        = aNumvar;

    If CDS_DETTAGLI = CDS_PROP Then
           CONTA_STO_ETR_CDR := 1;
    End If;

   Exception
     When Too_Many_Rows Then
           CONTA_STO_ETR := 1;
   End;

End If;

If CONTA_STO_ETR_CDR > 0 Then
  tipo := 'STO_E_CDS';
  Return TIPO;
Elsif CONTA_STO_ETR > 0 Then
  tipo := 'STO_E_TOT';
  Return TIPO;
End If;

tipo := 'VARIAZIONE';
Return TIPO;

End;

End;
