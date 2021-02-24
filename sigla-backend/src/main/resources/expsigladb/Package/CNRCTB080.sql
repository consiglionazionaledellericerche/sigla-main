--------------------------------------------------------
--  DDL for Package CNRCTB080
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRCTB080" as
--==================================================================================================
--
-- CNRCTB080 - Package di utilità ANAGRAFICO/TERZO
--
-- Date: 17/02/2004
-- Version: 2.17
--
-- Dependency: CNRCTB 015 IBMERR 001
--
-- History:
--
-- Date: 24/05/2002
-- Version: 1.0
--
-- Creazione
--
-- Date: 04/06/2002
-- Version: 1.1
--
-- Recupero della testata anagrafica relativa al terzo specificato
--
-- Date: 12/06/2002
-- Version: 1.2
--
-- Aggiunte le seguenti funzioni:
-- 1) Recupero del record MONTANTI dato un anagrafico di riferimento.
-- 2) Recupero del record RIF_INQUADRAMENTO dati il tipo rapporto, anagrafico e
--    data di riferimento
--
-- Date: 20/06/2002
-- Version: 1.3
-- Aggiunto recupero terzo e modalità di pagamento per una data UO
--
-- Date: 21/06/2002
-- Version: 1.4
-- Fix -> recupero inf. banca non cancellate
--
-- Date: 27/06/2002
-- Version: 1.5
--
-- Fix per modifica tabella INQUADRAMENTO
--
-- Date: 03/07/2002
-- Version: 1.6
-- Aggiunti comune e regione indefiniti
--
-- Date: 04/07/2002
-- Version: 1.7
-- Fix errore in estrazione INQUADRAMENTO mancavano attributi in join RAPPORTO, INQUADRAMENTO
--
-- Date: 15/07/2002
-- Version: 1.8
-- Introdotte routine di inserimento ed aggiornamento su tabella MONTANTI
--
-- Date: 16/07/2002
-- Version: 1.9
-- Introdotta routine di verifica esistenza detrazioni familiari
--
-- Date: 18/07/2002
-- Version: 2.0
-- Aggiornamento documentazione
--
-- Date: 23/07/2002
-- Version: 2.1
-- Fix errore in verifica esistenza detrazioni familiari
--
-- Date: 10/09/2002
-- Version: 2.2
-- Fix estrazione mod pag terzo UO
--
-- Date: 14/10/2002
-- Version: 2.3
-- Estrazione delle sole info banca valide (fl_Cancellato = 'N')
--
-- Date: 16/10/2002
-- Version: 2.4
-- Aggiunto metodo di estrazione del terzo per codice
--
-- Date: 08/11/2002
-- Version: 2.5
-- Inserito filtro di esclusione di modalità di pagamento e banca di tipo a cessionario nella procedura
-- getModPagUltime
--
-- Date: 10/12/2002
-- Version: 2.6
-- Gestione cc ente e cc cds in recupero modalità pagamento per terzo UO
--
-- Date: 07/01/2003
-- Version: 2.7
-- Definizione costanti per tipo entita
--
-- Date: 27/01/2003
-- Version: 2.8
--
-- Modifiche per finanziaria 2003
-- Inserita routine di lettura record da ANAGRAFICO_ESERCIZIO per recupero flag no tax area
--
-- Date: 27/01/2003
-- Version: 2.9
--
-- Modifiche per finanziaria 2003
-- Sistemazione routine di get ed insert montanti
--
-- Date: 29/01/2003
-- Version: 2.10
--
-- Invertito il dominio dell'attributo fl_notaxarea in ANAGRAFICO_ESERCIZIO
--
-- Date: 30/01/2003
-- Version: 2.11
--
-- Invertito il dominio dell'attributo fl_notaxarea in ANAGRAFICO_ESERCIZIO (adeguamento al client)
--
-- Date: 30/01/2003
-- Version: 2.12
--
-- Inserita gestione per attivazione generale del calcolo delle deduzioni IRPEF in routine getAnagFlNoTaxArea
--
-- Date: 24/02/2003
-- Version: 2.13
--
-- Valorizzate costanti per il recupero dell'anagrafico rappresentativo l'ente.
-- Inserito metodo di recupero del record di ANAGRAFICO dato un cd_anag
--
-- Date: 20/03/2003
-- Version: 2.14
--
-- Richiesta CINECA n. 547. Inserito metodo di recupero delle detrazioni personali impostate da utente
-- a valere in sede di conguaglio (tabella ANAGRAFICO_ESERCIZIO)
--
-- Date: 12/09/2003
-- Version: 2.15
--
-- Inserita gestione per controllo esistenza del terzo in area compensi (validazione per cessazione e rapporto)
-- chkEsisteTerzoPerCompenso. Fix errore interno 3635
--
-- Date: 12/02/2004
-- Version: 2.16
--
-- Migliorata gestione per controllo esistenza del terzo in area compensi (validazione per cessazione e rapporto)
-- chkEsisteTerzoPerCompenso. Errore CINECA n. 665
--
-- Date: 17/02/2004
-- Version: 2.17
--
-- Fix errore, manca order by su dt_inizio_validita in routine chkEsisteTerzoPerCompenso
--
-- Date: 18/01/2007
-- Version: 2.18
--
-- Adeguamenti Finanziaria 2007
-- Aggiunte le function "getAnagFlNoDetrazioniAltre", "getAnagFlNoDetrazioniFamily" e
-- "getAnagFlDetrazioniAltriTipi"
--
-- Date: 14/06/2011
-- Version: 2.19 - Riduzione Erariale per i dipendenti
--
-- Date: 11/01/2013
-- Version: 2.20 - Gestione montante INPGI
--
-- Date: 25/05/2014
-- Version: 2.21
-- Adeguamenti relativi al Bonus DL 66/2014
-- Aggiunta la function "getAnagFlNoCreditoIrpef" per recupero del flag no_credito_irpef
-- e la function getImponibilePagatoDip che ritorna il reddito di quanto eventualmente pagato
-- come dipendente (per il calcolo del reddito complessivo)
 --==================================================================================================
--
-- Constants:
--

   TI_PAGAMENTO_BANCARIO CONSTANT CHAR(1):='B';
   TI_PAGAMENTO_ALTRO CONSTANT CHAR(1):='A';
   TI_PAGAMENTO_POSTALE CONSTANT CHAR(1):='P';
   TI_PAGAMENTO_QUIETANZA CONSTANT CHAR(1):='Q';
   TI_PAGAMENTO_BANCA_ITALIA CONSTANT CHAR(1):='I';

   TI_ENTITA_DIVERSI CONSTANT CHAR(1):='D';
   TI_ENTITA_FISICA CONSTANT CHAR(1):='F';
   TI_ENTITA_GIURIDICA CONSTANT CHAR(1):='G';
   TI_ENTITA_UO CONSTANT CHAR(1):='U';

   CONTO_CORRENTE_SPECIALE CONSTANT VARCHAR2(50):='CONTO_CORRENTE_SPECIALE';
   CONTO_CORRENTE_SPECIALE_BI CONSTANT VARCHAR2(50):='CONTO_CORRENTE_SPECIALE_BI';
   ENTE CONSTANT VARCHAR2(50):='ENTE';

   REGIONE_INDEFINITA CONSTANT VARCHAR2(10):='*';
   COMUNE_INDEFINITO CONSTANT NUMBER(10):=0;

   ATTIVA_DEDUZIONE_KEY1 CONSTANT VARCHAR2(100):='ADEGUAMENTI_FIN_2003';
   ATTIVA_DEDUZIONE_KEY2 CONSTANT VARCHAR2(100):='FL_ATTIVO';

   CODICE_ANAG_ENTE_KEY1 CONSTANT VARCHAR2(100):='COSTANTI';
   CODICE_ANAG_ENTE_KEY2 CONSTANT VARCHAR2(100):='CODICE_ANAG_ENTE';

   ATTIVA_DEDUZIONE_KEY1_FAMILY CONSTANT VARCHAR2(100):='BASE_DEDUZIONE_FAMILY';


   -- Variabili globali

   TYPE GenericCurTyp IS REF CURSOR;

-- Functions e Procedures:

-- Ritorna il codice del terzo diversi di default definito in CONFIGURAZIONE_CNR

   FUNCTION getTerziDiversiDefault RETURN NUMBER;

-- Ritorna il record di ANAGRAFICO dato un TERZO.cd_terzo di riferimento

   FUNCTION getAnag (aCdTerzo NUMBER) RETURN ANAGRAFICO%ROWTYPE;

-- Ritorna il record di ANAGRAFICO dato un ANAGRAFICO.cd_anag di riferimento

   FUNCTION getAnagDaCdAnag (aCdAnag NUMBER) RETURN ANAGRAFICO%ROWTYPE;

-- Ritorna il record di MONTANTI dato un ANAGRAFICO.cd_anag di riferimento

   FUNCTION getMontanti
      (
       aEsercizio NUMBER,
       aCdAnag NUMBER,
       eseguiLock CHAR
      ) RETURN MONTANTI%ROWTYPE;

-- Inserisce un record in tabella MONTANTI

   PROCEDURE insMontanti
      (
       aRecMontante MONTANTI%ROWTYPE
      );

-- Aggiorna un record in tabella MONTANTI

   PROCEDURE upgMontantiAltri
      (
       aRecMontante MONTANTI%ROWTYPE
      );

-- Aggiorna un record in tabella MONTANTI per i DIP

   PROCEDURE upgMontantiDip
      (
       aRecMontante MONTANTI%ROWTYPE
      );

-- Ritorna il record di RIF_INQUADRAMENTO dato un codice anagrafico, un tipo rapporto e una data di
-- validità di riferimento

   FUNCTION getRifInquadramento
      (
       aCdTipoRapporto VARCHAR2,
       aCdAnag NUMBER,
       aDataRif DATE
      ) RETURN RIF_INQUADRAMENTO%ROWTYPE;

-- Verifica l'esistenza di una entrata in V_TERZO_PER_COMPENSO per controllo validazione (per cessazione e rapporto) del terzo

   FUNCTION chkEsisteTerzoPerCompenso
      (
       aCdTerzo NUMBER,
       aCdTipoRapporto VARCHAR2,
       aTiDipendenteAltro CHAR,
       aDtValidaTerzo DATE,
       aDtValidaRapportoIni DATE,
       aDtValidaRapportoFin DATE
      ) RETURN INTEGER;

-- Verifica l'esistenza di detrazioni familiari per un dato soggetto anagrafico

   FUNCTION chkEsisteDetrazFam
      (
       aCdAnag ANAGRAFICO.cd_anag%TYPE,
       aDataDa DATE,
       aDataA DATE
      ) RETURN INTEGER;

-- Verifica l'esistenza di carichi familiari non validi per un dato soggetto anagrafico

   FUNCTION chkEsisteCaricoFamNonValido
      (
       aCdAnag ANAGRAFICO.cd_anag%TYPE,
       aDataDa DATE,
       aDataA DATE
      ) RETURN INTEGER;

-- Ritorna il rowtype del terzo con codice aCdTerzo
 FUNCTION getTerzo(aCdTerzo number) return terzo%rowtype;

-- Ritorna il rowtype del terzo valido con codice anagrafico aCdAnag
 FUNCTION getTerzoDaCdAnag(aCdAnag number) return terzo%rowtype;

-- Ritorna le informazioni sul terzo legato all'Uo specificata
-- Estrae la modalità di pagamento bancario e informazione di tipo banca più recenti con i seguenti vincoli:
-- Se l'UO è di tipo ENTE -> Cerca nel terzo corrispondente una inf. banca con abi/cab e numero conto = a quelli dell'ente (CONFIG CNR)
-- Altrimenti -> Cerca nel terzo corrispondente una inf. banca targata fl_cc_cds = 'Y' (Conto corrente CDS).
-- Se non trova tali corrispondenze solleva errore
--
 PROCEDURE getTerzoPerUO(aCdUO varchar2, aCdTerzo out number, aCdModPag out varchar2, aPgBanca out number,aEsercizio in number);

-- Ritorna le informazioni sul terzo legato all'Uo ENTE
-- Estrae la modalità di pagamento (TIPO BANCA D'ITALIA) più recente con i seguenti vincoli:
-- IBAN deve essere quello presente in CONFIGURAZIONE_CNR avente come chiave primaria CONTO_CORRENTE_SPECIALE_BI
-- Se non trova tali corrispondenze solleva errore
--
 PROCEDURE getTerzoPerEnteContoBI(aCdUO varchar2, aCdTerzo out number, aCdModPag out varchar2, aPgBanca out number);

-- Estrae il codice del terzo collegato all'UO specificata
 PROCEDURE getTerzoPerUO(aCdUO varchar2, aCdTerzo out number);


-- Estrae l'ultima versione delle modalità di pagamento del tipo specificato per il terzo passato
-- Se aTipo = null prende le più recenti indipendentemente dal tipo
 PROCEDURE getModPagUltime(aCdTerzo number, aCdModPag out varchar2, aPgBanca out number, aTipo char);
 PROCEDURE getModPagUltime(aCdTerzo number, aCdModPag out varchar2, aPgBanca out number);

-- Recupero del flag no tax area per gestione finanziaria 2003

   FUNCTION getAnagFlNoTaxArea
      (
       aEsercizio NUMBER,
       aCdAnag ANAGRAFICO.cd_anag%TYPE
      ) RETURN VARCHAR2;

-- Recupero del flag no family area per gestione finanziaria 2005

   FUNCTION getAnagFlNoFamilyArea
      (
       aEsercizio NUMBER,
       aCdAnag ANAGRAFICO.cd_anag%TYPE
      ) RETURN VARCHAR2;

-- Recupero delle detrazioni personali impostate da utente a valere in sede di conguaglio

   FUNCTION getAnagImDetrazionePer
      (
       aEsercizio NUMBER,
       aCdAnag ANAGRAFICO.cd_anag%TYPE
      ) RETURN NUMBER;

-- Recupero delle deduzioni della Family impostate da utente a valere in sede di conguaglio

   FUNCTION getAnagImDeduzioniFam
      (
       aEsercizio NUMBER,
       aCdAnag ANAGRAFICO.cd_anag%TYPE
      ) RETURN NUMBER;

-- Recupero del flag no_detrazioni_altre per gestione finanziaria 2007

   FUNCTION getAnagFlNoDetrazioniAltre
      (
       aEsercizio NUMBER,
       aCdAnag ANAGRAFICO.cd_anag%TYPE
      ) RETURN VARCHAR2;

-- Recupero del flag no_detrazioni_family per gestione finanziaria 2007

   FUNCTION getAnagFlNoDetrazioniFamily
      (
       aEsercizio NUMBER,
       aCdAnag ANAGRAFICO.cd_anag%TYPE
      ) RETURN VARCHAR2;

-- Recupero del flag detrazioni_altri_tipi per gestione finanziaria 2007

   FUNCTION getAnagFlDetrazioniAltriTipi
      (
       aEsercizio NUMBER,
       aCdAnag ANAGRAFICO.cd_anag%TYPE
      ) RETURN VARCHAR2;

-- Recupero del flag no_credito_irpef per gestione spending 2014

   FUNCTION getAnagFlNoCreditoIrpef
      (
       aEsercizio NUMBER,
       aCdAnag ANAGRAFICO.cd_anag%TYPE
      ) RETURN VARCHAR2;

   FUNCTION getAnagFlNoCreditoCuneoIrpef
      (
       aEsercizio NUMBER,
       aCdAnag ANAGRAFICO.cd_anag%TYPE
      ) RETURN VARCHAR2;

   FUNCTION getAnagFlNoDetrCuneoIrpef
      (
       aEsercizio NUMBER,
       aCdAnag ANAGRAFICO.cd_anag%TYPE
      ) RETURN VARCHAR2;

-- Recupero del Reddito complessivo impostato da utente solo per il calcolo delle detrazioni

   FUNCTION getAnagRedditoComplessivo
      (
       aEsercizio NUMBER,
       aCdAnag ANAGRAFICO.cd_anag%TYPE
      ) RETURN NUMBER;

-- Recupero del Reddito abitazione principale impostato da utente solo per il calcolo delle detrazioni

   FUNCTION getAnagRedditoAbitazPrincipale
      (
       aEsercizio NUMBER,
       aCdAnag ANAGRAFICO.cd_anag%TYPE
      ) RETURN NUMBER;

-- Recupero del flag per l'applicazione o meno della detrazione personale massima

   FUNCTION getAnagFlApplicaDetrPersMax
      (
       aEsercizio NUMBER,
       aCdAnag ANAGRAFICO.cd_anag%TYPE
      ) RETURN VARCHAR2;

-- Ritorna il reddito di quanto eventualmente pagato come dipendente (per il calcolo del reddito complessivo)

FUNCTION getImponibilePagatoDip
      (
       aEsercizio NUMBER,
       aAnag TERZO.cd_anag%TYPE
       ) RETURN NUMBER;
END;
