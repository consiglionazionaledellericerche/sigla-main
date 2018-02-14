--------------------------------------------------------
--  DDL for View V_REVERSALE_UPG_STATO_DOCAMM
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_REVERSALE_UPG_STATO_DOCAMM" ("CD_CDS_REVERSALE", "ESERCIZIO_REVERSALE", "PG_REVERSALE", "CD_UO_REVERSALE", "CD_CDS_ORI_REVERSALE", "CD_UO_ORI_REVERSALE", "CD_TIPO_REVERSALE", "TI_REVERSALE", "STATO_REVERSALE", "DT_EMISSIONE_REVERSALE", "CD_TERZO", "ESERCIZIO_ACCERTAMENTO", "ESERCIZIO_ORI_ACCERTAMENTO", "PG_ACCERTAMENTO", "PG_ACCERTAMENTO_SCADENZARIO", "CD_CDS_DOCAMM", "CD_UO_DOCAMM", "ESERCIZIO_DOCAMM", "CD_TIPO_DOCAMM", "PG_DOCAMM") AS 
  SELECT
-- =================================================================================================
--
-- Date: 19/07/2006
-- Version: 1.3
--
-- Vista di estrazione di REVERSALE join REVERSALE_RIGA per aggiornamento dello
-- stato_cofi e cancellazione logica sui documenti amministrativi associati
-- allo stesso
--
-- History:
--
-- Date: 18/03/2002
-- Version: 1.0
--
-- Creazione vista
--
-- Date: 05/02/2003
-- Version: 1.1
--
-- Estrazione della data di emissione della reversale per poter esporre la data di esigibilità iva
--
-- Date: 19/02/2003
-- Version: 1.2
--
-- Estrazione del codice terzo della riga mandato per gestire l'aggiornamento dello stato sui
-- documenti amministrativi. Se più righe di generico insistono sulla stessa scadenza di obbligazione
-- (partite di giro a consumo su CNR) solo quelle che hanno terzo uguale a quello del mandato devono
-- essere aggiornate in modo concorde con inserimento o annullamento di mandati e reversali.
--
-- Date: 19/07/2006
-- Version: 1.3
-- Gestione Impegni/Accertamenti Residui:
-- gestito il nuovo campo ESERCIZIO_ORIGINALE
--
-- Body:
--
-- =================================================================================================
       A.cd_cds,
       A.esercizio,
       A.pg_reversale,
       A.cd_unita_organizzativa,
       A.cd_cds_origine,
       A.cd_uo_origine,
       A.cd_tipo_documento_cont,
       A.ti_reversale,
       A.stato,
       A.dt_emissione,
       B.cd_terzo,
       B.esercizio_accertamento,
       B.esercizio_ori_accertamento,
       B.pg_accertamento,
       B.pg_accertamento_scadenzario,
       B.cd_cds_doc_amm,
       B.cd_uo_doc_amm,
       B.esercizio_doc_amm,
       B.cd_tipo_documento_amm,
       B.pg_doc_amm
FROM   REVERSALE A,
       REVERSALE_RIGA B
WHERE  B.cd_cds = A.cd_cds AND
       B.esercizio = A.esercizio AND
       B.pg_reversale = A.pg_reversale;

   COMMENT ON TABLE "V_REVERSALE_UPG_STATO_DOCAMM"  IS 'Vista di estrazione di REVERSALE join REVERSALE_RIGA per aggiornamento dello
stato_cofi e cancellazione logica sui documenti amministrativi associati
allo stesso';
