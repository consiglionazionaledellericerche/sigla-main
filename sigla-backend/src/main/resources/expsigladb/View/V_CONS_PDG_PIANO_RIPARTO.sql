--------------------------------------------------------
--  DDL for View V_CONS_PDG_PIANO_RIPARTO
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_CONS_PDG_PIANO_RIPARTO" ("ESERCIZIO", "CD_CENTRO_RESPONSABILITA", "DS_CDR", "ID_CLASSIFICAZIONE", "CD_CLASSIFICAZIONE", "DS_CLASSIFICAZIONE", "IMPORTO_ASSEGNATO", "IMPORTO_RIPARTITO", "IMPORTO_DA_RIPARTIRE") AS 
  SELECT   a.esercizio, a.cd_centro_responsabilita, b.ds_cdr,
            a.id_classificazione, c.cd_classificazione, c.ds_classificazione,
            SUM (a.importo_assegnato) importo_assegnato,
            SUM (a.importo_ripartito) importo_ripartito,
              SUM (a.importo_assegnato)
            - SUM (a.importo_ripartito) importo_da_ripartire
       FROM (SELECT esercizio, cd_centro_responsabilita, id_classificazione,
                    im_tot_spese_acc importo_assegnato, 0 importo_ripartito
               FROM pdg_piano_riparto
             UNION ALL
             SELECT pdg_modulo_spese.esercizio, cd_centro_responsabilita,
                    pdg_modulo_spese.id_classificazione, 0 importo_assegnato,
                      NVL (im_spese_gest_accentrata_int, 0)
                    + NVL (im_spese_gest_accentrata_est, 0) importo_ripartito
               FROM pdg_modulo_spese, classificazione_voci
              WHERE pdg_modulo_spese.id_classificazione =
                                       classificazione_voci.id_classificazione
                AND classificazione_voci.fl_piano_riparto = 'Y'
                AND pdg_modulo_spese.id_classificazione NOT IN (
                       SELECT id_classificazione
                         FROM pdg_piano_riparto
                        WHERE pdg_piano_riparto.esercizio =
                                                    pdg_modulo_spese.esercizio
                          AND pdg_piano_riparto.cd_centro_responsabilita =
                                     pdg_modulo_spese.cd_centro_responsabilita)
             UNION ALL
             SELECT esercizio, cd_centro_responsabilita, id_classificazione,
                    NULL importo_assegnato,
                      NVL (im_spese_gest_accentrata_int, 0)
                    + NVL (im_spese_gest_accentrata_est, 0) importo_ripartito
               FROM pdg_modulo_spese
              WHERE id_classificazione IN (
                       SELECT id_classificazione
                         FROM pdg_piano_riparto
                        WHERE pdg_piano_riparto.esercizio =
                                                    pdg_modulo_spese.esercizio
                          AND pdg_piano_riparto.cd_centro_responsabilita =
                                     pdg_modulo_spese.cd_centro_responsabilita)
             UNION ALL
             SELECT a.esercizio, a.cd_centro_responsabilita,
                    TO_NUMBER (b.val01) id_classificazione,
                    NULL importo_assegnato,
                    NVL (a.im_cf_amm_immobili, 0) importo_ripartito
               FROM pdg_modulo_costi a, configurazione_cnr b
              WHERE a.esercizio = b.esercizio
                AND b.cd_chiave_primaria = 'PIANO_RIPARTO'
                AND b.cd_chiave_secondaria = 'IM_CF_AMM_IMMOBILI'
             UNION ALL
             SELECT a.esercizio, a.cd_centro_responsabilita,
                    TO_NUMBER (b.val01) id_classificazione,
                    NULL importo_assegnato,
                    NVL (a.im_cf_amm_attrezz, 0) importo_ripartito
               FROM pdg_modulo_costi a, configurazione_cnr b
              WHERE a.esercizio = b.esercizio
                AND b.cd_chiave_primaria = 'PIANO_RIPARTO'
                AND b.cd_chiave_secondaria = 'IM_CF_AMM_ATTREZZ'
             UNION ALL
             SELECT a.esercizio, a.cd_centro_responsabilita,
                    TO_NUMBER (b.val01) id_classificazione,
                    NULL importo_assegnato,
                    NVL (a.im_cf_amm_altro, 0) importo_ripartito
               FROM pdg_modulo_costi a, configurazione_cnr b
              WHERE a.esercizio = b.esercizio
                AND b.cd_chiave_primaria = 'PIANO_RIPARTO'
                AND b.cd_chiave_secondaria = 'IM_CF_AMM_ALTRO') a,
            cdr b,
            v_classificazione_voci c
      WHERE a.cd_centro_responsabilita = b.cd_centro_responsabilita
        AND a.id_classificazione = c.id_classificazione
   GROUP BY a.esercizio,
            a.cd_centro_responsabilita,
            b.ds_cdr,
            a.id_classificazione,
            c.cd_classificazione,
            c.ds_classificazione;
