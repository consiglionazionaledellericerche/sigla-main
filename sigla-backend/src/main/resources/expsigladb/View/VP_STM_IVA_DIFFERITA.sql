--------------------------------------------------------
--  DDL for View VP_STM_IVA_DIFFERITA
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VP_STM_IVA_DIFFERITA" ("ID_REPORT", "SEZIONE", "TIPOLOGIA_RIGA", "SEQUENZA", "SOTTOGRUPPO", "TITOLO_UO", "TITOLO_REPORT", "CD_CDS_ORIGINE", "CD_UO_ORIGINE", "ESERCIZIO", "CD_TIPO_SEZIONALE", "DATA_REGISTRAZIONE", "NUMERO_PROGRESSIVO", "DATA_EMISSIONE", "NUMERO_FATTURA", "TIPO", "PROTOCOLLO_IVA", "PROTOCOLLO_IVA_GEN", "CODICE_TERZO", "RAGIONE_SOCIALE", "IMPONIBILE", "IVA", "IVA_INDETRAIBILE", "TOTALE", "CODICE_IVA", "ESIGIBILITA_DIFF", "DATA_ESIGIBILITA", "ESERCIZIO_RIF", "RAGIONE_SOCIALE_ENTE", "PARTITA_IVA_ENTE", "PROVVISORIO_DEFINITIVO", "ACQUISTO_VENDITA", "DESCRIZIONE_GRUPPO") AS 
  SELECT
-- =================================================================================================
--
-- Date: 24/02/2003
-- Version: 3.1
--
-- Vista di stampa riepilogo iva differita
--
-- History:
--
-- Date: 18/12/2002
-- Version: 3.0
--
-- Creazione vista
--
-- Date: 24/02/2003
-- Version: 3.1
--
-- Inserimento nuovi attributi per memorizzazione della ragione sociale e partita iva ente + esercizio
-- di riferimento
--
-- Body:
--
-- =================================================================================================
       id,
       chiave,
       tipo,
       sequenza,
       descrizione,
       attributo_1,
       attributo_2,
       attributo_3,
       attributo_4,
       importo_1,
       attributo_5,
       data_1,
       importo_2,
       data_2,
       attributo_6,
       attributo_7,
       importo_3,
       importo_4,
       importo_5,
       attributo_8,
       importo_6,
       importo_7,
       importo_8,
       importo_9,
       attributo_9,
       attributo_10,
       data_3,
       importo_20,
       attributo_36,
       attributo_37,
       attributo_38,
       attributo_39,
       attributo_40
FROM   REPORT_GENERICO
;

   COMMENT ON TABLE "VP_STM_IVA_DIFFERITA"  IS '*/
comment()';
