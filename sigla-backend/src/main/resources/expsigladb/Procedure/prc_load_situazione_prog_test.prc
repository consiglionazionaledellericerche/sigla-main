CREATE OR REPLACE Procedure PRC_LOAD_SITUAZIONE_PROG_TEST
(P_ESERCIZIO             IN ESERCIZIO.ESERCIZIO%TYPE,
 P_PROGETTO              IN PROGETTO_SIP.PG_PROGETTO%TYPE,
 P_UO                    IN UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA%TYPE,
 P_GAE                   IN LINEA_ATTIVITA.CD_LINEA_ATTIVITA%TYPE,
 P_ROTTURA_ANNO          IN VARCHAR2 DEFAULT 'N',
 P_ROTTURA_GAE           IN VARCHAR2 DEFAULT 'N',
 P_ROTTURA_VOCE          IN VARCHAR2 DEFAULT 'N',
 P_ROTTURA_PIANO         IN VARCHAR2 DEFAULT 'N',
 P_SOLO_GAE_ATTIVE       IN VARCHAR2 DEFAULT 'N'
) Is
  P_CENTRO_RESPONSABILITA LINEA_ATTIVITA.CD_CENTRO_RESPONSABILITA%TYPE;
  P_PG_PROGLIV2           PROGETTO_SIP.PG_PROGETTO%TYPE;
  P_ROTTURA_ANNO_LOCAL    VARCHAR2(1) := 'N';
  P_CONTA_INSERT          NUMBER := 0;
Begin
  if P_ROTTURA_PIANO = 'S' then
     P_ROTTURA_ANNO_LOCAL := 'S';
  else
     P_ROTTURA_ANNO_LOCAL := P_ROTTURA_ANNO;
  end if;

  for rec in (SELECT distinct A.CD_CENTRO_RESPONSABILITA, A.PG_PROGETTO
              FROM V_LINEA_ATTIVITA_VALIDA A
              WHERE A.ESERCIZIO = P_ESERCIZIO
              AND  (P_UO IS NULL OR A.CD_CENTRO_RESPONSABILITA LIKE P_UO||'%')
              AND  (P_PROGETTO IS NULL OR A.PG_PROGETTO = P_PROGETTO)
              AND  A.ESERCIZIO >= 2016) Loop

      P_CENTRO_RESPONSABILITA := rec.CD_CENTRO_RESPONSABILITA;
      P_PG_PROGLIV2 := rec.PG_PROGETTO;

      INSERT INTO TMP_STAMPA_SITUAZIONE_PROGETTI
         (ESERCIZIO, ESERCIZIO_RES, TI_GESTIONE, 
          CD_CENTRO_RESPONSABILITA, PG_PROGETTO,
          CD_UNITA_PIANO, CD_VOCE_PIANO, DS_VOCE_PIANO, 
          CD_LINEA_ATTIVITA, DS_LINEA_ATTIVITA, 
          CD_ELEMENTO_VOCE, DS_ELEMENTO_VOCE, 
          STANZIAMENTO_ACC, VARIAZIONI_ACC, STANZIAMENTO_DEC, VARIAZIONI_DEC,
          TOT_IMPACC, TOT_MANREV, TOT_NUMMOV)
      (SELECT P_ESERCIZIO, p.esercizio_res, p.tipo, 
              P_CENTRO_RESPONSABILITA, P_PG_PROGLIV2, 
              'PIANONULLO', 
              'PIANONULLO', 
              'PIANONULLO',
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
              p.stanziamento_acc, p.variazioni_acc,
              p.stanziamento, p.variazioni,
              p.impacc, p.pagris, p.nummov
       FROM (SELECT a.tipo, a.esercizio_res, a.cd_unita_piano, a.cd_voce_piano,
                    a.cd_linea_attivita, a.cd_elemento_voce,
                    sum(a.stanziamento_acc) stanziamento_acc,
                    sum(a.variazioni_acc) variazioni_acc,
                    sum(a.stanziamento) stanziamento,
                    sum(a.variazioni) variazioni,
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
                          'PIANOUNICO' cd_unita_piano,
                          'PIANOUNICO' cd_voce_piano,
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
                                 AND   cd_linea_attivita=NVL(decode(P_GAE,'*',null,P_GAE), cd_linea_attivita))
                   UNION ALL
                   SELECT esercizio,
                          CASE WHEN P_ROTTURA_ANNO_LOCAL='S'
                               THEN esercizio_res
                               ELSE 0
                               END esercizio_res,
                          'S' tipo,
                          'PIANOUNICO',
                          'PIANOUNICO',
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
                                 AND   cd_linea_attivita=NVL(decode(P_GAE,'*',null,P_GAE), cd_linea_attivita))
                   UNION ALL
                   SELECT a.esercizio,
                          CASE WHEN P_ROTTURA_ANNO_LOCAL='S'
                               THEN a.esercizio
                               ELSE 0
                               END esercizio_res,
                          'S' tipo,
                          'PIANOUNICO',
                          'PIANOUNICO',
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
                          0 stanziamento, 0 variazioni, 0 impegnato, 0 pagato,
                          CASE WHEN NVL(a.im_spese_gest_accentrata_int,0)!=0 OR
                                    NVL(a.im_spese_gest_accentrata_est,0)!=0
                               THEN 1
                               ELSE 0
                               END contamov
                   FROM pdg_modulo_spese_gest a, v_linea_attivita_valida b
                   WHERE a.cd_cdr_assegnatario = P_CENTRO_RESPONSABILITA
                   AND   b.esercizio = P_ESERCIZIO
                   AND   a.cd_cdr_assegnatario = b.cd_centro_responsabilita
                   AND   a.cd_linea_attivita = b.cd_linea_attivita
                   AND   b.pg_progetto=P_PG_PROGLIV2
                   AND   b.cd_linea_attivita = NVL(decode(P_GAE,'*',null,P_GAE), b.cd_linea_attivita)
                   UNION ALL
                   SELECT a.esercizio,
                          CASE WHEN P_ROTTURA_ANNO_LOCAL='S'
                               THEN a.esercizio
                               ELSE 0
                               END esercizio_res,
                          'S' tipo,
                          'PIANOUNICO',
                          'PIANOUNICO',
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
                          0 stanziamento, 0 variazioni, 0 impegnato, 0 pagato,
                          CASE WHEN NVL(b.im_spese_gest_accentrata_int,0)!=0 OR
                                    NVL(b.im_spese_gest_accentrata_est,0)!=0
                               THEN 1
                               ELSE 0
                               END contamov
                   FROM pdg_variazione a,
                        pdg_variazione_riga_gest b,
                        v_linea_attivita_valida c
                   WHERE a.esercizio = b.esercizio
                   AND   a.pg_variazione_pdg = b.pg_variazione_pdg
                   AND   a.stato IN ('APP', 'APF')
                   AND   b.cd_cdr_assegnatario = P_CENTRO_RESPONSABILITA
                   AND   c.esercizio = P_ESERCIZIO
                   AND   b.cd_cdr_assegnatario = c.cd_centro_responsabilita
                   AND   b.cd_linea_attivita = c.cd_linea_attivita
                   AND   c.pg_progetto=P_PG_PROGLIV2
                   AND   c.cd_linea_attivita = NVL(decode(P_GAE,'*',null,P_GAE), c.cd_linea_attivita)
                   UNION ALL
                   SELECT c.esercizio,
                          CASE WHEN P_ROTTURA_ANNO_LOCAL='S'
                               THEN c.esercizio_originale
                               ELSE 0
                               END esercizio_res,
                          'S' tipo,
                          'PIANOUNICO',
                          'PIANOUNICO',
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
                          0 impegnato, 0 pagato, 1 contamov
                   FROM obbligazione_scad_voce a, obbligazione_scadenzario b, obbligazione c, v_linea_attivita_valida d
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
                   UNION ALL
                   SELECT c.esercizio,
                          CASE WHEN P_ROTTURA_ANNO_LOCAL='S'
                               THEN c.esercizio_originale
                               ELSE 0
                               END esercizio_res,
                          'S' tipo,
                          'PIANOUNICO',
                          'PIANOUNICO',
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
                          0 impegnato, 0 pagato, 1 contamov
                   FROM obbligazione_scad_voce a, obbligazione_scadenzario b, obbligazione c, v_linea_attivita_valida d
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
                   UNION ALL
                   SELECT c.esercizio,
                          CASE WHEN P_ROTTURA_ANNO_LOCAL='S'
                               THEN c.esercizio_originale
                               ELSE 0
                               END esercizio_res,
                          'E' tipo,
                          'PIANOUNICO',
                          'PIANOUNICO',
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
                          0 impegnato, 0 pagato, 1 contamov
                   FROM accertamento_scad_voce a, accertamento_scadenzario b, accertamento c, v_linea_attivita_valida d
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
                   UNION ALL
                   SELECT c.esercizio,
                          CASE WHEN P_ROTTURA_ANNO_LOCAL='S'
                               THEN c.esercizio_originale
                               ELSE 0
                               END esercizio_res,
                          'E' tipo,
                          'PIANOUNICO',
                          'PIANOUNICO',
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
                          0 impegnato, 0 pagato, 1 contamov
                   FROM accertamento_scad_voce a, accertamento_scadenzario b, accertamento c, v_linea_attivita_valida d
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
                   UNION ALL
                   SELECT a.esercizio,
                          CASE WHEN P_ROTTURA_ANNO_LOCAL='S'
                               THEN 2005
                               ELSE 0
                               END esercizio_res,
                          'S' tipo,
                          'PIANOUNICO',
                          'PIANOUNICO',
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
                          NVL(a.im_residuo, 0), 0 variazioni, 0 impegnato, 0 pagato, 1 contamov
                   FROM pdg_residuo_det a, v_linea_attivita_valida b
                   WHERE a.esercizio = 2005
                   AND   a.stato != 'A'
                   AND   a.cd_cdr_linea = P_CENTRO_RESPONSABILITA
                   AND   b.esercizio = P_ESERCIZIO
                   AND   a.cd_cdr_linea = b.cd_centro_responsabilita
                   AND   a.cd_linea_attivita = b.cd_linea_attivita
                   AND   b.pg_progetto=P_PG_PROGLIV2
                   AND   b.cd_linea_attivita = NVL(decode(P_GAE,'*',null,P_GAE), b.cd_linea_attivita)) a
             GROUP BY a.tipo, a.esercizio_res, a.cd_linea_attivita, a.cd_unita_piano, a.cd_voce_piano, a.cd_elemento_voce) P
             WHERE (p.stanziamento != 0 OR p.variazioni != 0 OR p.impacc != 0 OR p.pagris != 0 or p.nummov!=0)
             AND (P_SOLO_GAE_ATTIVE='N' OR
                  EXISTS(SELECT '1' FROM VOCE_F_SALDI_CDR_LINEA SALDI
                         WHERE SALDI.ESERCIZIO = P_ESERCIZIO
                         AND   SALDI.ESERCIZIO_RES = P.ESERCIZIO_RES
                         AND   SALDI.CD_CENTRO_RESPONSABILITA = P_CENTRO_RESPONSABILITA
                         AND   SALDI.CD_LINEA_ATTIVITA = P.CD_LINEA_ATTIVITA
                         AND   SALDI.TI_GESTIONE = P.TIPO
                         AND   SALDI.CD_ELEMENTO_VOCE = P.CD_ELEMENTO_VOCE)));
     
      --INSERISCO DETAIL VARIAZIONI
      INSERT INTO TMP_STAMPA_SITUAZIONE_PROGDET
         (ESERCIZIO, ESERCIZIO_RES, TI_GESTIONE, 
          CD_CENTRO_RESPONSABILITA, PG_PROGETTO,
          CD_UNITA_PIANO, CD_VOCE_PIANO, 
          CD_LINEA_ATTIVITA, CD_ELEMENTO_VOCE, 
          TIPO_DETAIL, ESERCIZIO_VARIAZIONE, PG_VARIAZIONE,
          DS_VARIAZIONE, IMPORTO_VARIAZIONE)
      (SELECT X.ESERCIZIO, X.ESERCIZIO_RES, X.TI_GESTIONE, 
              P_CENTRO_RESPONSABILITA, P_PG_PROGLIV2,
              'PIANONULLO', 'PIANONULLO', 
              X.CD_LINEA_ATTIVITA, X.CD_ELEMENTO_VOCE,
              'VAR', X.ESERCIZIO_VARIAZIONE, X.PG_VARIAZIONE, X.DS_VARIAZIONE,
              SUM(X.IMPORTO_VARIAZIONE)
       FROM(SELECT A.ESERCIZIO, A.ESERCIZIO ESERCIZIO_RES, A.TI_GESTIONE, 
                   CASE WHEN P_ROTTURA_GAE='S'
                        THEN A.CD_LINEA_ATTIVITA
                        ELSE 'GAE_UNICA'
                        END CD_LINEA_ATTIVITA,
                   CASE WHEN P_ROTTURA_VOCE='S'
                        THEN NVL(getCurrentCdElementoVoce(P_ESERCIZIO, A.ESERCIZIO, A.TI_GESTIONE, A.CD_ELEMENTO_VOCE),
                                 A.CD_ELEMENTO_VOCE)
                        ELSE 'VOCE_UNICA'
                        END CD_ELEMENTO_VOCE,
                    A.ESERCIZIO ESERCIZIO_VARIAZIONE, A.PG_VARIAZIONE_PDG PG_VARIAZIONE, B.DS_VARIAZIONE,
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
                                          AND   cd_linea_attivita=NVL(decode(P_GAE,'*',null,P_GAE), cd_linea_attivita))
            UNION ALL
            SELECT A.ESERCIZIO, A.ESERCIZIO_RES, A.TI_GESTIONE, 
                   CASE WHEN P_ROTTURA_GAE='S'
                        THEN A.CD_LINEA_ATTIVITA
                        ELSE 'GAE_UNICA'
                        END CD_LINEA_ATTIVITA,
                   CASE WHEN P_ROTTURA_VOCE='S'
                        THEN NVL(getCurrentCdElementoVoce(P_ESERCIZIO, A.ESERCIZIO, A.TI_GESTIONE, A.CD_ELEMENTO_VOCE),
                                 a.CD_ELEMENTO_VOCE)
                        ELSE 'VOCE_UNICA'
                        END CD_ELEMENTO_VOCE,
                        A.ESERCIZIO, A.PG_VARIAZIONE, B.DS_VARIAZIONE, NVL(A.IM_VARIAZIONE,0)
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
                                          AND   cd_linea_attivita=NVL(decode(P_GAE,'*',null,P_GAE), cd_linea_attivita))
            UNION ALL
            SELECT A.ESERCIZIO, A.ESERCIZIO_ORIGINALE, A.TI_GESTIONE, 
                   CASE WHEN P_ROTTURA_GAE='S'
                        THEN a.CD_LINEA_ATTIVITA
                        ELSE 'GAE_UNICA'
                        END CD_LINEA_ATTIVITA,
                   CASE WHEN P_ROTTURA_VOCE='S'
                        THEN NVL(getCurrentCdElementoVoce(P_ESERCIZIO, A.ESERCIZIO_ORIGINALE, A.TI_GESTIONE, C.CD_ELEMENTO_VOCE),
                                 C.CD_ELEMENTO_VOCE)
                        ELSE 'VOCE_UNICA'
                        END CD_ELEMENTO_VOCE,
                   C.ESERCIZIO, 99999, 'Variazione fittizia per cambiamento piano dei conti',
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
                                          AND   cd_linea_attivita=NVL(decode(P_GAE,'*',null,P_GAE), cd_linea_attivita))
            UNION ALL
            SELECT A.ESERCIZIO, A.ESERCIZIO_ORIGINALE, A.TI_GESTIONE, 
                   CASE WHEN P_ROTTURA_GAE='S'
                        THEN A.CD_LINEA_ATTIVITA
                        ELSE 'GAE_UNICA'
                        END CD_LINEA_ATTIVITA,
                   CASE WHEN P_ROTTURA_VOCE='S'
                        THEN C.cd_elemento_voce_next
                        ELSE 'VOCE_UNICA'
                        END CD_ELEMENTO_VOCE,
                   C.ESERCIZIO, 99999, 'Variazione fittizia per cambiamento piano dei conti',
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
                                          AND   cd_linea_attivita=NVL(decode(P_GAE,'*',null,P_GAE), cd_linea_attivita))) X
       GROUP BY X.ESERCIZIO, X.ESERCIZIO_RES, X.TI_GESTIONE, 
                X.CD_LINEA_ATTIVITA, X.CD_ELEMENTO_VOCE,
                X.ESERCIZIO_VARIAZIONE, X.PG_VARIAZIONE, X.DS_VARIAZIONE);
  End Loop;
End;
/


