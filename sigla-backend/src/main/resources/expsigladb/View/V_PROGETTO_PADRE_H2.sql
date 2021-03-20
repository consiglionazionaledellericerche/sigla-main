--------------------------------------------------------
--  DDL for View V_PROGETTO_PADRE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_PROGETTO_PADRE" ("ESERCIZIO", "PG_PROGETTO", "TIPO_FASE", "ESERCIZIO_PROGETTO_PADRE", "PG_PROGETTO_PADRE", "TIPO_FASE_PROGETTO_PADRE", "CD_PROGETTO", "DS_PROGETTO", "CD_TIPO_PROGETTO", "CD_UNITA_ORGANIZZATIVA", "CD_RESPONSABILE_TERZO", "DT_INIZIO", "DT_FINE", "DT_PROROGA", "IMPORTO_PROGETTO", "IMPORTO_DIVISA", "CD_DIVISA", "NOTE", "STATO", "CONDIVISO", "DURATA_PROGETTO", "LIVELLO", "CD_DIPARTIMENTO", "DACR", "UTCR", "DUVA", "UTUV", "PG_VER_REC", "FL_UTILIZZABILE", "FL_PIANO_TRIENNALE", "CD_PROGRAMMA", "CD_MISSIONE", "P_ESERCIZIO", "P_PG_PROGETTO", "P_CD_PROGETTO", "P_DS_PROGETTO", "P_CD_TIPO_PROGETTO", "P_CD_UNITA_ORGANIZZATIVA", "P_CD_RESPONSABILE_TERZO", "P_DT_INIZIO", "P_DT_FINE", "P_DT_PROROGA", "P_IMPORTO_PROGETTO", "P_IMPORTO_DIVISA", "P_CD_DIVISA", "P_NOTE", "P_STATO", "P_CONDIVISO", "P_DURATA_PROGETTO", "P_LIVELLO", "P_CD_DIPARTIMENTO", "P_CD_PROGRAMMA", "P_CD_MISSIONE", "PG_PROGETTO_OTHER_FIELD", "ID_TIPO_FINANZIAMENTO", "CODICE_TIPO_FINANZIAMENTO", "FL_ASSOCIA_CONTRATTO", "STATO_OTHER_FIELD", "DT_INIZIO_OTHER_FIELD", "DT_FINE_OTHER_FIELD", "DT_PROROGA_OTHER_FIELD", "IM_FINANZIATO_OTHER_FIELD", "IM_COFINANZIATO_OTHER_FIELD") AS
  SELECT /*+ optimizer_features_enable('10.1.0') */
--
-- Date: 09/11/2006
-- Version: 1.1
--
-- Estrae le informazioni di un progetto e del progetto padre
--
-- History:
--
-- Date: 01/01/2006
-- Version: 1.0
-- Creazione
--
-- Date: 09/11/2006
-- Version: 1.11
-- Aggiunta la selezione del progetto/commessa/modulo per anno
--
-- Body:
--
          progetto.esercizio, progetto.pg_progetto, progetto.tipo_fase,
          progetto.esercizio_progetto_padre, progetto.pg_progetto_padre,
          progetto.tipo_fase_progetto_padre, progetto.cd_progetto,
          progetto.ds_progetto, progetto.cd_tipo_progetto,
          progetto.cd_unita_organizzativa, progetto.cd_responsabile_terzo,
          progetto.dt_inizio, progetto.dt_fine, progetto.dt_proroga,
          progetto.importo_progetto, progetto.importo_divisa,
          progetto.cd_divisa, progetto.note, progetto.stato,
          progetto.condiviso, progetto.durata_progetto, progetto.livello,
          progetto.cd_dipartimento, progetto.dacr, progetto.utcr,
          progetto.duva, progetto.utuv, progetto.pg_ver_rec,
          progetto.fl_utilizzabile, progetto.fl_piano_triennale,
          progetto.cd_programma, progetto.cd_missione,
          progetto_padre.esercizio p_esercizio,
          progetto_padre.pg_progetto p_pg_progetto,
          progetto_padre.cd_progetto p_cd_progetto,
          progetto_padre.ds_progetto p_ds_progetto,
          progetto_padre.cd_tipo_progetto p_cd_tipo_progetto,
          progetto_padre.cd_unita_organizzativa p_cd_unita_organizzativa,
          progetto_padre.cd_responsabile_terzo p_cd_responsabile_terzo,
          progetto_padre.dt_inizio p_dt_inizio,
          progetto_padre.dt_fine p_dt_fine,
          progetto_padre.dt_proroga p_dt_proroga,
          progetto_padre.importo_progetto p_importo_progetto,
          progetto_padre.importo_divisa p_importo_divisa,
          progetto_padre.cd_divisa p_cd_divisa, progetto_padre.note p_note,
          progetto_padre.stato p_stato, progetto_padre.condiviso p_condiviso,
          progetto_padre.durata_progetto p_durata_progetto,
          progetto_padre.livello p_livello, progetto_padre.cd_dipartimento,
          progetto_padre.cd_programma p_cd_programma,
          progetto_padre.cd_missione p_cd_missione,
          progetto_other_field.pg_progetto pg_progetto_other_field,
          progetto_other_field.id_tipo_finanziamento id_tipo_finanziamento,
          tipo_finanziamento.codice codice_tipo_finanziamento,
          tipo_finanziamento.fl_associa_contratto fl_associa_contratto,
          progetto_other_field.stato stato_other_field,
          progetto_other_field.dt_inizio dt_inizio_other_field,
          progetto_other_field.dt_fine dt_fine_other_field,
          progetto_other_field.dt_proroga dt_proroga_other_field,
          progetto_other_field.im_finanziato im_finanziato_other_field,
          progetto_other_field.im_cofinanziato im_cofinanziato_other_field
     FROM progetto progetto left outer join progetto progetto_padre
       ON progetto.esercizio_progetto_padre = progetto_padre.esercizio
      AND progetto.pg_progetto_padre = progetto_padre.pg_progetto
      AND progetto.tipo_fase_progetto_padre = progetto_padre.tipo_fase
      left outer join progetto_other_field
      ON progetto.pg_progetto = progetto_other_field.pg_progetto
      left outer join tipo_finanziamento
      ON progetto_other_field.id_tipo_finanziamento = tipo_finanziamento.id;
