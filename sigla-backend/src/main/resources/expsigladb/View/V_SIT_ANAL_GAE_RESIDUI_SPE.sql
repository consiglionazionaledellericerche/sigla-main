--------------------------------------------------------
--  DDL for View V_SIT_ANAL_GAE_RESIDUI_SPE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_SIT_ANAL_GAE_RESIDUI_SPE" ("ESERCIZIO", "CDS", "UO", "CD_CENTRO_RESPONSABILITA", "DS_CDR", "CD_LINEA_ATTIVITA", "DS_LINEA_ATTIVITA", "CD_NATURA", "CD_PROGETTO", "DS_PROGETTO", "CD_COMMESSA", "DS_COMMESSA", "CD_MODULO", "DS_MODULO", "TI_APPARTENENZA", "TI_GESTIONE", "CD_ELEMENTO_VOCE", "CD_VOCE", "DS_ELEMENTO_VOCE", "ESERCIZIO_RES", "RESIDUI_INIZIALI", "VAR_STANZ_RES_PIU", "VAR_STANZ_RES_MENO_STO", "VAR_STANZ_RES_MENO_ECO", "TOTALE", "VINCOLI", "ASSUNTI", "DA_ASSUMERE", "LIQUIDATI", "PAGATI", "DA_PAGARE", "RESIDUI_FINALI") AS 
  SELECT
--
-- Date: 09/11/2006
-- Version: 1.1
--
-- Vista SITUAZIONE Analitica GAE sui residui di spesa
--
-- History:
--
-- Date: 01/01/2006
-- Version: 1.0
-- Creazione
--
-- Date: 09/11/2006
-- Version: 1.1
-- Aggiunta la selezione del progetto/commessa/modulo per anno
--
-- Body:
--
          esercizio, SUBSTR (cds, 1, 50), SUBSTR (uo, 1, 50),
          cd_centro_responsabilita, ds_cdr, cd_linea_attivita,
          ds_linea_attivita, cd_natura, cd_progetto, ds_progetto, cd_commessa,
          ds_commessa, cd_modulo, ds_modulo, ti_appartenenza, ti_gestione,
          cd_elemento_voce, cd_voce, ds_elemento_voce, esercizio_res,
          
-- RESIDUI_INIZIALI
            NVL (stanz_res_ini, 0)
          + NVL (res_pro_ini, 0)
          + NVL (res_imp_ribaltati, 0) residui_iniziali,
          
-- VAR_STANZ_RES_PIU
          NVL (var_stanz_res_piu, 0) var_stanz_res_piu,
          
-- VAR_STANZ_RES_MENO_STO
          NVL (var_stanz_res_meno_sto, 0) var_stanz_res_meno_sto,
          
-- VAR_STANZ_RES_MENO_ECO
          NVL (var_stanz_res_meno_eco, 0) var_stanz_res_meno_eco,
            
-- TOTALE
            (  NVL (stanz_res_ini, 0)
             + NVL (res_pro_ini, 0)
             + NVL (res_imp_ribaltati, 0)
             + NVL (var_stanz_res_piu, 0)
            )
          - (NVL (var_stanz_res_meno_sto, 0) + NVL (var_stanz_res_meno_eco, 0)
            ) totale,
          
-- VINCOLI
          NVL (res_vincoli, 0) vincoli,

-- ASSUNTI
            NVL (res_pro_ini, 0)
          + NVL (var_res_pro_piu, 0)
          - NVL (var_res_pro_meno, 0)
          + NVL (res_imp_ribaltati, 0)
          + NVL (res_imp_em_ese, 0) assunti,
            
-- DA_ASSUMERE
            (  (  NVL (stanz_res_ini, 0)
                + NVL (res_pro_ini, 0)
                + NVL (res_imp_ribaltati, 0)
                + NVL (var_stanz_res_piu, 0)
               )
             - (  NVL (var_stanz_res_meno_sto, 0)
                + NVL (var_stanz_res_meno_eco, 0)
               )
            )
          - (  NVL (res_pro_ini, 0)
             + NVL (var_res_pro_piu, 0)
             - NVL (var_res_pro_meno, 0)
             + NVL (res_imp_ribaltati, 0)
             + NVL (res_imp_em_ese, 0)
            ) 
          - NVL (res_vincoli, 0) da_assumere,
          
-- LIQUIDATI
            NVL (liquidato_pro, 0)
          + NVL (res_imp_rib_liq, 0)
          + NVL (res_imp_em_ese_liq, 0) liquidati,
          
-- PAGATI
            NVL (pagato_pro, 0)
          + NVL (res_imp_rib_pag, 0)
          + NVL (res_imp_em_ese_pag, 0) pagati,
            
-- DA_PAGARE
            (  NVL (res_pro_ini, 0)
             + NVL (var_res_pro_piu, 0)
             - NVL (var_res_pro_meno, 0)
             + NVL (res_imp_ribaltati, 0)
             + NVL (res_imp_em_ese, 0)
            )
          - (  NVL (pagato_pro, 0)
             + NVL (res_imp_rib_pag, 0)
             + NVL (res_imp_em_ese_pag, 0)
            ) da_pagare,
            
-- RESIDUI_FINALI
            (  (  NVL (stanz_res_ini, 0)
                + NVL (res_pro_ini, 0)
                + NVL (res_imp_ribaltati, 0)
                + NVL (var_stanz_res_piu, 0)
               )
             - (  NVL (var_stanz_res_meno_sto, 0)
                + NVL (var_stanz_res_meno_eco, 0)
               )
            )
          - (  NVL (res_pro_ini, 0)
             + NVL (var_res_pro_piu, 0)
             - NVL (var_res_pro_meno, 0)
             + NVL (res_imp_ribaltati, 0)
             + NVL (res_imp_em_ese, 0)
            )
          + (  (  NVL (res_pro_ini, 0)
                + NVL (var_res_pro_piu, 0)
                - NVL (var_res_pro_meno, 0)
                + NVL (res_imp_ribaltati, 0)
                + NVL (res_imp_em_ese, 0)
               )
             - (  NVL (pagato_pro, 0)
                + NVL (res_imp_rib_pag, 0)
                + NVL (res_imp_em_ese_pag, 0)
               )
            ) residui_finali
     FROM v_sit_gae_residui_spesa ;
