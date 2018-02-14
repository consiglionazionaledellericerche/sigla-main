--------------------------------------------------------
--  DDL for View V_GAE_TOTALE_STORICO_SPE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_GAE_TOTALE_STORICO_SPE" ("CDR", "CD_LINEA_ATTIVITA", "DENOMINAZIONE", "CD_RESPONSABILE_TERZO", "DS_TERZO", "CD_MODULO", "DS_MODULO", "PG_PROGETTO", "CD_ELEMENTO_VOCE", "STANZIAMENTO_ASSESTATO_TOT", "IMPEGNATO", "DISPONIBILITA_IMPEGNARE", "PAGATO", "DA_PAGARE") AS 
  SELECT   cdr, cd_linea_attivita, denominazione, cd_responsabile_terzo,
            ds_terzo, cd_modulo, ds_modulo, pg_progetto, cd_elemento_voce,
            SUM (stanziamento_assestato_tot), SUM (impegnato),
            SUM (stanziamento_assestato_tot - impegnato
                ) disponibilita_impegnare,
            SUM (pagato), SUM (impegnato - pagato) da_pagare
       FROM (            ----------------ricostruzione dei reidui-------------
             SELECT   pdg_residuo_det.esercizio, 2005 esercizio_res,
                      pdg_residuo_det.cd_cdr_linea cdr,
                      pdg_residuo_det.cd_linea_attivita,
                      linea_attivita.denominazione,
                      linea_attivita.cd_responsabile_terzo,
                      terzo.denominazione_sede ds_terzo,
                      progetto.cd_progetto cd_modulo,
                      progetto.ds_progetto ds_modulo,
                      linea_attivita.pg_progetto,
                      pdg_residuo_det.cd_elemento_voce,
                      SUM
                         (pdg_residuo_det.im_residuo
                         ) stanziamento_assestato_tot,
                      0 impegnato, 0 pagato
                 FROM pdg_residuo_det, linea_attivita, progetto, terzo
                WHERE pdg_residuo_det.cd_cdr_linea =
                                       linea_attivita.cd_centro_responsabilita
                  AND pdg_residuo_det.cd_linea_attivita =
                                              linea_attivita.cd_linea_attivita
                  AND progetto.pg_progetto = linea_attivita.pg_progetto
                  AND progetto.tipo_fase = 'G'
                  AND progetto.esercizio =
                         (SELECT MAX (pro.esercizio)
                            FROM progetto pro
                           WHERE pro.pg_progetto = linea_attivita.pg_progetto
                             AND pro.tipo_fase = progetto.tipo_fase)
                  AND linea_attivita.cd_responsabile_terzo = terzo.cd_terzo
                  AND pdg_residuo_det.stato != 'A'
             GROUP BY pdg_residuo_det.esercizio,
                      2005,
                      pdg_residuo_det.cd_cdr_linea,
                      pdg_residuo_det.cd_linea_attivita,
                      linea_attivita.denominazione,
                      linea_attivita.cd_responsabile_terzo,
                      terzo.denominazione_sede,
                      progetto.cd_progetto,
                      progetto.ds_progetto,
                      linea_attivita.pg_progetto,
                      pdg_residuo_det.cd_elemento_voce
             UNION
             ---------SALDI dal 2006 -----------------
             SELECT   voce_f_saldi_cdr_linea.esercizio,
                      voce_f_saldi_cdr_linea.esercizio_res,
                      voce_f_saldi_cdr_linea.cd_centro_responsabilita cdr,
                      voce_f_saldi_cdr_linea.cd_linea_attivita,
                      linea_attivita.denominazione,
                      linea_attivita.cd_responsabile_terzo,
                      terzo.denominazione_sede ds_terzo,
                      progetto.cd_progetto cd_modulo,
                      progetto.ds_progetto ds_modulo,
                      linea_attivita.pg_progetto,
                      voce_f_saldi_cdr_linea.cd_elemento_voce,
                      SUM
                         (  voce_f_saldi_cdr_linea.im_stanz_iniziale_a1
                          + voce_f_saldi_cdr_linea.variazioni_piu
                          - voce_f_saldi_cdr_linea.variazioni_meno
                          + voce_f_saldi_cdr_linea.var_piu_stanz_res_imp
                          - voce_f_saldi_cdr_linea.var_meno_stanz_res_imp
                         ) stanziamento_assestato_tot,
                      SUM
                         (  voce_f_saldi_cdr_linea.im_obbl_acc_comp
                          + voce_f_saldi_cdr_linea.im_obbl_res_imp
                          + voce_f_saldi_cdr_linea.var_piu_obbl_res_pro
                          - voce_f_saldi_cdr_linea.var_meno_obbl_res_pro
                         ) impegnato,
                      SUM
                         (  voce_f_saldi_cdr_linea.im_mandati_reversali_imp
                          + voce_f_saldi_cdr_linea.im_mandati_reversali_pro
                         ) pagato
                 FROM voce_f_saldi_cdr_linea, linea_attivita, progetto, terzo
                WHERE voce_f_saldi_cdr_linea.cd_centro_responsabilita =
                                       linea_attivita.cd_centro_responsabilita
                  AND voce_f_saldi_cdr_linea.cd_linea_attivita =
                                              linea_attivita.cd_linea_attivita
                  AND progetto.pg_progetto = linea_attivita.pg_progetto
                  AND progetto.tipo_fase = 'G'
                  AND progetto.esercizio =
                         (SELECT MAX (pro.esercizio)
                            FROM progetto pro
                           WHERE pro.pg_progetto = linea_attivita.pg_progetto
                             AND pro.tipo_fase = progetto.tipo_fase)
                  AND linea_attivita.cd_responsabile_terzo = terzo.cd_terzo
                  AND voce_f_saldi_cdr_linea.ti_gestione = 'S'
                  AND voce_f_saldi_cdr_linea.ti_appartenenza = 'D'
                  AND voce_f_saldi_cdr_linea.esercizio >= 2005
             GROUP BY voce_f_saldi_cdr_linea.esercizio,
                      voce_f_saldi_cdr_linea.esercizio_res,
                      voce_f_saldi_cdr_linea.cd_centro_responsabilita,
                      voce_f_saldi_cdr_linea.cd_linea_attivita,
                      linea_attivita.denominazione,
                      linea_attivita.cd_responsabile_terzo,
                      terzo.denominazione_sede,
                      progetto.cd_progetto,
                      progetto.ds_progetto,
                      linea_attivita.pg_progetto,
                      voce_f_saldi_cdr_linea.cd_elemento_voce)
   GROUP BY cdr,
            cd_linea_attivita,
            denominazione,
            cd_responsabile_terzo,
            ds_terzo,
            cd_modulo,
            ds_modulo,
            pg_progetto,
            cd_elemento_voce
     HAVING SUM (stanziamento_assestato_tot) != 0
         OR SUM (impegnato) != 0
         OR SUM (pagato) != 0;
