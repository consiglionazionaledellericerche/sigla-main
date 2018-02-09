CREATE OR REPLACE Procedure         PRC_LOAD_TABLE_STAMPA_RENDIC
(P_ESERCIZIO IN ESERCIZIO.ESERCIZIO%TYPE,
 P_TIPO IN VARCHAR2, --     Valori ammessi E per Entrate S per Spese
 P_NUM_LIV IN NUMBER,
 P_TIPO_AGGREGAZIONE IN VARCHAR2, -- Valori ammessi SCI per Scientifico FIN per finanziario
 P_TIPO_RENDICONTO IN VARCHAR2, -- Valori ammessi DEC per decisionale GES per gestionale
 P_ORIGINE IN VARCHAR2 DEFAULT 'REA', -- Valori ammessi REA per dati reali EXT per dati esterni
 P_COMPLETA IN VARCHAR2 DEFAULT 'Y',
 P_AGG_COMPETENZA_AC IN VARCHAR2 DEFAULT 'N',
 P_AGG_RESIDUI_AC IN VARCHAR2 DEFAULT 'N',
 P_AGG_CASSA_AC IN VARCHAR2 DEFAULT 'N', 
 P_AGG_COMPETENZA_AP IN VARCHAR2 DEFAULT 'N',
 P_AGG_RESIDUI_AP IN VARCHAR2 DEFAULT 'N',
 P_AGG_CASSA_AP IN VARCHAR2 DEFAULT 'N', 
 P_UTCR IN VARCHAR2 DEFAULT 'SYSTEM'
) Is
    FONTE_DATI                  VARCHAR2(6);
    
    /* Gestione di Competenza */
    PREV_INI                    NUMBER(17,2) :=0;--Iniziali
    VARIAZIONI                  NUMBER(17,2) :=0;--Variazioni in aumento
    ASSESTATO                   NUMBER(17,2) :=0;--Variazioni Definitive
    RIS_PAG                     NUMBER(17,2) :=0;--Riscosse/Pagate
    RIM_RIS_PAG                 NUMBER(17,2) :=0;--Rimaste da Riscuotere/Pagare
    TOT_ACC_IMP                 NUMBER(17,2) :=0;--Totali Accertament/Impegni
    DIFF_COMP                   NUMBER(17,2) :=0;--Differenze rispetto alle Previsioni

    /* Gestione dei Residui Attivi */
    RES_INI_ES                  NUMBER(17,2) :=0;--Residui all'inizio dell'esercizio
    RES_RIS_PAG                 NUMBER(17,2) :=0;--Riscossioni - Pagamenti
    RES_RIM_RIS_PAG             NUMBER(17,2) :=0;--Rimasti da riscuotere/pagare
    RES_TOTALI                  NUMBER(17,2) :=0;--Totali
    RES_VAR_PIU                 NUMBER(17,2) :=0;--Variazioni +
    RES_VAR_MENO                NUMBER(17,2) :=0;--Variazioni -

    PREVISIONE_INI_ES_PREC      NUMBER(17,2) :=0;
    SALDO_VARIAZIONI_ES_PREC    NUMBER(17,2) :=0;
    TOT_IMPACC_COMP_ES_PREC     NUMBER(17,2) :=0;
    TOT_IMPACC_RES_ES_PREC      NUMBER(17,2) :=0;
    TOT_MANREV_COMP_ES_PREC     NUMBER(17,2) :=0;
    TOT_MANREV_RES_ES_PREC      NUMBER(17,2) :=0;
    TOT_MOD_IMPACC_RES_ES_PREC  NUMBER(17,2) :=0;
    ASSESTATO_CASSA_ES_PREC     NUMBER(17,2) :=0;

    /* Gestione di Cassa */
    CASSA_PREV                  NUMBER(17,2) :=0;--Previsioni
    CASSA_RIS_PAG               NUMBER(17,2) :=0;--Riscossioni/Pagamenti
    CASSA_DIFF                  NUMBER(17,2) :=0;--Differenze rispetto alle previsioni

    TOT_RES_TER                 NUMBER(17,2) :=0;--Totale residui al termine dell'esecizio

    RES_INI_ES_PREC             NUMBER(17,2) :=0;--Residui anno precedente
    TOT_ACC_IMP_PREC            NUMBER(17,2) :=0;--Totali Accertament/Impegni anno precedente
    RIS_PAG_PREC                NUMBER(17,2) :=0;--Riscosse/Pagate anno precedente
    CASSA_PREV_ES_PREC          NUMBER(17,2) :=0;

    SEQUENZA                    NUMBER := 0;
    NUM_MAX_LIV                 NUMBER;

    PARLIV1_DS                  VARCHAR2(20);
    PARLIV2_DS                  VARCHAR2(20);
    PARLIV3_DS                  VARCHAR2(20);
    PARLIV4_DS                  VARCHAR2(20);
    PARLIV5_DS                  VARCHAR2(20);
    PARLIV6_DS                  VARCHAR2(20);
    PARLIV7_DS                  VARCHAR2(20);
    PARLIV8_DS                  VARCHAR2(20);
    PARLIV9_DS                  VARCHAR2(20);

    INNER_TIPO_AGGREGAZIONE     VARCHAR2(3);
Begin
  IF Nvl(P_AGG_COMPETENZA_AC,'N')='Y' or Nvl(P_AGG_RESIDUI_AC,'N')='Y' or Nvl(P_AGG_CASSA_AC,'N')='Y' or 
     Nvl(P_AGG_COMPETENZA_AP,'N')='Y' or Nvl(P_AGG_RESIDUI_AP,'N')='Y' or Nvl(P_AGG_CASSA_AP,'N')='Y' THEN

     UPDATE PDG_DATI_STAMPA_RENDIC_TEMP
     SET PREVISIONE_INI = decode(Nvl(P_AGG_COMPETENZA_AC,'N'), 'Y', 0, PREVISIONE_INI),
         SALDO_VARIAZIONI = decode(Nvl(P_AGG_COMPETENZA_AC,'N'), 'Y', 0, SALDO_VARIAZIONI),
         TOT_IMPACC_COMP = decode(Nvl(P_AGG_COMPETENZA_AC,'N'), 'Y', 0, TOT_IMPACC_COMP),
         TOT_MANREV_COMP = decode(Nvl(P_AGG_COMPETENZA_AC,'N'), 'Y', 0, TOT_MANREV_COMP),
         TOT_IMPACC_RES = decode(Nvl(P_AGG_RESIDUI_AC,'N'), 'Y', 0, TOT_IMPACC_RES),
         TOT_MOD_IMPACC_RES = decode(Nvl(P_AGG_RESIDUI_AC,'N'), 'Y', 0, TOT_MOD_IMPACC_RES),
         TOT_MANREV_RES = decode(Nvl(P_AGG_RESIDUI_AC,'N'), 'Y', 0, TOT_MANREV_RES),
         ASSESTATO_CASSA = decode(Nvl(P_AGG_CASSA_AC,'N'), 'Y', 0, ASSESTATO_CASSA),
         PREVISIONE_INI_ES_PREC = decode(Nvl(P_AGG_COMPETENZA_AP,'N'), 'Y', 0, PREVISIONE_INI_ES_PREC),
         SALDO_VARIAZIONI_ES_PREC = decode(Nvl(P_AGG_COMPETENZA_AP,'N'), 'Y', 0, SALDO_VARIAZIONI_ES_PREC),
         TOT_IMPACC_COMP_ES_PREC = decode(Nvl(P_AGG_COMPETENZA_AP,'N'), 'Y', 0, TOT_IMPACC_COMP_ES_PREC),
         TOT_MANREV_COMP_ES_PREC = decode(Nvl(P_AGG_COMPETENZA_AP,'N'), 'Y', 0, TOT_MANREV_COMP_ES_PREC),
         TOT_IMPACC_RES_ES_PREC = decode(Nvl(P_AGG_RESIDUI_AP,'N'), 'Y', 0, TOT_IMPACC_RES_ES_PREC),
         TOT_MOD_IMPACC_RES_ES_PREC = decode(Nvl(P_AGG_RESIDUI_AP,'N'), 'Y', 0, TOT_MOD_IMPACC_RES_ES_PREC),
         TOT_MANREV_RES_ES_PREC = decode(Nvl(P_AGG_RESIDUI_AP,'N'), 'Y', 0, TOT_MANREV_RES_ES_PREC),
         ASSESTATO_CASSA_ES_PREC = decode(Nvl(P_AGG_CASSA_AP,'N'), 'Y', 0, ASSESTATO_CASSA_ES_PREC),
         UTUV = P_UTCR,
         DUVA = SYSDATE,
         PG_VER_REC = PG_VER_REC + 1
     WHERE ESERCIZIO = P_ESERCIZIO;

     for rec in (SELECT a.esercizio, a.CD_CENTRO_RESPONSABILITA, a.tipo, 
                        a.cd_elemento_voce, a.cd_livello1 CD_MISSIONE, a.cd_livello2 CD_PROGRAMMA,
                        a.PREVISIONE_INI, a.SALDO_VARIAZIONI, a.TOT_IMPACC_COMP, a.TOT_IMPACC_RES,
                        a.TOT_MANREV_COMP, a.TOT_MANREV_RES, a.TOT_MOD_IMPACC_RES, a.ASSESTATO_CASSA, 
                        a.PREVISIONE_INI_ES_PREC, a.SALDO_VARIAZIONI_ES_PREC, a.TOT_IMPACC_COMP_ES_PREC, 
                        a.TOT_IMPACC_RES_ES_PREC, a.TOT_MANREV_COMP_ES_PREC, a.TOT_MANREV_RES_ES_PREC, 
                        a.TOT_MOD_IMPACC_RES_ES_PREC, a.ASSESTATO_CASSA_ES_PREC
                  FROM v_stampa_bilancio_rendic_x_cdr a
                WHERE a.esercizio = P_ESERCIZIO
                And   ((a.tipo='E' and a.fonte = 'REAFIN') or
                       (a.tipo='S' and a.fonte = 'REASCI'))) Loop
     
        Update PDG_DATI_STAMPA_RENDIC_TEMP
        SET PREVISIONE_INI = decode(Nvl(P_AGG_COMPETENZA_AC,'N'), 'Y', rec.PREVISIONE_INI, PREVISIONE_INI),
            SALDO_VARIAZIONI = decode(Nvl(P_AGG_COMPETENZA_AC,'N'), 'Y', rec.SALDO_VARIAZIONI, SALDO_VARIAZIONI),
            TOT_IMPACC_COMP = decode(Nvl(P_AGG_COMPETENZA_AC,'N'), 'Y', rec.TOT_IMPACC_COMP, TOT_IMPACC_COMP),
            TOT_MANREV_COMP = decode(Nvl(P_AGG_COMPETENZA_AC,'N'), 'Y', rec.TOT_MANREV_COMP, TOT_MANREV_COMP),
            TOT_IMPACC_RES = decode(Nvl(P_AGG_RESIDUI_AC,'N'), 'Y', rec.TOT_IMPACC_RES, TOT_IMPACC_RES),
            TOT_MOD_IMPACC_RES = decode(Nvl(P_AGG_RESIDUI_AC,'N'), 'Y', rec.TOT_MOD_IMPACC_RES, TOT_MOD_IMPACC_RES),
            TOT_MANREV_RES = decode(Nvl(P_AGG_RESIDUI_AC,'N'), 'Y', rec.TOT_MANREV_RES, TOT_MANREV_RES),
            ASSESTATO_CASSA = decode(Nvl(P_AGG_CASSA_AC,'N'), 'Y', rec.ASSESTATO_CASSA, ASSESTATO_CASSA),
            PREVISIONE_INI_ES_PREC = decode(Nvl(P_AGG_COMPETENZA_AP,'N'), 'Y', rec.PREVISIONE_INI_ES_PREC, PREVISIONE_INI_ES_PREC),
            SALDO_VARIAZIONI_ES_PREC = decode(Nvl(P_AGG_COMPETENZA_AP,'N'), 'Y', rec.SALDO_VARIAZIONI_ES_PREC, SALDO_VARIAZIONI_ES_PREC),
            TOT_IMPACC_COMP_ES_PREC = decode(Nvl(P_AGG_COMPETENZA_AP,'N'), 'Y', rec.TOT_IMPACC_COMP_ES_PREC, TOT_IMPACC_COMP_ES_PREC),
            TOT_MANREV_COMP_ES_PREC = decode(Nvl(P_AGG_COMPETENZA_AP,'N'), 'Y', rec.TOT_MANREV_COMP_ES_PREC, TOT_MANREV_COMP_ES_PREC),
            TOT_IMPACC_RES_ES_PREC = decode(Nvl(P_AGG_RESIDUI_AP,'N'), 'Y', rec.TOT_IMPACC_RES_ES_PREC, TOT_IMPACC_RES_ES_PREC),
            TOT_MOD_IMPACC_RES_ES_PREC = decode(Nvl(P_AGG_RESIDUI_AP,'N'), 'Y', rec.TOT_MOD_IMPACC_RES_ES_PREC, TOT_MOD_IMPACC_RES_ES_PREC),
            TOT_MANREV_RES_ES_PREC = decode(Nvl(P_AGG_RESIDUI_AP,'N'), 'Y', rec.TOT_MANREV_RES_ES_PREC, TOT_MANREV_RES_ES_PREC),
            ASSESTATO_CASSA_ES_PREC = decode(Nvl(P_AGG_CASSA_AP,'N'), 'Y', rec.ASSESTATO_CASSA_ES_PREC, ASSESTATO_CASSA_ES_PREC),
            UTUV = P_UTCR,
            DUVA = SYSDATE,
            PG_VER_REC = PG_VER_REC + 1
        where ESERCIZIO = P_ESERCIZIO
        and   CD_CENTRO_RESPONSABILITA = rec.CD_CENTRO_RESPONSABILITA
        and   TI_GESTIONE = rec.TIPO
        and   CD_ELEMENTO_VOCE = rec.CD_ELEMENTO_VOCE
        and   (rec.TIPO = 'E' OR
               (
                ((rec.CD_PROGRAMMA IS NULL AND CD_PROGRAMMA IS NULL) OR CD_PROGRAMMA = rec.CD_PROGRAMMA) and   
                ((rec.CD_MISSIONE IS NULL AND CD_MISSIONE IS NULL) OR CD_MISSIONE = rec.CD_MISSIONE)
               )
              );

        If sql%rowcount=0 Then
           INSERT INTO PDG_DATI_STAMPA_RENDIC_TEMP
                 (ESERCIZIO, CD_CENTRO_RESPONSABILITA, TI_GESTIONE, CD_ELEMENTO_VOCE, CD_PROGRAMMA, CD_MISSIONE,
                  PREVISIONE_INI, SALDO_VARIAZIONI, TOT_IMPACC_COMP, TOT_MANREV_COMP, 
                  TOT_IMPACC_RES, TOT_MOD_IMPACC_RES, TOT_MANREV_RES, ASSESTATO_CASSA, 
                  PREVISIONE_INI_ES_PREC, SALDO_VARIAZIONI_ES_PREC, TOT_IMPACC_COMP_ES_PREC, TOT_MANREV_COMP_ES_PREC, 
                  TOT_IMPACC_RES_ES_PREC, TOT_MOD_IMPACC_RES_ES_PREC, TOT_MANREV_RES_ES_PREC, ASSESTATO_CASSA_ES_PREC,
                  UTCR, DACR, UTUV, DUVA, PG_VER_REC)
           VALUES
                 (P_ESERCIZIO, rec.CD_CENTRO_RESPONSABILITA, rec.TIPO, rec.CD_ELEMENTO_VOCE,rec.cd_programma, rec.CD_MISSIONE, 
                  decode(Nvl(P_AGG_COMPETENZA_AC,'N'), 'Y', rec.PREVISIONE_INI, 0),
                  decode(Nvl(P_AGG_COMPETENZA_AC,'N'), 'Y', rec.SALDO_VARIAZIONI, 0),
                  decode(Nvl(P_AGG_COMPETENZA_AC,'N'), 'Y', rec.TOT_IMPACC_COMP, 0),
                  decode(Nvl(P_AGG_COMPETENZA_AC,'N'), 'Y', rec.TOT_MANREV_COMP, 0),
                  decode(Nvl(P_AGG_RESIDUI_AC,'N'), 'Y', rec.TOT_IMPACC_RES, 0),
                  decode(Nvl(P_AGG_RESIDUI_AC,'N'), 'Y', rec.TOT_MOD_IMPACC_RES, 0),
                  decode(Nvl(P_AGG_RESIDUI_AC,'N'), 'Y', rec.TOT_MANREV_RES, 0),
                  decode(Nvl(P_AGG_CASSA_AC,'N'), 'Y', rec.ASSESTATO_CASSA, 0),
                  decode(Nvl(P_AGG_COMPETENZA_AP,'N'), 'Y', rec.PREVISIONE_INI_ES_PREC, 0),
                  decode(Nvl(P_AGG_COMPETENZA_AP,'N'), 'Y', rec.SALDO_VARIAZIONI_ES_PREC, 0),
                  decode(Nvl(P_AGG_COMPETENZA_AP,'N'), 'Y', rec.TOT_IMPACC_COMP_ES_PREC, 0),
                  decode(Nvl(P_AGG_COMPETENZA_AP,'N'), 'Y', rec.TOT_MANREV_COMP_ES_PREC, 0),
                  decode(Nvl(P_AGG_RESIDUI_AP,'N'), 'Y', rec.TOT_IMPACC_RES_ES_PREC, 0),
                  decode(Nvl(P_AGG_RESIDUI_AP,'N'), 'Y', rec.TOT_MOD_IMPACC_RES_ES_PREC, 0),
                  decode(Nvl(P_AGG_RESIDUI_AP,'N'), 'Y', rec.TOT_MANREV_RES_ES_PREC, 0),
                  decode(Nvl(P_AGG_CASSA_AP,'N'), 'Y', rec.ASSESTATO_CASSA_ES_PREC, 0),
                  P_UTCR, SYSDATE, P_UTCR, SYSDATE, 1);
        End If;
     End Loop;

     Delete PDG_DATI_STAMPA_RENDIC_TEMP
     where PREVISIONE_INI = 0 
     and   SALDO_VARIAZIONI = 0
     and   TOT_IMPACC_COMP = 0
     and   TOT_MANREV_COMP = 0
     and   TOT_IMPACC_RES = 0
     and   TOT_MOD_IMPACC_RES = 0
     and   TOT_MANREV_RES = 0
     and   ASSESTATO_CASSA = 0
     and   PREVISIONE_INI_ES_PREC = 0
     and   SALDO_VARIAZIONI_ES_PREC = 0
     and   TOT_IMPACC_COMP_ES_PREC = 0
     and   TOT_MANREV_COMP_ES_PREC = 0
     and   TOT_IMPACC_RES_ES_PREC = 0
     and   TOT_MOD_IMPACC_RES_ES_PREC = 0
     and   TOT_MANREV_RES_ES_PREC = 0
     and   ASSESTATO_CASSA_ES_PREC = 0;

  Else
     If P_TIPO='E' and P_TIPO_AGGREGAZIONE='SCI' Then
        INNER_TIPO_AGGREGAZIONE := 'FIN';
     Else
        INNER_TIPO_AGGREGAZIONE := P_TIPO_AGGREGAZIONE;
     End If;
 
     FONTE_DATI := P_ORIGINE||INNER_TIPO_AGGREGAZIONE;

     Declare
        rec PARAMETRI_LIVELLI%rowtype;
     Begin
        Select * into rec
        From PARAMETRI_LIVELLI
        Where ESERCIZIO = P_ESERCIZIO;

        If (INNER_TIPO_AGGREGAZIONE = 'SCI') Then
           PARLIV1_DS := 'Area Progettuale';
           PARLIV2_DS := 'Progetto';
           If (P_TIPO = 'E') Then
              PARLIV3_DS := rec.DS_LIVELLO1E;
              PARLIV4_DS := rec.DS_LIVELLO2E;
              PARLIV5_DS := rec.DS_LIVELLO3E;
              PARLIV6_DS := rec.DS_LIVELLO4E;
              PARLIV7_DS := rec.DS_LIVELLO5E;
              PARLIV8_DS := rec.DS_LIVELLO6E;
              PARLIV9_DS := rec.DS_LIVELLO7E;
           Else
              PARLIV3_DS := rec.DS_LIVELLO1S;
              PARLIV4_DS := rec.DS_LIVELLO2S;
              PARLIV5_DS := rec.DS_LIVELLO3S;
              PARLIV6_DS := rec.DS_LIVELLO4S;
              PARLIV7_DS := rec.DS_LIVELLO5S;
              PARLIV8_DS := rec.DS_LIVELLO6S;
              PARLIV9_DS := rec.DS_LIVELLO7S;
           End If;
        Else
           If (P_TIPO = 'E') Then
              PARLIV1_DS := rec.DS_LIVELLO1E;
              PARLIV2_DS := rec.DS_LIVELLO2E;
              PARLIV3_DS := rec.DS_LIVELLO3E;
              PARLIV4_DS := rec.DS_LIVELLO4E;
              PARLIV5_DS := rec.DS_LIVELLO5E;
              PARLIV6_DS := rec.DS_LIVELLO6E;
              PARLIV7_DS := rec.DS_LIVELLO7E;
           Else
              PARLIV1_DS := rec.DS_LIVELLO1S;
              PARLIV2_DS := rec.DS_LIVELLO2S;
              PARLIV3_DS := rec.DS_LIVELLO3S;
              PARLIV4_DS := rec.DS_LIVELLO4S;
              PARLIV5_DS := rec.DS_LIVELLO5S;
              PARLIV6_DS := rec.DS_LIVELLO6S;
              PARLIV7_DS := rec.DS_LIVELLO7S;
           End If;
        End If;
     Exception
        WHEN NO_DATA_FOUND THEN
          NULL;
     End;

     --execute PRC_RENDICONTO_DEC_GEST('00001','E1',2010,'000000000000',null,'DPR',3,'D','E');
     FOR PREV_COMP IN (SELECT esercizio, tipo,  
                              cd_livello1, 
                              DECODE(SIGN(P_NUM_LIV-1),1,cd_livello2,null) cd_livello2,
                              DECODE(SIGN(P_NUM_LIV-2),1,cd_livello3,null) cd_livello3,
                              DECODE(SIGN(P_NUM_LIV-3),1,cd_livello4,null) cd_livello4,
                              DECODE(SIGN(P_NUM_LIV-4),1,cd_livello5,null) cd_livello5,
                              DECODE(SIGN(P_NUM_LIV-5),1,cd_livello6,null) cd_livello6,
                              DECODE(SIGN(P_NUM_LIV-6),1,cd_livello7,null) cd_livello7,
                              DECODE(SIGN(P_NUM_LIV-7),1,cd_livello8,null) cd_livello8,
                              DECODE(SIGN(P_NUM_LIV-8),1,cd_livello9,null) cd_livello9,
                              ds_livello1,
                              DECODE(SIGN(P_NUM_LIV-1),1,ds_livello2,null) ds_livello2,
                              DECODE(SIGN(P_NUM_LIV-2),1,ds_livello3,null) ds_livello3,
                              DECODE(SIGN(P_NUM_LIV-3),1,ds_livello4,null) ds_livello4,
                              DECODE(SIGN(P_NUM_LIV-4),1,ds_livello5,null) ds_livello5,
                              DECODE(SIGN(P_NUM_LIV-5),1,ds_livello6,null) ds_livello6,
                              DECODE(SIGN(P_NUM_LIV-6),1,ds_livello7,null) ds_livello7,
                              DECODE(SIGN(P_NUM_LIV-7),1,ds_livello8,null) ds_livello8,
                              DECODE(SIGN(P_NUM_LIV-8),1,ds_livello9,null) ds_livello9,
                              NVL(SUM(previsione_ini), 0) previsione_ini, 
                              NVL(SUM(saldo_variazioni), 0) saldo_variazioni, 
                              NVL(SUM(tot_impacc_comp), 0) tot_impacc_comp, 
                              NVL(SUM(tot_impacc_res), 0) tot_impacc_res, 
                              NVL(SUM(tot_manrev_comp), 0) tot_manrev_comp, 
                              NVL(SUM(tot_manrev_res), 0) tot_manrev_res, 
                              NVL(SUM(tot_mod_impacc_res), 0) tot_mod_impacc_res, 
                              NVL(SUM(assestato_cassa), 0) assestato_cassa, 
                              NVL(SUM(previsione_ini_es_prec), 0) previsione_ini_es_prec,  
                              NVL(SUM(saldo_variazioni_es_prec), 0) saldo_variazioni_es_prec, 
                              NVL(SUM(tot_impacc_comp_es_prec), 0) tot_impacc_comp_es_prec, 
                              NVL(SUM(tot_impacc_res_es_prec), 0) tot_impacc_res_es_prec, 
                              NVL(SUM(tot_manrev_comp_es_prec), 0) tot_manrev_comp_es_prec, 
                              NVL(SUM(tot_manrev_res_es_prec), 0) tot_manrev_res_es_prec, 
                              NVL(SUM(tot_mod_impacc_res_es_prec), 0) tot_mod_impacc_res_es_prec, 
                              NVL(SUM(assestato_cassa_es_prec), 0) assestato_cassa_es_prec
                        FROM v_stampa_bilancio_rendic
                        WHERE fonte = FONTE_DATI
                          AND esercizio = P_ESERCIZIO
                          AND tipo = P_TIPO
                        GROUP BY 
                              esercizio, 
                              tipo,  
                              cd_livello1, 
                              DECODE(SIGN(P_NUM_LIV-1),1,cd_livello2,null),
                              DECODE(SIGN(P_NUM_LIV-2),1,cd_livello3,null),
                              DECODE(SIGN(P_NUM_LIV-3),1,cd_livello4,null),
                              DECODE(SIGN(P_NUM_LIV-4),1,cd_livello5,null),
                              DECODE(SIGN(P_NUM_LIV-5),1,cd_livello6,null),
                              DECODE(SIGN(P_NUM_LIV-6),1,cd_livello7,null),
                              DECODE(SIGN(P_NUM_LIV-7),1,cd_livello8,null),
                              DECODE(SIGN(P_NUM_LIV-8),1,cd_livello9,null),
                              ds_livello1,
                              DECODE(SIGN(P_NUM_LIV-1),1,ds_livello2,null),
                              DECODE(SIGN(P_NUM_LIV-2),1,ds_livello3,null),
                              DECODE(SIGN(P_NUM_LIV-3),1,ds_livello4,null),
                              DECODE(SIGN(P_NUM_LIV-4),1,ds_livello5,null),
                              DECODE(SIGN(P_NUM_LIV-5),1,ds_livello6,null),
                              DECODE(SIGN(P_NUM_LIV-6),1,ds_livello7,null),
                              DECODE(SIGN(P_NUM_LIV-7),1,ds_livello8,null),
                              DECODE(SIGN(P_NUM_LIV-8),1,ds_livello9,null)) Loop
            
            PREVISIONE_INI_ES_PREC := 0;
            SALDO_VARIAZIONI_ES_PREC := 0;
            TOT_IMPACC_COMP_ES_PREC := 0;
            TOT_IMPACC_RES_ES_PREC := 0;
            TOT_MANREV_COMP_ES_PREC := 0;
            TOT_MANREV_RES_ES_PREC := 0;
            TOT_MOD_IMPACC_RES_ES_PREC := 0;
            ASSESTATO_CASSA_ES_PREC := 0;

            If (P_TIPO_RENDICONTO = 'DEC') Then
              PREVISIONE_INI_ES_PREC := PREV_COMP.PREVISIONE_INI_ES_PREC;
              SALDO_VARIAZIONI_ES_PREC := PREV_COMP.SALDO_VARIAZIONI_ES_PREC;
              TOT_IMPACC_COMP_ES_PREC := PREV_COMP.TOT_IMPACC_COMP_ES_PREC;
              TOT_IMPACC_RES_ES_PREC := PREV_COMP.TOT_IMPACC_RES_ES_PREC;
              TOT_MANREV_COMP_ES_PREC := PREV_COMP.TOT_MANREV_COMP_ES_PREC;
              TOT_MANREV_RES_ES_PREC := PREV_COMP.TOT_MANREV_RES_ES_PREC;
              TOT_MOD_IMPACC_RES_ES_PREC := PREV_COMP.TOT_MOD_IMPACC_RES_ES_PREC;
              ASSESTATO_CASSA_ES_PREC := PREV_COMP.ASSESTATO_CASSA_ES_PREC;
                

              RES_INI_ES_PREC := TOT_IMPACC_RES_ES_PREC;
              TOT_ACC_IMP_PREC := TOT_IMPACC_COMP_ES_PREC;
              RIS_PAG_PREC := TOT_MANREV_COMP_ES_PREC;
              CASSA_PREV_ES_PREC := ASSESTATO_CASSA_ES_PREC;
            End If;

            PREV_INI   := PREV_COMP.PREVISIONE_INI;
            VARIAZIONI := PREV_COMP.SALDO_VARIAZIONI;
            ASSESTATO  := PREV_COMP.PREVISIONE_INI + PREV_COMP.SALDO_VARIAZIONI;

            TOT_ACC_IMP := PREV_COMP.TOT_IMPACC_COMP;
            RIS_PAG     := PREV_COMP.TOT_MANREV_COMP;
            RIM_RIS_PAG := TOT_ACC_IMP - RIS_PAG;
            DIFF_COMP   := TOT_ACC_IMP - ASSESTATO;
            
            RES_INI_ES  := PREV_COMP.TOT_IMPACC_RES;
            RES_RIS_PAG := PREV_COMP.TOT_MANREV_RES;
            RES_TOTALI  := RES_INI_ES + NVL(PREV_COMP.TOT_MOD_IMPACC_RES,0);
            RES_RIM_RIS_PAG := RES_TOTALI - RES_RIS_PAG;

            If (PREV_COMP.TOT_MOD_IMPACC_RES >= 0)then
               RES_VAR_PIU := PREV_COMP.TOT_MOD_IMPACC_RES;
               RES_VAR_MENO := 0;
            Else
               RES_VAR_PIU := 0;
               RES_VAR_MENO := (PREV_COMP.TOT_MOD_IMPACC_RES) * -1;
            End If;

            CASSA_PREV := PREV_COMP.ASSESTATO_CASSA;
            CASSA_RIS_PAG := PREV_COMP.TOT_MANREV_COMP + PREV_COMP.TOT_MANREV_RES ;
            CASSA_DIFF := CASSA_RIS_PAG - PREV_COMP.ASSESTATO_CASSA;

            TOT_RES_TER := RIM_RIS_PAG + RES_RIM_RIS_PAG;

            /*Azzero le somme sui livelli inferiori in quanto vengono calcolate dal report*/
            If ((P_NUM_LIV=2 AND PREV_COMP.CD_LIVELLO2 IS null) OR
                (P_NUM_LIV=3 AND PREV_COMP.CD_LIVELLO3 IS null) OR
                (P_NUM_LIV=4 AND PREV_COMP.CD_LIVELLO4 IS null) OR
                (P_NUM_LIV=5 AND PREV_COMP.CD_LIVELLO5 IS null) OR
                (P_NUM_LIV=6 AND PREV_COMP.CD_LIVELLO6 IS null) OR
                (P_NUM_LIV=7 AND PREV_COMP.CD_LIVELLO7 IS null) OR
                (P_NUM_LIV=8 AND PREV_COMP.CD_LIVELLO8 IS null) OR
                (P_NUM_LIV=9 AND PREV_COMP.CD_LIVELLO9 IS null)) Then
               PREV_INI := 0;
               VARIAZIONI := 0;
               ASSESTATO := 0;
               RIS_PAG := 0;
               RIM_RIS_PAG := 0;
               TOT_ACC_IMP := 0;
               DIFF_COMP := 0;
               RES_INI_ES := 0;
               RES_RIS_PAG := 0;
               RES_RIM_RIS_PAG := 0;
               RES_TOTALI := 0;
               RES_VAR_PIU := 0;
               RES_VAR_MENO := 0;
               CASSA_PREV := 0;
               CASSA_RIS_PAG := 0;
               CASSA_DIFF := 0;
               TOT_RES_TER := 0;
               RES_INI_ES_PREC := NVL(RES_INI_ES_PREC,0);
               TOT_ACC_IMP_PREC := NVL(TOT_ACC_IMP_PREC,0);
               RIS_PAG_PREC := NVL(RIS_PAG_PREC,0);
               PREVISIONE_INI_ES_PREC := 0;
               CASSA_PREV_ES_PREC := 0;
            End If;

            SEQUENZA := SEQUENZA + 1;

            Insert Into TMP_STAMPA_RENDICONTO(
                SEQUENZA, TIPO, TOTALE,
                CD_LIVELLO1, CD_LIVELLO2, CD_LIVELLO3, CD_LIVELLO4,
                CD_LIVELLO5, CD_LIVELLO6, CD_LIVELLO7, CD_LIVELLO8, CD_LIVELLO9,
                DS_LIVELLO1, DS_LIVELLO2, DS_LIVELLO3, DS_LIVELLO4,
                DS_LIVELLO5, DS_LIVELLO6, DS_LIVELLO7, DS_LIVELLO8, DS_LIVELLO9,
                LIVELLO1_DS, LIVELLO2_DS, LIVELLO3_DS, LIVELLO4_DS,
                LIVELLO5_DS, LIVELLO6_DS, LIVELLO7_DS, LIVELLO8_DS, LIVELLO9_DS,
                PREV_INI, VARIAZIONI, ASSESTATO, RIS_PAG, RIM_RIS_PAG, TOT_ACC_IMP, DIFF_COMP,
                RES_INI_ES, RES_RIS_PAG, RES_RIM_RIS_PAG, RES_TOTALI, RES_VAR_PIU, RES_VAR_MENO,
                CASSA_PREV, CASSA_RIS_PAG, CASSA_DIFF, TOT_RES_TER,
                RES_INI_ES_PREC, TOT_ACC_IMP_PREC, RIS_PAG_PREC,
                PREV_INI_ES_PREC, CASSA_PREV_ES_PREC)
            Values(
                SEQUENZA, P_TIPO, 'N',
                PREV_COMP.CD_LIVELLO1, PREV_COMP.CD_LIVELLO2, PREV_COMP.CD_LIVELLO3, PREV_COMP.CD_LIVELLO4,
                PREV_COMP.CD_LIVELLO5, PREV_COMP.CD_LIVELLO6, PREV_COMP.CD_LIVELLO7, PREV_COMP.CD_LIVELLO8, 
                PREV_COMP.CD_LIVELLO9,
                PREV_COMP.DS_LIVELLO1, PREV_COMP.DS_LIVELLO2, PREV_COMP.DS_LIVELLO3, PREV_COMP.DS_LIVELLO4,
                PREV_COMP.DS_LIVELLO5, PREV_COMP.DS_LIVELLO6, PREV_COMP.DS_LIVELLO7, PREV_COMP.DS_LIVELLO8,
                PREV_COMP.DS_LIVELLO9,
                PARLIV1_DS, PARLIV2_DS, PARLIV3_DS, PARLIV4_DS, 
                PARLIV5_DS, PARLIV6_DS, PARLIV7_DS, PARLIV8_DS, PARLIV9_DS,
                PREV_INI, VARIAZIONI, ASSESTATO, RIS_PAG, RIM_RIS_PAG, TOT_ACC_IMP, DIFF_COMP,
                RES_INI_ES, RES_RIS_PAG, RES_RIM_RIS_PAG, RES_TOTALI, RES_VAR_PIU, RES_VAR_MENO,
                CASSA_PREV, CASSA_RIS_PAG, CASSA_DIFF, TOT_RES_TER,
                RES_INI_ES_PREC, TOT_ACC_IMP_PREC, RIS_PAG_PREC,
                PREVISIONE_INI_ES_PREC, CASSA_PREV_ES_PREC);
      End Loop;
      
      If (P_COMPLETA = 'Y') Then
         If (P_TIPO = 'S') Then
            PRC_LOAD_TABLE_STAMPA_RENDIC(P_ESERCIZIO, 'E', P_NUM_LIV, P_TIPO_AGGREGAZIONE, P_TIPO_RENDICONTO, P_ORIGINE, 'N');
         Else
            PRC_LOAD_TABLE_STAMPA_RENDIC(P_ESERCIZIO, 'S', P_NUM_LIV, P_TIPO_AGGREGAZIONE, P_TIPO_RENDICONTO, P_ORIGINE, 'N');
         End If;
      End If;
    End If;
End;
/


