--------------------------------------------------------
--  DDL for View V_LINEA_ATT_NOT_IN_PDG_ETR
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_LINEA_ATT_NOT_IN_PDG_ETR" ("ESERCIZIO", "ESERCIZIO_INIZIO", "CD_LINEA_ATTIVITA", "PG_PROGETTO", "CD_PROGETTO", "CD_PROGETTO_PADRE", "DS_LINEA_ATTIVITA", "CD_CENTRO_RESPONSABILITA", "CD_INSIEME_LA", "TI_GESTIONE", "DENOMINAZIONE", "CD_GRUPPO_LINEA_ATTIVITA", "CD_FUNZIONE", "CD_NATURA", "CD_TIPO_LINEA_ATTIVITA", "CD_CDR_COLLEGATO", "CD_LA_COLLEGATO", "ESERCIZIO_FINE", "DACR", "UTCR", "DUVA", "UTUV", "PG_VER_REC", "TI_APPARTENENZA", "TI_GESTIONE_VOCE", "CD_ELEMENTO_VOCE", "CD_RESPONSABILE_TERZO", "FL_LIMITE_ASS_OBBLIG", "CD_COFOG", "CD_PROGRAMMA", "CD_MISSIONE") AS 
  SELECT
--
-- Date: 09/11/2006
-- Version: 1.2
--
-- Estrae tutte le linee di attivita di tipo ENTRATA; per quelle presenti nel pdg viene
-- estratto anche l'elemento voce. E' utilizzata dall'on line della gestione accertamento
-- per recuperare le linee di attivit? non presenti nel pdg dato un certo elemento_voce.
--
-- History:
--
-- Body:
--
-- Date: 16/05/2005
-- Version: 1.0
-- Creazione
--
-- Date: 12/01/2006
-- Version: 1.1
-- Aggiunto il recupero dei dati da PDG_MODULO_ENTRATE_GEST se l'esercizio ? superiore al 2005
--
-- Date: 09/11/2006
-- Version: 1.2
-- Aggiunta la selezione del progetto/commessa/modulo per anno
--
-- Date: 11/04/2017
-- Version: 1.3
-- Gestione Linea attivitא di tipo Entrata/Spesa (ti_gestione='X')
--
-- Body:
--
          b.esercizio, a.esercizio_inizio, a.cd_linea_attivita, a.pg_progetto,
          (SELECT cd_progetto
             FROM progetto_prev progetto
            WHERE b.esercizio = progetto.esercizio
              AND a.pg_progetto = progetto.pg_progetto) cd_progetto,
          (SELECT cd_progetto
             FROM progetto_prev progetto_padre
            WHERE progetto_padre.esercizio = b.esercizio
              AND progetto_padre.pg_progetto =
                     (SELECT pg_progetto_padre
                        FROM progetto_prev progetto
                       WHERE b.esercizio = progetto.esercizio
                         AND a.pg_progetto = progetto.pg_progetto))
                                                            cd_progetto_padre,
          a.ds_linea_attivita, a.cd_centro_responsabilita, a.cd_insieme_la,
          'E' ti_gestione, a.denominazione, a.cd_gruppo_linea_attivita,
          a.cd_funzione, a.cd_natura, a.cd_tipo_linea_attivita,
          a.cd_cdr_collegato, a.cd_la_collegato, a.esercizio_fine, a.dacr,
          a.utcr, a.duva, a.utuv, a.pg_ver_rec, b.ti_appartenenza,
          b.ti_gestione, b.cd_elemento_voce, a.cd_responsabile_terzo,
          a.fl_limite_ass_obblig, a.cd_cofog, a.cd_programma, a.cd_missione
     FROM pdg_preventivo_etr_det b,
          linea_attivita a,
          cdr,
          unita_organizzativa,
          parametri_cds
    WHERE b.categoria_dettaglio = 'SIN'
      AND b.stato = 'Y'
      AND b.ti_appartenenza = 'C'
      AND b.ti_gestione = 'E'
      AND a.ti_gestione in ('E', 'X')
      AND a.cd_centro_responsabilita = b.cd_centro_responsabilita
      AND a.cd_linea_attivita = b.cd_linea_attivita
      AND a.cd_centro_responsabilita = cdr.cd_centro_responsabilita
      AND cdr.cd_unita_organizzativa =
                                    unita_organizzativa.cd_unita_organizzativa
      AND parametri_cds.esercizio = b.esercizio
      AND parametri_cds.cd_cds = unita_organizzativa.cd_unita_padre
      AND (   (    parametri_cds.fl_commessa_obbligatoria = 'Y'
               AND a.pg_progetto IS NOT NULL
              )
           OR (parametri_cds.fl_commessa_obbligatoria = 'N')
          )
   UNION
   SELECT pdg_modulo_entrate_gest.esercizio, linea_attivita.esercizio_inizio,
          linea_attivita.cd_linea_attivita, linea_attivita.pg_progetto,
          (SELECT cd_progetto
             FROM progetto_gest progetto
            WHERE pdg_modulo_entrate_gest.esercizio =
                                               progetto.esercizio
              AND linea_attivita.pg_progetto = progetto.pg_progetto)
                                                                  cd_progetto,
          (SELECT cd_progetto
             FROM progetto_gest progetto_padre
            WHERE progetto_padre.esercizio =
                          pdg_modulo_entrate_gest.esercizio
              AND progetto_padre.pg_progetto =
                     (SELECT pg_progetto_padre
                        FROM progetto_gest progetto
                       WHERE pdg_modulo_entrate_gest.esercizio =
                                                            progetto.esercizio
                         AND linea_attivita.pg_progetto = progetto.pg_progetto))
                                                            cd_progetto_padre,
          linea_attivita.ds_linea_attivita,
          linea_attivita.cd_centro_responsabilita,
          linea_attivita.cd_insieme_la, 'E' ti_gestione,
          linea_attivita.denominazione,
          linea_attivita.cd_gruppo_linea_attivita, linea_attivita.cd_funzione,
          linea_attivita.cd_natura, linea_attivita.cd_tipo_linea_attivita,
          linea_attivita.cd_cdr_collegato, linea_attivita.cd_la_collegato,
          linea_attivita.esercizio_fine, linea_attivita.dacr,
          linea_attivita.utcr, linea_attivita.duva, linea_attivita.utuv,
          linea_attivita.pg_ver_rec, pdg_modulo_entrate_gest.ti_appartenenza,
          pdg_modulo_entrate_gest.ti_gestione,
          pdg_modulo_entrate_gest.cd_elemento_voce,
          linea_attivita.cd_responsabile_terzo,
          linea_attivita.fl_limite_ass_obblig, linea_attivita.cd_cofog,
          linea_attivita.cd_programma, linea_attivita.cd_missione
     FROM pdg_modulo_entrate_gest, v_linea_attivita_valida linea_attivita
    WHERE pdg_modulo_entrate_gest.categoria_dettaglio = 'DIR'
      AND pdg_modulo_entrate_gest.ti_appartenenza = 'C'
      AND pdg_modulo_entrate_gest.ti_gestione = 'E'
      AND linea_attivita.ti_gestione in ('E', 'X')
      AND linea_attivita.esercizio = pdg_modulo_entrate_gest.esercizio
      AND linea_attivita.cd_centro_responsabilita =
                              pdg_modulo_entrate_gest.cd_centro_responsabilita
      AND linea_attivita.cd_linea_attivita =
                                     pdg_modulo_entrate_gest.cd_linea_attivita
   UNION
   SELECT a.esercizio, esercizio_inizio, cd_linea_attivita, pg_progetto,
          cd_progetto, cd_progetto_padre, ds_linea_attivita,
          a.cd_centro_responsabilita, cd_insieme_la, 'E' ti_gestione,
          denominazione, cd_gruppo_linea_attivita, a.cd_funzione, a.cd_natura,
          cd_tipo_linea_attivita, cd_cdr_collegato, cd_la_collegato,
          esercizio_fine, a.dacr, a.utcr, a.duva, a.utuv, a.pg_ver_rec,
          e.ti_appartenenza, e.ti_gestione, e.cd_elemento_voce,
          a.cd_responsabile_terzo, a.fl_limite_ass_obblig, a.cd_cofog,
          a.cd_programma, a.cd_missione
     FROM v_linea_attivita_valida a, elemento_voce e
    WHERE a.ti_gestione in ('E', 'X')
      AND a.esercizio = e.esercizio
      AND e.ti_gestione = 'E'
      AND e.ti_appartenenza = 'C'
      AND e.fl_partita_giro = 'N'
      AND NOT EXISTS (
             SELECT 1
               FROM pdg_preventivo_etr_det b
              WHERE a.cd_linea_attivita = b.cd_linea_attivita
                AND a.cd_centro_responsabilita = b.cd_centro_responsabilita
                AND a.esercizio = b.esercizio
             UNION
             SELECT 1
               FROM pdg_modulo_entrate_gest b
              WHERE a.cd_linea_attivita = b.cd_linea_attivita
                AND a.cd_centro_responsabilita = b.cd_cdr_assegnatario
                AND a.esercizio = b.esercizio
                AND e.esercizio = b.esercizio
                AND e.cd_elemento_voce = b.cd_elemento_voce
                AND e.ti_appartenenza = b.ti_appartenenza
                AND e.ti_gestione = b.ti_gestione);
