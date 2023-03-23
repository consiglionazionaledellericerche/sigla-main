CREATE OR REPLACE Procedure PRC_LOAD_SITUAZIONE_PROGETTI
(P_ESERCIZIO             IN ESERCIZIO.ESERCIZIO%TYPE,
 P_PROGETTO              IN PROGETTO_SIP.PG_PROGETTO%TYPE,
 P_UO                    IN UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA%TYPE,
 P_GAE                   IN LINEA_ATTIVITA.CD_LINEA_ATTIVITA%TYPE,
 P_ROTTURA_ANNO          IN VARCHAR2 DEFAULT 'N',
 P_ROTTURA_GAE           IN VARCHAR2 DEFAULT 'N',
 P_ROTTURA_VOCE          IN VARCHAR2 DEFAULT 'N',
 P_ROTTURA_PIANO         IN VARCHAR2 DEFAULT 'N',
 P_SOLO_GAE_ATTIVE       IN VARCHAR2 DEFAULT 'N',
 P_PRINT_MOVIMENTAZIONE  IN VARCHAR2 DEFAULT 'N',
 P_RESPONSABILE_GAE      IN LINEA_ATTIVITA.CD_RESPONSABILE_TERZO%TYPE DEFAULT NULL
) Is
  P_CENTRO_RESPONSABILITA LINEA_ATTIVITA.CD_CENTRO_RESPONSABILITA%TYPE;
  P_PG_PROGLIV2           PROGETTO_SIP.PG_PROGETTO%TYPE;
  P_ROTTURA_ANNO_LOCAL    VARCHAR2(1) := 'N';
  P_CONTA_INSERT          NUMBER := 0;
  P_REC_PRINCIPALE        VARCHAR2(6) := 'PRINC';
  P_REC_DETAIL_MOVIMENTI  VARCHAR2(6) := 'DETMOV';
  P_REC_DETAIL_VARIAZIONI VARCHAR2(6) := 'DETVAR';
Begin
  if P_ROTTURA_PIANO = 'S' then
     P_ROTTURA_ANNO_LOCAL := 'S';
  else
     P_ROTTURA_ANNO_LOCAL := P_ROTTURA_ANNO;
  end if;

  For rec in (SELECT distinct A.CD_CENTRO_RESPONSABILITA, A.PG_PROGETTO
              FROM V_LINEA_ATTIVITA_VALIDA A
              WHERE A.ESERCIZIO = P_ESERCIZIO
              AND  (P_UO IS NULL OR A.CD_CENTRO_RESPONSABILITA LIKE P_UO||'%' OR
                    A.CD_CENTRO_RESPONSABILITA IN (SELECT C.CD_CENTRO_RESPONSABILITA
                                                   FROM PROGETTO B, V_STRUTTURA_ORGANIZZATIVA C
                                                   WHERE B.ESERCIZIO_PROGETTO_PADRE = A.ESERCIZIO
                                                   AND   B.PG_PROGETTO_PADRE = A.PG_PROGETTO
                                                   AND   B.TIPO_FASE_PROGETTO_PADRE = 'X'
                                                   AND   B.LIVELLO=3
                                                   AND   B.CD_UNITA_ORGANIZZATIVA = C.CD_UNITA_ORGANIZZATIVA
                                                   AND   C.CD_TIPO_LIVELLO = 'CDR'))
              AND  (P_PROGETTO IS NULL OR A.PG_PROGETTO = P_PROGETTO)
              AND  (P_GAE IS NULL OR P_GAE = '*' OR A.CD_LINEA_ATTIVITA = P_GAE)
              AND  (P_RESPONSABILE_GAE IS NULL OR A.CD_RESPONSABILE_TERZO = P_RESPONSABILE_GAE)
              AND  A.ESERCIZIO >= 2016) Loop

      P_CENTRO_RESPONSABILITA := rec.CD_CENTRO_RESPONSABILITA;
      P_PG_PROGLIV2 := rec.PG_PROGETTO;

      INSERT INTO TMP_STAMPA_SITUAZIONE_PROGETTI
         (ESERCIZIO, ESERCIZIO_RES, TI_GESTIONE,
          CD_CENTRO_RESPONSABILITA, PG_PROGETTO,
          CD_UNITA_PIANO, CD_VOCE_PIANO, DS_VOCE_PIANO,
          CD_LINEA_ATTIVITA, DS_LINEA_ATTIVITA,
          CD_ELEMENTO_VOCE, DS_ELEMENTO_VOCE, TIPO_RECORD,
          STANZIAMENTO_ACC, VARIAZIONI_ACC, STANZIAMENTO_DEC, VARIAZIONI_DEC, TOT_VINCOLI,
          TOT_IMPACC, TOT_MANREV, TOT_NUMMOV, TOT_NUMMOV_OBBACC, TOT_NUMMOV_VARIAZIONI)
      (SELECT P_ESERCIZIO, p.esercizio_res, p.tipo,
              P_CENTRO_RESPONSABILITA, P_PG_PROGLIV2,
              p.cd_unita_piano,
              p.cd_voce_piano,
              CASE WHEN P_ROTTURA_PIANO='S'
                   THEN NVL((SELECT ds_voce_piano from VOCE_PIANO_ECONOMICO_PRG q
                             WHERE q.CD_UNITA_ORGANIZZATIVA = p.cd_unita_piano
                             AND   q.CD_VOCE_PIANO = p.cd_voce_piano), 'PIANONULLO')
                   ELSE 'DS_VOCE_PIANO_UNICO'
                   END ds_voce_piano,
              p.cd_linea_attivita,
              CASE WHEN P_ROTTURA_GAE='S'
                   THEN (SELECT nvl(q.ds_linea_attivita,q.denominazione) from linea_attivita q
                         WHERE q.cd_centro_responsabilita = P_CENTRO_RESPONSABILITA
                         AND   q.cd_linea_attivita = p.cd_linea_attivita)
                   ELSE 'DS_GAE_UNICA'
                   END ds_linea_attivita,
              p.cd_elemento_voce,
              CASE WHEN P_ROTTURA_VOCE='S'
                   THEN (SELECT b.ds_elemento_voce FROM ELEMENTO_VOCE B
                         WHERE B.TI_GESTIONE = P.TIPO
                         AND B.CD_ELEMENTO_VOCE = P.CD_ELEMENTO_VOCE
                         and ROWNUM<2)
                   ELSE 'DS_VOCE_UNICA'
                   END ds_elemento_voce,
              P_REC_PRINCIPALE,
              p.stanziamento_acc, p.variazioni_acc,
              p.stanziamento, p.variazioni, p.vincoli,
              p.impacc, p.pagris, p.nummov, 0, 0
       FROM (SELECT a.tipo, a.esercizio_res, a.cd_unita_piano, a.cd_voce_piano,
                    a.cd_linea_attivita, a.cd_elemento_voce,
                    sum(a.stanziamento_acc) stanziamento_acc,
                    sum(a.variazioni_acc) variazioni_acc,
                    sum(a.stanziamento) stanziamento,
                    sum(a.variazioni) variazioni,
                    sum(a.vincoli) vincoli,
                    sum(CASE WHEN a.esercizio=P_ESERCIZIO
                             THEN a.impacc
                             ELSE (CASE WHEN a.impacc<a.pagris
                                        THEN a.impacc
                                        ELSE a.pagris
                                        END)
                             END) impacc,
                    sum(a.pagris) pagris,
                    sum(a.contamov) nummov
             FROM (SELECT esercizio,
                          CASE WHEN P_ROTTURA_ANNO_LOCAL='S'
                               THEN esercizio_res
                               ELSE 0
                               END esercizio_res,
                          'E' tipo,
                          CASE WHEN P_ROTTURA_PIANO='S'
                               THEN cd_unita_piano
                               ELSE 'PIANOUNICO'
                               END cd_unita_piano,
                          CASE WHEN P_ROTTURA_PIANO='S'
                               THEN cd_voce_piano
                               ELSE 'PIANOUNICO'
                               END cd_voce_piano,
                          CASE WHEN P_ROTTURA_GAE='S'
                               THEN lda
                               ELSE 'GAE_UNICA'
                               END cd_linea_attivita,
                          CASE WHEN P_ROTTURA_VOCE='S'
                               THEN NVL(getCurrentCdElementoVoce(P_ESERCIZIO, esercizio_res, 'E' , cd_elemento_voce),
                                        cd_elemento_voce)
                               ELSE 'VOCE_UNICA'
                               END cd_elemento_voce,
                          0 stanziamento_acc, 0 variazioni_acc,
                          CASE WHEN esercizio = esercizio_res
                               THEN NVL(STANZ_INI,0)
                               ELSE 0
                               END stanziamento,
                          CASE WHEN esercizio = esercizio_res
                               THEN NVL(VAR_PIU,0)-NVL(VAR_MENO,0)
                               ELSE 0
                               END variazioni,
                          0 vincoli,
                          CASE WHEN esercizio = esercizio_res
                               THEN NVL(ACC_COMP,0)
                               ELSE NVL(ACC_RES_PRO,0)+
                                    NVL(VAR_PIU_RES_PRO,0)-NVL(VAR_MENO_RES_PRO,0)
                               END impacc,
                          CASE WHEN esercizio = esercizio_res
                               THEN NVL(RISCOSSO_COMP,0)
                               ELSE NVL(RISCOSSO_RES,0)
                               END pagris,
                          CASE WHEN esercizio = esercizio_res
                               THEN CASE WHEN NVL(STANZ_INI,0)!=0 OR NVL(VAR_PIU,0)!=0 OR
                                              NVL(VAR_MENO,0)!=0 OR NVL(ACC_COMP,0)!=0 OR NVL(RISCOSSO_COMP,0)!=0
                                         THEN 1
                                         ELSE 0
                                         END
                               ELSE CASE WHEN NVL(ACC_RES_PRO,0)!=0 OR NVL(VAR_PIU_RES_PRO,0)!=0 OR
                                              NVL(VAR_MENO_RES_PRO,0)!=0 OR NVL(RISCOSSO_RES,0)!=0
                                         THEN 1
                                         ELSE 0
                                         END
                               END contamov
                   FROM V_CONS_DISP_COMP_RES_ENT
                   WHERE cdr = P_CENTRO_RESPONSABILITA
                   AND   lda in (SELECT cd_linea_attivita
                                 FROM v_linea_attivita_valida
                                 WHERE cd_centro_responsabilita = P_CENTRO_RESPONSABILITA
                                 AND   pg_progetto=P_PG_PROGLIV2
                                 AND   cd_linea_attivita=NVL(decode(P_GAE,'*',null,P_GAE), cd_linea_attivita)
                                 AND   (P_RESPONSABILE_GAE IS NULL OR cd_responsabile_terzo=P_RESPONSABILE_GAE))
                   AND (P_SOLO_GAE_ATTIVE='N' OR
                        EXISTS(SELECT '1' FROM VOCE_F_SALDI_CDR_LINEA SALDI
                               WHERE SALDI.ESERCIZIO = P_ESERCIZIO
                               AND   SALDI.CD_CENTRO_RESPONSABILITA = cdr
                               AND   SALDI.CD_LINEA_ATTIVITA = lda))
                   UNION ALL
                   SELECT esercizio,
                          CASE WHEN P_ROTTURA_ANNO_LOCAL='S'
                               THEN esercizio_res
                               ELSE 0
                               END esercizio_res,
                          'S' tipo,
                          CASE WHEN P_ROTTURA_PIANO='S'
                               THEN cd_unita_piano
                               ELSE 'PIANOUNICO'
                               END cd_unita_piano,
                          CASE WHEN P_ROTTURA_PIANO='S'
                               THEN cd_voce_piano
                               ELSE 'PIANOUNICO'
                               END cd_voce_piano,
                          CASE WHEN P_ROTTURA_GAE='S'
                               THEN lda
                               ELSE 'GAE_UNICA'
                               END cd_linea_attivita,
                          CASE WHEN P_ROTTURA_VOCE='S'
                               THEN NVL(getCurrentCdElementoVoce(P_ESERCIZIO, esercizio_res, 'S' , cd_elemento_voce),
                                        cd_elemento_voce)
                               ELSE 'VOCE_UNICA'
                               END cd_elemento_voce,
                          0 stanziamento_acc, 0 variazioni_acc,
                          CASE WHEN esercizio = esercizio_res
                               THEN NVL(STANZ_INI,0)
                               ELSE 0
                               END stanziamento,
                          CASE WHEN esercizio = esercizio_res
                               THEN NVL(VAR_PIU,0)-NVL(VAR_MENO,0)
                               ELSE NVL(VAR_PIU_STANZ_RES_IMP,0)-NVL(VAR_MENO_STANZ_RES_IMP,0)
                               END variazioni,
                          CASE WHEN esercizio = esercizio_res
                               THEN cnrutl002.IM_VINCOLI(P_ESERCIZIO,
                                       v_cons_disp_comp_res.ESERCIZIO_RES,
                                       v_cons_disp_comp_res.CDR,
                                       v_cons_disp_comp_res.LDA,
                                       'D',
                                       'S',
                                       v_cons_disp_comp_res.cd_elemento_voce)
                               ELSE 0
                               END vincoli,
                          CASE WHEN esercizio = esercizio_res
                               THEN NVL(OBB_COMP,0)
                               ELSE NVL(OBB_RES_IMP,0)+NVL(OBB_RES_PRO,0)+
                                    NVL(VAR_PIU_RES_PRO,0)-NVL(VAR_MENO_RES_PRO,0)
                               END impegnato,
                          CASE WHEN esercizio = esercizio_res
                               THEN NVL(PAGATO_COMP,0)
                               ELSE NVL(PAGATO_RES,0)
                               END pagato,
                          CASE WHEN esercizio = esercizio_res
                               THEN CASE WHEN NVL(STANZ_INI,0)!=0 OR
                                              NVL(VAR_PIU,0)!=0 OR NVL(VAR_MENO,0)!=0 OR
                                              NVL(OBB_COMP,0)!=0 OR NVL(PAGATO_COMP,0)!=0
                                         THEN 1
                                         ELSE 0
                                         END
                               ELSE CASE WHEN NVL(VAR_PIU_STANZ_RES_IMP,0)!=0 OR NVL(VAR_MENO_STANZ_RES_IMP,0)!=0 OR
                                              NVL(OBB_RES_IMP,0)!=0 OR NVL(OBB_RES_PRO,0)!= 0 OR
                                              NVL(VAR_PIU_RES_PRO,0)!=0 OR NVL(VAR_MENO_RES_PRO,0)!=0 OR
                                              NVL(PAGATO_RES,0)!=0
                                         THEN 1
                                         ELSE 0
                                         END
                               END contamov
                   FROM V_CONS_DISP_COMP_RES
                   WHERE cdr = P_CENTRO_RESPONSABILITA
                   AND   lda in (SELECT cd_linea_attivita
                                 FROM v_linea_attivita_valida
                                 WHERE cd_centro_responsabilita = P_CENTRO_RESPONSABILITA
                                 AND   pg_progetto=P_PG_PROGLIV2
                                 AND   cd_linea_attivita=NVL(decode(P_GAE,'*',null,P_GAE), cd_linea_attivita)
                                 AND   (P_RESPONSABILE_GAE IS NULL OR cd_responsabile_terzo=P_RESPONSABILE_GAE))
                   AND (P_SOLO_GAE_ATTIVE='N' OR
                        EXISTS(SELECT '1' FROM VOCE_F_SALDI_CDR_LINEA SALDI
                               WHERE SALDI.ESERCIZIO = P_ESERCIZIO
                               AND   SALDI.CD_CENTRO_RESPONSABILITA = cdr
                               AND   SALDI.CD_LINEA_ATTIVITA = lda))
                   UNION ALL
                   SELECT a.esercizio,
                          CASE WHEN P_ROTTURA_ANNO_LOCAL='S'
                               THEN a.esercizio
                               ELSE 0
                               END esercizio_res,
                          'S' tipo,
                          CASE WHEN P_ROTTURA_PIANO='S'
                               THEN c.cd_unita_organizzativa
                               ELSE 'PIANOUNICO'
                               END cd_unita_piano,
                          CASE WHEN P_ROTTURA_PIANO='S'
                               THEN c.cd_voce_piano
                               ELSE 'PIANOUNICO'
                               END cd_voce_piano,
                          CASE WHEN P_ROTTURA_GAE='S'
                               THEN a.cd_linea_attivita
                               ELSE 'GAE_UNICA'
                               END cd_linea_attivita,
                          CASE WHEN P_ROTTURA_VOCE='S'
                               THEN NVL(getCurrentCdElementoVoce(P_ESERCIZIO, a.esercizio, 'S' , a.cd_elemento_voce),
                                        a.cd_elemento_voce)
                               ELSE 'VOCE_UNICA'
                               END cd_elemento_voce,
                          DECODE(a.cd_cdr_assegnatario_clgs, NULL,
                               NVL (a.im_spese_gest_accentrata_int, 0) + NVL (a.im_spese_gest_accentrata_est, 0),
                               0) stanziamento_acc,
                          0 variazioni_acc,
                          0 stanziamento, 0 variazioni, 0 vincoli, 0 impegnato, 0 pagato,
                          CASE WHEN P_ROTTURA_PIANO='S' AND
                                    (NVL(a.im_spese_gest_accentrata_int,0)!=0 OR
                                     NVL(a.im_spese_gest_accentrata_est,0)!=0)
                               THEN 1
                               ELSE 0
                               END contamov
                   FROM pdg_modulo_spese_gest a, v_linea_attivita_valida b,
                        ass_progetto_piaeco_voce c
                   WHERE a.cd_cdr_assegnatario = P_CENTRO_RESPONSABILITA
                   AND   b.esercizio = P_ESERCIZIO
                   AND   a.cd_cdr_assegnatario = b.cd_centro_responsabilita
                   AND   a.cd_linea_attivita = b.cd_linea_attivita
                   AND   b.pg_progetto=P_PG_PROGLIV2
                   AND   b.cd_linea_attivita = NVL(decode(P_GAE,'*',null,P_GAE), b.cd_linea_attivita)
                   AND   (P_RESPONSABILE_GAE IS NULL OR b.cd_responsabile_terzo=P_RESPONSABILE_GAE)
                   AND   b.pg_progetto = c.pg_progetto (+)
                   AND   (c.pg_progetto is null or 
                          (a.esercizio = c.esercizio_piano AND   
                           a.esercizio = c.esercizio_voce AND
                           a.ti_appartenenza = c.ti_appartenenza AND
                           a.ti_gestione = c.ti_gestione AND
                           a.cd_elemento_voce = c.cd_elemento_voce))
                   AND (P_SOLO_GAE_ATTIVE='N' OR
                        EXISTS(SELECT '1' FROM VOCE_F_SALDI_CDR_LINEA SALDI
                               WHERE SALDI.ESERCIZIO = P_ESERCIZIO
                               AND   SALDI.CD_CENTRO_RESPONSABILITA = b.cd_centro_responsabilita
                               AND   SALDI.CD_LINEA_ATTIVITA = b.cd_linea_attivita))
                   UNION ALL
                   SELECT a.esercizio,
                          CASE WHEN P_ROTTURA_ANNO_LOCAL='S'
                               THEN a.esercizio
                               ELSE 0
                               END esercizio_res,
                          'S' tipo,
                          CASE WHEN P_ROTTURA_PIANO='S'
                               THEN d.cd_unita_organizzativa
                               ELSE 'PIANOUNICO'
                               END cd_unita_piano,
                          CASE WHEN P_ROTTURA_PIANO='S'
                               THEN d.cd_voce_piano
                               ELSE 'PIANOUNICO'
                               END cd_voce_piano,
                          CASE WHEN P_ROTTURA_GAE='S'
                               THEN b.cd_linea_attivita
                               ELSE 'GAE_UNICA'
                               END cd_linea_attivita,
                          CASE WHEN P_ROTTURA_VOCE='S'
                               THEN NVL(getCurrentCdElementoVoce(P_ESERCIZIO, a.esercizio, 'S' , b.cd_elemento_voce),
                                    b.cd_elemento_voce)
                               ELSE 'VOCE_UNICA'
                               END cd_elemento_voce,
                          0 stanziamento_acc,
                          DECODE(b.cd_cdr_assegnatario_clgs, NULL,
                                 NVL (b.im_spese_gest_accentrata_int, 0) + NVL (b.im_spese_gest_accentrata_est, 0),
                                 0) variazioni_acc,
                          0 stanziamento, 0 variazioni, 0 vincoli, 0 impegnato, 0 pagato,
                          CASE WHEN P_ROTTURA_PIANO='S' AND
                                    (NVL(b.im_spese_gest_accentrata_int,0)!=0 OR
                                     NVL(b.im_spese_gest_accentrata_est,0)!=0)
                               THEN 1
                               ELSE 0
                               END contamov
                   FROM pdg_variazione a,
                        pdg_variazione_riga_gest b,
                        v_linea_attivita_valida c,
                        ass_progetto_piaeco_voce d
                   WHERE a.esercizio = b.esercizio
                   AND   a.pg_variazione_pdg = b.pg_variazione_pdg
                   AND   a.stato IN ('APP', 'APF')
                   AND   b.cd_cdr_assegnatario = P_CENTRO_RESPONSABILITA
                   AND   c.esercizio = P_ESERCIZIO
                   AND   b.cd_cdr_assegnatario = c.cd_centro_responsabilita
                   AND   b.cd_linea_attivita = c.cd_linea_attivita
                   AND   c.pg_progetto=P_PG_PROGLIV2
                   AND   c.cd_linea_attivita = NVL(decode(P_GAE,'*',null,P_GAE), c.cd_linea_attivita)
                   AND   (P_RESPONSABILE_GAE IS NULL OR c.cd_responsabile_terzo=P_RESPONSABILE_GAE)
                   AND   c.pg_progetto = d.pg_progetto (+)
                   AND   (d.pg_progetto is null or 
                          (b.esercizio = d.esercizio_piano AND   
                           b.esercizio = d.esercizio_voce AND
                           b.ti_appartenenza = d.ti_appartenenza AND
                           b.ti_gestione = d.ti_gestione AND
                           b.cd_elemento_voce = d.cd_elemento_voce))
                   AND (P_SOLO_GAE_ATTIVE='N' OR
                        EXISTS(SELECT '1' FROM VOCE_F_SALDI_CDR_LINEA SALDI
                               WHERE SALDI.ESERCIZIO = P_ESERCIZIO
                               AND   SALDI.CD_CENTRO_RESPONSABILITA = c.cd_centro_responsabilita
                               AND   SALDI.CD_LINEA_ATTIVITA = c.cd_linea_attivita))
                   UNION ALL
                   SELECT c.esercizio,
                          CASE WHEN P_ROTTURA_ANNO_LOCAL='S'
                               THEN c.esercizio_originale
                               ELSE 0
                               END esercizio_res,
                          'S' tipo,
                          CASE WHEN P_ROTTURA_PIANO='S'
                               THEN c.cd_unita_organizzativa
                               ELSE 'PIANOUNICO'
                               END cd_unita_piano,
                          CASE WHEN P_ROTTURA_PIANO='S'
                               THEN e.cd_voce_piano
                               ELSE 'PIANOUNICO'
                               END cd_voce_piano,
                          CASE WHEN P_ROTTURA_GAE='S'
                               THEN a.CD_LINEA_ATTIVITA
                               ELSE 'GAE_UNICA'
                               END cd_linea_attivita,
                          CASE WHEN P_ROTTURA_VOCE='S'
                               THEN NVL(getCurrentCdElementoVoce(P_ESERCIZIO, c.esercizio_originale, 'S', c.cd_elemento_voce),
                                        c.cd_elemento_voce)
                               ELSE 'VOCE_UNICA'
                               END cd_elemento_voce,
                          0 stanziamento_acc, 0 variazioni_acc,
                          0 stanziamento, -(im_voce-(im_voce*(IM_ASSOCIATO_DOC_CONTABILE/IM_SCADENZA))) variazioni,
                          0 vincoli, 0 impegnato, 0 pagato, 1 contamov
                   FROM obbligazione_scad_voce a, obbligazione_scadenzario b, obbligazione c, v_linea_attivita_valida d, ass_progetto_piaeco_voce e
                   WHERE a.cd_cds = b.cd_cds
                   and   a.esercizio=b.esercizio
                   and   a.esercizio_originale=b.esercizio_originale
                   and   a.pg_obbligazione = b.pg_obbligazione
                   and   a.pg_obbligazione_scadenzario = b.pg_obbligazione_scadenzario
                   and   a.cd_cds = c.cd_cds
                   and   a.esercizio=c.esercizio
                   and   a.esercizio_originale=c.esercizio_originale
                   and   a.pg_obbligazione = c.pg_obbligazione
                   and   c.cd_elemento_voce_next is not null
                   and   nvl(b.im_scadenza,0)!=0
                   and   a.cd_centro_responsabilita = P_CENTRO_RESPONSABILITA
                   AND   d.esercizio = P_ESERCIZIO
                   AND   a.cd_centro_responsabilita = d.cd_centro_responsabilita
                   AND   a.cd_linea_attivita = d.cd_linea_attivita
                   AND   d.pg_progetto=P_PG_PROGLIV2
                   AND   d.cd_linea_attivita = NVL(decode(P_GAE,'*',null,P_GAE), d.cd_linea_attivita)
                   AND   (P_RESPONSABILE_GAE IS NULL OR d.cd_responsabile_terzo=P_RESPONSABILE_GAE)
                   AND   (im_voce-(im_voce*(IM_ASSOCIATO_DOC_CONTABILE/IM_SCADENZA)))!=0
                   AND   d.pg_progetto = e.pg_progetto (+)
                   AND   (e.pg_progetto is null or 
                          (a.esercizio = e.esercizio_piano AND   
                           a.esercizio = e.esercizio_voce AND
                           a.ti_appartenenza = e.ti_appartenenza AND
                           a.ti_gestione = e.ti_gestione AND
                           a.cd_voce = e.cd_elemento_voce))
                   AND (P_SOLO_GAE_ATTIVE='N' OR
                        EXISTS(SELECT '1' FROM VOCE_F_SALDI_CDR_LINEA SALDI
                               WHERE SALDI.ESERCIZIO = P_ESERCIZIO
                               AND   SALDI.CD_CENTRO_RESPONSABILITA = d.cd_centro_responsabilita
                               AND   SALDI.CD_LINEA_ATTIVITA = d.cd_linea_attivita))
                   UNION ALL
                   SELECT c.esercizio,
                          CASE WHEN P_ROTTURA_ANNO_LOCAL='S'
                               THEN c.esercizio_originale
                               ELSE 0
                               END esercizio_res,
                          'S' tipo,
                          CASE WHEN P_ROTTURA_PIANO='S'
                               THEN e.cd_unita_organizzativa
                               ELSE 'PIANOUNICO'
                               END cd_unita_piano,
                          CASE WHEN P_ROTTURA_PIANO='S'
                               THEN e.cd_voce_piano
                               ELSE 'PIANOUNICO'
                               END cd_voce_piano,
                          CASE WHEN P_ROTTURA_GAE='S'
                               THEN a.CD_LINEA_ATTIVITA
                               ELSE 'GAE_UNICA'
                               END cd_linea_attivita,
                          CASE WHEN P_ROTTURA_VOCE='S'
                               THEN cd_elemento_voce_next
                               ELSE 'VOCE_UNICA'
                               END cd_elemento_voce,
                          0 stanziamento_acc, 0 variazioni_acc,
                          0 stanziamento, im_voce-(im_voce*(IM_ASSOCIATO_DOC_CONTABILE/IM_SCADENZA)) variazioni,
                          0 vincoli, 0 impegnato, 0 pagato, 1 contamov
                   FROM obbligazione_scad_voce a, obbligazione_scadenzario b, obbligazione c, v_linea_attivita_valida d, ass_progetto_piaeco_voce e
                   WHERE a.cd_cds = b.cd_cds
                   and   a.esercizio=b.esercizio
                   and   a.esercizio_originale=b.esercizio_originale
                   and   a.pg_obbligazione = b.pg_obbligazione
                   and   a.pg_obbligazione_scadenzario = b.pg_obbligazione_scadenzario
                   and   a.cd_cds = c.cd_cds
                   and   a.esercizio=c.esercizio
                   and   a.esercizio_originale=c.esercizio_originale
                   and   a.pg_obbligazione = c.pg_obbligazione
                   and   c.cd_elemento_voce_next is not null
                   and   nvl(b.im_scadenza,0)!=0
                   and   a.cd_centro_responsabilita = P_CENTRO_RESPONSABILITA
                   AND   d.esercizio = P_ESERCIZIO
                   AND   a.cd_centro_responsabilita = d.cd_centro_responsabilita
                   AND   a.cd_linea_attivita = d.cd_linea_attivita
                   AND   d.pg_progetto=P_PG_PROGLIV2
                   AND   d.cd_linea_attivita = NVL(decode(P_GAE,'*',null,P_GAE), d.cd_linea_attivita)
                   AND   (P_RESPONSABILE_GAE IS NULL OR d.cd_responsabile_terzo=P_RESPONSABILE_GAE)
                   AND   (im_voce-(im_voce*(IM_ASSOCIATO_DOC_CONTABILE/IM_SCADENZA)))!=0
                   AND   d.pg_progetto = e.pg_progetto (+)
                   AND   (e.pg_progetto is null or 
                          (a.esercizio = e.esercizio_piano AND   
                           a.esercizio = e.esercizio_voce AND
                           a.ti_appartenenza = e.ti_appartenenza AND
                           a.ti_gestione = e.ti_gestione AND
                           a.cd_voce = e.cd_elemento_voce))
                   AND (P_SOLO_GAE_ATTIVE='N' OR
                        EXISTS(SELECT '1' FROM VOCE_F_SALDI_CDR_LINEA SALDI
                               WHERE SALDI.ESERCIZIO = P_ESERCIZIO
                               AND   SALDI.CD_CENTRO_RESPONSABILITA = d.cd_centro_responsabilita
                               AND   SALDI.CD_LINEA_ATTIVITA = d.cd_linea_attivita))
                   UNION ALL
                   SELECT c.esercizio,
                          CASE WHEN P_ROTTURA_ANNO_LOCAL='S'
                               THEN c.esercizio_originale
                               ELSE 0
                               END esercizio_res,
                          'E' tipo,
                          CASE WHEN P_ROTTURA_PIANO='S'
                               THEN e.cd_unita_organizzativa
                               ELSE 'PIANOUNICO'
                               END cd_unita_piano,
                          CASE WHEN P_ROTTURA_PIANO='S'
                               THEN e.cd_voce_piano
                               ELSE 'PIANOUNICO'
                               END cd_voce_piano,
                          CASE WHEN P_ROTTURA_GAE='S'
                               THEN a.CD_LINEA_ATTIVITA
                               ELSE 'GAE_UNICA'
                               END cd_linea_attivita,
                          CASE WHEN P_ROTTURA_VOCE='S'
                               THEN NVL(getCurrentCdElementoVoce(P_ESERCIZIO, c.esercizio_originale, 'E', c.cd_elemento_voce),
                                        c.cd_elemento_voce)
                               ELSE 'VOCE_UNICA'
                               END cd_elemento_voce,
                          0 stanziamento_acc, 0 variazioni_acc,
                          0 stanziamento, -(im_voce-(im_voce*(IM_ASSOCIATO_DOC_CONTABILE/IM_SCADENZA))) variazioni,
                          0 vincoli, 0 impegnato, 0 pagato, 1 contamov
                   FROM accertamento_scad_voce a, accertamento_scadenzario b, accertamento c, v_linea_attivita_valida d, ass_progetto_piaeco_voce e
                   WHERE a.cd_cds = b.cd_cds
                   and   a.esercizio=b.esercizio
                   and   a.esercizio_originale=b.esercizio_originale
                   and   a.pg_accertamento = b.pg_accertamento
                   and   a.pg_accertamento_scadenzario = b.pg_accertamento_scadenzario
                   and   a.cd_cds = c.cd_cds
                   and   a.esercizio=c.esercizio
                   and   a.esercizio_originale=c.esercizio_originale
                   and   a.pg_accertamento = c.pg_accertamento
                   and   c.cd_elemento_voce_next is not null
                   and   nvl(b.im_scadenza,0)!=0
                   and   a.cd_centro_responsabilita = P_CENTRO_RESPONSABILITA
                   AND   d.esercizio = P_ESERCIZIO
                   AND   a.cd_centro_responsabilita = d.cd_centro_responsabilita
                   AND   a.cd_linea_attivita = d.cd_linea_attivita
                   AND   d.pg_progetto=P_PG_PROGLIV2
                   AND   d.cd_linea_attivita = NVL(decode(P_GAE,'*',null,P_GAE), d.cd_linea_attivita)
                   AND   (P_RESPONSABILE_GAE IS NULL OR d.cd_responsabile_terzo=P_RESPONSABILE_GAE)
                   AND   (im_voce-(im_voce*(IM_ASSOCIATO_DOC_CONTABILE/IM_SCADENZA)))!=0
                   AND   d.pg_progetto = e.pg_progetto (+)
                   AND   (e.pg_progetto is null or 
                          (c.esercizio = e.esercizio_piano AND   
                           c.esercizio = e.esercizio_voce AND
                           c.ti_appartenenza = e.ti_appartenenza AND
                           c.ti_gestione = e.ti_gestione AND
                           c.cd_voce = e.cd_elemento_voce))
                   AND (P_SOLO_GAE_ATTIVE='N' OR
                        EXISTS(SELECT '1' FROM VOCE_F_SALDI_CDR_LINEA SALDI
                               WHERE SALDI.ESERCIZIO = P_ESERCIZIO
                               AND   SALDI.CD_CENTRO_RESPONSABILITA = d.cd_centro_responsabilita
                               AND   SALDI.CD_LINEA_ATTIVITA = d.cd_linea_attivita))
                   UNION ALL
                   SELECT c.esercizio,
                          CASE WHEN P_ROTTURA_ANNO_LOCAL='S'
                               THEN c.esercizio_originale
                               ELSE 0
                               END esercizio_res,
                          'E' tipo,
                          CASE WHEN P_ROTTURA_PIANO='S'
                               THEN e.cd_unita_organizzativa
                               ELSE 'PIANOUNICO'
                               END cd_unita_piano,
                          CASE WHEN P_ROTTURA_PIANO='S'
                               THEN e.cd_voce_piano
                               ELSE 'PIANOUNICO'
                               END cd_voce_piano,
                          CASE WHEN P_ROTTURA_GAE='S'
                               THEN a.CD_LINEA_ATTIVITA
                               ELSE 'GAE_UNICA'
                               END cd_linea_attivita,
                          CASE WHEN P_ROTTURA_VOCE='S'
                               THEN cd_elemento_voce_next
                               ELSE 'VOCE_UNICA'
                               END cd_elemento_voce,
                          0 stanziamento_acc, 0 variazioni_acc,
                          0 stanziamento, im_voce-(im_voce*(IM_ASSOCIATO_DOC_CONTABILE/IM_SCADENZA)) variazioni,
                          0 vincoli, 0 impegnato, 0 pagato, 1 contamov
                   FROM accertamento_scad_voce a, accertamento_scadenzario b, accertamento c, v_linea_attivita_valida d, ass_progetto_piaeco_voce e
                   WHERE a.cd_cds = b.cd_cds
                   and   a.esercizio=b.esercizio
                   and   a.esercizio_originale=b.esercizio_originale
                   and   a.pg_accertamento = b.pg_accertamento
                   and   a.pg_accertamento_scadenzario = b.pg_accertamento_scadenzario
                   and   a.cd_cds = c.cd_cds
                   and   a.esercizio=c.esercizio
                   and   a.esercizio_originale=c.esercizio_originale
                   and   a.pg_accertamento = c.pg_accertamento
                   and   c.cd_elemento_voce_next is not null
                   and   nvl(b.im_scadenza,0)!=0
                   and   a.cd_centro_responsabilita = P_CENTRO_RESPONSABILITA
                   AND   d.esercizio = P_ESERCIZIO
                   AND   a.cd_centro_responsabilita = d.cd_centro_responsabilita
                   AND   a.cd_linea_attivita = d.cd_linea_attivita
                   AND   d.pg_progetto=P_PG_PROGLIV2
                   AND   d.cd_linea_attivita = NVL(decode(P_GAE,'*',null,P_GAE), d.cd_linea_attivita)
                   AND   (P_RESPONSABILE_GAE IS NULL OR d.cd_responsabile_terzo=P_RESPONSABILE_GAE)
                   AND   (im_voce-(im_voce*(IM_ASSOCIATO_DOC_CONTABILE/IM_SCADENZA)))!=0
                   AND   d.pg_progetto = e.pg_progetto (+)
                   AND   (e.pg_progetto is null or 
                          (c.esercizio = e.esercizio_piano AND   
                           c.esercizio = e.esercizio_voce AND
                           c.ti_appartenenza = e.ti_appartenenza AND
                           c.ti_gestione = e.ti_gestione AND
                           c.cd_voce = e.cd_elemento_voce))
                   AND (P_SOLO_GAE_ATTIVE='N' OR
                        EXISTS(SELECT '1' FROM VOCE_F_SALDI_CDR_LINEA SALDI
                               WHERE SALDI.ESERCIZIO = P_ESERCIZIO
                               AND   SALDI.CD_CENTRO_RESPONSABILITA = d.cd_centro_responsabilita
                               AND   SALDI.CD_LINEA_ATTIVITA = d.cd_linea_attivita))
                   UNION ALL
                   SELECT a.esercizio,
                          CASE WHEN P_ROTTURA_ANNO_LOCAL='S'
                               THEN 2005
                               ELSE 0
                               END esercizio_res,
                          'S' tipo,
                          CASE WHEN P_ROTTURA_PIANO='S'
                               THEN c.cd_unita_organizzativa
                               ELSE 'PIANOUNICO'
                               END cd_unita_piano,
                          CASE WHEN P_ROTTURA_PIANO='S'
                               THEN c.cd_voce_piano
                               ELSE 'PIANOUNICO'
                               END cd_voce_piano,
                          CASE WHEN P_ROTTURA_GAE='S'
                               THEN a.CD_LINEA_ATTIVITA
                               ELSE 'GAE_UNICA'
                               END cd_linea_attivita,
                          CASE WHEN P_ROTTURA_VOCE='S'
                               THEN NVL(getCurrentCdElementoVoce(P_ESERCIZIO, 2005, 'S' , a.cd_elemento_voce),
                                        a.cd_elemento_voce)
                               ELSE 'VOCE_UNICA'
                               END cd_elemento_voce,
                          0 stanziamento_acc, 0 variazioni_acc,
                          NVL(a.im_residuo, 0), 0 variazioni, 0 vincoli, 0 impegnato, 0 pagato, 1 contamov
                   FROM pdg_residuo_det a, v_linea_attivita_valida b, ass_progetto_piaeco_voce c
                   WHERE a.esercizio = 2005
                   AND   a.stato != 'A'
                   AND   a.cd_cdr_linea = P_CENTRO_RESPONSABILITA
                   AND   b.esercizio = P_ESERCIZIO
                   AND   a.cd_cdr_linea = b.cd_centro_responsabilita
                   AND   a.cd_linea_attivita = b.cd_linea_attivita
                   AND   b.pg_progetto=P_PG_PROGLIV2
                   AND   b.cd_linea_attivita = NVL(decode(P_GAE,'*',null,P_GAE), b.cd_linea_attivita)
                   AND   (P_RESPONSABILE_GAE IS NULL OR b.cd_responsabile_terzo=P_RESPONSABILE_GAE)
                   AND   b.pg_progetto = c.pg_progetto (+)
                   AND   (c.pg_progetto is null or 
                          (a.esercizio = c.esercizio_piano AND   
                           a.esercizio = c.esercizio_voce AND
                           a.ti_appartenenza = c.ti_appartenenza AND
                           a.ti_gestione = c.ti_gestione AND
                           a.cd_elemento_voce = c.cd_elemento_voce))
                   AND (P_SOLO_GAE_ATTIVE='N' OR
                        EXISTS(SELECT '1' FROM VOCE_F_SALDI_CDR_LINEA SALDI
                               WHERE SALDI.ESERCIZIO = P_ESERCIZIO
                               AND   SALDI.CD_CENTRO_RESPONSABILITA = b.cd_centro_responsabilita
                               AND   SALDI.CD_LINEA_ATTIVITA = b.cd_linea_attivita))) a
             WHERE a.esercizio <= P_ESERCIZIO
             GROUP BY a.tipo, a.esercizio_res, a.cd_linea_attivita, a.cd_unita_piano, a.cd_voce_piano, a.cd_elemento_voce) P
             WHERE (p.stanziamento != 0 OR p.variazioni != 0 OR p.impacc != 0 OR p.pagris != 0 or p.nummov!=0));

      IF P_PRINT_MOVIMENTAZIONE = 'S' THEN
          --INSERISCO DETAIL VARIAZIONI
          INSERT INTO TMP_STAMPA_SITUAZIONE_PROGETTI
             (ESERCIZIO, ESERCIZIO_RES, TI_GESTIONE,
              CD_CENTRO_RESPONSABILITA, PG_PROGETTO,
              CD_UNITA_PIANO, CD_VOCE_PIANO,
              CD_LINEA_ATTIVITA, CD_ELEMENTO_VOCE,
              TIPO_RECORD, ESERCIZIO_VARIAZIONE, PG_VARIAZIONE,
              DT_VARIAZIONE, DS_VARIAZIONE, IMPORTO_VARIAZIONE)
          (SELECT X.ESERCIZIO, X.ESERCIZIO_RES, X.TI_GESTIONE,
                  P_CENTRO_RESPONSABILITA, P_PG_PROGLIV2,
                  'PIANONULLO', 'PIANONULLO',
                  X.CD_LINEA_ATTIVITA, X.CD_ELEMENTO_VOCE,
                  P_REC_DETAIL_VARIAZIONI,
                  X.ESERCIZIO_VARIAZIONE, X.PG_VARIAZIONE,
                  X.DT_VARIAZIONE, X.DS_VARIAZIONE,
                  SUM(X.IMPORTO_VARIAZIONE)
           FROM(SELECT A.ESERCIZIO,
                       CASE WHEN P_ROTTURA_ANNO_LOCAL='S'
                            THEN A.ESERCIZIO
                            ELSE 0
                            END ESERCIZIO_RES,
                       A.TI_GESTIONE,
                       CASE WHEN P_ROTTURA_GAE='S'
                            THEN A.CD_LINEA_ATTIVITA
                            ELSE 'GAE_UNICA'
                            END CD_LINEA_ATTIVITA,
                       CASE WHEN P_ROTTURA_VOCE='S'
                            THEN NVL(getCurrentCdElementoVoce(P_ESERCIZIO, A.ESERCIZIO, A.TI_GESTIONE, A.CD_ELEMENTO_VOCE),
                                     A.CD_ELEMENTO_VOCE)
                            ELSE 'VOCE_UNICA'
                            END CD_ELEMENTO_VOCE,
                        A.ESERCIZIO ESERCIZIO_VARIAZIONE, A.PG_VARIAZIONE_PDG PG_VARIAZIONE,
                        B.DT_APPROVAZIONE DT_VARIAZIONE, B.DS_VARIAZIONE,
                        CASE WHEN A.TI_GESTIONE = 'E'
                             THEN NVL(A.IM_ENTRATA,0)
                             ELSE NVL(A.IM_SPESE_GEST_DECENTRATA_INT, 0)+
                                  NVL(A.IM_SPESE_GEST_ACCENTRATA_INT, 0)+
                                  NVL(A.IM_SPESE_GEST_DECENTRATA_EST, 0)+
                                  NVL(A.IM_SPESE_GEST_ACCENTRATA_EST, 0)
                             END IMPORTO_VARIAZIONE
                FROM PDG_VARIAZIONE_RIGA_GEST A, PDG_VARIAZIONE B
                WHERE A.ESERCIZIO = B.ESERCIZIO
                AND   A.PG_VARIAZIONE_PDG = B.PG_VARIAZIONE_PDG
                AND   B.STATO IN ('APP','APF')
                AND   A.ESERCIZIO <= P_ESERCIZIO
                AND   A.CD_CDR_ASSEGNATARIO = P_CENTRO_RESPONSABILITA
                AND   A.CD_LINEA_ATTIVITA in (SELECT cd_linea_attivita
                                              FROM v_linea_attivita_valida
                                              WHERE cd_centro_responsabilita = P_CENTRO_RESPONSABILITA
                                              AND   pg_progetto=P_PG_PROGLIV2
                                              AND   cd_linea_attivita=NVL(decode(P_GAE,'*',null,P_GAE), cd_linea_attivita)
                                              AND   (P_RESPONSABILE_GAE IS NULL OR cd_responsabile_terzo=P_RESPONSABILE_GAE))
                AND   (P_SOLO_GAE_ATTIVE='N' OR
                       EXISTS(SELECT '1' FROM VOCE_F_SALDI_CDR_LINEA SALDI
                              WHERE SALDI.ESERCIZIO = P_ESERCIZIO
                              AND   SALDI.CD_CENTRO_RESPONSABILITA = a.cd_cdr_assegnatario
                              AND   SALDI.CD_LINEA_ATTIVITA = a.cd_linea_attivita))
                UNION ALL
                SELECT A.ESERCIZIO,
                       CASE WHEN P_ROTTURA_ANNO_LOCAL='S'
                            THEN A.ESERCIZIO_RES
                            ELSE 0
                            END ESERCIZIO_RES,
                       A.TI_GESTIONE,
                       CASE WHEN P_ROTTURA_GAE='S'
                            THEN A.CD_LINEA_ATTIVITA
                            ELSE 'GAE_UNICA'
                            END CD_LINEA_ATTIVITA,
                       CASE WHEN P_ROTTURA_VOCE='S'
                            THEN NVL(getCurrentCdElementoVoce(P_ESERCIZIO, A.ESERCIZIO, A.TI_GESTIONE, A.CD_ELEMENTO_VOCE),
                                     a.CD_ELEMENTO_VOCE)
                            ELSE 'VOCE_UNICA'
                            END CD_ELEMENTO_VOCE,
                            A.ESERCIZIO, A.PG_VARIAZIONE,
                            B.DT_APPROVAZIONE, B.DS_VARIAZIONE,
                            NVL(A.IM_VARIAZIONE,0)
                FROM VAR_STANZ_RES_RIGA A, VAR_STANZ_RES B
                WHERE A.ESERCIZIO = B.ESERCIZIO
                AND   A.PG_VARIAZIONE = B.PG_VARIAZIONE
                AND   B.STATO IN ('APP','APF')
                AND   A.ESERCIZIO <= P_ESERCIZIO
                AND   A.CD_CDR = P_CENTRO_RESPONSABILITA
                AND   A.CD_LINEA_ATTIVITA in (SELECT cd_linea_attivita
                                              FROM v_linea_attivita_valida
                                              WHERE cd_centro_responsabilita = P_CENTRO_RESPONSABILITA
                                              AND   pg_progetto=P_PG_PROGLIV2
                                              AND   cd_linea_attivita=NVL(decode(P_GAE,'*',null,P_GAE), cd_linea_attivita)
                                              AND   (P_RESPONSABILE_GAE IS NULL OR cd_responsabile_terzo=P_RESPONSABILE_GAE))
                AND   (P_SOLO_GAE_ATTIVE='N' OR
                       EXISTS(SELECT '1' FROM VOCE_F_SALDI_CDR_LINEA SALDI
                              WHERE SALDI.ESERCIZIO = P_ESERCIZIO
                              AND   SALDI.CD_CENTRO_RESPONSABILITA = a.cd_cdr
                              AND   SALDI.CD_LINEA_ATTIVITA = a.cd_linea_attivita))
                UNION ALL
                SELECT A.ESERCIZIO,
                       CASE WHEN P_ROTTURA_ANNO_LOCAL='S'
                            THEN A.ESERCIZIO_ORIGINALE
                            ELSE 0
                            END ESERCIZIO_RES,
                       C.TI_GESTIONE,
                       CASE WHEN P_ROTTURA_GAE='S'
                            THEN a.CD_LINEA_ATTIVITA
                            ELSE 'GAE_UNICA'
                            END CD_LINEA_ATTIVITA,
                       CASE WHEN P_ROTTURA_VOCE='S'
                            THEN NVL(getCurrentCdElementoVoce(P_ESERCIZIO, C.ESERCIZIO_ORIGINALE, C.TI_GESTIONE, C.CD_ELEMENTO_VOCE),
                                     C.CD_ELEMENTO_VOCE)
                            ELSE 'VOCE_UNICA'
                            END CD_ELEMENTO_VOCE,
                       C.ESERCIZIO, 99999,
                       TO_DATE('01/01/2016','DD/MM/YYYY'), 'Variazione fittizia per cambiamento piano dei conti',
                       -(im_voce-(im_voce*(IM_ASSOCIATO_DOC_CONTABILE/IM_SCADENZA)))
                FROM accertamento_scad_voce a, accertamento_scadenzario b, accertamento c
                WHERE a.cd_cds = b.cd_cds
                and   a.esercizio=b.esercizio
                and   a.esercizio_originale=b.esercizio_originale
                and   a.pg_accertamento = b.pg_accertamento
                and   a.pg_accertamento_scadenzario = b.pg_accertamento_scadenzario
                and   a.cd_cds = c.cd_cds
                and   a.esercizio=c.esercizio
                and   a.esercizio_originale=c.esercizio_originale
                and   a.pg_accertamento = c.pg_accertamento
                and   c.cd_elemento_voce_next is not null
                and   nvl(b.im_scadenza,0)!=0
                AND   A.ESERCIZIO <= P_ESERCIZIO
                and   a.cd_centro_responsabilita = P_CENTRO_RESPONSABILITA
                AND   A.CD_LINEA_ATTIVITA in (SELECT cd_linea_attivita
                                              FROM v_linea_attivita_valida
                                              WHERE cd_centro_responsabilita = P_CENTRO_RESPONSABILITA
                                              AND   pg_progetto=P_PG_PROGLIV2
                                              AND   cd_linea_attivita=NVL(decode(P_GAE,'*',null,P_GAE), cd_linea_attivita)
                                              AND   (P_RESPONSABILE_GAE IS NULL OR cd_responsabile_terzo=P_RESPONSABILE_GAE))
                AND   (im_voce-(im_voce*(IM_ASSOCIATO_DOC_CONTABILE/IM_SCADENZA)))!=0
                AND   (P_SOLO_GAE_ATTIVE='N' OR
                       EXISTS(SELECT '1' FROM VOCE_F_SALDI_CDR_LINEA SALDI
                              WHERE SALDI.ESERCIZIO = P_ESERCIZIO
                              AND   SALDI.CD_CENTRO_RESPONSABILITA = a.cd_centro_responsabilita
                              AND   SALDI.CD_LINEA_ATTIVITA = a.cd_linea_attivita))
                UNION ALL
                SELECT A.ESERCIZIO,
                       CASE WHEN P_ROTTURA_ANNO_LOCAL='S'
                            THEN A.ESERCIZIO_ORIGINALE
                            ELSE 0
                            END ESERCIZIO_RES,
                       C.TI_GESTIONE,
                       CASE WHEN P_ROTTURA_GAE='S'
                            THEN A.CD_LINEA_ATTIVITA
                            ELSE 'GAE_UNICA'
                            END CD_LINEA_ATTIVITA,
                       CASE WHEN P_ROTTURA_VOCE='S'
                            THEN C.cd_elemento_voce_next
                            ELSE 'VOCE_UNICA'
                            END CD_ELEMENTO_VOCE,
                       C.ESERCIZIO, 99999,
                       TO_DATE('01/01/2016','DD/MM/YYYY'), 'Variazione fittizia per cambiamento piano dei conti',
                       im_voce-(im_voce*(IM_ASSOCIATO_DOC_CONTABILE/IM_SCADENZA))
                FROM accertamento_scad_voce a, accertamento_scadenzario b, accertamento c
                WHERE a.cd_cds = b.cd_cds
                and   a.esercizio=b.esercizio
                and   a.esercizio_originale=b.esercizio_originale
                and   a.pg_accertamento = b.pg_accertamento
                and   a.pg_accertamento_scadenzario = b.pg_accertamento_scadenzario
                and   a.cd_cds = c.cd_cds
                and   a.esercizio=c.esercizio
                and   a.esercizio_originale=c.esercizio_originale
                and   a.pg_accertamento = c.pg_accertamento
                and   c.cd_elemento_voce_next is not null
                and   nvl(b.im_scadenza,0)!=0
                AND   A.ESERCIZIO <= P_ESERCIZIO
                and   a.cd_centro_responsabilita = P_CENTRO_RESPONSABILITA
                AND   A.CD_LINEA_ATTIVITA in (SELECT cd_linea_attivita
                                              FROM v_linea_attivita_valida
                                              WHERE cd_centro_responsabilita = P_CENTRO_RESPONSABILITA
                                              AND   pg_progetto=P_PG_PROGLIV2
                                              AND   cd_linea_attivita=NVL(decode(P_GAE,'*',null,P_GAE), cd_linea_attivita)
                                              AND   (P_RESPONSABILE_GAE IS NULL OR cd_responsabile_terzo=P_RESPONSABILE_GAE))
                AND   (im_voce-(im_voce*(IM_ASSOCIATO_DOC_CONTABILE/IM_SCADENZA)))!=0
                AND   (P_SOLO_GAE_ATTIVE='N' OR
                       EXISTS(SELECT '1' FROM VOCE_F_SALDI_CDR_LINEA SALDI
                              WHERE SALDI.ESERCIZIO = P_ESERCIZIO
                              AND   SALDI.CD_CENTRO_RESPONSABILITA = a.cd_centro_responsabilita
                              AND   SALDI.CD_LINEA_ATTIVITA = a.cd_linea_attivita))
                UNION ALL
                SELECT A.ESERCIZIO,
                       CASE WHEN P_ROTTURA_ANNO_LOCAL='S'
                            THEN A.ESERCIZIO_ORIGINALE
                            ELSE 0
                            END ESERCIZIO_RES,
                       A.TI_GESTIONE,
                       CASE WHEN P_ROTTURA_GAE='S'
                            THEN a.CD_LINEA_ATTIVITA
                            ELSE 'GAE_UNICA'
                            END CD_LINEA_ATTIVITA,
                       CASE WHEN P_ROTTURA_VOCE='S'
                            THEN NVL(getCurrentCdElementoVoce(P_ESERCIZIO, A.ESERCIZIO_ORIGINALE, A.TI_GESTIONE, C.CD_ELEMENTO_VOCE),
                                     C.CD_ELEMENTO_VOCE)
                            ELSE 'VOCE_UNICA'
                            END CD_ELEMENTO_VOCE,
                       C.ESERCIZIO, 99999,
                       TO_DATE('01/01/2016','DD/MM/YYYY'), 'Variazione fittizia per cambiamento piano dei conti',
                       -(im_voce-(im_voce*(IM_ASSOCIATO_DOC_CONTABILE/IM_SCADENZA)))
                FROM obbligazione_scad_voce a, obbligazione_scadenzario b, obbligazione c
                WHERE a.cd_cds = b.cd_cds
                and   a.esercizio=b.esercizio
                and   a.esercizio_originale=b.esercizio_originale
                and   a.pg_obbligazione = b.pg_obbligazione
                and   a.pg_obbligazione_scadenzario = b.pg_obbligazione_scadenzario
                and   a.cd_cds = c.cd_cds
                and   a.esercizio=c.esercizio
                and   a.esercizio_originale=c.esercizio_originale
                and   a.pg_obbligazione = c.pg_obbligazione
                and   c.cd_elemento_voce_next is not null
                and   nvl(b.im_scadenza,0)!=0
                AND   A.ESERCIZIO <= P_ESERCIZIO
                and   a.cd_centro_responsabilita = P_CENTRO_RESPONSABILITA
                AND   A.CD_LINEA_ATTIVITA in (SELECT cd_linea_attivita
                                              FROM v_linea_attivita_valida
                                              WHERE cd_centro_responsabilita = P_CENTRO_RESPONSABILITA
                                              AND   pg_progetto=P_PG_PROGLIV2
                                              AND   cd_linea_attivita=NVL(decode(P_GAE,'*',null,P_GAE), cd_linea_attivita)
                                              AND   (P_RESPONSABILE_GAE IS NULL OR cd_responsabile_terzo=P_RESPONSABILE_GAE))
                AND   (im_voce-(im_voce*(IM_ASSOCIATO_DOC_CONTABILE/IM_SCADENZA)))!=0
                AND   (P_SOLO_GAE_ATTIVE='N' OR
                       EXISTS(SELECT '1' FROM VOCE_F_SALDI_CDR_LINEA SALDI
                              WHERE SALDI.ESERCIZIO = P_ESERCIZIO
                              AND   SALDI.CD_CENTRO_RESPONSABILITA = a.cd_centro_responsabilita
                              AND   SALDI.CD_LINEA_ATTIVITA = a.cd_linea_attivita))
                UNION ALL
                SELECT A.ESERCIZIO,
                       CASE WHEN P_ROTTURA_ANNO_LOCAL='S'
                            THEN A.ESERCIZIO_ORIGINALE
                            ELSE 0
                            END ESERCIZIO_RES,
                       A.TI_GESTIONE,
                       CASE WHEN P_ROTTURA_GAE='S'
                            THEN A.CD_LINEA_ATTIVITA
                            ELSE 'GAE_UNICA'
                            END CD_LINEA_ATTIVITA,
                       CASE WHEN P_ROTTURA_VOCE='S'
                            THEN C.cd_elemento_voce_next
                            ELSE 'VOCE_UNICA'
                            END CD_ELEMENTO_VOCE,
                       C.ESERCIZIO, 99999,
                       TO_DATE('01/01/2016','DD/MM/YYYY'), 'Variazione fittizia per cambiamento piano dei conti',
                       im_voce-(im_voce*(IM_ASSOCIATO_DOC_CONTABILE/IM_SCADENZA))
                FROM obbligazione_scad_voce a, obbligazione_scadenzario b, obbligazione c
                WHERE a.cd_cds = b.cd_cds
                and   a.esercizio=b.esercizio
                and   a.esercizio_originale=b.esercizio_originale
                and   a.pg_obbligazione = b.pg_obbligazione
                and   a.pg_obbligazione_scadenzario = b.pg_obbligazione_scadenzario
                and   a.cd_cds = c.cd_cds
                and   a.esercizio=c.esercizio
                and   a.esercizio_originale=c.esercizio_originale
                and   a.pg_obbligazione = c.pg_obbligazione
                and   c.cd_elemento_voce_next is not null
                and   nvl(b.im_scadenza,0)!=0
                AND   A.ESERCIZIO <= P_ESERCIZIO
                and   a.cd_centro_responsabilita = P_CENTRO_RESPONSABILITA
                AND   A.CD_LINEA_ATTIVITA in (SELECT cd_linea_attivita
                                              FROM v_linea_attivita_valida
                                              WHERE cd_centro_responsabilita = P_CENTRO_RESPONSABILITA
                                              AND   pg_progetto=P_PG_PROGLIV2
                                              AND   cd_linea_attivita=NVL(decode(P_GAE,'*',null,P_GAE), cd_linea_attivita)
                                              AND   (P_RESPONSABILE_GAE IS NULL OR cd_responsabile_terzo=P_RESPONSABILE_GAE))
                AND   (im_voce-(im_voce*(IM_ASSOCIATO_DOC_CONTABILE/IM_SCADENZA)))!=0
                AND   (P_SOLO_GAE_ATTIVE='N' OR
                       EXISTS(SELECT '1' FROM VOCE_F_SALDI_CDR_LINEA SALDI
                              WHERE SALDI.ESERCIZIO = P_ESERCIZIO
                              AND   SALDI.CD_CENTRO_RESPONSABILITA = a.cd_centro_responsabilita
                              AND   SALDI.CD_LINEA_ATTIVITA = a.cd_linea_attivita))) X
           GROUP BY X.ESERCIZIO, X.ESERCIZIO_RES, X.TI_GESTIONE,
                    X.CD_LINEA_ATTIVITA, X.CD_ELEMENTO_VOCE,
                    X.ESERCIZIO_VARIAZIONE, X.PG_VARIAZIONE,
                    X.DT_VARIAZIONE, X.DS_VARIAZIONE);


          --INSERISCO DETAIL IMPEGNI
          INSERT INTO TMP_STAMPA_SITUAZIONE_PROGETTI
             (ESERCIZIO, ESERCIZIO_RES, TI_GESTIONE,
              CD_CENTRO_RESPONSABILITA, PG_PROGETTO,
              CD_UNITA_PIANO, CD_VOCE_PIANO,
              CD_LINEA_ATTIVITA, CD_ELEMENTO_VOCE,
              TIPO_RECORD, ESERCIZIO_OBBACC, PG_OBBACC, DS_OBBACC, DT_OBBACC,
              CD_TERZO, DS_TERZO,
              CD_TIPO_DOCUMENTO_AMM, ESERCIZIO_DOCAMM, PG_DOCUMENTO_AMM,
              ESERCIZIO_MANREV, PG_MANREV, DT_MANREV, IMPORTO_OBBACC)
          (SELECT X.ESERCIZIO, X.ESERCIZIO_RES, X.TI_GESTIONE,
                  P_CENTRO_RESPONSABILITA, P_PG_PROGLIV2,
                  'PIANONULLO', 'PIANONULLO',
                  X.CD_LINEA_ATTIVITA, X.CD_ELEMENTO_VOCE,
                  P_REC_DETAIL_MOVIMENTI,
                  X.ESERCIZIO_OBBACC, X.PG_OBBACC, X.DS_OBBACC, X.DT_OBBACC,
                  X.CD_TERZO, (SELECT TERZO.DENOMINAZIONE_SEDE FROM TERZO WHERE TERZO.CD_TERZO = X.CD_TERZO) DS_TERZO,
                  X.CD_TIPO_DOCUMENTO_AMM, X.ESERCIZIO_DOCAMM, X.PG_DOCUMENTO_AMM,
                  X.ESERCIZIO_MANREV, X.PG_MANREV, X.DATA_MANREV, SUM(X.IM_VOCE)
           FROM(SELECT A.ESERCIZIO, A.ESERCIZIO_RES, A.TI_GESTIONE,
                       CASE WHEN P_ROTTURA_GAE='S'
                            THEN A.CD_LINEA_ATTIVITA
                            ELSE 'GAE_UNICA'
                            END CD_LINEA_ATTIVITA,
                       A.CD_ELEMENTO_VOCE,
                       A.ESERCIZIO_OBBACC, A.PG_OBBACC, A.DS_OBBACC, A.DT_OBBACC,
                       A.CD_TERZO,
                       A.CD_TIPO_DOCUMENTO_AMM, A.ESERCIZIO_DOCAMM, A.PG_DOCUMENTO_AMM,
                       A.ESERCIZIO_MANREV, A.PG_MANREV, A.DATA_MANREV, A.IM_VOCE
                FROM (SELECT ASV.ESERCIZIO,
                             CASE WHEN P_ROTTURA_ANNO_LOCAL='S'
                             THEN ASV.ESERCIZIO_ORIGINALE
                             ELSE 0
                             END ESERCIZIO_RES,
                             ACC.TI_GESTIONE,
                             ASV.CD_LINEA_ATTIVITA,
                             CASE WHEN P_ROTTURA_VOCE='S'
                                  THEN NVL(getCurrentCdElementoVoce(P_ESERCIZIO, ACC.ESERCIZIO_ORIGINALE, ACC.TI_GESTIONE, ACC.CD_ELEMENTO_VOCE),
                                           ACC.CD_ELEMENTO_VOCE)
                                  ELSE 'VOCE_UNICA'
                                  END CD_ELEMENTO_VOCE,
                             ACC.ESERCIZIO_ORIGINALE ESERCIZIO_OBBACC, ACC.PG_ACCERTAMENTO PG_OBBACC,
                             ACC.DS_ACCERTAMENTO DS_OBBACC, ACC.DT_REGISTRAZIONE DT_OBBACC,
                             ACC.CD_TERZO,
                             DOC.CD_TIPO_DOCUMENTO_AMM, DOC.ESERCIZIO ESERCIZIO_DOCAMM, DOC.PG_DOCUMENTO_AMM,
                             REV.ESERCIZIO ESERCIZIO_MANREV, REV.PG_REVERSALE PG_MANREV, REV.DT_INCASSO DATA_MANREV,
                             DOC.IM_TOTALE_DOC_AMM IM_VOCE
                      from ACCERTAMENTO_SCAD_VOCE ASV, ACCERTAMENTO ACC, V_DOC_ATTIVO DOC, REVERSALE_RIGA REVRIGA, REVERSALE REV
                      where ASV.ESERCIZIO <= P_ESERCIZIO
                      and   ASV.CD_CENTRO_RESPONSABILITA = P_CENTRO_RESPONSABILITA
                      and   ASV.CD_CDS = ACC.CD_CDS
                      and   ASV.ESERCIZIO = ACC.ESERCIZIO
                      and   ASV.ESERCIZIO_ORIGINALE = ACC.ESERCIZIO_ORIGINALE
                      and   ASV.PG_ACCERTAMENTO = ACC.PG_ACCERTAMENTO
                      and   ASV.CD_CDS = DOC.CD_CDS_ACCERTAMENTO
                      and   ASV.ESERCIZIO = DOC.ESERCIZIO_ACCERTAMENTO
                      and   ASV.ESERCIZIO_ORIGINALE = DOC.ESERCIZIO_ORI_ACCERTAMENTO
                      and   ASV.PG_ACCERTAMENTO = DOC.PG_ACCERTAMENTO
                      and   ASV.PG_ACCERTAMENTO_SCADENZARIO = DOC.PG_ACCERTAMENTO_SCADENZARIO
                      and   DOC.CD_CDS_ACCERTAMENTO = REVRIGA.CD_CDS
                      and   DOC.ESERCIZIO_ACCERTAMENTO = REVRIGA.ESERCIZIO_ACCERTAMENTO
                      and   DOC.ESERCIZIO_ORI_ACCERTAMENTO = REVRIGA.ESERCIZIO_ORI_ACCERTAMENTO
                      and   DOC.PG_ACCERTAMENTO = REVRIGA.PG_ACCERTAMENTO
                      and   DOC.PG_ACCERTAMENTO_SCADENZARIO = REVRIGA.PG_ACCERTAMENTO_SCADENZARIO
                      and   DOC.CD_CDS = REVRIGA.CD_CDS_DOC_AMM
                      and   DOC.CD_UNITA_ORGANIZZATIVA = REVRIGA.CD_UO_DOC_AMM
                      and   DOC.ESERCIZIO = REVRIGA.ESERCIZIO_DOC_AMM
                      and   DOC.CD_TIPO_DOCUMENTO_AMM = REVRIGA.CD_TIPO_DOCUMENTO_AMM
                      and   DOC.PG_DOCUMENTO_AMM = REVRIGA.PG_DOC_AMM
                      and   REVRIGA.CD_CDS = REV.CD_CDS
                      and   REVRIGA.ESERCIZIO = REV.ESERCIZIO
                      and   REVRIGA.PG_REVERSALE = REV.PG_REVERSALE
                      and   NVL(REV.STATO,'V') != 'A'
                      UNION ALL
                      select ASV.ESERCIZIO,
                             CASE WHEN P_ROTTURA_ANNO_LOCAL='S'
                             THEN ASV.ESERCIZIO_ORIGINALE
                             ELSE 0
                             END ESERCIZIO_RES,
                             ACC.TI_GESTIONE,
                             ASV.CD_LINEA_ATTIVITA,
                             CASE WHEN P_ROTTURA_VOCE='S'
                                  THEN ACC.CD_ELEMENTO_VOCE
                                  ELSE 'VOCE_UNICA'
                                  END CD_ELEMENTO_VOCE,
                             ACC.ESERCIZIO_ORIGINALE ESERCIZIO_OBBACC, ACC.PG_ACCERTAMENTO PG_OBBACC,
                             ACC.DS_ACCERTAMENTO DS_OBBACC, ACC.DT_REGISTRAZIONE DT_OBBACC,
                             ACC.CD_TERZO,
                             DOC.CD_TIPO_DOCUMENTO_AMM, DOC.ESERCIZIO ESERCIZIO_DOCAMM, DOC.PG_DOCUMENTO_AMM,
                             NULL ESERCIZIO_MANREV, NULL PG_MANREV, NULL DATA_MANREV,
                             DOC.IM_TOTALE_DOC_AMM IM_VOCE
                      from ACCERTAMENTO_SCAD_VOCE ASV, ACCERTAMENTO ACC, V_DOC_ATTIVO DOC
                      where ASV.ESERCIZIO = P_ESERCIZIO
                      and   ASV.CD_CENTRO_RESPONSABILITA = P_CENTRO_RESPONSABILITA
                      and   ASV.CD_CDS = ACC.CD_CDS
                      and   ASV.ESERCIZIO = ACC.ESERCIZIO
                      and   ASV.ESERCIZIO_ORIGINALE = ACC.ESERCIZIO_ORIGINALE
                      and   ASV.PG_ACCERTAMENTO = ACC.PG_ACCERTAMENTO
                      and   ASV.CD_CDS = DOC.CD_CDS_ACCERTAMENTO
                      and   ASV.ESERCIZIO = DOC.ESERCIZIO_ACCERTAMENTO
                      and   ASV.ESERCIZIO_ORIGINALE = DOC.ESERCIZIO_ORI_ACCERTAMENTO
                      and   ASV.PG_ACCERTAMENTO = DOC.PG_ACCERTAMENTO
                      and   ASV.PG_ACCERTAMENTO_SCADENZARIO = DOC.PG_ACCERTAMENTO_SCADENZARIO
                      and   not exists(Select '1' from REVERSALE_RIGA REVRIGA
                                       Where REVRIGA.CD_CDS = DOC.CD_CDS_ACCERTAMENTO
                                       and   REVRIGA.ESERCIZIO_ACCERTAMENTO = DOC.ESERCIZIO_ACCERTAMENTO
                                       and   REVRIGA.ESERCIZIO_ORI_ACCERTAMENTO = DOC.ESERCIZIO_ORI_ACCERTAMENTO
                                       and   REVRIGA.PG_ACCERTAMENTO = DOC.PG_ACCERTAMENTO
                                       and   REVRIGA.PG_ACCERTAMENTO_SCADENZARIO = DOC.PG_ACCERTAMENTO_SCADENZARIO
                                       and   REVRIGA.CD_CDS_DOC_AMM = DOC.CD_CDS
                                       and   REVRIGA.CD_UO_DOC_AMM = DOC.CD_UNITA_ORGANIZZATIVA
                                       and   REVRIGA.ESERCIZIO_DOC_AMM = DOC.ESERCIZIO
                                       and   REVRIGA.CD_TIPO_DOCUMENTO_AMM = DOC.CD_TIPO_DOCUMENTO_AMM
                                       and   REVRIGA.PG_DOC_AMM = DOC.PG_DOCUMENTO_AMM)
                      UNION ALL
                      select ASV.ESERCIZIO,
                             CASE WHEN P_ROTTURA_ANNO_LOCAL='S'
                             THEN ASV.ESERCIZIO_ORIGINALE
                             ELSE 0
                             END ESERCIZIO_RES,
                             ACC.TI_GESTIONE,
                             ASV.CD_LINEA_ATTIVITA,
                             CASE WHEN P_ROTTURA_VOCE='S'
                                  THEN ACC.CD_ELEMENTO_VOCE
                                  ELSE 'VOCE_UNICA'
                                  END CD_ELEMENTO_VOCE,
                             ACC.ESERCIZIO_ORIGINALE ESERCIZIO_OBBACC, ACC.PG_ACCERTAMENTO PG_OBBACC,
                             ACC.DS_ACCERTAMENTO DS_OBBACC, ACC.DT_REGISTRAZIONE DT_OBBACC,
                             ACC.CD_TERZO,
                             null, null, null,
                             null, null, null,
                             ASV.IM_VOCE
                      from ACCERTAMENTO_SCAD_VOCE ASV, ACCERTAMENTO ACC
                      where ASV.ESERCIZIO = P_ESERCIZIO
                      and   ASV.CD_CENTRO_RESPONSABILITA = P_CENTRO_RESPONSABILITA
                      and   ASV.CD_CDS = ACC.CD_CDS
                      and   ASV.ESERCIZIO = ACC.ESERCIZIO
                      and   ASV.ESERCIZIO_ORIGINALE = ACC.ESERCIZIO_ORIGINALE
                      and   ASV.PG_ACCERTAMENTO = ACC.PG_ACCERTAMENTO
                      and   not exists(Select '1' from V_DOC_ATTIVO_ACCERTAMENTO DOC
                                       Where DOC.CD_CDS_ACCERTAMENTO = ASV.CD_CDS
                                       and   DOC.ESERCIZIO_ACCERTAMENTO = ASV.ESERCIZIO
                                       and   DOC.ESERCIZIO_ORI_ACCERTAMENTO = ASV.ESERCIZIO_ORIGINALE
                                       and   DOC.PG_ACCERTAMENTO = ASV.PG_ACCERTAMENTO
                                       and   DOC.PG_ACCERTAMENTO_SCADENZARIO = ASV.PG_ACCERTAMENTO_SCADENZARIO)
                      UNION ALL
                      select OSV.ESERCIZIO,
                             CASE WHEN P_ROTTURA_ANNO_LOCAL='S'
                             THEN OSV.ESERCIZIO_ORIGINALE
                             ELSE 0
                             END ESERCIZIO_RES,
                             OBB.TI_GESTIONE,
                             OSV.CD_LINEA_ATTIVITA,
                             CASE WHEN P_ROTTURA_VOCE='S'
                                  THEN NVL(getCurrentCdElementoVoce(P_ESERCIZIO, OBB.ESERCIZIO_ORIGINALE, OBB.TI_GESTIONE, OBB.CD_ELEMENTO_VOCE),
                                           OBB.CD_ELEMENTO_VOCE)
                                  ELSE 'VOCE_UNICA'
                                  END CD_ELEMENTO_VOCE,
                             OSV.ESERCIZIO_ORIGINALE ESERCIZIO_OBBACC, OSV.PG_OBBLIGAZIONE PGOBBACC,
                             OBB.DS_OBBLIGAZIONE DS_OBBACC, OBB.DT_REGISTRAZIONE DT_OBBACC,
                             OBB.CD_TERZO,
                             DOC.CD_TIPO_DOCUMENTO_AMM, DOC.ESERCIZIO ESERCIZIO_DOCAMM, DOC.PG_DOCUMENTO_AMM,
                             MAN.ESERCIZIO ESERCIZIO_MANREV, MAN.PG_MANDATO PG_MANREV, MAN.DT_PAGAMENTO DATA_MANREV,
                             DOC.IM_TOTALE_DOC_AMM IM_VOCE
                      from OBBLIGAZIONE_SCAD_VOCE OSV, OBBLIGAZIONE OBB, V_DOC_PASSIVO DOC, MANDATO_RIGA MANRIGA, MANDATO MAN
                      where OSV.ESERCIZIO <= P_ESERCIZIO
                      and   OSV.CD_CENTRO_RESPONSABILITA = P_CENTRO_RESPONSABILITA
                      and   OSV.CD_CDS = OBB.CD_CDS
                      and   OSV.ESERCIZIO = OBB.ESERCIZIO
                      and   OSV.ESERCIZIO_ORIGINALE = OBB.ESERCIZIO_ORIGINALE
                      and   OSV.PG_OBBLIGAZIONE = OBB.PG_OBBLIGAZIONE
                      and   OSV.CD_CDS = DOC.CD_CDS_OBBLIGAZIONE
                      and   OSV.ESERCIZIO = DOC.ESERCIZIO_OBBLIGAZIONE
                      and   OSV.ESERCIZIO_ORIGINALE = DOC.ESERCIZIO_ORI_OBBLIGAZIONE
                      and   OSV.PG_OBBLIGAZIONE = DOC.PG_OBBLIGAZIONE
                      and   OSV.PG_OBBLIGAZIONE_SCADENZARIO = DOC.PG_OBBLIGAZIONE_SCADENZARIO
                      and   DOC.CD_CDS_OBBLIGAZIONE = MANRIGA.CD_CDS
                      and   DOC.ESERCIZIO_OBBLIGAZIONE = MANRIGA.ESERCIZIO_OBBLIGAZIONE
                      and   DOC.ESERCIZIO_ORI_OBBLIGAZIONE = MANRIGA.ESERCIZIO_ORI_OBBLIGAZIONE
                      and   DOC.PG_OBBLIGAZIONE = MANRIGA.PG_OBBLIGAZIONE
                      and   DOC.PG_OBBLIGAZIONE_SCADENZARIO = MANRIGA.PG_OBBLIGAZIONE_SCADENZARIO
                      and   DOC.CD_CDS = MANRIGA.CD_CDS_DOC_AMM
                      and   DOC.CD_UNITA_ORGANIZZATIVA = MANRIGA.CD_UO_DOC_AMM
                      and   DOC.ESERCIZIO = MANRIGA.ESERCIZIO_DOC_AMM
                      and   DOC.CD_TIPO_DOCUMENTO_AMM = MANRIGA.CD_TIPO_DOCUMENTO_AMM
                      and   DOC.PG_DOCUMENTO_AMM = MANRIGA.PG_DOC_AMM
                      and   MANRIGA.CD_CDS = MAN.CD_CDS
                      and   MANRIGA.ESERCIZIO = MAN.ESERCIZIO
                      and   MANRIGA.PG_MANDATO = MAN.PG_MANDATO
                      and   NVL(MAN.STATO,'V') != 'A'
                      UNION ALL
                      select OSV.ESERCIZIO,
                             CASE WHEN P_ROTTURA_ANNO_LOCAL='S'
                             THEN OSV.ESERCIZIO_ORIGINALE
                             ELSE 0
                             END ESERCIZIO_RES,
                             OBB.TI_GESTIONE,
                             OSV.CD_LINEA_ATTIVITA,
                             CASE WHEN P_ROTTURA_VOCE='S'
                                  THEN OBB.CD_ELEMENTO_VOCE
                                  ELSE 'VOCE_UNICA'
                                  END CD_ELEMENTO_VOCE,
                             OSV.ESERCIZIO_ORIGINALE ESERCIZIO_OBBACC, OSV.PG_OBBLIGAZIONE PGOBBACC,
                             OBB.DS_OBBLIGAZIONE DS_OBBACC, OBB.DT_REGISTRAZIONE DT_OBBACC,
                             OBB.CD_TERZO,
                             DOC.CD_TIPO_DOCUMENTO_AMM, DOC.ESERCIZIO ESERCIZIO_DOCAMM, DOC.PG_DOCUMENTO_AMM,
                             NULL ESERCIZIO_MANREV, NULL PG_MANREV, NULL DATA_MANREV,
                             DOC.IM_TOTALE_DOC_AMM IM_VOCE
                      from OBBLIGAZIONE_SCAD_VOCE OSV, OBBLIGAZIONE OBB, V_DOC_PASSIVO DOC
                      where OSV.ESERCIZIO = P_ESERCIZIO
                      and   OSV.CD_CENTRO_RESPONSABILITA = P_CENTRO_RESPONSABILITA
                      and   OSV.CD_CDS = OBB.CD_CDS
                      and   OSV.ESERCIZIO = OBB.ESERCIZIO
                      and   OSV.ESERCIZIO_ORIGINALE = OBB.ESERCIZIO_ORIGINALE
                      and   OSV.PG_OBBLIGAZIONE = OBB.PG_OBBLIGAZIONE
                      and   OSV.CD_CDS = DOC.CD_CDS_OBBLIGAZIONE
                      and   OSV.ESERCIZIO = DOC.ESERCIZIO_OBBLIGAZIONE
                      and   OSV.ESERCIZIO_ORIGINALE = DOC.ESERCIZIO_ORI_OBBLIGAZIONE
                      and   OSV.PG_OBBLIGAZIONE = DOC.PG_OBBLIGAZIONE
                      and   OSV.PG_OBBLIGAZIONE_SCADENZARIO = DOC.PG_OBBLIGAZIONE_SCADENZARIO
                      and   not exists(Select '1' from MANDATO_RIGA MANRIGA
                                       Where MANRIGA.CD_CDS = DOC.CD_CDS_OBBLIGAZIONE
                                       and   MANRIGA.ESERCIZIO_OBBLIGAZIONE = DOC.ESERCIZIO_OBBLIGAZIONE
                                       and   MANRIGA.ESERCIZIO_ORI_OBBLIGAZIONE = DOC.ESERCIZIO_ORI_OBBLIGAZIONE
                                       and   MANRIGA.PG_OBBLIGAZIONE = DOC.PG_OBBLIGAZIONE
                                       and   MANRIGA.PG_OBBLIGAZIONE_SCADENZARIO = DOC.PG_OBBLIGAZIONE_SCADENZARIO
                                       and   MANRIGA.CD_CDS_DOC_AMM = DOC.CD_CDS
                                       and   MANRIGA.CD_UO_DOC_AMM = DOC.CD_UNITA_ORGANIZZATIVA
                                       and   MANRIGA.ESERCIZIO_DOC_AMM = DOC.ESERCIZIO
                                       and   MANRIGA.CD_TIPO_DOCUMENTO_AMM = DOC.CD_TIPO_DOCUMENTO_AMM
                                       and   MANRIGA.PG_DOC_AMM = DOC.PG_DOCUMENTO_AMM)
                      UNION ALL
                      select OSV.ESERCIZIO,
                             CASE WHEN P_ROTTURA_ANNO_LOCAL='S'
                             THEN OSV.ESERCIZIO_ORIGINALE
                             ELSE 0
                             END ESERCIZIO_RES,
                             OBB.TI_GESTIONE,
                             OSV.CD_LINEA_ATTIVITA,
                             CASE WHEN P_ROTTURA_VOCE='S'
                                  THEN OBB.CD_ELEMENTO_VOCE
                                  ELSE 'VOCE_UNICA'
                                  END CD_ELEMENTO_VOCE,
                             OSV.ESERCIZIO_ORIGINALE ESERCIZIO_OBBACC, OSV.PG_OBBLIGAZIONE PGOBBACC,
                             OBB.DS_OBBLIGAZIONE DS_OBBACC, OBB.DT_REGISTRAZIONE DT_OBBACC,
                             OBB.CD_TERZO,
                             null, null, null,
                             null, null, null,
                             OSV.IM_VOCE
                      from OBBLIGAZIONE_SCAD_VOCE OSV, OBBLIGAZIONE OBB
                      where OSV.ESERCIZIO = P_ESERCIZIO
                      and   OSV.CD_CENTRO_RESPONSABILITA = P_CENTRO_RESPONSABILITA
                      and   OSV.CD_CDS = OBB.CD_CDS
                      and   OSV.ESERCIZIO = OBB.ESERCIZIO
                      and   OSV.ESERCIZIO_ORIGINALE = OBB.ESERCIZIO_ORIGINALE
                      and   OSV.PG_OBBLIGAZIONE = OBB.PG_OBBLIGAZIONE
                      and   not exists(Select '1' from V_DOC_PASSIVO_OBBLIGAZIONE DOC
                                       Where DOC.CD_CDS_OBBLIGAZIONE = OSV.CD_CDS
                           and   DOC.ESERCIZIO_OBBLIGAZIONE = OSV.ESERCIZIO
                           and   DOC.ESERCIZIO_ORI_OBBLIGAZIONE = OSV.ESERCIZIO_ORIGINALE
                           and   DOC.PG_OBBLIGAZIONE = OSV.PG_OBBLIGAZIONE
                           and   DOC.PG_OBBLIGAZIONE_SCADENZARIO = OSV.PG_OBBLIGAZIONE_SCADENZARIO)) A
                WHERE A.CD_LINEA_ATTIVITA in (SELECT cd_linea_attivita
                                              FROM v_linea_attivita_valida
                                              WHERE cd_centro_responsabilita = P_CENTRO_RESPONSABILITA
                                              AND   pg_progetto=P_PG_PROGLIV2
                                              AND   cd_linea_attivita=NVL(decode(P_GAE,'*',null,P_GAE), cd_linea_attivita)
                                              AND   (P_RESPONSABILE_GAE IS NULL OR cd_responsabile_terzo=P_RESPONSABILE_GAE))
                AND   (P_SOLO_GAE_ATTIVE='N' OR
                       EXISTS(SELECT '1' FROM VOCE_F_SALDI_CDR_LINEA SALDI
                              WHERE SALDI.ESERCIZIO = P_ESERCIZIO
                              AND   SALDI.CD_CENTRO_RESPONSABILITA = P_CENTRO_RESPONSABILITA
                              AND   SALDI.CD_LINEA_ATTIVITA = a.cd_linea_attivita))) X
          GROUP BY X.ESERCIZIO, X.ESERCIZIO_RES, X.TI_GESTIONE, X.CD_LINEA_ATTIVITA, X.CD_ELEMENTO_VOCE,
                   X.ESERCIZIO_OBBACC, X.PG_OBBACC, X.DS_OBBACC, X.DT_OBBACC, X.CD_TERZO,
                   X.CD_TIPO_DOCUMENTO_AMM, X.ESERCIZIO_DOCAMM, X.PG_DOCUMENTO_AMM,
                   X.ESERCIZIO_MANREV, X.PG_MANREV, X.DATA_MANREV);
      END IF;
  End Loop;

  IF P_PRINT_MOVIMENTAZIONE = 'S' THEN
      UPDATE TMP_STAMPA_SITUAZIONE_PROGETTI A
      SET TOT_NUMMOV_OBBACC = (SELECT COUNT(0) FROM TMP_STAMPA_SITUAZIONE_PROGETTI B
                               WHERE B.TIPO_RECORD = P_REC_DETAIL_MOVIMENTI
                               AND   B.ESERCIZIO <= A.ESERCIZIO
                               AND   B.ESERCIZIO_RES = A.ESERCIZIO_RES
                               AND   B.TI_GESTIONE = A.TI_GESTIONE
                               AND   B.PG_PROGETTO = A.PG_PROGETTO
                               AND   B.CD_CENTRO_RESPONSABILITA = A.CD_CENTRO_RESPONSABILITA
                               AND   B.CD_LINEA_ATTIVITA = A.CD_LINEA_ATTIVITA
                               AND   B.CD_ELEMENTO_VOCE = A.CD_ELEMENTO_VOCE),
          TOT_NUMMOV_VARIAZIONI = (SELECT COUNT(0) FROM TMP_STAMPA_SITUAZIONE_PROGETTI B
                                   WHERE B.TIPO_RECORD = P_REC_DETAIL_VARIAZIONI
                                   AND   B.ESERCIZIO <= A.ESERCIZIO
                                   AND   B.ESERCIZIO_RES = A.ESERCIZIO_RES
                                   AND   B.TI_GESTIONE = A.TI_GESTIONE
                                   AND   B.PG_PROGETTO = A.PG_PROGETTO
                                   AND   B.CD_CENTRO_RESPONSABILITA = A.CD_CENTRO_RESPONSABILITA
                                   AND   B.CD_LINEA_ATTIVITA = A.CD_LINEA_ATTIVITA
                                   AND   B.CD_ELEMENTO_VOCE = A.CD_ELEMENTO_VOCE)
      WHERE A.TIPO_RECORD = P_REC_PRINCIPALE
      AND   A.ESERCIZIO = P_ESERCIZIO
      AND   A.PG_PROGETTO = P_PG_PROGLIV2;
  End if;
End;
/


