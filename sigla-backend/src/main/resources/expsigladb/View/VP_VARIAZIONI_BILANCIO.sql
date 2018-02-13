--------------------------------------------------------
--  DDL for View VP_VARIAZIONI_BILANCIO
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VP_VARIAZIONI_BILANCIO" ("ESERCIZIO", "CD_CDS", "DS_CDS", "TI_APPARTENENZA", "PG_VARIAZIONE", "DS_VARIAZIONE", "DS_DELIBERA", "TI_VARIAZIONE", "STATO", "TI_GESTIONE", "CD_VOCE", "DS_VOCE", "IM_VARIAZIONE", "IM_ASSESTATO") AS 
  SELECT
--
-- Date: 18/03/2003
-- Version: 1.0
--
-- Vista di stampa delle variazioni di bilancio
-- A seconda dello stato della variazione, l'assestato viene calcolato
-- togliendo (stato definitivo 'D') o meno (stato provvisorio 'P') l'importo della variazione dall'assestato corrente
--
-- History:
--
-- Date: 18/03/2003
-- Version: 1.0
-- Creazione
--
-- Body:
--
            v.esercizio, v.cd_cds, c.ds_unita_organizzativa,
            v.ti_appartenenza, v.pg_variazione, v.ds_variazione,
            v.ds_delibera, v.ti_variazione, v.stato, vd.ti_gestione,
            vd.cd_voce, vf.ds_voce, vd.im_variazione,
              NVL (SUM (  vfs.im_stanz_iniziale_a1
                        + vfs.variazioni_piu
                        - vfs.variazioni_meno
                       ),
                   0
                  )
            - DECODE (v.stato, 'D', vd.im_variazione, 0)
       FROM var_bilancio v,
            var_bilancio_det vd,
            voce_f vf,
            voce_f_saldi_cdr_linea vfs,
            unita_organizzativa c
      WHERE c.cd_unita_organizzativa = v.cd_cds
        AND vd.cd_cds = v.cd_cds
        AND vd.esercizio = v.esercizio
        AND vd.ti_appartenenza = v.ti_appartenenza
        AND vd.pg_variazione = v.pg_variazione
        AND vf.esercizio = vd.esercizio
        AND vf.ti_appartenenza = vd.ti_appartenenza
        AND vf.ti_gestione = vd.ti_gestione
        AND vf.cd_voce = vd.cd_voce
        AND vfs.esercizio(+) = vd.esercizio
        AND cnrutl001.getcdsfromcdr (vfs.cd_centro_responsabilita(+)) = vd.cd_cds
        AND vfs.ti_appartenenza(+) = vd.ti_appartenenza
        AND vfs.ti_gestione(+) = vd.ti_gestione
        AND vfs.cd_voce(+) = vd.cd_voce
   GROUP BY v.esercizio,
            v.cd_cds,
            c.ds_unita_organizzativa,
            v.ti_appartenenza,
            v.pg_variazione,
            v.ds_variazione,
            v.ds_delibera,
            v.ti_variazione,
            v.stato,
            vd.ti_gestione,
            vd.cd_voce,
            vf.ds_voce,
            vd.im_variazione;

   COMMENT ON TABLE "VP_VARIAZIONI_BILANCIO"  IS 'Vista di stampa delle variazioni di bilancio
A seconda dello stato della variazione, l''assestato viene calcolato
togliendo (stato definitivo ''D'') o meno (stato provvisorio ''P'') l''importo della variazione dall''assestato corrente';
