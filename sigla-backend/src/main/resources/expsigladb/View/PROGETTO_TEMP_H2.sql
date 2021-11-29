--------------------------------------------------------
--  DDL for View PROGETTO_TEMP
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "PROGETTO_TEMP" ("ESERCIZIO", "PG_PROGETTO", "TIPO_FASE", "ESERCIZIO_PROGETTO_PADRE", "PG_PROGETTO_PADRE", "TIPO_FASE_PROGETTO_PADRE", "CD_PROGETTO", "DS_PROGETTO", "CD_TIPO_PROGETTO", "CD_UNITA_ORGANIZZATIVA", "CD_RESPONSABILE_TERZO", "DT_INIZIO", "DT_FINE", "DT_PROROGA", "IMPORTO_PROGETTO", "IMPORTO_DIVISA", "CD_DIVISA", "NOTE", "STATO", "CONDIVISO", "DURATA_PROGETTO", "LIVELLO", "CD_DIPARTIMENTO", "FL_UTILIZZABILE", "FL_PIANO_TRIENNALE", "DACR", "UTCR", "DUVA", "UTUV", "PG_VER_REC", "CD_PROGETTO_SIP", "CD_PROGRAMMA", "CD_MISSIONE", "PG_PROGETTO_OTHER_FIELD") AS 
  SELECT
--
-- Date: 13/11/2006
-- Version: 1.1
--
-- Vista dei Progetti esistenti in SIC piu quelli esistenti in SIP
-- in cui la chiave primaria dei record presenti ? "ESERCIZIO" "PG_PROGETTO" "TIPO_FASE"
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
          ds_progetto ds_progetto, cd_tipo_progetto, cd_unita_organizzativa,
          cd_responsabile_terzo, dt_inizio, dt_fine, dt_proroga,
          importo_progetto, importo_divisa, cd_divisa, note, stato, condiviso,
          durata_progetto, livello, cd_dipartimento, 'Y', fl_piano_triennale,
          dacr, utcr, duva, utuv, pg_ver_rec, NULL cd_progetto_sip,
          nvl(cd_programma, cd_dipartimento) cd_programma, cd_missione,
          pg_progetto_other_field
     FROM progetto_sic
    WHERE NOT EXISTS (
             SELECT 1
               FROM progetto_sip
              WHERE progetto_sip.esercizio = progetto_sic.esercizio
                AND progetto_sip.pg_progetto = progetto_sic.pg_progetto
                AND progetto_sip.tipo_fase = progetto_sic.tipo_fase)
   UNION ALL
   SELECT progetto_sip.esercizio, progetto_sip.pg_progetto,
          progetto_sip.tipo_fase, progetto_sip.esercizio_progetto_padre,
          progetto_sip.pg_progetto_padre,
          progetto_sip.tipo_fase_progetto_padre, progetto_sip.cd_progetto,
             DECODE (progetto_sic.cd_progetto,
                     NULL, NULL,
                     '(' || progetto_sic.cd_progetto || ') '
                    )
          || progetto_sip.ds_progetto ds_progetto,
          progetto_sip.cd_tipo_progetto, progetto_sip.cd_unita_organizzativa,
          progetto_sip.cd_responsabile_terzo, progetto_sip.dt_inizio,
          progetto_sip.dt_fine, progetto_sip.dt_proroga,
          progetto_sip.importo_progetto, progetto_sip.importo_divisa,
          progetto_sip.cd_divisa, progetto_sip.note, progetto_sip.stato,
          progetto_sip.condiviso, progetto_sip.durata_progetto,
          progetto_sip.livello, progetto_sip.cd_dipartimento,
          progetto_sip.fl_utilizzabile, progetto_sip.fl_piano_triennale,
          progetto_sip.dacr, progetto_sip.utcr,
          progetto_sip.duva, progetto_sip.utuv, progetto_sip.pg_ver_rec,
          progetto_sip.cd_progetto cd_progetto_sip,
          nvl(progetto_sip.cd_programma, progetto_sip.cd_dipartimento) cd_programma,
          progetto_sip.cd_missione, progetto_sip.pg_progetto_other_field
     FROM progetto_sip left outer join  progetto_sic
       On progetto_sip.esercizio = progetto_sic.esercizio
      AND progetto_sip.pg_progetto = progetto_sic.pg_progetto
      AND progetto_sip.tipo_fase = progetto_sic.tipo_fase;
