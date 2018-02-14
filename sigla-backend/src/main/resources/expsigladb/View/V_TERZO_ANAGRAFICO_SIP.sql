--------------------------------------------------------
--  DDL for View V_TERZO_ANAGRAFICO_SIP
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_TERZO_ANAGRAFICO_SIP" ("CD_TERZO", "CD_ANAG", "DENOMINAZIONE_SEDE", "TI_ENTITA", "CODICE_FISCALE_PARIVA", "NOME", "COGNOME", "VIA_FISCALE", "NUM_CIVICO_FISCALE", "CAP_COMUNE_FISCALE", "PG_NAZIONE_FISCALE", "PG_COMUNE_FISCALE", "DT_NASCITA", "PG_COMUNE_NASCITA", "SESSO", "COMUNE_FISCALE", "COMUNE_NASCITA", "DS_NAZIONE") AS 
  SELECT
--
-- Date: 19/07/2006
-- Version: 1.0
--
-- Vista che estrae tutti terzi legati alle anagrafiche per il sistema programmatico
--
-- History:
-- Date: 19/07/2006
-- Version: 1.0
-- Creazione
--
-- Body:
--
	   TERZO.CD_TERZO,
	   TERZO.CD_ANAG,
	   TERZO.DENOMINAZIONE_SEDE,
	   ANAGRAFICO.TI_ENTITA,
	   DECODE(ANAGRAFICO.TI_ENTITA, 'F',ANAGRAFICO.CODICE_FISCALE,'D',Nvl(ANAGRAFICO.CODICE_FISCALE,ANAGRAFICO.PARTITA_IVA),ANAGRAFICO.PARTITA_IVA),
	   ANAGRAFICO.NOME,
	   ANAGRAFICO.COGNOME,
	   Anagrafico.via_fiscale,
	   Anagrafico.NUM_CIVICO_FISCALE,
	   Anagrafico.cap_comune_fiscale,
	   Anagrafico.pg_nazione_fiscale,
	   Anagrafico.pg_comune_fiscale,
	   Anagrafico.dt_nascita,
	   Anagrafico.pg_comune_nascita,
	   Anagrafico.ti_sesso,
	   c_fiscale.ds_comune comune_fiscale,
	   c_nascita.ds_comune comune_nascita,
	   nazione.ds_nazione ds_nazione
FROM
	TERZO,
	ANAGRAFICO,comune c_fiscale,comune c_nascita,nazione
WHERE
	 TERZO.CD_ANAG = ANAGRAFICO.CD_ANAG and
	 anagrafico.pg_comune_fiscale = c_fiscale.pg_comune(+) and
	 anagrafico.pg_nazione_fiscale = nazione.pg_nazione(+) and
	 anagrafico.pg_comune_nascita = c_nascita.pg_comune(+) and
	 terzo.dt_fine_rapporto is null;
