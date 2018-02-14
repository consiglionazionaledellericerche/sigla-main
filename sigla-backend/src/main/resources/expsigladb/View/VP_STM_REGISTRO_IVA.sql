--------------------------------------------------------
--  DDL for View VP_STM_REGISTRO_IVA
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VP_STM_REGISTRO_IVA" ("ID_REPORT", "GRUPPO", "TIPOLOGIA_RIGA", "SEQUENZA", "SOTTOGRUPPO", "CD_CDS", "CD_UO", "ESERCIZIO", "CD_CDS_ORIGINE", "CD_UO_ORIGINE", "CD_TIPO_SEZIONALE", "TI_FATTURA", "DATA_REGISTRAZIONE", "NUMERO_PROGRESSIVO", "DATA_EMISSIONE", "NUMERO_FATTURA", "PROTOCOLLO_IVA", "PROTOCOLLO_IVA_GENERALE", "COMM_IST_TESTATA", "CODICE_ANAGRAFICO", "RAGIONE_SOCIALE", "IMPONIBILE", "IVA", "IVA_INDETRAIBILE", "TOTALE", "COMM_IST_DETTAGLIO", "CODICE_IVA", "PERCENTUALE_IVA", "DESCRIZIONE_IVA", "FL_IVA_DETRAIBILE", "PERCENTUALE_IVA_DETRAIBILE", "GRUPPO_IVA", "DESCRIZIONE_GRUPPO_IVA", "INTRA_UE", "CODICE_VALUTA", "IMPORTO_VALUTA", "ESIGIBILITA_DIFF", "DATA_ESIGIBILITA_DIFF", "ESERCIZIO_RIF", "RAGIONE_SOCIALE_ENTE", "PARTITA_IVA_ENTE", "PROVVISORIO_DEFINITIVO", "ACQUISTO_VENDITA", "DESCRIZIONE_GRUPPO", "IVA_ESIGIBILE", "FL_SPLIT_PAYMENT") AS 
  SELECT
-- =================================================================================================
--
-- Date: 24/02/2003
-- Version: 3.2
--
-- Vista di stampa registri IVA.
-- Informazioni poste su attributi il cui nome non corrisponde al contenuto:
-- 1) TITOLO REPORT
--    - CD_CDS       Codice e descrizione dell'unit�rganizzativa
--    - CD_UO        Titolo del registro (REGISTRI IVA ...... DAL .... AL ....)
--    - CD_CDS_ORIGINE     Codice e descrizione del sezionale in stampa
-- 2) TITOLO SEZIONE A - copertina del riepilogo dei sezionali in stampa
--    - CD_CDS       prima parte della descrizione (FRONTESPIZIO DEL REGISTRO ..... DAL .... AL ....)
--    - CD_UO        seconda parte della descrizione (RIASSUNTIVO DELLE FATTURE ...... )
-- 3) DETTAGLIO SEZIONE A - copertina del riepilogo dei sezionali in stampa
--    - CD_CDS       minimo numero di fattura
--    - CD_UO        massimo numero di fattura
--    - CD_CDS_ORIGINE     descrizione cablata (REGISTRATE DALL'UNITA' ......)
--    - CD_TIPO_SEZIONALE  codice del sezionale in stampa
--    - TI_FATTURA      descrizione del sezionale
-- 4) DETTAGLIO RIEPILOGO IVA PER CODICI E GRUPPO
--    - CD_CDS_ORIGINE     codice IVA
--    - CD_UO_ORIGINE      descrizione IVA o totale
--
-- History:
--
-- Date: 10/06/2002
-- Version: 1.0
--
-- Creazione
--
-- Date: 09/12/2002
-- Version: 1.1
--
-- Revisione vista per stampa registri IVA
--
-- Date: 10/12/2002
-- Version: 1.2
--
-- Rinominata la vista per aderire alla naming definita V_STM_REGISTRO_IVA --> VP_STM_REGISTRO_IVA
--
-- Date: 16/12/2002
-- Version: 3.0
--
-- Revisone complessiva della procedura
-- In tutte le stampe esposto, in report generico, come ultimi 3 campi le seguenti informazioni:
-- - tipoReport   -> P = Provvisorio D = Definitivo
-- - tipoRegistro    -> A = Acquisti V = Vendite
-- - descrizione gruppo di stampa
--
-- Date: 17/12/2002
-- Version: 3.1
--
-- Allineamento vista alla nuova struttura della tabella REPORT_GENERICO
--
-- Date: 24/02/2003
-- Version: 3.2
--
-- Inserimento nuovi attributi per memorizzazione della ragione sociale e partita iva ente + esercizio
-- di riferimento
--
-- Body:
--
-- =================================================================================================
          ID, chiave, tipo, sequenza, descrizione, attributo_1, attributo_2,
          importo_1, attributo_3, attributo_4, attributo_5, attributo_6,
          data_1, importo_2, data_2, attributo_7, importo_3, importo_4,
          attributo_8, importo_5, attributo_9, importo_6, importo_7,
          importo_8, importo_9, attributo_10, attributo_11, importo_10,
          attributo_12, attributo_13, importo_11, attributo_14, attributo_15,
          attributo_16, attributo_17, importo_12, attributo_18, data_3,
          importo_20, attributo_36, attributo_37, attributo_38, attributo_39,
          attributo_40, importo_17, attributo_19
     FROM report_generico;

   COMMENT ON TABLE "VP_STM_REGISTRO_IVA"  IS '*/
comment()';
