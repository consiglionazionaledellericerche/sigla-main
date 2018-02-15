--------------------------------------------------------
--  DDL for Package CNRCTB605
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRCTB605" AS
--==================================================================================================
--
-- CNRCTB605 - Calcolo e scrittura rate di minicarriera.
--
-- Date: 12/02/2004
-- Version: 1.12
--
-- Dependency: CNRCTB 080/600 IBMERR 001 IBMULL 001
--
-- History:
--
-- Date: 26/06/2002
-- Version: 1.0
--
-- Creazione Package.
--
-- Date: 23/07/2002
-- Version: 1.1
--
-- Inserita routine di controllo della consistenza delle rate di una minicarriera
--
-- Date: 25/07/2002
-- Version: 1.2
--
-- Fix errori in calcolo rate minicarriere
-- Aggiornata documentazione
--
-- Date: 26/07/2002
-- Version: 1.3
--
-- Fix errori in calcolo rate minicarriere
--
-- Date: 05/08/2002
-- Version: 1.4
--
-- Revisione calcolo rate per minicarriere a tassazione separata
--
-- Date: 08/08/2002
-- Version: 1.5
--
-- Corrette informazioni su dependencies
--
-- Date: 25/09/2002
-- Version: 1.6
--
-- Modificato il lancio della gestione rate a tassazione separata per attivazione da nuovo campo
-- in testata minicarriera fl_tassazione_separata e non dal fatto che la data di inizio della
-- minicarriera è antecedente al primo giorno dell'esercizio di creazione della minicarriera.
-- Inserito il controllo di non accettare, se la minicarriera è a tassazione separata, trattamenti
-- che non presentano l'attributo fl_tassazione_separata = 'Y'.
--
-- Date: 04/12/2002
-- Version: 1.7
--
-- Fix errore interno 2924.
-- L'errore era motivato dal fatto che nei campi dt_inizio_rata, dt_fine_rata e dt_scadenza talvolta
-- erano presenti delle date non troncate. Corretto package per essere sicuro di non inserire mai
-- date non troncate ed eliminato il round con due decimali in calcolo giorni
--
-- Date: 20/12/2002
-- Version: 1.8
--
-- Correzione genera minicarriera da sospesa
--
-- Date: 17/02/2003
-- Version: 1.9
--
-- Fix errore interno 3263, no data found in rinnovo di minicarriera totalmente associata a compensi.
-- In caso di rinnovo o ripristino di minicarriere le cui rate risultano essere totalmente associate
-- a compensi si crea una testata con importo = 0 e date di inizio e fine pari alla data odierna.
--
-- Date: 15/07/2003
-- Version: 1.10
--
-- Eliminato clone minicarriera in sede di calcola rate
--
-- Date: 12/09/2003
-- Version: 1.11
--
-- Inserito controllo esistenza del terzo in area compensi (validazione per cessazione e rapporto)
-- in sede di creazione minicarriera per rinnovo o ripristino. . Fix errore interno 3635.
-- Inserita la valorizzazione della data di registrazione in sede di creazione minicarriera per
-- rinnovo o ripristino.
--
-- Date: 12/02/2004
-- Version: 1.12
--
-- Migliorata gestione per controllo esistenza del terzo in area compensi (validazione per cessazione e rapporto)
-- CNRCTB080.chkEsisteTerzoPerCompenso. Errore CINECA n. 665
--
-- Date: 31/01/2007
-- Version: 1.13
--
-- Modificata la procedura "calcolaRateNormale" per il calcolo delle rate di una minicarriera.
-- Ora le rate, a parte la prima e l'ultima, sono sempre mensili (questo per un corretto calcolo
-- delle detrazioni familiari)
--==================================================================================================
--
-- Constants
--

   dataOdierna DATE;
   dataOdiernaTronca DATE;
   i BINARY_INTEGER;

   -- Testata minicarriera in elaborazione

   aRecMinicarriera MINICARRIERA%ROWTYPE;

   -- Dettaglio rate minicarriera in elaborazione

   aRecMCarrieraRata MINICARRIERA_RATA%ROWTYPE;

   -- Dichiarazioni tabelle PL/SQL

   matriceRate_tab CNRCTB600.rateTab;

   -- Dichiarazione di un cursore generico

   TYPE GenericCurTyp IS REF CURSOR;

--
-- Functions e Procedures
--

----------------------------------------------------------------------------------------------------
-- MAIN PROCEDURE
----------------------------------------------------------------------------------------------------
--
-- Creazione automatica rate di una minicarriera esistente
--
-- creaRateMinicarriera
--
-- pre-post-name: Non esiste la minicarriera in elaborazione
-- pre:           Non è stata trovata la minicarriera per la quale generare le rate
-- post:          Viene sollevata un'eccezione
--
-- pre-post-name: Creazione rate di una minicarriera
-- pre:           Viene richiesta la generazione delle rate a fronte dell'inserimento o modifica di
--                una minicarriera. Nessun'altra precondizione verificata
-- post:
--       Vengono letti i dati di base della testata di una minicarriera
--       Si procede con la funzione di calcola rate. Si distingue
--            La minicarriera presenta l'attributo fl_tassazione_separata = N. Non è più rilevante l'indicazione
--            di una data di inizio della minicarriera contenuta o futura rispetto all'esercizio di creazione della
--            stessa minicarriera.
--            Minicarriera no tassazione separata
--                 Si determina la tipologia delle rate che andranno a generarsi (mensili o no). Si considera
--                 una minicarriera a rate mensili quando:
--                      Le date di inizio e fine di una minicarriera coincidono con un inizio e fine mese.
--                      Le rate di inizio e fine di una minicarriera hanno, indipendentemente da mese e anno, la
--                      distanza di un giorno. Si tiene conto del fatto che i mesi non sono omogenei per data di fine.
--                      In ogni caso il numero delle rate indicate in testata della monicarriera coincide con quelle
--                      calcolate.
--                 Si determina la data minima della scadenza delle rate.
--                      Se le rate sono mensili
--                           Corrisponde al 20 del mese della data di sistema della creazione della minicarriera (non
--                           conta la data di registrazione. Se la data di sistema ha giorno > di 20 allora si
--                           sposta al giorno 20 del mese successivo.
--                      Altrimenti
--                           Corrisponde alla data di sistema della creazione della monicarriera.
--                 Si determina se la minicarriera prevede anticipo o posticipo delle date di scadenza rate.
--                 Le rate sono mensili.
--                      Se le date della minicarriera sono rispettivamente un inizio e fine mese si indica che sono
--                      anche di tipo mese
--                      Si calcolano le rate. Regole:
--                          IMPORTO: importo totale della minicarriera diviso il numero delle rate
--                          DATA INZIO: per la prima rata sarà la data di inizio della minicarriera, per le altre rate
--                          sarà il giorno successivo alla data di fine della rata precedente
--                          DATA FINE:
--                          se la minicarriera è di tipo mese
--                               La data di fine della rata sarà sempre l¿ultimo giorno dello stesso mese
--                          Altrimenti
--                               La data di fine dovrà essere indicata al giorno precedente del mese successivo.
--                          DATA DI SCADENZA:
--                          No anticipo posticipo
--                               sarà uguale al 20 del mese della data di fine della rata se maggiore della data
--                               minima di scadenza rate altrimenti è pari alla stessa data minima di scadenza.
--                          Si anticipo posticipo.
--                               Calcolo come sopra ma prima
--                                     Si aggiunge alla data di fine della rata il numero di mesi indicati se posticipata meno 1
--                                     Si sottrae il numero dei mesi indicati se anticipata
--                 Le rate non sono mensili.
--                      Valgono le regole sopra indicate tenuto conto che il calcolo degli intervalli dovrà essere
--                      determinato come media delle rate rispetto al periodo della minicarriera
--                      Arrotondamento alla seconda cifra decimale del numero di giorni del periodo della minicarriera
--                      diviso il numero delle rate.
--            La minicarriera presenta l'attributo fl_tassazione_separata = Y. In questo caso almeno la data di inizio
--            della minicarriera è precedente all'esercizio di creazione della stessa minicarriera
--            Minicarriera si tassazione separata.
--                 Si determina la tipologia delle rate che andranno a generarsi (mensili o no). Si considera
--                 una minicarriera a rate mensili quando:
--                      Valgono le regole per le rate di minicarriera non a tassazione separata ma occorre tenere
--                      conto che tutto quanto è precedente all'esercizio finisce in una unica rata.
--                 Si determina la data minima della scadenza delle rate.
--                      Valgono le regole per le rate della minicarriera non a tassazione separata.
--                 Si determina se la minicarriera prevede anticipo o posticipo delle date di scadenza rate.
--                 Le rate sono mensili.
--                      Se le date della minicarriera sono rispettivamente un inizio e fine mese si indica che sono
--                      anche di tipo mese
--                      Si calcolano le rate. Regole:
--                          In tutti i calcoli è fatto riferimento all'esercizio commerciale (360 giorni, ogni mese è
--                          di 30 gg).
--                          DATA DI SCADENZA. Valgolo le regole della minicarriera non a tassazione separata.
--                          No anticipo posticipo
--                               IMPORTO E DATA INIZIO DATA FINE. La prima rata andrà dalla data di inizio minicarriera
--                               al 31/12 dell¿anno precedente. Occorre determinare il numero di giorni che intercorre
--                               in questo periodo (utilizzando l¿anno commerciale).
--                               L'importo della prima rata sarà dato dall'importo totale della borsa diviso i giorni e
--                               moltiplicato per il numero di giorni della rata;
--                               La seconda rata andrà dal 1 di gennaio dell'anno in scrivania al 31 gennaio se la
--                               minicarriera è di tipo mese, o al giorno precedente quello indicato come data inizio
--                               minicarriera nel mese di febbraio se iniziava in un giorno diverso dal 1°.
--                               L'importo della rata sarà dato dal totale della minicarriera diviso i giorni complessivi
--                               per i giorni della rata;
--                               Per le rate successive si dovrà determinare l¿importo rimanente della minicarriera e
--                               dividerlo per il numero delle rate rimanenti (= numero totale rate ¿ 2), per trovare
--                               l¿importo di ogni rata.
--                               Data di inizio e di fine per le rate successive alla seconda verranno determinate con
--                               le regole ordinarie definite per la minicarriera non a tassazione separata.
--                          Si anticipo posticipo
--                               Se anticipo
--                                  IMPORTO E DATA INIZIO DATA FINE. La prima rata andrà dalla data di inizio minicarriera
--                                  al 31/12 dell¿anno precedente. Occorre determinare il numero di giorni che intercorre
--                                  in questo periodo (utilizzando l¿anno commerciale).
--                                  L'importo della prima rata sarà dato dall'importo totale della borsa diviso i giorni e
--                                  moltiplicato per il numero di giorni della rata;
--	                            La seconda rata partirà dal 1 di gennaio dell'anno in scrivania e avrà come data di fine,
--                                  la data di fine rata determinata normalmente più i mesi di anticipo.
--                                  L'importo della rata sarà dato dal totale della minicarriera diviso i giorni complessivi
--                                  per i giorni della rata.
--                                  Per le rate successive si dovrà determinare l¿importo rimanente della minicarriera e
--                                  dividerlo per il numero delle rate rimanenti (= numero totale rate ¿ 2), per trovare
--                                  l¿importo di ogni rata.
--                                  Data di inizio e di fine per le rate successive alla seconda verranno determinate con
--                                  le regole ordinarie definite per la minicarriera non a tassazione separata.
--                               Se posticipo
--                                  IMPORTO, DATA INIZIO E DATA FINE. La prima rata avrà come data di inizio, la data
--                                  di inizio della minicarriera e come data di fine la data di creazione a sistema
--                                  della minicarriera meno i mesi di posticipo. Tale data sarà posta a fine mese se
--                                  la minicarriera inizia il primo del mese o al giorno precedente la data di inizio
--                                  se la minicarriera iniziava in data diversa dal primo. Se la data fine ottenuta è
--                                  successiva al 31/12 dell¿anno precedente la prima rata finirà comunque al 31/12.
--                                  Occorre determinare il numero di giorni che intercorre in questo periodo (utilizzando
--                                  l'anno commerciale).
--                                  L'importo della prima rata sarà dato dall'importo totale della borsa diviso i giorni e
--                                  moltiplicato per il numero di giorni della rata;
--	                            La seconda rata partirà dal giorno successivo alla data di fine della prima rata e
--                                  avrà come data di fine, la fine del mese di inizio della rata se la minicarriera
--                                  iniziava il primo del mese, o il giorno precedente della data di inizio se la
--                                  minicarriera iniziava in data diversa dal primo, ma nel mese successivo.
--                                  Per le rate successive si dovrà determinare l¿importo rimanente della minicarriera e
--                                  dividerlo per il numero delle rate rimanenti (= numero totale rate ¿ 2), per trovare
--                                  l¿importo di ogni rata.
--                                  Data di inizio e di fine per le rate successive alla seconda verranno determinate con
--                                  le regole ordinarie definite per la minicarriera non a tassazione separata.
--       Si scrivono le rate calcolate.
--
-- Parametri:
-- inCdsMCarriera -> Cds della minicarriera
-- inUoMCarriera -> Codice UO della minicarriera
-- inEsercizioMCarriera -> Esercizio della minicarriera
-- inPgMCarriera -> Progressivo della minicarriera
-- inCopiaCdsMCarriera -> Cds della minicarriera "copia"
-- inCopiaUoMCarriera -> Codice UO della minicarriera "copia"
-- inCopiaEsercizioMCarriera -> Esercizio della minicarriera "copia"
-- inCopiaPgMCarriera -> Progressivo della minicarriera "copia"


   PROCEDURE creaRateMinicarriera
      (
       inCdsMCarriera MINICARRIERA.cd_cds%TYPE,
       inUoMCarriera MINICARRIERA.cd_unita_organizzativa%TYPE,
       inEseMCarriera MINICARRIERA.esercizio%TYPE,
       inPgMCarriera MINICARRIERA.pg_minicarriera%TYPE,
       inCopiaCdsMCarriera MINICARRIERA.cd_cds%TYPE,
       inCopiaUoMCarriera MINICARRIERA.cd_unita_organizzativa%TYPE,
       inCopiaEseMCarriera MINICARRIERA.esercizio%TYPE,
       inCopiaPgMCarriera MINICARRIERA.pg_minicarriera%TYPE
      );


-- Esecuzione della copia di una minicarriera
--
-- copiaMinicarriera
--
-- pre-post-name: Non esiste la minicarriera in elaborazione
-- pre:           Non è stata trovata la minicarriera per la quale eseguire la copia
-- post:          Viene sollevata un'eccezione

-- pre-post-name: Esecuzione della copia di una minicarriera
-- pre:           Viene richiesta la generazione di una copia di una minicarriera esistente.
--                Nessun'altra precondizione verificata
-- post:
--       Esecuzione della copia di una minicarriera esistente.
--            Se il progressivo di copia è un numero negativo si tratta della generazione del clone in caso di
--            inserimento e/o modifica.
--            Se il progressivo di copia è positivo si tratta della generazione della copia della
--            minicarriera per la gestione del ripristino o rinnovo.
--            Ripristino
--                 Quando da una minicarriera sospesa viene indicata la data di ripristino si genera una nuova minicarriera,
--                 ereditando i seguenti dati dalla precedente:
--                      dati del percipiente, tipo compenso.
--                 Inoltre il sistema indicherà come importo totale la somma degli importi delle rate ancora da liquidare
--                 della minicarriera precedente. Lo stato della nuova minicarriera sarà A. Le date sono modificabili.
--                 Si genera una relazione tra la minicarriera ripristinata e quella nuova da questa derivata.
--            Rinnovo
--                 Quando da una minicarriera attiva o ripristinata viene indicata la data di rinnovo si genera una
--                 nuova minicarriera ereditando i seguenti dati dalla precedente:
--                      dati del percipiente, tipo compenso.
--                 Lo stato della nuova minicarriera sarà A.
--                 Si genera una relazione tra la minicarriera ripristinata e quella nuova da questa derivata.
--
-- Parametri:
-- inCdsMCarriera -> Cds della minicarriera
-- inUoMCarriera -> Codice UO della minicarriera
-- inEsercizioMCarriera -> Esercizio della minicarriera
-- inPgMCarriera -> Progressivo della minicarriera
-- inCopiaCdsMCarriera -> Cds della minicarriera "copia"
-- inCopiaUoMCarriera -> Codice UO della minicarriera "copia"
-- inCopiaEsercizioMCarriera -> Esercizio della minicarriera "copia"
-- inCopiaPgMCarriera -> Progressivo della minicarriera "copia"


   PROCEDURE copiaMinicarriera
      (
       inCdsMCarriera MINICARRIERA.cd_cds%TYPE,
       inUoMCarriera MINICARRIERA.cd_unita_organizzativa%TYPE,
       inEseMCarriera MINICARRIERA.esercizio%TYPE,
       inPgMCarriera MINICARRIERA.pg_minicarriera%TYPE,
       inCopiaCdsMCarriera MINICARRIERA.cd_cds%TYPE,
       inCopiaUoMCarriera MINICARRIERA.cd_unita_organizzativa%TYPE,
       inCopiaEseMCarriera MINICARRIERA.esercizio%TYPE,
       inCopiaPgMCarriera MINICARRIERA.pg_minicarriera%TYPE
      );


-- Controllo congruenza delle scadenze nelle rate di una minicarriera
--
-- chkScadRateMinicarriera
--
-- pre-post-name: Data inizio prima rata errata
-- pre:           La data di inizio della prima rata non corrisponde alla data inizio della minicarriera
-- post:          Viene sollevata un'eccezione
--
-- pre-post-name: Data fine ultima rata errata
-- pre:           La data di fine dell'ultima rata non corrisponde alla data fine della minicarriera
-- post:          Viene sollevata un'eccezione
--
-- pre-post-name: Sequenza rate errata
-- pre:           Date di inizio e fine rata non contigue o con intersezioni.
--                Date scadenza non in sequenza.
-- post:          Viene sollevata un'eccezione
--
-- pre-post-name: Numero rate errato
-- pre:           Il numero delle rate della minicarriera non corrisponde a quanto indicato in testata
--                della stessa
-- post:          Viene sollevata un'eccezione
--
-- pre-post-name: Importo totale rate errato
-- pre:           La sommatoria degli importi delle rate della minicarriera non corrisponde a quanto
--                indicato in testata della stessa
-- post:          Viene sollevata un'eccezione
--
-- Parametri:
-- inCdsMCarriera -> Cds della minicarriera
-- inUoMCarriera -> Codice UO della minicarriera
-- inEsercizioMCarriera -> Esercizio della minicarriera
-- inPgMCarriera -> Progressivo della minicarriera


   PROCEDURE chkScadRateMinicarriera
      (
       inCdsMCarriera MINICARRIERA.cd_cds%TYPE,
       inUoMCarriera MINICARRIERA.cd_unita_organizzativa%TYPE,
       inEseMCarriera MINICARRIERA.esercizio%TYPE,
       inPgMCarriera MINICARRIERA.pg_minicarriera%TYPE
      );

----------------------------------------------------------------------------------------------------
-- ROUTINE COMUNI
----------------------------------------------------------------------------------------------------

-- Lettura dati di una minicarriera

   PROCEDURE leggiDatiBaseMinicarriera
      (
       aCdCds MINICARRIERA.cd_cds%TYPE,
       aCdUo MINICARRIERA.cd_unita_organizzativa%TYPE,
       aEsercizio MINICARRIERA.esercizio%TYPE,
       aPgMinicarriera MINICARRIERA.pg_minicarriera%TYPE,
       eseguiLock CHAR
      );

-- Routine principale di calcolo rate

   PROCEDURE calcolaRate;

-- Calcolo delle rate di una minicarriera con data inizio nell'esercizio di creazione o nel futuro

   PROCEDURE calcolaRateNormale;

-- Calcolo delle rate di una minicarriera con data inizio anteriore all'esercizio di creazione

   PROCEDURE calcolaRateTassazioneSep;

-- Inserimento delle rate di minicarriera calcolate

   PROCEDURE scriviRate;

END CNRCTB605;
