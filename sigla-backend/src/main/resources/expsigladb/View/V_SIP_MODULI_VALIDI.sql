--------------------------------------------------------
--  DDL for View V_SIP_MODULI_VALIDI
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_SIP_MODULI_VALIDI" ("ESERCIZIO", "PG_PROGETTO", "TIPO_FASE", "FL_CANCELLABILE", "ERR_CANCELLAZIONE", "FL_TERMINABILE", "ERR_TERMINAZIONE") AS 
  SELECT
--
-- Date: 09/02/2007
-- Version: 1.2
--
-- Vista di estrazione di progetti impiegati in fase Previsionale
--
--
-- History:
--
-- Date: 01/01/2006
-- Version: 1.0
-- Creazione
--
-- Date: 09/11/2006
-- Version: 1.1
-- Aggiunto l'anno e la tipologia PREV/GEST nella selezione del progetto
--
-- Date: 09/02/2007
-- Version: 1.2
-- Aggiunto la descrizione più puntuale dell'errore
--
-- Body:
--
          progetto.esercizio, pg_progetto, tipo_fase,
          NVL
             ((SELECT 'N'
                 FROM v_sip_moduli_utilizzati v_sip_moduli_utilizzati
                WHERE v_sip_moduli_utilizzati.esercizio = progetto.esercizio
                  AND v_sip_moduli_utilizzati.pg_progetto =
                                                          progetto.pg_progetto
                  AND v_sip_moduli_utilizzati.tipo_fase = progetto.tipo_fase
                  AND ROWNUM < 2),
              'Y'
             ) fl_cancellabile,
          NVL
             ((SELECT err
                 FROM v_sip_moduli_utilizzati v_sip_moduli_utilizzati
                WHERE v_sip_moduli_utilizzati.esercizio = progetto.esercizio
                  AND v_sip_moduli_utilizzati.pg_progetto =
                                                          progetto.pg_progetto
                  AND v_sip_moduli_utilizzati.tipo_fase = progetto.tipo_fase
                  AND ROWNUM < 2),
              'Errore Assente'
             ) err_cancellazione,
          NVL
             ((SELECT 'N'
                 FROM v_sip_moduli_nuova_previsione v_sip_moduli_nuova_previsione
                WHERE v_sip_moduli_nuova_previsione.esercizio =
                                                            progetto.esercizio
                  AND v_sip_moduli_nuova_previsione.pg_progetto =
                                                          progetto.pg_progetto
                  AND v_sip_moduli_nuova_previsione.tipo_fase =
                                                            progetto.tipo_fase),
              'Y'
             ) fl_terminabile,
             'Impossibile terminare il Modulo, risultano previsioni nel PdGP per l''esercizio '
          || progetto.esercizio err_terminazione
     FROM progetto progetto, parametri_cnr
    WHERE progetto.livello = 3
      AND parametri_cnr.esercizio = progetto.esercizio
      AND parametri_cnr.fl_nuovo_pdg = 'N'
   UNION
-- nuova gestione verifichiamo avendo in input id attivita se esistono previsioni per il pg_progetto_padre
   SELECT progetto.esercizio, pg_progetto, tipo_fase,
          NVL
             ((SELECT 'N'
                 FROM v_sip_moduli_utilizzati v_sip_moduli_utilizzati
                WHERE v_sip_moduli_utilizzati.esercizio =
                                             progetto.esercizio_progetto_padre
                  AND v_sip_moduli_utilizzati.pg_progetto =
                                                    progetto.pg_progetto_padre
                  AND v_sip_moduli_utilizzati.tipo_fase =
                                             progetto.tipo_fase_progetto_padre
                  AND ROWNUM < 2),
              'Y'
             ) fl_cancellabile,
          NVL
             ((SELECT err
                 FROM v_sip_moduli_utilizzati v_sip_moduli_utilizzati
                WHERE v_sip_moduli_utilizzati.esercizio =
                                             progetto.esercizio_progetto_padre
                  AND v_sip_moduli_utilizzati.pg_progetto =
                                                    progetto.pg_progetto_padre
                  AND v_sip_moduli_utilizzati.tipo_fase =
                                             progetto.tipo_fase_progetto_padre
                  AND ROWNUM < 2),
              'Errore Assente'
             ) err_cancellazione,
          NVL
             ((SELECT 'N'
                 FROM v_sip_moduli_nuova_previsione v_sip_moduli_nuova_previsione
                WHERE v_sip_moduli_nuova_previsione.esercizio =
                                             progetto.esercizio_progetto_padre
                  AND v_sip_moduli_nuova_previsione.pg_progetto =
                                                    progetto.pg_progetto_padre
                  AND v_sip_moduli_nuova_previsione.tipo_fase =
                                             progetto.tipo_fase_progetto_padre),
              'Y'
             ) fl_terminabile,
             'Impossibile terminare l''attività, risultano previsioni nel PdGP per l''esercizio '
          || progetto.esercizio err_terminazione
     FROM progetto progetto, parametri_cnr
    WHERE progetto.livello = 3
      AND parametri_cnr.esercizio = progetto.esercizio
      AND parametri_cnr.fl_nuovo_pdg = 'Y'
  UNION
-- nuova gestione verifichiamo avendo in input id attivita se esistono previsioni per il pg_progetto_padre
   SELECT progetto.esercizio, pg_progetto, tipo_fase,
          NVL((SELECT 'N'
                 FROM v_sip_moduli_utilizzati v_sip_moduli_utilizzati
                WHERE v_sip_moduli_utilizzati.esercizio = progetto.esercizio
                  AND v_sip_moduli_utilizzati.pg_progetto =
                                                          progetto.pg_progetto
                  AND v_sip_moduli_utilizzati.tipo_fase = progetto.tipo_fase
                  AND ROWNUM < 2),
              'Y'
             ) fl_cancellabile,
          NVL((SELECT err
                 FROM v_sip_moduli_utilizzati v_sip_moduli_utilizzati
                WHERE v_sip_moduli_utilizzati.esercizio = progetto.esercizio
                  AND v_sip_moduli_utilizzati.pg_progetto =
                                                          progetto.pg_progetto
                  AND v_sip_moduli_utilizzati.tipo_fase = progetto.tipo_fase
                  AND ROWNUM < 2),
              'Errore Assente'
             ) err_cancellazione,
          NVL((SELECT 'N'
                 FROM v_sip_moduli_nuova_previsione v_sip_moduli_nuova_previsione
                WHERE v_sip_moduli_nuova_previsione.esercizio =
                                                            progetto.esercizio
                  AND v_sip_moduli_nuova_previsione.pg_progetto =
                                                          progetto.pg_progetto
                  AND v_sip_moduli_nuova_previsione.tipo_fase =
                                                            progetto.tipo_fase),
              'Y'
             ) fl_terminabile,
             'Impossibile terminare il progetto, risultano previsioni nel PdGP per l''esercizio '|| progetto.esercizio err_terminazione
     FROM progetto progetto, parametri_cnr
    WHERE progetto.livello = 2
      AND parametri_cnr.esercizio = progetto.esercizio
      AND parametri_cnr.fl_nuovo_pdg = 'Y';
