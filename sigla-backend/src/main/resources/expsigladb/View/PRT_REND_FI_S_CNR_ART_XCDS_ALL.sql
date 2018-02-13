--------------------------------------------------------
--  DDL for View PRT_REND_FI_S_CNR_ART_XCDS_ALL
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "PRT_REND_FI_S_CNR_ART_XCDS_ALL" ("ESER", "CDS", "PARTE", "DS_PARTE", "TITOLO", "DS_TITOLO", "CATEGORIA", "DS_CATEGORIA", "SEZIONE", "DS_SEZIONE", "CAPITOLO", "DS_CAPITOLO", "ARTICOLO", "DS_ARTICOLO", "INIZIALE", "VAR_PIU", "VAR_MENO", "ASSESTATO", "IMPEGNATO", "ACCREDITAMENTI", "REGOLARIZZATI", "DA_ACCRED", "IN_MENO", "IN_PIU", "INIZIALE_R", "VAR_PIU_R", "VAR_MENO_R", "ASSESTATO_R", "ACCREDITAMENTI_R", "REGOLARIZZATI_R", "IN_PIU_R", "IN_MENO_R", "TOT_ACCRED", "TOT_DA_ACCRED", "CDS_OR") AS 
  (SELECT
--
-- Date: 13/12/2006
-- Version: 1.1
--
-- Vista di stampa Rendiconto Finanziario CNR SPESE X CDS(compresi importi a zero)
--
-- History:
--
-- Date: 02/03/2004
-- Version: 1.0
-- Creazione
--
-- Date: 13/12/2006
-- Version: 1.1
-- Aggiunto il campo per il recupero degli importi delle variazioni di bilancio
-- di tipo REGOLARIZZAZIONE_CONTABILE
--
-- Body
--
           eser, cds, parte,
           prt_getdes_vocecap ('E', eser, 'C', 'S', parte, 'P') AS ds_parte,
           titolo,
           prt_getdes_vocecap ('E', eser, 'C', 'S', titolo, 'T') AS ds_titolo,
           categoria,
           prt_getdes_vocecap ('E',
                               eser,
                               'C',
                               'S',
                               categoria,
                               'G'
                              ) AS ds_categoria,
           sezione,
           prt_getdes_vocecap ('F',
                               eser,
                               'C',
                               'S',
                               sezione,
                               'S'
                              ) AS ds_sezione,
           capitolo,
           prt_getdes_vocecap ('F',
                               eser,
                               'C',
                               'S',
                               capitolo,
                               'C'
                              ) AS ds_capitolo,
           articolo,
           prt_getdes_vocecap ('F',
                               eser,
                               'C',
                               'S',
                               articolo,
                               'E'
                              ) AS ds_articolo,
           iniziale, var_piu, var_meno, assestato, impegnato, accreditamenti,
                                                                     --IN_PIU,
           (SELECT NVL (SUM (b.im_variazione), 0)
              FROM var_bilancio a, var_bilancio_det b
             WHERE a.cd_cds = cds
               AND a.esercizio = eser
               AND a.ti_appartenenza = 'C'
               AND a.ti_variazione = 'VAR_REG'
               AND a.stato = 'D'
               AND a.esercizio_importi = eser
               AND a.cd_cds = b.cd_cds
               AND a.esercizio = b.esercizio
               AND a.ti_appartenenza = b.ti_appartenenza
               AND a.pg_variazione = b.pg_variazione
               AND b.ti_gestione = 'S'
               AND b.cd_voce = articolo) regolarizzati,
           da_accred, in_meno, in_piu, iniziale_r, var_piu_r, var_meno_r,
           assestato_r, accreditamenti_r,
           (SELECT NVL (SUM (b.im_variazione), 0)
              FROM var_bilancio a, var_bilancio_det b
             WHERE a.cd_cds = cds
               AND a.esercizio = eser
               AND a.ti_appartenenza = 'C'
               AND a.ti_variazione = 'VAR_REG'
               AND a.stato = 'D'
               AND a.esercizio_importi < eser
               AND a.cd_cds = b.cd_cds
               AND a.esercizio = b.esercizio
               AND a.ti_appartenenza = b.ti_appartenenza
               AND a.pg_variazione = b.pg_variazione
               AND b.ti_gestione = 'S'
               AND b.cd_voce = articolo) regolarizzati_r,
           in_piu_r, in_meno_r, tot_accred, tot_da_accred,
           SUBSTR (articolo, 17, 3) cds_or
      FROM
-- parte I del bilancio CNR
           (-- ANTE 2006 - VECCHIO REGOLAMENTO
            SELECT   a.esercizio eser, a.cd_cds cds,
                     SUBSTR (a.cd_voce, 1, 1) parte,
                     SUBSTR (a.cd_voce, 1, 4) titolo,
                     SUBSTR (a.cd_voce, 1, 6) categoria,
                     SUBSTR (a.cd_voce, 1, 9) sezione,
                     SUBSTR (a.cd_voce, 1, 13) capitolo, a.cd_voce articolo,
                     SUM (Nvl(a.im_stanz_iniziale_a1, 0)) iniziale,
                     SUM (Nvl(a.variazioni_piu, 0)) var_piu,
                     SUM (Nvl(a.variazioni_meno, 0)) var_meno,
                     SUM ((Nvl(a.im_stanz_iniziale_a1, 0)+Nvl (a.variazioni_piu, 0)-Nvl (a.variazioni_meno, 0))) assestato,
                     SUM (Nvl(a.im_obblig_imp_acr, 0)) impegnato,
                     SUM (Nvl(a.im_mandati_reversali, 0)) accreditamenti,
                     SUM (Nvl (a.im_obblig_imp_acr, 0)-Nvl (a.im_mandati_reversali, 0)) da_accred,
                     SUM ((Nvl (a.im_stanz_iniziale_a1, 0)+Nvl (a.variazioni_piu, 0)-Nvl (a.variazioni_meno, 0))-Nvl (a.im_obblig_imp_acr, 0)) in_meno,
                     SUM (Nvl (a.im_obblig_imp_acr, 0)-(Nvl(a.im_stanz_iniziale_a1, 0)+ Nvl (a.variazioni_piu, 0)- Nvl (a.variazioni_meno, 0))) in_piu,
                     SUM (NVL (ar.im_stanz_iniziale_a1, 0)) iniziale_r,
                     SUM (NVL (ar.variazioni_piu, 0)) var_piu_r,
                     SUM (NVL (ar.variazioni_meno, 0)) var_meno_r,
                     SUM ((Nvl (ar.im_stanz_iniziale_a1, 0)+ Nvl (ar.variazioni_piu, 0)- Nvl (ar.variazioni_meno, 0))) assestato_r,
                     SUM (NVL (ar.im_mandati_reversali, 0)) accreditamenti_r,
                     SUM (Nvl (ar.im_mandati_reversali, 0)- (  NVL (ar.im_stanz_iniziale_a1, 0)+ Nvl (ar.variazioni_piu, 0)- Nvl (ar.variazioni_meno, 0))) in_piu_r,
                     SUM ((Nvl (ar.im_stanz_iniziale_a1, 0)+ Nvl (ar.variazioni_piu, 0)-Nvl (ar.variazioni_meno, 0))- Nvl (ar.im_mandati_reversali, 0)) in_meno_r,
                     (Sum (Nvl(a.im_mandati_reversali, 0))+Sum (NVL (ar.im_mandati_reversali, 0))) As tot_accred,
                     Sum(Nvl(a.im_obblig_imp_acr, 0)-Nvl (a.im_mandati_reversali, 0))+Sum((Nvl(ar.im_stanz_iniziale_a1, 0)+Nvl (ar.variazioni_piu, 0)- Nvl (ar.variazioni_meno, 0))- Nvl (ar.im_mandati_reversali, 0)) As tot_da_accred
               From  voce_f_saldi_cmp a, voce_f_saldi_cmp ar
               WHERE a.esercizio = ar.esercizio
                 AND a.ti_appartenenza = ar.ti_appartenenza
                 AND a.ti_gestione = ar.ti_gestione
                 AND a.cd_voce = ar.cd_voce
                 AND a.ti_competenza_residuo = 'C'
                 AND ar.ti_competenza_residuo = 'R'
                 AND a.ti_appartenenza = 'C'
                 AND a.ti_gestione = 'S'
                 AND SUBSTR (a.cd_voce, 1, 1) = '1'
                 And (Select Nvl(FL_REGOLAMENTO_2006, 'N') From parametri_cnr Where esercizio = a.esercizio) = 'N'
            GROUP BY a.esercizio,
                     a.cd_cds,
                     SUBSTR (a.cd_voce, 1, 1),
                     SUBSTR (a.cd_voce, 1, 4),
                     SUBSTR (a.cd_voce, 1, 6),
                     SUBSTR (a.cd_voce, 1, 9),
                     SUBSTR (a.cd_voce, 1, 13),
                     a.cd_voce
            Union
            -- POST 2006 - NUOVO REGOLAMENTO
            SELECT   a.esercizio eser,
                     Substr(a.cd_centro_responsabilita, 1, 3) cds,
                     SUBSTR (a.cd_voce, 1, 1) parte,
                     SUBSTR (a.cd_voce, 1, 4) titolo,
                     SUBSTR (a.cd_voce, 1, 6) categoria,
                     SUBSTR (a.cd_voce, 1, 9) sezione,
                     SUBSTR (a.cd_voce, 1, 13) capitolo, a.cd_voce articolo,
                     Sum(Nvl(a.im_stanz_iniziale_a1, 0)) iniziale,
                     Sum(Nvl(a.variazioni_piu, 0)) var_piu,
                     Sum(Nvl(a.variazioni_meno, 0)) var_meno,
                     Sum((Nvl(a.im_stanz_iniziale_a1, 0)+Nvl(a.variazioni_piu, 0)-Nvl(a.variazioni_meno, 0))) assestato,
                     Sum(Nvl(a.IM_OBBL_ACC_COMP, 0)) impegnato,
                     Sum(Nvl(Decode(a.esercizio, a.esercizio_res, a.IM_MANDATI_REVERSALI_PRO, 0), 0)) accreditamenti,
                     Sum(Nvl(a.IM_OBBL_ACC_COMP, 0)-Nvl(Decode(a.esercizio, a.esercizio_res, a.IM_MANDATI_REVERSALI_PRO, 0), 0)) da_accred,
                     Sum((Nvl(a.im_stanz_iniziale_a1, 0)+Nvl(a.variazioni_piu, 0)-Nvl(a.variazioni_meno, 0))-Nvl(a.IM_OBBL_ACC_COMP, 0)) in_meno,
                     Sum(Nvl(a.IM_OBBL_ACC_COMP, 0)-(Nvl(a.im_stanz_iniziale_a1, 0)+Nvl(a.variazioni_piu, 0)-Nvl(a.variazioni_meno, 0))) in_piu,
                     Sum(Nvl(a.IM_OBBL_RES_PRO, 0)) iniziale_r,
                     Sum(Nvl(a.VAR_PIU_OBBL_RES_PRO, 0)) var_piu_r,
                     Sum(Nvl(a.VAR_MENO_OBBL_RES_PRO, 0)) var_meno_r,
                     Sum((Nvl(a.IM_OBBL_RES_PRO, 0)+ Nvl(a.VAR_PIU_OBBL_RES_PRO, 0)-Nvl(a.VAR_MENO_OBBL_RES_PRO, 0))) assestato_r,
                     Sum(Nvl(Decode(a.esercizio, a.esercizio_res, 0, a.IM_MANDATI_REVERSALI_PRO), 0)) accreditamenti_r,
                     Sum(Nvl(Decode(a.esercizio, a.esercizio_res, 0, a.IM_MANDATI_REVERSALI_PRO), 0)-(Nvl(a.IM_OBBL_RES_PRO, 0)+Nvl(a.VAR_PIU_OBBL_RES_PRO, 0)-Nvl(a.VAR_MENO_OBBL_RES_PRO, 0))) in_piu_r,
                     Sum((Nvl(IM_OBBL_RES_PRO, 0)+ Nvl(a.VAR_PIU_OBBL_RES_PRO, 0)-Nvl(a.VAR_MENO_OBBL_RES_PRO, 0))-Nvl(Decode(a.esercizio, a.esercizio_res, 0, a.IM_MANDATI_REVERSALI_PRO), 0)) in_meno_r,
                     (Sum (Nvl(Decode(a.esercizio, a.esercizio_res, a.IM_MANDATI_REVERSALI_PRO, 0), 0))+Sum(Nvl(Decode(a.esercizio, a.esercizio_res, 0, a.IM_MANDATI_REVERSALI_PRO), 0))) tot_accred,
                     Sum(Nvl(a.IM_OBBL_ACC_COMP, 0)-Nvl(Decode(a.esercizio, a.esercizio_res, a.IM_MANDATI_REVERSALI_PRO, 0), 0))+Sum((Nvl(a.IM_OBBL_RES_PRO, 0)+Nvl(a.VAR_PIU_OBBL_RES_PRO, 0)-Nvl(a.VAR_MENO_OBBL_RES_PRO, 0))-Nvl(Decode(a.esercizio, a.esercizio_res, 0, a.IM_MANDATI_REVERSALI_PRO), 0)) tot_da_accred
               From  voce_f_saldi_cdr_linea a
               WHERE a.ti_appartenenza = 'C'
                 AND a.ti_gestione = 'S'
                 AND SUBSTR (a.cd_voce, 1, 1) = '1'
                 And (Select Nvl(FL_REGOLAMENTO_2006, 'N') From parametri_cnr Where esercizio = a.esercizio) = 'Y'
            GROUP BY a.esercizio,
                     Substr(a.cd_centro_responsabilita, 1, 3),
                     SUBSTR (a.cd_voce, 1, 1),
                     SUBSTR (a.cd_voce, 1, 4),
                     SUBSTR (a.cd_voce, 1, 6),
                     SUBSTR (a.cd_voce, 1, 9),
                     SUBSTR (a.cd_voce, 1, 13),
                     a.cd_voce
            )
)
   UNION ALL
   (SELECT eser, cds, parte,
           prt_getdes_vocecap ('E', eser, 'C', 'S', parte, 'P') AS ds_parte,
           titolo, NULL AS ds_titolo, categoria, NULL AS ds_categoria,
           sezione, NULL AS ds_sezione, capitolo,
           prt_getdes_vocecap ('E',
                               eser,
                               'C',
                               'S',
                               capitolo,
                               'C'
                              ) AS ds_capitolo,
           articolo,
           prt_getdes_vocecap ('F',
                               eser,
                               'C',
                               'S',
                               articolo,
                               'E'
                              ) AS ds_articolo,
           iniziale, var_piu, var_meno, assestato, impegnato, accreditamenti,
           0 regolarizzati,
--IN_PIU,
                           da_accred, in_meno, in_piu, iniziale_r, var_piu_r,
           var_meno_r, assestato_r, accreditamenti_r, 0 regolarizzati_r,
           in_piu_r, in_meno_r, tot_accred, tot_da_accred,
           SUBSTR (articolo, 17, 3) cds_or
      FROM
-- parte II del bilancio CNR
           (-- VECCHIO REGOLAMENTO ANTE 2006
            SELECT   a.esercizio eser, a.cd_cds cds,
                     SUBSTR (a.cd_voce, 1, 1) parte, NULL titolo,
                     NULL categoria, NULL sezione,
                     SUBSTR (a.cd_voce, 1, 13) capitolo, a.cd_voce articolo,
                     SUM (NVL (a.im_stanz_iniziale_a1, 0)) iniziale,
                     SUM (NVL (a.variazioni_piu, 0)) var_piu,
                     SUM (NVL (a.variazioni_meno, 0)) var_meno,
                     SUM ((Nvl (a.im_stanz_iniziale_a1, 0)+Nvl (a.variazioni_piu, 0)- Nvl (a.variazioni_meno, 0))) assestato,
                     SUM (NVL (a.im_obblig_imp_acr, 0)) impegnato,
                     SUM (NVL (a.im_mandati_reversali, 0)) accreditamenti,
                     SUM (Nvl (a.im_obblig_imp_acr, 0)- Nvl (a.im_mandati_reversali, 0)) da_accred,
                     SUM ((Nvl (a.im_stanz_iniziale_a1, 0)+Nvl (a.variazioni_piu, 0)-Nvl (a.variazioni_meno, 0))-Nvl (a.im_obblig_imp_acr, 0)) in_meno,
                     SUM (Nvl (a.im_obblig_imp_acr, 0)-(Nvl (a.im_stanz_iniziale_a1, 0)+Nvl (a.variazioni_piu, 0)- Nvl (a.variazioni_meno, 0))) in_piu,
                     SUM (NVL (ar.im_stanz_iniziale_a1, 0)) iniziale_r,
                     SUM (NVL (ar.variazioni_piu, 0)) var_piu_r,
                     SUM (NVL (ar.variazioni_meno, 0)) var_meno_r,
                     SUM ((Nvl (ar.im_stanz_iniziale_a1, 0)+ Nvl (ar.variazioni_piu, 0)-Nvl (ar.variazioni_meno, 0))) assestato_r,
                     SUM (NVL (ar.im_mandati_reversali, 0)) accreditamenti_r,
                     SUM (Nvl (ar.im_mandati_reversali, 0)- (  NVL (ar.im_stanz_iniziale_a1, 0)+ Nvl (ar.variazioni_piu, 0)- Nvl (ar.variazioni_meno, 0))) in_piu_r,
                     SUM ((Nvl (ar.im_stanz_iniziale_a1, 0)+ Nvl (ar.variazioni_piu, 0)- Nvl (ar.variazioni_meno, 0))- Nvl (ar.im_mandati_reversali, 0)) in_meno_r,
                     (Sum (NVL (a.im_mandati_reversali, 0))+Sum (NVL (ar.im_mandati_reversali, 0))) As tot_accred,
                     Sum(Nvl (a.im_obblig_imp_acr, 0)- Nvl (a.im_mandati_reversali, 0))+Sum((Nvl (ar.im_stanz_iniziale_a1, 0)+ Nvl (ar.variazioni_piu, 0)- Nvl (ar.variazioni_meno, 0))- Nvl (ar.im_mandati_reversali, 0)) As tot_da_accred
                FROM voce_f_saldi_cmp a, voce_f_saldi_cmp ar
               WHERE a.esercizio = ar.esercizio
                 AND a.ti_appartenenza = ar.ti_appartenenza
                 AND a.ti_gestione = ar.ti_gestione
                 AND a.cd_voce = ar.cd_voce
                 AND a.ti_competenza_residuo = 'C'
                 AND ar.ti_competenza_residuo = 'R'
                 AND a.ti_appartenenza = 'C'
                 AND a.ti_gestione = 'S'
                 AND SUBSTR (a.cd_voce, 1, 1) = '2'
                 And (Select Nvl(FL_REGOLAMENTO_2006, 'N') From parametri_cnr Where esercizio = a.esercizio) = 'N'
            GROUP BY a.esercizio,
                     a.cd_cds,
                     SUBSTR (a.cd_voce, 1, 1),
                     SUBSTR (a.cd_voce, 1, 13),
                     a.cd_voce
            Union
            -- NUOVO REGOLAMENTO POST 2006
            SELECT   a.esercizio eser,
                     Substr(a.cd_centro_responsabilita, 1, 3) cds,
                     SUBSTR (a.cd_voce, 1, 1) parte,
                     NULL titolo,
                     NULL categoria,
                     NULL sezione,
                     SUBSTR (a.cd_voce, 1, 13) capitolo, a.cd_voce articolo,
                     SUM (Nvl(a.im_stanz_iniziale_a1, 0)) iniziale,
                     SUM (Nvl(a.variazioni_piu, 0)) var_piu,
                     SUM (Nvl(a.variazioni_meno, 0)) var_meno,
                     SUM ((Nvl(a.im_stanz_iniziale_a1, 0)+Nvl (a.variazioni_piu, 0)- Nvl (a.variazioni_meno, 0))) assestato,
                     SUM (Nvl(a.IM_OBBL_ACC_COMP, 0)) impegnato,
                     SUM (Nvl(Decode(a.esercizio, a.esercizio_res, a.IM_MANDATI_REVERSALI_PRO, 0), 0)) accreditamenti,
                     SUM (Nvl(a.IM_OBBL_ACC_COMP, 0)-Nvl(Decode(a.esercizio, a.esercizio_res, a.IM_MANDATI_REVERSALI_PRO, 0), 0)) da_accred,
                     SUM ((Nvl(a.im_stanz_iniziale_a1, 0)+Nvl(a.variazioni_piu, 0)-Nvl(a.variazioni_meno, 0))-Nvl(a.IM_OBBL_ACC_COMP, 0)) in_meno,
                     SUM (Nvl(a.IM_OBBL_ACC_COMP, 0)-(Nvl (a.im_stanz_iniziale_a1, 0)+Nvl(a.variazioni_piu, 0)- Nvl (a.variazioni_meno, 0))) in_piu,
                     SUM (Nvl(IM_OBBL_RES_PRO, 0)) iniziale_r,
                     SUM (Nvl(VAR_PIU_OBBL_RES_PRO, 0)) var_piu_r,
                     SUM (Nvl(VAR_MENO_OBBL_RES_PRO, 0)) var_meno_r,
                     SUM ((Nvl(IM_OBBL_RES_PRO, 0)+ Nvl (VAR_PIU_OBBL_RES_PRO, 0)-Nvl (VAR_MENO_OBBL_RES_PRO, 0))) assestato_r,
                     SUM (Nvl(Decode(a.esercizio, a.esercizio_res, 0, a.IM_MANDATI_REVERSALI_PRO), 0)) accreditamenti_r,
                     SUM (Nvl(Decode(a.esercizio, a.esercizio_res, 0, a.IM_MANDATI_REVERSALI_PRO), 0)- (  NVL (IM_OBBL_RES_PRO, 0)+ Nvl (VAR_PIU_OBBL_RES_PRO, 0)- Nvl (VAR_MENO_OBBL_RES_PRO, 0))) in_piu_r,
                     SUM ((Nvl(IM_OBBL_RES_PRO, 0)+ Nvl (VAR_PIU_OBBL_RES_PRO, 0)- Nvl (VAR_MENO_OBBL_RES_PRO, 0))- Nvl (Decode(a.esercizio, a.esercizio_res, 0, a.IM_MANDATI_REVERSALI_PRO), 0)) in_meno_r,
                     (Sum (Nvl(Decode(a.esercizio, a.esercizio_res, a.IM_MANDATI_REVERSALI_PRO, 0), 0))+Sum (NVL (Decode(a.esercizio, a.esercizio_res, 0, a.IM_MANDATI_REVERSALI_PRO), 0))) As tot_accred,
                     Sum(Nvl (a.IM_OBBL_ACC_COMP, 0)- Nvl (Decode(a.esercizio, a.esercizio_res, a.IM_MANDATI_REVERSALI_PRO, 0), 0))+Sum((Nvl (IM_OBBL_RES_PRO, 0)+ Nvl (VAR_PIU_OBBL_RES_PRO, 0)- Nvl (VAR_MENO_OBBL_RES_PRO, 0))- Nvl (Decode(a.esercizio, a.esercizio_res, 0, a.IM_MANDATI_REVERSALI_PRO), 0)) As tot_da_accred
                FROM voce_f_saldi_cdr_linea a
               Where a.ti_appartenenza = 'C'
                 AND a.ti_gestione = 'S'
                 AND SUBSTR (a.cd_voce, 1, 1) = '2'
                 And (Select Nvl(FL_REGOLAMENTO_2006, 'N') From parametri_cnr Where esercizio = a.esercizio) = 'Y'
            GROUP BY a.esercizio,
                     Substr(a.cd_centro_responsabilita, 1, 3),
                     SUBSTR (a.cd_voce, 1, 1),
                     SUBSTR (a.cd_voce, 1, 13),
                     a.cd_voce
            )
);

   COMMENT ON TABLE "PRT_REND_FI_S_CNR_ART_XCDS_ALL"  IS 'Vista di stampa Rendiconto Finanziario CNR SPESE X CDS(compresi importi a zero)';
