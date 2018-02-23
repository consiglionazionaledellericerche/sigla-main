--------------------------------------------------------
--  DDL for Package CNRCTB400
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRCTB400" AS
-- =================================================================================================
--
-- CNRCTB400 - Package per la gestione dell'inventario
--
-- Date: 19/11/2004
-- Version: 3.5
--
-- Dependency: IBMERR 001 - IBMUTL001/200/205/210
--
----------------------------------------------------------------------------------------------------
-- History:
--
-- Date: 12/04/2002
-- Version: 1.0
-- Creazione
--
-- Date: 24/04/2002
-- Version: 1.1
-- Aggiunta della procedura, updAssTipoAmmCatGruppo, che provvede al caricamento
-- della tabella Associativa tra tipo ammortamento e categoria gruppo inventario
--
-- Date: 02/05/2002
-- Version: 1.2
-- Inserimeto data di registrazione come parametro input di updScaricoInventarioBeni
-- e gestione del nuovo campo  DT_VALIDITA_VARIAZIONE sulla tabella INVENTARIO_BENI
--
-- Date: 02/05/2002
-- Version: 1.3
-- Inserimeto cancellazione dalla tabella  ASS_TIPO_AMM_CAT_GRUP_INV_APG.
--
-- Date: 03/05/2002
-- Version: 1.4
-- Gestione della cancellazione e dell''aggiornamento delle associazioni
--
-- Date: 06/05/2002
-- Version: 1.5
-- Gestione dell'operazione di Riassocia. Aggiunta dei parametri relativi al Tipo Ammortamento destinatario
-- quali cd_tipo_riassociato, fl_ordinario_riassociato, fl_anticipato_riassociato, fl_altro_riassociato.
-- Aggiunto, inoltre, il flag (fl_riassociato), che indica alla procedura se effettuare le operazioni per
-- l'operazione di riassocia.
--
-- Date: 28/05/2002
-- Version: 1.6
-- Fix per la procedura updAssTipoAmmCatGruppo, relativa all'operazione di riassociazione
-- dei gruppi di un tipo ammortamneto.
-- Inserimento della procedura di gestione dell'ammortamento dei beni.
--
-- Date: 31/05/2002
-- Version: 1.7
-- Gestione del nuovo campo STATO_COGE sulla tabella BUONO_CARICO_SCARICO_DETT
-- Lo STATO_COGE viene valorizzato, come di seguito descritto :
-- 'N' per tutti i dettagli relativi a buoni di SCARICO effettuati in modo diretto
-- (non generati da fattura attiva), per cui, il bene risulta, scaricato totalmente.
-- 'X' in tutti gli altri casi
--
-- Date: 05/07/2002
-- Version: 1.8
-- Introduzione "update" del valore alienazione nella tabella inventario beni.
--
-- Date: 18/07/2002
-- Version: 1.9
-- Aggiornamento documentazione
--
-- Date: 20/09/2002
-- Version: 1.10
-- Correzione nell'inserimento dei dati nella tabella ASS_INV_BENE_FATTURA.
--
-- Date: 24/09/2002
-- Version: 1.11
-- Correzione nella procedura updScaricoInventarioBeni. E' stata corretta la creazione dei Dettagli
--  del Buono di Scarico: nel caso in cui, per un Buono di Scarico da Fattura, uno stesso bene sia stato
--	indicato per più righe di Fattura, viene creata una sola riga di Dettaglio per il bene. E' stata corretta
--	anche l'operazione di UPDATE su INVENTARIO_BENI: nello stesso caso sopra citato, il VALORE_ALIENAZIONE
--	sarà la somma dei VALORE_ALIENAZIONE, indicati per il bene nelle diverse righe di Fattura.
--
-- Date: 31/07/2003
-- Version: 1.12
-- Revisione struttura pkg
--
-- Date: 08/08/2003
-- Version: 1.13
-- Revisione completa ammortamento per inserimento in bacth di chiusura
--
-- Date: 11/09/2003
-- Version: 1.14
-- Aggiornamento documentazione ammortamento
--
-- Date: 16/09/2003
-- Version: 1.15
-- Modifica ammortamento per gestione tipo ammortamento secondo specifica def. del 8-8-2003
--
-- Date: 02/12/2003
-- Version: 1.16
-- Aggiunta
--
-- Date: 03/12/2003
-- Version: 1.17
-- Inserita controllo del campo ti_ammortamento_bene della vista v_ammortamento_bene
--
-- Date: 24/06/2004
-- Version: 2.0
-- Inserita gestione per la generazione del buono di carico per aumento di valore da fattura passiva
--
-- Date: 08/07/2004
-- Version: 2.1
-- Aggiunto parametro relativo al PgFatturaPassiva nella procedura updCaricoBeniAumentoValFtP.
--
-- Date: 30/07/2004
-- Version: 2.2
-- Ristrutturazione procedura per gestione del trasferimento, create le routine di insert per
-- BUONO_CARICO_SCARICO, BUONO_CARICO_SCARICO_DETT, ASS_INV_BENE_FATTURA
--
-- Date: 07/09/2004
-- Version: 2.3
--
-- Costruzione interfaccia per procedura di trasferimento beni intra ed extra inventario
--
-- Date: 11/10/2004
-- Version: 2.4
--
-- Rilascio richiesta CINECA n. 841. Revisione per nuova gestione ammortamento beni che tenga conto
-- di quanto movimentato nell'esercizio successivo. Allineamento con correttiva
--
-- Date: 15/10/2004
-- Version: 2.5
--
-- Aggiunti i parametri di cds e UO per recupero del default di ubicazione
--
-- Date: 20/10/2004
-- Version: 2.6
--
-- Fix errori interni in trasferimento beni. Inserite pre-post conditions.
--
-- Date: 21/10/2004
-- Version: 2.7
--
-- Richiesta CINECA n. 840. Aggiornamento del campo imponibile_ammortamento e compilazione del messaggio
-- di aggiornamento con dettaglio dei beni per i quali questo aggiornamento non è stato possibile.
-- Procedure: updScaricoInventarioBeni, updCaricoBeniAumentoValFtPas
-- messaggio: attenzione, per questo bene non verrà modificato automaticamente l'imponibile ammortamento
-- in quanto non risulta allineato al suo valore assestato, procedere manualmente.
--
-- Date: 21/10/2004
-- Version: 2.8
--
-- Inserito controllo che il bene da trasferire non risulti totalmente scaricato o con assestato = 0.
-- Aggiunta valorizzazione del campo INVENTARIO_BENI.dt_validita_variazione.
-- Corretta valorizzazione del campo INVENTARIO_BENI.imponibile_ammortamento.
--
-- Date: 25/10/2004
-- Version: 2.9
--
-- Fix errori interni:
-- 1) Controllo NULL o 0 per definire trasferimento intra inventario
-- 2) Inserita valorizzazione pg_ver_rec in aggiornamento NUMERATORE_BUONO_C_S
-- 3) Inserita distinct in recupero beni da INVENTARIO_BENI_APG.
-- 4) Non cancello tabella INVENTARIO_BENI_APG in caso di errori
--
-- Date: 05/11/2004
-- Version: 3.0
--
-- Fix errori interni:
-- 1) Cancello tabella INVENTARIO_BENI_APG in caso di errori
-- 2) Inserite routine:
--    - getInventarioBeni con parametro id_bene_origine (gestione migrazione)
--    - checkEsisteBene con parametro id_bene_origine
--    - getPgInventario
--    - getCategoriaGruppo
--
-- Date: 09/11/2004
-- Version: 3.1
--
-- Esposta in interfaccia le seguenti routine:
-- - getNrInventarioIniziale
-- - insInventarioBeni
--
-- Date: 16/11/2004
-- Version: 3.2
--
-- Errore, in trasferimento beni stessa UO, non riporta il dettaglio INVENTARIO_UTILIZZATORI_LA
-- Inserito controllo inventario aperto
--
-- Date: 16/11/2004
-- Version: 3.3
--
-- Corretta gestione in riporto INVENTARIO_UTILIZZATORI_LA per trasferimento stessa UO
-- La regola generale è che solo i beni principali hanno questa valorizzazione; gli accessori ereditano
-- dal padre. Se il bene diventa accessorio non si scrive nulla, se il bene diventa principale (era accessorio)
-- si riportano le informazioni presenti sul padre origine.
--
-- Date: 17/11/2004
-- Version: 3.4
--
-- Memorizzazione valore del bene in ASS_TRASFERIMENTO_BENI_INV
--
-- Date: 19/11/2004
-- Version: 3.5
--
-- Fix errore in ammortamento beni. Nel caso in cui diverse UO di un CDS fanno riferimento allo stesso
-- inventario non era fatto il distinct su quest'ultimo.
--
-- =================================================================================================
--
-- Constants:
--

   BUONO_SCARICO CONSTANT CHAR(1) :='S';
   BUONO_CARICO CONSTANT CHAR(1) :='C';

   -- Costante tipo log per batch

   LOG_TIPO_TRASFBENI CONSTANT VARCHAR2(20) :='TRASF_BENI00';
   LOG_TIPO_AMMBENI   Constant VARCHAR2(20) :='AMM_BENI00';
   LOG_TIPO_ANNAMMBENI   Constant VARCHAR2(20) :='ANN_AMM_BENI00';
-- Definizione tabella PL/SQL di appoggio errori per trasferimento beni

   TYPE dsErroriRec IS RECORD
       (
        tStrIdBeneOrigine VARCHAR2(30),
        tStrIdBeneDest VARCHAR2(30),
        tStringaMsg VARCHAR2(4000),
        tStringaTrc VARCHAR2(4000),
        tStringaNote VARCHAR2(4000)
       );

   TYPE dsErroriTab IS TABLE OF dsErroriRec INDEX BY BINARY_INTEGER;

-- Definizione tabella PL/SQL di appoggio record per trasferimento beni

   TYPE beniDaTrasfRec IS RECORD
       (
        tPgInventario NUMBER(10),
        tNrInventario NUMBER(10),
        tProgressivo NUMBER(10),
        tFlTrasfComePrincipale CHAR(1),
        tPgInventarioPrincipale NUMBER(10),
        tNrInventarioPrincipale NUMBER(10),
        tProgressivoPrincipale NUMBER(10),
        tCategoriaNew VARCHAR2(10)
       );

   TYPE beniDaTrasfTab IS TABLE OF beniDaTrasfRec INDEX BY BINARY_INTEGER;


-- Variabili globali

   errori_tab dsErroriTab;
   beniDaTrasf_tab beniDaTrasfTab;
   TYPE GenericCurTyp IS REF CURSOR;

--
-- Functions e Procedures:
--
--
-- Recupero record di INVENTARIO_BENI
--
   FUNCTION getInventarioBeni
      (inPgInventario NUMBER,
       inNrInventario NUMBER,
       inProgressivo NUMBER,
       eseguiLock CHAR
      ) RETURN INVENTARIO_BENI%ROWTYPE;

   FUNCTION getInventarioBeni
      (inIdBeneOrigine VARCHAR2,
       eseguiLock CHAR
      ) RETURN INVENTARIO_BENI%ROWTYPE;

-- Inserisce un record in INVENTARIO_BENI

   PROCEDURE insInventarioBeni
      (aRecInventarioBeni INVENTARIO_BENI%Rowtype);

--
-- Torna Y/N se esiste o meno il bene in INVENTARIO_BENI a partire da id_bene_origine
--

   FUNCTION checkEsisteBene
      (inIdBeneOrigine VARCHAR2
      ) RETURN CHAR;

--
-- Ritorna il progressivo identificativo dell'inventario
--

   FUNCTION getPgInventario
      (inCdCds VARCHAR2,
       inCdUo VARCHAR2
      ) RETURN NUMBER;

--
-- Ritorna un record di CATEGORIA_GRUPPO_INVENT
--

   FUNCTION getCategoriaGruppoInvent
      (inCdCategoriaGruppo VARCHAR2
      ) RETURN CATEGORIA_GRUPPO_INVENT%ROWTYPE;

--
-- Lettura ubicazione di default
--

   FUNCTION getUbicazioneDefault
      (inCdCds VARCHAR2,
       inUo VARCHAR2
      ) RETURN UBICAZIONE_BENE%ROWTYPE;

   FUNCTION getCdUbicazioneDefault
      (inCdCds VARCHAR2,
       inUo VARCHAR2
      ) RETURN VARCHAR2;

-- Lettura del valore iniziale di numerazione beni per un dato inventario

   FUNCTION getNrInventarioIniziale
      (inPgInventario NUMBER) RETURN NUMBER;

-- Controllo stato apertura chiusura dell'inventario

   FUNCTION checkStatoInventApCh
      (inPgInventario NUMBER,
       inEsercizio NUMBER,
       inData DATE) RETURN VARCHAR2;

--
-- 	L'operation crea un documento di scarico di inventario, con i relativi dettagli e le eventuali associazioni con
--      le fatture di vendita.
--
-- 	Pre-post name: Scarico dell'inventario in modo diretto.
-- 	Pre:  L'inventario è scaricato in modo diretto
-- 	Post: Viene generata prima la testata del documento di scarico quindi una riga di dettaglio per ogni bene scaricato.
--
-- 	Pre-post name: Scarico dell'inventario da fattura Attive.
-- 	Pre:  L'inventario è scaricato tramite la registrazione di una  fattura attiva
-- 	Post: Viene generata prima la testata del documento di scarico quindi una riga di dettaglio per ogni bene scaricato.
--
-- 	Pre-post name: Scarico dell'inventario da fattura Attive o in modo diretto.
-- 	Pre:  L'inventario è scaricato.
-- 	Post: Viene aggiornato lo stato di inventario di tutti i beni coinvolti, nel seguente modo:
--            VARIAZIONE_MENO         =	VARIAZIONE_MENO + valore scaricato
--            VALORE_ALIENAZIONE      =	nuovo valore alienazione
--            FL_TOTALMENTE_SCARICATO =	Vale Y se avviene la dismissione altrimenti N
--            DT_VALIDITA_VARIAZIONE  =	data in cui è avvenuta la variazione
--
   PROCEDURE updScaricoInventarioBeni
      (localTransId VARCHAR2,
       aPgInventario NUMBER,
       aEsercizio NUMBER,
       aPgBuonoCaricoScarico NUMBER,
       aDsBuonoCaricoScarico VARCHAR2,
       aCdTipoCaricoScarico VARCHAR2,
       aUtente VARCHAR2,
       isDaFattura CHAR DEFAULT 'N',
       Ti_fattura CHAR,
       aDtRegistrazione DATE,
       aMessaggio IN OUT VARCHAR2);

-- 	L'operation crea un documento di carico di inventario per aumento valore da fattura, con i relativi dettagli e
--      le eventuali associazioni con le fatture di acquito.
--
-- 	Pre-post name: Carico dell'inventario per aumento valore da fattura passiva.
-- 	Pre:  L'inventario viene caricato tramite la registrazione di una  fattura passiva per aumento valore
-- 	Post: Viene generata prima la testata del documento di carico e quindi una riga di dettaglio per ogni bene
--            variato a valore.
--
-- 	Pre-post name: Aggiornamento del bene
-- 	Pre:  L'inventario viene scaricato.
-- 	Post: Viene aggiornato lo stato di inventario di tutti i beni coinvolti, nel seguente modo :
--            VARIAZIONE_PIU         =	VARIAZIONE_PIU + valore scaricato
--            DT_VALIDITA_VARIAZIONE =  data in cui è avvenuta la variazione

   PROCEDURE updCaricoBeniAumentoValFtPas
      (localTransId VARCHAR2,
       aPgInventario NUMBER,
       aEsercizio NUMBER,
       aPgBuonoCaricoScarico NUMBER,
       aDsBuonoCaricoScarico VARCHAR2,
       aCdTipoCaricoScarico VARCHAR2,
       aPgFattura NUMBER,
       aUtente VARCHAR2,
       aDtRegistrazione DATE,
       aMessaggio IN OUT VARCHAR2);

-- 	L'operation modifica l'associazione tra la categoria di un bene e il tipo di ammortamento.
--
-- 	Pre-post name: Nuova associazione di categoria bene con un tipo di ammortamento.
-- 	Pre:  Esistono associazioni tra la categoria gruppo e un tipo di ammortamento.
-- 	Post: Tutte le vecchie associazioni relative alla categoria gruppo in esame vengono cancellate logicamente, e
--            sono generate le nuove associazioni con il nuovo tipo di ammortamento
--
-- 	Pre-post name: Riassociazione di un tipo di ammortamento da una categoria bene (old) ad un'altra (new)
-- 	Pre:  Esistono associazioni tra il tipo di  ammortamento e la categoria bene (old).
-- 	Post: Tutte le vecchie associazioni relative alla categoria gruppo (old) vengono cancellate logicamente, e
--            sono generate le nuove associazioni tra la categoria gruppo (new) e il tipo di ammortamento.

   PROCEDURE updAssTipoAmmCatGruppo
      (localTransId VARCHAR2,
       cdTipoAmmortamento VARCHAR2,
       flOrdinario CHAR default 'N',
       flAnticipato CHAR default 'N',
       flAltro CHAR default 'N',
       flRiassociato CHAR default 'N',
       cdTipoRiassociato VARCHAR2,
       flOrdinarioRiassociato CHAR default 'N',
       flAnticipatoRiassociato CHAR default 'N',
       flAltroRiassociato CHAR default 'N',
       aEsercizio NUMBER,
       aUtente VARCHAR2);

-- PROCEDURE DI AMMORTAMENTO FINANZIARIO
--
-- Creazione dell'ammortamento
--
-- Pre-post name: Controllo se il bene risulta associato ad un tipo ammortamento
-- Pre:           Il bene non è associato ad un tipo di ammortamento
-- Post:          Viene sollevata un'eccezione
--
-- Pre-post name: Controllo se il bene risulta già ammortizzato nell'esercio in esame
-- Pre:           Esiste almeno un bene risulta già ammortizzato nell'anno specificato
-- Post:          Viene sollevata un'eccezione
--
-- Pre-post name: Stabilisco l'aliquota di ammortamento da applicare al bene (primo o altri anni)
-- Pre:           L'aliquota non viene trovata o vale zero
-- Post:          Viene sollevata un'eccezione
--
-- Pre-post name: Ammortamento finanziario
-- Pre:           Nessuna delle precondizioni precedenti è verificata
-- Post:   Vengono letti tutti i beni da ammortizzare ciclando su ASS_INVENTARIO_UO per il recuepero del/i pg_inventario
--         associati e quindi sulla vista V_AMMORTAMENTO_BENI. Sono letti solo beni con fl_ammortamento = Y per cui
--         risulta esistere un associazione valida con un tipo di ammortamento.
--
--         Controllo se esiste un tipo di ammortamento associato al gruppo categoria. Se tale tipo non è definito per
--         il bene si verifica che lo sia e univocamente per il suo GRUPPO di appartenenza.
--            Se il bene ha un tipo ammortamento configurato si legge quello per procedere al calcolo.
--            Se il bene non ce l'ha (maggioranza dei casi) allora si va a leggere il tipo di ammortamento associato
--               alla categoria.gruppo di appartenenza del bene:
--               Se la categoria.gruppo ha un solo tipo ammortamento associato allora si procede ad utilizzare quel tipo.
--               Se la categoria.gruppo è associata a più di un tipo ammortamento allora il sistema segnala l'impossibilità
--                  di procedere.
--
--         Stabilisco se devo usare aliquota del primo anno o di anni successivi.
--            Se l'aliquota non viene trovata o questa è zero vedi Pre-post name: Stabilisco l'aliquota di ammortamento
--               da applicare al bene (primo o altri anni)
--
--         Normalizzo il record per escludere quanto movimentato in anni successivi.
--         I beni totalizzano saldi in modo indipendente dagli esercizi mentre l'ammortamento deve operare sul
--         valore definito al 31/12 dell'esercizio di riferimento.
--         Si eseguono i seguenti passi:
--            Se il bene presenta il flag totalmente scaricato = Y ed è stato movimentato in esercizi successivi si
--               porta il flag = N
--            Si assestano i saldi (imponibile ammortamento) togliendo tutto quanto è stato movimentato da buoni
--            di carico e scarico in esercizi successivi. Questa funzione è svolta solo per i beni per cui risulta
--            verificata l'uguaglianza valore_iniziale + variazioni più - variazioni meno = imponibile ammortamento.
--
--         Si registra nel log un warning se il valore ammortizzato è maggiore dell'imponibile ammortamento. La
--         segnalazione non è bloccante.
--         Si registra nel log un warning se il valore assestato del bene vale zero e il flag totalmente scaricato = N
--         e imponibile ammortamento > 0. La segnalazione non è bloccante.
--
--         Si processano i soli record che hanno:
--            imponibile_ammortamento > 0
--            fl_totalmente_scaricato = 'N'
--            valore_ammortizzato < imponibile_ammortamento
--
--         Si calcola il valore della rata di ammortamento
--            Se la rata di ammortamento calcolata con la percentuale stabilita è troppo alta in quanto risulta
--               valore ammortizzato + rata_ammortamento > imponibile ammortamento allora
--               il bene viene ammortizzato di un valore pari allo scarto IMPONIBILE_AMMORTAMENTO - VALORE_AMMORTIZZATO.
--         Per tutti i casi in cui il bene deve subire una rata di ammortamento si aggiornare:
--         VALORE_AMMORTIZZATO del bene in esame
--         Si inserire un movimento di ammortamento relativo alla rata.
--
-- Parametri:
--   aPgExec -> Indice log registry: determinato e passato dall'eseterno
--   aEs     -> Esercizio in cui viene effettuato l'ammortamento
--   aCdCds  -> Codice del cds
--   aUser   -> Utente che effettua la variazione
--   aTSNow  -> Timestamp modifica
--
   PROCEDURE ammortamentoBeniInv
      (aPgExec NUMBER,
       aEs NUMBER,
       aCdCds VARCHAR2,
       aUser VARCHAR2,
       aTSNow DATE );

-- Annullamento dell'ammortamento
--
-- Pre-post name: Annullamento ammortamento finanziario
-- Pre:           Richiesta di annullamento dell'ammortamento finanziario per il bene
-- Post:          Per ogni bene ammortizzato sul CDS specificato:
--                Viene aggiornato il valore attuale del bene riportandolo al valore precedente l'ammortamento.
--                La rata di ammortamento viene eliminata dalla tabella delle rate di ammortamento per
--                l'anno specificato.
--
-- Parametri:
--   aPgExec -> Indice log registry: determinato e passato dall'eseterno
--   aEs     -> Esercizio in cui viene effettuato l'ammortamento
--   aCdCds  -> Codice del cds
--   aUser   -> Utente che effettua la variazione
--   aTSNow  -> Timestamp modifica

   PROCEDURE annullaAmmortBeniInv
      (aPgExec NUMBER,
       aEs NUMBER,
       aCdCds VARCHAR2,
       aUser VARCHAR2,
       aTSNow DATE);

-- Inserisce un record nella tabella AMMORTAMENTO_BENE_INV

   PROCEDURE ins_AMMORTAMENTO_BENE_INV
      (aDest AMMORTAMENTO_BENE_INV%rowtype);

-- Aggiornamento del bene per ammortamento (modifica del valore ammortizzato)

   PROCEDURE updBene
      (aPgInv NUMBER,
       aNrInv NUMBER,
       aProgressivo NUMBER,
       aDelta NUMBER,
       aTSNow DATE,
       aUser VARCHAR2);

-- Verifica se il bene è già stato ammortizzato nell'anno specificato

   FUNCTION chkBeneAmmortato
      (aRecVAmmortamentoBeni V_AMMORTAMENTO_BENI%ROWTYPE,
       aEs NUMBER
      )RETURN BOOLEAN;

-- Verifica se esiste il tipo di ammortamento specificato nell'esercizio specificato

   PROCEDURE chkEsisteTipoAmm
      (aRecVAmmortamentoBeni IN OUT V_AMMORTAMENTO_BENI%ROWTYPE,
       aEs NUMBER);
PROCEDURE upgRecBenePerAmmortamento
   (
    aRecVAmmortamentoBeni IN OUT V_AMMORTAMENTO_BENI%ROWTYPE,
    aEs NUMBER
   );
-- Main trasferimento beni
--
-- L'operation crea il job di trasferimento beni all'interno dello stesso inventario o verso inventari diversi
--
-- Pre-post name: Creaazione job di trasferimento beni
-- Pre:           Nessuna
-- Post:          E' costruita la struttura di chiamata per il batch dinamico. Si normalizzano i seguenti
--                parametri in input:
--                1) aPgInventarioDest se è NULL è posto uguale a inPgInventarioOri (trasferimento intra inventario)
--                2) aFlTrasferisciTutto se è NULL è posto uguale a N, non si esegue il trasferimento totale
--
-- Parametri:
--   inLocalTransId       -> Identificativo della chiave di lettura sulla tabella INVENTARIO_BENI_APG utilizzata
--                           quando il trasferimento non è totale e leggo i beni da trasferire selezionati dall'utente
--   inPgInventarioOri    -> Identificativo dell'inventario origine del trasferimento
--   inPgInventarioDest   -> Identificativo dell'inventario destinazione del trasferimento
--   inEsercizio          -> Esercizio di riferimento al trasferimento (utilizzato per creazione dei buoni di carico/scarico)
--   inCdTipoCarico       -> Codice identificativo del tipo carico per trasferimento da utilizzarsi nel buono di carico
--   inCdTipoScarico      -> Codice identificativo del tipo scarico per trasferimento da utilizzarsi nel buono di scarico
--   inDsBuonoCarScar     -> Descrizione del buono di carico e scarico
--   inDtRegistrazione    -> Data di registrazione del buono di carico e scarico
--   inFlTrasferisciTutto -> Indicatore di trasferimento totale (Y) o no (N)
--   inUtente             -> Utente che esegue l'operazione
--   inCdsDest            -> Identificativo del Cds di destinazione per il recupero dell'ubicazione di default per il bene
--                           caricato in altro inventario
--   inUoDest             -> Identificativo della Uo di destinazione per il recupero dell'ubicazione di default per il bene
--                           caricato in altro inventario
--

   PROCEDURE trasferisciBeni
      (inLocalTransId VARCHAR2,
       inPgInventarioOri NUMBER,
       inPgInventarioDest NUMBER,
       inEsercizio NUMBER,
       inCdTipoCarico VARCHAR2,
       inCdTipoScarico VARCHAR2,
       inDsBuonoCarScar VARCHAR2,
       inDtRegistrazione DATE,
       inFlTrasferisciTutto VARCHAR2,
       inUtente VARCHAR2,
       inCdsDest VARCHAR2,
       inUoDest VARCHAR2);

-- Job trasferimento beni
--
-- L'operation esegue il trasferimento beni all'interno dello stesso inventario o verso inventari diversi
--
-- Pre-post name: Controllo esercizio esecuzione del trasferimento beni
-- Pre:           L'esercizio è minore del 2004
-- Post:          Viene sollevata un'eccezione
--
-- Pre-post name: Recupero dell'ubicazione di default sull'inventario di destinazione (trasferimento altro inventario)
-- Pre:           L'ubicazione non viene trovata
-- Post:          Viene sollevata un'eccezione
--
-- Pre-post name: Controllo cardinalità della matrice beni da trasferire (blocco omogeneo di bene principale e suoi
--                accessori o accessori stand alone
-- Pre:           La matrice è vuota
-- Post:          Viene sollevata un'eccezione
--
-- Pre-post name: Controllo stato del bene in trasferimento
-- Pre:           Il bene non deve risultare totalmente scaricato o con assestato = 0
-- Post:          Viene sollevata un'eccezione
--
-- Pre-post name: Controlli di consistenza della matrice beni da trasferire (trasferimento stessa UO)
-- Pre:           Esiste nella matrice un bene principale
--                   1) Tutti i beni devono presentare il flag trasferisci come bene principale = 'N'
--                   2) Tutti i beni devono diventare accessori dello stesso bene di destinazione e questo non può
--                      essere nullo.
--                Non esiste un bene principale
--                   1) Se l'accessorio non è da trasferire come principale deve esistere l'indicazione del bene
--                      di destinazione
-- Post:          Viene sollevata un'eccezione
--
-- Pre-post name: Lettura del progressivo di numerazione del buono di carico/scarico e sua generazione se insesistente
-- Pre:           Ci sono errori
-- Post:          Viene sollevata un'eccezione
--
-- Pre-post name: Creazione del buono di carico/scarico e sua generazione se insesistente
-- Pre:           Ci sono errori
-- Post:          Viene sollevata un'eccezione
--
-- Pre-post name: Trasferimento beni
-- Pre:           Nessuna delle precondizioni precedenti è verificata
-- Post:   Vengono letti tutti i beni da trasferire distinguendo a seconda che si tratti di trasferimento totale o parziale.
--            Se trasferimento totale si leggono i beni da INVENTARIO_BENI
--            Se trasferimento parziale si leggono i beni da INVENTARIO_BENI_APG
--         I record sono ordinati per pg_inventario, nr_inventario e progressivo.
--         I record sono processati a blocchi omogenei per bene principale con i suoi accessori o i soli accessori.
--            Se il numero inventario è diverso da quello memorizzato allora si esegue il trasferimento per il blocco.
--            Se ho già portato dei beni sulla matrice di appoggio e la prima occorrenza non è un bene principale,
--               anche se il numero inventario è uguale al precedente, si esegue il trasferimento per il blocco.
--            Si memorizza il record da processare sulla matrice di appoggio.
--            Alla fine del ciclo si processa l'ultimo blocco di beni da trasferire.
--         Elaborazione del blocco omogeneo di beni da trasferire
--            Si costruisce la struttura base della testata e del dettaglio dei buoni di carico e scarico.
--            Si memorizzano i valori dei buoni di carico e scarico generati. La testata è generata ogni 150 beni
--            Si cicla sulla matrice di appoggio dei beni da trasferire
--               Si eseguono i controlli di consistenza Pre-post name: Controlli di consistenza della matrice beni da
--                  trasferire (trasferimento stessa UO).
--               Si crea la testata del buono di scarico. E' generata solo se è la prima volta o se il numero delle
--                  righe di dettaglio è maggiore di 150. Si legge con lock il prossimo numeratore del buono di scarico
--                  creando lo stesso se non esiste per esercizio e pg_inventario.
--               Si crea il dettaglio del buono di scarico, settando se questo avrà impatto nella gestione coge (solo
--                  per trasferimenti altro inventario).
--               Si aggiorna il bene origine portandolo a totalmente scaricato.
--               Si crea la testata del buono di carico. E' generata solo se è la prima volta o se il numero delle
--                  righe di dettaglio è maggiore di 150. Si legge con lock il prossimo numeratore del buono di ccarico
--                  creando lo stesso se non esiste per esercizio e pg_inventario.
--               Si crea il dettaglio del buono di carico, settando se questo avrà impatto nella gestione coge (solo
--                  per trasferimenti altro inventario).
--               Si crea il nuovo bene (carico).
--                  Se deve essere creato un bene principale si cerca il numero massimo di nr_inventario presente per
--                     un dato pg_inventario. Se il valore tornato è zero allora si torna il numero iniziale a partire
--                     dalla tabella ID_INVENTARIO.
--               Si crea l'entrata in INVENTARIO_UTILIZZATORI_LA solo in caso di trasferimento stesso inventario.
--                  La regola generale è che solo i beni principali hanno questa valorizzazione; gli accessori ereditano
--                  dal padre.
--                  Se il bene diventa accessorio non si scrive nulla
--                  Se il bene diventa principale (era accessorio) si riportano le informazioni presenti sul padre origine.
--               Si crea l'associativa origine/destinazione per il bene trasferito.
--               Si cancella, se del caso, la tabella INVENTARIO_BENI_APG per trasferimenti non totali
--            Se ci sono errori questi sono portati fuori e viene riempita la matrice degli errori
--         In uscita, se la matrice errori è piena, si segnala la terminazione con successo con alcuni errori. Il
--         trasferimento è sempre processato per intero ed è quindi possibile che alcuni beni siano trasferiti mentre
--         per altri si genera errore.
--
-- Parametri:
--   job                  -> Identificativo del numero di job
--   pg_exec              -> Identificativo del progressivo di esecuzione per la gestione log
--   next_date            -> Data esecuzione batch
--   inLocalTransId       -> Identificativo della chiave di lettura sulla tabella INVENTARIO_BENI_APG utilizzata
--                           quando il trasferimento non è totale e leggo i beni da trasferire selezionati dall'utente
--   inPgInventarioOri    -> Identificativo dell'inventario origine del trasferimento
--   aPgInventarioDest    -> Identificativo dell'inventario destinazione del trasferimento
--   inEsercizio          -> Esercizio di riferimento al trasferimento (utilizzato per creazione dei buoni di carico/scarico)
--   inCdTipoCarico       -> Codice identificativo del tipo carico per trasferimento da utilizzarsi nel buono di carico
--   inCdTipoScarico      -> Codice identificativo del tipo scarico per trasferimento da utilizzarsi nel buono di scarico
--   inDsBuonoCarScar     -> Descrizione del buono di carico e scarico
--   inDtRegistrazione    -> Data di registrazione del buono di carico e scarico
--   aFlTrasferisciTutto  -> Indicatore di trasferimento totale (Y) o no (N)
--   inUtente             -> Utente che esegue l'operazione
--   inCdsDest            -> Identificativo del Cds di destinazione per il recupero dell'ubicazione di default per il bene
--                           caricato in altro inventario
--   inUoDest             -> Identificativo della Uo di destinazione per il recupero dell'ubicazione di default per il bene
--                           caricato in altro inventario
--
   PROCEDURE job_trasferisciBeni
      (job NUMBER,
       pg_exec NUMBER,
       next_date DATE,
       inLocalTransId VARCHAR2,
       inPgInventarioOri NUMBER,
       aPgInventarioDest NUMBER,
       inEsercizio NUMBER,
       inCdTipoCarico VARCHAR2,
       inCdTipoScarico VARCHAR2,
       inDsBuonoCarScar VARCHAR2,
       inDtRegistrazione DATE,
       aFlTrasferisciTutto VARCHAR2,
       inUtente VARCHAR2,
       inCdsDest VARCHAR2,
       inUoDest VARCHAR2);

Procedure cancella_variazioni
   (p_esercizio 	number,
    p_inventario 	number,
    n_inventario 	number,
    p_progressivo	number,
    p_buono		number,
    p_utente	 	varchar2,
    p_data 		date,
    segno		varchar2,
    effettuato         In Out NUMBER);

Procedure AmmortamentoBeni(
p_esercizio 	number,
p_cds		varchar2,
p_utente 	varchar2);

Procedure job_ammortamentoBeniInv
      (	job NUMBER,
      	pg_exec NUMBER,
   	next_date DATE,
   	aEs NUMBER,
   	aCdCds VARCHAR2,
   	aUser VARCHAR2);

-- 	L'operation crea un documento di carico di inventario per aumento valore da fattura, con i relativi dettagli e
--      le eventuali associazioni con le fatture di acquito.
--
-- 	Pre-post name: Carico dell'inventario per aumento valore da fattura passiva.
-- 	Pre:  L'inventario viene caricato tramite la registrazione di una  fattura passiva per aumento valore
-- 	Post: Viene generata prima la testata del documento di carico e quindi una riga di dettaglio per ogni bene
--            variato a valore.
--
-- 	Pre-post name: Aggiornamento del bene
-- 	Pre:  L'inventario viene scaricato.
-- 	Post: Viene aggiornato lo stato di inventario di tutti i beni coinvolti, nel seguente modo :
--            VARIAZIONE_PIU         =	VARIAZIONE_PIU + valore scaricato
--            DT_VALIDITA_VARIAZIONE =  data in cui è avvenuta la variazione
Procedure updCaricoBeniAumentoValDoc
      (localTransId VARCHAR2,
       aPgInventario NUMBER,
       aEsercizio NUMBER,
       aPgBuonoCaricoScarico NUMBER,
       aDsBuonoCaricoScarico VARCHAR2,
       aCdTipoCaricoScarico VARCHAR2,
       aPgDocumento NUMBER,
       aUtente VARCHAR2,
       aDtRegistrazione DATE,
       aMessaggio IN OUT VARCHAR2);
End;
