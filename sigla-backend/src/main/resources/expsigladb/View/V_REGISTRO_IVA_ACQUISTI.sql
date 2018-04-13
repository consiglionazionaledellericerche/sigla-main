--------------------------------------------------------
--  DDL for View V_REGISTRO_IVA_ACQUISTI
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_REGISTRO_IVA_ACQUISTI" ("CD_CDS", "CD_UNITA_ORGANIZZATIVA", "ESERCIZIO", "CD_CDS_ORIGINE", "CD_UO_ORIGINE", "CD_TIPO_SEZIONALE", "TI_FATTURA", "DATA_REGISTRAZIONE", "NUMERO_PROGRESSIVO", "DATA_EMISSIONE", "NUMERO_FATTURA", "PROTOCOLLO_IVA", "PROTOCOLLO_IVA_GENERALE", "COMM_IST_TESTATA", "CODICE_ANAGRAFICO", "RAGIONE_SOCIALE", "IMPONIBILE_DETTAGLIO", "IVA_DETTAGLIO", "IVA_INDETRAIBILE_DETTAGLIO", "TOTALE_DETTAGLIO", "COMM_IST_DETTAGLIO", "CODICE_IVA", "PERCENTUALE_IVA", "DESCRIZIONE_IVA", "FL_IVA_DETRAIBILE", "PERCENTUALE_IVA_DETRAIBILE", "GRUPPO_IVA", "DESCRIZIONE_GRUPPO_IVA", "INTRA_UE", "BOLLA_DOGANALE", "SPEDIZIONIERE", "CODICE_VALUTA", "IMPORTO_VALUTA", "ESIGIBILITA_DIFF", "DATA_ESIGIBILITA_DIFF", "TIPO_DOCUMENTO_FT_PAS", "EXTRA_UE", "FL_SPLIT_PAYMENT") AS 
  SELECT
-- =================================================================================================
--
-- Date: 09/12/2002
-- Version: 1.2
--
-- Vista del registro iva relativo agli acquisti
--
-- History:
--
-- Date: 10/06/2002
-- Version: 1.0
-- Creazione
--
-- Date: 13/11/2002
-- Version: 1.1
--
-- Adeguamento alle corrispondenti modifiche sulla vista V_REGISTRO_IVA_ACQUISTI_PRE
-- Introdotto il campo TIPO_DOCUMENTO_FT_PAS per allineamento con le viste acquisti
--
-- Date: 09/12/2002
-- Version: 1.2
--
-- Aggiunta l'estrazione dell'indicatore di fattura dello spedizioniere
--
-- Body:
--
-- =================================================================================================
          a.cd_cds, a.cd_unita_organizzativa, a.esercizio, a.cd_cds_origine,
          a.cd_uo_origine, a.cd_tipo_sezionale, a.ti_fattura,
          a.data_registrazione, a.numero_progressivo, a.data_emissione,
          a.numero_fattura, a.protocollo_iva, a.protocollo_iva_generale,
          a.comm_ist_testata, a.codice_anagrafico, a.ragione_sociale,
          a.imponibile_dettaglio, a.iva_dettaglio,
          (a.iva_dettaglio * ((100 - a.percentuale_iva_detraibile) / 100)),
          a.totale_dettaglio, a.comm_ist_dettaglio, a.codice_iva,
          a.percentuale_iva, a.descrizione_iva, a.fl_iva_detraibile,
          a.percentuale_iva_detraibile, a.gruppo_iva,
          a.descrizione_gruppo_iva, a.intra_ue, a.bolla_doganale,
          a.spedizioniere, a.codice_valuta, a.importo_valuta,
          a.esigibilita_diff, a.data_esigibilita_diff, TO_CHAR (NULL),
          a.extra_ue, a.fl_split_payment
     FROM v_registro_iva_acquisti_pre a;
