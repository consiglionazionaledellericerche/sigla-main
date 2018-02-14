--------------------------------------------------------
--  DDL for View V_LETTERA_PAGAM_ESTERO_DOC
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_LETTERA_PAGAM_ESTERO_DOC" ("ESERCIZIO", "CD_CDS", "CD_UNITA_ORGANIZZATIVA", "PG_LETTERA", "CD_TIPO_DOCUMENTO_AMM", "PG_DOCUMENTO_AMM", "STATO_COFI", "IM_DOCUMENTO_AMM") AS 
  SELECT
-- =================================================================================================
--
-- Date: 17/03/2003
-- Version: 1.2
--
-- Estrae informazioni sul numero di documento, importo e stato del documento collegato alla lettera di pagamento estero
-- che può essere una fattura passiva o un generico di spesa
--
-- History:
--
-- Date: 19/02/2002
-- Version: 1.0
-- Creazione
--
-- Date: 14/03/2003
-- Version: 1.1
--
-- Fix errore CINECA n. 531. Per le fatture in divisa non era estratto il valore totale in euro ma
-- quello in divisa.
-- Revisione vista per ottimizzazione accessi
--
-- Date: 17/03/2003
-- Version: 1.2
--
-- Fix errore 537. Filtrate le lettere già collegate a sospeso
--
-- Body:
--
-- =================================================================================================
       A.esercizio,
       A.cd_cds,
       A.cd_unita_organizzativa,
       B.pg_lettera,
       'FATTURA_P',
       B.pg_fattura_passiva,
       B.stato_cofi,
       B.im_totale_quadratura
FROM   LETTERA_PAGAM_ESTERO A, FATTURA_PASSIVA B
WHERE  A.CD_SOSPESO IS NULL AND
       B.cd_cds = A.cd_cds AND
       B.esercizio_lettera = A.esercizio AND
       B.cd_unita_organizzativa = A.cd_unita_organizzativa AND
       B.pg_lettera = A.pg_lettera
UNION ALL
SELECT A.esercizio,
       A.cd_cds,
       A.cd_unita_organizzativa,
       B.pg_lettera,
       B.cd_tipo_documento_amm,
       B.pg_documento_generico,
       B.stato_cofi,
       B.im_totale
FROM   LETTERA_PAGAM_ESTERO A, DOCUMENTO_GENERICO B
WHERE  A.CD_SOSPESO IS NULL AND
       B.cd_cds = A.cd_cds AND
       B.esercizio_lettera = A.esercizio AND
       B.cd_unita_organizzativa = A.cd_unita_organizzativa AND
       B.pg_lettera = A.pg_lettera
;

   COMMENT ON TABLE "V_LETTERA_PAGAM_ESTERO_DOC"  IS 'Estrae informazioni sul numero di documento, importo e stato del documento collegato alla lettera di pagamento estero
che può essere una fattura passiva o un generico di spesa';
