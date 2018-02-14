--------------------------------------------------------
--  DDL for View V_MANDATO_RIGA_DP_SOSP
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_MANDATO_RIGA_DP_SOSP" ("CD_CDS", "ESERCIZIO", "PG_MANDATO", "CD_TIPO_DOC_AMM", "CD_CDS_DOC_AMM", "ESERCIZIO_DOC_AMM", "CD_UO_DOC_AMM", "PG_DOC_AMM", "CD_SOSPESO") AS 
  SELECT
--==================================================================================================
--
-- Date: 26/11/2003
-- Version: 1.0
--
-- Vista di estrazione del codice sospeso dei doc amm collegati al mandato specificato
--
-- Dependency:
--
-- History:
--
-- Date: 26/11/2003
-- Version: 1.0
-- Creazione
--
--
-- Body:
--
--==================================================================================================
       B.cd_cds,
       B.esercizio,
       B.pg_mandato,
	   'FATTURA_P',
	   a.cd_cds,
	   a.esercizio,
	   a.cd_unita_organizzativa,
	   a.pg_fattura_passiva,
       c.cd_sospeso
FROM   MANDATO_RIGA B,
       FATTURA_PASSIVA A,
       LETTERA_PAGAM_ESTERO C
WHERE
	   B.CD_TIPO_DOCUMENTO_AMM = 'FATTURA_P' AND
       A.cd_cds = B.cd_cds_doc_amm AND
       A.cd_unita_organizzativa = B.cd_uo_doc_amm AND
       A.esercizio = B.esercizio_doc_amm AND
       A.pg_fattura_passiva = B.pg_doc_amm AND
       C.cd_cds = A.cd_cds AND
       C.cd_unita_organizzativa = A.cd_unita_organizzativa AND
       C.esercizio = A.esercizio_lettera AND
       C.pg_lettera = A.pg_lettera
UNION ALL
SELECT
       B.cd_cds,
       B.esercizio,
       B.pg_mandato,
	   a.cd_tipo_documento_amm,
	   a.cd_cds,
	   a.esercizio,
	   a.cd_unita_organizzativa,
	   a.pg_documento_generico,
       c.cd_sospeso
FROM   MANDATO_RIGA B,
       DOCUMENTO_GENERICO A,
       LETTERA_PAGAM_ESTERO C
WHERE
	   B.CD_TIPO_DOCUMENTO_AMM = A.CD_TIPO_DOCUMENTO_AMM AND
       A.cd_cds = B.cd_cds_doc_amm AND
       A.cd_unita_organizzativa = B.cd_uo_doc_amm AND
       A.esercizio = B.esercizio_doc_amm AND
       A.pg_documento_generico = B.pg_doc_amm AND
       C.cd_cds = A.cd_cds AND
       C.cd_unita_organizzativa = A.cd_unita_organizzativa AND
       C.esercizio = A.esercizio_lettera AND
       C.pg_lettera = A.pg_lettera
;

   COMMENT ON TABLE "V_MANDATO_RIGA_DP_SOSP"  IS 'Vista di estrazione del codice sospeso dei doc amm collegati al mandato specificato';
