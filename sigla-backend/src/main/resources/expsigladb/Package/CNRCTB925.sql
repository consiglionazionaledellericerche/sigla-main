--------------------------------------------------------
--  DDL for Package CNRCTB925
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRCTB925" AS
----------------------------------------------------------------------------------------------------
--
-- Elaborazione per tracciato mensile INPS. (Modello GLA Mensile).
-- La procedura crea due file, uno contenente i dati anagrafici e l'altro i dati contabili.
-- Vengono estratti tutti i compensi inviati al tesoriere e la cui data di emissione coincide con
-- l'esercizio ed il mese fornito in input.
-- Inoltre, i dati suddetti vengono nel file raggruppati per codice anagrafico, tipo rapporto ed
-- aliquota secondo il tracciato fornito da Data Managment.
-- I dati richiesti dall'INPS e per il momento non gestiti da SIGLA sono stati impostati con dei
-- valori fissi.
--
-- >>>>>>>>>> Date: 04/05/2005
-- >>>>>>>>>> Version: 1.0
--
-- Dependency: CNRCTB IBMERR 001 IBMUTL 001
--
-- =================================================================================================
-- PARAMETRI IN INPUT
-- =================================================================================================
-- +--------------------------+------------------------------------------------+
-- |CAMPO                     |DESCRIZIONE                                     |
-- +--------------------------+------------------------------------------------+
-- |inEsercizio               |Esercizio di riferimento                        |
-- |inMese                     Mese di riferimento
-- |inRepID                   |Progressivo identificativo estrazione           |
-- |inMsgError                |Futura implementazione                          |
-- |inUtente                  |Identificativo utente della connessione         |
-- +--------------------------+------------------------------------------------+
--
----------------------------------------------------------------------------------------------------
--
-- History:
--
-- Date: 03/05/2005
-- Version: 1.0
--
-- Creazione package. Adeguamento normativo.
--
-- Date: 19/05/2005
-- Version: 1.1
--
-- Gestite le nuove informazioni richieste dall'INPS (Codice rapporto INPS, Codice Attivita' INPS,
-- Codice altra assicurazione INPS). Per i vecchi compensi che non hanno tali informazioni sono
-- stati lasciati i valori fissi.
--
-- Date: 13/06/2005
-- Version: 1.2
--
-- Escluse dall'estrazione le righe con imponibile = 0 e per i vecchi compensi in cui c'e'
-- ancora l'aliquota 17,80 e 18,80, forzati i valori richiesti dall'INPS (18,00 e 19,00)
--
-- Date: 08/07/2005
-- Version: 1.3
--
-- Escluse dall'estrazione anche le righe con aliquota = 0 e imponibile > 0 (quelle che hanno superato lo scaglione)
-- e nel caso in cui l'aliquota rodotta è del 15%, l'altra assicurazione obbligatoria valorizzata con '002'.
--
-- Date: 13/01/2006
-- Version: 1.4
--
-- Poichè le righe negative hanno aliquota = 0, con la modifica precedente sono state escluse.
-- Per riprenderle aggiunta la condizione "aliquota > 0 Or segno_imponibile = '-'"
--
-- Date: 14/06/2006
-- Version: 1.5
--
-- Adeguamento normativo GLA 2006: Aggiunti nell'elaborazione e nel file anche i Contributi dovuti,
-- trattenuti e versati
--
-- Date: 10/11/2006
-- Version: 1.6
--
-- GLA mensile 2006: Inseriti Nome e Cognome anche per le ditte individuali
--                   Nel file anagrafico eliminate le anagrafiche doppie
--
-- =================================================================================================
-- Constants:

   -- Costante tipo log per batch

   IDTIPOLOG CONSTANT VARCHAR2(20) := 'ESTR_INPS_MENSILE';

   -- Costante tipo blob per output file estrazione INPS

   IDTIPOBLOB CONSTANT VARCHAR2(10) := 'ESTR_INPS';

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

   -- Definizione tabella PL/SQL di appoggio errori per estrazione INPS MENSILE

   TYPE dsErroriRec IS RECORD
       (
        tStringaKey VARCHAR2(2000),
        tStringaErr VARCHAR2(2000)
       );

   TYPE dsErroriTab IS TABLE OF dsErroriRec
        INDEX BY BINARY_INTEGER;
   errori_tab dsErroriTab;

   -- Definizione tabella PL/SQL di appoggio per calcolo DATA DI ASSUNZIONE e la DATA DI CESSAZIONE
   -- presenti nel tracciato che devono essere relative a tutti i compensi
   tabella_date_tutte CNRCTB545.intervalloDateTab;        --intervalli date tutti compensi
   tabella_date_ok_tutte CNRCTB545.intervalloDateTab;     --intervalli NORMALIZZATI date tutti compensi
   tabella_date_tutte_neg CNRCTB545.intervalloDateTab;

   -- Definizione di un cursore variabile

   TYPE GenericCurTyp IS REF CURSOR;

   -- Constants:

   -- Costanti per tipo cori ente percipiente

   TICORIENTE CONSTANT CHAR(1) := 'E';
   TICORIPERCIP CONSTANT CHAR(1) := 'P';

   -- Costanti per tipo riga in stampa

   TIRIGA_DET CONSTANT CHAR(1) := 'N';
   TIRIGA_TOT CONSTANT CHAR(1) := 'T';

----------------------------------------------------------------------------------------------------
-- MAIN PROCEDURE
----------------------------------------------------------------------------------------------------

-- Main estrazione INPS MENSILE

   PROCEDURE estrazioneINPSmensile
      (
       inEsercizio NUMBER,
       inMese NUMBER,
       inRepID INTEGER,
       inMsgError IN OUT VARCHAR2,
       inUtente VARCHAR2
      );

-- Guscio per gestione estrazione INPS Mensile in batch

   PROCEDURE job_estrazioneINPSmensile
      (
       job NUMBER,
       pg_exec NUMBER,
       next_date DATE,
       inEsercizio NUMBER,
       inMese NUMBER,
       inRepID INTEGER,
       inMsgError VARCHAR2,
       inUtente VARCHAR2
      );


END CNRCTB925;
