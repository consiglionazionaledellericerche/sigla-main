--------------------------------------------------------
--  DDL for View V_TOT_DISTINTA_REV
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_TOT_DISTINTA_REV" ("CD_CDS", "ESERCIZIO", "CD_UNITA_ORGANIZZATIVA", "PG_DISTINTA", "TI_REVERSALE", "CD_TIPO_DOCUMENTO_CONT", "TI_CC_BI", "IM_TOT", "IM_TOT_ANN", "IM_TOT_ANN_RIT") AS 
  SELECT
--
-- Date: 17/02/2003
-- Version: 1.1
--
-- View di estrazione dei totali reversali (BNL e BI), tot attive, annullate, annullate e ritrasmesse
--
-- History:
--
-- Date: 17/02/2003
-- Version: 1.0
-- Creazione vista
--
-- Date: 17/02/2003
-- Version: 1.1
-- Fix
--
-- Body:
--
 A.CD_CDS,
 A.ESERCIZIO,
 A.CD_UNITA_ORGANIZZATIVA,
 A.PG_DISTINTA,
 B.TI_REVERSALE,
 B.CD_TIPO_DOCUMENTO_CONT,
 'C',
 SUM(decode(b.DT_ANNULLAMENTO,NULL,B.IM_REVERSALE,0)),
 SUM(decode(b.DT_ANNULLAMENTO,NULL,0,decode(b.DT_TRASMISSIONE,NULL,B.IM_REVERSALE,
            decode(sign(b.DT_ANNULLAMENTO-b.DT_TRASMISSIONE),-1,B.IM_REVERSALE,0)))),
 SUM(decode(b.DT_ANNULLAMENTO,NULL,0,decode(b.DT_TRASMISSIONE,NULL,0,
            decode(sign(b.DT_TRASMISSIONE-b.DT_ANNULLAMENTO),-1,B.IM_REVERSALE,0))))
FROM
 DISTINTA_CASSIERE_DET A, REVERSALE B
WHERE
     B.CD_CDS = A.CD_CDS_origine
 AND B.ESERCIZIO = A.ESERCIZIO
 AND B.PG_REVERSALE = A.PG_REVERSALE
 AND B.TI_REVERSALE = 'S'
 AND EXISTS (select 1 from sospeso s, sospeso_det_etr se where
              se.cd_cds_reversale = b.cd_cds AND
              se.esercizio = b.esercizio AND
              se.pg_reversale = b.pg_reversale AND
--              se.stato = 'N' AND
              s.esercizio = se.esercizio AND
              s.cd_cds = se.cd_cds AND
              s.ti_entrata_spesa = se.ti_entrata_spesa AND
              s.ti_sospeso_riscontro = se.ti_sospeso_riscontro AND
              s.cd_sospeso = se.cd_sospeso AND
              s.TI_CC_BI = 'C'
) group by
 A.CD_CDS,
 A.ESERCIZIO,
 A.CD_UNITA_ORGANIZZATIVA,
 A.PG_DISTINTA,
 B.TI_REVERSALE,
 B.CD_TIPO_DOCUMENTO_CONT
union all
SELECT
 A.CD_CDS,
 A.ESERCIZIO,
 A.CD_UNITA_ORGANIZZATIVA,
 A.PG_DISTINTA,
 B.TI_REVERSALE,
 B.CD_TIPO_DOCUMENTO_CONT,
 'B',
 SUM(decode(b.DT_ANNULLAMENTO,NULL,B.IM_REVERSALE,0)),
 SUM(decode(b.DT_ANNULLAMENTO,NULL,0,decode(b.DT_TRASMISSIONE,NULL,B.IM_REVERSALE,
            decode(sign(b.DT_ANNULLAMENTO-b.DT_TRASMISSIONE),-1,B.IM_REVERSALE,0)))),
 SUM(decode(b.DT_ANNULLAMENTO,NULL,0,decode(b.DT_TRASMISSIONE,NULL,0,
            decode(sign(b.DT_TRASMISSIONE-b.DT_ANNULLAMENTO),-1,B.IM_REVERSALE,0))))
FROM
 DISTINTA_CASSIERE_DET A, REVERSALE B
WHERE
     B.CD_CDS = A.CD_CDS_origine
 AND B.ESERCIZIO = A.ESERCIZIO
 AND B.PG_REVERSALE = A.PG_REVERSALE
 AND B.TI_REVERSALE = 'S'
 AND EXISTS (select 1 from sospeso s, sospeso_det_etr se where
              se.cd_cds_reversale = b.cd_cds AND
              se.esercizio = b.esercizio AND
              se.pg_reversale = b.pg_reversale AND
--              se.stato = 'N' AND
              s.esercizio = se.esercizio AND
              s.cd_cds = se.cd_cds AND
              s.ti_entrata_spesa = se.ti_entrata_spesa AND
              s.ti_sospeso_riscontro = se.ti_sospeso_riscontro AND
              s.cd_sospeso = se.cd_sospeso AND
              s.TI_CC_BI = 'B'
) group by
 A.CD_CDS,
 A.ESERCIZIO,
 A.CD_UNITA_ORGANIZZATIVA,
 A.PG_DISTINTA,
 B.TI_REVERSALE,
 B.CD_TIPO_DOCUMENTO_CONT;

   COMMENT ON TABLE "V_TOT_DISTINTA_REV"  IS 'View di estrazione dei totali reversali (BNL e BI), tot attive, annullate, annullate e ritrasmesse';
