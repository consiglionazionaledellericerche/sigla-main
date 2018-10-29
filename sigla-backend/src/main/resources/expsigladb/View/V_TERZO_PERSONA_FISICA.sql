--------------------------------------------------------
--  DDL for View V_TERZO_PERSONA_FISICA
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_TERZO_PERSONA_FISICA" ("CD_TERZO", "FRAZIONE_SEDE", "CD_ANAG", "DT_FINE_RAPPORTO", "TI_TERZO", "CD_PRECEDENTE", "DENOMINAZIONE_SEDE", "VIA_SEDE", "NUMERO_CIVICO_SEDE", "PG_COMUNE_SEDE", "CAP_COMUNE_SEDE", "PG_RAPP_LEGALE", "CD_UNITA_ORGANIZZATIVA", "NOME_UNITA_ORGANIZZATIVA", "NOTE", "DT_CANC", "DACR", "UTCR", "DUVA", "UTUV", "PG_VER_REC", "CODICE_FISCALE", "NOME", "COGNOME", "TI_SESSO", "DT_NASCITA", "CODICE_UNIVOCO_UFFICIO_IPA", "CODICE_UNIVOCO_PCC", "DENOMINAZIONE_PCC","CODICE_DESTINATARIO_FATT","FL_SBLOCCO_FATTURA_ELETTRONICA") AS 
  SELECT
--
-- Date: 31/05/2001
-- Version: 1.0
--
-- Vista che estrae tutti terzi legati alle anagrafiche ti dipo 'PERSONA FISICA'
-- e aggiunge alcuni campi dell'anagrafica.
--
-- History:
-- Date: 31/05/2001
-- Version: 1.0
-- Creazione
--
-- Body:
--
          terzo.cd_terzo, terzo.frazione_sede, terzo.cd_anag,
          terzo.dt_fine_rapporto, terzo.ti_terzo, terzo.cd_precedente,
          terzo.denominazione_sede, terzo.via_sede, terzo.numero_civico_sede,
          terzo.pg_comune_sede, terzo.cap_comune_sede, terzo.pg_rapp_legale,
          terzo.cd_unita_organizzativa, terzo.nome_unita_organizzativa,
          terzo.note, terzo.dt_canc, terzo.dacr, terzo.utcr, terzo.duva,
          terzo.utuv, terzo.pg_ver_rec, anagrafico.codice_fiscale,
          anagrafico.nome, anagrafico.cognome, anagrafico.ti_sesso,
          anagrafico.dt_nascita, terzo.codice_univoco_ufficio_ipa,terzo.codice_univoco_pcc,terzo.denominazione_pcc,CODICE_DESTINATARIO_FATT,FL_SBLOCCO_FATTURA_ELETTRONICA
     FROM terzo, anagrafico
    WHERE terzo.cd_anag = anagrafico.cd_anag AND anagrafico.ti_entita = 'F';

   COMMENT ON TABLE "V_TERZO_PERSONA_FISICA"  IS 'Vista che estrae tutti terzi legati alle anagrafiche ti dipo ''PERSONA FISICA'' e aggiunge alcuni campi dell''anagrafica.';
