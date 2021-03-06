--------------------------------------------------------
--  DDL for View V_FONDO_DOC_AMM
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_FONDO_DOC_AMM" ("CD_TIPO_DOCUMENTO_AMM", "CD_CDS", "ESERCIZIO", "CD_UNITA_ORGANIZZATIVA", "PG_DOCUMENTO", "ST_PAGAMENTO_FONDO", "DT_PAGAMENTO_FONDO") AS 
  SELECT
--
-- Date: 27/11/2003
-- Version: 1.0
--
-- Estrae i i doc amm legati ai fondi economali
--
-- History:
-- Date: 27/11/2003
-- Version: 1.0
-- Creazione
--
-- Body:
--
"CD_TIPO_DOCUMENTO_AMM","CD_CDS","ESERCIZIO","CD_UNITA_ORGANIZZATIVA","PG_DOCUMENTO","ST_PAGAMENTO_FONDO","DT_PAGAMENTO_FONDO"
FROM (
SELECT 'ANTICIPO' CD_TIPO_DOCUMENTO_AMM,
	   CD_CDS,
	   ESERCIZIO,
	   CD_UNITA_ORGANIZZATIVA,
	   PG_ANTICIPO PG_DOCUMENTO,
	   STATO_PAGAMENTO_FONDO_ECO ST_PAGAMENTO_FONDO,
	   DT_PAGAMENTO_FONDO_ECO DT_PAGAMENTO_FONDO
FROM ANTICIPO
UNION ALL
SELECT 'COMPENSO' CD_TIPO_DOCUMENTO_AMM,
	   CD_CDS, ESERCIZIO,
	   CD_UNITA_ORGANIZZATIVA,
	   PG_COMPENSO PG_DOCUMENTO,
	   STATO_PAGAMENTO_FONDO_ECO ST_PAGAMENTO_FONDO,
	   DT_PAGAMENTO_FONDO_ECO DT_PAGAMENTO_FONDO
FROM COMPENSO
UNION ALL
SELECT 'FATTURA_P' CD_TIPO_DOCUMENTO_AMM,
	   CD_CDS, ESERCIZIO,
	   CD_UNITA_ORGANIZZATIVA,
	   PG_FATTURA_PASSIVA PG_DOCUMENTO,
	   STATO_PAGAMENTO_FONDO_ECO ST_PAGAMENTO_FONDO,
	   DT_PAGAMENTO_FONDO_ECO DT_PAGAMENTO_FONDO
FROM FATTURA_PASSIVA
UNION ALL
SELECT 'GENERICO_S' CD_TIPO_DOCUMENTO_AMM,
	   CD_CDS, ESERCIZIO,
	   CD_UNITA_ORGANIZZATIVA,
	   PG_DOCUMENTO_GENERICO PG_DOCUMENTO,
	   STATO_PAGAMENTO_FONDO_ECO ST_PAGAMENTO_FONDO,
	   DT_PAGAMENTO_FONDO_ECO DT_PAGAMENTO_FONDO
FROM DOCUMENTO_GENERICO
WHERE CD_TIPO_DOCUMENTO_AMM = 'GENERICO_S'
UNION ALL
SELECT 'MISSIONE' CD_TIPO_DOCUMENTO_AMM,
	   CD_CDS, ESERCIZIO,
	   CD_UNITA_ORGANIZZATIVA,
	   PG_MISSIONE PG_DOCUMENTO,
	   STATO_PAGAMENTO_FONDO_ECO ST_PAGAMENTO_FONDO,
	   DT_PAGAMENTO_FONDO_ECO DT_PAGAMENTO_FONDO
FROM MISSIONE
)
;

   COMMENT ON TABLE "V_FONDO_DOC_AMM"  IS 'Vista di estrazione dei documenti amministrativi legati ai fondi economali';
