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
                    cr.pg_trovato
               FROM compenso c,
                    compenso_riga cr,
                    obbligazione o,
                    obbligazione_scadenzario os,
                    unita_organizzativa u
              WHERE c.cd_cds = cr.cd_cds
                AND c.cd_unita_organizzativa = cr.cd_unita_organizzativa
                AND c.esercizio = cr.esercizio
                AND c.pg_compenso = cr.pg_compenso
                AND c.cd_unita_organizzativa = u.cd_unita_organizzativa
                AND cr.cd_cds_obbligazione = os.cd_cds
                AND cr.esercizio_obbligazione = os.esercizio
                AND cr.esercizio_ori_obbligazione = os.esercizio_originale
                AND cr.pg_obbligazione = os.pg_obbligazione
                AND cr.pg_obbligazione_scadenzario = os.pg_obbligazione_scadenzario
                AND os.esercizio = o.esercizio
                AND os.cd_cds = o.cd_cds
                AND os.esercizio_originale = o.esercizio_originale
                AND os.pg_obbligazione = o.pg_obbligazione
                AND (o.cd_elemento_voce = '1.01.132' or cr.pg_trovato is not null)
                AND c.stato_cofi != 'A') ;
