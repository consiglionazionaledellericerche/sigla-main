--------------------------------------------------------
--  DDL for Package CNRCTB001
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRCTB001" as
--
-- CNRCTB001 - Package di gestione on-line del PDC Finanziario
-- Date: 14/09/2005
-- Version: 4.10
--
-- Gestione on-line del piano dei conti finanziario: metodi di aggiunzione eliminazione
-- di voci a partire da anagrafica conti.
--
-- Dependency: CNRCTB 020 IBMUTL 001 IBMERR 001
--
-- History:
-- 1.0 04/05/2001 - Creazione + Aggiunta gestione ins/mod in VOCE_F indotta da ins/mod UO/CDR
-- 1.1 06/05/2001 - Aggiunto ins in VOCE_F indotto da ins in incroci
-- 1.2 07/05/2001 - Aggiunta l'eliminazione automatica di voci
-- 1.3 18/05/2001 - Aggiornamento meccanismi di derivazione della voce_f
--              (aggiunta meccanismi anche per parti le cablate di elemento_voce)
-- 1.4 01/06/2001 - Fix errore esplosione STO che crea scorrettamente cd_voce
-- 1.5 16/06/2001 - Fix errori eliminazione associazione funzione, capitolo tipo_cds
-- 1.6 21/06/2001 - Fix errori + nuova gestione Aree ricerca
-- 1.7 24/06/2001 - L'area di ricerca non viene gestita via triggers: gestione applicativa completa
-- 1.8 26/06/2001 - Introdotto il cd_categoria su voce_f PER LE SPESE CNR PARTE 1
-- 1.9 26/06/2001 - Gestione eliminazione per cancellazione UO/CDR gestita a livello di trigger
-- 2.0 07/07/2001 - Fix errori
-- 2.1 12/07/2001 - Fix errori
-- 2.2 20/09/2001 - Fix errori (in creaEsplVociUO, sezione e rubrica in CDS Spese, ti_voce = C errato)
-- 2.3 30/09/2001 - Fix errore (cd_categoria non impostato su categoria spesa CNR)
--                  + il sottoarticolo = CDS creato solo per nature 1-4 in spesa CNR
-- 2.4 03/10/2001 - Aggiunta esplosione sottoarticoli aree in presidente area (da invocare appl.)
-- 2.5 16/10/2001 - Aggiunta gestione dell'ente TIPO_ENTE = 'ENTE'
-- 2.6 30/10/2001 - Aggiunta della sottoarticolazione della tipologia di intervento
-- 2.7 31/10/2001 - Fix errori
-- 2.8 05/11/2001 - Gestione esplosione tipologia di intervento a livello di funzione
--                - eliminazione della gestione SAC come presidente di AREA
-- 3.1 08/11/2001 - Eliminazione esercizio da STO
-- 3.2 16/11/2001 - Tolto insertElementoVoce
-- 3.3 03/12/2001 - Il capitolo CDS di entrata non veniva esploso con fl_mastrino = 'Y'
-- 3.4 04/12/2001 - Fix esplosione voci per inserimento UO rubrica + Fix voce padre in Rubrica Spesa CDS
-- 3.5 06/12/2001 - Fix esplosione voci nel caso NON sia ancora stato definito il CDS SAC
-- 3.6 11/12/2001 - Fix esplosione UO su spesa CDS - esplodeva solo il primo elemento_voce trovato
-- 3.7 04/01/2002 - Aggiunti controlli di eliminabilità degli incroci
-- 3.8 13/01/2002 - Gestione degli articoli non rubrica in spesa CDS parte 1 per il SAC:
--                  L'articolo avrà come chiave propria la chiave dell'UO di appartenenza del CDR (nel SAC esiste
--                  un solo CDR per UO)
-- 3.9 14/01/2002 - Fix errore: mancanza esercizio su query su V_UNITA_ORGANIZZATIVA_VALIDA
-- 4.0 15/01/2002 - Introdotto lo user come parametro per le procedure creaEsplVociUO e creaEsplVociCDR
-- 4.1 16/01/2002 - Introdotto il metodo di esplosione delle voci a partire dall'inserimento di un esercizio per un dato CDS
-- 4.2 16/01/2002 - Integrazione dell'ex package CNRCTB003
-- 4.3 21/01/2002 - Gestione del codice categoria in Capitolo e Articolo di ENTRATA CNR
-- 4.4 22/01/2002 - Fix gestione del codice categoria in Capitolo e Articolo di ENTRATA CNR - il codice è il codice proprio della categoria
-- 4.5 26/01/2002 - Esercizio 0 da non gestire in esplosione voci
-- 4.6 27/01/2002 - titolo-capitolo con semantica estesa + cd_cateoria valorizzato dalla categoria
-- 4.7 25/02/2002 - fix esplosione UO RUBRICA sotto entrate CNR in PDC FIN.
-- 4.8 18/07/2002 - Aggiornamento documentazione
--
-- Date: 23/09/2003
-- Version: 4.9
-- Inseriti hints di accesso per la voce_f
--
-- Date: 14/09/2005
-- Version: 4.10
-- Modificata la procedura creaEsplSottArtArea per gestire l'associazione multipla UO/Aree
--
-- Constants:

-- Tipi di unità organizzativa
--
-- Amministrazione centrale
TIPOCDS_SAC CONSTANT VARCHAR(10) :='SAC';
-- Istituto
TIPOCDS_IST CONSTANT VARCHAR(10) :='IST';
-- Programma Nazionale/Internazionale di ricerca
TIPOCDS_PNIR CONSTANT VARCHAR(10) :='PNIR';
-- Area di Ricerca
TIPOCDS_AREA CONSTANT VARCHAR(10) :='AREA';
-- Ente CNR
TIPO_ENTE CONSTANT VARCHAR(10) :='ENTE';

-- Tipi di appartenenza
APPARTENENZA_CDS CONSTANT CHAR(1) := 'D';
APPARTENENZA_CNR CONSTANT CHAR(1) := 'C';

-- Tipi di gestione
GESTIONE_SPESE CONSTANT CHAR(1) := 'S';
GESTIONE_ENTRATE CONSTANT CHAR(1) := 'E';

-- Tipi componenti albero voce_f
PARTE CONSTANT CHAR(1) := 'P';
CAPITOLO CONSTANT CHAR(1) := 'C';
TITOLO CONSTANT CHAR(1) := 'T';
CATEGORIA CONSTANT CHAR(1) := 'G';
SEZIONE CONSTANT CHAR(1) := 'S';
ARTICOLO CONSTANT CHAR(1) := 'A';
RUBRICA CONSTANT CHAR(1) := 'R';

-- Sotto l'articolo del CDS CAP Cat 1 Spese CNR
-- Ci sono aree e CDS stesso
SOTTOARTICOLO CONSTANT CHAR(1) := 'E';

-- ATTENZIONE - Si assume che il titolo di spesa CNR sia di 2 caratteri (obbligatoriamente)

CATEGORIA1_SPESE_CNR CONSTANT VARCHAR2(50) := '1';
CATEGORIA2_SPESE_CNR CONSTANT VARCHAR2(50) := '2';
PARTE1 CONSTANT VARCHAR2(50) := '1';
PARTE2 CONSTANT VARCHAR2(50) := '2';

-- Codice natura 5
NATURA_5 CONSTANT VARCHAR2(1) := '5';

-- Functions e Procedures:

-- Copia l'elemento voce in ingresso nella tabella delle voci
--  aEV -> rowtype di elemento_voce completo dei dati significativi per la creazione della voce
--  aLivello -> livello nella tabella delle voci
--  isMastrino -> Y se si tratta di ultimo livello

 function creaVoceCorrispondente(aEV elemento_voce%rowtype, aLivello number, isMastrino char) return voce_f%rowtype;

-- Esplode l'anagrafica dei conti in voci di bilancio finanziario aggiungendo le parti automatiche (CDS SPESE PARTE I)
--  aEV -> rowtype di elemento_voce completo dei dati significativi per la creazione della voce

 procedure creaEsplVoci(aEV IN elemento_voce%rowtype);

-- Elimina in anagrafica dei conti l'esplosione in voci di bilancio finanziario eliminando le parti automatiche (CDS SPESE PARTE I)
--  aEV -> rowtype di elemento_voce completo dei dati significativi per la eliminazione della voce

 procedure eliminaEsplVoci(aEV IN elemento_voce%rowtype);

-- Aggiornamento della capitolazione finanziaria per cambiamenti della struttura organizzativa UO/CDS
--
-- pre-post-name: UO o CDS di tipo ENTE non gestita
-- pre: L'UO specificata è di tipo ENTE
-- post: La procedura termina senza segnalazioni
--
-- pre-post-name: CDS di tipo AREA
-- pre:  Il CDS specificato è di tipo AREA
-- post:
--  Viene aggiornata la descrizione del sottoarticolo corrispondente sotto il presidente dell'AREA
--
-- pre-post-name:   CDS non AREA e non SAC: alimento il capitolo di spesa del CNR per Categoria I
-- pre:  Il CDS specificato non è di tipo AREA o SAC
-- post:
--  Ciclo su categorie 1 del PDC SPESA CNR (codici 1.XX.1 dove XX è il titolo)
--    Per ogni categoria il sistema cerca di inserire il livello sezione = funzione (1-6)
--     Ciclo sulle funzioni (codici 1.XX.1.YY con YY = 01-06)
--      Per ogni sezione il sistema cerca un preesistente capitolo (codice proprio = codice CDS) sotto la sezione
--      Se il capitolo non esiste viene creato, altrimenti ne viene modificata la descrizione con quella del CDS
--      Per ogni capitolo il sistema cerca di inserire il livello articolo=natura (1-5)
--      Ciclo sulle nature (codice 1.XX.1.YY.CDS.NN dove N = 1-5)
--        Per ogni natura diversa dalla natura 5 viene aggiunto il sottoarticolo con codice proprio = CDS
--        Se il sottoarticolo già esiste viene aggiornata la descrizione
--
-- pre-post-name:   CDS di tipo SAC: alimento il capitolo di spesa del CNR per Categoria II
-- pre:  Il CDS specificato è di tipo SAC
-- post:
--      Ciclo sugli articoli di categoria 2 (codice 1.XX.2.YY.TIN.NN dove N = 1-4)
--      Per ogni articolo il sistema cerca di creare il sottoarticolo con codice proprio = CDS
--       Se il nodo già esiste ne viene aggiornata la descrizione
--
-- pre-post-name:   UO qualsiasi: alimento l'articolo di entrata CNR
-- pre:  Viene specificata una UO
-- post:
--      Ciclo sui capitoli di entrata CNR
--      Per ogni capitolo il sistema cerca di creare l'articolo con codice proprio = CODICE UO
--       Se il nodo già esiste, ne viene aggiornata la descrizione
--
-- pre-post-name:   Spese CDS: viene aggiunta l'UO rubrica come livello RUBRICA
-- pre:  Viene specificata una UO di tipo rubrica
-- post:
--  Ciclo sui titoli di spesa parte 1 CDS (codici 1.XX dove XX è il titolo)
--     Ciclo sulle funzioni (codici 1.XX.1.YY con YY = 01-06) e voci del piano esistenti in associazione voce - funzione - tipo uo
--      Per ogni combinazione valida il sistema cerca di inserire la funzione come livello sezione sotto il titolo
--      Sotto la sezione viene aggiunto il livello RUBRICA con codice proprio = ccodice UO
--        Se tale nodo dell'albero già esiste, ne viene aggiornata la descrizione
--      Sotto la rubrica vengono aggiunte le voci del piano come livelli CAPITOLO
--       Se tali nodi già esistono, ne viene aggiornata la descrizione
--      Se l'UO è di tipo SAC sotto il capitolo viene aggiunto ogni CDR valido di quell'UO come ARTICOLO
--
-- Parametri:
--  aEs -> esercizio di scrivania
--  aCdUO -> codice unita organizzativa
--  aUser -> codice utente che effettua la modifica: se specificato viene utilizzato come utcr e utuv  mentre come duva e dacr viene utilizzato sysdate. Se null vengono considerati duva, dacr, utcr e utuv dell'UO aCdUO

 procedure creaEsplVociUO(aEs number, aCdUO varchar2, aUser varchar2);

-- Esplode come sottoarticolo del cds-capitolo parte spese CNR presidente di area, l'area di ricerca di codice
-- aCdArea.
-- Metodo non invocabile via TRIGGERS su UNITA_ORGANIZZATIVA perche rilegge la tabella UNITA_ORGANIZZATIVA
-- Nel caso esista gia una precedente esplosione sotto il cds-capitolo di un precedente diverso presidente di area,
-- l'esplosione originale viene eliminata e creata quella nuova
-- Nel caso non risultino presidenti dell'area in processo, viene eliminata una eventuale esplosione
-- precedente dell'area
--
-- pre-post-name: Il CDS non è valido  nell'esercizio specificato o non è di tipo AREA
-- pre: Il CDS specificato non è valido nell'esercizio specificato o non è di tipo AREA
-- post: Viene sollevata un'eccezione
--
-- pre-post-name: Il CDS presidente dell'AREA specificata non esiste (più)
-- pre:  Non esiste (più) un CDS che risulti presidente dell'AREA specificata
-- post:
--  Viene eliminata (dovunque si trovi nel PDC spese CDS) la sottoarticolazione corrispondente all'AREA in processo
--
--
-- pre-post-name:   CDS non AREA e non SAC: alimento il capitolo di spesa del CNR per Categoria I
-- pre:  Il CDS specificato non è di tipo AREA o SAC
-- post:
--  Ciclo sui capitoli di categorie 1 del PDC SPESA CNR (codici 1.XX.1.YY.CDS)
--    Ciclo sulle nature (codice 1.XX.1.YY.CDS.NN dove N = 1-5)
--    Per ogni natura aggiungo l'area come sottoarticolo (codice proprio = codice CDS AREA)
--      Se il nodo già esiste, ne viene aggiornata la descrizione
--
-- Parametri
--   aEsercizio -> esercizio
--   aCdArea -> aCodice CDS area di ricerca
--   aUser -> utente che effettua la modifica

 procedure creaEsplSottArtArea(aEsercizio number, aCdArea varchar2, aUser varchar2);

-- Aggiornamento della capitolazione finanziaria per cambiamenti della struttura organizzativa - CDR
--
-- pre-post-name: UO di appartenenza del CDR di tipo diverso da SAC non gestita
-- pre: L'UO di appartenenza del CDR è di tipo diverso da SAC
-- post: La procedura termina senza segnalazioni
--
-- pre-post-name:   Esplosione CDR
-- pre:  Il CDR specificato è valido nell'esercizio specificato
-- post:
--  Ciclo su titoli di spesa CDS
--   Ciclo su sezioni di spesa CDS
--    Ciclo su rubriche di spesa CDS
--     Ciclo su capitoli di spesa CDS
--        Per ogni capitolo viene aggiunto l'articolo con codice proprio = codice CDR
--         Se il nodo già esiste, ne viene aggiornata la descrizione
--
-- Parametri:
--  aEs -> esercizio di scrivania
--  aCdCdr -> codice centro di responsabilità
--  aUser -> codice utente che effettua la modifica: se null vengono considerati utcr e utuv
--        mentre come duva e dacr viene utilizzato sysdate. Se null vengono considerati duva, dacr, utcr e utuv del CDR aCdCDR

 procedure creaEsplVociCDR(aEs number, aCdCDR varchar2, aUser varchar2);


-- Esplosione della struttura organizzativa nel nuovo anno
-- (Funzione di aggiornamento di VOCE_F per la parte strutturale legata a CDS/UO e CDR validi nell'esercizio in definizione)
-- Funzione NON invocabile da trigger AI su ESERCIZIO perche utilizza le viste V_XXXX_VALIDX sulla struttura organizzativa
--
-- pre-post-name: Esercizio non definito per il CDS in processo
-- pre: L'esercizio non è definito per la coppia aEs/aCdCds
-- post: La procedura termina senza segnalazioni
--
-- pre-post-name: UO o CDS di tipo ENTE non gestita
-- pre: L'UO specificata è di tipo ENTE
-- post: La procedura termina senza segnalazioni
--
-- pre-post-name: Esplosione CDS/UO/CDR
-- pre: Nessuna delle precondizioni precedenti è verificata
-- post:
--  Viene invocato creaEsplVociUO per il CDS
--  Ciclo sulle UO valide del CDS e invocazione di creaEsplVociUO per tali UO
--  Per ogni UO
--   Ciclo sui CDR validi dell'UO e invocazione di creaEsplVociCDR per tali CDR
--
-- Parametri
--  aEs -> codice esercizio
--  aCdCDS -> codice CDS
--  aUser -> codice utente che effettua la modifica: se null vengono considerati utcr e utuv
--       mentre come duva e dacr viene utilizzato sysdate. Se null vengono considerati duva, dacr, utcr e utuv del CDR aCdCDR

 procedure creaEsplVociEsercizio(aEs number, aCdCDS varchar2, aUser varchar2);

 -- Esplode in voci l'aggiunta di una nuova associazione tra elemento_voce/funzione/tipo_cds
 -- aASSEVFUNZTIPOCDS -> associazione in fase di inserimento

 procedure creaEsplIncroci(aASSEVFUNZTIPOCDS IN ass_ev_funz_tipocds%rowtype);


 -- Elimina l'esplosione in voci per l'eliminazione di una associazione tra elemento_voce/funzione/tipo_cds

 -- Pre: Esiste in almeno un piano di gestione nell.esercizio specificato dall.associazione un dettaglio per cui valgono le seguenti condizioni:
 -- 1. CDR del dettaglio appartenente ad UO appartenete a CDS del tipo = tipo CDS specificato nell.associazione
 -- 2. linea di attività con funzione = a quella presente nell.associazione
 -- 3. titolo-capitolo di spesa = quello presente nell.associazione
 --
 -- Post: Viene segnalato all'utente che un incrocio non è eliminabile perchè utilizzato

 -- aASSEVFUNZTIPOCDS -> associazione in fase di eliminazione

 procedure eliminaEsplIncroci(aASSEVFUNZTIPOCDS IN ass_ev_funz_tipocds%rowtype);

 -- Legge una riga per chiave primaria da VOCE_F

 procedure leggiVoce(aV IN OUT voce_f%rowtype);

 -- Aggiorna la riga in VOCE_F

 procedure aggiornaVoce(aVoce IN voce_f%rowtype);

-- Inserisce una riga nella tabella delle voci

 procedure insertVocePdc(aVoce voce_f%rowtype);

-- Restituisce la descrizione di sottoarticolo CDS proprio

function descSottoArtCDSProprio(aCodiceSA varchar2, aArticolo voce_f%rowtype) return varchar2;

-- Restituisce la descrizione di sottoarticolo CDS area

function descSottoArtCDSArea(aCodiceSA varchar2, aArticolo voce_f%rowtype) return varchar2;

end;
