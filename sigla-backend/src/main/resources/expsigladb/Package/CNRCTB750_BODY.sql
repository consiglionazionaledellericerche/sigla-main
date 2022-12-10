--------------------------------------------------------
--  DDL for Package Body CNRCTB750
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "CNRCTB750" AS
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
   IBMERR001.RAISE_ERR_GENERICO('Parte del file è già stata processata in interfaccia ritorno cassiere');
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
   aggiorna_sospeso boolean;
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
 dbms_output.put_line('primo loop');
  For aR in (Select * from EXT_CASSIERE00
             Where    esercizio = aEs And
                      nome_file = aNomeFile And
                      stato     = CNRCTB755.STATO_RECORD_INIZIALE
             Order by pg_rec asc) Loop

   Begin -- CHIUDE ALLA FINE, APPENA PRIMA DELL'END LOOP SULLE RIGHE DI EXT_CASSIERE00 E POI INSERISCE I LOG
	dbms_output.put_line('entra nel primo loop');
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
	   -- per capire se la mod pag è Banca di Italia o meno

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

	   -- se num_pag_bi > 0 allora esistono reversali riga che hanno modalità pagamento con Banca Italia

	   if lNumPagBI > 0 then
	   	  -- Se num_pag_non_bi = 0 allora tutte le reversali riga hanno mod pagamento con Banca Italia
	   	  if lNumPagNonBI = 0 then
		  	 lModPagBI := true;
		  else
	   	  	 -- Se num_pag_non_bi > 0 allora c'è un errore nella reversale che non può essere per parte da banca d'Italia e per parte no
		  	 IBMERR001.RAISE_ERR_GENERICO('La reversale '||CNRCTB038.getDesc(aRev)||' possiede modalità d''incasso diverse (sia Banca d''Italia che non Banca d''Italia');
		  end if;
	   else
		  	 lModPagBI := false;
	   end if;

	   -- *************************************************************
	   -- Iac Fine
	   -- *************************************************************

	   -- Verifico che la reversale non abbia già riscontri manuali in tal caso blocco l'aggiornamento

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
                         ' sulla reversale '||CNRCTB038.getDesc(aRev)||', essa risulta già riscontrata manualmente (anche solo parzialmente).');

	    Exception when NO_DATA_FOUND then
	       Null;
	    End;


           -- controllo lo sfondamento del riscontrato (a questo punto automatico, avendo già controllato l'inesistenza di riscontri manuali)

   	   If CNRCTB048.getImRiscontratoManRev(aRev.cd_cds, aRev.esercizio, aRev.pg_reversale, 'R') = aRev.im_reversale Then
             IBMERR001.RAISE_ERR_GENERICO('La reversale '||CNRCTB038.getDesc(aRev)||' risulta già completamente riscontrata (totale dei riscontri ed importo '||
                                          ' della reversale pari a '||Ltrim(To_Char(aRev.im_reversale, '999g999g999g999g999g990d00'))||'). E'' impossibile quindi '||
                                          ' riscontrarla ulteriormente tramite flusso per '||Ltrim(To_Char(aR30.importo_operazione, '999g999g999g999g999g990d00'))||'.');
 	   Elsif CNRCTB048.getImRiscontratoManRev(aRev.cd_cds, aRev.esercizio, aRev.pg_reversale, 'R') + aR30.importo_operazione > aRev.im_reversale then
             IBMERR001.RAISE_ERR_GENERICO('Impossibile riscontrare la reversale '||CNRCTB038.getDesc(aRev)||
                                ' per '||Ltrim(To_Char(aR30.importo_operazione, '999g999g999g999g999g990d00'))||'. Essa risulta già riscontrata per '||
                                Ltrim(To_Char(CNRCTB048.getImRiscontratoManRev(aRev.cd_cds, aRev.esercizio, aRev.pg_reversale, 'R'), '999g999g999g999g999g990d00'))||
                                ' e pertanto il totale dei riscontri supererebbe l''importo della reversale stessa che è di '||
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
	      	  -- Se la reversale non è da Banca d'Italia mentre il ricosntro della Banca è da Banca d'Italia viene sollevata un'eccezione
		  IBMERR001.RAISE_ERR_GENERICO('La reversale '||CNRCTB038.getDesc(aRev)||' non è da Banca d''Italia mentre il riscontro è sul conto 218154');
	     End if;
		-- ************************************************
		-- Iacca Fine Modificata IF inserendo and lModPagBI
		-- ************************************************
  	 Else
          IBMERR001.RAISE_ERR_GENERICO('Il riscontro per un CDS ('||aCds.cd_unita_organizzativa||') non può essere da Banca d''Italia sul conto 218154');
 	 End If;
  Else
        If lModPagBI then
	  	-- Se il riscontro della banca non è da Banca d'Italia ma la reversale SI viene sollevata un'eccezione
	  	 				aRisc.TI_CC_BI := 'B';
     	        --IBMERR001.RAISE_ERR_GENERICO('La reversale '||CNRCTB038.getDesc(aRev)||' è da Banca d''Italia mentre il Riscontro è su un conto diverso ('||aR01.NUMERO_CONT_TESO||')');
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

	   -- Verifico che il mandato non abbia già riscontri manuali in tal caso blocco l'aggiornamento

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
                         ' sul mandato '||CNRCTB038.getDesc(aMan)||', esso risulta già riscontrato manualmente (anche solo parzialmente).');

	   Exception when NO_DATA_FOUND then
	        Null;
	   End;

           -- controllo lo sfondamento del riscontrato (a questo punto automatico, avendo già controllato l'inesistenza di riscontri manuali)

   	   If CNRCTB048.getImRiscontratoManRev(aMan.cd_cds, aMan.esercizio, aMan.pg_mandato, 'M') = aMan.im_mandato Then
             IBMERR001.RAISE_ERR_GENERICO('Il mandato '||CNRCTB038.getDesc(aMan)||' risulta già completamente riscontrato (totale dei riscontri ed importo '||
                                          ' del mandato pari a '||Ltrim(To_Char(aMan.im_mandato, '999g999g999g999g999g990d00'))||'). E'' impossibile quindi '||
                                          ' riscontrarlo ulteriormente tramite flusso per '||Ltrim(To_Char(aR30.importo_operazione, '999g999g999g999g999g990d00'))||'.');
 	   Elsif CNRCTB048.getImRiscontratoManRev(aMan.cd_cds, aMan.esercizio, aMan.pg_mandato, 'M') + aR30.importo_operazione > aMan.im_mandato then
             IBMERR001.RAISE_ERR_GENERICO('Impossibile riscontrare il mandato '||CNRCTB038.getDesc(aMan)||
                                ' per '||Ltrim(To_Char(aR30.importo_operazione, '999g999g999g999g999g990d00'))||'. Esso risulta già riscontrato per '||
                                Ltrim(To_Char(CNRCTB048.getImRiscontratoManRev(aMan.cd_cds, aMan.esercizio, aMan.pg_mandato, 'M'), '999g999g999g999g999g990d00'))||
                                ' e pertanto il totale dei riscontri supererebbe l''importo del mandato stesso che è di '||
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
                     IBMERR001.RAISE_ERR_GENERICO('Un mandato di un CDS ('||aCds.cd_unita_organizzativa||') non può essere da Banca d''Italia (sul conto 218154)');
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
       IBMERR001.RAISE_ERR_GENERICO('Il Tipo Ordinativo '||aR30.TIPO_ORDINATIVO||' non è compatibile, può assumere solo i valori R (Reversale) e M (Mandato)');
      end if;

     Else
       -- differenzio gli errori
       If aR30.STATO_ORDINATIVO != '03' Then
          IBMERR001.RAISE_ERR_GENERICO('Lo Stato Ordinativo assume valore diverso da "03" ('||aR30.STATO_ORDINATIVO||')');
       Elsif aR30.SEGNO_IMPORTO_OPERAZIONE != 1 Then
          IBMERR001.RAISE_ERR_GENERICO('Il Segno Importo Operazione è diverso da "1" che indica il segno positivo dell''operazione '||aR30.SEGNO_IMPORTO_OPERAZIONE);
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

 -- If aR01.anno_eser <> aEs Then -- 3
 --     IBMERR001.RAISE_ERR_GENERICO('Anno di esercizio del record TR01 ('||aR01.anno_eser||') diverso dall''esercizio di bilancio '||aEs);
 -- End if; -- 3

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
                                    ' ma tale importo è differente rispetto all''importo che il sospeso già possiede che è di '||
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
           IBMERR001.RAISE_ERR_GENERICO('Impossibile stornare il sospeso '||getDesc(aSosF)||', risulta già associato a documenti autorizzatori per '||Ltrim(To_Char(aSosF.im_associato, '999g999g999g999g999g990d00')));
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
                                             ' non può essere da Banca d''Italia (sul conto 218154)');
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

            If aR32.IMPORT*aR32.SEGNO_IMPO != importo_sospeso_esistente Then -- sospeso già esistente di importo diverso
               IBMERR001.RAISE_ERR_GENERICO('Impossibile inserire il sospeso '||aSospeso.CD_CDS||'/'||aSospeso.ESERCIZIO||'/'||aSospeso.TI_ENTRATA_SPESA||'/'||
                                             aSospeso.TI_SOSPESO_RISCONTRO||'/'||aSospeso.CD_SOSPESO||', già esiste. L''importo del sospeso che si sta tentando di '||
                                             'inserire è '||Ltrim(To_Char(aR32.IMPORT*aR32.SEGNO_IMPO, '999g999g999g999g999g990d00'))||' mentre l''importo del sospeso '||
                                             'già esistente con lo stesso codice è '||Ltrim(To_Char(importo_sospeso_esistente, '999g999g999g999g999g990d00'))||'.');
	    Else -- sospeso già esistente di uguale importo
	       IBMERR001.RAISE_ERR_GENERICO('Impossibile inserire il sospeso '||aSospeso.CD_CDS||'/'||aSospeso.ESERCIZIO||'/'||aSospeso.TI_ENTRATA_SPESA||'/'||
                                             aSospeso.TI_SOSPESO_RISCONTRO||'/'||aSospeso.CD_SOSPESO||'. Con quel codice ne esiste già uno ed ha lo stesso importo di quello che si sta'||
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

      -- Condizioni di processabilità non verificate
Else
  If aR32.STATO_SOSP != '01' Then
    IBMERR001.RAISE_ERR_GENERICO('Sospeso non caricabile per stato diverso da 01 ('||CNRCTB755.getDesc(aR)||'). Lo stato sospeso è '||aR32.STATO_SOSP);
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
dbms_output.put_line('fine primo loop');
-- nuovo 
 For aR in (Select * from MOVIMENTO_CONTO_EVIDENZA
             Where    esercizio = aEs And
                      IDENTIFICATIVO_FLUSSO = aNomeFile And
                      stato     = CNRCTB755.STATO_RECORD_INIZIALE
             Order by progressivo asc) Loop
    Begin -- CHIUDE ALLA FINE, APPENA PRIMA DELL'END LOOP SULLE RIGHE DI EXT_CASSIERE00 E POI INSERISCE I LOG
	    aggiorna_sospeso :=false;
	    dbms_output.put_line('entro secondo loop');
	    aEsercizio:=aR.esercizio;
      -- ====================================================================================================
      --                                                RISCONTRI
      -- ====================================================================================================
      If (aR.tipo_documento in('MANDATO','REVERSALE')) Then
	      dbms_output.put_line('if riscontri');
        aNumProcessati:=aNumProcessati+1;
        -- Check esistenza record di testata
        If aR.tipo_operazione in('ESEGUITO','REGOLARIZZATO')  then
          select * into aCds from unita_organizzativa
  			  where cd_tipo_unita ='ENTE'
          and fl_cds='Y';
          -- ====================================================================================================
          --                                                REVERSALE
          -- ====================================================================================================
          If aR.tipo_documento = 'REVERSALE' Then
            aRev:=null;
    			  aRev.esercizio:=aEsercizio;
    			  if(tesoreria_unica ='N') then
    				  aRev.cd_cds:=aCds.cd_unita_organizzativa;
    			  end if;
		    	  aRev.pg_reversale:= aR.numero_documento;
            -- Reversale
            Begin
              Select * into aRev
   	          From reversale
   	          Where esercizio = aRev.esercizio
              And cd_cds like decode(tesoreria_unica,'N',aCds.cd_unita_organizzativa,'%')
              And pg_reversale = aR.numero_documento
              And stato <> CNRCTB038.STATO_AUT_ANN
 	            For update nowait;
 	          Exception when NO_DATA_FOUND Then
              Select Count(*)
              Into conta_err
              From reversale
              Where esercizio = aRev.esercizio
              And cd_cds like decode(tesoreria_unica,'N',aCds.cd_unita_organizzativa,'%')
              And pg_reversale = aR.numero_documento
              And stato = CNRCTB038.STATO_AUT_ANN;
              If Conta_err > 0 Then
                IBMERR001.RAISE_ERR_GENERICO('Il riscontro di euro '||Ltrim(To_Char(aR.importo, '999g999g999g999g999g990d00'))||' fa riferimento alla reversale '||aCds.cd_unita_organizzativa||'/'||aEsercizio||'/'||aR.numero_documento||' che risulta annullata.');
              Else
                IBMERR001.RAISE_ERR_GENERICO('Il riscontro di euro '||Ltrim(To_Char(aR.importo, '999g999g999g999g999g990d00'))||' fa riferimento alla reversale '||aCds.cd_unita_organizzativa||'/'|| aEsercizio||'/'||aR.numero_documento||' che non esiste.');
              End If;
 	          End;
            -- controllo lo sfondamento del riscontrato (a questo punto automatico, avendo già controllato l'inesistenza di riscontri manuali)
   	        If CNRCTB048.getImRiscontratoManRev(aRev.cd_cds, aRev.esercizio, aRev.pg_reversale, 'R') = aRev.im_reversale Then
              IBMERR001.RAISE_ERR_GENERICO('La reversale '||CNRCTB038.getDesc(aRev)||' risulta già completamente riscontrata (totale dei riscontri ed importo '||
                                          ' della reversale pari a '||Ltrim(To_Char(aRev.im_reversale, '999g999g999g999g999g990d00'))||'). E'' impossibile quindi '||
                                          ' riscontrarla ulteriormente tramite flusso per '||Ltrim(To_Char(aR30.importo_operazione, '999g999g999g999g999g990d00'))||'.');
            Elsif CNRCTB048.getImRiscontratoManRev(aRev.cd_cds, aRev.esercizio, aRev.pg_reversale, 'R') + aR30.importo_operazione > aRev.im_reversale then
              IBMERR001.RAISE_ERR_GENERICO('Impossibile riscontrare la reversale '||CNRCTB038.getDesc(aRev)||
                                ' per '||Ltrim(To_Char(aR.importo, '999g999g999g999g999g990d00'))||'. Essa risulta già riscontrata per '||
                                Ltrim(To_Char(CNRCTB048.getImRiscontratoManRev(aRev.cd_cds, aRev.esercizio, aRev.pg_reversale, 'R'), '999g999g999g999g999g990d00'))||
                                ' e pertanto il totale dei riscontri supererebbe l''importo della reversale stessa che è di '||
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
            aRisc.DT_REGISTRAZIONE:=aR.DATA_MOVIMENTO;
            aRisc.DS_ANAGRAFICO:=aR.ANAGRAFICA_CLIENTE;
            aRisc.CAUSALE:=aR.CAUSALE;
       
            if aR.tipo_esecuzione in('ACCREDITO BANCA D''ITALIA','ACCREDITO TESORERIA PROVINCIALE STATO PER TAB A','ACCREDITO TESORERIA PROVINCIALE STATO PER TAB B',
              'REGOLARIZZAZIONE ACCREDITO BANCA D''ITALIA','REGOLARIZZAZIONE ACCREDITO TESORERIA PROVINCIALE STATO PER TAB A','REGOLARIZZAZIONE ACCREDITO TESORERIA PROVINCIALE STATO PER TAB B') then
              aRisc.TI_CC_BI:='B';
            else
              aRisc.TI_CC_BI:='C';
            end if;
            aRisc.FL_STORNATO:='N';
            aRisc.IM_SOSPESO:=aR.IMPORTO;
            aRisc.IM_ASSOCIATO:=aR.IMPORTO;
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
            Update movimento_conto_evidenza
            Set STATO = CNRCTB755.STATO_RECORD_PROCESSATO,duva = aRisc.duva, utuv= aRisc.utuv, pg_ver_rec = pg_ver_rec+1
            Where  esercizio = aR.esercizio
            And identificativo_flusso = aR.identificativo_flusso
            And progressivo = aR.progressivo;

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
            Set im_incassato = CNRCTB048.getImRiscontratoManRev(aRev.cd_cds, aRev.esercizio, aR.numero_documento, 'R'), duva = aRisc.duva, utuv=aRisc.utuv, pg_ver_rec=pg_ver_rec+1
	          Where esercizio = aRev.esercizio
            And cd_cds = aRev.cd_cds
            And pg_reversale = aR.numero_documento;
 	          -- Aggiorno lo stato di incasso della reversale e aggiorno i saldi

            If CNRCTB048.getImRiscontratoManRev(aRev.cd_cds, aRev.esercizio,aR.numero_documento, 'R') = aRev.im_reversale then
             CNRCTB037.riscontroReversale(aRev.esercizio,aRev.cd_cds,aRev.pg_reversale,'I',aUser);
            End If;
            aNumOk := aNumOk+1;
          Elsif aR.TIPO_DOCUMENTO = 'MANDATO' Then
            dbms_output.put_line('if riscontri man');
            -- ====================================================================================================
            --                                                MANDATO
            -- ====================================================================================================
            aMan := null;
	          aMan.esercizio := aEsercizio;
            if(tesoreria_unica ='N') then
	   		      aMan.cd_cds := aCds.cd_unita_organizzativa;
		        end if;
	   	      aMan.pg_mandato := aR.numero_documento;

            Begin
              Select * Into aMan
              From  mandato
              Where  esercizio =  aMan.esercizio
              And cd_cds like  decode(tesoreria_unica,'N',aCds.cd_unita_organizzativa,'%')
              And pg_mandato = aR.numero_documento
              And stato <> CNRCTB038.STATO_AUT_ANN
              For Update Nowait;
            Exception when NO_DATA_FOUND Then
 	            Select Count(*) Into conta_err
   	          From mandato
  	          Where  esercizio =  aMan.esercizio
              And cd_cds like decode(tesoreria_unica,'N',aCds.cd_unita_organizzativa,'%')
              And pg_mandato = aR.numero_documento
              And stato = CNRCTB038.STATO_AUT_ANN;
              If Conta_err > 0 Then
               IBMERR001.RAISE_ERR_GENERICO('Il riscontro di euro '||Ltrim(To_Char(aR.importo, '999g999g999g999g999g990d00'))||' fa riferimento al mandato '||aCds.cd_unita_organizzativa||'/'|| aEsercizio||'/'||aR.numero_documento||' che risulta annullato.');
              Else
               IBMERR001.RAISE_ERR_GENERICO('Il riscontro di euro '||Ltrim(To_Char(aR.importo, '999g999g999g999g999g990d00'))||' fa riferimento al mandato '||aCds.cd_unita_organizzativa||'/'|| aEsercizio||'/'||aR.numero_documento||' che non esiste.');
              End If;
 	          End;
	          -- controllo lo sfondamento del riscontrato (a questo punto automatico, avendo già controllato l'inesistenza di riscontri manuali)
   	        If CNRCTB048.getImRiscontratoManRev(aMan.cd_cds, aMan.esercizio, aMan.pg_mandato, 'M') = aMan.im_mandato Then
             IBMERR001.RAISE_ERR_GENERICO('Il mandato '||CNRCTB038.getDesc(aMan)||' risulta già completamente riscontrato (totale dei riscontri ed importo '||
                                          ' del mandato pari a '||Ltrim(To_Char(aMan.im_mandato, '999g999g999g999g999g990d00'))||'). E'' impossibile quindi '||
                                          ' riscontrarlo ulteriormente tramite flusso per '||Ltrim(To_Char(aR30.importo_operazione, '999g999g999g999g999g990d00'))||'.');
 	          Elsif CNRCTB048.getImRiscontratoManRev(aMan.cd_cds, aMan.esercizio, aMan.pg_mandato, 'M') + aR.importo > aMan.im_mandato then
             IBMERR001.RAISE_ERR_GENERICO('Impossibile riscontrare il mandato '||CNRCTB038.getDesc(aMan)||
                                ' per '||Ltrim(To_Char(aR30.importo_operazione, '999g999g999g999g999g990d00'))||'. Esso risulta già riscontrato per '||
                                Ltrim(To_Char(CNRCTB048.getImRiscontratoManRev(aMan.cd_cds, aMan.esercizio, aMan.pg_mandato, 'M'), '999g999g999g999g999g990d00'))||
                                ' e pertanto il totale dei riscontri supererebbe l''importo del mandato stesso che è di '||
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
            aRisc.DT_REGISTRAZIONE:=aR.DATA_MOVIMENTO;
            aRisc.DS_ANAGRAFICO:=aR.ANAGRAFICA_CLIENTE;
            aRisc.CAUSALE:=aR.CAUSALE;
            if aR.tipo_esecuzione in('ACCREDITO BANCA D''ITALIA','ACCREDITO TESORERIA PROVINCIALE STATO PER TAB A','ACCREDITO TESORERIA PROVINCIALE STATO PER TAB B',
               'REGOLARIZZAZIONE ACCREDITO BANCA D''ITALIA','REGOLARIZZAZIONE ACCREDITO TESORERIA PROVINCIALE STATO PER TAB A','REGOLARIZZAZIONE ACCREDITO TESORERIA PROVINCIALE STATO PER TAB B') then
              aRisc.TI_CC_BI:='B';
            else
              aRisc.TI_CC_BI:='C';
            end if;
            aRisc.FL_STORNATO:='N';
            aRisc.IM_SOSPESO:=aR.IMPORTO;
            aRisc.IM_ASSOCIATO:=aR.IMPORTO;
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

	          Update MOVIMENTO_CONTO_EVIDENZA
	          Set STATO = CNRCTB755.STATO_RECORD_PROCESSATO, duva = aRisc.duva, utuv = aRisc.utuv, pg_ver_rec=pg_ver_rec+1
	          Where esercizio = aR.esercizio
            And identificativo_flusso = aR.identificativo_flusso
            And progressivo = aR.progressivo;
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
  	        Update mandato Set im_pagato = CNRCTB048.getImRiscontratoManRev(aMan.cd_cds, aMan.esercizio, aR.numero_documento, 'M'), duva=aRisc.duva, utuv=aRisc.utuv, pg_ver_rec=pg_ver_rec+1
	          Where  esercizio = aMan.esercizio
            And cd_cds = aMan.cd_cds
            And pg_mandato = aR.numero_documento;
 	          -- Aggiorno lo stato di incasso del mandato e aggiorno i saldi

 	          if CNRCTB048.getImRiscontratoManRev(aMan.cd_cds, aMan.esercizio, aR.numero_documento, 'M') = aMan.im_mandato then
 	            CNRCTB037.riscontroMandato(aMan.esercizio,aMan.cd_cds,aMan.pg_mandato,'I',aUser);
            end if;
            aNumOk:=aNumOk+1;
          else -- Doc non riconosciuto
            IBMERR001.RAISE_ERR_GENERICO('Il Tipo Ordinativo '||aR.TIPO_DOCUMENTO||' non è compatibile, può assumere solo i valori R (Reversale) e M (Mandato)');
          end if;
        ElsIf aR.tipo_operazione = 'STORNATO'  then
          If aR.TIPO_DOCUMENTO = 'MANDATO' Then
            dbms_output.put_line('if stornato man');
            -- ====================================================================================================
            --                                                MANDATO
            -- ====================================================================================================
            aMan := null;
            aDetUsc := null;
            aMan.esercizio := aEsercizio;
            if(tesoreria_unica ='N') then
              aMan.cd_cds := aCds.cd_unita_organizzativa;
            end if;
            aMan.pg_mandato := aR.numero_documento;

            Begin
              Select * Into aMan
              From  mandato
              Where  esercizio =  aMan.esercizio
              And cd_cds like  decode(tesoreria_unica,'N',aCds.cd_unita_organizzativa,'%')
              And pg_mandato = aR.numero_documento
              And stato = CNRCTB038.STATO_AUT_ANN
              For Update Nowait;
            Exception when NO_DATA_FOUND Then
              Select Count(*) Into conta_err
              From mandato
              Where  esercizio =  aMan.esercizio
              And cd_cds like decode(tesoreria_unica,'N',aCds.cd_unita_organizzativa,'%')
              And pg_mandato = aR.numero_documento
              And stato <> CNRCTB038.STATO_AUT_ANN;
              If Conta_err > 0 Then
                IBMERR001.RAISE_ERR_GENERICO('Lo Storno di euro '||Ltrim(To_Char(aR.importo, '999g999g999g999g999g990d00'))||' fa riferimento al mandato '||aCds.cd_unita_organizzativa||'/'|| aEsercizio||'/'||aR.numero_documento||' che non risulta annullato.');
              Else
                IBMERR001.RAISE_ERR_GENERICO('Lo Storno di euro '||Ltrim(To_Char(aR.importo, '999g999g999g999g999g990d00'))||' fa riferimento al mandato '||aCds.cd_unita_organizzativa||'/'|| aEsercizio||'/'||aR.numero_documento||' che non esiste.');
              End If;
            End;
            BEGIN
              Select * into aDetUsc
              from SOSPESO_DET_USC
              Where CD_CDS = aMan.cd_cds
              And ESERCIZIO = aMan.esercizio
              And PG_MANDATO = aMan.pg_mandato
              And TI_ENTRATA_SPESA ='S'
              And TI_SOSPESO_RISCONTRO ='R';

              -- Azzero il riscontro del Mandato
              Update SOSPESO_DET_USC set IM_ASSOCIATO = IM_ASSOCIATO + aR.IMPORTO, duva=aTSNow, utuv=aUser, pg_ver_rec=pg_ver_rec+1
              Where CD_CDS = aMan.cd_cds
              And ESERCIZIO = aMan.esercizio
              And PG_MANDATO = aMan.pg_mandato
              And TI_ENTRATA_SPESA ='S'
              And TI_SOSPESO_RISCONTRO ='R'
              And CD_SOSPESO = aDetUsc.CD_SOSPESO;

              Update SOSPESO set IM_ASSOCIATO = IM_ASSOCIATO + aR.IMPORTO , IM_SOSPESO = IM_SOSPESO + aR.IMPORTO, FL_STORNATO= 'Y', DT_STORNO=aR.data_movimento , duva=aTSNow, utuv=aUser, pg_ver_rec=pg_ver_rec+1
              Where CD_CDS = aMan.cd_cds
              And ESERCIZIO = aMan.esercizio
              And TI_ENTRATA_SPESA ='S'
              And TI_SOSPESO_RISCONTRO ='R'
              And CD_SOSPESO = aDetUsc.CD_SOSPESO;

              -- Aggiorno lo stato trasmissione del mandato annullato
              Update mandato Set stato_trasmissione_annullo = 'T', duva=aTSNow, utuv=aUser, pg_ver_rec=pg_ver_rec+1
              Where  esercizio = aMan.esercizio
              And cd_cds = aMan.cd_cds
              And pg_mandato = aR.numero_documento;

              Update MOVIMENTO_CONTO_EVIDENZA
              Set STATO = CNRCTB755.STATO_RECORD_PROCESSATO, duva = aTSNow, utuv = aUser, pg_ver_rec=pg_ver_rec+1
              Where esercizio = aR.esercizio
              And identificativo_flusso = aR.identificativo_flusso
              And progressivo = aR.progressivo;
            Exception when NO_DATA_FOUND Then
              IBMERR001.RAISE_ERR_GENERICO('Lo Storno di euro '||Ltrim(To_Char(aR.importo, '999g999g999g999g999g990d00'))||' fa riferimento al mandato '||aCds.cd_unita_organizzativa||'/'|| aEsercizio||'/'||aR.numero_documento||' su cui non esiste il riscontro.');
            END;
          End If;
        End If; -- end controllo stato ordinativo = '03'
      End If;  -- TERMINE GESTIONE RISCONTRI (record T30)


-- =========================
-- SOSPESI
-- =========================
	
If  (aR.tipo_documento like 'SOSPESO%')  Then -- 1
	select * into aCds from unita_organizzativa
  where cd_tipo_unita ='ENTE' and fl_cds='Y';
  	
    
-- GESTIONE STORNO -- 4
if (aR.tipo_operazione = 'STORNATO') then
     
  	aSospeso := null;

   Begin
       Select *
       Into   aSospeso
       From   sospeso
       Where  cd_cds = aCds.cd_unita_organizzativa And
              esercizio = aR.esercizio And
              ti_entrata_spesa = decode(aR.tipo_movimento,'ENTRATA','E','S') And
              ti_sospeso_riscontro = 'S' And
              cd_sospeso = lpad(aR.numero_documento,18,'0')  And
              cd_sospeso_padre is null
       For Update Nowait;
   Exception when NO_DATA_FOUND then
       IBMERR001.RAISE_ERR_GENERICO('Sospeso da stornare non trovato: '||aR.numero_documento||' cds.: '||aCds.cd_unita_organizzativa||' es.: '||aR.esercizio);
   End;

   If abs(aR.IMPORTO) <> aSospeso.im_sospeso Then -- 5
        IBMERR001.RAISE_ERR_GENERICO('Si sta tentando di stornare il sospeso '||getDesc(aSospeso)||' per '
                                     ||Ltrim(To_Char(aR.IMPORTO, '999g999g999g999g999g990d00'))||
                                    ' ma tale importo è differente rispetto all''importo che il sospeso già possiede che è di '||
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
           IBMERR001.RAISE_ERR_GENERICO('Impossibile stornare il sospeso '||getDesc(aSosF)||', risulta già associato a documenti autorizzatori per '||Ltrim(To_Char(aSosF.im_associato, '999g999g999g999g999g990d00')));
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
               dt_storno   = aR.data_movimento,
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
             dt_storno    = aR.data_movimento,
	     duva         = aTSNow,
	     utuv         = aUser,
	     pg_ver_rec   = pg_ver_rec+1
       Where esercizio            = aSospeso.esercizio And
             cd_cds               = aSospeso.cd_cds And
             cd_sospeso           = aSospeso.cd_sospeso And
             ti_entrata_spesa     = aSospeso.ti_entrata_spesa And
             ti_sospeso_riscontro = aSospeso.ti_sospeso_riscontro;

       Update movimento_conto_evidenza
       Set    STATO=CNRCTB755.STATO_RECORD_PROCESSATO,
              duva=aTSNow,
              utuv=aUser,
	      pg_ver_rec=pg_ver_rec+1
       Where  esercizio =aR.esercizio And
              identificativo_flusso = aR.identificativo_flusso And
              progressivo = aR.progressivo;

       aNumOk := aNumOk+1;

Elsif -- GESTIONE INSERIMENTO IF 4

        aR.TIPO_OPERAZIONE = 'ESEGUITO'  Then

 	  	aSospeso:=null;
 	  	aSF:=null;
  	  aSospeso.CD_CDS:=aCds.cd_unita_organizzativa;
      aSospeso.ESERCIZIO:=aR.esercizio;
      
  	  if aR.TIPO_MOVIMENTO='ENTRATA' then          -- VERIFICARE I VALORI
               aSospeso.TI_ENTRATA_SPESA:='E';
          else
               aSospeso.TI_ENTRATA_SPESA:='S';
  	  end if;

  	  		aSospeso.TI_SOSPESO_RISCONTRO:='S';
          aSospeso.CD_SOSPESO:=lpad(aR.numero_documento,18,'0');
          aSospeso.CD_CDS_ORIGINE:=null;
          aSospeso.CD_UO_ORIGINE:=null;
          aSospeso.DT_REGISTRAZIONE:=aR.DATA_MOVIMENTO;
          aSospeso.DS_ANAGRAFICO:=aR.ANAGRAFICA_CLIENTE;
          aSospeso.CAUSALE:=aR.CAUSALE;
          -- identificazione banca d'Italia solo per sospesi ENTE
	  --If aR01.NUMERO_CONT_TESO = '000000218154' then
	  if ((aR.codice_rif_interno is not null and aR.codice_rif_interno like 'FZPBP1%') 
	  	or(aR.tipo_esecuzione in('ACCREDITO BANCA D''ITALIA','ACCREDITO TESORERIA PROVINCIALE STATO PER TAB A','ACCREDITO TESORERIA PROVINCIALE STATO PER TAB B',
	  	'REGOLARIZZAZIONE ACCREDITO BANCA D''ITALIA','REGOLARIZZAZIONE ACCREDITO TESORERIA PROVINCIALE STATO PER TAB A','REGOLARIZZAZIONE ACCREDITO TESORERIA PROVINCIALE STATO PER TAB B'))) then
        If aCds.cd_tipo_unita = CNRCTB020.TIPO_ENTE then
	        aSospeso.TI_CC_BI:='B';
 	      Else
                IBMERR001.RAISE_ERR_GENERICO('Il sospeso '||lpad(aR.numero_documento,18,'0')||' del CDS '||aCds.cd_unita_organizzativa||
                                             ' non può essere da Banca d''Italia');
	      End if;
	  Else
              aSospeso.TI_CC_BI:='C';
 	  End if;
					--aSospeso.TI_CC_BI:='C';
          aSospeso.FL_STORNATO:='N';
          aSospeso.IM_SOSPESO:=abs(aR.IMPORTO);
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
					aSospeso.tipo_contabilita:=aR.tipo_contabilita;
					aSospeso.destinazione:=aR.destinazione;
          Begin
	        CNRCTB038.ins_SOSPESO(aSospeso);
	        Exception when DUP_VAL_ON_INDEX Then
	           if (aR.codice_rif_interno is not null and aR.codice_rif_interno like 'PAGSTIP%')  Then
	              update sospeso set im_sospeso= im_sospeso+abs(aR.IMPORTO)
	              where
	               			 CD_CDS               = aSospeso.CD_CDS And
                       ESERCIZIO            = aSospeso.ESERCIZIO And
                       TI_ENTRATA_SPESA     = aSospeso.TI_ENTRATA_SPESA And
                       TI_SOSPESO_RISCONTRO = aSospeso.TI_SOSPESO_RISCONTRO And
                       CD_SOSPESO           = aSospeso.CD_SOSPESO;
	              update sospeso set im_sospeso= im_sospeso+abs(aR.IMPORTO)
	              where
	               			 CD_CDS               = aSospeso.CD_CDS And
                       ESERCIZIO            = aSospeso.ESERCIZIO And
                       TI_ENTRATA_SPESA     = aSospeso.TI_ENTRATA_SPESA And
                       TI_SOSPESO_RISCONTRO = aSospeso.TI_SOSPESO_RISCONTRO And
                       CD_SOSPESO           = aSospeso.CD_SOSPESO|| '.001';     
                aggiorna_sospeso:=true;       
           	else          
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

            	If abs(aR.IMPORTO) != importo_sospeso_esistente Then -- sospeso già esistente di importo diverso
               	IBMERR001.RAISE_ERR_GENERICO('Impossibile inserire il sospeso '||aSospeso.CD_CDS||'/'||aSospeso.ESERCIZIO||'/'||aSospeso.TI_ENTRATA_SPESA||'/'||
                                             aSospeso.TI_SOSPESO_RISCONTRO||'/'||aSospeso.CD_SOSPESO||', già esiste. L''importo del sospeso che si sta tentando di '||
                                             'inserire è '||Ltrim(To_Char(aR.IMPORTO, '999g999g999g999g999g990d00'))||' mentre l''importo del sospeso '||
                                             'già esistente con lo stesso codice è '||Ltrim(To_Char(importo_sospeso_esistente, '999g999g999g999g999g990d00'))||'.');
				    	Else -- sospeso già esistente di uguale importo
				       	IBMERR001.RAISE_ERR_GENERICO('Impossibile inserire il sospeso '||aSospeso.CD_CDS||'/'||aSospeso.ESERCIZIO||'/'||aSospeso.TI_ENTRATA_SPESA||'/'||
			                                             aSospeso.TI_SOSPESO_RISCONTRO||'/'||aSospeso.CD_SOSPESO||'. Con quel codice ne esiste già uno ed ha lo stesso importo di quello che si sta'||
			                                             ' tentando di caricare, vale a dire '||Ltrim(To_Char(aR.IMPORTO, '999g999g999g999g999g990d00'))||'.');
				    	End If;
	    			end if;
	  End;
			If (aggiorna_sospeso=false) then
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
    End if;
	  Update movimento_conto_evidenza
	  Set    STATO=CNRCTB755.STATO_RECORD_PROCESSATO,
                 duva=aTSNow,
                 utuv=aUser,
	         pg_ver_rec=pg_ver_rec+1
	  Where  esercizio =aR.esercizio And
	         identificativo_flusso = aR.identificativo_flusso And
	         progressivo = aR.progressivo;

	  aNumOk:=aNumOk+1;

      -- Condizioni di processabilità non verificate
End if;

End if; -- TERMINE GESTIONE SOPESI (record T32) if n. 1

Commit;

Exception
 When Others Then
                Rollback;

-- 16.01.2008 nuova gestione degli scarti, inserisce nella tabella degli scarti

If (aR.tipo_documento in('MANDATO','REVERSALE')) Then

  aRecScarto := Null;

  aRecScarto.ESERCIZIO               := aEs;
  aRecScarto.NOME_FILE               := aNomeFile;
  aRecScarto.PG_ESECUZIONE           := pg_exec;
  aRecScarto.PG_REC                  := aR.progressivo;
  aRecScarto.DT_GIORNALIERA          := aR.data_movimento;
  aRecScarto.DT_ELABORAZIONE         := Trunc(aTSNow);
  aRecScarto.DT_ESECUZIONE           := null;
  aRecScarto.CD_CDS                  := aCds.cd_unita_organizzativa;

  aRecScarto.TIPO_MOV                := substr(aR.tipo_documento,1,1);
  aRecScarto.CD_CDS_MANREV           := aCds.cd_unita_organizzativa;
  aRecScarto.ESERCIZIO_MANREV        := aR.esercizio;
  aRecScarto.PG_MANREV               := aR.numero_documento;
  aRecScarto.ANOMALIA                := IBMUTL001.getErrorMessage;
  aRecScarto.DACR                    := aTSNow;
  aRecScarto.UTCR                    := aUser;
  aRecScarto.DUVA                    := aTSNow;
  aRecScarto.UTUV                    := aUser;
  aRecScarto.PG_VER_REC              := 1;

Elsif aR.tipo_documento like 'SOSPESO%' Then -- SOSPESI

  aRecScarto := Null;

  aRecScarto.ESERCIZIO               := aEs;
  aRecScarto.NOME_FILE               := aNomeFile;
  aRecScarto.PG_ESECUZIONE           := pg_exec;
  aRecScarto.PG_REC                  := aR.progressivo;
  aRecScarto.DT_GIORNALIERA          := ar.Data_movimento;
  aRecScarto.DT_ELABORAZIONE         := Trunc(aTSNow);
  aRecScarto.DT_ESECUZIONE           := null; -- DATA REGISTRAZIONE SOSPESO
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

IBMUTL200.logerr(pg_exec, aCds.cd_unita_organizzativa||'-'||aNomeFile||'-'|| 'Riga -'||aR.progressivo, aCds.cd_unita_organizzativa||'-'||aNomeFile||'-'|| 'Riga -'||aR.progressivo, DBMS_UTILITY.FORMAT_ERROR_STACK);
End;

End Loop;
-- fine nuovo
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

  If (To_Number(To_Char(Sysdate,'hh24')) < 6) Then
    data := To_Date(To_Char(Sysdate,'ddmmyyyy')||' '||'06:00','ddmmyyyy hh24:mi');
    ora := '06:00';
  Elsif (To_Number(To_Char(Sysdate,'hh24')) > 6 And To_Number(To_Char(Sysdate,'hh24')) < 13) Then
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
  ('L''operazione verrà eseguita alle ore '||ora||'. Al completamento l''utente riceverà un messaggio di notifica ' ||
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
		   	  aTi_doc := 'M'; -- Il documento in canna è un Mandato
		   else
		   	  aTi_doc := 'R'; -- Il documento in canna è una Reversale
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
							  and 	PG_REVERSALE IS NOT NULL
							  )
						   )
						 )
					);

					if (aTi_doc = 'M') then
					   IBMERR001.RAISE_ERR_GENERICO('Attenzione: il Mandato ' || dett_dist.PG_MANDATO || ' è associato ad un Compenso a cui sono associati Mandati/Reversali non inseriti in distinta');
					else
					   IBMERR001.RAISE_ERR_GENERICO('Attenzione: la Reversale ' || dett_dist.PG_REVERSALE || ' è associata ad un Compenso a cui sono associati Mandati/Reversali non inseriti in distinta');
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
		   	  aTi_doc := 'MAN'; -- Il documento in canna è un Mandato
		   else
		   	  aTi_doc := 'REV'; -- Il documento in canna è una Reversale
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
          AND (STATO_VAR_SOS IS NULL OR (STATO_VAR_SOS IS NOT NULL AND STATO_VAR_SOS NOT IN ('ANNULLATO_PER_SOSTITUZIONE', 'SOSTITUZIONE_DEFINITIVA')))
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
          AND (STATO_VAR_SOS IS NULL OR (STATO_VAR_SOS IS NOT NULL AND STATO_VAR_SOS NOT IN ('ANNULLATO_PER_SOSTITUZIONE', 'SOSTITUZIONE_DEFINITIVA')))
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
					   IBMERR001.RAISE_ERR_GENERICO('Attenzione: il Mandato ' || dett_dist.PG_MANDATO || ' è associato ad un ANNULLO, Mandato ' || aNum || ' non inserito in distinta');
					else
					   IBMERR001.RAISE_ERR_GENERICO('Attenzione: la Reversale ' || dett_dist.PG_REVERSALE || ' è associata ad un ANNULLO, Reversale ' || aNum || ' non inserita in distinta');
					end if;
				end if;
				exception when NO_DATA_FOUND then
				 null;
				end;
			end loop; -- fine ciclo sulla VIEW ANNULLI
	  end loop; -- fine ciclo sulla distinta per i mandati

 end;
END;
