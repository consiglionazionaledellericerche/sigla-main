--------------------------------------------------------
--  DDL for View V_ESTRAI_IMPONIBILE_CUD
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_ESTRAI_IMPONIBILE_CUD" ("CD_CDS_COMPENSO", "CD_UO_COMPENSO", "ESERCIZIO_COMPENSO", "PG_COMPENSO", "FL_SENZA_CALCOLI", "FL_COMPENSO_CONGUAGLIO", "STATO_COFI", "DT_EMISSIONE_MANDATO", "DT_TRASMISSIONE_MANDATO", "DT_PAGAMENTO_MANDATO", "ESERCIZIO_PAGAMENTO", "CD_TERZO", "CD_ANAG", "ESERCIZIO_CONFIGURAZIONE", "CD_TRATTAMENTO", "FL_DETRAZIONI", "FL_TASSAZIONE_SEPARATA", "FL_COMPENSO_MINICARRIERA", "FL_RECUPERO_RATE", "FL_COCOCO", "CD_TIPOLOGIA_REDDITO", "DS_TIPOLOGIA_REDDITO", "DT_CMP_DA_COMPENSO", "DT_CMP_A_COMPENSO", "IM_LORDO_PERCIP_COMPENSO", "QUOTA_ESENTE_COMPENSO", "IM_NO_FISCALE_COMPENSO", "IMPONIBILE_FISC_LORDO_COMPENSO", "IMPONIBILE_FISC_NETTO_COMPENSO", "IM_DEDUZIONE_COMPENSO", "DETRAZ_PE_NETTO_COMPENSO", "DETRAZ_LA_NETTO_COMPENSO", "DETRAZ_CO_NETTO_COMPENSO", "DETRAZ_FI_NETTO_COMPENSO", "DETRAZ_AL_NETTO_COMPENSO", "DETRAZ_RID_CUNEO_NETTO_COMP", "FL_ESCLUDI_QVARIA_DEDUZIONE", "FL_INTERA_QFISSA_DEDUZIONE", "FL_COMPENSO_MISSIONE", "IM_REDD_NON_TASSATI_PER_CONV", "IM_REDD_ESENTI_PER_LEGGE", "CD_CATEGORIA") AS
  SELECT
--==================================================================================================
--
-- Date: 02/02/2004
-- Version: 1.0
--
-- Estrazione singoli compensi. Si definisce il rapporto di tipo COLL come identificativo
-- dei rapporti tipo CO.CO.CO
--
-- History:
--
-- Date: 02/02/2004
-- Version: 1.0
--
-- Creazione vista
--
--
-- Date: 07/03/2011
-- Version: 1.1
--
-- Aggiunta l'informazione compenso da missione (Y/N)
--
-- Date: 06/03/2013
-- Version: 1.2
--
-- Aggiunta l'informazione del tipo di tassazione del compenso
--
-- Body:
--
--==================================================================================================
       B.cd_cds,
       B.cd_unita_organizzativa,
       B.esercizio,
       B.pg_compenso,
       B.fl_senza_calcoli,
       B.fl_compenso_conguaglio,
       B.stato_cofi,
       B.dt_emissione_mandato,
       B.dt_trasmissione_mandato,
       B.dt_pagamento_mandato,
       DECODE(B.dt_emissione_mandato, NULL, 0, TO_NUMBER(TO_CHAR(B.dt_emissione_mandato,'YYYY'))),
       B.cd_terzo,
       C.cd_anag,
       A.esercizio,
       B.cd_trattamento,
       A.fl_detrazioni,
       B.fl_compenso_mcarriera_tassep,
       B.fl_compenso_minicarriera,
       B.fl_recupero_rate,
       DECODE(B.cd_tipo_rapporto,'COLL','Y','N'),
       A.cd_tipologia_reddito,
       A.ds_tipologia_reddito,
       B.dt_da_competenza_coge,
       B.dt_a_competenza_coge,
       B.im_lordo_percipiente,
       B.quota_esente,
       B.im_no_fiscale,
       B.imponibile_fiscale,
       B.imponibile_fiscale_netto,
       B.im_deduzione_irpef,
       B.detrazioni_personali_netto,
       B.detrazioni_la_netto,
       B.detrazione_coniuge_netto,
       B.detrazione_figli_netto,
       B.detrazione_altri_netto,
       B.detrazione_rid_cuneo_netto,
       B.fl_escludi_qvaria_deduzione,
       B.fl_intera_qfissa_deduzione,
       decode(b.pg_missione,null,'N','Y'),
       decode(A.FL_REDD_NON_TASSATI_PER_CONV,'Y',B.im_lordo_percipiente,0),
       decode(A.FL_REDD_ESENTI_PER_LEGGE,'Y',B.im_lordo_percipiente,0),
       A.cd_categoria
FROM   CONFIG_ESTRAZIONE_CUD A,
       COMPENSO B,
       TERZO C
WHERE  B.cd_trattamento = A.cd_trattamento AND
       B.stato_cofi = 'P' AND
       C.cd_terzo = B.cd_terzo;

   COMMENT ON TABLE "V_ESTRAI_IMPONIBILE_CUD"  IS 'View di estrazione compensi per elaborazione CUD';
