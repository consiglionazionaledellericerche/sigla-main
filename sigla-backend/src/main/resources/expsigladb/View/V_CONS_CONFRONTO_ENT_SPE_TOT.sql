--------------------------------------------------------
--  DDL for View V_CONS_CONFRONTO_ENT_SPE_TOT
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_CONS_CONFRONTO_ENT_SPE_TOT" ("ESERCIZIO_ORIGINALE", "ESERCIZIO", "CDS", "CD_CENTRO_RESPONSABILITA", "DS_CDR", "CD_LINEA_ATTIVITA", "DENOMINAZIONE", "CD_NATURA", "CD_MODULO", "DS_MODULO", "TI_APPARTENENZA", "TI_GESTIONE", "CD_ELEMENTO_VOCE", "DS_ELEMENTO_VOCE", "IM_STANZ_INIZIALE_A1", "VARIAZIONI_PIU", "VARIAZIONI_MENO", "ASSESTATO_COMP", "IM_OBBL_ACC_COMP", "IM_ASS_DOC_AMM_SPE", "IM_ASS_DOC_AMM_ETR", "IM_MANDATI_REVERSALI_PRO") AS 
  SELECT   esercizio_ORIGINALE,esercizio, cds, cd_centro_responsabilita, ds_cdr,
            cd_linea_attivita, denominazione, cd_natura, cd_modulo, ds_modulo,
            ti_appartenenza, ti_gestione, cd_elemento_voce, ds_elemento_voce,
            SUM (im_stanz_iniziale_a1), SUM (variazioni_piu),
            SUM (variazioni_meno), SUM (assestato_comp),
            SUM (im_obbl_acc_comp), SUM (im_ass_doc_amm_spe),
            SUM (im_ass_doc_amm_etr), SUM (im_mandati_reversali_pro)
       FROM (SELECT   esercizio esercizio_ORIGINALE,esercizio, cds, cd_centro_responsabilita, ds_cdr,
                      cd_linea_attivita, denominazione, cd_natura, cd_modulo,
                      ds_modulo, ti_appartenenza, ti_gestione,
                      cd_elemento_voce, ds_elemento_voce,
                      SUM (im_stanz_iniziale_a1) im_stanz_iniziale_a1,
                      SUM (variazioni_piu) variazioni_piu,
                      SUM (variazioni_meno) variazioni_meno,
                      SUM (assestato_comp) assestato_comp,
                      SUM (im_obbl_acc_comp) im_obbl_acc_comp,
                      SUM (im_ass_doc_amm_spe) im_ass_doc_amm_spe,
                      SUM (im_ass_doc_amm_etr) im_ass_doc_amm_etr,
                      SUM (im_mandati_reversali_pro) im_mandati_reversali_pro
                 FROM v_sit_analitica_gae_competenza
             GROUP BY esercizio,esercizio,
                      cds,
                      cd_centro_responsabilita,
                      ds_cdr,
                      cd_linea_attivita,
                      denominazione,
                      cd_natura,
                      cd_modulo,
                      ds_modulo,
                      ti_appartenenza,
                      ti_gestione,
                      cd_elemento_voce,
                      ds_elemento_voce
               HAVING SUM (im_stanz_iniziale_a1) != 0
                   OR SUM (variazioni_piu) != 0
                   OR SUM (variazioni_meno) != 0
                   OR SUM (assestato_comp) != 0
                   OR SUM (im_obbl_acc_comp) != 0
                   OR SUM (im_ass_doc_amm_spe) != 0
                   OR SUM (im_ass_doc_amm_etr) != 0
                   OR SUM (im_mandati_reversali_pro) != 0
	UNION ALL
		SELECT   esercizio_ORIGINALE,esercizio, cds, cd_centro_responsabilita, ds_cdr,
                      cd_linea_attivita, denominazione, cd_natura, cd_modulo,
                      ds_modulo, ti_appartenenza, ti_gestione,
                      cd_elemento_voce, ds_elemento_voce,
                      SUM (im_stanz_iniziale_a1) im_stanz_iniziale_a1,
                      SUM (variazioni_piu) variazioni_piu,
                      SUM (variazioni_meno) variazioni_meno,
                      SUM (assestato_comp) assestato_comp,
                      SUM (im_obbl_acc_comp) im_obbl_acc_comp,
                      SUM (im_ass_doc_amm_spe) im_ass_doc_amm_spe,
                      SUM (im_ass_doc_amm_etr) im_ass_doc_amm_etr,
                      SUM (im_mandati_reversali_pro) im_mandati_reversali_pro
                 FROM v_sit_analitica_gae_RESIDUI
             GROUP BY esercizio_ORIGINALE,esercizio,
                      cds,
                      cd_centro_responsabilita,
                      ds_cdr,
                      cd_linea_attivita,
                      denominazione,
                      cd_natura,
                      cd_modulo,
                      ds_modulo,
                      ti_appartenenza,
                      ti_gestione,
                      cd_elemento_voce,
                      ds_elemento_voce
               HAVING SUM (im_stanz_iniziale_a1) != 0
                   OR SUM (variazioni_piu) != 0
                   OR SUM (variazioni_meno) != 0
                   OR SUM (assestato_comp) != 0
                   OR SUM (im_obbl_acc_comp) != 0
                   OR SUM (im_ass_doc_amm_spe) != 0
                   OR SUM (im_ass_doc_amm_etr) != 0
                   OR SUM (im_mandati_reversali_pro) != 0  )
   GROUP BY esercizio_ORIGINALE,esercizio,
            cds,
            cd_centro_responsabilita,
            ds_cdr,
            cd_linea_attivita,
            denominazione,
            cd_natura,
            cd_modulo,
            ds_modulo,
            ti_appartenenza,
            ti_gestione,
            cd_elemento_voce,
            ds_elemento_voce;
