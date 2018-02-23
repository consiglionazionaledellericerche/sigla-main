--------------------------------------------------------
--  DDL for Package CNRMIG400
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRMIG400" AS
-- =================================================================================================
--
-- CNRCTB400 - Package per la migrazione dell'inventario
--
-- Date: 16/11/2004
-- Version: 1.3
--
-- Dependency: CNRCTB015/020/100/400/800 - IBMERR001 - IBMUTL200
--
----------------------------------------------------------------------------------------------------
-- History:
--
-- Date: 02/11/2004
-- Version: 1.0
--
-- Creazione
--
-- Date: 08/11/2004
-- Version: 1.1
--
-- Modifica chiamata per ottenere il codice dell'Ubicazione di default
--
-- Date: 09/11/2004
-- Version: 1.2
--
-- Sistemazione con dati CIR e pre-post conditions
-- Fix errore su cursore invalido
--
-- Date: 16/11/2004
-- Version: 1.3
--
-- Inserito controllo inventario aperto
--

-- 06/09/2005 Inserita procedura di inserimento in Inventario_beni per la gestione di fl_migrato
-- =================================================================================================
--
-- Constants:
--

   FASE_UNO CONSTANT VARCHAR2(50) :='MIGRAZIONE INVENTARIO - FASE BASE';
   FASE_DUE CONSTANT VARCHAR2(50) :='MIGRAZIONE INVENTARIO - CARICAMENTO IN CIR';
   SEMAFORO CONSTANT VARCHAR2(20) :='MIGRA_BENI00';
   CONFIG_CNR_KEY1 VARCHAR2(50) := 'LINEA_ATTIVITA_SPECIALE';
   CONFIG_CNR_KEY2 VARCHAR2(100) := 'LINEA_COMUNE_MIGRAZIONE_BENI';
   STATO_INIZIALE CHAR(1) := 'I';
   STATO_MIGRATO CHAR(1) := 'M';
   STATO_ERRORE CHAR(1) := 'E';
   CONDIZIONE_BENE_DEFAULT VARCHAR2(10) := '1';

-- Variabili globali

   dataOdierna DATE;
   aEsercizioBase NUMBER(4);
   aUtente VARCHAR2(20):= '$$$$$MIGRAZIONE$$$$$';

   TYPE beniDaMigrareOriList
        IS TABLE OF CNR_INVENTARIO_BENI_MIG_ORI%ROWTYPE
   INDEX BY BINARY_INTEGER;
   beniDaMigrareOri_tab beniDaMigrareOriList;

   TYPE beniDaMigrareList
        IS TABLE OF CNR_INVENTARIO_BENI_MIG%ROWTYPE
   INDEX BY BINARY_INTEGER;
   beniDaMigrare_tab beniDaMigrareList;

   TYPE GenericCurTyp IS REF CURSOR;

--
-- Functions e Procedures:
--
-- La procedure rappresenta la prima fase del processo di migrazione beni. I beni da migrare sono portati dalla tabella
-- compilata a cura CNR (CNR_INVENTARIO_BENI_MIG_ORI) alle tabelle base della migrazione CNR_INVENTARIO_BENI_MIG,
-- CNR_INVENTARIO_BENI_MIG_DETT e CNR_INVENTARIO_BENI_MIG_SCARTO.
--
-- Pre-post name: Acquisizione del semaforo applicativo
-- Pre:           E' in corso una migrazione beni
-- Post:          Viene sollevata un'eccezione
--
-- Pre-post name: Recupero del progressivo log
-- Pre:           Non è possibile recuperare il progressivo identificativo del log (testata e dettaglio)
-- Post:          Viene sollevata un'eccezione
--
-- Pre-post name: Recupero del progressivo di caricamento per CNR_INVENTARIO_BENI_MIG e CNR_INVENTARIO_BENI_MIG_DETT.
-- Pre:           Non è possibile recuperare il progressivo di caricamento
-- Post:          Viene sollevata un'eccezione
--
-- Pre-post name: Esecuzione della prima fase della migrazione beni
-- Pre:           Nessuna delle precondizioni precedenti è verificata
-- Post:   Vengono letti tutti i beni da caricare presenti in CNR_INVENTARIO_BENI_MIG_ORI ordinati per id_bene_origine
--         e ti_movimento (l'eventuale carico è il primo record).
--         Per ogni blocco di record omogenei per id_bene_origine si processa la migrazione in CNR_INVENTARIO_BENI_MIG e
--         CNR_INVENTARIO_BENI_MIG_DETT. Gli eventuali errori trovati non bloccano la procedura di migrazione.
--         Si eseguono i seguenti controlli e azioni (con generazione errore).
--            Controllo che la matrice beni sia piena
--            Controllo se il primo record riguarda un carico o una variazione di valore.
--            Recupero il record bene con parametro id_bene_origine. Se non esiste carico allora deve esistere
--            il bene altrimenti il bene non deve esistere
--            Recupero dell'esercizio.
--               Se non esiste il carico
--                  Si recupera il valore di INVENTARIO_BENI.esercizio_carico_bene
--               altrimenti si usa l'attributo CNR_INVENTARIO_BENI_MIG_ORI.esercizio_coep_bene
--            Recupero degli identificativi del Cds e UO di CIR dai riferimenti SCI
--            Recupero l'identificativo dell'inventario (pg_inventario)
--            Si compone il record di CNR_INVENTARIO_BENI_MIG definendo i valori in modo differente a seconda che
--            nel blocco sia presente o meno un carico.
--            Se non è presente un carico
--               alcuni valori sono presi da INVENTARIO_BENI
--            altrimenti si verificano i dati in input da CNR_INVENTARIO_BENI_MIG_ORI:
--               La descrizione del bene
--               La categoria gruppo deve esistere, deve essere per gestione inventario e deve essere un gruppo e
--               non una categoria
--               Il flag ammortamento.
--                  Se è NULL
--                     Si valorizza con quanto presente nella categoria gruppo.
--                  altrimenti
--                     Deve essere consistente con la categoria gruppo, Il bene non può essere definito come
--                     ammortizzabile in una categoria gruppo che non è ammortizzabile
--               Il tipo ammortamento (può essere NULL).
--                  Se il flag ammortamento è falso
--                     Non può essere valorizzato il tipo ammortamento
--                  altrimenti
--                     Si verifica che il tipo ammortamento indicato sia tra quelli previsti nell'esercizio per
--                     la categoria gruppo di riferimento
--               Ubicazione. Si prende quella di default
--               Si compone l'etichetta del bene
--               Si valorizza la condizione bene con il valore di default o con quanto indicato in CNR_INVENTARIO_BENI_MIG_ORI
--               Si valorizza il bene come istituzionale
--            Se tutto bene allora:
--               Si inserisce il record di testata in CNR_INVENTARIO_BENI_MIG (senza importi)
--               Si cicla sui dettagli del gruppo per il recupero dei valori (valore iniziale, variazioni, ecc.)
--                  Se il record è per carico l'importo non può essere negativo e questo è portato come valore iniziale del bene
--                  Se il record è per variazione si controllo il flag forza valore iniziale per portare l'importo in
--                  somma algebrica con il valore iniziale altrimenti, in base al segno, si valorizzano le variazioni piu e meno
--                  Si calcola l'imponibile ammortamento. E' pari all'assestato valore iniziale + variazioni solo per i beni
--                  dal 2000 in avanti e se il bene è ammortizzabile.
--                  Si calcola il valore ammortizzato. Si legge solo per i beni con esercizio carico dal 2000 in avanti e
--                  se il bene è ammortizzabile
--                  Se tutto bene si inserisce il record in CNR_INVENTARIO_BENI_MIG_DETT
--               Al termine del ciclo si controlla la consistenza complessiva dei valori migrati includendo, se il blocco
--               non prevede il carico, anche i valori già presenti in INVENTARIO_BENI.
--               Si aggiornano i campi valore su CNR_INVENTARIO_BENI_MIG (ora con importi)
--               Se tutto bene allora
--                  Si azzera il blocco e si prosegue con la fetch
--               altrimenti
--                  Si annulla quanto fatto e i record di CNR_INVENTARIO_BENI_MIG_ORI sono portati in
--                  CNR_INVENTARIO_BENI_MIG_SCARTO con la valorizzazione della motivazione
--            altrimenti
--               Si annulla quanto fatto e i record di CNR_INVENTARIO_BENI_MIG_ORI sono portati in
--               CNR_INVENTARIO_BENI_MIG_SCARTO con la valorizzazione della motivazione
--         In uscita dalla procedura:
--            Se non vi sono errori
--               Se non è stato letto alcun record si solleva eccezione
--               Si valorizza il rigo di dettaglio del log indicando il successo dell'operazione.
--                  Se vi sono stati degli scarti il dettaglio di log è messo in warning
--            Se vi sono errori
--               Si valorizza il rigo di dettaglio del log indicando l'errore dell'operazione.
--            Si rimuovono i record da CNR_INVENTARIO_BENI_MIG_ORI. Ora sono:
--               In CNR_INVENTARIO_BENI_MIG e CNR_INVENTARIO_BENI_MIG_DETT quelli andati a buon fine
--               In CNR_INVENTARIO_BENI_MIG_SCARTO quelli con errore
--            Si rilascia il semaforo di esecuzione migrazione in corso.
--
-- Parametri:
--

   PROCEDURE migrazioneBeniBase;

-- La procedura migra i beni in stato 'I' (iniziale) presenti sulla tabella INVENTARIO_BENI_MIG sulla tabella
-- INVENTARIO_BENI ed eventualmente su INVENTARIO_UTILIZZATORI_LA
--
-- Pre-post name: Acquisizione del semaforo applicativo
-- Pre:           E' in corso una migrazione beni
-- Post:          Viene sollevata un'eccezione
--
-- Pre-post name: Recupero del progressivo log
-- Pre:           Non è possibile recuperare il progressivo identificativo del log (testata e dettaglio)
-- Post:          Viene sollevata un'eccezione
--
-- Pre-post name: Recupero del progressivo di caricamento per CNR_INVENTARIO_BENI_MIG e CNR_INVENTARIO_BENI_MIG_DETT.
-- Pre:           Non è possibile recuperare il progressivo di caricamento
-- Post:          Viene sollevata un'eccezione
--
-- Pre-post name: Recupero della linea di attività (CONFIGURAZIONE_CNR) per la valorizzazione dell'utilizzatore
-- Pre:           Non è possibile recuperare la linea di attività
-- Post:          Viene sollevata un'eccezione
--
-- Pre-post name: Esecuzione della prima fase della migrazione beni
-- Pre:           Nessuna delle precondizioni precedenti è verificata
-- Post:   Vengono letti tutti i beni da caricare presenti in CNR_INVENTARIO_BENI_MIG che siano in stato I ed in base
--         agli eventuali parametri indicati. I record sono ordinati per id_bene_origine e pg_caricamento
--         (l'eventuale carico è il primo record).
--         Per ogni blocco di record omogenei per id_bene_origine si processa la migrazione in INVENTARIO_BENI.
--         Gli eventuali errori trovati non bloccano la procedura di migrazione.
--         Si eseguono i seguenti controlli e azioni (con generazione errore).
--            Controllo che la matrice beni sia piena
--            Controllo se il primo record riguarda un carico o una variazione di valore. (creazione bene o modifica
--            dello stesso
--            Se non deve essere creato il bene
--               Si recupera il valore dello stesso con la chiave primaria di INVENTARIO_BENI riportata in
--               CNR_INVENTARIO_BENI_MIG
--            altrimenti il controllo è fatto con parametro id_bene_origine.
--            Se non esiste carico allora deve esistere il bene altrimenti il bene non deve esistere
--            Si compone il record INVENTARIO_BENI (senza importi) recuperando, se il bene deve essere creato, il
--            numero di riferimento del bene (da 1 al valore iniziale impostato in ID_INVENTARIO)
--            Si cicla sui dettagli del gruppo per il recupero dei valori (valore iniziale, variazioni, ecc.)
--            Si aggiornano i campi relativi al numero bene ed allo stato della migrazione in CNR_INVENTARIO_BENI_MIG.
--            Al termine del ciclo si controlla la consistenza complessiva dei valori migrati includendo, se il blocco
--            non prevede il carico, anche i valori già presenti in INVENTARIO_BENI.
--            Si aggiornano i campi valore su INVENTARIO_BENI (ora con importi)
--            Se il bene deve essere creato si valorizza anche INVENTARIO_UTILIZZATORI_LA
--            Se tutto bene allora
--               Si azzera il blocco e si prosegue con la fetch
--            altrimenti
--               Si annulla quanto fatto e i record di CNR_INVENTARIO_BENI_MIG_DETT sono copiati in
--               CNR_INVENTARIO_BENI_MIG_SCARTO con la valorizzazione della motivazione.
--               Il record in processo di CNR_INVENTARIO_BENI_MIG è posto in stato errore.
--         In uscita dalla procedura:
--            Se non vi sono errori
--               Se non è stato letto alcun record si solleva eccezione
--               Si valorizza il rigo di dettaglio del log indicando il successo dell'operazione.
--                  Se vi sono stati degli scarti il dettaglio di log è messo in warning
--            Se vi sono errori
--               Si valorizza il rigo di dettaglio del log indicando l'errore dell'operazione.
--            Si rilascia il semaforo di esecuzione migrazione in corso.
--
-- Parametri: (possono essere nulli per processare tutti i records)
--   aCdCds         -> Codice del cds
--   aCdUO          -> Codice della UO
--   aPgInventario  -> Progressivo identificativo dell'inventario
--


   PROCEDURE migrazioneBeniCompleta
      (aCdCds VARCHAR2,
       aCdUo  VARCHAR2,
       aPgInventario NUMBER);


-- Inserisce un record in INVENTARIO_BENI

   PROCEDURE insInventarioBeni
      (aRecInventarioBeni INVENTARIO_BENI%Rowtype);
 END;
