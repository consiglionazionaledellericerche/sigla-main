--------------------------------------------------------
--  DDL for View V_CONS_SIOPE_BNL_SPE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_CONS_SIOPE_BNL_SPE" ("CD_CDS", "ESERCIZIO", "PG_MANDATO", "CD_SIOPE", "IMPORTO", "TI_MANDATO", "DT_EMISSIONE", "DT_TRASMISSIONE", "DT_PAGAMENTO", "CD_SOSPESO", "DT_REGISTRAZIONE") AS 
  SELECT mandato.cd_cds, mandato.esercizio, mandato.pg_mandato,
          mandato_siope.cd_siope, mandato_siope.importo, mandato.ti_mandato,
          mandato.dt_emissione,
          NVL (TRUNC (mandato.dt_ritrasmissione),
               TRUNC (mandato.dt_trasmissione)
              ),
          mandato.dt_pagamento, NULL, NULL
     FROM mandato, mandato_siope
    WHERE mandato.cd_cds = mandato_siope.cd_cds
      AND mandato.esercizio = mandato_siope.esercizio
      AND mandato.pg_mandato = mandato_siope.pg_mandato
      AND mandato.stato != 'A'
      AND NOT EXISTS (
             SELECT 1
               FROM sospeso_det_usc
              WHERE cd_cds_mandato = mandato.cd_cds
                AND esercizio = mandato.esercizio
                AND pg_mandato = mandato.pg_mandato
                AND ti_sospeso_riscontro = 'S')
   UNION ALL
   SELECT   mandato.cd_cds, mandato.esercizio, mandato.pg_mandato,
            mandato_siope.cd_siope,
            
-- MOLTIPLICO IL PESO CHE LA RIGA DI SOSPESO HA ALL'INTERNO DEL NETTO DEL MANDATO PER L'IMPORTO DELLA RIGA SIOPE
            SUM (DECODE (NVL (mandato_siope.importo, 0),
                         0, 0,
                           (  sospeso_det_usc.im_associato
                            / (mandato.im_mandato - mandato.im_ritenute)
                           )
                         * mandato_siope.importo
                        )
                ) importo_siope,
            mandato.ti_mandato, mandato.dt_emissione,
            NVL (TRUNC (mandato.dt_ritrasmissione),
                 TRUNC (mandato.dt_trasmissione)
                ),
            mandato.dt_pagamento, sospeso_det_usc.cd_sospeso,
            sospeso.dt_registrazione
       FROM sospeso, sospeso_det_usc, mandato, mandato_siope
      WHERE mandato.cd_cds = mandato_siope.cd_cds
        AND mandato.esercizio = mandato_siope.esercizio
        AND mandato.pg_mandato = mandato_siope.pg_mandato
        AND sospeso.cd_cds = sospeso_det_usc.cd_cds
        AND sospeso.esercizio = sospeso_det_usc.esercizio
        AND sospeso.ti_entrata_spesa = sospeso_det_usc.ti_entrata_spesa
        AND sospeso.ti_sospeso_riscontro =
                                          sospeso_det_usc.ti_sospeso_riscontro
        AND sospeso.cd_sospeso = sospeso_det_usc.cd_sospeso
        AND sospeso_det_usc.cd_cds_mandato = mandato.cd_cds
        AND sospeso_det_usc.esercizio = mandato.esercizio
        AND sospeso_det_usc.pg_mandato = mandato.pg_mandato
        AND sospeso.ti_entrata_spesa = 'S'
        AND sospeso.ti_sospeso_riscontro = 'S'
        AND mandato.stato != 'A'
   GROUP BY mandato.cd_cds,
            mandato.esercizio,
            mandato.pg_mandato,
            mandato_siope.cd_siope,
            mandato.ti_mandato,
            mandato.dt_emissione,
            NVL (TRUNC (mandato.dt_ritrasmissione),
                 TRUNC (mandato.dt_trasmissione)
                ),
            mandato.dt_pagamento,
            sospeso_det_usc.cd_sospeso,
            sospeso.dt_registrazione ;
