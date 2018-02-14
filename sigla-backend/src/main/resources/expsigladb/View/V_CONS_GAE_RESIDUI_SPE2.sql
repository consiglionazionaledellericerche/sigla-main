--------------------------------------------------------
--  DDL for View V_CONS_GAE_RESIDUI_SPE2
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_CONS_GAE_RESIDUI_SPE2" ("ESERCIZIO", "ESERCIZIO_RES", "CD_DIPARTIMENTO", "PG_PROGETTO", "CD_PROGETTO", "DS_PROGETTO", "PG_COMMESSA", "CD_COMMESSA", "DS_COMMESSA", "PG_MODULO", "CD_MODULO", "DS_MODULO", "CD_CENTRO_RESPONSABILITA", "CD_CDS", "CD_LINEA_ATTIVITA", "DS_LINEA_ATTIVITA", "TI_APPARTENENZA", "TI_GESTIONE", "CD_ELEMENTO_VOCE", "DS_ELEMENTO_VOCE", "IM_STANZ_RES_IMPROPRIO", "PG_VAR_ST_RES", "DS_VAR_ST_RES", "VAR_PIU_STANZ_RES_IMP", "VAR_MENO_STANZ_RES_IMP", "CD_CDS_OBB", "PG_OBBLIGAZIONE", "PG_OBBLIGAZIONE_SCADENZARIO", "CD_TIPO_DOCUMENTO_CONT", "DS_SCADENZA", "IM_OBBL_RES_IMP", "IM_OBBL_RES_PRO", "PG_VAR_RES_PRO", "DS_VAR_RES_PRO", "VAR_PIU_OBBL_RES_PRO", "VAR_MENO_OBBL_RES_PRO", "CD_CDS_MAN", "PG_MANDATO", "DS_MANDATO", "IM_MANDATI_REVERSALI_PRO", "IM_MANDATI_REVERSALI_IMP") AS 
  SELECT /*+ optimizer_features_enable('10.1.0') */
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
          unita_organizzativa.cd_unita_padre cd_cds,
                                                    --cnrutl001.getcdsfromcdr (saldi.cd_centro_responsabilita) cd_cds,
                                                    saldi.cd_linea_attivita,
          denominazione, saldi.ti_appartenenza, saldi.ti_gestione,
          saldi.cd_elemento_voce,
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
          cdr,
          unita_organizzativa,
          (                                            -- stanziamento residuo
           SELECT vfs.esercizio, vfs.esercizio_res,
                  vfs.cd_centro_responsabilita,
                                               --cnrutl001.getcdsfromcdr (vfs.cd_centro_responsabilita) cd_cds,
                                               vfs.cd_linea_attivita,
                  vfs.ti_appartenenza, vfs.ti_gestione, vfs.cd_elemento_voce,
                  vfs.im_stanz_res_improprio, NULL pg_var_st_res,
                  NULL ds_var_st_res, 0 var_piu_st_res, 0 var_meno_st_res,
                  NULL cds_obb, NULL pg_obb, NULL pg_obb_scad,
                  NULL cd_tipo_documento_cont, NULL ds_scadenza,
                  0 im_obbl_res_imp, 0 im_obbl_res_pro, NULL pg_var_res_pro,
                  NULL ds_var_res_pro, 0 var_piu_obb_res_pro,
                  0 var_meno_obb_res_pro, NULL cds_man, NULL pg_man,
                  NULL ds_mandato, 0 pagato_res_proprio,
                  0 pagato_res_improprio
             FROM voce_f_saldi_cdr_linea vfs
            WHERE vfs.esercizio > vfs.esercizio_res
              AND vfs.ti_gestione = 'S'
              AND vfs.im_stanz_res_improprio != 0
           UNION ALL
           -- VARIAZIONI PIU' STANZ RES
           SELECT t.esercizio, t.esercizio_res, d.cd_cdr,
                                                         --cnrutl001.getcdsfromcdr (d.cd_cdr) cd_cds,
                                                         d.cd_linea_attivita,
                  d.ti_appartenenza, d.ti_gestione, d.cd_elemento_voce, 0,
                  
                  -- IM_STANZ_RES_IMPROPRIO,
                  t.pg_variazione,                            -- PG_VAR_ST_RES
                                  t.ds_variazione,
                  
                  -- DS_VAR_ST_RES
                  DECODE (ABS (NVL (d.im_variazione, 0)),
                          NVL (d.im_variazione, 0), NVL (d.im_variazione, 0),
                          0
                         ),                                  -- VAR_PIU_ST_RES
                  0,                                        -- VAR_MENO_ST_RES
                    NULL,                                           -- CDS_OBB
                         NULL,                                       -- PG_OBB
                              NULL,                             -- PG_OBB_SCAD
                                   NULL,             -- CD_TIPO_DOCUMENTO_CONT
                                        NULL,                   -- DS_SCADENZA
                                             0,             -- IM_OBBL_RES_IMP
                                               0,           -- IM_OBBL_RES_PRO
                                                 NULL,       -- PG_VAR_RES_PRO
                                                      NULL,  -- DS_VAR_RES_PRO
                                                           0,
                                                             -- VAR_PIU_OBB_RES_PRO
                  0,
                    -- VAR_MENO_OBB_RES_PRO
                  NULL,
                       -- CDS_MAN
                  NULL,
                       -- PG_MAN
                  NULL,                                          -- DS_MANDATO
                       0,                                -- PAGATO_RES_PROPRIO
                         0                             -- PAGATO_RES_IMPROPRIO
             FROM var_stanz_res t, var_stanz_res_riga d
            WHERE t.esercizio = d.esercizio
              AND t.pg_variazione = d.pg_variazione
              AND t.stato = 'APP'
              AND d.im_variazione > 0
           UNION ALL
           -- VARIAZIONI MENO STANZ RES
           SELECT t.esercizio, t.esercizio_res, d.cd_cdr,
                                                         --cnrutl001.getcdsfromcdr (d.cd_cdr) cd_cds,
                                                         d.cd_linea_attivita,
                  d.ti_appartenenza, d.ti_gestione, d.cd_elemento_voce, 0,
                  
                  -- IM_STANZ_RES_IMPROPRIO,
                  t.pg_variazione,                            -- PG_VAR_ST_RES
                                  t.ds_variazione,
                                                  -- DS_VAR_ST_RES
                  0,
                  
                  -- VAR_PIU_ST_RES
                  DECODE (ABS (NVL (d.im_variazione, 0)),
                          NVL (d.im_variazione, 0), 0,
                          ABS (NVL (d.im_variazione, 0))
                         ),                                 -- VAR_MENO_ST_RES
                  NULL,                                             -- CDS_OBB
                       NULL,                                         -- PG_OBB
                            NULL,                               -- PG_OBB_SCAD
                                 NULL,               -- CD_TIPO_DOCUMENTO_CONT
                                      NULL,                     -- DS_SCADENZA
                                           0,               -- IM_OBBL_RES_IMP
                                             0,             -- IM_OBBL_RES_PRO
                                               NULL,         -- PG_VAR_RES_PRO
                                                    NULL,    -- DS_VAR_RES_PRO
                                                         0,
                                                           -- VAR_PIU_OBB_RES_PRO
                  0,
                    -- VAR_MENO_OBB_RES_PRO
                  NULL,                                             -- CDS_MAN
                       NULL,
                            -- PG_MAN
                  NULL,
                       -- DS_MANDATO
                  0,                                     -- PAGATO_RES_PROPRIO
                    0                                  -- PAGATO_RES_IMPROPRIO
             FROM var_stanz_res t, var_stanz_res_riga d
            WHERE t.esercizio = d.esercizio
              AND t.pg_variazione = d.pg_variazione
              AND t.stato = 'APP'
              AND d.im_variazione < 0
           UNION ALL
           -- residui impropri
           SELECT o.esercizio, o.esercizio_originale,
                  osv.cd_centro_responsabilita,
                                               --cnrutl001.getcdsfromcdr(osv.cd_centro_responsabilita) cd_cds,
                                               osv.cd_linea_attivita,
                  o.ti_appartenenza, o.ti_gestione, o.cd_elemento_voce, 0,
                  
                  -- IM_STANZ_RES_IMPROPRIO,
                  NULL,                                       -- PG_VAR_ST_RES
                       NULL,                                  -- DS_VAR_ST_RES
                            0,                               -- VAR_PIU_ST_RES
                              0,                            -- VAR_MENO_ST_RES
                                osv.cd_cds,                         -- CDS_OBB
                                           osv.pg_obbligazione,      -- PG_OBB
                  osv.pg_obbligazione_scadenzario,
                                                  -- PG_OBB_SCAD
                                                  o.cd_tipo_documento_cont,
                  
                  -- CD_TIPO_DOCUMENTO_CONT
                  os.ds_scadenza,                               -- DS_SCADENZA
                                 osv.im_voce,
                                             -- IM_OBBL_RES_IMP
                  0,
                    -- IM_OBBL_RES_PRO
                  NULL,
                       -- PG_VAR_RES_PRO
                  NULL,                                      -- DS_VAR_RES_PRO
                       0,                               -- VAR_PIU_OBB_RES_PRO
                         0,                            -- VAR_MENO_OBB_RES_PRO
                           NULL,                                    -- CDS_MAN
                                NULL,                                -- PG_MAN
                                     NULL,                       -- DS_MANDATO
                                          0,             -- PAGATO_RES_PROPRIO
                                            0          -- PAGATO_RES_IMPROPRIO
             FROM obbligazione o,
                  obbligazione_scadenzario os,
                  obbligazione_scad_voce osv
            WHERE osv.cd_cds = os.cd_cds
              AND osv.esercizio = os.esercizio
              AND osv.esercizio_originale = os.esercizio_originale
              AND osv.pg_obbligazione = os.pg_obbligazione
              AND osv.pg_obbligazione_scadenzario =
                                                os.pg_obbligazione_scadenzario
              AND os.cd_cds = o.cd_cds
              AND os.esercizio = o.esercizio
              AND os.esercizio_originale = o.esercizio_originale
              AND os.pg_obbligazione = o.pg_obbligazione
              AND o.cd_tipo_documento_cont = 'OBB_RESIM'
              AND o.pg_obbligazione > 0
-- sf 16.03.2009 evita le righe a zero
              AND osv.im_voce > 0
           UNION ALL
           -- residui propri
           SELECT o.esercizio, o.esercizio_originale,
                  osv.cd_centro_responsabilita,
                                               --cnrutl001.getcdsfromcdr (osv.cd_centro_responsabilita) cd_cds,
                                               osv.cd_linea_attivita,
                  o.ti_appartenenza, o.ti_gestione, o.cd_elemento_voce, 0,
                  
                  -- IM_STANZ_RES_IMPROPRIO,
                  NULL,                                       -- PG_VAR_ST_RES
                       NULL,                                  -- DS_VAR_ST_RES
                            0,                               -- VAR_PIU_ST_RES
                              0,                            -- VAR_MENO_ST_RES
                                osv.cd_cds,                         -- CDS_OBB
                                           osv.pg_obbligazione,      -- PG_OBB
                  osv.pg_obbligazione_scadenzario,
                                                  -- PG_OBB_SCAD
                                                  o.cd_tipo_documento_cont,
                  
                  -- CD_TIPO_DOCUMENTO_CONT
                  os.ds_scadenza,                               -- DS_SCADENZA
                                 0,
                                   -- IM_OBBL_RES_IMP
                                   osv.im_voce,
                                               -- IM_OBBL_RES_PRO   I N C L U S E    L E    V A R I A Z I O N I
                  NULL,
                       -- PG_VAR_RES_PRO
                  NULL,                                      -- DS_VAR_RES_PRO
                       0,                               -- VAR_PIU_OBB_RES_PRO
                         0,                            -- VAR_MENO_OBB_RES_PRO
                           NULL,                                    -- CDS_MAN
                                NULL,                                -- PG_MAN
                                     NULL,                       -- DS_MANDATO
                                          0,             -- PAGATO_RES_PROPRIO
                                            0          -- PAGATO_RES_IMPROPRIO
             FROM obbligazione o,
                  obbligazione_scadenzario os,
                  obbligazione_scad_voce osv
            WHERE osv.cd_cds = os.cd_cds
              AND osv.esercizio = os.esercizio
              AND osv.esercizio_originale = os.esercizio_originale
              AND osv.pg_obbligazione = os.pg_obbligazione
              AND osv.pg_obbligazione_scadenzario =
                                                os.pg_obbligazione_scadenzario
              AND osv.cd_cds = o.cd_cds
              AND osv.esercizio = o.esercizio
              AND osv.esercizio_originale = o.esercizio_originale
              AND osv.pg_obbligazione = o.pg_obbligazione
              AND o.cd_tipo_documento_cont IN
                                         ('OBB_RES', 'OBB_PGIR_R', 'IMP_RES')
              AND o.pg_obbligazione > 0
-- sf 16.03.2009 evita le righe a zero
              AND osv.im_voce > 0
           UNION ALL
           -- VARIAZIONI IN + RESIDUI PROPRI
           SELECT o.esercizio, o.esercizio_originale,
                  omv.cd_centro_responsabilita,
                                               --cnrutl001.getcdsfromcdr (omv.cd_centro_responsabilita) cd_cds,
                                               omv.cd_linea_attivita,
                  o.ti_appartenenza, o.ti_gestione, o.cd_elemento_voce, 0,
                  
                  -- IM_STANZ_RES_IMPROPRIO,
                  NULL,                                       -- PG_VAR_ST_RES
                       NULL,                                  -- DS_VAR_ST_RES
                            0,                               -- VAR_PIU_ST_RES
                              0,                            -- VAR_MENO_ST_RES
                                o.cd_cds,                           -- CDS_OBB
                                         o.pg_obbligazione,          -- PG_OBB
                                                           NULL,
                  
                  -- PG_OBB_SCAD
                  o.cd_tipo_documento_cont,
                                           -- CD_TIPO_DOCUMENTO_CONT
                                           o.ds_obbligazione,
                                                             -- DS_OBBLIGAZIONE
                  0,
                    -- IM_OBBL_RES_IMP
                  0,
                    -- IM_OBBL_RES_PRO
                    om.pg_modifica,                          -- PG_VAR_RES_PRO
                                   om.ds_modifica,
                                                  -- DS_VAR_RES_PRO
                                                  omv.im_modifica,
                                                                  -- VAR_PIU_OBB_RES_PRO
                  0,
                    -- VAR_MENO_OBB_RES_PRO
                  NULL,
                       -- CDS_MAN
                  NULL,
                       -- PG_MAN
                  NULL,                                          -- DS_MANDATO
                       0,                                -- PAGATO_RES_PROPRIO
                         0                             -- PAGATO_RES_IMPROPRIO
             FROM obbligazione o,
                  obbligazione_mod_voce omv,
                  obbligazione_modifica om
            WHERE o.cd_tipo_documento_cont IN
                                         ('OBB_RES', 'OBB_PGIR_R', 'IMP_RES')
              AND o.pg_obbligazione > 0
              AND om.cd_cds = o.cd_cds
              AND om.esercizio = o.esercizio
              AND om.esercizio_originale = o.esercizio_originale
              AND om.pg_obbligazione = o.pg_obbligazione
              AND omv.cd_cds = om.cd_cds
              AND omv.esercizio = om.esercizio
              AND omv.pg_modifica = om.pg_modifica
              AND omv.im_modifica > 0
              AND om.pg_modifica > 0
           UNION ALL
           -- VARIAZIONI IN - RESIDUI PROPRI
           SELECT o.esercizio, o.esercizio_originale,
                  omv.cd_centro_responsabilita,
                                               --cnrutl001.getcdsfromcdr (omv.cd_centro_responsabilita) cd_cds,
                                               omv.cd_linea_attivita,
                  o.ti_appartenenza, o.ti_gestione, o.cd_elemento_voce, 0,
                  
                  -- IM_STANZ_RES_IMPROPRIO,
                  NULL,                                       -- PG_VAR_ST_RES
                       NULL,                                  -- DS_VAR_ST_RES
                            0,                               -- VAR_PIU_ST_RES
                              0,                            -- VAR_MENO_ST_RES
                                o.cd_cds,                           -- CDS_OBB
                                         o.pg_obbligazione,          -- PG_OBB
                                                           NULL,
                  
                  -- PG_OBB_SCAD
                  o.cd_tipo_documento_cont,
                                           -- CD_TIPO_DOCUMENTO_CONT
                                           o.ds_obbligazione,   -- DS_SCADENZA
                                                             0,
                                                               -- IM_OBBL_RES_IMP
                  0,
                    -- IM_OBBL_RES_PRO
                    om.pg_modifica,                          -- PG_VAR_RES_PRO
                                   om.ds_modifica,
                                                  -- DS_VAR_RES_PRO
                  0,
                    -- VAR_PIU_OBB_RES_PRO
                    ABS (omv.im_modifica),             -- VAR_MENO_OBB_RES_PRO
                                          NULL,                     -- CDS_MAN
                                               NULL,                 -- PG_MAN
                                                    NULL,        -- DS_MANDATO
                                                         0,
                  
                  -- PAGATO_RES_PROPRIO
                  0                                    -- PAGATO_RES_IMPROPRIO
             FROM obbligazione o,
                  obbligazione_mod_voce omv,
                  obbligazione_modifica om
            WHERE o.cd_tipo_documento_cont IN
                                         ('OBB_RES', 'OBB_PGIR_R', 'IMP_RES')
              AND o.pg_obbligazione > 0
              AND om.cd_cds = o.cd_cds
              AND om.esercizio = o.esercizio
              AND om.esercizio_originale = o.esercizio_originale
              AND om.pg_obbligazione = o.pg_obbligazione
              AND omv.cd_cds = om.cd_cds
              AND omv.esercizio = om.esercizio
              AND omv.pg_modifica = om.pg_modifica
              AND omv.im_modifica < 0
              AND om.pg_modifica > 0
           UNION ALL
           -- MANDATI RESIDUI PROPRI
           SELECT o.esercizio, o.esercizio_originale,
                  osv.cd_centro_responsabilita,
                                               --cnrutl001.getcdsfromcdr (osv.cd_centro_responsabilita) cd_cds,
                                               osv.cd_linea_attivita,
                  o.ti_appartenenza, o.ti_gestione, o.cd_elemento_voce, 0,
                  
                  -- IM_STANZ_RES_IMPROPRIO,
                  NULL,                                       -- PG_VAR_ST_RES
                       NULL,                                  -- DS_VAR_ST_RES
                            0,                               -- VAR_PIU_ST_RES
                              0,                            -- VAR_MENO_ST_RES
                                osv.cd_cds,                         -- CDS_OBB
                                           osv.pg_obbligazione,      -- PG_OBB
                  osv.pg_obbligazione_scadenzario,
                                                  -- PG_OBB_SCAD
                                                  o.cd_tipo_documento_cont,
                  
                  -- CD_TIPO_DOCUMENTO_CONT
                  os.ds_scadenza,                               -- DS_SCADENZA
                                 0,
                                   -- IM_OBBL_RES_IMP
                  0,
                    -- IM_OBBL_RES_PRO
                  NULL,
                       -- PG_VAR_RES_PRO
                  NULL,
                       -- DS_VAR_RES_PRO
                  0,
                    -- VAR_PIU_OBB_RES_PRO
                  0,
                    -- VAR_MENO_OBB_RES_PRO
                    m.cd_cds,                                       -- CDS_MAN
                             m.pg_mandato,                           -- PG_MAN
                                          m.ds_mandato,          -- DS_MANDATO
                  DECODE (NVL (os.im_scadenza, 0),
                          0, 0,
                          (osv.im_voce / os.im_scadenza) * mr.im_mandato_riga
                         ),                              -- PAGATO_RES_PROPRIO
                  0                                    -- PAGATO_RES_IMPROPRIO
             FROM obbligazione o,
                  obbligazione_scadenzario os,
                  obbligazione_scad_voce osv,
                  mandato m,
                  mandato_riga mr
            WHERE osv.cd_cds = os.cd_cds
              AND osv.esercizio = os.esercizio
              AND osv.esercizio_originale = os.esercizio_originale
              AND osv.pg_obbligazione = os.pg_obbligazione
              AND osv.pg_obbligazione_scadenzario =
                                                os.pg_obbligazione_scadenzario
              -- sf 16.03.2009 evita le righe a zero
              AND osv.im_voce > 0
              AND os.cd_cds = o.cd_cds
              AND os.esercizio = o.esercizio
              AND os.esercizio_originale = o.esercizio_originale
              AND os.pg_obbligazione = o.pg_obbligazione
              AND o.cd_tipo_documento_cont IN
                                         ('OBB_RES', 'OBB_PGIR_R', 'IMP_RES')
              AND o.pg_obbligazione > 0
              AND os.cd_cds = mr.cd_cds
              AND os.esercizio = mr.esercizio_obbligazione
              AND os.esercizio_originale = mr.esercizio_ori_obbligazione
              AND os.pg_obbligazione = mr.pg_obbligazione
              AND os.pg_obbligazione_scadenzario =
                                                mr.pg_obbligazione_scadenzario
              AND mr.cd_cds = m.cd_cds
              AND mr.esercizio = m.esercizio
              AND mr.pg_mandato = m.pg_mandato
              AND m.stato != 'A'
           UNION ALL
           -- MANDATI RESIDUI IMPROPRI
           SELECT o.esercizio, o.esercizio_originale,
                  osv.cd_centro_responsabilita,
                                               --cnrutl001.getcdsfromcdr (osv.cd_centro_responsabilita) cd_cds,
                                               osv.cd_linea_attivita,
                  o.ti_appartenenza, o.ti_gestione, o.cd_elemento_voce, 0,
                  
                  -- IM_STANZ_RES_IMPROPRIO,
                  NULL,                                       -- PG_VAR_ST_RES
                       NULL,                                  -- DS_VAR_ST_RES
                            0,                               -- VAR_PIU_ST_RES
                              0,                            -- VAR_MENO_ST_RES
                                osv.cd_cds,                         -- CDS_OBB
                                           osv.pg_obbligazione,      -- PG_OBB
                  osv.pg_obbligazione_scadenzario,
                                                  -- PG_OBB_SCAD
                                                  o.cd_tipo_documento_cont,
                  
                  -- CD_TIPO_DOCUMENTO_CONT
                  os.ds_scadenza,                               -- DS_SCADENZA
                                 0,
                                   -- IM_OBBL_RES_IMP
                  0,
                    -- IM_OBBL_RES_PRO
                  NULL,
                       -- PG_VAR_RES_PRO
                  NULL,
                       -- DS_VAR_RES_PRO
                  0,
                    -- VAR_PIU_OBB_RES_PRO
                  0,
                    -- VAR_MENO_OBB_RES_PRO
                    m.cd_cds,                                       -- CDS_MAN
                             m.pg_mandato,                           -- PG_MAN
                                          m.ds_mandato,          -- DS_MANDATO
                                                       0,
                  
                  --  PAGATO_RES_PROPRIO
                  DECODE (NVL (os.im_scadenza, 0),
                          0, 0,
                          (osv.im_voce / os.im_scadenza) * mr.im_mandato_riga
                         )                             -- PAGATO_RES_IMPROPRIO
             FROM obbligazione o,
                  obbligazione_scadenzario os,
                  obbligazione_scad_voce osv,
                  mandato m,
                  mandato_riga mr
            WHERE osv.cd_cds = os.cd_cds
              AND osv.esercizio = os.esercizio
              AND osv.esercizio_originale = os.esercizio_originale
              AND osv.pg_obbligazione = os.pg_obbligazione
              AND osv.pg_obbligazione_scadenzario =
                                                os.pg_obbligazione_scadenzario
              -- sf 16.03.2009 evita le righe a zero
              AND osv.im_voce > 0
              AND os.cd_cds = o.cd_cds
              AND os.esercizio = o.esercizio
              AND os.esercizio_originale = o.esercizio_originale
              AND os.pg_obbligazione = o.pg_obbligazione
              AND o.cd_tipo_documento_cont = 'OBB_RESIM'
              AND o.pg_obbligazione > 0
              AND os.cd_cds = mr.cd_cds
              AND os.esercizio = mr.esercizio_obbligazione
              AND os.esercizio_originale = mr.esercizio_ori_obbligazione
              AND os.pg_obbligazione = mr.pg_obbligazione
              AND os.pg_obbligazione_scadenzario =
                                                mr.pg_obbligazione_scadenzario
              AND mr.cd_cds = m.cd_cds
              AND mr.esercizio = m.esercizio
              AND mr.pg_mandato = m.pg_mandato
              AND m.stato != 'A') saldi
    WHERE saldi.cd_centro_responsabilita =
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
      AND saldi.cd_centro_responsabilita = cdr.cd_centro_responsabilita
      AND cdr.cd_unita_organizzativa =
                                    unita_organizzativa.cd_unita_organizzativa ;
