CREATE OR REPLACE package CNRCTB200 as
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
-- Stati contabilit? COGE documenti
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
-- La gestione dei saldi COGE ? stata riportata alla versione precedente
--
-- Date: 17/07/2002
-- Version: 2.6
-- Fix errori su check scrittura modificata
--
-- Date: 18/07/2002
-- Version: 2.7
-- Se la lista dei movimenti ? vuota la scrittura COGE non viene creata
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
-- Per esercizi precedenti a quello di partenza, la chiusura COEP ? sempre definitiva
-- Per CDS non validi nell'esercizio in processo, tale esercizio ? dato come CHIUSO definitivamente
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
 -- Se sul movimento il campo TI_ISTITUZ_COMMERC non ? valorizzato, viene
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

 -- Se il numero di scritture originali e modificate ? diverso
 --  Ritorna true
 -- Cicla sulle scritture di origine
 --  Cerca la scrittura modificata con lo stesso terzo di quella di origine
 --  Se non ? trovata
 --   Ritorna false
 --  Altrimenti verifica la diversit? dei seguenti campi in testata scrittura:
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
-- pre: nessuna precedent eprecondizione ? soddisfatta
-- post: viene disattivata la scrittura attuale nella tabella SCRITTURA_PARTITA_DOPPIA settando a .N. il flag di scrittura attiva, viene creata una nuovas scrittura in partita doppia (inserisce la testata, i movimenti della scrittura e aggiorna i saldi) in cui ciascun movimento ha sezione opposta a quella originale. La nuova scrittura ha un riferimento alla scrittura di cui ? lo storno.
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
-- 	L'operation effettua la creazione della scrittura analitica di storno, quando viene contabilizzato un documento, a cui risulta gi? associata una scrittura 		analitica attiva.
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
-- 	Pre-post name: La scrittura originaria non ? legata a movimenti COAN.
-- 	Pre: Non esistono movimenti coan legati alla scrittura originaria.
-- 	Post: Viene sollevata un eccezione.
--
-- 	Pre-post name: La scrittura originaria ? legata a movimenti COAN.
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

-- Procedura di aggiornamento dei saldi in contabilit? economica, eventualmente creando le righe di saldo nulle in fase iniziale per la tabella SALDO_COGE.
--
-- pre-post-name: Aggiornamento del saldo
-- pre: Esiste una riga di saldo nella tabella SALDO_COGE, corrispondente al movimento della scrittura in partita doppia per centro di spesa, esercizio, unit? organizzativa, conto economico e terzo
-- post: viene aggiornata la riga incrementando i totali DARE/AVERE in dipendenza della sezione del movimento.
--
-- pre-post-name: Creazione della riga di saldo corrispondente alla scrittura in partita doppia
-- pre: Non esiste una riga di saldo nella tabella SALDO_COGE, corrispondente al movimento della scrittura in partita doppia per centro di spesa, esercizio, unit? organizzativa, conto economico e terzo
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
-- 	L'operation effettua l.aggiornamento del saldo della contabilit? analitica, quando viene generata una scrittura analitica.
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
-- 	Pre-post name: L'esercizio specificato ? < Esercizio inizio dell'applicazione
-- 	Pre: L'esercizio specificato ? minore dell'esercizio inizio dell'applicazione
-- 	Post: Viene ritornato un rowtype di CHIUSURA COEP cos? valorizzato:
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
-- 	Pre: L'esercizio inizio del CDS aCdCds ? maggiore di aEs
-- 	Post: Viene ritornato un rowtype di CHIUSURA COEP cos? valorizzato:
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
-- Ritorna 'Y' se l'esercizio economico ? chiuso definitivamente per il CDS specificato
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
-- Ritorna 'Y' se l'esercizio economico ? in prova di chiusura per il CDS specificato, (stato 'P')
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


CREATE OR REPLACE package body CNRCTB200 is
 -- Modifica del 13/11/2003: Per esercizi precedenti a quello di partenza, la chiusura COEP ? sempre definitiva
 function getChiusuraCoep(aEs number,aCdCds varchar2) return CHIUSURA_COEP%rowtype is
  aCC CHIUSURA_COEP%rowtype;
  aNumEs number(4);
 begin
  -- Se aEs ? minore dell'esercizio inizio dell'applicazione ? considerato chiuso definitivamente per il CDS aCdCds

  if aEs < CNRCTB008.ESERCIZIO_PARTENZA then
   aCC.cd_cds:=aCdCds;
   aCC.esercizio:=aEs;
   aCC.stato:=STATO_CHIUSURA_DEF;
   aCC.duva:=sysdate;
   aCC.dacr:=sysdate;
   aCC.utuv:='$$$$$MIGRAZIONE$$$$$';
   aCC.utcr:='$$$$$MIGRAZIONE$$$$$';
   aCC.pg_ver_rec:=1;
   return aCC;
  end if;

  -- Se l'esercizio inizio del CDS ? maggiore di aEs, aEs ? considerato chiuso definitivamente per il CDS aCdCds
  select esercizio_inizio into aNumEs from unita_organizzativa where cd_unita_organizzativa = aCdCds;
  if aNumEs > aEs then
   aCC.cd_cds:=aCdCds;
   aCC.esercizio:=aEs;
   aCC.stato:=STATO_CHIUSURA_DEF;
   aCC.duva:=sysdate;
   aCC.dacr:=sysdate;
   aCC.utuv:='$$$$$MIGRAZIONE$$$$$';
   aCC.utcr:='$$$$$MIGRAZIONE$$$$$';
   aCC.pg_ver_rec:=1;
   return aCC;
  end if;

  Begin
    select * into aCC from chiusura_coep where esercizio  = aEs and cd_cds = aCdCds;
  Exception
    When No_Data_Found Then
        aCC.cd_cds    := aCdCds;
        aCC.esercizio := aEs;
        aCC.stato     := STATO_PROVA_ANNULLATA; -- SE NON TROVA LA RIGA E' APERTO
        aCC.duva := sysdate;
        aCC.dacr := sysdate;
        aCC.utuv := 'NO_RIGA';
        aCC.utcr := 'NO_RIGA';
        aCC.pg_ver_rec := 1;
  End;
  return aCC;
 end;

 function isChiusuraCoepDef(aEs number,aCdCds varchar2) return char is
  aCC CHIUSURA_COEP%rowtype;
 begin
  if CNRCTB800.isSemStaticoCdsBloccato(aEs, aCdCds, SEMAFORO_CHIUSURA) then
   return 'Y';
  end if;
  aCC:=getChiusuraCoep(aEs, aCdCds);
  if aCC.stato = STATO_CHIUSURA_DEF then
   return 'Y';
  else
   return 'N';
  end if;
 exception when NO_DATA_FOUND then
  return 'N';
 end;

 function isChiusuraCoepProva(aEs number,aCdCds varchar2) return char is
  aCC CHIUSURA_COEP%rowtype;
 begin
  aCC:=getChiusuraCoep(aEs, aCdCds);
  if aCC.stato = STATO_PROVA_CHIUSURA then
   return 'Y';
  else
   return 'N';
  end if;
 exception when NO_DATA_FOUND then
  return 'N';
 end;

 function checkMovimentiCambiati(
  aListaMovOld movimentiList,
  aInListaMovNew movimentiList
 ) return boolean is
  isMovCambiato boolean;
  aTmpList movimentiList;
  aListaMovNew movimentiList;
 begin
  isMovCambiato:=true;

  if aListaMovOld.count != aInListaMovNew.count then
   return true;
  end if;

    -- copio in aListaMovNew il contenuto di aInListaMovNew
  aListaMovNew.delete;
  for k in 1..aInListaMovNew.count loop
   aListaMovNew(k):=aInListaMovNew(k);
  end loop;
  -- TEST DI UGUAGLIANZA DI MOVIMENTI
  isMovCambiato:=false;
  for k in 1..aListaMovOld.count loop
		isMovCambiato:=true; -- Serve per controllare che esista il nuovo movimento in corrispondenza del vecchio
		for u in 1..aListaMovNew.count loop
			if
                  aListaMovOld(k).CD_CDS=aListaMovNew(u).CD_CDS
              AND aListaMovOld(k).ESERCIZIO=aListaMovNew(u).ESERCIZIO
              AND aListaMovOld(k).CD_UNITA_ORGANIZZATIVA=aListaMovOld(u).CD_UNITA_ORGANIZZATIVA
              AND aListaMovOld(k).SEZIONE=aListaMovNew(u).SEZIONE
              AND aListaMovOld(k).TI_ISTITUZ_COMMERC=aListaMovNew(u).TI_ISTITUZ_COMMERC
              AND aListaMovOld(k).IM_MOVIMENTO=aListaMovNew(u).IM_MOVIMENTO
              AND nvl(aListaMovOld(k).FL_MOV_TERZO,'$')=nvl(aListaMovNew(u).FL_MOV_TERZO,'$')
              AND nvl(aListaMovOld(k).STATO,'$')=nvl(aListaMovNew(u).STATO,'$')
              AND nvl(aListaMovOld(k).CD_TERZO,-99999999)=nvl(aListaMovNew(u).CD_TERZO,-99999999)
              AND nvl(aListaMovOld(k).CD_VOCE_EP,'$_$_$_$_$_')=nvl(aListaMovNew(u).CD_VOCE_EP,'$_$_$_$_$_')
              AND nvl(aListaMovOld(k).DT_DA_COMPETENZA_COGE,to_date('01011960','DDMMYYYY'))=nvl(aListaMovNew(u).DT_DA_COMPETENZA_COGE,to_date('01011960','DDMMYYYY'))
              AND nvl(aListaMovOld(k).DT_A_COMPETENZA_COGE,to_date('01011960','DDMMYYYY'))=nvl(aListaMovNew(u).DT_A_COMPETENZA_COGE,to_date('01011960','DDMMYYYY'))
            then -- Trovato il movimento corrispondente
             aTmpList.delete;
             -- Ricopio in aTmpList aListaMovNew eccetto il movimento trovato uguale a corrispondente in vecchia
             for h in 1..aListaMovNew.count loop
              if h!=u then
               aTmpList(aTmpList.count+1):=aListaMovNew(h);
              end if;
             end loop;
             isMovCambiato:=false;
		     -- Resetto aListaMovNew con aTmpList
             aListaMovNew:=aTmpList;
             exit;
            end if;
        end loop; -- Fine loop sulla lista riaggiornata dei movimenti nuovi
        -- Se non viene trovato nessun movimento corrispondente a quello vecchio in processo devo uscire dal loop dei movimenti
        if isMovCambiato=true then
         exit;
        end if;
  end loop; -- Fine loop sui movimenti vecchi
  return isMovCambiato;
 end;

 function isModificata(aListaScrittureOld scrittureList, aInListaScrittureNew scrittureList) return boolean is
  aTmpList scrittureList;
  aListaMovOld movimentiList;
  aListaMovNew movimentiList;
  aScritturaNew scrittura_partita_doppia%rowtype;
  aScritturaOld scrittura_partita_doppia%rowtype;
  aListaScrittureNew scrittureList;
  isCambiata boolean;
 begin
  isCambiata:=false;
  -- Se il numero di scritture ? diverso: sicuramente devo riemettere
  if aListaScrittureOld.count != aInListaScrittureNew.count then
   return true;
  end if;

  -- copio in aListaScrittureNew il contenuto di aInListaScrittureNew
  aListaScrittureNew.delete;
  for i in 1..aInListaScrittureNew.count loop
   aListaScrittureNew(i):=aInListaScrittureNew(i);
  end loop;

  for i in 1..aListaScrittureOld.count loop
   isCambiata:=true; -- Serve per controllare che la scrittura nuova in corrispondenza del terzo della vecchia sia trovata
   for j in 1..aListaScrittureNew.count loop
    -- Se la testata della scrittura ? uguale devo verificare l'uguaglianza dei movimenti
    if(
           aListaScrittureOld(i).CD_CDS=aListaScrittureNew(j).CD_CDS
       AND aListaScrittureOld(i).ESERCIZIO=aListaScrittureNew(j).ESERCIZIO
       AND aListaScrittureOld(i).CD_UNITA_ORGANIZZATIVA=aListaScrittureNew(j).CD_UNITA_ORGANIZZATIVA
       AND aListaScrittureOld(i).IM_SCRITTURA=aListaScrittureNew(j).IM_SCRITTURA
       AND aListaScrittureOld(i).TI_SCRITTURA=aListaScrittureNew(j).TI_SCRITTURA
       AND aListaScrittureOld(i).STATO=aListaScrittureNew(j).STATO
       AND nvl(aListaScrittureOld(i).ORIGINE_SCRITTURA,'$_$_$_$_$_')=nvl(aListaScrittureNew(j).ORIGINE_SCRITTURA,'$_$_$_$_$_')
       AND nvl(aListaScrittureOld(i).CD_CAUSALE_COGE,'$_$_$_$_$_')=nvl(aListaScrittureNew(j).CD_CAUSALE_COGE,'$_$_$_$_$_')
       AND nvl(aListaScrittureOld(i).CD_TIPO_DOCUMENTO,'$_$_$_$_$_')=nvl(aListaScrittureNew(j).CD_TIPO_DOCUMENTO,'$_$_$_$_$_')
       AND nvl(aListaScrittureOld(i).CD_COMP_DOCUMENTO,'$_$_$_$_$_')=nvl(aListaScrittureNew(j).CD_COMP_DOCUMENTO,'$_$_$_$_$_')
       AND nvl(aListaScrittureOld(i).CD_DIVISA,'$_$_$_$_$_')=nvl(aListaScrittureNew(j).CD_DIVISA,'$_$_$_$_$_')
       AND nvl(aListaScrittureOld(i).COSTO_PLURIENNALE,'$_$_$_$_$_')=nvl(aListaScrittureNew(j).COSTO_PLURIENNALE,'$_$_$_$_$_')
       AND nvl(aListaScrittureOld(i).DS_SCRITTURA,'$_$_$_$_$_')=nvl(aListaScrittureNew(j).DS_SCRITTURA,'$_$_$_$_$_')
       AND nvl(aListaScrittureOld(i).CD_CDS_DOCUMENTO,'$_$_$_$_$_')=nvl(aListaScrittureNew(j).CD_CDS_DOCUMENTO,'$_$_$_$_$_')
       AND nvl(aListaScrittureOld(i).CD_UO_DOCUMENTO,'$_$_$_$_$_')=nvl(aListaScrittureNew(j).CD_UO_DOCUMENTO,'$_$_$_$_$_')
       AND nvl(aListaScrittureOld(i).ESERCIZIO_DOCUMENTO_AMM,-9999)=nvl(aListaScrittureNew(j).ESERCIZIO_DOCUMENTO_AMM,-9999)
       AND nvl(aListaScrittureOld(i).CD_TERZO,-99999999)=nvl(aListaScrittureNew(j).CD_TERZO,-99999999)
       AND nvl(aListaScrittureOld(i).PG_NUMERO_DOCUMENTO,-9999999999)=nvl(aListaScrittureNew(j).PG_NUMERO_DOCUMENTO,-9999999999)
       AND nvl(aListaScrittureOld(i).PG_ENTE,-9999999999)=nvl(aListaScrittureNew(j).PG_ENTE,-9999999999)
       AND nvl(aListaScrittureOld(i).DT_PAGAMENTO,to_date('01011960','DDMMYYYY'))=nvl(aListaScrittureNew(j).DT_PAGAMENTO,to_date('01011960','DDMMYYYY'))
    ) then
	 aListaMovOld.delete;
	 aListaMovNew.delete;
	 aScritturaNew:=aListaScrittureNew(j);
	 aScritturaOld:=aListaScrittureOld(i);
	 getScritturaEPLock(aScritturaOld,aListaMovOld);
	 getScritturaEPLock(aScritturaNew,aListaMovNew);
     -- Se trovo corrispondenza in tutti i movimenti
	 if not checkMovimentiCambiati(aListaMovOld,aListaMovNew) then
             aTmpList.delete;
             -- Ricopio in aTmpList aListaScrittureNew eccetto la scrittura trovata uguale a corrispondente in vecchia lista
             for h in 1..aListaScrittureNew.count loop
              if h!=j then
               aTmpList(aTmpList.count+1):=aListaScrittureNew(h);
              end if;
             end loop;
             aListaScrittureNew:=aTmpList;
             isCambiata:=false; -- Serve per controllare che la scrittura nuova in corrispondenza del terzo della vecchia sia trovata
             exit;
	 else
	         null;
     end if;
	end if;
   end loop;
   if isCambiata then
    return true;
   end if;
  end loop;
  if aListaScrittureNew.count=0 then
   return false;
  else
   return true;
  end if;
 end;

 function isModificata(aScritturaOld IN OUT scrittura_analitica%rowtype, aScritturaNew IN OUT scrittura_analitica%rowtype) return boolean is
  isCambiata boolean;
  isMovCambiato boolean;
  aListaMovOld movAnalitList;
  aListaMovNew movAnalitList;
  aTmpList movAnalitList;
 begin
  isCambiata:=false;
  isMovCambiato:=false;
  aListaMovOld.delete;
  aListaMovNew.delete;
  getScritturaANLock(aScritturaOld,aListaMovOld);
  getScritturaANLock(aScritturaNew,aListaMovNew);
  if aListaMovOld.count != aListaMovNew.count then
   return true;
  end if;
  isMovCambiato:=false;
  if(
          aScritturaOld.CD_CDS<>aScritturaNew.CD_CDS
       OR aScritturaOld.ESERCIZIO<>aScritturaNew.ESERCIZIO
       OR aScritturaOld.CD_UNITA_ORGANIZZATIVA<>aScritturaNew.CD_UNITA_ORGANIZZATIVA
       OR aScritturaOld.IM_SCRITTURA<>aScritturaNew.IM_SCRITTURA
       OR aScritturaOld.TI_SCRITTURA<>aScritturaNew.TI_SCRITTURA
       OR aScritturaOld.STATO<>aScritturaNew.STATO
       OR nvl(aScritturaOld.ORIGINE_SCRITTURA,'$_$_$_$_$_')<>nvl(aScritturaNew.ORIGINE_SCRITTURA,'$_$_$_$_$_')
       OR nvl(aScritturaOld.CD_TIPO_DOCUMENTO,'$_$_$_$_$_')<>nvl(aScritturaNew.CD_TIPO_DOCUMENTO,'$_$_$_$_$_')
       OR nvl(aScritturaOld.CD_CDS_DOCUMENTO,'$_$_$_$_$_')<>nvl(aScritturaNew.CD_CDS_DOCUMENTO,'$_$_$_$_$_')
       OR nvl(aScritturaOld.CD_UO_DOCUMENTO,'$_$_$_$_$_')<>nvl(aScritturaNew.CD_UO_DOCUMENTO,'$_$_$_$_$_')
       OR nvl(aScritturaOld.PG_NUMERO_DOCUMENTO,-9999999999)<>nvl(aScritturaNew.PG_NUMERO_DOCUMENTO,-9999999999)
       OR nvl(aScritturaOld.CD_TERZO,-99999999)<>nvl(aScritturaNew.CD_TERZO,-99999999)
       OR nvl(aScritturaOld.CD_COMP_DOCUMENTO,'$_$_$_$_$_')<>nvl(aScritturaNew.CD_COMP_DOCUMENTO,'$_$_$_$_$_')
       OR nvl(aScritturaOld.DS_SCRITTURA,'$_$_$_$_$_')<>nvl(aScritturaNew.DS_SCRITTURA,'$_$_$_$_$_')
--       OR nvl(aScritturaOld.DT_CONTABILIZZAZIONE,to_date('01011960','DDMMYYYY'))<>nvl(aScritturaNew.DT_CONTABILIZZAZIONE,to_date('01011960','DDMMYYYY'))
       OR nvl(aScritturaOld.DT_CANCELLAZIONE,to_date('01011960','DDMMYYYY'))<>nvl(aScritturaNew.DT_CANCELLAZIONE,to_date('01011960','DDMMYYYY'))
       OR nvl(aScritturaOld.ESERCIZIO_DOCUMENTO_AMM,-9999)<>nvl(aScritturaNew.ESERCIZIO_DOCUMENTO_AMM,-9999)
  ) then
   return true;
  end if;
  for k in 1..aListaMovOld.count loop
   isMovCambiato:=true; -- Serve per controllare che esista il nuovo movimento in corrispondenza del vecchio
   for u in 1..aListaMovNew.count loop
    if
	        aListaMovOld(k).CD_CDS=aListaMovNew(u).CD_CDS
        AND aListaMovOld(k).ESERCIZIO=aListaMovNew(u).ESERCIZIO
        AND aListaMovOld(k).CD_UNITA_ORGANIZZATIVA=aListaMovOld(u).CD_UNITA_ORGANIZZATIVA
        AND aListaMovOld(k).CD_VOCE_EP=aListaMovNew(u).CD_VOCE_EP
        AND aListaMovOld(k).IM_MOVIMENTO=aListaMovNew(u).IM_MOVIMENTO
        AND aListaMovOld(k).SEZIONE=aListaMovNew(u).SEZIONE
        AND aListaMovOld(k).STATO=aListaMovNew(u).STATO
        AND aListaMovOld(k).CD_CENTRO_RESPONSABILITA=aListaMovNew(u).CD_CENTRO_RESPONSABILITA
        AND aListaMovOld(k).CD_NATURA=aListaMovNew(u).CD_NATURA
        AND aListaMovOld(k).DS_MOVIMENTO=aListaMovNew(u).DS_MOVIMENTO
        AND nvl(aListaMovOld(k).CD_TERZO,-99999999)=nvl(aListaMovNew(u).CD_TERZO,-99999999)
        AND nvl(aListaMovOld(k).CD_FUNZIONE,'$_')=nvl(aListaMovNew(u).CD_FUNZIONE,'$_')
        AND nvl(aListaMovOld(k).CD_LINEA_ATTIVITA,'$_$_$_$_$_')=nvl(aListaMovNew(u).CD_LINEA_ATTIVITA,'$_$_$_$_$_')
        AND nvl(aListaMovOld(k).PG_NUMERO_DOCUMENTO,-9999999999)=nvl(aListaMovNew(u).PG_NUMERO_DOCUMENTO,-9999999999)
	then -- Trovato il movimento corrispondente
        aTmpList.delete;
		-- Rimuovo dalla lista nuova il movimento trovato uguale a corrispondente in vecchia
		for h in 1..aListaMovNew.count loop
		 if h!=u then
		  aTmpList(aTmpList.count+1):=aListaMovNew(h);
		 end if;
		end loop;
		isMovCambiato:=false;
		exit;
	 end if;
    end loop;
	if isMovCambiato then
     return true;
	end if;
    aListaMovNew.delete;
    aListaMovNew:=aTmpList;
   end loop;
  return isMovCambiato;
 end;

 function getSezioneOpposta(aMovimento movimento_coge%rowtype) return char is
 begin
  if aMovimento.sezione = CNRCTB200.IS_DARE then
   return CNRCTB200.IS_AVERE;
  else
   return CNRCTB200.IS_DARE;
  end if;
 end;

 function getSezioneOpposta(aSezione char) return char is
 begin
  if aSezione = CNRCTB200.IS_DARE then
   return CNRCTB200.IS_AVERE;
  else
   return CNRCTB200.IS_DARE;
  end if;
 end;

 function getDescScrittura(aScrittura scrittura_partita_doppia%rowtype) return varchar2 is
 begin
  return 'n.'||aScrittura.pg_scrittura||' del Cds '||aScrittura.cd_cds||' UO '||aScrittura.cd_unita_organizzativa||' in esercizio '||aScrittura.esercizio;
 end;

 function getDescScrittura(aScrittura scrittura_analitica%rowtype) return varchar2 is
 begin
  return 'n.'||aScrittura.pg_scrittura||' del Cds '||aScrittura.cd_cds||' UO '||aScrittura.cd_unita_organizzativa||' in esercizio '||aScrittura.esercizio;
 end;

 function getNextProgressivo(aEsercizio number,aCdCds varchar2, aCdUnitaOrganizzativa varchar2, aTipoScrittura varchar2, aUser varchar2, aTSNow date default sysdate) return number is
  aNumRec numerazione_COGE_coan%rowtype;
 begin
  begin
   select * into aNumRec from numerazione_coge_coan where
        cd_cds = aCdCds
    and esercizio = aEsercizio
    and cd_unita_organizzativa = aCdUnitaOrganizzativa
    and ti_documento = aTipoScrittura for update nowait;
  exception when NO_DATA_FOUND then
   aNumRec.esercizio:=aEsercizio;
   aNumRec.cd_cds:=aCdCds;
   aNumRec.cd_unita_organizzativa:=aCdUnitaOrganizzativa;
   aNumRec.ti_documento:=aTipoScrittura;
   aNumRec.corrente:=0;
   aNumRec.primo:=0;
   aNumRec.ultimo:=MAX_NUMERATORE;
   aNumRec.utcr:=aUser;
   aNumRec.utuv:=aUser;
   aNumRec.dacr:=aTSNow;
   aNumRec.duva:=aTSNow;
   aNumRec.pg_ver_rec:=1;
   begin
    ins_NUMERAZIONE_COGE_COAN(aNumRec);
   exception when DUP_VAL_ON_INDEX then
    IBMERR001.RAISE_ERR_GENERICO('Numerazione scritture COGE temporaneamente non disponibile');
   end;
  end;
  update numerazione_coge_coan
   set
    corrente=corrente+1,
	utuv=aUser,
 	duva=aTSNow,
	pg_ver_rec=pg_ver_rec+1
  where
        cd_cds = aCdCds
    and esercizio = aEsercizio
    and cd_unita_organizzativa = aCdUnitaOrganizzativa
    and ti_documento = aTipoScrittura;
  return aNumRec.corrente + 1;
 end;

 procedure disattivaScrittura(aScrittura IN OUT scrittura_partita_doppia%rowtype, aUser varchar2, aTSNow date default sysdate) is
 begin
   update scrittura_partita_doppia
    set
	 attiva='N',
	 utuv=aUser,
	 duva=aTSNow,
	 pg_ver_rec=pg_ver_rec+1
   where
        cd_cds = aScrittura.cd_cds
    and esercizio = aScrittura.esercizio
    and cd_unita_organizzativa = aScrittura.cd_unita_organizzativa
	and pg_scrittura = aScrittura.pg_scrittura;
   aScrittura.attiva:='N';
   aScrittura.duva:=aTSNow;
   aScrittura.utuv:=aUser;
   aScrittura.pg_ver_rec:=aScrittura.pg_ver_rec+1;
 end;

 procedure annullaScrittura(aScrittura IN OUT scrittura_partita_doppia%rowtype, aUser varchar2, aTSNow date default sysdate) is
 begin
   update scrittura_partita_doppia
    set
	 attiva='N',
	 dt_cancellazione=trunc(aTSNow),
	 utuv=aUser,
	 duva=aTSNow,
	 pg_ver_rec=pg_ver_rec+1
   where
        cd_cds = aScrittura.cd_cds
    and esercizio = aScrittura.esercizio
    and cd_unita_organizzativa = aScrittura.cd_unita_organizzativa
	and pg_scrittura = aScrittura.pg_scrittura;
   aScrittura.attiva:='N';
   aScrittura.dt_cancellazione:=trunc(aTSNow);
   aScrittura.duva:=aTSNow;
   aScrittura.utuv:=aUser;
   aScrittura.pg_ver_rec:=aScrittura.pg_ver_rec+1;
 end;

 procedure eliminaScrittura(aScrittura IN OUT scrittura_partita_doppia%rowtype, aUser varchar2, aTSNow date default sysdate) is
 begin
  for aScr in (select * from scrittura_partita_doppia where
            cd_cds=aScrittura.cd_cds
			and cd_unita_organizzativa = aScrittura.cd_unita_organizzativa
			and esercizio=aScrittura.esercizio
			and pg_scrittura=aScrittura.pg_scrittura
		    for update nowait)
  loop
   delete from movimento_coge where
                cd_cds=aScr.cd_cds
			and cd_unita_organizzativa = aScr.cd_unita_organizzativa
			and esercizio=aScr.esercizio
			and pg_scrittura=aScr.pg_scrittura;
   delete from scrittura_partita_doppia where
                cd_cds=aScr.cd_cds
			and cd_unita_organizzativa = aScr.cd_unita_organizzativa
			and esercizio=aScr.esercizio
			and pg_scrittura=aScr.pg_scrittura;
  end loop;
 end;

 procedure annullaScrittura(aScrittura IN OUT scrittura_analitica%rowtype, aUser varchar2, aTSNow date default sysdate) is
 begin
   update scrittura_analitica
    set
	 attiva='N',
	 dt_cancellazione=trunc(aTSNow),
	 utuv=aUser,
	 duva=aTSNow,
	 pg_ver_rec=pg_ver_rec+1
   where
        cd_cds = aScrittura.cd_cds
    and esercizio = aScrittura.esercizio
    and cd_unita_organizzativa = aScrittura.cd_unita_organizzativa
	and pg_scrittura = aScrittura.pg_scrittura;
   aScrittura.attiva:='N';
   aScrittura.dt_cancellazione:=trunc(aTSNow);
   aScrittura.duva:=aTSNow;
   aScrittura.utuv:=aUser;
   aScrittura.pg_ver_rec:=aScrittura.pg_ver_rec+1;
 end;

 procedure eliminaScrittura(aScrittura IN OUT scrittura_analitica%rowtype, aUser varchar2, aTSNow date default sysdate) is
 begin
  for aScr in (select * from scrittura_analitica where
            cd_cds=aScrittura.cd_cds
			and cd_unita_organizzativa = aScrittura.cd_unita_organizzativa
			and esercizio=aScrittura.esercizio
			and pg_scrittura=aScrittura.pg_scrittura
		    for update nowait)
  loop
   delete from movimento_coan where
                cd_cds=aScr.cd_cds
			and cd_unita_organizzativa = aScr.cd_unita_organizzativa
			and esercizio=aScr.esercizio
			and pg_scrittura=aScr.pg_scrittura;
   delete from scrittura_analitica where
                cd_cds=aScr.cd_cds
			and cd_unita_organizzativa = aScr.cd_unita_organizzativa
			and esercizio=aScr.esercizio
			and pg_scrittura=aScr.pg_scrittura;
  end loop;
 end;

 procedure disattivaScrittura(aScrittura IN OUT scrittura_analitica%rowtype, aUser varchar2, aTSNow date default sysdate) is
 begin
   update scrittura_analitica
    set
	 attiva='N',
	 utuv=aUser,
	 duva=aTSNow,
	 pg_ver_rec=pg_ver_rec+1
   where
        cd_cds = aScrittura.cd_cds
    and esercizio = aScrittura.esercizio
    and cd_unita_organizzativa = aScrittura.cd_unita_organizzativa
	and pg_scrittura = aScrittura.pg_scrittura;
   aScrittura.attiva:='N';
   aScrittura.duva:=aTSNow;
   aScrittura.utuv:=aUser;
   aScrittura.pg_ver_rec:=aScrittura.pg_ver_rec+1;
 end;

 procedure attivaScrittura(aScrittura IN OUT scrittura_partita_doppia%rowtype, aUser varchar2, aTSNow date default sysdate) is
 begin
   update scrittura_partita_doppia
    set attiva='Y',
	 utuv=aUser,
	 duva=aTSNow,
	 pg_ver_rec=pg_ver_rec+1
   where
        cd_cds = aScrittura.cd_cds
    and esercizio = aScrittura.esercizio
    and cd_unita_organizzativa = aScrittura.cd_unita_organizzativa
	and pg_scrittura = aScrittura.pg_scrittura;
   aScrittura.attiva:='Y';
   aScrittura.duva:=aTSNow;
   aScrittura.utuv:=aUser;
   aScrittura.pg_ver_rec:=aScrittura.pg_ver_rec+1;
 end;

 procedure attivaScrittura(aScrittura IN OUT scrittura_analitica%rowtype, aUser varchar2, aTSNow date default sysdate) is
 begin
   update scrittura_analitica
    set attiva='Y',
	 utuv=aUser,
	 duva=aTSNow,
	 pg_ver_rec=pg_ver_rec+1
   where
        cd_cds = aScrittura.cd_cds
    and esercizio = aScrittura.esercizio
    and cd_unita_organizzativa = aScrittura.cd_unita_organizzativa
	and pg_scrittura = aScrittura.pg_scrittura;
   aScrittura.attiva:='Y';
   aScrittura.duva:=aTSNow;
   aScrittura.utuv:=aUser;
   aScrittura.pg_ver_rec:=aScrittura.pg_ver_rec+1;
 end;

 procedure getScritturaEPLock(aScrittura IN OUT scrittura_partita_doppia%rowtype, aListaMovimenti OUT movimentiList) is
  aIndex number;
 begin
  begin
   select * into aScrittura from scrittura_partita_doppia where
        cd_cds = aScrittura.cd_cds
    and esercizio = aScrittura.esercizio
    and cd_unita_organizzativa = aScrittura.cd_unita_organizzativa
	and pg_scrittura = aScrittura.pg_scrittura;
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('Scrittura EP '||getDescScrittura(aScrittura));
  end;
  aListaMovimenti.delete;
  aIndex:=1;
  for aMovimentoEp in (select * from movimento_coge where
                           cd_cds = aScrittura.cd_cds
                       and esercizio = aScrittura.esercizio
                       and cd_unita_organizzativa = aScrittura.cd_unita_organizzativa
					   and pg_scrittura = aScrittura.pg_scrittura
					   for update nowait
					  ) loop
   aListaMovimenti(aIndex):=aMovimentoEp;
   aIndex:=aIndex+1;
  end loop;
 end;

Procedure getScritturaANLock(aScrittura IN OUT scrittura_analitica%rowtype, aListaMovimenti OUT movAnalitList) is
  aIndex number;
 begin
  begin
   select * into aScrittura from scrittura_analitica where
        cd_cds = aScrittura.cd_cds
    and esercizio = aScrittura.esercizio
    and cd_unita_organizzativa = aScrittura.cd_unita_organizzativa
	and pg_scrittura = aScrittura.pg_scrittura;
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('Scrittura AN '||getDescScrittura(aScrittura));
  end;
  aListaMovimenti.delete;
  aIndex:=1;
  for aMovimentoAn in (select * from movimento_coan where
                           cd_cds = aScrittura.cd_cds
                       and esercizio = aScrittura.esercizio
                       and cd_unita_organizzativa = aScrittura.cd_unita_organizzativa
					   and pg_scrittura = aScrittura.pg_scrittura
					   for update nowait
					  ) loop
   aListaMovimenti(aIndex):=aMovimentoAn;
   aIndex:=aIndex+1;
  end loop;
End;

 procedure creaScrittStornoCoge(
   aScrittura IN OUT scrittura_partita_doppia%rowtype,
   aListaMovimenti IN OUT movimentiList,
   aUser varchar2,
   aTSNow date default sysdate
 ) is
  aScritturaOriginale scrittura_partita_doppia%rowtype;
 begin
  if aUser is null then
   IBMERR001.RAISE_ERR_GENERICO('Utente che effettua la variazione non specificato in creazione scrittura di storno');
  end if;
Dbms_Output.PUT_LINE ('creaScrittStornoCoge 1');
  aScritturaOriginale:=aScrittura;
  disattivaScrittura(aScrittura,aUser,aTSNow);
  aScrittura.pg_scrittura:=null;
  for i in 1 .. aListaMovimenti.count loop
Dbms_Output.PUT_LINE ('creaScrittStornoCoge NEL LOOP');
   aListaMovimenti(i).pg_scrittura:=null;
   aListaMovimenti(i).pg_movimento:=null;
   aListaMovimenti(i).sezione:=getSezioneOpposta(aListaMovimenti(i));
  end loop;
  aScrittura.pg_scrittura_annullata:=aScritturaOriginale.pg_scrittura;
Dbms_Output.PUT_LINE ('creaScrittStornoCoge 2');
  CREASCRITTCOGE(aScrittura,aListaMovimenti);
 end;

  procedure annullaScrittCoan (
   aScrittura IN OUT scrittura_analitica%rowtype,
   aListaMovimenti IN OUT movAnalitList,
   aUser varchar2,
   aTSNow date default sysdate
 ) is
  aScritturaOriginale scrittura_analitica%rowtype;
 begin
  if aUser is null then
   IBMERR001.RAISE_ERR_GENERICO('Utente che effettua la variazione non specificato in creazione scrittura di storno');
  end if;
  annullaScrittura(aScrittura,aUser,aTSNow);
  -- Aggiorna i saldi in negativo
  annullaSaldoCoan(
     aScrittura,
     aListaMovimenti);
 end;

  procedure eliminaScrittCoan (
   aScrittura IN OUT scrittura_analitica%rowtype,
   aListaMovimenti IN OUT movAnalitList,
   aUser varchar2,
   aTSNow date default sysdate
 ) is
  aScritturaOriginale scrittura_analitica%rowtype;
 begin
  if aUser is null then
   IBMERR001.RAISE_ERR_GENERICO('Utente che effettua la variazione non specificato in creazione scrittura di storno');
  end if;
  -- Aggiorna i saldi in negativo
  annullaSaldoCoan(
     aScrittura,
     aListaMovimenti);
  eliminaScrittura(aScrittura,aUser,aTSNow);
 end;

 procedure annullaScrittCoge(
   aScrittura IN OUT scrittura_partita_doppia%rowtype,
   aListaMovimenti IN OUT movimentiList,
   aUser varchar2,
   aTSNow date default Sysdate) is
  aScritturaOriginale scrittura_partita_doppia%rowtype;
 begin
  if aUser is null then
   IBMERR001.RAISE_ERR_GENERICO('Utente che effettua la variazione non specificato in creazione scrittura di storno');
  end if;
  annullaScrittura(aScrittura,aUser,aTSNow);
  -- Aggiorna i saldi in negativo
  annullaSaldoCoge(aScrittura, aListaMovimenti);
 end;

 procedure eliminaScrittCoge(
   aScrittura IN OUT scrittura_partita_doppia%rowtype,
   aListaMovimenti IN OUT movimentiList,
   aUser varchar2,
   aTSNow date default sysdate
 ) is
  aScritturaOriginale scrittura_partita_doppia%rowtype;
 begin
  if aUser is null then
   IBMERR001.RAISE_ERR_GENERICO('Utente che effettua la variazione non specificato in creazione scrittura di storno');
  end if;
  -- Aggiorna i saldi in negativo
  annullaSaldoCoge(
     aScrittura,
     aListaMovimenti);
  eliminaScrittura(aScrittura,aUser,aTSNow);
 end;

 procedure creaScrittStornoCoan(
   aScrittura IN OUT scrittura_analitica%rowtype,
   aListaMovimenti IN OUT movAnalitList,
   aUser varchar2,
   aTSNow date default sysdate
 ) is
  aScritturaOriginale scrittura_analitica%rowtype;
 begin
  if aUser is null then
   IBMERR001.RAISE_ERR_GENERICO('Utente che effettua la variazione non specificato in creazione scrittura di storno');
  end if;
  aScritturaOriginale:=aScrittura;
  disattivaScrittura(aScrittura,aUser,aTSNow);
  aScrittura.pg_scrittura:=null;
  for i in 1 .. aListaMovimenti.count loop
   aListaMovimenti(i).pg_scrittura:=null;
   aListaMovimenti(i).pg_movimento:=null;
   aListaMovimenti(i).im_movimento:=-aListaMovimenti(i).im_movimento;
  end loop;
  aScrittura.pg_scrittura_annullata:=aScritturaOriginale.pg_scrittura;
--Dbms_Output.put_line ('chiamata a CREASCRITTCOAN 1');
  CREASCRITTCOAN(aScrittura,aListaMovimenti);
 end;

 procedure creaScrittStornoCoge(
   aCdCds varchar2,
   aEs number,
   aCdUnitaOrganizzativa varchar2,
   aPgScrittura number,
   aUser varchar2,
   aTSNow date default sysdate
 ) is
  aScrittura scrittura_partita_doppia%rowtype;
  aListaMovimenti movimentiList;
 begin
  if aUser is null then
   IBMERR001.RAISE_ERR_GENERICO('Utente che effettua la variazione non specificato in creazione scrittura di storno');
  end if;
  aScrittura.esercizio := aEs;
  aScrittura.cd_cds := aCdCds;
  aScrittura.cd_unita_organizzativa := aCdUnitaOrganizzativa;
  aScrittura.pg_scrittura := aPgScrittura;
  getScritturaEPLock(aScrittura, aListaMovimenti);
  creaScrittStornoCoge(
   aScrittura,
   aListaMovimenti,
   aUser,
   aTSNow);
 end;

 procedure creaScrittStornoCoan(
   aCdCds varchar2,
   aEs number,
   aCdUnitaOrganizzativa varchar2,
   aPgScrittura number,
   aUser varchar2,
   aTSNow date default sysdate
 ) is
  aScrittura scrittura_analitica%rowtype;
  aListaMovimenti movAnalitList;
 begin
  if aUser is null then
   IBMERR001.RAISE_ERR_GENERICO('Utente che effettua la variazione non specificato in creazione scrittura di storno');
  end if;
  aScrittura.cd_cds := aCdCds;
  aScrittura.esercizio := aEs;
  aScrittura.cd_unita_organizzativa := aCdUnitaOrganizzativa;
  aScrittura.pg_scrittura := aPgScrittura;
  getScritturaANLock(aScrittura, aListaMovimenti);
--Dbms_Output.put_line ('a');
  creaScrittStornoCoan(
   aScrittura,
   aListaMovimenti,
   aUser,
   aTSNow);
 end;

 procedure creaScrittCoge(
   aEs number,
   aScrittura IN OUT scrittura_partita_doppia%rowtype,
   aListaMovimenti IN OUT movimentiList
 ) is
   aSaldoCoge saldo_coge%rowtype;
   aTotDare number(15,2);
   aTotAvere number(15,2);
 Begin

  if aListaMovimenti.count=0 then
Dbms_Output.PUT_LINE ('NIENTE MOVIMENTI, ESCE');
   return;
  end if;

Dbms_Output.PUT_LINE ('ARRIVA UTENTE '||aScrittura.UTCR);

  -- Imposta l'esercizio di destinazione
  aScrittura.esercizio:=aEs;
  -- Determina il numero della scrittura
  aScrittura.pg_scrittura:=getNextProgressivo(aScrittura.esercizio,aScrittura.cd_cds, aScrittura.cd_unita_organizzativa, TIPO_COGE,aScrittura.utcr, aScrittura.dacr);

  aScrittura.im_scrittura:=0;
  aTotDare:=0;
  aTotAvere:=0;
  for i in 1 .. aListaMovimenti.count loop
   -- accumula gli importi dei movimenti della sezione dare per avere l'importo complessivo
   -- della scrittura

   if aListaMovimenti(i).SEZIONE = IS_DARE then
    aScrittura.im_scrittura := aScrittura.im_scrittura + aListaMovimenti(i).im_movimento;

    aTotDare := aTotDare + aListaMovimenti(i).im_movimento;
   else
    aTotAvere := aTotAvere + aListaMovimenti(i).im_movimento;
   end if;
  end loop;
  if (aTotAvere <> aTotDare) then
   IBMERR001.RAISE_ERR_GENERICO('Scrittura non quadrata in Dare ('||aTotDare||')/Avere ('||aTotAvere||')');
  end if;

  aScrittura.duva:=aScrittura.dacr;
  aScrittura.utuv:=aScrittura.utcr;
  aScrittura.pg_ver_rec:=1;
  -- Inserisce la testata della scrittura
Dbms_Output.PUT_LINE ('INS '||aScrittura.ESERCIZIO||' '||aScrittura.CD_CDS||' '||aScrittura.CD_UNITA_ORGANIZZATIVA||' '||aScrittura.PG_SCRITTURA);
  ins_SCRITTURA_PARTITA_DOPPIA(aScrittura);
  -- Inserisce i movimenti

  for i in 1 .. aListaMovimenti.count loop
   aListaMovimenti(i).cd_cds:=aScrittura.cd_cds;
   aListaMovimenti(i).esercizio:=aScrittura.esercizio;
   aListaMovimenti(i).cd_unita_organizzativa:=aScrittura.cd_unita_organizzativa;
   aListaMovimenti(i).pg_scrittura:=aScrittura.pg_scrittura;
   aListaMovimenti(i).pg_movimento:=i;
   aListaMovimenti(i).dacr:=aScrittura.dacr;
   aListaMovimenti(i).duva:=aScrittura.duva;
   aListaMovimenti(i).utuv:=aScrittura.utuv;
   aListaMovimenti(i).utcr:=aScrittura.utcr;
   aListaMovimenti(i).pg_ver_rec:=aScrittura.pg_ver_rec;
   if aListaMovimenti(i).TI_ISTITUZ_COMMERC is null then
    aListaMovimenti(i).TI_ISTITUZ_COMMERC := TI_ISTITUZIONALE;
   end if;
   ins_MOVIMENTO_COGE(aListaMovimenti(i));
  end loop;

  -- Aggiorna i saldi

  aggiornaSaldoCoge(
     aScrittura,
     aListaMovimenti);
 end;

 procedure creaScrittCoge(
   aScrittura IN OUT scrittura_partita_doppia%rowtype,
   aListaMovimenti IN OUT movimentiList
 ) is
 begin
  creaScrittCoge(aScrittura.esercizio,aScrittura,aListaMovimenti);
 end;

 procedure creaScrittCoan(
   aScrittura IN OUT scrittura_analitica%rowtype,
   aListaMovimenti IN OUT movAnalitList
 ) is
   aSaldoCoan saldo_coan%rowtype;
 Begin
  -- Determina il numero della scrittura

  aScrittura.pg_scrittura:=getNextProgressivo(aScrittura.esercizio,aScrittura.cd_cds, aScrittura.cd_unita_organizzativa, TIPO_COAN,aScrittura.utcr, aScrittura.dacr);

  aScrittura.im_scrittura:=0;
  for i in 1 .. aListaMovimenti.count loop

   -- accumula gli importi dei movimenti per avere l'importo complessivo della scrittura
   aScrittura.im_scrittura:=aScrittura.im_scrittura+aListaMovimenti(i).im_movimento;
  end loop;

  aScrittura.duva:=aScrittura.dacr;
  aScrittura.utuv:=aScrittura.utcr;
  aScrittura.pg_ver_rec:=1;
  -- Inserisce la testata della scrittura
  ins_SCRITTURA_ANALITICA(aScrittura);
  -- Inserisce i movimenti

  for i in 1 .. aListaMovimenti.count loop
   aListaMovimenti(i).cd_cds:=aScrittura.cd_cds;
   aListaMovimenti(i).esercizio:=aScrittura.esercizio;
   aListaMovimenti(i).cd_unita_organizzativa:=aScrittura.cd_unita_organizzativa;
   aListaMovimenti(i).pg_scrittura:=aScrittura.pg_scrittura;
   aListaMovimenti(i).pg_movimento:=i;
   aListaMovimenti(i).dacr:=aScrittura.dacr;
   aListaMovimenti(i).duva:=aScrittura.duva;
   aListaMovimenti(i).utuv:=aScrittura.utuv;
   aListaMovimenti(i).utcr:=aScrittura.utcr;
   aListaMovimenti(i).pg_ver_rec:=aScrittura.pg_ver_rec;
--Dbms_Output.put_line ('ins finale');
   ins_MOVIMENTO_COAN(aListaMovimenti(i));

  end loop;

  -- Aggiorna i saldi

  aggiornaSaldoCoan(
     aScrittura,
     aListaMovimenti);
 end;

 procedure aggiornaSaldoCoge(aScrittura IN OUT scrittura_partita_doppia%rowtype,
                             aListaMovimenti IN OUT movimentiList,
                             aUser varchar2,
                             aTSNow date default sysdate) is
  aSaldo saldo_coge%rowtype;
  aMovimento movimento_coge%rowtype;
  aTotDare number(15,2);
  aTotAvere number(15,2);
 begin
  for i in 1 .. aListaMovimenti.count loop
   begin
    aMovimento:=aListaMovimenti(i);
    select * into aSaldo
    from saldo_coge
    Where cd_cds = aMovimento.cd_cds
      and esercizio = aMovimento.esercizio
      and cd_unita_organizzativa = aMovimento.cd_unita_organizzativa
      and cd_voce_ep = aMovimento.cd_voce_ep
      and cd_terzo = aMovimento.cd_terzo
      And TI_ISTITUZ_COMMERC = amovimento.TI_ISTITUZ_COMMERC
    for update nowait;
   exception when NO_DATA_FOUND then
    aSaldo.CD_CDS:=aMovimento.cd_cds;
    aSaldo.ESERCIZIO:=aMovimento.esercizio;
    aSaldo.CD_UNITA_ORGANIZZATIVA:=aMovimento.cd_unita_organizzativa;
    aSaldo.CD_VOCE_EP:=aMovimento.cd_voce_ep;
    aSaldo.CD_TERZO:=aMovimento.cd_terzo;
    aSaldo.TI_ISTITUZ_COMMERC:=aMovimento.TI_ISTITUZ_COMMERC;
    aSaldo.TOT_DARE:=0;
    aSaldo.TOT_AVERE:=0;
    aSaldo.DACR:=aTSNow;
    aSaldo.UTCR:=aUser;
    aSaldo.DUVA:=aTSNow;
    aSaldo.UTUV:=aUser;
    aSaldo.PG_VER_REC:=1;
 	begin
	 ins_SALDO_COGE(aSaldo);
    exception when DUP_VAL_ON_INDEX then
     IBMERR001.RAISE_ERR_GENERICO('Saldo scritture COGE temporaneamente non disponibile');
    end;
   end;
   -- aggiorna il saldo
   aTotAvere:=0;
   aTotDare:=0;

-- Vista la nuova gestione sulle modifiche, i saldi degli storni vanno in sezioni opposta ai documenti
--
--   if aScrittura.pg_scrittura_annullata is not null then -- Sugli storni aggiorna in negativo sulla sezione opposta a quella del movimento
--    if aMovimento.sezione = IS_DARE then
--     aTotAvere:=0-aMovimento.im_movimento;
--   else
-- 	 aTotDare:=0-aMovimento.im_movimento;
--    end if;
--   else

    if aMovimento.sezione = IS_DARE then
         aTotDare:=aMovimento.im_movimento;
    else
 	 aTotAvere:=aMovimento.im_movimento;
    end if;

--   end if;

   update saldo_coge
    Set tot_dare  = tot_dare + aTotDare,
        tot_avere = tot_avere + aTotAvere,
 	 utuv=aUser,
 	 duva=aTSNow,
 	 pg_ver_rec=pg_ver_rec+1
   Where cd_cds = aMovimento.cd_cds
     and esercizio = aMovimento.esercizio
     and cd_unita_organizzativa = aMovimento.cd_unita_organizzativa
     and cd_voce_ep = aMovimento.cd_voce_ep
     and cd_terzo = aMovimento.cd_terzo
     And TI_ISTITUZ_COMMERC = aMovimento.TI_ISTITUZ_COMMERC;
  end loop;
 end;

 procedure aggiornaSaldoCoge(aScrittura IN OUT scrittura_partita_doppia%rowtype, aListaMovimenti IN OUT movimentiList) is
 begin
  aggiornaSaldoCoge(aScrittura, aListaMovimenti, aScrittura.utuv, aScrittura.duva);
 end;

 procedure annullaSaldoCoge(aScrittura IN OUT scrittura_partita_doppia%rowtype,
                            aListaMovimenti IN OUT movimentiList,
                            aUser varchar2,
                            aTSNow date default sysdate) is
  aSaldo saldo_coge%rowtype;
  aMovimento movimento_coge%rowtype;
  aTotDare number(15,2);
  aTotAvere number(15,2);
 begin
  for i in 1 .. aListaMovimenti.count loop
   begin
    aMovimento:=aListaMovimenti(i);
    select * into aSaldo
    from saldo_coge
    Where cd_cds = aMovimento.cd_cds
      and esercizio = aMovimento.esercizio
      and cd_unita_organizzativa = aMovimento.cd_unita_organizzativa
      and cd_voce_ep = aMovimento.cd_voce_ep
      and cd_terzo = aMovimento.cd_terzo
      And TI_ISTITUZ_COMMERC = amovimento.TI_ISTITUZ_COMMERC
      for update nowait;
   exception when NO_DATA_FOUND then
    IBMERR001.RAISE_ERR_GENERICO('Saldo scritture COGE da annullare non disponibile');
   end;
   -- aggiorna il saldo
   aTotAvere:=0;
   aTotDare:=0;
   if aMovimento.sezione = IS_DARE then
    aTotDare:=aMovimento.im_movimento;
   else
 	aTotAvere:=aMovimento.im_movimento;
   end if;
   update saldo_coge
    set
     tot_dare=tot_dare-aTotDare,
     tot_avere=tot_avere-aTotAvere,
 	 utuv=aUser,
 	 duva=aTSNow,
 	 pg_ver_rec=pg_ver_rec+1
   where
         cd_cds = aMovimento.cd_cds
     and esercizio = aMovimento.esercizio
     and cd_unita_organizzativa = aMovimento.cd_unita_organizzativa
     and cd_voce_ep = aMovimento.cd_voce_ep
     and cd_terzo = aMovimento.cd_terzo
     And TI_ISTITUZ_COMMERC = aMovimento.TI_ISTITUZ_COMMERC;
  end loop;
 end;

 procedure annullaSaldoCoan(aScrittura IN OUT scrittura_analitica%rowtype, aListaMovimenti IN OUT movAnalitList, aUser varchar2, aTSNow date default sysdate) is
  aSaldo saldo_coan%rowtype;
  aMovimento movimento_coan%rowtype;
  aTotDare number(15,2);
  aTotAvere number(15,2);
 begin
  for i in 1 .. aListaMovimenti.count loop
   begin
    aMovimento:=aListaMovimenti(i);
    select * into aSaldo
    from saldo_coan
    Where cd_cds = aMovimento.cd_cds
     and esercizio = aMovimento.esercizio
     and cd_unita_organizzativa = aMovimento.cd_unita_organizzativa
     and cd_centro_responsabilita = aMovimento.cd_centro_responsabilita
     and cd_linea_attivita = aMovimento.cd_linea_attivita
     and cd_voce_ep = aMovimento.cd_voce_ep
     And TI_ISTITUZ_COMMERC = aMovimento.TI_ISTITUZ_COMMERC
    For update nowait;
   exception when NO_DATA_FOUND then
    IBMERR001.RAISE_ERR_GENERICO('Saldo scritture COGE da annullare non disponibile');
   end;
   -- aggiorna il saldo
   aTotAvere:=0;
   aTotDare:=0;
   if aMovimento.sezione = IS_DARE then
    aTotDare:=aMovimento.im_movimento;
   else
 	aTotAvere:=aMovimento.im_movimento;
   end if;
   update saldo_coan
    Set tot_dare=tot_dare-aTotDare,
        tot_avere=tot_avere-aTotAvere,
 	 utuv=aUser,
 	 duva=aTSNow,
 	 pg_ver_rec=pg_ver_rec+1
   Where cd_cds = aMovimento.cd_cds
     and esercizio = aMovimento.esercizio
     and cd_unita_organizzativa = aMovimento.cd_unita_organizzativa
     and cd_centro_responsabilita = aMovimento.cd_centro_responsabilita
     and cd_linea_attivita = aMovimento.cd_linea_attivita
     and cd_voce_ep = aMovimento.cd_voce_ep
     And TI_ISTITUZ_COMMERC = amovimento.TI_ISTITUZ_COMMERC;
  end loop;
 end;

 procedure annullaSaldoCoge(aScrittura IN OUT scrittura_partita_doppia%rowtype, aListaMovimenti IN OUT movimentiList) is
 begin
  annullaSaldoCoge(aScrittura, aListaMovimenti, aScrittura.utuv, aScrittura.duva);
 end;

 procedure annullaSaldoCoan(aScrittura IN OUT scrittura_analitica%rowtype, aListaMovimenti IN OUT movAnalitList) is
 begin
  annullaSaldoCoan(aScrittura, aListaMovimenti, aScrittura.utuv, aScrittura.duva);
 end;

 procedure aggiornaSaldoCoan(aScrittura IN OUT scrittura_analitica%Rowtype,
                             aListaMovimenti IN OUT movAnalitList,
                             aUser varchar2,
                             aTSNow date default sysdate) is
  aSaldo saldo_coan%rowtype;
  aMovimento movimento_coan%rowtype;
 Begin
  for i in 1 .. aListaMovimenti.count loop
   begin
    aMovimento:=aListaMovimenti(i);

    select * into aSaldo from saldo_coan where
         cd_cds = aMovimento.cd_cds
     and esercizio = aMovimento.esercizio
     and cd_unita_organizzativa = aMovimento.cd_unita_organizzativa
     and cd_voce_ep = aMovimento.cd_voce_ep
     and cd_centro_responsabilita = aMovimento.cd_centro_responsabilita
     and cd_linea_attivita = aMovimento.cd_linea_attivita
     And TI_ISTITUZ_COMMERC = amovimento.TI_ISTITUZ_COMMERC
     for update nowait;

   exception when NO_DATA_FOUND then

    aSaldo.CD_CDS:=aMovimento.cd_cds;
    aSaldo.ESERCIZIO:=aMovimento.esercizio;
    aSaldo.CD_UNITA_ORGANIZZATIVA:=aMovimento.cd_unita_organizzativa;
    aSaldo.CD_VOCE_EP:=aMovimento.cd_voce_ep;
    aSaldo.cd_centro_responsabilita:=aMovimento.cd_centro_responsabilita;
    aSaldo.cd_linea_attivita:=aMovimento.cd_linea_attivita;
    aSaldo.TI_ISTITUZ_COMMERC := aMovimento.TI_ISTITUZ_COMMERC;
    aSaldo.tot_dare:=0;
    aSaldo.tot_avere:=0;
    aSaldo.DACR:=aTSNow;
    aSaldo.UTCR:=aUser;
    aSaldo.DUVA:=aTSNow;
    aSaldo.UTUV:=aUser;
    aSaldo.PG_VER_REC:=1;

    Begin
      Ins_SALDO_COAN(aSaldo);
    Exception when DUP_VAL_ON_INDEX then
      IBMERR001.RAISE_ERR_GENERICO('Saldo scritture COAN temporaneamente non disponibile');
    End;

   end;
   -- aggiorna il saldo coan
	 if aMovimento.sezione = IS_DARE then
           update saldo_coan
            set
             tot_dare=tot_dare+aMovimento.im_movimento,
        	 utuv=aUser,
         	 duva=aTSNow,
         	 pg_ver_rec=pg_ver_rec+1
           where
                 cd_cds = aMovimento.cd_cds
             and esercizio = aMovimento.esercizio
             and cd_unita_organizzativa = aMovimento.cd_unita_organizzativa
             and cd_voce_ep = aMovimento.cd_voce_ep
             and cd_centro_responsabilita = aMovimento.cd_centro_responsabilita
             and cd_linea_attivita = aMovimento.cd_linea_attivita
             And TI_ISTITUZ_COMMERC = amovimento.TI_ISTITUZ_COMMERC;
 	 else
           update saldo_coan
            set
             tot_avere=tot_avere+aMovimento.im_movimento,
        	 utuv=aUser,
         	 duva=aTSNow,
         	 pg_ver_rec=pg_ver_rec+1
           where
                 cd_cds = aMovimento.cd_cds
             and esercizio = aMovimento.esercizio
             and cd_unita_organizzativa = aMovimento.cd_unita_organizzativa
             and cd_voce_ep = aMovimento.cd_voce_ep
             and cd_centro_responsabilita = aMovimento.cd_centro_responsabilita
             and cd_linea_attivita = aMovimento.cd_linea_attivita
             And TI_ISTITUZ_COMMERC = amovimento.TI_ISTITUZ_COMMERC;
     end if;
  end loop;
 end;

 procedure aggiornaSaldoCoan(aScrittura IN OUT scrittura_analitica%rowtype, aListaMovimenti IN OUT movAnalitList) is
 begin
  aggiornaSaldoCoan(aScrittura, aListaMovimenti, aScrittura.utuv, aScrittura.duva);
 end;

 procedure aggiornaSaldoCoge(
   aCdCds varchar2,
   aEs number,
   aCdUnitaOrganizzativa varchar2,
   aPgScrittura number,
   aUser varchar2,
   aTSNow date default sysdate
 ) is
  aScrittura scrittura_partita_doppia%rowtype;
  aListaMovimenti movimentiList;
 begin
  if aUser is null then
   IBMERR001.RAISE_ERR_GENERICO('Utente che effettua la variazione non specificato in creazione scrittura di storno');
  end if;
  aScrittura.cd_cds := aCdCds;
  aScrittura.esercizio := aEs;
  aScrittura.cd_unita_organizzativa := aCdUnitaOrganizzativa;
  aScrittura.pg_scrittura := aPgScrittura;
  getScritturaEPLock(aScrittura, aListaMovimenti);
  aggiornaSaldoCoge(
   aScrittura,
   aListaMovimenti,
   aUser,
   aTSNow
  );
 end;

 procedure aggiornaSaldoCoan(
   aCdCds varchar2,
   aEs number,
   aCdUnitaOrganizzativa varchar2,
   aPgScrittura number,
   aUser varchar2,
   aTSNow date default sysdate
 ) is
  aScrittura scrittura_analitica%rowtype;
  aListaMovimenti movAnalitList;
 begin
  if aUser is null then
   IBMERR001.RAISE_ERR_GENERICO('Utente che effettua la variazione non specificato in creazione scrittura di storno');
  end if;
  aScrittura.cd_cds := aCdCds;
  aScrittura.esercizio := aEs;
  aScrittura.cd_unita_organizzativa := aCdUnitaOrganizzativa;
  aScrittura.pg_scrittura := aPgScrittura;
  getScritturaANLock(aScrittura, aListaMovimenti);
  aggiornaSaldoCoan(
   aScrittura,
   aListaMovimenti,
   aUser,
   aTSNow
  );
 end;

 procedure ins_SCRITTURA_PARTITA_DOPPIA (aDest SCRITTURA_PARTITA_DOPPIA%rowtype) is
  begin
   insert into SCRITTURA_PARTITA_DOPPIA (
     CD_CDS
    ,ESERCIZIO
    ,CD_UNITA_ORGANIZZATIVA
    ,PG_SCRITTURA
    ,ORIGINE_SCRITTURA
    ,CD_TERZO
    ,CD_CAUSALE_COGE
    ,CD_TIPO_DOCUMENTO
	,CD_CDS_DOCUMENTO
	,CD_UO_DOCUMENTO
    ,PG_NUMERO_DOCUMENTO
    ,CD_COMP_DOCUMENTO
    ,IM_SCRITTURA
    ,TI_SCRITTURA
    ,DT_CONTABILIZZAZIONE
    ,DT_PAGAMENTO
    ,DT_CANCELLAZIONE
    ,STATO
    ,CD_DIVISA
    ,COSTO_PLURIENNALE
    ,PG_ENTE
    ,DS_SCRITTURA
    ,PG_SCRITTURA_ANNULLATA
    ,ATTIVA
    ,ESERCIZIO_DOCUMENTO_AMM
    ,DACR
    ,UTCR
    ,DUVA
    ,UTUV
    ,PG_VER_REC
   ) values (
     aDest.CD_CDS
    ,aDest.ESERCIZIO
    ,aDest.CD_UNITA_ORGANIZZATIVA
    ,aDest.PG_SCRITTURA
    ,aDest.ORIGINE_SCRITTURA
    ,aDest.CD_TERZO
    ,aDest.CD_CAUSALE_COGE
    ,aDest.CD_TIPO_DOCUMENTO
	,aDest.CD_CDS_DOCUMENTO
	,aDest.CD_UO_DOCUMENTO
    ,aDest.PG_NUMERO_DOCUMENTO
    ,aDest.CD_COMP_DOCUMENTO
    ,aDest.IM_SCRITTURA
    ,aDest.TI_SCRITTURA
    ,aDest.DT_CONTABILIZZAZIONE
    ,aDest.DT_PAGAMENTO
    ,aDest.DT_CANCELLAZIONE
    ,aDest.STATO
    ,aDest.CD_DIVISA
    ,aDest.COSTO_PLURIENNALE
    ,aDest.PG_ENTE
    ,aDest.DS_SCRITTURA
    ,aDest.PG_SCRITTURA_ANNULLATA
    ,aDest.ATTIVA
    ,aDest.ESERCIZIO_DOCUMENTO_AMM
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.DUVA
    ,aDest.UTUV
    ,aDest.PG_VER_REC
    );
 end;
 procedure ins_SCRITTURA_ANALITICA (aDest SCRITTURA_ANALITICA%rowtype) is
  begin
   insert into SCRITTURA_ANALITICA (
     CD_CDS
    ,ESERCIZIO
    ,CD_UNITA_ORGANIZZATIVA
    ,PG_SCRITTURA
    ,ORIGINE_SCRITTURA
	,CD_TERZO
    ,CD_TIPO_DOCUMENTO
    ,CD_CDS_DOCUMENTO
    ,CD_UO_DOCUMENTO
    ,PG_NUMERO_DOCUMENTO
    ,CD_COMP_DOCUMENTO
    ,IM_SCRITTURA
    ,TI_SCRITTURA
    ,DT_CONTABILIZZAZIONE
    ,DT_CANCELLAZIONE
    ,STATO
    ,DS_SCRITTURA
    ,PG_SCRITTURA_ANNULLATA
    ,ATTIVA
    ,ESERCIZIO_DOCUMENTO_AMM
    ,DACR
    ,UTCR
    ,DUVA
    ,UTUV
    ,PG_VER_REC
   ) values (
     aDest.CD_CDS
    ,aDest.ESERCIZIO
    ,aDest.CD_UNITA_ORGANIZZATIVA
    ,aDest.PG_SCRITTURA
    ,aDest.ORIGINE_SCRITTURA
	,aDest.CD_TERZO
    ,aDest.CD_TIPO_DOCUMENTO
    ,aDest.CD_CDS_DOCUMENTO
    ,aDest.CD_UO_DOCUMENTO
    ,aDest.PG_NUMERO_DOCUMENTO
    ,aDest.CD_COMP_DOCUMENTO
    ,aDest.IM_SCRITTURA
    ,aDest.TI_SCRITTURA
    ,aDest.DT_CONTABILIZZAZIONE
    ,aDest.DT_CANCELLAZIONE
    ,aDest.STATO
    ,aDest.DS_SCRITTURA
    ,aDest.PG_SCRITTURA_ANNULLATA
    ,aDest.ATTIVA
    ,aDest.ESERCIZIO_DOCUMENTO_AMM
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.DUVA
    ,aDest.UTUV
    ,aDest.PG_VER_REC
    );
 end;
 procedure ins_MOVIMENTO_COGE (aDest MOVIMENTO_COGE%rowtype) is
  begin
   insert into MOVIMENTO_COGE (
     CD_CDS
    ,ESERCIZIO
    ,CD_UNITA_ORGANIZZATIVA
    ,PG_SCRITTURA
    ,PG_MOVIMENTO
    ,CD_TERZO
    ,CD_VOCE_EP
    ,IM_MOVIMENTO
    ,SEZIONE
    ,DT_DA_COMPETENZA_COGE
    ,DT_A_COMPETENZA_COGE
    ,STATO
    ,DACR
    ,UTCR
    ,DUVA
    ,UTUV
    ,PG_VER_REC
	,TI_ISTITUZ_COMMERC
	,fl_mov_terzo
   ) values (
     aDest.CD_CDS
    ,aDest.ESERCIZIO
    ,aDest.CD_UNITA_ORGANIZZATIVA
    ,aDest.PG_SCRITTURA
    ,aDest.PG_MOVIMENTO
    ,aDest.CD_TERZO
    ,aDest.CD_VOCE_EP
    ,aDest.IM_MOVIMENTO
    ,aDest.SEZIONE
    ,aDest.DT_DA_COMPETENZA_COGE
    ,aDest.DT_A_COMPETENZA_COGE
    ,aDest.STATO
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.DUVA
    ,aDest.UTUV
    ,aDest.PG_VER_REC
	,aDest.TI_ISTITUZ_COMMERC
	,aDest.fl_mov_terzo
    );
 end;
 procedure ins_MOVIMENTO_COAN (aDest MOVIMENTO_COAN%rowtype) is
  begin
   insert into MOVIMENTO_COAN (
     CD_CDS
    ,ESERCIZIO
    ,CD_UNITA_ORGANIZZATIVA
    ,PG_SCRITTURA
    ,CD_VOCE_EP
    ,PG_MOVIMENTO
    ,SEZIONE
    ,TI_ISTITUZ_COMMERC
    ,CD_CENTRO_RESPONSABILITA
    ,CD_TERZO
    ,IM_MOVIMENTO
    ,CD_FUNZIONE
    ,CD_NATURA
    ,STATO
    ,DS_MOVIMENTO
    ,DACR
    ,UTCR
    ,DUVA
    ,UTUV
    ,PG_VER_REC
    ,CD_LINEA_ATTIVITA
    ,PG_NUMERO_DOCUMENTO
   ) values (
     aDest.CD_CDS
    ,aDest.ESERCIZIO
    ,aDest.CD_UNITA_ORGANIZZATIVA
    ,aDest.PG_SCRITTURA
    ,aDest.CD_VOCE_EP
    ,aDest.PG_MOVIMENTO
    ,aDest.SEZIONE
    ,ADest.TI_ISTITUZ_COMMERC
    ,aDest.CD_CENTRO_RESPONSABILITA
    ,aDest.CD_TERZO
    ,aDest.IM_MOVIMENTO
    ,aDest.CD_FUNZIONE
    ,aDest.CD_NATURA
    ,aDest.STATO
    ,aDest.DS_MOVIMENTO
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.DUVA
    ,aDest.UTUV
    ,aDest.PG_VER_REC
    ,aDest.CD_LINEA_ATTIVITA
    ,aDest.PG_NUMERO_DOCUMENTO
    );
 end;
 procedure ins_SALDO_COGE (aDest SALDO_COGE%rowtype) is
  begin
   insert into SALDO_COGE (
     CD_CDS
    ,ESERCIZIO
    ,CD_UNITA_ORGANIZZATIVA
    ,CD_VOCE_EP
    ,CD_TERZO
    ,TI_ISTITUZ_COMMERC
    ,TOT_DARE
    ,TOT_AVERE
    ,DACR
    ,UTCR
    ,DUVA
    ,UTUV
    ,PG_VER_REC
   ) values (
     aDest.CD_CDS
    ,aDest.ESERCIZIO
    ,aDest.CD_UNITA_ORGANIZZATIVA
    ,aDest.CD_VOCE_EP
    ,aDest.CD_TERZO
    ,adest.TI_ISTITUZ_COMMERC
    ,aDest.TOT_DARE
    ,aDest.TOT_AVERE
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.DUVA
    ,aDest.UTUV
    ,aDest.PG_VER_REC
    );
 end;
 procedure ins_SALDO_COAN (aDest SALDO_COAN%rowtype) is
  begin
   insert into SALDO_COAN (
     CD_CDS
    ,ESERCIZIO
    ,CD_UNITA_ORGANIZZATIVA
    ,CD_CENTRO_RESPONSABILITA
    ,CD_LINEA_ATTIVITA
    ,CD_VOCE_EP
    ,TI_ISTITUZ_COMMERC
    ,TOT_DARE
    ,TOT_AVERE
    ,DACR
    ,UTCR
    ,DUVA
    ,UTUV
    ,PG_VER_REC
   ) values (
     aDest.CD_CDS
    ,aDest.ESERCIZIO
    ,aDest.CD_UNITA_ORGANIZZATIVA
    ,aDest.CD_CENTRO_RESPONSABILITA
    ,aDest.CD_LINEA_ATTIVITA
    ,aDest.CD_VOCE_EP
    ,adest.TI_ISTITUZ_COMMERC
    ,aDest.TOT_DARE
    ,aDest.TOT_AVERE
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.DUVA
    ,aDest.UTUV
    ,aDest.PG_VER_REC
    );
 End;

 procedure ins_NUMERAZIONE_COGE_COAN (aDest NUMERAZIONE_COGE_COAN%rowtype) is
  begin
   insert into NUMERAZIONE_COGE_COAN (
     CD_CDS
    ,ESERCIZIO
    ,CD_UNITA_ORGANIZZATIVA
    ,TI_DOCUMENTO
    ,PRIMO
    ,CORRENTE
    ,ULTIMO
    ,UTCR
    ,DACR
    ,UTUV
    ,DUVA
    ,PG_VER_REC
   ) values (
     aDest.CD_CDS
    ,aDest.ESERCIZIO
    ,aDest.CD_UNITA_ORGANIZZATIVA
    ,aDest.TI_DOCUMENTO
    ,aDest.PRIMO
    ,aDest.CORRENTE
    ,aDest.ULTIMO
    ,aDest.UTCR
    ,aDest.DACR
    ,aDest.UTUV
    ,aDest.DUVA
    ,aDest.PG_VER_REC
    );
 end;

 procedure ins_CHIUSURA_COEP (aDest CHIUSURA_COEP%rowtype) is
  begin
   insert into CHIUSURA_COEP (
     CD_CDS
    ,ESERCIZIO
    ,STATO
    ,DACR
    ,UTCR
    ,DUVA
    ,UTUV
    ,PG_VER_REC
   ) values (
     aDest.CD_CDS
    ,aDest.ESERCIZIO
    ,aDest.STATO
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.DUVA
    ,aDest.UTUV
    ,aDest.PG_VER_REC
    );
 end;
end;


