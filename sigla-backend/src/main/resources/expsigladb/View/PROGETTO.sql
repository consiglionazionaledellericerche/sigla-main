--------------------------------------------------------
--  DDL for View PROGETTO
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "PROGETTO" ("ESERCIZIO", "PG_PROGETTO", "TIPO_FASE", "ESERCIZIO_PROGETTO_PADRE", "PG_PROGETTO_PADRE", "TIPO_FASE_PROGETTO_PADRE", "CD_PROGETTO", "DS_PROGETTO", "CD_TIPO_PROGETTO", "CD_UNITA_ORGANIZZATIVA", "CD_RESPONSABILE_TERZO", "DT_INIZIO", "DT_FINE", "DT_PROROGA", "IMPORTO_PROGETTO", "IMPORTO_DIVISA", "CD_DIVISA", "NOTE", "STATO", "CONDIVISO", "DURATA_PROGETTO", "LIVELLO", "CD_DIPARTIMENTO", "FL_UTILIZZABILE", "FL_PIANO_TRIENNALE", "DACR", "UTCR", "DUVA", "UTUV", "PG_VER_REC", "CD_PROGETTO_SIP", "CD_PROGRAMMA", "CD_MISSIONE", "PG_PROGETTO_OTHER_FIELD") AS 
  SELECT
--
-- Date: 13/11/2006
-- Version: 1.1
--
-- Vista dei Progetti esistenti in SIC piu quelli esistenti in SIP
-- in cui la chiave primaria dei record presenti ? "ESERCIZIO" "PG_PROGETTO" "TIPO_FASE"
-- pi? i record con valore 'X' per garantire la chiave per "ESERCIZIO" "PG_PROGETTO"
--
-- History
--
-- Date :08/11/2005
-- Version: 1.0
-- Creazione
--
-- Date: 13/11/2006
-- Version: 1.1
-- Aggiunta la selezione del progetto/commessa/modulo per anno
--
-- Body
--
          esercizio, pg_progetto, tipo_fase, esercizio_progetto_padre,
          pg_progetto_padre, tipo_fase_progetto_padre, cd_progetto,
          ds_progetto, cd_tipo_progetto, cd_unita_organizzativa,
          cd_responsabile_terzo, dt_inizio, dt_fine, dt_proroga,
          importo_progetto, importo_divisa, cd_divisa, note, stato, condiviso,
          durata_progetto, livello, cd_dipartimento, fl_utilizzabile, fl_piano_triennale,
          dacr, utcr, duva, utuv, pg_ver_rec, cd_progetto, cd_programma, cd_missione,
          pg_progetto_other_field
     FROM progetto_sip
    WHERE tipo_fase != 'X'
   UNION
   SELECT esercizio, pg_progetto, 'X' tipo_fase, esercizio_progetto_padre,
          pg_progetto_padre,
          DECODE (pg_progetto_padre,
                  NULL, NULL,
                  'X'
                 ) tipo_fase_progetto_padre, cd_progetto, ds_progetto,
          cd_tipo_progetto, cd_unita_organizzativa, cd_responsabile_terzo,
          dt_inizio, dt_fine, dt_proroga, importo_progetto, importo_divisa,
          cd_divisa, note, stato, condiviso, durata_progetto, livello,
          cd_dipartimento, fl_utilizzabile, fl_piano_triennale,
          dacr, utcr, duva, utuv, pg_ver_rec,
          cd_progetto, cd_programma, cd_missione,
          pg_progetto_other_field
     FROM progetto_sip
    WHERE tipo_fase = 'G'
   UNION
   SELECT esercizio, pg_progetto, 'X' tipo_fase, esercizio_progetto_padre,
          pg_progetto_padre,
          DECODE (pg_progetto_padre,
                  NULL, NULL,
                  'X'
                 ) tipo_fase_progetto_padre, cd_progetto, ds_progetto,
          cd_tipo_progetto, cd_unita_organizzativa, cd_responsabile_terzo,
          dt_inizio, dt_fine, dt_proroga, importo_progetto, importo_divisa,
          cd_divisa, note, stato, condiviso, durata_progetto, livello,
          cd_dipartimento, fl_utilizzabile, fl_piano_triennale,
          dacr, utcr, duva, utuv, pg_ver_rec,
          cd_progetto, cd_programma, cd_missione,
          pg_progetto_other_field
     FROM progetto_sip
    WHERE tipo_fase = 'P'
      AND NOT EXISTS (
             SELECT '1'
               FROM progetto_sip a
              WHERE a.esercizio = progetto_sip.esercizio
                AND a.pg_progetto = progetto_sip.pg_progetto
                AND a.tipo_fase = 'G');
