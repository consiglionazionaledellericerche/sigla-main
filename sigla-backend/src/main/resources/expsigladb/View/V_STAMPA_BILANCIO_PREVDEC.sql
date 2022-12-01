--------------------------------------------------------
--  DDL for View V_STAMPA_BILANCIO_PREVDEC
--------------------------------------------------------

CREATE OR REPLACE FORCE VIEW "V_STAMPA_BILANCIO_PREVDEC" ("FONTE", "ESERCIZIO", "TIPO", "CD_LIVELLO1", "CD_LIVELLO2", "CD_LIVELLO3", "CD_LIVELLO4", "CD_LIVELLO5", "CD_LIVELLO6", "CD_LIVELLO7", "CD_LIVELLO8", "CD_LIVELLO9", "DS_LIVELLO1", "DS_LIVELLO2", "DS_LIVELLO3", "DS_LIVELLO4", "DS_LIVELLO5", "DS_LIVELLO6", "DS_LIVELLO7", "DS_LIVELLO8", "DS_LIVELLO9", "IM_RESIDUI_AC", "IM_PREVISIONE_AC", "IM_ASSESTATO_AC", "IM_CASSA_AC", "IM_RESIDUI_AP", "IM_PREVISIONE_AP", "IM_CASSA_AP", "IM_PREVISIONE_AC2", "IM_PREVISIONE_AC3") AS
  (SELECT   e.fonte, e.esercizio, e.ti_gestione tipo,
             e.cd_missione cd_livello1, e.cd_programma cd_livello2,
             e.cd_livello1 cd_livello3, e.cd_livello2 cd_livello4,
             e.cd_livello3 cd_livello5, e.cd_livello4 cd_livello6,
             e.cd_livello5 cd_livello7, e.cd_livello6 cd_livello8,
             e.cd_livello7 cd_livello9,
             NVL (g.ds_missione, 'NON DEFINITO') ds_livello1,
             NVL (h.ds_programma, 'NON DEFINITO') ds_livello2,
             e.ds_liv1 ds_livello3, e.ds_liv2 ds_livello4,
             e.ds_liv3 ds_livello5, e.ds_liv4 ds_livello6,
             e.ds_liv5 ds_livello7, e.ds_liv6 ds_livello8,
             e.ds_liv7 ds_livello9, SUM (e.im_residui_ac),
             SUM (e.im_previsione_ac), SUM (e.im_assestato_ac), SUM (e.im_cassa_ac),
             SUM (e.im_residui_ap), SUM (e.im_previsione_ap),
             SUM (e.im_cassa_ap),
             SUM (e.im_previsione_ac2),
             SUM (e.im_previsione_ac3)
        FROM (                           --PARTE SPESE DECISIONALE SCIENTIFICO
              SELECT 'DECSCI' fonte, a.esercizio, c.ti_gestione,
                     NVL (a.cd_missione, 'NDF') cd_missione,
                     NVL (areaprog.cd_programma, 'NDF') cd_programma,
                     c.cd_livello1, c.cd_livello2, c.cd_livello3,
                     c.cd_livello4, c.cd_livello5, c.cd_livello6,
                     c.cd_livello7, c.ds_liv1, c.ds_liv2, c.ds_liv3,
                     c.ds_liv4, c.ds_liv5, c.ds_liv6, c.ds_liv7,
                     0 im_residui_ac,
                       NVL (a.im_spese_gest_decentrata_int, 0)
                     + NVL (a.im_spese_gest_decentrata_est, 0)
                     + NVL (a.im_spese_gest_accentrata_int, 0)
                     + NVL (a.im_spese_gest_accentrata_est, 0) im_previsione_ac,
                     0 im_assestato_ac,
                     CASE WHEN e.descrizione='CNR'
                          then 0
                          else (SELECT NVL(SUM(IM_PAGAMENTI),0) FROM PDG_MODULO_SPESE_GEST SG
                                WHERE SG.ESERCIZIO = A.ESERCIZIO
                                AND   SG.CD_CENTRO_RESPONSABILITA = A.CD_CENTRO_RESPONSABILITA
                                AND   SG.PG_PROGETTO = A.PG_PROGETTO
                                AND   SG.ID_CLASSIFICAZIONE = A.ID_CLASSIFICAZIONE
                                AND   SG.CD_CDS_AREA = A.CD_CDS_AREA
                                AND   SG.PG_DETTAGLIO = A.PG_DETTAGLIO)
                     END im_cassa_ac,
                     0 im_residui_ap,
                     0 im_previsione_ap, 0 im_cassa_ap,
                     NVL (a.im_spese_a2, 0) im_previsione_ac2,
                     NVL (a.im_spese_a3, 0) im_previsione_ac3
                FROM pdg_modulo_spese a,
                     v_classificazione_voci_all c,
                     progetto_prev progetto,
                     progetto_prev areaprog,
                     parametri_ente e
               WHERE a.id_classificazione = c.id_classificazione
                 AND a.esercizio = progetto.esercizio
                 AND a.pg_progetto = progetto.pg_progetto
                 AND progetto.esercizio_progetto_padre = areaprog.esercizio
                 AND progetto.pg_progetto_padre = areaprog.pg_progetto
                 AND e.attivo='Y'
              UNION ALL
              --PARTE SPESE GESTIONALE SCIENTIFICO
              SELECT 'GESTSCI' fonte, a.esercizio, a.ti_gestione,
                     NVL (d.cd_missione, 'NDF'), NVL (d.cd_programma, 'NDF'),
                     c.cd_livello1, c.cd_livello2, c.cd_livello3,
                     c.cd_livello4, c.cd_livello5, c.cd_livello6,
                     c.cd_livello7, c.ds_liv1, c.ds_liv2, c.ds_liv3,
                     c.ds_liv4, c.ds_liv5, c.ds_liv6, c.ds_liv7,
                     0 im_residui_ac,
                       NVL (a.im_spese_gest_decentrata_int, 0)
                     + NVL (a.im_spese_gest_decentrata_est, 0)
                     + NVL (a.im_spese_gest_accentrata_int, 0)
                     + NVL (a.im_spese_gest_accentrata_est, 0) im_previsione_ac,
                     0 im_assestato_ac,
                     CASE WHEN e.descrizione='CNR'
                          then 0
                          else NVL (a.im_pagamenti, 0)
                     END im_cassa_ac, 0 im_residui_ap,
                     0 im_previsione_ap, 0 im_cassa_ap,
                     0 im_previsione_ac2, 0 im_previsione_ac3
                FROM pdg_modulo_spese_gest a,
                     elemento_voce b,
                     linea_attivita d,
                     v_classificazione_voci_all c,
                     parametri_ente e
               WHERE a.esercizio = b.esercizio
                 AND a.ti_appartenenza = b.ti_appartenenza
                 AND a.ti_gestione = b.ti_gestione
                 AND a.cd_elemento_voce = b.cd_elemento_voce
                 AND a.cd_cdr_assegnatario = d.cd_centro_responsabilita
                 AND a.cd_linea_attivita = d.cd_linea_attivita
                 AND b.id_classificazione = c.id_classificazione
                 AND a.cd_cdr_assegnatario_clgs IS NULL
                 AND e.attivo='Y'
              UNION ALL
              --PARTE SPESE DECISIONALE SCIENTIFICO PER VALORIZZAZIONE VARIAZIONI
              SELECT 'DECSCI' fonte, b.esercizio, 'S',
                     NVL (a.cd_missione, 'NDF'), NVL (a.cd_programma, 'NDF'),
                     c.cd_livello1, c.cd_livello2, c.cd_livello3,
                     c.cd_livello4, c.cd_livello5, c.cd_livello6,
                     c.cd_livello7, c.ds_liv1, c.ds_liv2, c.ds_liv3,
                     c.ds_liv4, c.ds_liv5, c.ds_liv6, c.ds_liv7,
                     0 im_residui_ac, 0 im_previsione_ac,
                     NVL(a.im_previsione_ap, 0) im_assestato_ac,
                     0 im_cassa_ac, 0 im_residui_ap,
                     0 im_previsione_ap, 0 im_cassa_ap,
                     0 im_previsione_ac2, 0 im_previsione_ac3
                FROM pdg_dati_stampa_bilancio_temp a,
                     elemento_voce b,
                     v_classificazione_voci_all c
               WHERE a.ti_gestione = 'S'
                 AND b.esercizio = a.esercizio-1
                 AND b.ti_gestione = a.ti_gestione
                 AND b.cd_elemento_voce = a.cd_elemento_voce
                 AND b.id_classificazione = c.id_classificazione
              UNION ALL
              --PARTE SPESE GESTIONALE SCIENTIFICO PER VALORIZZAZIONE VARIAZIONI
              SELECT 'GESTSCI' fonte, b.esercizio, 'S',
                     NVL (a.cd_missione, 'NDF'), NVL (a.cd_programma, 'NDF'),
                     c.cd_livello1, c.cd_livello2, c.cd_livello3,
                     c.cd_livello4, c.cd_livello5, c.cd_livello6,
                     c.cd_livello7, c.ds_liv1, c.ds_liv2, c.ds_liv3,
                     c.ds_liv4, c.ds_liv5, c.ds_liv6, c.ds_liv7,
                     0 im_residui_ac, 0 im_previsione_ac,
                     NVL(a.im_previsione_ap, 0) im_assestato_ac,
                     0 im_cassa_ac, 0 im_residui_ap,
                     0 im_previsione_ap, 0 im_cassa_ap,
                     0 im_previsione_ac2, 0 im_previsione_ac3
                FROM pdg_dati_stampa_bilancio_temp a,
                     elemento_voce b,
                     v_classificazione_voci_all c
               WHERE a.ti_gestione = 'S'
                 AND b.esercizio = a.esercizio-1
                 AND b.ti_gestione = a.ti_gestione
                 AND b.cd_elemento_voce = a.cd_elemento_voce
                 AND b.id_classificazione = c.id_classificazione
              UNION ALL
              --PARTE SPESE STANZIAMENTO SCIENTIFICO
              SELECT 'ASSSCI' fonte, a.esercizio, a.ti_gestione,
                     NVL (d.cd_missione, 'NDF'), NVL (d.cd_programma, 'NDF'),
                     c.cd_livello1, c.cd_livello2, c.cd_livello3,
                     c.cd_livello4, c.cd_livello5, c.cd_livello6,
                     c.cd_livello7, c.ds_liv1, c.ds_liv2, c.ds_liv3,
                     c.ds_liv4, c.ds_liv5, c.ds_liv6, c.ds_liv7,
                     0 im_residui_ac,
                     NVL (stanziamento_iniziale, 0) im_previsione_ac,
                     NVL (assestato_iniziale, 0) im_assestato_ac,
                     0 im_cassa_ac, 0 im_residui_ap,
                     0 im_previsione_ap, 0 im_cassa_ap,
                     0 im_previsione_ac2, 0 im_previsione_ac3
                FROM v_assestato a,
                     elemento_voce b,
                     linea_attivita d,
                     v_classificazione_voci_all c
               WHERE a.ti_gestione = 'S'
                 AND a.esercizio_res = a.esercizio
                 AND a.esercizio = b.esercizio
                 AND a.ti_appartenenza = b.ti_appartenenza
                 AND a.ti_gestione = b.ti_gestione
                 AND a.cd_elemento_voce = b.cd_elemento_voce
                 AND a.cd_centro_responsabilita = d.cd_centro_responsabilita
                 AND a.cd_linea_attivita = d.cd_linea_attivita
                 AND b.id_classificazione = c.id_classificazione
              UNION ALL
              --PARTE SPESE DATI ESTERNI SCIENTIFICI CLAUDIA
              SELECT 'EXTSCI' fonte, a.esercizio, 'S',
                     NVL (a.cd_missione, 'NDF'), NVL (a.cd_programma, 'NDF'),
                     c.cd_livello1, c.cd_livello2, c.cd_livello3,
                     c.cd_livello4, c.cd_livello5, c.cd_livello6,
                     c.cd_livello7, c.ds_liv1, c.ds_liv2, c.ds_liv3,
                     c.ds_liv4, c.ds_liv5, c.ds_liv6, c.ds_liv7,
                     NVL (a.im_residui_ac, 0) im_residui_ac,
                     NVL (a.im_previsione_ac, 0) im_previsione_ac,
                     NVL (a.im_previsione_ac, 0) im_assestato_ac,
                     NVL (a.im_cassa_ac, 0) im_cassa_ac,
                     NVL (a.im_residui_ap, 0) im_residui_ap,
                     NVL (a.im_previsione_ap, 0) im_previsione_ap,
                     NVL (a.im_cassa_ap, 0) im_cassa_ap,
                     0 im_previsione_ac2, 0 im_previsione_ac3
                FROM pdg_dati_stampa_bilancio_temp a,
                     elemento_voce b,
                     v_classificazione_voci_all c
               WHERE a.ti_gestione = 'S'
                 AND b.esercizio = a.esercizio
                 AND b.ti_gestione = a.ti_gestione
                 AND b.cd_elemento_voce = a.cd_elemento_voce
                 AND b.id_classificazione = c.id_classificazione
              UNION ALL
              --PARTE SPESE DATI STORICIZZATI SCIENTIFICI
              SELECT 'STOSCI' fonte, a.esercizio, 'S',
                     NVL (a.cd_missione, 'NDF'), NVL (a.cd_programma, 'NDF'),
                     c.cd_livello1, c.cd_livello2, c.cd_livello3,
                     c.cd_livello4, c.cd_livello5, c.cd_livello6,
                     c.cd_livello7, c.ds_liv1, c.ds_liv2, c.ds_liv3,
                     c.ds_liv4, c.ds_liv5, c.ds_liv6, c.ds_liv7,
                     NVL (a.im_residui_ac, 0) im_residui_ac,
                     0 im_previsione_ac,
                     0 im_assestato_ac,
                     CASE WHEN e.descrizione='CNR'
                          then NVL (a.im_cassa_ac, 0)
                          else 0
                     END im_cassa_ac,
                     0 im_residui_ap, 0 im_previsione_ap, 0 im_cassa_ap,
                     0 im_previsione_ac2, 0 im_previsione_ac3
                FROM pdg_dati_stampa_bilancio a,
                     elemento_voce b,
                     v_classificazione_voci_all c,
                     parametri_ente e
               WHERE a.ti_gestione = 'S'
                 AND b.esercizio = a.esercizio
                 AND b.ti_gestione = a.ti_gestione
                 AND b.cd_elemento_voce = a.cd_elemento_voce
                 AND b.id_classificazione = c.id_classificazione
                 AND e.attivo='Y'
              UNION ALL
              --PARTE SPESE DATI STORICIZZATI SCIENTIFICI
              SELECT 'STOSCI' fonte, a.esercizio, 'S',
                     NVL (a.cd_missione, 'NDF') cd_missione,
                     NVL (a.cd_programma, 'NDF') cd_programma, c.cd_livello1,
                     c.cd_livello2, c.cd_livello3, c.cd_livello4,
                     c.cd_livello5, c.cd_livello6, c.cd_livello7, c.ds_liv1,
                     c.ds_liv2, c.ds_liv3, c.ds_liv4, c.ds_liv5, c.ds_liv6,
                     c.ds_liv7, 0 im_residui_ac, 0 im_previsione_ac, 0 im_assestato_ac,
                     0 im_cassa_ac, NVL (a.im_residuo_ap, 0) im_residui_ap,
                     0 im_previsione_ap, 0 im_cassa_ap,
                     0 im_previsione_ac2, 0 im_previsione_ac3
                FROM (SELECT saldi.esercizio + 1 esercizio, saldi.ti_gestione,
                             NVL
                                ((SELECT cd_elemento_voce_new
                                    FROM ass_evold_evnew
                                   WHERE esercizio_old = saldi.esercizio
                                     AND ti_appartenenza_old =
                                                         saldi.ti_appartenenza
                                     AND ti_gestione_old = saldi.ti_gestione
                                     AND cd_elemento_voce_old =
                                                        saldi.cd_elemento_voce),
                                 saldi.cd_elemento_voce
                                ) cd_elemento_voce,
                             linea.cd_programma, linea.cd_missione,
                               NVL (im_stanz_res_improprio, 0)
                             + NVL (im_obbl_res_pro, 0) im_residuo_ap
                        FROM voce_f_saldi_cdr_linea saldi,
                             v_linea_attivita_valida linea
                       WHERE saldi.esercizio_res < saldi.esercizio
                         AND saldi.ti_gestione = 'S'
                         AND saldi.esercizio = linea.esercizio(+)
                         AND saldi.cd_centro_responsabilita = linea.cd_centro_responsabilita(+)
                         AND saldi.cd_linea_attivita = linea.cd_linea_attivita(+)) a,
                     elemento_voce b,
                     v_classificazione_voci_all c
               WHERE a.ti_gestione = 'S'
                 AND b.esercizio = a.esercizio
                 AND b.ti_gestione = a.ti_gestione
                 AND b.cd_elemento_voce = a.cd_elemento_voce
                 AND b.id_classificazione = c.id_classificazione) e,
             pdg_missione g,
             pdg_programma h
       WHERE e.cd_missione = g.cd_missione(+) AND e.cd_programma = h.cd_programma(+)
    GROUP BY e.fonte,
             e.esercizio,
             e.ti_gestione,
             e.cd_missione,
             e.cd_programma,
             e.cd_livello1,
             e.cd_livello2,
             e.cd_livello3,
             e.cd_livello4,
             e.cd_livello5,
             e.cd_livello6,
             e.cd_livello7,
             NVL (g.ds_missione, 'NON DEFINITO'),
             NVL (h.ds_programma, 'NON DEFINITO'),
             e.ds_liv1,
             e.ds_liv2,
             e.ds_liv3,
             e.ds_liv4,
             e.ds_liv5,
             e.ds_liv6,
             e.ds_liv7
    UNION ALL
    --PARTE SPESE DECISIONALE FINANZIARIO
    SELECT   'DECFIN' fonte, a.esercizio, c.ti_gestione, c.cd_livello1,
             c.cd_livello2, c.cd_livello3, c.cd_livello4, c.cd_livello5,
             c.cd_livello6, c.cd_livello7, NULL, NULL, c.ds_liv1, c.ds_liv2,
             c.ds_liv3, c.ds_liv4, c.ds_liv5, c.ds_liv6, c.ds_liv7, NULL,
             NULL, 0 im_residui_ac,
               NVL (SUM (a.im_spese_gest_decentrata_int), 0)
             + NVL (SUM (a.im_spese_gest_decentrata_est), 0)
             + NVL (SUM (a.im_spese_gest_accentrata_int), 0)
             + NVL (SUM (a.im_spese_gest_accentrata_est), 0) im_previsione_ac,
             0 im_assestato_ac,
             NVL(SUM(CASE WHEN e.descrizione='CNR'
                          then 0
                          else (SELECT NVL(SUM(IM_PAGAMENTI),0) FROM PDG_MODULO_SPESE_GEST SG
                                WHERE SG.ESERCIZIO = A.ESERCIZIO
                                AND   SG.CD_CENTRO_RESPONSABILITA = A.CD_CENTRO_RESPONSABILITA
                                AND   SG.PG_PROGETTO = A.PG_PROGETTO
                                AND   SG.ID_CLASSIFICAZIONE = A.ID_CLASSIFICAZIONE
                                AND   SG.CD_CDS_AREA = A.CD_CDS_AREA
                                AND   SG.PG_DETTAGLIO = A.PG_DETTAGLIO)
                     END), 0) im_cassa_ac,
             0 im_residui_ap, 0 im_previsione_ap, 0 im_cassa_ap,
             NVL (SUM (a.im_spese_a2), 0) im_previsione_ac2,
             NVL (SUM (a.im_spese_a3), 0) im_previsione_ac3
        FROM pdg_modulo_spese a, v_classificazione_voci_all c, parametri_ente e
       WHERE a.id_classificazione = c.id_classificazione
       AND e.attivo='Y'
    GROUP BY a.esercizio,
             c.ti_gestione,
             c.cd_livello1,
             c.cd_livello2,
             c.cd_livello3,
             c.cd_livello4,
             c.cd_livello5,
             c.cd_livello6,
             c.cd_livello7,
             c.ds_liv1,
             c.ds_liv2,
             c.ds_liv3,
             c.ds_liv4,
             c.ds_liv5,
             c.ds_liv6,
             c.ds_liv7
    UNION ALL
    --PARTE ENTRATE DECISIONALE FINANZIARIO
    SELECT   'DECFIN' fonte, a.esercizio, c.ti_gestione, c.cd_livello1,
             c.cd_livello2, c.cd_livello3, c.cd_livello4, c.cd_livello5,
             c.cd_livello6, c.cd_livello7, NULL, NULL, c.ds_liv1, c.ds_liv2,
             c.ds_liv3, c.ds_liv4, c.ds_liv5, c.ds_liv6, c.ds_liv7, NULL,
             NULL, 0 im_residui_ac,
             NVL (SUM (NVL (a.im_entrata_app, a.im_entrata)), 0) im_previsione_ac,
             0 im_assestato_ac,
             NVL(SUM(CASE WHEN e.descrizione='CNR'
                          then 0
                          else (SELECT NVL(SUM(IM_INCASSI),0) FROM PDG_MODULO_ENTRATE_GEST EG
                                WHERE EG.ESERCIZIO = A.ESERCIZIO
                                AND   EG.CD_CENTRO_RESPONSABILITA = A.CD_CENTRO_RESPONSABILITA
                                AND   EG.PG_PROGETTO = A.PG_PROGETTO
                                AND   EG.CD_NATURA = A.CD_NATURA
                                AND   EG.ID_CLASSIFICAZIONE = A.ID_CLASSIFICAZIONE
                                AND   EG.PG_DETTAGLIO = A.PG_DETTAGLIO
                                AND   EG.CD_CDS_AREA = A.CD_CDS_AREA)
                     END), 0) im_cassa_ac,
             0 im_residui_ap,
             0 im_previsione_ap, 0 im_cassa_ap,
             NVL (SUM (a.im_entrata_a2), 0) im_previsione_ac2,
             NVL (SUM (a.im_entrata_a3), 0) im_previsione_ac3
        FROM pdg_modulo_entrate a, v_classificazione_voci_all c, parametri_ente e
       WHERE a.id_classificazione = c.id_classificazione
       AND e.attivo='Y'
    GROUP BY a.esercizio,
             c.ti_gestione,
             c.cd_livello1,
             c.cd_livello2,
             c.cd_livello3,
             c.cd_livello4,
             c.cd_livello5,
             c.cd_livello6,
             c.cd_livello7,
             c.ds_liv1,
             c.ds_liv2,
             c.ds_liv3,
             c.ds_liv4,
             c.ds_liv5,
             c.ds_liv6,
             c.ds_liv7
    UNION ALL
    --PARTE SPESE GESTIONALE FINANZIARIO
    SELECT   'GESTFIN' fonte, a.esercizio, a.ti_gestione, c.cd_livello1,
             c.cd_livello2, c.cd_livello3, c.cd_livello4, c.cd_livello5,
             c.cd_livello6, c.cd_livello7, NULL, NULL, c.ds_liv1, c.ds_liv2,
             c.ds_liv3, c.ds_liv4, c.ds_liv5, c.ds_liv6, c.ds_liv7, NULL,
             NULL, 0 im_residui_ac,
               NVL (SUM (a.im_spese_gest_decentrata_int), 0)
             + NVL (SUM (a.im_spese_gest_decentrata_est), 0)
             + NVL (SUM (a.im_spese_gest_accentrata_int), 0)
             + NVL (SUM (a.im_spese_gest_accentrata_est), 0) im_previsione_ac,
             0 im_assestato_ac,
             NVL (SUM (CASE WHEN e.descrizione='CNR'
                            then 0
                            else NVL(a.im_pagamenti, 0)
                       END), 0) im_cassa_ac, 0 im_residui_ap,
             0 im_previsione_ap, 0 im_cassa_ap,
             NVL (SUM (d.im_spese_a2), 0) im_previsione_ac2,
             NVL (SUM (d.im_spese_a3), 0) im_previsione_ac3
        FROM pdg_modulo_spese_gest a,
             pdg_modulo_spese d,
             elemento_voce b,
             v_classificazione_voci_all c,
             parametri_ente e
       WHERE a.esercizio = d.esercizio
         AND a.cd_centro_responsabilita = d.cd_centro_responsabilita
         AND a.pg_progetto = d.pg_progetto
         AND a.id_classificazione = d.id_classificazione
         AND a.cd_cds_area = d.cd_cds_area
         AND a.pg_dettaglio = d.pg_dettaglio
         AND a.esercizio = b.esercizio
         AND a.ti_appartenenza = b.ti_appartenenza
         AND a.ti_gestione = b.ti_gestione
         AND a.cd_elemento_voce = b.cd_elemento_voce
         AND b.id_classificazione = c.id_classificazione
         AND a.cd_cdr_assegnatario_clgs IS NULL
         AND e.attivo = 'Y'
    GROUP BY a.esercizio,
             a.ti_gestione,
             c.cd_livello1,
             c.cd_livello2,
             c.cd_livello3,
             c.cd_livello4,
             c.cd_livello5,
             c.cd_livello6,
             c.cd_livello7,
             c.ds_liv1,
             c.ds_liv2,
             c.ds_liv3,
             c.ds_liv4,
             c.ds_liv5,
             c.ds_liv6,
             c.ds_liv7
    UNION ALL
    --PARTE ENTRATE GESTIONALE FINANZIARIO
    SELECT   'GESTFIN' fonte, a.esercizio, a.ti_gestione, c.cd_livello1,
             c.cd_livello2, c.cd_livello3, c.cd_livello4, c.cd_livello5,
             c.cd_livello6, c.cd_livello7, NULL, NULL, c.ds_liv1, c.ds_liv2,
             c.ds_liv3, c.ds_liv4, c.ds_liv5, c.ds_liv6, c.ds_liv7, NULL,
             NULL, 0 im_residui_ac,
             NVL (SUM (a.im_entrata), 0) im_previsione_ac,
             0 im_assestato_ac,
             NVL (SUM (a.im_incassi), 0) im_cassa_ac,
             0 im_residui_ap, 0 im_previsione_ap, 0 im_cassa_ap,
             NVL (SUM (d.im_entrata_a2), 0) im_previsione_ac2,
             NVL (SUM (d.im_entrata_a3), 0) im_previsione_ac3
        FROM pdg_modulo_entrate_gest a,
             pdg_modulo_entrate d,
             elemento_voce b,
             v_classificazione_voci_all c
       WHERE a.esercizio = d.esercizio
         AND a.cd_centro_responsabilita = d.cd_centro_responsabilita
         AND a.pg_progetto = d.pg_progetto
         AND a.cd_natura = d.cd_natura
         AND a.id_classificazione = d.id_classificazione
         AND a.cd_cds_area = d.cd_cds_area
         AND a.pg_dettaglio = d.pg_dettaglio
         AND a.esercizio = b.esercizio
         AND a.ti_appartenenza = b.ti_appartenenza
         AND a.ti_gestione = b.ti_gestione
         AND a.cd_elemento_voce = b.cd_elemento_voce
         AND b.id_classificazione = c.id_classificazione
         AND a.cd_cdr_assegnatario_clge IS NULL
    GROUP BY a.esercizio,
             a.ti_gestione,
             c.cd_livello1,
             c.cd_livello2,
             c.cd_livello3,
             c.cd_livello4,
             c.cd_livello5,
             c.cd_livello6,
             c.cd_livello7,
             c.ds_liv1,
             c.ds_liv2,
             c.ds_liv3,
             c.ds_liv4,
             c.ds_liv5,
             c.ds_liv6,
             c.ds_liv7
    UNION ALL
    --PARTE ENTRATE E SPESE DECISIONALE FINANZIARIO PER VALORIZZAZIONE VARIAZIONI
    SELECT   'DECFIN' fonte, b.esercizio, a.ti_gestione, c.cd_livello1,
             c.cd_livello2, c.cd_livello3, c.cd_livello4, c.cd_livello5,
             c.cd_livello6, c.cd_livello7, NULL, NULL, c.ds_liv1, c.ds_liv2,
             c.ds_liv3, c.ds_liv4, c.ds_liv5, c.ds_liv6, c.ds_liv7, NULL,
             NULL, 0 im_residui_ac, 0 im_previsione_ac,
             NVL (SUM (a.im_previsione_ap), 0) im_assestato_ac,
             0 im_cassa_ac, 0 im_residui_ap,
             0 im_previsione_ap, 0 im_cassa_ap,
             0 im_previsione_ac2, 0 im_previsione_ac3
        FROM pdg_dati_stampa_bilancio_temp a,
             elemento_voce b,
             v_classificazione_voci_all c
       WHERE b.esercizio = a.esercizio-1
         AND b.ti_gestione = a.ti_gestione
         AND b.cd_elemento_voce = a.cd_elemento_voce
         AND b.id_classificazione = c.id_classificazione
    GROUP BY b.esercizio,
             a.ti_gestione,
             c.cd_livello1,
             c.cd_livello2,
             c.cd_livello3,
             c.cd_livello4,
             c.cd_livello5,
             c.cd_livello6,
             c.cd_livello7,
             c.ds_liv1,
             c.ds_liv2,
             c.ds_liv3,
             c.ds_liv4,
             c.ds_liv5,
             c.ds_liv6,
             c.ds_liv7
    UNION ALL
    --PARTE ENTRATE E SPESE GESTIONALE FINANZIARIO PER VALORIZZAZIONE VARIAZIONI
    SELECT   'GESTFIN' fonte, b.esercizio, a.ti_gestione, c.cd_livello1,
             c.cd_livello2, c.cd_livello3, c.cd_livello4, c.cd_livello5,
             c.cd_livello6, c.cd_livello7, NULL, NULL, c.ds_liv1, c.ds_liv2,
             c.ds_liv3, c.ds_liv4, c.ds_liv5, c.ds_liv6, c.ds_liv7, NULL,
             NULL, 0 im_residui_ac, 0 im_previsione_ac,
             NVL (SUM (a.im_previsione_ap), 0) im_assestato_ac,
             0 im_cassa_ac, 0 im_residui_ap,
             0 im_previsione_ap, 0 im_cassa_ap,
             0 im_previsione_ac2, 0 im_previsione_ac3
        FROM pdg_dati_stampa_bilancio_temp a,
             elemento_voce b,
             v_classificazione_voci_all c
       WHERE b.esercizio = a.esercizio-1
         AND b.ti_gestione = a.ti_gestione
         AND b.cd_elemento_voce = a.cd_elemento_voce
         AND b.id_classificazione = c.id_classificazione
    GROUP BY b.esercizio,
             a.ti_gestione,
             c.cd_livello1,
             c.cd_livello2,
             c.cd_livello3,
             c.cd_livello4,
             c.cd_livello5,
             c.cd_livello6,
             c.cd_livello7,
             c.ds_liv1,
             c.ds_liv2,
             c.ds_liv3,
             c.ds_liv4,
             c.ds_liv5,
             c.ds_liv6,
             c.ds_liv7
    UNION ALL
    --PARTE ENTRATE E SPESE STANZIAMENTO FINANZIARIO
    SELECT   'ASSFIN' fonte, a.esercizio, a.ti_gestione, c.cd_livello1,
             c.cd_livello2, c.cd_livello3, c.cd_livello4, c.cd_livello5,
             c.cd_livello6, c.cd_livello7, NULL, NULL, c.ds_liv1, c.ds_liv2,
             c.ds_liv3, c.ds_liv4, c.ds_liv5, c.ds_liv6, c.ds_liv7, NULL,
             NULL, 0 im_residui_ac,
             NVL (SUM (stanziamento_iniziale), 0) im_previsione_ac,
             NVL (SUM (assestato_iniziale), 0) im_assestato_ac,
             0 im_cassa_ac, 0 im_residui_ap,
             0 im_previsione_ap, 0 im_cassa_ap,
             0 im_previsione_ac2, 0 im_previsione_ac3
        FROM v_assestato a, elemento_voce b, v_classificazione_voci_all c
       WHERE a.esercizio_res = a.esercizio
         AND a.esercizio = b.esercizio
         AND a.ti_appartenenza = b.ti_appartenenza
         AND a.ti_gestione = b.ti_gestione
         AND a.cd_elemento_voce = b.cd_elemento_voce
         AND b.id_classificazione = c.id_classificazione
    GROUP BY a.esercizio,
             a.ti_gestione,
             c.cd_livello1,
             c.cd_livello2,
             c.cd_livello3,
             c.cd_livello4,
             c.cd_livello5,
             c.cd_livello6,
             c.cd_livello7,
             c.ds_liv1,
             c.ds_liv2,
             c.ds_liv3,
             c.ds_liv4,
             c.ds_liv5,
             c.ds_liv6,
             c.ds_liv7
    UNION ALL
    --PARTE ENTRATE E SPESE DATI ESTERNI FINANZIARI CLAUDIA
    SELECT   'EXTFIN' fonte, a.esercizio, a.ti_gestione, c.cd_livello1,
             c.cd_livello2, c.cd_livello3, c.cd_livello4, c.cd_livello5,
             c.cd_livello6, c.cd_livello7, NULL, NULL, c.ds_liv1, c.ds_liv2,
             c.ds_liv3, c.ds_liv4, c.ds_liv5, c.ds_liv6, c.ds_liv7, NULL,
             NULL, NVL (SUM (a.im_residui_ac), 0) im_residui_ac,
             NVL (SUM (a.im_previsione_ac), 0) im_previsione_ac,
             NVL (SUM (a.im_previsione_ac), 0) im_assestato_ac,
             NVL (SUM (a.im_cassa_ac), 0) im_cassa_ac,
             NVL (SUM (a.im_residui_ap), 0) im_residui_ap,
             NVL (SUM (a.im_previsione_ap), 0) im_previsione_ap,
             NVL (SUM (a.im_cassa_ap), 0) im_cassa_ap,
             0 im_previsione_ac2, 0 im_previsione_ac3
        FROM pdg_dati_stampa_bilancio_temp a,
             elemento_voce b,
             v_classificazione_voci_all c
       WHERE b.esercizio = a.esercizio
         AND b.ti_gestione = a.ti_gestione
         AND b.cd_elemento_voce = a.cd_elemento_voce
         AND b.id_classificazione = c.id_classificazione
    GROUP BY a.esercizio,
             a.ti_gestione,
             c.cd_livello1,
             c.cd_livello2,
             c.cd_livello3,
             c.cd_livello4,
             c.cd_livello5,
             c.cd_livello6,
             c.cd_livello7,
             c.ds_liv1,
             c.ds_liv2,
             c.ds_liv3,
             c.ds_liv4,
             c.ds_liv5,
             c.ds_liv6,
             c.ds_liv7
    UNION ALL
    --PARTE ENTRATE E SPESE DATI STORICIZZATI FINANZIARI
    SELECT   'STOFIN' fonte, a.esercizio, a.ti_gestione, c.cd_livello1,
             c.cd_livello2, c.cd_livello3, c.cd_livello4, c.cd_livello5,
             c.cd_livello6, c.cd_livello7, NULL, NULL, c.ds_liv1, c.ds_liv2,
             c.ds_liv3, c.ds_liv4, c.ds_liv5, c.ds_liv6, c.ds_liv7, NULL,
             NULL, NVL (SUM (a.im_residui_ac), 0) im_residui_ac,
             0 im_previsione_ac,
             0 im_assestato_ac,
             NVL (SUM (CASE WHEN e.descrizione='CNR'
                            then NVL(a.im_cassa_ac, 0)
                            else 0
                       END), 0) im_cassa_ac,
             0 im_residui_ap, 0 im_previsione_ap, 0 im_cassa_ap,
             0 im_previsione_ac2, 0 im_previsione_ac3
        FROM pdg_dati_stampa_bilancio a,
             elemento_voce b,
             v_classificazione_voci_all c,
             parametri_ente e
       WHERE b.esercizio = a.esercizio
         AND b.ti_gestione = a.ti_gestione
         AND b.cd_elemento_voce = a.cd_elemento_voce
         AND b.id_classificazione = c.id_classificazione
         AND e.attivo = 'Y'
    GROUP BY a.esercizio,
             a.ti_gestione,
             c.cd_livello1,
             c.cd_livello2,
             c.cd_livello3,
             c.cd_livello4,
             c.cd_livello5,
             c.cd_livello6,
             c.cd_livello7,
             c.ds_liv1,
             c.ds_liv2,
             c.ds_liv3,
             c.ds_liv4,
             c.ds_liv5,
             c.ds_liv6,
             c.ds_liv7
    UNION ALL
    --PARTE ENTRATE E SPESE DATI STORICIZZATI FINANZIARI
    SELECT   'STOFIN' fonte, a.esercizio, c.ti_gestione, c.cd_livello1,
             c.cd_livello2, c.cd_livello3, c.cd_livello4, c.cd_livello5,
             c.cd_livello6, c.cd_livello7, NULL, NULL, c.ds_liv1, c.ds_liv2,
             c.ds_liv3, c.ds_liv4, c.ds_liv5, c.ds_liv6, c.ds_liv7, NULL,
             NULL, 0 im_residui_ac, 0 im_previsione_ac, 0 im_assestato_ac, 0 im_cassa_ac,
             NVL (SUM (im_residuo_ap), 0) im_residui_ap,
             0 im_previsione_ap, 0 im_cassa_ap,
             0 im_previsione_ac2, 0 im_previsione_ac3
        FROM (SELECT saldi.esercizio + 1 esercizio, saldi.ti_gestione,
                     NVL ((SELECT cd_elemento_voce_new
                             FROM ass_evold_evnew
                            WHERE esercizio_old = saldi.esercizio
                              AND ti_appartenenza_old = saldi.ti_appartenenza
                              AND ti_gestione_old = saldi.ti_gestione
                              AND cd_elemento_voce_old =
                                                        saldi.cd_elemento_voce),
                          saldi.cd_elemento_voce
                         ) cd_elemento_voce,
                     CASE WHEN e.descrizione='CNR'
                     then NVL (im_stanz_res_improprio, 0) + NVL (im_obbl_res_pro, 0)
                     else NVL (im_obbl_res_pro, 0)
                     END im_residuo_ap
                FROM voce_f_saldi_cdr_linea saldi,
                     parametri_ente e
               WHERE saldi.esercizio > 2015
                 AND saldi.esercizio_res < saldi.esercizio
                 AND e.attivo = 'Y') a,
             elemento_voce b,
             v_classificazione_voci_all c
       WHERE b.esercizio = a.esercizio
         AND b.ti_gestione = a.ti_gestione
         AND b.cd_elemento_voce = a.cd_elemento_voce
         AND b.id_classificazione = c.id_classificazione
    GROUP BY a.esercizio,
             c.ti_gestione,
             c.cd_livello1,
             c.cd_livello2,
             c.cd_livello3,
             c.cd_livello4,
             c.cd_livello5,
             c.cd_livello6,
             c.cd_livello7,
             c.ds_liv1,
             c.ds_liv2,
             c.ds_liv3,
             c.ds_liv4,
             c.ds_liv5,
             c.ds_liv6,
             c.ds_liv7);