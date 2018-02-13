--------------------------------------------------------
--  DDL for View V_SIT_GAE_RESIDUI_SPESA
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_SIT_GAE_RESIDUI_SPESA" ("ESERCIZIO", "CDS", "UO", "CD_CENTRO_RESPONSABILITA", "DS_CDR", "CD_LINEA_ATTIVITA", "DS_LINEA_ATTIVITA", "CD_NATURA", "CD_PROGETTO", "DS_PROGETTO", "CD_COMMESSA", "DS_COMMESSA", "CD_MODULO", "DS_MODULO", "TI_APPARTENENZA", "TI_GESTIONE", "CD_ELEMENTO_VOCE", "CD_VOCE", "DS_ELEMENTO_VOCE", "ESERCIZIO_RES", "STANZ_RES_INI", "VAR_STANZ_RES_PIU", "VAR_STANZ_RES_MENO_STO", "VAR_STANZ_RES_MENO_ECO", "RES_PRO_INI", "VAR_RES_PRO_PIU", "VAR_RES_PRO_MENO", "LIQUIDATO_PRO", "PAGATO_PRO", "RES_IMP_RIBALTATI", "RES_IMP_RIB_LIQ", "RES_IMP_RIB_PAG", "RES_IMP_EM_ESE", "RES_IMP_EM_ESE_LIQ", "RES_IMP_EM_ESE_PAG", "RES_IMP_ATTUALI", "RES_VINCOLI") AS 
  SELECT
--
-- Date: 09/11/2006
-- Version: 1.1
--
-- Vista SITUAZIONE GAE sui residui di spesa
--
-- History:
--
-- Date: 01/01/2006
-- Version: 1.0
-- Creazione
--
-- Date: 09/11/2006
-- Version: 1.1
-- Aggiunta la selezione del progetto/commessa/modulo per anno
--
-- Body:
--
          vs.esercizio esercizio,
          cnrutl001.getcdsfromcdr (vs.cd_centro_responsabilita) cds,
          cnrctb020.getcduo (vs.cd_centro_responsabilita) uo,
          vs.cd_centro_responsabilita cd_centro_responsabilita,
          cdr.ds_cdr ds_cdr, vs.cd_linea_attivita cd_linea_attivita,
          DECODE (la.denominazione,
                  NULL, DECODE (la.ds_linea_attivita,
                                NULL, 'NESSUNA DESCRIZIONE',
                                la.ds_linea_attivita
                               ),
                  la.denominazione
                 ) ds_linea_attivita,
          la.cd_natura cd_natura, prog.cd_progetto cd_progetto,
          prog.ds_progetto ds_progetto, comm.cd_progetto cd_commessa,
          comm.ds_progetto ds_commessa, modu.cd_progetto cd_modulo,
          modu.ds_progetto ds_modulo, vs.ti_appartenenza ti_appartenenza,
          vs.ti_gestione ti_gestione, vs.cd_elemento_voce cd_elemento_voce,
          vs.cd_voce cd_voce, e.ds_elemento_voce ds_elemento_voce,
          vs.esercizio_res esercizio_res,
          
/*
 RES_IMP_RIBALTATI,  -- PER AVERE L'INIZIALE OCCORRE FARE UNA SELECT
 RES_IMP_RIB_LIQ, -- RESIDUI IMPROPRI EMESSI NELL'ESERCIZIO LIQUIDATI (SELECT)
 RES_IMP_RIB_PAG, -- RESIDUI IMPROPRI EMESSI NELL'ESERCIZIO PAGATI (SELECT)
 RES_IMP_EM_ESE, -- RESIDUI IMPROPRI EMESSI NELL'ESERCIZIO (SELECT)
 RES_IMP_EM_ESE_LIQ, -- RESIDUI IMPROPRI EMESSI NELL'ESERCIZIO LIQUIDATI (SELECT)
 RES_IMP_EM_ESE_PAG, -- RESIDUI IMPROPRI EMESSI NELL'ESERCIZIO PAGATI (SELECT)
 RES_IMP_ATTUALI -- DA VOCE_F_SALDI_CDR_LINEA
*/
-- STANZ_RES_INI,
          NVL (vs.im_stanz_res_improprio, 0) stanz_res_ini,
          
-- VAR_STANZ_RES_PIU,
          NVL (vs.var_piu_stanz_res_imp, 0) var_stanz_res_piu,
          
-- VAR_STANZ_RES_MENO_STO
          cnrutl002.var_stanz_res_meno
                         (vs.esercizio,
                          vs.esercizio_res,
                          vs.cd_centro_responsabilita,
                          vs.cd_linea_attivita,
                          vs.ti_appartenenza,
                          vs.ti_gestione,
                          vs.cd_voce,
                          'STO_INT'
                         ) var_stanz_res_meno_sto,
          
-- VAR_STANZ_RES_MENO_ECO
          cnrutl002.var_stanz_res_meno
                         (vs.esercizio,
                          vs.esercizio_res,
                          vs.cd_centro_responsabilita,
                          vs.cd_linea_attivita,
                          vs.ti_appartenenza,
                          vs.ti_gestione,
                          vs.cd_voce,
                          'ECO'
                         ) var_stanz_res_meno_eco,
          
-- RES_PRO_INI
          NVL (vs.im_obbl_res_pro, 0) res_pro_ini,
          
-- VAR_RES_PRO_PIU
          NVL (vs.var_piu_obbl_res_pro, 0) var_res_pro_piu,
          
-- VAR_RES_PRO_MENO
          NVL (vs.var_meno_obbl_res_pro, 0) var_res_pro_meno,
          
-- LIQUIDATO_PRO
          cnrutl002.liquidato_pro (vs.esercizio,
                                   vs.esercizio_res,
                                   vs.cd_centro_responsabilita,
                                   vs.cd_linea_attivita,
                                   vs.ti_appartenenza,
                                   vs.ti_gestione,
                                   vs.cd_voce
                                  ) liquidato_pro,
          
-- PAGATO_PRO
          NVL (im_mandati_reversali_pro, 0) pagato_pro,
          
-- RES_IMP_RIBALTATI
          cnrutl002.residui_impropri
                              (vs.esercizio,
                               vs.esercizio_res,
                               vs.cd_centro_responsabilita,
                               vs.cd_linea_attivita,
                               vs.ti_appartenenza,
                               vs.ti_gestione,
                               vs.cd_voce,
                               'Y'
                              ) res_imp_ribaltati,
          
-- RES_IMP_RIB_LIQ
          cnrutl002.residui_impropri_liq
                                (vs.esercizio,
                                 vs.esercizio_res,
                                 vs.cd_centro_responsabilita,
                                 vs.cd_linea_attivita,
                                 vs.ti_appartenenza,
                                 vs.ti_gestione,
                                 vs.cd_voce,
                                 'Y'
                                ) res_imp_rib_liq,
          
-- RES_IMP_RIB_PAG
          cnrutl002.residui_impropri_pag
                                (vs.esercizio,
                                 vs.esercizio_res,
                                 vs.cd_centro_responsabilita,
                                 vs.cd_linea_attivita,
                                 vs.ti_appartenenza,
                                 vs.ti_gestione,
                                 vs.cd_voce,
                                 'Y'
                                ) res_imp_rib_pag,
          
-- RES_IMP_EM_ESE
          cnrutl002.residui_impropri
                                 (vs.esercizio,
                                  vs.esercizio_res,
                                  vs.cd_centro_responsabilita,
                                  vs.cd_linea_attivita,
                                  vs.ti_appartenenza,
                                  vs.ti_gestione,
                                  vs.cd_voce,
                                  'N'
                                 ) res_imp_em_ese,
          
-- RES_IMP_EM_ESE_LIQ
          cnrutl002.residui_impropri_liq
                             (vs.esercizio,
                              vs.esercizio_res,
                              vs.cd_centro_responsabilita,
                              vs.cd_linea_attivita,
                              vs.ti_appartenenza,
                              vs.ti_gestione,
                              vs.cd_voce,
                              'N'
                             ) res_imp_em_ese_liq,
          
-- RES_IMP_EM_ESE_PAG
          cnrutl002.residui_impropri_pag
                             (vs.esercizio,
                              vs.esercizio_res,
                              vs.cd_centro_responsabilita,
                              vs.cd_linea_attivita,
                              vs.ti_appartenenza,
                              vs.ti_gestione,
                              vs.cd_voce,
                              'N'
                             ) res_imp_em_ese_pag,
          
-- RES_IMP_ATTUALI
          NVL (vs.im_obbl_res_imp, 0) res_imp_attuali,
-- VINCOLI
          0 res_vincoli
     FROM voce_f_saldi_cdr_linea vs,
          linea_attivita la,
          progetto_gest modu,
          progetto_gest comm,
          progetto_gest prog,
          voce_f voce,
          cdr,
          elemento_voce e,
          parametri_cnr
    WHERE parametri_cnr.esercizio = vs.esercizio
      AND parametri_cnr.fl_nuovo_pdg = 'N'
      AND vs.esercizio > esercizio_res
      AND vs.ti_gestione = 'S'
      AND vs.cd_centro_responsabilita = la.cd_centro_responsabilita
      AND vs.cd_linea_attivita = la.cd_linea_attivita
      AND la.pg_progetto = modu.pg_progetto
      AND modu.esercizio = vs.esercizio
      AND modu.esercizio_progetto_padre = comm.esercizio
      AND modu.pg_progetto_padre = comm.pg_progetto
      AND comm.esercizio_progetto_padre = prog.esercizio
      AND comm.pg_progetto_padre = prog.pg_progetto
      AND vs.esercizio = voce.esercizio
      AND vs.ti_appartenenza = voce.ti_appartenenza
      AND vs.ti_gestione = voce.ti_gestione
      AND vs.cd_voce = voce.cd_voce
      AND vs.cd_centro_responsabilita = cdr.cd_centro_responsabilita
      AND e.esercizio = voce.esercizio
      AND e.ti_appartenenza = voce.ti_appartenenza
      AND e.ti_gestione = voce.ti_gestione
      AND e.cd_elemento_voce = voce.cd_elemento_voce
   UNION ALL
   SELECT vs.esercizio,
          cnrutl001.getcdsfromcdr (vs.cd_centro_responsabilita) cds,
          cnrctb020.getcduo (vs.cd_centro_responsabilita) uo,
          vs.cd_centro_responsabilita, cdr.ds_cdr, vs.cd_linea_attivita,
          DECODE (la.denominazione,
                  NULL, DECODE (la.ds_linea_attivita,
                                NULL, 'NESSUNA DESCRIZIONE',
                                la.ds_linea_attivita
                               ),
                  la.denominazione
                 ),
          la.cd_natura, NULL cd_progetto, NULL ds_progetto,
          (SELECT cd_progetto
             FROM progetto_gest comm
            WHERE modu.esercizio_progetto_padre = comm.esercizio
              AND modu.pg_progetto_padre = comm.pg_progetto) cd_commessa,
          (SELECT ds_progetto
             FROM progetto_gest comm
            WHERE modu.esercizio_progetto_padre = comm.esercizio
              AND modu.pg_progetto_padre = comm.pg_progetto) ds_commessa,
          modu.cd_progetto cd_modulo, modu.ds_progetto ds_modulo,
          vs.ti_appartenenza, vs.ti_gestione, vs.cd_elemento_voce, vs.cd_voce,
          e.ds_elemento_voce, vs.esercizio_res,
          
-- STANZ_RES_INI,
          NVL (vs.im_stanz_res_improprio, 0) stanz_res_ini,
          
-- VAR_STANZ_RES_PIU,
          NVL (vs.var_piu_stanz_res_imp, 0) var_stanz_res_piu,
          
-- VAR_STANZ_RES_MENO_STO
          cnrutl002.var_stanz_res_meno
                         (vs.esercizio,
                          vs.esercizio_res,
                          vs.cd_centro_responsabilita,
                          vs.cd_linea_attivita,
                          vs.ti_appartenenza,
                          vs.ti_gestione,
                          vs.cd_voce,
                          'STO_INT'
                         ) var_stanz_res_meno_sto,
          
-- VAR_STANZ_RES_MENO_ECO
          cnrutl002.var_stanz_res_meno
                         (vs.esercizio,
                          vs.esercizio_res,
                          vs.cd_centro_responsabilita,
                          vs.cd_linea_attivita,
                          vs.ti_appartenenza,
                          vs.ti_gestione,
                          vs.cd_voce,
                          'ECO'
                         ) var_stanz_res_meno_eco,
          
-- RES_PRO_INI
          NVL (vs.im_obbl_res_pro, 0) res_pro_ini,
          
-- VAR_RES_PRO_PIU
          NVL (vs.var_piu_obbl_res_pro, 0) var_res_pro_piu,
          
-- VAR_RES_PRO_MENO
          NVL (vs.var_meno_obbl_res_pro, 0) var_res_pro_meno,
          
-- LIQUIDATO_PRO
          cnrutl002.liquidato_pro (vs.esercizio,
                                   vs.esercizio_res,
                                   vs.cd_centro_responsabilita,
                                   vs.cd_linea_attivita,
                                   vs.ti_appartenenza,
                                   vs.ti_gestione,
                                   vs.cd_voce
                                  ) liquidato_pro,
          
-- PAGATO_PRO
          NVL (im_mandati_reversali_pro, 0) pagato_pro,
          
-- RES_IMP_RIBALTATI
          cnrutl002.residui_impropri
                              (vs.esercizio,
                               vs.esercizio_res,
                               vs.cd_centro_responsabilita,
                               vs.cd_linea_attivita,
                               vs.ti_appartenenza,
                               vs.ti_gestione,
                               vs.cd_voce,
                               'Y'
                              ) res_imp_ribaltati,
          
-- RES_IMP_RIB_LIQ
          cnrutl002.residui_impropri_liq
                                (vs.esercizio,
                                 vs.esercizio_res,
                                 vs.cd_centro_responsabilita,
                                 vs.cd_linea_attivita,
                                 vs.ti_appartenenza,
                                 vs.ti_gestione,
                                 vs.cd_voce,
                                 'Y'
                                ) res_imp_rib_liq,
          
-- RES_IMP_RIB_PAG
          cnrutl002.residui_impropri_pag
                                (vs.esercizio,
                                 vs.esercizio_res,
                                 vs.cd_centro_responsabilita,
                                 vs.cd_linea_attivita,
                                 vs.ti_appartenenza,
                                 vs.ti_gestione,
                                 vs.cd_voce,
                                 'Y'
                                ) res_imp_rib_pag,
          
-- RES_IMP_EM_ESE
          cnrutl002.residui_impropri
                                 (vs.esercizio,
                                  vs.esercizio_res,
                                  vs.cd_centro_responsabilita,
                                  vs.cd_linea_attivita,
                                  vs.ti_appartenenza,
                                  vs.ti_gestione,
                                  vs.cd_voce,
                                  'N'
                                 ) res_imp_em_ese,
          
-- RES_IMP_EM_ESE_LIQ
          cnrutl002.residui_impropri_liq
                             (vs.esercizio,
                              vs.esercizio_res,
                              vs.cd_centro_responsabilita,
                              vs.cd_linea_attivita,
                              vs.ti_appartenenza,
                              vs.ti_gestione,
                              vs.cd_voce,
                              'N'
                             ) res_imp_em_ese_liq,
          
-- RES_IMP_EM_ESE_PAG
          cnrutl002.residui_impropri_pag
                             (vs.esercizio,
                              vs.esercizio_res,
                              vs.cd_centro_responsabilita,
                              vs.cd_linea_attivita,
                              vs.ti_appartenenza,
                              vs.ti_gestione,
                              vs.cd_voce,
                              'N'
                             ) res_imp_em_ese_pag,
          
-- RES_IMP_ATTUALI
          NVL (vs.im_obbl_res_imp, 0) res_imp_attuali,
-- VINCOLI
          cnrutl002.im_vincoli
                                (vs.esercizio,
                                 vs.esercizio_res,
                                 vs.cd_centro_responsabilita,
                                 vs.cd_linea_attivita,
                                 vs.ti_appartenenza,
                                 vs.ti_gestione,
                                 vs.cd_voce
                                ) res_vincoli
     FROM voce_f_saldi_cdr_linea vs,
          v_linea_attivita_valida la,
          progetto_gest modu,
          cdr,
          elemento_voce e,
          parametri_cnr
    WHERE parametri_cnr.esercizio = vs.esercizio
      AND parametri_cnr.fl_nuovo_pdg = 'Y'
      AND vs.esercizio > esercizio_res
      AND vs.ti_gestione = 'S'
      AND vs.esercizio = la.esercizio
      AND vs.cd_centro_responsabilita = la.cd_centro_responsabilita
      AND vs.cd_linea_attivita = la.cd_linea_attivita
      AND la.pg_progetto = modu.pg_progetto
      AND modu.esercizio = vs.esercizio
      AND vs.esercizio = e.esercizio
      AND vs.ti_appartenenza = e.ti_appartenenza
      AND vs.ti_gestione = e.ti_gestione
      AND vs.cd_voce = e.cd_elemento_voce
      AND vs.cd_centro_responsabilita = cdr.cd_centro_responsabilita ;
