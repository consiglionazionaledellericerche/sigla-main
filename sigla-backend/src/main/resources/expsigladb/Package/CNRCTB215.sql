--------------------------------------------------------
--  DDL for Package CNRCTB215
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRCTB215" as
--
-- CNRCTB215 - Package controlli modifiche COEP/COAN
-- Date: 14/07/2006
-- Version: 1.12
--
-- Package per il controllo delle modifiche dei documenti contabili e amministrativi che generano modifiche COEP/COAN
--
-- Dependency: CNRCTB 100 IBMERR 001 IBMUTL 001
--
-- History:
--
-- Date: 02/05/2002
-- Version: 1.0
-- Creazione
--
-- Date: 07/05/2002
-- Version: 1.1
-- Completamento
--
-- Date: 15/05/2002
-- Version: 1.2
-- Miglioramento accesso a documenti amministrativi per aggiornamento stati
--
-- Date: 17/05/2002
-- Version: 1.3
-- Modificata interfaccia esterna di chiamata doRiproc
--
-- Date: 17/05/2002
-- Version: 1.3
-- Introdotti i metodi per la modifica sulla base delle differenze con una versione storica dell'obbligazione di dato pg_storico
--
-- Date: 27/05/2002
-- Version: 1.5
-- Fix errori su aggiornamento stato coge/coan
--
-- Date: 18/07/2002
-- Version: 1.6
-- Aggiornamento documentazione
--
-- Date: 28/08/2003
-- Version: 1.7
-- Fix errore su invocazione funzione di aggiornamento dello stato coge/coan doc amm
--
-- Date: 28/08/2003
-- Version: 1.8
-- Fix: aggiornamento stato coge/coan solo al cambiamento dell'elemento voce
--
-- Date: 31/01/2004
-- Version: 1.9
-- Lock del documento amministrativo di cui aggiornare lo stato coge/coan
-- Aggiornamento del solo pg_ver_rec e non di DUVA e UTUV
-- Controllo che il documento amministrativo da aggiornare NON sia già stato portato a nuovo esercizio anche
-- solo parzialmente
--
-- Date: 31/05/2004
-- Version: 1.10
-- Errore n. 827 nel recupero documento amministrativo: utilizzare il cds ed esercizio dell'obbligazione
--
-- Date: 02/11/2004
-- Version: 1.11
-- Errore n. 846
-- Se l'esercizio COEP del cds di origine del documento è chiuso definitivamente
-- il documento amministrativo non è messo "da riprocessare" e non viene sollevata eccezione
--
-- Date: 14/07/2006
-- Version: 1.12
-- Gestione Impegni/Accertamenti Residui:
-- aggiornata la funzione per tener conto anche del campo Esercizio Originale Impegno/Accertamento
--
-- Constants:

-- Functions e Procedures:

-- Aggiornamento dello stato COGE/COAN dei documenti amministrativi a seguito di modifiche su Obbligazione
--
-- Alla modifica di una obbligazione possono essere scatenate in automatico modifiche dello stato COGE e COAN dei documenti amministrativi collegati.
-- Il confronto può essere fatto ocon l'ultima versione presente nello storico dell'obbligazione o con versione storica identificata da progressivo aPgStorico.
--
-- pre-post-name: L'obbligazione non esiste
-- pre: Non esiste una obbligazione con esercizio/cds e pg obbligazione specificati
-- post: Viene sollevata un'eccezione
--
-- pre-post-name: Non esistono versioni storiche con cui confrontare la versione corrente dell'obbligazione
-- pre: La versione storica specificata con aPgStorico o l'ultima versione storica presente in tabella storico obbligazione (nel caso aPgStorico sia null) non viene trovata
-- post: Viene sollevata un'eccezione
--
-- pre-post-name: Aggiornamento dello stato COGE del documento amministrativo
-- pre: Lo stato COGE dei documenti amministrativi collegati alla scadenza modificata è "contabilizzato",
-- la voce del piano dell'obbligazione è cambiata
-- post: Viene aggiornato lo stato COGE del documento amministrativo a "da ricontabilizzare".
--
-- pre-post-name: Aggiornamento dello stato COAN del documento amministrativo
-- pre: Lo stato COAN dei documenti amministrativi collegati alla scadenza modificata è di contabilizzato,
-- la voce del piano dell'obbligazione è cambiata o la scadenza di obbligazione è cambiata
-- post: Viene aggiornato lo stato COAN del documento amministrativo a "da ricontabilizzare".
--
-- Parametri
--   aEs -> esercizio dell'obbligazione
--   aCdCds -> codice del cds dell'obbligazione
--   aPgObbligazione -> progressivo dell'obbligazione
--   aPgStorico -> progressivo della versione storica dell'obbligazione da cui parte il controllo sulle differenze con la versione corrente (opzionale)
--

 procedure doRiprocObb(
  aEs number,
  aCdCds varchar2,
  aEsOri number,
  aPgObbligazione number,
  aPgStorico number default null
 );

-- Aggiornamento dello stato COGE/COAN dei documenti amministrativi a seguito di modifiche su Accertamento
--
-- Alla modifica di una accertamento possono essere scatenate in automatico modifiche dello stato COGE e COAN dei documenti amministrativi collegati.
-- Il confronto può essere fatto ocon l'ultima versione presente nello storico dell'accertamento con versione storica identificata da progressivo aPgStorico.
--
-- pre-post-name: L'accertamento non esiste
-- pre: Non esiste un accertamento con esercizio/cds e pg accertamento specificati
-- post: Viene sollevata un'eccezione
--
-- pre-post-name: Non esistono versioni storiche con cui confrontare la versione corrente dell'accertamento
-- pre: La versione storica specificata con aPgStorico o l'ultima versione storica presente in tabella storico accertamento (nel caso aPgStorico sia null) non viene trovata
-- post: Viene sollevata un'eccezione
--
-- pre-post-name: Aggiornamento dello stato COGE del documento amministrativo
-- pre: Lo stato COGE dei documenti amministrativi collegati alla scadenza modificata è "contabilizzato",
-- la voce del piano dell'accertamento è cambiata
-- post: Viene aggiornato lo stato COGE del documento amministrativo a "da ricontabilizzare".
--
-- pre-post-name: Aggiornamento dello stato COAN del documento amministrativo
-- pre: Lo stato COAN dei documenti amministrativi collegati alla scadenza modificata è di contabilizzato,
-- la voce del piano dell'accertamento è cambiata o la scadenza di accertamento è cambiata
-- post: Viene aggiornato lo stato COGE del documento amministrativo a "da ricontabilizzare".
--
-- Parametri
--   aEs -> esercizio dell'accertamento
--   aCdCds -> codice del cds dell'accertamento
--   aPgAccertamento -> progressivo dell'accertamento
--   aPgStorico -> progressivo della versione storica dell'accertamento da cui parte il controllo sulle differenze con la versione corrente (opzionale)
--

 procedure doRiprocAcc(
  aEs number,
  aCdCds varchar2,
  aEsOri number,
  aPgAccertamento number,
  aPgStorico number default null
 );

 procedure doRiproc(aObb obbligazione%rowtype, aPgStorico number default null);
 procedure doRiproc(aAcc accertamento%rowtype, aPgStorico number default null);
end;
