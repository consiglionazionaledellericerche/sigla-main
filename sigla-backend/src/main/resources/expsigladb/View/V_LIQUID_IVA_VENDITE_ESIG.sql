--------------------------------------------------------
--  DDL for View V_LIQUID_IVA_VENDITE_ESIG
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_LIQUID_IVA_VENDITE_ESIG" ("CD_CDS", "CD_UNITA_ORGANIZZATIVA", "ESERCIZIO", "NUMERO_PROGRESSIVO", "DATA_REGISTRAZIONE", "DATA_EMISSIONE", "SEZIONE_LIQUIDAZIONE", "CD_CDS_ORIGINE", "CD_UO_ORIGINE", "CD_TIPO_SEZIONALE", "TI_FATTURA", "TI_ISTITUZ_COMMERC", "FL_IVA_DETRAIBILE", "PERCENTUALE_IVA_DETRAIBILE", "IVA_DETTAGLIO", "IVA_INDETRAIBILE_DETTAGLIO", "ESIGIBILITA_DIFF", "DATA_ESIGIBILITA", "PG_RIGA_DOCUMENTO") AS 
  SELECT
-- =================================================================================================
--
-- Date: 14/04/2003
-- Version: 1.2
--
-- Vista per il calcolo della liquidazione iva  esigibilita differita per vendite
--
-- History:
--
-- Date: 10/06/2002
-- Version: 1.0
--
-- Creazione vista
--
-- Date: 10/12/2002
-- Version: 1.1
--
-- Revisione per modifica alla tabella REPORT_DETTAGLIO
--
-- Date: 14/04/2003
-- Version: 1.2
--
-- Fix errore CINECA (mail). Errata estrazione della data di esigibilità iva
--
-- Body:
--
-- =================================================================================================
       A.cd_cds,
       A.cd_unita_organizzativa,
       A.esercizio,
       A.pg_fattura_attiva,
       A.dt_registrazione,
       A.dt_emissione,
       'VE',
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
       B.data_esigibilita_iva,
       B.PROGRESSIVO_RIGA
FROM   FATTURA_ATTIVA A,
       FATTURA_ATTIVA_RIGA B,
       CONFIGURAZIONE_CNR Cnr
WHERE  A.fl_liquidazione_differita = 'Y' AND
       B.cd_cds = A.cd_cds AND
       B.cd_unita_organizzativa = A.cd_unita_organizzativa AND
       B.esercizio = A.esercizio AND
       B.pg_fattura_attiva = A.pg_fattura_attiva AND
       Cnr.ESERCIZIO = 0 AND
       Cnr.cd_chiave_primaria = 'SPLIT_PAYMENT'
       AND Cnr.cd_chiave_secondaria = 'ATTIVA'
       AND Cnr.cd_unita_funzionale = '*'
       AND a.dt_emissione <   NVL (Cnr.dt01, a.dt_emissione)
       and NOT EXISTS
           (SELECT 1
            FROM   REPORT_DETTAGLIO D
            WHERE  D.cd_cds = A.cd_cds_origine AND
                   D.cd_unita_organizzativa = A.cd_uo_origine AND
                   D.esercizio = A.esercizio AND
                   D.tipo_report = 'LIQUIDAZIONE_VENDITE' AND
                   D.pg_documento = A.pg_fattura_attiva And
                   D.pg_riga_documento =  B.PROGRESSIVO_RIGA)
Group BY A.cd_cds,
         A.cd_unita_organizzativa,
         A.esercizio,
         A.pg_fattura_attiva,
         A.dt_registrazione,
         A.dt_emissione,
         'VE',
         A.cd_cds_origine,
         A.cd_uo_origine,
         A.cd_tipo_sezionale,
         A.ti_fattura,
         'C',
         'Y',
         100,
         0,
         A.fl_liquidazione_differita,
         B.data_esigibilita_iva,
         B.PROGRESSIVO_RIGA;
