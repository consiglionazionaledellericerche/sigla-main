--------------------------------------------------------
--  DDL for Package CNRCTB046
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRCTB046" as
--
-- CNRCTB046 - Riporto pratiche finanziarie interesercizio
-- Date: 13/07/2006
-- Version: 1.31
--
-- Dependency: IBMERR 001, CNRCTB 015 018 030 035 038 048 054 100
--
-- History:
--
-- Date: 07/05/2003
-- Version: 1.0
-- Creazione
--
-- Date: 14/05/2003
-- Version: 1.1
-- La pratica CORi di origine non viene troncata
--
-- Date: 10/06/2003
-- Version: 1.2
-- Aggiunti metodi relativi al riporto documenti contabili
--
-- Date: 11/06/2003
-- Version: 1.3
-- Aggiunti metodi per riporto Obbligazioni e Accertamenti
-- Corretto esercizio competenza per riporto Pgiro
--
-- Date: 16/06/2003
-- Version: 1.4
-- Riporto Obbligazioni
--
-- Date: 18/06/2003
-- Version: 1.5
-- Riporto obbligazioni pluriennali e pgiro CdS
-- Aggiunto controllo assunzione obbligazioni per pluriennali
-- Corretto metodo di aggiornamento doc amm (per impegni a consumo)
-- Aggiunto riporto parte entrate
--
-- Date: 26/06/2003
-- Version: 1.6
-- Aggiunti metodi di riporta/deriporta doc amministrativo
--
-- Date: 30/06/2003
-- Version: 1.7
-- Completato riporto di partite di giro CdS
-- Fix metodo deriporta doc amm
-- Controllo pratiche non ribaltabili
--
-- Date: 01/07/2003
-- Version: 1.8
-- Fix riporto doc amm
--
-- Date: 01/07/2003
-- Version: 1.9
-- Metodi di riporto con cambio di elemento_voce/voce_f
-- Riporto/deriporto singolo documento contabile
--
-- Date: 08/07/2003
-- Version: 1.10
-- Fix metodo di aggiornamento doc amm parte spese
-- Fix metodo di cancellazione fisica
-- Aggiunto metodo di confronto obbligazioni/accertamenti per
-- validare riporto esercizio precedente
--
-- Date: 09/07/2003
-- Version: 1.11
-- Fix controllo compatibilitÃ  esercizio riportoEsNextDocAmm
-- Fix riportoEsNextDocAmm (gestione note credito)
--
-- Date: 10/07/2003
-- Version: 1.12
-- Fix per allineamento a v_doc_amm_obb/acc
--
-- Date: 14/07/2003
-- Version: 1.13
-- Fix riporto doc amm per parzialmente pagati
--
-- Date: 15/07/2003
-- Version: 1.14
-- Fix metodi di controllo documento modificato per deriporto
-- Doc cont associati a doc amm riportati solo se stato coge/coan in (X,C)
--
-- Date: 17/07/2003
-- Version: 1.15
-- Fix messaggi di errore
-- Fix isDocModificato: non Ã¨ significativo controllare lo storico
--
-- Date: 23/07/2003
-- Version: 1.16
-- Fix CheckdeRiportaScadEsNext: verifica dell'esercizio del doc cont
-- (se doc amm collegato a doc cont poi annullato prima del riporto in avanti
--  non deve bloccare il porta indietro)
-- Fix riportoEsNextDocAmm e deriportoEsNextDocAmm: se non esistono documenti processabili
-- viene sollevato errore
-- Fix metodi di riporta/deriporta pgiro
--
-- Date: 24/07/2003
-- Version: 1.17
-- Fix metodo checkDeriportaScadEsNext per lettere di pagamento estero associate
-- nel nuovo esercizio a fatture passive
--
-- Date: 24/07/2003
-- Version: 1.18
-- Fix varie
-- Riorganizzazione metodi di deriporta con introduzione metodi specifici per
-- partite di giro cds
--
-- Date: 25/07/2003
-- Version: 1.19
-- Fix a CheckNoRiporta per doc amm associati a fondo economale
--
-- Date: 30/07/2003
-- Version: 1.20
-- Spostamento metodi di supporto su CNRCTB048
-- Fix su riporto avanti con cambio capitolo
--
-- Date: 31/07/2003
-- Version: 1.21
-- Fix per determinazione voce_f e controllo elemento voce su pgiro
-- Controlli sullo stato dell'esercizio
--
-- Date: 06/08/2003
-- Version: 1.22
-- Aggiunta documentazione: pre-post condizioni
--
-- Date: 26/08/2003
-- Version: 1.23
-- Fix riporto partite di giro: ripristinato importo associato a doc amm
--
-- Date: 07/01/2004
-- Version: 1.24
-- Fix temporanea per errore riporto impegnoni
--
-- Date: 07/01/2004
-- Version: 1.25
-- Fix creazione dettaglio di scadenza per gestione impegni e impegni residui
-- (correzione disallineamento saldi per doc a consumo: importo scad_voce pari alla
--  scadenza e non all'im_voce originale)
--
-- Date: 09/01/2004
-- Version: 1.26
-- Fix a riportoEsNextDocAmm: contabilizzazione economico/analitica del doc amm
-- da riportare prima del ribaltamento (permette di gestire il salvataggio del doc
-- da parte dell'applicazione che cambia contestualmente gli stati coge e coan)
--
-- Date: 12/01/2004
-- Version: 1.27
-- Fix per modifica interfaccia a CNRCTB048.getVoceF (determinazione capitolo per
-- obb di spese di costi altrui)
--
-- Date: 26/02/2004
-- Version: 1.28
-- Gestione riporto in avanti di doc cont ente riportati, deriportati ma senza cancellazione
-- fisica del residuo (doc amm associato, ma aanullato): non viene effettuato un inserimento
-- ma un aggiornamento del doc cont esistente (per acr, si aggiungono le scadenze e scad voce)
-- Nel caso di deriporto senza cancellazione fisica dei residui, su questi non viene
-- solo portato a zero l'importo, ma sono anche stornati (impostata la data di cancellazione)
--
-- Date: 25/10/2004
-- Version: 1.29
-- Fix ai controlli sullo stato dell'esercizio per il riporto avanti di doc amm
--
-- Date: 05/11/2004
-- Version: 1.30
-- Fix al riporto avanti di doc amm: non vanno processati in ecomomica se il riporto avviene
-- da scrivania diversa (maggiore) dell'esercizio del documento, il loro stato coge/coan deve
-- essere riportato a "contabilizzato"
--
-- Date: 13/07/2006
-- Version: 1.31
-- Gestione Impegni/Accertamenti Residui:
-- aggiornata la funzione per tener conto anche del campo Esercizio Originale Impegno/Accertamento
--
-- Constants:

-- Functions e Procedures:

 Function getElementoVoceNew (aEvOld elemento_voce%rowtype) Return elemento_voce%rowtype;

 Function GET_ESERCIZIO_ORIGINALE (aCds varchar2,
                                   aEs number,
                                   aEsOri number,
                                   aPg number,
                                   aTiGestione VARCHAR2) Return NUMBER;

 procedure ripPgiroCds(
  aObb IN OUT obbligazione%rowtype,
  aObbNew IN OUT obbligazione%rowtype,
  aTSNow date,
  aUser varchar2
 );

  procedure ripPgiroCds(
  aAcc IN OUT accertamento%rowtype,
  aObbNew IN OUT obbligazione%rowtype,
  aTSNow date,
  aUser varchar2
 );

  procedure ripPgiroCds(
  aAcc IN OUT accertamento%rowtype,
  aAccNew IN OUT accertamento%rowtype,
  aTSNow date,
  aUser varchar2
 );

  procedure ripPgiroCds(
  aObb IN OUT obbligazione%rowtype,
  aAccNew IN OUT accertamento%rowtype,
  aTSNow date,
  aUser varchar2
 );

 procedure annullaRipPgiroCds(
  aAccNew IN OUT accertamento%rowtype,
  aTSNow date,
  aUser varchar2
 );

 procedure annullaRipPgiroCds(
  aObbNew IN OUT obbligazione%rowtype,
  aTSNow date,
  aUser varchar2
 );

-- riportoEsNextObb
-- ----------------
--
-- pre-post name: riporto obbligazione all'esercizio successivo - documento giÃ  riportato
-- pre: l'obbligazione risulta giÃ  riportata all'esercizio successivo
-- post: viene sollevato un errore
--
-- pre-post name: riporto obbligazione all'esercizio successivo - obbligazione con importo nullo
-- pre: l'obbligazione non su pgiro o che apre partita di giro ha importo nullo
-- post: viene sollevato un errore
--
-- pre-post name: riporto obbligazione all'esercizio successivo - obbligazione non ribaltabile
-- pre: l'obbligazione non Ã¨ un documento ribaltabile all'esercizio successivo
-- post: viene sollevato un errore
--
-- pre-post name: riporto obbligazione all'esercizio successivo - obbligazione associata
--                a doc amm collegato a fondo economale
-- pre: l'obbligazione Ã¨ associata a doc amm giÃ  collegato a spesa di fondo economale
-- post: viene sollevato un errore
--
-- pre-post name: riporto obbligazione all'esercizio successivo - obbligazione associata
--                a fondo economale
-- pre: l'obbligazione Ã¨ collegata a spesa non documentata di fondo economale
-- post: viene sollevato un errore
--
-- pre-post name: riporto obbligazione all'esercizio successivo - obbligazione associata
--                a doc amm generico di versamento cori
-- pre: l'obbligazione Ã¨ associata a doc amm generico di versamento cori
-- post: viene sollevato un errore
--
-- pre-post name: riporto obbligazione all'esercizio successivo - obbligazione associata
--                a doc amm da contabilizzare in coge
-- pre: l'obbligazione Ã¨ associata a doc amm da contabilizzare in coge
-- post: viene sollevato un errore
--
-- pre-post name: riporto obbligazione all'esercizio successivo - obbligazione associata
--                a doc amm da contabilizzare in coan
-- pre: l'obbligazione Ã¨ associata a doc amm da contabilizzare in coan
-- post: viene sollevato un errore
--
-- pre-post name: riporto obbligazione all'esercizio successivo - obbligazione associata
--                a cori di compenso
-- pre: l'obbligazione Ã¨ associata a contributi ritenuta di compensi
-- post: viene sollevato un errore
--
-- pre-post name: riporto obbligazione all'esercizio successivo - obbligazione parte di
--                liquidazione CORI accentrata
-- pre: l'obbligazione Ã¨ parte di liquidazione CORI accentrata
-- post: viene sollevato un errore
--
-- pre-post name: riporto obbligazione all'esercizio successivo - obbligazione parte di
--                liquidazione IVA al centro
-- pre: l'obbligazione Ã¨ parte di liquidazione IVA al centro
-- post: viene sollevato un errore
--
-- pre-post name: riporto obbligazione all'esercizio successivo - terzo non valido
-- pre: il terzo non Ã¨ valido nel nuovo esercizio
-- post: viene sollevato un errore
--
-- pre-post name: riporto obbligazione all'esercizio successivo - unitÃ  organizzativa
--                non valida
-- pre: l'unitÃ  organizzativa dell'obbligazione non Ã¨ valida nel nuovo esercizio
-- post: viene sollevato un errore
--
-- pre-post name: riporto obbligazione all'esercizio successivo - esercizio non valido
-- pre: l'esercizio origine di ribaltamento non Ã¨ aperto oppure non esiste l'esercizio
--      successivo per il cds del documento contabile
-- post: viene sollevato un errore
--
-- pre-post name: riporto obbligazione all'esercizio successivo - valorizzati voce_f
--                ed elemento_voce
-- pre: nella chiamata alla procedura riportoObb() sono stati valorizzati sia la voce_f che
--      l'elemento_voce
-- post: viene sollevato un errore
--
-- pre-post name: riporto obbligazione all'esercizio successivo - cambio imputazione
--                finanziaria di impegni - valorizzato elemento_voce
-- pre: nel riporto di impegni non su partita di giro Ã¨ stata valorizzato l'elemento voce
-- post: viene sollevato un errore
--
-- pre-post name: riporto obbligazione all'esercizio successivo - cambio imputazione
--                finanziaria di impegni su pgiro - valorizzato voce_f
-- pre: nel riporto di impegni su partita di giro Ã¨ stata valorizzato la voce_f
-- post: viene sollevato un errore
--
-- pre-post name: riporto obbligazione all'esercizio successivo - cambio imputazione
--                finanziaria di obbligazioni cds - valorizzato voce_f
-- pre: nel riporto di obbligazioni cds Ã¨ stata valorizzato la voce_f
-- post: viene sollevato un errore
--
-- pre-post name: riporto obbligazione all'esercizio successivo - non esiste
--                elemento voce nel nuovo esercizio
-- pre: nel riporto di documenti contabili di spesa, non esiste l'elemento voce nel
--      nuovo esercizio
-- post: viene sollevato un errore
--
-- pre-post name: riporto obbligazione pluriennale all'esercizio successivo - controllo
--                assunzione obbligazioni non superato
-- pre: nel riporto di un'obbligazione pluriennale, il documento non supera il controllo
--      di assunzione obbligazioni nel nuovo esercizio
-- post: viene sollevato un errore
--
-- pre-post name: riporto obbligazione all'esercizio successivo - linea di attivitÃ 
--                non valida nel nuovo esercizio
-- pre: la linea di attivitÃ  definita per il dettaglio di scadenza non Ã¨ valida nel
--      nuovo esercizio
-- post: viene sollevato un errore
--
-- pre-post name: riporto obbligazione all'esercizio successivo - voce_f non valida nel
--                nuovo esercizio
-- pre: la voce_f definita per il dettaglio di scadenza non Ã¨ valida nel
--      nuovo esercizio
-- post: viene sollevato un errore
--
-- pre-post name: riporto impegni all'esercizio successivo - saldo non trovato
-- pre: nel riporto all'esercizio successivo di impegni, non viene trovato il saldo residuo
--      di cui aggiornare l'importo iniziale
-- post: viene sollevato un errore
--
-- pre-post name: riporto documenti su partita di giro all'esercizio successivo -
--                elemento voce non su partita di giro
-- pre: documento contabile su partita di giro, elemento voce non su partita di giro
-- post: viene sollevato un errore
--
-- pre-post name: riporto documenti non su partita di giro all'esercizio successivo -
--                elemento voce su partita di giro
-- pre: documento contabile non su partita di giro, elemento voce su partita di giro
-- post: viene sollevato un errore
--
-- pre-post name: riporto obbligazioni, impegni, impegni residui all'esercizio successivo
-- pre: nessuna delle precedenti precondizioni, il documento Ã¨ un'obbligazione, un impegno
--      o un impegno residuo, non viene definita una nuova imputazione finanziaria
-- post: viene costruita la testata del documento contabile definendo i riferimenti
--       ori_riporto al documento originale, se si tratta di obbligazione
--       pluriennale viene effettuato il controllo di assunzione obbligazioni. La procedura
--       cicla sulle scadenze del documento, e per ogni scadenza ribaltabile crea il record
--       della scadenza. Per ogni scadenza ribaltabile cicla sui dettagli di scadenza per
--       costriure la lista dei nuovi dettagli. Per ogni nuovo dettaglio, determina la linea
--       di attivitÃ  (verificando la presenza di una eventuale mappatura) e ne verifica
--       la validitÃ . Determina il capitolo finanziario (voce_f) in funzione della linea
--       di attivitÃ , del dettaglio di scadenza originale, e del tipo di documento contabile.
--       La procedura crea la scadenza di obbligazione con i suoi dettagli. Se la procedura
--       genera un impegno residuo, vengono aggiornati gli stanziamenti residui dei saldi.
--       Se la scadenza in esame Ã¨ associata a documenti amministrativi, vengono aggiornati
--       i riferimenti sulla nuova scadenza.
--       Viene aggiornata la testata del documento contabile origine come riportato.
--
-- pre-post name: riporto impegni, impegni residui,  non su partita di giro, all'esercizio
--                successivo con cambio di imputazione finanziaria
-- pre: nessuna delle precedenti precondizioni, il documento Ã¨ un impegno non su pgiro, un
--      impegno residuo non su pgiro, Ã¨ stata definita una voce_f
-- post: viene costruita la testata del documento contabile definendo i riferimenti
--       ori_riporto al documento originale, se si tratta di obbligazione
--       pluriennale viene effettuato il controllo di assunzione obbligazioni. La procedura
--       cicla sulle scadenze del documento, e per ogni scadenza ribaltabile crea il record
--       della scadenza. Per ogni scadenza ribaltabile cicla sui dettagli di scadenza per
--       costriure la lista dei nuovi dettagli. Per ogni nuovo dettaglio, determina la linea
--       di attivitÃ  (verificando la presenza di una eventuale mappatura) e ne verifica
--       la validitÃ . Il capitolo finanziario Ã¨ definito dal parametro di ingresso.
--       La procedura crea la scadenza di obbligazione con i suoi dettagli. Se la procedura
--       genera un impegno residuo, vengono aggiornati gli stanziamenti residui dei saldi.
--       Se la scadenza in esame Ã¨ associata a documenti amministrativi, vengono aggiornati
--       i riferimenti sulla nuova scadenza.
--       Viene aggiornata la testata del documento contabile origine come riportato.
--
-- pre-post name: riporto impegni, impegni residui su partita di giro, e obbligazioni all'
--                esercizio successivo con cambio di imputazione finanziaria
-- pre: nessuna delle precedenti precondizioni, il documento Ã¨ un impegno su pgiro, un
--      impegno residuo su pgiro, Ã¨ stata definito un elemento_voce
-- post: viene costruita la testata del documento contabile definendo i riferimenti
--       ori_riporto al documento originale, se si tratta di obbligazione
--       pluriennale viene effettuato il controllo di assunzione obbligazioni. La procedura
--       cicla sulle scadenze del documento, e per ogni scadenza ribaltabile crea il record
--       della scadenza. Per ogni scadenza ribaltabile cicla sui dettagli di scadenza per
--       costriure la lista dei nuovi dettagli. Per ogni nuovo dettaglio, determina la linea
--       di attivitÃ  (verificando la presenza di una eventuale mappatura) e ne verifica
--       la validitÃ . Il capitolo finanziario Ã¨ definito in funzione del parametro di ingresso
--       elemento_voce, e della linea di attivitÃ 
--       La procedura crea la scadenza di obbligazione con i suoi dettagli. Se la procedura
--       genera un impegno residuo, vengono aggiornati gli stanziamenti residui dei saldi.
--       Se la scadenza in esame Ã¨ associata a documenti amministrativi, vengono aggiornati
--       i riferimenti sulla nuova scadenza.
--       Viene aggiornata la testata del documento contabile origine come riportato.
--
-- pre-post name: riporto all'esercizio successivo di annotazioni su partita di giro cds
-- pre: nessuna delle precedenti precondizioni, l'obbligazione Ã¨ parte di annotazione su
--      partita di giro
-- post: se l'obbligazione chiude la partita di giro, viene creata una annotazione di spesa
--       su partita di giro tronca nel nuovo esercizio con elemento voce eventualmente
--       definito dall'utente. Se l'obbligazione Ã¨ associata a doc amm, vengono
--       aggiornati i riferimenti alla nuova annotazione. La testata dell'obbligazione
--       viene targata come riportata, e la nuova obbligazione fa riferimento al documento
--       originale.
--       Se l'obbligazione apre la partita di giro e la controparte Ã¨ completamente riscontrata,
--       viene creata un'annotazione di spesa su partita di giro tronca con elemento voce
--       eventualmente definito dall'utente. Se l'obbligazione Ã¨ associata a doc amm, vengono
--       aggiornati i riferimenti alla nuova annotazione. La testata dell'obbligazione
--       viene targata come riportata, e la nuova obbligazione fa riferimento al documento
--       originale.
--       Se l'obbligazione apre la partita di giro e la controparte non Ã¨ riscontrata,
--       viene creata una annotazione di spesa su partita di giro nel nuovo esercizio con
--       elemento voce eventualmente definito dall'utente. Se l'annotazione Ã¨ associata
--       a doc amm, vengono aggiornati i riferimenti all'annotazione. Entrambe le parti
--       dell'annotazione vengono targate come riportate e la nuova annotazione fa riferimento
--       all'annotazione originale.
--
-- Parametri:
-- aObb -> rowtype dell'obbligazione da riportare all'esercizio successivo
-- aElementoVoce -> rowtype dell'elemento_voce se cambia imputazione finanziaria
-- aVoceF -> rowtype della voce_F se cambia imputazione finanziaria
-- aUser -> utente che effettua il riporto del documento
-- aTSNow -> timestamp dell'operazione
procedure riportoEsNextObb(
		  aObb in out obbligazione%rowtype,
		  aElementoVoce elemento_voce%rowtype,
		  aVoceF voce_F%rowtype,
		  aUser varchar2,
		  aTSNow date);

-- riportoEsNextAcc
-- ----------------
--
-- pre-post name: riporto accertamento all'esercizio successivo - documento giÃ  riportato
-- pre: l'accertamento risulta giÃ  riportata all'esercizio successivo
-- post: viene sollevato un errore
--
-- pre-post name: riporto accertamento all'esercizio successivo - accertamento con importo nullo
-- pre: l'accertamento non su pgiro o che apre partita di giro ha importo nullo
-- post: viene sollevato un errore
--
-- pre-post name: riporto accertamento all'esercizio successivo - accertamento non ribaltabile
-- pre: l'accertamento non Ã¨ un documento ribaltabile all'esercizio successivo
-- post: viene sollevato un errore
--
-- pre-post name: riporto accertamento all'esercizio successivo - accertamento gestito in
--                automatico dalla liquidazione cori
-- pre: l'accertamento Ã¨ associato a doc amm generico di versamento cori
-- post: viene sollevato un errore
--
-- pre-post name: riporto accertamento all'esercizio successivo - accertamento associato
--                a doc amm da contabilizzare in coge
-- pre: l'accertamento Ã¨ associata a doc amm da contabilizzare in coge
-- post: viene sollevato un errore
--
-- pre-post name: riporto accertamento all'esercizio successivo - accertamento associato
--                a doc amm da contabilizzare in coan
-- pre: l'accertamento Ã¨ associato a doc amm da contabilizzare in coan
-- post: viene sollevato un errore
--
-- pre-post name: riporto accertamento all'esercizio successivo - accertamento associato
--                a cori di compenso
-- pre: l'accertamento Ã¨ associato a contributi ritenuta di compensi
-- post: viene sollevato un errore
--
-- pre-post name: riporto accertamento all'esercizio successivo - accertamento parte di
--                liquidazione CORI accentrata
-- pre: l'accertamento Ã¨ parte di liquidazione CORI accentrata
-- post: viene sollevato un errore
--
-- pre-post name: riporto accertamento all'esercizio successivo - terzo non valido
-- pre: il terzo non Ã¨ valido nel nuovo esercizio
-- post: viene sollevato un errore
--
-- pre-post name: riporto accertamento all'esercizio successivo - voce_f non valida
-- pre: non viene cambiata l'imputazione finanziaria, non esiste il capitolo finanziario
--      (voce_f) nel nuovo esercizio
-- post: viene sollevato un errore
--
-- pre-post name: riporto accertamento all'esercizio successivo - esercizio non valido
-- pre: l'esercizio origine di ribaltamento non Ã¨ aperto oppure non esiste l'esercizio
--      successivo per il cds del documento contabile
-- post: viene sollevato un errore
--
-- pre-post name: riporto accertamento all'esercizio successivo - unitÃ  organizzativa
--                non valida
-- pre: l'unitÃ  organizzativa dell'accertamento non Ã¨ valida nel nuovo esercizio
-- post: viene sollevato un errore
--
-- pre-post name: riporto documenti su partita di giro all'esercizio successivo -
--                elemento voce non su partita di giro
-- pre: documento contabile su partita di giro, elemento voce non su partita di giro
-- post: viene sollevato un errore
--
-- pre-post name: riporto documenti non su partita di giro all'esercizio successivo -
--                elemento voce su partita di giro
-- pre: documento contabile non su partita di giro, elemento voce su partita di giro
-- post: viene sollevato un errore
--
-- pre-post name: riporto accertamento all'esercizio successivo - capitolo finanziario
--                non valido nel nuovo esercizio
-- pre: riporto all'esercizio successivo con cambio di elemento voce, capitolo finanziario
--      corrispondente non trovato
-- post: viene sollevato errore
--
-- pre-post name: riporto accertamenti all'esercizio successivo - saldo non trovato
-- pre: nel riporto all'esercizio successivo di accertamenti nel bilancio ente, non viene
--      trovato il saldo residuo di cui aggiornare l'importo iniziale
-- post: viene sollevato un errore
--
-- pre-post name: riporto accertamento all'esercizio successivo - linea di attivitÃ 
--                non valida nel nuovo esercizio
-- pre: la linea di attivitÃ  definita per il dettaglio di scadenza non Ã¨ valida nel
--      nuovo esercizio
-- post: viene sollevato un errore
--
-- pre-post name: riporto accertamento all'esercizio successivo - linea di attivitÃ 
--                non compatibile con l'elemento voce
-- pre: la linea di attivitÃ  definita per il dettaglio di scadenza con natura non compati-
--      bile con l'elemento voce nel nuovo esercizio
-- post: viene sollevato un errore
--
-- pre-post name: riporto all'esercizio successivo di annotazioni su partita di giro cds
-- pre: nessuna delle precedenti precondizioni, l'accertamento Ã¨ parte di annotazione su
--      partita di giro nel bilancio cds
-- post: se l'accertamento chiude la partita di giro, viene creata una annotazione di entrata
--       su partita di giro tronca nel nuovo esercizio con elemento voce eventualmente
--       definito dall'utente. Se l'accertamento Ã¨ associato a doc amm, vengono
--       aggiornati i riferimenti alla nuova annotazione. La testata dell'accertamento
--       viene targata come riportata, e il nuovo acertamento fa riferimento al documento
--       originale.
--       Se l'accertamento apre la partita di giro e la controparte Ã¨ completamente riscontrata,
--       viene creata un'annotazione di entrata su partita di giro tronca con elemento voce
--       eventualmente definito dall'utente. Se l'accertamento Ã¨ associato a doc amm, vengono
--       aggiornati i riferimenti alla nuova annotazione. La testata dell'accertamento
--       viene targato come riportato, e il nuovo accertamento fa riferimento al documento
--       originale.
--       Se l'accertamento apre la partita di giro e la controparte non Ã¨ riscontrata,
--       viene creata una annotazione di entrata su partita di giro nel nuovo esercizio con
--       elemento voce eventualmente definito dall'utente. Se l'annotazione Ã¨ associata
--       a doc amm, vengono aggiornati i riferimenti all'annotazione. Entrambe le parti
--       dell'annotazione vengono targate come riportate e la nuova annotazione fa riferimento
--       all'annotazione originale.
--
-- pre-post name: riporto accertamento all'esercizio successivo
-- pre: nessuna delle precedenti precondizioni, il documento Ã¨ un accertamento (ente)
-- post: viene costruita la testata del documento contabile definendo i riferimenti
--       ori_riporto al documento originale. L'imputazione finanziaria Ã¨ eventualmente
--       definita dall'elemento voce scelto dall'utente.
--       La procedura cicla sulle scadenze del documento, e per ogni scadenza ribaltabile
--       crea il record della scadenza. Per ogni scadenza ribaltabile cicla sui dettagli
--       di scadenza per costruire la lista dei nuovi dettagli. Per ogni nuovo dettaglio,
--       determina la linea di attivitÃ  (verificando la presenza di una eventuale mappatura)
--       e ne verifica la validitÃ .
--       La procedura crea la scadenza di accertamento con i suoi dettagli. Se la procedura
--       genera un accertamento residuo, vengono aggiornati gli stanziamenti residui dei saldi.
--       Se la scadenza in esame Ã¨ associata a documenti amministrativi, vengono aggiornati
--       i riferimenti sulla nuova scadenza.
--       Viene aggiornata la testata del documento contabile origine come riportato.
--
-- Parametri:
-- aAcc -> rowtype dell'accertamento da riportare all'esercizio successivo
-- aElementoVoce -> rowtype dell'elemento_voce se cambia imputazione finanziaria
-- aUser -> utente che effettua l'operazione
-- aTSNow -> timestap dell'operazione
procedure riportoEsNextAcc(aAcc          In Out accertamento%Rowtype,
		           aElementoVoce elemento_voce%Rowtype,
		           aUser         VARCHAR2,
		           aTSNow        DATE);

-- riportoEsNextDocCont
-- --------------------
--
-- pre-post name: riporto all'esercizio successivo di un documento contabile - documento
--                inesistente
-- pre: la chiave del documento contabile con la sua gestione (entrata o spesa) non individua
--      un documento contabile esistente
-- post: viene sollevato un errore
--
-- pre-post: riporto all'esercizio successivo di un documento contabile di entrata -
--           operazione fallita
-- pre: nessuna delle precedenti precondizioni, il documento contabile Ã¨ di entrata,
--      la procedura di riporto all'esercizio successivo solleva un errore
--      (vd. pre-post relative a riportoEsNextAcc)
-- post: viene sollevato un errore
--
-- pre-post: riporto all'esercizio successivo di un documento contabile di spesa -
--           operazione fallita
-- pre: nessuna delle precedenti precondizioni, il documento contabile Ã¨ di spesa,
--      la procedura di riporto all'esercizio successivo solleva un errore
--      (vd. pre-post relative a riportoEsNextObb)
-- post: viene sollevato un errore
--
-- pre-post name: riporto all'esercizio successivo di un documento contabile di entrata
-- pre: nessuna delle precedenti precondizioni, il documento contabile Ã¨ un accertamento
-- post: il documento contabile viene riportato all'esercizio successivo (vd. pre-post relative
--       a riportoEsNextAcc() )
--
-- pre-post name: riporto all'esercizio successivo di un documento contabile di spesa
-- pre: nessuna delle precedenti precondizioni, il documento contabile Ã¨ un'obbligazione
--      o impegno
-- post: il documento contabile viene riportato all'esercizio successivo (vd. pre-post relative
--       a riportoEsNextObb() )
--
-- Parametri:
--
-- aCds -> cds del documento contabile
-- aEs -> esercizio del documento contabiel
-- aPg -> numero progressivo del documento contabile
-- aTiGestione -> entrata o spesa
-- aUser -> utente che effettua l'operazione
procedure riportoEsNextDocCont(aCds varchar2,
		               aEs number,
		               aEsOri number,
		               aPg number,
		               aTiGestione varchar2,
		               aUser varchar2);

-- deriportoEsNextDocCont
--
-- Riporto dall'esercizio successivo di Documenti Contabili
--
-- pre-post-name: Riporto dall'esercizio successivo non possibile
-- pre: Ã¨ stato riscontrato un errore durante l'operazione di riporto del documento contabile di entrata/spesa,
--	(v. deriportoEsNextAcc/deriportoEsNextObb)
-- post: viene lanciata una eccezione
--
-- pre-post-name: Riporto dall'esercizio successivo di Documenti Contabili
-- pre: Il sistema controlla se l'operazione Ã¨ stata richiesta per documenti tipo entrata o spesa
-- post: viene richiamato la procedura relativa al tipo di gestione richiesta:
--	- CNRCTB001.GESTIONE_ENTRATE-> deriportoEsNextAcc;
--	- CNRCTB001.GESTIONE_SPESE -> deriportoEsNextObb.
--
-- Parametri:
-- aCds -> il Cds del documento contabile
-- aEs -> esercizio del documento contabile
-- aPg -> Progressivo del documento contabile
-- aTiGestione -> flag che indica se l'operazione Ã¨ effettuata per una gestione entrate (CNRCTB001.GESTIONE_ENTRATE) o spese (CNRCTB001.GESTIONE_SPESE)
-- aUser -> utente che effettua la modifica

procedure deriportoEsNextDocCont(aCds varchar2,
		  aEs number,
		  aEsOri number,
		  aPg number,
		  aTiGestione varchar2,
		  aUser varchar2);

-- deriportoEsNextAcc
--
-- Riporto dall'esercizio successivo di Documenti Contabili tipo Entrata (Accertamenti)
--
-- pre-post-name: esercizio corrente non valido
-- pre: l'esercizio di scrivania non risulta aperto per il Cds (v. cnrctb048.CHECKESERCIZIO)
-- post: viene lanciata una eccezione
--
-- pre-post-name: documento su partita di giro
-- pre: il documento Ã¨ su partita di giro e non Ã¨ un RESIDUO
-- post: l'operazione continua nel metodo corrispondente (v. deriportoPGiroCds)
--
-- pre-post-name: il documento non trova corrispondenza nell'esercizio successivo
-- pre: il documento non passa il controllo sulla corrispondenza nell'esercizio successivo (v. CNRCTB048.getDocRiportato)
-- post: viene sollevata un'eccezione
--
-- pre-post-name: annotazione su partita di giro non riportata o variata nell'esercizio successivo
-- pre: l'annotazione su partita di giro non risulta riportata o ha subito delle modifiche/variazioni
--	nell'esercizio successivo, (v. checkDeRiportaEsNext)
-- post: viene sollevata un'eccezione
--
-- pre-post-name: documento contabile associato a documento ammin. o parzialmente pagato
-- pre: il documento contabile dell'esercizio successivo, risulta essere associato a documento amministrativo
--	oppure Ã¨ stato parzialmente pagato, (v. checkDeRiportaScadEsNext)
-- post: viene sollevata un'eccezione
--
-- pre-post-name: Saldo residuo non trovato per voce finanziaria
-- pre: un accertamento di tipo RESIDUO, (CNRCTB018.TI_DOC_ACC_RES), non ha una saldo residuo per la voce finanziaria
-- post: viene sollevata un'eccezione
--
-- pre-post-name: Riporto dall'esercizio successivo di Documenti Contabili tipo Entrata (Accertamenti)
-- pre: Nessun'altra precondizione verificata
-- post: cicla sulle scadenze dell'accertamento riportato:
--	se l'accertamento Ã¨ associato a doc_amm, vengono aggiornati i documenti amministrativi collegati (v. CNRCTB048.aggiornaDocAmm)
--	l'importo associato a doc_amm viene impostato = 0;
--	se l'accertamento Ã¨ di tipo Residuo, viene aggiornato l'importo stanziamento iniziale nei saldi;
--   In testata dell'accertamento, viene riportato a 'N' il flag "riportato".
--   Viene cancellato fisicamente l'accertamento riportato.
--
-- Parametri:
-- aAcc -> il documento da riportare
-- aUser -> utente che effettua la modifica
-- aTSNow -> la data di sistema
 procedure deriportoEsNextAcc(aAcc in out accertamento%rowtype,
 		   aUser varchar2,
		   aTSNow date);

-- deriportoEsNextObb
--
-- Riporto dall'esercizio successivo di Documenti Contabili tipo Spesa (Obbligazione)
--
-- pre-post-name: esercizio corrente non valido
-- pre: l'esercizio di scrivania non risulta aperto per il Cds (v. cnrctb048.CHECKESERCIZIO)
-- post: viene lanciata una eccezione
--
-- pre-post-name: obbligazione su partita di giro
-- pre: il documento Ã¨ una obbligazione su partita di giro e non Ã¨ un IMPEGNO/RESIDUO
-- post: l'operazione continua nel metodo corrispondente (v. deriportoPGiroCds)
--
-- pre-post-name: il documento non trova corrispondenza nell'esercizio successivo
-- pre: il documento non passa il controllo sulla corrispondenza nell'esercizio successivo (v. CNRCTB048.getDocRiportato)
-- post: viene sollevata un'eccezione
--
-- pre-post-name: annotazione su partita di giro non riportata o variata nell'esercizio successivo
-- pre: l'annotazione su partita di giro non risulta riportata o ha subito delle modifiche/variazioni
--	nell'esercizio successivo, (v. checkDeRiportaEsNext)
-- post: viene sollevata un'eccezione
--
-- pre-post-name: documento contabile associato a documento ammin. o parzialmente pagato
-- pre: il documento contabile dell'esercizio successivo, risulta essere associata a documento amministrativo
--	oppure Ã¨ stato parzialmente pagato, (v. checkDeRiportaScadEsNext)
-- post: viene sollevata un'eccezione
--
-- pre-post-name: Saldo residuo non trovato per voce finanziaria
-- pre: una obbligazione di tipo RESIDUO, (CNRCTB018.TI_DOC_IMP_RES), non ha una saldo residuo per la voce finanziaria
-- post: viene sollevata un'eccezione
--
-- pre-post-name: Riporto dall'esercizio successivo di Documenti Contabili tipo Spesa (Obbligazione)
-- pre: Nessun'altra precondizione verificata
-- post: cicla sulle scadenze dell'obbligazione riportata:
--	se l'obbligazione Ã¨ associata a doc_amm, vengono aggiornati i documenti amministrativi collegati (v. CNRCTB048.aggiornaDocAmm)
--	l'importo associato a doc_amm viene impostato = 0;
--	se l'obbligazione Ã¨ di tipo Imegno/Residuo, viene aggiornato l'importo stanziamento iniziale nei saldi;
--   In testata dell'obbligazione, viene riportato a 'N' il flag "riportato".
--   Viene cancellata fisicamente l'obbligazione riportata.
--
--
-- Parametri:
-- aObb -> il documento da riportare
-- aUser -> utente che effettua la modifica
-- aTSNow -> la data di sistema
 procedure deriportoEsNextObb(aObb in out obbligazione%rowtype,
 		              aUser varchar2,
		              aTSNow date);

-- cancellaFisicamente
--
-- Cancellazione fisica di un documento contabile riportato nell'esercizio successivo
--
-- pre-post-name: annotazione su partita di giro non valida
-- pre: l'annotazione Ã¨ su partita di giro e non Ã¨ un impegno/residuo, ma risultano piÃ¹ di un dettaglio o di una scadenza,
--	(v. CNRCTB035.GETPGIROCDS)
-- post: viene sollevata un'eccezione
--
-- pre-post-name: cancellazione non possibile
-- pre: la cancellazione fisica del documento contabile ha sollevato un errore generico
-- post: viene sollevata un'eccezione
--
-- pre-post-name: richiesta di cancelllazione fisica di un documento contabile
-- pre: Nessun'altra precondizione verificata
-- post: il documento viene cancellato
--
-- Parametri:
-- aObbNew (aAccNew) -> il documento contabile da cancellare
 procedure cancellaFisicamente(aObbNew IN OUT obbligazione%rowtype,aUser varchar2, aTSNow date);
 procedure cancellaFisicamente(aAccNew IN OUT accertamento%rowtype,aUser varchar2, aTSNow date);

-- deriportoEsNextDocAmm
--
-- Riporto dall'esercizio successivo di Documenti Amministrativi
--
-- pre-post-name: documenti non disponibili
-- pre: non ci sono documenti disponibili per l'operazione di riporto dall'esercizio successivo.
-- post: viene sollevata un'eccezione
--
-- pre-post-name: esercizio di scrivania non valido
-- pre: l'esercizio del Documento contabile associato al documento amministrativo Ã¨ uguale all'esercizio di scrivania
-- post: viene sollevata un'eccezione
--
-- pre-post-name: esercizio di scrivania ed esercizio del documento non compatibili
-- pre: l'esercizio del Documento contabile associato al documento Ã¨ diverso dall'esercizio di scrivania-1
-- post: viene sollevata un'eccezione
--
-- pre-post-name: Riporto dall'esercizio successivo di Documenti Amministrativi
-- pre: Nessun'altra precondizione verificata
-- post: cicla su obbligazioni ed Accertamenti associati ai documenti ammnnistrativi trovati ed effettua il riporto dall'esercizio successivo
--	(v. deriportoEsNextObb/deriportoEsNextAcc).
--
-- Parametri:
-- aEsScrivania -> esercizio di riferimento
-- aTiDoc -> Tipo Documento Amministrativo
-- aCdCdsDoc -> Cds parte della chiave del Documento Amminstrativo
-- aEsDoc -> Esercizio del Documento Amministrativo
-- aCdUoDoc -> UnitÃ  Organizzativa del Documento Amministrativo
-- aPgDoc -> Progressivo del Documento Amministrativo
-- aUser -> utente che effettua la modifica

 procedure deriportoEsNextDocAmm(aEsScrivania number,
 		   aTiDoc varchar2,
		   aCdCdsDoc varchar2,
		   aEsDoc number,
		   aCdUoDoc varchar2,
		   aPgDoc number,
		   aUser varchar2);

-- riportoEsNextDocAmm
-- -------------------
-- pre-post name: riporto esercizio successivo doc cont collegati a doc amm - non esistono
--                doc cont riportabili
-- pre: nessuno dei documenti contabili collegati al documento amministrativo puÃ² essere
--      riportato all'esercizio successivo
-- post: viene sollevato un errore all'utente di riporto impossibile
--
-- pre-post name: riporto esercizio successivo doc cont collegati a doc amm - esercizio di
--                scrivania non valido
-- pre: l'esercizio di scrivania dell'operazione Ã¨ il successivo rispetto all'esercizio
--      del documento contabile
-- post: viene sollevato un errore
--
-- pre-post name: riporto esercizio successivo doc cont collegati a doc amm - incompatibilitÃ 
--                esercizio di scrivania e esercizio documento contabile
-- pre: l'esercizio di scrivania Ã¨ diverso dall'esercizio del documento contabile
-- post: viene sollevato un errore
--
-- pre-post name: riporto esercizio successivo doc cont collegati a doc amm
-- pre: nessuna delle precondizioni precedenti
-- post: la procedura cicla sui documenti contabili di spesa, o di entrata se non esistono
--       i primi, e riporta ciascuno di essi all'esercizio successivo
--       (vd. pre-post "riporto accertamento/obbligazione all'esercizio successivo")
--
-- Parametri:
-- aEsScrivania -> esercizio di scrivania da cui si effettua il ribaltamento
-- aTiDoc -> tipo documento amministrativo
-- aCdCdsDoc -> cds del documento amministrativo
-- aEsDoc -> esercizio del documento amministrativo
-- aCdUoDoc -> unitÃ  organizzativa del documento amministrativo
-- aPgDoc -> numero progressivo del documento amministrativo
-- aUser -> utente che effettua il riporto
 procedure riportoEsNextDocAmm(aEsScrivania number,
 		   aTiDoc varchar2,
		   aCdCdsDoc varchar2,
		   aEsDoc number,
		   aCdUoDoc varchar2,
		   aPgDoc number,
		   aUser varchar2);

Procedure ripPgiroCdsEntrambe(
  aObb            obbligazione%Rowtype,
  aObbScad        obbligazione_scadenzario%Rowtype,
  aObbScadVoce    obbligazione_scad_voce%Rowtype,
  aAcc            accertamento%Rowtype,
  aAccScad        accertamento_scadenzario%Rowtype,
  aAccScadVoce    accertamento_scad_voce%Rowtype,
  aElementoVoceE  elemento_voce%Rowtype,
  aElementoVoceS  elemento_voce%Rowtype,
  aTi_Origine     CHAR,
  aTSNow          DATE,
  aUser           VARCHAR2,
  aObbNew         Out obbligazione%Rowtype,
  aAccNew         Out accertamento%Rowtype);


end;
