--------------------------------------------------------
--  DDL for View V_CONS_ACC_GAE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_CONS_ACC_GAE" ("CDS", "UO", "CD_CENTRO_RESPONSABILITA", "CD_LINEA_ATTIVITA", "DS_LINEA_ATTIVITA", "CD_ELEMENTO_VOCE", "DT_REGISTRAZIONE", "ESERCIZIO_ORIGINALE", "PG_ACCERTAMENTO", "ESERCIZIO", "PG_ACCERTAMENTO_SCADENZARIO", "DS_SCADENZA", "IM_SCADENZA", "IM_VOCE", "ESERCIZIO_DOCAMM", "PG_DOC_AMM", "DATA_DOCAMM", "PG_REVERSALE", "DATA_INCASSO", "CD_TERZO", "DENOMINAZIONE_SEDE", "TIPO_DOC_AMM") AS 
  SELECT   
--  creata specularmente alla v_cons_obbl_linee_attivita
--  ELIMINATA V_LINEA_ATTIVITA_VALIDA - problemi performance
-- Body
--
-- Prima Union Sono gli accertamenti Collegati a Documenti Amministrativi e non, non incassati
            accertamento.cd_cds_origine, accertamento.cd_uo_origine,
            accertamento_scad_voce.cd_centro_responsabilita,
            linea_attivita.cd_linea_attivita,
            nvl(linea_attivita.ds_linea_attivita,linea_attivita.denominazione),
            accertamento.cd_elemento_voce, accertamento.dt_registrazione,
            accertamento.esercizio_originale, accertamento.pg_accertamento,
            accertamento_scadenzario.esercizio,
            accertamento_scadenzario.pg_accertamento_scadenzario,
            accertamento_scadenzario.ds_scadenza,
            accertamento_scadenzario.im_scadenza,
            DECODE
               (v_doc_attivo.ti_fattura,
                'C', 0,
                accertamento_scad_voce.im_voce
               ) im_voce,
             DECODE (TO_CHAR (v_doc_attivo.esercizio),
                    NULL, NULL,
                    TO_CHAR (v_doc_attivo.esercizio)
                   ) esercizio_docamm,
            DECODE (TO_CHAR (v_doc_attivo.pg_documento_amm),
                    NULL, NULL,
                    TO_CHAR (v_doc_attivo.pg_documento_amm)
                   ) pg_doc_amm,
            DECODE (v_doc_attivo.dt_emissione,
                    NULL, to_date(NULL),
                    v_doc_attivo.dt_emissione
                   ) data_docamm,
            null pg_reversale,
            to_date(null) data_incasso,
            accertamento.cd_terzo,
            terzo.denominazione_sede,
            DECODE
               (v_doc_attivo.cd_tipo_documento_amm,
                NULL, null,
                cnrctb002.getdestipodocamm
                                          (v_doc_attivo.cd_tipo_documento_amm)
               )
       FROM
            linea_attivita,
            accertamento,
            accertamento_scadenzario,
            accertamento_scad_voce,
            v_doc_attivo,
            terzo
      WHERE accertamento_scad_voce.cd_cds = accertamento.cd_cds
        AND accertamento_scad_voce.esercizio = accertamento.esercizio
        AND accertamento_scad_voce.esercizio_originale =
                                              accertamento.esercizio_originale
        AND accertamento_scad_voce.pg_accertamento =
                                                  accertamento.pg_accertamento
        AND accertamento_scadenzario.cd_cds = accertamento_scad_voce.cd_cds
        AND accertamento_scadenzario.esercizio =
                                              accertamento_scad_voce.esercizio
        AND accertamento_scadenzario.esercizio_originale =
                                    accertamento_scad_voce.esercizio_originale
        AND accertamento_scadenzario.pg_accertamento =
                                        accertamento_scad_voce.pg_accertamento
        AND accertamento_scadenzario.pg_accertamento_scadenzario =
                            accertamento_scad_voce.pg_accertamento_scadenzario
        AND accertamento_scadenzario.im_associato_doc_contabile = 0
        AND accertamento_scadenzario.im_scadenza != 0
        AND accertamento_scadenzario.cd_cds = v_doc_attivo.cd_cds_accertamento(+)
        AND accertamento_scadenzario.esercizio = v_doc_attivo.esercizio_accertamento(+)
        AND accertamento_scadenzario.esercizio_originale = v_doc_attivo.esercizio_ori_accertamento(+)
        AND accertamento_scadenzario.pg_accertamento = v_doc_attivo.pg_accertamento(+)
        AND accertamento_scadenzario.pg_accertamento_scadenzario = v_doc_attivo.pg_accertamento_scadenzario(+)
        AND linea_attivita.cd_centro_responsabilita = accertamento_scad_voce.cd_centro_responsabilita
        AND linea_attivita.cd_linea_attivita =
                                     accertamento_scad_voce.cd_linea_attivita
 				AND accertamento.cd_terzo = terzo.cd_terzo
 				and accertamento.cd_tipo_documento_cont in('ACR','ACR_RES')
   UNION
-- Seconda Union Sono gli accertamenti  Collegate a Documenti Amministrativi incassati
   SELECT  accertamento.cd_cds_origine, accertamento.cd_uo_origine,
            accertamento_scad_voce.cd_centro_responsabilita,
            linea_attivita.cd_linea_attivita,
            nvl(linea_attivita.ds_linea_attivita,linea_attivita.denominazione),
            accertamento.cd_elemento_voce, accertamento.dt_registrazione,
            accertamento.esercizio_originale, accertamento.pg_accertamento,
            accertamento_scadenzario.esercizio,
            accertamento_scadenzario.pg_accertamento_scadenzario,
            accertamento_scadenzario.ds_scadenza,
            accertamento_scadenzario.im_scadenza,
            DECODE
               (v_doc_attivo.ti_fattura,
                'C', 0,
                accertamento_scad_voce.im_voce
               ) im_voce,
            DECODE (TO_CHAR (v_doc_attivo.esercizio),
                    NULL, NULL,
                    TO_CHAR (v_doc_attivo.esercizio)
                   ) esercizio_docamm,
            DECODE (TO_CHAR (v_doc_attivo.pg_documento_amm),
                    NULL, NULL,
                    TO_CHAR (v_doc_attivo.pg_documento_amm)
                   ) pg_doc_amm,
            DECODE (v_doc_attivo.dt_emissione,
                    NULL, to_date(NULL),
                    v_doc_attivo.dt_emissione
                   ) data_docamm,
            DECODE (TO_CHAR (reversale_riga.pg_reversale),
                    NULL, null,
                    TO_CHAR (reversale_riga.pg_reversale)
                   ) pg_reversale,
            DECODE (reversale.dt_trasmissione,
                    NULL, to_date(null),
                    reversale.dt_trasmissione
                   ) data_incasso,
            accertamento.cd_terzo,
            terzo.denominazione_sede,
            DECODE
               (v_doc_attivo.cd_tipo_documento_amm,
                NULL, null,
                cnrctb002.getdestipodocamm
                                          (v_doc_attivo.cd_tipo_documento_amm)
               )
       FROM linea_attivita,
            accertamento,
            accertamento_scadenzario,
            accertamento_scad_voce,
            reversale,
            reversale_riga,
            terzo,
            v_doc_attivo
      WHERE accertamento_scad_voce.cd_cds = accertamento.cd_cds
        AND accertamento_scad_voce.esercizio = accertamento.esercizio
        AND accertamento_scad_voce.esercizio_originale =
                                              accertamento.esercizio_originale
        AND accertamento_scad_voce.pg_accertamento =
                                                  accertamento.pg_accertamento
        AND accertamento_scad_voce.cd_cds = accertamento_scadenzario.cd_cds
        AND accertamento_scad_voce.esercizio =
                                            accertamento_scadenzario.esercizio
        AND accertamento_scad_voce.esercizio_originale =
                                  accertamento_scadenzario.esercizio_originale
        AND accertamento_scad_voce.pg_accertamento =
                                      accertamento_scadenzario.pg_accertamento
        AND accertamento_scad_voce.pg_accertamento_scadenzario =
                          accertamento_scadenzario.pg_accertamento_scadenzario
				AND accertamento_scadenzario.im_scadenza != 0
        AND accertamento_scadenzario.im_associato_doc_amm != 0
        AND accertamento_scadenzario.im_associato_doc_contabile != 0
        AND linea_attivita.cd_centro_responsabilita = accertamento_scad_voce.cd_centro_responsabilita
        AND linea_attivita.cd_linea_attivita =
                                     accertamento_scad_voce.cd_linea_attivita
        AND
--INNER JOIN con v_doc_attivo visto che tutte le tipologie di documento attivi collegati all'accertamento
--           sono nella vista presenti
            accertamento_scadenzario.cd_cds =
                                             v_doc_attivo.cd_cds_accertamento
        AND accertamento_scadenzario.esercizio =
                                          v_doc_attivo.esercizio_accertamento
        AND accertamento_scadenzario.esercizio_originale =
                                      v_doc_attivo.esercizio_ori_accertamento
        AND accertamento_scadenzario.pg_accertamento =
                                                 v_doc_attivo.pg_accertamento
        AND accertamento_scadenzario.pg_accertamento_scadenzario =
                                     v_doc_attivo.pg_accertamento_scadenzario
        AND
--JOIN tra reversale_riga e reversale
            reversale_riga.cd_cds = reversale.cd_cds
        AND reversale_riga.esercizio = reversale.esercizio
        AND reversale_riga.PG_reversale = reversale.PG_reversale AND
--JOIN tra reversale_riga e v_doc_attivo
            reversale_riga.cd_cds = v_doc_attivo.cd_cds_accertamento
        AND reversale_riga.esercizio = v_doc_attivo.esercizio_accertamento
        AND reversale_riga.esercizio_ori_accertamento =
                                      v_doc_attivo.esercizio_ori_accertamento
        AND reversale_riga.pg_accertamento = v_doc_attivo.pg_accertamento
        AND reversale_riga.pg_accertamento_scadenzario =
                                     v_doc_attivo.pg_accertamento_scadenzario
        AND reversale_riga.cd_cds_doc_amm = v_doc_attivo.cd_cds
        AND reversale_riga.cd_uo_doc_amm = v_doc_attivo.cd_unita_organizzativa
        AND reversale_riga.esercizio_doc_amm = v_doc_attivo.esercizio
        AND reversale_riga.cd_tipo_documento_amm =
                                           v_doc_attivo.cd_tipo_documento_amm
        AND reversale_riga.pg_doc_amm = v_doc_attivo.pg_documento_amm
        AND reversale_riga.stato <> 'A'
        AND accertamento.cd_terzo = terzo.cd_terzo
        and accertamento.cd_tipo_documento_cont in('ACR','ACR_RES');
