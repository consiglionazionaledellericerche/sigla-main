--------------------------------------------------------
--  DDL for Package CNRCTB900
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRCTB900" AS
----------------------------------------------------------------------------------------------------
--
-- Elaborazione per Estrazione dati CUD. E' possibile l'elaborazione per tutti o per una singola anagrafica.
--
-- >>>>>>>>>> Date: 26/03/2004
-- >>>>>>>>>> Version: 1.9
--
-- Dependency: CNRCTB 020 IBMERR 001 IBMUTL 001
--
-- =================================================================================================
-- PARAMETRI IN INPUT
-- =================================================================================================
-- +--------------------------+------------------------------------------------+
-- |CAMPO                     |DESCRIZIONE                                     |
-- +--------------------------+------------------------------------------------+
-- |inEsercizio               |Esercizio di riferimento                        |
-- |inCdAnag                  |Identificativo soggetto di riferimento. Vale:   |
-- |                          |- Se %           --> stampa totale CUD          |
-- |                          |- Altrimenti     --> stampa CUD per il solo     |
-- |                          |                     soggetto anagrafico        |
-- |inRepID                   |Progressivo identificativo estrazione           |
-- |inMsgError                |Futura implementazione                          |
-- |inUtente                  |Identificativo utente della connessione         |
-- +--------------------------+------------------------------------------------+
--
--
-- =================================================================================================
-- CONTENUTO DELLA TABELLA PARAMETRI (parametri_tab)
-- =================================================================================================
-- +----------------------------+---------------------------------------------+------------+
-- |VARIABILE                   |DESCRIZIONE                                  |OBBLIGATORIO|
-- +----------------------------+---------------------------------------------+------------+
-- |parametri_tab( 1).stringa   |Identificativo nome ENTE dichiarante         |     SI     |
-- |parametri_tab( 2).stringa   |Codice fiscale ENTE dichiarante              |     SI     |
-- |parametri_tab( 3).stringa   |Comune ENTE dichiarante                      |     SI     |
-- |parametri_tab( 4).stringa   |Provincia ENTE dichiarante                   |     SI     |
-- |parametri_tab( 5).stringa   |CAP ENTE dichiarante                         |     SI     |
-- |parametri_tab( 6).stringa   |Indirizzo ENTE dichiarante                   |     SI     |
-- |parametri_tab( 7).stringa   |Telefono ENTE dichiarante                    |     SI     |
-- |parametri_tab( 8).stringa   |Fax ENTE dichiarante                         |     SI     |
-- |parametri_tab( 9).stringa   |Mail ENTE dichiarante                        |     SI     |
-- |parametri_tab(10).stringa   |Rappresentante fiscale                       |     SI     |
-- |parametri_tab(11).stringa   |Stato contabile liquidazione CORI            |     NO     |
-- |parametri_tab(12).stringa   |Annotazione di default                       |     SI     |
-- |parametri_tab(13).stringa   |Annotazione per aliquota_max                 |     SI     |
-- |parametri_tab(14).stringa   |Annotazione per addizionali trattenute       |     SI     |
-- |parametri_tab(15).stringa   |Annotazione per addizionali accantonate      |     SI     |
-- |parametri_tab(16).stringa   |Annotazione per stranieri                    |     SI     |
-- |parametri_tab(17).stringa   |Annotazione di default per indicare gli      |     SI     |
-- |                            |intervalli temporali di riferimento compensi |     SI     |
-- |parametri_tab(18).stringa   |Annotazione di default prima parte quota     |     SI     |
-- |                            |deduzione standard                           |     SI     |
-- |parametri_tab(19).stringa   |Annotazione di default seconda parte quota   |     SI     |
-- |                            |deduzione standard                           |     SI     |
-- |parametri_tab(20).stringa   |Annotazione prima parte quota deduzione      |     SI     |
-- |                            |fissa intera                                 |     SI     |
-- |parametri_tab(21).stringa   |Annotazione seconda parte quota deduzione    |     SI     |
-- |                            |fissa intera                                 |     SI     |
-- |parametri_tab(22).stringa   |Annotazione importo complessivo              |     SI     |
-- +----------------------------+---------------------------------------------+------------+
--
----------------------------------------------------------------------------------------------------
--
-- History:
--
-- Date: 02/02/2004
-- Version: 1.0
--
-- Creazione package. Richiesta CINECA n. 750.
--
-- Date: 09/02/2004
-- Version: 1.1
--
-- Rinominata vista di estrazione imponibili per elaborazione CUD per rispetto della naming.
--
-- Date: 09/02/2004
-- Version: 1.2
--
-- Fix errore in recupero addizionali territorio accantonate
--
-- Date: 12/02/2004
-- Version: 1.3
--
-- Correzioni in gestione annotazioni:
-- 1) Test del solo campo fl_intera_qfissa_deduzione per mettere a Y il CAMPO dip_rp_deduzione_fissa_intera
-- 2) L'annotazione del versamento delle addizionali territorio vale solo se non vi sono accantonamenti
--
-- Date: 13/02/2004
-- Version: 1.4
--
-- Fix recupero dati cococo. Per errore si espongono i dati IRPEF e non quelli INPS.
--
-- Date: 03/03/2004
-- Version: 1.5
--
-- Fix errori segnalati:
-- - Non era valorizzato l'attributo fl_incluso_in_conguaglio
-- - Non viene estratto l'ammontare ente delle previdenziali per cococo.
-- - I dati cococo sono estratti indipendentemente dalla gestione a tassazione separata o meno del compenso
--
-- Date: 08/03/2004
-- Version: 1.6
--
-- Fix errori CINECA n. 788
-- - Errori corretti in data 03/03/2004
-- - La data fine cococo non è la massima data fine ma la minima
-- Richiesta CINECA n. 789. Modifiche estrazione CUD per adeguamento al tracciato CNR
-- - Normalizzazione delle date COCOCO al 31/12/esercizio
-- - Inserimento e gestione dei nuovi campi richiesti da CNR e estrazione della tipologia di reddito
--   prevalente e calcolo dell'intervallo assoluto delle date (minima e massima competenza dei compensi)
--
-- Date: 08/03/2004
-- Version: 1.7
--
-- Fix errore CINECA. Nell'indicare i compensi associati a conguaglio devono essere esclusi i conguagli
-- non pagati.
--
-- Date: 11/03/2004
-- Version: 1.8
--
-- Fix errori CINECA.
-- - Calcolo gioni non corretto in presenza di compensi solo di imponibile punto 2
-- - Revisione calcolo spaccatura deduzione in parte 3000 e parte 4500 per compensi con imponibile di
--   tipo 2.
--
-- Date: 26/03/2004
-- Version: 1.9
--
-- Fix errori CINECA e richieste.
-- - Sostituzione della costante 1.2 con 100 nella codifica del tempo determinato.
-- - Estrazione dell'imponibile previdenziale dalla CONTRIBUTO_RITENUTA per gestire correttamente
--   l'imponibile cococo in caso di senza calcoli.
-- - Gestione delle anagrafiche DIVERSI
-- - Correzione estrazione importo rateizzato con controllo di prendere da RATEIZZA_CLASSIFIC_CORI o
--   RATEIZZA_CLASSIFIC_CORI_S il valore associato all'ultimo conguaglio estratto dal CUD e non a quello
--   ultimo presente in base dati
--
-- Date: 03/02/2005
-- Version: 1.10
--
-- Nuove modifiche
-- - Aggiunto nel tracciato Indirizzo Domicilio Fiscale completo solo per la stampa delle Etichette
-- - Aggiunto UPPER a tutti gli alfanumerici
-- - Modificato calcolo del numero massimo di giorni per un dato esercizio (deve essere sempre 365 anche per i bisestili)
-- - Aggiunta nel file la nuova informazione "Flag previdenza complementare" (deve valere 3 solo per cococo)
--   (se per un anag esiste in ESTRAZIONE_CUR_DETT almeno un compenso con FL_COCOCO ='Y', in ESTRAZIONE_CUD per
--    lo stesso terzo vengono valorizzati i campi DT_INIZIO_COCOCO e DT_FINE_COCOCO: quindi usiamo questa informazione
--    per capire se è un cococo)
-- - Valorizzata Addizionale regionale e comunale per l'anno precedente
-- - Modificato calcolo gg_detrazioni: oltre ai giorni di competenza dei compensi con flag fl_detrazione = 'Y', vengono
--   presi anche i compensi con trattamento 'T098' (assegni alimentari)
-- - Modificato calcolo gg_periodo_tassep: non deve essere il numero dei giorni, bensi' l'anno di competenza dei compensi
--   che hanno il flag tassazione_separata = 'Y' (se sono piu' anni, si deve prendere il maggiore)
-- - Modificata la valorizzazione di aDataMinGenerale e aDataMaxGenerale: prima considerava solo i compensi non a
--   tassazione separata, ora prende tutti i compensi
--
-- Date: 07/02/2006
-- Version: 1.11
--
-- Adeguamento CUD 2006
-- - Eliminate le Detrazioni (tranne quelle a tassazione separata) ed aggiunta la Deduzione Family
-- - Aggiunti nel file i nuovi campi per i "Dati Esterni", ma non valorizzati non essendoci dati al
--   momento e poi perchè occorre verificare in Sigla come vengono gestiti
--
-- Date: 02/03/2010
-- Version: 1.12
--
-- Adeguamento CUD 2010 (REDDITI 2009)
-- - Aggiunta la sospensione per i terremotati
--
-- Date: 01/03/2010
-- Version: 1.13
--
-- Adeguamento CUD 2011 (REDDITI 2010)
-- Esclusi dal cud i terzi che hanno imponibile fiscale/previdenziale nullo
-- Nel calcolo dei giorni lavorativi esclusi quelli delle missioni
--
-- Date: 27/02/2015
-- Version: 1.14
--
-- Adeguamento CUD 2015 (REDDITI 2014)
-- Gestito il bonusdl66, il rientro dei lavoratori ed adeguato il tracciato
--
-- =================================================================================================
--
--
-- Constants:

   -- Costante tipo log per batch

   IDTIPOLOG CONSTANT VARCHAR2(20) := 'ESTRAZIONE_CUD';

   -- Costante tipo blob per output file estrazione CUD

   IDTIPOBLOB CONSTANT VARCHAR2(10) := 'ESTRAI_CUD';

-- Variabili globali

   dataOdierna DATE := Sysdate;
   maxGGAnno INTEGER;

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

   -- Definizione tabella PL/SQL di appoggio errori per estrazione CUD

   TYPE dsErroriRec IS RECORD
       (
        tStringaKey VARCHAR2(2000),
        tStringaErr VARCHAR2(2000)
       );

   TYPE dsErroriTab IS TABLE OF dsErroriRec
        INDEX BY BINARY_INTEGER;
   errori_tab dsErroriTab;


   -- Definizione tabella PL/SQL di appoggio per calcolo giorni di lavoro

   tabella_date CNRCTB545.intervalloDateTab;              --intervalli date compensi non a tassazione separata
   tabella_date_neg CNRCTB545.intervalloDateTab;
   tabella_date_cococo CNRCTB545.intervalloDateTab;       --intervalli date compensi non a tassazione separata (solo cococo)
   tabella_date_neg_cococo CNRCTB545.intervalloDateTab;
   tabella_date_ok CNRCTB545.intervalloDateTab;		  --intervalli NORMALIZZATI date compensi non a tassazione separata
   tabella_date_ok_cococo CNRCTB545.intervalloDateTab;    --intervalli NORMALIZZATI date compensi non a tassazione separata (solo cococo)
   tabella_date_tassep CNRCTB545.intervalloDateTab;       --intervalli date compensi a tassazione separata
   tabella_date_neg_tassep CNRCTB545.intervalloDateTab;
   tabella_date_ok_tassep CNRCTB545.intervalloDateTab;    --intervalli NORMALIZZATI date compensi a tassazione separata
   -- nuove tabelle per gestire tutte le date dei compensi senza distinguerli se a tassazione separata o meno
   -- perche' la DATA DI ASSUNZIONE e la DATA DI CESSAZIONE presenti nel tracciato devono essere relative a
   -- tutti i compensi
   tabella_date_tutte CNRCTB545.intervalloDateTab;        --intervalli date tutti compensi
   tabella_date_ok_tutte CNRCTB545.intervalloDateTab;     --intervalli NORMALIZZATI date tutti compensi
   tabella_date_tutte_neg CNRCTB545.intervalloDateTab;

   -- Definizione di un cursore variabile

   TYPE GenericCurTyp IS REF CURSOR;

   --Tipologie di Certificazioni
   /*
   "RA" = "a Ritenuta d'Acconto"
	 "PR" = "a Ritenuta Previdenziale"
	 "TI" = "a Titolo d'Imposta"
	 "CC" = "a Titolo d'Imposta - Co.Co.Co."
	 "RC" = "per Contributi corrisposti ad imprese"
	 "PC" = "a Titolo d'Imposta - Premi per concorsi"
	 "RAPPT" = "a Ritenuta d'Acconto su somme liquidate a seguito di pignoramenti presso terzi"
   */
----------------------------------------------------------------------------------------------------
-- MAIN PROCEDURE
----------------------------------------------------------------------------------------------------

-- Main estrazione CUD

   PROCEDURE estrazioneCUD
      (
       inEsercizio NUMBER,
       inCdAnag VARCHAR2,
       inRepID INTEGER,
       inMsgError IN OUT VARCHAR2,
       inUtente VARCHAR2
      );

-- Guscio per gestione estrazione CUD in batch

   PROCEDURE job_estrazioneCUD
      (
       job NUMBER,
       pg_exec NUMBER,
       next_date DATE,
       inEsercizio NUMBER,
       inCdAnag VARCHAR2,
       inRepID INTEGER,
       inMsgError VARCHAR2,
       inUtente VARCHAR2
      );

   Procedure inserisciDatiCUDDett
   (
    inEsercizio NUMBER,
    aCdAnag NUMBER,
    inRepID INTEGER,
    inUtente VARCHAR2
   );

   PROCEDURE upgDatiCUDDettCori
   (
    inEsercizio NUMBER,
    inRepID INTEGER
   );

   PROCEDURE upgDatiCUDDettCong
   (
    inEsercizio NUMBER,
    inRepID INTEGER
   );

   PROCEDURE aggregaDatiCUD
   (
    inEsercizio NUMBER,
    inRepID INTEGER,
    inUtente VARCHAR2
   );

   PROCEDURE valorizzaParametri
   (
    inEsercizio NUMBER
   );

END CNRCTB900;
