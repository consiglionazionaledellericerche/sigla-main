--------------------------------------------------------
--  DDL for View V_LINEA_ATTIVITA_VALIDA
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_LINEA_ATTIVITA_VALIDA" ("ESERCIZIO", "ESERCIZIO_INIZIO", "CD_LINEA_ATTIVITA", "PG_PROGETTO", "CD_PROGETTO", "CD_PROGETTO_PADRE", "DS_LINEA_ATTIVITA", "CD_CENTRO_RESPONSABILITA", "CD_INSIEME_LA", "TI_GESTIONE", "DENOMINAZIONE", "CD_GRUPPO_LINEA_ATTIVITA", "CD_FUNZIONE", "CD_NATURA", "CD_TIPO_LINEA_ATTIVITA", "CD_CDR_COLLEGATO", "CD_LA_COLLEGATO", "ESERCIZIO_FINE", "DACR", "UTCR", "DUVA", "UTUV", "PG_VER_REC", "CD_RESPONSABILE_TERZO", "FL_LIMITE_ASS_OBBLIG", "CD_COFOG", "CD_PROGRAMMA", "CD_MISSIONE") AS 
  SELECT
--
--
-- Date: 09/11/2006
-- Version: 1.5
--
-- Estrae tutte le linee di attivita valide nell'esercizio specificato
--
-- History:
--
-- Date: 09/11/2001
-- Version: 1.0
-- Creazione
--
-- Date: 14/11/2001
-- Version: 1.2
-- eliminato controllo ESERCIZIO_FINE IS NULL
--
-- Date: 20/02/2002
-- Version: 1.3
-- Introduzione insieme Linea Attivita e Gestione Linea attivita
--
-- Date: 12/05/2005
-- Version: 1.4
-- Corretto il recupero del codice progetto padre
--
-- Date: 09/11/2006
-- Version: 1.5
-- Aggiunta la selezione del progetto/commessa/modulo per anno
--
-- Date: 06/10/2015
-- Version: 1.6
-- Aggiunta nuova gestione pdg
--
-- Body:
--
          b.esercizio, a.esercizio_inizio, a.cd_linea_attivita,
          NVL (c.pg_progetto, a.pg_progetto),
          (SELECT cd_progetto
             FROM progetto
            WHERE progetto.esercizio = b.esercizio
              AND progetto.pg_progetto = NVL (c.pg_progetto, a.pg_progetto)
              AND progetto.tipo_fase = 'X') cd_progetto,
          (SELECT cd_progetto
             FROM progetto progetto_padre
            WHERE progetto_padre.esercizio = b.esercizio
              AND progetto_padre.pg_progetto =
                     (SELECT pg_progetto_padre
                        FROM progetto progetto
                       WHERE progetto.esercizio = b.esercizio
                         AND progetto.pg_progetto =
                                            NVL (c.pg_progetto, a.pg_progetto)
                         AND progetto.tipo_fase = 'X')
              AND progetto_padre.tipo_fase = 'X') cd_progetto_padre,
          a.ds_linea_attivita, a.cd_centro_responsabilita, a.cd_insieme_la,
          a.ti_gestione, a.denominazione, a.cd_gruppo_linea_attivita,
          a.cd_funzione, a.cd_natura, a.cd_tipo_linea_attivita,
          a.cd_cdr_collegato, a.cd_la_collegato, a.esercizio_fine, a.dacr,
          a.utcr, a.duva, a.utuv, a.pg_ver_rec, a.cd_responsabile_terzo,
          a.fl_limite_ass_obblig, a.cd_cofog, a.cd_programma, a.cd_missione
     FROM linea_attivita a left outer join ass_linea_attivita_esercizio c
            on a.cd_centro_responsabilita = c.cd_centro_responsabilita
            and a.cd_linea_attivita = c.cd_linea_attivita,
          v_esercizi b,
          cdr,
          unita_organizzativa,
          parametri_cds
    WHERE b.esercizio >= c.esercizio
      AND b.esercizio <= c.esercizio_fine
      AND a.cd_centro_responsabilita = cdr.cd_centro_responsabilita
      AND cdr.cd_unita_organizzativa =
                                    unita_organizzativa.cd_unita_organizzativa
      AND parametri_cds.esercizio = b.esercizio
      AND parametri_cds.cd_cds = unita_organizzativa.cd_unita_padre
      AND (   (    parametri_cds.fl_commessa_obbligatoria = 'Y'
               AND NVL (c.pg_progetto, a.pg_progetto) IS NOT NULL
              )
           OR (parametri_cds.fl_commessa_obbligatoria = 'N')
          );
