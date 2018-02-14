--------------------------------------------------------
--  DDL for View PRT_REND_FIN_SPESE_CDS_ALL
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "PRT_REND_FIN_SPESE_CDS_ALL" ("ESERCIZIO", "CDS", "PARTE", "DS_PARTE", "TITOLO", "DS_TITOLO", "SEZIONE", "DS_SEZIONE", "RUBRICA", "DS_RUBRICA", "CAPITOLO", "DS_CAPITOLO", "INIZIALE", "VAR_PIU", "VAR_MENO", "ASSESTATO", "PAGAMENTI", "IN_PIU", "IN_MENO") AS 
  SELECT  DISTINCT
--
-- Date: 27/10/2003
-- Version: 1.0
--
-- Vista di stampa Rendiconto Finanziario CDS Spese (compresi importi a zero)
--
-- History:
--
-- Date: 27/10/2003
-- Version: 1.0
-- Creazione
-- (effettuate alcune modifiche per ottimizzazione-Cineca)
--
-- Body
--
eser, cds, parte, ds_parte, titolo, ds_titolo, sezione, ds_sezione, rubrica, ds_rubrica,
capitolo, ds_capitolo, iniziale, var_piu, var_meno,
assestato, pagamenti, in_piu, in_meno FROM
--estrazione dati per la parte I del bilancio
(SELECT DISTINCT eser, cds, parte,
Prt_Getdes_Vocecap('E', eser, 'D', 'S', parte, 'P') AS ds_parte, titolo,
Prt_Getdes_Vocecap('E', eser, 'D', 'S', titolo, 'T') AS ds_titolo, sezione,
Prt_Getdes_Vocecap('F', eser, 'D', 'S', sezione, 'S') AS ds_sezione,
rubrica, Prt_Getdes_Vocecap('F', eser, 'D', 'S', rubrica, 'R') AS ds_rubrica,
capitolo, Prt_Getdes_Vocecap('E', eser, 'D', 'S', SUBSTR(capitolo,1,5)||SUBSTR(capitolo,17,3), 'C') AS ds_capitolo,
iniziale, var_piu, var_meno, assestato, pagamenti, in_piu, in_meno
FROM
(SELECT
VOCE_F_SALDI_CMP.ESERCIZIO eser,
VOCE_F_SALDI_CMP.CD_CDS cds,
SUBSTR(VOCE_F_SALDI_CMP.CD_VOCE,1,1) parte,
SUBSTR(VOCE_F_SALDI_CMP.CD_VOCE,1,4) titolo,
SUBSTR(VOCE_F_SALDI_CMP.CD_VOCE,1,7) sezione,
SUBSTR(VOCE_F_SALDI_CMP.CD_VOCE,1,15) rubrica,
SUBSTR(VOCE_F_SALDI_CMP.CD_VOCE,1,19) capitolo,
SUM(NVL(VOCE_F_SALDI_CMP.IM_STANZ_INIZIALE_A1,0)) iniziale,
SUM(NVL(VOCE_F_SALDI_CMP.VARIAZIONI_PIU,0)) var_piu,
SUM(NVL(VOCE_F_SALDI_CMP.VARIAZIONI_MENO,0)) var_meno,
SUM((NVL(VOCE_F_SALDI_CMP.IM_STANZ_INIZIALE_A1,0)+NVL(VOCE_F_SALDI_CMP.VARIAZIONI_PIU,0)-NVL(VOCE_F_SALDI_CMP.VARIAZIONI_MENO,0))) assestato,
SUM(NVL(VOCE_F_SALDI_CMP.IM_MANDATI_REVERSALI,0)) pagamenti,
SUM(NVL(VOCE_F_SALDI_CMP.IM_MANDATI_REVERSALI,0)  - (NVL(VOCE_F_SALDI_CMP.IM_STANZ_INIZIALE_A1,0)+NVL(VOCE_F_SALDI_CMP.VARIAZIONI_PIU,0)-NVL(VOCE_F_SALDI_CMP.VARIAZIONI_MENO,0))) in_piu,
SUM((NVL(VOCE_F_SALDI_CMP.IM_STANZ_INIZIALE_A1,0)+NVL(VOCE_F_SALDI_CMP.VARIAZIONI_PIU,0)-NVL(VOCE_F_SALDI_CMP.VARIAZIONI_MENO,0))-NVL(VOCE_F_SALDI_CMP.IM_MANDATI_REVERSALI,0)) in_meno
FROM VOCE_F_SALDI_CMP
WHERE
VOCE_F_SALDI_CMP.TI_APPARTENENZA='D'
AND VOCE_F_SALDI_CMP.TI_GESTIONE='S'
AND SUBSTR(VOCE_F_SALDI_CMP.CD_VOCE,1,1)='1'
GROUP BY
VOCE_F_SALDI_CMP.ESERCIZIO,
VOCE_F_SALDI_CMP.CD_CDS,
SUBSTR(VOCE_F_SALDI_CMP.CD_VOCE,1,1) ,
SUBSTR(VOCE_F_SALDI_CMP.CD_VOCE,1,4) ,
SUBSTR(VOCE_F_SALDI_CMP.CD_VOCE,1,7) ,
SUBSTR(VOCE_F_SALDI_CMP.CD_VOCE,1,15),
SUBSTR(VOCE_F_SALDI_CMP.CD_VOCE,1,19)
)
UNION ALL
-- estrazione dati parte II del bilancio
(SELECT DISTINCT eser, cds, parte,
Prt_Getdes_Vocecap('E', eser, 'D', 'S', parte, 'P') AS ds_parte, titolo,  NULL, sezione, NULL, rubrica, NULL,
capitolo, Prt_Getdes_Vocecap('E', eser, 'D', 'S', capitolo, 'C') AS ds_capitolo, iniziale, var_piu, var_meno, assestato, pagamenti, in_piu, in_meno
FROM
(SELECT
VOCE_F_SALDI_CMP.ESERCIZIO eser,
VOCE_F_SALDI_CMP.CD_CDS cds,
SUBSTR(VOCE_F_SALDI_CMP.CD_VOCE,1,1) parte,
NULL titolo,
NULL sezione,
NULL rubrica,
SUBSTR(VOCE_F_SALDI_CMP.CD_VOCE,1,19) capitolo,
SUM(NVL(VOCE_F_SALDI_CMP.IM_STANZ_INIZIALE_A1,0)) iniziale,
SUM(NVL(VOCE_F_SALDI_CMP.VARIAZIONI_PIU,0)) var_piu,
SUM(NVL(VOCE_F_SALDI_CMP.VARIAZIONI_MENO,0)) var_meno,
SUM((NVL(VOCE_F_SALDI_CMP.IM_STANZ_INIZIALE_A1,0)+NVL(VOCE_F_SALDI_CMP.VARIAZIONI_PIU,0)-NVL(VOCE_F_SALDI_CMP.VARIAZIONI_MENO,0))) assestato,
SUM(NVL(VOCE_F_SALDI_CMP.IM_MANDATI_REVERSALI,0)) pagamenti,
SUM(NVL(VOCE_F_SALDI_CMP.IM_MANDATI_REVERSALI,0)- ( NVL(VOCE_F_SALDI_CMP.IM_STANZ_INIZIALE_A1,0)+NVL(VOCE_F_SALDI_CMP.VARIAZIONI_PIU,0)-NVL(VOCE_F_SALDI_CMP.VARIAZIONI_MENO,0))) in_piu,
SUM((NVL(VOCE_F_SALDI_CMP.IM_STANZ_INIZIALE_A1,0)+NVL(VOCE_F_SALDI_CMP.VARIAZIONI_PIU,0)-NVL(VOCE_F_SALDI_CMP.VARIAZIONI_MENO,0))-NVL(VOCE_F_SALDI_CMP.IM_MANDATI_REVERSALI,0)) in_meno
FROM VOCE_F_SALDI_CMP
WHERE
VOCE_F_SALDI_CMP.TI_APPARTENENZA='D'
AND VOCE_F_SALDI_CMP.TI_GESTIONE='S'
AND SUBSTR(VOCE_F_SALDI_CMP.CD_VOCE,1,1)='2'
GROUP BY
VOCE_F_SALDI_CMP.ESERCIZIO,
VOCE_F_SALDI_CMP.CD_CDS,
SUBSTR(VOCE_F_SALDI_CMP.CD_VOCE,1,1) ,
SUBSTR(VOCE_F_SALDI_CMP.CD_VOCE,1,19)
)))
;

   COMMENT ON TABLE "PRT_REND_FIN_SPESE_CDS_ALL"  IS 'Vista di stampa Rendiconto Finanziario CDS Spese (compresi importi a zero)';
