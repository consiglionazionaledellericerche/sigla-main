--------------------------------------------------------
--  DDL for View V_SIT_ANALITICA_GAE_RESIDUI
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_SIT_ANALITICA_GAE_RESIDUI" ("ESERCIZIO_ORIGINALE", "CDS", "UO", "CD_CENTRO_RESPONSABILITA", "DS_CDR", "CD_LINEA_ATTIVITA", "DENOMINAZIONE", "CD_NATURA", "CD_PROGETTO", "DS_PROGETTO", "CD_COMMESSA", "DS_COMMESSA", "CD_MODULO", "DS_MODULO", "TI_APPARTENENZA", "TI_GESTIONE", "CD_ELEMENTO_VOCE", "CD_VOCE", "DS_ELEMENTO_VOCE", "IM_STANZ_INIZIALE_A1", "VARIAZIONI_PIU", "VARIAZIONI_MENO", "ASSESTATO_COMP", "IM_OBBL_ACC_COMP", "DA_ASSUMERE", "IM_ASS_DOC_AMM_SPE", "IM_ASS_DOC_AMM_ETR", "IM_MANDATI_REVERSALI_PRO", "DA_PAGARE_INCASSARE", "ESERCIZIO") AS 
  SELECT
--
-- Date: 09/11/2006
-- Version: 1.1
--
-- Vista ANALITICA GAE
--
-- History:
--
-- Date: 18/07/2006
-- Version: 1.0
-- Gestione Impegni/Accertamenti Residui:
-- gestito il nuovo campo ESERCIZIO_ORIGINALE
--
-- Date: 09/11/2006
-- Version: 1.1
-- Aggiunta la selezione del progetto/commessa/modulo per anno
--
-- Body:
--
          vs.esercizio_res,
          SUBSTR (cnrutl001.getcdsfromcdr (vs.cd_centro_responsabilita),
                  1,
                  50
                 ) cds,
          SUBSTR (cnrctb020.getcduo (vs.cd_centro_responsabilita), 1, 50) uo,
          vs.cd_centro_responsabilita, cdr.ds_cdr, vs.cd_linea_attivita,
          DECODE (la.denominazione,
                  NULL, DECODE (la.ds_linea_attivita,
                                NULL, 'NESSUNA DESCRIZIONE',
                                la.ds_linea_attivita
                               ),
                  la.denominazione
                 ),
          la.cd_natura, prog.cd_progetto, prog.ds_progetto,
          comm.cd_progetto cd_commessa, comm.ds_progetto ds_commessa,
          modu.cd_progetto cd_modulo, modu.ds_progetto ds_modulo,
          vs.ti_appartenenza, vs.ti_gestione, vs.cd_elemento_voce, vs.cd_voce,
          e.ds_elemento_voce,
          im_stanz_res_improprio + NVL (im_obbl_res_pro, 0),
            NVL (var_piu_stanz_res_imp, 0)
          + NVL (var_meno_obbl_res_pro, 0) variazioni_piu,
            NVL (var_meno_stanz_res_imp, 0)
          + NVL (var_piu_obbl_res_pro, 0) variazioni_meno,
            NVL (im_stanz_res_improprio, 0)
          + NVL (im_obbl_res_pro, 0)
          + NVL (var_piu_stanz_res_imp, 0)
          + NVL (var_meno_obbl_res_pro, 0)
          - (NVL (var_meno_stanz_res_imp, 0) + NVL (var_piu_obbl_res_pro, 0)),
            NVL (im_obbl_res_pro, 0)
          + NVL (var_piu_obbl_res_pro, 0)
          - NVL (var_meno_obbl_res_pro, 0)
          + NVL (im_obbl_res_imp, 0),                       --IMP_OBB_ACC_COMP
            NVL (im_stanz_res_improprio, 0)
          + NVL (im_obbl_res_pro, 0)
          + NVL (var_piu_stanz_res_imp, 0)
          + NVL (var_meno_obbl_res_pro, 0)
          - NVL (var_meno_stanz_res_imp, 0)
          - NVL (var_piu_obbl_res_pro, 0)
          - (  NVL (im_obbl_res_pro, 0)
             + NVL (var_piu_obbl_res_pro, 0)
             - NVL (var_meno_obbl_res_pro, 0)
             + NVL (im_obbl_res_imp, 0)
            ),
          NVL
             ((SELECT SUM (im_voce)
                 FROM obbligazione_scad_voce osv,
                      obbligazione o,
                      obbligazione_scadenzario os
                WHERE osv.esercizio = vs.esercizio
                  AND osv.ti_appartenenza = vs.ti_appartenenza
                  AND osv.ti_gestione = vs.ti_gestione
                  AND osv.cd_voce = vs.cd_voce
                  AND osv.cd_centro_responsabilita =
                                                   vs.cd_centro_responsabilita
                  AND osv.cd_linea_attivita = vs.cd_linea_attivita
                  AND osv.cd_cds = os.cd_cds
                  AND osv.esercizio = os.esercizio
                  AND osv.esercizio_originale = os.esercizio_originale
                  AND osv.pg_obbligazione = os.pg_obbligazione
                  AND osv.pg_obbligazione_scadenzario =
                                                os.pg_obbligazione_scadenzario
                  AND osv.cd_cds = o.cd_cds
                  AND osv.esercizio = o.esercizio
                  AND osv.esercizio_originale = o.esercizio_originale
                  AND osv.pg_obbligazione = o.pg_obbligazione
                  AND o.esercizio != o.esercizio_originale
                  AND os.im_associato_doc_amm = os.im_scadenza),
              0
             ) im_ass_doc_amm_spe,
          NVL
             ((SELECT SUM (im_voce)
                 FROM accertamento_scad_voce osv,
                      accertamento o,
                      accertamento_scadenzario os
                WHERE o.esercizio = vs.esercizio
                  AND o.ti_appartenenza = vs.ti_appartenenza
                  AND o.ti_gestione = vs.ti_gestione
                  AND o.cd_voce = vs.cd_voce
                  AND o.esercizio != o.esercizio_originale
                  AND osv.cd_centro_responsabilita =
                                                   vs.cd_centro_responsabilita
                  AND osv.cd_linea_attivita = vs.cd_linea_attivita
                  AND osv.cd_cds = os.cd_cds
                  AND osv.esercizio = os.esercizio
                  AND osv.esercizio_originale = os.esercizio_originale
                  AND osv.pg_accertamento = os.pg_accertamento
                  AND osv.pg_accertamento_scadenzario =
                                                os.pg_accertamento_scadenzario
                  AND osv.cd_cds = o.cd_cds
                  AND osv.esercizio = o.esercizio
                  AND osv.esercizio_originale = o.esercizio_originale
                  AND osv.pg_accertamento = o.pg_accertamento
                  AND os.im_associato_doc_amm = os.im_scadenza),
              0
             ) im_ass_doc_amm_etr,
          im_mandati_reversali_pro,
            NVL (im_obbl_res_pro, 0)
          + NVL (var_piu_obbl_res_pro, 0)
          - NVL (var_meno_obbl_res_pro, 0)
          + NVL (im_obbl_res_imp, 0)
          - NVL (im_mandati_reversali_pro, 0),
          vs.esercizio
     FROM voce_f_saldi_cdr_linea vs,
          linea_attivita la,
          cdr,
          elemento_voce e,
          progetto_gest modu,
          progetto_gest comm,
          progetto_gest prog,
          parametri_cnr
    WHERE parametri_cnr.esercizio = vs.esercizio
      AND parametri_cnr.fl_nuovo_pdg = 'N'
      And vs.esercizio != esercizio_res
      AND vs.cd_centro_responsabilita = la.cd_centro_responsabilita
      AND vs.cd_linea_attivita = la.cd_linea_attivita
      AND modu.esercizio = vs.esercizio
      AND modu.pg_progetto = la.pg_progetto
      AND modu.tipo_fase = 'G'
      AND modu.esercizio_progetto_padre = comm.esercizio
      AND modu.pg_progetto_padre = comm.pg_progetto
      AND modu.tipo_fase_progetto_padre = comm.tipo_fase
      AND comm.esercizio_progetto_padre = prog.esercizio
      AND comm.pg_progetto_padre = prog.pg_progetto
      AND comm.tipo_fase_progetto_padre = prog.tipo_fase
      AND cdr.cd_centro_responsabilita = vs.cd_centro_responsabilita
      AND e.esercizio = vs.esercizio
      AND e.ti_appartenenza = vs.ti_appartenenza
      AND e.ti_gestione = vs.ti_gestione
      AND e.cd_elemento_voce = vs.cd_elemento_voce
Union All
   SELECT
          vs.esercizio_res,
          SUBSTR (cnrutl001.getcdsfromcdr (vs.cd_centro_responsabilita),
                  1,
                  50
                 ) cds,
          SUBSTR (cnrctb020.getcduo (vs.cd_centro_responsabilita), 1, 50) uo,
          vs.cd_centro_responsabilita, cdr.ds_cdr, vs.cd_linea_attivita,
          DECODE (la.denominazione,
                  NULL, DECODE (la.ds_linea_attivita,
                                NULL, 'NESSUNA DESCRIZIONE',
                                la.ds_linea_attivita
                               ),
                  la.denominazione
                 ),
          la.cd_natura, Null cd_progetto, Null ds_progetto,
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
          e.ds_elemento_voce,
          im_stanz_res_improprio + NVL (im_obbl_res_pro, 0),
            NVL (var_piu_stanz_res_imp, 0)
          + NVL (var_meno_obbl_res_pro, 0) variazioni_piu,
            NVL (var_meno_stanz_res_imp, 0)
          + NVL (var_piu_obbl_res_pro, 0) variazioni_meno,
            NVL (im_stanz_res_improprio, 0)
          + NVL (im_obbl_res_pro, 0)
          + NVL (var_piu_stanz_res_imp, 0)
          + NVL (var_meno_obbl_res_pro, 0)
          - (NVL (var_meno_stanz_res_imp, 0) + NVL (var_piu_obbl_res_pro, 0)),
            NVL (im_obbl_res_pro, 0)
          + NVL (var_piu_obbl_res_pro, 0)
          - NVL (var_meno_obbl_res_pro, 0)
          + NVL (im_obbl_res_imp, 0),                       --IMP_OBB_ACC_COMP
            NVL (im_stanz_res_improprio, 0)
          + NVL (im_obbl_res_pro, 0)
          + NVL (var_piu_stanz_res_imp, 0)
          + NVL (var_meno_obbl_res_pro, 0)
          - NVL (var_meno_stanz_res_imp, 0)
          - NVL (var_piu_obbl_res_pro, 0)
          - (  NVL (im_obbl_res_pro, 0)
             + NVL (var_piu_obbl_res_pro, 0)
             - NVL (var_meno_obbl_res_pro, 0)
             + NVL (im_obbl_res_imp, 0)
            ),
          NVL
             ((SELECT SUM (im_voce)
                 FROM obbligazione_scad_voce osv,
                      obbligazione o,
                      obbligazione_scadenzario os
                WHERE osv.esercizio = vs.esercizio
                  AND osv.ti_appartenenza = vs.ti_appartenenza
                  AND osv.ti_gestione = vs.ti_gestione
                  AND osv.cd_voce = vs.cd_voce
                  AND osv.cd_centro_responsabilita =
                                                   vs.cd_centro_responsabilita
                  AND osv.cd_linea_attivita = vs.cd_linea_attivita
                  AND osv.cd_cds = os.cd_cds
                  AND osv.esercizio = os.esercizio
                  AND osv.esercizio_originale = os.esercizio_originale
                  AND osv.pg_obbligazione = os.pg_obbligazione
                  AND osv.pg_obbligazione_scadenzario =
                                                os.pg_obbligazione_scadenzario
                  AND osv.cd_cds = o.cd_cds
                  AND osv.esercizio = o.esercizio
                  AND osv.esercizio_originale = o.esercizio_originale
                  AND osv.pg_obbligazione = o.pg_obbligazione
                  AND o.esercizio != o.esercizio_originale
                  AND os.im_associato_doc_amm = os.im_scadenza),
              0
             ) im_ass_doc_amm_spe,
          NVL
             ((SELECT SUM (im_voce)
                 FROM accertamento_scad_voce osv,
                      accertamento o,
                      accertamento_scadenzario os
                WHERE o.esercizio = vs.esercizio
                  AND o.ti_appartenenza = vs.ti_appartenenza
                  AND o.ti_gestione = vs.ti_gestione
                  AND o.cd_voce = vs.cd_voce
                  AND o.esercizio != o.esercizio_originale
                  AND osv.cd_centro_responsabilita =
                                                   vs.cd_centro_responsabilita
                  AND osv.cd_linea_attivita = vs.cd_linea_attivita
                  AND osv.cd_cds = os.cd_cds
                  AND osv.esercizio = os.esercizio
                  AND osv.esercizio_originale = os.esercizio_originale
                  AND osv.pg_accertamento = os.pg_accertamento
                  AND osv.pg_accertamento_scadenzario =
                                                os.pg_accertamento_scadenzario
                  AND osv.cd_cds = o.cd_cds
                  AND osv.esercizio = o.esercizio
                  AND osv.esercizio_originale = o.esercizio_originale
                  AND osv.pg_accertamento = o.pg_accertamento
                  AND os.im_associato_doc_amm = os.im_scadenza),
              0
             ) im_ass_doc_amm_etr,
          im_mandati_reversali_pro,
            NVL (im_obbl_res_pro, 0)
          + NVL (var_piu_obbl_res_pro, 0)
          - NVL (var_meno_obbl_res_pro, 0)
          + NVL (im_obbl_res_imp, 0)
          - NVL (im_mandati_reversali_pro, 0),
          vs.esercizio
     FROM voce_f_saldi_cdr_linea vs,
          v_linea_attivita_valida la,
          cdr,
          elemento_voce e,
          progetto_gest modu,
          parametri_cnr
    WHERE parametri_cnr.esercizio = vs.esercizio
      AND parametri_cnr.fl_nuovo_pdg = 'Y'
      AND vs.esercizio != esercizio_res
      AND vs.esercizio = la.esercizio
      AND vs.cd_centro_responsabilita = la.cd_centro_responsabilita
      AND vs.cd_linea_attivita = la.cd_linea_attivita
      AND modu.esercizio = vs.esercizio
      AND modu.pg_progetto = la.pg_progetto
      AND modu.tipo_fase = 'G'
      AND cdr.cd_centro_responsabilita = vs.cd_centro_responsabilita
      AND e.esercizio = vs.esercizio
      AND e.ti_appartenenza = vs.ti_appartenenza
      AND e.ti_gestione = vs.ti_gestione
      AND e.cd_elemento_voce = vs.cd_elemento_voce ;
