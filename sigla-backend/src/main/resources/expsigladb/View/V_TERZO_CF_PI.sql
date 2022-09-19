--------------------------------------------------------
--  DDL for View V_TERZO_CF_PI
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_TERZO_CF_PI" (
    "CD_TERZO",
    "FRAZIONE_SEDE",
    "CD_ANAG",
    "DT_FINE_RAPPORTO",
    "TI_TERZO",
    "CD_PRECEDENTE",
    "DENOMINAZIONE_SEDE",
    "VIA_SEDE",
    "NUMERO_CIVICO_SEDE",
    "PG_COMUNE_SEDE",
    "CAP_COMUNE_SEDE",
    "PG_RAPP_LEGALE",
    "CD_UNITA_ORGANIZZATIVA",
    "NOME_UNITA_ORGANIZZATIVA",
    "NOTE",
    "DT_CANC",
    "DACR",
    "UTCR",
    "DUVA",
    "UTUV",
    "PG_VER_REC",
    "CODICE_FISCALE_ANAGRAFICO",
    "PARTITA_IVA_ANAGRAFICO",
    "NOME_ANAGRAFICO",
    "COGNOME_ANAGRAFICO",
    "CODICE_UNIVOCO_UFFICIO_IPA",
    "CODICE_UNIVOCO_PCC",
    "DENOMINAZIONE_PCC",
    "CODICE_DESTINATARIO_FATT",
    "FL_SBLOCCO_FATTURA_ELETTRONICA"
  ) AS SELECT
          --
           -- Date: 10/05/2010
           -- Version: 1.0
           --
           -- View per la ricerca del codice fiscale e della partita iva nei codici terzi
           --
           -- History:
           -- Date: 10/05/2010
           -- Version: 1.0
           -- Creazione
           -- History:
           -- Date: 16/09/2022
           -- Version: 1.1
           -- Aggiunti Nome e Cognome
           --
           -- Body:
           --
          t."CD_TERZO", t."FRAZIONE_SEDE", t."CD_ANAG", t."DT_FINE_RAPPORTO",
          t."TI_TERZO", t."CD_PRECEDENTE", t."DENOMINAZIONE_SEDE",
          t."VIA_SEDE", t."NUMERO_CIVICO_SEDE", t."PG_COMUNE_SEDE",
          t."CAP_COMUNE_SEDE", t."PG_RAPP_LEGALE", t."CD_UNITA_ORGANIZZATIVA",
          t."NOME_UNITA_ORGANIZZATIVA", t."NOTE", t."DT_CANC", t."DACR",
          t."UTCR", t."DUVA", t."UTUV", t."PG_VER_REC", a.codice_fiscale,
          a.partita_iva,a.nome, a.cognome, t.CODICE_UNIVOCO_UFFICIO_IPA,t.codice_univoco_pcc,t.denominazione_pcc,t.CODICE_DESTINATARIO_FATT, t.FL_SBLOCCO_FATTURA_ELETTRONICA
     FROM terzo t, anagrafico a
    WHERE t.cd_anag = a.cd_anag;
