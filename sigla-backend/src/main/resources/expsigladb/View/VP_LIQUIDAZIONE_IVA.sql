--------------------------------------------------------
--  DDL for View VP_LIQUIDAZIONE_IVA
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VP_LIQUIDAZIONE_IVA" ("ID_REPORT", "CHIAVE", "TIPO", "SEQUENZA", "DESCRIZIONE", "RP_IVA_VEN", "RP_IVA_VEN_DIFF", "RP_IVA_VEN_DIFF_ESIG", "RP_IVA_VEN_DIFF_ES_PREC_ESIG", "RP_IVA_VEN_AUTOFATT", "RP_IVA_VEN_INTRAUE", "RP_IVA_ACQ", "RP_IVA_ACQ_NON_DETR", "RP_IVA_ACQ_DIFF", "RP_IVA_ACQ_DIFF_ESIG", "RP_IVA_ACQ_DIFF_ES_PREC_ESIG", "RP_IVA_NON_VERS_PER_PREC", "RP_IVA_DEB_CRED_PER_PREC", "RP_IVA_CRED_NO_PRORATA", "RP_PERC_PRORATA_DETRAIBILE", "RP_GESTIONE_PRORATA", "RP_ESERCIZIO_EURO", "RP_DS_GRUPPO_STAMPA", "RP_IVA_LIQ_ESTERNA", "RP_IVA_VEN_SPLIT_PAYMENT", "RP_IVA_ACQ_SPLIT_PAYMENT") AS 
  SELECT
-- =================================================================================================
--
-- Date: 03/02/2003
-- Version: 4.0
--
-- Vista di mappatura della tabella REPORT_GENERICO propedeutica al calcolo della liquidazione IVA
--
-- History:
--
-- Date: 03/02/2003
-- Version: 4.0
--
-- Creazione Vista
--
-- Date: 23/08/2005
-- Version: 4.1
-- Aggiunti i due campi RP_IVA_VEN_DIFF_ES_PREC_ESIG e RP_IVA_ACQ_DIFF_ES_PREC_ESIG per correzione
-- anomalia: veniva sommata nello stesso campo l'IVA sulle fatture dell'esercizio in corso divenute
-- esigibili nel periodo da quelle di esercizi precedenti divenute esigibili nel periodo
--
-- Body:
--
-- =================================================================================================
          ID, chiave, tipo, sequenza, descrizione, importo_1, importo_2,
          importo_3, importo_4, importo_5, importo_6, importo_7, importo_8,
          importo_9, importo_10, importo_11, importo_12, importo_13,
          importo_14, importo_15, attributo_1, attributo_2, attributo_40,
          importo_16, importo_17, importo_18
     FROM report_generico;

   COMMENT ON TABLE "VP_LIQUIDAZIONE_IVA"  IS '*/
comment()';
