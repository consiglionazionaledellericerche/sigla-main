--------------------------------------------------------
--  DDL for View V_RISCONTRO_MANDATI_PARZIALI
--------------------------------------------------------
CREATE OR REPLACE FORCE VIEW "V_RISCONTRO_MANDATI_PARZIALI"("ESERCIZIO",
                                                             "CD_CDS",
	                                                           "CD_UO",
	                                                           "PG_MANDATO",
	                                                           "STATO",
	                                                           "DT_EMISSIONE",
	                                                           "DT_TRASMISSIONE",
	                                                           "DT_PAGAMENTO",
	                                                           "IM_MANDATO",
	                                                           "IM_PAGATO",
	                                                           "IM_RITENUTE",
	                                                           "DATA_RISCONTRO_PARZ",
	                                                           "IMPORTO_RISCONTRO_PARZ"
                                                          )
AS
   SELECT   mandato.esercizio, mandato.cd_cds_origine, mandato.cd_uo_origine,
            mandato.pg_mandato, mandato.stato, mandato.dt_emissione,
            dt_trasmissione, mandato.dt_pagamento, mandato.im_mandato,
            mandato.im_pagato, mandato.im_ritenute,
            TRUNC (sospeso_det_usc.dacr) data_risconto_parz, im_associato
       FROM mandato, sospeso_det_usc
      WHERE (    (mandato.cd_cds = sospeso_det_usc.cd_cds_mandato)
             AND (mandato.esercizio = sospeso_det_usc.esercizio)
             AND (mandato.pg_mandato = sospeso_det_usc.pg_mandato)
            )
        AND mandato.ti_mandato != 'R'
        AND sospeso_det_usc.esercizio > 2017
        AND im_associato != 0
        AND sospeso_det_usc.ti_entrata_spesa = 'S'
        AND sospeso_det_usc.ti_sospeso_riscontro = 'R'
        AND sospeso_det_usc.stato = 'N'
        AND TRUNC (dt_pagamento) > TRUNC (sospeso_det_usc.dacr)
   ORDER BY mandato.esercizio, mandato.pg_mandato;