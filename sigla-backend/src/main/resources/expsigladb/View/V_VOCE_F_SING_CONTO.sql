--------------------------------------------------------
--  DDL for View V_VOCE_F_SING_CONTO
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_VOCE_F_SING_CONTO" ("CD_CDS", "ESERCIZIO", "TI_APPARTENENZA", "TI_GESTIONE", "CD_VOCE", "TI_COMPETENZA_RESIDUO", "CD_CDS_PROPRIO", "DS_VOCE", "CD_NATURA", "CD_FUNZIONE", "CD_PARTE", "CD_CATEGORIA", "CD_UNITA_ORGANIZZATIVA", "CD_PROPRIO_VOCE", "CD_ELEMENTO_VOCE", "DS_ELEMENTO_VOCE", "FL_PARTITA_GIRO", "FL_VOCE_SAC") AS 
  Select Distinct
--
-- Date: 14/01/2004
-- Version: 1.0
--
-- Vista per la ricerca di capitoli per la stampa
-- della situazione singolo conto
--
-- History:
--
-- Date: 14/01/2004
-- Version: 1.0
-- Creazione
--
          Substr(vs.cd_cENTRO_RESPONSABILITA, 1, 3), vs.esercizio, vs.ti_appartenenza, vs.ti_gestione,
          vs.cd_voce, Decode(vs.esercizio, vs.esercizio_res, 'C', 'R'), vf.cd_cds, vf.ds_voce,
          vf.cd_natura, vf.cd_funzione, vf.cd_parte, vf.cd_categoria,
          vf.cd_unita_organizzativa, vf.cd_proprio_voce, ev.cd_elemento_voce,
          ev.ds_elemento_voce, ev.fl_partita_giro, ev.fl_voce_sac
     FROM voce_f_saldi_cdr_linea vs, voce_f vf, elemento_voce ev
    WHERE vf.esercizio = vs.esercizio
      AND vf.ti_appartenenza = vs.ti_appartenenza
      AND vf.ti_gestione = vs.ti_gestione
      AND vf.cd_voce = vs.cd_voce
      AND ev.esercizio = vf.esercizio
      AND ev.ti_appartenenza = vf.ti_appartenenza
      AND ev.ti_gestione = vf.ti_gestione
      AND ev.cd_elemento_voce = vf.cd_titolo_capitolo;

   COMMENT ON TABLE "V_VOCE_F_SING_CONTO"  IS 'Vista per la ricerca di capitoli per la stampa
della situazione singolo conto';
