--------------------------------------------------------
--  DDL for View V_UNITA_ORGANIZZATIVA_VALIDA
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_UNITA_ORGANIZZATIVA_VALIDA" ("ESERCIZIO", "ESERCIZIO_INIZIO", "CD_UNITA_ORGANIZZATIVA", "CD_PROPRIO_UNITA", "CD_TIPO_UNITA", "FL_CDS", "FL_UO_CDS", "FL_RUBRICA", "CD_UNITA_PADRE", "LIVELLO", "DS_UNITA_ORGANIZZATIVA", "PRC_COPERTURA_OBBLIG_2", "PRC_COPERTURA_OBBLIG_3", "CD_AREA_RICERCA", "FL_PRESIDENTE_AREA", "CD_RESPONSABILE", "CD_RESPONSABILE_AMM", "ESERCIZIO_FINE", "CD_AREA_SCIENTIFICA", "ID_FUNZIONE_PUBBLICA", "CODICEAOOIPA", "DACR", "UTCR", "DUVA", "UTUV", "PG_VER_REC") AS
  SELECT
--
-- Date: 25/03/2010
-- Version: 1.5
--
-- Estrae le UO valide nell'esercizio esercizio corrente (campo esercizio)
--
-- History:
-- Date: 07/11/2001
-- Version: 1.0
-- Creazione
--
-- Date: 14/11/2001
-- Version: 1.2
-- eliminato controllo ESERCIZIO_FINE IS NULL
--
-- Date: 20/06/2002
-- Version: 1.3
-- Aggiunto CD_AREA_SCIENTIFICA
--
-- Date: 25/03/2010
-- Version: 1.5
-- Aggiunto ID_FUNZIONE_PUBBLICA
--
-- Body:
--
          b.esercizio, a.esercizio_inizio, a.cd_unita_organizzativa,
          a.cd_proprio_unita, a.cd_tipo_unita, a.fl_cds, a.fl_uo_cds,
          a.fl_rubrica, a.cd_unita_padre, a.livello, a.ds_unita_organizzativa,
          a.prc_copertura_obblig_2, a.prc_copertura_obblig_3,
          a.cd_area_ricerca, a.fl_presidente_area, a.cd_responsabile,
          a.cd_responsabile_amm, a.esercizio_fine, a.cd_area_scientifica,
          a.id_funzione_pubblica, a.codiceAooIpa,
          a.dacr, a.utcr, a.duva, a.utuv, a.pg_ver_rec
     FROM unita_organizzativa a, v_esercizi b
    WHERE b.esercizio >= a.esercizio_inizio
          AND b.esercizio <= a.esercizio_fine;
