--------------------------------------------------------
--  DDL for Package CNRCTB570
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRCTB570" As
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
-- Il doc generico di versamento cori è sempre di tipo versamento (entrata/spesa) non trasferimento nei caso di cori accentrati
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
-- Fix errore in raggruppamento dati di interfaccia dettaglio CORI: pg_caricamento ora è ignorato
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
-- La liquidazione al centro è bloccata quando quella locale è bloccata
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
-- non deve essere fatto solo da aGruppi.im_liquidato perchè esso contiene solo la parte positiva
-- bensì deve essere sottratta la parte negativa presente in V_LIQUID_CENTRO_UO e se la differenza è
-- negativa la liquidazione deve essere bloccata.
-- Gestito con RAISE_WRN_GENERICO e non con RAISE_ERR_GENERICO il messaggio 'Nessun gruppo CORI trovato
-- per la liquidazione' per poterlo intercettare nella Liquidazione CORI massiva.
--
-- Date: 19/04/2006
-- Version: 7.22
-- Gestite le scadenze per le pgiro di entrata per consentire la chiusura di una Reversale Provvisoria
-- legata a gruppi CORI accentrati ancora aperti. In questo caso, cioè se il gruppo è aperto ma la Rev. Provv.
-- è chiusa, viene creata una nuova scadenza per la pgiro di entrata ed una nuova rev. provv. a cui agganciarla
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
												 voce_obb_pgiro			varchar2(20),
                        CD_CDS_ACC_PGIRO_OPP VARCHAR2(30), 
                        ES_ACC_PGIRO_OPP number(4), 
                        ES_ORIG_ACC_PGIRO_OPP number(4), 
                        PG_ACC_PGIRO_OPP number(9), 
                        CD_CDS_OBB_PGIRO_OPP VARCHAR2(30), 
                        ES_OBB_PGIRO_OPP number(4), 
                        ES_ORIG_OBB_PGIRO_OPP number(4), 
                        PG_OBB_PGIRO_OPP number(9));

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
-- pre-post-name: Calcolo della liquidazione effettuato dall'unità organizzativa di versamento per l'esercizio
-- pre: l'unità organizzativa che effettua il calcolo è UO di versamento per l'esercizio
-- post: genera la testata della liquidazione valorizzandola dai contributi ritenuta e la inserisce in LIQUID_CORI.
-- Estrae l'UO del SAC responsabile del versamento CORI accentrato. Per ogni unità organizzativa, che abbia generato
-- un mandato di pagamento al SAC, genera una riga di liquidazione in LIQUID_GRUPPO_CORI, per quei
-- contributi che siano stati inviati nell'ambito del periodo di validità del contributo stesso (Data di emissione/invio cassiere
-- del mandato di pagamento emesso dalle UO locali verso l'UO di versamento della SAC).
-- Lo stato dei dettagli propri e raccolti da altre UO è uguale a L (Liquidato)
-- Vedi pre-post 'Calcolo della liquidazione per tutte le unità organizzative non responsabili del versamento
-- accentrato per l'esercizio.'
--
-- pre-post-name: Calcolo della liquidazione per tutte le unità organizzative : gruppo CORI non trovato
-- pre: per il contributo ritenuta in esame non viene trovato il gruppo cui appartiene
-- post: viene sollevata un'eccezione
--
-- pre-post-name: Calcolo della liquidazione "da esercizio precedente"
-- pre: l'utente selziona il tipo di liquidazione "da esercizio precedente"
-- post: in questo caso vengono estratti tutti i CORI relativi a compensi con mandato inviato o emesso nel periodo specificato
--       e nell'esercizio appena precedente a quello in processo.
--
-- pre-post-name: Calcolo della liquidazione per tutte le unità organizzative non responsabili del versamento
-- accentrato per l'esercizio
-- pre: non è soddisfatta nessuna pre-post precedente
-- post: per i contributi ritenuta che non siano già stati processati, estrae l'informazione di versamento accentrato
-- per il gruppo di appartenenza del contributo ritenuta, aggiorna il totale del contributo e definisce come accentrato
-- o meno il contributo ritenuta (se si tratta dell'unità organizzativa responsabile del versamento centralizzato, non
-- c'è accentramento; se si tratta di un'altra UO, l'accentramento è definito a seconda del gruppo di appartenenza del
-- contributo e dell'unità organizzativa per i comportamenti in deroga al gruppo in funzione della UO), genera la
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
-- aCdUo -> unità organizzativa che effettua il calcolo della liquidazione
-- aPgLiq -> numero progressivo della liquidazione
-- aDtDa -> data di inizio validità
-- aDtA -> data fine validità
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
-- pre: l'unità organizzativa che effettua la liquidazione non è responsabile del versamento centralizzato dei
-- contributi ritenuta, non trova l'associazione tra obbligazioni ed accertamenti su partita di giro parte spese
-- per il contributo ritenuta in ASS_OBB_ACR_PGIRO
-- post: viene sollevata un'eccezione
--
-- pre-post-name: Esiste più di un conto finanziario associato a un gruppo CR via i suoi CORI
-- pre: Esistono almeno 2 voci del piano diverse associate a CORI raccolti nello stesso gruppo di versamento
-- post: viene sollevata un.eccezione
--
-- pre-post-name: Obbligazione di contropartita CORI non trovata in UO non responsabile versamento centralizzato
-- pre: l'unità organizzativa che effettua la liquidazione non è responsabile del versamento centralizzato dei
-- contributi ritenuta, non trova l'obbligazione collegata all'accertamento in partita di giro in OBBLIGAZIONE.
-- post: viene sollevata un'eccezione
--
-- pre-post-name: Scadenza dell'obbligazione di contropartita CORI non trovata in UO non responsabile versamento centralizzato
-- pre: l'unità organizzativa che effettua la liquidazione non è responsabile del versamento centralizzato dei
-- contributi ritenuta, non trova la scadenza dell.obbligazione in OBBLIGAZIONE_SCADENZIARIO.
-- post: viene sollevata un.eccezione
--
-- pre-post-name: Accertamento in partita di giro per il CdS origine non trovata, per gruppi di contributi ritenuta
-- accentrati
-- pre: l'unità organizzativa che effettua la liquidazione non è responsabile del versamento centralizzato dei
-- contributi ritenuta, non trova l'annotazione di entrata su partita di giro origine per il contributo ritenuta
-- post: viene sollevata un'eccezione
--
-- pre-post-name: Terzo corrispondente all'unità organizzativa non trovato in anagrafica, per gruppi di contributi
-- ritenuta accentrati
-- pre: l'unità organizzativa che effettua la liquidazione non è responsabile del versamento centralizzato dei
-- contributi ritenuta, non trova il terzo corrispondente all'unità organizzativa in anagrafica
-- post: viene sollevata un'eccezione
--
-- pre-post-name: Liquidazione dei contributi ritenuta da parte di unità organizzativa non responsabile del versamento
-- centralizzato per l'esercizio
-- pre: l'unità organizzativa che effettua la liquidazione non è responsabile del versamento centralizzato dei
-- contributi ritenuta
-- post: per ogni gruppo contributo ritenuta (comprensivo di regione comune), di cui si vuole effettuare la liquidazione, selezionato dall'applicazione,
-- genera la testata di un mandato principale di versamento dei contributi ritenuta valorizzato dai parametri della
-- liquidazione. Per ogni contributo ritenuta appartenente al gruppo in esame, in ASS_OBB_ACR_PGIRO individua
-- associazione fra obbligazione ed accertamento su partita di giro parte spese per il contributo ritenuta generata
-- dalla liquidazione del compenso, individua l'obbligazione collegata in OBBLIGAZIONE e le relative scadenze in OBBLIGAZIONE_SCADENZIARIO.
-- Se la liquidazione del gruppo contributo ritenuta in esame è effettuato direttamente dall'unità organizzativa:
-- crea un documento generico di spesa su partita di giro collegato all'annotazione di speda su partita di giro aperta dalla liquidazione
-- compenso (le righe del generico sono valorizzate anche in funzione dell'anagrafica del terzo), genera i
-- dettagli del mandato di pagamento associato al documento generico.
-- Se la liquidazione del gruppo contributo ritenuta in esame è accentrata ad un'altra unità organizzativa responsabile
-- del versamento: verifica l'esistenza di un'obbligazione su partita di giro definita nell'UO di versamento.
-- Il riferimento a tale obbligazione è registrato nella tabella LIQUID_GRUPPO_CENTRO definita per GRUPPO/REGIONE/COMUNE è con stato = I (Iniziale)
--  Se tale riferimento non viene trovato, crea un accertamento su UO di versamento centralizzato in partita di giro
--   utilizzando il capitolo (che deve essere unico) per il gruppo cori in processo.
--   Viene creato un nuovo riferimento (nuovo PG_GRUPPO_CENTRO) in LIQUID_GRUPPO_CENTRO all'obbligazione di contropartita creato in stato=I (Iniziale)
--   Viene creato un documento generico di entrata collegata all'accertamento e una reversale provvisoria collegata all'accertamento stesso.
--   L'indicazione della contropartita di spesa (obbligazione) creata sull'unità organizzativa che accentra i versamenti viene iscritta in LIQUIDAZIONE_GRUPPO_CORI
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
-- pre: l'unità organizzativa che effettua la liquidazione è responsabile del versamento centralizzato dei contributi
-- ritenuta, non trova la liquidazione dei contributi ritenuta di origine in LIQUID_CORI
-- post: viene sollevata un'eccezione
--
-- pre-post-name: Gruppo contributi ritenuta di origine non trovato
-- pre: l'unità organizzativa che effettua la liquidazione è responsabile del versamento centralizzato dei contributi
-- ritenuta, non trova il gruppo contributi ritenuta di origine in LIQUID_GRUPPO_CORI
-- post: viene sollevata un.eccezione
--
-- pre-post-name: Obbligazione non trovata per versamento accentrato
-- pre: l'unità organizzativa che effettua la liquidazione è responsabile del versamento centralizzato dei contributi
-- ritenuta, non trova l'obbligazione, dell'annotazione aperta su partita di giro per il versamento accentrato in
-- OBBLIGAZIONE
-- post: viene sollevata un.eccezione
--
-- pre-post-name: Scadenza dell'obbligazione non trovata per il versamento accentrato
-- pre: l'unità organizzativa che effettua la liquidazione è responsabile del versamento centralizzato dei contributi
-- ritenuta, non trova la scadenza dell'obbligazione in OBBLIGAZIONE_SCADENZIARIO
-- post: viene sollevata un.eccezione
--
-- pre-post-name: Liquidazione contributi ritenuta da parte dell'unità organizzativa di versamento per l'esercizio
-- pre: l'unità organizzativa che effettua la liquidazione è responsabile del versamento centralizzato dei contributi
-- ritenuta
-- post: per ogni gruppo contributo ritenuta, di cui si vuole effettuare la liquidazione, selezionato dall'applicazione,
-- genera la testata di un mandato principale di versamento dei contributi ritenuta valorizzato dai parametri della
-- liquidazione.
-- Gestione del versamento dei contributi ritenuta accentrati da altre unità organizzative: individua la liquidazione
-- di origine in LIQUID_CORI, individua il gruppo di contributi ritenuta di origine in LIQUID_GRUPPO_CORI, individua
-- l'obbligazione emessa da altra unità organizzativa (annotazione su partita di giro aperta per versamento accentrato)
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
--       Se l'importo del mandato di liquidazione all'erario è superiore o uguale all'importo della reversale
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
 -- pre-post-name: Non è possibile annullare il versamento accentrato nel caso un gruppo locale faccia parte del MANDATO
 -- pre: Il mandato collegato a aLGC è quello di versamento accentrato ed esiste un GRUPPO LOCALE presente in quel mandato
 -- post: viene sollevata un'eccezione
 --
 -- pre-post-name: Non è possibile annullare il trasferimento al centro se la liquidazione al centro è gia stata effettuata
 -- pre: Il mandato collegato a aLGC è quello di trasferimento a CORI e la liquidazione è in stato L (Liquidata)
 -- post: viene sollevata un'eccezione
 --
 -- pre-post-name: Non è possibile modificare la partita di giro aperta sul centro per il versamento accentrato
 -- pre: Il gruppo CORI è accentrato e l'obbligazione creato al centro collegata all'accertamento generato per trasferimento
 -- liquidazione risulta chiusa da liquidazione centralizzata (stato C chiuso in LIQUID_GRUPPO_CENTRO che referenzia l'obbligazione)
 -- post: viene sollevata un'eccezione
 --
 -- pre-post-name: Annullamento liquidazione gruppo cori
 -- pre: Nessuna delle precondizioni precedenti è verificata
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
