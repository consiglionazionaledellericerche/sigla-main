--------------------------------------------------------
--  DDL for View V_CONS_SALDI_COMPETENZA_ENT
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_CONS_SALDI_COMPETENZA_ENT" ("ESERCIZIO", "CD_CENTRO_RESPONSABILITA", "CD_CDS", "CD_LINEA_ATTIVITA", "TI_APPARTENENZA", "TI_GESTIONE", "CD_ELEMENTO_VOCE", "IM_STANZ_INIZIALE_A1", "PG_VARIAZIONE_PDG", "DS_VARIAZIONE", "VARIAZIONI_PIU", "VARIAZIONI_MENO", "CD_CDS_ACC", "PG_ACCERTAMENTO", "PG_ACCERTAMENTO_SCADENZARIO", "DS_SCADENZA", "ACCERTAMENTI_COMP", "CD_CDS_REV", "PG_REVERSALE", "DS_REVERSALE", "REVERSALI_COMP") AS 
  SELECT vfs.esercizio, vfs.cd_centro_responsabilita,
          cnrutl001.getcdsfromcdr (vfs.cd_centro_responsabilita) cd_cds,
          vfs.cd_linea_attivita, vfs.ti_appartenenza, vfs.ti_gestione,
          vfs.cd_elemento_voce, vfs.im_stanz_iniziale_a1,
          NULL pg_variazione_pdg,                                    -- PG_VAR
                                 NULL ds_variazione,          -- DS_VARIAZIONE
                                                    0 variazioni_piu,
          
          -- VAR_PIU
          0 variazioni_meno,                                       -- VAR_MENO
                            NULL cd_cds_acc,                        -- CDS_ACC
                                            NULL pg_accertamento,
          
          -- PG_ACC
          NULL pg_accertamento_scadenzario,                     -- PG_ACC_SCAD
                                           NULL ds_scadenza,
          
          -- DS_SCADENZA
          0 accertamenti_comp,                                    -- ACCERTATO
                              NULL cd_cds_rev,                      -- CDS_REV
                                              NULL pg_reversale,
          
          -- PG_REV
          NULL ds_reversale,                                   -- DS_REVERSALE
                            0 reversali_comp                       -- RISCOSSO
     FROM voce_f_saldi_cdr_linea vfs
    WHERE vfs.esercizio = vfs.esercizio_res
      AND vfs.ti_gestione = 'E'
      AND vfs.im_stanz_iniziale_a1 != 0
   UNION ALL
-- VARIAZIONI PIU'
   SELECT t.esercizio, d.cd_cdr_assegnatario,
          cnrutl001.getcdsfromcdr (d.cd_cdr_assegnatario) cd_cds,
          d.cd_linea_attivita, d.ti_appartenenza, d.ti_gestione,
          d.cd_elemento_voce, 0,                                        -- INI
                                t.pg_variazione_pdg,                 -- PG_VAR
                                                    t.ds_variazione,
          
          -- DS_VARIAZIONE
          DECODE (ABS (NVL (im_entrata, 0)),
                  NVL (im_entrata, 0), NVL (im_entrata, 0)
                 ),                                                 -- VAR PIU
          0,                                                       -- VAR MENO
            NULL,                                                   -- CDS_ACC
                 NULL,                                               -- PG_ACC
                      NULL,                                     -- PG_ACC_SCAD
                           NULL,                                -- DS_SCADENZA
                                0,                                -- ACCERTATO
                                  NULL,                             -- CDS_REV
                                       NULL,                         -- PG_REV
                                            NULL,              -- DS_REVERSALE
                                                 0                 -- RISCOSSO
     FROM pdg_variazione t, pdg_variazione_riga_gest d
    WHERE d.ti_gestione = 'E'
      AND t.esercizio = d.esercizio
      AND t.pg_variazione_pdg = d.pg_variazione_pdg
      AND t.stato IN ('APP', 'APF')
      AND categoria_dettaglio != 'SCR'
      AND DECODE (ABS (NVL (im_entrata, 0)),
                  NVL (im_entrata, 0), NVL (im_entrata, 0)
                 ) != 0
   UNION ALL
-- VARIAZIONI MENO
   SELECT t.esercizio, d.cd_cdr_assegnatario,
          cnrutl001.getcdsfromcdr (d.cd_cdr_assegnatario) cd_cds,
          d.cd_linea_attivita, d.ti_appartenenza, d.ti_gestione,
          d.cd_elemento_voce, 0,                                        -- INI
                                t.pg_variazione_pdg,          -- PG_VARIAZIONE
                                                    t.ds_variazione,
                                                                    -- DS_VARIAZIONE
          0,                                                        -- VAR PIU
          ABS (DECODE (ABS (NVL (im_entrata, 0)),
                       NVL (im_entrata, 0), 0,
                       NVL (im_entrata, 0)
                      )
              ),                                                   -- VAR MENO
          NULL,                                                     -- CDS_ACC
               NULL,                                                 -- PG_ACC
                    NULL,                                       -- PG_ACC_SCAD
                         NULL,                                  -- DS_SCADENZA
                              0,                                  -- ACCERTATO
                                NULL,                               -- CDS_REV
                                     NULL,                           -- PG_REV
                                          NULL,                -- DS_REVERSALE
                                               0                   -- RISCOSSO
     FROM pdg_variazione t, pdg_variazione_riga_gest d
    WHERE d.ti_gestione = 'E'
      AND t.esercizio = d.esercizio
      AND t.pg_variazione_pdg = d.pg_variazione_pdg
      AND t.stato IN ('APP', 'APF')
      AND categoria_dettaglio != 'SCR'
      AND ABS (DECODE (ABS (NVL (im_entrata, 0)),
                       NVL (im_entrata, 0), 0,
                       NVL (im_entrata, 0)
                      )
              ) != 0                                               -- VAR MENO
   UNION ALL
-- ACCERTAMENTI ED IMPORTO ACCERTATO
   SELECT a.esercizio, asv.cd_centro_responsabilita,
          cnrutl001.getcdsfromcdr (asv.cd_centro_responsabilita) cd_cds,
          asv.cd_linea_attivita, a.ti_appartenenza, a.ti_gestione,
          a.cd_elemento_voce, 0,                                        -- INI
                                NULL,                                -- PG_VAR
                                     NULL,                    -- DS_VARIAZIONE
                                          0,                        -- VAR_PIU
                                            0,                     -- VAR_MENO
                                              asv.cd_cds,           -- CDS_ACC
                                                         asv.pg_accertamento,
                                                                     -- PG_ACC
          asv.pg_accertamento_scadenzario,
                                          -- PG_ACC_SCAD
                                          acs.ds_scadenza,      -- DS_SCADENZA
                                                          asv.im_voce,
                                                                  -- ACCERTATO
                                                                      NULL,
                                                                    -- CDS_REV
          NULL,                                                      -- PG_REV
               NULL,                                           -- DS_REVERSALE
                    0                                              -- RISCOSSO
     FROM accertamento a,
          accertamento_scadenzario acs,
          accertamento_scad_voce asv
    WHERE a.esercizio = a.esercizio_originale
      AND asv.cd_cds = acs.cd_cds
      AND asv.esercizio = acs.esercizio
      AND asv.esercizio_originale = acs.esercizio_originale
      AND asv.pg_accertamento = acs.pg_accertamento
      AND asv.pg_accertamento_scadenzario = acs.pg_accertamento_scadenzario
      AND asv.cd_cds = a.cd_cds
      AND asv.esercizio = a.esercizio
      AND asv.esercizio_originale = a.esercizio_originale
      AND asv.pg_accertamento = a.pg_accertamento
      AND (   (    a.esercizio_originale = a.esercizio
               AND 'Y' = (SELECT NVL (fl_regolamento_2006, 'N')
                            FROM parametri_cnr
                           WHERE esercizio = a.esercizio)
              )
           OR (    a.esercizio_ori_riporto IS NULL
               AND 'N' = (SELECT NVL (fl_regolamento_2006, 'N')
                            FROM parametri_cnr
                           WHERE esercizio = a.esercizio)
              )
          )
      AND a.pg_accertamento > 0
   UNION ALL
-- ACCERTAMENTI, REVERSALI ED IMPORTO RISCOSSO
   SELECT a.esercizio, asv.cd_centro_responsabilita,
          cnrutl001.getcdsfromcdr (asv.cd_centro_responsabilita) cd_cds,
          asv.cd_linea_attivita, a.ti_appartenenza, a.ti_gestione,
          a.cd_elemento_voce, 0,                                        -- INI
                                NULL,                                -- PG_VAR
                                     NULL,                    -- DS_VARIAZIONE
                                          0,                        -- VAR_PIU
                                            0,                     -- VAR_MENO
                                              asv.cd_cds,           -- CDS_ACC
                                                         asv.pg_accertamento,
                                                                     -- PG_OBB
          asv.pg_accertamento_scadenzario,
                                          -- PG_OBB_SCAD
                                          acs.ds_scadenza,      -- DS_SCADENZA
                                                          0,      -- ACCERTATO
                                                            r.cd_cds,
                                                                    -- CDS_MAN
          r.pg_reversale,                                            -- PG_MAN
                         r.ds_reversale,
          
          -- DS_REVERSALE
          DECODE (NVL (acs.im_scadenza, 0),
                  0, 0,
                  (asv.im_voce / acs.im_scadenza) * rr.im_reversale_riga
                 )                                                 -- RISCOSSO
     FROM accertamento a,
          accertamento_scadenzario acs,
          accertamento_scad_voce asv,
          reversale r,
          reversale_riga rr
    WHERE a.esercizio = a.esercizio_originale
      AND asv.cd_cds = acs.cd_cds
      AND asv.esercizio = acs.esercizio
      AND asv.esercizio_originale = acs.esercizio_originale
      AND asv.pg_accertamento = acs.pg_accertamento
      AND asv.pg_accertamento_scadenzario = acs.pg_accertamento_scadenzario
      AND asv.cd_cds = a.cd_cds
      AND asv.esercizio = a.esercizio
      AND asv.esercizio_originale = a.esercizio_originale
      AND asv.pg_accertamento = a.pg_accertamento
      AND (   (    a.esercizio_originale = a.esercizio
               AND 'Y' = (SELECT NVL (fl_regolamento_2006, 'N')
                            FROM parametri_cnr
                           WHERE esercizio = a.esercizio)
              )
           OR (    a.esercizio_ori_riporto IS NULL
               AND 'N' = (SELECT NVL (fl_regolamento_2006, 'N')
                            FROM parametri_cnr
                           WHERE esercizio = a.esercizio)
              )
          )
      AND a.pg_accertamento > 0
      AND acs.cd_cds = rr.cd_cds
      AND acs.esercizio = rr.esercizio_accertamento
      AND acs.esercizio_originale = rr.esercizio_ori_accertamento
      AND acs.pg_accertamento = rr.pg_accertamento
      AND acs.pg_accertamento_scadenzario = rr.pg_accertamento_scadenzario
      AND rr.cd_cds = r.cd_cds
      AND rr.esercizio = r.esercizio
      AND rr.pg_reversale = r.pg_reversale
      AND r.stato != 'A' ;
