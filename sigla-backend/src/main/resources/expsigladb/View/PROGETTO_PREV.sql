--------------------------------------------------------
--  DDL for View PROGETTO_PREV
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "PROGETTO_PREV" ("ESERCIZIO", "PG_PROGETTO", "TIPO_FASE", "ESERCIZIO_PROGETTO_PADRE", "PG_PROGETTO_PADRE", "TIPO_FASE_PROGETTO_PADRE", "CD_PROGETTO", "DS_PROGETTO", "CD_TIPO_PROGETTO", "CD_UNITA_ORGANIZZATIVA", "CD_RESPONSABILE_TERZO", "DT_INIZIO", "DT_FINE", "DT_PROROGA", "IMPORTO_PROGETTO", "IMPORTO_DIVISA", "CD_DIVISA", "NOTE", "STATO", "CONDIVISO", "DURATA_PROGETTO", "LIVELLO", "CD_DIPARTIMENTO", "FL_UTILIZZABILE", "DACR", "UTCR", "DUVA", "UTUV", "PG_VER_REC", "CD_PROGETTO_SIP", "CD_PROGRAMMA", "CD_MISSIONE") AS 
  SELECT
--
-- Date: 13/11/2006
-- Version: 1.0
--
-- Vista dei Progetti esistenti di tipo Previsionale
--
-- History
--
-- Date :13/11/2006
-- Version: 1.0
-- Creazione
--
-- Body
--
          esercizio, pg_progetto, tipo_fase, esercizio_progetto_padre,
          pg_progetto_padre, tipo_fase_progetto_padre, cd_progetto,
          ds_progetto, cd_tipo_progetto, cd_unita_organizzativa,
          cd_responsabile_terzo, dt_inizio, dt_fine, dt_proroga,
          importo_progetto, importo_divisa, cd_divisa, note, stato, condiviso,
          durata_progetto, livello, cd_dipartimento, fl_utilizzabile, dacr,
          utcr, duva, utuv, pg_ver_rec, cd_progetto_sip, cd_programma, cd_missione
     FROM progetto
    WHERE tipo_fase = 'P';
