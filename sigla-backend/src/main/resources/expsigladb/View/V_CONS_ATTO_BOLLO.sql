/* Formatted on 2018/02/09 15:46 (Formatter Plus v4.8.8) */
CREATE OR REPLACE FORCE VIEW v_cons_atto_bollo (esercizio,
                                                descrizione_atto,
                                                cd_unita_organizzativa,
                                                ds_unita_organizzativa,
                                                cd_tipo_atto,
                                                ds_tipo_atto,
                                                riferimento,
                                                ti_dettagli,
                                                num_dettagli,
                                                im_bollo,
                                                im_totale_bollo
                                               )
AS
   (SELECT atto_bollo.esercizio, atto_bollo.descrizione_atto,
           atto_bollo.cd_unita_organizzativa,
           unita_organizzativa.ds_unita_organizzativa, tipo_atto_bollo.codice,
           tipo_atto_bollo.descrizione,
              atto_bollo.cd_provv
           || CASE
                 WHEN atto_bollo.cd_provv != NULL
                 AND atto_bollo.nr_provv != NULL
                    THEN '-'
                 ELSE ''
              END
           || atto_bollo.nr_provv
           || CASE
                 WHEN atto_bollo.dt_provv != NULL
                    THEN ' del '
                         || TO_CHAR (atto_bollo.dt_provv, 'DD/MM/YYYY')
                 ELSE ''
              END riferimento,
           tipo_atto_bollo.tipo_calcolo ti_dettagli,
           NVL (atto_bollo.num_dettagli, 0) num_dettagli,
           NVL (tipo_atto_bollo.im_bollo, 0) im_bollo,
             NVL (atto_bollo.num_dettagli, 0)
           * NVL (tipo_atto_bollo.im_bollo, 0) im_totale_bollo
      FROM atto_bollo, tipo_atto_bollo, unita_organizzativa
     WHERE atto_bollo.id_tipo_atto_bollo = tipo_atto_bollo.ID
       AND atto_bollo.cd_unita_organizzativa =
                                    unita_organizzativa.cd_unita_organizzativa
    UNION ALL
    SELECT fattura_attiva.esercizio, fattura_attiva.ds_fattura_attiva,
           fattura_attiva.cd_uo_origine,
           unita_organizzativa.ds_unita_organizzativa, tipo_atto_bollo.codice,
           tipo_atto_bollo.descrizione,
              fattura_attiva.cd_cds
           || '/'
           || fattura_attiva.esercizio
           || '/'
           || fattura_attiva.pg_fattura_attiva,
           'E' ti_dettagli, 1 num_dettagli,
           NVL (fattura_attiva_riga.im_totale_divisa, 0) im_bollo,
           NVL (fattura_attiva_riga.im_totale_divisa, 0) im_totale_bollo
      FROM fattura_attiva,
           fattura_attiva_riga,
           bene_servizio,
           tipo_atto_bollo,
           unita_organizzativa
     WHERE fattura_attiva.cd_cds = fattura_attiva_riga.cd_cds
       AND fattura_attiva.cd_unita_organizzativa =
                                    fattura_attiva_riga.cd_unita_organizzativa
       AND fattura_attiva.esercizio = fattura_attiva_riga.esercizio
       AND fattura_attiva.pg_fattura_attiva =
                                         fattura_attiva_riga.pg_fattura_attiva
       AND fattura_attiva_riga.cd_bene_servizio =
                                                bene_servizio.cd_bene_servizio
       AND bene_servizio.fl_bollo = 'Y'
       AND fattura_attiva.cd_uo_origine =
                                    unita_organizzativa.cd_unita_organizzativa
       AND tipo_atto_bollo.codice =
              (SELECT val01
                 FROM configurazione_cnr
                WHERE configurazione_cnr.esercizio = fattura_attiva.esercizio
                  AND configurazione_cnr.cd_unita_funzionale = '*'
                  AND configurazione_cnr.cd_chiave_primaria = 'BOLLO_VIRTUALE'
                  AND configurazione_cnr.cd_chiave_secondaria =
                                             'CODICE_DOCUMENTO_FATTURA_ATTIVA')
       AND tipo_atto_bollo.dt_fin_validita IS NULL);

