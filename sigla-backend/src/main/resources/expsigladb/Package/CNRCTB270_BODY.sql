--------------------------------------------------------
--  DDL for Package Body CNRCTB270
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "CNRCTB270" AS

 FUNCTION getDescLiquidazione
   (aLiquidIva LIQUIDAZIONE_IVA%ROWTYPE)
   RETURN VARCHAR2 IS
   aDescrizione VARCHAR2(500);
   aTipologia VARCHAR2(100);

 BEGIN

   aDescrizione:='LIQUIDAZIONE IVA ';

   IF    aLiquidIva.tipo_liquidazione = CNRCTB250.TI_LIQ_IVA_COMMERC THEN
         aTipologia:='ATTIVITA''COMMERCIALI ';
   ELSIF aLiquidIva.tipo_liquidazione = CNRCTB250.TI_LIQ_IVA_ISTINTR THEN
         aTipologia:='ATTIVITA'' ISTITUZIONALI BENI INTRA_UE ';
   ELSIF aLiquidIva.tipo_liquidazione = CNRCTB250.TI_LIQ_IVA_ISTSMSI THEN
         aTipologia:='ATTIVITA'' ISTITUZIONALI SAN MARINO SENZA IVA ';
   ELSIF aLiquidIva.tipo_liquidazione = CNRCTB250.TI_LIQ_IVA_ISTSNR Then
         aTipologia:='ATTIVITA'' ISTITUZIONALI SERVIZI NON RESIDENTI ';
   ELSIF aLiquidIva.tipo_liquidazione = CNRCTB250.TI_LIQ_IVA_ISTSPLIT Then
         aTipologia:='ATTIVITA'' ISTITUZIONALI SPLIT PAYMENT ';
   END IF;

   aDescrizione:=aDescrizione || aTipologia ||
                 'CDS: ' || aLiquidIva.cd_cds || ' UO: ' || aLiquidIva.cd_unita_organizzativa || ' '  ||
                 'PERIODO: '||to_char(aLiquidIva.dt_inizio,'DD/MM/YYYY') || ' A ' ||
                 to_char(aLiquidIva.dt_fine,'DD/MM/YYYY');
  RETURN aDescrizione;

 END getDescLiquidazione;

 Function getCDRUOVERSIVA(aEs INTEGER)
 Return v_cdr_valido%Rowtype
 Is
   aUOVERSIVA unita_organizzativa%Rowtype;
   aCDRUOVERSIVA v_cdr_valido%Rowtype;
 Begin
   -- Estrazione dell'UO di versamento IVA
   aUOVERSIVA:=CNRCTB020.getUOVersIVA(aEs);

   If aUOVERSIVA.cd_unita_organizzativa Is Null Then
     IBMERR001.RAISE_ERR_GENERICO('Uo Versamento Iva non presente nella tabella di configurazione CNR.');
   End If;

   Select * Into aCDRUOVERSIVA
   From v_cdr_valido
   Where esercizio = aEs
   And   cd_unita_organizzativa = aUOVERSIVA.cd_unita_organizzativa
   And   To_Number(cd_proprio_cdr)=0;

   Return aCDRUOVERSIVA;
 Exception
   When NO_DATA_FOUND then
     IBMERR001.RAISE_ERR_GENERICO('CDR di responsabile dell''UO '||aUOVERSIVA.cd_unita_organizzativa||' non trovato');
 End getCDRUOVERSIVA;

 Function getLASPECVERSIVA(aEs INTEGER, aCDRUO cdr%Rowtype)
 Return v_linea_attivita_valida%Rowtype
 Is
   codLASpecVersIva linea_attivita.cd_linea_attivita%Type;
   aLASpecVersIva v_linea_attivita_valida%Rowtype;
 Begin
   -- Estrazione della linea di attività  comune da utilizzare per la creazione della variazione
   codLASpecVersIva := CNRCTB015.GETVAL01PERCHIAVE(LINEA_ATTIVITA_SPECIALE,LA_VER_IVA);

   If codLASpecVersIva Is Null Then
     IBMERR001.RAISE_ERR_GENERICO('Codice Gae Versamento Iva non presente nella tabella di configurazione CNR.');
   End If;

   Select a.* Into aLASpecVersIva
   From v_linea_attivita_valida a
   Where a.esercizio = aEs
   And   a.cd_centro_responsabilita = aCDRUO.cd_centro_responsabilita
   And   a.cd_linea_attivita = codLASpecVersIva
   And   a.ti_gestione = CNRCTB001.GESTIONE_SPESE
   And   Exists (Select 1 From tipo_linea_attivita b
                 Where b.cd_tipo_linea_attivita = a.cd_tipo_linea_attivita
                 And   b.ti_tipo_la = CNRCTB010.TI_TIPO_LA_COMUNE);

   Return aLASpecVersIva;
 Exception
   When NO_DATA_FOUND Then
     IBMERR001.RAISE_ERR_GENERICO('Linea di attività comune per '||cnrutil.getLabelObbligazioneMin()||' versamento IVA non trovata ('||
aCDRUO.cd_centro_responsabilita||'/'||codLASpecVersIva||') per l''esercizio '||to_char(aEs));
 End getLASPECVERSIVA;

 Function getLASPECVERSIVASAC(aEs INTEGER, aCDRUOVERSIVA v_cdr_valido%Rowtype)
 Return v_linea_attivita_valida%Rowtype
 Is
   codLASpecVersIvaSAC linea_attivita.cd_linea_attivita%Type;
   aLASpecVersIvaSac v_linea_attivita_valida%Rowtype;
 Begin
   -- Estrazione della linea di attività  comune da utilizzare per la creazione della variazione
   codLASpecVersIvaSAC := CNRCTB015.GETVAL01PERCHIAVE(aEs,LINEA_ATTIVITA_SPECIALE,LA_VER_IVA_SAC);

   If codLASpecVersIvaSAC Is Null Then
     IBMERR001.RAISE_ERR_GENERICO('Codice Gae Versamento Iva per SAC non presente nella tabella di configurazione CNR.');
   End If;

   Select a.* into aLASpecVersIvaSac
   From v_linea_attivita_valida a
   Where a.esercizio = aEs
   And   a.cd_centro_responsabilita = aCDRUOVERSIVA.cd_centro_responsabilita
   And   a.cd_linea_attivita = codLASpecVersIvaSAC
   And   a.ti_gestione = CNRCTB001.GESTIONE_SPESE;

   Return aLASpecVersIvaSac;
 Exception
   When NO_DATA_FOUND Then
     IBMERR001.RAISE_ERR_GENERICO('Linea di attività per '||cnrutil.getLabelObbligazioneMin()||' versamento IVA non trovata ('||
aCDRUOVERSIVA.cd_centro_responsabilita||'/'||codLASpecVersIvaSAC||') per l''esercizio '||to_char(aEs));
 End getLASPECVERSIVASAC;

 Function getVoceIvaCommerciale(aEs INTEGER)
 Return elemento_voce%Rowtype
 Is
   aEV elemento_voce%Rowtype;
 Begin
   Select * into aEV
   From elemento_voce
   Where  esercizio = aEs
   And    ti_appartenenza = CNRCTB001.APPARTENENZA_CDS
   And    ti_gestione = CNRCTB001.GESTIONE_SPESE
   And    ti_elemento_voce = CNRCTB001.CAPITOLO
   And    cd_elemento_voce = CNRCTB015.GETVAL01PERCHIAVE(aEs,ELEMENTO_VOCE_SPECIALE,VOCE_VERSAMENTO_IVA);

   Return aEV;
 Exception
   When NO_DATA_FOUND Then
     IBMERR001.RAISE_ERR_GENERICO('Voce del piano da utilizzare per il versamento dell''IVA non trovata');
 End getVoceIvaCommerciale;

 Function getVoceIvaIstituzionale(aEs INTEGER, nuovoPdg VARCHAR2, tipo_liquid VARCHAR2)
 Return elemento_voce%Rowtype
 Is
   aAssPgiro ass_partita_giro%Rowtype;
   aEV elemento_voce%Rowtype;
 Begin
   Begin
     Select * into aAssPgiro
     From ass_partita_giro
     Where esercizio = aEs
     And   ti_gestione = CNRCTB001.GESTIONE_ENTRATE
     And   ((nuovoPdg = 'N' And ti_appartenenza = CNRCTB001.APPARTENENZA_CDS) Or
            (nuovoPdg = 'Y' And ti_appartenenza = CNRCTB001.APPARTENENZA_CNR))
     And   ((cd_voce = CNRCTB015.GETVAL01PERCHIAVE(aEs,ELEMENTO_VOCE_SPECIALE,VOCE_IVA_FATTURA_ESTERA) and
      			tipo_liquid !=CNRCTB250.TI_LIQ_IVA_ISTSPLIT ) or
      			(cd_voce = CNRCTB015.GETVAL02PERCHIAVE(aEs,ELEMENTO_VOCE_SPECIALE,VOCE_IVA_FATTURA_ESTERA) and
      			tipo_liquid = CNRCTB250.TI_LIQ_IVA_ISTSPLIT));
   Exception
     When NO_DATA_FOUND then
       IBMERR001.RAISE_ERR_GENERICO('Conto finanziario di spesa di partita di giro per versamento IVA locale non trovato');
   End;

   Begin
     Select * into aEV
     From elemento_voce
     Where esercizio = aEs
     And   ti_appartenenza = CNRCTB001.APPARTENENZA_CDS
     And   ti_gestione = CNRCTB001.GESTIONE_SPESE
     And   ti_elemento_voce = CNRCTB001.CAPITOLO
     And   cd_elemento_voce = aAssPgiro.cd_voce_clg;
   Exception
     When NO_DATA_FOUND then
       IBMERR001.RAISE_ERR_GENERICO('Voce del piano da utilizzare per il versamento dell''IVA non trovata');
   End;
   Return aEV;
 End getVoceIvaIstituzionale;

 Function getVoceIvaIstituzionaleEnte(aEs INTEGER, aCdCoriIva VARCHAR2, nuovoPdg VARCHAR2)
 Return elemento_voce%Rowtype
 Is
   aAssCoriFin ass_tipo_cori_ev%Rowtype;
   aAssPgiro ass_partita_giro%Rowtype;
   aEV elemento_voce%Rowtype;
 Begin
   Begin
     Select * into aAssCoriFin
     From ass_tipo_cori_ev
     Where cd_contributo_ritenuta = aCdCoriIva
     And   esercizio = aEs
     And   ti_gestione = CNRCTB001.GESTIONE_ENTRATE
     And   ((nuovoPdg = 'N' And ti_appartenenza = CNRCTB001.APPARTENENZA_CDS) Or
            (nuovoPdg = 'Y' And ti_appartenenza = CNRCTB001.APPARTENENZA_CNR))
     And ti_ente_percepiente = 'E';
   Exception
     When NO_DATA_FOUND Then
       IBMERR001.RAISE_ERR_GENERICO('Conto finanziario di partita di giro per versamento IVA non trovato');
   End;

   Begin
     Select * Into aAssPgiro
     From ass_partita_giro
     Where esercizio = aAssCoriFin.esercizio
     And   ti_gestione = aAssCoriFin.ti_gestione
     And   ti_appartenenza = aAssCoriFin.ti_appartenenza
     And   cd_voce = aAssCoriFin.cd_elemento_voce;
   Exception
     When NO_DATA_FOUND Then
       IBMERR001.RAISE_ERR_GENERICO('Conto finanziario di spesa di partita di giro per versamento IVA centro non trovato');
   End;

   Begin
     Select * into aEV
     From elemento_voce
     Where esercizio = aEs
     And   ti_appartenenza = CNRCTB001.APPARTENENZA_CDS
     And   ti_gestione = CNRCTB001.GESTIONE_SPESE
     And   ti_elemento_voce = CNRCTB001.CAPITOLO
     And   cd_elemento_voce = aAssPgiro.cd_voce_clg;
   Exception when NO_DATA_FOUND then
     IBMERR001.RAISE_ERR_GENERICO('Voce del piano da utilizzare per il versamento dell''IVA non trovata');
   End;
   Return aEV;
 End getVoceIvaIstituzionaleEnte;

 Procedure generaVariazioneBilancio
   (aCdCds VARCHAR2,
    aEs NUMBER,
    aCdUo VARCHAR2,
    aEV elemento_voce%Rowtype,
    aLiquidIva LIQUIDAZIONE_IVA%ROWTYPE,
    aTSNow date,
    aUser VARCHAR2) Is

   aCDRUO cdr%rowtype;
   aCDRUOVERSIVA v_cdr_valido%rowtype;
   aLASpecVersIva v_linea_attivita_valida%Rowtype;
   aLASpecVersIvaSac v_linea_attivita_valida%Rowtype;

   aIvaResiduaDaVersare  number :=0;
   pRigaLiquidVariazione number := 0;

   aEsDefault number;

   Function getSaldi(aEs NUMBER, aEsRes NUMBER, aCdCdr VARCHAR2, aCdGae VARCHAR2, aTiApp VARCHAR2, aTiGes VARCHAR2, aCdVoce VARCHAR2)
   Return Voce_f_saldi_cdr_linea%Rowtype
   Is
     recSaldi voce_f_saldi_cdr_linea%Rowtype;
   Begin
     Begin
       Select * Into recSaldi
       From voce_f_saldi_cdr_linea
       Where esercizio = aEs
       And   esercizio_res = aEsRes
       And   cd_centro_responsabilita = aCdCdr
       And   cd_linea_attivita = aCdGae
       And   ti_appartenenza = aTiApp
       And   ti_gestione = aTiGes
       And   cd_elemento_voce = aCdVoce
       And   cd_voce = aCdVoce
       for update nowait;
     Exception
       When No_Data_Found Then
         null;
     End;
     Return recSaldi;
   End;

   Function getDisp(aEs NUMBER, aEsRes NUMBER, aCdCdr VARCHAR2, aCdGae VARCHAR2, aTiApp VARCHAR2, aTiGes VARCHAR2, aCdVoce VARCHAR2) Return Number
   Is
     p_impdisp voce_f_saldi_cdr_linea.IM_STANZ_INIZIALE_A1%type := 0;
   Begin
     Begin
       Select importo_disponibile Into p_impdisp
       From v_assestato
       Where esercizio = aEs
       And   esercizio_res = aEsRes
       And   cd_centro_responsabilita = aCdCdr
       And   cd_linea_attivita = aCdGae
       And   ti_appartenenza = aTiApp
       And   ti_gestione = aTiGes
       And   cd_elemento_voce = aCdVoce
       And   cd_voce = aCdVoce;
     Exception
       When No_Data_Found Then
         p_impdisp := 0;
     End;
     Return p_impdisp;
   End;

   Function getOrCreateSaldi(aEs NUMBER, aEsRes NUMBER, aCdCdr VARCHAR2, aCdGae VARCHAR2, aTiApp VARCHAR2, aTiGes VARCHAR2, aCdVoce VARCHAR2)
   Return Voce_f_saldi_cdr_linea%Rowtype
   Is
     recSaldi voce_f_saldi_cdr_linea%Rowtype;
   Begin
     recSaldi := getSaldi(aEs, aEsRes, aCdCdr, aCdGae, aTiApp, aTiGes, aCdVoce);
     if recSaldi.esercizio is null Then
       recSaldi.esercizio := aEs;
       recSaldi.esercizio_res := aEsRes;
       recSaldi.cd_centro_responsabilita := aCdCdr;
       recSaldi.cd_linea_attivita := aCdGae;
       recSaldi.ti_appartenenza := aTiApp;
       recSaldi.ti_gestione := aTiGes;
       recSaldi.cd_elemento_voce := aCdVoce;
       recSaldi.cd_voce := aCdVoce;
       recSaldi.IM_STANZ_INIZIALE_A1           := 0;
       recSaldi.IM_STANZ_INIZIALE_A2           := 0;
       recSaldi.IM_STANZ_INIZIALE_A3           := 0;
       recSaldi.VARIAZIONI_PIU                 := 0;
       recSaldi.VARIAZIONI_MENO                := 0;
       recSaldi.IM_STANZ_INIZIALE_CASSA        := 0;
       recSaldi.VARIAZIONI_PIU_CASSA           := 0;
       recSaldi.VARIAZIONI_MENO_CASSA          := 0;
       recSaldi.IM_OBBL_ACC_COMP               := 0;
       recSaldi.IM_STANZ_RES_IMPROPRIO         := 0;
       recSaldi.VAR_PIU_STANZ_RES_IMP          := 0;
       recSaldi.VAR_MENO_STANZ_RES_IMP         := 0;
       recSaldi.IM_OBBL_RES_IMP                := 0;
       recSaldi.VAR_PIU_OBBL_RES_IMP           := 0;
       recSaldi.VAR_MENO_OBBL_RES_IMP          := 0;
       recSaldi.IM_OBBL_RES_PRO                := 0;
       recSaldi.VAR_PIU_OBBL_RES_PRO           := 0;
       recSaldi.VAR_MENO_OBBL_RES_PRO          := 0;
       recSaldi.IM_MANDATI_REVERSALI_PRO       := 0;
       recSaldi.IM_MANDATI_REVERSALI_IMP       := 0;
       recSaldi.IM_PAGAMENTI_INCASSI           := 0;
       recSaldi.DACR                           := aTSNow;
       recSaldi.UTCR                           := aUser;
       recSaldi.DUVA                           := aTSNow;
       recSaldi.UTUV                           := aUser;
       recSaldi.PG_VER_REC                     := 1;

       CNRCTB054.ins_VOCE_F_SALDI_CDR_LINEA(recSaldi);
     End If;
     Return recSaldi;
   End;
 Begin
   Dbms_Output.put_line('generaVariazioneBilancio');

   aEsDefault := to_char(aLiquidIva.dt_inizio, 'YYYY');

   -- Estrazione del CDR della UO di versamento iva
   aCDRUO := CNRCTB020.getCDRResponsabileUO(aCdUo);

   -- Estrazione della linea di attività  comune da utilizzare per la creazione della variazione
   aLASpecVersIva := getLASPECVERSIVA(aEs, aCDRUO);

   -- Estrazione del CDR SAC di versamento iva
   --Viene recuperato sempre la UO dell'anno di liquidazione
   aCDRUOVERSIVA := getCDRUOVERSIVA(aEsDefault);

   -- Estrazione della linea di attività  SAC da utilizzare per la creazione della variazione
   aLASpecVersIvaSac := getLASPECVERSIVASAC(aEs, aCDRUOVERSIVA);

   aIvaResiduaDaVersare := abs(aLiquidIva.iva_da_versare);

   Dbms_Output.put_line('1. aIvaResiduaDaVersare: '||aIvaResiduaDaVersare);

   For recEsVariazione in (select a.esercizio_variazione, nvl(sum(a.im_variazione), 0) im_variazione
                           from (select esercizio_variazione, nvl(im_variazione, 0) im_variazione
                                 from liquidazione_iva_ripart_fin
                                 Where cd_cds = aLiquidIva.cd_cds
                                 and   esercizio = aLiquidIva.esercizio
                                 and   cd_unita_organizzativa = aLiquidIva.cd_unita_organizzativa
                                 and   dt_inizio = aLiquidIva.dt_inizio
                                 and   dt_fine = aLiquidIva.dt_fine
                                 and   im_variazione>0
                                 union all
                                 select aEsDefault, abs(aLiquidIva.iva_da_versare)-nvl(sum(im_variazione), 0) im_variazione
                                 from liquidazione_iva_ripart_fin
                                 Where cd_cds = aLiquidIva.cd_cds
                                 and   esercizio = aLiquidIva.esercizio
                                 and   cd_unita_organizzativa = aLiquidIva.cd_unita_organizzativa
                                 and   dt_inizio = aLiquidIva.dt_inizio
                                 and   dt_fine = aLiquidIva.dt_fine
                                 and   im_variazione>0) a
                           group by a.esercizio_variazione
                           having nvl(sum(a.im_variazione), 0)>0
                           order by a.esercizio_variazione) Loop
     Declare
       lPgVariazione                 numerazione_base.cd_corrente%type;
       pRigaVariazione               number := 0;
       aIvaDaVersareEsercizio        number:=0;
       aIvaResiduaDaVersareEsercizio number:=0;
       isVariazioneCompetenza        boolean := false;
       aPdgVariazione                pdg_variazione%rowtype;
       aVarStanzRes                  var_stanz_res%rowtype;
     Begin
       Dbms_Output.put_line('Es: '||recEsVariazione.esercizio_variazione||' - Imp: '||recEsVariazione.im_variazione);

       If aEs = recEsVariazione.esercizio_variazione Then
         isVariazioneCompetenza := true;
       End If;

       aIvaDaVersareEsercizio := recEsVariazione.im_variazione;

       If aIvaDaVersareEsercizio > aIvaResiduaDaVersare Then
         aIvaDaVersareEsercizio := aIvaResiduaDaVersare;
       End If;

       Dbms_Output.put_line('2. aIvaDaVersareEsercizio: '||aIvaDaVersareEsercizio);

       --creo la testata variazione per esercizio
       If isVariazioneCompetenza Then -- VARIAZIONE DI COMPETENZA
         Begin
           Select cd_corrente + 1
           Into lPgVariazione
           From numerazione_base
           Where esercizio = aEs
           And tabella = 'PDG_VARIAZIONE'
           And colonna = 'PG_VARIAZIONE_PDG'
           For update nowait;
         Exception when no_data_found then
           ibmerr001.RAISE_ERR_GENERICO('Manca la configurazione dei numeratori per la tabella PDG_VARIAZIONE.');
         End;

         aPdgVariazione.ESERCIZIO:=aEs;
         aPdgVariazione.PG_VARIAZIONE_PDG := lPgVariazione;
         aPdgVariazione.CD_CENTRO_RESPONSABILITA:=aCDRUO.cd_centro_responsabilita;
         aPdgVariazione.DT_APERTURA:=trunc(aTSNow);
         aPdgVariazione.DS_VARIAZIONE:=getDescLiquidazione(aLiquidIva);
         aPdgVariazione.RIFERIMENTI:=getDescLiquidazione(aLiquidIva);
         aPdgVariazione.DS_DELIBERA:='.';
         aPdgVariazione.STATO:='APP';
         aPdgVariazione.DT_APERTURA:=Trunc(Sysdate);
         aPdgVariazione.DT_CHIUSURA:=Trunc(Sysdate);
         aPdgVariazione.DT_APPROVAZIONE:=Trunc(Sysdate);
         aPdgVariazione.TIPOLOGIA:='STO_S_TOT';
         aPdgVariazione.TIPOLOGIA_FIN:='FES';
         aPdgVariazione.TI_MOTIVAZIONE_VARIAZIONE:='RAG';
         aPdgVariazione.FL_VISTO_DIP_VARIAZIONI:='N';
         aPdgVariazione.DACR:=aTSNow;
         aPdgVariazione.UTCR:=aUser;
         aPdgVariazione.DUVA:=aTSNow;
         aPdgVariazione.UTUV:=aUser;
         aPdgVariazione.PG_VER_REC:=1;

         CNRCTB051.ins_PDG_VARIAZIONE(aPdgVariazione);

         --Aggiorno il numeratore
         Update numerazione_base
         Set cd_corrente = lPgVariazione
         Where esercizio = aEs
         And tabella = 'PDG_VARIAZIONE'
         And colonna = 'PG_VARIAZIONE_PDG';
       Else
         Begin
           Select cd_corrente + 1
           Into lPgVariazione
           From numerazione_base
           Where esercizio = aEs
           And tabella = 'VAR_STANZ_RES'
           And colonna = 'PG_VARIAZIONE'
           For Update nowait;
         Exception
           When No_Data_Found Then
             ibmerr001.RAISE_ERR_GENERICO('Manca la configurazione dei numeratori per la tabella VAR_STANZ_RES.');
         End;

         aVarStanzRes.ESERCIZIO:=aEs;
         aVarStanzRes.PG_VARIAZIONE := lPgVariazione;
         aVarStanzRes.CD_CDS:=aCdCds;
         aVarStanzRes.CD_CENTRO_RESPONSABILITA:=aCDRUO.cd_centro_responsabilita;
         aVarStanzRes.ESERCIZIO_RES:=recEsVariazione.esercizio_variazione;
         aVarStanzRes.DT_APERTURA:=trunc(aTSNow);
         aVarStanzRes.DT_CHIUSURA:=Trunc(Sysdate);
         aVarStanzRes.DT_APPROVAZIONE:=Trunc(Sysdate);
         aVarStanzRes.DS_VARIAZIONE:=getDescLiquidazione(aLiquidIva);
         aVarStanzRes.DS_DELIBERA:='.';
         aVarStanzRes.STATO:='APP';
         aVarStanzRes.TIPOLOGIA:='STO';
         aVarStanzRes.TIPOLOGIA_FIN:='FES';
         aVarStanzRes.TI_MOTIVAZIONE_VARIAZIONE:='RAG';
         aVarStanzRes.DACR:=aTSNow;
         aVarStanzRes.UTCR:=aUser;
         aVarStanzRes.DUVA:=aTSNow;
         aVarStanzRes.UTUV:=aUser;
         aVarStanzRes.PG_VER_REC:=1;

         CNRCTB051.ins_VAR_STANZ_RES(aVarStanzRes);

         Update numerazione_base
         Set cd_corrente = lPgVariazione
         Where esercizio = aEs
         And tabella = 'VAR_STANZ_RES'
         And colonna = 'PG_VARIAZIONE';
       End If;

       aIvaResiduaDaVersareEsercizio := aIvaDaVersareEsercizio;

       --creo i dettagli variazione da sottrarre
       For recCDR in (select a.cd_cdr, nvl(sum(a.im_variazione), 0) im_variazione
                      from (select cd_cdr, nvl(im_variazione, 0) im_variazione
                            from liquidazione_iva_ripart_fin
                            Where cd_cds = aLiquidIva.cd_cds
                            and   esercizio = aLiquidIva.esercizio
                            and   cd_unita_organizzativa = aLiquidIva.cd_unita_organizzativa
                            and   dt_inizio = aLiquidIva.dt_inizio
                            and   dt_fine = aLiquidIva.dt_fine
                            and   im_variazione>0
                            and   esercizio_variazione = recEsVariazione.esercizio_variazione
                            union all
                            select null,
                                   case
                                     when recEsVariazione.esercizio_variazione = aEsDefault
                                     then abs(aLiquidIva.iva_da_versare)-nvl(sum(im_variazione), 0)
                                     else 0
                                     end
                            from liquidazione_iva_ripart_fin
                            Where cd_cds = aLiquidIva.cd_cds
                            and   esercizio = aLiquidIva.esercizio
                            and   cd_unita_organizzativa = aLiquidIva.cd_unita_organizzativa
                            and   dt_inizio = aLiquidIva.dt_inizio
                            and   dt_fine = aLiquidIva.dt_fine
                            and   im_variazione>0
                            and   recEsVariazione.esercizio_variazione = aEsDefault) a
                      group by a.cd_cdr
                      having nvl(sum(a.im_variazione), 0)>0
                      order by a.cd_cdr) LOOP
         Declare
           aAssPdgVariazioneCdr    ass_pdg_variazione_cdr%rowtype;
           aAssVarStanzResCdr      ass_var_stanz_res_cdr%rowtype;
           currentCDR              linea_attivita.cd_centro_responsabilita%type;
           aIvaDaVersareCDR        number:=0;
           aIvaResiduaDaVersareCDR number:=0;
         Begin
           Dbms_Output.put_line('Es: '||recEsVariazione.esercizio_variazione||
           ' - Cdr: '||recCDR.cd_cdr||
           ' - Imp: '||recCDR.im_variazione);

           aIvaDaVersareCDR := recCDR.im_variazione;

           If aIvaDaVersareCDR > aIvaResiduaDaVersareEsercizio Then
              aIvaDaVersareCDR := aIvaResiduaDaVersareEsercizio;
           End If;

           Dbms_Output.put_line('3. aIvaDaVersareCDR: '||aIvaDaVersareCDR);

           If recCDR.cd_cdr is not null Then
             currentCDR := recCDR.cd_cdr;
           Else
             currentCDR := aCDRUO.cd_centro_responsabilita;
           End If;

           If isVariazioneCompetenza Then -- VARIAZIONE DI COMPETENZA
             --Riga del CDR da cui togliere
             aAssPdgVariazioneCdr.ESERCIZIO:=aEs;
             aAssPdgVariazioneCdr.PG_VARIAZIONE_PDG := lPgVariazione;
             aAssPdgVariazioneCdr.CD_CENTRO_RESPONSABILITA:=currentCDR;
             aAssPdgVariazioneCdr.IM_ENTRATA := 0;
             aAssPdgVariazioneCdr.IM_SPESA := -abs(aIvaDaVersareCDR);
             aAssPdgVariazioneCdr.DACR:=aTSNow;
             aAssPdgVariazioneCdr.UTCR:=aUser;
             aAssPdgVariazioneCdr.DUVA:=aTSNow;
             aAssPdgVariazioneCdr.UTUV:=aUser;
             aAssPdgVariazioneCdr.PG_VER_REC:=1;

             CNRCTB050.ins_ASS_PDG_VARIAZIONE_CDR(aAssPdgVariazioneCdr);

           Else
             --Riga del CDR da cui togliere
             aAssVarStanzResCdr.ESERCIZIO:=aEs;
             aAssVarStanzResCdr.PG_VARIAZIONE := lPgVariazione;
             aAssVarStanzResCdr.CD_CENTRO_RESPONSABILITA:=currentCDR;
             aAssVarStanzResCdr.IM_SPESA := -abs(aIvaDaVersareCDR);
             aAssVarStanzResCdr.DACR:=aTSNow;
             aAssVarStanzResCdr.UTCR:=aUser;
             aAssVarStanzResCdr.DUVA:=aTSNow;
             aAssVarStanzResCdr.UTUV:=aUser;
             aAssVarStanzResCdr.PG_VER_REC:=1;

             CNRCTB051.ins_ASS_VAR_STANZ_RES_CDR(aAssVarStanzResCdr);

           End If;

           aIvaResiduaDaVersareCDR := aIvaDaVersareCDR;

           --Righe del CDR da cui togliere
           For recGAE in (select a.cd_linea_attivita, nvl(sum(a.im_variazione), 0) im_variazione
                          from (select cd_linea_attivita, nvl(im_variazione, 0) im_variazione
                                from liquidazione_iva_ripart_fin
                                Where cd_cds = aLiquidIva.cd_cds
                                and   esercizio = aLiquidIva.esercizio
                                and   cd_unita_organizzativa = aLiquidIva.cd_unita_organizzativa
                                and   dt_inizio = aLiquidIva.dt_inizio
                                and   dt_fine = aLiquidIva.dt_fine
                                and   im_variazione>0
                                and   esercizio_variazione = recEsVariazione.esercizio_variazione
                                and   nvl(cd_cdr,'Z') = nvl(recCDR.cd_cdr, 'Z')
                                union all
                                select null,
                                       case
                                         when recEsVariazione.esercizio_variazione = aEsDefault
                                         then abs(aLiquidIva.iva_da_versare)-nvl(sum(im_variazione), 0)
                                         else 0
                                         end
                                from liquidazione_iva_ripart_fin
                                Where cd_cds = aLiquidIva.cd_cds
                                and   esercizio = aLiquidIva.esercizio
                                and   cd_unita_organizzativa = aLiquidIva.cd_unita_organizzativa
                                and   dt_inizio = aLiquidIva.dt_inizio
                                and   dt_fine = aLiquidIva.dt_fine
                                and   im_variazione>0
                                and   recEsVariazione.esercizio_variazione = aEsDefault) a
                          group by a.cd_linea_attivita
                          having nvl(sum(a.im_variazione), 0)>0
                          order by a.cd_linea_attivita) LOOP
             Declare
               aPdgVariazioneRiga pdg_variazione_riga_gest%rowtype;
               aVarStanzResRiga   var_stanz_res_riga%rowtype;
               recSaldiLinea      voce_f_saldi_cdr_linea%Rowtype;
               pImpDisp           voce_f_saldi_cdr_linea.IM_STANZ_INIZIALE_A1%type := 0;
               currentGAE         linea_attivita.cd_linea_attivita%type;
               aIvaDaVersareGAE   number:=0;
             Begin
               Dbms_Output.put_line('Es: '||recEsVariazione.esercizio_variazione||
               ' - Cdr: '||recCDR.cd_cdr||
               ' - GAE: '||recGAE.cd_linea_attivita||
               ' - Imp: '||recGAE.im_variazione);

               aIvaDaVersareGAE := recGAE.im_variazione;

               If aIvaDaVersareGAE > aIvaResiduaDaVersareCDR Then
                 aIvaDaVersareGAE := aIvaResiduaDaVersareCDR;
               End If;

               Dbms_Output.put_line('4. aIvaDaVersareGAE: '||aIvaDaVersareGAE);

               If recGAE.cd_linea_attivita is not null Then
                 currentGAE := recGAE.CD_LINEA_ATTIVITA;
               Else
                 currentGAE := aLASpecVersIva.cd_linea_attivita;
               End If;

               --Effettuo il lock della tabella saldi
               recSaldiLinea := getOrCreateSaldi(aEs, recEsVariazione.esercizio_variazione, currentCDR, currentGAE, aEV.TI_APPARTENENZA, aEV.TI_GESTIONE, aEV.CD_ELEMENTO_VOCE);

               --Effettuo il controllo disponibilità per le GAE diverse dalla C0000020
               If currentGAE != aLASpecVersIva.cd_linea_attivita Then
                 --Leggo la disponibilità  residua
                 pImpDisp := getDisp(aEs, recEsVariazione.esercizio_variazione, currentCDR, currentGAE, aEV.TI_APPARTENENZA, aEV.TI_GESTIONE, aEV.CD_ELEMENTO_VOCE);

                 If aIvaDaVersareGAE > pImpDisp Then
                    IBMERR001.RAISE_ERR_GENERICO('Disponibilita'' residua della GAE '||currentCDR||'/'||currentGAE||' per l''esercizio '||recEsVariazione.esercizio_variazione||' e per la voce '||
                    aEV.TI_GESTIONE||'/'||aEV.CD_ELEMENTO_VOCE||
                    ' ('||to_char(pImpDisp,'FM999G999G999G999G990D00')||
                    ') insufficiente a coprire l''importo indicato ('||
                    to_char(aIvaDaVersareGAE,'FM999G999G999G999G990D00')||').');
                 End if;
               End If;

               pRigaVariazione := pRigaVariazione + 1;
               If isVariazioneCompetenza Then -- VARIAZIONE DI COMPETENZA
                 aPdgVariazioneRiga.ESERCIZIO := aEs;
                 aPdgVariazioneRiga.PG_VARIAZIONE_PDG := lPgVariazione;
                 aPdgVariazioneRiga.PG_RIGA := pRigaVariazione;
                 aPdgVariazioneRiga.CD_CDR_ASSEGNATARIO := currentCDR;
                 aPdgVariazioneRiga.CD_LINEA_ATTIVITA := currentGAE;
                 aPdgVariazioneRiga.CD_CDS_AREA := CNRUTL001.GETCDSFROMCDR(aAssPdgVariazioneCdr.CD_CENTRO_RESPONSABILITA);
                 aPdgVariazioneRiga.TI_APPARTENENZA := aEV.TI_APPARTENENZA;
                 aPdgVariazioneRiga.TI_GESTIONE := aEV.TI_GESTIONE;
                 aPdgVariazioneRiga.CD_ELEMENTO_VOCE := aEV.CD_ELEMENTO_VOCE;
                 aPdgVariazioneRiga.DT_REGISTRAZIONE := trunc(aTSNow);
                 aPdgVariazioneRiga.CATEGORIA_DETTAGLIO := 'DIR';
                 aPdgVariazioneRiga.IM_SPESE_GEST_DECENTRATA_INT := 0;
                 aPdgVariazioneRiga.IM_SPESE_GEST_DECENTRATA_EST := -abs(aIvaDaVersareGAE);
                 aPdgVariazioneRiga.IM_SPESE_GEST_ACCENTRATA_INT := 0;
                 aPdgVariazioneRiga.IM_SPESE_GEST_ACCENTRATA_EST := 0;
                 aPdgVariazioneRiga.IM_ENTRATA := 0;
                 aPdgVariazioneRiga.FL_VISTO_DIP_VARIAZIONI := 'N';
                 aPdgVariazioneRiga.DACR:=aTSNow;
                 aPdgVariazioneRiga.UTCR:=aUser;
                 aPdgVariazioneRiga.DUVA:=aTSNow;
                 aPdgVariazioneRiga.UTUV:=aUser;
                 aPdgVariazioneRiga.PG_VER_REC:=1;

                 CNRCTB051.ins_PDG_VARIAZIONE_RIGA_GEST(aPdgVariazioneRiga);

                 Update voce_f_saldi_cdr_linea
                 Set VARIAZIONI_MENO = VARIAZIONI_MENO + Abs(aPdgVariazioneRiga.IM_SPESE_GEST_DECENTRATA_EST)
                 Where esercizio = recSaldiLinea.esercizio
                 And   esercizio_res = recSaldiLinea.esercizio_res
                 And   cd_centro_responsabilita = recSaldiLinea.CD_CENTRO_RESPONSABILITA
                 And   cd_linea_attivita = recSaldiLinea.CD_LINEA_ATTIVITA
                 And   ti_appartenenza = recSaldiLinea.TI_APPARTENENZA
                 And   ti_gestione = recSaldiLinea.TI_GESTIONE
                 And   cd_elemento_voce = recSaldiLinea.CD_ELEMENTO_VOCE
                 And   cd_voce = recSaldiLinea.CD_VOCE;

               Else
                 -- VARIAZIONE DI RESIDUO
                 aVarStanzResRiga.ESERCIZIO := aEs;
                 aVarStanzResRiga.PG_VARIAZIONE := lPgVariazione;
                 aVarStanzResRiga.PG_RIGA := pRigaVariazione;
                 aVarStanzResRiga.ESERCIZIO_VOCE := aEV.ESERCIZIO;
                 aVarStanzResRiga.ESERCIZIO_RES := aVarStanzRes.ESERCIZIO_RES;
                 aVarStanzResRiga.CD_CDR := currentCDR;
                 aVarStanzResRiga.CD_LINEA_ATTIVITA := currentGAE;
                 aVarStanzResRiga.TI_APPARTENENZA := aEV.TI_APPARTENENZA;
                 aVarStanzResRiga.TI_GESTIONE := aEV.TI_GESTIONE;
                 aVarStanzResRiga.CD_VOCE := aEV.CD_ELEMENTO_VOCE;
                 aVarStanzResRiga.CD_ELEMENTO_VOCE := aEV.CD_ELEMENTO_VOCE;
                 aVarStanzResRiga.IM_VARIAZIONE := -abs(aIvaDaVersareGAE);
                 aVarStanzResRiga.DACR:=aTSNow;
                 aVarStanzResRiga.UTCR:=aUser;
                 aVarStanzResRiga.DUVA:=aTSNow;
                 aVarStanzResRiga.UTUV:=aUser;
                 aVarStanzResRiga.PG_VER_REC:=1;

                 CNRCTB051.ins_VAR_STANZ_RES_RIGA(aVarStanzResRiga);
                 Update voce_f_saldi_cdr_linea
                 Set VAR_MENO_STANZ_RES_IMP = VAR_MENO_STANZ_RES_IMP + Abs(aVarStanzResRiga.IM_VARIAZIONE)
                 Where esercizio = recSaldiLinea.esercizio
                 And   esercizio_res = recSaldiLinea.esercizio_res
                 And   cd_centro_responsabilita = recSaldiLinea.CD_CENTRO_RESPONSABILITA
                 And   cd_linea_attivita = recSaldiLinea.CD_LINEA_ATTIVITA
                 And   ti_appartenenza = recSaldiLinea.TI_APPARTENENZA
                 And   ti_gestione = recSaldiLinea.TI_GESTIONE
                 And   cd_elemento_voce = recSaldiLinea.CD_ELEMENTO_VOCE
                 And   cd_voce = recSaldiLinea.CD_VOCE;
               End If;

               aIvaResiduaDaVersareCDR := aIvaResiduaDaVersareCDR - aIvaDaVersareGAE;

               Dbms_Output.put_line('1 OUT. aIvaResiduaDaVersareCDR: '||aIvaResiduaDaVersareCDR);

               If aIvaResiduaDaVersareCDR<=0 Then
                 exit;
               End if;
             End;
           End Loop; --recGAE

           aIvaResiduaDaVersareEsercizio := aIvaResiduaDaVersareEsercizio - aIvaDaVersareCDR;

           Dbms_Output.put_line('2 OUT. aIvaResiduaDaVersareEsercizio: '||aIvaResiduaDaVersareEsercizio);

           If aIvaResiduaDaVersareEsercizio<=0 Then
             exit;
           End if;
         End;
       End Loop; --recCDR

       --Costruisco la riga cui dare gli importi
       Declare
         aAssPdgVariazioneCdr ass_pdg_variazione_cdr%rowtype;
         aPdgVariazioneRiga   pdg_variazione_riga_gest%rowtype;
         aVarStanzResRiga     var_stanz_res_riga%rowtype;
         aAssVarStanzResCdr   ass_var_stanz_res_cdr%rowtype;
         recSaldiLinea        voce_f_saldi_cdr_linea%Rowtype;
       Begin
         recSaldiLinea := getOrCreateSaldi(aEs, recEsVariazione.esercizio_variazione, aCDRUOVERSIVA.CD_CENTRO_RESPONSABILITA, aLASpecVersIvaSac.CD_LINEA_ATTIVITA, aEV.TI_APPARTENENZA, aEV.TI_GESTIONE, aEV.CD_ELEMENTO_VOCE);

         --Riga del CDR a cui dare
         pRigaVariazione := pRigaVariazione + 1;
         If isVariazioneCompetenza Then -- VARIAZIONE DI COMPETENZA
           aAssPdgVariazioneCdr.ESERCIZIO:=aEs;
           aAssPdgVariazioneCdr.PG_VARIAZIONE_PDG := lPgVariazione;
           aAssPdgVariazioneCdr.CD_CENTRO_RESPONSABILITA:=aCDRUOVERSIVA.CD_CENTRO_RESPONSABILITA;
           aAssPdgVariazioneCdr.IM_ENTRATA := 0;
           aAssPdgVariazioneCdr.IM_SPESA := abs(aIvaDaVersareEsercizio);
           aAssPdgVariazioneCdr.DACR:=aTSNow;
           aAssPdgVariazioneCdr.UTCR:=aUser;
           aAssPdgVariazioneCdr.DUVA:=aTSNow;
           aAssPdgVariazioneCdr.UTUV:=aUser;
           aAssPdgVariazioneCdr.PG_VER_REC:=1;

           CNRCTB050.ins_ASS_PDG_VARIAZIONE_CDR(aAssPdgVariazioneCdr);

           aPdgVariazioneRiga.ESERCIZIO := aEs;
           aPdgVariazioneRiga.PG_VARIAZIONE_PDG := lPgVariazione;
           aPdgVariazioneRiga.PG_RIGA := pRigaVariazione;
           aPdgVariazioneRiga.CD_CDR_ASSEGNATARIO := aAssPdgVariazioneCdr.CD_CENTRO_RESPONSABILITA;
           aPdgVariazioneRiga.CD_LINEA_ATTIVITA := aLASpecVersIvaSac.CD_LINEA_ATTIVITA;
           aPdgVariazioneRiga.CD_CDS_AREA := CNRUTL001.GETCDSFROMCDR(aAssPdgVariazioneCdr.CD_CENTRO_RESPONSABILITA);
           aPdgVariazioneRiga.TI_APPARTENENZA := aEV.TI_APPARTENENZA;
           aPdgVariazioneRiga.TI_GESTIONE := aEV.TI_GESTIONE;
           aPdgVariazioneRiga.CD_ELEMENTO_VOCE := aEV.CD_ELEMENTO_VOCE;
           aPdgVariazioneRiga.DT_REGISTRAZIONE := trunc(aTSNow);
           aPdgVariazioneRiga.CATEGORIA_DETTAGLIO := 'DIR';
           aPdgVariazioneRiga.IM_SPESE_GEST_DECENTRATA_INT := 0;
           aPdgVariazioneRiga.IM_SPESE_GEST_DECENTRATA_EST := abs(aIvaDaVersareEsercizio);
           aPdgVariazioneRiga.IM_SPESE_GEST_ACCENTRATA_INT := 0;
           aPdgVariazioneRiga.IM_SPESE_GEST_ACCENTRATA_EST := 0;
           aPdgVariazioneRiga.IM_ENTRATA := 0;
           aPdgVariazioneRiga.FL_VISTO_DIP_VARIAZIONI := 'N';
           aPdgVariazioneRiga.DACR:=aTSNow;
           aPdgVariazioneRiga.UTCR:=aUser;
           aPdgVariazioneRiga.DUVA:=aTSNow;
           aPdgVariazioneRiga.UTUV:=aUser;
           aPdgVariazioneRiga.PG_VER_REC:=1;

           CNRCTB051.ins_PDG_VARIAZIONE_RIGA_GEST(aPdgVariazioneRiga);

           Update voce_f_saldi_cdr_linea
           Set VARIAZIONI_PIU = VARIAZIONI_PIU + Abs(aPdgVariazioneRiga.IM_SPESE_GEST_DECENTRATA_EST)
           Where esercizio = recSaldiLinea.esercizio
           And   esercizio_res = recSaldiLinea.esercizio_res
           And   cd_centro_responsabilita = recSaldiLinea.CD_CENTRO_RESPONSABILITA
           And   cd_linea_attivita = recSaldiLinea.CD_LINEA_ATTIVITA
           And   ti_appartenenza = recSaldiLinea.TI_APPARTENENZA
           And   ti_gestione = recSaldiLinea.TI_GESTIONE
           And   cd_elemento_voce = recSaldiLinea.CD_ELEMENTO_VOCE
           And   cd_voce = recSaldiLinea.CD_VOCE;
         Else
           --Riga del CDR a cui dare
           aAssVarStanzResCdr.ESERCIZIO:=aEs;
           aAssVarStanzResCdr.PG_VARIAZIONE := lPgVariazione;
           aAssVarStanzResCdr.CD_CENTRO_RESPONSABILITA:=aCDRUOVERSIVA.CD_CENTRO_RESPONSABILITA;
           aAssVarStanzResCdr.IM_SPESA := abs(aIvaDaVersareEsercizio);
           aAssVarStanzResCdr.DACR:=aTSNow;
           aAssVarStanzResCdr.UTCR:=aUser;
           aAssVarStanzResCdr.DUVA:=aTSNow;
           aAssVarStanzResCdr.UTUV:=aUser;
           aAssVarStanzResCdr.PG_VER_REC:=1;

           CNRCTB051.ins_ASS_VAR_STANZ_RES_CDR(aAssVarStanzResCdr);

           aVarStanzResRiga.ESERCIZIO := aEs;
           aVarStanzResRiga.PG_VARIAZIONE := lPgVariazione;
           aVarStanzResRiga.PG_RIGA := pRigaVariazione;
           aVarStanzResRiga.ESERCIZIO_VOCE := aEV.ESERCIZIO;
           aVarStanzResRiga.ESERCIZIO_RES := aVarStanzRes.ESERCIZIO_RES;
           aVarStanzResRiga.CD_CDR := aAssVarStanzResCdr.CD_CENTRO_RESPONSABILITA;
           aVarStanzResRiga.CD_LINEA_ATTIVITA := aLASpecVersIvaSac.CD_LINEA_ATTIVITA;
           aVarStanzResRiga.TI_APPARTENENZA := aEV.TI_APPARTENENZA;
           aVarStanzResRiga.TI_GESTIONE := aEV.TI_GESTIONE;
           aVarStanzResRiga.CD_VOCE := aEV.CD_ELEMENTO_VOCE;
           aVarStanzResRiga.CD_ELEMENTO_VOCE := aEV.CD_ELEMENTO_VOCE;
           aVarStanzResRiga.IM_VARIAZIONE := abs(aIvaDaVersareEsercizio);
           aVarStanzResRiga.DACR:=aTSNow;
           aVarStanzResRiga.UTCR:=aUser;
           aVarStanzResRiga.DUVA:=aTSNow;
           aVarStanzResRiga.UTUV:=aUser;
           aVarStanzResRiga.PG_VER_REC:=1;

           CNRCTB051.ins_VAR_STANZ_RES_RIGA(aVarStanzResRiga);

           Update voce_f_saldi_cdr_linea
           Set VAR_PIU_STANZ_RES_IMP = VAR_PIU_STANZ_RES_IMP + Abs(aVarStanzResRiga.IM_VARIAZIONE)
           Where esercizio = recSaldiLinea.esercizio
           And   esercizio_res = recSaldiLinea.esercizio_res
           And   cd_centro_responsabilita = recSaldiLinea.CD_CENTRO_RESPONSABILITA
           And   cd_linea_attivita = recSaldiLinea.CD_LINEA_ATTIVITA
           And   ti_appartenenza = recSaldiLinea.TI_APPARTENENZA
           And   ti_gestione = recSaldiLinea.TI_GESTIONE
           And   cd_elemento_voce = recSaldiLinea.CD_ELEMENTO_VOCE
           And   cd_voce = recSaldiLinea.CD_VOCE;
         End If;
       End;

       -- Aggiorno i riferimento alle variazioni di versamento create
       pRigaLiquidVariazione := pRigaLiquidVariazione + 1;

       If isVariazioneCompetenza Then -- VARIAZIONE DI COMPETENZA
         Insert into LIQUIDAZIONE_IVA_VARIAZIONI
            (CD_CDS, ESERCIZIO, CD_UNITA_ORGANIZZATIVA,
             TIPO_LIQUIDAZIONE, DT_INIZIO, DT_FINE,
             REPORT_ID, PG_DETTAGLIO, ESERCIZIO_VARIAZIONE_COMP, PG_VARIAZIONE_COMP,
             ESERCIZIO_VARIAZIONE_RES, PG_VARIAZIONE_RES, DACR, UTCR, DUVA, UTUV, PG_VER_REC)
          Values
            (aLiquidIva.cd_cds, aLiquidIva.esercizio, aLiquidIva.cd_unita_organizzativa,
             aLiquidIva.tipo_liquidazione, aLiquidIva.dt_inizio, aLiquidIva.dt_fine,
             0, pRigaLiquidVariazione, aPdgVariazione.ESERCIZIO, aPdgVariazione.PG_VARIAZIONE_PDG,
             null, null, aTSNow, aUser, aTSNow, aUser, 1);
       Else
         Insert into LIQUIDAZIONE_IVA_VARIAZIONI
            (CD_CDS, ESERCIZIO, CD_UNITA_ORGANIZZATIVA,
             TIPO_LIQUIDAZIONE, DT_INIZIO, DT_FINE,
             REPORT_ID, PG_DETTAGLIO, ESERCIZIO_VARIAZIONE_COMP, PG_VARIAZIONE_COMP,
             ESERCIZIO_VARIAZIONE_RES, PG_VARIAZIONE_RES, DACR, UTCR, DUVA, UTUV, PG_VER_REC)
          Values
            (aLiquidIva.cd_cds, aLiquidIva.esercizio, aLiquidIva.cd_unita_organizzativa,
             aLiquidIva.tipo_liquidazione, aLiquidIva.dt_inizio, aLiquidIva.dt_fine,
             0, pRigaLiquidVariazione, null, null,
             aVarStanzRes.ESERCIZIO, aVarStanzRes.PG_VARIAZIONE, aTSNow, aUser, aTSNow, aUser, 1);
       End If;

       aIvaResiduaDaVersare := aIvaResiduaDaVersare - aIvaDaVersareEsercizio;

       Dbms_Output.put_line('3 OUT. aIvaResiduaDaVersare: '||aIvaResiduaDaVersare);

       Dbms_Output.put_line('Es: '||recEsVariazione.esercizio_variazione||
               ' - aIvaResiduaDaVersare: '||aIvaResiduaDaVersare);

       If aIvaResiduaDaVersare<=0 Then
         exit;
       End if;
     End;
   End Loop; --recEsVariazione
 End generaVariazioneBilancio;

 FUNCTION getDescPraticaFinCentro
   (aLiquidIva LIQUIDAZIONE_IVA%ROWTYPE)
   RETURN VARCHAR2 IS
   aDescrizione VARCHAR2(500);
   aTipologia VARCHAR2(100);

 BEGIN

   aDescrizione:='LIQUIDAZIONE IVA ';

   IF    aLiquidIva.tipo_liquidazione = CNRCTB250.TI_LIQ_IVA_COMMERC THEN
         aTipologia:='ATTIVITA''COMMERCIALI ';
   ELSIF aLiquidIva.tipo_liquidazione = CNRCTB250.TI_LIQ_IVA_ISTINTR THEN
         aTipologia:='ATTIVITA'' ISTITUZIONALI INTRA_UE ';
   ELSIF aLiquidIva.tipo_liquidazione = CNRCTB250.TI_LIQ_IVA_ISTSMSI THEN
         aTipologia:='ATTIVITA'' ISTITUZIONALI SAN MARINO SENZA IVA ';
   ELSIF aLiquidIva.tipo_liquidazione = CNRCTB250.TI_LIQ_IVA_ISTSNR Then
         aTipologia:='ATTIVITA'' ISTITUZIONALI SERVIZI NON RESIDENTI ';
   END IF;

   aDescrizione:=aDescrizione || aTipologia ||
                 'PERIODO: '||to_char(aLiquidIva.dt_inizio,'DD/MM/YYYY') || ' A ' ||
                 to_char(aLiquidIva.dt_fine,'DD/MM/YYYY');
  RETURN aDescrizione;

 END;

 function getLiqIvaCentro(aLiq liquidazione_iva%rowtype) return liquidazione_iva_centro%rowtype is
  aLiqC liquidazione_iva_centro%rowtype;
 begin
  select * into aLiqC from liquidazione_iva_centro where
       esercizio = aLiq.esercizio
   and dt_inizio = aLiq.dt_inizio
   and dt_fine = aLiq.dt_fine
   and tipo_liquidazione = aLiq.tipo_liquidazione
   for update nowait;
  return aLiqC;
 end;

 function creaLiqIvaCentro(aLiq liquidazione_iva%rowtype,aObb obbligazione%rowtype,aTSNow date, aUser varchar2) return liquidazione_iva_centro%rowtype is
  aLIC liquidazione_iva_centro%rowtype;
  aPgGruppoCentro number(10);
 begin
  aLIC.ESERCIZIO:=aLiq.esercizio;
  aLIC.DT_INIZIO:=aLiq.DT_INIZIO;
  aLIC.DT_FINE:=aLiq.DT_FINE;
  aLIC.TIPO_LIQUIDAZIONE:=aLiq.TIPO_LIQUIDAZIONE;
  aLIC.STATO:=STATO_LIQUID_CENTRO_INIZIALE;
  aLIC.CD_CDS_OBB_ACCENTR:=aObb.cd_cds;
  aLIC.ESERCIZIO_OBB_ACCENTR:=aObb.esercizio;
  aLIC.ESERCIZIO_ORI_OBB_ACCENTR:=aObb.esercizio_originale;
  aLIC.PG_OBB_ACCENTR:=aObb.pg_obbligazione;
  aLIC.CD_CDS_LI:=null;
  aLIC.CD_UO_LI:=null;
  aLIC.REPORT_ID_LI:=null;
  aLIC.DACR:=aTSNow;
  aLIC.UTCR:=aUser;
  aLIC.DUVA:=aTSNow;
  aLIC.UTUV:=aUser;
  aLIC.PG_VER_REC:=1;
  begin
   INS_LIQUIDAZIONE_IVA_CENTRO(aLIC);
  exception when DUP_VAL_ON_INDEX then
   -- L'inserimento può dare un errore di chiave duplicata se due sessioni tentano di inserire
   -- il primo record per lo stesso gruppo di versamento: la prima inserisce, la seconda deve essere bloccata
   -- e restituire l'errore di risorsa occupata
   IBMERR001.RAISE_ERR_GENERICO('Risorsa occupata riprovare più tardi');
  end;
  return aLIC;
 end;

 procedure aggiornaPraticaGruppoCentro(aLiq liquidazione_iva%rowtype, aUOVERSIVA unita_organizzativa%rowtype,aTesoreriaUnica VARCHAR2,aTSNow date,aUser varchar2) is
  aObbPG obbligazione%rowtype;
  aObbPGScad obbligazione_scadenzario%rowtype;
  aAcc accertamento%rowtype;
  aAccScad accertamento_scadenzario%rowtype;
  aVoceF voce_f%rowtype;
  aLIC liquidazione_iva_centro%rowtype;
  aCdTerzoAcc number(8);
  aCdModPagAcc varchar2(10);
  aPgBancaAcc number(10);
  aGen documento_generico%rowtype;
  aGenRiga documento_generico_riga%rowtype;
  aListGenRighe CNRCTB100.docGenRigaList;
  aRev reversale%rowtype;
  aRevRiga reversale_riga%rowtype;
  aAnagTst anagrafico%rowtype;
  aAssCoriFin ass_tipo_cori_ev%rowtype;
  aCdCoriIva varchar(100);
 Begin
  aLIC:=null;
  Begin
    -- Cerca di recuperare la pratica al centro di accantonamento dei versamenti locali (riferimento in LIQUID_GRUPPO_CENTRO)
    aLIC := getLiqIvaCentro(aliq);
    -- Se lo stato di liquidazione del centro è già chiuso solleva eccezione
    If aLIC.stato = STATO_LIQUID_CENTRO_CHIUSO then
      IBMERR001.RAISE_ERR_GENERICO('Liquidazione finanziaria al centro già effettuata. '||getDescLiquidazione(aliq));
    End if;
    If aTesoreriaUnica = 'N' Then
       -- Modifica la pratica di accantonamento al centro dell'importo della liquidazione locale
       CNRCTB043.modificaPraticaObb(aLIC.esercizio_obb_accentr,aLIC.cd_cds_obb_accentr,aLIC.esercizio_ori_obb_accentr,aLIC.pg_obb_accentr,abs(aLiq.iva_da_versare),aTSNow,aUser);
    End If;
  Exception when NO_DATA_FOUND then
    aObbPG:=null;
    aObbPGScad:=null;

    -- La pratica al centro non esiste ancora, devo definirla solo se non è attiva la tesoreria unica
    If aTesoreriaUnica='N' Then
       -- Estraggo il terzo UO relativo all'UO SAC che accentra i versamenti
       CNRCTB080.GETTERZOPERUO(aUOVERSIVA.cd_unita_organizzativa,aCdTerzoAcc, aCdModPagAcc, aPgBancaAcc, aLiq.esercizio);

       -- Estrazione del codice CORI per l'IVA
       If (aLiq.tipo_liquidazione = CNRCTB250.TI_LIQ_IVA_COMMERC ) Then
          aCdCoriIva:=CNRCTB015.GETVAL01PERCHIAVE(CNRCTB575.TI_CORI_SPECIALE,CNRCTB575.TI_CORI_IVA);
       elsif (aLiq.tipo_liquidazione = CNRCTB250.TI_LIQ_IVA_ISTSPLIT ) Then
    		  aCdCoriIva:=CNRCTB015.GETVAL03PERCHIAVE(CNRCTB575.TI_CORI_SPECIALE,CNRCTB575.TI_CORI_IVA);
       Else
          aCdCoriIva:=CNRCTB015.GETVAL02PERCHIAVE(CNRCTB575.TI_CORI_SPECIALE,CNRCTB575.TI_CORI_IVA);
       End if;
       -- Estrazione del conto finanziario da utilizzare
       Begin
         Select * into aAssCoriFin
         From ass_tipo_cori_ev
         Where cd_contributo_ritenuta = aCdCoriIva
         And   esercizio = aLiq.esercizio
         And   ti_gestione = CNRCTB001.GESTIONE_ENTRATE
         And   ti_appartenenza = CNRCTB001.APPARTENENZA_CDS
         And   ti_ente_percepiente = 'E';
       Exception when NO_DATA_FOUND then
         IBMERR001.RAISE_ERR_GENERICO('Conto finanziario di partita di giro per versamento IVA non trovato');
       End;
       Begin
         Select * into aVoceF
         From voce_f
         Where esercizio = aLiq.esercizio
         And   ti_appartenenza = CNRCTB001.APPARTENENZA_CDS
         And   ti_gestione = CNRCTB001.GESTIONE_ENTRATE
         And   cd_titolo_capitolo = aAssCoriFin.cd_elemento_voce
         And   ti_voce = CNRCTB001.CAPITOLO;
       Exception when NO_DATA_FOUND then
         IBMERR001.RAISE_ERR_GENERICO('Conto finanziario di partita di giro per versamento IVA non trovato');
       End;

       -- Creazione dell'accertamento al centro di accantonamento versamenti locali
       aAcc:=null;
       aAccScad:=null;
       aAcc.CD_CDS:=aUOVERSIVA.cd_unita_padre;
       aAcc.ESERCIZIO:=aLiq.esercizio;
       aAcc.CD_TIPO_DOCUMENTO_CONT:=CNRCTB018.TI_DOC_ACC_PGIRO;
       aAcc.CD_UNITA_ORGANIZZATIVA:=aUOVERSIVA.cd_unita_organizzativa;
       aAcc.CD_CDS_ORIGINE:=aUOVERSIVA.cd_unita_padre;
       aAcc.CD_UO_ORIGINE:=aUOVERSIVA.cd_unita_organizzativa;
       aAcc.TI_APPARTENENZA:=CNRCTB001.APPARTENENZA_CDS;
       aAcc.TI_GESTIONE:=CNRCTB001.GESTIONE_ENTRATE;
       -- Utilizzo come conto il primo conto che trovo di un CORI appartenente al gruppo
       aAcc.CD_ELEMENTO_VOCE:=aAssCoriFin.cd_elemento_voce;
       aAcc.CD_VOCE:=aVoceF.cd_voce;
       aAcc.DT_REGISTRAZIONE:=TRUNC(aTSNow);
       aAcc.DS_ACCERTAMENTO:=getDescPraticaFinCentro(aLiq);
       aAcc.NOTE_ACCERTAMENTO:='';
       aAcc.CD_TERZO:=aCdTerzoAcc;
       aAcc.IM_ACCERTAMENTO:=abs(aLiq.iva_da_versare);
       aAcc.FL_PGIRO:='Y';
       aAcc.RIPORTATO:='N';
       aAcc.DACR:=aTSNow;
       aAcc.UTCR:=aUser;
       aAcc.DUVA:=aTSNow;
       aAcc.UTUV:=aUser;
       aAcc.PG_VER_REC:=1;
       aAcc.ESERCIZIO_COMPETENZA:=aLiq.esercizio;
       -- Creo la partita di giro tronca di accantonamento versamenti locali
       CNRCTB040.CREAACCERTAMENTOPGIROTRONC(false,aAcc,aAccScad,aObbPG,aObbPGScad,trunc(aTSNow));

       -- Crea generico e reversale provvisoria per completare la pratica di accantonamento di versamento iva centro

       aAnagTst:=CNRCTB080.GETANAG(aCdTerzoAcc);

       aGen:=null;
       aGenRiga:=null;
       aGen.CD_TIPO_DOCUMENTO_AMM:=CNRCTB100.TI_GEN_CORI_ACC_ENTRATA;
       aGen.CD_CDS:=aAcc.cd_cds;
       aGen.CD_UNITA_ORGANIZZATIVA:=aAcc.cd_unita_organizzativa;
       aGen.ESERCIZIO:=aAcc.esercizio;
       aGen.CD_CDS_ORIGINE:=aAcc.cd_cds;
       aGen.CD_UO_ORIGINE:=aAcc.cd_unita_organizzativa;
       aGen.IM_TOTALE:=aAcc.im_accertamento;
       aGen.DATA_REGISTRAZIONE:=TRUNC(aTSNow);
       -- La competenza economica è il periodo della liquidazione
       aGen.DT_DA_COMPETENZA_COGE:=TRUNC(aLiq.dt_inizio);
       aGen.DT_A_COMPETENZA_COGE:=TRUNC(aLiq.dt_fine);
       aGen.DS_DOCUMENTO_GENERICO:=getDescPraticaFinCentro(aLiq);
       If aLiq.tipo_liquidazione = CNRCTB250.TI_LIQ_IVA_COMMERC Then
           aGen.TI_ISTITUZ_COMMERC:=CNRCTB100.TI_COMMERCIALE;
       Else
           aGen.TI_ISTITUZ_COMMERC:=CNRCTB100.TI_ISTITUZIONALE;
       End If;
       aGen.STATO_COFI:=CNRCTB100.STATO_GEN_COFI_TOT_MR;
       aGen.STATO_COGE:=CNRCTB100.STATO_COEP_EXC;
       aGen.STATO_COAN:=CNRCTB100.STATO_COEP_EXC;
       aGen.CD_DIVISA:='EURO';
       aGen.CAMBIO:=1;
       aGen.DACR:=aTSNow;
       aGen.UTCR:=aUser;
       aGen.DUVA:=aTSNow;
       aGen.UTUV:=aUser;
       aGen.PG_VER_REC:=1;
       aGen.DT_SCADENZA:=TRUNC(aTSNow);
       aGen.STATO_PAGAMENTO_FONDO_ECO:=CNRCTB100.STATO_NO_PFONDOECO;
       aGen.TI_ASSOCIATO_MANREV:=CNRCTB100.TI_ASSOC_TOT_MAN_REV ;
       aGenRiga.CD_CDS:=aGen.CD_CDS;
       aGenRiga.CD_UNITA_ORGANIZZATIVA:=aGen.CD_UNITA_ORGANIZZATIVA;
       aGenRiga.ESERCIZIO:=aGen.ESERCIZIO;
       aGenRiga.CD_TIPO_DOCUMENTO_AMM:=aGen.CD_TIPO_DOCUMENTO_AMM;
       aGenRiga.DS_RIGA:=aGen.DS_DOCUMENTO_GENERICO;
       aGenRiga.IM_RIGA_DIVISA:=aGen.IM_TOTALE;
       aGenRiga.IM_RIGA:=aGen.IM_TOTALE;
       aGenRiga.RAGIONE_SOCIALE:=aAnagTst.ragione_sociale;
       aGenRiga.NOME:=aAnagTst.nome;
       aGenRiga.COGNOME:=aAnagTst.cognome;
       aGenRiga.CODICE_FISCALE:=aAnagTst.codice_fiscale;
       aGenRiga.PARTITA_IVA:=aAnagTst.partita_iva;
       aGenRiga.DT_DA_COMPETENZA_COGE:=aGen.DT_DA_COMPETENZA_COGE;
       aGenRiga.DT_A_COMPETENZA_COGE:=aGen.DT_A_COMPETENZA_COGE;
       aGenRiga.STATO_COFI:=aGen.STATO_COFI;
       aGenRiga.CD_TERZO:=aAcc.cd_terzo;
       aGenRiga.CD_CDS_ACCERTAMENTO:=aAcc.CD_CDS;
       aGenRiga.ESERCIZIO_ACCERTAMENTO:=aAcc.ESERCIZIO;
       aGenRiga.ESERCIZIO_ORI_ACCERTAMENTO:=aAcc.ESERCIZIO_ORIGINALE;
       aGenRiga.PG_ACCERTAMENTO:=aAcc.PG_ACCERTAMENTO;
       aGenRiga.PG_ACCERTAMENTO_SCADENZARIO:=1;
       aGenRiga.CD_TERZO_UO_CDS:=aCdTerzoAcc;
       aGenRiga.PG_BANCA_UO_CDS:=aPgBancaAcc;
       aGenRiga.CD_MODALITA_PAG_UO_CDS:=aCdModPagAcc;
       aGenRiga.DACR:=aGen.DACR;
       aGenRiga.UTCR:=aGen.UTCR;
       aGenRiga.UTUV:=aGen.UTUV;
       aGenRiga.DUVA:=aGen.DUVA;
       aGenRiga.PG_VER_REC:=aGen.PG_VER_REC;
       aGenRiga.TI_ASSOCIATO_MANREV:=aGen.TI_ASSOCIATO_MANREV;
       aListGenRighe(1):=aGenRiga;
       CNRCTB110.CREAGENERICOAGGOBBACC(aGen,aListGenRighe);

       aRev:=null;
       aRevRiga:=null;
       aRev.CD_CDS:=aAcc.cd_cds;
       aRev.ESERCIZIO:=aAcc.esercizio;
       aRev.CD_UNITA_ORGANIZZATIVA:=aAcc.cd_unita_organizzativa;
       aRev.CD_CDS_ORIGINE:=aAcc.cd_cds;
       aRev.CD_UO_ORIGINE:=aAcc.cd_unita_organizzativa;
       aRev.CD_TIPO_DOCUMENTO_CONT:=CNRCTB018.TI_DOC_REV_PROVV;
       aRev.TI_REVERSALE:=CNRCTB038.TI_REV_SOS;
       aRev.TI_COMPETENZA_RESIDUO:=CNRCTB038.TI_MAN_COMP;
       aRev.DS_REVERSALE:=getDescPraticaFinCentro(aLiq);
       aRev.STATO:=CNRCTB038.STATO_REV_EME;
       -- Modifica del 26/02/2004
       -- La reversale di accantonamento IVA va in economica sul conto di patrimonio C/Erario IVA
       aRev.STATO_COGE:=CNRCTB100.STATO_COEP_INI;
       --
       aRev.DT_EMISSIONE:=TRUNC(aTSNow);
       aRev.IM_REVERSALE:=aAcc.im_accertamento;
       aRev.IM_INCASSATO:=0;
       aRev.DACR:=aTSNow;
       aRev.UTCR:=aUser;
       aRev.DUVA:=aTSNow;
       aRev.UTUV:=aUser;
       aRev.PG_VER_REC:=1;
       aRev.STATO_TRASMISSIONE:=CNRCTB038.STATO_REV_TRASCAS_NODIST;
       aRevRiga.CD_CDS:=aRev.cd_cds;
       aRevRiga.ESERCIZIO:=aRev.esercizio;
       aRevRiga.ESERCIZIO_ACCERTAMENTO:=aAcc.esercizio;
       aRevRiga.ESERCIZIO_ORI_ACCERTAMENTO:=aAcc.esercizio_originale;
       aRevRiga.PG_ACCERTAMENTO:=aAcc.pg_accertamento;
       aRevRiga.PG_ACCERTAMENTO_SCADENZARIO:=aAccScad.pg_accertamento_scadenzario;
       aRevRiga.CD_CDS_DOC_AMM:=aGen.cd_cds;
       aRevRiga.CD_UO_DOC_AMM:=aGen.cd_unita_organizzativa;
       aRevRiga.ESERCIZIO_DOC_AMM:=aGen.esercizio;
       aRevRiga.CD_TIPO_DOCUMENTO_AMM:=aGen.cd_tipo_documento_amm;
       aRevRiga.PG_DOC_AMM:=aGen.pg_documento_generico;
       aRevRiga.DS_REVERSALE_RIGA:=aRev.ds_reversale;
       aRevRiga.STATO:=aRev.STATO;
       aRevRiga.CD_TERZO:=aGenRiga.cd_terzo;
       aRevRiga.CD_TERZO_UO:=aGenRiga.cd_terzo_uo_cds;
       aRevRiga.PG_BANCA:=aGenRiga.pg_banca_uo_cds;
       aRevRiga.CD_MODALITA_PAG:=aGenRiga.cd_modalita_pag_uo_cds;
       aRevRiga.IM_REVERSALE_RIGA:=aRev.im_reversale;
       aRevRiga.FL_PGIRO:='Y';
       aRevRiga.UTCR:=aUser;
       aRevRiga.DACR:=aTSNow;
       aRevRiga.UTUV:=aUser;
       aRevRiga.DUVA:=aTSNow;
       aRevRiga.PG_VER_REC:=1;
       CNRCTB037.generaRevProvvPgiro(aRev, aRevRiga, aTSNow, aUser);
    End If; --Fine "If aTesoreriaUnica='N' Then"
    -- Crea il record in LIQUID_IVA_CENTRO che referenzia la pratica di accantonamento al centro dei versamenti IVA locali
    aLIC:=creaLiqIvaCentro(aLiq,aObbPG,aTSNow,aUser);
  End;

  -- Aggiorna la liquidazione IVA locale con le informazioni relative all'obbligazione pgiro su UO che versa
  Update liquidazione_iva
  Set    cd_cds_obb_accentr        = aLIC.cd_cds_obb_accentr,
         esercizio_obb_accentr     = aLIC.esercizio_obb_accentr,
         esercizio_ori_obb_accentr = aLIC.esercizio_ori_obb_accentr,
         pg_obb_accentr            = aLIC.pg_obb_accentr
  Where  cd_cds                 = aLiq.cd_cds And
         esercizio              = aLiq.esercizio And
         cd_unita_organizzativa = aLiq.cd_unita_organizzativa And
         tipo_liquidazione      = aLiq.tipo_liquidazione And
         dt_inizio              = aLiq.dt_inizio And
         dt_fine                = aLiq.dt_fine And
         report_id              = aLiq.report_id;

 End;

 procedure annullaLiquidIVA(aL liquidazione_iva%rowtype, aUtente varchar2) is
  aTSNow date;
  aLIC liquidazione_iva_centro%rowtype;
 begin
  aTSNow:=sysdate;

  IBMERR001.RAISE_ERR_GENERICO('Operazione non permessa');

  aLIC:=null;
  begin
   aLIC:=getLiqIvaCentro(aL);
   -- Decrementa l'importo della pratica di accantonamento della quota da versare della liquidazione annullata
   CNRCTB043.modificaPraticaObb(aLIC.esercizio_obb_accentr,aLIC.cd_cds_obb_accentr,aLIC.esercizio_ori_obb_accentr,aLIC.pg_obb_accentr,0-abs(aL.iva_da_versare),aTSNow,aUtente);
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('Riferimento al centro per liquidazione locale non trovato. '||getDescLiquidazione(aL));
  end;

  -- Aggiorna la liquidazione IVA togliendo i riferimenti a doc generico di versamento (trasferimento al centro)

  update liquidazione_iva
   set
     cd_tipo_documento = null
        ,esercizio_doc_amm = null
        ,cd_cds_doc_amm = null
        ,cd_uo_doc_amm = null
        ,pg_doc_amm =null
        ,duva = aTSNow
        ,utuv = aUtente
        ,pg_ver_rec = pg_ver_rec + 1
  where
         cd_cds = aL.cd_cds
     and esercizio = aL.esercizio
         and cd_unita_organizzativa = aL.cd_unita_organizzativa
         and dt_inizio = aL.dt_inizio
         and dt_fine = aL.dt_fine
         and tipo_liquidazione = aL.tipo_liquidazione
         and report_id = aL.report_id;
 end;

  Procedure contabilLiquidIVA(
    aCdCds VARCHAR2,
    aEs NUMBER,
    aCdUo VARCHAR2,
    aDtDa DATE,
    aDtA DATE,
    aUser VARCHAR2,
    aTipoLiquidazione VARCHAR2,
    inTipoImpegno VARCHAR2 Default Null)
  Is
    aUOVERSIVA unita_organizzativa%rowtype;
    aUOENTE unita_organizzativa%rowtype;
    aDivisaEuro varchar2(30);
    aCdTerzo number(8);
    aCdModPag varchar2(10);
    aPgBanca number(10);
    aObb obbligazione%rowtype;
    aObbScad obbligazione_scadenzario%rowtype;
    aObbScadVoce obbligazione_scad_voce%rowtype;
    aDettObbScad CNRCTB035.scadVoceListS;
    aGen documento_generico%rowtype;
    aGenRiga documento_generico_riga%rowtype;
    aListGenRighe CNRCTB100.docGenRigaList;
    aManP mandato%rowtype;
    aManPRiga mandato_riga%rowtype;
    aListRigheManP CNRCTB038.righeMandatoList;
    aEV elemento_voce%rowtype;
    aVoceF voce_f%rowtype;
    aTotMandato number(15,2);
    aAcc accertamento%rowtype;
    aAccScad accertamento_scadenzario%rowtype;
    aAccOrig accertamento%rowtype;
    aAccPG accertamento%rowtype;
    aAccPGScad accertamento_scadenzario%rowtype;
    aAssCoriFin ass_tipo_cori_ev%rowtype;
    aTSNow date;
    aAnagTst anagrafico%rowtype;
    aCdCoriIva varchar2(10);
    aGruppoCoriDet gruppo_cr_det%rowtype;
    aLiquidIva liquidazione_iva%rowtype;
    aCDRUO v_cdr_valido%rowtype;
    aLASpecIva v_linea_attivita_valida%rowtype;
    aAssPgiro ass_partita_giro%rowtype;
    isIvaInterfaccia boolean;
    recParametriCNR      PARAMETRI_CNR%Rowtype;
    aEsDefault number;

    Procedure createObbligazioneEnte(aEs INTEGER, aLiquidIva liquidazione_iva%Rowtype,
                                     aEsRes INTEGER, importoObb NUMBER,
                                     aUOVERSIVA unita_organizzativa%Rowtype,
                                     aEv elemento_voce%Rowtype, aGruppoCoriDet gruppo_cr_det%Rowtype, aLAIvaObb v_linea_attivita_valida%Rowtype,
                                     nuovoPdg VARCHAR2, tipodoc VARCHAR2, aUser VARCHAR2,
                                     aObb In Out obbligazione%Rowtype, aObbScad In Out obbligazione_scadenzario%Rowtype,
                                     aObbScadVoce In Out obbligazione_scad_voce%rowtype)
    Is
    Begin
      aObb.ESERCIZIO:=aEs;
      aObb.CD_CDS:=aUOVERSIVA.cd_unita_padre;
      aObb.CD_TIPO_DOCUMENTO_CONT:=tipodoc;
      aObb.ESERCIZIO_ORIGINALE:=aEsRes;
      aObb.CD_UNITA_ORGANIZZATIVA:=aUOVERSIVA.cd_unita_organizzativa;
      aObb.CD_CDS_ORIGINE:=aUOVERSIVA.cd_unita_padre;
      aObb.CD_UO_ORIGINE:=aUOVERSIVA.cd_unita_organizzativa;
      aObb.CD_TIPO_OBBLIGAZIONE:=NULL;
      aObb.TI_APPARTENENZA:=CNRCTB001.APPARTENENZA_CDS;
      aObb.TI_GESTIONE:=CNRCTB001.GESTIONE_SPESE;
      aObb.CD_ELEMENTO_VOCE:=aEv.cd_elemento_voce;
      aObb.DT_REGISTRAZIONE:=trunc(Sysdate);
      aObb.DS_OBBLIGAZIONE:=getDescLiquidazione(aLiquidIva);
      aObb.NOTE_OBBLIGAZIONE:=NULL;
      aObb.CD_TERZO:=aGruppoCoriDet.cd_terzo_versamento;
      aObb.IM_OBBLIGAZIONE:=abs(importoObb);
      aObb.IM_COSTI_ANTICIPATI:=0;
      aObb.ESERCIZIO_COMPETENZA:=aEs;
      aObb.STATO_OBBLIGAZIONE:=CNRCTB035.STATO_DEFINITIVO;
      aObb.DT_CANCELLAZIONE:=NULL;
      aObb.CD_RIFERIMENTO_CONTRATTO:=NULL;
      aObb.DT_SCADENZA_CONTRATTO:=NULL;
      aObb.FL_CALCOLO_AUTOMATICO:='Y';
      aObb.CD_FONDO_RICERCA:=NULL;
      aObb.FL_SPESE_COSTI_ALTRUI:='N';

      If tipodoc In (CNRCTB018.TI_DOC_OBB_PGIRO, CNRCTB018.TI_DOC_OBB_PGIRO_RES) Then
        aObb.FL_PGIRO:='Y';
      Else
        aObb.FL_PGIRO:='N';
      End If;

      aObb.DACR:=Sysdate;
      aObb.UTCR:=aUser;
      aObb.DUVA:=Sysdate;
      aObb.UTUV:=aUser;
      aObb.RIPORTATO:='N';
      aObb.PG_VER_REC:=1;

      aObbScad.ESERCIZIO:=aObb.esercizio;
      aObbScad.ESERCIZIO_ORIGINALE:=aObb.esercizio_originale;
      aObbScad.CD_CDS:=aObb.cd_cds;
      aObbScad.DT_SCADENZA:=aObb.dt_registrazione;
      aObbScad.DS_SCADENZA:=aObb.ds_obbligazione;
      aObbScad.IM_SCADENZA:=aObb.IM_OBBLIGAZIONE;
      If tipodoc In (CNRCTB018.TI_DOC_OBB_PGIRO, CNRCTB018.TI_DOC_OBB_PGIRO_RES) Then
        aObbScad.IM_ASSOCIATO_DOC_AMM:=aObb.IM_OBBLIGAZIONE;
      Else
        aObbScad.IM_ASSOCIATO_DOC_AMM:=0;
      End If;
      aObbScad.IM_ASSOCIATO_DOC_CONTABILE:=0;
      aObbScad.DACR:=Sysdate;
      aObbScad.UTCR:=aUser;
      aObbScad.DUVA:=Sysdate;
      aObbScad.UTUV:=aUser;
      aObbScad.PG_VER_REC:=1;

      aObbScadVoce.ESERCIZIO:=aObb.esercizio;
      aObbScadVoce.ESERCIZIO_ORIGINALE:=aObb.esercizio_originale;
      aObbScadVoce.CD_CDS:=aObb.cd_cds;

      aVoceF := Null;
      If nuovoPdg = 'N' Then
        Begin
          Select * Into aVoceF
          From voce_f
          Where esercizio = aLiquidIva.esercizio
          And   ti_appartenenza = CNRCTB001.APPARTENENZA_CDS
          And   ti_gestione = CNRCTB001.GESTIONE_SPESE
          And   cd_titolo_capitolo = aEv.cd_elemento_voce
          And   ti_voce = CNRCTB001.CAPITOLO;
        Exception
          When NO_DATA_FOUND Then
            IBMERR001.RAISE_ERR_GENERICO('Conto finanziario di spesa su partita di giro per versamento IVA centro non trovato');
        End;

        aObbScadVoce.TI_APPARTENENZA:=aVoceF.ti_appartenenza;
        aObbScadVoce.TI_GESTIONE:=aVoceF.ti_gestione;
        aObbScadVoce.CD_VOCE:=aVoceF.cd_voce;
      Else
        aObbScadVoce.TI_APPARTENENZA:=CNRCTB001.APPARTENENZA_CDS;
        aObbScadVoce.TI_GESTIONE:=CNRCTB001.GESTIONE_SPESE;
        aObbScadVoce.CD_VOCE:=aEv.cd_elemento_voce;
      End If;

      aObbScadVoce.CD_CENTRO_RESPONSABILITA:=aLAIvaObb.cd_centro_responsabilita;
      aObbScadVoce.CD_LINEA_ATTIVITA:=aLAIvaObb.cd_linea_attivita;
      aObbScadVoce.IM_VOCE:=aObb.IM_OBBLIGAZIONE;
      aObbScadVoce.CD_FONDO_RICERCA:=NULL;
      aObbScadVoce.DACR:=Sysdate;
      aObbScadVoce.UTCR:=aUser;
      aObbScadVoce.DUVA:=Sysdate;
      aObbScadVoce.UTUV:=aUser;
      aObbScadVoce.PG_VER_REC:=1;
    End createObbligazioneEnte;
  Begin
    recParametriCNR := CNRUTL001.getRecParametriCnr(aEs);

    If recParametriCNR.esercizio is null Then
      ibmerr001.RAISE_ERR_GENERICO('Contabilizzazione Liquidazione Iva fallita, manca la configurazione per esercizio '||to_char(aEs));
    End If;

    aTSNow:=sysdate;
    If aEs is Null Or aCdCds is Null Or aCdUo is Null Or aDtDa is Null Or aDtA is Null Or aTipoLiquidazione Is Null then
      IBMERR001.RAISE_ERR_GENERICO('Alcuni parametri di ingresso non sono stati valorizzati');
    End if;

    Begin
      Select * into aLiquidIva
      From liquidazione_iva
      Where cd_cds = aCdCds
      And   esercizio = aEs
      And   cd_unita_organizzativa = aCdUo
      And   tipo_liquidazione = aTipoLiquidazione
      And   dt_inizio = trunc(aDtDa)
      And   dt_fine = trunc(aDta)
      And   report_id = 0
      For update nowait;
    Exception when NO_DATA_FOUND then
      IBMERR001.RAISE_ERR_GENERICO('Dati per liquidazione IVA dell''UO:'||aCdUo||' non trovati');
    End;

    If aLiquidIva.iva_da_versare is null then
      IBMERR001.RAISE_ERR_GENERICO('Importo IVA da versare non specificato');
    End if;

    -- Check se si tratta di UO liquidata via interfaccia

    isIvaInterfaccia:=false;
    Declare
      aTempNum number;
    Begin
      Select distinct 1 into aTempNum
      From liquid_iva_interf_cds
      Where esercizio = aEs
      And   cd_cds = aCdCds;

      isIvaInterfaccia:=true;
    Exception
      When NO_DATA_FOUND then
        null;
    End;

    aEsDefault := to_char(aLiquidIva.dt_inizio, 'YYYY');

    -- Estrazione dell'UO di versamento IVA
    aUOVERSIVA:=CNRCTB020.getUOVersIVA(aEsDefault);

    -- Estrazione dell'UO ente
    aUOENTE:=CNRCTB020.GETUOENTE(aEs);

    -- Se si tratta di versamento accentrato, aggiorno il riferimento alla pratica di accantonamento al centro delle
    -- liquidazioni locali
    If aUOENTE.cd_unita_organizzativa = aCdUo Then
      Declare
        aLIC liquidazione_iva_centro%Rowtype;
      Begin
        aLIC:=getLiqIvaCentro(aLiquidIva);
        Update liquidazione_iva_centro
        Set stato = STATO_LIQUID_CENTRO_CHIUSO,
            cd_cds_li = aLiquidIva.cd_cds,
            cd_uo_li = aLiquidIva.cd_unita_organizzativa,
            report_id_li = aLiquidIva.report_id,
            utuv=aUser,
            duva=aTSNow,
            pg_ver_rec=pg_ver_rec+1
        Where esercizio = aLiquidIva.esercizio
        And   dt_inizio = aLiquidIva.dt_inizio
        And   dt_fine = aLiquidIva.dt_fine
        And   tipo_liquidazione = aLiquidIva.tipo_liquidazione;
      Exception
        When NO_DATA_FOUND Then
          -- LIQUIDAZIONE_IVA_CENTRO pu?Â² essere vuota (nessuna UO a debito d'iva nel periodo)
         Null;
      End;
    End If;

    -- Se l'importo da versare di IVA ?Â¨ minore o uguale a 0, non devo effettuare alcun versamento
    If aLiquidIva.iva_da_versare >= 0 Then
      Return;
    End if;

    -- Estrazione del codice CORI per l'IVA
    If (aLiquidIva.tipo_liquidazione = CNRCTB250.TI_LIQ_IVA_COMMERC ) Then
      aCdCoriIva:=CNRCTB015.GETVAL01PERCHIAVE(CNRCTB575.TI_CORI_SPECIALE,CNRCTB575.TI_CORI_IVA);
    elsif (aLiquidIva.tipo_liquidazione = CNRCTB250.TI_LIQ_IVA_ISTSPLIT ) Then
      aCdCoriIva:=CNRCTB015.GETVAL03PERCHIAVE(CNRCTB575.TI_CORI_SPECIALE,CNRCTB575.TI_CORI_IVA);
    Else
      -- Estrazione del codice CORI per l'IVA (Istituzionale)
      aCdCoriIva:=CNRCTB015.GETVAL02PERCHIAVE(CNRCTB575.TI_CORI_SPECIALE,CNRCTB575.TI_CORI_IVA);
    End If;

    -- Estraggo il CDR da utilizzare per estrarre la linea di attività  per obbligazione di versamento  (centro e periferia)
    -- PER TUTTE LE UO TRANNE QUELLE LIQUIDATE VIA INTERFCACCIA
    If not isIvaInterfaccia then
      Begin
        Select * into aCDRUO
        From v_cdr_valido
        Where esercizio = aEs
        And   cd_unita_organizzativa = decode(aCdUo,aUOENTE.cd_unita_organizzativa,aUOVERSIVA.cd_unita_organizzativa,aCdUo)
        And   to_number(cd_proprio_cdr)=0;
      Exception
        When NO_DATA_FOUND Then
          IBMERR001.RAISE_ERR_GENERICO('CDR di responsabile dell''UO '||aCdUo||' non trovato');
      End;

      -- Estrazione della linea di attività  comune da utilizzare per la creazione dell'obbligazione di versamento
      Begin
        Select a.* into aLASpecIva
        From v_linea_attivita_valida a
        Where a.esercizio = aEs
        And   a.cd_centro_responsabilita = aCDRUO.cd_centro_responsabilita
        And   a.cd_linea_attivita = CNRCTB015.GETVAL01PERCHIAVE(LINEA_ATTIVITA_SPECIALE,LA_VER_IVA)
        And   a.ti_gestione = CNRCTB001.GESTIONE_SPESE
        And   Exists (Select 1 From tipo_linea_attivita b
                      Where b.cd_tipo_linea_attivita = a.cd_tipo_linea_attivita
                      And   b.ti_tipo_la = CNRCTB010.TI_TIPO_LA_COMUNE);
      Exception
        When NO_DATA_FOUND Then
          IBMERR001.RAISE_ERR_GENERICO('Linea di attività comune per '||cnrutil.getLabelObbligazioneMin()||' versamento IVA non trovata ('||
aCDRUO.cd_centro_responsabilita||'/'||CNRCTB015.GETVAL01PERCHIAVE(LINEA_ATTIVITA_SPECIALE,LA_VER_IVA)||') per l''esercizio '||to_char(aEs));
      End;
    End If;

    Dbms_Output.put_line('aLASpecIva: '||aLASpecIva.cd_linea_attivita);

    aDivisaEuro:=CNRCTB015.GETVAL01PERCHIAVE(CNRCTB100.CCNR_DIVISA,CNRCTB100.CCNR_EURO);

    -- Se si tratta di liquidazione di UO <> da UO di versamento:
    --
    -- 1. creo una obbligazione su partite normali+generico (tipo gens accantonamento) e relativo mandato per il trasferimento all'UO di versamento
    -- 2. apro una partita di giro (E-S) sull'UO di versamento
    --

    If aUOENTE.cd_unita_organizzativa <> aCdUo Then
      -- Se la liquidazione ?Â¨ fatta via interfaccia, non devo gestire pratiche finanziarie all'interno del CDS versante
      If not isIvaInterfaccia Then -- non interfaccia

        CNRCTB080.GETTERZOPERUO(aUOVERSIVA.cd_unita_organizzativa,aCdTerzo, aCdModPag, aPgBanca, aEs);
        aAnagTst:=CNRCTB080.GETANAG(aCdTerzo);

        -- ===================================
        -- IVA COMMERCIALE (PARTITE NORMALI)
        -- ===================================
        If aLiquidIva.tipo_liquidazione = CNRCTB250.TI_LIQ_IVA_COMMERC Then
          -- Estrazione della voce del piano da utilizzare per il versamento dell'IVA
          aEV := getVoceIvaCommerciale(aEs);

          --Se non attiva la tesoreria unica effettuo il giro classico di creazione obbligazione
          --Se attiva invece devo creare una variazione di bilancio
          If nvl(recParametriCNR.fl_tesoreria_unica,'N') = 'N' Then
            -- Estrazione del capitolo di versamento IVA
            -- Se l'UO ?Â¨ del SAC -> Articolo corrispondente all'UO (CDR = CDR RUO), funzione = quella LA speciale IVA
            -- Se l'UO non ?Â¨ del SAC -> Capitolo corrispondente all'UO, funzione = quella LA speciale IVA
            Begin
              Select * into aVoceF
              From voce_f
              Where esercizio = aEV.esercizio
              And   ti_appartenenza = aEV.ti_appartenenza
              And   ti_gestione = aEV.ti_gestione
              And   cd_titolo_capitolo = aEV.cd_elemento_voce
              And   ((cd_centro_responsabilita = aCDRUO.cd_centro_responsabilita And
                      TI_VOCE = CNRCTB001.ARTICOLO)
                     Or
                     (cd_centro_responsabilita is Null And
                      cd_unita_organizzativa = aCdUo And
                      TI_VOCE = CNRCTB001.CAPITOLO)
                    )
              And   cd_funzione = aLASpecIva.cd_funzione
              And   fl_mastrino = 'Y';
            Exception
              When NO_DATA_FOUND Then
                IBMERR001.RAISE_ERR_GENERICO('Conto di spesa CDS da utilizzare per il versamento dell''IVA non trovato');
            End;

            If inTipoImpegno Is Null Then
              IBMERR001.RAISE_ERR_GENERICO('Tipologia Impegno per il versamento dell''IVA non trovata');
            End If;

            aObb.ESERCIZIO:=aEs;
            aObb.CD_CDS:=aCdCds;
            If inTipoImpegno = 'R' Then
              aObb.CD_TIPO_DOCUMENTO_CONT:=CNRCTB018.TI_DOC_OBB_RES_IMPRO;
            Else
              aObb.CD_TIPO_DOCUMENTO_CONT:=CNRCTB018.TI_DOC_OBB;
            End If;
            aObb.CD_UNITA_ORGANIZZATIVA:=aCdUo;
            aObb.CD_CDS_ORIGINE:=aCdCds;
            aObb.CD_UO_ORIGINE:=aCdUo;
            aObb.CD_TIPO_OBBLIGAZIONE:=NULL;
            aObb.TI_APPARTENENZA:=CNRCTB001.APPARTENENZA_CDS;
            aObb.TI_GESTIONE:=CNRCTB001.GESTIONE_SPESE;
            aObb.CD_ELEMENTO_VOCE:=aEV.cd_elemento_voce;
            aObb.DT_REGISTRAZIONE:=trunc(aTSNow);
            aObb.DS_OBBLIGAZIONE:=getDescLiquidazione(aLiquidIva);
            aObb.NOTE_OBBLIGAZIONE:=NULL;
            aObb.CD_TERZO:=aCdTerzo;
            aObb.IM_OBBLIGAZIONE:=abs(aLiquidIva.iva_da_versare);
            aObb.IM_COSTI_ANTICIPATI:=0;
            aObb.ESERCIZIO_COMPETENZA:=aEs;
            aObb.STATO_OBBLIGAZIONE:=CNRCTB035.STATO_DEFINITIVO;
            aObb.DT_CANCELLAZIONE:=NULL;
            aObb.CD_RIFERIMENTO_CONTRATTO:=NULL;
            aObb.DT_SCADENZA_CONTRATTO:=NULL;
            aObb.FL_CALCOLO_AUTOMATICO:='Y';
            aObb.CD_FONDO_RICERCA:=NULL;
            aObb.FL_SPESE_COSTI_ALTRUI:='N';
            aObb.FL_PGIRO:='N';
            aObb.DACR:=aTSNow;
            aObb.UTCR:=aUser;
            aObb.DUVA:=aTSNow;
            aObb.UTUV:=aUser;
            aObb.RIPORTATO:='N';
            aObb.PG_VER_REC:=1;
            If inTipoImpegno = 'R' Then
               aObb.ESERCIZIO_ORIGINALE:=aEs-1;
            Else
               aObb.ESERCIZIO_ORIGINALE:=aEs;
            End If;
            aObbScad.ESERCIZIO:=aObb.esercizio;
            aObbScad.CD_CDS:=aObb.cd_cds;
            aObbScad.DT_SCADENZA:=aObb.dt_registrazione;
            aObbScad.DS_SCADENZA:=aObb.ds_obbligazione;
            aObbScad.IM_SCADENZA:=aObb.IM_OBBLIGAZIONE;
            aObbScad.IM_ASSOCIATO_DOC_AMM:=aObb.IM_OBBLIGAZIONE;
            aObbScad.IM_ASSOCIATO_DOC_CONTABILE:=0;
            aObbScad.DACR:=aTSNow;
            aObbScad.UTCR:=aUser;
            aObbScad.DUVA:=aTSNow;
            aObbScad.UTUV:=aUser;
            aObbScad.PG_VER_REC:=1;
            aObbScad.ESERCIZIO_ORIGINALE:=aObb.esercizio_originale;
            aObbScadVoce.ESERCIZIO:=aObb.esercizio;
            aObbScadVoce.CD_CDS:=aObb.cd_cds;
            aObbScadVoce.TI_APPARTENENZA:=aVoceF.ti_appartenenza;
            aObbScadVoce.TI_GESTIONE:=aVoceF.ti_gestione;
            aObbScadVoce.CD_VOCE:=aVoceF.cd_voce;
            aObbScadVoce.CD_CENTRO_RESPONSABILITA:=aLASpecIva.cd_centro_responsabilita;
            aObbScadVoce.CD_LINEA_ATTIVITA:=aLASpecIva.cd_linea_attivita;
            aObbScadVoce.IM_VOCE:=aObb.IM_OBBLIGAZIONE;
            aObbScadVoce.CD_FONDO_RICERCA:=NULL;
            aObbScadVoce.DACR:=aTSNow;
            aObbScadVoce.UTCR:=aUser;
            aObbScadVoce.DUVA:=aTSNow;
            aObbScadVoce.UTUV:=aUser;
            aObbScadVoce.PG_VER_REC:=1;
            aObbScadVoce.ESERCIZIO_ORIGINALE:=aObb.esercizio_originale;
            aDettObbScad(1):=aObbScadVoce;
            --modificato il primo parametro (che indica se effettuare i controlli bloccanti di disponibilità ) a false
            --poiché i controlli sono stati spostati
            If inTipoImpegno = 'R' Then
               CNRCTB030.creaObbligazioneResidua(False,aObb,aObbScad,aDettObbScad);
            Else
               CNRCTB030.creaObbligazione(False,aObb,aObbScad,aDettObbScad);
            End If;

            aGen := null;
            aGenRiga := null;

            -- Creo il documento generico di spesa su partita normale collegato all'obbligazione

            aGen.CD_CDS:=aObb.cd_cds;
            aGen.CD_UNITA_ORGANIZZATIVA:=aObb.cd_unita_organizzativa;
            aGen.ESERCIZIO:=aObb.esercizio;
            aGen.CD_CDS_ORIGINE:=aObb.cd_cds;
            aGen.CD_UO_ORIGINE:=aObb.cd_unita_organizzativa;
            aGen.CD_TIPO_DOCUMENTO_AMM:=CNRCTB100.TI_GENERICO_TRASF_S;
            aGen.DATA_REGISTRAZIONE:=TRUNC(aTSNow);
            aGen.DS_DOCUMENTO_GENERICO:=aObb.ds_obbligazione;
            aGen.TI_ISTITUZ_COMMERC:=CNRCTB100.TI_COMMERCIALE;
            aGen.IM_TOTALE:=aObb.im_obbligazione;
            aGen.STATO_COFI:=CNRCTB100.STATO_GEN_COFI_TOT_MR;
            aGen.STATO_COGE:=CNRCTB100.STATO_COEP_EXC;
            aGen.STATO_COAN:=CNRCTB100.STATO_COEP_EXC;
            aGen.CD_DIVISA:=aDivisaEuro;
            aGen.CAMBIO:=1;
            --  aGen.ESERCIZIO_LETTERA:=0;
            --  aGen.PG_LETTERA:=0;
            aGen.DACR:=aTSNow;
            aGen.UTCR:=aUser;
            aGen.DUVA:=aTSNow;
            aGen.UTUV:=aUser;
            aGen.PG_VER_REC:=1;
            aGen.DT_SCADENZA:=TRUNC(aTSNow);
            aGen.STATO_PAGAMENTO_FONDO_ECO:=CNRCTB100.STATO_NO_PFONDOECO;
            aGen.TI_ASSOCIATO_MANREV:=CNRCTB100.TI_ASSOC_TOT_MAN_REV ;
            aGenRiga.CD_CDS:=aGen.CD_CDS;
            aGenRiga.CD_UNITA_ORGANIZZATIVA:=aGen.CD_UNITA_ORGANIZZATIVA;
            aGenRiga.ESERCIZIO:=aGen.ESERCIZIO;
            aGenRiga.CD_TIPO_DOCUMENTO_AMM:=aGen.CD_TIPO_DOCUMENTO_AMM;
            aGenRiga.DS_RIGA:=aGen.DS_DOCUMENTO_GENERICO;
            aGenRiga.IM_RIGA_DIVISA:=aGen.IM_TOTALE;
            aGenRiga.IM_RIGA:=aGen.IM_TOTALE;
            aGenRiga.CD_TERZO:=aCdTerzo;
            aGenRiga.RAGIONE_SOCIALE:=aAnagTst.RAGIONE_SOCIALE;
            aGenRiga.NOME:=aAnagTst.NOME;
            aGenRiga.COGNOME:=aAnagTst.COGNOME;
            aGenRiga.CODICE_FISCALE:=aAnagTst.CODICE_FISCALE;
            aGenRiga.PARTITA_IVA:=aAnagTst.PARTITA_IVA;
            aGenRiga.CD_MODALITA_PAG:=aCdModPag;
            aGenRiga.PG_BANCA:=aPgBanca;
            --   aGenRiga.CD_TERMINI_PAG:=aCompenso.CD_TERMINI_PAG;
            --   aGenRiga.CD_TERMINI_PAG_UO_CDS:=aCompenso.CD_TERMINI_PAG_UO_CDS;
            --   aGenRiga.PG_BANCA_UO_CDS:=aGen.PG_BANCA_UO_CDS;
            --   aGenRiga.CD_MODALITA_PAG_UO_CDS:=aGen.CD_MODALITA_PAG_UO_CDS;
            --   aGenRiga.NOTE:=aGen.NOTE;
            aGenRiga.DT_DA_COMPETENZA_COGE:=TRUNC(aTSNow);
            aGenRiga.DT_A_COMPETENZA_COGE:=TRUNC(aTSNow);
            aGenRiga.STATO_COFI:=aGen.STATO_COFI;
            --   aGenRiga.DT_CANCELLAZIONE:=aGen.DT_CANCELLAZIONE;
            --   aGenRiga.CD_CDS_OBBLIGAZIONE:=aGen.CD_CDS_OBBLIGAZIONE;
            --   aGenRiga.ESERCIZIO_OBBLIGAZIONE:=aGen.ESERCIZIO_OBBLIGAZIONE;
            --   aGenRiga.ESERCIZIO_ORI_OBBLIGAZIONE:=aGen.ESERCIZIO_ORI_OBBLIGAZIONE;
            --   aGenRiga.PG_OBBLIGAZIONE:=aGen.PG_OBBLIGAZIONE;
            --   aGenRiga.PG_OBBLIGAZIONE_SCADENZARIO:=aGen.PG_OBBLIGAZIONE_SCADENZARIO;
            aGenRiga.CD_CDS_OBBLIGAZIONE:=aObb.CD_CDS;
            aGenRiga.ESERCIZIO_OBBLIGAZIONE:=aObb.ESERCIZIO;
            aGenRiga.ESERCIZIO_ORI_OBBLIGAZIONE:=aObb.ESERCIZIO_ORIGINALE;
            aGenRiga.PG_OBBLIGAZIONE:=aObb.PG_OBBLIGAZIONE;
            aGenRiga.PG_OBBLIGAZIONE_SCADENZARIO:=1;
            aGenRiga.DACR:=aGen.DACR;
            aGenRiga.UTCR:=aGen.UTCR;
            aGenRiga.UTUV:=aGen.UTUV;
            aGenRiga.DUVA:=aGen.DUVA;
            aGenRiga.PG_VER_REC:=aGen.PG_VER_REC;
            aGenRiga.TI_ASSOCIATO_MANREV:=aGen.TI_ASSOCIATO_MANREV;
            aListGenRighe(1):=aGenRiga;
            CNRCTB110.CREAGENERICO(aGen,aListGenRighe);

            aManP.CD_CDS:=aObb.cd_cds;
            aManP.ESERCIZIO:=aObb.esercizio;
            aManP.CD_UNITA_ORGANIZZATIVA:=aObb.cd_unita_organizzativa;
            aManP.CD_CDS_ORIGINE:=aObb.cd_cds;
            aManP.CD_UO_ORIGINE:=aObb.cd_unita_organizzativa;
            aManP.CD_TIPO_DOCUMENTO_CONT:=CNRCTB018.TI_DOC_MAN;
            aManP.TI_MANDATO:=CNRCTB038.TI_MAN_PAG;
            aManP.TI_COMPETENZA_RESIDUO:=CNRCTB038.TI_MAN_COMP;
            aManP.DS_MANDATO:=aObb.ds_obbligazione;
            aManP.STATO:=CNRCTB038.STATO_MAN_EME;
            aManP.DT_EMISSIONE:=TRUNC(aTSNow);
            aManP.IM_RITENUTE:=0;
            aManP.IM_MANDATO:=aObb.im_obbligazione;
            --  aManP.DT_TRASMISSIONE:=;
            --  aManP.DT_PAGAMENTO:=;
            --  aManP.DT_ANNULLAMENTO:=;
            aManP.IM_PAGATO:=0;
            aManP.UTCR:=aUser;
            aManP.DACR:=aTSNow;
            aManP.UTUV:=aUser;
            aManP.DUVA:=aTSNow;
            aManP.PG_VER_REC:=1;
            aManP.STATO_TRASMISSIONE:=CNRCTB038.STATO_MAN_TRASCAS_NODIST;
            -- Generazione righe mandato

            aManPRiga:=null;
            aManPRiga.CD_CDS:=aGen.cd_cds;
            aManPRiga.ESERCIZIO:=aGen.esercizio;
            aManPRiga.ESERCIZIO_OBBLIGAZIONE:=aGenRiga.esercizio_obbligazione;
            aManPRiga.ESERCIZIO_ORI_OBBLIGAZIONE:=aGenRiga.esercizio_ori_obbligazione;
            aManPRiga.PG_OBBLIGAZIONE:=aGenRiga.pg_obbligazione;
            aManPRiga.PG_OBBLIGAZIONE_SCADENZARIO:=aGenRiga.pg_obbligazione_scadenzario;
            aManPRiga.CD_CDS_DOC_AMM:=aGen.cd_cds;
            aManPRiga.CD_UO_DOC_AMM:=aGen.cd_unita_organizzativa;
            aManPRiga.ESERCIZIO_DOC_AMM:=aGen.esercizio;
            aManPRiga.CD_TIPO_DOCUMENTO_AMM:=aGen.cd_tipo_documento_amm;
            aManPRiga.PG_DOC_AMM:=aGen.pg_documento_generico;
            aManPRiga.DS_MANDATO_RIGA:=aManP.ds_mandato;
            aManPRiga.STATO:=aManP.stato;
            aManPRiga.CD_TERZO:=aGenRiga.cd_terzo;
            aManPRiga.PG_BANCA:=aGenRiga.pg_banca;
            aManPRiga.CD_MODALITA_PAG:=aGenRiga.cd_modalita_pag;
            aManPRiga.IM_MANDATO_RIGA:=aObb.im_obbligazione;
            aManPRiga.IM_RITENUTE_RIGA:=0;
            aManPRiga.FL_PGIRO:='N';
            aManPRiga.UTCR:=aUser;
            aManPRiga.DACR:=aTSNow;
            aManPRiga.UTUV:=aUser;
            aManPRiga.DUVA:=aTSNow;
            aManPRiga.PG_VER_REC:=1;
            aTotMandato:=aTotMandato+aManPRiga.im_mandato_riga;
            aListRigheManP(1):=aManPRiga;
            -- =======================================================================================================
            --  inizio S.F. 10.07.2007
            -- Aggiorno il riferimento al documento generico di versamento in tabella liquidazione
            -- tranne che per le UO liquidate via interfaccia (inutile l'if tanto ?Â¨ gi?Â  nel ramo non interfaccia)
            Update liquidazione_iva
            Set    cd_tipo_documento = aGen.cd_tipo_documento_amm,
                   esercizio_doc_amm = aGen.esercizio,
                   cd_cds_doc_amm    = aGen.cd_cds,
                   cd_uo_doc_amm     = aGen.cd_unita_organizzativa,
                   pg_doc_amm        = aGen.pg_documento_generico,
                   duva              = aTSNow,
                   utuv              = aUser,
                   pg_ver_rec        = pg_ver_rec + 1
            Where  cd_cds                 = aCdCds And
                   esercizio              = aEs And
                   cd_unita_organizzativa = aCdUo And
                   dt_inizio              = trunc(aDtDa) And
                   dt_fine                = trunc(aDta) And
                   tipo_liquidazione      = aTipoLiquidazione And
                   report_id              = 0;

            --  fine S.F. 10.07.2007
            -- =======================================================================================================
            CNRCTB037.generaDocumento(aManP,aListRigheManP); -- IVA COMMERCIALE
          Else --nvl(recParametriCNR.fl_tesoreria_unica,'N') = 'Y'
            generaVariazioneBilancio(aCdCds,aEs,aCdUo,aEV,aLiquidIva,aTSNow,aUser);
          End If;
        Else --Else di ("If aLiquidIva.tipo_liquidazione = CNRCTB250.TI_LIQ_IVA_COMMERC Then")
          -- ====================================================
          -- INTRAUE/SAN MARINO SENZ'IVA  (PARTITA DI GIRO TRONCA)
          -- ====================================================
          --Se non attiva la tesoreria unica effettuo il giro classico di creazione obbligazione
          --Se attiva invece non devo creare nulla
          If nvl(recParametriCNR.fl_tesoreria_unica,'N') = 'N' Then
            -- Estrazione del conto finanziario da utilizzare
            aEV := getVoceIvaIstituzionale(aEs,nvl(recParametriCNR.fl_nuovo_pdg,'N'), aLiquidIva.tipo_liquidazione);

            aManP.CD_CDS:=aCdCds;
            aManP.ESERCIZIO:=aEs;
            aManP.CD_UNITA_ORGANIZZATIVA:=aCdUo;
            aManP.CD_CDS_ORIGINE:=aCdCds;
            aManP.CD_UO_ORIGINE:=aCdUo;
            aManP.CD_TIPO_DOCUMENTO_CONT:=CNRCTB018.TI_DOC_MAN;
            aManP.TI_MANDATO:=CNRCTB038.TI_MAN_PAG;
            aManP.TI_COMPETENZA_RESIDUO:=CNRCTB038.TI_MAN_COMP;
            aManP.DS_MANDATO:=getDescLiquidazione(aLiquidIva);
            aManP.STATO:=CNRCTB038.STATO_MAN_EME;
            aManP.DT_EMISSIONE:=TRUNC(aTSNow);
            aManP.IM_RITENUTE:=0;
            --  aManP.DT_TRASMISSIONE:=;
            --  aManP.DT_PAGAMENTO:=;
            --  aManP.DT_ANNULLAMENTO:=;
            aManP.IM_PAGATO:=0;
            aManP.UTCR:=aUser;
            aManP.DACR:=aTSNow;
            aManP.UTUV:=aUser;
            aManP.DUVA:=aTSNow;
            aManP.PG_VER_REC:=1;
            aManP.STATO_TRASMISSIONE:=CNRCTB038.STATO_MAN_TRASCAS_NODIST;

            aTotMandato:=0;

            aGen := null;

            -- Creo il documento generico di spesa su partita di giro collegato all'annotazione di entrata su pgiro del contributo ritenuta
            aGen.CD_CDS:=aCdCds;
            aGen.CD_UNITA_ORGANIZZATIVA:=aCdUo;
            aGen.ESERCIZIO:=aEs;
            aGen.CD_CDS_ORIGINE:=aCdCds;
            aGen.CD_UO_ORIGINE:=aCdUo;
            aGen.CD_TIPO_DOCUMENTO_AMM:=CNRCTB100.TI_GEN_CORI_VER_SPESA;
            aGen.DATA_REGISTRAZIONE:=TRUNC(aTSNow);
            aGen.DS_DOCUMENTO_GENERICO:=getDescLiquidazione(aLiquidIva);
            aGen.TI_ISTITUZ_COMMERC:=CNRCTB100.TI_ISTITUZIONALE;
            aGen.STATO_COFI:=CNRCTB100.STATO_GEN_COFI_TOT_MR;
            aGen.STATO_COGE:=CNRCTB100.STATO_COEP_EXC;
            aGen.STATO_COAN:=CNRCTB100.STATO_COEP_EXC;
            aGen.CD_DIVISA:=aDivisaEuro;
            aGen.CAMBIO:=1;
            --  aGen.ESERCIZIO_LETTERA:=0;
            --  aGen.PG_LETTERA:=0;
            aGen.DACR:=aTSNow;
            aGen.UTCR:=aUser;
            aGen.DUVA:=aTSNow;
            aGen.UTUV:=aUser;
            aGen.PG_VER_REC:=1;
            aGen.DT_SCADENZA:=TRUNC(aTSNow);
            aGen.STATO_PAGAMENTO_FONDO_ECO:=CNRCTB100.STATO_NO_PFONDOECO;
            aGen.TI_ASSOCIATO_MANREV:=CNRCTB100.TI_ASSOC_TOT_MAN_REV ;

            begin
             select * into aVoceF from voce_f where
                    esercizio = aLiquidIva.esercizio
              and ti_appartenenza = CNRCTB001.APPARTENENZA_CDS
              and ti_gestione = CNRCTB001.GESTIONE_SPESE
                and cd_titolo_capitolo = aAssPgiro.cd_voce_clg
                and ti_voce = CNRCTB001.CAPITOLO;
            exception when NO_DATA_FOUND then
             IBMERR001.RAISE_ERR_GENERICO('Conto finanziario di spesa su partita di giro per versamento IVA centro non trovato');
            end;

            aObb.ESERCIZIO:=aEs;
            aObb.CD_CDS:=aCdCds;
            aObb.CD_TIPO_DOCUMENTO_CONT:=CNRCTB018.TI_DOC_OBB_PGIRO;
            aObb.CD_UNITA_ORGANIZZATIVA:=aCdUo;
            aObb.CD_CDS_ORIGINE:=aCdCds;
            aObb.CD_UO_ORIGINE:=aCdUo;
            aObb.CD_TIPO_OBBLIGAZIONE:=NULL;
            aObb.TI_APPARTENENZA:=CNRCTB001.APPARTENENZA_CDS;
            aObb.TI_GESTIONE:=CNRCTB001.GESTIONE_SPESE;
            aObb.CD_ELEMENTO_VOCE:=aEv.cd_elemento_voce;
            aObb.DT_REGISTRAZIONE:=trunc(aTSNow);
            aObb.DS_OBBLIGAZIONE:=getDescLiquidazione(aLiquidIva);
            aObb.NOTE_OBBLIGAZIONE:=NULL;
            aObb.CD_TERZO:=aCdTerzo;
            aObb.IM_OBBLIGAZIONE:=abs(aLiquidIva.iva_da_versare);
            aObb.IM_COSTI_ANTICIPATI:=0;
            aObb.ESERCIZIO_COMPETENZA:=aEs;
            aObb.STATO_OBBLIGAZIONE:=CNRCTB035.STATO_DEFINITIVO;
            aObb.DT_CANCELLAZIONE:=NULL;
            aObb.CD_RIFERIMENTO_CONTRATTO:=NULL;
            aObb.DT_SCADENZA_CONTRATTO:=NULL;
            aObb.FL_CALCOLO_AUTOMATICO:='Y';
            aObb.CD_FONDO_RICERCA:=NULL;
            aObb.FL_SPESE_COSTI_ALTRUI:='N';
            aObb.FL_PGIRO:='Y';
            aObb.DACR:=aTSNow;
            aObb.UTCR:=aUser;
            aObb.DUVA:=aTSNow;
            aObb.UTUV:=aUser;
            aObb.RIPORTATO:='N';
            aObb.PG_VER_REC:=1;
            aObbScad.ESERCIZIO:=aObb.esercizio;
            aObbScad.CD_CDS:=aObb.cd_cds;
            aObbScad.DT_SCADENZA:=aObb.dt_registrazione;
            aObbScad.DS_SCADENZA:=aObb.ds_obbligazione;
            aObbScad.IM_SCADENZA:=aObb.IM_OBBLIGAZIONE;
            aObbScad.IM_ASSOCIATO_DOC_AMM:=aObb.IM_OBBLIGAZIONE;
            aObbScad.IM_ASSOCIATO_DOC_CONTABILE:=0;
            aObbScad.DACR:=aTSNow;
            aObbScad.UTCR:=aUser;
            aObbScad.DUVA:=aTSNow;
            aObbScad.UTUV:=aUser;
            aObbScad.PG_VER_REC:=1;
            aObbScadVoce.ESERCIZIO:=aObb.esercizio;
            aObbScadVoce.CD_CDS:=aObb.cd_cds;
            aObbScadVoce.TI_APPARTENENZA:=aVoceF.ti_appartenenza;
            aObbScadVoce.TI_GESTIONE:=aVoceF.ti_gestione;
            aObbScadVoce.CD_VOCE:=aVoceF.cd_voce;
            aObbScadVoce.CD_CENTRO_RESPONSABILITA:=aLASpecIva.cd_centro_responsabilita;
            aObbScadVoce.CD_LINEA_ATTIVITA:=aLASpecIva.cd_linea_attivita;
            aObbScadVoce.IM_VOCE:=aObb.IM_OBBLIGAZIONE;
            aObbScadVoce.CD_FONDO_RICERCA:=NULL;
            aObbScadVoce.DACR:=aTSNow;
            aObbScadVoce.UTCR:=aUser;
            aObbScadVoce.DUVA:=aTSNow;
            aObbScadVoce.UTUV:=aUser;
            aObbScadVoce.PG_VER_REC:=1;
            aDettObbScad(1):=aObbScadVoce;

            -- Creo la partita di giro tronca di accantonamento versamenti locali
            CNRCTB030.CREAOBBLIGAZIONEPGIROTRONC(false,aObb,aObbScad,aAccPG,aAccPGScad,trunc(aTSNow));

            aGenRiga:=null;

            aGenRiga.CD_CDS:=aGen.CD_CDS;
            aGenRiga.CD_UNITA_ORGANIZZATIVA:=aGen.CD_UNITA_ORGANIZZATIVA;
            aGenRiga.ESERCIZIO:=aGen.ESERCIZIO;
            aGenRiga.CD_TIPO_DOCUMENTO_AMM:=aGen.CD_TIPO_DOCUMENTO_AMM;
            aGenRiga.DS_RIGA:=aGen.DS_DOCUMENTO_GENERICO;
            aGenRiga.IM_RIGA_DIVISA:= aObb.im_obbligazione;
            aGenRiga.IM_RIGA:=aObb.im_obbligazione;
            aGenRiga.CD_TERZO:=aCdTerzo;
            aGenRiga.RAGIONE_SOCIALE:=aAnagTst.RAGIONE_SOCIALE;
            aGenRiga.NOME:=aAnagTst.NOME;
            aGenRiga.COGNOME:=aAnagTst.COGNOME;
            aGenRiga.CODICE_FISCALE:=aAnagTst.CODICE_FISCALE;
            aGenRiga.PARTITA_IVA:=aAnagTst.PARTITA_IVA;
            aGenRiga.CD_MODALITA_PAG:=aCdModPag;
            aGenRiga.PG_BANCA:=aPgBanca;
            --   aGenRiga.CD_TERMINI_PAG:=aCompenso.CD_TERMINI_PAG;
            --   aGenRiga.CD_TERMINI_PAG_UO_CDS:=aCompenso.CD_TERMINI_PAG_UO_CDS;
            --   aGenRiga.PG_BANCA_UO_CDS:=aGen.PG_BANCA_UO_CDS;
            --   aGenRiga.CD_MODALITA_PAG_UO_CDS:=aGen.CD_MODALITA_PAG_UO_CDS;
            --   aGenRiga.NOTE:=aGen.NOTE;
            aGenRiga.DT_DA_COMPETENZA_COGE:=TRUNC(aLiquidIva.dt_inizio);
            aGenRiga.DT_A_COMPETENZA_COGE:=TRUNC(aLiquidIva.dt_fine);
            aGenRiga.STATO_COFI:=aGen.STATO_COFI;
            --   aGenRiga.DT_CANCELLAZIONE:=aGen.DT_CANCELLAZIONE;
            --   aGenRiga.CD_CDS_OBBLIGAZIONE:=aGen.CD_CDS_OBBLIGAZIONE;
            --   aGenRiga.ESERCIZIO_OBBLIGAZIONE:=aGen.ESERCIZIO_OBBLIGAZIONE;
            --   aGenRiga.ESERCIZIO_ORI_OBBLIGAZIONE:=aGen.ESERCIZIO_ORI_OBBLIGAZIONE;
            --   aGenRiga.PG_OBBLIGAZIONE:=aGen.PG_OBBLIGAZIONE;
            --   aGenRiga.PG_OBBLIGAZIONE_SCADENZARIO:=aGen.PG_OBBLIGAZIONE_SCADENZARIO;
            aGenRiga.CD_CDS_OBBLIGAZIONE:=aObb.CD_CDS;
            aGenRiga.ESERCIZIO_OBBLIGAZIONE:=aObb.ESERCIZIO;
            aGenRiga.ESERCIZIO_ORI_OBBLIGAZIONE:=aObb.ESERCIZIO_ORIGINALE;
            aGenRiga.PG_OBBLIGAZIONE:=aObb.PG_OBBLIGAZIONE;
            aGenRiga.PG_OBBLIGAZIONE_SCADENZARIO:=1;
            aGenRiga.DACR:=aGen.DACR;
            aGenRiga.UTCR:=aGen.UTCR;
            aGenRiga.UTUV:=aGen.UTUV;
            aGenRiga.DUVA:=aGen.DUVA;
            aGenRiga.PG_VER_REC:=aGen.PG_VER_REC;
            aGenRiga.TI_ASSOCIATO_MANREV:=aGen.TI_ASSOCIATO_MANREV;
            aListGenRighe(aListGenRighe.count+1):=aGenRiga;
            -- Generazione righe mandato
            aManPRiga:=null;
            aManPRiga.CD_CDS:=aGen.cd_cds;
            aManPRiga.ESERCIZIO:=aGen.esercizio;
            aManPRiga.ESERCIZIO_OBBLIGAZIONE:=aGenRiga.esercizio_obbligazione;
            aManPRiga.ESERCIZIO_ORI_OBBLIGAZIONE:=aGenRiga.esercizio_ori_obbligazione;
            aManPRiga.PG_OBBLIGAZIONE:=aGenRiga.pg_obbligazione;
            aManPRiga.PG_OBBLIGAZIONE_SCADENZARIO:=aGenRiga.pg_obbligazione_scadenzario;
            aManPRiga.CD_CDS_DOC_AMM:=aGen.cd_cds;
            aManPRiga.CD_UO_DOC_AMM:=aGen.cd_unita_organizzativa;
            aManPRiga.ESERCIZIO_DOC_AMM:=aGen.esercizio;
            aManPRiga.CD_TIPO_DOCUMENTO_AMM:=aGen.cd_tipo_documento_amm;
            aManPRiga.PG_DOC_AMM:=aGen.pg_documento_generico;
            aManPRiga.DS_MANDATO_RIGA:=aManP.ds_mandato;
            aManPRiga.STATO:=aManP.stato;
            aManPRiga.CD_TERZO:=aGenRiga.cd_terzo;
            aManPRiga.PG_BANCA:=aGenRiga.pg_banca;
            aManPRiga.CD_MODALITA_PAG:=aGenRiga.cd_modalita_pag;
            aManPRiga.IM_MANDATO_RIGA:=aGenRiga.im_riga;
            aManPRiga.IM_RITENUTE_RIGA:=0;
            aManPRiga.FL_PGIRO:='Y';
            aManPRiga.UTCR:=aUser;
            aManPRiga.DACR:=aTSNow;
            aManPRiga.UTUV:=aUser;
            aManPRiga.DUVA:=aTSNow;
            aManPRiga.PG_VER_REC:=1;
            aTotMandato:=aTotMandato+aManPRiga.im_mandato_riga;
            aListRigheManP(aListRigheManP.count+1):=aManPRiga;

            aGen.IM_TOTALE:=aTotMandato;
            CNRCTB110.CREAGENERICOAGGOBBACC(aGen,aListGenRighe);
            FOR I IN 1 .. aListRigheManP.COUNT LOOP
             aListRigheManP(I).CD_CDS_DOC_AMM:=aGen.cd_cds;
             aListRigheManP(I).CD_UO_DOC_AMM:=aGen.cd_unita_organizzativa;
             aListRigheManP(I).ESERCIZIO_DOC_AMM:=aGen.esercizio;
             aListRigheManP(I).CD_TIPO_DOCUMENTO_AMM:=aGen.cd_tipo_documento_amm;
             aListRigheManP(I).PG_DOC_AMM:=aGen.pg_documento_generico;
            END LOOP;

            aManP.IM_MANDATO:=aTotMandato;

            -- =======================================================================================================
            --  inizio S.F. 10.07.2007

            -- Aggiorno il riferimento al documento generico di versamento in tabella liquidazione
            -- tranne che per le UO liquidate via interfaccia (inutile l'if tanto ?Â¨ gi?Â  nel ramo non interfaccia)

            Update liquidazione_iva
            Set    cd_tipo_documento = aGen.cd_tipo_documento_amm,
                   esercizio_doc_amm = aGen.esercizio,
                   cd_cds_doc_amm    = aGen.cd_cds,
                   cd_uo_doc_amm     = aGen.cd_unita_organizzativa,
                   pg_doc_amm        = aGen.pg_documento_generico,
                   duva              = aTSNow,
                   utuv              = aUser,
                   pg_ver_rec        = pg_ver_rec + 1
            Where  cd_cds                 = aCdCds And
                   esercizio              = aEs And
                   cd_unita_organizzativa = aCdUo And
                   dt_inizio              = trunc(aDtDa) And
                   dt_fine                = trunc(aDta) And
                   tipo_liquidazione      = aTipoLiquidazione And
                   report_id              = 0;

            --  fine S.F. 10.07.2007
            -- =======================================================================================================

            CNRCTB037.generaDocumento(aManP, aListRigheManP); -- INTRAUE/SAN MARINO SENZ'IVA
          End If; -- Fine "If nvl(recParametriCNR.fl_tesoreria_unica,'N') = 'N' Then")
        End If; -- Fine gestione COMMERCIALE - INTRAUE/SAN MARINO SENZ'IVA
      End If; -- UO NON liquidate via interfaccia (CHIUDE "If not isIvaInterfaccia Then")

      -- Apertura della partita di giro TRONCA sulla UO SAC di versamento se non attiva la tesoreria unica
      aggiornaPraticaGruppoCentro(aLiquidIva, aUOVERSIVA, nvl(recParametriCNR.fl_tesoreria_unica,'N'), aTSNow, aUser);
    Else -- PARTE RELATIVA A LIQUIDAZIONE DEL CENTRO **************** Else di ("If aUOENTE.cd_unita_organizzativa <> aCdUo Then")
      -- =====================
      -- Se l'iva da versare in liquidazione centralizzata ?Â¨ < -26 genero il mandato
      -- =====================

      If aLiquidIva.iva_da_versare <  (-1)*26 Then
        Declare
          Type obbScadTableType Is Table Of obbligazione_scadenzario%Rowtype Index By BINARY_INTEGER;
          obbScadList obbScadTableType;
        Begin
          -- Se si tratta di liquidazione di UO = da UO di versamento:
          --
          -- 1. creo 1 generico con n righe per chiudere lato spesa le partite di giro aperte dalle UO con i versamenti locali
          -- 2. creo un mandato di versamento cumulativo al terzo (ERARIO) recuperato dalla tabella gruppo_cr_det
          --

          -- Recupera il gruppo CORI dell'IVA
          Begin
            Select a.* Into aGruppoCoriDet
            From gruppo_cr_det a, tipo_cr_base b
            Where a.esercizio = aEs
            And   b.esercizio = a.esercizio
            And   b.cd_contributo_ritenuta = aCdCoriIva
            And   b.cd_gruppo_cr = a.cd_gruppo_cr
            And   a.cd_regione = CNRCTB080.REGIONE_INDEFINITA
            And   a.pg_comune = CNRCTB080.COMUNE_INDEFINITO;
          Exception
            When NO_DATA_FOUND Then
              IBMERR001.RAISE_ERR_GENERICO('Dettagli di versamento CORI IVA non trovati');
          End;

          If aGruppoCoriDet.pg_banca Is null Or aGruppoCoriDet.cd_modalita_pagamento Is Null Or aGruppoCoriDet.cd_terzo_versamento Is Null Then
            IBMERR001.RAISE_ERR_GENERICO('Terzo o sue modalità di pagamento non specificate per GRUPPO CORI IVA');
          End If;

          -- Recupera l'anagrafica di tale gruppo
          aAnagTst:=CNRCTB080.GETANAG(aGruppoCoriDet.cd_terzo_versamento);

          aManP.CD_CDS:=aUOVERSIVA.cd_unita_padre;
          aManP.ESERCIZIO:=aEs;
          aManP.CD_UNITA_ORGANIZZATIVA:=aUOVERSIVA.cd_unita_organizzativa;
          aManP.CD_CDS_ORIGINE:=aUOVERSIVA.cd_unita_padre;
          aManP.CD_UO_ORIGINE:=aUOVERSIVA.cd_unita_organizzativa;
          aManP.CD_TIPO_DOCUMENTO_CONT:=CNRCTB018.TI_DOC_MAN;
          aManP.TI_MANDATO:=CNRCTB038.TI_MAN_PAG;
          aManP.TI_COMPETENZA_RESIDUO:=CNRCTB038.TI_MAN_COMP;
          aManP.DS_MANDATO:=getDescLiquidazione(aLiquidIva);
          aManP.STATO:=CNRCTB038.STATO_MAN_EME;
          aManP.DT_EMISSIONE:=TRUNC(aTSNow);
          aManP.IM_RITENUTE:=0;
          --  aManP.DT_TRASMISSIONE:=;
          --  aManP.DT_PAGAMENTO:=;
          --  aManP.DT_ANNULLAMENTO:=;
          aManP.IM_PAGATO:=0;
          If aLiquidIva.tipo_liquidazione = CNRCTB250.TI_LIQ_IVA_ISTSPLIT Then
            aManP.tipo_debito_siope := 'I';
          End If;
          aManP.UTCR:=aUser;
          aManP.DACR:=aTSNow;
          aManP.UTUV:=aUser;
          aManP.DUVA:=aTSNow;
          aManP.PG_VER_REC:=1;
          aManP.STATO_TRASMISSIONE:=CNRCTB038.STATO_MAN_TRASCAS_NODIST;

          aTotMandato:=0;

          aGen:=null;
          -- Creo il documento generico di spesa su partita di giro collegato all'annotazione di entrata su pgiro del contributo ritenuta
          aGen.CD_CDS:=aUOVERSIVA.cd_unita_padre;
          aGen.CD_UNITA_ORGANIZZATIVA:=aUOVERSIVA.cd_unita_organizzativa;
          aGen.ESERCIZIO:=aEs;
          aGen.CD_CDS_ORIGINE:=aUOVERSIVA.cd_unita_padre;
          aGen.CD_UO_ORIGINE:=aUOVERSIVA.cd_unita_organizzativa;
          aGen.CD_TIPO_DOCUMENTO_AMM:=CNRCTB100.TI_GEN_CORI_VER_SPESA;
          aGen.DATA_REGISTRAZIONE:=TRUNC(aTSNow);
          aGen.DS_DOCUMENTO_GENERICO:=getDescLiquidazione(aLiquidIva);
          If aLiquidIva.tipo_liquidazione = CNRCTB250.TI_LIQ_IVA_COMMERC Then
             aGen.TI_ISTITUZ_COMMERC:=CNRCTB100.TI_COMMERCIALE;
          Else
             aGen.TI_ISTITUZ_COMMERC:=CNRCTB100.TI_ISTITUZIONALE;
          End If;
          aGen.STATO_COFI:=CNRCTB100.STATO_GEN_COFI_TOT_MR;
          aGen.STATO_COGE:=CNRCTB100.STATO_COEP_EXC;
          aGen.STATO_COAN:=CNRCTB100.STATO_COEP_EXC;
          aGen.CD_DIVISA:=aDivisaEuro;
          aGen.CAMBIO:=1;
          --  aGen.ESERCIZIO_LETTERA:=0;
          --  aGen.PG_LETTERA:=0;
          aGen.DACR:=aTSNow;
          aGen.UTCR:=aUser;
          aGen.DUVA:=aTSNow;
          aGen.UTUV:=aUser;
          aGen.PG_VER_REC:=1;
          aGen.DT_SCADENZA:=TRUNC(aTSNow);
          aGen.STATO_PAGAMENTO_FONDO_ECO:=CNRCTB100.STATO_NO_PFONDOECO;
          aGen.TI_ASSOCIATO_MANREV:=CNRCTB100.TI_ASSOC_TOT_MAN_REV ;

          If Nvl(recParametriCNR.fl_tesoreria_unica,'N') = 'Y' And aLiquidIva.tipo_liquidazione = CNRCTB250.TI_LIQ_IVA_COMMERC Then
            aEv := getVoceIvaCommerciale(aLiquidIva.esercizio);

            Declare
              aCDRUOVERSIVA v_cdr_valido%rowtype;
              aLASpecVersIva v_linea_attivita_valida%Rowtype;
              aLASpecVersIvaSac v_linea_attivita_valida%Rowtype;
              impObbligazione NUMBER := 0;
              impLiqResiduo NUMBER := 0;
              tipoObbligazione VARCHAR2(10);
              aObb obbligazione%rowtype;
              aObbScad obbligazione_scadenzario%rowtype;
              aObbScadVoce obbligazione_scad_voce%rowtype;
            Begin
              -- Estrazione del CDR SAC di versamento iva
              aCDRUOVERSIVA := getCDRUOVERSIVA(aEsDefault);
              -- Estrazione della linea di attività  SAC da utilizzare per la creazione della variazione
              aLASpecVersIvaSac := getLASPECVERSIVASAC(aEsDefault, aCDRUOVERSIVA);

              impLiqResiduo := Abs(aLiquidIva.iva_da_versare);

              --trovo la disponibilità  residua sulla GAE della SAC
              For rec in (Select * From v_assestato
                          Where esercizio = aEs
                          And   esercizio_res <= aEs
                          And   cd_centro_responsabilita = aCDRUOVERSIVA.CD_CENTRO_RESPONSABILITA
                          And   cd_linea_attivita = aLASpecVersIvaSac.CD_LINEA_ATTIVITA
                          And   cd_elemento_voce = aEv.cd_elemento_voce
                          And   Nvl(importo_disponibile, 0) > 0
                          order by esercizio_res) Loop
                If impLiqResiduo > 0 Then
                  If Nvl(rec.importo_disponibile,0) <= impLiqResiduo Then
                    impObbligazione := Nvl(rec.importo_disponibile,0);
                  Else
                    impObbligazione := impLiqResiduo;
                  End If;

                  If rec.esercizio = rec.esercizio_res Then
                    tipoObbligazione := CNRCTB018.TI_DOC_OBB;
                  Else
                    tipoObbligazione := CNRCTB018.TI_DOC_OBB_RES_IMPRO;
                  End If;

                  createObbligazioneEnte(aEs, aLiquidIva, rec.esercizio_res, impObbligazione, aUOVERSIVA, aEv,
                                         aGruppoCoriDet, aLASpecVersIvaSac, nvl(recParametriCNR.fl_nuovo_pdg,'N'),
                                         tipoObbligazione, aUser, aObb, aObbScad, aObbScadVoce);

                  aDettObbScad(1):=aObbScadVoce;

                  If rec.esercizio = rec.esercizio_res Then
                    CNRCTB030.creaObbligazione(False,aObb,aObbScad,aDettObbScad);
                  Else
                    CNRCTB030.creaObbligazioneResidua(False,aObb,aObbScad,aDettObbScad);
                  End If;

                  obbScadList(obbScadList.count+1) := aObbScad;

                  impLiqResiduo := impLiqResiduo - impObbligazione;
                End If;
              End Loop;

              If impLiqResiduo>0 Then
                IBMERR001.RAISE_ERR_GENERICO('Disponibilità  del CDR '||aCDRUOVERSIVA.CD_CENTRO_RESPONSABILITA||' sulla GAE '||
                  aLASpecVersIvaSac.CD_LINEA_ATTIVITA||' insufficiente di '||Ltrim(Rtrim(To_Char(Abs(impLiqResiduo),'999g999g999g999g990d00')))||
                  ' per effettuare il versamento Iva di '||Ltrim(Rtrim(To_Char(Abs(aLiquidIva.iva_da_versare),'999g999g999g999g990d00'))));
              End If;
            End;
          Else
            aEv := getVoceIvaIstituzionaleEnte(aLiquidIva.esercizio, aCdCoriIva, nvl(recParametriCNR.fl_nuovo_pdg,'N'));
            createObbligazioneEnte(aEs, aLiquidIva, aEs, abs(aLiquidIva.iva_da_versare), aUOVERSIVA, aEv, aGruppoCoriDet, aLASpecIva,
                                   nvl(recParametriCNR.fl_nuovo_pdg,'N'), CNRCTB018.TI_DOC_OBB_PGIRO, aUser,
                                   aObb, aObbScad, aObbScadVoce);
            -- Creo la partita di giro tronca di accantonamento versamenti locali
            CNRCTB030.CREAOBBLIGAZIONEPGIROTRONC(false,aObb,aObbScad,aAccPG,aAccPGScad,trunc(aTSNow));
            obbScadList(obbScadList.count+1) := aObbScad;
          End If;

          For i In 1..obbScadList.count loop
            Dbms_Output.put_line('Obbligazione '||i||' nr:'||obbScadList(i).CD_CDS||obbScadList(i).ESERCIZIO||obbScadList(i).ESERCIZIO_ORIGINALE
            ||obbScadList(i).PG_OBBLIGAZIONE||obbScadList(i).PG_OBBLIGAZIONE_SCADENZARIO||' - IMPORTO: '||obbScadList(i).im_scadenza||
            ' - IMPORTO ASS: '||obbScadList(i).im_associato_doc_amm);

            aGenRiga:=null;

            aGenRiga.CD_CDS:=aGen.CD_CDS;
            aGenRiga.CD_UNITA_ORGANIZZATIVA:=aGen.CD_UNITA_ORGANIZZATIVA;
            aGenRiga.ESERCIZIO:=aGen.ESERCIZIO;
            aGenRiga.CD_TIPO_DOCUMENTO_AMM:=aGen.CD_TIPO_DOCUMENTO_AMM;
            aGenRiga.DS_RIGA:=aGen.DS_DOCUMENTO_GENERICO;
            aGenRiga.IM_RIGA_DIVISA:= obbScadList(i).im_scadenza;
            aGenRiga.IM_RIGA:=obbScadList(i).im_scadenza;
            aGenRiga.CD_TERZO:=aGruppoCoriDet.cd_terzo_versamento;
            aGenRiga.RAGIONE_SOCIALE:=aAnagTst.RAGIONE_SOCIALE;
            aGenRiga.NOME:=aAnagTst.NOME;
            aGenRiga.COGNOME:=aAnagTst.COGNOME;
            aGenRiga.CODICE_FISCALE:=aAnagTst.CODICE_FISCALE;
            aGenRiga.PARTITA_IVA:=aAnagTst.PARTITA_IVA;
            aGenRiga.CD_MODALITA_PAG:=aGruppoCoriDet.CD_MODALITA_PAGAMENTO;
            aGenRiga.PG_BANCA:=aGruppoCoriDet.PG_BANCA;
            --   aGenRiga.CD_TERMINI_PAG:=aCompenso.CD_TERMINI_PAG;
            --   aGenRiga.CD_TERMINI_PAG_UO_CDS:=aCompenso.CD_TERMINI_PAG_UO_CDS;
            --   aGenRiga.PG_BANCA_UO_CDS:=aGen.PG_BANCA_UO_CDS;
            --   aGenRiga.CD_MODALITA_PAG_UO_CDS:=aGen.CD_MODALITA_PAG_UO_CDS;
            --   aGenRiga.NOTE:=aGen.NOTE;
            aGenRiga.DT_DA_COMPETENZA_COGE:=TRUNC(aLiquidIva.dt_inizio);
            aGenRiga.DT_A_COMPETENZA_COGE:=TRUNC(aLiquidIva.dt_fine);
            aGenRiga.STATO_COFI:=aGen.STATO_COFI;
            --   aGenRiga.DT_CANCELLAZIONE:=aGen.DT_CANCELLAZIONE;
            --   aGenRiga.CD_CDS_OBBLIGAZIONE:=aGen.CD_CDS_OBBLIGAZIONE;
            --   aGenRiga.ESERCIZIO_OBBLIGAZIONE:=aGen.ESERCIZIO_OBBLIGAZIONE;
            --   aGenRiga.ESERCIZIO_ORI_OBBLIGAZIONE:=aGen.ESERCIZIO_ORI_OBBLIGAZIONE;
            --   aGenRiga.PG_OBBLIGAZIONE:=aGen.PG_OBBLIGAZIONE;
            --   aGenRiga.PG_OBBLIGAZIONE_SCADENZARIO:=aGen.PG_OBBLIGAZIONE_SCADENZARIO;
            aGenRiga.CD_CDS_OBBLIGAZIONE:=obbScadList(i).CD_CDS;
            aGenRiga.ESERCIZIO_OBBLIGAZIONE:=obbScadList(i).ESERCIZIO;
            aGenRiga.ESERCIZIO_ORI_OBBLIGAZIONE:=obbScadList(i).ESERCIZIO_ORIGINALE;
            aGenRiga.PG_OBBLIGAZIONE:=obbScadList(i).PG_OBBLIGAZIONE;
            aGenRiga.PG_OBBLIGAZIONE_SCADENZARIO:=obbScadList(i).PG_OBBLIGAZIONE_SCADENZARIO;
            aGenRiga.DACR:=aGen.DACR;
            aGenRiga.UTCR:=aGen.UTCR;
            aGenRiga.UTUV:=aGen.UTUV;
            aGenRiga.DUVA:=aGen.DUVA;
            aGenRiga.PG_VER_REC:=aGen.PG_VER_REC;
            aGenRiga.TI_ASSOCIATO_MANREV:=aGen.TI_ASSOCIATO_MANREV;
            aListGenRighe(aListGenRighe.count+1):=aGenRiga;

            -- Generazione righe mandato
            aManPRiga:=null;

            aManPRiga.CD_CDS:=aGen.cd_cds;
            aManPRiga.ESERCIZIO:=aGen.esercizio;
            aManPRiga.ESERCIZIO_OBBLIGAZIONE:=aGenRiga.esercizio_obbligazione;
            aManPRiga.ESERCIZIO_ORI_OBBLIGAZIONE:=aGenRiga.esercizio_ori_obbligazione;
            aManPRiga.PG_OBBLIGAZIONE:=aGenRiga.pg_obbligazione;
            aManPRiga.PG_OBBLIGAZIONE_SCADENZARIO:=aGenRiga.pg_obbligazione_scadenzario;
            aManPRiga.CD_CDS_DOC_AMM:=aGen.cd_cds;
            aManPRiga.CD_UO_DOC_AMM:=aGen.cd_unita_organizzativa;
            aManPRiga.ESERCIZIO_DOC_AMM:=aGen.esercizio;
            aManPRiga.CD_TIPO_DOCUMENTO_AMM:=aGen.cd_tipo_documento_amm;
            aManPRiga.PG_DOC_AMM:=aGen.pg_documento_generico;
            aManPRiga.DS_MANDATO_RIGA:=aManP.ds_mandato;
            aManPRiga.STATO:=aManP.stato;
            aManPRiga.CD_TERZO:=aGenRiga.cd_terzo;
            aManPRiga.PG_BANCA:=aGenRiga.pg_banca;
            aManPRiga.CD_MODALITA_PAG:=aGenRiga.cd_modalita_pag;
            aManPRiga.IM_MANDATO_RIGA:=aGenRiga.im_riga;
            aManPRiga.IM_RITENUTE_RIGA:=0;

            If Nvl(recParametriCNR.fl_tesoreria_unica,'N') = 'Y' And aLiquidIva.tipo_liquidazione = CNRCTB250.TI_LIQ_IVA_COMMERC Then
              aManPRiga.FL_PGIRO:='N';
            Else
              aManPRiga.FL_PGIRO:='Y';
            End If;

            aManPRiga.UTCR:=aUser;
            aManPRiga.DACR:=aTSNow;
            aManPRiga.UTUV:=aUser;
            aManPRiga.DUVA:=aTSNow;
            aManPRiga.PG_VER_REC:=1;
            aTotMandato:=aTotMandato+aManPRiga.im_mandato_riga;
            aListRigheManP(aListRigheManP.count+1):=aManPRiga;
          End Loop;

          aGen.IM_TOTALE:=aTotMandato;
          CNRCTB110.CREAGENERICOAGGOBBACC(aGen,aListGenRighe);

          For I In 1 .. aListRigheManP.COUNT Loop
            aListRigheManP(I).CD_CDS_DOC_AMM:=aGen.cd_cds;
            aListRigheManP(I).CD_UO_DOC_AMM:=aGen.cd_unita_organizzativa;
            aListRigheManP(I).ESERCIZIO_DOC_AMM:=aGen.esercizio;
            aListRigheManP(I).CD_TIPO_DOCUMENTO_AMM:=aGen.cd_tipo_documento_amm;
            aListRigheManP(I).PG_DOC_AMM:=aGen.pg_documento_generico;
          End Loop;

          aManP.IM_MANDATO := aTotMandato;

          /* STANI 08.07.2007 L'UPDATE PRIMA ERA DOPO LA GENERAZIONE DEL DOCUMENTO, HO DOVUTO SPOSTARLO PERCHE' IL CODICE
                      SIOPE SPECIALE (DA CONFIGURAZIONE CNR) LO DERIVA DALLA ASSOCIAZIONE DEL DOCUMENTO A
                      LIQUIDAZIONE_IVA (CHE OVVIAMENTE NON TROVAVA) */

          Update liquidazione_iva
          Set cd_cds_obb_accentr = obbScadList(1).cd_cds,
              esercizio_obb_accentr = obbScadList(1).esercizio,
              esercizio_ori_obb_accentr = obbScadList(1).esercizio_originale,
              pg_obb_accentr = obbScadList(1).pg_obbligazione,
              cd_tipo_documento = aGen.cd_tipo_documento_amm,
              esercizio_doc_amm = aGen.esercizio,
              cd_cds_doc_amm = aGen.cd_cds,
              cd_uo_doc_amm = aGen.cd_unita_organizzativa,
              pg_doc_amm = aGen.pg_documento_generico,
              duva = aTSNow,
              utuv = aUser,
              pg_ver_rec = pg_ver_rec + 1
          Where cd_cds = aCdCds
          And   esercizio = aEs
          And   cd_unita_organizzativa = aCdUo
          And   dt_inizio = trunc(aDtDa)
          And   dt_fine = trunc(aDta)
          And   tipo_liquidazione = aTipoLiquidazione
          And   report_id = 0;

          CNRCTB037.generaDocumento(aManP,aListRigheManP);
        End;
      End If; -- Fine caso importo da versare < -26 euro
    End If; -- Fine di ("If aUOENTE.cd_unita_organizzativa <> aCdUo Then")
  End;

 procedure ins_LIQUIDAZIONE_IVA_CENTRO (aDest LIQUIDAZIONE_IVA_CENTRO%rowtype) is
  begin
   insert into LIQUIDAZIONE_IVA_CENTRO (
     ESERCIZIO
    ,TIPO_LIQUIDAZIONE
    ,DT_INIZIO
    ,DT_FINE
    ,STATO
    ,CD_CDS_OBB_ACCENTR
    ,ESERCIZIO_OBB_ACCENTR
    ,ESERCIZIO_ORI_OBB_ACCENTR
    ,PG_OBB_ACCENTR
    ,CD_CDS_LI
    ,CD_UO_LI
    ,REPORT_ID_LI
    ,DACR
    ,UTCR
    ,DUVA
    ,UTUV
    ,PG_VER_REC
   ) values (
     aDest.ESERCIZIO
    ,aDest.TIPO_LIQUIDAZIONE
    ,aDest.DT_INIZIO
    ,aDest.DT_FINE
    ,aDest.STATO
    ,aDest.CD_CDS_OBB_ACCENTR
    ,aDest.ESERCIZIO_OBB_ACCENTR
    ,aDest.ESERCIZIO_ORI_OBB_ACCENTR
    ,aDest.PG_OBB_ACCENTR
    ,aDest.CD_CDS_LI
    ,aDest.CD_UO_LI
    ,aDest.REPORT_ID_LI
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.DUVA
    ,aDest.UTUV
    ,aDest.PG_VER_REC
    );
 end;

 Procedure ctrlEsistenzaVariazioniIva(aCdCds VARCHAR2,aEs NUMBER,aTipo VARCHAR2) Is
   messageCmp  VARCHAR2(10000);
   messageRes  VARCHAR2(10000);
   messageErr  VARCHAR2(10000);
   contaVar    NUMBER:=0;
 Begin
   If aTipo In ('C','T') Then
     For rec In (Select B.PG_VARIAZIONE_PDG, B.CD_CENTRO_RESPONSABILITA
                 From PDG_VARIAZIONE_RIGA_GEST A, PDG_VARIAZIONE B
                 Where A.ESERCIZIO = aEs
                 And   A.CD_CDR_ASSEGNATARIO In (Select CD_CENTRO_RESPONSABILITA
                                                 From V_STRUTTURA_ORGANIZZATIVA
                                                 Where CD_CDS = aCdCds
                                                 And   CD_TIPO_LIVELLO = 'CDR'
                                                 And   ESERCIZIO = aEs)
                 And   A.CD_LINEA_ATTIVITA = CNRCTB015.GETVAL01PERCHIAVE(LINEA_ATTIVITA_SPECIALE,LA_VER_IVA)
                 And   A.ESERCIZIO = B.ESERCIZIO
                 And   A.PG_VARIAZIONE_PDG = B.PG_VARIAZIONE_PDG) Loop
        contaVar := contaVar + 1;
        If messageCmp Is Not Null Then
          messageCmp := messageCmp||', ';
        End If;
        messageCmp := messageCmp||'n.'||rec.PG_VARIAZIONE_PDG||' del CDR '||rec.CD_CENTRO_RESPONSABILITA;
     End Loop;
   End If;

   If aTipo In ('R','T') Then
     For rec In (Select B.PG_VARIAZIONE, B.CD_CENTRO_RESPONSABILITA
                 From VAR_STANZ_RES_RIGA A, VAR_STANZ_RES B
                 Where A.ESERCIZIO = aEs
                 And   A.CD_CDR In (Select CD_CENTRO_RESPONSABILITA
                                    From V_STRUTTURA_ORGANIZZATIVA
                                    Where CD_CDS = aCdCds
                                    And   CD_TIPO_LIVELLO = 'CDR'
                                    And   ESERCIZIO = aEs)
                 And   A.CD_LINEA_ATTIVITA = CNRCTB015.GETVAL01PERCHIAVE(LINEA_ATTIVITA_SPECIALE,LA_VER_IVA)
                 And   A.ESERCIZIO = B.ESERCIZIO
                 And   A.PG_VARIAZIONE = B.PG_VARIAZIONE) Loop
        contaVar := contaVar + 1;
        If messageRes Is Not Null Then
          messageRes := messageRes||', ';
        End If;
        messageRes := messageRes||'n.'||rec.PG_VARIAZIONE||' del CDR '||rec.CD_CENTRO_RESPONSABILITA;
      End Loop;
   End If;

   If messageCmp Is Not Null Or messageRes Is Not Null Then
     messageErr := 'Registrazione non possibile.\n';
     If contaVar = 1 Then
       messageErr := messageErr||'La variazione di ';
     Else
       messageErr := messageErr||'Le variazioni di ';
     End If;

     If messageCmp Is Not Null Then
       messageErr := messageErr||'competenza '||messageCmp;
       If messageRes Is Not Null Then
         messageErr := messageErr||' e di ';
       End If;
     End If;

     If messageRes Is Not Null Then
       messageErr := messageErr||'residuo '||messageRes;
     End If;

     If contaVar = 1 Then
       messageErr := messageErr||' grava ';
     Else
       messageErr := messageErr||' gravano ';
     End If;
     messageErr := messageErr||'ancora sulla G.A.E. dell''IVA.';
     IBMERR001.RAISE_ERR_GENERICO(messageErr);
   End If;
 End;

 Procedure controllaAssunzioneImpegni(aCdCds VARCHAR2,aEs NUMBER) Is
   esiste Number;
   aObb Obbligazione%rowtype;
   BLOCCO_ASS_IMP_IVA CHAR(1);
 Begin
   Begin
     Select FL_BLOCCO_ASS_IMP_IVA Into BLOCCO_ASS_IMP_IVA
     From PARAMETRI_CDS
     Where Cd_cds = aCdCds
       And Esercizio = aEs;
   Exception
     When No_Data_Found Then
        IBMERR001.RAISE_ERR_GENERICO('Parametri CDS ('||aCdCds||') mancanti per l''anno '||aEs);
   End;
   If BLOCCO_ASS_IMP_IVA = 'Y' Then
     ctrlEsistenzaVariazioniIva(aCdCds, aEs, 'T');

     Select Count(1) Into esiste
     From VOCE_F_SALDI_CDR_LINEA
     Where ESERCIZIO = aEs
       And ESERCIZIO_RES = aEs
       And CD_CENTRO_RESPONSABILITA In (Select CD_CENTRO_RESPONSABILITA
                                        From V_STRUTTURA_ORGANIZZATIVA
                                        Where CD_CDS = aCdCds
                                          And CD_TIPO_LIVELLO = 'CDR'
                                          And ESERCIZIO = aEs)
       And CD_LINEA_ATTIVITA = CNRCTB015.GETVAL01PERCHIAVE(LINEA_ATTIVITA_SPECIALE,LA_VER_IVA)
       And TI_APPARTENENZA = 'D'
       And TI_GESTIONE = 'S'
       And IM_OBBL_ACC_COMP != 0;
       If Esiste > 0 Then
        Begin
         Select o.* Into aObb
         From Obbligazione o, obbligazione_scadenzario obs, obbligazione_scad_voce obsv
         Where o.cd_cds = obs.cd_cds
           And o.esercizio = obs.esercizio
           And o.esercizio_originale = obs.esercizio_originale
           And o.pg_obbligazione = obs.pg_obbligazione
           And obs.cd_cds = obsv.cd_cds
           And obs.esercizio = obsv.esercizio
           And obs.esercizio_originale = obsv.esercizio_originale
           And obs.pg_obbligazione = obsv.pg_obbligazione
           And obs.pg_obbligazione_scadenzario = obsv.pg_obbligazione_scadenzario
           And obsv.ti_appartenenza = 'D'
           And obsv.ti_gestione = 'S'
           And obsv.cd_centro_responsabilita In (Select CD_CENTRO_RESPONSABILITA
                                                 From V_STRUTTURA_ORGANIZZATIVA
                                                 Where CD_CDS = aCdCds
                                                   And CD_TIPO_LIVELLO = 'CDR'
                                                   And ESERCIZIO = aEs)
           And obsv.esercizio = aEs
           And obsv.esercizio_originale = aEs
           And obsv.cd_linea_attivita = CNRCTB015.GETVAL01PERCHIAVE(LINEA_ATTIVITA_SPECIALE,LA_VER_IVA);
         IBMERR001.RAISE_ERR_GENERICO('Registrazione non possibile.\nL''impegno n.'||aObb.pg_obbligazione||' della UO '||aObb.cd_unita_organizzativa||' grava ancora sulla G.A.E. dell''IVA.');
        Exception
          When No_Data_Found Then
            Null;
          When Too_Many_Rows Then
         IBMERR001.RAISE_ERR_GENERICO('Registrazione non possibile.\nEsistono più impegni che gravano ancora sulla G.A.E. dell''IVA.');
        End;
       End If;
   End If;
 End;

 Procedure controllaAssunzioneImpResImpro(aCdCds VARCHAR2,aEs NUMBER) Is
   esiste Number;
   aObb Obbligazione%rowtype;
   BLOCCO_ASS_IMP_IVA CHAR(1);
 Begin
   Begin
     Select FL_BLOCCO_ASS_IMP_IVA Into BLOCCO_ASS_IMP_IVA
     From PARAMETRI_CDS
     Where Cd_cds = aCdCds
       And Esercizio = aEs;
   Exception
     When No_Data_Found Then
        IBMERR001.RAISE_ERR_GENERICO('Parametri CDS ('||aCdCds||') mancanti per l''anno '||aEs);
   End;
   If BLOCCO_ASS_IMP_IVA = 'Y' Then
     ctrlEsistenzaVariazioniIva(aCdCds, aEs, 'T');

     Select Count(1) Into esiste
     From VOCE_F_SALDI_CDR_LINEA
     Where ESERCIZIO = aEs
       And ESERCIZIO_RES < aEs
       And CD_CENTRO_RESPONSABILITA In (Select CD_CENTRO_RESPONSABILITA
                                        From V_STRUTTURA_ORGANIZZATIVA
                                        Where CD_CDS = aCdCds
                                          And CD_TIPO_LIVELLO = 'CDR'
                                          And ESERCIZIO = aEs)
       And CD_LINEA_ATTIVITA = CNRCTB015.GETVAL01PERCHIAVE(LINEA_ATTIVITA_SPECIALE,LA_VER_IVA)
       And TI_APPARTENENZA = 'D'
       And TI_GESTIONE = 'S'
       And IM_OBBL_RES_IMP != 0;
       If Esiste > 0 Then
        Begin
         Select o.* Into aObb
         From Obbligazione o, obbligazione_scadenzario obs, obbligazione_scad_voce obsv
         Where o.cd_cds = obs.cd_cds
           And o.esercizio = obs.esercizio
           And o.esercizio_originale = obs.esercizio_originale
           And o.pg_obbligazione = obs.pg_obbligazione
           And obs.cd_cds = obsv.cd_cds
           And obs.esercizio = obsv.esercizio
           And obs.esercizio_originale = obsv.esercizio_originale
           And obs.pg_obbligazione = obsv.pg_obbligazione
           And obs.pg_obbligazione_scadenzario = obsv.pg_obbligazione_scadenzario
           And obsv.ti_appartenenza = 'D'
           And obsv.ti_gestione = 'S'
           And obsv.cd_centro_responsabilita In (Select CD_CENTRO_RESPONSABILITA
                                                 From V_STRUTTURA_ORGANIZZATIVA
                                                 Where CD_CDS = aCdCds
                                                  And CD_TIPO_LIVELLO = 'CDR'
                                                   And ESERCIZIO = aEs)
           And obsv.esercizio = aEs
           And obsv.esercizio_originale < aEs
           And obsv.cd_linea_attivita = CNRCTB015.GETVAL01PERCHIAVE(LINEA_ATTIVITA_SPECIALE,LA_VER_IVA);
         IBMERR001.RAISE_ERR_GENERICO('Registrazione non possibile.\nL''impegno residuo n.'||aObb.esercizio_originale||'/'||aObb.pg_obbligazione||' della UO '||aObb.cd_unita_organizzativa||' grava ancora sulla G.A.E. dell''IVA.');
        Exception
          When No_Data_Found Then
            Null;
          When Too_Many_Rows Then
         IBMERR001.RAISE_ERR_GENERICO('Registrazione non possibile.\nEsistono più impegni residui che gravano ancora sulla G.A.E. dell''IVA.');
        End;
       End If;
   End If;
 End;

End;
