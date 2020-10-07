--------------------------------------------------------
--  DDL for Package CNRCTB650
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRCTB650" AS
--==================================================================================================
--
-- CNRCTB650 - Calcolo e scrittura CONGUAGLIO
--
-- Date: 30/01/2004
-- Version: 2.10
--
-- Dependency: CNRCTB 080/545/550 IBMERR 001 IBMUTL 001
--
-- History:
--
-- Date: 10/07/2002
-- Version: 1.0
--
-- Creazione Package.
--
-- Date: 29/08/2002
-- Version: 1.1
--
-- Revisione calcolo conguaglio
--
-- Date: 02/09/2002
-- Version: 1.2
--
-- Aggiunta memorizzazione della relazione compenso conguaglio
--
-- Date: 03/09/2002
-- Version: 1.3
--
-- Aggiunta routine per aggiornamento della relazione compenso conguaglio e revisione calcolo
--
-- Date: 09/09/2002
-- Version: 1.4
--
-- Inserito controllo di estrazione, per conguaglio, dei soli compensi con mandato emesso
--
-- Date: 24/09/2002
-- Version: 1.5
--
-- Modifica della routine di inserimento di un compenso per la gestione dei nuovi campi in tabella
-- ti_istituz_commerc, imponibile_inail, esercizio_fattura_fornitore, dt_fattura_fornitore,
-- nr_fattura_fornitore, fl_generata_fattura
--
-- Date: 17/10/2002
-- Version: 1.6
--
-- Modifica della routine di inserimento di un compenso per la gestione dei nuovi campi in tabella
-- COMPENSO fl_compenso_stipendi, fl_compenso_conguaglio
--
-- Date: 07/11/2002
-- Version: 1.7
--
-- Inserita la gestione (routine abilitaConguaglio) di eliminare le associazioni tra conguaglio in
-- input e compensi ad ogni accesso alla routine. Elimina l'errore di mantenimento associazioni
-- negative nel caso in cui dopo aver generato il compenso da conguaglio questo non sia confermato
-- bensì annullato e quindi rigenerato magari ad un terzo riferito ad altro anagrafico
-- Errore interno 2831
--
-- Date: 18/11/2002
-- Version: 1.8
--
-- Adeguamento package alla struttura modificata della tabella COMPENSO. Aggiunti attributi
-- fl_compenso_minicarriera e aliquota_irpef_da_missione.
--
-- Date: 23/12/2002
-- Version: 1.9
--
-- Modifica della routine di inserimento di un compenso per la gestione dei nuovi campi in tabella
-- COMPENSO fl_compenso_mcarriera_tassep, aliquota_irpef_tassep
--
-- Date: 24/01/2003
-- Version: 1.10
--
-- finanziaria 2003.
-- Modifica della routine di inserimento di un compenso per la gestione dei nuovi campi in tabella
-- COMPENSO  im_deduzione_irpef, imponibile_fiscale_netto
--
-- Date: 27/01/2003
-- Version: 1.11
--
-- finanziaria 2003.
-- Modifica della routine di inserimento di un compenso per la gestione dei nuovi campi in tabella
--    COMPENSO   numero_giorni
--    CONGUAGLIO numero_giorni
-- Introdotto il calcolo del conguaglio per finanziaria 2003
--
-- Date: 28/01/2003
-- Version: 1.12
--
-- Fix errore interno in routine abilita conguaglio e get ultimo conguaglio, l'exception del no_data_found
-- è rimasta nel blocco interno BEGIN END
--
-- Date: 29/01/2003
-- Version: 1.13
--
-- Fix errore interno non calcolava le detrazioni personali per intero in sede di conguaglio
-- Inserito aggiornamento dei montanti per la quota differenza goduto, dovuto delle deduzioni IRPEF
-- calcolate dal conguaglio.
--
-- Date: 29/01/2003
-- Version: 1.14
--
-- Fix errore interno. In ricerca del conguaglio passava il pg del compenso in sede di aggiornamento montanti
--
-- Date: 30/01/2003
-- Version: 1.15
--
-- Revisione finanziaria 2003, le detrazioni familiari e personali sono calcolate a partire dall'imponibile
-- fiscale lordo e non quello netto
--
-- Date: 03/03/2003
-- Version: 1.16
--
-- Fix errore interno, errore nel calcolo della eliminazione degli intervalli sovrapposti sulla matrice date
-- metodo normalizzaMatriceDate
--
-- Date: 20/03/2003
-- Version: 1.17
--
-- Richiesta CINECA n. 545, 546, 547
-- Mapping nuovi attributi (fl_escludi_qvaria_deduzione, fl_intera_qfissa_deduzione,
-- im_detrazione_personale_anag) in tabella CONGUAGLIO
-- 547 Inserito recupero dell'importo delle detrazioni personali impostate in ANAGRAFICO_ESERCIZIO
--     Eseguito calcolo di forzatura dell'importo delle detrazioni personali
--
-- Date: 15/04/2003
-- Version: 1.18
--
-- Richiesta CINECA n. 562. Implementazione della cancellazione del conguaglio.
--
-- Date: 16/04/2003
-- Version: 1.19
--
-- Richiesta CINECA n. 562. Introdotta anche la gestione della cancellazione logica.
-- Fix errore di mancato aggiornamento dei montanti
--
-- Date: 19/06/2003
-- Version: 1.20
--
-- Se, in caso di cancellazione logica :
-- esercizio corrente > esercizio scrivania --> dt_cancellazione = 31/12/esercizio scrivania
--
-- Date: 09/07/2003
-- Version: 1.21
--
-- Richiesta CINECA n. ???. Attivazione gestione recupero rate.
-- Mapping nuovo attributo fl_recupero_rate in tabella COMPENSO
-- Modificate procedure copiaCompenso, insCompenso
--
-- Date: 10/07/2003
-- Version: 1.22
--
-- Chiusura esercizio. Inserimento del filtro per recuperare i soli compensi creati in esercizio
-- pari od inferiore a quello di creazione del conguaglio e che hanno data pagamento nell'esercizio
-- del conguaglio stesso.
-- Attivazione della gestione del recupero rate in calcolo giorni
--
-- Date: 18/07/2003
-- Version: 1.23
--
-- Inserita la raise di un errore, nella gestione del recupero rate in calcolo giorni, se residuano
-- intervalli negativi.
--
-- Date: 19/09/2003
-- Version: 1.24
--
-- Modificato l'inizializzazione della data di cancellazione del conguaglio in
-- caso di cancellazione logica
--
-- Date: 28/10/2003
-- Version: 2.0
--
-- Rilascio richiesta CINECA n. 655. Gestione conguaglio per aliquota massima.
--
-- Date: 04/11/2003
-- Version: 2.1
--
-- Rilascio richiesta CINECA n. 471. Gestione rateizzazione addizionali territorio. (allinea DB)
-- Inserimento di un attributo per memorizzare l'indicazione di compenso calcolato con aliquota
-- massima in anagrafica.
--
-- Date: 11/11/2003
-- Version: 2.2
--
-- Rilascio richiesta CINECA n. 471. Gestione rateizzazione addizionali territorio. (Attivazione
-- dell'accantonamento delle addizionali territorio). Regole di gestione:
-- - Non ci sono vincoli sulla successione di conguagli che prevedono l'accantonamento o meno delle
--   addizionali territorio. Se un conguaglio tuttavia paga le addizionali allora sono trattenute
--   al percipiente anche quelle che sono state precedentemente accantonate.
-- - Non è possibile attivare la gestione dell'accantonamento se questo risulta negativo.
-- Inserito il controllo per cui se il compenso associato all'ultimo conguaglio non risulta pagato
-- allora si impedisce l'esecuzione di un nuovo conguaglio
--
-- Date: 27/11/2003
-- Version: 2.3
--
-- Fix errore interno in calcola accantonamento addizionali territorio.
-- Corretta la cancellazione del conguaglio (gestione accumulo delle rateizzazioni addizionali territorio)
-- Errore CINECA n. 701. Fix errore in calcolo delle detrazioni personali (passava l'intervallo di un
-- giorno invece che dell'anno).
--
-- Date: 01/12/2003
-- Version: 2.4
--
-- Fix errore interno. Era errata la normalizzazione dell'intervallo negativo nella gestione del recupero
-- rate con intervalli sovrapposti (esterno sinistro) e date di fine coincidenti.
--
-- Date: 04/12/2003
-- Version: 2.5
--
-- Fix errore CINECA n 705. In alcuni casi la procedura di calcolo conguaglio segnala erroneamente la
-- rimenenza di intervalli date negativi per recupero rate. (Sovrapposizione senza esteremi da o a uguali)
--
-- Date: 09/12/2003
-- Version: 2.6
--
-- Rilascio richiesta CINECA n. 471. Gestione rateizzazione addizionali territorio (seconda fase =
-- addebito della rata in esercizio successivo).
-- Memorizzazione anche del valore del saldo delle rate addizionali territorio accantonate
-- nell'esercizio precedente
--
-- Date: 09/12/2003
-- Version: 2.7
--
-- Fix interna. Forzato il passaggio delle date dell'esercizio nel calcolo delle detrazioni personali
--
-- Date: 10/12/2003
-- Version: 2.8
--
-- Fix interna. Errore in aggiornamento dell'importo rateizzato in conguaglio richiesta CINECA n. 471
--
-- Date: 16/12/2003
-- Version: 2.9
--
-- Fix errore CINECA n ???. In alcuni casi la procedura di calcolo conguaglio segnala erroneamente la
-- rimenenza di intervalli date negativi per recupero rate. (Recupero rate esterne all'intervallo)
--
-- Date: 30/01/2004
-- Version: 2.10
--
-- Corretto l'aggiornamento dei montanti al termine del conguaglio per allineare il montante irpef netto
-- al valore montante lordo - valore della deduzione. (vale solo per gestione a non dipendenti).
--
--
-- Date: 07/03/2005
-- Version: 2.11
--
-- Aggiornamento del pck per il conguaglio della FAMILY AREA
--
-- Date: 01/08/2006
-- Version: 2.12
--
-- Aggiunta la nuova "Gestione dei Cervelli"
--
-- Date: 15/05/2007
-- Version: 2.11
--
-- Adeguata la gestione dei Conguagli alla Finanziaria 2007
-- Gestiti gli Acconti per le addizionali Comunali
-- Gestite le esenzioni per le addizionali comunali
--
-- Date: 08/05/2009
-- Version: 2.12
--
-- Gestita la sospensione IRPEF per i terremotati che ne fanno richiesta:
-- 1) Importo Conguaglio IRPEF > 0 (deve essere emessa una reversale)
--    a) Se il conguaglio viene effettuato nel periodo in cui la sospensione è ancora attiva,
--       la ritenuta viene sospesa (IRPEF=0) e lo stesso importo viene aggiunto al sospeso precedente.
--    b) Se il conguaglio viene effettuato nel periodo in cui la sospensione non è ancora attiva,
--       la ritenuta viene calcolata come al solito e il totale del sospeso resta quello precedente.
-- 2) Importo Conguaglio IRPEF < 0 (deve essere emesso un mandato)
--    a) Qualunque sia il periodo in cui viene fatto il conguaglio, se esiste un importo sospeso precedente,
--       questo viene diminuito dell'irpef calcolata, fino alla capienza.
-- - L'importo sospeso presente sulla testata del conguaglio rappresenta il totale del sospeso di
-- tutti i compensi che si stanno conguagliando + evenutale sospeso da conguaglio precedente
-- - L'importo sospeso presente su contributo_ritenuta del compenso generato dal conguaglio rappresenta
-- il totale del sospeso del percipiente
--
-- Date: 01/03/2011
-- Version: 2.13
--
-- Memorizzati i flag per le detrazioni direttamente nel conguaglio
-- Corretta anomalia nel calcolo dell'imponibile per i cervelloni: non veniva esclusa la quota esente
--
-- Date: 25/05/2014
-- Version: 2.14
-- Adeguamenti relativi al Bonus DL 66/2014
--==================================================================================================
--
-- Constants
--

--
-- Variabili globali
--

   TYPE recCreditoIrpef IS RECORD
       (
        tCdCori CONTRIBUTO_RITENUTA.cd_contributo_ritenuta%TYPE,
        tDtIniValCori TIPO_CONTRIBUTO_RITENUTA.dt_ini_validita%TYPE,
        tImCreditoIrpefGoduto CONTRIBUTO_RITENUTA.ammontare%TYPE,
        tImCreditoIrpefDovuto CONTRIBUTO_RITENUTA.ammontare%TYPE,
        tImCreditoMaxDovuto CONTRIBUTO_RITENUTA.ammontare%TYPE
       );
   TYPE tCreditoIrpef IS TABLE OF recCreditoIrpef
        INDEX BY BINARY_INTEGER;

   tabCreditoIrpef tCreditoIrpef;
   dataOdierna DATE;
   aDataIniEsercizio DATE;
   aDataFinEsercizio DATE;

   -- Valore del goduto e dovuto di un conguaglio

   glbImportoIrpefGoduto NUMBER(15,2);
   glbImportoFamilyGoduto NUMBER(15,2);
   glbImportoAddRegGoduto NUMBER(15,2);
   glbImportoAddProGoduto NUMBER(15,2);
   glbImportoAddComGoduto NUMBER(15,2);
   glbImportoAddComAccGoduto NUMBER(15,2);
   glbDetrazioniPeGoduto NUMBER(15,2);
   glbDetrazioniLaGoduto NUMBER(15,2);
   glbDetrazioniCoGoduto NUMBER(15,2);
   glbDetrazioniFiGoduto NUMBER(15,2);
   glbDetrazioniAlGoduto NUMBER(15,2);
   glbDetrazioniCuneoGoduto NUMBER(15,2);
   glbDeduzioneIrpefGoduto NUMBER(15,2);
   glbDeduzioneFamilyGoduto NUMBER(15,2);
   glbImportoIrpefSospesoGoduto NUMBER(15,2);
   glbImportoCreditoIrpefGoduto NUMBER(15,2);
   glbImportoBonusIrpefGoduto NUMBER(15,2);

   glbImportoIrpefDovuto NUMBER(15,2);
   glbImportoFamilyDovuto NUMBER(15,2);
   glbImportoAddRegDovuto NUMBER(15,2);
   glbImportoAddProDovuto NUMBER(15,2);
   glbImportoAddComDovuto NUMBER(15,2);
   glbDetrazioniPeDovuto NUMBER(15,2);
   glbDetrazioniLaDovuto NUMBER(15,2);
   glbDetrazioniCoDovuto NUMBER(15,2);
   glbDetrazioniFiDovuto NUMBER(15,2);
   glbDetrazioniAlDovuto NUMBER(15,2);
   glbDetrazioniCuneoDovuto NUMBER(15,2);
   glbDeduzioneIrpefDovuto NUMBER(15,2);
   glbDeduzioneFamilyDovuto NUMBER(15,2);
   glbImportoCreditoIrpefDovuto NUMBER(15,2);
   glbImportoBonusIrpefDovuto NUMBER(15,2);
   -- Valore addebito rate di esercizio precedente (rateizzazione addizionali territorio)

   glbImpAddRegRateEseprec NUMBER(15,2);
   glbImpAddProRateEseprec NUMBER(15,2);
   glbImpAddComRateEseprec NUMBER(15,2);

   glbEsisteCompensoCervelli VARCHAR2(1);
   glbImponibileLordoPercipiente NUMBER(15,2);
   glbTotalePrevidInail NUMBER(15,2);

   -- Valore lordo e netto dell'imponibile letto dai compensi inclusi nel conguaglio

   glbImponibileLordoIrpef NUMBER(15,2);
   glbImponibileLordoDetFam NUMBER(15,2);
   glbImponibileLordoDetPer NUMBER(15,2);
   glbImponibileNettoIrpef NUMBER(15,2);
   glbImponibileNettoDetFam NUMBER(15,2);
   glbImponibileNettoDetPer NUMBER(15,2);
   glbDeduzioneIrpef NUMBER(15,2);
   glbDeduzioneFamily NUMBER(15,2);

   -- Valore minimo e massimo delle date di competenza dei c0ompensi inclusi nel conguaglio

   glbDataMinCompetenza DATE;
   glbDataMaxCompetenza DATE;

   -- Valore complessivo dei giorni reali coperti dalle date di competenza dei compensi inclusi
   -- nel conguaglio

   glbNumeroGiorni INTEGER;

   -- Memorizza gestione no detrazioni altre per il soggetto anagrafico

   glbFlNoDetrazioniAltre CHAR(1);

   -- Memorizza gestione no detrazioni family per il soggetto anagrafico

   glbFlNoDetrazioniFamily CHAR(1);

   -- Memorizza gestione detrazioni altri tipi per il soggetto anagrafico

   glbFlDetrazioniAltriTipi CHAR(1);

 -- Memorizza gestione no credito IRPEF per il soggetto anagrafico

   glbFlNoCreditoIrpef CHAR(1);
   glbFlNoCreditoCuneoIrpef CHAR(1);
   glbFlNoDetrCuneoIrpef CHAR(1);

   -- Memorizza il Reddito Complessivo e quello della prima casa presenti in anagrafica

   glbRedditoComplessivo       NUMBER(15,2);
   glbRedditoAbitazPrincipale  NUMBER(15,2);

   -- Memorizza il Totale del Reddito Complessivo, sia del CNR (anche DIP) che presente in anagrafica, escluso Reddito Abitazione principale
   --aTotRedditoComplessivo   NUMBER(15,2);


   glbImponibilePagatoDip    NUMBER(15,2);

   -- Memorizza gestione Applica detrazione personale massima per il soggetto anagrafico

   glbFlApplicaDetrPersMax CHAR(1);

   -- Memorizza il conguaglio in eaborazione

   aRecConguaglio CONGUAGLIO%ROWTYPE;
   aRecTipoTrattamento TIPO_TRATTAMENTO%ROWTYPE;

   -- Memorizza il soggetto anagrafico in eaborazione

   aRecAnagrafico ANAGRAFICO%ROWTYPE;

   -- Definizione tabella PL/SQL di appoggio per settagglio delle date

   tabella_date CNRCTB545.intervalloDateTab;
   tabella_date_ok CNRCTB545.intervalloDateTab;
   tabella_date_fam CNRCTB545.intervalloDateTab;
   tabella_date_fam_ok CNRCTB545.intervalloDateTab;
   tabella_date_fam_ok_unite CNRCTB545.intervalloDateTab;
   tabella_date_per CNRCTB545.intervalloDateTab;
   tabella_date_per_ok CNRCTB545.intervalloDateTab;

   tabella_date_neg CNRCTB545.intervalloDateTab;
   tabella_date_neg_ok CNRCTB545.intervalloDateTab;
   tabella_date_fam_neg CNRCTB545.intervalloDateTab;
   tabella_date_fam_neg_ok CNRCTB545.intervalloDateTab;
   tabella_date_per_neg CNRCTB545.intervalloDateTab;
   tabella_date_per_neg_ok CNRCTB545.intervalloDateTab;

   -- Numero dei mesi dei compensi inseriti nel conguaglio e relativi all'anno precedente
   numero_mesi_anno_precedente NUMBER;

   -- Numero dei mesi dei compensi inseriti nel conguaglio e relativi all'anno successivo
   numero_mesi_anno_successivo NUMBER;

   -- Dichiarazione di un cursore generico

   TYPE GenericCurTyp IS REF CURSOR;

--
-- Functions e Procedures
--

----------------------------------------------------------------------------------------------------
-- MAIN PROCEDURE
----------------------------------------------------------------------------------------------------

-- Verifica ammissibibilita del conguaglio per un dato soggetto anagrafico. Se il conguaglio è
-- ammesso si recuperano i dati esterni dell'ultimo conguaglio registrato e si riportano sul corrente.

   PROCEDURE abilitaConguaglio
      (
       inCdsConguaglio CONGUAGLIO.cd_cds%TYPE,
       inUoConguaglio CONGUAGLIO.cd_unita_organizzativa%TYPE,
       inEsercizioConguaglio CONGUAGLIO.esercizio%TYPE,
       inPgConguaglio CONGUAGLIO.pg_conguaglio%TYPE
      );

   PROCEDURE creaCompensoConguaglio
      (
       inCdsConguaglio CONGUAGLIO.cd_cds%TYPE,
       inUoConguaglio CONGUAGLIO.cd_unita_organizzativa%TYPE,
       inEsercizioConguaglio CONGUAGLIO.esercizio%TYPE,
       inPgConguaglio CONGUAGLIO.pg_conguaglio%TYPE,
       inCdsCompenso COMPENSO.cd_cds%TYPE,
       inUoCompenso COMPENSO.cd_unita_organizzativa%TYPE,
       inEsercizioCompenso COMPENSO.esercizio%TYPE,
       inPgCompenso COMPENSO.pg_compenso%TYPE
      );

   PROCEDURE upgKeyAssCompensoConguaglio
      (
       inCdsConguaglio CONGUAGLIO.cd_cds%TYPE,
       inUoConguaglio CONGUAGLIO.cd_unita_organizzativa%TYPE,
       inEsercizioConguaglio CONGUAGLIO.esercizio%TYPE,
       inPgConguaglioOld CONGUAGLIO.pg_conguaglio%TYPE,
       inPgConguaglioNew CONGUAGLIO.pg_conguaglio%TYPE,
       inCdsCompenso COMPENSO.cd_cds%TYPE,
       inUoCompenso COMPENSO.cd_unita_organizzativa%TYPE,
       inEsercizioCompenso COMPENSO.esercizio%TYPE,
       inPgCompensoOld COMPENSO.pg_compenso%TYPE,
       inPgCompensoNew COMPENSO.pg_compenso%TYPE
      );

-- Elimina le associazioni al compenso congualio presenti in ASS_COMPENSO_CONGUAGLIO e in RATEIZZA_CLASSIFIC_CORI

   PROCEDURE eliminaAssCompensoConguaglio
      (
       inCdsConguaglio CONGUAGLIO.cd_cds%TYPE,
       inUoConguaglio CONGUAGLIO.cd_unita_organizzativa%TYPE,
       inEsercizioConguaglio CONGUAGLIO.esercizio%TYPE,
       inPgConguaglio CONGUAGLIO.pg_conguaglio%TYPE
      );

   PROCEDURE eseguiDelConguaglio
      (
       esercizioScrivania CONGUAGLIO.esercizio%TYPE,
       inCdsConguaglio CONGUAGLIO.cd_cds%TYPE,
       inUoConguaglio CONGUAGLIO.cd_unita_organizzativa%TYPE,
       inEsercizioConguaglio CONGUAGLIO.esercizio%TYPE,
       inPgConguaglio CONGUAGLIO.pg_conguaglio%TYPE,
       inUtente CONGUAGLIO.utuv%TYPE,
       statoCancella IN OUT NUMBER
      );

----------------------------------------------------------------------------------------------------
-- ROUTINE COMUNI
----------------------------------------------------------------------------------------------------
-- Lettura dati di base legati al conguaglio in calcolo, testata conguaglio e anagrafico.
-- I dati sono memorizzati rispettivamente in aRecConguaglio e aRecAnagrafico

   PROCEDURE getDatiBaseConguaglio
      (
       inCdsConguaglio CONGUAGLIO.cd_cds%TYPE,
       inUoConguaglio CONGUAGLIO.cd_unita_organizzativa%TYPE,
       inEsercizioConguaglio CONGUAGLIO.esercizio%TYPE,
       inPgConguaglio CONGUAGLIO.pg_conguaglio%TYPE,
       eseguiLock CHAR
      );

-- Lettura, se esiste, dell'ultimo conguaglio presente nel sistema per il soggetto anagrafico in
-- elaborazione. Le informazioni definite nelle variabili _DOVUTO sono portate in _GODUTO del
-- nuovo conguaglio.

   PROCEDURE getUltimoConguaglio
      (
       inEsercizioConguaglio CONGUAGLIO.esercizio%TYPE
      );

-- Lettura di tutti i compensi registrati per il soggetto anagrafico in elaborazione, sono esclusi i
-- soli compensi da conguaglio. Da questa elaborazione si estraggono gli intervalli temporali per il
-- ricalcolo delle detrazioni familiari e personali e gli imponibili di riferimento per l'elaborazione.
-- I valori portati dai compensi mai compresi in un precedente conguaglio concorrono alla formazione del _GODUTO

   PROCEDURE leggiCompensiPerConguaglio;

-- Scarico intervalli temporali per recupero rate

   PROCEDURE scaricaRecuperoRate
      (
       intervallo_date IN OUT CNRCTB545.intervalloDateTab,
       intervallo_date_neg IN OUT CNRCTB545.intervalloDateTab
      );

-- Eliminazione degli intervalli sovrapposti sulla matrice date

   PROCEDURE normalizzaMatriceDate
      (
       intervallo_date IN OUT CNRCTB545.intervalloDateTab,
       intervallo_date_ok IN OUT CNRCTB545.intervalloDateTab
      );

-- Unisce gli intervalli contigui

   PROCEDURE unisciMatriceDate
      (
       inEsercizioCompenso In NUMBER,
       intervallo_date_ok IN OUT CNRCTB545.intervalloDateTab,
       intervallo_date_ok_unite IN OUT CNRCTB545.intervalloDateTab
      );

-- Storna intervalli spuri di recupero rate

   PROCEDURE chkSpuriRecuperoRate
      (
       intervallo_date_neg CNRCTB545.intervalloDateTab
      );

-- Inserimento dei record in ASS_COMPENSO_CONGUAGLIO

   PROCEDURE componiAssCompensoConguaglio
      (
       inCdsCompenso COMPENSO.cd_cds%TYPE,
       inUoCompenso COMPENSO.cd_unita_organizzativa%TYPE,
       inEsercizioCompenso COMPENSO.esercizio%TYPE,
       inPgCompenso COMPENSO.pg_compenso%TYPE
      );

-- Determina il netto delle detrazioni personali e familiari se IRPEF risulta inferiore al totale
-- calcolato di queste

   PROCEDURE nettizzaDetrazioni
      (
       aImporto NUMBER,
       aImDetrazioniPeNetto IN OUT NUMBER,
       aImDetrazioniLaNetto IN OUT NUMBER,
       aImDetrazioniCoNetto IN OUT NUMBER,
       aImDetrazioniFiNetto IN OUT NUMBER,
       aImDetrazioniAlNetto IN OUT NUMBER,
       aImDetrazioniCuneoNetto IN OUT NUMBER
      );

-- Cancellazione del conguaglio

   FUNCTION cancellaConguaglio
      (
       esercizioScrivania CONGUAGLIO.esercizio%TYPE,
       inCdsConguaglio CONGUAGLIO.cd_cds%TYPE,
       inUoConguaglio CONGUAGLIO.cd_unita_organizzativa%TYPE,
       inEsercizioConguaglio CONGUAGLIO.esercizio%TYPE,
       inPgConguaglio CONGUAGLIO.pg_conguaglio%TYPE,
       inUtente CONGUAGLIO.utuv%TYPE
      ) RETURN NUMBER;

-- Ritorna i record di RATEIZZA_CLASSIFIC_CORI o RATEIZZA_CLASSIFIC_CORI_S per addizionali territorio

   PROCEDURE getRateizzaAddTerritorio
      (
       aEsercizio NUMBER,
       aCdsConguaglio CONGUAGLIO.cd_cds%TYPE,
       aUoConguaglio CONGUAGLIO.cd_unita_organizzativa%TYPE,
       aPgConguaglio CONGUAGLIO.pg_conguaglio%TYPE,
       aCdAnag NUMBER,
       isStorico CHAR,
       isTemporaneo CHAR,
       eseguiLock CHAR,
       aRecRateizzaClassificCoriC0 IN OUT RATEIZZA_CLASSIFIC_CORI%ROWTYPE,
       aRecRateizzaClassificCoriP0 IN OUT RATEIZZA_CLASSIFIC_CORI%ROWTYPE,
       aRecRateizzaClassificCoriR0 IN OUT RATEIZZA_CLASSIFIC_CORI%ROWTYPE
      );

-- Aggiornamento della tabella RATEIZZA_CLASSIFIC_CORI_S per inserimento o cancellazione di conguaglio

   PROCEDURE upgStrRateizzaAddTerritorio
      (
       tiInserimentoCancellazione CHAR,
       aRecConguaglio CONGUAGLIO%ROWTYPE,
       aCdAnag NUMBER,
       isTemporaneo CHAR
      );

-- Aggiornamento della tabella RATEIZZA_CLASSIFIC_CORI per modifica dell'importo rateizzato

   PROCEDURE upgImRateizzatoAddTerritorio
      (
       tiInserimentoCancellazione CHAR,
       aRecConguaglio CONGUAGLIO%ROWTYPE,
       aCdAnag NUMBER,
       isTemporaneo CHAR
      );
   Procedure calcolaDetFam
      (
       inEsercizioConguaglio CONGUAGLIO.esercizio%TYPE,
       aTotRedditoComplessivo NUMBER,
       anag ANAGRAFICO.cd_anag%TYPE,
       aImTotDetrazioniCo IN OUT NUMBER,
       aImTotDetrazioniFi IN OUT NUMBER,
       aImTotDetrazioniAl IN OUT NUMBER,
       aImTotDetrazioniFiS IN OUT NUMBER,
       esisteFiSenzaConiuge CHAR
      );

-- Calcolo del credito irpef

   procedure calcolaCreditoIrpefDovuto
      (
        aImportoRiferimento in NUMBER,
        inNumGGTotMinPerCredito in INTEGER,
        aRecCompenso in COMPENSO%ROWTYPE,
        aDataMin in date,
        aDataMax in date,
        aCdCori in tipo_contributo_ritenuta.cd_contributo_ritenuta%type,
        dt_ini_val_cori in tipo_contributo_ritenuta.dt_ini_validita%type,
        totImportoDetrazioni in out number
      ) ;
END CNRCTB650;
