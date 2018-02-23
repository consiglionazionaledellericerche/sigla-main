--------------------------------------------------------
--  DDL for Package CNRCTB210
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRCTB210" as
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
-- Modifica del tipo scrittura che da ultima è stata portata a Singola 'S'
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
-- Gestione del controllo di necessità di riemettere la scrittura su modifica
--
-- Date: 19/06/2003
-- Version: 1.26
-- Il documento amministrativo di origine non viene più modificato in DUVA e PG_VER_REC
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
-- Sistemato errore in regDocAmmCoan nel caso il documento sia già processato in COAN esce senza effettuare operazioni
-- Sistemazione recupero sezione corretta per note di credito passive su obbligazione e attive su accertamento
--
-- Date: 05/03/2004
-- Version: 1.36
-- Modificata controllo di non processabilità in invocazione regDocAmmCoan
--
-- Date: 24/09/2004
-- Version: 1.37
-- Richiets 843: I controlli sull'esercizio (finanziario ed economico) del documento in processo sono cambiati
-- La parte di controlli riguardante l'esercizio del documento (che è quello di registrazione economica) sono
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
-- La procedura si occupa di generare la contabilità analitica, per il documento amministrativo passato in input.
-- Tutti i documenti amministrativi che sono raccolti nella vista V_COAN_DOCUMENTI, sono da contabilizzare in COAN.
-- La generazione della contabilità analitica per un documento amministrativo, significa :
-- 1)	Costruzione o Recupero di una Scrittura (inserimento o recupero, di una riga nella tabella SCRITTURA_ANALITICA,
-- 2)	Recupero della Sezione Economica legata al documento.
-- 3)	Recupero di tutte le Scadenze legate al documento,
-- 4)	Recupero del Conto Economico Patrimoniale legato alla scadenza,
-- 5)	Eventuale distribuzione uniforme dell.importo del documento amministrativo sulle diverse linee di attività. La distribuzione dell.importo del documento deve essere fatta in base alle scadenze a cui risulta legato. Infatti ogni documento, e precisamente le sue righe, possono essere raggruppate in base a scadenze (di obbligazioni o di accertamenti).  Una volta raggruppate le righe del documento in base alle scadenze, e sommato i loro importi, otteniamo la distribuzione dell.importo del documento sulle diverse scadenze ad esso associate. Gli importi delle singole scadenze del documento vengono poi distribuiti sulle diverse LA in base alla percentuale stabilita. La distribuzione sulle LA rispetta la regola che l.ultima LA trattata nel calcolo della distribuzione eredita l.eventuale scarto accumulato durante i calcoli precedenti.
--
-- pre-post-name: Controlli su esercizio finanziario ed economico di registrazione del documento
-- pre: L'esercizio contabile in cui effettuare la liquidazione non risulta aperto o
--      chiuso finanziariamente o risulta in fase di chiusura economica o chiuso economicamente in modo definitivo
-- post: Viene sollevata un'eccezione
--
-- pre-post-name: Esercizio economico precedente non chiuso definitivamente
-- pre: L'esercizio economico precedente a quello di origine del documento non è chiuso definitivamente
-- post: Viene sollevata un'eccezione
--
-- pre-post-name: Documento riportato anche parzialmente a nuovo esercizio impedisce registrazione analitica
-- pre: Il documento amministrativo da processare è stato riportato a nuovo esercizio anche solo parzialmente
-- post: Viene sollevata un'eccezione
--
-- pre-post-name: Il documento deve essere presente nella vista V_COAN_DOCUMENTI, nella quale risultano aggregati tutti i documenti amministrativi per cui risulta che lo STATO_COAN<>.C..
-- pre: Il documento non appartiene alla vista
-- post: Viene sollevata un'eccezione
--
-- pre-post-name: Il documento deve avere almeno una scadenza (di obbligazione o di scadenzario) associata. A questo proposito è stata costruita una vista V_COAN_SCADENZE, che fornisce l.aggregazione per scadenze delle righe del documento amministrativo.
-- pre: Il documento no ha scadenze associate,  non risulta presente nella vista
-- post: Viene sollevata un'eccezione
--
-- pre-post-name: Il documento deve avere una sezione economica associata.
-- pre: Il documento no ha sezioni economiche, o ne risultano più sezioni economiche ad esso associate.
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
-- pre: Il documento risulta già essere associato ad una scrittura analitica.
-- post: La scrittura viene stornata, insieme a tutti i movimenti coan ad essa associati, e il documento viene ricontabilizzato.
--
-- pre-post-name: Una volta divise le righe del documento a gruppi, in base alla scadenza, si sommano gli importi delle righe appartenenti allo stesso gruppo. Questo importo viene distribuito sulle diverse LA con profondità di aggregazione fino a Codice Funzione e Codice Natura. Questa distribuzione e fatta tramite la vista V_COAN_MOVIMENTI_PLUS
-- pre: Per una scadenza legata al documento non risulta che la somma degli importi distribuiti tramite la vista V_COAN_MOVIMENTI_PLUS sia pari all.importo della scadenza.
-- post: All.ultima LA viene assegnato l.importo ad essa dovuta, più lo scarto derivante dalle operazioni di distribuzione precedenti.
--
-- Parametri:
-- aCd_Tipo_Documento_Amm -> Tipologia del documento amministrativo che si deve trattare (FATTURA_A, FATTURA_P  e tutti quelli presenti in 					         TIPO_DOCUMENTO_AMM )
-- aCd_Cds -> Codice dell'centro di spesa ha cui appartiene il documento in esame.
-- aCd_Unita_Organizzativa -> Codice dell'unità organizzativa ha cui appartiene il documento in esame.
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
