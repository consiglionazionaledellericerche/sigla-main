--------------------------------------------------------
--  DDL for Package CNRCTB600
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRCTB600" AS
-- ==================================================================================================
--
-- CNRCTB600 - package di utilità per gestione MINICARRIERE
--
-- Date: 21/07/2003
-- Version: 1.25
--
-- Dependency:
--
-- History:
--
-- Date: 26/06/2002
-- Version: 1.0
--
-- Creazione package
--
-- Date: 26/07/2002
-- Version: 1.1
--
-- Inserite routine per check tipologa rata mensile o no di una minicarriera
--
-- Date: 26/07/2002
-- Version: 1.2
--
-- Adeguamento per modifiche alle routine standard delle date
--
-- Date: 01/08/2002
-- Version: 1.3
--
-- Modifiche concordate con cineca per gestione minicarriere
--
-- Date: 02/08/2002
-- Version: 1.4
--
-- Modifica routine ceck per tassazione separata
--
-- Date: 07/08/2002
-- Version: 1.5
--
-- Modifica routine per controllo della cancellazione di un compenso da minicarriera
--
-- Date: 24/09/2002
-- Version: 1.6
--
-- Modifica package per inserimento di nuovi campi in testata della minicarriera
-- ti_istituz_commerc, fl_tassazione_separata
--
-- Date: 25/09/2002
-- Version: 1.7
--
-- Inserito il controllo applicativo di sollevare un'errore nel caso in cui sia richiesto, per una
-- minicarriera a tassazione separata, di generare una sola rata quando queste sono, per le regole
-- definite, maggiori di 1
--
-- Date: 12/12/2002
-- Version: 1.8
--
-- Inserita routine per:
-- - Calcolo valore complessivo, per esercizio, delle rate di minicarriera attive per un dato terzo.
-- - Valore complessivo delle rate, di una data minicarierra, che non risultano associate a compensi
--
-- Date: 12/12/2002
-- Version: 1.9
--
-- Fix errori in test della procedura di calcolo valore complessivo, per esercizio, delle rate di
-- minicarriera attive per un dato terzo
--
-- Date: 13/12/2002
-- Version: 1.10
--
-- Fix errori in test della procedura di calcolo valore complessivo, per esercizio, delle rate di
-- minicarriera attive per un dato terzo (esclusione cloni minicarriera)
--
-- Date: 17/12/2002
-- Version: 1.11
--
-- Inserita routine per calcolo dell'aliquota media IRPEF per gestione a tassazione separata
--
-- Date: 07/01/2003
-- Version: 1.12
--
-- Completata routine per calcolo dell'aliquota media IRPEF per gestione a tassazione separata
--
-- Date: 09/01/2003
-- Version: 1.13
--
-- Definita la memorizzazione dell'aliquota media IRPEF per gestione a tassazione separata come valore
-- non diviso per 100
--
-- Date: 27/01/2003
-- Version: 1.14
--
-- Modificata routine getImRateDiTerzoInEsercizio per includere anche il riferimento alla chiave di
-- una minicarriera se il compenso è creato contestualmente alla minicarriera stessa (questa
-- presenta un valore in pg_minicarriera < 1.
--
-- Date: 27/01/2003
-- Version: 1.15
--
-- Modificata la routine getImRateDiTerzoInEsercizio per operare sul codice anagrafico e non sul terzo.
-- Inserito il controllo di esclusione delle rate a tassazione separata.
-- Inserito controllo di sollevare eccezione se l'importo recuperato è pari a zero
--
-- Date: 28/02/2003
-- Version: 1.16
--
-- Modificata il calcolo dell'aliquota media IRPEF per tassazione separata per accedere agli scaglioni
-- con la data di di due esercizi prima dell'esercizio della minicarriera =
-- 31/12/(esercizio_minicarriera - 2)
--
-- Date: 20/03/2003
-- Version: 1.17
--
-- Richiesta CINECA n.545. Mapping nuovi attributi (fl_escludi_qvaria_deduzione) in tabella MINICARRIERA
--
-- Date: 24/03/2003
-- Version: 1.18
--
-- Richiesta CINECA n.541. Revisione del calcolo dell'aliquota fiscale media in pagamento compensi da
-- minicarriera. Modifica metodo getImRateDiTerzoInEsercizio, non si esporta più semplicemente il valore
-- totale delle rate di minicarriera con scadenza nell'esercizio per un dato soggetto anagrafico ma
-- a rottura di ogni trattamento, si procede al calcolo del vero imponibile fiscale.
--
-- Date: 27/03/2003
-- Version: 1.19
--
-- Richiesta CINECA n.542. Revisione del calcolo della deduzione IRPEF per minicarriere. Inserita routine
-- di calcolo del numero complessivo di giorni per le rate di minicarriera in calcolo
--
-- Date: 28/03/2003
-- Version: 1.20
--
-- Fix errore interno n.3386. Errato parametro in normalizza matrice date proprie della minicarriera
-- da cui si origina il compenso
-- Fix errore interno n. 3384. Inserito messaggio di errore nel caso in cui si recuperino giorni da
-- minicarriera pari a zero (tutte le scadenze sono in esercizio successivo o precedente)
--
-- Date: 31/03/2003
-- Version: 1.21
--
-- Richiesta CINECA n.551. Arrotondare il calcolo dell'aliquota media per tassazione separata a 2 decimali
--
-- Date: 09/04/2003
-- Version: 1.22
--
-- Richiesta CINECA. Inclusa, nelle routine getImRateDiTerzoInEsercizio e getNumeroGGRateMcarriera, la
-- gestione delle minicarriere cessate e sospese recuperando per queste i valori delle sole rate che risultano
-- associate a compensi.
--
-- Date: 14/04/2003
-- Version: 1.23
--
-- Richiesta CINECA. Completata richiesta di includere, nelle routine getImRateDiTerzoInEsercizio e
-- getNumeroGGRateMcarriera, la gestione delle minicarriere cessate e sospese recuperando per queste
-- i valori delle sole rate che risultano associate a compensi (Questa gestione è estesa anche alle
-- minicarriere ripristinate e/o rinnovate che ora hanno gestione analoga a quelle attive
--
-- Date: 15/07/2003
-- Version: 1.24
--
-- Richiesta CINECA n. ???. Attivazione gestione recupero rate.
-- Revisione chiamata CNRCTB545.componiMatriceDate
--
-- Date: 21/07/2003
-- Version: 1.25
--
-- Richiesta CINECA n. 620. Controllo tipo trattamento nel calcolo della deduzione da applicare al
-- compenso da minicarriera
-- Nel calcolo della deduzione si deve tener conto solo delle minicarriere che hanno un trattamento
-- con FL_SOGGETTO_CONGUAGIO = Y.
-- La formula era:
--      Deduzione teorica:
--      Dt = 7500 * (n° complessivo dei giorni delle rate nell'anno delle minicarriere a quel percipiente
--                   togliendo le intersezioni e controllando che il valore totale non superi 365)/365
-- deve diventare:
--      Deduzione teorica:
--      Dt = 7500 * (n° complessivo dei giorni delle rate nell'anno delle minicarriere con trattamento
--                   che hanno FL_SOGGETTO_CONGUAGIO = Y a quel percipiente togliendo le intersezioni
--                   e controllando che il valore totale non superi 365)/365
--
-- Date: 28/05/2008
-- Version: 1.26
--
-- Nella function getAliquotaMediaIrpef che restituisce il valore dell'aliquota media irpef per calcolo
-- minicarriera a tassazione separata, è stata modificata aDataAccessoScaglione inserendo aEsercizioMCarriera
-- invece di (aEsercizioMCarriera - 2)
--
--
-- Date: 25/05/2014
-- Version: 1.27
-- Adeguamenti relativi al Bonus DL 66/2014
-- Inserita routine getNumeroGGRateInPeriodo per il calcolo del numero complessivo di giorni per le rate di minicarriera
-- con scadenza nell'anno e competenza in un dato intervallo
-- ==================================================================================================
--
-- Constants
--

   -- Variabili globali

   -- Dichiarazione di un cursore generico

   TYPE GenericCurTyp IS REF CURSOR;

   -- Tabelle PL/SQL

   -- Matrice di appoggio per montanti

   TYPE rateRec IS RECORD
       (
        tDtInizio MINICARRIERA_RATA.dt_inizio_rata%TYPE,
        tDtFine MINICARRIERA_RATA.dt_fine_rata%TYPE,
        tDtScadenza MINICARRIERA_RATA.dt_scadenza%TYPE,
        tImRata MINICARRIERA_RATA.im_rata%TYPE
       );
   TYPE rateTab IS TABLE OF rateRec
        INDEX BY BINARY_INTEGER;

   -- Matrice di appoggio importi per trattamento

   TYPE imTrattamentoRec IS RECORD
       (
        tCdTrattamento MINICARRIERA.cd_trattamento%TYPE,
        tImportoLordoIrpef MINICARRIERA.im_totale_minicarriera%TYPE,
        tImponibileIrpef MINICARRIERA.im_totale_minicarriera%TYPE
       );
   TYPE imTrattamentoTab IS TABLE OF imTrattamentoRec
        INDEX BY BINARY_INTEGER;

--
-- Functions e Procedures
--

----------------------------------------------------------------------------------------------------
-- MAIN PROCEDURE
----------------------------------------------------------------------------------------------------

-- Torna il valore dell'aliquota media irpef per calcolo minicarriera a tassazione separata

   FUNCTION getAliquotaMediaIrpef
      (
       aImponibile_eseprec2 NUMBER,
       aImponibile_eseprec1 NUMBER,
       aEsercizio NUMBER,
       aEsercizioMCarriera NUMBER,
       aDtRegMCarriera DATE,
       aTiAnagrafico CHAR,
       aCdTerzo NUMBER,
       aCdTipoRapporto VARCHAR2,
       aCdTipoTrattamento VARCHAR2
      ) RETURN NUMBER;

----------------------------------------------------------------------------------------------------
-- ROUTINE COMUNI
----------------------------------------------------------------------------------------------------

-- Ritorna un record della tabella MINICARRIERA

   FUNCTION getMinicarriera
      (
       aCdCds MINICARRIERA.cd_cds%TYPE,
       aCdUo MINICARRIERA.cd_unita_organizzativa%TYPE,
       aEsercizio MINICARRIERA.esercizio%TYPE,
       aPgMinicarriera MINICARRIERA.pg_minicarriera%TYPE,
       eseguiLock CHAR
      ) RETURN MINICARRIERA%ROWTYPE;

-- Inserimento in MINICARRIERA

   PROCEDURE insMinicarriera
      (
       aRecMinicariera MINICARRIERA%ROWTYPE
      );

-- Inserimento righe in MINICARRIERA_RATA

   PROCEDURE insMinicarrieraRate
      (
       aRecMCarrieraRata MINICARRIERA_RATA%ROWTYPE
      );

-- Cancellazione della testata di una minicarriera

   PROCEDURE delMinicarriera
      (
       aCdCds MINICARRIERA.cd_cds%TYPE,
       aCdUnitaOrganizzativa MINICARRIERA.cd_unita_organizzativa%TYPE,
       aEsercizio MINICARRIERA.esercizio%TYPE,
       aPgMinicarriera MINICARRIERA.pg_minicarriera%TYPE
      );

-- Cancellazione di tutte le rate di una minicarriera

   PROCEDURE delMinicarrieraRate
      (
       aCdCds MINICARRIERA_RATA.cd_cds%TYPE,
       aCdUnitaOrganizzativa MINICARRIERA_RATA.cd_unita_organizzativa%TYPE,
       aEsercizio MINICARRIERA_RATA.esercizio%TYPE,
       aPgMinicarriera MINICARRIERA_RATA.pg_minicarriera%TYPE
      );

-- Verifica se le rate sono mensili per minicarriere non gestite a tassazione separata

   FUNCTION chkRateMensiliNormale
      (
       aRecMinicarriera MINICARRIERA%ROWTYPE
       ) RETURN CHAR;

-- Verifica se le rate sono mensili per minicarriere gestite a tassazione separata

   FUNCTION chkRateMensiliTassazSep
      (
       aRecMinicarriera MINICARRIERA%ROWTYPE
       ) RETURN CHAR;

-- Ritorna la data di scadenza di una rata (prima o seconda) per i controlli sulle minicarriere a tassazione separata

   FUNCTION getDataScadenzaRata
      (
       aRecMinicarriera MINICARRIERA%ROWTYPE,
       isRataMeseIntero CHAR,
       isRataMensile CHAR,
       aRataCalcolo INTEGER,
       aDataScadPrimaRata DATE
      ) RETURN DATE;

-- Elimina l'associazione di un compenso ad una rata di minicarriera

   PROCEDURE sganciaAssCompensoMCarriera
      (
       aCdsCompenso COMPENSO.cd_cds%TYPE,
       aUoCompenso COMPENSO.cd_unita_organizzativa%TYPE,
       aEsercizio COMPENSO.esercizio%TYPE,
       aPgCompenso COMPENSO.pg_compenso%TYPE
      );

-- Torna il valore complessivo delle rate di minicarriere attive definite per un dato terzo e con
-- scadenza in un dato esercizio

   FUNCTION getImRateDiTerzoInEsercizio
      (
       aRecAnagrafico ANAGRAFICO%ROWTYPE,
       aRecCompenso COMPENSO%ROWTYPE,
       aCdCdsMcarriera MINICARRIERA.cd_cds%TYPE,
       aCdUoMcarriera MINICARRIERA.cd_unita_organizzativa%TYPE,
       aEsercizioMcarriera MINICARRIERA.esercizio%TYPE,
       aPgMcarriera MINICARRIERA.pg_minicarriera%TYPE
      ) RETURN NUMBER;

-- Torna l'ammontare complessivo, la minima data inizio, la massima data fine ed il numero delle rate
-- non ancora liquidate data una minicarriera

   PROCEDURE getNonLiquidatoRate
      (
       aCdCds MINICARRIERA_RATA.cd_cds%TYPE,
       aCdUnitaOrganizzativa MINICARRIERA_RATA.cd_unita_organizzativa%TYPE,
       aEsercizio MINICARRIERA_RATA.esercizio%TYPE,
       aPgMinicarriera MINICARRIERA_RATA.pg_minicarriera%TYPE,
       aImporto IN OUT NUMBER,
       aDataMin IN OUT DATE,
       aDataMax IN OUT DATE,
       aNumeroRate IN OUT NUMBER
      );

-- Inserita routine di calcolo del numero complessivo di giorni per le rate di minicarriera per revisione
-- calcolo della deduzione IRPEF

   PROCEDURE getNumeroGGRateMcarriera
      (
       aRecAnagrafico ANAGRAFICO%ROWTYPE,
       aRecCompenso COMPENSO%ROWTYPE,
       aCdCdsMcarriera MINICARRIERA.cd_cds%TYPE,
       aCdUoMcarriera MINICARRIERA.cd_unita_organizzativa%TYPE,
       aEsercizioMcarriera MINICARRIERA.esercizio%TYPE,
       aPgMcarriera MINICARRIERA.pg_minicarriera%TYPE,
       aNumeroGGTutti IN OUT INTEGER,
       aNumeroGGProprio IN OUT INTEGER
      );

   PROCEDURE getNumeroGGRateMcarriera
      (
       aRecAnagrafico ANAGRAFICO%ROWTYPE,
       aRecCompenso COMPENSO%ROWTYPE,
       aCdCdsMcarriera MINICARRIERA.cd_cds%TYPE,
       aCdUoMcarriera MINICARRIERA.cd_unita_organizzativa%TYPE,
       aEsercizioMcarriera MINICARRIERA.esercizio%TYPE,
       aPgMcarriera MINICARRIERA.pg_minicarriera%TYPE,
       aDataInizio date,
       aDataFine date,
       aNumeroGGTutti IN OUT INTEGER,
       aNumeroGGProprio IN OUT INTEGER
      );

-- Inserita routine di calcolo del numero complessivo di Mesi per le rate di minicarriera per revisione
-- calcolo della deduzione FAMILY

   PROCEDURE getNumeroMMRateMcarriera
      (
       aRecAnagrafico ANAGRAFICO%ROWTYPE,
       aRecCompenso COMPENSO%ROWTYPE,
       aCdCdsMcarriera MINICARRIERA.cd_cds%TYPE,
       aCdUoMcarriera MINICARRIERA.cd_unita_organizzativa%TYPE,
       aEsercizioMcarriera MINICARRIERA.esercizio%TYPE,
       aPgMcarriera MINICARRIERA.pg_minicarriera%TYPE,
       aNumeroMMTutti IN OUT INTEGER,
       aNumeroMMProprio IN OUT INTEGER,
       aNumeroMMEsercizio IN OUT INTEGER,
       aOrigineCompenso INTEGER
      );

PROCEDURE getNumeroGGRateInPeriodo
   (
    aCdTerzo COMPENSO.CD_TERZO%TYPE,
    aEsercizioCompenso  COMPENSO.ESERCIZIO%TYPE,
    aDtDaCompetenza  COMPENSO.DT_DA_COMPETENZA_COGE%TYPE,
    aDtACompetenza COMPENSO.DT_A_COMPETENZA_COGE%TYPE,
    aNumeroGGTutti IN OUT INTEGER
   );

-- Eliminazione degli intervalli sovrapposti sulla matrice date

   PROCEDURE normalizzaMatriceDate
      (
       intervallo_date IN OUT CNRCTB545.intervalloDateTab,
       intervallo_date_ok IN OUT CNRCTB545.intervalloDateTab
      );

END CNRCTB600;
