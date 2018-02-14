--------------------------------------------------------
--  DDL for View V_DOC_CONT_COMP
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_DOC_CONT_COMP" ("CD_CDS_COMPENSO", "CD_UO_COMPENSO", "ESERCIZIO_COMPENSO", "PG_COMPENSO", "CD_CDS_DOC_CONT", "ESERCIZIO_DOC_CONT", "PG_DOC_CONT", "DS_DOC_CONT", "TIPO_DOC_CONT", "DT_TRASMISSIONE", "DT_PAGAMENTO", "PRINCIPALE") AS 
  SELECT
--==============================================================================
--
-- Date: 19/07/2006
-- Version: 1.5
--
-- Vista estrazione documenti contabili generati dai compensi
--
-- History:
-- Date: 11/10/2002
-- Version: 1.0
-- Creazione
--
-- Date: 18/10/2002
-- Version: 1.1
-- Aggiunto filtro sul campo CD_TIPO_DOCUMENTO_AMM nella tabella MANDATO_RIGA
--
-- Date: 23/10/2002
-- Version: 1.2
-- Inserita UNION ALL tra la tabella COMPENSO e la tabella MANDATO_RIGA per estrarre
-- solo il Mandato Principale associato al compenso
-- Modificate le UNION ALL per recuperare i Mandati/Reversali secondari
-- associati al Mandato Principale (potrebbero non esistere anche in presenza di un
-- mandato principale)
-- Inserita UNION ALL tra la tabella COMPENSO e la tabella REVERSALE_RIGA per estrarre
-- solo la Reversale Principale associata al compenso
-- Inserito flag per indentificare il Documento Contabile principale
--
-- Date: 11/11/2002
-- Version: 1.3
-- Inserito il filtro per scartare i mandati e le reversali annullate
--
-- Date: 03/02/2003
-- Version: 1.4
-- Inserito filtro per evitare duplicazione  reversali e mandati legati a compenso senza mandato principale,
-- nel caso venga creato il collegamento manualmente tra tali mandati e reversali
--
-- Date: 19/07/2006
-- Version: 1.5
-- Gestione Impegni/Accertamenti Residui:
-- gestito il nuovo campo ESERCIZIO_ORIGINALE
--
-- Date: 09/09/2016
-- Version: 1.6
-- Aggiunti campi data trasmissione e data pagamento de mandati/reversali
--
-- Body:
--
--==============================================================================
       C.CD_CDS,
       C.CD_UNITA_ORGANIZZATIVA,
       C.ESERCIZIO,
       C.PG_COMPENSO,
       M.CD_CDS,
       M.ESERCIZIO,
       M.PG_MANDATO,
       M.DS_MANDATO,
       'M',
       M.DT_TRASMISSIONE, 
       M.DT_PAGAMENTO, 
       'Y'
FROM   MANDATO_RIGA MR, COMPENSO C, MANDATO M
WHERE  MR.CD_CDS_DOC_AMM = C.CD_CDS AND
       MR.CD_UO_DOC_AMM = C.CD_UNITA_ORGANIZZATIVA AND
       MR.ESERCIZIO_DOC_AMM = C.ESERCIZIO AND
       MR.PG_DOC_AMM = C.PG_COMPENSO AND
       MR.CD_TIPO_DOCUMENTO_AMM = 'COMPENSO' AND
       MR.STATO <> 'A' AND
       M.CD_CDS = MR.CD_CDS AND
       M.ESERCIZIO = MR.ESERCIZIO AND
       M.PG_MANDATO = MR.PG_MANDATO
UNION ALL
SELECT
       C.CD_CDS,
       C.CD_UNITA_ORGANIZZATIVA,
       C.ESERCIZIO,
       C.PG_COMPENSO,
       AMR.CD_CDS_REVERSALE,
       AMR.ESERCIZIO_REVERSALE,
       AMR.PG_REVERSALE,
       R.DS_REVERSALE,
       'R',
       R.DT_TRASMISSIONE, 
       R.DT_INCASSO, 
       'N'
FROM   MANDATO_RIGA MR, COMPENSO C, ASS_MANDATO_REVERSALE AMR, REVERSALE R
WHERE  MR.CD_CDS_DOC_AMM = C.CD_CDS AND
       MR.CD_UO_DOC_AMM = C.CD_UNITA_ORGANIZZATIVA AND
       MR.ESERCIZIO_DOC_AMM = C.ESERCIZIO AND
       MR.PG_DOC_AMM = C.PG_COMPENSO AND
       MR.CD_TIPO_DOCUMENTO_AMM = 'COMPENSO' AND
       MR.STATO <> 'A' AND
       AMR.CD_CDS_MANDATO = MR.CD_CDS AND
       AMR.ESERCIZIO_MANDATO = MR.ESERCIZIO AND
       AMR.PG_MANDATO = MR.PG_MANDATO AND
       R.CD_CDS = AMR.CD_CDS_REVERSALE AND
       R.ESERCIZIO = AMR.ESERCIZIO_REVERSALE AND
       R.PG_REVERSALE = AMR.PG_REVERSALE AND
	   NOT EXISTS (SELECT 1 FROM ASS_COMP_DOC_CONT_NMP WHERE
	        CD_CDS_DOC = AMR.CD_CDS_REVERSALE
		AND ESERCIZIO_DOC = AMR.ESERCIZIO_REVERSALE
		AND PG_DOC = AMR.PG_REVERSALE
		AND CD_TIPO_DOC = 'R'
	   )
UNION ALL
SELECT
       C.CD_CDS,
       C.CD_UNITA_ORGANIZZATIVA,
       C.ESERCIZIO,
       C.PG_COMPENSO,
       AMM.CD_CDS_COLL,
       AMM.ESERCIZIO_COLL,
       AMM.PG_MANDATO_COLL,
       M.DS_MANDATO,
       'M',
       M.DT_TRASMISSIONE, 
       M.DT_PAGAMENTO, 
       'N'
FROM   MANDATO_RIGA MR, COMPENSO C, ASS_MANDATO_MANDATO AMM, MANDATO M
WHERE  MR.CD_CDS_DOC_AMM = C.CD_CDS AND
       MR.CD_UO_DOC_AMM = C.CD_UNITA_ORGANIZZATIVA AND
       MR.ESERCIZIO_DOC_AMM = C.ESERCIZIO AND
       MR.PG_DOC_AMM = C.PG_COMPENSO AND
       MR.CD_TIPO_DOCUMENTO_AMM = 'COMPENSO' AND
       MR.STATO <> 'A' AND
       AMM.CD_CDS = MR.CD_CDS AND
       AMM.ESERCIZIO = MR.ESERCIZIO AND
       AMM.PG_MANDATO = MR.PG_MANDATO AND
       M.CD_CDS = AMM.CD_CDS_COLL AND
       M.ESERCIZIO = AMM.ESERCIZIO_COLL AND
       M.PG_MANDATO = AMM.PG_MANDATO_COLL AND
	   NOT EXISTS (SELECT 1 FROM ASS_COMP_DOC_CONT_NMP WHERE
	        CD_CDS_DOC = AMM.CD_CDS_COLL
		AND ESERCIZIO_DOC = AMM.ESERCIZIO_COLL
		AND PG_DOC = AMM.PG_MANDATO_COLL
		AND CD_TIPO_DOC = 'M'
	   )
UNION ALL
SELECT
       C.CD_CDS,
       C.CD_UNITA_ORGANIZZATIVA,
       C.ESERCIZIO,
       C.PG_COMPENSO,
       R.CD_CDS,
       R.ESERCIZIO,
       R.PG_REVERSALE,
       R.DS_REVERSALE,
       'R',
       R.DT_TRASMISSIONE, 
       R.DT_INCASSO, 
       'Y'
FROM   COMPENSO C, REVERSALE_RIGA RR, REVERSALE R
WHERE  RR.CD_CDS = C.CD_CDS_ACCERTAMENTO AND
       RR.ESERCIZIO_ACCERTAMENTO = C.ESERCIZIO_ACCERTAMENTO AND
       RR.ESERCIZIO_ORI_ACCERTAMENTO = C.ESERCIZIO_ORI_ACCERTAMENTO AND
       RR.PG_ACCERTAMENTO = C.PG_ACCERTAMENTO AND
       RR.PG_ACCERTAMENTO_SCADENZARIO = C.PG_ACCERTAMENTO_SCADENZARIO AND
       RR.STATO <> 'A' AND
       R.CD_CDS = RR.CD_CDS AND
       R.ESERCIZIO = RR.ESERCIZIO AND
       R.PG_REVERSALE = RR.PG_REVERSALE
UNION ALL
SELECT
       C.CD_CDS,
       C.CD_UNITA_ORGANIZZATIVA,
       C.ESERCIZIO,
       C.PG_COMPENSO,
       ASS.CD_CDS_DOC,
       ASS.ESERCIZIO_DOC,
       ASS.PG_DOC,
       DECODE(ASS.CD_TIPO_DOC,'M',M.DS_MANDATO, R.DS_REVERSALE),
       ASS.CD_TIPO_DOC,
       DECODE(ASS.CD_TIPO_DOC,'M',M.DT_TRASMISSIONE, R.DT_TRASMISSIONE),
       DECODE(ASS.CD_TIPO_DOC,'M',M.DT_PAGAMENTO, R.DT_INCASSO),
       'N'
FROM   ASS_COMP_DOC_CONT_NMP ASS, COMPENSO C, MANDATO M, REVERSALE R
WHERE  ASS.CD_CDS_COMPENSO = C.CD_CDS AND
       ASS.CD_UO_COMPENSO = C.CD_UNITA_ORGANIZZATIVA AND
       ASS.ESERCIZIO_COMPENSO = C.ESERCIZIO AND
       ASS.PG_COMPENSO = C.PG_COMPENSO AND
       M.CD_CDS(+) = ASS.CD_CDS_DOC AND
       M.ESERCIZIO(+) = ASS.ESERCIZIO_DOC AND
       M.PG_MANDATO(+) = ASS.PG_DOC AND
       R.CD_CDS(+) = ASS.CD_CDS_DOC AND
       R.ESERCIZIO(+) = ASS.ESERCIZIO_DOC AND
       R.PG_REVERSALE(+) = ASS.PG_DOC ;

   COMMENT ON TABLE "V_DOC_CONT_COMP"  IS 'Vista estrazione documenti contabili generati dai compensi';
