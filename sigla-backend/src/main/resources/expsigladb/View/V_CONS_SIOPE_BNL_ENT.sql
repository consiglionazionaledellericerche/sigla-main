--------------------------------------------------------
--  DDL for View V_CONS_SIOPE_BNL_ENT
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_CONS_SIOPE_BNL_ENT" ("CD_CDS", "ESERCIZIO", "PG_REVERSALE", "CD_SIOPE", "IMPORTO_SIOPE", "TI_REVERSALE", "DT_EMISSIONE", "DT_TRASMISSIONE", "DT_INCASSO", "CD_SOSPESO", "DT_REGISTRAZIONE") AS 
  SELECT   reversale.cd_cds, reversale.esercizio, reversale.pg_reversale,
            reversale_siope.cd_siope,
            SUM (DECODE (NVL (sospeso_det_etr.im_associato, 0),
                         0, 0,
                           (reversale_siope.importo / reversale.im_reversale
                           )
                         * sospeso_det_etr.im_associato
                        )
                ) importo_siope,
            reversale.ti_reversale, reversale.dt_emissione,
            NVL (TRUNC (reversale.dt_ritrasmissione),
                 TRUNC (reversale.dt_trasmissione)
                ),
            reversale.dt_incasso, sospeso_det_etr.cd_sospeso,
            sospeso.dt_registrazione
--        Sum(Decode(Nvl(RISCONTRO_DET_ETR.IM_ASSOCIATO, 0), 0, 0, (REVERSALE_SIOPE.IMPORTO/REVERSALE.IM_REVERSALE)*RISCONTRO_DET_ETR.IM_ASSOCIATO))
   FROM     sospeso,
            sospeso_det_etr,
            reversale,
            reversale_siope
                      --, SOSPESO riscontro, SOSPESO_DET_ETR riscontro_det_etr
      WHERE reversale.cd_cds = reversale_siope.cd_cds
        AND reversale.esercizio = reversale_siope.esercizio
        AND reversale.pg_reversale = reversale_siope.pg_reversale
        AND sospeso.cd_cds = sospeso_det_etr.cd_cds
        AND sospeso.esercizio = sospeso_det_etr.esercizio
        AND sospeso.ti_entrata_spesa = sospeso_det_etr.ti_entrata_spesa
        AND sospeso.ti_sospeso_riscontro =
                                          sospeso_det_etr.ti_sospeso_riscontro
        AND sospeso.cd_sospeso = sospeso_det_etr.cd_sospeso
        AND sospeso_det_etr.cd_cds_reversale = reversale.cd_cds
        AND sospeso_det_etr.esercizio = reversale.esercizio
        AND sospeso_det_etr.pg_reversale = reversale.pg_reversale
        AND sospeso.ti_entrata_spesa = 'E'
        AND sospeso.ti_sospeso_riscontro = 'S'
        AND reversale.stato != 'A'                                       --And
/*      riscontro_det_etr.TI_SOSPESO_RISCONTRO (+) = 'R' AND
        riscontro_det_etr.STATO (+)        = 'N' AND
        riscontro_det_etr.CD_CDS (+)       = REVERSALE.CD_CDS AND
        riscontro_det_etr.ESERCIZIO (+)    = REVERSALE.ESERCIZIO AND
        riscontro_det_etr.PG_REVERSALE (+) = REVERSALE.PG_REVERSALE AND
        riscontro.CD_CDS               = riscontro_det_etr.CD_CDS  AND
        riscontro.ESERCIZIO            = riscontro_det_etr.ESERCIZIO AND
        riscontro.TI_ENTRATA_SPESA     = riscontro_det_etr.TI_ENTRATA_SPESA AND
        riscontro.TI_SOSPESO_RISCONTRO = riscontro_det_etr.TI_SOSPESO_RISCONTRO AND
        riscontro.CD_SOSPESO           = riscontro_det_etr.CD_SOSPESO */
   GROUP BY reversale.cd_cds,
            reversale.esercizio,
            reversale.pg_reversale,
            reversale_siope.cd_siope,
            reversale.ti_reversale,
            reversale.dt_emissione,
            NVL (TRUNC (reversale.dt_ritrasmissione),
                 TRUNC (reversale.dt_trasmissione)
                ),
            reversale.dt_incasso,
            sospeso_det_etr.cd_sospeso,
            sospeso.dt_registrazione
   UNION ALL
   SELECT reversale.cd_cds, reversale.esercizio, reversale.pg_reversale,
          reversale_siope.cd_siope, reversale_siope.importo,
          reversale.ti_reversale, reversale.dt_emissione,
          NVL (TRUNC (reversale.dt_ritrasmissione),
               TRUNC (reversale.dt_trasmissione)
              ),
          reversale.dt_incasso, NULL, NULL
     FROM reversale, reversale_siope
    WHERE reversale.cd_cds = reversale_siope.cd_cds
      AND reversale.esercizio = reversale_siope.esercizio
      AND reversale.pg_reversale = reversale_siope.pg_reversale
      AND reversale.stato != 'A'
      AND NOT EXISTS (
             SELECT 1
               FROM sospeso_det_etr
              WHERE cd_cds_reversale = reversale.cd_cds
                AND esercizio = reversale.esercizio
                AND pg_reversale = reversale.pg_reversale) ;
