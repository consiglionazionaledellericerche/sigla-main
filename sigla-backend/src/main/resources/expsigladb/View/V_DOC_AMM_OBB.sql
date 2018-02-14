--------------------------------------------------------
--  DDL for View V_DOC_AMM_OBB
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_DOC_AMM_OBB" ("CD_CDS", "CD_UNITA_ORGANIZZATIVA", "CD_CDS_ORIGINE", "CD_UO_ORIGINE", "ESERCIZIO", "CD_TIPO_DOCUMENTO_AMM", "TI_FATTURA", "FL_CONGELATA", "IS_SOLA_TESTATA", "PG_DOCUMENTO_AMM", "PROTOCOLLO_IVA", "TI_ASSOCIATO_MANREV", "STATO_PAGAMENTO_FONDO_ECO", "PG_RIGA", "STATO_COFI", "STATO_COGE", "STATO_COAN", "CD_CDS_OBBLIGAZIONE", "ESERCIZIO_OBBLIGAZIONE", "ESERCIZIO_ORI_OBBLIGAZIONE", "PG_OBBLIGAZIONE", "PG_OBBLIGAZIONE_SCADENZARIO") AS 
  SELECT
--==================================================================================================
--
-- Date: 18/07/2006
-- Version: 1.7
--
-- Vista per estrazione per ogni scadenza di obbligazione tutti i dettagli di doc. amm. associati
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
-- Filtro su fatture non associate ad obbligazioni
--
-- Date: 15/07/2003
-- Version: 1.3
-- Estratti stato coge e stato coan della testata del documento
--
-- Date: 28/07/2003
-- Version: 1.4
-- Estratto il campo tipo fattura diverso da null solo per fatture attive e passive
--
-- Date: 04/08/2003
-- Version: 1.5
-- Aggiunto flag congelata per fatture
--
-- Date: 26/08/2003
-- Version: 1.6
-- Aggiunto protocollo IVA per fatture
--
-- Date: 18/07/2006
-- Version: 1.7
-- Gestione Impegni/Accertamenti Residui:
-- aggiornata la funzione per tener conto anche del campo Esercizio Originale OBBLIGAZIONE
--
-- Body:
--
--==================================================================================================
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
	   A.protocollo_iva,
       B.ti_associato_manrev,
       A.stato_pagamento_fondo_eco,
       B.progressivo_riga,
       B.stato_cofi,
	   A.STATO_COGE,
	   A.STATO_COAN,
       B.cd_cds_obbligazione,
       B.esercizio_obbligazione,
       B.esercizio_ori_obbligazione,
       B.pg_obbligazione,
       B.pg_obbligazione_scadenzario
FROM   FATTURA_PASSIVA A,
       FATTURA_PASSIVA_RIGA B
WHERE  B.cd_cds_obbligazione IS NOT NULL AND
       B.esercizio_obbligazione IS NOT NULL AND
       B.esercizio_ori_obbligazione IS NOT NULL AND
       B.pg_obbligazione IS NOT NULL AND
       B.pg_obbligazione_scadenzario IS NOT NULL AND
	   B.cd_cds = A.cd_cds AND
       B.cd_unita_organizzativa = A.cd_unita_organizzativa AND
       B.esercizio = A.esercizio AND
       B.pg_fattura_passiva = A.pg_fattura_passiva AND
       B.dt_cancellazione IS NULL
UNION ALL
SELECT
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
	   A.protocollo_iva,
       B.ti_associato_manrev,
       'N',
       B.progressivo_riga,
       B.stato_cofi,
	   A.STATO_COGE,
	   A.STATO_COAN,
       B.cd_cds_obbligazione,
       B.esercizio_obbligazione,
       B.esercizio_ori_obbligazione,
       B.pg_obbligazione,
       B.pg_obbligazione_scadenzario
FROM   FATTURA_ATTIVA A,
       FATTURA_ATTIVA_RIGA B
WHERE  A.ti_fattura = 'C' AND
       B.cd_cds = A.cd_cds AND
       B.cd_unita_organizzativa = A.cd_unita_organizzativa AND
       B.esercizio = A.esercizio AND
       B.pg_fattura_attiva = A.pg_fattura_attiva AND
       B.cd_cds_obbligazione IS NOT NULL AND
       B.esercizio_obbligazione IS NOT NULL AND
       B.esercizio_ori_obbligazione IS NOT NULL AND
       B.pg_obbligazione IS NOT NULL AND
       B.pg_obbligazione_scadenzario IS NOT NULL AND
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
       A.stato_pagamento_fondo_eco,
       B.progressivo_riga,
       B.stato_cofi,
	   A.STATO_COGE,
	   A.STATO_COAN,
       B.cd_cds_obbligazione,
       B.esercizio_obbligazione,
       B.esercizio_ori_obbligazione,
       B.pg_obbligazione,
       B.pg_obbligazione_scadenzario
FROM   DOCUMENTO_GENERICO_RIGA B,
       DOCUMENTO_GENERICO A
WHERE  B.cd_cds_obbligazione IS NOT NULL AND
       B.esercizio_obbligazione IS NOT NULL AND
       B.esercizio_ori_obbligazione IS NOT NULL AND
       B.pg_obbligazione IS NOT NULL AND
       B.pg_obbligazione_scadenzario IS NOT NULL AND
       B.dt_cancellazione IS NULL AND
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
       'ANTICIPO',
       null,
       null,
	   'Y',
       A.pg_anticipo,
	   to_number(null),
       A.ti_associato_manrev,
       A.stato_pagamento_fondo_eco,
       0,
       A.stato_cofi,
	   A.STATO_COGE,
	   A.STATO_COAN,
       A.cd_cds_obbligazione,
       A.esercizio_obbligazione,
       A.esercizio_ori_obbligazione,
       A.pg_obbligazione,
       A.pg_obbligazione_scadenzario
FROM   ANTICIPO A
WHERE  A.cd_cds_obbligazione IS NOT NULL AND
       A.esercizio_obbligazione IS NOT NULL AND
       A.esercizio_ori_obbligazione IS NOT NULL AND
       A.pg_obbligazione IS NOT NULL AND
       A.pg_obbligazione_scadenzario IS NOT NULL AND
       A.dt_cancellazione IS NULL
UNION ALL
SELECT
       A.cd_cds,
       A.cd_unita_organizzativa,
       A.cd_cds_origine,
       A.cd_uo_origine,
       A.esercizio,
       'COMPENSO',
       null,
       null,
	   'Y',
       A.pg_compenso,
	   to_number(null),
       A.ti_associato_manrev,
       A.stato_pagamento_fondo_eco,
       0,
       A.stato_cofi,
	   A.STATO_COGE,
	   A.STATO_COAN,
       A.cd_cds_obbligazione,
       A.esercizio_obbligazione,
       A.esercizio_ori_obbligazione,
       A.pg_obbligazione,
       A.pg_obbligazione_scadenzario
FROM   COMPENSO A
WHERE  A.cd_cds_obbligazione IS NOT NULL AND
       A.esercizio_obbligazione IS NOT NULL AND
       A.esercizio_ori_obbligazione IS NOT NULL AND
       A.pg_obbligazione IS NOT NULL AND
       A.pg_obbligazione_scadenzario IS NOT NULL AND
       A.dt_cancellazione IS NULL AND
       A.pg_compenso > 0
UNION ALL
SELECT
       A.cd_cds,
       A.cd_unita_organizzativa,
       A.cd_cds,
       A.cd_unita_organizzativa,
       A.esercizio,
       'MISSIONE',
       null,
       null,
	   'Y',
       A.pg_missione,
	   to_number(null),
       A.ti_associato_manrev,
       A.stato_pagamento_fondo_eco,
       0,
       A.stato_cofi,
	   A.STATO_COGE,
	   A.STATO_COAN,
       A.cd_cds_obbligazione,
       A.esercizio_obbligazione,
       A.esercizio_ori_obbligazione,
       A.pg_obbligazione,
       A.pg_obbligazione_scadenzario
FROM   MISSIONE A
WHERE  A.cd_cds_obbligazione IS NOT NULL AND
       A.esercizio_obbligazione IS NOT NULL AND
       A.esercizio_ori_obbligazione IS NOT NULL AND
       A.pg_obbligazione IS NOT NULL AND
       A.pg_obbligazione_scadenzario IS NOT NULL AND
       A.dt_cancellazione IS NULL;

   COMMENT ON TABLE "V_DOC_AMM_OBB"  IS 'Vista per estrazione per ogni scadenza di obbligazione tutti i dettagli di doc. amm. associati';
