CREATE OR REPLACE package CNRCTB210 as
--
-- CNRCTB210 - Package COAN
-- Date: 14/07/2006
-- Version: 1.38
--
-- Package per la gestione delle scritture COAN
--
-- Dependency: CNRCTB 001/100/200 IBMERR 001
--
-- History:
--
-- Date: 14/03/2002
-- Version: 1.0
-- Creazione
--
-- Date: 29/03/2002
-- Version: 1.1
-- Inserimento controlli sul tipo di conto EP, ristrutturazione
--     del Main BUILDMOVIMENNTICOAN per la gestione della movimentazione
--     COAN all'interno del PACKAGE del COGE.
--
-- Date: 07/05/2002
-- Version: 1.2
-- Rioirganizzazione scrittura analitica con testata
--
-- Date: 08/05/2002
-- Version: 1.3
-- Riorganizzazione
--
-- Date: 16/05/2002
-- Version: 1.4
-- Eliminazione vecchio metodo di update dello stato del documento
--
-- Date: 16/05/2002
-- Version: 1.5
-- Snellita gestione dacr/utcr etc. in scritture e movimenti
--
-- Date: 28/05/2002
-- Version: 1.6
-- Aggiunti UO e CDS origine del documento amministrativo in testata della scrittura
--
-- Date: 06/06/2002
-- Version: 1.7
-- Rimozione della gestione degli errori tramite la WHEN OTHERS
--
-- Date: 10/06/2002
-- Version: 1.8
-- Fix per recuperare correttamente le SEZAIONE assocata ad un documento amministrativo:
--
-- Date: 10/06/2002
-- Version: 1.9
-- Fix : Introduzione della relazione tra documento amm e (obbligazione o accertamento)
--
-- Date: 11/06/2002
-- Version: 1.10
-- Fix : Trattamento del recupero della sezione nei casi in cui non si trattano fatture
--
-- Date: 14/06/2002
-- Version: 1.11
-- Fix : Nuova gestione errori su sezione non trovata, o documento non presente in V_COAN_DOCUMENTI
--
-- Date: 14/06/2002
-- Version: 1.12
-- Fix : Gestione dei movimenti associati solo a conti EEC
--
-- Date: 19/06/2002
-- Version: 1.13
-- Fix su generazione dei movimenti: azzerava scorrettamente l'importo base del documento
--
-- Date: 24/06/2002
-- Version: 1.14
-- Fix per il calcolo degli importi relativi ai movimenti COAN
--
-- Date: 25/06/2002
-- Version: 1.15
-- Introduzione in COAN dei conti con Natura voce = EER
--
-- Date: 27/06/2002
-- Version: 1.16
-- Modifica del tipo scrittura che da ultima ? stata portata a Singola 'S'
--
-- Date: 27/06/2002
-- Version: 1.17
-- Modifica della condizione di WHERE nella funzione di storno_scrittura,
-- dove mancava il numero del documento.
--
-- Date: 08/07/2002
-- Version: 1.18
-- Modificata la condizuione di where sulle ricerche della scrittura analitica.
-- Ora la scrittura viene ricercata in base alle seguenti condizioni :
-- scrittur_analitica.cd_cds                 = cd_cds_origine
-- scrittur_analitica.esercizio				 = esercizio
-- scrittur_analitica.cd_uoita_organizzativa = cd_uo_origine
-- scrittur_analitica.pg_numero_documento    = pg_documento
-- scrittur_analitica.attiva                 = 'Y''
--
-- Date: 09/07/2002
-- Version: 1.19
-- Eliminazione delle varibili locale rLA_su_Scad
--
-- Date: 16/07/2002
-- Version: 1.20
-- Asseganzione della sysdata alla data di contabilizzazione della scrittura
--
-- Date: 16/07/2002
-- Version: 1.21
-- Asseganzione della trunc(sysdata) alla data di contabilizzazione della scrittura
--
-- Date: 18/07/2002
-- Version: 1.22
-- Aggiornamento della documentazione
--
-- Date: 18/10/2002
-- Version: 1.23
-- Aggiunto nel metodo di contabilizzazione l'aggiornamento dello stato coan del documento amministrativo
--
-- Date: 18/11/2002
-- Version: 1.24
-- Riorganizzazione package CNRCTB205
--
-- Date: 16/06/2003
-- Version: 1.25
-- Errore in estrazione delle scritture analitiche per documento aministrativo
-- Gestione del controllo di necessit? di riemettere la scrittura su modifica
--
-- Date: 19/06/2003
-- Version: 1.26
-- Il documento amministrativo di origine non viene pi? modificato in DUVA e PG_VER_REC
--
-- Date: 05/08/2003
-- Version: 1.27
-- Filtro di eslusione COAN per documenti GENERICI di trasferimento e apertura fondo economale
--
-- Date: 08/08/2003
-- Version: 1.28
-- Eliminato da analitica il generico di entrata per IVA
--
-- Date: 28/08/2003
-- Version: 1.29
-- Aggiunti controlli sullo stato dell'esercizio e blocco registrazione su documenti riportati a nuovo anno
--
-- Date: 09/09/2003
-- Version: 1.30
-- Fix su determinazione sezione principale del movimento su compenso con importo totale del compenso < 0
--
-- Date: 23/09/2003
-- Version: 1.31
-- Errore 639. I compensi senza costo principale (im_totale_compenso=0) sono messi a contabilizzato COAN alla prima esecuzione
-- del batch di economica/analitica. In questo modo se per caso per modifiche sul compenso viene reintrodotto il costo
-- principale, il documento viene riprocessato correttamente.
--
-- Date: 13/11/2003
-- Version: 1.32
-- L'esercizio economico precedente a quello in processo deve essere chiuso definitivamente
--
-- Date: 09/01/2004
-- Version: 1.33
-- Introduzione interfaccia per chiamata diretta regDocAmmCoan
--
-- Date: 27/01/2004
-- Version: 1.34
-- Gestione corretta effetti annullamento documento su scrittura analitica
--
-- Date: 28/01/2004
-- Version: 1.35
-- Sistemato errore in regDocAmmCoan nel caso il documento sia gi? processato in COAN esce senza effettuare operazioni
-- Sistemazione recupero sezione corretta per note di credito passive su obbligazione e attive su accertamento
--
-- Date: 05/03/2004
-- Version: 1.36
-- Modificata controllo di non processabilit? in invocazione regDocAmmCoan
--
-- Date: 24/09/2004
-- Version: 1.37
-- Richiets 843: I controlli sull'esercizio (finanziario ed economico) del documento in processo sono cambiati
-- La parte di controlli riguardante l'esercizio del documento (che ? quello di registrazione economica) sono
-- stati spostati nel metodo: CNRCTB204.checkChiusuraEsercizio
--
-- Date: 14/07/2006
-- Version: 1.38
-- Gestione Impegni/Accertamenti Residui:
-- aggiornata la funzione per tener conto anche del campo Esercizio Originale Impegno/Accertamento
--
-- Constants:

-- tMovimentiPlus risulta essere una tabella logica che collezione la distribuzione degli importi
-- divisa su tutte le scdenze obbligazioni, sulle diverse linee attivita legate ad un documento e .
type tMovimentiPlus  is table of V_COAN_MOVIMENTI_PLUS%rowtype index by binary_integer;
type tScadenze is table of v_coan_scadenze%rowtype index by binary_integer;

-- Tabelle Logiche
lMovimentiPlus tMovimentiPlus;

--lListaScritture cnrctb200.scrAnalitList;

-- variabili
num_movimento_doc NUMBER;

-- Functions e Procedures:

-- Scrittura analitica (COAN)
--
-- La procedura si occupa di generare la contabilit? analitica, per il documento amministrativo passato in input.
-- Tutti i documenti amministrativi che sono raccolti nella vista V_COAN_DOCUMENTI, sono da contabilizzare in COAN.
-- La generazione della contabilit? analitica per un documento amministrativo, significa :
-- 1)	Costruzione o Recupero di una Scrittura (inserimento o recupero, di una riga nella tabella SCRITTURA_ANALITICA,
-- 2)	Recupero della Sezione Economica legata al documento.
-- 3)	Recupero di tutte le Scadenze legate al documento,
-- 4)	Recupero del Conto Economico Patrimoniale legato alla scadenza,
-- 5)	Eventuale distribuzione uniforme dell.importo del documento amministrativo sulle diverse linee di attivit?. La distribuzione dell.importo del documento deve essere fatta in base alle scadenze a cui risulta legato. Infatti ogni documento, e precisamente le sue righe, possono essere raggruppate in base a scadenze (di obbligazioni o di accertamenti).  Una volta raggruppate le righe del documento in base alle scadenze, e sommato i loro importi, otteniamo la distribuzione dell.importo del documento sulle diverse scadenze ad esso associate. Gli importi delle singole scadenze del documento vengono poi distribuiti sulle diverse LA in base alla percentuale stabilita. La distribuzione sulle LA rispetta la regola che l.ultima LA trattata nel calcolo della distribuzione eredita l.eventuale scarto accumulato durante i calcoli precedenti.
--
-- pre-post-name: Controlli su esercizio finanziario ed economico di registrazione del documento
-- pre: L'esercizio contabile in cui effettuare la liquidazione non risulta aperto o
--      chiuso finanziariamente o risulta in fase di chiusura economica o chiuso economicamente in modo definitivo
-- post: Viene sollevata un'eccezione
--
-- pre-post-name: Esercizio economico precedente non chiuso definitivamente
-- pre: L'esercizio economico precedente a quello di origine del documento non ? chiuso definitivamente
-- post: Viene sollevata un'eccezione
--
-- pre-post-name: Documento riportato anche parzialmente a nuovo esercizio impedisce registrazione analitica
-- pre: Il documento amministrativo da processare ? stato riportato a nuovo esercizio anche solo parzialmente
-- post: Viene sollevata un'eccezione
--
-- pre-post-name: Il documento deve essere presente nella vista V_COAN_DOCUMENTI, nella quale risultano aggregati tutti i documenti amministrativi per cui risulta che lo STATO_COAN<>.C..
-- pre: Il documento non appartiene alla vista
-- post: Viene sollevata un'eccezione
--
-- pre-post-name: Il documento deve avere almeno una scadenza (di obbligazione o di scadenzario) associata. A questo proposito ? stata costruita una vista V_COAN_SCADENZE, che fornisce l.aggregazione per scadenze delle righe del documento amministrativo.
-- pre: Il documento no ha scadenze associate,  non risulta presente nella vista
-- post: Viene sollevata un'eccezione
--
-- pre-post-name: Il documento deve avere una sezione economica associata.
-- pre: Il documento no ha sezioni economiche, o ne risultano pi? sezioni economiche ad esso associate.
-- post: Viene sollevata un'eccezione
--
-- pre-post-name: Il documento deve avere una associazione valida tra una voce del piano finanziario e voce economica patrimoniale.
-- pre: Il documento non ha associazioni valide.
-- post: Viene sollevata un'eccezione
--
-- pre-post-name: Vengono trattati solo importi delle righe del documento che risultano associati (tramite la scadenza) a conti economici patrimoniali per cui risulta che NATURA_VOCE <> .EEC. .
-- pre: Il documento non risulta essere associato a nessun conto economico patrimoniale con Natura .EEC..
-- post: Non viene riportato in COAN
--
-- pre-post-name: Il documento possiede sempre una sola scrittura analitica associata.
-- pre: Il documento risulta gi? essere associato ad una scrittura analitica.
-- post: La scrittura viene stornata, insieme a tutti i movimenti coan ad essa associati, e il documento viene ricontabilizzato.
--
-- pre-post-name: Una volta divise le righe del documento a gruppi, in base alla scadenza, si sommano gli importi delle righe appartenenti allo stesso gruppo. Questo importo viene distribuito sulle diverse LA con profondit? di aggregazione fino a Codice Funzione e Codice Natura. Questa distribuzione e fatta tramite la vista V_COAN_MOVIMENTI_PLUS
-- pre: Per una scadenza legata al documento non risulta che la somma degli importi distribuiti tramite la vista V_COAN_MOVIMENTI_PLUS sia pari all.importo della scadenza.
-- post: All.ultima LA viene assegnato l.importo ad essa dovuta, pi? lo scarto derivante dalle operazioni di distribuzione precedenti.
--
-- Parametri:
-- aCd_Tipo_Documento_Amm -> Tipologia del documento amministrativo che si deve trattare (FATTURA_A, FATTURA_P  e tutti quelli presenti in 					         TIPO_DOCUMENTO_AMM )
-- aCd_Cds -> Codice dell'centro di spesa ha cui appartiene il documento in esame.
-- aCd_Unita_Organizzativa -> Codice dell'unit? organizzativa ha cui appartiene il documento in esame.
-- aEsercizio -> Anno di esercizio in relativo al documento in esame.
-- aPg_Numero_Documento -> progressivo di identificazione del documento in esame.
-- Utente -> Utente che ha lanciato la procedura di contabilizzazione.

 procedure regDocAmmCoan(aEs number, aCds varchar2, aUO varchar2, aTiDocumento varchar2, aPgDocAmm number, aUser varchar2, aTSNow date);

 procedure buildMovimentiCoan(aCd_Tipo_Documento_Amm V_COAN_DOCUMENTI.CD_TIPO_DOCUMENTO_AMM%TYPE,
 		   					  aCd_Cds V_COAN_DOCUMENTI.CD_CDS%TYPE,
 		   					  aCd_Unita_Organizzativa V_COAN_DOCUMENTI.CD_UNITA_ORGANIZZATIVA%TYPE,
 		   					  aEsercizio V_COAN_DOCUMENTI.ESERCIZIO%TYPE,
 		   					  aPg_Numero_Documento V_COAN_DOCUMENTI.PG_NUMERO_DOCUMENTO%TYPE,
							  utente varchar2
 							  );

-- Ritorna la scrittura attiva associata al documento in esame
 function buildScrittura(doc v_coan_documenti%rowtype, utente varchar2 )  return scrittura_analitica%rowtype;

-- Ritorna la Sezione (Dare o Avere)
 function getSezione(aDoc V_COAN_DOCUMENTI%rowtype ) return char;

-- Ritorna la lista delle scadenze associate ad un Documento
  function getScadenze(aDoc V_COAN_DOCUMENTI%rowtype ) return tScadenze;

-- Ritorna il conto Economico Patrimoniale associato al documento in esame
 function getContoEp(aDoc V_COAN_SCADENZE%rowtype) return voce_ep%rowtype;

-- Ritorna la lista dei movimenti coan con gli importi distribuiti uniformemente sulle
-- LA tenemdo conto delle diverse scadenze dato un documento.
 procedure normalizza_importi (aScadenze V_COAN_SCADENZE%rowtype, aUser varchar2) ;

-- Ricalcola la distribuzione degli importi sulle diverse LA, NON tenendo conto delle
-- diverse scadenze legate al documento in esame.
 function preparaMovimentiCoan (documento v_coan_documenti%rowtype,
		  					   ContoEp voce_ep%rowtype,
							   sezione CHAR,
							   utente varchar2
							   ) return cnrctb200.movAnalitList;


end;


CREATE OR REPLACE package body CNRCTB210 is

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

        -- Se trovo che il documento ? da processare o riprocessare in COAN eseguo il reprocessing altrimenti esco
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
       IBMERR001.RAISE_ERR_GENERICO('L''esercizio economico precedente non ? chiuso definitivamente per il cds: '||rDocumento.cd_cds_origine);
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

      -- Se il documento ? riportato anche parzialmente non pu? essere processato in analitica
--Dbms_Output.PUT_LINE ('F');
      if
       CNRCTB105.isRiportato(rDocumento.cd_cds,rDocumento.cd_unita_organizzativa, rDocumento.esercizio,
	                         rDocumento.pg_numero_documento, rDocumento.cd_tipo_documento_amm
							) = 'Y'
      then
       IBMERR001.RAISE_ERR_GENERICO('Il documento riportato (anche parzialmente) non pu? essere processato automaticamente in analitica');
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
		select DECODE(NVL(PG_OBBLIGAZIONE,-1),-1,CNRCTB001.GESTIONE_ENTRATE,CNRCTB001.GESTIONE_SPESE)
		INTO  aTiGestioneEv
		from COMPENSO
		WHERE CD_CDS = aDoc.CD_CDS
		AND   CD_UNITA_ORGANIZZATIVA = aDoc.CD_UNITA_ORGANIZZATIVA
		AND   ESERCIZIO = aDoc.ESERCIZIO
		AND   PG_COMPENSO = aDoc.PG_NUMERO_DOCUMENTO
		AND ROWNUM = 1;
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


