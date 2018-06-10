--------------------------------------------------------
--  DDL for Package Body UTIL_VARIAZIONI
--------------------------------------------------------

CREATE OR REPLACE PACKAGE BODY "UTIL_VARIAZIONI" AS
 Function getGae(aEs INTEGER,
 aTiGestione elemento_voce.ti_gestione%type,
 aCDRUO cdr%rowtype,
 aCdGae linea_attivita.cd_linea_attivita%type)
 Return v_linea_attivita_valida%Rowtype
 Is
   aLA v_linea_attivita_valida%Rowtype;
 Begin
   Select a.* Into aLA
   From v_linea_attivita_valida a
   Where a.esercizio = aEs
   And   a.cd_centro_responsabilita = aCdRuo.cd_centro_responsabilita
   And   a.cd_linea_attivita = aCdGae
   And   a.ti_gestione = aTiGestione;

   Return aLA;
 Exception
   When NO_DATA_FOUND Then
     IBMERR001.RAISE_ERR_GENERICO('Linea di attivitא non trovata ('||aCDRUO.cd_centro_responsabilita||'/'||aCdGae||') per l''esercizio '||to_char(aEs));
 End getGae;

 Function getVoceF(
 aVoce elemento_voce%Rowtype,
 aLa v_linea_attivita_valida%Rowtype)
 Return voce_f.cd_voce%type
 Is
   aCdVoceF voce_f.cd_voce%type;
 Begin
   aCdVoceF := CNRCTB053.getVoce_FdaEV(aVoce.esercizio, aVoce.TI_APPARTENENZA, aVoce.TI_GESTIONE,
                                     aVoce.CD_ELEMENTO_VOCE, aLa.cd_centro_responsabilita,
                                     aLa.cd_linea_attivita);
   If aCdVoceF is null Then
     IBMERR001.RAISE_ERR_GENERICO('VoceF non trovata ('||
aVoce.TI_APPARTENENZA||'/'||aVoce.TI_GESTIONE||'/'||aVoce.CD_ELEMENTO_VOCE||') per l''esercizio '||to_char(aVoce.esercizio)||' e per il CDR/GAE '||aLa.cd_centro_responsabilita||'/'||
aLa.cd_linea_attivita);
   End If;
   Return aCdVoceF;
 End getVoceF;

 Function getElementoVoce(aEs INTEGER,
 aTiGestione elemento_voce.ti_gestione%type,
 aTiAppartenenza elemento_voce.ti_appartenenza%type,
 aCdElementoVoce elemento_voce.cd_elemento_voce%type)
 Return elemento_voce%Rowtype
 Is
   aElementoVoce elemento_voce%Rowtype;
 Begin
   Select a.* Into aElementoVoce
   From elemento_voce a
   Where a.esercizio = aEs
   And   a.ti_gestione = aTiGestione
   And   a.ti_appartenenza = aTiAppartenenza
   and   a.cd_elemento_voce = aCdElementoVoce;

   Return aElementoVoce;
 Exception
   When NO_DATA_FOUND Then
     IBMERR001.RAISE_ERR_GENERICO('Elemento Voce non trovato ('||
aTiAppartenenza||'/'||aTiGestione||'/'||aCdElementoVoce||') per l''esercizio '||to_char(aEs));
 End getElementoVoce;

 Procedure generaStornoTraCdsMonoEv
   (aEs NUMBER,
    aEsRes NUMBER,
    aDsVar Varchar2,
    aFonti Varchar2,
    aTiGestione elemento_voce.ti_gestione%type,
    aCdCdrMeno In VARCHAR2,
    aTiAppVoceMeno elemento_voce.ti_appartenenza%type,
    aCdVoceMeno elemento_voce.cd_elemento_voce%type,
    aCdGaeMeno linea_attivita.cd_linea_attivita%type,
    aCdCdrPiu VARCHAR2,
    aTiAppVocePiu elemento_voce.ti_appartenenza%type,
    aCdVocePiu elemento_voce.cd_elemento_voce%type,
    aCdGaePiu linea_attivita.cd_linea_attivita%type,
    aImportoVar ass_pdg_variazione_cdr.IM_SPESA%type,
    aUser VARCHAR2,
    oEsVar Out NUMBER,
    oPgVar Out NUMBER) Is

   aPdgVariazione  pdg_variazione%rowtype;
   aAssPdgVariazioneCdr1 ass_pdg_variazione_cdr%Rowtype;
   aAssPdgVariazioneCdr2 ass_pdg_variazione_cdr%Rowtype;
   aPdgVariazioneRiga1 pdg_variazione_riga_gest%rowtype;
   aPdgVariazioneRiga2 pdg_variazione_riga_gest%rowtype;

   aVarStanzRes  var_stanz_res%rowtype;
   aAssVarStanzResCdr1 ass_var_stanz_res_cdr%Rowtype;
   aAssVarStanzResCdr2 ass_var_stanz_res_cdr%Rowtype;
   aVarStanzResRiga1 var_stanz_res_riga%rowtype;
   aVarStanzResRiga2 var_stanz_res_riga%rowtype;

   lPgVariazione numerazione_base.cd_corrente%type;
   aCDRMeno cdr%rowtype;
   aCDRPiu cdr%rowtype;

   aLAMeno v_linea_attivita_valida%Rowtype;
   aLAPiu v_linea_attivita_valida%Rowtype;

   aCdVoceFMeno voce_f.cd_voce%type;
   aCdVoceFPiu voce_f.cd_voce%type;

   aVoceMeno elemento_voce%Rowtype;
   aVocePiu elemento_voce%Rowtype;

   aVoceMenoNew elemento_voce%Rowtype;
   aVocePiuNew elemento_voce%Rowtype;

   isNuovoPdg boolean := false;
   recParametriCNR parametri_cnr%rowtype;
   aTSNow date;

   Function getDeltaSaldo(saldoLinea Voce_f_saldi_cdr_linea%Rowtype, aUser VARCHAR2)
   Return Voce_f_saldi_cdr_linea%Rowtype
   Is
     aDeltaSaldo voce_f_saldi_cdr_linea%Rowtype;
   Begin
     aDeltaSaldo.esercizio := saldoLinea.esercizio;
     aDeltaSaldo.esercizio_res := saldoLinea.esercizio_res;
     aDeltaSaldo.cd_centro_responsabilita := saldoLinea.cd_centro_responsabilita;
     aDeltaSaldo.cd_linea_attivita := saldoLinea.cd_linea_attivita;
     aDeltaSaldo.ti_appartenenza := saldoLinea.ti_appartenenza;
     aDeltaSaldo.ti_gestione := saldoLinea.ti_gestione;
     aDeltaSaldo.cd_elemento_voce := saldoLinea.cd_elemento_voce;
     aDeltaSaldo.cd_voce := saldoLinea.cd_voce;

     aDeltaSaldo.UTUV := aUser;

     CNRCTB054.RESET_IMPORTI_SALDI(aDeltaSaldo);
     Return aDeltaSaldo;
   End getDeltaSaldo;

   Function getOrCreateSaldi(aEs NUMBER, aEsRes NUMBER, aCdCdr VARCHAR2, aCdGae VARCHAR2, aTiApp VARCHAR2, aTiGes VARCHAR2, aCdElementoVoce VARCHAR2, aCdVoce VARCHAR2)
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
       And   cd_elemento_voce = aCdElementoVoce
       And   cd_voce = aCdVoce;
     Exception
       When No_Data_Found Then
         recSaldi.esercizio := aEs;
         recSaldi.esercizio_res := aEsRes;
         recSaldi.cd_centro_responsabilita := aCdCdr;
         recSaldi.cd_linea_attivita := aCdGae;
         recSaldi.ti_appartenenza := aTiApp;
         recSaldi.ti_gestione := aTiGes;
         recSaldi.cd_elemento_voce := aCdElementoVoce;
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
     End;
     Return recSaldi;
   End getOrCreateSaldi;

   Function getOrCreateSaldiNew(recSaldiLinea Voce_f_saldi_cdr_linea%Rowtype)
   Return Voce_f_saldi_cdr_linea%Rowtype
   Is
     recSaldi voce_f_saldi_cdr_linea%Rowtype;
     parametri_esercizio parametri_cnr%rowtype:=null;
     parametri_esercizioNew parametri_cnr%rowtype:=null;
     ev_old  elemento_voce%rowtype:=null;
     ev_new  elemento_voce%rowtype:=null;
     vocef   voce_f%Rowtype;
   Begin
     If CNRCTB048.getcdsribaltato (recSaldiLinea.ESERCIZIO, CNRUTL001.GETCDSFROMCDR(recSaldiLinea.cd_centro_responsabilita)) = 'Y' Then
        parametri_esercizio:=CNRUTL001.getRecParametriCnr(recSaldiLinea.ESERCIZIO);
        parametri_esercizioNew:=CNRUTL001.getRecParametriCnr(recSaldiLinea.ESERCIZIO+1);

        If (parametri_esercizio.fl_nuovo_pdg='N' and parametri_esercizioNew.fl_nuovo_pdg='Y') Then
          Select * Into vocef
          From voce_f
          Where esercizio = recSaldiLinea.ESERCIZIO
          And   ti_gestione = recSaldiLinea.ti_gestione
          And   ti_appartenenza = recSaldiLinea.ti_appartenenza
          And   cd_voce = recSaldiLinea.cd_voce;

          Select * Into ev_old
          From elemento_voce
          Where esercizio =recSaldiLinea.ESERCIZIO
          And   ti_gestione = recSaldiLinea.ti_gestione
          And   ti_appartenenza = recSaldiLinea.ti_appartenenza
          And   cd_elemento_voce = vocef.cd_elemento_voce;

          ev_new :=cnrctb046.getElementoVoceNew(ev_old);

          Return getOrCreateSaldi(recSaldiLinea.ESERCIZIO+1, recSaldiLinea.esercizio_res, recSaldiLinea.cd_centro_responsabilita,
                                  recSaldiLinea.CD_LINEA_ATTIVITA, recSaldiLinea.TI_APPARTENENZA, recSaldiLinea.TI_GESTIONE,
                                  ev_new.cd_elemento_voce, ev_new.cd_elemento_voce);
        End If;
     End If;
     Return Null;
   End getOrCreateSaldiNew;
 Begin
   Dbms_Output.put_line('generaStornoTraCdsMonoEv');

   aTSNow := trunc(sysdate);
   isNuovoPdg := recParametriCNR.fl_nuovo_pdg is not null and recParametriCNR.fl_nuovo_pdg='Y';

   recParametriCNR := CNRUTL001.getRecParametriCnr(aEs);

   -- Estrazione del CDR della UO di versamento iva
   aCDRMeno := CNRCTB020.getCDRValido(aEs, aCdCdrMeno);
   aCDRPiu := CNRCTB020.getCDRValido(aEs, aCdCdrPiu);

   -- Estrazione della linea di attivitא comune da utilizzare per la creazione della variazione
   aLAMeno := getGae(aEs, aTiGestione, aCDRMeno, aCdGaeMeno);
   aLAPiu := getGae(aEs, aTiGestione, aCDRPiu, aCdGaePiu);

   aVoceMeno := getElementoVoce(aEs, aTiGestione, aTiAppVoceMeno, aCdVoceMeno);
   aVocePiu := getElementoVoce(aEs, aTiGestione, aTiAppVocePiu, aCdVocePiu);

   If not isNuovoPdg Then
     aCdVoceFMeno := getVoceF(aVoceMeno, aLAMeno);
     aCdVoceFPiu := getVoceF(aVocePiu, aLAPiu);
   End If;

   dbms_output.put_line(aCdVoceFMeno);
   dbms_output.put_line(aCdVoceFPiu);

   If aEs = aEsRes Then
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
     aPdgVariazione.CD_CENTRO_RESPONSABILITA:=aCDRMeno.cd_centro_responsabilita;
     aPdgVariazione.DT_APERTURA:=aTSNow;
     aPdgVariazione.DS_VARIAZIONE:=aDsVar;
     aPdgVariazione.RIFERIMENTI:=aDsVar;
     aPdgVariazione.DS_DELIBERA:='.';
     aPdgVariazione.STATO:='APP';
     aPdgVariazione.DT_APERTURA:=Trunc(Sysdate);
     aPdgVariazione.DT_CHIUSURA:=Trunc(Sysdate);
     aPdgVariazione.DT_APPROVAZIONE:=Trunc(Sysdate);
     aPdgVariazione.TIPOLOGIA:='STO_S_TOT';
     aPdgVariazione.TIPOLOGIA_FIN:=aFonti;
     aPdgVariazione.FL_VISTO_DIP_VARIAZIONI:='N';
     aPdgVariazione.DACR:=aTSNow;
     aPdgVariazione.UTCR:=aUser;
     aPdgVariazione.DUVA:=aTSNow;
     aPdgVariazione.UTUV:=aUser;
     aPdgVariazione.PG_VER_REC:=1;

     --Riga 1 del CDR da cui togliere
     aAssPdgVariazioneCdr1.ESERCIZIO:=aEs;
     aAssPdgVariazioneCdr1.PG_VARIAZIONE_PDG := lPgVariazione;
     aAssPdgVariazioneCdr1.CD_CENTRO_RESPONSABILITA:=aPdgVariazione.CD_CENTRO_RESPONSABILITA;
     aAssPdgVariazioneCdr1.IM_ENTRATA := 0;
     aAssPdgVariazioneCdr1.IM_SPESA := -abs(aImportoVar);
     aAssPdgVariazioneCdr1.DACR:=aTSNow;
     aAssPdgVariazioneCdr1.UTCR:=aUser;
     aAssPdgVariazioneCdr1.DUVA:=aTSNow;
     aAssPdgVariazioneCdr1.UTUV:=aUser;
     aAssPdgVariazioneCdr1.PG_VER_REC:=1;

     aPdgVariazioneRiga1.ESERCIZIO := aEs;
     aPdgVariazioneRiga1.PG_VARIAZIONE_PDG := lPgVariazione;
     aPdgVariazioneRiga1.PG_RIGA := 1;
     aPdgVariazioneRiga1.CD_CDR_ASSEGNATARIO := aAssPdgVariazioneCdr1.CD_CENTRO_RESPONSABILITA;
     aPdgVariazioneRiga1.CD_LINEA_ATTIVITA := aLAMeno.cd_linea_attivita;
     aPdgVariazioneRiga1.CD_CDS_AREA := CNRUTL001.GETCDSFROMCDR(aAssPdgVariazioneCdr1.CD_CENTRO_RESPONSABILITA);
     aPdgVariazioneRiga1.TI_APPARTENENZA := aVoceMeno.TI_APPARTENENZA;
     aPdgVariazioneRiga1.TI_GESTIONE := aVoceMeno.TI_GESTIONE;
     aPdgVariazioneRiga1.CD_ELEMENTO_VOCE := aVoceMeno.CD_ELEMENTO_VOCE;
     aPdgVariazioneRiga1.DT_REGISTRAZIONE := trunc(aTSNow);
     aPdgVariazioneRiga1.CATEGORIA_DETTAGLIO := 'DIR';
     aPdgVariazioneRiga1.IM_SPESE_GEST_DECENTRATA_INT := 0;
     aPdgVariazioneRiga1.IM_SPESE_GEST_DECENTRATA_EST := -abs(aImportoVar);
     aPdgVariazioneRiga1.IM_SPESE_GEST_ACCENTRATA_INT := 0;
     aPdgVariazioneRiga1.IM_SPESE_GEST_ACCENTRATA_EST := 0;
     aPdgVariazioneRiga1.IM_ENTRATA := 0;
     aPdgVariazioneRiga1.FL_VISTO_DIP_VARIAZIONI := 'N';
     aPdgVariazioneRiga1.DACR:=aTSNow;
     aPdgVariazioneRiga1.UTCR:=aUser;
     aPdgVariazioneRiga1.DUVA:=aTSNow;
     aPdgVariazioneRiga1.UTUV:=aUser;
     aPdgVariazioneRiga1.PG_VER_REC:=1;

     --Riga 2 del CDR a cui dare
     aAssPdgVariazioneCdr2.ESERCIZIO:=aEs;
     aAssPdgVariazioneCdr2.PG_VARIAZIONE_PDG := lPgVariazione;
     aAssPdgVariazioneCdr2.CD_CENTRO_RESPONSABILITA:=aCDRPiu.CD_CENTRO_RESPONSABILITA;
     aAssPdgVariazioneCdr2.IM_ENTRATA := 0;
     aAssPdgVariazioneCdr2.IM_SPESA := abs(aImportoVar);
     aAssPdgVariazioneCdr2.DACR:=aTSNow;
     aAssPdgVariazioneCdr2.UTCR:=aUser;
     aAssPdgVariazioneCdr2.DUVA:=aTSNow;
     aAssPdgVariazioneCdr2.UTUV:=aUser;
     aAssPdgVariazioneCdr2.PG_VER_REC:=1;

     aPdgVariazioneRiga2.ESERCIZIO := aEs;
     aPdgVariazioneRiga2.PG_VARIAZIONE_PDG := lPgVariazione;
     aPdgVariazioneRiga2.PG_RIGA := 2;
     aPdgVariazioneRiga2.CD_CDR_ASSEGNATARIO := aAssPdgVariazioneCdr2.CD_CENTRO_RESPONSABILITA;
     aPdgVariazioneRiga2.CD_LINEA_ATTIVITA := aLAPiu.CD_LINEA_ATTIVITA;
     aPdgVariazioneRiga2.CD_CDS_AREA := CNRUTL001.GETCDSFROMCDR(aAssPdgVariazioneCdr2.CD_CENTRO_RESPONSABILITA);
     aPdgVariazioneRiga2.TI_APPARTENENZA := aVocePiu.TI_APPARTENENZA;
     aPdgVariazioneRiga2.TI_GESTIONE := aVocePiu.TI_GESTIONE;
     aPdgVariazioneRiga2.CD_ELEMENTO_VOCE := aVocePiu.CD_ELEMENTO_VOCE;
     aPdgVariazioneRiga2.DT_REGISTRAZIONE := trunc(aTSNow);
     aPdgVariazioneRiga2.CATEGORIA_DETTAGLIO := 'DIR';
     aPdgVariazioneRiga2.IM_SPESE_GEST_DECENTRATA_INT := 0;
     aPdgVariazioneRiga2.IM_SPESE_GEST_DECENTRATA_EST := abs(aImportoVar);
     aPdgVariazioneRiga2.IM_SPESE_GEST_ACCENTRATA_INT := 0;
     aPdgVariazioneRiga2.IM_SPESE_GEST_ACCENTRATA_EST := 0;
     aPdgVariazioneRiga2.IM_ENTRATA := 0;
     aPdgVariazioneRiga2.FL_VISTO_DIP_VARIAZIONI := 'N';
     aPdgVariazioneRiga2.DACR:=aTSNow;
     aPdgVariazioneRiga2.UTCR:=aUser;
     aPdgVariazioneRiga2.DUVA:=aTSNow;
     aPdgVariazioneRiga2.UTUV:=aUser;
     aPdgVariazioneRiga2.PG_VER_REC:=1;

     CNRCTB051.ins_PDG_VARIAZIONE(aPdgVariazione);

     CNRCTB050.ins_ASS_PDG_VARIAZIONE_CDR(aAssPdgVariazioneCdr1);
     CNRCTB051.ins_PDG_VARIAZIONE_RIGA_GEST(aPdgVariazioneRiga1);

     CNRCTB050.ins_ASS_PDG_VARIAZIONE_CDR(aAssPdgVariazioneCdr2);
     CNRCTB051.ins_PDG_VARIAZIONE_RIGA_GEST(aPdgVariazioneRiga2);

     oEsVar := aPdgVariazione.ESERCIZIO;
     oPgVar := aPdgVariazione.PG_VARIAZIONE_PDG;

     --Aggiorno il numeratore
     Update numerazione_base
     Set cd_corrente = lPgVariazione
     Where esercizio = aEs
     And tabella = 'PDG_VARIAZIONE'
     And colonna = 'PG_VARIAZIONE_PDG';

     --Aggiorno i saldi linea 1
     Declare
       recAssestato v_assestato%Rowtype;
       recSaldiLinea voce_f_saldi_cdr_linea%Rowtype;
       recSaldiLineaNew voce_f_saldi_cdr_linea%Rowtype;
       deltaSaldoLinea voce_f_saldi_cdr_linea%Rowtype;
     Begin
       recSaldiLinea := getOrCreateSaldi(aPdgVariazioneRiga1.esercizio, aPdgVariazioneRiga1.esercizio, aPdgVariazioneRiga1.CD_CDR_ASSEGNATARIO,
                           aPdgVariazioneRiga1.CD_LINEA_ATTIVITA, aPdgVariazioneRiga1.TI_APPARTENENZA, aPdgVariazioneRiga1.TI_GESTIONE,
                           aPdgVariazioneRiga1.CD_ELEMENTO_VOCE,
                           aCdVoceFMeno);

       recSaldiLineaNew := getOrCreateSaldiNew(recSaldiLinea);

       deltaSaldoLinea := getDeltaSaldo(recSaldiLinea, aUser);
       deltaSaldoLinea.VARIAZIONI_MENO := Abs(aPdgVariazioneRiga1.IM_SPESE_GEST_DECENTRATA_EST);

       CNRCTB054.CREA_AGGIORNA_SALDI(deltaSaldoLinea, 'util_variazioni.crea 1', 'Y');
     End;

     --Aggiorno i saldi linea 2
     Declare
       recSaldiLinea voce_f_saldi_cdr_linea%Rowtype;
       recSaldiLineaNew voce_f_saldi_cdr_linea%Rowtype;
       deltaSaldoLinea voce_f_saldi_cdr_linea%Rowtype;
     Begin
       recSaldiLinea := getOrCreateSaldi(aPdgVariazioneRiga2.esercizio, aPdgVariazioneRiga2.esercizio, aPdgVariazioneRiga2.CD_CDR_ASSEGNATARIO,
                           aPdgVariazioneRiga2.CD_LINEA_ATTIVITA, aPdgVariazioneRiga2.TI_APPARTENENZA, aPdgVariazioneRiga2.TI_GESTIONE,
                           aPdgVariazioneRiga2.CD_ELEMENTO_VOCE,
                           aCdVoceFPiu);

       recSaldiLineaNew := getOrCreateSaldiNew(recSaldiLinea);

       deltaSaldoLinea := getDeltaSaldo(recSaldiLinea, aUser);
       deltaSaldoLinea.VARIAZIONI_PIU := Abs(aPdgVariazioneRiga2.IM_SPESE_GEST_DECENTRATA_EST);

       CNRCTB054.CREA_AGGIORNA_SALDI(deltaSaldoLinea, 'util_variazioni.crea 2', 'Y');
     End;
   Elsif aEs > aEsRes Then
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
     aVarStanzRes.CD_CDS:=CNRUTL001.GETCDSFROMCDR(aCDRMeno.cd_centro_responsabilita);
     aVarStanzRes.CD_CENTRO_RESPONSABILITA:=aCDRMeno.cd_centro_responsabilita;
     aVarStanzRes.ESERCIZIO_RES:=aEsRes;
     aVarStanzRes.DT_APERTURA:=trunc(aTSNow);
     aVarStanzRes.DT_CHIUSURA:=Trunc(Sysdate);
     aVarStanzRes.DT_APPROVAZIONE:=Trunc(Sysdate);
     aVarStanzRes.DS_VARIAZIONE:=aDsVar;
     aVarStanzRes.DS_DELIBERA:='.';
     aVarStanzRes.STATO:='APP';
     aVarStanzRes.TIPOLOGIA:='STO';
     aVarStanzRes.TIPOLOGIA_FIN:=aFonti;
     aVarStanzRes.DACR:=aTSNow;
     aVarStanzRes.UTCR:=aUser;
     aVarStanzRes.DUVA:=aTSNow;
     aVarStanzRes.UTUV:=aUser;
     aVarStanzRes.PG_VER_REC:=1;

     --Riga 1 del CDR da cui togliere
     aAssVarStanzResCdr1.ESERCIZIO:=aEs;
     aAssVarStanzResCdr1.PG_VARIAZIONE := lPgVariazione;
     aAssVarStanzResCdr1.CD_CENTRO_RESPONSABILITA:=aVarStanzRes.CD_CENTRO_RESPONSABILITA;
     aAssVarStanzResCdr1.IM_SPESA := -abs(aImportoVar);
     aAssVarStanzResCdr1.DACR:=aTSNow;
     aAssVarStanzResCdr1.UTCR:=aUser;
     aAssVarStanzResCdr1.DUVA:=aTSNow;
     aAssVarStanzResCdr1.UTUV:=aUser;
     aAssVarStanzResCdr1.PG_VER_REC:=1;

     aVarStanzResRiga1.ESERCIZIO := aEs;
     aVarStanzResRiga1.PG_VARIAZIONE := lPgVariazione;
     aVarStanzResRiga1.PG_RIGA := 1;
     aVarStanzResRiga1.ESERCIZIO_VOCE := aEs;
     aVarStanzResRiga1.ESERCIZIO_RES := aVarStanzRes.ESERCIZIO_RES;
     aVarStanzResRiga1.CD_CDR := aAssVarStanzResCdr1.CD_CENTRO_RESPONSABILITA;
     aVarStanzResRiga1.CD_LINEA_ATTIVITA := aLAMeno.cd_linea_attivita;
     aVarStanzResRiga1.TI_APPARTENENZA := aVoceMeno.TI_APPARTENENZA;
     aVarStanzResRiga1.TI_GESTIONE := aVoceMeno.TI_GESTIONE;

     If not isNuovoPdg Then
       aVarStanzResRiga1.CD_VOCE := aCdVoceFMeno;
     Else
       aVarStanzResRiga1.CD_VOCE := aVoceMeno.CD_ELEMENTO_VOCE;
     End If;

     aVarStanzResRiga1.CD_ELEMENTO_VOCE := aVoceMeno.CD_ELEMENTO_VOCE;
     aVarStanzResRiga1.IM_VARIAZIONE := -abs(aImportoVar);
     aVarStanzResRiga1.DACR:=aTSNow;
     aVarStanzResRiga1.UTCR:=aUser;
     aVarStanzResRiga1.DUVA:=aTSNow;
     aVarStanzResRiga1.UTUV:=aUser;
     aVarStanzResRiga1.PG_VER_REC:=1;

     --Riga 2 del CDR a cui dare
     aAssVarStanzResCdr2.ESERCIZIO:=aEs;
     aAssVarStanzResCdr2.PG_VARIAZIONE := lPgVariazione;
     aAssVarStanzResCdr2.CD_CENTRO_RESPONSABILITA:=aCDRPiu.CD_CENTRO_RESPONSABILITA;
     aAssVarStanzResCdr2.IM_SPESA := abs(aImportoVar);
     aAssVarStanzResCdr2.DACR:=aTSNow;
     aAssVarStanzResCdr2.UTCR:=aUser;
     aAssVarStanzResCdr2.DUVA:=aTSNow;
     aAssVarStanzResCdr2.UTUV:=aUser;
     aAssVarStanzResCdr2.PG_VER_REC:=1;

     aVarStanzResRiga2.ESERCIZIO := aEs;
     aVarStanzResRiga2.PG_VARIAZIONE := lPgVariazione;
     aVarStanzResRiga2.PG_RIGA := 2;
     aVarStanzResRiga2.ESERCIZIO_VOCE := aEs;
     aVarStanzResRiga2.ESERCIZIO_RES := aVarStanzRes.ESERCIZIO_RES;
     aVarStanzResRiga2.CD_CDR := aAssVarStanzResCdr2.CD_CENTRO_RESPONSABILITA;
     aVarStanzResRiga2.CD_LINEA_ATTIVITA := aLAPiu.CD_LINEA_ATTIVITA;
     aVarStanzResRiga2.TI_APPARTENENZA := aVocePiu.TI_APPARTENENZA;
     aVarStanzResRiga2.TI_GESTIONE := aVocePiu.TI_GESTIONE;

     If not isNuovoPdg Then
       aVarStanzResRiga2.CD_VOCE := aCdVoceFPiu;
     Else
       aVarStanzResRiga2.CD_VOCE := aVocePiu.CD_ELEMENTO_VOCE;
     End If;

     aVarStanzResRiga2.CD_ELEMENTO_VOCE := aVocePiu.CD_ELEMENTO_VOCE;
     aVarStanzResRiga2.IM_VARIAZIONE := abs(aImportoVar);
     aVarStanzResRiga2.DACR:=aTSNow;
     aVarStanzResRiga2.UTCR:=aUser;
     aVarStanzResRiga2.DUVA:=aTSNow;
     aVarStanzResRiga2.UTUV:=aUser;
     aVarStanzResRiga2.PG_VER_REC:=1;

     CNRCTB051.ins_VAR_STANZ_RES(aVarStanzRes);

     CNRCTB051.ins_ASS_VAR_STANZ_RES_CDR(aAssVarStanzResCdr1);
     CNRCTB051.ins_VAR_STANZ_RES_RIGA(aVarStanzResRiga1);

     CNRCTB051.ins_ASS_VAR_STANZ_RES_CDR(aAssVarStanzResCdr2);
     CNRCTB051.ins_VAR_STANZ_RES_RIGA(aVarStanzResRiga2);

     oEsVar := aVarStanzRes.ESERCIZIO;
     oPgVar := aVarStanzRes.PG_VARIAZIONE;

     Update numerazione_base
     Set cd_corrente = lPgVariazione
     Where esercizio = aEs
     And tabella = 'VAR_STANZ_RES'
     And colonna = 'PG_VARIAZIONE';

     --Aggiorno i saldi linea 1
     Declare
       recSaldiLinea voce_f_saldi_cdr_linea%Rowtype;
       recAssestato v_assestato_residuo%Rowtype;
       recSaldiLineaNew voce_f_saldi_cdr_linea%Rowtype;
       deltaSaldoLinea voce_f_saldi_cdr_linea%Rowtype;
     Begin
       recSaldiLinea := getOrCreateSaldi(aVarStanzResRiga1.esercizio, aVarStanzResRiga1.esercizio_res, aVarStanzResRiga1.CD_CDR,
                           aVarStanzResRiga1.CD_LINEA_ATTIVITA, aVarStanzResRiga1.TI_APPARTENENZA, aVarStanzResRiga1.TI_GESTIONE,
                           aVarStanzResRiga1.CD_ELEMENTO_VOCE, aVarStanzResRiga1.CD_VOCE);

       recSaldiLineaNew := getOrCreateSaldiNew(recSaldiLinea);

       deltaSaldoLinea := getDeltaSaldo(recSaldiLinea, aUser);
       deltaSaldoLinea.VAR_MENO_STANZ_RES_IMP := Abs(aVarStanzResRiga1.IM_VARIAZIONE);

       CNRCTB054.CREA_AGGIORNA_SALDI(deltaSaldoLinea, 'util_variazioni.crea 3', 'Y');
     End;

     --Aggiorno i saldi linea 2
     Declare
       recSaldiLinea voce_f_saldi_cdr_linea%Rowtype;
       recSaldiLineaNew voce_f_saldi_cdr_linea%Rowtype;
       deltaSaldoLinea voce_f_saldi_cdr_linea%Rowtype;
     Begin
       recSaldiLinea := getOrCreateSaldi(aVarStanzResRiga2.esercizio, aVarStanzResRiga2.esercizio_res, aVarStanzResRiga2.CD_CDR,
                           aVarStanzResRiga2.CD_LINEA_ATTIVITA, aVarStanzResRiga2.TI_APPARTENENZA, aVarStanzResRiga2.TI_GESTIONE,
                           aVarStanzResRiga2.CD_ELEMENTO_VOCE,
                           aVarStanzResRiga2.CD_VOCE);

       recSaldiLineaNew := getOrCreateSaldiNew(recSaldiLinea);

       deltaSaldoLinea := getDeltaSaldo(recSaldiLinea, aUser);
       deltaSaldoLinea.VAR_PIU_STANZ_RES_IMP := Abs(aVarStanzResRiga2.IM_VARIAZIONE);

       CNRCTB054.CREA_AGGIORNA_SALDI(deltaSaldoLinea, 'util_variazioni.crea 4', 'Y');
     End;
   End If;
 End generaStornoTraCdsMonoEv;

 Procedure generaPerenzioneDaRiaccResPass
   (aEs NUMBER,
    aDsVar Varchar2,
    aUser VARCHAR2,
    oNumVarCreate Out NUMBER) Is

   aVarStanzRes  var_stanz_res%rowtype;
   aAssVarStanzResCdr ass_var_stanz_res_cdr%Rowtype;
   aVarStanzResRiga var_stanz_res_riga%rowtype;

   lPgVariazione numerazione_base.cd_corrente%type;
   lPgVariazioneRiga number := 0;

   aDtVar date;
   aTSNow date;
 Begin
   Dbms_Output.put_line('generaPerenzioneDaRiaccResPass');

   aDtVar := to_date('31/12/'||aEs, 'dd/mm/yyyy');
   aTSNow := sysdate;
   oNumVarCreate := 0;

   Begin
     Select cd_corrente
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

   For grp in (Select riac.cdr, riac.esercizio_res, nat.tipo, nvl(sum(nvl(importo,0)),0) aImportoVar
               from APPO_RIACCERTAMENTO riac, linea_attivita latt, natura nat
               where riac.esercizio=aEs
               and   riac.esercizio_res<riac.esercizio
               and   riac.ti_gestione='S'
               and   riac.cdr = latt.cd_centro_responsabilita
               and   riac.cd_linea_attivita = latt.cd_linea_attivita
               and   latt.cd_natura = nat.cd_natura
               group by riac.cdr, riac.esercizio_res, nat.tipo
               order by riac.cdr, riac.esercizio_res, nat.tipo desc) Loop

      lPgVariazione := lPgVariazione + 1;

      aVarStanzRes.ESERCIZIO:=aEs;
      aVarStanzRes.PG_VARIAZIONE := lPgVariazione;
      aVarStanzRes.CD_CDS:=CNRUTL001.GETCDSFROMCDR(grp.cdr);
      aVarStanzRes.CD_CENTRO_RESPONSABILITA:=grp.cdr;
      aVarStanzRes.ESERCIZIO_RES:=grp.esercizio_res;
      aVarStanzRes.DT_APERTURA:=trunc(aDtVar);
      aVarStanzRes.DT_CHIUSURA:=null;
      aVarStanzRes.DT_APPROVAZIONE:=null;
      aVarStanzRes.DS_VARIAZIONE:=aDsVar;
      aVarStanzRes.DS_DELIBERA:=null;
      aVarStanzRes.STATO:='PRP';
      aVarStanzRes.TIPOLOGIA:='ECO';
      aVarStanzRes.TIPOLOGIA_FIN:=grp.tipo;
      aVarStanzRes.DACR:=aTSNow;
      aVarStanzRes.UTCR:=aUser;
      aVarStanzRes.DUVA:=aTSNow;
      aVarStanzRes.UTUV:=aUser;
      aVarStanzRes.PG_VER_REC:=1;

      CNRCTB051.ins_VAR_STANZ_RES(aVarStanzRes);
      oNumVarCreate := oNumVarCreate + 1;

      --Riga 1 del CDR da cui togliere
      aAssVarStanzResCdr.ESERCIZIO:=aEs;
      aAssVarStanzResCdr.PG_VARIAZIONE := lPgVariazione;
      aAssVarStanzResCdr.CD_CENTRO_RESPONSABILITA:=aVarStanzRes.CD_CENTRO_RESPONSABILITA;
      aAssVarStanzResCdr.IM_SPESA := -abs(grp.aImportoVar);
      aAssVarStanzResCdr.DACR:=aTSNow;
      aAssVarStanzResCdr.UTCR:=aUser;
      aAssVarStanzResCdr.DUVA:=aTSNow;
      aAssVarStanzResCdr.UTUV:=aUser;
      aAssVarStanzResCdr.PG_VER_REC:=1;

      CNRCTB051.ins_ASS_VAR_STANZ_RES_CDR(aAssVarStanzResCdr);

      lPgVariazioneRiga := 0;
      For rec in (Select riac.CD_LINEA_ATTIVITA, riac.TI_APPARTENENZA, riac.CD_ELEMENTO_VOCE, 
                         nvl(sum(nvl(importo,0)),0) aImportoVar
                  from APPO_RIACCERTAMENTO riac, linea_attivita latt, natura nat
                  where riac.esercizio=aEs
                  and   riac.esercizio_res<riac.esercizio
                  and   riac.ti_gestione='S'
                  and   riac.cdr = latt.cd_centro_responsabilita
                  and   riac.cd_linea_attivita = latt.cd_linea_attivita
                  and   latt.cd_natura = nat.cd_natura
                  and   riac.cdr = grp.cdr
                  and   riac.esercizio_res = grp.esercizio_res
                  and   nat.tipo = grp.tipo
                  group by riac.CD_LINEA_ATTIVITA, riac.TI_APPARTENENZA, riac.CD_ELEMENTO_VOCE
                  order by riac.CD_LINEA_ATTIVITA, riac.TI_APPARTENENZA, riac.CD_ELEMENTO_VOCE) Loop
        lPgVariazioneRiga := lPgVariazioneRiga + 1;
        aVarStanzResRiga.ESERCIZIO := aEs;
        aVarStanzResRiga.PG_VARIAZIONE := lPgVariazione;
        aVarStanzResRiga.PG_RIGA := lPgVariazioneRiga;
        aVarStanzResRiga.ESERCIZIO_VOCE := aEs;
        aVarStanzResRiga.ESERCIZIO_RES := aVarStanzRes.ESERCIZIO_RES;
        aVarStanzResRiga.CD_CDR := aAssVarStanzResCdr.CD_CENTRO_RESPONSABILITA;
        aVarStanzResRiga.CD_LINEA_ATTIVITA := rec.cd_linea_attivita;
        aVarStanzResRiga.TI_APPARTENENZA := rec.TI_APPARTENENZA;
        aVarStanzResRiga.TI_GESTIONE := 'S';
        aVarStanzResRiga.CD_VOCE := rec.CD_ELEMENTO_VOCE;
        aVarStanzResRiga.CD_ELEMENTO_VOCE := rec.CD_ELEMENTO_VOCE;
        aVarStanzResRiga.IM_VARIAZIONE := -abs(rec.aImportoVar);
        aVarStanzResRiga.DACR:=aTSNow;
        aVarStanzResRiga.UTCR:=aUser;
        aVarStanzResRiga.DUVA:=aTSNow;
        aVarStanzResRiga.UTUV:=aUser;
        aVarStanzResRiga.PG_VER_REC:=1;

        CNRCTB051.ins_VAR_STANZ_RES_RIGA(aVarStanzResRiga);
      End Loop;
   End Loop;

   Update numerazione_base
   Set cd_corrente = lPgVariazione
   Where esercizio = aEs
   And tabella = 'VAR_STANZ_RES'
   And colonna = 'PG_VARIAZIONE';
 End generaPerenzioneDaRiaccResPass; 

 Procedure generaStornoResidui
   (aEs NUMBER,
    aDsVar Varchar2,
    aCdCdrPiu In VARCHAR2,
    aTiAppVocePiu elemento_voce.ti_appartenenza%type,
    aCdVocePiu elemento_voce.cd_elemento_voce%type,
    aCdGaeFinPiu linea_attivita.cd_linea_attivita%type,
    aCdGaeFesPiu linea_attivita.cd_linea_attivita%type,
    aUser VARCHAR2,
    oNumVarCreate Out NUMBER) Is

   aVarStanzRes  var_stanz_res%rowtype;
   aAssVarStanzResCdr1 ass_var_stanz_res_cdr%Rowtype;
   aAssVarStanzResCdr2 ass_var_stanz_res_cdr%Rowtype;
   aVarStanzResRiga1 var_stanz_res_riga%rowtype;
   aVarStanzResRiga2 var_stanz_res_riga%rowtype;

   lPgVariazione numerazione_base.cd_corrente%type;
   lPgVariazioneRiga number := 0;

   aDtVar date;
   aTSNow date;
 Begin
   Dbms_Output.put_line('generaStornoTraCds');

   aDtVar := to_date('31/12/'||aEs, 'dd/mm/yyyy');
   aTSNow := sysdate;
   oNumVarCreate := 0;

   Begin
     Select cd_corrente
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

   For esenat in (Select dati.esercizio_res, nat.tipo, nvl(sum(nvl(dati.importo,0)),0) aImportoVar
                  from GENERA_VAR_AUTO dati, linea_attivita latt, natura nat
                  where dati.tipologia = 'STO_S_TOT'
                  and   dati.esercizio=aEs
                  and   dati.esercizio_res<dati.esercizio
                  and   dati.ti_gestione='S'
                  and   dati.cdr = latt.cd_centro_responsabilita
                  and   dati.cd_linea_attivita = latt.cd_linea_attivita
                  and   latt.cd_natura = nat.cd_natura
                  group by dati.esercizio_res, nat.tipo
                  order by dati.esercizio_res, nat.tipo desc) Loop

      lPgVariazione := lPgVariazione + 1;

      aVarStanzRes.ESERCIZIO:=aEs;
      aVarStanzRes.PG_VARIAZIONE := lPgVariazione;
      aVarStanzRes.CD_CDS:=CNRUTL001.GETCDSFROMCDR(aCdCdrPiu);
      aVarStanzRes.CD_CENTRO_RESPONSABILITA:=aCdCdrPiu;
      aVarStanzRes.ESERCIZIO_RES:=esenat.esercizio_res;
      aVarStanzRes.DT_APERTURA:=trunc(aDtVar);
      aVarStanzRes.DT_CHIUSURA:=null;
      aVarStanzRes.DT_APPROVAZIONE:=null;
      aVarStanzRes.DS_VARIAZIONE:=aDsVar;
      aVarStanzRes.DS_DELIBERA:=null;
      aVarStanzRes.STATO:='PRP';
      aVarStanzRes.TIPOLOGIA:='STO_S_TOT';
      aVarStanzRes.TIPOLOGIA_FIN:=esenat.tipo;
      aVarStanzRes.DACR:=aTSNow;
      aVarStanzRes.UTCR:=aUser;
      aVarStanzRes.DUVA:=aTSNow;
      aVarStanzRes.UTUV:=aUser;
      aVarStanzRes.PG_VER_REC:=1;

      CNRCTB051.ins_VAR_STANZ_RES(aVarStanzRes);
      oNumVarCreate := oNumVarCreate + 1;

      --Riga 1 del CDR a cui dare
      aAssVarStanzResCdr1.ESERCIZIO:=aVarStanzRes.ESERCIZIO;
      aAssVarStanzResCdr1.PG_VARIAZIONE := aVarStanzRes.PG_VARIAZIONE;
      aAssVarStanzResCdr1.CD_CENTRO_RESPONSABILITA:=aVarStanzRes.CD_CENTRO_RESPONSABILITA;
      aAssVarStanzResCdr1.IM_SPESA := abs(esenat.aImportoVar);
      aAssVarStanzResCdr1.DACR:=aTSNow;
      aAssVarStanzResCdr1.UTCR:=aUser;
      aAssVarStanzResCdr1.DUVA:=aTSNow;
      aAssVarStanzResCdr1.UTUV:=aUser;
      aAssVarStanzResCdr1.PG_VER_REC:=1;

      CNRCTB051.ins_ASS_VAR_STANZ_RES_CDR(aAssVarStanzResCdr1);

      lPgVariazioneRiga := 1;

      aVarStanzResRiga1.ESERCIZIO := aVarStanzRes.ESERCIZIO;
      aVarStanzResRiga1.PG_VARIAZIONE := aVarStanzRes.PG_VARIAZIONE;
      aVarStanzResRiga1.PG_RIGA := lPgVariazioneRiga;
      aVarStanzResRiga1.ESERCIZIO_VOCE := aVarStanzRes.ESERCIZIO;
      aVarStanzResRiga1.ESERCIZIO_RES := aVarStanzRes.ESERCIZIO_RES;
      aVarStanzResRiga1.CD_CDR := aAssVarStanzResCdr1.CD_CENTRO_RESPONSABILITA;
      If esenat.tipo = 'FIN' Then
        aVarStanzResRiga1.CD_LINEA_ATTIVITA := aCdGaeFinPiu;
      Else
        aVarStanzResRiga1.CD_LINEA_ATTIVITA := aCdGaeFesPiu;
      End If;
      aVarStanzResRiga1.TI_APPARTENENZA := aTiAppVocePiu;
      aVarStanzResRiga1.TI_GESTIONE := 'S';
      aVarStanzResRiga1.CD_VOCE := aCdVocePiu;
      aVarStanzResRiga1.CD_ELEMENTO_VOCE := aCdVocePiu;
      aVarStanzResRiga1.IM_VARIAZIONE := aAssVarStanzResCdr1.IM_SPESA;
      aVarStanzResRiga1.DACR:=aTSNow;
      aVarStanzResRiga1.UTCR:=aUser;
      aVarStanzResRiga1.DUVA:=aTSNow;
      aVarStanzResRiga1.UTUV:=aUser;
      aVarStanzResRiga1.PG_VER_REC:=1;

      CNRCTB051.ins_VAR_STANZ_RES_RIGA(aVarStanzResRiga1);

      For grpcdr in (Select dati.cdr, nvl(sum(nvl(dati.importo,0)),0) aImportoVar
                     from GENERA_VAR_AUTO dati, linea_attivita latt, natura nat
                     where dati.tipologia = 'STO_S_TOT'
                     and   dati.esercizio=aEs
                     and   dati.esercizio_res<dati.esercizio
                     and   dati.ti_gestione='S'
                     and   dati.cdr = latt.cd_centro_responsabilita
                     and   dati.cd_linea_attivita = latt.cd_linea_attivita
                     and   latt.cd_natura = nat.cd_natura
                     and   dati.esercizio_res = esenat.esercizio_res
                     and   nat.tipo = esenat.tipo
                     group by dati.cdr
                     order by dati.cdr) Loop
        --Riga 2 del CDR a cui togliere
        aAssVarStanzResCdr2.ESERCIZIO:=aVarStanzRes.ESERCIZIO;
        aAssVarStanzResCdr2.PG_VARIAZIONE := aVarStanzRes.PG_VARIAZIONE;
        aAssVarStanzResCdr2.CD_CENTRO_RESPONSABILITA:=grpcdr.cdr;
        aAssVarStanzResCdr2.IM_SPESA := -abs(grpcdr.aImportoVar);
        aAssVarStanzResCdr2.DACR:=aTSNow;
        aAssVarStanzResCdr2.UTCR:=aUser;
        aAssVarStanzResCdr2.DUVA:=aTSNow;
        aAssVarStanzResCdr2.UTUV:=aUser;
        aAssVarStanzResCdr2.PG_VER_REC:=1;

        CNRCTB051.ins_ASS_VAR_STANZ_RES_CDR(aAssVarStanzResCdr2);

        For rec in (Select dati.CD_LINEA_ATTIVITA, dati.TI_APPARTENENZA, dati.CD_ELEMENTO_VOCE, 
                           nvl(sum(nvl(dati.importo,0)),0) aImportoVar
                    from GENERA_VAR_AUTO dati, linea_attivita latt, natura nat
                    where dati.tipologia = 'STO_S_TOT'
                    and   dati.esercizio=aEs
                    and   dati.esercizio_res<dati.esercizio
                    and   dati.ti_gestione='S'
                    and   dati.cdr = latt.cd_centro_responsabilita
                    and   dati.cd_linea_attivita = latt.cd_linea_attivita
                    and   latt.cd_natura = nat.cd_natura
                    and   dati.esercizio_res = esenat.esercizio_res
                    and   nat.tipo = esenat.tipo
                    and   dati.cdr = grpcdr.cdr
                    group by dati.CD_LINEA_ATTIVITA, dati.TI_APPARTENENZA, dati.CD_ELEMENTO_VOCE
                    order by dati.CD_LINEA_ATTIVITA, dati.TI_APPARTENENZA, dati.CD_ELEMENTO_VOCE) Loop
          lPgVariazioneRiga := lPgVariazioneRiga + 1;

          aVarStanzResRiga2.ESERCIZIO := aVarStanzRes.ESERCIZIO;
          aVarStanzResRiga2.PG_VARIAZIONE := aVarStanzRes.PG_VARIAZIONE;
          aVarStanzResRiga2.PG_RIGA := lPgVariazioneRiga;
          aVarStanzResRiga2.ESERCIZIO_VOCE := aVarStanzRes.ESERCIZIO;
          aVarStanzResRiga2.ESERCIZIO_RES := aVarStanzRes.ESERCIZIO_RES;
          aVarStanzResRiga2.CD_CDR := aAssVarStanzResCdr2.CD_CENTRO_RESPONSABILITA;
          aVarStanzResRiga2.CD_LINEA_ATTIVITA := rec.CD_LINEA_ATTIVITA;
          aVarStanzResRiga2.TI_APPARTENENZA := rec.TI_APPARTENENZA;
          aVarStanzResRiga2.TI_GESTIONE := 'S';
          aVarStanzResRiga2.CD_VOCE := rec.CD_ELEMENTO_VOCE;
          aVarStanzResRiga2.CD_ELEMENTO_VOCE := rec.CD_ELEMENTO_VOCE;
          aVarStanzResRiga2.IM_VARIAZIONE := -abs(rec.aImportoVar);
          aVarStanzResRiga2.DACR:=aTSNow;
          aVarStanzResRiga2.UTCR:=aUser;
          aVarStanzResRiga2.DUVA:=aTSNow;
          aVarStanzResRiga2.UTUV:=aUser;
          aVarStanzResRiga2.PG_VER_REC:=1;

          CNRCTB051.ins_VAR_STANZ_RES_RIGA(aVarStanzResRiga2);
        End Loop;
     End Loop;
   End Loop;

   Update numerazione_base
   Set cd_corrente = lPgVariazione
   Where esercizio = aEs
   And tabella = 'VAR_STANZ_RES'
   And colonna = 'PG_VARIAZIONE';
 End generaStornoResidui;
End UTIL_VARIAZIONI;
/
