CREATE OR REPLACE Procedure POPOLA_ART_22_BIS (IN_DA_DATA DATE,
                                               IN_A_DATA  DATE,
                                               perc_int   NUMBER) Is

VARPIU          NUMBER;
VARMENO         NUMBER;
IMP             NUMBER;
Aassestato       NUMBER;
Aperc_ass  NUMBER;
Adisp            NUMBER;
Ariduzione       NUMBER;

numVariazione     Number := 0;
numRiga           Number := 0;
totRiduzioneCDS   Number := 0;
totRiduzioneCDR   Number := 0;
Begin

Delete DATI_ART_22;


For aSaldi in (Select V.ESERCIZIO, V.ESERCIZIO_RES, V.CD_CENTRO_RESPONSABILITA, V.CD_LINEA_ATTIVITA, V.TI_APPARTENENZA, V.TI_GESTIONE,
                      V.CD_VOCE, IM_STANZ_INIZIALE_A1, v.cd_elemento_voce
              From  VOCE_F_SALDI_CDR_LINEA V, LINEA_ATTIVITA L
              Where V.esercizio = 2006 And
                    V.ESERCIZIO_RES = 2006 And
                    V.TI_GESTIONE = 'S' And
                    l.PG_PROGETTO != '2694' And
                    NOT (v.CD_CENTRO_RESPONSABILITA = '000.301.000' AND v.CD_LINEA_ATTIVITA = 'P0000008') And
                    Substr(v.cd_centro_responsabilita, 1, 3) != '035' And
                    V.cd_ELEMENTO_VOCE In ('1.01.061', '1.01.065', '1.01.101', '1.01.103', '1.01.104', '1.01.105', '1.01.107',
                                           '1.01.109', '1.01.110', '1.01.111', '1.01.113', '1.01.118', '1.01.124',
                                           '1.01.126', '1.01.127', '1.01.128', '1.01.129', '1.01.131', '1.01.133',
                                           '1.01.134', '1.01.135', '1.01.136', '1.01.139') And
                    V.CD_CENTRO_RESPONSABILITA = L.CD_CENTRO_RESPONSABILITA And
                    V.cd_linea_attivita = L.CD_LINEA_ATTIVITA And
--                    V.CD_CENTRO_RESPONSABILITA In ('038.000.000', '002.000.000', '313.000.000') And
                    CD_NATURA = '1') Loop

--Dbms_Output.put_line('1');
VARPIU := CNRUTL002.VARIAZIONI_PIU (aSaldi.ESERCIZIO, aSaldi.ESERCIZIO_RES, aSaldi.CD_CENTRO_RESPONSABILITA,
                                    aSaldi.CD_LINEA_ATTIVITA, aSaldi.TI_APPARTENENZA, aSaldi.TI_GESTIONE,
                                    aSaldi.CD_VOCE, aSaldi.CD_ELEMENTO_VOCE, IN_DA_DATA, IN_A_DATA);

--Dbms_Output.put_line('2');
VARMENO := CNRUTL002.VARIAZIONI_MENO (aSaldi.ESERCIZIO, aSaldi.ESERCIZIO_RES, aSaldi.CD_CENTRO_RESPONSABILITA,
                                      aSaldi.CD_LINEA_ATTIVITA, aSaldi.TI_APPARTENENZA, aSaldi.TI_GESTIONE,
                                      aSaldi.CD_VOCE, aSaldi.CD_ELEMENTO_VOCE, IN_DA_DATA, IN_A_DATA);


--Dbms_Output.put_line('3');
IMP := CNRUTL002.IM_OBBL_ACC_COMP     (aSaldi.ESERCIZIO, aSaldi.ESERCIZIO_RES, aSaldi.CD_CENTRO_RESPONSABILITA,
                                       aSaldi.CD_LINEA_ATTIVITA, aSaldi.TI_APPARTENENZA, aSaldi.TI_GESTIONE,
                                       aSaldi.CD_VOCE, aSaldi.CD_ELEMENTO_VOCE, IN_DA_DATA, IN_A_DATA);

Aassestato      := Nvl(aSaldi.IM_STANZ_INIZIALE_A1, 0) + Nvl(varpiu, 0) + Nvl(varmeno, 0);
Aperc_ass := (Nvl(aSaldi.IM_STANZ_INIZIALE_A1, 0) + Nvl(varpiu, 0) + Nvl(varmeno, 0))*(perc_int/100);
Adisp           := Nvl(aSaldi.IM_STANZ_INIZIALE_A1, 0) + Nvl(varpiu, 0) + Nvl(varmeno, 0) - Nvl(imp, 0);


If Aperc_ass <= Adisp Then
  Ariduzione := Aperc_ass;
Else
  If Adisp > 0 Then
     Ariduzione := Adisp;
  Else
     Ariduzione := 0;
  End If;
End If;

If aSaldi.IM_STANZ_INIZIALE_A1 != 0 Or varpiu != 0 Or varmeno != 0 Or imp != 0 Then

Insert Into DATI_ART_22
(DA_DATA, A_DATA, ESERCIZIO, ESERCIZIO_RES, CD_CENTRO_RESPONSABILITA, CD_LINEA_ATTIVITA, TI_APPARTENENZA,
 TI_GESTIONE, CD_VOCE, cd_elemento_voce, IM_STANZ_INIZIALE_A1, VARIAZIONI_PIU, VARIAZIONI_MENO, IM_OBBL_ACC_COMP,
 assestato, perc_ass, disp, riduzione)
Values
(IN_DA_DATA, IN_A_DATA, aSaldi.ESERCIZIO, aSaldi.ESERCIZIO_RES, aSaldi.CD_CENTRO_RESPONSABILITA,
 aSaldi.CD_LINEA_ATTIVITA, aSaldi.TI_APPARTENENZA, aSaldi.TI_GESTIONE, aSaldi.CD_VOCE, aSaldi.CD_ELEMENTO_VOCE,
 aSaldi.IM_STANZ_INIZIALE_A1, varpiu, varmeno, imp, Aassestato, Aperc_ass, Adisp, Nvl(ariduzione, 0));

Commit;

End If;

End Loop;

--Dbms_Output.put_line('E VAI!!!');
--Carico le variazioni con i dati caricati in DATI_ART_22

Select nvl(max(PG_VARIAZIONE_PDG), 0)
into numVariazione
from PDG_VARIAZIONE
where esercizio = 2006;

For recCDS in (select distinct substr(CD_CENTRO_RESPONSABILITA,1,3) cds
               from DATI_ART_22
               where esercizio = 2006
               order by 1) Loop

--Dbms_Output.put_line('entro');
   numRiga := 0;
   numVariazione := numVariazione + 1;
   totRiduzioneCDS := 0;
   totRiduzioneCDR := 0;

--Dbms_Output.put_line('inserisco');
   Insert into PDG_VARIAZIONE (ESERCIZIO, PG_VARIAZIONE_PDG, CD_CENTRO_RESPONSABILITA,
                               DT_APERTURA, DT_CHIUSURA, DT_APPROVAZIONE, DT_ANNULLAMENTO,
                               DS_VARIAZIONE, DS_DELIBERA, STATO, RIFERIMENTI,
                               CD_CAUSALE_RESPINTA, DS_CAUSALE_RESPINTA,
                               DACR, UTCR, DUVA, UTUV, PG_VER_REC,
                               DT_APP_FORMALE, TIPOLOGIA, TIPOLOGIA_FIN,
                               TI_APPARTENENZA, TI_GESTIONE, CD_ELEMENTO_VOCE)
                        values(2006, numVariazione, '000.100.000',
                               trunc(sysdate), null, null, null,
                               Substr('Riduzione delle spese di funzionamento (fonti interne) per consumi intermedi in c/competenza di cui '||
                                      'all''art.22 D.L. 04/07/2006, n. 223, pubblicato sulla G.U. n. 153 del 04/07/2006',1,300),
                               Substr('Riduzione delle spese di funzionamento (fonti interne) per consumi intermedi in c/competenza di cui '||
                                      'all''art.22 D.L. 04/07/2006, n. 223, pubblicato sulla G.U. n. 153 del 04/07/2006',1,200),
                               'PRP', 'Centro di Spesa '||recCDS.cds,
                               null, null,
                               trunc(sysdate), 'S.I.', trunc(sysdate), 'S.I.', 1,
                               null, 'STO_S_TOT', 'FIN',
                               null, null, null);

--Dbms_Output.put_line('inserito');
   For recCDR in (select distinct CD_CENTRO_RESPONSABILITA from DATI_ART_22
                  where esercizio = 2006
                  and   substr(CD_CENTRO_RESPONSABILITA,1,3) = recCDS.cds) Loop

      totRiduzioneCDR := 0;

--Dbms_Output.put_line('rientro2');
      For recDET in (select * from DATI_ART_22
                     where esercizio = 2006
                     and   CD_CENTRO_RESPONSABILITA = recCDR.CD_CENTRO_RESPONSABILITA) Loop

 --Dbms_Output.put_line('rientro3');
         If recDET.RIDUZIONE != 0 Then

             numRiga := numRiga + 1;

             totRiduzioneCDR := totRiduzioneCDR + recDET.RIDUZIONE;
             totRiduzioneCDS := totRiduzioneCDS + recDET.RIDUZIONE;

--Dbms_Output.put_line('inserisco2');
             Insert into PDG_VARIAZIONE_RIGA_GEST(ESERCIZIO, PG_VARIAZIONE_PDG, PG_RIGA,
                                                  CD_CDR_ASSEGNATARIO, CD_LINEA_ATTIVITA, CD_CDS_AREA,
                                                  TI_APPARTENENZA, TI_GESTIONE, CD_ELEMENTO_VOCE,
                                                  DT_REGISTRAZIONE, DESCRIZIONE, CATEGORIA_DETTAGLIO,
                                                  IM_SPESE_GEST_DECENTRATA_INT,
                                                  IM_SPESE_GEST_DECENTRATA_EST,
                                                  IM_SPESE_GEST_ACCENTRATA_INT,
                                                  IM_SPESE_GEST_ACCENTRATA_EST,
                                                  IM_ENTRATA, PG_RIGA_CLGS,
                                                  CD_CDR_ASSEGNATARIO_CLGS, CD_LINEA_ATTIVITA_CLGS,
                                                  DACR, UTCR, DUVA, UTUV, PG_VER_REC)
                                           Values(2006, numVariazione, numRiga,
                                                  recDET.CD_CENTRO_RESPONSABILITA, recDET.CD_LINEA_ATTIVITA, recCDS.cds,
                                                  recDET.TI_APPARTENENZA, recDET.TI_GESTIONE, recDET.CD_ELEMENTO_VOCE,
                                                  trunc(sysdate), null, 'DIR',
                                                  decode(recDET.TI_GESTIONE, 'S', -recDET.RIDUZIONE, 0),
                                                  0,
                                                  0,
                                                  0 ,
                                                  0, null,
                                                  null, null,
                                                  trunc(sysdate), 'S.I.', trunc(sysdate), 'S.I.', 1);
          End If;
      End Loop;

      If totRiduzioneCDR != 0 Then
--Dbms_Output.put_line('inserisco3');
         Insert into ASS_PDG_VARIAZIONE_CDR(ESERCIZIO, PG_VARIAZIONE_PDG, CD_CENTRO_RESPONSABILITA,
                                            IM_ENTRATA, IM_SPESA,
                                            DACR, UTCR, DUVA, UTUV, PG_VER_REC)
                                     Values(2006, numVariazione, recCDR.CD_CENTRO_RESPONSABILITA,
                                            0, -totRiduzioneCDR,
                                            trunc(sysdate), 'S.I.', trunc(sysdate), 'S.I.', 1);
      End If;
   End Loop;

   If totRiduzioneCDS != 0 Then
--Dbms_Output.put_line('inserisco4');
      Insert into ASS_PDG_VARIAZIONE_CDR(ESERCIZIO, PG_VARIAZIONE_PDG, CD_CENTRO_RESPONSABILITA,
                                         IM_ENTRATA, IM_SPESA,
                                         DACR, UTCR, DUVA, UTUV, PG_VER_REC)
                                  Values(2006, numVariazione, '000.101.000',
                                         0, totRiduzioneCDS,
                                         trunc(sysdate), 'S.I.', trunc(sysdate), 'S.I.', 1);

      numRiga := numRiga + 1;
--Dbms_Output.put_line('inserisco5');
     Insert into PDG_VARIAZIONE_RIGA_GEST(ESERCIZIO, PG_VARIAZIONE_PDG, PG_RIGA,
                                           CD_CDR_ASSEGNATARIO, CD_LINEA_ATTIVITA, CD_CDS_AREA,
                                           TI_APPARTENENZA, TI_GESTIONE, CD_ELEMENTO_VOCE,
                                           DT_REGISTRAZIONE, DESCRIZIONE, CATEGORIA_DETTAGLIO,
                                           IM_SPESE_GEST_DECENTRATA_INT,
                                           IM_SPESE_GEST_DECENTRATA_EST,
                                           IM_SPESE_GEST_ACCENTRATA_INT,
                                           IM_SPESE_GEST_ACCENTRATA_EST,
                                           IM_ENTRATA, PG_RIGA_CLGS,
                                           CD_CDR_ASSEGNATARIO_CLGS, CD_LINEA_ATTIVITA_CLGS,
                                           DACR, UTCR, DUVA, UTUV, PG_VER_REC)
                                    Values(2006, numVariazione, numRiga,
                                           '000.101.000', 'P0000015', '000',
                                           'D', 'S', '1.01.124',
                                           trunc(sysdate), null, 'DIR',
                                           totRiduzioneCDS,
                                           0,
                                           0,
                                           0,
                                           0, null,
                                           null, null,
                                           trunc(sysdate), 'S.I.', trunc(sysdate), 'S.I.', 1);
   End If;
End Loop;
End;
/


