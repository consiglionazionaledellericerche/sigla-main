--------------------------------------------------------
--  DDL for View V_DOC_AMM_ANAG_MANREV
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_DOC_AMM_ANAG_MANREV" ("TIPOLOGIA", "DT_REGISTRAZIONE", "CD_CDS", "CD_UNITA_ORGANIZZATIVA", "ESERCIZIO", "CD_TIPO_DOCUMENTO_AMM", "PG_DOCUMENTO_AMM", "DT_EMISSIONE", "CD_CDS_ORIGINE", "CD_UO_ORIGINE", "TI_FATTURA", "STATO_COFI", "STATO_PAGAMENTO_FONDO_ECO", "DT_PAGAMENTO_FONDO_ECO", "DT_FATTURA_FORNITORE", "NR_FATTURA_FORNITORE", "CD_ANAG", "CD_TERZO", "CD_TERZO_CESSIONARIO", "COGNOME", "NOME", "RAGIONE_SOCIALE", "CD_MODALITA_PAG", "IM_TOTALE_DOC_AMM", "CODICE_FISCALE", "PARTITA_IVA", "DS_DOCUMENTO", "ESERCIZIO_MANREV", "CDS_MANREV", "NR_MANREV") AS 
  select 'Passivo' ,dt_registrazione,
                                                      cd_cds,
                                                      cd_unita_organizzativa,
                                                      esercizio,
                                                      cd_tipo_documento_amm,
                                                      pg_documento_amm,
                                                      null,--dt_emissione
                                                      cd_cds_origine,
                                                      cd_uo_origine,
                                                      ti_fattura,
                                                      stato_cofi,
                                                      stato_pagamento_fondo_eco,
                                                      dt_pagamento_fondo_eco,
                                                      dt_fattura_fornitore,
                                                      nr_fattura_fornitore,
                                                      cd_anag,
                                                      cd_terzo,
                                                      cd_terzo_cessionario,
                                                      cognome,
                                                      nome,
                                                      ragione_sociale,
                                                      cd_modalita_pag,
                                                      im_totale_doc_amm,
                                                      codice_fiscale,
                                                      partita_iva,
                                                      ds_documento,
                                                      esercizio_man,
                                                      cds_mandato,
                                                      nr_mandato
                                                      from v_doc_passivo_anag_mand
                                                  union
																											select 'Attivo', dt_registrazione,
                                                      cd_cds,
                                                      cd_unita_organizzativa,
                                                      esercizio,
                                                      cd_tipo_documento_amm,
                                                      pg_documento_amm,
                                                      dt_emissione,
                                                      cd_cds_origine,
                                                      cd_uo_origine,
                                                      ti_fattura,
                                                      stato_cofi,
                                                      null,--stato_pagamento_fondo_eco
                                                      null,--dt_pagamento_fondo_eco
                                                      null,--dt_fattura_fornitore
                                                      null,--nr_fattura_fornitore
                                                      cd_anag,
                                                      cd_terzo,
                                                      null,--cd_terzo_cessionario
                                                      cognome,
                                                      nome,
                                                      ragione_sociale,
                                                      cd_modalita_pag,
                                                      im_totale_doc_amm,
                                                      codice_fiscale,
                                                      partita_iva,
                                                      ds_documento,
                                                      esercizio_rev,
                                                      cds_reversale,
                                                      nr_reversale
                                                      from v_doc_attivo_anag_rev
                                                      order by dt_registrazione;
