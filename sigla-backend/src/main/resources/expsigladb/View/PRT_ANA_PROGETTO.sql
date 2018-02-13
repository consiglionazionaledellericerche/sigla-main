--------------------------------------------------------
--  DDL for View PRT_ANA_PROGETTO
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "PRT_ANA_PROGETTO" ("PG_PROGETTO", "PG_PROGETTO_PADRE", "CD_PROGETTO", "DS_PROGETTO", "CD_TIPO_PROGETTO", "CD_UNITA_ORGANIZZATIVA", "CD_RESPONSABILE_TERZO", "CD_DIPARTIMENTO", "DT_INIZIO", "DT_FINE", "DT_PROROGA", "IMPORTO_PROGETTO", "IMPORTO_DIVISA", "CD_DIVISA", "NOTE", "STATO", "CONDIVISO", "DURATA_PROGETTO", "LIVELLO", "DACR", "UTCR", "DUVA", "UTUV", "PG_VER_REC", "CD_PARTNER_ESTERNO", "DS_PARTNER_ESTERNO", "IMPORTO", "CD_FINANZIATORE_TERZO", "DS_FINANZIATORE_TERZO", "IMPORTO_FINANZIATO", "CD_UNITA_PARTECIPANTE", "DS_UNITA_PARTECIPANTE") AS 
  SELECT     progetto.pg_progetto, progetto.pg_progetto_padre,
              progetto.cd_progetto, progetto.ds_progetto,
              progetto.cd_tipo_progetto, progetto.cd_unita_organizzativa,
              progetto.cd_responsabile_terzo, progetto.dt_inizio,
              progetto.dt_fine, progetto.dt_proroga,
              progetto.importo_progetto, progetto.importo_divisa,
              progetto.cd_divisa, progetto.note, progetto.stato,
              progetto.condiviso, progetto.durata_progetto, progetto.livello,
              progetto.dacr, progetto.utcr, progetto.duva, progetto.utuv,
              progetto.pg_ver_rec, progetto.cd_dipartimento,
              progetto_partner_esterno.cd_partner_esterno,
              cnrutl001.getvalore
                 ('TERZO',
                  'DENOMINAZIONE_SEDE',
                  progetto_partner_esterno.cd_partner_esterno
                 ) ds_partner_esterno,
              progetto_partner_esterno.importo,
              progetto_finanziatore.cd_finanziatore_terzo,
              cnrutl001.getvalore
                 ('TERZO',
                  'DENOMINAZIONE_SEDE',
                  progetto_finanziatore.cd_finanziatore_terzo
                 ) ds_finanziatore_terzo,
              progetto_finanziatore.importo_finanziato,
              progetto_uo.cd_unita_organizzativa cd_unita_partecipante,
              cnrutl001.getvalore
                    ('UNITA_ORGANIZZATIVA',
                     'DS_UNITA_ORGANIZZATIVA',
                     progetto_uo.cd_unita_organizzativa
                    ) ds_unita_partecipante
         FROM progetto,
              progetto_partner_esterno,
              progetto_finanziatore,
              progetto_uo
        WHERE progetto.pg_progetto = progetto_partner_esterno.pg_progetto(+)
          AND progetto.pg_progetto = progetto_finanziatore.pg_progetto(+)
          AND progetto.pg_progetto = progetto_uo.pg_progetto(+)
   START WITH pg_progetto_padre IS NULL
   CONNECT BY PRIOR progetto.pg_progetto = pg_progetto_padre;
