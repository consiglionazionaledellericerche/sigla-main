--------------------------------------------------------
--  DDL for View V_STM_IVA_DIFF_PASSIVA
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_STM_IVA_DIFF_PASSIVA" ("ESERCIZIO", "CD_CDS", "CD_UNITA_ORGANIZZATIVA", "CD_CDS_ORIGINE", "CD_UO_ORIGINE", "CD_TIPO_SEZIONALE", "TI_FATTURA", "DATA_REGISTRAZIONE", "NUMERO_PROGRESSIVO", "DATA_EMISSIONE", "NUMERO_FATTURA", "PROTOCOLLO_IVA", "PROTOCOLLO_IVA_GENERALE", "COMM_IST_TESTATA", "CODICE_ANAGRAFICO", "RAGIONE_SOCIALE", "IMPONIBILE_DETTAGLIO", "IVA_DETTAGLIO", "IVA_INDETRAIBILE_DETTAGLIO", "TOTALE_DETTAGLIO", "COMM_IST_DETTAGLIO", "CODICE_IVA", "PERCENTUALE_IVA", "DESCRIZIONE_IVA", "FL_IVA_DETRAIBILE", "PERCENTUALE_IVA_DETRAIBILE", "INTRA_UE", "BOLLA_DOGANALE", "SPEDIZIONIERE", "ESIGIBILITA_DIFF", "DATA_ESIGIBILITA_DIFF", "PROGRESSIVO_RIGA") AS 
  SELECT
-- =================================================================================================
--
-- Date: 18/12/2002
-- Version: 3.0
--
-- Vista per la stampa dell'IVA passiva ad esigibilità differira
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
       A.data_registrazione,
       A.numero_progressivo,
       A.data_emissione,
       A.numero_fattura,
       A.protocollo_iva,
       A.protocollo_iva_generale,
       A.comm_ist_testata,
       A.codice_anagrafico,
       A.ragione_sociale,
       A.imponibile_dettaglio,
       A.iva_dettaglio,
       (A.iva_dettaglio * ((100 - A.percentuale_iva_detraibile) / 100)),
       A.totale_dettaglio,
       A.comm_ist_dettaglio,
       A.codice_iva,
       A.percentuale_iva,
       A.descrizione_iva,
       A.fl_iva_detraibile,
       A.percentuale_iva_detraibile,
       A.intra_ue,
       A.bolla_doganale,
       A.spedizioniere,
       A.esigibilita_diff,
       A.data_esigibilita_diff,
       A.PROGRESSIVO_RIGA
FROM   V_STM_IVA_DIFF_PASSIVA_PRE A
WHERE  A.esigibilita_diff = 'Y';
