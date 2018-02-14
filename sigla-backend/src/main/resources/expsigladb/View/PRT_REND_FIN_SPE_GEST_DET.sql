--------------------------------------------------------
--  DDL for View PRT_REND_FIN_SPE_GEST_DET
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "PRT_REND_FIN_SPE_GEST_DET" ("ESERCIZIO", "CD_PROPRIO_UNITA", "CD_UNITA_ORGANIZZATIVA", "UO_DS_UNITA_ORGANIZZATIVA", "CDS_DS_UNITA_ORGANIZZATIVA", "CDR", "CD_LINEA_ATTIVITA", "PG_PROGETTO", "CD_ELEMENTO_VOCE", "CD_CLASSIFICAZIONE", "DS_CLASSIFICAZIONE", "NR_LIVELLO", "CD_LIVELLO1", "DS_LIVELLO1", "CD_LIVELLO2", "DS_LIVELLO2", "CD_LIVELLO3", "CD_LIVELLO4", "DS_LIVELLO4", "CD_LIVELLO5", "CD_LIVELLO6", "CD_LIVELLO7", "PREV_INIZIALE", "VAR_PIU_COMP", "VAR_MENO_COMP", "IM_OBBL_ACC_COMP", "PAGATO_COMP", "RES_INIZIALI", "VAR_PIU_RES_PRO", "VAR_MENO_RES_PRO", "STANZ_RES_INI", "VAR_PIU_ST_RES", "VAR_MENO_ST_RES", "RES_IMP", "PAGATO_RES_PRO", "PAGATO_RES_IMP", "ASSEST_CASSA") AS 
  SELECT a.esercizio, a.cd_proprio_unita, a.cd_unita_organizzativa,
       a.uo_ds_unita_organizzativa, a.cds_ds_unita_organizzativa,
       a.cd_centro_responsabilita, a.cd_linea_attivita,
       a.pg_progetto, a.cd_elemento_voce,
       a.cd_classificazione, a.ds_classificazione,
       a.nr_livello, a.cd_livello1, a.ds_livello1,
       a.cd_livello2, a.ds_livello2, a.cd_livello3,
       a.cd_livello4, a.ds_livello4, a.cd_livello5,
       a.cd_livello6, a.cd_livello7, 
-- STANZIAMENTO DI COMPETENZA
       cnrutl002.rf_im_stanz_iniziale_a1
                               (a.esercizio,
                                a.esercizio,
                                a.cd_centro_responsabilita,
                                a.cd_linea_attivita,
                                a.ti_appartenenza,
                                a.ti_gestione,
                                NULL,
                                a.cd_elemento_voce
                               ) prev_iniziale,
-- VARIAZIONI IN PIU' (SOLO POSITIVE)
       cnrutl002.rf_variazioni_piu
                                (a.esercizio,
                                 a.esercizio,
                                 a.cd_centro_responsabilita,
                                 a.cd_linea_attivita,
                                 a.ti_appartenenza,
                                 a.ti_gestione,
                                 NULL,
                                 a.cd_elemento_voce
                                ) var_piu_comp,
            
-- VARIAZIONI IN MENO (SOLO NEGATIVE)
            ABS
               (cnrutl002.rf_variazioni_meno (a.esercizio,
                                              a.esercizio,
                                              a.cd_centro_responsabilita,
                                              a.cd_linea_attivita,
                                              a.ti_appartenenza,
                                              a.ti_gestione,
                                              NULL,
                                              a.cd_elemento_voce
                                             )
               ) var_meno_comp,
            
-- IMPEGNATO A COMPETENZA
            cnrutl002.rf_im_obbl_acc_comp (a.esercizio,
                                           a.esercizio,
                                           a.cd_centro_responsabilita,
                                           a.cd_linea_attivita,
                                           a.ti_appartenenza,
                                           a.ti_gestione,
                                           NULL,
                                           a.cd_elemento_voce
                                          ),
            
-- PAGATO A COMPETENZA
            cnrutl002.rf_im_mandati_reversali_pro
                                              (a.esercizio,
                                               a.esercizio,
                                               a.cd_centro_responsabilita,
                                               a.cd_linea_attivita,
                                               a.ti_appartenenza,
                                               a.ti_gestione,
                                               NULL,
                                               a.cd_elemento_voce
                                              ),
            
-- RESIDUI PROPRI INIZIALI (DAI SALDI, QUINDI SENZA LE VARIAZIONI)
            cnrutl002.rf_im_obbl_res_pro (a.esercizio,
                                          NULL,
                                          a.cd_centro_responsabilita,
                                          a.cd_linea_attivita,
                                          a.ti_appartenenza,
                                          a.ti_gestione,
                                          NULL,
                                          a.cd_elemento_voce
                                         ),
            
-- VARIAZIONI PIU' AI RESIDUI PROPRI
            cnrutl002.var_piu_obbl_res_pro (a.esercizio,
                                            NULL,
                                            a.cd_centro_responsabilita,
                                            a.cd_linea_attivita,
                                            a.ti_appartenenza,
                                            a.ti_gestione,
                                            NULL,
                                            a.cd_elemento_voce
                                           ),
            
-- VARIAZIONI MENO AI RESIDUI PROPRI
            cnrutl002.var_meno_obbl_res_pro (a.esercizio,
                                             NULL,
                                             a.cd_centro_responsabilita,
                                             a.cd_linea_attivita,
                                             a.ti_appartenenza,
                                             a.ti_gestione,
                                             NULL,
                                             a.cd_elemento_voce
                                            ),
            
-- STANZIAMENTO RESIDUO INIZIALE
            cnrutl002.im_stanz_res_improprio (a.esercizio,
                                              NULL,
                                              a.cd_centro_responsabilita,
                                              a.cd_linea_attivita,
                                              a.ti_appartenenza,
                                              a.ti_gestione,
                                              NULL,
                                              a.cd_elemento_voce
                                             ),
            
-- VARIAZIONI PIU' STANZIAMENTO RESIDUO
            cnrutl002.var_piu_stanz_res_imp (a.esercizio,
                                             NULL,
                                             a.cd_centro_responsabilita,
                                             a.cd_linea_attivita,
                                             a.ti_appartenenza,
                                             a.ti_gestione,
                                             NULL,
                                             a.cd_elemento_voce
                                            ),
            
-- VARIAZIONI MENO STANZIAMENTO RESIDUO
            cnrutl002.var_meno_stanz_res_imp (a.esercizio,
                                              NULL,
                                              a.cd_centro_responsabilita,
                                              a.cd_linea_attivita,
                                              a.ti_appartenenza,
                                              a.ti_gestione,
                                              NULL,
                                              a.cd_elemento_voce
                                             ),
            
-- RESIDUI IMPROPRI (NON HANNO VARIAZIONI)
            cnrutl002.im_obbl_res_imp (a.esercizio,
                                       NULL,
                                       a.cd_centro_responsabilita,
                                       a.cd_linea_attivita,
                                       a.ti_appartenenza,
                                       a.ti_gestione,
                                       NULL,
                                       a.cd_elemento_voce
                                      ),
            
-- PAGATO A RESIDUO PROPRIO
            cnrutl002.rf_im_mandati_reversali_pro
                                              (a.esercizio,
                                               NULL,
                                               a.cd_centro_responsabilita,
                                               a.cd_linea_attivita,
                                               a.ti_appartenenza,
                                               a.ti_gestione,
                                               NULL,
                                               a.cd_elemento_voce
                                              ),
            
-- PAGATO A RESIDUO IMPROPRIO
            cnrutl002.im_mandati_reversali_imp
                                              (a.esercizio,
                                               NULL,
                                               a.cd_centro_responsabilita,
                                               a.cd_linea_attivita,
                                               a.ti_appartenenza,
                                               a.ti_gestione,
                                               NULL,
                                               a.cd_elemento_voce
                                              ),
            
-- PREVISIONE INIZIALE DI CASSA, LE VARIAZIONI NON CI SONO
            cnrutl002.im_stanz_iniziale_cassa (a.esercizio,
                                               a.esercizio,
                                               a.cd_centro_responsabilita,
                                               a.cd_linea_attivita,
                                               a.ti_appartenenza,
                                               a.ti_gestione,
                                               NULL,
                                               a.cd_elemento_voce
                                              )
from (select distinct saldi.esercizio, uo.cd_proprio_unita, cds.cd_unita_organizzativa,
            uo.ds_unita_organizzativa uo_ds_unita_organizzativa, cds.ds_unita_organizzativa cds_ds_unita_organizzativa,
            NVL (linea_attivita.cd_cdr_collegato,
                 saldi.cd_centro_responsabilita
                ) cd_centro_responsabilita,
            NVL (linea_attivita.cd_la_collegato,
                 saldi.cd_linea_attivita
                ) cd_linea_attivita,
            linea_attivita.pg_progetto, saldi.ti_appartenenza, saldi.ti_gestione, saldi.cd_elemento_voce,
            CLASS.cd_classificazione, CLASS.ds_classificazione,
            CLASS.nr_livello, CLASS.cd_livello1, liv_1.ds_classificazione ds_livello1,
            CLASS.cd_livello2, liv_2.ds_classificazione ds_livello2, CLASS.cd_livello3,
            CLASS.cd_livello4, liv_4.ds_classificazione ds_livello4, CLASS.cd_livello5,
            CLASS.cd_livello6, CLASS.cd_livello7
       FROM (SELECT DISTINCT esercizio, cd_centro_responsabilita,
                             cd_linea_attivita, ti_appartenenza, ti_gestione,
                             cd_elemento_voce
                        FROM voce_f_saldi_cdr_linea) saldi,
            elemento_voce,
            v_classificazione_voci CLASS,
            v_classificazione_voci liv_1,
            v_classificazione_voci liv_2,
            v_classificazione_voci liv_4,
            v_linea_attivita_valida linea_attivita,
            unita_organizzativa uo,
            unita_organizzativa cds
      WHERE saldi.ti_gestione = 'S'
        AND CLASS.id_classificazione = elemento_voce.id_classificazione
        AND CLASS.esercizio = liv_1.esercizio
        AND CLASS.ti_gestione = liv_1.ti_gestione
        AND CLASS.cd_livello1 = liv_1.cd_livello1
        AND liv_1.cd_livello2 IS NULL
        AND CLASS.esercizio = liv_2.esercizio
        AND CLASS.ti_gestione = liv_2.ti_gestione
        AND CLASS.cd_livello1 = liv_2.cd_livello1
        AND CLASS.cd_livello2 = liv_2.cd_livello2
        AND liv_2.cd_livello3 IS NULL
        AND CLASS.esercizio = liv_4.esercizio
        AND CLASS.ti_gestione = liv_4.ti_gestione
        AND CLASS.cd_livello1 = liv_4.cd_livello1
        AND CLASS.cd_livello2 = liv_4.cd_livello2
        AND CLASS.cd_livello3 = liv_4.cd_livello3
        AND CLASS.cd_livello4 = liv_4.cd_livello4
        AND liv_4.cd_livello5 IS NULL
        AND elemento_voce.esercizio = saldi.esercizio
        AND elemento_voce.ti_appartenenza = saldi.ti_appartenenza
        AND elemento_voce.ti_gestione = saldi.ti_gestione
        AND elemento_voce.cd_elemento_voce = saldi.cd_elemento_voce
        AND saldi.esercizio = linea_attivita.esercizio
        AND saldi.cd_centro_responsabilita =
                                       linea_attivita.cd_centro_responsabilita
        AND saldi.cd_linea_attivita = linea_attivita.cd_linea_attivita
        AND linea_attivita.pg_progetto IS NOT NULL
        AND uo.cd_unita_organizzativa =
               cnrctb020.getcduo (NVL (linea_attivita.cd_cdr_collegato,
                                       saldi.cd_centro_responsabilita
                                      )
                                 )
        AND uo.cd_unita_padre = cds.cd_unita_organizzativa) a ;
