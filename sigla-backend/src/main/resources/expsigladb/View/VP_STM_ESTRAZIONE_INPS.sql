--------------------------------------------------------
--  DDL for View VP_STM_ESTRAZIONE_INPS
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VP_STM_ESTRAZIONE_INPS" ("ID_REPORT", "GRUPPO", "TIPOLOGIA_RIGA", "SEQUENZA", "TITOLO", "ESERCIZIO", "CD_UO_DESCRIZIONE", "CD_ANAG", "COGNOME_NOME", "COMUNE_DATA_NASCITA", "CODICE_FISCALE", "INDIRIZZO", "CAP_COMUNE_RESIDENZA", "ATTIVITA_INPS", "ALTRA_FORMA_ASS_INPS", "CD_CDS_COMPENSO", "CD_UO_COMPENSO", "ESERCIZIO_COMPENSO", "PG_COMPENSO", "V_MESE_PAGAMENTO", "V_DATA_VERSAMENTO", "V_NUMERO_VERSAMENTI", "V_IMPORTO_VERSATO", "V_IMPORTO_DOVUTO", "V_IMPORTO_SALDO", "D_MESE_COMPENSO", "D_IMPONIBILE_PREVIDENZIALE", "D_TIPO_CORI_INPS", "D_FL_SENZA_CALCOLI", "D_IMPONIBILE_ALIQUOTA", "D_ALIQUOTA", "D_CONTRIBUTO_DOVUTO", "D_DT_ATTIVITA_DA", "D_DT_ATTIVITA_A", "D_DT_VERSAMENTO") AS 
  SELECT
-- =================================================================================================
--
-- Date: 12/03/2004
-- Version: 1.0
--
-- Vista di stampa estrazione INPS. Legenda
-- GRUPPO Dominio A = Versamenti B = Dettaglio compensi
-- TIPOLOGIA_RIGA Dominio N = normale T = Totale (grassetto più riga prima e dopo)
--
-- History:
--
-- Date: 12/03/2004
-- Version: 1.0
--
-- Creazione vista
--
-- Body:
--
-- =================================================================================================
       id,
       chiave,
       tipo,
       sequenza,
       descrizione,
       importo_1,
       attributo_1,
       importo_2,
       attributo_2,
       attributo_3,
       attributo_4,
       attributo_5,
       attributo_6,
       attributo_7,
       attributo_8,
       attributo_9,
       attributo_10,
       importo_3,
       importo_4,
       attributo_11,
       data_1,
       importo_5,
       importo_6,
       importo_7,
       importo_8,
       attributo_12,
       importo_9,
       attributo_13,
       attributo_14,
       importo_10,
       importo_11,
       importo_12,
       data_2,
       data_3,
       data_4
FROM   REPORT_GENERICO
;

   COMMENT ON TABLE "VP_STM_ESTRAZIONE_INPS"  IS 'Vista per la stampa estrazione INPS';
