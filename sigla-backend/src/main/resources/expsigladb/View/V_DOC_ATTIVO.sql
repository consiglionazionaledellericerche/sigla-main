--------------------------------------------------------
--  DDL for View V_DOC_ATTIVO
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_DOC_ATTIVO" ("CD_CDS", "CD_UNITA_ORGANIZZATIVA", "ESERCIZIO", "CD_TIPO_DOCUMENTO_AMM", "PG_DOCUMENTO_AMM", "DT_EMISSIONE", "PG_VER_REC", "CD_CDS_ORIGINE", "CD_UO_ORIGINE", "TI_FATTURA", "STATO_COFI", "CD_CDS_ACCERTAMENTO", "ESERCIZIO_ACCERTAMENTO", "ESERCIZIO_ORI_ACCERTAMENTO", "PG_ACCERTAMENTO", "PG_ACCERTAMENTO_SCADENZARIO", "CD_TERZO", "CD_TERZO_UO_CDS", "COGNOME", "NOME", "RAGIONE_SOCIALE", "PG_BANCA", "CD_MODALITA_PAG", "IM_IMPONIBILE_DOC_AMM", "IM_IVA_DOC_AMM", "IM_TOTALE_DOC_AMM", "FL_SELEZIONE") AS 
  SELECT
--==================================================================================================
--
-- Date: 19/07/2006
-- Version: 1.9
--
-- Pre view di estrazione delle righe di fatture attive, note di credito passive su accertamenti e
-- documenti generici attivi utilizzabili nella costruzione di una reversale.
-- I record con FL_SELEZIONE = 'Y' sono quelli, se non pagati, che possono essere
-- oggetto di associazione ad una nuova reversale
--
-- History:
--
-- Date: 18/02/2002
-- Version: 1.0
--
-- Creazione vista
--
-- Date: 14/03/2002
-- Version: 1.1
--
-- Modificate colonne PG_BANCA con PG_BANC_UO_CDS e CD_MODALITA_PAG con CD_MODALITA_PAG_UO_CDS su
-- FATTURA_ATTIVA
--
-- Date: 21/03/2002
-- Version: 1.2
--
-- Corretta estrazione; era ridondato cd_terzo invece di cd_terzo_uo_cds nella estrazione per nota
-- di credito passiva
-- Inserito filtro per eliminare le righe documento cancellate
--
-- Date: 25/03/2002
-- Version: 1.3
--
-- Corretta estrazione; nella selezione dei documenti generici non era presente nelle condizioni
-- di where l'attibuto CD_TIPO_DOCUMENTO_AMM ed il filto sui record di dettaglio che puntano
-- ad accertamenti.
--
-- Date: 27/06/2002
-- Version: 1.4
--
-- Introdotta la previsione del tipo documento RIMBORSO
--
-- Date: 30/07/2002
-- Version: 1.5
--
-- Introdotto il recupero dei campi di nome, cognome e ragione sociale del rimborso
--
-- Date: 19/02/2003
-- Version: 1.6
--
-- Estrazione dello stato cofi dei documenti prendendolo dalle righe
--
-- Date: 03/06/2003
-- Version: 1.7
-- Aggiunto il pg_ver_rec del documento amministrativo
--
-- Date: 06/08/2003
-- Version: 1.8
-- Modificata la gestione del flag fl_selezione, che ora tiene conto anche del fl_congelata per le atture Attive e per
-- le Fatture Passive tipo 'C', (nota di credito).
--
-- Date: 19/07/2006
-- Version: 1.9
-- Gestione Impegni/Accertamenti Residui:
-- gestito il nuovo campo ESERCIZIO_ORIGINALE
--
-- Body:
--
--==================================================================================================
       A.cd_cds,
       A.cd_unita_organizzativa,
       A.esercizio,
       'FATTURA_A',
       A.pg_fattura_attiva,
       A.dt_emissione,
       A.pg_ver_rec,
       A.cd_cds_origine,
       A.cd_uo_origine,
       A.ti_fattura,
       B.stato_cofi,
       B.cd_cds_accertamento,
       B.esercizio_accertamento,
       B.esercizio_ori_accertamento,
       B.pg_accertamento,
       B.pg_accertamento_scadenzario,
       A.cd_terzo,
       A.cd_terzo_uo_cds,
       A.cognome,
       A.nome,
       A.ragione_sociale,
       A.pg_banca_uo_cds,
       A.cd_modalita_pag_uo_cds,
       DECODE(A.ti_fattura, 'C', (B.im_imponibile * -1), B.im_imponibile),
       decode(FL_LIQUIDAZIONE_DIFFERITA,'Y',0, DECODE(A.ti_fattura, 'C', (B.im_iva * -1), B.im_iva)) iva,
       DECODE(A.ti_fattura, 'C', decode(FL_LIQUIDAZIONE_DIFFERITA,'Y',B.im_imponibile*-1,((B.im_imponibile + B.im_iva) * -1)),(decode(FL_LIQUIDAZIONE_DIFFERITA,'Y',B.im_imponibile,B.im_imponibile + B.im_iva))),
       DECODE(A.ti_fattura, 'F', (DECODE(A.FL_CONGELATA, 'Y', 'N', 'Y')), 'N')
FROM   FATTURA_ATTIVA A,
       FATTURA_ATTIVA_RIGA B,
       CONFIGURAZIONE_CNR C
WHERE  B.cd_cds = A.cd_cds AND
       B.cd_unita_organizzativa = A.cd_unita_organizzativa AND
       B.esercizio = A.esercizio AND
       B.pg_fattura_attiva = A.pg_fattura_attiva AND
       B.dt_cancellazione IS NULL
       and
       C.ESERCIZIO = 0 AND
       c.cd_chiave_primaria = 'SPLIT_PAYMENT'
       AND c.cd_chiave_secondaria = 'ATTIVA'
       AND c.cd_unita_funzionale = '*'
       AND nvl(a.dt_emissione,a.dt_registrazione) >=   NVL (c.dt01, nvl(a.dt_emissione,a.dt_registrazione))
union all
select
  A.cd_cds,
       A.cd_unita_organizzativa,
       A.esercizio,
       'FATTURA_A',
       A.pg_fattura_attiva,
       A.dt_emissione,
       A.pg_ver_rec,
       A.cd_cds_origine,
       A.cd_uo_origine,
       A.ti_fattura,
       B.stato_cofi,
       B.cd_cds_accertamento,
       B.esercizio_accertamento,
       B.esercizio_ori_accertamento,
       B.pg_accertamento,
       B.pg_accertamento_scadenzario,
       A.cd_terzo,
       A.cd_terzo_uo_cds,
       A.cognome,
       A.nome,
       A.ragione_sociale,
       A.pg_banca_uo_cds,
       A.cd_modalita_pag_uo_cds,
       DECODE(A.ti_fattura, 'C', (B.im_imponibile * -1), B.im_imponibile),
       DECODE(A.ti_fattura, 'C', (B.im_iva * -1), B.im_iva) iva,
       DECODE(A.ti_fattura, 'C', ((B.im_imponibile + B.im_iva) * -1),(B.im_imponibile + B.im_iva)),
       DECODE(A.ti_fattura, 'F', (DECODE(A.FL_CONGELATA, 'Y', 'N', 'Y')), 'N')
FROM   FATTURA_ATTIVA A,
       FATTURA_ATTIVA_RIGA B,
       CONFIGURAZIONE_CNR C
WHERE  B.cd_cds = A.cd_cds AND
       B.cd_unita_organizzativa = A.cd_unita_organizzativa AND
       B.esercizio = A.esercizio AND
       B.pg_fattura_attiva = A.pg_fattura_attiva AND
       B.dt_cancellazione IS NULL and
       C.ESERCIZIO = 0 AND
       c.cd_chiave_primaria = 'SPLIT_PAYMENT'
       AND c.cd_chiave_secondaria = 'ATTIVA'
       AND c.cd_unita_funzionale = '*'
       AND nvl(a.dt_emissione,a.dt_registrazione) <   NVL (c.dt01, nvl(a.dt_emissione,a.dt_registrazione))
UNION ALL
SELECT A.cd_cds,
       A.cd_unita_organizzativa,
       A.esercizio,
       'FATTURA_P',
       A.pg_fattura_passiva,
       to_date(null),
       A.pg_ver_rec,
       A.cd_cds_origine,
       A.cd_uo_origine,
       A.ti_fattura,
       B.stato_cofi,
       B.cd_cds_accertamento,
       B.esercizio_accertamento,
       B.esercizio_ori_accertamento,
       B.pg_accertamento,
       B.pg_accertamento_scadenzario,
       A.cd_terzo,
       A.cd_terzo_uo_cds,
       A.cognome,
       A.nome,
       A.ragione_sociale,
       A.pg_banca_uo_cds,
       A.cd_modalita_pag_uo_cds,
       B.im_imponibile,
       B.im_iva,
       decode(a.fl_split_payment,'Y',B.im_imponibile,(B.im_imponibile + B.im_iva)),
       DECODE(A.FL_CONGELATA, 'Y', 'N', 'Y') --'Y'
FROM   FATTURA_PASSIVA A,
       FATTURA_PASSIVA_RIGA B
WHERE  A.ti_fattura = 'C' AND
       B.cd_cds = A.cd_cds AND
       B.cd_unita_organizzativa = A.cd_unita_organizzativa AND
       B.esercizio = A.esercizio AND
       B.pg_fattura_passiva = A.pg_fattura_passiva AND
       B.cd_cds_accertamento IS NOT NULL AND
       B.esercizio_accertamento IS NOT NULL AND
       B.esercizio_ori_accertamento IS NOT NULL AND
       B.pg_accertamento IS NOT NULL AND
       B.pg_accertamento_scadenzario IS NOT NULL AND
       B.dt_cancellazione IS NULL
UNION ALL
SELECT A.cd_cds,
       A.cd_unita_organizzativa,
       A.esercizio,
       A.cd_tipo_documento_amm,
       A.pg_documento_generico,
       to_date(null),
       B.pg_ver_rec,
       B.cd_cds_origine,
       B.cd_uo_origine,
       NULL,
       A.stato_cofi,
       A.cd_cds_accertamento,
       A.esercizio_accertamento,
       A.esercizio_ori_accertamento,
       A.pg_accertamento,
       A.pg_accertamento_scadenzario,
       A.cd_terzo,
       A.cd_terzo_uo_cds,
       A.cognome,
       A.nome,
       A.ragione_sociale,
       A.pg_banca_uo_cds,
       A.cd_modalita_pag_uo_cds,
       0,
       0,
       A.im_riga,
       'Y'
FROM   DOCUMENTO_GENERICO_RIGA A,
       DOCUMENTO_GENERICO B
WHERE  A.dt_cancellazione IS NULL AND
       A.cd_cds_accertamento IS NOT NULL AND
       A.esercizio_accertamento IS NOT NULL AND
       A.esercizio_ori_accertamento IS NOT NULL AND
       A.pg_accertamento IS NOT NULL AND
       A.pg_accertamento_scadenzario IS NOT NULL AND
       B.cd_cds = A.cd_cds AND
       B.cd_unita_organizzativa = A.cd_unita_organizzativa AND
       B.esercizio = A.esercizio AND
       B.cd_tipo_documento_amm = A.cd_tipo_documento_amm AND
       B.pg_documento_generico = A.pg_documento_generico
UNION ALL
SELECT A.cd_cds,
       A.cd_unita_organizzativa,
       A.esercizio,
       'RIMBORSO',
       A.pg_rimborso,
       to_date(null),
       A.pg_ver_rec,
       A.cd_cds_origine,
       A.cd_uo_origine,
       NULL,
       A.stato_cofi,
       A.cd_cds_accertamento,
       A.esercizio_accertamento,
       A.esercizio_ori_accertamento,
       A.pg_accertamento,
       A.pg_accertamento_scadenzario,
       A.cd_terzo,
       A.cd_terzo_uo_cds,
       A.cognome,
       A.nome,
       A.ragione_sociale,
       A.pg_banca_uo_cds,
       A.cd_modalita_pag_uo_cds,
       0,
       0,
       A.im_rimborso,
       'Y'
FROM   RIMBORSO A
WHERE  A.dt_cancellazione IS NULL AND
       A.cd_cds_accertamento IS NOT NULL AND
       A.esercizio_accertamento IS NOT NULL AND
       A.esercizio_ori_accertamento IS NOT NULL AND
       A.pg_accertamento IS NOT NULL AND
       A.pg_accertamento_scadenzario IS NOT NULL;

   COMMENT ON TABLE "V_DOC_ATTIVO"  IS 'Pre view di estrazione delle righe di fatture passive, attive e documenti passivi necessari
alla selezione nella costruzione di un mandato. La vista è stata scorporata da
V_DOC_PASSIVO_OBBLIGAZIONE.
I record con FL_SELEZIONE = ''Y'' sono quelli, se non pagati, che possono essere oggetto di
associazione ad un nuovo mandato';
