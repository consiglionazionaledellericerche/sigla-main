--------------------------------------------------------
--  DDL for View V_TERZO_PER_COMPENSO
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_TERZO_PER_COMPENSO" ("CD_TIPO_RAPPORTO", "DS_TIPO_RAPPORTO", "TI_DIPENDENTE_ALTRO", "DT_INI_VALIDITA", "DT_FIN_VALIDITA", "CD_ANAG", "NOME", "COGNOME", "RAGIONE_SOCIALE", "CODICE_FISCALE", "PARTITA_IVA", "PG_COMUNE_FISCALE", "DS_COMUNE_FISCALE", "CD_PROVINCIA_FISCALE", "DS_PROVINCIA_FISCALE", "CD_REGIONE_FISCALE", "DS_REGIONE_FISCALE", "CD_TERZO", "DT_FINE_VALIDITA_TERZO", "CD_TERZO_PRECEDENTE") AS 
  SELECT
--==================================================================================================
--
-- Date: 03/02/2004
-- Version: 1.6
--
-- Vista di estrazione dei terzi, con il riferimento anagrafico, utilizabili in compensi e missioni
-- con i dati del rapporto
--
-- History:
--
-- Date: 03/05/2002
-- Version: 1.0
--
-- Creazione vista
--
-- Date: 18/07/2002
-- Version: 1.1
--
-- Aggiunta la data di fine validità del terzo
--
-- Date: 28/08/2002
-- Version: 1.2
--
-- Messa in outer join PROVINCIA E REGIONE
--
-- Date: 10/09/2002
-- Version: 1.3
--
-- Inserita gestione per compensi a soggetti esteri. Se l'anagrafico ha comune fiscale in Italia
-- allora recupero regione e provincia dall'indirizzo del domicilio fiscale presente in anagrafico.
-- Se l'anagrafico ha comune fiscale all'estero allora recupero regione e provincia dall'indirizzo
-- sede presente in terzo
--
-- Date: 13/09/2002
-- Version: 1.4
--
-- Se il codice regione o provincia sono nulli allora ritorno *
--
-- Date: 10/03/2003
-- Version: 1.5
--
-- Aggiunto il recupero dell'attributo TERZO.cd_precedente per ricerche
--
-- Date: 03/02/2004
-- Version: 1.6
--
-- Richiesta CINECA n. 642. Inserita gestione per compensi a soggetti esteri. Se l'anagrafico ha comune
-- all'estero allora recupero anche il codice del comune dall'indirizzo sede presente in terzo.
--
-- Body:
--
--==================================================================================================
       A.cd_tipo_rapporto,
       A.ds_tipo_rapporto,
       A.ti_dipendente_altro,
       B.dt_ini_validita,
       B.dt_fin_validita,
       C.cd_anag,
       C.nome,
       C.cognome,
       C.ragione_sociale,
       C.codice_fiscale,
       C.partita_iva,
       C.pg_comune_fiscale,
       E.ds_comune,
       NVL(F.cd_provincia,'*'),
       F.ds_provincia,
       NVL(G.cd_regione,'*'),
       G.ds_regione,
       D.cd_terzo,
       D.dt_fine_rapporto,
       D.cd_precedente
FROM   TIPO_RAPPORTO A,
       RAPPORTO B,
       ANAGRAFICO C,
       TERZO D,
       COMUNE E,
       PROVINCIA F,
       REGIONE G
WHERE  B.cd_tipo_rapporto = A.cd_tipo_rapporto AND
       C.cd_anag = B.cd_anag AND
       D.cd_anag = C.cd_anag AND
       E.pg_comune = C.pg_comune_fiscale AND
       E.ti_italiano_estero = 'I' AND
       F.cd_provincia (+) = E.cd_provincia AND
       G.cd_regione (+) = F.cd_regione
UNION ALL
SELECT A.cd_tipo_rapporto,
       A.ds_tipo_rapporto,
       A.ti_dipendente_altro,
       B.dt_ini_validita,
       B.dt_fin_validita,
       C.cd_anag,
       C.nome,
       C.cognome,
       C.ragione_sociale,
       C.codice_fiscale,
       C.partita_iva,
       D.pg_comune_sede,
       E1.ds_comune,
       NVL(F.cd_provincia,'*'),
       F.ds_provincia,
       NVL(G.cd_regione,'*'),
       G.ds_regione,
       D.cd_terzo,
       D.dt_fine_rapporto,
       D.cd_precedente
FROM   TIPO_RAPPORTO A,
       RAPPORTO B,
       ANAGRAFICO C,
       TERZO D,
       COMUNE E,
       COMUNE E1,
       PROVINCIA F,
       REGIONE G
WHERE  B.cd_tipo_rapporto = A.cd_tipo_rapporto AND
       C.cd_anag = B.cd_anag AND
       D.cd_anag = C.cd_anag AND
       E.pg_comune = C.pg_comune_fiscale AND
       E.ti_italiano_estero = 'E' AND
       E1.pg_comune = D.pg_comune_sede AND
       F.cd_provincia (+) = E1.cd_provincia AND
       G.cd_regione (+) = F.cd_regione
;
