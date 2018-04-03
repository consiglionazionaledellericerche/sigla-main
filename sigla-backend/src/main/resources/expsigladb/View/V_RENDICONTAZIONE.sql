--------------------------------------------------------
--  DDL for View V_RENDICONTAZIONE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_RENDICONTAZIONE" ("TIPODOCUMENTO", "ESERCIZIO", "CD_CDS", "CD_UNITA_ORGANIZZATIVA", "CD_TERZO", "DENOMINAZIONE", "PARTITA_IVA", "CODICE_FISCALE", "NR_FATTURA_FORNITORE", "DT_FATTURA_FORNITORE", "TIPORAPPORTO", "MATRICOLA", "DS_DOCUMENTO", "IM_TOTALE", "PG_DOCUMENTO", "DT_INIZIO_COMP", "DT_FINE_COMP", "CD_ELEMENTO_VOCE", "DS_ELEMENTO_VOCE", "CDR", "GAE", "DS_GAE", "DT_PAGAMENTO", "NR_MANDATO") AS 
  SELECT 'Fattura Passiva' tipoDocumento,v.esercizio, v.cd_cds, v.cd_unita_organizzativa, v.cd_terzo,
          NVL (v.ragione_sociale, v.cognome || ' ' || v.nome) denominazione,
          v.partita_iva, v.codice_fiscale, v.nr_fattura_fornitore,
          v.dt_fattura_fornitore, NULL tiporapporto, NULL matricola,
          nvl(v.ds_fattura_passiva,riga.DS_RIGA_FATTURA) ds_documento,
          DECODE (v.ti_fattura,
                  'C', -v.im_totale_fattura,
                  v.im_totale_fattura
                 ) im_totale,
          v.pg_fattura_passiva pg_documento,
          riga.dt_da_competenza_coge dt_inizio_comp,
          riga.dt_a_competenza_coge dt_fine_comp,
          obb.cd_elemento_voce cd_elemento_voce,elemento_voce.ds_elemento_voce,
          obb_scad_voce.cd_centro_responsabilita cd_centro_responsabilita,
          obb_scad_voce.cd_linea_attivita gae, linea_attivita.ds_linea_attivita,m.dt_pagamento,m.pg_mandato nr_mandato
     FROM fattura_passiva v,
          anagrafico a,
          comune c,
          terzo t,
          fattura_passiva_riga riga,
          obbligazione obb,
          obbligazione_scadenzario obb_scad,
          obbligazione_scad_voce obb_scad_voce,
          mandato m,
          mandato_riga m_riga,
          elemento_voce,
          linea_attivita
    WHERE m_riga.cd_cds = m.cd_cds
      AND m_riga.esercizio = m.esercizio
      AND m_riga.pg_mandato = m.pg_mandato
      AND m_riga.cd_cds = riga.cd_cds_obbligazione
      AND m_riga.esercizio_obbligazione = riga.esercizio_obbligazione
      AND m_riga.esercizio_ori_obbligazione = riga.esercizio_ori_obbligazione
      AND m_riga.pg_obbligazione = riga.pg_obbligazione
      AND m_riga.pg_obbligazione_scadenzario =
                                              riga.pg_obbligazione_scadenzario
      AND m_riga.esercizio_doc_amm = riga.esercizio
      AND m_riga.cd_cds_doc_amm = riga.cd_cds
      AND m_riga.cd_uo_doc_amm = riga.cd_unita_organizzativa
      AND m_riga.pg_doc_amm = riga.pg_fattura_passiva
      AND riga.esercizio = v.esercizio
      AND riga.cd_cds = v.cd_cds
      AND riga.cd_unita_organizzativa = v.cd_unita_organizzativa
      AND riga.pg_fattura_passiva = v.pg_fattura_passiva
      AND riga.esercizio_obbligazione = obb_scad.esercizio
      AND riga.cd_cds_obbligazione = obb_scad.cd_cds
      AND riga.pg_obbligazione = obb_scad.pg_obbligazione
      AND riga.pg_obbligazione_scadenzario =
                                          obb_scad.pg_obbligazione_scadenzario
      AND riga.esercizio_ori_obbligazione = obb_scad.esercizio_originale
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
      AND v.cd_terzo = t.cd_terzo
      AND t.cd_anag = a.cd_anag
      AND a.pg_comune_fiscale = c.pg_comune
      AND riga.stato_cofi = 'P'
      AND m.dt_pagamento IS NOT NULL
      and elemento_voce.esercizio = obb.esercizio
      AND elemento_voce.ti_appartenenza = obb.ti_appartenenza
      AND elemento_voce.ti_gestione = obb.ti_gestione
      AND elemento_voce.cd_elemento_voce = obb.cd_elemento_voce
      and obb_scad_voce.cd_centro_responsabilita = linea_attivita.cd_centro_responsabilita
      AND obb_scad_voce.cd_linea_attivita = linea_attivita.cd_linea_attivita
      and v.STATO_PAGAMENTO_FONDO_ECO='N'
   UNION
   SELECT 'Fattura Passiva' tipoDocumento,v.esercizio, v.cd_cds, v.cd_unita_organizzativa, v.cd_terzo,
          NVL (v.ragione_sociale, v.cognome || ' ' || v.nome) denominazione,
          v.partita_iva, v.codice_fiscale, v.nr_fattura_fornitore,
          v.dt_fattura_fornitore, NULL tiporapporto, NULL matricola,
          nvl(v.ds_fattura_passiva,riga.DS_RIGA_FATTURA) ds_documento,
          DECODE (v.ti_fattura,
                  'C', -v.im_totale_fattura,
                  v.im_totale_fattura
                 ) im_totale,
          v.pg_fattura_passiva pg_documento,
          riga.dt_da_competenza_coge dt_inizio_comp,
          riga.dt_a_competenza_coge dt_fine_comp,
          acc.cd_elemento_voce cd_elemento_voce,elemento_voce.ds_elemento_voce,
          acc_scad_voce.cd_centro_responsabilita cd_centro_responsabilita,
          acc_scad_voce.cd_linea_attivita gae, linea_attivita.ds_linea_attivita, r.dt_incasso,r.pg_reversale nr_mandato
     FROM fattura_passiva v,
          anagrafico a,
          comune c,
          terzo t,
          fattura_passiva_riga riga,
          accertamento acc,
          accertamento_scadenzario acc_scad,
          accertamento_scad_voce acc_scad_voce,
          reversale r,
          reversale_riga r_riga,
          elemento_voce,
          linea_attivita
    WHERE r_riga.cd_cds = r.cd_cds
      AND r_riga.esercizio = r.esercizio
      AND r_riga.pg_reversale = r.pg_reversale
      AND r_riga.cd_cds = riga.cd_cds_accertamento
      AND r_riga.esercizio_accertamento = riga.esercizio_accertamento
      AND r_riga.pg_accertamento = riga.pg_accertamento
      AND r_riga.pg_accertamento_scadenzario =
                                              riga.pg_accertamento_scadenzario
      AND r_riga.esercizio_ori_accertamento = riga.esercizio_ori_accertamento
      AND r_riga.esercizio_doc_amm = riga.esercizio
      AND r_riga.cd_cds_doc_amm = riga.cd_cds
      AND r_riga.cd_uo_doc_amm = riga.cd_unita_organizzativa
      AND r_riga.pg_doc_amm = riga.pg_fattura_passiva
      AND riga.esercizio = v.esercizio
      AND riga.cd_cds = v.cd_cds
      AND riga.cd_unita_organizzativa = v.cd_unita_organizzativa
      AND riga.pg_fattura_passiva = v.pg_fattura_passiva
      AND riga.esercizio_accertamento = acc_scad.esercizio
      AND riga.cd_cds_accertamento = acc_scad.cd_cds
      AND riga.pg_accertamento = acc_scad.pg_accertamento
      AND riga.pg_accertamento_scadenzario =
                                          acc_scad.pg_accertamento_scadenzario
      AND riga.esercizio_ori_accertamento = acc_scad.esercizio_originale
      AND acc_scad.esercizio = acc.esercizio
      AND acc_scad.cd_cds = acc.cd_cds
      AND acc_scad.pg_accertamento = acc.pg_accertamento
      AND acc_scad.esercizio_originale = acc.esercizio_originale
      AND acc_scad.esercizio = acc_scad_voce.esercizio
      AND acc_scad.cd_cds = acc_scad_voce.cd_cds
      AND acc_scad.pg_accertamento = acc_scad_voce.pg_accertamento
      AND acc_scad.pg_accertamento_scadenzario =
                                     acc_scad_voce.pg_accertamento_scadenzario
      AND acc_scad.esercizio_originale = acc_scad_voce.esercizio_originale
      AND v.cd_terzo = t.cd_terzo
      AND t.cd_anag = a.cd_anag
      AND a.pg_comune_fiscale = c.pg_comune
      AND riga.stato_cofi = 'P'
      AND r.dt_incasso IS NOT NULL
      and elemento_voce.esercizio = acc.esercizio
      AND elemento_voce.ti_appartenenza = acc.ti_appartenenza
      AND elemento_voce.ti_gestione = acc.ti_gestione
      AND elemento_voce.cd_elemento_voce = acc.cd_elemento_voce
      and acc_scad_voce.cd_centro_responsabilita = linea_attivita.cd_centro_responsabilita
      AND acc_scad_voce.cd_linea_attivita = linea_attivita.cd_linea_attivita
      and v.STATO_PAGAMENTO_FONDO_ECO='N'
   UNION
   SELECT 'Missione' tipoDocumento,v.esercizio, v.cd_cds, v.cd_unita_organizzativa, v.cd_terzo,
          NVL (v.ragione_sociale, v.cognome || ' ' || v.nome) denominazione,
          v.partita_iva, v.codice_fiscale, null nr_fattura_fornitore,
                                               null dt_fattura_fornitore, decode(ti_anagrafico,'A','Altro','Dipendente') tiporapporto,
          (SELECT DISTINCT matricola_dipendente
                      FROM rapporto, terzo t
                     WHERE cd_tipo_rapporto = 'DIP'
                       AND rapporto.cd_anag = t.cd_anag
                       and matricola_dipendente is not null
                       AND v.cd_terzo = t.cd_terzo) matricola,
          v.ds_missione ds_documento, v.im_totale_missione im_totale,
          v.pg_missione pg_documento, dt_inizio_missione dt_inizio_comp,
          dt_fine_missione dt_fine_comp,
          obb.cd_elemento_voce cd_elemento_voce,elemento_voce.ds_elemento_voce,
          obb_scad_voce.cd_centro_responsabilita cd_centro_responsabilita,
          obb_scad_voce.cd_linea_attivita gae, linea_attivita.ds_linea_attivita, m.dt_pagamento,m.pg_mandato nr_mandato
     FROM missione v,
          missione_riga mr,
          obbligazione obb,
          obbligazione_scadenzario obb_scad,
          obbligazione_scad_voce obb_scad_voce,
          mandato m,
          mandato_riga m_riga,
          elemento_voce,
          linea_attivita
    WHERE m_riga.esercizio = m.esercizio
      AND m_riga.cd_cds = m.cd_cds
      AND m_riga.pg_mandato = m.pg_mandato
      AND m_riga.esercizio_obbligazione = mr.esercizio_obbligazione
      AND m_riga.esercizio_ori_obbligazione = mr.esercizio_ori_obbligazione
      AND m_riga.cd_cds = mr.cd_cds_obbligazione
      AND m_riga.pg_obbligazione = mr.pg_obbligazione
      AND m_riga.pg_obbligazione_scadenzario = mr.pg_obbligazione_scadenzario
      AND v.cd_cds = mr.cd_cds
      AND v.cd_unita_organizzativa = mr.cd_unita_organizzativa
      AND v.esercizio = mr.esercizio
      AND v.pg_missione = mr.pg_missione
      AND m_riga.esercizio_doc_amm = v.esercizio
      AND m_riga.cd_cds_doc_amm = v.cd_cds
      AND m_riga.cd_uo_doc_amm = v.cd_unita_organizzativa
      AND m_riga.pg_doc_amm = v.pg_missione
      AND mr.esercizio_obbligazione = obb_scad.esercizio
      AND mr.cd_cds_obbligazione = obb_scad.cd_cds
      AND mr.pg_obbligazione = obb_scad.pg_obbligazione
      AND mr.pg_obbligazione_scadenzario = obb_scad.pg_obbligazione_scadenzario
      AND mr.esercizio_ori_obbligazione = obb_scad.esercizio_originale
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
      AND m.dt_pagamento IS NOT NULL
      and elemento_voce.esercizio = obb.esercizio
      AND elemento_voce.ti_appartenenza = obb.ti_appartenenza
      AND elemento_voce.ti_gestione = obb.ti_gestione
      AND elemento_voce.cd_elemento_voce = obb.cd_elemento_voce
      and obb_scad_voce.cd_centro_responsabilita = linea_attivita.cd_centro_responsabilita
      AND obb_scad_voce.cd_linea_attivita = linea_attivita.cd_linea_attivita
      and v.STATO_PAGAMENTO_FONDO_ECO='N'
  union
  SELECT 'Compenso' tipoDocumento ,v.esercizio, v.cd_cds, v.cd_unita_organizzativa, v.cd_terzo,
          NVL (v.ragione_sociale, v.cognome || ' ' || v.nome) denominazione,
          v.partita_iva, v.codice_fiscale, null nr_fattura_fornitore,
                                               null dt_fattura_fornitore, decode(ti_anagrafico,'A','Altro','Dipendente') tiporapporto,
          (SELECT DISTINCT matricola_dipendente
                      FROM rapporto, terzo t
                     WHERE cd_tipo_rapporto = 'DIP'
                       AND rapporto.cd_anag = t.cd_anag
                       and matricola_dipendente is not null
                       AND v.cd_terzo = t.cd_terzo) matricola,
          v.ds_compenso ds_documento, v.im_totale_compenso im_totale,
          v.pg_compenso pg_documento, dt_da_competenza_coge dt_inizio_comp,
          dt_a_competenza_coge dt_fine_comp,
          obb.cd_elemento_voce cd_elemento_voce,elemento_voce.ds_elemento_voce,
          obb_scad_voce.cd_centro_responsabilita cd_centro_responsabilita,
          obb_scad_voce.cd_linea_attivita gae, linea_attivita.ds_linea_attivita, m.dt_pagamento,m.pg_mandato nr_mandato
     FROM compenso v,
          compenso_riga cr,
          obbligazione obb,
          obbligazione_scadenzario obb_scad,
          obbligazione_scad_voce obb_scad_voce,
          mandato m,
          mandato_riga m_riga,
          elemento_voce,
          linea_attivita
    WHERE m_riga.esercizio = m.esercizio
      AND m_riga.cd_cds = m.cd_cds
      AND m_riga.pg_mandato = m.pg_mandato
      AND m_riga.esercizio_obbligazione = cr.esercizio_obbligazione
      AND m_riga.esercizio_ori_obbligazione = cr.esercizio_ori_obbligazione
      AND m_riga.cd_cds = cr.cd_cds_obbligazione
      AND m_riga.pg_obbligazione = cr.pg_obbligazione
      AND m_riga.pg_obbligazione_scadenzario = cr.pg_obbligazione_scadenzario
      AND v.cd_cds = cr.cd_cds
      AND v.cd_unita_organizzativa = cr.cd_unita_organizzativa
      AND v.esercizio = cr.esercizio
      AND v.pg_compenso = cr.pg_compenso
      AND m_riga.esercizio_doc_amm = v.esercizio
      AND m_riga.cd_cds_doc_amm = v.cd_cds
      AND m_riga.cd_uo_doc_amm = v.cd_unita_organizzativa
      AND m_riga.pg_doc_amm = v.pg_compenso
      AND cr.esercizio_obbligazione = obb_scad.esercizio
      AND cr.cd_cds_obbligazione = obb_scad.cd_cds
      AND cr.pg_obbligazione = obb_scad.pg_obbligazione
      AND cr.pg_obbligazione_scadenzario = obb_scad.pg_obbligazione_scadenzario
      AND cr.esercizio_ori_obbligazione = obb_scad.esercizio_originale
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
      AND v.pg_missione IS NULL
      AND fl_generata_fattura = 'N'
      AND fl_compenso_stipendi = 'N'
      AND fl_compenso_conguaglio = 'N'
      AND im_totale_compenso > 0
      AND m.dt_pagamento IS NOT NULL
      and elemento_voce.esercizio = obb.esercizio
      AND elemento_voce.ti_appartenenza = obb.ti_appartenenza
      AND elemento_voce.ti_gestione = obb.ti_gestione
      AND elemento_voce.cd_elemento_voce = obb.cd_elemento_voce
      and obb_scad_voce.cd_centro_responsabilita = linea_attivita.cd_centro_responsabilita
      AND obb_scad_voce.cd_linea_attivita = linea_attivita.cd_linea_attivita
      and v.STATO_PAGAMENTO_FONDO_ECO='N'
   union
  SELECT 'Documento generico' tipoDocumento,v.esercizio, v.cd_cds, v.cd_unita_organizzativa, riga.cd_terzo,
          NVL (riga.ragione_sociale, riga.cognome || ' ' || riga.nome) denominazione,
          riga.partita_iva, riga.codice_fiscale, null nr_fattura_fornitore,
          null dt_fattura_fornitore, NULL tiporapporto, NULL matricola,
          riga.ds_riga ds_documento,
          v.im_totale im_totale,
          v.PG_DOCUMENTO_GENERICO pg_documento,
          riga.dt_da_competenza_coge dt_inizio_comp,
          riga.dt_a_competenza_coge dt_fine_comp,
          obb.cd_elemento_voce cd_elemento_voce,elemento_voce.ds_elemento_voce,
          obb_scad_voce.cd_centro_responsabilita cd_centro_responsabilita,
          obb_scad_voce.cd_linea_attivita gae, linea_attivita.ds_linea_attivita, m.dt_pagamento,m.pg_mandato nr_mandato
     FROM anagrafico a,
          comune c,
          terzo t,
          documento_generico v, documento_generico_riga riga,
          obbligazione obb,
          obbligazione_scadenzario obb_scad,
          obbligazione_scad_voce obb_scad_voce,
          mandato m,
          mandato_riga m_riga,
          elemento_voce,
          linea_attivita
    WHERE m_riga.cd_cds = m.cd_cds
      AND m_riga.esercizio = m.esercizio
      AND m_riga.pg_mandato = m.pg_mandato
      AND m_riga.cd_cds = riga.cd_cds_obbligazione
      AND m_riga.esercizio_obbligazione = riga.esercizio_obbligazione
      AND m_riga.esercizio_ori_obbligazione = riga.esercizio_ori_obbligazione
      AND m_riga.pg_obbligazione = riga.pg_obbligazione
      AND m_riga.pg_obbligazione_scadenzario =
                                              riga.pg_obbligazione_scadenzario
      AND m_riga.esercizio_doc_amm = riga.esercizio
      AND m_riga.cd_cds_doc_amm = riga.cd_cds
      AND m_riga.cd_uo_doc_amm = riga.cd_unita_organizzativa
      AND m_riga.pg_doc_amm = riga.PG_DOCUMENTO_GENERICO
      and v.cd_cds = riga.cd_cds
      AND v.cd_unita_organizzativa =  riga.cd_unita_organizzativa
      AND v.esercizio = riga.esercizio
      AND v.cd_tipo_documento_amm = riga.cd_tipo_documento_amm
      AND v.pg_documento_generico = riga.pg_documento_generico
      AND v.cd_tipo_documento_amm ='GENERICO_S'
      AND riga.esercizio_obbligazione = obb_scad.esercizio
      AND riga.cd_cds_obbligazione = obb_scad.cd_cds
      AND riga.pg_obbligazione = obb_scad.pg_obbligazione
      AND riga.pg_obbligazione_scadenzario =
                                          obb_scad.pg_obbligazione_scadenzario
      AND riga.esercizio_ori_obbligazione = obb_scad.esercizio_originale
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
      AND riga.cd_terzo = t.cd_terzo
      AND t.cd_anag = a.cd_anag
      AND a.pg_comune_fiscale = c.pg_comune
      AND riga.stato_cofi = 'P'
      AND m.dt_pagamento IS NOT NULL
      and elemento_voce.esercizio = obb.esercizio
      AND elemento_voce.ti_appartenenza = obb.ti_appartenenza
      AND elemento_voce.ti_gestione = obb.ti_gestione
      AND elemento_voce.cd_elemento_voce = obb.cd_elemento_voce
      and obb_scad_voce.cd_centro_responsabilita = linea_attivita.cd_centro_responsabilita
      AND obb_scad_voce.cd_linea_attivita = linea_attivita.cd_linea_attivita
      and v.STATO_PAGAMENTO_FONDO_ECO='N';
