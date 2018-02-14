--------------------------------------------------------
--  DDL for View V_INTRA12
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_INTRA12" ("CD_TIPO_SEZIONALE", "DS_TIPO_SEZIONALE", "ESERCIZIO", "MESE", "IMPONIBILE", "IVA", "CD_BENE_SERVIZIO", "FL_INTRA_UE", "FL_EXTRA_UE") AS 
  SELECT   f.cd_tipo_sezionale, t.ds_tipo_sezionale, f.esercizio,
            TO_NUMBER (TO_CHAR (r.data_inizio, 'mm')) mese,
            SUM (NVL (DECODE (ti_fattura, 'C', -im_imponibile, im_imponibile),
                      0
                     )
                ) imp,
            SUM (NVL (DECODE (ti_fattura, 'C', -im_iva, im_iva), 0)) iva,
            DECODE (b.ti_bene_servizio, 'B', 'BENI', riga.cd_bene_servizio),
            DECODE (f.fl_merce_intra_ue,
                    'Y', 'Y',
                    f.fl_intra_ue
                   ) fl_intra_ue, 
                   decode(f.fl_san_marino_senza_iva,'Y','Y',f.fl_extra_ue) fl_extra_ue
       FROM report_dettaglio r,
            fattura_passiva f,
            fattura_passiva_riga riga,
            bene_servizio b,
            tipo_sezionale t
      WHERE t.ti_acquisti_vendite = 'A'
        AND t.ti_istituz_commerc = 'I'
        AND t.cd_tipo_sezionale = f.cd_tipo_sezionale
        AND b.cd_bene_servizio = riga.cd_bene_servizio
        AND (b.ti_bene_servizio = 'B' OR b.fl_autofattura = 'Y')
        AND b.fl_valido = 'Y'
        AND t.ti_bene_servizio != '*'
        AND f.esercizio = riga.esercizio
        AND f.cd_cds = riga.cd_cds
        AND f.cd_unita_organizzativa = riga.cd_unita_organizzativa
        AND f.pg_fattura_passiva = riga.pg_fattura_passiva
        AND r.ti_documento = 'FATTURA_P'
        AND r.esercizio = f.esercizio
        AND r.cd_cds_altro = f.cd_cds
        AND r.cd_uo_altro = f.cd_unita_organizzativa
        AND r.pg_documento = f.pg_fattura_passiva
        AND f.esercizio > 2009
   GROUP BY f.cd_tipo_sezionale,
            f.esercizio,
            TO_CHAR (r.data_inizio, 'mm'),
            DECODE (b.ti_bene_servizio, 'B', 'BENI', riga.cd_bene_servizio),
            DECODE (f.fl_merce_intra_ue, 'Y', 'Y', f.fl_intra_ue),
            decode(f.fl_san_marino_senza_ivA,'Y','Y',f.fl_extra_ue),
            t.ds_tipo_sezionale ;
