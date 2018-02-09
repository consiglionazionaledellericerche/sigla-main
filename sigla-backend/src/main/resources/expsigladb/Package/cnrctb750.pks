CREATE OR REPLACE PACKAGE CNRCTB750 AS
--==================================================================================================
--
-- CNRCTB750 - package di utilit? per gestione cassiere
--
-- Date: 08/01/2004
-- Version: 1.16
--
-- Dependency:
--
-- History:
--
-- Date: 20/03/2003
-- Version: 1.0
-- Creazione package
--
-- Date: 15/04/2003
-- Version: 1.1
-- Modifica struttura EXT_CASSIERE00
--
-- Date: 16/04/2003
-- Version: 1.2
-- Introdotta gestione batch
--
-- Date: 17/04/2003
-- Version: 1.3
-- Completamento
--
-- Date: 22/04/2003
-- Version: 1.4
-- Corretta gestione sospesi da Banca d'Italia
--
-- Date: 26/04/2003
-- Version: 1.5
-- Gestione registrazione del log collegato al processo dell'interfaccia
--
-- Date: 26/05/2003
-- Version: 1.6
-- Gestione annullamento sospesi e caricamento riscontri
--
-- Date: 29/05/2003
-- Version: 1.7
-- Fix nuove funzionalita
--
-- Date: 05/06/2003
-- Version: 1.8
-- Gestione dell'update/annullamento di righe in BFRAME_BLOB
--
-- Date: 15/09/2003
-- Version: 1.9
-- Aggiornamento documentazione
--
-- Date: 32/10/2003
-- Version: 1.10
-- Introduzione della gestione di pagamento relativo alla Banca d'Italia
-- Introduzione del nuovo tracciato record dei Log
-- Azzeramento delle variabili aCds e aR01 all'interno del ciclo di calcolo
--
-- Date: 27/10/2003
-- Version: 1.11
-- Fix di non processo reversali e mandati annullati
-- Fix segnalazione errore nel caso la reversale ente non sia da Banca d'Italia mentre lo ? il riscontro
-- della Banca.
-- Segnalazione di errore nel caso la reversale sia parte da Banca d'Italia e parte no.
--
-- Date: 27/10/2003
-- Version: 1.12
-- Fix minori: errore di log della reversale/mandato non trovato/annullato.
-- Fix segnalazione errore nel caso la reversale sia da Banca d'Italia mentre il non lo ? il riscontro
-- della Banca.
--
-- Date: 20/11/2003
-- Version: 1.13
-- Fix distinta cassiere: errore nella selezione dei mandati/reversali da inserire in una distinta.
--  Se uno dei documenti inseriti ? collegato ad un compenso al quale sono collegati Mand./Rev. CORI,
--	(tabella ASS_COMP_DOC_CONT_NMP), devono essere indicati anche quei Mand./Rev.
--
-- Date: 25/11/2003
-- Version: 1.14
-- Fix sul metodo checkDocContForDistCas: errore nella ricerca dei doc. cont. associati ad un eventuale
-- compenso.
--
-- Date: 27/11/2003
-- Version: 1.15
-- Fix sul metodo checkDocContForDistCas: Lock delle tabelle DISTINTA_CASSIERE e ASS_COMP_DOC_CONT_NMP.
--
-- Date: 08/01/2004
-- Version: 1.16
-- Fix mancato aggiornamento dello stato processato del record di storno sospeso
-- Aggiornamento di utuv,duva,pg_ver_rec aggiornamento dello stato processato del record di storno/inserimento sospeso
--
--==================================================================================================
--
-- Constants
--

CONTO_CORRENTE_SPECIALE CONSTANT VARCHAR2(50) := 'CONTO_CORRENTE_SPECIALE';
CONTO_CORRENTE_ENTE CONSTANT VARCHAR2(100) := 'ENTE';

-- ABIBNL determinato utilizzato CONFIGURAZIONE_CNR
ABIBNL number(5);

--
-- Functions e Procedures
--
 procedure checkRigaFile(aRiga ext_cassiere00%rowtype);

-- Pre-post-name: Invocazione del batch di processing dell'interfaccia di ritorno cassiere
-- pre: viene richiesta l'elaborazione dell'interfaccia di ritorno cassiere nell'esercizio spec. per il file specificato
--      dall'utente specificato
-- post: viene creato un job oracle (che invoca job_interfaccia_cassiere) sottomesso per l'esecuzione in background e
--       notificato all'utente il completamento dell'operazione di sottomissione.
--
-- Parametri:
--   aEs -> esercizio contabile
--   aNomeFile -> Nome del file dati
--   aUser -> utente che effettua l'operazione
 procedure processaInterfaccia(aEs number, aNomeFile varchar2,aUser varchar2);

-- Job di caricamento dell'interfaccia di ritorno cassiere per la gestione di:
--  1. Sospesi (Inserimento/Cancellazione)
--  2. Riscontri (Solo inserimento)
--
-- Pre-post-name: Record di testat T01 non trovato
-- pre: Nel loop delle righe del file cassiere viene trovato un dato di tipo TXX con XX <> 01 prima di trovare un record di tipo T01
-- post: Viene sollevata un'eccezione
--
-- Pre-post-name: Record di testat T01 con esercizio diverso dall'esercizio specificato
-- pre: L'esercizio aEs ? diverso da quello del record di tipo T01 trovato
-- post: Viene sollevata un'eccezione
--
-- Pre-post-name: La reversale o il mandato risultano gi? parzialmente riscontrata manualmente
-- pre: Esistono riscontri manuali per il documento autorizzatorio
-- post: viene sollevata un'eccezione
--
-- Pre-post-name: La reversale o il mandato risultano annullati
-- pre: La reversale ed il mandato specificato sono annullati
-- post: viene sollevata un'eccezione
--
-- Pre-post-name: L'importo dei riscontri supera l'importo della reversale (mandato)
-- pre: il totale riscontrato risulta superiore all'importo del documento
-- post: viene sollevata un'eccezione
--
-- Pre-post-name: Reversale CNR non da Banca d'Italia mentre il riscontro della Banca ? da Banca d'Italia
-- pre: La Reversale CNR non ? da Banca d'Italia mentre il riscontro specificato si
-- post: viene sollevata un'eccezione
--
-- Pre-post-name: Reversale CNR da Banca d'Italia mentre il riscontro della Banca non ? da Banca d'Italia
-- pre: La Reversale CNR ? da Banca d'Italia mentre il riscontro specificato nell'interfaccia no
-- post: viene sollevata un'eccezione
--
-- Pre-post-name: Reversale in parte da Banca d'Italia ed in parte no
-- pre: La Reversale risulta per parte da Banca d'Italia e per parte no
-- post: viene sollevata un'eccezione
--
-- Pre-post-name: Documento da processare di CDS con specifica cc banca d'italia
-- pre: NUMERO_CONT_TESO del record T30 = '000000218154' e CDS letto da T01 diverso da Ente
-- post: viene sollevata un'eccezione
--
-- Pre-post-name: Tipo di oridinativo non compatibile (T30)
-- pre:  se il tipo di ordinativo (TIPO_ORDINATIVO) ? diverso da 'R' o 'M' su record T30
-- post: viene sollevata un'eccezione
--
-- Pre-post-name: Stato ordinativo non compatibile per record T30
-- pre: Lo stato dell'ordinativo (STATO_ORDINATIVO) = '03' e il segno (SEGNO_IMPORTO_OPERAZIONE) = 1 (record T30)
-- post: viene sollevata un'eccezione
--
-- Pre-post-name: Sospeso gi? annullato
-- pre: Richiesto annullamento di sospeso (record T32 e STATO_SOSP del record = '05') e il sospeso ? gi? annullato
-- post: viene sollevata un'eccezione
--
-- Pre-post-name: Sospeso gi? utilizzato
-- pre: Richiesto annullamento di sospeso (record T32 e STATO_SOSP del record = '05') e importo associato a doc.
--      autorizzatori o 1210 ? diverso da 0
-- post: viene sollevata un'eccezione
--
-- Pre-post-name: Caricamento dell'interfaccia di ritorno cassiere
-- pre: viene richiesta l'elaborazione dell'interfaccia di ritorno cassiere nell'esercizio spec. per il file specificato
--      dall'utente specificato
-- post: Vengono eseguite le seguenti operiazioni:
--       lock della tabella EXT_CASSIERE00 che contiene il file con il tracciato della banca
--       Per ogni record in STATO iniziale presente in EXT_CASSIERE00 per l'esercizio e nome file specificati
--       ordinati per progressivo record:
--         1. Il record ? di tipo T01 (record aR01)
--         2. Il record ? di tipo T30 (Riscontri/Mandati record aR30)
--            	 Se lo stato dell'ordinativo (STATO_ORDINATIVO) = '03' e il segno (SEGNO_IMPORTO_OPERAZIONE) = 1
--                Determina il CDS dal record T01 corrispondente
--                Se il tipo di ordinativo (TIPO_ORDINATIVO) ? 'R' (Reversale)
--                  > Crea il record di riscontro (aRisc) in tabella SOSPESO con le seguenti caratteristiche:
--                            ...
--							aRisc.DT_REGISTRAZIONE:=aR30.DATA_ESECUZIONE (T30)
--                            aRisc.DS_ANAGRAFICO:=aR30.DESCRIZIONE_BENEFICIARIODEBITO (T30)
--                            aRisc.CAUSALE:=aR30.CAUSALE;
-- 	                        Se aR01.NUMERO_CONT_TESO = '000000218154' allora
--                             Se il CDS ? l'Ente
--                        	  aRisc.TI_CC_BI:='B'; (Tipo conto corrente da Banca d'Italia)
--                        	altrimenti
--                             aRisc.TI_CC_BI:='C'; (Conto corrente CDS)
--                            aRisc.IM_SOSPESO:=aR30.IMPORTO_OPERAZIONE;
--                            aRisc.IM_ASSOCIATO:=aR30.IMPORTO_OPERAZIONE;
--                            ...
--                  > Aggiorno il dettaglio det ETR
--                  > Aggiorna l'importo incassato della reversale
--                  > Se il totale riscontrato alla data = importo reversale
--                         Invoca CNRCTB037.riscontroReversale per le azioni di aggiornamento dei saldi Incassato
--                altrimenti (Mandato 'M')
--                  > Crea il record di riscontro (aRisc) in tabella SOSPESO con le seguenti caratteristiche:
--                            ...
--							aRisc.DT_REGISTRAZIONE:=aR30.DATA_ESECUZIONE (T30)
--                            aRisc.DS_ANAGRAFICO:=aR30.DESCRIZIONE_BENEFICIARIODEBITO (T30)
--                            aRisc.CAUSALE:=aR30.CAUSALE;
-- 	                        Se aR01.NUMERO_CONT_TESO = '000000218154' allora
--                             Se il CDS ? l'Ente
--                        	  aRisc.TI_CC_BI:='B'; (Tipo conto corrente da Banca d'Italia)
--                        	altrimenti
--                             aRisc.TI_CC_BI:='C'; (Conto corrente CDS)
--                            aRisc.IM_SOSPESO:=aR30.IMPORTO_OPERAZIONE;
--                            aRisc.IM_ASSOCIATO:=aR30.IMPORTO_OPERAZIONE;
--                            ...
--                  > Aggiorno il dettaglio det USC
--                  > Aggiorna l'importo incassato del mandato
--                  > Se il totale riscontrato alla data = importo mandato
--                         Invoca CNRCTB037.riscontroMandato per le azioni di aggiornamento dei saldi Pagato
--         3. Il record ? di tipo T32 (Sospesi, record aR32)
--             	 Se GESTIONE STORNO (aR32.STATO_SOSP = '05')
--                 Viene aggiornato lo stato a stornato (fl_stornato) del sopeso padre e dei suoi figli
--             	 Altrimenti se GESTIONE INSERIMENTO (aR32.STATO_SOSP = '02') e aR32.IMPORT = aR32.IMPORT_RESI_OPE
--               e aR32.SEGNO_IMPO = aR32.SEGNO_IMPO_RESI_OPER ed aR32.SEGNO_IMPO = 1
--                 Viene creato un record nella tabella SOPESO per il sospeso padre (aSospeso) con le sguenti caratteristiche
--				      ...
--                    aSospeso.CD_SOSPESO:=aR32.NUMERO_SOSP;
--                    aSospeso.DT_REGISTRAZIONE:=TO_DATE(aR32.DATA_OPER,'YYYYMMDD')
--                    aSospeso.DS_ANAGRAFICO:=aR32.DESCRI_BENE
--                    aSospeso.CAUSALE:=aR32.CAUSAL_1A_PART concatenata con aR32.CAUSAL_2A_PART
--                    -- identificazione banca d'Italia solo per sospesi ENTE
--                    Se il aR01.NUMERO_CONT_TESO = '000000218154'
--                     Se il cds ? l'Ente
--                      aSospeso.TI_CC_BI:='B' (Banca d'Italia)
--                    altrimenti
--                     aSospeso.TI_CC_BI:='C'
--                    aSospeso.IM_SOSPESO:=aR32.IMPORT*aR32.SEGNO_IMPO
--                 Viene creato un record per il sospeso figlio (aSF) con la seguente caratteristica:
--                 Se aCds.cd_tipo_unita diversa da Ente
--                  aSF.CD_CDS_ORIGINE:=aSospeso.CD_CDS
--                  aSF.CD_UO_ORIGINE:=null
--                  aSF.STATO_SOSPESO:='A' --ASSEGNATO A CDS
--                 altrimenti
--                  aSF.CD_CDS_ORIGINE:=null
--                  aSF.CD_UO_ORIGINE:=null
--                  aSF.STATO_SOSPESO:='I' --INIZIALE
--       Al termine dell'operazione viene mandato attraverso il package 205 un messaggio applicativo all'utente
--       con informazioni riassuntive sull'operazione.
--       Gli errori vengono loggati con l'usuale meccanismo di log.
--       Se una riga di interfaccia viene processata con successo, viene effettuato anche un commit.
--       Altrimenti viene effetuato un rollback.
--
-- Parametri:
--   job,pg_exec,next_date -> parametri sistemistici dell'interfaccia di invocazione JOBs Oracle
--   aEs -> esercizio contabile
--   aNomeFile -> Nome del file dati
--   aUser -> utente che effettua l'operazione

 procedure job_interfaccia_cassiere (
  job NUMBER,
  pg_exec NUMBER,
  next_date DATE,
  aEs number,
  aNomeFile varchar2,
  aUser varchar2
 );

 procedure carica_ext_cassiere(aTipo varchar2, aPath varchar2, aFilename varchar2, aClob in out clob);
 procedure checkRemoveFile(aTipo varchar2, aPath varchar2, aFilename varchar2, aClob in out clob);

-- Pre-post-name: Controlla tutti i documenti inseriti in distinta.
-- pre: Cicla su tutti i documenti inseriti nella distinta.

-- post: Per ogni documento inserito in distinta, controlla nella tabella ASS_COMP_DOC_CONT_NMP
-- 		se ? associato ad un compenso. In caso affermativo, controlla che tutti i Mand./Rev. collegati
--		al compenso, siano presenti nella distinta.
--		Se un documento ? associato a Compenso il quale ? associato a Mand./Rev. CORI non presenti nella distinta,
--		viene lanciata una eccezione indicando all'utente che il documento ? associato a
-- 		doc. cont. legati ad un Compenso che ha dei Mand./Rev. che devono essere inclusi nella distinta.
--
-- Parametri:
--   aCdCds -> Cd_Cds della distinta
--   aEs 	-> esercizio della distinta
--   aCdUO 	-> Cd_uo della distinta
--   aPgDistinta -> progressivo della distinta

 procedure checkDocContForDistCas(
  aCdCds varchar2,
  aEs number,
  aCdUO varchar2,
  aPgDistinta number
 );
 procedure checkDocContForDistCasAnn(
   aCdCds varchar2,
   aEs number,
   aCdUO varchar2,
   aPgDistinta number
 );
END;
/


CREATE OR REPLACE PACKAGE BODY CNRCTB750 AS
 procedure checkRemoveFile(aTipo varchar2, aPath varchar2, aFilename varchar2, aClob in out clob) is
  aNum number;
 begin
  aNum:=0;
  lock table ext_cassiere00 in exclusive mode;
  select count(*) into aNum from ext_cassiere00 where
           esercizio =  substr(aFileName,1,4)
	   and nome_file = aFileName
	   and stato = CNRCTB755.STATO_RECORD_PROCESSATO;
  if aNum > 0 then
   IBMERR001.RAISE_ERR_GENERICO('Parte del file ? gi? stata processata in interfaccia ritorno cassiere');
  else
   delete from ext_cassiere00 where
           esercizio =  substr(aFileName,1,4)
	   and nome_file = aFileName;
  end if;
 end;

 procedure carica_ext_cassiere(aTipo varchar2, aPath varchar2, aFilename varchar2, aClob in out clob) is
  aString       varchar2(4000);
  aSPos         number:=1;
  aExtCassiere  ext_cassiere00%rowtype;
  aPgRec        number;
  aTSNow        DATE := sysdate;
  aUser         varchar2(20) := '$$$$$CASSIERE00$$$$$';

 begin
  aPgRec:=0;

  Loop

   if IBMUTL005.NEXTLINE(aClob, aSPos,aString) = -1 then
    exit;
   end if;

   aPgRec := aPgRec+1;
   aExtCassiere.ESERCIZIO                := substr(aFileName, 1, 4);
   aExtCassiere.NOME_FILE                := aFileName;
   aExtCassiere.PG_REC                   := aPgRec;
   aExtCassiere.TR                       := substr(aString, 1, 2);
   aExtCassiere.STATO                    := CNRCTB755.STATO_RECORD_INIZIALE;
   aExtCassiere.DATA                     := aString;
   aExtCassiere.DACR                     := aTSNow;
   aExtCassiere.UTCR                     := aUser;
   aExtCassiere.DUVA                     := aTSNow;
   aExtCassiere.UTUV                     := aUser;
   aExtCassiere.PG_VER_REC               := 1;
   aExtCassiere.CD_CDS_SR                := null;
   aExtCassiere.ESERCIZIO_SR             := null;
   aExtCassiere.TI_ENTRATA_SPESA_SR      := null;
   aExtCassiere.TI_SOSPESO_RISCONTRO_SR  := null;
   aExtCassiere.CD_SR                    := null;

   CNRCTB755.INS_EXT_CASSIERE00(aExtCassiere);

  End Loop;

 end;

 function getCds(aEs number, aR01 CNRCTB755.T01) return unita_organizzativa%rowtype is
  aCDS unita_organizzativa%rowtype;
  aAssCass EXT_CASSIERE_CDS%rowtype;
  aCdUO varchar2(30);
  tesoreria_unica char(1);
 begin
	-- modifica in sospeso (Continuano ad arrivare i sospesi per gli istituti con il proteo)
   --select fl_tesoreria_unica into tesoreria_unica from parametri_cnr where esercizio = aEs;
   --if(tesoreria_unica ='N') then
		  begin
		   select * into aAssCass from EXT_CASSIERE_CDS where
		        esercizio = aEs
		    and codice_proto = substr(aR01.CODICE_ENTE,3,4);
		   aCDS:=CNRCTB020.GETUOVALIDA(aEs,aAssCass.cd_cds);
		  exception
		   when NO_DATA_FOUND then
		    IBMERR001.RAISE_ERR_GENERICO('Nessun CDS risulta associato al Codice Ente '||aR01.codice_ente||' per l''esercizio '||aEs||' nella tabella EXT_CASSIERE_CDS');
		  end;
   --else
     --aCDS:=CNRCTB020.GETUOENTE(aEs);
	 --end if;
  return aCDS;
 end;

 function getDesc(aSos sospeso%rowtype) return varchar2 is
 begin
  return ' n.'||aSos.cd_sospeso||' cds:'||aSos.cd_cds||' es.:'||aSos.esercizio;
 end;

 procedure checkRigaFile(aRiga ext_cassiere00%rowtype) is
 begin
  if (length(aRiga.nome_file)!=16) then
   IBMERR001.RAISE_ERR_GENERICO('Nome file caricato non compatibile');
  end if;
 end;


 procedure job_interfaccia_cassiere (
  job NUMBER,
  pg_exec NUMBER,
  next_date DATE,
  aEs number,
  aNomeFile varchar2,
  aUser varchar2
 ) is
  aR01 CNRCTB755.T01;
  aR32 CNRCTB755.T32;
  aR30 CNRCTB755.T30;
  aEsercizio  number;
  aCds unita_organizzativa%rowtype;
  aSospeso sospeso%rowtype;
  aSF sospeso%rowtype;
  aTSNow date:=sysdate;
  aNumProcessati number:=0;
  aNumOk number:=0;
  aLog EXT_CASSIERE00_LOGS%rowtype;
  aMan mandato%rowtype;
  aRev reversale%rowtype;
  aDetEtr sospeso_det_etr%rowtype;
  aDetUsc sospeso_det_usc%rowtype;
  aRisc sospeso%rowtype;
  aTR sospeso%rowtype;
  conta_err             NUMBER;
  NUMERO                NUMBER;
  aRecScarto            EXT_CASSIERE00_SCARTI%Rowtype;
  importo_sospeso_esistente     NUMBER;
-- Massimo
   lmodPagBI boolean;
   lNumPagBI number;
   lNumPagNonBI number;
   lTiRiga varchar2(2);
-- Massimo
  tesoreria_unica char(1);
 begin
   select fl_tesoreria_unica into tesoreria_unica from parametri_cnr where esercizio = aEs;

  -- Lancio start esecuzione log
  IBMUTL210.logStartExecutionUpd(pg_exec, CNRCTB755.LOG_TIPO_INTERF_CASS00, job,
                                 'Caricamento interfaccia ritorno cassiere. File:'||aNomeFile||' Utente:' || nvl(aUser,'UNDEF'),
				 'CICF-'||aNomeFile||' Start:' || TO_CHAR(sysdate,'YYYY/MM/DD HH-MI-SS'));

  -- Check parametri
  if aEs is null or aNomeFile is null or aUser is null then
   IBMERR001.RAISE_ERR_GENERICO('Esercizio, Nome file cassiere o Utente indefiniti');
  end if;

  aLog.ESERCIZIO        := aEs;
  aLog.NOME_FILE        := aNomeFile;
  aLog.PG_ESECUZIONE    := pg_exec;
  aLog.DACR             := aTSNow;
  aLog.UTCR             := aUser;
  aLog.DUVA             := aTSNow;
  aLog.UTUV             := aUser;
  aLog.PG_VER_REC       := 1;

  CNRCTB755.ins_EXT_CASSIERE00_LOGS(aLog);

  ABIBNL := CNRCTB015.GETVAL01PERCHIAVE(CONTO_CORRENTE_SPECIALE,CONTO_CORRENTE_ENTE);

  aR01 := null; -- RECORD DI TIPO T01

  -- Lock di tutta la tabella di interfaccia durante il processing
  lock table EXT_CASSIERE00 in exclusive mode;

  -- LOOP SULLA TABELLA DOVE SONO STATI CARICATI I RECORD DELLA GIORNALIERA PRENDENDO SOLO QUELLI IN STATO "INIZIALE" E NON "PROCESSATO"

  For aR in (Select * from EXT_CASSIERE00
             Where    esercizio = aEs And
                      nome_file = aNomeFile And
                      stato     = CNRCTB755.STATO_RECORD_INIZIALE
             Order by pg_rec asc) Loop

   Begin -- CHIUDE ALLA FINE, APPENA PRIMA DELL'END LOOP SULLE RIGHE DI EXT_CASSIERE00 E POI INSERISCE I LOG

     lTiRiga := null;

     If CNRCTB755.isT01(aR.data) Then
	 -- Iacca Inizio
  	 aR01 := null;
  	 aCds := null;
	 -- Iacca Fine
	 	lTiRiga := '01';
     	aR01    := CNRCTB755.parseT01(aR.data); -- SE IL RECORD E' DI TIPO 01 SCARICA LA COLONNA "DATA" - VARCHAR2(1000) - NELLA STRUTTURA DEL RECORD T01
     	-- 25/01/2018 vengono inviati insieme riscontri di+esercizi nello stesso file
      aEsercizio :=aR01.anno_eser;
     End If;

-- ====================================================================================================
--                                                RISCONTRI
-- ====================================================================================================

If CNRCTB755.isT30(aR.data) Then

  lTiRiga := '30';
  aNumProcessati:=aNumProcessati+1;

  -- Check esistenza record di testata

  if aR01.TIPO_RECO is null then
       IBMERR001.RAISE_ERR_GENERICO('Tipo record (TIPO_RECO) del record TR01 nullo');
  end if;

	-- if aR01.anno_eser <> aEs then
      	--IBMERR001.RAISE_ERR_GENERICO('Anno di esercizio (ANNO_ESER) del record TR01 diverso dall''esercizio di bilancio '||aEs);
 	--end if;

  aR30 := CNRCTB755.parseT30 (aR.data); -- SE IL RECORD E' DI TIPO 30 SCARICA LA COLONNA "DATA" - VARCHAR2(1000) - NELLA STRUTTURA DEL RECORD T30

  -- controlla solo che il numero dell'ordinativo non contenga caratteri (a partire dal secondo carattere del campo NUMERO_ORDINATIVO)
  Begin
    NUMERO := Substr(aR30.numero_ordinativo, 2);
  Exception
    When Others Then
      aCds := getCds(aEs, aR01); -- PER POTER DARE IL CDS NEGLI SCARTI MI OCCORRE FARE LA GET
      IBMERR001.RAISE_ERR_GENERICO('Il numero dell''ordinativo contiene caratteri alfabetici: '||Substr(aR30.numero_ordinativo, 2));
  End;

  If aR30.STATO_ORDINATIVO = '03' and aR30.SEGNO_IMPORTO_OPERAZIONE = 1 then

        aCds := getCds(aEs, aR01);

-- ====================================================================================================
--                                                REVERSALE
-- ====================================================================================================

If aR30.TIPO_ORDINATIVO = 'R' Then

                        aRev:=null;
	                			aRev.esercizio:=aEsercizio;
	                			if(tesoreria_unica ='N') then
	                				aRev.cd_cds:=aCds.cd_unita_organizzativa;
	                			end if;
	                aRev.pg_reversale:= Substr(aR30.numero_ordinativo, 2);

        	   -- Reversale
  	      Begin
   	         Select * into aRev
   	         From   reversale
   	         Where  esercizio = aRev.esercizio And
   	                cd_cds like decode(tesoreria_unica,'N',aCds.cd_unita_organizzativa,'%') And
   	                pg_reversale = Substr(aR30.numero_ordinativo, 2) And
   	                stato <> CNRCTB038.STATO_AUT_ANN
 	         For update nowait;

 	      Exception when NO_DATA_FOUND Then

 	         Select Count(*)
 	         Into   conta_err
   	         From   reversale
   	         Where  esercizio = aRev.esercizio And
   	                cd_cds like decode(tesoreria_unica,'N',aCds.cd_unita_organizzativa,'%') And
   	                pg_reversale = Substr(aR30.numero_ordinativo, 2) And
   	                stato = CNRCTB038.STATO_AUT_ANN;
                 If Conta_err > 0 Then
                   IBMERR001.RAISE_ERR_GENERICO('Il riscontro di euro '||Ltrim(To_Char(aR30.importo_operazione, '999g999g999g999g999g990d00'))||' fa riferimento alla reversale '||aCds.cd_unita_organizzativa||'/'||aEsercizio||'/'||Substr(aR30.numero_ordinativo, 2)||' che risulta annullata.');
                 Else
                   IBMERR001.RAISE_ERR_GENERICO('Il riscontro di euro '||Ltrim(To_Char(aR30.importo_operazione, '999g999g999g999g999g990d00'))||' fa riferimento alla reversale '||aCds.cd_unita_organizzativa||'/'|| aEsercizio||'/'||Substr(aR30.numero_ordinativo, 2)||' che non esiste.');
                 End If;
 	      End;

	   -- *************************************************************
	   -- Iac Inizio
	   -- *************************************************************
	   -- Recupero informazioni modalita pagamento della reversale riga
	   -- per capire se la mod pag ? Banca di Italia o meno

	   lNumPagBI    := 0;
	   lNumPagNonBI := 0;
           lModPagBI    := false;

	   Select sum(decode(rifp.ti_pagamento,'I',1,0)), sum(decode(rifp.ti_pagamento,'I',0,1))
	   Into   lNumPagBI, lNumPagNonBI
	   From   reversale_riga revr, rif_modalita_pagamento rifp
	   Where  revr.cd_Cds = aRev.cd_Cds And
	          revr.esercizio = aRev.esercizio And
	          revr.pg_reversale = aRev.pg_reversale And
	          rifp.cd_modalita_pag = revr.cd_modalita_pag;

	   -- se num_pag_bi > 0 allora esistono reversali riga che hanno modalit? pagamento con Banca Italia

	   if lNumPagBI > 0 then
	   	  -- Se num_pag_non_bi = 0 allora tutte le reversali riga hanno mod pagamento con Banca Italia
	   	  if lNumPagNonBI = 0 then
		  	 lModPagBI := true;
		  else
	   	  	 -- Se num_pag_non_bi > 0 allora c'? un errore nella reversale che non pu? essere per parte da banca d'Italia e per parte no
		  	 IBMERR001.RAISE_ERR_GENERICO('La reversale '||CNRCTB038.getDesc(aRev)||' possiede modalit? d''incasso diverse (sia Banca d''Italia che non Banca d''Italia');
		  end if;
	   else
		  	 lModPagBI := false;
	   end if;

	   -- *************************************************************
	   -- Iac Fine
	   -- *************************************************************

	   -- Verifico che la reversale non abbia gi? riscontri manuali in tal caso blocco l'aggiornamento

           Declare
	    aLOCNUM number(10);

	   Begin
	    Select distinct 1
	    Into  aLOCNUM
	    From  sospeso a
	    Where cd_cds = aRev.cd_cds And
	          esercizio = aRev.esercizio And
	          ti_entrata_spesa = 'E' And
	          ti_sospeso_riscontro = 'R' And
	              not exists (Select 1
	                          From  ext_cassiere00
	                          Where cd_cds_sr = a.cd_cds And
	                                esercizio_sr = a.esercizio And
	                                ti_entrata_spesa_sr = a.ti_entrata_spesa And
	                                ti_sospeso_riscontro_sr = a.ti_sospeso_riscontro And
	                                cd_sr = a.cd_sospeso) -- da flusso
		      and exists (Select 1
		                  from sospeso_det_etr
		                  Where cd_cds = a.cd_cds And
		                        esercizio = a.esercizio And
		                        pg_reversale = aRev.pg_reversale And
		                        ti_entrata_spesa = a.ti_entrata_spesa And
		                        ti_sospeso_riscontro = a.ti_sospeso_riscontro And
		                        cd_sospeso = a.cd_sospeso  and
		                        sospeso_det_etr.stato !='A' );

                IBMERR001.RAISE_ERR_GENERICO('Impossibile caricare tramite flusso il riscontro di '||Ltrim(To_Char(aR30.importo_operazione, '999g999g999g999g999g990d00'))||
                         ' sulla reversale '||CNRCTB038.getDesc(aRev)||', essa risulta gi? riscontrata manualmente (anche solo parzialmente).');

	    Exception when NO_DATA_FOUND then
	       Null;
	    End;


           -- controllo lo sfondamento del riscontrato (a questo punto automatico, avendo gi? controllato l'inesistenza di riscontri manuali)

   	   If CNRCTB048.getImRiscontratoManRev(aRev.cd_cds, aRev.esercizio, aRev.pg_reversale, 'R') = aRev.im_reversale Then
             IBMERR001.RAISE_ERR_GENERICO('La reversale '||CNRCTB038.getDesc(aRev)||' risulta gi? completamente riscontrata (totale dei riscontri ed importo '||
                                          ' della reversale pari a '||Ltrim(To_Char(aRev.im_reversale, '999g999g999g999g999g990d00'))||'). E'' impossibile quindi '||
                                          ' riscontrarla ulteriormente tramite flusso per '||Ltrim(To_Char(aR30.importo_operazione, '999g999g999g999g999g990d00'))||'.');
 	   Elsif CNRCTB048.getImRiscontratoManRev(aRev.cd_cds, aRev.esercizio, aRev.pg_reversale, 'R') + aR30.importo_operazione > aRev.im_reversale then
             IBMERR001.RAISE_ERR_GENERICO('Impossibile riscontrare la reversale '||CNRCTB038.getDesc(aRev)||
                                ' per '||Ltrim(To_Char(aR30.importo_operazione, '999g999g999g999g999g990d00'))||'. Essa risulta gi? riscontrata per '||
                                Ltrim(To_Char(CNRCTB048.getImRiscontratoManRev(aRev.cd_cds, aRev.esercizio, aRev.pg_reversale, 'R'), '999g999g999g999g999g990d00'))||
                                ' e pertanto il totale dei riscontri supererebbe l''importo della reversale stessa che ? di '||
                                Ltrim(To_Char(aRev.im_reversale, '999g999g999g999g999g990d00')));
 	   End If;


 	   -- Preparo l'inserimento del riscontro
       aRisc.CD_CDS:=aRev.cd_cds;
       aRisc.ESERCIZIO:=aRev.esercizio;
       aRisc.TI_ENTRATA_SPESA:='E';
       aRisc.TI_SOSPESO_RISCONTRO:='R';
       aRisc.CD_SOSPESO:=CNRCTB038.nextProgressivoRiscontro(aRev.cd_cds, aRev.esercizio, 'E');
       aRisc.CD_CDS_ORIGINE:=aRev.cd_cds;
       aRisc.CD_UO_ORIGINE:=null;
       aRisc.DT_REGISTRAZIONE:=TO_DATE(aR30.DATA_ESECUZIONE,'YYYYMMDD');
       aRisc.DS_ANAGRAFICO:=aR30.DESCRIZIONE_BENEFICIARIODEBITO;
       aRisc.CAUSALE:=aR30.CAUSALE;

  If aR01.NUMERO_CONT_TESO = '000000218154' then
   --      ****************************************
   --      Iacca Inizio
   --      ****************************************
        -- if aCds.cd_tipo_unita = CNRCTB020.TIPO_ENTE then

        If aCds.cd_tipo_unita = CNRCTB020.TIPO_ENTE Then
             If lModPagBI then
 	       	  aRisc.TI_CC_BI := 'B';
	     Else
	      	  -- Se la reversale non ? da Banca d'Italia mentre il ricosntro della Banca ? da Banca d'Italia viene sollevata un'eccezione
		  IBMERR001.RAISE_ERR_GENERICO('La reversale '||CNRCTB038.getDesc(aRev)||' non ? da Banca d''Italia mentre il riscontro ? sul conto 218154');
	     End if;
		-- ************************************************
		-- Iacca Fine Modificata IF inserendo and lModPagBI
		-- ************************************************
  	 Else
          IBMERR001.RAISE_ERR_GENERICO('Il riscontro per un CDS ('||aCds.cd_unita_organizzativa||') non pu? essere da Banca d''Italia sul conto 218154');
 	 End If;
  Else
        If lModPagBI then
	  	-- Se il riscontro della banca non ? da Banca d'Italia ma la reversale SI viene sollevata un'eccezione
	  	 				aRisc.TI_CC_BI := 'B';
     	        --IBMERR001.RAISE_ERR_GENERICO('La reversale '||CNRCTB038.getDesc(aRev)||' ? da Banca d''Italia mentre il Riscontro ? su un conto diverso ('||aR01.NUMERO_CONT_TESO||')');
	else
                aRisc.TI_CC_BI:='C';
  	end if;
  End If;

       aRisc.FL_STORNATO:='N';
       aRisc.IM_SOSPESO:=aR30.IMPORTO_OPERAZIONE;
       aRisc.IM_ASSOCIATO:=aR30.IMPORTO_OPERAZIONE;
       aRisc.STATO_SOSPESO:='N';
       aRisc.DACR:=aTSNow;
       aRisc.UTCR:=aUser;
       aRisc.UTUV:=aUser;
       aRisc.DUVA:=aTSNow;
       aRisc.PG_VER_REC:=1;
       aRisc.IM_ASS_MOD_1210:=0;
       aRisc.CD_SOSPESO_PADRE:=null;
       aRisc.CD_PROPRIO_SOSPESO:=null;

       -- inserisce il riscontro
       CNRCTB038.INS_SOSPESO(aRisc);

     -- e mette il record a processato
     Update ext_cassiere00
     Set    STATO                   = CNRCTB755.STATO_RECORD_PROCESSATO,
            cd_cds_sr               = aRisc.cd_cds,
            esercizio_sr            = aRisc.esercizio,
            ti_entrata_spesa_sr     = aRisc.ti_entrata_spesa,
            ti_sospeso_riscontro_sr = aRisc.ti_sospeso_riscontro,
	    cd_sr                   = aRisc.cd_sospeso,
            duva                    = aRisc.duva,
	    utuv                    = aRisc.utuv,
	    pg_ver_rec              = pg_ver_rec+1
     Where  esercizio = aR.esercizio And
            nome_file = aR.nome_file And
            pg_rec    = aR.pg_rec;

 	   -- Aggiorno il dettaglio det USC
 	   aDetEtr.CD_CDS       := aRev.cd_cds;
           aDetEtr.ESERCIZIO    := aRev.esercizio;
           aDetEtr.PG_REVERSALE := aRev.pg_reversale;
           aDetEtr.TI_ENTRATA_SPESA := 'E';
           aDetEtr.TI_SOSPESO_RISCONTRO := 'R';
           aDetEtr.CD_SOSPESO   := aRisc.cd_sospeso;
           aDetEtr.IM_ASSOCIATO := aRisc.IM_SOSPESO;
           aDetEtr.STATO        := CNRCTB038.STATO_SOSPESO_DET_DEFAULT;
           aDetEtr.DACR         := aTSNow;
           aDetEtr.UTCR         := aUser;
           aDetEtr.UTUV         := aUser;
           aDetEtr.DUVA         := aTSNow;
           aDetEtr.PG_VER_REC   := 1;
           aDetEtr.CD_CDS_REVERSALE :=aRev.cd_cds;
           CNRCTB038.INS_SOSPESO_DET_ETR(aDetEtr);

           -- Aggiorno l'importo incassato della reversale
  	   Update reversale
  	   Set    im_incassato = CNRCTB048.getImRiscontratoManRev(aRev.cd_cds, aRev.esercizio, Substr(aR30.numero_ordinativo, 2), 'R'),
                  duva = aRisc.duva,
		  utuv=aRisc.utuv,
		  pg_ver_rec=pg_ver_rec+1
	   Where  esercizio = aRev.esercizio And
	          cd_cds = aRev.cd_cds and
	          pg_reversale = Substr(aR30.numero_ordinativo, 2);

 	   -- Aggiorno lo stato di incasso della reversale e aggiorno i saldi

 	   If CNRCTB048.getImRiscontratoManRev(aRev.cd_cds, aRev.esercizio, Substr(aR30.numero_ordinativo, 2), 'R') = aRev.im_reversale then
 	     CNRCTB037.riscontroReversale(aRev.esercizio,aRev.cd_cds,aRev.pg_reversale,'I',aUser);
 	   End If;

           aNumOk := aNumOk+1;


Elsif aR30.TIPO_ORDINATIVO = 'M' Then

-- ====================================================================================================
--                                                MANDATO
-- ====================================================================================================

           aMan := null;
	   aMan.esercizio := aEsercizio;
	   if(tesoreria_unica ='N') then
	   		aMan.cd_cds := aCds.cd_unita_organizzativa;
		 end if;
	   aMan.pg_mandato := Substr(aR30.numero_ordinativo, 2);

  	   Begin
  	    Select *
  	    Into   aMan
  	    From   mandato
  	    Where  esercizio =  aMan.esercizio And
  	           cd_cds like  decode(tesoreria_unica,'N',aCds.cd_unita_organizzativa,'%') And
  	           pg_mandato = Substr(aR30.numero_ordinativo, 2) And
  	           stato <> CNRCTB038.STATO_AUT_ANN
 	    For Update Nowait;

 	   Exception when NO_DATA_FOUND Then

 	     Select Count(*)
 	     Into   conta_err
   	     From   mandato
  	     Where  esercizio =  aMan.esercizio And
  	            cd_cds like decode(tesoreria_unica,'N',aCds.cd_unita_organizzativa,'%') And
  	            pg_mandato = Substr(aR30.numero_ordinativo, 2) And
  	            stato = CNRCTB038.STATO_AUT_ANN;

             If Conta_err > 0 Then
               IBMERR001.RAISE_ERR_GENERICO('Il riscontro di euro '||Ltrim(To_Char(aR30.importo_operazione, '999g999g999g999g999g990d00'))||' fa riferimento al mandato '||aCds.cd_unita_organizzativa||'/'|| aEsercizio||'/'||Substr(aR30.numero_ordinativo, 2)||' che risulta annullato.');
             Else
               IBMERR001.RAISE_ERR_GENERICO('Il riscontro di euro '||Ltrim(To_Char(aR30.importo_operazione, '999g999g999g999g999g990d00'))||' fa riferimento al mandato '||aCds.cd_unita_organizzativa||'/'|| aEsercizio||'/'||Substr(aR30.numero_ordinativo, 2)||' che non esiste.');
             End If;

 	   End;

	   -- Verifico che il mandato non abbia gi? riscontri manuali in tal caso blocco l'aggiornamento

           Declare
	    aLOCNUM number(10);
	   Begin
	    Select distinct 1 into aLOCNUM from sospeso a
	    Where  cd_cds = aMan.cd_cds And
	           esercizio = aMan.esercizio And
	           ti_entrata_spesa = 'S' And
	           ti_sospeso_riscontro = 'R'
		   and not exists (Select 1 from ext_cassiere00
		                   Where cd_cds_sr = a.cd_cds And
		                         esercizio_sr = a.esercizio And
		                         ti_entrata_spesa_sr = a.ti_entrata_spesa And
		                         ti_sospeso_riscontro_sr = a.ti_sospeso_riscontro And
		                         cd_sr = a.cd_sospeso) -- non da flusso
		   and exists (Select 1 from sospeso_det_usc
		               Where    cd_cds = a.cd_cds And
		                        esercizio = a.esercizio And
		                        pg_mandato = aMan.pg_mandato And
		                        ti_entrata_spesa = a.ti_entrata_spesa And
		                        ti_sospeso_riscontro = a.ti_sospeso_riscontro And
		                        cd_sospeso = a.cd_sospeso  and
	                          sospeso_det_usc.stato !='A' );

                   IBMERR001.RAISE_ERR_GENERICO('Impossibile caricare tramite flusso il riscontro di '||Ltrim(To_Char(aR30.importo_operazione, '999g999g999g999g999g990d00'))||
                         ' sul mandato '||CNRCTB038.getDesc(aMan)||', esso risulta gi? riscontrato manualmente (anche solo parzialmente).');

	   Exception when NO_DATA_FOUND then
	        Null;
	   End;

           -- controllo lo sfondamento del riscontrato (a questo punto automatico, avendo gi? controllato l'inesistenza di riscontri manuali)

   	   If CNRCTB048.getImRiscontratoManRev(aMan.cd_cds, aMan.esercizio, aMan.pg_mandato, 'M') = aMan.im_mandato Then
             IBMERR001.RAISE_ERR_GENERICO('Il mandato '||CNRCTB038.getDesc(aMan)||' risulta gi? completamente riscontrato (totale dei riscontri ed importo '||
                                          ' del mandato pari a '||Ltrim(To_Char(aMan.im_mandato, '999g999g999g999g999g990d00'))||'). E'' impossibile quindi '||
                                          ' riscontrarlo ulteriormente tramite flusso per '||Ltrim(To_Char(aR30.importo_operazione, '999g999g999g999g999g990d00'))||'.');
 	   Elsif CNRCTB048.getImRiscontratoManRev(aMan.cd_cds, aMan.esercizio, aMan.pg_mandato, 'M') + aR30.importo_operazione > aMan.im_mandato then
             IBMERR001.RAISE_ERR_GENERICO('Impossibile riscontrare il mandato '||CNRCTB038.getDesc(aMan)||
                                ' per '||Ltrim(To_Char(aR30.importo_operazione, '999g999g999g999g999g990d00'))||'. Esso risulta gi? riscontrato per '||
                                Ltrim(To_Char(CNRCTB048.getImRiscontratoManRev(aMan.cd_cds, aMan.esercizio, aMan.pg_mandato, 'M'), '999g999g999g999g999g990d00'))||
                                ' e pertanto il totale dei riscontri supererebbe l''importo del mandato stesso che ? di '||
                                Ltrim(To_Char(aMan.im_mandato, '999g999g999g999g999g990d00')));
 	   End If;


 	   -- Preparo l'inserimento del riscontro
                aRisc.CD_CDS:=aMan.cd_cds;
                aRisc.ESERCIZIO:=aMan.esercizio;
                aRisc.TI_ENTRATA_SPESA:='S';
                aRisc.TI_SOSPESO_RISCONTRO:='R';
                aRisc.CD_SOSPESO:=CNRCTB038.nextProgressivoRiscontro(aMan.cd_cds, aMan.esercizio, 'S');
                aRisc.CD_CDS_ORIGINE:=aMan.cd_cds;
                aRisc.CD_UO_ORIGINE:=null;
                aRisc.DT_REGISTRAZIONE:=TO_DATE(aR30.DATA_ESECUZIONE,'YYYYMMDD');
                aRisc.DS_ANAGRAFICO:=aR30.DESCRIZIONE_BENEFICIARIODEBITO;
                aRisc.CAUSALE:=aR30.CAUSALE;

               if aR01.NUMERO_CONT_TESO = '000000218154' then
                  if aCds.cd_tipo_unita = CNRCTB020.TIPO_ENTE then
 	             aRisc.TI_CC_BI:='B';
  	          else
                     IBMERR001.RAISE_ERR_GENERICO('Un mandato di un CDS ('||aCds.cd_unita_organizzativa||') non pu? essere da Banca d''Italia (sul conto 218154)');
 	          end if;
 	       else
                  aRisc.TI_CC_BI:='C';
  	       end if;

                aRisc.FL_STORNATO:='N';
                aRisc.IM_SOSPESO:=aR30.IMPORTO_OPERAZIONE;
                aRisc.IM_ASSOCIATO:=aR30.IMPORTO_OPERAZIONE;
                aRisc.STATO_SOSPESO:='N';
                aRisc.DACR:=aTSNow;
                aRisc.UTCR:=aUser;
                aRisc.UTUV:=aUser;
                aRisc.DUVA:=aTSNow;
                aRisc.PG_VER_REC:=1;
                aRisc.IM_ASS_MOD_1210:=0;
                aRisc.CD_SOSPESO_PADRE:=null;
                aRisc.CD_PROPRIO_SOSPESO:=null;

                CNRCTB038.INS_SOSPESO(aRisc);

	        Update ext_cassiere00
	        Set     STATO = CNRCTB755.STATO_RECORD_PROCESSATO,
                        cd_cds_sr = aRisc.cd_cds,
                        esercizio_sr = aRisc.esercizio,
                        ti_entrata_spesa_sr = aRisc.ti_entrata_spesa,
                        ti_sospeso_riscontro_sr = aRisc.ti_sospeso_riscontro,
			cd_sr = aRisc.cd_sospeso,
                        duva = aRisc.duva,
			utuv = aRisc.utuv,
			pg_ver_rec=pg_ver_rec+1
	        Where   esercizio = aR.esercizio And
	                nome_file = aR.nome_file And
	                pg_rec = aR.pg_rec;

 	   -- Aggiorno il dettaglio det USC

 	   aDetUsc.CD_CDS:=aMan.cd_cds;
           aDetUsc.ESERCIZIO:=aMan.esercizio;
           aDetUsc.PG_MANDATO:=aMan.pg_mandato;
           aDetUsc.TI_ENTRATA_SPESA:='S';
           aDetUsc.TI_SOSPESO_RISCONTRO:='R';
           aDetUsc.CD_SOSPESO:=aRisc.cd_sospeso;
           aDetUsc.IM_ASSOCIATO:=aRisc.IM_SOSPESO;
           aDetUsc.STATO:=CNRCTB038.STATO_SOSPESO_DET_DEFAULT;
           aDetUsc.DACR:=aTSNow;
           aDetUsc.UTCR:=aUser;
           aDetUsc.UTUV:=aUser;
           aDetUsc.DUVA:=aTSNow;
           aDetUsc.PG_VER_REC:=1;
           aDetUsc.CD_CDS_MANDATO:=aMan.cd_cds;
           CNRCTB038.INS_SOSPESO_DET_USC(aDetUsc);

           -- Aggiorno l'importo pagato del mandato

  	   Update mandato
  	   Set    im_pagato = CNRCTB048.getImRiscontratoManRev(aMan.cd_cds, aMan.esercizio, Substr(aR30.numero_ordinativo, 2), 'M'),
                  duva=aRisc.duva,
		  utuv=aRisc.utuv,
		  pg_ver_rec=pg_ver_rec+1
	   Where  esercizio = aMan.esercizio And
	          cd_cds = aMan.cd_cds And
	          pg_mandato = Substr(aR30.numero_ordinativo, 2);

 	   -- Aggiorno lo stato di incasso del mandato e aggiorno i saldi

 	   if CNRCTB048.getImRiscontratoManRev(aMan.cd_cds, aMan.esercizio, Substr(aR30.numero_ordinativo, 2), 'M') = aMan.im_mandato then
 	    CNRCTB037.riscontroMandato(aMan.esercizio,aMan.cd_cds,aMan.pg_mandato,'I',aUser);
           end if;

           aNumOk:=aNumOk+1;

      else -- Doc non riconosciuto
       IBMERR001.RAISE_ERR_GENERICO('Il Tipo Ordinativo '||aR30.TIPO_ORDINATIVO||' non ? compatibile, pu? assumere solo i valori R (Reversale) e M (Mandato)');
      end if;

     Else
       -- differenzio gli errori
       If aR30.STATO_ORDINATIVO != '03' Then
          IBMERR001.RAISE_ERR_GENERICO('Lo Stato Ordinativo assume valore diverso da "03" ('||aR30.STATO_ORDINATIVO||')');
       Elsif aR30.SEGNO_IMPORTO_OPERAZIONE != 1 Then
          IBMERR001.RAISE_ERR_GENERICO('Il Segno Importo Operazione ? diverso da "1" che indica il segno positivo dell''operazione '||aR30.SEGNO_IMPORTO_OPERAZIONE);
       End If;

     End If; -- end controllo stato ordinativo = '03'

 End If;  -- TERMINE GESTIONE RISCONTRI (record T30)


-- =========================
-- SOSPESI
-- =========================

If CNRCTB755.isT32(aR.data) Then -- 1
     aNumProcessati:=aNumProcessati+1;
     -- Check esistenza record di testata
     lTiRiga := '32';
  If aR01.TIPO_RECO is null Then -- 2
      IBMERR001.RAISE_ERR_GENERICO('Tipo record del record TR01 nullo');
  End if; -- 2

  If aR01.anno_eser <> aEs Then -- 3
      IBMERR001.RAISE_ERR_GENERICO('Anno di esercizio del record TR01 ('||aR01.anno_eser||') diverso dall''esercizio di bilancio '||aEs);
  End if; -- 3

  aR32 := CNRCTB755.parseT32(aR.data); -- SCARICA LA STRINGA "aR.data" NEL RECORD DI TIPO T32
	 -- Controlla che:
	 -- stato sospeso = '01'
	 -- IMPORTO = IMPORTO OPERAZIONE
	 -- SEGNO IMPORTO = SEGNO IMPORTO OPERAZIONE

-- GESTIONE STORNO -- 4

  If aR32.STATO_SOSP = '05' Then

  	  aCds     := getCds(aEs,aR01);
 	  aSospeso := null;

   Begin
       Select *
       Into   aSospeso
       From   sospeso
       Where  cd_cds = aCds.cd_unita_organizzativa And
              esercizio = aR.esercizio And
              ti_entrata_spesa = decode(aR32.TIPO_VOCE,'E','E','S') And
              ti_sospeso_riscontro = 'S' And
              cd_sospeso = aR32.numero_sosp And
              cd_sospeso_padre is null
       For Update Nowait;
   Exception when NO_DATA_FOUND then
       IBMERR001.RAISE_ERR_GENERICO('Sospeso da stornare non trovato: '||aR32.NUMERO_SOSP||' cds.: '||aCds.cd_unita_organizzativa||' es.: '||aR.esercizio);
   End;

   If aR32.IMPORT*aR32.SEGNO_IMPO <> aSospeso.im_sospeso Then -- 5
        IBMERR001.RAISE_ERR_GENERICO('Si sta tentando di stornare il sospeso '||getDesc(aSospeso)||' per '
                                     ||Ltrim(To_Char(aR32.IMPORT*aR32.SEGNO_IMPO, '999g999g999g999g999g990d00'))||
                                    ' ma tale importo ? differente rispetto all''importo che il sospeso gi? possiede che ? di '||
                                    Ltrim(To_Char(aSospeso.im_sospeso, '999g999g999g999g999g990d00')));
   End if; -- 5

   -- Operazioni di storno del sospeso
   -- Le operazioni consistono nel verificare che il sospeso non sia associato a mandati/reversali o 1210

     For aSosF in (select * from sospeso where
	  	   	 cd_cds = aSospeso.cd_cds
		 and esercizio = aSospeso.esercizio
		 and cd_sospeso_padre = aSospeso.cd_sospeso
		 and ti_entrata_spesa = aSospeso.ti_entrata_spesa
		 and ti_sospeso_riscontro = aSospeso.ti_sospeso_riscontro
	    for update nowait) Loop  -- INIZIO LOOP 1

        If aSosF.im_ass_mod_1210 <> 0 Then
           IBMERR001.RAISE_ERR_GENERICO('Impossibile stornare il sospeso '||getDesc(aSosF)||', risulta associato a 1210 per '||Ltrim(To_Char(aSosF.im_ass_mod_1210, '999g999g999g999g999g990d00')));
        End If;

        If aSosF.im_associato <> 0 Then
           IBMERR001.RAISE_ERR_GENERICO('Impossibile stornare il sospeso '||getDesc(aSosF)||', risulta gi? associato a documenti autorizzatori per '||Ltrim(To_Char(aSosF.im_associato, '999g999g999g999g999g990d00')));
        End If;

	If aSosF.ti_entrata_spesa = 'E' then
           for aSosDet in (Select * from sospeso_det_etr
                           Where esercizio = aSosF.esercizio And
                                 cd_cds = aSosF.cd_cds And
                                 cd_sospeso = aSosF.cd_sospeso And
                                 ti_entrata_spesa = aSosF.ti_entrata_spesa And
                                 ti_sospeso_riscontro  =aSosF.ti_sospeso_riscontro And
                                 stato <> 'A'
                           For update nowait) Loop
            IBMERR001.RAISE_ERR_GENERICO('Il sospeso '||getDesc(aSosF)||' non risulta associato a reversali, ma esistono associazioni con reversale non annullate '||
                                        '(per esempio alla reversale '||aSosDet.CD_CDS||'/'||aSosDet.esercizio||'/'||aSosDet.PG_REVERSALE||')');
	   End loop;
        else
           for aSosDet in (select * from sospeso_det_usc where
                    		     esercizio = aSosF.esercizio
                    		 and cd_cds = aSosF.cd_cds
                    		 and cd_sospeso = aSosF.cd_sospeso
                    		 and ti_entrata_spesa = aSosF.ti_entrata_spesa
                    		 and ti_sospeso_riscontro  =aSosF.ti_sospeso_riscontro
							 and stato <> 'A'
						 for update nowait) Loop
            IBMERR001.RAISE_ERR_GENERICO('Il sospeso '||getDesc(aSosF)||' non risulta associato a mandati, ma esistono associazioni con mandati non annullate '||
                                        '(per esempio al mandato '||aSosDet.CD_CDS||'/'||aSosDet.esercizio||'/'||aSosDet.PG_mandato||')');
           end loop;
	end if;

        Update sospeso
        Set    fl_stornato = 'Y',
               dt_storno   = To_Date(aR32.data_oper, 'yyyymmdd'),
               duva        = aTSNow,
	       utuv        = aUser,
	       pg_ver_rec  = pg_ver_rec + 1
	Where  esercizio            = aSosF.esercizio And
	       cd_cds               = aSosF.cd_cds And
	       cd_sospeso           = aSosF.cd_sospeso And
	       ti_entrata_spesa     = aSosF.ti_entrata_spesa And
	       ti_sospeso_riscontro = aSosF.ti_sospeso_riscontro;

End loop; -- FINE LOOP 1


       Update sospeso
       Set   fl_stornato  = 'Y',
             dt_storno    = To_Date(aR32.data_oper, 'yyyymmdd'),
	     duva         = aTSNow,
	     utuv         = aUser,
	     pg_ver_rec   = pg_ver_rec+1
       Where esercizio            = aSospeso.esercizio And
             cd_cds               = aSospeso.cd_cds And
             cd_sospeso           = aSospeso.cd_sospeso And
             ti_entrata_spesa     = aSospeso.ti_entrata_spesa And
             ti_sospeso_riscontro = aSospeso.ti_sospeso_riscontro;

       Update EXT_CASSIERE00
       Set    STATO=CNRCTB755.STATO_RECORD_PROCESSATO,
              duva=aTSNow,
              utuv=aUser,
	      pg_ver_rec=pg_ver_rec+1
       Where  esercizio =aR.esercizio And
              nome_file = aR.nome_file And
              pg_rec = aR.pg_rec;

       aNumOk := aNumOk+1;

Elsif -- GESTIONE INSERIMENTO IF 4

        aR32.STATO_SOSP = '01' And aR32.IMPORT = aR32.IMPORT_RESI_OPER And
                aR32.SEGNO_IMPO = aR32.SEGNO_IMPO_RESI_OPER And aR32.SEGNO_IMPO = 1 Then

  	  aCds := getCds(aEs,aR01);

 	  aSospeso:=null;
 	  aSF:=null;
  	  aSospeso.CD_CDS:=aCds.cd_unita_organizzativa;
          aSospeso.ESERCIZIO:=aR.esercizio;

  	  if aR32.TIPO_VOCE='E' then          -- VERIFICARE I VALORI
               aSospeso.TI_ENTRATA_SPESA:='E';
          else
               aSospeso.TI_ENTRATA_SPESA:='S';
  	  end if;

  	  aSospeso.TI_SOSPESO_RISCONTRO:='S';
          aSospeso.CD_SOSPESO:=aR32.NUMERO_SOSP;
          aSospeso.CD_CDS_ORIGINE:=null;
          aSospeso.CD_UO_ORIGINE:=null;
          aSospeso.DT_REGISTRAZIONE:=TO_DATE(aR32.DATA_OPER,'YYYYMMDD');
          aSospeso.DS_ANAGRAFICO:=aR32.DESCRI_BENE;
          aSospeso.CAUSALE:=aR32.CAUSAL_1A_PART||aR32.CAUSAL_2A_PART;
          -- identificazione banca d'Italia solo per sospesi ENTE
	  --If aR01.NUMERO_CONT_TESO = '000000218154' then
	  if (aR32.TIPO_SOSP is not null and aR32.TIPO_SOSP like 'TURN%') then
        If aCds.cd_tipo_unita = CNRCTB020.TIPO_ENTE then
	        aSospeso.TI_CC_BI:='B';
 	      Else
                IBMERR001.RAISE_ERR_GENERICO('Il sospeso '||aR32.NUMERO_SOSP||' del CDS '||aCds.cd_unita_organizzativa||
                                             ' non pu? essere da Banca d''Italia (sul conto 218154)');
	      End if;
	  Else
              aSospeso.TI_CC_BI:='C';
 	  End if;

          aSospeso.FL_STORNATO:='N';
          aSospeso.IM_SOSPESO:=aR32.IMPORT*aR32.SEGNO_IMPO;
          aSospeso.IM_ASSOCIATO:=0;
          aSospeso.STATO_SOSPESO:='I'; --INIZIALE
          aSospeso.DACR:=aTSNow;
          aSospeso.UTCR:=aUser;
          aSospeso.UTUV:=aUser;
          aSospeso.DUVA:=aTSNow;
          aSospeso.PG_VER_REC:=1;
          aSospeso.IM_ASS_MOD_1210:=0;
          aSospeso.CD_SOSPESO_PADRE:=null;
          aSospeso.CD_PROPRIO_SOSPESO:=null;

          Begin
	       CNRCTB038.ins_SOSPESO(aSospeso);
          Exception when DUP_VAL_ON_INDEX Then
              Begin
                Select im_sospeso
                Into   importo_sospeso_esistente
                From   sospeso
                Where  CD_CDS               = aSospeso.CD_CDS And
                       ESERCIZIO            = aSospeso.ESERCIZIO And
                       TI_ENTRATA_SPESA     = aSospeso.TI_ENTRATA_SPESA And
                       TI_SOSPESO_RISCONTRO = aSospeso.TI_SOSPESO_RISCONTRO And
                       CD_SOSPESO           = aSospeso.CD_SOSPESO;
              Exception
                When Others Then
                  importo_sospeso_esistente := Null;
              End;

            If aR32.IMPORT*aR32.SEGNO_IMPO != importo_sospeso_esistente Then -- sospeso gi? esistente di importo diverso
               IBMERR001.RAISE_ERR_GENERICO('Impossibile inserire il sospeso '||aSospeso.CD_CDS||'/'||aSospeso.ESERCIZIO||'/'||aSospeso.TI_ENTRATA_SPESA||'/'||
                                             aSospeso.TI_SOSPESO_RISCONTRO||'/'||aSospeso.CD_SOSPESO||', gi? esiste. L''importo del sospeso che si sta tentando di '||
                                             'inserire ? '||Ltrim(To_Char(aR32.IMPORT*aR32.SEGNO_IMPO, '999g999g999g999g999g990d00'))||' mentre l''importo del sospeso '||
                                             'gi? esistente con lo stesso codice ? '||Ltrim(To_Char(importo_sospeso_esistente, '999g999g999g999g999g990d00'))||'.');
	    Else -- sospeso gi? esistente di uguale importo
	       IBMERR001.RAISE_ERR_GENERICO('Impossibile inserire il sospeso '||aSospeso.CD_CDS||'/'||aSospeso.ESERCIZIO||'/'||aSospeso.TI_ENTRATA_SPESA||'/'||
                                             aSospeso.TI_SOSPESO_RISCONTRO||'/'||aSospeso.CD_SOSPESO||'. Con quel codice ne esiste gi? uno ed ha lo stesso importo di quello che si sta'||
                                             ' tentando di caricare, vale a dire '||Ltrim(To_Char(aR32.IMPORT*aR32.SEGNO_IMPO, '999g999g999g999g999g990d00'))||'.');
	    End If;
	  End;

          aSF.CD_CDS:=aSospeso.CD_CDS;
          aSF.ESERCIZIO:=aSospeso.esercizio;
          aSF.TI_ENTRATA_SPESA:=aSospeso.ti_entrata_spesa;
          aSF.TI_SOSPESO_RISCONTRO:=aSospeso.ti_sospeso_riscontro;
          aSF.CD_SOSPESO:=aSospeso.cd_sospeso || '.001';
          aSF.DT_REGISTRAZIONE:=aSospeso.DT_REGISTRAZIONE;
          aSF.DS_ANAGRAFICO:=aSospeso.DS_ANAGRAFICO;
          aSF.CAUSALE:=aSospeso.CAUSALE;
          aSF.TI_CC_BI:=aSospeso.TI_CC_BI;
          aSF.FL_STORNATO:=aSospeso.FL_STORNATO;
          aSF.IM_SOSPESO:=aSospeso.IM_SOSPESO;
          aSF.IM_ASSOCIATO:=0;
          aSF.DACR:=aTSNow;
          aSF.UTCR:=aUser;
          aSF.UTUV:=aUser;
          aSF.DUVA:=aTSNow;
          aSF.PG_VER_REC:=1;
          aSF.IM_ASS_MOD_1210:=0;
          aSF.CD_SOSPESO_PADRE:=aSospeso.CD_SOSPESO;
          aSF.CD_PROPRIO_SOSPESO:='001';

 	  if aCds.cd_tipo_unita <> CNRCTB020.TIPO_ENTE then
            aSF.CD_CDS_ORIGINE:=aSospeso.CD_CDS;
            aSF.CD_UO_ORIGINE:=null;
            aSF.STATO_SOSPESO:='A'; --ASSEGNATO A CDS
          else
            aSF.CD_CDS_ORIGINE:=null;
            aSF.CD_UO_ORIGINE:=null;
            aSF.STATO_SOSPESO:='I'; --INIZIALE
          end if;

          CNRCTB038.ins_SOSPESO(aSF);

	  Update EXT_CASSIERE00
	  Set    STATO=CNRCTB755.STATO_RECORD_PROCESSATO,
                 duva=aTSNow,
                 utuv=aUser,
	         pg_ver_rec=pg_ver_rec+1
	  Where  esercizio =aR.esercizio And
	         nome_file = aR.nome_file And
	         pg_rec = aR.pg_rec;

	  aNumOk:=aNumOk+1;

      -- Condizioni di processabilit? non verificate
Else
  If aR32.STATO_SOSP != '01' Then
    IBMERR001.RAISE_ERR_GENERICO('Sospeso non caricabile per stato diverso da 01 ('||CNRCTB755.getDesc(aR)||'). Lo stato sospeso ? '||aR32.STATO_SOSP);
  End If;

  If aR32.IMPORT != aR32.IMPORT_RESI_OPER Then
    IBMERR001.RAISE_ERR_GENERICO('Sospeso non caricabile per importo del sospeso diverso dall''importo residuo operazione: '||CNRCTB755.getDesc(aR));
  End If;

  If aR32.SEGNO_IMPO != aR32.SEGNO_IMPO_RESI_OPER Then
    IBMERR001.RAISE_ERR_GENERICO('Sospeso non caricabile per segno importo del sospeso diverso dal segno importo residuo operazione: '||CNRCTB755.getDesc(aR));
  End If;

  If aR32.SEGNO_IMPO != 1 Then
    IBMERR001.RAISE_ERR_GENERICO('Sospeso non caricabile per segno importo negativo: '||CNRCTB755.getDesc(aR));
  End If;

End if;

End if; -- TERMINE GESTIONE SOPESI (record T32) if n. 1

Commit;

Exception
 When Others Then
                Rollback;

-- 16.01.2008 nuova gestione degli scarti, inserisce nella tabella degli scarti

If CNRCTB755.isT30(aR.data) Then -- RISCONTRI

  aRecScarto := Null;

  aRecScarto.ESERCIZIO               := aEs;
  aRecScarto.NOME_FILE               := aNomeFile;
  aRecScarto.PG_ESECUZIONE           := pg_exec;
  aRecScarto.PG_REC                  := aR.pg_rec;
  aRecScarto.DT_GIORNALIERA          := To_Date(Substr(aNomeFile, 1, 8), 'YYYYMMDD');
  aRecScarto.DT_ELABORAZIONE         := Trunc(aTSNow);
  aRecScarto.DT_ESECUZIONE           := To_Date(To_Char(aR30.DATA_ESECUZIONE), 'YYYYMMDD');
  aRecScarto.CD_CDS                  := aCds.cd_unita_organizzativa;

-- SOSPESO INUTILE

--  aRecScarto.CD_CDS_SR               := aR.CD_CDS_SR;
--  aRecScarto.ESERCIZIO_SR            := aR.ESERCIZIO_SR;
--  aRecScarto.TI_ENTRATA_SPESA_SR     := aR.TI_ENTRATA_SPESA_SR;
--  aRecScarto.TI_SOSPESO_RISCONTRO_SR := aR.TI_SOSPESO_RISCONTRO_SR;
--  aRecScarto.CD_SR                   := aR.CD_SR;

  aRecScarto.TIPO_MOV                := aR30.TIPO_ORDINATIVO;
  aRecScarto.CD_CDS_MANREV           := aCds.cd_unita_organizzativa;
  aRecScarto.ESERCIZIO_MANREV        := aR01.anno_eser;
  aRecScarto.PG_MANREV               := Substr(aR30.NUMERO_ORDINATIVO, 2);
  aRecScarto.ANOMALIA                := IBMUTL001.getErrorMessage;
  aRecScarto.DACR                    := aTSNow;
  aRecScarto.UTCR                    := aUser;
  aRecScarto.DUVA                    := aTSNow;
  aRecScarto.UTUV                    := aUser;
  aRecScarto.PG_VER_REC              := 1;

Elsif CNRCTB755.isT32(aR.data) Then -- SOSPESI

  aRecScarto := Null;

  aRecScarto.ESERCIZIO               := aEs;
  aRecScarto.NOME_FILE               := aNomeFile;
  aRecScarto.PG_ESECUZIONE           := pg_exec;
  aRecScarto.PG_REC                  := aR.pg_rec;
  aRecScarto.DT_GIORNALIERA          := To_Date(Substr(aNomeFile, 1, 8), 'YYYYMMDD');
  aRecScarto.DT_ELABORAZIONE         := Trunc(aTSNow);
  aRecScarto.DT_ESECUZIONE           := To_Date(aR32.DATA_OPER,'YYYYMMDD'); -- DATA REGISTRAZIONE SOSPESO
  aRecScarto.CD_CDS                  := aCds.cd_unita_organizzativa;

  aRecScarto.TIPO_MOV                := 'S'; -- SOSPESO

  aRecScarto.CD_CDS_SR               := aSospeso.CD_CDS;
  aRecScarto.ESERCIZIO_SR            := aSospeso.esercizio;
  aRecScarto.TI_ENTRATA_SPESA_SR     := aSospeso.ti_entrata_spesa;
  aRecScarto.TI_SOSPESO_RISCONTRO_SR := aSospeso.ti_sospeso_riscontro;
  aRecScarto.CD_SR                   := aSospeso.cd_sospeso || '.001';

  -- I DATI MANDATI E REVERSALI NON SI METTONO PER I SOSPESI
  aRecScarto.ANOMALIA                := IBMUTL001.getErrorMessage;
  aRecScarto.DACR                    := aTSNow;
  aRecScarto.UTCR                    := aUser;
  aRecScarto.DUVA                    := aTSNow;
  aRecScarto.UTUV                    := aUser;
  aRecScarto.PG_VER_REC              := 1;

End If;

IBMUTL200.ins_EXT_CASSIERE00_SCARTI (aRecScarto);
Commit;


-- ************
-- Iac Inizio
-- ************
--    IBMUTL200.logerr(pg_exec,
--	                 'Caricamento interfaccia ritorno cassiere. ',
--                     'Errore in processo riga ' || CNRCTB755.getDesc(aR),
--					 DBMS_UTILITY.FORMAT_ERROR_STACK);

IBMUTL200.logerr(pg_exec, aR.data, aCds.cd_unita_organizzativa||'-'||aNomeFile||'-'|| CNRCTB755.getDesc(aR), DBMS_UTILITY.FORMAT_ERROR_STACK);
-- ************
-- Iac Fine
-- ************
End;

End Loop;

IBMUTL200.loginf(pg_exec, 'Processati: '||aNumProcessati||' Caricati: '||aNumOk, null,
                 'Termine operazione caricamento interfaccia ritorno cassiere. '||To_Char(sysdate,'DD/MM/YYYY HH:MI:SS'));

  -- Messaggio a utente
IBMUTL205.LOGWAR('Caricamento interfaccia ritorno cassiere: '||aNomeFile||' es.'||aEs||' ','Caricamento interfaccia ritorno cassiere: '||aNomeFile||' es.'||aEs||' '||
          TO_CHAR(sysdate,'DD/MM/YYYY HH:MI:SS') ||' (log esecuzione: ' || pg_exec || ')',
          'Operazione completata. Processati: '||aNumProcessati||' Caricati: '||aNumOk, aUser);
End;


Procedure processaInterfaccia(aEs number, aNomeFile varchar2,aUser varchar2) is
  aProcedure varchar2(1000);
  data DATE;
  ora VARCHAR2(5);
 begin
  aProcedure:='CNRCTB750.job_interfaccia_cassiere(job, pg_exec, next_date, '
                  || aEs || ',' ||
                  '''' || aNomeFile || ''',' ||
                  '''' || aUser  || ''');';

  If (To_Number(To_Char(Sysdate,'hh24')) < 13) Then
    data := To_Date(To_Char(Sysdate,'ddmmyyyy')||' '||'13:00','ddmmyyyy hh24:mi');
    ora := '13:00';
  Elsif (To_Number(To_Char(Sysdate,'hh24')) > 13 And To_Number(To_Char(Sysdate,'hh24')) < 18) Then
    data := To_Date(To_Char(Sysdate,'ddmmyyyy')||' '||'18:00','ddmmyyyy hh24:mi');
    ora := '18:00';
  Else
    data := Sysdate;
    ora := To_Char(Sysdate,'hh24:mi');
  End If;

  IBMUTL210.creabatchdinamico('Caricamento interfaccia ritorno cassiere',
                               aProcedure,
                               aUser,
                               data);

  IBMUTL001.deferred_commit;

  IBMERR001.RAISE_ERR_GENERICO
  ('L''operazione verr? eseguita alle ore '||ora||'. Al completamento l''utente ricever? un messaggio di notifica ' ||
   'dello stato dell''operazione');
 end;

 procedure checkDocContForDistCas(
   aCdCds varchar2,
   aEs number,
   aCdUO varchar2,
   aPgDistinta number
 ) is
 aTi_doc char(1);
 aDist distinta_cassiere%rowtype;
 begin
 	  -- Locko la testata della Distinta Cassiere
	  select * into aDist from distinta_cassiere
	     where CD_CDS    = aCdCds
		 and   ESERCIZIO = aEs
		 and   CD_UNITA_ORGANIZZATIVA = aCdUo
		 and   PG_DISTINTA			  = aPgDistinta
		 for update nowait;

 	  -- Ciclo su tutti i documenti (Mand/Rev) inseriti nella distinta
 	  for dett_dist in (select *
					 	 from DISTINTA_CASSIERE_DET
						 where CD_CDS 				  = aCdCds
						 and   ESERCIZIO 			  = aEs
						 and   CD_UNITA_ORGANIZZATIVA = aCdUO
						 and   PG_DISTINTA 			  = aPgDistinta
						-- and   PG_MANDATO IS NOT NULL
						)
	  loop
	  	   -- Ciclo sulla tabella ASS_COMP_DOC_CONT_NMP per controllare se ci sono
		   -- dei comp. associati ad uno dei docum. della distinta

		   if (dett_dist.PG_MANDATO is not null) then
		   	  aTi_doc := 'M'; -- Il documento in canna ? un Mandato
		   else
		   	  aTi_doc := 'R'; -- Il documento in canna ? una Reversale
		   end if;

		   for ass in (select *
		   	   	   	  	from ASS_COMP_DOC_CONT_NMP
						where cd_cds_doc    = dett_dist.cd_cds_origine
						and	  esercizio_doc = dett_dist.esercizio
						and	  pg_doc		= decode (aTi_doc, 'M', dett_dist.pg_mandato, dett_dist.pg_reversale)
						and   cd_tipo_doc	= aTi_doc
						for update nowait
		   	   	   	  )
			loop
				-- Controllo, per ogni comp, che tutti i doc cont ad esso associati siano stati inseriti nella distinta
				declare
				 aNum number;
				begin

				 select 1 into aNum from dual where exists (
					SELECT 1 FROM ASS_COMP_DOC_CONT_NMP
					WHERE CD_CDS_COMPENSO    = ass.CD_CDS_COMPENSO
					AND   ESERCIZIO_COMPENSO = ass.ESERCIZIO_COMPENSO
					AND	  CD_UO_COMPENSO 	 = ass.CD_UO_COMPENSO
					AND   PG_COMPENSO		 = ass.PG_COMPENSO
-- 					AND   CD_CDS_DOC		 = ass.CD_CDS_DOC
-- 					AND	  ESERCIZIO_DOC		 = ass.ESERCIZIO_DOC
-- 					AND	  PG_DOC			 = ass.PG_DOC
-- 					AND	  CD_TIPO_DOC		 = ass.CD_TIPO_DOC
					AND(
						 (CD_TIPO_DOC = 'M'
						  AND PG_DOC NOT IN (
							  SELECT PG_MANDATO
							  FROM 	 DISTINTA_CASSIERE_DET
							  WHERE CD_CDS 				  = dett_dist.CD_CDS
							  and  	ESERCIZIO 			  = dett_dist.ESERCIZIO
							  and   CD_UNITA_ORGANIZZATIVA= dett_dist.CD_UNITA_ORGANIZZATIVA
							  and   PG_DISTINTA 		  = dett_dist.PG_DISTINTA
							  and 	PG_MANDATO IS NOT NULL
							  )
						  )
					OR
						  (CD_TIPO_DOC = 'R'
						   AND PG_DOC NOT IN (
							  SELECT PG_REVERSALE
							  FROM 	 DISTINTA_CASSIERE_DET
							  WHERE CD_CDS 				  = dett_dist.CD_CDS
							  and  	ESERCIZIO 			  = dett_dist.ESERCIZIO
							  and   CD_UNITA_ORGANIZZATIVA= dett_dist.CD_UNITA_ORGANIZZATIVA
							  and   PG_DISTINTA 		  = dett_dist.PG_DISTINTA
							  and 	PG_REVERSALE IS NOT NULL
							  )
						   )
						 )
					);

					if (aTi_doc = 'M') then
					   IBMERR001.RAISE_ERR_GENERICO('Attenzione: il Mandato ' || dett_dist.PG_MANDATO || ' ? associato ad un Compenso a cui sono associati Mandati/Reversali non inseriti in distinta');
					else
					   IBMERR001.RAISE_ERR_GENERICO('Attenzione: la Reversale ' || dett_dist.PG_REVERSALE || ' ? associata ad un Compenso a cui sono associati Mandati/Reversali non inseriti in distinta');
					end if;
				exception when NO_DATA_FOUND then
				 null;
				end;
			end loop; -- fine ciclo sulla tabella ASS_COMP_DOC_CONT_NMP
	  end loop; -- fine ciclo sulla distinta per i mandati

 end;

 procedure checkDocContForDistCasAnn(
   aCdCds varchar2,
   aEs number,
   aCdUO varchar2,
   aPgDistinta number
 ) is
 aTi_doc char(3);
 aDist distinta_cassiere%rowtype;
 begin
 	  -- Locko la testata della Distinta Cassiere
	  select * into aDist from distinta_cassiere
	     where CD_CDS    = aCdCds
		 and   ESERCIZIO = aEs
		 and   CD_UNITA_ORGANIZZATIVA = aCdUo
		 and   PG_DISTINTA			  = aPgDistinta
		 for update nowait;

 	  -- Ciclo su tutti i documenti (Mand/Rev) inseriti nella distinta
 	  for dett_dist in (select *
					 	 from DISTINTA_CASSIERE_DET
						 where CD_CDS 				  = aCdCds
						 and   ESERCIZIO 			  = aEs
						 and   CD_UNITA_ORGANIZZATIVA = aCdUO
						 and   PG_DISTINTA 			  = aPgDistinta
						)
	  loop
	  	   -- Ciclo sulla tabella Mandati per controllare se ci sono
		   -- dei DOC. associati ad uno dei docum. della distinta

		   if (dett_dist.PG_MANDATO is not null) then
		   	  aTi_doc := 'MAN'; -- Il documento in canna ? un Mandato
		   else
		   	  aTi_doc := 'REV'; -- Il documento in canna ? una Reversale
		   end if;

		   for ass in (select *
		   	   	   	  	from V_MANDATO_REVERSALE_DIST_ANN
						where cd_cds  = dett_dist.cd_cds_origine
						and	  esercizio = dett_dist.esercizio
						and	  PG_DOCUMENTO_CONT		= decode (aTi_doc, 'MAN', dett_dist.pg_mandato, dett_dist.pg_reversale)
						and   CD_TIPO_DOCUMENTO_CONT	= aTi_doc
		   	   	   	  )
			loop
				-- Controllo, per ogni ANNULLO, che tutti i doc cont ad esso associati siano stati inseriti nella distinta
				declare
				 aNum number:=0;
				begin
				 if(aTi_doc = 'MAN') Then
				 begin
					SELECT  pg_mandato_riemissione into aNum
					FROM MANDATO
					WHERE CD_CDS_ORIGINE    = ass.CD_CDS_ORIGINE
					AND   ESERCIZIO = ass.ESERCIZIO
					AND   PG_MANDATO		 = ass.PG_DOCUMENTO_CONT
					AND PG_MANDATO_riemissione NOT IN (
							  SELECT PG_MANDATO
							  FROM 	 DISTINTA_CASSIERE_DET
							  WHERE CD_CDS 				  = dett_dist.CD_CDS
							  and  	ESERCIZIO 			  = dett_dist.ESERCIZIO
							  and   CD_UNITA_ORGANIZZATIVA= dett_dist.CD_UNITA_ORGANIZZATIVA
							  and   PG_DISTINTA 		  = dett_dist.PG_DISTINTA
							  and 	PG_MANDATO IS NOT NULL);
					exception when no_data_found then
					  aNum:=0;
					end;
				if aNum=0  then
				 begin
					SELECT  pg_mandato into aNum
					FROM MANDATO
					WHERE CD_CDS_ORIGINE    = ass.CD_CDS_ORIGINE
					AND   ESERCIZIO = ass.ESERCIZIO
					AND   PG_MANDATO_riemissione		 = ass.PG_DOCUMENTO_CONT
					AND PG_MANDATO NOT IN (
							  SELECT PG_MANDATO
							  FROM 	 DISTINTA_CASSIERE_DET
							  WHERE CD_CDS 				  = dett_dist.CD_CDS
							  and  	ESERCIZIO 			  = dett_dist.ESERCIZIO
							  and   CD_UNITA_ORGANIZZATIVA= dett_dist.CD_UNITA_ORGANIZZATIVA
							  and   PG_DISTINTA 		  = dett_dist.PG_DISTINTA
							  and 	PG_MANDATO IS NOT NULL);
					exception when no_data_found then
					  aNum:=0;
					end;
				end if;
				else
				begin
					SELECT pg_reversale_riemissione into aNum FROM REVERSALE
					WHERE CD_CDS_ORIGINE    = ass.CD_CDS_ORIGINE
					AND   ESERCIZIO = ass.ESERCIZIO
					AND   PG_REVERSALE		 = ass.PG_DOCUMENTO_CONT
					AND PG_REVERSALE_riemissione NOT IN (
							  SELECT PG_REVERSALE
							  FROM 	 DISTINTA_CASSIERE_DET
							  WHERE CD_CDS 				  = dett_dist.CD_CDS
							  and  	ESERCIZIO 			  = dett_dist.ESERCIZIO
							  and   CD_UNITA_ORGANIZZATIVA= dett_dist.CD_UNITA_ORGANIZZATIVA
							  and   PG_DISTINTA 		  = dett_dist.PG_DISTINTA
							  and 	PG_REVERSALE IS NOT NULL);
				exception when no_data_found then
					  aNum:=0;
				end;
				if aNum=0  then
				begin
					SELECT pg_reversale into aNum FROM REVERSALE
					WHERE CD_CDS_ORIGINE    = ass.CD_CDS_ORIGINE
					AND   ESERCIZIO = ass.ESERCIZIO
					AND   PG_REVERSALE_riemissione		 = ass.PG_DOCUMENTO_CONT
					AND PG_REVERSALE NOT IN (
							  SELECT PG_REVERSALE
							  FROM 	 DISTINTA_CASSIERE_DET
							  WHERE CD_CDS 				  = dett_dist.CD_CDS
							  and  	ESERCIZIO 			  = dett_dist.ESERCIZIO
							  and   CD_UNITA_ORGANIZZATIVA= dett_dist.CD_UNITA_ORGANIZZATIVA
							  and   PG_DISTINTA 		  = dett_dist.PG_DISTINTA
							  and 	PG_REVERSALE IS NOT NULL);
					exception when no_data_found then
					  aNum:=0;
					end;
				end if;
			end if;

				IF (aNum!=0) then
					if (aTi_doc = 'MAN') then
					   IBMERR001.RAISE_ERR_GENERICO('Attenzione: il Mandato ' || dett_dist.PG_MANDATO || ' ? associato ad un ANNULLO, Mandato ' || aNum || ' non inserito in distinta');
					else
					   IBMERR001.RAISE_ERR_GENERICO('Attenzione: la Reversale ' || dett_dist.PG_REVERSALE || ' ? associata ad un ANNULLO, Reversale ' || aNum || ' non inserita in distinta');
					end if;
				end if;
				exception when NO_DATA_FOUND then
				 null;
				end;
			end loop; -- fine ciclo sulla VIEW ANNULLI
	  end loop; -- fine ciclo sulla distinta per i mandati

 end;
END;
/


