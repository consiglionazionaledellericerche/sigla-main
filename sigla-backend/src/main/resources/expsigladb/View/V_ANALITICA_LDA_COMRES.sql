--------------------------------------------------------
--  DDL for View V_ANALITICA_LDA_COMRES
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_ANALITICA_LDA_COMRES" ("ESERCIZIO", "CDR", "CDS", "UO", "COMMESSA", "MODULO", "LDA", "VOCE", "SPESE_A1_INI", "SPESE_A1_VAR", "SPESE_A1", "RESIDUO_A1", "OBBL_A1", "DOC_OBBL_A1", "MND_OBBL_A1", "FUNZ", "NAT", "DS_CDR", "DS_COMMESSA", "DS_MODULO", "DS_LDA", "DS_VOCE", "CDR_AFF", "UO_AFF", "DIP_PRG") AS 
  SELECT
--
-- Date: 09/11/2006
-- Version: 1.1
--
-- History
--
-- Date: 26/09/2005
-- Version: 1.0
-- Gestione Impegni/Accertamenti Residui:
-- gestito il nuovo campo ESERCIZIO_ORIGINALE
--
-- Date: 09/11/2006
-- Version: 1.1
-- Aggiunta la selezione del progetto/commessa/modulo per anno
--
-- Body
--
A.ESERCIZIO,
       CDR, cds, uo,
       NVL(COMMESSA.CD_PROGETTO, 'ZZZZZZZZZZ') commessa,
       NVL(MODULO.CD_PROGETTO, 'ZZZZZZZZZZ') modulo,
       lda, voce,
       SUM(imp_spese_A1_ini) spese_a1_ini,
       SUM(imp_spese_A1_var) spese_a1_var,
       SUM(imp_spese_A1_ini + imp_spese_A1_var)	spese_a1,
       SUM(imp_residuo_A1),
       SUM(imp_obbl_A1),
       SUM(imp_doc_obbl_A1),
       SUM(imp_mnd_obbl_A1),
       LINEA_ATTIVITA.CD_FUNZIONE, LINEA_ATTIVITA.CD_NATURA,
       CDR.DS_CDR,
       NVL(COMMESSA.DS_PROGETTO, 'ZZZZZZZZZZ') DS_COMMESSA,
       NVL(MODULO.DS_PROGETTO, 'ZZZZZZZZZZ') DS_MODULO,
       LINEA_ATTIVITA.DS_LINEA_ATTIVITA, ds_voce,
       CDR.CD_CDR_AFFERENZA, NVL(SUBSTR(CDR.cd_cdr_afferenza,1,7),' '),	NVL(PROGETTO.CD_DIPARTIMENTO, 'XXX')
FROM
     (
      -- recupero i dati di previsione dai pdg	con la voce di bilancio
      SELECT a.ESERCIZIO ESERCIZIO,
	     a.cd_centro_responsabilita	CDR,
	     SUBSTR(a.cd_centro_responsabilita,1,3) cds,
	     SUBSTR(a.cd_centro_responsabilita,1,7) uo,
	     a.cd_linea_attivita lda,
	     a.CD_ELEMENTO_VOCE	voce,
	     a.ds_voce,
	     SUM(a.IM_RI_CCS_SPESE_ODC_I+a.IM_RK_CCS_SPESE_OGC_I+a.IM_RQ_SSC_COSTI_ODC_I+
		 a.IM_RS_SSC_COSTI_OGC_I+a.IM_RU_SPESE_COSTI_ALTRUI_I) imp_spese_A1_ini,
	     SUM(a.IM_RI_CCS_SPESE_ODC_V+a.IM_RK_CCS_SPESE_OGC_V+a.IM_RQ_SSC_COSTI_ODC_V+
		 a.IM_RS_SSC_COSTI_OGC_V+a.IM_RU_SPESE_COSTI_ALTRUI_V) imp_spese_A1_var,
	     SUM(a.IM_RI_CCS_SPESE_ODC_I+a.IM_RK_CCS_SPESE_OGC_I+a.IM_RQ_SSC_COSTI_ODC_I+
		 a.IM_RS_SSC_COSTI_OGC_I+a.IM_RU_SPESE_COSTI_ALTRUI_I+
		 a.IM_RI_CCS_SPESE_ODC_V+a.IM_RK_CCS_SPESE_OGC_V+a.IM_RQ_SSC_COSTI_ODC_V+
		 a.IM_RS_SSC_COSTI_OGC_V+a.IM_RU_SPESE_COSTI_ALTRUI_V) imp_spese_A1,
	     SUM(a.IM_RESIDUO) imp_residuo_A1,
	     0 imp_obbl_A1,
	     0 imp_doc_obbl_A1,
	     0 imp_mnd_obbl_A1
      FROM
	   V_ANALITICA_LDA_COMRES_PDG_SPE a
      GROUP BY
	       a.ESERCIZIO,
	       a.cd_centro_responsabilita,
	       SUBSTR(a.cd_centro_responsabilita,1,3),
	       SUBSTR(a.cd_centro_responsabilita,1,7),
	       a.cd_linea_attivita,
	       a.CD_ELEMENTO_VOCE,
	       a.ds_voce
      UNION ALL
      -- recupero i dati di previsione relativi	alla competenza	dai pdg	con la voce di bilancio
      -- sulla nuova tabella previsionale PDG_MODULO_SPESE_GEST
      SELECT PDG_MODULO_SPESE_GEST.ESERCIZIO ESERCIZIO,
	     PDG_MODULO_SPESE_GEST.CD_CDR_ASSEGNATARIO CDR,
	     SUBSTR(PDG_MODULO_SPESE_GEST.CD_CDR_ASSEGNATARIO,1,3) cds,
	     SUBSTR(PDG_MODULO_SPESE_GEST.CD_CDR_ASSEGNATARIO,1,7) uo,
	     PDG_MODULO_SPESE_GEST.CD_LINEA_ATTIVITA lda,
	     PDG_MODULO_SPESE_GEST.CD_ELEMENTO_VOCE voce,
	     ELEMENTO_VOCE.DS_ELEMENTO_VOCE,
	     SUM(DECODE(PDG_MODULO_SPESE_GEST.PG_VARIAZIONE_PDG, NULL,
			NVL(PDG_MODULO_SPESE_GEST.IM_SPESE_GEST_DECENTRATA_INT,0)+
			NVL(PDG_MODULO_SPESE_GEST.IM_SPESE_GEST_DECENTRATA_EST,0)+
			DECODE(PDG_MODULO_SPESE_GEST.CD_CDR_ASSEGNATARIO, CLASSIFICAZIONE_VOCI.CDR_ACCENTRATORE,
			       NVL(PDG_MODULO_SPESE_GEST.IM_SPESE_GEST_ACCENTRATA_INT,0)+
			       NVL(PDG_MODULO_SPESE_GEST.IM_SPESE_GEST_ACCENTRATA_EST,0), 0), 0)) imp_spese_A1_ini,
	     SUM(DECODE(PDG_MODULO_SPESE_GEST.PG_VARIAZIONE_PDG, NULL,
			0 ,NVL(PDG_MODULO_SPESE_GEST.IM_SPESE_GEST_DECENTRATA_INT,0)+
			   NVL(PDG_MODULO_SPESE_GEST.IM_SPESE_GEST_DECENTRATA_EST,0)+
			   DECODE(PDG_MODULO_SPESE_GEST.CD_CDR_ASSEGNATARIO, CLASSIFICAZIONE_VOCI.CDR_ACCENTRATORE,
				  NVL(PDG_MODULO_SPESE_GEST.IM_SPESE_GEST_ACCENTRATA_INT,0)+
				  NVL(PDG_MODULO_SPESE_GEST.IM_SPESE_GEST_ACCENTRATA_EST,0), 0)))  imp_spese_A1_var,
	     SUM(NVL(PDG_MODULO_SPESE_GEST.IM_SPESE_GEST_DECENTRATA_INT,0)+
		 NVL(PDG_MODULO_SPESE_GEST.IM_SPESE_GEST_DECENTRATA_EST,0)+
		 DECODE(PDG_MODULO_SPESE_GEST.CD_CDR_ASSEGNATARIO, CLASSIFICAZIONE_VOCI.CDR_ACCENTRATORE,
			NVL(PDG_MODULO_SPESE_GEST.IM_SPESE_GEST_ACCENTRATA_INT,0)+
			NVL(PDG_MODULO_SPESE_GEST.IM_SPESE_GEST_ACCENTRATA_EST,0))) imp_spese_A1,
	     0 imp_residuo_A1,
	     0 imp_obbl_A1,
	     0 imp_doc_obbl_A1,
	     0 imp_mnd_obbl_A1
      FROM
	   PDG_MODULO_SPESE_GEST,
	   ELEMENTO_VOCE,
	   CLASSIFICAZIONE_VOCI
      WHERE
	   (SUBSTR(PDG_MODULO_SPESE_GEST.CD_CENTRO_RESPONSABILITA,1,3) = PDG_MODULO_SPESE_GEST.CD_CDS_AREA Or
	    SUBSTR(PDG_MODULO_SPESE_GEST.CD_CDR_ASSEGNATARIO,1,3) = PDG_MODULO_SPESE_GEST.CD_CDS_AREA) AND
	   --Join tra "PDG_MODULO_SPESE_GEST" e	"ELEMENTO_VOCE"
	   PDG_MODULO_SPESE_GEST.ESERCIZIO = ELEMENTO_VOCE.ESERCIZIO AND
	   PDG_MODULO_SPESE_GEST.TI_GESTIONE = ELEMENTO_VOCE.TI_GESTIONE AND
	   PDG_MODULO_SPESE_GEST.TI_APPARTENENZA = ELEMENTO_VOCE.TI_APPARTENENZA AND
	   PDG_MODULO_SPESE_GEST.CD_ELEMENTO_VOCE = ELEMENTO_VOCE.CD_ELEMENTO_VOCE AND
	   PDG_MODULO_SPESE_GEST.ID_CLASSIFICAZIONE = CLASSIFICAZIONE_VOCI.ID_CLASSIFICAZIONE
      GROUP BY
	       PDG_MODULO_SPESE_GEST.ESERCIZIO,
	       PDG_MODULO_SPESE_GEST.CD_CDR_ASSEGNATARIO,
	       SUBSTR(PDG_MODULO_SPESE_GEST.CD_CDR_ASSEGNATARIO,1,3),
	       SUBSTR(PDG_MODULO_SPESE_GEST.CD_CDR_ASSEGNATARIO,1,7),
	       PDG_MODULO_SPESE_GEST.CD_LINEA_ATTIVITA,
	       PDG_MODULO_SPESE_GEST.CD_ELEMENTO_VOCE,
	       ELEMENTO_VOCE.DS_ELEMENTO_VOCE
      UNION ALL
      -- recupero i dati di previsione relativi	ai residui dai pdg con la voce di bilancio
      -- sulla nuova tabella previsionale VOCE_F_SALDI_CDR_LINEA
      SELECT VOCE_F_SALDI_CDR_LINEA.ESERCIZIO ESERCIZIO,
	     VOCE_F_SALDI_CDR_LINEA.CD_CENTRO_RESPONSABILITA CDR,
	     SUBSTR(VOCE_F_SALDI_CDR_LINEA.CD_CENTRO_RESPONSABILITA,1,3) cds,
	     SUBSTR(VOCE_F_SALDI_CDR_LINEA.CD_CENTRO_RESPONSABILITA,1,7) uo,
	     VOCE_F_SALDI_CDR_LINEA.CD_LINEA_ATTIVITA lda,
	     VOCE_F_SALDI_CDR_LINEA.CD_ELEMENTO_VOCE voce,
	     ELEMENTO_VOCE.DS_ELEMENTO_VOCE,
	     0 imp_spese_A1_ini,
	     0 imp_spese_A1_var,
	     0 imp_spese_A1,
	     SUM(NVL(VOCE_F_SALDI_CDR_LINEA.IM_STANZ_RES_IMPROPRIO,0)+
		 NVL(VOCE_F_SALDI_CDR_LINEA.IM_OBBL_RES_PRO,0))	imp_residuo_A1,
	     0 imp_obbl_A1,
	     0 imp_doc_obbl_A1,
	     0 imp_mnd_obbl_A1
      FROM
	   VOCE_F_SALDI_CDR_LINEA,
	   ELEMENTO_VOCE
      WHERE
	   --Join tra "VOCE_F_SALDI_CDR_LINEA" e "ELEMENTO_VOCE"
	   VOCE_F_SALDI_CDR_LINEA.TI_GESTIONE =	'S' AND
	   VOCE_F_SALDI_CDR_LINEA.ESERCIZIO = ELEMENTO_VOCE.ESERCIZIO AND
	   VOCE_F_SALDI_CDR_LINEA.TI_GESTIONE =	ELEMENTO_VOCE.TI_GESTIONE AND
	   VOCE_F_SALDI_CDR_LINEA.TI_APPARTENENZA = ELEMENTO_VOCE.TI_APPARTENENZA AND
	   VOCE_F_SALDI_CDR_LINEA.CD_ELEMENTO_VOCE = ELEMENTO_VOCE.CD_ELEMENTO_VOCE
      GROUP BY
	       VOCE_F_SALDI_CDR_LINEA.ESERCIZIO,
	       VOCE_F_SALDI_CDR_LINEA.CD_CENTRO_RESPONSABILITA,
	       SUBSTR(VOCE_F_SALDI_CDR_LINEA.CD_CENTRO_RESPONSABILITA,1,3),
	       SUBSTR(VOCE_F_SALDI_CDR_LINEA.CD_CENTRO_RESPONSABILITA,1,7),
	       VOCE_F_SALDI_CDR_LINEA.CD_LINEA_ATTIVITA,
	       VOCE_F_SALDI_CDR_LINEA.CD_ELEMENTO_VOCE,
	       ELEMENTO_VOCE.DS_ELEMENTO_VOCE
      UNION ALL
      -- recupero i dati delle obbligazioni per	lda con	indicata la voce di bilancio
      SELECT b.ESERCIZIO ESERCIZIO,
	     b.cd_centro_responsabilita	CDR,
	     SUBSTR(b.cd_centro_responsabilita,1,3) cds,
	     SUBSTR(b.cd_centro_responsabilita,1,7) uo,
	     b.cd_linea_attivita lda,
	     a.CD_ELEMENTO_VOCE	voce,
	     e.ds_elemento_voce,
	     0 imp_spese_A1_ini,
	     0 imp_spese_A1_var,
	     0 imp_spese_A1,
	     0 imp_residuo_A1,
	     (DECODE(a.ESERCIZIO,a.esercizio_competenza,b.IM_VOCE,0)) imp_obbl_A1,
	     ROUND((DECODE(a.ESERCIZIO,a.esercizio_competenza,(d.IM_ASSOCIATO_DOC_AMM*(b.im_voce*100/DECODE(d.im_scadenza,0,1,d.im_scadenza))/100) ,0)),2) imp_doc_obbl_A1,
	     ROUND((DECODE(a.ESERCIZIO,a.esercizio_competenza,(d.IM_ASSOCIATO_DOC_CONTABILE*(b.im_voce*100/DECODE(d.im_scadenza,0,1,d.im_scadenza))/100),0)),2)	imp_mnd_obbl_A1
      FROM
	   OBBLIGAZIONE	a,
	   OBBLIGAZIONE_SCAD_VOCE b,
	   OBBLIGAZIONE_SCADENZARIO d,
	   ELEMENTO_VOCE e
      WHERE
	    a.pg_obbligazione >	0 AND
	    b.cd_cds=a.cd_cds AND
	    b.ESERCIZIO=a.ESERCIZIO AND
	    b.esercizio_originale=a.esercizio_originale And
            b.pg_obbligazione=a.pg_obbligazione	AND
	    d.cd_cds=a.cd_cds AND
	    D.ESERCIZIO=A.ESERCIZIO AND
	    d.esercizio_originale=a.esercizio_originale And
            d.pg_obbligazione=a.pg_obbligazione	AND
	    B.CD_CDS=D.CD_CDS AND
	    B.ESERCIZIO=D.ESERCIZIO AND
	    B.ESERCIZIO_ORIGINALE=D.ESERCIZIO_ORIGINALE And
            B.PG_OBBLIGAZIONE=D.PG_OBBLIGAZIONE	AND
	    b.PG_OBBLIGAZIONE_SCADENZARIO=d.PG_OBBLIGAZIONE_SCADENZARIO	AND
	    e.ESERCIZIO=a.ESERCIZIO AND
	    e.ti_appartenenza='D' AND
	    e.ti_gestione='S' AND
	    e.cd_elemento_voce=a.cd_elemento_voce AND
	    e.ESERCIZIO=a.ESERCIZIO AND
	    EXISTS( SELECT 1
		    FROM CDR c
		    WHERE b.cd_centro_responsabilita = c.cd_centro_responsabilita AND
			  a.CD_UNITA_ORGANIZZATIVA = c.CD_UNITA_ORGANIZZATIVA)
      /* unione	di una query per estrarre le obbligazioni sul 000.999.000 */
      UNION ALL
      SELECT b.ESERCIZIO ESERCIZIO,
	     b.cd_centro_responsabilita	CDR,
	     SUBSTR(b.cd_centro_responsabilita,1,3) cds,
	     SUBSTR(b.cd_centro_responsabilita,1,7) uo,
	     b.cd_linea_attivita lda,
	     a.CD_ELEMENTO_VOCE	voce,
	     e.ds_elemento_voce,
	     0 imp_spese_A1_ini,
	     0 imp_spese_A1_var,
	     0 imp_spese_A1,
	     0 imp_residuo_A1,
	     (DECODE(a.ESERCIZIO,a.esercizio_competenza,b.IM_VOCE,0)) imp_obbl_A1,
	     ROUND((DECODE(a.ESERCIZIO,a.esercizio_competenza,(d.IM_ASSOCIATO_DOC_AMM*(b.im_voce*100/DECODE(d.im_scadenza,0,1,d.im_scadenza))/100) ,0)),2) imp_doc_obbl_A1,
	     ROUND((DECODE(a.ESERCIZIO,a.esercizio_competenza,(d.IM_ASSOCIATO_DOC_CONTABILE*(b.im_voce*100/DECODE(d.im_scadenza,0,1,d.im_scadenza))/100),0)),2)	imp_mnd_obbl_A1
      FROM
	   OBBLIGAZIONE	a,
	   OBBLIGAZIONE_SCAD_VOCE b,
	   OBBLIGAZIONE_SCADENZARIO d,
	   ELEMENTO_VOCE e
      WHERE
	    a.pg_obbligazione >	0 AND
	    b.cd_cds=a.cd_cds AND
	    b.ESERCIZIO=a.ESERCIZIO AND
	    b.esercizio_originale=a.esercizio_originale And
            b.pg_obbligazione=a.pg_obbligazione	AND
	    d.cd_cds=a.cd_cds AND
	    D.ESERCIZIO=A.ESERCIZIO AND
	    d.esercizio_originale=a.esercizio_originale And
            d.pg_obbligazione=a.pg_obbligazione	AND
	    B.CD_CDS=D.CD_CDS AND
	    B.ESERCIZIO=D.ESERCIZIO AND
	    B.ESERCIZIO_ORIGINALE=D.ESERCIZIO_ORIGINALE And
            B.PG_OBBLIGAZIONE=D.PG_OBBLIGAZIONE	AND
	    b.PG_OBBLIGAZIONE_SCADENZARIO=d.PG_OBBLIGAZIONE_SCADENZARIO	AND
	    e.ESERCIZIO=a.ESERCIZIO AND
	    e.ti_appartenenza='D' AND
	    e.ti_gestione='S' AND
	    e.cd_elemento_voce=a.cd_elemento_voce AND
	    SUBSTR(b.cd_centro_responsabilita,1,3)<>'999' AND
	    NOT	EXISTS ( SELECT	1
			 FROM CDR c
			 WHERE b.cd_centro_responsabilita = c.cd_centro_responsabilita AND
			       a.CD_UNITA_ORGANIZZATIVA	= c.CD_UNITA_ORGANIZZATIVA)
      /* fine inserimento nuova	query*/
      ) A, LINEA_ATTIVITA, CDR, PROGETTO_GEST MODULO, PROGETTO_GEST COMMESSA, PROGETTO_GEST PROGETTO
WHERE
      LINEA_ATTIVITA.CD_LINEA_ATTIVITA = lda AND
      LINEA_ATTIVITA.CD_CENTRO_RESPONSABILITA =	CDR AND
      CDR.CD_CENTRO_RESPONSABILITA = CDR AND
      LINEA_ATTIVITA.PG_PROGETTO = MODULO.PG_PROGETTO(+) And
      (MODULO.ESERCIZIO Is Null Or MODULO.ESERCIZIO = A.ESERCIZIO) And
      NVL(MODULO.ESERCIZIO_PROGETTO_PADRE, -999) = COMMESSA.ESERCIZIO(+) AND
      NVL(MODULO.PG_PROGETTO_PADRE, -999) = COMMESSA.PG_PROGETTO(+) AND
      NVL(COMMESSA.ESERCIZIO_PROGETTO_PADRE, -999) = PROGETTO.ESERCIZIO(+) AND
      NVL(COMMESSA.PG_PROGETTO_PADRE, -999) = PROGETTO.PG_PROGETTO (+)
GROUP BY
	 A.ESERCIZIO,
	 CDR,
	 cds,
	 uo,
	 NVL(COMMESSA.CD_PROGETTO, 'ZZZZZZZZZZ'),
	 NVL(MODULO.CD_PROGETTO, 'ZZZZZZZZZZ'),
	 LDA,
	 voce,
	 LINEA_ATTIVITA.CD_FUNZIONE,
	 LINEA_ATTIVITA.CD_NATURA,
	 CDR.DS_CDR,
	 NVL(COMMESSA.DS_PROGETTO, 'ZZZZZZZZZZ'),
	 NVL(MODULO.DS_PROGETTO, 'ZZZZZZZZZZ'),
	 LINEA_ATTIVITA.DS_LINEA_ATTIVITA,
	 ds_voce,
	 CDR.CD_CDR_AFFERENZA,
	 SUBSTR(CDR.CD_CDR_AFFERENZA,1,7),
	 Nvl(PROGETTO.CD_DIPARTIMENTO, 'XXX');
