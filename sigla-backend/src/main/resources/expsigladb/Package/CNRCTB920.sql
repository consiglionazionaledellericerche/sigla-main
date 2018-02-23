--------------------------------------------------------
--  DDL for Package CNRCTB920
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRCTB920" AS
----------------------------------------------------------------------------------------------------
--
-- Elaborazione per stampa INPS. (Modello GLA).
-- l'Estrazione è fatta per U.O. ad eccezione della SAC dove si elabora l'intero CdS.
--
-- >>>>>>>>>> Date: 23/03/2004
-- >>>>>>>>>> Version: 1.4
--
-- Dependency: CNRCTB 015/020/575/890 IBMERR 001 IBMUTL 001
--
-- =================================================================================================
-- PARAMETRI IN INPUT
-- =================================================================================================
-- +--------------------------+--------------------------------------------------------------------+
-- |CAMPO                     |DESCRIZIONE                                                         |
-- +--------------------------+--------------------------------------------------------------------+
-- |inEsercizio               |Esercizio di riferimento                                            |
-- |inCdCdS                   |Identificativo del CdS di riferimento                               |
-- |inCdUo                    |Identificativo dell'unità organizzativa di riferimento              |
-- |inRepID                   |Progressivo identificativo estrazione                               |
-- |inMsgError                |Futura implementazione                                              |
-- |inUtente                  |Identificativo utente della connessione                             |
-- +--------------------------+--------------------------------------------------------------------+
--
--
-- =================================================================================================
-- CONTENUTO DELLA TABELLA PARAMETRI (parametri_tab)
-- =================================================================================================
-- +----------------------------+----------------------------------------------------+------------+
-- |VARIABILE                   |DESCRIZIONE                                         |OBBLIGATORIO|
-- +----------------------------+----------------------------------------------------+------------+
-- |parametri_tab( 1).stringa   |Descrizione unità organizzativa o Cds se si tratta  |     SI     |
-- |                            |di SAC                                              |            |
-- |parametri_tab( 1).intero    |Indicatore di UO SAC. Dominio:                      |     SI     |
-- |                            |  0 = SAC                                           |            |
-- |                            |  1 = Non SAC                                       |            |
-- |parametri_tab( 2).stringa   |Stato contabile liquidazione CORI                   |     SI     |
-- +----------------------------+----------------------------------------------------+------------+
--
----------------------------------------------------------------------------------------------------
--
-- History:
--
-- Date: 17/03/2004
-- Version: 1.0
--
-- Creazione package. Richiesta CINECA n. 772.
--
-- Date: 18/03/2004
-- Version: 1.1
--
-- Inserimento gestione accodamento stampa sullo spooler.
--
-- Date: 18/03/2004
-- Version: 1.2
--
-- Aggiunta documentazione pre post
--
-- Date: 22/03/2004
-- Version: 1.3
--
-- Fix errore CINECA. Non era gestito il mandato/reversale in esercizio successivo (liquidazione CORI
-- di gennaio) nella costruzione della matrice dei versamenti.
--
-- Date: 23/03/2004
-- Version: 1.4
--
-- Fix errore CINECA n. 804. Ordinamento per annomese emissione/trasmissione mandato/reversale compenso.
--
-- Date: 17/02/2005
-- Version: 1.5
-- Gestito errore nel caso in cui non ci sono dati da inserire nel GLA
-- Gestito errore nel caso in cui esistono scaglioni diversi per lo stesso compenso (modificato cnrctb550.pks)
-- Aggiunti gli OCCA
-- =================================================================================================
--
--
-- Constants:

   -- Costanti per tipo cori ente percipiente

   TICORIENTE CONSTANT CHAR(1) := 'E';
   TICORIPERCIP CONSTANT CHAR(1) := 'P';

   -- Costanti per tipo riga in stampa

   TIRIGA_DET CONSTANT CHAR(1) := 'N';
   TIRIGA_TOT CONSTANT CHAR(1) := 'T';

   -- Costante tipo log per batch

   IDTIPOLOG CONSTANT VARCHAR2(20) := 'ESTRAZIONE_INPS';

   -- Nome del report

   IDNOMESTAMPA CONSTANT VARCHAR2(1000) := '/docamm/docamm/estrazione_inps.jasper';

-- Variabili globali

   dataOdierna DATE;

   -- Definizione tabella PL/SQL di appoggio per parametri

   TYPE ParametriRec IS RECORD
       (
        stringa VARCHAR2(1000):=null,
        intero INTEGER:=0,
        numero NUMBER:=0
       );
   TYPE ParametriTab IS TABLE OF ParametriRec
        INDEX BY BINARY_INTEGER;
   parametri_tab ParametriTab;

   -- Definizione tabella PL/SQL di appoggio per versamenti

   TYPE versamentiRec IS RECORD
       (
        tDtVersamento DATE,
        tNumVersamenti NUMBER(5),
        tImVersato NUMBER(15,2),
        tImDovuto NUMBER(15,2)
       );

   TYPE versamentiTab IS TABLE OF versamentiRec
        INDEX BY BINARY_INTEGER;
   versamenti_tab versamentiTab;

   -- Definizione tabella PL/SQL di appoggio errori per estrazione INPS

   TYPE dsErroriRec IS RECORD
       (
        tStringaKey VARCHAR2(2000),
        tStringaErr VARCHAR2(2000)
       );

   TYPE dsErroriTab IS TABLE OF dsErroriRec
        INDEX BY BINARY_INTEGER;
   errori_tab dsErroriTab;

   -- Definizione di un cursore variabile

   TYPE GenericCurTyp IS REF CURSOR;

----------------------------------------------------------------------------------------------------
-- MAIN PROCEDURE
----------------------------------------------------------------------------------------------------

-- Main estrazione INPS
--
-- Main procedura di lancio estrazione INPS.
-- L'estrazione è fatta per singola unità organizzativa. Nel caso in cui tale UO sia di tipo SAC sono elaborate tutte
-- le unità organizzative del Cds SAC. Sono presi in considerazione i compensi liquidati ad altri creditori nell'esercizio
-- di riferimento. Si controlla il parametro di configurazione STATO_LIQUIDA_CORI per filtrare i soli compensi associati ad
-- un documento contabile che sia allo stato di emesso o trasmesso al cassiere.
-- I CORI attualmente elaborati sono INPSA, INPSB, INPSCil
--
-- pre-post-name: Controllo parametri in input.
-- pre:           Viene controllata la congruenza dei parametri in input alla procedura
-- post:          Viene sollevata un'eccezione.
--
-- pre-post-name: Controllo sequenza record ENTE --> PERCIPIENTE
-- pre:           Viene controllata la sequenza ENTE --> PERCIPIENTE nell'elaborazione dei CORI INPS oggetto dell'estrazione.
-- post:          Viene sollevata un'eccezione.
--
-- pre-post-name: Lancio estrazione INPS.
-- pre:           Viene eseguita, come job dinamico, una elaborazione di estrazione dati per la stampa della
--                dichiarazione INPS.
-- post:          Si procede all'attivazione della gestione batch per estrazione INPS.
--
--                E' controllata la congruenza dei parametri in input alla procedura
--                   (vedi pre-post: Controllo parametri in input).
--
--                E' valorizzata la matrice dei parametri di base alla esecuzione della procedura:
--                   Si memorizza la tipologia della UO, SAC o non SAC.
--                   Si memorizza la descrizione della UO.
--
--                      Se la UO è di tipo non SAC la descrizione è quella della UO in input.
--                      Se la UO è di tipo SAC la descrizione è quella del Cds SAC.
--
--                   Si memorizza il valore del parametro STATO_LIQUIDA_CORI per l'esercizio di elaborazione. Tale
--                   valore regola l'estrazione dei dati e la gestione delle date di riferimento e di pagamento dei
--                   compensi. (EME = documento contabile emesso, INV = documento contabile trasmesso al cassiere).
--
--                Si procede all'estrazione dei dati INPS.
--
--                   FASE 1. Si estraggono i dati dalla vista V_ESTRAI_DATI_INPS per portarli sulla tabella di
--                           appoggio ESTRAZIONE_INPS_DETT.
--                           Si elaborano tutti i compensi che risultano pagati nell'esecizio di riferimento.
--
--                              Se la UO è di tipo non SAC si elabora la singola unità organizzativa.
--                              Se la UO è di tipo SAC si elabora l'intero Cds SAC.
--
--                                 Se STATO_LIQUIDA_CORI = EME sono presi in considerazione tutti i compensi pagati.
--                                 Se STATO_LIQUIDA_CORI = INV il documento contabile associato deve essere anche in stato
--                                    trasmesso a cassiere.
--
--                           I dati ente e percipiente sono portati su di una unica riga controllando la sequenza
--                           ENTE --> PERCIPIENTE. (vedi pre-post: Controllo sequenza record ENTE --> PERCIPIENTE).
--
--                   FASE 2. I record recuperati dalla FASE 1 sono aggiornati delle informazioni relative ai versamenti
--
--                   FASE 3. Si compone la matrice dei versamenti da esporre in stampa solo per la parte relativa ai
--                           versamenti stessi.
--
--                           Gli importi di versamento sono aggregati per mese di riferimento. Regole di determinazione:
--                              Si prende in considerazione la data di emissione o di trasmissione del mandato/reversale
--                              di versamento in base al valore del parametro STATO_LIQUIDA_CORI.
--
--                                 Se i giorni della data di cui sopra sono > 15 il mese di riferimento è pari allo stesso mese.
--                                 altrimenti il mese di riferimento è quello precedente.
--                                 Si corregge ad 1 il mese di riferimento 12 derivante da una data riferimento <
--                                 del 16/01/esercizio di riferimento.
--                                 Si corregge a 12 il mese di riferimento derivante da una data maggiore del
--                                 31/12/esercizio di riferimento.
--
--                   FASE 4. Inserimento dati per stampa INPS - Tabella VP_STM_ESTRAZIONE_INPS.
--
--                           Si recuperano, dalle relative sequence, i progressivi di riferimento per:
--                              Progressivo stampa = PG_STAMPA
--                              Progressivo report generico = IBMSEQ00_STAMPA
--
--                           Si recuperano da ESTRAZIONE_INPS_DETT i compensi da elaborare ordinandoli per
--                           codice anagrafico, data emissione o trasmissione mandato in dipendenza del valore del
--                           parametro STATO_LIQUIDA_CORI, codice contributo ritenuta e data competenza da cpompenso.
--
--                           A rottura di codice anagrafico sono recuperate le informazioni relative da esporre nei dati
--                           del percipiente.
--
--                           Si memorizza ogni record letto nella vista VP_STM_ESTRAZIONE_INPS sezione dettaglio per
--                           percipiente.
--                           Si aggiornano i totali per anagrafico.
--                           Si completa la matrice dei versamenti con le informazioni relative al dovuto.
--
--                           Si scarica la matrice dei versamenti nella vista VP_STM_ESTRAZIONE_INPS sezione versamenti
--                           elaborando il relativo totale.
--
--                   FASE 5. Accodamento della stampa.
--
-- Parametri:
-- inEsercizio -> Esercizio di riferimento dell'elaborazione
-- inCdCdS -> Identificativo del CdS di riferimento
-- inCdUo -> Identificativo dell'unità organizzativa di riferimento
-- inRepID  -> Progressivo identificativo l'estrazione
-- inMsgError -> Futura implementazione gestione messaggi
-- inUtente -> Identificativo utente della connessione

   PROCEDURE estrazioneINPS
      (
       inEsercizio NUMBER,
       inCdCds VARCHAR2,
       inCdUo VARCHAR2,
       inRepID INTEGER,
       inMsgError IN OUT VARCHAR2,
       inUtente VARCHAR2
      );

-- Guscio per gestione estrazione INPS in batch

   PROCEDURE job_estrazioneINPS
      (
       job NUMBER,
       pg_exec NUMBER,
       next_date DATE,
       inEsercizio NUMBER,
       inCdCds VARCHAR2,
       inCdUo VARCHAR2,
       inRepID INTEGER,
       inMsgError VARCHAR2,
       inUtente VARCHAR2
      );

END CNRCTB920;
