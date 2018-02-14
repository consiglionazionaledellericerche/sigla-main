--------------------------------------------------------
--  DDL for View V_SIT_BIL_CDS_CNR
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_SIT_BIL_CDS_CNR" ("ESERCIZIO", "CD_CDS", "TI_GESTIONE", "CD_VOCE", "IM_STANZ_INIZIALE", "IM_VARIAZIONI_PIU", "IM_VARIAZIONI_MENO", "IM_ASSESTATO", "IM_RESIDUO_INIZIALE", "IM_ACCERTATO_IMPEGNATO", "IM_PAGATO_INCASSATO_COMPETENZA", "IM_PAGATO_INCASSATO_RESIDUO", "IM_ACCREDITATO") AS 
  SELECT
--
-- Date: 06/06/2002
-- Version: 1.3
--
-- Situazione di bilancio entrate/spese CNR raggruppata per CDS
-- Situazione capillare su mastrini di spesa ed entrata
--
-- E' utilizzata dal mandato Accreditamento CNR-CdS per visualizzare i prospetti con la
-- situazione del bilancio entrate/spese del CNR per un certo CDS
--
-- La colonna degli accreditamenti e estratta selezionando solo le nature Attivita vincolata e conto terzi (2 e 3)
--
-- History:
--
-- Date: 06/02/2002
-- Version: 1.0
-- Creazione
--
-- Date: 13/02/2002
-- Version: 1.1
-- Aggiunto CD_VOCE
--
-- Date: 19/02/2002
-- Version: 1.2
-- Creazione body
--
-- Date: 06/06/2002
-- Version: 1.3
-- Gestione corretta dell'area
--
-- Date: 15/06/2009
-- Version: 1.4
-- Sostituita voce_f_saldi_cmp con voce_f_saldi_cdr_linea
--
-- Body:
--
            esercizio, cds, ti_gestione, cd_voce, SUM (im_stanz_iniziale),
            SUM (im_variazioni_piu), SUM (im_variazioni_meno),
            SUM (im_assestato), SUM (im_residuo_iniziale),
            SUM (im_accertato_impegnato),
            SUM (im_pagato_incassato_competenza),
            SUM (im_pagato_incassato_residuo), SUM (im_accreditato)
       FROM (SELECT a.esercizio esercizio,
                    DECODE (b.ti_gestione,
                            'S', DECODE (b.cd_cds,
                                         b.cd_proprio_voce, b.cd_cds,
                                         b.cd_proprio_voce
                                        ),
                            b.cd_cds
                           ) cds,
                    a.ti_gestione ti_gestione, a.cd_voce cd_voce,
                    a.im_stanz_iniziale_a1 im_stanz_iniziale,
                    a.variazioni_piu im_variazioni_piu,
                    a.variazioni_meno im_variazioni_meno,
                      a.im_stanz_iniziale_a1
                    + variazioni_piu
                    - variazioni_meno im_assestato,
                    a.im_obbl_res_pro im_residuo_iniziale,
                    a.im_obbl_acc_comp im_accertato_impegnato,
                    DECODE
                       (a.esercizio,
                        a.esercizio_res, a.im_pagamenti_incassi,
                        0
                       ) im_pagato_incassato_competenza,
                    DECODE
                          (a.esercizio,
                           a.esercizio_res, 0,
                           a.im_pagamenti_incassi
                          ) im_pagato_incassato_residuo,
                    0 im_accreditato
               FROM voce_f_saldi_cdr_linea a, voce_f b
              WHERE a.ti_appartenenza = 'C'
                AND
                    -- join voce_f
                    b.esercizio = a.esercizio
                AND b.ti_appartenenza = a.ti_appartenenza
                AND b.ti_gestione = a.ti_gestione
                AND b.cd_voce = a.cd_voce
                AND b.fl_mastrino = 'Y'
             UNION ALL
-- Estrae l'accreditato (solo natura 2 e 3, vincolata e conto terzi)
             SELECT a.esercizio esercizio,
                    DECODE (b.ti_gestione,
                            'S', DECODE (b.cd_cds,
                                         b.cd_proprio_voce, b.cd_cds,
                                         b.cd_proprio_voce
                                        ),
                            b.cd_cds
                           ) cds,
                    a.ti_gestione ti_gestione, a.cd_voce cd_voce,
                    0 im_stanz_iniziale, 0 im_variazioni_piu,
                    0 im_variazioni_meno, 0 im_assestato,
                    0 im_residuo_iniziale, 0 im_accertato_impegnato,
                    0 im_pagato_incassato_competenza,
                    0 im_pagato_incassato_residuo,
                    im_mandati_reversali_pro im_accreditato
               FROM voce_f_saldi_cdr_linea a, voce_f b
              WHERE a.esercizio = a.esercizio_res
                AND a.ti_appartenenza = 'C'
                AND a.ti_gestione = 'S'
                AND b.esercizio = a.esercizio
                AND b.ti_appartenenza = a.ti_appartenenza
                AND b.ti_gestione = a.ti_gestione
                AND b.cd_voce = a.cd_voce
                AND b.fl_mastrino = 'Y'
                AND b.cd_natura IN ('2', '3'))
   GROUP BY esercizio, cds, ti_gestione, cd_voce;

   COMMENT ON TABLE "V_SIT_BIL_CDS_CNR"  IS 'Situazione di bilancio entrate/spese CNR raggruppata per CDS.
Per quanto rigurda le spese e'' necessario calcolare anche l''ACCREDITATO
calcolato come "mandati emessi aventi imputazione contabile con articoli
contenenti le nature "attività vincolate" e "attività conto terzi""
E'' utilizzata dal mandato Accreditamento CNR-CdS per visualizzare i prospetti con la
situazione del bilancio entrate/spese del CNR per un certo CDS
Situazione capillare su mastrini di spesa ed entrata';
