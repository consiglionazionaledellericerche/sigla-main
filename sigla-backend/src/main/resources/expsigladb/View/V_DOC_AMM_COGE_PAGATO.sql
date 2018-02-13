--------------------------------------------------------
--  DDL for View V_DOC_AMM_COGE_PAGATO
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_DOC_AMM_COGE_PAGATO" ("CD_TIPO_DOCUMENTO", "CD_CDS", "CD_UNITA_ORGANIZZATIVA", "ESERCIZIO", "PG_NUMERO_DOCUMENTO", "CD_TERZO", "CD_ELEMENTO_VOCE", "STATO_COGE_DOCAMM", "STATO_COGE_DOCCONT", "IM_PAGATO_INCASSATO") AS 
  (
select -- Estrazione fattura passiva
--
-- Date: 15/07/2003
-- Version: 1.0
--
-- Vista di estrazione del saldo pagato di FATTURE E GENERICI
--
-- History:
--
-- Date: 15/07/2003
-- Version: 1.0
-- Creazione
--
--
-- Body:
--
     MANDATO_RIGA.CD_TIPO_DOCUMENTO_AMM
    ,MANDATO_RIGA.CD_CDS_DOC_AMM
    ,MANDATO_RIGA.CD_UO_DOC_AMM
    ,MANDATO_RIGA.ESERCIZIO_DOC_AMM
    ,MANDATO_RIGA.PG_DOC_AMM
    ,MANDATO_RIGA.CD_TERZO
    ,obbligazione.CD_ELEMENTO_VOCE
    ,obbligazione.STATO_COGE_DOCAMM
    ,obbligazione.STATO_COGE_DOCCONT
    ,sum(MANDATO_RIGA.im_mandato_riga)
FROM obbligazione,  mandato_riga
 WHERE  		 obbligazione.cd_cds 											= mandato_riga.cd_cds
        AND  obbligazione.esercizio 									= mandato_riga.esercizio_obbligazione
        AND  obbligazione.esercizio_originale 				= mandato_riga.esercizio_ori_obbligazione
        AND  obbligazione.pg_obbligazione 						= mandato_riga.pg_obbligazione
        AND  MANDATO_RIGA.stato <> 'A'
group by
     MANDATO_RIGA.CD_TIPO_DOCUMENTO_AMM
    ,MANDATO_RIGA.CD_CDS_DOC_AMM
    ,MANDATO_RIGA.CD_UO_DOC_AMM
    ,MANDATO_RIGA.ESERCIZIO_DOC_AMM
    ,MANDATO_RIGA.PG_DOC_AMM
    ,MANDATO_RIGA.CD_TERZO
    ,obbligazione.CD_ELEMENTO_VOCE
    ,obbligazione.STATO_COGE_DOCAMM
    ,obbligazione.STATO_COGE_DOCCONT
union all
select
     REVERSALE_RIGA.CD_TIPO_DOCUMENTO_AMM
    ,REVERSALE_RIGA.CD_CDS_DOC_AMM
    ,REVERSALE_RIGA.CD_UO_DOC_AMM
    ,REVERSALE_RIGA.ESERCIZIO_DOC_AMM
    ,REVERSALE_RIGA.PG_DOC_AMM
    ,REVERSALE_RIGA.CD_TERZO
    ,ACCERTAMENTO.CD_ELEMENTO_VOCE
    ,ACCERTAMENTO.STATO_COGE_DOCAMM
    ,ACCERTAMENTO.STATO_COGE_DOCCONT
    ,sum(REVERSALE_RIGA.im_reversale_riga)
from REVERSALE_RIGA, accertamento
 WHERE       accertamento.cd_cds 											= reversale_riga.cd_cds
        AND  accertamento.esercizio 									= reversale_riga.esercizio_accertamento
        AND  accertamento.esercizio_originale 				= reversale_riga.esercizio_ori_accertamento
        AND  accertamento.pg_accertamento 						= reversale_riga.pg_accertamento
        and  reversale_riga.stato <> 'A'
group by
    REVERSALE_RIGA.CD_TIPO_DOCUMENTO_AMM
    ,REVERSALE_RIGA.CD_CDS_DOC_AMM
    ,REVERSALE_RIGA.CD_UO_DOC_AMM
    ,REVERSALE_RIGA.ESERCIZIO_DOC_AMM
    ,REVERSALE_RIGA.PG_DOC_AMM
    ,REVERSALE_RIGA.CD_TERZO
    ,ACCERTAMENTO.CD_ELEMENTO_VOCE
    ,ACCERTAMENTO.STATO_COGE_DOCAMM
    ,ACCERTAMENTO.STATO_COGE_DOCCONT
);

   COMMENT ON TABLE "V_DOC_AMM_COGE_PAGATO"  IS 'Vista di estrazione del saldo pagato di FATTURE E GENERICI';
