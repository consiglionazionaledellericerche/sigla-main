--------------------------------------------------------
--  DDL for View PRT_REND_FIN_ENT_GEST
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "PRT_REND_FIN_ENT_GEST" ("ESERCIZIO", "ST_DIP_YN", "SCIENT_YN", "PESO_DIP", "NUM_DIP", "DIP", "DS_DIPARTIMENTO", "CDS", "DS_CDS", "CDR", "CD_LINEA_ATTIVITA", "CD_ELEMENTO_VOCE", "CD_CLASSIFICAZIONE", "DS_CLASSIFICAZIONE", "NR_LIVELLO", "CD_LIVELLO1", "CD_LIVELLO1_ROM", "DS_LIVELLO1", "CD_LIVELLO2", "DS_LIVELLO2", "CD_LIVELLO3", "DS_LIVELLO3", "CD_LIVELLO4", "CD_LIVELLO5", "CD_LIVELLO6", "CD_LIVELLO7", "PREV_INIZIALE", "VAR_PIU_COMP", "VAR_MENO_COMP", "RISCOSSIONI_COMP", "IM_OBBL_ACC_COMP", "RESIDUI_INIZIALI", "VAR_PIU_RES", "VAR_MENO_RES", "RESIDUI_FINALI", "RISCOSSIONI_RES", "ASSEST_CASSA") AS 
  SELECT prt_rend_fin_ent_gest_det.esercizio,
          
          -- ST_DIP_YN
          DECODE (progetto.cd_dipartimento,
                  'SAC', DECODE (cd_livello1, '4', 'N', '5', 'N', 'Y'),
                  'Y'
                 ),
          
          -- SCIENT_YN
          DECODE (progetto.cd_dipartimento, 'SAC', 'N', 'Y') scient_yn,
          
          -- NEW Peso dip (anche 14 per Fondi e Partite di Giro)
          DECODE (progetto.cd_dipartimento,
                  'SAC', DECODE (cd_livello1,
                                 '4', '14',
                                 '5', '14',
                                 LPAD (TO_CHAR (peso), 2, '0')
                                ),
                  LPAD (TO_CHAR (peso), 2, '0')
                 ),
          
          -- NUM_DIP DA 01 A 13 (PER NUMERAZIONE)
          LPAD (TO_CHAR (peso), 2, '0'),
          DECODE (progetto.cd_dipartimento,
                  'SAC', DECODE (cd_livello1,
                                 '4', 'SAC_FONDI_PGIRO',
                                 '5', 'SAC_FONDI_PGIRO',
                                 'SAC'
                                ),
                  progetto.cd_dipartimento
                 ),
          p.ds_dipartimento,
          DECODE (progetto.cd_dipartimento,
                  'SAC', prt_rend_fin_ent_gest_det.cd_proprio_unita,
                  prt_rend_fin_ent_gest_det.cd_unita_organizzativa
                 ) proprio_uo,
          DECODE
             (progetto.cd_dipartimento,
              'SAC', prt_rend_fin_ent_gest_det.uo_ds_unita_organizzativa,
              prt_rend_fin_ent_gest_det.cds_ds_unita_organizzativa
             ) des_proprio,
          cdr, cd_linea_attivita, cd_elemento_voce, cd_classificazione,
          ds_classificazione, nr_livello, cd_livello1,
          TRIM (TO_CHAR (cd_livello1, 'RM')), ds_livello1, cd_livello2,
          ds_livello2, cd_livello3, ds_livello3, cd_livello4, cd_livello5,
          cd_livello6, cd_livello7,
                                   -- STANZIAMENTO DI COMPETENZA
                                   prev_iniziale,
          
          -- VARIAZIONI IN PIU' (TOTALI, COMPRESE LE NEGATIVE)
          DECODE (var_piu_comp - var_meno_comp,
                  ABS (var_piu_comp - var_meno_comp), var_piu_comp
                   - var_meno_comp,
                  0
                 ),
          
          -- VARIAZIONI IN MENO (TOTALI, COMPRESE LE POSITIVE)
          DECODE (var_piu_comp - var_meno_comp,
                  ABS (var_piu_comp - var_meno_comp), 0,
                  ABS (var_piu_comp - var_meno_comp)
                 ),
          
          -- RISCOSSO A COMPETENZA
          riscossioni_comp,
                           -- ACCERTATO A COMPETENZA
                           im_obbl_acc_comp,
          
          -- RESIDUI_INIZIALI
          residui_assestato - var_piu_res + var_meno_res,
                                                         -- VARIAZIONI IN PIU' AI RESIDUI (SOLO 2006)
                                                         var_piu_res,
          
          -- VARIAZIONI IN MENO AI RESIDUI (SOLO 2006)
          var_meno_res,
                       -- RESIDUI FINALI
                       residui_assestato,
                                         -- RISCOSSIONI A RESIDUO
                                         riscossioni_res, assest_cassa
     FROM parametri_cnr par,
          prt_rend_fin_ent_gest_det,
          dipartimento_peso p,
          progetto_gest progetto,
          progetto_gest com,
          progetto_gest modu
    WHERE par.esercizio = prt_rend_fin_ent_gest_det.esercizio
      AND par.fl_nuovo_pdg = 'N'
      AND prt_rend_fin_ent_gest_det.pg_progetto = modu.pg_progetto
      AND prt_rend_fin_ent_gest_det.esercizio = modu.esercizio
      AND modu.esercizio_progetto_padre = com.esercizio
      AND modu.pg_progetto_padre = com.pg_progetto
      AND com.esercizio_progetto_padre = progetto.esercizio
      AND com.pg_progetto_padre = progetto.pg_progetto
      AND progetto.esercizio = p.esercizio(+)
      AND progetto.cd_dipartimento = p.cd_dipartimento(+)
   UNION ALL
   SELECT prt_rend_fin_ent_gest_det.esercizio,
          
          -- ST_DIP_YN
          DECODE (progetto.cd_dipartimento,
                  'SAC', DECODE (cd_livello1, '4', 'N', '5', 'N', 'Y'),
                  'Y'
                 ),
          
          -- SCIENT_YN
          DECODE (progetto.cd_dipartimento, 'SAC', 'N', 'Y') scient_yn,
          
          -- NEW Peso dip (anche 14 per Fondi e Partite di Giro)
          DECODE (progetto.cd_dipartimento,
                  'SAC', DECODE (cd_livello1,
                                 '4', '14',
                                 '5', '14',
                                 LPAD (TO_CHAR (peso), 2, '0')
                                ),
                  LPAD (TO_CHAR (peso), 2, '0')
                 ),
          
          -- NUM_DIP DA 01 A 13 (PER NUMERAZIONE)
          LPAD (TO_CHAR (peso), 2, '0'),
          DECODE (progetto.cd_dipartimento,
                  'SAC', DECODE (cd_livello1,
                                 '4', 'SAC_FONDI_PGIRO',
                                 '5', 'SAC_FONDI_PGIRO',
                                 'SAC'
                                ),
                  progetto.cd_dipartimento
                 ),
          p.ds_dipartimento,
          DECODE (progetto.cd_dipartimento,
                  'SAC', prt_rend_fin_ent_gest_det.cd_proprio_unita,
                  prt_rend_fin_ent_gest_det.cd_unita_organizzativa
                 ) proprio_uo,
          DECODE
             (progetto.cd_dipartimento,
              'SAC', prt_rend_fin_ent_gest_det.uo_ds_unita_organizzativa,
              prt_rend_fin_ent_gest_det.cds_ds_unita_organizzativa
             ) des_proprio,
          cdr, cd_linea_attivita, cd_elemento_voce, cd_classificazione,
          ds_classificazione, nr_livello, cd_livello1,
          TRIM (TO_CHAR (cd_livello1, 'RM')), ds_livello1, cd_livello2,
          ds_livello2, cd_livello3, ds_livello3, cd_livello4, cd_livello5,
          cd_livello6, cd_livello7,
                                   -- STANZIAMENTO DI COMPETENZA
                                   prev_iniziale,
          
          -- VARIAZIONI IN PIU' (TOTALI, COMPRESE LE NEGATIVE)
          DECODE (var_piu_comp - var_meno_comp,
                  ABS (var_piu_comp - var_meno_comp), var_piu_comp
                   - var_meno_comp,
                  0
                 ),
          
          -- VARIAZIONI IN MENO (TOTALI, COMPRESE LE POSITIVE)
          DECODE (var_piu_comp - var_meno_comp,
                  ABS (var_piu_comp - var_meno_comp), 0,
                  ABS (var_piu_comp - var_meno_comp)
                 ),
          
          -- RISCOSSO A COMPETENZA
          riscossioni_comp,
                           -- ACCERTATO A COMPETENZA
                           im_obbl_acc_comp,
          
          -- RESIDUI_INIZIALI
          residui_assestato - var_piu_res + var_meno_res,
                                                         -- VARIAZIONI IN PIU' AI RESIDUI (SOLO 2006)
                                                         var_piu_res,
          
          -- VARIAZIONI IN MENO AI RESIDUI (SOLO 2006)
          var_meno_res,
                       -- RESIDUI FINALI
                       residui_assestato,
                                         -- RISCOSSIONI A RESIDUO
                                         riscossioni_res, assest_cassa
     FROM parametri_cnr par,
          prt_rend_fin_ent_gest_det,
          dipartimento_peso p,
          progetto_gest progetto,
          progetto_gest com
    WHERE par.esercizio = prt_rend_fin_ent_gest_det.esercizio
      AND par.fl_nuovo_pdg = 'Y'
      AND prt_rend_fin_ent_gest_det.pg_progetto = com.pg_progetto
      AND prt_rend_fin_ent_gest_det.esercizio = com.esercizio
      AND com.esercizio_progetto_padre = progetto.esercizio
      AND com.pg_progetto_padre = progetto.pg_progetto
      AND progetto.esercizio = p.esercizio(+)
      AND progetto.cd_dipartimento = p.cd_dipartimento(+) ;
