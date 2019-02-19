--------------------------------------------------------
--  DDL for View V_DOC_ATTIVO_ACCERTAMENTO
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_DOC_ATTIVO_ACCERTAMENTO" ("CD_CDS", "CD_UNITA_ORGANIZZATIVA", "ESERCIZIO", "CD_TIPO_DOCUMENTO_AMM", "PG_DOCUMENTO_AMM", "PG_VER_REC", "CD_CDS_ORIGINE", "CD_UO_ORIGINE", "TI_FATTURA", "STATO_COFI", "CD_CDS_ACCERTAMENTO", "ESERCIZIO_ACCERTAMENTO", "ESERCIZIO_ORI_ACCERTAMENTO", "PG_ACCERTAMENTO", "PG_ACCERTAMENTO_SCADENZARIO", "CD_TIPO_DOCUMENTO_CONT", "DT_SCADENZA", "FL_PGIRO", "CD_TERZO", "CD_TERZO_UO_CDS", "PG_BANCA", "CD_MODALITA_PAG", "TI_PAGAMENTO", "IM_IMPONIBILE_DOC_AMM", "IM_IVA_DOC_AMM", "IM_TOTALE_DOC_AMM", "IM_SCADENZA", "IM_ASSOCIATO_DOC_CONTABILE", "FL_SELEZIONE", "FL_FATTURA_ELETTRONICA", "STATO_INVIO_SDI", "CODICE_UNIVOCO_UFFICIO_IPA") AS 
  SELECT
--
-- Date: 19/07/2006
-- Version: 1.11
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
-- Revisione della vista per ottimizzazione prestazioni. Scorporata in una
-- pre view V_DOC_PASSIVO
--
-- Date: 12/07/2002
-- Version: 1.8
-- Aggiunta outer join sulle modalità di pagamento in modo da includere anche i doc. amm che non hanno mod. pagamento( REGOLA_E)
--
-- Date: 03/06/2003
-- Version: 1.9
-- Aggiunto il pg_ver_rec del documento amministrativo
--
-- Date: 26/02/2004
-- Version: 1.10
-- Err. CNR 780 - Borriello: eliminati RAGIONE_SOCIALE, COGNOME, NOME.
--
-- Date: 19/07/2006
-- Version: 1.11
-- Gestione Impegni/Accertamenti Residui:
-- gestito il nuovo campo ESERCIZIO_ORIGINALE
--
-- Body:
--
A.cd_cds,
A.cd_unita_organizzativa,
A.esercizio,
A.cd_tipo_documento_amm,
A.pg_documento_amm,
A.pg_ver_rec,
A.cd_cds_origine,
A.cd_uo_origine,
A.ti_fattura,
A.stato_cofi,
A.cd_cds_accertamento,
A.esercizio_accertamento,
A.esercizio_ori_accertamento,
A.pg_accertamento,
A.pg_accertamento_scadenzario,
C.cd_tipo_documento_cont,
B.dt_scadenza_incasso,
C.fl_pgiro,
A.cd_terzo,
A.cd_terzo_uo_cds,
--A.cognome,
--A.nome,
--A.ragione_sociale,
A.pg_banca,
A.cd_modalita_pag,
D.ti_pagamento,
SUM(A.im_imponibile_doc_amm),
SUM(A.im_iva_doc_amm),
SUM(A.im_totale_doc_amm),
B.im_scadenza,
B.im_associato_doc_contabile,
A.fl_selezione,
A.FL_FATTURA_ELETTRONICA, A.STATO_INVIO_SDI, A.CODICE_UNIVOCO_UFFICIO_IPA
FROM   V_DOC_ATTIVO A,
ACCERTAMENTO_SCADENZARIO B,
ACCERTAMENTO C,
RIF_MODALITA_PAGAMENTO D
WHERE  B.cd_cds = A.cd_cds_accertamento AND
B.esercizio = A.esercizio_accertamento AND
B.esercizio_originale = A.esercizio_ori_accertamento AND
B.pg_accertamento = A.pg_accertamento AND
B.pg_accertamento_scadenzario = A.pg_accertamento_scadenzario AND
C.cd_cds = B.cd_cds AND
C.esercizio = B.esercizio AND
C.esercizio_originale = B.esercizio_originale AND
C.pg_accertamento = B.pg_accertamento AND
D.cd_modalita_pag (+) = A.cd_modalita_pag
GROUP BY A.cd_cds,
A.cd_unita_organizzativa,
A.esercizio,
A.cd_tipo_documento_amm,
A.pg_documento_amm,
A.pg_ver_rec,
A.cd_cds_origine,
A.cd_uo_origine,
A.ti_fattura,
A.stato_cofi,
A.cd_cds_accertamento,
A.esercizio_accertamento,
A.esercizio_ori_accertamento,
A.pg_accertamento,
A.pg_accertamento_scadenzario,
B.dt_scadenza_incasso,
C.fl_pgiro,
C.cd_tipo_documento_cont,
A.cd_terzo,
A.cd_terzo_uo_cds,
--A.cognome,
--A.nome,
--A.ragione_sociale,
A.pg_banca,
A.cd_modalita_pag,
D.ti_pagamento,
B.im_scadenza,
B.im_associato_doc_contabile,
A.fl_selezione,
A.FL_FATTURA_ELETTRONICA, A.STATO_INVIO_SDI, A.CODICE_UNIVOCO_UFFICIO_IPA;

   COMMENT ON TABLE "V_DOC_ATTIVO_ACCERTAMENTO"  IS 'Estrae tutte le righe di fattura o di documenti attivi e le relative scadenze
di accertamenti su cui sono state contabilizzate.';
