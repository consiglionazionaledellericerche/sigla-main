--------------------------------------------------------
--  DDL for View PRT_STAMPA_OBBL_LINEE_ATTIVITA
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "PRT_STAMPA_OBBL_LINEE_ATTIVITA" ("CDS", "UO", "CD_CENTRO_RESPONSABILITA", "CD_LINEA_ATTIVITA", "DS_LINEA_ATTIVITA", "CD_ELEMENTO_VOCE", "DT_REGISTRAZIONE", "ESERCIZIO_ORIGINALE", "PG_OBBLIGAZIONE", "ESERCIZIO_ORI_ORI_RIPORTO", "PG_OBBLIGAZIONE_ORI_RIPORTO", "ESERCIZIO", "ESERCIZIO_ORI_RIPORTO", "PG_OBBLIGAZIONE_SCADENZARIO", "DS_SCADENZA", "IM_SCADENZA", "IM_VOCE", "PG_DOC_AMM", "PG_MANDATO", "CD_TERZO", "DENOMINAZIONE_SEDE", "TI_FATTURA") AS 
  select
--
-- Date: 18/01/2007
-- Version: 1.6
--
-- Stampa obbligazioni distinte per linee di attività
--
--
-- History:
--
-- Date: 20/04/2004
-- Version: 1.0
-- Creazione
--
-- Date: 21/06/2004
-- Version: 1.1
-- aggiunto cd_unita_organizzativa
--
-- Date: 15/07/2004
-- Version: 1.2
-- Aggiunti campi LA e CDR sul file rpt
--
-- Date: 30/09/2004
-- Version: 1.3
-- Eliminata join con insieme_la ed aggiunta colonna ti_fattura
-- (errore visualizzazione insiemi non gestiti e visualizzazione nota credito)
--
-- Date: 18/07/2006
-- Version: 1.4
-- Gestione Impegni/Accertamenti Residui:
-- gestito il nuovo campo ESERCIZIO_ORIGINALE
--
-- Date: 12/12/2006
-- Version: 1.5
-- Anomalia: corrette le JOIN tra Mandato e Documenti Contabile che creavano doppioni di dati
--           specialmente in presenza di NC
--
-- Date: 18/01/2007
-- Version: 1.6
-- Anomalia: allineate le view V_CONS_OBBL_LINEE_ATTIVITA e PRT_STAMPA_OBBL_LINEE_ATTIVITA che devono riportare gli
--           stessi valori
--           la V_CONS_OBBL_LINEE_ATTIVITA contiene la select di ricerca mentre la PRT_STAMPA_OBBL_LINEE_ATTIVITA
--           viene ottenuta come select della V_CONS_OBBL_LINEE_ATTIVITA
--
-- Body:
--
-- Prima Union Sono le Obbligazioni Collegate a Documenti Amministrativi e non, non pagati
CDS, UO, CD_CENTRO_RESPONSABILITA, CD_LINEA_ATTIVITA, DS_LINEA_ATTIVITA,
CD_ELEMENTO_VOCE, DT_REGISTRAZIONE, ESERCIZIO_ORIGINALE, PG_OBBLIGAZIONE, ESERCIZIO_ORI_ORI_RIPORTO,
PG_OBBLIGAZIONE_ORI_RIPORTO, ESERCIZIO, ESERCIZIO_ORI_RIPORTO, PG_OBBLIGAZIONE_SCADENZARIO, DS_SCADENZA,
IM_SCADENZA, IM_VOCE, PG_DOC_AMM, PG_MANDATO, CD_TERZO,
DENOMINAZIONE_SEDE, TI_FATTURA
From V_CONS_OBBL_LINEE_ATTIVITA
;
