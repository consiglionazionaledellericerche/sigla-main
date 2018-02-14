--------------------------------------------------------
--  DDL for View COMPENSO_BREVETTI
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "COMPENSO_BREVETTI" ("CD_CDS", "CD_UNITA_ORGANIZZATIVA", "DS_UNITA_ORGANIZZATIVA", "ESERCIZIO", "PG_COMPENSO", "DS_COMPENSO", "CD_TERZO", "DS_TERZO", "CODICE_FISCALE", "PARTITA_IVA", "TI_ANAGRAFICO", "ESERCIZIO_FATTURA_FORNITORE", "DT_FATTURA_FORNITORE", "NR_FATTURA_FORNITORE", "IM_TOTALE_COMPENSO", "IM_LORDO_PERCIPIENTE", "IM_NETTO_PERCIPIENTE", "IMPONIBILE_FISCALE", "IMPONIBILE_IVA", "CD_VOCE_IVA", "PG_TROVATO") AS 
  (SELECT DISTINCT c.cd_cds, c.cd_unita_organizzativa,
                    u.ds_unita_organizzativa, c.esercizio, c.pg_compenso,
                    c.ds_compenso, c.cd_terzo,
                    DECODE (c.ragione_sociale,
                            NULL, c.nome || ',' || c.cognome,
                            c.ragione_sociale
                           ) ds_terzo,
                    c.codice_fiscale, c.partita_iva, ti_anagrafico,
                    c.esercizio_fattura_fornitore, c.dt_fattura_fornitore,
                    c.nr_fattura_fornitore, c.im_totale_compenso,
                    c.im_lordo_percipiente, c.im_netto_percipiente,
                    c.imponibile_fiscale, c.imponibile_iva, c.cd_voce_iva,
                    c.pg_trovato
               FROM compenso c,
                    obbligazione o,
                    obbligazione_scadenzario os,
                    unita_organizzativa u
              WHERE c.cd_unita_organizzativa = u.cd_unita_organizzativa
--And f.cd_terzo = t.cd_terzo
--And t.cd_anag = a.cd_anag
                AND c.cd_cds_obbligazione = os.cd_cds
                AND c.esercizio_obbligazione = os.esercizio
                AND c.esercizio_ori_obbligazione = os.esercizio_originale
                AND c.pg_obbligazione = os.pg_obbligazione
                AND c.pg_obbligazione_scadenzario =
                                                os.pg_obbligazione_scadenzario
                AND os.esercizio = o.esercizio
                AND os.cd_cds = o.cd_cds
                AND os.esercizio_originale = o.esercizio_originale
                AND os.pg_obbligazione = o.pg_obbligazione
/*And mr.cd_cds (+)= os.cd_cds
and mr.esercizio(+)  = os.esercizio
And mr.esercizio_ori_obbligazione(+) = os.esercizio_originale
And mr.pg_obbligazione(+) = os.pg_obbligazione
and mr.pg_obbligazione_scadenzario(+) = os.pg_obbligazione_scadenzario*/
/*and mr.ESERCIZIO(+) = m.ESERCIZIO (+)
and mr.CD_CDS(+) = m.CD_CDS (+)
and mr.PG_MANDATO(+) = m.PG_MANDATO(+)*/
                AND (o.cd_elemento_voce = '1.01.132' or c.pg_trovato is not null)
                AND c.stato_cofi != 'A') ;
