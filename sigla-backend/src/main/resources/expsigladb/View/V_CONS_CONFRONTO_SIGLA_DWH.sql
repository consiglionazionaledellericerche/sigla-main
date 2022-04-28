--------------------------------------------------------
--  DDL for View V_CONS_CONFRONTO_SIGLA_DWH
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_CONS_CONFRONTO_SIGLA_DWH" ("DT_ELABORAZIONE", "PG_ELABORAZIONE", "ESERCIZIO", "ESERCIZIO_RES", "CD_CENTRO_RESPONSABILITA", "CD_LINEA_ATTIVITA", "TI_GESTIONE", "CD_ELEMENTO_VOCE", "IM_STANZ_INIZIALE_A1_SIGLA", "IM_STANZ_INIZIALE_A1_DWH", "DIFF_IM_STANZ_INIZIALE_A1", "VARIAZIONI_PIU_SIGLA", "VARIAZIONI_PIU_DWH", "DIFF_VARIAZIONI_PIU", "VARIAZIONI_MENO_SIGLA", "VARIAZIONI_MENO_DWH", "DIFF_VARIAZIONI_MENO", "IM_OBBL_ACC_COMP_SIGLA", "IM_OBBL_ACC_COMP_DWH", "DIFF_IM_OBBL_ACC_COMP", "IM_STANZ_RES_IMPROPRIO_SIGLA", "IM_STANZ_RES_IMPROPRIO_DWH", "DIFF_IM_STANZ_RES_IMPROPRIO", "VAR_PIU_STANZ_RES_IMP_SIGLA", "VAR_PIU_STANZ_RES_IMP_DWH", "DIFF_VAR_PIU_STANZ_RES_IMP", "VAR_MENO_STANZ_RES_IMP_SIGLA", "VAR_MENO_STANZ_RES_IMP_DWH", "DIFF_VAR_MENO_STANZ_RES_IMP", "IM_OBBL_RES_IMP_SIGLA", "IM_OBBL_RES_IMP_DWH", "DIFF_IM_OBBL_RES_IMP", "IM_OBBL_RES_PRO_SIGLA", "IM_OBBL_RES_PRO_DWH", "DIFF_IM_OBBL_RES_PRO", "VAR_PIU_OBBL_RES_PRO_SIGLA", "VAR_PIU_OBBL_RES_PRO_DWH", "DIFF_VAR_PIU_OBBL_RES_PRO", "VAR_MENO_OBBL_RES_PRO_SIGLA", "VAR_MENO_OBBL_RES_PRO_DWH", "DIFF_VAR_MENO_OBBL_RES_PRO", "IM_MANDATI_REVERSALI_SIGLA", "IM_MANDATI_REVERSALI_DWH", "DIFF_IM_MANDATI_REVERSALI") AS 
  Select DT_ELABORAZIONE, PG_ELABORAZIONE, ESERCIZIO, ESERCIZIO_RES, CD_CENTRO_RESPONSABILITA,
       CD_LINEA_ATTIVITA, TI_GESTIONE, CD_ELEMENTO_VOCE,
       Sum(IM_STANZ_INIZIALE_A1_SIGLA) IM_STANZ_INIZIALE_A1_SIGLA, Sum(IM_STANZ_INIZIALE_A1_DWH) IM_STANZ_INIZIALE_A1_DWH,
                Sum(IM_STANZ_INIZIALE_A1_SIGLA)-Sum(IM_STANZ_INIZIALE_A1_DWH) DIFF_IM_STANZ_INIZIALE_A1,
       Sum(VARIAZIONI_PIU_SIGLA) VARIAZIONI_PIU_SIGLA, Sum(VARIAZIONI_PIU_DWH) VARIAZIONI_PIU_DWH,
                Sum(VARIAZIONI_PIU_SIGLA)-Sum(VARIAZIONI_PIU_DWH) DIFF_VARIAZIONI_PIU,
       Abs(SUM(VARIAZIONI_MENO_SIGLA)) VARIAZIONI_MENO_SIGLA, Abs(SUM(VARIAZIONI_MENO_DWH)) VARIAZIONI_MENO_DWH,
                Abs(SUM(VARIAZIONI_MENO_SIGLA))-Abs(SUM(VARIAZIONI_MENO_DWH)) DIFF_VARIAZIONI_MENO,
       Sum(IM_OBBL_ACC_COMP_SIGLA) IM_OBBL_ACC_COMP_SIGLA, Sum(IM_OBBL_ACC_COMP_DWH) IM_OBBL_ACC_COMP_DWH,
                Sum(IM_OBBL_ACC_COMP_SIGLA)-Sum(IM_OBBL_ACC_COMP_DWH) DIFF_IM_OBBL_ACC_COMP,
       Sum(IM_STANZ_RES_IMPROPRIO_SIGLA) IM_STANZ_RES_IMPROPRIO_SIGLA, Sum(IM_STANZ_RES_IMPROPRIO_DWH) IM_STANZ_RES_IMPROPRIO_DWH,
                Sum(IM_STANZ_RES_IMPROPRIO_SIGLA) - Sum(IM_STANZ_RES_IMPROPRIO_DWH) DIFF_IM_STANZ_RES_IMPROPRIO,
       Sum(VAR_PIU_STANZ_RES_IMP_SIGLA) VAR_PIU_STANZ_RES_IMP_SIGLA, Sum(VAR_PIU_STANZ_RES_IMP_DWH) VAR_PIU_STANZ_RES_IMP_DWH,
                Sum(VAR_PIU_STANZ_RES_IMP_SIGLA) - Sum(VAR_PIU_STANZ_RES_IMP_DWH) DIFF_VAR_PIU_STANZ_RES_IMP,
       Abs(SUM(VAR_MENO_STANZ_RES_IMP_SIGLA)) VAR_MENO_STANZ_RES_IMP_SIGLA, Abs(SUM(VAR_MENO_STANZ_RES_IMP_DWH)) VAR_MENO_STANZ_RES_IMP_DWH,
                Abs(SUM(VAR_MENO_STANZ_RES_IMP_SIGLA)) - Abs(SUM(VAR_MENO_STANZ_RES_IMP_DWH)) DIFF_VAR_MENO_STANZ_RES_IMP,
       Sum(IM_OBBL_RES_IMP_SIGLA) IM_OBBL_RES_IMP_SIGLA, Sum(IM_OBBL_RES_IMP_DWH) IM_OBBL_RES_IMP_DWH,
                Sum(IM_OBBL_RES_IMP_SIGLA) - Sum(IM_OBBL_RES_IMP_DWH) DIFF_IM_OBBL_RES_IMP,
       Sum(IM_OBBL_RES_PRO_SIGLA) IM_OBBL_RES_PRO_SIGLA, Sum(IM_OBBL_RES_PRO_DWH)+ABS(SUM(VAR_MENO_OBBL_RES_PRO_DWH))-SUM(VAR_PIU_OBBL_RES_PRO_DWH) IM_OBBL_RES_PRO_DWH,
                Sum(IM_OBBL_RES_PRO_SIGLA)-(Sum(IM_OBBL_RES_PRO_DWH)+ABS(SUM(VAR_MENO_OBBL_RES_PRO_DWH))-SUM(VAR_PIU_OBBL_RES_PRO_DWH)) DIFF_IM_OBBL_RES_PRO,
       Sum(VAR_PIU_OBBL_RES_PRO_SIGLA) VAR_PIU_OBBL_RES_PRO_SIGLA, Sum(VAR_PIU_OBBL_RES_PRO_DWH) VAR_PIU_OBBL_RES_PRO_DWH,
                Sum(VAR_PIU_OBBL_RES_PRO_SIGLA) - Sum(VAR_PIU_OBBL_RES_PRO_DWH) DIFF_VAR_PIU_OBBL_RES_PRO,
       Abs(SUM(VAR_MENO_OBBL_RES_PRO_SIGLA)) VAR_MENO_OBBL_RES_PRO_SIGLA, Abs(SUM(VAR_MENO_OBBL_RES_PRO_DWH)) VAR_MENO_OBBL_RES_PRO_DWH,
                Abs(SUM(VAR_MENO_OBBL_RES_PRO_SIGLA)) - Abs(SUM(VAR_MENO_OBBL_RES_PRO_DWH)) DIFF_VAR_MENO_OBBL_RES_PRO,
       Sum(IM_MANDATI_REVERSALI_SIGLA) IM_MANDATI_REVERSALI_SIGLA, Sum(IM_MANDATI_REVERSALI_DWH) IM_MANDATI_REVERSALI_DWH,
                Sum(IM_MANDATI_REVERSALI_SIGLA) - Sum(IM_MANDATI_REVERSALI_DWH) DIFF_IM_MANDATI_REVERSALI
From   CONFRONTO_SIGLA_DWH
Group By DT_ELABORAZIONE, PG_ELABORAZIONE, ESERCIZIO, ESERCIZIO_RES, CD_CENTRO_RESPONSABILITA, CD_LINEA_ATTIVITA, TI_GESTIONE, CD_ELEMENTO_VOCE
Having ABS( SUM(IM_STANZ_INIZIALE_A1_SIGLA)     - SUM(IM_STANZ_INIZIALE_A1_DWH)            ) > 0.00 OR
       ABS( SUM(VARIAZIONI_PIU_SIGLA)           - SUM(VARIAZIONI_PIU_DWH)                  ) > 0.00 OR
       ABS( ABS(SUM(VARIAZIONI_MENO_SIGLA))          - ABS(SUM(VARIAZIONI_MENO_DWH))            ) > 0.00 OR
       ABS( SUM(IM_OBBL_ACC_COMP_SIGLA)         - SUM(IM_OBBL_ACC_COMP_DWH)                ) > 0.00 OR
       ABS( SUM(IM_STANZ_RES_IMPROPRIO_SIGLA)   - SUM(IM_STANZ_RES_IMPROPRIO_DWH)          ) > 0.00 OR
       ABS( SUM(VAR_PIU_STANZ_RES_IMP_SIGLA)    - SUM(VAR_PIU_STANZ_RES_IMP_DWH)           ) > 0.00 OR
       ABS( SUM(VAR_MENO_STANZ_RES_IMP_SIGLA)   - ABS(SUM(VAR_MENO_STANZ_RES_IMP_DWH))     ) > 0.00 OR
       ABS( SUM(IM_OBBL_RES_IMP_SIGLA)          - SUM(IM_OBBL_RES_IMP_DWH)                 ) > 0.00 OR
       SUM(IM_OBBL_RES_PRO_SIGLA)          - (SUM(IM_OBBL_RES_PRO_DWH)+ABS(SUM(VAR_MENO_OBBL_RES_PRO_DWH))-SUM(VAR_PIU_OBBL_RES_PRO_DWH)) > 0.00 OR
       ABS( SUM(VAR_PIU_OBBL_RES_PRO_SIGLA)     - SUM(VAR_PIU_OBBL_RES_PRO_DWH)            ) > 0.00 OR
       ABS( SUM(VAR_MENO_OBBL_RES_PRO_SIGLA)    - ABS(SUM(VAR_MENO_OBBL_RES_PRO_DWH))      ) > 0.00 OR
       ABS( SUM(IM_MANDATI_REVERSALI_SIGLA)     - SUM(IM_MANDATI_REVERSALI_DWH)            ) > 0.00;