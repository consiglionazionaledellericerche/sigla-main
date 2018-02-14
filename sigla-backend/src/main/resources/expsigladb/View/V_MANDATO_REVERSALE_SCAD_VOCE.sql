--------------------------------------------------------
--  DDL for View V_MANDATO_REVERSALE_SCAD_VOCE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_MANDATO_REVERSALE_SCAD_VOCE" ("TI_DOCUMENTO", "TI_COMPETENZA_RESIDUO", "CD_CDS", "ESERCIZIO", "PG_DOCUMENTO", "TI_APPARTENENZA", "TI_GESTIONE", "CD_VOCE", "CD_CENTRO_RESPONSABILITA", "CD_LINEA_ATTIVITA", "ESERCIZIO_ORIGINALE", "CD_TIPO_DOCUMENTO_CONT", "IM_VOCE") AS 
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
 TI_APPARTENENZA,
 TI_GESTIONE,
 CD_VOCE,
 CD_CENTRO_RESPONSABILITA,
 CD_LINEA_ATTIVITA,
 ESERCIZIO_ORIGINALE,
 CD_TIPO_DOCUMENTO_CONT,
 IM_VOCE
from (
 select
	   'M' ti_documento,
	   man.TI_COMPETENZA_RESIDUO,
	   manr.CD_CDS,
	   manr.ESERCIZIO,
	   manr.pg_mandato pg_documento,
	   obbsv.TI_APPARTENENZA,
	   obbsv.TI_GESTIONE,
	   obbsv.CD_VOCE,
           obbsv.CD_CENTRO_RESPONSABILITA,
           obbsv.CD_LINEA_ATTIVITA,
           obb.ESERCIZIO_ORIGINALE,
           obb.CD_TIPO_DOCUMENTO_CONT,
           Decode(NVL(obbs.IM_SCADENZA,0),0,0,(obbsv.IM_VOCE/obbs.IM_SCADENZA )*manr.IM_MANDATO_RIGA) IM_VOCE
--           obbsv.IM_VOCE
FROM	   MANDATO man,
	   MANDATO_RIGA manr,
	   OBBLIGAZIONE obb,
	   OBBLIGAZIONE_SCADENZARIO obbs,
	   OBBLIGAZIONE_SCAD_VOCE obbsv
Where  manr.cd_cds 			 = man.cd_cds
AND    manr.ESERCIZIO 		  	 = man.ESERCIZIO
AND    manr.pg_mandato 		  	 = man.pg_mandato
AND    obb.cd_cds 			 = manr.cd_cds
AND    obb.ESERCIZIO 		  	 = manr.ESERCIZIO
AND    obb.ESERCIZIO_ORIGINALE	  	 = manr.ESERCIZIO_ORI_OBBLIGAZIONE
AND    obb.PG_OBBLIGAZIONE	  	 = manr.PG_OBBLIGAZIONE
And    obbs.cd_cds 			 = manr.cd_cds
AND    obbs.ESERCIZIO 		  	 = manr.ESERCIZIO
AND    obbs.ESERCIZIO_ORIGINALE	  	 = manr.ESERCIZIO_ORI_OBBLIGAZIONE
AND    obbs.PG_OBBLIGAZIONE	  	 = manr.PG_OBBLIGAZIONE
AND    obbs.PG_OBBLIGAZIONE_SCADENZARIO  = manr.PG_OBBLIGAZIONE_SCADENZARIO
AND    obbsv.CD_CDS			 = obbs.CD_CDS
AND    obbsv.ESERCIZIO			 = obbs.ESERCIZIO
AND    obbsv.ESERCIZIO_ORIGINALE	 = obbs.ESERCIZIO_ORIGINALE
And    obbsv.PG_OBBLIGAZIONE		 = obbs.PG_OBBLIGAZIONE
AND    obbsv.PG_OBBLIGAZIONE_SCADENZARIO = obbs.PG_OBBLIGAZIONE_SCADENZARIO
Union all
 select
	   'R' ti_documento,
	   rev.TI_COMPETENZA_RESIDUO,
	   rev.CD_CDS,
	   rev.ESERCIZIO,
	   rev.pg_reversale pg_documento,
	   acc.TI_APPARTENENZA,
	   acc.TI_GESTIONE,
	   acc.CD_VOCE,
           accsv.CD_CENTRO_RESPONSABILITA,
           accsv.CD_LINEA_ATTIVITA,
           acc.ESERCIZIO_ORIGINALE,
           acc.CD_TIPO_DOCUMENTO_CONT,
           DECODE(NVL(accs.IM_SCADENZA,0),0,0,(accsv.IM_VOCE/accs.IM_SCADENZA )*REVR.IM_REVERSALE_RIGA) IM_VOCE
           --accsv.IM_VOCE
From
	   REVERSALE rev,
	   REVERSALE_RIGA revr,
	   ACCERTAMENTO acc,
	   ACCERTAMENTO_SCADENZARIO accs,
	   ACCERTAMENTO_SCAD_VOCE accsv
WHERE  revr.cd_cds 			             = rev.cd_cds
AND    revr.ESERCIZIO 		  		     = rev.ESERCIZIO
AND    revr.pg_reversale 		  	     = rev.pg_reversale
AND    accs.CD_CDS				     = revr.CD_CDS
AND    accs.ESERCIZIO				     = revr.ESERCIZIO
AND    accs.ESERCIZIO_ORIGINALE	  		     = revr.ESERCIZIO_ORI_ACCERTAMENTO
And    accs.PG_accertamento			     = revr.PG_accertamento
And    accs.PG_accertamento_scadenzario		     = revr.PG_accertamento_scadenzario
AND    accs.CD_CDS				     = acc.CD_CDS
AND    accs.ESERCIZIO				     = acc.ESERCIZIO
AND    accs.ESERCIZIO_ORIGINALE	  		     = acc.ESERCIZIO_ORIGINALE
And    accs.PG_accertamento			     = acc.PG_accertamento
AND    accsv.CD_CDS				     = accs.CD_CDS
AND    accsv.ESERCIZIO				     = accs.ESERCIZIO
AND    accsv.ESERCIZIO_ORIGINALE	  	     = accs.ESERCIZIO_ORIGINALE
And    accsv.PG_accertamento			     = accs.PG_accertamento
And    accsv.PG_accertamento_scadenzario	     = accs.PG_accertamento_scadenzario);
