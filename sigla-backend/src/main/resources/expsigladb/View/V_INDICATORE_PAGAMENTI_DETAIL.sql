--------------------------------------------------------
--  DDL for View V_INDICATORE_PAGAMENTI_DETAIL
--------------------------------------------------------
CREATE OR REPLACE FORCE VIEW "V_INDICATORE_PAGAMENTI_DETAIL" (
    "ESERCIZIO_DOCUMENTO",
    "UO_DOCUMENTO",
    "NUMERO_DOCUMENTO",
    "TIPO_DOCUMENTO",
    "IMPORTO_DOCUMENTO",
    "DATA_SCADENZA",
    "DATA_TRASMISSIONE",
    "DATA_RICEZIONE",
    "DATA_REGISTRAZIONE",
    "DATA_LIQUIDAZIONE",
    "DIFFERENZA_GIORNI",
    "IMPORTO_PAGATO",
    "IMPORTO_PESATO",
    "CD_ANAG",
    "CD_TERZO",
    "ESERCIZIO_OBBLIGAZIONE",
    "CD_CDS_OBBLIGAZIONE",
    "PG_OBBLIGAZIONE",
    "PG_OBBLIGAZIONE_SCADENZARIO",
    "ESERCIZIO_ORI_OBBLIGAZIONE",
    "ESERCIZIO_CONTRATTO",
    "PG_CONTRATTO",
    "ESERCIZIO_INC",
    "PG_INCARICO",
    "IDENTIFICATIVO_SDI"
) AS (
    SELECT ESERCIZIO_DOCUMENTO, UO_DOCUMENTO, NUMERO_DOCUMENTO, TIPO_DOCUMENTO, SUM(IMPORTO_DOCUMENTO),
           DATA_SCADENZA, DATA_TRASMISSIONE, DATA_RICEZIONE, DATA_REGISTRAZIONE, DATA_LIQUIDAZIONE,
           SUM(DIFFERENZA_GIORNI), SUM(IMPORTO_PAGATO), SUM(IMPORTO_PESATO), CD_ANAG, CD_TERZO, ESERCIZIO_OBBLIGAZIONE, CD_CDS_OBBLIGAZIONE,
           PG_OBBLIGAZIONE, PG_OBBLIGAZIONE_SCADENZARIO, ESERCIZIO_ORI_OBBLIGAZIONE, ESERCIZIO_CONTRATTO, PG_CONTRATTO,
           ESERCIZIO_INC, PG_INCARICO, IDENTIFICATIVO_SDI
    FROM (
        SELECT f.esercizio esercizio_documento,f.CD_UO_ORIGINE uo_documento, f.PG_FATTURA_PASSIVA numero_documento,
              'FATTURA_P' tipo_documento,
              decode(f.ti_fattura,'C',(fr.im_imponibile+fr.im_iva)*-1,(fr.im_imponibile+fr.im_iva)) importo_documento,
              nvl(f.dt_scadenza,f.dt_fattura_fornitore+30) data_scadenza, trunc(dt_trasmissione) data_trasmissione,
              det.data_ricezione, f.dt_registrazione DATA_REGISTRAZIONE, f.dt_protocollo_liq DATA_LIQUIDAZIONE,
              TRUNC ((m.dt_emissione) - nvl(f.data_protocollo,f.dt_fattura_fornitore) - (m.dt_emissione - nvl(f.dt_inizio_sospensione, m.dt_emissione))) DIFFERENZA_GIORNI,
              (mr.im_mandato_riga) importo_pagato,
              ((TRUNC (m.dt_emissione) - nvl(f.data_protocollo,f.dt_fattura_fornitore) - (m.dt_emissione - nvl(f.dt_inizio_sospensione, m.dt_emissione)))* mr.im_mandato_riga) importo_pesato,
              a.cd_anag, f.cd_terzo, os.esercizio esercizio_obbligazione, os.cd_cds cd_cds_obbligazione, os.pg_obbligazione, os.pg_obbligazione_scadenzario,
              os.esercizio_originale esercizio_ori_obbligazione,
              c.esercizio esercizio_contratto, c.pg_contratto, null esercizio_inc, null pg_incarico, f.identificativo_sdi
             FROM contratto c,
                  obbligazione o,
                  obbligazione_scadenzario os,
                  fattura_passiva f,
                  fattura_passiva_riga fr,
                  mandato_riga mr,
                  mandato m,
                  anagrafico a,
                  terzo t,
                  documento_ele_trasmissione det
            WHERE a.cd_anag=t.cd_anag
              AND t.cd_terzo=f.cd_terzo
              AND c.esercizio(+) = o.esercizio_contratto
              AND c.stato(+) = o.stato_contratto
              AND c.pg_contratto(+) = o.pg_contratto
              AND o.esercizio = os.esercizio
              AND o.esercizio_originale = os.esercizio_originale
              AND o.cd_cds = os.cd_cds
              AND o.pg_obbligazione = os.pg_obbligazione
              AND fr.esercizio = f.esercizio
              AND fr.cd_cds = f.cd_cds
              AND fr.cd_unita_organizzativa = f.cd_unita_organizzativa
              AND fr.pg_fattura_passiva = f.pg_fattura_passiva
              and f.fl_fattura_compenso = 'N'
              AND os.esercizio = fr.esercizio_obbligazione
              AND os.esercizio_originale = fr.esercizio_ori_obbligazione
              AND os.cd_cds = fr.cd_cds_obbligazione
              AND os.pg_obbligazione = fr.pg_obbligazione
              AND os.pg_obbligazione_scadenzario = fr.pg_obbligazione_scadenzario
              AND os.esercizio = mr.esercizio_obbligazione
              AND os.cd_cds = mr.cd_cds
              AND os.esercizio_originale = mr.esercizio_ori_obbligazione
              AND os.pg_obbligazione = mr.pg_obbligazione
              AND os.pg_obbligazione_scadenzario = mr.pg_obbligazione_scadenzario
              AND mr.cd_cds = m.cd_cds
              AND mr.esercizio = m.esercizio
              AND mr.pg_mandato = m.pg_mandato
              AND m.stato != 'A'
              AND f.stato_cofi != 'A'
              AND m.ti_mandato in( 'P')
              and mr.cd_cds_doc_amm=f.cd_cds_origine
              and mr.CD_UO_DOC_AMM=f.cd_uo_origine
              and mr.esercizio_doc_amm=f.esercizio
              and mr.pg_doc_amm = f.pg_fattura_passiva
              AND mr.cd_tipo_documento_amm = 'FATTURA_P'
              AND m.dt_trasmissione IS NOT NULL
              and STATO_PAGAMENTO_FONDO_ECO ='N'
              --AND decode(f.ti_fattura,'F',NVL (f.stato_liquidazione, 'LIQ'),'LIQ') = 'LIQ'
              AND NVL (f.stato_liquidazione, 'LIQ') = 'LIQ'
              AND f.id_paese = det.id_paese(+)
              AND f.id_codice = det.id_codice(+)
              AND f.identificativo_sdi = det.identificativo_sdi(+)
             union all
             -- fatture legati a compensi pagate nel periodo sia con contratto che con incarico
              select comp.esercizio,comp.CD_unita_organizzativa uo,comp.pg_compenso pg_doc,'COMPENSO'  tipo_doc,
                comp.im_totale_compenso tot, comp.dt_scadenza,trunc(m.dt_trasmissione) dt_trasmissione,null,null,null,
                TRUNC ((m.dt_emissione) - nvl(f.data_protocollo,f.dt_fattura_fornitore) - (m.dt_emissione - nvl(f.dt_inizio_sospensione, m.dt_emissione))) DIFFERENZA_GIORNI,
                (mr.im_mandato_riga) tot_pagato,
                ((TRUNC (m.dt_emissione) - nvl(f.data_protocollo,f.dt_fattura_fornitore) - (m.dt_emissione - nvl(f.dt_inizio_sospensione, m.dt_emissione)))* mr.im_mandato_riga) tot_pesato,
                a.cd_anag,f.cd_terzo,os.esercizio esercizio_obb,os.cd_cds,os.pg_obbligazione,os.pg_obbligazione_scadenzario,os.esercizio_originale,
                c.esercizio esercizio_contratto,c.pg_contratto,i.esercizio esercizio_inc,i.pg_repertorio pg_incarico, f.identificativo_sdi
                 FROM contratto c,
                      obbligazione o,
                      obbligazione_scadenzario os,
                      fattura_passiva f,fattura_passiva_riga fr,
                      mandato_riga mr,
                      mandato m,anagrafico a, terzo t,
                      INCARICHI_REPERTORIO I,
                      compenso comp
                WHERE a.cd_anag = t.cd_anag and t.cd_terzo= f.cd_terzo and
                      c.esercizio(+) = o.esercizio_contratto
                  AND c.stato(+) = o.stato_contratto
                  AND c.pg_contratto(+) = o.pg_contratto
                  AND (O.ESERCIZIO_REP = I.ESERCIZIO(+)
                  AND O.PG_REPERTORIO  =I.PG_REPERTORIO(+)
                  AND comp.CD_TIPO_RAPPORTO IN('OCCA','PROF'))
                  AND o.esercizio = os.esercizio
                  AND o.esercizio_originale = os.esercizio_originale
                  AND o.cd_cds = os.cd_cds
                  AND o.pg_obbligazione = os.pg_obbligazione
                  and comp.fl_generata_fattura = 'Y'
                  AND os.esercizio = comp.esercizio_obbligazione
                  AND os.esercizio_originale = comp.esercizio_ori_obbligazione
                  AND os.cd_cds = comp.cd_cds_obbligazione
                  AND os.pg_obbligazione = comp.pg_obbligazione
                  AND os.pg_obbligazione_scadenzario = comp.pg_obbligazione_scadenzario
                  AND comp.esercizio = f.esercizio_compenso
                  AND comp.cd_cds = f.cds_compenso
                  AND comp.cd_unita_organizzativa = f.uo_compenso
                  AND comp.pg_compenso = f.pg_compenso
                  AND fr.esercizio = f.esercizio
                  AND fr.cd_cds = f.cd_cds
                  AND fr.cd_unita_organizzativa = f.cd_unita_organizzativa
                  AND fr.pg_fattura_passiva = f.pg_fattura_passiva
                  and f.fl_fattura_compenso = 'Y'
                  AND os.esercizio = mr.esercizio_obbligazione
                  AND os.cd_cds = mr.cd_cds
                  AND os.esercizio_originale = mr.esercizio_ori_obbligazione
                  AND os.pg_obbligazione = mr.pg_obbligazione
                  AND os.pg_obbligazione_scadenzario = mr.pg_obbligazione_scadenzario
                  AND mr.cd_cds = m.cd_cds
                  AND mr.esercizio = m.esercizio
                  AND mr.pg_mandato = m.pg_mandato
                  AND m.stato != 'A'
                  AND f.stato_cofi != 'A'
                  AND m.ti_mandato in( 'P')
                  and mr.cd_cds_doc_amm=comp.cd_cds
                  and mr.CD_UO_DOC_AMM=comp.cd_unita_organizzativa
                  and mr.esercizio_doc_amm=comp.esercizio
                  and mr.pg_doc_amm = comp.pg_compenso
                  AND mr.cd_tipo_documento_amm = 'COMPENSO'
                  AND comp.dt_scadenza IS NOT NULL
                  AND m.dt_trasmissione IS NOT NULL
                  and comp.STATO_PAGAMENTO_FONDO_ECO ='N'
                  AND NVL (comp.stato_liquidazione, 'LIQ') = 'LIQ'
    ) GROUP BY ESERCIZIO_DOCUMENTO, UO_DOCUMENTO, NUMERO_DOCUMENTO, TIPO_DOCUMENTO,
              DATA_SCADENZA, DATA_TRASMISSIONE, DATA_RICEZIONE, DATA_REGISTRAZIONE, DATA_LIQUIDAZIONE,
              CD_ANAG, CD_TERZO, ESERCIZIO_OBBLIGAZIONE, CD_CDS_OBBLIGAZIONE,
              PG_OBBLIGAZIONE, PG_OBBLIGAZIONE_SCADENZARIO, ESERCIZIO_ORI_OBBLIGAZIONE, ESERCIZIO_CONTRATTO, PG_CONTRATTO,
              ESERCIZIO_INC, PG_INCARICO, IDENTIFICATIVO_SDI
)
/
