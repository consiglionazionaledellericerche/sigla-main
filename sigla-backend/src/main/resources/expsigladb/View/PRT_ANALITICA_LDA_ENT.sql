--------------------------------------------------------
--  DDL for View PRT_ANALITICA_LDA_ENT
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "PRT_ANALITICA_LDA_ENT" ("ESERCIZIO", "CDR", "CDS", "UO", "LDA", "VOCE", "DS_VOCE", "ENTRATE_A1", "RICAVI_A1", "RICAVI_EFF_A1", "RICAVI_EFF_PDG_A1", "ACCERT_A1", "TOT_ACCERT_A1", "DOC_ACCERT_A1", "REV_ACCERT_A1", "ACCERT_A1_R", "TOT_ACCERT_A1_R", "DOC_ACCERT_A1_R", "REV_ACCERT_A1_R", "FUNZ", "NAT", "INS", "DESCR", "CDR_AFF", "UO_AFF", "DS_CDR") AS 
  SELECT ESERCIZIO, CDR, cds, uo,lda, VOCE, DS_VOCE,
       SUM(imp_entrate_A1), SUM (imp_ricavi_A1), SUM(imp_ricavi_eff_A1),
       SUM (imp_ricavi_eff_pdg_A1) , SUM(imp_accert_A1),
       SUM(imp_tot_accert_A1), SUM(imp_doc_accert_A1), SUM(imp_rev_accert_A1),
       SUM(IMP_accert_A1_R), SUM(IMP_tot_accert_A1_R), SUM(IMP_doc_accert_A1_R), SUM(IMP_rev_accert_A1_R),
       LINEA_ATTIVITA.CD_FUNZIONE, LINEA_ATTIVITA.CD_NATURA, LINEA_ATTIVITA.CD_INSIEME_LA,
       SUBSTR(LINEA_ATTIVITA.DS_LINEA_ATTIVITA,1,80),
       CDR.CD_CDR_AFFERENZA, NVL(SUBSTR(CDR.cd_cdr_afferenza, 1,7),' ') uo_aff,CDR.DS_CDR
FROM
    (
     -- recupero i dati di previsione dai pdg
     Select a.ESERCIZIO ESERCIZIO,
            a.cd_centro_responsabilita CDR,
            SUBSTR(a.cd_centro_responsabilita,1,3) cds,
            SUBSTR(a.cd_centro_responsabilita,1,7) uo,
            a. cd_linea_attivita lda,
            A.CD_ELEMENTO_VOCE VOCE,
            A.DS_VOCE,
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
          PRT_ANALITICA_LDA_PDG_ENT a
     GROUP BY
          a.ESERCIZIO,
          a.cd_centro_responsabilita,
          SUBSTR(a.cd_centro_responsabilita,1,3),
          SUBSTR(a.cd_centro_responsabilita,1,7),
          a.cd_linea_attivita,
          A.CD_ELEMENTO_VOCE,
          A.DS_VOCE
     UNION ALL
     --estrazione dei ricavi effettivi registrati sul capitolo identificato nella tabella configurazione_Cnr
     -- alla voce elemento_voce_speciale/ ricavo_figurativo_altro_cdr (02.01.006 alla data di creazione
     -- della query diventera 02.00.000 in futuro)
     SELECT a.ESERCIZIO ESERCIZIO,
            a.cd_centro_responsabilita CDR,
            SUBSTR(a.cd_centro_responsabilita,1,3) cds,
            SUBSTR(a.cd_centro_responsabilita,1,7) uo,
            a. cd_linea_attivita lda,
            A.CD_ELEMENTO_VOCE VOCE,
            A.DS_VOCE,
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
          PRT_ANALITICA_LDA_PDG_ENT a
     WHERE
          a.CD_ELEMENTO_VOCE = prt_Getdati_confcnr(a.ESERCIZIO, 'ELEMENTO_VOCE_SPECIALE',  'RICAVO_FIGURATIVO_ALTRO_CDR' )
     GROUP BY
          a.ESERCIZIO,
          a.cd_centro_responsabilita,
          SUBSTR(a.cd_centro_responsabilita,1,3),
          SUBSTR(a.cd_centro_responsabilita,1,7),
          a.cd_linea_attivita,
          A.CD_ELEMENTO_VOCE ,
          A.DS_VOCE
     --fine query per RICAVI  effettivi da pdg
     Union All
     -- recupero i dati di previsione relativi alla competenza dai pdg con la voce di bilancio
     -- sulla nuova tabella previsionale PDG_MODULO_SPESE_GEST
     Select PDG_MODULO_ENTRATE_GEST.ESERCIZIO ESERCIZIO,
            PDG_MODULO_ENTRATE_GEST.CD_CDR_ASSEGNATARIO CDR,
            Substr(PDG_MODULO_ENTRATE_GEST.CD_CDR_ASSEGNATARIO,1,3) cds,
            Substr(PDG_MODULO_ENTRATE_GEST.CD_CDR_ASSEGNATARIO,1,7) uo,
            PDG_MODULO_ENTRATE_GEST.CD_LINEA_ATTIVITA lda,
            PDG_MODULO_ENTRATE_GEST.CD_ELEMENTO_VOCE voce,
            ELEMENTO_VOCE.DS_ELEMENTO_VOCE,
            Nvl(Sum(PDG_MODULO_ENTRATE_GEST.IM_ENTRATA),0) imp_entrate_A1,
            0 imp_ricavi_A1,
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
     From
          PDG_MODULO_ENTRATE_GEST,
          ELEMENTO_VOCE
     Where
          --Join tra "PDG_MODULO_ENTRATE_GEST" e "ELEMENTO_VOCE"
          PDG_MODULO_ENTRATE_GEST.ESERCIZIO = ELEMENTO_VOCE.ESERCIZIO And
          PDG_MODULO_ENTRATE_GEST.TI_GESTIONE = ELEMENTO_VOCE.TI_GESTIONE And
          PDG_MODULO_ENTRATE_GEST.TI_APPARTENENZA = ELEMENTO_VOCE.TI_APPARTENENZA And
          PDG_MODULO_ENTRATE_GEST.CD_ELEMENTO_VOCE = ELEMENTO_VOCE.CD_ELEMENTO_VOCE
     Group By
           PDG_MODULO_ENTRATE_GEST.ESERCIZIO,
           PDG_MODULO_ENTRATE_GEST.CD_CDR_ASSEGNATARIO,
           SUBSTR(PDG_MODULO_ENTRATE_GEST.CD_CDR_ASSEGNATARIO,1,3),
           SUBSTR(PDG_MODULO_ENTRATE_GEST.CD_CDR_ASSEGNATARIO,1,7),
           PDG_MODULO_ENTRATE_GEST.CD_LINEA_ATTIVITA,
           PDG_MODULO_ENTRATE_GEST.CD_ELEMENTO_VOCE,
           ELEMENTO_VOCE.DS_ELEMENTO_VOCE
     Union All
     --recupero i dati degli accertamenti registrati per lda
     SELECT
          b.ESERCIZIO ESERCIZIO,
          b.cd_centro_responsabilita CDR,
          SUBSTR(b.cd_centro_responsabilita,1,3) cds,
          SUBSTR(b.cd_centro_responsabilita,1,7) uo,
          b.cd_linea_attivita lda,
          A.CD_ELEMENTO_VOCE VOCE,
          E.DS_ELEMENTO_VOCE DS_VOCE,
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
          ACCERTAMENTO_SCADENZARIO d,
          ELEMENTO_VOCE E
     WHERE
         b.cd_cds=a.cd_cds
     AND b.ESERCIZIO=a.ESERCIZIO
     And b.esercizio_originale=a.esercizio_originale
     And b.pg_accertamento=a.pg_accertamento
     AND d.cd_cds=a.cd_cds
     AND d.ESERCIZIO=a.ESERCIZIO
     AND d.esercizio_originale=a.esercizio_originale
     AND d.pg_accertamento=a.pg_accertamento
     AND b.cd_cds=d.cd_cds
     AND b.ESERCIZIO=d.ESERCIZIO
     AND b.esercizio_originale=d.esercizio_originale
     AND b.pg_accertamento=d.pg_accertamento
     AND b.PG_accertamento_SCADENZARIO=d.PG_accertamento_SCADENZARIO
     AND E.ESERCIZIO=A.ESERCIZIO
     AND E.TI_GESTIONE='E'
     AND E.TI_APPARTENENZA='C'
     AND E.CD_ELEMENTO_VOCE=A.CD_ELEMENTO_VOCE
     AND a.CD_TIPO_DOCUMENTO_CONT='ACR'
     /* unione di una query per estrarre GLI ACCERTAMENTI RESIDUI */
     UNION ALL
     SELECT
          b.ESERCIZIO ESERCIZIO,
          b.cd_centro_responsabilita CDR,
          SUBSTR(b.cd_centro_responsabilita,1,3) cds,
          SUBSTR(b.cd_centro_responsabilita,1,7) uo,
          b.cd_linea_attivita lda,
          A.CD_ELEMENTO_VOCE VOCE,
          E.DS_ELEMENTO_VOCE DS_VOCE,
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
         ACCERTAMENTO_SCADENZARIO d,
         ELEMENTO_VOCE E
     WHERE
         b.cd_cds=a.cd_cds
     AND b.ESERCIZIO=a.ESERCIZIO
     AND b.ESERCIZIO_ORIGINALE=a.ESERCIZIO_ORIGINALE
     AND b.pg_accertamento=a.pg_accertamento
     AND d.cd_cds=a.cd_cds
     AND d.ESERCIZIO=a.ESERCIZIO
     AND d.ESERCIZIO_ORIGINALE=a.ESERCIZIO_ORIGINALE
     AND d.pg_accertamento=a.pg_accertamento
     AND b.cd_cds=d.cd_cds
     AND b.ESERCIZIO=d.ESERCIZIO
     AND b.ESERCIZIO_ORIGINALE=d.ESERCIZIO_ORIGINALE
     AND b.pg_accertamento=d.pg_accertamento
     AND b.PG_accertamento_SCADENZARIO=d.PG_accertamento_SCADENZARIO
     AND E.ESERCIZIO=A.ESERCIZIO
     AND E.TI_GESTIONE='E'
     AND E.TI_APPARTENENZA='C'
     AND E.CD_ELEMENTO_VOCE=A.CD_ELEMENTO_VOCE
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
          ASS_EV_VOCEEP.CD_ELEMENTO_VOCE ,
          ELEMENTO_VOCE.DS_ELEMENTO_VOCE,
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
     FROM
         MOVIMENTO_COAN,ASS_EV_VOCEEP, ELEMENTO_VOCE
     WHERE
         MOVIMENTO_COAN.SEZIONE='A'
     AND ASS_EV_VOCEEP.CD_VOCE_EP=MOVIMENTO_COAN.CD_VOCE_EP
     AND ASS_EV_VOCEEP.ESERCIZIO=MOVIMENTO_COAN.ESERCIZIO
     AND ASS_EV_VOCEEP.TI_APPARTENENZA='C'
     AND ASS_EV_VOCEEP.TI_GESTIONE='E'
     AND ELEMENTO_VOCE.ESERCIZIO=ASS_EV_VOCEEP.ESERCIZIO
     AND ELEMENTO_VOCE.ti_appartenenza='C'
     AND ELEMENTO_VOCE.ti_gestione='E'
     AND ELEMENTO_VOCE.cd_elemento_voce=ASS_EV_VOCEEP.cd_elemento_voce
     GROUP BY
           MOVIMENTO_COAN.ESERCIZIO,
           MOVIMENTO_COAN.CD_CENTRO_RESPONSABILITA,
           SUBSTR(MOVIMENTO_COAN.cd_centro_responsabilita,1,3),
           SUBSTR(MOVIMENTO_COAN.cd_centro_responsabilita,1,7),
           MOVIMENTO_COAN.CD_LINEA_ATTIVITA,
           ASS_EV_VOCEEP.CD_ELEMENTO_VOCE,
           ELEMENTO_VOCE.DS_ELEMENTO_VOCE
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
      VOCE,
      DS_VOCE,
      LINEA_ATTIVITA.CD_FUNZIONE,
      LINEA_ATTIVITA.CD_NATURA,
      LINEA_ATTIVITA.CD_INSIEME_LA,
      LINEA_ATTIVITA.DS_LINEA_ATTIVITA,
      CDR.CD_CDR_AFFERENZA,
      SUBSTR(CDR.cd_cdr_afferenza,1,7),
      CDR.DS_CDR;

   COMMENT ON TABLE "PRT_ANALITICA_LDA_ENT"  IS 'Vista di stampa Situazione ANALITICA Linea di Attivita Entrate / Ricavi  Anno corrente';
