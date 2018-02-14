--------------------------------------------------------
--  DDL for View PRT_ANALITICA_LDA
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "PRT_ANALITICA_LDA" ("ESERCIZIO", "CDR", "CDS", "UO", "LDA", "VOCE", "DS_VOCE", "SPESE_A1", "COSTI_A1", "COSTI_EFF_A1", "COSTI_EFF_PDGP_A1", "OBBL_A1", "TOT_OBBL_A1", "DOC_OBBL_A1", "MND_OBBL_A1", "FUNZ", "NAT", "INS", "DESCR", "CDR_AFF", "UO_AFF", "DS_CDR") AS 
  SELECT
--
-- Date: 18/07/2006
-- Version: 1.3
--
-- Vista di stampa Situazione ANALITICA Linea di Attivita
--
-- History
--
-- Date :28/04/2003
-- Version: 1.0
-- Creazione
--
-- Date: 26/09/2003
-- Version: 1.1
-- Modifica per estrarre cds, uo, cdr_aff, uo_aff per ottimizzazione stampa
--
-- Date: 16/01/2004
-- Version: 1.2
-- Modifica per correggere i join fra le tabelle delle obbligazioni
--
-- Date: 18/07/2006
-- Version: 1.3
-- Gestione Impegni/Accertamenti Residui:
-- gestito il nuovo campo ESERCIZIO_ORIGINALE
--
-- Body
--
ESERCIZIO, CDR, cds, uo, lda, voce, ds_voce,
SUM(imp_spese_A1), SUM (imp_costi_A1), SUM(imp_costi_eff_A1),
SUM (imp_costi_eff_pdgP_A1),
SUM(imp_obbl_A1),SUM(imp_tot_obbl_A1), SUM(imp_doc_obbl_A1), SUM(imp_mnd_obbl_A1),
LINEA_ATTIVITA.CD_FUNZIONE, LINEA_ATTIVITA.CD_NATURA, LINEA_ATTIVITA.CD_INSIEME_LA,
LINEA_ATTIVITA.DS_LINEA_ATTIVITA,
CDR.CD_CDR_AFFERENZA, NVL(SUBSTR(CDR.cd_cdr_afferenza,1,7),' '), CDR.DS_CDR
FROM
(
-- recupero i dati di previsione dai pdg  con la voce di bilancio
SELECT
a.ESERCIZIO ESERCIZIO,
a.cd_centro_responsabilita CDR,
SUBSTR(a.cd_centro_responsabilita,1,3) cds,
SUBSTR(a.cd_centro_responsabilita,1,7) uo,
a. cd_linea_attivita lda,
a.CD_ELEMENTO_VOCE voce,
a.ds_voce,
SUM (a.IM_RI_CCS_SPESE_ODC+a.IM_RK_CCS_SPESE_OGC+a.IM_RQ_SSC_COSTI_ODC+
a.IM_RS_SSC_COSTI_OGC+a.IM_RU_SPESE_COSTI_ALTRUI) imp_spese_A1,
SUM (a.IM_RH_CCS_COSTI+a.IM_RM_CSS_AMMORTAMENTI+a.IM_RN_CSS_RIMANENZE+
a.IM_RO_CSS_ALTRI_COSTI+ a.IM_RP_CSS_VERSO_ALTRO_CDR) imp_costi_A1,
0 imp_costi_eff_A1,
SUM (a.IM_RP_CSS_VERSO_ALTRO_CDR) imp_costi_eff_pdgP_A1,
0 imp_obbl_A1,
0 imp_tot_obbl_A1,
0 imp_doc_obbl_A1,
0 imp_mnd_obbl_A1
FROM
PRT_analitica_LDA_PDG_SPE a
GROUP BY
a.ESERCIZIO,
a.cd_centro_responsabilita,
SUBSTR(a.cd_centro_responsabilita,1,3),
SUBSTR(a.cd_centro_responsabilita,1,7),
a.cd_linea_attivita,
a.CD_ELEMENTO_VOCE,
a.ds_voce
UNION ALL
-- recupero i dati delle obbligazioni per lda con indicata la voce di bilancio
SELECT
b.ESERCIZIO ESERCIZIO,
b.cd_centro_responsabilita CDR,
SUBSTR(b.cd_centro_responsabilita,1,3) cds,
SUBSTR(b.cd_centro_responsabilita,1,7) uo,
b.cd_linea_attivita lda,
a.CD_ELEMENTO_VOCE voce,
e.ds_elemento_voce,
0 imp_spese_A1,
0 imp_costi_A1,
0 imp_costi_eff_A1,
0 imp_costi_eff_pdgP_A1,
(DECODE(a.ESERCIZIO,a.esercizio_competenza,b.IM_VOCE,0)) imp_obbl_A1,
(DECODE(a.ESERCIZIO,a.esercizio_competenza,a.IM_OBBLIGAZIONE,0)) imp_tot_obbl_A1,
ROUND((DECODE(a.ESERCIZIO,a.esercizio_competenza,(d.IM_ASSOCIATO_DOC_AMM*(b.im_voce*100/DECODE(d.im_scadenza,0,1,d.im_scadenza))/100) ,0)),2) imp_doc_obbl_A1,
ROUND((DECODE(a.ESERCIZIO,a.esercizio_competenza,(d.IM_ASSOCIATO_DOC_CONTABILE*(b.im_voce*100/DECODE(d.im_scadenza,0,1,d.im_scadenza))/100),0)),2) imp_mnd_obbl_A1
FROM
OBBLIGAZIONE a,
OBBLIGAZIONE_SCAD_VOCE b,
OBBLIGAZIONE_SCADENZARIO d,
ELEMENTO_VOCE e
WHERE
b.cd_cds=a.cd_cds
AND b.ESERCIZIO=a.ESERCIZIO
AND b.esercizio_originale=a.esercizio_originale
AND b.pg_obbligazione=a.pg_obbligazione
AND d.cd_cds=a.cd_cds
AND D.ESERCIZIO=A.ESERCIZIO
AND d.esercizio_originale=a.esercizio_originale
AND d.pg_obbligazione=a.pg_obbligazione
And B.CD_CDS=D.CD_CDS
AND B.ESERCIZIO=D.ESERCIZIO
AND B.ESERCIZIO_ORIGINALE=D.ESERCIZIO_ORIGINALE
AND B.PG_OBBLIGAZIONE=D.PG_OBBLIGAZIONE
AND b.PG_OBBLIGAZIONE_SCADENZARIO=d.PG_OBBLIGAZIONE_SCADENZARIO
AND e.ESERCIZIO=a.ESERCIZIO
AND e.ti_appartenenza='D'
AND e.ti_gestione='S'
AND e.cd_elemento_voce=a.cd_elemento_voce
AND e.ESERCIZIO=a.ESERCIZIO
AND EXISTS ( SELECT 1 FROM
CDR c
WHERE
b.cd_centro_responsabilita = c.cd_centro_responsabilita AND
a.CD_UNITA_ORGANIZZATIVA = c.CD_UNITA_ORGANIZZATIVA)
/* unione di una query per estrarre le obbligazioni sul 000.999.000 */
UNION ALL
SELECT
b.ESERCIZIO ESERCIZIO,
b.cd_centro_responsabilita CDR,
SUBSTR(b.cd_centro_responsabilita,1,3)cds ,
SUBSTR(b.cd_centro_responsabilita,1,7) uo,
b.cd_linea_attivita lda,
a.CD_ELEMENTO_VOCE voce,
e.ds_elemento_voce,
0 imp_spese_A1,
0 imp_costi_A1,
0 imp_costi_eff_A1,
0 imp_costi_eff_pdgP_A1,
(DECODE(a.ESERCIZIO,a.esercizio_competenza,b.IM_VOCE,0)) imp_obbl_A1,
(DECODE(a.ESERCIZIO,a.esercizio_competenza,a.IM_OBBLIGAZIONE,0)) imp_tot_obbl_A1,
ROUND((DECODE(a.ESERCIZIO,a.esercizio_competenza,(d.IM_ASSOCIATO_DOC_AMM*(b.im_voce*100/DECODE(d.im_scadenza,0,1,d.im_scadenza))/100) ,0)),2) imp_doc_obbl_A1,
ROUND((DECODE(a.ESERCIZIO,a.esercizio_competenza,(d.IM_ASSOCIATO_DOC_CONTABILE*(b.im_voce*100/DECODE(d.im_scadenza,0,1,d.im_scadenza))/100),0)),2) imp_mnd_obbl_A1
FROM
OBBLIGAZIONE a,
OBBLIGAZIONE_SCAD_VOCE b,
OBBLIGAZIONE_SCADENZARIO d,
ELEMENTO_VOCE e
WHERE
b.ESERCIZIO=a.ESERCIZIO
AND b.cd_cds=a.cd_cds
AND b.esercizio_originale=a.esercizio_originale
AND b.pg_obbligazione=a.pg_obbligazione
AND d.cd_cds=a.cd_cds
AND d.esercizio_originale=a.esercizio_originale
AND d.pg_obbligazione=a.pg_obbligazione
AND D.ESERCIZIO=A.ESERCIZIO
AND B.CD_CDS=D.CD_CDS
AND B.ESERCIZIO=D.ESERCIZIO
AND B.ESERCIZIO_ORIGINALE=D.ESERCIZIO_ORIGINALE
AND B.PG_OBBLIGAZIONE=D.PG_OBBLIGAZIONE
AND b.PG_OBBLIGAZIONE_SCADENZARIO=d.PG_OBBLIGAZIONE_SCADENZARIO
AND e.ESERCIZIO=a.ESERCIZIO
AND e.ti_appartenenza='D'
AND e.ti_gestione='S'
AND e.cd_elemento_voce=a.cd_elemento_voce
AND SUBSTR(b.cd_centro_responsabilita,1,3)<>cnrctb020.getCdCdsEnte(a.ESERCIZIO)
AND NOT EXISTS ( SELECT 1 FROM
CDR c
WHERE
b.cd_centro_responsabilita = c.cd_centro_responsabilita AND
a.CD_UNITA_ORGANIZZATIVA = c.CD_UNITA_ORGANIZZATIVA)
/* fine inserimento nuova query*/
UNION ALL
-- recupero i dati dei costi effettivi dalla cont. econ. con voce di bilancio
SELECT
MOVIMENTO_COAN.ESERCIZIO,
MOVIMENTO_COAN.CD_CENTRO_RESPONSABILITA,
SUBSTR(MOVIMENTO_COAN.cd_centro_responsabilita,1,3) cds,
SUBSTR(MOVIMENTO_COAN.cd_centro_responsabilita,1,7) uo,
MOVIMENTO_COAN.CD_LINEA_ATTIVITA,
ASS_EV_VOCEEP.CD_ELEMENTO_VOCE,
ELEMENTO_VOCE.DS_ELEMENTO_VOCE,
0 imp_spese_A1,
0 imp_costi_A1,
SUM(MOVIMENTO_COAN.IM_MOVIMENTO),
0 imp_costi_eff_pdgP_A1,
0 imp_obbl_A1,
0 imp_tot_obbl_A1,
0 imp_doc_obbl_A1,
0 imp_mnd_obbl_A1
FROM MOVIMENTO_COAN, ASS_EV_VOCEEP, ELEMENTO_VOCE
WHERE MOVIMENTO_COAN.SEZIONE='D'
AND ASS_EV_VOCEEP.CD_VOCE_EP=MOVIMENTO_COAN.CD_VOCE_EP
AND ASS_EV_VOCEEP.ESERCIZIO=MOVIMENTO_COAN.ESERCIZIO
AND ASS_EV_VOCEEP.TI_APPARTENENZA='D'
AND ASS_EV_VOCEEP.TI_GESTIONE='S'
AND ELEMENTO_VOCE.ESERCIZIO=ASS_EV_VOCEEP.ESERCIZIO
AND ELEMENTO_VOCE.ti_appartenenza='D'
AND ELEMENTO_VOCE.ti_gestione='S'
AND ELEMENTO_VOCE.cd_elemento_voce=ASS_EV_VOCEEP.cd_elemento_voce
GROUP BY
MOVIMENTO_COAN.ESERCIZIO,
MOVIMENTO_COAN.CD_CENTRO_RESPONSABILITA,
SUBSTR(MOVIMENTO_COAN.cd_centro_responsabilita,1,3),
SUBSTR(MOVIMENTO_COAN.cd_centro_responsabilita,1,7),
MOVIMENTO_COAN.CD_LINEA_ATTIVITA,
ASS_EV_VOCEEP.CD_ELEMENTO_VOCE,
ELEMENTO_VOCE.ds_elemento_voce
), LINEA_ATTIVITA, CDR
WHERE
LINEA_ATTIVITA.CD_LINEA_ATTIVITA= lda
AND LINEA_ATTIVITA.CD_CENTRO_RESPONSABILITA=CDR
AND CDR.CD_CENTRO_RESPONSABILITA=CDR
GROUP BY
ESERCIZIO,
CDR,
cds,
uo,
LDA,
voce,
ds_voce,
LINEA_ATTIVITA.CD_FUNZIONE,
LINEA_ATTIVITA.CD_NATURA,
LINEA_ATTIVITA.CD_INSIEME_LA,
LINEA_ATTIVITA.DS_LINEA_ATTIVITA,
CDR.CD_CDR_AFFERENZA,
CDR.DS_CDR;

   COMMENT ON TABLE "PRT_ANALITICA_LDA"  IS 'Vista di stampa Situazione ANALITICA Linea di Attivita';
