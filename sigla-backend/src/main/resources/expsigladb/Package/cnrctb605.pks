CREATE OR REPLACE PACKAGE CNRCTB605 AS
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
-- minicarriera ? antecedente al primo giorno dell'esercizio di creazione della minicarriera.
-- Inserito il controllo di non accettare, se la minicarriera ? a tassazione separata, trattamenti
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
-- pre:           Non ? stata trovata la minicarriera per la quale generare le rate
-- post:          Viene sollevata un'eccezione
--
-- pre-post-name: Creazione rate di una minicarriera
-- pre:           Viene richiesta la generazione delle rate a fronte dell'inserimento o modifica di
--                una minicarriera. Nessun'altra precondizione verificata
-- post:
--       Vengono letti i dati di base della testata di una minicarriera
--       Si procede con la funzione di calcola rate. Si distingue
--            La minicarriera presenta l'attributo fl_tassazione_separata = N. Non ? pi? rilevante l'indicazione
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
--                          DATA INZIO: per la prima rata sar? la data di inizio della minicarriera, per le altre rate
--                          sar? il giorno successivo alla data di fine della rata precedente
--                          DATA FINE:
--                          se la minicarriera ? di tipo mese
--                               La data di fine della rata sar? sempre l?ultimo giorno dello stesso mese
--                          Altrimenti
--                               La data di fine dovr? essere indicata al giorno precedente del mese successivo.
--                          DATA DI SCADENZA:
--                          No anticipo posticipo
--                               sar? uguale al 20 del mese della data di fine della rata se maggiore della data
--                               minima di scadenza rate altrimenti ? pari alla stessa data minima di scadenza.
--                          Si anticipo posticipo.
--                               Calcolo come sopra ma prima
--                                     Si aggiunge alla data di fine della rata il numero di mesi indicati se posticipata meno 1
--                                     Si sottrae il numero dei mesi indicati se anticipata
--                 Le rate non sono mensili.
--                      Valgono le regole sopra indicate tenuto conto che il calcolo degli intervalli dovr? essere
--                      determinato come media delle rate rispetto al periodo della minicarriera
--                      Arrotondamento alla seconda cifra decimale del numero di giorni del periodo della minicarriera
--                      diviso il numero delle rate.
--            La minicarriera presenta l'attributo fl_tassazione_separata = Y. In questo caso almeno la data di inizio
--            della minicarriera ? precedente all'esercizio di creazione della stessa minicarriera
--            Minicarriera si tassazione separata.
--                 Si determina la tipologia delle rate che andranno a generarsi (mensili o no). Si considera
--                 una minicarriera a rate mensili quando:
--                      Valgono le regole per le rate di minicarriera non a tassazione separata ma occorre tenere
--                      conto che tutto quanto ? precedente all'esercizio finisce in una unica rata.
--                 Si determina la data minima della scadenza delle rate.
--                      Valgono le regole per le rate della minicarriera non a tassazione separata.
--                 Si determina se la minicarriera prevede anticipo o posticipo delle date di scadenza rate.
--                 Le rate sono mensili.
--                      Se le date della minicarriera sono rispettivamente un inizio e fine mese si indica che sono
--                      anche di tipo mese
--                      Si calcolano le rate. Regole:
--                          In tutti i calcoli ? fatto riferimento all'esercizio commerciale (360 giorni, ogni mese ?
--                          di 30 gg).
--                          DATA DI SCADENZA. Valgolo le regole della minicarriera non a tassazione separata.
--                          No anticipo posticipo
--                               IMPORTO E DATA INIZIO DATA FINE. La prima rata andr? dalla data di inizio minicarriera
--                               al 31/12 dell?anno precedente. Occorre determinare il numero di giorni che intercorre
--                               in questo periodo (utilizzando l?anno commerciale).
--                               L'importo della prima rata sar? dato dall'importo totale della borsa diviso i giorni e
--                               moltiplicato per il numero di giorni della rata;
--                               La seconda rata andr? dal 1 di gennaio dell'anno in scrivania al 31 gennaio se la
--                               minicarriera ? di tipo mese, o al giorno precedente quello indicato come data inizio
--                               minicarriera nel mese di febbraio se iniziava in un giorno diverso dal 1°.
--                               L'importo della rata sar? dato dal totale della minicarriera diviso i giorni complessivi
--                               per i giorni della rata;
--                               Per le rate successive si dovr? determinare l?importo rimanente della minicarriera e
--                               dividerlo per il numero delle rate rimanenti (= numero totale rate ? 2), per trovare
--                               l?importo di ogni rata.
--                               Data di inizio e di fine per le rate successive alla seconda verranno determinate con
--                               le regole ordinarie definite per la minicarriera non a tassazione separata.
--                          Si anticipo posticipo
--                               Se anticipo
--                                  IMPORTO E DATA INIZIO DATA FINE. La prima rata andr? dalla data di inizio minicarriera
--                                  al 31/12 dell?anno precedente. Occorre determinare il numero di giorni che intercorre
--                                  in questo periodo (utilizzando l?anno commerciale).
--                                  L'importo della prima rata sar? dato dall'importo totale della borsa diviso i giorni e
--                                  moltiplicato per il numero di giorni della rata;
--	                            La seconda rata partir? dal 1 di gennaio dell'anno in scrivania e avr? come data di fine,
--                                  la data di fine rata determinata normalmente pi? i mesi di anticipo.
--                                  L'importo della rata sar? dato dal totale della minicarriera diviso i giorni complessivi
--                                  per i giorni della rata.
--                                  Per le rate successive si dovr? determinare l?importo rimanente della minicarriera e
--                                  dividerlo per il numero delle rate rimanenti (= numero totale rate ? 2), per trovare
--                                  l?importo di ogni rata.
--                                  Data di inizio e di fine per le rate successive alla seconda verranno determinate con
--                                  le regole ordinarie definite per la minicarriera non a tassazione separata.
--                               Se posticipo
--                                  IMPORTO, DATA INIZIO E DATA FINE. La prima rata avr? come data di inizio, la data
--                                  di inizio della minicarriera e come data di fine la data di creazione a sistema
--                                  della minicarriera meno i mesi di posticipo. Tale data sar? posta a fine mese se
--                                  la minicarriera inizia il primo del mese o al giorno precedente la data di inizio
--                                  se la minicarriera iniziava in data diversa dal primo. Se la data fine ottenuta ?
--                                  successiva al 31/12 dell?anno precedente la prima rata finir? comunque al 31/12.
--                                  Occorre determinare il numero di giorni che intercorre in questo periodo (utilizzando
--                                  l'anno commerciale).
--                                  L'importo della prima rata sar? dato dall'importo totale della borsa diviso i giorni e
--                                  moltiplicato per il numero di giorni della rata;
--	                            La seconda rata partir? dal giorno successivo alla data di fine della prima rata e
--                                  avr? come data di fine, la fine del mese di inizio della rata se la minicarriera
--                                  iniziava il primo del mese, o il giorno precedente della data di inizio se la
--                                  minicarriera iniziava in data diversa dal primo, ma nel mese successivo.
--                                  Per le rate successive si dovr? determinare l?importo rimanente della minicarriera e
--                                  dividerlo per il numero delle rate rimanenti (= numero totale rate ? 2), per trovare
--                                  l?importo di ogni rata.
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
-- pre:           Non ? stata trovata la minicarriera per la quale eseguire la copia
-- post:          Viene sollevata un'eccezione

-- pre-post-name: Esecuzione della copia di una minicarriera
-- pre:           Viene richiesta la generazione di una copia di una minicarriera esistente.
--                Nessun'altra precondizione verificata
-- post:
--       Esecuzione della copia di una minicarriera esistente.
--            Se il progressivo di copia ? un numero negativo si tratta della generazione del clone in caso di
--            inserimento e/o modifica.
--            Se il progressivo di copia ? positivo si tratta della generazione della copia della
--            minicarriera per la gestione del ripristino o rinnovo.
--            Ripristino
--                 Quando da una minicarriera sospesa viene indicata la data di ripristino si genera una nuova minicarriera,
--                 ereditando i seguenti dati dalla precedente:
--                      dati del percipiente, tipo compenso.
--                 Inoltre il sistema indicher? come importo totale la somma degli importi delle rate ancora da liquidare
--                 della minicarriera precedente. Lo stato della nuova minicarriera sar? A. Le date sono modificabili.
--                 Si genera una relazione tra la minicarriera ripristinata e quella nuova da questa derivata.
--            Rinnovo
--                 Quando da una minicarriera attiva o ripristinata viene indicata la data di rinnovo si genera una
--                 nuova minicarriera ereditando i seguenti dati dalla precedente:
--                      dati del percipiente, tipo compenso.
--                 Lo stato della nuova minicarriera sar? A.
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
/


CREATE OR REPLACE PACKAGE BODY CNRCTB605 AS

--==================================================================================================
-- Lettura di una missione esistente, verifica ammissibilit? delle spese registrate e calcolo delle diarie
--==================================================================================================
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
   ) IS
   eseguiLock CHAR(1);

BEGIN

   -------------------------------------------------------------------------------------------------
   -- Memorizzazione parametri generali della procedura

   dataOdierna:=sysdate;
   dataOdiernaTronca:=TRUNC(sysdate);
   eseguiLock:='Y';

   -------------------------------------------------------------------------------------------------
   -- Lettura della minicarriera (testata della minicarriera in elaborazione)

   leggiDatiBaseMinicarriera(inCdsMCarriera,
                             inUoMCarriera,
                             inEseMCarriera,
                             inPgMCarriera,
                             eseguiLock);

   -------------------------------------------------------------------------------------------------
   -- Calcolo  e scrittura delle rate

   calcolaRate;
   scriviRate;

END creaRateMinicarriera;

--==================================================================================================
-- Lettura di una minicaroera
--==================================================================================================
PROCEDURE leggiDatiBaseMinicarriera
   (
    aCdCds MINICARRIERA.cd_cds%TYPE,
    aCdUo MINICARRIERA.cd_unita_organizzativa%TYPE,
    aEsercizio MINICARRIERA.esercizio%TYPE,
    aPgMinicarriera MINICARRIERA.pg_minicarriera%TYPE,
    eseguiLock CHAR
   ) IS

BEGIN

   aRecMinicarriera:=CNRCTB600.getMinicarriera(aCdCds,
                                               aCdUo,
                                               aEsercizio,
                                               aPgMinicarriera,
                                               eseguiLock);

END leggiDatiBaseMinicarriera;


-- ==================================================================================================
-- Routine principale di calcolo delle rate
-- ==================================================================================================
PROCEDURE calcolaRate
   IS
   aRecTipoTrattamento TIPO_TRATTAMENTO%ROWTYPE;

BEGIN

   --------------------------------------------------------------------------------------------------
   -- Valorizzazione parametri

   -- Azzeramento della matrice rate

   matriceRate_tab.DELETE;

   --------------------------------------------------------------------------------------------------
   -- Distinguo se devo gestire o meno la tassazione separata. Se attivo la tassazione separata il
   -- trattamento selezionato deve essere per tassazione separata

   IF aRecMinicarriera.fl_tassazione_separata = 'Y' THEN
      aRecTipoTrattamento:=CNRCTB545.getTipoTrattamento (aRecMinicarriera.cd_trattamento,
                                                         aRecMinicarriera.dt_registrazione);
      IF aRecTipoTrattamento.fl_tassazione_separata = 'N' THEN
         IBMERR001.RAISE_ERR_GENERICO
            ('La minicarriera in calcolo prevede la gestione a tassazione separata che non ? compatibile ' ||
             'con quanto indicato nel trattamento selezionato');
      END IF;
      calcolaRateTassazioneSep;
   ELSE
      calcolaRateNormale;
   END IF;

END calcolaRate;

-- ==================================================================================================
-- Calcolo delle rate di una minicarriera con data inizio nell'esercizio di creazione o nel futuro
-- ==================================================================================================
PROCEDURE calcolaRateNormale
   IS
   isRataMensile CHAR(1);
   isRataMese CHAR(1);
   aDataMinScadenza DATE;
   aImportoRata NUMBER(15,2);
   aImportoResto NUMBER(15,2);
   --aNumeroGG NUMBER;

   aNumeroGGMiniComm  NUMBER;
   aImportoGGMiniComm NUMBER;
   aNumeroGGPrimaRata  NUMBER;
   aNumeroGGUltimaRata  NUMBER;
BEGIN

   --------------------------------------------------------------------------------------------------
   -- Valorizzazione parametri
   /*viene passato dalla mappa
   numero_rate := ROUND(IBMUTL001.getMonthsBetween(IBMUTL001.getFirstDayOfMonth(aRecMinicarriera.dt_inizio_minicarriera),
                               IBMUTL001.getLastDayOfMonth(aRecMinicarriera.dt_fine_minicarriera)));
   */

   -- Controllo che il numero delle rate trovi capienza nel numero di giorni della minicariera
   IF IBMUTL001.getDaysBetween(aRecMinicarriera.dt_inizio_minicarriera,
                               aRecMinicarriera.dt_fine_minicarriera) < aRecMinicarriera.numero_rate THEN
      IBMERR001.RAISE_ERR_GENERICO
         ('Il numero di rate indicate nella minicarriera ' || aRecMinicarriera.numero_rate ||
          ' non trova capienza nei giorni di durata della stessa ' || IBMUTL001.getDaysBetween(aRecMinicarriera.dt_inizio_minicarriera,
                                                                                               aRecMinicarriera.dt_fine_minicarriera));
   END IF;

   -- Determino la tipologia delle rate che andranno a generarsi. Rate mensili o meno

   isRataMensile:=CNRCTB600.chkRateMensiliNormale(aRecMinicarriera);

   -- Se la rata ? mensile e le date sono di inizio e fine mese allora metto isRataMese = 'Y'. Per ogni
   -- rata nel primo caso la data di fine ? il giorno precedente della data inizio (al limite il fine mese),
   -- nel secondo caso la data di fine ? sempre un fine mese
   -- Determino la data minima della scadenza riferita alla data di creazione (data di sistema).
   -- Rate mensili
   --    Si normalizza al 20 del mese di creazione o di quello successivo se il giorno ? superiore a 20
   --    Si mantiene la data di sistema
   IF isRataMensile = IBMUTL001.isIntervalloDateMensile THEN
      IF IBMUTL001.isDifferenzaMese(aRecMinicarriera.dt_inizio_minicarriera,
                                    aRecMinicarriera.dt_fine_minicarriera) = IBMUTL001.isIntervalloDateMese THEN
         isRataMese:=IBMUTL001.isIntervalloDateMese;
      ELSE
         isRataMese:=IBMUTL001.isNotIntervalloDateMese;
      END IF;
      IF IBMUTL001.getDayOfDate(dataOdiernaTronca) > 20 THEN
         aDataMinScadenza:=TO_DATE('20' || TO_CHAR(IBMUTL001.getAddMonth(IBMUTL001.getFirstDayOfMonth(dataOdiernaTronca),1),'MMYYYY'),'DDMMYYYY');
      ELSE
         aDataMinScadenza:=TO_DATE('20' || TO_CHAR(dataOdiernaTronca,'MMYYYY'),'DDMMYYYY');
      END IF;
   ELSE
      isRataMese:=IBMUTL001.isNotIntervalloDateMese;
      aDataMinScadenza:=dataOdiernaTronca;
   END IF;
   --------------------------------------------------------------------------------------------------
   -- Costruzione della matrice rate

   BEGIN

      FOR i IN 1 .. aRecMinicarriera.numero_rate

      LOOP

         -- Data inizio rata. Data inizio minicarriera o data fine rata precedente + 1

         IF i = 1 THEN
            matriceRate_tab(i).tDtInizio:=aRecMinicarriera.dt_inizio_minicarriera;
         ELSE
            matriceRate_tab(i).tDtInizio:=(matriceRate_tab(i-1).tDtFine + 1);
         END IF;

         -- Data fine rata. Data fine minicarriera o data fine mese

         IF i = aRecMinicarriera.numero_rate THEN
              matriceRate_tab(i).tDtFine:=aRecMinicarriera.dt_fine_minicarriera;
         ELSE
              matriceRate_tab(i).tDtFine:=IBMUTL001.getLastDayOfMonth(matriceRate_tab(i).tDtInizio);
         END IF;

         -- Determino la data di scadenza temporaneo portata al 20 della data fine rata solo se isRataMensile = Y

         IF isRataMensile = 'Y' THEN
            matriceRate_tab(i).tDtScadenza:=TO_DATE('20' || TO_CHAR(matriceRate_tab(i).tDtFine,'MMYYYY'),'DDMMYYYY');
         ELSE
            matriceRate_tab(i).tDtScadenza:=matriceRate_tab(i).tDtFine;
         END IF;

         -- Determino la data di scadenza in base ai mesi di anticipo e posticipo.
         -- Se posticipo data di fine rata + mesi posticipo -1.
         -- Se anticipo data di fine rata - mesi anticipo

         IF    aRecMinicarriera.ti_anticipo_posticipo = 'A' THEN
               matriceRate_tab(i).tDtScadenza:=IBMUTL001.getAddMonth(matriceRate_tab(i).tDtScadenza,
                                                                     (aRecMinicarriera.mesi_anticipo_posticipo * -1));
         ELSIF aRecMinicarriera.ti_anticipo_posticipo = 'P' THEN
               matriceRate_tab(i).tDtScadenza:=IBMUTL001.getAddMonth(matriceRate_tab(i).tDtScadenza,
                                                                     (aRecMinicarriera.mesi_anticipo_posticipo - 1));
         END IF;

         -- Normalizzo sempre la data di scadenza calcolata alla data minima di scadenza se quest'ultima ? maggiore
         IF matriceRate_tab(i).tDtScadenza < aDataMinScadenza THEN
            matriceRate_tab(i).tDtScadenza:=aDataMinScadenza;
         END IF;
      END LOOP;

      aNumeroGGMiniComm := 0;
      aNumeroGGPrimaRata:=0;
      aNumeroGGUltimaRata:=0;

      --Calcolo il numero di gg della minicarriera (rapportati a 30)
      For i IN 1 .. aRecMinicarriera.numero_rate  Loop
           If (IBMUTL001.getDayOfDate(matriceRate_tab(i).tDtInizio) = IBMUTL001.getDayFirstDayOfMonth(matriceRate_tab(i).tDtInizio))
              And
              (IBMUTL001.getDayOfDate(matriceRate_tab(i).tDtFine) = IBMUTL001.getDayLastDayOfMonth(matriceRate_tab(i).tDtFine)) Then

	         aNumeroGGMiniComm := aNumeroGGMiniComm + 30;
	         --potrebbe essere anche la prima e/o l'ultima rata
	         If i = 1 Then
	             aNumeroGGPrimaRata := 30;
	         Elsif i = aRecMinicarriera.numero_rate Then
	             aNumeroGGUltimaRata := 30;
	         End If;
	   Elsif (IBMUTL001.getDayOfDate(matriceRate_tab(i).tDtInizio) = IBMUTL001.getDayFirstDayOfMonth(matriceRate_tab(i).tDtInizio)) Then
	         -- ultimo mese
	         aNumeroGGUltimaRata := IBMUTL001.getDayOfDate(matriceRate_tab(i).tDtFine);
	         If aNumeroGGUltimaRata = 31 Then
	             aNumeroGGUltimaRata := 30;
	         Elsif IBMUTL001.getMonthOfDate(matriceRate_tab(i).tDtFine) = 2 And
	               aNumeroGGUltimaRata = IBMUTL001.getDayLastDayOfMonth(matriceRate_tab(i).tDtFine) Then
	             aNumeroGGUltimaRata := 30;
	         End If;
	         aNumeroGGMiniComm := aNumeroGGMiniComm + aNumeroGGUltimaRata;
	   Elsif (IBMUTL001.getDayOfDate(matriceRate_tab(i).tDtFine) = IBMUTL001.getDayLastDayOfMonth(matriceRate_tab(i).tDtFine)) Then
	         --primo mese
	         aNumeroGGPrimaRata:=(30 - (IBMUTL001.getDayOfDate(matriceRate_tab(i).tDtInizio)-IBMUTL001.getDayFirstDayOfMonth(matriceRate_tab(i).tDtInizio)));
	         If aNumeroGGPrimaRata = 0 Then
	             aNumeroGGPrimaRata := 1;
	         End If;
	         aNumeroGGMiniComm := aNumeroGGMiniComm + aNumeroGGPrimaRata;
	   Else
	         --ho una minicarriera che dura meno di un mese
	         aNumeroGGMiniComm :=  IBMUTL001.getDayOfDate(matriceRate_tab(i).tDtFine) - IBMUTL001.getDayOfDate(matriceRate_tab(i).tDtInizio) + 1;
	         If aNumeroGGMiniComm = 0 Then
	            aNumeroGGMiniComm := 1;
	         End If;
	         If aNumeroGGMiniComm = 31 Then
	            aNumeroGGMiniComm := 30;
	         End If;
	         aNumeroGGPrimaRata:=aNumeroGGMiniComm;
	         aNumeroGGUltimaRata:=aNumeroGGMiniComm;
	   End If;
      END LOOP;

      aImportoGGMiniComm:=(aRecMinicarriera.im_totale_minicarriera / aNumeroGGMiniComm);
      aImportoResto:=aRecMinicarriera.im_totale_minicarriera;

      FOR i IN 1 .. aRecMinicarriera.numero_rate
      LOOP
	   If i = aRecMinicarriera.numero_rate Then
	       	         matriceRate_tab(i).tImRata:= aImportoResto;
	   Elsif ((IBMUTL001.getDayOfDate(matriceRate_tab(i).tDtInizio) = IBMUTL001.getDayFirstDayOfMonth(matriceRate_tab(i).tDtInizio))
                  And
                  (IBMUTL001.getDayOfDate(matriceRate_tab(i).tDtFine) = IBMUTL001.getDayLastDayOfMonth(matriceRate_tab(i).tDtFine))) Then

	           aImportoRata := Round(aImportoGGMiniComm * 30,2);
	           matriceRate_tab(i).tImRata:= aImportoRata;
	           aImportoResto := aImportoResto - aImportoRata;
	   Elsif (IBMUTL001.getDayOfDate(matriceRate_tab(i).tDtFine) = IBMUTL001.getDayLastDayOfMonth(matriceRate_tab(i).tDtFine)) Then
	         --primo mese
	         aImportoRata := Round(aImportoGGMiniComm * aNumeroGGPrimaRata,2);
	         matriceRate_tab(i).tImRata:= aImportoRata;
	         aImportoResto := aImportoResto - aImportoRata;
	   End If;
      END LOOP;

   END;

END calcolaRateNormale;

/* Vecchia procedura - prima della Finanziaria 2007
PROCEDURE calcolaRateNormale
   IS
   isRataMensile CHAR(1);
   isRataMese CHAR(1);
   aDataMinScadenza DATE;
   aImportoRata NUMBER(15,2);
   aImportoResto NUMBER(15,2);
   aNumeroGG NUMBER;

BEGIN

   --------------------------------------------------------------------------------------------------
   -- Valorizzazione parametri

   -- Controllo che il numero delle rate trovi capienza nel numero di giorni della minicariera

   IF IBMUTL001.getDaysBetween(aRecMinicarriera.dt_inizio_minicarriera,
                               aRecMinicarriera.dt_fine_minicarriera) < aRecMinicarriera.numero_rate THEN
      IBMERR001.RAISE_ERR_GENERICO
         ('Il numero di rate indicate nella minicarriera ' || aRecMinicarriera.numero_rate ||
          ' non trova capienza nei giorni di durata della stessa ' || IBMUTL001.getDaysBetween(aRecMinicarriera.dt_inizio_minicarriera,
                                                                                               aRecMinicarriera.dt_fine_minicarriera));
   END IF;

   -- Determino la tipologia delle rate che andranno a generarsi. Rate mensili o meno

   isRataMensile:=CNRCTB600.chkRateMensiliNormale(aRecMinicarriera);

   -- Se la rata ? mensile e le date sono di inizio e fine mese allora metto isRataMese = 'Y'. Per ogni
   -- rata nel primo caso la data di fine ? il giorno precedente della data inizio (al limite il fine mese),
   -- nel secondo caso la data di fine ? sempre un fine mese
   -- Determino la data minima della scadenza riferita alla data di creazione (data di sistema).
   -- Rate mensili
   --    Si normalizza al 20 del mese di creazione o di quello successivo se il giorno ? superiore a 20
   --    Si mantiene la data di sistema

   IF isRataMensile = IBMUTL001.isIntervalloDateMensile THEN
      IF IBMUTL001.isDifferenzaMese(aRecMinicarriera.dt_inizio_minicarriera,
                                    aRecMinicarriera.dt_fine_minicarriera) = IBMUTL001.isIntervalloDateMese THEN
         isRataMese:=IBMUTL001.isIntervalloDateMese;
      ELSE
         isRataMese:=IBMUTL001.isNotIntervalloDateMese;
      END IF;
      IF IBMUTL001.getDayOfDate(dataOdiernaTronca) > 20 THEN
         aDataMinScadenza:=TO_DATE('20' || TO_CHAR(IBMUTL001.getAddMonth(IBMUTL001.getFirstDayOfMonth(dataOdiernaTronca),1),'MMYYYY'),'DDMMYYYY');
      ELSE
         aDataMinScadenza:=TO_DATE('20' || TO_CHAR(dataOdiernaTronca,'MMYYYY'),'DDMMYYYY');
      END IF;
   ELSE
      isRataMese:=IBMUTL001.isNotIntervalloDateMese;
      aDataMinScadenza:=dataOdiernaTronca;
   END IF;

   --------------------------------------------------------------------------------------------------
   -- Calcolo dell'importo della rata in modo proporzionale al numero di rate

   aImportoRata:=ROUND((aRecMinicarriera.im_totale_minicarriera / aRecMinicarriera.numero_rate),2);
   aImportoResto:=aRecMinicarriera.im_totale_minicarriera;

   --------------------------------------------------------------------------------------------------
   -- Se le rate non sono mensili allora calcolo i giorni di ogni rata in modo proporzionale al numero
   -- delle stesse

   IF isRataMensile = IBMUTL001.isNotIntervalloDateMensile THEN
      aNumeroGG:=ROUND(((IBMUTL001.getDaysBetween(aRecMinicarriera.dt_inizio_minicarriera,
                                                  aRecMinicarriera.dt_fine_minicarriera)) /
                       aRecMinicarriera.numero_rate));
   ELSE
      aNumeroGG:=0;
   END IF;

   --------------------------------------------------------------------------------------------------
   -- Costruzione della matrice rate

   BEGIN

      FOR i IN 1 .. aRecMinicarriera.numero_rate

      LOOP

         -- Data inizio rata. Data inizio minicarriera o data fine rata precedente + 1

         IF i = 1 THEN
            matriceRate_tab(i).tDtInizio:=aRecMinicarriera.dt_inizio_minicarriera;
         ELSE
            IF isRataMensile = IBMUTL001.isIntervalloDateMensile THEN
               matriceRate_tab(i).tDtInizio:=IBMUTL001.getAddMonth(aRecMinicarriera.dt_inizio_minicarriera,(i-1));
            ELSE
               matriceRate_tab(i).tDtInizio:=(matriceRate_tab(i-1).tDtFine + 1);
            END IF;
         END IF;

         -- Data fine rata. Data fine minicarriera o data fine mese o calcolata in base ai giorni

         IF i = aRecMinicarriera.numero_rate THEN
            matriceRate_tab(i).tDtFine:=aRecMinicarriera.dt_fine_minicarriera;
         ELSE
            IF isRataMensile = IBMUTL001.isIntervalloDateMensile THEN
               IF isRataMese = IBMUTL001.isIntervalloDateMese THEN
                  matriceRate_tab(i).tDtFine:=IBMUTL001.getLastDayOfMonth(matriceRate_tab(i).tDtInizio);
               ELSE
                  matriceRate_tab(i).tDtFine:=(IBMUTL001.getAddMonth(aRecMinicarriera.dt_inizio_minicarriera,(i)) - 1);
               END IF;
            ELSE
               matriceRate_tab(i).tDtFine:=matriceRate_tab(i).tDtInizio + aNumeroGG;
            END IF;
         END IF;

         -- Determino la data di scadenza temporaneo portata al 20 della data fine rata solo se isRataMensile = Y

         IF isRataMensile = 'Y' THEN
            matriceRate_tab(i).tDtScadenza:=TO_DATE('20' || TO_CHAR(matriceRate_tab(i).tDtFine,'MMYYYY'),'DDMMYYYY');
         ELSE
            matriceRate_tab(i).tDtScadenza:=matriceRate_tab(i).tDtFine;
         END IF;

         -- Determino la data di scadenza in base ai mesi di anticipo e posticipo.
         -- Se posticipo data di fine rata + mesi posticipo -1.
         -- Se anticipo data di fine rata - mesi anticipo

         IF    aRecMinicarriera.ti_anticipo_posticipo = 'A' THEN
               matriceRate_tab(i).tDtScadenza:=IBMUTL001.getAddMonth(matriceRate_tab(i).tDtScadenza,
                                                                     (aRecMinicarriera.mesi_anticipo_posticipo * -1));
         ELSIF aRecMinicarriera.ti_anticipo_posticipo = 'P' THEN
               matriceRate_tab(i).tDtScadenza:=IBMUTL001.getAddMonth(matriceRate_tab(i).tDtScadenza,
                                                                     (aRecMinicarriera.mesi_anticipo_posticipo - 1));
         END IF;

         -- Normalizzo sempre la data di scadenza calcolata alla data minima di scadenza se quest'ultima ? maggiore

         IF matriceRate_tab(i).tDtScadenza < aDataMinScadenza THEN
            matriceRate_tab(i).tDtScadenza:=aDataMinScadenza;
         END IF;

         -- Importo della rata. L'ultima rata accoglie il resto della divisione

         IF i = aRecMinicarriera.numero_rate THEN
            matriceRate_tab(i).tImRata:=aImportoResto;
         ELSE
            matriceRate_tab(i).tImRata:=aImportoRata;
            aImportoResto:=aImportoResto - aImportoRata;
         END IF;

      END LOOP;

   END;

END calcolaRateNormale;
*/
-- ==================================================================================================
-- Calcolo delle rate di una minicarriera con data inizio nell'esercizio di creazione o nel futuro
-- ==================================================================================================
PROCEDURE calcolaRateTassazioneSep
   IS
   isRataMensile CHAR(1);
   isRataMese CHAR(1);
   isMensile CHAR(1);
   isMese CHAR(1);
   aDataMinScadenza DATE;
   aImportoRata NUMBER(15,2);
   aImportoPrimaRata NUMBER(15,2);
   aImportoSecondaRata NUMBER(15,2);
   aImportoResto NUMBER(15,2);
   aNumeroGG NUMBER;
   ggTotaliComm INTEGER;
   ggPrimaRataComm INTEGER;
   dtScadPrimaRataComm DATE;
   ggSecondaRataComm INTEGER;
   dtScadSecondaRataComm DATE;
   ggRestoRataComm INTEGER;
   aDataInizioAnno DATE;
   aDataRif DATE;

BEGIN

   --------------------------------------------------------------------------------------------------
   -- Valorizzazione parametri

   aDataInizioAnno:=TO_DATE('0101' || aRecMinicarriera.esercizio,'DDMMYYYY');
   isMensile:=NULL;
   isMese:=NULL;
   dtScadPrimaRataComm:=NULL;
   dtScadSecondaRataComm:=NULL;
   aImportoPrimaRata:=0;
   aImportoSecondaRata:=0;

   -- Determino la tipologia delle rate che andranno a generarsi. Rate mensili o meno

   isRataMensile:=CNRCTB600.chkRateMensiliTassazSep(aRecMinicarriera);

   -- Se la rata ? mensile e le date sono di inizio e fine mese allora metto isRataMese = 'Y'. Nella
   -- generazione delle rate la data di fine rata ? sempre un fine mese.
   -- Determino la data minima della scadenza. Se le rate sono mensili allora si normalizza al 20
   -- del mese di creazione o di quello successivo se la data di sistema ? superiore al giorno 20
   -- Valorizzo isMese e isMensile per l'accesso alla routine di determinazione della rata di prima scadenza

   IF isRataMensile = IBMUTL001.isIntervalloDateMensile THEN
      IF IBMUTL001.isDifferenzaMese(aRecMinicarriera.dt_inizio_minicarriera, aRecMinicarriera.dt_fine_minicarriera) = IBMUTL001.isIntervalloDateMese THEN
         isRataMese:=IBMUTL001.isIntervalloDateMese;
         isMensile:='N';
         isMese:='Y';
      ELSE
         isRataMese:=IBMUTL001.isNotIntervalloDateMese;
         isMensile:='Y';
         isMese:='N';
      END IF;
      IF TO_CHAR(dataOdiernaTronca,'DD') > 20 THEN
         aDataMinScadenza:=TO_DATE('20' || TO_CHAR(IBMUTL001.getAddMonth(IBMUTL001.getFirstDayOfMonth(dataOdiernaTronca),1),'MMYYYY'),'DDMMYYYY');
      ELSE
         aDataMinScadenza:=TO_DATE('20' || TO_CHAR(dataOdiernaTronca,'MMYYYY'),'DDMMYYYY');
      END IF;
   ELSE
      isRataMese:=IBMUTL001.isNotIntervalloDateMese;
      aDataMinScadenza:=dataOdiernaTronca;
      isMensile:='N';
      isMese:='N';
   END IF;

   --------------------------------------------------------------------------------------------------
   -- Determinazione della data di scadenza della prima e seconda rata


   FOR i IN 1 .. 2

   LOOP

      aDataRif:=CNRCTB600.getDataScadenzaRata(aRecMinicarriera,
                                              isMese,
                                              isMensile,
                                              i,
                                              dtScadPrimaRataComm);

      IF i = 1 THEN
         dtScadPrimaRataComm:=aDataRif;
      ELSE
         dtScadSecondaRataComm:=aDataRif;
      END IF;

   END LOOP;


   --------------------------------------------------------------------------------------------------
   -- Calcolo i giorni totali sull'anno commerciale della minicarriera.
   -- Calcolo i giorni normali della minicarriera secondo l'anno solare a partire della seconda rata.
   -- Tale calcolo opera solo per le rate successive alla seconda.

   ggTotaliComm:=IBMUTL001.getDaysCommBetween(aRecMinicarriera.dt_inizio_minicarriera, aRecMinicarriera.dt_fine_minicarriera);
   IF aRecMinicarriera.numero_rate > 2 THEN
      aNumeroGG:=ROUND(
                          (
                              IBMUTL001.getDaysBetween((dtScadSecondaRataComm + 1), aRecMinicarriera.dt_fine_minicarriera) /
                              (aRecMinicarriera.numero_rate - 2)
                          )
                      );

   END IF;


   --------------------------------------------------------------------------------------------------
   -- Calcolo dell'importo delle rate

   aImportoResto:=aRecMinicarriera.im_totale_minicarriera;

   aImportoPrimaRata:=ROUND((aRecMinicarriera.im_totale_minicarriera  / ggTotaliComm) *
                            (IBMUTL001.getDaysCommBetween(aRecMinicarriera.dt_inizio_minicarriera,
                                                          dtScadPrimaRataComm)
                            ),2);

   IF aRecMinicarriera.numero_rate > 1 THEN
      aImportoSecondaRata:=ROUND((aRecMinicarriera.im_totale_minicarriera  / ggTotaliComm) *
                                 (IBMUTL001.getDaysCommBetween((dtScadPrimaRataComm + 1),
                                                               dtScadSecondaRataComm)
                                 ),2);
   END IF;

   IF aRecMinicarriera.numero_rate > 2 THEN
      aImportoRata:=ROUND(
                            (
                               (aRecMinicarriera.im_totale_minicarriera - aImportoPrimaRata - aImportoSecondaRata) /
                               (aRecMinicarriera.numero_rate - 2)
                            ),2
                         );
   END IF;

   --------------------------------------------------------------------------------------------------
   -- Costruzione della matrice rate

   BEGIN

      FOR i IN 1 .. aRecMinicarriera.numero_rate

      LOOP

         -- Data inizio rata.

         IF i = 1 THEN
            matriceRate_tab(i).tDtInizio:=aRecMinicarriera.dt_inizio_minicarriera;
         ELSE
            matriceRate_tab(i).tDtInizio:=(matriceRate_tab(i-1).tDtFine + 1);
         END IF;

         -- Data fine rata. Data fine minicarriera o data fine mese o calcolata in base ai giorni

         IF i = aRecMinicarriera.numero_rate THEN
            matriceRate_tab(i).tDtFine:=aRecMinicarriera.dt_fine_minicarriera;
         ELSE
            IF    i = 1 THEN
                  matriceRate_tab(i).tDtFine:=dtScadPrimaRataComm;
            ELSIF i = 2 THEN
                  matriceRate_tab(i).tDtFine:=dtScadSecondaRataComm;
            ELSE
               IF isRataMensile = 'Y' THEN
                  IF isRataMese = 'Y' THEN
                     matriceRate_tab(i).tDtFine:=IBMUTL001.getLastDayOfMonth(matriceRate_tab(i).tDtInizio);
                  ELSE
                     matriceRate_tab(i).tDtFine:=(IBMUTL001.getAddMonth(matriceRate_tab(i).tDtInizio,1) - 1);
                  END IF;
               ELSE
                  matriceRate_tab(i).tDtFine:=matriceRate_tab(i).tDtInizio + aNumeroGG - 1;
               END IF;
            END IF;
         END IF;

         -- Determino la data di scadenza temporaneo portata al 20 della data fine rata solo se isRataMensile = Y

         IF isRataMensile = 'Y' THEN
            matriceRate_tab(i).tDtScadenza:=TO_DATE('20' || TO_CHAR(matriceRate_tab(i).tDtFine,'MMYYYY'),'DDMMYYYY');
         ELSE
            matriceRate_tab(i).tDtScadenza:=matriceRate_tab(i).tDtFine;
         END IF;

         -- Normalizzo sempre la data di scadenza calcolata alla data minima di scadenza se quest'ultima ? maggiore

         IF matriceRate_tab(i).tDtScadenza < aDataMinScadenza THEN
            matriceRate_tab(i).tDtScadenza:=aDataMinScadenza;
         END IF;

         -- Importo della rata. L'ultima rata accoglie il resto della divisione

         IF i = aRecMinicarriera.numero_rate THEN
            matriceRate_tab(i).tImRata:=aImportoResto;
         ELSE
            IF    i = 1 THEN
                  matriceRate_tab(i).tImRata:=aImportoPrimaRata;
                  aImportoResto:=aImportoResto - aImportoPrimaRata;
            ELSIF i = 2 THEN
                  matriceRate_tab(i).tImRata:=aImportoSecondaRata;
                  aImportoResto:=aImportoResto - aImportoSecondaRata;
            ELSE
                  matriceRate_tab(i).tImRata:=aImportoRata;
                  aImportoResto:=aImportoResto - aImportoRata;
            END IF;
         END IF;

      END LOOP;

   END;

END calcolaRateTassazioneSep;

-- ==================================================================================================
-- Scrittura delle rate di una minicarriera
-- ==================================================================================================
PROCEDURE scriviRate
   IS

BEGIN

   -- Cancellazione delle rate

   CNRCTB600.delMinicarrieraRate(aRecMinicarriera.cd_cds,
                                 aRecMinicarriera.cd_unita_organizzativa,
                                 aRecMinicarriera.esercizio,
                                 aRecMinicarriera.pg_minicarriera);

   -- Ciclo di inserimento delle rate

   BEGIN

      aRecMCarrieraRata:=NULL;
      aRecMCarrieraRata.cd_cds:=aRecMinicarriera.cd_cds;
      aRecMCarrieraRata.cd_unita_organizzativa:=aRecMinicarriera.cd_unita_organizzativa;
      aRecMCarrieraRata.esercizio:=aRecMinicarriera.esercizio;
      aRecMCarrieraRata.pg_minicarriera:=aRecMinicarriera.pg_minicarriera;
      aRecMCarrieraRata.stato_ass_compenso:='N';
      aRecMCarrieraRata.cd_cds_compenso:=NULL;
      aRecMCarrieraRata.esercizio_compenso:=NULL;
      aRecMCarrieraRata.cd_uo_compenso:=NULL;
      aRecMCarrieraRata.pg_compenso:=NULL;
      aRecMCarrieraRata.dacr:=dataOdierna;
      aRecMCarrieraRata.utcr:=aRecMinicarriera.utcr;
      aRecMCarrieraRata.duva:=dataOdierna;
      aRecMCarrieraRata.utuv:=aRecMinicarriera.utuv;
      aRecMCarrieraRata.pg_ver_rec:=aRecMinicarriera.pg_ver_rec;

      FOR i IN matriceRate_tab.FIRST .. matriceRate_tab.LAST

      LOOP

         aRecMCarrieraRata.pg_rata:=i;
         aRecMCarrieraRata.dt_inizio_rata:=matriceRate_tab(i).tDtInizio;
         aRecMCarrieraRata.dt_fine_rata:=matriceRate_tab(i).tDtFine;
         aRecMCarrieraRata.dt_scadenza:=matriceRate_tab(i).tDtScadenza;
         aRecMCarrieraRata.im_rata:=matriceRate_tab(i).tImRata;
         CNRCTB600.insMinicarrieraRate(aRecMCarrieraRata);

      END LOOP;

   END;

END scriviRate;

--==================================================================================================
-- Esecuzione della copia di una minicarriera esistente
--==================================================================================================
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
   ) IS
   eseguiLock CHAR(1);
   aRecMinicarrieraClone MINICARRIERA%ROWTYPE;
   aRecMCarrieraRateClone MINICARRIERA_RATA%ROWTYPE;
   aImporto NUMBER(15,2);
   aDataMin DATE;
   aDataMax DATE;
   aNumeroRate NUMBER(3);
   dataOdierna DATE;
   dataMinMaxDefault DATE;

   gen_cur GenericCurTyp;

BEGIN

   -------------------------------------------------------------------------------------------------
   -- Memorizzazione parametri generali della procedura

   eseguiLock:='Y';
   dataOdierna:=SYSDATE;

   -- Settaggio della data di riferimento (registrazione o rate) di default

   dataMinMaxDefault:=TRUNC(dataOdierna);
   IF dataMinMaxDefault > TO_DATE('3112' || inCopiaEseMCarriera, 'DDMMYYYY') THEN
      dataMinMaxDefault:= TO_DATE('3112' || inCopiaEseMCarriera, 'DDMMYYYY');
   END IF;

   -------------------------------------------------------------------------------------------------
   -- Memorizzazione della minicarriera in calcolo

   aRecMinicarriera:=CNRCTB600.getMinicarriera(inCdsMCarriera,
                                               inUoMCarriera,
                                               inEseMCarriera,
                                               inPgMCarriera,
                                               eseguiLock);

   -------------------------------------------------------------------------------------------------
   -- Copia MINICARRIERA

   -- Copia come memorizzazione del record in elaborazione in caso di inserimento o modifica
   -- In caso di ripristino o rinnovo la nuova minicarriera ha data registrazione pari alla data
   -- odierna eventualmente limitata al 31/12 dell'esercizio di riferimento.

   aRecMinicarrieraClone:=aRecMinicarriera;
   aRecMinicarrieraClone.cd_cds:=inCopiaCdsMCarriera;
   aRecMinicarrieraClone.cd_unita_organizzativa:=inCopiaUoMCarriera;
   aRecMinicarrieraClone.esercizio:=inCopiaEseMCarriera;
   aRecMinicarrieraClone.pg_minicarriera:=inCopiaPgMCarriera;
   IF (aRecMinicarriera.stato ='R' OR
       aRecMinicarriera.stato ='P') THEN
      aRecMinicarrieraClone.cd_cds_minicarriera_ori:=aRecMinicarriera.cd_cds;
      aRecMinicarrieraClone.cd_uo_minicarriera_ori:=aRecMinicarriera.cd_unita_organizzativa;
      aRecMinicarrieraClone.esercizio_minicarriera_ori:=aRecMinicarriera.esercizio;
      aRecMinicarrieraClone.pg_minicarriera_ori:=aRecMinicarriera.pg_minicarriera;
      aRecMinicarrieraClone.dt_registrazione:=dataMinMaxDefault;
      aRecMinicarrieraClone.stato:='A';
      aRecMinicarrieraClone.dt_sospensione:=NULL;
      aRecMinicarrieraClone.dt_ripristino:=NULL;
      aRecMinicarrieraClone.dt_rinnovo:=NULL;
      aRecMinicarrieraClone.dt_cessazione:=NULL;
      aRecMinicarrieraClone.dacr:=dataOdierna;
      aRecMinicarrieraClone.duva:=dataOdierna;
      aRecMinicarrieraClone.utcr:=aRecMinicarrieraClone.utuv;
      aRecMinicarrieraClone.pg_ver_rec:=1;

      aImporto:=0;
      aDataMin:=NULL;
      aDataMax:=NULL;
      aNumeroRate:=0;

      -- Valorizzazioni campi per minicarriera ripristinata

      IF aRecMinicarriera.stato ='R' THEN
         CNRCTB600.getNonLiquidatoRate(aRecMinicarriera.cd_cds,
                                       aRecMinicarriera.cd_unita_organizzativa,
                                       aRecMinicarriera.esercizio,
                                       aRecMinicarriera.pg_minicarriera,
                                       aImporto,
                                       aDataMin,
                                       aDataMax,
                                       aNumeroRate);
         aRecMinicarrieraClone.im_totale_minicarriera:=aImporto;
         aRecMinicarrieraClone.numero_rate:=aNumeroRate;
         aRecMinicarrieraClone.dt_inizio_minicarriera:=aDataMin;
         aRecMinicarrieraClone.dt_fine_minicarriera:=aDataMax;
      END IF;

      -- Se aDataMin o aDataMax sono NULL allora si valorizzano con la data odierna

      IF (aDataMin IS NULL OR
          aDataMax IS NULL) THEN
         aRecMinicarrieraClone.dt_inizio_minicarriera:=dataMinMaxDefault;
         aRecMinicarrieraClone.dt_fine_minicarriera:=dataMinMaxDefault;
      END IF;

   END IF;

   -- Validazione del terzo sulla minicarriera in clonazione

   IF CNRCTB080.chkEsisteTerzoPerCompenso(aRecMinicarrieraClone.cd_terzo,
                                          aRecMinicarrieraClone.cd_tipo_rapporto,
                                          aRecMinicarrieraClone.ti_anagrafico,
                                          aRecMinicarrieraClone.dt_registrazione,
                                          aRecMinicarrieraClone.dt_inizio_minicarriera,
                                          aRecMinicarrieraClone.dt_fine_minicarriera) = 0 THEN
      IBMERR001.RAISE_ERR_GENERICO('Terzo ' || aRecMinicarrieraClone.cd_terzo || ' inesistente. ' ||
                                   'Il terzo e/o il rapporto ' || aRecMinicarrieraClone.cd_tipo_rapporto ||
                                   'risultano cessati o non contigui alle date di riferimento della minicarriera');

   END IF;

   CNRCTB600.insMinicarriera(aRecMinicarrieraClone);

   -- Copia delle rate della minicarriera

   IF aRecMinicarriera.stato='A' THEN

      BEGIN

         OPEN gen_cur FOR

              SELECT *
              FROM   MINICARRIERA_RATA
              WHERE  cd_cds = inCdsMCarriera AND
                     cd_unita_organizzativa = inUoMCarriera AND
                     esercizio = inEseMCarriera AND
                     pg_minicarriera = inPgMCarriera
              ORDER BY 1,2,3,4,5;

         LOOP

            FETCH gen_cur INTO
                  aRecMCarrieraRateClone;

            EXIT WHEN gen_cur%NOTFOUND;

            aRecMCarrieraRateClone.cd_cds:=inCopiaCdsMCarriera;
            aRecMCarrieraRateClone.cd_unita_organizzativa:=inCopiaUoMCarriera;
            aRecMCarrieraRateClone.esercizio:=inCopiaEseMCarriera;
            aRecMCarrieraRateClone.pg_minicarriera:=inCopiaPgMCarriera;

            CNRCTB600.insMinicarrieraRate(aRecMCarrieraRateClone);

        END LOOP;

        CLOSE gen_cur;

      END;

   END IF;

END copiaMinicarriera;

-- ==================================================================================================
-- Inserita routine di controllo della consistenza delle rate di una minicarriera
-- ==================================================================================================
PROCEDURE chkScadRateMinicarriera
   (
    inCdsMCarriera MINICARRIERA.cd_cds%TYPE,
    inUoMCarriera MINICARRIERA.cd_unita_organizzativa%TYPE,
    inEseMCarriera MINICARRIERA.esercizio%TYPE,
    inPgMCarriera MINICARRIERA.pg_minicarriera%TYPE
   ) IS
   eseguiLock CHAR(1);
   aRecMCarrieraRata MINICARRIERA_RATA%ROWTYPE;
   aMemRecMCarrieraRata MINICARRIERA_RATA%ROWTYPE;
   aNumeroRate INTEGER;
   aTotImportoRate MINICARRIERA_RATA.im_rata%TYPE;
   aDtInizioPrimaRata DATE;
   aDtFineUltimaRata DATE;
   aStringa VARCHAR2(2000);

   gen_cur GenericCurTyp;

BEGIN

   -------------------------------------------------------------------------------------------------
   -- Memorizzazione parametri generali della procedura

   eseguiLock:='Y';
   aMemRecMCarrieraRata:=NULL;
   aNumeroRate:=0;
   aTotImportoRate:=0;
   aStringa:=NULL;

   -------------------------------------------------------------------------------------------------
   -- Memorizzazione della minicarriera in calcolo

   aRecMinicarriera:=CNRCTB600.getMinicarriera(inCdsMCarriera,
                                               inUoMCarriera,
                                               inEseMCarriera,
                                               inPgMCarriera,
                                               eseguiLock);

   -------------------------------------------------------------------------------------------------
   -- Ciclo di lettura delle rate di una minicarriera

   BEGIN

      OPEN gen_cur FOR

           SELECT *
           FROM   MINICARRIERA_RATA
           WHERE  cd_cds = inCdsMCarriera AND
                  cd_unita_organizzativa = inUoMCarriera AND
                  esercizio = inEseMCarriera AND
                  pg_minicarriera = inPgMCarriera
           ORDER BY 1,2,3,4,5;

      LOOP

         FETCH gen_cur INTO
               aRecMCarrieraRata;

         EXIT WHEN gen_cur%NOTFOUND;

         -- Memorizzo il record precedente

         IF aMemRecMCarrieraRata.cd_cds IS NULL THEN
            aDtInizioPrimaRata:=aRecMCarrieraRata.dt_inizio_rata;
            aMemRecMCarrieraRata:=aRecMCarrieraRata;
         END IF;

         aDtFineUltimaRata:=aRecMCarrieraRata.dt_fine_rata;
         aNumeroRate:=aNumeroRate + 1;
         aTotImportoRate:=aTotImportoRate + aRecMCarrieraRata.im_rata;

         -- Controllo congruenza delle date sulle singole rate

         IF aNumeroRate > 1 THEN

            LOOP

               IF aRecMCarrieraRata.dt_inizio_rata > aRecMCarrieraRata.dt_fine_rata + 1 THEN
                  aStringa:=aStringa || 'Errore in sequenza delle date su rata ' ||
                            LPAD(aRecMCarrieraRata.pg_rata,3,0) || CHR(10);
                  EXIT;
               END IF;

               IF aRecMCarrieraRata.dt_inizio_rata != aMemRecMCarrieraRata.dt_fine_rata + 1 THEN
                  aStringa:=aStringa || 'Errore in sequenza delle date su rata ' ||
                            LPAD(aRecMCarrieraRata.pg_rata,3,0) || CHR(10);
                  EXIT;
               END IF;

               IF aRecMCarrieraRata.dt_scadenza < aMemRecMCarrieraRata.dt_scadenza THEN
                  aStringa:=aStringa || 'Errore in sequenza delle date su rata ' ||
                            LPAD(aRecMCarrieraRata.pg_rata,3,0) || CHR(10);
                  EXIT;
               END IF;

               EXIT;

            END LOOP;

            aMemRecMCarrieraRata:=aRecMCarrieraRata;

         END IF;

      END LOOP;

        CLOSE gen_cur;

      -- Controllo

      IF aRecMinicarriera.numero_rate != aNumeroRate THEN
         aStringa:=aStringa ||
                    'Numero rate in testata minicarriera ' || aRecMinicarriera.numero_rate  ||
                    ' diverso dal numero rate presente in dettaglio ' || aNumeroRate  || CHR(10);
      END IF;

      IF aRecMinicarriera.im_totale_minicarriera != aTotImportoRate THEN
         aStringa:=aStringa ||
                   'Importo totale della minicarriera ' || aRecMinicarriera.im_totale_minicarriera ||
                   ' diverso dalla sommatora degli importi delle rate ' || aTotImportoRate || CHR(10);
      END IF;

      IF aRecMinicarriera.dt_inizio_minicarriera != aDtInizioPrimaRata THEN
         aStringa:=aStringa ||
                   'Data di inizio della minicarriera ' || TO_CHAR(aRecMinicarriera.dt_inizio_minicarriera,'DD/MM/YYYY') ||
                   ' diverso dalla data inizio della prima rata ' || TO_CHAR(aDtInizioPrimaRata,'DD/MM/YYYY') || CHR(10);
      END IF;

      IF aRecMinicarriera.dt_fine_minicarriera != aDtFineUltimaRata THEN
         aStringa:=aStringa ||
                   'Data di fine della minicarriera ' || TO_CHAR(aRecMinicarriera.dt_fine_minicarriera,'DD/MM/YYYY') ||
                   ' diversa dalla data fine dell''ultima rata ' || TO_CHAR(aDtFineUltimaRata,'DD/MM/YYYY') || CHR(10);
      END IF;

   END;

   IF aStringa IS NOT NULL THEN
      IBMERR001.RAISE_ERR_GENERICO (aStringa);
   END IF;

   -------------------------------------------------------------------------------------------------
   -- Validazione del terzo sulla minicarriera

   IF CNRCTB080.chkEsisteTerzoPerCompenso(aRecMinicarriera.cd_terzo,
                                          aRecMinicarriera.cd_tipo_rapporto,
                                          aRecMinicarriera.ti_anagrafico,
                                          aRecMinicarriera.dt_registrazione,
                                          aRecMinicarriera.dt_inizio_minicarriera,
                                          aRecMinicarriera.dt_fine_minicarriera) = 0 THEN
      IBMERR001.RAISE_ERR_GENERICO('Terzo ' || aRecMinicarriera.cd_terzo || ' inesistente. ' ||
                                   'Il terzo e/o il rapporto ' || aRecMinicarriera.cd_tipo_rapporto ||
                                   'risultano cessati o non contigui alle date di riferimento della minicarriera');

   END IF;

END chkScadRateMinicarriera;

END;
/


