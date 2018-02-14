--------------------------------------------------------
--  DDL for View V_CONS_GAE_COMPETENZA_SPE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_CONS_GAE_COMPETENZA_SPE" ("ESERCIZIO", "CD_DIPARTIMENTO", "PG_PROGETTO", "CD_PROGETTO", "DS_PROGETTO", "PG_COMMESSA", "CD_COMMESSA", "DS_COMMESSA", "PG_MODULO", "CD_MODULO", "DS_MODULO", "CD_CENTRO_RESPONSABILITA", "CD_CDS", "CD_LINEA_ATTIVITA", "DS_LINEA_ATTIVITA", "TI_APPARTENENZA", "TI_GESTIONE", "CD_ELEMENTO_VOCE", "DS_ELEMENTO_VOCE", "IM_STANZ_INIZIALE_A1", "PG_VARIAZIONE_PDG", "DS_VARIAZIONE", "VARIAZIONI_PIU", "VARIAZIONI_MENO", "CD_CDS_OBB", "PG_OBBLIGAZIONE", "PG_OBBLIGAZIONE_SCADENZARIO", "DS_SCADENZA", "IMPEGNI_COMP", "CD_CDS_MAN", "PG_MANDATO", "DS_MANDATO", "MANDATI_COMP", "DS_NATURA") AS 
  SELECT /*+ optimizer_features_enable('10.1.0') */
--
-- Date: 23/10/2008
-- Version: 1.1
--
-- Vista per la consultazione GAE Residui Spese
--
-- History:
-- Date: ?
-- Version: 1.0
-- Creazione
--
-- Date: 23/10/2008
-- Version: 1.1
-- Aggiunto il campo CD_CDS
-- Body:
--
          saldi.esercizio, progetto.cd_dipartimento, progetto.pg_progetto,
          progetto.cd_progetto, progetto.ds_progetto, com.pg_progetto,
          com.cd_progetto, com.ds_progetto, modu.pg_progetto,
          modu.cd_progetto, modu.ds_progetto, saldi.cd_centro_responsabilita,
          cnrutl001.getcdsfromcdr (saldi.cd_centro_responsabilita) cd_cds,
          saldi.cd_linea_attivita, denominazione, saldi.ti_appartenenza,
          saldi.ti_gestione, saldi.cd_elemento_voce,
          (SELECT ds_elemento_voce
             FROM elemento_voce
            WHERE esercizio = saldi.esercizio
              AND ti_appartenenza = saldi.ti_appartenenza
              AND ti_gestione = saldi.ti_gestione
              AND cd_elemento_voce = saldi.cd_elemento_voce),
          saldi.im_stanz_iniziale_a1, saldi.pg_variazione_pdg,
          saldi.ds_variazione, saldi.variazioni_piu, saldi.variazioni_meno,
          saldi.cd_cds_obb, saldi.pg_obbligazione,
          saldi.pg_obbligazione_scadenzario, saldi.ds_scadenza,
          saldi.impegni_comp, saldi.cd_cds_man, saldi.pg_mandato,
          saldi.ds_mandato, saldi.mandati_comp,
          DECODE (natura.tipo,
                  'FIN', 'Fonti Interne',
                  'Fonti Esterne'
                 ) ds_natura
     FROM linea_attivita,
          progetto_gest progetto,
          progetto_gest com,
          progetto_gest modu,
          natura,
          v_cons_saldi_competenza_spe saldi,
          parametri_cnr
    WHERE parametri_cnr.esercizio = saldi.esercizio
      AND parametri_cnr.fl_nuovo_pdg = 'N'
      AND saldi.cd_centro_responsabilita = linea_attivita.cd_centro_responsabilita
      AND saldi.cd_linea_attivita = linea_attivita.cd_linea_attivita
      AND natura.cd_natura = linea_attivita.cd_natura
      AND linea_attivita.pg_progetto IS NOT NULL
      AND modu.esercizio = saldi.esercizio
      AND modu.pg_progetto = linea_attivita.pg_progetto
      AND modu.tipo_fase = 'G'
      AND modu.esercizio_progetto_padre = com.esercizio
      AND modu.pg_progetto_padre = com.pg_progetto
      AND modu.tipo_fase_progetto_padre = com.tipo_fase
      AND com.esercizio_progetto_padre = progetto.esercizio
      AND com.pg_progetto_padre = progetto.pg_progetto
      AND com.tipo_fase_progetto_padre = progetto.tipo_fase
UNION ALL
   SELECT /*+ optimizer_features_enable('10.1.0') */
          saldi.esercizio, 
          (SELECT progetto_gest.cd_dipartimento
             FROM progetto_gest
            WHERE progetto_gest.esercizio = area_prog.esercizio_progetto_padre
              AND progetto_gest.pg_progetto = area_prog.pg_progetto_padre) cd_dipartimento,
          null, null, null,
          (SELECT progetto_gest.pg_progetto
             FROM progetto_gest
            WHERE progetto_gest.esercizio = area_prog.esercizio_progetto_padre
              AND progetto_gest.pg_progetto = area_prog.pg_progetto_padre),
          (SELECT progetto_gest.cd_progetto
             FROM progetto_gest
            WHERE progetto_gest.esercizio = area_prog.esercizio_progetto_padre
              AND progetto_gest.pg_progetto = area_prog.pg_progetto_padre),
          (SELECT progetto_gest.ds_progetto
             FROM progetto_gest
            WHERE progetto_gest.esercizio = area_prog.esercizio_progetto_padre
              AND progetto_gest.pg_progetto = area_prog.pg_progetto_padre),
          area_prog.pg_progetto, area_prog.cd_progetto, area_prog.ds_progetto, 
          saldi.cd_centro_responsabilita,
          cnrutl001.getcdsfromcdr (saldi.cd_centro_responsabilita) cd_cds,
          saldi.cd_linea_attivita, denominazione, saldi.ti_appartenenza,
          saldi.ti_gestione, saldi.cd_elemento_voce,
          (SELECT ds_elemento_voce
             FROM elemento_voce
            WHERE esercizio = saldi.esercizio
              AND ti_appartenenza = saldi.ti_appartenenza
              AND ti_gestione = saldi.ti_gestione
              AND cd_elemento_voce = saldi.cd_elemento_voce),
          saldi.im_stanz_iniziale_a1, saldi.pg_variazione_pdg,
          saldi.ds_variazione, saldi.variazioni_piu, saldi.variazioni_meno,
          saldi.cd_cds_obb, saldi.pg_obbligazione,
          saldi.pg_obbligazione_scadenzario, saldi.ds_scadenza,
          saldi.impegni_comp, saldi.cd_cds_man, saldi.pg_mandato,
          saldi.ds_mandato, saldi.mandati_comp,
          DECODE (natura.tipo,
                  'FIN', 'Fonti Interne',
                  'Fonti Esterne'
                 ) ds_natura
     FROM v_linea_attivita_valida,
          progetto_gest area_prog,
          natura,
          v_cons_saldi_competenza_spe saldi,
          parametri_cnr
    WHERE parametri_cnr.esercizio = saldi.esercizio
      AND parametri_cnr.fl_nuovo_pdg = 'Y'
      AND saldi.esercizio = v_linea_attivita_valida.esercizio
      AND saldi.cd_centro_responsabilita = v_linea_attivita_valida.cd_centro_responsabilita
      AND saldi.cd_linea_attivita = v_linea_attivita_valida.cd_linea_attivita
      AND natura.cd_natura = v_linea_attivita_valida.cd_natura
      AND v_linea_attivita_valida.pg_progetto IS NOT NULL
      AND area_prog.esercizio = saldi.esercizio
      AND area_prog.pg_progetto = v_linea_attivita_valida.pg_progetto ;
