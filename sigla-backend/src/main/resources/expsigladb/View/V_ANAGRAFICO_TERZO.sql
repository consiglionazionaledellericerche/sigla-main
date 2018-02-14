--------------------------------------------------------
--  DDL for View V_ANAGRAFICO_TERZO
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_ANAGRAFICO_TERZO" ("CD_ANAG", "TI_ITALIANO_ESTERO", "TI_ENTITA", "TI_ENTITA_FISICA", "TI_ENTITA_GIURIDICA", "FL_SOGGETTO_IVA", "CODICE_FISCALE", "PARTITA_IVA", "ID_FISCALE_ESTERO", "RAGIONE_SOCIALE", "NOME", "COGNOME", "VIA_FISCALE", "NUM_CIVICO_FISCALE", "PG_COMUNE_FISCALE", "DS_COMUNE_FISCALE", "CD_PROVINCIA_FISCALE", "DS_PROVINCIA_FISCALE", "CAP_COMUNE_FISCALE", "FRAZIONE_FISCALE", "PG_NAZIONE_FISCALE", "TI_SESSO", "DT_NASCITA", "PG_COMUNE_NASCITA", "DS_COMUNE_NASCITA", "CD_PROVINCIA_NASCITA", "DS_PROVINCIA_NASCITA", "PG_NAZIONE_NAZIONALITA", "FL_FATTURAZIONE_DIFFERITA", "FL_OCCASIONALE", "CD_CLASSIFIC_ANAG", "CD_ATTIVITA_INPS", "ALTRA_ASS_PREVID_INPS", "ALIQUOTA_FISCALE", "CODICE_FISCALE_CAF", "DENOMINAZIONE_CAF", "SEDE_INAIL", "MATRICOLA_INAIL", "CONTO_NUMERARIO_CREDITO", "CONTO_NUMERARIO_DEBITO", "NUM_ISCRIZ_CCIAA", "NUM_ISCRIZ_ALBO", "DT_FINE_RAPPORTO", "CAUSALE_FINE_RAPPORTO", "DT_ANTIMAFIA", "CD_ENTE_APPARTENENZA", "CD_TERZO", "CD_PRECEDENTE", "PG_RAPP_LEGALE", "TI_TERZO", "DENOMINAZIONE_SEDE", "VIA_SEDE", "NUMERO_CIVICO_SEDE", "PG_COMUNE_SEDE", "DS_COMUNE_SEDE", "CD_PROVINCIA_SEDE", "DS_PROVINCIA_SEDE", "CAP_COMUNE_SEDE", "FRAZIONE_SEDE", "CD_UNITA_ORGANIZZATIVA", "NOME_UNITA_ORGANIZZATIVA", "NOTE", "DT_CANC", "DACR", "UTCR", "DUVA", "UTUV", "PG_VER_REC", "FL_CERVELLONE") AS 
  SELECT
--
-- Date: 10/05/2001
-- Version: 1.0
--
-- Vista estrazione terzi con tutte le informazioni di base della anagrafica a cui appartengono--
--
-- History:
-- Date: 10/05/2001
-- Version: 1.0
-- Creazione
--
-- Body:
--
       T.cd_anag,
       A.ti_italiano_estero,
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
       A.via_fiscale,
       A.num_civico_fiscale,
       A.pg_comune_fiscale,
       C1.ds_comune,
       C1.cd_provincia,
       P1.ds_provincia,
       A.cap_comune_fiscale,
       A.frazione_fiscale,
       A.pg_nazione_fiscale,
       A.ti_sesso,
       A.dt_nascita,
       A.pg_comune_nascita,
       C2.ds_comune,
       C2.cd_provincia,
       P2.ds_provincia,
       A.pg_nazione_nazionalita,
       A.fl_fatturazione_differita,
       A.fl_occasionale,
       A.cd_classific_anag,
       A.cd_attivita_inps,
       A.altra_ass_previd_inps,
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
       A.dt_antimafia,
       A.cd_ente_appartenenza,
       T.cd_terzo,
       T.cd_precedente,
       T.pg_rapp_legale,
       T.ti_terzo,
       T.denominazione_sede,
       T.via_sede,
       T.numero_civico_sede,
       T.pg_comune_sede,
       C3.ds_comune,
       C3.cd_provincia,
       P3.ds_provincia,
       T.cap_comune_sede,
       T.frazione_sede,
       T.cd_unita_organizzativa,
       T.nome_unita_organizzativa,
       T.note,
       T.dt_canc,
       T.dacr,
       T.utcr,
       T.duva,
       T.utuv,
       T.pg_ver_rec,
       A.fl_cervellone
FROM   TERZO T,
       ANAGRAFICO A,
       COMUNE C1,
       PROVINCIA P1,
       COMUNE C2,
       PROVINCIA P2,
       COMUNE C3,
       PROVINCIA P3
WHERE  A.cd_anag = T.cd_anag AND
       C1.pg_comune = A.pg_comune_fiscale AND
       P1.cd_provincia (+) = C1.cd_provincia AND
       C2.pg_comune (+)= A.pg_comune_nascita AND
       P2.cd_provincia (+) = C2.cd_provincia AND
       C3.pg_comune = T.pg_comune_sede AND
       P3.cd_provincia (+) = C3.cd_provincia;
