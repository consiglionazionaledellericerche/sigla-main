--------------------------------------------------------
--  DDL for View V_CONS_SALDI_COMPETENZA_SPE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_CONS_SALDI_COMPETENZA_SPE" ("ESERCIZIO", "CD_CENTRO_RESPONSABILITA", "CD_LINEA_ATTIVITA", "TI_APPARTENENZA", "TI_GESTIONE", "CD_ELEMENTO_VOCE", "IM_STANZ_INIZIALE_A1", "PG_VARIAZIONE_PDG", "DS_VARIAZIONE", "VARIAZIONI_PIU", "VARIAZIONI_MENO", "CD_CDS_OBB", "PG_OBBLIGAZIONE", "PG_OBBLIGAZIONE_SCADENZARIO", "DS_SCADENZA", "IMPEGNI_COMP", "CD_CDS_MAN", "PG_MANDATO", "DS_MANDATO", "MANDATI_COMP") AS 
  SELECT /*+ optimizer_features_enable('10.1.0') */
      saldi.esercizio, saldi.cd_centro_responsabilita,
      
      --cnrutl001.getcdsfromcdr (saldi.cd_centro_responsabilita) cd_cds,
      saldi.cd_linea_attivita, saldi.ti_appartenenza,
      saldi.ti_gestione, saldi.cd_elemento_voce,
      saldi.im_stanz_iniziale_a1, NULL pg_variazione_pdg,
      NULL ds_variazione, 0 variazioni_piu, 0 variazioni_meno,
      NULL cd_cds_obb, NULL pg_obbligazione,
      NULL pg_obbligazione_scadenzario, NULL ds_scadenza,
      0 impegni_comp, NULL cd_cds_man, NULL pg_mandato,
      NULL ds_mandato, 0 mandati_comp
 FROM voce_f_saldi_cdr_linea saldi
WHERE saldi.esercizio = saldi.esercizio_res
  AND saldi.ti_gestione = 'S'
  AND saldi.im_stanz_iniziale_a1 != 0
UNION ALL
-- VARIAZIONI PIU'
SELECT /*+ optimizer_features_enable('10.1.0') */
      t.esercizio, d.cd_cdr_assegnatario,
                                         --cnrutl001.getcdsfromcdr (d.cd_cdr_assegnatario) cd_cds,
                                         d.cd_linea_attivita,
      d.ti_appartenenza, d.ti_gestione, d.cd_elemento_voce, 0,
                                                            -- INI
      t.pg_variazione_pdg,                               -- PG_VAR
                          t.ds_variazione,
      
        -- DS_VARIAZIONE
        DECODE (ABS (NVL (im_spese_gest_decentrata_int, 0)),
                NVL (im_spese_gest_decentrata_int, 0), NVL
                                    (im_spese_gest_decentrata_int,
                                     0
                                    ),
                0
               )
      + DECODE (ABS (NVL (im_spese_gest_decentrata_est, 0)),
                NVL (im_spese_gest_decentrata_est, 0), NVL
                                    (im_spese_gest_decentrata_est,
                                     0
                                    ),
                0
               )
      + DECODE (ABS (NVL (im_spese_gest_accentrata_int, 0)),
                NVL (im_spese_gest_accentrata_int, 0), NVL
                                    (im_spese_gest_accentrata_int,
                                     0
                                    ),
                0
               )
      + DECODE (ABS (NVL (im_spese_gest_accentrata_est, 0)),
                NVL (im_spese_gest_accentrata_est, 0), NVL
                                    (im_spese_gest_accentrata_est,
                                     0
                                    )
               ),                                       -- VAR PIU
      0,                                               -- VAR MENO
        NULL,                                           -- CDS_OBB
             NULL,                                       -- PG_OBB
                  NULL,                             -- PG_OBB_SCAD
                       NULL,                        -- DS_SCADENZA
                            0,                        -- IMPEGNATO
                              NULL,                     -- CDS_MAN
                                   NULL,                 -- PG_MAN
                                        NULL,        -- DS_MANDATO
                                             0           -- PAGATO
 FROM pdg_variazione t, pdg_variazione_riga_gest d
WHERE t.esercizio = d.esercizio
  AND t.pg_variazione_pdg = d.pg_variazione_pdg
  AND t.stato IN ('APP', 'APF')
  AND d.ti_gestione = 'S'
  AND categoria_dettaglio != 'SCR'
  AND   DECODE (ABS (NVL (im_spese_gest_decentrata_int, 0)),
                NVL (im_spese_gest_decentrata_int, 0), NVL
                                    (im_spese_gest_decentrata_int,
                                     0
                                    ),
                0
               )
      + DECODE (ABS (NVL (im_spese_gest_decentrata_est, 0)),
                NVL (im_spese_gest_decentrata_est, 0), NVL
                                    (im_spese_gest_decentrata_est,
                                     0
                                    ),
                0
               )
      + DECODE (ABS (NVL (im_spese_gest_accentrata_int, 0)),
                NVL (im_spese_gest_accentrata_int, 0), NVL
                                    (im_spese_gest_accentrata_int,
                                     0
                                    ),
                0
               )
      + DECODE (ABS (NVL (im_spese_gest_accentrata_est, 0)),
                NVL (im_spese_gest_accentrata_est, 0), NVL
                                    (im_spese_gest_accentrata_est,
                                     0
                                    )
               ) != 0
UNION ALL
-- VARIAZIONI MENO
SELECT /*+ optimizer_features_enable('10.1.0') */
      t.esercizio, d.cd_cdr_assegnatario,
                                         --cnrutl001.getcdsfromcdr (d.cd_cdr_assegnatario) cd_cds,
                                         d.cd_linea_attivita,
      d.ti_appartenenza, d.ti_gestione, d.cd_elemento_voce, 0,
                                                            -- INI
      t.pg_variazione_pdg,                        -- PG_VARIAZIONE
                          t.ds_variazione,
                                          -- DS_VARIAZIONE
      0,                                                -- VAR PIU
      ABS (  DECODE (ABS (NVL (im_spese_gest_decentrata_int, 0)),
                     NVL (im_spese_gest_decentrata_int, 0), 0,
                     NVL (im_spese_gest_decentrata_int, 0)
                    )
           + DECODE (ABS (NVL (im_spese_gest_decentrata_est, 0)),
                     NVL (im_spese_gest_decentrata_est, 0), 0,
                     NVL (im_spese_gest_decentrata_est, 0)
                    )
           + DECODE (ABS (NVL (im_spese_gest_accentrata_int, 0)),
                     NVL (im_spese_gest_accentrata_int, 0), 0,
                     NVL (im_spese_gest_accentrata_int, 0)
                    )
           + DECODE (ABS (NVL (im_spese_gest_accentrata_est, 0)),
                     NVL (im_spese_gest_accentrata_est, 0), 0,
                     NVL (im_spese_gest_accentrata_est, 0)
                    )
          ),                                           -- VAR MENO
      NULL,                                             -- CDS_OBB
           NULL,                                         -- PG_OBB
                NULL,                               -- PG_OBB_SCAD
                     NULL,                          -- DS_SCADENZA
                          0,                          -- IMPEGNATO
                            NULL,                       -- CDS_MAN
                                 NULL,                   -- PG_MAN
                                      NULL,          -- DS_MANDATO
                                           0             -- PAGATO
 FROM pdg_variazione t, pdg_variazione_riga_gest d
WHERE t.esercizio = d.esercizio
  AND t.pg_variazione_pdg = d.pg_variazione_pdg
  AND d.ti_gestione = 'S'
  AND t.stato IN ('APP', 'APF')
  AND categoria_dettaglio != 'SCR'
  AND   DECODE (ABS (NVL (im_spese_gest_decentrata_int, 0)),
                NVL (im_spese_gest_decentrata_int, 0), 0,
                NVL (im_spese_gest_decentrata_int, 0)
               )
      + DECODE (ABS (NVL (im_spese_gest_decentrata_est, 0)),
                NVL (im_spese_gest_decentrata_est, 0), 0,
                NVL (im_spese_gest_decentrata_est, 0)
               )
      + DECODE (ABS (NVL (im_spese_gest_accentrata_int, 0)),
                NVL (im_spese_gest_accentrata_int, 0), 0,
                NVL (im_spese_gest_accentrata_int, 0)
               )
      + DECODE (ABS (NVL (im_spese_gest_accentrata_est, 0)),
                NVL (im_spese_gest_accentrata_est, 0), 0,
                NVL (im_spese_gest_accentrata_est, 0)
               ) != 0                                  -- VAR MENO
UNION ALL
-- IMPEGNI ED IMPORTO IMPEGNATO
SELECT /*+ optimizer_features_enable('10.1.0') */
      o.esercizio, osv.cd_centro_responsabilita,
      
      --cnrutl001.getcdsfromcdr (osv.cd_centro_responsabilita) cd_cds,
      osv.cd_linea_attivita, o.ti_appartenenza, o.ti_gestione,
      o.cd_elemento_voce, 0,                                -- INI
                            NULL,                        -- PG_VAR
                                 NULL,            -- DS_VARIAZIONE
                                      0,                -- VAR_PIU
                                        0,             -- VAR_MENO
                                          osv.cd_cds,   -- CDS_OBB
      osv.pg_obbligazione,                               -- PG_OBB
                          osv.pg_obbligazione_scadenzario,
      
      -- PG_OBB_SCAD
      os.ds_scadenza,                               -- DS_SCADENZA
                     osv.im_voce,                     -- IMPEGNATO
                                 NULL,                  -- CDS_MAN
                                      NULL,              -- PG_MAN
                                           NULL,     -- DS_MANDATO
                                                0        -- PAGATO
 FROM obbligazione o,
      obbligazione_scadenzario os,
      obbligazione_scad_voce osv
WHERE o.esercizio = o.esercizio_originale
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
  AND (   (    o.esercizio_originale = o.esercizio
           AND 'Y' = (SELECT NVL (fl_regolamento_2006, 'N')
                        FROM parametri_cnr
                       WHERE esercizio = o.esercizio)
          )
       OR (    o.esercizio_ori_riporto IS NULL
           AND 'N' = (SELECT NVL (fl_regolamento_2006, 'N')
                        FROM parametri_cnr
                       WHERE esercizio = o.esercizio)
          )
      )
  AND o.pg_obbligazione > 0
-- SF 16.03.2009 PER EVITARE LE RIGHE A ZERO
  AND osv.im_voce > 0
UNION ALL
-- IMPEGNI, MANDATI ED IMPORTO PAGATO
SELECT /*+ optimizer_features_enable('10.1.0') */
      o.esercizio, osv.cd_centro_responsabilita,
      
      --cnrutl001.getcdsfromcdr (osv.cd_centro_responsabilita) cd_cds,
      osv.cd_linea_attivita, o.ti_appartenenza, o.ti_gestione,
      o.cd_elemento_voce, 0,                                -- INI
                            NULL,                        -- PG_VAR
                                 NULL,            -- DS_VARIAZIONE
                                      0,                -- VAR_PIU
                                        0,             -- VAR_MENO
                                          osv.cd_cds,   -- CDS_OBB
      osv.pg_obbligazione,                               -- PG_OBB
                          osv.pg_obbligazione_scadenzario,
      
      -- PG_OBB_SCAD
      os.ds_scadenza,                               -- DS_SCADENZA
                     0,            --OSV.IM_VOCE,     -- IMPEGNATO
                       m.cd_cds,                        -- CDS_MAN
                                m.pg_mandato,            -- PG_MAN
                                             m.ds_mandato,
      
      -- DS_MANDATO
      DECODE (NVL (os.im_scadenza, 0),
              0, 0,
              (osv.im_voce / os.im_scadenza) * mr.im_mandato_riga
             )                                           -- PAGATO
 FROM obbligazione o,
      obbligazione_scadenzario os,
      obbligazione_scad_voce osv,
      mandato m,
      mandato_riga mr
WHERE o.esercizio = o.esercizio_originale
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
  -- SF 16.03.2009 PER EVITARE LE RIGHE A ZERO
  AND osv.im_voce > 0
  AND (   (    o.esercizio_originale = o.esercizio
           AND 'Y' = (SELECT NVL (fl_regolamento_2006, 'N')
                        FROM parametri_cnr
                       WHERE esercizio = o.esercizio)
          )
       OR (    o.esercizio_ori_riporto IS NULL
           AND 'N' = (SELECT NVL (fl_regolamento_2006, 'N')
                        FROM parametri_cnr
                       WHERE esercizio = o.esercizio)
          )
      )
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
