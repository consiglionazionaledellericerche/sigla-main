CREATE OR REPLACE Procedure PRT_PREV_FIN_ENT_SPE_DEC
(INES           IN NUMBER,
 INGEST         IN CHAR) Is

 aId    NUMBER;

 i NUMBER := 0;

-- VARIABILI PARTE ENTRATE (NUOVA PREVISIONE E ESERCIZIO PRECEDENTE)

 RES_PRES_AC            NUMBER;
 PREV_COMPETENZA_AC     NUMBER;
 PREV_CASSA_AC          NUMBER;

 INI_AP                 NUMBER;
 VAR_PIU_AP             NUMBER;
 VAR_MENO_AP            NUMBER;

 RES_ASS_AP             NUMBER;
 VAR_PIU_RES_AP         NUMBER;
 VAR_MENO_RES_AP        NUMBER;
 STANZ_RES_INI          NUMBER;

 RES_INIZ_REALI_AP      NUMBER;
 PREV_ASS_COMP_AP       NUMBER;
 PREV_ASS_CASSA_AP      NUMBER;

 ULTIMO_ANNO            NUMBER;
 VOCE_FONDO             CHAR(1);

Begin

SELECT IBMSEQ00_CR_PACKAGE.NEXTVAL
INTO aId
FROM dual;

-- PREVENTIVO DECISIONALE SPESA

If INGEST = 'E' Then

-- RECUPERO PRIMA LA NUOVA PREVISIONE DI SPESA DI COMPETENZA

For NEW_PREV_COMP In  (Select  PDG_MODULO_ENTRATE.PG_PROGETTO,
                               CLASS.CD_LIVELLO1,
                               CLASS.CD_LIVELLO2,
                               Sum(IM_ENTRATA_APP) TOT_PREV_COMP
                       From    PDG_MODULO_ENTRATE,
                               V_CLASSIFICAZIONE_VOCI CLASS
                       Where   PDG_MODULO_ENTRATE.ESERCIZIO = INES               AND
                               CLASS.ID_CLASSIFICAZIONE     = PDG_MODULO_ENTRATE.ID_CLASSIFICAZIONE
                       Group By PDG_MODULO_ENTRATE.PG_PROGETTO, CLASS.CD_LIVELLO1, CLASS.CD_LIVELLO2) Loop

I := I + 1;
Insert Into PRT_VPG_PREV_FIN_ENT_SPE_DEC_D (ID, CHIAVE, TIPO, SEQUENZA,
                                            ESERCIZIO, CDR, LINEA, PG_MODULO, DIP, CD_LIVELLO1, CD_LIVELLO2,
                                            PREV_COMPETENZA_AC)
Values
(1, 'SUB', 'C', I,
 INES, Null, Null, NEW_PREV_COMP.PG_PROGETTO, Get_Dip_From_Modulo(INES, NEW_PREV_COMP.PG_PROGETTO, 'X'),
 NEW_PREV_COMP.CD_LIVELLO1, NEW_PREV_COMP.CD_LIVELLO2, NEW_PREV_COMP.TOT_PREV_COMP);

End Loop;

-- POI RECUPERO I DATI STORICIZZATI PER L'ANNO IN PREVISIONE: I RESIDUI PRESUNTI ALLA FINE DELL'ESERCIZIO IN CORSO e
--                                                            PREVISIONI DI CASSA (NON LE PRENDO DAL PDG)

For CUR_DATI_STORICI_AC In (Select CD_DIPARTIMENTO, CD_LIVELLO1, CD_LIVELLO2, RES_PRES_FINE_ES_PREC, PREV_CASSA
                            From   PARAMETRI_REND_PREV_DEC
                            Where  ESERCIZIO = INES And
                                   TI_GESTIONE = 'E') Loop

I := I + 1;
Insert Into PRT_VPG_PREV_FIN_ENT_SPE_DEC_D (ID, CHIAVE, TIPO, SEQUENZA,
                                        ESERCIZIO, CDR, LINEA, PG_MODULO, DIP, CD_LIVELLO1, CD_LIVELLO2,
                                        RES_PRES_AC, PREV_CASSA_AC)
Values
(1, 'SUB', 'C', I,
 INES, Null, Null, Null, CUR_DATI_STORICI_AC.CD_DIPARTIMENTO,
 CUR_DATI_STORICI_AC.CD_LIVELLO1, CUR_DATI_STORICI_AC.CD_LIVELLO2, CUR_DATI_STORICI_AC.RES_PRES_FINE_ES_PREC,
 CUR_DATI_STORICI_AC.PREV_CASSA);
End Loop;

-- POI RECUPERO I DATI STORICIZZATI PER L'ANNO PRECEDENTE: LE PREVISIONI ASSESTATE DI CASSA (NON DAI SALDI)

For CUR_DATI_STORICI_AP In (Select CD_DIPARTIMENTO, CD_LIVELLO1, CD_LIVELLO2, PREV_ASS_CASSA
                            From   PARAMETRI_REND_PREV_DEC
                            Where  ESERCIZIO = INES-1 And
                                   TI_GESTIONE = 'E') Loop

I := I + 1;
Insert Into PRT_VPG_PREV_FIN_ENT_SPE_DEC_D (ID, CHIAVE, TIPO, SEQUENZA,
                                        ESERCIZIO, CDR, LINEA, PG_MODULO, DIP, CD_LIVELLO1, CD_LIVELLO2,
                                        PREV_ASS_CASSA_AP)
Values
(1, 'SUB', 'C', I,
 INES, Null, Null, Null, CUR_DATI_STORICI_AP.CD_DIPARTIMENTO,
 CUR_DATI_STORICI_AP.CD_LIVELLO1, CUR_DATI_STORICI_AP.CD_LIVELLO2, CUR_DATI_STORICI_AP.PREV_ASS_CASSA);
End Loop;


-- LOOP SUI SALDI DELL'ESERCIZIO PRECEDENTE A QUELLO DI PREVISIONE PER PRENDERE:
-- I RESIDUI INIZIALI
-- LE PREVISIONI DI COMPETENZA ASSESTATE

For COMB_CONTABILI_AP In
                      (Select  DISTINCT
                               SALDI.CD_CENTRO_RESPONSABILITA CDR,
                               SALDI.CD_LINEA_ATTIVITA,
                               SALDI.TI_GESTIONE,
                               SALDI.TI_APPARTENENZA,
                               SALDI.CD_ELEMENTO_VOCE,
                               LINEA_ATTIVITA.PG_PROGETTO,
                               CLASS.CD_LIVELLO1,
                               CLASS.CD_LIVELLO2
                       From    VOCE_F_SALDI_CDR_LINEA SALDI,
                       	       ELEMENTO_VOCE,
                               V_CLASSIFICAZIONE_VOCI CLASS,
                       	       LINEA_ATTIVITA
                       Where   SALDI.ESERCIZIO                = INES-1                          And
                               SALDI.TI_GESTIONE              = 'E'                             And
                               ELEMENTO_VOCE.ESERCIZIO        = SALDI.ESERCIZIO                 And
                               ELEMENTO_VOCE.TI_APPARTENENZA  = SALDI.TI_APPARTENENZA           And
                               ELEMENTO_VOCE.TI_GESTIONE      = SALDI.TI_GESTIONE               And
                               ELEMENTO_VOCE.CD_ELEMENTO_VOCE = SALDI.CD_ELEMENTO_VOCE          And
                               ELEMENTO_VOCE.ID_CLASSIFICAZIONE = CLASS.ID_CLASSIFICAZIONE      And
                               CLASS.ESERCIZIO                = ELEMENTO_VOCE.ESERCIZIO         And
                               CLASS.TI_GESTIONE              = ELEMENTO_VOCE.TI_GESTIONE       And
                               SALDI.CD_CENTRO_RESPONSABILITA = LINEA_ATTIVITA.CD_CENTRO_RESPONSABILITA And
                               SALDI.CD_LINEA_ATTIVITA	      = LINEA_ATTIVITA.CD_LINEA_ATTIVITA        And
                               LINEA_ATTIVITA.PG_PROGETTO     IS NOT NULL) Loop

Select Nvl(FL_VOCE_FONDO, 'N')
Into   VOCE_FONDO
From   ELEMENTO_VOCE
Where  ESERCIZIO        = INES-1                            And
       TI_APPARTENENZA  = COMB_CONTABILI_AP.TI_APPARTENENZA And
       TI_GESTIONE      = COMB_CONTABILI_AP.TI_GESTIONE     And
       CD_ELEMENTO_VOCE = COMB_CONTABILI_AP.CD_ELEMENTO_VOCE;

If VOCE_FONDO = 'N' Then
   INI_AP      := cnrutl002.RF_IM_STANZ_INIZIALE_A1 (INES-1, INES-1, COMB_CONTABILI_AP.CDR, COMB_CONTABILI_AP.CD_LINEA_ATTIVITA, COMB_CONTABILI_AP.TI_APPARTENENZA, COMB_CONTABILI_AP.TI_GESTIONE, NULL, COMB_CONTABILI_AP.CD_ELEMENTO_VOCE);
Elsif VOCE_FONDO = 'Y' Then
  begin
   Select IM_STANZ_INIZIALE_A1
   Into   INI_AP
   From   VOCE_F_SALDI_CDR_LINEA
   Where  ESERCIZIO                  = INES-1                              AND
          ESERCIZIO_RES              = INES-1                              And
          CD_CENTRO_RESPONSABILITA   = COMB_CONTABILI_AP.CDR               AND
          CD_LINEA_ATTIVITA          = COMB_CONTABILI_AP.CD_LINEA_ATTIVITA AND
          TI_APPARTENENZA            = COMB_CONTABILI_AP.TI_APPARTENENZA   AND
          TI_GESTIONE                = COMB_CONTABILI_AP.TI_GESTIONE       AND
          CD_ELEMENTO_VOCE           = COMB_CONTABILI_AP.CD_ELEMENTO_VOCE;
    exception when no_Data_found then
    	INI_AP:=0;
    end;
End If;

   VAR_PIU_AP  := cnrutl002.RF_variazioni_piu (INES-1, INES-1, COMB_CONTABILI_AP.CDR, COMB_CONTABILI_AP.CD_LINEA_ATTIVITA, COMB_CONTABILI_AP.TI_APPARTENENZA, COMB_CONTABILI_AP.TI_GESTIONE, NULL, COMB_CONTABILI_AP.CD_ELEMENTO_VOCE);
   VAR_MENO_AP := Abs(cnrutl002.RF_variazioni_meno (INES-1, INES-1, COMB_CONTABILI_AP.CDR, COMB_CONTABILI_AP.CD_LINEA_ATTIVITA, COMB_CONTABILI_AP.TI_APPARTENENZA, COMB_CONTABILI_AP.TI_GESTIONE, NULL, COMB_CONTABILI_AP.CD_ELEMENTO_VOCE));



RES_ASS_AP      := cnrutl002.RF_IM_OBBL_RES_PRO (INES-1, NULL, COMB_CONTABILI_AP.CDR, COMB_CONTABILI_AP.CD_LINEA_ATTIVITA, COMB_CONTABILI_AP.TI_APPARTENENZA, COMB_CONTABILI_AP.TI_GESTIONE, NULL, COMB_CONTABILI_AP.CD_ELEMENTO_VOCE);
--VAR_PIU_RES_AP  := cnrutl002.VAR_PIU_OBBL_RES_PRO (INES-1, NULL, COMB_CONTABILI_AP.CDR, COMB_CONTABILI_AP.CD_LINEA_ATTIVITA, COMB_CONTABILI_AP.TI_APPARTENENZA, COMB_CONTABILI_AP.TI_GESTIONE, NULL, COMB_CONTABILI_AP.CD_ELEMENTO_VOCE);
--VAR_MENO_RES_AP := cnrutl002.VAR_MENO_OBBL_RES_PRO (INES-1, NULL, COMB_CONTABILI_AP.CDR, COMB_CONTABILI_AP.CD_LINEA_ATTIVITA, COMB_CONTABILI_AP.TI_APPARTENENZA, COMB_CONTABILI_AP.TI_GESTIONE, NULL, COMB_CONTABILI_AP.CD_ELEMENTO_VOCE);


I := I + 1;
--dbms_output.put_line('I '||i||' cdr '||COMB_CONTABILI_AP.CDR||' gae '||COMB_CONTABILI_AP.CD_LINEA_ATTIVITA||' dip '||nvl(Get_Dip_From_Modulo(INES-1, COMB_CONTABILI_AP.PG_PROGETTO, 'X'),'BOO'));
Insert Into PRT_VPG_PREV_FIN_ENT_SPE_DEC_D (ID, CHIAVE, TIPO, SEQUENZA,
                                        ESERCIZIO, CDR, LINEA, PG_MODULO, DIP, CD_LIVELLO1, CD_LIVELLO2,
                                        RES_INIZ_REALI_AP, PREV_ASS_COMP_AP)
Values
(1, 'SUB', 'C', I,
 INES-1, COMB_CONTABILI_AP.CDR, COMB_CONTABILI_AP.CD_LINEA_ATTIVITA, COMB_CONTABILI_AP.PG_PROGETTO,
 Get_Dip_From_Modulo(INES-1, COMB_CONTABILI_AP.PG_PROGETTO, 'X'),
 COMB_CONTABILI_AP.CD_LIVELLO1, COMB_CONTABILI_AP.CD_LIVELLO2,
 Nvl(RES_ASS_AP, 0) /* - Nvl(VAR_PIU_RES_AP, 0)*/ + Nvl(VAR_MENO_RES_AP, 0), -- RES_INIZ_REALI_AP
 Nvl(INI_AP, 0) + Nvl(VAR_PIU_AP , 0) - Nvl(VAR_MENO_AP, 0)); -- PREV_ASS_COMP_AP

End Loop; -- FINE LOOP SALDI ESERCIZIO PRECEDENTE

-- inserimento NELLA VIEW (FISSO)


Insert Into PRT_VPG_PREV_FIN_ENT_SPE_DEC (ID, CHIAVE, TIPO, SEQUENZA,
                                      ESERCIZIO, ST_DIP_YN, SCIENT_YN,
                                      PESO_DIP, NUM_DIP, DIP, DS_DIPARTIMENTO,
                                      CD_LIVELLO1, CD_LIVELLO1_ROM, DS_LIVELLO1,
                                      CD_LIVELLO2, DS_LIVELLO2,
                                      RES_PRES_AC, PREV_COMPETENZA_AC, /*PREV_FULL_AC,*/ PREV_CASSA_AC,
                                      RES_INIZ_REALI_AP, PREV_ASS_COMP_AP, /*PREV_ASS_FULL_AP,*/ PREV_ASS_CASSA_AP)
Select aId, 'chiave', 't', Rownum,
    PRT_VPG_PREV_FIN_ENT_SPE_DEC_D.ESERCIZIO,
-- ST_DIP_YN
decode(PRT_VPG_PREV_FIN_ENT_SPE_DEC_D.dip,'SAC',Decode (CD_LIVELLO1, '5', 'N', '6', 'N', 'Y'),'Y') ST_DIP_YN,
-- SCIENT_YN
 decode(PRT_VPG_PREV_FIN_ENT_SPE_DEC_D.dip,'SAC','N','Y') SCIENT_YN,
decode(PRT_VPG_PREV_FIN_ENT_SPE_DEC_D.dip,'SAC',Decode (CD_LIVELLO1, '5', '14', '6', '14', lpad(to_char(peso),2,'0')),lpad(to_char(peso),2,'0')) PESO_DIP,
-- NUM_DIP
lpad(to_char(peso),2,'0') NUM_DIP,
-- DIP
decode(PRT_VPG_PREV_FIN_ENT_SPE_DEC_D.dip,'SAC',Decode (CD_LIVELLO1, '4', 'SAC_PGIRO', '5', 'SAC_PGIRO', 'SAC'),PRT_VPG_PREV_FIN_ENT_SPE_DEC_D.dip) DIP,
p.DS_DIPARTIMENTO,
PRT_VPG_PREV_FIN_ENT_SPE_DEC_D.CD_LIVELLO1,
trim(TO_CHAR(CD_LIVELLO1, 'RM')),
--cnrctb020.
cnrctb020.Getdeslivello(PRT_VPG_PREV_FIN_ENT_SPE_DEC_D.ESERCIZIO , 'E', PRT_VPG_PREV_FIN_ENT_SPE_DEC_D.CD_LIVELLO1, NULL, NULL, NULL, NULL, NULL, NULL),
PRT_VPG_PREV_FIN_ENT_SPE_DEC_D.CD_LIVELLO2,
--cnrctb020.
cnrctb020.Getdeslivello(PRT_VPG_PREV_FIN_ENT_SPE_DEC_D.ESERCIZIO , 'E', PRT_VPG_PREV_FIN_ENT_SPE_DEC_D.CD_LIVELLO1, PRT_VPG_PREV_FIN_ENT_SPE_DEC_D.CD_LIVELLO2, NULL, NULL, NULL, NULL, NULL),
Nvl(Sum(RES_PRES_AC),0),
Nvl(Sum(PREV_COMPETENZA_AC),0),
Nvl(Sum(PREV_CASSA_AC),0),
Nvl(Sum(RES_INIZ_REALI_AP),0),
Nvl(Sum(PREV_ASS_COMP_AP),0),
Nvl(Sum(PREV_ASS_CASSA_AP),0)
From  PRT_VPG_PREV_FIN_ENT_SPE_DEC_D,
    dipartimento_peso p
Where
	PRT_VPG_PREV_FIN_ENT_SPE_DEC_D.esercizio= p.esercizio AND
  PRT_VPG_PREV_FIN_ENT_SPE_DEC_D.dip= p.cd_dipartimento
and CHIAVE = 'SUB'
Group By aId, 'chiave', 't', ROWNUM,
 			PRT_VPG_PREV_FIN_ENT_SPE_DEC_D.ESERCIZIO,
       -- ST_DIP_YN
       decode(PRT_VPG_PREV_FIN_ENT_SPE_DEC_D.dip,'SAC',Decode (CD_LIVELLO1, '5', 'N', '6', 'N', 'Y'),'Y'),
			-- SCIENT_YN
 			decode(PRT_VPG_PREV_FIN_ENT_SPE_DEC_D.dip,'SAC','N','Y') ,
 			-- PESO_DIP
			decode(PRT_VPG_PREV_FIN_ENT_SPE_DEC_D.dip,'SAC',Decode (CD_LIVELLO1, '5', '14', '6', '14',lpad(to_char(peso),2,'0')),lpad(to_char(peso),2,'0')),
			-- NUM_DIP
			lpad(to_char(peso),2,'0'),
			-- DIP
			decode(PRT_VPG_PREV_FIN_ENT_SPE_DEC_D.dip,'SAC',Decode (CD_LIVELLO1, '4', 'SAC_PGIRO', '5', 'SAC_PGIRO', 'SAC'),PRT_VPG_PREV_FIN_ENT_SPE_DEC_D.dip),
			p.DS_DIPARTIMENTO,
CD_LIVELLO1,
trim(TO_CHAR(CD_LIVELLO1, 'RM')),
cnrctb020.Getdeslivello(PRT_VPG_PREV_FIN_ENT_SPE_DEC_D.ESERCIZIO , 'E', PRT_VPG_PREV_FIN_ENT_SPE_DEC_D.CD_LIVELLO1, NULL, NULL, NULL, NULL, NULL, NULL),
CD_LIVELLO2,
cnrctb020.Getdeslivello(PRT_VPG_PREV_FIN_ENT_SPE_DEC_D.ESERCIZIO , 'E', PRT_VPG_PREV_FIN_ENT_SPE_DEC_D.CD_LIVELLO1, PRT_VPG_PREV_FIN_ENT_SPE_DEC_D.CD_LIVELLO2, NULL, NULL, NULL, NULL, NULL);
Elsif INGEST = 'S' THEN

------------------------------------------------------------------------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------------------------------------------------------
--                                                                        SPESA
------------------------------------------------------------------------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------------------------------------------------------


-- RECUPERO PRIMA LA NUOVA PREVISIONE DI SPESA DI COMPETENZA

For NEW_PREV_COMP In  (Select  PDG_MODULO_SPESE.PG_PROGETTO,
                               CLASS.CD_LIVELLO1,
                               CLASS.CD_LIVELLO2,
                               Sum(IM_SPESE_GEST_DECENTRATA_INT)+
                               Sum(IM_SPESE_GEST_DECENTRATA_EST)+
                               Sum(IM_SPESE_GEST_ACCENTRATA_INT)+
                               Sum(IM_SPESE_GEST_ACCENTRATA_EST) TOT_PREV_COMP
                       From    PDG_MODULO_SPESE,
                               V_CLASSIFICAZIONE_VOCI CLASS
                       Where   PDG_MODULO_SPESE.ESERCIZIO = INES         And
                               --cd_centro_responsabilita = '999.000.000' And
                               CLASS.ID_CLASSIFICAZIONE     = PDG_MODULO_SPESE.ID_CLASSIFICAZIONE
                       Group By PDG_MODULO_SPESE.PG_PROGETTO, CLASS.CD_LIVELLO1, CLASS.CD_LIVELLO2) Loop

I := I + 1;

-- E LA INSERISCO NELLA VIEW DI DETTAGLIO

--Dbms_Output.put_line ('inserisco '||NEW_PREV_COMP.CD_LIVELLO1||' '||NEW_PREV_COMP.CD_LIVELLO2);

Insert Into PRT_VPG_PREV_FIN_ENT_SPE_DEC_D (ID, CHIAVE, TIPO, SEQUENZA,
                                            ESERCIZIO, CDR, LINEA, PG_MODULO, DIP, CD_LIVELLO1, CD_LIVELLO2,
                                            PREV_COMPETENZA_AC)
Values (1, 'SUB', 'C', I,
        INES,
        Null, Null, -- cdr e linea vuoti
        NEW_PREV_COMP.PG_PROGETTO, Get_Dip_From_Modulo(INES, NEW_PREV_COMP.PG_PROGETTO, 'X'),
        NEW_PREV_COMP.CD_LIVELLO1, NEW_PREV_COMP.CD_LIVELLO2, NEW_PREV_COMP.TOT_PREV_COMP);

End Loop;



-- POI RECUPERO I DATI STORICIZZATI PER L'ANNO IN PREVISIONE: I RESIDUI PRESUNTI ALLA FINE DELL'ESERCIZIO IN CORSO e
--                                                            PREVISIONI DI CASSA (NON LE PRENDO DAL PDG)

-- ATTENZIONE !!! AGGIUNGERE IL FULL

For CUR_DATI_STORICI_AC In (Select CD_DIPARTIMENTO, CD_LIVELLO1, CD_LIVELLO2, RES_PRES_FINE_ES_PREC, PREV_CASSA
                            From   PARAMETRI_REND_PREV_DEC
                            Where  ESERCIZIO = INES And
                                   TI_GESTIONE = 'S') Loop

I := I + 1;

-- E LI INSERISCO NELLA VIEW DI DETTAGLIO

Insert Into PRT_VPG_PREV_FIN_ENT_SPE_DEC_D (ID, CHIAVE, TIPO, SEQUENZA,
                                        ESERCIZIO, CDR, LINEA, PG_MODULO, DIP, CD_LIVELLO1, CD_LIVELLO2,
                                        RES_PRES_AC, PREV_CASSA_AC)
Values (1, 'SUB', 'C', I,
        INES, Null, Null, Null, CUR_DATI_STORICI_AC.CD_DIPARTIMENTO,
        CUR_DATI_STORICI_AC.CD_LIVELLO1, CUR_DATI_STORICI_AC.CD_LIVELLO2, CUR_DATI_STORICI_AC.RES_PRES_FINE_ES_PREC,
        CUR_DATI_STORICI_AC.PREV_CASSA);
End Loop;


-- POI RECUPERO I DATI STORICIZZATI PER L'ANNO PRECEDENTE: LE PREVISIONI ASSESTATE DI CASSA (NON DAI SALDI)

-- ATTENZIONE !!! AGGIUNGERE IL FULL


For CUR_DATI_STORICI_AP In (Select CD_DIPARTIMENTO, CD_LIVELLO1, CD_LIVELLO2, PREV_ASS_CASSA
                            From   PARAMETRI_REND_PREV_DEC
                            Where  ESERCIZIO = INES-1 And
                                   TI_GESTIONE = 'S') Loop

I := I + 1;

-- E LI INSERISCO NELLA VIEW DI DETTAGLIO

Insert Into PRT_VPG_PREV_FIN_ENT_SPE_DEC_D (ID, CHIAVE, TIPO, SEQUENZA,
                                        ESERCIZIO, CDR, LINEA, PG_MODULO, DIP, CD_LIVELLO1, CD_LIVELLO2,
                                        PREV_ASS_CASSA_AP)
Values (1, 'SUB', 'C', I,
        INES, Null, Null, Null, CUR_DATI_STORICI_AP.CD_DIPARTIMENTO,
        CUR_DATI_STORICI_AP.CD_LIVELLO1, CUR_DATI_STORICI_AP.CD_LIVELLO2, CUR_DATI_STORICI_AP.PREV_ASS_CASSA);
End Loop;


-- LOOP SUI SALDI DELL'ESERCIZIO PRECEDENTE A QUELLO DI PREVISIONE PER PRENDERE:

-- I RESIDUI INIZIALI
-- LE PREVISIONI DI COMPETENZA ASSESTATE

-- ATTENZIONE !!! CAMBIARE FORMULA PER LA SPESA (PRENDERLA DAL GESTIONALE)

For COMB_CONTABILI_AP In
                      (Select  Distinct
                               SALDI.CD_CENTRO_RESPONSABILITA CDR,
                               SALDI.CD_LINEA_ATTIVITA,
                               SALDI.TI_GESTIONE,
                               SALDI.TI_APPARTENENZA,
                               SALDI.CD_ELEMENTO_VOCE,
                               LINEA_ATTIVITA.PG_PROGETTO,
                               CLASS.CD_LIVELLO1,
                               CLASS.CD_LIVELLO2
                       From    VOCE_F_SALDI_CDR_LINEA SALDI,
                       	       ELEMENTO_VOCE,
                               V_CLASSIFICAZIONE_VOCI CLASS,
                       	       LINEA_ATTIVITA
                       Where   SALDI.ESERCIZIO                = INES-1                          And
                               SALDI.TI_GESTIONE              = 'S'                             And
/* TOGLIERE */                 --SALDI.CD_CENTRO_RESPONSABILITA = '999.000.000'                   And
                               ELEMENTO_VOCE.ESERCIZIO        = SALDI.ESERCIZIO                 And
                               ELEMENTO_VOCE.TI_APPARTENENZA  = SALDI.TI_APPARTENENZA           And
                               ELEMENTO_VOCE.TI_GESTIONE      = SALDI.TI_GESTIONE               And
                               ELEMENTO_VOCE.CD_ELEMENTO_VOCE = SALDI.CD_ELEMENTO_VOCE          And
                               ELEMENTO_VOCE.ID_CLASSIFICAZIONE = CLASS.ID_CLASSIFICAZIONE      And
                               CLASS.ESERCIZIO                = ELEMENTO_VOCE.ESERCIZIO         And
                               CLASS.TI_GESTIONE              = ELEMENTO_VOCE.TI_GESTIONE       And
                               SALDI.CD_CENTRO_RESPONSABILITA = LINEA_ATTIVITA.CD_CENTRO_RESPONSABILITA And
                               SALDI.CD_LINEA_ATTIVITA	      = LINEA_ATTIVITA.CD_LINEA_ATTIVITA        And
                               LINEA_ATTIVITA.PG_PROGETTO     IS NOT NULL) Loop


Select Nvl(FL_VOCE_FONDO, 'N')
Into   VOCE_FONDO
From   ELEMENTO_VOCE
Where  ESERCIZIO        = INES-1                            And
       TI_APPARTENENZA  = COMB_CONTABILI_AP.TI_APPARTENENZA And
       TI_GESTIONE      = COMB_CONTABILI_AP.TI_GESTIONE     And
       CD_ELEMENTO_VOCE = COMB_CONTABILI_AP.CD_ELEMENTO_VOCE;

If VOCE_FONDO = 'N' Then
   INI_AP      := cnrutl002.RF_IM_STANZ_INIZIALE_A1 (INES-1, INES-1, COMB_CONTABILI_AP.CDR, COMB_CONTABILI_AP.CD_LINEA_ATTIVITA, COMB_CONTABILI_AP.TI_APPARTENENZA, COMB_CONTABILI_AP.TI_GESTIONE, NULL, COMB_CONTABILI_AP.CD_ELEMENTO_VOCE);
Elsif VOCE_FONDO = 'Y' Then
  begin
   Select IM_STANZ_INIZIALE_A1
   Into   INI_AP
   From   VOCE_F_SALDI_CDR_LINEA
   Where  ESERCIZIO                  = INES-1                              AND
          ESERCIZIO_RES              = INES-1                              And
          CD_CENTRO_RESPONSABILITA   = COMB_CONTABILI_AP.CDR               AND
          CD_LINEA_ATTIVITA          = COMB_CONTABILI_AP.CD_LINEA_ATTIVITA AND
          TI_APPARTENENZA            = COMB_CONTABILI_AP.TI_APPARTENENZA   AND
          TI_GESTIONE                = COMB_CONTABILI_AP.TI_GESTIONE       AND
          CD_ELEMENTO_VOCE           = COMB_CONTABILI_AP.CD_ELEMENTO_VOCE;
    exception when no_Data_found then
    	INI_AP:=0;
    end;
End If;

VAR_PIU_AP  := cnrutl002.RF_variazioni_piu (INES-1, INES-1, COMB_CONTABILI_AP.CDR, COMB_CONTABILI_AP.CD_LINEA_ATTIVITA, COMB_CONTABILI_AP.TI_APPARTENENZA, COMB_CONTABILI_AP.TI_GESTIONE, NULL, COMB_CONTABILI_AP.CD_ELEMENTO_VOCE);
VAR_MENO_AP := Abs(cnrutl002.RF_variazioni_meno (INES-1, INES-1, COMB_CONTABILI_AP.CDR, COMB_CONTABILI_AP.CD_LINEA_ATTIVITA, COMB_CONTABILI_AP.TI_APPARTENENZA, COMB_CONTABILI_AP.TI_GESTIONE, NULL, COMB_CONTABILI_AP.CD_ELEMENTO_VOCE));

-- RESIDUI PROPRI INIZIALI
RES_ASS_AP      := cnrutl002.RF_IM_OBBL_RES_PRO (INES-1, NULL, COMB_CONTABILI_AP.CDR, COMB_CONTABILI_AP.CD_LINEA_ATTIVITA, COMB_CONTABILI_AP.TI_APPARTENENZA, COMB_CONTABILI_AP.TI_GESTIONE, NULL, COMB_CONTABILI_AP.CD_ELEMENTO_VOCE);
-- VARIAZIONI PIU' AI RESIDUI PROPRI
--VAR_PIU_RES_AP  := cnrutl002.VAR_PIU_OBBL_RES_PRO (INES-1, NULL, COMB_CONTABILI_AP.CDR, COMB_CONTABILI_AP.CD_LINEA_ATTIVITA, COMB_CONTABILI_AP.TI_APPARTENENZA, COMB_CONTABILI_AP.TI_GESTIONE, NULL, COMB_CONTABILI_AP.CD_ELEMENTO_VOCE);
-- VARIAZIONI MENO AI RESIDUI PROPRI
--VAR_MENO_RES_AP := cnrutl002.VAR_MENO_OBBL_RES_PRO (INES-1, NULL, COMB_CONTABILI_AP.CDR, COMB_CONTABILI_AP.CD_LINEA_ATTIVITA, COMB_CONTABILI_AP.TI_APPARTENENZA, COMB_CONTABILI_AP.TI_GESTIONE, NULL, COMB_CONTABILI_AP.CD_ELEMENTO_VOCE);
-- STANZIAMENTO RESIDUO INIZIALE
STANZ_RES_INI   := Cnrutl002.IM_STANZ_RES_IMPROPRIO (INES-1, NULL, COMB_CONTABILI_AP.CDR, COMB_CONTABILI_AP.CD_LINEA_ATTIVITA, COMB_CONTABILI_AP.TI_APPARTENENZA, COMB_CONTABILI_AP.TI_GESTIONE, NULL, COMB_CONTABILI_AP.CD_ELEMENTO_VOCE);

I := I + 1;
/*
Select Max(esercizio)
Into   ultimo_anno
From   progetto
Where  pg_progetto = COMB_CONTABILI_AP.pg_progetto;*/

-- E LI INSERISCO NELLA VIEW DI DETTAGLIO
--dbms_output.put_line('I '||i||' dip '||Get_Dip_From_Modulo(INES-1, COMB_CONTABILI_AP.PG_PROGETTO, 'X')||' cdr '||COMB_CONTABILI_AP.CDR||' gae '||COMB_CONTABILI_AP.CD_LINEA_ATTIVITA);
Insert Into PRT_VPG_PREV_FIN_ENT_SPE_DEC_D (ID, CHIAVE, TIPO, SEQUENZA,
                                            ESERCIZIO, CDR, LINEA, PG_MODULO, DIP, CD_LIVELLO1, CD_LIVELLO2,
                                            RES_INIZ_REALI_AP, PREV_ASS_COMP_AP)
Values (1, 'SUB', 'C', I,
        INES-1, COMB_CONTABILI_AP.CDR, COMB_CONTABILI_AP.CD_LINEA_ATTIVITA, COMB_CONTABILI_AP.PG_PROGETTO,
        Get_Dip_From_Modulo(INES-1, COMB_CONTABILI_AP.PG_PROGETTO, 'X'),
        COMB_CONTABILI_AP.CD_LIVELLO1, COMB_CONTABILI_AP.CD_LIVELLO2,
        Nvl(RES_ASS_AP, 0) + Nvl(STANZ_RES_INI, 0), -- RES_INIZ_REALI_AP
        Nvl(INI_AP, 0) + Nvl(VAR_PIU_AP , 0) - Nvl(VAR_MENO_AP, 0)); -- PREV_ASS_COMP_AP

End Loop; -- FINE LOOP SALDI ESERCIZIO PRECEDENTE

-- inserimento NELLA VIEW (FISSO)


Insert Into PRT_VPG_PREV_FIN_ENT_SPE_DEC (ID, CHIAVE, TIPO, SEQUENZA,
                                      ESERCIZIO, ST_DIP_YN, SCIENT_YN,
                                      PESO_DIP, NUM_DIP, DIP, DS_DIPARTIMENTO,
                                      CD_LIVELLO1, CD_LIVELLO1_ROM, DS_LIVELLO1,
                                      CD_LIVELLO2, DS_LIVELLO2,
                                      RES_PRES_AC, PREV_COMPETENZA_AC, PREV_FULL_AC, PREV_CASSA_AC,
                                      RES_INIZ_REALI_AP, PREV_ASS_COMP_AP, PREV_ASS_FULL_AP, PREV_ASS_CASSA_AP)
Select aId, 'chiave', 't', Rownum,
       PRT_VPG_PREV_FIN_ENT_SPE_DEC_D.ESERCIZIO,
-- ST_DIP_YN
decode(PRT_VPG_PREV_FIN_ENT_SPE_DEC_D.dip,'SAC',Decode (CD_LIVELLO1, '5', 'N', '6', 'N', 'Y'),'Y') ST_DIP_YN,
-- SCIENT_YN
 decode(PRT_VPG_PREV_FIN_ENT_SPE_DEC_D.dip,'SAC','N','Y') SCIENT_YN,
decode(PRT_VPG_PREV_FIN_ENT_SPE_DEC_D.dip,'SAC',Decode (CD_LIVELLO1, '5', '14', '6', '14', lpad(to_char(peso),2,'0')),lpad(to_char(peso),2,'0')) PESO_DIP,
-- NUM_DIP
 lpad(to_char(peso),2,'0') NUM_DIP,
-- DIP
decode(PRT_VPG_PREV_FIN_ENT_SPE_DEC_D.dip,'SAC',Decode (CD_LIVELLO1, '4', 'SAC_PGIRO', '5', 'SAC_PGIRO', 'SAC'),PRT_VPG_PREV_FIN_ENT_SPE_DEC_D.dip) DIP,
p.DS_DIPARTIMENTO,
Nvl(PRT_VPG_PREV_FIN_ENT_SPE_DEC_D.CD_LIVELLO1, 'Q'),
Nvl(trim(TO_CHAR(PRT_VPG_PREV_FIN_ENT_SPE_DEC_D.CD_LIVELLO1, 'RM')), 'W'),
cnrctb020.Getdeslivello(PRT_VPG_PREV_FIN_ENT_SPE_DEC_D.ESERCIZIO , 'S', PRT_VPG_PREV_FIN_ENT_SPE_DEC_D.CD_LIVELLO1, NULL, NULL, NULL, NULL, NULL, NULL),
Nvl(PRT_VPG_PREV_FIN_ENT_SPE_DEC_D.CD_LIVELLO2, 'Q'),
cnrctb020.Getdeslivello(PRT_VPG_PREV_FIN_ENT_SPE_DEC_D.ESERCIZIO , 'S', PRT_VPG_PREV_FIN_ENT_SPE_DEC_D.CD_LIVELLO1, PRT_VPG_PREV_FIN_ENT_SPE_DEC_D.CD_LIVELLO2, NULL, NULL, NULL, NULL, NULL),
Nvl(Sum(RES_PRES_AC),0),
Nvl(Sum(PREV_COMPETENZA_AC),0),
0, -- full ac
Nvl(Sum(PREV_CASSA_AC),0),
Nvl(Sum(RES_INIZ_REALI_AP),0),
Nvl(Sum(PREV_ASS_COMP_AP),0),
0, -- full ap
Nvl(Sum(PREV_ASS_CASSA_AP),0)
From  PRT_VPG_PREV_FIN_ENT_SPE_DEC_D,
    dipartimento_peso p
Where
 PRT_VPG_PREV_FIN_ENT_SPE_DEC_D.ESERCIZIO= p.esercizio AND
 PRT_VPG_PREV_FIN_ENT_SPE_DEC_D.dip= p.cd_dipartimento
and CHIAVE = 'SUB'
Group By aId, 'chiave', 't', ROWNUM,
       PRT_VPG_PREV_FIN_ENT_SPE_DEC_D.ESERCIZIO,
       -- ST_DIP_YN
       decode(PRT_VPG_PREV_FIN_ENT_SPE_DEC_D.dip,'SAC',Decode (CD_LIVELLO1, '5', 'N', '6', 'N', 'Y'),'Y'),
			-- SCIENT_YN
 			decode(PRT_VPG_PREV_FIN_ENT_SPE_DEC_D.dip,'SAC','N','Y') ,
 			-- PESO_DIP
			decode(PRT_VPG_PREV_FIN_ENT_SPE_DEC_D.dip,'SAC',Decode (CD_LIVELLO1, '5', '14', '6', '14',  lpad(to_char(peso),2,'0')), lpad(to_char(peso),2,'0')),
			-- NUM_DIP
			 lpad(to_char(peso),2,'0'),
			-- DIP
			decode(PRT_VPG_PREV_FIN_ENT_SPE_DEC_D.dip,'SAC',Decode (CD_LIVELLO1, '4', 'SAC_PGIRO', '5', 'SAC_PGIRO', 'SAC'),PRT_VPG_PREV_FIN_ENT_SPE_DEC_D.dip),
			p.DS_DIPARTIMENTO,
Nvl(PRT_VPG_PREV_FIN_ENT_SPE_DEC_D.CD_LIVELLO1, 'Q'),
Nvl(trim(TO_CHAR(PRT_VPG_PREV_FIN_ENT_SPE_DEC_D.CD_LIVELLO1, 'RM')), 'W'),
cnrctb020.Getdeslivello(PRT_VPG_PREV_FIN_ENT_SPE_DEC_D.ESERCIZIO , 'S', PRT_VPG_PREV_FIN_ENT_SPE_DEC_D.CD_LIVELLO1, NULL, NULL, NULL, NULL, NULL, NULL),
Nvl(PRT_VPG_PREV_FIN_ENT_SPE_DEC_D.CD_LIVELLO2, 'Q'),
cnrctb020.Getdeslivello(PRT_VPG_PREV_FIN_ENT_SPE_DEC_D.ESERCIZIO , 'S', PRT_VPG_PREV_FIN_ENT_SPE_DEC_D.CD_LIVELLO1, PRT_VPG_PREV_FIN_ENT_SPE_DEC_D.CD_LIVELLO2, NULL, NULL, NULL, NULL, NULL);
End If;

End;
/


