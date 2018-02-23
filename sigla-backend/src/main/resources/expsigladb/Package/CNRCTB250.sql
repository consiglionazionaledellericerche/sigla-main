--------------------------------------------------------
--  DDL for Package CNRCTB250
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRCTB250" AS
--==================================================================================================
--
-- CNRCTB250 - Main per gestione report IVA e liquidazione IVA
--
-- Date: 27/02/2004
-- Version: 4.18
--
-- Dependency: CNRCTB 001/015/080/100/120/255/260/265/270 IBMERR 001 IBMUTL 001/205/210
--
-- History:
--
-- Date: 07/06/2002
-- Version: 1.0
-- Creazione
--
-- Date: 25/06/2002
-- Version: 1.1
-- Eliminazione del parametro aTiFattura
--
-- Date: 25/06/2002
-- Version: 1.2
-- Eliminazione parametri prorata
--
-- Date: 26/06/2002
-- Version: 1.3
-- Gestione formattata dei messaggi di errore :
-- CODICE ERRORE - DESCRIZIONE ERRORE.
-- Il codice errore ט seguito da spazio trattino spazio, poi dalla descrizione,
-- che a sua volta ט seguita dal punto.
--
-- Date: 03/07/2002
-- Version: 1.4
-- Introdotta gestione dei sezionali multipli per la Liquidazione IVA
--
-- Date: 04/07/2002
-- Version: 1.5
-- Modifice per il calcolo della liquidazione IVA
--
-- Date: 04/07/2002
-- Version: 1.6
-- Inserimento chiamata procedura di contabilizzazione
--
-- Date: 04/07/2002
-- Version: 1.7
-- Fix 1 per calcolo Liquidazione_iva
--
-- Date: 08/07/2002
-- Version: 1.8
-- Fix 2 per calcolo Liquidazione_iva con Unita Organizzativa
--
-- Date: 11/07/2002
-- Version: 1.9
-- Introdotta gestione separata per il calcolo della liquidazioneiva per CNR e sui CDS
--
-- Date: 11/07/2002
-- Version: 1.10
-- Gestione standard del error handling
--
-- Date: 15/07/2002
-- Version: 1.11
-- Introduzione del warning di stampa vuota.
--
-- Date: 17/07/2002
-- Version: 1.12
-- Modifica controlo dei registri in stato definitivo, per la liquidazione IVA.
--
-- Date: 18/07/2002
-- Version: 1.13
-- Introduzione nuova gestione per il trottamento di errore non bloccante,
-- tramite il parametro IN UOT della procedura STAMPE IVA,
-- aggiunta funzione di conferma registro
-- aggiunta funzione annulla registro
--
-- Date: 18/07/2002
-- Version: 1.14
-- FIX SULLA PROCEDURA DI ANNULLAMENTO E CONFERMA REGISTRI (MODIFICATA WHERE CONDITION)
--
-- Date: 22/07/2002
-- Version: 1.15
-- Fix sulla descrizione dell'errore per registro non stampato in modo definitivo
--
-- Date: 23/07/2002
-- Version: 1.16
-- Modifica calcolo importo debito credito della liquidazione precedente
--
-- Date: 23/07/2002
-- Version: 1.17
-- Inserimento controllo chiamata alla procedura di contabilizzazione CNRCTB270
--
-- Date: 25/07/2002
-- Version: 1.19
-- Aggiunto controllo prima di creare registri definitivi vuoti
--
-- Date: 26/08/2002
-- Version: 1.20
-- Aggiunto identificativo dell'utente come parametro di input della procedura STAMPE IVA
--
-- Date: 29/08/2002
-- Version: 1.21
-- Modifica del msg di errore, riguardo la creazione del registro iva
--
-- Date: 30/08/2002
-- Version: 1.23
-- Modificato controllo per la stampa dei registri riepilogativi.
--
-- Date: 30/08/2002
-- Version: 1.23
-- Introdotta la stampa per i registri riepilogativi su tutti i sezionali di vendita o di acquisto.
--
-- Date: 02/09/2002
-- Version: 1.24
-- Corretta la gestione per la stampa dei registri iva e riepilogativi, differenziati,
-- per acquisti e vendite
--
-- Date: 05/09/2002
-- Version: 1.25
-- Rimosso controllo sulla generazione dei registri riepilogativi.
--
-- Date: 09/09/2002
-- Version: 1.26
-- Fix sulla chiamata alla function InsReportStatoSenzaFatt
--
-- Date: 12/09/2002
-- Version: 1.27
-- Fix sul controllo dei registri per la liquidazione IVA definitiva.
-- eliminata costante TI_REGISTRO
--
-- Date: 19/09/2002
-- Version: 2.0
-- Ristrutturazione del package, eliminazione delle variabili globali, conversione
-- delle function in procedure.
--
-- Date: 20/09/2002
-- Version: 2.1
-- Inserito il parametro di input della procedura STAMPEIVA (LOGID)
--
-- Date: 23/09/2002
-- Version: 2.2
-- Corretto il controllo sulla liquidazione IVA per i registri del mese in corso
--
-- Date: 25/09/2002
-- Version: 2.3
-- Modificata la chiamata alla procedura di inserimento fatture per registri.
--
-- Date: 11/11/2002
-- Version: 2.4
-- Inserita protocollazione Autofatture in stampa registri vendite.
--
-- Date: 14/11/2002
-- Version: 2.5
--
-- Fix errore in protocollazione fatture per cui sono incluse anche quelle annullate
-- Revisione completa della procedura di protocollazione documenti in quanto non teneva conto che
-- l'assegnazione dei protocolli deve operare su documenti in ordine di data (registrazione) e
-- progressivo identificativo.
-- Modifica viste per stampa registri
-- Introdotta la gestione di stampa anche dei sezionali di tipo istituzionale
-- Modifica della sintassi usata e reintrodotto il recupero della descrizione del sezionale e del
-- suo riferimento istituzionale e commerciale in sede di valorizzaParametri. Corretto l'assenza del
-- filtro per valorizzare sulle matrici acquisti e vendite i soli sezionali di acquisto o vendita.
-- Eliminato il filtro per non memorizzare sull'array i sezionali non movimentati, ora questi sono
-- memorizzati ma con l'indicazione di registro vuoto
--
-- Date: 09/12/2002
-- Version: 2.6
--
-- Revisione completa per centralizzare i controlli di ammissibilitא delle stampe e correzione degli
-- stessi.
--
-- Date: 10/12/2002
-- Version: 2.7
--
-- Revisione per modifica alla tabella REPORT_DETTAGLIO
--
-- Date: 11/12/2002
-- Version: 2.8
--
-- Modifica costanti per identificare in tabella REPORT_STATO le diverse tipologie di elaborazioni IVA.
-- Introdotto il controllo per la ristampa in caso di elaborazioni provvisorie fatte nel periodo in
-- cui una data elaborazione risulta definitiva
-- Attivata la stampa riepilogativi
--
-- Date: 12/12/2002
-- Version: 2.9
--
-- Date: 16/12/2002
-- Version: 3.0
--
-- Revisone complessiva della procedura
-- In tutte le stampe esposto, in report generico, come ultimi 3 campi le seguenti informazioni:
-- - tipoReport   -> P = Provvisorio D = Definitivo
-- - tipoRegistro   -> A = Acquisti V = Vendite
-- - descrizione gruppo di stampa
--
-- Date: 17/12/2002
-- Version: 3.1
--
-- Allineamento vista alla nuova struttura della tabella REPORT_GENERICO
-- Fix errore di intestazione della stampa registro senza spazio di separazione tra i due campi.
-- Fix errore in elaborazione della liquidazione, errato controllo ammissibilitא alla stampa.
--
-- Date: 18/12/2002
-- Version: 3.2
--
-- Inserita gestione sui gruppi di sezionali in elaborazione per la gestione dei riepilogativi.
-- Spostata gestione dell'inserimento in tabella REPORT_STATO nel package CNRCTB255.
-- Inserita la memorizzazione in REPORT_STATO per i riepilogativi
-- Corretto recupero e inserimento della REPORT_STATO per stampa riepilogativo per UO
--
-- Date: 19/12/2002
-- Version: 3.3
--
-- Fix errori in stampa iva differita
-- Fix errore interno 3057 per cui veniva inserito un record nella tabella REPORT_STATO anche in caso
-- di ristampa. (CHIAVE DUPLICATA)
--
-- Date: 08/01/2003
-- Version: 3.4
--
-- Fix errore interno 3094. Era inserito un record in REPORT_STATO anche in caso di stampa provvisoria
-- se questa era vuota.
-- Fix errore interno 3095. Era inserito un record in REPORT_STATO anche in caso di stampa provvisoria
-- dei riepilogativi.
-- Inizio aggiornamento documentazione.
--
-- Date: 15/01/2003
-- Version: 3.5
--
-- Inserita la gestione differenziata della LIQUIDAZIONE_IVA per attivitא commerciali ed istituzionali
-- (San Marino senza IVA e Intra_ue).
--
-- Date: 17/01/2003
-- Version: 3.6
--
-- Fix errore interno 3161, eliminata la gestione del controllo della liquidazione precedente in quanto
-- inutile.
-- Fix errore CINECA (manca segnalazione) non estraeva i dati delle autofattute in LIQUIDAZIONE iva
-- Inserita la gestione differenziata della LIQUIDAZIONE_IVA per attivitא commerciali ed istituzionali
-- (San Marino senza IVA e Intra_ue) anche in sede di inserimento ed aggiornamento della tabella
-- LIQUIDAZIONE_IVA
--
-- Date: 22/01/2003
-- Version: 3.7
--
-- Fix errore interno 3185, inserita gestione report vuoto in stampa esigibilitא differita
-- Fix errore interno 3186, inserita gestione report stato in caso di stampa definitiva per l'iva differita
--
-- Date: 30/01/2003
-- Version: 3.8
--
-- Aggiunti tipi di liquidazione IVA
--
-- Date: 30/01/2003
-- Version: 3.9
--
-- Liquidazione finanziaria iva solo per IVA commerciale
--
-- Date: 31/01/2003
-- Version: 3.10
--
-- Fix errore interno 3235. Il controllo, in stampa definitiva dei registri, che siano state fatte
-- liquidazioni per il periodo precedente non esportava il parametro della tipologia di liquidazione
-- (Commerciale, Istituzionale San Marino senza IVA o Istituzionale Intra UE) nel controllo.
-- Inserito il controllo di non attivare il controllo sulla stampa delle liquidazioni per il periodo
-- precedente in caso di stampa registri definitivi istituzionali
-- Verificato funzionamento del client e revisione della modalitא di aggiornamento della liquidazione
--
-- Date: 03/02/2003
-- Version: 3.11
--
-- Controllo definitivo gestione corretta dell'id_report per liquidazione definitiva (id = 0)
--
-- Date: 03/02/2003
-- Version: 4.0
--
-- Inserito controllo di impossibilitא a procedere alla liquidazione definitiva per UO se non vi sono
-- sezionali definiti per la stessa.
-- Attivazione della liquidazione iva per istituzionali (San Marino senza IVA e Intra_ue)
-- Attivazione della gestione del prorata (CONFIGURAZIONE_CNR)
-- Inserito controllo di ammissibilitא alla liquidazione ente (tutte le uo devono aver liquidato in
-- modo definitivo
-- Inserito filtro su tabella LIQUID_IVA_INTERF_CDS per il controllo della liquidazione ente
--
-- Date: 07/02/2003
-- Version: 4.1
--
-- Revisione procedura per la predisposizione alle modifiche alla gestione della liquidazione IVA
-- Inserimento delle funzioni per stampa riepilogativo del centro
--
-- Date: 07/02/2003
-- Version: 4.2
--
-- Fix errori in calcolo liquidazione. Spostamento della logica client sul server
--
-- Date: 10/02/2003
-- Version: 4.3
--
-- Completato inserimento gestione del riepilogativo per sezionale del centro.
-- Spegnimento dei controlli per registro vuoto in caso di stampa riepilogativi per ente
--
-- Date: 24/02/2003
-- Version: 4.4
--
-- Fix errori CINECA n. 492 e 497. Corrette gestioni di conferma ed annullamento stampa registri svolte
-- dal centro. Errore del client che non eseguiva il commit e aggiunti controlli sull'ammissibilitא
-- ed azzeramento dei progressivi iva (protocolli).
-- Richiesta CINECA n. 486. Modifica layout della stampa del riepilogativo CENTRO. Inserito il recupero
-- dell'informazione della ragione sociale e partita IVA dell'ente da esporre in intestazione per tutti
-- i report IVA
--
-- Date: 26/02/2003
-- Version: 4.5
--
-- Inserito controllo, in sede di esposizione errori, che non si superino i 2000 caratteri nel messaggio
-- di non ammissibilitא della liquidazione al centro.
--
-- Date: 27/02/2003
-- Version: 4.6
--
-- Inserita gestione della liquidazione di massa al centro per le singole UO.
-- Inserita gestione batch per la liquidazione di massa
--
-- Date: 27/02/2003
-- Version: 4.7
--
-- Fix errore per cui non era cancellata l'entrata sulla REPORT_GENERICO nella liquidazione di massa
-- Eliminato filtro di non chiamare la contabilizzazione iva del centro se a credito o con debito inferiore
-- a 26 euro. Il controllo ט stato spostato su CNRCTB270
--
-- Date: 28/02/2003
-- Version: 4.8
--
-- Verifica che la liquidazione di massa salti il calcolo della liquidazione se questa ט giא stata
-- calcolata dalla singola UO.
--
-- Date: 28/02/2003
-- Version: 4.9
--
-- Fix in ammetti liquidazione di massa che cercava i record in report stato per la stampa dei registri
-- solo con stato B e non anche C
--
-- Date: 04/03/2003
-- Version: 4.10
--
-- Richieste CINECA
-- La liquidazione IVA di massa ora committa quelle andate a buon fine e scrive il log in BATCH_LOG_RIGA
-- per quelle che hanno avuto errore.
-- Inserita la gestione del calcolo prorata anche per la liquidazione UO.
--
-- Date: 06/03/2003
-- Version: 4.11
--
-- Richieste CINECA
-- Le gestioni intra_ue e san marino senza IVA (istituzionali) a rilevanza IVA sono solo quelle dove
-- il tipo sezionale presenta l'attributo ti_bene_servizio = 'B'
--
-- Date: 10/03/2003
-- Version: 4.12
--
-- Richieste CINECA
-- Inserita chiamata alla gestione per interfaccia della liquidazione IVA
--
-- Date: 13/03/2003
-- Version: 4.13
--
-- Richieste CINECA
-- Inserita chiamata alla gestione per interfaccia della liquidazione IVA anche per la gestione
-- provvisoria.
-- Eliminato il controllo di ammissibilitא della liquidazione di massa al fatto che sia lanciata
-- dal cds 999
--
-- Date: 18/03/2003
-- Version: 4.14
--
-- Errore interno 3328. Completamento gestione per deferred in lancio batch dinamici
--
-- Date: 14/04/2003
-- Version: 4.15
--
-- Errore CINECA n. 570. Duplicazione delle liquidazioni San Marino (valori pari a quelli intra) in
-- gestione liquidazione IVA di massa
--
-- Date: 15/09/2003
-- Version: 4.16
--
-- Attivazione gestione liquidazione IVA del mese di dicembre da eseguirsi sempre in esercizio
-- successivo. (chiusure).
-- La chiamata ט fatta sempre con periodo dicembre esercizio ed esercizio = esercizio + 1.
--
-- Date: 29/01/2004
-- Version: 4.17
--
-- Richiesta CINECA n. 739. Revisione recupero record relativi ad iva ad esigibilitא differita, si
-- vogliono includere non solo i record con data esigibilitא compresa nell'intervallo di selezione
-- ma anche quelli retrodatati e mai processati da liquidazione.
-- Interventi su:
-- 1) Indicatore di sezionale pieno. Routine valorizzaParametri
--
-- Date: 27/02/2004
-- Version: 4.18
--
-- Riapertura errore CINECA n. 660, manca la descrizione del sezionale nella stampa riepilogativi ente.
--
-- Date: 11/04/2005
-- Version: 4.19
-- Corretta anomalia: Nella liquidazione massiva, per ogni uo venivano elaborate sempre tutte le fatture,
-- qualunque fosse il tipo (C/I/S). In particolare, se quella commerciale andava in errore per mancanza
-- di disponibilita', le eventuali fatture ad esigibilita' differita divenute immediate nel mese di elaborazione,
-- rientravano nella liquidazione Intra UE e cosi' via....
--
-- Date: 02/01/2007
-- Version: 4.20
-- Gestione Liquidazione IVA sui residui: modificate le varie procedure con il nuovo parametro TipoImpegno
--
--==================================================================================================
--
--
-- Constants:

   -- Identificativi tipo elaborazione in input

   TI_ELAB_REGISTRO_IVA CONSTANT VARCHAR2(50) :='REGISTRI';
   TI_ELAB_RIEPILOGATIVO_UO CONSTANT VARCHAR2(50) :='RIEPILOGATIVI';
   TI_ELAB_RIEPILOGATIVO_CENTRO CONSTANT VARCHAR2(50) :='RIEPILOGATIVI_CENTRO';
   TI_ELAB_IVA_DIFFERITA CONSTANT VARCHAR2(50) :='DIFFERITA';
   TI_ELAB_IVA_DIFFERITA_II CONSTANT VARCHAR2(50) :='DIFFERITA_II';
   TI_ELAB_IVA_DIFFERITA_CENTRO CONSTANT VARCHAR2(50) :='DIFFERITA_CENTRO';
   TI_ELAB_LIQUIDAZIONE CONSTANT VARCHAR2(50) :='LIQUIDAZIONE';
   TI_ELAB_LIQUIDAZIONE_DEF CONSTANT VARCHAR2(50) :='LIQUIDAZIONE_DEF';
   TI_ELAB_LIQUIDAZIONE_MASSA CONSTANT VARCHAR2(50) :='LIQUIDAZIONE_MASSA';
   TI_ELAB_LIQUIDAZIONE_MASSA_PRV CONSTANT VARCHAR2(50) :='LIQUIDAZIONE_MASSA_PRV';

   -- Tipi liquidazione IVA
    -- Commerciale
   TI_LIQ_IVA_COMMERC CONSTANT CHAR(1) :='C';
    -- Istituzionale intraue
   TI_LIQ_IVA_ISTINTR CONSTANT CHAR(1) :='I';
    -- San Marino senz'iva
   TI_LIQ_IVA_ISTSMSI CONSTANT CHAR(1) :='S';
   -- Servizi non residenti
   TI_LIQ_IVA_ISTSNR  CONSTANT CHAR(1) :='X';
   -- Servizi split_payment
   TI_LIQ_IVA_ISTSPLIT  CONSTANT CHAR(1) :='P';

   -- Tipologie registri (A=Acquisti, V=Vendite *=Entrambi)

   TI_TIPO_REGISTRO_ACQ  CONSTANT CHAR(1) :='A';
   TI_TIPO_REGISTRO_VEN  CONSTANT CHAR(1) :='V';
   TI_TIPO_REGISTRO_ALL  CONSTANT CHAR(1) :='*';

   -- Entrate per gestione IVA in CONFIGURAZIONE_CNR

   K_PRIMA_GSTIVA CONSTANT CONFIGURAZIONE_CNR.cd_chiave_primaria%TYPE := 'GESTIONE_IVA';
   K_SECONDA_PRORATA_GSTIVA CONSTANT CONFIGURAZIONE_CNR.cd_chiave_secondaria%TYPE := 'ATTIVA_PRORATA';

   -- Costante tipo log per batch

   LOG_TIPO_LIQIVAMAS CONSTANT VARCHAR2(20) :='LIQUID_IVA_MASS00';

-- Definizione tabella PL/SQL di appoggio per parametri

   TYPE ParametriRec IS RECORD
       (
        stringa1 VARCHAR2(500),
        stringa2 VARCHAR2(100),
        stringa3 VARCHAR2(100),
        stringa4 VARCHAR2(100),
        stringa5 VARCHAR2(100),
        stringa6 VARCHAR2(100),
        stringa7 VARCHAR2(100),
        stringa8 VARCHAR2(100),
        intero INTEGER,
        numero NUMBER(15,2)
       );

   TYPE ParametriTab IS TABLE OF ParametriRec INDEX BY BINARY_INTEGER;

-- Definizione tabella PL/SQL di appoggio errori per liquidazione IVA di massa

   TYPE dsErroriRec IS RECORD
       (
        tStringaUo VARCHAR2(50),
        tStringaTipo VARCHAR2(10),
        tStringaErr VARCHAR2(2000)
       );

   TYPE dsErroriTab IS TABLE OF dsErroriRec INDEX BY BINARY_INTEGER;

-- Variabili globali

   gDataOdierna DATE;

   gparametri_tab ParametriTab;
   gsezio_acq_tab ParametriTab;
   gsezio_ven_tab ParametriTab;
   gmesi_tab ParametriTab;
   gtrimestri_tab ParametriTab;
   errori_tab dsErroriTab;

   TYPE GenericCurTyp IS REF CURSOR;

-- Functions e Procedures:

----------------------------------------------------------------------------------------------------
-- Main PROCEDURE
----------------------------------------------------------------------------------------------------
--
-- Main procedura di elaborazione stampe IVA
--
-- Elaborazione e lancio di tutte le stampe ed elaborazioni IVA
--
-- pre-post-name: Stampa provvisoria richiesta per un intervallo giא elaborato in modo definitivo.
-- pre:           Non ט possibile la stampa provvisoria di un registro IVA, riepilogativo del centro e UO (tutti i sezionali)
--                e stampa iva differita per periodi giא elaborati in definitivo.
--                E' obbligatorio l'uso della funzione di ristampa
-- post:          Viene sollevata eccezione.
--
-- pre-post-name: Stampa definitiva richiesta per un intervallo giא elaborato in modo definitivo.
-- pre:           Non ט possibile una elaborazione definitiva IVA per un intervallo giא elaborato in modo definitivo.
-- post:          Viene sollevata eccezione.
--
-- pre-post-name: Stampa definitiva richiesta per un intervallo temporale per cui non esiste una analoga elaborazione
--                definitiva nel periodo precedente ma vi sono entrate in altri periodi prima o dopo.
-- pre:           Non ט possibile una elaborazione definitiva IVA per un intervallo temporale per cui non esiste una
--                analoga elaborazione definitiva nel periodo precedente ma vi sono entrate in altri periodi prima o dopo.
-- post:          Viene sollevata eccezione.
--
-- pre-post-name: Elaborazione e lancio di una procedura IVA
-- pre:           Viene richiesta l'elaborazione di una procedura IVA. Nessun'altra precondizione verificata
-- post:
--                Note generali applicabili alle elaborazioni previste
--
--                     I periodi di riferimento di ogni stampa sono sempre per intervallo mensile.
--                     La gestione per esigibilitא differita si applica solo alle fatture attive.
--
--                Prima dell'attivazione delle singole elaborazioni IVA richieste sono svolte alcune funzioni
--                di carattere generale:
--
--                VALORIZZAZIONE PARAMETRI. Routine valorizzaParametri(...).
--                     Si estraggono i parametri generali per tutte le elaborazioni. L'elaborazione si articola in 7 passi:
--                     1) Memorizzazione codice IVA di riferimento per righe di tipo istituzionale per fatturazione
--                        passiva promiscua. NON RICHIESTO ALLO STATO ATTUALE (Nel caso di elaborazione stampa registri
--                        commerciali si escludono dalla selezione le righe fattura di tipo istituzionale).
--                     2) Memorizzazione identificativo moneta ITALIA (esposizione delle divise estere in stampa registri).
--                     3) Determinazione dei riferimenti ai sezionali (VENDITE)
--                        Si determina se l'elaborazione ט richiesta per tutti i sezionali o per uno solo
--                             Elaborazione per tutti i sezionali (Stampa riepilogativi per UO provvisoria e definitiva,
--                             stampa IVA differita, Liquidazione IVA)
--                                  Si estraggono i riferimenti ai codici sezionale (+ descrizione, tipo istituzionale e
--                                  commerciale, flag San Marino senza iva e flag intra ue) attivi, in un dato esercizio,
--                                  per una certa unitא organizzativa.
--                                  Se l'elaborazione ט riferita a riepilogativi per UO o a liquidazione
--                                       Se la tipologia di estrazione ט per gruppo commerciale si estraggono i soli
--                                       sezionali di tipo commerciale
--                                       Se la tipologia di estrazione ט per gruppo intra_ue si estraggono i soli sezionali
--                                       di tipo istituzionale che sono riferiti a gestione intra_ue
--                                       Se la tipologia di estrazione ט per gruppo san marino senza IVA si estraggono i soli
--                                       sezionali di tipo istituzionale riferiti a gestione san marino senza iva
--                                  Se l'elaborazione ט riferita a stampa riepilogo iva differita si aggiunge
--                                  il filtro di estrazione dei soli sezionali di tipo commerciale.
--                             Elaborazione per singolo sezionali (Stampa registri IVA provvisoria e definitiva, stampa
--                             riepilogativi per UO provvisoria)
--                                  Si estraggono i riferimenti al codice sezionale (+ descrizione e tipo istituzionale e
--                                  commerciale) in input all'elaborazione.
--                        Si memorizzano i sezionali cosל individuati rilevando se il loro contenuto ט pieno o vuoto in
--                        relazione all'elaborazione selezionata.
--                     4) Determinazione dei riferimenti ai sezionali (ACQUISTI). Vedi sopra punto 3 per i sezionali di
--                        vendita
--                     5) Indicatore di esercizio di riferimento in Euro o meno. Allo stato attuale sempre vero
--                     6) Indicatore di prevista gestione del prorata
--                     7) Decodifica nome unitא organizzativa
--
--                AMMETTI ELABORAZIONE. Routine ammettiElaborazione(...).
--                     Si controlla l'ammissibilitא della elaborazione richiesta (solo se non si tratta di ristampa).
--                     Elaborazione provvisoria
--                          1) In caso di stampa registri IVA, riepilogativo del centro e UO (tutti i sezionali) e stampa
--                             iva differita si controlla che lo stesso intervallo temporale non sia giא stato elaborato
--                             in modo definitivo.
--                             Vedi pre-post-name: Stampa provvisoria richiesta per un intervallo giא elaborato in modo definitivo
--                     Elaborazione definitiva
--                          1) Per ogni elaborazione si controlla che lo stesso intervallo temporale non sia giא stato
--                             elaborato in modo definitivo.
--                             Vedi pre-post-name: Stampa definitiva richiesta per un intervallo giא elaborato in modo definitivo.
--                          2) Per ogni elaborazione si controlla che, se non esiste una analoga elaborazione definitiva
--                             nel periodo precedente, non deve esistere alcuna entrata definitiva sia anteriore che
--                             successiva al periodo precedente.
--                             Vedi pre-post-name: Stampa definitiva richiesta per un intervallo temporale per cui non
--                             esiste una analoga elaborazione definitiva nel periodo precedente ma vi sono entrate in
--                             altri periodi prima o dopo.
--                          3) Se ט richiesta una stampa registri definitiva
--                             - Se esiste liquidazione definitiva nel periodo corrente o in periodi successivi sollevo
--                               errore. La liquidazione controllata ט quella compatibile con il tipo sezionale del registro
--                               in stampa (commerciale o istituzionale distinto per san marino senza iva e intra_ue).
--                               L'eventuale stampa definitiva di altri registri istituzionali non valida la liquidazione.
--                             - Se non esiste liquidazione definitiva per il periodo precedente ed esistono stampe di
--                               altri registri definitivi in periodi diversi dal corrente sollevo errore
--                          4) Nel caso in cui sia richiesta la stampa definitiva di un riepilogativo, dell'iva differita
--                             o di una liquidazione per un cd_cds diverso dall'ente, allora si controlla che tutti i
--                             registri per il mese corrente risultano stampati in modo definitivo.
--                          5) Se ט richiesta la stampa definitiva di una liquidazione ENTE si controlla, se non ט la prima,
--                             che tutte le liquidazioni UO siano state fatte in modo definitivo.
--                          6) Se ט richiesta la stampa definitiva di una liquidazione UO si controlla, se non ט la prima,
--                             che sia stata fatta la liquidazione ente.
--
--                ELABORAZIONE VUOTA. Routine chkRegistraReportVuoto(...).
--                     Si controlla il fatto che l'elaborazione possa produrre una stampa vuota. La gestione si applica
--                     solo a stampe registri, riepilogativi e per iva differita.
--                     STAMPA REGISTRI
--                          E' prevista la possibilitא di stampare un solo sezionale per volta sia in modo provvisorio
--                          che definitivo.
--                          L'elaborazione ט applicabile ad ogni tipologia di registro sia commerciale che istituzionale.
--                          Nel caso di elaborazione per registri commerciali si escludono dalla selezione le righe
--                          fattura di tipo istituzionale.
--                     STAMPA RIEPILOGATIVI (UO)
--                         E' prevista l'applicazione ai soli sezionali di tipo commerciale.
--
--        Sono definite le seguenti elaborazioni:
--        STAMPA REGISTRI IVA
--             E' possibile la stampa provvisoria o definitiva
--             PROVVISORIA
--                  E'sempre possibile eseguire una stampa provvisoria. Sono calcolati, ma non
--                  memorizzati, i progressivi di protocollazione iva.

--                     STAMPA REGISTRI
--                          E' prevista la possibilitא di stampare un solo sezionale per volta.
--                          L'elaborazione ט applicabile ad ogni tipologia di registro sia commerciale che istituzionale.
--                          Nel caso di elaborazione per registri commerciali si escludono dalla selezione le righe
--                          fattura di tipo istituzionale (previste solo per documenti promiscui).
--                     STAMPA RIEPILOGATIVI (UO)
--                         E' prevista l'applicazione ai soli sezionali di tipo commerciale.
--
--
--
--
-- Parametri:
-- inCdCdsOrigine   -> Codice del CDS richiedente l'elaborazione
-- inCdUoOrigine  -> Codice della UO richiedente l'elaborazione
-- inEsercizio    -> Esercizio di riferimento
-- inCdTipoSezionale  -> codice di un singolo sezionale in elaborazione oppure vale '*' per indicare
--                         tutti i sezionali di acquisto o vendita
-- inDataInizio     -> data di inizio periodo di riferimento all'elaborazione
-- inDataFine     -> data di fine periodo di riferimento all'elaborazione
-- inTipoStampa     -> Tipo di stampa in elaborazione:
--         'REGISTRI'
--         'RIEPILOGATIVI'
--                         'DIFFERITA'
--                         'LIQUIDAZIONE', 'LIQUIDAZIONE_DEF'
-- inTipoRegistro   -> Tipologia A = ACQUISTI, V = VENDITE, * = non definito
-- inTipoReport     -> Stampa P = PROVVISORIO D = DEFINITIVO
-- inRistampa     -> N = Prima stampa, Y = Ristampa
-- repID    -> Progressivo identificativo dell'elaborazione (sequence)
-- msgError   -> Eventuale messaggio da riportare al front end
-- id_utente    -> Identificativo utente della connessione
-- inGruppoRepot  -> Utilizzato per i riepilogativi totali e per la Liquidazione IVA:
--                         C = Commerciali
--                         I = Intra_ue
--                         S = San Marino

-- Guscio per gestione liquidazione IVA di massa in batch

   PROCEDURE job_liquidazione_massa
      (
       job NUMBER, pg_exec NUMBER, next_date DATE,
       inCdCdsOrigine VARCHAR2,
       inCdUoOrigine VARCHAR2,
       inEsercizio NUMBER,
       inCdTipoSezionale VARCHAR2,
       inDataInizio DATE,
       inDataFine DATE,
       inTipoStampa VARCHAR2,
       inTipoRegistro VARCHAR2,
       inTipoReport VARCHAR2,
       inRistampa VARCHAR2,
       repID INTEGER,
       logid INTEGER,
       msgError VARCHAR2,
       id_utente VARCHAR2,
       inGruppoReport VARCHAR2,
       inTipoImpegno VARCHAR2 Default Null
      );

   PROCEDURE StampeIVA
      (
       inCdCdsOrigine VARCHAR2,
       inCdUoOrigine VARCHAR2,
       inEsercizio NUMBER,
       inCdTipoSezionale VARCHAR2,
       inDataInizio DATE,
       inDataFine DATE,
       inTipoStampa VARCHAR2,
       inTipoRegistro VARCHAR2,
       inTipoReport VARCHAR2,
       inRistampa VARCHAR2,
       repID INTEGER,
       logid INTEGER,
       msgError IN OUT VARCHAR2,
       id_utente VARCHAR2,
       inGruppoReport VARCHAR2,
       inTipoImpegno VARCHAR2 Default Null
      );

   PROCEDURE StampeIVAInterna
      (
       inCdCdsOrigine VARCHAR2,
       inCdUoOrigine VARCHAR2,
       inEsercizio NUMBER,
       inCdTipoSezionale VARCHAR2,
       inDataInizio DATE,
       inDataFine DATE,
       inTipoStampa VARCHAR2,
       inTipoRegistro VARCHAR2,
       inTipoReport VARCHAR2,
       inRistampa VARCHAR2,
       repID INTEGER,
       logid INTEGER,
       msgError IN OUT VARCHAR2,
       id_utente VARCHAR2,
       inGruppoReport VARCHAR2,
       inTipoImpegno VARCHAR2 Default Null
      );

-- Aggiornamento dello stato del registro da B ad C

   PROCEDURE conferma_registro
      (
       aCdCds VARCHAR2,
       aCdUo VARCHAR2,
       aEsercizio NUMBER,
       aCodiceSezionale VARCHAR2,
       aDataInizio DATE,
       aDataFine DATE,
       aUtente VARCHAR2
      );

   PROCEDURE annulla_registro
      (
       aCdCds VARCHAR2,
       aCdUo VARCHAR2,
       aEsercizio NUMBER,
       aCodiceSezionale VARCHAR2,
       aDataInizio DATE,
       aDataFine DATE,
       aUtente VARCHAR2
      );


----------------------------------------------------------------------------------------------------
-- FUNZIONI e PROCEDURE di servizio
----------------------------------------------------------------------------------------------------

-- Lancio delle delle diverse stampe IVA previa esecuzione dei controlli di ammissibilitא

   PROCEDURE creaReportPredefinito
      (
       inCdCdsOrigine VARCHAR2,
       inCdUoOrigine VARCHAR2,
       inEsercizio NUMBER,
       inCdTipoSezionale VARCHAR2,
       inDataInizio DATE,
       inDataFine DATE,
       inTipoStampa VARCHAR2,
       inTipoRegistro VARCHAR2,
       inTipoReport VARCHAR2,
       inRistampa VARCHAR2,
       repID INTEGER,
       msg_out IN OUT VARCHAR2,
       id_utente VARCHAR2,
       inGruppoReport VARCHAR2,
       aEsercizioReale NUMBER,
       aTipoReportStato VARCHAR2,
       inTipoImpegno VARCHAR2 Default Null
      );

-- Memorizzazione parametri comuni a tutte le stampe

   PROCEDURE valorizzaParametri
      (
       inCdCdsOrigine VARCHAR2,
       inCdUoOrigine VARCHAR2,
       inEsercizio NUMBER,
       inCdTipoSezionale VARCHAR2,
       inDataInizio DATE,
       inDataFine DATE,
       inTipoStampa VARCHAR2,
       inTipoRegistro VARCHAR2,
       inGruppoReport VARCHAR2
      );

-- Composizione statement per selezione su SEZIONALE JOIN TIPO_SEZIONALE

   FUNCTION componiStatementSezionale
      (
       inCdCdsOrigine VARCHAR2,
       inCdUoOrigine VARCHAR2,
       inEsercizio NUMBER,
       inCdTipoSezionale VARCHAR2,
       inTipoStampa VARCHAR2,
       aTipoRegistro VARCHAR2,
       inGruppoReport VARCHAR2
      ) RETURN VARCHAR2;

-- Controllo ammissibilitא ad una elaborazione IVA

   PROCEDURE chkAmmettiElaborazione
      (
       inCdCdsOrigine VARCHAR2,
       inCdUoOrigine VARCHAR2,
       inEsercizio NUMBER,
       inCdTipoSezionale VARCHAR2,
       inDataInizio DATE,
       inDataFine DATE,
       inTipoStampa VARCHAR2,
       inTipoRegistro VARCHAR2,
       inTipoReport VARCHAR2,
       inGruppoReport VARCHAR2,
       aEsercizioReale NUMBER,
       aTipoReportStato VARCHAR2
      );

-- Controlla i sezionali vuoti e, per essi, genera una entrata in report_stato

   FUNCTION chkRegistraReportVuoto
      (
       inCdCdsOrigine VARCHAR2,
       inCdUoOrigine VARCHAR2,
       inEsercizio NUMBER,
       inCdTipoSezionale VARCHAR2,
       inDataInizio DATE,
       inDataFine DATE,
       inTipoStampa VARCHAR2,
       inTipoRegistro VARCHAR2,
       inTipoReport VARCHAR2,
       inRistampa VARCHAR2,
       idUtente VARCHAR2,
       inGruppoReport VARCHAR2,
       aTipoReportStato VARCHAR2
      ) RETURN CHAR;

-- Stampa registri IVA

   PROCEDURE stampaRegistri
      (
       inCdCdsOrigine VARCHAR2,
       inCdUoOrigine VARCHAR2,
       inEsercizio NUMBER,
       inCdTipoSezionale VARCHAR2,
       inDataInizio DATE,
       inDataFine DATE,
       inTipoStampa VARCHAR2,
       inTipoRegistro VARCHAR2,
       inTipoReport VARCHAR2,
       inRistampa VARCHAR2,
       repID INTEGER,
       msg_out IN OUT VARCHAR2,
       id_utente VARCHAR2,
       inGruppoReport VARCHAR2,
       aTipoReportStato VARCHAR2
      );

-- Stampa riepilogativi IVA

   PROCEDURE stampaRiepilogativi
      (
       inCdCdsOrigine VARCHAR2,
       inCdUoOrigine VARCHAR2,
       inEsercizio NUMBER,
       inCdTipoSezionale VARCHAR2,
       inDataInizio DATE,
       inDataFine DATE,
       inTipoStampa VARCHAR2,
       inTipoRegistro VARCHAR2,
       inTipoReport VARCHAR2,
       inRistampa VARCHAR2,
       repID INTEGER,
       msg_out IN OUT VARCHAR2,
       id_utente VARCHAR2,
       inGruppoReport VARCHAR2,
       aTipoReportStato VARCHAR2
      );

-- Stampa riepilogativi IVA per centro (ente)

   PROCEDURE stampaRiepilogativiCentro
      (
       inCdCdsOrigine VARCHAR2,
       inCdUoOrigine VARCHAR2,
       inEsercizio NUMBER,
       inCdTipoSezionale VARCHAR2,
       inDataInizio DATE,
       inDataFine DATE,
       inTipoStampa VARCHAR2,
       inTipoRegistro VARCHAR2,
       inTipoReport VARCHAR2,
       inRistampa VARCHAR2,
       repID INTEGER,
       msg_out IN OUT VARCHAR2,
       id_utente VARCHAR2,
       inGruppoReport VARCHAR2,
       aTipoReportStato VARCHAR2
      );

-- Stampa IVA ad esigibilitא differita

   PROCEDURE stampaIvaDifferita
      (
       inCdCdsOrigine VARCHAR2,
       inCdUoOrigine VARCHAR2,
       inEsercizio NUMBER,
       inCdTipoSezionale VARCHAR2,
       inDataInizio DATE,
       inDataFine DATE,
       inTipoStampa VARCHAR2,
       inTipoRegistro VARCHAR2,
       inTipoReport VARCHAR2,
       inRistampa VARCHAR2,
       repID INTEGER,
       id_utente VARCHAR2,
       inGruppoReport VARCHAR2,
       aTipoReportStato VARCHAR2
      );

-- Elaborazione della LIQUIDAZIONE di massa (centro per tutte le UO)

   PROCEDURE elaboraLiquidazioneMassa
      (
       aCdCdsOrigine VARCHAR2,
       aCdUoOrigine VARCHAR2,
       aEsercizio NUMBER,
       aCdTipoSezionale VARCHAR2,
       aDataInizio DATE,
       aDataFine DATE,
       aTipoStampa VARCHAR2,
       aTipoRegistro VARCHAR2,
       aTipoReport VARCHAR2,
       aRistampa VARCHAR2,
       repID INTEGER,
       id_utente VARCHAR2,
       inGruppoReport VARCHAR2,
       aEsercizioReale NUMBER,
       aTipoReportStato VARCHAR2,
       inTipoImpegno VARCHAR2 Default Null
      );

-- Elaborazione della LIQUIDAZIONE

   PROCEDURE stampaLiquidazione
      (
       aCdCdsOrigine VARCHAR2,
       aCdUoOrigine VARCHAR2,
       aEsercizio NUMBER,
       aCdTipoSezionale VARCHAR2,
       aDataInizio DATE,
       aDataFine DATE,
       aTipoStampa VARCHAR2,
       aTipoRegistro VARCHAR2,
       aTipoReport VARCHAR2,
       aRistampa VARCHAR2,
       repID INTEGER,
       id_utente VARCHAR2,
       inGruppoReport VARCHAR2,
       aEsercizioReale NUMBER,
       aTipoReportStato VARCHAR2,
       inTipoImpegno VARCHAR2 Default Null
      );

-- Calcolo della liquidazione IVA sia UO che Ente

   PROCEDURE calcolaLiquidazione
      (
       aCdCdsOrigine VARCHAR2,
       aCdUoOrigine VARCHAR2,
       aEsercizio NUMBER,
       aCdTipoSezionale VARCHAR2,
       aDataInizio DATE,
       aDataFine DATE,
       aTipoRegistro VARCHAR2,
       aTipoReport VARCHAR2,
       aRistampa VARCHAR2,
       repID INTEGER,
       id_utente VARCHAR2,
       inGruppoReport VARCHAR2,
       aEsercizioReale NUMBER
      );

-- Aggiornamento dei dati per liquidazione definitiva

   PROCEDURE liquidazioneDefinitiva
      (
       aCdCdsOrigine VARCHAR2,
       aCdUoOrigine VARCHAR2,
       aEsercizio NUMBER,
       aCdTipoSezionale VARCHAR2,
       aDataInizio DATE,
       aDataFine DATE,
       aTipoStampa VARCHAR2,
       aTipoRegistro VARCHAR2,
       aTipoReport VARCHAR2,
       aRistampa VARCHAR2,
       repID INTEGER,
       id_utente VARCHAR2,
       inGruppoReport VARCHAR2,
       aTipoReportStato VARCHAR2
      );

procedure CalcolaMesiTrimestri;

END CNRCTB250;
