--------------------------------------------------------
--  DDL for View V_LIQUID_IVA_ACQUISTI_PRE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_LIQUID_IVA_ACQUISTI_PRE" ("CD_CDS", "CD_UNITA_ORGANIZZATIVA", "ESERCIZIO", "NUMERO_PROGRESSIVO", "DATA_REGISTRAZIONE", "DATA_EMISSIONE", "SEZIONE_LIQUIDAZIONE", "CD_CDS_ORIGINE", "CD_UO_ORIGINE", "CD_TIPO_SEZIONALE", "TI_FATTURA", "TI_ISTITUZ_COMMERC", "FL_IVA_DETRAIBILE", "PERCENTUALE_IVA_DETRAIBILE", "IVA_DETTAGLIO", "IVA_INDETRAIBILE_DETTAGLIO", "ESIGIBILITA_DIFF", "TIPO_DOCUMENTO_FT_PAS", "TIPO_AUTOFATTURA", "FL_SPLIT_PAYMENT") AS 
  SELECT
-- =================================================================================================
--
-- Date: 03/02/2003
-- Version: 4.0
--
-- Vista preparativa per il calcolo della liquidazione iva per acquisti
--
-- History:
--
-- Date: 10/06/2002
-- Version: 1.0
-- Creazione
--
-- Date: 03/02/2003
-- Version: 4.0
--
-- Correzione della vista per includere le gestioni istituzionali San marino senza iva e intra_ue
--
-- Body:
--
-- =================================================================================================
            a.cd_cds, a.cd_unita_organizzativa, a.esercizio,
            a.pg_fattura_passiva, TRUNC (a.dt_registrazione),
            TRUNC (a.dt_registrazione), 'AF', a.cd_cds_origine,
            a.cd_uo_origine, a.cd_tipo_sezionale, a.ti_fattura,
            b.ti_istituz_commerc, c.fl_detraibile,
            DECODE (c.fl_detraibile, 'N', 0, c.percentuale_detraibilita),
            DECODE (a.ti_fattura, 'C', (SUM (b.im_iva) * -1), SUM (b.im_iva)),
            0, a.fl_liquidazione_differita, NULL, NULL, a.fl_split_payment
       FROM fattura_passiva a, fattura_passiva_riga b, voce_iva c
      WHERE a.dt_cancellazione IS NULL
        AND b.cd_cds = a.cd_cds
        AND b.cd_unita_organizzativa = a.cd_unita_organizzativa
        AND b.esercizio = a.esercizio
        AND b.pg_fattura_passiva = a.pg_fattura_passiva
        AND c.cd_voce_iva = b.cd_voce_iva
   GROUP BY a.cd_cds,
            a.cd_unita_organizzativa,
            a.esercizio,
            a.pg_fattura_passiva,
            TRUNC (a.dt_registrazione),
            TRUNC (a.dt_registrazione),
            'AF',
            a.cd_cds_origine,
            a.cd_uo_origine,
            a.cd_tipo_sezionale,
            a.ti_fattura,
            b.ti_istituz_commerc,
            c.fl_detraibile,
            DECODE (c.fl_detraibile, 'N', 0, c.percentuale_detraibilita),
            0,
            a.fl_liquidazione_differita,
            NULL,
            NULL,
            a.fl_split_payment;
