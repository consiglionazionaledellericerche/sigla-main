--------------------------------------------------------
--  DDL for View V_LIQUID_IVA_ACQUISTI_ESIG
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_LIQUID_IVA_ACQUISTI_ESIG" ("CD_CDS", "CD_UNITA_ORGANIZZATIVA", "ESERCIZIO", "NUMERO_PROGRESSIVO", "DATA_REGISTRAZIONE", "DATA_EMISSIONE", "SEZIONE_LIQUIDAZIONE", "CD_CDS_ORIGINE", "CD_UO_ORIGINE", "CD_TIPO_SEZIONALE", "TI_FATTURA", "TI_ISTITUZ_COMMERC", "FL_IVA_DETRAIBILE", "PERCENTUALE_IVA_DETRAIBILE", "IVA_DETTAGLIO", "IVA_INDETRAIBILE_DETTAGLIO", "ESIGIBILITA_DIFF", "DATA_ESIGIBILITA", "PG_RIGA_DOCUMENTO") AS 
  SELECT
       A.cd_cds,
       A.cd_unita_organizzativa,
       A.esercizio,
       A.pg_fattura_passiva,
       A.dt_registrazione,
       A.dt_registrazione,
       'AC',
       A.cd_cds_origine,
       A.cd_uo_origine,
       A.cd_tipo_sezionale,
       A.ti_fattura,
       'C',
       'Y',
       100,
       DECODE(A.ti_fattura, 'C', (SUM(B.im_iva) * -1), SUM(B.im_iva)),
       0,
       A.fl_liquidazione_differita,
       b.data_esigibilita_iva,
       B.PROGRESSIVO_RIGA
FROM   FATTURA_PASSIVA A,
       FATTURA_PASSIVA_RIGA B
WHERE  A.fl_liquidazione_differita = 'Y' AND
       B.cd_cds = A.cd_cds AND
       B.cd_unita_organizzativa = A.cd_unita_organizzativa AND
       B.esercizio = A.esercizio AND
       B.pg_fattura_PASSIVA = A.pg_fattura_PASSIVA AND
       NOT EXISTS
           (SELECT 1
            FROM   REPORT_DETTAGLIO D
            WHERE  D.cd_cds = A.cd_cds_origine AND
                   D.cd_unita_organizzativa = A.cd_uo_origine AND
                   D.esercizio = A.esercizio AND
                   D.tipo_report = 'LIQUIDAZIONE_ACQUISTI' AND
                   D.pg_documento = A.pg_fattura_passiva And
                   D.pg_riga_documento =  B.PROGRESSIVO_RIGA)
GROUP BY A.cd_cds,
         A.cd_unita_organizzativa,
         A.esercizio,
         A.pg_fattura_passiva,
         A.dt_registrazione,
         A.dt_registrazione,
         'AC',
         A.cd_cds_origine,
         A.cd_uo_origine,
         A.cd_tipo_sezionale,
         A.ti_fattura,
         'C',
         'Y',
         100,
         0,
         A.fl_liquidazione_differita,
         b.data_esigibilita_iva,
         B.PROGRESSIVO_RIGA;
