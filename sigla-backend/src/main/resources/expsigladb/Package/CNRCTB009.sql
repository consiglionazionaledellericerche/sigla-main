--------------------------------------------------------
--  DDL for Package CNRCTB009
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRCTB009" as
--
-- CNRCTB009 - Package di gestione della chiusura dell'esercizio contabile
-- Date: 12/07/2006
-- Version: 1.9
--
-- Gestisce le verifiche da effettuarsi alla chiusura dell'esercizio contabile
--
-- Dependency:
--
-- History:
--
-- Date: 16/07/2003
-- Version: 1.0
-- Creazione
--
-- Date: 16/07/2003
-- Version: 1.1
-- Modifica mandati/reversali la where su cd_cds_origine invece che su cd_Cds
--
-- Date: 02/09/2003
-- Version: 1.2
-- Eliminata condizione where su esercizio quando si estrae  la liquidazione IVA
--
-- Date: 03/09/2003
-- Version: 1.3
-- Inserito controllo su PDG_PREVENTIVO, PDG_AGGREGATI e BILANCIO_PREVENTIVO
--
-- Date: 23/01/2004
-- Version: 1.4
-- Inseriti controlli avanzo cassa e martello di disponivilita cassa 999
-- Inseriti controlli avanzo cassa per tutti i cds <> 999
--
-- Date: 09/02/2004
-- Version: 1.5
-- Modifiche sostanziali alla gestione dell'avanzo di cassa/competenza CNR e cassa CDS in chiusura esercizio
--
-- Date: 10/02/2004
-- Version: 1.6
-- Aggiunte pre-post condizioni
--
-- Date: 06/09/2004
-- Version: 1.7
-- Fix errori CINECA:
-- - 832 Il sistema attualmente controlla, in chiusura di esercizio, che una uo creata nell'esercizio successivo
--   (Es. 2004) abbia la liquidazione iva di dicembre 2003.
-- - 833 Il sistema attualmente controlla, in chiusura di esercizio, che una lettera di pagamento legata ad un
--   documento annullato sia associata a sospeso.
--
-- Date: 08/10/2004
-- Version: 1.8
-- Fix errore in chiusura esercizio 999: errato controllo chiusura altri esercizi finanziari
--
-- Date: 12/07/2006
-- Version: 1.9
-- Gestione Impegni/Accertamenti Residui:
-- aggiornata la funzione per tener conto anche del campo Esercizio Originale Impegno/Accertamento
--
-- Constants:
--
gENTE CONSTANT CHAR(1):='C';
gNONENTE CONSTANT CHAR(1):='D';

gENTRATA CONSTANT CHAR(1):='E';
gSPESA CONSTANT CHAR(1):='S';

gCOMPETENZA CONSTANT CHAR(1):='C';

gPKCONF CONSTANT VARCHAR2(50):='VOCEF_SPECIALE';
gSKCONFCNR_ENTRATA CONSTANT VARCHAR2(100):='AVANZO_E_CNR';
gSKCONFCNR_SPESA CONSTANT VARCHAR2(100):='AVANZO_S_CNR';
gSKCONFCDS_ENTRATA CONSTANT VARCHAR2(100):='AVANZO_E_CDS';
gSKCONFCDS_SPESA CONSTANT VARCHAR2(100):='AVANZO_S_CDS';
--
-- Functions e Procedures:
--
-- La procedura verifica se l'esercizio contabile del cds può essere
-- chiuso. Solleva eccezione ibmerr001.raise_application_error() nel caso
-- una delle condizioni non sia verificata.
-- Inoltre la procedura controllo l'avanzo di amministrazione e cassa ed aggiorna la cassa iniziale in esercizio
-- successivo a quello specificato
--
-- pre-post-name: Controlli di chiudibilità
-- pre: una delle seguenti precondizioni non è verificata
--      Se il cds è ente e non risulta che gli esercizi di tutti i suoi cds siano chiusi
--      allora viene sollevata un eccezione
--
--      Controlliamo se tutti i mandati del cds in esame risultano riscontrati
--      un mandato risulta riscontrato se ad esso è legato un sospeso, ma ciò
--      implica che lo stato del mandato sia P = Pagato
--      Se risulta che un mandato prorio o verso il CNR non sia riscontrato
--      allora si solleva un eccezione
--
--      Controlliamo se tutte le reversali del cds in esame risultano riscontrate
--      una reversale risulta riscontrata se ad essa è legato un sospeso, ma ciò
--      implica che lo stato della reversale sia I = Incassata
--      Se risulta che una reversale proria o verso il CNR non sia riscontrata
--      allora si solleva un eccezione
--
--      Controlliamo che se i fondi economali sono aperti
--      Se esistono solleviamo eccezione
--
--      Controlliamo che tutte le obbligazioni non ancora riportate siano legate
--      a documenti amministrativi e che inoltre risultino riscontrate
--      un obbligazione risulta riscontrata se associata ad un mandato in stato Pagato
--
--      Controlliamo che tutte gli accertamenti non ancora riportati siano legati
--      a documenti amministrativi e che inoltre risultino riscontrati
--      un accertamento risulta riscontrato se associato ad un mandato in stato Pagato
--
--      Controlliamo che se esistono sospesi figli non stornati e non associati
--      completamente a mandati/reversali
--      Se esistono solleviamo eccezione
--
--      Controlliamo se esistano lettere pagamento estero che non hanno sospeso associato. Il controllo
--      esclude quelle associate a documenti amministrativi annullati.
--      Se esistono solleviamo eccezione
--
--      Controlliamo che se gli invetari sono aperti
--      Se esistono solleviamo eccezione
--
--      Controlliamo, per un dato esercizio, che tutte le liquidazioni iva (dicembre) siano in stato definitivo
--      per le unità organizzative valide.
--      Se non esistono liquidazioni definitive solleviamo eccezione.
--
--      Controlliamo che tutti i piani di gestione del cds
--      devono essere chiusi definitivamente stato = F (in questo caso
--      dobbiamo considerare tutti i piani di gestione
--      relativi ai tutti CDR legati al nostro cds )
--
--      Controlliamo che tutti i piani di gestione aggregati del cds
--      devono essere chiusi definitivamente stato = B (in questo caso
--      dobbiamo considerare tutti i piani di gestione aggregati
--      relativi ai soli CDR di primo livello legati al nostro cds)
--
--      Controlliamo che il bilancio preventivo del CDS
--      sia stato approvato.
-- post: Vinene sollevata un'eccezione

-- Controllo e gestione dell'avanzo di amministrazione/cassa del CNR e cassa del CDS
--
-- pre-post-name: (Solo CNR) avanzo di amministrazione corrente non trovato
-- pre: la lettura sulla vista v_avanzo_amm_cnr non resistuisce righe
-- post: viene sollevata un'eccezione
--
-- pre-post-name: (Solo CNR) avanzo di cassa non trovato
-- pre: la lettura sulla vista v_disp_cassa_cnr non resistuisce righe
-- post: viene sollevata un'eccezione
--
-- pre-post-name: (Solo CNR) esercizio in chiusura o successivo non definito per Ente
-- pre: non esiste in tabella esercizio la riga per l'Ente per l'esercizio in chiusura o il successivo
-- post: viene sollevata un'eccezione
--
-- pre-post-name: (Solo CNR) capitoli di avanzo e disavanzo CNR non trovati o non corretti in CONFIGURAZIONE_CNR
-- pre: i capitoli dell'avanzo o disavanzo non sono stati trovati o non sono corretti in configurazione_CNR
-- post: viene sollevata un'eccezione
--
-- pre-post-name: (Solo CNR) log delle discrepanzae di avanzo di amministrazione
-- pre: l'avanzo corrente viene confrontato con quello presunto letto dal capitolo di avanzo o disavanzo del preventivo
--      nell'esercizio successivo a quello in chiusura; gli importi sono diversi
-- post: viene aperto un log tramite meccanismo di logging che segnala la discrepanza
--
-- pre-post-name: (Solo CDS) avanzo di cassa non trovato
-- pre: la lettura sulla vista v_avanzo_Cassa_cds non resistuisce righe
-- post: viene sollevata un'eccezione
--
-- pre-post-name: (Solo CDS) esercizio in chiusura o successivo non definito per Cds in processo
-- pre: non esiste in tabella esercizio la riga per il Cds in processo per l'esercizio in chiusura o il successivo
-- post: viene sollevata un'eccezione
--
-- pre-post-name: (Solo CDS) capitoli di avanzo e disavanzo CDS non trovati o non corretti in CONFIGURAZIONE_CNR
-- pre: i capitoli dell'avanzo o disavanzo non sono stati trovati o non sono corretti in configurazione_CNR
-- post: viene sollevata un'eccezione
--
-- pre-post-name: (Solo CDS) log delle discrepanzae di avanzo di cassa
-- pre: l'avanzo di cassa corrente del Cds viene confrontato con quello presunto letto dal
-- capitolo di avanzo o disavanzo opportuno del preventivo del Cds nell'esercizio successivo
-- a quello in chiusura; gli importi sono diversi
-- post: viene aperto un log tramite meccanismo di logging che segnala la discrepanza
--
-- pre-post-name: (CNR e CDS) aggiornamento della cassa iniziale in esercizio successivo
-- pre: aggiornamento della cassa iniziale esercizio successivo a quello in chiusura
-- post: l'avanzo di cassa corrente letto in precedenza viene utilizzato per aggiornare (sovrascrittura) la cassa iniziale dell'esercizio
--      successivo a quello in chiusura

procedure checkEsercizioChiusura(aEs number, aCds varchar2, aUser varchar2);

-- procedure gestioneAvanzo(aCds varchar2, aEs number, aUser varchar2);

end;
