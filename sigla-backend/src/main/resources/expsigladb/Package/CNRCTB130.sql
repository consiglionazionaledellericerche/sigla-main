--------------------------------------------------------
--  DDL for Package CNRCTB130
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRCTB130" AS
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
-- banca e modalità di pagamento quelle dell'economo.
-- Per ogni scadenza di obbligazione collegata al gruppo da reintegrare, viene creata una riga di documento generico.
--
-- Date: 31/05/2002
-- Version: 1.9
-- Fix errori
--
-- Date: 03/06/2002
-- Version: 2.0
-- Recupero della modalità di pagamento (e banca) con dacr più elevato per il terzo specificato in obbligazione
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
-- Recupero delle modalità di pagamento del terzo OBB più recenti con metodo di servizio del package 80
--
-- Date: 21/06/2002
-- Version: 2.9
-- Fix date, Fix ti_associato_man_rev
--
-- Date: 01/07/2002
-- Version: 3.0
-- Il generico di reintegro è escluso da COEP/COAN
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
-- è stata aggiunta la procedura ChiudiSpese, (v.).
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
-- Se il mandato è di regolarizzazione pg_banca e CD_MODALITA_PAG devono essere a null sul reintegro spese non doc.
--
-- Date: 28/01/2004
-- Version: 3.22
-- Fix errori 756 e 757
-- La reversale di regolarizzazione in chiusura del fondo è esclusa COEP
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
-- L'operazione di reintegro consiste nell'incremento della liquidità del fondo attraverso
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
-- Tale operazione può essere richiesta dall'utente su alcune spese specifiche oppure
-- può essere fatta in automatico alla chiusura del fondo su tutte le spese non ancora reintegrate.
-- L'operazione di reintegro consiste nell'incremento della liquidità del fondo attraverso
-- l'emissione di un mandato all'economo per la copertura (banca) delle spese sostenute per cassa
-- Nel caso di reintegro manuale il mandato è un mandato di pagamento, nel caso di reintegro
-- automatico alla chiusura del fondo il mandato è di regolarizzazione con conseguente reversale.
--
-- Pre-post name: Non tutte le spese collegate ad obbligazione
-- Pre: Esistono spese, tra quelle selezionate per il reintegro del fondo, non collegate ad
-- obbligazioni
-- Post: Viene sollevata un'eccezione
--
-- Pre-post name: Alcune spese già reintegrate
-- Pre: Esistono spese, tra quelle selezionate per il reintegro del fondo, già reintegrate
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
-- Pre: Nessuna delle precondizioni è verificata e il parametro aTiMandato = PAGAMENTO
-- Post:
--  Esecuzione reintegro
--    Fase 1. generazione documento generico per spese non documentate
--     f1. le spese vengono aggregate per scadenza di obbligazione collegata
--     f2. per ogni obbligazione viene creato un documento generico con n-righe pari alle scadenze
--         collegate a spese da reintegrare tale generico eredita il terzo dell'obbligazione: come modalità e
--         termini di pagamento di tale terzo vengono recuperati i primi disponibili per tale terzo
--    Fase 2. generazione mandato di reintegro
--     f1. viene preparato un mandato associato agli n-generici delle spese non documentate + m-doc
--         amministrativi collegati a spese documentate (non compensi): tale mandato è
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
-- Pre: Nessuna delle precondizioni è verificata e il parametro aTiMandato = REGOLARIZZAZIONE
-- Post:
--  Esecuzione reintegro
--    Fase 1. generazione documento generico per spese non documentate
--     f1. le spese vengono aggregate per scadenza di obbligazione collegata
--     f2. per ogni obbligazione viene creato un documento generico con n-righe pari alle scadenze
--         collegate a spese da reintegrare tale generico eredita il terzo dell'obbligazione: come modalità e
--         termini di pagamento di tale terzo vengono recuperati i primi disponibili per tale terzo
--    Fase 2. generazione mandato di regolarizzazione per reintegro
--     f1. viene preparato un mandato di regolarizzazione associato agli n-generici delle spese
--         non documentate + m-doc amministrativi collegati a spese documentate (non compensi): tale mandato è
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
--   aTiMandato -> può assumetre il valore CNRCTB038.TI_MAN_PAG per il reintegro manuale
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
-- pre: Un record di tipo Vsx_reintegro_fondo è stato preparato
-- post: Il record è stato inserito in tabella
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
-- pre:  Non è stato trovato alcun Fondo Economale corrispondente ai parametri passati.
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
--       è stato emesso
-- post: 1 - Viene creata una annotazione di giro tronca in parte entrata (v. creaAnnotPGiroTroncEntrata).
--	 2 - Viene creato un documento amministrativo generico di regolarizzazione
--           di importo pari al mandato di regolarizzazione. Tale documento viene contabilizzato
--           sull'accertamento/i in partita di giro legato all'obbligazione/i su cui è stato contabilizzato
--           il documento/i generico di apertura del fondo
--       3 - Viene creata una reversale di regolarizzazione sul documento generico creato al punto 1
--       4 - Viene creata l'associazione fra il mandato e la reversale di regolarizzazione
--            (procedure 'cnrctb038.ins_ASS_MANDATO_REVERSALE')
--
-- Parametri:
--
--    aMan -> IL mandato di regolarizzazione già creato
--    aFondo -> Il fondo economale su cui è necessario creare la reversale di regolarizzazione
--    aPgCall -> numero di chiamata VSX determinato da applicazione JAVA


 procedure creaReversaleRegolarizzazione(
 aMan MANDATO%rowtype,
 aFondo FONDO_ECONOMALE%rowtype,
 aPgCall number
 );

-- Creazione della reversale di chiusura del fondo economale,
--
-- pre-post-name: Disponibilità accertamenti in partita di giro
-- pre:  La disponibilità delle scadenze degli accertamenti in partita di giro associati alle
--       obbligazioni su cui sono stati contabilizzati i documenti di apertura del fondo economale
--       è inferiore all'importo della reversale da generare
-- post: Viene sollevata un'eccezione
--
-- pre-post-name: Creazione reversale di chiusura del fondo
-- pre:  Nessun'altra precondizione verificata
-- post: 1 - Viene creato un documento amministrativo generico di chiusura del fondo economale
--           di importo pari al residuo iniziale del fondo. Tale documento viene contabilizzato
--           sull'accertamento/i in partita di giro legato all'obbligazione/i su cui è stato contabilizzato
--           il documento/i generico di apertura del fondo
--       2 - Viene creata una reversale a regolamento sospeso sul documento generico creato al punto 2
--
--
-- Parametri:
--    aFondo -> Il fondo economale su cui è necessario creare la reversale di chiusura
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
-- pre:  Non è stato trovato alcun Fondo Economale corrispondente ai parametri passati.
-- post: Viene sollevata un'eccezione
--
-- pre-post-name: Fondo non aperto
-- pre:  Lo stato del fondo è chiuso
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
-- post: 1 - Viene impostato il fl_rev_da_emettere. Se l'importo residuo del Fondo è = 0, il flag viene
--	      impostato a N, (non è necessario emettere la reversale quando si chiude il Fondo).
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
-- La procedura è richiamata sia in fase di chiusura delle Spese del Fondo Economale, sia per la chiusura del
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
-- pre: La procedura è stata chiamata per il Reintegro delle Spese per la Chiusura del Fondo
-- post: L'importo dell'annotazione è valorizzato come l'importo del mandato di regolarizzazione.
--
-- pre-post-name: Chiusura del Fondo Economale.
-- pre: La procedura è stata chiamata per la Chiusura del Fondo.
-- post: L'importo dell'annotazione è valorizzato come l'ammontare del Fondo prima che venissero reintegrate le spese non reintegrate.
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
-- aImporto -> rappresenta l'importo del mandato di regolarizzazione, (è valorizzato nei casi di reintegro delle spese)
-- reintegro -> flag che indica se la procedura è stata chiamata per Reintegro delle Spese, (reintegro = true),
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
-- aCdTerzo -> Codice terzo dell'economo (vengono estratte le informazioni modpag e banca bancarie più recenti)
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
