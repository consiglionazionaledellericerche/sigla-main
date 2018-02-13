--------------------------------------------------------
--  DDL for View V_CONS_PDG_ETR_PRELIMINARE_V
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_CONS_PDG_ETR_PRELIMINARE_V" ("ESERCIZIO", "PESO_DIP", "CD_DIPARTIMENTO", "CD_CENTRO_SPESA", "CD_UNITA_ORGANIZZATIVA", "CD_CENTRO_RESPONSABILITA", "CD_CLASSIFICAZIONE", "NR_LIVELLO", "CD_LIVELLO1", "CD_LIVELLO2", "CD_LIVELLO3", "CD_LIVELLO4", "CD_LIVELLO5", "CD_LIVELLO6", "CD_LIVELLO7", "PG_MODULO", "CD_MODULO", "CD_TIPO_MODULO", "PG_COMMESSA", "CD_COMMESSA", "PG_PROGETTO", "CD_PROGETTO", "CD_TERZO_FINANZIATORE", "TOT_FINANZIAMENTO", "TOT_ENT_IST_A1", "TOT_ENT_AREE_A1", "TOT_ENT_IST_A2", "TOT_ENT_AREE_A2", "TOT_ENT_IST_A3", "TOT_ENT_AREE_A3", "ID_CLASSIFICAZIONE", "CD_ELEMENTO_VOCE", "DS_ELEMENTO_VOCE") AS 
  SELECT
--
-- Date: 09/11/2006
-- Version: 1.1
--
-- Vista CONSULTAZIONE Piano di Gestione Preliminare Entrate
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
            pdg_etr.esercizio, NVL (p.peso, 1000) peso_dip,
            NVL (prog.cd_dipartimento, NULL) cd_dipartimento,
            unita_organizzativa.cd_unita_padre cd_centro_spesa,
            unita_organizzativa.cd_unita_organizzativa,
            pdg_etr.cd_centro_responsabilita, cd_classificazione, nr_livello,
            cd_livello1, cd_livello2, cd_livello3, cd_livello4, cd_livello5,
            cd_livello6, cd_livello7, modu.pg_progetto pg_modulo,
            modu.cd_progetto cd_modulo, modu.cd_tipo_progetto cd_tipo_modulo,
            comm.pg_progetto pg_commessa, comm.cd_progetto cd_commessa,
            prog.pg_progetto, prog.cd_progetto,
            pdg_etr.cd_terzo cd_terzo_finanziatore,
            NVL (SUM (im_entrata_tot), 0) tot_finanziamento,
            NVL (SUM (im_entrata), 0) tot_ent_ist_a1, 0 tot_ent_aree_a1,
            NVL (SUM (im_entrata_a2), 0) tot_ent_ist_a2, 0 tot_ent_aree_a2,
            NVL (SUM (im_entrata_a3), 0) tot_ent_ist_a3, 0 tot_ent_aree_a3,
            cla.id_classificazione,
            voce.cd_elemento_voce,
            voce.ds_elemento_voce
       FROM pdg_modulo_entrate pdg_etr,
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
            pdg_etr.id_classificazione = cla.id_classificazione
        AND
--Join tra PDG_MODULO_SPESE e AREA (UNITA_ORGANIZZATIVA)
            pdg_etr.cd_cds_area = area.cd_unita_organizzativa
        AND
--Join tra PDG_MODULO_SPESE e CDR
            pdg_etr.cd_centro_responsabilita = cdr.cd_centro_responsabilita
        AND
--Join tra CDR e UNITA_ORGANIZZATIVA
            cdr.cd_unita_organizzativa =
                                    unita_organizzativa.cd_unita_organizzativa
        AND
--Join tra PDG_MODULO_SPESE e MODULO (PROGETTO)
            pdg_etr.pg_progetto = modu.pg_progetto
        AND modu.esercizio = pdg_etr.esercizio
        AND
--Join tra MODULO e COMMESSA (PROGETTO)
            modu.esercizio_progetto_padre = comm.esercizio
        AND modu.pg_progetto_padre = comm.pg_progetto
        AND
--Join tra COMMESSA e PROGETTO
            comm.esercizio_progetto_padre = prog.esercizio
        AND comm.pg_progetto_padre = prog.pg_progetto
        AND area.cd_tipo_unita != 'AREA'
        and voce.id_classificazione=cla.id_classificazione
   GROUP BY pdg_etr.esercizio,
            NVL (p.peso, 1000),
            NVL (prog.cd_dipartimento, NULL),
            unita_organizzativa.cd_unita_padre,
            unita_organizzativa.cd_unita_organizzativa,
            pdg_etr.cd_centro_responsabilita,
            cd_classificazione,
            nr_livello,
            cd_livello1,
            cd_livello2,
            cd_livello3,
            cd_livello4,
            cd_livello5,
            cd_livello6,
            cd_livello7,
            modu.pg_progetto,
            modu.cd_progetto,
            modu.cd_tipo_progetto,
            comm.pg_progetto,
            comm.cd_progetto,
            prog.pg_progetto,
            prog.cd_progetto,
            pdg_etr.cd_terzo,
            cla.id_classificazione,
            voce.cd_elemento_voce,
            voce.ds_elemento_voce
   UNION ALL                                                            --Aree
   SELECT   pdg_etr.esercizio, NVL (p.peso, 1000) peso_dip,
            NVL (prog.cd_dipartimento, NULL) cd_dipartimento,
            pdg_etr.cd_cds_area cd_centro_spesa,
            unita_organizzativa.cd_unita_organizzativa,
            cdr.cd_centro_responsabilita, cd_classificazione, nr_livello,
            cd_livello1, cd_livello2, cd_livello3, cd_livello4, cd_livello5,
            cd_livello6, cd_livello7, modu.pg_progetto pg_modulo,
            modu.cd_progetto cd_modulo, modu.cd_tipo_progetto cd_tipo_modulo,
            comm.pg_progetto pg_commessa, comm.cd_progetto cd_commessa,
            prog.pg_progetto, prog.cd_progetto,
            pdg_etr.cd_terzo cd_terzo_finanziatore,
            NVL (SUM (im_entrata_tot), 0) tot_finanziamento, 0 tot_ent_ist_a1,
            NVL (SUM (im_entrata), 0) tot_ent_aree_a1, 0 tot_ent_ist_a2,
            NVL (SUM (im_entrata_a2), 0) tot_ent_aree_a2, 0 tot_ent_ist_a3,
            NVL (SUM (im_entrata_a3), 0) tot_ent_aree_a3,
            cla.id_classificazione,
            voce.cd_elemento_voce,
            voce.ds_elemento_voce
       FROM pdg_modulo_entrate pdg_etr,
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
            pdg_etr.id_classificazione = cla.id_classificazione
        AND
--Join tra PDG_MODULO_SPESE e AREA (UNITA_ORGANIZZATIVA)
            pdg_etr.cd_cds_area = area.cd_unita_organizzativa
        AND
--Join tra AREA e UNITA_ORGANIZZATIVA
            area.cd_unita_organizzativa = unita_organizzativa.cd_unita_padre
        AND unita_organizzativa.fl_uo_cds = 'Y'
        AND
--Join tra UNITA_ORGANIZZATIVA e CDR
            unita_organizzativa.cd_unita_organizzativa =
                                                    cdr.cd_unita_organizzativa
        AND cdr.cd_cdr_afferenza IS NULL
        AND
--Join tra PDG_MODULO_SPESE e MODULO (PROGETTO)
            pdg_etr.pg_progetto = modu.pg_progetto
        AND modu.esercizio = pdg_etr.esercizio
        AND
--Join tra MODULO e COMMESSA (PROGETTO)
            modu.esercizio_progetto_padre = comm.esercizio
        AND modu.pg_progetto_padre = comm.pg_progetto
        AND
--Join tra COMMESSA e PROGETTO
            comm.esercizio_progetto_padre = prog.esercizio
        AND comm.pg_progetto_padre = prog.pg_progetto
        AND area.cd_tipo_unita = 'AREA'
        and voce.id_classificazione=cla.id_classificazione
   GROUP BY pdg_etr.esercizio,
            NVL (p.peso, 1000),
            NVL (prog.cd_dipartimento, NULL),
            pdg_etr.cd_cds_area,
            unita_organizzativa.cd_unita_organizzativa,
            cdr.cd_centro_responsabilita,
            cd_classificazione,
            nr_livello,
            cd_livello1,
            cd_livello2,
            cd_livello3,
            cd_livello4,
            cd_livello5,
            cd_livello6,
            cd_livello7,
            modu.pg_progetto,
            modu.cd_progetto,
            modu.cd_tipo_progetto,
            comm.pg_progetto,
            comm.cd_progetto,
            prog.pg_progetto,
            prog.cd_progetto,
            pdg_etr.cd_terzo,
            cla.id_classificazione,
            voce.cd_elemento_voce,
            voce.ds_elemento_voce;
