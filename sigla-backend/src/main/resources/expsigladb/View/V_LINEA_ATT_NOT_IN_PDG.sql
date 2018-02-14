--------------------------------------------------------
--  DDL for View V_LINEA_ATT_NOT_IN_PDG
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_LINEA_ATT_NOT_IN_PDG" ("ESERCIZIO", "ESERCIZIO_INIZIO", "CD_LINEA_ATTIVITA", "PG_PROGETTO", "CD_PROGETTO", "CD_PROGETTO_PADRE", "DS_LINEA_ATTIVITA", "CD_CENTRO_RESPONSABILITA", "CD_INSIEME_LA", "TI_GESTIONE", "DENOMINAZIONE", "CD_GRUPPO_LINEA_ATTIVITA", "CD_FUNZIONE", "CD_NATURA", "CD_TIPO_LINEA_ATTIVITA", "CD_CDR_COLLEGATO", "CD_LA_COLLEGATO", "ESERCIZIO_FINE", "DACR", "UTCR", "DUVA", "UTUV", "PG_VER_REC", "TI_APPARTENENZA", "TI_GESTIONE_VOCE", "CD_ELEMENTO_VOCE", "CD_RESPONSABILE_TERZO", "FL_LIMITE_ASS_OBBLIG", "CD_COFOG", "CD_PROGRAMMA", "CD_MISSIONE") AS 
  SELECT
--
-- Date: 09/11/2006
-- Version: 1.8
--
-- Estrae tutte le linee di attivita di tipo SPESA; per quelle presenti nel pdg viene
-- estratto anche l'elemento voce. E' utilizzata dall'on line della gestione obbligazione
-- per recuperare le linee di attivit? non presenti nel pdg dato un certo elemento_voce.
--
-- History:
--
-- Date: 10/09/2001
-- Version: 1.0
-- Creazione
--
-- Date: 14/09/2001
-- Version: 1.1
-- Adattamento alle modifiche di LINEA_ATTIVITA (eliminato OBIETTIVO,
-- aggiunti CD_CDR_COLLEGATO, CD_LA_COLLEGATO)
--
-- Date: 09/11/2001
-- Version: 1.2
-- modifiche per eliminazione ESERCIZIO dalla chiave della struttura organizz.
--
-- Date: 20/11/2001
-- Version: 1.3
-- aggiunto colonna ESERCIZIO da V_LINEA_ATTIVITA_VALIDA
--
-- Date: 26/02/2002
-- Version: 1.4
-- Introduzione insieme Linea Attivita e Gestione Linea attivita
--
-- Date: 27/02/2002
-- Version: 1.5
-- Filtri su  TI_GESTIONE='S'
--
-- Date: 30/09/2002
-- Version: 1.6
-- Aggiunta UNION per selezionare le linee di attivita da pdg con il relativo elemento_voce
--
-- Date: 12/01/2006
-- Version: 1.7
-- Aggiunto il recupero dei dati da PDG_MODULO_SPESE_GEST se l'esercizio ? superiore al 2005
--
-- Date: 09/11/2006
-- Version: 1.8
-- Aggiunta la selezione del progetto/commessa/modulo per anno
--
-- Date: 11/04/2017
-- Version: 1.9
-- Gestione Linea attivit�  di tipo Entrata/Spesa (ti_gestione='X')
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
          'S' ti_gestione, a.denominazione, a.cd_gruppo_linea_attivita,
          a.cd_funzione, a.cd_natura, a.cd_tipo_linea_attivita,
          a.cd_cdr_collegato, a.cd_la_collegato, a.esercizio_fine, a.dacr,
          a.utcr, a.duva, a.utuv, a.pg_ver_rec, b.ti_appartenenza,
          b.ti_gestione, b.cd_elemento_voce, a.cd_responsabile_terzo,
          a.fl_limite_ass_obblig, a.cd_cofog, a.cd_programma, a.cd_missione
     FROM pdg_preventivo_spe_det b,
          linea_attivita a,
          cdr,
          unita_organizzativa,
          parametri_cds
    WHERE b.categoria_dettaglio = 'SIN'
--AND B.STATO = 'Y'
      AND b.ti_appartenenza = 'D'
      AND b.ti_gestione = 'S'
      AND a.ti_gestione in ('S', 'X')
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
   SELECT pdg_modulo_spese_gest.esercizio, linea_attivita.esercizio_inizio,
          linea_attivita.cd_linea_attivita, linea_attivita.pg_progetto,
          (SELECT cd_progetto
             FROM progetto_gest progetto
            WHERE pdg_modulo_spese_gest.esercizio =
                                               progetto.esercizio
              AND linea_attivita.pg_progetto = progetto.pg_progetto)
                                                                  cd_progetto,
          (SELECT cd_progetto
             FROM progetto_gest progetto_padre
            WHERE progetto_padre.esercizio =
                            pdg_modulo_spese_gest.esercizio
              AND progetto_padre.pg_progetto =
                     (SELECT pg_progetto_padre
                        FROM progetto_gest progetto
                       WHERE pdg_modulo_spese_gest.esercizio =
                                                            progetto.esercizio
                         AND linea_attivita.pg_progetto = progetto.pg_progetto))
                                                            cd_progetto_padre,
          linea_attivita.ds_linea_attivita,
          linea_attivita.cd_centro_responsabilita,
          linea_attivita.cd_insieme_la, 'S' ti_gestione,
          linea_attivita.denominazione,
          linea_attivita.cd_gruppo_linea_attivita, linea_attivita.cd_funzione,
          linea_attivita.cd_natura, linea_attivita.cd_tipo_linea_attivita,
          linea_attivita.cd_cdr_collegato, linea_attivita.cd_la_collegato,
          linea_attivita.esercizio_fine, linea_attivita.dacr,
          linea_attivita.utcr, linea_attivita.duva, linea_attivita.utuv,
          linea_attivita.pg_ver_rec, pdg_modulo_spese_gest.ti_appartenenza,
          pdg_modulo_spese_gest.ti_gestione,
          pdg_modulo_spese_gest.cd_elemento_voce,
          linea_attivita.cd_responsabile_terzo,
          linea_attivita.fl_limite_ass_obblig, linea_attivita.cd_cofog,
          linea_attivita.cd_programma, linea_attivita.cd_missione
     FROM pdg_modulo_spese_gest, v_linea_attivita_valida linea_attivita
    WHERE pdg_modulo_spese_gest.categoria_dettaglio = 'DIR'
      AND pdg_modulo_spese_gest.ti_appartenenza = 'D'
      AND pdg_modulo_spese_gest.ti_gestione = 'S'
      AND linea_attivita.ti_gestione in ('S', 'X')
      AND linea_attivita.esercizio = pdg_modulo_spese_gest.esercizio
      AND linea_attivita.cd_centro_responsabilita =
                                pdg_modulo_spese_gest.cd_centro_responsabilita
      AND linea_attivita.cd_linea_attivita =
                                       pdg_modulo_spese_gest.cd_linea_attivita
   UNION
   SELECT voce_f_saldi_cdr_linea.esercizio, linea_attivita.esercizio_inizio,
          linea_attivita.cd_linea_attivita, linea_attivita.pg_progetto,
          (SELECT cd_progetto
             FROM progetto_gest progetto
            WHERE voce_f_saldi_cdr_linea.esercizio =
                                               progetto.esercizio
              AND linea_attivita.pg_progetto = progetto.pg_progetto)
                                                                  cd_progetto,
          (SELECT cd_progetto
             FROM progetto_gest progetto_padre
            WHERE progetto_padre.esercizio =
                           voce_f_saldi_cdr_linea.esercizio
              AND progetto_padre.pg_progetto =
                     (SELECT pg_progetto_padre
                        FROM progetto_gest progetto
                       WHERE voce_f_saldi_cdr_linea.esercizio =
                                                            progetto.esercizio
                         AND linea_attivita.pg_progetto = progetto.pg_progetto))
                                                            cd_progetto_padre,
          linea_attivita.ds_linea_attivita,
          linea_attivita.cd_centro_responsabilita,
          linea_attivita.cd_insieme_la, 'S' ti_gestione,
          linea_attivita.denominazione,
          linea_attivita.cd_gruppo_linea_attivita, linea_attivita.cd_funzione,
          linea_attivita.cd_natura, linea_attivita.cd_tipo_linea_attivita,
          linea_attivita.cd_cdr_collegato, linea_attivita.cd_la_collegato,
          linea_attivita.esercizio_fine, linea_attivita.dacr,
          linea_attivita.utcr, linea_attivita.duva, linea_attivita.utuv,
          linea_attivita.pg_ver_rec, voce_f_saldi_cdr_linea.ti_appartenenza,
          voce_f_saldi_cdr_linea.ti_gestione,
          voce_f_saldi_cdr_linea.cd_elemento_voce,
          linea_attivita.cd_responsabile_terzo,
          linea_attivita.fl_limite_ass_obblig, linea_attivita.cd_cofog,
          linea_attivita.cd_programma, linea_attivita.cd_missione
     FROM voce_f_saldi_cdr_linea, v_linea_attivita_valida linea_attivita
    WHERE voce_f_saldi_cdr_linea.esercizio >
                                          voce_f_saldi_cdr_linea.esercizio_res
      AND voce_f_saldi_cdr_linea.ti_appartenenza = 'D'
      AND voce_f_saldi_cdr_linea.ti_gestione = 'S'
      AND linea_attivita.ti_gestione in ('S', 'X')
      AND linea_attivita.esercizio = voce_f_saldi_cdr_linea.esercizio
      AND linea_attivita.cd_centro_responsabilita =
                               voce_f_saldi_cdr_linea.cd_centro_responsabilita
      AND linea_attivita.cd_linea_attivita =
                                      voce_f_saldi_cdr_linea.cd_linea_attivita
   UNION
   SELECT esercizio, esercizio_inizio, cd_linea_attivita, pg_progetto,
          cd_progetto, cd_progetto_padre, ds_linea_attivita,
          cd_centro_responsabilita, cd_insieme_la, 'S' ti_gestione, denominazione,
          cd_gruppo_linea_attivita, cd_funzione, cd_natura,
          cd_tipo_linea_attivita, cd_cdr_collegato, cd_la_collegato,
          esercizio_fine, dacr, utcr, duva, utuv, pg_ver_rec, NULL, NULL,
          NULL, a.cd_responsabile_terzo, a.fl_limite_ass_obblig, a.cd_cofog,
          a.cd_programma, a.cd_missione
     FROM v_linea_attivita_valida a
    WHERE ti_gestione in ('S', 'X')
      AND NOT EXISTS (
             SELECT 1
               FROM pdg_preventivo_spe_det b
              WHERE a.cd_linea_attivita = b.cd_linea_attivita
                AND a.cd_centro_responsabilita = b.cd_centro_responsabilita
                AND a.esercizio = b.esercizio
             UNION
             SELECT 1
               FROM pdg_modulo_spese_gest b
              WHERE a.cd_linea_attivita = b.cd_linea_attivita
                AND a.cd_centro_responsabilita = b.cd_centro_responsabilita
                AND a.esercizio = b.esercizio
             UNION
             SELECT 1
               FROM voce_f_saldi_cdr_linea b
              WHERE a.cd_linea_attivita = b.cd_linea_attivita
                AND a.cd_centro_responsabilita = b.cd_centro_responsabilita
                AND a.esercizio = b.esercizio
                AND b.esercizio > b.esercizio_res);
