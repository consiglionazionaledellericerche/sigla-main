--------------------------------------------------------
--  DDL for View V_UNITA_ORGANIZZATIVA
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_UNITA_ORGANIZZATIVA" ("CD_UO", "ESERCIZIO", "ESERCIZIO_INIZIO", "CD_UNITA_ORGANIZZATIVA", "CD_PROPRIO_UNITA", "CD_TIPO_UNITA", "FL_CDS", "FL_UO_CDS", "FL_RUBRICA", "CD_UNITA_PADRE", "LIVELLO", "DS_UNITA_ORGANIZZATIVA", "PRC_COPERTURA_OBBLIG_2", "PRC_COPERTURA_OBBLIG_3", "DACR", "CD_AREA_RICERCA", "FL_PRESIDENTE_AREA", "CD_RESPONSABILE", "UTCR", "DUVA", "CD_RESPONSABILE_AMM", "UTUV", "PG_VER_REC", "ESERCIZIO_FINE", "CD_AREA_SCIENTIFICA", "ID_FUNZIONE_PUBBLICA", "CODICEAOOIPA") AS
  SELECT
--
-- Date: 25/03/2010
-- Version: 1.6
--
-- CD_UO  rappresenta il codice di UO da usare in ingresso sulla vista
-- Se si tratta di UO CDS non del SAC, vengono estratte tutte le UO appartenenti al CDS
-- (n righe con CD_UNITA_ORGANIZZATIVA = al codice di UOappartenente al cds di)
-- cui CD_UO e la UO CDS)
-- Se si tratta di UO rubrica del SAC, vengono estratte tutte le UO non rubriche appartenenti all'UO
-- rubrica (n righe con CD_UNITA_ORGANIZZATIVA = al codice di UO appartenente alla rubrica con codice CD_UO)
-- La vista verifica la validita della STO
--
-- History:
--
-- Date: 10/07/2001
-- Version: 1.0
-- Creazione
--
-- Date: 27/08/2001
-- Version: 1.1
-- Introduzione del fl_presidente_area in UNITA_ORGANIZZATIVA
--
-- Date: 13/11/2001
-- Version: 1.2
-- Eliminazione esercizio da STO
--
-- Date: 11/03/2002
-- Version: 1.3
-- Nel SAC per l'UO CDS vanno estratte solamente le UO non rubrica che afferiscono all'UO CDS
--
-- Date: 11/03/2002
-- Version: 1.4
-- Ottimizzazione performance
--
-- Date: 20/06/2002
-- Version: 1.5
-- Aggiunto CD_AREA_SCIENTIFICA
--
-- Date: 25/03/2010
-- Version: 1.6
-- Aggiunto ID_FUNZIONE_PUBBLICA
--
-- Body:
--
          uo_cds.cd_unita_organizzativa AS cd_uo,
          unita_organizzativa.esercizio, unita_organizzativa.esercizio_inizio,
          unita_organizzativa.cd_unita_organizzativa,
          unita_organizzativa.cd_proprio_unita,
          unita_organizzativa.cd_tipo_unita, unita_organizzativa.fl_cds,
          unita_organizzativa.fl_uo_cds, unita_organizzativa.fl_rubrica,
          unita_organizzativa.cd_unita_padre, unita_organizzativa.livello,
          unita_organizzativa.ds_unita_organizzativa,
          unita_organizzativa.prc_copertura_obblig_2,
          unita_organizzativa.prc_copertura_obblig_3,
          unita_organizzativa.dacr, unita_organizzativa.cd_area_ricerca,
          unita_organizzativa.fl_presidente_area,
          unita_organizzativa.cd_responsabile, unita_organizzativa.utcr,
          unita_organizzativa.duva, unita_organizzativa.cd_responsabile_amm,
          unita_organizzativa.utuv, unita_organizzativa.pg_ver_rec,
          unita_organizzativa.esercizio_fine,
          unita_organizzativa.cd_area_scientifica,
          unita_organizzativa.id_funzione_pubblica,
          unita_organizzativa.codiceAooIpa
     FROM v_unita_organizzativa_valida unita_organizzativa,
          v_unita_organizzativa_valida uo_cds
    WHERE uo_cds.fl_uo_cds = 'Y'
      AND uo_cds.cd_tipo_unita != 'SAC'
      AND unita_organizzativa.cd_unita_padre = uo_cds.cd_unita_padre
      AND uo_cds.esercizio = unita_organizzativa.esercizio
   UNION ALL
   SELECT
-- Se CD_UO non e il codice di una UO_CDS, viene estratta la sola UO non CDS se CDS !SAC altrimenti tutte le UO non rubrica per il SAC
-- (1 riga con CD_UNITA_ORGANIZZATIVA = al codice di UO specificata in CD_UO)
          unita_organizzativa.cd_unita_organizzativa AS cd_uo,
          unita_organizzativa.esercizio, unita_organizzativa.esercizio_inizio,
          unita_organizzativa.cd_unita_organizzativa,
          unita_organizzativa.cd_proprio_unita,
          unita_organizzativa.cd_tipo_unita, unita_organizzativa.fl_cds,
          unita_organizzativa.fl_uo_cds, unita_organizzativa.fl_rubrica,
          unita_organizzativa.cd_unita_padre, unita_organizzativa.livello,
          unita_organizzativa.ds_unita_organizzativa,
          unita_organizzativa.prc_copertura_obblig_2,
          unita_organizzativa.prc_copertura_obblig_3,
          unita_organizzativa.dacr, unita_organizzativa.cd_area_ricerca,
          unita_organizzativa.fl_presidente_area,
          unita_organizzativa.cd_responsabile, unita_organizzativa.utcr,
          unita_organizzativa.duva, unita_organizzativa.cd_responsabile_amm,
          unita_organizzativa.utuv, unita_organizzativa.pg_ver_rec,
          unita_organizzativa.esercizio_fine,
          unita_organizzativa.cd_area_scientifica,
          unita_organizzativa.id_funzione_pubblica,
          unita_organizzativa.codiceAooIpa
     FROM v_unita_organizzativa_valida unita_organizzativa
    WHERE unita_organizzativa.fl_uo_cds = 'N'
      AND cd_tipo_unita != 'SAC'               -- L'UO CDS al di fuori del SAC
   UNION ALL
   SELECT
-- Nel caso UO sia rubrica del SAC estrae tutte le UO sotto la rubrica e la rubrica stessa
          uo_rubrica.cd_unita_organizzativa AS cd_uo,
          unita_organizzativa.esercizio, unita_organizzativa.esercizio_inizio,
          unita_organizzativa.cd_unita_organizzativa,
          unita_organizzativa.cd_proprio_unita,
          unita_organizzativa.cd_tipo_unita, unita_organizzativa.fl_cds,
          unita_organizzativa.fl_uo_cds, unita_organizzativa.fl_rubrica,
          unita_organizzativa.cd_unita_padre, unita_organizzativa.livello,
          unita_organizzativa.ds_unita_organizzativa,
          unita_organizzativa.prc_copertura_obblig_2,
          unita_organizzativa.prc_copertura_obblig_3,
          unita_organizzativa.dacr, unita_organizzativa.cd_area_ricerca,
          unita_organizzativa.fl_presidente_area,
          unita_organizzativa.cd_responsabile, unita_organizzativa.utcr,
          unita_organizzativa.duva, unita_organizzativa.cd_responsabile_amm,
          unita_organizzativa.utuv, unita_organizzativa.pg_ver_rec,
          unita_organizzativa.esercizio_fine,
          unita_organizzativa.cd_area_scientifica,
          unita_organizzativa.id_funzione_pubblica,
          unita_organizzativa.codiceAooIpa
     FROM v_unita_organizzativa_valida unita_organizzativa,
          v_unita_organizzativa_valida uo_rubrica,
          v_cdr_valido cdr,
-- CDR di secondo livello afferente all'UO UNITA_ORGANIZZATIVA (RUBRICA o non)
          v_cdr_valido cdr_rubrica
                              -- CDR di primo livello afferente all'UO RUBRICA
    WHERE uo_rubrica.esercizio = unita_organizzativa.esercizio
      AND cdr.esercizio = unita_organizzativa.esercizio
      AND cdr_rubrica.esercizio = unita_organizzativa.esercizio
      AND uo_rubrica.fl_rubrica = 'Y'
      AND unita_organizzativa.cd_tipo_unita = 'SAC'
      AND uo_rubrica.cd_tipo_unita = 'SAC'
      AND unita_organizzativa.cd_unita_padre = uo_rubrica.cd_unita_padre
      AND cdr.cd_unita_organizzativa =
                                    unita_organizzativa.cd_unita_organizzativa
      AND cdr_rubrica.cd_unita_organizzativa =
                                             uo_rubrica.cd_unita_organizzativa
      AND (   (    unita_organizzativa.cd_unita_organizzativa !=
                                             uo_rubrica.cd_unita_organizzativa
               AND cdr.cd_cdr_afferenza = cdr_rubrica.cd_centro_responsabilita
              )
           OR (unita_organizzativa.cd_unita_organizzativa =
                                             uo_rubrica.cd_unita_organizzativa
              )
          )
   UNION ALL
   SELECT
-- Se CD_UO non e il codice di una UO_CDS, viene estratta la sola UO non CDS se CDS !SAC altrimenti tutte le UO non rubrica per il SAC
-- (1 riga con CD_UNITA_ORGANIZZATIVA = al codice di UO specificata in CD_UO)
          unita_organizzativa.cd_unita_organizzativa AS cd_uo,
          unita_organizzativa.esercizio, unita_organizzativa.esercizio_inizio,
          unita_organizzativa.cd_unita_organizzativa,
          unita_organizzativa.cd_proprio_unita,
          unita_organizzativa.cd_tipo_unita, unita_organizzativa.fl_cds,
          unita_organizzativa.fl_uo_cds, unita_organizzativa.fl_rubrica,
          unita_organizzativa.cd_unita_padre, unita_organizzativa.livello,
          unita_organizzativa.ds_unita_organizzativa,
          unita_organizzativa.prc_copertura_obblig_2,
          unita_organizzativa.prc_copertura_obblig_3,
          unita_organizzativa.dacr, unita_organizzativa.cd_area_ricerca,
          unita_organizzativa.fl_presidente_area,
          unita_organizzativa.cd_responsabile, unita_organizzativa.utcr,
          unita_organizzativa.duva, unita_organizzativa.cd_responsabile_amm,
          unita_organizzativa.utuv, unita_organizzativa.pg_ver_rec,
          unita_organizzativa.esercizio_fine,
          unita_organizzativa.cd_area_scientifica,
          unita_organizzativa.id_funzione_pubblica,
          unita_organizzativa.codiceAooIpa
     FROM v_unita_organizzativa_valida unita_organizzativa
    WHERE unita_organizzativa.fl_rubrica = 'N'
      AND cd_tipo_unita = 'SAC';

   COMMENT ON TABLE "V_UNITA_ORGANIZZATIVA"  IS 'CD_UO  rappresenta il codice di UO da usare in ingresso sulla vista.
Se si tratta di UO CDS non del SAC, vengono estratte tutte le UO appartenenti al CDS
(n righe con CD_UNITA_ORGANIZZATIVA = al codice di UO appartenente al cds di)
cui CD_UO e la UO CDS)
Se si tratta di UO rubrica del SAC, vengono estratte tutte le UO non rubriche appartenenti all''UO
rubrica (n righe con CD_UNITA_ORGANIZZATIVA = al codice di UO appartenente alla rubrica con codice CD_UO)
La vista verifica la validita della STO';
