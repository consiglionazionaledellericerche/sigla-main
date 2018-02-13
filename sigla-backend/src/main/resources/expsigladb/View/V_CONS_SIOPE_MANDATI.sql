--------------------------------------------------------
--  DDL for View V_CONS_SIOPE_MANDATI
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_CONS_SIOPE_MANDATI" ("CD_CDS", "ESERCIZIO", "PG_MANDATO", "ESERCIZIO_OBBLIGAZIONE", "ESERCIZIO_ORI_OBBLIGAZIONE", "PG_OBBLIGAZIONE", "PG_OBBLIGAZIONE_SCADENZARIO", "CD_CDS_DOC_AMM", "CD_UO_DOC_AMM", "ESERCIZIO_DOC_AMM", "CD_TIPO_DOCUMENTO_AMM", "DS_TIPO_DOC_AMM", "PG_DOC_AMM", "ESERCIZIO_SIOPE", "TI_GESTIONE", "CD_SIOPE", "DS_SIOPE", "IMPORTO", "DT_EMISSIONE", "DT_PAGAMENTO", "DT_TRASMISSIONE") AS 
  select
--
-- Date: 06/07/2007
-- Versione 1.0
--
-- Consultazione Riepilogo Codici Siope Spese
--
-- History:
--
-- Date: 06/07/2007
-- Versione 1.0
-- Creazione
--
-- Body:
--
ms.cd_cds, ms.esercizio, ms.pg_mandato, ms.esercizio_obbligazione,
ms.esercizio_ori_obbligazione, ms.pg_obbligazione, ms.pg_obbligazione_scadenzario, ms.cd_cds_doc_amm, ms.cd_uo_doc_amm,
ms.esercizio_doc_amm,ms.cd_tipo_documento_amm,da.ds_tipo_documento_amm,ms.pg_doc_amm,
ms.esercizio_siope,
ms.ti_gestione, ms.cd_siope, s.descrizione, ms.importo,
trunc(m.dt_emissione), trunc(m.dt_pagamento), trunc(m.dt_trasmissione)
from mandato m, mandato_siope ms, codici_siope s, tipo_documento_amm da
where m.cd_cds = ms.cd_cds
and m.esercizio = ms.esercizio
and m.pg_mandato = ms.pg_mandato
and m.stato != 'A'
and ms.esercizio = s.esercizio
and ms.ti_gestione = s.ti_gestione
and ms.cd_siope = s.cd_siope
and ms.cd_tipo_documento_amm=da.cd_tipo_documento_amm ;
