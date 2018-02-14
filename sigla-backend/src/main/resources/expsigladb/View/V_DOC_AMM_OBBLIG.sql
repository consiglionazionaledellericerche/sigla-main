--------------------------------------------------------
--  DDL for View V_DOC_AMM_OBBLIG
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_DOC_AMM_OBBLIG" ("CD_CDS_DOC_AMM", "CD_UO_DOC_AMM", "ESERCIZIO_DOC_AMM", "PG_DOC_AMM", "PROGRESSIVO_RIGA", "CD_TIPO_DOCUMENTO_AMM", "CD_CDS_OBBLIG", "ESERCIZIO_OBBLIG", "ESERCIZIO_ORI_OBBLIG", "PG_OBBLIG", "PG_OBBLIGAZIONE_SCADENZARIO", "CD_VOCE", "IM_VOCE") AS 
  SELECT --ANTICIPO
--
-- Date: 18/07/2006
-- Version: 1.1
--
-- Vista di estrazione delle obbligazioni e dei capitoli legati ai documenti amministrativi
--    quali ANTICIPO, COMPENSO, MISSIONE, FATTURA_PASSIVA, DOCUMENTO_GENERICO, con valorizzazioni a fini FONDO_ECONOMALE
--
-- History:
--
-- Date: 28/02/2003
-- Version: 1.0
-- Creazione
--
-- Date: 18/07/2006
-- Version: 1.1
-- Gestione Impegni/Accertamenti Residui:
-- aggiornata la funzione per tener conto anche del campo Esercizio Originale Impegno/Accertamento
--
--
	ant.cd_cds,
	ant.cd_unita_organizzativa,
	ant.esercizio,
	ant.pg_anticipo,
	0,
	'ANTICIPO',
	ant.cd_cds_obbligazione,
	ant.esercizio_obbligazione,
	ant.esercizio_ori_obbligazione,
	ant.pg_obbligazione,
	ant.pg_obbligazione_scadenzario,
	obb_scad.CD_VOCE,
	obb_scad.IM_VOCE
FROM   ANTICIPO ant, OBBLIGAZIONE_SCAD_VOCE obb_scad
WHERE ant.CD_CDS_OBBLIGAZIONE    = obb_scad.CD_CDS(+)
and   ant.ESERCIZIO_OBBLIGAZIONE = obb_scad.ESERCIZIO(+)
and   ant.ESERCIZIO_ORI_OBBLIGAZIONE = obb_scad.ESERCIZIO_ORIGINALE(+)
and   ant.PG_OBBLIGAZIONE 		 = obb_scad.PG_OBBLIGAZIONE(+)
and   ant.PG_OBBLIGAZIONE_SCADENZARIO   = obb_scad.PG_OBBLIGAZIONE_SCADENZARIO(+)
union all
SELECT --MISSIONE
	mis.cd_cds,
	mis.cd_unita_organizzativa,
	mis.esercizio,
	mis.pg_missione,
	0,
	'MISSIONE',
	mis.cd_cds_obbligazione,
	mis.esercizio_obbligazione,
	mis.esercizio_ori_obbligazione,
	mis.pg_obbligazione,
	mis.pg_obbligazione_scadenzario,
	obb_scad.CD_VOCE,
	obb_scad.IM_VOCE
FROM   MISSIONE mis, OBBLIGAZIONE_SCAD_VOCE obb_scad
WHERE mis.CD_CDS_OBBLIGAZIONE 	 = obb_scad.CD_CDS(+)
and   mis.ESERCIZIO_OBBLIGAZIONE = obb_scad.ESERCIZIO(+)
and   mis.ESERCIZIO_ORI_OBBLIGAZIONE = obb_scad.ESERCIZIO_ORIGINALE(+)
and   mis.PG_OBBLIGAZIONE 		 = obb_scad.PG_OBBLIGAZIONE(+)
and   mis.PG_OBBLIGAZIONE_SCADENZARIO   = obb_scad.PG_OBBLIGAZIONE_SCADENZARIO(+)
union all
SELECT --COMPENSO
	comp.cd_cds,
	comp.cd_unita_organizzativa,
	comp.esercizio,
	comp.PG_COMPENSO,
	0,
	'COMPENSO',
	comp.cd_cds_obbligazione,
	comp.esercizio_obbligazione,
	comp.esercizio_ori_obbligazione,
	comp.pg_obbligazione,
	comp.pg_obbligazione_scadenzario,
	obb_scad.CD_VOCE,
	obb_scad.IM_VOCE
FROM   COMPENSO comp, OBBLIGAZIONE_SCAD_VOCE obb_scad
WHERE comp.CD_CDS_OBBLIGAZIONE 	  = obb_scad.CD_CDS(+)
and   comp.ESERCIZIO_OBBLIGAZIONE = obb_scad.ESERCIZIO(+)
and   comp.ESERCIZIO_ORI_OBBLIGAZIONE = obb_scad.ESERCIZIO_ORIGINALE(+)
and   comp.PG_OBBLIGAZIONE 		  = obb_scad.PG_OBBLIGAZIONE(+)
and   comp.PG_OBBLIGAZIONE_SCADENZARIO   = obb_scad.PG_OBBLIGAZIONE_SCADENZARIO(+)
union all
SELECT --FATTURA_PASSIVA
	fat_p.cd_cds,
	fat_p.cd_unita_organizzativa,
	fat_p.esercizio,
	fat_p.PG_FATTURA_PASSIVA,
	riga.PROGRESSIVO_RIGA,
	'FATTURA_P',
	riga.cd_cds_obbligazione,
	riga.esercizio_obbligazione,
	riga.esercizio_ori_obbligazione,
	riga.pg_obbligazione,
	riga.pg_obbligazione_scadenzario,
	obb_scad.CD_VOCE,
	obb_scad.IM_VOCE
FROM FATTURA_PASSIVA fat_p,
	 FATTURA_PASSIVA_RIGA riga,
	 OBBLIGAZIONE_SCAD_VOCE obb_scad
WHERE fat_p.CD_CDS 				 = riga.CD_CDS
and fat_p.CD_UNITA_ORGANIZZATIVA = riga.CD_UNITA_ORGANIZZATIVA
and fat_p.ESERCIZIO 			 = riga.ESERCIZIO
and fat_p.PG_FATTURA_PASSIVA 	 = riga.PG_FATTURA_PASSIVA
and riga.CD_CDS_OBBLIGAZIONE 	 = obb_scad.CD_CDS(+)
and riga.ESERCIZIO_OBBLIGAZIONE  = obb_scad.ESERCIZIO(+)
and riga.ESERCIZIO_ORI_OBBLIGAZIONE = obb_scad.ESERCIZIO_ORIGINALE(+)
and riga.PG_OBBLIGAZIONE 		 = obb_scad.PG_OBBLIGAZIONE(+)
and riga.PG_OBBLIGAZIONE_SCADENZARIO = obb_scad.PG_OBBLIGAZIONE_SCADENZARIO(+)
union all
SELECT --DOCUMENTO_GENERICO
	gen_s.cd_cds,
	gen_s.cd_unita_organizzativa,
	gen_s.esercizio,
	gen_s.PG_DOCUMENTO_GENERICO,
	riga.PROGRESSIVO_RIGA,
	gen_s.CD_TIPO_DOCUMENTO_AMM,
	riga.cd_cds_obbligazione,
	riga.esercizio_obbligazione,
	riga.esercizio_ori_obbligazione,
	riga.pg_obbligazione,
	riga.pg_obbligazione_scadenzario,
	obb_scad.CD_VOCE,
	obb_scad.IM_VOCE
FROM DOCUMENTO_GENERICO gen_s,
	 DOCUMENTO_GENERICO_RIGA riga,
	 OBBLIGAZIONE_SCAD_VOCE obb_scad
WHERE gen_s.CD_CDS 			     = riga.CD_CDS
and gen_s.CD_UNITA_ORGANIZZATIVA = riga.CD_UNITA_ORGANIZZATIVA
and gen_s.ESERCIZIO 			 = riga.ESERCIZIO
and gen_s.PG_DOCUMENTO_GENERICO  = riga.PG_DOCUMENTO_GENERICO
and gen_s.CD_TIPO_DOCUMENTO_AMM  = riga.CD_TIPO_DOCUMENTO_AMM
and riga.CD_CDS_OBBLIGAZIONE 	 = obb_scad.CD_CDS(+)
and riga.ESERCIZIO_OBBLIGAZIONE  = obb_scad.ESERCIZIO(+)
and riga.ESERCIZIO_ORI_OBBLIGAZIONE = obb_scad.ESERCIZIO_ORIGINALE(+)
and riga.PG_OBBLIGAZIONE 		 = obb_scad.PG_OBBLIGAZIONE(+)
and riga.PG_OBBLIGAZIONE_SCADENZARIO = obb_scad.PG_OBBLIGAZIONE_SCADENZARIO(+);

   COMMENT ON TABLE "V_DOC_AMM_OBBLIG"  IS 'Vista di estrazione delle obbligazioni e dei capitoli legati ai documenti amministrativi
quali ANTICIPO, COMPENSO, MISSIONE, FATTURA_PASSIVA, DOCUMENTO_GENERICO, con valorizzazioni a fini FONDO_ECONOMALE';
