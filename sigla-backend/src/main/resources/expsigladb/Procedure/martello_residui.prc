CREATE OR REPLACE procedure martello_residui (aEs NUMBER, aCDS VARCHAR2, UPD_YN CHAR) Is

ADELTASALDO VOCE_F_SALDI_CDR_LINEA%Rowtype;

calcolato       number;
saldo           number;

Begin

For INCDS In (Select Distinct CD_CDS
              From PARAMETRI_CDS
              Where ESERCIZIO = AES And
                    CD_CDS = Nvl(ACDS, CD_CDS) And
                    FL_RIBALTATO = 'Y') Loop

For disp_imp_da_ribaltare In (Select ESERCIZIO, ESERCIZIO_RES, CD_CENTRO_RESPONSABILITA,
                                     CD_LINEA_ATTIVITA, TI_APPARTENENZA, TI_GESTIONE, CD_VOCE,
                                     Sum(TOT_IM_RESIDUI_RICOSTRUITI) TOT_IM_RESIDUI_RICOSTRUITI,
                                     Sum(TOT_IM_STANZ_INIZIALE_A1)   TOT_IM_STANZ_INIZIALE_A1,
                                     sum(TOT_VARIAZIONI_PIU)         TOT_VARIAZIONI_PIU,
                                     sum(TOT_VARIAZIONI_MENO)        TOT_VARIAZIONI_MENO,
                                     sum(TOT_IM_OBBL_ACC_COMP)       TOT_IM_OBBL_ACC_COMP,
                                     sum(TOT_IM_STANZ_RES_IMPROPRIO) TOT_IM_STANZ_RES_IMPROPRIO,
                                     sum(TOT_VAR_PIU_STANZ_RES_IMP)  TOT_VAR_PIU_STANZ_RES_IMP,
                                     sum(TOT_VAR_MENO_STANZ_RES_IMP) TOT_VAR_MENO_STANZ_RES_IMP,
                                     sum(TOT_IM_OBBL_RES_IMP)        TOT_IM_OBBL_RES_IMP,
                                     sum(TOT_VAR_PIU_OBBL_RES_IMP)   TOT_VAR_PIU_OBBL_RES_IMP,
                                     sum(TOT_VAR_MENO_OBBL_RES_IMP)  TOT_VAR_MENO_OBBL_RES_IMP
                               From  v_disp_res_improprie
                               Where esercizio = aEs And
                                     CNRUTL001.GETCDSFROMCDR(CD_CENTRO_RESPONSABILITA) = INCDS.CD_CDS And
                                     TI_GESTIONE = 'S'
                               Group By ESERCIZIO, ESERCIZIO_RES, CD_CENTRO_RESPONSABILITA,
                                      CD_LINEA_ATTIVITA, TI_APPARTENENZA, TI_GESTIONE, CD_VOCE) Loop

If disp_imp_da_ribaltare.ESERCIZIO = disp_imp_da_ribaltare.ESERCIZIO_RES Then

-- COMPETENZA

Begin
select IM_STANZ_RES_IMPROPRIO
into   saldo
from   voce_f_saldi_cdr_linea
where  ESERCIZIO = aEs + 1 and
       ESERCIZIO_RES = disp_imp_da_ribaltare.ESERCIZIO_RES and
       CD_CENTRO_RESPONSABILITA = disp_imp_da_ribaltare.CD_CENTRO_RESPONSABILITA and
       CD_LINEA_ATTIVITA = disp_imp_da_ribaltare.CD_LINEA_ATTIVITA and
       TI_APPARTENENZA = disp_imp_da_ribaltare.TI_APPARTENENZA and
       TI_GESTIONE = disp_imp_da_ribaltare.TI_GESTIONE and
       CD_VOCE = disp_imp_da_ribaltare.CD_VOCE;
Exception
 When No_Data_Found Then
    saldo := 0;
End;


if Saldo != ((disp_imp_da_ribaltare.TOT_IM_RESIDUI_RICOSTRUITI +
              disp_imp_da_ribaltare.tot_IM_STANZ_INIZIALE_A1 + disp_imp_da_ribaltare.tot_VARIAZIONI_PIU -
              disp_imp_da_ribaltare.tot_VARIAZIONI_MENO) - (disp_imp_da_ribaltare.tot_IM_OBBL_ACC_COMP)) then


Dbms_Output.put_line (aEs + 1||'/'||disp_imp_da_ribaltare.ESERCIZIO_RES||'/'||
disp_imp_da_ribaltare.CD_CENTRO_RESPONSABILITA||'/'||
disp_imp_da_ribaltare.CD_LINEA_ATTIVITA||'/'||
disp_imp_da_ribaltare.TI_APPARTENENZA||'/'||
disp_imp_da_ribaltare.TI_GESTIONE||'/'||
disp_imp_da_ribaltare.CD_VOCE||' Calc. '||((disp_imp_da_ribaltare.TOT_IM_RESIDUI_RICOSTRUITI +
              disp_imp_da_ribaltare.tot_IM_STANZ_INIZIALE_A1 + disp_imp_da_ribaltare.tot_VARIAZIONI_PIU -
              disp_imp_da_ribaltare.tot_VARIAZIONI_MENO) - (disp_imp_da_ribaltare.tot_IM_OBBL_ACC_COMP))||
' Saldo '||saldo);

If UPD_YN = 'Y' Then
Update VOCE_F_SALDI_CDR_LINEA
Set    IM_STANZ_RES_IMPROPRIO = ((disp_imp_da_ribaltare.TOT_IM_RESIDUI_RICOSTRUITI +
              disp_imp_da_ribaltare.tot_IM_STANZ_INIZIALE_A1 + disp_imp_da_ribaltare.tot_VARIAZIONI_PIU -
              disp_imp_da_ribaltare.tot_VARIAZIONI_MENO) - (disp_imp_da_ribaltare.tot_IM_OBBL_ACC_COMP))
Where  ESERCIZIO = aEs + 1 and
       ESERCIZIO_RES = disp_imp_da_ribaltare.ESERCIZIO_RES and
       CD_CENTRO_RESPONSABILITA = disp_imp_da_ribaltare.CD_CENTRO_RESPONSABILITA and
       CD_LINEA_ATTIVITA = disp_imp_da_ribaltare.CD_LINEA_ATTIVITA and
       TI_APPARTENENZA = disp_imp_da_ribaltare.TI_APPARTENENZA and
       TI_GESTIONE = disp_imp_da_ribaltare.TI_GESTIONE and
       CD_VOCE = disp_imp_da_ribaltare.CD_VOCE;
End If;

End if;

Elsif disp_imp_da_ribaltare.ESERCIZIO > disp_imp_da_ribaltare.ESERCIZIO_RES Then

-- RESIDUI

Begin
select IM_STANZ_RES_IMPROPRIO
into   saldo
from   voce_f_saldi_cdr_linea
where  ESERCIZIO = aEs + 1 and
       ESERCIZIO_RES = disp_imp_da_ribaltare.ESERCIZIO_RES and
       CD_CENTRO_RESPONSABILITA = disp_imp_da_ribaltare.CD_CENTRO_RESPONSABILITA and
       CD_LINEA_ATTIVITA = disp_imp_da_ribaltare.CD_LINEA_ATTIVITA and
       TI_APPARTENENZA = disp_imp_da_ribaltare.TI_APPARTENENZA and
       TI_GESTIONE = disp_imp_da_ribaltare.TI_GESTIONE and
       CD_VOCE = disp_imp_da_ribaltare.CD_VOCE;
Exception
 When No_Data_Found Then
    saldo := 0;
End;

if Saldo != ((disp_imp_da_ribaltare.tot_IM_STANZ_RES_IMPROPRIO + disp_imp_da_ribaltare.tot_VAR_PIU_STANZ_RES_IMP -
         disp_imp_da_ribaltare.tot_VAR_MENO_STANZ_RES_IMP) -
        (disp_imp_da_ribaltare.tot_IM_OBBL_RES_IMP + disp_imp_da_ribaltare.tot_VAR_PIU_OBBL_RES_IMP -
         disp_imp_da_ribaltare.tot_VAR_MENO_OBBL_RES_IMP)) then

dbms_output.put_line ('errore 2');

End If;

end if;

End Loop;

End Loop;

End;
/


