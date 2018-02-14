--------------------------------------------------------
--  DDL for View V_SIT_GAE_RESIDUI_ENTRATA
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_SIT_GAE_RESIDUI_ENTRATA" ("ESERCIZIO", "CDS", "UO", "CD_CENTRO_RESPONSABILITA", "DS_CDR", "CD_LINEA_ATTIVITA", "DS_LINEA_ATTIVITA", "CD_NATURA", "CD_PROGETTO", "DS_PROGETTO", "CD_COMMESSA", "DS_COMMESSA", "CD_MODULO", "DS_MODULO", "TI_APPARTENENZA", "TI_GESTIONE", "CD_ELEMENTO_VOCE", "CD_VOCE", "DS_ELEMENTO_VOCE", "ESERCIZIO_RES", "RES_PRO_INI", "VAR_RES_PRO_PIU", "VAR_RES_PRO_MENO", "TOTALE", "LIQUIDATO_PRO", "INCASSATO_PRO", "DA_INCASSARE") AS 
  SELECT
--
-- Date: 09/11/2006
-- Version: 1.1
--
-- Vista SITUAZIONE GAE sui residui di entrata
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
          vs.esercizio,
          SUBSTR (cnrutl001.getcdsfromcdr (vs.cd_centro_responsabilita),
                  1,
                  50
                 ) cds,
          SUBSTR (cnrctb020.getcduo (vs.cd_centro_responsabilita), 1, 50) uo,
          vs.cd_centro_responsabilita, cdr.ds_cdr, vs.cd_linea_attivita,
          DECODE (la.denominazione,
                  NULL, DECODE (la.ds_linea_attivita,
                                NULL, 'NESSUNA DESCRIZIONE',
                                la.ds_linea_attivita
                               ),
                  la.denominazione
                 ),
          la.cd_natura, prog.cd_progetto, prog.ds_progetto,
          comm.cd_progetto cd_commessa, comm.ds_progetto ds_commessa,
          modu.cd_progetto cd_modulo, modu.ds_progetto ds_modulo,
          vs.ti_appartenenza, vs.ti_gestione, vs.cd_elemento_voce, vs.cd_voce,
          e.ds_elemento_voce, vs.esercizio_res,
          
-- RES_PRO_INI
          NVL (vs.im_obbl_res_pro, 0) res_pro_ini,
          
-- VAR_RES_PRO_PIU
          NVL (vs.var_piu_obbl_res_pro, 0) var_res_pro_piu,
          
-- VAR_RES_PRO_MENO
          NVL (vs.var_meno_obbl_res_pro, 0) var_res_pro_meno,
          
-- TOTALE
            NVL (vs.im_obbl_res_pro, 0)
          + NVL (vs.var_piu_obbl_res_pro, 0)
          - NVL (vs.var_meno_obbl_res_pro, 0) totale,
          
-- LIQUIDATO_PRO
          cnrutl002.liquidato_pro (vs.esercizio,
                                   vs.esercizio_res,
                                   vs.cd_centro_responsabilita,
                                   vs.cd_linea_attivita,
                                   vs.ti_appartenenza,
                                   vs.ti_gestione,
                                   vs.cd_voce
                                  ) liquidato_pro,
          
-- INCASSATO_PRO
          NVL (im_mandati_reversali_pro, 0) pagato_pro,
            
-- DA_INCASSARE
            (  NVL (vs.im_obbl_res_pro, 0)
             + NVL (vs.var_piu_obbl_res_pro, 0)
             - NVL (vs.var_meno_obbl_res_pro, 0)
            )
          - NVL (im_mandati_reversali_pro, 0) da_incassare
     FROM voce_f_saldi_cdr_linea vs,
          linea_attivita la,
          progetto_gest modu,
          progetto_gest comm,
          progetto_gest prog,
          voce_f voce,
          cdr,
          elemento_voce e,
          parametri_cnr
    WHERE parametri_cnr.esercizio = vs.esercizio
      AND parametri_cnr.fl_nuovo_pdg = 'N'
      AND vs.esercizio > esercizio_res
      AND vs.ti_gestione = 'E'
      AND vs.cd_centro_responsabilita = la.cd_centro_responsabilita
      AND vs.cd_linea_attivita = la.cd_linea_attivita
      AND la.pg_progetto = modu.pg_progetto
      AND modu.esercizio = vs.esercizio
      AND modu.esercizio_progetto_padre = comm.esercizio
      AND modu.pg_progetto_padre = comm.pg_progetto
      AND comm.esercizio_progetto_padre = prog.esercizio
      AND comm.pg_progetto_padre = prog.pg_progetto
      AND vs.esercizio = voce.esercizio
      AND vs.ti_appartenenza = voce.ti_appartenenza
      AND vs.ti_gestione = voce.ti_gestione
      AND vs.cd_voce = voce.cd_voce
      AND vs.cd_centro_responsabilita = cdr.cd_centro_responsabilita
      AND e.esercizio = voce.esercizio
      AND e.ti_appartenenza = voce.ti_appartenenza
      AND e.ti_gestione = voce.ti_gestione
      AND e.cd_elemento_voce = voce.cd_elemento_voce
Union All
   SELECT
          vs.esercizio,
          SUBSTR (cnrutl001.getcdsfromcdr (vs.cd_centro_responsabilita),
                  1,
                  50
                 ) cds,
          SUBSTR (cnrctb020.getcduo (vs.cd_centro_responsabilita), 1, 50) uo,
          vs.cd_centro_responsabilita, cdr.ds_cdr, vs.cd_linea_attivita,
          DECODE (la.denominazione,
                  NULL, DECODE (la.ds_linea_attivita,
                                NULL, 'NESSUNA DESCRIZIONE',
                                la.ds_linea_attivita
                               ),
                  la.denominazione
                 ),
          la.cd_natura, Null cd_progetto, Null ds_progetto,
          (SELECT cd_progetto
             FROM progetto_gest comm
            WHERE modu.esercizio_progetto_padre = comm.esercizio
              AND modu.pg_progetto_padre = comm.pg_progetto) cd_commessa,
          (SELECT ds_progetto
             FROM progetto_gest comm
            WHERE modu.esercizio_progetto_padre = comm.esercizio
              AND modu.pg_progetto_padre = comm.pg_progetto) ds_commessa,
          modu.cd_progetto cd_modulo, modu.ds_progetto ds_modulo,
          vs.ti_appartenenza, vs.ti_gestione, vs.cd_elemento_voce, vs.cd_voce,
          e.ds_elemento_voce, vs.esercizio_res,
          
-- RES_PRO_INI
          NVL (vs.im_obbl_res_pro, 0) res_pro_ini,
          
-- VAR_RES_PRO_PIU
          NVL (vs.var_piu_obbl_res_pro, 0) var_res_pro_piu,
          
-- VAR_RES_PRO_MENO
          NVL (vs.var_meno_obbl_res_pro, 0) var_res_pro_meno,
          
-- TOTALE
            NVL (vs.im_obbl_res_pro, 0)
          + NVL (vs.var_piu_obbl_res_pro, 0)
          - NVL (vs.var_meno_obbl_res_pro, 0) totale,
          
-- LIQUIDATO_PRO
          cnrutl002.liquidato_pro (vs.esercizio,
                                   vs.esercizio_res,
                                   vs.cd_centro_responsabilita,
                                   vs.cd_linea_attivita,
                                   vs.ti_appartenenza,
                                   vs.ti_gestione,
                                   vs.cd_voce
                                  ) liquidato_pro,
          
-- INCASSATO_PRO
          NVL (im_mandati_reversali_pro, 0) pagato_pro,
            
-- DA_INCASSARE
            (  NVL (vs.im_obbl_res_pro, 0)
             + NVL (vs.var_piu_obbl_res_pro, 0)
             - NVL (vs.var_meno_obbl_res_pro, 0)
            )
          - NVL (im_mandati_reversali_pro, 0) da_incassare
     FROM voce_f_saldi_cdr_linea vs,
          v_linea_attivita_valida la,
          progetto_gest modu,
          cdr,
          elemento_voce e,
          parametri_cnr
    WHERE parametri_cnr.esercizio = vs.esercizio
      AND parametri_cnr.fl_nuovo_pdg = 'Y'
      AND vs.esercizio > esercizio_res
      AND vs.ti_gestione = 'E'
      AND vs.esercizio = la.esercizio
      AND vs.cd_centro_responsabilita = la.cd_centro_responsabilita
      AND vs.cd_linea_attivita = la.cd_linea_attivita
      AND la.pg_progetto = modu.pg_progetto
      AND modu.esercizio = vs.esercizio
      AND vs.esercizio = e.esercizio
      AND vs.ti_appartenenza = e.ti_appartenenza
      AND vs.ti_gestione = e.ti_gestione
      AND vs.cd_voce = e.cd_elemento_voce
      AND vs.cd_centro_responsabilita = cdr.cd_centro_responsabilita ;
