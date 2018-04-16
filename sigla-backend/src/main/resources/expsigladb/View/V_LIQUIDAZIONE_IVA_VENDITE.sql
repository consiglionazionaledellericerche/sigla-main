--------------------------------------------------------
--  DDL for View V_LIQUIDAZIONE_IVA_VENDITE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_LIQUIDAZIONE_IVA_VENDITE" ("CD_CDS", "CD_UNITA_ORGANIZATIVA", "ESERCIZIO", "NUMERO_PROGRESSIVO", "DATA_REGISTRAZIONE", "DATA_EMISSIONE", "PROTOCOLLO_IVA", "PROTOCOLLO_IVA_GENERALE", "SEZIONE_LIQUIDAZIONE", "CD_CDS_ORIGINE", "CD_UO_ORIGINE", "CD_TIPO_SEZIONALE", "TI_FATTURA", "TI_ISTITUZ_COMMERC", "FL_IVA_DETRAIBILE", "PERCENTUALE_IVA_DETRAIBILE", "IVA_DETTAGLIO", "IVA_INDETRAIBILE_DETTAGLIO", "ESIGIBILITA_DIFF", "TIPO_DOCUMENTO_FT_PAS", "TIPO_AUTOFATTURA", "FL_SPLIT_PAYMENT") AS 
  (SELECT
-- =================================================================================================
--
-- Date: 31/01/2003
-- Version: 1.2
--
-- Vista per il calcolo della liquidazione iva per vendite
--
-- History:
--
-- Date: 10/06/2002
-- Version: 1.0
--
-- Creazione
--
-- Date: 17/01/2003
-- Version: 1.1
--
-- Fix errore per cui non erano estratte, in liquidazione, le autofatture
-- Inserito il controllo di escludere le fatture cancellate
--
-- Date: 31/01/2003
-- Version: 1.2
--
-- Inserita tipizzazione in attributo TIPO_AUTOFATTURA per distinguere quelle intra_ue da tutte le
-- altre. Dominio 001 = Altre autofatture 002 = intra_ue
--
-- Body:
--
-- =================================================================================================
             a.cd_cds, a.cd_unita_organizzativa, a.esercizio,
             a.pg_fattura_attiva, a.dt_registrazione, a.dt_emissione,
             a.protocollo_iva, a.protocollo_iva_generale, 'VF',
             a.cd_cds_origine, a.cd_uo_origine, a.cd_tipo_sezionale,
             a.ti_fattura, 'C', 'Y', 100,
             DECODE (a.ti_fattura,
                     'C', (SUM (NVL (b.im_iva, 0)) * -1),
                     SUM (NVL (b.im_iva, 0))
                    ),
             0, a.fl_liquidazione_differita, NULL, NULL, 'N' fl_split_payment
        FROM fattura_attiva a, fattura_attiva_riga b, configurazione_cnr cnr
       WHERE a.dt_cancellazione IS NULL
         AND b.cd_cds = a.cd_cds
         AND b.cd_unita_organizzativa = a.cd_unita_organizzativa
         AND b.esercizio = a.esercizio
         AND b.pg_fattura_attiva = a.pg_fattura_attiva
         AND cnr.esercizio = 0
         AND cnr.cd_chiave_primaria = 'SPLIT_PAYMENT'
         AND cnr.cd_chiave_secondaria = 'ATTIVA'
         AND cnr.cd_unita_funzionale = '*'
         AND a.dt_emissione < NVL (cnr.dt01, a.dt_emissione)
    GROUP BY a.cd_cds,
             a.cd_unita_organizzativa,
             a.esercizio,
             a.pg_fattura_attiva,
             a.dt_registrazione,
             a.dt_emissione,
             a.protocollo_iva,
             a.protocollo_iva_generale,
             'VF',
             a.cd_cds_origine,
             a.cd_uo_origine,
             a.cd_tipo_sezionale,
             a.ti_fattura,
             'C',
             'Y',
             100,
             0,
             a.fl_liquidazione_differita,
             NULL,
             NULL
    UNION ALL
    SELECT   a.cd_cds, a.cd_unita_organizzativa, a.esercizio,
             a.pg_fattura_attiva, a.dt_registrazione, a.dt_emissione,
             a.protocollo_iva, a.protocollo_iva_generale, 'VF',
             a.cd_cds_origine, a.cd_uo_origine, a.cd_tipo_sezionale,
             a.ti_fattura, 'C', 'Y', 100,
             DECODE (a.ti_fattura,
                     'C', (SUM (NVL (b.im_iva, 0)) * -1),
                     SUM (NVL (b.im_iva, 0))
                    ),
             0, 'N', NULL, NULL, 'Y' fl_split_payment
        FROM fattura_attiva a, fattura_attiva_riga b, configurazione_cnr cnr
       WHERE a.dt_cancellazione IS NULL
         AND b.cd_cds = a.cd_cds
         AND b.cd_unita_organizzativa = a.cd_unita_organizzativa
         AND b.esercizio = a.esercizio
         AND b.pg_fattura_attiva = a.pg_fattura_attiva
         AND cnr.esercizio = 0
         AND cnr.cd_chiave_primaria = 'SPLIT_PAYMENT'
         AND cnr.cd_chiave_secondaria = 'ATTIVA'
         AND cnr.cd_unita_funzionale = '*'
         AND a.dt_emissione >= NVL (cnr.dt01, a.dt_emissione)
         AND a.fl_liquidazione_differita = 'N'
    GROUP BY a.cd_cds,
             a.cd_unita_organizzativa,
             a.esercizio,
             a.pg_fattura_attiva,
             a.dt_registrazione,
             a.dt_emissione,
             a.protocollo_iva,
             a.protocollo_iva_generale,
             'VF',
             a.cd_cds_origine,
             a.cd_uo_origine,
             a.cd_tipo_sezionale,
             a.ti_fattura,
             'C',
             'Y',
             100,
             0,
             'N',
             NULL,
             NULL
    UNION ALL
    SELECT   a.cd_cds, a.cd_unita_organizzativa, a.esercizio,
             a.pg_autofattura, a.dt_registrazione, a.dt_registrazione,
             a.protocollo_iva, a.protocollo_iva_generale, 'VA',
             a.cd_cds_origine, a.cd_uo_origine, a.cd_tipo_sezionale,
             a.ti_fattura, a.ti_istituz_commerc, 'Y', 100,
             DECODE (b.ti_fattura,
                     'C', (SUM (NVL (c.im_iva, 0)) * -1),
                     SUM (NVL (c.im_iva, 0))
                    ),
             0, a.fl_liquidazione_differita, b.ti_fattura,
             DECODE (a.fl_intra_ue, 'Y', '002',
                DECODE(a.fl_split_payment, 'Y', '003', '001')),
             a.fl_split_payment
        FROM autofattura a,
             fattura_passiva b,
             fattura_passiva_riga c,
             voce_iva, tipo_sezionale
       WHERE b.cd_cds = a.cd_cds_ft_passiva
         AND b.cd_unita_organizzativa = a.cd_uo_ft_passiva
         AND b.esercizio = a.esercizio
         AND b.pg_fattura_passiva = a.pg_fattura_passiva
         AND c.cd_cds = b.cd_cds
         AND c.cd_unita_organizzativa = b.cd_unita_organizzativa
         AND c.esercizio = b.esercizio
         AND c.pg_fattura_passiva = b.pg_fattura_passiva
         AND voce_iva.cd_voce_iva = c.cd_voce_iva
         AND (   voce_iva.fl_autofattura = 'Y'
              OR b.fl_intra_ue = 'Y'
              OR b.fl_extra_ue = 'Y'
              OR b.fl_san_marino_senza_iva = 'Y'
              OR a.fl_split_payment = 'Y'
             )
          and tipo_sezionale.cd_tipo_sezionale = a.cd_tipo_sezionale   
          and tipo_sezionale.fl_reg_tardiva = 'N' 
    GROUP BY a.cd_cds,
             a.cd_unita_organizzativa,
             a.esercizio,
             a.pg_autofattura,
             a.dt_registrazione,
             a.dt_registrazione,
             a.protocollo_iva,
             a.protocollo_iva_generale,
             'VA',
             a.cd_cds_origine,
             a.cd_uo_origine,
             a.cd_tipo_sezionale,
             a.ti_fattura,
             a.ti_istituz_commerc,
             'Y',
             100,
             0,
             a.fl_liquidazione_differita,
             b.ti_fattura,
             DECODE (a.fl_intra_ue, 'Y', '002',
                DECODE(a.fl_split_payment, 'Y', '003', '001')),
             a.fl_split_payment);
