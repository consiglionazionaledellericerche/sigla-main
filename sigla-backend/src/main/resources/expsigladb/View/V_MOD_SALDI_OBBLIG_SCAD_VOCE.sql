--------------------------------------------------------
--  DDL for View V_MOD_SALDI_OBBLIG_SCAD_VOCE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_MOD_SALDI_OBBLIG_SCAD_VOCE" ("PG_OLD", "CD_CDS", "ESERCIZIO", "ESERCIZIO_ORIGINALE", "PG_OBBLIGAZIONE", "PG_OBBLIGAZIONE_SCADENZARIO", "TI_APPARTENENZA", "TI_GESTIONE", "CD_VOCE", "CD_CENTRO_RESPONSABILITA", "CD_LINEA_ATTIVITA", "CD_TIPO_DOCUMENTO_CONT", "IM_DELTA_VOCE", "IM_DELTA_MAN_VOCE", "IM_DELTA_PAG_VOCE", "PROGRESSIVO") AS 
  Select PG_OLD, CD_CDS, ESERCIZIO, ESERCIZIO_ORIGINALE, PG_OBBLIGAZIONE, Max(PG_OBBLIGAZIONE_SCADENZARIO),
 TI_APPARTENENZA, TI_GESTIONE, CD_VOCE, CD_CENTRO_RESPONSABILITA, CD_LINEA_ATTIVITA, CD_TIPO_DOCUMENTO_CONT,
 Sum(IM_DELTA_VOCE), Sum(IM_DELTA_MAN_VOCE), Sum(IM_DELTA_PAG_VOCE), PROGRESSIVO
 From (
Select
--
-- Date: 18/07/2006
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
-- Date: 18/07/2006
-- Version: 1.2
-- Gestione Impegni/Accertamenti Residui:
-- gestito il nuovo campo ESERCIZIO_ORIGINALE
--
-- Body:
--
  a.pg_storico_ PG_OLD,
  a.cd_cds,
  a.esercizio,
  a.esercizio_originale,
  a.pg_obbligazione,
  a.pg_obbligazione_scadenzario,
  a.ti_appartenenza,
  a.ti_gestione,
  a.cd_voce,
  a.cd_centro_responsabilita,
  a.cd_linea_attivita,
  s.cd_tipo_documento_cont,
  0 -a.im_voce im_delta_voce,
  0 im_delta_man_voce,
  0 im_delta_pag_voce,
  1 PROGRESSIVO
 From obbligazione_s s, obbligazione_scad_voce_s a
 Where s.cd_cds = a.cd_cds
   And s.esercizio = a.esercizio
   And s.esercizio_originale = a.esercizio_originale
   And s.pg_obbligazione = a.pg_obbligazione
   And s.pg_storico_ = a.pg_storico_
Union All
 Select
  s.pg_storico_,
  a.cd_cds,
  a.esercizio,
  a.esercizio_originale,
  a.pg_obbligazione,
  a.pg_obbligazione_scadenzario,
  a.ti_appartenenza,
  a.ti_gestione,
  a.cd_voce,
  a.cd_centro_responsabilita,
  a.cd_linea_attivita,
  s.cd_tipo_documento_cont,
  a.im_voce im_delta_voce,
  0 im_delta_man_voce,
  0 im_delta_pag_voce,
  1
 From obbligazione_s s, obbligazione_scad_voce a
 Where s.cd_cds = a.cd_cds
   And s.esercizio = a.esercizio
   And s.esercizio_originale = a.esercizio_originale
   And s.pg_obbligazione = a.pg_obbligazione
Union All
 Select Distinct
  a.pg_storico_,
  a.cd_cds,
  a.esercizio,
  a.esercizio_originale,
  a.pg_obbligazione,
  a.pg_obbligazione_scadenzario,
  a.ti_appartenenza,
  a.ti_gestione,
  a.cd_voce,
  a.cd_centro_responsabilita,
  a.cd_linea_attivita,
  s.cd_tipo_documento_cont,
  0 im_delta_voce,
  0-decode(u.cd_tipo_unita,'ENTE',b.im_mandato_riga,decode(s.fl_pgiro,'Y',b.im_mandato_riga,a.im_voce)) im_delta_man_voce,
  0-decode(c.stato,'P',decode(u.cd_tipo_unita,'ENTE',b.im_mandato_riga,decode(s.fl_pgiro,'Y',b.im_mandato_riga,a.im_voce)),0) im_delta_pag_voce,
  1
 From obbligazione_scad_voce_s a, unita_organizzativa u, obbligazione_s s,
      obbligazione_scadenzario_s os, mandato_riga b, mandato c
 Where s.cd_cds = a.cd_cds
   And s.esercizio = a.esercizio
   And s.esercizio_originale = a.esercizio_originale
   And s.pg_obbligazione = a.pg_obbligazione
   And s.pg_storico_ = a.pg_storico_
   And b.cd_cds = a.cd_cds
   And b.esercizio = a.esercizio
   And b.esercizio_ori_obbligazione = a.esercizio_originale
   And b.pg_obbligazione = a.pg_obbligazione
   And b.pg_obbligazione_scadenzario = a.pg_obbligazione_scadenzario
   And os.cd_cds = a.cd_cds
   And os.esercizio = a.esercizio
   And os.esercizio_originale = a.esercizio_originale
   And os.pg_obbligazione = a.pg_obbligazione
   And os.pg_obbligazione_scadenzario = a.pg_obbligazione_scadenzario
   And os.pg_storico_ = a.pg_storico_
   And c.cd_cds = b.cd_cds
   And c.esercizio = b.esercizio
   And c.pg_mandato = b.pg_mandato
   And u.cd_unita_organizzativa = a.cd_cds
   And u.fl_cds = 'Y'
   And c.stato <> 'A'
Union All
 Select Distinct
  s.pg_storico_,
  a.cd_cds,
  a.esercizio,
  a.esercizio_originale,
  a.pg_obbligazione,
  a.pg_obbligazione_scadenzario,
  a.ti_appartenenza,
  a.ti_gestione,
  a.cd_voce,
  a.cd_centro_responsabilita,
  a.cd_linea_attivita,
  s.cd_tipo_documento_cont,
  0 im_delta_voce,
  decode(u.cd_tipo_unita,'ENTE',b.im_mandato_riga,decode(s.fl_pgiro,'Y',b.im_mandato_riga,a.im_voce)) im_delta_man_voce,
  decode(c.stato,'P',decode(u.cd_tipo_unita,'ENTE',b.im_mandato_riga,decode(s.fl_pgiro,'Y',b.im_mandato_riga,a.im_voce)),0) im_delta_pag_voce,
  1
 From unita_organizzativa u, obbligazione_s s, obbligazione_scadenzario os,
      obbligazione_scad_voce a, mandato_riga b, mandato c
 Where s.cd_cds = a.cd_cds
   And s.esercizio = a.esercizio
   And s.esercizio_originale = a.esercizio_originale
   And s.pg_obbligazione = a.pg_obbligazione
   And b.cd_cds = a.cd_cds
   And b.esercizio = a.esercizio
   And b.esercizio_ori_obbligazione = a.esercizio_originale
   And b.pg_obbligazione = a.pg_obbligazione
   And b.pg_obbligazione_scadenzario = a.pg_obbligazione_scadenzario
   And os.cd_cds = a.cd_cds
   And os.esercizio = a.esercizio
   And os.esercizio_originale = a.esercizio_originale
   And os.pg_obbligazione = a.pg_obbligazione
   And os.pg_obbligazione_scadenzario = a.pg_obbligazione_scadenzario
   And c.cd_cds = b.cd_cds
   And c.esercizio = b.esercizio
   And c.pg_mandato = b.pg_mandato
   And u.cd_unita_organizzativa = a.cd_cds
   And u.fl_cds = 'Y'
   And c.stato <> 'A'   )
Group By PG_OLD, CD_CDS, ESERCIZIO, ESERCIZIO_ORIGINALE, PG_OBBLIGAZIONE,
 TI_APPARTENENZA, TI_GESTIONE, CD_VOCE, CD_CENTRO_RESPONSABILITA, CD_LINEA_ATTIVITA,
 CD_TIPO_DOCUMENTO_CONT, PROGRESSIVO
;
