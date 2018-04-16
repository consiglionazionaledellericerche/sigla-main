--------------------------------------------------------
--  DDL for View V_REGISTRO_IVA_VENDITE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_REGISTRO_IVA_VENDITE" ("CD_CDS", "CD_UNITA_ORGANIZZATIVA", "ESERCIZIO", "CD_CDS_ORIGINE", "CD_UO_ORIGINE", "CD_TIPO_SEZIONALE", "TI_FATTURA", "DATA_REGISTRAZIONE", "NUMERO_PROGRESSIVO", "DATA_EMISSIONE", "NUMERO_FATTURA", "PROTOCOLLO_IVA", "PROTOCOLLO_IVA_GENERALE", "COMM_IST_TESTATA", "CODICE_ANAGRAFICO", "RAGIONE_SOCIALE", "IMPONIBILE_DETTAGLIO", "IVA_DETTAGLIO", "IVA_INDETRAIBILE_DETTAGLIO", "TOTALE_DETTAGLIO", "COMM_IST_DETTAGLIO", "CODICE_IVA", "PERCENTUALE_IVA", "DESCRIZIONE_IVA", "FL_IVA_DETRAIBILE", "PERCENTUALE_IVA_DETRAIBILE", "GRUPPO_IVA", "DESCRIZIONE_GRUPPO_IVA", "INTRA_UE", "BOLLA_DOGANALE", "SPEDIZIONIERE", "CODICE_VALUTA", "IMPORTO_VALUTA", "ESIGIBILITA_DIFF", "DATA_ESIGIBILITA_DIFF", "TIPO_DOCUMENTO_FT_PAS", "EXTRA_UE", "FL_SPLIT_PAYMENT") AS 
  SELECT
-- =================================================================================================
--
-- Date: 09/12/2002
-- Version: 1.3
--
-- Vista associativa per i registri iva relativo alle vendite
--
-- History:
--
-- Date: 10/06/2002
-- Version: 1.0
-- Creazione
--
-- Date: 11/06/2002
-- Version: 1.1
-- Modificata where per Autofatture su fatture passive
--
-- Date: 14/11/2002
-- Version: 1.2
--
-- Fix errore per cui era estratta la sola ragione sociale del fornitore e non la concatenazione anche con
-- nome e cognome
-- Recuperata l'informazione del tipo commerciale o istituzionale del dettaglio.
-- Eliminati gli NVL inutili per la nuoiva base dati.
-- Eliminate le outer join per VOCE_IVA e GRUPPO_IVA
-- Fix esposizione del progressivo identificativo della fattura passiva invece che l'identificativo
-- della autofattura
-- Introdotta sum in importo divisa con autofatture
-- Corretta join autofattura fattura_passiva
-- Eliminata la condizione fl_autofattura = 'Y'
--
-- Date: 09/12/2002
-- Version: 1.3
--
-- Aggiunta l'estrazione dell'indicatore di fattura spedizioniere per allineamento alla corrispondente
-- vista degli acquisti.
-- Esposto il numero della fattura attiva come conversione in carattere del protocollo_iva
--
-- Body:
--
-- =================================================================================================
            a.cd_cds, a.cd_unita_organizzativa, a.esercizio, a.cd_cds_origine,
            a.cd_uo_origine, a.cd_tipo_sezionale, a.ti_fattura,
            a.dt_registrazione, a.pg_fattura_attiva, a.dt_emissione,
            LPAD (a.protocollo_iva, 10, ' '), a.protocollo_iva,
            a.protocollo_iva_generale, 'C', a.cd_terzo,
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
            'C', b.cd_voce_iva, c.percentuale, c.ds_voce_iva, c.fl_detraibile,
            c.percentuale_detraibilita, c.cd_gruppo_iva, d.ds_gruppo_iva,
            a.fl_intra_ue, 'N', 'N', a.cd_divisa, SUM (b.im_totale_divisa),
            a.fl_liquidazione_differita,
            DECODE (a.dt_emissione - cnr.dt01,
                    ABS (a.dt_emissione - cnr.dt01), NULL,
                    TRUNC (b.data_esigibilita_iva)
                   ),
            TO_CHAR (NULL), a.fl_extra_ue,
            DECODE (a.dt_emissione - cnr.dt01,
                    ABS (a.dt_emissione - cnr.dt01),
                    a.fl_liquidazione_differita, 'N') fl_split_payment
       FROM fattura_attiva a,
            fattura_attiva_riga b,
            voce_iva c,
            gruppo_iva d,
            configurazione_cnr cnr
      WHERE a.dt_cancellazione IS NULL
        AND b.cd_cds = a.cd_cds
        AND b.cd_unita_organizzativa = a.cd_unita_organizzativa
        AND b.esercizio = a.esercizio
        AND b.pg_fattura_attiva = a.pg_fattura_attiva
        AND c.cd_voce_iva = b.cd_voce_iva
        AND d.cd_gruppo_iva = c.cd_gruppo_iva
        AND cnr.esercizio = 0
        AND cnr.cd_chiave_primaria = 'SPLIT_PAYMENT'
        AND cnr.cd_chiave_secondaria = 'ATTIVA'
        AND cnr.cd_unita_funzionale = '*'
   GROUP BY a.cd_cds,
            a.cd_unita_organizzativa,
            a.esercizio,
            a.cd_cds_origine,
            a.cd_uo_origine,
            a.cd_tipo_sezionale,
            a.ti_fattura,
            a.dt_registrazione,
            a.pg_fattura_attiva,
            a.dt_emissione,
            TO_CHAR (NULL),
            a.protocollo_iva,
            a.protocollo_iva_generale,
            'C',
            a.cd_terzo,
            LTRIM (RTRIM (a.ragione_sociale || ' ' || a.cognome || ' '
                          || a.nome
                         )
                  ),
            0,
            'C',
            b.cd_voce_iva,
            c.percentuale,
            c.ds_voce_iva,
            c.fl_detraibile,
            c.percentuale_detraibilita,
            c.cd_gruppo_iva,
            d.ds_gruppo_iva,
            a.fl_intra_ue,
            'N',
            'N',
            a.cd_divisa,
            a.fl_liquidazione_differita,
            TRUNC (b.data_esigibilita_iva),
            a.fl_extra_ue,
            cnr.dt01
   UNION ALL
   SELECT   a.cd_cds, a.cd_unita_organizzativa, a.esercizio, a.cd_cds_origine,
            a.cd_uo_origine, a.cd_tipo_sezionale, a.ti_fattura,
            TRUNC (a.dt_registrazione), a.pg_autofattura,
            TRUNC (a.dt_registrazione), LPAD (a.protocollo_iva, 10, ' '),
            a.protocollo_iva, a.protocollo_iva_generale, 'C', b.cd_terzo,
            LTRIM (RTRIM (b.ragione_sociale || ' ' || b.cognome || ' '
                          || b.nome
                         )
                  ),
            DECODE (b.ti_fattura,
                    'C', (SUM (c.im_imponibile) * -1),
                    SUM (c.im_imponibile)
                   ),
            DECODE (b.ti_fattura, 'C', (SUM (c.im_iva) * -1), SUM (c.im_iva)),
            0,
            DECODE (b.ti_fattura,
                    'C', (SUM (c.im_imponibile + c.im_iva) * -1),
                    SUM (c.im_imponibile + c.im_iva)
                   ),
            'C', c.cd_voce_iva, d.percentuale, d.ds_voce_iva, d.fl_detraibile,
            d.percentuale_detraibilita, d.cd_gruppo_iva, e.ds_gruppo_iva,
            b.fl_intra_ue, b.fl_bolla_doganale, b.fl_spedizioniere,
            b.cd_divisa, SUM (c.im_totale_divisa),
            a.fl_liquidazione_differita,
            DECODE (TRUNC (a.dt_registrazione) - cnr.dt01,
                    ABS (TRUNC (a.dt_registrazione) - cnr.dt01), NULL,
                    TRUNC (a.data_esigibilita_iva)
                   ),
            b.ti_fattura, b.fl_extra_ue, a.fl_split_payment
       FROM autofattura a,
            fattura_passiva b,
            fattura_passiva_riga c,
            voce_iva d,
            gruppo_iva e,
            configurazione_cnr cnr
      WHERE b.cd_cds = a.cd_cds_ft_passiva
        AND b.cd_unita_organizzativa = a.cd_uo_ft_passiva
        AND b.esercizio = a.esercizio
        AND b.pg_fattura_passiva = a.pg_fattura_passiva
        AND c.cd_cds = b.cd_cds
        AND c.cd_unita_organizzativa = b.cd_unita_organizzativa
        AND c.esercizio = b.esercizio
        AND c.pg_fattura_passiva = b.pg_fattura_passiva
        AND d.cd_voce_iva = c.cd_voce_iva
        AND e.cd_gruppo_iva = d.cd_gruppo_iva
        AND cnr.esercizio = 0
        AND cnr.cd_chiave_primaria = 'SPLIT_PAYMENT'
        AND cnr.cd_chiave_secondaria = 'ATTIVA'
        AND cnr.cd_unita_funzionale = '*'
        AND (   d.fl_autofattura = 'Y'
             OR b.fl_intra_ue = 'Y'
             OR b.fl_extra_ue = 'Y'
             OR b.fl_san_marino_senza_iva = 'Y'
             OR a.fl_split_payment = 'Y'
            )
   GROUP BY a.cd_cds,
            a.cd_unita_organizzativa,
            a.esercizio,
            a.cd_cds_origine,
            a.cd_uo_origine,
            a.cd_tipo_sezionale,
            a.ti_fattura,
            TRUNC (a.dt_registrazione),
            a.pg_autofattura,
            TRUNC (a.dt_registrazione),
            TO_CHAR (NULL),
            a.protocollo_iva,
            a.protocollo_iva_generale,
            'C',
            b.cd_terzo,
            LTRIM (RTRIM (b.ragione_sociale || ' ' || b.cognome || ' '
                          || b.nome
                         )
                  ),
            0,
            'C',
            c.cd_voce_iva,
            d.percentuale,
            d.ds_voce_iva,
            d.fl_detraibile,
            d.percentuale_detraibilita,
            d.cd_gruppo_iva,
            e.ds_gruppo_iva,
            b.fl_intra_ue,
            b.fl_bolla_doganale,
            b.fl_spedizioniere,
            b.cd_divisa,
            a.fl_liquidazione_differita,
            TRUNC (a.data_esigibilita_iva),
            b.ti_fattura,
            b.fl_extra_ue,
            a.fl_liquidazione_differita,
            a.fl_split_payment,
            cnr.dt01;
