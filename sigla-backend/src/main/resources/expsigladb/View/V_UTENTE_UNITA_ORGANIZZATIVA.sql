--------------------------------------------------------
--  DDL for View V_UTENTE_UNITA_ORGANIZZATIVA
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_UTENTE_UNITA_ORGANIZZATIVA" ("CD_UTENTE", "ESERCIZIO", "ESERCIZIO_INIZIO", "CD_UNITA_ORGANIZZATIVA", "CD_PROPRIO_UNITA", "CD_TIPO_UNITA", "FL_CDS", "FL_UO_CDS", "FL_RUBRICA", "CD_UNITA_PADRE", "LIVELLO", "DS_UNITA_ORGANIZZATIVA", "PRC_COPERTURA_OBBLIG_2", "PRC_COPERTURA_OBBLIG_3", "DACR", "CD_AREA_RICERCA", "FL_PRESIDENTE_AREA", "CD_RESPONSABILE", "UTCR", "DUVA", "CD_RESPONSABILE_AMM", "UTUV", "PG_VER_REC", "ESERCIZIO_FINE", "CD_AREA_SCIENTIFICA", "ID_FUNZIONE_PUBBLICA", "CODICEAOOIPA") AS
  SELECT
--
-- Date: 25/03/2010
-- Version: 1.8
--
-- Ritorna tutte le UO su cui UTENTE ha almeno un accesso proprio o ereditato
-- Non controlla validita dell'STO
--
-- History:
--
-- Date: 10/07/2001
-- Version: 1.0
-- Creazione
--
-- Date: 27/08/2001
-- Version: 1.1
-- Aggiunta fl_presidente_area a tabella UNITA_ORGANIZZATIVA
--
-- Date: 09/11/2001
-- Version: 1.2
-- modifiche per eliminazione ESERCIZIO dalla chiave della struttura organizz.
--
-- Date: 20/06/2002
-- Version: 1.3
-- Aggiunto CD_AREA_SCIENTIFICA
--
-- Date: 13/11/2002
-- Version: 1.4
-- Fix eliminazione distinct perche viene fatta una UNION
--
-- Date: 14/11/2002
-- Version: 1.5
-- Ottimizzazione sostanziale per introduzione exists al post di select diretta su UTENTE_UNITA_XXXX
-- Rimossa la join con ruolo visto che esiste foreign key tra UTENTE_UNITA_RUOLO e RUOLO
--
-- Date: 18/11/2002
-- Version: 1.6
-- Errore estrazione UO indotte da utenza template
--
-- Date: 12/12/2002
-- Version: 1.7
-- Limitazione esercizi a quelli maggiori o uguali esercizio inizio specificato in CONFIGURAZIONE_CNR
--
-- Date: 25/03/2010
-- Version: 1.8
-- Aggiunto ID_FUNZIONE_PUBBLICA
--
-- Body:
--
-- UO indotte da UTENTE_UNITA_ACCESSO
-- UO indotte da UTENTE_UNITA_RUOLO
--
          utente.cd_utente, v_unita_organizzativa.esercizio,
          v_unita_organizzativa.esercizio_inizio,
          v_unita_organizzativa.cd_unita_organizzativa,
          v_unita_organizzativa.cd_proprio_unita,
          v_unita_organizzativa.cd_tipo_unita, v_unita_organizzativa.fl_cds,
          v_unita_organizzativa.fl_uo_cds, v_unita_organizzativa.fl_rubrica,
          v_unita_organizzativa.cd_unita_padre, v_unita_organizzativa.livello,
          v_unita_organizzativa.ds_unita_organizzativa,
          v_unita_organizzativa.prc_copertura_obblig_2,
          v_unita_organizzativa.prc_copertura_obblig_3,
          v_unita_organizzativa.dacr, v_unita_organizzativa.cd_area_ricerca,
          v_unita_organizzativa.fl_presidente_area,
          v_unita_organizzativa.cd_responsabile, v_unita_organizzativa.utcr,
          v_unita_organizzativa.duva,
          v_unita_organizzativa.cd_responsabile_amm,
          v_unita_organizzativa.utuv, v_unita_organizzativa.pg_ver_rec,
          v_unita_organizzativa.esercizio_fine,
          v_unita_organizzativa.cd_area_scientifica,
          v_unita_organizzativa.id_funzione_pubblica,
          v_unita_organizzativa.codiceAooIpa
     FROM v_unita_organizzativa, utente, configurazione_cnr
    WHERE configurazione_cnr.esercizio = 0
      AND configurazione_cnr.cd_unita_funzionale = '*'
      AND configurazione_cnr.cd_chiave_primaria = 'ESERCIZIO_SPECIALE'
      AND configurazione_cnr.cd_chiave_secondaria = 'ESERCIZIO_PARTENZA'
      AND v_unita_organizzativa.esercizio >= configurazione_cnr.im01
      AND (   EXISTS (
                 SELECT 1
                   FROM utente_unita_accesso
                  WHERE cd_unita_organizzativa = v_unita_organizzativa.cd_uo
                    AND cd_utente = utente.cd_utente)
           OR EXISTS (
                 SELECT 1
                   FROM utente_unita_ruolo a
                  WHERE a.cd_unita_organizzativa = v_unita_organizzativa.cd_uo
                    AND a.cd_utente = utente.cd_utente)
          )
   UNION
   SELECT
--
-- UO indotte da UTENTE_UNITA_ACCESSO ATTRAVERSO UTENTE TEMPLATE
--
          utente.cd_utente, v_unita_organizzativa.esercizio,
          v_unita_organizzativa.esercizio_inizio,
          v_unita_organizzativa.cd_unita_organizzativa,
          v_unita_organizzativa.cd_proprio_unita,
          v_unita_organizzativa.cd_tipo_unita, v_unita_organizzativa.fl_cds,
          v_unita_organizzativa.fl_uo_cds, v_unita_organizzativa.fl_rubrica,
          v_unita_organizzativa.cd_unita_padre, v_unita_organizzativa.livello,
          v_unita_organizzativa.ds_unita_organizzativa,
          v_unita_organizzativa.prc_copertura_obblig_2,
          v_unita_organizzativa.prc_copertura_obblig_3,
          v_unita_organizzativa.dacr, v_unita_organizzativa.cd_area_ricerca,
          v_unita_organizzativa.fl_presidente_area,
          v_unita_organizzativa.cd_responsabile, v_unita_organizzativa.utcr,
          v_unita_organizzativa.duva,
          v_unita_organizzativa.cd_responsabile_amm,
          v_unita_organizzativa.utuv, v_unita_organizzativa.pg_ver_rec,
          v_unita_organizzativa.esercizio_fine,
          v_unita_organizzativa.cd_area_scientifica,
          v_unita_organizzativa.id_funzione_pubblica,
          v_unita_organizzativa.codiceAooIpa
     FROM v_unita_organizzativa,
          utente utente_templ,
          utente,
          configurazione_cnr
    WHERE configurazione_cnr.esercizio = 0
      AND configurazione_cnr.cd_unita_funzionale = '*'
      AND configurazione_cnr.cd_chiave_primaria = 'ESERCIZIO_SPECIALE'
      AND configurazione_cnr.cd_chiave_secondaria = 'ESERCIZIO_PARTENZA'
      AND v_unita_organizzativa.esercizio >= configurazione_cnr.im01
      AND utente_templ.cd_utente = utente.cd_utente_templ
      AND (   EXISTS (
                 SELECT 1
                   FROM utente_unita_accesso
                  WHERE cd_unita_organizzativa = v_unita_organizzativa.cd_uo
                    AND cd_utente = utente_templ.cd_utente)
           OR EXISTS (
                 SELECT 1
                   FROM utente_unita_ruolo a
                  WHERE a.cd_unita_organizzativa = v_unita_organizzativa.cd_uo
                    AND a.cd_utente = utente_templ.cd_utente)
          )
   UNION
   SELECT utente.cd_utente, v_unita_organizzativa.esercizio,
          v_unita_organizzativa.esercizio_inizio,
          v_unita_organizzativa.cd_unita_organizzativa,
          v_unita_organizzativa.cd_proprio_unita,
          v_unita_organizzativa.cd_tipo_unita, v_unita_organizzativa.fl_cds,
          v_unita_organizzativa.fl_uo_cds, v_unita_organizzativa.fl_rubrica,
          v_unita_organizzativa.cd_unita_padre, v_unita_organizzativa.livello,
          v_unita_organizzativa.ds_unita_organizzativa,
          v_unita_organizzativa.prc_copertura_obblig_2,
          v_unita_organizzativa.prc_copertura_obblig_3,
          v_unita_organizzativa.dacr, v_unita_organizzativa.cd_area_ricerca,
          v_unita_organizzativa.fl_presidente_area,
          v_unita_organizzativa.cd_responsabile, v_unita_organizzativa.utcr,
          v_unita_organizzativa.duva,
          v_unita_organizzativa.cd_responsabile_amm,
          v_unita_organizzativa.utuv, v_unita_organizzativa.pg_ver_rec,
          v_unita_organizzativa.esercizio_fine,
          v_unita_organizzativa.cd_area_scientifica,
          v_unita_organizzativa.id_funzione_pubblica,
          v_unita_organizzativa.codiceAooIpa
     FROM v_unita_organizzativa, utente, configurazione_cnr
    WHERE configurazione_cnr.esercizio = 0
      AND configurazione_cnr.cd_unita_funzionale = '*'
      AND configurazione_cnr.cd_chiave_primaria = 'ESERCIZIO_SPECIALE'
      AND configurazione_cnr.cd_chiave_secondaria = 'ESERCIZIO_PARTENZA'
      AND v_unita_organizzativa.esercizio >= configurazione_cnr.im01
      AND utente.fl_supervisore = 'Y'
      AND EXISTS (
             SELECT 1
               FROM ruolo_accesso, ruolo, ass_tipo_ruolo_privilegio
              WHERE ruolo_accesso.cd_ruolo = ruolo.cd_ruolo
                AND ruolo.tipo = ass_tipo_ruolo_privilegio.tipo
                AND ass_tipo_ruolo_privilegio.cd_privilegio = 'SUPVIS');

   COMMENT ON TABLE "V_UTENTE_UNITA_ORGANIZZATIVA"  IS 'Vista estrazione tutte le unita organizzative su cui UTENTE ha almeno un accesso proprio o ereditato. Non controlla validita dell''STO';
