--------------------------------------------------------
--  DDL for View PRT_ELENCO_FATT_PER_CLIENTE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "PRT_ELENCO_FATT_PER_CLIENTE" ("CD_TERZO", "RAGIONE_SOCIALE", "NOME", "COGNOME", "CODICE_FISCALE", "PARTITA_IVA", "CD_CDS_FATT", "CD_UNITA_ORGANIZZATIVA_FATT", "ESERCIZIO_FATT", "PG_FATTURA_ATTIVA_FATT", "CD_CDS_ORIGINE_FATT", "DES_CDS", "CD_UO_ORIGINE_FATT", "DES_UO", "TI_FATTURA_FATT", "PROTOCOLLO_IVA_FATT", "DT_REGISTRAZIONE_FATT", "DT_EMISSIONE_FATT", "CD_MODALITA_PAG_UO_CDS", "IM_TOTALE_IMPONIBILE_FATT", "IM_TOTALE_IVA_FATT", "IM_TOTALE_FATTURA_FATT", "CD_DIVISA_FATT", "RIFERIMENTO_ORDINE_FATT", "PROGRESSIVO_RIGA", "CD_TARIFFARIO_RIGA", "DS_RIGA_FATTURA", "IM_IMPONIBILE_RIGA", "CD_VOCE_IVA_RIGA", "IM_IVA_RIGA", "DT_DA_COMPETENZA_COGE_RIGA", "DT_A_COMPETENZA_COGE_RIGA", "ENT_SPE", "CD_CDS_ACC_OBB", "ESERCIZIO_ACC_OBB", "ESERCIZIO_ORI_ACC_OBB", "PG_ACC_OBB", "PG_ACC_OBB_SCAD", "CD_CDS_REV_MAN", "ESERCIZIO_REV_MAN", "PG_REV_MAN", "PROV_DEF_REV_MAN", "TIPO_REV_MAN", "COMP_RES_REV_MAN", "DS_REV_MAN", "DT_EM_REV_MAN", "DT_TRASMISSIONE_REV_MAN", "DT_INCASSO_REV_MAN", "SPLIT_PAYMENT") AS 
  SELECT
--
-- Date: 18/07/2006
-- Version: 1.3
--
-- Vista per stampa elenco fatture attive per cliente
--
-- History:
--
-- Date: 28/07/2004
-- Version: 1.0
-- Creazione
--
-- Date: 06/08/2004
-- Version: 1.1
-- Rinominata view, cambiato in string il tipo delle date
--
-- Date: 14/01/2005
-- Version: 1.2
-- Aggiunte le fatture attive e le note non ancora riscosse
--
-- Date: 18/07/2006
-- Version: 1.3
-- Gestione Impegni/Accertamenti Residui:
-- gestito il nuovo campo ESERCIZIO_ORIGINALE
--
-- Body:
--FATTURE E NOTE GIA' RISCOSSE
          fattura_attiva.cd_terzo, anagrafico.ragione_sociale,
          anagrafico.nome, anagrafico.cognome, anagrafico.codice_fiscale,
          anagrafico.partita_iva, fattura_attiva.cd_cds,
          fattura_attiva.cd_unita_organizzativa, fattura_attiva.esercizio,
          fattura_attiva.pg_fattura_attiva, fattura_attiva.cd_cds_origine,
          uo_cds.ds_unita_organizzativa, fattura_attiva.cd_uo_origine,
          uo_uo.ds_unita_organizzativa, fattura_attiva.ti_fattura,
          fattura_attiva.protocollo_iva, fattura_attiva.dt_registrazione,
          fattura_attiva.dt_emissione, fattura_attiva.cd_modalita_pag_uo_cds,
          fattura_attiva.im_totale_imponibile, fattura_attiva.im_totale_iva,
          fattura_attiva.im_totale_fattura, fattura_attiva.cd_divisa,
          fattura_attiva.riferimento_ordine,
          fattura_attiva_riga.progressivo_riga,
          fattura_attiva_riga.cd_tariffario,
          fattura_attiva_riga.ds_riga_fattura,
          fattura_attiva_riga.im_imponibile, fattura_attiva_riga.cd_voce_iva,
          fattura_attiva_riga.im_iva,
          fattura_attiva_riga.dt_da_competenza_coge,
          fattura_attiva_riga.dt_a_competenza_coge, 'E',
          fattura_attiva_riga.cd_cds_accertamento,
          fattura_attiva_riga.esercizio_accertamento,
          fattura_attiva_riga.esercizio_ori_accertamento,
          fattura_attiva_riga.pg_accertamento,
          fattura_attiva_riga.pg_accertamento_scadenzario,
          reversale_riga.cd_cds, reversale_riga.esercizio,
          reversale_riga.pg_reversale, reversale.cd_tipo_documento_cont,
          reversale.ti_reversale, reversale.ti_competenza_residuo,
          reversale.ds_reversale, reversale.dt_emissione,
          reversale.dt_trasmissione, reversale.dt_incasso,
          DECODE
             (fattura_attiva.fl_liquidazione_differita,
              'Y', DECODE (  NVL (fattura_attiva.dt_emissione,
                                  fattura_attiva.dt_registrazione
                                 )
                           - NVL (c.dt01,
                                  NVL (fattura_attiva.dt_emissione,
                                       fattura_attiva.dt_registrazione
                                      )
                                 ),
                           ABS (  NVL (fattura_attiva.dt_emissione,
                                       fattura_attiva.dt_registrazione
                                      )
                                - NVL (c.dt01,
                                       NVL (fattura_attiva.dt_emissione,
                                            fattura_attiva.dt_registrazione
                                           )
                                      )
                               ), 'Y',
                           'N'
                          ),
              'N'
             ) split_payment
     FROM fattura_attiva,
          fattura_attiva_riga,
          reversale,
          reversale_riga,
          anagrafico,
          terzo,
          unita_organizzativa uo_cds,
          unita_organizzativa uo_uo,
          configurazione_cnr c
    WHERE c.esercizio = 0
      AND c.cd_chiave_primaria = 'SPLIT_PAYMENT'
      AND c.cd_chiave_secondaria = 'ATTIVA'
      AND c.cd_unita_funzionale = '*'
      AND fattura_attiva.cd_terzo = terzo.cd_terzo
      AND terzo.cd_anag = anagrafico.cd_anag
      AND uo_cds.cd_unita_organizzativa = fattura_attiva.cd_cds_origine
      AND uo_uo.cd_unita_organizzativa = fattura_attiva.cd_uo_origine
      AND fattura_attiva.cd_cds = fattura_attiva_riga.cd_cds
      AND fattura_attiva.cd_unita_organizzativa =
                                    fattura_attiva_riga.cd_unita_organizzativa
      AND fattura_attiva.esercizio = fattura_attiva_riga.esercizio
      AND fattura_attiva.pg_fattura_attiva =
                                         fattura_attiva_riga.pg_fattura_attiva
      AND reversale_riga.cd_cds = reversale.cd_cds
      AND reversale_riga.esercizio = reversale.esercizio
      AND reversale_riga.pg_reversale = reversale.pg_reversale
      AND reversale_riga.cd_cds = fattura_attiva_riga.cd_cds
      AND
          REVERSALE_RIGA.ESERCIZIO_ACCERTAMENTO      = FATTURA_ATTIVA_RIGA.ESERCIZIO_ACCERTAMENTO                   AND
          reversale_riga.esercizio_ori_accertamento =
                                fattura_attiva_riga.esercizio_ori_accertamento
      AND reversale_riga.pg_accertamento = fattura_attiva_riga.pg_accertamento
      AND reversale_riga.pg_accertamento_scadenzario =
                               fattura_attiva_riga.pg_accertamento_scadenzario
      AND reversale_riga.cd_cds_doc_amm = fattura_attiva_riga.cd_cds
      AND reversale_riga.cd_uo_doc_amm =
                                    fattura_attiva_riga.cd_unita_organizzativa
      AND reversale_riga.esercizio_doc_amm = fattura_attiva_riga.esercizio
      AND reversale_riga.cd_tipo_documento_amm = 'FATTURA_A'
      AND reversale_riga.pg_doc_amm = fattura_attiva_riga.pg_fattura_attiva
      AND reversale_riga.stato != 'A'
   UNION
--FATTURE E NOTE NON RISCOSSE E NON PAGATE (se ci sono solo note viene emesso un mandato)
   SELECT fattura_attiva.cd_terzo, anagrafico.ragione_sociale,
          anagrafico.nome, anagrafico.cognome, anagrafico.codice_fiscale,
          anagrafico.partita_iva, fattura_attiva.cd_cds,
          fattura_attiva.cd_unita_organizzativa, fattura_attiva.esercizio,
          fattura_attiva.pg_fattura_attiva, fattura_attiva.cd_cds_origine,
          uo_cds.ds_unita_organizzativa, fattura_attiva.cd_uo_origine,
          uo_uo.ds_unita_organizzativa, fattura_attiva.ti_fattura,
          fattura_attiva.protocollo_iva, fattura_attiva.dt_registrazione,
          fattura_attiva.dt_emissione, fattura_attiva.cd_modalita_pag_uo_cds,
          fattura_attiva.im_totale_imponibile, fattura_attiva.im_totale_iva,
          fattura_attiva.im_totale_fattura, fattura_attiva.cd_divisa,
          fattura_attiva.riferimento_ordine,
          fattura_attiva_riga.progressivo_riga,
          fattura_attiva_riga.cd_tariffario,
          fattura_attiva_riga.ds_riga_fattura,
          fattura_attiva_riga.im_imponibile, fattura_attiva_riga.cd_voce_iva,
          fattura_attiva_riga.im_iva,
          fattura_attiva_riga.dt_da_competenza_coge,
          fattura_attiva_riga.dt_a_competenza_coge, 'E',
          fattura_attiva_riga.cd_cds_accertamento,
          DECODE (fattura_attiva_riga.esercizio_accertamento,
                  NULL, fattura_attiva_riga.esercizio_obbligazione,
                  fattura_attiva_riga.esercizio_accertamento
                 ),
          DECODE (fattura_attiva_riga.esercizio_ori_accertamento,
                  NULL, fattura_attiva_riga.esercizio_ori_obbligazione,
                  fattura_attiva_riga.esercizio_ori_accertamento
                 ),
          DECODE (fattura_attiva_riga.pg_accertamento,
                  NULL, fattura_attiva_riga.pg_obbligazione,
                  fattura_attiva_riga.pg_accertamento
                 ),
          DECODE (fattura_attiva_riga.pg_accertamento_scadenzario,
                  NULL, fattura_attiva_riga.pg_obbligazione_scadenzario,
                  fattura_attiva_riga.pg_accertamento_scadenzario
                 ),
          NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
          DECODE
             (fattura_attiva.fl_liquidazione_differita,
              'Y', DECODE (  NVL (fattura_attiva.dt_emissione,
                                  fattura_attiva.dt_registrazione
                                 )
                           - NVL (c.dt01,
                                  NVL (fattura_attiva.dt_emissione,
                                       fattura_attiva.dt_registrazione
                                      )
                                 ),
                           ABS (  NVL (fattura_attiva.dt_emissione,
                                       fattura_attiva.dt_registrazione
                                      )
                                - NVL (c.dt01,
                                       NVL (fattura_attiva.dt_emissione,
                                            fattura_attiva.dt_registrazione
                                           )
                                      )
                               ), 'Y',
                           'N'
                          ),
              'N'
             ) split_payment
     FROM fattura_attiva,
          fattura_attiva_riga,
          anagrafico,
          terzo,
          unita_organizzativa uo_cds,
          unita_organizzativa uo_uo,
          configurazione_cnr c
    WHERE c.esercizio = 0
      AND c.cd_chiave_primaria = 'SPLIT_PAYMENT'
      AND c.cd_chiave_secondaria = 'ATTIVA'
      AND c.cd_unita_funzionale = '*'
      AND fattura_attiva.cd_terzo = terzo.cd_terzo
      AND terzo.cd_anag = anagrafico.cd_anag
      AND uo_cds.cd_unita_organizzativa = fattura_attiva.cd_cds_origine
      AND uo_uo.cd_unita_organizzativa = fattura_attiva.cd_uo_origine
      AND fattura_attiva.cd_cds = fattura_attiva_riga.cd_cds
      AND fattura_attiva.cd_unita_organizzativa =
                                    fattura_attiva_riga.cd_unita_organizzativa
      AND fattura_attiva.esercizio = fattura_attiva_riga.esercizio
      AND fattura_attiva.pg_fattura_attiva =
                                         fattura_attiva_riga.pg_fattura_attiva
      AND NOT EXISTS (
             SELECT '1'
               FROM reversale_riga
              WHERE reversale_riga.cd_cds = fattura_attiva_riga.cd_cds
                AND
                    REVERSALE_RIGA.ESERCIZIO_ACCERTAMENTO = FATTURA_ATTIVA_RIGA.ESERCIZIO_ACCERTAMENTO And
                    reversale_riga.esercizio_ori_accertamento =
                                fattura_attiva_riga.esercizio_ori_accertamento
                AND reversale_riga.pg_accertamento =
                                           fattura_attiva_riga.pg_accertamento
                AND reversale_riga.pg_accertamento_scadenzario =
                               fattura_attiva_riga.pg_accertamento_scadenzario
                AND reversale_riga.cd_cds_doc_amm = fattura_attiva_riga.cd_cds
                AND reversale_riga.cd_uo_doc_amm =
                                    fattura_attiva_riga.cd_unita_organizzativa
                AND reversale_riga.esercizio_doc_amm =
                                                 fattura_attiva_riga.esercizio
                AND reversale_riga.cd_tipo_documento_amm = 'FATTURA_A'
                AND reversale_riga.pg_doc_amm =
                                         fattura_attiva_riga.pg_fattura_attiva
                AND reversale_riga.stato != 'A')
      AND NOT EXISTS (
             SELECT '1'
               FROM mandato_riga
              WHERE mandato_riga.cd_cds = fattura_attiva_riga.cd_cds
                AND
                    MANDATO_RIGA.ESERCIZIO_OBBLIGAZIONE = FATTURA_ATTIVA_RIGA.ESERCIZIO_OBBLIGAZIONE And
                    mandato_riga.esercizio_ori_obbligazione =
                                fattura_attiva_riga.esercizio_ori_obbligazione
                AND mandato_riga.pg_obbligazione =
                                           fattura_attiva_riga.pg_obbligazione
                AND mandato_riga.pg_obbligazione_scadenzario =
                               fattura_attiva_riga.pg_obbligazione_scadenzario
                AND mandato_riga.cd_cds_doc_amm = fattura_attiva_riga.cd_cds
                AND mandato_riga.cd_uo_doc_amm =
                                    fattura_attiva_riga.cd_unita_organizzativa
                AND mandato_riga.esercizio_doc_amm =
                                                 fattura_attiva_riga.esercizio
                AND mandato_riga.cd_tipo_documento_amm = 'FATTURA_A'
                AND mandato_riga.pg_doc_amm =
                                         fattura_attiva_riga.pg_fattura_attiva
                AND mandato_riga.stato != 'A')
--NOTE PER LE QUALI VIENE EMESSO UN MANDATO
   UNION
   SELECT fattura_attiva.cd_terzo, anagrafico.ragione_sociale,
          anagrafico.nome, anagrafico.cognome, anagrafico.codice_fiscale,
          anagrafico.partita_iva, fattura_attiva.cd_cds,
          fattura_attiva.cd_unita_organizzativa, fattura_attiva.esercizio,
          fattura_attiva.pg_fattura_attiva, fattura_attiva.cd_cds_origine,
          uo_cds.ds_unita_organizzativa, fattura_attiva.cd_uo_origine,
          uo_uo.ds_unita_organizzativa, fattura_attiva.ti_fattura,
          fattura_attiva.protocollo_iva, fattura_attiva.dt_registrazione,
          fattura_attiva.dt_emissione, fattura_attiva.cd_modalita_pag_uo_cds,
          fattura_attiva.im_totale_imponibile, fattura_attiva.im_totale_iva,
          fattura_attiva.im_totale_fattura, fattura_attiva.cd_divisa,
          fattura_attiva.riferimento_ordine,
          fattura_attiva_riga.progressivo_riga,
          fattura_attiva_riga.cd_tariffario,
          fattura_attiva_riga.ds_riga_fattura,
          fattura_attiva_riga.im_imponibile, fattura_attiva_riga.cd_voce_iva,
          fattura_attiva_riga.im_iva,
          fattura_attiva_riga.dt_da_competenza_coge,
          fattura_attiva_riga.dt_a_competenza_coge, 'S',
          fattura_attiva_riga.cd_cds_obbligazione,
          fattura_attiva_riga.esercizio_obbligazione,
          fattura_attiva_riga.esercizio_ori_obbligazione,
          fattura_attiva_riga.pg_obbligazione,
          fattura_attiva_riga.pg_obbligazione_scadenzario,
          mandato_riga.cd_cds, mandato_riga.esercizio,
          mandato_riga.pg_mandato, mandato.cd_tipo_documento_cont,
          mandato.ti_mandato, mandato.ti_competenza_residuo,
          mandato.ds_mandato, mandato.dt_emissione, mandato.dt_trasmissione,
          mandato.dt_pagamento,
          DECODE
             (fattura_attiva.fl_liquidazione_differita,
              'Y', DECODE (  NVL (fattura_attiva.dt_emissione,
                                  fattura_attiva.dt_registrazione
                                 )
                           - NVL (c.dt01,
                                  NVL (fattura_attiva.dt_emissione,
                                       fattura_attiva.dt_registrazione
                                      )
                                 ),
                           ABS (  NVL (fattura_attiva.dt_emissione,
                                       fattura_attiva.dt_registrazione
                                      )
                                - NVL (c.dt01,
                                       NVL (fattura_attiva.dt_emissione,
                                            fattura_attiva.dt_registrazione
                                           )
                                      )
                               ), 'Y',
                           'N'
                          ),
              'N'
             ) split_payment
     FROM fattura_attiva,
          fattura_attiva_riga,
          mandato,
          mandato_riga,
          anagrafico,
          terzo,
          unita_organizzativa uo_cds,
          unita_organizzativa uo_uo,
          configurazione_cnr c
    WHERE c.esercizio = 0
      AND c.cd_chiave_primaria = 'SPLIT_PAYMENT'
      AND c.cd_chiave_secondaria = 'ATTIVA'
      AND c.cd_unita_funzionale = '*'
      AND fattura_attiva.cd_terzo = terzo.cd_terzo
      AND terzo.cd_anag = anagrafico.cd_anag
      AND uo_cds.cd_unita_organizzativa = fattura_attiva.cd_cds_origine
      AND uo_uo.cd_unita_organizzativa = fattura_attiva.cd_uo_origine
      AND fattura_attiva.cd_cds = fattura_attiva_riga.cd_cds
      AND fattura_attiva.cd_unita_organizzativa =
                                    fattura_attiva_riga.cd_unita_organizzativa
      AND fattura_attiva.esercizio = fattura_attiva_riga.esercizio
      AND fattura_attiva.pg_fattura_attiva =
                                         fattura_attiva_riga.pg_fattura_attiva
      AND fattura_attiva.ti_fattura = 'C'
      AND mandato_riga.cd_cds = mandato.cd_cds
      AND mandato_riga.esercizio = mandato.esercizio
      AND mandato_riga.pg_mandato = mandato.pg_mandato
      AND mandato_riga.cd_cds = fattura_attiva_riga.cd_cds
      AND
          mANDATO_RIGA.ESERCIZIO_OBBLIGAZIONE      = FATTURA_ATTIVA_RIGA.ESERCIZIO_OBBLIGAZIONE                   AND
          mandato_riga.esercizio_ori_obbligazione =
                                fattura_attiva_riga.esercizio_ori_obbligazione
      AND mandato_riga.pg_obbligazione = fattura_attiva_riga.pg_obbligazione
      AND mandato_riga.pg_obbligazione_scadenzario =
                               fattura_attiva_riga.pg_obbligazione_scadenzario
      AND mandato_riga.cd_cds_doc_amm = fattura_attiva_riga.cd_cds
      AND mandato_riga.cd_uo_doc_amm =
                                    fattura_attiva_riga.cd_unita_organizzativa
      AND mandato_riga.esercizio_doc_amm = fattura_attiva_riga.esercizio
      AND mandato_riga.cd_tipo_documento_amm = 'FATTURA_A'
      AND mandato_riga.pg_doc_amm = fattura_attiva_riga.pg_fattura_attiva
      AND mandato_riga.stato != 'A' ;
