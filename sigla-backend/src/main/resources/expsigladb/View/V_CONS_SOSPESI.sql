--------------------------------------------------------
--  DDL for View V_CONS_SOSPESI
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_CONS_SOSPESI" ("ESERCIZIO", "TI_ENTRATA_SPESA", "CD_CDS", "CD_CDS_ORIGINE", "CD_SOSPESO", "LIVELLO", "DT_REGISTRAZIONE", "DS_ANAGRAFICO", "CAUSALE", "TI_CC_BI", "DES_TI_CC_BI", "STATO_VALIDITA", "DT_STORNO", "IM_SOSPESO", "IM_ASSOCIATO", "IM_DA_ASSOCIARE", "DS_STATO_SOSPESO", "IM_ASS_MOD_1210", "CD_SOSPESO_PADRE", "PG_MAN_REV", "CD_AVVISO_PAGOPA") AS
  SELECT esercizio, ti_entrata_spesa, cd_cds, cd_cds_origine, cd_sospeso,
          DECODE (cd_sospeso_padre, NULL, 1, 2),
                                                -- LIVELLO 1: SOSPESO PADRE O FIGLIO
                                                dt_registrazione,
          ds_anagrafico, causale, ti_cc_bi,
          -- CODICE Banca d'Italia/Conto Corrente
          DECODE (ti_cc_bi, 'C', 'Conto Corrente', 'B', 'Banca d''Italia'),
          -- DS Banca d'Italia/Conto Corrente
          DECODE (fl_stornato, 'N', 'Valido', 'Y', 'Stornato'),
                                                               -- STATO_VALIDITA
                                                               dt_storno,
          im_sospeso,
          DECODE (cd_sospeso_padre,
                  NULL, nvl((SELECT SUM (nvl(figli.im_associato,0))
                           FROM sospeso figli
                          WHERE figli.cd_cds = sospeso.cd_cds
                            AND figli.esercizio = sospeso.esercizio
                            AND figli.ti_entrata_spesa =
                                                      sospeso.ti_entrata_spesa
                            AND figli.ti_sospeso_riscontro =
                                                  sospeso.ti_sospeso_riscontro
                            AND figli.fl_stornato =  'N'
                            AND figli.cd_sospeso_padre = sospeso.cd_sospeso),0),
                  im_associato
                 ),                                            -- IM_ASSOCIATO
          DECODE (fl_stornato, 'N',DECODE (cd_sospeso_padre,
                  NULL, im_sospeso
                   - nvl((SELECT SUM (nvl(figli.im_associato,0))
                        FROM sospeso figli
                       WHERE figli.cd_cds = sospeso.cd_cds
                         AND figli.esercizio = sospeso.esercizio
                         AND figli.ti_entrata_spesa = sospeso.ti_entrata_spesa
                         AND figli.ti_sospeso_riscontro =
                                                  sospeso.ti_sospeso_riscontro
                         AND figli.fl_stornato =  'N'
                         AND figli.cd_sospeso_padre = sospeso.cd_sospeso),0),
                  NVL (im_sospeso, 0) - NVL (im_associato, 0)
                 ),0),                                            -- DA ASSOCIARE
          DECODE (cd_sospeso_padre,
                  NULL,                                     -- STATO DEL PADRE
                  DECODE (im_sospeso,
                          nvl((SELECT SUM (nvl(figli.im_associato,0))
                             FROM sospeso figli
                            WHERE figli.cd_cds = sospeso.cd_cds
                              AND figli.esercizio = sospeso.esercizio
                              AND figli.ti_entrata_spesa =
                                                      sospeso.ti_entrata_spesa
                              AND figli.ti_sospeso_riscontro =
                                                  sospeso.ti_sospeso_riscontro
                              AND figli.fl_stornato =  'N'
                              AND figli.cd_sospeso_padre = sospeso.cd_sospeso),0), 'Completamente associato a '
                           || DECODE (ti_entrata_spesa,
                                      'E', 'Reversali',
                                      'Mandati'
                                     ),
                          DECODE (nvl((SELECT SUM (nvl(figli.im_associato,0))
                                     FROM sospeso figli
                                    WHERE figli.cd_cds = sospeso.cd_cds
                                      AND figli.esercizio = sospeso.esercizio
                                      AND figli.ti_entrata_spesa =
                                                      sospeso.ti_entrata_spesa
                                      AND figli.ti_sospeso_riscontro =
                                                  sospeso.ti_sospeso_riscontro
                                      AND figli.fl_stornato =  'N'
                                      AND figli.cd_sospeso_padre =
                                                            sospeso.cd_sospeso),0),
                                  0,  'Totalmente da associare a '
                                   || DECODE (ti_entrata_spesa,
                                              'E', 'Reversali',
                                              'Mandati'
                                             ),
                                     'Parzialmente associato a '
                                  || DECODE (ti_entrata_spesa,
                                             'E', 'Reversali',
                                             'Mandati'
                                            )
                                 )
                         ),
                  -- STATO DEL FIGLIO
                  DECODE (im_sospeso,
                          im_associato, 'Completamente associato a '
                           || DECODE (ti_entrata_spesa,
                                      'E', 'Reversali',
                                      'Mandati'
                                     ),
                          DECODE (im_associato,
                                  0,  'Totalmente da associare a '
                                   || DECODE (ti_entrata_spesa,
                                              'E', 'Reversali',
                                              'Mandati'
                                             ),
                                     'Parzialmente associato a '
                                  || DECODE (ti_entrata_spesa,
                                             'E', 'Reversali',
                                             'Mandati'
                                            )
                                 )
                         )
                 ),
          im_ass_mod_1210, cd_sospeso_padre, NULL pg_man_rev, CD_AVVISO_PAGOPA
     FROM sospeso
    WHERE ti_sospeso_riscontro = 'S'
   UNION ALL
-- MANDATI ASSOCIATI
   SELECT sospeso_det_usc.esercizio, ti_entrata_spesa, sospeso_det_usc.cd_cds,
          NULL, cd_sospeso, 3,          -- LIVELLO 3: ASSOCIAZIONE A REVERSALE
                              dt_emissione,        -- DATA EMISSIONE REVERSALE
                                           NULL,              -- DS_ANAGRAFICO
                                                ds_mandato,         -- CAUSALE
                                                           NULL,
                                                                -- CODICE Banca d'Italia/Conto Corrente
          NULL,
          -- DS Banca d'Italia/Conto Corrente
          DECODE (sospeso_det_usc.stato, 'N', 'Valido', 'A', 'Annullato'),
          -- STATO_VALIDITA
          NULL,                                                   -- dt_storno
               0,
                 -- IM_SOSPESO
                 im_associato,                                 -- IM_ASSOCIATO
                              0,                               -- DA ASSOCIARE
          DECODE (sospeso_det_usc.stato,
                  'N', 'Associato a Mandato Valido',
                  'Associato a Mandato Annullato'
                 ),                                        -- DS_STATO_SOSPESO
          0,                                                -- IM_ASS_MOD_1210
            cd_sospeso,                                    -- CD_SOSPESO_PADRE
                       sospeso_det_usc.pg_mandato pg_man_rev     -- PG_MAN_REV
                       , NULL
     FROM sospeso_det_usc, mandato
    WHERE ti_sospeso_riscontro = 'S'
      AND ti_entrata_spesa = 'S'
      and sospeso_det_usc.stato ='N'
      AND sospeso_det_usc.cd_cds_mandato = mandato.cd_cds
      AND sospeso_det_usc.esercizio = mandato.esercizio
      AND sospeso_det_usc.pg_mandato = mandato.pg_mandato
   UNION ALL
-- REVERSALI ASSOCIATE
   SELECT sospeso_det_etr.esercizio, ti_entrata_spesa, sospeso_det_etr.cd_cds,
          NULL,                                              -- CD_CDS_ORIGINE
               cd_sospeso, 3,             -- LIVELLO 3: ASSOCIAZIONE A MANDATO
                             dt_emissione,           -- DATA EMISSIONE MANDATO
                                          NULL,               -- DS_ANAGRAFICO
                                               ds_reversale,        -- CAUSALE
                                                            NULL,  -- TI_CC_BI
                                                                 NULL,
                                                                       -- DES_TI_CC_BI
          'N',
              -- FL_STORNATO (Y/N)
          NULL,                                                   -- dt_storno
               0,
                 -- IM_SOSPESO
                 im_associato,                                 -- IM_ASSOCIATO
                              0,                               -- DA ASSOCIARE
          DECODE (sospeso_det_etr.stato,
                  'N', 'Associato a Reversale Valida',
                  'Associato a Reversale Annullata'
                 ),                                        -- DS_STATO_SOSPESO
          0,                                                -- IM_ASS_MOD_1210
            cd_sospeso,                                    -- CD_SOSPESO_PADRE
                       sospeso_det_etr.pg_reversale pg_man_rev   -- PG_MAN_REV
                       , NULL
     FROM sospeso_det_etr, reversale
    WHERE ti_sospeso_riscontro = 'S'
      AND ti_entrata_spesa = 'E'
      and sospeso_det_etr.stato ='N'
      AND sospeso_det_etr.cd_cds_reversale = reversale.cd_cds
      AND sospeso_det_etr.esercizio = reversale.esercizio
      AND sospeso_det_etr.pg_reversale = reversale.pg_reversale;
