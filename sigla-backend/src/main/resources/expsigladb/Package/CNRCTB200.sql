--------------------------------------------------------
--  DDL for Package CNRCTB200
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRCTB200" as
--
-- CNRCTB200 - Package interfaccia tabelle COGE/COAN
-- Date: 17/11/2004
-- Version: 3.25
--
-- Package per la gestione delle scritture COGE
--
-- Dependency: IBMERR 001
--
-- History:
--
-- Date: 07/02/2002
-- Version: 1.0
-- Creazione
--
-- Date: 04/03/2002
-- Version: 1.1
-- Gestione causali in scrittura
--
-- Date: 03/04/2002
-- Version: 1.2
-- Estesa interfaccia aggiornamento saldi
--
-- Date: 03/05/2002
-- Version: 1.3
-- Revisione motore per introduzione numerazione/saldi/scritture per UO
--
-- Date: 07/05/2002
-- Version: 1.4
-- Stati contabilità COGE documenti
--
-- Date: 08/05/2002
-- Version: 1.5
-- Saldo COAN per sezioni sdoppiate
--
-- Date: 16/05/2002
-- Version: 1.6
-- Tolte costanti spostate in CNRCTB100
--
-- Date: 16/05/2002
-- Version: 1.7
-- Nuovo metodo di recupero sezione opposta
--
-- Date: 16/05/2002
-- Version: 1.8
-- Gestione implicita di duva, utuv, pg_ver_rec in creazione scritture
--
-- Date: 22/05/2002
-- Version: 1.9
-- Scrittura singola
--
-- Date: 28/05/2002
-- Version: 2.0
-- Introdotto cds e uo origine in testata scrittura economica e analitica
--
-- Date: 03/06/2002
-- Version: 2.1
-- Introduzione causali
--
-- Date: 19/06/2002
-- Version: 2.2
-- Fix su passaggio dati da scrittura a movimenti (CDS/UO/ESERCIZIO)
--
-- Date: 08/07/2002
-- Version: 2.3
-- Attivazione della chiamata di disattivazione scrittura, nella procedura
-- che provvede a stornare uan scrittura COAN
--
-- Date: 11/07/2002
-- Version: 2.4
-- Lo storno di scritture COGE annulla sui saldi l'effetto delle scritture origine
--
-- Date: 17/07/2002
-- Version: 2.5
-- Introduzione di metodo di controllo che le scritture per documento siano effettivamente cambiate
-- La gestione dei saldi COGE è stata riportata alla versione precedente
--
-- Date: 17/07/2002
-- Version: 2.6
-- Fix errori su check scrittura modificata
--
-- Date: 18/07/2002
-- Version: 2.7
-- Se la lista dei movimenti è vuota la scrittura COGE non viene creata
--
-- Date: 18/07/2002
-- Version: 2.8
-- Aggiornamento della documentazione
--
-- Date: 18/11/2002
-- Version: 2.9
-- Aggiunto costante stato scrittura
--
-- Date: 26/11/2002
-- Version: 3.0
-- Gestione flag istituzionale/commerciale
--
-- Date: 09/12/2002
-- Version: 3.1
-- Fix nomi causale perdite/utile su cambi
--
-- Date: 08/01/2003
-- Version: 3.2
-- Aggiunti metodi di annulamento delle scritture COGE (con aggiornamento in negativo dei saldi e senza storno)
--
-- Date: 05/02/2003
-- Version: 3.3
-- Controllo di quadratura DARE/AVERE della scrittura economica
--
-- Date: 17/06/2003
-- Version: 3.4
-- Controllo di modifica della scrittura analitica
--
-- Date: 07/07/2003
-- Version: 3.5
-- Aggiunta causale risconti/ratei, annullamento esercizio chiuso
--
-- Date: 09/07/2003
-- Version: 3.6
-- Aggiunta causale ratei parte 2
--
-- Date: 12/07/2003
-- Version: 3.7
-- Aggiunte nuove costanti per chiusura del conto economico e stato patrimoniale
--
-- Date: 14/07/2003
-- Version: 3.8
-- Determinazione dell'utile o della perdita d'esercizio
--
-- Date: 15/07/2003
-- Version: 3.9
-- Origine della scrittura fuori da esercizio contabile
--
-- Date: 17/07/2003
-- Version: 3.10
-- Introdott causali per pagamento cassa banca e 1210
-- tolta l'origine scrittura extra esercizio contabile
--
-- Date: 18/07/2003
-- Version: 3.11
-- Aggiunte le causali: liquid iva acquisti/vendite/saldo
-- Aggiunta l'origine liquidazione iva centro + origine stipendi spostata da CNRCTB207
--
-- Date: 01/08/2003
-- Version: 3.12
-- Introdotto il metodo per l'annullamento delle scritture COAN
--
-- Date: 04/08/2003
-- Version: 3.13
-- Aggiunti metodi di eliminazione FISICA della scrittura
--
-- Date: 05/08/2003
-- Version: 3.14
-- Aggiunta gestione CHIUSURA_COEP
--
-- Date: 28/08/2003
-- Version: 3.15
-- Estratto metodo di controllo della chiusura definitiva coep
--
-- Date: 15/09/2003
-- Version: 3.16
-- Documentazione
--
-- Date: 22/09/2003
-- Version: 3.17
-- Estratto metodo di controllo della chiusura prova coep, (function isChiusuraCoepProva)
-- Constants:
--
-- Date: 13/11/2003
-- Version: 3.18
-- Per esercizi precedenti a quello di partenza, la chiusura COEP è sempre definitiva
-- Per CDS non validi nell'esercizio in processo, tale esercizio è dato come CHIUSO definitivamente
--
-- Date: 18/11/2003
-- Version: 3.19
-- Fix errore 686: effetto secondario che ha richiesto la riscrittura di isModificata per gestire
-- il controllo di modifica su scritture multiple sullo stesso terzo collegate allo stesso documento amministrativo
-- (compenso senza mandato principale)
--
-- Date: 24/11/2003
-- Version: 3.20
-- Test funzione isModificata per scritture economiche e analitiche
--
-- Date: 27/01/2004
-- Version: 3.21
-- Fix isModificata scritture coan
-- Portata in interfaccia desc scrittura COAN
--
-- Date: 20/05/2004
-- Version: 3.22
-- Nuova costante di CARICO bene durevole
--
-- Date: 23/09/2004
-- Version: 3.23
-- Introdotta costante tipo SEMAFORO CHIUSURA COGE
--
-- Date: 12/11/2004
-- Version: 3.24
-- Inserimento causale per movimentazione economica per migrazione inventario
--
-- Date: 17/11/2004
-- Version: 3.25
-- Inserimento causale per movimentazione economica per carico o dismissione bene da trasferimento
--
-- Constants:
--
-- Stato della scrittura in partita doppia
STATO_DEFINITIVO CONSTANT CHAR(1) := 'D';

STATO_PROVA_CHIUSURA CONSTANT CHAR(1):='P';

STATO_CHIUSURA_DEF CONSTANT CHAR(1):='C';

STATO_PROVA_ANNULLATA CONSTANT CHAR(1):='A';

type movimentiList is table of movimento_coge%rowtype index by binary_integer;
type scrittureList is table of scrittura_partita_doppia%rowtype index by binary_integer;

type movAnalitList is table of movimento_coan%rowtype index by binary_integer;
type scrAnalitList is table of scrittura_analitica%rowtype index by binary_integer;

 -- Tipo movimento coge
 TI_ISTITUZIONALE CONSTANT CHAR(1) := 'I';
 TI_COMMERCIALE CONSTANT CHAR(1) := 'C';

 -- Tipo documento coge
 TIPO_COGE CONSTANT VARCHAR2(10) := 'COGE';

 -- Tipo documento coan
 TIPO_COAN CONSTANT VARCHAR2(10) := 'COAN';

 -- Sezione movimento
 IS_DARE CONSTANT CHAR(1) := 'D';
 IS_AVERE CONSTANT CHAR(1) := 'A';

 MAX_NUMERATORE CONSTANT NUMBER(10) := '9999999999';

 -- Tipi di scrittura

 TI_SCRITTURA_PRIMA CONSTANT CHAR(1) := 'P';
 TI_SCRITTURA_ULTIMA CONSTANT CHAR(1) := 'U';
 TI_SCRITTURA_SINGOLA CONSTANT CHAR(1) := 'S';

 -- Origine della scrittura
 ORIGINE_DOCUMENTO_CONT CONSTANT VARCHAR2(20) := 'DOCCONT';
 ORIGINE_MANUALE        CONSTANT VARCHAR2(20) := 'CAUSALE';
 ORIGINE_DOCUMENTO_AMM CONSTANT VARCHAR2(20) := 'DOCAMM';
 ORIGINE_CHIUSURA CONSTANT VARCHAR2(20) := 'CHIUSURA';
 ORIGINE_LIQUID_IVA CONSTANT VARCHAR2(20) := 'LIQUID_IVA';
 ORIGINE_STIPENDI CONSTANT VARCHAR2(100):='STIPENDI';
 ORIGINE_MIGRA_BENI CONSTANT VARCHAR2(20) := 'MIGRAZIONE_BENI';

 CAU_PERDITA_SU_CAMBI CONSTANT VARCHAR2(50) := 'PERDITE_SU_CAMBI';
 CAU_UTILE_SU_CAMBI CONSTANT VARCHAR2(50) := 'UTILE_SU_CAMBI';
 CAU_DISMISSIONE_BENE_DURVOLE CONSTANT VARCHAR2(50) := 'DISMISSIONE_BENE_DUREVOLE';
 CAU_CARICO_BENE_DURVOLE CONSTANT VARCHAR2(50) := 'CARICO_BENE_DUREVOLE';
 CAU_RISCONTI CONSTANT VARCHAR2(50) := 'RISCONTI';
 CAU_RISCONTI_P2 CONSTANT VARCHAR2(50) := 'RISCONTI_P2';

-- 19.05.2009 CHIUSURE RATEI (ORIGINATA DA PROBLEMI CREDITI)

-- RATEI TOTALMENTE IN ESERCIIO PRECEDENTE (VENGONO CHIUSI/RIBALTATI CON LA CHIUSURA DELL'ESERCIZIO)
 CAU_RATEI     Constant VARCHAR2(50) := 'RATEI';
 CAU_RATEI_P2  Constant VARCHAR2(50) := 'RATEI_P2';

-- RATEI PARZIALMENTE IN ESERCIIO PRECEDENTE (VENGONO CHIUSI/RIBALTATI ALL'ATTO DELLA SCRITTURA CNRCTB205 ROVESCIANDOLI)
 CAU_RATEI_QUOTA     Constant VARCHAR2(50) := 'RATEI_QUOTA';
 CAU_RATEI_QUOTA_P2  Constant VARCHAR2(50) := 'RATEI_QUOTA_P2';

 CAU_ANNULL_ES_CHIUSO CONSTANT VARCHAR2(50) := 'ANNULLAMENTO_DOC_ESERCIZIO_CHIUSO';
 CAU_ANNULL_RIGHE_ES_CHIUSO CONSTANT VARCHAR2(50) := 'ANNULLAMENTO_RIGHE_DOC_ESERCIZIO_CHIUSO';
 CAU_AMMORTAMENTO CONSTANT VARCHAR2(50) := 'AMMORTAMENTO';
 CAU_CHIUSURA_CONTO_ECONOMICO CONSTANT VARCHAR2(50) := 'CHIUSURA_CONTO_ECONOMICO';
 CAU_CHIUSURA_ST_PATRIMONIALE CONSTANT VARCHAR2(50) := 'CHIUSURA_STATO_PATRIMONIALE';
 CAU_DET_UTILE_PERDITA CONSTANT VARCHAR2(50) := 'DETERMINAZIONE_UTILE_PERDITA';
 CAU_RIAPERTURA_CONTI CONSTANT VARCHAR2(50) := 'RIAPERTURA_CONTI';
 CAU_PAGAMENTO_1210 CONSTANT VARCHAR2(50) := 'PAGAMENTO_1210';
 CAU_CASSA_BANCA CONSTANT VARCHAR2(50) := 'CASSA_BANCA';
 CAU_LIQIVAVENDITE CONSTANT VARCHAR2(50) := 'LIQUID_IVA_VENDITE';
 CAU_LIQIVAACQUISTI CONSTANT VARCHAR2(50) := 'LIQUID_IVA_ACQUISTI';
 CAU_LIQIVASALDO VARCHAR2(50) := 'LIQUID_IVA_SALDO';
 CAU_MIGRAZIONE_BENI CONSTANT VARCHAR2(50) := 'MIGRAZIONE_BENI';
 CAU_SCA_BENE_DUREVOLE_TRASF CONSTANT VARCHAR2(50) := 'DISMISSIONE_BENE_DUREVOLE_TRASF';
 CAU_CAR_BENE_DUREVOLE_TRASF CONSTANT VARCHAR2(50) := 'CARICO_BENE_DUREVOLE_TRASF';


 -- Utilizzato per la specifica del semaforo di gestione della chiusura coge

 SEMAFORO_CHIUSURA CONSTANT VARCHAR2(20):='CHIUSURA_COGE00';

 -- Functions e Procedures:

 -- Ritorna una descrizione consistente per la Scrittura aScrittura (economica)
 -- Parametri:
 -- aScrittura -> Scrittura da descrivere

 function getDescScrittura(aScrittura scrittura_partita_doppia%rowtype) return varchar2;

 -- Ritorna una descrizione consistente per la Scrittura aScrittura (analitica)
 -- Parametri:
 -- aScrittura -> Scrittura da descrivere

 function getDescScrittura(aScrittura scrittura_analitica%rowtype) return varchar2;

 -- Attiva una scrittura coep su db
 -- Parametri:
 -- aScrittura -> Scrittura da attivare
 -- aUser -> utente che effettua la modifica
 -- aTSNow -> timestamp modifica

 procedure attivaScrittura(aScrittura IN OUT scrittura_partita_doppia%rowtype, aUser varchar2, aTSNow date default sysdate);

 -- Disattiva una scrittura coep su db
 -- Parametri:
 -- aScrittura -> Scrittura da attivare
 -- aUser -> utente che effettua la modifica
 -- aTSNow -> timestamp modifica

 procedure disattivaScrittura(aScrittura IN OUT scrittura_partita_doppia%rowtype, aUser varchar2, aTSNow date default sysdate);

 -- Crea e inserisce una scrittura in partita doppia (testata) per esercizio di destinazione
 -- Ritorna in aScrittura il rowtype della scrittura inserita
 -- Il metodo effettua anche la numerazione della scrittura inserita
 -- Viene controllato che la scrittura sia quadrata in dare/avere
 -- Sulla scrittura come duva e utuv vengono forzati dacr e utcr.
 -- Sui movimenti vengono trasferiti dacr,utcr,duva,utuv della scrittura.
 -- Se sul movimento il campo TI_ISTITUZ_COMMERC non è valorizzato, viene
 -- preimpostato a 'I' Istituzionale.

 -- Parametri:
 -- aScrittura -> Scrittura da creare
 -- aListaMovimenti -> Lista dei movimenti

 procedure creaScrittCoge(
   aScrittura IN OUT scrittura_partita_doppia%rowtype,
   aListaMovimenti IN OUT movimentiList
 );

 -- Come prec. + Imposta l'esercizio aEs specificato sulla scrittura e relativi movimenti

 -- Parametri:
 -- aEs -> Esercizio da impostare sulla scrittura
 -- aScrittura -> Scrittura da creare
 -- aListaMovimenti -> Lista dei movimenti

 procedure creaScrittCoge(
   aEs number,
   aScrittura IN OUT scrittura_partita_doppia%rowtype,
   aListaMovimenti IN OUT movimentiList
 );

 -- Crea e inserisce una scrittura analitica (testata)
 -- Ritorna in aScrittura il rowtype della scrittura inserita
 -- Il metodo effettua anche la numerazione della scrittura inserita

 -- Parametri:
 -- aScrittura -> Scrittura da creare
 -- aListaMovimenti -> Lista dei movimenti

 procedure creaScrittCoan(
   aScrittura IN OUT scrittura_analitica%rowtype,
   aListaMovimenti IN OUT movAnalitList
 );

 -- Recupera locka una scrittura in partita doppia
 -- Carica la scrittura specificata in aScrittura come chiave primaria

 -- Parametri:
 -- aScrittura -> Scrittura da leggere (valorizzare la chiave primaria della scrittura)
 -- aListaMovimenti -> Lista movimenti estratti dalla scrittura

 procedure getScritturaEPLock(aScrittura IN OUT scrittura_partita_doppia%rowtype, aListaMovimenti OUT movimentiList);

 -- Recupera locka una scrittura analitica
 -- Carica la scrittura specificata in aScrittura come chiave primaria

 -- Parametri:
 -- aScrittura -> Scrittura da leggere (valorizzare la chiave primaria della scrittura)
 -- aListaMovimenti -> Lista movimenti estratti dalla scrittura

 procedure getScritturaANLock(aScrittura IN OUT scrittura_analitica%rowtype, aListaMovimenti OUT movAnalitList);

 -- Verifica se la collezione di scritture old e le new sono uguali

 -- Se il numero di scritture originali e modificate è diverso
 --  Ritorna true
 -- Cicla sulle scritture di origine
 --  Cerca la scrittura modificata con lo stesso terzo di quella di origine
 --  Se non è trovata
 --   Ritorna false
 --  Altrimenti verifica la diversità dei seguenti campi in testata scrittura:
 --    CD_CDS
 --    ESERCIZIO
 --    CD_UNITA_ORGANIZZATIVA
 --    ORIGINE_SCRITTURA
 --    CD_TERZO
 --    CD_CAUSALE_COGE
 --    CD_TIPO_DOCUMENTO
 --    PG_NUMERO_DOCUMENTO
 --    CD_COMP_DOCUMENTO
 --    IM_SCRITTURA
 --    TI_SCRITTURA
 --    DT_PAGAMENTO
 --    CD_DIVISA
 --    COSTO_PLURIENNALE
 --    PG_ENTE
 --    DS_SCRITTURA
 --    ESERCIZIO_DOCUMENTO_AMM
 --    CD_CDS_DOCUMENTO
 --    CD_UO_DOCUMENTO
 -- Nel caso ritorna true
 -- Se le scritture corrispondono a livello di testata, e il numero di movimenti di ognuna non coincide
 --  Ritorna true
 -- Altrimenti
 --  cicla sui movimenti della scrittura originale
 --   Cerca un movimento corrispondente con dati corrispondenti identici:
 -- 	    CD_CDS
 --         ESERCIZIO
 --         CD_UNITA_ORGANIZZATIVA
 --         CD_TERZO
 --         CD_VOCE_EP
 --         IM_MOVIMENTO
 --         SEZIONE
 --         DT_DA_COMPETENZA_COGE
 --         DT_A_COMPETENZA_COGE
 --         STATO
 --  se non lo trova ritorna true
 -- Altrimenti ritorna false

 -- Parametri:
 -- aListaScrittureOld -> Scritture originali
 -- aInListaScrittureNew -> Scritture modificate

 function isModificata(aListaScrittureOld scrittureList, aInListaScrittureNew scrittureList) return boolean;

 -- Verifica se la scrittura analitica old e la new

 -- Parametri:
 -- aListaScrittureOld -> Scritture originali
 -- aListaScrittureNew -> Scritture modificate

 function isModificata(aScritturaOld IN OUT scrittura_analitica%rowtype, aScritturaNew IN OUT scrittura_analitica%rowtype) return boolean;

-- Crea una scrittura di storno in partita doppia. Ritorna in aScrittura il rowtype della scrittura inserita. Il metodo effettua anche la numerazione della scrittura inserita.
--
-- pre-post-name: Utente non specificato
-- pre: non viene specificato l.utente che effettua la modifica
-- post: viene sollevata un.eccezione
--
-- pre-post-name: Creazione della scrittura di storno
-- pre: nessuna precedent eprecondizione è soddisfatta
-- post: viene disattivata la scrittura attuale nella tabella SCRITTURA_PARTITA_DOPPIA settando a .N. il flag di scrittura attiva, viene creata una nuovas scrittura in partita doppia (inserisce la testata, i movimenti della scrittura e aggiorna i saldi) in cui ciascun movimento ha sezione opposta a quella originale. La nuova scrittura ha un riferimento alla scrittura di cui è lo storno.
--
-- Parametri:
-- aCdCDS -> codice del centro di spesa
-- aEs -> esercizio
-- aPgScrittura -> progressivo della scrittura
-- aUser -> utente che effettua la modifica
-- aTSNow -> data della modifica
--

procedure creaScrittStornoCoge(
   aCdCds varchar2,
   aEs number,
   aCdUnitaOrganizzativa varchar2,
   aPgScrittura number,
   aUser varchar2,
   aTSNow date default sysdate
 );

 procedure creaScrittStornoCoge(
   aScrittura IN OUT scrittura_partita_doppia%rowtype,
   aListaMovimenti IN OUT movimentiList,
   aUser varchar2,
   aTSNow date default sysdate
 );

-- Annulla la scrittura coge specificata eliminando anche l'effetto della stessa sui saldi
-- La scrittura viene disattivata
-- Non viene creata una scrittura di storno

  procedure annullaScrittCoge(
   aScrittura IN OUT scrittura_partita_doppia%rowtype,
   aListaMovimenti IN OUT movimentiList,
   aUser varchar2,
   aTSNow date default sysdate
 );

-- Elimina fisicamente la scrittura coge specificata eliminando anche l'effetto della stessa sui saldi
-- La scrittura viene eliminata fisicamente
-- Non viene creata una scrittura di storno

  procedure eliminaScrittCoge(
   aScrittura IN OUT scrittura_partita_doppia%rowtype,
   aListaMovimenti IN OUT movimentiList,
   aUser varchar2,
   aTSNow date default sysdate
 );

-- Annulla la scrittura coan specificata eliminando anche l'effetto della stessa sui saldi
-- La scrittura viene disattivata
-- Non viene creata una scrittura di storno

  procedure annullaScrittCoan (
   aScrittura IN OUT scrittura_analitica%rowtype,
   aListaMovimenti IN OUT movAnalitList,
   aUser varchar2,
   aTSNow date default sysdate
 );

-- Elimina fisicamente la scrittura coan specificata eliminando anche l'effetto della stessa sui saldi
-- La scrittura viene eliminata fisicamente
-- Non viene creata una scrittura di storno

  procedure eliminaScrittCoan(
   aScrittura IN OUT scrittura_analitica%rowtype,
   aListaMovimenti IN OUT movAnalitList,
   aUser varchar2,
   aTSNow date default sysdate
 );

-- Crea una scrittura di storno COAN
--
-- 	L'operation effettua la creazione della scrittura analitica di storno, quando viene contabilizzato un documento, a cui risulta già associata una scrittura 		analitica attiva.
-- 	L'operazione di storno consiste nel :
-- 		Disattivare la scrittura originaria.
-- 		Creare una nuova scrittura disattiva, con importo pari alla somma degli importi dei movimenti coan cambiati di segno. Associare a questa 		scrittura tutti i movimenti coan della scrittura originaria, con i relativi importi cambiati di segno. Questa scrittura viene messa in relazione con la 		scrittura originaria.
--
-- 	Pre-post name: Non esiste la scrittura originaria.
-- 	Pre: Se non esiste la scrittura originaria.
-- 	Post: Viene sollevata un eccezione
--
-- 	Pre-post name: Esiste la scrittura originaria.
-- 	Pre: Esiste la scrittura originaria.
-- 	Post: Viene disattivata la scrittura originaria.  E vengono estratti tutti i suoi movimenti.
--
-- 	Pre-post name: La scrittura originaria non è legata a movimenti COAN.
-- 	Pre: Non esistono movimenti coan legati alla scrittura originaria.
-- 	Post: Viene sollevata un eccezione.
--
-- 	Pre-post name: La scrittura originaria è legata a movimenti COAN.
-- 	Pre: Esistono movimenti coan legati alla scrittura originaria.
-- 		Post:Viene creata una nuova scrittura disattiva associata alla scrittura originaria, a cui vengono legati tutti i movimenti coan della scrittura originaria, con i 	relativi importi cambiati di segno. Viene aggiornato il saldo coan.
--
-- Parametri:
-- aCdCds varchar2 -> Cds scrittura
-- aEs number -> Esercizio scrittura
-- aCdUnitaOrganizzativa -> Codice UO scrittura
-- aPgScrittura -> Pg scrittura
-- aUser -> Utente che effettua l'operazione
-- aTSNow -> Timestamp dell'operazione (opzionale default sysdate)
--

 procedure creaScrittStornoCoan(
   aCdCds varchar2,
   aEs number,
   aCdUnitaOrganizzativa varchar2,
   aPgScrittura number,
   aUser varchar2,
   aTSNow date default sysdate
 );

 procedure creaScrittStornoCoan(
   aScrittura IN OUT scrittura_analitica%rowtype,
   aListaMovimenti IN OUT movAnalitList,
   aUser varchar2,
   aTSNow date default sysdate
 );

-- Ritorna il prossimo numeratore di scritture nel cds specificato per l'esercizio specificato e la scrittura
-- specificata. Crea o aggiorna il numeratore utilizzandolo come semaforo
--
-- aEsercizio -> esercizio di riferimento
-- aCdCds -> codice del CDS
-- aCdUnitaOrganizzativa -> codice dell'UO
-- aTipoScrittura -> Tipo di scrittura (COGE o COAN)
 function getNextProgressivo(aEsercizio number,aCdCds varchar2, aCdUnitaOrganizzativa varchar2, aTipoScrittura varchar2, aUser varchar2, aTSNow date default sysdate) return number;

-- Recupera la sezione opposta a quella del movimento specificato
 function getSezioneOpposta(aMovimento movimento_coge%rowtype) return char;
-- Recupera la sezione opposta a quella specificata
 function getSezioneOpposta(aSezione char) return char;

-- Procedura di aggiornamento dei saldi in contabilità economica, eventualmente creando le righe di saldo nulle in fase iniziale per la tabella SALDO_COGE.
--
-- pre-post-name: Aggiornamento del saldo
-- pre: Esiste una riga di saldo nella tabella SALDO_COGE, corrispondente al movimento della scrittura in partita doppia per centro di spesa, esercizio, unità organizzativa, conto economico e terzo
-- post: viene aggiornata la riga incrementando i totali DARE/AVERE in dipendenza della sezione del movimento.
--
-- pre-post-name: Creazione della riga di saldo corrispondente alla scrittura in partita doppia
-- pre: Non esiste una riga di saldo nella tabella SALDO_COGE, corrispondente al movimento della scrittura in partita doppia per centro di spesa, esercizio, unità organizzativa, conto economico e terzo
-- post: inserimento della riga nella tabella SALDO_COGE relativa al movimento della scrittura in partita doppia, valorizzazioni ottenute dal movimento della scrittura.
--
-- Parametri:
-- aCdCds varchar2 -> Cds scrittura
-- aEs number -> Esercizio scrittura
-- aCdUnitaOrganizzativa -> Codice UO scrittura
-- aPgScrittura -> Pg scrittura
-- aUser -> Utente che effettua l'operazione
-- aTSNow -> Timestamp dell'operazione (opzionale default sysdate)
--

 procedure aggiornaSaldoCoge(
   aCdCds varchar2,
   aEs number,
   aCdUnitaOrganizzativa varchar2,
   aPgScrittura number,
   aUser varchar2,
   aTSNow date default sysdate
 );

-- Parametri:
-- aScrittura -> scrittura partita doppia
-- aListaMovimenti -> lista dei movimenti

 procedure aggiornaSaldoCoge(
  aScrittura IN OUT scrittura_partita_doppia%rowtype,
  aListaMovimenti IN OUT movimentiList);

-- Annullamento del saldo coge relativo alla scrittura specificata
-- Viene richiesto l'annulamento dell'effetto della scrittura sui saldi
-- Viene decrementato per ogni movimento il valore del saldo corrispondente nella sezione corretta
--
-- Parametri:
-- aScrittura -> scrittura partita doppia
-- aListaMovimenti -> lista dei movimenti

 procedure annullaSaldoCoge(aScrittura IN OUT scrittura_partita_doppia%rowtype, aListaMovimenti IN OUT movimentiList);

-- Aggiornamento del saldo coan
--
-- 	L'operation effettua l.aggiornamento del saldo della contabilità analitica, quando viene generata una scrittura analitica.
-- 	L'operazione di aggiornamento consiste nell.aggiornamento del totale avere con gli importi dei movimenti legati alla nuova scrittura.
--
-- 	Pre-post name: Non esiste un saldo legato alla scrittura.
-- 	Pre: Se non esiste un saldo legato alla scrittura.
-- 	Post: Viene creato il saldo con importi :
-- 		tot_avere = 0
-- 		tot_dare = 0
--
-- 	Pre-post name: Esiste un saldo legato alla scrittura
-- 	Pre: Esiste un saldo legato alla scrittura.
-- 	Post: Vengono aggiornati gli importi in dare e avere :
-- 		tot_avere = tot_avere + somma (importi dei movimenti in avere)
-- 		tot_dare = tot_dare + somma (importi dei movimenti in dare)
--
-- Parametri:
-- aCdCds varchar2 -> Cds scrittura
-- aEs number -> Esercizio scrittura
-- aCdUnitaOrganizzativa -> Codice UO scrittura
-- aPgScrittura -> Pg scrittura
-- aUser -> Utente che effettua l'operazione
-- aTSNow -> Timestamp dell'operazione (opzionale default sysdate)

 procedure aggiornaSaldoCoan(
   aCdCds varchar2,
   aEs number,
   aCdUnitaOrganizzativa varchar2,
   aPgScrittura number,
   aUser varchar2,
   aTSNow date default sysdate
 );

-- Parametri:
-- aScrittura -> scrittura partita doppia
-- aListaMovimenti -> lista dei movimenti

 procedure aggiornaSaldoCoan(
  aScrittura IN OUT scrittura_analitica%rowtype,
  aListaMovimenti IN OUT movAnalitList);

-- Annullamento del saldo coan relativo alla scrittura specificata
-- Viene richiesto l'annulamento dell'effetto della scrittura sui saldi
-- Viene decrementato per ogni movimento il valore del saldo corrispondente nella sezione corretta
--
-- Parametri:
-- aScrittura -> scrittura analitica
-- aListaMovimenti -> lista dei movimenti analitici

 procedure annullaSaldoCoan(aScrittura IN OUT scrittura_analitica%rowtype, aListaMovimenti IN OUT movAnalitList);
-- Funzioni di inserimento di scritture e movimenti COGE/COAN

 procedure ins_SCRITTURA_PARTITA_DOPPIA (aDest SCRITTURA_PARTITA_DOPPIA%rowtype);
 procedure ins_MOVIMENTO_COGE (aDest MOVIMENTO_COGE%rowtype);
 procedure ins_SALDO_COGE (aDest SALDO_COGE%rowtype);
 procedure ins_SCRITTURA_ANALITICA (aDest SCRITTURA_ANALITICA%rowtype);
 procedure ins_MOVIMENTO_COAN (aDest MOVIMENTO_COAN%rowtype);
 procedure ins_SALDO_COAN (aDest SALDO_COAN%rowtype);

-- Funzione di inserimento numerazione COGE/COAN
 procedure ins_NUMERAZIONE_COGE_COAN (aDest NUMERAZIONE_COGE_COAN%rowtype);

-- Creazione record chiusura esercizio COEP
 procedure ins_CHIUSURA_COEP (aDest CHIUSURA_COEP%rowtype);

-- Ritorna il record di stato della chiusura COEP
--
-- 	Pre-post name: L'esercizio specificato è < Esercizio inizio dell'applicazione
-- 	Pre: L'esercizio specificato è minore dell'esercizio inizio dell'applicazione
-- 	Post: Viene ritornato un rowtype di CHIUSURA COEP così valorizzato:
--                aCC.cd_cds:=aCdCds;
--                aCC.esercizio:=aEs;
--                aCC.stato:=STATO_CHIUSURA_DEF;
--                aCC.duva:=sysdate;
--                aCC.dacr:=sysdate;
--                aCC.utuv:='$$$$$MIGRAZIONE$$$$$';
--                aCC.utcr:='$$$$$MIGRAZIONE$$$$$';
--                aCC.pg_ver_rec:=1;
--        Tale valorizzazione equivale a considerare CHIUSO DEFINITIVAMENTE l'esercizio aEs per il Cds aCdCds
--
-- 	Pre-post name: Il cds specificato NON E' valido nell'esercizio aEs
-- 	Pre: L'esercizio inizio del CDS aCdCds è maggiore di aEs
-- 	Post: Viene ritornato un rowtype di CHIUSURA COEP così valorizzato:
--                aCC.cd_cds:=aCdCds;
--                aCC.esercizio:=aEs;
--                aCC.stato:=STATO_CHIUSURA_DEF;
--                aCC.duva:=sysdate;
--                aCC.dacr:=sysdate;
--                aCC.utuv:='$$$$$MIGRAZIONE$$$$$';
--                aCC.utcr:='$$$$$MIGRAZIONE$$$$$';
--                aCC.pg_ver_rec:=1;
--        Tale valorizzazione equivale a considerare CHIUSO DEFINITIVAMENTE l'esercizio aEs per il Cds aCdCds
--
-- 	Pre-post name: Default
-- 	Pre: Nessun'altra precondizione verificata
-- 	Post: Viene ritornato il record letto dalla tabella CHIUSURA_COEP in corrispondenza del cds aCdCds ed esercizio aEs
--
-- Parametri:
--  aEs -> Esercizio contabile
--  aCdCds -> Codice del centro di spesa

 function getChiusuraCoep(aEs number,aCdCds varchar2) return CHIUSURA_COEP%rowtype;

-- Controllo chiusura definitiva dell'esercizio COEP
-- Ritorna 'Y' se l'esercizio economico è chiuso definitivamente per il CDS specificato
--
-- 	Pre-post name: Richiesta estrazione stato chiusura dell'esercizio economico per il cds specificato
-- 	Pre: Il rowtype ritornato da getChiusuraCoep ha stato definitivo
-- 	Post: ritorna 'Y', altrimenti ritorna 'N'
--
-- Parametri:
--  aEs -> Esercizio contabile
--  aCdCds -> Codice del centro di spesa

 function isChiusuraCoepDef(aEs number,aCdCds varchar2) return char;


-- Controllo chiusura per prova, dell'esercizio COEP
-- Ritorna 'Y' se l'esercizio economico è in prova di chiusura per il CDS specificato, (stato 'P')
--
-- 	Pre-post name: Richiesta estrazione stato chiusura dell'esercizio economico per il cds specificato
-- 	Pre: Il rowtype ritornato da getChiusuraCoep ha stato provvisorio
-- 	Post: ritorna 'Y', altrimenti ritorna 'N'
--
-- Parametri:
--  aEs -> Esercizio contabile
--  aCdCds -> Codice del centro di spesa
 function isChiusuraCoepProva(aEs number,aCdCds varchar2) return char;

end;
