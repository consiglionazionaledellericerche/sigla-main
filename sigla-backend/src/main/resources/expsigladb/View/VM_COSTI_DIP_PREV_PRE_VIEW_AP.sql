--------------------------------------------------------
--  DDL for View VM_COSTI_DIP_PREV_PRE_VIEW_AP
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VM_COSTI_DIP_PREV_PRE_VIEW_AP" ("ESERCIZIO", "TI_PREV_CONS", "MATRICOLA", "TI_APPARTENENZA", "TI_GESTIONE", "CD_ELEMENTO_VOCE", "CD_UNITA_ORGANIZZATIVA", "IM_A1", "IM_A2", "IM_A3", "IM_ONERI_CNR_A1", "IM_ONERI_CNR_A2", "IM_ONERI_CNR_A3", "IM_TFR_A1", "IM_TFR_A2", "IM_TFR_A3") AS 
  SELECT
        A.anno_gest + 1,
        'P',
        A.matricola,
        'D',
        'S',
        A.voce_cia,
        DECODE(A.tit, NULL, NULL, SUBSTR(A.tit,1,3) || '.' || SUBSTR(A.tit,4,3)),
        DECODE(A.anno_gest + 1, A.anno_prev, A.impo_annuo, 0),
        DECODE(A.anno_gest + 2, A.anno_prev, A.impo_annuo, 0),
        DECODE(A.anno_gest + 3, A.anno_prev, A.impo_annuo, 0),
        DECODE(A.anno_gest + 1, A.anno_prev, A.oneri_cnr, 0),
        DECODE(A.anno_gest + 2, A.anno_prev, A.oneri_cnr, 0),
        DECODE(A.anno_gest + 3, A.anno_prev, A.oneri_cnr, 0),
        DECODE(A.anno_gest + 1, A.anno_prev, A.tfr, 0),
        DECODE(A.anno_gest + 2, A.anno_prev, A.tfr, 0),
        DECODE(A.anno_gest + 3, A.anno_prev, A.tfr, 0)
FROM    CNR_COSTI_PREV_APPO A;
