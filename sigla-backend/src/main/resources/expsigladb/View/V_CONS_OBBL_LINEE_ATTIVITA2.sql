--------------------------------------------------------
--  DDL for View V_CONS_OBBL_LINEE_ATTIVITA2
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_CONS_OBBL_LINEE_ATTIVITA2" ("CDS", "UO", "CD_CENTRO_RESPONSABILITA", "CD_LINEA_ATTIVITA", "DS_LINEA_ATTIVITA", "CD_ELEMENTO_VOCE", "DT_REGISTRAZIONE", "ESERCIZIO_ORIGINALE", "PG_OBBLIGAZIONE", "ESERCIZIO_ORI_ORI_RIPORTO", "PG_OBBLIGAZIONE_ORI_RIPORTO", "ESERCIZIO", "ESERCIZIO_ORI_RIPORTO", "PG_OBBLIGAZIONE_SCADENZARIO", "DS_SCADENZA", "IM_SCADENZA", "IM_VOCE", "ESERCIZIO_DOCAMM", "PG_DOC_AMM", "DATA_DOCAMM", "PG_MANDATO", "DATA_PAGAMENTO", "CD_TERZO", "DENOMINAZIONE_SEDE", "TI_FATTURA", "TIPO_DOC_AMM", "ESERCIZIO_CONTRATTO", "PG_CONTRATTO") AS 
  SELECT
--
-- Date: 18/01/2007
-- Version: 1.1
--
-- History
--
-- Date: 18/07/2006
-- Version: 1.0
-- Gestione Impegni/Accertamenti Residui:
-- gestito il nuovo campo ESERCIZIO_ORIGINALE
--
-- Date: 18/01/2007
-- Version: 1.1
-- Anomalia: allineate le view V_CONS_OBBL_LINEE_ATTIVITA e PRT_STAMPA_OBBL_LINEE_ATTIVITA che devono riportare gli
--           stessi valori
--           la V_CONS_OBBL_LINEE_ATTIVITA contiene la select di ricerca mentre la PRT_STAMPA_OBBL_LINEE_ATTIVITA
--           viene ottenuta come select della V_CONS_OBBL_LINEE_ATTIVITA
--
-- Body
--
-- Prima Union Sono le Obbligazioni Collegate a Documenti Amministrativi e non, non pagati
            obbligazione.cd_cds, obbligazione.cd_unita_organizzativa,
            cdr.cd_centro_responsabilita,
            v_linea_attivita_valida.cd_linea_attivita,
            v_linea_attivita_valida.ds_linea_attivita,
            obbligazione.cd_elemento_voce, obbligazione.dt_registrazione,
            obbligazione.esercizio_originale, obbligazione.pg_obbligazione,
            obbligazione.esercizio_ori_ori_riporto,
            obbligazione.pg_obbligazione_ori_riporto,
            obbligazione_scadenzario.esercizio,
            obbligazione.esercizio_ori_riporto,
            obbligazione_scadenzario.pg_obbligazione_scadenzario,
            obbligazione_scadenzario.ds_scadenza,
            obbligazione_scadenzario.im_scadenza,
            DECODE
               (DECODE (TO_CHAR (fattura_passiva_riga.pg_fattura_passiva),
                        NULL, 'X',
                        v_doc_passivo.ti_fattura
                       ),
                'C', 0,
                obbligazione_scad_voce.im_voce
               ) im_voce,
            DECODE (TO_CHAR (v_doc_passivo.esercizio),
                    NULL, NULL,
                    TO_CHAR (v_doc_passivo.esercizio)
                   ) esercizio_docamm,
            DECODE (TO_CHAR (v_doc_passivo.pg_documento_amm),
                    NULL, NULL,
                    TO_CHAR (v_doc_passivo.pg_documento_amm)
                   ) pg_doc_amm,
            DECODE (v_doc_passivo.dt_fattura_fornitore,
                    NULL, to_date(NULL),
                    v_doc_passivo.dt_fattura_fornitore
                   ) data_docamm,
            null pg_mandato,
            to_date(null) data_pagamento,
            DECODE (TO_CHAR (v_doc_passivo.cd_terzo),
                    NULL, NULL,
                    TO_CHAR (v_doc_passivo.cd_terzo)
                   ) cd_terzo,
            (SELECT terzo.denominazione_sede
               FROM terzo
              WHERE terzo.cd_terzo =
                                    v_doc_passivo.cd_terzo)
                                                           denominazione_sede,
            DECODE
                (TO_CHAR (fattura_passiva_riga.pg_fattura_passiva),
                 NULL, 'X',
                 v_doc_passivo.ti_fattura
                ) ti_fattura,
            DECODE
               (v_doc_passivo.cd_tipo_documento_amm,
                NULL, null,
                cnrctb002.getdestipodocamm
                                          (v_doc_passivo.cd_tipo_documento_amm)
               ),
            obbligazione.esercizio_contratto, obbligazione.pg_contratto
       FROM cdr,
            elemento_voce,
            v_linea_attivita_valida,
            obbligazione,
            obbligazione_scadenzario,
            obbligazione_scad_voce,
            v_doc_passivo,
            fattura_passiva_riga
      WHERE obbligazione_scad_voce.cd_cds = obbligazione.cd_cds
        AND obbligazione_scad_voce.esercizio = obbligazione.esercizio
        AND obbligazione_scad_voce.esercizio_originale =
                                              obbligazione.esercizio_originale
        AND obbligazione_scad_voce.pg_obbligazione =
                                                  obbligazione.pg_obbligazione
        AND obbligazione_scadenzario.cd_cds = obbligazione_scad_voce.cd_cds
        AND obbligazione_scadenzario.esercizio =
                                              obbligazione_scad_voce.esercizio
        AND obbligazione_scadenzario.esercizio_originale =
                                    obbligazione_scad_voce.esercizio_originale
        AND obbligazione_scadenzario.pg_obbligazione =
                                        obbligazione_scad_voce.pg_obbligazione
        AND obbligazione_scadenzario.pg_obbligazione_scadenzario =
                            obbligazione_scad_voce.pg_obbligazione_scadenzario
        AND obbligazione_scadenzario.im_associato_doc_contabile = 0
        AND obbligazione_scadenzario.cd_cds = v_doc_passivo.cd_cds_obbligazione(+)
        AND obbligazione_scadenzario.esercizio = v_doc_passivo.esercizio_obbligazione(+)
        AND obbligazione_scadenzario.esercizio_originale = v_doc_passivo.esercizio_ori_obbligazione(+)
        AND obbligazione_scadenzario.pg_obbligazione = v_doc_passivo.pg_obbligazione(+)
        AND obbligazione_scadenzario.pg_obbligazione_scadenzario = v_doc_passivo.pg_obbligazione_scadenzario(+)
        AND obbligazione_scadenzario.cd_cds = fattura_passiva_riga.cd_cds_obbligazione(+)
        AND obbligazione_scadenzario.esercizio = fattura_passiva_riga.esercizio_obbligazione(+)
        AND obbligazione_scadenzario.esercizio_originale = fattura_passiva_riga.esercizio_ori_obbligazione(+)
        AND obbligazione_scadenzario.pg_obbligazione = fattura_passiva_riga.pg_obbligazione(+)
        AND obbligazione_scadenzario.pg_obbligazione_scadenzario = fattura_passiva_riga.pg_obbligazione_scadenzario(+)
        AND elemento_voce.esercizio = obbligazione.esercizio
        AND obbligazione.cd_elemento_voce = elemento_voce.cd_elemento_voce
        AND obbligazione_scad_voce.cd_centro_responsabilita =
                              v_linea_attivita_valida.cd_centro_responsabilita
        AND obbligazione_scad_voce.cd_linea_attivita =
                                     v_linea_attivita_valida.cd_linea_attivita
        AND obbligazione_scad_voce.esercizio =
                                             v_linea_attivita_valida.esercizio
        AND v_linea_attivita_valida.cd_centro_responsabilita =
                                                  cdr.cd_centro_responsabilita
        AND fattura_passiva_riga.dt_cancellazione IS NULL
        and v_doc_passivo.cd_tipo_documento_amm != 'ORDINE'
   UNION
-- Seconda Union Sono le Obbligazioni Collegate a Documenti Amministrativi pagati
   SELECT
            obbligazione.cd_cds, obbligazione.cd_unita_organizzativa,
            cdr.cd_centro_responsabilita,
            v_linea_attivita_valida.cd_linea_attivita,
            v_linea_attivita_valida.ds_linea_attivita,
            obbligazione.cd_elemento_voce, obbligazione.dt_registrazione,
            obbligazione.esercizio_originale, obbligazione.pg_obbligazione,
            obbligazione.esercizio_ori_ori_riporto,
            obbligazione.pg_obbligazione_ori_riporto,
            obbligazione_scadenzario.esercizio,
            obbligazione.esercizio_ori_riporto,
            obbligazione_scadenzario.pg_obbligazione_scadenzario,
            obbligazione_scadenzario.ds_scadenza,
            obbligazione_scadenzario.im_scadenza,
            DECODE
               (DECODE (TO_CHAR (fattura_passiva_riga.pg_fattura_passiva),
                        NULL, 'X',
                        v_doc_passivo.ti_fattura
                       ),
                'C', 0,
                obbligazione_scad_voce.im_voce
               ) im_voce,
            DECODE (TO_CHAR (v_doc_passivo.esercizio),
                    NULL, NULL,
                    TO_CHAR (v_doc_passivo.esercizio)
                   ) esercizio_docamm,
            DECODE (TO_CHAR (v_doc_passivo.pg_documento_amm),
                    NULL, NULL,
                    TO_CHAR (v_doc_passivo.pg_documento_amm)
                   ) pg_doc_amm,
            DECODE (v_doc_passivo.dt_fattura_fornitore,
                    NULL, to_date(NULL),
                    v_doc_passivo.dt_fattura_fornitore
                   ) data_docamm,
            DECODE (TO_CHAR (mandato_riga.pg_mandato),
                    NULL, null,
                    TO_CHAR (mandato_riga.pg_mandato)
                   ) pg_mandato,
            DECODE (mandato.dt_trasmissione,
                    NULL, to_date(null),
                    mandato.dt_trasmissione
                   ) data_pagamento,
            TO_CHAR (obbligazione.cd_terzo), terzo.denominazione_sede,
            DECODE
                (TO_CHAR (fattura_passiva_riga.pg_fattura_passiva),
                 NULL, 'X',
                 v_doc_passivo.ti_fattura
                ) ti_fattura,
            DECODE
               (v_doc_passivo.cd_tipo_documento_amm,
                NULL, null,
                cnrctb002.getdestipodocamm
                                          (v_doc_passivo.cd_tipo_documento_amm)
               ),
            obbligazione.esercizio_contratto, obbligazione.pg_contratto
       FROM cdr,
            elemento_voce,
            v_linea_attivita_valida,
            obbligazione,
            obbligazione_scadenzario,
            obbligazione_scad_voce,
            mandato_riga,
            mandato,
            terzo,
            v_doc_passivo,
            fattura_passiva_riga
      WHERE obbligazione_scad_voce.cd_cds = obbligazione.cd_cds
        AND obbligazione_scad_voce.esercizio = obbligazione.esercizio
        AND obbligazione_scad_voce.esercizio_originale =
                                              obbligazione.esercizio_originale
        AND obbligazione_scad_voce.pg_obbligazione =
                                                  obbligazione.pg_obbligazione
        AND obbligazione_scad_voce.cd_cds = obbligazione_scadenzario.cd_cds
        AND obbligazione_scad_voce.esercizio =
                                            obbligazione_scadenzario.esercizio
        AND obbligazione_scad_voce.esercizio_originale =
                                  obbligazione_scadenzario.esercizio_originale
        AND obbligazione_scad_voce.pg_obbligazione =
                                      obbligazione_scadenzario.pg_obbligazione
        AND obbligazione_scad_voce.pg_obbligazione_scadenzario =
                          obbligazione_scadenzario.pg_obbligazione_scadenzario
        AND obbligazione_scad_voce.ti_gestione = 'S'
        AND obbligazione_scadenzario.im_associato_doc_amm != 0
        AND obbligazione_scadenzario.im_associato_doc_contabile != 0
        AND elemento_voce.esercizio = obbligazione.esercizio
        AND obbligazione.cd_elemento_voce = elemento_voce.cd_elemento_voce
        AND obbligazione_scad_voce.cd_centro_responsabilita =
                              v_linea_attivita_valida.cd_centro_responsabilita
        AND obbligazione_scad_voce.cd_linea_attivita =
                                     v_linea_attivita_valida.cd_linea_attivita
        AND obbligazione_scad_voce.esercizio =
                                             v_linea_attivita_valida.esercizio
        AND v_linea_attivita_valida.cd_centro_responsabilita =
                                                  cdr.cd_centro_responsabilita
        AND
--INNER JOIN con V_DOC_PASSIVO visto che tutte le tipologie di documento passivi collegati all'obbligazione
--           sono nella vista presenti
            obbligazione_scadenzario.cd_cds =
                                             v_doc_passivo.cd_cds_obbligazione
        AND obbligazione_scadenzario.esercizio =
                                          v_doc_passivo.esercizio_obbligazione
        AND obbligazione_scadenzario.esercizio_originale =
                                      v_doc_passivo.esercizio_ori_obbligazione
        AND obbligazione_scadenzario.pg_obbligazione =
                                                 v_doc_passivo.pg_obbligazione
        AND obbligazione_scadenzario.pg_obbligazione_scadenzario =
                                     v_doc_passivo.pg_obbligazione_scadenzario
        AND
--JOIN tra MANDATO_RIGA e MANDATO
            mandato_riga.cd_cds = MANDATO.cd_cds
        AND mandato_riga.esercizio = MANDATO.esercizio
        AND mandato_riga.PG_MANDATO = mandato.PG_MANDATO AND
--JOIN tra MANDATO_RIGA e V_DOC_PASSIVO
            mandato_riga.cd_cds = v_doc_passivo.cd_cds_obbligazione
        AND mandato_riga.esercizio = v_doc_passivo.esercizio_obbligazione
        AND mandato_riga.esercizio_ori_obbligazione =
                                      v_doc_passivo.esercizio_ori_obbligazione
        AND mandato_riga.pg_obbligazione = v_doc_passivo.pg_obbligazione
        AND mandato_riga.pg_obbligazione_scadenzario =
                                     v_doc_passivo.pg_obbligazione_scadenzario
        AND mandato_riga.cd_cds_doc_amm = v_doc_passivo.cd_cds
        AND mandato_riga.cd_uo_doc_amm = v_doc_passivo.cd_unita_organizzativa
        AND mandato_riga.esercizio_doc_amm = v_doc_passivo.esercizio
        AND mandato_riga.cd_tipo_documento_amm =
                                           v_doc_passivo.cd_tipo_documento_amm
        AND mandato_riga.pg_doc_amm = v_doc_passivo.pg_documento_amm
        AND mandato_riga.stato <> 'A'
        AND
--OUTER JOIN con FATTURA_PASSIVA_RIGA visto che potrebbe esserci o meno a secondo del
--           documento passivo collegato all'obbligazione
            mandato_riga.cd_cds = fattura_passiva_riga.cd_cds_obbligazione(+)
        AND mandato_riga.esercizio = fattura_passiva_riga.esercizio_obbligazione(+)
        AND mandato_riga.esercizio_ori_obbligazione = fattura_passiva_riga.esercizio_ori_obbligazione(+)
        AND mandato_riga.pg_obbligazione = fattura_passiva_riga.pg_obbligazione(+)
        AND mandato_riga.pg_obbligazione_scadenzario = fattura_passiva_riga.pg_obbligazione_scadenzario(+)
        AND mandato_riga.cd_cds_doc_amm = fattura_passiva_riga.cd_cds(+)
        AND mandato_riga.cd_uo_doc_amm = fattura_passiva_riga.cd_unita_organizzativa(+)
        AND mandato_riga.esercizio_doc_amm = fattura_passiva_riga.esercizio(+)
        AND mandato_riga.pg_doc_amm = fattura_passiva_riga.pg_fattura_passiva(+)
        AND obbligazione.cd_terzo = terzo.cd_terzo
        AND fattura_passiva_riga.dt_cancellazione IS NULL
        and v_doc_passivo.cd_tipo_documento_amm != 'ORDINE';
