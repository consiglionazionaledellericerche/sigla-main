--------------------------------------------------------
--  DDL for View PZ_COSTI_DIP_PREV_VIEW
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "PZ_COSTI_DIP_PREV_VIEW" ("ESERCIZIO", "TI_PREV_CONS", "MATRICOLA", "TI_APPARTENENZA", "TI_GESTIONE", "CD_ELEMENTO_VOCE", "CD_UNITA_ORGANIZZATIVA", "IM_A1", "IM_A2", "IM_A3", "IM_ONERI_CNR_A1", "IM_ONERI_CNR_A2", "IM_ONERI_CNR_A3", "IM_TFR_A1", "IM_TFR_A2", "IM_TFR_A3") AS 
  SELECT  A.esercizio,
        A.ti_prev_cons,
        A.matricola,
        A.ti_appartenenza,
        A.ti_gestione,
        A.cd_elemento_voce,
        A.cd_unita_organizzativa,
        SUM(A.im_a1),
        SUM(A.im_a2),
        SUM(A.im_a3),
        SUM(A.im_oneri_cnr_a1),
        SUM(A.im_oneri_cnr_a2),
        SUM(A.im_oneri_cnr_a3),
        SUM(A.im_tfr_a1),
        SUM(A.im_tfr_a2),
        SUM(A.im_tfr_a3)
FROM    PZ_COSTI_DIP_PREV_PRE_VIEW A
GROUP BY A.esercizio,
        A.ti_prev_cons,
        A.matricola,
        A.ti_appartenenza,
        A.ti_gestione,
        A.cd_elemento_voce,
        A.cd_unita_organizzativa
;
