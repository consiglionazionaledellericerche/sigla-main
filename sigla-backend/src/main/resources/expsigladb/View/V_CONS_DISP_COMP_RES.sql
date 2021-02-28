--------------------------------------------------------
--  DDL for View V_CONS_DISP_COMP_RES
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_CONS_DISP_COMP_RES" ("PROGETTO", "DS_PROGETTO", "COMMESSA", "DS_COMMESSA", "MODULO", "DS_MODULO", "DIPARTIMENTO", "DS_DIPARTIMENTO", "CDS", "DS_CDS", "UO", "CDR", "DS_CDR", "LDA", "DS_LDA", "CD_RESPONSABILE_TERZO", "ESERCIZIO", "ESERCIZIO_RES", "CD_VOCE", "CD_ELEMENTO_VOCE", "DS_VOCE", "STANZ_INI", "VAR_PIU", "VAR_MENO", "ASSESTATO_COMP", "OBB_COMP", "VINCOLI_COMP", "DISP_COMP", "PAGATO_COMP", "STANZ_RES_IMP", "VAR_PIU_STANZ_RES_IMP", "VAR_MENO_STANZ_RES_IMP", "ASSESTATO_RES_IMP", "OBB_RES_IMP", "OBB_RES_PRO", "VAR_PIU_RES_PRO", "VAR_MENO_RES_PRO", "VINCOLI_RES", "DISP_RES", "PAGATO_RES", "CD_NATURA", "CD_UNITA_PIANO", "CD_VOCE_PIANO") AS
  SELECT a.progetto, a.ds_progetto, a.commessa, a.ds_commessa, a.modulo,
          a.ds_modulo, a.dipartimento, a.ds_dipartimento, a.cds, a.ds_cds,
          a.uo, a.cdr, a.ds_cdr, a.lda, a.ds_lda, a.cd_responsabile_terzo,  a.esercizio,
          a.esercizio_res, a.cd_voce, a.cd_elemento_voce, a.ds_voce,
          a.stanz_ini, a.var_piu, a.var_meno, a.assestato_comp, a.obb_comp,
          0 vincoli_comp, a.disp_comp, a.pagato_comp, a.stanz_res_imp,
          a.var_piu_stanz_res_imp, a.var_meno_stanz_res_imp,
          a.assestato_res_imp, a.obb_res_imp, a.obb_res_pro,
          a.var_piu_res_pro, a.var_meno_res_pro, 0 vincoli_res, a.disp_res,
          a.pagato_res, a.cd_natura, a.cd_unita_piano, a.cd_voce_piano
     FROM (SELECT progetto.cd_progetto progetto,
                  progetto.ds_progetto ds_progetto, com.cd_progetto commessa,
                  com.ds_progetto ds_commessa, modu.cd_progetto modulo,
                  modu.ds_progetto ds_modulo,
                  progetto.cd_dipartimento dipartimento,
                  SUBSTR (dipartimento.ds_dipartimento,
                          1,
                          100
                         ) ds_dipartimento,
                  cds.cd_unita_organizzativa cds,
                  cds.ds_unita_organizzativa ds_cds,
                  uo.cd_unita_organizzativa uo,
                  cdr.cd_centro_responsabilita cdr, cdr.ds_cdr ds_cdr,
                  linea_attivita.cd_linea_attivita lda,
                  NVL (linea_attivita.ds_linea_attivita,
                       linea_attivita.denominazione
                      ) ds_lda,
                  linea_attivita.cd_responsabile_terzo,
                  voce_f_saldi_cdr_linea.esercizio esercizio,
                  voce_f_saldi_cdr_linea.esercizio_res esercizio_res,
                  voce_f_saldi_cdr_linea.cd_voce cd_voce,
                  voce_f_saldi_cdr_linea.cd_elemento_voce cd_elemento_voce,
                  elemento_voce.ds_elemento_voce ds_voce,
                  voce_f_saldi_cdr_linea.im_stanz_iniziale_a1 stanz_ini,
                  voce_f_saldi_cdr_linea.variazioni_piu var_piu,
                  voce_f_saldi_cdr_linea.variazioni_meno var_meno,
                  (  voce_f_saldi_cdr_linea.im_stanz_iniziale_a1
                   + voce_f_saldi_cdr_linea.variazioni_piu
                   - variazioni_meno
                  ) assestato_comp,
                  voce_f_saldi_cdr_linea.im_obbl_acc_comp obb_comp,
                    (  voce_f_saldi_cdr_linea.im_stanz_iniziale_a1
                     + voce_f_saldi_cdr_linea.variazioni_piu
                     - variazioni_meno
                    )
                  - voce_f_saldi_cdr_linea.im_obbl_acc_comp disp_comp,
                  DECODE
                     (voce_f_saldi_cdr_linea.esercizio,
                      voce_f_saldi_cdr_linea.esercizio_res, voce_f_saldi_cdr_linea.im_mandati_reversali_pro,
                      0
                     ) pagato_comp,
                  voce_f_saldi_cdr_linea.im_stanz_res_improprio stanz_res_imp,
                  voce_f_saldi_cdr_linea.var_piu_stanz_res_imp
                                                        var_piu_stanz_res_imp,
                  voce_f_saldi_cdr_linea.var_meno_stanz_res_imp
                                                       var_meno_stanz_res_imp,
                    voce_f_saldi_cdr_linea.im_stanz_res_improprio
                  + voce_f_saldi_cdr_linea.var_piu_stanz_res_imp
                  - voce_f_saldi_cdr_linea.var_meno_stanz_res_imp
                                                            assestato_res_imp,
                    voce_f_saldi_cdr_linea.im_obbl_res_imp
                  + voce_f_saldi_cdr_linea.var_piu_obbl_res_imp
                  - voce_f_saldi_cdr_linea.var_meno_obbl_res_imp obb_res_imp,
                  voce_f_saldi_cdr_linea.im_obbl_res_pro obb_res_pro,
                  voce_f_saldi_cdr_linea.var_piu_obbl_res_pro var_piu_res_pro,
                  voce_f_saldi_cdr_linea.var_meno_obbl_res_pro
                                                             var_meno_res_pro,
                    (  voce_f_saldi_cdr_linea.im_stanz_res_improprio
                     + voce_f_saldi_cdr_linea.var_piu_stanz_res_imp
                     - voce_f_saldi_cdr_linea.var_meno_stanz_res_imp
                    )
                  - (  voce_f_saldi_cdr_linea.im_obbl_res_imp
                     + voce_f_saldi_cdr_linea.var_piu_obbl_res_imp
                     - voce_f_saldi_cdr_linea.var_meno_obbl_res_imp
                     + voce_f_saldi_cdr_linea.var_piu_obbl_res_pro
                     - voce_f_saldi_cdr_linea.var_meno_obbl_res_pro
                    ) disp_res,
                  DECODE
                     (voce_f_saldi_cdr_linea.esercizio,
                      voce_f_saldi_cdr_linea.esercizio_res, 0,
                        voce_f_saldi_cdr_linea.im_mandati_reversali_pro
                      + voce_f_saldi_cdr_linea.im_mandati_reversali_imp
                     ) pagato_res,
                  linea_attivita.cd_natura, NULL cd_unita_piano,
                  NULL cd_voce_piano
             FROM progetto_gest progetto,
                  progetto_gest com,
                  progetto_gest modu,
                  linea_attivita,
                  unita_organizzativa cds,
                  unita_organizzativa uo,
                  cdr,
                  elemento_voce,
                  voce_f_saldi_cdr_linea,
                  dipartimento
            WHERE progetto.esercizio = com.esercizio_progetto_padre
              AND progetto.pg_progetto = com.pg_progetto_padre
              AND com.esercizio = modu.esercizio_progetto_padre
              AND com.pg_progetto = modu.pg_progetto_padre
              AND linea_attivita.pg_progetto = modu.pg_progetto
              AND modu.esercizio = voce_f_saldi_cdr_linea.esercizio
              AND linea_attivita.cd_centro_responsabilita =
                               voce_f_saldi_cdr_linea.cd_centro_responsabilita
              AND linea_attivita.cd_linea_attivita =
                                      voce_f_saldi_cdr_linea.cd_linea_attivita
              AND cdr.cd_centro_responsabilita =
                               voce_f_saldi_cdr_linea.cd_centro_responsabilita
              AND cdr.cd_unita_organizzativa = uo.cd_unita_organizzativa
              AND uo.cd_unita_padre = cds.cd_unita_organizzativa
              AND elemento_voce.esercizio = voce_f_saldi_cdr_linea.esercizio
              AND elemento_voce.cd_elemento_voce =
                                       voce_f_saldi_cdr_linea.cd_elemento_voce
              AND elemento_voce.ti_appartenenza =
                                        voce_f_saldi_cdr_linea.ti_appartenenza
              AND elemento_voce.ti_gestione =
                                            voce_f_saldi_cdr_linea.ti_gestione
              AND voce_f_saldi_cdr_linea.ti_gestione = 'S'
              AND linea_attivita.pg_progetto IS NOT NULL
              AND progetto.cd_dipartimento = dipartimento.cd_dipartimento
           UNION ALL
           SELECT 'XXXXXXX', 'Progetto non definito', 'XXXXXXX',
                  'Commessa non definita', 'XXXXXXX', 'Modulo non definito',
                  'XXXXXXX', 'Dipartimento non definito',
                  cds.cd_unita_organizzativa, cds.ds_unita_organizzativa,
                  uo.cd_unita_organizzativa, cdr.cd_centro_responsabilita,
                  cdr.ds_cdr, linea_attivita.cd_linea_attivita,
                  NVL (linea_attivita.ds_linea_attivita,
                       linea_attivita.denominazione
                      ) ds_linea_attivita,
                  linea_attivita.cd_responsabile_terzo,
                  voce_f_saldi_cdr_linea.esercizio,
                  voce_f_saldi_cdr_linea.esercizio_res,
                  voce_f_saldi_cdr_linea.cd_voce,
                  voce_f_saldi_cdr_linea.cd_elemento_voce,
                  elemento_voce.ds_elemento_voce,
                  voce_f_saldi_cdr_linea.im_stanz_iniziale_a1,
                  voce_f_saldi_cdr_linea.variazioni_piu,
                  voce_f_saldi_cdr_linea.variazioni_meno,
                  (  voce_f_saldi_cdr_linea.im_stanz_iniziale_a1
                   + voce_f_saldi_cdr_linea.variazioni_piu
                   - variazioni_meno
                  ),
                  voce_f_saldi_cdr_linea.im_obbl_acc_comp,
                    (  voce_f_saldi_cdr_linea.im_stanz_iniziale_a1
                     + voce_f_saldi_cdr_linea.variazioni_piu
                     - variazioni_meno
                    )
                  - voce_f_saldi_cdr_linea.im_obbl_acc_comp,
                  voce_f_saldi_cdr_linea.im_mandati_reversali_pro,
                  voce_f_saldi_cdr_linea.im_stanz_res_improprio,
                  voce_f_saldi_cdr_linea.var_piu_stanz_res_imp,
                  voce_f_saldi_cdr_linea.var_meno_stanz_res_imp,
                    voce_f_saldi_cdr_linea.im_stanz_res_improprio
                  + voce_f_saldi_cdr_linea.var_piu_stanz_res_imp
                  - voce_f_saldi_cdr_linea.var_meno_stanz_res_imp,
                    voce_f_saldi_cdr_linea.im_obbl_res_imp
                  + voce_f_saldi_cdr_linea.var_piu_obbl_res_imp
                  - voce_f_saldi_cdr_linea.var_meno_obbl_res_imp,
                  voce_f_saldi_cdr_linea.im_obbl_res_pro,
                  voce_f_saldi_cdr_linea.var_piu_obbl_res_pro,
                  voce_f_saldi_cdr_linea.var_meno_obbl_res_pro,
                    (  voce_f_saldi_cdr_linea.im_stanz_res_improprio
                     + voce_f_saldi_cdr_linea.var_piu_stanz_res_imp
                     - voce_f_saldi_cdr_linea.var_meno_stanz_res_imp
                    )
                  - (  voce_f_saldi_cdr_linea.im_obbl_res_imp
                     + voce_f_saldi_cdr_linea.var_piu_obbl_res_imp
                     - voce_f_saldi_cdr_linea.var_meno_obbl_res_imp
                     + voce_f_saldi_cdr_linea.var_piu_obbl_res_pro
                     - voce_f_saldi_cdr_linea.var_meno_obbl_res_pro
                    ),
                    voce_f_saldi_cdr_linea.im_mandati_reversali_pro
                  + voce_f_saldi_cdr_linea.im_mandati_reversali_imp,
                  linea_attivita.cd_natura, NULL cd_unita_piano,
                  NULL cd_voce_piano
             FROM linea_attivita,
                  unita_organizzativa cds,
                  unita_organizzativa uo,
                  cdr,
                  elemento_voce,
                  voce_f_saldi_cdr_linea
            WHERE linea_attivita.cd_centro_responsabilita =
                               voce_f_saldi_cdr_linea.cd_centro_responsabilita
              AND linea_attivita.cd_linea_attivita =
                                      voce_f_saldi_cdr_linea.cd_linea_attivita
              AND cdr.cd_centro_responsabilita =
                               voce_f_saldi_cdr_linea.cd_centro_responsabilita
              AND cdr.cd_unita_organizzativa = uo.cd_unita_organizzativa
              AND uo.cd_unita_padre = cds.cd_unita_organizzativa
              AND elemento_voce.esercizio = voce_f_saldi_cdr_linea.esercizio
              AND elemento_voce.cd_elemento_voce =
                                       voce_f_saldi_cdr_linea.cd_elemento_voce
              AND elemento_voce.ti_appartenenza =
                                        voce_f_saldi_cdr_linea.ti_appartenenza
              AND elemento_voce.ti_gestione =
                                            voce_f_saldi_cdr_linea.ti_gestione
              AND voce_f_saldi_cdr_linea.ti_gestione = 'S'
              AND linea_attivita.pg_progetto IS NULL) a,
          parametri_cnr
    WHERE parametri_cnr.fl_nuovo_pdg = 'N'
      AND parametri_cnr.esercizio = a.esercizio
   UNION ALL
   SELECT a.progetto, a.ds_progetto, a.commessa, a.ds_commessa, a.modulo,
          a.ds_modulo, a.dipartimento, a.ds_dipartimento, a.cds, a.ds_cds,
          a.uo, a.cdr, a.ds_cdr, a.lda, a.ds_lda, a.cd_responsabile_terzo, a.esercizio,
          a.esercizio_res, a.cd_voce, a.cd_elemento_voce, a.ds_voce,
          a.stanz_ini, a.var_piu, a.var_meno, a.assestato_comp, a.obb_comp,
          CASE
             WHEN a.esercizio = a.esercizio_res
                THEN a.vincoli
             ELSE 0
          END vincoli_comp,
          CASE
             WHEN a.esercizio = a.esercizio_res
                THEN a.disp_comp - a.vincoli
             ELSE a.disp_comp
          END disp_comp,
          a.pagato_comp, a.stanz_res_imp, a.var_piu_stanz_res_imp,
          a.var_meno_stanz_res_imp, a.assestato_res_imp, a.obb_res_imp,
          a.obb_res_pro, a.var_piu_res_pro, a.var_meno_res_pro,
          CASE
             WHEN a.esercizio > a.esercizio_res
                THEN a.vincoli
             ELSE 0
          END vincoli_res,
          CASE
             WHEN a.esercizio > a.esercizio_res
                THEN a.disp_res - a.vincoli
             ELSE a.disp_res
          END disp_res,
          a.pagato_res, a.cd_natura, a.cd_unita_piano, a.cd_voce_piano
     FROM (SELECT NULL progetto, NULL ds_progetto,
                  (SELECT com.cd_progetto
                     FROM progetto_gest com
                    WHERE com.esercizio =
                                       modu.esercizio_progetto_padre
                      AND com.pg_progetto = modu.pg_progetto_padre) commessa,
                  (SELECT com.ds_progetto
                     FROM progetto_gest com
                    WHERE com.esercizio =
                                    modu.esercizio_progetto_padre
                      AND com.pg_progetto = modu.pg_progetto_padre)
                                                                  ds_commessa,
                  modu.cd_progetto modulo, modu.ds_progetto ds_modulo,
                  modu.p_cd_dipartimento dipartimento,
                  SUBSTR (dipartimento.ds_dipartimento,
                          1,
                          100
                         ) ds_dipartimento,
                  cds.cd_unita_organizzativa cds,
                  cds.ds_unita_organizzativa ds_cds,
                  uo.cd_unita_organizzativa uo,
                  cdr.cd_centro_responsabilita cdr, cdr.ds_cdr,
                  v_linea_attivita_valida.cd_linea_attivita lda,
                  NVL (v_linea_attivita_valida.ds_linea_attivita,
                       v_linea_attivita_valida.denominazione
                      ) ds_lda,
                  v_linea_attivita_valida.cd_responsabile_terzo,
                  voce_f_saldi_cdr_linea.esercizio,
                  voce_f_saldi_cdr_linea.esercizio_res,
                  voce_f_saldi_cdr_linea.cd_voce,
                  voce_f_saldi_cdr_linea.cd_elemento_voce,
                  elemento_voce.ds_elemento_voce ds_voce,
                  voce_f_saldi_cdr_linea.im_stanz_iniziale_a1 stanz_ini,
                  voce_f_saldi_cdr_linea.variazioni_piu var_piu,
                  voce_f_saldi_cdr_linea.variazioni_meno var_meno,
                  (  voce_f_saldi_cdr_linea.im_stanz_iniziale_a1
                   + voce_f_saldi_cdr_linea.variazioni_piu
                   - voce_f_saldi_cdr_linea.variazioni_meno
                  ) assestato_comp,
                  voce_f_saldi_cdr_linea.im_obbl_acc_comp obb_comp,
                    (  voce_f_saldi_cdr_linea.im_stanz_iniziale_a1
                     + voce_f_saldi_cdr_linea.variazioni_piu
                     - voce_f_saldi_cdr_linea.variazioni_meno
                    )
                  - voce_f_saldi_cdr_linea.im_obbl_acc_comp disp_comp,
                  DECODE
                     (voce_f_saldi_cdr_linea.esercizio,
                      voce_f_saldi_cdr_linea.esercizio_res, voce_f_saldi_cdr_linea.im_mandati_reversali_pro,
                      0
                     ) pagato_comp,
                  voce_f_saldi_cdr_linea.im_stanz_res_improprio stanz_res_imp,
                  voce_f_saldi_cdr_linea.var_piu_stanz_res_imp,
                  voce_f_saldi_cdr_linea.var_meno_stanz_res_imp,
                    voce_f_saldi_cdr_linea.im_stanz_res_improprio
                  + voce_f_saldi_cdr_linea.var_piu_stanz_res_imp
                  - voce_f_saldi_cdr_linea.var_meno_stanz_res_imp
                                                            assestato_res_imp,
                    voce_f_saldi_cdr_linea.im_obbl_res_imp
                  + voce_f_saldi_cdr_linea.var_piu_obbl_res_imp
                  - voce_f_saldi_cdr_linea.var_meno_obbl_res_imp obb_res_imp,
                  voce_f_saldi_cdr_linea.im_obbl_res_pro obb_res_pro,
                  voce_f_saldi_cdr_linea.var_piu_obbl_res_pro var_piu_res_pro,
                  voce_f_saldi_cdr_linea.var_meno_obbl_res_pro
                                                             var_meno_res_pro,
                    (  voce_f_saldi_cdr_linea.im_stanz_res_improprio
                     + voce_f_saldi_cdr_linea.var_piu_stanz_res_imp
                     - voce_f_saldi_cdr_linea.var_meno_stanz_res_imp
                    )
                  - (  voce_f_saldi_cdr_linea.im_obbl_res_imp
                     + voce_f_saldi_cdr_linea.var_piu_obbl_res_imp
                     - voce_f_saldi_cdr_linea.var_meno_obbl_res_imp
                     + voce_f_saldi_cdr_linea.var_piu_obbl_res_pro
                     - voce_f_saldi_cdr_linea.var_meno_obbl_res_pro
                    ) disp_res,
                  DECODE
                     (voce_f_saldi_cdr_linea.esercizio,
                      voce_f_saldi_cdr_linea.esercizio_res, 0,
                        voce_f_saldi_cdr_linea.im_mandati_reversali_pro
                      + voce_f_saldi_cdr_linea.im_mandati_reversali_imp
                     ) pagato_res,
                  v_linea_attivita_valida.cd_natura,
                  NULL cd_unita_piano,
                  NULL cd_voce_piano,
                  cnrutl002.IM_VINCOLI(voce_f_saldi_cdr_linea.esercizio,
                                       voce_f_saldi_cdr_linea.esercizio_res,
                                       voce_f_saldi_cdr_linea.cd_centro_responsabilita,
                                       voce_f_saldi_cdr_linea.cd_linea_attivita,
                                       voce_f_saldi_cdr_linea.ti_appartenenza,
                                       voce_f_saldi_cdr_linea.ti_gestione,
                                       voce_f_saldi_cdr_linea.cd_elemento_voce) vincoli
             FROM v_progetto_padre modu,
                  v_linea_attivita_valida,
                  unita_organizzativa cds,
                  unita_organizzativa uo,
                  cdr,
                  elemento_voce,
                  voce_f_saldi_cdr_linea,
                  dipartimento,
                  parametri_cnr
            WHERE parametri_cnr.fl_nuovo_pdg = 'Y'
              AND parametri_cnr.esercizio = voce_f_saldi_cdr_linea.esercizio
              AND v_linea_attivita_valida.esercizio = modu.esercizio
              AND modu.tipo_fase = 'G'
              AND v_linea_attivita_valida.pg_progetto = modu.pg_progetto
              AND v_linea_attivita_valida.esercizio =
                                              voce_f_saldi_cdr_linea.esercizio
              AND v_linea_attivita_valida.cd_centro_responsabilita =
                               voce_f_saldi_cdr_linea.cd_centro_responsabilita
              AND v_linea_attivita_valida.cd_linea_attivita =
                                      voce_f_saldi_cdr_linea.cd_linea_attivita
              AND cdr.cd_centro_responsabilita =
                               voce_f_saldi_cdr_linea.cd_centro_responsabilita
              AND cdr.cd_unita_organizzativa = uo.cd_unita_organizzativa
              AND uo.cd_unita_padre = cds.cd_unita_organizzativa
              AND elemento_voce.esercizio = voce_f_saldi_cdr_linea.esercizio
              AND elemento_voce.cd_elemento_voce =
                                       voce_f_saldi_cdr_linea.cd_elemento_voce
              AND elemento_voce.ti_appartenenza =
                                        voce_f_saldi_cdr_linea.ti_appartenenza
              AND elemento_voce.ti_gestione =
                                            voce_f_saldi_cdr_linea.ti_gestione
              AND voce_f_saldi_cdr_linea.ti_gestione = 'S'
              AND v_linea_attivita_valida.pg_progetto IS NOT NULL
              AND modu.p_cd_dipartimento = dipartimento.cd_dipartimento) a;
