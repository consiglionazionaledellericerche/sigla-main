--------------------------------------------------------
--  DDL for View V_MANDATO_REVERSALE_VOCE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_MANDATO_REVERSALE_VOCE" ("TI_DOCUMENTO", "TI_COMPETENZA_RESIDUO", "CD_CDS", "ESERCIZIO", "PG_DOCUMENTO", "IM_DOCUMENTO", "TI_APPARTENENZA", "TI_GESTIONE", "CD_VOCE", "IM_CAPITOLO_PESATO") AS 
  select
--
-- Date: 18/07/2006
-- Version: 1.1
--
-- Vista di estrazione della spaccatura del mandato/reversale sui capitoli finanziari collegati (no gestione rotti)
--
-- History:
--
-- Date: 11/09/2002
-- Version: 1.0
-- Creazione
--
-- Date: 18/07/2006
-- Version: 1.1
-- Gestione Impegni/Accertamenti Residui:
-- gestito il nuovo campo ESERCIZIO_ORIGINALE
--
-- Body:
--
 TI_DOCUMENTO,
 TI_COMPETENZA_RESIDUO,
 CD_CDS,
 ESERCIZIO,
 PG_DOCUMENTO,
 IM_DOCUMENTO,
 TI_APPARTENENZA,
 TI_GESTIONE,
 CD_VOCE,
 sum(IM_RIGA_PESATO)
from (
 select
	   'M' ti_documento,
	   --man.TI_COMPETENZA_RESIDUO,
	   Decode (obbs.ESERCIZIO, obbs.ESERCIZIO_ORIGINALE, 'C', 'R') TI_COMPETENZA_RESIDUO,
	   man.im_mandato IM_DOCUMENTO,
	   manr.CD_CDS,
	   manr.ESERCIZIO,
	   manr.pg_mandato pg_documento,
	   manr.ESERCIZIO_OBBLIGAZIONE,
	   manr.ESERCIZIO_ORI_OBBLIGAZIONE,
	   manr.PG_OBBLIGAZIONE,
	   manr.PG_OBBLIGAZIONE_SCADENZARIO,
           manr.CD_CDS_DOC_AMM,
	   manr.CD_UO_DOC_AMM,
	   manr.ESERCIZIO_DOC_AMM,
	   manr.CD_TIPO_DOCUMENTO_AMM,
	   manr.PG_DOC_AMM,
	   obbsv.TI_APPARTENENZA,
	   obbsv.TI_GESTIONE,
	   obbsv.CD_VOCE,
	   manr.IM_MANDATO_RIGA,
       obbs.IM_SCADENZA,
	   DECODE(NVL(obbs.IM_SCADENZA,0),0,0,(sum(obbsv.IM_VOCE)/obbs.IM_SCADENZA )*manr.IM_MANDATO_RIGA) IM_RIGA_PESATO
FROM
	   MANDATO man,
	   MANDATO_RIGA manr,
	   OBBLIGAZIONE_SCADENZARIO obbs,
	   OBBLIGAZIONE_SCAD_VOCE obbsv
WHERE  manr.cd_cds 			             = man.cd_cds
AND    manr.ESERCIZIO 		  			 = man.ESERCIZIO
AND    manr.pg_mandato 		  			 = man.pg_mandato
AND    obbs.cd_cds 			  			 = manr.cd_cds
AND    obbs.ESERCIZIO 		  			 = manr.ESERCIZIO
AND    obbs.ESERCIZIO_ORIGINALE  			 = manr.ESERCIZIO_ORI_OBBLIGAZIONE
AND    obbs.PG_OBBLIGAZIONE	  			 = manr.PG_OBBLIGAZIONE
AND    obbs.PG_OBBLIGAZIONE_SCADENZARIO  = manr.PG_OBBLIGAZIONE_SCADENZARIO
And    obbsv.CD_CDS						 = obbs.CD_CDS
AND    obbsv.ESERCIZIO					 = obbs.ESERCIZIO
AND    obbsv.ESERCIZIO_ORIGINALE  			 = obbs.ESERCIZIO_ORIGINALE
And    obbsv.PG_OBBLIGAZIONE			 = obbs.PG_OBBLIGAZIONE
AND    obbsv.PG_OBBLIGAZIONE_SCADENZARIO = obbs.PG_OBBLIGAZIONE_SCADENZARIO
GROUP BY
	   Decode (obbs.ESERCIZIO, obbs.ESERCIZIO_ORIGINALE, 'C', 'R'), --man.TI_COMPETENZA_RESIDUO,
	   man.im_mandato,
	   manr.CD_CDS,
	   manr.ESERCIZIO,
	   manr.pg_mandato,
	   manr.ESERCIZIO_OBBLIGAZIONE,
	   manr.ESERCIZIO_ORI_OBBLIGAZIONE,
	   manr.PG_OBBLIGAZIONE,
	   manr.PG_OBBLIGAZIONE_SCADENZARIO,
           manr.CD_CDS_DOC_AMM,
	   manr.CD_UO_DOC_AMM,
	   manr.ESERCIZIO_DOC_AMM,
	   manr.CD_TIPO_DOCUMENTO_AMM,
	   manr.PG_DOC_AMM,
	   obbsv.TI_APPARTENENZA,
	   obbsv.TI_GESTIONE,
	   obbsv.CD_VOCE,
	   manr.IM_MANDATO_RIGA,
       obbs.IM_SCADENZA
union all
 select
	   'R' ti_documento,
	   Decode (acc.ESERCIZIO, acc.ESERCIZIO_ORIGINALE, 'C', 'R'), -- rev.TI_COMPETENZA_RESIDUO,
	   rev.im_reversale IM_DOCUMENTO,
	   revr.CD_CDS,
	   revr.ESERCIZIO,
	   revr.pg_reversale pg_documento,
	   revr.ESERCIZIO_ACCERTAMENTO,
	   revr.ESERCIZIO_ORI_ACCERTAMENTO,
	   revr.PG_ACCERTAMENTO,
	   revr.PG_ACCERTAMENTO_SCADENZARIO,
       revr.CD_CDS_DOC_AMM,
	   revr.CD_UO_DOC_AMM,
	   revr.ESERCIZIO_DOC_AMM,
	   revr.CD_TIPO_DOCUMENTO_AMM,
	   revr.PG_DOC_AMM,
	   acc.TI_APPARTENENZA,
	   acc.TI_GESTIONE,
	   acc.CD_VOCE,
	   0,
       0,
	   SUM(revr.im_reversale_riga) IM_RIGA_PESATO
FROM
	   REVERSALE rev,
	   REVERSALE_RIGA revr,
	   ACCERTAMENTO acc
WHERE  revr.cd_cds 			             = rev.cd_cds
AND    revr.ESERCIZIO 		  			 = rev.ESERCIZIO
AND    revr.pg_reversale 		  	     = rev.pg_reversale
AND    acc.CD_CDS						 = revr.CD_CDS
AND    acc.ESERCIZIO					 = revr.ESERCIZIO
AND    acc.ESERCIZIO_ORIGINALE				 = revr.ESERCIZIO_ORI_ACCERTAMENTO
AND	   acc.PG_accertamento			     = revr.PG_accertamento
GROUP BY
	   Decode (acc.ESERCIZIO, acc.ESERCIZIO_ORIGINALE, 'C', 'R'), -- rev.TI_COMPETENZA_RESIDUO,
	   rev.im_reversale,
	   revr.CD_CDS,
	   revr.ESERCIZIO,
	   revr.pg_reversale,
	   revr.ESERCIZIO_ACCERTAMENTO,
	   revr.ESERCIZIO_ORI_ACCERTAMENTO,
	   revr.PG_ACCERTAMENTO,
	   revr.PG_ACCERTAMENTO_SCADENZARIO,
       revr.CD_CDS_DOC_AMM,
	   revr.CD_UO_DOC_AMM,
	   revr.ESERCIZIO_DOC_AMM,
	   revr.CD_TIPO_DOCUMENTO_AMM,
	   revr.PG_DOC_AMM,
	   acc.TI_APPARTENENZA,
	   acc.TI_GESTIONE,
	   acc.CD_VOCE,
	   rev.IM_reversale
) group by
 TI_DOCUMENTO,
 TI_COMPETENZA_RESIDUO,
 CD_CDS,
 ESERCIZIO,
 PG_DOCUMENTO,
 IM_DOCUMENTO,
 TI_APPARTENENZA,
 TI_GESTIONE,
 CD_VOCE
;

   COMMENT ON TABLE "V_MANDATO_REVERSALE_VOCE"  IS 'Vista di estrazione della spaccatura del mandato/reversale sui capitoli finanziari collegati (no gestione rotti)';
