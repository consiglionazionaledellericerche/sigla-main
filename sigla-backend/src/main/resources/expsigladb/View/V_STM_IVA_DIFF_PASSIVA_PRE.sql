--------------------------------------------------------
--  DDL for View V_STM_IVA_DIFF_PASSIVA_PRE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_STM_IVA_DIFF_PASSIVA_PRE" ("ESERCIZIO", "CD_CDS", "CD_UNITA_ORGANIZZATIVA", "CD_CDS_ORIGINE", "CD_UO_ORIGINE", "CD_TIPO_SEZIONALE", "TI_FATTURA", "DATA_REGISTRAZIONE", "NUMERO_PROGRESSIVO", "DATA_EMISSIONE", "NUMERO_FATTURA", "PROTOCOLLO_IVA", "PROTOCOLLO_IVA_GENERALE", "COMM_IST_TESTATA", "CODICE_ANAGRAFICO", "RAGIONE_SOCIALE", "IMPONIBILE_DETTAGLIO", "IVA_DETTAGLIO", "IVA_INDETRAIBILE_DETTAGLIO", "TOTALE_DETTAGLIO", "COMM_IST_DETTAGLIO", "CODICE_IVA", "PERCENTUALE_IVA", "DESCRIZIONE_IVA", "FL_IVA_DETRAIBILE", "PERCENTUALE_IVA_DETRAIBILE", "INTRA_UE", "BOLLA_DOGANALE", "SPEDIZIONIERE", "ESIGIBILITA_DIFF", "DATA_ESIGIBILITA_DIFF", "PROGRESSIVO_RIGA") AS 
  SELECT
-- =================================================================================================
--
-- Date: 18/12/2002
-- Version: 3.0
--
-- Vista (di preparazione) per la stampa dell'IVA passiva ad esigibilità differira
--
-- History:
--
-- Date: 10/06/2002
-- Version: 1.0
-- Creazione
--
-- Date: 18/12/2002
-- Version: 3.0
--
-- Revisione vista per nuova gestione IVA, corretti errori in estrazione
--
-- Body:
--
-- =================================================================================================
       A.esercizio,
       A.cd_cds,
       A.cd_unita_organizzativa,
       A.cd_cds_origine,
       A.cd_uo_origine,
       A.cd_tipo_sezionale,
       A.ti_fattura,
       TRUNC(A.dt_registrazione),
       A.pg_fattura_passiva,
       TRUNC(A.dt_fattura_fornitore),
       A.nr_fattura_fornitore,
       A.protocollo_iva,
       A.protocollo_iva_generale,
       A.ti_istituz_commerc,
       A.cd_terzo,
       LTRIM(RTRIM(A.ragione_sociale || ' ' || A.cognome || ' ' || A.nome)),
       DECODE(A.ti_fattura, 'C', (SUM(B.im_imponibile) * -1), SUM(B.im_imponibile)),
       DECODE(A.ti_fattura, 'C', (SUM(B.im_iva) * -1), SUM(B.im_iva)),
       0,
       DECODE(A.ti_fattura, 'C', (SUM(B.im_imponibile + B.im_iva) * -1), SUM(B.im_imponibile + B.im_iva)),
       B.ti_istituz_commerc,
       B.cd_voce_iva,
       C.percentuale,
       C.ds_voce_iva,
       C.fl_detraibile,
       DECODE(C.fl_detraibile, 'N', 0, C.percentuale_detraibilita),
       A.fl_intra_ue,
       A.fl_bolla_doganale,
       A.fl_spedizioniere,
       fl_liquidazione_differita,
       data_esigibilita_iva,
       PROGRESSIVO_RIGA
FROM   FATTURA_PASSIVA A,
       FATTURA_PASSIVA_RIGA B,
       VOCE_IVA C
WHERE  (A.ti_istituz_commerc = 'C' OR
        A.ti_istituz_commerc = 'P') AND
       B.cd_cds = A.cd_cds AND
       B.cd_unita_organizzativa = A.cd_unita_organizzativa AND
       B.esercizio = A.esercizio AND
       B.pg_fattura_passiva = A.pg_fattura_passiva AND
       B.ti_istituz_commerc = 'C' AND
       C.cd_voce_iva = B.cd_voce_iva
GROUP BY A.esercizio,
         A.cd_cds,
         A.cd_unita_organizzativa,
         A.cd_cds_origine,
         A.cd_uo_origine,
         A.cd_tipo_sezionale,
         A.ti_fattura,
         TRUNC(A.dt_registrazione),
         A.pg_fattura_passiva,
         TRUNC(A.dt_fattura_fornitore),
         A.nr_fattura_fornitore,
         A.protocollo_iva,
         A.protocollo_iva_generale,
         A.ti_istituz_commerc,
         A.cd_terzo,
         LTRIM(RTRIM(A.ragione_sociale || ' ' || A.cognome || ' ' || A.nome)),
         0,
         B.ti_istituz_commerc,
         B.cd_voce_iva,
         C.percentuale,
         C.ds_voce_iva,
         C.fl_detraibile,
         DECODE(C.fl_detraibile, 'N', 0, C.percentuale_detraibilita),
         A.fl_intra_ue,
         A.fl_bolla_doganale,
         A.fl_spedizioniere,
         fl_liquidazione_differita,
         data_esigibilita_iva,
         PROGRESSIVO_RIGA;
