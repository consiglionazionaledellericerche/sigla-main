--------------------------------------------------------
--  DDL for Package Body CNRCTB210
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "CNRCTB210" is

 procedure regDocAmmCoan(aEs number, aCds varchar2, aUO varchar2, aTiDocumento varchar2, aPgDocAmm number, aUser varchar2, aTSNow date) is
  aDocCoan V_COAN_DOCUMENTI%rowtype;
  aDoc     V_DOC_AMM_COGE_RIGA%Rowtype;
  aNum number;
 begin

     -- (10.05.2005 - copiata dal cnrctb205)
     -- Modifica del 15/03/2004
      -- Gestione speciale righe di generico di spesa su capitoli di parte 1 nell'ente
      -- Tali righe non generano movimenti

      If aTiDocumento = CNRCTB100.TI_GENERICO_SPESA And
          aDoc.ti_appartenenza_ev = CNRCTB001.APPARTENENZA_CNR then
	begin
	    select 1 into aNum from elemento_voce
	    Where esercizio=aDoc.esercizio_ev
              and ti_appartenenza=aDoc.ti_appartenenza_ev
			  and ti_gestione=aDoc.ti_gestione_ev
			  and cd_elemento_voce=aDoc.cd_elemento_voce_ev
			  and cd_parte=CNRCTB001.PARTE1;
        return;
	   exception when NO_DATA_FOUND then
	    null;
       end;
      end if;

    -- fine copia

        -- Se trovo che il documento è da processare o riprocessare in COAN eseguo il reprocessing altrimenti esco
		-- senza segnalare eccezioni
		BEGIN
	          SELECT *
	 	  into aDocCoan
	 	  FROM V_COAN_DOCUMENTI
	 	  WHERE cd_tipo_documento_amm = aTiDocumento
	 	  AND   cd_cds = aCds
	   	  AND   cd_unita_organizzativa = aUO
	 	  AND   esercizio = aEs
	 	  AND   pg_numero_documento = aPgDocAmm;
        EXCEPTION WHEN NO_DATA_FOUND THEN
	     return;
	    END;
--Dbms_Output.PUT_LINE ('A');
  	    CNRCTB210.BUILDMOVIMENTICOAN (aTiDocumento,
  	   							   aCds,
  	 							   aUO,
  	 							   aEs,
  	 							   aPgDocAmm,
								   aUser);
 end;

 procedure buildMovimentiCoan(aCd_Tipo_Documento_Amm V_COAN_DOCUMENTI.CD_TIPO_DOCUMENTO_AMM%TYPE,
 		   					  aCd_Cds V_COAN_DOCUMENTI.CD_CDS%TYPE,
 		   					  aCd_Unita_Organizzativa V_COAN_DOCUMENTI.CD_UNITA_ORGANIZZATIVA%TYPE,
 		   					  aEsercizio V_COAN_DOCUMENTI.ESERCIZIO%TYPE,
 		   					  aPg_Numero_Documento V_COAN_DOCUMENTI.PG_NUMERO_DOCUMENTO%TYPE,
							  Utente varchar2
 							  ) is
 num_scadenze number;
 contatore number :=1;
 cSezione CHAR(1);
 num_scritture number;

 lListaMovimenti cnrctb200.movAnalitList;
 lListaScadenze  tScadenze;

 riga_scrittura scrittura_analitica%rowtype;
 aOldScrittura scrittura_analitica%rowtype;
 aTmpScrittura scrittura_analitica%rowtype;
 lOldListaMovimenti cnrctb200.movAnalitList;
 rDocumento V_COAN_DOCUMENTI%rowtype;
 rScadenze  v_coan_scadenze%rowtype;
 rContoEp voce_ep%rowtype;
 aTSNow date;

 begin
      aTSNow:=sysdate;
 	  -- Azzeramento della varibile che identifica il numero di linee di attivita
	  -- per il documento
  	  num_movimento_doc :=0;

 	  BEGIN
		  -- Selezionare il documento dalla vista dei DOCUMENTI COAN,
		  -- Se il documento
--Dbms_Output.PUT_LINE ('B');
	 	  SELECT *
		  into rDocumento
		  FROM V_COAN_DOCUMENTI
		  WHERE cd_tipo_documento_amm = aCd_Tipo_Documento_Amm
		  AND   cd_cds = aCd_Cds
	  	  AND   cd_unita_organizzativa = aCd_Unita_Organizzativa
		  AND   esercizio = aEsercizio
		  AND   pg_numero_documento = aPg_Numero_Documento;
	  EXCEPTION WHEN NO_DATA_FOUND THEN
 		   IBMERR001.RAISE_ERR_GENERICO('Il Documento non deve essere contabilizzato in COAN');
	  END;

	  -- Lock del documento amministrativo
--Dbms_Output.PUT_LINE ('C');
	  CNRCTB100.LOCKDOCAMM(
	   aCd_Tipo_Documento_Amm,
	   aCd_Cds,
	   aEsercizio,
	   aCd_Unita_Organizzativa,
	   aPg_Numero_Documento
	  );

      if not (CNRCTB200.ISCHIUSURACOEPDEF(rDocumento.esercizio-1, rDocumento.cd_cds_origine)='Y') then
       IBMERR001.RAISE_ERR_GENERICO('L''esercizio economico precedente non è chiuso definitivamente per il cds: '||rDocumento.cd_cds_origine);
      end if;

	  -- Gestione filtrodocumenti speciali che non vanno in COGE come prime scritture
      -- in attesa di porre tali documenti in stato coge escluso
      if aCd_Tipo_Documento_Amm in (
            CNRCTB100.TI_GENERICO_TRASF_E,
			CNRCTB100.TI_GENERICO_TRASF_S,
			CNRCTB100.TI_GEN_APERTURA_FONDO,
			CNRCTB100.TI_GEN_IVA_ENTRATA
	 )
      Then
      --Dbms_Output.PUT_LINE ('D');
       CNRCTB100.UPDATEDOCAMM_NODUVAUTUV(
        aCd_Tipo_Documento_Amm,
        aCd_Cds,
        aEsercizio,
        aCd_Unita_Organizzativa,
        aPg_Numero_Documento,
        'stato_coan='''||CNRCTB100.STATO_COEP_EXC||'''',
        null
       );
       return;
      end if;
--Dbms_Output.PUT_LINE ('E');
      -- Fix del 20040924 Richiesta 843
      CNRCTB204.checkChiusuraEsercizio(rDocumento.esercizio, rDocumento.cd_cds_origine);

      -- Se il documento è riportato anche parzialmente non può essere processato in analitica
--Dbms_Output.PUT_LINE ('F');
      if
       CNRCTB105.isRiportato(rDocumento.cd_cds,rDocumento.cd_unita_organizzativa, rDocumento.esercizio,
	                         rDocumento.pg_numero_documento, rDocumento.cd_tipo_documento_amm
							) = 'Y'
      then
       IBMERR001.RAISE_ERR_GENERICO('Il documento riportato (anche parzialmente) non può essere processato automaticamente in analitica');
      end if;

	  -- Compensi senza costo principale non transitano in analitica
--Dbms_Output.PUT_LINE ('G');
      if
           aCd_Tipo_Documento_Amm =CNRCTB100.TI_COMPENSO
	   and rDocumento.im_totale_fattura = 0 -- im_totale_compenso = 0 (compensi senza costo principale sono d'ufficio messi a contabilizzati)
	  then
       CNRCTB100.UPDATEDOCAMM_NODUVAUTUV(
        aCd_Tipo_Documento_Amm,
        aCd_Cds,
        aEsercizio,
        aCd_Unita_Organizzativa,
        aPg_Numero_Documento,
        'stato_coan='''||CNRCTB100.STATO_COEP_CON||'''',
        null
       );
       return;
      end if;

	  -- Cancellazione delle liste logiche
--Dbms_Output.PUT_LINE ('H');
	  lMovimentiPlus.delete;
	  lListaMovimenti.delete;
	  lListaScadenze.delete;

	  -- Savepoint per il rollback parziale nel caso la scrittura rigenerata non sia diversa da quella
	  -- di partenza

	  SAVEPOINT SAVEPOINT_COAN00;

	  -- conta scritture
	  begin
--Dbms_Output.PUT_LINE ('I');
 	   SELECT * into aOldScrittura from scrittura_analitica
	   WHERE
     	        ESERCIZIO_DOCUMENTO_AMM = rDocumento.esercizio
     	  AND   CD_CDS_DOCUMENTO = rDocumento.cd_cds
     	  AND   CD_UO_DOCUMENTO = rDocumento.cd_unita_organizzativa
     	  AND   CD_TIPO_DOCUMENTO = rDocumento.cd_tipo_documento_amm
     	  AND   PG_NUMERO_DOCUMENTO = rDocumento.pg_numero_documento
     	  AND   ATTIVA ='Y';
	  	 -- se esiste una scrittura attiva per il documento, allora
		 -- bisogna effettuare lo storno della stessa.
--Dbms_Output.PUT_LINE ('J');
       CNRCTB200.GETSCRITTURAANLOCK(aOldScrittura,lOldListaMovimenti);
	   aTmpScrittura:=aOldScrittura;
--Dbms_Output.put_line ('nel 210 CREASCRITTSTORNOCOAN cds '||lOldListaMovimenti(1).TI_ISTITUZ_COMMERC);

       cnrctb200.CREASCRITTSTORNOCOAN(aTmpScrittura, lOldListaMovimenti, utente);
	  exception when NO_DATA_FOUND then
	   null;
      end;

      if
          rDocumento.stato_cofi = CNRCTB100.STATO_GEN_COFI_ANN
      then
       CNRCTB100.UPDATEDOCAMM_NODUVAUTUV(
        rDocumento.cd_tipo_documento_amm,
        rDocumento.cd_cds,
        rDocumento.esercizio,
        rDocumento.cd_unita_organizzativa,
        rDocumento.pg_numero_documento,
        'stato_coan='''||CNRCTB100.STATO_COEP_CON||'''',
        null
       );
       return;
      end if;

      -- recupero della scrittura associata al Documento
--Dbms_Output.PUT_LINE ('L');
      riga_scrittura := buildScrittura(rDocumento,utente);
	  -- recupero Lista delle scadenze
      lListaScadenze := getScadenze(rDocumento);
	  -- Conta Scadenze
	  num_scadenze := lListaScadenze.count;
	  -- recupero Lista delle scadenze
      cSezione   := getSezione(rDocumento);

	  for contatore in 1..num_scadenze loop
	      rScadenze := lListaScadenze(contatore);
          rContoEp   := getContoEp(rScadenze);
		  if ( (UPPER(TRIM(rContoEp.NATURA_VOCE)) = 'EEC') OR
		       (UPPER(trim(rContoEp.NATURA_VOCE)) = 'EER') or
		       (UPPER(trim(rContoEp.RIEPILOGA_A)) ='CEC') )then
			 normalizza_importi(rScadenze,'user');
		  end if;
	  end loop;
	  if lMovimentiPlus.count >0 then
		  lListaMovimenti := preparaMovimentiCoan (rDocumento, rContoEp, cSezione, Utente);
--Dbms_Output.put_line ('nel 210 ');
		  CNRCTB200.CREASCRITTCOAN(riga_scrittura,lListaMovimenti);
	      if aOldScrittura.pg_scrittura is not null then
 	       if not CNRCTB200.isModificata(riga_scrittura,aOldScrittura) then
            rollback to savepoint SAVEPOINT_COAN00;
	       end if;
	      end if;
	   end if;

	 CNRCTB100.UPDATEDOCAMM_NODUVAUTUV(
	  aCd_Tipo_Documento_Amm,
	  aCd_Cds,
	  aEsercizio,
	  aCd_Unita_Organizzativa,
	  aPg_Numero_Documento,
	  'stato_coan='''||CNRCTB100.STATO_COEP_CON||'''',
	  null
	 );

 exception when NO_DATA_FOUND then
 		   IBMERR001.RAISE_ERR_GENERICO('Documento non risulta da contabilizzare COAN');
 end;

 /* Questa Funzione si occupa di impostare la SCRITTURA COAN Attiva associata
    ad un documento : Ritorna la riga della tabella SCRITTURA_ANALITICA
	associata alla scrittura */
 function buildScrittura(doc v_coan_documenti%rowtype, utente varchar2 )  return scrittura_analitica%rowtype is
   rScrittura scrittura_analitica%rowtype;
   vDate  DATE;
 begin
  	vDate := sysdate;
	rScrittura.CD_CDS := doc.CD_CDS_ORIGINE;
	rScrittura.ESERCIZIO := doc.ESERCIZIO;
	rScrittura.CD_UNITA_ORGANIZZATIVA := doc.CD_UO_ORIGINE;
	rScrittura.PG_SCRITTURA := NULL;
	rScrittura.ORIGINE_SCRITTURA := cnrctb200.ORIGINE_DOCUMENTO_AMM;
	rScrittura.CD_TERZO := doc.CD_TERZO;
	rScrittura.CD_CDS_DOCUMENTO := doc.CD_CDS;
	rScrittura.CD_UO_DOCUMENTO := doc.CD_UNITA_ORGANIZZATIVA;
	rScrittura.CD_TIPO_DOCUMENTO := doc.CD_TIPO_DOCUMENTO_AMM;
	rScrittura.PG_NUMERO_DOCUMENTO := doc.PG_NUMERO_DOCUMENTO ;
	rScrittura.CD_COMP_DOCUMENTO := NULL ;
	rScrittura.IM_SCRITTURA := 0;
	rScrittura.TI_SCRITTURA := CNRCTB200.TI_SCRITTURA_SINGOLA;
	rScrittura.DT_CONTABILIZZAZIONE := trunc(vDate) ;
	rScrittura.DT_CANCELLAZIONE := NULL;
	rScrittura.STATO := CNRCTB200.STATO_DEFINITIVO;
	rScrittura.DS_SCRITTURA := 'Scrittura Generata da ' || doc.CD_TIPO_DOCUMENTO_AMM || ' num :' || doc.PG_NUMERO_DOCUMENTO ;
	rScrittura.PG_SCRITTURA_ANNULLATA := NULL;
	rScrittura.ATTIVA := 'Y';
	rScrittura.ESERCIZIO_DOCUMENTO_AMM := doc.ESERCIZIO;
	rScrittura.DACR := vDate;
	rScrittura.UTCR := utente;
	return rScrittura;
 end;


 /* Questa Funzione si occupa di recuperare la Sezione associata
    ad un documento : Ritorna un char che identifica la sezione  DARE AVERE*/
 function getSezione(aDoc V_COAN_DOCUMENTI%rowtype ) return char is
  aSezione char(1);
  aTiGestioneEv CHAR(1);
 begin
    aSezione := CNRCTB100.GETSEZIONEECONOMICA(aDoc.cd_tipo_documento_amm);

	IF aDoc.cd_tipo_documento_amm = CNRCTB100.TI_FATTURA_PASSIVA THEN
		select DECODE(NVL(PG_OBBLIGAZIONE,-1),-1,CNRCTB001.GESTIONE_ENTRATE,CNRCTB001.GESTIONE_SPESE)
		INTO  aTiGestioneEv
		from FATTURA_PASSIVA_RIGA
		WHERE CD_CDS = aDoc.CD_CDS
		AND   CD_UNITA_ORGANIZZATIVA = aDoc.CD_UNITA_ORGANIZZATIVA
		AND   ESERCIZIO = aDoc.ESERCIZIO
		AND   PG_FATTURA_PASSIVA = aDoc.PG_NUMERO_DOCUMENTO
		AND ROWNUM = 1;
	ELSIF aDoc.cd_tipo_documento_amm = CNRCTB100.TI_FATTURA_ATTIVA THEN
		select DECODE(NVL(PG_OBBLIGAZIONE,-1),-1,CNRCTB001.GESTIONE_ENTRATE,CNRCTB001.GESTIONE_SPESE)
		INTO  aTiGestioneEv
		from FATTURA_ATTIVA_RIGA
		WHERE CD_CDS = aDoc.CD_CDS
		AND   CD_UNITA_ORGANIZZATIVA = aDoc.CD_UNITA_ORGANIZZATIVA
		AND   ESERCIZIO = aDoc.ESERCIZIO
		AND   PG_FATTURA_ATTIVA = aDoc.PG_NUMERO_DOCUMENTO
		AND ROWNUM = 1;
	ELSIF aDoc.cd_tipo_documento_amm = CNRCTB100.TI_COMPENSO THEN
		select decode(count(0),0,CNRCTB001.GESTIONE_ENTRATE,CNRCTB001.GESTIONE_SPESE)
		INTO  aTiGestioneEv
		from COMPENSO_RIGA
		WHERE CD_CDS = aDoc.CD_CDS
		AND   CD_UNITA_ORGANIZZATIVA = aDoc.CD_UNITA_ORGANIZZATIVA
		AND   ESERCIZIO = aDoc.ESERCIZIO
		AND   PG_COMPENSO = aDoc.PG_NUMERO_DOCUMENTO
		AND   ROWNUM = 1;
    ELSE
	  	aTiGestioneEv := ' ';
	END IF;

    if ( -- La nota di creadito su FATTURE va sempre in sezione economica opposta a quella del documento da cui deriva
	             aDoc.cd_tipo_documento_amm = CNRCTB100.TI_FATTURA_PASSIVA
		 	 and aDoc.ti_fattura = CNRCTB100.TI_FATT_NOTA_C
		--	 and aTiGestioneEv = CNRCTB001.GESTIONE_ENTRATE
	 	 or
		     	 aDoc.cd_tipo_documento_amm = CNRCTB100.TI_FATTURA_ATTIVA
			 and aDoc.ti_fattura = CNRCTB100.TI_FATT_NOTA_C
		--     and aTiGestioneEv = CNRCTB001.GESTIONE_SPESE
	 	 or
		     	 aDoc.cd_tipo_documento_amm = CNRCTB100.TI_COMPENSO
		     and aTiGestioneEv = CNRCTB001.GESTIONE_ENTRATE
	  )
    then
        aSezione:=CNRCTB200.getSezioneOpposta(aSezione);
    end if;
    RETURN aSezione;
 exception when NO_DATA_FOUND then
 		   IBMERR001.RAISE_ERR_GENERICO('Sezione economica non trovata');
 end;

 /* Questa Funzione si occupa di recuperare le Scadenze delle Obbligazioni associate
    ad un documento : Ritorna una lista di righe del tipo v_coan_scadenze ognuna conteneti
	informazioni sulla singola scadenza associata al documento */
 function getScadenze(aDoc V_COAN_DOCUMENTI%rowtype ) return tScadenze is
 i number;
 lScadenze tScadenze;
 begin
  i:=1;
  for aScad in
	  (select *
	  from v_coan_scadenze v
	  where v.CD_TIPO_DOCUMENTO_AMM = aDoc.CD_TIPO_DOCUMENTO_AMM
	  and   v.CD_CDS = aDoc.CD_CDS
	  and   v.CD_UNITA_ORGANIZZATIVA = aDoc.CD_UNITA_ORGANIZZATIVA
	  and   v.PG_NUMERO_DOCUMENTO = aDoc.PG_NUMERO_DOCUMENTO
	  and   v.ESERCIZIO = aDoc.ESERCIZIO) loop
	  		lScadenze(i) := aScad;
			i := i + 1;
	  end loop;
	  return lScadenze;
 exception
 when NO_DATA_FOUND then
 	     IBMERR001.RAISE_ERR_GENERICO('Nessuna Scadenza trovata');
 end;

 /* Questa Funzione si occupa di recuperare il Conto ECONOMICO PATRIMONIALE associato
    alla scadenza. Ritorna la riga della tabella voce_ep associata al conto in esame */
 function getContoEp(aDoc V_COAN_SCADENZE%rowtype) return voce_ep%rowtype is
  aAss ass_ev_voceep%rowtype;
  aVoceEp voce_ep%rowtype;
 begin
  begin
   select * into aAss from ass_ev_voceep where
        esercizio = aDoc.esercizio
    and ti_appartenenza = aDoc.ti_appartenenza
    and ti_gestione = aDoc.ti_gestione
    and cd_elemento_voce = aDoc.cd_elemento_voce;
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('Associazione tra voce del piano finanziaria ed economica non trovata per la Voce '||aDoc.esercizio||'/'||aDoc.ti_appartenenza||'/'||aDoc.ti_gestione||'/'||aDoc.cd_elemento_voce);
  end;
  begin
   select * into aVoceEp from voce_ep where
        esercizio = aDoc.esercizio
    and cd_voce_ep = aAss.cd_voce_ep;
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('Voce EP per scrittura economica non trovata');
  end;
  return aVoceEp;
 end;



  /* Questa Funzione si occupa calcolare la somma degli importi delle scadenze
     in esame, e distribuirlo sulle diverse linee di attivita: Restituisce la
	 lista delle linee di attivita con la giusta distribuzuione degli importi  */
procedure normalizza_importi (aScadenze V_COAN_SCADENZE%rowtype, aUser varchar2)
 is
 /* la vista v_coan_movimenti_plus contiene per ogni documento la distribuzione
    dello stesso sulle dicverse linee attivita :
	Data in input una scadenza viene, a parita di documento e di scadenza,
	estratta dalla vista la somma degli importi. SE questa somma coincide con
	l'importo totale della scadenza in esame allora la distribuzione degli importi
	sulle linee di attivita e corretta MENTRE se la somma non coincide con l'importo scdenza
	all'ultima lineaa di attivita viene assegnata la differenza */

--  rLA_su_Scad v_coan_movimenti_plus%rowtype;
  nImporto number(15,2);

  BEGIN
 		nImporto :=0;
 		FOR rLA_su_Scad IN (SELECT *
					        FROM V_COAN_MOVIMENTI_PLUS V
						    WHERE V.CD_TIPO_DOCUMENTO_AMM = aScadenze.CD_TIPO_DOCUMENTO_AMM
						      AND V.CD_TERZO = aScadenze.CD_TERZO
						      AND V.CD_CDS = aScadenze.CD_CDS
						      AND V.CD_UNITA_ORGANIZZATIVA = aScadenze.CD_UNITA_ORGANIZZATIVA
						      AND V.ESERCIZIO = aScadenze.ESERCIZIO
						      AND V.PG_NUMERO_DOCUMENTO = aScadenze.PG_NUMERO_DOCUMENTO
						      AND V.CD_CDS_OBB_ACC = aScadenze.CD_CDS_OBB_ACC
						      AND V.ESERCIZIO_ORI_OBB_ACC = aScadenze.ESERCIZIO_ORI_OBB_ACC
						      AND V.PG_OBB_ACC = aScadenze.PG_OBB_ACC
						      AND V.PG_OBB_ACC_SCADENZARIO = aScadenze.PG_OBB_ACC_SCADENZARIO
						      AND V.TI_APPARTENENZA = aScadenze.TI_APPARTENENZA
						      AND V.TI_GESTIONE = aScadenze.TI_GESTIONE
					          AND V.CD_ELEMENTO_VOCE = aScadenze.CD_ELEMENTO_VOCE
							 ) LOOP
 			nImporto :=nImporto + rLA_su_Scad.IM_PARZIALE;
    	    num_movimento_doc := num_movimento_doc + 1;
            lMovimentiPlus(num_movimento_doc) :=  rLA_su_Scad;
 		END LOOP;
 		IF num_movimento_doc!=0 and nImporto <> aScadenze.IM_TOTALE THEN
 			lMovimentiPlus(num_movimento_doc).IM_PARZIALE := aScadenze.IM_TOTALE - nImporto;
		END IF;
 END;

/* Questa funzione per ogni linea di attivita legata al documento genera un movimento COAN */
Function preparaMovimentiCoan ( documento v_coan_documenti%rowtype,
			        ContoEp voce_ep%rowtype,
			        sezione CHAR,
			        utente VARCHAR2) return cnrctb200.movAnalitList is
 i number:=1;
 fermo number:=1;
 conta_mov number :=1 ;
 lMovimenti cnrctb200.movAnalitList;

Begin
 For rMovimento in (select * from v_coan_movimenti v
                   Where v.CD_TIPO_DOCUMENTO_AMM = documento.CD_TIPO_DOCUMENTO_AMM
	     and   v.CD_CDS = documento.CD_CDS
	     and   v.ESERCIZIO = documento.ESERCIZIO
	     and   v.CD_UNITA_ORGANIZZATIVA = documento.CD_UNITA_ORGANIZZATIVA
	     and   v.PG_NUMERO_DOCUMENTO = documento.PG_NUMERO_DOCUMENTO  ) loop

	  fermo :=  lMovimentiPlus.count;
	  -- Fix per la valorizzazione dei movimenti coan
	  rMovimento.IM_MOVIMENTO_COAN := 0;

	  for i in 1..fermo loop
	  	if (  rMovimento.CD_TIPO_DOCUMENTO_AMM    = lMovimentiPlus(i).CD_TIPO_DOCUMENTO_AMM
		  and rMovimento.CD_TERZO 		  = lMovimentiPlus(i).CD_TERZO
		  and rMovimento.CD_CDS 		  = lMovimentiPlus(i).CD_CDS
		  and rMovimento.CD_UNITA_ORGANIZZATIVA   = lMovimentiPlus(i).CD_UNITA_ORGANIZZATIVA
		  and rMovimento.ESERCIZIO 		  = lMovimentiPlus(i).ESERCIZIO
		  and rMovimento.PG_NUMERO_DOCUMENTO 	  = lMovimentiPlus(i).PG_NUMERO_DOCUMENTO
		  and rMovimento.TI_APPARTENENZA 	  = lMovimentiPlus(i).TI_APPARTENENZA
		  and rMovimento.TI_GESTIONE 		  = lMovimentiPlus(i).TI_GESTIONE
		  and rMovimento.CD_ELEMENTO_VOCE 	  = lMovimentiPlus(i).CD_ELEMENTO_VOCE
		  and rMovimento.CD_CENTRO_RESPONSABILITA = lMovimentiPlus(i).CD_CENTRO_RESPONSABILITA
		  and rMovimento.CD_LINEA_ATTIVITA 	  = lMovimentiPlus(i).CD_LINEA_ATTIVITA
		  and rMovimento.CD_NATURA 		  = lMovimentiPlus(i).CD_NATURA
		  and (NVL(rMovimento.CD_FUNZIONE,'-1')   = NVL(lMovimentiPlus(i).CD_FUNZIONE,'-1') )
		    ) then
		  rMovimento.IM_MOVIMENTO_COAN := rMovimento.IM_MOVIMENTO_COAN + lMovimentiPlus(i).IM_PARZIALE;
	  	end if;
	  end loop;

       	  lMovimenti (conta_mov).CD_CDS := rMovimento.CD_CDS;
       	  lMovimenti (conta_mov).ESERCIZIO := rMovimento.ESERCIZIO;
       	  lMovimenti (conta_mov).CD_UNITA_ORGANIZZATIVA := rMovimento.CD_UNITA_ORGANIZZATIVA;
       	  lMovimenti (conta_mov).PG_SCRITTURA := NULL;
       	  lMovimenti (conta_mov).CD_VOCE_EP := ContoEp.CD_VOCE_EP;
       	  lMovimenti (conta_mov).PG_MOVIMENTO := NULL;
       	  lMovimenti (conta_mov).SEZIONE := sezione;
       	  lMovimenti (conta_mov).CD_CENTRO_RESPONSABILITA :=  rMovimento.CD_CENTRO_RESPONSABILITA;
       	  lMovimenti (conta_mov).IM_MOVIMENTO := rMovimento.IM_MOVIMENTO_COAN;
       	  lMovimenti (conta_mov).CD_TERZO := rMovimento.CD_TERZO;
       	  lMovimenti (conta_mov).CD_FUNZIONE := rMovimento.CD_FUNZIONE;
       	  lMovimenti (conta_mov).CD_NATURA := rMovimento.CD_NATURA;
       	  lMovimenti (conta_mov).STATO :='D';
       	  lMovimenti (conta_mov).ti_istituz_commerc := documento.ti_istituz_commerc;
       	  lMovimenti (conta_mov).DS_MOVIMENTO := 'Movimento Coan';
       	  lMovimenti (conta_mov).CD_LINEA_ATTIVITA :=  rMovimento.CD_LINEA_ATTIVITA;
       	  lMovimenti (conta_mov).PG_NUMERO_DOCUMENTO := documento.PG_NUMERO_DOCUMENTO;
       	  conta_mov :=conta_mov + 1 ;
 End loop;
 Return  lMovimenti;
 End;

end;
