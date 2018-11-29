------------------------------------------------------
--  DDL for Package CNRCTB030
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRCTB030" as
--
-- CNRCTB030 - Package di gestione applicativa obbligazioni/impegni
--
-- Date: 12/07/2006
-- Version: 4.12
--
-- Package per la gestione applicativa dell'obbligazione
-- Funzioni di controllo copertura per registrazione obbligazione
-- Funzioni di generazione impegni automatici
--
-- Dependency: CNRCTB 001/015/020/016/018/020/035/054 IBMERR 001
--
-- History:
--
-- Date: 17/09/2001
-- Version: 1.0
-- Creazione
--
-- Date: 21/12/2001
-- Version: 1.1
-- Aggiunto parametro esercizio_scrivania in checkAssunzObblig
--
-- Date: 22/01/2002
-- Version: 1.2
-- Cambiata la semantica del package: contiene le procedure di controllo e gestione applicativa di obbligazioni/impegni
--
-- Date: 24/01/2002
-- Version: 1.3
-- Introdotto controllo copertura per assunzione obbligazioni
--
-- Date: 04/02/2002
-- Version: 1.4
-- Fix errori controllo di copertura
--
-- Date: 20/02/2002
-- Version: 1.5
-- Gestione su tabella obbligazione
--
-- Date: 21/02/2002
-- Version: 1.6
-- L'importo dell'impegno generato in automatico non può mai scendere sotto il saldo dei documenti amministrativi collegati
--
-- Date: 21/02/2002
-- Version: 1.7
-- Spaccato CNRCTB055 su CNRCTB054
--
-- Date: 21/02/2002
-- Version: 1.8
-- Aggiunto il metodo di aggiornamento dell'impegno capitolo richiamabile da applicazione
--
-- Date: 11/03/2002
-- Version: 1.9
-- Lettura della linea di attività di spesa ENTE da configurazione CNR per creazione impegno automatico CNR
--
-- Date: 25/03/2002
-- Version: 2.0
-- Gestione del terzo speciale CODICE_DIVERSI_IMPEGNI
--
-- Date: 27/03/2002
-- Version: 2.1
-- Check esistenza terzo per impegno CNR automatico in tabella TERZO su aggiornamento impegno CNR
--
-- Date: 23/04/2002
-- Version: 2.2
-- Ribaltato update obbligazione a partire dalla testata per modifica della logica del trigger di storico
--
-- Date: 09/05/2002
-- Version: 2.3
-- Introduzione mtore registrazione obbligazioni
--
-- Date: 10/05/2002
-- Version: 2.4
-- Registrazione obbligazioni su partita di giro
--
-- Date: 11/05/2002
-- Version: 2.5
-- Test motore pgiro
--
-- Date: 13/05/2002
-- Version: 2.6
-- Cambiata interfaccia pgiro
--
-- Date: 03/07/2002
-- Version: 2.7
-- Creazione obbligazione su di una sola scadenza
-- Fix generazione impegni su capitolo su CNR -> utuv e utcr settati erronemante a duva del saldo
--
-- Date: 17/07/2002
-- Version: 2.8
-- Troncamento della data di registrazione e scadenza in obbligazione e derivati
--
-- Date: 18/07/2002
-- Version: 2.9
-- Aggiornamento documentazione
--
-- Date: 21/07/2002
-- Version: 3.0
-- Fix aggiorna impegno capitolo con aggiornamento del saldo del capitolo
--
-- Date: 04/09/2002
-- Version: 3.1
-- Fix su pgiro aperta dalla spesa: errore in recupero del capitolo
--
-- Date: 19/09/2002
-- Version: 3.2
-- Aggiunti metodi per la modifica di obbligazioni con singola spaccatura analitica sotto la scadenza
--
-- Date: 26/09/2002
-- Version: 3.3
-- Aggiunta estrazione della massa spendibile
--
-- Date: 26/09/2002
-- Version: 3.4
-- Ulteriori controlli
--
-- Date: 26/09/2002
-- Version: 3.5
-- fix su check ass obb pluriennali
--
-- Date: 26/09/2002
-- Version: 3.6
-- riorganizzazione saldi accertamento
--
-- Date: 30/09/2002
-- Version: 3.7
-- Fix su check assunz. obblig. e getMassaSpendibile -> se fl_limite_ass_obblig = N non effettua controlli (per annuale e pluriennale)
--
-- Date: 01/10/2002
-- Version: 3.8
-- Fix su rate per controllo assunzione obbligazione a 100 nel caso non esista
-- percentuale per gli anni successivi al primo impostata sul CDS in processo
--
-- Date: 26/11/2002
-- Version: 3.9
-- Fix su utuv e duva saldo competenza CNR spese parte 1 per aggiornamento impegno capitolo automatico nel caso in
-- cui l'impegno esista già
--
-- Date: 25/02/2003
-- Version: 4.0
-- Gestione partita di giro tronca
--
-- Date: 06/03/2003
-- Version: 4.1
-- Aggiunto metodo di creazione/modifica impegno capitolo per variazione di bilancio
--
-- Date: 07/03/2003
-- Version: 4.2
-- Fix minori
--
-- Date: 12/06/2003
-- Version: 4.3
-- Fix a metodo adeguaObbSV
--
-- Date: 21/07/2003
-- Version: 4.4
-- Fix a metodo adeguaObbSV: non settava correttamente l'importo della testata di obbligazione
--
-- Date: 22/07/2003
-- Version: 4.5
-- Fix a metodo adeguaObbSV: lock testata obbligazione
--
-- Date: 10/10/2003
-- Version: 4.6
-- Nel calcolo della massa spendibile, aggiunta detrazione degli importi relativi ai madati di
--  regolarizzazione in riferimento al Cds di scrivania, (Rich. CNR n.651).
--
-- Date: 22/12/2003
-- Version: 4.7
-- Richiesta telefonica con Mingarelli da aprire in sito ??? per togliere il controllo
-- bloccante disponibilità di competenza su modifica obbligazione stipendi in liquidazione 13-esimo mese
--
-- Date: 07/01/2004
-- Version: 4.8
-- Controllo che se l'impegno su capitolo CNR è riportato, NON POSSO modificare tale impegno per variazione
-- di bilancio
--
-- Date: 29/01/2004
-- Version: 4.9
-- Rich. n. 734
-- Nel calcolo della massa spendibile, aggiunta tot mandati + es prec - tot reversali es prec
-- Modificato anche checkAssunzObblig per richiesta 651
--
-- Date: 11/02/2004
-- Version: 4.10
-- Fix su controllo della massa spendibile
--
-- Date: 16/04/2004
-- Version: 4.11
-- Errore n.815
--
-- Date: 12/07/2006
-- Version: 4.12
-- Gestione Impegni/Accertamenti Residui:
-- aggiornata la funzione per tener conto anche del campo Esercizio Originale Impegno/Accertamento
--
--
-- Date: 02/01/2007
-- Version: 4.13
-- Gestione Liquidazione IVA sui residui: aggiunta la procedura "creaObbligazioneResidua"
-- per la creazione di impegni residui per la liquidazione dell''IVA
--
-- Constants:

-- Functions e Procedures:

-- Controllo di assunzione obbligazione scatenato dopo l'inserimento dell'importo in testata dell'obblig.
--
-- pre-post-name: Nessun controllo su voci di spesa con Flag limite assunzione obbligazioni = N
-- pre: Flag limite assunzione obbligazioni della voce del piano = 'N'
-- post: ritorna 'Y'
--
-- pre-post-name: Controllo importi per obbligazioni con esercizio di competenza = esercizio e Flag limite assunzione obbligazioni
--                della voce del piano = 'Y'
-- pre:
--  Esercizio di competenza = esercizio
--  Flag limite assunzione obbligazioni
--                della voce del piano = 'Y'
--  e
--    + stanziamento anno1
--    + variazioni in più
--    - variazioni in meno
--    + residuo iniziale
--    + residuo variazioni in più
--    - residuo variazioni in meno
--    + cassa iniziale
--     - saldo obbligazioni
--     - importi relativi ai madati di regolarizzazione in riferimento al Cds di scrivania (Rich. CNR n.651)
--     + tot mandati su pgiro di esercizio precedente (se es scrivania>esercizio partenza) (Rich. CNR n.743)
--     - tot reversali su pgiro di esercizio precedente (se es scrivania>esercizio partenza) (Rich. CNR n.743)
--     - importo obbligazione
--  è minore di 0
-- post: Ritorna 'N'
--
-- pre-post-name: Controllo importi per obbligazioni con esercizio di competenza = esercizio + n con n=1,2
-- pre:
--  Se Flag limite assunzione obbligazioni della voce del piano = 'Y'
--  e
--     + (stanziamento anno1+n)*(percentuale di copertura obbligazioni anno1+n estratta sulla base del CDS dell'obbligazione, se non trovata = 0)
--     - saldo obbligazioni anno1+n
--     - importo obbligazione
--  è minore di 0
-- post: Ritorna 'N'
--
-- pre-post-name: Controllo superato
-- pre: Nessun'altra precondizione verificata
-- post: Ritorna 'Y'
--
-- Parametri:
--   aEs -> esercizio di competenza dell'obbligazione
--   aEsScrivania -> esercizio di scrivania (= esercizio dell'obbligazione)
--   aCdCds -> codice del centro di spesa
--   aCdUO -> codice dell'unità organizzativa
--   aCdElementoVoce -> codice dell'elemento voce di spesa CDS
--   im_obbligazione -> importo dell'obbligazione

-- Ritorna la massa spendibile a seconda dell'esercizio dell'obbligazione e quello di scrivania
-- UTILE SOLO nella mappa Ricostruzione dei Residui per il 2005 e richiamata in RicostruzioneResiduiComponent
--
-- pre-post-name: esercizi non specificati
-- pre: esercizio o esercizio di competenza non specificato
-- post: solleva un'eccezione
--
-- pre-post-name: esercizio di competenza = esercizio
-- pre: Richista massa spendibile per obbligazione in esercizio di competenza = esercizio di scrivania
-- post: ritorna
--    + stanziamento anno1
--    + variazioni in più
--    - variazioni in meno
--    + residuo iniziale
--    + residuo variazioni in più
--    - residuo variazioni in meno
--    + cassa iniziale
--     - saldo obbligazioni
--     - importi relativi ai madati di regolarizzazione in riferimento al Cds di scrivania (Rich. CNR n.651)
--     + tot mandati su pgiro di esercizio precedente (se es scrivania>esercizio partenza) (Rich. CNR n.743)
--     - tot reversali su pgiro di esercizio precedente (se es scrivania>esercizio partenza) (Rich. CNR n.743)
--
-- pre-post-name: esercizio di competenza = esercizio + n con n=1,2
-- pre: Richista massa spendibile per obbligazione in esercizio di competenza = esercizio di scrivania + n con n=1,2
-- post: ritorna
--     + (stanziamento anno1+n)*(percentuale di copertura obbligazioni anno1+n estratta sulla base del CDS dell'obbligazione,
--       se non trovata = (stanziamento anno1+n))
--     - saldo obbligazioni anno1+n
--
-- pre-post-name: esercizio di competenza = esercizio + n con n>2
-- pre: Richista massa spendibile per obbligazione in esercizio di competenza = esercizio di scrivania + n con n>2
-- post: Ritorna 0
--
-- Parametri:
--   aEs -> esercizio di competenza dell'obbligazione
--   aEsScrivania -> esercizio di scrivania (= esercizio dell'obbligazione)
--   aCdCds -> codice del centro di spesa
--   aCdElementoVoce -> codice dell'elemento voce di spesa CDS (attualmente non utilizzato)


/*
 function getMassaSpendibileResidui(
  aEs number,
  aEsScrivania number,
  aCdCds varchar2,
  aCdElementoVoce varchar2
 ) return number;
*/
-- Controllo di disponibilità di cassa, scatenato al salvataggio dell'obbligazione

 function checkDisponibilitaCassa(
  esercizio number,
  cd_cds varchar2,
  esercizio_originale number,
  pg_obbligazione NUMBER
 ) return char;

 --
 -- Crea gli impegni CNR secondo la seguente specifica
 --
 -- L'impegno viene creato su tutti i capitoli/articoli di spesa parte 1 (mastrini)
 -- La numerazione segue quella stabilita per gli impegni

 procedure creaImpegniCnr(aEs VARCHAR2, aCDR VARCHAR2, Utente VARCHAR2);

-- Crea o modifica l'impegno CNR per il saldo specificato: capitolo di competenza CNR spesa parte 1
--
-- pre-post-name: Modifica impegno su mastrino preesistente
-- pre:  Per la voce selezionata (voce di spesa CNR parte 1) esiste l'impegno associato al mastrino
-- post:
--  Viene estratto tale impegno strutturato sui tre livelli della tabella OBBLIGAZIONE/OBBLIGAZIONE_SCADENZARIO/OBBLIGAZIONE_SCAD_VOCE
--  Viene aggiornato il valore dell'impegno legato al capitolo con la seguente regola:
--    se lo stanziamento iniziale sul capitolo è minore o uguale all'importo associato a documenti amministrativi
--         l'importo dell'impegno = importo associato a documenti amministrativi
--    altrimenti
--         l'importo dell'impegno = stanziamento iniziale del capitolo
--
-- pre-post-name: Creazione dell'impegno nel caso non esista
-- pre:  Per la voce selezionata (voce di spesa CNR parte 1) non esiste l'impegno associato al mastrino
-- post:
--  Viene creato tale impegno sui tre livelli della tabella OBBLIGAZIONE/OBBLIGAZIONE_SCADENZARIO/OBBLIGAZIONE_SCAD_VOCE
--  L'impegno nrisulta avere una sola scadenza e dettaglio di scadenza
--  L'importo dell'impegno = stanziamento iniziale presente sulla voce a cui è collegato
--
-- Parametri:
-- aEsercizio -> esercizio contabile
-- aVoceF -> codice di mastrino finanziario di spesa CNR parte 1

 Procedure aggiornaImpegnoCapitolo(aEs varchar2, aEs_Residuo NUMBER, aVoceF VARCHAR2, DELTA_VAR_RES NUMBER, utente VARCHAR2);

 -- Metodo interno da non usare: non effettua controlli sul fatto che il capitolo sia di spesa CDS di parte 1!
  Procedure aggiornaImpegnoCapitolo(aSaldo voce_f_saldi_cdr_linea%Rowtype, DELTA_VAR_RES NUMBER, utente VARCHAR2);

-- Crea o modifica l'impegno CNR per il saldo specificato nel caso di variazione di bilancio: capitolo di competenza CNR spesa parte 1
--
-- pre-post-name: Modifica impegno su mastrino preesistente
-- pre:  Per la voce selezionata (voce di spesa CNR parte 1) esiste l'impegno associato al mastrino
-- post:
--  Viene estratto tale impegno strutturato sui tre livelli della tabella OBBLIGAZIONE/OBBLIGAZIONE_SCADENZARIO/OBBLIGAZIONE_SCAD_VOCE
--  Viene aggiornato il valore dell'impegno legato al capitolo con la seguente regola:
--    se lo stanziamento assestato sul capitolo è minore o uguale all'importo associato a documenti amministrativi
--         viene sollevata un'eccezione
--    altrimenti
--         l'importo dell'impegno = stanziamento iniziale del capitolo
--
-- pre-post-name: Creazione dell'impegno nel caso non esista
-- pre:  Per la voce selezionata (voce di spesa CNR parte 1) non esiste l'impegno associato al mastrino
-- post:
--  Viene sollevata un'eccezione
--
-- Parametri:
-- aEsercizio -> esercizio contabile
-- aVoceF -> codice di mastrino finanziario di spesa CNR parte 1

 procedure aggiornaImpegnoCapitoloVar(aEs varchar2, aEs_Residuo NUMBER, aVoceF VARCHAR2, DELTA_VAR_RES NUMBER, utente VARCHAR2);

-- Registrazione obbligazione su partita normale

 procedure creaObbligazione(
  isControlloBloccante boolean,
  aObb IN OUT obbligazione%rowtype,
  aScadenza1 in out obbligazione_scadenzario%rowtype,
  aDettScadenza1 in out CNRCTB035.scadVoceListS
 );

 procedure creaObbligazione(
  isControlloBloccante boolean,
  aObb IN OUT obbligazione%rowtype,
  aScadenza1 in out obbligazione_scadenzario%rowtype,
  aDettScadenza1 in out CNRCTB035.scadVoceListS,
  aScadenza2 in out obbligazione_scadenzario%rowtype,
  aDettScadenza2 out CNRCTB035.scadVoceListS,
  aScadenza3 in out obbligazione_scadenzario%rowtype,
  aDettScadenza3 in out CNRCTB035.scadVoceListS,
  aScadenza4 in out obbligazione_scadenzario%rowtype,
  aDettScadenza4 in out CNRCTB035.scadVoceListS,
  aScadenza5 in out obbligazione_scadenzario%rowtype,
  aDettScadenza5 in out CNRCTB035.scadVoceListS,
  aScadenza6 in out obbligazione_scadenzario%rowtype,
  aDettScadenza6 in out CNRCTB035.scadVoceListS,
  aScadenza7 in out obbligazione_scadenzario%rowtype,
  aDettScadenza7 in out CNRCTB035.scadVoceListS,
  aScadenza8 in out obbligazione_scadenzario%rowtype,
  aDettScadenza8 in out CNRCTB035.scadVoceListS,
  aScadenza9 in out obbligazione_scadenzario%rowtype,
  aDettScadenza9 in out CNRCTB035.scadVoceListS,
  aScadenza10 in out obbligazione_scadenzario%rowtype,
  aDettScadenza10 in out CNRCTB035.scadVoceListS
 );

-- Registrazione obbligazione residua

 procedure creaObbligazioneResidua(
  isControlloBloccante boolean,
  aObb IN OUT obbligazione%rowtype,
  aScadenza In out obbligazione_scadenzario%rowtype,
  aDettScadenza In out CNRCTB035.scadVoceListS
 );

-- Registrazione obbligazione su partita di giro

 procedure creaObbligazionePgiro(
  isControlloBloccante boolean,
  aObb IN OUT obbligazione%rowtype,
  aObbScad IN OUT obbligazione_scadenzario%rowtype,
  aAcc OUT accertamento%rowtype,
  aAccScad OUT accertamento_scadenzario%rowtype,
  aDtScadenza date);

-- Registrazione obbligazione su partita di giro TRONCA

 procedure creaObbligazionePgiroTronc(
  isControlloBloccante boolean,
  aObb IN OUT obbligazione%rowtype,
  aObbScad IN OUT obbligazione_scadenzario%rowtype,
  aAcc OUT accertamento%rowtype,
  aAccScad OUT accertamento_scadenzario%rowtype,
  aDtScadenza date
 );

-- Modifica di obbligazioni con singola spaccatura analitica sotto la scadenza
-- Adegua l'importo dell'obbligazione sulla base del nuovo importo attribuito alla scadenza specificata
-- Tolto controllo su disponibilità di competenza (richiesta ??? Mingarelli 20031222)

 procedure adeguaObbSV(aObbScad obbligazione_scadenzario%rowtype,aNewImp number,aUser varchar2);

-- Modifica di obbligazioni con singola spaccatura analitica sotto la scadenza
-- Adegua l'importo della scadenza successiva a quella specificata per modifica a nuovo importo di quella specificata

 procedure adeguaObbScadSuccSV(aObbScad obbligazione_scadenzario%rowtype,aNextPgObbScad number,aNewImp number,aUser varchar2);

-- INTERNE
 function getTotManRegolNoPgiro(aEsScrivania number,aCdCds varchar2) return number;


end;
