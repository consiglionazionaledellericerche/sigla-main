--------------------------------------------------------
--  DDL for View V_SALDI_PIANO_ECONOM_PROGETTO
--------------------------------------------------------

CREATE OR REPLACE FORCE VIEW "V_SALDI_PIANO_ECONOM_PROGETTO" ("PG_PROGETTO", "ESERCIZIO", "CD_UNITA_PIANO", "CD_VOCE_PIANO", "TI_GESTIONE", "IMPORTO_FIN", "STANZIAMENTO_FIN", "VARIAPIU_FIN", "VARIAMENO_FIN", "TRASFPIU_FIN", "TRASFMENO_FIN", "IMPORTO_COFIN", "STANZIAMENTO_COFIN", "VARIAPIU_COFIN", "VARIAMENO_COFIN", "TRASFPIU_COFIN", "TRASFMENO_COFIN", "IMPACC_FIN", "IMPACC_COFIN", "MANRIS_FIN", "MANRIS_COFIN") AS 
  (SELECT   x.pg_progetto, x.esercizio, x.cd_unita_piano, x.cd_voce_piano,
             x.ti_gestione, 
             SUM (x.importo_fin) importo_fin,
             SUM (x.stanziamento_fin) stanziamento_fin,
             SUM (x.variapiu_fin) variapiu_fin,
             SUM (x.variameno_fin) variameno_fin,
             SUM (x.trasfpiu_fin) trasfpiu_fin,
             SUM (x.trasfmeno_fin) trasfmeno_fin,
             SUM (x.importo_cofin) importo_cofin,
             SUM (x.stanziamento_cofin) stanziamento_cofin,
             SUM (x.variapiu_cofin) variapiu_cofin,
             SUM (x.variameno_cofin) variameno_cofin,
             SUM (x.trasfpiu_cofin) trasfpiu_cofin,
             SUM (x.trasfmeno_cofin) trasfmeno_cofin,
             SUM (x.impacc_fin) impacc_fin,
             SUM (x.impacc_cofin) impacc_cofin,
             SUM (x.manris_fin) manris_fin,
             SUM (x.manris_cofin) manris_cofin
        FROM (SELECT a.pg_progetto, a.esercizio_piano esercizio,
                     a.cd_unita_organizzativa cd_unita_piano, a.cd_voce_piano,
                     'S' ti_gestione, NVL(a.im_spesa_finanziato, 0) importo_fin,
                     0 stanziamento_fin, 0 variapiu_fin, 0 variameno_fin,
                     0 trasfpiu_fin, 0 trasfmeno_fin,
                     NVL(a.im_spesa_cofinanziato, 0) importo_cofin,
                     0 stanziamento_cofin, 0 variapiu_cofin, 0 variameno_cofin, 
                     0 trasfpiu_cofin, 0 trasfmeno_cofin,
                     0 impacc_fin, 0 impacc_cofin, 0 manris_fin, 0 manris_cofin
                FROM progetto_piano_economico a
              UNION ALL
              SELECT a.pg_progetto, a.esercizio_piano, a.cd_unita_organizzativa,
                     a.cd_voce_piano, b.ti_gestione, 
                     0 importo_fin, 
                     b.stanziamento_fin, 
                     b.variapiu_fin, b.variameno_fin,
                     b.trasfpiu_fin, b.trasfmeno_fin,
                     0 importo_cofin,
                     b.stanziamento_cofin, 
                     b.variapiu_cofin, b.variameno_cofin, 
                     b.trasfpiu_cofin, b.trasfmeno_cofin,
                     b.impacc_fin, b.impacc_cofin,
                     b.manris_fin, b.manris_cofin
                FROM ass_progetto_piaeco_voce a,
                     V_SALDI_VOCE_PROGETTO b
               WHERE a.pg_progetto = b.pg_progetto
                 AND a.esercizio_piano = b.esercizio
                 AND a.esercizio_voce = b.esercizio
                 AND a.ti_appartenenza = b.ti_appartenenza
                 AND a.ti_gestione = b.ti_gestione
                 AND a.cd_elemento_voce = b.cd_elemento_voce) x
    GROUP BY x.pg_progetto,
             x.esercizio,
             x.cd_unita_piano,
             x.cd_voce_piano,
             x.ti_gestione);
