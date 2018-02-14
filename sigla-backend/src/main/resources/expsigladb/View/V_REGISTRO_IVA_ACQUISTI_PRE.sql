--------------------------------------------------------
--  DDL for View V_REGISTRO_IVA_ACQUISTI_PRE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_REGISTRO_IVA_ACQUISTI_PRE" ("CD_CDS", "CD_UNITA_ORGANIZZATIVA", "ESERCIZIO", "CD_CDS_ORIGINE", "CD_UO_ORIGINE", "CD_TIPO_SEZIONALE", "TI_FATTURA", "DATA_REGISTRAZIONE", "NUMERO_PROGRESSIVO", "DATA_EMISSIONE", "NUMERO_FATTURA", "PROTOCOLLO_IVA", "PROTOCOLLO_IVA_GENERALE", "COMM_IST_TESTATA", "CODICE_ANAGRAFICO", "RAGIONE_SOCIALE", "IMPONIBILE_DETTAGLIO", "IVA_DETTAGLIO", "IVA_INDETRAIBILE_DETTAGLIO", "TOTALE_DETTAGLIO", "COMM_IST_DETTAGLIO", "CODICE_IVA", "PERCENTUALE_IVA", "DESCRIZIONE_IVA", "FL_IVA_DETRAIBILE", "PERCENTUALE_IVA_DETRAIBILE", "GRUPPO_IVA", "DESCRIZIONE_GRUPPO_IVA", "INTRA_UE", "BOLLA_DOGANALE", "SPEDIZIONIERE", "CODICE_VALUTA", "IMPORTO_VALUTA", "ESIGIBILITA_DIFF", "DATA_ESIGIBILITA_DIFF", "EXTRA_UE", "FL_SPLIT_PAYMENT") AS 
  SELECT
-- =================================================================================================
--
-- Date: 09/12/2002
-- Version: 1.3
--
-- Vista di preparazione per permettere di compilare il registro iva relativo agli acquisti
--
-- History:
--
-- Date: 10/06/2002
-- Version: 1.0
-- Creazione
--
-- Date: 11/11/2002
-- Version: 1.1
--
-- Fix errore interno 2842. Esclusione, dalla stampa registro iva acquisti, delle fatture annullate.
--
-- Date: 14/11/2002
-- Version: 1.2
--
-- Corretta la vista per non filtrare le operazioni di tipo commerciale o istituzionale. Tale selezione �-- operata dalla procedura in base a quanto definito in TIPO_SEZIONALE.ti_istituz_commerc.
-- Aggiunti i campi identificativi del documento per il fornitore
-- Fix per cui il campo esterno COMM_IST_TESTATA non conteneva il tipo istituzionale o commerciale
-- del documento ma ancora il tipo documento.
-- Fix errore per cui era estratta la sola ragione sociale del fornitore e non la concatenazione anche con
-- nome e cognome
-- Recuperata l'informazione del tipo commerciale o istituzionale del dettaglio.
-- Eliminati gli NVL inutili per la nuoiva base dati.
-- Eliminate le outer join per VOCE_IVA e GRUPPO_IVA
--
-- Date: 09/12/2002
-- Version: 1.3
--
-- Aggiunta l'estrazione dell'indicatore di fattura dello spedizioniere
--
-- Body:
--
-- =================================================================================================
            a.cd_cds, a.cd_unita_organizzativa, a.esercizio, a.cd_cds_origine,
            a.cd_uo_origine, a.cd_tipo_sezionale, a.ti_fattura,
            TRUNC (a.dt_registrazione), a.pg_fattura_passiva,
            TRUNC (a.dt_fattura_fornitore), a.nr_fattura_fornitore,
            a.protocollo_iva, a.protocollo_iva_generale, a.ti_istituz_commerc,
            a.cd_terzo,
            LTRIM (RTRIM (a.ragione_sociale || ' ' || a.cognome || ' '
                          || a.nome
                         )
                  ),
            DECODE (a.ti_fattura,
                    'C', (SUM (b.im_imponibile) * -1),
                    SUM (b.im_imponibile)
                   ),
            DECODE (a.ti_fattura, 'C', (SUM (b.im_iva) * -1), SUM (b.im_iva)),
            0,
            DECODE (a.ti_fattura,
                    'C', (SUM (b.im_imponibile + b.im_iva) * -1),
                    SUM (b.im_imponibile + b.im_iva)
                   ),
            b.ti_istituz_commerc, b.cd_voce_iva, c.percentuale, c.ds_voce_iva,
            c.fl_detraibile,
            DECODE (c.fl_detraibile, 'N', 0, c.percentuale_detraibilita),
            c.cd_gruppo_iva, d.ds_gruppo_iva, a.fl_intra_ue,
            a.fl_bolla_doganale, a.fl_spedizioniere, a.cd_divisa,
            SUM (b.im_totale_divisa), fl_liquidazione_differita,
            data_esigibilita_iva, a.fl_extra_ue, a.fl_split_payment
       FROM fattura_passiva a,
            fattura_passiva_riga b,
            voce_iva c,
            gruppo_iva d
      WHERE a.dt_cancellazione IS NULL
        AND b.cd_cds = a.cd_cds
        AND b.cd_unita_organizzativa = a.cd_unita_organizzativa
        AND b.esercizio = a.esercizio
        AND b.pg_fattura_passiva = a.pg_fattura_passiva
        AND c.cd_voce_iva = b.cd_voce_iva
        AND d.cd_gruppo_iva = c.cd_gruppo_iva
   GROUP BY a.cd_cds,
            a.cd_unita_organizzativa,
            a.esercizio,
            a.cd_cds_origine,
            a.cd_uo_origine,
            a.cd_tipo_sezionale,
            a.ti_fattura,
            TRUNC (a.dt_registrazione),
            a.pg_fattura_passiva,
            TRUNC (a.dt_fattura_fornitore),
            a.nr_fattura_fornitore,
            a.protocollo_iva,
            a.protocollo_iva_generale,
            a.ti_istituz_commerc,
            a.cd_terzo,
            LTRIM (RTRIM (a.ragione_sociale || ' ' || a.cognome || ' '
                          || a.nome
                         )
                  ),
            0,
            b.ti_istituz_commerc,
            b.cd_voce_iva,
            c.percentuale,
            c.ds_voce_iva,
            c.fl_detraibile,
            DECODE (c.fl_detraibile, 'N', 0, c.percentuale_detraibilita),
            c.cd_gruppo_iva,
            d.ds_gruppo_iva,
            a.fl_intra_ue,
            a.fl_bolla_doganale,
            a.fl_spedizioniere,
            a.cd_divisa,
            fl_liquidazione_differita,
            data_esigibilita_iva,
            a.fl_extra_ue,
            a.fl_split_payment;
