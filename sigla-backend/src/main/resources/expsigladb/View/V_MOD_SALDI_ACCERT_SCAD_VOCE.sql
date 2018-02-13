--------------------------------------------------------
--  DDL for View V_MOD_SALDI_ACCERT_SCAD_VOCE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_MOD_SALDI_ACCERT_SCAD_VOCE" ("PG_OLD", "CD_CDS", "ESERCIZIO", "ESERCIZIO_ORIGINALE", "PG_ACCERTAMENTO", "PG_ACCERTAMENTO_SCADENZARIO", "TI_APPARTENENZA", "TI_GESTIONE", "CD_VOCE", "CD_CENTRO_RESPONSABILITA", "CD_LINEA_ATTIVITA", "CD_TIPO_DOCUMENTO_CONT", "IM_DELTA_VOCE", "IM_DELTA_REV_VOCE", "IM_DELTA_INC_VOCE", "PROGRESSIVO") AS 
  Select
--
-- Date: 19/07/2006
-- Version: 1.2
--
-- History:
--
-- Date: 22/03/2005
-- Version: 1.0
-- Creazione
--
-- Date: 12/01/2006
-- Version: 1.1
-- Gestione Residui - Aggiunto il campo ESERCIZIO_ORIGINALE e CD_TIPO_DOCUMENTO_CONT
--
-- Date: 19/07/2006
-- Version: 1.2
-- Gestione Impegni/Accertamenti Residui:
-- gestito il nuovo campo ESERCIZIO_ORIGINALE
--
-- Body:
--
PG_OLD, CD_CDS, ESERCIZIO, ESERCIZIO_ORIGINALE, PG_ACCERTAMENTO, Max(PG_ACCERTAMENTO_SCADENZARIO),
 TI_APPARTENENZA, TI_GESTIONE, CD_VOCE, CD_CENTRO_RESPONSABILITA, CD_LINEA_ATTIVITA, CD_TIPO_DOCUMENTO_CONT,
 Sum(IM_DELTA_VOCE), Sum(IM_DELTA_REV_VOCE), Sum(IM_DELTA_INC_VOCE), PROGRESSIVO
 From (
Select
  a.pg_storico_ PG_OLD,
  a.cd_cds,
  a.esercizio,
  a.esercizio_originale,
  a.pg_accertamento,
  a.pg_accertamento_scadenzario,
  s.ti_appartenenza,
  s.ti_gestione,
  s.cd_voce,
  a.cd_centro_responsabilita,
  a.cd_linea_attivita,
  s.cd_tipo_documento_cont,
  0 -a.im_voce im_delta_voce,
  0 IM_DELTA_REV_VOCE,
  0 IM_DELTA_INC_VOCE,
  1 PROGRESSIVO
 From accertamento_s s, accertamento_scad_voce_s a
 Where s.cd_cds = a.cd_cds
   And s.esercizio = a.esercizio
   And s.esercizio_originale = a.esercizio_originale
   And s.pg_accertamento = a.pg_accertamento
   And s.pg_storico_ = a.pg_storico_
Union All
 Select
  s.pg_storico_,
  a.cd_cds,
  a.esercizio,
  a.esercizio_originale,
  a.pg_accertamento,
  a.pg_accertamento_scadenzario,
  acc.ti_appartenenza,
  acc.ti_gestione,
  acc.cd_voce,
  a.cd_centro_responsabilita,
  a.cd_linea_attivita,
  s.cd_tipo_documento_cont,
  a.im_voce im_delta_voce,
  0 im_delta_rev_voce,
  0 im_delta_pag_voce,
  1
 From accertamento_s s, accertamento acc, accertamento_scad_voce a
 Where s.cd_cds = a.cd_cds
   And s.esercizio = a.esercizio
   And s.esercizio_originale = a.esercizio_originale
   And s.pg_accertamento = a.pg_accertamento
   And s.cd_cds = acc.cd_cds
   And s.esercizio = acc.esercizio
   And s.esercizio_originale = acc.esercizio_originale
   And s.pg_accertamento = acc.pg_accertamento
Union All
 Select Distinct
  a.pg_storico_,
  a.cd_cds,
  a.esercizio,
  a.esercizio_originale,
  a.pg_accertamento,
  a.pg_accertamento_scadenzario,
  s.ti_appartenenza,
  s.ti_gestione,
  s.cd_voce,
  a.cd_centro_responsabilita,
  a.cd_linea_attivita,
  s.cd_tipo_documento_cont,
  0 im_delta_voce,
  Round(Decode(NVL(asca.IM_ASSOCIATO_DOC_CONTABILE,0),0,0,-(a.IM_VOCE/asca.IM_ASSOCIATO_DOC_CONTABILE )*b.IM_REVERSALE_RIGA),2) im_delta_rev_voce,
  0 - decode(c.stato,'P',b.im_reversale_riga,0) im_delta_inc_voce,
  1
 From accertamento_scad_voce_s a, accertamento_s s,
      accertamento_scadenzario_s asca, reversale_riga b, reversale c
 Where s.cd_cds = a.cd_cds
   And s.esercizio = a.esercizio
   And s.esercizio_originale = a.esercizio_originale
   And s.pg_accertamento = a.pg_accertamento
   And s.pg_storico_ = a.pg_storico_
   And b.cd_cds = a.cd_cds
   And b.esercizio = a.esercizio
   And b.esercizio_ori_accertamento = a.esercizio_originale
   And b.pg_accertamento = a.pg_accertamento
   And b.pg_accertamento_scadenzario = a.pg_accertamento_scadenzario
   And asca.cd_cds = a.cd_cds
   And asca.esercizio = a.esercizio
   And asca.esercizio_originale = a.esercizio_originale
   And asca.pg_accertamento = a.pg_accertamento
   And asca.pg_accertamento_scadenzario = a.pg_accertamento_scadenzario
   And asca.pg_storico_ = a.pg_storico_
   And c.cd_cds = b.cd_cds
   And c.esercizio = b.esercizio
   And c.pg_reversale = b.pg_reversale
   And c.stato <> 'A'
Union All
 Select Distinct
  s.pg_storico_,
  a.cd_cds,
  a.esercizio,
  a.esercizio_originale,
  a.pg_accertamento,
  a.pg_accertamento_scadenzario,
  acc.ti_appartenenza,
  acc.ti_gestione,
  acc.cd_voce,
  a.cd_centro_responsabilita,
  a.cd_linea_attivita,
  s.cd_tipo_documento_cont,
  0 im_delta_voce,
  Round(Decode(NVL(asca.IM_ASSOCIATO_DOC_CONTABILE,0),0,0,(a.IM_VOCE/asca.IM_ASSOCIATO_DOC_CONTABILE )*b.IM_REVERSALE_RIGA),2) im_delta_rev_voce,
  decode(c.stato,'P',b.im_reversale_riga,0) im_delta_inc_voce,
  1
 From accertamento_s s, accertamento acc, accertamento_scadenzario asca,
      accertamento_scad_voce a, reversale_riga b, reversale c
 Where s.cd_cds = a.cd_cds
   And s.esercizio = a.esercizio
   And s.esercizio_originale = a.esercizio_originale
   And s.pg_accertamento = a.pg_accertamento
   And s.cd_cds = acc.cd_cds
   And s.esercizio = acc.esercizio
   And s.esercizio_originale = acc.esercizio_originale
   And s.pg_accertamento = acc.pg_accertamento
   And b.cd_cds = a.cd_cds
   And b.esercizio = a.esercizio
   And b.esercizio_ori_accertamento = a.esercizio_originale
   And b.pg_accertamento = a.pg_accertamento
   And b.pg_accertamento_scadenzario = a.pg_accertamento_scadenzario
   And asca.cd_cds = a.cd_cds
   And asca.esercizio = a.esercizio
   And asca.esercizio_originale = a.esercizio_originale
   And asca.pg_accertamento = a.pg_accertamento
   And asca.pg_accertamento_scadenzario = a.pg_accertamento_scadenzario
   And c.cd_cds = b.cd_cds
   And c.esercizio = b.esercizio
   And c.pg_reversale = b.pg_reversale
   And c.stato <> 'A'
)
Group By PG_OLD, CD_CDS, ESERCIZIO, ESERCIZIO_ORIGINALE, PG_ACCERTAMENTO,
 TI_APPARTENENZA, TI_GESTIONE, CD_VOCE, CD_CENTRO_RESPONSABILITA, CD_LINEA_ATTIVITA,
 CD_TIPO_DOCUMENTO_CONT, PROGRESSIVO
;
