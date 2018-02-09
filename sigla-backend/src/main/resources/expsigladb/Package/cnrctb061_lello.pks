CREATE OR REPLACE Package CNRCTB061_lello as
--
-- CNRCTB061_lello - Package di gestione dello scarico dei costi del personale su PDGP e PDG
--
-- Date: 27/03/2006
-- Version: 1.3
--
-- Dependency: CNRCTB000/020/010/050 IBMERR 001
--
-- History:
--
-- Date: 25/10/2005
-- Version: 1.0
-- Creazione
--
-- Date: 02/12/2005
-- Version: 1.1
-- Modificato per gestire l'annullamento dei rotti
--
-- Date: 19/12/2005
-- Version: 1.2
-- Aggiunta la procedure scaricaPdgPSuPdg per gestire il rilascio del PDGP sul PDG
--
-- Date: 27/03/2006
-- Version: 1.3
-- Gestito lo scarico di una UO CDS
--
-- Constants:
--
-- Stato di scarico del dettaglio del personale su piano di gestione
--
contaaASSCDPLA NUMBER := 0;
contatabModuloSpeseGest NUMBER := 0;
diff NUMBER := 0;
trovato Boolean;

STATO_CDP_NON_SCARICATO CONSTANT VARCHAR2(5) := 'I';
STATO_CDP_SCARICATO CONSTANT VARCHAR2(5) := 'S';
STATO_CDP_PDGP_SCARICATO CONSTANT VARCHAR2(5) := 'SP';
--
-- Origine del dettaglio del piano di gestione gestionale
--
ORIGINE_UTENTE CONSTANT VARCHAR2(5) := 'DIR';
ORIGINE_PREVISIONE CONSTANT VARCHAR2(5) := 'PRE';
ORIGINE_PROPOSTA_VARIAZIONE CONSTANT VARCHAR2(5) := 'PDV';
ORIGINE_VARIAZIONE_APPROVATA CONSTANT VARCHAR2(5) := 'APP';
--
-- Categoria del dettaglio del piano di gestione gestionale
--
CATEGORIA_DIRETTA CONSTANT VARCHAR2(5) := 'DIR';
CATEGORIA_SCARICO CONSTANT VARCHAR2(5) := 'SCR';
CATEGORIA_STIPENDI CONSTANT VARCHAR2(5) := 'STI';
--
-- Dominio per individuare tabella COSTI/SPESE
--
TAB_COSTI CONSTANT VARCHAR2(5):='C';
TAB_SPESE CONSTANT VARCHAR2(5):='S';
--
-- Codici di DEFAULT
--
DEFAULT_ID_CLASSIFICAZIONE CONSTANT NUMBER(1):=0;
DEFAULT_CD_CDS_AREA CONSTANT VARCHAR2(5):='XXXXX';
--
CDP_TI_RAPP_DETERMINATO CONSTANT VARCHAR2(10) :='DET';
CDP_TI_RAPP_INDETERMINATO CONSTANT VARCHAR2(10) :='IND';
--
--
-- Stato di scarico del dettaglio del personale su piano di gestione preliminare

-- Legge tutti i PDGP RUO/NRUO del CDR RUO aCDRRUO NO LOCK POSSIBILE
--
-- aEs -> Esercizio contabile
-- aCdCDRRUO -> Codice del centro di responsabilita RUO

 cursor PDGP_CON_CONFIG_SCR(aEs number, aCdCDRRUO VARCHAR2, aUO unita_organizzativa%Rowtype) RETURN PDG_MODULO%ROWTYPE is (
   Select a.* from PDG_MODULO a, V_PDGP_CDR_RUO_NRUO b
   Where b.esercizio = aEs
   And   b.cd_cdr_root = aCdCDRRUO
   And  (aUO.cd_tipo_unita != CNRCTB020.TIPO_SAC Or
 	 b.cd_unita_organizzativa = aUO.cd_unita_organizzativa)
   And   a.esercizio = aEs
   And   a.cd_centro_responsabilita = b.cd_centro_responsabilita
   And   Exists (select 1 from ASS_CDP_LA
                 Where esercizio = aEs
                 And   mese=0
                 And   cd_centro_responsabilita = b.cd_centro_responsabilita) );

 -- Functions e Procedures:

 Procedure scaricaCDPSuPdgLello(aEsercizio number, aCdCdr varchar2, aUser varchar2);

End;
/


CREATE OR REPLACE Package Body CNRCTB061_lello is
 Type typeModuloSpeseGest Is Table Of pdg_modulo_spese_gest%Rowtype Index By BINARY_INTEGER;

 -- Imposta gli importi sul dettaglio di PDG da creare
 Procedure setImporto(aASSCDPLA V_CDP_SPACCATO_CDR_LA_VOCE%rowtype,
                      aDestS in out pdg_modulo_spese_gest%rowtype) is
   aCDRPersonale cdr%Rowtype;
   aLATmp linea_attivita%rowtype;
 Begin
   -- Leggo il CDR del personale e lock del PDG

   aCDRPersonale:=CNRCTB020.GETCDRPERSONALE;

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
                      aTabModulo in out typeModuloSpeseGest,
                      i NUMBER) is
   aCDRPersonale cdr%Rowtype;
   aLATmp linea_attivita%rowtype;
 Begin
   -- Leggo il CDR del personale e lock del PDG

   aCDRPersonale:=CNRCTB020.GETCDRPERSONALE;

   -- Leggo la linea di attivita per aggiornare gli importi del PDGP in base alla natura
   Select * into aLATmp from linea_attivita
   Where cd_linea_attivita = aASSCDPLA.cd_linea_attivita
   And   cd_centro_responsabilita = aASSCDPLA.cd_cdr;

   If aTabModulo(i).cd_centro_responsabilita=aCDRPersonale.cd_centro_responsabilita Then
      If aLATmp.cd_natura = '2' Or aLATmp.cd_natura = '3' Then
         aTabModulo(i).IM_SPESE_GEST_DECENTRATA_EST:=Nvl(aTabModulo(i).IM_SPESE_GEST_DECENTRATA_EST, 0) +
                                              Nvl(aASSCDPLA.im_a1, 0);
      Else
         aTabModulo(i).IM_SPESE_GEST_DECENTRATA_INT:=Nvl(aTabModulo(i).IM_SPESE_GEST_DECENTRATA_INT, 0) +
                                              Nvl(aASSCDPLA.im_a1, 0);
      End If;
   Else
      If aLATmp.cd_natura = '2' Or aLATmp.cd_natura = '3' Then
         aTabModulo(i).IM_SPESE_GEST_ACCENTRATA_EST:=Nvl(aTabModulo(i).IM_SPESE_GEST_ACCENTRATA_EST, 0) +
                                              Nvl(aASSCDPLA.im_a1, 0);
      Else
         aTabModulo(i).IM_SPESE_GEST_ACCENTRATA_INT:=Nvl(aTabModulo(i).IM_SPESE_GEST_ACCENTRATA_INT, 0) +
                                              Nvl(aASSCDPLA.im_a1, 0);
      End If;
   End If;
   --Serve per evitare che alcuni importi abbiano valore null
   aTabModulo(i).IM_SPESE_GEST_DECENTRATA_EST:=Nvl(aTabModulo(i).IM_SPESE_GEST_DECENTRATA_EST, 0);
   aTabModulo(i).IM_SPESE_GEST_DECENTRATA_INT:=Nvl(aTabModulo(i).IM_SPESE_GEST_DECENTRATA_INT, 0);
   aTabModulo(i).IM_SPESE_GEST_ACCENTRATA_EST:=Nvl(aTabModulo(i).IM_SPESE_GEST_ACCENTRATA_EST, 0);
   aTabModulo(i).IM_SPESE_GEST_ACCENTRATA_INT:=Nvl(aTabModulo(i).IM_SPESE_GEST_ACCENTRATA_INT, 0);
 End;

 Procedure scaricaCDPSuPdgLello(aEsercizio number, aCdCdr varchar2, aUser varchar2) is
   aCDRRUO cdr%Rowtype;
   aUO unita_organizzativa%Rowtype;

   aAssSpese ass_cdp_pdgp%Rowtype;

   tabModuloSpeseGest typeModuloSpeseGest;
   trovatoModuloSpeseGest Boolean := False;

   aDestS    pdg_modulo_spese_gest%Rowtype;

   aTSNow DATE;
   aDescrizione pdg_modulo_spese_gest.descrizione%Type := 'Inserimento automatico proveniente dallo scarico dei costi del personale.';
   aModuloTmp progetto%Rowtype;

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

   aLATmp  linea_attivita%Rowtype;
   aModulo progetto%Rowtype;
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

   aCDRPersonale:=CNRCTB020.GETCDRPERSONALE;

   Select count(*) into aNum
   From ass_cdp_pdgp
   Where esercizio = aEsercizio
   And   ti_costi_spese = CNRCTB061_lello.TAB_COSTI
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

   -- Start dello scarico su PDG dei costi del personale
   For aASSCDPLA In (
     Select * From V_CDP_SPACC_CDR_LA_VOCE_LELLO Where
 	          esercizio = aEsercizio
 		  And mese = 0
 		  And cd_cdr_root In (Select cd_centro_responsabilita from V_PDGP_CDR_RUO_NRUO
                                      Where esercizio = aEsercizio
 		                      And   cd_cdr_root =  aCdCdr
 		                      And  (aUO.cd_tipo_unita != CNRCTB020.TIPO_SAC Or
 		                            cd_unita_organizzativa = aUO.cd_unita_organizzativa))
 		  Order by cd_cdr) loop  -- Cicla sulle configurazioni di scarico
contaaASSCDPLA := contaaASSCDPLA + 1;
     -- Determina il tipo di rapporto dalla tabella COSTO_DEL_DIPENDENTE

     isTempoDeterminato:=false;
     If aASSCDPLA.ti_rapporto = CDP_TI_RAPP_DETERMINATO Then
       isTempoDeterminato:=true;
     End if;

     -- Leggo la linea di attivita per aggiornare il dettaglio del PDG con funzione e natura
     Begin
       Select * into aLATmp from linea_attivita
       Where cd_linea_attivita = aASSCDPLA.cd_linea_attivita
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

     isVoceTFR:=False;
     If aASSCDPLA.cd_elemento_voce = aVoceTFR.cd_elemento_voce Or
        aASSCDPLA.cd_elemento_voce = aVoceTFRTEMPODET.cd_elemento_voce Then
        isVoceTFR:=True;
     End If;

     If Not (isVoceTFR) Then
        trovatoModuloSpeseGest := False;
        For i In 1..tabModuloSpeseGest.count Loop
          If tabModuloSpeseGest(i).ESERCIZIO = aEsercizio And
             tabModuloSpeseGest(i).CD_CENTRO_RESPONSABILITA = aCDRRUO.cd_centro_responsabilita And
             tabModuloSpeseGest(i).PG_PROGETTO = aLATmp.pg_progetto And
             tabModuloSpeseGest(i).ID_CLASSIFICAZIONE = aAllVociClassificazioneDef.id_classificazione And
             tabModuloSpeseGest(i).CD_CDS_AREA = aUO.cd_unita_padre And
             tabModuloSpeseGest(i).CD_CDR_ASSEGNATARIO = aASSCDPLA.cd_cdr And
             tabModuloSpeseGest(i).CD_LINEA_ATTIVITA = aASSCDPLA.cd_linea_attivita And
             tabModuloSpeseGest(i).TI_APPARTENENZA = aASSCDPLA.ti_appartenenza And
             tabModuloSpeseGest(i).TI_GESTIONE = aASSCDPLA.ti_gestione And
             tabModuloSpeseGest(i).CD_ELEMENTO_VOCE = aASSCDPLA.cd_elemento_voce Then

             Setimporto(aASSCDPLA, tabModuloSpeseGest, i);
/*
             tabModuloSpeseGest(i).IM_SPESE_GEST_DECENTRATA_EST := Nvl(tabModuloSpeseGest(i).IM_SPESE_GEST_DECENTRATA_EST, 0) +
                                                               Nvl(aDestS.IM_SPESE_GEST_DECENTRATA_EST, 0);
             tabModuloSpeseGest(i).IM_SPESE_GEST_DECENTRATA_INT := Nvl(tabModuloSpeseGest(i).IM_SPESE_GEST_DECENTRATA_INT, 0) +
                                                               Nvl(aDestS.IM_SPESE_GEST_DECENTRATA_INT, 0);
             tabModuloSpeseGest(i).IM_SPESE_GEST_ACCENTRATA_EST := Nvl(tabModuloSpeseGest(i).IM_SPESE_GEST_ACCENTRATA_EST, 0) +
                                                               Nvl(aDestS.IM_SPESE_GEST_ACCENTRATA_EST, 0);
             tabModuloSpeseGest(i).IM_SPESE_GEST_ACCENTRATA_INT := Nvl(tabModuloSpeseGest(i).IM_SPESE_GEST_ACCENTRATA_INT, 0) +
                                                               Nvl(aDestS.IM_SPESE_GEST_ACCENTRATA_INT, 0);
*/
             trovatoModuloSpeseGest := True;
             Exit;
          End If;
        End Loop;
        If Not trovatoModuloSpeseGest Then
          aDestS:=null;
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
          aDestS.ORIGINE := CNRCTB061_lello.ORIGINE_PREVISIONE;
          aDestS.CATEGORIA_DETTAGLIO := CNRCTB061_lello.CATEGORIA_STIPENDI;
          aDestS.FL_SOLA_LETTURA := 'Y';

          CNRCTB051.resetCampiImporto(aDestS);

          Setimporto(aASSCDPLA, aDestS);

          aDestS.DACR:=aTSNow;
          aDestS.UTCR:=aUser;
          aDestS.DUVA:=aTSNow;
          aDestS.UTUV:=aUser;
          aDestS.PG_VER_REC:=1;

          tabModuloSpeseGest(tabModuloSpeseGest.count+1):=aDestS;
        End If;
     End If;
   End Loop;

   -- Distribuzione dei rotti su dettagli creati
   For aAssCdpPdgPRnd In (Select * From ass_cdp_pdgp_round
                          Where esercizio = aEsercizio
 		          And   cd_cdr_root = aCDRRUO.cd_centro_responsabilita
 		          And   pg_progetto_spese Is Not Null) loop  -- Cicla sulle configurazioni dei ROTTI

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
       Select * into aLATmp from linea_attivita
       Where cd_linea_attivita = aAssCdpPdgPRnd.cd_linea_attivita
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
                 For i In 1..tabModuloSpeseGest.count Loop
                   if aAssCdpPdgPRnd.esercizio = tabModuloSpeseGest(i).esercizio and
                        aAssCdpPdgPRnd.cd_cdr_root = tabModuloSpeseGest(i).cd_centro_responsabilita and
                        aAssCdpPdgPRnd.pg_progetto_spese = tabModuloSpeseGest(i).pg_progetto and
                        aAssCdpPdgPRnd.id_classificazione = tabModuloSpeseGest(i).id_classificazione and
                        aAssCdpPdgPRnd.cd_cds_area = tabModuloSpeseGest(i).cd_cds_area and
                        aAssCdpPdgPRnd.cd_centro_responsabilita = tabModuloSpeseGest(i).cd_cdr_assegnatario and
                        aAssCdpPdgPRnd.cd_linea_attivita = tabModuloSpeseGest(i).cd_linea_attivita and
                        aAssCdpPdgPRnd.ti_appartenenza = tabModuloSpeseGest(i).ti_appartenenza and
                        aAssCdpPdgPRnd.ti_gestione = tabModuloSpeseGest(i).ti_gestione and
                        aAssCdpPdgPRnd.cd_elemento_voce = tabModuloSpeseGest(i).cd_elemento_voce Then
                        tabModuloSpeseGest(i).IM_SPESE_GEST_DECENTRATA_EST:=tabModuloSpeseGest(i).IM_SPESE_GEST_DECENTRATA_EST+aAssCdpPdgPRnd.im_arr_a1;
                    End If;
                  End Loop;
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
                 For i In 1..tabModuloSpeseGest.count Loop
                   if aAssCdpPdgPRnd.esercizio = tabModuloSpeseGest(i).esercizio and
                        aAssCdpPdgPRnd.cd_cdr_root = tabModuloSpeseGest(i).cd_centro_responsabilita and
                        aAssCdpPdgPRnd.pg_progetto_spese = tabModuloSpeseGest(i).pg_progetto and
                        aAssCdpPdgPRnd.id_classificazione = tabModuloSpeseGest(i).id_classificazione and
                        aAssCdpPdgPRnd.cd_cds_area = tabModuloSpeseGest(i).cd_cds_area and
                        aAssCdpPdgPRnd.cd_centro_responsabilita = tabModuloSpeseGest(i).cd_cdr_assegnatario and
                        aAssCdpPdgPRnd.cd_linea_attivita = tabModuloSpeseGest(i).cd_linea_attivita and
                        aAssCdpPdgPRnd.ti_appartenenza = tabModuloSpeseGest(i).ti_appartenenza and
                        aAssCdpPdgPRnd.ti_gestione = tabModuloSpeseGest(i).ti_gestione and
                        aAssCdpPdgPRnd.cd_elemento_voce = tabModuloSpeseGest(i).cd_elemento_voce Then
                        tabModuloSpeseGest(i).IM_SPESE_GEST_DECENTRATA_INT:=tabModuloSpeseGest(i).IM_SPESE_GEST_DECENTRATA_INT+aAssCdpPdgPRnd.im_arr_a1;
                    End If;
                  End Loop;
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
                 For i In 1..tabModuloSpeseGest.count Loop
                   if aAssCdpPdgPRnd.esercizio = tabModuloSpeseGest(i).esercizio and
                        aAssCdpPdgPRnd.cd_cdr_root = tabModuloSpeseGest(i).cd_centro_responsabilita and
                        aAssCdpPdgPRnd.pg_progetto_spese = tabModuloSpeseGest(i).pg_progetto and
                        aAssCdpPdgPRnd.id_classificazione = tabModuloSpeseGest(i).id_classificazione and
                        aAssCdpPdgPRnd.cd_cds_area = tabModuloSpeseGest(i).cd_cds_area and
                        aAssCdpPdgPRnd.cd_centro_responsabilita = tabModuloSpeseGest(i).cd_cdr_assegnatario and
                        aAssCdpPdgPRnd.cd_linea_attivita = tabModuloSpeseGest(i).cd_linea_attivita and
                        aAssCdpPdgPRnd.ti_appartenenza = tabModuloSpeseGest(i).ti_appartenenza and
                        aAssCdpPdgPRnd.ti_gestione = tabModuloSpeseGest(i).ti_gestione and
                        aAssCdpPdgPRnd.cd_elemento_voce = tabModuloSpeseGest(i).cd_elemento_voce Then
                        tabModuloSpeseGest(i).IM_SPESE_GEST_ACCENTRATA_EST:=tabModuloSpeseGest(i).IM_SPESE_GEST_ACCENTRATA_EST+aAssCdpPdgPRnd.im_arr_a1;
                    End If;
                  End Loop;
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
                 For i In 1..tabModuloSpeseGest.count Loop
                   if aAssCdpPdgPRnd.esercizio = tabModuloSpeseGest(i).esercizio and
                        aAssCdpPdgPRnd.cd_cdr_root = tabModuloSpeseGest(i).cd_centro_responsabilita and
                        aAssCdpPdgPRnd.pg_progetto_spese = tabModuloSpeseGest(i).pg_progetto and
                        aAssCdpPdgPRnd.id_classificazione = tabModuloSpeseGest(i).id_classificazione and
                        aAssCdpPdgPRnd.cd_cds_area = tabModuloSpeseGest(i).cd_cds_area and
                        aAssCdpPdgPRnd.cd_centro_responsabilita = tabModuloSpeseGest(i).cd_cdr_assegnatario and
                        aAssCdpPdgPRnd.cd_linea_attivita = tabModuloSpeseGest(i).cd_linea_attivita and
                        aAssCdpPdgPRnd.ti_appartenenza = tabModuloSpeseGest(i).ti_appartenenza and
                        aAssCdpPdgPRnd.ti_gestione = tabModuloSpeseGest(i).ti_gestione and
                        aAssCdpPdgPRnd.cd_elemento_voce = tabModuloSpeseGest(i).cd_elemento_voce Then
                        tabModuloSpeseGest(i).IM_SPESE_GEST_ACCENTRATA_INT:=tabModuloSpeseGest(i).IM_SPESE_GEST_ACCENTRATA_INT+aAssCdpPdgPRnd.im_arr_a1;
                    End If;
                  End Loop;
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

   --Carico la tabella PDG_MODULO_SPESE_GEST con i valori conservati nella PLSQLTABLE
   For i In 1..tabModuloSpeseGest.count Loop
/*
     Insert Into pdg_modulo_spese_gest_lello(ESERCIZIO, CD_CENTRO_RESPONSABILITA, PG_PROGETTO, ID_CLASSIFICAZIONE,
                                             CD_CDS_AREA, CD_CDR_ASSEGNATARIO, CD_LINEA_ATTIVITA, TI_APPARTENENZA,
                                             TI_GESTIONE, CD_ELEMENTO_VOCE,
                                             IM_SPESE_GEST_DECENTRATA_INT,
                                             IM_SPESE_GEST_DECENTRATA_EST,
                                             IM_SPESE_GEST_ACCENTRATA_INT,
                                             IM_SPESE_GEST_ACCENTRATA_EST,
                                             UTCR)
     Values(tabModuloSpeseGest(i).esercizio, tabModuloSpeseGest(i).cd_centro_responsabilita,
            tabModuloSpeseGest(i).pg_progetto, tabModuloSpeseGest(i).id_classificazione,
            tabModuloSpeseGest(i).cd_cds_area, tabModuloSpeseGest(i).cd_cdr_assegnatario,
            tabModuloSpeseGest(i).cd_linea_attivita, tabModuloSpeseGest(i).ti_appartenenza,
            tabModuloSpeseGest(i).ti_gestione, tabModuloSpeseGest(i).cd_elemento_voce,
            NVL(tabModuloSpeseGest(i).IM_SPESE_GEST_DECENTRATA_INT, 0),
            NVL(tabModuloSpeseGest(i).IM_SPESE_GEST_DECENTRATA_EST, 0),
            NVL(tabModuloSpeseGest(i).IM_SPESE_GEST_ACCENTRATA_INT, 0),
            NVL(tabModuloSpeseGest(i).IM_SPESE_GEST_ACCENTRATA_EST, 0),
            'LELLO');
*/
     contatabModuloSpeseGest := contatabModuloSpeseGest + 1;

     Begin
       -- Leggo il modulo_spese per inserire il dettaglio....se lo trovo sollevo una eccezione
       Select * into aDestS from pdg_modulo_spese_gest
       Where esercizio = tabModuloSpeseGest(i).esercizio
       And   cd_centro_responsabilita = tabModuloSpeseGest(i).cd_centro_responsabilita
       And   pg_progetto = tabModuloSpeseGest(i).pg_progetto
       And   id_classificazione = tabModuloSpeseGest(i).id_classificazione
       And   cd_cds_area = tabModuloSpeseGest(i).cd_cds_area
       And   cd_cdr_assegnatario = tabModuloSpeseGest(i).cd_cdr_assegnatario
       And   cd_linea_attivita = tabModuloSpeseGest(i).cd_linea_attivita
       And   ti_appartenenza = tabModuloSpeseGest(i).ti_appartenenza
       And   ti_gestione = tabModuloSpeseGest(i).ti_gestione
       And   cd_elemento_voce = tabModuloSpeseGest(i).cd_elemento_voce;

       If NVL(tabModuloSpeseGest(i).IM_SPESE_GEST_ACCENTRATA_INT, 0) != NVL(aDestS.IM_SPESE_GEST_ACCENTRATA_INT, 0) or
          NVL(tabModuloSpeseGest(i).IM_SPESE_GEST_ACCENTRATA_EST, 0) != NVL(aDestS.IM_SPESE_GEST_ACCENTRATA_EST, 0) or
          NVL(tabModuloSpeseGest(i).IM_SPESE_GEST_DECENTRATA_INT, 0) != NVL(aDestS.IM_SPESE_GEST_DECENTRATA_INT, 0) or
          NVL(tabModuloSpeseGest(i).IM_SPESE_GEST_DECENTRATA_EST, 0) != NVL(aDestS.IM_SPESE_GEST_DECENTRATA_EST, 0) Then
          Dbms_Output.put_line('IMPORTI DIFFERENTI');
          Dbms_Output.put_line(tabModuloSpeseGest(i).esercizio);
          Dbms_Output.put_line('1: '||tabModuloSpeseGest(i).cd_centro_responsabilita);
          Dbms_Output.put_line('2: '||tabModuloSpeseGest(i).pg_progetto);
          Dbms_Output.put_line('3: '||tabModuloSpeseGest(i).id_classificazione);
          Dbms_Output.put_line('4: '||tabModuloSpeseGest(i).cd_cds_area);
          Dbms_Output.put_line('5: '||tabModuloSpeseGest(i).cd_cdr_assegnatario);
          Dbms_Output.put_line('6: '||tabModuloSpeseGest(i).cd_linea_attivita);
          Dbms_Output.put_line('7: '||tabModuloSpeseGest(i).ti_appartenenza);
          Dbms_Output.put_line('8: '||tabModuloSpeseGest(i).ti_gestione);
          Dbms_Output.put_line('9: '||tabModuloSpeseGest(i).cd_elemento_voce);
          Dbms_Output.put_line('10: '||to_char(NVL(tabModuloSpeseGest(i).IM_SPESE_GEST_ACCENTRATA_INT, 0), '999g999g999g999g990d00'));
          Dbms_Output.put_line('10TAB: '||to_char(NVL(aDestS.IM_SPESE_GEST_ACCENTRATA_INT, 0), '999g999g999g999g990d00'));
          Dbms_Output.put_line('11: '||to_char(NVL(tabModuloSpeseGest(i).IM_SPESE_GEST_ACCENTRATA_EST, 0), '999g999g999g999g990d00'));
          Dbms_Output.put_line('11TAB: '||to_char(NVL(aDestS.IM_SPESE_GEST_ACCENTRATA_EST, 0), '999g999g999g999g990d00'));
          Dbms_Output.put_line('12: '||to_char(NVL(tabModuloSpeseGest(i).IM_SPESE_GEST_DECENTRATA_INT, 0), '999g999g999g999g990d00'));
          Dbms_Output.put_line('12TAB: '||to_char(NVL(aDestS.IM_SPESE_GEST_DECENTRATA_INT, 0), '999g999g999g999g990d00'));
          Dbms_Output.put_line('13: '||to_char(NVL(tabModuloSpeseGest(i).IM_SPESE_GEST_DECENTRATA_EST, 0), '999g999g999g999g990d00'));
          Dbms_Output.put_line('13TAB: '||to_char(NVL(aDestS.IM_SPESE_GEST_DECENTRATA_EST, 0), '999g999g999g999g990d00'));
          diff := diff + (NVL(tabModuloSpeseGest(i).IM_SPESE_GEST_ACCENTRATA_INT, 0) +
                          NVL(tabModuloSpeseGest(i).IM_SPESE_GEST_ACCENTRATA_EST, 0) +
                          NVL(tabModuloSpeseGest(i).IM_SPESE_GEST_DECENTRATA_INT, 0) +
                          NVL(tabModuloSpeseGest(i).IM_SPESE_GEST_DECENTRATA_EST, 0))
                       - (NVL(aDestS.IM_SPESE_GEST_ACCENTRATA_INT, 0) +
                          NVL(aDestS.IM_SPESE_GEST_ACCENTRATA_EST, 0) +
                          NVL(aDestS.IM_SPESE_GEST_DECENTRATA_INT, 0) +
                          NVL(aDestS.IM_SPESE_GEST_DECENTRATA_EST, 0));

          Dbms_Output.put_line('DIFFTEMP: '||to_char(diff, '999g999g999g999g990d00'));

          Update pdg_modulo_spese_gest
          Set IM_SPESE_GEST_ACCENTRATA_INT = NVL(tabModuloSpeseGest(i).IM_SPESE_GEST_ACCENTRATA_INT, 0),
              IM_SPESE_GEST_ACCENTRATA_EST = NVL(tabModuloSpeseGest(i).IM_SPESE_GEST_ACCENTRATA_EST, 0),
              IM_SPESE_GEST_DECENTRATA_INT = NVL(tabModuloSpeseGest(i).IM_SPESE_GEST_DECENTRATA_INT, 0),
              IM_SPESE_GEST_DECENTRATA_EST = NVL(tabModuloSpeseGest(i).IM_SPESE_GEST_DECENTRATA_EST, 0),
              utuv = 'SISTEMI',
              duva = Trunc(Sysdate)
          Where esercizio = tabModuloSpeseGest(i).esercizio
          And   cd_centro_responsabilita = tabModuloSpeseGest(i).cd_centro_responsabilita
          And   pg_progetto = tabModuloSpeseGest(i).pg_progetto
          And   id_classificazione = tabModuloSpeseGest(i).id_classificazione
          And   cd_cds_area = tabModuloSpeseGest(i).cd_cds_area
          And   cd_cdr_assegnatario = tabModuloSpeseGest(i).cd_cdr_assegnatario
          And   cd_linea_attivita = tabModuloSpeseGest(i).cd_linea_attivita
          And   ti_appartenenza = tabModuloSpeseGest(i).ti_appartenenza
          And   ti_gestione = tabModuloSpeseGest(i).ti_gestione
          And   cd_elemento_voce = tabModuloSpeseGest(i).cd_elemento_voce;

          If Sql%Rowcount != 1 Then
             IBMERR001.RAISE_ERR_GENERICO('Update 1 non logica');
          End If;

          Update pdg_modulo_spese_gest
          Set IM_SPESE_GEST_ACCENTRATA_INT = IM_SPESE_GEST_ACCENTRATA_INT +
                                             (NVL(tabModuloSpeseGest(i).IM_SPESE_GEST_ACCENTRATA_INT, 0) -
                                              NVL(aDestS.IM_SPESE_GEST_ACCENTRATA_INT, 0)),
              IM_SPESE_GEST_ACCENTRATA_EST = IM_SPESE_GEST_ACCENTRATA_EST +
                                             (NVL(tabModuloSpeseGest(i).IM_SPESE_GEST_ACCENTRATA_EST, 0) -
                                              NVL(aDestS.IM_SPESE_GEST_ACCENTRATA_EST, 0)),
              IM_SPESE_GEST_DECENTRATA_INT = IM_SPESE_GEST_DECENTRATA_INT +
                                             (NVL(tabModuloSpeseGest(i).IM_SPESE_GEST_DECENTRATA_INT, 0) -
                                              NVL(aDestS.IM_SPESE_GEST_DECENTRATA_INT, 0)),
              IM_SPESE_GEST_DECENTRATA_EST = IM_SPESE_GEST_DECENTRATA_EST +
                                             (NVL(tabModuloSpeseGest(i).IM_SPESE_GEST_DECENTRATA_EST, 0) -
                                              NVL(aDestS.IM_SPESE_GEST_DECENTRATA_EST, 0)),
              utuv = 'SISTEMI',
              duva = Trunc(Sysdate)
          Where esercizio = tabModuloSpeseGest(i).esercizio
          And   cd_centro_responsabilita = tabModuloSpeseGest(i).cd_centro_responsabilita
          And   pg_progetto = tabModuloSpeseGest(i).pg_progetto
          And   id_classificazione = tabModuloSpeseGest(i).id_classificazione
          And   cd_cds_area = tabModuloSpeseGest(i).cd_cds_area
          And   cd_cdr_assegnatario = '000.403.000'
          And   cd_linea_attivita = 'P0000007'
          And   ti_appartenenza = tabModuloSpeseGest(i).ti_appartenenza
          And   ti_gestione = tabModuloSpeseGest(i).ti_gestione
          And   cd_elemento_voce = tabModuloSpeseGest(i).cd_elemento_voce;

          If Sql%Rowcount != 1 Then
             IBMERR001.RAISE_ERR_GENERICO('Update 2 non logica');
          End If;

          Update voce_f_saldi_cdr_linea
          Set IM_STANZ_INIZIALE_A1 = IM_STANZ_INIZIALE_A1 +
                                             (NVL(tabModuloSpeseGest(i).IM_SPESE_GEST_ACCENTRATA_INT, 0) -
                                              NVL(aDestS.IM_SPESE_GEST_ACCENTRATA_INT, 0)) +
                                             (NVL(tabModuloSpeseGest(i).IM_SPESE_GEST_ACCENTRATA_EST, 0) -
                                              NVL(aDestS.IM_SPESE_GEST_ACCENTRATA_EST, 0)) +
                                             (NVL(tabModuloSpeseGest(i).IM_SPESE_GEST_DECENTRATA_INT, 0) -
                                              NVL(aDestS.IM_SPESE_GEST_DECENTRATA_INT, 0)) +
                                             (NVL(tabModuloSpeseGest(i).IM_SPESE_GEST_DECENTRATA_EST, 0) -
                                              NVL(aDestS.IM_SPESE_GEST_DECENTRATA_EST, 0)),
              utuv = 'SISTEMI',
              duva = Trunc(Sysdate)
          Where esercizio = tabModuloSpeseGest(i).esercizio
          And   esercizio_res = tabModuloSpeseGest(i).esercizio
          And   cd_centro_responsabilita = '000.403.000'
          And   cd_linea_attivita = 'P0000007'
          And   ti_appartenenza = 'D'
          And   ti_gestione = tabModuloSpeseGest(i).ti_gestione
          And   cd_elemento_voce = tabModuloSpeseGest(i).cd_elemento_voce;

          If Sql%Rowcount != 1 Then
             IBMERR001.RAISE_ERR_GENERICO('Update 3 non logica');
          End If;
/*
          Update voce_f_saldi_cdr_linea
          Set IM_STANZ_INIZIALE_A1 = IM_STANZ_INIZIALE_A1 +
                                             (NVL(tabModuloSpeseGest(i).IM_SPESE_GEST_ACCENTRATA_INT, 0) -
                                              NVL(aDestS.IM_SPESE_GEST_ACCENTRATA_INT, 0)) +
                                             (NVL(tabModuloSpeseGest(i).IM_SPESE_GEST_DECENTRATA_INT, 0) -
                                              NVL(aDestS.IM_SPESE_GEST_DECENTRATA_INT, 0)),
              utuv = 'SISTEMI,
              duva = Trunc(Sysdate)
          Where esercizio = tabModuloSpeseGest(i).esercizio
          And   esercizio_res = tabModuloSpeseGest(i).esercizio
          And   cd_centro_responsabilita = '999.000.000'
          And   cd_linea_attivita = 'P0000002'
          And   ti_appartenenza = 'D'
          And   ti_gestione = 'C'
          And   cd_voce = '1.01.2.01.001.1.000';

          If Sql%Rowcount != 1 Then
             IBMERR001.RAISE_ERR_GENERICO('Update 4 non logica');
          End If;

          Update voce_f_saldi_cdr_linea
          Set IM_STANZ_INIZIALE_A1 = IM_STANZ_INIZIALE_A1 +
                                             (NVL(tabModuloSpeseGest(i).IM_SPESE_GEST_ACCENTRATA_EST, 0) -
                                              NVL(aDestS.IM_SPESE_GEST_ACCENTRATA_EST, 0)) +
                                             (NVL(tabModuloSpeseGest(i).IM_SPESE_GEST_DECENTRATA_EST, 0) -
                                              NVL(aDestS.IM_SPESE_GEST_DECENTRATA_EST, 0)),
              utuv = 'SISTEMI,
              duva = Trunc(Sysdate)
          Where esercizio = tabModuloSpeseGest(i).esercizio
          And   esercizio_res = tabModuloSpeseGest(i).esercizio
          And   cd_centro_responsabilita = '999.000.000'
          And   cd_linea_attivita = 'P0000002'
          And   ti_appartenenza = 'D'
          And   ti_gestione = 'C'
          And   cd_voce = '1.01.2.01.001.1.000';

          If Sql%Rowcount != 1 Then
             IBMERR001.RAISE_ERR_GENERICO('Update 5 non logica');
          End If;
*/
       Else
          Dbms_Output.put_line('OK');
       End If;
     Exception
       When No_Data_Found Then
          Dbms_Output.put_line('NON CARICATO');
          Dbms_Output.put_line(tabModuloSpeseGest(i).esercizio);
          Dbms_Output.put_line('1: '||tabModuloSpeseGest(i).cd_centro_responsabilita);
          Dbms_Output.put_line('2: '||tabModuloSpeseGest(i).pg_progetto);
          Dbms_Output.put_line('3: '||tabModuloSpeseGest(i).id_classificazione);
          Dbms_Output.put_line('4: '||tabModuloSpeseGest(i).cd_cds_area);
          Dbms_Output.put_line('5: '||tabModuloSpeseGest(i).cd_cdr_assegnatario);
          Dbms_Output.put_line('6: '||tabModuloSpeseGest(i).cd_linea_attivita);
          Dbms_Output.put_line('7: '||tabModuloSpeseGest(i).ti_appartenenza);
          Dbms_Output.put_line('8: '||tabModuloSpeseGest(i).ti_gestione);
          Dbms_Output.put_line('9: '||tabModuloSpeseGest(i).cd_elemento_voce);
          Dbms_Output.put_line('10: '||to_char(NVL(tabModuloSpeseGest(i).IM_SPESE_GEST_ACCENTRATA_INT, 0), '999g999g999g999g990d00'));
          Dbms_Output.put_line('11: '||to_char(NVL(tabModuloSpeseGest(i).IM_SPESE_GEST_ACCENTRATA_EST, 0), '999g999g999g999g990d00'));
          Dbms_Output.put_line('12: '||to_char(NVL(tabModuloSpeseGest(i).IM_SPESE_GEST_DECENTRATA_INT, 0), '999g999g999g999g990d00'));
          Dbms_Output.put_line('13: '||to_char(NVL(tabModuloSpeseGest(i).IM_SPESE_GEST_DECENTRATA_EST, 0), '999g999g999g999g990d00'));

          diff := diff + (NVL(tabModuloSpeseGest(i).IM_SPESE_GEST_ACCENTRATA_INT, 0) +
                          NVL(tabModuloSpeseGest(i).IM_SPESE_GEST_ACCENTRATA_EST, 0) +
                          NVL(tabModuloSpeseGest(i).IM_SPESE_GEST_DECENTRATA_INT, 0) +
                          NVL(tabModuloSpeseGest(i).IM_SPESE_GEST_DECENTRATA_EST, 0));

          IBMERR001.RAISE_ERR_GENERICO('STOP');
     End;
   End Loop;

   For I In (Select * From pdg_modulo_spese_gest
             Where esercizio = aEsercizio
             And   cd_centro_responsabilita = aCdCdr
             And   cd_cdr_assegnatario = aCdCdr
             And   categoria_dettaglio = 'STI') Loop

     trovato := False;
     For y In 1..tabModuloSpeseGest.count Loop
       contatabModuloSpeseGest := contatabModuloSpeseGest + 1;

       If i.esercizio = tabModuloSpeseGest(Y).esercizio And
          i.cd_centro_responsabilita = tabModuloSpeseGest(Y).cd_centro_responsabilita And
          i.pg_progetto = tabModuloSpeseGest(Y).pg_progetto And
          i.id_classificazione = tabModuloSpeseGest(Y).id_classificazione And
          i.cd_cds_area = tabModuloSpeseGest(Y).cd_cds_area And
          i.cd_cdr_assegnatario = tabModuloSpeseGest(Y).cd_cdr_assegnatario And
          i.cd_linea_attivita = tabModuloSpeseGest(Y).cd_linea_attivita And
          i.ti_appartenenza = tabModuloSpeseGest(Y).ti_appartenenza And
          i.ti_gestione = tabModuloSpeseGest(Y).ti_gestione And
          i.cd_elemento_voce = tabModuloSpeseGest(Y).cd_elemento_voce Then
          trovato := True;
          Exit;
       End If;
     End Loop;
     If Not (trovato) Then
          Dbms_Output.put_line('DA ELIMINARE');
          Dbms_Output.put_line(i.esercizio);
          Dbms_Output.put_line('1: '||i.cd_centro_responsabilita);
          Dbms_Output.put_line('2: '||i.pg_progetto);
          Dbms_Output.put_line('3: '||i.id_classificazione);
          Dbms_Output.put_line('4: '||i.cd_cds_area);
          Dbms_Output.put_line('5: '||i.cd_cdr_assegnatario);
          Dbms_Output.put_line('6: '||i.cd_linea_attivita);
          Dbms_Output.put_line('7: '||i.ti_appartenenza);
          Dbms_Output.put_line('8: '||i.ti_gestione);
          Dbms_Output.put_line('9: '||i.cd_elemento_voce);
          Dbms_Output.put_line('10: '||to_char(NVL(i.IM_SPESE_GEST_ACCENTRATA_INT, 0), '999g999g999g999g990d00'));
          Dbms_Output.put_line('11: '||to_char(NVL(i.IM_SPESE_GEST_ACCENTRATA_EST, 0), '999g999g999g999g990d00'));
          Dbms_Output.put_line('12: '||to_char(NVL(i.IM_SPESE_GEST_DECENTRATA_INT, 0), '999g999g999g999g990d00'));
          Dbms_Output.put_line('13: '||to_char(NVL(i.IM_SPESE_GEST_DECENTRATA_EST, 0), '999g999g999g999g990d00'));

          diff := diff - (NVL(i.IM_SPESE_GEST_ACCENTRATA_INT, 0) +
                          NVL(i.IM_SPESE_GEST_ACCENTRATA_EST, 0) +
                          NVL(i.IM_SPESE_GEST_DECENTRATA_INT, 0) +
                          NVL(i.IM_SPESE_GEST_DECENTRATA_EST, 0));
     End If;
   End Loop;

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
Dbms_Output.put_line('contaaASSCDPLA: '||contaaASSCDPLA);
Dbms_Output.put_line('contatabModuloSpeseGest: '||contatabModuloSpeseGest);
Dbms_Output.put_line('Differenza: '||To_Char(diff, '999g999g999g999g990d00'));
 Exception
   When esci then null;
 End;
End;
/


