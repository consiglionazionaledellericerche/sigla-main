--------------------------------------------------------
--  DDL for Package CNRCTB206
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRCTB206" as
--
-- CNRCTB206 - Package scritture COEP
-- Date: 16/11/2004
-- Version: 1.17
--
-- Package per la gestione delle scritture COEP di CHIUSURA
--
-- Dependency: CNRCTB 200/204 IBMERR 001
--
-- History:
--
-- Date: 12/07/2003
-- Version: 1.0
-- Creazione
--
-- Date: 13/07/2003
-- Version: 1.1
-- Aggiunte pre-post condizioni
--
-- Date: 14/07/2003
-- Version: 1.2
-- Completamento chiusura
--
-- Date: 15/07/2003
-- Version: 1.3
-- Spostato annullamento documenti in esercizio chiuso
--
-- Date: 16/07/2003
-- Version: 1.4
-- Modifica metodo trova conto anag
--
-- Date: 31/07/2003
-- Version: 1.5
-- Completamento ammortamento con registrazione analitica
--
-- Date: 01/08/2003
-- Version: 1.6
-- Completamento ammortamento con registrazione analitica
--
-- Date: 04/08/2003
-- Version: 1.7
-- Test batch chiusura e annullamento chiusura
-- Uso rbsbig
--
-- Date: 06/08/2003
-- Version: 1.8
-- introduzione metodi commitRbsBig/rollbackRbsBig
--
-- Date: 08/08/2003
-- Version: 1.9
-- Fix su registrazione ammortamento
--
-- Date: 08/08/2003
-- Version: 1.10
-- Introdotto ammortamento finanziario in chiusura economica
-- nuova modalita di gestione della chiusura definitiva
--
-- Date: 11/09/2003
-- Version: 1.11
-- Razionalizzazione controlli su Risconti
--
-- Date: 19/02/2004
-- Version: 1.12
-- Modifcata gestione ratei parte 2 per gestione delle fatture passive e note di credito
--
-- Date: 07/05/2004
-- Version: 1.13
-- Fix scrittura analitica ammortamento
--
-- Date: 12/05/2004
-- Version: 1.14
-- Fix eliminazione delle scritture di ammortamento COGE
--
-- Date: 23/09/2004
-- Version: 1.15
-- Revisione gestione job chiusura coge: introduzione semaforo statico per la chiusura definitiva di un CDS
-- Gestita la possibilità di chiudere definitivamente la COGE di un CDS singolo
-- indipendentemente dagli altri
-- Fix errore utcr/duva/dacr in metodi chiusura su movimenti generati
-- Fix errore di annullamento economica dell'ammortamento
--
-- Date: 25/09/2004
-- Version: 1.16
-- Test nuovo processo
--
-- Date: 16/11/2004
-- Version: 1.17
-- I dati di utilizzatore sono sul bene padre (progressivo=0)
--
-- Constants:

TIPO_LOG_JOB_CHIUSURA_COGE CONSTANT VARCHAR2(20) := 'CHIUSURA_COGE00';

-- Costanti relative alla dismissione del bene durevole

-- Functions e Procedures:

 -- ===================================================
 -- Chiusura economica
 -- ===================================================

-- pre-post-name: Controllo parametro modalita
-- pre: Il flag modalitaProva non e in ('Y','N')
-- post: viene sollevata un'eccezione
--
-- pre-post-name: Controllo parametro modalita
-- pre: Il Cds specificato e null
-- post: viene sollevata un'eccezione
--
-- pre-post-name: Chiusura definitiva di un CDS possibile solo ad esercizio finanziario chiuso per tutti i cds
-- pre: Esercizio finanziario non chiuso per almeno un CDS e modalitaProva='N'
-- post: viene effettuata una chiusura in prova per tutti i CDS impattati dalla procedura
--
-- pre-post-name: Esercizio n+1 non aperto il CDS in processo
-- pre: Per un dato CDS in processo l'esercizio successivo a quello in chiusura non e aperto
-- post: viene sollevata un'eccezione che non blocca il processo su altri CDS
--
-- pre-post-name: Chiusura gia eseguita e non annullata
-- pre: Per il CDS specificato e gia stata effettuata una prova di chiusura non annullata (record in tabella CHIUSURA_COEP)
-- post: viene sollevata un'eccezione che non blocca il processo su altri CDS
--
-- pre-post-name: Chiusura economica
-- pre: L'utente richiede la chiusura economica dell'esercizio specificato
-- post: Per ogni CDS specificato vengono eseguite in sequenza le seguenti operazioni:
--       1.Ammortamenti (finanziari ed economici)
--       2.Risconti
--       3.Risconti parte 2
--       4.Chiusura del conto economico
--       5.Chiusura dello stato patrimoniale
--       6.Rilevazione utile perdita d'esercizio
--       7.Riapertura dei conti in nuovo esercizio
--       8.Ratei parte 2
--       Viene aggiornato/creato il record in tabella CHIUSURA_COEP con stato P (prova)
--       per il cds in processo
--      Al termine del processo se la modalita scelta e definitiva, e per ogni cds l'esercizio finanziario e chiuso
--      fiene resa definitiva anche la chiusura economica
--
--
-- Parametri:
-- job,pg_exec, next_date -> interfaccia job
-- aEs -> Esercizio in chiusura
-- modalitaProva -> Y => prova di chiusura N => chiusura definitiva
-- aCdCds -> Cds, * => tutti i CDS
-- aUser -> Utente che effettua la variazione
-- aTSNow -> Timestamp modifica

 procedure job_chiusuraCogeCoan(job number, pg_exec number, next_date date, modalitaProva char, aEs number, aCdCds varchar2);

-- pre-post-name: Controllo parametro modalita
-- pre: Il Cds specificato e null
-- post: viene sollevata un'eccezione
--
-- pre-post-name: Chiusura in prova non ancora eseguita
-- pre: la chiusura in prova non e stata ancora eseguita per il cds specificato
-- post: viene sollevata un'eccezione
--
-- pre-post-name: Chiusura economica definitiva
-- pre: L'esercizio economico e chiuso definitivamente per CDS
-- post: viene sollevata un'eccezione
--
-- pre-post-name: Annullamento chiusura economica
-- pre: L'utente richiede l'annullamento della chiusura economica per l'esercizio specificato
-- post: Vengono eliminate fisicamente tutte le scritture corrispondenti eseguite in chiusura
--       Viene aggiornato ad A (annullato) lo stato di chiusura dell'esercizio economico per il cds
--
-- Parametri:
-- aEs -> Esercizio in chiusura
-- aCdCds -> Codice del centro di spesa, se * => tutti i cds
-- aUser -> Utente che effettua la variazione
-- aTSNow -> Timestamp modifica

 procedure job_annullaChiusuraCOGECOAN(job number, pg_exec number, next_date date, aEs number, aCdCds varchar2);

 -- ===================================================
 -- Registrazione dell ammortamento in economica
 -- ===================================================

 -- pre-post-name: Scrittura di ammortamento gia eseguita
 -- pre: Esiste almeno una scrittura nell'esercizio specificato con causale ammortamento
 -- post: viene sollevata un'eccezione

 -- pre-post-name: Registrazione economica dell'ammortamento
 -- pre: E' richiesta l'effettuazione dell'ammortamento in economica
 -- post: Le scritture di ammortamento vengono effetuate per cds,uo,inventario,categoria economica del bene.
 --       Per ogni bene processato viene determinata la categoria (padre del gruppo del bene).
 --       Tramite la tabella di associazione tra categoria economica e voce ep (CATEGORIA_GRUPPO_VOCE_EP) vengono
 --       determinati il conto principale e quello patrimoniale del fondo ammortamento.
 --       La scrittura ha sempre in DARE il conto principale per l'importo di ammortamento dell'anno letto
 --       da tabella AMMORTAMENTO_BENE_INV.
 --       I movimenti hanno competenza economica da 31/12/<anno di chiusura> a 31/12/<anno di chiusura>
 --       La scrittura ha data di contabilizzazione 31/12/<anno di chiusura>
 --       La causale coge e AMMORTAMENTO
 --       L'origine della scrittura e CHIUSURA
 --       Nel campo CD_COMP_DOCUMENTO vengono indicati pg_inventario e codice della categoria inventariale
 --       Viene registrata la scrittura analitica dell'ammortamento
 --        Per ogni bene ammortizzato appartenente alla categoria considerata
 --         Viene costruita una scrittura analitica con moviementi per ogni cdr/la in invent_utilizzatori_la (presente SOLO sul bene padre, progressivo=0)
 --         L'importo dei movimenti e ottenuto considerando il frazionamento dell'importo ammortizzato per la percentuale di utilizzo (CDR/LA)
 --         I rotti vengono sottratti o aggiunti all'ultimo movimento
 --         La scrittura ha ORIGINE_SCRITTURA = CHIUSURA
 --
 -- Parametri:
 -- aEs -> Esercizio in chiusura
 -- aCdCds -> Centro di spesa
 -- aUser -> Utente che effettua la variazione
 -- aTSNow -> Timestamp modifica

 procedure regAmmortamentoCOGE(modalitaProva char, aEs number, aCdCds varchar2, aUser varchar2, aTSNow date);


 -- =============================================================
 -- Registrazione dei ratei parte 2
 -- =============================================================

 -- pre-post-name: Ratei parte 2 gia eseguiti
 -- pre: Vengono ricercate e trovate le scritture di rateo parte 2 per il doc. amministrativo
 -- post: Viene sollevata un'eccezione

 -- pre-post-name: Registrazione economica dei ratei parte 2
 -- pre: E' richiesta la riapertura del debito credito verso fornitore nell'esercizio succ. a quello in chiusura
 -- post: Se la scrittura di rateo parte 2 per il documento in processo e gia stata eseguita
 --         tale scrittura viene recuperata e stornata
 --       Vengono recuparate le scritture di rateo parte 1 effettuate alla registrazione del documento
 --       nell'esercizio in chiusura.
 --       Tali scritture vengono riemesse nel nuovo esercizio con le seguenti sostituzioni:
 --       Il movimento di contropartita terzo (identificato da fl_mov_terzo='Y' in tabella movimento_coge)
 --       viene ribaltato di sezione a parita di conto (ratei attivi/passivi). Nel caso il doc. amministrativo sia una
 --       fattura attiva o passiva (anche nota di credito/debito) con IVA, l'iva viene registrata nel nuovo esercizio.
 --       La scrittura e chiusa sul terzo debitore o creditore per il delta.
 --       I movimenti hanno competenza economica = a quella del mov. di origine con fl_mov_terzo='Y'
 --       La scrittura ha data di contabilizzazione <data odierna>
 --       La causale coge e RATEI_P2
 --       L'origine della scrittura e CHIUSURA
 --       La scrittura e di tipo P (Prima)
 --
-- Parametri:
-- aDocTst -> Documento registrato in esercizio succ. a quello in chiusura e cono competenza nell'esercizio precedente
-- aUser -> Utente che effettua la variazione
-- aTSNow -> Timestamp modifica

 procedure regDocAmmRateiParte2COGE(modalitaProva char, aDocTst V_DOC_AMM_COGE_TSTA%rowtype, aUser varchar2, aTSNow date);

 -- =============================================================
 -- Registra i risconti
 -- =============================================================

 -- pre-post-name: Risconti (1 o 2) gia eseguiti
 -- pre: Vengono ricercate e trovate le scritture di risconto per il doc. amministrativo
 -- post: Viene sollevata un'eccezione

 -- pre-post-name: Scrittura prima del documento non trovata
 -- pre: Viene cercata e non trovata la scrittura prima del documento
 -- post: Viene sollevata un'eccezione

 -- pre-post-name: Registrazione economica dei risconti
 -- pre: E' richiesta la registrazione dei risconti in esercizio in chiusura per il doc. specificato
 -- post: Vengono recuperate le scritture economiche prime del documento
 --        Per ogni scrittura viene costruita una nuova scrittura nell'esercizio in chiusura con le seguenti caratteristiche:
 --         -> Per ogni movimento della scrittura di origine su conto EEC o EER (ecnomico di costo/ricavo), con fine competenza coge
 --            in esercizio successivo a quello in chiusura:
 --            --> viene costruito un movimento con le seguenti caratteristiche
 --             -> sezione opposta a quella del movimento di origine
 --             -> importo calcolato sulla base di quello di origine frazionato sul periodo di competenza
 --                in esercizio successivo del movimento di origine (periodo di risconto)
 --                  esempio: periodo origine 20/10/2003 31/03/2004 -> periodo di risconto: 01/01/2004 31/03/2004
 --                  esempio: periodo origine 20/01/2004 31/03/2004 -> periodo di risconto: 20/01/2004 31/03/2004
 --             -> conto del movimento di origine
 --             -> competenza economica = periodo di risconto
 --        La scrittura viene chiusa in contropartita sul conto dei RISCONTI della sezione opportuna (ATTIVI se la scrittura si chiude in DARE altrimenti PASSIVI)
 --        Una scrittura analoga ribaltata in sezione viene registrata nel nuovo esercizio per la riapertura dei risconti sui conti di costo opportuno
 --        Nella nuova scrittura la data di contabilizzazione e impostata a 0101<esercizio nuovo>
-- Parametri:
-- aDocTst -> Documento registrato in esercizio succ. a quello in chiusura e cono competenza nell'esercizio precedente
-- aUser -> Utente che effettua la variazione
-- aTSNow -> Timestamp modifica

 procedure regRiscontiCOGE(modalitaProva char, aDocTst V_DOC_AMM_COGE_TSTA%rowtype, aUser varchar2, aTSNow date);

 -- ================================
 -- Chiusura del conto economico
 -- ================================

 -- pre-post-name: Chiusura del conto economico
 -- pre: E' richiesta la chiusura del conto economico per il cds specificato
 -- post: Per ogni Uo/terzo presente nei saldi COGE del cds specificato
 --        Per ogni saldo di conto economico di costo o ricavo (EEC,EER)
 --         Viene creato un movimento per il valore del saldo del conto sulla sezione opposta a quella del saldo
 --         Viene creato un movimento per il valore del saldo del conto sulla sezione opposta e sul conto CONTO_ECONOMICO
 --        La scrittura ha data di contabilizzazione 31/12/<anno di chiusura>
 --        La causale coge e CHIUSURA_CONTO_ECONOMICO
 --        L'origine della scrittura e CHIUSURA
 --        Nel campo CD_COMP_DOCUMENTO vengono indicati cds e uo chiusi con la scrittura
 --
 -- Parametri:
 -- aEs -> esercizio in chiusura
 -- aCdCds -> Codice del cds che viene chiuso
 -- aUser -> Utente che effettua la variazione
 -- aTSNow -> Timestamp modifica

 procedure chiusuraContoEconomicoCOGE(modalitaProva char, aEs number, aCdCds varchar2, aUser varchar2, aTSNow date);


 -- =====================================
 -- Riapertura dello stato patrimoniale
 -- =====================================

 -- pre-post-name: Riapertura dello stato patrimoniale
 -- pre: E' richiesta la riapertura dello stato patrimoniale per il cds specificato
 -- post: Vengono estratte tutte le scritture di chiusura dello stato patrimoniale del cds spec.
 --        Per ogni scrittura estratta viene creata una scrittura corrispondente nel nuovo esercizio
 --         con sezioni ribaltate dei movimenti originali
 --          La scrittura ha data di contabilizzazione 01/01/<anno di riapertura>
 --          La causale coge e RIAPERTURA_CONTI
 --          L'origine della scrittura e CHIUSURA

 --
 -- Parametri:
 -- aEsNext -> esercizio successivo a quello in chiusura
 -- aCdCds -> Codice del cds che viene chiuso
 -- aUser -> Utente che effettua la variazione
 -- aTSNow -> Timestamp modifica

 procedure riaperturaStPatrimonialeCOGE(modalitaProva char, aEsNext number, aCdCds varchar2, aUser varchar2, aTSNow date);

 -- ==================================
 -- Chiusura dello stato patrimoniale
 -- ==================================

 -- pre-post-name: Chiusura dello stato patrimoniale
 -- pre: E' richiesta la chiusura dello stato patrimoniale per il cds specificato
 -- post: Per ogni Uo/terzo presente nei saldi COGE
 --        Per ogni saldo di conto CHE NON SIA economico di costo o ricavo (EEC,EER)
 --         Viene creato un movimento per il valore del saldo del conto sulla sezione opposta a quella del saldo
 --         Viene creato un movimento per il valore del saldo del conto sulla sezione opposta e sul conto STATO_PATRIMONIALE
 --        La scrittura ha data di contabilizzazione 31/12/<anno di chiusura>
 --       La causale coge e CHIUSURA_ST_PATRIMONIALE
 --       L'origine della scrittura e CHIUSURA
 --       Nel campo CD_COMP_DOCUMENTO vengono indicati cds e uo e terzo chiusi con la scrittura
 --
 --
 -- Parametri:
 -- aEs -> esercizio in cui si annulla il documento proveniente da esercizio chiuso
 -- aCdCds -> Codice del cds che viene chiuso
 -- aUser -> Utente che effettua la variazione
 -- aTSNow -> Timestamp modifica

 procedure chiusuraStatoPatrimonialeCOGE(modalitaProva char, aEs number, aCdCds varchar2, aUser varchar2, aTSNow date);

 -- ==================================
 -- Determinazione utile perdita
 -- ==================================

 -- pre-post-name: E' richiesta la det. dell'utile o della perdita
 -- pre: E' richiesta la la det. dell'utile o della perdita per il cds specificato
 -- post: Per ogni UO/Terzo presente nei saldi COGE
 --        Viene girato il saldo del CONTO_ECONOMICO su PROFITTI_PERDITE
 --        Viene girato il saldo dello STATO_PATRIMONIALE su PROFITTI_PERDITE
 --        Le due scritture generate hanno data di contabilizzazione 31/12/<anno di chiusura>
 --       La causale coge e RILEVAZIONE_UTILE_PERDITA
 --       L'origine della scrittura e CHIUSURA
 --       Nel campo CD_COMP_DOCUMENTO vengono indicati cds e uo e terzo chiusi con la scrittura
 --
 --
 -- Parametri:
 -- aEs -> esercizio in cui si annulla il documento proveniente da esercizio chiuso
 -- aCdCds -> Codice del cds che viene chiuso
 -- aUser -> Utente che effettua la variazione
 -- aTSNow -> Timestamp modifica

 procedure rilevazioneUtilePerditaCOGE(modalitaProva char, aEs number, aCdCds varchar2, aUser varchar2, aTSNow date);

 -- Operazioni di controllo per chiusura economica definitiva del CDS
 -- L'operazione viene fatta in una transazione
 --
 -- 1. Non devono esserci scritture con numerazione superiore alla prima scrittura generata in chiusura

 procedure checkChiudibilitaDef(aEs number, aCdCds varchar2);

end;
