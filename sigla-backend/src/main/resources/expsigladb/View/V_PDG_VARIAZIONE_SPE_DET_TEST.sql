--------------------------------------------------------
--  DDL for View V_PDG_VARIAZIONE_SPE_DET_TEST
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_PDG_VARIAZIONE_SPE_DET_TEST" ("ESERCIZIO", "CD_CENTRO_RESPONSABILITA", "DS_CDR", "CD_LINEA_ATTIVITA", "DENOMINAZIONE", "DS_LINEA_ATTIVITA", "PG_PROGETTO", "TI_GESTIONE", "CD_ELEMENTO_VOCE", "DS_ELEMENTO_VOCE", "VARIAZIONE", "ASSESTATO_SPESA", "STATO", "CD_NATURA", "DS_NATURA", "PG_VARIAZIONE_PDG", "ESERCIZIO_VARIAZIONE_PDG", "CD_CENTRO_RESPONSABILITA_CLGS", "DS_CDR_CLGS", "ID_CLASSIFICAZIONE", "DS_CLASSIFICAZIONE", "TITOLO", "CATEGORIA", "CD_MODULO", "DS_MODULO") AS 
  SELECT
--
-- Date: 13/04/2007
-- Version: 1.4
--
-- Vista di estrazione dei dati per la stampa Variazione al PdG
-- in particolare per il sub-report sulle spese
--
-- History:
--
-- Date: 06/10/2005
-- Version: 1.0
-- Creazione
--
-- Date: 03/11/2005
-- Version: 1.1
-- E' stata Aggiunta la funzione ASSESTATO_SPESA
--
-- Date: 16/05/2006
-- Version: 1.2
-- Aggiunte le nuove tabelle di gestione del PDG
--
-- Date: 09/11/2006
-- Version: 1.3
-- Aggiunta la selezione del progetto/commessa/modulo per anno
--
-- Date: 13/04/2007
-- Version: 1.4
-- Modifica: La view deve estrarre i dati da progetto_gest e non da progetto_prev
--
-- Body:
--
g.ESERCIZIO,g.CD_CENTRO_RESPONSABILITA,g.DS_CDR,g.CD_LINEA_ATTIVITA,g.DENOMINAZIONE,g.DS_LINEA_ATTIVITA,g.PG_PROGETTO,g.TI_GESTIONE,
 g.CD_ELEMENTO_VOCE,g.DS_ELEMENTO_VOCE,g.VARIAZIONE,
-- CAL_ASSESTATO.ASSESTATO_SPESA(g.ESERCIZIO,NULL,NULL,NULL,g.CD_CENTRO_RESPONSABILITA,
 --g.CD_ELEMENTO_VOCE,g.CD_LINEA_ATTIVITA,g.PG_VARIAZIONE_PDG,g.TITOLO,g.CATEGORIA,NULL,NULL,NULL,NULL,NULL,NULL) ASSESTATO_SPESA,
 0 ASSESTATO_SPESA,
  g.STATO,g.CD_NATURA,g.DS_NATURA,g.PG_VARIAZIONE_PDG,g.ESERCIZIO_PDG_VARIAZIONE,g.CD_CENTRO_RESPONSABILITA_CLGS,g.DS_CDR_CLGS,
  g.ID_CLASSIFICAZIONE,g.DS_CLASSIFICAZIONE,g.TITOLO,g.CATEGORIA,modulo.cd_progetto cd_modulo,modulo.ds_progetto ds_modulo
	FROM
        (SELECT f.ESERCIZIO,f.cd_centro_responsabilita,f.ds_cdr,f.cd_linea_attivita,Substr(LINEA_ATTIVITA.denominazione,1,80) denominazione,
         Substr(LINEA_ATTIVITA.ds_linea_attivita,1,80) ds_linea_attivita,LINEA_ATTIVITA.pg_progetto,f.ti_gestione,f.cd_elemento_voce,
		  f.ds_elemento_voce,f.variazione,f.stato,f.cd_natura,f.ds_natura,f.pg_variazione_pdg,f.esercizio_pdg_variazione,f.cd_centro_responsabilita_clgs, f.ds_cdr_clgs,
			f.id_classificazione,f.ds_classificazione,f.titolo,f.categoria
	FROM
        (SELECT e.ESERCIZIO,e.cd_centro_responsabilita,e.ds_cdr,e.cd_linea_attivita,e.ti_gestione,e.cd_elemento_voce,
		  e.ds_elemento_voce,e.variazione,e.stato,e.cd_natura,e.ds_natura,e.pg_variazione_pdg,e.esercizio_pdg_variazione, e.cd_centro_responsabilita_clgs, e.ds_cdr_clgs,
			e.id_classificazione,CLASSIFICAZIONE_VOCI.ds_classificazione,CLASSIFICAZIONE_VOCI.cd_livello1 titolo,
			CLASSIFICAZIONE_VOCI.cd_livello2 categoria
	FROM
        (SELECT d.ESERCIZIO,d.cd_centro_responsabilita,d.ds_cdr,d.cd_linea_attivita,d.ti_gestione,d.cd_elemento_voce,
		   ELEMENTO_VOCE.ds_elemento_voce,
				 d.variazione,d.stato,d.cd_natura,d.ds_natura,d.pg_variazione_pdg,d.esercizio_pdg_variazione,d.cd_centro_responsabilita_clgs, d.ds_cdr_clgs,
				 ELEMENTO_VOCE.id_classificazione
	FROM
        (SELECT c.ESERCIZIO,c.cd_centro_responsabilita,c.ds_cdr,c.cd_linea_attivita,c.ti_gestione,c.cd_elemento_voce,
				 c.variazione,c.stato,c.cd_natura,NATURA.ds_natura,c.pg_variazione_pdg,c.esercizio_pdg_variazione, c.cd_centro_responsabilita_clgs, c.ds_cdr_clgs
		FROM
               (Select b.ESERCIZIO,b.cd_centro_responsabilita,SUBSTR(CDR.ds_cdr,1,80) ds_cdr,b.cd_linea_attivita,b.ti_gestione,b.cd_elemento_voce,
		       b.variazione,b.stato,b.cd_natura,b.pg_variazione_pdg,b.esercizio_pdg_variazione, b.cd_centro_responsabilita_clgs, SUBSTR(cdr_clgs.ds_cdr,1,80) ds_cdr_clgs
		FROM
			(Select DISTINCT a.ESERCIZIO,a.cd_centro_responsabilita,a.cd_linea_attivita,a.ti_gestione,a.cd_elemento_voce,
				 a.variazione,e.stato,e.cd_natura,e.pg_variazione_pdg,e.esercizio_pdg_variazione, e.cd_centro_responsabilita_clgs
			 FROM
			  	 (SELECT PDG_PREVENTIVO_SPE_DET.ESERCIZIO,
			  	         PDG_PREVENTIVO_SPE_DET.cd_centro_responsabilita,
			  	         PDG_PREVENTIVO_SPE_DET.cd_linea_attivita,
			  	         PDG_PREVENTIVO_SPE_DET.ti_appartenenza,
				         PDG_PREVENTIVO_SPE_DET.ti_gestione,
				         PDG_PREVENTIVO_SPE_DET.cd_elemento_voce,
				         PDG_PREVENTIVO_SPE_DET.pg_spesa,
 			  		 NVL(SUM(NVL(PDG_PREVENTIVO_SPE_DET.im_rh_ccs_costi, 0) +
 			  		         NVL(PDG_PREVENTIVO_SPE_DET.im_rq_ssc_costi_odc, 0) +
 			  		         NVL(PDG_PREVENTIVO_SPE_DET.im_rr_ssc_costi_odc_altra_uo, 0) +
 			  		         NVL(PDG_PREVENTIVO_SPE_DET.im_rs_ssc_costi_ogc, 0) +
 			  		         NVL(PDG_PREVENTIVO_SPE_DET.im_rt_ssc_costi_ogc_altra_uo, 0)), 0) variazione
			   	  FROM PDG_PREVENTIVO_SPE_DET, PARAMETRI_CNR
			          WHERE PDG_PREVENTIVO_SPE_DET.ESERCIZIO = PARAMETRI_CNR.ESERCIZIO
			          AND   PARAMETRI_CNR.fl_regolamento_2006 = 'N'
			          GROUP BY PDG_PREVENTIVO_SPE_DET.ESERCIZIO,
				  	   PDG_PREVENTIVO_SPE_DET.cd_centro_responsabilita,
					   PDG_PREVENTIVO_SPE_DET.cd_linea_attivita,
					   PDG_PREVENTIVO_SPE_DET.ti_appartenenza,
					   PDG_PREVENTIVO_SPE_DET.ti_gestione,
					   PDG_PREVENTIVO_SPE_DET.cd_elemento_voce,
					   PDG_PREVENTIVO_SPE_DET.pg_spesa) a, PDG_PREVENTIVO_SPE_DET e
			WHERE a.ESERCIZIO = e.ESERCIZIO
			AND a.cd_centro_responsabilita = e.cd_centro_responsabilita
			AND a.cd_linea_attivita  = e.cd_linea_attivita
			AND a.ti_appartenenza = e.ti_appartenenza
			AND a.ti_gestione = e.ti_gestione
			AND a.cd_elemento_voce = e.cd_elemento_voce
			AND a.pg_spesa = e.pg_spesa
			AND e.categoria_dettaglio!='CAR'
			UNION ALL
			SELECT -- Distinct
			       a.ESERCIZIO,a.cd_cdr_assegnatario cd_centro_responsabilita,
			       a.cd_linea_attivita,a.ti_gestione,a.cd_elemento_voce,
			       Sum(NVL(Im_spese_gest_decentrata_int, 0) + NVL(Im_spese_gest_decentrata_est, 0) +
                               NVL(Im_spese_gest_accentrata_int, 0) + NVL(Im_spese_gest_accentrata_est, 0)) variazione,
                               var.stato,la.cd_natura,a.pg_variazione_pdg,a.ESERCIZIO esercizio_pdg_variazione,
			       a.cd_cdr_assegnatario_clgs cd_centro_responsabilita_clgs
			FROM PDG_VARIAZIONE_RIGA_GEST a, PARAMETRI_CNR, PDG_VARIAZIONE var, LINEA_ATTIVITA la
	                WHERE a.ESERCIZIO = PARAMETRI_CNR.ESERCIZIO
			AND   a.ti_gestione = 'S'
			AND   a.categoria_dettaglio!='SCR'
			AND   PARAMETRI_CNR.fl_regolamento_2006 = 'Y'
			AND   a.ESERCIZIO = var.ESERCIZIO
			AND   a.pg_variazione_pdg = var.pg_variazione_pdg
			AND   a.cd_cdr_assegnatario = la.cd_centro_responsabilita
			AND   a.cd_linea_attivita  = la.cd_linea_attivita
			GROUP By a.ESERCIZIO,a.cd_cdr_assegnatario,
			       a.cd_linea_attivita,a.ti_gestione,a.cd_elemento_voce,
			       var.stato,la.cd_natura,a.pg_variazione_pdg,a.ESERCIZIO,
			       a.cd_cdr_assegnatario_clgs) b, CDR, CDR cdr_clgs
 		WHERE b.cd_centro_responsabilita=CDR.cd_centro_responsabilita
		AND   b.cd_centro_responsabilita_clgs=cdr_clgs.cd_centro_responsabilita(+)) c, NATURA
	  WHERE c.cd_natura=NATURA.cd_natura) d, ELEMENTO_VOCE
	WHERE d.cd_elemento_voce=ELEMENTO_VOCE.cd_elemento_voce
	AND d.esercizio_pdg_variazione = ELEMENTO_VOCE.ESERCIZIO) e, CLASSIFICAZIONE_VOCI
  WHERE e.id_classificazione = CLASSIFICAZIONE_VOCI.id_classificazione) f, LINEA_ATTIVITA
 WHERE f.cd_linea_attivita= LINEA_ATTIVITA.cd_linea_attivita
 AND f.cd_centro_responsabilita=LINEA_ATTIVITA.cd_centro_responsabilita) g, progetto_gest modulo
WHERE g.pg_progetto  = modulo.pg_progetto(+)
And   (modulo.esercizio Is Null Or modulo.esercizio = g.esercizio);
