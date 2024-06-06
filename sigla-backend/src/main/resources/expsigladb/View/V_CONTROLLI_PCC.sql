--------------------------------------------------------
--  DDL for View V_CONTROLLI_PCC
--------------------------------------------------------
CREATE OR REPLACE FORCE VIEW "V_CONTROLLI_PCC" (
    "IDENTIFICATIVO_SDI",
    "NUMERO_DOCUMENTO",
    "DATA_DOCUMENTO",
    "CODICE_FISCALE",
    "CODICE_DESTINATARIO",
    "DATA_RICEZIONE",
    "DATA_SCADENZA",
    "STATO_DOCUMENTO",
    "IMPORTO_DOCUMENTO",
    "IMPONIBILE",
    "IMPOSTA",
    "TIPO_DOCUMENTO",
    "ESERCIZIO",
    "CD_UNITA_ORGANIZZATIVA",
    "PG_FATTURA_PASSIVA",
    "DT_REGISTRAZIONE",
    "STATO_LIQUIDAZIONE",
    "CAUSALE",
    "DT_INIZIO_SOSPENSIONE",
    "DT_EMISSIONE_MAN",
    "IM_MANDATO",
    "CD_TIPO_CONTRATTO",
    "FL_IRREGISTRABILE",
    "CD_UO_CUU",
    "FL_DA_COMPENSO"
) AS SELECT
    "IDENTIFICATIVO_SDI",
    "NUMERO_DOCUMENTO",
    "DATA_DOCUMENTO",
    "CODICE_FISCALE",
    "CODICE_DESTINATARIO",
    "DATA_RICEZIONE",
    "DATA_SCADENZA",
    "STATO_DOCUMENTO",
    DECODE(TIPO_DOCUMENTO, 'TD04', -ABS(IMPORTO_DOCUMENTO), IMPORTO_DOCUMENTO) "IMPORTO_DOCUMENTO",
    DECODE(TIPO_DOCUMENTO, 'TD04', -ABS(IMPONIBILE), IMPONIBILE) "IMPONIBILE",
    DECODE(TIPO_DOCUMENTO, 'TD04', -ABS(IMPOSTA), IMPOSTA) "IMPOSTA",
    "TIPO_DOCUMENTO",
    "ESERCIZIO",
    "CD_UNITA_ORGANIZZATIVA",
    "PG_FATTURA_PASSIVA",
    "DT_REGISTRAZIONE",
    "STATO_LIQUIDAZIONE",
    "CAUSALE",
    "DT_INIZIO_SOSPENSIONE",
    "DT_EMISSIONE_MAN",
    "IM_MANDATO",
    "CD_TIPO_CONTRATTO",
    "FL_IRREGISTRABILE",
    "CD_UO_CUU",
    "FL_DA_COMPENSO" FROM (
        SELECT det.IDENTIFICATIVO_SDI, det.NUMERO_DOCUMENTO, det.DATA_DOCUMENTO, nvl(det2.PRESTATORE_CODICEFISCALE,PRESTATORE_CODICE) CODICE_FISCALE, det2.CODICE_DESTINATARIO, det2.DATA_RICEZIONE,
        nvl(fp.DT_SCADENZA, det2.DATA_RICEZIONE+30) DATA_SCADENZA,
        det.STATO_DOCUMENTO,det.IMPORTO_DOCUMENTO,

        (SELECT SUM(IMPONIBILE_IMPORTO) FROM DOCUMENTO_ELE_IVA dei
            WHERE dei.ID_PAESE = det.ID_PAESE
            AND dei.ID_CODICE = det.ID_CODICE
            AND dei.IDENTIFICATIVO_SDI = det.IDENTIFICATIVO_SDI
            AND dei.PROGRESSIVO = det.PROGRESSIVO) IMPONIBILE,

        (SELECT SUM(IMPOSTA) FROM DOCUMENTO_ELE_IVA dei
            WHERE dei.ID_PAESE = det.ID_PAESE
            AND dei.ID_CODICE = det.ID_CODICE
            AND dei.IDENTIFICATIVO_SDI = det.IDENTIFICATIVO_SDI
            AND dei.PROGRESSIVO = det.PROGRESSIVO) IMPOSTA,

        det.TIPO_DOCUMENTO,
        fp.ESERCIZIO,
        fp.CD_UNITA_ORGANIZZATIVA,
        fp.PG_FATTURA_PASSIVA,
        fp.DT_REGISTRAZIONE,
        fp.STATO_LIQUIDAZIONE,
        fp.CAUSALE,
        fp.DT_INIZIO_SOSPENSIONE,

        (SELECT min(m.DT_EMISSIONE) FROM FATTURA_PASSIVA_RIGA fpr, OBBLIGAZIONE_SCADENZARIO os, mandato_riga mr, mandato m
            WHERE fp.CD_CDS = fpr.CD_CDS(+)
            AND fp.CD_UNITA_ORGANIZZATIVA = fpr.CD_UNITA_ORGANIZZATIVA(+)
            AND fp.ESERCIZIO = fpr.ESERCIZIO(+)
            AND fp.PG_FATTURA_PASSIVA = fpr.PG_FATTURA_PASSIVA(+)
            AND fpr.esercizio_obbligazione = os.esercizio(+)
            AND fpr.esercizio_ori_obbligazione = os.esercizio_originale(+)
            AND fpr.cd_cds_obbligazione = os.cd_cds(+)
            AND fpr.pg_obbligazione = os.pg_obbligazione(+)
            AND fpr.pg_obbligazione_scadenzario = os.pg_obbligazione_scadenzario(+)
            AND os.esercizio = mr.esercizio_obbligazione
            AND os.cd_cds = mr.cd_cds
            AND os.esercizio_originale = mr.esercizio_ori_obbligazione
            AND os.pg_obbligazione = mr.pg_obbligazione
            AND os.pg_obbligazione_scadenzario = mr.pg_obbligazione_scadenzario
            AND mr.cd_cds = m.cd_cds
            AND mr.esercizio = m.esercizio
            AND mr.pg_mandato = m.pg_mandato
        ) DT_EMISSIONE_MAN,

        (SELECT SUM(fpr.IM_TOTALE_DIVISA) FROM FATTURA_PASSIVA_RIGA fpr, OBBLIGAZIONE_SCADENZARIO os, mandato_riga mr
            WHERE fp.CD_CDS = fpr.CD_CDS(+)
            AND fp.CD_UNITA_ORGANIZZATIVA = fpr.CD_UNITA_ORGANIZZATIVA(+)
            AND fp.ESERCIZIO = fpr.ESERCIZIO(+)
            AND fp.PG_FATTURA_PASSIVA = fpr.PG_FATTURA_PASSIVA(+)
            AND fpr.esercizio_obbligazione = os.esercizio(+)
            AND fpr.esercizio_ori_obbligazione = os.esercizio_originale(+)
            AND fpr.cd_cds_obbligazione = os.cd_cds(+)
            AND fpr.pg_obbligazione = os.pg_obbligazione(+)
            AND fpr.pg_obbligazione_scadenzario = os.pg_obbligazione_scadenzario(+)
            AND os.esercizio = mr.esercizio_obbligazione
            AND os.cd_cds = mr.cd_cds
            AND os.esercizio_originale = mr.esercizio_ori_obbligazione
            AND os.pg_obbligazione = mr.pg_obbligazione
            AND os.pg_obbligazione_scadenzario = mr.pg_obbligazione_scadenzario
        ) IM_MANDATO,

        (SELECT min(c.CD_TIPO_CONTRATTO) FROM FATTURA_PASSIVA_RIGA fpr, OBBLIGAZIONE o, OBBLIGAZIONE_SCADENZARIO os, CONTRATTO c
            WHERE fp.CD_CDS = fpr.CD_CDS(+)
            AND fp.CD_UNITA_ORGANIZZATIVA = fpr.CD_UNITA_ORGANIZZATIVA(+)
            AND fp.ESERCIZIO = fpr.ESERCIZIO(+)
            AND fp.PG_FATTURA_PASSIVA = fpr.PG_FATTURA_PASSIVA(+)
            AND fpr.esercizio_obbligazione = os.esercizio(+)
            AND fpr.esercizio_ori_obbligazione = os.esercizio_originale(+)
            AND fpr.cd_cds_obbligazione = os.cd_cds(+)
            AND fpr.pg_obbligazione = os.pg_obbligazione(+)
            AND fpr.pg_obbligazione_scadenzario = os.pg_obbligazione_scadenzario(+)
            AND o.esercizio = os.esercizio
            AND o.esercizio_originale = os.esercizio_originale
            AND o.cd_cds = os.cd_cds
            AND o.pg_obbligazione = os.pg_obbligazione
            AND c.esercizio = o.esercizio_contratto
            AND c.stato = o.stato_contratto
            AND c.pg_contratto = o.pg_contratto
            ) CD_TIPO_CONTRATTO, det.FL_IRREGISTRABILE,
            (
            SELECT t.CD_UNITA_ORGANIZZATIVA
            FROM TERZO t
            WHERE t.CODICE_UNIVOCO_UFFICIO_IPA = det2.CODICE_DESTINATARIO
            AND DUVA = (SELECT MAX(DUVA) FROM TERZO t2 WHERE t2.CODICE_UNIVOCO_UFFICIO_IPA = det2.CODICE_DESTINATARIO)
            ) CD_UO_CUU,
            DECODE(fp.ESERCIZIO_COMPENSO, null, 'N', 'Y') FL_DA_COMPENSO
        FROM DOCUMENTO_ELE_TESTATA det, DOCUMENTO_ELE_TRASMISSIONE det2, FATTURA_PASSIVA fp
        WHERE det.ID_PAESE = det2.ID_PAESE
        AND det.ID_CODICE = det2.ID_CODICE
        AND det.IDENTIFICATIVO_SDI = det2.IDENTIFICATIVO_SDI
        AND det.ID_PAESE = fp.ID_PAESE(+)
        AND det.ID_CODICE = fp.ID_CODICE(+)
        AND det.IDENTIFICATIVO_SDI = fp.IDENTIFICATIVO_SDI(+)
        AND det.PROGRESSIVO = fp.PROGRESSIVO(+)
    )
/

 COMMENT ON COLUMN "V_CONTROLLI_PCC"."IDENTIFICATIVO_SDI" IS 'Identificativo SDI';
 COMMENT ON COLUMN "V_CONTROLLI_PCC"."NUMERO_DOCUMENTO" IS 'Numero Documento';
 COMMENT ON COLUMN "V_CONTROLLI_PCC"."DATA_DOCUMENTO" IS 'Data Documento';
 COMMENT ON COLUMN "V_CONTROLLI_PCC"."CODICE_FISCALE" IS 'Codice Fiscale';
 COMMENT ON COLUMN "V_CONTROLLI_PCC"."CODICE_DESTINATARIO" IS 'Codice Destinatario';
 COMMENT ON COLUMN "V_CONTROLLI_PCC"."DATA_RICEZIONE" IS 'Data Ricezione';
 COMMENT ON COLUMN "V_CONTROLLI_PCC"."DATA_SCADENZA" IS 'Data Scadenza';
 COMMENT ON COLUMN "V_CONTROLLI_PCC"."STATO_DOCUMENTO" IS 'Stato Documento';
 COMMENT ON COLUMN "V_CONTROLLI_PCC"."IMPORTO_DOCUMENTO" IS 'Importo Documento';
 COMMENT ON COLUMN "V_CONTROLLI_PCC"."IMPONIBILE" IS 'Imponibile';
 COMMENT ON COLUMN "V_CONTROLLI_PCC"."IMPOSTA" IS 'Imposta';
 COMMENT ON COLUMN "V_CONTROLLI_PCC"."TIPO_DOCUMENTO" IS 'Tipo Documento';
 COMMENT ON COLUMN "V_CONTROLLI_PCC"."ESERCIZIO" IS 'Esercizio';
 COMMENT ON COLUMN "V_CONTROLLI_PCC"."CD_UNITA_ORGANIZZATIVA" IS 'Unità Organizzativa';
 COMMENT ON COLUMN "V_CONTROLLI_PCC"."PG_FATTURA_PASSIVA" IS 'Progressivo Fattura Passiva';
 COMMENT ON COLUMN "V_CONTROLLI_PCC"."DT_REGISTRAZIONE" IS 'Data Registrazione';
 COMMENT ON COLUMN "V_CONTROLLI_PCC"."STATO_LIQUIDAZIONE" IS 'Stato Liquidazione';
 COMMENT ON COLUMN "V_CONTROLLI_PCC"."CAUSALE" IS 'Causale';
 COMMENT ON COLUMN "V_CONTROLLI_PCC"."DT_INIZIO_SOSPENSIONE" IS 'Data Inizio Sospensione';
 COMMENT ON COLUMN "V_CONTROLLI_PCC"."DT_EMISSIONE_MAN" IS 'Data Emissione Mandato';
 COMMENT ON COLUMN "V_CONTROLLI_PCC"."IM_MANDATO" IS 'Importo Mandato';
 COMMENT ON COLUMN "V_CONTROLLI_PCC"."CD_TIPO_CONTRATTO" IS 'Codice Contratto';
 COMMENT ON COLUMN "V_CONTROLLI_PCC"."FL_IRREGISTRABILE" IS 'Fattura elettronica non registrabile';
 COMMENT ON COLUMN "V_CONTROLLI_PCC"."CD_UO_CUU" IS 'Unità Organizzativa del CUU';
 COMMENT ON COLUMN "V_CONTROLLI_PCC"."FL_DA_COMPENSO" IS 'Fattura da Compenso';

