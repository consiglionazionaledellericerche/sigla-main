CREATE OR REPLACE PROCEDURE PRC_LOAD_TABLE_STAMPA_BILANCIO(P_ESERCIZIO IN NUMBER,
                                                           P_AGG_PREVISIONE_AC IN char,
                                                           P_AGG_RESIDUI_AC IN char,
                                                           P_AGG_PREVISIONE_AP IN char,
                                                           P_AGG_RESIDUI_AP IN char,
                                                           P_AGG_CASSA_AC IN char,
                                                           P_PERC_CASSA IN NUMBER,
                                                           P_UTCR IN VARCHAR2) IS
  parEnte PARAMETRI_ENTE%Rowtype;
BEGIN
  IF P_ESERCIZIO<2016 THEN
     RETURN;
  END IF;
  
  parEnte := CNRUTL001.getRecParametriEnteAttivo;

  IF Nvl(P_AGG_PREVISIONE_AC,'N')='Y' THEN
     UPDATE PDG_DATI_STAMPA_BILANCIO_TEMP
     SET IM_PREVISIONE_AC = 0,
         UTUV = P_UTCR,
         DUVA = SYSDATE,
         PG_VER_REC = PG_VER_REC + 1
     WHERE ESERCIZIO = P_ESERCIZIO;

     For rec in (SELECT a.CD_CENTRO_RESPONSABILITA, 'S' TI_GESTIONE, c.CD_LIVELLO6, d.CD_PROGRAMMA, d.CD_MISSIONE,
                        SUM ( NVL(a.im_spese_gest_decentrata_int, 0) + NVL(a.im_spese_gest_decentrata_est, 0) +
                                NVL(a.im_spese_gest_accentrata_int, 0) + NVL(a.im_spese_gest_accentrata_est, 0)) IM_PREVISIONE_AC
                  FROM pdg_modulo_spese_gest a,
                       elemento_voce b,
                       v_linea_attivita_valida d,
                       v_classificazione_voci_all c
                 WHERE P_ESERCIZIO = 2016
                 AND   a.esercizio = P_ESERCIZIO
                 AND   a.esercizio = b.esercizio
                 AND   a.ti_appartenenza = b.ti_appartenenza
                 AND   a.ti_gestione = b.ti_gestione
                 AND   a.cd_elemento_voce = b.cd_elemento_voce
                 AND   a.esercizio = d.esercizio
                 AND   a.cd_cdr_assegnatario = d.cd_centro_responsabilita
                 AND   a.cd_linea_attivita = d.cd_linea_attivita
                 AND   b.id_classificazione = c.id_classificazione
                 AND   a.cd_cdr_assegnatario_clgs is null
                 GROUP BY a.CD_CENTRO_RESPONSABILITA, c.CD_LIVELLO6, d.CD_PROGRAMMA, d.CD_MISSIONE
                 UNION ALL
                 SELECT a.CD_CENTRO_RESPONSABILITA,'S', c.CD_LIVELLO6, areaprog.CD_DIPARTIMENTO, a.CD_MISSIONE,
                        SUM ( NVL(a.im_spese_gest_decentrata_int, 0) + NVL(a.im_spese_gest_decentrata_est, 0) +
                              NVL(a.im_spese_gest_accentrata_int, 0) + NVL(a.im_spese_gest_accentrata_est, 0)) IM_PREVISIONE_AC
                  FROM pdg_modulo_spese a,
                       v_classificazione_voci_all c,
                       progetto_prev progetto,
                       progetto_prev areaprog
                 WHERE P_ESERCIZIO > 2016
                 AND   a.esercizio = P_ESERCIZIO
                 AND   a.id_classificazione = c.id_classificazione
                 AND   a.esercizio = progetto.esercizio
                 AND   a.pg_progetto = progetto.pg_progetto
                 AND   progetto.esercizio_progetto_padre = areaprog.esercizio
                 AND   progetto.pg_progetto_padre = areaprog.pg_progetto
                 GROUP BY a.CD_CENTRO_RESPONSABILITA, c.CD_LIVELLO6, areaprog.CD_DIPARTIMENTO, a.CD_MISSIONE
                 UNION ALL
                 SELECT a.CD_CENTRO_RESPONSABILITA, 'E', c.CD_LIVELLO6, areaprog.CD_DIPARTIMENTO, null, 
                        SUM ( NVL (a.im_entrata_app, NVL (a.im_entrata, 0))) IM_PREVISIONE_AC
                  FROM pdg_modulo_entrate a,
                       v_classificazione_voci_all c,
                       progetto_prev progetto,
                       progetto_prev areaprog
                 WHERE a.esercizio = P_ESERCIZIO
                 AND   a.id_classificazione = c.id_classificazione
                 AND   a.esercizio = progetto.esercizio
                 AND   a.pg_progetto = progetto.pg_progetto
                 AND   progetto.esercizio_progetto_padre = areaprog.esercizio
                 AND   progetto.pg_progetto_padre = areaprog.pg_progetto
                 GROUP BY a.CD_CENTRO_RESPONSABILITA, c.CD_LIVELLO6, areaprog.CD_DIPARTIMENTO) Loop
           
        Update PDG_DATI_STAMPA_BILANCIO_TEMP
        set IM_PREVISIONE_AC = rec.IM_PREVISIONE_AC,
            UTUV = P_UTCR,
            DUVA = SYSDATE,
            PG_VER_REC = PG_VER_REC + 1
        where ESERCIZIO = P_ESERCIZIO
        and   CD_CENTRO_RESPONSABILITA = rec.CD_CENTRO_RESPONSABILITA
        and   TI_GESTIONE = rec.TI_GESTIONE
        and   CD_ELEMENTO_VOCE = rec.CD_LIVELLO6
        and   ((rec.CD_PROGRAMMA IS NULL AND CD_PROGRAMMA IS NULL) OR CD_PROGRAMMA = rec.CD_PROGRAMMA)
        and   ((rec.CD_MISSIONE IS NULL AND CD_MISSIONE IS NULL) OR CD_MISSIONE = rec.CD_MISSIONE);

        If sql%rowcount=0 Then
           INSERT INTO PDG_DATI_STAMPA_BILANCIO_TEMP
             (ESERCIZIO, CD_CENTRO_RESPONSABILITA, TI_GESTIONE, CD_ELEMENTO_VOCE, CD_PROGRAMMA, CD_MISSIONE,
              IM_RESIDUI_AC, IM_PREVISIONE_AC, IM_CASSA_AC, IM_RESIDUI_AP, UTCR, DACR, UTUV, DUVA, PG_VER_REC)
           VALUES
             (P_ESERCIZIO, rec.CD_CENTRO_RESPONSABILITA, rec.TI_GESTIONE, rec.CD_LIVELLO6, 
              rec.cd_programma, rec.CD_MISSIONE, 0, rec.IM_PREVISIONE_AC, 0, 0, P_UTCR, SYSDATE, P_UTCR, SYSDATE, 1);
        End If;
     End Loop;                          
  END IF;
  
  IF P_ESERCIZIO>2016 THEN
    IF Nvl(P_AGG_RESIDUI_AC,'N')='Y' THEN
       UPDATE PDG_DATI_STAMPA_BILANCIO
       SET IM_RESIDUI_AC = 0,
           UTUV = P_UTCR,
           DUVA = SYSDATE,
           PG_VER_REC = PG_VER_REC + 1
       WHERE ESERCIZIO = P_ESERCIZIO;

       UPDATE PDG_DATI_STAMPA_BILANCIO_TEMP
       SET IM_RESIDUI_AC = 0,
           UTUV = P_UTCR,
           DUVA = SYSDATE,
           PG_VER_REC = PG_VER_REC + 1
       WHERE ESERCIZIO = P_ESERCIZIO;

       --CARICO I DATI RESIDUI ANNO CORRENTE
       For rec in (select CD_CENTRO_RESPONSABILITA, TI_GESTIONE, CD_ELEMENTO_VOCE, 
                          CD_PROGRAMMA, CD_MISSIONE, NVL(SUM(IM_RESIDUO_AC), 0) IM_RESIDUO_AC,
                          NVL(SUM(IM_RESIDUO_AC_SOLO_IMP), 0) IM_RESIDUO_AC_SOLO_IMP
                    FROM (select A.CD_CENTRO_RESPONSABILITA, A.TI_GESTIONE, 
                                 NVL((SELECT CD_ELEMENTO_VOCE_NEW
                                      FROM ASS_EVOLD_EVNEW
                                      WHERE ESERCIZIO_OLD = A.ESERCIZIO
                                      AND   TI_APPARTENENZA_OLD = A.TI_APPARTENENZA
                                      AND   TI_GESTIONE_OLD = A.TI_GESTIONE
                                      AND   CD_ELEMENTO_VOCE_OLD = A.CD_ELEMENTO_VOCE), A.CD_ELEMENTO_VOCE) CD_ELEMENTO_VOCE,
                                 linea.cd_programma, linea.cd_missione, 
                                 nvl(case when a.esercizio = a.esercizio_res
                                          then NVL (im_stanz_iniziale_a1, 0) + NVL (variazioni_piu, 0) - NVL (variazioni_meno, 0) 
                                               - NVL (im_mandati_reversali_pro, 0)
                                          else case when e.descrizione='CNR'
                                                    then NVL (im_stanz_res_improprio, 0)
                                                         + NVL (var_piu_stanz_res_imp, 0) - NVL (var_meno_stanz_res_imp, 0 )
                                                         + NVL (im_obbl_res_pro, 0)
                                                         - NVL (im_mandati_reversali_pro, 0) - NVL (im_mandati_reversali_imp, 0)
                                                    else NVL (im_obbl_res_pro, 0)
                                                         - NVL (im_mandati_reversali_pro, 0) - NVL (im_mandati_reversali_imp, 0)
                                                    end
                                          end, 0) IM_RESIDUO_AC,
                                 nvl(case when a.esercizio = a.esercizio_res
                                          then NVL (im_obbl_acc_comp, 0) - NVL (im_mandati_reversali_pro, 0)
                                          else NVL (im_obbl_res_pro, 0) + NVL (im_obbl_res_imp, 0) 
                                               - NVL (im_mandati_reversali_pro, 0) - NVL (im_mandati_reversali_imp, 0)
                                          end, 0) IM_RESIDUO_AC_SOLO_IMP
                          from voce_f_saldi_cdr_linea a,
                               v_linea_attivita_valida linea,
                               parametri_ente e
                          where a.esercizio = P_ESERCIZIO-1
                          and   a.esercizio = linea.esercizio (+)
                          and   a.cd_centro_responsabilita = linea.cd_centro_responsabilita (+)
                          and   a.cd_linea_attivita = linea.cd_linea_attivita (+)
                          and   e.attivo = 'Y')
                   group by CD_CENTRO_RESPONSABILITA, TI_GESTIONE, CD_ELEMENTO_VOCE, CD_PROGRAMMA, CD_MISSIONE) loop
        Declare
          im_residuo saldi_stanziamenti.im_stanz_iniziale_a1%type;
        Begin
          if parEnte.descrizione='CNR' Then
            im_residuo := rec.IM_RESIDUO_AC;
          else
            im_residuo := rec.IM_RESIDUO_AC_SOLO_IMP;
          END IF;

          Update PDG_DATI_STAMPA_BILANCIO
          set IM_RESIDUI_AC = im_residuo
          where ESERCIZIO = P_ESERCIZIO
          and   CD_CENTRO_RESPONSABILITA = rec.CD_CENTRO_RESPONSABILITA
          and   TI_GESTIONE = rec.TI_GESTIONE
          and   CD_ELEMENTO_VOCE = rec.CD_ELEMENTO_VOCE
          and   ((rec.CD_PROGRAMMA IS NULL AND CD_PROGRAMMA IS NULL) OR CD_PROGRAMMA = rec.CD_PROGRAMMA)
          and   ((rec.CD_MISSIONE IS NULL AND CD_MISSIONE IS NULL) OR CD_MISSIONE = rec.CD_MISSIONE);

          If sql%rowcount=0 Then
            INSERT INTO PDG_DATI_STAMPA_BILANCIO
              (ESERCIZIO, CD_CENTRO_RESPONSABILITA, TI_GESTIONE, CD_ELEMENTO_VOCE, CD_PROGRAMMA, CD_MISSIONE,
               IM_RESIDUI_AC, IM_CASSA_AC, UTCR, DACR, UTUV, DUVA, PG_VER_REC)
            VALUES
              (P_ESERCIZIO, rec.CD_CENTRO_RESPONSABILITA, rec.TI_GESTIONE, rec.CD_ELEMENTO_VOCE, 
               rec.cd_programma, rec.CD_MISSIONE, im_residuo, 0, P_UTCR, SYSDATE, P_UTCR, SYSDATE, 1);
          End If;

          Update PDG_DATI_STAMPA_BILANCIO_TEMP
          set IM_RESIDUI_AC = im_residuo
          where ESERCIZIO = P_ESERCIZIO
          and   CD_CENTRO_RESPONSABILITA = rec.CD_CENTRO_RESPONSABILITA
          and   TI_GESTIONE = rec.TI_GESTIONE
          and   CD_ELEMENTO_VOCE = rec.CD_ELEMENTO_VOCE
          and   ((rec.CD_PROGRAMMA IS NULL AND CD_PROGRAMMA IS NULL) OR CD_PROGRAMMA = rec.CD_PROGRAMMA)
          and   ((rec.CD_MISSIONE IS NULL AND CD_MISSIONE IS NULL) OR CD_MISSIONE = rec.CD_MISSIONE);

          If sql%rowcount=0 Then
            INSERT INTO PDG_DATI_STAMPA_BILANCIO_TEMP
              (ESERCIZIO, CD_CENTRO_RESPONSABILITA, TI_GESTIONE, CD_ELEMENTO_VOCE, CD_PROGRAMMA, CD_MISSIONE,
               IM_RESIDUI_AC, IM_PREVISIONE_AC, IM_CASSA_AC, IM_RESIDUI_AP, UTCR, DACR, UTUV, DUVA, PG_VER_REC)
            VALUES
              (P_ESERCIZIO, rec.CD_CENTRO_RESPONSABILITA, rec.TI_GESTIONE, rec.CD_ELEMENTO_VOCE, 
               rec.cd_programma, rec.CD_MISSIONE, im_residuo, 0, 0, 0, P_UTCR, SYSDATE, P_UTCR, SYSDATE, 1);
          End If;
        End;
       End loop;
    End If;

    If Nvl(P_AGG_CASSA_AC,'N')='Y' Then
       update PDG_DATI_STAMPA_BILANCIO_TEMP
       set IM_CASSA_AC = 0
       where ESERCIZIO = P_ESERCIZIO;

       update PDG_DATI_STAMPA_BILANCIO_TEMP
       set IM_CASSA_AC = (NVL(IM_PREVISIONE_AC,0)+NVL(IM_RESIDUI_AC,0))*80/100
       where ESERCIZIO = P_ESERCIZIO;
    End If;

    IF Nvl(P_AGG_PREVISIONE_AP,'N')='Y' THEN
       UPDATE PDG_DATI_STAMPA_BILANCIO_TEMP
       SET IM_PREVISIONE_AP = 0,
           UTUV = P_UTCR,
           DUVA = SYSDATE,
           PG_VER_REC = PG_VER_REC + 1
       WHERE ESERCIZIO = P_ESERCIZIO;

       For rec in (SELECT a.CD_CENTRO_RESPONSABILITA, a.TI_GESTIONE, c.CD_LIVELLO6, d.CD_PROGRAMMA, d.CD_MISSIONE,
                          SUM ( NVL(a.assestato_iniziale, 0)) IM_PREVISIONE_AP
                   FROM v_assestato a,
                        elemento_voce b,
                        v_linea_attivita_valida d,
                        v_classificazione_voci_all c
                   WHERE P_ESERCIZIO >= 2016
                   AND   a.esercizio = P_ESERCIZIO-1
                   AND   a.esercizio_res = P_ESERCIZIO-1
                   AND   a.esercizio = b.esercizio
                   AND   a.ti_appartenenza = b.ti_appartenenza
                   AND   a.ti_gestione = b.ti_gestione
                   AND   a.cd_elemento_voce = b.cd_elemento_voce
                   AND   a.esercizio = d.esercizio
                   AND   a.cd_centro_responsabilita = d.cd_centro_responsabilita
                   AND   a.cd_linea_attivita = d.cd_linea_attivita
                   AND   b.id_classificazione = c.id_classificazione
                   GROUP BY a.CD_CENTRO_RESPONSABILITA, a.TI_GESTIONE, c.CD_LIVELLO6, d.CD_PROGRAMMA, d.CD_MISSIONE
                   HAVING SUM ( NVL(a.assestato_iniziale, 0))>0) Loop
           
          Update PDG_DATI_STAMPA_BILANCIO_TEMP
          set IM_PREVISIONE_AP = rec.IM_PREVISIONE_AP,
              UTUV = P_UTCR,
              DUVA = SYSDATE,
              PG_VER_REC = PG_VER_REC + 1
          where ESERCIZIO = P_ESERCIZIO
          and   CD_CENTRO_RESPONSABILITA = rec.CD_CENTRO_RESPONSABILITA
          and   TI_GESTIONE = rec.TI_GESTIONE
          and   CD_ELEMENTO_VOCE = rec.CD_LIVELLO6
          and   ((rec.CD_PROGRAMMA IS NULL AND CD_PROGRAMMA IS NULL) OR CD_PROGRAMMA = rec.CD_PROGRAMMA)
          and   ((rec.CD_MISSIONE IS NULL AND CD_MISSIONE IS NULL) OR CD_MISSIONE = rec.CD_MISSIONE);

          If sql%rowcount=0 Then
             INSERT INTO PDG_DATI_STAMPA_BILANCIO_TEMP
               (ESERCIZIO, CD_CENTRO_RESPONSABILITA, TI_GESTIONE, CD_ELEMENTO_VOCE, CD_PROGRAMMA, CD_MISSIONE,
                IM_RESIDUI_AC, IM_PREVISIONE_AC, IM_CASSA_AC, IM_RESIDUI_AP, IM_PREVISIONE_AP, 
                UTCR, DACR, UTUV, DUVA, PG_VER_REC)
             VALUES
               (P_ESERCIZIO, rec.CD_CENTRO_RESPONSABILITA, rec.TI_GESTIONE, rec.CD_LIVELLO6, 
                rec.cd_programma, rec.CD_MISSIONE, 0, 0, 0, 0, rec.IM_PREVISIONE_AP,
                P_UTCR, SYSDATE, P_UTCR, SYSDATE, 1);
          End If;
       End Loop;                          
    END IF;

    IF Nvl(P_AGG_RESIDUI_AP,'N')='Y' THEN
       UPDATE PDG_DATI_STAMPA_BILANCIO_TEMP
       SET IM_RESIDUI_AP = 0,
           UTUV = P_UTCR,
           DUVA = SYSDATE,
           PG_VER_REC = PG_VER_REC + 1
       WHERE ESERCIZIO = P_ESERCIZIO;

       --CARICO I DATI RESIDUI INIZIALI ANNO PRECEDENTE
       For rec in (select CD_CENTRO_RESPONSABILITA, TI_GESTIONE, CD_ELEMENTO_VOCE, 
                          CD_PROGRAMMA, CD_MISSIONE, NVL(SUM(IM_RESIDUO_AP), 0) IM_RESIDUO_AP
                    FROM (select A.CD_CENTRO_RESPONSABILITA, A.TI_GESTIONE, 
                                 NVL((SELECT CD_ELEMENTO_VOCE_NEW
                                      FROM ASS_EVOLD_EVNEW
                                      WHERE ESERCIZIO_OLD = A.ESERCIZIO
                                      AND   TI_APPARTENENZA_OLD = A.TI_APPARTENENZA
                                      AND   TI_GESTIONE_OLD = A.TI_GESTIONE
                                      AND   CD_ELEMENTO_VOCE_OLD = A.CD_ELEMENTO_VOCE), A.CD_ELEMENTO_VOCE) CD_ELEMENTO_VOCE,
                                 linea.cd_programma, linea.cd_missione, 
                                 case when e.descrizione='CNR'
                                      then NVL (im_stanz_res_improprio, 0) + NVL (im_obbl_res_pro, 0)
                                      else NVL (im_obbl_res_pro, 0)
                                 end IM_RESIDUO_AP
                          from voce_f_saldi_cdr_linea a,
                               v_linea_attivita_valida linea,
                               parametri_ente e
                          where a.esercizio = P_ESERCIZIO-1
                          and   a.esercizio_res < a.esercizio
                          and   a.esercizio = linea.esercizio (+)
                          and   a.cd_centro_responsabilita = linea.cd_centro_responsabilita (+)
                          and   a.cd_linea_attivita = linea.cd_linea_attivita (+)
                          and   e.attivo = 'Y')
                   group by CD_CENTRO_RESPONSABILITA, TI_GESTIONE, CD_ELEMENTO_VOCE, CD_PROGRAMMA, CD_MISSIONE) loop
      
          Update PDG_DATI_STAMPA_BILANCIO_TEMP
          set IM_RESIDUI_AP = rec.IM_RESIDUO_AP
          where ESERCIZIO = P_ESERCIZIO
          and   CD_CENTRO_RESPONSABILITA = rec.CD_CENTRO_RESPONSABILITA
          and   TI_GESTIONE = rec.TI_GESTIONE
          and   CD_ELEMENTO_VOCE = rec.CD_ELEMENTO_VOCE
          and   ((rec.CD_PROGRAMMA IS NULL AND CD_PROGRAMMA IS NULL) OR CD_PROGRAMMA = rec.CD_PROGRAMMA)
          and   ((rec.CD_MISSIONE IS NULL AND CD_MISSIONE IS NULL) OR CD_MISSIONE = rec.CD_MISSIONE);

          If sql%rowcount=0 Then
            INSERT INTO PDG_DATI_STAMPA_BILANCIO_TEMP
              (ESERCIZIO, CD_CENTRO_RESPONSABILITA, TI_GESTIONE, CD_ELEMENTO_VOCE, CD_PROGRAMMA, CD_MISSIONE, 
               IM_RESIDUI_AC, IM_PREVISIONE_AC, IM_CASSA_AC, IM_RESIDUI_AP, UTCR, DACR, UTUV, DUVA, PG_VER_REC)
            VALUES
              (P_ESERCIZIO, rec.CD_CENTRO_RESPONSABILITA, rec.TI_GESTIONE, rec.CD_ELEMENTO_VOCE, 
               rec.cd_programma, rec.CD_MISSIONE, 0, 0, 0, rec.IM_RESIDUO_AP, P_UTCR, SYSDATE, P_UTCR, SYSDATE, 1);
          End If;
       End loop;
    End If;
  End If;

  Delete from PDG_DATI_STAMPA_BILANCIO
  where ESERCIZIO = P_ESERCIZIO
  and IM_RESIDUI_AC = 0
  and IM_CASSA_AC = 0;

  Delete from PDG_DATI_STAMPA_BILANCIO_TEMP
  where ESERCIZIO = P_ESERCIZIO
  and IM_RESIDUI_AC = 0
  and IM_PREVISIONE_AC = 0
  and IM_CASSA_AC = 0
  and IM_RESIDUI_AP = 0
  and IM_PREVISIONE_AP = 0
  and IM_CASSA_AP = 0;
END;
/


