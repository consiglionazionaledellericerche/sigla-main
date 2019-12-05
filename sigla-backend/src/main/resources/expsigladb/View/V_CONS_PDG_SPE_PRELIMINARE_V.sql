--------------------------------------------------------
--  DDL for View V_CONS_PDG_SPE_PRELIMINARE_V
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_CONS_PDG_SPE_PRELIMINARE_V" ("ESERCIZIO", "PESO_DIP", "CD_DIPARTIMENTO", "CD_CENTRO_SPESA", "CD_UNITA_ORGANIZZATIVA", "CD_CENTRO_RESPONSABILITA", "CD_CDR_ORIGINE_FONDI", "CD_CLASSIFICAZIONE", "NR_LIVELLO", "CD_LIVELLO1", "CD_LIVELLO2", "CD_LIVELLO3", "CD_LIVELLO4", "CD_LIVELLO5", "CD_LIVELLO6", "CD_LIVELLO7", "PG_MODULO", "CD_MODULO", "CD_TIPO_MODULO", "PG_COMMESSA", "CD_COMMESSA", "PG_PROGETTO", "CD_PROGETTO", "IM_DEC_IST_INT", "IM_DEC_IST_EST", "IM_DEC_AREA_INT", "IM_DEC_AREA_EST", "TRATT_ECON_INT", "TRATT_ECON_EST", "IM_ACC_ALTRE_SP_INT", "IM_PREV_A2", "IM_PREV_A3", "ID_CLASSIFICAZIONE", "CD_ELEMENTO_VOCE", "DS_ELEMENTO_VOCE") AS 
  SELECT
--
-- Date: 09/11/2006
-- Version: 1.1
--
-- Vista CONSULTAZIONE Piano di Gestione Preliminare Spese
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
            esercizio, peso_dip, cd_dipartimento, cd_centro_spesa,
            cd_unita_organizzativa, cd_centro_responsabilita,
            cd_cdr_origine_fondi, cd_classificazione, nr_livello, cd_livello1,
            cd_livello2, cd_livello3, cd_livello4, cd_livello5, cd_livello6,
            cd_livello7, pg_modulo, cd_modulo, cd_tipo_modulo, pg_commessa,
            cd_commessa, pg_progetto, cd_progetto,
            NVL (SUM (im_dec_ist_int), 0) im_dec_ist_int,
            NVL (SUM (im_dec_ist_est), 0) im_dec_ist_est,
            NVL (SUM (im_dec_area_int), 0) im_dec_area_int,
            NVL (SUM (im_dec_area_est), 0) im_dec_area_est,
            NVL (SUM (tratt_econ_int), 0) tratt_econ_int,
            NVL (SUM (tratt_econ_est), 0) tratt_econ_est,
            NVL (SUM (im_acc_altre_sp_int), 0) im_acc_altre_sp_int,
            NVL (SUM (im_prev_a2), 0) im_prev_a2,
            NVL (SUM (im_prev_a3), 0) im_prev_a3,
	       		id_classificazione,
	          cd_elemento_voce,
	          ds_elemento_voce
       FROM (SELECT pdg_spe.esercizio,
                    DECODE (unita_organizzativa.cd_tipo_unita,
                            'SAC', '13',
                            TO_CHAR (NVL (p.peso, 1000))
                           ) peso_dip,
                    DECODE
                          (unita_organizzativa.cd_tipo_unita,
                           'SAC', 'SAC',
                           NVL (prog.cd_dipartimento, NULL)
                          ) cd_dipartimento,
                    unita_organizzativa.cd_unita_padre cd_centro_spesa,
                    unita_organizzativa.cd_unita_organizzativa,
                    pdg_spe.cd_centro_responsabilita,
                    pdg_spe.cd_centro_responsabilita cd_cdr_origine_fondi,
                    cd_classificazione, nr_livello, cd_livello1, cd_livello2,
                    cd_livello3, cd_livello4, cd_livello5, cd_livello6,
                    cd_livello7, modu.pg_progetto pg_modulo,
                    modu.cd_progetto cd_modulo,
                    modu.cd_tipo_progetto cd_tipo_modulo,
                    comm.pg_progetto pg_commessa,
                    comm.cd_progetto cd_commessa, prog.pg_progetto,
                    prog.cd_progetto,
                    NVL (pdg_spe.im_spese_gest_decentrata_int,
                         0
                        ) im_dec_ist_int,
                    NVL (pdg_spe.im_spese_gest_decentrata_est,
                         0
                        ) im_dec_ist_est,
                    0 im_dec_area_int, 0 im_dec_area_est, 0 tratt_econ_int,
                    0 tratt_econ_est, 0 im_acc_altre_sp_int, 0 im_prev_a2,
                    0 im_prev_a3,
	          				cla.id_classificazione,
	          				voce.cd_elemento_voce,
	          				voce.ds_elemento_voce
               FROM pdg_modulo_spese pdg_spe,
                    v_classificazione_voci cla,
                    unita_organizzativa,
                    unita_organizzativa area,
                    cdr,
                    progetto_prev modu,
                    progetto_prev comm,
                    progetto_prev prog,
                    dipartimento_peso p,
            				elemento_voce voce
              WHERE prog.esercizio = p.esercizio(+)
                AND prog.cd_dipartimento = p.cd_dipartimento(+)
                AND
                    --Join tra PDG_MODULO_SPESE e V_CLASSIFICAZIONE_VOCI
                    pdg_spe.id_classificazione = cla.id_classificazione
                AND
--Join tra PDG_MODULO_SPESE e AREA (UNITA_ORGANIZZATIVA)
                    pdg_spe.cd_cds_area = area.cd_unita_organizzativa
                AND
--Join tra PDG_MODULO_SPESE e CDR
                    pdg_spe.cd_centro_responsabilita =
                                                  cdr.cd_centro_responsabilita
                AND
--Join tra CDR e UNITA_ORGANIZZATIVA
                    cdr.cd_unita_organizzativa =
                                    unita_organizzativa.cd_unita_organizzativa
                AND
--Join tra PDG_MODULO_SPESE e MODULO (PROGETTO)
                    pdg_spe.pg_progetto = modu.pg_progetto
                AND modu.esercizio = pdg_spe.esercizio
                AND
--Join tra MODULO e COMMESSA (PROGETTO)
                    modu.esercizio_progetto_padre = comm.esercizio
                AND modu.pg_progetto_padre = comm.pg_progetto
                AND
--Join tra COMMESSA e PROGETTO
                    comm.esercizio_progetto_padre = prog.esercizio
                AND comm.pg_progetto_padre = prog.pg_progetto
                AND NVL (cla.cdr_accentratore, 'xxx') != CNRCTB020.getCdCDRPersonale(PDG_SPE.ESERCIZIO)
                AND area.cd_tipo_unita != 'AREA'
                and voce.id_classificazione=cla.id_classificazione
             UNION ALL                                                 -- AREE
             SELECT pdg_spe.esercizio,
                    DECODE (unita_organizzativa.cd_tipo_unita,
                            'SAC', '13',
                            TO_CHAR (NVL (p.peso, 1000))
                           ) peso_dip,
                    DECODE
                          (unita_organizzativa.cd_tipo_unita,
                           'SAC', 'SAC',
                           NVL (prog.cd_dipartimento, NULL)
                          ) cd_dipartimento,
                    pdg_spe.cd_cds_area cd_centro_spesa,
                    unita_organizzativa.cd_unita_organizzativa,
                    cdr.cd_centro_responsabilita,
                    pdg_spe.cd_centro_responsabilita cd_cdr_origine_fondi,
                    cd_classificazione, nr_livello, cd_livello1, cd_livello2,
                    cd_livello3, cd_livello4, cd_livello5, cd_livello6,
                    cd_livello7, modu.pg_progetto pg_modulo,
                    modu.cd_progetto cd_modulo,
                    modu.cd_tipo_progetto cd_tipo_modulo,
                    comm.pg_progetto pg_commessa,
                    comm.cd_progetto cd_commessa, prog.pg_progetto,
                    prog.cd_progetto, 0 im_dec_ist_int, 0 im_dec_ist_est,
                    NVL
                       (pdg_spe.im_spese_gest_decentrata_int,
                        0
                       ) im_dec_area_int,
                    NVL
                       (pdg_spe.im_spese_gest_decentrata_est,
                        0
                       ) im_dec_area_est,
                    0 tratt_econ_int, 0 tratt_econ_est, 0 im_acc_altre_sp_int,
                    0 im_prev_a2, 0 im_prev_a3,
	          				cla.id_classificazione,
	          			  voce.cd_elemento_voce,
	          				voce.ds_elemento_voce
               FROM pdg_modulo_spese pdg_spe,
                    v_classificazione_voci cla,
                    unita_organizzativa area,
                    unita_organizzativa,
                    cdr,
                    progetto_prev modu,
                    progetto_prev comm,
                    progetto_prev prog,
                    dipartimento_peso p,
            				elemento_voce voce
              WHERE prog.esercizio = p.esercizio(+)
                AND prog.cd_dipartimento = p.cd_dipartimento(+)
                AND
                    --Join tra PDG_MODULO_SPESE e V_CLASSIFICAZIONE_VOCI
                    pdg_spe.id_classificazione = cla.id_classificazione
                AND
--Join tra PDG_MODULO_SPESE e AREA (UNITA_ORGANIZZATIVA)
                    pdg_spe.cd_cds_area = area.cd_unita_organizzativa
                AND
--Join tra AREA e UNITA_ORGANIZZATIVA
                    area.cd_unita_organizzativa =
                                            unita_organizzativa.cd_unita_padre
                AND unita_organizzativa.fl_uo_cds = 'Y'
                AND
--Join tra UNITA_ORGANIZZATIVA e CDR
                    unita_organizzativa.cd_unita_organizzativa =
                                                    cdr.cd_unita_organizzativa
                AND cdr.cd_cdr_afferenza IS NULL
                AND
--Join tra PDG_MODULO_SPESE e MODULO (PROGETTO)
                    pdg_spe.pg_progetto = modu.pg_progetto
                AND modu.esercizio = pdg_spe.esercizio
                AND
--Join tra MODULO e COMMESSA (PROGETTO)
                    modu.esercizio_progetto_padre = comm.esercizio
                AND modu.pg_progetto_padre = comm.pg_progetto
                AND
--Join tra COMMESSA e PROGETTO
                    comm.esercizio_progetto_padre = prog.esercizio
                AND comm.pg_progetto_padre = prog.pg_progetto
                AND NVL (cla.cdr_accentratore, 'xxx') != CNRCTB020.getCdCDRPersonale(PDG_SPE.ESERCIZIO)
                AND area.cd_tipo_unita = 'AREA'
                and voce.id_classificazione=cla.id_classificazione
             UNION ALL                       -- SPESE ACCENTRATE NON PERSONALE
             SELECT pdg_spe.esercizio,
                    DECODE (unita_organizzativa.cd_tipo_unita,
                            'SAC', '13',
                            TO_CHAR (NVL (p.peso, 1000))
                           ) peso_dip,
                    DECODE
                          (unita_organizzativa.cd_tipo_unita,
                           'SAC', 'SAC',
                           NVL (prog.cd_dipartimento, NULL)
                          ) cd_dipartimento,
                    unita_organizzativa.cd_unita_padre cd_centro_spesa,
                    unita_organizzativa.cd_unita_organizzativa,
                    cla.cdr_accentratore cd_centro_responsabilita,
                    pdg_spe.cd_centro_responsabilita cd_cdr_origine_fondi,
                    cd_classificazione, nr_livello, cd_livello1, cd_livello2,
                    cd_livello3, cd_livello4, cd_livello5, cd_livello6,
                    cd_livello7, modu.pg_progetto pg_modulo,
                    modu.cd_progetto cd_modulo,
                    modu.cd_tipo_progetto cd_tipo_modulo,
                    comm.pg_progetto pg_commessa,
                    comm.cd_progetto cd_commessa, prog.pg_progetto,
                    prog.cd_progetto, 0 im_dec_ist_int, 0 im_dec_ist_est,
                    0 im_dec_area_int, 0 im_dec_area_est, 0 tratt_econ_int,
                    0 tratt_econ_est,
                      NVL
                         (pdg_spe.im_spese_gest_accentrata_int,
                          0
                         )
                    + NVL (pdg_spe.im_spese_gest_accentrata_est, 0)
                                                          im_acc_altre_sp_int,
                    0 im_prev_a2, 0 im_prev_a3,
	          				cla.id_classificazione,
	          				voce.cd_elemento_voce,
	          				voce.ds_elemento_voce
               FROM pdg_modulo_spese pdg_spe,
                    v_classificazione_voci cla,
                    unita_organizzativa,
                    cdr,
                    progetto_prev modu,
                    progetto_prev comm,
                    progetto_prev prog,
                    dipartimento_peso p,
            				elemento_voce voce
              WHERE prog.esercizio = p.esercizio(+)
                AND prog.cd_dipartimento = p.cd_dipartimento(+)
                AND
                    --Join tra PDG_MODULO_SPESE e V_CLASSIFICAZIONE_VOCI
                    pdg_spe.id_classificazione = cla.id_classificazione
                AND
--Join tra PDG_MODULO_SPESE e AREA (UNITA_ORGANIZZATIVA)
                    cla.cdr_accentratore IS NOT NULL
                AND cla.cdr_accentratore = cdr.cd_centro_responsabilita
                AND
--Join tra CDR e UNITA_ORGANIZZATIVA
                    cdr.cd_unita_organizzativa =
                                    unita_organizzativa.cd_unita_organizzativa
                AND
--Join tra PDG_MODULO_SPESE e MODULO (PROGETTO)
                    pdg_spe.pg_progetto = modu.pg_progetto
                AND modu.esercizio = pdg_spe.esercizio
                AND
--Join tra MODULO e COMMESSA (PROGETTO)
                    modu.esercizio_progetto_padre = comm.esercizio
                AND modu.pg_progetto_padre = comm.pg_progetto
                AND
--Join tra COMMESSA e PROGETTO
                    comm.esercizio_progetto_padre = prog.esercizio
                AND comm.pg_progetto_padre = prog.pg_progetto
                AND NVL (cla.cdr_accentratore, 'xxx') != CNRCTB020.getCdCDRPersonale(PDG_SPE.ESERCIZIO)
                 and voce.id_classificazione=cla.id_classificazione
             UNION ALL                  -- TRATTAMENTO ECONOMICO DEL PERSONALE
             SELECT pdg_spe.esercizio,
                    DECODE (unita_organizzativa.cd_tipo_unita,
                            'SAC', '13',
                            TO_CHAR (NVL (p.peso, 1000))
                           ) peso_dip,
                    DECODE
                          (unita_organizzativa.cd_tipo_unita,
                           'SAC', 'SAC',
                           NVL (prog.cd_dipartimento, NULL)
                          ) cd_dipartimento,
                    unita_organizzativa.cd_unita_padre cd_centro_spesa,
                    unita_organizzativa.cd_unita_organizzativa,
                    cla.cdr_accentratore cd_centro_responsabilita,
                    pdg_spe.cd_centro_responsabilita cd_cdr_origine_fondi,
                    cd_classificazione, nr_livello, cd_livello1, cd_livello2,
                    cd_livello3, cd_livello4, cd_livello5, cd_livello6,
                    cd_livello7, modu.pg_progetto pg_modulo,
                    modu.cd_progetto cd_modulo,
                    modu.cd_tipo_progetto cd_tipo_modulo,
                    comm.pg_progetto pg_commessa,
                    comm.cd_progetto cd_commessa, prog.pg_progetto,
                    prog.cd_progetto, 0 im_dec_ist_int, 0 im_dec_ist_est,
                    0 im_dec_area_int, 0 im_dec_area_est,
                      NVL
                         (pdg_spe.im_spese_gest_accentrata_int,
                          0
                         )
                    + DECODE (unita_organizzativa.cd_tipo_unita,
                              'SAC', NVL
                                        (pdg_spe.im_spese_gest_decentrata_int,
                                         0
                                        ),
                              0
                             ) tratt_econ_int,
                      NVL
                         (pdg_spe.im_spese_gest_accentrata_est,
                          0
                         )
                    + DECODE (unita_organizzativa.cd_tipo_unita,
                              'SAC', NVL
                                        (pdg_spe.im_spese_gest_decentrata_est,
                                         0
                                        ),
                              0
                             ) tratt_econ_est,
                    0 im_acc_altre_sp_int, 0 im_prev_a2, 0 im_prev_a3,
	          				cla.id_classificazione,
	          				voce.cd_elemento_voce,
	          				voce.ds_elemento_voce
               FROM pdg_modulo_spese pdg_spe,
                    v_classificazione_voci cla,
                    unita_organizzativa,
                    cdr,
                    progetto_prev modu,
                    progetto_prev comm,
                    progetto_prev prog,
                    dipartimento_peso p,
            				elemento_voce voce
              WHERE prog.esercizio = p.esercizio(+)
                AND prog.cd_dipartimento = p.cd_dipartimento(+)
                AND
                    --Join tra PDG_MODULO_SPESE e V_CLASSIFICAZIONE_VOCI
                    pdg_spe.id_classificazione = cla.id_classificazione
                AND
--Join tra PDG_MODULO_SPESE e AREA (UNITA_ORGANIZZATIVA)
                    cla.cdr_accentratore IS NOT NULL
                AND cla.cdr_accentratore = cdr.cd_centro_responsabilita
                AND
--Join tra CDR e UNITA_ORGANIZZATIVA
                    cdr.cd_unita_organizzativa =
                                    unita_organizzativa.cd_unita_organizzativa
                AND
--Join tra PDG_MODULO_SPESE e MODULO (PROGETTO)
                    pdg_spe.pg_progetto = modu.pg_progetto
                AND modu.esercizio = pdg_spe.esercizio
                AND
--Join tra MODULO e COMMESSA (PROGETTO)
                    modu.esercizio_progetto_padre = comm.esercizio
                AND modu.pg_progetto_padre = comm.pg_progetto
                AND
--Join tra COMMESSA e PROGETTO
                    comm.esercizio_progetto_padre = prog.esercizio
                AND comm.pg_progetto_padre = prog.pg_progetto
                AND NVL (cla.cdr_accentratore, 'xxx') = CNRCTB020.getCdCDRPersonale(PDG_SPE.ESERCIZIO)
                and voce.id_classificazione=cla.id_classificazione
             UNION ALL                                           -- ANNO 2 E 3
             SELECT pdg_spe.esercizio,
                    DECODE (unita_organizzativa.cd_tipo_unita,
                            'SAC', '13',
                            TO_CHAR (NVL (p.peso, 1000))
                           ) peso_dip,
                    DECODE
                          (unita_organizzativa.cd_tipo_unita,
                           'SAC', 'SAC',
                           NVL (prog.cd_dipartimento, NULL)
                          ) cd_dipartimento,
                    unita_organizzativa.cd_unita_padre cd_centro_spesa,
                    unita_organizzativa.cd_unita_organizzativa
                                                       cd_unita_organizzativa,
                    pdg_spe.cd_centro_responsabilita,
                    pdg_spe.cd_centro_responsabilita cd_cdr_origine_fondi,
                    cd_classificazione, nr_livello, cd_livello1, cd_livello2,
                    cd_livello3, cd_livello4, cd_livello5, cd_livello6,
                    cd_livello7, modu.pg_progetto pg_modulo,
                    modu.cd_progetto cd_modulo,
                    modu.cd_tipo_progetto cd_tipo_modulo,
                    comm.pg_progetto pg_commessa,
                    comm.cd_progetto cd_commessa, prog.pg_progetto,
                    prog.cd_progetto, 0 im_dec_ist_int, 0 im_dec_ist_est,
                    0 im_dec_area_int, 0 im_dec_area_est, 0 tratt_econ_int,
                    0 tratt_econ_est, 0 im_acc_altre_sp_int,
                    NVL (pdg_spe.im_spese_a2, 0) im_prev_a2,
                    NVL (pdg_spe.im_spese_a3, 0) im_prev_a3,
	          				cla.id_classificazione,
	          				voce.cd_elemento_voce,
	          				voce.ds_elemento_voce
               FROM pdg_modulo_spese pdg_spe,
                    v_classificazione_voci cla,
                    unita_organizzativa,
                    cdr,
                    progetto_prev modu,
                    progetto_prev comm,
                    progetto_prev prog,
                    dipartimento_peso p,
            				elemento_voce voce
              WHERE prog.esercizio = p.esercizio(+)
                AND prog.cd_dipartimento = p.cd_dipartimento(+)
                AND
                    --Join tra PDG_MODULO_SPESE e V_CLASSIFICAZIONE_VOCI
                    pdg_spe.id_classificazione = cla.id_classificazione
                AND
--Join tra PDG_MODULO_SPESE e CDR
                    pdg_spe.cd_centro_responsabilita =
                                                  cdr.cd_centro_responsabilita
                AND
--Join tra CDR e UNITA_ORGANIZZATIVA
                    cdr.cd_unita_organizzativa =
                                    unita_organizzativa.cd_unita_organizzativa
                AND
--Join tra PDG_MODULO_SPESE e MODULO (PROGETTO)
                    pdg_spe.pg_progetto = modu.pg_progetto
                AND modu.esercizio = pdg_spe.esercizio
                AND
--Join tra MODULO e COMMESSA (PROGETTO)
                    modu.esercizio_progetto_padre = comm.esercizio
                AND modu.pg_progetto_padre = comm.pg_progetto
                AND
--Join tra COMMESSA e PROGETTO
                    comm.esercizio_progetto_padre = prog.esercizio
                AND comm.pg_progetto_padre = prog.pg_progetto
                and voce.id_classificazione=cla.id_classificazione)
   GROUP BY esercizio,
            peso_dip,
            cd_dipartimento,
            cd_centro_spesa,
            cd_unita_organizzativa,
            cd_centro_responsabilita,
            cd_cdr_origine_fondi,
            cd_classificazione,
            nr_livello,
            cd_livello1,
            cd_livello2,
            cd_livello3,
            cd_livello4,
            cd_livello5,
            cd_livello6,
            cd_livello7,
            pg_modulo,
            cd_modulo,
            cd_tipo_modulo,
            pg_commessa,
            cd_commessa,
            pg_progetto,
            cd_progetto,
	          id_classificazione,
	          cd_elemento_voce,
	          ds_elemento_voce;
