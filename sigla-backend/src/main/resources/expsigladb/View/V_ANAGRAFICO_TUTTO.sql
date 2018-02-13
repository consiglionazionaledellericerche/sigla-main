--------------------------------------------------------
--  DDL for View V_ANAGRAFICO_TUTTO
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_ANAGRAFICO_TUTTO" ("CD_ANAG", "CD_CLASSIFIC_ANAG", "TI_ENTITA", "TI_ENTITA_FISICA", "TI_ENTITA_GIURIDICA", "FL_SOGGETTO_IVA", "CODICE_FISCALE", "PARTITA_IVA", "ID_FISCALE_ESTERO", "RAGIONE_SOCIALE", "NOME", "COGNOME", "TI_SESSO", "DT_NASCITA", "PG_COMUNE_NASCITA", "DS_COMUNE_NASCITA", "CD_PROVINCIA_NASCITA", "DS_PROVINCIA_NASCITA", "PG_COMUNE_FISCALE", "DS_COMUNE_FISCALE", "CD_PROVINCIA_FISCALE", "DS_PROVINCIA_FISCALE", "VIA_FISCALE", "NUM_CIVICO_FISCALE", "CAP_COMUNE_FISCALE", "FRAZIONE_FISCALE", "PG_NAZIONE_FISCALE", "PG_NAZIONE_NAZIONALITA", "FL_FATTURAZIONE_DIFFERITA", "FL_OCCASIONALE", "TI_ITALIANO_ESTERO", "CD_ATTIVITA_INPS", "DS_ATTIVITA_INPS", "ALTRA_ASS_PREVID_INPS", "DS_ALTRA_ASS_PREVID_INPS", "ALIQUOTA_FISCALE", "CODICE_FISCALE_CAF", "DENOMINAZIONE_CAF", "SEDE_INAIL", "MATRICOLA_INAIL", "CONTO_NUMERARIO_CREDITO", "CONTO_NUMERARIO_DEBITO", "NUM_ISCRIZ_CCIAA", "NUM_ISCRIZ_ALBO", "DT_FINE_RAPPORTO", "CAUSALE_FINE_RAPPORTO", "DT_CANC", "DT_ANTIMAFIA", "CD_ENTE_APPARTENENZA", "NOTE", "DACR", "UTCR", "DUVA", "UTUV", "PG_VER_REC") AS 
  SELECT
-- =================================================================================================
-- Date: 12/03/2004
-- Version: 1.0
--
-- Vista estrazione anagrafico con la decodifica di tutte le informazioni relative
--
-- History:
--
-- Date: 12/03/2004
-- Version: 1.0
--
-- Creazione vista
--
-- Body:
--
-- =================================================================================================
       A.cd_anag,
       A.cd_classific_anag,
       A.ti_entita,
       A.ti_entita_fisica,
       A.ti_entita_giuridica,
       A.fl_soggetto_iva,
       A.codice_fiscale,
       A.partita_iva,
       A.id_fiscale_estero,
       A.ragione_sociale,
       A.nome,
       A.cognome,
       A.ti_sesso,
       A.dt_nascita,
       A.pg_comune_nascita,
       C1.ds_comune,
       C1.cd_provincia,
       P1.ds_provincia,
       A.pg_comune_fiscale,
       C2.ds_comune,
       C2.cd_provincia,
       P2.ds_provincia,
       A.via_fiscale,
       A.num_civico_fiscale,
       A.cap_comune_fiscale,
       A.frazione_fiscale,
       A.pg_nazione_fiscale,
       A.pg_nazione_nazionalita,
       A.fl_fatturazione_differita,
       A.fl_occasionale,
       A.ti_italiano_estero,
       A.cd_attivita_inps,
       D.ds_attivita_inps,
       A.altra_ass_previd_inps,
       E.ds_altra_ass_previd_inps,
       A.aliquota_fiscale,
       A.codice_fiscale_caf,
       A.denominazione_caf,
       A.sede_inail,
       A.matricola_inail,
       A.conto_numerario_credito,
       A.conto_numerario_debito,
       A.num_iscriz_cciaa,
       A.num_iscriz_albo,
       A.dt_fine_rapporto,
       A.causale_fine_rapporto,
       A.dt_canc,
       A.dt_antimafia,
       A.cd_ente_appartenenza,
       A.note,
       A.dacr,
       A.utcr,
       A.duva,
       A.utuv,
       A.pg_ver_rec
FROM   ANAGRAFICO A,
       COMUNE C1,
       PROVINCIA P1,
       COMUNE C2,
       PROVINCIA P2,
       CODICI_ATTIVITA_INPS D,
       CODICI_ALTRA_FORMA_ASS_INPS E
WHERE  C1.pg_comune (+) = A.pg_comune_nascita AND
       P1.cd_provincia (+) = C1.cd_provincia AND
       C2.pg_comune = A.pg_comune_fiscale AND
       P2.cd_provincia (+) = C2.cd_provincia AND
       D.cd_attivita_inps (+) = A.cd_attivita_inps AND
       E.altra_ass_previd_inps (+) = A.altra_ass_previd_inps
;

   COMMENT ON TABLE "V_ANAGRAFICO_TUTTO"  IS 'Vista estrazione anagrafico con la decodifica di tutte le informazioni relative';
