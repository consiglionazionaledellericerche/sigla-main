--------------------------------------------------------
--  DDL for View PROGETTO_GEST_UO
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "PROGETTO_GEST_UO" ("ESERCIZIO", "PG_PROGETTO", "TIPO_FASE", "ESERCIZIO_PROGETTO_PADRE", "PG_PROGETTO_PADRE", "TIPO_FASE_PROGETTO_PADRE", "CD_PROGETTO", "DS_PROGETTO", "CD_TIPO_PROGETTO", "CD_UNITA_ORGANIZZATIVA", "CD_RESPONSABILE_TERZO", "DT_INIZIO", "DT_FINE", "DT_PROROGA", "IMPORTO_PROGETTO", "IMPORTO_DIVISA", "CD_DIVISA", "NOTE", "STATO", "CONDIVISO", "DURATA_PROGETTO", "LIVELLO", "CD_DIPARTIMENTO", "FL_UTILIZZABILE", "DACR", "UTCR", "DUVA", "UTUV", "PG_VER_REC", "CD_PROGETTO_SIP") AS 
  SELECT   DISTINCT
                       LIV2.esercizio,
            LIV2.pg_progetto,
            LIV2.tipo_fase,
            LIV2.esercizio_progetto_padre,
            LIV2.pg_progetto_padre,
            LIV2.tipo_fase_progetto_padre,
            LIV2.cd_progetto,
            LIV2.ds_progetto,
            LIV2.cd_tipo_progetto,
            LIV3.cd_unita_organizzativa,
            LIV2.cd_responsabile_terzo,
            LIV2.dt_inizio,
            LIV2.dt_fine,
            LIV2.dt_proroga,
            LIV2.importo_progetto,
            LIV2.importo_divisa,
            LIV2.cd_divisa,
            LIV2.note,
            LIV2.stato,
            LIV2.condiviso,
            LIV2.durata_progetto,
            LIV2.livello,
            LIV2.cd_dipartimento,
            LIV2.fl_utilizzabile,
            LIV2.dacr,
            LIV2.utcr,
            LIV2.duva,
            LIV2.utuv,
            LIV2.pg_ver_rec,
            LIV2.cd_progetto_sip
    FROM PROGETTO_GEST LIV2, PROGETTO_GEST LIV3
    WHERE LIV2.LIVELLO = 2 AND
                LIV2.ESERCIZIO = LIV3.ESERCIZIO_PROGETTO_PADRE AND
                LIV2.PG_PROGETTO = LIV3.PG_PROGETTO_PADRE AND
                LIV2.TIPO_FASE = LIV3.TIPO_FASE_PROGETTO_PADRE AND
                LIV3.CD_UNITA_ORGANIZZATIVA IS NOT NULL ;
