--------------------------------------------------------
--  DDL for View V_MANDATO_NON_ASSOCIABILE_REV
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_MANDATO_NON_ASSOCIABILE_REV" ("CD_CDS", "ESERCIZIO", "PG_MANDATO") AS 
  SELECT
--==============================================================================
--
-- Date: 26/05/2003
-- Version: 1.1
--
-- Vista estrazione mandati non associabili a reversali manualmente
--  Mandati di reintegro del fondo economale
--  Mandati di accreditamento e regolarizzazione
--  Mandati su fatture passive con recupero iva (reversale)
--
-- History:
-- Date: 03/02/2003
-- Version: 1.0
-- Creazione
--
-- Date: 26/05/2003
-- Version: 1.1
-- Aggiunto l'ulteriore controllo che il mandato non si a di accreditamento o regolarizzazione o
-- recupero iva
--
-- Body:
--
--==============================================================================
          m.cd_cds, m.esercizio, m.pg_mandato
     FROM mandato m
    WHERE
          -- Mandati di reintegro del fondo economale
          EXISTS (
             SELECT 1
               FROM fondo_spesa
              WHERE cd_cds_mandato = m.cd_cds
                AND esercizio_mandato = m.esercizio
                AND pg_mandato = m.pg_mandato)
       -- Mandati di accreditamento e regolarizzazione
       OR m.ti_mandato IN ('A', 'R')
       -- Mandati su fatture passive con recupero iva (reversale)
       OR EXISTS (
             SELECT 1
               FROM mandato_riga a,
                    fattura_passiva_riga b,
                    fattura_passiva c,
                    tipo_sezionale t
              WHERE c.cd_tipo_sezionale = t.cd_tipo_sezionale
                AND a.cd_cds = m.cd_cds
                AND a.esercizio = m.esercizio
                AND a.pg_mandato = m.pg_mandato
                AND a.cd_tipo_documento_amm = 'FATTURA_P'
                AND b.cd_cds = a.cd_cds_doc_amm
                AND b.esercizio = a.esercizio_doc_amm
                AND b.cd_unita_organizzativa = a.cd_uo_doc_amm
                AND b.pg_fattura_passiva = a.pg_doc_amm
                AND c.cd_cds = b.cd_cds
                AND c.esercizio = b.esercizio
                AND c.cd_unita_organizzativa = b.cd_unita_organizzativa
                AND c.pg_fattura_passiva = b.pg_fattura_passiva
                AND SUBSTR
                       (getflfaireversale (c.ti_fattura,
                                           c.ti_istituz_commerc,
                                           c.ti_bene_servizio,
                                           c.fl_san_marino_senza_iva,
                                           DECODE (c.fl_merce_intra_ue,
                                                   'Y', 'Y',
                                                   c.fl_intra_ue
                                                  ),
                                           c.fl_split_payment,
                                           DECODE (c.ti_bene_servizio,
                                                   'B', t.ti_bene_servizio,
                                                   t.fl_servizi_non_residenti
                                                  )
                                          ),
                        1,
                        1
                       ) = 'Y');

   COMMENT ON TABLE "V_MANDATO_NON_ASSOCIABILE_REV"  IS 'Vista estrazione mandati non associabili a reversali manualmente';
