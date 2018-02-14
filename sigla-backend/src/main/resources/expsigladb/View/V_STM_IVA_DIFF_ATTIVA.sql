--------------------------------------------------------
--  DDL for View V_STM_IVA_DIFF_ATTIVA
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_STM_IVA_DIFF_ATTIVA" ("ESERCIZIO", "CD_CDS", "CD_UNITA_ORGANIZZATIVA", "CD_CDS_ORIGINE", "CD_UO_ORIGINE", "CD_TIPO_SEZIONALE", "TI_FATTURA", "DATA_REGISTRAZIONE", "NUMERO_PROGRESSIVO", "DATA_EMISSIONE", "NUMERO_FATTURA", "PROTOCOLLO_IVA", "PROTOCOLLO_IVA_GENERALE", "COMM_IST_TESTATA", "CODICE_ANAGRAFICO", "RAGIONE_SOCIALE", "IMPONIBILE_DETTAGLIO", "IVA_DETTAGLIO", "IVA_INDETRAIBILE_DETTAGLIO", "TOTALE_DETTAGLIO", "COMM_IST_DETTAGLIO", "CODICE_IVA", "PERCENTUALE_IVA", "DESCRIZIONE_IVA", "FL_IVA_DETRAIBILE", "PERCENTUALE_IVA_DETRAIBILE", "INTRA_UE", "BOLLA_DOGANALE", "SPEDIZIONIERE", "ESIGIBILITA_DIFF", "DATA_ESIGIBILITA_DIFF", "PROGRESSIVO_RIGA") AS 
  SELECT
-- =================================================================================================
--
-- Date: 18/12/2002
-- Version: 3.0
--
-- Vista per la stampa dell'IVA attiva ad esigibilità differira
--
-- History:
--
-- Date: 10/06/2002
-- Version: 1.0
-- Creazione
--
-- Date: 18/12/2002
-- Version: 3.0
--
-- Revisione vista per nuova gestione IVA, corretti errori in estrazione
--
-- Body:
--
-- =================================================================================================
       A.esercizio,
       A.cd_cds,
       A.cd_unita_organizzativa,
       A.cd_cds_origine,
       A.cd_uo_origine,
       A.cd_tipo_sezionale,
       A.ti_fattura,
       A.dt_registrazione,
       A.pg_fattura_attiva,
       A.dt_emissione,
       LPAD(A.protocollo_iva,10,' '),
       A.protocollo_iva,
       A.protocollo_iva_generale,
       'C',
       A.cd_terzo,
       LTRIM(RTRIM(A.ragione_sociale || ' ' || A.cognome || ' ' || A.nome)),
       DECODE(A.ti_fattura, 'C', (SUM(B.im_imponibile) * -1), SUM(B.im_imponibile)),
       DECODE(A.ti_fattura, 'C', (SUM(B.im_iva) * -1), SUM(B.im_iva)),
       0,
       DECODE(A.ti_fattura, 'C', (SUM(B.im_imponibile + B.im_iva) * -1), SUM(B.im_imponibile + B.im_iva)),
       'C',
       B.cd_voce_iva,
       C.percentuale,
       C.ds_voce_iva,
       'Y',
       100,
       A.fl_intra_ue,
       'N',
       'N',
       A.fl_liquidazione_differita,
       TRUNC(B.data_esigibilita_iva),
       progressivo_riga
FROM   FATTURA_ATTIVA A,
       FATTURA_ATTIVA_RIGA B,
       VOCE_IVA C,
       CONFIGURAZIONE_CNR Cnr
WHERE  A.fl_liquidazione_differita = 'Y' AND
       B.cd_cds = A.cd_cds AND
       B.cd_unita_organizzativa = A.cd_unita_organizzativa AND
       B.esercizio = A.esercizio AND
       B.pg_fattura_attiva = A.pg_fattura_attiva AND
       C.cd_voce_iva = B.cd_voce_iva AND
       Cnr.ESERCIZIO = 0 AND
       Cnr.cd_chiave_primaria = 'SPLIT_PAYMENT'
       AND Cnr.cd_chiave_secondaria = 'ATTIVA'
       AND Cnr.cd_unita_funzionale = '*'
       AND a.dt_emissione <   NVL (Cnr.dt01, a.dt_emissione)
GROUP BY A.esercizio,
         A.cd_cds,
         A.cd_unita_organizzativa,
         A.cd_cds_origine,
         A.cd_uo_origine,
         A.cd_tipo_sezionale,
         A.ti_fattura,
         A.dt_registrazione,
         A.pg_fattura_attiva,
         A.dt_emissione,
         LPAD(A.protocollo_iva,10,' '),
         A.protocollo_iva,
         A.protocollo_iva_generale,
         'C',
         A.cd_terzo,
         LTRIM(RTRIM(A.ragione_sociale || ' ' || A.cognome || ' ' || A.nome)),
         0,
         'C',
         B.cd_voce_iva,
         C.percentuale,
         C.ds_voce_iva,
         'Y',
         100,
         A.fl_intra_ue,
         'N',
         'N',
         A.fl_liquidazione_differita,
         TRUNC(B.data_esigibilita_iva),
        progressivo_riga;
