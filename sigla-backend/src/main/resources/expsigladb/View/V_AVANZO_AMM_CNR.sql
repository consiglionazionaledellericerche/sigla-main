--------------------------------------------------------
--  DDL for View V_AVANZO_AMM_CNR
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_AVANZO_AMM_CNR" ("ESERCIZIO", "IM_AVANZO") AS 
  SELECT   esercizio, SUM (im_avanzo)
       FROM (Select esercizio,
                    NVL (DECODE (esercizio, 2003, im01, 0), 0) im_avanzo
               FROM configurazione_cnr
              WHERE cd_chiave_primaria = 'IMPORTO_SPECIALE'
                AND cd_chiave_secondaria = 'AVANZO_AMM_ES_PREC'
                AND esercizio = 2003
             UNION ALL
             -- comp ante 2006
             SELECT esercizio,
                    DECODE (ti_gestione,
                            'E', im_obblig_imp_acr,
                            -im_obblig_imp_acr
                           )
               FROM voce_f_saldi_cmp
              WHERE ti_competenza_residuo = 'C'
                AND cd_cds = '999'
                AND (SELECT NVL (fl_regolamento_2006, 'N')
                       FROM parametri_cnr
                      WHERE esercizio = voce_f_saldi_cmp.esercizio) = 'N'
             UNION ALL
             -- res ante 2006
             SELECT esercizio,
                    DECODE (ti_gestione,
                            'E', variazioni_piu - variazioni_meno,
                            - (variazioni_piu - variazioni_meno)
                           )
               FROM voce_f_saldi_cmp
              WHERE ti_competenza_residuo = 'R'
                AND cd_cds = '999'
                AND (SELECT NVL (fl_regolamento_2006, 'N')
                       FROM parametri_cnr
                      WHERE esercizio = voce_f_saldi_cmp.esercizio) = 'N'
             UNION ALL
             -- comp post 2006
             SELECT esercizio,
                    DECODE (ti_appartenenza,
                            'C', DECODE (ti_gestione,
                                         'E', im_obbl_acc_comp,
                                         -im_obbl_acc_comp
                                        ),
                            0
                           )
               FROM voce_f_saldi_cdr_linea
              WHERE esercizio = esercizio_res
                AND (SELECT NVL (fl_regolamento_2006, 'N')
                       FROM parametri_cnr
                      WHERE esercizio = voce_f_saldi_cdr_linea.esercizio) =
                                                                           'Y'
             UNION ALL
             -- res post 2006
             SELECT esercizio,
                    DECODE (ti_appartenenza,
                            'C', DECODE (ti_gestione,
                                         'E', var_piu_obbl_res_pro
                                          - var_meno_obbl_res_pro,
                                         - (  var_piu_obbl_res_pro
                                            - var_meno_obbl_res_pro
                                           )
                                        ),
                            0
                           )
               FROM voce_f_saldi_cdr_linea
              WHERE esercizio > esercizio_res
                AND (SELECT NVL (fl_regolamento_2006, 'N')
                       FROM parametri_cnr
                      WHERE esercizio = voce_f_saldi_cdr_linea.esercizio) =
                                                                           'Y')
   GROUP BY esercizio;

   COMMENT ON TABLE "V_AVANZO_AMM_CNR"  IS 'Vista di estrazione dell''ammontare di cassa del CNR';
