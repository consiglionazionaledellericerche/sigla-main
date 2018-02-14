--------------------------------------------------------
--  DDL for View V_LIQUIDAZIONE_IVA_ACQUISTI
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_LIQUIDAZIONE_IVA_ACQUISTI" ("CD_CDS", "CD_UNITA_ORGANIZZATIVA", "ESERCIZIO", "NUMERO_PROGRESSIVO", "DATA_REGISTRAZIONE", "DATA_EMISSIONE", "SEZIONE_LIQUIDAZIONE", "CD_CDS_ORIGINE", "CD_UO_ORIGINE", "CD_TIPO_SEZIONALE", "TI_FATTURA", "TI_ISTITUZ_COMMERC", "FL_IVA_DETRAIBILE", "PERCENTUALE_IVA_DETRAIBILE", "IVA_DETTAGLIO", "IVA_INDETRAIBILE_DETTAGLIO", "ESIGIBILITA_DIFF", "TIPO_DOCUMENTO_FT_PAS", "TIPO_AUTOFATTURA", "FL_SPLIT_PAYMENT") AS 
  SELECT
-- =================================================================================================
--
-- Date: 03/02/2003
-- Version: 4.0
--
-- Vista per il calcolo della liquidazione iva per acquisti
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
          a.numero_progressivo, a.data_registrazione, a.data_emissione,
          a.sezione_liquidazione, a.cd_cds_origine, a.cd_uo_origine,
          a.cd_tipo_sezionale, a.ti_fattura, a.ti_istituz_commerc,
          a.fl_iva_detraibile, a.percentuale_iva_detraibile, a.iva_dettaglio,
          (a.iva_dettaglio * ((100 - a.percentuale_iva_detraibile) / 100)),
          a.esigibilita_diff, a.tipo_documento_ft_pas, a.tipo_autofattura,
          a.fl_split_payment
     FROM v_liquid_iva_acquisti_pre a,FATTURA_PASSIVA B
      where
         b.cd_cds = a.cd_cds
        AND b.cd_unita_organizzativa = a.cd_unita_organizzativa
        AND b.esercizio = a.esercizio
        AND b.pg_fattura_passiva = a.numero_progressivo
        AND (to_char(B.DT_FATTURA_FORNITORE,'yyyy') = a.esercizio OR
             a.ti_istituz_commerc!='C' OR A.fl_split_payment!='N'  or b.im_totale_iva=0 or nvl(to_char(B.Data_protocollo,'yyyy'),b.esercizio) = B.esercizio);
