--------------------------------------------------------
--  DDL for Package CNRCTB610
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRCTB610" AS
--==================================================================================================
--
-- CNRCTB610 - Liquidazione massiva delle minicarriere
--
-- Date: 14/07/2006
-- Version: 2.2
--
-- Dependency: CNRCTB 610 IBMERR 001 IBMULL 001
--
-- History:
--
-- Date: 14/01/2003
-- Version: 1.0
--
-- Creazione Package.
--
--
-- Date: 15/01/2003
-- Version: 1.1
--
-- Aggiunto la scrittura su Log dei documenti contabili generati dalla
-- liquidazione del compenso
--
--
-- Date: 15/01/2003
-- Version: 1.2
--
-- Errore: la data di registrazione del compenso veniva inizializzata con un
--         timestamp e non con una data (trunc(sysdate))
--
--
-- Date: 15/01/2003
-- Version: 1.3
--
-- Modificato scrittura nella tabella di LOG
--
--
-- Date: 20/01/2003
-- Version: 1.4
--
-- STM 3172 - Corretto l'aggiornamento del campo STATO_ASS_COMPENSO della rata)
-- STM 3175 - Corretto la gestione della minicarriera a tassazione separata)
-- STM 3180 - Corretto gestione numerazione compenso
-- lettura da Tabella Configurazione CNR non del campo TRATTAMENTO_IVA ma del
-- campo VOCE_IVA
--
-- Date: 20/01/2003
-- Version: 1.5
--
-- Corretto gestione progressivi
--
--
-- Date: 22/01/2003
-- Version: 1.6
--
-- Aggiunto chiamata alla procedura di aggiornamento dei montanti (per compenso)
-- Aggiunto inizializzazione di due nuovi campi del compenso (im_deduzione_irpef e
-- imponibile_fiscale_netto)
--
--
-- Date: 24/01/2003
-- Version: 1.7
--
-- Modificato chiamata alla procedura "elaboraCompenso" - Ci sono 4 nuovi parametri
--
--
-- Date: 27/01/2003
-- Version: 1.8
--
-- Aggiunto inizializzazione di un nuovo campo del compenso (numero_giorni)
--
--
-- Date: 29/01/2003
-- Version: 1.9
--
-- Modificato chiamata a LOGSTART (aggiunto il parametro tipoLog e modificato valore
-- ultimi due parametri)
--
--
-- Date: 30/01/2003
-- Version: 1.10
--
-- Modificato inizializzazione 'pg_ver_rec' del compenso
--
-- Date: 12/03/2003
-- Version: 1.11
--
-- Tolto commit improprio al termine della procedura
--
-- Date: 25/03/2003
-- Version: 1.12
--
-- STM 3380 - Inizializzazione nuovi campi del COMPENSO
--
-- Date: 26/03/2003
-- Version: 1.13
--
-- Inizializzazione del campo FL_ESCLUDI_QVARIA_DEDUZIONE del compenso con quello della
-- minicarriera
--
-- Date: 28/03/2003
-- Version: 1.14
--
-- Modificato il valore dei parametri della chiamata ad "elaboraCompenso"
--
-- Date: 01/04/2003
-- Version: 1.15
--
-- Tolto "FOR UPDATE NOWAIT" nella lettura della tabella "VSX_LIQUIDAZIONE_RATE"
--
-- Date: 17/06/2003
-- Version: 1.16
--
-- L'esercizio del compenso viene inizializzato con l'esercizio di scrivania
-- Aggiunto un parametro alla chiamata della procedura "contabilizzaCompensoCOFI"
--
-- Date: 17/06/2003
-- Version: 1.17
--
-- Modificato l'inizializzazione della data di registrazione del compenso.
-- Se esercizio corrente > esercizio scrivania : dt_registrazione = 31/12/esercizio scrivania
--
-- Date: 19/06/2003
-- Version: 1.18
--
-- Modificato l'inizializzazione della data di registrazione dell'obbligazione creata per il
-- compenso.
-- Se esercizio corrente > esercizio scrivania : dt_registrazione = 31/12/esercizio scrivania
--
-- Date: 09/07/2003
-- Version: 1.19
--
-- Richiesta CINECA n. :1:2:3. Attivazione gestione recupero rate.
-- Mapping nuovo attributo fl_recupero_rate in tabella COMPENSO
--
-- Date: 09/09/2003
-- Version: 1.20
--
-- Ad ogni liquidazione di una rata verifico se l'esercizio e' aperto.
--
-- Date: 09/09/2003
-- Version: 1.21
--
-- Tolto il controllo sullo stato dell'esercizio introdotto con la precedente versione.
--
-- Date: 27/11/2003
-- Version: 2.0
--
-- Rilascio richiesta CINECA n. 471. Gestione rateizzazione addizionali territorio. (allinea DB)
--
-- Date: 06/02/2004
-- Version: 2.1
--
-- Date: 14/07/2006
-- Version: 2.2
-- Gestione Impegni/Accertamenti Residui:
-- aggiornata la funzione per tener conto anche del campo Esercizio Originale Impegno/Accertamento
--
-- Rilascio richiesta CINECA n. 732. Inserito ordinamento in sede di creazione massiva compensi da
-- minicarriera.
--
--==================================================================================================
--
-- Constants
--
   dataOdierna DATE:=trunc(sysdate);
   TYPE GenericCurTyp IS REF CURSOR;
   cdTipoDocumentoAmm NUMERAZIONE_DOC_AMM.CD_TIPO_DOCUMENTO_AMM%TYPE := 'COMPENSO';
   cdTipoDocumentoCont NUMERAZIONE_DOC_CONT.CD_TIPO_DOCUMENTO_CONT%TYPE := 'OBB';
   gPgLogCreazioneCompenso NUMBER:=0;
   gPgLogLiquidazioneCompenso NUMBER:=0;
   pgDocAmm NUMBER;
   docMandato CHAR := 'M';
   docReversale CHAR := 'R';
   descErrore VARCHAR2(200);
   -- flgCompensoCreato = TRUE = compenso creato con successo.
   flgFaseCreazione BOOLEAN;
   tipoLog VARCHAR2(20):='LIQUID_MC00';

----------------------------------------------------------------------------------------------------
-- MAIN PROCEDURE
----------------------------------------------------------------------------------------------------
--
-- Creazione automatica di un compenso da una rata di minicarriera
-- creaCompensoRataMinicarriera
--
-- Parametri:
-- pgCall -> numero di chiamata VSX determinato da applicazione JAVA

   PROCEDURE liquidazMassivaMinicarriere
      (
       pgCall    NUMBER,
       cdUO	     VARCHAR2,
   	   cdCds     VARCHAR2,
	   esercizio NUMBER,
	   aUtente   VARCHAR2
      );

-- Inizializzazione della regione_irap, tipologia_rischio, voce_iva del
-- compenso a seconda del codice trattamento
--
-- Parametri :
-- cd_trattamento_inail -> codice trattamento INAIL di default letto da configurazione CNR
-- cd_voce_iva -> codice voce IVA di default letto da configurazione CNR
-- aRecCompenso -> compenso da inizializzare

   FUNCTION inizializzaDatiLiquidazione
      (
	   		cd_trattamento_inail VARCHAR2,
			cd_voce_iva VARCHAR2,
			cd_regione_irap VARCHAR2,
			aRecCompenso COMPENSO%ROWTYPE
      )RETURN COMPENSO%ROWTYPE;

-- Inizializzazione, inserimento, elaborazione del compenso da rata minicarriera
--
-- Parametri :
-- aRecRataMinicarriera -> rata di minicarriera necessaria per inizializzare il compenso
-- 						   da creare

   FUNCTION creaCompensoRataMinicarriera
      (
	   		aRecMinicarriera MINICARRIERA%ROWTYPE,
	   		aRecRataMinicarriera MINICARRIERA_RATA%ROWTYPE,
			cd_trattamento_inail VARCHAR2,
		    cd_voce_iva VARCHAR2,
			cd_regione_irap VARCHAR2,
			esercizio NUMBER
      )RETURN COMPENSO%ROWTYPE;

-- Inizializzazione, inserimento dell'obbligazione per compenso
--
-- Parametri :
-- aRecCompenso -> compenso necessario per inizializzare l'oobligazione da creare

   PROCEDURE creaObbligazionePerCompenso
      (
  		aRecCompenso COMPENSO%ROWTYPE,
        aRecScadenzaObbligazione IN OUT OBBLIGAZIONE_SCADENZARIO%ROWTYPE,
		cd_elemento_voce VARCHAR2,
		esercizio_competenza NUMBER,
		ti_appartenenza VARCHAR2,
		ti_gestione VARCHAR2,
		cd_voce VARCHAR2,
		cd_centro_responsabilita VARCHAR2,
		cd_linea_attivita VARCHAR2
      );

--
--	Lettura della rata di minicarriera per la quale generare un compenso
--
   FUNCTION getRataMinicarriera
     (
       aCdCds VSX_LIQUIDAZIONE_RATE.cd_cds%TYPE,
       aCdUo VSX_LIQUIDAZIONE_RATE.cd_unita_organizzativa%TYPE,
       aEsercizio VSX_LIQUIDAZIONE_RATE.esercizio%TYPE,
       aPgMinicarriera VSX_LIQUIDAZIONE_RATE.pg_minicarriera%TYPE,
       aPgRata VSX_LIQUIDAZIONE_RATE.pg_rata%TYPE,
       eseguiLock CHAR
   ) RETURN MINICARRIERA_RATA%ROWTYPE;

--
--	Lettura da configurazione del Codice Trattamento INAIL
--
   FUNCTION getCdTrattamentoInail RETURN VARCHAR2;

--
--	Lettura da configurazione del Codice Voce IVA
--
   FUNCTION getCdVoceIva RETURN VARCHAR2;

--
--	Lettura della Regione IRAP
--
   FUNCTION getCdRegioneIrap
     (
	  cdUO VARCHAR2
	 ) RETURN VARCHAR2;

--
-- Lettura del terzo della minicarriera
--
   PROCEDURE getTerzoMinicarriera
      (
       cdTerzo MINICARRIERA.CD_TERZO%TYPE,
       pgComuneFiscale IN OUT NUMBER,
       cdProvincia IN OUT VARCHAR2,
       cdRegione IN OUT VARCHAR2
      );

--
--	Cancello dalla vista VSX_LIQUIDAZIONE_RATE le rate per le quali sono
--	stati generati dei compensi
--
   PROCEDURE cancellaRateMinicarriere
      (
       pgCall NUMBER
      );

--
--	Aggiorno la rata con i dati del compenso appena creato
--
   PROCEDURE aggiornaRataMinicarriera
      (
       aRecCompenso COMPENSO%ROWTYPE,
	   aRecRataMinicarriera	MINICARRIERA_RATA%ROWTYPE
      );

--
-- Aggiornamento del campo STATO_ASS_COMPENSO della minicarriera
--
   PROCEDURE aggiornaMinicarriera
     (
	   aRecMinicarriera	MINICARRIERA%ROWTYPE
     );

--
-- Aggiorno compenso con dati obbligazione
--
   PROCEDURE aggiornaCompensoObbligazione
      (
	    aRecCompenso COMPENSO%ROWTYPE,
		aRecScadenzaObbligazione OBBLIGAZIONE_SCADENZARIO%ROWTYPE
	  );

--
-- Metto lock alla numerazione del compenso e dell'obbligazione
--
   PROCEDURE setLockTabelleNumerazione
      (
	 	cdCds      VARCHAR2,
		cdUO       VARCHAR2,
		aEsercizio NUMBER,
		aUtente    VARCHAR2
	  );

--
-- Recupero i numeri dei documenti contabili generati dalla liquidazione
-- del compenso per scriverli nel Log (campo Note)
--
   FUNCTION getDocContCompenso
     (
	   aRecCompenso COMPENSO%ROWTYPE
	 ) RETURN VARCHAR2;

--
-- Ripristino l'ultimo prograssivo nella tabella NUMERAZIONE_DOC_AMM.
-- Al termine di ogni ciclo del loop incremento il progressivo del
-- compenso per poterlo aasegnare al successivo. Se pero' non ho piu' rate
-- da elaborare devo riaggiornare la tabella di numerazione diminuendo il
-- corrente di 1
--
   PROCEDURE aggiornaUltimoPgDocAmm
     (
	 	cdCds      VARCHAR2,
		cdUO       VARCHAR2,
		aEsercizio NUMBER,
		aUtente    VARCHAR2
	 );

END CNRCTB610;
