--------------------------------------------------------
--  DDL for View V_MAN_REV_VOCE_CDR_LINEA
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_MAN_REV_VOCE_CDR_LINEA" ("TI_DOCUMENTO", "TI_COMPETENZA_RESIDUO", "CD_CDS", "ESERCIZIO", "PG_DOCUMENTO", "IM_DOCUMENTO", "TI_APPARTENENZA", "TI_GESTIONE", "CD_VOCE", "CD_CENTRO_RESPONSABILITA", "CD_LINEA_ATTIVITA", "IM_CAPITOLO_PESATO") AS 
  select
--
-- Date: 18/07/2006
-- Version: 1.1
--
-- Vista di estrazione della spaccatura del mandato/reversale sui capitoli finanziari collegati (no gestione rotti)
--
-- History:
--
-- Date: 30/03/2005
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
 CD_CENTRO_RESPONSABILITA,
 CD_LINEA_ATTIVITA,
 sum(IM_RIGA_PESATO)
From (
 Select
   'M' ti_documento,
   man.TI_COMPETENZA_RESIDUO,
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
   obbsv.CD_CENTRO_RESPONSABILITA,
   obbsv.CD_LINEA_ATTIVITA,
   manr.IM_MANDATO_RIGA,
   obbs.IM_SCADENZA,
   Decode(NVL(obbs.IM_SCADENZA,0),0,0,(sum(obbsv.IM_VOCE)/obbs.IM_SCADENZA )*manr.IM_MANDATO_RIGA) IM_RIGA_PESATO
 From MANDATO man, MANDATO_RIGA manr, OBBLIGAZIONE_SCADENZARIO obbs, OBBLIGAZIONE_SCAD_VOCE obbsv
 Where manr.cd_cds = man.cd_cds
   And manr.ESERCIZIO = man.ESERCIZIO
   And manr.pg_mandato = man.pg_mandato
   And obbs.cd_cds = manr.cd_cds
   And obbs.ESERCIZIO = manr.ESERCIZIO
   And obbs.ESERCIZIO_ORIGINALE = manr.ESERCIZIO_ORI_OBBLIGAZIONE
   And obbs.PG_OBBLIGAZIONE = manr.PG_OBBLIGAZIONE
   And obbs.PG_OBBLIGAZIONE_SCADENZARIO = manr.PG_OBBLIGAZIONE_SCADENZARIO
   And obbsv.CD_CDS = obbs.CD_CDS
   And obbsv.ESERCIZIO = obbs.ESERCIZIO
   And obbsv.ESERCIZIO_ORIGINALE = obbs.ESERCIZIO_ORIGINALE
   And obbsv.PG_OBBLIGAZIONE = obbs.PG_OBBLIGAZIONE
   And obbsv.PG_OBBLIGAZIONE_SCADENZARIO = obbs.PG_OBBLIGAZIONE_SCADENZARIO
 Group By man.TI_COMPETENZA_RESIDUO,
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
          obbsv.CD_CENTRO_RESPONSABILITA,
          obbsv.CD_LINEA_ATTIVITA,
          manr.IM_MANDATO_RIGA,
          obbs.IM_SCADENZA
 Union All
 Select
   'R' ti_documento,
   rev.TI_COMPETENZA_RESIDUO,
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
   acc_scad_voce.CD_CENTRO_RESPONSABILITA,
   acc_scad_voce.CD_LINEA_ATTIVITA,
   REVr.IM_REVERSALE_RIGA,
   ACCs.IM_SCADENZA,
   Decode(NVL(ACCs.IM_SCADENZA,0),0,0,(sum(acc_scad_voce.IM_VOCE)/ACCs.IM_SCADENZA )*REVr.IM_REVERSALE_RIGA) IM_RIGA_PESATO
 From REVERSALE rev, REVERSALE_RIGA revr, ACCERTAMENTO_SCADENZARIO ACCS, ACCERTAMENTO acc, accertamento_scad_voce acc_scad_voce
 Where revr.cd_cds = rev.cd_cds
   And revr.ESERCIZIO = rev.ESERCIZIO
   And revr.pg_reversale = rev.pg_reversale
   And ACC.cd_cds = ACCS.cd_cds
   And ACC.ESERCIZIO = ACCS.ESERCIZIO
   And ACC.ESERCIZIO_ORIGINALE = ACCS.ESERCIZIO_ORIGINALE
   And ACC.PG_ACCERTAMENTO = ACCS.PG_ACCERTAMENTO
   And ACCS.cd_cds = REVr.cd_cds
   And ACCS.ESERCIZIO = REVr.ESERCIZIO
   And ACCS.ESERCIZIO_ORIGINALE = REVr.ESERCIZIO_ORI_ACCERTAMENTO
   And ACCS.PG_ACCERTAMENTO = REVr.PG_ACCERTAMENTO
   And ACCS.PG_ACCERTAMENTO_SCADENZARIO = REVr.PG_ACCERTAMENTO_SCADENZARIO
   And acc_scad_voce.CD_CDS =  ACCs.CD_CDS
   And acc_scad_voce.ESERCIZIO =  ACCs.ESERCIZIO
   And acc_scad_voce.ESERCIZIO_ORIGINALE = ACCs.ESERCIZIO_ORIGINALE
   And acc_scad_voce.PG_ACCERTAMENTO = ACCs.PG_ACCERTAMENTO
   And acc_scad_voce.PG_ACCERTAMENTO_SCADENZARIO =  ACCs.PG_ACCERTAMENTO_SCADENZARIO
GRoup By rev.TI_COMPETENZA_RESIDUO,
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
          acc_scad_voce.CD_CENTRO_RESPONSABILITA,
          acc_scad_voce.CD_LINEA_ATTIVITA,
          REVr.IM_REVERSALE_RIGA,
          ACCs.IM_SCADENZA
) group by
 TI_DOCUMENTO,
 TI_COMPETENZA_RESIDUO,
 CD_CDS,
 ESERCIZIO,
 PG_DOCUMENTO,
 IM_DOCUMENTO,
 TI_APPARTENENZA,
 TI_GESTIONE,
 CD_VOCE,
 CD_CENTRO_RESPONSABILITA,
 CD_LINEA_ATTIVITA;
