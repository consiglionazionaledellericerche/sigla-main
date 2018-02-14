--------------------------------------------------------
--  DDL for View VP_STM_RIEPILOGATIVO_UO
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VP_STM_RIEPILOGATIVO_UO" ("ID_REPORT", "GRUPPO", "TIPOLOGIA_RIGA", "SEQUENZA", "SOTTOGRUPPO", "CODICE_SEZIONALE", "CODICE_GRUPPO_IVA", "CODICE_IVA", "SEZIONALE_DS", "IVA_DS", "IMPONIBILE", "IVA", "IVA_INDETRAIBILE", "TOTALE", "ESERCIZIO", "RAGIONE_SOCIALE_ENTE", "PARTITA_IVA_ENTE", "PROVVISORIO_DEFINITIVO", "ACQUISTI_VENDITE", "DESCRIZIONE_GRUPPO", "IMPONIBILE_SPLIT_PAYMENT", "IVA_SPLIT_PAYMENT") AS 
  SELECT
-- =================================================================================================
--
-- Date: 24/02/2003
-- Version: 3.1
--
-- Vista di stampa riepilogativo per U.O.
--
-- History:
--
-- Date: 10/12/2002
-- Version: 1.0
--
-- Creazione vista
--
-- Date: 17/12/2002
-- Version: 3.0
--
-- Allineamento vista alla nuova struttura della tabella REPORT_GENERICO e aggiunto campo per
-- record di stampa provvisoria o definitiva
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
          ID, chiave, tipo, sequenza, descrizione, attributo_1, attributo_2,
          attributo_3, attributo_4, attributo_5, importo_1, importo_2,
          importo_3, importo_4, importo_20, attributo_36, attributo_37,
          attributo_38, attributo_39, attributo_40, importo_5, importo_6
     FROM report_generico;

   COMMENT ON TABLE "VP_STM_RIEPILOGATIVO_UO"  IS '*/
comment()';
