--------------------------------------------------------
--  DDL for View FATTURE_BREVETTI_ATTIVE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "FATTURE_BREVETTI_ATTIVE" ("CD_CDS", "CD_UNITA_ORGANIZZATIVA", "DS_UNITA_ORGANIZZATIVA", "ESERCIZIO", "PG_FATTURA_PASSIVA", "PROGRESSIVO_RIGA", "CD_TERZO", "CREDITORE", "CODICE_FISCALE", "PARTITA_IVA", "TIPO", "DS_FATTURA_PASSIVA", "DS_RIGA_FATTURA", "IM_TOTALE_FATTURA", "IM_IMPONIBILE", "IM_IVA", "CD_VOCE_IVA", "PG_TROVATO") AS 
  (SELECT DISTINCT f.cd_cds_origine, f.cd_uo_origine,
                    u.ds_unita_organizzativa, f.esercizio,
                    f.pg_fattura_attiva, fr.progressivo_riga, f.cd_terzo,
                    DECODE (f.ragione_sociale,
                            NULL, f.nome || ',' || f.cognome,
                            f.ragione_sociale
                           ) fornitore,
                    f.codice_fiscale, f.partita_iva,
                    DECODE (f.ti_fattura,
                            'C', 'Nota di Credito',
                            'Fattura'
                           ) tipo,
                    f.ds_fattura_attiva, fr.ds_riga_fattura,
                    DECODE (f.ti_fattura,
                            'C', (f.im_totale_fattura) * -1,
                            f.im_totale_fattura
                           ),
                    DECODE (f.ti_fattura,
                            'C', (fr.im_imponibile) * -1,
                            fr.im_imponibile
                           ),
                    DECODE (f.ti_fattura, 'C', (fr.im_iva) * -1, fr.im_iva),
                    fr.cd_voce_iva, fr.pg_trovato
               FROM fattura_attiva f,
                    fattura_attiva_riga fr,
                    accertamento o,
                    accertamento_scadenzario os,
                    unita_organizzativa u
              WHERE f.esercizio = fr.esercizio
                AND f.cd_cds = fr.cd_cds
                AND f.cd_unita_organizzativa = fr.cd_unita_organizzativa
                AND f.pg_fattura_attiva = fr.pg_fattura_attiva
                AND f.cd_uo_origine = u.cd_unita_organizzativa
                AND fr.cd_cds_accertamento = os.cd_cds
                AND fr.esercizio_accertamento = os.esercizio
                AND fr.esercizio_ori_accertamento = os.esercizio_originale
                AND fr.pg_accertamento = os.pg_accertamento
                AND fr.pg_accertamento_scadenzario =
                                                os.pg_accertamento_scadenzario
                AND os.esercizio = o.esercizio
                AND os.cd_cds = o.cd_cds
                AND os.esercizio_originale = o.esercizio_originale
                AND os.pg_accertamento = o.pg_accertamento
                AND fr.pg_Trovato is not null
                AND f.stato_cofi != 'A') ;
