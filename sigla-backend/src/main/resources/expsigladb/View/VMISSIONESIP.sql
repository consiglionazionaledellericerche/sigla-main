--------------------------------------------------------
--  DDL for View VMISSIONESIP
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VMISSIONESIP" ("ESERCIZIO", "CD_CDS", "CD_UNITA_ORGANIZZATIVA", "CD_TERZO", "DENOMINAZIONE", "PARTITA_IVA", "CODICE_FISCALE", "TIPO", "MATRICOLA", "DS_MISSIONE", "IM_TOTALE_MISSIONE", "PG_MISSIONE", "DT_INIZIO_MISSIONE", "DT_FINE_MISSIONE", "CD_ELEMENTO_VOCE", "CD_CENTRO_RESPONSABILITA", "GAE", "DT_PAGAMENTO") AS 
  SELECT v.esercizio, v.cd_cds, v.cd_unita_organizzativa, v.cd_terzo,
          NVL (v.ragione_sociale, v.cognome || ' ' || v.nome) denominazione,
          v.partita_iva, v.codice_fiscale, ti_anagrafico,
          (SELECT DISTINCT matricola_dipendente
                      FROM rapporto, terzo t
                     WHERE cd_tipo_rapporto = 'DIP'
                       AND rapporto.cd_anag = t.cd_anag
                       and dt_inizio_missione>= rapporto.DT_INI_VALIDITA
                       and dt_fine_missione  <=rapporto.dt_fin_validita
                       AND v.cd_terzo = t.cd_terzo) matricola_dipendente,
          v.ds_missione, v.im_totale_missione, v.pg_missione,
          dt_inizio_missione, dt_fine_missione,
          obb.cd_elemento_voce cd_elemento_voce,
          obb_scad_voce.cd_centro_responsabilita cd_centro_responsabilita,
          obb_scad_voce.cd_linea_attivita gae, m.dt_pagamento
     FROM missione v,
          obbligazione obb,
          obbligazione_scadenzario obb_scad,
          obbligazione_scad_voce obb_scad_voce,
          mandato m,
          mandato_riga m_riga
    WHERE m_riga.esercizio = m.esercizio
      AND m_riga.cd_cds = m.cd_cds
      AND m_riga.pg_mandato = m.pg_mandato
      AND m_riga.esercizio_obbligazione = v.esercizio_obbligazione
      AND m_riga.esercizio_ori_obbligazione = v.esercizio_ori_obbligazione
      AND m_riga.cd_cds = v.cd_cds_obbligazione
      AND m_riga.pg_obbligazione = v.pg_obbligazione
      AND m_riga.pg_obbligazione_scadenzario = v.pg_obbligazione_scadenzario
      AND m_riga.esercizio_doc_amm = v.esercizio
      AND m_riga.cd_cds_doc_amm = v.cd_cds
      AND m_riga.cd_uo_doc_amm = v.cd_unita_organizzativa
      AND m_riga.pg_doc_amm = v.pg_missione
      AND v.esercizio_obbligazione = obb_scad.esercizio
      AND v.cd_cds_obbligazione = obb_scad.cd_cds
      AND v.pg_obbligazione = obb_scad.pg_obbligazione
      AND v.pg_obbligazione_scadenzario = obb_scad.pg_obbligazione_scadenzario
      AND v.esercizio_ori_obbligazione = obb_scad.esercizio_originale
      AND obb_scad.esercizio = obb.esercizio
      AND obb_scad.cd_cds = obb.cd_cds
      AND obb_scad.pg_obbligazione = obb.pg_obbligazione
      AND obb_scad.esercizio_originale = obb.esercizio_originale
      AND obb_scad.esercizio = obb_scad_voce.esercizio
      AND obb_scad.cd_cds = obb_scad_voce.cd_cds
      AND obb_scad.pg_obbligazione = obb_scad_voce.pg_obbligazione
      AND obb_scad.pg_obbligazione_scadenzario =
                                     obb_scad_voce.pg_obbligazione_scadenzario
      AND obb_scad.esercizio_originale = obb_scad_voce.esercizio_originale
      AND v.stato_cofi = 'P'
      AND v.ti_provvisorio_definitivo = 'D'
      AND m.dt_pagamento IS NOT NULL ;
