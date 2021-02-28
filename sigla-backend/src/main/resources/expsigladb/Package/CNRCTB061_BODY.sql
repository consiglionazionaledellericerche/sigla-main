--------------------------------------------------------
--  DDL for Package Body CNRCTB061
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "CNRCTB061" is

 Procedure ins_ASS_CDP_PDGP (aDest ASS_CDP_PDGP%rowtype) is
 Begin
   Insert into ASS_CDP_PDGP (
     ESERCIZIO
    ,TI_COSTI_SPESE
    ,ID_MATRICOLA
    ,CD_CENTRO_RESPONSABILITA
    ,PG_PROGETTO_COSTI
    ,PG_RIGA
    ,PG_PROGETTO_SPESE
    ,ID_CLASSIFICAZIONE
    ,CD_CDS_AREA
    ,DACR
    ,UTCR
    ,DUVA
    ,UTUV
    ,PG_VER_REC
    ,cd_cofog
    ,cd_missione
   ) Values (
     aDest.ESERCIZIO
    ,aDest.TI_COSTI_SPESE
    ,aDest.ID_MATRICOLA
    ,aDest.CD_CENTRO_RESPONSABILITA
    ,aDest.PG_PROGETTO_COSTI
    ,aDest.PG_RIGA
    ,aDest.PG_PROGETTO_SPESE
    ,aDest.ID_CLASSIFICAZIONE
    ,aDest.CD_CDS_AREA
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.DUVA
    ,aDest.UTUV
    ,aDest.PG_VER_REC
    ,aDest.cd_cofog
    ,aDest.cd_missione
    );
 End;

 Procedure ins_ASS_CDP_PDGP_ROUND (aDest ASS_CDP_PDGP_ROUND%rowtype) Is
 Begin
   Insert into ASS_CDP_PDGP_ROUND (
     ESERCIZIO
    ,CD_CDR_ROOT
    ,TI_APPARTENENZA
    ,TI_GESTIONE
    ,CD_ELEMENTO_VOCE
    ,CD_CENTRO_RESPONSABILITA
    ,CD_LINEA_ATTIVITA
    ,ID_MATRICOLA
    ,IM_ARR_A1
    ,IM_ARR_A2
    ,IM_ARR_A3
    ,IM_ARR_A1_NON_DISTR
    ,IM_ARR_A2_NON_DISTR
    ,IM_ARR_A3_NON_DISTR
    ,PG_PROGETTO_COSTI
    ,PG_PROGETTO_SPESE
    ,ID_CLASSIFICAZIONE
    ,CD_CDS_AREA
    ,DACR
    ,UTUV
    ,UTCR
    ,DUVA
    ,PG_VER_REC
    ,cd_cofog
    ,cd_missione
   ) Values (
     aDest.ESERCIZIO
    ,aDest.CD_CDR_ROOT
    ,aDest.TI_APPARTENENZA
    ,aDest.TI_GESTIONE
    ,aDest.CD_ELEMENTO_VOCE
    ,aDest.CD_CENTRO_RESPONSABILITA
    ,aDest.CD_LINEA_ATTIVITA
    ,aDest.ID_MATRICOLA
    ,aDest.IM_ARR_A1
    ,aDest.IM_ARR_A2
    ,aDest.IM_ARR_A3
    ,aDest.IM_ARR_A1_NON_DISTR
    ,aDest.IM_ARR_A2_NON_DISTR
    ,aDest.IM_ARR_A3_NON_DISTR
    ,aDest.PG_PROGETTO_COSTI
    ,aDest.PG_PROGETTO_SPESE
    ,aDest.ID_CLASSIFICAZIONE
    ,aDest.CD_CDS_AREA
    ,aDest.DACR
    ,aDest.UTUV
    ,aDest.UTCR
    ,aDest.DUVA
    ,aDest.PG_VER_REC
    ,aDest.cd_cofog
    ,aDest.cd_missione
    );
 End;

 -- Imposta gli importi sul dettaglio di PDG da creare
 Procedure setImporto(aASSCDPLA V_CDP_SPACCATO_CDR_LA_VOCE%rowtype,
                      aDestS in out pdg_modulo_spese%rowtype) is
   aCDRPersonale cdr%Rowtype;
   aLATmp linea_attivita%rowtype;
 Begin
   -- Leggo il CDR del personale e lock del PDG

   aCDRPersonale:=CNRCTB020.GETCDRPERSONALE(aASSCDPLA.esercizio);

   -- Leggo la linea di attivita per aggiornare gli importi del PDGP in base alla natura
   Select * into aLATmp from linea_attivita
   Where cd_linea_attivita = aASSCDPLA.cd_linea_attivita
   And   cd_centro_responsabilita = aASSCDPLA.cd_cdr;

   If aDestS.cd_centro_responsabilita=aCDRPersonale.cd_centro_responsabilita Then
      If aLATmp.cd_natura = '2' Or aLATmp.cd_natura = '3' Then
         aDestS.IM_SPESE_GEST_DECENTRATA_EST:=Nvl(aDestS.IM_SPESE_GEST_DECENTRATA_EST, 0) +
                                              Nvl(aASSCDPLA.im_a1, 0);
      Else
         aDestS.IM_SPESE_GEST_DECENTRATA_INT:=Nvl(aDestS.IM_SPESE_GEST_DECENTRATA_INT, 0) +
                                              Nvl(aASSCDPLA.im_a1, 0);
      End If;
   Else
      If aLATmp.cd_natura = '2' Or aLATmp.cd_natura = '3' Then
         aDestS.IM_SPESE_GEST_ACCENTRATA_EST:=Nvl(aDestS.IM_SPESE_GEST_ACCENTRATA_EST, 0) +
                                              Nvl(aASSCDPLA.im_a1, 0);
      Else
         aDestS.IM_SPESE_GEST_ACCENTRATA_INT:=Nvl(aDestS.IM_SPESE_GEST_ACCENTRATA_INT, 0) +
                                              Nvl(aASSCDPLA.im_a1, 0);
      End If;
   End If;
   --Serve per evitare che alcuni importi abbiano valore null
   aDestS.IM_SPESE_GEST_DECENTRATA_EST:=Nvl(aDestS.IM_SPESE_GEST_DECENTRATA_EST, 0);
   aDestS.IM_SPESE_GEST_DECENTRATA_INT:=Nvl(aDestS.IM_SPESE_GEST_DECENTRATA_INT, 0);
   aDestS.IM_SPESE_GEST_ACCENTRATA_EST:=Nvl(aDestS.IM_SPESE_GEST_ACCENTRATA_EST, 0);
   aDestS.IM_SPESE_GEST_ACCENTRATA_INT:=Nvl(aDestS.IM_SPESE_GEST_ACCENTRATA_INT, 0);
 End;

 -- Imposta gli importi sul dettaglio di PDG da creare
 Procedure setImporto(aASSCDPLA V_CDP_SPACCATO_CDR_LA_VOCE%rowtype,
                      aDestS in out pdg_modulo_spese_gest%rowtype) is
   aCDRPersonale cdr%Rowtype;
   aLATmp linea_attivita%rowtype;
 Begin
   -- Leggo il CDR del personale e lock del PDG

   aCDRPersonale:=CNRCTB020.GETCDRPERSONALE(aASSCDPLA.esercizio);

   -- Leggo la linea di attivita per aggiornare gli importi del PDGP in base alla natura
   Select * into aLATmp from linea_attivita
   Where cd_linea_attivita = aASSCDPLA.cd_linea_attivita
   And   cd_centro_responsabilita = aASSCDPLA.cd_cdr;

   If aDestS.cd_centro_responsabilita=aCDRPersonale.cd_centro_responsabilita Then
      If aLATmp.cd_natura = '2' Or aLATmp.cd_natura = '3' Then
         aDestS.IM_SPESE_GEST_DECENTRATA_EST:=Nvl(aDestS.IM_SPESE_GEST_DECENTRATA_EST, 0) +
                                              Nvl(aASSCDPLA.im_a1, 0);
      Else
         aDestS.IM_SPESE_GEST_DECENTRATA_INT:=Nvl(aDestS.IM_SPESE_GEST_DECENTRATA_INT, 0) +
                                              Nvl(aASSCDPLA.im_a1, 0);
      End If;
   Else
      If aLATmp.cd_natura = '2' Or aLATmp.cd_natura = '3' Then
         aDestS.IM_SPESE_GEST_ACCENTRATA_EST:=Nvl(aDestS.IM_SPESE_GEST_ACCENTRATA_EST, 0) +
                                              Nvl(aASSCDPLA.im_a1, 0);
      Else
         aDestS.IM_SPESE_GEST_ACCENTRATA_INT:=Nvl(aDestS.IM_SPESE_GEST_ACCENTRATA_INT, 0) +
                                              Nvl(aASSCDPLA.im_a1, 0);
      End If;
   End If;
   --Serve per evitare che alcuni importi abbiano valore null
   aDestS.IM_SPESE_GEST_DECENTRATA_EST:=Nvl(aDestS.IM_SPESE_GEST_DECENTRATA_EST, 0);
   aDestS.IM_SPESE_GEST_DECENTRATA_INT:=Nvl(aDestS.IM_SPESE_GEST_DECENTRATA_INT, 0);
   aDestS.IM_SPESE_GEST_ACCENTRATA_EST:=Nvl(aDestS.IM_SPESE_GEST_ACCENTRATA_EST, 0);
   aDestS.IM_SPESE_GEST_ACCENTRATA_INT:=Nvl(aDestS.IM_SPESE_GEST_ACCENTRATA_INT, 0);
 End;

 Procedure setImportoTFR(aASSCDPLA V_CDP_SPACCATO_CDR_LA_VOCE%rowtype, aDestC in out pdg_modulo_costi%Rowtype,
                         isTempoDeterminato Boolean) is
 Begin
   If isTempoDeterminato Then
     aDestC.IM_CF_TFR_DET:=Nvl(aDestC.IM_CF_TFR_DET, 0) + Nvl(aASSCDPLA.im_a1, 0);
   Else
     aDestC.IM_CF_TFR:=Nvl(aDestC.IM_CF_TFR, 0) + Nvl(aASSCDPLA.im_a1, 0);
   End If;
   --Serve per evitare che alcuni importi abbiano valore null
   aDestC.IM_CF_TFR:=Nvl(aDestC.IM_CF_TFR,0);
   aDestC.IM_CF_TFR_DET:=Nvl(aDestC.IM_CF_TFR_DET, 0);
 End;

 -- Funzione di accantonamento dei rotti di scarico
 Procedure fill_round_table(aASSCDPLA V_CDP_SPACCATO_CDR_LA_VOCE%rowtype, aTSNow date, aUser varchar2) is
   aAssCdpPdgPRnd ASS_CDP_PDGP_ROUND%rowtype;
   aLATmp linea_attivita%rowtype;
 Begin
   If aASSCDPLA.IM_RND_A1 = 0 and aASSCDPLA.IM_RND_A2 = 0 and aASSCDPLA.IM_RND_A3 = 0 Then
      Return;
   End if;

   aAssCdpPdgPRnd:=Null;
   aAssCdpPdgPRnd.ESERCIZIO:=aASSCDPLA.Esercizio;
   aAssCdpPdgPRnd.CD_CDR_ROOT:=aASSCDPLA.cd_cdr_root;
   aAssCdpPdgPRnd.TI_APPARTENENZA:=aASSCDPLA.ti_appartenenza;
   aAssCdpPdgPRnd.TI_GESTIONE:=aASSCDPLA.TI_GESTIONE;
   aAssCdpPdgPRnd.CD_ELEMENTO_VOCE:=aASSCDPLA.cd_elemento_voce;
   aAssCdpPdgPRnd.CD_CENTRO_RESPONSABILITA:=aASSCDPLA.cd_cdr;
   aAssCdpPdgPRnd.CD_LINEA_ATTIVITA:=aASSCDPLA.cd_linea_attivita;
   aAssCdpPdgPRnd.ID_MATRICOLA:=aASSCDPLA.ti_rapporto;

   select cd_cofog,cd_missione into aAssCdpPdgPRnd.cd_cofog,aAssCdpPdgPRnd.cd_missione
   from v_linea_attivita_valida
   where
       v_linea_attivita_valida.cd_centro_responsabilita = aAssCdpPdgPRnd.CD_CENTRO_RESPONSABILITA and
       v_linea_attivita_valida.cd_linea_attivita = aAssCdpPdgPRnd.cd_linea_attivita
       and v_linea_attivita_valida.esercizio =aAssCdpPdgPRnd.esercizio ;
   Begin
     Select * into aAssCdpPdgPRnd
     From ass_cdp_pdgp_round
     Where ESERCIZIO=aAssCdpPdgPRnd.esercizio
     And   CD_CDR_ROOT=aAssCdpPdgPRnd.cd_cdr_root
     And   TI_APPARTENENZA=aAssCdpPdgPRnd.ti_appartenenza
     And   TI_GESTIONE=aAssCdpPdgPRnd.ti_gestione
     And   CD_ELEMENTO_VOCE=aAssCdpPdgPRnd.cd_elemento_voce
     And   CD_CENTRO_RESPONSABILITA=aAssCdpPdgPRnd.cd_centro_responsabilita
     And   CD_LINEA_ATTIVITA=aAssCdpPdgPRnd.cd_linea_attivita
     And   ID_MATRICOLA=aAssCdpPdgPRnd.id_matricola
     For update nowait;

     Update ass_cdp_pdgp_round
     Set IM_ARR_A1=IM_ARR_A1+aASSCDPLA.im_rnd_a1,
         IM_ARR_A2=IM_ARR_A2+aASSCDPLA.im_rnd_a2,
         IM_ARR_A3=IM_ARR_A3+aASSCDPLA.im_rnd_a3,
         utuv=aUser,
         duva=aTSNow,
         pg_ver_rec = pg_ver_rec + 1
     Where ESERCIZIO=aAssCdpPdgPRnd.esercizio
     And   CD_CDR_ROOT=aAssCdpPdgPRnd.cd_cdr_root
     And   TI_APPARTENENZA=aAssCdpPdgPRnd.ti_appartenenza
     And   TI_GESTIONE=aAssCdpPdgPRnd.ti_gestione
     And   CD_ELEMENTO_VOCE=aAssCdpPdgPRnd.cd_elemento_voce
     And   CD_CENTRO_RESPONSABILITA=aAssCdpPdgPRnd.cd_centro_responsabilita
     And   CD_LINEA_ATTIVITA=aAssCdpPdgPRnd.cd_linea_attivita
     And   ID_MATRICOLA=aAssCdpPdgPRnd.id_matricola;
   Exception
     When NO_DATA_FOUND Then
       aAssCdpPdgPRnd.IM_ARR_A1:=aASSCDPLA.im_rnd_a1;
       aAssCdpPdgPRnd.IM_ARR_A2:=aASSCDPLA.im_rnd_a2;
       aAssCdpPdgPRnd.IM_ARR_A3:=aASSCDPLA.im_rnd_a3;
       aAssCdpPdgPRnd.IM_ARR_A1_NON_DISTR:=0;
       aAssCdpPdgPRnd.IM_ARR_A2_NON_DISTR:=0;
       aAssCdpPdgPRnd.IM_ARR_A3_NON_DISTR:=0;
       aAssCdpPdgPRnd.DACR:=aTSNow;
       aAssCdpPdgPRnd.UTUV:=aUser;
       aAssCdpPdgPRnd.UTCR:=aUser;
       aAssCdpPdgPRnd.DUVA:=aTSNow;
       aAssCdpPdgPRnd.PG_VER_REC:=1;

       ins_ASS_CDP_PDGP_ROUND(aAssCdpPdgPRnd);
   End;
 End;

 Procedure scaricaCDPSuPdgP(aEsercizio number, aCdCdr varchar2, aUser varchar2) is
   aCDRRUO cdr%Rowtype;
   aUO unita_organizzativa%Rowtype;

   aPdgModulo pdg_modulo%Rowtype;

   aAssCosti ass_cdp_pdgp%Rowtype;
   aAssSpese ass_cdp_pdgp%Rowtype;

   Type typeModuloSpese Is Table Of pdg_modulo_spese%Rowtype Index By BINARY_INTEGER;
   tabModuloSpese typeModuloSpese;
   trovatoModuloSpese Boolean := False;

   Type typeAssCdpPdgP Is Table Of ass_cdp_pdgp%Rowtype Index By BINARY_INTEGER;
   tabAssCdpPdgP typeAssCdpPdgP;

   aDestS    pdg_modulo_spese%Rowtype;
   aDestC    pdg_modulo_costi%Rowtype;

   aTSNow DATE;
   aModuloTmp progetto_prev%Rowtype;

   aNum NUMBER(8);

   aVoce elemento_voce%Rowtype;
   aVoceTFR elemento_voce%Rowtype;
   aVoceTFRTEMPODET elemento_voce%Rowtype;
   aVoceONERICNR elemento_voce%Rowtype;
   aVoceONERICNRTEMPODET elemento_voce%Rowtype;
   aParCNR parametri_cnr%Rowtype;
   aCdClassificazione v_classificazione_voci_all.cd_classificazione%Type;
   aAllVociClassificazione v_classificazione_voci_all%Rowtype;
   aAllVociClassificazioneDef v_classificazione_voci_all%Rowtype;
   isTempoDeterminato Boolean;
   prog number:=0;
   aLATmp  v_linea_attivita_valida%Rowtype;
   aModulo progetto_prev%Rowtype;
   isDettRedistrRottiFound Boolean;
   isProcessato Boolean;
   isVoceTFR Boolean;
   matricola_err         VARCHAR2(6000);
   aCDRPersonale cdr%Rowtype;
 Begin
   aTSNow:=sysdate;

   -- Leggo il CDR
   aCDRRUO:=CNRCTB020.GETCDRVALIDO(aEsercizio, aCdCdr);

   -- Leggo la UO del CDR
   aUO := CNRCTB020.getUO(aCDRRUO);

   if to_number(aCDRRUO.cd_proprio_cdr) != 0 then
     IBMERR001.RAISE_ERR_GENERICO('Operazione permessa solo su cdr di tipo RUO!');
   end if;

   -- Leggo il CDR del personale

   aCDRPersonale:=CNRCTB020.GETCDRPERSONALE(aEsercizio);

   -- Lock del PDG del CDR in processo
   CNRCTB051.LOCKPDGP(aEsercizio, aCdCdr);

   Select count(*) into aNum
   From ass_cdp_pdgp
   Where esercizio = aEsercizio
   And   ti_costi_spese = CNRCTB061.TAB_COSTI
   And   cd_centro_responsabilita in (Select cd_centro_responsabilita
                                      From V_PDGP_CDR_RUO_NRUO
                                      Where esercizio = aEsercizio
                          And   cd_cdr_root =  aCdCdr
                          And   (aUO.cd_tipo_unita != CNRCTB020.TIPO_SAC Or
                                 cd_unita_organizzativa = aUO.cd_unita_organizzativa));

   If aNum > 0 then
     IBMERR001.RAISE_ERR_GENERICO('Operazione gia effettuata su cdr '||aCdCdr);
   End if;

   -- Controllo modificabilita PDGP CDR Personale

   For aPDGP in PDGP_CON_CONFIG_SCR(aEsercizio, aCDRRUO.cd_centro_responsabilita, aUO) loop
     If not (aPDGP.stato in (
      CNRCTB051.STATO_PDGP_APERTURA,
      CNRCTB051.STATO_PDGP_ADEGUAMENTO)) Then

       -- Leggo il modulo per recuperare la descrizione
       Select * into aModuloTmp from progetto_prev
       Where pg_progetto = aPDGP.pg_progetto
       And   esercizio = aEsercizio;

       IBMERR001.RAISE_ERR_GENERICO('Il piano di gestione preliminare del CDR NRUO '||aPDGP.cd_centro_responsabilita||
                                    ' per il modulo '||aModuloTmp.cd_progetto||' ('||aModuloTmp.cd_progetto_sip||')'||
                                    ' non e'' in stato di apertura o di adeguamento!');
     End If;
   End Loop;

   -- Il totale di scarico configurato dei dipendenti dell'UO e 100%  (comprendento le quote verso altre UO accettate)

   For aUOCDS In (Select * From UNITA_ORGANIZZATIVA
                  Where cd_unita_padre = aUO.cd_unita_padre
                  And  (aUO.cd_tipo_unita != CNRCTB020.TIPO_SAC Or
            cd_unita_organizzativa = aUO.cd_unita_organizzativa)) Loop
       Select count(*) into aNum
       From V_CDP_TOT_PRC a, (Select id_matricola,
                                     Nvl(Sum(Nvl(IM_A1, 0) + Nvl(IM_ONERI_CNR_A1, 0) + Nvl(IM_TFR_A1, 0)),0) IM_A1,
                                     Nvl(Sum(Nvl(IM_A2, 0) + Nvl(IM_ONERI_CNR_A2, 0) + Nvl(IM_TFR_A2, 0)),0) IM_A2,
                                     Nvl(Sum(Nvl(IM_A3, 0) + Nvl(IM_ONERI_CNR_A3, 0) + Nvl(IM_TFR_A3, 0)),0) IM_A3
                              From COSTO_DEL_DIPENDENTE
                              Where COSTO_DEL_DIPENDENTE.esercizio = aEsercizio
                              And   COSTO_DEL_DIPENDENTE.mese = 0
                              And   (COSTO_DEL_DIPENDENTE.cd_unita_organizzativa = aUOCDS.cd_unita_organizzativa or
                                     COSTO_DEL_DIPENDENTE.cd_unita_organizzativa in (Select V_CDP_MATRICOLA_UO.cd_unita_organizzativa
                                                                                     from ass_cdp_uo, V_CDP_MATRICOLA_UO
                                                                                     Where ass_cdp_uo.stato In ('X', 'Y')
                                                                                     and   ass_cdp_uo.esercizio = COSTO_DEL_DIPENDENTE.esercizio
                                                                                     and   ass_cdp_uo.mese = COSTO_DEL_DIPENDENTE.mese
                                                                                     and   ass_cdp_uo.id_matricola = COSTO_DEL_DIPENDENTE.id_matricola
                                                                                     and   ass_cdp_uo.esercizio = V_CDP_MATRICOLA_UO.esercizio
                                                                                     and   ass_cdp_uo.mese = V_CDP_MATRICOLA_UO.mese
                                                                                     And   ass_cdp_uo.id_matricola = V_CDP_MATRICOLA_UO.id_matricola))
                              Group By id_matricola) b
       Where a.esercizio = aEsercizio
       And   a.mese = 0
       And   a.cd_unita_organizzativa = aUOCDS.cd_unita_organizzativa
       And   a.id_matricola = b.id_matricola
       And  ((b.im_a1 != 0 And prc_a1 < 100) Or
             (b.im_a2 != 0 And prc_a2 < 100) Or
             (b.im_a3 != 0 And prc_a3 < 100));

       If aNum > 0 Then
          For rec_err In (Select Distinct a.id_matricola
                          From V_CDP_TOT_PRC a, (Select id_matricola,
                                     Nvl(Sum(Nvl(IM_A1, 0) + Nvl(IM_ONERI_CNR_A1, 0) + Nvl(IM_TFR_A1, 0)),0) IM_A1,
                                     Nvl(Sum(Nvl(IM_A2, 0) + Nvl(IM_ONERI_CNR_A2, 0) + Nvl(IM_TFR_A2, 0)),0) IM_A2,
                                     Nvl(Sum(Nvl(IM_A3, 0) + Nvl(IM_ONERI_CNR_A3, 0) + Nvl(IM_TFR_A3, 0)),0) IM_A3
                                                 From COSTO_DEL_DIPENDENTE
                                                 Where COSTO_DEL_DIPENDENTE.esercizio = aEsercizio
                                                 And   COSTO_DEL_DIPENDENTE.mese = 0
                                                 And   (COSTO_DEL_DIPENDENTE.cd_unita_organizzativa = aUOCDS.cd_unita_organizzativa or
                                                        COSTO_DEL_DIPENDENTE.cd_unita_organizzativa in (Select V_CDP_MATRICOLA_UO.cd_unita_organizzativa
                                                                                                        from ass_cdp_uo, V_CDP_MATRICOLA_UO
                                                                                                        Where ass_cdp_uo.stato In ('X', 'Y')
                                                                                                        and   ass_cdp_uo.esercizio = COSTO_DEL_DIPENDENTE.esercizio
                                                                                                        and   ass_cdp_uo.mese = COSTO_DEL_DIPENDENTE.mese
                                                                                                        and   ass_cdp_uo.id_matricola = COSTO_DEL_DIPENDENTE.id_matricola
                                                                                                        and   ass_cdp_uo.esercizio = V_CDP_MATRICOLA_UO.esercizio
                                                                                                        and   ass_cdp_uo.mese = V_CDP_MATRICOLA_UO.mese
                                                                                                        And   ass_cdp_uo.id_matricola = V_CDP_MATRICOLA_UO.id_matricola))
                                                 Group By id_matricola) b
                          Where a.esercizio = aEsercizio
                          And   a.mese = 0
                          And   a.cd_unita_organizzativa = aUOCDS.cd_unita_organizzativa
                          And   a.id_matricola = b.id_matricola
                          And  ((b.im_a1 != 0 And prc_a1 < 100) Or
                                (b.im_a2 != 0 And prc_a2 < 100) Or
                                (b.im_a3 != 0 And prc_a3 < 100))) Loop

              matricola_err := matricola_err||' - '||rec_err.id_matricola;
          End Loop;
          IBMERR001.RAISE_ERR_GENERICO('La configurazione di scarico dei dipendenti dell''UO '||aUOCDS.cd_unita_organizzativa||' non e'' completa per n. '||anum||' matricola/e ('||matricola_err||')');
       End If;
   End Loop;

   -- Carico la voce del piano TFR e ONERICNR per il personale a tempo indeterminato

   aVoceTFR:=CNRCTB000.GETVOCETFR(aEsercizio);
   aVoceONERICNR:=CNRCTB000.GETVOCEONERICNR(aEsercizio);

   -- Carico la voce del piano TFR e ONERICNR per il personale a tempo determinato

   aVoceTFRTEMPODET:=CNRCTB000.GETVOCETFRTEMPODET(aEsercizio);
   aVoceONERICNRTEMPODET:=CNRCTB000.GETVOCEONERICNRTEMPODET(aEsercizio);


   -- Leggo i parametri per conoscere il livello classificazione di inserimento

   Begin
     Select * into aParCNR from parametri_cnr
     Where esercizio = aEsercizio;
   Exception
     When No_Data_Found Then
        IBMERR001.RAISE_ERR_GENERICO('Mancano i parametri CNR per l''esercizio '||aEsercizio||
                                     'necessari per la determinazione del livello classificazione da utilizzare per '||
                                     'il caricamento del PdgP. Impossibile lo scarico.');
   End;

   -- Start dello scarico su PDG dei costi del personale

   For aASSCDPLA In (
       Select * From V_CDP_SPACCATO_CDR_LA_VOCE Where
            esercizio = aEsercizio
      And mese = 0
      And cd_cdr_root In (Select cd_centro_responsabilita from V_PDGP_CDR_RUO_NRUO
                                      Where esercizio = aEsercizio
                          And cd_cdr_root =  aCdCdr
                          And   (aUO.cd_tipo_unita != CNRCTB020.TIPO_SAC Or
                                 cd_unita_organizzativa = aUO.cd_unita_organizzativa))
      Order by cd_cdr) loop  -- Cicla sulle configurazioni di scarico

     -- Determina il tipo di rapporto dalla tabella COSTO_DEL_DIPENDENTE

     isTempoDeterminato:=false;
     If aASSCDPLA.ti_rapporto = CDP_TI_RAPP_DETERMINATO Then
       isTempoDeterminato:=true;
     End if;

     -- Leggo la linea di attivita per aggiornare il dettaglio del PDG con funzione e natura
     Begin
       Select * into aLATmp from v_linea_attivita_valida
       Where esercizio = aASSCDPLA.esercizio
       and   cd_linea_attivita = aASSCDPLA.cd_linea_attivita
       And   cd_centro_responsabilita = aASSCDPLA.cd_cdr;
     Exception
       When No_Data_Found Then
          IBMERR001.RAISE_ERR_GENERICO('E'' stato imputato, nella ripartizione dei costi del personale, un codice G.A.E. '||
 aASSCDPLA.cd_linea_attivita||' per il cdr '||aASSCDPLA.cd_cdr||' inesistente. Impossibile lo scarico.');
     End;

     If aLaTmp.cd_natura = '5' then
        IBMERR001.RAISE_ERR_GENERICO('Esiste una ripartizione dei CDP su G.A.E. di natura 5. Impossibile lo scarico.');
     End if;

     -- Leggo il modulo associato alla Linea di Attivit?
     If aLaTmp.pg_progetto Is Null then
          IBMERR001.RAISE_ERR_GENERICO('E'' stato imputato, nella ripartizione dei costi del personale, un codice G.A.E. '||
 aASSCDPLA.cd_linea_attivita||' al quale non risulta associato alcun modulo. Impossibile lo scarico.');
     End if;

     Begin
       Select * into aModulo from progetto_prev
       Where pg_progetto = aLATmp.pg_progetto
       And   esercizio = aEsercizio;
     Exception
       When No_Data_Found Then
          IBMERR001.RAISE_ERR_GENERICO('E'' stato imputato, nella ripartizione dei costi del personale, un codice G.A.E. '||
 aASSCDPLA.cd_linea_attivita||' al quale risulta associato un progressivo modulo ('||aLATmp.pg_progetto||') inesistente. Impossibile lo scarico.');
     End;

     If aLaTmp.cd_natura = '5' then
        IBMERR001.RAISE_ERR_GENERICO('Esiste una ripartizione dei CDP su G.A.E. di natura 5. Impossibile lo scarico.');
     End if;

     -- Leggo la voce per recuperare la classificazione
     Begin
       Select * into aVoce from elemento_voce
       Where esercizio = aASSCDPLA.esercizio
       And   ti_appartenenza = aASSCDPLA.ti_appartenenza
       And   ti_gestione = aASSCDPLA.ti_gestione
       And   cd_elemento_voce = aASSCDPLA.cd_elemento_voce;
     Exception
       When No_Data_Found Then
          IBMERR001.RAISE_ERR_GENERICO('E'' stata indicata, nei costi del personale, un codice elemento voce '||
                                       aASSCDPLA.cd_elemento_voce||' inesistente. Impossibile lo scarico.');
     End;

     If aVoce.id_classificazione Is Null then
        IBMERR001.RAISE_ERR_GENERICO('Manca la classificazione sulla voce '||aVoce.cd_elemento_voce||' dell''esercizio '||aVoce.esercizio||'. Impossibile lo scarico.');
     End if;

     -- Leggo tutta la classificazione per reperire i suoi livelli precedenti
     Begin
       Select * into aAllVociClassificazione from v_classificazione_voci_all
       Where id_classificazione = aVoce.id_classificazione;
     Exception
       When No_Data_Found Then
          IBMERR001.RAISE_ERR_GENERICO('E'' stata indicata, nei costi del personale, un codice elemento voce '||
                                       aASSCDPLA.cd_elemento_voce||' con classificazione ('||aVoce.id_classificazione||
                                       ') non presente nella tabella corrispondente. Impossibile lo scarico.');
     End;

     aCdClassificazione := Null;
     -- Individuo la classificazione da utilizzare
     If aParCNR.LIVELLO_PDG_DECIS_SPE = 1 Then
       aCdClassificazione := aAllVociClassificazione.cd_liv1;
     ElsIf aParCNR.LIVELLO_PDG_DECIS_SPE = 2 Then
       aCdClassificazione := aAllVociClassificazione.cd_liv2;
     ElsIf aParCNR.LIVELLO_PDG_DECIS_SPE = 3 Then
       aCdClassificazione := aAllVociClassificazione.cd_liv3;
     ElsIf aParCNR.LIVELLO_PDG_DECIS_SPE = 4 Then
       aCdClassificazione := aAllVociClassificazione.cd_liv4;
     ElsIf aParCNR.LIVELLO_PDG_DECIS_SPE = 5 Then
       aCdClassificazione := aAllVociClassificazione.cd_liv5;
     ElsIf aParCNR.LIVELLO_PDG_DECIS_SPE = 6 Then
       aCdClassificazione := aAllVociClassificazione.cd_liv6;
     ElsIf aParCNR.LIVELLO_PDG_DECIS_SPE = 7 Then
       aCdClassificazione := aAllVociClassificazione.cd_liv7;
     End If;

     If aCdClassificazione Is Null Then
        IBMERR001.RAISE_ERR_GENERICO('E'' stata indicata, nei costi del personale, un codice elemento voce '||
                                     aASSCDPLA.cd_elemento_voce||' con classificazione ('||aVoce.id_classificazione||
                                     ') che ha generato anomalia nella ricerca del codice livello necessario per il '||
                                     'caricamento del PdgP. Impossibile lo scarico.'||aParCNR.LIVELLO_PDG_DECIS_SPE);
     End If;

     -- Leggo la classificazione finale da inserire in PDG_MODULO_SPESE
     Begin
       Select * into aAllVociClassificazioneDef from v_classificazione_voci_all
       Where esercizio = aASSCDPLA.esercizio
       And   ti_gestione = aASSCDPLA.ti_gestione
       And   cd_classificazione = aCdClassificazione;
     Exception
       When No_Data_Found Then
          IBMERR001.RAISE_ERR_GENERICO('E'' stata indicata, nei costi del personale, un codice elemento voce '||
                                       aASSCDPLA.cd_elemento_voce||' con classificazione ('||aVoce.id_classificazione||
                                       ') che ha generato anomalia nella ricerca del livello necessario per il '||
                                       'caricamento del PdgP. Impossibile lo scarico.');
     End;

     isVoceTFR:=False;
     If aASSCDPLA.cd_elemento_voce = aVoceTFR.cd_elemento_voce Or
        aASSCDPLA.cd_elemento_voce = aVoceTFRTEMPODET.cd_elemento_voce Then
        isVoceTFR:=True;
     End If;

     Begin
       -- Leggo il PDG_MODULO per decidere se inserirlo in quanto necessario per caricare il resto dei dati
       Select * into aPdgModulo from pdg_modulo
       Where esercizio = aEsercizio
       And   cd_centro_responsabilita = aCDRRUO.cd_centro_responsabilita
       And   pg_progetto = aLATmp.pg_progetto;
     Exception
       When No_Data_Found Then
          IBMERR001.RAISE_ERR_GENERICO('Non risulta essere caricato nel PdgP il modulo '
                                       ||aModulo.cd_progetto||' ('||aModulo.cd_progetto_sip||') '||
                                       'associato al Cdr '||aLATmp.cd_centro_responsabilita||'. '||
                                       'Impossibile lo scarico.');
     End;

     Begin
       -- Leggo il modulo_costi per decidere se inserire o aggiornare
       Select * into aDestC from pdg_modulo_costi
       Where esercizio = aPdgModulo.esercizio
       And   cd_centro_responsabilita = aPdgModulo.cd_centro_responsabilita
       And   pg_progetto = aPdgModulo.pg_progetto;

       If isVoceTFR then
          setImportoTFR(aASSCDPLA, aDestC, isTempoDeterminato);

          Update pdg_modulo_costi
          Set RIS_ES_PREC_TIT_I       = aDestC.RIS_ES_PREC_TIT_I,
              RIS_ES_PREC_TIT_II      = aDestC.RIS_ES_PREC_TIT_II,
              RIS_PRES_ES_PREC_TIT_I  = aDestC.RIS_PRES_ES_PREC_TIT_I,
              RIS_PRES_ES_PREC_TIT_II = aDestC.RIS_PRES_ES_PREC_TIT_II,
              IM_COSTI_GENERALI       = aDestC.IM_COSTI_GENERALI,
              IM_CF_TFR               = aDestC.IM_CF_TFR,
              IM_CF_TFR_DET           = aDestC.IM_CF_TFR_DET,
              IM_CF_AMM_IMMOBILI      = aDestC.IM_CF_AMM_IMMOBILI,
              IM_CF_AMM_ATTREZZ       = aDestC.IM_CF_AMM_ATTREZZ,
              IM_CF_AMM_ALTRO         = aDestC.IM_CF_AMM_ALTRO,
              utuv=aUser,
              duva=aTSNow,
              pg_ver_rec = pg_ver_rec + 1
          Where esercizio = aDestC.esercizio
          And   cd_centro_responsabilita = aDestC.cd_centro_responsabilita
          And   pg_progetto = aDestC.pg_progetto;
       End If;
     Exception
       When No_Data_Found Then
         aDestC:=null;
         aDestC.ESERCIZIO:=aPdgModulo.Esercizio;
         aDestC.CD_CENTRO_RESPONSABILITA:=aPdgModulo.cd_centro_responsabilita;
         aDestC.PG_PROGETTO:=aPdgModulo.pg_progetto;

         CNRCTB051.resetCampiImporto(aDestC);

         If isVoceTFR then
            setImportoTFR(aASSCDPLA, aDestC, isTempoDeterminato);
         End If;

         aDestC.DACR:=aTSNow;
         aDestC.UTCR:=aUser;
         aDestC.DUVA:=aTSNow;
         aDestC.UTUV:=aUser;
         aDestC.PG_VER_REC:=1;
         CNRCTB051.ins_PDG_MODULO_COSTI (aDestC);
     End;

     aAssCosti:=Null;
     Begin
       Select * into aAssCosti From ASS_CDP_PDGP
       Where ESERCIZIO = aDestC.Esercizio
       And   TI_COSTI_SPESE = CNRCTB061.TAB_COSTI
       And   ID_MATRICOLA = aASSCDPLA.ti_rapporto
       And   CD_CENTRO_RESPONSABILITA = aDestC.cd_centro_responsabilita
       And   PG_PROGETTO_COSTI = aDestC.pg_progetto
       And   PG_PROGETTO_SPESE Is Null;
     Exception
       When No_Data_Found Then
         aAssCosti.ESERCIZIO:=aDestC.Esercizio;
         aAssCosti.TI_COSTI_SPESE:=CNRCTB061.TAB_COSTI;
         aAssCosti.ID_MATRICOLA:=aASSCDPLA.ti_rapporto;
         aAssCosti.CD_CENTRO_RESPONSABILITA:=aDestC.cd_centro_responsabilita;
         aAssCosti.PG_PROGETTO_COSTI:=aDestC.pg_progetto;
         aAssCosti.PG_PROGETTO_SPESE:=Null;
         aAssCosti.ID_CLASSIFICAZIONE:=Null;
         aAssCosti.CD_CDS_AREA:=Null;
         aAssCosti.PG_RIGA:=0;
         aAssCosti.DACR:=aTSNow;
         aAssCosti.UTCR:=aUser;
         aAssCosti.DUVA:=aTSNow;
         aAssCosti.UTUV:=aUser;
         aAssCosti.PG_VER_REC:=1;
         --DBMS_output.put_line('1 miss '||aLATmp.cd_missione);
         aAssCosti.cd_cofog:=aLATmp.cd_cofog;
         aAssCosti.cd_missione:=aLATmp.cd_missione;
         tabAssCdpPdgP(tabAssCdpPdgP.count+1):=aAssCosti;

         -- Accantonamento degli arrotondamenti dovuti allo scarico corrente (per elemento voce e tipo rapporto)
         fill_round_table(aASSCDPLA, aTSNow, aUser);
     End;

     If Not (isVoceTFR) Then
       Begin
         -- Leggo il modulo_spese per decidere se inserire o aggiornare
         --dbms_output.put_line('modulo spese '||aDestC.pg_progetto||' clas '|| aAllVociClassificazioneDef.id_classificazione||' cofog '||aLATmp.cd_cofog||' mis '||aLATmp.cd_missione);
         Select * into aDestS from pdg_modulo_spese
         Where esercizio = aDestC.esercizio
         And   cd_centro_responsabilita = aDestC.cd_centro_responsabilita
         And   pg_progetto = aDestC.pg_progetto
         And   id_classificazione = aAllVociClassificazioneDef.id_classificazione
         And   cd_cds_area = aUO.cd_unita_padre
         and   ((aParCNR.LIVELLO_PDG_COFOG != 0 and cd_cofog = aLATmp.cd_cofog) or
               (aParCNR.LIVELLO_PDG_COFOG = 0 and pg_dettaglio = 1))
         and ((aParCNR.fl_nuovo_pdg= 'Y' and cd_missione = aLATmp.cd_missione) or
                     (aParCNR.fl_nuovo_pdg = 'N' and pg_dettaglio=1));

         IBMERR001.RAISE_ERR_GENERICO('Risulta gia'' caricato nel PdgP per il modulo '
                                      ||aModuloTmp.cd_progetto||' ('||aModuloTmp.cd_progetto_sip||') '||
                                      'associato al Cdr '||aDestC.cd_centro_responsabilita||' un dettaglio sulla '||
                                      'classificazione '||aAllVociClassificazioneDef.cd_classificazione||'. '||
                                      'Impossibile lo scarico.');
       Exception
         When No_Data_Found Then
           aDestS:=null;
           --dbms_output.put_line('modulo spese no data '||aDestC.pg_progetto||' '||aLATmp.cd_cofog||' class '||aAllVociClassificazioneDef.cd_classificazione);
           Select Nvl(Max(pg_dettaglio), 0) + 1 Into aDEstS.pg_dettaglio
            From pdg_modulo_spese
            Where  esercizio = aDestC.esercizio
             And   cd_centro_responsabilita = aDestC.cd_centro_responsabilita
             And   pg_progetto = aDestC.pg_progetto
             And   id_classificazione = aAllVociClassificazioneDef.id_classificazione
             And   cd_cds_area = aUO.cd_unita_padre
             and  ((aParCNR.LIVELLO_PDG_COFOG != 0 and cd_cofog = aLATmp.cd_cofog) or
                   (aParCNR.LIVELLO_PDG_COFOG = 0 and pg_dettaglio = 1))
             and ((aParCNR.fl_nuovo_pdg= 'Y' and cd_missione = aLATmp.cd_missione) or
                     (aParCNR.fl_nuovo_pdg = 'N' and pg_dettaglio = 1));

           aDestS.ESERCIZIO:=aDestC.Esercizio;
           aDestS.CD_CENTRO_RESPONSABILITA:=aDestC.cd_centro_responsabilita;
           aDestS.PG_PROGETTO:=aDestC.pg_progetto;
           aDestS.ID_CLASSIFICAZIONE:=aAllVociClassificazioneDef.id_classificazione;
           aDestS.CD_CDS_AREA:=aUO.cd_unita_padre;
           aDestS.CD_COFOG:=aLATmp.cd_cofog;
           aDestS.CD_MISSIONE:=aLATmp.cd_missione;
           CNRCTB051.resetCampiImporto(aDestS);

           setImporto(aASSCDPLA, aDestS);

           aDestS.DACR:=aTSNow;
           aDestS.UTCR:=aUser;
           aDestS.DUVA:=aTSNow;
           aDestS.UTUV:=aUser;
           aDestS.PG_VER_REC:=1;

           trovatoModuloSpese := False;
           For i In 1..tabModuloSpese.count Loop
             If tabModuloSpese(i).ESERCIZIO = aDestS.ESERCIZIO And
                tabModuloSpese(i).CD_CENTRO_RESPONSABILITA = aDestS.CD_CENTRO_RESPONSABILITA And
                tabModuloSpese(i).PG_PROGETTO = aDestS.PG_PROGETTO And
                tabModuloSpese(i).ID_CLASSIFICAZIONE = aDestS.ID_CLASSIFICAZIONE And
                tabModuloSpese(i).CD_CDS_AREA = aDestS.CD_CDS_AREA and
                ((aParCNR.LIVELLO_PDG_COFOG != 0 and tabModuloSpese(i).cd_cofog = aLATmp.cd_cofog) or
                (aParCNR.LIVELLO_PDG_COFOG = 0 and tabModuloSpese(i).pg_dettaglio = aDestS.pg_dettaglio)) and
                ((aParCNR.fl_nuovo_pdg= 'Y' and         tabModuloSpese(i).cd_missione = aLATmp.cd_missione) or
                         (aParCNR.fl_nuovo_pdg = 'N' and tabModuloSpese(i).pg_dettaglio = aDestS.pg_dettaglio))  then
                 tabModuloSpese(i).IM_SPESE_GEST_DECENTRATA_EST := Nvl(tabModuloSpese(i).IM_SPESE_GEST_DECENTRATA_EST, 0) +
                                                                  Nvl(aDestS.IM_SPESE_GEST_DECENTRATA_EST, 0);
                tabModuloSpese(i).IM_SPESE_GEST_DECENTRATA_INT := Nvl(tabModuloSpese(i).IM_SPESE_GEST_DECENTRATA_INT, 0) +
                                                                  Nvl(aDestS.IM_SPESE_GEST_DECENTRATA_INT, 0);
                tabModuloSpese(i).IM_SPESE_GEST_ACCENTRATA_EST := Nvl(tabModuloSpese(i).IM_SPESE_GEST_ACCENTRATA_EST, 0) +
                                                                  Nvl(aDestS.IM_SPESE_GEST_ACCENTRATA_EST, 0);
                tabModuloSpese(i).IM_SPESE_GEST_ACCENTRATA_INT := Nvl(tabModuloSpese(i).IM_SPESE_GEST_ACCENTRATA_INT, 0) +
                                                                  Nvl(aDestS.IM_SPESE_GEST_ACCENTRATA_INT, 0);
                trovatoModuloSpese := True;
                Exit;
             End If;
           End Loop;
           If Not trovatoModuloSpese Then
             tabModuloSpese(tabModuloSpese.count+1):=aDestS;
           End If;
           --dbms_output.put_line('modulo spese2 '||aDestS.pg_progetto||' clas '|| aDestS.id_classificazione||' cofog '||aLATmp.cd_cofog||' mis '||aLATmp.cd_missione);
           aAssSpese:=Null;
           aAssSpese.ESERCIZIO:=aDestS.Esercizio;
           aAssSpese.TI_COSTI_SPESE:=CNRCTB061.TAB_COSTI;
           aAssSpese.ID_MATRICOLA:=aASSCDPLA.ti_rapporto;
           aAssSpese.CD_CENTRO_RESPONSABILITA:=aDestS.cd_centro_responsabilita;
           aAssSpese.PG_PROGETTO_COSTI:=aDestS.pg_progetto;
           aAssSpese.PG_RIGA:=0;
           aAssSpese.PG_PROGETTO_SPESE:=aDestS.pg_progetto;
           aAssSpese.ID_CLASSIFICAZIONE:=aDestS.id_classificazione;
           aAssSpese.CD_CDS_AREA:=aDestS.cd_cds_area;
           aAssSpese.CD_COFOG:=aLATmp.cd_cofog;
           aAssSpese.CD_missione:=aLATmp.cd_missione;
           aAssSpese.DACR:=aTSNow;
           aAssSpese.UTCR:=aUser;
           aAssSpese.DUVA:=aTSNow;
           aAssSpese.UTUV:=aUser;
           aAssSpese.PG_VER_REC:=1;

           tabAssCdpPdgP(tabAssCdpPdgP.count+1):=aAssSpese;
       End;
     End If;

     Update ass_cdp_la
     Set stato = STATO_CDP_PDGP_SCARICATO,
         dt_scarico = aTSNow,
         utuv=aUser,
         duva=aTSNow,
         pg_ver_rec = pg_ver_rec + 1
     Where esercizio = aASSCDPLA.esercizio
     And   mese = 0
     And   cd_linea_attivita = aASSCDPLA.cd_linea_attivita
     And   stato = STATO_CDP_NON_SCARICATO
     And   cd_centro_responsabilita In (Select cd_centro_responsabilita from V_PDGP_CDR_RUO_NRUO
                                        Where esercizio = aEsercizio
                            And cd_cdr_root =  aCdCdr
                            And (aUO.cd_tipo_unita != CNRCTB020.TIPO_SAC Or
                                 cd_unita_organizzativa = aUO.cd_unita_organizzativa));
   End loop;

   --Carico la tabella PDG_MODULO_SPESE con i valori conservati nella PLSQLTABLE
   For i In 1..tabModuloSpese.count Loop
     Begin
       -- Leggo il modulo_spese per inserire il dettaglio....se lo trovo sollevo una eccezione
       Select * into aDestS from pdg_modulo_spese
       Where esercizio = tabModuloSpese(i).esercizio
       And   cd_centro_responsabilita = tabModuloSpese(i).cd_centro_responsabilita
       And   pg_progetto = tabModuloSpese(i).pg_progetto
       And   id_classificazione = tabModuloSpese(i).id_classificazione
       And   cd_cds_area = tabModuloSpese(i).cd_cds_area and
       ((aParCNR.LIVELLO_PDG_COFOG != 0 and cd_cofog = tabModuloSpese(i).cd_cofog ) or
        (aParCNR.LIVELLO_PDG_COFOG = 0 and pg_dettaglio = tabModuloSpese(i).pg_dettaglio))
        and ((aParCNR.fl_nuovo_pdg= 'Y' and cd_missione = tabModuloSpese(i).cd_missione) or
                (aParCNR.fl_nuovo_pdg = 'N' and pg_dettaglio = tabModuloSpese(i).pg_dettaglio));

       Begin
         Select * into aModulo from progetto_prev
         Where pg_progetto = tabModuloSpese(i).pg_progetto
         And   esercizio = tabModuloSpese(i).esercizio;
       Exception
         When No_Data_Found Then
           IBMERR001.RAISE_ERR_GENERICO('E'' stato imputato, nella ripartizione dei costi del personale, un codice G.A.E. '||
                                       ' al quale risulta associato un progressivo modulo ('||
                                       tabModuloSpese(i).pg_progetto||') inesistente. Impossibile lo scarico.');
       End;

       Begin
         Select * into aAllVociClassificazioneDef from v_classificazione_voci_all
         Where esercizio = tabModuloSpese(i).esercizio
         And   id_classificazione = tabModuloSpese(i).id_classificazione;
       Exception
         When No_Data_Found Then
            IBMERR001.RAISE_ERR_GENERICO('E'' stata indicata, nei costi del personale, un codice elemento voce '||
                                         'con classificazione ('||tabModuloSpese(i).id_classificazione||
                                         ') inesistente. Impossibile lo scarico.');
       End;

       IBMERR001.RAISE_ERR_GENERICO('Risulta gia'' caricato nel PdgP per il modulo '||
                                    aModuloTmp.cd_progetto||' ('||aModuloTmp.cd_progetto_sip||')  '||
                                    'associato al Cdr '||tabModuloSpese(i).cd_centro_responsabilita||' un dettaglio sulla '||
                                    'classificazione '||aAllVociClassificazioneDef.cd_classificazione||'. '||
                                    'Impossibile lo scarico.');
     Exception
       When No_Data_Found Then
        Select Nvl(Max(pg_dettaglio), 0) + 1 Into tabModuloSpese(i).pg_dettaglio
            From pdg_modulo_spese
            Where  esercizio = tabModuloSpese(i).esercizio
             And   cd_centro_responsabilita = tabModuloSpese(i).cd_centro_responsabilita
             And   pg_progetto = tabModuloSpese(i).pg_progetto
             And   id_classificazione = tabModuloSpese(i).id_classificazione
             And   cd_cds_area = tabModuloSpese(i).cd_cds_area;
          --dbms_output.put_line('modulo spese 3 '|| tabModuloSpese(i).esercizio||' '||tabModuloSpese(i).cd_centro_responsabilita||' prog '||tabModuloSpese(i).pg_progetto||' '||tabModuloSpese(i).id_classificazione||' '||tabModuloSpese(i).cd_cds_area||' '||tabModuloSpese(i).pg_dettaglio||' '||tabModuloSpese(i).cd_cofog||tabModuloSpese(i).cd_missione);
          CNRCTB051.INS_PDG_MODULO_SPESE(tabModuloSpese(i));
     End;
   End Loop;

   --Carico la tabella ASS_CDP_PDGP con i valori conservati nella PLSQLTABLE relativamente alla sola
   --tabella PDG_MODULO_SPESE
   For i In 1..tabAssCdpPdgP.count Loop
     Select Nvl(Max(pg_riga), 0) + 1 Into tabAssCdpPdgP(i).pg_riga
     From ASS_CDP_PDGP
     Where ESERCIZIO = tabAssCdpPdgP(i).Esercizio
     And   TI_COSTI_SPESE = tabAssCdpPdgP(i).ti_costi_spese
     And   ID_MATRICOLA = tabAssCdpPdgP(i).id_matricola
     And   CD_CENTRO_RESPONSABILITA = tabAssCdpPdgP(i).cd_centro_responsabilita
     And   PG_PROGETTO_COSTI = tabAssCdpPdgP(i).pg_progetto_costi;

     INS_ASS_CDP_PDGP(tabAssCdpPdgP(i));
   End Loop;

   -- Distribuzione dei rotti su dettagli creati
   For aAssCdpPdgPRnd In (Select * From ass_cdp_pdgp_round
                          Where esercizio = aEsercizio
              And   cd_cdr_root = aCDRRUO.cd_centro_responsabilita
              For update nowait) loop  -- Cicla sulle configurazioni dei ROTTI

     isDettRedistrRottiFound:=false;

     -- Leggo la voce per recuperare la classificazione
     Begin
       Select * into aVoce from elemento_voce
       Where esercizio = aAssCdpPdgPRnd.esercizio
       And   ti_appartenenza = aAssCdpPdgPRnd.ti_appartenenza
       And   ti_gestione = aAssCdpPdgPRnd.ti_gestione
       And   cd_elemento_voce = aAssCdpPdgPRnd.cd_elemento_voce;
     Exception
       When No_Data_Found Then
          IBMERR001.RAISE_ERR_GENERICO('E'' stata indicata, nei costi del personale, un codice elemento voce '||
                                       aAssCdpPdgPRnd.cd_elemento_voce||' inesistente. Impossibile lo scarico.');
     End;

     If aVoce.id_classificazione Is Null then
        IBMERR001.RAISE_ERR_GENERICO('Manca la classificazione sulla voce '||aVoce.cd_elemento_voce||' dell''esercizio '||aVoce.esercizio||'. Impossibile lo scarico.');
     End if;

     -- Leggo tutta la classificazione per reperire i suoi livelli precedenti
     Begin
       Select * into aAllVociClassificazione from v_classificazione_voci_all
       Where id_classificazione = aVoce.id_classificazione;
     Exception
       When No_Data_Found Then
          IBMERR001.RAISE_ERR_GENERICO('E'' stata indicata, nei costi del personale, un codice elemento voce '||
                                       aAssCdpPdgPRnd.cd_elemento_voce||' con classificazione ('||aVoce.id_classificazione||
                                       ') non presente nella tabella corrispondente. Impossibile lo scarico.');
     End;

     aCdClassificazione := Null;
     -- Individuo la classificazione da utilizzare
     If aParCNR.LIVELLO_PDG_DECIS_SPE = 1 Then
       aCdClassificazione := aAllVociClassificazione.cd_liv1;
     ElsIf aParCNR.LIVELLO_PDG_DECIS_SPE = 2 Then
       aCdClassificazione := aAllVociClassificazione.cd_liv2;
     ElsIf aParCNR.LIVELLO_PDG_DECIS_SPE = 3 Then
       aCdClassificazione := aAllVociClassificazione.cd_liv3;
     ElsIf aParCNR.LIVELLO_PDG_DECIS_SPE = 4 Then
       aCdClassificazione := aAllVociClassificazione.cd_liv4;
     ElsIf aParCNR.LIVELLO_PDG_DECIS_SPE = 5 Then
       aCdClassificazione := aAllVociClassificazione.cd_liv5;
     ElsIf aParCNR.LIVELLO_PDG_DECIS_SPE = 6 Then
       aCdClassificazione := aAllVociClassificazione.cd_liv6;
     ElsIf aParCNR.LIVELLO_PDG_DECIS_SPE = 7 Then
       aCdClassificazione := aAllVociClassificazione.cd_liv7;
     End If;

     If aCdClassificazione Is Null Then
        IBMERR001.RAISE_ERR_GENERICO('E'' stata indicata, nei costi del personale, un codice elemento voce '||
                                     aAssCdpPdgPRnd.cd_elemento_voce||' con classificazione ('||aVoce.id_classificazione||
                                     ') che ha generato anomalia nella ricerca del codice livello necessario per il '||
                                     'caricamento del PdgP. Impossibile lo scarico.'||aParCNR.LIVELLO_PDG_DECIS_SPE);
     End If;

     -- Leggo la classificazione finale da utilizzare per cercare in PDG_MODULO_SPESE
     Begin
       Select * into aAllVociClassificazioneDef from v_classificazione_voci_all
       Where esercizio = aAssCdpPdgPRnd.esercizio
       And   ti_gestione = aAssCdpPdgPRnd.ti_gestione
       And   cd_classificazione = aCdClassificazione;
     Exception
       When No_Data_Found Then
          IBMERR001.RAISE_ERR_GENERICO('E'' stata indicata, nei costi del personale, un codice elemento voce '||
                                       aAssCdpPdgPRnd.cd_elemento_voce||' con classificazione ('||aVoce.id_classificazione||
                                       ') che ha generato anomalia nella ricerca del livello necessario per il '||
                                       'caricamento del PdgP. Impossibile lo scarico.');
     End;

     isVoceTFR:=False;
     If aAssCdpPdgPRnd.cd_elemento_voce = aVoceTFR.cd_elemento_voce Or
        aAssCdpPdgPRnd.cd_elemento_voce = aVoceTFRTEMPODET.cd_elemento_voce Then
        isVoceTFR:=True;
        --dbms_output.put_line('voce tfr');
     End If;

     -- Leggo la linea di attivita per recuperare il modulo
     Begin
       Select * into aLATmp from v_linea_attivita_valida
       Where esercizio = aAssCdpPdgPRnd.esercizio
       and   cd_linea_attivita = aAssCdpPdgPRnd.cd_linea_attivita
       And   cd_centro_responsabilita = aAssCdpPdgPRnd.cd_centro_responsabilita;
     Exception
       When No_Data_Found Then
          IBMERR001.RAISE_ERR_GENERICO('E'' stato imputato, nella ripartizione dei costi del personale, un codice G.A.E. '||
 aAssCdpPdgPRnd.cd_linea_attivita||' per il cdr '||aAssCdpPdgPRnd.cd_centro_responsabilita||' inesistente. Impossibile lo scarico.');
     End;

     If isVoceTFR Then
        For aModSpeDet In (Select p.* From pdg_modulo_costi p
                           Where p.esercizio=aAssCdpPdgPRnd.esercizio
                           And   p.cd_centro_responsabilita=aAssCdpPdgPRnd.cd_cdr_root
                           And   p.pg_progetto = aLATmp.pg_progetto
                           And   Exists (Select 1 From ass_cdp_pdgp
                            Where ESERCIZIO=p.esercizio
                                         And   ID_MATRICOLA=aAssCdpPdgPRnd.id_matricola
                                         And   CD_CENTRO_RESPONSABILITA=p.cd_centro_responsabilita
                                         And   PG_PROGETTO=p.pg_progetto)) Loop
           isProcessato:=false;

           If Not isProcessato And
              aAssCdpPdgPRnd.ID_MATRICOLA = CDP_TI_RAPP_DETERMINATO Then

              If --(aModSpeDet.IM_CF_TFR_DET=0 and aAssCdpPdgPRnd.im_arr_a1 <> 0  ) Or
                  aModSpeDet.IM_CF_TFR_DET+aAssCdpPdgPRnd.im_arr_a1 < 0 Then
                 Null;
                 --dbms_output.put_line('voce tfr null '||aAssCdpPdgPRnd.im_arr_a1 ||' '||aModSpeDet.IM_CF_TFR_DET||' '||aLATmp.pg_progetto||aAssCdpPdgPRnd.cd_linea_attivita||aAssCdpPdgPRnd.cd_cdr_root);
              Else
                 Update pdg_modulo_costi
                 Set IM_CF_TFR_DET=IM_CF_TFR_DET+aAssCdpPdgPRnd.im_arr_a1,
                     utuv=aUser,
                     duva=aTSNow,
                     pg_ver_rec = pg_ver_rec + 1
                 Where ESERCIZIO=aModSpeDet.esercizio
                 And   CD_CENTRO_RESPONSABILITA=aModSpeDet.cd_centro_responsabilita
                 And   PG_PROGETTO=aModSpeDet.pg_progetto;
              --dbms_output.put_line('voce tfr upd '||aAssCdpPdgPRnd.im_arr_a1 ||' '||aModSpeDet.IM_CF_TFR_DET||' '||aLATmp.pg_progetto||aAssCdpPdgPRnd.cd_linea_attivita);
                 isProcessato:=True;
              End If;
           End If;
    If Not isProcessato And
              Not aAssCdpPdgPRnd.ID_MATRICOLA = CDP_TI_RAPP_DETERMINATO Then
                If  --(aModSpeDet.IM_CF_TFR=0 and aAssCdpPdgPRnd.im_arr_a1 <> 0) Or
                  aModSpeDet.IM_CF_TFR+aAssCdpPdgPRnd.im_arr_a1 < 0 Then
                 Null;
               Else
                 Update pdg_modulo_costi
                 Set IM_CF_TFR=IM_CF_TFR+aAssCdpPdgPRnd.im_arr_a1,
                     utuv=aUser,
                     duva=aTSNow,
                     pg_ver_rec = pg_ver_rec + 1
                 Where ESERCIZIO=aModSpeDet.esercizio
                 And   CD_CENTRO_RESPONSABILITA=aModSpeDet.cd_centro_responsabilita
                 And   PG_PROGETTO=aModSpeDet.pg_progetto;
                --dbms_output.put_line('voce tfr update 2');
                 isProcessato:=True;
              End If;
           End If;
           If isProcessato Then
       Update ass_cdp_pdgp_round
       Set pg_progetto_costi = aModSpeDet.pg_progetto,
           im_arr_a1_non_distr = round(aAssCdpPdgPRnd.im_arr_a1 - round(aAssCdpPdgPRnd.im_arr_a1,2),30),
           im_arr_a2_non_distr = round(aAssCdpPdgPRnd.im_arr_a2 - round(aAssCdpPdgPRnd.im_arr_a2,2),30),
           im_arr_a3_non_distr = round(aAssCdpPdgPRnd.im_arr_a3 - round(aAssCdpPdgPRnd.im_arr_a3,2),30),
           utuv=aUser,
           duva=aTSNow,
           pg_ver_rec = pg_ver_rec + 1
              Where ESERCIZIO=aAssCdpPdgPRnd.esercizio
              And   CD_CDR_ROOT=aAssCdpPdgPRnd.cd_cdr_root
              And   TI_APPARTENENZA=aAssCdpPdgPRnd.ti_appartenenza
              And   TI_GESTIONE=aAssCdpPdgPRnd.ti_gestione
              And   CD_ELEMENTO_VOCE=aAssCdpPdgPRnd.cd_elemento_voce
              And   CD_CENTRO_RESPONSABILITA=aAssCdpPdgPRnd.cd_centro_responsabilita
              And   ID_MATRICOLA=aAssCdpPdgPRnd.id_matricola;

              isDettRedistrRottiFound:=True;
              Exit;
           End if;

        End Loop;
     End If;

     If Not isVoceTFR And Not isDettRedistrRottiFound Then
        For aModSpeDet In (Select p.* From pdg_modulo_spese p
                           Where p.esercizio=aAssCdpPdgPRnd.esercizio
                           And   p.cd_centro_responsabilita=aAssCdpPdgPRnd.cd_cdr_root
                           And   p.pg_progetto = aLATmp.pg_progetto
                           And   p.id_classificazione = aAllVociClassificazioneDef.id_classificazione
                           And   p.cd_cds_area = aUO.cd_unita_padre
                           And   Exists (Select 1 From ass_cdp_pdgp
                             Where ESERCIZIO=p.esercizio
                                         And   ID_MATRICOLA=aAssCdpPdgPRnd.id_matricola
                                         And   CD_CENTRO_RESPONSABILITA=p.cd_centro_responsabilita
                                         And   PG_PROGETTO_SPESE=p.pg_progetto
                                         And   ID_CLASSIFICAZIONE=p.id_classificazione
                                         And   CD_CDS_AREA = p.cd_cds_area
                                         AND cd_cofog = P.cd_cofog
                                         and cd_missione =p.cd_missione)) Loop

           isProcessato:=False;

           If Not isProcessato And
              aModSpeDet.cd_centro_responsabilita=aCDRPersonale.cd_centro_responsabilita And
              (aLATmp.cd_natura = '2' Or aLATmp.cd_natura = '3') Then
              If (aModSpeDet.IM_SPESE_GEST_DECENTRATA_EST=0 and aAssCdpPdgPRnd.im_arr_a1 <> 0) Or
                  aModSpeDet.IM_SPESE_GEST_DECENTRATA_EST+aAssCdpPdgPRnd.im_arr_a1 < 0 Then
                  Null;
              Else
                 Update pdg_modulo_spese
                 Set IM_SPESE_GEST_DECENTRATA_EST=IM_SPESE_GEST_DECENTRATA_EST+aAssCdpPdgPRnd.im_arr_a1,
                     utuv=aUser,
                     duva=aTSNow,
                     pg_ver_rec = pg_ver_rec + 1
           Where ESERCIZIO=aModSpeDet.esercizio
                 And   CD_CENTRO_RESPONSABILITA=aModSpeDet.cd_centro_responsabilita
                 And   PG_PROGETTO=aModSpeDet.pg_progetto
                 And   ID_CLASSIFICAZIONE=aModSpeDet.id_classificazione
                 And   CD_CDS_AREA=aModSpeDet.cd_cds_area
                 And   PG_DETTAGLIO=aModSpeDet.PG_DETTAGLIO;
                 isProcessato:=True;
              End If;
           End If;
           If Not isProcessato And
              aModSpeDet.cd_centro_responsabilita=aCDRPersonale.cd_centro_responsabilita And
              Not (aLATmp.cd_natura = '2' Or aLATmp.cd_natura = '3') Then
              If (aModSpeDet.IM_SPESE_GEST_DECENTRATA_INT=0 and aAssCdpPdgPRnd.im_arr_a1 <> 0) Or
                  aModSpeDet.IM_SPESE_GEST_DECENTRATA_INT+aAssCdpPdgPRnd.im_arr_a1 < 0 Then
                  Null;
              Else
                 Update pdg_modulo_spese
                 Set IM_SPESE_GEST_DECENTRATA_INT=IM_SPESE_GEST_DECENTRATA_INT+aAssCdpPdgPRnd.im_arr_a1,
                     utuv=aUser,
                     duva=aTSNow,
                     pg_ver_rec = pg_ver_rec + 1
             Where ESERCIZIO=aModSpeDet.esercizio
                 And   CD_CENTRO_RESPONSABILITA=aModSpeDet.cd_centro_responsabilita
                 And   PG_PROGETTO=aModSpeDet.pg_progetto
                 And   ID_CLASSIFICAZIONE=aModSpeDet.id_classificazione
                 And   CD_CDS_AREA=aModSpeDet.cd_cds_area
                 And   PG_DETTAGLIO=aModSpeDet.PG_DETTAGLIO;

                 isProcessato:=True;
              End If;
           End If;
           If Not isProcessato And
              Not aModSpeDet.cd_centro_responsabilita=aCDRPersonale.cd_centro_responsabilita And
              (aLATmp.cd_natura = '2' Or aLATmp.cd_natura = '3') Then
              If (aModSpeDet.IM_SPESE_GEST_ACCENTRATA_EST=0 and aAssCdpPdgPRnd.im_arr_a1 <> 0) Or
                  aModSpeDet.IM_SPESE_GEST_ACCENTRATA_EST+aAssCdpPdgPRnd.im_arr_a1 < 0 Then
                  Null;
              Else
                 Update pdg_modulo_spese
                 Set IM_SPESE_GEST_ACCENTRATA_EST=IM_SPESE_GEST_ACCENTRATA_EST+aAssCdpPdgPRnd.im_arr_a1,
                     utuv=aUser,
                     duva=aTSNow,
                     pg_ver_rec = pg_ver_rec + 1
           Where ESERCIZIO=aModSpeDet.esercizio
                 And   CD_CENTRO_RESPONSABILITA=aModSpeDet.cd_centro_responsabilita
                 And   PG_PROGETTO=aModSpeDet.pg_progetto
                 And   ID_CLASSIFICAZIONE=aModSpeDet.id_classificazione
                 And   CD_CDS_AREA=aModSpeDet.cd_cds_area
                 And   PG_DETTAGLIO=aModSpeDet.PG_DETTAGLIO;

                 isProcessato:=True;
              End If;
           End If;
           If Not isProcessato And
              Not aModSpeDet.cd_centro_responsabilita=aCDRPersonale.cd_centro_responsabilita And
              Not (aLATmp.cd_natura = '2' Or aLATmp.cd_natura = '3') Then
              If (aModSpeDet.IM_SPESE_GEST_ACCENTRATA_INT=0 and aAssCdpPdgPRnd.im_arr_a1 <> 0) Or
                  aModSpeDet.IM_SPESE_GEST_ACCENTRATA_INT+aAssCdpPdgPRnd.im_arr_a1 < 0 Then
                  Null;
              Else
                 Update pdg_modulo_spese
                 Set IM_SPESE_GEST_ACCENTRATA_INT=IM_SPESE_GEST_ACCENTRATA_INT+aAssCdpPdgPRnd.im_arr_a1,
                     utuv=aUser,
                     duva=aTSNow,
                     pg_ver_rec = pg_ver_rec + 1
           Where ESERCIZIO=aModSpeDet.esercizio
                 And   CD_CENTRO_RESPONSABILITA=aModSpeDet.cd_centro_responsabilita
                 And   PG_PROGETTO=aModSpeDet.pg_progetto
                 And   ID_CLASSIFICAZIONE=aModSpeDet.id_classificazione
                 And   CD_CDS_AREA=aModSpeDet.cd_cds_area
                 And   PG_DETTAGLIO=aModSpeDet.PG_DETTAGLIO;

                 isProcessato:=True;
              End If;
           End If;

           If isProcessato Then
       Update ass_cdp_pdgp_round
       Set pg_progetto_spese = aModSpeDet.pg_progetto,
           id_classificazione = aModSpeDet.id_classificazione,
           cd_cds_area = aModSpeDet.cd_cds_area,
           im_arr_a1_non_distr = round(aAssCdpPdgPRnd.im_arr_a1 - round(aAssCdpPdgPRnd.im_arr_a1,2),30),
           im_arr_a2_non_distr = round(aAssCdpPdgPRnd.im_arr_a2 - round(aAssCdpPdgPRnd.im_arr_a2,2),30),
           im_arr_a3_non_distr = round(aAssCdpPdgPRnd.im_arr_a3 - round(aAssCdpPdgPRnd.im_arr_a3,2),30),
                 utuv=aUser,
                 duva=aTSNow,
                 pg_ver_rec = pg_ver_rec + 1
              Where ESERCIZIO=aAssCdpPdgPRnd.esercizio
              And   CD_CDR_ROOT=aAssCdpPdgPRnd.cd_cdr_root
              And   TI_APPARTENENZA=aAssCdpPdgPRnd.ti_appartenenza
              And   TI_GESTIONE=aAssCdpPdgPRnd.ti_gestione
              And   CD_ELEMENTO_VOCE=aAssCdpPdgPRnd.cd_elemento_voce
              And   CD_CENTRO_RESPONSABILITA=aAssCdpPdgPRnd.cd_centro_responsabilita
        And   CD_LINEA_ATTIVITA=aAssCdpPdgPRnd.cd_linea_attivita
        And   ID_MATRICOLA=aAssCdpPdgPRnd.id_matricola;

              isDettRedistrRottiFound:=True;
    End If;
        End Loop;
      End If;

      If not isDettRedistrRottiFound Then
        IBMERR001.RAISE_ERR_GENERICO('Impossibile redistribuire i rotti per cdr RUO: '||aAssCdpPdgPRnd.cd_cdr_root||' es: '||aAssCdpPdgPRnd.esercizio||' voce del piano: '||aAssCdpPdgPRnd.cd_elemento_voce||' tipo rapporto: '||aAssCdpPdgPRnd.id_matricola);
      End if;
   End loop;
 End;

 Procedure annullaCDPSuPdgP(aEsercizio number, aCdCdr varchar2, aUser varchar2) is
   aTSNow date;
   aCDRRUO cdr%Rowtype;
   aUO unita_organizzativa%Rowtype;
   aNum number(8);
   aModuloTmp progetto_prev%Rowtype;
 Begin
   aTSNow:=sysdate;

   -- Leggo il CDR

   aCDRRUO:=CNRCTB020.GETCDRVALIDO(aEsercizio, aCdCdr);

   -- Leggo la UO del CDR
   aUO := CNRCTB020.getUO(aCDRRUO);

   if to_number(aCDRRUO.cd_proprio_cdr) != 0 then
     IBMERR001.RAISE_ERR_GENERICO('Operazione permessa solo su CDR di tipo RUO!');
   end if;

   Select count(*) into aNum from ass_cdp_pdgp
   Where esercizio = aEsercizio
   And   ti_costi_spese = CNRCTB061.TAB_COSTI
   And   cd_centro_responsabilita in (Select cd_centro_responsabilita from V_PDGP_CDR_RUO_NRUO
                                      Where esercizio = aEsercizio
                          And cd_cdr_root =  aCdCdr
                          And   (aUO.cd_tipo_unita != CNRCTB020.TIPO_SAC Or
                                 cd_unita_organizzativa = aUO.cd_unita_organizzativa));

   if aNum = 0 then
     IBMERR001.RAISE_ERR_GENERICO('Operazione di scarico non ancora effettuata per cdr '||aCdCdr);
   end if;

   -- Controllo modificabilita PDG CDR RUO/NRUO
   For aPDGP in PDGP_CON_CONFIG_SCR(aEsercizio, aCDRRUO.cd_centro_responsabilita, aUO) loop
     If not (aPDGP.stato in (
      CNRCTB051.STATO_PDGP_APERTURA,
      CNRCTB051.STATO_PDGP_ADEGUAMENTO)) Then

       -- Leggo il modulo per recuperare la descrizione
       Select * into aModuloTmp from progetto_prev
       Where pg_progetto = aPDGP.pg_progetto
       And   esercizio = aEsercizio;

       IBMERR001.RAISE_ERR_GENERICO('Il piano di gestione preliminare del CDR NRUO '||aPDGP.cd_centro_responsabilita||
                                    ' per il modulo '||aModuloTmp.cd_progetto||' ('||aModuloTmp.cd_progetto_sip||') non e'' in stato di apertura o di adeguamento!');
     End if;
   End loop;

   -- Eliminazione del dettaglio principale nel CDR di origine
   For aAssCdpPdgP In (Select * from ass_cdp_pdgp
                       Where esercizio = aEsercizio
                       And   ti_costi_spese = CNRCTB061.TAB_COSTI
                       And   cd_centro_responsabilita in (Select cd_centro_responsabilita from V_PDGP_CDR_RUO_NRUO
                                                          Where esercizio = aEsercizio
                                              And   cd_cdr_root =  aCdCdr
                                              And  (aUO.cd_tipo_unita != CNRCTB020.TIPO_SAC Or
                                                    cd_unita_organizzativa = aUO.cd_unita_organizzativa))) Loop
     -- Prima le spese
     Delete From pdg_modulo_spese
     Where esercizio = aAssCdpPdgP.esercizio
     And   cd_centro_responsabilita = aAssCdpPdgP.cd_centro_responsabilita
     And   pg_progetto = aAssCdpPdgP.pg_progetto_spese
     And   id_classificazione = aAssCdpPdgP.id_classificazione
     And   cd_cds_area = aAssCdpPdgP.cd_cds_area;

     Update pdg_modulo_costi
     Set IM_CF_TFR     = 0,
         IM_CF_TFR_DET = 0,
         utuv=aUser,
         duva=aTSNow,
         pg_ver_rec = pg_ver_rec + 1
     Where esercizio = aAssCdpPdgP.esercizio
     And   cd_centro_responsabilita = aAssCdpPdgP.cd_centro_responsabilita
     And   pg_progetto = aAssCdpPdgP.pg_progetto_costi;

     Delete ass_cdp_pdgp
     Where esercizio = aAssCdpPdgP.esercizio
     And   cd_centro_responsabilita = aAssCdpPdgP.cd_centro_responsabilita
     And   pg_progetto_costi = aAssCdpPdgP.pg_progetto_costi;

     Delete ass_cdp_pdgp_round
     Where esercizio = aAssCdpPdgP.esercizio
     And   cd_cdr_root = aAssCdpPdgP.cd_centro_responsabilita
     And   pg_progetto_costi = aAssCdpPdgP.pg_progetto_costi;
   End Loop;

   Update ass_cdp_la
   Set stato = STATO_CDP_NON_SCARICATO,
       dt_scarico = Null,
       utuv = aUser,
       duva = aTSNow,
       pg_ver_rec = pg_ver_rec + 1
   Where esercizio = aEsercizio
   And   mese = 0
   And   cd_centro_responsabilita In (Select cd_centro_responsabilita from V_PDGP_CDR_RUO_NRUO
                                      Where esercizio = aEsercizio
                          And   cd_cdr_root =  aCdCdr
                          And  (aUO.cd_tipo_unita != CNRCTB020.TIPO_SAC Or
                                cd_unita_organizzativa = aUO.cd_unita_organizzativa));
 End;

 Procedure ins_COSTO_DEL_DIPENDENTE (aDest COSTO_DEL_DIPENDENTE%rowtype) is
 Begin
   Insert into COSTO_DEL_DIPENDENTE (
     ESERCIZIO
    ,TI_PREV_CONS
    ,ID_MATRICOLA
    ,TI_APPARTENENZA
    ,TI_GESTIONE
    ,CD_ELEMENTO_VOCE
    ,CD_UNITA_ORGANIZZATIVA
    ,TI_RAPPORTO
    ,IM_A1
    ,IM_A2
    ,IM_A3
    ,IM_ONERI_CNR_A1
    ,IM_ONERI_CNR_A2
    ,IM_ONERI_CNR_A3
    ,IM_TFR_A1
    ,IM_TFR_A2
    ,IM_TFR_A3
    ,DT_SCARICO
    ,DACR
    ,UTCR
    ,UTUV
    ,DUVA
    ,PG_VER_REC
   ) values (
     aDest.ESERCIZIO
    ,aDest.TI_PREV_CONS
    ,aDest.ID_MATRICOLA
    ,aDest.TI_APPARTENENZA
    ,aDest.TI_GESTIONE
    ,aDest.CD_ELEMENTO_VOCE
    ,aDest.CD_UNITA_ORGANIZZATIVA
    ,aDest.TI_RAPPORTO
    ,aDest.IM_A1
    ,aDest.IM_A2
    ,aDest.IM_A3
    ,aDest.IM_ONERI_CNR_A1
    ,aDest.IM_ONERI_CNR_A2
    ,aDest.IM_ONERI_CNR_A3
    ,aDest.IM_TFR_A1
    ,aDest.IM_TFR_A2
    ,aDest.IM_TFR_A3
    ,aDest.DT_SCARICO
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.UTUV
    ,aDest.DUVA
    ,aDest.PG_VER_REC
    );
 End;

 Procedure scaricaCDPSuPdg(aEsercizio number, aCdCdr varchar2, aUser varchar2) is
   aCDRRUO cdr%Rowtype;
   aUO unita_organizzativa%Rowtype;

   aAssSpese ass_cdp_pdgp%Rowtype;

   Type typeModuloSpeseGest Is Table Of pdg_modulo_spese_gest%Rowtype Index By BINARY_INTEGER;
   tabModuloSpeseGest typeModuloSpeseGest;
   trovatoModuloSpeseGest Boolean := False;

   aDestS    pdg_modulo_spese_gest%Rowtype;

   aTSNow DATE;
   aDescrizione pdg_modulo_spese_gest.descrizione%Type := 'Inserimento automatico proveniente dallo scarico dei costi del personale.';
   aModuloTmp progetto_gest%Rowtype;

   aNum NUMBER(8);

   aVoce elemento_voce%Rowtype;
   aVoceTFR elemento_voce%Rowtype;
   aVoceTFRTEMPODET elemento_voce%Rowtype;
   aVoceONERICNR elemento_voce%Rowtype;
   aVoceONERICNRTEMPODET elemento_voce%Rowtype;
   aParCNR parametri_cnr%Rowtype;
   aCdClassificazione v_classificazione_voci_all.cd_classificazione%Type;
   aAllVociClassificazione v_classificazione_voci_all%Rowtype;
   aAllVociClassificazioneDef v_classificazione_voci_all%Rowtype;
   isTempoDeterminato Boolean;
   aPdgModuloSpese pdg_modulo_spese%Rowtype;
   aLATmp  v_linea_attivita_valida%Rowtype;
   aModulo progetto_gest%Rowtype;
   isDettRedistrRottiFound Boolean;
   isProcessato Boolean;
   isVoceTFR Boolean;
   aCDRPersonale cdr%Rowtype;
   esci exception;
 Begin
   aTSNow:=sysdate;

   -- Leggo il CDR
   aCDRRUO:=CNRCTB020.GETCDRVALIDO(aEsercizio, aCdCdr);

   -- Leggo la UO del CDR
   aUO := CNRCTB020.getUO(aCDRRUO);

   if to_number(aCDRRUO.cd_proprio_cdr) != 0 then
     IBMERR001.RAISE_ERR_GENERICO('Operazione permessa solo su cdr di tipo RUO!');
   end if;

   -- Leggo il CDR del personale

   aCDRPersonale:=CNRCTB020.GETCDRPERSONALE(aEsercizio);

   Select count(*) into aNum
   From ass_cdp_pdgp
   Where esercizio = aEsercizio
   And   ti_costi_spese = CNRCTB061.TAB_COSTI
   And   cd_centro_responsabilita in (Select cd_centro_responsabilita
                                      From V_PDGP_CDR_RUO_NRUO
                                      Where esercizio = aEsercizio
                          And   cd_cdr_root =  aCdCdr
                          And  (aUO.cd_tipo_unita != CNRCTB020.TIPO_SAC Or
                                cd_unita_organizzativa = aUO.cd_unita_organizzativa));

   If aNum = 0 then
     Select count(*) into aNum
     From V_CDP_SPACCATO_CDR_LA_VOCE
     Where esercizio = aEsercizio
     And mese = 0
     And cd_cdr_root In (Select cd_centro_responsabilita from V_PDGP_CDR_RUO_NRUO
                         Where esercizio = aEsercizio
                   And   cd_cdr_root =  aCdCdr
                   And  (aUO.cd_tipo_unita != CNRCTB020.TIPO_SAC Or
                   cd_unita_organizzativa = aUO.cd_unita_organizzativa));


     If aNum = 0 then
       --vuol dire che non ci sono costi da ripartire
       Raise esci;
     End If;
     IBMERR001.RAISE_ERR_GENERICO('Operazione di scarico sul PDGP non effettuata su cdr '||aCdCdr);
   End if;

   Select count(*) into aNum
   From ass_cdp_la
   Where esercizio = aEsercizio
   And   mese = 0
   And   cd_centro_responsabilita in (Select cd_centro_responsabilita
                                      From V_PDGP_CDR_RUO_NRUO
                                      Where esercizio = aEsercizio
                          And   cd_cdr_root =  aCdCdr
                          And  (aUO.cd_tipo_unita != CNRCTB020.TIPO_SAC Or
                                cd_unita_organizzativa = aUO.cd_unita_organizzativa))
   And   stato != STATO_CDP_PDGP_SCARICATO;

   If aNum != 0 then
     IBMERR001.RAISE_ERR_GENERICO('Operazione non effettuata. Esistono dettagli (ASS_CDP_LA) non ancora scaricati sul PDGP');
   Else
     Update ass_cdp_la
     Set stato = STATO_CDP_NON_SCARICATO
     Where esercizio = aEsercizio
     And   mese = 0
     And   cd_centro_responsabilita in (Select cd_centro_responsabilita
                                        From V_PDGP_CDR_RUO_NRUO
                                        Where esercizio = aEsercizio
                              And   cd_cdr_root =  aCdCdr
                            And  (aUO.cd_tipo_unita != CNRCTB020.TIPO_SAC Or
                                  cd_unita_organizzativa = aUO.cd_unita_organizzativa));
   End if;

   -- Carico la voce del piano TFR e ONERICNR per il personale a tempo indeterminato

   aVoceTFR:=CNRCTB000.GETVOCETFR(aEsercizio);
   aVoceONERICNR:=CNRCTB000.GETVOCEONERICNR(aEsercizio);

   -- Carico la voce del piano TFR e ONERICNR per il personale a tempo determinato

   aVoceTFRTEMPODET:=CNRCTB000.GETVOCETFRTEMPODET(aEsercizio);
   aVoceONERICNRTEMPODET:=CNRCTB000.GETVOCEONERICNRTEMPODET(aEsercizio);

   -- Leggo i parametri per conoscere il livello classificazione di inserimento

   Begin
     Select * into aParCNR from parametri_cnr
     Where esercizio = aEsercizio;
   Exception
     When No_Data_Found Then
        IBMERR001.RAISE_ERR_GENERICO('Mancano i parametri CNR per l''esercizio '||aEsercizio||
                                     'necessari per la determinazione del livello classificazione da utilizzare per '||
                                     'il caricamento del Pdg. Impossibile lo scarico.');
   End;

   -- Lock del PDG del CDR in processo
   CNRCTB051.LOCKPDGP(aEsercizio, aCdCdr);

   -- Start dello scarico su PDG dei costi del personale
   For aASSCDPLA In (
     Select * From V_CDP_SPACCATO_CDR_LA_VOCE Where
            esercizio = aEsercizio
      And mese = 0
      And cd_cdr_root In (Select cd_centro_responsabilita from V_PDGP_CDR_RUO_NRUO
                                      Where esercizio = aEsercizio
                          And   cd_cdr_root =  aCdCdr
                          And  (aUO.cd_tipo_unita != CNRCTB020.TIPO_SAC Or
                                cd_unita_organizzativa = aUO.cd_unita_organizzativa))
      Order by cd_cdr) loop  -- Cicla sulle configurazioni di scarico

     -- Determina il tipo di rapporto dalla tabella COSTO_DEL_DIPENDENTE

     isTempoDeterminato:=false;
     If aASSCDPLA.ti_rapporto = CDP_TI_RAPP_DETERMINATO Then
       isTempoDeterminato:=true;
     End if;
    --dbms_output.put_line(' aASSCDPLA '||aASSCDPLA.cd_linea_attivita);
    --dbms_output.put_line(' cd_cdr '||aASSCDPLA.cd_cdr);
     -- Leggo la linea di attivita per aggiornare il dettaglio del PDG con funzione e natura
     Begin
       Select * into aLATmp from v_linea_attivita_valida
       Where esercizio = aASSCDPLA.esercizio
       And   cd_linea_attivita = aASSCDPLA.cd_linea_attivita
       And   cd_centro_responsabilita = aASSCDPLA.cd_cdr;
     Exception
       When No_Data_Found Then
          IBMERR001.RAISE_ERR_GENERICO('E'' stato imputato, nella ripartizione dei costi del personale, un codice G.A.E. '||
 aASSCDPLA.cd_linea_attivita||' per il cdr '||aASSCDPLA.cd_cdr||' inesistente. Impossibile lo scarico.');
     End;

     If aLaTmp.cd_natura = '5' then
        IBMERR001.RAISE_ERR_GENERICO('Esiste una ripartizione dei CDP su G.A.E. di natura 5. Impossibile lo scarico.');
     End if;

     -- Leggo la voce per recuperare la classificazione
     Begin
       Select * into aVoce from elemento_voce
       Where esercizio = aASSCDPLA.esercizio
       And   ti_appartenenza = aASSCDPLA.ti_appartenenza
       And   ti_gestione = aASSCDPLA.ti_gestione
       And   cd_elemento_voce = aASSCDPLA.cd_elemento_voce;
     Exception
       When No_Data_Found Then
          IBMERR001.RAISE_ERR_GENERICO('E'' stata indicata, nei costi del personale, un codice elemento voce '||
                                       aASSCDPLA.cd_elemento_voce||' inesistente. Impossibile lo scarico.');
     End;
--dbms_output.put_line('  aVoce.id_classificazione '|| aVoce.id_classificazione);
     If aVoce.id_classificazione Is Null then
        IBMERR001.RAISE_ERR_GENERICO('Manca la classificazione sulla voce '||aVoce.cd_elemento_voce||' dell''esercizio '||aVoce.esercizio||'. Impossibile lo scarico.');
     End if;

     -- Leggo tutta la classificazione per reperire i suoi livelli precedenti
     Begin
       Select * into aAllVociClassificazione from v_classificazione_voci_all
       Where id_classificazione = aVoce.id_classificazione;
     Exception
       When No_Data_Found Then
          IBMERR001.RAISE_ERR_GENERICO('E'' stata indicata, nei costi del personale, un codice elemento voce '||
                                       aASSCDPLA.cd_elemento_voce||' con classificazione ('||aVoce.id_classificazione||
                                       ') non presente nella tabella corrispondente. Impossibile lo scarico.');
     End;

     aCdClassificazione := Null;
     -- Individuo la classificazione da utilizzare
     If aParCNR.LIVELLO_PDG_DECIS_SPE = 1 Then
       aCdClassificazione := aAllVociClassificazione.cd_liv1;
     ElsIf aParCNR.LIVELLO_PDG_DECIS_SPE = 2 Then
       aCdClassificazione := aAllVociClassificazione.cd_liv2;
     ElsIf aParCNR.LIVELLO_PDG_DECIS_SPE = 3 Then
       aCdClassificazione := aAllVociClassificazione.cd_liv3;
     ElsIf aParCNR.LIVELLO_PDG_DECIS_SPE = 4 Then
       aCdClassificazione := aAllVociClassificazione.cd_liv4;
     ElsIf aParCNR.LIVELLO_PDG_DECIS_SPE = 5 Then
       aCdClassificazione := aAllVociClassificazione.cd_liv5;
     ElsIf aParCNR.LIVELLO_PDG_DECIS_SPE = 6 Then
       aCdClassificazione := aAllVociClassificazione.cd_liv6;
     ElsIf aParCNR.LIVELLO_PDG_DECIS_SPE = 7 Then
       aCdClassificazione := aAllVociClassificazione.cd_liv7;
     End If;

     If aCdClassificazione Is Null Then
        IBMERR001.RAISE_ERR_GENERICO('E'' stata indicata, nei costi del personale, un codice elemento voce '||
                                     aASSCDPLA.cd_elemento_voce||' con classificazione ('||aVoce.id_classificazione||
                                     ') che ha generato anomalia nella ricerca del codice livello necessario per il '||
                                     'caricamento del Pdg. Impossibile lo scarico.'||aParCNR.LIVELLO_PDG_DECIS_SPE);
     End If;

     -- Leggo la classificazione finale da inserire in PDG_MODULO_SPESE
     Begin
       Select * into aAllVociClassificazioneDef from v_classificazione_voci_all
       Where esercizio = aASSCDPLA.esercizio
       And   ti_gestione = aASSCDPLA.ti_gestione
       And   cd_classificazione = aCdClassificazione;
     Exception
       When No_Data_Found Then
          IBMERR001.RAISE_ERR_GENERICO('E'' stata indicata, nei costi del personale, un codice elemento voce '||
                                       aASSCDPLA.cd_elemento_voce||' con classificazione ('||aVoce.id_classificazione||
                                       ') che ha generato anomalia nella ricerca del livello necessario per il '||
                                       'caricamento del PdgP. Impossibile lo scarico.');
     End;
   --  dbms_output.put_line('aASSCDPLA.cd_elemento_voce  '||aASSCDPLA.cd_elemento_voce );
     isVoceTFR:=False;
     If aASSCDPLA.cd_elemento_voce = aVoceTFR.cd_elemento_voce Or
        aASSCDPLA.cd_elemento_voce = aVoceTFRTEMPODET.cd_elemento_voce Then
        isVoceTFR:=True;
     End If;
   -- dbms_output.put_line('rec '||aCDRRUO.cd_centro_responsabilita||' '||aLATmp.pg_progetto||' '||aAllVociClassificazioneDef.id_classificazione||' '||aUO.cd_unita_padre||' '||aLATmp.cd_cofog);
-- Leggo pdg_modulo_spese per recuperare il pg_dettaglio di riferimento

       Select nvl(max(pg_dettaglio),1) into aPdgModuloSpese.pg_dettaglio from pdg_modulo_spese
        Where esercizio = aEsercizio
         And   cd_centro_responsabilita = aCDRRUO.cd_centro_responsabilita
         And   pg_progetto =  aLATmp.pg_progetto
         And   id_classificazione =aAllVociClassificazioneDef.id_classificazione
         And   cd_cds_area =  aUO.cd_unita_padre
         and   ((aParCNR.LIVELLO_PDG_COFOG != 0 and cd_cofog = aLATmp.cd_cofog) or
               (aParCNR.LIVELLO_PDG_COFOG = 0 and pg_dettaglio = 1))
         and   ((aParCNR.fl_nuovo_pdg= 'Y' and cd_missione = aLATmp.cd_missione) or
                (aParCNR.fl_nuovo_pdg = 'N' and pg_dettaglio = 1));

     If Not (isVoceTFR) Then
     --dbms_output.put_line('not '||aASSCDPLA.cd_elemento_voce);
        trovatoModuloSpeseGest := False;
        For i In 1..tabModuloSpeseGest.count Loop
          If tabModuloSpeseGest(i).ESERCIZIO = aEsercizio And
             tabModuloSpeseGest(i).CD_CENTRO_RESPONSABILITA = aCDRRUO.cd_centro_responsabilita And
             tabModuloSpeseGest(i).PG_PROGETTO = aLATmp.pg_progetto And
             tabModuloSpeseGest(i).ID_CLASSIFICAZIONE = aAllVociClassificazioneDef.id_classificazione And
             tabModuloSpeseGest(i).CD_CDS_AREA = aUO.cd_unita_padre And
             tabModuloSpeseGest(i).pg_dettaglio = aPdgModuloSpese.pg_dettaglio And
             tabModuloSpeseGest(i).CD_CDR_ASSEGNATARIO = aASSCDPLA.cd_cdr And
             tabModuloSpeseGest(i).CD_LINEA_ATTIVITA = aASSCDPLA.cd_linea_attivita And
             tabModuloSpeseGest(i).TI_APPARTENENZA = aASSCDPLA.ti_appartenenza And
             tabModuloSpeseGest(i).TI_GESTIONE = aASSCDPLA.ti_gestione And
             tabModuloSpeseGest(i).CD_ELEMENTO_VOCE = aASSCDPLA.cd_elemento_voce Then
             tabModuloSpeseGest(i).IM_SPESE_GEST_DECENTRATA_EST := Nvl(tabModuloSpeseGest(i).IM_SPESE_GEST_DECENTRATA_EST, 0) +
                                                               Nvl(aDestS.IM_SPESE_GEST_DECENTRATA_EST, 0);
             tabModuloSpeseGest(i).IM_SPESE_GEST_DECENTRATA_INT := Nvl(tabModuloSpeseGest(i).IM_SPESE_GEST_DECENTRATA_INT, 0) +
                                                               Nvl(aDestS.IM_SPESE_GEST_DECENTRATA_INT, 0);
             tabModuloSpeseGest(i).IM_SPESE_GEST_ACCENTRATA_EST := Nvl(tabModuloSpeseGest(i).IM_SPESE_GEST_ACCENTRATA_EST, 0) +
                                                               Nvl(aDestS.IM_SPESE_GEST_ACCENTRATA_EST, 0);
             tabModuloSpeseGest(i).IM_SPESE_GEST_ACCENTRATA_INT := Nvl(tabModuloSpeseGest(i).IM_SPESE_GEST_ACCENTRATA_INT, 0) +
                                                               Nvl(aDestS.IM_SPESE_GEST_ACCENTRATA_INT, 0);
             trovatoModuloSpeseGest := True;
             Exit;
          End If;
        End Loop;
        If Not trovatoModuloSpeseGest Then
          aDestS:=null;

          Select Nvl(Max(pg_dettaglio), 1) Into aDEstS.pg_dettaglio
          From pdg_modulo_spese
            Where  esercizio = aEsercizio
             And   cd_centro_responsabilita = aCDRRUO.cd_centro_responsabilita
             And   pg_progetto = aLATmp.pg_progetto
             And   id_classificazione = aAllVociClassificazioneDef.id_classificazione
             And   cd_cds_area = aUO.cd_unita_padre
             and (aParCNR.LIVELLO_PDG_COFOG != 0 and cd_cofog = aLATmp.cd_cofog)
             and (aParCNR.fl_nuovo_pdg= 'Y' and cd_missione = aLATmp.cd_missione);
          aDestS.ESERCIZIO:=aEsercizio;
          aDestS.CD_CENTRO_RESPONSABILITA:=aCDRRUO.cd_centro_responsabilita;
          aDestS.PG_PROGETTO:=aLATmp.pg_progetto;
          aDestS.ID_CLASSIFICAZIONE:=aAllVociClassificazioneDef.id_classificazione;
          aDestS.CD_CDS_AREA:=aUO.cd_unita_padre;

          aDestS.CD_CDR_ASSEGNATARIO := aASSCDPLA.cd_cdr;
          aDestS.CD_LINEA_ATTIVITA := aASSCDPLA.cd_linea_attivita;
          aDestS.TI_APPARTENENZA := aASSCDPLA.ti_appartenenza;
          aDestS.TI_GESTIONE := aASSCDPLA.ti_gestione;
          aDestS.CD_ELEMENTO_VOCE := aASSCDPLA.cd_elemento_voce;
          aDestS.DT_REGISTRAZIONE := aTSNow;
          aDestS.DESCRIZIONE := aDescrizione;
          aDestS.ORIGINE := CNRCTB061.ORIGINE_PREVISIONE;
          aDestS.CATEGORIA_DETTAGLIO := CNRCTB061.CATEGORIA_STIPENDI;
          aDestS.FL_SOLA_LETTURA := 'Y';

          CNRCTB051.resetCampiImporto(aDestS);

          setImporto(aASSCDPLA, aDestS);

          aDestS.DACR:=aTSNow;
          aDestS.UTCR:=aUser;
          aDestS.DUVA:=aTSNow;
          aDestS.UTUV:=aUser;
          aDestS.PG_VER_REC:=1;

          tabModuloSpeseGest(tabModuloSpeseGest.count+1):=aDestS;
        End If;
     End If;

     Update ass_cdp_la
     Set stato = STATO_CDP_SCARICATO,
         dt_scarico = aTSNow,
         utuv=aUser,
         duva=aTSNow,
         pg_ver_rec = pg_ver_rec + 1
     Where esercizio = aEsercizio
     And   mese = 0
     And   cd_linea_attivita = aASSCDPLA.cd_linea_attivita
     And   stato = STATO_CDP_NON_SCARICATO
     And   cd_centro_responsabilita In (Select cd_centro_responsabilita from V_PDGP_CDR_RUO_NRUO
                                        Where esercizio = aEsercizio
                            And   cd_cdr_root =  aCdCdr
                            And  (aUO.cd_tipo_unita != CNRCTB020.TIPO_SAC Or
                                  cd_unita_organizzativa = aUO.cd_unita_organizzativa));
   End Loop;

   --Carico la tabella PDG_MODULO_SPESE_GEST con i valori conservati nella PLSQLTABLE
   For i In 1..tabModuloSpeseGest.count Loop
     Begin
       -- Leggo il modulo_spese per inserire il dettaglio....se lo trovo sollevo una eccezione
       Select * into aDestS from pdg_modulo_spese_gest
       Where esercizio = tabModuloSpeseGest(i).esercizio
       And   cd_centro_responsabilita = tabModuloSpeseGest(i).cd_centro_responsabilita
       And   pg_progetto = tabModuloSpeseGest(i).pg_progetto
       And   id_classificazione = tabModuloSpeseGest(i).id_classificazione
       And   cd_cds_area = tabModuloSpeseGest(i).cd_cds_area
       And   pg_dettaglio = tabModuloSpeseGest(i).pg_dettaglio
       And   cd_cdr_assegnatario = tabModuloSpeseGest(i).cd_cdr_assegnatario
       And   cd_linea_attivita = tabModuloSpeseGest(i).cd_linea_attivita
       And   ti_appartenenza = tabModuloSpeseGest(i).ti_appartenenza
       And   ti_gestione = tabModuloSpeseGest(i).ti_gestione
       And   cd_elemento_voce = tabModuloSpeseGest(i).cd_elemento_voce;
      --dbms_output.put_line(tabModuloSpeseGest(i).pg_progetto||' '||tabModuloSpeseGest(i).pg_dettaglio||' '||aLATmp.cd_cofog||' gae '||tabModuloSpeseGest(i).cd_linea_attivita||' '||tabModuloSpeseGest(i).id_classificazione||' voce '||tabModuloSpeseGest(i).cd_elemento_voce);
       Begin
         Select * into aModulo from progetto_gest
         Where pg_progetto = tabModuloSpeseGest(i).pg_progetto
         And   esercizio = tabModuloSpeseGest(i).esercizio;
       Exception
         When No_Data_Found Then
           IBMERR001.RAISE_ERR_GENERICO('E'' stato imputato, nella ripartizione dei costi del personale, un codice G.A.E. '||
                                       ' al quale risulta associato un progressivo modulo ('||
                                       tabModuloSpeseGest(i).pg_progetto||') inesistente. Impossibile lo scarico.');
       End;

       Begin
         Select * into aAllVociClassificazioneDef from v_classificazione_voci_all
         Where esercizio = tabModuloSpeseGest(i).esercizio
         And   id_classificazione = tabModuloSpeseGest(i).id_classificazione;
       Exception
         When No_Data_Found Then
            IBMERR001.RAISE_ERR_GENERICO('E'' stata indicata, nei costi del personale, un codice elemento voce '||
                                         'con classificazione ('||tabModuloSpeseGest(i).id_classificazione||
                                         ') inesistente. Impossibile lo scarico.');
       End;

       IBMERR001.RAISE_ERR_GENERICO('Risulta gia'' caricato nel Pdg per il modulo '
                                    ||aModuloTmp.cd_progetto||' ('||aModuloTmp.cd_progetto_sip||') '||
                                    'associato al Cdr '||tabModuloSpeseGest(i).cd_centro_responsabilita||' e alla '||
                                    'classificazione '||aAllVociClassificazioneDef.cd_classificazione||
                                    ' un dettaglio sull''elemento voce '||tabModuloSpeseGest(i).cd_elemento_voce||
                                    ' per la G.a.e. '||tabModuloSpeseGest(i).cd_linea_attivita||'. '||
                                    'Impossibile lo scarico.');
     Exception
       When No_Data_Found Then
          Declare
            conta number;
            recPdgModuloSpese pdg_modulo_spese%rowtype;
            pLinea linea_attivita%rowtype;
          Begin
            select count(0) into conta
            from pdg_modulo_spese
            where esercizio = tabModuloSpeseGest(i).ESERCIZIO
            AND   CD_CENTRO_RESPONSABILITA = tabModuloSpeseGest(i).CD_CENTRO_RESPONSABILITA
            AND   PG_PROGETTO = tabModuloSpeseGest(i).PG_PROGETTO
            AND   ID_CLASSIFICAZIONE = tabModuloSpeseGest(i).ID_CLASSIFICAZIONE
            AND   CD_CDS_AREA = tabModuloSpeseGest(i).CD_CDS_AREA
            AND   PG_DETTAGLIO = tabModuloSpeseGest(i).PG_DETTAGLIO;

            If conta=0 Then
              If tabModuloSpeseGest(i).IM_SPESE_GEST_DECENTRATA_INT != 0 or tabModuloSpeseGest(i).IM_SPESE_GEST_DECENTRATA_EST != 0 or
                 tabModuloSpeseGest(i).IM_SPESE_GEST_ACCENTRATA_INT != 0 or tabModuloSpeseGest(i).IM_SPESE_GEST_ACCENTRATA_EST != 0 or
                 tabModuloSpeseGest(i).IM_PAGAMENTI != 0 Then
                 IBMERR001.RAISE_ERR_GENERICO('Non risulta presente nel bilancio previsionale una combinazione voce/gae richiesta '||
                 'invece nell''ambito del gestionale. Impossibile lo scarico. '||
                 '(CodErr: '||tabModuloSpeseGest(i).cd_cdr_assegnatario||'-'||tabModuloSpeseGest(i).id_classificazione||'-'||
                 tabModuloSpeseGest(i).pg_progetto||'-'||tabModuloSpeseGest(i).pg_dettaglio||')');
              End If;

              Select * into pLinea
              from linea_attivita
              where cd_centro_responsabilita = tabModuloSpeseGest(i).cd_cdr_assegnatario
              And   cd_linea_attivita = tabModuloSpeseGest(i).cd_linea_attivita;

              recPdgModuloSpese.esercizio := tabModuloSpeseGest(i).ESERCIZIO;
              recPdgModuloSpese.CD_CENTRO_RESPONSABILITA := tabModuloSpeseGest(i).CD_CENTRO_RESPONSABILITA;
              recPdgModuloSpese.PG_PROGETTO := tabModuloSpeseGest(i).PG_PROGETTO;
              recPdgModuloSpese.ID_CLASSIFICAZIONE := tabModuloSpeseGest(i).ID_CLASSIFICAZIONE;
              recPdgModuloSpese.CD_CDS_AREA := tabModuloSpeseGest(i).CD_CDS_AREA;
              recPdgModuloSpese.PG_DETTAGLIO := tabModuloSpeseGest(i).PG_DETTAGLIO;
              recPdgModuloSpese.CD_COFOG := pLinea.cd_cofog;
              recPdgModuloSpese.CD_MISSIONE := pLinea.CD_MISSIONE;
              recPdgModuloSpese.IM_SPESE_GEST_DECENTRATA_INT := 0;
              recPdgModuloSpese.IM_SPESE_GEST_DECENTRATA_EST := 0;
              recPdgModuloSpese.IM_SPESE_GEST_ACCENTRATA_INT := 0;
              recPdgModuloSpese.IM_SPESE_GEST_ACCENTRATA_EST := 0;
              recPdgModuloSpese.IM_SPESE_A2 := 0;
              recPdgModuloSpese.IM_SPESE_A3 := 0;
              recPdgModuloSpese.UTCR := tabModuloSpeseGest(i).UTCR;
              recPdgModuloSpese.DACR := tabModuloSpeseGest(i).DACR;
              recPdgModuloSpese.UTUV := tabModuloSpeseGest(i).UTUV;
              recPdgModuloSpese.DUVA := tabModuloSpeseGest(i).DUVA;
              recPdgModuloSpese.PG_VER_REC := tabModuloSpeseGest(i).PG_VER_REC;

              CNRCTB051.INS_PDG_MODULO_SPESE(recPdgModuloSpese);
            End If;
          End;
          Dbms_Output.put_line('inserisco -'||tabModuloSpeseGest(i).cd_cdr_assegnatario||' '||aLATmp.cd_cofog||' '||tabModuloSpeseGest(i).id_classificazione||' '||tabModuloSpeseGest(i).pg_progetto||' '||tabModuloSpeseGest(i).pg_dettaglio);
          CNRCTB051.INS_PDG_MODULO_SPESE_GEST(tabModuloSpeseGest(i));
     End;
   End Loop;

   -- Distribuzione dei rotti su dettagli creati
   For aAssCdpPdgPRnd In (Select * From ass_cdp_pdgp_round
                          Where esercizio = aEsercizio
              And   cd_cdr_root = aCDRRUO.cd_centro_responsabilita
              And   pg_progetto_spese Is Not Null
              For update nowait) loop  -- Cicla sulle configurazioni dei ROTTI

     isDettRedistrRottiFound:=false;

     -- Leggo la voce per recuperare la classificazione
     Begin
       Select * into aVoce from elemento_voce
       Where esercizio = aAssCdpPdgPRnd.esercizio
       And   ti_appartenenza = aAssCdpPdgPRnd.ti_appartenenza
       And   ti_gestione = aAssCdpPdgPRnd.ti_gestione
       And   cd_elemento_voce = aAssCdpPdgPRnd.cd_elemento_voce;
     Exception
       When No_Data_Found Then
          IBMERR001.RAISE_ERR_GENERICO('E'' stata indicata, nei costi del personale, un codice elemento voce '||
                                       aAssCdpPdgPRnd.cd_elemento_voce||' inesistente. Impossibile lo scarico.');
     End;

     If aVoce.id_classificazione Is Null then
        IBMERR001.RAISE_ERR_GENERICO('Manca la classificazione sulla voce '||aVoce.cd_elemento_voce||' dell''esercizio '||aVoce.esercizio||'. Impossibile lo scarico.');
     End if;

     -- Leggo tutta la classificazione per reperire i suoi livelli precedenti
     Begin
       Select * into aAllVociClassificazione from v_classificazione_voci_all
       Where id_classificazione = aVoce.id_classificazione;
     Exception
       When No_Data_Found Then
          IBMERR001.RAISE_ERR_GENERICO('E'' stata indicata, nei costi del personale, un codice elemento voce '||
                                       aAssCdpPdgPRnd.cd_elemento_voce||' con classificazione ('||aVoce.id_classificazione||
                                       ') non presente nella tabella corrispondente. Impossibile lo scarico.');
     End;

     aCdClassificazione := Null;
     -- Individuo la classificazione da utilizzare
     If aParCNR.LIVELLO_PDG_DECIS_SPE = 1 Then
       aCdClassificazione := aAllVociClassificazione.cd_liv1;
     ElsIf aParCNR.LIVELLO_PDG_DECIS_SPE = 2 Then
       aCdClassificazione := aAllVociClassificazione.cd_liv2;
     ElsIf aParCNR.LIVELLO_PDG_DECIS_SPE = 3 Then
       aCdClassificazione := aAllVociClassificazione.cd_liv3;
     ElsIf aParCNR.LIVELLO_PDG_DECIS_SPE = 4 Then
       aCdClassificazione := aAllVociClassificazione.cd_liv4;
     ElsIf aParCNR.LIVELLO_PDG_DECIS_SPE = 5 Then
       aCdClassificazione := aAllVociClassificazione.cd_liv5;
     ElsIf aParCNR.LIVELLO_PDG_DECIS_SPE = 6 Then
       aCdClassificazione := aAllVociClassificazione.cd_liv6;
     ElsIf aParCNR.LIVELLO_PDG_DECIS_SPE = 7 Then
       aCdClassificazione := aAllVociClassificazione.cd_liv7;
     End If;

     If aCdClassificazione Is Null Then
        IBMERR001.RAISE_ERR_GENERICO('E'' stata indicata, nei costi del personale, un codice elemento voce '||
                                     aAssCdpPdgPRnd.cd_elemento_voce||' con classificazione ('||aVoce.id_classificazione||
                                     ') che ha generato anomalia nella ricerca del codice livello necessario per il '||
                                     'caricamento del Pdg. Impossibile lo scarico.'||aParCNR.LIVELLO_PDG_DECIS_SPE);
     End If;

     -- Leggo la classificazione finale da utilizzare per cercare in PDG_MODULO_SPESE_GEST
     Begin
       Select * into aAllVociClassificazioneDef from v_classificazione_voci_all
       Where esercizio = aAssCdpPdgPRnd.esercizio
       And   ti_gestione = aAssCdpPdgPRnd.ti_gestione
       And   cd_classificazione = aCdClassificazione;
     Exception
       When No_Data_Found Then
          IBMERR001.RAISE_ERR_GENERICO('E'' stata indicata, nei costi del personale, un codice elemento voce '||
                                       aAssCdpPdgPRnd.cd_elemento_voce||' con classificazione ('||aVoce.id_classificazione||
                                       ') che ha generato anomalia nella ricerca del livello necessario per il '||
                                       'caricamento del Pdg. Impossibile lo scarico.');
     End;

     isVoceTFR:=False;
     If aAssCdpPdgPRnd.cd_elemento_voce = aVoceTFR.cd_elemento_voce Or
        aAssCdpPdgPRnd.cd_elemento_voce = aVoceTFRTEMPODET.cd_elemento_voce Then
        isVoceTFR:=True;
     End If;

     -- Leggo la linea di attivita per recuperare il modulo
     Begin
       Select * into aLATmp from v_linea_attivita_valida
       Where esercizio = aAssCdpPdgPRnd.esercizio
       And   cd_linea_attivita = aAssCdpPdgPRnd.cd_linea_attivita
       And   cd_centro_responsabilita = aAssCdpPdgPRnd.cd_centro_responsabilita;
     Exception
       When No_Data_Found Then
          IBMERR001.RAISE_ERR_GENERICO('E'' stato imputato, nella ripartizione dei costi del personale, un codice G.A.E. '||
 aAssCdpPdgPRnd.cd_linea_attivita||' per il cdr '||aAssCdpPdgPRnd.cd_centro_responsabilita||' inesistente. Impossibile lo scarico.');
     End;

     If Not isVoceTFR Then
        For aModSpeDet In (Select p.* From pdg_modulo_spese_gest p
                           Where esercizio = aAssCdpPdgPRnd.esercizio
                           And   cd_centro_responsabilita = aAssCdpPdgPRnd.cd_cdr_root
                           And   pg_progetto = aAssCdpPdgPRnd.pg_progetto_spese
                           And   id_classificazione = aAssCdpPdgPRnd.id_classificazione
                           And   cd_cds_area = aAssCdpPdgPRnd.cd_cds_area
                           And   cd_cdr_assegnatario = aAssCdpPdgPRnd.cd_centro_responsabilita
                           And   cd_linea_attivita = aAssCdpPdgPRnd.cd_linea_attivita
                           And   ti_appartenenza = aAssCdpPdgPRnd.ti_appartenenza
                           And   ti_gestione = aAssCdpPdgPRnd.ti_gestione
                           And   cd_elemento_voce = aAssCdpPdgPRnd.cd_elemento_voce) Loop

           isProcessato:=False;

           If Not isProcessato And
              aModSpeDet.cd_centro_responsabilita=aCDRPersonale.cd_centro_responsabilita And
              (aLATmp.cd_natura = '2' Or aLATmp.cd_natura = '3') Then
              If (aModSpeDet.IM_SPESE_GEST_DECENTRATA_EST=0 and aAssCdpPdgPRnd.im_arr_a1 <> 0) Or
                  aModSpeDet.IM_SPESE_GEST_DECENTRATA_EST+aAssCdpPdgPRnd.im_arr_a1 < 0 Then
                  Null;
              Else
                 Update pdg_modulo_spese_gest
                 Set IM_SPESE_GEST_DECENTRATA_EST=IM_SPESE_GEST_DECENTRATA_EST+aAssCdpPdgPRnd.im_arr_a1,
                     utuv=aUser,
                     duva=aTSNow,
                     pg_ver_rec = pg_ver_rec + 1
                 Where esercizio = aAssCdpPdgPRnd.esercizio
                 And   cd_centro_responsabilita = aAssCdpPdgPRnd.cd_cdr_root
                 And   pg_progetto = aAssCdpPdgPRnd.pg_progetto_spese
                 And   id_classificazione = aAssCdpPdgPRnd.id_classificazione
                 And   cd_cds_area = aAssCdpPdgPRnd.cd_cds_area
                 And   cd_cdr_assegnatario = aAssCdpPdgPRnd.cd_centro_responsabilita
                 And   cd_linea_attivita = aAssCdpPdgPRnd.cd_linea_attivita
                 And   ti_appartenenza = aAssCdpPdgPRnd.ti_appartenenza
                 And   ti_gestione = aAssCdpPdgPRnd.ti_gestione
                 And   cd_elemento_voce = aAssCdpPdgPRnd.cd_elemento_voce;

                 isProcessato:=True;
              End If;
           End If;

           If Not isProcessato And
              aModSpeDet.cd_centro_responsabilita=aCDRPersonale.cd_centro_responsabilita And
              Not (aLATmp.cd_natura = '2' Or aLATmp.cd_natura = '3') Then
              If (aModSpeDet.IM_SPESE_GEST_DECENTRATA_INT=0 and aAssCdpPdgPRnd.im_arr_a1 <> 0) Or
                  aModSpeDet.IM_SPESE_GEST_DECENTRATA_INT+aAssCdpPdgPRnd.im_arr_a1 < 0 Then
                  Null;
              Else
                 Update pdg_modulo_spese_gest
                 Set IM_SPESE_GEST_DECENTRATA_INT=IM_SPESE_GEST_DECENTRATA_INT+aAssCdpPdgPRnd.im_arr_a1,
                     utuv=aUser,
                     duva=aTSNow,
                     pg_ver_rec = pg_ver_rec + 1
                 Where esercizio = aAssCdpPdgPRnd.esercizio
                 And   cd_centro_responsabilita = aAssCdpPdgPRnd.cd_cdr_root
                 And   pg_progetto = aAssCdpPdgPRnd.pg_progetto_spese
                 And   id_classificazione = aAssCdpPdgPRnd.id_classificazione
                 And   cd_cds_area = aAssCdpPdgPRnd.cd_cds_area
                 And   cd_cdr_assegnatario = aAssCdpPdgPRnd.cd_centro_responsabilita
                 And   cd_linea_attivita = aAssCdpPdgPRnd.cd_linea_attivita
                 And   ti_appartenenza = aAssCdpPdgPRnd.ti_appartenenza
                 And   ti_gestione = aAssCdpPdgPRnd.ti_gestione
                 And   cd_elemento_voce = aAssCdpPdgPRnd.cd_elemento_voce;

                 isProcessato:=True;
              End If;
           End If;

           If Not isProcessato And
              Not aModSpeDet.cd_centro_responsabilita=aCDRPersonale.cd_centro_responsabilita And
              (aLATmp.cd_natura = '2' Or aLATmp.cd_natura = '3') Then
              If (aModSpeDet.IM_SPESE_GEST_ACCENTRATA_EST=0 and aAssCdpPdgPRnd.im_arr_a1 <> 0) Or
                  aModSpeDet.IM_SPESE_GEST_ACCENTRATA_EST+aAssCdpPdgPRnd.im_arr_a1 < 0 Then
                  Null;
              Else
                 Update pdg_modulo_spese_gest
                 Set IM_SPESE_GEST_ACCENTRATA_EST=IM_SPESE_GEST_ACCENTRATA_EST+aAssCdpPdgPRnd.im_arr_a1,
                     utuv=aUser,
                     duva=aTSNow,
                     pg_ver_rec = pg_ver_rec + 1
                 Where esercizio = aAssCdpPdgPRnd.esercizio
                 And   cd_centro_responsabilita = aAssCdpPdgPRnd.cd_cdr_root
                 And   pg_progetto = aAssCdpPdgPRnd.pg_progetto_spese
                 And   id_classificazione = aAssCdpPdgPRnd.id_classificazione
                 And   cd_cds_area = aAssCdpPdgPRnd.cd_cds_area
                 And   cd_cdr_assegnatario = aAssCdpPdgPRnd.cd_centro_responsabilita
                 And   cd_linea_attivita = aAssCdpPdgPRnd.cd_linea_attivita
                 And   ti_appartenenza = aAssCdpPdgPRnd.ti_appartenenza
                 And   ti_gestione = aAssCdpPdgPRnd.ti_gestione
                 And   cd_elemento_voce = aAssCdpPdgPRnd.cd_elemento_voce;

                 isProcessato:=True;
              End If;
           End If;

           If Not isProcessato And
              Not aModSpeDet.cd_centro_responsabilita=aCDRPersonale.cd_centro_responsabilita And
              Not (aLATmp.cd_natura = '2' Or aLATmp.cd_natura = '3') Then
              If (aModSpeDet.IM_SPESE_GEST_ACCENTRATA_INT=0 and aAssCdpPdgPRnd.im_arr_a1 <> 0) Or
                  aModSpeDet.IM_SPESE_GEST_ACCENTRATA_INT+aAssCdpPdgPRnd.im_arr_a1 < 0 Then
                  Null;
              Else
                 Update pdg_modulo_spese_gest
                 Set IM_SPESE_GEST_ACCENTRATA_INT=IM_SPESE_GEST_ACCENTRATA_INT+aAssCdpPdgPRnd.im_arr_a1,
                     utuv=aUser,
                     duva=aTSNow,
                     pg_ver_rec = pg_ver_rec + 1
                 Where esercizio = aAssCdpPdgPRnd.esercizio
                 And   cd_centro_responsabilita = aAssCdpPdgPRnd.cd_cdr_root
                 And   pg_progetto = aAssCdpPdgPRnd.pg_progetto_spese
                 And   id_classificazione = aAssCdpPdgPRnd.id_classificazione
                 And   cd_cds_area = aAssCdpPdgPRnd.cd_cds_area
                 And   cd_cdr_assegnatario = aAssCdpPdgPRnd.cd_centro_responsabilita
                 And   cd_linea_attivita = aAssCdpPdgPRnd.cd_linea_attivita
                 And   ti_appartenenza = aAssCdpPdgPRnd.ti_appartenenza
                 And   ti_gestione = aAssCdpPdgPRnd.ti_gestione
                 And   cd_elemento_voce = aAssCdpPdgPRnd.cd_elemento_voce;

                 isProcessato:=True;
              End If;
           End If;
           If isProcessato Then
              isDettRedistrRottiFound:=True;
     End If;
        End Loop;

        If not isDettRedistrRottiFound Then
          IBMERR001.RAISE_ERR_GENERICO('Impossibile redistribuire i rotti per cdr RUO: '||aAssCdpPdgPRnd.cd_cdr_root||' es: '||aAssCdpPdgPRnd.esercizio||' voce del piano: '||aAssCdpPdgPRnd.cd_elemento_voce||' tipo rapporto: '||aAssCdpPdgPRnd.id_matricola);
        End if;
     End If;
   End loop;

   Select count(*) into aNum
   From ass_cdp_la
   Where esercizio = aEsercizio
   And   mese = 0
   And   cd_centro_responsabilita in (Select cd_centro_responsabilita
                                      From V_PDGP_CDR_RUO_NRUO
                                      Where esercizio = aEsercizio
                          And   cd_cdr_root =  aCdCdr
                          And  (aUO.cd_tipo_unita != CNRCTB020.TIPO_SAC Or
                                cd_unita_organizzativa = aUO.cd_unita_organizzativa))
   And   Stato != STATO_CDP_SCARICATO;

   If aNum != 0 then
     IBMERR001.RAISE_ERR_GENERICO('Operazione non effettuata. Esistono dettagli (ASS_CDP_LA) che non sono stati scarictai al termine del processo');
   End if;
 Exception
   When esci then null;
 End;

 Procedure annullaCDPSuPdg(aEsercizio number, aCdCdr varchar2, aUser varchar2) is
   aCDRRUO cdr%Rowtype;
   aUO unita_organizzativa%Rowtype;
   aNum number(8);
   esci exception;
 Begin
   -- Leggo il CDR

   aCDRRUO:=CNRCTB020.GETCDRVALIDO(aEsercizio, aCdCdr);

   -- Leggo la UO del CDR
   aUO := CNRCTB020.getUO(aCDRRUO);

   if to_number(aCDRRUO.cd_proprio_cdr) != 0 then
     IBMERR001.RAISE_ERR_GENERICO('Operazione permessa solo su CDR di tipo RUO!');
   end if;

   Select count(*) into aNum from pdg_modulo_spese_gest
   Where esercizio = aEsercizio
   And   origine = CNRCTB061.ORIGINE_PREVISIONE
   And   categoria_dettaglio = CNRCTB061.CATEGORIA_STIPENDI
   And   cd_centro_responsabilita in (Select cd_centro_responsabilita from V_PDGP_CDR_RUO_NRUO
                                      Where esercizio = aEsercizio
                          And   cd_cdr_root =  aCdCdr
                          And  (aUO.cd_tipo_unita != CNRCTB020.TIPO_SAC Or
                                cd_unita_organizzativa = aUO.cd_unita_organizzativa));

   if aNum = 0 then
     Select count(*) into aNum
     From V_CDP_SPACCATO_CDR_LA_VOCE
     Where esercizio = aEsercizio
     And mese = 0
     And cd_cdr_root In (Select cd_centro_responsabilita from V_PDGP_CDR_RUO_NRUO
                         Where esercizio = aEsercizio
                 And   cd_cdr_root =  aCdCdr
             And  (aUO.cd_tipo_unita != CNRCTB020.TIPO_SAC Or
                   cd_unita_organizzativa = aUO.cd_unita_organizzativa));

     If aNum = 0 then
       --vuol dire che non ci sono costi da ripartire
       raise esci;
     End If;
     IBMERR001.RAISE_ERR_GENERICO('Operazione di scarico su Pdg non ancora effettuata per cdr '||aCdCdr);
   end if;

   Select count(*) into aNum
   From ass_cdp_la
   Where esercizio = aEsercizio
   And   mese = 0
   And   cd_centro_responsabilita in (Select cd_centro_responsabilita
                                      From V_PDGP_CDR_RUO_NRUO
                                      Where esercizio = aEsercizio
                          And   cd_cdr_root =  aCdCdr
                          And  (aUO.cd_tipo_unita != CNRCTB020.TIPO_SAC Or
                                cd_unita_organizzativa = aUO.cd_unita_organizzativa))
   And   stato != STATO_CDP_SCARICATO;

   If aNum != 0 then
     IBMERR001.RAISE_ERR_GENERICO('Operazione non effettuata. Esistono dettagli (ASS_CDP_LA) che risultano non ancora scaricati sul PDG');
   Else
     Update ass_cdp_la
     Set stato = STATO_CDP_PDGP_SCARICATO
     Where esercizio = aEsercizio
     And   mese = 0
     And   cd_centro_responsabilita in (Select cd_centro_responsabilita
                                        From V_PDGP_CDR_RUO_NRUO
                                        Where esercizio = aEsercizio
                              And   cd_cdr_root =  aCdCdr
                              And  (aUO.cd_tipo_unita != CNRCTB020.TIPO_SAC Or
                                  cd_unita_organizzativa = aUO.cd_unita_organizzativa));
   End if;

   Delete pdg_modulo_spese_gest
   Where esercizio = aEsercizio
   And   origine = CNRCTB061.ORIGINE_PREVISIONE
   And   categoria_dettaglio = CNRCTB061.CATEGORIA_STIPENDI
   And   cd_centro_responsabilita in (Select cd_centro_responsabilita from V_PDGP_CDR_RUO_NRUO
                                      Where esercizio = aEsercizio
                          And cd_cdr_root =  aCdCdr
                          And  (aUO.cd_tipo_unita != CNRCTB020.TIPO_SAC Or
                                cd_unita_organizzativa = aUO.cd_unita_organizzativa));
 Exception
   When esci then null;
 End;
End;
