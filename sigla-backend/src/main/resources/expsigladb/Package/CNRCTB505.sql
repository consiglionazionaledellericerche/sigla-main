--------------------------------------------------------
--  DDL for Package CNRCTB505
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRCTB505" AS
--==================================================================================================
--
-- CNRCTB505 - Calcolo e scrittura righe diaria su missioni.
--
-- Date: 29/10/2003
-- Version: 1.27
--
----------------------------------------------------------------------------------------------------
-- PARAMETRI IN INPUT
----------------------------------------------------------------------------------------------------
--
-- +----------------------+---------------------------------------+------------+
-- |PARAMETRO             |DESCRIZIONE                            |OBBLIGATORIO|
-- +----------------------+---------------------------------------+------------+
-- |inCDSCompenso         |CDS di riferimento compenso            |     SI     |
-- |inUOCompenso          |UO di riferimento compenso             |     SI     |
-- |inEsercizioCompenso   |Esercizio di riferimento compenso      |     SI     |
-- |inPgCompenso          |Progressivo identificativo compenso    |     SI     |
-- +----------------------+---------------------------------------+------------+
--
-- Dependency: CNRCTB 080/500/545 IBMERR 001
--
-- History:
--
-- Date: 12/06/2002
-- Version: 1.0
--
-- Creazione Package.
--
-- Date: 27/06/2002
-- Version: 1.1
--
-- Introdotta la gestione delle missioni in comune propio.
-- Revisione generale della procedura ed introduzione del controllo su MISSIONE_ABBATTIMENTI
--
-- Date: 01/07/2002
-- Version: 1.2
--
-- Fix errore in verifica ammissibilità delle spese; era sempre esposto il messaggio di spesa non
-- prevista in configurazione per tutte le tappe inserite.
-- Fix errore abbattimento della diaria e della quota esente; la percentuale non era divisa per 100.
--
-- Date: 17/07/2002
-- Version: 1.3
--
-- Fix errore di chiave duplicata in inserimento diarie
--
-- Date: 19/07/2002
-- Version: 1.4
--
-- Aggiornamento documentazione
--
-- Date: 23/07/2002
-- Version: 1.5
--
-- Inserita routine di valorizzazione dell'importo lordo e della quota esente in sede di generazione
-- compenso da missione.
-- Inserita routine di cancellazione della missione
--
-- Date: 08/08/2002
-- Version: 1.6
--
-- Modificata gestione accessi agli abbattimenti ora si controlla solo per:
--      < 4 = intervallo ore della tappa minore di 4 ( 4 ore compreso)
--      > 4 = intervallo ore maggiore di 4 e minore di 8 (8 ore comprese)
--      > 8 = intervallo ore maggiore di 8
-- Modifica routine di calcolo lordizzazione
--
-- Date: 02/09/2002
-- Version: 1.7
--
-- Modificata la routine di recupero dello scaglione per errore in caso di configurazione sia ente
-- che percipiente.
--
-- Date: 25/09/2002
-- Version: 1.8
--
-- Fix errore in calcolo imponibile lordo del compenso in caso di missione in comune proprio o all'estero
-- La lordizzazione o il calcolo prendevano in considerazione la diaria lorda e non quella netta
-- ovvero quella decurtata degli eventuali abbattimenti
--
-- Date: 26/09/2002
-- Version: 1.9
--
-- Inserito il controllo che la stringa degli errori sugli abbattimenti non superi la dimensione del
-- campo di 1000 caratteri
-- Modifiche alla routine di cancellazione della missione
--
-- Date: 27/09/2002
-- Version: 1.10
--
-- Inserito calcolo della lordizzazione dell'importo lordo del compenso non più in base all'aliquota
-- massima ma sulla media calcolata dagli scaglioni.
-- Inserito controllo sugli anticipi in caso di cancellazione di una missione
--
-- Date: 02/10/2002
-- Version: 1.11
--
-- Fix errore in calcolo missione con lordizzazione se opero per più scaglioni
-- (ritorno importo compenso a NULL)
--
-- Date: 08/10/2002
-- Version: 1.12
--
-- Fix errore calcolo imponibile per missione con percipiente altro e trattamento irpef
-- annualizzata
--
-- Date: 10/10/2002
-- Version: 1.13
--
-- Fix errore missione senza spese (max riga)
--
-- Date: 10/10/2002
-- Version: 1.14
--
-- Fix errore - eliminato cancellazione dettagli diaria
--
-- Date: 14/10/2002
-- Version: 1.15
--
-- Fix errore - le missioni con stato coge/coan ad R o C possono
-- 	   		    essere cancellate solo logicamente
--
-- Date: 15/10/2002
-- Version: 1.16
--
-- Fix errore - le missioni con stato coge/coan ad X devono
-- 	   		    essere cancellate
--
-- Date: 28/10/2002
-- Version: 1.17
--
-- Fix errore - In fase di creazione diaria per missione non viene visualizzato il messaggio di
-- errore se non esiste l' entrata nella tabella MISSIONE_ABBATTIMENTIO
--
-- Date: 07/11/2002
-- Version: 1.18
--
-- Fix errore 355 - errore in lordizzazione missione estero. Era sempre usata, nel calcolo del totale
-- irpef l'aliquota del primo scaglione e non quella dei successivi
--
-- Date: 18/11/2002
-- Version: 1.19
--
-- Memorizzazione dell'aliquota IRPEF in caso di lordizzazione con trattamento annualizzato.
-- Tale aliquota è portata in testata compenso e viene usata dallo stesso nel calcolo dell'IRPEF
-- in modo indipendente da quanto indicato sul trattamento.
--
-- Date: 19/02/2003
-- Version: 1.20
--
-- L'aliquota IRPEF in caso di lordizzazione con trattamento annualizzato è esportata moltiplicata
-- per 100
--
-- Date: 28/02/2003
-- Version: 1.21
--
-- Modificato un parametro nella chiamata della funzione "getMissioneDiaria" del package
-- CNRCTB500. Modifica effettuata per consentire di utilizzare il cambio della tappa
-- per convertire, se necessario, l'importo della diaria.
--
-- Date: 03/03/2003
-- Version: 1.22
--
-- Aggiunto la clausola di ORDER BY nella ricerca degli abbattimenti - funzione
-- "verificaConfigSpeseTappe"
--
-- Date: 26/03/2003
-- Version: 1.23
--
-- Fix errore CINECA n. 557. L'importo della maggiorazione di spese anticipate non era considerato
-- nella valorizzazione degli importi per il calcolo del compenso
--
-- Date: 26/03/2003
-- Version: 1.24
--
-- Richiesta CINECA n. 535. Forzata ricerca su tabella abbattimenti per ore > 8 per tappe di durata
-- inferiore quando la durata della stessa missione è maggiore di 8 ore.
--
-- Date: 16/04/2003
-- Version: 1.25
--
-- Richiesta CINECA n. 562. Implementazione della cancellazione del conguaglio. Modifica chiamata
-- alla routine CNRCTB545.eliminaLogicoCompenso.
--
-- Date: 22/04/2003
-- Version: 1.26
--
-- Fix errore CINECA n. 582. Modifica base dati, aggiunto attributo MISSIONE_DETTAGLIO.im_maggiorazione_euro.
-- Modifica del calcolo degli importi per il compenso per recuperare non
-- da MISSIONE_DETTAGLIO.im_maggiorazione ma da MISSIONE_DETTAGLIO.im_maggiorazione_euro
--
-- Date: 29/10/2003
-- Version: 1.27
--
-- Troncato la data di inizio missione nella ricerca degli inquadramenti
--==================================================================================================
--
-- Constants
--

   isOttoOre CONSTANT NUMBER := 0.33333;

-- Variabili globali

   dataOdierna DATE;

   -- Testata missione in elaborazione

   aRecMissione MISSIONE%ROWTYPE;

   -- Anagrafico di riferimento per il terzo della missione

   aRecAnagrafico ANAGRAFICO%ROWTYPE;

   -- Informazioni base dell'inquadramento

   aRecRifInquadramento RIF_INQUADRAMENTO%ROWTYPE;

   -- Quota esente Italia e estera

   aRecQuotaEsenteItalia MISSIONE_QUOTA_ESENTE%ROWTYPE;
   aRecQuotaEsenteEstero MISSIONE_QUOTA_ESENTE%ROWTYPE;

   -- Dichiarazioni tabelle PL/SQL

   matriceTappe_tab CNRCTB500.matriceTappeTab;
   coriLordizza_tab CNRCTB500.coriLordizzaTab;

   -- Dichiarazione di un cursore generico

   TYPE GenericCurTyp IS REF CURSOR;

--
-- Functions e Procedures
--

----------------------------------------------------------------------------------------------------
-- MAIN PROCEDURE
----------------------------------------------------------------------------------------------------

-- Lettura di una missione esistente, verifica ammissibilità delle spese registrate e calcolo delle diarie
--
-- pre-post-name: Entità anagrafica associata a terzo non trovata
-- pre: Non viene trovata l'entità anagrafica a cui il terzo della missione si riferisce
-- post: Viene sollevata un'eccezione
--
-- pre-post-name: Inquadramento soggetto anagrafico non trovato
-- pre: Inquadramento valido non trovato per il terzo della missione alla data di inizio della missione
-- post: Viene sollevata un'eccezione
--
-- pre-post-name: Nessuna informazione di abbattimento trovata per alcune tappe
-- pre: Durante l'elaborazione delle tappe, non vengono trovate informazioni di configurazione sugli abbattimenti
-- post: Viene sollevata un'eccezione
--
-- pre-post-name: Quota esente è minore della diaria netta in caso di missione in italia con tappa in altro comune
-- pre: Esiste una tappa della missione per cui risulta
--               Area geografica Italia ('I')
--               Comune proprio
--               Quota esente < Importo netto della diaria
-- post: Viene sollevata un'eccezione
--
-- pre-post-name: Lettura di una missione esistente, verifica ammissibilità delle spese registrate e calcolo delle diarie
-- pre: Nesun'altra precondizione verificata
-- post:
-- Vengono letti i dati di base della missione (metodo getDatiBaseMissione) come segue:
--    -- Lettura della testata della missione
--    -- Recupero dei dati dell'anagrafico associato al terzo della missione
--    -- Lettura dati base dell'inquadramento:
--    -- Lettura della quota esente Italia/Estero
--            Se la missione è in comune proprio o altro comune italiano
--                   Viene recuperata la quota esente per comune ITALIA ('I') valida alla data di inizio missione
--            Se la missione è in comune estero
--                   Viene recuperata la quota esente per comune ESTERO('E') valida alla data di inizio missione
--
-- Viene costruita la matrice diarie per tappa e verificata la consistenza delle spese. Queste devono trovare una entrata nella tabella
--     Per ogni tappa esistente nella missione
--          Vengono impostate data inizio/fine della tappa = a quelle della missione
--          Viene determinato il numero di ore della tappa come segue:
--                 -- La tappa è di 24 ore
--                         Il numero di ore = 24
--                -- Controllo se la tappa ha data inizio e fine nello stesso giorno
--                    Se l'inizio/fine della tappa è sotto il primo quarto d'ora dell'ora
--                        L'ora di inizio/fine = ora inizio/fine della tappa
--                    Altrimenti se l'inizio/fine della tappa è nell'ultimo quarto d'ora dell'ora
--                        L'ora di inizio/fine = ora inizio/fine della tappa + 1
--                    Altrimenti
--                        L'ora di inizio/fine = ora inizio/fine della tappa + 0,5
--                    Il numero di ore = Ora di fine - Ora di inizio se il giorno è lo stesso
--                    Il numero di ore = 24 - Ora di inizio + Ora di fine se il giorno di inizio è diverso da quello di fine
--          Viene determinata l'area geografica e la conseguente quota esente lorda
--              Se il comune è estero
--                L'area geografica viene impostata a 'E'
--                La quota esente viene impostata al valore relativo a comune estero
--              Altrimenti
--                L'area geografica viene impostata a 'I'
--                La quota esente viene impostata al valore relativo a comune italiano
--           Viene impostato il codice della nazione
--           Viene impostato l'indicatore di comune proprio
--           Viene impostato l'inquadramento del soggetto anagrafico
--           Vengono inizializzati alcuni flag relativi alla tappa:
--                   Tappa con pasto='N'
--                   Tappa con alloggio='N'
--                   Tappa con trasporto='N';
--                   Tappa con navigazione=Ereditato dalla tappa in processo
--                   Tappa con vitto gratuito=Ereditato dalla tappa in processo
--                   Tappa con alloggio gratuito=Ereditato dalla tappa in processo
--                   Tappa con vitto e alloggio gratuito=Ereditato dalla tappa in processo
--                   Tappa senza diaria=Ereditato dalla tappa in processo
--                   Abbattimento tappa trovato='N'
--                   Percentuale abbattimento=0
--                   Diaria netta=0
--
--            Vengono lette le spese registrate a fronte di ogni tappa. La funzione aggiorna gli elementi della matrice delle tappe passati in input in base alle risultanze ricavate dalle spese associate ad ogni tappa.
--              Viene letta la diaria massima applicabile ad ogni tappa.  Si tratta della sola diaria lorda senza alcuna normalizzazione per le ore di riferimento o in base alle percentuali di abbattimento:
--                        Se la tappa è senza diaria
--                                Diaria lorda = 0
--                        Altrimenti
--                                Viene letta la diaria valida per il codice di gruppo di inquadramento, la nazione e la data di inizio missione.
--
-- Viene letta la matrice delle tappe della missione per aggiornamento degli indicatori di ritrovamento di un riferimento in MISSIONE_ABBATTIMENTI dati i dettagli di spesa associati ad una tappa
--    Per ogni tappa presente nella tabella tappe della missione:
--      Per ogni dato di abbattimento che soddisfa alle seguenti condizioni:
--                            Data inizio validità <= Data inizio tappa
--                            Data fine validità >= Data fine tappa
--                            Informazioni su Pasto/Alloggio/Trasporto/Navigazione/Vitto gratuito/Alloggio gratuito/Vitto e Alloggio gratuito = Quelli della tappa
--                            Area geografica = a quella della tappa o '*' (Indefinita)
--                            Nazione = a quella della tappa o 0 (indefinita)
--                            Inquadramento = a quello della tappa o 0 (Indefinito)
--            Se il numero di ore è minore di 4
--                  Se la durata specificata nell'abbattimento = '<4'
--                        La tappa è con abbattimento
--                        La percentuale di abbattimento è quella del dato di abbattimento corrente
--            Altrimenti se il numero di ore = 4
--                  Se la durata specificata nell'abbattimento = '>4'
--                        La tappa è con abbattimento
--                        La percentuale di abbattimento è quella del dato di abbattimento corrente
--            Altrimenti se il numero di ore > 4  e il numero di ore < 8
--                  Se la durata specificata nell'abbattimento = '>4'
--                        La tappa è con abbattimento
--                        La percentuale di abbattimento è quella del dato di abbattimento corrente
--            Altrimenti se il numero di ore = 8
--                  Se la durata specificata nell'abbattimento = '>4'
--                        La tappa è con abbattimento
--                        La percentuale di abbattimento è quella del dato di abbattimento corrente
--            Altrimenti se il numero di ore > 8
--                  Se la durata specificata nell'abbattimento = '>8'
--                        La tappa è con abbattimento
--                        La percentuale di abbattimento è quella del dato di abbattimento corrente
--
-- Viene calcolata la diaria. Se la tappa è in comune altro non è possibile che la quota esente sia inferiore all'importo della diaria (calcolaDiaria)
--   Per ogni tappa esistente nella missione in processo:
--          Viene lettura la diaria massima da applicare alla tappa. Se, per la tappa è senza diaria questa è posta = 0.
--          Negli altri casi si operano i seguenti calcoli:
--           1) Si determina il valore della diaria lorda per le ore effettive di durata della missione in
--              data tappa con la seguente formula:
--                              Arrotonda alla seconda cifra decimale((diaria lorda / 24) *numero ore tappa)
--           2) Si determina l'importo dela diaria netta considerando la percentuale di abbattimento
--              recuperata per ogni singola tappa con la seguente formula:
--                               Arrotonda alla seconda cifra decimale(Diaria lorda - (Diaria lorda * Percentuale abbattimento / 100))
--           3) Si abbatte allo stesso modo della diaria la quota esente.
--           4) Se la tappa della missione è in Italia e comune proprio viene azzerata la quota esente.
--               Se la quota esente è maggiore della diaria netta si rende uguale a quest'ultima.
--
-- Al termine vengono cancellate le righe di diaria preesistenti nella missione ed inserimento di quelle calcolate
--
-- Parametri:
--     inCdsMissione -> Codice del cds della missione
--     inUoMissione -> Codice Uo della missione
--     inEsercizioMissione -> Esercizio Missione
--     inPgMissione -> Progressivo della missione

   PROCEDURE elaboraMissioneDiaria
      (
       inCdsMissione MISSIONE.cd_cds%TYPE,
       inUoMissione MISSIONE.cd_unita_organizzativa%TYPE,
       inEsercizioMissione MISSIONE.esercizio%TYPE,
       inPgMissione MISSIONE.pg_missione%TYPE
      );

-- Valorizzazione di importo lordo e quota esente in caso di generazione di un compenso
--
-- pre-post-name: Entità anagrafica associata a terzo non trovata
-- pre: Non viene trovata l'entità anagrafica a cui il terzo della missione si riferisce
-- post: Viene sollevata un'eccezione
--
-- pre-post-name: Inquadramento soggetto anagrafico non trovato
-- pre: Inquadramento valido non trovato per il terzo della missione alla data di inizio della missione
-- post: Viene sollevata un'eccezione
--
-- pre-post-name: Lettura di una missione esistente e calcolo dell'importo lordo e della quota esente in caso
--                di generazione di un compenso da missione
-- pre:           Nesun'altra precondizione verificata
-- post:
--       Vengono letti i dati di base della missione (metodo getDatiBaseMissione) come segue:
--            Lettura della testata della missione
--            Recupero dei dati dell'anagrafico associato al terzo della missione
--            Lettura dati base dell'inquadramento:
--            Lettura della quota esente Italia/Estero
--                 Se la missione è in comune proprio o altro comune italiano
--                      Viene recuperata la quota esente per comune ITALIA ('I') valida alla data di inizio missione
--                 Se la missione è in comune estero
--                      Viene recuperata la quota esente per comune ESTERO('E') valida alla data di inizio missione
--
--       Si attiva il calcolo dell'importo complessivo di importo lordo e quota esente portati dalla missione
--            Si azzerano i valori di importo lordo e quota esente distinti per destinazione (comune proprio, altro o estero)
--            Vengono lette le tappe componenti la missione. Per ogni tappa esistente sono letti dettagli di spesa e diaria
--            Dettaglio di spesa
--                 Si scartano le spese di tipo anticipato
--                 Tappa in comune proprio
--                      Se la spesa è di tipo trasporto
--                            L'importo della spesa è sommato all'importo lordo ed alla quota esente
--                      Altrimenti l'importo della spesa è sommato all'importo lordo
--                 Altrimenti l'importo della spesa è sommato all'importo lordo ed alla quota esente
--            Dettaglio di diaria
--                 L'importo netto della diaria (dopo abbattimento) è sommato all'importo lordo.
--                 L'importo della quota esente è sommato all'importo quota esente.
--            L'importo lordo e quota esente sono sommati ai rispettivi contatori distinti per destinazione
--            (comune proprio, altro o estero)
--
--       Si procede al calcolo dell'importo complessivo di importo lordo e quota esente portati dalla missione
--            Se la missione non ha tappe in comune estero
--                 L'importo lordo del compenso è la sommatoria del corrispondente valore in comune proprio o altro
--                 La quota esente del compenso è la sommatoria del corrispondente valore in comune proprio o altro
--            Se la missione ha tappe in comune estero
--                 Si lordizza l'importo lordo totalizzato dalle tappe in comune estero
--                      Sono recuperati i montanti per l'anagrafico di riferimento al terzo della missione
--                      Si recupera il tipo di trattamento definito per la missione
--                      Si estraggono tutti i cori presenti nel trattamento che hanno a vero l'indicatore di
--                      uso nella formula di lordizzazione
--                      Per i tipi contributo ritenuta estratti si determina il corrispondente montante di riferimento
--                      Missione di soggetti dipendente
--                           Si accede agli scaglioni con il montante fiscale e previdenziale per il recupero
--                           delle aliquote di riferimento
--                      Missione di soggetti altri. Casi particolari
--                           Se per l'anagrafico è valorizzato l'indicatore di uso di una aliquota fiscale
--                                Si accede agli scaglioni con l'aliquota fiscale e con il montante previdenziale
--                                per il recupero delle aliquote di riferimento. L'accesso con l'aliquota fiscale
--                                è necessario per verificare che tale scaglione sia presente nel sistema. Se
--                                non è trovato alcun record si torna l'aliquota corrispondente allo scaglione più basso.
--                                Il recupero delle aliquote previdenziali è standard
--                           Se il trattamento prevede annualizzazione.
--                                Si parte dall'importo netto della missione (solo per la parte estera), lo si divide
--                                per il numero di giorni della rata (anno commerciale) e si moltiplica per 360 e si
--                                determina l'imponibile annualizzato.
--                                Su questo importo si applicano gli scaglioni IRPEF. Si entra con importo 0 fino alla
--                                concorrenza dell'importo annualizzato.
--                                Si sommano i valori IRPEF ottenuti dall'applicazione delle diverse aliquote nei diversi
--                                scaglioni.
--                                Il valore di IRPEF così trovato è moltiplicato per cento e diviso per l'imponibile
--                                annualizzato ottenendo così l'aliquota irpef da usare nella determinazione del
--                                coefficente di lordizzazione.
--                                Il recupero delle aliquote previdenziali è standard
--                           Altrimenti si accede agli scaglioni con il montante fiscale e previdenziale per il recupero
--                           delle aliquote di riferimento
--                      Si applica il coefficente di lordizzazione all'importo netto della diaria (- abbattimento - quota esente).
--                      Si ritorna l'importo lordo come somma della quota esente + importo lordizzato.
--
--                 L'importo lordo del compenso è la sommatoria del corrispondente valore in comune proprio, altro e estero lordizzato
--                 La quota esente del compenso è la sommatoria del corrispondente valore in comune proprio, altro e estero
--
-- Al termine vengono ritornati gli importi lordo compenso e quota esente
--
-- Parametri:
--     inCdsMissione -> Codice del cds della missione
--     inUoMissione -> Codice Uo della missione
--     inEsercizioMissione -> Esercizio Missione
--     inPgMissione -> Progressivo della missione
--     inImportoLordo -> Valore calcolato dell'importo lordo
--     inQuotaEsente -> Valore calcolato della quota esente

   PROCEDURE calcolaImportiCompenso
      (
       inCdsMissione MISSIONE.cd_cds%TYPE,
       inUoMissione MISSIONE.cd_unita_organizzativa%TYPE,
       inEsercizioMissione MISSIONE.esercizio%TYPE,
       inPgMissione MISSIONE.pg_missione%TYPE,
       inImportoLordo IN OUT NUMBER,
       inQuotaEsente IN OUT NUMBER,
       inAliquotaIrpef IN OUT NUMBER
      );

-- Controllo eliminazione della missione
--
-- Esecuzione dei controlli di ammissibilità alla cancellazione logica o fisica di una missione
--
-- pre-post-name: Il missione selezionata risulta essere già stato annullato
-- pre:           Si cerca di eliminare una missione che risulta essere già in stato di annullato
-- post:          Viene sollevata un'eccezione
--
-- pre-post-name: Impossibile eliminare una missione che risulta pagata
-- pre:           Si cerca di eliminare una missione che risulta essere collegata a mandati e reversali
-- post:          Viene sollevata un'eccezione
--
-- pre-post-name: Impossibile eliminare una missione che risulta associata ad una spesa del fondo economale
-- pre:           Si cerca di eliminare una missione che risulta essere associata ad una spesa del fondo economale
-- post:          Viene sollevata un'eccezione
--
-- pre-post-name: Esegui cancellazione di una missione
-- pre:           Viene richiesta la validazione sulla eliminazione di una missione
-- post:
--       Vengono letti i dati di base della missione
--            Se la missione risulta essere annullata (vedi pre-post-name: La missione selezionata risulta essere già
--            in stato annullato).
--            Se la missione risulta essere pagata (vedi pre-post-name: Impossibile eliminare una missione
--            che risulta pagata).

--            Se il compenso è da missione (vedi pre-post-name: I compensi associati a missione possono essere eliminati
--            solo dal pannello di gestione della missione).
--            Se il compenso è da conguaglio e non sono verificate le condizioni di cui sopra allora:
--                 Si procede all'eliminazione dello stesso conguaglio ponendo il compenso abilitato alla cancellazione
--            Se il compenso è da minicarriera e non sono verificate le condizioni di cui sopra allora:
--                 Si procede allo sgancio del compenso dalla minicarriera ed all'aggiornamento dello stato della
--                 stessa in merito all'associazione con i compensi ponendo il compenso abilitato alla cancellazione
--            In tutti i casi la cancellazione è fatta dal client mentre la procedura, abilitando la cancellazione
--            del compenso, definisce anche se questa può essere fisica o solo logica. E' logica quando il compenso
--            risulta essere già staton associato a mandati ora annullati.


   PROCEDURE eseguiDelMissione
      (
       inCdsMissione MISSIONE.cd_cds%TYPE,
       inUoMissione MISSIONE.cd_unita_organizzativa%TYPE,
       inEsercizioMissione MISSIONE.esercizio%TYPE,
       inPgMissione MISSIONE.pg_missione%TYPE,
       statoCancella IN OUT NUMBER
      );

   PROCEDURE elaboraMissioneRimborso
      (
       inCdsMissione MISSIONE.cd_cds%TYPE,
       inUoMissione MISSIONE.cd_unita_organizzativa%TYPE,
       inEsercizioMissione MISSIONE.esercizio%TYPE,
       inPgMissione MISSIONE.pg_missione%TYPE,
       inCdTrattamento MISSIONE.cd_trattamento%TYPE default null
      );
----------------------------------------------------------------------------------------------------
-- ROUTINE COMUNI
----------------------------------------------------------------------------------------------------

-- Lettura dati di base legati alla missione in calcolo; testata missione, anagrafico, base
-- inquadramento e quote esenti

   PROCEDURE getDatiBaseMissione
      (
       inCdsMissione MISSIONE.cd_cds%TYPE,
       inUoMissione MISSIONE.cd_unita_organizzativa%TYPE,
       inEsercizioMissione MISSIONE.esercizio%TYPE,
       inPgMissione MISSIONE.pg_missione%TYPE,
       eseguiLock CHAR
      );

-- Costruzione matrice diaria per tappa

   PROCEDURE creaMatriceTappe;

-- Lettura della tipologie di spese registrate per ogni tappa. Si aggiornano i riferimenti
-- per la ricerca di una entrata in MISSIONE_ABBATTIMENTI

   PROCEDURE leggiSpeseTappa
      (
       aRecMissioneTappa MISSIONE_TAPPA%ROWTYPE,
       flPasto IN OUT CHAR,
       flAlloggio IN OUT CHAR,
       flTrasporto IN OUT CHAR
      );

-- Verifica esistenza di tappe le cui spese non hanno trovato una entrata nella tabella
-- MISSIONE_ABBATTIMENTI di configurazione all'ammissibilità delle stesse

   PROCEDURE verificaConfigSpeseTappe;

-- Esecuzione del calcolo della diaria

   PROCEDURE calcolaDiaria;

-- Inserimento delle righe di diaria in MISSIONE_DETTAGLIO

   PROCEDURE inserisciDiarie;

-- Esecuzione del calcolo del rimborso

   PROCEDURE calcolaRimborso
      (
       inCdTrattamento IN MISSIONE.cd_trattamento%TYPE
      )
;

-- Inserimento delle righe di rimborso in MISSIONE_DETTAGLIO

   PROCEDURE inserisciRimborsi;

-- Lordizzazione importo per missione estera

   PROCEDURE lordizzaImporto
      (
       aInOutImportoLordo IN OUT NUMBER,
       aInOutQuotaEsente IN OUT NUMBER,
       inAliquotaIrpef IN OUT NUMBER,
       aAliquotaFiscaleAnag ANAGRAFICO.aliquota_fiscale%TYPE
      );

-- Ritorna l'aliquota cori per lordizzazione

   FUNCTION getAliquotaLordizza
      (
       isIrpef CHAR,
       aCdContributoRitenuta SCAGLIONE.cd_contributo_ritenuta%TYPE,
       aTiAnagrafico SCAGLIONE.ti_anagrafico%TYPE,
       aDataRif DATE,
       isAnnualizzato CHAR,
       aImportoAccesso IN OUT SCAGLIONE.im_inferiore%TYPE,
       aImportoMaxRif SCAGLIONE.im_inferiore%TYPE,
       aAliquotaAnag IN OUT SCAGLIONE.aliquota%TYPE
      ) RETURN SCAGLIONE.aliquota%TYPE;

END CNRCTB505;
