--------------------------------------------------------
--  DDL for View V_SALDI_VOCE_PROGETTO
--------------------------------------------------------

CREATE OR REPLACE FORCE VIEW "V_SALDI_VOCE_PROGETTO" ("PG_PROGETTO", "ESERCIZIO", "ESERCIZIO_VOCE", "TI_APPARTENENZA", "TI_GESTIONE", "CD_ELEMENTO_VOCE", "STANZIAMENTO_FIN", "VARIAPIU_FIN", "VARIAMENO_FIN", "TRASFPIU_FIN", "TRASFMENO_FIN", "STANZIAMENTO_COFIN", "VARIAPIU_COFIN", "VARIAMENO_COFIN", "TRASFPIU_COFIN", "TRASFMENO_COFIN", "IMPACC_FIN", "IMPACC_COFIN", "MANRIS_FIN", "MANRIS_COFIN") AS 
  (SELECT    x.pg_progetto, x.esercizio, 
             x.esercizio_voce, x.ti_appartenenza, x.ti_gestione, x.cd_elemento_voce,
             SUM (x.stanziamento_fin) stanziamento_fin,
             SUM (x.variapiu_fin) variapiu_fin,
             SUM (x.variameno_fin) variameno_fin,
             SUM (x.trasfpiu_fin) trasfpiu_fin,
             SUM (x.trasfmeno_fin) trasfmeno_fin,
             SUM (x.stanziamento_cofin) stanziamento_cofin,
             SUM (x.variapiu_cofin) variapiu_cofin,
             SUM (x.variameno_cofin) variameno_cofin,
             SUM (x.trasfpiu_cofin) trasfpiu_cofin,
             SUM (x.trasfmeno_cofin) trasfmeno_cofin,
             SUM (x.impacc_fin) impacc_fin,
             SUM (x.impacc_cofin) impacc_cofin,
             SUM (x.manris_fin) manris_fin,
             SUM (x.manris_cofin) manris_cofin
   FROM V_SALDI_GAE_VOCE_PROGETTO x
   GROUP BY x.pg_progetto, x.esercizio, 
            x.esercizio_voce, x.ti_appartenenza, x.ti_gestione, x.cd_elemento_voce);
