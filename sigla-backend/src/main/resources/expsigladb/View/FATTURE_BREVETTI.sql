--------------------------------------------------------
--  DDL for View FATTURE_BREVETTI
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "FATTURE_BREVETTI" ("CD_CDS", "CD_UNITA_ORGANIZZATIVA", "DS_UNITA_ORGANIZZATIVA", "ESERCIZIO", "PG_FATTURA_PASSIVA", "PROGRESSIVO_RIGA", "NR_FATTURA_FORNITORE", "DT_FATTURA_FORNITORE", "ESERCIZIO_FATTURA_FORNITORE", "CD_TERZO", "FORNITORE", "CODICE_FISCALE", "PARTITA_IVA", "TIPO", "DS_FATTURA_PASSIVA", "DS_RIGA_FATTURA", "IM_TOTALE_FATTURA", "IM_IMPONIBILE", "IM_IVA", "CD_VOCE_IVA", "PG_TROVATO", "CD_CIG") AS 
  (SELECT DISTINCT f.cd_cds, f.cd_unita_organizzativa,
                    u.ds_unita_organizzativa, f.esercizio,
                    f.pg_fattura_passiva, fr.progressivo_riga,
                    f.nr_fattura_fornitore, f.dt_fattura_fornitore,
                    f.esercizio_fattura_fornitore, f.cd_terzo,
                    DECODE (f.ragione_sociale,
                            NULL, f.nome || ',' || f.cognome,
                            f.ragione_sociale
                           ) fornitore,
                    f.codice_fiscale, f.partita_iva,
                    DECODE (f.ti_fattura,
                            'C', 'Nota di Credito',
                            'Fattura'
                           ) tipo,
                    f.ds_fattura_passiva, fr.ds_riga_fattura,
                    DECODE (f.ti_fattura,
                            'C', (f.im_totale_fattura) * -1,
                            f.im_totale_fattura
                           ),
                    DECODE (f.ti_fattura,
                            'C', (fr.im_imponibile) * -1,
                            fr.im_imponibile
                           ),
                    DECODE (f.ti_fattura, 'C', (fr.im_iva) * -1, fr.im_iva),
                    fr.cd_voce_iva, fr.pg_trovato, cd_cig                  /*,
                                        mr.ESERCIZIO esercizio_mandato,
                                        mr.PG_MANDATO*/
               FROM fattura_passiva f,
                    fattura_passiva_riga fr,
                    obbligazione o,
                    obbligazione_scadenzario os,
                    unita_organizzativa u,
                    contratto co
              /*mandato m, */--mandato_riga mr
                 --terzo t, anagrafica a
    WHERE           f.esercizio = fr.esercizio
                AND f.cd_cds = fr.cd_cds
                AND f.cd_unita_organizzativa = fr.cd_unita_organizzativa
                AND f.pg_fattura_passiva = fr.pg_fattura_passiva
                AND f.cd_unita_organizzativa = u.cd_unita_organizzativa
                AND o.esercizio_contratto = co.esercizio(+)
                AND o.stato_contratto = co.stato(+)
                AND o.pg_contratto = co.pg_contratto(+)
                AND fr.cd_cds_obbligazione = os.cd_cds
                AND fr.esercizio_obbligazione = os.esercizio
                AND fr.esercizio_ori_obbligazione = os.esercizio_originale
                AND fr.pg_obbligazione = os.pg_obbligazione
                AND fr.pg_obbligazione_scadenzario =
                                                os.pg_obbligazione_scadenzario
                AND os.esercizio = o.esercizio
                AND os.cd_cds = o.cd_cds
                AND os.esercizio_originale = o.esercizio_originale
                AND os.pg_obbligazione = o.pg_obbligazione
                AND (o.cd_elemento_voce = '1.01.132'
                or fr.pg_trovato IS NOT NULL)
                AND f.stato_cofi != 'A') ;
