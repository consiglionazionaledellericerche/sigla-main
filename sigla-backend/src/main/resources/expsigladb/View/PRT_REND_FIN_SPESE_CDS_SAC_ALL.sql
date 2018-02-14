--------------------------------------------------------
--  DDL for View PRT_REND_FIN_SPESE_CDS_SAC_ALL
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "PRT_REND_FIN_SPESE_CDS_SAC_ALL" ("ESERCIZIO", "CDS", "RUBRICA", "DS_RUBRICA", "ARTICOLO", "DS_ARTICOLO", "CAPITOLO", "DS_CAPITOLO", "INIZIALE", "VAR_PIU", "VAR_MENO", "ASSESTATO", "PAGAMENTI", "IN_PIU", "IN_MENO") AS 
  SELECT DISTINCT
--
-- Date: 27/10/2003
-- Version: 1.0
--
-- Vista di stampa Rendiconto Finanziario CDS Spese per articolo, SOLO SAC   (compresi importi a zero)
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
eser, cds, rubrica,
prt_getdes_vocecap('F', eser, 'D', 'S', SUBSTR(CAPITOLO,1,15), 'R') AS ds_rubrica, ARTICOLO,
prt_getdes_vocecap('F', eser, 'D', 'S', (capitolo||'.'||SUBSTR(articolo,9,3)) , 'A') AS DS_ARTICOLO, capitolo,
prt_getdes_vocecap('E', eser, 'D', 'S', SUBSTR(capitolo,1,5)||SUBSTR(CAPITOLO,17,3) , 'C')  AS ds_capitolo,
iniziale, var_piu, var_meno, assestato, pagamenti, in_piu, in_meno
--estrazione dati per la parte I del bilancio
FROM
(SELECT
VOCE_F_SALDI_CMP.ESERCIZIO eser,
VOCE_F_SALDI_CMP.CD_CDS cds,
SUBSTR(VOCE_F_SALDI_CMP.CD_VOCE,9,7) rubrica,
(SUBSTR(VOCE_F_SALDI_CMP.CD_VOCE,9,7) ||  '.' || SUBSTR(VOCE_F_SALDI_CMP.CD_VOCE,21,3) ) articolo,
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
AND VOCE_F_SALDI_CMP.CD_CDS='000'
GROUP BY
VOCE_F_SALDI_CMP.ESERCIZIO,
VOCE_F_SALDI_CMP.CD_CDS,
SUBSTR(VOCE_F_SALDI_CMP.CD_VOCE,9,7),
(SUBSTR(VOCE_F_SALDI_CMP.CD_VOCE,9,7) ||  '.' || SUBSTR(VOCE_F_SALDI_CMP.CD_VOCE,21,3) ),
SUBSTR(VOCE_F_SALDI_CMP.CD_VOCE,1,19)
)
;

   COMMENT ON TABLE "PRT_REND_FIN_SPESE_CDS_SAC_ALL"  IS 'Vista di stampa Rendiconto Finanziario CDS Spese per articolo, SOLO SAC   (compresi importi a zero)';
