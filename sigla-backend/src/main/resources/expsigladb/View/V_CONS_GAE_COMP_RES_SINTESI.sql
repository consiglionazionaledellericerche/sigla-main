--------------------------------------------------------
--  DDL for View V_CONS_GAE_COMP_RES_SINTESI
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_CONS_GAE_COMP_RES_SINTESI" ("ESERCIZIO", "CDS", "CDR", "DS_CDR", "LDA", "DS_LDA", "CD_RESPONSABILE_TERZO", "DENOMINAZIONE_SEDE", "CD_ELEMENTO_VOCE", "DS_ELEMENTO_VOCE", "CD_CDS_OBB", "ESERCIZIO_OBBLIGAZIONE", "ESERCIZIO_ORIGINALE", "PG_OBBLIGAZIONE", "PG_OBBLIGAZIONE_SCADENZARIO", "DS_SCADENZA", "IM_IMPEGNI", "CD_CDS_MAN", "PG_MANDATO", "DT_EMISSIONE", "DT_TRASMISSIONE", "DT_PAGAMENTO", "DS_MANDATO", "IM_MANDATI") AS 
  SELECT
            riepilogo.esercizio,
            riepilogo.cd_cds,
            riepilogo.cd_centro_responsabilita, ds_cdr,
            riepilogo.cd_linea_attivita, ds_lda, cd_responsabile_terzo,
            denominazione_sede, cd_elemento_voce, ds_elemento_voce,
            cd_cds_obb, esercizio_obbligazione, esercizio_originale,
            pg_obbligazione, pg_obbligazione_scadenzario, ds_scadenza,
            SUM (im_impegni), cd_cds_man, riepilogo.pg_mandato, dt_emissione,
            TRUNC (dt_trasmissione), dt_pagamento, riepilogo.ds_mandato,
            SUM (im_mandati)
       FROM
-- COMPETENZA
            (SELECT esercizio, cd_centro_responsabilita, cd_linea_attivita,cd_cds ,
                    ds_linea_attivita ds_lda, cd_elemento_voce,
                    ds_elemento_voce, cd_cds_obb,
                    esercizio esercizio_obbligazione,
                    esercizio esercizio_originale, pg_obbligazione,
                    pg_obbligazione_scadenzario, ds_scadenza,
                    impegni_comp im_impegni, cd_cds_man, pg_mandato,
                    ds_mandato, mandati_comp im_mandati
               FROM v_cons_gae_competenza_spe
              WHERE cd_cds_obb IS NOT NULL
             UNION
             -- RESIDUI
             SELECT esercizio, cd_centro_responsabilita, cd_linea_attivita,cd_cds,
                    ds_linea_attivita, cd_elemento_voce, ds_elemento_voce,
                    cd_cds_obb, esercizio esercizio_obbligazione,
                    esercizio_res esercizio_originale, pg_obbligazione,
                    pg_obbligazione_scadenzario, ds_scadenza,
                    NVL (im_obbl_res_imp, 0) + NVL (im_obbl_res_pro, 0),
                    cd_cds_man, pg_mandato, ds_mandato,
                      NVL (im_mandati_reversali_pro, 0)
                    + NVL (im_mandati_reversali_imp, 0)
               FROM v_cons_gae_residui_spe
              WHERE pg_obbligazione_scadenzario IS NOT NULL) riepilogo,
            mandato,
            cdr,
            linea_attivita,
            terzo
      WHERE riepilogo.cd_cds_man = mandato.cd_cds(+)
        AND riepilogo.esercizio = mandato.esercizio(+)
        AND riepilogo.pg_mandato = mandato.pg_mandato(+)
        AND linea_attivita.cd_responsabile_terzo = terzo.cd_terzo(+)
        AND riepilogo.cd_centro_responsabilita =
                                       linea_attivita.cd_centro_responsabilita
        AND riepilogo.cd_linea_attivita = linea_attivita.cd_linea_attivita
        AND riepilogo.cd_centro_responsabilita = cdr.cd_centro_responsabilita
   GROUP BY riepilogo.esercizio,
            riepilogo.cd_cds,
            riepilogo.cd_centro_responsabilita,
            ds_cdr,
            riepilogo.cd_linea_attivita,
            ds_lda,
            linea_attivita.cd_responsabile_terzo,
            denominazione_sede,
            cd_elemento_voce,
            ds_elemento_voce,
            cd_cds_obb,
            riepilogo.esercizio_obbligazione,
            riepilogo.esercizio_originale,
            pg_obbligazione,
            pg_obbligazione_scadenzario,
            ds_scadenza,
            cd_cds_man,
            riepilogo.pg_mandato,
            dt_emissione,
            TRUNC (dt_trasmissione),
            dt_pagamento,
            riepilogo.ds_mandato;
