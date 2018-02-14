--------------------------------------------------------
--  DDL for View V_COMUNICAZIONE_PIGNORATI
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_COMUNICAZIONE_PIGNORATI" ("ESERCIZIO", "CREDITORE", "COGNOME_CREDITORE", "NOME_CREDITORE", "RS_CREDITORE", "CF_CREDITORE", "LORDO_PERCIPIENTE", "PIGNORATO", "COGNOME_PIGNORATO", "NOME_PIGNORATO", "RS_PIGNORATO", "INDIRIZZO_PIGNORATO", "CAP_PIGNORATO", "FRAZIONE_PIGNORATO", "COMUNE_PIGNORATO", "PROV_PIGNORATO", "NAZIONE_PIGNORATO", "CF_PIGNORATO") AS 
  SELECT
--
-- Date: 09/03/2012
-- Version: 1.0
--
-- Vista di estrazione compensi con trattamenti utilizzati per liquidare somme relative
-- a pignoramenti presso terzi
--
-- History:
-- Date: 09/03/2012
-- Version: 1.0
-- Creazione
--
-- Body:
--
            TO_NUMBER (TO_CHAR (c.dt_emissione_mandato, 'YYYY')), c.cd_terzo,
            vat.cognome, vat.nome, vat.ragione_sociale, vat.codice_fiscale,
            SUM (c.im_lordo_percipiente), c.cd_terzo_pignorato,
            vatpignorato.cognome, vatpignorato.nome,
            vatpignorato.ragione_sociale,
            vatpignorato.via_fiscale || ' ' || vatpignorato.num_civico_fiscale,
            vatpignorato.cap_comune_fiscale, vatpignorato.frazione_fiscale,
            vatpignorato.ds_comune_fiscale, vatpignorato.ds_provincia_fiscale,
            naz.ds_nazione,vatpignorato.codice_fiscale
       FROM compenso c,
            tipo_trattamento t,
            v_anagrafico_terzo vatpignorato,
            v_anagrafico_terzo vat,
            nazione naz
      WHERE c.cd_trattamento = t.cd_trattamento
        AND c.dt_da_competenza_coge >= t.dt_ini_validita
        AND c.dt_a_competenza_coge <= t.dt_fin_validita
        AND c.cd_terzo_pignorato = vatpignorato.cd_terzo
        AND c.cd_terzo = vat.cd_terzo
        AND vat.pg_nazione_fiscale = naz.pg_nazione
        AND t.fl_pignorato_obbl = 'Y'
        AND c.stato_cofi = 'P'
   GROUP BY TO_NUMBER (TO_CHAR (c.dt_emissione_mandato, 'YYYY')),
            c.cd_terzo,
            vat.cognome,
            vat.nome,
            vat.ragione_sociale,
            vat.codice_fiscale,
            c.cd_terzo_pignorato,
            vatpignorato.cognome,
            vatpignorato.nome,
            vatpignorato.ragione_sociale,
            vatpignorato.via_fiscale || ' ' || vatpignorato.num_civico_fiscale,
            vatpignorato.cap_comune_fiscale,
            vatpignorato.frazione_fiscale,
            vatpignorato.ds_comune_fiscale,
            vatpignorato.ds_provincia_fiscale,
            naz.ds_nazione,
            vatpignorato.codice_fiscale;

   COMMENT ON TABLE "V_COMUNICAZIONE_PIGNORATI"  IS 'Vista di estrazione compensi con trattamenti utilizzati per liquidare somme relative a pignoramenti presso terzi';
