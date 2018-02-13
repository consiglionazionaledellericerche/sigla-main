--------------------------------------------------------
--  DDL for View V_DOC_PASSIVO_OBBLIGAZIONE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_DOC_PASSIVO_OBBLIGAZIONE" ("CD_CDS", "CD_UNITA_ORGANIZZATIVA", "ESERCIZIO", "CD_TIPO_DOCUMENTO_AMM", "PG_DOCUMENTO_AMM", "CD_NUMERATORE", "PG_VER_REC", "CD_CDS_ORIGINE", "CD_UO_ORIGINE", "TI_FATTURA", "STATO_COFI", "CD_CDS_OBBLIGAZIONE", "ESERCIZIO_OBBLIGAZIONE", "ESERCIZIO_ORI_OBBLIGAZIONE", "PG_OBBLIGAZIONE", "PG_OBBLIGAZIONE_SCADENZARIO", "DT_SCADENZA", "FL_PGIRO", "CD_TIPO_DOCUMENTO_CONT", "DT_FATTURA_FORNITORE", "NR_FATTURA_FORNITORE", "CD_TERZO", "CD_TERZO_CESSIONARIO", "PG_BANCA", "CD_MODALITA_PAG", "TI_PAGAMENTO", "IM_IMPONIBILE_DOC_AMM", "IM_IVA_DOC_AMM", "IM_TOTALE_DOC_AMM", "IM_SCADENZA", "IM_ASSOCIATO_DOC_CONTABILE", "PG_LETTERA", "TI_ENTRATA_SPESA", "TI_SOSPESO_RISCONTRO", "CD_SOSPESO", "FL_SELEZIONE", "FL_FAI_REVERSALE") AS 
  SELECT
--==================================================================================================
--
-- Date: 18/07/2006
-- Version: 1.13
--
-- Estrae tutte le righe di fattura o di documenti passivi e le relative scadenze
-- di obbligazioni su cui sono state contabilizzate. Questa vista e' utilizzata dalla gestione dei mandati.
--
-- History:
--
-- Date: 05/10/2001
-- Version: 1.0
-- Creazione
--
-- Date: 12/12/2001
-- Version: 1.1
-- Aggiunto STATO_COFI
--
-- Date: 14/12/2001
-- Version: 1.2
-- Tolto STATO_COFI e aggiunto IM_ASSOCIATO_DOC_CONTABILE
--
-- Date: 17/01/2002
-- Version: 1.3
-- corretto errore nella join fra FATTURA_PASSIVA_RIGA e OBBLIGAZIONE_SCADENZARIO
--
-- Date: 18/01/2002
-- Version: 1.4
-- aggiunta colonna CD_CDS_OBBLIGAZIONE
--
-- Date: 24/01/2002
-- Version: 1.5
-- aggiunta colonna FL_PGIRO
--
-- Date: 30/01/2002
-- Version: 1.6
-- corretta clausole di join fra DOCUMENTO_GENERICO e DOCUMENTO_GENERICO_RIGA
--
-- Date: 18/02/2002
-- Version: 1.7
--
-- Revisione della vista per ottimizzazione prestazioni. Scorporata in una
-- pre view V_DOC_PASSIVO
--
-- Date: 25/03/2002
-- Version: 1.8
--
-- Introdotto codice terzo cessionario
--
-- Date: 10/09/2002
-- Version: 1.9
--
-- Controllo modello 1210 ed estrazione dei relativi campi per la gestione del controllo sospeso su
-- mandati. Aggiunta estrazione per campo CD_TIPO_DOCUMENTO_CONT
--
-- Date: 15/10/2002
-- Version: 1.10
--
-- Inserita l'estrazione del campo FL_FAI_REVERSALE per gestire i casi in cui a fronte dell'emissione
-- di un mandato deve essere generata in automatico e dall'on-line una reversale. Solo fatture
--
-- Date: 03/06/2003
-- Version: 1.11
-- Aggiunto il pg_ver_rec del doc amministrativo
--
-- Date: 27/02/2004
-- Version: 1.12
-- Eliminati i campi COGNOME, NOME, ANARAFICO: per fix su creazione dei mandati. Conseguenza dell' Err. CNR 780
--
-- Date: 18/07/2006
-- Version: 1.13
-- Gestione Impegni/Accertamenti Residui:
-- aggiornata la funzione per tener conto anche del campo Esercizio Originale Impegno/Accertamento
--
-- Body:
--
--==================================================================================================
            a.cd_cds, a.cd_unita_organizzativa, a.esercizio,
            a.cd_tipo_documento_amm, a.pg_documento_amm,
            a.cd_numeratore, a.pg_ver_rec,
            a.cd_cds_origine, a.cd_uo_origine, a.ti_fattura, a.stato_cofi,
            a.cd_cds_obbligazione, a.esercizio_obbligazione,
            a.esercizio_ori_obbligazione, a.pg_obbligazione,
            a.pg_obbligazione_scadenzario, b.dt_scadenza, c.fl_pgiro,
            c.cd_tipo_documento_cont, a.dt_fattura_fornitore,
            a.nr_fattura_fornitore, a.cd_terzo, a.cd_terzo_cessionario,
            --A.cognome,
            --A.nome,
            --A.ragione_sociale,
            a.pg_banca, a.cd_modalita_pag, d.ti_pagamento,
            SUM (a.im_imponibile_doc_amm), SUM (a.im_iva_doc_amm),
            SUM (a.im_totale_doc_amm), b.im_scadenza,
            b.im_associato_doc_contabile, a.pg_lettera, a.ti_entrata_spesa,
            a.ti_sospeso_riscontro, a.cd_sospeso, a.fl_selezione,
            a.fl_fai_reversale
       FROM v_doc_passivo a,
            obbligazione_scadenzario b,
            obbligazione c,
            rif_modalita_pagamento d
      WHERE b.cd_cds = a.cd_cds_obbligazione
        AND b.esercizio = a.esercizio_obbligazione
        AND b.esercizio_originale = a.esercizio_ori_obbligazione
        AND b.pg_obbligazione = a.pg_obbligazione
        AND b.pg_obbligazione_scadenzario = a.pg_obbligazione_scadenzario
        AND c.cd_cds = b.cd_cds
        AND c.esercizio = b.esercizio
        AND c.esercizio_originale = b.esercizio_originale
        AND c.pg_obbligazione = b.pg_obbligazione
        AND a.cd_modalita_pag = d.cd_modalita_pag (+)
   GROUP BY a.cd_cds,
            a.cd_unita_organizzativa,
            a.esercizio,
            a.cd_tipo_documento_amm,
            a.pg_documento_amm,
            a.cd_numeratore,
            a.pg_ver_rec,
            a.cd_cds_origine,
            a.cd_uo_origine,
            a.ti_fattura,
            a.stato_cofi,
            a.cd_cds_obbligazione,
            a.esercizio_obbligazione,
            a.esercizio_ori_obbligazione,
            a.pg_obbligazione,
            a.pg_obbligazione_scadenzario,
            b.dt_scadenza,
            c.fl_pgiro,
            c.cd_tipo_documento_cont,
            a.dt_fattura_fornitore,
            a.nr_fattura_fornitore,
            a.cd_terzo,
            a.cd_terzo_cessionario,
            --A.cognome,
            --A.nome,
            --A.ragione_sociale,
            a.pg_banca,
            a.cd_modalita_pag,
            d.ti_pagamento,
            b.im_scadenza,
            b.im_associato_doc_contabile,
            a.pg_lettera,
            a.ti_entrata_spesa,
            a.ti_sospeso_riscontro,
            a.cd_sospeso,
            a.fl_selezione,
            a.fl_fai_reversale;

   COMMENT ON TABLE "V_DOC_PASSIVO_OBBLIGAZIONE"  IS 'Estrae tutte le righe di fattura non ancora pagate o di documenti passivi non ancora pagati e le relative scadenze
di obbligazioni su cui sono state contabilizzate. Questa vista e'' utilizzata dalla gestione dei mandati.';
