--------------------------------------------------------
--  DDL for Package CNRCTB560
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRCTB560" AS
--==============================================================================
--
-- CNRCTB560 - Contabilizzazione finanziaria COMPENSI
--
-- Date: 14/07/2006
-- Version: 5.5
--
-- Dependency: CNRCTB 001/015/018/020/030/035/037/038/040/080/100/110/545 IBMERR 001 IBMUTL 001
--
-- History:
--
-- Date: 16/04/2002
-- Version: 1.0
-- Creazione
--
-- Date: 12/05/2002
-- Version: 1.1
-- Introduzione tipo documento generico CORI accantonamento entrata
--
-- Date: 15/05/2002
-- Version: 1.2
-- Recipero corretto dell'effetto COFI COEP
--
-- Date: 15/05/2002
-- Version: 1.3
-- Fix costanti CNRCTB100
--
-- Date: 29/05/2002
-- Version: 1.4
-- Aggiunta gestione im_ritenute in mandato collegato a compenso
--
-- Date: 09/06/2002
-- Version: 1.5
-- Fix errori su generazione reversali
--
-- Date: 10/06/2002
-- Version: 1.6
-- Liquidazione CORI negativi
--
-- Date: 11/06/2002
-- Version: 1.7
-- Sistemata la gestione dei CORI negativi (recupero del conto di spesa associato in p.giro da quello di entrata canonico)
--
-- Date: 20/06/2002
-- Version: 1.8
-- Fix generali su creazione documenti automatici
--
-- Date: 21/06/2002
-- Version: 1.9
-- Fix per test
--
-- Date: 28/06/2002
-- Version: 2.0
-- Fix messaggi di conto non trovato
--
-- Date: 01/07/2002
-- Version: 2.1
-- Il doc generico CORI non va in economica/analitica
--
-- Date: 02/07/2002
-- Version: 2.2
-- Gestione del compenso negativo
--
-- Date: 08/07/2002
-- Version: 2.3
-- Blocco su compensi associati a fondo economale
--
-- Date: 08/07/2002
-- Version: 2.3
-- Gestione della contabilizzazione di compenso attraverso il fondo economale
--
-- Date: 12/07/2002
-- Version: 2.5
-- Fix modalit�agamento e banca su generico CORI
--
-- Date: 17/07/2002
-- Version: 2.6
-- Aggiornata data emissione mandato in COMPENSO
--
-- Date: 17/07/2002
-- Version: 2.7
-- Sistemazione generazione pgiro di spesa
--
-- Date: 18/07/2002
-- Version: 2.8
-- Fix modailit�agamento generico e reversale CORI
-- Aggiornamento documentazione
--
-- Date: 30/07/2002
-- Version: 2.9
-- Fix gestione della contabilizzazione di compenso legato a missioni con anticipi
--
-- Date: 31/07/2002
-- Version: 3.0
-- Fix estrazione importo anticipo su missione
-- Se i CORI superano il mandato principale nel caso di compenso positivo, viene sollevata un'eccezione
--
-- Date: 04/09/2002
-- Version: 3.1
-- Fix generazione obb. pgiro cori negativi
--
-- Date: 09/09/2002
-- Version: 3.2
-- Fix gestione compensi con importo totale compenso negativo o nullo
--
-- Date: 12/09/2002
-- Version: 3.3
-- Fix estrazione numerazione mandati cori
--
-- Date: 13/09/2002
-- Version: 3.4
-- FIx aggiornamento CORI post collegamento a acc o obbligazione -> mancava ti_ente_percipiente in where cond
--
-- Date: 17/09/2002
-- Version: 3.5
-- Fix creazione generico su CORI negativi
--
-- Creazione Package.
--
-- Date: 18/09/2002
-- Version: 3.6
-- Introdotto il filtro per non contabilizzare cori di tipo IVA e RIVALSA
--
-- Date: 18/09/2002
-- Version: 3.7
-- Inserimento in tabella ASS_COMP_DOC_CONT_NMP per gestione associazione compenso senza mandato principale con suoi doc autorizz. CORI
-- Aggiornamento del compenso con informazioni relative a doc generico e accertamento generati al centro nel caso di importo negativo
--
-- Date: 19/09/2002
-- Version: 3.8
-- Fix su ASS_COMP_DOC_CONT_NMP
--
-- Date: 19/09/2002
-- Version: 3.9
-- Gestione linea di attivit�n generico di recupero crediti da terzi effettuato al centro
--
-- Date: 19/09/2002
-- Version: 4.0
-- Cds e Uo origine corretti in generico al centro su compesno negativo
--
-- Date: 20/09/2002
-- Version: 4.1
-- Fix creazione doc generico al centro
--
-- Date: 24/09/2002
-- Version: 4.2
-- Fix errore su totale CORI in mandato
--
-- Date: 15/10/2002
-- Version: 4.3
-- La data di emissione mandato viene impostata anche se non esiste il mandato principale del compenso
--
-- Date: 15/10/2002
-- Version: 4.4
-- Il generico creato al centro deve avere lo stato non associato a mandati e reversali
--
-- Date: 15/10/2002
-- Version: 4.5
-- Gestione del versamento dei CORI negativi di tipo ENTE al centro e non al titolare del compenso
--
-- Date: 18/10/2002
-- Version: 4.6
-- Creazione associazione reversale cori a compenso in interfaccia package
--
-- Date: 21/10/2002
-- Version: 4.7
-- Allineamento con correttiva version: 4.5.0
-- Richiesta CINECA del 18/10/2002 di utilizzare il terzo relativo all'UO del compenso per generico di recupero crediti da terzi
-- e non il terzo del compenso
--
-- Date: 23/10/2002
-- Version: 4.7
-- Allineamento con correttiva version: 4.5.1
-- Fix segnalazione su liquidazione compensi  provvisori (pg_negativo) segnalazione n.351
--
-- Date: 25/10/2002
-- Version: 4.8
-- Allineamento con correttiva version: 4.5.2
-- Richiesta CINECA del 18/10/2002 - completamento informazioni relative al terzo su generico di recupero crediti da terzi
--
-- Date: 10/11/2002
-- Version: 4.9
-- Sistemazione periodo di competenza in testata doc generico (fasato con quello delle righe)
--
-- Date: 14/01/2003
-- Version: 4.10
-- Fix liquidazione mandato con anticipo
-- Se c'�nticipo sulla missione, prepara l'importo del mandato con la differenza tra netto percipiente e anticipo se > 0 altrimenti 0
-- A questo importo verr�ommato (algebricamente) il totale dei CORI presenti sul compenso.
-- In questo modo se l'anticipo < del netto percipiente il mandato verr�iquidato per la somma dei cori + la differenza netto percipiente anticipo
-- Altrimenti il mandato verr�iquidato per il totale dei cori (netto percipiente - anticipo = 0)
--
-- Date: 04/02/2003
-- Version: 4.11
-- Importo ritenute mandato principale del compenso = somma dei CORI positivi
--
-- Date: 10/02/2003
-- Version: 4.12
-- contabilizzazione CORI solo per CORI con importo <> 0
--
-- Date: 15/05/2003
-- Version: 4.13
-- Nuova gestione della liquidazione di compenso con CORI positivi liquidati di importo superiore
-- a quello del mandato principale del compenso: vengono collegate al mandato principale SOLO
-- le reversali CORI ENTE mentre le reversali percipiente sono create scollegate
--
-- Date: 07/05/2003
-- Version: 5.0
-- Modifiche per gestione liquidazione compensi in chiusura esercizio
--
-- Date: 07/05/2003
-- Version: 5.1
-- Portato in interf. package il metodo di riempimento tabella ASS_COMP_DOC_CONT_NMP
-- per mandati
--
-- Date: 13/06/2003
-- Version: 5.2
-- Sistemazione date di registrazione documenti e controllo esercizio
--
-- Date: 15/07/2003
-- Version: 5.3
-- Fix errore impostazione dt_emissione_mandato su compenso
--
-- Date: 04/08/2003
-- Version: 5.4
-- Registrazione compenso via fondo in esercizio diverso da quello di origine del compenso
--
-- Date: 14/07/2006
-- Version: 5.5
-- Gestione Impegni/Accertamenti Residui:
-- aggiornata la funzione per tener conto anche del campo Esercizio Originale Impegno/Accertamento
--
-- Date: 22/04/2008
-- Version: 5.6
-- Gestione Sospensione netto nei compensi
-- Aggiunta alla procedura GENERADOCUMENTI la possibilit�i creare l'undicesima reversale
-- Aggiunta la procedura gestioneNoCori per gestire la nuova PGIRO e Reversale,
-- non da ritenuta, associata a compenso
--==============================================================================
--
-- Constants
--
ELEMENTO_VOCE_SPECIALE CONSTANT VARCHAR2(50):='ELEMENTO_VOCE_SPECIALE';
REC_CRED_DA_TERZI CONSTANT VARCHAR2(100):='RECUPERO_CREDITI_DA_TERZI';

TI_MANDATO CONSTANT CHAR(1):='M';
TI_REVERSALE CONSTANT CHAR(1):='R';

--
-- Functions e Procedures
--

-- Contabilizzazione finanziaria compensi
--
-- Contabilizzazione finanziaria dei compensi. I passaggi sono:
-- 1. lettura del compenso;
-- 2. lettura dei contributi ritenuta e generazione partite di giro;
-- 3. generazione mandato e reversali associate su partita di giro.
--
-- pre-post-name: Compenso non trovato
-- pre: il compenso, contabilizzato ed associato ad obbligazione, non viene trovato per CdS, esercizio, unit�rganizzativa, numero progressivo del documento
-- post: viene sollevata un'eccezione
--
-- pre-post-name: Obbligazione associata a compenso non trovata
-- pre: non viene trovata l'obbligazione specificata nel compenso per CdS, esercizio e numero progressivo del compenso
-- post: viene sollevata un'eccezione
--
-- pre-post-name: Scadenza dell'obbligazione non trovata
-- pre: non viene trovata la scadenza dell'obbligazione specificata nel compenso per CdS, esercizio, numero progressivo dell'obbligazione e numero progressivo dello scadenziario dell'obbligazione
-- post: viene sollevata un'eccezione
--
-- pre-post-name: Numero di contributi ritenuta superiore a dieci
-- pre: il numero di contributi ritenuta associati al compenso �uperiore a dieci
-- post: viene sollevata un'eccezione
--
-- pre-post-name: Gestione del singolo contributo ritenuta - conto economico non trovato
-- pre: non esiste un'associazione fra il contributo ritenuta e il conto economico
-- post: viene sollevata un'eccezione
--
-- pre-post-name: Gestione del singolo contributo ritenuta - conto finanziario non trovato
-- pre: non esiste un'associazione fra il contributo ritenuta e il conto finanziario
-- post: viene sollevata un'eccezione
--
-- pre-post-name: Gestione del singolo contributo ritenuta - non esiste un'associazione in partita di giro per il conto economico
-- pre: l'ammontare del contributo ritenuta �egativo, non esiste un conto associato in partita di giro al conto economico del compenso
-- post: viene sollevata un'eccezione
--
-- pre-post-name: Gestione del singolo contributo ritenuta
-- pre: il numero di contributi ritenuta associati al compenso �inore o uguale a dieci
-- post: Per ogni contributo ritenuta:
-- 1.	se l'ammontare associato al contributo ritenuta �ullo, non vengono generati documenti;
-- 2.	se l'ammontare associato al contributo ritenuta �ositivo, estrae il terzo associato all'unit�rganizzativa del compenso con le sue modalit�i pagamento di tipo bancario da ASS_TIPO_CORI_EV. Estrae il conto finanziario associato al conto economico patrimoniale relativo al compenso. Stabilisce l'effetto COFI e determina la partita di giro: genera un accertamento in partita di giro per l'importo del contributo ritenuta, valorizzandolo secondo i dati del compenso e del conto finanziario. Genera un documento generico d.accertamento contributi ritenuta d.entrata valorizzato dal compenso e dall'accertamento. Genera una reversale per l'importo del relativo accertamento. Aggiorna i saldi dello scadenziario dell'accertamento. Aggiorna il contributo ritenuta relativamente all'accertamento.
-- 3.	se l'ammontare associato al contributo ritenuta �egativo, estrae il terzo associato all'unit�rganizzativa del compenso con le sue modalit�i pagamento di tipo bancario da ASS_TIPO_CORI_EV. Estrae il conto finanziario associato al conto economico patrimoniale relativo al compenso. Stabilisce l'effetto COFI e determina la partita di giro: genera un'obbligazione in partita di giro per importo pari al valore assoluto dell'ammontare dei contributi con dettagli estratti da ASS_PARTITA_GIRO (per identificare i collegamenti tra i conti in partita di giro) e dal compenso. Genera un documento generico d.accertamento contributi ritenuta di spesa valorizzato dal compenso e dall'obbligazione. Genera un mandato per l'importo dell'obbligazione. Aggiorna i saldi dello scadenziario dell'obbligazione. Aggiorna il contributo ritenuta relativamente all'obbligazione.
--
-- pre-post-name: Gestione dei contributi ritenuta del compenso
-- pre: il numero di contributi ritenuta associati al compenso �inore o uguale a dieci
-- post: viene accumulato il totale dell.ammontare dei contributi. Per ogni contributo ritenuta, vedi pre-post .Gestione del singolo contributo ritenuta..
--
-- pre-post-name: Importo totale del compenso negativo - voce speciale di recupero crediti da terzi non trovata
-- pre: l'importo totale del compenso �egativo, non viene trovata la voce speciale di recupero crediti da terzi nella configurazione CNR.
-- post: viene sollevata un'eccezione
--
-- pre-post-name: Importo totale del compenso negativo - conto finanziario non trovato
-- pre: l'importo totale del compenso �egativo, non viene trovato l'articolo d.entrata CNR per il conto economico del compenso relativamente all'unit�rganizzativa
-- post: viene sollevata un'eccezione
--
-- pre-post-name: Importo totale del compenso negativo - linea di attivit�on specificata nel compenso
-- pre: l'importo totale del compenso �egativo, nel compenso non �pecificata la linea di attivit�er la creazione dell'accertamento
-- post: viene sollevata un'eccezione
--
-- pre-post-name: Importo totale del compenso negativo
-- pre: ha gestito i contributi ritenuta, l'importo totale del compenso �egativo
-- post: individua il conto finanziario avendo preventivamente trovato il conto economico attraverso la configurazione CNR per la voce speciale di recupero crediti da terzi. Genera un accertamento per importo pari al valore assoluto del compenso totale, avendo individuato l.unit�rganizzativa valida in esercizio. Genera un documento generico di tipo recupero crediti da terzi per l.importo dell.accertamento associato. Aggiorna per chiave primaria i compensi.
--
-- pre-post-name: Importo totale del compenso = 0 senza anticipi esistenti su missioni collegate al compenso
-- pre: ha gestito i contributi ritenuta, l'importo totale del compenso � 0
-- post: non genera il mandato principale
--
-- pre-post-name: Importo totale del compenso positivo senza anticipi esistenti su missioni collegate al compenso
-- pre: ha gestito i contributi ritenuta, l'importo totale del compenso �ositivo
-- post: genera un mandato principale di liquidazione compenso per l'importo totale del compenso stesso. Aggiorna per chiave primaria i compensi.
--
-- pre-post-name: Importo totale del compenso - anticipo su missione collegata a compenso minore o uguale a 0
-- pre: Esiste una missione collegata al compenso con anticipo tale che la differenza tra lordo compenso e anticipo �inore o uguale a 0
-- post: Non viene generato il mandato principale.
--
-- pre-post-name: Importo totale del compenso - anticipo su missione collegata a compenso maggiore di 0
-- pre: Esiste una missione collegata al compenso con anticipo tale che la differenza tra lordo compenso e anticipo �aggiore di 0
-- post: Viene generato il mandato principale per il netto della differenza tra lordo compenso e anticipo su missione.
--
-- pre-post-name: Emissione documenti
-- pre: sono stati generati i documenti accertamento/obbligazione e reversale/mandato
-- post: emette il mandato principale (se esiste, vedi pre-post 'Importo totale del compenso positivo'), ed emette i mandati e le reversali collegati al mandato principale.
--
-- Parametri:
-- inCDSCompenso -> CdS che emette il compenso
-- inUOCompenso -> Unit�rganizzativa associata al compenso
-- inEsercizio -> esercizio di riferimento per la pratica finanziaria
-- inEsercizioOrigine -> esercizio del compenso
-- inPgCompenso -> numero progressivo del compenso
-- aUser -> utente che effettua la modifica

   PROCEDURE contabilizzaCompensoCOFI
      (
       inCDSCompenso VARCHAR2,
       inUOCompenso VARCHAR2,
       inEsercizio NUMBER,
       inEsercizioOrigine NUMBER,
       inPgCompenso NUMBER,
	   aUser varchar2
      );

   PROCEDURE contabilizzaCompensoCOFIFondo
      (
       inCDSCompenso VARCHAR2,
       inUOCompenso VARCHAR2,
       inEsercizio NUMBER,
       inEsercizioCompenso NUMBER,
       inPgCompenso NUMBER,
	   aUser varchar2,
	   aManP IN OUT mandato%rowtype
      );

-- Contabilizzazione finanziaria di un compenso generale

-- Steps:
-- 1. lettura del compenso
-- 2. lettura CORI e generazione partite di giro
-- 3. generazione mandato e reversali associate su p.giro

-- inOrigine: O -> On Line F -> Fondo economale

   PROCEDURE contabilizzaCompensoCOFI
      (
	   inOrigine CHAR,
       inCDSCompenso VARCHAR2,
       inUOCompenso VARCHAR2,
       inEsercizio NUMBER,
       inEsercizioOrigine NUMBER,
       inPgCompenso NUMBER,
	   aUser varchar2,
	   aManP IN OUT mandato%rowtype
      );

 --	 Crea l'associazione tra compenso e reversale nel caso non esista mandato principale
 procedure addAssCompDocNMP(aCompenso compenso%rowtype,aRev reversale%rowtype,aRevRighe CNRCTB038.righeReversaleList);

 procedure addAssCompDocNMP(aCompenso compenso%rowtype,aMan mandato%rowtype, aManRighe CNRCTB038.righeMandatoList);

 -- Inserimento in tabella ASS_COMP_DOC_CONT_NMP
 procedure ins_ASS_COMP_DOC_CONT_NMP (aDest ASS_COMP_DOC_CONT_NMP%rowtype);

END;
