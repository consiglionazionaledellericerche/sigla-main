--------------------------------------------------------
--  DDL for View V_CONS_SALDI_RESIDUI_SPE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_CONS_SALDI_RESIDUI_SPE" ("ESERCIZIO", "ESERCIZIO_RES", "CD_CENTRO_RESPONSABILITA", "CD_LINEA_ATTIVITA", "TI_APPARTENENZA", "TI_GESTIONE", "CD_ELEMENTO_VOCE", "IM_STANZ_RES_IMPROPRIO", "PG_VAR_ST_RES", "DS_VAR_ST_RES", "VAR_PIU_ST_RES", "VAR_MENO_ST_RES", "CDS_OBB", "PG_OBB", "PG_OBB_SCAD", "CD_TIPO_DOCUMENTO_CONT", "DS_SCADENZA", "IM_OBBL_RES_IMP", "IM_OBBL_RES_PRO", "PG_VAR_RES_PRO", "DS_VAR_RES_PRO", "VAR_PIU_OBB_RES_PRO", "VAR_MENO_OBB_RES_PRO", "CDS_MAN", "PG_MAN", "DS_MANDATO", "PAGATO_RES_PROPRIO", "PAGATO_RES_IMPROPRIO") AS 
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
    om.pg_modifica,                            -- PG_VAR_RES_PRO
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
    om.pg_modifica,                            -- PG_VAR_RES_PRO
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
AND m.stato != 'A' ;
