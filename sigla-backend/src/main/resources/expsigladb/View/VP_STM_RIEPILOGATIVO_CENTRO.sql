--------------------------------------------------------
--  DDL for View VP_STM_RIEPILOGATIVO_CENTRO
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VP_STM_RIEPILOGATIVO_CENTRO" ("ID_REPORT", "GRUPPO", "TIPOLOGIA_RIGA", "SEQUENZA", "SOTTOGRUPPO", "CD_CDS", "CD_UO", "DS_UO", "CD_SEZIONALE", "STATO_REGISTRO", "CD_GRUPPO_IVA", "CD_IVA", "DESCRIZIONE_RIGA", "IMPONIBILE", "IVA", "IVA_INDETRAIBILE", "IVA_ESIGIBILE", "TOTALE", "PROGRESSIVO_UO", "ESERCIZIO_RIF", "RAGIONE_SOCIALE_ENTE", "PARTITA_IVA_ENTE", "PROVVISORIO_DEFINITIVO", "ACQUISTI_VENDITE", "DESCRIZIONE_GRUPPO", "IMPONIBILE_SPLIT_PAYMENT", "IVA_SPLIT_PAYMENT") AS 
  SELECT
-- =================================================================================================
--
-- Date: 24/02/2003
-- Version: 1.1
--
-- Vista di stampa riepilogativo per CENTRO
--
-- History:
--
-- Date: 10/02/2003
-- Version: 1.0
--
-- Creazione vista
--
-- Date: 24/02/2003
-- Version: 1.1
--
-- Richiesta CINECA n. 486. Modifica layout della stampa del riepilogativo CENTRO. Inserito il recupero
-- dell'informazione della ragione sociale e partita IVA dell'ente da esporre in intestazione per tutti
-- i report IVA
--
-- Body:
--
-- =================================================================================================
          ID, chiave, tipo, sequenza, descrizione, attributo_1, attributo_2,
          attributo_3, attributo_4, attributo_5, attributo_6, attributo_7,
          attributo_8, importo_1, importo_2, importo_3, importo_4, importo_5,
          attributo_9, importo_20, attributo_36, attributo_37, attributo_38,
          attributo_39, attributo_40, importo_6, importo_7
     FROM report_generico;

   COMMENT ON TABLE "VP_STM_RIEPILOGATIVO_CENTRO"  IS '*/
comment()';
