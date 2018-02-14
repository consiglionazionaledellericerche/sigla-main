--------------------------------------------------------
--  DDL for View VP_DBIL_FIN_CDS
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VP_DBIL_FIN_CDS" ("ESERCIZIO", "TI_APPARTENENZA", "TI_GESTIONE", "CD_PARTE", "CDC_TITOLO", "CDC_SEZIONE", "CDC_RUBRICA", "CDC_CAPITOLO", "CDC_ARTICOLO", "CD_CDS", "DS_UNITA_ORGANIZZATIVA", "CD_TIPO_UNITA", "DS_ELEMENTO_VOCE", "DS_TITOLO", "IM_STANZ_INIZIALE_AP", "IM_STANZ_INIZIALE", "VARIAZIONI_PIU", "VARIAZIONI_MENO") AS 
  Select UNION_TOTALE.esercizio,
        UNION_TOTALE.ti_appartenenza,
        UNION_TOTALE.ti_gestione,
        UNION_TOTALE.cd_parte,
        UNION_TOTALE.cdc_titolo,
        UNION_TOTALE.cdc_sezione,
        UNION_TOTALE.cdc_rubrica,
        UNION_TOTALE.cdc_capitolo,
        UNION_TOTALE.cdc_articolo,
        CNRUTL001.GETCDSFROMCDR(UNION_TOTALE.cd_centro_responsabilita) cd_cds,
        UO.ds_unita_organizzativa,
        UO.cd_tipo_unita,
        ELEMENTO_VOCE.ds_elemento_voce,
        UNION_TOTALE.ds_titolo,
       (Select Nvl(Sum(Nvl(im_stanz_iniziale_a1, 0)+Nvl(variazioni_piu, 0)-Nvl(variazioni_meno, 0)), 0)
        From   voce_f_saldi_cdr_linea
        Where  esercizio       = UNION_TOTALE.esercizio-1 And
               ti_appartenenza = UNION_TOTALE.ti_appartenenza And
               ti_gestione     = UNION_TOTALE.ti_gestione And
               cd_voce         = UNION_TOTALE.cd_voce And
               CNRUTL001.GETCDSFROMCDR(cd_centro_responsabilita) = CNRUTL001.GETCDSFROMCDR(UNION_TOTALE.cd_centro_responsabilita) And
               esercizio       = esercizio_res)       im_stanz_iniziale_ap, -- Assestato anno precedente
        UNION_TOTALE.im_stanz_iniziale,
        UNION_TOTALE.variazioni_piu,
        UNION_TOTALE.variazioni_meno
 From
   (-- SPESA CDS PARTE 1
    Select vsaldi.esercizio, vsaldi.ti_appartenenza, vsaldi.ti_gestione,
           vf.cd_parte,
           vf.cd_parte||'.'||Substr(vsaldi.cd_voce, 3, 2)  cdc_titolo,
           vf.cd_parte||'.'||SUBSTR(vsaldi.cd_voce, 3, 2)||'.'||vf.cd_funzione  cdc_sezione,
           vf.cd_parte||'.'||SUBSTR(vsaldi.cd_voce, 3, 2)||'.'||vf.cd_funzione||'.'||vf.cd_unita_organizzativa cdc_rubrica,
           SUBSTR(vsaldi.cd_voce, 1, 19)    cdc_capitolo,
           Decode(vf.cd_centro_responsabilita, NULL, NULL, vsaldi.cd_voce)  cdc_articolo,
           vsaldi.cd_centro_responsabilita,
           evt.ds_elemento_voce                                     ds_titolo,
           vsaldi.CD_ELEMENTO_VOCE,
           vsaldi.CD_VOCE,
           Sum(Nvl(vsaldi.im_stanz_iniziale_a1, 0))          im_stanz_iniziale,
           Sum(Nvl(vsaldi.variazioni_piu, 0))                variazioni_piu,
           Sum(Nvl(vsaldi.variazioni_meno, 0))               variazioni_meno
    From voce_f_saldi_cdr_linea vsaldi,
           voce_f vf,
           elemento_voce evt
    Where vsaldi.ti_appartenenza = 'D'
      AND vsaldi.ti_gestione = 'S'
      AND vf.esercizio = vsaldi.esercizio -- join con voce_f
      AND vf.ti_appartenenza = vsaldi.ti_appartenenza
      AND vf.ti_gestione = vsaldi.ti_gestione
      AND vf.cd_voce = vsaldi.cd_voce
      AND vf.cd_parte = '1'
      AND evt.esercizio = vsaldi.esercizio  -- join con elemento_voce (2/2)
      AND evt.ti_appartenenza = vsaldi.ti_appartenenza
      AND evt.ti_gestione = vsaldi.ti_gestione
      AND evt.cd_elemento_voce = SUBSTR(vsaldi.cd_voce, 1, 4)
    Group By vsaldi.esercizio, vsaldi.ti_appartenenza, vsaldi.ti_gestione,
           vf.cd_parte,
           vf.cd_parte||'.'||Substr(vsaldi.cd_voce, 3, 2),
           vf.cd_parte||'.'||SUBSTR(vsaldi.cd_voce, 3, 2)||'.'||vf.cd_funzione,
           vf.cd_parte||'.'||SUBSTR(vsaldi.cd_voce, 3, 2)||'.'||vf.cd_funzione||'.'||vf.cd_unita_organizzativa,
           SUBSTR(vsaldi.cd_voce, 1, 19),
           Decode(vf.cd_centro_responsabilita, NULL, NULL, vsaldi.cd_voce),
           vsaldi.cd_centro_responsabilita,
           evt.ds_elemento_voce,
           vsaldi.CD_ELEMENTO_VOCE,
           vsaldi.CD_VOCE
    Union All
    -- SPESA CDS PARTE 2
    Select vsaldi.esercizio,
           vsaldi.ti_appartenenza,
           vsaldi.ti_gestione,
           vf.cd_parte,
           NULL, NULL, NULL, vsaldi.cd_voce, NULL,
           vsaldi.cd_centro_responsabilita,
           NULL,
           vsaldi.CD_ELEMENTO_VOCE,
           vsaldi.CD_VOCE,
           Sum(Nvl(vsaldi.im_stanz_iniziale_a1, 0)),
           Sum(Nvl(vsaldi.variazioni_piu, 0)),
           Sum(Nvl(vsaldi.variazioni_meno, 0))
    From   voce_f_saldi_cdr_linea vsaldi,
           voce_f vf
    Where vsaldi.ti_appartenenza = 'D'
      AND vsaldi.ti_gestione = 'S'
      AND vf.esercizio = vsaldi.esercizio
      AND vf.ti_appartenenza = vsaldi.ti_appartenenza
      AND vf.ti_gestione = vsaldi.ti_gestione
      AND vf.cd_voce = vsaldi.cd_voce
      AND vf.cd_parte = '2'
    Group By  vsaldi.esercizio,
              vsaldi.ti_appartenenza,
              vsaldi.ti_gestione,
              vf.cd_parte,
              NULL, NULL, NULL, vsaldi.cd_voce, NULL,
              vsaldi.cd_centro_responsabilita,
              NULL,
              vsaldi.CD_ELEMENTO_VOCE,
              vsaldi.CD_VOCE
    Union All
    -- ENTRATA CDS
    Select vsaldi.esercizio, vsaldi.ti_appartenenza,
           vsaldi.ti_gestione,
           NULL,
           SUBSTR(vsaldi.cd_voce, 1, 2),
           NULL,
           NULL,
           vsaldi.cd_voce,
           NULL,
           vsaldi.cd_centro_responsabilita,
           evt.ds_elemento_voce,
           vsaldi.CD_ELEMENTO_VOCE,
           vsaldi.CD_VOCE,
           Sum(Nvl(vsaldi.im_stanz_iniziale_a1, 0)),
           Sum(Nvl(vsaldi.variazioni_piu, 0)),
           Sum(Nvl(vsaldi.variazioni_meno, 0))
    From   voce_f_saldi_cdr_linea vsaldi,
           voce_f vf,
           elemento_voce evt
    Where  vsaldi.ti_appartenenza = 'D'
       AND vsaldi.ti_gestione     = 'E'
       AND vf.esercizio           = vsaldi.esercizio
       AND vf.ti_appartenenza     = vsaldi.ti_appartenenza
       AND vf.ti_gestione         = vsaldi.ti_gestione
       AND vf.cd_voce             = vsaldi.cd_voce
       AND evt.esercizio          = vsaldi.esercizio
       AND evt.ti_appartenenza    = vsaldi.ti_appartenenza
       AND evt.ti_gestione        = vsaldi.ti_gestione
       AND evt.cd_elemento_voce   = SUBSTR(vsaldi.cd_voce, 1, 2)
    Group By vsaldi.esercizio, vsaldi.ti_appartenenza,
           vsaldi.ti_gestione,
           NULL,
           SUBSTR(vsaldi.cd_voce, 1, 2),
           NULL,
           NULL,
           vsaldi.cd_voce,
           NULL,
           vsaldi.cd_centro_responsabilita,
           evt.ds_elemento_voce,
           vsaldi.CD_ELEMENTO_VOCE,
           vsaldi.CD_VOCE) UNION_TOTALE, unita_organizzativa uo, ELEMENTO_VOCE
Where uo.cd_unita_organizzativa      = CNRUTL001.GETCDSFROMCDR(UNION_TOTALE.cd_centro_responsabilita) And
      ELEMENTO_VOCE.ESERCIZIO        = UNION_TOTALE.ESERCIZIO     And
      ELEMENTO_VOCE.TI_APPARTENENZA  = UNION_TOTALE.TI_APPARTENENZA And
      ELEMENTO_VOCE.TI_GESTIONE      = UNION_TOTALE.TI_GESTIONE     And
      ELEMENTO_VOCE.CD_ELEMENTO_VOCE = UNION_TOTALE.CD_ELEMENTO_VOCE;

   COMMENT ON TABLE "VP_DBIL_FIN_CDS"  IS 'Vista di stampa del bilancio finanziario dei CdS';
