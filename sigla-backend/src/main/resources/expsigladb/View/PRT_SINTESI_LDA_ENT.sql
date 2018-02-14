--------------------------------------------------------
--  DDL for View PRT_SINTESI_LDA_ENT
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "PRT_SINTESI_LDA_ENT" ("ESERCIZIO", "CDR", "CDS", "UO", "LDA", "ENTRATE_A1", "RICAVI_A1", "RICAVI_EFF_A1", "RICAVI_EFF_PDG_A1", "ACCERT_A1", "TOT_ACCERT_A1", "DOC_ACCERT_A1", "REV_ACCERT_A1", "ACCERT_A1_R", "TOT_ACCERT_A1_R", "DOC_ACCERT_A1_R", "REV_ACCERT_A1_R", "FUNZ", "NAT", "INS", "DESCR", "CDR_AFF", "UO_AFF", "DS_CDR") AS 
  SELECT
--
-- Date: 19/07/2006
-- Version: 1.2
--
-- Vista di stampa Situazione sintetica Linea di Attivita Entrate / Ricavi  Anno corrente
--
-- History
--
-- Date :27/10/2003
-- Version: 1.0
-- Creazione
-- (effettuate alcune modifiche per ottimizzazione-Cineca)
--
-- Date :16/01/2004
-- Version: 1.1
-- Modifica per correggere i join fra le tabelle delle obbligazioni
--
-- Date: 19/07/2006
-- Version: 1.2
-- Gestione Impegni/Accertamenti Residui:
-- gestito il nuovo campo ESERCIZIO_ORIGINALE
--
-- Body
--
ESERCIZIO, CDR, CDS, UO, lda,
SUM(imp_entrate_A1), SUM (imp_ricavi_A1), SUM(imp_ricavi_eff_A1),
SUM (imp_ricavi_eff_pdg_A1) , SUM(imp_accert_A1),
SUM(imp_tot_accert_A1), SUM(imp_doc_accert_A1), SUM(imp_rev_accert_A1),
SUM(IMP_accert_A1_R), SUM(IMP_tot_accert_A1_R), SUM(IMP_doc_accert_A1_R), SUM(IMP_rev_accert_A1_R),
LINEA_ATTIVITA.CD_FUNZIONE, LINEA_ATTIVITA.CD_NATURA, LINEA_ATTIVITA.CD_INSIEME_LA,
SUBSTR(LINEA_ATTIVITA.DS_LINEA_ATTIVITA,1,80),
CDR.CD_CDR_AFFERENZA, NVL(SUBSTR(CDR.CD_CDR_AFFERENZA,1,7), ' ') UO_AFF, CDR.DS_CDR
FROM
(
-- recupero i dati di previsione dai pdg
SELECT
a.ESERCIZIO ESERCIZIO,
a.cd_centro_responsabilita CDR,
SUBSTR(a.cd_centro_responsabilita,1,3) cds,
SUBSTR(a.cd_centro_responsabilita,1,7) uo,
a. cd_linea_attivita lda,
SUM (a.IM_RA_RCE+a.IM_RC_ESRC_ESR) imp_entrate_A1,
SUM (a.IM_RA_RCE+a.IM_RB_RSE) imp_ricavi_A1,
0 imp_ricavi_eff_A1,
0 imp_ricavi_eff_pdg_A1,
0 imp_accert_A1,
0 imp_tot_accert_A1,
0 imp_doc_accert_A1,
0 imp_rev_accert_A1,
0 imp_accert_A1_R,
0 imp_tot_accert_A1_R,
0 imp_doc_accert_A1_R,
0 imp_rev_accert_A1_R
FROM
PRT_SINTESI_LDA_PDG_ENT a
GROUP BY
a.ESERCIZIO,
a.cd_centro_responsabilita,
SUBSTR(a.cd_centro_responsabilita,1,3),
SUBSTR(a.cd_centro_responsabilita,1,7),
a.cd_linea_attivita
UNION ALL
--estrazione dei ricavi figurativi registrati sul capitolo identificato nella tabella configurazione_Cnr
-- alla voce elemento_voce_speciale/ ricavo_figurativo_altro_cdr (02.01.006 alla data di creazione
-- della query diventera 02.00.000 in futuro)
SELECT
a.ESERCIZIO ESERCIZIO,
a.cd_centro_responsabilita CDR,
SUBSTR(a.cd_centro_responsabilita,1,3) cds,
SUBSTR(a.cd_centro_responsabilita,1,7) uo,
a. cd_linea_attivita lda,
0 imp_entrate_A1,
0 imp_ricavi_A1,
0 imp_ricavi_eff_A1,
SUM (a.IM_RB_RSE) imp_ricavi_eff_pdg_A1,
0 imp_accert_A1,
0 imp_tot_accert_A1,
0 imp_doc_accert_A1,
0 imp_rev_accert_A1,
0 imp_accert_A1_R,
0 imp_tot_accert_A1_R,
0 imp_doc_accert_A1_R,
0 imp_rev_accert_A1_R
FROM
PRT_SINTESI_LDA_PDG_ENT a
WHERE a.CD_ELEMENTO_VOCE= Prt_Getdati_Confcnr(a.ESERCIZIO, 'ELEMENTO_VOCE_SPECIALE',  'RICAVO_FIGURATIVO_ALTRO_CDR' )
GROUP BY
a.ESERCIZIO,
a.cd_centro_responsabilita,
SUBSTR(a.cd_centro_responsabilita,1,3) ,
SUBSTR(a.cd_centro_responsabilita,1,7) ,
a.cd_linea_attivita
--fine query per ricavi figurativi da pdg
UNION ALL
--recupero i dati degli accertamenti registrati per lda
SELECT
b.ESERCIZIO ESERCIZIO,
b.cd_centro_responsabilita CDR,
SUBSTR(B.cd_centro_responsabilita,1,3) cds,
SUBSTR(B.cd_centro_responsabilita,1,7) uo,
b.cd_linea_attivita lda,
0 imp_entrate_A1,
0 imp_ricavi_A1,
0 imp_ricavi_eff_A1,
0 imp_ricavi_eff_pdg_A1,
(DECODE(a.ESERCIZIO,a.esercizio_competenza,b.IM_VOCE,0)) imp_accert_A1,
(DECODE(a.ESERCIZIO,a.esercizio_competenza,a.IM_accertamento,0)) imp_tot_accert_A1,
ROUND((DECODE(a.ESERCIZIO,a.esercizio_competenza,(d.IM_ASSOCIATO_DOC_AMM*(b.im_voce*100/DECODE(d.im_scadenza,0,1,d.im_scadenza))/100) ,0)),2) imp_doc_accert_A1,
ROUND((DECODE(a.ESERCIZIO,a.esercizio_competenza,(d.IM_ASSOCIATO_DOC_CONTABILE*(b.im_voce*100/DECODE(d.im_scadenza,0,1,d.im_scadenza))/100),0)),2) imp_rev_accert_A1,
0 imp_accert_A1_R,
0 imp_tot_accert_A1_R,
0 imp_doc_accert_A1_R,
0 imp_rev_accert_A1_R
FROM
ACCERTAMENTO a,
ACCERTAMENTO_SCAD_VOCE b,
ACCERTAMENTO_SCADENZARIO d
WHERE
b.cd_cds=a.cd_cds
AND b.ESERCIZIO=a.ESERCIZIO
AND b.esercizio_originale=a.esercizio_originale
AND b.pg_accertamento=a.pg_accertamento
AND d.cd_cds=a.cd_cds
AND d.ESERCIZIO=a.ESERCIZIO
AND d.esercizio_originale=a.esercizio_originale
AND d.pg_accertamento=a.pg_accertamento
AND b.cd_cds=d.cd_cds
AND b.ESERCIZIO=d.ESERCIZIO
AND b.esercizio_originale=d.esercizio_originale
AND b.pg_accertamento=d.pg_accertamento
AND b.PG_accertamento_SCADENZARIO=d.PG_accertamento_SCADENZARIO
AND a.CD_TIPO_DOCUMENTO_CONT='ACR'
/* unione di una query per estrarre GLI ACCERTAMENTI RESIDUI */
UNION ALL
SELECT
b.ESERCIZIO ESERCIZIO,
b.cd_centro_responsabilita CDR,
SUBSTR(B.cd_centro_responsabilita,1,3) cds,
SUBSTR(B.cd_centro_responsabilita,1,7) uo,
b.cd_linea_attivita lda,
0 imp_entrate_A1,
0 imp_ricavi_A1,
0 imp_ricavi_eff_A1,
0 imp_ricavi_eff_pdg_A1,
0 imp_accert_A1,
0 imp_tot_accert_A1,
0 imp_doc_accert_A1,
0 imp_rev_accert_A1,
(DECODE(a.ESERCIZIO,a.esercizio_competenza,b.IM_VOCE,0)) imp_accert_A1_R,
(DECODE(a.ESERCIZIO,a.esercizio_competenza,a.IM_accertamento,0)) imp_tot_accert_A1_R,
ROUND((DECODE(a.ESERCIZIO,a.esercizio_competenza,(d.IM_ASSOCIATO_DOC_AMM*(b.im_voce*100/DECODE(d.im_scadenza,0,1,d.im_scadenza))/100) ,0)),2) imp_doc_accert_A1_R,
ROUND((DECODE(a.ESERCIZIO,a.esercizio_competenza,(d.IM_ASSOCIATO_DOC_CONTABILE*(b.im_voce*100/DECODE(d.im_scadenza,0,1,d.im_scadenza))/100),0)),2) imp_rev_accert_A1_R
FROM
ACCERTAMENTO a,
ACCERTAMENTO_SCAD_VOCE b,
ACCERTAMENTO_SCADENZARIO d
WHERE
b.cd_cds=a.cd_cds
AND b.ESERCIZIO=a.ESERCIZIO
AND b.esercizio_originale=a.esercizio_originale
AND b.pg_accertamento=a.pg_accertamento
AND d.cd_cds=a.cd_cds
AND d.ESERCIZIO=a.ESERCIZIO
AND d.esercizio_originale=a.esercizio_originale
AND d.pg_accertamento=a.pg_accertamento
AND b.cd_cds=d.cd_cds
AND b.ESERCIZIO=d.ESERCIZIO
AND b.esercizio_originale=d.esercizio_originale
AND b.pg_accertamento=d.pg_accertamento
AND b.PG_accertamento_SCADENZARIO=d.PG_accertamento_SCADENZARIO
AND a.CD_TIPO_DOCUMENTO_CONT='ACR_RES'
/* fine inserimento nuova query*/
UNION ALL
-- dati dei ricavi effettivi dalla contabilita economica
SELECT
MOVIMENTO_COAN.ESERCIZIO,
MOVIMENTO_COAN.CD_CENTRO_RESPONSABILITA,
SUBSTR(MOVIMENTO_COAN.cd_centro_responsabilita,1,3) cds,
SUBSTR(MOVIMENTO_COAN.cd_centro_responsabilita,1,7) uo,
MOVIMENTO_COAN.CD_LINEA_ATTIVITA,
0 imp_entrate_A1,
0 imp_ricavi_A1,
SUM(MOVIMENTO_COAN.IM_MOVIMENTO),
0 imp_ricavi_eff_pdg_A1,
0 imp_accert_A1,
0 imp_tot_accert_A1,
0 imp_doc_accert_A1,
0 imp_rev_accert_A1,
0 imp_accert_A1_R,
0 imp_tot_accert_A1_R,
0 imp_doc_accert_A1_R,
0 imp_rev_accert_A1_R
FROM MOVIMENTO_COAN
WHERE MOVIMENTO_COAN.SEZIONE='A'
GROUP BY
MOVIMENTO_COAN.ESERCIZIO,
MOVIMENTO_COAN.CD_CENTRO_RESPONSABILITA,
SUBSTR(MOVIMENTO_COAN.cd_centro_responsabilita,1,3),
SUBSTR(MOVIMENTO_COAN.cd_centro_responsabilita,1,7),
MOVIMENTO_COAN.CD_LINEA_ATTIVITA
), LINEA_ATTIVITA, CDR
WHERE
LINEA_ATTIVITA.CD_LINEA_ATTIVITA= lda
AND LINEA_ATTIVITA.CD_CENTRO_RESPONSABILITA=CDR
AND CDR.CD_CENTRO_RESPONSABILITA=CDR
GROUP BY
ESERCIZIO,
CDR,
CDS,
UO,
LDA,
LINEA_ATTIVITA.CD_FUNZIONE,
LINEA_ATTIVITA.CD_NATURA,
LINEA_ATTIVITA.CD_INSIEME_LA,
LINEA_ATTIVITA.DS_LINEA_ATTIVITA,
CDR.CD_CDR_AFFERENZA,
SUBSTR(CDR.CD_CDR_AFFERENZA,1,7),
CDR.DS_CDR;

   COMMENT ON TABLE "PRT_SINTESI_LDA_ENT"  IS 'Vista di stampa Situazione sintetica Linea di Attivita Entrate / Ricavi  Anno corrente';
