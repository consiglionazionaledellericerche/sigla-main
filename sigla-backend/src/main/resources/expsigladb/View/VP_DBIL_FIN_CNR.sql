--------------------------------------------------------
--  DDL for View VP_DBIL_FIN_CNR
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VP_DBIL_FIN_CNR" ("ESERCIZIO", "TI_APPARTENENZA", "TI_GESTIONE", "CD_PARTE", "CDC_TITOLO", "CDC_CATEGORIA", "CDC_SEZIONE", "CDC_CAPITOLO", "CDC_ARTICOLO", "CD_VOCE", "DS_CAPITOLO", "CD_UNITA_ORGANIZZATIVA", "TI_VOCE", "DS_VOCE", "DS_PARTE", "CD_TITOLO", "DS_TITOLO", "CD_CATEGORIA", "DS_CATEGORIA", "CD_FUNZIONE", "DS_FUNZIONE", "CD_CDS", "DS_CDS", "CD_NATURA", "DS_NATURA", "CD_PROPRIO_VOCE", "FL_MASTRINO", "LIVELLO", "CD_TITOLO_CAPITOLO", "DS_TITOLO_CAPITOLO", "CD_SEZIONE_CAPITOLO", "CD_CENTRO_RESPONSABILITA", "IM_STANZ_ES_PREC_ASS", "IM_RES_INIZIALE", "IM_RES_VAR_PIU", "IM_RES_VAR_MENO", "IM_STANZ_INIZIALE_A1", "IM_STANZ_VAR_PIU", "IM_STANZ_VAR_MENO", "IM_STANZ_INIZIALE_A2", "IM_STANZ_INIZIALE_A3") AS 
  (Select a.esercizio, a.ti_appartenenza, a.ti_gestione, b.cd_parte,
           b.cd_parte||'.'||Substr(a.cd_voce, 3, 2),
           b.cd_parte||'.'||Substr(a.cd_voce, 3, 2)||'.'||b.cd_categoria,
           b.cd_parte||'.'||Substr(a.cd_voce, 3, 2)||'.'||b.cd_categoria||'.'||b.cd_funzione,
           b.cd_parte||'.'||Substr(a.cd_voce, 3, 2)||'.'||b.cd_categoria||'.'||b.cd_funzione||'.'||b.cd_cds,
           b.cd_parte||'.'||Substr(a.cd_voce, 3, 2)||'.'||b.cd_categoria||'.'||b.cd_funzione||'.'||b.cd_cds||'.'||b.cd_natura,
           a.cd_voce,
           acds.ds_unita_organizzativa,
           b.cd_unita_organizzativa,
           b.ti_voce,
           SUBSTR(b.ds_voce, 1, 100), 'Parte '||b.cd_parte,
           SUBSTR(a.cd_voce, 3, 2),
           SUBSTR(aevtitolo.ds_elemento_voce, 1, 100), b.cd_categoria,
           SUBSTR(aevcategoria.ds_elemento_voce, 1, 100), b.cd_funzione,
           af.ds_funzione, b.cd_cds,
           SUBSTR(acds.ds_unita_organizzativa, 1, 100),
           b.cd_natura,
           an.ds_natura, b.cd_proprio_voce, b.fl_mastrino, b.livello,
           b.cd_titolo_capitolo,
           SUBSTR(aevcategoria.ds_elemento_voce, 1, 100),
           b.cd_sezione_capitolo,
           b.cd_centro_responsabilita,
           (Select Nvl(Sum(Nvl(im_stanz_iniziale_a1, 0)+Nvl(variazioni_piu, 0)-Nvl(variazioni_meno, 0)), 0)
            From   voce_f_saldi_cdr_linea
            Where  esercizio       = a.esercizio-1 And
                   ti_appartenenza = a.ti_appartenenza And
                   ti_gestione     = a.ti_gestione And
                   cd_voce         = a.cd_voce And
                   esercizio       = esercizio_res), -- Assestato anno precedente
           Sum(Nvl(a.IM_OBBL_RES_PRO, 0)),     -- Residui iniziali
           Sum(Nvl(a.VAR_PIU_OBBL_RES_PRO, 0)),
           Sum(Nvl(a.VAR_MENO_OBBL_RES_PRO, 0)),
           Sum(Nvl(a.im_stanz_iniziale_a1, 0)),
           Sum(Nvl(a.variazioni_piu, 0)),            -- Var +
           Sum(Nvl(a.variazioni_meno, 0)),           -- Var -
           Sum(Nvl(a.im_stanz_iniziale_a2, 0)),      -- Competenza anno corrente + 1
           Sum(Nvl(a.im_stanz_iniziale_a3, 0))       -- Competenza anno corrente + 2
      FROM voce_f_saldi_cdr_linea a,
           voce_f b,
           funzione af,
           natura an,
           unita_organizzativa acds,
           elemento_voce aevcategoria,
           elemento_voce aevtitolo
     WHERE b.esercizio                    = a.esercizio
       AND b.ti_appartenenza              = a.ti_appartenenza
       AND b.ti_gestione                  = a.ti_gestione
       AND b.cd_voce                      = a.cd_voce
       And A.TI_APPARTENENZA              = 'C'
       AND af.cd_funzione(+)              = b.cd_funzione
       AND an.cd_natura(+)                = b.cd_natura
       AND a.ti_gestione                  = 'S'
       AND b.cd_categoria                 = '1'
       AND b.cd_parte                     = '1'
   -- Dati esercizio precedente -- NOn e detto che esistano (come primo anno)
       AND acds.cd_unita_organizzativa(+) = b.cd_cds
       AND acds.fl_cds(+)                 = 'Y'
       AND aevtitolo.esercizio            = b.esercizio
       AND aevtitolo.ti_appartenenza      = a.ti_appartenenza
       AND aevtitolo.ti_gestione          = a.ti_gestione
       AND aevtitolo.cd_elemento_voce     = SUBSTR(a.cd_voce, 1, 4)
       AND aevcategoria.esercizio         = b.esercizio
       AND aevcategoria.ti_appartenenza   = a.ti_appartenenza
       AND aevcategoria.ti_gestione       = a.ti_gestione
       AND aevcategoria.cd_elemento_voce  = b.cd_titolo_capitolo
    Group By a.esercizio, a.ti_appartenenza, a.ti_gestione, b.cd_parte,
             b.cd_parte||'.'||Substr(a.cd_voce, 3, 2),
             b.cd_parte||'.'||Substr(a.cd_voce, 3, 2)||'.'||b.cd_categoria,
             b.cd_parte||'.'||Substr(a.cd_voce, 3, 2)||'.'||b.cd_categoria||'.'||b.cd_funzione,
             b.cd_parte||'.'||Substr(a.cd_voce, 3, 2)||'.'||b.cd_categoria||'.'||b.cd_funzione||'.'||b.cd_cds,
             b.cd_parte||'.'||Substr(a.cd_voce, 3, 2)||'.'||b.cd_categoria||'.'||b.cd_funzione||'.'||b.cd_cds||'.'||b.cd_natura,
             a.cd_voce,
             acds.ds_unita_organizzativa,
             b.cd_unita_organizzativa,
             b.ti_voce,
             SUBSTR(b.ds_voce, 1, 100), 'Parte '||b.cd_parte,
             SUBSTR(a.cd_voce, 3, 2),
             SUBSTR(aevtitolo.ds_elemento_voce, 1, 100), b.cd_categoria,
             SUBSTR(aevcategoria.ds_elemento_voce, 1, 100), b.cd_funzione,
             af.ds_funzione, b.cd_cds,
             SUBSTR(acds.ds_unita_organizzativa, 1, 100),
             b.cd_natura,
             an.ds_natura, b.cd_proprio_voce, b.fl_mastrino, b.livello,
             b.cd_titolo_capitolo,
             SUBSTR(aevcategoria.ds_elemento_voce, 1, 100),
             b.cd_sezione_capitolo,
             b.cd_centro_responsabilita
-- Nella categtoria 1 il titolo capitolo e il codice completo della categoria stessa
    UNION ALL
-- PARTE 1 CATEGORIA 2
    SELECT a.esercizio, a.ti_appartenenza, a.ti_gestione, b.cd_parte,
           b.cd_parte||'.'||SUBSTR(a.cd_voce, 3, 2),
           b.cd_parte||'.'||SUBSTR(a.cd_voce, 3, 2)||'.'||b.cd_categoria,
           b.cd_parte||'.'||SUBSTR(a.cd_voce, 3, 2)||'.'||b.cd_categoria||'.'||b.cd_funzione,
           b.cd_parte||'.'||SUBSTR(a.cd_voce, 3, 2)||'.'||b.cd_categoria||'.'||b.cd_funzione||'.'||aev.cd_proprio_elemento,
           b.cd_parte||'.'||SUBSTR(a.cd_voce, 3, 2)||'.'||b.cd_categoria||'.'||b.cd_funzione||'.'||aev.cd_proprio_elemento||'.'||b.cd_natura,
           a.cd_voce, aev.ds_elemento_voce, b.cd_unita_organizzativa,
           b.ti_voce, SUBSTR(b.ds_voce, 1, 100),
           'Parte '||b.cd_parte,
           SUBSTR(a.cd_voce, 3, 2),
           aevtitolo.ds_elemento_voce,
           b.cd_categoria, aevcategoria.ds_elemento_voce, b.cd_funzione,
           af.ds_funzione, b.cd_cds,
           SUBSTR(acds.ds_unita_organizzativa, 1, 100), b.cd_natura,
           an.ds_natura, b.cd_proprio_voce, b.fl_mastrino, b.livello,
           b.cd_titolo_capitolo, SUBSTR(aev.ds_elemento_voce, 1, 100),
           b.cd_sezione_capitolo, b.cd_centro_responsabilita,
           (Select Nvl(Sum(Nvl(im_stanz_iniziale_a1, 0)+Nvl(variazioni_piu, 0)-Nvl(variazioni_meno, 0)), 0)
            From   voce_f_saldi_cdr_linea
            Where  esercizio       = a.esercizio-1 And
                   ti_appartenenza = a.ti_appartenenza And
                   ti_gestione     = a.ti_gestione And
                   cd_voce         = a.cd_voce And
                   esercizio       = esercizio_res), -- Assestato anno precedente
           Sum(Nvl(a.IM_OBBL_RES_PRO, 0)),     -- Residui iniziali
           Sum(Nvl(a.VAR_PIU_OBBL_RES_PRO, 0)),
           Sum(Nvl(a.VAR_MENO_OBBL_RES_PRO, 0)),
           Sum(Nvl(a.im_stanz_iniziale_a1, 0)),
           Sum(Nvl(a.variazioni_piu, 0)),            -- Var +
           Sum(Nvl(a.variazioni_meno, 0)),           -- Var -
           Sum(Nvl(a.im_stanz_iniziale_a2, 0)),      -- Competenza anno corrente + 1
           Sum(Nvl(a.im_stanz_iniziale_a3, 0))       -- Competenza anno corrente + 2
      FROM voce_f_saldi_cdr_linea a,
           voce_f b,
           funzione af,
           natura an,
           unita_organizzativa acds,
           elemento_voce aev,                        -- Tipologia di intervento
           elemento_voce aevcategoria,
           elemento_voce aevtitolo
     WHERE b.esercizio = a.esercizio
       AND b.ti_appartenenza = a.ti_appartenenza
       AND b.ti_gestione = a.ti_gestione
       AND b.cd_voce = a.cd_voce
       And A.TI_APPARTENENZA              = 'C'
       AND af.cd_funzione(+) = b.cd_funzione
       AND an.cd_natura(+) = b.cd_natura
       AND a.ti_gestione = 'S'
       AND b.cd_categoria = '2'
       AND b.cd_parte = '1'
   -- Dati esercizio precedente -- NOn e detto che esistano (come primo anno)
       AND acds.cd_unita_organizzativa(+) = b.cd_cds
       AND acds.fl_cds(+) = 'Y'
       AND aev.esercizio = b.esercizio
       AND aev.ti_appartenenza = a.ti_appartenenza
       AND aev.ti_gestione = a.ti_gestione
       AND aev.cd_elemento_voce = b.cd_titolo_capitolo
       AND aevtitolo.esercizio = b.esercizio
       AND aevtitolo.ti_appartenenza = a.ti_appartenenza
       AND aevtitolo.ti_gestione = a.ti_gestione
       AND aevtitolo.cd_elemento_voce = SUBSTR(a.cd_voce, 1, 4)
       AND aevcategoria.esercizio = b.esercizio
       AND aevcategoria.ti_appartenenza = a.ti_appartenenza
       AND aevcategoria.ti_gestione = a.ti_gestione
       AND aevcategoria.cd_elemento_voce = Substr(a.cd_voce, 1, 4)||'.'||b.cd_categoria
     Group By a.esercizio, a.ti_appartenenza, a.ti_gestione, b.cd_parte,
           b.cd_parte||'.'||SUBSTR(a.cd_voce, 3, 2),
           b.cd_parte||'.'||SUBSTR(a.cd_voce, 3, 2)||'.'||b.cd_categoria,
           b.cd_parte||'.'||SUBSTR(a.cd_voce, 3, 2)||'.'||b.cd_categoria||'.'||b.cd_funzione,
           b.cd_parte||'.'||SUBSTR(a.cd_voce, 3, 2)||'.'||b.cd_categoria||'.'||b.cd_funzione||'.'||aev.cd_proprio_elemento,
           b.cd_parte||'.'||SUBSTR(a.cd_voce, 3, 2)||'.'||b.cd_categoria||'.'||b.cd_funzione||'.'||aev.cd_proprio_elemento||'.'||b.cd_natura,
           a.cd_voce, aev.ds_elemento_voce, b.cd_unita_organizzativa,
           b.ti_voce, SUBSTR(b.ds_voce, 1, 100),
           'Parte '||b.cd_parte,
           SUBSTR(a.cd_voce, 3, 2),
           aevtitolo.ds_elemento_voce,
           b.cd_categoria, aevcategoria.ds_elemento_voce, b.cd_funzione,
           af.ds_funzione, b.cd_cds,
           SUBSTR(acds.ds_unita_organizzativa, 1, 100), b.cd_natura,
           an.ds_natura, b.cd_proprio_voce, b.fl_mastrino, b.livello,
           b.cd_titolo_capitolo, SUBSTR(aev.ds_elemento_voce, 1, 100),
           b.cd_sezione_capitolo, b.cd_centro_responsabilita
              -- Nella categtoria 2 devo estrarre l'elemento_voce di categoria
    UNION ALL
-- PARTE 2
    SELECT a.esercizio, a.ti_appartenenza, a.ti_gestione, b.cd_parte, '*',
           '*', '*', a.cd_voce, '*', a.cd_voce, b.ds_voce,
           b.cd_unita_organizzativa, b.ti_voce, SUBSTR(b.ds_voce, 1, 100),
           'Parte '||b.cd_parte, '*', NULL, '*', NULL, b.cd_funzione, NULL,
           b.cd_cds, NULL, b.cd_natura, NULL, b.cd_proprio_voce,
           b.fl_mastrino, b.livello, b.cd_titolo_capitolo, b.ds_voce,
           b.cd_sezione_capitolo, b.cd_centro_responsabilita,
           (Select Nvl(Sum(Nvl(im_stanz_iniziale_a1, 0)+Nvl(variazioni_piu, 0)-Nvl(variazioni_meno, 0)), 0)
            From   voce_f_saldi_cdr_linea
            Where  esercizio       = a.esercizio-1 And
                   ti_appartenenza = a.ti_appartenenza And
                   ti_gestione     = a.ti_gestione And
                   cd_voce         = a.cd_voce And
                   esercizio       = esercizio_res),        -- Iniziale anno precedente
           Sum(Nvl(a.IM_OBBL_RES_PRO, 0)),     -- Residui iniziali
           Sum(Nvl(a.VAR_PIU_OBBL_RES_PRO, 0)),
           Sum(Nvl(a.VAR_MENO_OBBL_RES_PRO, 0)),
           Sum(Nvl(a.im_stanz_iniziale_a1, 0)),
           Sum(Nvl(a.variazioni_piu, 0)),            -- Var +
           Sum(Nvl(a.variazioni_meno, 0)),           -- Var -
           Sum(Nvl(a.im_stanz_iniziale_a2, 0)),      -- Competenza anno corrente + 1
           Sum(Nvl(a.im_stanz_iniziale_a3, 0))       -- Competenza anno corrente + 2
      FROM voce_f_saldi_cdr_linea a,
           voce_f b
     WHERE b.esercizio = a.esercizio
       AND b.ti_appartenenza = a.ti_appartenenza
       AND b.ti_gestione = a.ti_gestione
       AND b.cd_voce = a.cd_voce
       And A.TI_APPARTENENZA              = 'C'
       AND a.ti_gestione = 'S'
       AND b.cd_parte = '2'
    Group By  a.esercizio, a.ti_appartenenza, a.ti_gestione, b.cd_parte, '*',
              '*', '*', a.cd_voce, '*', a.cd_voce, b.ds_voce,
              b.cd_unita_organizzativa, b.ti_voce, SUBSTR(b.ds_voce, 1, 100),
              'Parte '||b.cd_parte, '*', NULL, '*', NULL, b.cd_funzione, NULL,
              b.cd_cds, NULL, b.cd_natura, NULL, b.cd_proprio_voce,
              b.fl_mastrino, b.livello, b.cd_titolo_capitolo, b.ds_voce,
              b.cd_sezione_capitolo, b.cd_centro_responsabilita
    Union ALL
    SELECT
-- PARTE ENTRATE CNR
           a.esercizio, a.ti_appartenenza, a.ti_gestione, b.cd_parte,
           SUBSTR(a.cd_voce, 1, 2),
           SUBSTR(a.cd_voce, 1, 2)||'.'||b.cd_categoria, '*',
           b.cd_voce_padre, '*', a.cd_voce, aev.ds_elemento_voce,
           b.cd_unita_organizzativa, b.ti_voce, SUBSTR(b.ds_voce, 1, 100),
           '*', NULL, aevtitolo.ds_elemento_voce, b.cd_categoria,
           aevcategoria.ds_elemento_voce, b.cd_funzione, NULL, b.cd_cds,
           SUBSTR(acds.ds_unita_organizzativa, 1, 100), b.cd_natura, NULL,
           b.cd_proprio_voce, b.fl_mastrino, b.livello, b.cd_titolo_capitolo,
           SUBSTR(aev.ds_elemento_voce, 1, 100), b.cd_sezione_capitolo,
           b.cd_centro_responsabilita,
           (Select Nvl(Sum(Nvl(im_stanz_iniziale_a1, 0)+Nvl(variazioni_piu, 0)-Nvl(variazioni_meno, 0)), 0)
            From   voce_f_saldi_cdr_linea
            Where  esercizio       = a.esercizio-1 And
                   ti_appartenenza = a.ti_appartenenza And
                   ti_gestione     = a.ti_gestione And
                   cd_voce         = a.cd_voce And
                   esercizio       = esercizio_res),          -- Iniziale anno precedente
           Sum(Nvl(a.IM_OBBL_RES_PRO, 0)),     -- Residui iniziali
           Sum(Nvl(a.VAR_PIU_OBBL_RES_PRO, 0)),
           Sum(Nvl(a.VAR_MENO_OBBL_RES_PRO, 0)),
           Sum(Nvl(a.im_stanz_iniziale_a1, 0)),
           Sum(Nvl(a.variazioni_piu, 0)),            -- Var +
           Sum(Nvl(a.variazioni_meno, 0)),           -- Var -
           Sum(Nvl(a.im_stanz_iniziale_a2, 0)),      -- Competenza anno corrente + 1
           Sum(Nvl(a.im_stanz_iniziale_a3, 0))       -- Competenza anno corrente + 2
      FROM voce_f_saldi_cdr_linea a,
           voce_f b,
           natura an,
           unita_organizzativa acds,
           elemento_voce aev,
           elemento_voce aevtitolo,
           elemento_voce aevcategoria
     WHERE b.esercizio = a.esercizio
       AND b.ti_appartenenza = a.ti_appartenenza
       AND b.ti_gestione = a.ti_gestione
       AND b.cd_voce = a.cd_voce
       AND an.cd_natura(+) = b.cd_natura
       AND a.ti_gestione = 'E'
       And A.TI_APPARTENENZA              = 'C'
       AND acds.cd_unita_organizzativa(+) = b.cd_cds
       AND acds.fl_cds(+) = 'Y'
       AND aev.esercizio(+) = b.esercizio
       AND aev.ti_appartenenza(+) = b.ti_appartenenza
       AND aev.ti_gestione(+) = b.ti_gestione
       AND aev.cd_elemento_voce(+) = b.cd_titolo_capitolo
       AND aevtitolo.esercizio = b.esercizio
       AND aevtitolo.ti_appartenenza = a.ti_appartenenza
       AND aevtitolo.ti_gestione = a.ti_gestione
       AND aevtitolo.cd_elemento_voce = SUBSTR(a.cd_voce, 1, 2)
       AND aevcategoria.esercizio = b.esercizio
       AND aevcategoria.ti_appartenenza = a.ti_appartenenza
       AND aevcategoria.ti_gestione = a.ti_gestione
       AND aevcategoria.cd_elemento_voce = Substr(a.cd_voce, 1, 2)||'.'||b.cd_categoria
   Group By a.esercizio, a.ti_appartenenza, a.ti_gestione, b.cd_parte,
           SUBSTR(a.cd_voce, 1, 2),
           SUBSTR(a.cd_voce, 1, 2)||'.'||b.cd_categoria, '*',
           b.cd_voce_padre, '*', a.cd_voce, aev.ds_elemento_voce,
           b.cd_unita_organizzativa, b.ti_voce, SUBSTR(b.ds_voce, 1, 100),
           '*', NULL, aevtitolo.ds_elemento_voce, b.cd_categoria,
           aevcategoria.ds_elemento_voce, b.cd_funzione, NULL, b.cd_cds,
           SUBSTR(acds.ds_unita_organizzativa, 1, 100), b.cd_natura, NULL,
           b.cd_proprio_voce, b.fl_mastrino, b.livello, b.cd_titolo_capitolo,
           SUBSTR(aev.ds_elemento_voce, 1, 100), b.cd_sezione_capitolo,
           b.cd_centro_responsabilita
              -- Nella categtoria 2 devo estrarre l'elemento_voce di categoria
);

   COMMENT ON TABLE "VP_DBIL_FIN_CNR"  IS 'Vista di stampa del bilancio finanziario CNR';
