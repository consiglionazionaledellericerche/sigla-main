--------------------------------------------------------
--  DDL for Package CNRCTB204
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRCTB204" as
--
-- CNRCTB204 - Package di servizio scritture COEP
-- Date: 24/09/2004
-- Version: 1.26
--
-- Package per la gestione di servizio per le scritture COEP
--
-- Dependency: CNRCTB 001/002/008/038/100/200 IBMERR 001
--
-- History:
--
-- Date: 17/11/2002
-- Version: 1.0
-- Creazione
--
-- Date: 26/11/2002
-- Version: 1.1
-- Completamento riorganizzazione economica
--
-- Date: 28/11/2002
-- Version: 1.2
-- GEstione flag istituzionale commerciale su movimenti coge
--
-- Date: 08/01/2003
-- Version: 1.3
-- Fix su estrazione scritture banca cassa
--
-- Date: 27/02/2003
-- Version: 1.4
-- Fix recupero contropartita anagrafica correttamente
-- Fix recupero conto CORI per IVA nel caso di dt_fine_competenza 2002 del documento ed esercizio = primo esercizio app.
--
-- Date: 18/06/2003
-- Version: 1.5
-- Modifica dei getter dei descrittori dei documenti
--
-- Date: 19/06/2003
-- Version: 1.6
-- Aggiunto metodo descrizione buono di carico scarico dettaglio
--
-- Date: 07/07/2003
-- Version: 1.7
-- Aggiunto il metodo di estrazione delle scritture con causale di un doc amm.
--
-- Date: 08/07/2003
-- Version: 1.8
-- Aggiunti metodi per generazione scritture in esercizio precedente
-- L'estrazione delle scritture prime è fatto con l'esercizio del doc amministrativo
-- e non con quello della scrittura in tabella scrittura_partita_doppia
--
-- Date: 09/07/2003
-- Version: 1.9
-- Modificato estrattore competenza fuori esercizio
--
-- Date: 12/07/2003
-- Version: 1.10
-- Estrattore scritture generiche per causale
--
-- Date: 13/07/2003
-- Version: 1.11
-- Aggiunto metodo di recupero sezione di chiusura scrittura
--
-- Date: 15/07/2003
-- Version: 1.12
-- Corretto metodo di recupero del conto di contropartita anagrafica per note credito attive/pass
--
-- Date: 16/07/2003
-- Version: 1.13
-- Eliminato il getter di scrittura BANCA CASSA inglobato nel getter di scritture ultime
--
-- Date: 17/07/2003
-- Version: 1.14
-- Aggiunto metodo per inserimento scritture ultime con causale
--
-- Date: 18/07/2003
-- Version: 1.15
-- Aggiunto il metodo di estrazione in stringa di pk di liquidazione IVA
--
-- Date: 01/08/2003
-- Version: 1.16
-- Modifice richiesta il 01/08/2003: nel caso la competenza econ. sia in es. prec. chiuso, la scrittura è del tutto normale
--
-- Date: 04/08/2003
-- Version: 1.17
-- Messaggio parlante in mancata associazione conto finanziario/economico
--
-- Date: 04/08/2003
-- Version: 1.18
-- Aggiunto estrattore scritture per causale e origine
--
-- Date: 07/08/2003
-- Version: 1.19
-- Sistemazione recupero configurazioni da esercizio mandato in scritture ultime e non da esercizio doc amministrativo
--
-- Date: 07/08/2003
-- Version: 1.20
-- Il recupero della sezione del movimento principale della prima scrittura non deve essere influenzato dal fatto
-- che la riga (proveniente da fattura) sia di sconto abbuono: è il metodo di costruzione del movimento che trovando
-- l'importo imponibile sulla riga < 0 inverte la sezione rispetto alla principale del documento
--
-- Date: 25/08/2003
-- Version: 1.21
-- Fix errore n. 579
-- Reintroduzione delle regole in deroga per PRIMO esercizio applicazione che sostituiscono al conto di costo/ricavo
-- lo stato patrimoniale iniziale o il conto fatture da emettere a seconda del caso.
--
-- Date: 17/09/2003
-- Version: 1.22
-- Fix errore di determinazione conto anagrafica per note di credito
--
-- Date: 19/02/2004
-- Version: 1.23
-- Aggiunta gestione estrazione giusto conto fatture da emettere/ricevere per ratei su fatture passive/note di credito/deb.
--
-- Date: 01/03/2004
-- Version: 1.24
-- La commpressione dei movimenti in buildMovPrinc distingue tra istituzionale e commerciale
--
-- Date: 26/08/2004
-- Version: 1.25
-- Riordinamento funzioni di utilità
--
-- Date: 24/09/2004
-- Version: 1.26
-- Richiesta 843: introdotto metodo di controllo chiusura esercizio
--
-- Constants:

-- Costanti relative alla dismissione del bene durevole

-- Costanti anagrafica
-- Tipo entita giuridica
TI_ANAG_ENT_GIU CONSTANT VARCHAR2(5) := 'G';

-- Functions e Procedures:

-- Build movimenti (istituzionale)

 procedure buildMovPrinc(
  aCdCds varchar2,
  aEs number,
  aCdUo varchar2,
  aContoEp voce_ep%rowtype,
  aImporto number,
  aSezione char,
  aDaCompCoge date,
  aACompCoge date,
  aCdTerzo number,
  aListaMovimenti IN OUT CNRCTB200.movimentiList,
  aUser varchar2,
  aTSnow date
 );


-- Build movimenti (controllo istituzionale/commerciale)
 procedure buildMovPrinc(
  aCdCds varchar2,
  aEs number,
  aCdUo varchar2,
  aContoEp voce_ep%rowtype,
  aImporto number,
  aSezione char,
  aDaCompCoge date,
  aACompCoge date,
  aCdTerzo number,
  aTiIstituzCommerc char,
  aListaMovimenti IN OUT CNRCTB200.movimentiList,
  aUser varchar2,
  aTSnow date
 );

-- Build movimento di chiusura della scrittura per simmetrizzazione del saldo sul conto specificato
 procedure buildChiusuraScrittura(
  aCdCds varchar2,
  aEs number,
  aCdUo varchar2,
  aContoEp voce_ep%rowtype,
  aDaCompCoge date,
  aACompCoge date,
  aCdTerzo number,
  aTiIstituzCommerc char,
  aListaMovimenti IN OUT CNRCTB200.movimentiList,
  aUser varchar2,
  aTSnow date
 );

-- Ritorna la sezione di chiusura della scrittura sulla base degli importi degli altri movimenti
 function getSezioneChiusuraScr(aListaMovimenti IN CNRCTB200.movimentiList) return char;

-- Costruisce la scrittura
 function buildScrPEP(aDocTst V_DOC_AMM_COGE_TSTA%rowtype, aCdTerzo number, aUser varchar2, aTSnow date) return scrittura_partita_doppia%rowtype;

-- Costruisce la scrittura di annullamento di documento di esercizio chiuso
 function buildScrPEPAnnull(aEs number, aDocTst V_DOC_AMM_COGE_TSTA%rowtype, aCdTerzo number, aUser varchar2, aTSnow date) return scrittura_partita_doppia%rowtype;

-- Costruisce la scrittura ultima senza causale
 function buildScrUEP(aDocTst V_DOC_ULT_COGE_TSTA%rowtype, aCdTerzo number, aTipoScrittura char, aUser varchar2, aTSnow date) return scrittura_partita_doppia%rowtype;

-- Costruisce la scrittura ultima con causale
 function buildScrUEP(aDocTst V_DOC_ULT_COGE_TSTA%rowtype, aCdTerzo number, aTipoScrittura char, aCdCausale varchar2, aUser varchar2, aTSnow date) return scrittura_partita_doppia%rowtype;

-- Estrae una stringa con le informazioni principali relative al documento in processo COGE (PRIMO)
function getDescDocumento(aDocTst V_DOC_AMM_COGE_TSTA%rowtype) return varchar2;

-- Estrae una stringa con le informazioni principali relative al documento in processo COGE (PRIMO)
function getDescDocumento(aDocTst V_DOC_ULT_COGE_TSTA%rowtype) return varchar2;

-- Estrae una stringa con le informazioni principali relative al documento in processo COGE (Dismissione)
function getDescDocumento(aBS buono_carico_scarico_dett%rowtype) return varchar2;

-- Estrae le scritture PRIME collegate al documento aDocT e locka le testate
 procedure getScritturePEPLock(aDocTst V_DOC_AMM_COGE_TSTA%rowtype, aListaScritture OUT CNRCTB200.scrittureList);

-- Estrae le scritture della causale specificata al documento aDocT e locka le testate
 procedure getScritturePEPLock(aDocTst V_DOC_AMM_COGE_TSTA%rowtype, aCdCausaleCoge varchar2, aListaScritture OUT CNRCTB200.scrittureList);

-- Estrae le scritture in esercizio specificato della causale specificata al documento aDocT e locka le testate
 procedure getScritturePEPLock(aEs number, aDocTst V_DOC_AMM_COGE_TSTA%rowtype, aCdCausaleCoge varchar2, aListaScritture OUT CNRCTB200.scrittureList);

-- Estrae le scritture di ammortamento in esercizio specificato per il cds specificato
 procedure getScrittureAmmortamentoLock(aEs number, aCdCds varchar2, aListaScritture OUT CNRCTB200.scrittureList);

-- Estrae le scritture ULTIME collegate al documento aDocT e locka le testate
 procedure getScrittureUEPLock(aDocTst V_DOC_ULT_COGE_TSTA%rowtype, aListaScritture OUT CNRCTB200.scrittureList);

-- Estrae le scritture per causale e origine nell'esercizio spec. e nel cds specificato
 procedure getScrittureLock(aEs number, aCdCds varchar2, aCdCausaleCoge varchar2, aOrigine varchar2, aListaScritture OUT CNRCTB200.scrittureList);

 Function ISREVERSALESUPGIRO (aDocRiga In V_DOC_ULT_COGE_RIGA%Rowtype) Return Boolean;

 Function ISMANDATOCOLLSUPGIRO (aDocRiga In V_DOC_ULT_COGE_RIGA%Rowtype) Return Boolean;

 Function ISVOCECOLLCATEGORIA (INES NUMBER, INGEST CHAR, INAPP CHAR, INELVOCE VARCHAR2) Return Boolean;

-- =============================================================
-- SCRITTURA in partita doppia di tipo PRIMO
-- =============================================================

-- Recupera la sezione principale del documento (PRIMO)
 function getSezione(aDocTst V_DOC_AMM_COGE_TSTA%rowtype, aDoc V_DOC_AMM_COGE_RIGA%rowtype) return char;
-- Recupera la sezione principale per CORI (PRIMO)
 function getSezione(aDocTst V_DOC_AMM_COGE_TSTA%rowtype, aCori V_DOC_AMM_COGE_CORI%rowtype) return char;

-- Recupera il conto economico da utilizzare per la registrazione del movimento (PRIMO)
 function trovaContoEp(aDocTst V_DOC_AMM_COGE_TSTA%rowtype, aDoc V_DOC_AMM_COGE_RIGA%rowtype) return voce_ep%rowtype;
-- Recupera il conto economico da utilizzare per la registrazione del movimento CORI(PRIMO)
 function trovaContoEp(aDocTst V_DOC_AMM_COGE_TSTA%rowtype, aCori V_DOC_AMM_COGE_CORI%rowtype) return voce_ep%rowtype;
-- Recupera il conto economico da utilizzare in scrittura PRIMA per la registrazione del movimento di contropartita anagrafica (PRIMO)
 function trovaContoContrEp(aDocTst V_DOC_AMM_COGE_TSTA%rowtype, aCdTerzo number, ACdElementoVoce varchar2) return voce_ep%rowtype;

-- =============================================================
-- SCRITTURA in partita doppia di tipo ULTIMO O SINGOLO
-- =============================================================

-- Recupera la sezione principale del documento (ULTIMO)
 function getSezione(aDocTst V_DOC_ULT_COGE_TSTA%rowtype) return char;

-- Recupera il conto economico da utilizzare in scrittura ULTIMA per movimenti CORI(ULTIMO)
 function trovaContoContrEp(aDocTst V_DOC_AMM_COGE_TSTA%rowtype, aCori V_DOC_AMM_COGE_CORI%rowtype) return voce_ep%rowtype;
-- Recupera il conto economico da utilizzare in scrittura ULTIMA per la registrazione a BANCA
 function trovaContoContrEp(aDocTst V_DOC_ULT_COGE_TSTA%rowtype, aDoc V_DOC_ULT_COGE_RIGA%rowtype) return voce_ep%rowtype;

-- Trova il conto associato all'entità anagrafica specificata utilizzando anche il tipo di documento
 function trovaContoAnag(aEs number, aCdTerzo number, aTipoDoc varchar2, aTiFattura char,ACdElementoVoce varchar2) return voce_ep%rowtype;

 function trovaContoAnag(aDocUltRiga V_DOC_ULT_COGE_RIGA%rowtype, aCdTerzo number,ACdElementoVoce varchar2) return voce_ep%rowtype;

 function trovaContoAnag(aDocTst V_DOC_AMM_COGE_TSTA%rowtype, aCdTerzo number,ACdElementoVoce varchar2) return voce_ep%rowtype;

-- Ritorna il tipo di scrittura S Singola o U Ultima
 function getTipoScrittura(aDoc V_DOC_ULT_COGE_RIGA%rowtype) return char;

-- Trova la sezione del cori
 function getSezioneCoriComp(aCori contributo_ritenuta%rowtype) return char;

-- Ritorna l'associazione tra CORI e VOCE_EP
 function getAssCoriEp(
  aEs number,
  aTipoCori varchar2,
  aTipoEntePercipiente char,
  aSezione char
 ) return ass_tipo_cori_voce_ep%rowtype;

 function getAssCoriEp(aDocTst V_DOC_AMM_COGE_TSTA%rowtype, aCori V_DOC_AMM_COGE_CORI%rowtype) return ass_tipo_cori_voce_ep%rowtype;

-- Ritorna true se la coge è da differire
 function checkIsCogeDifferita(aDocTst V_DOC_AMM_COGE_TSTA%rowtype) return boolean;

-- Ritorna true se l'esercizio è quello di partenza
 function isEsercizioPartenza(aEs number) return boolean;
-- Ritorna l'anno del timestamp passato
 function getEsercizio(aTS date) return number;

-- Verifica se la data specificata (fine periodo di competenza) è fuori esercizio o in esercizio
-- Soleva eccezioni nel caso in cui la data di fine validità sia in esercizio precedente ed il corrente non è il primo esercizio in assoluto (2003)
-- Soleva eccezioni nel caso in cui la data di fine validità sia in esercizi precedenti al precedente
 function getCompetenzaFuoriEsercizio(aDocTst V_DOC_AMM_COGE_TSTA%rowtype) return boolean;

-- Verifica se il documento amministrativo passato ha date di competenza
-- a cavallo di esercizio
 Function getCompetenzaacavalloconEsPrec(aDocTst V_DOC_AMM_COGE_TSTA%rowtype) return boolean;

 function getContoEpPgiro(aDocTst v_doc_amm_coge_tsta%rowtype,aDoc v_doc_amm_coge_riga%rowtype) return voce_ep%rowtype;

-- Trasforma la primary key della liquidazione in una stringa da utilizzare come identificativo univoco
 function pk2String(aLiqIva liquidazione_iva%rowtype) return varchar2;

-- Recupera l'anticipo collegato al documento specificato (Missione o compenso)
 function getAnticipo(aDocTst V_DOC_AMM_COGE_TSTA%rowtype) return anticipo%rowtype;
 function getAnticipo(aDocTst V_DOC_ULT_COGE_RIGA%rowtype) return anticipo%rowtype;

-- Ritorna 'Y' se il compenso deriva da missione con anticipo
-- e l'anticipo è maggiore del netto percipiente
-- Solleva eccezione se il documento in aDocTst NON E' UN COMPENSO
 function isCompConAntMaggNetto(aDocTst v_doc_amm_coge_tsta%rowtype) return char;
 function isCompConAntMaggNetto(aDocTst v_doc_ult_coge_riga%rowtype) return char;

-- RILEVA SE, PER UNA NOTA CREDITO, LA COMPETENZA ECONOMICA DELLA FATTURA E' IN ESERCIZIO PRECEDENTE CHIUSO
 Function isCompFattEsPreChiusoADTREG (aDocTst V_DOC_AMM_COGE_TSTA%rowtype, aDoc V_DOC_AMM_COGE_RIGA%rowtype) return Boolean;

-- Richiesta 843: nuove verifiche su chiusura degli esercizio finanziari ed economici
-- Pre: Richiesta verifica stato finanziario ed economico del cds per esercizio
-- Post:
--      Se l'esercizio finanziario non è aperto o chiuso solleva eccezione
--      Se l'esercizio economico è chiuso definitivamente o in fase di chiusura definitiva solleva eccezione
 procedure checkChiusuraEsercizio(aEs number, aCdCds varchar2);

-- Recupera il conto economico di contropartita
 function trovaContoContrEp(aEs number,aTiAppartenza char,aTiGestione Char, aEV varchar2,aVocein varchar2) return voce_ep%rowtype;

end;
