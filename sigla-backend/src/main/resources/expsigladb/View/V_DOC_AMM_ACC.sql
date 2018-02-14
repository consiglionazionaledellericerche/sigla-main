--------------------------------------------------------
--  DDL for View V_DOC_AMM_ACC
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_DOC_AMM_ACC" ("CD_CDS", "CD_UNITA_ORGANIZZATIVA", "CD_CDS_ORIGINE", "CD_UO_ORIGINE", "ESERCIZIO", "CD_TIPO_DOCUMENTO_AMM", "TI_FATTURA", "FL_CONGELATA", "IS_SOLA_TESTATA", "PG_DOCUMENTO_AMM", "PROTOCOLLO_IVA", "TI_ASSOCIATO_MANREV", "PG_RIGA", "STATO_COFI", "STATO_COGE", "STATO_COAN", "CD_CDS_ACCERTAMENTO", "ESERCIZIO_ACCERTAMENTO", "ESERCIZIO_ORI_ACCERTAMENTO", "PG_ACCERTAMENTO", "PG_ACCERTAMENTO_SCADENZARIO") AS 
  SELECT
--==================================================================================================
--
-- Date: 19/07/2006
-- Version: 1.7
--
-- Vista per estrazione per ogni scadenza di accertamento tutti i dettagli di doc. amm. associati
--
-- History:
--
-- Date: 25/06/2003
-- Version: 1.0
-- Creazione
--
-- Date: 01/07/2003
-- Version: 1.1
-- Estratto il tipo associato man rev dalla riga del documento amministrativo con righe
--
-- Date: 10/07/2003
-- Version: 1.2
-- Filtrate fatture non associate ad accertamento
--
-- Date: 15/07/2003
-- Version: 1.3
-- Estratti stato coge e stato coan della testata del documento
--
-- Date: 28/07/2003
-- Version: 1.4
-- Aggiunto il campo ti_fattura diverso da null solo per fatture
--
-- Date: 04/08/2003
-- Version: 1.5
-- Aggiunto flag congelata per fatture
--
-- Date: 26/08/2003
-- Version: 1.6
-- Aggiunto il protocollo IVA  per le fatture
--
-- Date: 19/07/2006
-- Version: 1.7
-- Gestione Impegni/Accertamenti Residui:
-- aggiornata la funzione per tener conto anche del campo Esercizio Originale ACCERTAMENTO
--
-- Body:
--
--==================================================================================================
       A.cd_cds,
       A.cd_unita_organizzativa,
       A.cd_cds_origine,
       A.cd_uo_origine,
       A.esercizio,
       'FATTURA_A',
       A.ti_fattura,
       A.fl_congelata,
       'N',
       A.pg_fattura_attiva,
       A.PROTOCOLLO_IVA,
       B.ti_associato_manrev,
       B.progressivo_riga,
       B.stato_cofi,
	   A.STATO_COGE,
	   A.STATO_COAN,
       B.cd_cds_accertamento,
       B.esercizio_accertamento,
       B.esercizio_ori_accertamento,
       B.pg_accertamento,
       B.pg_accertamento_scadenzario
FROM   FATTURA_ATTIVA A,
       FATTURA_ATTIVA_RIGA B
WHERE  B.cd_cds_accertamento IS NOT NULL AND
       B.esercizio_accertamento IS NOT NULL AND
       B.esercizio_ori_accertamento IS NOT NULL AND
       B.pg_accertamento IS NOT NULL AND
       B.pg_accertamento_scadenzario IS NOT NULL AND
	   B.cd_cds = A.cd_cds AND
       B.cd_unita_organizzativa = A.cd_unita_organizzativa AND
       B.esercizio = A.esercizio AND
       B.pg_fattura_attiva = A.pg_fattura_attiva AND
       B.dt_cancellazione IS NULL
UNION ALL
SELECT
       A.cd_cds,
       A.cd_unita_organizzativa,
       A.cd_cds_origine,
       A.cd_uo_origine,
       A.esercizio,
       'FATTURA_P',
       A.ti_fattura,
       A.fl_congelata,
       'N',
       A.pg_fattura_passiva,
       A.PROTOCOLLO_IVA,
       B.ti_associato_manrev,
       B.progressivo_riga,
       B.stato_cofi,
	   A.STATO_COGE,
	   A.STATO_COAN,
       B.cd_cds_accertamento,
       B.esercizio_accertamento,
       B.esercizio_ori_accertamento,
       B.pg_accertamento,
       B.pg_accertamento_scadenzario
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
SELECT
       A.cd_cds,
       A.cd_unita_organizzativa,
       A.cd_cds_origine,
       A.cd_uo_origine,
       A.esercizio,
       A.cd_tipo_documento_amm,
       null,
       null,
       'N',
       A.pg_documento_generico,
       to_number(null),
       B.ti_associato_manrev,
       B.progressivo_riga,
       B.stato_cofi,
	   b.STATO_COGE,
	   A.STATO_COAN,
       B.cd_cds_accertamento,
       B.esercizio_accertamento,
       B.esercizio_ori_accertamento,
       B.pg_accertamento,
       B.pg_accertamento_scadenzario
FROM   DOCUMENTO_GENERICO_RIGA B,
       DOCUMENTO_GENERICO A
WHERE  B.dt_cancellazione IS NULL AND
       B.cd_cds_accertamento IS NOT NULL AND
       B.esercizio_accertamento IS NOT NULL AND
       B.esercizio_ori_accertamento IS NOT NULL AND
       B.pg_accertamento IS NOT NULL AND
       B.pg_accertamento_scadenzario IS NOT NULL AND
       A.cd_cds = B.cd_cds AND
       A.cd_unita_organizzativa = B.cd_unita_organizzativa AND
       A.esercizio = B.esercizio AND
       A.cd_tipo_documento_amm = B.cd_tipo_documento_amm AND
       A.pg_documento_generico = B.pg_documento_generico
UNION ALL
SELECT
       A.cd_cds,
       A.cd_unita_organizzativa,
       A.cd_cds_origine,
       A.cd_uo_origine,
       A.esercizio,
       'RIMBORSO',
       null,
       null,
       'Y',
       A.pg_rimborso,
       to_number(null),
       A.ti_associato_manrev,
       0,
       A.stato_cofi,
	   A.STATO_COGE,
	   A.STATO_COAN,
       A.cd_cds_accertamento,
       A.esercizio_accertamento,
       A.esercizio_ori_accertamento,
       A.pg_accertamento,
       A.pg_accertamento_scadenzario
FROM   RIMBORSO A
WHERE  A.dt_cancellazione IS NULL AND
       A.cd_cds_accertamento IS NOT NULL AND
       A.esercizio_accertamento IS NOT NULL AND
       A.esercizio_ori_accertamento IS NOT NULL AND
       A.pg_accertamento IS NOT NULL AND
       A.pg_accertamento_scadenzario IS NOT NULL
;

   COMMENT ON TABLE "V_DOC_AMM_ACC"  IS 'Vista per estrazione per ogni scadenza di accertamento tutti i dettagli di doc. amm. associati';
