--------------------------------------------------------
--  DDL for View VP_REGISTRO_IVA_VENDITE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VP_REGISTRO_IVA_VENDITE" ("CD_CDS", "CD_UNITA_ORGANIZZATIVA", "ESERCIZIO", "PG_FATTURA", "TI_FATTURA", "CD_CDS_ORIGINE", "CD_UO_ORIGINE", "CD_TIPO_SEZIONALE", "DATA_REGISTRAZIONE", "DATA_EMISSIONE", "PROTOCOLLO_IVA", "PROTOCOLLO_IVA_GENERALE", "COMM_IST_TESTATA", "CODICE_ANAGRAFICO", "RAGIONE_SOCIALE", "IMPONIBILE_DETTAGLIO", "IVA_DETTAGLIO", "IVA_INDETRAIBILE_DETTAGLIO", "TOTALE_DETTAGLIO", "COMM_IST_DETTAGLIO", "CODICE_IVA", "PERCENTUALE_IVA", "DESCRIZIONE_IVA", "FL_IVA_DETRAIBILE", "PERCENTUALE_IVA_DETRAIBILE", "GRUPPO_IVA", "DESCRIZIONE_GRUPPO_IVA", "INTRA_UE", "BOLLA_DOGANALE", "CODICE_VALUTA", "IMPORTO_VALUTA", "FLAG_ESIGIBILITA_DIFF", "DATA_ESIGIBILITA_IVA", "TIPO_DOCUMENTO_FT_PAS") AS 
  SELECT
--
-- Date: 03/04/2002
-- Version: 1.0
--
-- Vista di estrazione dei documenti di vendita
--
-- History:
--
-- Date: 03/04/2002
-- Version: 1.0
-- Creazione
--
-- Body:
--
            a.cd_cds cd_cds, a.cd_unita_organizzativa cd_unita_organizzativa,
            a.esercizio esercizio, a.pg_fattura_attiva pg_fattura,
            a.ti_fattura tipologia_documento, a.cd_cds_origine cd_cds_origine,
            a.cd_uo_origine cd_uo_origine,
            a.cd_tipo_sezionale cd_tipo_sezionale,
            TRUNC (a.dt_registrazione) data_registrazione,
            TRUNC (a.dt_emissione) data_emissione,
            a.protocollo_iva protocollo_iva,
            a.protocollo_iva_generale protocollo_iva_generale,
            'C' comm_ist_testata, a.cd_terzo codice_anagrafico,
            a.ragione_sociale ragione_sociale,
            DECODE (a.ti_fattura,
                    'C', (SUM (NVL (b.im_imponibile, 0)) * -1),
                    SUM (NVL (b.im_imponibile, 0))
                   ) imponibile_dettaglio,
            DECODE (a.ti_fattura,
                    'C', (SUM (NVL (b.im_iva, 0)) * -1),
                    SUM (NVL (b.im_iva, 0))
                   ) iva_dettaglio,
            0 iva_indetraibile_dettaglio,
            DECODE (a.ti_fattura,
                    'C', (  SUM (NVL (b.im_imponibile, 0) + NVL (b.im_iva, 0))
                          * -1),
                    SUM (NVL (b.im_imponibile, 0) + NVL (b.im_iva, 0))
                   ) totale_dettaglio,
            'C' comm_ist_dettaglio, b.cd_voce_iva codice_iva,
            c.percentuale percentuale_iva, c.ds_voce_iva descrizione_iva,
            'Y' fl_iva_detraibile, 100 percentuale_iva_detraibile,
            NVL (c.cd_gruppo_iva,
                 '***** GRUPPO IVA INESISTENTE *****'
                ) gruppo_iva,
            d.ds_gruppo_iva descrizione_gruppo_iva,
            NVL (a.fl_intra_ue, 'N') flag_intra_ue, 'N' bolla_doganale,
            NULL codice_valuta, NULL importo_valuta,
            NVL (a.fl_liquidazione_differita, 'N') flag_esigibilita_diff,
            TRUNC (b.data_esigibilita_iva) data_esigibilita_iva,
            NULL tipo_documento_ft_pas
       FROM fattura_attiva a, fattura_attiva_riga b, voce_iva c, gruppo_iva d
      WHERE b.cd_cds = a.cd_cds
        AND b.cd_unita_organizzativa = a.cd_unita_organizzativa
        AND b.esercizio = a.esercizio
        AND b.pg_fattura_attiva = a.pg_fattura_attiva
        AND c.cd_voce_iva(+) = b.cd_voce_iva
        AND d.cd_gruppo_iva(+) = c.cd_gruppo_iva
   GROUP BY a.cd_cds,
            a.cd_unita_organizzativa,
            a.esercizio,
            a.pg_fattura_attiva,
            a.ti_fattura,
            a.cd_cds_origine,
            a.cd_uo_origine,
            a.cd_tipo_sezionale,
            TRUNC (a.dt_registrazione),
            a.protocollo_iva,
            TRUNC (a.dt_emissione),
            a.protocollo_iva_generale,
            'C',
            a.cd_terzo,
            a.ragione_sociale,
            0,
            'C',
            b.cd_voce_iva,
            c.percentuale,
            c.ds_voce_iva,
            'Y',
            100,
            NVL (c.cd_gruppo_iva, '***** GRUPPO IVA INESISTENTE *****'),
            d.ds_gruppo_iva,
            NVL (a.fl_intra_ue, 'N'),
            'N',
            NULL,
            NULL,
            NVL (a.fl_liquidazione_differita, 'N'),
            TRUNC (b.data_esigibilita_iva),
            NULL
   UNION ALL
   SELECT   a.cd_cds cd_cds, a.cd_unita_organizzativa cd_unita_organizzativa,
            a.esercizio esercizio, a.pg_fattura_passiva pg_fattura,
            a.ti_fattura tipologia_documento, b.cd_cds_origine cd_cds_origine,
            b.cd_uo_origine cd_uo_origine,
            a.cd_tipo_sezionale cd_tipo_sezionale,
            TRUNC (a.dt_registrazione) data_registrazione,
            TRUNC (a.dt_registrazione) dt_emissione,
            a.protocollo_iva protocollo_iva,
            a.protocollo_iva_generale protocollo_iva_generale,
            'C' comm_ist_testata, b.cd_terzo codice_anagrafico,
            b.ragione_sociale ragione_sociale,
            DECODE (b.ti_fattura,
                    'C', (SUM (NVL (c.im_imponibile, 0)) * -1),
                    SUM (NVL (c.im_imponibile, 0))
                   ) imponibile_dettaglio,
            DECODE (b.ti_fattura,
                    'C', (SUM (NVL (c.im_iva, 0)) * -1),
                    SUM (NVL (c.im_iva, 0))
                   ) iva_dettaglio,
            0 iva_indetraibile_dettaglio,
            DECODE (b.ti_fattura,
                    'C', (  SUM (NVL (c.im_imponibile, 0) + NVL (c.im_iva, 0))
                          * -1),
                    SUM (NVL (c.im_imponibile, 0) + NVL (c.im_iva, 0))
                   ) totale_dettaglio,
            'C' comm_ist_dettaglio, c.cd_voce_iva codice_iva,
            d.percentuale percentuale_iva, d.ds_voce_iva descrizione_iva,
            'Y' fl_iva_detraibile, 100 percentuale_iva_detraibile,
            NVL (d.cd_gruppo_iva,
                 '***** GRUPPO IVA INESISTENTE *****'
                ) gruppo_iva,
            e.ds_gruppo_iva descrizione_gruppo_iva,
            NVL (b.fl_intra_ue, 'N') flag_intra_ue, 'N' bolla_doganale,
            NULL codice_valuta, NULL importo_valuta,
            NVL (a.fl_liquidazione_differita, 'N') flag_esigibilita_diff,
            TRUNC (a.data_esigibilita_iva) data_esigibilita_iva, b.ti_fattura
       FROM autofattura a,
            fattura_passiva b,
            fattura_passiva_riga c,
            voce_iva d,
            gruppo_iva e
      WHERE b.cd_cds = a.cd_cds_ft_passiva
        AND b.cd_unita_organizzativa = a.cd_uo_ft_passiva
        AND b.pg_fattura_passiva = a.pg_fattura_passiva
        AND b.esercizio = a.esercizio
        AND b.fl_autofattura = 'Y'
        AND c.cd_cds = b.cd_cds
        AND c.cd_unita_organizzativa = b.cd_unita_organizzativa
        AND c.esercizio = b.esercizio
        AND c.pg_fattura_passiva = b.pg_fattura_passiva
        AND d.cd_voce_iva(+) = c.cd_voce_iva
        AND e.cd_gruppo_iva(+) = d.cd_gruppo_iva
   GROUP BY a.cd_cds,
            a.cd_unita_organizzativa,
            a.esercizio,
            a.pg_fattura_passiva,
            a.ti_fattura,
            b.cd_cds_origine,
            b.cd_uo_origine,
            a.cd_tipo_sezionale,
            TRUNC (a.dt_registrazione),
            a.protocollo_iva,
            TRUNC (a.dt_registrazione),
            a.protocollo_iva_generale,
            'C',
            b.cd_terzo,
            b.ragione_sociale,
            0,
            'C',
            c.cd_voce_iva,
            d.percentuale,
            d.ds_voce_iva,
            'Y',
            100,
            NVL (d.cd_gruppo_iva, '***** GRUPPO IVA INESISTENTE *****'),
            e.ds_gruppo_iva,
            NVL (b.fl_intra_ue, 'N'),
            'N',
            NULL,
            NULL,
            NVL (a.fl_liquidazione_differita, 'N'),
            TRUNC (a.data_esigibilita_iva),
            b.ti_fattura ;
