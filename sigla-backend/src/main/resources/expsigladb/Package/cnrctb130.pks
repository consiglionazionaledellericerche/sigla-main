CREATE OR REPLACE PACKAGE CNRCTB130 AS
--
-- CNRCTB130 - Gestione applicativa del fondo economale
--
-- Date: 13/07/2006
-- Version: 3.23
--
-- Comments:
--
-- Dependecy: CNRCTB 015/018/037/038/080/100/110/560 IBMERR 001
--
-- History:
--
-- Date: 14/05/2002
-- Version: 1.0
-- Creazione
--
-- Date: 15/05/2002
-- Version: 1.1
-- Completamento reintegro
--
-- Date: 16/05/2002
-- Version: 1.2
-- Gestione valuta default su generico automatico
--
-- Date: 17/05/2002
-- Version: 1.3
-- Completamento
--
-- Date: 20/05/2002
-- Version: 1.4
-- Fix errore in specifica collegamento a mandato in fondo spesa
--
-- Date: 21/05/2002
-- Version: 1.5
-- Fix
--
-- Date: 23/05/2002
-- Version: 1.6
-- Aggiunto metodo fake di chiusura del fondo
--
-- Date: 26/05/2002
-- Version: 1.8
-- Riorganizzazione generazione doc generico su spese non documentate:
-- viene genrato un doc generico per ogni obbligazione che eredita come terzo quello dell'obbligazione e come
-- banca e modalit? di pagamento quelle dell'economo.
-- Per ogni scadenza di obbligazione collegata al gruppo da reintegrare, viene creata una riga di documento generico.
--
-- Date: 31/05/2002
-- Version: 1.9
-- Fix errori
--
-- Date: 03/06/2002
-- Version: 2.0
-- Recupero della modalit? di pagamento (e banca) con dacr pi? elevato per il terzo specificato in obbligazione
--
-- Date: 03/06/2002
-- Version: 2.1
-- Sistemato errore di cambiamento pg_obbligazione
--
-- Date: 03/06/2002
-- Version: 2.2
-- Aggiornato l'importo associato a doc contabili in obbligazione scadenza, per le spese non documentate
--
-- Date: 04/06/2002
-- Version: 2.3
-- Data al posto di timestamp in data registrazione del documento generico
--
-- Date: 04/06/2002
-- Version: 2.4
-- Recupero dati aggiuntivi terzo in generico + eliminazione importo associato a doc cont in scadenza (viene fatto all'inseimento del mandato)
--
-- Date: 05/06/2002
-- Version: 2.5
-- Fix set di pg_accertamento_scadenzario errato in dettaglio egenrico
--
-- Date: 06/06/2002
-- Version: 2.6
-- Gestione annulamento reintegro
--
-- Date: 17/06/2002
-- Version: 2.7
-- Aggiornamento documentazione
--
-- Date: 20/06/2002
-- Version: 2.8
-- Recupero delle modalit? di pagamento del terzo OBB pi? recenti con metodo di servizio del package 80
--
-- Date: 21/06/2002
-- Version: 2.9
-- Fix date, Fix ti_associato_man_rev
--
-- Date: 01/07/2002
-- Version: 3.0
-- Il generico di reintegro ? escluso da COEP/COAN
--
-- Date: 08/07/2002
-- Version: 3.1
-- Fix controllo su importo scadenza = importo collegato a doc amministrativi
--
-- Date: 08/07/2002
-- Version: 3.2
-- Contabilizzazione del compenso tramite fondo
--
-- Date: 17/07/2002
-- Version: 3.3
-- Eliminato to_date su date field
--
-- Date: 18/07/2002
-- Version: 3.4
-- Aggiornamento della documentazione
--
-- Date: 03/09/2002
-- Version: 3.5
-- Fix su aggiornamento spese post reintegro
--
-- Date: 10/11/2002
-- Version: 3.6
-- Sistemazione periodo economico troncando sysdate a DATE
--
-- Date: 24/11/2002
-- Version: 3.7
-- Corretto aggiornamento del mandato per reintegro fondo su documento compenso
--
-- Date: 16/12/2002
-- Version: 3.8
-- Aggiunta gestione chiusura fondo economale
--
-- Date: 07/01/2003
-- Version: 3.9
-- fix sulll'annullamento di un reintegro di un fondo chiuso
--
-- Date: 27/05/2003
-- Version: 3.10
-- fix sull< chiusura di un fondo con im_residuo = 0 (non deve essere creata la reversale di chisura)
--
-- Date: 29/05/2003
-- Version: 3.11
-- fix sull< chiusura di un fondo: se im_ammontare_fondo = im_totale_reintegrise non veniva erroneamente generata
-- la reversale di chiusura del fondo
--
-- Date: 23/06/2003
-- Version: 3.12
-- Revisione del package per gestione chiusura Fondo Economale per chiusura contabile.
-- Le operazioni di chiusura delle spese non reintegrate e del Fondo Economale vengono separate.
-- Per questo, la procedura ChiudiFondo effettua SOLO la chiusura del Fondo Economale, mentre per le spese
-- ? stata aggiunta la procedura ChiudiSpese, (v.).
-- Aggiunte, inoltre, procedure di controllo e servizio, (v. chkAnnotazoniPGiro, creaAnnotPGiroTroncEntrata).
--
-- Date: 14/07/2003
-- Version: 3.13
-- fix sulla emissione del Mandato di Regolarizzazione: la procedura non aggiorna gli importi IM_PAGAMENTI_INCASSI su VOCE_F_SALDI_CMP.
--
-- Date: 04/08/2003
-- Version: 3.14
-- Fix problema del compilatore PL/SQL
-- Fix gestione reintegro compensi su spese in esercizio diverso da quello del compenso
--
-- Date: 06/08/2003
-- Version: 3.15
-- Fix errore aggiornamento spesa ca Y per compensi
--
-- Date: 26/08/2003
-- Version: 3.16
-- Fix errore aggiornamento Fondo Economale per chiusura: alla chiusura del F.E. viene aggiornato
-- il fl_rev_da_emettere impostandolo a 'N'.
--
-- Date: 29/08/2003
-- Version: 3.17
-- Aggiunte Pre Post conditions.
--
-- Date: 15/09/2003
-- Version: 3.18
-- Fix documentazione.
--
-- Date: 16/09/2003
-- Version: 3.19
-- Fix errore creazione righe Mandato nel reintegro delle spese documentate: se si reintegra una
-- spesa associata ad un documento riportato nell'esercizio successivo,la riga del mandato di
-- reintegro non tiene conto dell'esercizio della spesa, ma di quello del documento, che quindi, risulta
-- nell'esercizio precedente.
--
-- Date: 10/12/2003
-- Version: 3.20
-- Introdotti metodi di creazione pratiche finanziarie di apertura ed incremento del fondo economale (675)
--
-- Date: 27/01/2004
-- Version: 3.21
-- Se il mandato ? di regolarizzazione pg_banca e CD_MODALITA_PAG devono essere a null sul reintegro spese non doc.
--
-- Date: 28/01/2004
-- Version: 3.22
-- Fix errori 756 e 757
-- La reversale di regolarizzazione in chiusura del fondo ? esclusa COEP
-- I generici di reintegro sono inclusi COEP
--
-- Date: 13/07/2006
-- Version: 3.23
-- Gestione Impegni/Accertamenti Residui:
-- aggiornata la funzione per tener conto anche del campo Esercizio Originale Impegno/Accertamento
--
-- Creazione Package.
--
-- Constants
--
PROC_NAME_CHIUDI_FONDO CONSTANT VARCHAR2(100):='CNRCTB130.chiudiFondo';

ELEMENTO_VOCE_SPECIALE CONSTANT VARCHAR2(100):='ELEMENTO_VOCE_SPECIALE';
VOCE_APERTURA_FONDO_ECO CONSTANT VARCHAR2(100):='VOCE_APERTURA_FONDO_ECO';

--
-- Functions e Procedures
--
-- Reintegro spese fondo economale
--
-- L'operation effettua il reintegro di spese documentate e non del fondo economale
-- L'operazione di reintegro consiste nell'incremento della liquidit? del fondo attraverso
-- l'emissione di un mandato all'economo per la copertura (banca) delle spese sostenute per cassa
--
-- Pre-post name: L'utente richiede il reintegro di spese
-- Pre: L'utente richiede il reintegro di spese
-- Post: L'operazione viene eseguita richiamando la procedure 'vsx_reintegraChiudiSpeseFondo' e
--       specificando di generare dei mandati di pagamento
--
-- Parametri
--   pgCall -> numero di chiamata VSX determinato da applicazione JAVA
--

 PROCEDURE vsx_reintegraSpeseFondo
      (
       pgCall NUMBER
      );

-- Reintegro o chiusra spese fondo economale
--
-- L'operation effettua il reintegro di spese documentate e non del fondo economale
-- Tale operazione pu? essere richiesta dall'utente su alcune spese specifiche oppure
-- pu? essere fatta in automatico alla chiusura del fondo su tutte le spese non ancora reintegrate.
-- L'operazione di reintegro consiste nell'incremento della liquidit? del fondo attraverso
-- l'emissione di un mandato all'economo per la copertura (banca) delle spese sostenute per cassa
-- Nel caso di reintegro manuale il mandato ? un mandato di pagamento, nel caso di reintegro
-- automatico alla chiusura del fondo il mandato ? di regolarizzazione con conseguente reversale.
--
-- Pre-post name: Non tutte le spese collegate ad obbligazione
-- Pre: Esistono spese, tra quelle selezionate per il reintegro del fondo, non collegate ad
-- obbligazioni
-- Post: Viene sollevata un'eccezione
--
-- Pre-post name: Alcune spese gi? reintegrate
-- Pre: Esistono spese, tra quelle selezionate per il reintegro del fondo, gi? reintegrate
-- Post: Viene sollevata un'eccezione
--
-- Pre-post name: Scadenze non completamente coperte da spese  da reintegrare
-- Pre: Esistono scadenze di obbligazioni collegate a spese selezionate per il reintegro, non
-- completamente coperte da scadenze selezionate per il reintegro
-- Post: Viene sollevata un'eccezione
--
-- Pre-post name: Scadenze non consistenti
-- Pre: Esistono scadenze di obbligazioni collegate a spese selezionate per il reintegro con importo
-- associato a documenti amministrativi maggiore dell'importo della scadenza stessa
-- Post: Viene sollevata un'eccezione
--
-- Pre-post name: Reintegro manuale del fondo economale
-- Pre: Nessuna delle precondizioni ? verificata e il parametro aTiMandato = PAGAMENTO
-- Post:
--  Esecuzione reintegro
--    Fase 1. generazione documento generico per spese non documentate
--     f1. le spese vengono aggregate per scadenza di obbligazione collegata
--     f2. per ogni obbligazione viene creato un documento generico con n-righe pari alle scadenze
--         collegate a spese da reintegrare tale generico eredita il terzo dell'obbligazione: come modalit? e
--         termini di pagamento di tale terzo vengono recuperati i primi disponibili per tale terzo
--    Fase 2. generazione mandato di reintegro
--     f1. viene preparato un mandato associato agli n-generici delle spese non documentate + m-doc
--         amministrativi collegati a spese documentate (non compensi): tale mandato ?
--         intestato all'economo (specificato in testata del fondo economale)
--    Fase 3. Vengono generati j mandati corrispondenti alle j spese legate a compensi (ogni mandato
--            con reversali collegate)
--    Fase 4. ogni spesa reintegrata viene marcata come tale: sulla spesa del fondo viene registrato
--            il numero del mandato di reintegro che in questo modo funge da aggregatore di spese reintegrate
--    Fase 5. calcolato aTotMandato come la somma degli importi di tutti i mandati emessi per
--            reintegro delle spese selezionate, viene aggiornata la testata del fondo economale nel seguente
--            modo:
--            im_residuo_fondo=im_residuo_fondo+aTotMandato,
--            im_totale_reintegri=im_totale_reintegri+aTotMandato,
--            im_totale_spese=im_totale_spese-aTotMandato,
--
-- Pre-post name: Reintegro automatico delle spese del fondo economale alla chiusura del fondo
-- Pre: Nessuna delle precondizioni ? verificata e il parametro aTiMandato = REGOLARIZZAZIONE
-- Post:
--  Esecuzione reintegro
--    Fase 1. generazione documento generico per spese non documentate
--     f1. le spese vengono aggregate per scadenza di obbligazione collegata
--     f2. per ogni obbligazione viene creato un documento generico con n-righe pari alle scadenze
--         collegate a spese da reintegrare tale generico eredita il terzo dell'obbligazione: come modalit? e
--         termini di pagamento di tale terzo vengono recuperati i primi disponibili per tale terzo
--    Fase 2. generazione mandato di regolarizzazione per reintegro
--     f1. viene preparato un mandato di regolarizzazione associato agli n-generici delle spese
--         non documentate + m-doc amministrativi collegati a spese documentate (non compensi): tale mandato ?
--         intestato all'economo (specificato in testata del fondo economale)
--     f2. viene generata la reversale di regolarizzazione collegata al mandato (procedure 'creaReversaleregolarizzazione')
--    Fase 4. ogni spesa reintegrata viene marcata come tale: sulla spesa del fondo viene regisgtrato
--            il numero del mandato di reintegro che in questo modo funge da aggregatore di spese reintegrate
--    Fase 5. calcolato aTotMandato come la somma degli importi di tutti i mandati emessi per
--            reintegro delle spese selezionate, viene aggiornata la testata del fondo economale nel seguente
--            modo:
--             im_residuo_fondo=im_residuo_fondo+aTotMandato,
--             im_totale_reintegri=im_totale_reintegri+aTotMandato,
--             im_totale_spese=im_totale_spese-aTotMandato,
--
-- Parametri
--   pgCall -> numero di chiamata VSX
--   aTiMandato -> pu? assumetre il valore CNRCTB038.TI_MAN_PAG per il reintegro manuale
--                 delle spese del fondo oppure CNRCTB038.TI_MAN_REG per il reintegro in
--                 automatico  fatto alla chiusura del fondo


 PROCEDURE vsx_reintegraChiudiSpeseFondo
      (
       pgCall NUMBER,
	   aTiMandato VARCHAR2
      );

-- Operazione di inserimento di un record nella vista VSX_REINTEGRO_FONDO
--
-- pre-post-name: Inserimento record
-- pre: Un record di tipo Vsx_reintegro_fondo ? stato preparato
-- post: Il record ? stato inserito in tabella
-- Parametri:
--    aDest -> record da inserire


 procedure ins_VSX_REINTEGRO_FONDO
 	(
	 aDest VSX_REINTEGRO_FONDO%rowtype
	);

-- Operazione di aggiornamento del fondo in conseguenza dell'annullamento del mandato di reintegro di fondo economale
--
-- pre-post-name: Fondo economale non trovato
-- pre: Viene cercata la testata di fondo economale con spese associate al mandato di reintegro in processo e non viene trovata
-- post: Viene sollevata un'eccezione
--
-- pre-post-name: Annullamento del reintegro
-- pre:  Nessun'altra precondizione verificata
-- post:
--    Viene aggiornata la testata del fondo economale come segue:
--     im_residuo_fondo=im_residuo_fondo-aImMandato
--     im_totale_reintegri=im_totale_reintegri-aImMandato
--     im_totale_spese=im_totale_spese+aImMandato
--    Per ogni spesa collegata al mandato in processo viengono resettati:
--       CD_CDS_MANDATO
--       ESERCIZIO_MANDATO
--       PG_MANDATO
--       e viene messo a 'N' l'indicatore di spesa reintegrata
--
-- Parametri:
--    aCdCdsMandato -> Codice cds del mandato di reintegro
--    aEsMandato -> Esercizio del mandato di reintegro
--    aPgMandato -> Progressivo del mandato di reintegro
--    aImMandato -> Importo del mandato di reintregro
--    aUser -> utente che effettua l'operazione
--    aTSNow -> timestamp dell'operazione

 PROCEDURE annullaReintegroSpeseFondo
      (
       aCdCdsMandato varchar2,
       aEsMandato varchar2,
       aPgMandato number,
       aImMandato number,
	   aUser varchar2,
	   aTSNow date
      );


-- Esecuzione della chiusura del fondo economale.
-- Un mandato a regolamento sospeso viene emesso di importo pari al residuo del fondo.
-- Lo stato del fondo viene messo a chiuso.
--
-- pre-post-name: Fondo non trovato
-- pre:  Non ? stato trovato alcun Fondo Economale corrispondente ai parametri passati.
-- post: Viene sollevata un'eccezione
--
-- pre-post-name: Reversale di chiusura non necessaria
-- pre:  La Reversale di chiusura del Fondo non deve essere creata, (fl_rev_da_emettere = 'N')
-- post: Viene sollevata un'eccezione
--
-- pre-post-name: Annot. su pgiro su capitoli diversi
-- pre: Le annotazioni su pgiro collegate ai documenti di apertura del Fondo Economale fanno riferim.
--	a capitoli finanziari differenti, (v. chkAnnotazoniPGiro)
-- post: Viene sollevata un'eccezione
--
-- pre-post-name: Chiusura del fondo
-- pre:  Nessun'altra precondizione verificata
-- post: 1 - Viene creata una annotazione di giro tronca in parte entrata (v. creaAnnotPGiroTroncEntrata).
--	 2 - Viene creato un documento amministrativo generico di entrata per la chiusura
--           del fondo economale di importo pari al residuo iniziale del fondo e una reversale
--           a regolamento sospeso (procedure 'creaReversaleChiusuraFondo')
--       3 - Viene aggiornato il fondo valorizzando il pg_reversale con quello della reversale di chiusura e viene
--	     impostato il fl_rev_da_emettere = 'N'
--
--
-- Parametri:
--    aCdCds -> Codice cds del fondo economale
--    aEsercizio -> Esercizio del fondo economale
--    aCdUnitaOrganizzativa -> Codice Unita Organizzativa del fondo economale
--    aCdCodiceFondo -> Codice Unita Organizzativa del fondo economale
--    aUser -> utente che effettua l'operazione



 procedure chiudiFondo(
  aCdCds varchar2,
  aEsercizio number,
  aCdUnitaOrganizzativa varchar2,
  aCdCodiceFondo varchar2,
  aUser varchar2
 );

-- creaReversaleRegolarizzazione
--
-- pre-post-name: Creazione reversale di regolarizzazione
-- pre:  L'utente ha inoltrato una richiesta di chiusura del fondo
--       Un mandato di regolarizzazione per reintegrare tutte le spese non ancora reintegrate del fondo
--       ? stato emesso
-- post: 1 - Viene creata una annotazione di giro tronca in parte entrata (v. creaAnnotPGiroTroncEntrata).
--	 2 - Viene creato un documento amministrativo generico di regolarizzazione
--           di importo pari al mandato di regolarizzazione. Tale documento viene contabilizzato
--           sull'accertamento/i in partita di giro legato all'obbligazione/i su cui ? stato contabilizzato
--           il documento/i generico di apertura del fondo
--       3 - Viene creata una reversale di regolarizzazione sul documento generico creato al punto 1
--       4 - Viene creata l'associazione fra il mandato e la reversale di regolarizzazione
--            (procedure 'cnrctb038.ins_ASS_MANDATO_REVERSALE')
--
-- Parametri:
--
--    aMan -> IL mandato di regolarizzazione gi? creato
--    aFondo -> Il fondo economale su cui ? necessario creare la reversale di regolarizzazione
--    aPgCall -> numero di chiamata VSX determinato da applicazione JAVA


 procedure creaReversaleRegolarizzazione(
 aMan MANDATO%rowtype,
 aFondo FONDO_ECONOMALE%rowtype,
 aPgCall number
 );

-- Creazione della reversale di chiusura del fondo economale,
--
-- pre-post-name: Disponibilit? accertamenti in partita di giro
-- pre:  La disponibilit? delle scadenze degli accertamenti in partita di giro associati alle
--       obbligazioni su cui sono stati contabilizzati i documenti di apertura del fondo economale
--       ? inferiore all'importo della reversale da generare
-- post: Viene sollevata un'eccezione
--
-- pre-post-name: Creazione reversale di chiusura del fondo
-- pre:  Nessun'altra precondizione verificata
-- post: 1 - Viene creato un documento amministrativo generico di chiusura del fondo economale
--           di importo pari al residuo iniziale del fondo. Tale documento viene contabilizzato
--           sull'accertamento/i in partita di giro legato all'obbligazione/i su cui ? stato contabilizzato
--           il documento/i generico di apertura del fondo
--       2 - Viene creata una reversale a regolamento sospeso sul documento generico creato al punto 2
--
--
-- Parametri:
--    aFondo -> Il fondo economale su cui ? necessario creare la reversale di chiusura
--    aRev -> La reversale da creare
--    aImReversale -> L'importo della reversale da creare
--    aUser -> utente che effettua l'operazione

 procedure creaReversaleChiusuraFondo(
 aFondo in out FONDO_ECONOMALE%rowtype,
 aRev in out reversale%rowtype,
 aUser varchar2,
 aPgCall number
 );



-- Esecuzione della chiusura delle spese non ancora reintegrate, per la chiusura del Fondo Economale.
-- Un mandato di regolarizzazione viene emesso di importo pari alla somma delle spese non ancora reintegrate.
-- Lo stato del fondo viene messo a chiuso.
--
-- pre-post-name: Fondo non trovato
-- pre:  Non ? stato trovato alcun Fondo Economale corrispondente ai parametri passati.
-- post: Viene sollevata un'eccezione
--
-- pre-post-name: Fondo non aperto
-- pre:  Lo stato del fondo ? chiuso
-- post: Viene sollevata un'eccezione
--
-- pre-post-name: Spese associate a compensi non reintegrate.
-- pre:  Alcune spese associate a compensi non sono state reintegrate
-- post: Viene sollevata un'eccezione
--
-- pre-post-name: Annot. su pgiro su capitoli diversi
-- pre: Le annotazioni su pgiro collegate ai documenti di apertura del Fondo Economale fanno riferim.
--	a capitoli finanziari differenti, (v. chkAnnotazoniPGiro)
-- post: Viene sollevata un'eccezione
--
-- pre-post-name: Chiusura delle spese non reintegrate
-- pre:  Nessun'altra precondizione verificata
-- post: 1 - Viene impostato il fl_rev_da_emettere. Se l'importo residuo del Fondo ? = 0, il flag viene
--	      impostato a N, (non ? necessario emettere la reversale quando si chiude il Fondo).
--	 2 - Cicla sulle spese relative al Fondo specificato, non ancora reintegrate e le inserisce nella VSX_REINTEGRO_FONDO.
--	 3 - Effettua il reintegro delle spese, (v. vsx_reintegraChiudiSpeseFondo).
--
-- Parametri:
--    aCdCds -> Codice cds del fondo economale
--    aEsercizio -> Esercizio del fondo economale
--    aCdUnitaOrganizzativa -> Codice Unita Organizzativa del fondo economale
--    aCdCodiceFondo -> Codice Unita Organizzativa del fondo economale
--    aUser -> utente che effettua l'operazione

 procedure chiudiSpese(
  aCdCds varchar2,
  aEsercizio number,
  aCdUnitaOrganizzativa varchar2,
  aCdCodiceFondo varchar2,
  aUser varchar2
 );

-- Controllo delle annot. su PGrio collegate ai docum. di apertura di un Fondo Economale.
-- La procedura ? richiamata sia in fase di chiusura delle Spese del Fondo Economale, sia per la chiusura del
-- 	  Fondo Economale stesso, e verifica che tutte le annotazioni su PGiro in parte spesa collegate
--	  ai docum. di apertura di un dato Fondo, siano create sullo stesso capitolo finanziario.
--
-- pre-post-name: Annot. su pgiro su capitoli diversi
-- pre: Le annotazioni su pgiro collegate ai documenti di apertura del Fondo Economale fanno riferim.
--	a capitoli finanziari differenti.
-- post: Viene sollevata un'eccezione
--
-- pre-post-name: Controllo concluso correttamente.
-- pre:  Nessun'altra precondizione verificata
-- post: Le operazioni di chiusura procedono regolarmente.
--
-- Parametri:
--    aCdCds -> Codice cds del fondo economale
--    aEsercizio -> Esercizio del fondo economale
--    aCdUnitaOrganizzativa -> Codice Unita Organizzativa del fondo economale
--    aCdCodiceFondo -> Codice Unita Organizzativa del fondo economale

 procedure chkAnnotazoniPGiro(
  aCdCds varchar2,
  aEsercizio number,
  aCdUnitaOrganizzativa varchar2,
  aCdCodiceFondo varchar2
 );


-- Creazione di un'annotazione su partita di giro tronca.
--
-- pre-post-name: Reintegro delle Spese per la Chiusura del Fondo Economale.
-- pre: La procedura ? stata chiamata per il Reintegro delle Spese per la Chiusura del Fondo
-- post: L'importo dell'annotazione ? valorizzato come l'importo del mandato di regolarizzazione.
--
-- pre-post-name: Chiusura del Fondo Economale.
-- pre: La procedura ? stata chiamata per la Chiusura del Fondo.
-- post: L'importo dell'annotazione ? valorizzato come l'ammontare del Fondo prima che venissero reintegrate le spese non reintegrate.
--
-- pre-post-name: Crea una annotazione su partita di giro tronca.
-- pre: Nessun'altra precondizione verificata
-- post: Viene creata una annotazione su partita di giro tronca in parte entrate.
--	 Il capitolo finanziario viene ricavato dall'associazione fra entrate/spese delle partite di giro
--	 considerando come capitolo di spesa quello dell'annot. tronca in parte spesa assoc. al/i mandato/i di apertura.
--
--
-- Parametri:
-- aPgCall -> numero di chiamata VSX determinato da applicazione JAVA
-- aFondo -> il Fondo Economale sul quale si sta lavorando
-- aAccert -> l'annotazionie su partita di giro da creare
-- aAccertScad -> scadenza relativa all'annotazione da creare
-- aObblig -> l'annotazione su partita di giro parte spese
-- aObbligScad -> la scadenza dell'annotazione di spesa
-- aUser -> utente che effettua la modifica
-- aImporto -> rappresenta l'importo del mandato di regolarizzazione, (? valorizzato nei casi di reintegro delle spese)
-- reintegro -> flag che indica se la procedura ? stata chiamata per Reintegro delle Spese, (reintegro = true),
--		oppure per Chiusura del Fondo, (reintegro = flase)

 procedure creaAnnotPGiroTroncEntrata(
   aPgCall number,
   aFondo fondo_economale%rowtype,
   aAccert IN OUT accertamento%rowtype,
   aAccertScad IN OUT accertamento_scadenzario%rowtype,
   aObblig IN OUT obbligazione%rowtype,
   aObbligScad IN OUT obbligazione_scadenzario%rowtype,
   aUser varchar2,
   aImporto number,
   reintegro boolean
 );

-- Generatore di pratica finanziaria per apertura/incremento del fondo economale
-- Il metodo genera la pratica contabile relativa all'apertura/incremento del fondo economale
--
-- Vengono generati i seguenti documenti:
--
-- 1. Obbligazione su partita di giro tronca intestata all'economo letto dalla testata valida del fondo (deve essere aperto)
-- 2. Documento generico di apertura del fondo economale
-- 3. Mandato di pagamento all'economo collegato al generico
--
-- Vegono effettuati i seguenti controlli:
--  A. Apertura dell'esercizio per il CDS di appartenenza del fondo
--  B. L'esercizio specificato = a quello della data di sistema
--  C. L'importo aImFondo > 0
--  D. Esiste in CONFIG_CNR la specifica dell'elemento voce (PGIRO) da usare nella pratica
-- ====================================================

-- Parametri:
-- aEs -> esercizio fondo economale
-- aCdCds -> Cds fondo
-- aCdUO -> UO fondo
-- aCdTerzo -> Codice terzo dell'economo (vengono estratte le informazioni modpag e banca bancarie pi? recenti)
-- aDescPratica -> Descrizione aggiunta in coda a tutti i documenti della pratica
-- aImFondo -> Importo di apertura
-- aUser -> Utente che effettua l'operazione

 procedure creaPraticaAperturaFondo(
  aEs number,
  aCdCds varchar2,
  aCdUO varchar2,
  aCdTerzo varchar2,
  aDescPratica varchar2,
  aImFondo number,
  aUser varchar2
 );

-- Parametri:
-- aEs -> esercizio fondo economale
-- aCdCds -> Cds fondo
-- aCdUO -> UO fondo
-- aCdFondo -> Codice di fondo economale esistente (da questo vengono recuperate le informazioni modpag e banca relative al terzo)
-- aImFondo -> Importo di apertura
-- aUser -> Utente che effettua l'operazione

 procedure creaPraticaIntegrazioneFondo(
  aEs number,
  aCdCds varchar2,
  aCdUO varchar2,
  aCdFondo varchar2,
  aImFondo number,
  aUser varchar2
 );

END;
/


CREATE OR REPLACE PACKAGE BODY CNRCTB130 AS

 procedure chiudiFondo(
  aCdCds varchar2,
  aEsercizio number,
  aCdUnitaOrganizzativa varchar2,
  aCdCodiceFondo varchar2,
  aUser varchar2
 ) is
   aPgCall number;
   aFondo fondo_economale%rowtype;
   aFondoSpesa fondo_spesa%rowtype;
   aVsxFondo vsx_reintegro_fondo%rowtype;
   aCount number;
   aTSNow date;
   aReversale reversale%rowtype;
--   aImReversaleChiusura number;
 begin
  aTSNow:=sysdate;
  begin

  -- Recupera il Fondo Economale
     select * into aFondo from fondo_economale a where
	   cd_cds = aCdCds
	   and cd_unita_organizzativa = aCdUnitaOrganizzativa
       and esercizio = aEsercizio
 	   and cd_codice_fondo = aCdCodiceFondo
     for update nowait;
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('Fondo economale non trovato');
  end;

--   -- Controlla che il Fondo Economale sia in stato "Aperto"
--   if aFondo.fl_aperto = 'N' then
--      IBMERR001.RAISE_ERR_GENERICO('Fondo economale non aperto');
--   end if;

--   -- Controlla che tutte le spese associate a documenti siano state reintegrate
--   select count(*) into aNumSpeseSuCompensi from
-- 				  fondo_spesa b
--                where
-- 			         b.esercizio = aEsercizio
-- 			     and b.cd_cds = aCdCds
-- 				 and b.cd_unita_organizzativa =aCdUnitaOrganizzativa
-- 				 and b.cd_codice_fondo = aCdCodiceFondo
-- 				 and b.fl_reintegrata = 'N'
-- 				 and b.fl_documentata = 'Y'
-- 				 and b.cd_tipo_documento_amm = CNRCTB100.TI_COMPENSO;
--
--   if aNumSpeseSuCompensi > 0 then
--      IBMERR001.RAISE_ERR_GENERICO('Alcune spese associate a compensi non sono state reintegrate');
--   end if;

  if aFondo.fl_rev_da_emettere = 'N' then
     IBMERR001.RAISE_ERR_GENERICO('La Reversale di chiusura del Fondo non deve essere creata.');
  end if;
  -- Controlla i Capitoli Finanziari delle annotazioni su PGiro relative al Fondo Economale
  chkAnnotazoniPGiro (aCdCds, aEsercizio, aCdUnitaOrganizzativa, aCdCodiceFondo);


	 creaReversaleChiusuraFondo( aFondo, aReversale, /*aImReversaleChiusura, */aUser, aPgCall );

	 --update fondo
	update fondo_economale set
	   pg_reversale = aReversale.pg_reversale,
	   fl_rev_da_emettere = 'N',
	   duva = aTSNow,
	   utuv = aTSNow,
	pg_ver_rec=pg_ver_rec+1
	where
	   esercizio = aEsercizio
	   and cd_cds = aCdCds
	   and cd_unita_organizzativa = aCdUnitaOrganizzativa
	and cd_codice_fondo = aCdCodiceFondo;

 end;


 PROCEDURE annullaReintegroSpeseFondo
      (
       aCdCdsMandato varchar2,
       aEsMandato varchar2,
       aPgMandato number,
       aImMandato number,
	   aUser varchar2,
	   aTSNow date
      ) is
  aFondo fondo_economale%rowtype;
 begin

  begin
   select * into aFondo from fondo_economale a where
    exists (select 1 from fondo_spesa a1 where
	                a1.cd_cds = a.cd_cds
				and a1.cd_unita_organizzativa = a.cd_unita_organizzativa
                and a1.esercizio = a.esercizio
				and a1.cd_codice_fondo = a.cd_codice_fondo
				and a1.cd_cds_mandato = aCdCdsMandato
				and a1.esercizio_mandato = aEsMandato
				and a1.pg_mandato = aPgMandato)
    for update nowait;
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('Fondo economale da aggiornare per annullamento mandato n.'||aPgMandato||' del cds:'||aCdCdsMandato||' es:'||aEsMandato||' non trovato');
  end;

  if aFondo.fl_aperto = 'N' then
     IBMERR001.RAISE_ERR_GENERICO('Impossibile annullare il reintegro di spese di un fondo economale non aperto');
  end if;

  update fondo_economale
   set
    im_residuo_fondo=im_residuo_fondo-aImMandato,
    im_totale_reintegri=im_totale_reintegri-aImMandato,
    im_totale_spese=im_totale_spese+aImMandato,
    utuv=aUser,
    duva=aTSNow,
    pg_ver_rec=pg_ver_rec+1
  where
       esercizio=aFondo.esercizio
   and cd_cds=aFondo.cd_cds
   and cd_unita_organizzativa=aFondo.cd_unita_organizzativa
   and cd_codice_fondo=aFondo.cd_codice_fondo;


  -- Aggiornamento

  for aSpesa in (select * from fondo_spesa where
                      esercizio=aFondo.esercizio
                  and cd_cds=aFondo.cd_cds
                  and cd_unita_organizzativa=aFondo.cd_unita_organizzativa
                  and cd_codice_fondo=aFondo.cd_codice_fondo
                  and cd_cds_mandato = aCdCdsMandato
				  and esercizio_mandato = aEsMandato
                  and pg_mandato = aPgMandato
				  for update nowait
			  ) loop
   update fondo_spesa set
	 CD_CDS_MANDATO=null,
     ESERCIZIO_MANDATO=null,
     PG_MANDATO=null,
     FL_REINTEGRATA='N',
     UTUV=aUser,
	 DUVA=aTSnow,
	 PG_VER_REC=PG_VER_REC+1
   where
        cd_cds=aSpesa.cd_cds
    and esercizio = aSpesa.esercizio
	and cd_unita_organizzativa=aSpesa.cd_unita_organizzativa
    and cd_codice_fondo=aSpesa.cd_codice_fondo
	and pg_fondo_spesa=aSpesa.pg_fondo_spesa;
  end loop;
 end;

 procedure vsx_reintegraSpeseFondo(
       pgCall NUMBER
 ) is
 begin
 	  vsx_reintegraChiudiSpeseFondo( pgCall, CNRCTB038.TI_MAN_PAG);
 end;

 procedure vsx_reintegraChiudiSpeseFondo(
       pgCall NUMBER,
	   aTiMandato VARCHAR2
 ) is
   aEs number(4);
   aCdCds varchar2(30);
   aCdUo varchar2(30);
   aCdCodiceFondo varchar2(10);
   aNumNonCollegateObblig number;
   aNumReintegrate number;
   aObb obbligazione%rowtype;
   aTotCollegatoScad number(15,2);
   aUser varchar2(20);
   aTSNow date;
   aGen documento_generico%rowtype;
   aGenRiga documento_generico_riga%rowtype;
   aListGenRighe CNRCTB100.docGenRigaList;
   aIRigaMan number;
   aTotMandato number(15,2);
   aMan mandato%rowtype;
   aManRiga mandato_riga%rowtype;
   aManPComp mandato%rowtype;
   aListRigheManP CNRCTB038.righeMandatoList;
   aFE fondo_economale%rowtype;
   aDivisaEuro varchar2(30);
   i number;
   aCdModPag varchar2(10);
   aPgBanca number(8);
   aAnagTst anagrafico%rowtype;
   aTerzoCompenso number(8);
  aTotSpese number(15,2);
  aDataRegistrazione date;
 begin
  aTSNow:=sysdate;
  aTotMandato:=0;
  aIRigaMan:=0;

  for aPar in (select distinct cd_cds, esercizio, cd_uo, cd_codice_fondo, utcr
                from vsx_reintegro_fondo
				   where pg_call = pgCall) loop
   aEs:=aPar.esercizio;
   aCdCds:=aPar.cd_cds;
   aCdUo:=aPar.cd_uo;
   aCdCodiceFondo:=aPar.cd_codice_fondo;
   aUser:=aPar.utcr;
  end loop;


  -- Recupera il Fondo Economale
  select * into aFE from fondo_economale where
                    esercizio=aEs
				and cd_cds=aCdCds
				and cd_unita_organizzativa=aCdUo
				and cd_codice_fondo=aCdCodiceFondo
   for update nowait;

  -- Controlla che tutte le spese da reintegrare siano collegate ad una obbligazione
  select count(*) into aNumNonCollegateObblig from
				  fondo_spesa b
               where
			         b.esercizio = aEs
			     and b.cd_cds = aCdCds
				 and b.cd_unita_organizzativa =aCdUo
				 and b.cd_codice_fondo = aCdCodiceFondo
				 and b.fl_documentata = 'N'
				 and pg_obbligazione is null
			   	 and exists (select 1 from vsx_reintegro_fondo
                    	         where
                                       pg_call = pgCall
                    			   and cd_cds = b.cd_cds
                    			   and cd_uo =b.cd_unita_organizzativa
                    			   and esercizio = b.esercizio
                    			   and cd_codice_fondo = b.cd_codice_fondo
                    			   and pg_fondo_spesa = b.pg_fondo_spesa
            			);

  if aNumNonCollegateObblig > 0 then
   IBMERR001.RAISE_ERR_GENERICO('Alcune spese da reintegrare risultano non ancora collegate ad '||cnrutil.getLabelObbligazioniMin());
  end if;

  -- Controlla che non ci siano spese gi? reintegrate
  select count(*) into aNumReintegrate from
				  fondo_spesa b
               where
			         b.esercizio = aEs
			     and b.cd_cds = aCdCds
				 and b.cd_unita_organizzativa =aCdUo
				 and b.cd_codice_fondo = aCdCodiceFondo
				 and b.fl_reintegrata = 'Y'
			   	 and exists (select 1 from vsx_reintegro_fondo
                    	         where
                                       pg_call = pgCall
                    			   and cd_cds = b.cd_cds
                    			   and cd_uo =b.cd_unita_organizzativa
                    			   and esercizio = b.esercizio
                    			   and cd_codice_fondo = b.cd_codice_fondo
                    			   and pg_fondo_spesa = b.pg_fondo_spesa
            			);
  if aNumReintegrate > 0 then
   IBMERR001.RAISE_ERR_GENERICO('Alcune spese risultano gi? reintegrate');
  end if;

  -- Recupera la Data che sar? utilizzata per la registrazione dei documenti.
  --  La data dipende dal'Esercizio di scrivania e dalla SYSDATE;
  aDataRegistrazione:= CNRCTB008.GETTIMESTAMPCONTABILE(aEs, aTSNow);

  aMan.CD_CDS:=aCdCds;
  aMan.ESERCIZIO:=aEs;
  aMan.CD_UNITA_ORGANIZZATIVA:=aCdUo;
  aMan.CD_CDS_ORIGINE:=aCdCds;
  aMan.CD_UO_ORIGINE:=aCdUo;
  aMan.CD_TIPO_DOCUMENTO_CONT:=CNRCTB018.TI_DOC_MAN;
  aMan.TI_MANDATO:=aTiMandato;
  aMan.TI_COMPETENZA_RESIDUO:=CNRCTB038.TI_MAN_COMP;
  if aTiMandato = CNRCTB038.TI_MAN_REG then
     aMan.DS_MANDATO:='Mandato di regolarizzazione per reintegro fondo economale: '||aEs||'-'||aCdUo||' n.'||aCdCodiceFondo;
     aMan.STATO:=CNRCTB038.STATO_MAN_PAG;
  else
     aMan.DS_MANDATO:='Mandato per reintegro fondo economale: '||aEs||'-'||aCdUo||' n.'||aCdCodiceFondo;
     aMan.STATO:=CNRCTB038.STATO_MAN_EME;
  end if;
  aMan.DT_EMISSIONE:=TRUNC(aDataRegistrazione); 	 -- TRUNC(aTSNow);
  aMan.IM_RITENUTE:=0;
--  aMan.DT_TRASMISSIONE:=;
--  aMan.DT_PAGAMENTO:=;
--  aMan.DT_ANNULLAMENTO:=;
  aMan.UTCR:=aUser;
  aMan.DACR:=aTSNow;
  aMan.UTUV:=aUser;
  aMan.DUVA:=aTSNow;
  aMan.PG_VER_REC:=1;
  aMan.STATO_TRASMISSIONE:=CNRCTB038.STATO_MAN_TRASCAS_NODIST;

  -- Ciclo sulle scadenze di obbligazioni collegate a spese selezionate come da reintegrare

  -- Reset obligazione
  aObb:=null;
  aListGenRighe.delete;
  aGen:=null;
  aGenRIga:=null;
  aDivisaEuro:=CNRCTB015.GETVAL01PERCHIAVE(CNRCTB100.CCNR_DIVISA,CNRCTB100.CCNR_EURO);
  i:=0;
  for aKObbScad in (select distinct a.cd_cds, a.esercizio, a.esercizio_originale, a.pg_obbligazione, a.pg_obbligazione_scadenzario, a.im_scadenza, a.im_associato_doc_amm from
                  obbligazione_scadenzario a,
				  fondo_spesa b
               where
			         b.esercizio = aEs
			     and b.cd_cds = aCdCds
				 and b.cd_unita_organizzativa =aCdUo
				 and b.cd_codice_fondo = aCdCodiceFondo
				 and b.fl_documentata = 'N'
				 and a.cd_cds = b.cd_cds_obbligazione
				 and a.esercizio = b.esercizio
				 and a.esercizio_originale = b.esercizio_ori_obbligazione
				 and a.pg_obbligazione = b.pg_obbligazione
				 and a.pg_obbligazione_scadenzario = b.pg_obbligazione_scadenzario
			   	 and exists (select 1 from vsx_reintegro_fondo
                    	         where
                                       pg_call = pgCall
                    			   and cd_cds = b.cd_cds
                    			   and cd_uo =b.cd_unita_organizzativa
                    			   and esercizio = b.esercizio
                    			   and cd_codice_fondo = b.cd_codice_fondo
                    			   and pg_fondo_spesa = b.pg_fondo_spesa
            			)

  ) loop
   if
              aObb.cd_cds is not null
    	  and aObb.esercizio is not null
    	  and aObb.esercizio_originale is not null
    	  and aObb.pg_obbligazione is not null
          and aObb.esercizio is not null
    	  and
		  (
		       aObb.cd_cds <> aKObbScad.cd_cds
		    or aObb.esercizio <> aKObbScad.esercizio
	    or aObb.esercizio_originale <> aKObbScad.esercizio_originale
    	    or aObb.pg_obbligazione <> aKObbScad.pg_obbligazione
	      )
	  or
         	  aObb.esercizio is null
   then
    if aGen.esercizio is not null then
     -- Creo il documento generico del giro precedente
     CNRCTB110.CREAGENERICO(aGen,aListGenRighe);
     for aIndex in 1 .. aListRigheManP.count loop
      if aListRigheManP(aIndex).PG_DOC_AMM is null then
	   aListRigheManP(aIndex).PG_DOC_AMM:=aGen.pg_documento_generico;
      end if;
	 end loop;
    end if;

	aListGenRighe.delete;
	aGen:=null;
	aGenRIga:=null;
    i:=0;

    -- Lettura lockante dell'obbligazione nuova
    begin
 	 select * into aObb from obbligazione where
           cd_cds = aKObbScad.cd_cds
 	  and esercizio = aKObbScad.esercizio
 	  and esercizio_originale = aKObbScad.esercizio_originale
 	  and pg_obbligazione = aKObbScad.pg_obbligazione
      for update nowait;
    exception when NO_DATA_FOUND then
     IBMERR001.RAISE_ERR_GENERICO(cnrutil.getLabelObbligazione()||' non trovata');
    end;

    aCdModPag:=null;
    aPgBanca:=null;

	-- Estraggo le modalit? di pagamento pi? recenti per il terzo specificato in obbligazione
	CNRCTB080.getModPagUltime(aObb.cd_terzo, aCdModPag, aPgBanca);

	aAnagTst:=CNRCTB080.GETANAG(aObb.cd_terzo);

    -- Creo la testata del nuovo generico per l'obbligazione in processo

    aGen.CD_CDS:=aObb.cd_cds;
    aGen.CD_UNITA_ORGANIZZATIVA:=aObb.cd_unita_organizzativa;
    aGen.ESERCIZIO:=aObb.esercizio;
    aGen.CD_TIPO_DOCUMENTO_AMM:=CNRCTB100.TI_GEN_REINTEGRO_FONDO;
    aGen.CD_CDS_ORIGINE:=aObb.cd_cds;
    aGen.CD_UO_ORIGINE:=aObb.cd_unita_organizzativa;
    aGen.DATA_REGISTRAZIONE:=TRUNC(aDataRegistrazione);	-- trunc(aTSNow);
    aGen.DS_DOCUMENTO_GENERICO:='Reintegro fondo economale: '||aEs||'-'||aCdUo||' n.'||aCdCodiceFondo;
    aGen.TI_ISTITUZ_COMMERC:=CNRCTB100.TI_ISTITUZIONALE;
    -- aGen.IM_TOTALE:=null; -- calcolato da motore di gen. doc. gen.
    aGen.STATO_COFI:=CNRCTB100.STATO_GEN_COFI_TOT_MR;
	-- Fix errore 757 i generici di reintegro vanno in coep come prime scritture
    aGen.STATO_COGE:=CNRCTB100.STATO_COEP_INI;
    aGen.STATO_COAN:=CNRCTB100.STATO_COEP_INI;
    aGen.CD_DIVISA:=aDivisaEuro;
    aGen.CAMBIO:=1;
    --   aGen.ESERCIZIO_LETTERA:=0;
    --   aGen.PG_LETTERA:=0;
    aGen.DACR:=aTSNow;
    aGen.UTCR:=aUser;
    aGen.DUVA:=aTSNow;
    aGen.UTUV:=aUser;
    aGen.PG_VER_REC:=1;
    aGen.DT_SCADENZA:=TRUNC(aDataRegistrazione);	  -- aTSNow;
    aGen.STATO_PAGAMENTO_FONDO_ECO:=CNRCTB100.STATO_NO_PFONDOECO;
    aGen.TI_ASSOCIATO_MANREV:=CNRCTB100.TI_ASSOC_TOT_MAN_REV ;
   end if;



   -- Check che la scadenza sia completamente coperta da spese che appartengono al gruppo delle selezionate per il reintegro

   select sum(b.im_ammontare_spesa) into aTotCollegatoScad from
				  fondo_spesa b
               where
			         b.esercizio = aEs
			     and b.cd_cds = aCdCds
				 and b.cd_unita_organizzativa =aCdUo
				 and b.cd_codice_fondo = aCdCodiceFondo
				 and b.fl_documentata = 'N'
				 and b.cd_cds_obbligazione = aKObbScad.cd_cds
                                 and b.esercizio_ori_obbligazione = aKObbScad.esercizio_originale
				 and b.pg_obbligazione = aKObbScad.pg_obbligazione
				 and b.pg_obbligazione_scadenzario = aKObbScad.pg_obbligazione_scadenzario
			   	 and exists (select 1 from vsx_reintegro_fondo
                    	         where
                                       pg_call = pgCall
                    			   and cd_cds = b.cd_cds
                    			   and cd_uo =b.cd_unita_organizzativa
                    			   and esercizio = b.esercizio
                    			   and cd_codice_fondo = b.cd_codice_fondo
                    			   and pg_fondo_spesa = b.pg_fondo_spesa
   );
   if aKObbScad.im_scadenza != aTotCollegatoScad then
    IBMERR001.RAISE_ERR_GENERICO('La scadenza di '||cnrutil.getLabelObbligazioneMin()||': '||aKObbScad.pg_obbligazione_scadenzario||' dell''obbligazione: '||aKObbScad.pg_obbligazione||' non risulta completamente coperta da spese selezionate per il reintegro');
   end if;

   if aKObbScad.im_scadenza != aKObbScad.im_associato_doc_amm then
    IBMERR001.RAISE_ERR_GENERICO('L''importo associato a documenti amministrativi risulta diverso dall''importo della scadenza: '||aKObbScad.pg_obbligazione_scadenzario||' dell'''||cnrutil.getLabelObbligazioneMin()||': '||aKObbScad.pg_obbligazione);
   end if;

   aGenRiga.CD_CDS:=aGen.CD_CDS;
   aGenRiga.CD_UNITA_ORGANIZZATIVA:=aGen.CD_UNITA_ORGANIZZATIVA;
   aGenRiga.ESERCIZIO:=aGen.ESERCIZIO;
   aGenRiga.CD_TIPO_DOCUMENTO_AMM:=aGen.CD_TIPO_DOCUMENTO_AMM;
   aGenRiga.DS_RIGA:=aGen.DS_DOCUMENTO_GENERICO;
   aGenRiga.IM_RIGA_DIVISA:=aKObbScad.IM_SCADENZA;
   aGenRiga.IM_RIGA:=aKObbScad.IM_SCADENZA;
   aGenRiga.CD_TERZO:=aObb.cd_terzo;
--   aGenRiga.CD_TERZO_CESSIONARIO:=aGen.CD_TERZO_CESSIONARIO;
--   aGenRiga.CD_TERZO_UO_CDS:=aGen.CD_TERZO_UO_CDS;

   aGenRiga.RAGIONE_SOCIALE:=aAnagTst.RAGIONE_SOCIALE;
   aGenRiga.NOME:=aAnagTst.NOME;
   aGenRiga.COGNOME:=aAnagTst.COGNOME;
   aGenRiga.CODICE_FISCALE:=aAnagTst.CODICE_FISCALE;
   aGenRiga.PARTITA_IVA:=aAnagTst.PARTITA_IVA;

   aGenRiga.CD_MODALITA_PAG:=aCdModPag;
   aGenRiga.PG_BANCA:=aPgBanca;
--   aGenRiga.CD_TERMINI_PAG:=aCompenso.CD_TERMINI_PAG;
--   aGenRiga.CD_TERMINI_PAG_UO_CDS:=aCompenso.CD_TERMINI_PAG_UO_CDS;
--   aGenRiga.PG_BANCA_UO_CDS:=aGen.PG_BANCA_UO_CDS;
--   aGenRiga.CD_MODALITA_PAG_UO_CDS:=aGen.CD_MODALITA_PAG_UO_CDS;
--   aGenRiga.NOTE:=aGen.NOTE;
   aGenRiga.DT_DA_COMPETENZA_COGE:=TRUNC(aDataRegistrazione);			  -- TRUNC(aTSNow);
   aGenRiga.DT_A_COMPETENZA_COGE:=TRUNC(aDataRegistrazione);			  -- TRUNC(aTSNow);
   aGenRiga.STATO_COFI:=aGen.STATO_COFI;
--   aGenRiga.DT_CANCELLAZIONE:=
   aGenRiga.CD_CDS_OBBLIGAZIONE:=aKObbScad.CD_CDS;
   aGenRiga.ESERCIZIO_OBBLIGAZIONE:=aKObbScad.ESERCIZIO;
   aGenRiga.ESERCIZIO_ORI_OBBLIGAZIONE:=aKObbScad.ESERCIZIO_ORIGINALE;
   aGenRiga.PG_OBBLIGAZIONE:=aKObbScad.PG_OBBLIGAZIONE;
   aGenRiga.PG_OBBLIGAZIONE_SCADENZARIO:=aKObbScad.PG_OBBLIGAZIONE_SCADENZARIO;
--   aGenRiga.CD_CDS_ACCERTAMENTO:=aAcc.CD_CDS;
--   aGenRiga.ESERCIZIO_ACCERTAMENTO:=aAcc.ESERCIZIO;
--   aGenRiga.ESERCIZIO_ORI_ACCERTAMENTO:=aAcc.ESERCIZIO_ORIGINALE;
--   aGenRiga.PG_ACCERTAMENTO:=aAcc.PG_ACCERTAMENTO;
--   aGenRiga.PG_ACCERTAMENTO_SCADENZARIO:=null;
   aGenRiga.DACR:=aGen.DACR;
   aGenRiga.UTCR:=aGen.UTCR;
   aGenRiga.UTUV:=aGen.UTUV;
   aGenRiga.DUVA:=aGen.DUVA;
   aGenRiga.PG_VER_REC:=aGen.PG_VER_REC;
   aGenRiga.TI_ASSOCIATO_MANREV:=aGen.TI_ASSOCIATO_MANREV;
   i:=i+1;
   aListGenRighe(i):=aGenRiga;

   -- Generazione righe mandato

   aManRiga:=null;
   aManRiga.CD_CDS:=aGen.cd_cds;
   aManRiga.ESERCIZIO:=aGen.esercizio;
   aManRiga.ESERCIZIO_OBBLIGAZIONE:=aGenRiga.esercizio_obbligazione;
   aManRiga.ESERCIZIO_ORI_OBBLIGAZIONE:=aGenRiga.esercizio_ori_obbligazione;
   aManRiga.PG_OBBLIGAZIONE:=aGenRiga.pg_obbligazione;
   aManRiga.PG_OBBLIGAZIONE_SCADENZARIO:=aGenRiga.pg_obbligazione_scadenzario;
   aManRiga.CD_CDS_DOC_AMM:=aGen.cd_cds;
   aManRiga.CD_UO_DOC_AMM:=aGen.cd_unita_organizzativa;
   aManRiga.ESERCIZIO_DOC_AMM:=aGen.esercizio;
   aManRiga.CD_TIPO_DOCUMENTO_AMM:=aGen.cd_tipo_documento_amm;
   aManRiga.DS_MANDATO_RIGA:=aMan.ds_mandato;
   aManRiga.STATO:=aMan.stato;
   aManRiga.CD_TERZO:=aFE.cd_terzo;
   -- Fix del 27/01/2004 - se il mandato ? di regolarizzazione pg_banca e CD_MODALITA_PAG devono essere a null
   if aMan.ti_mandato <> CNRCTB038.TI_MAN_REG then
    aManRiga.PG_BANCA:=aFE.pg_banca;
    aManRiga.CD_MODALITA_PAG:=aFE.cd_modalita_pag;
   end if;
   aManRiga.IM_MANDATO_RIGA:=aKObbScad.IM_SCADENZA;
   aManRiga.IM_RITENUTE_RIGA:=0;
   aManRiga.FL_PGIRO:='N';
   aManRiga.UTCR:=aUser;
   aManRiga.DACR:=aTSNow;
   aManRiga.UTUV:=aUser;
   aManRiga.DUVA:=aTSNow;
   aManRiga.PG_VER_REC:=1;
   aIRigaMan:=aIRigaMan+1;
   aTotMandato:=aTotMandato+aManRiga.im_mandato_riga;
   aListRigheManP(aIRigaMan):=aManRiga;
  end loop;

  -- Creo il documento generico dell'ultimo giro
  if aGen.esercizio is not null then
   CNRCTB110.CREAGENERICO(aGen,aListGenRighe);
   for aIndex in 1 .. aListRigheManP.count loop
    if aListRigheManP(aIndex).PG_DOC_AMM is null then
     aListRigheManP(aIndex).PG_DOC_AMM:=aGen.pg_documento_generico;
    end if;
   end loop;
  end if;
  -- ===================================================================
  -- Sezione relativa alle spese DOCUMENTATE (non derivanti da compensi)
  -- ===================================================================
  for aSpesa1 in (select * from
				  fondo_spesa b
               where
			         b.esercizio = aEs
			     and b.cd_cds = aCdCds
				 and b.cd_unita_organizzativa =aCdUo
				 and b.cd_codice_fondo = aCdCodiceFondo
				 and b.cd_tipo_documento_amm != CNRCTB100.TI_COMPENSO
				 and pg_obbligazione is null
			   	 and exists (select 1 from vsx_reintegro_fondo
                    	         where
                                       pg_call = pgCall
                    			   and cd_cds = b.cd_cds
                    			   and cd_uo =b.cd_unita_organizzativa
                  			   and esercizio = b.esercizio
                    			   and cd_codice_fondo = b.cd_codice_fondo
                    			   and pg_fondo_spesa = b.pg_fondo_spesa
            			)
  ) loop
   -- Lock del documento in processo
   CNRCTB100.lockDocAmm(
    aSpesa1.cd_tipo_documento_amm,
    aSpesa1.cd_cds_doc_amm,
    aSpesa1.esercizio_doc_amm,
    aSpesa1.cd_uo_doc_amm,
    aSpesa1.pg_documento_amm
   );
   for aDocRiga in (select * from v_doc_amm_cofi_fondo_riga where
                         esercizio = aSpesa1.esercizio_doc_amm
				     and cd_tipo_documento_amm = aSpesa1.cd_tipo_documento_amm
					 and cd_cds = aSpesa1.cd_cds_doc_amm
					 and cd_unita_organizzativa = aSpesa1.cd_uo_doc_amm
					 and pg_documento = aSpesa1.pg_documento_amm
   ) loop
    -- Generazione righe mandato
    aManRiga:=null;
    aManRiga.CD_CDS:=aDocRiga.cd_cds;
    aManRiga.ESERCIZIO:=aSpesa1.esercizio; --aDocRiga.esercizio;
    aManRiga.ESERCIZIO_OBBLIGAZIONE:=aDocRiga.esercizio_obbligazione;
    aManRiga.ESERCIZIO_ORI_OBBLIGAZIONE:=aDocRiga.esercizio_ori_obbligazione;
    aManRiga.PG_OBBLIGAZIONE:=aDocRiga.pg_obbligazione;
    aManRiga.PG_OBBLIGAZIONE_SCADENZARIO:=aDocRiga.pg_obbligazione_scadenzario;
    aManRiga.CD_CDS_DOC_AMM:=aDocRiga.cd_cds;
    aManRiga.CD_UO_DOC_AMM:=aDocRiga.cd_unita_organizzativa;
    aManRiga.ESERCIZIO_DOC_AMM:=aDocRiga.esercizio;
    aManRiga.CD_TIPO_DOCUMENTO_AMM:=aDocRiga.cd_tipo_documento_amm;
    aManRiga.PG_DOC_AMM:=aDocRiga.pg_documento;
    aManRiga.DS_MANDATO_RIGA:=aMan.ds_mandato;
    aManRiga.STATO:=aMan.stato;
    aManRiga.CD_TERZO:=aFE.cd_terzo;
	if aMan.ti_mandato <> CNRCTB038.TI_MAN_REG then
       aManRiga.PG_BANCA:=aFE.pg_banca;
       aManRiga.CD_MODALITA_PAG:=aFE.cd_modalita_pag;
	end if;
    aManRiga.IM_MANDATO_RIGA:=aDocRiga.im_scadenza;
    aManRiga.IM_RITENUTE_RIGA:=0;
    aManRiga.FL_PGIRO:='N';
    aManRiga.UTCR:=aUser;
    aManRiga.DACR:=aTSNow;
    aManRiga.UTUV:=aUser;
    aManRiga.DUVA:=aTSNow;
    aManRiga.PG_VER_REC:=1;
    aIRigaMan:=aIRigaMan+1;
    aTotMandato:=aTotMandato+aManRiga.im_mandato_riga;
    aListRigheManP(aIRigaMan):=aManRiga;
	select * into aObb from obbligazione where
	     cd_cds = aDocRiga.cd_cds_obbligazione
	 and esercizio=aDocRiga.esercizio_obbligazione
	 and esercizio_originale = aDocRiga.esercizio_ori_obbligazione
	 and pg_obbligazione = aDocRiga.pg_obbligazione
	for update nowait;
   end loop;

   CNRCTB100.updateDocAmm(
    aSpesa1.cd_tipo_documento_amm,
    aSpesa1.cd_cds_doc_amm,
    aSpesa1.esercizio_doc_amm,
    aSpesa1.cd_uo_doc_amm,
    aSpesa1.pg_documento_amm,
    ' stato_cofi = '''||CNRCTB100.STATO_COM_COFI_TOT_MR||''', ti_associato_manrev = '''||CNRCTB100.TI_ASSOC_TOT_MAN_REV||'''',
    null,
    aUser,
    aTSNow
   );

   for aDR in (select * from v_doc_amm_riga_key where
                         esercizio = aSpesa1.esercizio_doc_amm
				     and cd_tipo_documento_amm = aSpesa1.cd_tipo_documento_amm
					 and cd_cds = aSpesa1.cd_cds_doc_amm
					 and cd_unita_organizzativa = aSpesa1.cd_uo_doc_amm
					 and pg_documento = aSpesa1.pg_documento_amm) loop
    CNRCTB100.updateDocAmmRiga(
     aSpesa1.cd_tipo_documento_amm,
     aSpesa1.cd_cds_doc_amm,
     aSpesa1.esercizio_doc_amm,
     aSpesa1.cd_uo_doc_amm,
     aSpesa1.pg_documento_amm,
     aDR.progressivo_riga,
     ' stato_cofi = '''||CNRCTB100.STATO_COM_COFI_TOT_MR||'''',
	 null,
     aUser,
     aTSNow
    );
   end loop;

  end loop;

  aMan.IM_MANDATO:=aTotMandato;
  if aTiMandato = CNRCTB038.TI_MAN_REG then
    aMan.IM_PAGATO:=aTotMandato;
  else
    aMan.IM_PAGATO:=0;
  end if;

  if aTotMandato <> 0 then
     CNRCTB037.generaDocumento(aMan,aListRigheManP);
     if aTiMandato = CNRCTB038.TI_MAN_REG then

-- 	    -- Calcola la somma delle spese non reintegrate, che sar? l'importo dell'accertamento.
-- 		   select sum(b.im_ammontare_spesa) into aTotSpese from
-- 						  fondo_spesa b
-- 		               where b.esercizio = aEs
-- 					     and b.cd_cds = aCdCds
-- 						 and b.cd_unita_organizzativa = aCdUo
-- 						 and b.cd_codice_fondo = aCdCodiceFondo
-- 					   	 and exists (select 1 from vsx_reintegro_fondo
-- 		                    	         where
-- 		                                       pg_call = pgCall
-- 		                    			   and cd_cds = b.cd_cds
-- 		                    			   and cd_uo =b.cd_unita_organizzativa
-- 		                    			   and esercizio = b.esercizio
-- 		                    			   and cd_codice_fondo = b.cd_codice_fondo
-- 		                    			   and pg_fondo_spesa = b.pg_fondo_spesa
-- 		   );

	-- Aggiorna il campo IM_PAGAMENTI_INCASSI su VOCE_F_SALDI_CMP
	    CNRCTB037.RISCONTROMANDATO (aMan.esercizio, aMan.cd_cds, aMan.pg_mandato, 'I', aMan.utcr);

        creaReversaleRegolarizzazione( aMan, aFE, pgCall );
     end if;
   end if;

  -- Aggiornamento

  for aPar in (select * from vsx_reintegro_fondo
				   where pg_call = pgCall
			  ) loop
   update fondo_spesa set
	 CD_CDS_MANDATO=aMan.cd_cds,
     ESERCIZIO_MANDATO=aMan.esercizio,
     PG_MANDATO=aMan.pg_mandato,
     FL_REINTEGRATA='Y',
     UTUV=aUser,
	 DUVA=aTSnow,
	 PG_VER_REC=PG_VER_REC+1
   where
        cd_cds=aPar.cd_cds
    and esercizio = aPar.esercizio
	and cd_unita_organizzativa=apar.cd_uo
    and cd_codice_fondo=aPar.cd_codice_fondo
	and pg_fondo_spesa=aPar.pg_fondo_spesa
    and (
	    cd_tipo_documento_amm != CNRCTB100.TI_COMPENSO
     or cd_tipo_documento_amm is null
	);
  end loop;


  -- ===============================================================
  -- Sezione relativa alle spese DOCUMENTATE (DA COMPENSI)
  -- ===============================================================
  for aSpesa in (select * from
				  fondo_spesa b
               where
			         esercizio = aEs
			     and cd_cds = aCdCds
				 and cd_unita_organizzativa =aCdUo
				 and cd_codice_fondo = aCdCodiceFondo
				 and cd_tipo_documento_amm = CNRCTB100.TI_COMPENSO
				 and pg_obbligazione is null
			   	 and exists (select 1 from vsx_reintegro_fondo
                    	         where
                                       pg_call = pgCall
                    			   and cd_cds = b.cd_cds
                    			   and cd_uo =b.cd_unita_organizzativa
                  			       and esercizio = b.esercizio
                    			   and cd_codice_fondo = b.cd_codice_fondo
                    			   and pg_fondo_spesa = b.pg_fondo_spesa
            			))
  loop
   CNRCTB100.lockDocAmm(
    aSpesa.cd_tipo_documento_amm,
    aSpesa.cd_cds_doc_amm,
    aSpesa.esercizio_doc_amm,
    aSpesa.cd_uo_doc_amm,
    aSpesa.pg_documento_amm
   );
   CNRCTB560.CONTABILIZZACOMPENSOCOFIFONDO(
    aSpesa.cd_cds_doc_amm,
	aSpesa.cd_uo_doc_amm,
    aSpesa.esercizio,
    aSpesa.esercizio_doc_amm,
	aSpesa.pg_documento_amm,
	aUser,
	aManPComp
   );
   if aManPComp.pg_mandato is null then
    IBMERR001.RAISE_ERR_GENERICO('Compenso da reintegrare via fondo senza mandato principale non supportato!');
   end if;

   --Poich? il mandato di reintegro di un "COMPENSO" viene generato con il terzo percipiente e la sua modalit? di pagamento
   --associata nel compenso e solo dopo viene modificato il terzo e la modalit? di pagamento mettendo quelle
   --dell'economo, occorre ripetre il controllo sulla tracciabilit? dei pagamenti.
   Begin
      for aManRigaComp in (
         select * from mandato_riga
         where cd_cds = aManPComp.cd_cds
	         and esercizio = aManPComp.esercizio
	         and pg_mandato = aManPComp.pg_mandato)
      loop
          cnrctb037.verificaTracciabilitaPag(aManPComp.esercizio,
                                             aManPComp.dt_emissione,
                                             aFE.cd_modalita_pag,
                                             aManRigaComp.cd_tipo_documento_amm,
                                             aManPComp.im_mandato - aManPComp.im_ritenute);
      end loop;
      for aManCoriComp in (
          select * from mandato a
          where cd_cds = aManPComp.cd_cds
	          and esercizio = aManPComp.esercizio
            and exists (select 1 from ass_mandato_mandato
                        where cd_cds = aManPComp.cd_cds
	                        and esercizio = aManPComp.esercizio
	                        and pg_mandato =  aManPComp.pg_mandato
                          and cd_cds_coll = a.cd_cds
	                        and esercizio_coll = a.esercizio
	                        and pg_mandato_coll =  a.pg_mandato))
      loop
            for aManRigaCoriComp in (
                select * from mandato_riga
                where cd_cds = aManCoriComp.cd_cds
	                and esercizio = aManCoriComp.esercizio
	                and pg_mandato = aManCoriComp.pg_mandato)
            loop
                   cnrctb037.verificaTracciabilitaPag(aManCoriComp.esercizio,
                                                      aManCoriComp.dt_emissione,
                                                      aFE.cd_modalita_pag,
                                                      aManRigaCoriComp.cd_tipo_documento_amm,
                                                      aManCoriComp.im_mandato - aManCoriComp.im_ritenute);
            end loop;
      end loop;
   End;

   select cd_terzo into aTerzoCompenso from compenso where
         esercizio = aSpesa.esercizio_doc_amm
	 and cd_cds = aSpesa.cd_cds_doc_amm
	 and cd_unita_organizzativa = aSpesa.cd_uo_doc_amm
	 and pg_compenso = aSpesa.pg_documento_amm;

   -- Aggiornare sul mandato di pagamento: il terzo, la descrizone in modo da uniformarli a quelle del mandato di reintegro.
   update mandato set
       ds_mandato = 'Mandato di reintegro fondo economale (spesa da compenso): '||aEs||'-'||aCdUo||' n.'||aCdCodiceFondo
   where
        cd_cds = aManPComp.cd_cds
	and esercizio = aManPComp.esercizio
	and pg_mandato = aManPComp.pg_mandato;
   update mandato_terzo set
       cd_terzo = aFE.cd_terzo
   where
        cd_cds = aManPComp.cd_cds
	and esercizio = aManPComp.esercizio
	and pg_mandato = aManPComp.pg_mandato
    and cd_terzo = aTerzoCompenso;
   update mandato_riga set
     CD_TERZO=aFE.cd_terzo
    ,PG_BANCA=aFE.pg_banca
    ,CD_MODALITA_PAG=aFE.cd_modalita_pag
   where
        cd_cds = aManPComp.cd_cds
	and esercizio = aManPComp.esercizio
	and pg_mandato = aManPComp.pg_mandato
	and cd_terzo = aTerzoCompenso;
   -- Aggiorna l'indicazione sul terzo nelle reversali collegate
   for aRevCoriComp in (
    select * from reversale a where
         cd_cds = aManPComp.cd_cds
	 and esercizio = aManPComp.esercizio
     and exists (select 1 from ass_mandato_reversale where
           cd_cds_mandato = aManPComp.cd_cds
	   and esercizio_mandato = aManPComp.esercizio
	   and pg_mandato =  aManPComp.pg_mandato
       and cd_cds_reversale = a.cd_cds
	   and esercizio_reversale = a.esercizio
	   and pg_reversale =  a.pg_reversale
	 )
   ) loop
    update reversale_riga set
      CD_TERZO=aFE.cd_terzo
    where
         cd_cds = aRevCoriComp.cd_cds
	 and esercizio = aRevCoriComp.esercizio
	 and pg_reversale = aRevCoriComp.pg_reversale
	 and cd_terzo = aTerzoCompenso;
    update reversale_terzo set
       cd_terzo = aFE.cd_terzo
    where
         cd_cds = aRevCoriComp.cd_cds
	 and esercizio = aRevCoriComp.esercizio
	 and pg_reversale = aRevCoriComp.pg_reversale
     and cd_terzo = aTerzoCompenso;
   end loop;

   for aManCoriComp in (
    select * from mandato a where
         cd_cds = aManPComp.cd_cds
	 and esercizio = aManPComp.esercizio
     and exists (select 1 from ass_mandato_mandato where
           cd_cds = aManPComp.cd_cds
	   and esercizio = aManPComp.esercizio
	   and pg_mandato =  aManPComp.pg_mandato
       and cd_cds_coll = a.cd_cds
	   and esercizio_coll = a.esercizio
	   and pg_mandato_coll =  a.pg_mandato
	 )
   ) loop
    update mandato_riga set
      CD_TERZO=aFE.cd_terzo
     ,PG_BANCA=aFE.pg_banca
     ,CD_MODALITA_PAG=aFE.cd_modalita_pag
    where
         cd_cds = aManCoriComp.cd_cds
	 and esercizio = aManCoriComp.esercizio
	 and pg_mandato = aManCoriComp.pg_mandato
	 and cd_terzo = aTerzoCompenso;
    update mandato_terzo set
       cd_terzo = aFE.cd_terzo
    where
         cd_cds = aManCoriComp.cd_cds
	 and esercizio = aManCoriComp.esercizio
	 and pg_mandato = aManCoriComp.pg_mandato
     and cd_terzo = aTerzoCompenso;
   end loop;

   aTotMandato:=aTotMandato+aManPComp.im_mandato;
   update fondo_spesa set
	 CD_CDS_MANDATO=aManPComp.cd_cds,
     ESERCIZIO_MANDATO=aManPComp.esercizio,
     PG_MANDATO=aManPComp.pg_mandato,
     FL_REINTEGRATA='Y',
     UTUV=aUser,
	 DUVA=aTSnow,
	 PG_VER_REC=PG_VER_REC+1
   where
        cd_cds=aSpesa.cd_cds
    and esercizio = aSpesa.esercizio
	and cd_unita_organizzativa=aSpesa.cd_unita_organizzativa
    and cd_codice_fondo=aSpesa.cd_codice_fondo
	and pg_fondo_spesa=aSpesa.pg_fondo_spesa
    and cd_tipo_documento_amm = CNRCTB100.TI_COMPENSO;
  end loop; -- FINE SEZIONE DERIVANTE DA COMPENSO

  update fondo_economale
   set
    im_residuo_fondo=im_residuo_fondo+aTotMandato,
    im_totale_reintegri=im_totale_reintegri+aTotMandato,
    im_totale_spese=im_totale_spese-aTotMandato,
    utuv=aUser,
    duva=aTSNow,
    pg_ver_rec=pg_ver_rec+1
  where
       esercizio=aEs
   and cd_cds=aCdCds
   and cd_unita_organizzativa=aCdUo
   and cd_codice_fondo=aCdCodiceFondo;

 end; -- FINE vsx_reintegraChiudiSpeseFondo



  procedure creaReversaleRegolarizzazione( aMan MANDATO%rowtype,
  										   aFondo FONDO_ECONOMALE%rowtype,
										   aPgCall number ) is
  aImReversale number(15,2);
--  aImResiduoDoc number(15,2);
  aImDispScadenza number(15,2);
  aCdTerzoUO number(8);
  aCdModPagUO varchar2(10);
  aPgBancaUO number(10);
  aTSNow date;
  aUser varchar2(20);
  aEuro varchar2(30);
  aGen documento_generico%rowtype;
  aGenRiga documento_generico_riga%rowtype;
  aListGenRighe CNRCTB100.docGenRigaList;
  aRev reversale%rowtype;
  aRevRiga reversale_riga%rowtype;
  aRevRighe CNRCTB038.righeReversaleList;
  aListaAcrScad CNRCTB035.scadAcrList;
  docCount number;
  revCount number;
  aDataRegistrazione date;
  aAccert accertamento%rowtype;
  aAccertScad accertamento_scadenzario%rowtype;
  aObblig obbligazione%rowtype;
  aObbligScad obbligazione_scadenzario%rowtype;
 -- aTotSpese number(15,2);
 -- aAccertTronc accertamento%rowtype;
   aEs number(4);
   aCdCds varchar2(30);
   aCdUo varchar2(30);
   aCdCodiceFondo varchar2(10);
  begin
    aImReversale:=aMan.im_mandato;
   --estrae il terzo UO
   CNRCTB080.getTerzoPerUO(aFondo.cd_unita_organizzativa, aCdTerzoUO, aCdModPagUO, aPgBancaUO,aFondo.esercizio);
   aTSNow:=aMan.dacr;
   aEuro:=CNRCTB015.GETVAL01PERCHIAVE(CNRCTB100.CCNR_DIVISA,CNRCTB100.CCNR_EURO);
   aUser:=aMan.utcr;

   aEs:=aFondo.esercizio;
   aCdCds:=aFondo.cd_cds;
   aCdUo:=aFondo.cd_unita_organizzativa;
   aCdCodiceFondo:=aFondo.cd_codice_fondo;

  -- Recupera la Data che sar? utilizzata per la registrazione dei documenti.
  --  La data dipende dal'Esercizio di scrivania e dalla SYSDATE;
  aDataRegistrazione:= aMan.dt_emissione;

	-- creo documento generico di entrata per regolarizzazione
   aGen:=null;
   aGen.CD_TIPO_DOCUMENTO_AMM:=CNRCTB100.TI_GENERICO_REGOLA_E;
   aGen.CD_CDS:=aFondo.cd_cds;
   aGen.CD_UNITA_ORGANIZZATIVA:=aFondo.cd_unita_organizzativa;
   aGen.ESERCIZIO:=aFondo.esercizio;
   aGen.CD_CDS_ORIGINE:=aFondo.cd_cds;
   aGen.CD_UO_ORIGINE:=aFondo.cd_unita_organizzativa;
   aGen.DATA_REGISTRAZIONE:=TRUNC(aDataRegistrazione);    -- TRUNC(aTSNow);
   aGen.DT_DA_COMPETENZA_COGE:=TRUNC(aDataRegistrazione); -- TRUNC(aTSNow);
   aGen.DT_A_COMPETENZA_COGE:=TRUNC(aDataRegistrazione);  -- TRUNC(aTSNow);
   aGen.DS_DOCUMENTO_GENERICO:='Generico di regolarizzazione spese fondo economale: '||aFondo.esercizio||'-'||aFondo.cd_unita_organizzativa||' n.'||aFondo.cd_codice_fondo;
   aGen.TI_ISTITUZ_COMMERC:=CNRCTB100.TI_ISTITUZIONALE;
   aGen.IM_TOTALE:=aImReversale;
   aGen.STATO_COFI:=CNRCTB100.STATO_GEN_COFI_TOT_MR;
   aGen.STATO_COGE:=CNRCTB100.STATO_COEP_EXC;
   aGen.STATO_COAN:=CNRCTB100.STATO_COEP_EXC;
   aGen.CD_DIVISA:=aEuro;
   aGen.CAMBIO:=1;
--   aGen.ESERCIZIO_LETTERA:=0;
--   aGen.PG_LETTERA:=0;
   aGen.DACR:=aTSNow;
   aGen.UTCR:=aUser;
   aGen.DUVA:=aTSNow;
   aGen.UTUV:=aUser;
   aGen.PG_VER_REC:=1;
   aGen.DT_SCADENZA:=TRUNC(aDataRegistrazione); -- TRUNC(aTSNow);
   aGen.STATO_PAGAMENTO_FONDO_ECO:=CNRCTB100.STATO_NO_PFONDOECO;
   aGen.TI_ASSOCIATO_MANREV:=CNRCTB100.TI_ASSOC_TOT_MAN_REV ;

   docCount:=1;
--   aImResiduoDoc := aImReversale;

   -- Crea una annotazione di giro tronca in parte entrate (Accertamento), per le spese non
   --  reintegrate
   aAccert:=null;

	-- Crea l'accertamento scadenzario relativo all'accertamento creato in precedenza
   aAccertScad:=null;

   -- Crea l'obbligazione in pgiro tronca relativa all'accertamento precedentemente creato
   aObblig:=null;

   -- Creo l'obbligazione_scadenzario
   aObbligScad:=null;

   creaAnnotPGiroTroncEntrata(aPgCall, aFondo, aAccert, aAccertScad, aObblig, aObbligScad, aUser, aMan.im_mandato, true);

   -- Crea la riga del documento generico
   aGenRiga:=null;
   aGenRiga.CD_CDS:=aGen.CD_CDS;
   aGenRiga.CD_UNITA_ORGANIZZATIVA:=aGen.CD_UNITA_ORGANIZZATIVA;
   aGenRiga.ESERCIZIO:=aGen.ESERCIZIO;
   aGenRiga.CD_TIPO_DOCUMENTO_AMM:=aGen.CD_TIPO_DOCUMENTO_AMM;
   aGenRiga.DS_RIGA:=aGen.DS_DOCUMENTO_GENERICO;
   aGenRiga.IM_RIGA_DIVISA:=aAccert.IM_ACCERTAMENTO; -- (Tot Spese) ??? CORRETTO ???
   aGenRiga.IM_RIGA:=aGenRiga.IM_RIGA_DIVISA;
   aGenRiga.CD_TERZO_UO_CDS:=aCdTerzoUO;
   aGenRiga.CD_TERZO:=aFondo.CD_TERZO;
   aGenRiga.RAGIONE_SOCIALE:=null;
   aGenRiga.NOME:=null;
   aGenRiga.COGNOME:=null;
   aGenRiga.CODICE_FISCALE:=null;
   aGenRiga.PARTITA_IVA:=null;
   aGenRiga.DT_DA_COMPETENZA_COGE:=TRUNC(aDataRegistrazione); -- TRUNC(aTSNow);
   aGenRiga.DT_A_COMPETENZA_COGE:=TRUNC(aDataRegistrazione);   -- TRUNC(aTSNow);
   aGenRiga.STATO_COFI:=aGen.STATO_COFI;
   aGenRiga.CD_CDS_ACCERTAMENTO:=aAccert.CD_CDS;	   -- aListaAcrScad.CD_CDS;
   aGenRiga.ESERCIZIO_ACCERTAMENTO:=aAccert.ESERCIZIO; -- aListaAcrScad.ESERCIZIO;
   aGenRiga.ESERCIZIO_ORI_ACCERTAMENTO:=aAccert.ESERCIZIO_ORIGINALE; -- aListaAcrScad.ESERCIZIO_ORIGINALE;
   aGenRiga.PG_ACCERTAMENTO:=aAccert.PG_ACCERTAMENTO;  -- aListaAcrScad.PG_ACCERTAMENTO;
   aGenRiga.PG_ACCERTAMENTO_SCADENZARIO:=aAccertScad.PG_ACCERTAMENTO_SCADENZARIO;  -- aListaAcrScad.PG_ACCERTAMENTO_SCADENZARIO;
   aGenRiga.PG_BANCA_UO_CDS:=aPgBancaUO;
   aGenRiga.CD_MODALITA_PAG_UO_CDS:=aCdModPagUO;
   aGenRiga.DACR:=aGen.DACR;
   aGenRiga.UTCR:=aGen.UTCR;
   aGenRiga.UTUV:=aGen.UTUV;
   aGenRiga.DUVA:=aGen.DUVA;
   aGenRiga.PG_VER_REC:=aGen.PG_VER_REC;
   aGenRiga.TI_ASSOCIATO_MANREV:=aGen.TI_ASSOCIATO_MANREV;


   aListGenRighe(docCount):=aGenRiga;


   CNRCTB110.creaGenericoAggObbAcc(aGen,aListGenRighe);

--- creo la reversale di regolarizzazione
   aRev.CD_CDS:=aFondo.cd_cds;
   aRev.ESERCIZIO:=aFondo.esercizio;
   aRev.CD_UNITA_ORGANIZZATIVA:=aFondo.cd_unita_organizzativa;
   aRev.CD_CDS_ORIGINE:=aFondo.cd_cds;
   aRev.CD_UO_ORIGINE:=aFondo.cd_unita_organizzativa;
   aRev.CD_TIPO_DOCUMENTO_CONT:=CNRCTB018.TI_DOC_REV;
   aRev.TI_REVERSALE:=CNRCTB038.TI_REV_REG;
   aRev.TI_COMPETENZA_RESIDUO:=CNRCTB038.TI_MAN_COMP;
   aRev.DS_REVERSALE:='Regolarizzazione spese fondo economale: '||aFondo.esercizio||'-'||aFondo.cd_unita_organizzativa||' n.'||aFondo.cd_codice_fondo;
   aRev.STATO:=CNRCTB038.STATO_REV_PAG;
   aRev.DT_EMISSIONE:=TRUNC(aDataRegistrazione); -- TRUNC(aTSNow);
   -- Fix errore 757 la reversale di regolarizzazione in chiusura del fondo ? esclusa COEP
   aRev.STATO_COGE := CNRCTB100.STATO_COEP_EXC;
--   aRev.DT_TRASMISSIONE:=;
--   aRev.DT_INCASSO:=;
--   aRev.DT_ANNULLAMENTO:=;
   aRev.IM_REVERSALE:=aImReversale ;
   aRev.IM_INCASSATO:=aImReversale ;
   aRev.DACR:=aTSNow;
   aRev.UTCR:=aUser;
   aRev.DUVA:=aTSNow;
   aRev.UTUV:=aUser;
   aRev.PG_VER_REC:=1;
   aRev.STATO_TRASMISSIONE:=CNRCTB038.STATO_REV_TRASCAS_NODIST;

   revCount:=1;
	-- per ogni riga di doc.generico creato per la chiusra del fondo viene creata una riga di reversale
   for i in 1 .. aListGenRighe.count loop
      aRevRiga:=null;
      aRevRiga.CD_CDS:=aRev.cd_cds;
      aRevRiga.ESERCIZIO:=aRev.esercizio;
      aRevRiga.ESERCIZIO_ACCERTAMENTO:=aListGenRighe(i).esercizio;
      aRevRiga.ESERCIZIO_ORI_ACCERTAMENTO:=aListGenRighe(i).esercizio_ori_accertamento;
      aRevRiga.PG_ACCERTAMENTO:=aListGenRighe(i).pg_accertamento;
      aRevRiga.PG_ACCERTAMENTO_SCADENZARIO:=aListGenRighe(i).pg_accertamento_scadenzario;
      aRevRiga.CD_CDS_DOC_AMM:=aGen.cd_cds;
      aRevRiga.CD_UO_DOC_AMM:=aGen.cd_unita_organizzativa;
      aRevRiga.ESERCIZIO_DOC_AMM:=aGen.esercizio;
      aRevRiga.CD_TIPO_DOCUMENTO_AMM:=aGen.cd_tipo_documento_amm;
      aRevRiga.PG_DOC_AMM:=aGen.pg_documento_generico;
      aRevRiga.DS_REVERSALE_RIGA:=aRev.ds_reversale;
      aRevRiga.STATO:=aRev.STATO;
      aRevRiga.CD_TERZO:=aFondo.cd_terzo;
      aRevRiga.CD_TERZO_UO:=aGenRiga.cd_terzo_uo_cds;
      ---aRevRiga.PG_BANCA:=aGenRiga.pg_banca_uo_cds;
      ---aRevRiga.CD_MODALITA_PAG:=aGenRiga.cd_modalita_pag_uo_cds;
      aRevRiga.IM_REVERSALE_RIGA:=aListGenRighe(i).im_riga;
      aRevRiga.FL_PGIRO:='Y';
      aRevRiga.UTCR:=aUser;
      aRevRiga.DACR:=aTSNow;
      aRevRiga.UTUV:=aUser;
      aRevRiga.DUVA:=aTSNow;
      aRevRiga.PG_VER_REC:=1;
      aRevRighe(i):=aRevRiga;
      revCount:=revCount+1;
   end loop;

   cnrctb037.GENERAREVERSALE( aRev,aRevRighe);
   cnrctb038.ins_ASS_MANDATO_REVERSALE ( aMan, aMan.pg_mandato, aRev);

   -- Aggiorna il campo IM_PAGAMENTI_INCASSI su VOCE_F_SALDI_CMP
   CNRCTB037.RISCONTROREVERSALE (aRev.esercizio, aRev.cd_cds, aRev.pg_reversale, 'I', aMan.utcr);


  end;

  procedure ins_VSX_REINTEGRO_FONDO (aDest VSX_REINTEGRO_FONDO%rowtype) is
  begin
   insert into VSX_REINTEGRO_FONDO (
     PG_CALL
    ,PAR_NUM
    ,PROC_NAME
    ,MESSAGETOUSER
    ,CD_CDS
    ,CD_UO
    ,ESERCIZIO
    ,CD_CODICE_FONDO
    ,PG_FONDO_SPESA
    ,UTCR
    ,DACR
    ,UTUV
    ,DUVA
    ,PG_VER_REC
   ) values (
     aDest.PG_CALL
    ,aDest.PAR_NUM
    ,aDest.PROC_NAME
    ,aDest.MESSAGETOUSER
    ,aDest.CD_CDS
    ,aDest.CD_UO
    ,aDest.ESERCIZIO
    ,aDest.CD_CODICE_FONDO
    ,aDest.PG_FONDO_SPESA
    ,aDest.UTCR
    ,aDest.DACR
    ,aDest.UTUV
    ,aDest.DUVA
    ,aDest.PG_VER_REC
    );
 end;

 procedure creaReversaleChiusuraFondo(aFondo  in out FONDO_ECONOMALE%rowtype,
 				      aRev    in out reversale%rowtype,
				      aUser   varchar2,
				      aPgCall number) is
-- aImResiduoDoc number(15,2);
  aImDispScadenza number(15,2);
  aCdTerzoUO number(8);
  aCdModPagUO varchar2(10);
  aPgBancaUO number(10);
  aTSNow date;
  aEuro varchar2(30);
  aGen documento_generico%rowtype;
  aGenRiga documento_generico_riga%rowtype;
  aListGenRighe CNRCTB100.docGenRigaList;
  aRevRiga reversale_riga%rowtype;
  aRevRighe CNRCTB038.righeReversaleList;
  aListaAcrScad CNRCTB035.scadAcrList;
  docCount number;
  revCount number;
  aSospeso sospeso%rowtype;
  aSospesoDet sospeso_det_etr%rowtype;

  aAccert         accertamento%rowtype;
  aAccertScad     accertamento_scadenzario%rowtype;
  aAccertScadVoce accertamento_scad_voce%Rowtype;

  aObblig         obbligazione%rowtype;
  aObbligScad     obbligazione_scadenzario%rowtype;
  aObbligScadVoce obbligazione_scad_voce%rowtype;

  aDataRegistrazione date;

  aAccertRiportata accertamento%rowtype;
  aObbligRiportata obbligazione%rowtype;

  aAccertPerRev accertamento%rowtype;
  aEsRev number(4);

  parametri_esercizio parametri_cnr%rowtype;
 begin
   parametri_esercizio:=CNRUTL001.getRecParametriCnr(aFondo.ESERCIZIO);
  --estrae il terzo UO
   CNRCTB080.getTerzoPerUO(aFondo.cd_unita_organizzativa, aCdTerzoUO, aCdModPagUO, aPgBancaUO,aFondo.esercizio);
   aTSNow:=sysdate;
   aEuro:=CNRCTB015.GETVAL01PERCHIAVE(CNRCTB100.CCNR_DIVISA,CNRCTB100.CCNR_EURO);

   -- Recupera la Data che sar? utilizzata per la registrazione dei documenti.
   --  La data dipende dal'Esercizio di scrivania e dalla SYSDATE;
   aDataRegistrazione:= CNRCTB008.GETTIMESTAMPCONTABILE(aFondo.esercizio_reversale, aTSNow);

   -- Crea una annotazione di giro tronca in parte entrate (Accertamento), per le spese non reintegrate
   aAccert := null;
   -- Crea l'accertamento scadenzario relativo all'accertamento creato in precedenza
   aAccertScad := null;

   -- Crea l'obbligazione in pgiro tronca relativa all'accertamento precedentemente creato
   aObblig := null;
   -- Creo l'obbligazione_scadenzario
   aObbligScad := null;

   creaAnnotPGiroTroncEntrata (aPgCall, aFondo, aAccert, aAccertScad, aObblig, aObbligScad, aUser, null, false);

   aAccertPerRev := null;

   	-- Controlla la data di scrivania

   if (aFondo.esercizio = aFondo.esercizio_reversale)  then
        -- L'esercizio di scrivania corrisponde alla data odierna.
	-- L'esercizio della Reversale sar? uguale all'esercizio del Fondo.
   	aEsRev := aFondo.esercizio;
	aAccertPerRev := aAccert;
   else
        -- L'esercizio di scrivania ? diverso dalla data odierna.
	-- L'esercizio della Reversale sar? successivo a quello di scrivania
   	aEsRev:= aFondo.esercizio_reversale;

        -- 10.01.2008 remmata per nuova gestione
   	-- Riporta l'annot. su pgiro tronca nell'esercizio successivo
	--CNRCTB046.RIPPGIROCDS (aAccert, aAccertRiportata, aDataRegistrazione, aUser);

        CNRCTB035.getPgiroCds(aAccert, aAccertScad, aAccertScadVoce, aObblig, aObbligScad, aObbligScadVoce);

        -- 10.01.2008 SF NUOVA GESTIONE RIBALTAMENTO PARTITE DI GIRO
        CNRCTB046.ripPgiroCdsEntrambe(aObblig, aObbligScad, aObbligScadVoce, aAccert, aAccertScad, aAccertScadVoce, Null, Null, CNRCTB001.GESTIONE_ENTRATE,
                                                      aTSNow, aUser, aObbligRiportata, aAccertRiportata);


	-- L'accertamento che sar? utilizzato per la creazione della Reversale e degli altri docum.
	-- sar? l'accertamento riportato.
	aAccertPerRev:= aAccertRiportata;
   end if;


	-- creo documento generico di entrata per chiusura del fondo economale
   aGen:=null;
   aGen.CD_TIPO_DOCUMENTO_AMM:=CNRCTB100.TI_GEN_CHIUSURA_FONDO;
   aGen.CD_CDS:=aFondo.cd_cds;
   aGen.CD_UNITA_ORGANIZZATIVA:=aFondo.cd_unita_organizzativa;
   aGen.ESERCIZIO:=aEsRev;
   aGen.CD_CDS_ORIGINE:=aFondo.cd_cds;
   aGen.CD_UO_ORIGINE:=aFondo.cd_unita_organizzativa;
   aGen.DATA_REGISTRAZIONE:=TRUNC(aDataRegistrazione);		 -- TRUNC(aTSNow);
   aGen.DT_DA_COMPETENZA_COGE:=TRUNC(aDataRegistrazione);	 -- TRUNC(aTSNow);
   aGen.DT_A_COMPETENZA_COGE:=TRUNC(aDataRegistrazione);	 -- TRUNC(aTSNow);
   aGen.DS_DOCUMENTO_GENERICO:='Generico di chiusura fondo economale: '||aFondo.esercizio||'-'||aFondo.cd_unita_organizzativa||' n.'||aFondo.cd_codice_fondo;
   aGen.TI_ISTITUZ_COMMERC:=CNRCTB100.TI_ISTITUZIONALE;
   aGen.IM_TOTALE:=aAccertPerRev.im_accertamento;	   -- aImReversale;
   aGen.STATO_COFI:=CNRCTB100.STATO_GEN_COFI_TOT_MR;
   aGen.STATO_COGE:=CNRCTB100.STATO_COEP_EXC;
   aGen.STATO_COAN:=CNRCTB100.STATO_COEP_EXC;
   aGen.CD_DIVISA:=aEuro;
   aGen.CAMBIO:=1;
--   aGen.ESERCIZIO_LETTERA:=0;
--   aGen.PG_LETTERA:=0;
   aGen.DACR:=aTSNow;
   aGen.UTCR:=aUser;
   aGen.DUVA:=aTSNow;
   aGen.UTUV:=aUser;
   aGen.PG_VER_REC:=1;
   aGen.DT_SCADENZA:=TRUNC(aDataRegistrazione);		     -- TRUNC(aTSNow);
   aGen.STATO_PAGAMENTO_FONDO_ECO:=CNRCTB100.STATO_NO_PFONDOECO;
   aGen.TI_ASSOCIATO_MANREV:=CNRCTB100.TI_ASSOC_TOT_MAN_REV ;

   docCount:=1;
--   aImResiduoDoc := aImReversale;



   -- Crea la riga del documento generico
   aGenRiga:=null;
   aGenRiga.CD_CDS:=aGen.CD_CDS;
   aGenRiga.CD_UNITA_ORGANIZZATIVA:=aGen.CD_UNITA_ORGANIZZATIVA;
   aGenRiga.ESERCIZIO:=aGen.ESERCIZIO;
   aGenRiga.CD_TIPO_DOCUMENTO_AMM:=aGen.CD_TIPO_DOCUMENTO_AMM;
   aGenRiga.DS_RIGA:=aGen.DS_DOCUMENTO_GENERICO;
   aGenRiga.IM_RIGA_DIVISA:=aAccertPerRev.IM_ACCERTAMENTO; -- (Tot Spese) ??? CORRETTO ???
   aGenRiga.IM_RIGA:=aGenRiga.IM_RIGA_DIVISA;
   aGenRiga.CD_TERZO_UO_CDS:=aCdTerzoUO;
   aGenRiga.CD_TERZO:=aFondo.CD_TERZO;
   aGenRiga.RAGIONE_SOCIALE:=null;
   aGenRiga.NOME:=null;
   aGenRiga.COGNOME:=null;
   aGenRiga.CODICE_FISCALE:=null;
   aGenRiga.PARTITA_IVA:=null;
   aGenRiga.DT_DA_COMPETENZA_COGE:=TRUNC(aDataRegistrazione); -- TRUNC(aTSNow);
   aGenRiga.DT_A_COMPETENZA_COGE:=TRUNC(aDataRegistrazione);   -- TRUNC(aTSNow);
   aGenRiga.STATO_COFI:=aGen.STATO_COFI;
   aGenRiga.CD_CDS_ACCERTAMENTO:=aAccertPerRev.CD_CDS;	   -- aListaAcrScad.CD_CDS;
   aGenRiga.ESERCIZIO_ACCERTAMENTO:=aAccertPerRev.ESERCIZIO; -- aListaAcrScad.ESERCIZIO;
   aGenRiga.ESERCIZIO_ORI_ACCERTAMENTO:=aAccertPerRev.ESERCIZIO_ORIGINALE;  -- aListaAcrScad.ESERCIZIO_ORIGINALE;
   aGenRiga.PG_ACCERTAMENTO:=aAccertPerRev.PG_ACCERTAMENTO;  -- aListaAcrScad.PG_ACCERTAMENTO;
   aGenRiga.PG_ACCERTAMENTO_SCADENZARIO:=aAccertScad.PG_ACCERTAMENTO_SCADENZARIO;  -- aListaAcrScad.PG_ACCERTAMENTO_SCADENZARIO;
   aGenRiga.PG_BANCA_UO_CDS:=aPgBancaUO;
   aGenRiga.CD_MODALITA_PAG_UO_CDS:=aCdModPagUO;
   aGenRiga.DACR:=aGen.DACR;
   aGenRiga.UTCR:=aGen.UTCR;
   aGenRiga.UTUV:=aGen.UTUV;
   aGenRiga.DUVA:=aGen.DUVA;
   aGenRiga.PG_VER_REC:=aGen.PG_VER_REC;
   aGenRiga.TI_ASSOCIATO_MANREV:=aGen.TI_ASSOCIATO_MANREV;


   aListGenRighe(docCount):=aGenRiga;

   CNRCTB110.creaGenericoAggObbAcc(aGen,aListGenRighe);

--- creo la reversale di chiusura fondo
   aRev.CD_CDS:=aFondo.cd_cds;
   aRev.ESERCIZIO:=aEsRev; 	  -- Esercizio stabilito in base al sospeso scelto dall'utente
   aRev.CD_UNITA_ORGANIZZATIVA:=aFondo.cd_unita_organizzativa;
   aRev.CD_CDS_ORIGINE:=aFondo.cd_cds;
   aRev.CD_UO_ORIGINE:=aFondo.cd_unita_organizzativa;
   aRev.CD_TIPO_DOCUMENTO_CONT:=CNRCTB018.TI_DOC_REV;
   aRev.TI_REVERSALE:=CNRCTB038.TI_REV_SOS;
   aRev.TI_COMPETENZA_RESIDUO:=CNRCTB038.TI_MAN_COMP;
   aRev.DS_REVERSALE:='Chiusura fondo economale: '||aFondo.esercizio||'-'||aFondo.cd_unita_organizzativa||' n.'||aFondo.cd_codice_fondo;
   aRev.STATO:=CNRCTB038.STATO_REV_EME;
   aRev.DT_EMISSIONE:=TRUNC(aDataRegistrazione);		 -- TRUNC(aTSNow);
   aRev.STATO_COGE := CNRCTB100.STATO_COEP_INI;
--   aRev.DT_TRASMISSIONE:=;
--   aRev.DT_INCASSO:=;
--   aRev.DT_ANNULLAMENTO:=;
   aRev.IM_REVERSALE:=aAccertPerRev.im_accertamento;			 -- aImReversale;
   aRev.IM_INCASSATO:=0;
   aRev.DACR:=aTSNow;
   aRev.UTCR:=aUser;
   aRev.DUVA:=aTSNow;
   aRev.UTUV:=aUser;
   aRev.PG_VER_REC:=1;
   aRev.STATO_TRASMISSIONE:=CNRCTB038.STATO_REV_TRASCAS_NODIST;

   revCount:=1;
	-- per ogni riga di doc.generico creato per la chiusra del fondo viene creata una riga di reversale
   for i in 1 .. aListGenRighe.count loop
   aRevRiga:=null;
   aRevRiga.CD_CDS:=aRev.cd_cds;
   aRevRiga.ESERCIZIO:=aRev.esercizio;
   aRevRiga.ESERCIZIO_ACCERTAMENTO:=aAccertPerRev.esercizio;
   aRevRiga.ESERCIZIO_ORI_ACCERTAMENTO:=aAccertPerRev.esercizio_originale;
   aRevRiga.PG_ACCERTAMENTO:=aAccertPerRev.pg_accertamento;
   aRevRiga.PG_ACCERTAMENTO_SCADENZARIO:=aAccertScad.pg_accertamento_scadenzario;
   aRevRiga.CD_CDS_DOC_AMM:=aGen.cd_cds;
   aRevRiga.CD_UO_DOC_AMM:=aGen.cd_unita_organizzativa;
   aRevRiga.ESERCIZIO_DOC_AMM:=aGen.esercizio;
   aRevRiga.CD_TIPO_DOCUMENTO_AMM:=aGen.cd_tipo_documento_amm;
   aRevRiga.PG_DOC_AMM:=aGen.pg_documento_generico;
   aRevRiga.DS_REVERSALE_RIGA:=aRev.ds_reversale;
   aRevRiga.STATO:=aRev.STATO;
   aRevRiga.CD_TERZO:=aFondo.cd_terzo;
   aRevRiga.CD_TERZO_UO:=aGenRiga.cd_terzo_uo_cds;
   aRevRiga.PG_BANCA:=aGenRiga.pg_banca_uo_cds;
   aRevRiga.CD_MODALITA_PAG:=aGenRiga.cd_modalita_pag_uo_cds;
   aRevRiga.IM_REVERSALE_RIGA:=aAccertPerRev.im_accertamento;
   aRevRiga.FL_PGIRO:='Y';
   aRevRiga.UTCR:=aUser;
   aRevRiga.DACR:=aTSNow;
   aRevRiga.UTUV:=aUser;
   aRevRiga.DUVA:=aTSNow;
   aRevRiga.PG_VER_REC:=1;
   aRevRighe(i):=aRevRiga;
   revCount:=revCount+1;
   end loop;

   cnrctb037.GENERAREVERSALE( aRev,aRevRighe);
	if (parametri_esercizio.fl_tesoreria_unica='Y' ) then
   aSospeso.cd_cds:= CNRCTB020.GETCDCDSENTE (aFondo.esercizio);
  else
   aSospeso.cd_cds:= aFondo.cd_cds;
  end if;
   aSospeso.esercizio:= aFondo.esercizio_reversale;
   aSospeso.cd_sospeso:= aFondo.cd_sospeso;
   aSospeso.ti_entrata_spesa:= aFondo.ti_es_sospeso;
   aSospeso.ti_sospeso_riscontro:= aFondo.ti_sr_sospeso;

   cnrctb037.GENERADETT_ETR_SOSPESO( aRev,aSospeso, aUser);

end;

procedure chiudiSpese(
  aCdCds varchar2,
  aEsercizio number,
  aCdUnitaOrganizzativa varchar2,
  aCdCodiceFondo varchar2,
  aUser varchar2
 ) is
   aPgCall number;
   aFondo fondo_economale%rowtype;
   aFondoSpesa fondo_spesa%rowtype;
   aVsxFondo vsx_reintegro_fondo%rowtype;
   aCount number;
   aTSNow date;
   aReversale reversale%rowtype;
--   aImReversaleChiusura number(15,2);
   aFl_rev_da_emettere char(1);
   aNumSpeseSuCompensi number;
 begin
  aTSNow:=sysdate;
  begin

  -- Recupera il Fondo Economale
     select * into aFondo from fondo_economale a where
	   cd_cds = aCdCds
	   and cd_unita_organizzativa = aCdUnitaOrganizzativa
       and esercizio = aEsercizio
 	   and cd_codice_fondo = aCdCodiceFondo
     for update nowait;
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('Fondo economale non trovato');
  end;

  -- Controlla che il Fondo sia in stato "Aperto"
  if aFondo.fl_aperto = 'N' then
     IBMERR001.RAISE_ERR_GENERICO('Fondo economale non aperto');
  end if;

    -- Controlla che tutte le spese associate a documenti siano state reintegrate
  select count(*) into aNumSpeseSuCompensi from
				  fondo_spesa b
               where
			         b.esercizio = aEsercizio
			     and b.cd_cds = aCdCds
				 and b.cd_unita_organizzativa =aCdUnitaOrganizzativa
				 and b.cd_codice_fondo = aCdCodiceFondo
				 and b.fl_reintegrata = 'N'
				 and b.fl_documentata = 'Y'
				 and b.cd_tipo_documento_amm = CNRCTB100.TI_COMPENSO;

  if aNumSpeseSuCompensi > 0 then
     IBMERR001.RAISE_ERR_GENERICO('Alcune spese associate a compensi non sono state reintegrate');
  end if;

  -- Controlla i Capitoli Finanziari delle annotazioni su PGiro relative al Fondo Economale
  chkAnnotazoniPGiro (aCdCds, aEsercizio, aCdUnitaOrganizzativa, aCdCodiceFondo);

  -- Controlla l'importo residuo del Fondo, per impostare il flag FL_REV_DA_EMETTERE
  if aFondo.IM_RESIDUO_FONDO = 0 then
  	 aFl_rev_da_emettere := 'N';
  else
  	 aFl_rev_da_emettere:= 'Y';
  end if;

  -- Aggiorna il FL_REV_DA_EMETTERE
  update fondo_economale
  		 set FL_REV_DA_EMETTERE = aFl_rev_da_emettere,
		 	 fl_aperto = 'N'
		 where cd_cds = aCdCds
		 and cd_unita_organizzativa = aCdUnitaOrganizzativa
       	 and esercizio = aEsercizio
 	   	 and cd_codice_fondo = aCdCodiceFondo;


  aPgCall := IBMUTL020.vsx_get_pg_call();
  aCount := 0;

  -- Cicla sul Fondo_spesa ed inserisce nella VSX_REINTEGRO_FONDO le spese non reintegrate
  for aFondoSpesa in (select * from fondo_spesa where
                esercizio=aEsercizio
                and cd_cds=aCdCds
                and cd_unita_organizzativa=aCdUnitaOrganizzativa
                and cd_codice_fondo=aCdCodiceFondo
		  		and fl_reintegrata = 'N'
			  ) loop
     aVsxFondo.PG_CALL:=aPgCall;
     aVsxFondo.PAR_NUM:=aCount;
     aVsxFondo.PROC_NAME:=PROC_NAME_CHIUDI_FONDO;
--     aVsxFondo.MESSAGETOUSER:=;
     aVsxFondo.CD_CDS:=aCdCds;
     aVsxFondo.CD_UO:=aCdUnitaOrganizzativa;
     aVsxFondo.ESERCIZIO:=aEsercizio;
     aVsxFondo.CD_CODICE_FONDO:=aCdCodiceFondo;
     aVsxFondo.PG_FONDO_SPESA:=aFondoSpesa.pg_fondo_spesa;
     aVsxFondo.UTCR:=aUser;
     aVsxFondo.DACR:=sysdate;
     aVsxFondo.UTUV:=aUser;
     aVsxFondo.DUVA:=sysdate;
     aVsxFondo.PG_VER_REC:=0;
	 ins_VSX_REINTEGRO_FONDO( aVsxFondo );

	 aCount:=aCount + 1;
  	 end loop;

   -- Reintegra le spese non ancora reintegrate
   if aCount > 0 then
      vsx_reintegraChiudiSpeseFondo( aPgCall, CNRCTB038.TI_MAN_REG );

      delete from vsx_reintegro_fondo
      where
  	    pg_call = aPgCall and
	    par_num >= 0 and
	    par_num < aCount;

   end if;

 end;

 procedure chkAnnotazoniPGiro(
  aCdCds varchar2,
  aEsercizio number,
  aCdUnitaOrganizzativa varchar2,
  aCdCodiceFondo varchar2
 ) is
 aNumAnnotaz number;
 cd_elemento_voce varchar2(20);
 aObbligazione OBBLIGAZIONE%rowtype;
 begin
 -- Cicla sulla vista V_ASS_MANDATO_FONDO_ECO per recuperare tutti i Mandati associati al Fondo Econ.
 for aV_fondo_econ in (SELECT *
	  			   	   FROM V_ASS_MANDATO_FONDO_ECO
					   WHERE CD_CDS    = aCdCds
					   AND 	 ESERCIZIO = aEsercizio
					   AND 	 CD_UNITA_ORGANIZZATIVA = aCdUnitaOrganizzativa
					   AND 	 CD_CODICE_FONDO = aCdCodiceFondo
					   ) loop

        -- Per ogni riga di mandato, recupera l'obbligazione corrispondente
		for aObblig in (SELECT o.*
						FROM MANDATO_RIGA r,
							 OBBLIGAZIONE o
						WHERE r.CD_CDS    = o.CD_CDS
						AND   r.ESERCIZIO_OBBLIGAZIONE = o.ESERCIZIO
						AND   r.ESERCIZIO_ORI_OBBLIGAZIONE = o.ESERCIZIO_ORIGINALE
						AND   r.PG_OBBLIGAZIONE = o.PG_OBBLIGAZIONE
						AND   r.CD_CDS 	  = aV_fondo_econ.CD_CDS
						AND   r.ESERCIZIO = aV_fondo_econ.ESERCIZIO
						AND   r.PG_MANDATO= aV_fondo_econ.PG_MANDATO
						 ) loop

-- 				-- Per ogni riga di mandato, recupera l'obbligazione corrispondente
-- 				SELECT o.* into aObbligazione
-- 				FROM MANDATO_RIGA r,
-- 					 OBBLIGAZIONE o
-- 				WHERE r.CD_CDS    = o.CD_CDS
-- 				AND   r.ESERCIZIO_OBBLIGAZIONE = o.ESERCIZIO
-- 				AND   r.ESERCIZIO_ORI_OBBLIGAZIONE = o.ESERCIZIO_ORIGINALE
-- 				AND   r.PG_OBBLIGAZIONE = o.PG_OBBLIGAZIONE
-- 				AND   r.CD_CDS 	  = aV_fondo_econ.CD_CDS
-- 				AND   r.ESERCIZIO = aV_fondo_econ.ESERCIZIO
-- 				AND   r.PG_MANDATO= aV_fondo_econ.PG_MANDATO;

				-- Controlla che il CD_ELEMENTO_VOCE sia sempre lo stesso
				if (cd_elemento_voce is null) then
				   cd_elemento_voce := aObblig.CD_ELEMENTO_VOCE;
				else if (cd_elemento_voce <> aObblig.CD_ELEMENTO_VOCE) then
					 IBMERR001.RAISE_ERR_GENERICO('Le annotazioni su partita di giro collegate ai documenti di apertura del Fondo Economale sono state create su capitoli finanziari differenti.');
				end if;
				end if;
		end loop;

	end loop;

 end;

 procedure creaAnnotPGiroTroncEntrata(
   aPgCall number,
   aFondo fondo_economale%rowtype,
   aAccert IN OUT accertamento%rowtype,
   aAccertScad IN OUT accertamento_scadenzario%rowtype,
   aObblig IN OUT obbligazione%rowtype,
   aObbligScad IN OUT obbligazione_scadenzario%rowtype,
   aUser varchar2,
   aImporto number,
   reintegro boolean
 ) is
   aEs number(4);
   aCdCds varchar2(30);
   aCdUo varchar2(30);
   aCdCodiceFondo varchar2(10);
  aDataRegistrazione date;
  aTotSpese number(15,2);
  aAccertTronc accertamento%rowtype;
  aMandato  mandato%rowtype;
  aTSNow date;
  aDsAccert varchar2 (300);
  parametri_esercizio parametri_cnr%rowtype;
 begin
   aTSNow:=sysdate;
   aEs:=aFondo.esercizio;
   aCdCds:=aFondo.cd_cds;
   aCdUo:=aFondo.cd_unita_organizzativa;
   aCdCodiceFondo:=aFondo.cd_codice_fondo;
	parametri_esercizio:=CNRUTL001.getRecParametriCnr(aEs);
  -- Recupera la Data che sar? utilizzata per la registrazione dei documenti.
  --  La data dipende dal'Esercizio di scrivania e dalla SYSDATE;
  aDataRegistrazione:= CNRCTB008.GETTIMESTAMPCONTABILE(aEs, sysdate);

   -- Recupera l'accertamento relativo all'obbligazione legata alla spesa di apertura del Fondo
   select acc.* into aAccertTronc
	from fondo_economale fe
		 ,mandato_riga man_r
		 ,obbligazione obb
	 	 ,ass_obb_acr_pgiro ass
	 	 ,accertamento acc
	where fe.CD_CDS    = aCdCds
	and   fe.ESERCIZIO = aEs
	and   fe.CD_UNITA_ORGANIZZATIVA = aCdUo
	and   fe.CD_CODICE_FONDO 		= aCdCodiceFondo
	-- Join Fondo - Mand. Riga
	and   man_r.CD_CDS    = fe.CD_CDS
	and   man_r.ESERCIZIO = fe.ESERCIZIO
	and   man_r.PG_MANDATO= fe.PG_MANDATO
	-- Join Mand. Riga - Obbligazione
	and   obb.CD_CDS    = man_r.CD_CDS
	and   obb.ESERCIZIO = man_r.ESERCIZIO_OBBLIGAZIONE
	and   obb.ESERCIZIO_ORIGINALE = man_r.ESERCIZIO_ORI_OBBLIGAZIONE
	and   obb.PG_OBBLIGAZIONE = man_r.PG_OBBLIGAZIONE
	-- Join ass_obb_acr_pgiro - obbligazione
	and   ass.CD_CDS    = obb.CD_CDS
	and   ass.ESERCIZIO = obb.ESERCIZIO
	and   ass.ESERCIZIO_ORI_OBBLIGAZIONE = obb.ESERCIZIO_ORIGINALE
	and   ass.PG_OBBLIGAZIONE = obb.PG_OBBLIGAZIONE
	-- Join ass_obb_acr_pgiro - accertamento
	and   acc.CD_CDS    = ass.CD_CDS
	and   acc.ESERCIZIO = ass.ESERCIZIO
	and   acc.ESERCIZIO_ORIGINALE = ass.ESERCIZIO_ORI_ACCERTAMENTO
	and   acc.PG_ACCERTAMENTO = ass.PG_ACCERTAMENTO;

	aDsAccert:= null;

	if (reintegro) then -- La procedura ? stata chiamata per il Reintegro delle Spese per la Chiusura del Fondo
--	    -- Calcola la somma delle spese non reintegrate, che sar? l'importo dell'accertamento.
-- 		   select sum(b.im_ammontare_spesa) into aTotSpese from
-- 						  fondo_spesa b
-- 		               where b.esercizio = aEs
-- 					     and b.cd_cds = aCdCds
-- 						 and b.cd_unita_organizzativa = aCdUo
-- 						 and b.cd_codice_fondo = aCdCodiceFondo
-- 					   	 and exists (select 1 from vsx_reintegro_fondo
-- 		                    	         where
-- 		                                       pg_call = aPgCall
-- 		                    			   and cd_cds = b.cd_cds
-- 		                    			   and cd_uo =b.cd_unita_organizzativa
-- 		                    			   and esercizio = b.esercizio
-- 		                    			   and cd_codice_fondo = b.cd_codice_fondo
-- 		                    			   and pg_fondo_spesa = b.pg_fondo_spesa
-- 		   							);
		aTotSpese:= aImporto;

		aDsAccert:= 'Annotazione d''Entrata su Partita di Giro per regolarizzazione spese fondo economale: '||aFondo.esercizio||'-'||aFondo.cd_unita_organizzativa||' n.'||aFondo.cd_codice_fondo;
    else -- (reintegro = false) -- La procedura ? stata chiamata per la Chiusura del Fondo
	   begin

		   select m.* into aMandato
			from mandato m
			where exists (select 1 from fondo_spesa fs
				where
				      m.cd_cds    = fs.cd_cds
				and   m.esercizio = fs.esercizio
				and	  m.pg_mandato= fs.PG_MANDATO
				and   fs.esercizio= aEs
				and   fs.cd_cds   = aCdCds
				and   fs.cd_unita_organizzativa = aCdUo
				and   fs.cd_codice_fondo = aCdCodiceFondo
				and   m.TI_MANDATO  = 'R');

-- 		 select m.* into aMandato
-- 			from fondo_spesa fs
-- 				,mandato m
-- 			where m.cd_cds    = fs.cd_cds
-- 			and   m.esercizio = fs.esercizio
-- 			and	  m.pg_mandato= fs.PG_MANDATO
-- 			and   fs.esercizio= aEs
-- 			and   fs.cd_cds   = aCdCds
-- 			and   fs.cd_unita_organizzativa = aCdUo
-- 			and   fs.cd_codice_fondo = aCdCodiceFondo
-- 			and m.TI_MANDATO  = 'R';

         aTotSpese:= aFondo.im_ammontare_fondo - aMandato.im_mandato;

	   exception when NO_DATA_FOUND then
	   		aTotSpese:= aFondo.im_ammontare_fondo;
	   end;
--		aTotSpese:= aFondo.im_ammontare_fondo - aMandato.im_mandato;

		aDsAccert:= 'Annotazione d''Entrata su Partita di Giro per chiusura fondo economale: '||aFondo.esercizio||'-'||aFondo.cd_unita_organizzativa||' n.'||aFondo.cd_codice_fondo;

	end if;


	-- Crea una annotazione di giro tronca in parte entrate (Accertamento), per le spese non
   --  reintegrate
   aAccert:=null;
   aAccert.CD_CDS:=aCdCds;
   aAccert.ESERCIZIO:=aEs;
   aAccert.ESERCIZIO_ORIGINALE:=aEs;
   aAccert.CD_TIPO_DOCUMENTO_CONT:='ACR_PGIRO';
   aAccert.CD_UNITA_ORGANIZZATIVA:=aCdUo;
   aAccert.CD_CDS_ORIGINE:=aCdCds;
   aAccert.CD_UO_ORIGINE:=aCdUo;
   if(parametri_esercizio.fl_nuovo_pdg='N' ) then
   		aAccert.TI_APPARTENENZA:='D';
   else
   		aAccert.TI_APPARTENENZA:='C';
   end if;
   aAccert.TI_GESTIONE:='E';
   aAccert.CD_ELEMENTO_VOCE:= aAccertTronc.cd_elemento_voce;
   aAccert.CD_VOCE:= aAccertTronc.cd_voce;
   aAccert.DT_REGISTRAZIONE:= TRUNC(aDataRegistrazione);
   aAccert.DS_ACCERTAMENTO:= aDsAccert;
   aAccert.CD_TERZO:= aFondo.CD_TERZO;
   aAccert.IM_ACCERTAMENTO:=aTotSpese;
   aAccert.FL_PGIRO:='Y';
   aAccert.RIPORTATO:='N';
   aAccert.DACR:=aTSNow;
   aAccert.UTCR:=aUser;
   aAccert.DUVA:=aTSNow;
   aAccert.UTUV:=aUser;
   aAccert.PG_VER_REC:=1;
   aAccert.ESERCIZIO_COMPETENZA:= aFondo.ESERCIZIO;

	-- Crea l'accertamento scadenzario relativo all'accertamento creato in precedenza
   aAccertScad:=null;
   aAccertScad.CD_CDS:= aCdCds;
   aAccertScad.ESERCIZIO:= aEs;
   aAccertScad.DT_SCADENZA_EMISSIONE_FATTURA:=TRUNC(aDataRegistrazione);
   aAccertScad.DT_SCADENZA_INCASSO:=TRUNC(aDataRegistrazione);
   aAccertScad.DS_SCADENZA:= 'Annotazione d''Entrata su Partita di Giro creata in automatico';
   aAccertScad.IM_SCADENZA:=aAccert.IM_ACCERTAMENTO;
   aAccertScad.IM_ASSOCIATO_DOC_AMM:=0;
   aAccertScad.IM_ASSOCIATO_DOC_CONTABILE:=0;
   aAccertScad.DACR:=aTSNow;
   aAccertScad.UTCR:=aUser;
   aAccertScad.DUVA:=aTSNow;
   aAccertScad.UTUV:=aUser;
   aAccertScad.PG_VER_REC:=1;

   -- Crea l'obbligazione in pgiro tronca relativa all'accertamento precedentemente creato
   aObblig:=null;
--    aObblig.CD_CDS:=aCdCds;
--    aObblig.ESERCIZIO:=aEs;
--    aObblig.CD_TIPO_DOCUMENTO_CONT:='OBB_PGIRO';
--    aObblig.CD_UNITA_ORGANIZZATIVA:=aCdUo;
--    aObblig.CD_CDS_ORIGINE:=aCdCds;
--    aObblig.CD_UO_ORIGINE:=aCdUo;
--    aObblig.TI_APPARTENENZA:='D';
--    aObblig.TI_GESTIONE:='S';
--    aObblig.CD_ELEMENTO_VOCE:=aAccertTronc.cd_elemento_voce; -- !!! SBAGLIATO !!!
--    aObblig.DT_REGISTRAZIONE:=aDataRegistrazione;
--    aObblig.DS_OBBLIGAZIONE:='Annotazione di Spesa su Partita di Giro creata in automatico';
--    aObblig.CD_TERZO:= aFondo.CD_TERZO;
--    aObblig.IM_OBBLIGAZIONE:=0;
--    aObblig.IM_COSTI_ANTICIPATI:=0;
--    aObblig.ESERCIZIO_COMPETENZA:=aFondo.ESERCIZIO; --- CORRETTO ??? ----
--    aObblig.STATO_OBBLIGAZIONE:='D';    -- ??? CORRETTO ???
--    aObblig.FL_CALCOLO_AUTOMATICO:='N'; -- ??? CORRETTO ???
--    aObblig.FL_SPESE_COSTI_ALTRUI:='N'; -- ??? CORRETTO ???
--    aObblig.FL_PGIRO:='Y';			   -- ??? CORRETTO ???
--    aObblig.RIPORTATO:='N';			   -- ??? CORRETTO ???
--    aObblig.DACR:=aTSNow;
--    aObblig.UTCR:=aUser;
--    aObblig.DUVA:=aTSNow;
--    aObblig.UTUV:=aUser;
--    aObblig.PG_VER_REC:=1;

   -- Creo l'obbligazione_scadenzario
   aObbligScad:=null;
--    aObbligScad.CD_CDS:= aCdCds;
--    aObbligScad.ESERCIZIO:= aEs;
--    aObbligScad.DT_SCADENZA:=aDataRegistrazione;
--    aObbligScad.DS_SCADENZA:= 'Annotazione di Spesa su Partita di Giro creata in automatico';
--    aObbligScad.IM_SCADENZA:=0;
--    aObbligScad.IM_ASSOCIATO_DOC_AMM:=0;
--    aObbligScad.IM_ASSOCIATO_DOC_CONTABILE:=0;
--    aObbligScad.DACR:=aTSNow;
--    aObbligScad.UTCR:=aUser;
--    aObbligScad.DUVA:=aTSNow;
--    aObbligScad.UTUV:=aUser;
--    aObbligScad.PG_VER_REC:=1;

   CNRCTB040.CREAACCERTAMENTOPGIROTRONC (false, aAccert, aAccertScad, aObblig, aObbligScad, null);

 end;



 procedure creaPraticaApIncFondo(
  aEs number,
  aCdCds varchar2,
  aCdUO varchar2,
  aCdFondo varchar2,
  aCdTerzo varchar2,
  aDescPratica varchar2,
  aImFondo number,
  aOperazione varchar2,
  aUser varchar2
 ) is
  aFondo fondo_economale%rowtype;
  aAnag anagrafico%rowtype;
  aCdTerzoEconomo number(8);
  aCdEV varchar2(20);
  aEV elemento_voce%rowtype;
  aGen documento_generico%rowtype;
  aGenRiga documento_generico_riga%rowtype;
  aObb obbligazione%rowtype;
  aObbScad obbligazione_scadenzario%rowtype;
  aAcc accertamento%rowtype;
  aAccScad accertamento_scadenzario%rowtype;
  aMan mandato%rowtype;
  aManRiga mandato_riga%rowtype;
  aAzione varchar2(10);
  aDescFondo varchar2(1000);
  aManRighe CNRCTB038.righeMandatoList;
  aListGenRighe CNRCTB100.docGenRigaList;
  aTSNow date;
 begin
  aTSNow:=sysdate;
  if
   aEs is null or
   aCdCds is null or
   aCdUO is null or
   aOperazione is null or
   aImFondo is null or
   aUser is null
  then
   IBMERR001.RAISE_ERR_GENERICO('Parametri non completamente specificati');
  end if;

  if
   aImFondo <= 0
  then
   IBMERR001.RAISE_ERR_GENERICO('L''importo della pratica non pu? essere negativo o nullo');
  end if;

  -- Controllo se l'esercizio aEs = a quello della data di sistema
  if to_number(to_char(aTSNow,'YYYY')) != aEs then
   IBMERR001.RAISE_ERR_GENERICO('L''esercizio '||aEs||' ? di anno diverso da quello della data odierna');
  end if;
  -- Check su esercizio
  if
   not CNRCTB008.ISESERCIZIOAPERTO(aEs, aCdCds)
  then
   IBMERR001.RAISE_ERR_GENERICO('L''esercizio '||aEs||' non ? aperto per il CDS '||aCdCds);
  end if;

  if aOperazione = 'A' then
   if aCdTerzo is null or aDescPratica is null then
    IBMERR001.RAISE_ERR_GENERICO('Terzo non specificato per generazione pratica finanziaria di apertura fondo');
   end if;
   aAzione:='apertura';
  elsif aOperazione = 'I' then
   if aCdFondo is null then
    IBMERR001.RAISE_ERR_GENERICO('Codice fondo non specificato per generazione pratica finanziaria di incremento fondo');
   end if;
   aAzione:='incremento';
  else
   IBMERR001.RAISE_ERR_GENERICO('Azione non correttamente specificata: A->apertura I->Incremento');
  end if;

  if aOperazione = 'I' then
   aDescFondo:='Fondo es:'||aEs||' uo:'||aCdUO||' cds:'||aCdCds||' cod.:'||aCdFondo;
   begin
    select * into aFondo from fondo_economale where
          esercizio=aEs
  	  and cd_unita_organizzativa=aCdUO
 	  and cd_cds=aCdCds
 	  and cd_codice_fondo = aCdFondo
    for update nowait;
   exception when NO_DATA_FOUND then
    IBMERR001.RAISE_ERR_GENERICO('Fondo economale non trovato '||aDescFondo);
   end;
  else
   aDescFondo:=aDescPratica;
   aFondo.esercizio:=aEs;
   aFondo.cd_cds:=aCdCds;
   aFondo.cd_unita_organizzativa:=aCdUO;
   aFondo.cd_terzo:=aCdTerzo;
   CNRCTB080.GETMODPAGULTIME(aCdTerzo,aFondo.cd_modalita_pag,aFondo.pg_banca,CNRCTB080.TI_PAGAMENTO_BANCARIO);
  end if;

  aAnag:=CNRCTB080.GETANAG(aFondo.cd_terzo);

  aCdEv:=CNRCTB015.GETVAL01PERCHIAVE(aFondo.esercizio,ELEMENTO_VOCE_SPECIALE,VOCE_APERTURA_FONDO_ECO);
  begin
  	 select * into aEV from elemento_voce where
 	      esercizio = aFondo.esercizio
 	  and ti_appartenenza = CNRCTB001.APPARTENENZA_CDS
 	  and ti_gestione = CNRCTB001.GESTIONE_SPESE
 	  and cd_elemento_voce = aCdEV
 	  and fl_partita_giro='Y';
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('Conto su partita di giro per apertura fondo economale non trovato: '||aCdEV);
  end;
  aObb:=null;
  aObbScad:=null;
  aAcc:=null;
  aAccScad:=null;
  aObb.CD_CDS:=aFondo.cd_cds;
  aObb.ESERCIZIO:=aFondo.esercizio;
  aObb.ESERCIZIO_ORIGINALE:=aFondo.esercizio;
  aObb.CD_TIPO_DOCUMENTO_CONT:=CNRCTB018.TI_DOC_OBB_PGIRO;
  aObb.CD_UNITA_ORGANIZZATIVA:=aFondo.cd_unita_organizzativa;
  aObb.CD_CDS_ORIGINE:=aFondo.cd_cds;
  aObb.CD_UO_ORIGINE:=aFondo.cd_unita_organizzativa;
  aObb.TI_APPARTENENZA:=aEV.ti_appartenenza;
  aObb.TI_GESTIONE:=aEV.ti_gestione;
  aObb.CD_ELEMENTO_VOCE:=aEV.cd_elemento_voce;
  aObb.DT_REGISTRAZIONE:=TRUNC(trunc(aTSNow));
  aObb.DS_OBBLIGAZIONE:='Documento per '||aAzione||' fondo economale. '||aDescFondo;
  aObb.NOTE_OBBLIGAZIONE:='';
  aObb.CD_TERZO:=aFondo.cd_terzo;
  aObb.IM_OBBLIGAZIONE:=abs(aImFondo);
  aObb.stato_obbligazione:=CNRCTB035.STATO_DEFINITIVO;
  aObb.im_costi_anticipati:=0;
  aObb.fl_calcolo_automatico:='N';
  aObb.fl_spese_costi_altrui:='N';
  aObb.FL_PGIRO:='Y';
  aObb.RIPORTATO:='N';
  aObb.DACR:=aTSNow;
  aObb.UTCR:=aUser;
  aObb.DUVA:=aTSNow;
  aObb.UTUV:=aUser;
  aObb.PG_VER_REC:=1;
  aObb.ESERCIZIO_COMPETENZA:=aFondo.esercizio;
  CNRCTB030.CREAOBBLIGAZIONEPGIROTRONC(false,aObb,aObbScad,aAcc,aAccScad,trunc(aTSNow));
  -- Creo il documento generico di spesa su partita di giro per apertura fondo economale
  aGen:=null;
  aGenRiga:=null;
  aGen.CD_TIPO_DOCUMENTO_AMM:=CNRCTB100.TI_GEN_APERTURA_FONDO;
  aGen.CD_CDS:=aObb.cd_cds;
  aGen.CD_UNITA_ORGANIZZATIVA:=aObb.cd_unita_organizzativa;
  aGen.ESERCIZIO:=aObb.esercizio;
  aGen.CD_CDS_ORIGINE:=aObb.cd_cds;
  aGen.CD_UO_ORIGINE:=aObb.cd_unita_organizzativa;
  aGen.IM_TOTALE:=aObb.im_obbligazione;
  aGen.DATA_REGISTRAZIONE:=TRUNC(trunc(aTSNow));
  aGen.DT_DA_COMPETENZA_COGE:=trunc(aTSNow);
  aGen.DT_A_COMPETENZA_COGE:=trunc(aTSNow);
  aGen.DS_DOCUMENTO_GENERICO:='Generico per '||aAzione||' fondo economale. '||aDescFondo;
  aGen.TI_ISTITUZ_COMMERC:=CNRCTB100.TI_ISTITUZIONALE;
  aGen.STATO_COFI:=CNRCTB100.STATO_GEN_COFI_TOT_MR;
  aGen.STATO_COGE:=CNRCTB100.STATO_COEP_EXC;
  aGen.STATO_COAN:=CNRCTB100.STATO_COEP_EXC;
  aGen.CD_DIVISA:='EURO';
  aGen.CAMBIO:=1;
  aGen.DACR:=aTSNow;
  aGen.UTCR:=aUser;
  aGen.DUVA:=aTSNow;
  aGen.UTUV:=aUser;
  aGen.PG_VER_REC:=1;
  aGen.DT_SCADENZA:=null; -- TRUNC(aTSNow);
  aGen.STATO_PAGAMENTO_FONDO_ECO:=CNRCTB100.STATO_NO_PFONDOECO;
  aGen.TI_ASSOCIATO_MANREV:=CNRCTB100.TI_ASSOC_TOT_MAN_REV;
  aGenRiga.CD_CDS:=aGen.CD_CDS;
  aGenRiga.CD_UNITA_ORGANIZZATIVA:=aGen.CD_UNITA_ORGANIZZATIVA;
  aGenRiga.ESERCIZIO:=aGen.ESERCIZIO;
  aGenRiga.CD_TIPO_DOCUMENTO_AMM:=aGen.CD_TIPO_DOCUMENTO_AMM;
  aGenRiga.DS_RIGA:=aGen.DS_DOCUMENTO_GENERICO;
  aGenRiga.IM_RIGA_DIVISA:=aGen.IM_TOTALE;
  aGenRiga.IM_RIGA:=aGen.IM_TOTALE;
  aGenRiga.RAGIONE_SOCIALE:=aAnag.RAGIONE_SOCIALE;
  aGenRiga.NOME:=aAnag.NOME;
  aGenRiga.COGNOME:=aAnag.COGNOME;
  aGenRiga.CODICE_FISCALE:=aAnag.CODICE_FISCALE;
  aGenRiga.PARTITA_IVA:=aAnag.PARTITA_IVA;
  aGenRiga.DT_DA_COMPETENZA_COGE:=trunc(aTSNow);
  aGenRiga.DT_A_COMPETENZA_COGE:=trunc(aTSNow);
  aGenRiga.STATO_COFI:=aGen.STATO_COFI;
 --   aGenRiga.DT_CANCELLAZIONE:=;
 --   aGenRiga.CD_TERZO_CESSIONARIO:=;
 --   aGenRiga.CD_TERMINI_PAG:=;
 --   aGenRiga.CD_TERMINI_PAG_UO_CDS:=;
 --   aGenRiga.NOTE:=;
 --   aGen.ESERCIZIO_LETTERA:=;
 --   aGen.PG_LETTERA:=;
  aGenRiga.CD_TERZO:=aFondo.cd_terzo;
  aGenRiga.CD_MODALITA_PAG:=aFondo.cd_modalita_pag;
  aGenRiga.PG_BANCA:=aFondo.pg_banca;
  aGenRiga.CD_CDS_OBBLIGAZIONE:=aObb.CD_CDS;
  aGenRiga.ESERCIZIO_OBBLIGAZIONE:=aObb.ESERCIZIO;
  aGenRiga.ESERCIZIO_ORI_OBBLIGAZIONE:=aObb.ESERCIZIO_ORIGINALE;
  aGenRiga.PG_OBBLIGAZIONE:=aObb.PG_OBBLIGAZIONE;
  aGenRiga.PG_OBBLIGAZIONE_SCADENZARIO:=aObbScad.PG_OBBLIGAZIONE_SCADENZARIO;
  aGenRiga.DACR:=aGen.DACR;
  aGenRiga.UTCR:=aGen.UTCR;
  aGenRiga.UTUV:=aGen.UTUV;
  aGenRiga.DUVA:=aGen.DUVA;
  aGenRiga.PG_VER_REC:=aGen.PG_VER_REC;
  aGenRiga.TI_ASSOCIATO_MANREV:=aGen.TI_ASSOCIATO_MANREV;
  aListGenRighe(1):=aGenRiga;
  CNRCTB110.CREAGENERICO(aGen,aListGenRighe);
  -- Creazione del mandato
  aMan:=null;
  aManRiga:=null;
  aMan.CD_CDS:=aObb.cd_cds;
  aMan.ESERCIZIO:=aObb.esercizio;
  aMan.CD_UNITA_ORGANIZZATIVA:=aObb.cd_unita_organizzativa;
  aMan.CD_CDS_ORIGINE:=aObb.cd_cds;
  aMan.CD_UO_ORIGINE:=aObb.cd_unita_organizzativa;
  aMan.CD_TIPO_DOCUMENTO_CONT:=CNRCTB018.TI_DOC_MAN;
  aMan.TI_MANDATO:=CNRCTB038.TI_MAN_PAG;
  aMan.TI_COMPETENZA_RESIDUO:=CNRCTB038.TI_MAN_COMP;
  aMan.DS_MANDATO:='Mandato di '||aAzione||' fondo economale. '||aDescFondo;
  aMan.STATO:=CNRCTB038.STATO_AUT_EME;
  aMan.DT_EMISSIONE:=TRUNC(aTSNow);
 --   aMan.DT_TRASMISSIONE:=;
 --   aMan.DT_INCASSO:=;
 --   aMan.DT_ANNULLAMENTO:=;
  aMan.IM_MANDATO:=aObb.im_obbligazione;
  aMan.IM_RITENUTE:=0;
  aMan.IM_PAGATO:=0;
 -- Massimo Iaccarino Fine
  aMan.DACR:=aTSNow;
  aMan.UTCR:=aUser;
  aMan.DUVA:=aTSNow;
  aMan.UTUV:=aUser;
  aMan.PG_VER_REC:=1;
  aMan.STATO_TRASMISSIONE:=CNRCTB038.STATO_AUT_TRASCAS_NODIST;
  aManRiga.CD_CDS:=aMan.cd_cds;
  aManRiga.ESERCIZIO:=aMan.esercizio;
  aManRiga.ESERCIZIO_OBBLIGAZIONE:=aObb.esercizio;
  aManRiga.ESERCIZIO_ORI_OBBLIGAZIONE:=aObb.esercizio_originale;
  aManRiga.PG_OBBLIGAZIONE:=aObb.pg_obbligazione;
  aManRiga.PG_OBBLIGAZIONE_SCADENZARIO:=aObbScad.pg_obbligazione_scadenzario;
  aManRiga.CD_CDS_DOC_AMM:=aGen.cd_cds;
  aManRiga.CD_UO_DOC_AMM:=aGen.cd_unita_organizzativa;
  aManRiga.ESERCIZIO_DOC_AMM:=aGen.esercizio;
  aManRiga.CD_TIPO_DOCUMENTO_AMM:=aGen.cd_tipo_documento_amm;
  aManRiga.PG_DOC_AMM:=aGen.pg_documento_generico;
  aManRiga.DS_MANDATO_RIGA:=aMan.ds_mandato;
  aManRiga.STATO:=aMan.STATO;
  aManRiga.CD_TERZO:=aGenRiga.cd_terzo;
 --   aManRiga.CD_TERZO_UO:=0;
  aManRiga.PG_BANCA:=aGenRiga.PG_BANCA;
  aManRiga.CD_MODALITA_PAG:=aGenRiga.CD_MODALITA_PAG;
  aManRiga.IM_MANDATO_RIGA:=aMan.im_mandato;
  aManRiga.IM_RITENUTE_RIGA:=0;
  aManRiga.FL_PGIRO:='Y';
  aManRiga.UTCR:=aUser;
  aManRiga.DACR:=aTSNow;
  aManRiga.UTUV:=aUser;
  aManRiga.DUVA:=aTSNow;
  aManRiga.PG_VER_REC:=1;
  aManRighe(1):=aManRiga;
  -- Aggiornamento saldi scadenza obbligazione
  update obbligazione_scadenzario set
      im_associato_doc_amm = im_associato_doc_amm + aGen.im_totale,
      duva=aTSNow,
      utuv=aUser,
  	pg_ver_rec = pg_ver_rec+1
  where
          cd_cds = aObbScad.cd_cds
  	and esercizio = aObbScad.esercizio
  	and esercizio_originale = aObbScad.esercizio_originale
  	and pg_obbligazione= aObbScad.pg_obbligazione
  	and pg_obbligazione_scadenzario = aObbScad.pg_obbligazione_scadenzario;
  CNRCTB037.GENERADOCUMENTO(
    aMan,
    aManRighe);
 end;

 procedure creaPraticaAperturaFondo(
  aEs number,
  aCdCds varchar2,
  aCdUO varchar2,
  aCdTerzo varchar2,
  aDescPratica varchar2,
  aImFondo number,
  aUser varchar2
 ) is
 begin
  creaPraticaApIncFondo(
   aEs,
   aCdCds,
   aCdUO,
   null,
   aCdTerzo,
   aDescPratica,
   aImFondo,
   'A',
   aUser
  );
 end;

 procedure creaPraticaIntegrazioneFondo(
  aEs number,
  aCdCds varchar2,
  aCdUO varchar2,
  aCdFondo varchar2,
  aImFondo number,
  aUser varchar2
 ) is
 begin
  creaPraticaApIncFondo(
   aEs,
   aCdCds,
   aCdUO,
   aCdFondo,
   null,
   null,
   aImFondo,
   'I',
   aUser
  );
 end;

END;
/


