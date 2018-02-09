CREATE OR REPLACE TRIGGER BCRUD_SALDI
INSTEAD OF Delete  OR Insert Or Update
ON VOCE_F_SALDI_CDR_LINEA REFERENCING NEW AS NEW OLD AS OLD
FOR EACH ROW
Begin
  If inserting Then
    Insert Into Saldi_stanziamenti values(:new.ESERCIZIO,
                                          :new.ESERCIZIO_RES,
                                          :new.CD_CENTRO_RESPONSABILITA,
                                          :new.CD_LINEA_ATTIVITA,
                                          :new.TI_APPARTENENZA,
                                          :new.TI_GESTIONE,
                                          :new.CD_VOCE,
                                          :new.CD_ELEMENTO_VOCE,
                                          :new.IM_STANZ_INIZIALE_A1,
                                          :new.IM_STANZ_INIZIALE_A2,
                                          :new.IM_STANZ_INIZIALE_A3,
                                          :new.VARIAZIONI_PIU,
                                          :new.VARIAZIONI_MENO,
                                          :new.IM_STANZ_INIZIALE_CASSA,
                                          :new.VARIAZIONI_PIU_CASSA,
                                          :new.VARIAZIONI_MENO_CASSA,
                                          :new.IM_STANZ_RES_IMPROPRIO,
                                          :new.VAR_PIU_STANZ_RES_IMP,
                                          :new.VAR_MENO_STANZ_RES_IMP,
                                          :new.DACR,
                                          :new.UTCR,
                                          :new.DUVA,
                                          :new.UTUV,
                                          :new.PG_VER_REC);
    Insert Into SALDI_IMP_ACC values(:new.ESERCIZIO,
                                     :new.ESERCIZIO_RES,
                                     :new.CD_CENTRO_RESPONSABILITA,
                                     :new.CD_LINEA_ATTIVITA,
                                     :new.TI_APPARTENENZA,
                                     :new.TI_GESTIONE,
                                     :new.CD_VOCE,
                                     :new.CD_ELEMENTO_VOCE,
                                     :new.IM_OBBL_ACC_COMP,
                                     :new.IM_OBBL_RES_IMP,
                                     :new.VAR_PIU_OBBL_RES_IMP,
                                     :new.VAR_MENO_OBBL_RES_IMP,
                                     :new.IM_OBBL_RES_PRO,
                                     :new.VAR_PIU_OBBL_RES_PRO,
                                     :new.VAR_MENO_OBBL_RES_PRO,
                                     :new.DACR,
                                     :new.UTCR,
                                     :new.DUVA,
                                     :new.UTUV,
                                     :new.PG_VER_REC);
    Insert Into SALDI_PAG_INC values(:new.ESERCIZIO,
                                     :new.ESERCIZIO_RES,
                                     :new.CD_CENTRO_RESPONSABILITA,
                                     :new.CD_LINEA_ATTIVITA,
                                     :new.TI_APPARTENENZA,
                                     :new.TI_GESTIONE,
                                     :new.CD_VOCE,
                                     :new.CD_ELEMENTO_VOCE,
                                     :new.IM_MANDATI_REVERSALI_PRO,
                                     :new.IM_MANDATI_REVERSALI_IMP,
                                     :new.IM_PAGAMENTI_INCASSI,
                                     :new.DACR,
                                     :new.UTCR,
                                     :new.DUVA,
                                     :new.UTUV,
                                     :new.PG_VER_REC);
  Elsif deleting Then
    Delete SALDI_STANZIAMENTI
    Where ESERCIZIO = :old.ESERCIZIO
      And ESERCIZIO_RES = :old.ESERCIZIO_RES
      And CD_CENTRO_RESPONSABILITA = :old.CD_CENTRO_RESPONSABILITA
      And CD_LINEA_ATTIVITA = :old.CD_LINEA_ATTIVITA
      And TI_APPARTENENZA = :old.TI_APPARTENENZA
      And TI_GESTIONE = :old.TI_GESTIONE
      And CD_VOCE = :old.CD_VOCE;

    Delete SALDI_IMP_ACC
    Where ESERCIZIO = :old.ESERCIZIO
      And ESERCIZIO_RES = :old.ESERCIZIO_RES
      And CD_CENTRO_RESPONSABILITA = :old.CD_CENTRO_RESPONSABILITA
      And CD_LINEA_ATTIVITA = :old.CD_LINEA_ATTIVITA
      And TI_APPARTENENZA = :old.TI_APPARTENENZA
      And TI_GESTIONE = :old.TI_GESTIONE
      And CD_VOCE = :old.CD_VOCE;

    Delete SALDI_PAG_INC
    Where ESERCIZIO = :old.ESERCIZIO
      And ESERCIZIO_RES = :old.ESERCIZIO_RES
      And CD_CENTRO_RESPONSABILITA = :old.CD_CENTRO_RESPONSABILITA
      And CD_LINEA_ATTIVITA = :old.CD_LINEA_ATTIVITA
      And TI_APPARTENENZA = :old.TI_APPARTENENZA
      And TI_GESTIONE = :old.TI_GESTIONE
      And CD_VOCE = :old.CD_VOCE;
  Elsif updating Then
    If (:new.IM_STANZ_INIZIALE_A1 != :old.IM_STANZ_INIZIALE_A1 Or
       :new.IM_STANZ_INIZIALE_A2 != :old.IM_STANZ_INIZIALE_A2 Or
       :new.IM_STANZ_INIZIALE_A3 != :old.IM_STANZ_INIZIALE_A3 Or
       :new.VARIAZIONI_PIU != :old.VARIAZIONI_PIU Or
       :new.VARIAZIONI_MENO != :old.VARIAZIONI_MENO Or
       :new.IM_STANZ_INIZIALE_CASSA != :old.IM_STANZ_INIZIALE_CASSA Or
       :new.VARIAZIONI_PIU_CASSA != :old.VARIAZIONI_PIU_CASSA Or
       :new.VARIAZIONI_MENO_CASSA != :old.VARIAZIONI_MENO_CASSA Or
       :new.IM_STANZ_RES_IMPROPRIO != :old.IM_STANZ_RES_IMPROPRIO Or
       :new.VAR_PIU_STANZ_RES_IMP != :old.VAR_PIU_STANZ_RES_IMP Or
       :new.VAR_MENO_STANZ_RES_IMP != :old.VAR_MENO_STANZ_RES_IMP) Then

      Update SALDI_STANZIAMENTI Set
        IM_STANZ_INIZIALE_A1 = :New.IM_STANZ_INIZIALE_A1,
        IM_STANZ_INIZIALE_A2 = :New.IM_STANZ_INIZIALE_A2,
        IM_STANZ_INIZIALE_A3 = :New.IM_STANZ_INIZIALE_A3,
        VARIAZIONI_PIU = :New.VARIAZIONI_PIU,
        VARIAZIONI_MENO = :New.VARIAZIONI_MENO,
        IM_STANZ_INIZIALE_CASSA = :New.IM_STANZ_INIZIALE_CASSA,
        VARIAZIONI_PIU_CASSA = :New.VARIAZIONI_PIU_CASSA,
        VARIAZIONI_MENO_CASSA = :New.VARIAZIONI_MENO_CASSA,
        IM_STANZ_RES_IMPROPRIO = :New.IM_STANZ_RES_IMPROPRIO,
        VAR_PIU_STANZ_RES_IMP = :New.VAR_PIU_STANZ_RES_IMP,
        VAR_MENO_STANZ_RES_IMP = :New.VAR_MENO_STANZ_RES_IMP,
        DACR = :new.DACR,
        UTCR = :new.UTCR,
        DUVA = :new.DUVA,
        UTUV = :new.UTUV,
        PG_VER_REC = :new.PG_VER_REC
      Where ESERCIZIO = :old.ESERCIZIO
      And ESERCIZIO_RES = :old.ESERCIZIO_RES
      And CD_CENTRO_RESPONSABILITA = :old.CD_CENTRO_RESPONSABILITA
      And CD_LINEA_ATTIVITA = :old.CD_LINEA_ATTIVITA
      And TI_APPARTENENZA = :old.TI_APPARTENENZA
      And TI_GESTIONE = :old.TI_GESTIONE
      And CD_VOCE = :old.CD_VOCE;
    End If;
    If (:New.IM_OBBL_ACC_COMP           != :Old.IM_OBBL_ACC_COMP            Or
        :New.IM_OBBL_RES_IMP            != :Old.IM_OBBL_RES_IMP             Or
        :New.VAR_PIU_OBBL_RES_IMP       != :Old.VAR_PIU_OBBL_RES_IMP        Or
        :New.VAR_MENO_OBBL_RES_IMP      != :Old.VAR_MENO_OBBL_RES_IMP       Or
        :New.IM_OBBL_RES_PRO            != :Old.IM_OBBL_RES_PRO             Or
        :New.VAR_PIU_OBBL_RES_PRO       != :Old.VAR_PIU_OBBL_RES_PRO        Or
        :New.VAR_MENO_OBBL_RES_PRO      != :Old.VAR_MENO_OBBL_RES_PRO) Then
      Update SALDI_IMP_ACC Set
        IM_OBBL_ACC_COMP           = :New.IM_OBBL_ACC_COMP            ,
        IM_OBBL_RES_IMP            = :New.IM_OBBL_RES_IMP             ,
        VAR_PIU_OBBL_RES_IMP       = :New.VAR_PIU_OBBL_RES_IMP        ,
        VAR_MENO_OBBL_RES_IMP      = :New.VAR_MENO_OBBL_RES_IMP       ,
        IM_OBBL_RES_PRO            = :New.IM_OBBL_RES_PRO             ,
        VAR_PIU_OBBL_RES_PRO       = :New.VAR_PIU_OBBL_RES_PRO        ,
        VAR_MENO_OBBL_RES_PRO      = :New.VAR_MENO_OBBL_RES_PRO,
        DACR = :new.DACR,
        UTCR = :new.UTCR,
        DUVA = :new.DUVA,
        UTUV = :new.UTUV,
        PG_VER_REC = :new.PG_VER_REC
      Where ESERCIZIO = :old.ESERCIZIO
      And ESERCIZIO_RES = :old.ESERCIZIO_RES
      And CD_CENTRO_RESPONSABILITA = :old.CD_CENTRO_RESPONSABILITA
      And CD_LINEA_ATTIVITA = :old.CD_LINEA_ATTIVITA
      And TI_APPARTENENZA = :old.TI_APPARTENENZA
      And TI_GESTIONE = :old.TI_GESTIONE
      And CD_VOCE = :old.CD_VOCE;
    End If;
    If (:new.IM_MANDATI_REVERSALI_PRO != :Old.IM_MANDATI_REVERSALI_PRO  Or
        :new.IM_MANDATI_REVERSALI_IMP != :Old.IM_MANDATI_REVERSALI_IMP  Or
        :new.IM_PAGAMENTI_INCASSI     != :Old.IM_PAGAMENTI_INCASSI) Then
      Update SALDI_PAG_INC Set
        IM_MANDATI_REVERSALI_PRO = :New.IM_MANDATI_REVERSALI_PRO,
        IM_MANDATI_REVERSALI_IMP = :New.IM_MANDATI_REVERSALI_IMP,
        IM_PAGAMENTI_INCASSI     = :New.IM_PAGAMENTI_INCASSI,
        DACR = :new.DACR,
        UTCR = :new.UTCR,
        DUVA = :new.DUVA,
        UTUV = :new.UTUV,
        PG_VER_REC = :new.PG_VER_REC
      Where ESERCIZIO = :old.ESERCIZIO
      And ESERCIZIO_RES = :old.ESERCIZIO_RES
      And CD_CENTRO_RESPONSABILITA = :old.CD_CENTRO_RESPONSABILITA
      And CD_LINEA_ATTIVITA = :old.CD_LINEA_ATTIVITA
      And TI_APPARTENENZA = :old.TI_APPARTENENZA
      And TI_GESTIONE = :old.TI_GESTIONE
      And CD_VOCE = :old.CD_VOCE;
    End If;
  End If;
End;
/


