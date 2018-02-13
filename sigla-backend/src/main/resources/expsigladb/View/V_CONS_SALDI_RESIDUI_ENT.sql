--------------------------------------------------------
--  DDL for View V_CONS_SALDI_RESIDUI_ENT
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_CONS_SALDI_RESIDUI_ENT" ("ESERCIZIO", "ESERCIZIO_RES", "CD_CENTRO_RESPONSABILITA", "CD_LINEA_ATTIVITA", "TI_APPARTENENZA", "TI_GESTIONE", "CD_ELEMENTO_VOCE", "CDS_ACCERTAMENTO", "PG_ACCERTAMENTO", "PG_ACCERTAMENTO_SCADENZARIO", "CD_TIPO_DOCUMENTO_CONT", "DS_SCADENZA", "IM_OBBL_RES_PRO", "PG_VAR_RES_PRO", "DS_VAR_RES_PRO", "VAR_PIU_ACC_RES_PRO", "VAR_MENO_ACC_RES_PRO", "CD_CDS_REV", "PG_REVERSALE", "DS_REVERSALE", "RISCOSSO_RES_PROPRIO") AS 
  SELECT o.esercizio, o.esercizio_originale esercizio_res,
    osv.cd_centro_responsabilita,
                                 --cnrutl001.getCdsFromCdr(OSV.CD_CENTRO_RESPONSABILITA) CD_CDS,
                                 osv.cd_linea_attivita,
    o.ti_appartenenza, o.ti_gestione, o.cd_elemento_voce,
    osv.cd_cds cds_accertamento,                      -- CDS_ACC
                                osv.pg_accertamento,   -- PG_ACC
    osv.pg_accertamento_scadenzario,              -- PG_ACC_SCAD
                                    o.cd_tipo_documento_cont,
                                       -- CD_TIPO_DOCUMENTO_CONT
    os.ds_scadenza,                               -- DS_SCADENZA
                   osv.im_voce im_obbl_res_pro,
-- IM_OBBL_RES_PRO   I N C L U S E    L E    V A R I A Z I O N I
    NULL pg_var_res_pro,                       -- PG_VAR_RES_PRO
                        NULL ds_var_res_pro,   -- DS_VAR_RES_PRO
    0 var_piu_acc_res_pro,                -- VAR_PIU_OBB_RES_PRO
                          0 var_meno_acc_res_pro,
                                         -- VAR_MENO_OBB_RES_PRO
    NULL cd_cds_rev,                                  -- CDS_REV
                    NULL pg_reversale,                 -- PG_REV
                                      NULL ds_reversale,
                                                 -- DS_REVERSALE
    0 riscosso_res_proprio               -- RISCOSSO_RES_PROPRIO
FROM accertamento o,
    accertamento_scadenzario os,
    accertamento_scad_voce osv
WHERE osv.cd_cds = os.cd_cds
AND osv.esercizio = os.esercizio
AND osv.esercizio_originale = os.esercizio_originale
AND osv.pg_accertamento = os.pg_accertamento
AND osv.pg_accertamento_scadenzario =
                                  os.pg_accertamento_scadenzario
AND osv.cd_cds = o.cd_cds
AND osv.esercizio = o.esercizio
AND osv.esercizio_originale = o.esercizio_originale
AND osv.pg_accertamento = o.pg_accertamento
AND o.cd_tipo_documento_cont IN
                         ('ACR_RES', 'ACR_PGIR_R', 'ACR_PGIRO')
AND o.pg_accertamento > 0
AND
-- 17.03.2009 SF ELIMINAZIONE RIGHE A ZERO
    osv.im_voce > 0
UNION ALL
-- VARIAZIONI IN + RESIDUI PROPRI
SELECT o.esercizio, o.esercizio_originale,
    omv.cd_centro_responsabilita,
                                 --cnrutl001.getCdsFromCdr(OMV.CD_CENTRO_RESPONSABILITA) CD_CDS,
                                 omv.cd_linea_attivita,
    o.ti_appartenenza, o.ti_gestione, o.cd_elemento_voce,
    o.cd_cds,                                         -- CDS_ACC
             o.pg_accertamento,                        -- PG_ACC
                               NULL,              -- PG_ACC_SCAD
                                    o.cd_tipo_documento_cont,
                                       -- CD_TIPO_DOCUMENTO_CONT
    o.ds_accertamento,                        -- DS_ACCERTAMENTO
                      0,                      -- IM_OBBL_RES_PRO
                        om.pg_modifica,        -- PG_VAR_RES_PRO
                                       om.ds_modifica,
                                               -- DS_VAR_RES_PRO
    omv.im_modifica,                      -- VAR_PIU_OBB_RES_PRO
                    0,                   -- VAR_MENO_OBB_RES_PRO
                      NULL,                           -- CDS_REV
                           NULL,                       -- PG_REV
                                NULL,            -- DS_REVERSALE
                                     0   -- RISCOSSO_RES_PROPRIO
FROM accertamento o,
    accertamento_mod_voce omv,
    accertamento_modifica om
WHERE o.cd_tipo_documento_cont IN
                         ('ACR_RES', 'ACR_PGIR_R', 'ACR_PGIRO')
AND o.pg_accertamento > 0
AND om.cd_cds = o.cd_cds
AND om.esercizio = o.esercizio
AND om.pg_accertamento = o.pg_accertamento
AND om.esercizio_originale = o.esercizio_originale
AND omv.cd_cds = om.cd_cds
AND omv.esercizio = om.esercizio
AND omv.pg_modifica = om.pg_modifica
AND omv.im_modifica > 0
AND om.pg_modifica > 0
UNION ALL
-- VARIAZIONI IN - RESIDUI PROPRI
SELECT o.esercizio, o.esercizio_originale,
    omv.cd_centro_responsabilita,
                                 --cnrutl001.getCdsFromCdr(OMV.CD_CENTRO_RESPONSABILITA) CD_CDS,
                                 omv.cd_linea_attivita,
    o.ti_appartenenza, o.ti_gestione, o.cd_elemento_voce,
    o.cd_cds,                                         -- CDS_ACC
             o.pg_accertamento,                        -- PG_ACC
                               NULL,              -- PG_ACC_SCAD
                                    o.cd_tipo_documento_cont,
                                       -- CD_TIPO_DOCUMENTO_CONT
    o.ds_accertamento,                            -- DS_SCADENZA
                      0,                      -- IM_OBBL_RES_PRO
                        om.pg_modifica,        -- PG_VAR_RES_PRO
                                       om.ds_modifica,
                                               -- DS_VAR_RES_PRO
                                                      0,
                                          -- VAR_PIU_OBB_RES_PRO
    ABS (omv.im_modifica),               -- VAR_MENO_OBB_RES_PRO
                          NULL,                       -- CDS_REV
                               NULL,                   -- PG_REV
                                    NULL,        -- DS_REVERSALE
    0                                    -- RISCOSSO_RES_PROPRIO
FROM accertamento o,
    accertamento_mod_voce omv,
    accertamento_modifica om
WHERE o.cd_tipo_documento_cont IN
                         ('ACR_RES', 'ACR_PGIR_R', 'ACR_PGIRO')
AND o.pg_accertamento > 0
AND om.cd_cds = o.cd_cds
AND om.esercizio = o.esercizio
AND om.pg_accertamento = o.pg_accertamento
AND om.esercizio_originale = o.esercizio_originale
AND omv.cd_cds = om.cd_cds
AND omv.esercizio = om.esercizio
AND omv.pg_modifica = om.pg_modifica
AND omv.im_modifica < 0
AND om.pg_modifica > 0
UNION ALL
-- REVERSALI RESIDUI PROPRI
SELECT o.esercizio, o.esercizio_originale,
    osv.cd_centro_responsabilita,
                                 --cnrutl001.getCdsFromCdr(OSV.CD_CENTRO_RESPONSABILITA) CD_CDS,
                                 osv.cd_linea_attivita,
    o.ti_appartenenza, o.ti_gestione, o.cd_elemento_voce,
    osv.cd_cds,                                       -- CDS_OBB
               osv.pg_accertamento,                    -- PG_ACC
    osv.pg_accertamento_scadenzario,              -- PG_ACC_SCAD
                                    o.cd_tipo_documento_cont,
                                       -- CD_TIPO_DOCUMENTO_CONT
    os.ds_scadenza,                               -- DS_SCADENZA
                   0,                         -- IM_OBBL_RES_PRO
                     NULL,                     -- PG_VAR_RES_PRO
                          NULL,                -- DS_VAR_RES_PRO
                               0,         -- VAR_PIU_OBB_RES_PRO
                                 0,      -- VAR_MENO_OBB_RES_PRO
                                   m.cd_cds,          -- CDS_REV
                                            m.pg_reversale,
                                                       -- PG_REV
    m.ds_reversale,                              -- DS_REVERSALE
    DECODE (NVL (os.im_scadenza, 0),
            0, 0,
              (osv.im_voce / os.im_scadenza)
            * mr.im_reversale_riga
           )                             -- RISCOSSO_RES_PROPRIO
FROM accertamento o,
    accertamento_scadenzario os,
    accertamento_scad_voce osv,
    reversale m,
    reversale_riga mr
WHERE osv.cd_cds = os.cd_cds
AND osv.esercizio = os.esercizio
AND osv.esercizio_originale = os.esercizio_originale
AND osv.pg_accertamento = os.pg_accertamento
AND osv.pg_accertamento_scadenzario =
                                  os.pg_accertamento_scadenzario
AND os.cd_cds = o.cd_cds
AND os.esercizio = o.esercizio
AND os.esercizio_originale = o.esercizio_originale
AND os.pg_accertamento = o.pg_accertamento
AND o.cd_tipo_documento_cont IN
                         ('ACR_RES', 'ACR_PGIR_R', 'ACR_PGIRO')
AND o.pg_accertamento > 0
AND os.cd_cds = mr.cd_cds
AND os.esercizio = mr.esercizio_accertamento
AND os.esercizio_originale = mr.esercizio_ori_accertamento
AND os.pg_accertamento = mr.pg_accertamento
AND os.pg_accertamento_scadenzario =
                                  mr.pg_accertamento_scadenzario
AND mr.cd_cds = m.cd_cds
AND mr.esercizio = m.esercizio
AND mr.pg_reversale = m.pg_reversale
AND m.stato != 'A' ;
