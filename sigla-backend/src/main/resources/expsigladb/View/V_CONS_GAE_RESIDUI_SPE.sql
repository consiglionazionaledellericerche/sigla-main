--------------------------------------------------------
--  DDL for View V_CONS_GAE_RESIDUI_SPE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_CONS_GAE_RESIDUI_SPE" ("ESERCIZIO", "ESERCIZIO_RES", "CD_DIPARTIMENTO", "PG_PROGETTO", "CD_PROGETTO", "DS_PROGETTO", "PG_COMMESSA", "CD_COMMESSA", "DS_COMMESSA", "PG_MODULO", "CD_MODULO", "DS_MODULO", "CD_CENTRO_RESPONSABILITA", "CD_CDS", "CD_LINEA_ATTIVITA", "DS_LINEA_ATTIVITA", "TI_APPARTENENZA", "TI_GESTIONE", "CD_ELEMENTO_VOCE", "DS_ELEMENTO_VOCE", "IM_STANZ_RES_IMPROPRIO", "PG_VAR_ST_RES", "DS_VAR_ST_RES", "VAR_PIU_STANZ_RES_IMP", "VAR_MENO_STANZ_RES_IMP", "CD_CDS_OBB", "PG_OBBLIGAZIONE", "PG_OBBLIGAZIONE_SCADENZARIO", "CD_TIPO_DOCUMENTO_CONT", "DS_SCADENZA", "IM_OBBL_RES_IMP", "IM_OBBL_RES_PRO", "PG_VAR_RES_PRO", "DS_VAR_RES_PRO", "VAR_PIU_OBBL_RES_PRO", "VAR_MENO_OBBL_RES_PRO", "CD_CDS_MAN", "PG_MANDATO", "DS_MANDATO", "IM_MANDATI_REVERSALI_PRO", "IM_MANDATI_REVERSALI_IMP") AS 
  SELECT
--
-- Date: 23/10/2008
-- Version: 1.1
--
-- Vista per la consultazione GAE Residui Spese
--
-- History:
-- Date: ?
-- Version: 1.0
-- Creazione
--
-- Date: 23/10/2008
-- Version: 1.1
-- Aggiunto il campo CD_CDS
-- Body:
--
          saldi.esercizio, saldi.esercizio_res, progetto.cd_dipartimento,
          progetto.pg_progetto, progetto.cd_progetto, progetto.ds_progetto,
          com.pg_progetto, com.cd_progetto, com.ds_progetto, modu.pg_progetto,
          modu.cd_progetto, modu.ds_progetto, saldi.cd_centro_responsabilita,
          cnrutl001.getcdsfromcdr (saldi.cd_centro_responsabilita) cd_cds,
          saldi.cd_linea_attivita, denominazione, saldi.ti_appartenenza,
          saldi.ti_gestione, saldi.cd_elemento_voce,
          (SELECT ds_elemento_voce
             FROM elemento_voce
            WHERE esercizio = saldi.esercizio
              AND ti_appartenenza = saldi.ti_appartenenza
              AND ti_gestione = saldi.ti_gestione
              AND cd_elemento_voce = saldi.cd_elemento_voce) ds_elemento_voce,
          saldi.im_stanz_res_improprio, saldi.pg_var_st_res,
          saldi.ds_var_st_res, saldi.var_piu_st_res, saldi.var_meno_st_res,
          saldi.cds_obb, saldi.pg_obb, saldi.pg_obb_scad,
          saldi.cd_tipo_documento_cont, saldi.ds_scadenza,
          saldi.im_obbl_res_imp, saldi.im_obbl_res_pro, saldi.pg_var_res_pro,
          saldi.ds_var_res_pro, saldi.var_piu_obb_res_pro,
          saldi.var_meno_obb_res_pro, saldi.cds_man, saldi.pg_man,
          saldi.ds_mandato, saldi.pagato_res_proprio,
          saldi.pagato_res_improprio
     FROM linea_attivita,
          progetto_gest progetto,
          progetto_gest com,
          progetto_gest modu,
          v_cons_saldi_residui_spe saldi,
          parametri_cnr
    WHERE parametri_cnr.esercizio = saldi.esercizio
      AND parametri_cnr.fl_nuovo_pdg = 'N'
      AND saldi.cd_centro_responsabilita =
                                       linea_attivita.cd_centro_responsabilita
      AND saldi.cd_linea_attivita = linea_attivita.cd_linea_attivita  -- LINEA
      AND linea_attivita.pg_progetto IS NOT NULL                 -- CON MODULO
      AND modu.esercizio = saldi.esercizio_res
      AND modu.pg_progetto = linea_attivita.pg_progetto             -- SOLO PG
      AND modu.tipo_fase = 'G'
      AND modu.esercizio_progetto_padre = com.esercizio
      AND modu.pg_progetto_padre = com.pg_progetto
      AND modu.tipo_fase_progetto_padre = com.tipo_fase
      AND com.esercizio_progetto_padre = progetto.esercizio
      AND com.pg_progetto_padre = progetto.pg_progetto
      AND com.tipo_fase_progetto_padre = progetto.tipo_fase
   UNION ALL
   SELECT
          saldi.esercizio, saldi.esercizio_res, 
          (SELECT progetto_gest.cd_dipartimento
             FROM progetto_gest
            WHERE progetto_gest.esercizio = area_prog.esercizio_progetto_padre
              AND progetto_gest.pg_progetto = area_prog.pg_progetto_padre) cd_dipartimento,
          null,null,null,
          (SELECT progetto_gest.pg_progetto
             FROM progetto_gest
            WHERE progetto_gest.esercizio = area_prog.esercizio_progetto_padre
              AND progetto_gest.pg_progetto = area_prog.pg_progetto_padre),
          (SELECT progetto_gest.cd_progetto
             FROM progetto_gest
            WHERE progetto_gest.esercizio = area_prog.esercizio_progetto_padre
              AND progetto_gest.pg_progetto = area_prog.pg_progetto_padre),
          (SELECT progetto_gest.ds_progetto
             FROM progetto_gest
            WHERE progetto_gest.esercizio = area_prog.esercizio_progetto_padre
              AND progetto_gest.pg_progetto = area_prog.pg_progetto_padre),
          area_prog.pg_progetto, area_prog.cd_progetto, area_prog.ds_progetto, 
          saldi.cd_centro_responsabilita,
          cnrutl001.getcdsfromcdr (saldi.cd_centro_responsabilita) cd_cds,
          saldi.cd_linea_attivita, denominazione, saldi.ti_appartenenza,
          saldi.ti_gestione, saldi.cd_elemento_voce,
          (SELECT ds_elemento_voce
             FROM elemento_voce
            WHERE esercizio = saldi.esercizio
              AND ti_appartenenza = saldi.ti_appartenenza
              AND ti_gestione = saldi.ti_gestione
              AND cd_elemento_voce = saldi.cd_elemento_voce) ds_elemento_voce,
          saldi.im_stanz_res_improprio, saldi.pg_var_st_res,
          saldi.ds_var_st_res, saldi.var_piu_st_res, saldi.var_meno_st_res,
          saldi.cds_obb, saldi.pg_obb, saldi.pg_obb_scad,
          saldi.cd_tipo_documento_cont, saldi.ds_scadenza,
          saldi.im_obbl_res_imp, saldi.im_obbl_res_pro, saldi.pg_var_res_pro,
          saldi.ds_var_res_pro, saldi.var_piu_obb_res_pro,
          saldi.var_meno_obb_res_pro, saldi.cds_man, saldi.pg_man,
          saldi.ds_mandato, saldi.pagato_res_proprio,
          saldi.pagato_res_improprio
     FROM v_linea_attivita_valida,
          progetto_gest area_prog,
          v_cons_saldi_residui_spe saldi,
          parametri_cnr
    WHERE parametri_cnr.esercizio = saldi.esercizio
      AND parametri_cnr.fl_nuovo_pdg = 'Y'
      AND saldi.esercizio = v_linea_attivita_valida.esercizio
      AND saldi.cd_centro_responsabilita = v_linea_attivita_valida.cd_centro_responsabilita
      AND saldi.cd_linea_attivita = v_linea_attivita_valida.cd_linea_attivita  -- LINEA
      AND v_linea_attivita_valida.pg_progetto IS NOT NULL                 -- CON MODULO
      AND area_prog.esercizio = saldi.esercizio
      AND area_prog.pg_progetto = v_linea_attivita_valida.pg_progetto ;
