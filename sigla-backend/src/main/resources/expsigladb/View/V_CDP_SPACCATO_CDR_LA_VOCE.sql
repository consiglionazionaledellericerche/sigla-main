--------------------------------------------------------
--  DDL for View V_CDP_SPACCATO_CDR_LA_VOCE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_CDP_SPACCATO_CDR_LA_VOCE" ("ESERCIZIO", "MESE", "CD_CDR_ROOT", "CD_CDR", "TI_APPARTENENZA", "TI_GESTIONE", "CD_ELEMENTO_VOCE", "CD_LINEA_ATTIVITA", "TI_RAPPORTO", "TI_PREV_CONS", "IM_A1", "IM_A2", "IM_A3", "IM_RND_A1", "IM_RND_A2", "IM_RND_A3") AS 
  SELECT
--
-- Date: 28/09/2011
-- Version: 1.3
--
-- Vista di estrazione dello spaccato mensile (pdg se mese = 0) costi dipendente per cdr/voce/la
--
-- History:
-- Date: 23/09/2002
-- Version: 1.0
-- Creazione
--
-- Date: 08/01/2004
-- Version: 1.1
-- Estrazione delgli arrotondamenti causati dalla precisione (15,2)
--
-- Date: 06/10/2005
-- Version: 1.2
-- Separati gli Oneri CNR ed il TFR del personale a tempo indeterminato da quello a tempo determinato
--
-- Date: 28/09/2011
-- Version: 1.3
-- Separati gli Oneri CNR del personale a tempo determinato con contratti privati rispetto a tutti gli altri TD
--
-- Body:
--
            esercizio, mese, cd_cdr_root, cd_cdr, ti_appartenenza,
            ti_gestione, cd_elemento_voce, cd_linea_attivita, ti_rapporto,
            ti_prev_cons, SUM (im_a1), SUM (im_a2), SUM (im_a3),
            SUM (im_a1) - ROUND (SUM (im_a1), 2),
            SUM (im_a2) - ROUND (SUM (im_a2), 2),
            SUM (im_a3) - ROUND (SUM (im_a3), 2)
       FROM (SELECT a.esercizio, a.mese, a.cd_cdr_root, a.cd_cdr,
                    p.ti_appartenenza, p.ti_gestione, p.cd_elemento_voce,
                    a.cd_linea_attivita, p.ti_rapporto, p.ti_prev_cons,
                    p.im_a1 * a.prc_a1 im_a1,
                    DECODE (p.ti_rapporto,
                            'IND', p.im_a2 * a.prc_a2,
                            0
                           ) im_a2,
                    DECODE (p.ti_rapporto,
                            'IND', p.im_a3 * a.prc_a3,
                            0
                           ) im_a3
               FROM costo_del_dipendente p, v_cdp_spaccato_cdr_la_matr a
              WHERE p.esercizio = a.esercizio
                AND p.ti_prev_cons = 'P'
                AND p.id_matricola = a.id_matricola
                AND p.mese = a.mese
                AND a.stato = 'I'
             UNION ALL
             SELECT a.esercizio, a.mese, a.cd_cdr_root, a.cd_cdr,
                    p.ti_appartenenza, p.ti_gestione, c.val01,
                    a.cd_linea_attivita, p.ti_rapporto, p.ti_prev_cons,
                    p.im_oneri_cnr_a1 * a.prc_a1 im_a1,
                    DECODE (p.ti_rapporto,
                            'IND', p.im_oneri_cnr_a2 * a.prc_a2,
                            0
                           ) im_a2,
                    DECODE (p.ti_rapporto,
                            'IND', p.im_oneri_cnr_a3 * a.prc_a3,
                            0
                           ) im_a3
               FROM costo_del_dipendente p,
                    v_cdp_spaccato_cdr_la_matr a,
                    configurazione_cnr c
              WHERE p.esercizio = a.esercizio
                AND p.ti_prev_cons = 'P'
                AND p.id_matricola = a.id_matricola
                AND p.mese = a.mese
                AND c.esercizio = a.esercizio
                AND c.cd_chiave_primaria = 'ELEMENTO_VOCE_SPECIALE'
                AND (   (    c.cd_chiave_secondaria = 'ONERI_CNR'
                         AND p.ti_rapporto = 'IND'
                        )
                     OR (    c.cd_chiave_secondaria = 'ONERI_CNR_TEMPO_DET'
                         AND p.ti_rapporto = 'DET'
                         AND p.fl_rapporto13 = 'N'
                         and p.ORIGINE_FONTI = 'FES'
                        )
                      OR (    c.cd_chiave_secondaria = 'ONERI_CNR_TEMPO_DET_FIN'
                         AND p.ti_rapporto = 'DET'
                         AND p.fl_rapporto13 = 'N'
                         and p.ORIGINE_FONTI = 'FIN'
                        )
                     OR (    c.cd_chiave_secondaria =
                                                   'ONERI_CNR_TEMPO_DET_RAP13'
                         AND p.ti_rapporto = 'DET'
                         AND p.fl_rapporto13 = 'Y'
                        )
                    )
                AND a.stato = 'I'
             UNION ALL
             SELECT a.esercizio, a.mese, a.cd_cdr_root, a.cd_cdr,
                    p.ti_appartenenza, p.ti_gestione, c.val01,
                    a.cd_linea_attivita, p.ti_rapporto, p.ti_prev_cons,
                    p.im_tfr_a1 * a.prc_a1 im_a1,
                    p.im_tfr_a2 * a.prc_a2 im_a2,
                    p.im_tfr_a3 * a.prc_a3 im_a3
               FROM costo_del_dipendente p,
                    v_cdp_spaccato_cdr_la_matr a,
                    configurazione_cnr c
              WHERE p.esercizio = a.esercizio
                AND a.mese = p.mese
                AND p.ti_prev_cons = 'P'
                AND p.id_matricola = a.id_matricola
                AND c.esercizio = a.esercizio
                AND c.cd_chiave_primaria = 'ELEMENTO_VOCE_SPECIALE'
                AND (   (    c.cd_chiave_secondaria = 'TFR'
                         AND p.ti_rapporto = 'IND'
                        )
                     OR (    c.cd_chiave_secondaria = 'TFR_TEMPO_DET'
                         AND p.ti_rapporto = 'DET'
                        )
                    )
                AND a.stato = 'I')
   GROUP BY esercizio,
            mese,
            cd_cdr_root,
            cd_cdr,
            ti_appartenenza,
            ti_gestione,
            cd_elemento_voce,
            cd_linea_attivita,
            ti_rapporto,
            ti_prev_cons;
