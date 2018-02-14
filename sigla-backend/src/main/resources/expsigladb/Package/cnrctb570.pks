CREATE OR REPLACE PACKAGE         CNRCTB570 As
--==============================================================================
--
-- CNRCTB570 - Liquidazione contributi/ritenute
--
-- Date: 14/07/2006
-- Version: 7.23
--
-- Dependency: CNRCTB 001/015/018/020/037/038/040/043/080/100/110/575 IBMERR 001
--
-- History:
--
-- Date: 02/06/2002
-- Version: 1.0
-- Creazione
--
-- Date: 04/06/2002
-- Version: 1.1
-- Aggiunta gestione accentramento CORI
--
-- Date: 06/06/2002
-- Version: 1.2
-- Ristrutturazione liquidazione
--
-- Date: 09/06/2002
-- Version: 1.3
-- Fix errori
--
-- Date: 13/06/2002
-- Version: 1.4
-- Fix errori
--
-- Date: 17/06/2002
-- Version: 1.5
-- Test approfondito
--
-- Date: 18/06/2002
-- Version: 1.6
-- Modificato nome della vista utilizzata per calcolare la liquidazione
--
-- Date: 24/06/2002
-- Version: 1.7
-- Controllo estrazione cori su invio/emissione del mandato collegato al compenso
--
-- Date: 24/06/2002
-- Version: 1.8
-- Revisione tabelle per gestione versamento accentrato
--
-- Date: 25/06/2002
-- Version: 1.9
-- Gestione CORI accentrati
--
-- Date: 26/06/2002
-- Version: 2.0
-- Fix su generazione mandato di versamento
--
-- Date: 26/06/2002
-- Version: 2.1
-- Fix su aggiornamento stato liquidazione
--
-- Date: 01/07/2002
-- Version: 2.2
-- Documenti generici tutti esclusi da COEP/COAN
--
-- Date: 03/07/2002
-- Version: 2.3
-- Fix errori
--
-- Date: 04/07/2002
-- Version: 2.4
-- Fix errori
-- Aggiunto esercizio origine documento aut. e fl_accentrato su liquidazione gruppo
-- Tale flag influenza lo stato dell'intera liquidazione
--
-- Date: 11/07/2002
-- Version: 2.5
-- Fix errore estrazione CORI
--
-- Date: 11/07/2002
-- Version: 2.6
-- Fix errore in liquidazione CORI
--
-- Date: 12/07/2002
-- Version: 2.7
-- Fix errore ordinamento estrazione CORI
--
-- Date: 12/07/2002
-- Version: 2.8
-- Fix errore ordinamento estrazione CORI per UO=SAC
-- Fix errore chiave per estrazione CORI
--
-- Date: 12/07/2002
-- Version: 2.9
-- Impostato il codice del documento di versamento in liquid gruppo cori
--
-- Date: 12/07/2002
-- Version: 3.0
-- Trunc su data scadenza accertamento pgiro
--
-- Date: 18/07/2002
-- Version: 3.1
-- Aggiornamento documentazione
--
-- Date: 23/07/2002
-- Version: 3.2
-- Annullamento della liquidazione CORI
-- Blocco del versamento nel caso il totale relativo ad un gruppo CORI sia negativo
-- Gestione liquidazione per dettaglio capillare (GRUPPO CORI + REGIONE + COMUNE)
--
-- Date: 24/07/2002
-- Version: 3.3
-- Revisione della liquidazione CORI
--
-- Date: 24/07/2002
-- Version: 3.4
-- Fix revisione
--
-- Date: 25/07/2002
-- Version: 3.5
-- Eliminazione della partita di giro creata sul'UO del centro che versa a seguito dei liquidazione locale accentrata
--
-- Date: 25/07/2002
-- Version: 3.6
-- Fix aggiornamento saldo coll. doc amm su obb o acc collegato a generico
--
-- Date: 25/07/2002
-- Version: 3.7
-- Fix liquidazione centrale in raccolta liq. locali
--
-- Date: 25/07/2002
-- Version: 3.8
-- Fix query estrazione accentrati
--
-- Date: 13/09/2002
-- Version: 3.9
-- Fix su fl_partita_giro = 'Y' in riga mandato di chiusura CORI
--
-- Date: 11/10/2002
-- Version: 4.0
-- Fix su chiusura partita di giro dei CORI negativi -> collegamento errato del generico a obbligazione invece che accertamento
--
-- Date: 14/10/2002
-- Version: 4.1
-- Corretta gestione CORI negativi come terzo e importo totale generico
--
-- Date: 15/10/2002
-- Version: 4.2
-- Fix generazione reversale su chiusura partita di giro cori negativi
--
-- Date: 21/10/2002
-- Version: 4.3
-- Fix inizializzazione vettore righe mandato (segnalazione CINECA del 20021018)
--
-- Date: 18/11/2002
-- Version: 4.4
-- Il doc generico di versamento cori ? sempre di tipo versamento (entrata/spesa) non trasferimento nei caso di cori accentrati
--
-- Date: 29/01/2003
-- Version: 5.0
-- Aggiunto esercizio in query estrazione liquidazione UO diverse da quelle di liquidazione in calcolo della liquidazione
--
-- Date: 07/02/2003
-- Version: 5.1
-- Aggiunto calcolo liquidazione da interfaccia -- Solo per Pisa
--
-- Date: 11/02/2003
-- Version: 5.2
-- Fix per controllo di versamento completo liquidazioni accentrate al versamento del centro
-- Fix su estrazione cori del centro per versamento cori propri del centro
-- Nel calcolo considera anche i CORI provenienti da liquidazione speciale via interfaccia
--
-- Date: 12/02/2003
-- Version: 5.3
-- Rimosse insert di controllo
--
-- Date: 20/02/2003
-- Version: 6.0
-- Nuova gestione della pratica per versamenti accentrati
--
-- Date: 20/02/2003
-- Version: 6.1
-- Fix
--
-- Date: 20/02/2003
-- Version: 6.2
-- Fix
--
-- Date: 21/02/2003
-- Version: 6.3
-- Inserito controllo per il calcolo della liquidazione da interfaccia
-- il controllo genera un errore se la UO legata alla liquid_gruppo_interf
-- risulta appartenere ad un gruppo per cui il flag_accentrata = 'N'
--
-- Date: 22/02/2003
-- Version: 6.4
-- Fix
--
-- Date: 24/02/2003
-- Version: 6.5
-- Modificate regole di annullamento liquidazione di gruppi
--
-- Date: 25/02/2003
-- Version: 6.6
-- Fix errore di calcolo della liquidazione
--
-- Date: 27/02/2003
-- Version: 6.7
-- Fix errore ripetizione liquidazione CORI locale via interfaccia
--
-- Date: 03/03/2003
-- Version: 6.8
-- Fix su errore buffer aUtente di soli 10 caratteri
--
-- Date: 14/03/2003
-- Version: 6.9
-- fIX ERR. 530 importo ritenute su mandato di versamento CORI
--
-- Date: 07/05/2003
-- Version: 7.0
-- Modifiche per gestione liquidazione CORI in chiusura
--
-- Date: 22/05/2003
-- Version: 7.1
-- Annullamento liquidazione CORI in chiusura
--
-- Date: 13/06/2003
-- Version: 7.2
-- Fix estr. obb./acc. in liq. dell'anno + fix calcola liq.
--
-- Date: 13/06/2003
-- Version: 7.3
-- Controllo apertura/chiusura esercizio + sistemazione date registrazione e competenza
--
-- Date: 23/06/2003
-- Version: 7.4
-- Fix per impedire di riliquidare in esercizi diversi gli stessi CORI
--
-- Date: 23/06/2003
-- Version: 7.5
-- gestione completa della restituzione alle UO in credito e reversale di versamento gruppi locali negativi al centro
-- che va in ritenuta sul mandato al centro del gruppo CORI
--
-- Date: 24/06/2003
-- Version: 7.6
-- Nuova versione metodi di riporto pratiche finanziarie ad esercizio successivo
--
-- Date: 25/06/2003
-- Version: 7.7
-- Controllo liquidazione locale/centro aperta + liq iva via interf da es. prec.
--
-- Date: 26/06/2003
-- Version: 7.8
-- Fix test err. minori su liq. iva interfaccia + sistemazione agg. stato liq. locali
--
-- Date: 19/07/2003
-- Version: 7.9
-- Gestione liquidazione cori via interfaccia per dettaglio
--
-- Date: 20/07/2003
-- Version: 7.10
-- Fix su clean up dettaglio interfaccia nel caso il gruppo non venga liquidato
--
-- Date: 23/07/2003
-- Version: 7.11
-- Tolti riferimenti a tabella LIQUID_CORI_INTERF
--
-- Date: 31/07/2003
-- Version: 7.12
-- Fix errore in raggruppamento dati di interfaccia dettaglio CORI: pg_caricamento ora ? ignorato
--
-- Date: 23/10/2003
-- Version: 7.13
-- Fix su lock del compenso in calcola: utilizzo esercizio_compenso e non esercizio
--
-- Date: 27/11/2003
-- Version: 7.14
-- Fix errore su liq. gruppi nulli non di fine anno
--
-- Date: 08/12/2003
-- Version: 7.15
-- Fix mancato aggiornamento pg_obb_accentr su liquid_gruppo_cori_locale alla creazione obb accentr
-- sul gruppo CORI al centro
--
-- Date: 09/01/2004
-- Version: 7.16
-- Fix errore di mancato reset righe mandato di restituzione crediti
--
-- Date: 03/02/2004
-- Version: 7.17
-- Fix errore 729
-- La liquidazione al centro ? bloccata quando quella locale ? bloccata
--
-- Date: 23/04/2004
-- Version: 7.18
-- Le reversali di versamento CORI devono essere processate in ECONOMICA
--
-- Date: 13/04/2005
-- Version: 7.19
-- Gestiti i Versamenti Unificati per tutte le UO della SAC: Esiste in CONFIGURAZIONE_CNR una UO abilitata
-- ad effettuare i versamenti di tutte le UO della SAC
--
-- Date: 23/06/2005
-- Version: 7.20
-- Gestita l'interfaccia per i nuovi cds accorpati. A differenza di quello gia' esistente (PISA) che alimenta
-- l'archivio LIQUID_CORI_INTERF_DET, per i nuovi e' stato gestito l'archivio LIQUID_CORI_INTERF in cui i dati
-- sono gia' accorpati.
--
-- Date: 30/12/2005
-- Version: 7.21
-- Consentita la liquidazione di gruppi negativi solo "Da Esercizio precedente" e solo per i
-- gruppi accentrati. Inoltre, per i gruppi dei versamenti locali, il controllo dell'importo negativo
-- non deve essere fatto solo da aGruppi.im_liquidato perch? esso contiene solo la parte positiva
-- bens? deve essere sottratta la parte negativa presente in V_LIQUID_CENTRO_UO e se la differenza ?
-- negativa la liquidazione deve essere bloccata.
-- Gestito con RAISE_WRN_GENERICO e non con RAISE_ERR_GENERICO il messaggio 'Nessun gruppo CORI trovato
-- per la liquidazione' per poterlo intercettare nella Liquidazione CORI massiva.
--
-- Date: 19/04/2006
-- Version: 7.22
-- Gestite le scadenze per le pgiro di entrata per consentire la chiusura di una Reversale Provvisoria
-- legata a gruppi CORI accentrati ancora aperti. In questo caso, cio? se il gruppo ? aperto ma la Rev. Provv.
-- ? chiusa, viene creata una nuova scadenza per la pgiro di entrata ed una nuova rev. provv. a cui agganciarla
--
-- Date: 14/07/2006
-- Version: 7.23
-- Gestione Impegni/Accertamenti Residui:
-- aggiornata la funzione per tener conto anche del campo Esercizio Originale Impegno/Accertamento
--
-- Date: 28/12/2010
-- Version: 7.24
-- Gestione Liquidazione gruppi F24EP dalla UO ENTE 999.000
--
--
-- Date: 24/11/2015
-- Version: 7.25
-- Gestione Liquidazione con tesoreria unica
--
-- Creazione Package.
--
--==============================================================================
--
-- Constants
--


 type rec_ass_pgiro is record(
 											 cd_cds      		varchar2(30),
 											 cd_uo			 		varchar2(30),
 											 cd_cds_orig 		varchar2(30),
 											 cd_uo_orig	 		varchar2(30),
                        esercizio   		number(4),
                        es_compenso 		number(4),
                        pg_compenso 		number(10),
                        pg_liq      		number(8),
                        pg_liq_orig 		number(8),
                        cd_gr_cr    		varchar2(10),
                        cd_regione  		varchar2(10),
                        pg_comune   		number(10),
                        cd_cori     		varchar2(10),
                        ti_en_per   		varchar2(1),
                        ti_origine	 		varchar2(1),	--'E' o 'S'
                        es_acc					number(4),
                        es_ori_acc			number(4),
                        cds_acc				varchar2(30),
                        pg_acc					number(10),
                        uo_acc					varchar2(30),
                        es_obb					number(4),
                        es_ori_obb			number(4),
                        cds_obb				varchar2(30),
                        pg_obb					number(10),
                        uo_obb					varchar2(30),
 											 cd_cds_acc_pgiro 	varchar2(30),
                        es_acc_pgiro   		number(4),
                        es_orig_acc_pgiro 	number(4),
                        pg_acc_pgiro       number(10),
												 uo_acc_pgiro				varchar2(30),
												 voce_acc_pgiro			varchar2(20),
 											 cd_cds_obb_pgiro 	varchar2(30),
                        es_obb_pgiro   		number(4),
                        es_orig_obb_pgiro 	number(4),
                        pg_obb_pgiro       number(10),
                        ti_origine_pgiro	  varchar2(1),	--'E' o 'S'
												 uo_obb_pgiro				varchar2(30),
												 voce_obb_pgiro			varchar2(20));

 type tab_ass_pgiro is table of rec_ass_pgiro index by binary_integer;
 tb_ass_pgiro            tab_ass_pgiro;

--
-- Functions e Procedures
--

-- Calcolo della liquidazione
--
-- pre-post-name: Parametri non specificati correttamente
-- pre: alcuni parametri della liquidazione non sono stati specificati correttamente
-- post: viene sollevata un'eccezione
--
-- pre-post-name: Calcolo della liquidazione effettuato dall'unit? organizzativa di versamento per l'esercizio
-- pre: l'unit? organizzativa che effettua il calcolo ? UO di versamento per l'esercizio
-- post: genera la testata della liquidazione valorizzandola dai contributi ritenuta e la inserisce in LIQUID_CORI.
-- Estrae l'UO del SAC responsabile del versamento CORI accentrato. Per ogni unit? organizzativa, che abbia generato
-- un mandato di pagamento al SAC, genera una riga di liquidazione in LIQUID_GRUPPO_CORI, per quei
-- contributi che siano stati inviati nell'ambito del periodo di validit? del contributo stesso (Data di emissione/invio cassiere
-- del mandato di pagamento emesso dalle UO locali verso l'UO di versamento della SAC).
-- Lo stato dei dettagli propri e raccolti da altre UO ? uguale a L (Liquidato)
-- Vedi pre-post 'Calcolo della liquidazione per tutte le unit? organizzative non responsabili del versamento
-- accentrato per l'esercizio.'
--
-- pre-post-name: Calcolo della liquidazione per tutte le unit? organizzative : gruppo CORI non trovato
-- pre: per il contributo ritenuta in esame non viene trovato il gruppo cui appartiene
-- post: viene sollevata un'eccezione
--
-- pre-post-name: Calcolo della liquidazione "da esercizio precedente"
-- pre: l'utente selziona il tipo di liquidazione "da esercizio precedente"
-- post: in questo caso vengono estratti tutti i CORI relativi a compensi con mandato inviato o emesso nel periodo specificato
--       e nell'esercizio appena precedente a quello in processo.
--
-- pre-post-name: Calcolo della liquidazione per tutte le unit? organizzative non responsabili del versamento
-- accentrato per l'esercizio
-- pre: non ? soddisfatta nessuna pre-post precedente
-- post: per i contributi ritenuta che non siano gi? stati processati, estrae l'informazione di versamento accentrato
-- per il gruppo di appartenenza del contributo ritenuta, aggiorna il totale del contributo e definisce come accentrato
-- o meno il contributo ritenuta (se si tratta dell'unit? organizzativa responsabile del versamento centralizzato, non
-- c'? accentramento; se si tratta di un'altra UO, l'accentramento ? definito a seconda del gruppo di appartenenza del
-- contributo e dell'unit? organizzativa per i comportamenti in deroga al gruppo in funzione della UO), genera la
-- testata di liquidazione in stato I (Iniziale) ed inserisce i dettagli in LIQUID_GRUPPO_CORI per ogni gruppo con stato
-- uguale a L per i gruppi a versamento diretto e T per quelli a versamento accentrato.
-- Per ogni gruppo di versamento inserisce in LIQUID_GRUPPO_CORI_DET il riferimento al contributo ritenuta dei compensi
--
-- relativi.
--
-- Parametri:
-- aCdCds -> CdS che effettua il calcolo della liquidazione
-- aEs -> esercizio di riferimento
-- daEsercizioPrec -> 'Y' -> compensi con mandato in esercizio precedente al corrente 'N' compensi con mandato nell'esercizio corrente
-- aCdUo -> unit? organizzativa che effettua il calcolo della liquidazione
-- aPgLiq -> numero progressivo della liquidazione
-- aDtDa -> data di inizio validit?
-- aDtA -> data fine validit?
-- aUser -> utente che effettua la modifica

 procedure calcolaLiquidazione(aCdCds varchar2, aEs number, daEsercizioPrec char, aCdUo varchar2, aPgLiq number, aDtDa date, aDtA date, aUser varchar2);

-- Liquidazione contributi ritenuta
--
-- La contabilizzazione dei CORI consiste nei seguenti passi:
-- 1.	I CORI vengono aggregati per fl_accentrato
-- 2.	I CORI accentrati vengono gestiti come segue:
-- 2.1.	viene creato un documento generico di trasferimento per la chiusura della partita di giro aperta dal CORI
-- 2.2.	viene generato un mandato verso il SAC su tale generico
-- 2.3  nell'UO della SAC che versa viene aggiornata una partita di giro complessiva definita per GRUPPO/REGION/COMUNE
--      che viene chiusa con il versamento della SAC centralizzato
-- 3.	I CORI non accentrati vengono gestiti come segue:
-- 3.1.	viene creato un documento generico di liquidazione CORI per la chiusura della partita di giro aperta dal CORI
-- 3.2.	viene generato un mandato verso il terzo specificato in GRUPPO_CR
--
-- pre-post-name: Nessun gruppo contributo ritenuta specificato per la liquidazione
-- pre: nessun gruppo contributo ritenuta individuato per numero di chiamata
-- post: viene sollevata un'eccezione
--
-- pre-post-name: Liquidazione CORI non trovata
-- pre: non trova le informazioni di testata relative alla liquidazione del contributo ritenuta
-- post: viene sollevata un'eccezione
--
-- pre-post-name: Associazione tra obbligazioni ed accertamenti su partita di giro non trovata
-- pre: l'unit? organizzativa che effettua la liquidazione non ? responsabile del versamento centralizzato dei
-- contributi ritenuta, non trova l'associazione tra obbligazioni ed accertamenti su partita di giro parte spese
-- per il contributo ritenuta in ASS_OBB_ACR_PGIRO
-- post: viene sollevata un'eccezione
--
-- pre-post-name: Esiste pi? di un conto finanziario associato a un gruppo CR via i suoi CORI
-- pre: Esistono almeno 2 voci del piano diverse associate a CORI raccolti nello stesso gruppo di versamento
-- post: viene sollevata un.eccezione
--
-- pre-post-name: Obbligazione di contropartita CORI non trovata in UO non responsabile versamento centralizzato
-- pre: l'unit? organizzativa che effettua la liquidazione non ? responsabile del versamento centralizzato dei
-- contributi ritenuta, non trova l'obbligazione collegata all'accertamento in partita di giro in OBBLIGAZIONE.
-- post: viene sollevata un'eccezione
--
-- pre-post-name: Scadenza dell'obbligazione di contropartita CORI non trovata in UO non responsabile versamento centralizzato
-- pre: l'unit? organizzativa che effettua la liquidazione non ? responsabile del versamento centralizzato dei
-- contributi ritenuta, non trova la scadenza dell.obbligazione in OBBLIGAZIONE_SCADENZIARIO.
-- post: viene sollevata un.eccezione
--
-- pre-post-name: Accertamento in partita di giro per il CdS origine non trovata, per gruppi di contributi ritenuta
-- accentrati
-- pre: l'unit? organizzativa che effettua la liquidazione non ? responsabile del versamento centralizzato dei
-- contributi ritenuta, non trova l'annotazione di entrata su partita di giro origine per il contributo ritenuta
-- post: viene sollevata un'eccezione
--
-- pre-post-name: Terzo corrispondente all'unit? organizzativa non trovato in anagrafica, per gruppi di contributi
-- ritenuta accentrati
-- pre: l'unit? organizzativa che effettua la liquidazione non ? responsabile del versamento centralizzato dei
-- contributi ritenuta, non trova il terzo corrispondente all'unit? organizzativa in anagrafica
-- post: viene sollevata un'eccezione
--
-- pre-post-name: Liquidazione dei contributi ritenuta da parte di unit? organizzativa non responsabile del versamento
-- centralizzato per l'esercizio
-- pre: l'unit? organizzativa che effettua la liquidazione non ? responsabile del versamento centralizzato dei
-- contributi ritenuta
-- post: per ogni gruppo contributo ritenuta (comprensivo di regione comune), di cui si vuole effettuare la liquidazione, selezionato dall'applicazione,
-- genera la testata di un mandato principale di versamento dei contributi ritenuta valorizzato dai parametri della
-- liquidazione. Per ogni contributo ritenuta appartenente al gruppo in esame, in ASS_OBB_ACR_PGIRO individua
-- associazione fra obbligazione ed accertamento su partita di giro parte spese per il contributo ritenuta generata
-- dalla liquidazione del compenso, individua l'obbligazione collegata in OBBLIGAZIONE e le relative scadenze in OBBLIGAZIONE_SCADENZIARIO.
-- Se la liquidazione del gruppo contributo ritenuta in esame ? effettuato direttamente dall'unit? organizzativa:
-- crea un documento generico di spesa su partita di giro collegato all'annotazione di speda su partita di giro aperta dalla liquidazione
-- compenso (le righe del generico sono valorizzate anche in funzione dell'anagrafica del terzo), genera i
-- dettagli del mandato di pagamento associato al documento generico.
-- Se la liquidazione del gruppo contributo ritenuta in esame ? accentrata ad un'altra unit? organizzativa responsabile
-- del versamento: verifica l'esistenza di un'obbligazione su partita di giro definita nell'UO di versamento.
-- Il riferimento a tale obbligazione ? registrato nella tabella LIQUID_GRUPPO_CENTRO definita per GRUPPO/REGIONE/COMUNE ? con stato = I (Iniziale)
--  Se tale riferimento non viene trovato, crea un accertamento su UO di versamento centralizzato in partita di giro
--   utilizzando il capitolo (che deve essere unico) per il gruppo cori in processo.
--   Viene creato un nuovo riferimento (nuovo PG_GRUPPO_CENTRO) in LIQUID_GRUPPO_CENTRO all'obbligazione di contropartita creato in stato=I (Iniziale)
--   Viene creato un documento generico di entrata collegata all'accertamento e una reversale provvisoria collegata all'accertamento stesso.
--   L'indicazione della contropartita di spesa (obbligazione) creata sull'unit? organizzativa che accentra i versamenti viene iscritta in LIQUIDAZIONE_GRUPPO_CORI
--   per recuperare tale informazione in fase di liquidazione al centro.
--  Se tale riferimento viene trovato, viene recuperata la pratica ACCERTAMENTO/GENERICO/REVERSALE PROVVISORIA e questa
--   viene aggiornata in delta dell'importo del gruppo di liquidazione locale in processo.
-- Viene quindi creato un documento generico di trasferimento spese collegato all'annotazione di spesa su partita di giro aperta
-- con la liquidazione del compenso (le righe del generico sono valorizzate anche in funzione dell'anagrafica del terzo), genera i
-- dettagli del mandato di pagamento associato al documento generico.
-- Aggiorna lo stato della testata della liquidazione da I (iniziale) a:
--  se esistono gruppi accentrati nella liquidazione, lo stato diventa T (trasferito)
--  altrimenti L (liquidato).
--
-- pre-post-name: Liquidazione contributi ritenuta di origine non trovata
-- pre: l'unit? organizzativa che effettua la liquidazione ? responsabile del versamento centralizzato dei contributi
-- ritenuta, non trova la liquidazione dei contributi ritenuta di origine in LIQUID_CORI
-- post: viene sollevata un'eccezione
--
-- pre-post-name: Gruppo contributi ritenuta di origine non trovato
-- pre: l'unit? organizzativa che effettua la liquidazione ? responsabile del versamento centralizzato dei contributi
-- ritenuta, non trova il gruppo contributi ritenuta di origine in LIQUID_GRUPPO_CORI
-- post: viene sollevata un.eccezione
--
-- pre-post-name: Obbligazione non trovata per versamento accentrato
-- pre: l'unit? organizzativa che effettua la liquidazione ? responsabile del versamento centralizzato dei contributi
-- ritenuta, non trova l'obbligazione, dell'annotazione aperta su partita di giro per il versamento accentrato in
-- OBBLIGAZIONE
-- post: viene sollevata un.eccezione
--
-- pre-post-name: Scadenza dell'obbligazione non trovata per il versamento accentrato
-- pre: l'unit? organizzativa che effettua la liquidazione ? responsabile del versamento centralizzato dei contributi
-- ritenuta, non trova la scadenza dell'obbligazione in OBBLIGAZIONE_SCADENZIARIO
-- post: viene sollevata un.eccezione
--
-- pre-post-name: Liquidazione contributi ritenuta da parte dell'unit? organizzativa di versamento per l'esercizio
-- pre: l'unit? organizzativa che effettua la liquidazione ? responsabile del versamento centralizzato dei contributi
-- ritenuta
-- post: per ogni gruppo contributo ritenuta, di cui si vuole effettuare la liquidazione, selezionato dall'applicazione,
-- genera la testata di un mandato principale di versamento dei contributi ritenuta valorizzato dai parametri della
-- liquidazione.
-- Gestione del versamento dei contributi ritenuta accentrati da altre unit? organizzative: individua la liquidazione
-- di origine in LIQUID_CORI, individua il gruppo di contributi ritenuta di origine in LIQUID_GRUPPO_CORI, individua
-- l'obbligazione emessa da altra unit? organizzativa (annotazione su partita di giro aperta per versamento accentrato)
-- in OBBLIGAZIONE, individua la scadenza in OBBLIGAZIONE_SCADENZIARIO, crea un documento generico di versamento dei
-- contributi ritenuta su partita di giro collegato all'annotazione di spesa su partita di giro aperta dalle UO di
-- versamento locali sui gruppi accentrati, genera i dettagli del mandato di pagamento associato al documento generico.
-- Nella tabella LIQUID_GRUPPO_CENTRO viene aggiornato lo stato relativo al gruppo accentrato da I (Iniziale) a C (Chiuso)
-- Vengono cercati nelle liquidazioni locali tutti i dettagli di gruppo che referenziano il pg_gruppo_centro in LIQUID_GRUPPO_CENTRO
-- ed il loro stato viene portato a L (liquidato) da T (trasferito). Se tutti i dettagli di gruppo della liquidazione sono
-- in stato L (liquidato ) anche la testata (LIQUID_CORI) passa in stato L.
-- Gestione del versamento dei propri contributi ritenuta: per ogni contributo ritenuta appartenente al gruppo in
-- esame, in ASS_OBB_ACR_PGIRO individua l'annotazione su partita di giro parte spese per il contributo ritenuta,
-- individua l'obbligazione collegata in OBBLIGAZIONE e le relative scadenze in OBBLIGAZIONE_SCADENZIARIO, crea un
-- documento generico di spesa su partita di giro collegato all'annotazione di spesa su partita di giro aperta dalla
-- liquidazione del compenso (le righe del generico sono valorizzate anche in funzione dell'anagrafica del terzo), genera
-- i dettagli del mandato di pagamento associato al documento generico.
--
-- pre-post-name: Liquidazione di gruppi CORI locali "da esercizio precedente" con saldo negativo
-- pre: l'utente decide di liquidare gruppi CORI locali targati "da esercizio precedente" con saldo negativo
-- post: in questo caso localmente viene creata una partita di giro da Obbligazione per compensazione della
-- quota negativa (saldo negativo) CORI su gruppo locale per quell'UO. Su tale obbligazione viene creato un
-- doc. generico di VERS. SPESA e riga di mandato che chiude a 0 il netto con la reversale di chiusura dei CORI locali negativi.
-- In questo modo l'UO locale resta in attesa della restituzione delle quote a credito dal centro.
-- Centralmente viene creata una partita di giro di restituzione all'UO (terzo Obbligazione = terzo UO) che viene registrata nella tabella
-- LIQUID_GRUPPO_CENTRO_COMP sotto forma di accertamento di contropartita dell'obbligazione.
--
-- pre-post-name: Liquidazione di gruppi CORI centrale con presenza di saldi locali a credito
-- pre: l'utente del centro decide di liquidare gruppi CORI al centro ('999')
--      per tali gruppi sono presenti in LIQUID_GRUPPO_CENTRO_COMP dei crediti locali da restituire alle rispettive UO
-- post: per ogni credito locale di UO viene generato un generico di versamento e relativo mandato di restituzione
--       all'UO origine, mentre la contropartita di entrata viene chiusa con un generico di VERSAMENTO di ENTRATA e reversale
--       che contiene l'intero credito locale delle UO sul gruppo CORI in processo.
--       Se l'importo del mandato di liquidazione all'erario ? superiore o uguale all'importo della reversale
--       dei CREDITI locali, tale reversale viene collegata al mandato in modo da pagare per il netto l'erario.
--       Altrimenti viene creato un mandato di versamento all'erario ed una reversale libera per la parte a credito.
--       Tale reversale viene registrata nella riga LIQUID_GRUPPO_CORI '999' in processo nei campi xxx_rev.
--
-- Parametri:
--
-- pgCall -> numero progressivo della chiamata alla stored procedure


 procedure vsx_liquida_cori(
       pgCall NUMBER
 );

 -- Annulla una liquidazione CORI come effetto collaterale dell'annullamento del mandato relativo
 --
 -- pre-post-name: Non ? possibile annullare il versamento accentrato nel caso un gruppo locale faccia parte del MANDATO
 -- pre: Il mandato collegato a aLGC ? quello di versamento accentrato ed esiste un GRUPPO LOCALE presente in quel mandato
 -- post: viene sollevata un'eccezione
 --
 -- pre-post-name: Non ? possibile annullare il trasferimento al centro se la liquidazione al centro ? gia stata effettuata
 -- pre: Il mandato collegato a aLGC ? quello di trasferimento a CORI e la liquidazione ? in stato L (Liquidata)
 -- post: viene sollevata un'eccezione
 --
 -- pre-post-name: Non ? possibile modificare la partita di giro aperta sul centro per il versamento accentrato
 -- pre: Il gruppo CORI ? accentrato e l'obbligazione creato al centro collegata all'accertamento generato per trasferimento
 -- liquidazione risulta chiusa da liquidazione centralizzata (stato C chiuso in LIQUID_GRUPPO_CENTRO che referenzia l'obbligazione)
 -- post: viene sollevata un'eccezione
 --
 -- pre-post-name: Annullamento liquidazione gruppo cori
 -- pre: Nessuna delle precondizioni precedenti ? verificata
 -- post:
 --   Vengono eliminati tutti i dettagli di aLOGC da liquidazione gruppo cori dettaglio
 --   Viene modificato in diminuzione della quota di aLOGC la pratica su partita di giro creato sull'UO di versamento accentrato
 --   Viene eliminato aLOGC da liquidazione gruppo cori
 --
 -- Parametri:
 --     aLGCori -> Rowtype del gruppo cori collegato a mandato annullato
 procedure annullaLiquidazione(aLGCori liquid_gruppo_cori%rowtype, aUser  varchar2);

 function IsCdsInterfDet (aCdCds varchar2, aEs number) return boolean;
 function IsCdsInterfTot (aCdCds varchar2, aEs number) return boolean;
 procedure calcolaLiquidInterf (aCdCds varchar2, aEs number, daEsercizioPrec char, aCdUo varchar2, aPgLiq number, aDtDa date, aDtA date, aUser varchar2);
 procedure calcolaLiquidInterfTot (aCdCds varchar2, aEs number, daEsercizioPrec char, aCdUo varchar2, aPgLiq number, aDtDa date, aDtA date, aUser varchar2);

-- Funzione di creazione del riferimento in LIQUID_GRUPPO_CENTRO all'accertamento su pgiro aperto dalle liquidazioni di gruppi locali accentrati

 function creaGruppoCentro(aL liquid_cori%rowtype, aAggregato liquid_gruppo_cori%rowtype,aTSNow date, aUser varchar2) return liquid_gruppo_centro%rowtype;

-- Ritorna il record di LIQUID_GRUPPO_CENTRO corrispondente all'aggregato aAggregato (colonna pg_gruppo_centro in LIQUID_GRUPPO_CORI)

 function getGruppoCentro(aL liquid_cori%rowtype, aAggregato liquid_gruppo_cori%rowtype) return liquid_gruppo_centro%rowtype;

-- Ritorna il record di LIQUID_GRUPPO_CENTRO aperto in corrispondenza del gruppo di aAggregato.

 function getGruppoCentroAperto(aL liquid_cori%rowtype, aAggregato liquid_gruppo_cori%rowtype) return liquid_gruppo_centro%rowtype;

-- Crea/Aggiorna la pratica finanziaria al centro per liquidazione di gruppi accentrati
-- Registra in LIQUID_GRUPPO_CENTRO l'obbligazione di controparte generata al centro
-- Crea/Modifica generico e reversale provvisoria

 procedure aggiornaPraticaGruppoCentro(aL liquid_cori%rowtype, aAggregato liquid_gruppo_cori%rowtype,aUOVERSACC unita_organizzativa%rowtype,aTSNow date,aUser varchar2, tb_ass_pgiro IN OUT tab_ass_pgiro);

 function IsGruppoF24EP (aEs number, aGruppo varchar2) return boolean;
 function GruppoF24EP (aEs number, aGruppo varchar2) return varchar2;
 function GruppoValido (aEs number, aGruppo varchar2, aCdUo varchar2) return varchar2;

 Procedure CREALIQUIDCORIASSPGIRO(aLiquid liquid_cori%rowtype,aGruppo varchar2, aRegione varchar2, aComune number,aTipo varchar2,aObbNew obbligazione%rowtype,aObbOld obbligazione%rowtype,aAccNew accertamento%rowtype,aAccOld accertamento%rowtype,aUser varchar2,aTSNow date);

 Procedure CREA_ASS_PGIRO_GR_C(tb_ass_pgiro tab_ass_pgiro, aUser varchar2, aTSNow date);

END;


CREATE OR REPLACE PACKAGE BODY         CNRCTB570 AS
 procedure annullaLiquidazione(aLGCori liquid_gruppo_cori%rowtype, aUser varchar2) is
  aLC liquid_cori%rowtype;
  aUOVERSACC unita_organizzativa%rowtype;
  aUOVERSCONTOBI unita_organizzativa%rowtype;
  aTSNow date;
  aGC liquid_gruppo_centro%rowtype;
  aLGCC liquid_gruppo_centro_comp%rowtype;
  aAcc accertamento%rowtype;
  aAccScad accertamento_scadenzario%rowtype;
  aAccScadVoce accertamento_scad_voce%rowtype;
  aObb obbligazione%rowtype;
  aObbScad obbligazione_scadenzario%rowtype;
  aObbScadVoce obbligazione_scad_voce%rowtype;
  aAccC accertamento%rowtype;
  aAccCScad accertamento_scadenzario%rowtype;
  aAccCScadVoce accertamento_scad_voce%rowtype;
  aObbC obbligazione%rowtype;
  aObbCScad obbligazione_scadenzario%rowtype;
  aObbCScadVoce obbligazione_scad_voce%rowtype;
  recParametriCNR PARAMETRI_CNR%Rowtype;
 Begin
  aTSNow:=sysdate;
  aUOVERSACC:=CNRCTB020.getUOVersCori(aLGCori.esercizio);
  aUOVERSCONTOBI:=CNRCTB020.getUOVersCoriContoBI(aLGCori.esercizio);
  recParametriCNR := CNRUTL001.getRecParametriCnr(aLGCori.esercizio);
  
  If aLGCori.cd_unita_organizzativa = aUOVERSCONTOBI.cd_unita_organizzativa then
        IBMERR001.RAISE_ERR_GENERICO('Non ? possibile eliminare il versamento dei CORI che rientrano nel Modello F24EP');
  End If;      
    
  -- Leggo la testata della liquidazione (lock)
  select * into aLC from liquid_cori where
       cd_cds = aLGCori.cd_cds
   and esercizio = aLGCori.esercizio
   and cd_unita_organizzativa = aLGCori.cd_unita_organizzativa
   and pg_liquidazione = aLGCori.pg_liquidazione
   for update nowait;

  -- Se il versamento ? quello dell'UO di versamento, e il gruppo di cui annullare la liquidazione ? composto di SOLI
  -- dati propri dell'UO che versa, posso smontare la liquidazione di quel gruppo

  if aLGCori.cd_unita_organizzativa = aUOVERSACC.cd_unita_organizzativa then
   declare
    aTempN number;
   begin
    select distinct 1 into aTempN from liquid_gruppo_centro where
         cd_cds_lc = aLGCori.cd_cds
     and esercizio = aLGCori.esercizio
     and cd_uo_lc = aLGCori.cd_unita_organizzativa
     and pg_lc = aLGCori.pg_liquidazione
     and cd_gruppo_cr = aLGCori.cd_gruppo_cr
     and cd_regione = aLGCori.cd_regione
     and pg_comune = aLGCori.pg_comune;
    IBMERR001.RAISE_ERR_GENERICO('Non ? possibile eliminare il versamento accentrato dei CORI perch? la liquidazione raccoglie versamenti di altre UO per il gruppo'||CNRCTB575.getDesc(aLGCori));
   exception when NO_DATA_FOUND then
    null;
   end;
  end if;

  -- Devo modificare la partita di giro sull'UO che versa legata a aLGCori con fl_accentrato a Y

  if aLGCori.fl_accentrato = 'Y' then
   -- Solo se lo stato ? diverso da liquidato posso smontare
   if aLGCori.stato = CNRCTB575.STATO_LIQUIDATO then
    IBMERR001.RAISE_ERR_GENERICO('La liquidazione CORI al centro risulta gi? chiusa per il gruppo CORI '||CNRCTB575.getDesc(aLGCori));
   end if;
   begin
    aGC:=getGruppoCentro(aLC, aLGCori);
   exception when NO_DATA_FOUND then
    IBMERR001.RAISE_ERR_GENERICO('Riferimento a gruppo centro di liquidazione non trovato per il gruppo CORI '||CNRCTB575.getDesc(aLGCori));
   end;
   if aGC.stato = CNRCTB575.STATO_GRUPPO_CENTRO_CHIUSO then
    IBMERR001.RAISE_ERR_GENERICO('La liquidazione CORI al centro risulta gi? chiusa per il gruppo CORI '||CNRCTB575.getDesc(aLGCori));
   end if;
   if aLGCori.im_liquidato > 0 then
		IF recParametriCNR.FL_TESORERIA_UNICA = 'Y' THEN
/*    	aObbGiro := null;
    	aObbGiro.esercizio:=aGC.esercizio_obb_accentr;
	  	aObbGiro.cd_cds:=aGC.cd_cds_obb_accentr;
  		aObbGiro.esercizio_originale:=aGC.esercizio_ori_obb_accentr;
  		aObbGiro.pg_obbligazione:=aGC.pg_obb_accentr;
    	
    	CNRCTB035.GETPGIROCDSINV(aObbGiro,aObbScadGiro,aObbScadVoceGiro,aAccGiro,aAccScadGiro,aAccScadVoceGiro);
    	
    	CNRCTB043.modificaPraticaAcc(aAccGiro.esercizio,aAccGiro.cd_cds,aAccGiro.esercizio_originale,aAccGiro.pg_accertamento,0-aLGCori.im_liquidato,aTSNow,aUser);*/
 	    IBMERR001.RAISE_ERR_GENERICO('Impossibile annullare la liquidazione CORI nel caso di tesoreria unica');
		else
    	CNRCTB043.modificaPraticaObb(aGC.esercizio_obb_accentr,aGC.cd_cds_obb_accentr,aGC.esercizio_ori_obb_accentr,aGC.pg_obb_accentr,0-aLGCori.im_liquidato,aTSNow,aUser,'Y');
		end if;
   elsif aLGCori.im_liquidato < 0 then
    -- Elimina la partita di giro di restituzione e la riga in LIQUID_GRUPPO_ENTRO_COMP
	begin
     select * into aLGCC from liquid_gruppo_centro_comp where
                 esercizio = aLGCori.esercizio
             and cd_gruppo_cr = aLGCori.cd_gruppo_cr
             and cd_regione = aLGCori.cd_regione
             and pg_comune = aLGCori.pg_comune
			 and pg_gruppo_centro = aLGCori.pg_gruppo_centro
			 and cd_unita_organizzativa = aLGCori.cd_unita_organizzativa
			 for update nowait;
	 aAcc.cd_cds:=aLGCC.cd_cds_acc_accentr;
	 aAcc.esercizio:=aLGCC.esercizio_acc_accentr;
	 aAcc.esercizio_originale:=aLGCC.esercizio_ori_acc_accentr;
	 aAcc.pg_accertamento:=aLGCC.pg_acc_accentr;
		IF recParametriCNR.FL_TESORERIA_UNICA = 'Y' THEN
 	    IBMERR001.RAISE_ERR_GENERICO('Impossibile annullare la liquidazione CORI nel caso di tesoreria unica');
		else
	 		CNRCTB035.GETPGIROCDSINV(aAcc,aAccScad,aAccScadVoce,aObb,aObbScad,aObbScadVoce);
     	CNRCTB035.annullaObbligazione(aObb.cd_cds,aObb.esercizio,aObb.esercizio_originale,aObb.pg_obbligazione,aUser);
     	-- Elimina la partita di giro di compensazione locale
     	if aLGCori.pg_acc_compens is not null then
 	  		aAccC.cd_cds:=aLGCori.cd_cds_acc_compens;
	  		aAccC.esercizio:=aLGCori.esercizio_acc_compens;
	  		aAccC.esercizio_originale:=aLGCori.esercizio_ori_acc_compens;
	  		aAccC.pg_accertamento:=aLGCori.pg_acc_compens;
	  		CNRCTB035.GETPGIROCDSINV(aAccC,aAccCScad,aAccCScadVoce,aObbC,aObbCScad,aObbCScadVoce);
	  		CNRCTB035.annullaObbligazione(aObbC.cd_cds,aObbC.esercizio,aObbC.esercizio_originale,aObbC.pg_obbligazione,aUser);
     	end if;
		end if;
     delete from liquid_gruppo_centro_comp where
                 esercizio = aLGCori.esercizio
             and cd_gruppo_cr = aLGCori.cd_gruppo_cr
             and cd_regione = aLGCori.cd_regione
             and pg_comune = aLGCori.pg_comune
			 and pg_gruppo_centro = aLGCori.pg_gruppo_centro
			 and cd_unita_organizzativa = aLGCori.cd_unita_organizzativa;
    exception when NO_DATA_FOUND then
 	 null;
	end;
   else
    null;
   end if;
  end if; -- FINE AGGIORNAMENTO PARTITA DI GIRO CREATA AL CENTRO

  -- Elimino il dettaglio minimo della liquidazione di aLGCori
  delete from liquid_gruppo_cori_det where
       cd_cds = aLGCori.cd_cds
   and esercizio = aLGCori.esercizio
   and cd_unita_organizzativa = aLGCori.cd_unita_organizzativa
   and pg_liquidazione = aLGCori.pg_liquidazione
   and cd_cds_origine = aLGCori.cd_cds_origine
   and cd_uo_origine = aLGCori.cd_uo_origine
   and pg_liquidazione_origine = aLGCori.pg_liquidazione_origine
   and cd_gruppo_cr = aLGCori.cd_gruppo_cr
   and cd_regione = aLGCori.cd_regione
   and pg_comune = aLGCori.pg_comune;

  delete from ASS_PGIRO_GRUPPO_CENTRO where
       cd_cds = aLGCori.cd_cds
   and esercizio = aLGCori.esercizio
   and cd_UO = aLGCori.cd_unita_organizzativa
   and pg_liq = aLGCori.pg_liquidazione
   and cd_cds_orig = aLGCori.cd_cds_origine
   and cd_uo_orig = aLGCori.cd_uo_origine
   and pg_liq_orig = aLGCori.pg_liquidazione_origine
   and cd_gr_cr = aLGCori.cd_gruppo_cr
   and cd_regione = aLGCori.cd_regione
   and pg_comune = aLGCori.pg_comune;

  -- Elimino aLGCori
  delete from liquid_gruppo_cori where
       cd_cds = aLGCori.cd_cds
   and esercizio = aLGCori.esercizio
   and cd_unita_organizzativa = aLGCori.cd_unita_organizzativa
   and pg_liquidazione = aLGCori.pg_liquidazione
   and cd_cds_origine = aLGCori.cd_cds_origine
   and cd_uo_origine = aLGCori.cd_uo_origine
   and pg_liquidazione_origine = aLGCori.pg_liquidazione_origine
   and cd_gruppo_cr = aLGCori.cd_gruppo_cr
   and cd_regione = aLGCori.cd_regione
   and pg_comune = aLGCori.pg_comune;

   -- Se non ci sono sotto la liquidazione gruppi TRASFERITI (stato T) posso mettere la liquidazione a Liquidata (L)
   declare
    aTempC char(1);
   begin
    select distinct stato into aTempC from liquid_gruppo_cori where
         cd_cds = aLGCori.cd_cds
     and esercizio = aLGCori.esercizio
     and cd_unita_organizzativa = aLGCori.cd_unita_organizzativa
     and pg_liquidazione = aLGCori.pg_liquidazione
	 and stato = CNRCTB575.STATO_TRASFERITO;
   exception when NO_DATA_FOUND Then
    update liquid_cori
	set
	 stato = CNRCTB575.STATO_LIQUIDATO,
     duva = aTSNow,
     utuv = aUser,
	 pg_ver_rec = pg_ver_rec + 1
	where
        cd_cds = aLGCori.cd_cds
    and esercizio = aLGCori.esercizio
    and cd_unita_organizzativa = aLGCori.cd_unita_organizzativa
    and pg_liquidazione = aLGCori.pg_liquidazione;
   end;

   -- Se non ci sono altre righe nella liquidazione, elimino anche la testata (LIQUID_CORI)
   -- LIQUID_CORI per aUOVERSACC.cd_unita_organizzativa potrebbe avere altri dettagli, quindi
   -- viene trattata a parte
   declare
    aLocG liquid_gruppo_cori%rowtype;
   begin
    select * into aLocG from liquid_gruppo_cori where
            esercizio = aLGCori.esercizio
        and pg_liquidazione = aLGCori.pg_liquidazione
        And cd_cds_origine = aLGCori.cd_cds_origine
        and cd_uo_origine = aLGCori.cd_uo_origine
	for update nowait;
   exception
    when TOO_MANY_ROWS then
	 null;
    when NO_DATA_FOUND Then
     delete from liquid_cori where
            cd_cds = aLGCori.cd_cds_origine
        and esercizio = aLGCori.esercizio
        and cd_unita_organizzativa = aLGCori.cd_uo_origine
        and pg_liquidazione = aLGCori.pg_liquidazione
        And cd_unita_organizzativa != aUOVERSACC.cd_unita_organizzativa
        And cd_unita_organizzativa != aUOVERSCONTOBI.cd_unita_organizzativa;
   end;
   -- Se non ci sono altre righe nella liquidazione per aUOVERSACC.cd_unita_organizzativa, 
   -- elimino anche la testata (LIQUID_CORI)
   declare
    aLocG liquid_gruppo_cori%rowtype;
   begin
    select * into aLocG from liquid_gruppo_cori where
            cd_cds = aLGCori.cd_cds
        and esercizio = aLGCori.esercizio
        and cd_unita_organizzativa = aLGCori.cd_unita_organizzativa
        and pg_liquidazione = aLGCori.pg_liquidazione
        And cd_unita_organizzativa = aUOVERSACC.cd_unita_organizzativa
	for update nowait;
   exception
    when TOO_MANY_ROWS then
	 null;
    when NO_DATA_FOUND Then
     delete from liquid_cori where
            cd_cds = aLGCori.cd_cds
        and esercizio = aLGCori.esercizio
        and cd_unita_organizzativa = aLGCori.cd_unita_organizzativa
        and pg_liquidazione = aLGCori.pg_liquidazione
        And cd_unita_organizzativa = aUOVERSACC.cd_unita_organizzativa;
   end;
  null;
 end;

 function getGruppoCentroAperto(aL liquid_cori%rowtype, aAggregato liquid_gruppo_cori%rowtype) return liquid_gruppo_centro%rowtype is
  aLGC liquid_gruppo_centro%rowtype;
 begin
  select * into aLGC from liquid_gruppo_centro where
       esercizio = aAggregato.esercizio
   and cd_gruppo_cr = aAggregato.cd_gruppo_cr
   and cd_regione = aAggregato.cd_regione
   and pg_comune = aAggregato.pg_comune
   and da_esercizio_precedente  = aL.da_esercizio_precedente
   and stato = CNRCTB575.STATO_GRUPPO_CENTRO_INIZIALE
   for update nowait;
  return aLGC;
 end;

 function getGruppoCentro(aL liquid_cori%rowtype, aAggregato liquid_gruppo_cori%rowtype) return liquid_gruppo_centro%rowtype is
  aLGC liquid_gruppo_centro%rowtype;
 begin
  select * into aLGC from liquid_gruppo_centro where
       esercizio = aAggregato.esercizio
   and cd_gruppo_cr = aAggregato.cd_gruppo_cr
   and cd_regione = aAggregato.cd_regione
   and pg_comune = aAggregato.pg_comune
   and pg_gruppo_centro = aAggregato.pg_gruppo_centro
   and da_esercizio_precedente  = aL.da_esercizio_precedente
   for update nowait;
  return aLGC;
 end;

 function creaGruppoCentroComp(aL liquid_cori%rowtype, aLGC liquid_gruppo_centro%rowtype, aAggregato liquid_gruppo_cori%rowtype,aTSNow date, aUser varchar2) return liquid_gruppo_centro_comp%rowtype is
  aLGCC liquid_gruppo_centro_comp%rowtype;
 begin
  aLGCC.ESERCIZIO:=aLGC.esercizio;
  aLGCC.CD_GRUPPO_CR:=aLGC.cd_gruppo_cr;
  aLGCC.CD_REGIONE:=aLGC.cd_regione;
  aLGCC.PG_COMUNE:=aLGC.pg_comune;
  aLGCC.PG_GRUPPO_CENTRO:=aLGC.pg_gruppo_centro;
  aLGCC.cd_unita_organizzativa:=aAggregato.cd_unita_organizzativa;
  aLGCC.DACR:=aTSNow;
  aLGCC.UTCR:=aUser;
  aLGCC.DUVA:=aTSNow;
  aLGCC.UTUV:=aUser;
  aLGCC.PG_VER_REC:=1;
  begin
   CNRCTB575.INS_LIQUID_GRUPPO_CENTRO_COMP(aLGCC);
  exception when DUP_VAL_ON_INDEX then
   -- L'inserimento pu? dare un errore di chiave duplicata se due sessioni tentano di inserire
   -- il primo record per lo stesso gruppo di versamento: la prima inserisce, la seconda deve essere bloccata
   -- e restituire l'errore di risorsa occupata
   IBMERR001.RAISE_ERR_GENERICO('Esiste gi? una liquidazione con saldo negativo sul gruppo'||aLGCC.cd_gruppo_cr||'.'||aLGCC.cd_regione||'.'||aLGCC.pg_comune||' per l''UO:'||aLGCC.cd_unita_organizzativa);
  end;
  return aLGCC;
 end;

 procedure restituzioneCrediti(aLiquid liquid_cori%rowtype,Cds In Out VARCHAR2,Uo In Out VARCHAR2,terzo_versamento In Out VARCHAR2, aLGC liquid_gruppo_centro%rowtype, aTotReversale IN OUT number, aRevP IN OUT reversale%rowtype, aListRigheRevP IN OUT CNRCTB038.righeReversaleList, aUser varchar2, aTSNow date) is
   aGen documento_generico%rowtype;
   aGenRiga documento_generico_riga%rowtype;
   aListGenRighe CNRCTB100.docGenRigaList;
   aGenVE documento_generico%rowtype;
   aGenVERiga documento_generico_riga%rowtype;
   aListGenVERighe CNRCTB100.docGenRigaList;
   aAcc accertamento%rowtype;
   aAccScad accertamento_scadenzario%rowtype;
   aAccScadVoce accertamento_scad_voce%rowtype;
   aAccNew accertamento%rowtype;
   aAccScadNew accertamento_scadenzario%rowtype;
   aAccScadVoceNew accertamento_scad_voce%rowtype;   
   aObb obbligazione%rowtype;
   aObbScad obbligazione_scadenzario%rowtype;
   aObbScadVoce obbligazione_scad_voce%rowtype;
   aObbNew obbligazione%rowtype;
   aObbScadNew obbligazione_scadenzario%rowtype;
   aObbScadVoceNew obbligazione_scad_voce%rowtype;
   aCdTerzoRes number(8);
   aCdModPagRes varchar2(10);
   aPgBancaRes number(10);
   aCdTerzoVE number(8);
   aCdModPagVE varchar2(10);
   aPgBancaVE number(10);
   aManP mandato%rowtype;
   aManPRiga mandato_riga%rowtype;
   aListRigheManP CNRCTB038.righeMandatoList;
   aAnagTst anagrafico%rowtype;
   aAnagVer anagrafico%rowtype;
   aRevPRiga reversale_riga%rowtype;
   aDateCont date;
   aDivisaEuro varchar2(50);
   aUOVERSCONTOBI unita_organizzativa%rowtype;
   aCdEV varchar2(20);
   elementoVoce elemento_voce%rowtype;
 begin
  aDateCont:=CNRCTB008.getTimestampContabile(aLGC.esercizio,aTSNow);
  aDivisaEuro:=CNRCTB015.GETVAL01PERCHIAVE(CNRCTB100.CCNR_DIVISA,CNRCTB100.CCNR_EURO);
  
  aUOVERSCONTOBI:=CNRCTB020.getUOVersCoriContoBI(aLGC.esercizio);

  aGenVE:=null;
  -- Creo il documento generico di entrata su partita di giro collegato all'annotazione di spesa su pgiro del contributo ritenuta
  aGenVE.CD_CDS:=Cds;
  aGenVE.CD_UNITA_ORGANIZZATIVA:=Uo;
  aGenVE.ESERCIZIO:=aLGC.esercizio;
  aGenVE.CD_CDS_ORIGINE:=Cds;
  aGenVE.CD_UO_ORIGINE:=Uo;
  aGenVE.CD_TIPO_DOCUMENTO_AMM:=CNRCTB100.TI_GEN_CORI_VER_ENTRATA;
  aGenVE.DATA_REGISTRAZIONE:=TRUNC(aDateCont);
  aGenVE.DS_DOCUMENTO_GENERICO:='CORI-COMPENSAZIONE gruppo cr:'||aLGC.cd_gruppo_cr||'.'||aLGC.cd_regione||'.'||aLGC.pg_comune;
  aGenVE.TI_ISTITUZ_COMMERC:=CNRCTB100.TI_ISTITUZIONALE;
--  aGenVE.IM_TOTALE:=;
  aGenVE.STATO_COFI:=CNRCTB100.STATO_GEN_COFI_TOT_MR;
  aGenVE.STATO_COGE:=CNRCTB100.STATO_COEP_EXC;
  aGenVE.STATO_COAN:=CNRCTB100.STATO_COEP_EXC;
  aGenVE.CD_DIVISA:=aDivisaEuro;
  aGenVE.CAMBIO:=1;
  aGenVE.DACR:=aTSNow;
  aGenVE.UTCR:=aUser;
  aGenVE.DUVA:=aTSNow;
  aGenVE.UTUV:=aUser;
  aGenVE.PG_VER_REC:=1;
  aGenVE.DT_SCADENZA:=TRUNC(aTSNow);
  aGenVE.STATO_PAGAMENTO_FONDO_ECO:=CNRCTB100.STATO_NO_PFONDOECO;
  aGenVE.TI_ASSOCIATO_MANREV:=CNRCTB100.TI_ASSOC_TOT_MAN_REV ;
  aGenVE.DT_DA_COMPETENZA_COGE:=TRUNC(aDateCont);
  aGenVE.DT_A_COMPETENZA_COGE:=TRUNC(aDateCont);

  aAnagVer:=CNRCTB080.GETANAG(terzo_versamento);

  for aLGCC in (select * from liquid_gruppo_centro_comp where
                        ESERCIZIO=aLGC.esercizio
                    and CD_GRUPPO_CR=aLGC.cd_gruppo_cr
                    and CD_REGIONE=aLGC.cd_regione
                    and PG_COMUNE=aLGC.pg_comune
                    and PG_GRUPPO_CENTRO=aLGC.pg_gruppo_centro
				for update nowait
  ) loop
         -- Estraggo il terzo UO relativo all'UO SAC che accentra i versamenti
         CNRCTB080.GETTERZOPERUO(aLGCC.cd_unita_organizzativa,aCdTerzoRes, aCdModPagRes, aPgBancaRes,aLGC.esercizio);
         aAnagTst:=CNRCTB080.GETANAG(aCdTerzoRes);

 	       aGen:=null;
         aGenRiga:=null;

         aAcc.cd_cds:=aLGCC.cd_cds_acc_accentr;
         aAcc.esercizio:=aLGCC.esercizio_acc_accentr;
         aAcc.esercizio_originale:=aLGCC.esercizio_ori_acc_accentr;
         aAcc.pg_accertamento:=aLGCC.pg_acc_accentr;
         CNRCTB035.GETPGIROCDSINV(aAcc,aAccScad,aAccScadVoce,aObb,aObbScad,aObbScadVoce);

				 --aAcc si chiude con la reversale di versamento
				 --aObb si chiude con il mandato di restituzione
				 --Se la UO ? 999.000, solo la reversale di versamento deve uscire dalla 999, mentre il mandato di restituzione deve uscire dalla SAC
				 --quindi devo annullare aAcc sulla SAC (cio? rendere tronca aObb) e ricrearla tronca sulla 999 
				 if Uo = aUOVERSCONTOBI.cd_unita_organizzativa then
							 CNRCTB043.troncaPraticaObbPgiro(aObb.esercizio,aObb.cd_cds,aObb.esercizio_originale,aObb.pg_obbligazione,aTSNow,aUser); 
               -- devo creare la pgiro tronca sulla 999
                  aAccNew:=null;
                  aAccScadNew:=null;
                  aObbNew:=null;
                  aObbScadNew:=null;
                  --determino il capitolo da mettere sulla pgiro
                  Begin
                     Select distinct a.cd_elemento_voce
                     Into aCdEV
                     From ass_tipo_cori_ev a, tipo_cr_base b
                     Where b.esercizio = aLGCC.esercizio
                       And b.cd_gruppo_cr = aLGCC.cd_gruppo_cr
                       And a.cd_contributo_ritenuta = b.cd_contributo_ritenuta
                       And a.esercizio = aLGCC.esercizio
                       And a.ti_gestione = CNRCTB001.GESTIONE_ENTRATE
                       And a.ti_appartenenza = CNRCTB001.APPARTENENZA_CNR;
                  Exception 
                          when TOO_MANY_ROWS then
                          	   IBMERR001.RAISE_ERR_GENERICO('Esiste pi? di un conto finanziario associato a CORI del gruppo '||aLGCC.cd_gruppo_cr);
                          when NO_DATA_FOUND then
                              IBMERR001.RAISE_ERR_GENERICO('Conto finanziario associato a CORI del gruppo '||aLGCC.cd_gruppo_cr||' non trovato');
                  End;
                  Begin
                     Select *
                     Into elementoVoce
                     From elemento_voce
                     Where esercizio = aLGCC.esercizio
                       And ti_appartenenza = CNRCTB001.APPARTENENZA_CNR
                       And ti_gestione = CNRCTB001.GESTIONE_ENTRATE
                       And cd_elemento_voce = aCdEV;
                  Exception 
                          when NO_DATA_FOUND then
                         	   IBMERR001.RAISE_ERR_GENERICO('Codice voce '||aCdEV||' associata a CORI del gruppo '||aLGCC.cd_gruppo_cr||' non esistente');
                  End;
                  
                  aAccNew.CD_CDS:=Cds;
                  aAccNew.ESERCIZIO:=aAcc.esercizio;
                  aAccNew.ESERCIZIO_ORIGINALE:=aAcc.esercizio_originale;
                  If aAcc.esercizio = aAcc.esercizio_originale Then
                       aAccNew.CD_TIPO_DOCUMENTO_CONT:=CNRCTB018.TI_DOC_ACC;
                  Else     
                       aAccNew.CD_TIPO_DOCUMENTO_CONT:=CNRCTB018.TI_DOC_ACC_RES;
                  End If;
                  aAccNew.CD_UNITA_ORGANIZZATIVA:=Uo;
                  aAccNew.CD_CDS_ORIGINE:=Cds;
                  aAccNew.CD_UO_ORIGINE:=Uo;
                  aAccNew.TI_APPARTENENZA:=elementoVoce.ti_appartenenza;
                  aAccNew.TI_GESTIONE:=elementoVoce.ti_gestione;
                  aAccNew.CD_ELEMENTO_VOCE:=elementoVoce.cd_elemento_voce;
                  aAccNew.CD_VOCE:=elementoVoce.cd_elemento_voce;
                  aAccNew.DT_REGISTRAZIONE:=TRUNC(trunc(aTSNow));
                  aAccNew.DS_ACCERTAMENTO:='PGiro creata in automatico da liquidazione CORI';
                  aAccNew.NOTE_ACCERTAMENTO:='';
                  aAccNew.CD_TERZO:=aAcc.CD_TERZO;
                  aAccNew.IM_ACCERTAMENTO:=aObb.IM_OBBLIGAZIONE;    --aAcc.IM_ACCERTAMENTO ? stato annullato    
                  aAccNew.FL_PGIRO:='Y';
                  aAccNew.RIPORTATO:='N';
                  aAccNew.DACR:=aTSNow;
                  aAccNew.UTCR:=aUser;
                  aAccNew.DUVA:=aTSNow;
                  aAccNew.UTUV:=aUser;
                  aAccNew.PG_VER_REC:=1;
                  aAccNew.ESERCIZIO_COMPETENZA:=aAcc.esercizio;
                
                  CNRCTB040.CREAACCERTAMENTOPGIROTRONC(false,aAccNew,aAccScadNew,aObbNew,aObbScadNew,trunc(aTSNow));
 
                  CREALIQUIDCORIASSPGIRO(aLiquid,aLGCC.cd_gruppo_cr,aLGCC.cd_regione,aLGCC.pg_comune,'E',null,null,aAccNew,aAcc,aUser,trunc(aTSNow));
                  
                  aAcc     := aAccNew;
		              aAccScad := aAccScadNew;
         End If;

         -- Creo il documento generico di spesa su partita di giro collegato all'annotazione di entrata su pgiro
 		     -- per versamento accentrato
         aGen.CD_CDS:=aObb.cd_cds;       --Cds;
         aGen.CD_UNITA_ORGANIZZATIVA:=aObb.cd_unita_organizzativa;     --Uo;
         aGen.ESERCIZIO:=aLGC.esercizio;
         aGen.CD_CDS_ORIGINE:=aObb.cd_cds;     --Cds;
         aGen.CD_UO_ORIGINE:=aObb.cd_unita_organizzativa;    --Uo;
         aGen.CD_TIPO_DOCUMENTO_AMM:=CNRCTB100.TI_GEN_CORI_VER_SPESA;
         aGen.DATA_REGISTRAZIONE:=TRUNC(aDateCont);
         aGen.DS_DOCUMENTO_GENERICO:='CORI-RESTITUZIONE A UO:'||aLGCC.cd_unita_organizzativa||' gruppo cr:'||aLGC.cd_gruppo_cr||'.'||aLGC.cd_regione||'.'||aLGC.pg_comune;
         aGen.TI_ISTITUZ_COMMERC:=CNRCTB100.TI_ISTITUZIONALE;
         aGen.IM_TOTALE:=aObb.im_obbligazione;
         aGen.STATO_COFI:=CNRCTB100.STATO_GEN_COFI_TOT_MR;
         aGen.STATO_COGE:=CNRCTB100.STATO_COEP_EXC;
         aGen.STATO_COAN:=CNRCTB100.STATO_COEP_EXC;
         aGen.CD_DIVISA:=aDivisaEuro;
         aGen.CAMBIO:=1;
         --  aGen.ESERCIZIO_LETTERA:=0;
         --  aGen.PG_LETTERA:=0;
         aGen.DACR:=aTSNow;
         aGen.UTCR:=aUser;
         aGen.DUVA:=aTSNow;
         aGen.UTUV:=aUser;
         aGen.PG_VER_REC:=1;
         aGen.DT_SCADENZA:=TRUNC(aTSNow);
         aGen.STATO_PAGAMENTO_FONDO_ECO:=CNRCTB100.STATO_NO_PFONDOECO;
         aGen.TI_ASSOCIATO_MANREV:=CNRCTB100.TI_ASSOC_TOT_MAN_REV ;
         aGen.DT_DA_COMPETENZA_COGE:=TRUNC(aDateCont);
         aGen.DT_A_COMPETENZA_COGE:=TRUNC(aDateCont);

         aGenRiga.DT_DA_COMPETENZA_COGE:=aGen.DT_DA_COMPETENZA_COGE;
         aGenRiga.DT_A_COMPETENZA_COGE:=aGen.DT_A_COMPETENZA_COGE;
         aGenRiga.CD_CDS:=aGen.CD_CDS;
         aGenRiga.CD_UNITA_ORGANIZZATIVA:=aGen.CD_UNITA_ORGANIZZATIVA;
         aGenRiga.ESERCIZIO:=aGen.ESERCIZIO;
         aGenRiga.CD_TIPO_DOCUMENTO_AMM:=aGen.CD_TIPO_DOCUMENTO_AMM;
         aGenRiga.DS_RIGA:=aGen.DS_DOCUMENTO_GENERICO;
         aGenRiga.IM_RIGA_DIVISA:=aGen.IM_TOTALE;
         aGenRiga.IM_RIGA:=aGen.IM_TOTALE;
         aGenRiga.CD_TERZO:=aCdTerzoRes;
         aGenRiga.CD_MODALITA_PAG:=aCdModPagRes;
         aGenRiga.PG_BANCA:=aPgBancaRes;
         aGenRiga.RAGIONE_SOCIALE:=aAnagTst.RAGIONE_SOCIALE;
         aGenRiga.NOME:=aAnagTst.NOME;
         aGenRiga.COGNOME:=aAnagTst.COGNOME;
         aGenRiga.CODICE_FISCALE:=aAnagTst.CODICE_FISCALE;
         aGenRiga.PARTITA_IVA:=aAnagTst.PARTITA_IVA;
         --   aGenRiga.CD_TERMINI_PAG:=aCompenso.CD_TERMINI_PAG;
         --   aGenRiga.CD_TERMINI_PAG_UO_CDS:=aCompenso.CD_TERMINI_PAG_UO_CDS;
         --   aGenRiga.PG_BANCA_UO_CDS:=aGen.PG_BANCA_UO_CDS;
         --   aGenRiga.CD_MODALITA_PAG_UO_CDS:=aGen.CD_MODALITA_PAG_UO_CDS;
         --   aGenRiga.NOTE:=aGen.NOTE;
         aGenRiga.STATO_COFI:=aGen.STATO_COFI;
         --   aGenRiga.DT_CANCELLAZIONE:=aGen.DT_CANCELLAZIONE;
         --   aGenRiga.CD_CDS_OBBLIGAZIONE:=aGen.CD_CDS_OBBLIGAZIONE;
         --   aGenRiga.ESERCIZIO_OBBLIGAZIONE:=aGen.ESERCIZIO_OBBLIGAZIONE;
         --   aGenRiga.ESERCIZIO_ORI_OBBLIGAZIONE:=aGen.ESERCIZIO_ORI_OBBLIGAZIONE;
         --   aGenRiga.PG_OBBLIGAZIONE:=aGen.PG_OBBLIGAZIONE;
         --   aGenRiga.PG_OBBLIGAZIONE_SCADENZARIO:=aGen.PG_OBBLIGAZIONE_SCADENZARIO;
         aGenRiga.CD_CDS_OBBLIGAZIONE:=aObb.cd_cds;
         aGenRiga.ESERCIZIO_OBBLIGAZIONE:=aObb.esercizio;
         aGenRiga.ESERCIZIO_ORI_OBBLIGAZIONE:=aObb.esercizio_originale;
         aGenRiga.PG_OBBLIGAZIONE:=aObb.pg_obbligazione;
         aGenRiga.PG_OBBLIGAZIONE_SCADENZARIO:=1;
         aGenRiga.DACR:=aGen.DACR;
         aGenRiga.UTCR:=aGen.UTCR;
         aGenRiga.UTUV:=aGen.UTUV;
         aGenRiga.DUVA:=aGen.DUVA;
         aGenRiga.PG_VER_REC:=aGen.PG_VER_REC;
         aGenRiga.TI_ASSOCIATO_MANREV:=aGen.TI_ASSOCIATO_MANREV;
         --
 	     aListGenRighe(1):=aGenRiga;
         CNRCTB110.CREAGENERICOAGGOBBACC(aGen,aListGenRighe);

		 aListRigheManP.delete;
		 aManP:=null;
	     aManP.CD_CDS:=aGen.cd_cds;
	     aManP.ESERCIZIO:=aGen.esercizio;
	     aManP.CD_UNITA_ORGANIZZATIVA:=aGen.cd_unita_organizzativa;
	     aManP.CD_CDS_ORIGINE:=aGen.cd_cds;
	     aManP.CD_UO_ORIGINE:=aGen.cd_unita_organizzativa;
	     aManP.CD_TIPO_DOCUMENTO_CONT:=CNRCTB018.TI_DOC_MAN;
         aManP.TI_MANDATO:=CNRCTB038.TI_MAN_PAG;
         aManP.TI_COMPETENZA_RESIDUO:=CNRCTB038.TI_MAN_COMP;
         aManP.DS_MANDATO:='Mandato di resituzione crediti versamento gruppo CORI: '||aLGC.cd_gruppo_cr||'.'||aLGC.cd_regione||'.'||aLGC.pg_comune;
         aManP.STATO:=CNRCTB038.STATO_MAN_EME;
         aManP.DT_EMISSIONE:=TRUNC(aDateCont);
         aManP.IM_RITENUTE:=0;
         aManP.IM_MANDATO:=aGen.IM_TOTALE;
         --  aManP.DT_TRASMISSIONE:=;
         --  aManP.DT_PAGAMENTO:=;
         --  aManP.DT_ANNULLAMENTO:=;
         aManP.IM_PAGATO:=0;
         aManP.UTCR:=aUser;
         aManP.DACR:=aTSNow;
         aManP.UTUV:=aUser;
         aManP.DUVA:=aTSNow;
         aManP.PG_VER_REC:=1;
         aManP.STATO_TRASMISSIONE:=CNRCTB038.STATO_MAN_TRASCAS_NODIST;
         -- Generazione righe mandato
         aManPRiga:=null;
         aManPRiga.CD_CDS:=aGen.cd_cds;
         aManPRiga.ESERCIZIO:=aGen.esercizio;
         aManPRiga.ESERCIZIO_OBBLIGAZIONE:=aGenRiga.esercizio_obbligazione;
         aManPRiga.ESERCIZIO_ORI_OBBLIGAZIONE:=aGenRiga.esercizio_ori_obbligazione;
         aManPRiga.PG_OBBLIGAZIONE:=aGenRiga.pg_obbligazione;
         aManPRiga.PG_OBBLIGAZIONE_SCADENZARIO:=aGenRiga.pg_obbligazione_scadenzario;
         aManPRiga.CD_CDS_DOC_AMM:=aGen.cd_cds;
         aManPRiga.CD_UO_DOC_AMM:=aGen.cd_unita_organizzativa;
         aManPRiga.ESERCIZIO_DOC_AMM:=aGen.esercizio;
         aManPRiga.CD_TIPO_DOCUMENTO_AMM:=aGen.cd_tipo_documento_amm;
         aManPRiga.PG_DOC_AMM:=aGen.pg_documento_generico;
         aManPRiga.DS_MANDATO_RIGA:=aManP.ds_mandato;
         aManPRiga.STATO:=aManP.stato;
         aManPRiga.CD_TERZO:=aGenRiga.CD_TERZO;
         aManPRiga.PG_BANCA:=aGenRiga.pg_banca;
         aManPRiga.CD_MODALITA_PAG:=aGenRiga.cd_modalita_pag;
         aManPRiga.IM_MANDATO_RIGA:=aGen.IM_TOTALE;
         aManPRiga.IM_RITENUTE_RIGA:=0;
         aManPRiga.FL_PGIRO:='Y';
         aManPRiga.UTCR:=aUser;
         aManPRiga.DACR:=aTSNow;
         aManPRiga.UTUV:=aUser;
         aManPRiga.DUVA:=aTSNow;
         aManPRiga.PG_VER_REC:=1;
         aListRigheManP(aListRigheManP.count+1):=aManPRiga;
         CNRCTB037.GENERADOCUMENTO(aManP,aListRigheManP);

         -- Crea righe di generico e di reversale per compensazione liquidazione al centro

         aGenVERiga:=null;

         aGenVERiga.DT_DA_COMPETENZA_COGE:=aGenVE.DT_DA_COMPETENZA_COGE;
         aGenVERiga.DT_A_COMPETENZA_COGE:=aGenVE.DT_A_COMPETENZA_COGE;
         aGenVERiga.CD_CDS:=aGenVE.CD_CDS;
         aGenVERiga.CD_UNITA_ORGANIZZATIVA:=aGenVE.CD_UNITA_ORGANIZZATIVA;
         aGenVERiga.ESERCIZIO:=aGenVE.ESERCIZIO;
         aGenVERiga.CD_TIPO_DOCUMENTO_AMM:=aGenVE.CD_TIPO_DOCUMENTO_AMM;
         aGenVERiga.DS_RIGA:=aGenVE.DS_DOCUMENTO_GENERICO;
         aGenVERiga.IM_RIGA_DIVISA:=aAcc.im_accertamento;
         aGenVERiga.IM_RIGA:=aAcc.im_accertamento;
         -- Utilizzo delle corrette informazioni di pagamento nel caso di accentramento o non accentramento
         aGenVERiga.CD_TERZO:=terzo_versamento;
         --   aGenVERiga.CD_TERZO_CESSIONARIO:=aGenVE.CD_TERZO_CESSIONARIO;
         
         If Uo = aUOVERSCONTOBI.cd_unita_organizzativa then
	            CNRCTB080.getTerzoPerEnteContoBI(Uo, aCdTerzoVE, aCdModPagVE, aPgBancaVE);
	       Else
    	        aCdTerzoVE := aCdTerzoRes;
    	        aPgBancaVE := aPgBancaRes;
    	        aCdModPagVE := aCdModPagRes;
    	   End If;
    	      
         aGenVERiga.CD_TERZO_UO_CDS:=aCdTerzoVE;
         aGenVERiga.PG_BANCA_UO_CDS:=aPgBancaVE;
         aGenVERiga.CD_MODALITA_PAG_UO_CDS:=aCdModPagVE;

         aGenVERiga.RAGIONE_SOCIALE:=aAnagVer.RAGIONE_SOCIALE;
         aGenVERiga.NOME:=aAnagVer.NOME;
         aGenVERiga.COGNOME:=aAnagVer.COGNOME;
         aGenVERiga.CODICE_FISCALE:=aAnagVer.CODICE_FISCALE;
         aGenVERiga.PARTITA_IVA:=aAnagVer.PARTITA_IVA;
         --   aGenVERiga.CD_TERMINI_PAG:=aCompenso.CD_TERMINI_PAG;
         --   aGenVERiga.CD_TERMINI_PAG_UO_CDS:=aCompenso.CD_TERMINI_PAG_UO_CDS;
         --   aGenVERiga.NOTE:=aGenVE.NOTE;
         aGenVERiga.STATO_COFI:=aGenVE.STATO_COFI;
         --   aGenVERiga.DT_CANCELLAZIONE:=aGenVE.DT_CANCELLAZIONE;
         --   aGenVERiga.CD_CDS_OBBLIGAZIONE:=aGenVE.CD_CDS_OBBLIGAZIONE;
         --   aGenVERiga.ESERCIZIO_OBBLIGAZIONE:=aGenVE.ESERCIZIO_OBBLIGAZIONE;
         --   aGenVERiga.ESERCIZIO_ORI_OBBLIGAZIONE:=aGenVE.ESERCIZIO_ORI_OBBLIGAZIONE;
         --   aGenVERiga.PG_OBBLIGAZIONE:=aGenVE.PG_OBBLIGAZIONE;
         --   aGenVERiga.PG_OBBLIGAZIONE_SCADENZARIO:=aGenVE.PG_OBBLIGAZIONE_SCADENZARIO;
         aGenVERiga.CD_CDS_ACCERTAMENTO:=aAcc.CD_CDS;
         aGenVERiga.ESERCIZIO_ACCERTAMENTO:=aAcc.ESERCIZIO;
         aGenVERiga.ESERCIZIO_ORI_ACCERTAMENTO:=aAcc.ESERCIZIO_ORIGINALE;
         aGenVERiga.PG_ACCERTAMENTO:=aAcc.PG_ACCERTAMENTO;
         aGenVERiga.PG_ACCERTAMENTO_SCADENZARIO:=1;
         aGenVERiga.DACR:=aGenVE.DACR;
         aGenVERiga.UTCR:=aGenVE.UTCR;
         aGenVERiga.UTUV:=aGenVE.UTUV;
         aGenVERiga.DUVA:=aGenVE.DUVA;
         aGenVERiga.PG_VER_REC:=aGenVE.PG_VER_REC;
         aGenVERiga.TI_ASSOCIATO_MANREV:=aGenVE.TI_ASSOCIATO_MANREV;
         aListGenVERighe(aListGenVERighe.count+1):=aGenVERiga;
         -- Generazione righe reversale
         aRevPRiga:=null;
         aRevPRiga.CD_CDS:=aGenVE.cd_cds;
         aRevPRiga.ESERCIZIO:=aGenVE.esercizio;
         aRevPRiga.ESERCIZIO_ACCERTAMENTO:=aGenVERiga.esercizio_ACCERTAMENTO;
         aRevPRiga.ESERCIZIO_ORI_ACCERTAMENTO:=aGenVERiga.esercizio_ORI_ACCERTAMENTO;
         aRevPRiga.PG_ACCERTAMENTO:=aGenVERiga.pg_accertamento;
         aRevPRiga.PG_ACCERTAMENTO_SCADENZARIO:=aGenVERiga.pg_accertamento_scadenzario;
         aRevPRiga.CD_CDS_DOC_AMM:=aGenVE.cd_cds;
         aRevPRiga.CD_UO_DOC_AMM:=aGenVE.cd_unita_organizzativa;
         aRevPRiga.ESERCIZIO_DOC_AMM:=aGenVE.esercizio;
         aRevPRiga.CD_TIPO_DOCUMENTO_AMM:=aGenVE.cd_tipo_documento_amm;
         aRevPRiga.PG_DOC_AMM:=-1000;
         aRevPRiga.DS_REVERSALE_RIGA:=aRevP.ds_reversale;
         aRevPRiga.STATO:=aRevP.stato;
         aRevPRiga.CD_TERZO:=aGenVERiga.cd_terzo;
         aRevPRiga.CD_TERZO_UO:=aGenVERiga.cd_terzo_uo_cds;
         aRevPRiga.PG_BANCA:=aGenVERiga.pg_banca_uo_cds;
         aRevPRiga.CD_MODALITA_PAG:=aGenVERiga.cd_modalita_pag_uo_cds;
         aRevPRiga.IM_REVERSALE_RIGA:=aAcc.im_accertamento;
         aRevPRiga.FL_PGIRO:='Y';
         aRevPRiga.UTCR:=aUser;
         aRevPRiga.DACR:=aTSNow;
         aRevPRiga.UTUV:=aUser;
         aRevPRiga.DUVA:=aTSNow;
         aRevPRiga.PG_VER_REC:=1;
         aTotReversale:=aTotReversale+aRevPRiga.im_reversale_riga;
         aListRigheRevP(aListRigheRevP.count+1):=aRevPRiga;
   end loop;
   if aListGenRighe.count > 0 then
    CNRCTB110.CREAGENERICOAGGOBBACC(aGenVE,aListGenVERighe);
   end if;
   for i in 1..aListRigheRevP.count loop
    if aListRigheRevP(i).pg_doc_amm=-1000 then
	 aListRigheRevP(i).pg_doc_amm:=aGenVE.pg_documento_generico;
	end if;
   end loop;
 end;

 function creaGruppoCentro(aL liquid_cori%rowtype, aAggregato liquid_gruppo_cori%rowtype,aTSNow date, aUser varchar2) return liquid_gruppo_centro%rowtype is
  aLGC liquid_gruppo_centro%rowtype;
  aPgGruppoCentro number(10);
 begin
  begin
   select pg_gruppo_centro+1 into aPgGruppoCentro from liquid_gruppo_centro where
         ESERCIZIO=aAggregato.esercizio
     AND CD_GRUPPO_CR=aAggregato.cd_gruppo_cr
     AND CD_REGIONE=aAggregato.cd_regione
     AND PG_COMUNE=aAggregato.pg_comune
     AND pg_gruppo_centro=(select max(pg_gruppo_centro) from liquid_gruppo_centro
       where
            ESERCIZIO=aAggregato.esercizio
        AND CD_GRUPPO_CR=aAggregato.cd_gruppo_cr
        AND CD_REGIONE=aAggregato.cd_regione
        AND PG_COMUNE=aAggregato.pg_comune
	 )
	 for update nowait;
  exception when NO_DATA_FOUND then
   aPgGruppoCentro:=1;
  end;
  aLGC.ESERCIZIO:=aAggregato.esercizio;
  aLGC.CD_GRUPPO_CR:=aAggregato.cd_gruppo_cr;
  aLGC.CD_REGIONE:=aAggregato.cd_regione;
  aLGC.PG_COMUNE:=aAggregato.pg_comune;
  aLGC.PG_GRUPPO_CENTRO:=aPgGruppoCentro;
  aLGC.STATO:=CNRCTB575.STATO_GRUPPO_CENTRO_INIZIALE;
  aLGC.CD_CDS_LC:=null;
  aLGC.CD_UO_LC:=null;
  aLGC.PG_LC:=null;
  aLGC.DACR:=aTSNow;
  aLGC.UTCR:=aUser;
  aLGC.DUVA:=aTSNow;
  aLGC.UTUV:=aUser;
  aLGC.PG_VER_REC:=1;
  aLGC.DA_ESERCIZIO_PRECEDENTE:=aL.da_esercizio_precedente;
  begin
   CNRCTB575.INS_LIQUID_GRUPPO_CENTRO(aLGC);
  exception when DUP_VAL_ON_INDEX then
   -- L'inserimento pu? dare un errore di chiave duplicata se due sessioni tentano di inserire
   -- il primo record per lo stesso gruppo di versamento: la prima inserisce, la seconda deve essere bloccata
   -- e restituire l'errore di risorsa occupata
   IBMERR001.RAISE_ERR_GENERICO('Risorsa occupata riprovare pi? tardi');
  end;
  return aLGC;
 end;

 procedure aggiornaPraticaGruppoCentro(aL liquid_cori%rowtype, aAggregato liquid_gruppo_cori%rowtype,aUOVERSACC unita_organizzativa%rowtype,aTSNow date,aUser varchar2, tb_ass_pgiro IN OUT tab_ass_pgiro) is
  aAcc accertamento%rowtype;
  aAccScad accertamento_scadenzario%rowtype;
  aObb obbligazione%rowtype;
  aObbScad obbligazione_scadenzario%rowtype;
  aObbNew obbligazione%rowtype;
  aObbScadNew obbligazione_scadenzario%rowtype;
  aAccNew accertamento%rowtype;
  aAccScadNew accertamento_scadenzario%rowtype;
  aObbPG obbligazione%rowtype;
  aObbGiro obbligazione%rowtype;
	aObbScadGiro	obbligazione_scadenzario%rowtype;
	aObbScadVoceGiro	obbligazione_scad_voce%rowtype;
	aObbScadVoce	obbligazione_scad_voce%rowtype;
	aAccScadVoce	accertamento_scad_voce%rowtype;
  aObbPGScad obbligazione_scadenzario%rowtype;
  aAccGiro accertamento%rowtype;
  aAccScadGiro accertamento_scadenzario%rowtype;
	aAccScadVoceGiro	accertamento_scad_voce%rowtype;
  aAccPG accertamento%rowtype;
  aAccPGScad accertamento_scadenzario%rowtype;
  aCdEV varchar2(20);
  elementoVoce elemento_voce%rowtype;
  aLGC liquid_gruppo_centro%rowtype;
  aLGCC liquid_gruppo_centro_comp%rowtype;
  aCdTerzoAcc number(8);
  aCdModPagAcc varchar2(10);
  aPgBancaAcc number(10);
  aCdTerzoUo number(8);
  aCdModPagUo varchar2(10);
  aPgBancaUo number(10);
  aGen documento_generico%rowtype;
  aGenRiga documento_generico_riga%rowtype;
  aListGenRighe CNRCTB100.docGenRigaList;
  aRev reversale%rowtype;
  aRevRiga reversale_riga%rowtype;
  aAnagTst anagrafico%rowtype;
  aContoColl ass_partita_giro%rowtype;
  aDateCont date;
  aAccConScad varchar2(1):='Y';
  recParametriCNR PARAMETRI_CNR%Rowtype;
	TROVATO_DETTAGLIO_P_GIRO VARCHAR2(1);
Begin
  if aAggregato.im_liquidato = 0 then
   -- ATTENZIONE!!! Se l'importo da liquidare ? 0 non c'? niente da aggiornare al centro
   return;
  end if;

  recParametriCNR := CNRUTL001.getRecParametriCnr(aL.esercizio);
  aDateCont:=CNRCTB008.getTimestampContabile(aL.esercizio,aTSNow);

  aLGC:=null;

--pipe.send_message('1 '|| 	aL.esercizio); 	         
  begin
   aLGC:=getGruppoCentroAperto(aL, aAggregato);
  exception when NO_DATA_FOUND then
   aLGC:=creaGruppoCentro(aL, aAggregato,aTSNow,aUser);
  end;

  -- Estraggo il terzo UO relativo all'UO SAC che accentra i versamenti
  CNRCTB080.GETTERZOPERUO(aUOVERSACC.cd_unita_organizzativa,aCdTerzoAcc, aCdModPagAcc, aPgBancaAcc,aL.esercizio);

--GGG1 DA CAMBIARE
  if aAggregato.im_liquidato < 0 Then
  --and recParametriCNR.FL_TESORERIA_UNICA != 'Y' 
    aLGCC:=creaGruppoCentroComp(aL, aLGC, aAggregato,aTSNow,aUser);
    IF recParametriCNR.FL_TESORERIA_UNICA = 'Y' THEN
      aObbPG:=null;
      aObbPGScad:=null;
      begin
       select distinct a.cd_elemento_voce
       into aCdEV
       from ass_tipo_cori_ev a, tipo_cr_base b
       where b.esercizio = aAggregato.esercizio
         and b.cd_gruppo_cr = aAggregato.cd_gruppo_cr
         and a.cd_contributo_ritenuta = b.cd_contributo_ritenuta
         and a.esercizio = aAggregato.esercizio
         and a.ti_gestione = CNRCTB001.GESTIONE_ENTRATE
         and a.ti_appartenenza = CNRCTB001.APPARTENENZA_CNR;
      exception when TOO_MANY_ROWS then
         IBMERR001.RAISE_ERR_GENERICO('Esiste pi? di un conto finanziario associato a CORI del gruppo '||aAggregato.cd_gruppo_cr);
       		   when NO_DATA_FOUND then
        		   IBMERR001.RAISE_ERR_GENERICO('Conto finanziario associato a CORI del gruppo '||aAggregato.cd_gruppo_cr||' non trovato');
      end;
      begin
       	select 	*
      	into 		elementoVoce
      	from 		elemento_Voce
      	where esercizio = aAggregato.esercizio
          and ti_appartenenza = CNRCTB001.APPARTENENZA_CNR
          and ti_gestione = CNRCTB001.GESTIONE_ENTRATE
         	and cd_elemento_voce = aCdEV;
      exception when NO_DATA_FOUND then
       	IBMERR001.RAISE_ERR_GENERICO('Codice voce '||aCdEV||' associata a CORI del gruppo '||aAggregato.cd_gruppo_cr||' non esistente');
      end;

    	aAcc:=null;
    	aAccScad:=null;
    	aAcc.CD_CDS:=aUOVERSACC.cd_unita_padre;
    	aAcc.ESERCIZIO:=aAggregato.esercizio;
    	aAcc.CD_TIPO_DOCUMENTO_CONT:=CNRCTB018.TI_DOC_ACC_PGIRO;
    	aAcc.CD_UNITA_ORGANIZZATIVA:=aUOVERSACC.cd_unita_organizzativa;
    	aAcc.CD_CDS_ORIGINE:=aUOVERSACC.cd_unita_padre;
    	aAcc.CD_UO_ORIGINE:=aUOVERSACC.cd_unita_organizzativa;
    	aAcc.TI_APPARTENENZA:=elementoVoce.TI_APPARTENENZA;
    	aAcc.TI_GESTIONE:=elementoVoce.TI_GESTIONE;
    	-- Utilizzo come conto il primo conto che trovo di un CORI appartenente al gruppo
    	aAcc.CD_ELEMENTO_VOCE:=aCdEV;
    	aAcc.CD_VOCE:=elementoVoce.cd_elemento_voce;
    	aAcc.DT_REGISTRAZIONE:=TRUNC(aDateCont);
    	aAcc.DS_ACCERTAMENTO:='CORI-VA compensazione gruppo cr:'||aAggregato.cd_gruppo_cr||'.'||aAggregato.cd_regione||'.'||aAggregato.pg_comune;
    	aAcc.NOTE_ACCERTAMENTO:='';
    	aAcc.CD_TERZO:=aCdTerzoAcc;
    	aAcc.FL_PGIRO:='Y';
    	aAcc.RIPORTATO:='N';
    	aAcc.DACR:=aTSNow;
    	aAcc.UTCR:=aUser;
    	aAcc.DUVA:=aTSNow;
    	aAcc.UTUV:=aUser;
    	aAcc.PG_VER_REC:=1;
    	aAcc.ESERCIZIO_COMPETENZA:=aAggregato.esercizio;
    	aAcc.IM_ACCERTAMENTO:=abs(aAggregato.im_liquidato);
    	CNRCTB040.CREAACCERTAMENTOPGIROTRONC(false,aAcc,aAccScad,aObbPG,aObbPGScad,trunc(aTSNow));
      aAccPG:=aAcc;
    	aAccPGScad:=aAccScad;
    	TROVATO_DETTAGLIO_P_GIRO := 'N';
begin
    	For i in 1..Nvl(tb_ass_pgiro.Count,0) Loop
				if 	aAggregato.cd_cds = tb_ass_pgiro(i).cd_cds and
						aAggregato.esercizio = tb_ass_pgiro(i).esercizio and
						aAggregato.cd_unita_organizzativa = tb_ass_pgiro(i).cd_uo and
						aAggregato.pg_liquidazione = tb_ass_pgiro(i).pg_liq and
						aAggregato.cd_cds_origine = tb_ass_pgiro(i).cd_cds_orig and
						aAggregato.cd_uo_origine = tb_ass_pgiro(i).cd_uo_orig and
						aAggregato.pg_liquidazione_origine = tb_ass_pgiro(i).pg_liq_orig and
						aAggregato.cd_gruppo_cr = tb_ass_pgiro(i).cd_gr_cr and
						aAggregato.cd_regione = tb_ass_pgiro(i).cd_regione and
						aAggregato.pg_comune = tb_ass_pgiro(i).pg_comune then
			
						TROVATO_DETTAGLIO_P_GIRO := 'S';
    	
  					tb_ass_pgiro(i).cd_cds_acc_pgiro 	:= aAccPG.cd_cds;
    	      tb_ass_pgiro(i).es_acc_pgiro   		:= aAccPG.esercizio;
    	      tb_ass_pgiro(i).es_orig_acc_pgiro := aAccPG.esercizio_originale;
    	      tb_ass_pgiro(i).pg_acc_pgiro      := aAccPG.pg_accertamento;
						tb_ass_pgiro(i).ti_origine_pgiro := 'E';
				end if;
			End Loop;
 Exception when NO_DATA_FOUND then
 	   IBMERR001.RAISE_ERR_GENERICO('Errore a ');
end;
			IF TROVATO_DETTAGLIO_P_GIRO = 'N' THEN
         IBMERR001.RAISE_ERR_GENERICO('Per il cds '||aAggregato.cd_cds||', l''esercizio '||aAggregato.esercizio||', la UO '||aAggregato.cd_unita_organizzativa||', la liquidazione '||aAggregato.pg_liquidazione||
         			', il gruppo '||aAggregato.cd_gruppo_cr||', la regione '||aAggregato.cd_regione||', il comune '||aAggregato.pg_comune||' non ? stata trovata la partita di giro di dettaglio');
			END IF;			
    ELSE
      aAccPG:=null;
      aAccPGScad:=null;
      begin
       select distinct a.cd_elemento_voce
       into aCdEV
       from ass_tipo_cori_ev a, tipo_cr_base b
       where
               b.esercizio = aAggregato.esercizio
              and b.cd_gruppo_cr = aAggregato.cd_gruppo_cr
              and a.cd_contributo_ritenuta = b.cd_contributo_ritenuta
              and a.esercizio = aAggregato.esercizio
              and a.ti_gestione = CNRCTB001.GESTIONE_ENTRATE
              and a.ti_appartenenza = CNRCTB001.APPARTENENZA_CNR;
      exception when TOO_MANY_ROWS then
          		   IBMERR001.RAISE_ERR_GENERICO('Esiste pi? di un conto finanziario associato a CORI del gruppo '||aAggregato.cd_gruppo_cr);
         		   when NO_DATA_FOUND then
          		   IBMERR001.RAISE_ERR_GENERICO('Conto finanziario associato a CORI del gruppo '||aAggregato.cd_gruppo_cr||' non trovato');
      end;
      begin
	      select * into aContoColl from ass_partita_giro where
            esercizio = aAggregato.esercizio
        and ti_gestione = CNRCTB001.GESTIONE_ENTRATE
        and ti_appartenenza = CNRCTB001.APPARTENENZA_CNR
        and cd_voce = aCdEV;
      exception when NO_DATA_FOUND then
        IBMERR001.RAISE_ERR_GENERICO('Conto associato in partita di giro non trovato per voce di entrata:'||aCdEV);
      end;
      
      -- Estraggo il terzo UO relativo all'UO SAC che accentra i versamenti
      CNRCTB080.GETTERZOPERUO(aLGCC.cd_unita_organizzativa,aCdTerzoUo, aCdModPagUo, aPgBancaUo,aAggregato.esercizio);
      
      aObb:=null;
      aObbScad:=null;
      aObb.CD_CDS:=aUOVERSACC.cd_unita_padre;
      aObb.ESERCIZIO:=aAggregato.esercizio;
      aObb.CD_TIPO_DOCUMENTO_CONT:=CNRCTB018.TI_DOC_OBB_PGIRO;
      aObb.CD_UNITA_ORGANIZZATIVA:=aUOVERSACC.cd_unita_organizzativa;
      aObb.CD_CDS_ORIGINE:=aUOVERSACC.cd_unita_padre;
      aObb.CD_UO_ORIGINE:=aUOVERSACC.cd_unita_organizzativa;
      aObb.TI_APPARTENENZA:=aContoColl.ti_appartenenza_clg;
      aObb.TI_GESTIONE:=aContoColl.ti_gestione_clg;
      aObb.CD_ELEMENTO_VOCE:=aContoColl.cd_voce_clg;
      aObb.DT_REGISTRAZIONE:=TRUNC(aDateCont);
      aObb.DS_OBBLIGAZIONE:='CORI-VA compensazione gruppo cr:'||aAggregato.cd_gruppo_cr||'.'||aAggregato.cd_regione||'.'||aAggregato.pg_comune;
      aObb.NOTE_OBBLIGAZIONE:='';
      aObb.CD_TERZO:=aCdTerzoUo;
      aObb.IM_OBBLIGAZIONE:=abs(aAggregato.im_liquidato);
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
      aObb.ESERCIZIO_COMPETENZA:=aAggregato.esercizio;
      
      CNRCTB030.CREAOBBLIGAZIONEPGIRO(false,aObb,aObbScad,aAccPG,aAccPGScad,trunc(aTSNow));
    END IF;
    update liquid_gruppo_centro_comp set
           cd_cds_acc_accentr = aAccPG.cd_cds
          ,esercizio_acc_accentr = aAccPG.esercizio
          ,esercizio_ori_acc_accentr = aAccPG.esercizio_originale
          ,pg_acc_accentr = aAccPG.pg_accertamento
 	  where esercizio = aLGC.esercizio
    	  and cd_gruppo_cr = aLGC.cd_gruppo_cr
	 		  and cd_regione = aLGC.cd_regione
	 		  and pg_comune = aLGC.pg_comune
	 		  and pg_gruppo_centro = aLGC.pg_gruppo_centro
	 		  and cd_unita_organizzativa = aAggregato.cd_unita_organizzativa;
  else  -- aAggregato.im_liquidato > 0

   if aLGC.pg_obb_accentr is not null then
    --CNRCTB043.modificaPraticaObb(aLGC.esercizio_obb_accentr,aLGC.cd_cds_obb_accentr,aLGC.esercizio_ori_obb_accentr,aLGC.pg_obb_accentr,aAggregato.im_liquidato,aTSNow,aUser);
    --aggiunto l'ultimo parametro aAccConScad = 'Y' per indicare che sono gestite le scadenze per le pgiro di entrata
		IF recParametriCNR.FL_TESORERIA_UNICA = 'Y' THEN
    	aObbGiro := null;
    	aObbGiro.esercizio:=aLGC.esercizio_obb_accentr;
	  	aObbGiro.cd_cds:=aLGC.cd_cds_obb_accentr;
  		aObbGiro.esercizio_originale:=aLGC.esercizio_ori_obb_accentr;
  		aObbGiro.pg_obbligazione:=aLGC.pg_obb_accentr;
    	
    	CNRCTB035.GETPGIROCDS(aObbGiro,aObbScadGiro,aObbScadVoceGiro,aAccGiro,aAccScadGiro,aAccScadVoceGiro);
    	
    	CNRCTB043.modificaPraticaAcc(aAccGiro.esercizio,aAccGiro.cd_cds,aAccGiro.esercizio_originale,aAccGiro.pg_accertamento,aAggregato.im_liquidato,aTSNow,aUser);
			TROVATO_DETTAGLIO_P_GIRO := 'N';
begin
    	For i in 1..Nvl(tb_ass_pgiro.Count,0) Loop
				if 	aAggregato.cd_cds = tb_ass_pgiro(i).cd_cds and
						aAggregato.esercizio = tb_ass_pgiro(i).esercizio and
						aAggregato.cd_unita_organizzativa = tb_ass_pgiro(i).cd_uo and
						aAggregato.pg_liquidazione = tb_ass_pgiro(i).pg_liq and
						aAggregato.cd_cds_origine = tb_ass_pgiro(i).cd_cds_orig and
						aAggregato.cd_uo_origine = tb_ass_pgiro(i).cd_uo_orig and
						aAggregato.pg_liquidazione_origine = tb_ass_pgiro(i).pg_liq_orig and
						aAggregato.cd_gruppo_cr = tb_ass_pgiro(i).cd_gr_cr and
						aAggregato.cd_regione = tb_ass_pgiro(i).cd_regione and
						aAggregato.pg_comune = tb_ass_pgiro(i).pg_comune then
			
						TROVATO_DETTAGLIO_P_GIRO := 'S';
    	
  					tb_ass_pgiro(i).cd_cds_obb_pgiro 	:= aObbGiro.cd_cds;
    	      tb_ass_pgiro(i).es_obb_pgiro   		:= aObbGiro.esercizio;
    	      tb_ass_pgiro(i).es_orig_obb_pgiro := aObbGiro.esercizio_originale;
    	      tb_ass_pgiro(i).pg_obb_pgiro      := aObbGiro.pg_obbligazione;
						tb_ass_pgiro(i).ti_origine_pgiro := 'S';
				end if;					
			End Loop;
 Exception when NO_DATA_FOUND then
 	   IBMERR001.RAISE_ERR_GENERICO('Errore b ');
end;
			IF TROVATO_DETTAGLIO_P_GIRO = 'N' THEN
         IBMERR001.RAISE_ERR_GENERICO('Per il cds '||aAggregato.cd_cds||', l''esercizio '||aAggregato.esercizio||', la UO '||aAggregato.cd_unita_organizzativa||', la liquidazione '||aAggregato.pg_liquidazione||
         			', il gruppo '||aAggregato.cd_gruppo_cr||', la regione '||aAggregato.cd_regione||', il comune '||aAggregato.pg_comune||' non ? stata trovata la partita di giro di dettaglio');
			END IF;			
    ELSE
    	CNRCTB043.modificaPraticaObb(aLGC.esercizio_obb_accentr,aLGC.cd_cds_obb_accentr,aLGC.esercizio_ori_obb_accentr,aLGC.pg_obb_accentr,aAggregato.im_liquidato,aTSNow,aUser,aAccConScad);
    END IF;
   else
    aObbPG:=null;
    aObbPGScad:=null;
		IF recParametriCNR.FL_TESORERIA_UNICA = 'Y' THEN
      aObbNew:=null;
      aObbScadNew:=null;
      aAccNew:=null;
      aAccScadNew:=null;
      --determino il capitolo da mettere sulla pgiro
      Begin
         Select distinct a.cd_elemento_voce
         Into aCdEV
         From ass_tipo_cori_ev a, tipo_cr_base b
         Where b.esercizio = aAggregato.esercizio
           And b.cd_gruppo_cr = aAggregato.cd_gruppo_cr
           And a.cd_contributo_ritenuta = b.cd_contributo_ritenuta
           And a.esercizio = aAggregato.esercizio
           And a.ti_gestione = CNRCTB001.GESTIONE_SPESE
           And a.ti_appartenenza = CNRCTB001.APPARTENENZA_CDS;
      Exception 
              when TOO_MANY_ROWS then
              	   IBMERR001.RAISE_ERR_GENERICO('Esiste pi? di un conto finanziario associato a CORI del gruppo '||aAggregato.cd_gruppo_cr);
              when NO_DATA_FOUND then
                   IBMERR001.RAISE_ERR_GENERICO('Conto finanziario associato a CORI del gruppo '||aAggregato.cd_gruppo_cr||' non trovato');
      End;

      Begin
         Select *
         Into elementoVoce
         From elemento_voce
         Where esercizio = aAggregato.esercizio
           And ti_appartenenza = CNRCTB001.APPARTENENZA_CDS
           And ti_gestione = CNRCTB001.GESTIONE_SPESE
           And cd_elemento_voce = aCdEV;
      Exception 
              when NO_DATA_FOUND then
             	   IBMERR001.RAISE_ERR_GENERICO('Codice voce '||aCdEV||' associata a CORI del gruppo '||aAggregato.cd_gruppo_cr||' non esistente');
      End;
      
      aObbNew.CD_CDS:=aUOVERSACC.cd_unita_padre;
      aObbNew.ESERCIZIO:=aAggregato.esercizio;
      aObbNew.ESERCIZIO_ORIGINALE:=aAggregato.esercizio;
      aObbNew.CD_TIPO_DOCUMENTO_CONT:=CNRCTB018.TI_DOC_OBB_PGIRO;
      aObbNew.CD_UNITA_ORGANIZZATIVA:=aUOVERSACC.cd_unita_organizzativa;
      aObbNew.CD_CDS_ORIGINE:=aUOVERSACC.cd_unita_padre;
      aObbNew.CD_UO_ORIGINE:=aUOVERSACC.cd_unita_organizzativa;
      aObbNew.TI_APPARTENENZA:=elementoVoce.ti_appartenenza;
      aObbNew.TI_GESTIONE:=elementoVoce.ti_gestione;
      aObbNew.CD_ELEMENTO_VOCE:=aCdEV;
      aObbNew.DT_REGISTRAZIONE:=TRUNC(aDateCont);
      aObbNew.DS_OBBLIGAZIONE:='CORI-VA gruppo cr:'||aAggregato.cd_gruppo_cr||'.'||aAggregato.cd_regione||'.'||aAggregato.pg_comune;
      aObbNew.NOTE_OBBLIGAZIONE:='';
		  
		  aObbNew.CD_TERZO:=CNRCTB015.GETIM01PERCHIAVE(CNRCTB035.TERZO_SPECIALE,CNRCTB035.CODICE_DIVERSI_PGIRO);

      aObbNew.IM_OBBLIGAZIONE:=aAggregato.im_liquidato;
      aObbNew.stato_obbligazione:=CNRCTB035.STATO_DEFINITIVO;
      aObbNew.im_costi_anticipati:=0;
      aObbNew.fl_calcolo_automatico:='N';
      aObbNew.fl_spese_costi_altrui:='N';
      aObbNew.FL_PGIRO:='Y';
      aObbNew.RIPORTATO:='N';
      aObbNew.DACR:=aTSNow;
      aObbNew.UTCR:=aUser;
      aObbNew.DUVA:=aTSNow;
      aObbNew.UTUV:=aUser;
      aObbNew.PG_VER_REC:=1;
      aObbNew.ESERCIZIO_COMPETENZA:=aAggregato.esercizio;

      CNRCTB030.CREAOBBLIGAZIONEPGIROTRONC(false,aObbNew,aObbScadNew,aAccNew,aAccScadNew,trunc(aTSNow));
      
      aObbPG:=aObbNew;
    	aObbPGScad:=aObbScadNew;
    	aAcc:=aAccNew;
    	aAccScad:=aAccScadNew;

			TROVATO_DETTAGLIO_P_GIRO := 'N';
			


begin
    	For i in 1..Nvl(tb_ass_pgiro.Count,0) Loop
				if 	aAggregato.cd_cds = tb_ass_pgiro(i).cd_cds and
						aAggregato.esercizio = tb_ass_pgiro(i).esercizio and
						aAggregato.cd_unita_organizzativa = tb_ass_pgiro(i).cd_uo and
						aAggregato.pg_liquidazione = tb_ass_pgiro(i).pg_liq and
						aAggregato.cd_cds_origine = tb_ass_pgiro(i).cd_cds_orig and
						aAggregato.cd_uo_origine = tb_ass_pgiro(i).cd_uo_orig and
						aAggregato.pg_liquidazione_origine = tb_ass_pgiro(i).pg_liq_orig and
						aAggregato.cd_gruppo_cr = tb_ass_pgiro(i).cd_gr_cr and
						aAggregato.cd_regione = tb_ass_pgiro(i).cd_regione and
						aAggregato.pg_comune = tb_ass_pgiro(i).pg_comune then
			
						TROVATO_DETTAGLIO_P_GIRO := 'S';
    	
  					tb_ass_pgiro(i).cd_cds_obb_pgiro 	:= aObbNew.cd_cds;
    	      tb_ass_pgiro(i).es_obb_pgiro   		:= aObbNew.esercizio;
    	      tb_ass_pgiro(i).es_orig_obb_pgiro := aObbNew.esercizio_originale;
    	      tb_ass_pgiro(i).pg_obb_pgiro      := aObbNew.pg_obbligazione;
						tb_ass_pgiro(i).ti_origine_pgiro := 'S';
				end if;					
			End Loop;
 Exception when NO_DATA_FOUND then
 	   IBMERR001.RAISE_ERR_GENERICO('Errore c1 ');
end;
			IF TROVATO_DETTAGLIO_P_GIRO = 'N' THEN
         IBMERR001.RAISE_ERR_GENERICO('Per il cds '||aAggregato.cd_cds||', l''esercizio '||aAggregato.esercizio||', la UO '||aAggregato.cd_unita_organizzativa||', la liquidazione '||aAggregato.pg_liquidazione||
         			', il gruppo '||aAggregato.cd_gruppo_cr||', la regione '||aAggregato.cd_regione||', il comune '||aAggregato.pg_comune||' non ? stata trovata la partita di giro di dettaglio');
			END IF;
		ELSE
      aObbPG:=null;
      aObbPGScad:=null;
      begin
       select distinct a.cd_elemento_voce
       into aCdEV
       from ass_tipo_cori_ev a, tipo_cr_base b
       where b.esercizio = aAggregato.esercizio
         and b.cd_gruppo_cr = aAggregato.cd_gruppo_cr
         and a.cd_contributo_ritenuta = b.cd_contributo_ritenuta
         and a.esercizio = aAggregato.esercizio
         and a.ti_gestione = CNRCTB001.GESTIONE_ENTRATE
         and a.ti_appartenenza = CNRCTB001.APPARTENENZA_CNR;
      exception when TOO_MANY_ROWS then
         IBMERR001.RAISE_ERR_GENERICO('Esiste pi? di un conto finanziario associato a CORI del gruppo '||aAggregato.cd_gruppo_cr);
       		   when NO_DATA_FOUND then
        		   IBMERR001.RAISE_ERR_GENERICO('Conto finanziario associato a CORI del gruppo '||aAggregato.cd_gruppo_cr||' non trovato');
      end;
      begin
       	select 	*
      	into 		elementoVoce
      	from 		elemento_voce
      	where esercizio = aAggregato.esercizio
          and ti_appartenenza = CNRCTB001.APPARTENENZA_CNR
          and ti_gestione = CNRCTB001.GESTIONE_ENTRATE
         	and cd_elemento_voce = aCdEV;
      exception when NO_DATA_FOUND then
       	IBMERR001.RAISE_ERR_GENERICO('Codice voce '||aCdEV||' associata a CORI del gruppo '||aAggregato.cd_gruppo_cr||' non esistente');
      end;

    	aAcc:=null;
    	aAccScad:=null;
    	aAcc.CD_CDS:=aUOVERSACC.cd_unita_padre;
    	aAcc.ESERCIZIO:=aAggregato.esercizio;
    	aAcc.CD_TIPO_DOCUMENTO_CONT:=CNRCTB018.TI_DOC_ACC_PGIRO;
    	aAcc.CD_UNITA_ORGANIZZATIVA:=aUOVERSACC.cd_unita_organizzativa;
    	aAcc.CD_CDS_ORIGINE:=aUOVERSACC.cd_unita_padre;
    	aAcc.CD_UO_ORIGINE:=aUOVERSACC.cd_unita_organizzativa;
    	aAcc.TI_APPARTENENZA:=elementoVoce.ti_APPARTENENZA;
    	aAcc.TI_GESTIONE:=elementoVoce.ti_GESTIONE;
    	-- Utilizzo come conto il primo conto che trovo di un CORI appartenente al gruppo
    	aAcc.CD_ELEMENTO_VOCE:=aCdEV;
    	aAcc.CD_VOCE:=elementoVoce.cd_elemento_voce;
    	aAcc.DT_REGISTRAZIONE:=TRUNC(aDateCont);
    	aAcc.DS_ACCERTAMENTO:='CORI-VA gruppo cr:'||aAggregato.cd_gruppo_cr||'.'||aAggregato.cd_regione||'.'||aAggregato.pg_comune;
    	aAcc.NOTE_ACCERTAMENTO:='';
    	aAcc.CD_TERZO:=aCdTerzoAcc;
    	aAcc.FL_PGIRO:='Y';
    	aAcc.RIPORTATO:='N';
    	aAcc.DACR:=aTSNow;
    	aAcc.UTCR:=aUser;
    	aAcc.DUVA:=aTSNow;
    	aAcc.UTUV:=aUser;
    	aAcc.PG_VER_REC:=1;
    	aAcc.ESERCIZIO_COMPETENZA:=aAggregato.esercizio;
    	aAcc.IM_ACCERTAMENTO:=aAggregato.im_liquidato;
    	CNRCTB040.CREAACCERTAMENTOPGIRO(false,aAcc,aAccScad,aObbPG,aObbPGScad,trunc(aTSNow));
    	
    	-- Crea generico e reversale provvisoria
    	
    	aAnagTst:=CNRCTB080.GETANAG(aCdTerzoAcc);
    	
    	aGen:=null;
    	aGenRiga:=null;
			aListGenRighe.delete;
    	aGen.CD_TIPO_DOCUMENTO_AMM:=CNRCTB100.TI_GEN_CORI_ACC_ENTRATA;
    	aGen.CD_CDS:=aAcc.cd_cds;
    	aGen.CD_UNITA_ORGANIZZATIVA:=aAcc.cd_unita_organizzativa;
    	aGen.ESERCIZIO:=aAcc.esercizio;
    	aGen.CD_CDS_ORIGINE:=aAcc.cd_cds;
    	aGen.CD_UO_ORIGINE:=aAcc.cd_unita_organizzativa;
    	aGen.IM_TOTALE:=aAcc.im_accertamento;
    	aGen.DATA_REGISTRAZIONE:=TRUNC(aDateCont);
    	aGen.DT_DA_COMPETENZA_COGE:=TRUNC(aDateCont);
    	aGen.DT_A_COMPETENZA_COGE:=TRUNC(aDateCont);
    	aGen.DS_DOCUMENTO_GENERICO:='CORI-VA gruppo cr:'||aAggregato.cd_gruppo_cr||'.'||aAggregato.cd_regione||'.'||aAggregato.pg_comune;
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
    	aGen.DT_SCADENZA:=TRUNC(aTSNow);
    	aGen.STATO_PAGAMENTO_FONDO_ECO:=CNRCTB100.STATO_NO_PFONDOECO;
    	aGen.TI_ASSOCIATO_MANREV:=CNRCTB100.TI_ASSOC_TOT_MAN_REV ;
    	aGenRiga.CD_CDS:=aGen.CD_CDS;
    	aGenRiga.CD_UNITA_ORGANIZZATIVA:=aGen.CD_UNITA_ORGANIZZATIVA;
    	aGenRiga.ESERCIZIO:=aGen.ESERCIZIO;
    	aGenRiga.CD_TIPO_DOCUMENTO_AMM:=aGen.CD_TIPO_DOCUMENTO_AMM;
    	aGenRiga.DS_RIGA:=aGen.DS_DOCUMENTO_GENERICO;
    	aGenRiga.IM_RIGA_DIVISA:=aGen.IM_TOTALE;
    	aGenRiga.IM_RIGA:=aGen.IM_TOTALE;
    	aGenRiga.RAGIONE_SOCIALE:=aAnagTst.ragione_sociale;
    	aGenRiga.NOME:=aAnagTst.nome;
    	aGenRiga.COGNOME:=aAnagTst.cognome;
    	aGenRiga.CODICE_FISCALE:=aAnagTst.codice_fiscale;
    	aGenRiga.PARTITA_IVA:=aAnagTst.partita_iva;
    	aGenRiga.DT_DA_COMPETENZA_COGE:=aGen.DT_DA_COMPETENZA_COGE;
    	aGenRiga.DT_A_COMPETENZA_COGE:=aGen.DT_A_COMPETENZA_COGE;
    	aGenRiga.STATO_COFI:=aGen.STATO_COFI;
    	aGenRiga.CD_TERZO:=aAcc.cd_terzo;
    	aGenRiga.CD_CDS_ACCERTAMENTO:=aAcc.CD_CDS;
    	aGenRiga.ESERCIZIO_ACCERTAMENTO:=aAcc.ESERCIZIO;
    	aGenRiga.ESERCIZIO_ORI_ACCERTAMENTO:=aAcc.ESERCIZIO_ORIGINALE;
    	aGenRiga.PG_ACCERTAMENTO:=aAcc.PG_ACCERTAMENTO;
    	aGenRiga.PG_ACCERTAMENTO_SCADENZARIO:=1;
    	aGenRiga.CD_TERZO_UO_CDS:=aCdTerzoAcc;
    	aGenRiga.PG_BANCA_UO_CDS:=aPgBancaAcc;
    	aGenRiga.CD_MODALITA_PAG_UO_CDS:=aCdModPagAcc;
    	aGenRiga.DACR:=aGen.DACR;
    	aGenRiga.UTCR:=aGen.UTCR;
    	aGenRiga.UTUV:=aGen.UTUV;
    	aGenRiga.DUVA:=aGen.DUVA;
    	aGenRiga.PG_VER_REC:=aGen.PG_VER_REC;
    	aGenRiga.TI_ASSOCIATO_MANREV:=aGen.TI_ASSOCIATO_MANREV;
    	aListGenRighe(1):=aGenRiga;
    	CNRCTB110.CREAGENERICOAGGOBBACC(aGen,aListGenRighe);
    	
    	aRev:=null;
    	aRevRiga:=null;
    	aRev.CD_CDS:=aAcc.cd_cds;
    	aRev.ESERCIZIO:=aAcc.esercizio;
    	aRev.CD_UNITA_ORGANIZZATIVA:=aAcc.cd_unita_organizzativa;
    	aRev.CD_CDS_ORIGINE:=aAcc.cd_cds;
    	aRev.CD_UO_ORIGINE:=aAcc.cd_unita_organizzativa;
    	aRev.CD_TIPO_DOCUMENTO_CONT:=CNRCTB018.TI_DOC_REV_PROVV;
    	aRev.TI_REVERSALE:=CNRCTB038.TI_REV_SOS;
    	aRev.TI_COMPETENZA_RESIDUO:=CNRCTB038.TI_MAN_COMP;
    	aRev.DS_REVERSALE:='CORI-VA gruppo cr:'||aAggregato.cd_gruppo_cr||'.'||aAggregato.cd_regione||'.'||aAggregato.pg_comune;
    	aRev.STATO:=CNRCTB038.STATO_REV_EME;
    	aRev.DT_EMISSIONE:=TRUNC(aDateCont);
    	aRev.IM_REVERSALE:=aAcc.im_accertamento;
    	aRev.IM_INCASSATO:=0;
    	aRev.DACR:=aTSNow;
    	aRev.UTCR:=aUser;
    	aRev.DUVA:=aTSNow;
    	aRev.UTUV:=aUser;
    	aRev.PG_VER_REC:=1;
    	aRev.STATO_TRASMISSIONE:=CNRCTB038.STATO_REV_TRASCAS_NODIST;
    	aRevRiga.CD_CDS:=aRev.cd_cds;
    	aRevRiga.ESERCIZIO:=aRev.esercizio;
    	aRevRiga.ESERCIZIO_ACCERTAMENTO:=aAcc.esercizio;
    	aRevRiga.ESERCIZIO_ORI_ACCERTAMENTO:=aAcc.esercizio_originale;
    	aRevRiga.PG_ACCERTAMENTO:=aAcc.pg_accertamento;
    	aRevRiga.PG_ACCERTAMENTO_SCADENZARIO:=aAccScad.pg_accertamento_scadenzario;
    	aRevRiga.CD_CDS_DOC_AMM:=aGen.cd_cds;
    	aRevRiga.CD_UO_DOC_AMM:=aGen.cd_unita_organizzativa;
    	aRevRiga.ESERCIZIO_DOC_AMM:=aGen.esercizio;
    	aRevRiga.CD_TIPO_DOCUMENTO_AMM:=aGen.cd_tipo_documento_amm;
    	aRevRiga.PG_DOC_AMM:=aGen.pg_documento_generico;
    	aRevRiga.DS_REVERSALE_RIGA:=aRev.ds_reversale;
    	aRevRiga.STATO:=aRev.STATO;
    	aRevRiga.CD_TERZO:=aGenRiga.cd_terzo;
    	aRevRiga.CD_TERZO_UO:=aGenRiga.cd_terzo_uo_cds;
    	aRevRiga.PG_BANCA:=aGenRiga.pg_banca_uo_cds;
    	aRevRiga.CD_MODALITA_PAG:=aGenRiga.cd_modalita_pag_uo_cds;
    	aRevRiga.IM_REVERSALE_RIGA:=aRev.im_reversale;
    	aRevRiga.FL_PGIRO:='Y';
    	aRevRiga.UTCR:=aUser;
    	aRevRiga.DACR:=aTSNow;
    	aRevRiga.UTUV:=aUser;
    	aRevRiga.DUVA:=aTSNow;
    	aRevRiga.PG_VER_REC:=1;
    	CNRCTB037.generaRevProvvPgiro(aRev, aRevRiga, aTSNow, aUser);
		END IF;	
		update 	liquid_gruppo_centro 
		set    	cd_cds_obb_accentr = aObbPG.cd_cds
         ,esercizio_obb_accentr = aObbPG.esercizio
	       ,esercizio_ori_obb_accentr = aObbPG.esercizio_originale
	       ,pg_obb_accentr = aObbPG.pg_obbligazione
 		where	esercizio = aLGC.esercizio
      And cd_gruppo_cr = aLGC.cd_gruppo_cr
	 		and cd_regione = aLGC.cd_regione
	 		and pg_comune = aLGC.pg_comune
	 		and pg_gruppo_centro = aLGC.pg_gruppo_centro;
-- Fix mancato aggiornamento pg_obb_accentr su liquid_gruppo_cori_locale alla creazione obb accentr
-- sul gruppo CORI al centro
		
		aLGC.cd_cds_obb_accentr:=aObbPG.cd_cds;
		aLGC.esercizio_obb_accentr:=aObbPG.esercizio;
		aLGC.esercizio_ori_obb_accentr:=aObbPG.esercizio_originale;
		aLGC.pg_obb_accentr:=aObbPG.pg_obbligazione;
   end if;
  end if;

  -- Aggiorna liquid_gruppo_cori con le informazioni relative all'obbligazione pgiro su UO che versa
  if aAggregato.im_liquidato < 0 Then
   	update liquid_gruppo_cori
    set pg_gruppo_centro = aLGC.pg_gruppo_centro,
     		stato = CNRCTB575.STATO_TRASFERITO
    where esercizio = aAggregato.esercizio
      and cd_cds = aAggregato.cd_cds
      and cd_unita_organizzativa = aAggregato.cd_unita_organizzativa
      and cd_cds_origine = aAggregato.cd_cds_origine
      and cd_uo_origine = aAggregato.cd_uo_origine
      and pg_liquidazione = aAggregato.pg_liquidazione
      and pg_liquidazione_origine = aAggregato.pg_liquidazione_origine
      and cd_gruppo_cr = aAggregato.cd_gruppo_cr
      and cd_regione = aAggregato.cd_regione
      and pg_comune = aAggregato.pg_comune;
  else
   	update liquid_gruppo_cori
    set  	cd_cds_obb_accentr = aLGC.cd_cds_obb_accentr,
     			esercizio_obb_accentr = aLGC.esercizio_obb_accentr,
     			esercizio_ori_obb_accentr = aLGC.esercizio_ori_obb_accentr,
     			pg_obb_accentr = aLGC.pg_obb_accentr,
     			pg_gruppo_centro = aLGC.pg_gruppo_centro,
     			stato = CNRCTB575.STATO_TRASFERITO
    where esercizio = aAggregato.esercizio
      and cd_cds = aAggregato.cd_cds
      and cd_unita_organizzativa = aAggregato.cd_unita_organizzativa
      and cd_cds_origine = aAggregato.cd_cds_origine
      and cd_uo_origine = aAggregato.cd_uo_origine
      and pg_liquidazione = aAggregato.pg_liquidazione
      and pg_liquidazione_origine = aAggregato.pg_liquidazione_origine
      and cd_gruppo_cr = aAggregato.cd_gruppo_cr
      and cd_regione = aAggregato.cd_regione
      and pg_comune = aAggregato.pg_comune;
  end if;
	CREA_ASS_PGIRO_GR_C(tb_ass_pgiro, aUser, aTSNow);
 end;

 procedure vsx_liquida_cori(
       pgCall NUMBER
 ) is
   aEs number(4);
   aCdCds varchar2(30);
   aCdUo varchar2(30);
   aUOVERSACC unita_organizzativa%rowtype;
   aUOVERSCONTOBI unita_organizzativa%rowtype;
	 CONTO_BI	varchar2(1);
   aPgLiq number(8);
   aAssObbAcr ass_obb_acr_pgiro%rowtype;
   aUser varchar2(20);
   aTSNow date;
   aObb obbligazione%rowtype;
   aObbScad obbligazione_scadenzario%rowtype;
   aObbScadVoce obbligazione_scad_voce%rowtype;
   aAcc accertamento%rowtype;
   aAccScad accertamento_scadenzario%rowtype;
   aAccScadVoce accertamento_scad_voce%rowtype;
   aAccOrig accertamento%rowtype;
   aObbVC obbligazione%rowtype;
   aGen documento_generico%rowtype;
   aGenRiga documento_generico_riga%rowtype;
   aListGenRighe CNRCTB100.docGenRigaList;
   aTotMandato number(15,2);
   aTotReversale number(15,2);
   aManP mandato%rowtype;
   aManPRiga mandato_riga%rowtype;
   aListRigheManP CNRCTB038.righeMandatoList;
   aRevP reversale%rowtype;
   aRevPRiga reversale_riga%rowtype;
   aListRigheRevP CNRCTB038.righeReversaleList;
   aDivisaEuro varchar2(30);
   aLiquidCori liquid_cori%rowtype;
   aLiquidGruppoCori liquid_gruppo_cori_det%rowtype;
   aLiquidGruppoCoriDet liquid_gruppo_cori_det%rowtype;
   aTipoGenerico varchar2(10);
   aAnagTst anagrafico%rowtype;
   aLiquid liquid_cori%rowtype;
   aLiquidOrig liquid_cori%rowtype;
   isLiquidParzAcc boolean;
   aStatoLiquidazione char(1);
   elementoVoce elemento_voce%rowtype;
   aCdTerzoAcc number(8);
   aCdModPagAcc varchar2(10);
   aPgBancaAcc number(10);
   aCdTerzoUO number(8);
   aCdModPagUO varchar2(10);
   aPgBancaUO number(10);
   lIsCdsInterDet boolean;
   lIsCdsInterTot boolean;
   aAccTmp accertamento%rowtype;
   aAccScadTmp accertamento_scadenzario%rowtype;
   aAccScadVoceTmp accertamento_scad_voce%rowtype;
   aObbTmp obbligazione%rowtype;
   aObbScadTmp obbligazione_scadenzario%rowtype;
   aObbScadVoceTmp obbligazione_scad_voce%rowtype;
   aCdEV varchar2(20);
   aEVComp elemento_voce%rowtype;
   aDateCont date;
   UOENTE unita_organizzativa%rowtype;
   tipo_ic  CHAR(1);
   aObbOld obbligazione%rowtype;
   aObbScadOld obbligazione_scadenzario%rowtype;
   aObbScadVoceOld obbligazione_scad_voce%rowtype;
   aAccOld accertamento%rowtype;
   aAccScadOld accertamento_scadenzario%rowtype;
   aAccScadVoceOld accertamento_scad_voce%rowtype;
   aObbNew obbligazione%rowtype;
   aObbScadNew obbligazione_scadenzario%rowtype;
   aObbScadVoceNew obbligazione_scad_voce%rowtype;
   aAccNew accertamento%rowtype;
   aAccScadNew accertamento_scadenzario%rowtype;
   aAccScadVoceNew accertamento_scad_voce%rowtype;
   recParametriCNR PARAMETRI_CNR%Rowtype;
   ind_pGiro		number := 0;
 begin
  for aPar in (select * from vsx_liquidazione_cori where pg_call = pgCall
  ) loop
        aEs:=aPar.esercizio;
   	aCdCds:=aPar.cd_cds;
   	aCdUo:=aPar.cd_unita_organizzativa;
   	aPgLiq:=aPar.pg_liquidazione;
   	aUser:=aPar.utcr;
   	aTSNow:=aPar.dacr;
	exit;
  end loop;

  if aEs is null then
   	 IBMERR001.RAISE_WRN_GENERICO('Nessun gruppo CORI trovato per la liquidazione');
  end if;
  recParametriCNR := CNRUTL001.getRecParametriCnr(aEs);

  -- Controllo che la liq. per il centro sia aperta nell'esercizio specificato
  CNRCTB575.CHECKLIQUIDCENTROAPERTA(aEs);

  -- Check su esercizio
  if
   not CNRCTB008.ISESERCIZIOAPERTO(aEs, aCdCds)
  then
   IBMERR001.RAISE_ERR_GENERICO('L''esercizio '||aEs||' non ? aperto per il CDS '||aCdCds);
  end if;

  aDateCont:=CNRCTB008.getTimestampContabile(aEs,aTSNow);

  begin
	select *
	into aLiquid
	from liquid_cori
	where esercizio = aEs
	  and cd_cds = aCdCds
	  and cd_unita_organizzativa = aCdUo
	  and pg_liquidazione = aPgLiq
	for update nowait;
  exception when NO_DATA_FOUND then
     IBMERR001.RAISE_ERR_GENERICO('Liquidazione CORI non trovata n.'||aPgLiq||' es.'||aEs||' cds:'||aCdCds||' uo:'||aCdUo);
  end;

   -- Elimino il dettaglio minimo della liquidazione di aLGC non selezionati
  delete from liquid_gruppo_cori_det a where
       cd_cds = aCdCds
   and esercizio = aEs
   and cd_unita_organizzativa = aCdUo
   and pg_liquidazione = aPgLiq
   and not exists (select 1 from vsx_liquidazione_cori aParD
   	   	   where aParD.pg_call = pgCall
		     and aParD.cd_cds = a.cd_cds
		     and aParD.esercizio = a.esercizio
		     and aParD.cd_unita_organizzativa = a.cd_unita_organizzativa
		     and aParD.pg_liquidazione = a.pg_liquidazione
		     and aParD.cd_cds_origine = a.cd_cds_origine
		     and aParD.cd_uo_origine = a.cd_uo_origine
		     and aParD.pg_liquidazione_origine = a.pg_liquidazione_origine
		     and aParD.cd_gruppo_cr = a.cd_gruppo_cr
		     and aParD.cd_regione = a.cd_regione
		     and aParD.pg_comune = a.pg_comune );
   -- Elimino aLGC non selezionati
  delete from liquid_gruppo_cori a Where
       cd_cds = aCdCds
   and esercizio = aEs
   and cd_unita_organizzativa = aCdUo
   and pg_liquidazione = aPgLiq
   and not exists (select 1 from vsx_liquidazione_cori aParD
   	   	   where aParD.pg_call = pgCall
	             and aParD.cd_cds = a.cd_cds
		     and aParD.esercizio = a.esercizio
		     and aParD.cd_unita_organizzativa = a.cd_unita_organizzativa
		     and aParD.pg_liquidazione = a.pg_liquidazione
		     and aParD.cd_cds_origine = a.cd_cds_origine
		     and aParD.cd_uo_origine = a.cd_uo_origine
		     and aParD.pg_liquidazione_origine = a.pg_liquidazione_origine
		     and aParD.cd_gruppo_cr = a.cd_gruppo_cr
		     and aParD.cd_regione = a.cd_regione
		     and aParD.pg_comune = a.pg_comune);
		     
      -- Flag che determina se l'UO in processo ? di CDS versato via interfaccia
      lIsCdsInterDet := IsCdsInterfDet(aCdCds,aEs);
      lIsCdsInterTot := IsCdsInterfTot(aCdCds,aEs);

      aDivisaEuro:=CNRCTB015.GETVAL01PERCHIAVE(CNRCTB100.CCNR_DIVISA,CNRCTB100.CCNR_EURO);
      aUOVERSACC:=CNRCTB020.getUOVersCori(aEs);
      aUOVERSCONTOBI:=CNRCTB020.getUOVersCoriContoBI(aEs);

     -- Controllo che la liq. per locale sia aperta nell'esercizio specificato
     if aUOVERSACC.cd_unita_organizzativa <> aCdUO and aUOVERSCONTOBI.cd_unita_organizzativa <> aCdUO then
        CNRCTB575.CHECKLIQUIDLOCALEAPERTA(aEs);
     end if;

     UOENTE:=CNRCTB020.GETUOENTE(aEs);
     
     -- Check su esercizio
     If Not CNRCTB008.ISESERCIZIOAPERTO(aEs, aUOVERSACC.cd_unita_padre)
     Then
        IBMERR001.RAISE_ERR_GENERICO('L''esercizio '||aEs||' non ? aperto per il CDS di versamento CORI '||aUOVERSACC.cd_unita_padre);
     End If;

     -- Nel caso di liq. via interfaccia, devo ripulire in interfaccia
     -- il puntatore alla liquidazione per quei gruppi che l'utente ha scelto di NON versare
     -- PER L'INTERFACCIA DI TIPO DETTAGLIO:
     If lIsCdsInterDet Then
       For aLCInterf in (
           Select * From liquid_cori_interf_dett alc 
           Where esercizio=decode(aLiquid.da_esercizio_precedente,'Y',aEs-1,aEs)
      	     And cd_cds = aCdCds
      	     And cd_unita_organizzativa =	aCdUo
      	     And pg_liquidazione = aPgLiq
	           And cd_gruppo_cr is not null
             And Not Exists (
               Select 1 from vsx_liquidazione_cori aParD
               Where aParD.pg_call = pgCall
                 and aParD.cd_cds = aCdCds
                 and aParD.esercizio = aEs
                 and aParD.cd_unita_organizzativa = aCdUo
                 and aParD.pg_liquidazione = aPgLiq
                 and aParD.cd_uo_origine = cd_unita_organizzativa
                 and aParD.cd_gruppo_cr = alc.cd_gruppo_cr
                 and aParD.cd_regione = alc.cd_regione
                 and aParD.pg_comune = alc.pg_comune)
      	   For update nowait) Loop
	   		 Update liquid_cori_interf_dett
	   		 Set cd_gruppo_cr = null,
	   			    pg_liquidazione = null,
	   			    utuv = aUser,
	   			    duva = aTSNow,
	   			    pg_ver_rec = pg_ver_rec + 1
	   		 Where
               CD_CDS=aLCInterf.CD_CDS AND
               ESERCIZIO=aLCInterf.ESERCIZIO AND
               CD_UNITA_ORGANIZZATIVA=aLCInterf.CD_UNITA_ORGANIZZATIVA AND
               PG_CARICAMENTO=aLCInterf.PG_CARICAMENTO AND
               DT_INIZIO=aLCInterf.DT_INIZIO AND
               DT_FINE=aLCInterf.DT_FINE AND
               MATRICOLA=aLCInterf.MATRICOLA AND
               CODICE_FISCALE=aLCInterf.CODICE_FISCALE AND
               TI_PAGAMENTO=aLCInterf.TI_PAGAMENTO AND
               ESERCIZIO_COMPENSO=aLCInterf.ESERCIZIO_COMPENSO AND
               CD_IMPONIBILE=aLCInterf.CD_IMPONIBILE AND
               TI_ENTE_PERCIPIENTE=aLCInterf.TI_ENTE_PERCIPIENTE AND
               CD_CONTRIBUTO_RITENUTA=aLCInterf.CD_CONTRIBUTO_RITENUTA;
       End Loop;
     End If; --DELL'INTERFACCIA DI TIPO DETTAGLIO
     -- PER L'INTERFACCIA DI TIPO TOTALE:
     If lIsCdsInterTot Then
       For aLCInterf in (
           Select * From liquid_cori_interf alc 
           Where esercizio=decode(aLiquid.da_esercizio_precedente,'Y',aEs-1,aEs)
      	     And cd_cds = aCdCds
      	     And cd_unita_organizzativa =	aCdUo
      	     And pg_liquidazione = aPgLiq
	           And cd_gruppo_cr is not null
             And Not Exists (
               Select 1 from vsx_liquidazione_cori aParD
               Where aParD.pg_call = pgCall
                 and aParD.cd_cds = aCdCds
                 and aParD.esercizio = aEs
                 and aParD.cd_unita_organizzativa = aCdUo
                 and aParD.pg_liquidazione = aPgLiq
                 and aParD.cd_uo_origine = cd_unita_organizzativa
                 and aParD.cd_gruppo_cr = alc.cd_gruppo_cr
                 and aParD.cd_regione = alc.cd_regione
                 and aParD.pg_comune = alc.pg_comune)
      	   For update nowait) Loop
	       Update liquid_cori_interf
	       Set pg_liquidazione = null,
	           utuv = aUser,
	           duva = aTSNow,
	           pg_ver_rec = pg_ver_rec + 1
	       Where
               CD_CDS=aLCInterf.CD_CDS AND
               ESERCIZIO=aLCInterf.ESERCIZIO AND
               CD_UNITA_ORGANIZZATIVA=aLCInterf.CD_UNITA_ORGANIZZATIVA AND
               PG_CARICAMENTO=aLCInterf.PG_CARICAMENTO And
               CD_GRUPPO_CR=aLCInterf.CD_GRUPPO_CR And
               CD_REGIONE = aLCInterf.CD_REGIONE And
               PG_COMUNE = aLCInterf.PG_COMUNE And
               DT_INIZIO=aLCInterf.DT_INIZIO And
               DT_FINE=aLCInterf.DT_FINE;
       End Loop;
     End If;-- DELL'INTERFACCIA DI TIPO TOTALE
     -- Nel caso di UO di versamento accentrato, devo ripulire nei gruppi centro impattati
     -- il puntatore alla liquidazione per quei gruppi che l'utente ha scelto di NON versare
     If aUOVERSACC.cd_unita_organizzativa = aCdUo or aUOVERSCONTOBI.cd_unita_organizzativa = aCdUo then
       For aDGruppoCentro in (
           Select * From liquid_gruppo_centro 
           Where esercizio=aEs
             And cd_cds_lc = aCdCds
             And cd_uo_lc =	aCdUo
             And pg_lc = aPgLiq
             And stato = CNRCTB575.STATO_GRUPPO_CENTRO_INIZIALE
             For update nowait) 
       Loop

         Declare
           aLNum number;
         Begin
           Select distinct 1 into aLNum 
           From vsx_liquidazione_cori aParD
           Where aParD.pg_call = pgCall
             And aParD.cd_cds = aCdCds
             And aParD.esercizio = aEs
             And aParD.cd_unita_organizzativa = aCdUo
             And aParD.pg_liquidazione = aPgLiq
             And aParD.cd_uo_origine = UOENTE.cd_unita_organizzativa  
             And aParD.cd_gruppo_cr = aDGruppoCentro.cd_gruppo_cr
             And aParD.cd_regione = aDGruppoCentro.cd_regione
             And aParD.pg_comune = aDGruppoCentro.pg_comune;
         Exception when NO_DATA_FOUND Then
                Update liquid_gruppo_centro 
                Set cd_cds_lc = null,
                    cd_uo_lc = null,
                    pg_lc = null,
		                pg_ver_rec=pg_ver_rec+1
                Where esercizio = aEs
                  And cd_gruppo_cr = aDGruppoCentro.cd_gruppo_cr
                  And cd_regione = aDGruppoCentro.cd_regione
                  And pg_comune = aDGruppoCentro.pg_comune
                  And pg_gruppo_centro = aDGruppoCentro.pg_gruppo_centro;
         End;
       End loop;
     End If;

     -- Estraggo il terzo UO relativo all'UO SAC che accentra i versamenti
     CNRCTB080.GETTERZOPERUO(aUOVERSACC.cd_unita_organizzativa,aCdTerzoAcc, aCdModPagAcc, aPgBancaAcc,aEs);
    
     isLiquidParzAcc:=false;
    
     -- LIQUIDAZIONE DI UO
     -- CICLO SU AGGREGATI PROPRI
     -- Nel caso di UO di versamento centrale: raccolgo anche i gruppi dei versamenti locali
     -- Nel caso di UO di versamenti unificati: raccolgo anche i gruppi di tutte le UO della SAC
     
     -- Deve esistere un loop esterno che raggruppa i gruppi, per ognuno dei quali viene creato un Mandato
     -- (prima per ogni aggregato veniva creato un mandato poiche' non potevano esserci due uo con lo stesso gruppo)
     -- e il loop interno, gia' esistente, per ogni gruppo prende tutte le UO

  For aVersamenti In (Select a.cd_gruppo_cr,a.esercizio,a.cd_cds,a.cd_unita_organizzativa,
                         a.cd_terzo_versamento,a.cd_modalita_pagamento,a.pg_banca,fl_accentrato,Sum(a.im_liquidato) tot_liquidato, b.ti_mandato 
                   From V_LIQUID_GRUPPO_CORI_CR_DET a, rif_modalita_pagamento b 
     		   Where a.esercizio = aEs
                     And a.cd_cds = aCdCds
                     And a.cd_unita_organizzativa = aCdUo
                     And a.pg_liquidazione = aPgLiq
                     and a.cd_modalita_pagamento = b.cd_modalita_pag
    		     And Exists (Select 1 From vsx_liquidazione_cori v
    				  Where v.pg_call = pgCall
    				    And v.esercizio = a.esercizio
    			            And v.cd_cds = a.cd_cds
    				    And v.cd_unita_organizzativa = a.cd_unita_organizzativa
    				    And v.cd_cds_origine = a.cd_cds_origine
    				    And v.cd_uo_origine = a.cd_uo_origine
    				    And v.cd_gruppo_cr = a.cd_gruppo_cr
    				    And v.cd_regione = a.cd_regione
    				    And v.pg_comune = a.pg_comune
    				    And v.pg_liquidazione = a.pg_liquidazione
    				    And v.pg_liquidazione_origine = a.pg_liquidazione_origine)
                Group by a.cd_gruppo_cr,a.esercizio,a.cd_cds,a.cd_unita_organizzativa,
                         a.cd_terzo_versamento,a.cd_modalita_pagamento,a.pg_banca,fl_accentrato, b.ti_mandato
                Order by a.cd_gruppo_cr,a.esercizio,a.cd_cds,a.cd_unita_organizzativa,
                         a.cd_terzo_versamento,a.cd_modalita_pagamento,a.pg_banca,fl_accentrato         
  ) Loop -- LOOP VERSAMENTO aVersamenti
--pipe.send_message('aVersamenti.cd_gruppo_cr = '||aVersamenti.cd_gruppo_cr); 

			select 	FL_CONTO_BI
			INTO		CONTO_BI
			from		rif_modalita_pagamento
			WHERE		CD_MODALITA_PAG = aVersamenti.cd_modalita_pagamento;
			
      If aVersamenti.fl_accentrato = 'Y' then
          if aCdTerzoAcc is null or aCdModPagAcc is null or aPgBancaAcc is null then
          	 IBMERR001.RAISE_ERR_GENERICO('Dati relativi al terzo di versamento accentrato non trovati o non completi');
          end if;
          aAnagTst:=CNRCTB080.GETANAG(aCdTerzoAcc);
          isLiquidParzAcc:=true; -- Serve per marcare l'intera liquidazione come TRASFERITA (la chiusura dei pending verr? fatta alcentro)
      Else
          if aVersamenti.cd_terzo_versamento is null or aVersamenti.pg_banca is null or aVersamenti.cd_modalita_pagamento is null then
          	 IBMERR001.RAISE_ERR_GENERICO('Dati relativi al terzo di versamento CORI non trovati o non completi per gruppo:'||aVersamenti.cd_gruppo_cr);
          end if;
          aAnagTst:=CNRCTB080.GETANAG(aVersamenti.cd_terzo_versamento);
      End If;

--pipe.send_message('aVersamenti.cd_gruppo_cr '||aVersamenti.cd_gruppo_cr); 
--pipe.send_message('aVersamenti.tot_liquidato '||aVersamenti.tot_liquidato);  
      -- Gestione liquidazione gruppi negativi solo da esercizio precedente
      If aVersamenti.tot_liquidato < 0 then
        If aLiquid.da_esercizio_precedente = 'N' Or aVersamenti.fl_accentrato = 'N' Then
           IBMERR001.RAISE_ERR_GENERICO('Importo di liquidazione negativo o nullo per gruppo CORI: '||aVersamenti.cd_gruppo_cr||' - Terzo di versamento: '||aVersamenti.cd_terzo_versamento);
        End if;
      End if;
    	         
      -- Per i gruppi dei versamenti locali non devo controllare aVersamenti.tot_liquidato bens? devo fare
      -- la somma da V_LIQUID_CENTRO_UO perch? aVersamenti.tot_liquidato contiene solo la parte positiva
      -- e se la somma ? negativa il versamento deve essere bloccato
      --PER IL MOMENTO PENSO CHE DEVO BLOCCARE ANCHE SE UN SOLO DETTAGLIO (ISTITUTO) E' NEGATIVO ma nel secondo loop
      --QUESTO CONTROLLO FORSE VA MODIFICATO TOGLIENDO LA CONDIZIONE SU REGIONE E COMUNE E AGGIUNGENDO IL TERZO DI VERSAMENTO
      --?????????????????????
      
      If aUOVERSACC.cd_unita_organizzativa = aCdUo or aUOVERSCONTOBI.cd_unita_organizzativa = aCdUo Then
        Declare
           totale_negativo    number;
        Begin
           select nvl(sum(l.im_liquidato),0)
           into totale_negativo
           from  V_LIQUID_CENTRO_UO l, GRUPPO_CR_DET g
           where l.cd_cds = aCdCds
             and l.cd_unita_organizzativa = aCdUo
             and l.esercizio = aEs
             and l.pg_liquidazione = aPgLiq
             And l.esercizio = g.esercizio
             And l.cd_gruppo_cr = g.cd_gruppo_cr
             And l.cd_regione = g.cd_regione
             And l.pg_comune = g.pg_comune
             and l.cd_gruppo_cr = aVersamenti.cd_gruppo_cr
             And g.cd_terzo_versamento = aVersamenti.cd_terzo_versamento
             And g.cd_modalita_pagamento = aVersamenti.cd_modalita_pagamento
             And g.pg_banca = aVersamenti.pg_banca
             and im_liquidato < 0
             And Exists (Select 1 From vsx_liquidazione_cori v
    			 Where v.pg_call = pgCall
    			   And v.esercizio = aEs
    			   And v.cd_cds = aCdCds
    			   And v.cd_unita_organizzativa = aCdUo
  			   And v.cd_uo_origine = UOENTE.cd_unita_organizzativa
    			   And v.cd_gruppo_cr = aVersamenti.cd_gruppo_cr
    				    And v.cd_regione =  l.cd_regione
    				    And v.pg_comune = l.pg_comune
    				    And v.pg_liquidazione = aPgLiq
    				    And v.pg_liquidazione_origine = 0);
--pipe.send_message('aVersamenti.tot_liquidato + totale_negativo '||To_Char(aVersamenti.tot_liquidato + totale_negativo));
      	    If (aVersamenti.tot_liquidato + totale_negativo) < 0 then
               IBMERR001.RAISE_ERR_GENERICO('Esistono Gruppi Locali negativi che rendono tutta la liquidazione negativa per il gruppo CORI:'||aVersamenti.cd_gruppo_cr||' - Terzo di versamento:'||aVersamenti.cd_terzo_versamento);
            End if;
        End;
      End If;     
      
      -- Questa parte, relativa alla chiusura delle partite di giro dei compensi va tolta per i CDS caricati via interfaccia
      -- Inizio parte 1 esclusa per liq. di uo appartenenti a cds liquidati via interfaccia
      aTotMandato:=0;
      aTotReversale:=0;
      If Not lIsCdsInterDet And Not lIsCdsInterTot then
       		aManP:=null;
       		aManP.CD_CDS:=aCdCds;
       		aManP.ESERCIZIO:=aEs;
       		aManP.CD_UNITA_ORGANIZZATIVA:=aCdUo;
       		aManP.CD_CDS_ORIGINE:=aCdCds;
       		aManP.CD_UO_ORIGINE:=aCdUo;
       		aManP.CD_TIPO_DOCUMENTO_CONT:=CNRCTB018.TI_DOC_MAN;
       		if aVersamenti.ti_mandato = 'S' THEN
	       		aManP.TI_MANDATO:=CNRCTB038.TI_MAN_SOS;
					ELSE
	       		aManP.TI_MANDATO:=CNRCTB038.TI_MAN_PAG;
					END IF;
       		aManP.TI_COMPETENZA_RESIDUO:=CNRCTB038.TI_MAN_COMP;
       		aManP.DS_MANDATO:='Mandato di versamento CORI: '||aEs||'-'||aCdCds||' - '||aCdUo||' gruppo CORI:'||aVersamenti.cd_gruppo_cr;
       		aManP.STATO:=CNRCTB038.STATO_MAN_EME;
       		aManP.DT_EMISSIONE:=TRUNC(aDateCont);
       		aManP.IM_RITENUTE:=0;
       		--  aManP.DT_TRASMISSIONE:=;
       		--  aManP.DT_PAGAMENTO:=;
       		--  aManP.DT_ANNULLAMENTO:=;
       		aManP.IM_PAGATO:=0;
       		aManP.UTCR:=aUser;
       		aManP.DACR:=aTSNow;
       		aManP.UTUV:=aUser;
       		aManP.DUVA:=aTSNow;
       		aManP.PG_VER_REC:=1;
       		aManP.STATO_TRASMISSIONE:=CNRCTB038.STATO_MAN_TRASCAS_NODIST;
       		aRevP:=null;
       		aRevP.CD_CDS:=aCdCds;
       		aRevP.ESERCIZIO:=aEs;
       		aRevP.CD_UNITA_ORGANIZZATIVA:=aCdUo;
       		aRevP.CD_CDS_ORIGINE:=aCdCds;
       		aRevP.CD_UO_ORIGINE:=aCdUo;
       		aRevP.CD_TIPO_DOCUMENTO_CONT:=CNRCTB018.TI_DOC_REV;
       		aRevP.TI_REVERSALE:=CNRCTB038.TI_REV_INC;
       		aRevP.TI_COMPETENZA_RESIDUO:=CNRCTB038.TI_MAN_COMP;
       		aRevP.DS_REVERSALE:='Reversale di versamento CORI: '||aEs||'-'||aCdCds||' - '||aCdUo||' gruppo CORI:'||aVersamenti.cd_gruppo_cr;
       		aRevP.STATO:=CNRCTB038.STATO_REV_EME;
       		aRevP.DT_EMISSIONE:=TRUNC(aDateCont);
       		--  aRevP.DT_TRASMISSIONE:=;
       		--  aRevP.DT_PAGAMENTO:=;
       		--  aRevP.DT_ANNULLAMENTO:=;
       		aRevP.IM_INCASSATO:=0;
       		aRevP.UTCR:=aUser;
       		aRevP.DACR:=aTSNow;
       		aRevP.UTUV:=aUser;
       		aRevP.DUVA:=aTSNow;
       		aRevP.PG_VER_REC:=1;
       		aRevP.STATO_TRASMISSIONE:=CNRCTB038.STATO_REV_TRASCAS_NODIST;
       		-- Modifica del 23/04/2004
       		-- Le reversali di versamento CORI DEVONO essere processate in economica come i mandati
       		aRevP.STATO_COGE:=CNRCTB100.STATO_COEP_INI;
       		aListRigheManP.delete;
       		aListRigheRevP.delete;
      End If;    --fine       If not lIsCdsInterDet and not lIsCdsInterTot

  For aGruppi In (Select a.cd_gruppo_cr,a.cd_regione,a.pg_comune,a.esercizio,a.cd_cds,a.cd_unita_organizzativa,
                         a.cd_terzo_versamento,a.cd_modalita_pagamento,a.pg_banca,fl_accentrato,Sum(a.im_liquidato) im_liquidato
                   From V_LIQUID_GRUPPO_CORI_CR_DET a 
     		   Where a.esercizio = aEs
                     And a.cd_cds = aCdCds
                     And a.cd_unita_organizzativa = aCdUo
                     And a.pg_liquidazione = aPgLiq
                     And a.cd_gruppo_cr = aVersamenti.cd_gruppo_cr
                     And a.cd_terzo_versamento = aVersamenti.cd_terzo_versamento
                     And a.cd_modalita_pagamento = aVersamenti.cd_modalita_pagamento
                     And a.pg_banca = aVersamenti.pg_banca
    		     And Exists (Select 1 From vsx_liquidazione_cori v
    				  Where v.pg_call = pgCall
    				    And v.esercizio = a.esercizio
    			            And v.cd_cds = a.cd_cds
    				    And v.cd_unita_organizzativa = a.cd_unita_organizzativa
    				    And v.cd_cds_origine = a.cd_cds_origine
    				    And v.cd_uo_origine = a.cd_uo_origine
    				    And v.cd_gruppo_cr = a.cd_gruppo_cr
    				    And v.cd_regione = a.cd_regione
    				    And v.pg_comune = a.pg_comune
    				    And v.pg_liquidazione = a.pg_liquidazione
    				    And v.pg_liquidazione_origine = a.pg_liquidazione_origine)
                Group by a.cd_gruppo_cr,a.cd_regione,a.pg_comune,a.esercizio,a.cd_cds,a.cd_unita_organizzativa,
                         a.cd_terzo_versamento,a.cd_modalita_pagamento,a.pg_banca,fl_accentrato
                Order by a.cd_gruppo_cr,a.cd_regione,a.pg_comune,a.esercizio,a.cd_cds,a.cd_unita_organizzativa,
                         a.cd_terzo_versamento,a.cd_modalita_pagamento,a.pg_banca,fl_accentrato
 ) Loop -- loop principale aGruppi
--pipe.send_message('Loop aGruppi'); 
--pipe.send_message('aGruppi.cd_gruppo_cr '||aGruppi.cd_gruppo_cr); 
--pipe.send_message('aGruppi.im_liquidato '||aGruppi.im_liquidato);       
      -- Gestione liquidazione gruppi negativi solo da esercizio precedente
      -- DA COMMENTARE SOLO A GENNAIO PER LA LIQ DA ESERCIZIO PRECEDENTE     ---- SI COMMENTA SOLO LA PARTE INTERNA...NON QUELLA CHE EMETTE I MANDATI
--INIZIO PARTE DA COMMENTARE SOLO A GENNAIO

			If aGruppi.im_liquidato < 0 then
        If aLiquid.da_esercizio_precedente = 'N' Or aGruppi.fl_accentrato = 'N' Then
           IBMERR001.RAISE_ERR_GENERICO('Importo di liquidazione negativo o nullo per gruppo CORI: '||aGruppi.cd_gruppo_cr||'.'||aGruppi.cd_regione||'.'||aGruppi.pg_comune);
        End if;
      End if;

--FINE PARTE DA COMMENTARE SOLO A GENNAIO
      
      -- Per i gruppi dei versamenti locali non devo controllare aGruppi.im_liquidato bens? devo fare
      -- la somma da V_LIQUID_CENTRO_UO perch? aGruppi.im_liquidato contiene solo la parte positiva
      -- e se la somma ? negativa il versamento deve essere bloccato
       If aUOVERSACC.cd_unita_organizzativa = aCdUo or aUOVERSCONTOBI.cd_unita_organizzativa = aCdUo Then
         Declare
           totale_negativo    number;
         Begin
           select nvl(sum(im_liquidato),0)
           into totale_negativo
           from  V_LIQUID_CENTRO_UO
           where cd_cds = aCdCds
             and cd_unita_organizzativa = aCdUo
             and esercizio = aEs
             and pg_liquidazione = aPgLiq
             and cd_gruppo_cr = aGruppi.cd_gruppo_cr
             and cd_regione = aGruppi.cd_regione
             and pg_comune = aGruppi.pg_comune
             and im_liquidato < 0
             And Exists (Select 1 From vsx_liquidazione_cori v
    			 Where v.pg_call = pgCall
    			   And v.esercizio = aEs
    			   And v.cd_cds = aCdCds
    			   And v.cd_unita_organizzativa = aCdUo
  			   And v.cd_uo_origine = UOENTE.cd_unita_organizzativa
    			   And v.cd_gruppo_cr = aGruppi.cd_gruppo_cr
    				    And v.cd_regione =  aGruppi.cd_regione
    				    And v.pg_comune = aGruppi.pg_comune
    				    And v.pg_liquidazione = aPgLiq
    				    And v.pg_liquidazione_origine = 0);
--pipe.send_message('aGruppi.im_liquidato + totale_negativo '||To_Char(aGruppi.im_liquidato + totale_negativo)); 

            -- DA COMMENTARE SOLO A GENNAIO PER LA LIQ DA ESERCIZIO PRECEDENTE---- SI COMMENTA SOLO LA PARTE INTERNA...NON QUELLA CHE EMETTE I MANDATI
--INIZIO PARTE DA COMMENTARE SOLO A GENNAIO

      	    If (aGruppi.im_liquidato + totale_negativo) < 0 then
               IBMERR001.RAISE_ERR_GENERICO('Esistono Gruppi Locali negativi che rendono tutta la liquidazione negativa per il gruppo CORI:'||aGruppi.cd_gruppo_cr||'.'||aGruppi.cd_regione||'.'||aGruppi.pg_comune);
               --IBMERR001.RAISE_ERR_GENERICO('Esistono Gruppi Locali negativi che rendono tutta la liquidazione negativa per il gruppo CORI:'||aGruppi.cd_gruppo_cr||'.'||aGruppi.cd_regione||'.'||aGruppi.pg_comune||'/'||totale_negativo||'/'||aGruppi.im_liquidato);
            End if;

--FINE PARTE DA COMMENTARE SOLO A GENNAIO
         End;
       End If;
           
       --
       --UOENTE:=CNRCTB020.GETUOENTE(aEs);
      
       -- Questa parte, relativa alla chiusura delle partite di giro dei compensi va tolta per i CDS caricati via interfaccia
       -- Inizio parte 1 esclusa per liq. di uo appartenenti a cds liquidati via interfaccia

       -- Devo prendere tutte le UO di origine tranne la 999.000    
       For aAggregato in (Select * From V_LIQUID_GRUPPO_CORI_CR_DET a 
     		        Where a.esercizio = aEs
                          And a.cd_cds = aCdCds
                          And a.cd_unita_organizzativa = aCdUo
                          And a.pg_liquidazione = aPgLiq
                          And (-- Proprio
                               a.cd_uo_origine = a.cd_unita_organizzativa
                               -- Non proprio e non presente come proprio
                               Or 
                               (a.cd_uo_origine <> a.cd_unita_organizzativa
                               /* 
                                And Not Exists (Select 1 From vsx_liquidazione_cori v 
                                		Where v.pg_call = pgCall
               			                  And v.esercizio = a.esercizio
               				          And v.cd_cds = a.cd_cds
               			                  And v.cd_unita_organizzativa = a.cd_unita_organizzativa
               				          And v.pg_liquidazione = a.pg_liquidazione
               				          And v.cd_uo_origine = v.cd_unita_organizzativa
                                                  And v.cd_gruppo_cr = a.cd_gruppo_cr
                                                  And v.cd_regione = a.cd_regione
                                                  And v.pg_comune = a.pg_comune)*/
    			                      )
    			                     ) 
    			                And a.cd_uo_origine <> UOENTE.cd_unita_organizzativa     --'999.000'    
    		                  And Exists (Select 1 From vsx_liquidazione_cori v
    				                           Where v.pg_call = pgCall
    				                             And v.esercizio = a.esercizio
    			                               And v.cd_cds = a.cd_cds
    				                             And v.cd_unita_organizzativa = a.cd_unita_organizzativa
    				                             And v.cd_cds_origine = a.cd_cds_origine
    				                             And v.cd_uo_origine = a.cd_uo_origine
    				                             And v.cd_gruppo_cr = a.cd_gruppo_cr
    				                             And v.cd_regione = a.cd_regione
    				                             And v.pg_comune = a.pg_comune
    				                             And v.pg_liquidazione = a.pg_liquidazione
    				                             And v.pg_liquidazione_origine = a.pg_liquidazione_origine)
    			                               And a.cd_gruppo_cr = aGruppi.cd_gruppo_cr
    			                               And a.cd_regione = aGruppi.cd_regione
    			                               And a.pg_comune = aGruppi.pg_comune    			      			  
     ) Loop -- loop 1
		tb_ass_pgiro.delete;
--pipe.send_message('Loop aAggregato'); 
    If not lIsCdsInterDet And Not lIsCdsInterTot then
       -- CICLO SUI CORI DEI MIEI AGGREGATI PER CHIUDERE LE PARTITE DI GIRO APERTE
       -- Questa gestione estrae solo i CORI appartenenti all'UO che liquida
       For aCori in (Select *
       	   	         From contributo_ritenuta a
         	           Where
    		              -- Tolgo la clausola sull'esercizio per estrarre indipendentemente da quello
    		              --    a.esercizio = aEsOrigine
    		              --      a.cd_cds = aAggregato.cd_cds
    		              --  And a.cd_unita_organizzativa = aAggregato.cd_unita_organizzativa
    		                  a.cd_cds = aAggregato.cd_cds_origine
    		              And a.cd_unita_organizzativa = aAggregato.cd_uo_origine
     	  	            And exists (Select 1 From liquid_gruppo_cori_det a1
    			                        Where a1.cd_gruppo_cr = aAggregato.cd_gruppo_cr
    				                      And a1.cd_regione = aAggregato.cd_regione
    				                      And a1.pg_comune = aAggregato.pg_comune
    				                      And a1.cd_cds = aAggregato.cd_cds
    				                      And a1.cd_uo_origine = aAggregato.cd_uo_origine
    				                      And a1.cd_cds_origine = aAggregato.cd_cds_origine
    				                      And a1.cd_unita_organizzativa = aAggregato.cd_unita_organizzativa
    				                      And a1.pg_liquidazione = aAggregato.pg_liquidazione
    				                      And a1.pg_liquidazione_origine = aAggregato.pg_liquidazione
    				                      And a1.esercizio = aAggregato.esercizio
    				                      -- Estrae solo i CORI referenziati in LIQUID_GRUPPO_CORI_DET
    				                      And a1.esercizio_contributo_ritenuta = a.esercizio
    				                      And a1.cd_cds_origine = a.cd_cds
    				                      And a1.cd_uo_origine = a.cd_unita_organizzativa
    				                      And a1.pg_compenso = a.pg_compenso
    				                      And a1.cd_contributo_ritenuta = a.cd_contributo_ritenuta
    				                      And a1.ti_ente_percipiente = a.ti_ente_percipiente)
         		         For update nowait
       ) Loop -- loop2
       	   --Il documento generico deve essere Istituzionale o Commerciale cos? come il compenso
       	   Begin
       	     Select c.ti_istituz_commerc
             Into tipo_ic
             From compenso c
             Where c.cd_cds = aCori.cd_cds
               And c.cd_unita_organizzativa = aCori.cd_unita_organizzativa
               And c.esercizio = aCori.esercizio
               And c.pg_compenso = aCori.pg_compenso;
           Exception
               When Others Then 
                  tipo_ic := 'I';
           End;   

	       -- Gestione dei CORI positivi
	       if aCori.ammontare > 0 then
   	        -- Modifica del 18/11/2002 anche il generico di trasferimento nel caso di CORI accentrati ? targato come GEN_COR_VER_S

	          aTipoGenerico := CNRCTB100.TI_GEN_CORI_VER_SPESA;
--GGGG_TODO VERIFICARE IN CONTRIBUTO_RITENUTA LE DIFFERENZE DI DATI TRA GLI ISTITUTI E LE UO...VERIFICARE ANCHE LE QUERY DEI CURSORI..
		        aAccTmp.cd_cds              := aCori.cd_cds_accertamento;
		        aAccTmp.esercizio           := aCori.esercizio_accertamento;
		        aAccTmp.esercizio_originale := aCori.esercizio_ori_accertamento;
		        aAccTmp.pg_accertamento     := aCori.pg_accertamento;
		        CNRCTB035.getPgiroCds(aAccTmp, aAccScadTmp, aAccScadVoceTmp, aObbTmp, aObbScadTmp, aObbScadVoceTmp);

--/* GG REMMATO SOLO PER IL FINE ANNO 2015
            If aLiquid.da_esercizio_precedente = 'Y' Then

               -- 09.01.2008 remmata questa chiamata, e utilizzata la nuova gestione che ribalta entrambi i documenti
               -- CNRCTB046.ripPgiroCds (aAccTmp, aObb, aTSNow, aUser);
               
               -- 09.01.2008 SF PARTITE DI GIRO DA ESERCIZIO PRECEDENTE
               CNRCTB046.ripPgiroCdsEntrambe(aObbTmp, aObbScadTmp, aObbScadVoceTmp, aAccTmp, aAccScadTmp, aAccScadVoceTmp, Null, Null, CNRCTB001.GESTIONE_SPESE,
                                             aTSNow, aUser, aObb, aAcc);
                    
 		           CNRCTB035.getPgiroCds (aObb, aObbScad, aObbScadVoce, aAcc, aAccScad, aAccScadVoce);

		        Else
		           aObb     := aObbTmp;
		           aObbScad := aObbScadTmp;
		        End if;
--*/ 
--		        aObb     := aObbTmp;
--		        aObbScad := aObbScadTmp;
-- FINE GG

------------------------------------            
            -- se la UO ? quella del versamento su conto BI occorre rendere tronca la PGIRO in oggetto 
            -- e crearla sempre tronca sulla UO di versamento (999.000)
            If aCdUo = aUOVERSCONTOBI.cd_unita_organizzativa 
--GG CONDIZIONE AGGIUNTA PER EVITARE LA PARTITA DI GIRO DALLA 000.407 ALLA 999.000            
            and aUOVERSACC.cd_unita_organizzativa != aUOVERSCONTOBI.cd_unita_organizzativa then
                  --prendo la pgiro duale (e verifico che sia effettivamente chiusa prima di renderla tronca)
                  --aObbOld.cd_cds:=aGruppoCentro.cd_cds_obb_accentr;
                  --aObbOld.esercizio:=aGruppoCentro.esercizio_obb_accentr;
                  --aObbOld.esercizio_originale:=aGruppoCentro.esercizio_ori_obb_accentr;
                  --aObbOld.pg_obbligazione:=aGruppoCentro.pg_obb_accentr;
                  --CNRCTB035.GETPGIROCDSINV(aObbOld,aObbScadOld,aObbScadVoceOld,aAccOld,aAccScadOld,aAccScadVoceOld);
                  --rendo tronca la stessa (cio? annullo la parte spesa)
--/* GG REMMATO SOLO PER IL FINE ANNO 2015
                  If aLiquid.da_esercizio_precedente = 'N' Then
                       CNRCTB043.troncaPraticaAccPgiro(aAccTmp.esercizio,aAccTmp.cd_cds,aAccTmp.esercizio_originale,aAccTmp.pg_accertamento,aTSNow,aUser); 
                  Else  -- rendo tronca quella ribaltata
                       CNRCTB043.troncaPraticaAccPgiroInv(aAcc.esercizio,aAcc.cd_cds,aAcc.esercizio_originale,aAcc.pg_accertamento,aTSNow,aUser); 
                  End if;
--*/                  
--                  CNRCTB043.troncaPraticaAccPgiro(aAccTmp.esercizio,aAccTmp.cd_cds,aAccTmp.esercizio_originale,aAccTmp.pg_accertamento,aTSNow,aUser); 
-- FINE GG
                  -- devo creare la pgiro tronca sulla 999
                  aObbNew:=null;
                  aObbScadNew:=null;
                  aAccNew:=null;
                  aAccScadNew:=null;
                  --determino il capitolo da mettere sulla pgiro
                  Begin
                     Select distinct a.cd_elemento_voce
                     Into aCdEV
                     From ass_tipo_cori_ev a, tipo_cr_base b
                     Where b.esercizio = aEs   --aCori.esercizio
                       And b.cd_gruppo_cr = aAggregato.cd_gruppo_cr
                       And a.cd_contributo_ritenuta = b.cd_contributo_ritenuta
                       And a.esercizio = aEs   --aCori.esercizio
                       And a.ti_gestione = CNRCTB001.GESTIONE_SPESE
                       And a.ti_appartenenza = CNRCTB001.APPARTENENZA_CDS;
                  Exception 
                          when TOO_MANY_ROWS then
                          	   IBMERR001.RAISE_ERR_GENERICO('Esiste pi? di un conto finanziario associato a CORI del gruppo '||aAggregato.cd_gruppo_cr);
                          when NO_DATA_FOUND then
                               IBMERR001.RAISE_ERR_GENERICO('Conto finanziario associato a CORI del gruppo '||aAggregato.cd_gruppo_cr||' non trovato');
                  End;
                  Begin
                     Select *
                     Into elementoVoce
                     From elemento_voce
                     Where esercizio = aEs      --aCori.esercizio
                       And ti_appartenenza = CNRCTB001.APPARTENENZA_CDS
                       And ti_gestione = CNRCTB001.GESTIONE_SPESE
                       And cd_elemento_voce = aCdEV;
                  Exception 
                          when NO_DATA_FOUND then
                         	   IBMERR001.RAISE_ERR_GENERICO('Codice voce '||aCdEV||' associata a CORI del gruppo '||aAggregato.cd_gruppo_cr||' non esistente');
                  End;
                  
                  aObbNew.CD_CDS:=aCdCds;
                  aObbNew.ESERCIZIO:=aObb.esercizio;
                  aObbNew.ESERCIZIO_ORIGINALE:=aObb.esercizio_originale;
--/* GG REMMATO SOLO PER IL FINE ANNO 2015
                  If aLiquid.da_esercizio_precedente = 'N' Then
                     aObbNew.CD_TIPO_DOCUMENTO_CONT:=CNRCTB018.TI_DOC_IMP;
                  Else
                     aObbNew.CD_TIPO_DOCUMENTO_CONT:=CNRCTB018.TI_DOC_IMP_RES;
                  End If;   
--*/                  
--                  aObbNew.CD_TIPO_DOCUMENTO_CONT:=CNRCTB018.TI_DOC_IMP;
-- FINE GG
                  aObbNew.CD_UNITA_ORGANIZZATIVA:=aCdUo;
                  aObbNew.CD_CDS_ORIGINE:=aCdCds;
                  aObbNew.CD_UO_ORIGINE:=aCdUo;
                  aObbNew.TI_APPARTENENZA:=elementoVoce.ti_appartenenza;
                  aObbNew.TI_GESTIONE:=elementoVoce.ti_gestione;
                  aObbNew.CD_ELEMENTO_VOCE:=elementoVoce.cd_elemento_voce;
                  aObbNew.DT_REGISTRAZIONE:=TRUNC(trunc(aTSNow));
                  aObbNew.DS_OBBLIGAZIONE:='PGiro creata in automatico da liquidazione CORI';
                  aObbNew.NOTE_OBBLIGAZIONE:='';
                  aObbNew.CD_TERZO:=aObb.CD_TERZO;
                  aObbNew.IM_OBBLIGAZIONE:=aObb.IM_OBBLIGAZIONE;    --aObbOld.IM_OBBLIGAZIONE ? stata gi? annullata
                  aObbNew.stato_obbligazione:=CNRCTB035.STATO_DEFINITIVO;
                  aObbNew.im_costi_anticipati:=0;
                  aObbNew.fl_calcolo_automatico:='N';
                  aObbNew.fl_spese_costi_altrui:='N';
                  aObbNew.FL_PGIRO:='Y';
                  aObbNew.RIPORTATO:='N';
                  aObbNew.DACR:=aTSNow;
                  aObbNew.UTCR:=aUser;
                  aObbNew.DUVA:=aTSNow;
                  aObbNew.UTUV:=aUser;
                  aObbNew.PG_VER_REC:=1;
                  aObbNew.ESERCIZIO_COMPETENZA:=aEs;
                
                  CNRCTB030.CREAOBBLIGAZIONEPGIROTRONC(false,aObbNew,aObbScadNew,aAccNew,aAccScadNew,trunc(aTSNow));
 
                  CREALIQUIDCORIASSPGIRO(aLiquid,aAggregato.cd_gruppo_cr,aAggregato.cd_regione,aAggregato.pg_comune,'S',aObbNew,aObb,null,null,aUser,trunc(aTSNow));
                  
                  
                  aObb     := aObbNew;
		              aObbScad := aObbScadNew;
            End If;
            
-------------------------------------            
---INIZIO GGG
						IF recParametriCNR.FL_TESORERIA_UNICA = 'Y' and aAggregato.cd_unita_organizzativa <> aUOVERSACC.cd_unita_organizzativa and
         				aAggregato.cd_unita_organizzativa <> aUOVERSCONTOBI.cd_unita_organizzativa and aAggregato.fl_accentrato = 'Y' THEN
							If aLiquid.da_esercizio_precedente = 'N' Then
                CNRCTB043.troncaPraticaAccPgiro(aCori.esercizio_accertamento,aCori.cd_cds_accertamento,aCori.esercizio_ori_accertamento,aCori.pg_accertamento,aTSNow,aUser); 
              Else  -- rendo tronca quella ribaltata
-- GGG TODO_DA_VERIFICARE	COSA PASSARE...DOVREBBE ESSERE OK...CHIEDERE A TILDE
                CNRCTB043.troncaPraticaAccPgiroInv(aAcc.esercizio,aAcc.cd_cds,aAcc.esercizio_oriGINALE,aAcc.pg_accertamento,aTSNow,aUser); 
              End if;
              ind_pGiro := Nvl(tb_ass_pgiro.Count,0) + 1;
							tb_ass_pgiro(ind_pGiro).cd_cds := aCori.cd_cds;
							tb_ass_pgiro(ind_pGiro).cd_uo := aCori.cd_unita_organizzativa;
							tb_ass_pgiro(ind_pGiro).cd_cds_orig := aCori.cd_cds;
							tb_ass_pgiro(ind_pGiro).cd_uo_orig := aCori.cd_unita_organizzativa;
							tb_ass_pgiro(ind_pGiro).esercizio := aEs;
							tb_ass_pgiro(ind_pGiro).es_compenso := aCori.esercizio;
							tb_ass_pgiro(ind_pGiro).pg_compenso := aCori.pg_compenso;
							tb_ass_pgiro(ind_pGiro).pg_liq := aPgLiq;
							tb_ass_pgiro(ind_pGiro).pg_liq_orig := aPgLiq;
							tb_ass_pgiro(ind_pGiro).cd_gr_cr := aAggregato.cd_gruppo_cr;
							tb_ass_pgiro(ind_pGiro).cd_regione := aAggregato.cd_regione;
							tb_ass_pgiro(ind_pGiro).pg_comune := aAggregato.pg_comune;
							tb_ass_pgiro(ind_pGiro).cd_cori := aCori.cd_contributo_ritenuta;
							tb_ass_pgiro(ind_pGiro).ti_en_per := aCori.ti_ente_percipiente;
							tb_ass_pgiro(ind_pGiro).ti_origine := 'E';
							tb_ass_pgiro(ind_pGiro).es_acc := aCori.esercizio_accertamento;
							tb_ass_pgiro(ind_pGiro).es_ori_acc := aCori.esercizio_ori_accertamento;
							tb_ass_pgiro(ind_pGiro).cds_acc := aCori.cd_cds_accertamento;
							tb_ass_pgiro(ind_pGiro).pg_acc := aCori.pg_accertamento;
						ELSE
	  	      	aGen:=null;
	  	      	aGenRiga:=null;
         	  	aListGenRighe.delete;
	          	-- Creo il documento generico di spesa su partita di giro collegato all'annotazione di entrata su pgiro del contributo ritenuta
	          	aGen.CD_CDS:=aObb.cd_cds;
	          	aGen.CD_UNITA_ORGANIZZATIVA:=aObb.cd_unita_organizzativa;
	          	aGen.ESERCIZIO:=aObb.esercizio;
	          	aGen.CD_CDS_ORIGINE:=aObb.cd_cds;
	          	aGen.CD_UO_ORIGINE:=aObb.cd_unita_organizzativa;
	          	aGen.CD_TIPO_DOCUMENTO_AMM:=aTipoGenerico;
	          	aGen.DATA_REGISTRAZIONE:=TRUNC(aDateCont);
	          	aGen.DS_DOCUMENTO_GENERICO:='CORI-D cn.'||aCori.pg_compenso||' '||aCori.cd_contributo_ritenuta;
	          	--aGen.TI_ISTITUZ_COMMERC:=CNRCTB100.TI_ISTITUZIONALE;
	          	aGen.TI_ISTITUZ_COMMERC:=tipo_ic;
	          	aGen.IM_TOTALE:=aObb.im_obbligazione;
	          	aGen.STATO_COFI:=CNRCTB100.STATO_GEN_COFI_TOT_MR;
	          	aGen.STATO_COGE:=CNRCTB100.STATO_COEP_EXC;
	          	aGen.STATO_COAN:=CNRCTB100.STATO_COEP_EXC;
	          	--Massimo Iaccarino Inizio
	          	aGen.CD_DIVISA:=aDivisaEuro;
	          	aGen.CAMBIO:=1;
	          	--Massimo Iaccarino Fine
	          	--  aGen.ESERCIZIO_LETTERA:=0;
	          	--  aGen.PG_LETTERA:=0;
	          	aGen.DACR:=aTSNow;
	          	aGen.UTCR:=aUser;
	          	aGen.DUVA:=aTSNow;
	          	aGen.UTUV:=aUser;
	          	aGen.PG_VER_REC:=1;
	          	aGen.DT_SCADENZA:=TRUNC(aTSNow);
	          	aGen.STATO_PAGAMENTO_FONDO_ECO:=CNRCTB100.STATO_NO_PFONDOECO;
	          	aGen.TI_ASSOCIATO_MANREV:=CNRCTB100.TI_ASSOC_TOT_MAN_REV ;
		        	aGen.DT_DA_COMPETENZA_COGE:=TRUNC(aDateCont);
	          	aGen.DT_A_COMPETENZA_COGE:=TRUNC(aDateCont);
            	
		        	aGenRiga.DT_DA_COMPETENZA_COGE:=aGen.DT_DA_COMPETENZA_COGE;
	          	aGenRiga.DT_A_COMPETENZA_COGE:=aGen.DT_A_COMPETENZA_COGE;
	          	--
		        	aGenRiga.CD_CDS:=aGen.CD_CDS;
	          	aGenRiga.CD_UNITA_ORGANIZZATIVA:=aGen.CD_UNITA_ORGANIZZATIVA;
	          	aGenRiga.ESERCIZIO:=aGen.ESERCIZIO;
	          	aGenRiga.CD_TIPO_DOCUMENTO_AMM:=aGen.CD_TIPO_DOCUMENTO_AMM;
	          	aGenRiga.DS_RIGA:=aGen.DS_DOCUMENTO_GENERICO;
	          	aGenRiga.IM_RIGA_DIVISA:=aGen.IM_TOTALE;
	          	aGenRiga.IM_RIGA:=aGen.IM_TOTALE;
		        	-- Utilizzo delle corrette informazioni di pagamento nel caso di accentramento o non accentramento
	          	if aAggregato.fl_accentrato = 'Y' then
	          	  aGenRiga.CD_TERZO:=aCdTerzoAcc;
	          	  aGenRiga.CD_MODALITA_PAG:=aCdModPagAcc;
	          	  aGenRiga.PG_BANCA:=aPgBancaAcc;
	          	else
	          	  aGenRiga.CD_TERZO:=aAggregato.cd_terzo_versamento;
	          	  aGenRiga.CD_MODALITA_PAG:=aAggregato.CD_MODALITA_PAGAMENTO;
	          	  aGenRiga.PG_BANCA:=aAggregato.PG_BANCA;
	          	end if;
	          	--   aGenRiga.CD_TERZO_CESSIONARIO:=aGen.CD_TERZO_CESSIONARIO;
	          	--   aGenRiga.CD_TERZO_UO_CDS:=aGen.CD_TERZO_UO_CDS;
		        	aGenRiga.RAGIONE_SOCIALE:=aAnagTst.RAGIONE_SOCIALE;
	          	aGenRiga.NOME:=aAnagTst.NOME;
	          	aGenRiga.COGNOME:=aAnagTst.COGNOME;
	          	aGenRiga.CODICE_FISCALE:=aAnagTst.CODICE_FISCALE;
	          	aGenRiga.PARTITA_IVA:=aAnagTst.PARTITA_IVA;
	          	--   aGenRiga.CD_TERMINI_PAG:=aCompenso.CD_TERMINI_PAG;
	          	--   aGenRiga.CD_TERMINI_PAG_UO_CDS:=aCompenso.CD_TERMINI_PAG_UO_CDS;
	          	--   aGenRiga.PG_BANCA_UO_CDS:=aGen.PG_BANCA_UO_CDS;
	          	--   aGenRiga.CD_MODALITA_PAG_UO_CDS:=aGen.CD_MODALITA_PAG_UO_CDS;
	          	--   aGenRiga.NOTE:=aGen.NOTE;
	          	aGenRiga.STATO_COFI:=aGen.STATO_COFI;
	          	--   aGenRiga.DT_CANCELLAZIONE:=aGen.DT_CANCELLAZIONE;
	          	--   aGenRiga.CD_CDS_OBBLIGAZIONE:=aGen.CD_CDS_OBBLIGAZIONE;
	          	--   aGenRiga.ESERCIZIO_OBBLIGAZIONE:=aGen.ESERCIZIO_OBBLIGAZIONE;
	          	--   aGenRiga.ESERCIZIO_ORI_OBBLIGAZIONE:=aGen.ESERCIZIO_ORI_OBBLIGAZIONE;
	          	--   aGenRiga.PG_OBBLIGAZIONE:=aGen.PG_OBBLIGAZIONE;
	          	--   aGenRiga.PG_OBBLIGAZIONE_SCADENZARIO:=aGen.PG_OBBLIGAZIONE_SCADENZARIO;
	          	aGenRiga.CD_CDS_OBBLIGAZIONE:=aObb.CD_CDS;
	          	aGenRiga.ESERCIZIO_OBBLIGAZIONE:=aObb.ESERCIZIO;
	          	aGenRiga.ESERCIZIO_ORI_OBBLIGAZIONE:=aObb.ESERCIZIO_ORIGINALE;
	          	aGenRiga.PG_OBBLIGAZIONE:=aObb.PG_OBBLIGAZIONE;
	          	aGenRiga.PG_OBBLIGAZIONE_SCADENZARIO:=1;
	          	aGenRiga.DACR:=aGen.DACR;
	          	aGenRiga.UTCR:=aGen.UTCR;
	          	aGenRiga.UTUV:=aGen.UTUV;
	          	aGenRiga.DUVA:=aGen.DUVA;
	          	aGenRiga.PG_VER_REC:=aGen.PG_VER_REC;
	          	aGenRiga.TI_ASSOCIATO_MANREV:=aGen.TI_ASSOCIATO_MANREV;
	          	aListGenRighe(1):=aGenRiga;
	          	--
		        	CNRCTB110.CREAGENERICOAGGOBBACC(aGen,aListGenRighe);
            	
		        	-- Generazione righe mandato
	          	aManPRiga:=null;
	          	aManPRiga.CD_CDS:=aGen.cd_cds;
	          	aManPRiga.ESERCIZIO:=aGen.esercizio;
	          	aManPRiga.ESERCIZIO_OBBLIGAZIONE:=aGenRiga.esercizio_obbligazione;
	          	aManPRiga.ESERCIZIO_ORI_OBBLIGAZIONE:=aGenRiga.esercizio_ori_obbligazione;
	          	aManPRiga.PG_OBBLIGAZIONE:=aGenRiga.pg_obbligazione;
	          	aManPRiga.PG_OBBLIGAZIONE_SCADENZARIO:=aGenRiga.pg_obbligazione_scadenzario;
	          	aManPRiga.CD_CDS_DOC_AMM:=aGen.cd_cds;
	          	aManPRiga.CD_UO_DOC_AMM:=aGen.cd_unita_organizzativa;
	          	aManPRiga.ESERCIZIO_DOC_AMM:=aGen.esercizio;
	          	aManPRiga.CD_TIPO_DOCUMENTO_AMM:=aGen.cd_tipo_documento_amm;
		        	aManPRiga.PG_DOC_AMM:=aGen.pg_documento_generico;
	          	aManPRiga.DS_MANDATO_RIGA:=aManP.ds_mandato;
	          	aManPRiga.STATO:=aManP.stato;
	          	aManPRiga.CD_TERZO:=aGenRiga.cd_terzo;
	          	aManPRiga.PG_BANCA:=aGenRiga.pg_banca;
	          	aManPRiga.CD_MODALITA_PAG:=aGenRiga.cd_modalita_pag;
	          	aManPRiga.IM_MANDATO_RIGA:=aObb.im_obbligazione;
	          	aManPRiga.IM_RITENUTE_RIGA:=0;
	          	aManPRiga.FL_PGIRO:='Y';
	          	aManPRiga.UTCR:=aUser;
	          	aManPRiga.DACR:=aTSNow;
	          	aManPRiga.UTUV:=aUser;
	          	aManPRiga.DUVA:=aTSNow;
	          	aManPRiga.PG_VER_REC:=1;
	          	aTotMandato:=aTotMandato+aManPRiga.im_mandato_riga;
	          	aListRigheManP(aListRigheManP.count+1):=aManPRiga;
	          END IF;
---FINE GGG
	       -- ================================================================
			   -- Se il CORI ? negativo
	       -- ================================================================
	       elsif aCori.ammontare < 0 Then
		        -- Modifica del 18/11/2002 anche il generico di trasferimento nel caso di CORI accentrati ? targato come GEN_COR_VER_E
	          --if aAggregato.fl_accentrato = 'Y' then
	          --	aTipoGenerico := CNRCTB100.TI_GENERICO_TRASF_E;
	          -- else
	          aTipoGenerico := CNRCTB100.TI_GEN_CORI_VER_ENTRATA;
	          -- end if;
		        aObbTmp.cd_cds:=aCori.cd_cds_obbligazione;
		        aObbTmp.esercizio:=aCori.esercizio_obbligazione;
		        aObbTmp.esercizio_originale:=aCori.esercizio_ori_obbligazione;
		        aObbTmp.pg_obbligazione:=aCori.pg_obbligazione;
-- recupero l'accertamento collegato alla partita di giro di spesa.		        
		        CNRCTB035.getPgiroCds(aObbTmp,aObbScadTmp,aObbScadVoceTmp,aAccTmp,aAccScadTmp,aAccScadVoceTmp);
--/* GG REMMATO SOLO PER IL FINE ANNO 2015
            If aLiquid.da_esercizio_precedente = 'Y' Then

               -- 09.01.2008 remmata questa chiamata, e utilizzata la nuova gestione che ribalta entrambi i documenti
		           -- CNRCTB046.ripPgiroCds(aObbTmp,aAcc,aTSNow,aUser);
                 
               -- 09.01.2008 SF PARTITE DI GIRO DA ESERCIZIO PRECEDENTE
               CNRCTB046.ripPgiroCdsEntrambe(aObbTmp, aObbScadTmp, aObbScadVoceTmp, aAccTmp, aAccScadTmp, aAccScadVoceTmp, Null, Null, CNRCTB001.GESTIONE_ENTRATE,
                                             aTSNow, aUser, aObb, aAcc);

 		           CNRCTB035.getPgiroCds(aAcc,aAccScad,aAccScadVoce,aObb,aObbScad,aObbScadVoce);
	          Else
		           aAcc     := aAccTmp;
		           aAccScad := aAccScadTmp;
		        End If;
--*/
--		           aAcc     := aAccTmp;
--		           aAccScad := aAccScadTmp;
-- FINE GG
------------------------------------            
            -- se la UO ? quella del versamento su conto BI occorre rendere tronca la PGIRO in oggetto 
            -- e crearla sempre tronca sulla UO di versamento (999.000)
            If aCdUo = aUOVERSCONTOBI.cd_unita_organizzativa 
--GG CONDIZIONE AGGIUNTA PER EVITARE LA PARTITA DI GIRO DALLA 000.407 ALLA 999.000            
            and aUOVERSACC.cd_unita_organizzativa != aUOVERSCONTOBI.cd_unita_organizzativa then
                  --prendo la pgiro duale (e verifico che sia effettivamente chiusa prima di renderla tronca)
                  --aObbOld.cd_cds:=aGruppoCentro.cd_cds_obb_accentr;
                  --aObbOld.esercizio:=aGruppoCentro.esercizio_obb_accentr;
                  --aObbOld.esercizio_originale:=aGruppoCentro.esercizio_ori_obb_accentr;
                  --aObbOld.pg_obbligazione:=aGruppoCentro.pg_obb_accentr;
                  --CNRCTB035.GETPGIROCDSINV(aObbOld,aObbScadOld,aObbScadVoceOld,aAccOld,aAccScadOld,aAccScadVoceOld);
                  --rendo tronca la stessa (cio? annullo la parte entrata)
--/* GG REMMATO SOLO PER IL FINE ANNO 2015
							    If aLiquid.da_esercizio_precedente = 'N' Then
                       CNRCTB043.troncaPraticaObbPgiro(aObbTmp.esercizio,aObbTmp.cd_cds,aObbTmp.esercizio_originale,aObbTmp.pg_obbligazione,aTSNow,aUser); 
                  Else  -- rendo tronca quella ribaltata
                       CNRCTB043.troncaPraticaObbPgiroInv(aObb.esercizio,aObb.cd_cds,aObb.esercizio_originale,aObb.pg_obbligazione,aTSNow,aUser); 
                  End if;
--*/
--                       CNRCTB043.troncaPraticaObbPgiro(aObbTmp.esercizio,aObbTmp.cd_cds,aObbTmp.esercizio_originale,aObbTmp.pg_obbligazione,aTSNow,aUser); 
-- FINE GG
                  -- devo creare la pgiro tronca sulla 999
                  aAccNew:=null;
                  aAccScadNew:=null;
                  aObbNew:=null;
                  aObbScadNew:=null;
                  --determino il capitolo da mettere sulla pgiro
                  Begin
                     Select distinct a.cd_elemento_voce
                     Into aCdEV
                     From ass_tipo_cori_ev a, tipo_cr_base b
                     Where b.esercizio = aEs   --aCori.esercizio
                       And b.cd_gruppo_cr = aAggregato.cd_gruppo_cr
                       And a.cd_contributo_ritenuta = b.cd_contributo_ritenuta
                       And a.esercizio = aEs   --aCori.esercizio
                       And a.ti_gestione = CNRCTB001.GESTIONE_ENTRATE
                       And a.ti_appartenenza = CNRCTB001.APPARTENENZA_CNR;
                  Exception 
                          when TOO_MANY_ROWS then
                          	   IBMERR001.RAISE_ERR_GENERICO('Esiste pi? di un conto finanziario associato a CORI del gruppo '||aAggregato.cd_gruppo_cr);
                          when NO_DATA_FOUND then
                              IBMERR001.RAISE_ERR_GENERICO('Conto finanziario associato a CORI del gruppo '||aAggregato.cd_gruppo_cr||' non trovato');
                  End;
                  Begin
                     Select *
                     Into elementoVoce
                     From elemento_voce
                     Where esercizio = aEs   --aCori.esercizio
                       And ti_appartenenza = CNRCTB001.APPARTENENZA_CNR
                       And ti_gestione = CNRCTB001.GESTIONE_ENTRATE
                       And cd_elemento_voce = aCdEV;
                  Exception 
                          when NO_DATA_FOUND then
                         	   IBMERR001.RAISE_ERR_GENERICO('Codice voce '||aCdEV||' associata a CORI del gruppo '||aAggregato.cd_gruppo_cr||' non esistente');
                  End;
                  
                  aAccNew.CD_CDS:=aCdCds;
                  aAccNew.ESERCIZIO:=aAcc.esercizio;
                  aAccNew.ESERCIZIO_ORIGINALE:=aAcc.esercizio_originale;
--/* GG REMMATO SOLO PER IL FINE ANNO 2015
                  If aLiquid.da_esercizio_precedente = 'N' Then
                       aAccNew.CD_TIPO_DOCUMENTO_CONT:=CNRCTB018.TI_DOC_ACC;
                  Else     
                       aAccNew.CD_TIPO_DOCUMENTO_CONT:=CNRCTB018.TI_DOC_ACC_RES;
                  End If;
--*/
--                  aAccNew.CD_TIPO_DOCUMENTO_CONT:=CNRCTB018.TI_DOC_ACC;
-- FINE GG
                  aAccNew.CD_UNITA_ORGANIZZATIVA:=aCdUo;
                  aAccNew.CD_CDS_ORIGINE:=aCdCds;
                  aAccNew.CD_UO_ORIGINE:=aCdUo;
                  aAccNew.TI_APPARTENENZA:=elementoVoce.ti_appartenenza;
                  aAccNew.TI_GESTIONE:=elementoVoce.ti_gestione;
                  aAccNew.CD_ELEMENTO_VOCE:=elementoVoce.cd_elemento_voce;
                  aAccNew.CD_VOCE:=elementoVoce.cd_elemento_voce;
                  aAccNew.DT_REGISTRAZIONE:=TRUNC(trunc(aTSNow));
                  aAccNew.DS_ACCERTAMENTO:='PGiro creata in automatico da liquidazione CORI';
                  aAccNew.NOTE_ACCERTAMENTO:='';
                  aAccNew.CD_TERZO:=aAcc.CD_TERZO;
                  aAccNew.IM_ACCERTAMENTO:=aAcc.IM_ACCERTAMENTO;    
                  aAccNew.FL_PGIRO:='Y';
                  aAccNew.RIPORTATO:='N';
                  aAccNew.DACR:=aTSNow;
                  aAccNew.UTCR:=aUser;
                  aAccNew.DUVA:=aTSNow;
                  aAccNew.UTUV:=aUser;
                  aAccNew.PG_VER_REC:=1;
                  aAccNew.ESERCIZIO_COMPETENZA:=aEs;
                
                  CNRCTB040.CREAACCERTAMENTOPGIROTRONC(false,aAccNew,aAccScadNew,aObbNew,aObbScadNew,trunc(aTSNow));

                  CREALIQUIDCORIASSPGIRO(aLiquid,aAggregato.cd_gruppo_cr,aAggregato.cd_regione,aAggregato.pg_comune,'E',null,null,aAccNew,aAcc,aUser,trunc(aTSNow));
                  
                  aAcc     := aAccNew;
		              aAccScad := aAccScadNew;
            End If;
            
-------------------------------------            
---INIZIO GGG
						IF recParametriCNR.FL_TESORERIA_UNICA = 'Y' and aAggregato.cd_unita_organizzativa <> aUOVERSACC.cd_unita_organizzativa and
         				aAggregato.cd_unita_organizzativa <> aUOVERSCONTOBI.cd_unita_organizzativa and aAggregato.fl_accentrato = 'Y' THEN
						-- rendo tronca LA PARTITA DI GIRO DELL'ISTITUTO
							If aLiquid.da_esercizio_precedente = 'N' Then
                CNRCTB043.troncaPraticaObbPgiro(aCori.esercizio_obbligazione,aCori.cd_cds,aCori.esercizio_ori_obbligazione,aCori.pg_obbligazione,aTSNow,aUser); 
              Else  -- rendo tronca quella ribaltata
-- GGG TODO_DA_VERIFICARE	COSA PASSARE...DOVREBBE ESSERE OK...CHIEDERE A TILDE
                CNRCTB043.troncaPraticaObbPgiroInv(aObb.esercizio,aObb.cd_cds,aObb.esercizio_originale,aObb.pg_obbligazione,aTSNow,aUser); 
              End if;
              ind_pGiro := Nvl(tb_ass_pgiro.Count,0) + 1;
							tb_ass_pgiro(ind_pGiro).cd_cds := aCori.cd_cds;
							tb_ass_pgiro(ind_pGiro).cd_uo := aCori.cd_unita_organizzativa;
							tb_ass_pgiro(ind_pGiro).cd_cds_orig := aCori.cd_cds;
							tb_ass_pgiro(ind_pGiro).cd_uo_orig := aCori.cd_unita_organizzativa;
							tb_ass_pgiro(ind_pGiro).esercizio := aEs;
							tb_ass_pgiro(ind_pGiro).es_compenso := aCori.esercizio;
							tb_ass_pgiro(ind_pGiro).pg_compenso := aCori.pg_compenso;
							tb_ass_pgiro(ind_pGiro).pg_liq := aPgLiq;
							tb_ass_pgiro(ind_pGiro).pg_liq_orig := aPgLiq;
							tb_ass_pgiro(ind_pGiro).cd_gr_cr := aAggregato.cd_gruppo_cr;
							tb_ass_pgiro(ind_pGiro).cd_regione := aAggregato.cd_regione;
							tb_ass_pgiro(ind_pGiro).pg_comune := aAggregato.pg_comune;
							tb_ass_pgiro(ind_pGiro).cd_cori := aCori.cd_contributo_ritenuta;
							tb_ass_pgiro(ind_pGiro).ti_en_per := aCori.ti_ente_percipiente;
							tb_ass_pgiro(ind_pGiro).ti_origine := 'S';
							tb_ass_pgiro(ind_pGiro).es_obb := aCori.esercizio_obbligazione;
							tb_ass_pgiro(ind_pGiro).es_ori_obb := aCori.esercizio_ori_obbligazione;
							tb_ass_pgiro(ind_pGiro).cds_obb := aCori.cd_cds_obbligazione;
							tb_ass_pgiro(ind_pGiro).pg_obb := aCori.pg_obbligazione;
						ELSE
	  	      	aGen:=null;
	  	      	aGenRiga:=null;
           		aListGenRighe.delete;
            	
	          	-- Creo il documento generico di entrata su partita di giro collegato all'annotazione di spesa su pgiro del contributo ritenuta
	          	aGen.CD_CDS:=aAcc.cd_cds;
	          	aGen.CD_UNITA_ORGANIZZATIVA:=aAcc.cd_unita_organizzativa;
	          	aGen.ESERCIZIO:=aAcc.esercizio;
	          	aGen.CD_CDS_ORIGINE:=aAcc.cd_cds;
	          	aGen.CD_UO_ORIGINE:=aAcc.cd_unita_organizzativa;
	          	aGen.CD_TIPO_DOCUMENTO_AMM:=aTipoGenerico;
	          	aGen.DATA_REGISTRAZIONE:=TRUNC(aDateCont);
	          	aGen.DS_DOCUMENTO_GENERICO:='CORI-D cn.'||aCori.pg_compenso||' '||aCori.cd_contributo_ritenuta;
	          	aGen.TI_ISTITUZ_COMMERC:=tipo_ic;
	          	aGen.IM_TOTALE:=aAcc.im_accertamento;
	          	aGen.STATO_COFI:=CNRCTB100.STATO_GEN_COFI_TOT_MR;
	          	aGen.STATO_COGE:=CNRCTB100.STATO_COEP_EXC;
	          	aGen.STATO_COAN:=CNRCTB100.STATO_COEP_EXC;
	          	aGen.CD_DIVISA:=aDivisaEuro;
	          	aGen.CAMBIO:=1;
	          	aGen.DACR:=aTSNow;
	          	aGen.UTCR:=aUser;
	          	aGen.DUVA:=aTSNow;
	          	aGen.UTUV:=aUser;
	          	aGen.PG_VER_REC:=1;
	          	aGen.DT_SCADENZA:=TRUNC(aTSNow);
	          	aGen.STATO_PAGAMENTO_FONDO_ECO:=CNRCTB100.STATO_NO_PFONDOECO;
	          	aGen.TI_ASSOCIATO_MANREV:=CNRCTB100.TI_ASSOC_TOT_MAN_REV ;
            	aGen.DT_DA_COMPETENZA_COGE:=TRUNC(aDateCont);
            	aGen.DT_A_COMPETENZA_COGE:=TRUNC(aDateCont);
            	
            	aGenRiga.DT_DA_COMPETENZA_COGE:=aGen.DT_DA_COMPETENZA_COGE;
            	aGenRiga.DT_A_COMPETENZA_COGE:=aGen.DT_A_COMPETENZA_COGE;
		        	aGenRiga.CD_CDS:=aGen.CD_CDS;
	          	aGenRiga.CD_UNITA_ORGANIZZATIVA:=aGen.CD_UNITA_ORGANIZZATIVA;
	          	aGenRiga.ESERCIZIO:=aGen.ESERCIZIO;
	          	aGenRiga.CD_TIPO_DOCUMENTO_AMM:=aGen.CD_TIPO_DOCUMENTO_AMM;
	          	aGenRiga.DS_RIGA:=aGen.DS_DOCUMENTO_GENERICO;
	          	aGenRiga.IM_RIGA_DIVISA:=aGen.IM_TOTALE;
	          	aGenRiga.IM_RIGA:=aGen.IM_TOTALE;
		        	-- Utilizzo delle corrette informazioni di pagamento nel caso di accentramento o non accentramento
	          	if aAggregato.fl_accentrato = 'Y' then
	          	    aGenRiga.CD_TERZO:=aCdTerzoAcc;
		        	else
	          	    aGenRiga.CD_TERZO:=aAggregato.cd_terzo_versamento;
		        	end if;
	          	--   aGenRiga.CD_TERZO_CESSIONARIO:=aGen.CD_TERZO_CESSIONARIO;
	          	-- Estrae il terzo associato all'UO del compenso e le sue modalit? di pagamento di tipo bancario pi? recenti
	          	-- se entro dalla 999 estraggo il terzo associato ad essa con modalit? di pagamento BI
	          	IF CONTO_BI = 'Y' THEN
	          	   CNRCTB080.getTerzoPerEnteContoBI(aCdUo, aCdTerzoUO, aCdModPagUO, aPgBancaUO);
	          	Else
    	      	   CNRCTB080.getTerzoPerUO(aCori.cd_unita_organizzativa, aCdTerzoUO, aCdModPagUO, aPgBancaUO,aAcc.esercizio);
    	      	End If;   
			      	--
	          	aGenRiga.CD_TERZO_UO_CDS:=aCdTerzoUO;
	          	aGenRiga.PG_BANCA_UO_CDS:=aPgBancaUO;
	          	aGenRiga.CD_MODALITA_PAG_UO_CDS:=aCdModPagUO;
	 	        	aGenRiga.RAGIONE_SOCIALE:=aAnagTst.RAGIONE_SOCIALE;
	          	aGenRiga.NOME:=aAnagTst.NOME;
	          	aGenRiga.COGNOME:=aAnagTst.COGNOME;
	          	aGenRiga.CODICE_FISCALE:=aAnagTst.CODICE_FISCALE;
	          	aGenRiga.PARTITA_IVA:=aAnagTst.PARTITA_IVA;
	          	--   aGenRiga.CD_TERMINI_PAG:=aCompenso.CD_TERMINI_PAG;
	          	--   aGenRiga.CD_TERMINI_PAG_UO_CDS:=aCompenso.CD_TERMINI_PAG_UO_CDS;
	          	--   aGenRiga.NOTE:=aGen.NOTE;
	          	aGenRiga.STATO_COFI:=aGen.STATO_COFI;
	          	--   aGenRiga.DT_CANCELLAZIONE:=aGen.DT_CANCELLAZIONE;
	          	--   aGenRiga.CD_CDS_OBBLIGAZIONE:=aGen.CD_CDS_OBBLIGAZIONE;
	          	--   aGenRiga.ESERCIZIO_OBBLIGAZIONE:=aGen.ESERCIZIO_OBBLIGAZIONE;
	          	--   aGenRiga.ESERCIZIO_ORI_OBBLIGAZIONE:=aGen.ESERCIZIO_ORI_OBBLIGAZIONE;
	          	--   aGenRiga.PG_OBBLIGAZIONE:=aGen.PG_OBBLIGAZIONE;
	          	--   aGenRiga.PG_OBBLIGAZIONE_SCADENZARIO:=aGen.PG_OBBLIGAZIONE_SCADENZARIO;
	          	aGenRiga.CD_CDS_ACCERTAMENTO:=aAcc.CD_CDS;
	          	aGenRiga.ESERCIZIO_ACCERTAMENTO:=aAcc.ESERCIZIO;
	          	aGenRiga.ESERCIZIO_ORI_ACCERTAMENTO:=aAcc.ESERCIZIO_ORIGINALE;
	          	aGenRiga.PG_ACCERTAMENTO:=aAcc.PG_ACCERTAMENTO;
	          	aGenRiga.PG_ACCERTAMENTO_SCADENZARIO:=1;
	          	aGenRiga.DACR:=aGen.DACR;
	          	aGenRiga.UTCR:=aGen.UTCR;
	          	aGenRiga.UTUV:=aGen.UTUV;
	          	aGenRiga.DUVA:=aGen.DUVA;
	          	aGenRiga.PG_VER_REC:=aGen.PG_VER_REC;
	          	aGenRiga.TI_ASSOCIATO_MANREV:=aGen.TI_ASSOCIATO_MANREV;
	          	aListGenRighe(1):=aGenRiga;
	          	--
		        	CNRCTB110.CREAGENERICOAGGOBBACC(aGen,aListGenRighe);
	          	-- Generazione righe reversale
	          	aRevPRiga:=null;
	          	aRevPRiga.CD_CDS:=aGen.cd_cds;
	          	aRevPRiga.ESERCIZIO:=aGen.esercizio;
	          	aRevPRiga.ESERCIZIO_ACCERTAMENTO:=aGenRiga.esercizio_ACCERTAMENTO;
	          	aRevPRiga.ESERCIZIO_ORI_ACCERTAMENTO:=aGenRiga.esercizio_ORI_ACCERTAMENTO;
	          	aRevPRiga.PG_ACCERTAMENTO:=aGenRiga.pg_accertamento;
	          	aRevPRiga.PG_ACCERTAMENTO_SCADENZARIO:=aGenRiga.pg_accertamento_scadenzario;
	          	aRevPRiga.CD_CDS_DOC_AMM:=aGen.cd_cds;
	          	aRevPRiga.CD_UO_DOC_AMM:=aGen.cd_unita_organizzativa;
	          	aRevPRiga.ESERCIZIO_DOC_AMM:=aGen.esercizio;
	          	aRevPRiga.CD_TIPO_DOCUMENTO_AMM:=aGen.cd_tipo_documento_amm;
		        	aRevPRiga.PG_DOC_AMM:=aGen.pg_documento_generico;
	          	aRevPRiga.DS_REVERSALE_RIGA:=aManP.ds_mandato;
	          	aRevPRiga.STATO:=aManP.stato;
	          	aRevPRiga.CD_TERZO:=aGenRiga.cd_terzo;
	          	aRevPRiga.CD_TERZO_UO:=aGenRiga.cd_terzo_uo_cds;
	          	aRevPRiga.PG_BANCA:=aGenRiga.pg_banca_uo_cds;
	          	aRevPRiga.CD_MODALITA_PAG:=aGenRiga.cd_modalita_pag_uo_cds;
	          	aRevPRiga.IM_REVERSALE_RIGA:=aAcc.im_accertamento;
	          	aRevPRiga.FL_PGIRO:='Y';
	          	aRevPRiga.UTCR:=aUser;
	          	aRevPRiga.DACR:=aTSNow;
	          	aRevPRiga.UTUV:=aUser;
	          	aRevPRiga.DUVA:=aTSNow;
	          	aRevPRiga.PG_VER_REC:=1;
	          	aTotReversale:=aTotReversale+aRevPRiga.im_reversale_riga;
	          	aListRigheRevP(aListRigheRevP.count+1):=aRevPRiga;
						END IF;
---FINE GGG
		     else -- su ammontare = 0
		 	      null;
		     end if; -- if su ammontare

	   End Loop; -- FINE DEL CICLO DI LOOP SU CORI --loop2
     -- Creazione della partita di giro di compensazione nel caso di liquidazione negativa (non via interfaccia)
-- GGG TODO....VERIFICARE QUALI MODIFICHE APPORTARE IN QUESTO CASO...ESERCIZIO PRECEDENTE
	   If recParametriCNR.FL_TESORERIA_UNICA != 'Y' and 
	   		Not lIsCdsInterDet 
	      And Not lIsCdsInterTot
	      And aAggregato.im_liquidato < 0
	      And aLiquid.da_esercizio_precedente = 'Y'
	      -- La compensazione viene fatta SOLO per le liquidazioni locali non per quella del centro!!!
	      -- I cori negativi del centro vanno nella reversale con le compensazioni negative dei gruppi centro da esercizio precedente
	      And aAggregato.cd_unita_organizzativa <> aUOVERSACC.cd_unita_organizzativa
	      And aAggregato.cd_unita_organizzativa <> aUOVERSCONTOBI.cd_unita_organizzativa
	   Then
        -- Genera pgiro
	      -- Riporta pgiro
	      -- Crea riga generico e mandato
	      aAcc:=null;
        aAccScad:=null;

	      -- Devo estrarre l'ELEMENTO_VOCE identificato in CONFIGURAZIONE_CNR come COMPENSAZIONE_CORI

	      aCdEV:=CNRCTB015.GETVAL01PERCHIAVE(aAggregato.esercizio,CNRCTB575.ELEMENTO_VOCE_SPECIALE,CNRCTB575.COMPENSAZIONE_CORI);

    	  Begin
     	    Select * Into aEVComp 
     	    From elemento_voce 
     	    Where esercizio = aAggregato.esercizio
    	      And ti_gestione = CNRCTB001.GESTIONE_SPESE
    	      And ti_appartenenza = CNRCTB001.APPARTENENZA_CDS
    	      And cd_elemento_voce = aCdEV
  		      And fl_partita_giro = 'Y';
        Exception when NO_DATA_FOUND then
    	        IBMERR001.RAISE_ERR_GENERICO('Conto su partita di giro per compensazione CORI negativi non trovato: '||aCdEV);
    	  End;

    	  aObb:=null;
        aObbScad:=null;
        aObb.CD_CDS:=aAggregato.cd_cds;
        aObb.ESERCIZIO:=aAggregato.esercizio;
        aObb.CD_TIPO_DOCUMENTO_CONT:=CNRCTB018.TI_DOC_OBB_PGIRO;
        aObb.CD_UNITA_ORGANIZZATIVA:=aAggregato.cd_unita_organizzativa;
        aObb.CD_CDS_ORIGINE:=aAggregato.cd_cds;
        aObb.CD_UO_ORIGINE:=aAggregato.cd_unita_organizzativa;
        aObb.TI_APPARTENENZA:=aEVComp.ti_appartenenza;
        aObb.TI_GESTIONE:=aEVComp.ti_gestione;
        aObb.CD_ELEMENTO_VOCE:=aEVComp.cd_elemento_voce;
        aObb.DT_REGISTRAZIONE:=TRUNC(aDateCont);
        aObb.DS_OBBLIGAZIONE:='CORI-VA compensazione gruppo cr:'||aAggregato.cd_gruppo_cr||'.'||aAggregato.cd_regione||'.'||aAggregato.pg_comune;
        aObb.NOTE_OBBLIGAZIONE:='';
        If aAggregato.fl_accentrato = 'Y' then
            aObb.CD_TERZO:=aCdTerzoAcc;
        Else
            aObb.CD_TERZO:=aAggregato.cd_terzo_versamento;
	      End If;
    	  aObb.IM_OBBLIGAZIONE:=abs(aAggregato.im_liquidato);
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
        aObb.ESERCIZIO_COMPETENZA:=aAggregato.esercizio;
        CNRCTB030.CREAOBBLIGAZIONEPGIRO(false,aObb,aObbScad,aAcc,aAccScad,trunc(aTSNow));
	      aTipoGenerico := CNRCTB100.TI_GEN_CORI_VER_SPESA;
  	    aGen:=null;
  	    aGenRiga:=null;
     	  aListGenRighe.delete;
        -- Creo il documento generico di spesa su partita di giro collegato all'annotazione di entrata su pgiro del contributo ritenuta
        aGen.CD_CDS:=aAggregato.cd_cds;
        aGen.CD_UNITA_ORGANIZZATIVA:=aAggregato.cd_unita_organizzativa;
        aGen.ESERCIZIO:=aAggregato.esercizio;
        aGen.CD_CDS_ORIGINE:=aAggregato.cd_cds;
        aGen.CD_UO_ORIGINE:=aAggregato.cd_unita_organizzativa;
        aGen.CD_TIPO_DOCUMENTO_AMM:=aTipoGenerico;
        aGen.DATA_REGISTRAZIONE:=TRUNC(aDateCont);
        aGen.DS_DOCUMENTO_GENERICO:='COMPENSAZIONE LIQ. GRUPPO CORI:'||aAggregato.cd_gruppo_cr||'.'||aAggregato.cd_regione||'.'||aAggregato.pg_comune;
        aGen.TI_ISTITUZ_COMMERC:=CNRCTB100.TI_ISTITUZIONALE;
        aGen.IM_TOTALE:=aObb.im_obbligazione;
        aGen.STATO_COFI:=CNRCTB100.STATO_GEN_COFI_TOT_MR;
        aGen.STATO_COGE:=CNRCTB100.STATO_COEP_EXC;
        aGen.STATO_COAN:=CNRCTB100.STATO_COEP_EXC;
        aGen.CD_DIVISA:=aDivisaEuro;
        aGen.CAMBIO:=1;
        aGen.DACR:=aTSNow;
        aGen.UTCR:=aUser;
        aGen.DUVA:=aTSNow;
        aGen.UTUV:=aUser;
        aGen.PG_VER_REC:=1;
        aGen.DT_SCADENZA:=TRUNC(aTSNow);
        aGen.STATO_PAGAMENTO_FONDO_ECO:=CNRCTB100.STATO_NO_PFONDOECO;
        aGen.TI_ASSOCIATO_MANREV:=CNRCTB100.TI_ASSOC_TOT_MAN_REV ;
        aGen.DT_DA_COMPETENZA_COGE:=TRUNC(aDateCont);
        aGen.DT_A_COMPETENZA_COGE:=TRUNC(aDateCont);
        
        aGenRiga.DT_DA_COMPETENZA_COGE:=aGen.DT_DA_COMPETENZA_COGE;
        aGenRiga.DT_A_COMPETENZA_COGE:=aGen.DT_A_COMPETENZA_COGE;
        --
	      aGenRiga.CD_CDS:=aGen.CD_CDS;
        aGenRiga.CD_UNITA_ORGANIZZATIVA:=aGen.CD_UNITA_ORGANIZZATIVA;
        aGenRiga.ESERCIZIO:=aGen.ESERCIZIO;
        aGenRiga.CD_TIPO_DOCUMENTO_AMM:=aGen.CD_TIPO_DOCUMENTO_AMM;
        aGenRiga.DS_RIGA:=aGen.DS_DOCUMENTO_GENERICO;
        aGenRiga.IM_RIGA_DIVISA:=aGen.IM_TOTALE;
        aGenRiga.IM_RIGA:=aGen.IM_TOTALE;
	      -- Utilizzo delle corrette informazioni di pagamento nel caso di accentramento o non accentramento
        if aAggregato.fl_accentrato = 'Y' then
           aGenRiga.CD_TERZO:=aCdTerzoAcc;
           aGenRiga.CD_MODALITA_PAG:=aCdModPagAcc;
           aGenRiga.PG_BANCA:=aPgBancaAcc;
        else
           aGenRiga.CD_TERZO:=aAggregato.cd_terzo_versamento;
           aGenRiga.CD_MODALITA_PAG:=aAggregato.CD_MODALITA_PAGAMENTO;
           aGenRiga.PG_BANCA:=aAggregato.PG_BANCA;
        end if;
        --   aGenRiga.CD_TERZO_CESSIONARIO:=aGen.CD_TERZO_CESSIONARIO;
        --   aGenRiga.CD_TERZO_UO_CDS:=aGen.CD_TERZO_UO_CDS;
	      aGenRiga.RAGIONE_SOCIALE:=aAnagTst.RAGIONE_SOCIALE;
        aGenRiga.NOME:=aAnagTst.NOME;
        aGenRiga.COGNOME:=aAnagTst.COGNOME;
        aGenRiga.CODICE_FISCALE:=aAnagTst.CODICE_FISCALE;
        aGenRiga.PARTITA_IVA:=aAnagTst.PARTITA_IVA;
        --   aGenRiga.CD_TERMINI_PAG:=aCompenso.CD_TERMINI_PAG;
        --   aGenRiga.CD_TERMINI_PAG_UO_CDS:=aCompenso.CD_TERMINI_PAG_UO_CDS;
        --   aGenRiga.PG_BANCA_UO_CDS:=aGen.PG_BANCA_UO_CDS;
        --   aGenRiga.CD_MODALITA_PAG_UO_CDS:=aGen.CD_MODALITA_PAG_UO_CDS;
        --   aGenRiga.NOTE:=aGen.NOTE;
        aGenRiga.STATO_COFI:=aGen.STATO_COFI;
        --   aGenRiga.DT_CANCELLAZIONE:=aGen.DT_CANCELLAZIONE;
        --   aGenRiga.CD_CDS_OBBLIGAZIONE:=aGen.CD_CDS_OBBLIGAZIONE;
        --   aGenRiga.ESERCIZIO_OBBLIGAZIONE:=aGen.ESERCIZIO_OBBLIGAZIONE;
        --   aGenRiga.ESERCIZIO_ORI_OBBLIGAZIONE:=aGen.ESERCIZIO_ORI_OBBLIGAZIONE;
        --   aGenRiga.PG_OBBLIGAZIONE:=aGen.PG_OBBLIGAZIONE;
        --   aGenRiga.PG_OBBLIGAZIONE_SCADENZARIO:=aGen.PG_OBBLIGAZIONE_SCADENZARIO;
        aGenRiga.CD_CDS_OBBLIGAZIONE:=aObb.CD_CDS;
        aGenRiga.ESERCIZIO_OBBLIGAZIONE:=aObb.ESERCIZIO;
        aGenRiga.ESERCIZIO_ORI_OBBLIGAZIONE:=aObb.ESERCIZIO_ORIGINALE;
        aGenRiga.PG_OBBLIGAZIONE:=aObb.PG_OBBLIGAZIONE;
        aGenRiga.PG_OBBLIGAZIONE_SCADENZARIO:=1;
        aGenRiga.DACR:=aGen.DACR;
        aGenRiga.UTCR:=aGen.UTCR;
        aGenRiga.UTUV:=aGen.UTUV;
        aGenRiga.DUVA:=aGen.DUVA;
        aGenRiga.PG_VER_REC:=aGen.PG_VER_REC;
        aGenRiga.TI_ASSOCIATO_MANREV:=aGen.TI_ASSOCIATO_MANREV;
        aListGenRighe(1):=aGenRiga;
        --
	      CNRCTB110.CREAGENERICOAGGOBBACC(aGen,aListGenRighe);

	      -- Generazione righe mandato
        aManPRiga:=null;
        aManPRiga.CD_CDS:=aGen.cd_cds;
        aManPRiga.ESERCIZIO:=aGen.esercizio;
        aManPRiga.ESERCIZIO_OBBLIGAZIONE:=aGenRiga.esercizio_obbligazione;
        aManPRiga.ESERCIZIO_ORI_OBBLIGAZIONE:=aGenRiga.esercizio_ori_obbligazione;
        aManPRiga.PG_OBBLIGAZIONE:=aGenRiga.pg_obbligazione;
        aManPRiga.PG_OBBLIGAZIONE_SCADENZARIO:=aGenRiga.pg_obbligazione_scadenzario;
        aManPRiga.CD_CDS_DOC_AMM:=aGen.cd_cds;
        aManPRiga.CD_UO_DOC_AMM:=aGen.cd_unita_organizzativa;
        aManPRiga.ESERCIZIO_DOC_AMM:=aGen.esercizio;
        aManPRiga.CD_TIPO_DOCUMENTO_AMM:=aGen.cd_tipo_documento_amm;
	      aManPRiga.PG_DOC_AMM:=aGen.pg_documento_generico;
        aManPRiga.DS_MANDATO_RIGA:=aManP.ds_mandato;
        aManPRiga.STATO:=aManP.stato;
        aManPRiga.CD_TERZO:=aGenRiga.cd_terzo;
        aManPRiga.PG_BANCA:=aGenRiga.pg_banca;
        aManPRiga.CD_MODALITA_PAG:=aGenRiga.cd_modalita_pag;
        aManPRiga.IM_MANDATO_RIGA:=aObb.im_obbligazione;
        aManPRiga.IM_RITENUTE_RIGA:=0;
        aManPRiga.FL_PGIRO:='Y';
        aManPRiga.UTCR:=aUser;
        aManPRiga.DACR:=aTSNow;
        aManPRiga.UTUV:=aUser;
        aManPRiga.DUVA:=aTSNow;
        aManPRiga.PG_VER_REC:=1;
        aTotMandato:=aTotMandato+aManPRiga.im_mandato_riga;
        aListRigheManP(aListRigheManP.count+1):=aManPRiga;
        Update liquid_gruppo_cori 
        Set cd_cds_acc_compens=aAcc.cd_cds,
		        esercizio_acc_compens=aAcc.esercizio,
   	        esercizio_ori_acc_compens=aAcc.esercizio_originale,
		        pg_acc_compens=aAcc.pg_accertamento,
            utuv=aUser,
 		        duva=aTSNow,
 		        pg_ver_rec=pg_ver_rec+1
	      Where esercizio = aAggregato.esercizio
          and cd_cds = aAggregato.cd_cds
          and cd_unita_organizzativa = aAggregato.cd_unita_organizzativa
          and pg_liquidazione = aAggregato.pg_liquidazione
          and cd_gruppo_cr = aAggregato.cd_gruppo_cr
          and cd_regione = aAggregato.cd_regione
          and pg_comune = aAggregato.pg_comune
          and cd_cds_origine = aAggregato.cd_cds_origine
          and cd_uo_origine = aAggregato.cd_uo_origine
          and pg_liquidazione_origine = aAggregato.pg_liquidazione_origine;
     End If; -- fine generazione pgiro di compensazione su liq. locale negativa

    End If; -- Fine parte 1 esclusa per liq. di uo appartenenti a cds liquidati via interfaccia - If not lIsCdsInterDet And Not lIsCdsInterTot
      -- ================================================================================
      -- AGGIORNAMENTO DELLA PRATICA AL CENTRO (SOLO LIQUIDAZIONE LOCALE)
      -- ================================================================================
      If aAggregato.cd_unita_organizzativa <> aUOVERSACC.cd_unita_organizzativa and
         aAggregato.cd_unita_organizzativa <> aUOVERSCONTOBI.cd_unita_organizzativa and
         aAggregato.fl_Accentrato = 'Y' Then
 	       Declare
 	          aLocAggregato liquid_gruppo_cori%rowtype;
 			begin
 	       Begin
         	   Select * into aLocAggregato 
         	   From liquid_gruppo_cori 
         	   Where cd_cds = aAggregato.cd_cds
        	     and esercizio = aAggregato.esercizio
        	     and cd_unita_organizzativa = aAggregato.cd_unita_organizzativa
        	     and pg_liquidazione = aAggregato.pg_liquidazione
        	     and cd_cds_origine = aAggregato.cd_cds_origine
        	     and cd_uo_origine = aAggregato.cd_uo_origine
        	     and pg_liquidazione_origine = aAggregato.pg_liquidazione_origine
        	     and cd_gruppo_cr = aAggregato.cd_gruppo_cr
        	     and cd_regione = aAggregato.cd_regione
        	     and pg_comune = aAggregato.pg_comune
 	 	         For update nowait;
         Exception when NO_DATA_FOUND then
 	          IBMERR001.RAISE_ERR_GENERICO('Dettaglio gruppo di liquidazione CORI non trovato');
 	       End;
 	           aggiornaPraticaGruppoCentro(aLiquid, aLocAggregato,aUOVERSACC,aTSNow,aUser, tb_ass_pgiro);
 	       End;
      End If;
    End Loop; -- FINE CICLO DI LOOP SU AGGREGATI -- LOOP 1
  
      -- ================================================================================
      -- GESTIONE DEGLI AGGREGATI RACCOLTI DA ALTRE UO DEL TIPO DI QUELLO IN PROCESSO
      -- ================================================================================
      -- Se l'UO ? quella di versamento CORI centralizzati
      -- Raccoglie tutti gli aggregati al centro del tipo specificato dall'utente (da es. prec e non)
      -- Non filtra pi? per UO
      if aCdUo = aUOVERSACC.cd_unita_organizzativa or aCdUo = aUOVERSCONTOBI.cd_unita_organizzativa then
--pipe.send_message('UOENTE.cd_unita_organizzativa = '||UOENTE.cd_unita_organizzativa);       
--pipe.send_message('aGruppi.cd_unita_organizzativa = '||aGruppi.cd_unita_organizzativa);       
         Declare
            aVSX vsx_liquidazione_cori%rowtype;
            isFoundGruppoCentro boolean;
         Begin
            Select * Into aVSX 
            From vsx_liquidazione_cori v 
            Where v.pg_call = pgCall
	            And v.esercizio = aGruppi.esercizio
	            And v.cd_cds = aGruppi.cd_cds
	            And v.cd_unita_organizzativa = aGruppi.cd_unita_organizzativa
	            And v.cd_gruppo_cr = aGruppi.cd_gruppo_cr
	            And v.cd_regione = aGruppi.cd_regione
	            And v.pg_comune = aGruppi.pg_comune
	            And v.pg_liquidazione = aPgLiq
	            And v.cd_uo_origine = UOENTE.cd_unita_organizzativa;         --'999.000'
	            --And v.cd_uo_origine <> aGruppi.cd_unita_organizzativa;   ?????????????????????????????????? perch? c'??????????

            isFoundGruppoCentro:=false;

	          For aGruppoCentro in (Select * From liquid_gruppo_centro 
	                                Where esercizio = aGruppi.esercizio
 	                                  And cd_gruppo_cr = aGruppi.cd_gruppo_cr
 	                                  And cd_regione = aGruppi.cd_regione
 	                                  And pg_comune = aGruppi.pg_comune
		                                And da_esercizio_precedente = aLiquid.da_esercizio_precedente
 	                                  And stato = CNRCTB575.STATO_GRUPPO_CENTRO_INIZIALE
   	                              For update nowait) 
   	        Loop
               isFoundGruppoCentro:=true;
	             Update liquid_gruppo_centro 
	             Set stato = CNRCTB575.STATO_GRUPPO_CENTRO_CHIUSO,
 		               utuv=aUser,
 		               duva=aTSNow,
 		               pg_ver_rec=pg_ver_rec+1
 	             Where esercizio = aGruppoCentro.esercizio
 	               And cd_gruppo_cr = aGruppoCentro.cd_gruppo_cr
 	               And cd_regione = aGruppoCentro.cd_regione
 	               And pg_comune = aGruppoCentro.pg_comune
 	               And pg_gruppo_centro = aGruppoCentro.pg_gruppo_centro;
            
     	      -- Crea i mandati di eventuale restituzione crediti alle UO con gruppi negativi....GIRA SOLO SUI DATI DEGLI ANNI PRECEDENTI
	          --restituzioneCrediti(aAggregato,aGruppoCentro,aTotReversale,aRevP,aListRigheRevP,aUser,aTSNow);
-- GGG TODO PER I MANDATI DI RESTITUZIONE..SOLO A FINE ANNO
					  If recParametriCNR.FL_TESORERIA_UNICA != 'Y' then
		          restituzioneCrediti(aLiquid,aCdCds,aCdUo,aGruppi.cd_terzo_versamento,aGruppoCentro,aTotReversale,aRevP,aListRigheRevP,aUser,aTSNow);
						end if;
 	          aGen:=null;
            aGenRiga:=null;
            if aGruppoCentro.pg_obb_accentr is not null then
		           begin
                 select * into aObbVC
 		             from obbligazione
 		             where cd_cds = aGruppoCentro.cd_cds_obb_accentr
 		             and esercizio = aGruppoCentro.esercizio_obb_accentr
 		             and esercizio_originale = aGruppoCentro.esercizio_ori_obb_accentr
 		             and pg_obbligazione = aGruppoCentro.pg_obb_accentr
 		             for update nowait;
               exception 
                 when NO_DATA_FOUND then
                   If cnrutil.isLabelObbligazione() Then
                         IBMERR001.RAISE_ERR_GENERICO('Obbligazione di versamento cori al centro non trovata: '||aGruppoCentro.pg_obb_accentr);
                	 Else
                         IBMERR001.RAISE_ERR_GENERICO('Impegno di versamento cori al centro non trovato: '||aGruppoCentro.pg_obb_accentr);
                   End If;
		           end;
		        -- se la UO ? quella del versamento su conto BI occorre rendere tronca la PGIRO in oggetto 
            -- e crearla sempre tronca sulla UO di versamento (999.000)
--pipe.send_message('aCdUo = '||aCdUo);                           
--pipe.send_message('aUOVERSCONTOBI.cd_unita_organizzativa = '||aUOVERSCONTOBI.cd_unita_organizzativa);                           
               If aCdUo = aUOVERSCONTOBI.cd_unita_organizzativa 
--GG CONDIZIONE AGGIUNTA PER EVITARE LA PARTITA DI GIRO DALLA 000.407 ALLA 999.000            
            and aUOVERSACC.cd_unita_organizzativa != aUOVERSCONTOBI.cd_unita_organizzativa then
                  --prendo la pgiro duale (e verifico che sia effettivamente chiusa prima di renderla tronca)
                  aObbOld.cd_cds:=aGruppoCentro.cd_cds_obb_accentr;
                  aObbOld.esercizio:=aGruppoCentro.esercizio_obb_accentr;
                  aObbOld.esercizio_originale:=aGruppoCentro.esercizio_ori_obb_accentr;
                  aObbOld.pg_obbligazione:=aGruppoCentro.pg_obb_accentr;
                  -- Il parametro 'X' serve solo per baipassare il controllo che non possono esistere pi? scadenze 
                  -- per le pgiro di entrata 
                  CNRCTB035.GETPGIROCDSINV(aObbOld,aObbScadOld,aObbScadVoceOld,aAccOld,aAccScadOld,aAccScadVoceOld,'X');
                  --rendo tronca la stessa (cio? annullo la parte spesa)
                  CNRCTB043.troncaPraticaAccPgiro(aAccOld.esercizio,aAccOld.cd_cds,aAccOld.esercizio_originale,aAccOld.pg_accertamento,aTSNow,aUser); 
                  -- devo creare la pgiro tronca sulla 999
                  aObbNew:=null;
                  aObbScadNew:=null;
                  aAccNew:=null;
                  aAccScadNew:=null;
                  --determino il capitolo da mettere sulla pgiro
                  Begin
                     Select distinct a.cd_elemento_voce
                     Into aCdEV
                     From ass_tipo_cori_ev a, tipo_cr_base b
                     Where b.esercizio = aGruppoCentro.esercizio
                       And b.cd_gruppo_cr = aGruppoCentro.cd_gruppo_cr
                       And a.cd_contributo_ritenuta = b.cd_contributo_ritenuta
                       And a.esercizio = aGruppoCentro.esercizio
                       And a.ti_gestione = CNRCTB001.GESTIONE_SPESE
                       And a.ti_appartenenza = CNRCTB001.APPARTENENZA_CDS;
                  Exception 
                          when TOO_MANY_ROWS then
                          	   IBMERR001.RAISE_ERR_GENERICO('Esiste pi? di un conto finanziario associato a CORI del gruppo '||aGruppoCentro.cd_gruppo_cr);
                          when NO_DATA_FOUND then
                              IBMERR001.RAISE_ERR_GENERICO('Conto finanziario associato a CORI del gruppo '||aGruppoCentro.cd_gruppo_cr||' non trovato');
                  End;
                  Begin
                     Select *
                     Into elementoVoce
                     From elemento_Voce
                     Where esercizio = aGruppoCentro.esercizio
                       And ti_appartenenza = CNRCTB001.APPARTENENZA_CDS
                       And ti_gestione = CNRCTB001.GESTIONE_SPESE
                       And cd_elemento_Voce = aCdEV;
                  Exception 
                          when NO_DATA_FOUND then
                         	   IBMERR001.RAISE_ERR_GENERICO('Codice voce '||aCdEV||' associata a CORI del gruppo '||aGruppoCentro.cd_gruppo_cr||' non trovato');
                  End;
                  
                  aObbNew.CD_CDS:=aCdCds;
                  aObbNew.ESERCIZIO:=aEs;
                  aObbNew.ESERCIZIO_ORIGINALE:= aObbOld.esercizio_originale;   ------    aEs;
                  If aObbNew.esercizio = aObbNew.esercizio_originale Then
                       aObbNew.CD_TIPO_DOCUMENTO_CONT:=CNRCTB018.TI_DOC_IMP;
                  Else     
                       aObbNew.CD_TIPO_DOCUMENTO_CONT:=CNRCTB018.TI_DOC_IMP_RES;
                  End If;
                  aObbNew.CD_UNITA_ORGANIZZATIVA:=aCdUo;
                  aObbNew.CD_CDS_ORIGINE:=aCdCds;
                  aObbNew.CD_UO_ORIGINE:=aCdUo;
                  aObbNew.TI_APPARTENENZA:=elementoVoce.ti_appartenenza;
                  aObbNew.TI_GESTIONE:=elementoVoce.ti_gestione;
                  aObbNew.CD_ELEMENTO_VOCE:=elementoVoce.cd_elemento_voce;
                  aObbNew.DT_REGISTRAZIONE:=TRUNC(trunc(aTSNow));
                  aObbNew.DS_OBBLIGAZIONE:='PGiro creata in automatico da liquidazione CORI';
                  aObbNew.NOTE_OBBLIGAZIONE:='';
                  aObbNew.CD_TERZO:=aObbOld.CD_TERZO;
                  aObbNew.IM_OBBLIGAZIONE:=aObbVC.IM_OBBLIGAZIONE;    --aObbOld.IM_OBBLIGAZIONE ? stata gi? annullata
                  aObbNew.stato_obbligazione:=CNRCTB035.STATO_DEFINITIVO;
                  aObbNew.im_costi_anticipati:=0;
                  aObbNew.fl_calcolo_automatico:='N';
                  aObbNew.fl_spese_costi_altrui:='N';
                  aObbNew.FL_PGIRO:='Y';
                  aObbNew.RIPORTATO:='N';
                  aObbNew.DACR:=aTSNow;
                  aObbNew.UTCR:=aUser;
                  aObbNew.DUVA:=aTSNow;
                  aObbNew.UTUV:=aUser;
                  aObbNew.PG_VER_REC:=1;
                  aObbNew.ESERCIZIO_COMPETENZA:=aEs;
               
                  CNRCTB030.CREAOBBLIGAZIONEPGIROTRONC(false,aObbNew,aObbScadNew,aAccNew,aAccScadNew,trunc(aTSNow));

                  CREALIQUIDCORIASSPGIRO(aLiquid,aGruppoCentro.cd_gruppo_cr,aGruppoCentro.cd_regione,aGruppoCentro.pg_comune,'S',aObbNew,aObbOld,null,null,aUser,trunc(aTSNow));
               End If;
        
                -- Creo il documento generico di spesa su partita di giro collegato all'annotazione di entrata su pgiro
 		            -- per versamento accentrato
                aGen.CD_CDS:=aGruppi.cd_cds;
                aGen.CD_UNITA_ORGANIZZATIVA:=aGruppi.cd_unita_organizzativa;
                aGen.ESERCIZIO:=aGruppi.esercizio;
                aGen.CD_CDS_ORIGINE:=aGruppi.cd_cds;
                aGen.CD_UO_ORIGINE:=aGruppi.cd_unita_organizzativa;
                aGen.CD_TIPO_DOCUMENTO_AMM:=CNRCTB100.TI_GEN_CORI_VER_SPESA;
                aGen.DATA_REGISTRAZIONE:=TRUNC(aDateCont);
                aGen.DS_DOCUMENTO_GENERICO:='CORI-ACC'||' cds:'||aGruppi.cd_cds||' uo:'||aGruppi.cd_unita_organizzativa||' pg_liq.:'||aPgLiq||' gruppo cr:'||aGruppi.cd_gruppo_cr;
                aGen.TI_ISTITUZ_COMMERC:=CNRCTB100.TI_ISTITUZIONALE;
                aGen.IM_TOTALE:=aObbVC.im_obbligazione;
                aGen.STATO_COFI:=CNRCTB100.STATO_GEN_COFI_TOT_MR;
                aGen.STATO_COGE:=CNRCTB100.STATO_COEP_EXC;
                aGen.STATO_COAN:=CNRCTB100.STATO_COEP_EXC;
                aGen.CD_DIVISA:=aDivisaEuro;
                aGen.CAMBIO:=1;
                --  aGen.ESERCIZIO_LETTERA:=0;
                --  aGen.PG_LETTERA:=0;
                aGen.DACR:=aTSNow;
                aGen.UTCR:=aUser;
                aGen.DUVA:=aTSNow;
                aGen.UTUV:=aUser;
                aGen.PG_VER_REC:=1;
                aGen.DT_SCADENZA:=TRUNC(aTSNow);
                aGen.STATO_PAGAMENTO_FONDO_ECO:=CNRCTB100.STATO_NO_PFONDOECO;
                aGen.TI_ASSOCIATO_MANREV:=CNRCTB100.TI_ASSOC_TOT_MAN_REV ;
                aGen.DT_DA_COMPETENZA_COGE:=TRUNC(aDateCont);
                aGen.DT_A_COMPETENZA_COGE:=TRUNC(aDateCont);
                
                aGenRiga.DT_DA_COMPETENZA_COGE:=aGen.DT_DA_COMPETENZA_COGE;
                aGenRiga.DT_A_COMPETENZA_COGE:=aGen.DT_A_COMPETENZA_COGE;
                aGenRiga.CD_CDS:=aGen.CD_CDS;
                aGenRiga.CD_UNITA_ORGANIZZATIVA:=aGen.CD_UNITA_ORGANIZZATIVA;
                aGenRiga.ESERCIZIO:=aGen.ESERCIZIO;
                aGenRiga.CD_TIPO_DOCUMENTO_AMM:=aGen.CD_TIPO_DOCUMENTO_AMM;
                aGenRiga.DS_RIGA:=aGen.DS_DOCUMENTO_GENERICO;
                aGenRiga.IM_RIGA_DIVISA:=aGen.IM_TOTALE;
                aGenRiga.IM_RIGA:=aGen.IM_TOTALE;
                aGenRiga.CD_TERZO:=aGruppi.cd_terzo_versamento;
                aGenRiga.CD_MODALITA_PAG:=aGruppi.CD_MODALITA_PAGAMENTO;
                aGenRiga.PG_BANCA:=aGruppi.PG_BANCA;
                aGenRiga.RAGIONE_SOCIALE:=aAnagTst.RAGIONE_SOCIALE;
                aGenRiga.NOME:=aAnagTst.NOME;
                aGenRiga.COGNOME:=aAnagTst.COGNOME;
                aGenRiga.CODICE_FISCALE:=aAnagTst.CODICE_FISCALE;
                aGenRiga.PARTITA_IVA:=aAnagTst.PARTITA_IVA;
                aGenRiga.STATO_COFI:=aGen.STATO_COFI;
								If aCdUo = aUOVERSCONTOBI.cd_unita_organizzativa 
--GG CONDIZIONE AGGIUNTA PER EVITARE LA PARTITA DI GIRO DALLA 000.407 ALLA 999.000            
            and aUOVERSACC.cd_unita_organizzativa != aUOVERSCONTOBI.cd_unita_organizzativa then
								    aGenRiga.CD_CDS_OBBLIGAZIONE:=aObbNew.cd_cds;
 	                  aGenRiga.ESERCIZIO_OBBLIGAZIONE:=aObbNew.esercizio;
                    aGenRiga.ESERCIZIO_ORI_OBBLIGAZIONE:=aObbNew.esercizio_originale;
                    aGenRiga.PG_OBBLIGAZIONE:=aObbNew.pg_obbligazione;
                Else
								    aGenRiga.CD_CDS_OBBLIGAZIONE:=aGruppoCentro.cd_cds_obb_accentr;
 	                  aGenRiga.ESERCIZIO_OBBLIGAZIONE:=aGruppoCentro.esercizio_obb_accentr;
                    aGenRiga.ESERCIZIO_ORI_OBBLIGAZIONE:=aGruppoCentro.esercizio_ori_obb_accentr;
                    aGenRiga.PG_OBBLIGAZIONE:=aGruppoCentro.pg_obb_accentr;                
                End if;
                aGenRiga.PG_OBBLIGAZIONE_SCADENZARIO:=1;
                aGenRiga.DACR:=aGen.DACR;
                aGenRiga.UTCR:=aGen.UTCR;
                aGenRiga.UTUV:=aGen.UTUV;
                aGenRiga.DUVA:=aGen.DUVA;
                aGenRiga.PG_VER_REC:=aGen.PG_VER_REC;
                aGenRiga.TI_ASSOCIATO_MANREV:=aGen.TI_ASSOCIATO_MANREV;
                --
 	              aListGenRighe(1):=aGenRiga;
                CNRCTB110.CREAGENERICOAGGOBBACC(aGen,aListGenRighe);
                
                -- Generazione righe mandato
                aManPRiga:=null;
                aManPRiga.CD_CDS:=aGen.cd_cds;
                aManPRiga.ESERCIZIO:=aGen.esercizio;
                aManPRiga.ESERCIZIO_OBBLIGAZIONE:=aGenRiga.esercizio_obbligazione;
                aManPRiga.ESERCIZIO_ORI_OBBLIGAZIONE:=aGenRiga.esercizio_ori_obbligazione;
                aManPRiga.PG_OBBLIGAZIONE:=aGenRiga.pg_obbligazione;
                aManPRiga.PG_OBBLIGAZIONE_SCADENZARIO:=aGenRiga.pg_obbligazione_scadenzario;
                aManPRiga.CD_CDS_DOC_AMM:=aGen.cd_cds;
                aManPRiga.CD_UO_DOC_AMM:=aGen.cd_unita_organizzativa;
                aManPRiga.ESERCIZIO_DOC_AMM:=aGen.esercizio;
                aManPRiga.CD_TIPO_DOCUMENTO_AMM:=aGen.cd_tipo_documento_amm;
                aManPRiga.PG_DOC_AMM:=aGen.pg_documento_generico;
                aManPRiga.DS_MANDATO_RIGA:=aManP.ds_mandato;
                aManPRiga.STATO:=aManP.stato;
                aManPRiga.CD_TERZO:=aGenRiga.CD_TERZO;
                aManPRiga.PG_BANCA:=aGenRiga.pg_banca;
                aManPRiga.CD_MODALITA_PAG:=aGenRiga.cd_modalita_pag;
                aManPRiga.IM_MANDATO_RIGA:=aGen.IM_TOTALE;
                aManPRiga.IM_RITENUTE_RIGA:=0;
                aManPRiga.FL_PGIRO:='Y';
                aManPRiga.UTCR:=aUser;
                aManPRiga.DACR:=aTSNow;
                aManPRiga.UTUV:=aUser;
                aManPRiga.DUVA:=aTSNow;
                aManPRiga.PG_VER_REC:=1;
                --
 		            aTotMandato:=aTotMandato+aManPRiga.im_mandato_riga;
                aListRigheManP(aListRigheManP.count+1):=aManPRiga;
	          end if;    --fine if aGruppoCentro.pg_obb_accentr is not null then
            For aAggregatoOrig in (Select * From liquid_gruppo_cori
 		   	   	                       Where esercizio = aGruppoCentro.esercizio
 				                             And cd_unita_organizzativa <> aGruppi.cd_unita_organizzativa
 				                             And cd_gruppo_cr = aGruppoCentro.cd_gruppo_cr
 				                             And cd_regione = aGruppoCentro.cd_regione
 				                             And pg_comune = aGruppoCentro.pg_comune
 				                             And pg_gruppo_centro = aGruppoCentro.pg_gruppo_centro
				                             And stato = CNRCTB575.STATO_TRASFERITO
                                   For update nowait
             ) Loop -- loop 3
 		           -- Aggiorna lo stato della liquidazione di origine
 		           select *
 		           into aLiquidOrig
 		           from liquid_cori
 		           where esercizio = aAggregatoOrig.esercizio
 		           and cd_cds = aAggregatoOrig.cd_cds
 		           and cd_unita_organizzativa = aAggregatoOrig.cd_unita_organizzativa
 		           and pg_liquidazione = aAggregatoOrig.pg_liquidazione
 		           for update nowait;
               
 		           -- Aggiorna lo stato della riga di liquidazione in periferia
 		           update liquid_gruppo_cori set
                              stato = CNRCTB575.STATO_LIQUIDATO,
 		              utuv=aUser,
 		              duva=aTSNow,
 		              pg_ver_rec=pg_ver_rec+1
 		           Where esercizio = aAggregatoOrig.esercizio
 		             and cd_cds = aAggregatoOrig.cd_cds
                             and cd_unita_organizzativa = aAggregatoOrig.cd_unita_organizzativa
                             and pg_liquidazione = aAggregatoOrig.pg_liquidazione
                             and cd_gruppo_cr = aAggregatoOrig.cd_gruppo_cr
                             and cd_regione = aAggregatoOrig.cd_regione
                             and pg_comune = aAggregatoOrig.pg_comune
                             and cd_cds_origine = aAggregatoOrig.cd_cds_origine
                             and cd_uo_origine = aAggregatoOrig.cd_uo_origine
                             and pg_liquidazione_origine = aAggregatoOrig.pg_liquidazione_origine;
               
               -- Quando tutti i dettagli trasferiti di una liquidazione risultano liquidati
 		           -- la liquidazione diventa liquidata
 		           declare
 		            aLAGG liquid_gruppo_cori%rowtype;
 		           begin
 		             select * into aLAGG from liquid_gruppo_cori
 		             where esercizio = aAggregatoOrig.esercizio
 		               and cd_cds = aAggregatoOrig.cd_cds
 		               and cd_unita_organizzativa = aAggregatoOrig.cd_unita_organizzativa
 		               and pg_liquidazione = aAggregatoOrig.pg_liquidazione
                             and stato = CNRCTB575.STATO_TRASFERITO
 		             for update nowait;
 		           exception
 		       	      when TOO_MANY_ROWS then
 		       	         null;
 		       	      when NO_DATA_FOUND then
 		       	         update liquid_cori
 		       	         set stato = CNRCTB575.STATO_LIQUIDATO,
 		       	             utuv=aUser,
 		       	             duva=aTSNow,
 		       	             pg_ver_rec=pg_ver_rec+1
 		       	         where esercizio = aAggregatoOrig.esercizio
 		       	           and cd_cds = aAggregatoOrig.cd_cds
 		       	           and cd_unita_organizzativa = aAggregatoOrig.cd_unita_organizzativa
 		       	           and pg_liquidazione = aAggregatoOrig.pg_liquidazione;
 		       	   end;
 	   	      End Loop; -- loop 3
        End Loop; -- Chiusura loop sui gruppi centro (sono 2 da esercizio prec o corr.)
        if not isFoundGruppoCentro then
           IBMERR001.RAISE_ERR_GENERICO('Gruppo centro non liquidato non trovato per Gruppo CORI:'||aGruppi.cd_gruppo_cr||'.'||aGruppi.cd_regione||'.'||aGruppi.pg_comune);
	      end if;
      Exception when NO_DATA_FOUND then
  	     null;
      End;
    End If; -- FINE GESTIONE SPECIALE PER UO DI VERSAMENTO ACCENTRATO if aCdUo = aUOVERSACC.cd_unita_organizzativa or aCdUo = aUOVERSCONTOBI.cd_unita_organizzativa
  End Loop; -- FINE CICLO DI LOOP PRINCIPALE aGruppi
     -- Questa parte, relativa alla chiusura delle partite di giro dei compensi va tolta per i CDS caricati via interfaccia
     -- Inizio parte 2 esclusa per liq. di uo appartenenti a cds liquidati via interfaccia
     if not lIsCdsInterDet And Not lIsCdsInterTot then
        aManP.IM_MANDATO:=aTotMandato;
	      -- Aggiornamento del 14/03/2003 ERR. 530
        aRevP.IM_REVERSALE:=aTotReversale;
        if aTotMandato - aTotReversale >= 0 then
           aManP.IM_RITENUTE:=aTotReversale;
           CNRCTB037.generaDocumento(aManP,aListRigheManP,aRevP,aListRigheRevP);
        else
           --NON DOVREBBE ENTRARE MAI!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
           /*
           CNRCTB037.generaDocumento(aManP,aListRigheManP);
	         if aVersamenti.cd_unita_organizzativa = aUOVERSACC.cd_unita_organizzativa then
               CNRCTB037.generaReversale(aRevP,aListRigheRevP);
	             -- mette il riferimento alla reversale sciolta su tutte e due le liquidazioni 999 e propria del centro
	             update liquid_gruppo_cori l
 	                set l.cd_cds_rev = aRevP.cd_cds
  	              ,l.esercizio_rev = aRevP.esercizio
  	              ,l.pg_rev = aRevP.pg_reversale
               Where l.esercizio = aVersamenti.esercizio
   	             And l.cd_cds = aVersamenti.cd_cds
                 And l.cd_unita_organizzativa = aVersamenti.cd_unita_organizzativa
      	         And l.pg_liquidazione = aPgLiq
       	         And l.cd_gruppo_cr = aVersamenti.cd_gruppo_cr
   	             --And cd_regione = aGruppi.cd_regione
   	             --And pg_comune = aGruppi.pg_comune;
   	             And (l.esercizio,l.cd_gruppo_cr,l.cd_regione,l.pg_comune) In
   	                  (Select c.esercizio,c.cd_gruppo_cr,c.cd_regione,c.pg_comune
   	                   From gruppo_cr_det c
   	       	           Where c.cd_terzo_versamento = aVersamenti.cd_terzo_versamento
   	                     And c.cd_modalita_pagamento = aVersamenti.cd_modalita_pagamento
   	                     And c.pg_banca = aVersamenti.pg_banca);
           end if; 
           */  	        
           IBMERR001.RAISE_ERR_GENERICO('La quota complessiva CORI da versare per Gruppo CORI:'||aVersamenti.cd_gruppo_cr||' e Terzo di versamento .'||aVersamenti.cd_terzo_versamento||' risulta negativa!');
	      end if;
        -- Aggiorna liquid_gruppo_cori con le informazioni relative al mandato di versamento/trasferimento
        Update liquid_gruppo_cori l
 	      Set l.cd_cds_doc = aManP.cd_cds
  	        ,l.esercizio_doc = aManP.esercizio
  	        ,l.pg_doc = aManP.pg_mandato
        Where l.esercizio = aVersamenti.esercizio
   	      And l.cd_cds = aVersamenti.cd_cds
          And l.cd_unita_organizzativa = aVersamenti.cd_unita_organizzativa
          And l.pg_liquidazione = aPgLiq
          And l.cd_gruppo_cr = aVersamenti.cd_gruppo_cr
   	      --And cd_regione = aGruppi.cd_regione
    	    --And pg_comune = aGruppi.pg_comune;
    	    And (l.esercizio,l.cd_gruppo_cr,l.cd_regione,l.pg_comune) In
   	          (Select c.esercizio,c.cd_gruppo_cr,c.cd_regione,c.pg_comune
   	           From gruppo_cr_det c
   	           Where c.cd_terzo_versamento = aVersamenti.cd_terzo_versamento
   	             And c.cd_modalita_pagamento = aVersamenti.cd_modalita_pagamento
   	             And c.pg_banca = aVersamenti.pg_banca);
   	     
     End If; -- Fine parte 2 esclusa per liq. di uo appartenenti a cds liquidati via interfaccia
  End Loop; -- FINE CICLO DI LOOP VERSAMENTO aVersamenti

  -- Aggiorna lo stato della liquidazione corrente
  if isLiquidParzAcc and aUOVERSACC.cd_unita_organizzativa <> aCdUo and aUOVERSCONTOBI.cd_unita_organizzativa <> aCdUo then
  	aStatoLiquidazione:=CNRCTB575.STATO_TRASFERITO;
  else
  	aStatoLiquidazione:=CNRCTB575.STATO_LIQUIDATO;
  end if;
     	        
  update liquid_cori
  set stato = aStatoLiquidazione,
  	  utuv=aUser,
 	    duva=aTSNow,
 	    pg_ver_rec=pg_ver_rec+1
  where esercizio = aEs
    and cd_cds = aCdCds
    and cd_unita_organizzativa = aCdUo
    and pg_liquidazione = aPgLiq;     	        
end;

 procedure aggiorna_totale_gruppo_cori (p_cds in varchar2, p_uo in varchar2, p_aTotLiquid in number, p_aFlAccentrato in varchar2, p_aOldGruppoCr in varchar2, p_aOldRegioneCr in varchar2, p_aOldComuneCr in number, aPgLiq in number, aCdCds in varchar2, aCdUo in varchar2, aEs in number) is 
 begin
   Update 	liquid_gruppo_cori
     set 	im_liquidato = p_aTotLiquid,
      			fl_accentrato=p_aFlAccentrato,
 	       	stato=decode(p_aFlAccentrato,'Y',CNRCTB575.STATO_TRASFERITO,CNRCTB575.STATO_LIQUIDATO)
     Where esercizio = aEs
      And 	cd_cds = aCdCds
      And 	cd_cds_origine = p_cds
      And 	cd_unita_organizzativa = aCdUo
      And 	cd_uo_origine = p_uo
      And 	cd_gruppo_cr = p_aOldGruppoCr
      And 	cd_regione = p_aOldRegioneCr
      And 	pg_comune = p_aOldComuneCr 
      And 	pg_liquidazione = aPgLiq
      And 	pg_liquidazione_origine = aPgLiq;
 end;
 	
 -- ==========================
 -- Calcolo della liquidazione
 -- ==========================

 procedure calcolaLiquidazione(aCdCds varchar2, aEs number,daEsercizioPrec char, aCdUo varchar2, aPgLiq number, aDtDa date, aDtA date, aUser varchar2) is
  aTSNow date;
  aLiquidCori liquid_cori%rowtype;
  aLiquidGruppoCori liquid_gruppo_cori%rowtype;
  aLiquidGruppoCoriDet liquid_gruppo_cori_det%rowtype;
  aOldGruppoCr varchar2(30);
  aOldComuneCr number(10);
  aOldRegioneCr varchar2(10);
  aTotLiquid number(15,2);
  aOldPgCompenso number(10);
  isLiquidaSuInviato char(1);
  aUOVERSACC unita_organizzativa%rowtype;
  aUOVERSUNIFICATI unita_organizzativa%rowtype;
  aUOVERSCONTOBI unita_organizzativa%rowtype;
  aFlAccentrato char(1);
  aIsCdsInterDet boolean;
  aIsCdsInterTot boolean;
  aObb obbligazione%rowtype;
  UOENTE unita_organizzativa%rowtype;

 begin
 	
  if    aPgLiq is null
   	 or aCdCds is null
   	 or aCdUo is null
   	 or aEs is null
   	 or daEsercizioPrec is null
   	 or aDtDa is null
   	 or aDtA is null
   	 or aUser is null
  then
   IBMERR001.RAISE_ERR_GENERICO('Alcuni parametri di liquidazione non sono stati specificati');
  end if;
  -- Controllo che la liquidazione al centro sia aperta
  -- Fix errore 729
  CNRCTB575.CHECKLIQUIDCENTROAPERTA(aEs);

  aIsCdsInterDet := IsCdsInterfDet(aCdCds, aEs);
  aIsCdsInterTot := IsCdsInterfTot(aCdCds, aEs);

  if aIsCdsInterDet then
   	 calcolaLiquidInterf (aCdCds,
   					     aEs ,
						 daEsercizioPrec ,
						 aCdUo ,
						 aPgLiq ,
						 aDtDa ,
						 aDtA ,
						 aUser );
  end if;
  If aIsCdsInterTot Then
   	 calcolaLiquidInterfTot (aCdCds,
   				 aEs ,
				 daEsercizioPrec ,
				 aCdUo ,
				 aPgLiq ,
				 aDtDa ,
				 aDtA ,
				 aUser );
  end if;

  if not aIsCdsInterDet And Not aIsCdsInterTot  then -- Diramazione calcolo cori inter
	 aTSNow:=sysdate;
	 aLiquidCori.CD_CDS:=aCdCds;
	 aLiquidCori.CD_UNITA_ORGANIZZATIVA:=aCdUo;
	 aLiquidCori.ESERCIZIO:=aEs;
	 aLiquidCori.PG_LIQUIDAZIONE:=aPgLiq;
	 aLiquidCori.DA_ESERCIZIO_PRECEDENTE:=daEsercizioPrec;
	 aLiquidCori.DT_DA:=trunc(aDtDa);
	 aLiquidCori.DT_A:=trunc(aDtA);
	 aLiquidCori.STATO:=CNRCTB575.STATO_INIZIALE;
	 aLiquidCori.DACR:=aTSNow;
	 aLiquidCori.UTCR:=aUser;
	 aLiquidCori.DUVA:=aTSNow;
	 aLiquidCori.UTUV:=aUser;
	 aLiquidCori.PG_VER_REC:=0;
	 CNRCTB575.INS_LIQUID_CORI(aLiquidCori);
	 isLiquidaSuInviato:=CNRCTB575.ISLIQUIDACORIINVIATO(aEs);

	 -- Estrazione delle UO di versamento CORI accentrato, unificato e 
	 -- della UO abilitata al versamento direttamente dal conto presso la Banca d'Italia (999.000)
	 aUOVERSACC:=CNRCTB020.getUOVersCori(aEs);
	 aUOVERSUNIFICATI:=CNRCTB020.getUOVersCoriTuttaSAC(aEs);
	 aUOVERSCONTOBI:=CNRCTB020.getUOVersCoriContoBI(aEs);
	 
     -- Controllo che la liq. locale sia aperta nell'esercizio specificato
	 if aUOVERSACC.cd_unita_organizzativa <> aCdUO and aUOVERSCONTOBI.cd_unita_organizzativa <> aCdUO then
           CNRCTB575.CHECKLIQUIDLOCALEAPERTA(aEs);
   end if;

   -- Se l'UO di versamento = all'UO in processo e la liquidazione ? di esercizio CORRENTE
   -- creo le righe relative alla liquidazione al centro dei gruppi locali
   -- targandole con 999.000, 999 rispettivamente in UO e CDS origine
   If aUOVERSACC.cd_unita_organizzativa = aCdUo or aUOVERSCONTOBI.cd_unita_organizzativa = aCdUo Then
	      UOENTE:=CNRCTB020.GETUOENTE(aEs);
        For aGCentro in (Select * from liquid_gruppo_centro 
                         Where esercizio = aEs
			                     And da_esercizio_precedente = daEsercizioPrec
			                     And stato = CNRCTB575.STATO_GRUPPO_CENTRO_INIZIALE
			                     And cd_gruppo_cr = GruppoValido(esercizio, cd_gruppo_cr, aCdUo)
			                   for update nowait)
	      Loop -- loop 1
           Update liquid_gruppo_centro set
             cd_cds_lc = aCdCds,
             cd_uo_lc =aCdUo,
             pg_lc =aPgLiq,
             utuv=aUser,
             duva=aTSNow,
             pg_ver_rec=pg_ver_rec+1
 	         Where esercizio = aGCentro.esercizio
             And pg_gruppo_centro = aGCentro.pg_gruppo_centro
	           And cd_gruppo_cr = aGCentro.cd_gruppo_cr
	           And cd_regione = aGCentro.cd_regione
	           And pg_comune = aGCentro.pg_comune;
	         aObb:=null;
	         if aGCentro.pg_obb_accentr is not null then
		          Begin
     	          Select * into aObb 
     	          From obbligazione 
     	          Where cd_cds = aGCentro.cd_cds_obb_accentr
		              And esercizio = aGCentro.esercizio_obb_accentr
		              And esercizio_originale = aGCentro.esercizio_ori_obb_accentr
		              And pg_obbligazione = aGCentro.pg_obb_accentr
		            For update nowait;
		          Exception when NO_DATA_FOUND then
                 If cnrutil.isLabelObbligazione() Then
			              IBMERR001.RAISE_ERR_GENERICO('Obbligazione per versamento del gruppo centro non trovata');
           	     Else
  			            IBMERR001.RAISE_ERR_GENERICO('Impegno per versamento del gruppo centro non trovato');
          	     End If;
		          End;
	         end if;
	       	 aLiquidGruppoCori.ESERCIZIO:=aGCentro.esercizio;
	       	 aLiquidGruppoCori.CD_CDS:=aCdCds;
	       	 aLiquidGruppoCori.CD_UNITA_ORGANIZZATIVA:=aCdUo;
	       	 aLiquidGruppoCori.PG_LIQUIDAZIONE:=aPgLiq;
	       	 aLiquidGruppoCori.CD_CDS_ORIGINE:=UOENTE.cd_unita_padre;
	       	 aLiquidGruppoCori.CD_UO_ORIGINE:=UOENTE.cd_unita_organizzativa;
	       	 aLiquidGruppoCori.PG_LIQUIDAZIONE_ORIGINE:=0;
	       	 aLiquidGruppoCori.CD_GRUPPO_CR:=aGCentro.cd_gruppo_cr;
	       	 aLiquidGruppoCori.CD_REGIONE:=aGCentro.cd_regione;
	       	 aLiquidGruppoCori.PG_COMUNE:=aGCentro.pg_comune;
	       	 aLiquidGruppoCori.IM_LIQUIDATO:=nvl(aObb.im_obbligazione,0);
	       	 aLiquidGruppoCori.FL_ACCENTRATO:='N';
	       	 aLiquidGruppoCori.STATO:=CNRCTB575.STATO_LIQUIDATO;
	       	 aLiquidGruppoCori.pg_gruppo_centro:=aGCentro.pg_gruppo_centro;
	       	 aLiquidGruppoCori.cd_cds_obb_accentr:=aGCentro.cd_cds_obb_accentr;
	       	 aLiquidGruppoCori.esercizio_obb_accentr:=aGCentro.esercizio_obb_accentr;
	       	 aLiquidGruppoCori.esercizio_ori_obb_accentr:=aGCentro.esercizio_ori_obb_accentr;
	       	 aLiquidGruppoCori.pg_obb_accentr:=aGCentro.pg_obb_accentr;
	       	 aLiquidGruppoCori.ESERCIZIO_DOC:=null;
	       	 aLiquidGruppoCori.CD_CDS_DOC:=null;
	       	 aLiquidGruppoCori.PG_DOC:=null;
	       	 aLiquidGruppoCori.DACR:=aTSNow;
	     	   aLiquidGruppoCori.UTCR:=aUser;
	       	 aLiquidGruppoCori.DUVA:=aTSNow;
	       	 aLiquidGruppoCori.UTUV:=aUser;
	       	 aLiquidGruppoCori.PG_VER_REC:=1;
	  	     CNRCTB575.INS_LIQUID_GRUPPO_CORI(aLiquidGruppoCori);
	  	
        End Loop; -- loop 1
   End If;    --If aUOVERSACC.cd_unita_organizzativa = aCdUo or aUOVERSCONTOBI.cd_unita_organizzativa = aCdUo


	 aLiquidGruppoCori:=null;
	 aOldGruppoCr:=null;
	 aOldPgCompenso:=null;
	 aOldComuneCr:=null;
	 aOldRegioneCr:=null;

   -- Si prendono i dati della UO in processo
	 For aCori in  (select * from v_calcola_liquid_cori_det a
	 	        where a.esercizio = aEs
			-- Questa clausola definisce la provenienza del compenso:
			-- se daEsercizioPrec='Y'
			-- vengono estratti compensi con mandati/reversali dell'esercizio precedente a quello di scrivania
                   	-- altrimenti
			-- vengono estratti compensi con mandati/reversali nell'esercizio di scrivania
				and a.esercizio_doc_cont = decode(daEsercizioPrec,'Y',a.esercizio-1,a.esercizio)
	      and a.cd_cds = aCdCds
			  and a.cd_unita_organizzativa = aCdUo
			  and decode(isLiquidaSuInviato,'Y',a.dt_trasmissione_mandato,a.dt_emissione_mandato) >= trunc(aDtDa)
			  and decode(isLiquidaSuInviato,'Y',a.dt_trasmissione_mandato,a.dt_emissione_mandato) < trunc(aDtA + 1)
			  and a.cd_gruppo_cr = GruppoValido(aEs, a.cd_gruppo_cr, aCdUo)
			  and not exists (Select 1 from liquid_gruppo_cori_det a1
			  	   	  Where -- Esclude i cori gi? processati
					    --a1.cd_cds = a.cd_cds 
					    a1.esercizio_contributo_ritenuta = a.esercizio_compenso
					    --And a1.cd_unita_organizzativa = a.cd_unita_organizzativa 
					    And a1.cd_cds_origine = a.cd_cds
					    And a1.cd_uo_origine = a.cd_unita_organizzativa
					    And (a1.pg_liquidazione != aPgLiq
						 Or 
						 a1.esercizio != a.esercizio
						 Or
						 --se ad es. verso da 000.407 e poi vado nella 000.101, il pg_liq potrebbe
						 --essere lo stesso e quindi rivedo il compenso. Devo non farlo vedere
						 (a1.pg_liquidazione = aPgLiq And
						  a1.esercizio = a.esercizio And 
						  ( (a1.cd_cds = aUOVERSUNIFICATI.cd_unita_padre And 
						     a1.cd_unita_organizzativa = aUOVERSUNIFICATI.cd_unita_organizzativa)
						     Or
						    (a1.cd_cds = a.cd_cds And 
						     a1.cd_unita_organizzativa = a.cd_unita_organizzativa) )
						  )
						 )
					    And a1.cd_contributo_ritenuta = a.cd_contributo_ritenuta
					    And a1.pg_compenso = a.pg_compenso
					    And a1.ti_ente_percipiente = a.ti_ente_percipiente)
			order by cd_gruppo_cr, cd_regione, pg_comune, pg_compenso, cd_contributo_ritenuta)
	 Loop -- loop 3
		 -- Lock del compenso
		 if aCori.cd_gruppo_cr is null then
		    IBMERR001.RAISE_ERR_GENERICO('Gruppo CORI non trovato per CORI:'||aCori.cd_contributo_ritenuta);
		 end if;

		 if aCori.cd_regione is null or aCori.pg_comune is null then
		    IBMERR001.RAISE_ERR_GENERICO('Dettaglio Gruppo CORI non trovato per CORI:'||aCori.cd_contributo_ritenuta||' di tipo:'||aCori.cd_classificazione_cori||' regione:'||aCori.cd_regione_cori||' comune:'||aCori.pg_comune_cori);
		 end if;

		 if aOldPgCompenso is null or aOldPgCompenso<>aCori.pg_compenso then
		    CNRCTB100.LOCKDOCAMM(CNRCTB100.TI_COMPENSO,aCori.cd_cds,aCori.esercizio_compenso,aCori.cd_unita_organizzativa,aCori.pg_compenso);
			aOldPgCompenso:=aCori.pg_compenso;
		 end if;

		 if aOldGruppoCr is null
			or aOldRegioneCr is null
			or aOldComuneCr is null
			or aOldGruppoCr<>aCori.cd_gruppo_cr
		  or aOldRegioneCr<>aCori.cd_regione
			or aOldComuneCr<>aCori.pg_comune      then
		     -- Estraggo l'informazione di versamento accentrato per il gruppo specificato
		     -- Aggiorno le informazioni relative ad importo liquidato e flag accentrato del totale precedente
		     if aOldGruppoCr is not null then
					 aggiorna_totale_gruppo_cori (aCdCds, aCdUo, aTotLiquid, aFlAccentrato, aOldGruppoCr, aOldRegioneCr, aOldComuneCr, aPgLiq, aCdCds, aCdUo, aEs);
         end if;

		     Begin
        	        -- Calcolo il nuovo valore del flag accentrato
        	        select decode(aCori.cd_unita_organizzativa,aUOVERSACC.cd_unita_organizzativa,'N',nvl(d.fl_accentrato,b.fl_accentrato))
        	        into aFlAccentrato
        	        from gruppo_cr b, gruppo_cr_det c, gruppo_cr_uo d
        	        where b.esercizio = aEs
        	        and b.cd_gruppo_cr = aCori.cd_gruppo_cr
        	        and c.esercizio = aEs
        	        and c.cd_gruppo_cr = aCori.cd_gruppo_cr
        	        and c.cd_regione = aCori.cd_regione
        	        and c.pg_comune = aCori.pg_comune
        	        and d.esercizio (+)= c.esercizio
        	        and d.cd_gruppo_cr (+)= c.cd_gruppo_cr
         	        and d.cd_unita_organizzativa (+)=aCori.cd_unita_organizzativa;
		     Exception when NO_DATA_FOUND then
		        IBMERR001.RAISE_ERR_GENERICO('Gruppo CORI non trovato');
		     End;
		     aTotLiquid:=0;
		     aOldGruppoCr:=aCori.cd_gruppo_cr;
		     aOldRegioneCr:=aCori.cd_regione;
		     aOldComuneCr:=aCori.pg_comune;
		     aLiquidGruppoCori.ESERCIZIO:=aEs;
		     aLiquidGruppoCori.CD_CDS:=aCori.cd_cds;
		     aLiquidGruppoCori.CD_UNITA_ORGANIZZATIVA:=aCori.cd_unita_organizzativa;
		 	    aLiquidGruppoCori.CD_CDS_ORIGINE:=aCori.cd_cds;
		     aLiquidGruppoCori.CD_UO_ORIGINE:=aCori.cd_unita_organizzativa;
		     aLiquidGruppoCori.PG_LIQUIDAZIONE:=aLiquidCori.pg_liquidazione;
		     aLiquidGruppoCori.PG_LIQUIDAZIONE_ORIGINE:=aLiquidCori.pg_liquidazione;
		     aLiquidGruppoCori.CD_GRUPPO_CR:=aCori.cd_gruppo_cr;
		     aLiquidGruppoCori.CD_REGIONE:=aCori.cd_regione;
		     aLiquidGruppoCori.PG_COMUNE:=aCori.pg_comune;
		     aLiquidGruppoCori.IM_LIQUIDATO:=null;
		     aLiquidGruppoCori.FL_ACCENTRATO:='N';
	       aLiquidGruppoCori.STATO:=CNRCTB575.STATO_LIQUIDATO;
		     aLiquidGruppoCori.ESERCIZIO_DOC:=null;
		     aLiquidGruppoCori.CD_CDS_DOC:=null;
		     aLiquidGruppoCori.PG_DOC:=null;
		     aLiquidGruppoCori.DACR:=aTSNow;
		     aLiquidGruppoCori.UTCR:=aUser;
		     aLiquidGruppoCori.DUVA:=aTSNow;
		     aLiquidGruppoCori.UTUV:=aUser;
		     aLiquidGruppoCori.PG_VER_REC:=1;
		     aOldGruppoCr:=aLiquidGruppoCori.cd_gruppo_cr;
		     CNRCTB575.INS_LIQUID_GRUPPO_CORI(aLiquidGruppoCori);
	   end if;    --aOldGruppoCr is null or .....
		 aLiquidGruppoCoriDet.CD_CDS:=aCori.cd_cds;
		 aLiquidGruppoCoriDet.CD_UNITA_ORGANIZZATIVA:=aCori.cd_unita_organizzativa;
		 aLiquidGruppoCoriDet.CD_CDS_ORIGINE:=aCori.cd_cds;
		 aLiquidGruppoCoriDet.CD_UO_ORIGINE:=aCori.cd_unita_organizzativa;
		 aLiquidGruppoCoriDet.ESERCIZIO:=aEs;
		 aLiquidGruppoCoriDet.ESERCIZIO_CONTRIBUTO_RITENUTA:=aCori.esercizio_compenso;
		 aLiquidGruppoCoriDet.PG_LIQUIDAZIONE:=aPgLiq;
		 aLiquidGruppoCoriDET.PG_LIQUIDAZIONE_ORIGINE:=aPgLiq;
		 aLiquidGruppoCoriDet.CD_GRUPPO_CR:=aCori.cd_gruppo_cr;
		 aLiquidGruppoCoriDet.CD_REGIONE:=aCori.cd_regione;
		 aLiquidGruppoCoriDet.PG_COMUNE:=aCori.pg_comune;
		 aLiquidGruppoCoriDet.CD_CONTRIBUTO_RITENUTA:=aCori.cd_contributo_ritenuta;
		 aLiquidGruppoCoriDet.PG_COMPENSO:=aCori.pg_compenso;
		 aLiquidGruppoCoriDet.TI_ENTE_PERCIPIENTE:=aCori.ti_ente_percipiente;
		 aLiquidGruppoCoriDet.DACR:=aTSNow;
		 aLiquidGruppoCoriDet.UTCR:=aUser;
		 aLiquidGruppoCoriDet.DUVA:=aTSNow;
		 aLiquidGruppoCoriDet.UTUV:=aUser;
		 aLiquidGruppoCoriDet.PG_VER_REC:=1;
		 CNRCTB575.INS_LIQUID_GRUPPO_CORI_DET(aLiquidGruppoCoriDet);
		 aTotLiquid:=aTotLiquid + aCori.im_cori;
	 End Loop; -- loop 3
   	 If aOldGruppoCr is not null then
	     -- Aggiorno il totale precedente
			 aggiorna_totale_gruppo_cori (aCdCds, aCdUo, aTotLiquid, aFlAccentrato, aOldGruppoCr, aOldRegioneCr, aOldComuneCr, aPgLiq, aCdCds, aCdUo, aEs);
   	 End if;

     -- Se la UO in processo ? quella abilitata a versare i dati di tutte le UO della SAC oppure ? la 999.000
     If aUOVERSUNIFICATI.cd_unita_organizzativa = aCdUo or aUOVERSCONTOBI.cd_unita_organizzativa = aCdUo Then  
     	 For aCdUoSAC In (Select a.cd_unita_organizzativa, a.cd_unita_padre
     	   		  From unita_organizzativa a 
     	   		  Where a.fl_cds = 'N'
             		And a.cd_tipo_unita = CNRCTB020.TIPO_SAC
     	     		  And exists (Select 1 from v_unita_organizzativa_valida 
     	                 	    Where esercizio = aEs
	 		                        And cd_unita_organizzativa = a.cd_unita_organizzativa)
	                            And a.cd_unita_organizzativa <> aCdUo) 
	     Loop
  	      aLiquidGruppoCori:=null;
	        aOldGruppoCr:=null;
	        aOldPgCompenso:=null;
	        aOldComuneCr:=null;
	        aOldRegioneCr:=null;
 
	        For aCori in (select * from v_calcola_liquid_cori_det a
	 	                    where a.esercizio = aEs
			                  -- Questa clausola definisce la provenienza del compenso:
			                  -- se daEsercizioPrec='Y'
			                  -- vengono estratti compensi con mandati/reversali dell'esercizio precedente a quello di scrivania
                                     	-- altrimenti
			                  -- vengono estratti compensi con mandati/reversali nell'esercizio di scrivania
			                    and a.esercizio_doc_cont = decode(daEsercizioPrec,'Y',a.esercizio-1,a.esercizio)
	                        and a.cd_cds = aCdUoSAC.cd_unita_padre
			                    and a.cd_unita_organizzativa = aCdUoSAC.cd_unita_organizzativa
			                    and decode(isLiquidaSuInviato,'Y',a.dt_trasmissione_mandato,a.dt_emissione_mandato) >= trunc(aDtDa)
			                    and decode(isLiquidaSuInviato,'Y',a.dt_trasmissione_mandato,a.dt_emissione_mandato) < trunc(aDtA + 1)
			                    and a.cd_gruppo_cr = GruppoValido(aEs, a.cd_gruppo_cr, aCdUo)
			                    and not exists (Select 1 from liquid_gruppo_cori_det a1
			              	   	                Where -- Esclude i cori gi? processati
			            		                    --    a1.cd_cds = a.cd_cds 
			            		                    a1.esercizio_contributo_ritenuta = a.esercizio_compenso
			            		                    --And a1.cd_unita_organizzativa = a.cd_unita_organizzativa 
			            		                    And a1.cd_cds_origine = a.cd_cds
			            		                    And a1.cd_uo_origine = a.cd_unita_organizzativa
			            		                    And (a1.pg_liquidazione != aPgLiq
			            			                       Or 
			            			                       a1.esercizio != a.esercizio
			            			                       Or
			            			                       --se ad es. verso da 000.407 e poi vado nella 000.101, il pg_liq potrebbe
			            			                       --essere lo stesso e quindi rivedo il compenso. Devo non farlo vedere
			            			                       (a1.pg_liquidazione = aPgLiq And
			            			                        a1.esercizio = a.esercizio And 
			            			                         ( (a1.cd_cds = aUOVERSUNIFICATI.cd_unita_padre And 
			            			                            a1.cd_unita_organizzativa = aUOVERSUNIFICATI.cd_unita_organizzativa)
			            			                            Or
			            			                           (a1.cd_cds = a.cd_cds And 
			            			                            a1.cd_unita_organizzativa = a.cd_unita_organizzativa) )
			            			                       )
			            			                       )
			            		                    And a1.cd_contributo_ritenuta = a.cd_contributo_ritenuta
			            		                    And a1.pg_compenso = a.pg_compenso
			            		                    And a1.ti_ente_percipiente = a.ti_ente_percipiente)
			                  order by cd_unita_organizzativa, cd_gruppo_cr, cd_regione, pg_comune, pg_compenso, cd_contributo_ritenuta)
	        Loop -- loop 3
		      -- Lock del compenso
		      if aCori.cd_gruppo_cr is null then
		         IBMERR001.RAISE_ERR_GENERICO('Gruppo CORI non trovato per CORI:'||aCori.cd_contributo_ritenuta||' e per UO: '||aCori.cd_unita_organizzativa);
		      end if;
          
		      if aCori.cd_regione is null or aCori.pg_comune is null then
		         IBMERR001.RAISE_ERR_GENERICO('Dettaglio Gruppo CORI non trovato per CORI:'||aCori.cd_contributo_ritenuta||' di tipo:'||aCori.cd_classificazione_cori||' regione:'||aCori.cd_regione_cori||' comune:'||aCori.pg_comune_cori||' e per UO: '||aCori.cd_unita_organizzativa);
		      end if;
          
		      if aOldPgCompenso is null or aOldPgCompenso<>aCori.pg_compenso then
		         CNRCTB100.LOCKDOCAMM(CNRCTB100.TI_COMPENSO,aCori.cd_cds,aCori.esercizio_compenso,aCori.cd_unita_organizzativa,aCori.pg_compenso);
			     aOldPgCompenso:=aCori.pg_compenso;
		      end if;
          
		      if aOldGruppoCr is null
			       or aOldRegioneCr is null
			       or aOldComuneCr is null
			       or aOldGruppoCr<>aCori.cd_gruppo_cr
		         or aOldRegioneCr<>aCori.cd_regione
			       or aOldComuneCr<>aCori.pg_comune      then
		          -- Estraggo l'informazione di versamento accentrato per il gruppo specificato
		          -- Aggiorno le informazioni relative ad importo liquidato e flag accentrato del totale precedente
		          if aOldGruppoCr is not null then
								aggiorna_totale_gruppo_cori(aCdUoSAC.cd_unita_padre,aCdUoSAC.cd_unita_organizzativa,  aTotLiquid, aFlAccentrato, aOldGruppoCr, aOldRegioneCr, aOldComuneCr, aPgLiq, aCdCds, aCdUo, aEs);
              End if;
          
		          Begin
             	        -- Calcolo il nuovo valore del flag accentrato
             	        select decode(aCori.cd_unita_organizzativa,aUOVERSACC.cd_unita_organizzativa,'N',aUOVERSCONTOBI.cd_unita_organizzativa,'N',nvl(d.fl_accentrato,b.fl_accentrato))
             	        into aFlAccentrato
             	        from gruppo_cr b, gruppo_cr_det c, gruppo_cr_uo d
             	        where b.esercizio = aEs
             	        and b.cd_gruppo_cr = aCori.cd_gruppo_cr
             	        and c.esercizio = aEs
             	        and c.cd_gruppo_cr = aCori.cd_gruppo_cr
             	        and c.cd_regione = aCori.cd_regione
             	        and c.pg_comune = aCori.pg_comune
             	        and d.esercizio (+)= c.esercizio
             	        and d.cd_gruppo_cr (+)= c.cd_gruppo_cr
              	        and d.cd_unita_organizzativa (+)=aCori.cd_unita_organizzativa;
		          Exception when NO_DATA_FOUND then
		             IBMERR001.RAISE_ERR_GENERICO('Gruppo CORI non trovato');
		          End;
          
		          aTotLiquid:=0;
		          aOldGruppoCr:=aCori.cd_gruppo_cr;
		          aOldRegioneCr:=aCori.cd_regione;
		          aOldComuneCr:=aCori.pg_comune;
		          aLiquidGruppoCori.ESERCIZIO:=aEs;
		          aLiquidGruppoCori.CD_CDS:=aCdCds;
		          aLiquidGruppoCori.CD_UNITA_ORGANIZZATIVA:=aCdUo;
		          aLiquidGruppoCori.CD_CDS_ORIGINE:=aCori.cd_cds;
		          aLiquidGruppoCori.CD_UO_ORIGINE:=aCori.cd_unita_organizzativa;
		          aLiquidGruppoCori.PG_LIQUIDAZIONE:=aPgLiq;
		          aLiquidGruppoCori.PG_LIQUIDAZIONE_ORIGINE:=aPgLiq;--0;
		          aLiquidGruppoCori.CD_GRUPPO_CR:=aCori.cd_gruppo_cr;
		          aLiquidGruppoCori.CD_REGIONE:=aCori.cd_regione;
		          aLiquidGruppoCori.PG_COMUNE:=aCori.pg_comune;
		          aLiquidGruppoCori.IM_LIQUIDATO:=null;
		          aLiquidGruppoCori.FL_ACCENTRATO:='N';
	            aLiquidGruppoCori.STATO:=CNRCTB575.STATO_LIQUIDATO;
		          aLiquidGruppoCori.ESERCIZIO_DOC:=null;
		          aLiquidGruppoCori.CD_CDS_DOC:=null;
		          aLiquidGruppoCori.PG_DOC:=null;
		          aLiquidGruppoCori.DACR:=aTSNow;
		          aLiquidGruppoCori.UTCR:=aUser;
		          aLiquidGruppoCori.DUVA:=aTSNow;
		          aLiquidGruppoCori.UTUV:=aUser;
		          aLiquidGruppoCori.PG_VER_REC:=1;
		          aOldGruppoCr:=aLiquidGruppoCori.cd_gruppo_cr;
		          CNRCTB575.INS_LIQUID_GRUPPO_CORI(aLiquidGruppoCori);
	        end if;   --if aOldGruppoCr is null or ....
		      aLiquidGruppoCoriDet.CD_CDS:=aCdCds;
		      aLiquidGruppoCoriDet.CD_UNITA_ORGANIZZATIVA:=aCdUo;
		      aLiquidGruppoCoriDet.CD_CDS_ORIGINE:=aCori.cd_cds;
		      aLiquidGruppoCoriDet.CD_UO_ORIGINE:=aCori.cd_unita_organizzativa;
		      aLiquidGruppoCoriDet.ESERCIZIO:=aEs;
		      aLiquidGruppoCoriDet.ESERCIZIO_CONTRIBUTO_RITENUTA:=aCori.esercizio_compenso;
		      aLiquidGruppoCoriDet.PG_LIQUIDAZIONE:=aPgLiq;
		      aLiquidGruppoCoriDET.PG_LIQUIDAZIONE_ORIGINE:=aPgLiq;--0;
		      aLiquidGruppoCoriDet.CD_GRUPPO_CR:=aCori.cd_gruppo_cr;
		      aLiquidGruppoCoriDet.CD_REGIONE:=aCori.cd_regione;
		      aLiquidGruppoCoriDet.PG_COMUNE:=aCori.pg_comune;
		      aLiquidGruppoCoriDet.CD_CONTRIBUTO_RITENUTA:=aCori.cd_contributo_ritenuta;
		      aLiquidGruppoCoriDet.PG_COMPENSO:=aCori.pg_compenso;
		      aLiquidGruppoCoriDet.TI_ENTE_PERCIPIENTE:=aCori.ti_ente_percipiente;
		      aLiquidGruppoCoriDet.DACR:=aTSNow;
		      aLiquidGruppoCoriDet.UTCR:=aUser;
		      aLiquidGruppoCoriDet.DUVA:=aTSNow;
		      aLiquidGruppoCoriDet.UTUV:=aUser;
		      aLiquidGruppoCoriDet.PG_VER_REC:=1;
		      CNRCTB575.INS_LIQUID_GRUPPO_CORI_DET(aLiquidGruppoCoriDet);
		      
		      aTotLiquid:=aTotLiquid + aCori.im_cori;
		      
	        End Loop; -- loop 3 
   	   if aOldGruppoCr is not null then

					aggiorna_totale_gruppo_cori(aCdUoSAC.cd_unita_padre,aCdUoSAC.cd_unita_organizzativa,  aTotLiquid, aFlAccentrato, aOldGruppoCr, aOldRegioneCr, aOldComuneCr, aPgLiq, aCdCds, aCdUo, aEs);

   	   end if;
       End Loop;
     End If;       --  fine If aUOVERSUNIFICATI.cd_unita_organizzativa = aCdUo or aUOVERSCONTOBI.cd_unita_organizzativa = aCdUo 
  end if; -- Diramazione calcolo cori inter
 end;

 function IsCdsInterfDet (aCdCds varchar2, aEs number) return boolean is
 coriInterfDet number;
 begin
  	  select count(*)
  	  into coriInterfDet
  	  from LIQUID_CORI_INTERF_CDS
  	  where esercizio = aEs
  	  and   cd_cds = aCdCds
  	  And tipo = 'D';
	  if coriInterfDet > 0 then
	  	 return true;
	  else
	  	 return false;
	  end if;
 end;
 Function IsCdsInterfTot (aCdCds varchar2, aEs number) return boolean is
 coriInterfTot number;
 begin
  	  select count(*)
  	  into coriInterfTot
  	  from LIQUID_CORI_INTERF_CDS
  	  where esercizio = aEs
  	  and   cd_cds = aCdCds
  	  And tipo = 'T';
	  if coriInterfTot > 0 then
	  	 return true;
	  else
	  	 return false;
	  end if;
 end;
 Procedure calcolaLiquidInterf (aCdCds varchar2, aEs number,daEsercizioPrec char,aCdUo varchar2, aPgLiq number, aDtDa date, aDtA date, aUser varchar2) is
 lTSNow date;
 isLiquidaSuInviato char(1);
 lUOVERSACC unita_organizzativa%rowtype;
 lLiquidCori liquid_cori%rowtype;
 lLiquidGruppoCori liquid_gruppo_cori%rowtype;
 lTotLiquidato number (15,2);
 lFlAccentrato char (1);
 aOldCdCori varchar2(10);
 aCdGruppo varchar2(10);

-- lLiquidGruppoCoriInter liquid_cori_interf%rowtype;
 begin
 	  lTotLiquidato := 0;
	  lTSNow:=sysdate;

	  lLiquidCori.CD_CDS:=aCdCds;
	  lLiquidCori.CD_UNITA_ORGANIZZATIVA:=aCdUo;
	  lLiquidCori.ESERCIZIO:=aEs;
	  lLiquidCori.PG_LIQUIDAZIONE:=aPgLiq;
	  -- La liquidazione via interfaccia non pu? essere da esercizio precedente
	  lLiquidCori.DA_ESERCIZIO_PRECEDENTE:=daEsercizioPrec;
	  lLiquidCori.DT_DA:=trunc(aDtDa);
	  lLiquidCori.DT_A:=trunc(aDtA);
	  lLiquidCori.STATO:=CNRCTB575.STATO_INIZIALE;
	  lLiquidCori.DACR:=lTSNow;
	  lLiquidCori.UTCR:=aUser;
	  lLiquidCori.DUVA:=lTSNow;
	  lLiquidCori.UTUV:=aUser;
	  lLiquidCori.PG_VER_REC:=0;
	  CNRCTB575.INS_LIQUID_CORI(lLiquidCori);

--	  isLiquidaSuInviato:=CNRCTB575.ISLIQUIDACORIINVIATO(aEs);

	  -- Estrazione dell'UO di versamento CORI
	  lUOVERSACC:=CNRCTB020.getUOVersCori(aEs);

	  for aLCID in (
	   select * from liquid_cori_interf_dett where
                 cd_cds = aCdCds
           and   cd_unita_organizzativa = aCdUO
           and   esercizio = decode(daEsercizioPrec,'Y',aEs-1,aEs)
           and   pg_liquidazione is null
		   and   dt_inizio = trunc(aDtDa)
		   and   dt_fine 	= trunc(aDtA)
		order by cd_contributo_ritenuta
		for update nowait
	  ) loop
	   aOldCdCori:=null;
	   if aOldCdCori is null or aLCID.cd_contributo_ritenuta <> aOldCdCori then
	    aOldCdCori:=aLCID.cd_contributo_ritenuta;
        begin
	     select cd_gruppo_cr into aCdGruppo from tipo_cr_base where
	                esercizio=aEs
				and cd_contributo_ritenuta = aLCID.cd_contributo_ritenuta;
	    exception when NO_DATA_FOUND then
         IBMERR001.RAISE_ERR_GENERICO('Gruppo cori non trovato per CORI:'||aLCID.cd_contributo_ritenuta);
		end;
	   end if;
       update liquid_cori_interf_dett set
        cd_gruppo_cr = aCdGruppo,
        pg_liquidazione = aPgLiq,
        utuv = aUser,
        duva = lTSNow,
        pg_ver_rec=pg_ver_rec+1
       where
        CD_CDS=aLCID.CD_CDS AND
        ESERCIZIO=aLCID.ESERCIZIO AND
        CD_UNITA_ORGANIZZATIVA=aLCID.CD_UNITA_ORGANIZZATIVA AND
        PG_CARICAMENTO=aLCID.PG_CARICAMENTO AND
        DT_INIZIO=aLCID.DT_INIZIO AND
        DT_FINE=aLCID.DT_FINE AND
        MATRICOLA=aLCID.MATRICOLA AND
        CODICE_FISCALE=aLCID.CODICE_FISCALE AND
        TI_PAGAMENTO=aLCID.TI_PAGAMENTO AND
        ESERCIZIO_COMPENSO=aLCID.ESERCIZIO_COMPENSO AND
        CD_IMPONIBILE=aLCID.CD_IMPONIBILE AND
        TI_ENTE_PERCIPIENTE=aLCID.TI_ENTE_PERCIPIENTE AND
        CD_CONTRIBUTO_RITENUTA=aLCID.CD_CONTRIBUTO_RITENUTA;
	  end loop;
	  for lLiquidGruppoCoriInter in (select
           	       CD_CDS, ESERCIZIO, CD_UNITA_ORGANIZZATIVA, CD_GRUPPO_CR, CD_REGIONE, PG_COMUNE,
				   sum(IM_RITENUTA) IM_LIQUIDATO
									 from liquid_cori_interf_dett
									  where
                                            cd_cds = aCdCds
                                      and   cd_unita_organizzativa = aCdUO
                                      and   esercizio = decode(daEsercizioPrec,'Y',aEs-1,aEs)
                                      and   pg_liquidazione = aPgLiq
                                      and   dt_inizio = trunc(aDtDa)
                                      and   dt_fine 	= trunc(aDtA)
									  group by CD_CDS, ESERCIZIO, CD_UNITA_ORGANIZZATIVA, CD_GRUPPO_CR, CD_REGIONE, PG_COMUNE
									  order by CD_CDS, ESERCIZIO, CD_UNITA_ORGANIZZATIVA, CD_GRUPPO_CR, CD_REGIONE, PG_COMUNE
									 )
	  loop
	      select decode(lLiquidGruppoCoriInter.cd_unita_organizzativa,
		  		 		lUOVERSACC.cd_unita_organizzativa,'N',
						nvl(d.fl_accentrato,b.fl_accentrato))
		  into lFlAccentrato
		  from gruppo_cr b, gruppo_cr_det c, gruppo_cr_uo d
		  where   b.esercizio                  = lLiquidGruppoCoriInter.esercizio
	          and b.cd_gruppo_cr 			   = lLiquidGruppoCoriInter.cd_gruppo_cr
	    	  and c.esercizio	 			   = lLiquidGruppoCoriInter.esercizio
	          and c.cd_gruppo_cr 			   = lLiquidGruppoCoriInter.cd_gruppo_cr
	    	  and c.cd_regione 	 			   = lLiquidGruppoCoriInter.cd_regione
	    	  and c.pg_comune 	 			   = lLiquidGruppoCoriInter.pg_comune
			  and d.esercizio (+)			   = c.esercizio
	          and d.cd_gruppo_cr (+)           = c.cd_gruppo_cr
	    	  and d.cd_unita_organizzativa (+) = lLiquidGruppoCoriInter.cd_unita_organizzativa;
		  if lFlAccentrato = 'Y' then
			  lLiquidGruppoCori.CD_CDS                  := lLiquidGruppoCoriInter.cd_cds;
			  lLiquidGruppoCori.ESERCIZIO 			    := aEs;
			  lLiquidGruppoCori.CD_UNITA_ORGANIZZATIVA  := lLiquidGruppoCoriInter.CD_UNITA_ORGANIZZATIVA;
			  lLiquidGruppoCori.PG_LIQUIDAZIONE		    := lLiquidCori.PG_LIQUIDAZIONE;
			  lLiquidGruppoCori.CD_CDS_ORIGINE		    := lLiquidGruppoCoriInter.cd_cds;
			  lLiquidGruppoCori.CD_UO_ORIGINE		    := lLiquidGruppoCoriInter.CD_UNITA_ORGANIZZATIVA;
			  lLiquidGruppoCori.PG_LIQUIDAZIONE_ORIGINE := lLiquidCori.PG_LIQUIDAZIONE;
			  lLiquidGruppoCori.CD_GRUPPO_CR			:= lLiquidGruppoCoriInter.CD_GRUPPO_CR;
			  lLiquidGruppoCori.CD_REGIONE				:= lLiquidGruppoCoriInter.CD_REGIONE;
			  lLiquidGruppoCori.PG_COMUNE 				:= lLiquidGruppoCoriInter.PG_COMUNE;
			  lLiquidGruppoCori.IM_LIQUIDATO			:= nvl(lLiquidGruppoCoriInter.IM_LIQUIDATO,0);
			  lLiquidGruppoCori.FL_ACCENTRATO			:= lFlAccentrato;
			  lLiquidGruppoCori.STATO			        := CNRCTB575.STATO_TRASFERITO;
			  lLiquidGruppoCori.CD_CDS_DOC				:= NULL;
			  lLiquidGruppoCori.ESERCIZIO_DOC			:= NULL;
			  lLiquidGruppoCori.PG_DOC					:= NULL;
			  lLiquidGruppoCori.CD_CDS_OBB_ACCENTR		:= NULL;
			  lLiquidGruppoCori.ESERCIZIO_OBB_ACCENTR	:= NULL;
			  lLiquidGruppoCori.ESERCIZIO_ORI_OBB_ACCENTR	:= NULL;
			  lLiquidGruppoCori.PG_OBB_ACCENTR			:= NULL;
			  lLiquidGruppoCori.DACR					:= lTSNow;
			  lLiquidGruppoCori.UTCR					:= aUser;
			  lLiquidGruppoCori.DUVA					:= lTSNow;
			  lLiquidGruppoCori.UTUV					:= aUser;
			  lLiquidGruppoCori.PG_VER_REC				:= 1;
			  CNRCTB575.INS_LIQUID_GRUPPO_CORI(lLiquidGruppoCori);
			  lTotLiquidato := lTotLiquidato + lLiquidGruppoCoriInter.IM_LIQUIDATO;
		  else
		  	  IBMERR001.RAISE_ERR_GENERICO('Il gruppo cori relativo all''unita organizzativa risulta essere non accentrato');
		  end if;

	  end loop;
 end;
Procedure calcolaLiquidInterfTot (aCdCds varchar2, aEs number,daEsercizioPrec char,aCdUo varchar2, aPgLiq number, aDtDa date, aDtA date, aUser varchar2) is
 lTSNow date;
 isLiquidaSuInviato char(1);
 lUOVERSACC unita_organizzativa%rowtype;
 lLiquidCori liquid_cori%rowtype;
 lLiquidGruppoCori liquid_gruppo_cori%rowtype;
 lTotLiquidato number (15,2);
 lFlAccentrato char (1);

 Begin
  	  lTotLiquidato := 0;
	  lTSNow:=sysdate;

	  lLiquidCori.CD_CDS:=aCdCds;
	  lLiquidCori.CD_UNITA_ORGANIZZATIVA:=aCdUo;
	  lLiquidCori.ESERCIZIO:=aEs;
	  lLiquidCori.PG_LIQUIDAZIONE:=aPgLiq;
	  -- La liquidazione via interfaccia non pu? essere da esercizio precedente
	  lLiquidCori.DA_ESERCIZIO_PRECEDENTE:=daEsercizioPrec;
	  lLiquidCori.DT_DA:=trunc(aDtDa);
	  lLiquidCori.DT_A:=trunc(aDtA);
	  lLiquidCori.STATO:=CNRCTB575.STATO_INIZIALE;
	  lLiquidCori.DACR:=lTSNow;
	  lLiquidCori.UTCR:=aUser;
	  lLiquidCori.DUVA:=lTSNow;
	  lLiquidCori.UTUV:=aUser;
	  lLiquidCori.PG_VER_REC:=0;
	  CNRCTB575.INS_LIQUID_CORI(lLiquidCori);

	  -- Estrazione dell'UO di versamento CORI
	  lUOVERSACC:=CNRCTB020.getUOVersCori(aEs);

	  --Valorizzazione del progressivo liquidazionje interfaccia
          Update liquid_cori_interf 
          Set pg_liquidazione = aPgLiq,
              utuv = aUser,
              duva = lTSNow,
              pg_ver_rec=pg_ver_rec+1
          Where CD_CDS=aCdCds And
                ESERCIZIO=aEs And
                CD_UNITA_ORGANIZZATIVA=aCdUo And
	        DT_INIZIO=trunc(aDtDa) And
        	DT_FINE=trunc(aDtA) And
        	pg_liquidazione Is Null;

	  for lLiquidGruppoCoriInter in 
	  	(Select CD_CDS, ESERCIZIO, CD_UNITA_ORGANIZZATIVA, CD_GRUPPO_CR, 
	  		CD_REGIONE, PG_COMUNE, Nvl(Sum(IM_LIQUIDATO),0) IM_LIQUIDATO
		 From liquid_cori_interf
		 Where cd_cds = aCdCds
                   And   cd_unita_organizzativa = aCdUO
                   And   esercizio = decode(daEsercizioPrec,'Y',aEs-1,aEs)
                   And   pg_liquidazione = aPgLiq
                   And   dt_inizio = trunc(aDtDa)
                   And   dt_fine = trunc(aDtA)
		 Group by CD_CDS, ESERCIZIO, CD_UNITA_ORGANIZZATIVA, CD_GRUPPO_CR, CD_REGIONE, PG_COMUNE
		 Order by CD_CDS, ESERCIZIO, CD_UNITA_ORGANIZZATIVA, CD_GRUPPO_CR, CD_REGIONE, PG_COMUNE)
	  Loop
	         Select decode(lLiquidGruppoCoriInter.cd_unita_organizzativa,
		  	    lUOVERSACC.cd_unita_organizzativa,'N',
			    nvl(d.fl_accentrato,b.fl_accentrato))
		 Into lFlAccentrato
		 From gruppo_cr b, gruppo_cr_det c, gruppo_cr_uo d
		 Where b.esercizio                  = lLiquidGruppoCoriInter.esercizio
	         And b.cd_gruppo_cr 			   = lLiquidGruppoCoriInter.cd_gruppo_cr
	    	 And c.esercizio	 			   = lLiquidGruppoCoriInter.esercizio
	         And c.cd_gruppo_cr 			   = lLiquidGruppoCoriInter.cd_gruppo_cr
	    	 And c.cd_regione 	 			   = lLiquidGruppoCoriInter.cd_regione
	    	 And c.pg_comune 	 			   = lLiquidGruppoCoriInter.pg_comune
		 And d.esercizio (+)			   = c.esercizio
	         And d.cd_gruppo_cr (+)           = c.cd_gruppo_cr
	    	 And d.cd_unita_organizzativa (+) = lLiquidGruppoCoriInter.cd_unita_organizzativa;
		  
		  if lFlAccentrato = 'Y' then
		         lLiquidGruppoCori.CD_CDS                  := lLiquidGruppoCoriInter.cd_cds;
		         lLiquidGruppoCori.ESERCIZIO 			    := aEs;
			 lLiquidGruppoCori.CD_UNITA_ORGANIZZATIVA  := lLiquidGruppoCoriInter.CD_UNITA_ORGANIZZATIVA;
			 lLiquidGruppoCori.PG_LIQUIDAZIONE		    := lLiquidCori.PG_LIQUIDAZIONE;
			 lLiquidGruppoCori.CD_CDS_ORIGINE		    := lLiquidGruppoCoriInter.cd_cds;
			 lLiquidGruppoCori.CD_UO_ORIGINE		    := lLiquidGruppoCoriInter.CD_UNITA_ORGANIZZATIVA;
			 lLiquidGruppoCori.PG_LIQUIDAZIONE_ORIGINE := lLiquidCori.PG_LIQUIDAZIONE;
			 lLiquidGruppoCori.CD_GRUPPO_CR			:= lLiquidGruppoCoriInter.CD_GRUPPO_CR;
			 lLiquidGruppoCori.CD_REGIONE				:= lLiquidGruppoCoriInter.CD_REGIONE;
			 lLiquidGruppoCori.PG_COMUNE 				:= lLiquidGruppoCoriInter.PG_COMUNE;
			 lLiquidGruppoCori.IM_LIQUIDATO			:= nvl(lLiquidGruppoCoriInter.IM_LIQUIDATO,0);
			 lLiquidGruppoCori.FL_ACCENTRATO			:= lFlAccentrato;
			 lLiquidGruppoCori.STATO			        := CNRCTB575.STATO_TRASFERITO;
			 lLiquidGruppoCori.CD_CDS_DOC				:= NULL;
			 lLiquidGruppoCori.ESERCIZIO_DOC			:= NULL;
			 lLiquidGruppoCori.PG_DOC					:= NULL;
			 lLiquidGruppoCori.CD_CDS_OBB_ACCENTR		:= NULL;
			 lLiquidGruppoCori.ESERCIZIO_OBB_ACCENTR	:= NULL;
			 lLiquidGruppoCori.ESERCIZIO_ORI_OBB_ACCENTR	:= NULL;
			 lLiquidGruppoCori.PG_OBB_ACCENTR			:= NULL;
			 lLiquidGruppoCori.DACR					:= lTSNow;
			 lLiquidGruppoCori.UTCR					:= aUser;
			 lLiquidGruppoCori.DUVA					:= lTSNow;
			 lLiquidGruppoCori.UTUV					:= aUser;
			 lLiquidGruppoCori.PG_VER_REC				:= 1;
			 CNRCTB575.INS_LIQUID_GRUPPO_CORI(lLiquidGruppoCori);
			 lTotLiquidato := lTotLiquidato + lLiquidGruppoCoriInter.IM_LIQUIDATO;
		  else
		  	  IBMERR001.RAISE_ERR_GENERICO('Il gruppo cori relativo all''unita organizzativa risulta essere non accentrato');
		  end if;

	  end loop;
 end;
 function IsGruppoF24EP (aEs number, aGruppo varchar2) return boolean is
    countGruppi number;
 begin
  	  Select count(*)
  	  Into countGruppi
  	  From GRUPPO_CR
  	  Where esercizio = aEs
  	    And cd_gruppo_cr = aGruppo
  	    And (fl_f24online = 'Y' or fl_f24online_previd = 'Y');
	  if countGruppi > 0 then
	  	 return true;
	  else
	  	 return false;
	  end if;
 end;
 function GruppoF24EP (aEs number, aGruppo varchar2) return varchar2 is
    gruppo varchar2(10);
 begin
  	  Select cd_gruppo_cr
  	  Into gruppo
  	  From GRUPPO_CR
  	  Where esercizio = aEs
  	    And cd_gruppo_cr = aGruppo
  	    And (fl_f24online = 'Y' or fl_f24online_previd = 'Y');
  	  return gruppo;  
 exception 	    
     when others then 
         gruppo := 'XXXXXXXXXX';
         return gruppo;
 end;
 
  function GruppoValido (aEs number, aGruppo varchar2, aCdUo varchar2) return varchar2 is
    gruppo varchar2(10);
    aUOVERSACC unita_organizzativa%rowtype;
    aUOVERSCONTOBI unita_organizzativa%rowtype;
 begin
 	    aUOVERSACC:=CNRCTB020.getUOVersCori(aEs);
	    aUOVERSCONTOBI:=CNRCTB020.getUOVersCoriContoBI(aEs);
      -- Se la UO di versamento accentrato ? uguale alla UO dei versamenti su Conto BI tutti i gruppi sono validi
	    IF (aUOVERSACC.cd_unita_organizzativa = aUOVERSCONTOBI.cd_unita_organizzativa) THEN
	    	RETURN aGruppo;
	    ELSif aUOVERSACC.cd_unita_organizzativa = aCdUo or cnrctb020.isUOSAC(aCdUo) then 
	      Begin
  	      Select cd_gruppo_cr
  	      Into gruppo
  	      From GRUPPO_CR
  	      Where esercizio = aEs
  	        And cd_gruppo_cr = aGruppo
  	        And (fl_f24online = 'N' and fl_f24online_previd = 'N');
  	    Exception 	    
           when others then 
               gruppo := 'XXXXXXXXXX';
               return gruppo;    
        End;         
  	  elsif aUOVERSCONTOBI.cd_unita_organizzativa = aCdUo then 
  	    Begin
  	      Select cd_gruppo_cr
  	      Into gruppo
  	      From GRUPPO_CR
  	      Where esercizio = aEs
  	        And cd_gruppo_cr = aGruppo
  	        And (fl_f24online = 'Y' or fl_f24online_previd = 'Y');
  	    Exception 	    
           when others then 
               gruppo := 'XXXXXXXXXX';
               return gruppo;    
        End;
      -- la UO in processo non ? n? quella dei versamenti accentrati, n? una della SAC, n? quella dei versamente su Conto BI  
      -- quindi tutti i gruppi sono validi
      else   
  	    return aGruppo;
  	  end if;  
  	        
  	  return gruppo;  
 end;
 Procedure CREALIQUIDCORIASSPGIRO(aLiquid liquid_cori%rowtype,aGruppo varchar2, aRegione varchar2, aComune number,aTipo varchar2,aObbNew obbligazione%rowtype,aObbOld obbligazione%rowtype,aAccNew accertamento%rowtype,aAccOld accertamento%rowtype,aUser varchar2,aTSNow date) is
 begin
 	  if aTipo = 'S' then
 	      insert into LIQUID_CORI_ASS_PGIRO (
            CD_CDS,                  
            ESERCIZIO,
            CD_UNITA_ORGANIZZATIVA,
            PG_LIQUIDAZIONE,
            CD_GRUPPO_CR,
            CD_REGIONE,
            PG_COMUNE,
            TIPO_PGIRO,
            CD_CDS_PGIRO_NEW,
            ESERCIZIO_PGIRO_NEW,
            PG_PGIRO_NEW,
            ESERCIZIO_ORI_PGIRO_NEW,
            CD_CDS_PGIRO_ORIGINE,
            ESERCIZIO_PGIRO_ORIGINE,
            PG_PGIRO_ORIGINE,
            ESERCIZIO_ORI_PGIRO_ORIGINE,
            UTCR,
            DACR,
            UTUV,
            DUVA,
            PG_VER_REC)
          values(
            aLiquid.CD_CDS,                  
            aLiquid.ESERCIZIO,
            aLiquid.CD_UNITA_ORGANIZZATIVA,
            aLiquid.PG_LIQUIDAZIONE,
            aGruppo,
            aRegione,
            aComune,
            aTipo,
            aObbNew.CD_CDS,
            aObbNew.ESERCIZIO,
            aObbNew.PG_OBBLIGAZIONE,
            aObbNew.ESERCIZIO_ORIGINALE,
            aObbOld.CD_CDS,
            aObbOld.ESERCIZIO,
            aObbOld.PG_OBBLIGAZIONE,
            aObbOld.ESERCIZIO_ORIGINALE,
            aUser,
            aTSNow,
            aUser,
            aTSNow,
            1); 
    Elsif aTipo = 'E' then
 	      insert into LIQUID_CORI_ASS_PGIRO (
            CD_CDS,                  
            ESERCIZIO,
            CD_UNITA_ORGANIZZATIVA,
            PG_LIQUIDAZIONE,
            CD_GRUPPO_CR,
            CD_REGIONE,
            PG_COMUNE,
            TIPO_PGIRO,
            CD_CDS_PGIRO_NEW,
            ESERCIZIO_PGIRO_NEW,
            PG_PGIRO_NEW,
            ESERCIZIO_ORI_PGIRO_NEW,
            CD_CDS_PGIRO_ORIGINE,
            ESERCIZIO_PGIRO_ORIGINE,
            PG_PGIRO_ORIGINE,
            ESERCIZIO_ORI_PGIRO_ORIGINE,
            UTCR,
            DACR,
            UTUV,
            DUVA,
            PG_VER_REC)
          values(
            aLiquid.CD_CDS,                  
            aLiquid.ESERCIZIO,
            aLiquid.CD_UNITA_ORGANIZZATIVA,
            aLiquid.PG_LIQUIDAZIONE,
            aGruppo,
            aRegione,
            aComune,
            aTipo,
            aAccNew.CD_CDS,
            aAccNew.ESERCIZIO,
            aAccNew.PG_ACCERTAMENTO,
            aAccNew.ESERCIZIO_ORIGINALE,
            aAccOld.CD_CDS,
            aAccOld.ESERCIZIO,
            aAccOld.PG_ACCERTAMENTO,
            aAccOld.ESERCIZIO_ORIGINALE,
            aUser,
            aTSNow,
            aUser,
            aTSNow,
            1); 
    End if;                  
 end;
 
 Procedure CREA_ASS_PGIRO_GR_C(tb_ass_pgiro tab_ass_pgiro, aUser varchar2, aTSNow date) is
 begin
  For i in 1..Nvl(tb_ass_pgiro.Count,0) Loop
 		insert into ASS_PGIRO_GRUPPO_CENTRO (
  	    cd_cds      	    ,
  	    cd_uo			 		    ,
  	    cd_cds_orig 	    ,
  	    cd_uo_orig	 	    ,
  	    esercizio   	    ,
  	    es_compenso 	    ,
  	    pg_compenso 	    ,
  	    pg_liq      	    ,
  	    pg_liq_orig 	    ,
  	    cd_gr_cr    	    ,
  	    cd_regione  	    ,
  	    pg_comune   	    ,
  	    cd_cori     	    ,
  	    ti_en_per   	    ,
  	    ti_origine	 	    ,
  	    es_acc				    ,
  	    es_ori_acc		    ,
  	    cds_acc				    ,
  	    pg_acc				    ,
  	    uo_acc				    ,
  	    es_obb				    ,
  	    es_ori_obb		    ,
  	    cds_obb				    ,
  	    pg_obb				    ,
  	    uo_obb				    ,
  	    cd_cds_acc_pgiro  ,
  	    es_acc_pgiro   	  ,
  	    es_orig_acc_pgiro ,
  	    pg_acc_pgiro      ,
  	    uo_acc_pgiro			,
  	    voce_acc_pgiro		,
  	    cd_cds_obb_pgiro  ,
  	    es_obb_pgiro   	  ,
  	    es_orig_obb_pgiro ,
  	    pg_obb_pgiro      ,
  	    ti_origine_pgiro	,
  	    uo_obb_pgiro			,
  	    voce_obb_pgiro		,
  			DACR              ,
  			UTCR              ,
  			DUVA              ,
  			UTUV              ,
  	    PG_VER_REC)
  	  values(
				tb_ass_pgiro(i).cd_cds     ,
				tb_ass_pgiro(i).cd_uo      ,
				tb_ass_pgiro(i).cd_cds_orig,
				tb_ass_pgiro(i).cd_uo_orig ,
				tb_ass_pgiro(i).esercizio  ,
				tb_ass_pgiro(i).es_compenso ,
				tb_ass_pgiro(i).pg_compenso ,
				tb_ass_pgiro(i).pg_liq     ,
				tb_ass_pgiro(i).pg_liq_orig,
				tb_ass_pgiro(i).cd_gr_cr   ,
				tb_ass_pgiro(i).cd_regione ,
				tb_ass_pgiro(i).pg_comune  ,
				tb_ass_pgiro(i).cd_cori  ,
				tb_ass_pgiro(i).ti_en_per,
				tb_ass_pgiro(i).ti_origine,
				tb_ass_pgiro(i).es_acc,
				tb_ass_pgiro(i).es_ori_acc,
				tb_ass_pgiro(i).cds_acc,
				tb_ass_pgiro(i).pg_acc,
				tb_ass_pgiro(i).uo_acc,
				tb_ass_pgiro(i).es_obb,
				tb_ass_pgiro(i).es_ori_obb,
				tb_ass_pgiro(i).cds_obb,
				tb_ass_pgiro(i).pg_obb,
				tb_ass_pgiro(i).uo_obb,
  			tb_ass_pgiro(i).cd_cds_acc_pgiro ,
        tb_ass_pgiro(i).es_acc_pgiro   		,
        tb_ass_pgiro(i).es_orig_acc_pgiro,
        tb_ass_pgiro(i).pg_acc_pgiro     ,
        tb_ass_pgiro(i).uo_acc_pgiro,
        tb_ass_pgiro(i).voce_acc_pgiro  ,
  			tb_ass_pgiro(i).cd_cds_obb_pgiro ,
        tb_ass_pgiro(i).es_obb_pgiro   		,
        tb_ass_pgiro(i).es_orig_obb_pgiro,
        tb_ass_pgiro(i).pg_obb_pgiro     ,
				tb_ass_pgiro(i).ti_origine_pgiro ,
        tb_ass_pgiro(i).uo_obb_pgiro,
        tb_ass_pgiro(i).voce_obb_pgiro  ,
  	    aTSNow,
  	    aUser,
  	    aTSNow,
  	    aUser,
  	    1); 
	End Loop;
 end;
End;


