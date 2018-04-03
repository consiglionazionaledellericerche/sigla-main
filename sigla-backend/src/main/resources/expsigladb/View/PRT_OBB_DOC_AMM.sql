--------------------------------------------------------
--  DDL for View PRT_OBB_DOC_AMM
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "PRT_OBB_DOC_AMM" ("CD_CDS_OBBLIG", "ESERCIZIO_OBBLIG", "ESERCIZIO_RESIDUO", "PG_OBBLIG", "TIPO_DOC", "DS_OBBLIGAZIONE", "CD_ELEMENTO_VOCE", "DS_ELEMENTO_VOCE", "IM_OBBLIGAZIONE", "PG_OBBLIGAZIONE_SCADENZARIO", "DT_SCADENZA", "DS_SCADENZA", "IM_SCADENZA", "CD_TIPO_DOCUMENTO_AMM", "DS_TIPO_DOCUMENTO_AMM", "CD_CDS_DOC_AMM", "CD_UO_DOC_AMM", "ESERCIZIO_DOC_AMM", "PG_DOC_AMM", "PROGRESSIVO_RIGA", "TI_ISTITUZ_COMMERC", "IMPONIBILE", "IVA", "TOT_DOCUMENTO") AS 
  SELECT
-- Vista di estrazione delle obbligazioni e dei capitoli legati ai documenti amministrativi
--    quali ANTICIPO, COMPENSO, MISSIONE, FATTURA_PASSIVA, DOCUMENTO_GENERICO, con valorizzazioni a fini FONDO_ECONOMALE
--
-- Date: 18/07/2006
-- Version: 1.1
--
-- History:
--
-- Date: 01/01/2006
-- Version: 1.0
-- Creazione
--
-- Date: 18/07/2006
-- Version: 1.1
-- Gestione Impegni/Accertamenti Residui:
-- aggiornata la funzione per tener conto anche del campo Esercizio Originale Impegno/Accertamento
--
-- Body:
--
	ant.cd_cds_obbligazione,
	ant.esercizio_obbligazione,
	ant.esercizio_ori_obbligazione,
	ant.pg_obbligazione,
	OBB.CD_TIPO_DOCUMENTO_CONT,
	OBB.DS_OBBLIGAZIONE,
        OBB.CD_ELEMENTO_VOCE,
        ev.ds_elemento_voce,
        OBB.IM_OBBLIGAZIONE,
	ant.pg_obbligazione_scadenzario,
	OBB_SCAD.DT_SCADENZA,
	OBB_SCAD.DS_SCADENZA,
	OBB_SCAD.IM_SCADENZA,
	'ANTICIPO' CD_TIPO_DOCUMENTO_AMM,
	ds_tipo_documento_amm,
	ant.cd_cds,
	ant.cd_unita_organizzativa,
	ant.ESERCIZIO,
	ant.pg_anticipo PG_DOC_AMM,
	0,
	'I', -- ANTICIPO SOLO ISTITUZIONALE
	ANT.IM_ANTICIPO,
	0,
        ANT.IM_ANTICIPO
From   ANTICIPO ant, OBBLIGAZIONE OBB, OBBLIGAZIONE_SCADENZARIO OBB_SCAD, tipo_documento_amm tdoc, elemento_voce ev
WHERE ant.CD_CDS_OBBLIGAZIONE    = obb_scad.CD_CDS
AND   ant.ESERCIZIO_OBBLIGAZIONE = obb_scad.ESERCIZIO
AND   ant.ESERCIZIO_ORI_OBBLIGAZIONE = obb_scad.ESERCIZIO_ORIGINALE
AND   ant.PG_OBBLIGAZIONE 		 = obb_scad.PG_OBBLIGAZIONE
AND   ant.PG_OBBLIGAZIONE_SCADENZARIO   = obb_scad.PG_OBBLIGAZIONE_SCADENZARIO
AND   OBB_SCAD.CD_CDS           = OBB.CD_CDS
AND   OBB_SCAD.ESERCIZIO        = OBB.ESERCIZIO
AND   OBB_SCAD.ESERCIZIO_ORIGINALE = OBB.ESERCIZIO_ORIGINALE
AND   OBB_SCAD.PG_OBBLIGAZIONE  = OBB.PG_OBBLIGAZIONE
and   tdoc.CD_TIPO_DOCUMENTO_AMM = 'ANTICIPO'
and   ev.esercizio = OBB.ESERCIZIO
and   ev.cd_elemento_voce = OBB.CD_ELEMENTO_VOCE
and   ev.ti_appartenenza = OBB.ti_appartenenza
and   ev.ti_gestione = OBB.ti_gestione
UNION ALL
SELECT --MISSIONE
	MIS.cd_cds_obbligazione,
	MIS.esercizio_obbligazione,
	MIS.esercizio_ori_obbligazione,
	MIS.pg_obbligazione,
	OBB.CD_TIPO_DOCUMENTO_CONT,
	OBB.DS_OBBLIGAZIONE,
        OBB.CD_ELEMENTO_VOCE,
        ev.ds_elemento_voce,
        OBB.IM_OBBLIGAZIONE,
	MIS.pg_obbligazione_scadenzario,
	OBB_SCAD.DT_SCADENZA,
	OBB_SCAD.DS_SCADENZA,
	OBB_SCAD.IM_SCADENZA,
	'MISSIONE' CD_TIPO_DOCUMENTO_AMM,
	ds_tipo_documento_amm,
	mis.cd_cds,
	mis.cd_unita_organizzativa,
	mis.ESERCIZIO,
	mis.pg_missione PG_DOC_AMM,
	0,
	TI_ISTITUZ_COMMERC,
	MIS.IM_TOTALE_MISSIONE,
	0,
        MIS.IM_TOTALE_MISSIONE
From   MISSIONE mis, OBBLIGAZIONE_SCADENZARIO obb_scad, OBBLIGAZIONE OBB, tipo_documento_amm tdoc, elemento_voce ev
WHERE mis.CD_CDS_OBBLIGAZIONE 	 = obb_scad.CD_CDS
AND   mis.ESERCIZIO_OBBLIGAZIONE = obb_scad.ESERCIZIO
AND   mis.ESERCIZIO_ORI_OBBLIGAZIONE = obb_scad.ESERCIZIO_ORIGINALE
AND   mis.PG_OBBLIGAZIONE 		 = obb_scad.PG_OBBLIGAZIONE
AND   mis.PG_OBBLIGAZIONE_SCADENZARIO   = obb_scad.PG_OBBLIGAZIONE_SCADENZARIO
AND   OBB_SCAD.CD_CDS           = OBB.CD_CDS
AND   OBB_SCAD.ESERCIZIO        = OBB.ESERCIZIO
AND   OBB_SCAD.ESERCIZIO_ORIGINALE = OBB.ESERCIZIO_ORIGINALE
AND   OBB_SCAD.PG_OBBLIGAZIONE  = OBB.PG_OBBLIGAZIONE
and   tdoc.CD_TIPO_DOCUMENTO_AMM = 'MISSIONE'
and   ev.esercizio = OBB.ESERCIZIO
and   ev.cd_elemento_voce = OBB.CD_ELEMENTO_VOCE
and   ev.ti_appartenenza = OBB.ti_appartenenza
and   ev.ti_gestione = OBB.ti_gestione
UNION ALL
SELECT --COMPENSO
	RIGA.cd_cds_obbligazione,
	RIGA.esercizio_obbligazione,
	RIGA.esercizio_ori_obbligazione,
	RIGA.pg_obbligazione,
	OBB.CD_TIPO_DOCUMENTO_CONT,
	OBB.DS_OBBLIGAZIONE,
        OBB.CD_ELEMENTO_VOCE,
        ev.ds_elemento_voce,
        OBB.IM_OBBLIGAZIONE,
	RIGA.pg_obbligazione_scadenzario,
	OBB_SCAD.DT_SCADENZA,
	OBB_SCAD.DS_SCADENZA,
	OBB_SCAD.IM_SCADENZA,
	'COMPENSO' CD_TIPO_DOCUMENTO_AMM,
	ds_tipo_documento_amm,
	comp.cd_cds,
	comp.cd_unita_organizzativa,
	comp.ESERCIZIO,
	comp.PG_COMPENSO PG_DOC_AMM,
	0,
	TI_ISTITUZ_COMMERC,
	RIGA.IM_TOTALE_RIGA_COMPENSO,
	0,
	RIGA.IM_TOTALE_RIGA_COMPENSO
FROM   COMPENSO comp, COMPENSO_RIGA RIGA, OBBLIGAZIONE_SCADENZARIO obb_scad, OBBLIGAZIONE OBB, tipo_documento_amm tdoc, elemento_voce ev
WHERE comp.CD_CDS = RIGA.CD_CDS
AND   comp.CD_UNITA_ORGANIZZATIVA = RIGA.CD_UNITA_ORGANIZZATIVA
AND   comp.ESERCIZIO = RIGA.ESERCIZIO
AND   comp.PG_COMPENSO = RIGA.PG_COMPENSO
AND   RIGA.CD_CDS_OBBLIGAZIONE 	  = obb_scad.CD_CDS
AND   RIGA.ESERCIZIO_OBBLIGAZIONE = obb_scad.ESERCIZIO
AND   RIGA.ESERCIZIO_ORI_OBBLIGAZIONE = obb_scad.ESERCIZIO_ORIGINALE
AND   RIGA.PG_OBBLIGAZIONE 		  = obb_scad.PG_OBBLIGAZIONE
AND   RIGA.PG_OBBLIGAZIONE_SCADENZARIO   = obb_scad.PG_OBBLIGAZIONE_SCADENZARIO
AND   OBB_SCAD.CD_CDS           = OBB.CD_CDS
AND   OBB_SCAD.ESERCIZIO        = OBB.ESERCIZIO
AND   OBB_SCAD.ESERCIZIO_ORIGINALE = OBB.ESERCIZIO_ORIGINALE
AND   OBB_SCAD.PG_OBBLIGAZIONE  = OBB.PG_OBBLIGAZIONE
and   tdoc.CD_TIPO_DOCUMENTO_AMM = 'COMPENSO'
and   ev.esercizio = OBB.ESERCIZIO
and   ev.cd_elemento_voce = OBB.CD_ELEMENTO_VOCE
and   ev.ti_appartenenza = OBB.ti_appartenenza
and   ev.ti_gestione = OBB.ti_gestione
UNION ALL
SELECT --FATTURA_PASSIVA
	RIGA.cd_cds_obbligazione,
	RIGA.esercizio_obbligazione,
	RIGA.esercizio_ori_obbligazione,
	RIGA.pg_obbligazione,
	OBB.CD_TIPO_DOCUMENTO_CONT,
	OBB.DS_OBBLIGAZIONE,
        OBB.CD_ELEMENTO_VOCE,
        ev.ds_elemento_voce,
        OBB.IM_OBBLIGAZIONE,
	RIGA.pg_obbligazione_scadenzario,
	OBB_SCAD.DT_SCADENZA,
	OBB_SCAD.DS_SCADENZA,
	OBB_SCAD.IM_SCADENZA,
	'FATTURA_P' CD_TIPO_DOCUMENTO_AMM,
	ds_tipo_documento_amm,
	fat_p.cd_cds,
	fat_p.cd_unita_organizzativa,
	fat_p.ESERCIZIO,
	fat_p.PG_FATTURA_PASSIVA PG_DOC_AMM,
	riga.PROGRESSIVO_RIGA,
	RIGA.TI_ISTITUZ_COMMERC,
	decode(fat_p.TI_FATTURA, 'C', -Nvl(IM_IMPONIBILE, 0), Nvl(IM_IMPONIBILE, 0)),
	decode(fat_p.TI_FATTURA, 'C', -Nvl(IM_IVA, 0), Nvl(IM_IVA, 0)),
	decode(fat_p.TI_FATTURA, 'C', -(Nvl(IM_IMPONIBILE, 0) + Nvl(IM_IVA, 0)), Nvl(IM_IMPONIBILE, 0) + Nvl(IM_IVA, 0))
From FATTURA_PASSIVA fat_p, FATTURA_PASSIVA_RIGA riga, OBBLIGAZIONE_SCADENZARIO obb_scad, OBBLIGAZIONE OBB, tipo_documento_amm tdoc, elemento_voce ev
WHERE fat_p.CD_CDS 				 = riga.CD_CDS
AND fat_p.CD_UNITA_ORGANIZZATIVA = riga.CD_UNITA_ORGANIZZATIVA
AND fat_p.ESERCIZIO 		 = riga.ESERCIZIO
AND fat_p.PG_FATTURA_PASSIVA 	 = riga.PG_FATTURA_PASSIVA
AND riga.CD_CDS_OBBLIGAZIONE 	 = obb_scad.CD_CDS
AND riga.ESERCIZIO_OBBLIGAZIONE  = obb_scad.ESERCIZIO
AND riga.ESERCIZIO_ORI_OBBLIGAZIONE  = obb_scad.ESERCIZIO_ORIGINALE
AND riga.PG_OBBLIGAZIONE 	 = obb_scad.PG_OBBLIGAZIONE
AND riga.PG_OBBLIGAZIONE_SCADENZARIO = obb_scad.PG_OBBLIGAZIONE_SCADENZARIO
AND   OBB_SCAD.CD_CDS           = OBB.CD_CDS
AND   OBB_SCAD.ESERCIZIO        = OBB.ESERCIZIO
AND   OBB_SCAD.ESERCIZIO_ORIGINALE = OBB.ESERCIZIO_ORIGINALE
AND   OBB_SCAD.PG_OBBLIGAZIONE  = OBB.PG_OBBLIGAZIONE
and   tdoc.CD_TIPO_DOCUMENTO_AMM = 'FATTURA_P'
and   ev.esercizio = OBB.ESERCIZIO
and   ev.cd_elemento_voce = OBB.CD_ELEMENTO_VOCE
and   ev.ti_appartenenza = OBB.ti_appartenenza
and   ev.ti_gestione = OBB.ti_gestione
Union ALL
SELECT --DOCUMENTO_GENERICO
	RIGA.cd_cds_obbligazione,
	RIGA.esercizio_obbligazione,
	RIGA.esercizio_ori_obbligazione,
	RIGA.pg_obbligazione,
	OBB.CD_TIPO_DOCUMENTO_CONT,
	OBB.DS_OBBLIGAZIONE,
        OBB.CD_ELEMENTO_VOCE,
        ev.ds_elemento_voce,
        OBB.IM_OBBLIGAZIONE,
	RIGA.pg_obbligazione_scadenzario,
	OBB_SCAD.DT_SCADENZA,
	OBB_SCAD.DS_SCADENZA,
	OBB_SCAD.IM_SCADENZA,
	gen_s.CD_TIPO_DOCUMENTO_AMM CD_TIPO_DOCUMENTO_AMM,
	ds_tipo_documento_amm,
	gen_s.cd_cds,
	gen_s.cd_unita_organizzativa,
	gen_s.ESERCIZIO,
	gen_s.PG_DOCUMENTO_GENERICO PG_DOC_AMM,
	riga.PROGRESSIVO_RIGA,
	gen_s.TI_ISTITUZ_COMMERC,
	RIGA.IM_RIGA,
	0,
	RIGA.IM_RIGA
From DOCUMENTO_GENERICO gen_s, DOCUMENTO_GENERICO_RIGA riga, OBBLIGAZIONE_SCADENZARIO obb_scad, OBBLIGAZIONE OBB, tipo_documento_amm tdoc, elemento_voce ev
WHERE gen_s.CD_CDS               = riga.CD_CDS
AND gen_s.CD_UNITA_ORGANIZZATIVA = riga.CD_UNITA_ORGANIZZATIVA
AND gen_s.ESERCIZIO 		 = riga.ESERCIZIO
AND gen_s.PG_DOCUMENTO_GENERICO  = riga.PG_DOCUMENTO_GENERICO
AND gen_s.CD_TIPO_DOCUMENTO_AMM  = riga.CD_TIPO_DOCUMENTO_AMM
AND riga.CD_CDS_OBBLIGAZIONE 	 = obb_scad.CD_CDS
AND riga.ESERCIZIO_OBBLIGAZIONE  = obb_scad.ESERCIZIO
AND riga.ESERCIZIO_ORI_OBBLIGAZIONE  = obb_scad.ESERCIZIO_ORIGINALE
AND riga.PG_OBBLIGAZIONE 	 = obb_scad.PG_OBBLIGAZIONE
AND riga.PG_OBBLIGAZIONE_SCADENZARIO = obb_scad.PG_OBBLIGAZIONE_SCADENZARIO
AND   OBB_SCAD.CD_CDS           = OBB.CD_CDS
AND   OBB_SCAD.ESERCIZIO        = OBB.ESERCIZIO
AND   OBB_SCAD.ESERCIZIO_ORIGINALE = OBB.ESERCIZIO_ORIGINALE
AND   OBB_SCAD.PG_OBBLIGAZIONE  = OBB.PG_OBBLIGAZIONE
and   tdoc.CD_TIPO_DOCUMENTO_AMM = gen_s.CD_TIPO_DOCUMENTO_AMM
and   ev.esercizio = OBB.ESERCIZIO
and   ev.cd_elemento_voce = OBB.CD_ELEMENTO_VOCE
and   ev.ti_appartenenza = OBB.ti_appartenenza
and   ev.ti_gestione = OBB.ti_gestione;
