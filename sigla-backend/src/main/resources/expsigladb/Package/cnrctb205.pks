CREATE OR REPLACE package CNRCTB205 as
--
-- CNRCTB205 - Package scritture COEP
-- Date: 14/07/2006
-- Version: 8.53
--
-- Package per la gestione delle scritture COEP
--
-- Dependency: CNRCTB 001/002/008/038/100/200/204 IBMERR 001
--
-- History:
--
-- Date: 16/02/2002
-- Version: 1.0
-- Creazione
--
-- Date: 04/03/2002
-- Version: 1.1
-- Fix per gestone causali COGE
--
-- Date: 05/03/2002
-- Version: 1.2
-- Aggiunte scritture ultime
--
-- Date: 07/03/2002
-- Version: 1.3
-- Messaggistica per logs
--
-- Date: 10/03/2002
-- Version: 1.4
-- Introduzione job registrazione economica
--
-- Date: 28/03/2002
-- Version: 1.5
-- Introduzione del lock della testata del documento,
-- Introduzione del filtro (STATO_COGE, FL_MODIFICA_COGE)
--       sui documenti di cui bisogna effettuare la scrittura
-- Introduzione dell'aggiornamento dello stato coge per i documenti trattati
-- Introduzione del movimento COAN
--
-- Date: 03/05/2002
-- Version: 1.6
-- Gestione scritture COEP/COAN per UO e testata scrittura COAN
--
-- Date: 06/05/2002
-- Version: 1.7
-- UO e CDS della scrittura sono UO e CDS origine
--
-- Date: 08/05/2002
-- Version: 1.8
-- Riorganizzazione
--
-- Date: 15/05/2002
-- Version: 1.9
-- Corretto recupero del conto economico IVA con la sezeion del documento su effetto COEP CORI
--
-- Date: 15/05/2002
-- Version: 2.0
-- Introduzione contabilizzazione COEP del compenso
--
-- Date: 16/05/2002
-- Version: 2.1
-- Integrazione IVA a contabilizzazione CORI
--
-- Date: 16/05/2002
-- Version: 2.2
-- Gestione scrittura ultima anche per compenso
-- Snellito passaggio utuv/duva etc. a Scritt. movimenti
--
-- Date: 19/05/2002
-- Version: 2.3
-- Gestione sconti/abbuoni
--
-- Date: 20/05/2002
-- Version: 2.4
-- Fix errori su sconti/abbuoni
--
-- Date: 21/05/2002
-- Version: 2.5
-- Vendita di bene durevole
--
-- Date: 22/05/2002
-- Version: 2.6
-- Registrazione economica del fondo economale
--
-- Date: 27/05/2002
-- Version: 2.7
-- Gestione della non contabilizzazione doc. uscita da pgiro
--
-- Date: 28/05/2002
-- Version: 2.8
-- Corretta gestione dell'UO e CDS di appartenenza del documento
-- Aggiunta nuova gestione dell'origine del documento (DOC AMM DOC CONT)
-- Scrittura singola cassa/banca su fondo
--
-- Date: 29/05/2002
-- Version: 2.9
-- Eliminata prima scrittura coge per generico di apertura fondo
-- Aggiunto man_rev per discriminare tra tabella mandato e reversale
--
-- Date: 30/05/2002
-- Version: 3.0
-- Introduzione documento di anticipo su missione
--
-- Date: 31/05/2002
-- Version: 3.1
-- Test motore
--
-- Date: 31/05/2002
-- Version: 3.2
-- Gestione aggiornamento stato documenti direttamente nel motore
--
-- Date: 31/05/2002
-- Version: 3.3
-- Utilizzo Banca CDS su trasferimenti
-- Corretta gestione Banca CDS/Ente in scitture ultime
--
-- Date: 03/06/2002
-- Version: 3.4
-- Introduzione causali
--
-- Date: 06/06/2002
-- Version: 3.5
-- Se il documento amministrativo ? stato annullato, viene solo stornato e non riemesso in COGE
-- Aggiunta la data di esito doc. autorizzatorio in scrittura pagamento/incasso
--
-- Date: 07/06/2002
-- Version: 3.6
-- Gestione della missione
--
-- Date: 07/06/2002
-- Version: 3.7
-- Corretta la gestione della sezione principale del documento amministrativo
--
-- Date: 10/06/2002
-- Version: 3.8
-- Fix errore determinaz. contropartita
--
-- Date: 11/06/2002
-- Version: 3.9
-- Fix up COEP compenso
--
-- Date: 14/06/2002
-- Version: 4.0
-- La missione che transita per il compenso non va in COEP
--
-- Date: 19/06/2002
-- Version: 4.1
-- Fix - La missione senza anticipo non deve generare movimenti relativi all'anticipo
--
-- Date: 21/06/2002
-- Version: 4.2
-- Fix su gestione sconto/abbuono
--
-- Date: 27/06/2002
-- Version: 4.3
-- Fix recupero sezione per note di creadito/debito
--
-- Date: 28/06/2002
-- Version: 4.4
-- Fix recupero sezione per documento economico
--
-- Date: 28/06/2002
-- Version: 4.5
-- Fix su sezione notecreadito/debito
--
-- Date: 04/07/2002
-- Version: 4.6
-- Fix su errore reg ultima + dismissione bene duevole per vendita
--
-- Date: 04/07/2002
-- Version: 4.7
-- Registrazione COEP versamento ente mensile dell'IVA
--
-- Date: 06/07/2002
-- Version: 4.8
-- Aggiunta COEP per dismissione bene durevole
--
-- Date: 08/07/2002
-- Version: 4.9
-- Terzo 0 su registrazione dismissione bene durevole
--
-- Date: 16/07/2002
-- Version: 5.0
-- Fix estrazione scritture attive per documento
--
-- Date: 16/07/2002
-- Version: 5.1
-- Partita di giro e normale fuse in una sola scrittura
--
-- Date: 17/07/2002
-- Version: 5.2
-- Introdotto controllo che la scrittura coge sia effettivamente modificata
--
-- Date: 18/07/2002
-- Version: 5.3
-- Fix errori su controllo di scrittura modificata
--
-- Date: 18/07/2002
-- Version: 5.4
-- Se la lista dei movimenti da chiudere in contropartita ? vuota, il metodo
-- ceh costruisce il movimento di contropartita PEP ritorna subito senza errore
--
-- Date: 18/07/2002
-- Version: 5.5
-- Aggiornamento documentazione
--
-- Date: 30/07/2002
-- Version: 5.6
-- Fix-gestione compenso proveniente da missione con anticipo
--
-- Date: 31/07/2002
-- Version: 5.7
-- Fix estrazione dell'importo delle spese anticipate da anticipo
--
-- Date: 16/10/2002
-- Version: 5.8
-- Aggiornamento dello stato coge dei documenti dopo l'annullamento a 'C'
-- Aggiornamento ad X dello stato coge dei COGE esclusi (temporaneo)
--
-- Date: 25/10/2002
-- Version: 6.0
-- Registrazione economica su partita di giro
--
-- Date: 24/10/2002
-- Version: 5.9
-- Nuova gestione economica delle partite di giro
--
-- Date: 27/10/2002
-- Version: 6.1
-- Gestione corretta del recupero del conto di contropartita cliente/fornitore utilizzando la sezione principale del documento
-- Fix della documentazione
--
-- Date: 30/10/2002
-- Version: 6.2
-- Nuova gestione su documenti con CORI o IVA su partite di giro
--
-- Date: 04/11/2002
-- Version: 6.3
-- Gestione scritture speciali in deroga per esercizio inizio 2003 quando la competenza economica ? nel 2002 (fine periodo competenza)
--
-- Date: 07/11/2002
-- Version: 6.4
-- Fix messaggio di errore nel caso non venga trovato il conto di contropartita sul terzo
--
-- Date: 08/11/2002
-- Version: 6.5
-- Introduzione conto debiti iniziali
--
-- Date: 09/11/2002
-- Version: 6.6
-- Riorganizzata registrazione ultima
--
-- Date: 11/11/2002
-- Version: 6.7
-- Sistemati CORI IVA e RIVALSA su conto di costo compenso tranne che per IVA in compenso COMMERCIALE
-- Sistemato movimento anticipo sempre in dare in reg. economica compenso con anticipo (via missione)
--
-- Date: 17/11/2002
-- Version: 6.8
-- Gestione corretta versamenti CORI
-- Gestione economica compensi senza costo principale
--
-- Date: 18/11/2002
-- Version: 6.9
-- Package spaccato in 2 parti: parte di servizio spostata in CNRCTB204
--
-- Date: 19/11/2002
-- Version: 7.0
-- Fix su registrazione del compenso (nuova gestione cori)
--
-- Date: 26/11/2002
-- Version: 7.1
-- Gestione anticipi su missione e compenso completata
--
-- Date: 28/11/2002
-- Version: 7.2
-- Gestione tipo istituzionale/commerciale
--
-- Date: 06/12/2002
-- Version: 7.3
-- Fix scrittura con causale per differenze di cambio (su esitato non emesso)
--
-- Date: 11/12/2002
-- Version: 7.4
-- Fix interna su terzo UO
--
-- Date: 13/12/2002
-- Version: 7.5
-- Fix su contabil. compenso/missione senza anticipo
--
-- Date: 13/12/2002
-- Version: 7.6
-- Uso del lordo del mandato su movimento ultimo fornitore
--
-- Date: 23/12/2002
-- Version: 7.7
-- Concentrata scrittura ultima dcumenti di reintegro al momento di esito del mandato
--
-- Date: 08/01/2003
-- Version: 7.8
-- Fix su registrazione pagamenti (estrazione dettaglio coep per tipo doc contabile)
--
-- Date: 15/01/2003
-- Version: 7.9
-- Modifica scritture ultime per gestione COEP pagamenti per reintegri fondo economale fatti su documenti con terzi diversi
--
-- Date: 04/02/2003
-- Version: 8.0
-- Corretta gestione apertura debito verso erario su COEP compenso nel caso non esista mandato principale
-- Gestione del ricavo su documenti collegabili a obbligazione e accertamento (compenso):
-- nel caso importo documento COEP sia negativo, gestisce l'inversione della sezione e l'abs sull'importo
--
-- Date: 05/02/2003
-- Version: 8.1
-- Corretta generazione movimento anagrafica ENTE su CORI negativi ente
--
-- Date: 14/03/2003
-- Version: 8.2
-- Fix scrittura utile/perdita su cambi sotituito im_totale_fattura (che ? in divisa per la fattura passiva) con im_imponibile + im_iva
--
-- Date: 17/06/2003
-- Version: 8.3
-- Modificata la cont. econ. dei vers. CORi
--
-- Date: 17/06/2003
-- Version: 8.4
-- Fix su registrazione contr. compenso in scrittura ultima
--
-- Date: 19/06/2003
-- Version: 8.5
-- Tolto l'aggiornamento su duva e utuv nei documenti origine della scrittura
-- Nuova gestione  per CDS di origine del documento
--
-- Date: 08/07/2003
-- Version: 8.6
-- Aggiunte scritture speciali in esercizio precedente (ratei e fatt. da emettere) e scrittura risconti
-- Annullamento documenti di esercizi chiusi
--
-- Date: 09/07/2003
-- Version: 8.7
-- Gestione separata del rateo parte1 (registrazione economica del documento) e parte2 (alla chiusura)
---
-- Date: 12/07/2003
-- Version: 8.8
-- Creato il package 206 per le scritture di chiusura
--
-- Date: 15/07/2003
-- Version: 8.9
-- Modificato recupero conto di contropartita anagrafica
-- Aggiunto annullamento documenti di esercizi chiusi
-- Controllo apertura esercizio in scrittura ultima
-- Tolti controlli inutili su doc ultimo esitato visto che il processo di pagamento
-- in economica ? attivato solo al riscontro
-- Primo aggiornamento pre-post
--
-- Date: 16/07/2003
-- Version: 8.10
-- Completamento pre-post scrittura ultima
-- Fix errore in determinazione sezione movimento versamenti cori in scrittura ultima
--
-- Date: 17/07/2003
-- Version: 8.11
-- Introduzione  causali 1210, cassa banca e annullamento doc. esercizio chiuso
--
-- Date: 18/07/2003
-- Version: 8.12
-- Risistemazione completa della liquidazione mensile IVA e relative pre-post
--
-- Date: 01/08/2003
-- Version: 8.13
-- Modifice richiesta il 01/08/2003: nel caso la competenza econ. sia in es. prec. chiuso, la scrittura ? del tutto normale
--
-- Date: 06/08/2003
-- Version: 8.14
-- Lettura lockante del buono di scarico in dismissione bene durevole
-- Fix errore in economica liq. iva mensile
--
-- Date: 07/08/2003
-- Version: 8.15
-- Gestione speciale fatture istituzionali intraue/san marino senz'iva beni in scrittura ultima (RICH. CINECA del 06/08/2003)
-- Le commissioni 1210 sono annegate nella scrittura di utile perdita su cambi
--
-- Date: 07/08/2003
-- Version: 8.16
-- Correzione su gestione introdotta in versione 8.15
-- Sistemazione recupero configurazioni da esercizio mandato in scritture ultime e non da esercizio doc amministrativo
--
-- Date: 08/08/2003
-- Version: 8.17
-- Escluso da contabilizzazione prima il GENERICO DI ENTRATA PER IVA
-- Escluso da contabilizzazione ultima reversale di recupero IVA
--
-- Date: 25/08/2003
-- Version: 8.18
-- Fix errore n. 579
-- Reintrodotta la gestione in deroga del movimento di contropartita angrafica nel caso di prime scritture di doc. del primo anno
-- d'esercizio dell'applicazione con competenza economica in esercizio precedente
--
-- Date: 28/08/2003
-- Version: 8.19
-- Se il documento ? riportato anche parzialmente non pu? essere processato in economica (prima scrittura)
--
-- Date: 08/09/2003
-- Version: 8.20
-- Gestione completa del fl_congelata in annullamento fatture di esercizi precedenti chiusi
--
-- Date: 16/09/2003
-- Version: 8.21
-- Fix errore 641. in dismissione bene durevole: usare l'assestato e non il valore iniziale
--
-- Date: 12/11/2003
-- Version: 8.22
-- Fix errore 681 su dismissione bene durevole
-- Fix errore 682 su scrittura ultima di apertura del fondo economale
--
-- Date: 13/11/2003
-- Version: 8.23
-- Se l'esercizio n-1 non ? chiuso, il documento ? con comp. in es. prec, l'IVA su fatture attive (non note credeb) non viene gestita
-- infatti in questo caso  la scrittura viene fatta al 3112(n-1) come RATEO parte 1.
-- Ai controlli di chiusura dell'esercizio finanziario sono stati sostituiti quelli di chiusura definitiva di quello economico
-- Bloccate tutte le scritture COEP
--
-- Date: 13/11/2003
-- Version: 8.24
-- Fix recupero conto crediti da terzi nel caso di fattura attiva con competenza in esercizio precedente al primo esercizio
-- dell'applicazione
--
-- Date: 18/11/2003
-- Version: 8.24
-- Fix errore 686: per compensi senza mandato principale vengono create tante scritture quanti sono i CORI
-- Per ogni scrittura il terzo ? sempre quello del compenso tranne il caso in cui il CORI sia ENTE < 0
-- Viene utilizzato il conto CREDITORIO o DEBITORIO sulla base del segno del contributo ritenuta
-- Utilizzo del valore assestato del bene in dismissione per vendita
--
-- Date: 28/01/2004
-- Version: 8.25
-- Fix errori 756 e 757
-- Sistemazione scrittura CASSA BANCA su fondo economale
--
-- Date: 29/01/2004
-- Version: 8.26
-- Fix errori 756 e 757
-- Il mandato di regolarizzazione per chiusura fondo economale non effettua la scrittura banca cassa
--
-- Date: 19/02/2004
-- Version: 8.27
-- Aggiunta gestione estrazione giusto conto fatture da emettere/ricevere per ratei su fatture passive/note di credito/deb.
-- I ratei parte 1 devono poter essere registrati anche quando l'esercizio precedente NON E' CHIUSO DEFINITIVAMENTE
--
-- Date: 26/02/2004
-- Version: 8.28
-- Nuova gestione dell'economica della registrazione liquidazione IVA
-- Viene eseguita per tutte le UO tranne quella di versamento accentrato
-- Per l'UO di versamento accentrato, viene registrata la scrittura CONTO ERARIO IVA a BANCA
--
-- Date: 02/03/2004
-- Version: 8.29
-- Aggiunta gestione ISTITUZIONALE/COMMERCIALE corretta (n.685)
-- Nelle prime scritture: separazione movimenti istituzionali e commerciali di costo
-- Nelle prime scritture: gestione ti_istituz_commerc=P nel caso di documenti promiscui (fatture passive)
-- Nelle ultime scritture: il movimento anagrafico ? di tipo I/C/P a seconda del tipo ist/comm/prom del documento
-- amministrativo collegato alla riga di mandato/reversale.
-- Il movimento verso banca o cassa pone ti_istituz_commerc=P se il mandato copre documenti sia istituzionali che commerciali o documenti promiscui
-- Le altre scritture girano sempre come istituzionali (cori, versamenti, scritture cassa banca iva etc.)
--
-- Date: 05/03/2004
-- Version: 8.30
-- Richiesta n. 793 (gestione iva intra ue per beni istituzionale)
-- Gestione completa delle commissioni alla registrazione dell'ultima scrittura
--
-- Date: 12/03/2004
-- Version: 8.31
-- Fix errore su dismissione bene durevole: il movimento per beni deve usare l'assestato
-- prima della dismissione
-- Fix errore introdotto 02/2004, nelle fatture di vendita di beni l'IVA deve essere aggiunta nel caso il bene sia istituzionale al caricamento
--
-- Date: 15/03/2004
-- Version: 8.32
-- Gestione speciale righe di generico di spesa su capitoli di parte 1 nell'ente
-- Tali righe non generano movimenti
--
-- Date: 16/03/2004
-- Version: 8.33
-- Richiesta n. 801 - i mandati di regolarizzazione dell'ente fatti dall'ente sono
-- esclusi dalleconomica
--
-- Date: 22/04/2004
-- Version: 8.34
-- Sistemazione finale liquidazione IVA e scritture ultime su mandati di liquidazione IVA
-- La scrittura che gira il conto erario iva sulla banca viene fatta sul mandato di versamento del centro
-- La scrittura sui mandati di versamento iva collegati a generici di tipo TI_GEN_CORI_VER_SPESA vengono
-- comunque eseguite
--
-- Date: 23/04/2004
-- Version: 8.35
-- Le reversali di versamento CORI non venivano processate per errore in buildMovVersCoriUEP
--
-- Date: 18/05/2004
-- Version: 8.36
-- Modifica del 18/05/2004 err. 825 -> se la competenza ? in esercizio precedente e l'esercizio attuale ? il primo dell'applicazione
-- Il conto di costo/ricavo ? SP Iniziale
--
-- Date: 20/05/2004
-- Version: 8.37
-- Gestione registrazione economica del carico di bene mediante buono di carico non collegato a fattura
--
-- Date: 30/05/2004
-- Version: 8.38
-- Richiesta n. 693
-- Consolidamento scrittura economica carico bene mediante buono di carico non collegato a fattura
--
-- Date: 08/06/2004
-- Version: 8.39
-- Test richiesta 693 + sistemazione documentazione pre-post
--
-- Date: 22/06/2004
-- Version: 8.40
-- Errore 829 - il mandato di regolarizzazione per chiusura del fondo economale non fa cassa banca
-- Errore 830 - al posto di iva_credito va utilizzata l'iva a credito senza prorata (campo iva_credito_no_prorata della liquidazione)
--
-- Date: 10/08/2004
-- Version: 8.41
-- Fix errore 834 - sistemate le scritture di compensi con anticipi > dell'importo netto del compenso
--
-- Date: 25/08/2004
-- Version: 8.42
-- Completata la fix evitando di generare la scrittura del debito verso l'erario sul pagamento
-- Sistemato il recupero del conto DEBITI su di un compenso
-- Spostati i getAnticipo e check com con antic > netto comp su CNRCTB204
-- Riportate le modifica su 205EPR
--
-- Date: 06/09/2004
-- Version: 8.43
-- Modificata la scrittura ultima sulla lettera di pagamento estero
-- In particolare utile/perdita su cambi viene gestito per la differenza tra pagato-commissioni meno totale documentoa amm.
-- Le commissioni vanno in DARE e il debito sul terzo ? aperto per mandato-commissioni (viene generato un movimento
-- di storno per l'importo delle commissioni in AVERE sul conto del terzo)
--
-- Date: 24/09/2004
-- Version: 8.44
-- Richiets 843: I controlli sull'esercizio (finanziario ed economico) del documento in processo sono cambiati
-- La parte di controlli riguardante l'esercizio del documento (che ? quello di registrazione economica) sono
-- stati spostati nel metodo: CNRCTB204.checkChiusuraEsercizio
--
-- Date: 18/10/2004
-- Version: 8.45
-- Fix richiesta n. 844: corretta gestione conto commissioni+utilizzo netto iva in movmento sul terzo
-- nel caso di fattura passiva San Marino o intra ue che siano anche istituzionale e per acquisto di beni
--
-- Date: 05/11/2004
-- Version: 8.46
-- Modifiche minimali a regAnnullaDocEsChiuso (aggiunto il controllo che l'esercizio precedente a quello
-- in cui si ? effettuato l'annullamento sia chiuso definitivamente economicamente)
--
-- Date: 10/11/2004
-- Version: 8.47
-- Fix al controllo sullo stato riportato in scrivania in regAnnullaDocEsChiuso
--
-- Date: 10/11/2004
-- Version: 8.48
-- Nella registrazione annullamento doc. amm. esercizio chiuso, pone a C anche lo stato COAN del documento
-- Verifica che il documento sia stato processato almeno una volta in economica
--
-- Date: 12/11/2004
-- Version: 8.49
-- Inserimento, nel ciclo generali del job di registrazione COGE, dell'economica da migrazione inventario
--
-- Date: 16/11/2004
-- Version: 8.50
-- Errore in documentazione parametri per migrazione inventario e spegnimento controllo per storno scritture coge
-- (non applicabile alla gestione)
--
-- Date: 17/11/2004
-- Version: 8.51
-- Fix generazione economica per carico e dismissione bene durevole da trasferimento
--
-- Date: 17/11/2004
-- Version: 8.52
-- Fix errore in generazione coge da migrazione beni
--
-- Date: 14/07/2006
-- Version: 8.53
-- Gestione Impegni/Accertamenti Residui:
-- aggiornata la funzione per tener conto anche del campo Esercizio Originale Impegno/Accertamento
--
-- Constants:

-- Costanti relative alla dismissione del bene durevole

TI_DOC_FITT_SCR_BENE CONSTANT VARCHAR2(10) := 'DISMBD_SCR';

-- Costanti relative al caricamento del bene durevole

TI_DOC_FITT_CAR_BENE CONSTANT VARCHAR2(10) := 'CARIBD_SCR';

-- Costanti relative alla migrazione bene durevole

TI_DOC_FITT_MIGRA_BENE CONSTANT VARCHAR2(10) := 'MIGRBD_SCR';

-- Tipi CORI

TI_CORI_ENTE CONSTANT CHAR(1) := 'E';
TI_CORI_PERCIPIENTE CONSTANT CHAR(1) := 'P';

-- Functions e Procedures:

-- =============================================================
-- Registra in COEP la liquidazione ente mensile dell'IVA
-- =============================================================

-- Registrazione in COEP della liquidazione ente mensile dell.IVA
--
-- Registrazione in contabilit? economico patrimoniale della liquidazione ente mensile dell'IVA
--
-- pre-post-name: Controlli su esercizio finanziario ed economico di registrazione del documento
-- pre: L'esercizio contabile in cui effettuare la liquidazione non risulta aperto o
--      chiuso finanziariamente o risulta in fase di chiusura economica o chiuso economicamente in modo definitivo
-- post: Viene sollevata un'eccezione
--
-- pre-post-name: Esercizio economico precedente non chiuso definitivamente
-- pre: L'esercizio economico precedente a quello di origine del documento non ? chiuso definitivamente
-- post: Viene sollevata un'eccezione
--
-- pre-post-name: Esercizio economico precedente non chiuso definitivamente
-- pre: L'esercizio economico precedente a quello di origine del documento non ? chiuso definitivamente
-- post: Viene sollevata un'eccezione
--
-- pre-post-name: Parametri non specificati correttamente
-- pre: alcuni parametri della liquidazione IVA non sono stati specificati correttamente
-- post: viene sollevata un'eccezione
--
-- pre-post-name: L'UO di liquidazione ? l'ENTE
-- pre: l'UO della liquidazione IVA specificata ? l'UO ente
-- post: Viene generata una scrittura se esiste generico di versamento
--       che gira il conto voce ep erario iva sulla banca UO
--
-- pre-post-name: Liquidazione definitiva mensile IVA non trovata per il periodo di validit?
-- pre: non trova la liquidazione mensile IVA per il periodo di validit?
-- post: viene sollevata un'eccezione
--
-- pre-post-name: Operazione di contabilizzazione gi? effettuata
-- pre: Viene cercata e trovata una scrittura con origine LIQUID_IVA per la liquidazione IVA in processo
-- (campo cd_comp_documento)
-- post: viene sollevata un'eccezione
--
-- pre-post-name: Documento generico di versamento IVA non trovato
-- pre: l'iva non ? stata versata con la liquidazione
-- post:
--       il terzo delle scritture effettuate (solo quelle acquisti/vendite) = 0
--       la data di contabilizzazione = data di sistema
--
-- pre-post-name: Documento generico di versamento IVA trovato
-- pre: l'iva ? stata versata con la liquidazione
-- post:
--       il terzo delle scritture effettuate = terzo del generico di versamento
--       la data di contabilizzazione = data di registrazione del documento generico
--
-- pre-post-name: Liquidazione ente mensile dell'IVA: conto economico associato a terzo non trovato
-- pre: non trova il conto economico di contropartita associato al terzo
-- post: viene sollevata un'eccezione
--
-- pre-post-name: Liquidazione ente mensile dell'IVA
-- pre: non ? soddisfatta nessuna pre-post precedente
-- post: individua la liquidazione definitiva mensile IVA, individua il documento generico di versamento IVA
-- (se presente) e il dettaglio di versamento dello stesso.
-- Le scritture vengono tutte create con cds e uo = a quelli dell'UO di versamento IVA (mentre la liquidazione
-- ? fatta sull'uo ente).
-- Crea la scrittura per le vendite in partita doppia:
--    tipo -> definitivo,
--    riferimento a doc generico (nullo se tale documento non esiste)
--    cd_comp_documento -> una stringa di testo con la chiave della liquidazione cos? costuita cds-es-uo-dt_inizio-dt_fine
--    origine -> ORIGINE_LIQUID_IVA
--    causale -> LIQUID_IVA_VENDITE
--    movimenti
--         un movimento in DARE sul conto speciale dell'IVA a debito ed un movimento in AVERE
--         sul conto speciale erario c/IVA.
-- Da qui in poi l'importo di iva a credito deve essere senza il prorata (iva credito no prorata)
-- Crea la scrittura per gli acquisti in partita doppia:
--    tipo -> definitivo,
--    riferimento a doc generico (nullo se tale documento non esiste)
--    cd_comp_documento -> una stringa di testo con la chiave della liquidazione cos? costuita cds-es-uo-dt_inizio-dt_fine
--    origine -> ORIGINE_LIQUID_IVA
--    causale -> LIQUID_IVA_ACQUISTI
--    movimenti
--         un movimento in DARE sul conto speciale erario c/IVA ed un movimento in AVERE sul conto speciale dell'IVA
--         a credito
-- Se esiste il documento generico di versamento
--  Genera la scrittura in partita doppia per il saldo, la quota versata e la differenza (su conto di costo o ricavo)
--    tipo -> definitivo,
--    riferimento a doc generico
--    cd_comp_documento -> una stringa di testo con la chiave della liquidazione cos? costuita cds-es-uo-dt_inizio-dt_fine
--    origine -> ORIGINE_LIQUID_IVA
--    causale -> LIQUID_IVA_SALDO
--    movimento 1
--	  	   importo = valore assoluto del saldo crediti/debiti
--         se il saldo debito credito ? negativo
--          un movimento in AVERE sul conto speciale erario c/IVA
--         altrimenti
--          un movimento in DARE sul conto speciale erario c/IVA
--    movimento 2
--	  	   importo = valore assoluto dell' (importo mandato versamento - saldo crediti/debiti)
--         se la differenza tra l'importo del mandato di versamento e il saldo debiti/crediti < 0
--          un movimento in AVERE sul conto di ricavo identificato in configurazione CNR (RICAVO_IVA_NON_DETRAIBILE)
--         altrimenti
--          un movimento in DARE sul conto di costo identificato in configurazione CNR (COSTO_IVA_NON_DETRAIBILE)
--    movimento 3
--         importo = importo del mandato di versamento
--         movimento in dare sul conto debiti da erario (identificato via terzo mandato di versamento)
-- Altrimenti
--  Genera la scrittura in partita doppia per il saldo, la quota versata e la differenza (su conto di costo o ricavo)
--    tipo -> definitivo,
--    riferimento a doc generico
--    cd_comp_documento -> una stringa di testo con la chiave della liquidazione cos? costuita cds-es-uo-dt_inizio-dt_fine
--    origine -> ORIGINE_LIQUID_IVA
--    causale -> LIQUID_IVA_SALDO
--    movimento 1
--	  	   importo = valore assoluto del saldo crediti/debiti
--         se il saldo debito credito ? negativo
--          un movimento in AVERE sul conto speciale erario c/IVA
--         altrimenti
--          un movimento in DARE sul conto speciale erario c/IVA
--    movimento 2
--	  	   importo = valore assoluto del saldo crediti/debiti
--         se la differenza tra l'importo del mandato di versamento e il saldo debiti/crediti < 0
--          un movimento in AVERE sul conto di ricavo identificato in configurazione CNR (RICAVO_IVA_NON_DETRAIBILE)
--         altrimenti
--          un movimento in DARE sul conto di costo identificato in configurazione CNR (COSTO_IVA_NON_DETRAIBILE)
--
-- Al pagamento del mandato viene registrata in automatico la scrittura ultima terzo su banca secondo le regole del batch
--
-- Parametri:
-- aEs -> esercizio di riferimento
-- aCdCds -> CdS che effettua la liquidazione dell.IVA
-- aCdUo -> unit? organizzativa che effettua la liquidazione dell.IVA
-- aTipo -> tipo di liquidazione (I, S, C)
-- aDtDa -> data di inizio validit?
-- aDtA -> data di fine validit?
-- aUser -> utente che effettua la modifica
--

 procedure regLiqIvaMensileCOGE(aEs number, aCds varchar2, aUO varchar2, aTipo char, aDtDa date, aDtA date, aUser varchar2);

 function getDesc(aLiqIva liquidazione_iva%rowtype) return VARCHAR2;

-- =============================================================
-- Registra in COEP la dismissione di bene durevole
-- =============================================================

-- Dismissione di un bene durevole senza riferimenti esterni
--
-- Registrazione in contabilit? economica patrimoniale della dismissione di un bene durevole senza riferimenti esterni,
-- ovvero senza bisogno di movimenti finanziari n? documenti amministrativi, oppure per obsolescenza.
--
-- pre-post-name: Controlli su esercizio finanziario ed economico di registrazione del documento
-- pre: L'esercizio contabile in cui effettuare la liquidazione non risulta aperto o
--      chiuso finanziariamente o risulta in fase di chiusura economica o chiuso economicamente in modo definitivo
-- post: Viene sollevata un'eccezione
--
-- pre-post-name: Esercizio economico precedente non chiuso definitivamente
-- pre: L'esercizio economico precedente a quello di origine del documento non ? chiuso definitivamente
-- post: Viene sollevata un'eccezione
--
-- pre-post-name: Dismissione di un bene durevole: bene non trovato
-- pre: bene non trovato in inventario per numero progressivo d.inventario
-- post: viene sollevata un.eccezione
--
-- pre-post-name: Dismissione di un bene durevole: gruppo inventariale non trovato
-- pre: il bene non ha un gruppo inventariale corrispondente in CATEGORIA_GRUPPO_INVENT
-- post: viene sollevata un.eccezione
--
-- pre-post-name: Dismissione di un bene durevole: nessun conto economico associato a categoria
-- pre: non esiste un conto economico associato alla categoria di appartenenza del bene
-- post: viene sollevata un.eccezione
--
-- pre-post-name: Dismissione di un bene durevole
-- pre: esiste un bene nell.inventario relativo al buono, esiste un gruppo inventariale relativo al bene, esiste un
-- conto economico associato al gruppo inventariale
-- post: per ogni buono di scarico non contabilizzato oppure da contabilizzare nuovamente in COGE, determina il conto
-- economico e il conto economico di contropartita associato alla categoria del bene nella tabella VOCE_EP. Calcola il
-- residuo del valore del bene: se positivo, genera un movimento principale in DARE sul conto economico speciale della
-- minus valenza. Genera un movimento principale in AVERE sul conto economico della categoria gruppo inventario per il
-- valore iniziale del bene. Se il valore ammortizzato del bene ? positivo, genera un movimento principale in DARE sul
-- conto economico di contropartita associato alla categoria del bene stesso per il valore ammortizzato.
-- Crea la scrittura in partita doppia (causale coge di tipo dismissione di bene durevole). Aggiorna lo stato COGE del
-- buono di scarico, definendolo come processato.
--
-- Parametri:
-- aEs -> esercizio di riferimento
-- aPgInv -> numero progressivo del bene in inventario
-- aUser -> utente che effettua le modifiche

 procedure regDismBeneDurevoleCOGE(aTBS buono_carico_scarico_dett%rowtype, aUser varchar2, aTSNow date);

-- =============================================================
-- Registra in COEP il carico di un bene durevole
-- =============================================================

-- Caricamento di un bene durevole senza riferimenti esterni
--
-- Registrazione in contabilit? economica patrimoniale del caricamento senza riferimenti esterni,
-- ovvero senza bisogno di movimenti finanziari n? documenti amministrativi.
--
-- pre-post-name: Controlli su esercizio finanziario ed economico di registrazione del documento
-- pre: L'esercizio contabile in cui effettuare la liquidazione non risulta aperto o
--      chiuso finanziariamente o risulta in fase di chiusura economica o chiuso economicamente in modo definitivo
-- post: Viene sollevata un'eccezione
--
-- pre-post-name: Esercizio economico precedente non chiuso definitivamente
-- pre: L'esercizio economico precedente a quello di origine del documento non ? chiuso definitivamente
-- post: Viene sollevata un'eccezione
--
-- pre-post-name: Caricamento di un bene durevole: bene non trovato
-- pre: bene non trovato in inventario per numero progressivo d.inventario
-- post: viene sollevata un.eccezione
--
-- pre-post-name: Caricamento di un bene durevole: gruppo inventariale non trovato
-- pre: il bene non ha un gruppo inventariale corrispondente in CATEGORIA_GRUPPO_INVENT
-- post: viene sollevata un.eccezione
--
-- pre-post-name: Caricamento di un bene durevole: nessun conto economico associato a categoria
-- pre: non esiste un conto economico associato alla categoria di appartenenza del bene
-- post: viene sollevata un.eccezione
--
-- pre-post-name: Caricamento di un bene durevole
-- pre: esiste un bene nell.inventario relativo al buono, esiste un gruppo inventariale relativo al bene, esiste un
-- conto economico associato al gruppo inventariale
-- post: per ogni buono di carico non contabilizzato oppure da contabilizzare nuovamente in COGE, determina il conto
-- economico associato alla categoria del bene nella tabella VOCE_EP.
-- Crea un movimento in DARE sul conto recuperato utilizzando l'importo del BUONO di carico (valore unitario*quantit?)
-- Crea un movimento in AVERE sul conto sopravvenienze attive che chiude la scrittura
-- Crea la scrittura in partita doppia (utilizza la causale di caricamento del bene durevole). Aggiorna lo stato COGE del
-- buono di carico, definendolo come processato.
--
-- Parametri:
-- aEs -> esercizio di riferimento
-- aPgInv -> numero progressivo del bene in inventario
-- aUser -> utente che effettua le modifiche

 procedure regCaricBeneDurevoleCOGE(aTBS buono_carico_scarico_dett%rowtype, aUser varchar2, aTSNow date);

-- =============================================================
-- Registra in COEP la migrazione beni durevoli
-- =============================================================

-- Caricamento di un bene migrato in inventario
--
-- Registrazione in contabilit? economica patrimoniale del caricamento senza riferimenti esterni,
-- ovvero senza bisogno di movimenti finanziari n? documenti amministrativi.
--
-- pre-post-name: Controlli su esercizio finanziario ed economico di registrazione del documento
-- pre:           L'esercizio contabile in cui effettuare la liquidazione non risulta aperto o
--                chiuso finanziariamente o risulta in fase di chiusura economica o chiuso economicamente in modo definitivo
-- post:          Viene sollevata un'eccezione
--
-- pre-post-name: Esercizio economico precedente non chiuso definitivamente
-- pre:           L'esercizio economico precedente a quello di origine del documento non ? chiuso definitivamente
-- post:          Viene sollevata un'eccezione
--
-- pre-post-name: Caricamento di un bene durevole migrato: gruppo inventariale non trovato
-- pre:           Il bene non ha un gruppo inventariale corrispondente in CATEGORIA_GRUPPO_INVENT
-- post:          Viene sollevata un.eccezione
--
-- pre-post-name: Caricamento di un bene durevole migrato: nessun conto economico associato a categoria
-- pre:           Non esiste un conto economico associato alla categoria di appartenenza del bene
-- post:          viene sollevata un.eccezione
--
-- pre-post-name: Caricamento di un bene durevole migrato
-- pre:   Genera economica per beni durevoli migrati, input da CNR_INVENTARIO_BENI_MIG
-- post:  Per ogni blocco UO e categoria gruppo non contabilizzato oppure da contabilizzare nuovamente in COGE, determina
--        il conto economico associato alla categoria del bene nella tabella VOCE_EP.
--        Crea un movimento in DARE sul conto recuperato utilizzando l'importo assestato
--        Crea un movimento in AVERE sul conto contropartita della categoria gruppo se il valore ammortizzato ? diverso da 0.
--        Crea un movimento in AVERE sul conto speciale recuperato da CONFIGURAZIONE_CNR (CONTROPARTITA_MIGRAZIONE_BENI) con
--        i seguenti importi:
--           Se valore ammortizzato = 0 allora importo assestato
--           Se valore ammortizzato > 0 allora importo assestato - valore ammortizzato.
--        Crea la scrittura in partita doppia (utilizza la causale di caricamento del bene durevole). Aggiorna lo stato
--        COGE del gruppo UO e categoria gruppo definendolo come processato.
--
-- Parametri:
-- aEs -> esercizio di riferimento
-- aCdCds -> Cds di riferimento
-- aCdUo -> UO di riferimento
-- aCdCategoriaGruppo -> Codice categoria gruppo
-- aValoreAssestato -> Valore assestato del bene
-- aValoreAmmortizzato -> Valore ammortizzato
-- aUser -> utente che effettua le modifiche
-- aTSNow -> Data di riferimento

 procedure regMigrazioneBeniCoge(aEs NUMBER,
                                 aCdCds VARCHAR2,
                                 aCdUo VARCHAR2,
                                 aCdCategoriaGruppo VARCHAR2,
                                 aValoreAssestato NUMBER,
                                 aValoreAmmortizzato NUMBER,
                                 aUser VARCHAR2,
                                 aTSNow DATE,
                                 aStato CHAR);

-- =============================================================
-- Registra in COEP un documento amministrativo
-- =============================================================

-- Registrazione scritture prime in COEP di un documento amministrativo
--
-- Effettua la registrazione della scrittura prima in partita doppia di un documento amministrativo.
-- Riceve in ingresso la testata del documento, estratta dalla vista V_DOC_AMM_TSTA, interfaccia uniforme dei
-- documenti amministrativi FATTURA_PASSIVA, FATTURA_ATTIVA, DOCUMENTO_GENERICO, COMPENSO, ANTICIPO, RIMBORSO.
-- Utilizza la vista V_DOC_AMM_COGE_RIGA per recuperare l.associazione tra conto finanziario e conto economico,
-- la vista ? un.estrazione dei dettagli dei documenti amministrativi (FATTURA_PASSIVA, FATTURA_ATTIVA,
-- DOCUMENTO_GENERICO, COMPENSO, ANTICIPO, RIMBORSO) con valorizzazione ai fini COGE.
-- Viene utilizzata la tabella ASS_ANAG_VOCE_EP per le associazioni tra l'anagrafico ed il codice economico,
-- individuato grazie alla chiava primaria definita su ESERCIZIO, TI_TERZO dalla sezione economica principale
-- del documento (creditorio -> C debitorio -> D, no asterisco no entrambi E), ITALIANO_ESTERO, TI_ENTITA (entit?
-- giuridica, entit? fisica, unit? organizzativa o diversi), ENTE_ALTRO (solo per entit? giuridiche distiguo in enti
-- pubblici o altro) e CD_CLASSIFIC_ANAG (codice di classificazione). Si tratta di una tabella che supporta gli
-- asterischi:
-- i record di una tabella vengono ordinati secondo la definizione della primary key. I primi elementi sono quelli
-- con i campi definiti (non asterischi) nella primary key, seguono poi quelli con asterischi e l.ordine segue la
-- sequenza dei campi della primary key. Esempio di ordinamento: ESERCIZIO	TI_TERZO	ITALIANO_ESTERO	TI_ENTITA
-- ENTE_ALTRO	CD_CLASSIFIC_ANAG	...
-- 2002	C	I	G	P	CONSORZIO	...
-- 2002	C	I	D	*	*	...
-- 2002	C	I	*	*	*	...
--
-- pre-post-name: Documenti amministrativi che non vanno in COGE come prime scritture
-- pre: il documento amministrativo ? un generico di entrata/spesa di accreditamenti dal CNR al CdS,
-- o generico di reintegro/apertura fondo economale, oppure si tratta di missione che transita per il compenso
-- post: viene aggiornato lo stato COGE del documento a X e il programma termina senza sollevare eccezioni
--
-- pre-post-name: Documento da non processare
-- pre: Lo stato coge del documento amministrativo non ? N (da processare) o R (da riprocessare)
-- post: il programma termina senza eccezioni
--
-- pre-post-name: Controlli su esercizio finanziario ed economico di registrazione del documento
-- pre: L'esercizio contabile in cui effettuare la liquidazione non risulta aperto o
--      chiuso finanziariamente o risulta in fase di chiusura economica o chiuso economicamente in modo definitivo
-- post: Viene sollevata un'eccezione
--
-- pre-post-name: Esercizio economico precedente non chiuso definitivamente
-- pre: L'esercizio economico precedente a quello di origine del documento non ? chiuso definitivamente
-- post: Viene sollevata un'eccezione
--
-- pre-post-name: Documento (parzialmente) riportato in nuovo esercizio
-- pre: Il documento ? associato a doc contabili riportati a nuovo esercizio
-- post: Viene sollevata un'eccezione
--
-- pre-post-name: Esistono scritture gi? registrate per il documento amministrativo
-- pre: esistono scritture gi? registrate per il documento amministrativo e tali scritture sono DIVERSE da quelle
-- nuove generate per il documento amministrativo
-- post: viene creato uno storno per ogni scrittura e viene poi emessa la scrittura aggiornata, opposta in sezione
-- alla scrittura originale
--
-- pre-post-name: Documento amministrativo annullato
-- pre: il documento amministrativo ? annullato
-- post: viene creata solo una scrittura di storno
--
-- pre-post-name: Gestione vendita bene durevole in fattura attiva
-- pre: il documento amministrativo ? di tipo fattura attiva e la causale dell.emissione ? per beni durevoli, il
-- documento ammette un.associazione con l.inventario beni (ASS_INV_BENI_FATTURA)
-- post: generazione dei movimenti di minus/plus valenza del bene estraendo il conto economico speciale in minus/plus
-- valenza dalla configurazione del CNR. Generazione dei movimenti per conto economico di minus/plus valenza: il movimento
-- ? in sezione AVERE se in plus valenza, in sezione DARE se in minus valenza. Generazione dei movimenti principali
-- per il valore iniziale e per il valore ammortizzato del bene.
-- Vedi Generazione movimenti e creazione scrittura nella pre-post Creazione della scrittura prima in partita doppia
-- per il documento amministrativo (situazione generale non su partita di giro).
--
-- pre-post-name: Gestione vendita bene durevole in fattura attiva  bene non trovato
-- pre: il documento amministrativo ? di tipo fattura attiva e la causale dell.emissione ? per beni durevoli, il bene
-- non ha un.associazione con l.inventario (ASS_INV_BENI_FATTURA)
-- post: viene sollevata un.eccezione
--
-- pre-post-name: Gestione vendita bene durevole in fattura attiva  gruppo inventariale non trovato
-- pre: il documento amministrativo ? di tipo fattura attiva e la causale dell.emissione ? per beni durevoli, il bene
-- non ha un gruppo inventariale corrispondente in CATEGORIA_GRUPPO_INVENT
-- post: viene sollevata un.eccezione
--
-- pre-post-name: Gestione vendita bene durevole in fattura attiva  nessun conto economico associato alla categoria
-- pre: il documento amministrativo ? di tipo fattura attiva e la causale dell.emissione ? per beni durevoli, il bene
-- appartiene ad una categoria priva di associazione con il conto economico in CATEGORIA_GRUPPO_VOCE_EP
-- post: viene sollevata un.eccezione
--
-- pre-post-name: Creazione della scrittura prima in partita doppia per il documento amministrativo di tipo missione
-- pre: il documento amministrativo ? di tipo missione
-- post: generazione dei movimenti principali della scrittura avendo determinato il conto economico associato dalla
-- configurazione del CNR: se l.anticipo ? minore della missione, l.anticipo va in AVERE; se l.anticipo ? maggiore
-- della missione, l.anticipo va in DARE. Generazione dei movimenti della scrittura in DARE per l.importo totale della
-- missione, avendo determinato il conto economico associato da ASS_EV_VOCEEP per esercizio, gestione, appartenenza e
-- conto finanziario del documento amministrativo.
-- Vedi Generazione movimenti e creazione scrittura nella pre-post Creazione della scrittura prima in partita doppia
-- per il documento amministrativo (situazione generale non su partita di giro).
--
-- pre-post-name: Creazione della scrittura prima in partita doppia per il documento amministrativo di tipo compenso
-- collegato a missione con anticipo
-- pre: il documento amministrativo ? di compenso ed ? collegato a missione e la missione ha un importo di spese
-- anticipate non nullo
-- post: il compenso viene contabilizzato COEP come un normale compenso. La scrittura viene estesa con un movimento
-- relativo all'anticipo secondo le stesse regole
-- utilizzate per la gestione dell'anticipo su missione. Per l'applicazione di tali regole al posto dell'importo della
-- missione viene utilizzato l'importo lordi del compenso.
--
-- pre-post-name: Documento amministrativo di tipo missione non trovato
-- pre: il documento amministrativo di tipo missione non ? trovato
-- post: viene sollevata un.eccezione
--
-- pre-post-name: Documento amministrativo privo di associazione a conto economico
-- pre: il documento amministrativo non ? un documento di uscita di partita di giro, non ha un.associazione con un
-- conto economico in VOCE_EP (per documenti amministrativi di tipo MISSIONE, per documenti amministrativi di tipo
-- FATTURA PASSIVA con riga di dettaglio di sconto o abbuono) o in ASS_EV_VOCE_EP (per tutti gli altri documenti)
-- post: viene sollevata un.eccezione
--
-- pre-post-name: Documento amministrativo privo di associazione cori/coep
-- pre: il documento amministrativo non ? un documento di uscita di partita di giro, ma non c'? un.associazione
-- cori/coep per il documento in ASS_TIPO_CORI_VOCE_EP
-- post: viene sollevata un.eccezione
--
-- pre-post-name: Documento amministrativo privo di conto economico associato a contributo ritenuta
-- pre: il documento amministrativo non ? un documento di uscita di partita di giro, esiste un.associazione cori/coep
-- in ASS_TIPO_CORI_VOCE_EP, non viene trovato il conto economico associato al contributo ritenuta in VOCE_EP
-- post: viene sollevata un.eccezione
--
-- pre-post-name: Costruzione del movimento di contropartita per la scrittura prima per la quadratura della scrittura
-- non esistono movimenti
-- pre: documento amministrativo non ? un documento di uscita di partita di giro, la lista dei movimenti ? vuota
-- post: viene sollevata un.eccezione
--
-- pre-post-name: Creazione della scrittura prima in partita doppia per il documento amministrativo su partita di giro,
-- non esiste un.associazione con la partita di giro
-- pre: i dettagli del documento amministrativo sono su partita di giro, il documento amministrativo non ammette
-- un'associazione con la partita di giro per esercizio, CdS, numero progressivo del documento amministrativo
-- (di accertamento o obbligazione) e per sezione corrispondente (in ASS_OBB_ACR_PGIRO).
-- post: non vengono generate scritture
--
-- pre-post-name: Creazione della scrittura prima in partita doppia per il documento amministrativo su partita di
-- giro - conto economico su partita di giro non trovato
-- pre: i dettagli del documento amministrativo sono su partita di giro, il documento amministrativo ammette un'associazione
-- con la partita di giro per esercizio, CdS, numero progressivo del documento amministrativo (di accertamento o
-- obbligazione) e per sezione corrispondente (in ASS_OBB_ACR_PGIRO). Non viene individuato il conto economico per
-- il costo e/o per il ricavo in ASS_PARTITA_GIRO e/o in ASS_EV_VOCEEP.
-- post: viene sollevata un.eccezione
--
-- pre-post-name: Creazione della scrittura prima in partita doppia per il documento amministrativo (situazione
-- generale su partita di giro)
-- pre: i dettagli del documento amministrativo sono su partita di giro, il documento amministrativo ammette un'associazione
-- con la partita di giro per esercizio, CdS, numero progressivo del documento amministrativo (di accertamento o
-- obbligazione) e per sezione corrispondente (in ASS_OBB_ACR_PGIRO).
-- post: generazione di movimenti principali in DARE per il costo e di movimenti principali in AVERE per il ricavo.
-- Il conto economico del costo e del ricavo sono determinati dalla tabella VOCE_EP, ma in funzione del tipo di gestione:
-- se si tratta di entrate, per il costo si individua preventivamente il collegamento con il conto su partita di giro
-- in ASS_PARTITA_GIRO e quindi  attraverso la tabella ASS_EV_VOCEEP. Se si tratta di spese, questa operazione viene fatta per il ricavo.
-- Creazione e inserimento della scrittura prima in partita doppia (con numerazione della scrittura inserita):
-- inserisce la testata, i movimenti della scrittura e aggiorna i saldi.
-- Aggiorna per chiave primaria la testata del documento.
--
-- pre-post-name: Creazione della scrittura prima in partita doppia per il documento amministrativo (situazione
-- generale - non su partita di giro)
-- pre:  i dettagli del documento amministrativo non sono su partita di giro
-- post: generazione dei movimenti di default, individuando il conto economico da ASS_EV_VOCEEP per esercizio,
-- gestione, appartenenza e conto finanziario del documento amministrativo, e determinando la sezione del documento amministrativo: se esiste gi? un movimento con lo stesso conto economico, sezione e intervallo temporale di competenza, viene sommato l.importo al movimento esistente; se non esiste gi? un movimento, viene creato ed aggiunto alla lista dei movimenti. Per il compenso, l'importo del movimento ? il lordo del percipiente.
-- Vengono create tante scritture quante sono le entit? anagrafiche specificate (individuate tramite la vista
-- V_DOC_AMM_COGE_RIGA).
-- Generazione movimenti e creazione scrittura:
-- Generazione dei movimenti di contributi ritenuta e IVA del documento individuando il conto economico attraverso
-- l.associazione tra elemento voce e contributi ritenuta in ASS_TIPO_CORI_VOCE_EP: se esiste gi? un movimento con lo
-- stesso conto economico, sezione e intervallo temporale di competenza, viene sommato l.importo al movimento esistente; se non esiste gi? un movimento, viene creato ed aggiunto alla lista dei movimenti. L'importo del movimento ? dato dal totale dell'ammontare dei contributi ritenuta.
-- Costruzione del movimento di contropartita per la scrittura prima per la quadratura della scrittura, individuando
-- il conto economico attraverso l.anagrafica completa del terzo nella vista V_ANAGRAFICO_TERZO e la tabella
-- ASS_ANAG_VOCE_EP, associazione fra anagrafico e elemento voce. Se il totale DARE dei movimenti supera il totale
-- AVERE, la sezione principale ? DARE (contropartita in AVERE). Se il totale AVERE dei movimenti supera il totale
-- DARE, la sezione principale ? AVERE (contropartita in DARE). Se la partita doppia ? gi? chiusa non devo generare
-- movimenti.
-- Creazione e inserimento della scrittura prima in partita doppia (con numerazione della scrittura inserita):
-- inserisce la testata, i movimenti della scrittura e aggiorna i saldi.
-- Aggiorna per chiave primaria la testata del documento.
--
-- pre-post-name: Scrittura di rateo parte 1
-- pre: Il documento amministrativo ? creato con competenza economica in esercizio aperto precedente a quello del
-- documento
-- post: Viene creata una scrittura economica uguale ad un scrittura dell'anno con le seguenti deroghe:
--           1. la scrittura ? registrata nell'esercizio precedente a quello del documento amministrativo
--           2. al posto del conto creditore/debitore, viene utilizzato l'opportuno conto dei ratei attivi/passivi
-- recuperato da CONFIGURAZIONE_CNR
--           3. la data di contabilizzazione della scrittura ? 3112<anno precedente a quello del documento>
--       Lo stato COGE del documento viene aggiornato di conseguenza
--
-- Parametri:
-- aDocTst ->Testata di un documento amministrativo, estratta dalla vista V_DOC_AMM_COGE_TSTA
-- aUser -> Utente che effettua la variazione
-- aTSNow -> Timestamp modifica

procedure regDocAmmCOGE(aDocTst V_DOC_AMM_COGE_TSTA%rowtype, aUser varchar2, aTSNow date);

-- Parametri:
-- aEs -> Esercizio documento amministrativo
-- aCds -> Codice del CDS documento amministrativo
-- aUO -> Codice unit? organizzativa documento amministrativo
-- aTiDocumento -> Tipo documento amministrativo
-- aPgDocAmm -> Progressivo del documento amministrativo
-- aUser -> Utente che effettua la variazione
-- aTSNow -> Timestamp modifica

procedure regDocAmmCOGE(aEs number, aCds varchar2, aUO varchar2, aTiDocumento varchar2, aPgDocAmm number, aUser varchar2, aTSNow date);

-- =============================================================
-- Registra il pagamento di un documento amministrativo in COGE
-- =============================================================

-- Registrazione scritture ultime e singole in COEP di un documento autorizzatorio
--
-- Registrazione del pagamento di un documento autorizzatorio: scritture ultime e singole.
-- Riceve in ingresso la testata di un documento, estratta dalla vista V_DOC_ULT_COGE_TSTA, interfaccia uniforme dei documenti autorizzatori (MANDATO e REVERSALE).
--
-- pre-post-name: Stato documento autorizzatorio non compatibile
-- pre: Lo stato del mandato/reversale non ? ESITATO o ANNULLATO
-- post: Il programma termina senza sollevare eccezioni
--
-- pre-post-name: Controlli su esercizio finanziario ed economico di registrazione del documento
-- pre: L'esercizio contabile in cui effettuare la liquidazione non risulta aperto o
--      chiuso finanziariamente o risulta in fase di chiusura economica o chiuso economicamente in modo definitivo
-- post: Viene sollevata un'eccezione
--
-- pre-post-name: Esercizio economico precedente non chiuso definitivamente
-- pre: L'esercizio economico precedente a quello di origine del documento non ? chiuso definitivamente
-- post: Viene sollevata un'eccezione
--
-- pre-post-name: Documento non supportato
-- pre: il tipo di documento autorizzatorio non ? supportato
-- post: viene sollevata un.eccezione
--
-- pre-post-name: Documento autorizzatorio contabilizzato in COGE oppure da non contabilizzare
-- pre: il documento autorizzatorio ? gi? contabilizzato in COGE oppure non ? da contabilizzare
-- post: Il programma termina senza sollevare eccezioni
--
-- pre-post-name: Documento autorizzatorio annullato
-- pre: il documento autorizzatorio ? stato annullato
-- post: viene creato uno storno per ogni scrittura ultima e singola (di tipo Banca/Cassa) gi? registrata emettendo
-- la scrittura opposta in sezione alla scrittura originale.
-- Vedi pre-post Aggiornamento dello stato COGE.
--
-- pre-post-name: Stato pagamento fondo economale non trovato
-- pre: Non viene determinato lo stato pagamento fondo economale dei documenti amministrativi collegati al doc. aturoizzatorio
-- post: Viene sollevata un'eccezione
--
-- pre-post-name: Terzo non trovato nel documento autorizzatorio
-- pre: il documento autorizzatorio non risulta annullato, non risulta un terzo associato al tipo di
-- documento per l'esercizio, il centro di spesa e il progressivo del documento contabile.
-- post: viene sollevata un'eccezione
--
-- pre-post-name: Creazione ed inserimento di scrittura ultima o singola in COGE  utente non specificato
-- pre: non viene specificato l'utente che effettua la modifica
-- post: viene sollevata un'eccezione
--
-- pre-post-name: Conto economico non trovato
-- pre: non viene trovato il conto economico nella tabella VOCE_EP
-- post: viene sollevata un'eccezione
--
-- pre-post-name: Documento amministrativo associato al documento autorizzatorio non trovato
-- pre: nella vista V_DOC_AMM_COGE_TSTA non viene trovato il documento amministrativo associato al documento
-- autorizzatorio per centro di spesa, esercizio, tipo di documneto contabile,
-- unit? organizzativa e numero progressivo del documento.
-- post: viene sollevata un'eccezione
--
-- pre-post-name: Documento amministrativo GENERICO di reintegro fondo economale
-- pre: il documento amministrativo collegato al documento autorizzatorio ? generico di reintegro fondo economale
-- post: nelle pre-post Documento autorizzatorio associato a fondo economale, la scrittura di default che viene
-- registrata ? di tipo singolo e non di tipo ultimo.
--
-- pre-post-name: Documento amministrativo GENERICO di VERSAMENTO ENTRATA O SPESA
-- pre: il documento amministrativo collegato al documento autorizzatorio ? generico di VERSAMENTO ENTRATA o SPESA
--      viene chiuso il debito/credito verso l'erario con il versamento
-- post: il movimento principale ? gestito in deroga come segue:
--       1. Determina a partire dalla riga di doc generico l'obbligazione o accertamento che apre la partita di giro del versamento (se non trova esce senza sollevare eccezioni)
--       2. Dall'accertamento/obbligazione risale al contributo ritenuta versato (se non trova esce senza sollevare eccezioni)
--       3. Determina l'associazione tra CORI e VOCE ECONOMICA tramite tabella ASS_TIPO_CORI_VOCE_EP
--          entrando con esercizio,codice cori,ente/percipiente,sezione (DARE se >0 AVERE viceversa)
--       4. Dall'associazione estrae il conto di contropartita patrimoniale associato al CORI
--       5. La sezione del movimento ? DARE se cori >0 AVERE se CORI <0
--
-- pre-post-name: Documento autorizzatorio di tipo mandato di accreditamento
-- pre: il documento autorizzatorio ? di tipo mandato di accreditamento, generato in automatico da trasferimento
-- CNR --> CdS. Esiste solo per bilancio Cnr.
-- post: il conto utilizzato per il movimento principale ? banca CdS.
--
-- pre-post-name: Documento amministrativo di tipo COMPENSO
-- pre: il documento amministrativo collegato al documento autorizzatorio ? di tipo COMPENSO,
--      viene aperto il debito/credito verso l'erario
-- post: Si ha una diversa generazione dei movimenti di contropartita secondo il seguente schema:
--       Determina tutti i CORI del compenso
--        Per ogni CORI
--         Se il CORI non ? IVA o RIVALSA (identificati tramite classificazione CORI)
--          1. Determina l'associazione tra CORI e VOCE ECONOMICA tramite tabella ASS_TIPO_CORI_VOCE_EP
--             entrando con esercizio,codice cori,ente/percipiente,sezione (DARE se >0 AVERE viceversa)
--          2. Dall'associazione estrae il conto di contropartita patrimoniale associato al CORI
--          3. La sezione del movimento ? DARE se cori <0 AVERE se CORI >0
--
-- pre-post-name: Individuazione conto economico di contropartita
-- pre: ? necessario individuare il conto economico di contropartita
-- post: nelle pre-post Documento autorizzatorio associato a fondo economale , Documento autorizzatorio
-- non  associato a fondo economale il conto economico di contropartita viene individuato per tipologia
-- di documento dalla tabella VOCE_EP:
-- 1. il documento autorizzatorio ? una REVERSALE di REGOLARIZZAZIONE: il conto economico ? individuato come
--    voce speciale di tipo insussitenza di credito;
-- 2. il documento autorizzatorio ? collegato a doc. amministrativo associato a fondo economale (sia nel caso sia gi? registrata la spese,
--    sia in quello in cui non sia ancora registrata): il conto economico ? individuato come voce speciale
--    di tipo cassa;
-- 3. il documento autorizzatorio appartiene al CNR: il conto economico ? individuato come voce speciale di
--    tipo banca (conto dell'ente);
-- 4. il documento autorizzatorio non appartiene al CNR: il conto economico ? individuato come voce speciale
--    di tipo banca del CdS (conto CdS).
--
-- pre-post-name: Documento autorizzatorio associato a fondo economale
-- pre: il documento autorizzatorio ? associato a fondo economale (sia nel caso sia gi? registrata la spese,
-- sia in quello in cui non sia ancora registrata).
-- post: generazione del movimento principale di default della scrittura ultima per ogni riga del documento
-- autorizzatorio: si determina il conto economico associato al documento attraverso la vista V_ANAGRAFICO_TERZO,
-- le tabelle ASS_ANAG_VOCE_EP e VOCE_EP.
-- Generazione dei movimenti di contropartita di default per ogni riga del documento autorizzatorio individuando
-- il conto economico di contropartita nella tabella VOCE_EP per codice di documento (vedi pre-post Individuazione
-- conto economico di contropartita).
-- Creazione ed inserimento della scrittura ultima di default in partita doppia (con numerazione della scrittura
-- inserita): inserisce la testata della scrittura ed aggiorna i saldi.
-- Generazione di un movimento sul conto economico della cassa in sezione opposta alla sezione del documento
-- autorizzatorio, e di un moviimento sul conto economico della banca del CdS nella stessa sezione del documento.
-- Creazione ed inserimento della scrittura singola in partita doppia (con numerazione della scrittura inserita):
-- inserisce la testata della scrittura ed aggiorna i saldi.
-- Gestione del 1210 su riscontro: dal documento amministrativo determino la differenza tra l'effettivo pagamento,
-- la commisione della banca e l.importo della fattura. Se risulta una differenza non nulla registro il movimento
-- principale in DARE o AVERE in dipendenza della differenza appena calcolata (importo del movimento), determinando
-- il conto economico associato dalla tabella VOCE_EP in perdita sul cambio o utile su cambio rispettivamente.
-- Registro anche un movimento in sezione opposta sul conto economico del terzo attraverso la vista
-- V_ANAGRAFICO_TERZO (vista estrazione dei terzi con tutte le informazioni di base della anagrafica a cui appartengono)
-- e la tabella ASS_ANAG_VOCE_EP. Creazione ed inserimento della scrittura singola in partita doppia
-- (con numerazione della scrittura inserita): inserisce la testata della scrittura ed aggiorna i saldi.
-- Vedi pre-post Aggiornamento dello stato COGE
--
-- pre-post-name: Documento autorizzatorio non  associato a fondo economale
-- pre: il documento autorizzatorio non ? associato a fondo economale. Non esistono scritture ultime in partita
-- doppia associate per esercizio, centro di spesa, unit? organizzativa, numero progressivo di documento contabile.
-- post: generazione del movimento principale di default della scrittura ultima per ogni riga del documento
-- autorizzatorio: si determina il conto economico associato al documento attraverso la vista V_ANAGRAFICO_TERZO,
-- le tabelle ASS_ANAG_VOCE_EP e VOCE_EP.
-- Generazione dei movimenti di contropartita di default per ogni riga del documento autorizzatorio individuando
-- il conto economico di contropartita nella tabella VOCE_EP per codice di documento (vedi pre-post Individuazione
-- conto economico di contropartita).
-- Creazione ed inserimento della scrittura ultima di default in partita doppia (con numerazione della scrittura
-- inserita): inserisce la testata della scrittura ed aggiorna i saldi.
-- Gestione del 1210 su riscontro: dal documento amministrativo determino la differenza tra l'effettivo pagamento,
-- la commisione della banca e l.importo della fattura. Se risulta una differenza non nulla registro il movimento
-- principale in DARE o AVERE in dipendenza della differenza appena calcolata (importo del movimento), determinando
-- il conto economico associato dalla tabella VOCE_EP in perdita sul cambio o utile su cambio rispettivamente.
-- Registro anche un movimento in sezione opposta sul conto economico del terzo attraverso la vista
-- V_ANAGRAFICO_TERZO (vista estrazione dei terzi con tutte le informazioni di base della anagrafica a cui appartengono)
-- e la tabella ASS_ANAG_VOCE_EP. Creazione ed inserimento della scrittura singola in partita doppia
-- (con numerazione della scrittura inserita): inserisce la testata della scrittura ed aggiorna i saldi.
-- Vedi pre-post Aggiornamento dello stato COGE
--
-- pre-post-name: Aggiornamento dello stato COGE
-- pre: il documento autorizzatorio ? un MANDATO o una REVERSALE
-- post: vengono aggiornate le tabelle MANDATO o REVERSALE (in dipendenza della tipologia di documento autorizzatorio)
-- definendo lo stato COGE del documento, l.utente che effettua la modifica, la data della modifica e incrementando il
-- numero progressivo del documento in corrispondenza dell.esercizio, del centro di spesa e del numero progressivo del
-- documento contabile.
--
-- Parametri:
-- aDocTst -> testata di un documento amministrativo estratta dalla vista V_DOC_ULT_COGE_TSTA
-- aUser -> utente che effettua le modifiche

 procedure regDocPagCOGE(aDocTst V_DOC_ULT_COGE_TSTA%rowtype, aUser varchar2, aTSNow date);

 -- =============================================================
 -- Registra annullamento post chiusura esercizio
 -- =============================================================

 -- pre-post-name: Cds origine del documento non pi? valido nell'esercizio di registrazione economica dell'annullamento
 -- pre: Il cds origine del documento non ? pi? valido nell'esercizio di registrazione economica dell'annullamento
 -- post: Viene sollevata un'eccezione

 -- pre-post-name: Esercizio in cui si annulla il documento non ancora aperto o chiuso finanziariamente
 -- pre: L'esercizio in cui si annulla il documento non ancora aperto per il cds specificato
 -- post: Viene sollevata un'eccezione

 -- pre-post-name: Esercizio precedente a quello in cui si ? annulato il documento nonn ? chiuso
 -- economicamente definitivamente
 -- pre: L'esercizio di origine del documento non ? ancora chiuso
 -- post: Viene sollevata un'eccezione

 -- pre-post-name: Esercizio economico di origine del documento non chiuso
 -- pre: L'esercizio economico di origine del documento non ? ancora chiuso
 -- post: Viene sollevata un'eccezione

 -- pre-post-name: Verifica che il documento sia stato riportato all'esercizio economico di annullamento e non oltre
 -- pre: non ? vero che il documento ? stato riportato all'esercizio di annullamento e non oltre
 -- post: Viene sollevata un'eccezione

 -- pre-post-name: Verifica che il documento sia stato processato almeno una volta in economica
 -- pre: Il documento non ? mai stato processato in economica (stato_coge='N')
 -- post: Viene sollevata un'eccezione

 -- pre-post-name: Documento non ancora processato in economica
 -- pre: Il doc. amministrativo non ? stato processato con scrittura PRIMA economica nell'esercizio in chiusura
 -- post: La procedura esce senza sollevare eccezioni

 -- pre-post-name: Annullamento gi? eseguito
 -- pre: Vengono ricercate e trovate le scritture di annullamento speciale per il doc. amministrativo
 -- post: La procedura esce senza sollevare eccezioni

 -- pre-post-name: Importo movimento minore o uguale a 0
 -- pre: L'importo del movimento di sopravveninenza attiva o passiva risulta negativo o nullo
 -- post: Viene sollevata un'eccezione

 -- pre-post-name: Registrazione economica dell'annulamento
 -- pre: E' richiesta la registrazione dell'annullamento del documento amministrativo in esercizio diverso da quello di origine quest'ultimo chiuso economicamente
 -- post:
 --     Vengono recuperate le scritture prime del documento nel'esercizio ORIGINE dello stesso
 --     per ogni scrittura prima associata al documento (solo il generico ha event. pi? di una scrittura per i singoli terzi)
 --      Recupera il movimento del terzo per estrarre:
 --          aCodTerzo -> codice terzo
 --          aImMovimento -> importo del movimento
 --          aSez -> sezione del movimento
 --          aTiIstitComm -> tipo istituzionale commerciale del movimento
 --      Se il documento ? generico o fattura passiva o attiva
 --       Estrae da mandato riga la quota pagata/incassata (aImPagatoIncassato) del documento amministrativo per il terzo in processo
 --      Crea un movimento nell'esercizio aEs di destinazione con:
 --		 	  intervallo competenza coge => data di sistema
 --           importo movimento = aImMovimento (- aImPagatoIncassatoper generici e fatture)
 --           terzo = aCdTerzo
 --           tipo istituzionale commerciale = aTiIstituzComm
 --           sezione = sezione opposta ad aSez
 --           voce economica = conto associato a aCdTerzo nell'esercizio aEs
 --      Crea il movimento di contropartita sulla voce delle sopravvenienze (attive o passive a seconda del caso)
 --      Crea la scrittura economica con le seguenti caratteristiche:
 --          Data di contabilizzazione -> data di sistema
 --          La causale coge -> ANNULLAMENTO_DOC_ESERCIZIO_CHIUSO
 --          Origine della scrittura -> ORIGINE_DOCUMENTO_AMM
 --          Terzo -> aCdTerzo
 -- Parametri:
 -- aEs -> esercizio in cui si annulla (congela) il documento proveniente da esercizio chiuso economicamente
 -- aDocTst -> Documento registrato in esercizio succ. a quello in chiusura e cono competenza nell'esercizio precedente
 -- aUser -> Utente che effettua la variazione
 -- aTSNow -> Timestamp modifica

 procedure regAnnullaDocEsChiusoCOGE(aEs number, aDocTst V_DOC_AMM_COGE_TSTA%rowtype, aUser varchar2, aTSNow date);

 Procedure regAnnullaRigheDocEsChiusoCOGE(aEs number, aRigheAnn V_DOC_AMM_COGE_RIGHE_ANNULLATE%rowtype, aUser varchar2, aTSNow date);
-- parametro da valutare in molte procedure
 recParametriCNR PARAMETRI_CNR%Rowtype;
end;
/


CREATE OR REPLACE package body         CNRCTB205 is
Function isGenericoCollInv(aDoc V_DOC_AMM_COGE_RIGA%ROWTYPE)Return Boolean
 Is
  conta NUMBER:=0;
 Begin
         Select Count(0) Into conta FROM ASS_INV_BENE_FATTURA WHERE
                      cd_cds_doc_gen = aDoc.cd_cds
                   AND cd_uo_doc_gen = aDoc.cd_unita_organizzativa
                   AND esercizio_doc_gen = aDoc.ESERCIZIO
                   AND pg_documento_generico = aDoc.pg_numero_documento
                   And CD_TIPO_DOCUMENTO_AMM = aDoc.cd_tipo_documento
                   AND progressivo_riga_doc_gen = aDoc.pg_riga;
	If conta!=0 Then
		Return True;
	else
		return False;
	End If;
 Exception When Others Then
	Return False;
 End;
 -- =======================================
 --
 -- CONTABILIZZAZIONE PARTE PRINCIPALE
 --
 -- =======================================

 procedure buildMovPEPAnticipo(aListaMovimenti IN OUT CNRCTB200.movimentiList, aDocTst V_DOC_AMM_COGE_TSTA%rowtype, aDoc V_DOC_AMM_COGE_RIGA%rowtype, aUser varchar2, aTSnow date) is
  aAnticipo anticipo%rowtype;
  aImpAnticipo number(15,2);
  aVoce_ep voce_ep%rowtype;
 begin
  Dbms_Output.put_line ('anticipo buildMovPEPAnticipo ');
  aAnticipo := CNRCTB204.getAnticipo(aDocTst);
  -- No anticipo no movimento
  if aAnticipo.pg_anticipo is null then
   return;
  end if;
  aImpAnticipo := aAnticipo.im_anticipo;
  -- Se l'anticipo ? 0 non genero movimenti legati all'anticipo
  	if aImpAnticipo > 0 and  aDoc.CD_TIPO_DOCUMENTO = CNRCTB100.TI_COMPENSO and CNRCTB204.isCompConAntMaggNetto(aDocTst)='Y' then
   		 null;
    elsif aImpAnticipo > 0 then
   -- L'anticipo ? sempre in avere
		Dbms_Output.put_line ('buildMovPrinc in buildMovPEPAnticipo CNRCTB002.GETVOCEEPANTICIPOMISSIONE');
    aVoce_ep:=CNRCTB002.GETVOCEEPANTICIPOMISSIONE(aDocTst.esercizio);

    if aVoce_ep.cd_voce_ep is null then
    		aVoce_ep:=CNRCTB204.trovaContoEp(aDocTst,aDoc);
    		Dbms_Output.PUT_LINE ('aVoce_ep.cd_voce_ep '||avoce_ep.cd_voce_ep );
    end if;
       	CNRCTB204.buildMovPrinc(aDocTst.cd_cds_origine,aDocTst.esercizio,aDocTst.cd_uo_origine,aVoce_ep,aImpAnticipo,CNRCTB200.IS_AVERE,aDoc.dt_da_competenza_coge,aDoc.dt_a_competenza_coge,aDoc.cd_terzo,aDoc.ti_istituz_commerc, aListaMovimenti,aUser,aTSnow);
-- 23.06.2006 mette sull'anticipo il flag movimento terzo senn? in caso di ratei va male la chiusura del rateo
    	aListaMovimenti(aListaMovimenti.count).FL_MOV_TERZO := 'Y';

  end if;
 end;

Procedure buildMovPEPIVAServIstNR(aListaMovimenti IN OUT CNRCTB200.movimentiList, aDocTst V_DOC_AMM_COGE_TSTA%rowtype, aDoc V_DOC_AMM_COGE_RIGA%rowtype, aUser varchar2, aTSnow date) is
  aEffCori ass_tipo_cori_voce_ep%rowtype;
  aContoEp voce_ep%rowtype;
  aSezione CHAR(1);
  Fl_SNR CHAR(1);
Begin

Dbms_Output.PUT_LINE ('IN buildMovPEPIVAServIstNR');
If  aDocTst.cd_tipo_documento = CNRCTB100.TI_FATTURA_PASSIVA  and
	  aDocTst.TI_BENE_SERVIZIO = CNRCTB100.TI_FT_ACQ_SERVIZI And
    aDocTst.TI_ISTITUZ_COMMERC = CNRCTB100.TI_ISTITUZIONALE Then

    select fl_servizi_non_residenti into Fl_SNR
    from tipo_sezionale ,fattura_passiva
    where
    tipo_sezionale.cd_tipo_sezionale =fattura_passiva.cd_tipo_sezionale and
    fattura_passiva.esercizio = aDocTst.esercizio and
    fattura_passiva.cd_cds = aDocTst.cd_cds and
    fattura_passiva.cd_unita_organizzativa = aDocTst.cd_unita_organizzativa and
    fattura_passiva.pg_fattura_passiva = aDocTst.PG_NUMERO_DOCUMENTO;

  -- Se la fattura ? istituzionale NON RESIDENTI, apro il debito verso l'erario
	  If Fl_SNR = 'Y'  then
		    aEffCori := CNRCTB204.getAssCoriEp(aDocTst.esercizio, 'IVA', CNRCTB001.GESTIONE_ENTRATE, CNRCTB100.IS_DARE);

		    Begin
			aContoEp:=CNRCTB002.getVoceEp(aEffCori.esercizio, aEffCori.cd_voce_ep_contr);
		    Exception when OTHERS then
		        IBMERR001.RAISE_ERR_GENERICO('Conto economico di contr.: '||aEffCori.cd_voce_ep_contr||' associato a CORI IVA non trovato');
		    End;

		-- modifica del 14/06/2006 per le note l'iva va in Dare, prima metteva l'iva sempre in Avere e per le note sfetecchiava
		-- tutta la scrittura per il fatto che l'ultimo importo lo mette per quadrare la scrittura

		    If aDocTst.ti_fattura = 'C' Then
		        aSezione := CNRCTB100.IS_DARE;
		    Else
		        aSezione := CNRCTB100.IS_AVERE;
		    End If;

				Dbms_Output.PUT_LINE ('CARICA RIGA CON '||aContoEp.CD_VOCE_EP);
		    CNRCTB204.buildMovPrinc(aDocTst.cd_cds_origine, aDocTst.esercizio, aDocTst.cd_uo_origine, aContoEp, abs(aDoc.im_iva),
		                            aSezione,
		                            aDoc.dt_da_competenza_coge, aDoc.dt_a_competenza_coge, aDocTst.cd_terzo,
		                            aListaMovimenti, aUser, aTSnow);
    End if;
End if;
End;
Procedure buildMovPEPIVABeniIstIUSMSI(aListaMovimenti IN OUT CNRCTB200.movimentiList, aDocTst V_DOC_AMM_COGE_TSTA%rowtype, aDoc V_DOC_AMM_COGE_RIGA%rowtype, aUser varchar2, aTSnow date) is
  aEffCori ass_tipo_cori_voce_ep%rowtype;
  aContoEp voce_ep%rowtype;
  aSezione CHAR(1);
  fl_merce_intra_ue  CHAR(1);
Begin

If  aDocTst.cd_tipo_documento = CNRCTB100.TI_FATTURA_PASSIVA  and
	  aDocTst.TI_BENE_SERVIZIO = CNRCTB100.TI_FT_ACQ_BENI And
    aDocTst.TI_ISTITUZ_COMMERC = CNRCTB100.TI_ISTITUZIONALE Then

    select fl_merce_intra_ue into fl_merce_intra_ue
    from fattura_passiva
    where
    fattura_passiva.esercizio = aDocTst.esercizio and
    fattura_passiva.cd_cds = aDocTst.cd_cds and
    fattura_passiva.cd_unita_organizzativa = aDocTst.cd_unita_organizzativa and
    fattura_passiva.pg_fattura_passiva = aDocTst.PG_NUMERO_DOCUMENTO;

		Dbms_Output.PUT_LINE ('IN buildMovPEPIVABeniIstIUSMSI');
  -- Se la fattura ? istituzionale intraue o san marino per beni, apro il debito verso l'erario
			If  (aDocTst.FL_SAN_MARINO_SENZA_IVA = 'Y' Or aDocTst.FL_INTRA_UE = 'Y' or fl_merce_intra_ue='Y') then
			    aEffCori := CNRCTB204.getAssCoriEp(aDocTst.esercizio, 'IVA', CNRCTB001.GESTIONE_ENTRATE, CNRCTB100.IS_DARE);

			    Begin
				aContoEp:=CNRCTB002.getVoceEp(aEffCori.esercizio, aEffCori.cd_voce_ep_contr);
			    Exception when OTHERS then
			        IBMERR001.RAISE_ERR_GENERICO('Conto economico di contr.: '||aEffCori.cd_voce_ep_contr||' associato a CORI IVA non trovato');
			    End;

			-- modifica del 14/06/2006 per le note l'iva va in Dare, prima metteva l'iva sempre in Avere e per le note sfetecchiava
			-- tutta la scrittura per il fatto che l'ultimo importo lo mette per quadrare la scrittura

			    If aDocTst.ti_fattura = 'C' Then
			        aSezione := CNRCTB100.IS_DARE;
			    Else
			        aSezione := CNRCTB100.IS_AVERE;
			    End If;

			Dbms_Output.PUT_LINE ('CARICA RIGA CON '||aContoEp.CD_VOCE_EP);
			    CNRCTB204.buildMovPrinc(aDocTst.cd_cds_origine, aDocTst.esercizio, aDocTst.cd_uo_origine, aContoEp, abs(aDoc.im_iva),
			                            aSezione,
			                            aDoc.dt_da_competenza_coge, aDoc.dt_a_competenza_coge, aDocTst.cd_terzo,
			                            aListaMovimenti, aUser, aTSnow);
			End if;
End if;
End;


 PROCEDURE buildMovPEPBeniDurevoli(aListaMovimenti IN OUT CNRCTB200.movimentiList, aDocTst V_DOC_AMM_COGE_TSTA%ROWTYPE, aDoc V_DOC_AMM_COGE_RIGA%ROWTYPE, aUser VARCHAR2, aTSnow DATE) IS
  aInventBene INVENTARIO_BENI%ROWTYPE;
  isBeneNonTrovato BOOLEAN;
  aCatGruppo CATEGORIA_GRUPPO_INVENT%ROWTYPE;
  aCatGruppoVoceEp CATEGORIA_GRUPPO_VOCE_EP%ROWTYPE;
  aVoceEPCG VOCE_EP%ROWTYPE;
  aVoceEPCGContr VOCE_EP%ROWTYPE;
  aVoceEPPlusvalenza VOCE_EP%ROWTYPE;
  aVoceEPMinusvalenza VOCE_EP%ROWTYPE;
  aResiduo NUMBER(15,2):=0;
  quote_storno NUMBER(15,2):=0;
  aDelta NUMBER(15,2):=0;
  TOT_DOC NUMBER(15,2):=0;
  Conta NUMBER:=0;
  scarico_tot Boolean;
  aBSC BUONO_CARICO_SCARICO_DETT%ROWTYPE;

begin
  	recParametriCNR := CNRUTL001.getRecParametriCnr(aDoc.esercizio);

	  If (aDocTst.cd_tipo_documento= CNRCTB100.TI_FATTURA_ATTIVA)Then
	    isBeneNonTrovato:=TRUE;
  	    FOR aAssInvBF IN (SELECT * FROM ASS_INV_BENE_FATTURA WHERE
                               cd_cds_fatt_att = aDoc.cd_cds
                           AND cd_uo_fatt_att = aDoc.cd_unita_organizzativa
                           AND esercizio_fatt_att = aDoc.ESERCIZIO
                           AND pg_fattura_attiva = aDoc.pg_numero_documento
                           AND progressivo_riga_fatt_att = aDoc.pg_riga FOR UPDATE NOWAIT) LOOP
	            isBeneNonTrovato:=FALSE;
	            Begin
	        	    SELECT * INTO aInventBene FROM INVENTARIO_BENI WHERE
		      	         pg_inventario=aAssInvBF.pg_inventario
		      	     AND nr_inventario=aAssInvBF.nr_inventario
		      	     AND progressivo=aAssInvBF.progressivo FOR UPDATE NOWAIT;
	             EXCEPTION WHEN NO_DATA_FOUND THEN
	              IBMERR001.RAISE_ERR_GENERICO('Bene non trovato in inventario:'||aAssInvBF.pg_inventario||'-'||aAssInvBF.nr_inventario||'-'||aAssInvBF.progressivo);
	            End;
	              -- 22/02/2008 verifico che non ci siano + righe di documento generico collegate allo stesso dettaglio di buono di scarico
	      -- altrimenti i dati dell'inventario vengono presi + volte
	      Select Count(*) Into conta
	                   From ASS_INV_BENE_FATTURA,ASS_INV_BENE_FATTURA ass
	                   Where
	                       ASS_INV_BENE_FATTURA.cd_cds_fatt_att = aDoc.cd_cds
                           And ASS_INV_BENE_FATTURA.cd_uo_fatt_att = aDoc.cd_unita_organizzativa
                           And ASS_INV_BENE_FATTURA.esercizio_fatt_att = aDoc.ESERCIZIO
                           And ASS_INV_BENE_FATTURA.pg_fattura_attiva = aDoc.pg_numero_documento
                           And ASS_INV_BENE_FATTURA.progressivo_riga_fatt_att = aDoc.pg_riga
                           And ass.pg_inventario = ASS_INV_BENE_FATTURA.pg_inventario
                           And ass.NR_inventario = ASS_INV_BENE_FATTURA.NR_inventario
                           And ass.pROGRESSIVO = ASS_INV_BENE_FATTURA.pROGRESSIVO
                           And ass.ESERCIZIO = ASS_INV_BENE_FATTURA.ESERCIZIO
                           And ass.TI_DOCUMENTO = ASS_INV_BENE_FATTURA.TI_DOCUMENTO
                           And ass.PG_BUONO_C_S = ASS_INV_BENE_FATTURA.PG_BUONO_C_S
                           And ass.pg_inventario = aAssInvBF.pg_inventario
                           And ass.NR_inventario = aAssInvBF.NR_inventario
                           And ass.pROGRESSIVO = aAssInvBF.pROGRESSIVO
                           And ass.ESERCIZIO = aAssInvBF.ESERCIZIO
                           And ass.TI_DOCUMENTO = aAssInvBF.TI_DOCUMENTO
                           And ass.PG_BUONO_C_S = aAssInvBF.PG_BUONO_C_S
                           And ass.cd_cds_fatt_att = aDoc.cd_cds
                           And ass.cd_uo_fatt_att = aDoc.cd_unita_organizzativa
                           And ass.esercizio_fatt_att = aDoc.ESERCIZIO
                           And ass.pg_fattura_attiva = aDoc.pg_numero_documento
                           And ass.pg_riga <= ASS_INV_BENE_FATTURA.pg_riga;
                If(Conta =1 ) Then
		    BEGIN
			  SELECT * INTO aBSC FROM BUONO_CARICO_SCARICO_DETT bcsd WHERE
	                     PG_INVENTARIO=aAssInvBF.pg_inventario
	                 AND TI_DOCUMENTO='S'
	                 AND ESERCIZIO=aDoc.ESERCIZIO
	                 AND NR_INVENTARIO=aAssInvBF.nr_inventario
	                 AND PROGRESSIVO=aAssInvBF.progressivo
			 AND PG_BUONO_C_S = aAssInvBF.pg_buono_c_s
			  FOR UPDATE NOWAIT;
             	     EXCEPTION WHEN NO_DATA_FOUND THEN
              		IBMERR001.RAISE_ERR_GENERICO('Buono di scarico non trovato');
		     END;

	             BEGIN
	        	    SELECT * INTO aCatGruppo FROM CATEGORIA_GRUPPO_INVENT WHERE
	      	                   cd_categoria_gruppo = aInventBene.cd_categoria_gruppo;
	             EXCEPTION WHEN NO_DATA_FOUND THEN
	              IBMERR001.RAISE_ERR_GENERICO('Gruppo inventariale non trovato:'||aInventBene.cd_categoria_gruppo);
	             END;
		     BEGIN
						if recParametriCNR.fl_nuovo_pdg='N' then
  	   	  	      SELECT * INTO aCatGruppoVoceEp FROM CATEGORIA_GRUPPO_VOCE_EP WHERE
	      	              cd_categoria_gruppo = aCatGruppo.cd_categoria_padre
					  				AND sezione = CNRCTB200.IS_AVERE
					  				AND ESERCIZIO=aDocTst.ESERCIZIO;
					  else
					    begin
					       	      SELECT * INTO aCatGruppoVoceEp FROM CATEGORIA_GRUPPO_VOCE_EP WHERE
	      	              cd_categoria_gruppo = aCatGruppo.cd_categoria_gruppo
					  				AND sezione = CNRCTB200.IS_AVERE
					  				AND ESERCIZIO=aDocTst.ESERCIZIO
					  				and cd_elemento_voce = aDoc.CD_ELEMENTO_VOCE_EV;
					  	EXCEPTION WHEN NO_DATA_FOUND THEN
					  	   	      SELECT * INTO aCatGruppoVoceEp FROM CATEGORIA_GRUPPO_VOCE_EP WHERE
	      	              cd_categoria_gruppo = aCatGruppo.cd_categoria_gruppo
					  				AND sezione = CNRCTB200.IS_AVERE
					  				AND ESERCIZIO=aDocTst.ESERCIZIO
					  				and fl_default='Y';
					  	end;
					  end if;
         EXCEPTION WHEN NO_DATA_FOUND THEN
          IBMERR001.RAISE_ERR_GENERICO('Nessun conto economico associato a categoria:'||aCatGruppo.cd_categoria_gruppo);
         END;
		 -- Conto automezzi
		 aVoceEPCG:=CNRCTB002.getVoceEp(aCatGruppoVoceEp.ESERCIZIO,aCatGruppoVoceEp.cd_voce_ep);
		 if(aCatGruppoVoceEp.cd_voce_ep_contr is not null) then
		 -- Conto per l'ammortamento
		 			aVoceEPCGContr:=CNRCTB002.getVoceEp(aCatGruppoVoceEp.ESERCIZIO,aCatGruppoVoceEp.cd_voce_ep_contr);
		 end if;
	     	aResiduo:=aResiduo+aInventBene.valore_iniziale + aInventBene.variazione_piu - (aInventBene.variazione_meno-(aBSC.quantita*aBSC.valore_unitario)) - NVL(aInventBene.valore_ammortizzato,0);
                CNRCTB204.buildMovPrinc(aDocTst.cd_cds_origine,aDocTst.ESERCIZIO,aDocTst.cd_uo_origine,aVoceEPCG,aInventBene.valore_iniziale + aInventBene.variazione_piu - (aInventBene.variazione_meno-(aBSC.quantita*aBSC.valore_unitario)),CNRCTB200.IS_AVERE,aDoc.dt_da_competenza_coge,aDoc.dt_a_competenza_coge,aDoc.cd_terzo,aDoc.ti_istituz_commerc,aListaMovimenti,aUser,aTSnow);
                IF NVL(aInventBene.valore_ammortizzato,0) > 0 THEN
			  CNRCTB204.buildMovPrinc(aDocTst.cd_cds_origine,aDocTst.ESERCIZIO,aDocTst.cd_uo_origine,aVoceEPCGContr,NVL(aInventBene.valore_ammortizzato,0),CNRCTB200.IS_DARE,aDoc.dt_da_competenza_coge,aDoc.dt_a_competenza_coge,aDoc.cd_terzo,aDoc.ti_istituz_commerc,aListaMovimenti,aUser,aTSnow);
                END IF;
                IF aInventBene.ti_commerciale_istituzionale = CNRCTB100.TI_ISTITUZIONALE THEN
	                TOT_DOC:=aDoc.im_imponibile + aDoc.im_iva;
	        Else
	                TOT_DOC:=aDoc.im_imponibile;
	        end if;
	   -- r.p. 22/02/2008 caso + righe collegate stesso dettaglio buono devo considerare solo l'importo della riga di doc.
	   Else
	     IF aInventBene.ti_commerciale_istituzionale = CNRCTB100.TI_ISTITUZIONALE THEN
	      TOT_DOC:=aDoc.im_imponibile + aDoc.im_iva;
	     ELSE
	      TOT_DOC:=aDoc.im_imponibile;
	     end if;
	   End If;
	   	aVoceEPMinusvalenza:=CNRCTB002.getVoceEpMinusval(aDocTst.ESERCIZIO,aCatGruppo.cd_categoria_gruppo,null);
	    aVoceEPPlusvalenza:=CNRCTB002.getVoceEpPlusval(aDocTst.ESERCIZIO,aCatGruppo.cd_categoria_gruppo,null);

        END LOOP;
        -- r.p. 14/02/2007 spostato all'esterno del loop in quanto sono dati relativi al documento
        -- quindi devono essere considerati una sola volta nel loop viene calcolato il residuo totale
			--aVoceEPMinusvalenza:=CNRCTB002.getVoceEpMinusval(aDocTst.ESERCIZIO,aCatGruppo.cd_categoria_gruppo,aDoc.CD_ELEMENTO_VOCE_EV);
	    --aVoceEPPlusvalenza:=CNRCTB002.getVoceEpPlusval(aDocTst.ESERCIZIO,aCatGruppo.cd_categoria_gruppo,aDoc.CD_ELEMENTO_VOCE_EV);
	    aDelta:=TOT_DOC - aResiduo;
	 IF aDelta > 0 THEN
		 CNRCTB204.buildMovPrinc(aDocTst.cd_cds_origine,aDocTst.ESERCIZIO,aDocTst.cd_uo_origine,aVoceEPPlusvalenza,aDelta,CNRCTB200.IS_AVERE,aDoc.dt_da_competenza_coge,aDoc.dt_a_competenza_coge,aDoc.cd_terzo,aDoc.ti_istituz_commerc,aListaMovimenti,aUser,aTSnow);
	 END IF;
	 IF aDelta < 0 THEN
		  CNRCTB204.buildMovPrinc(aDocTst.cd_cds_origine,aDocTst.ESERCIZIO,aDocTst.cd_uo_origine,aVoceEPMinusvalenza,ABS(aDelta),CNRCTB200.IS_DARE,aDoc.dt_da_competenza_coge,aDoc.dt_a_competenza_coge,aDoc.cd_terzo,aDoc.ti_istituz_commerc,aListaMovimenti,aUser,aTSnow);
	 END IF;
        IF isBeneNonTrovato THEN
           IBMERR001.RAISE_ERR_GENERICO('Associazione con bene ad inventario non trovata');
        END IF;
       Else
        -- generico attivo
          FOR aAssInvBF IN (SELECT * FROM ASS_INV_BENE_FATTURA WHERE
                             cd_cds_doc_gen = aDoc.cd_cds
                         AND cd_uo_doc_gen = aDoc.cd_unita_organizzativa
                         AND esercizio_doc_gen = aDoc.ESERCIZIO
                         AND pg_documento_generico = aDoc.pg_numero_documento
                         And CD_TIPO_DOCUMENTO_AMM = aDoc.cd_tipo_documento
                         AND progressivo_riga_doc_gen = aDoc.pg_riga
                         FOR UPDATE NOWAIT) Loop
           	Begin
       	   	 	SELECT * INTO aInventBene
       	   	 	FROM INVENTARIO_BENI
       	   	 	WHERE
    	        	    pg_inventario=aAssInvBF.pg_inventario
      	     		AND nr_inventario=aAssInvBF.nr_inventario
      	     		AND progressivo=aAssInvBF.progressivo FOR UPDATE NOWAIT;
            EXCEPTION WHEN NO_DATA_FOUND Then
              		IBMERR001.RAISE_ERR_GENERICO('Bene non trovato in inventario:'||aAssInvBF.pg_inventario||'-'||aAssInvBF.nr_inventario||'-'||aAssInvBF.progressivo);
	      		End;
	      -- 22/02/2008 verifico che non ci siano + righe di documento generico collegate allo stesso dettaglio di buono di scarico
	      -- altrimenti i dati dell'inventario vengono presi + volte
	      		Select Count(*) Into conta
	                   From ASS_INV_BENE_FATTURA,ASS_INV_BENE_FATTURA ass
	                   Where
	                       			 ASS_INV_BENE_FATTURA.cd_cds_doc_gen = aDoc.cd_cds
                           And ASS_INV_BENE_FATTURA.cd_uo_doc_gen = aDoc.cd_unita_organizzativa
                           And ASS_INV_BENE_FATTURA.esercizio_doc_gen = aDoc.ESERCIZIO
                           And ASS_INV_BENE_FATTURA.pg_documento_generico = aDoc.pg_numero_documento
                           And ASS_INV_BENE_FATTURA.CD_TIPO_DOCUMENTO_AMM = aDoc.cd_tipo_documento
                           And ASS_INV_BENE_FATTURA.progressivo_riga_doc_gen = aDoc.pg_riga
                           And ass.pg_inventario = ASS_INV_BENE_FATTURA.pg_inventario
                           And ass.NR_inventario = ASS_INV_BENE_FATTURA.NR_inventario
                           And ass.pROGRESSIVO = ASS_INV_BENE_FATTURA.pROGRESSIVO
                           And ass.ESERCIZIO = ASS_INV_BENE_FATTURA.ESERCIZIO
                           And ass.TI_DOCUMENTO = ASS_INV_BENE_FATTURA.TI_DOCUMENTO
                           And ass.PG_BUONO_C_S = ASS_INV_BENE_FATTURA.PG_BUONO_C_S
                           And ass.pg_inventario = aAssInvBF.pg_inventario
                           And ass.NR_inventario = aAssInvBF.NR_inventario
                           And ass.pROGRESSIVO = aAssInvBF.pROGRESSIVO
                           And ass.ESERCIZIO = aAssInvBF.ESERCIZIO
                           And ass.TI_DOCUMENTO = aAssInvBF.TI_DOCUMENTO
                           And ass.PG_BUONO_C_S = aAssInvBF.PG_BUONO_C_S
                           And ass.cd_cds_doc_gen = aDoc.cd_cds
                           And ass.cd_uo_doc_gen = aDoc.cd_unita_organizzativa
                           And ass.esercizio_doc_gen = aDoc.ESERCIZIO
                           And ass.pg_documento_generico = aDoc.pg_numero_documento
                           And ass.CD_TIPO_DOCUMENTO_AMM = aDoc.cd_tipo_documento
                           And ass.pg_riga <= ASS_INV_BENE_FATTURA.pg_riga;
             If(Conta =1 ) Then
	        		BEGIN
							  Select * INTO aBSC
							  FROM BUONO_CARICO_SCARICO_DETT bcsd
							  WHERE
		              PG_INVENTARIO=aAssInvBF.pg_inventario
                  AND TI_DOCUMENTO='S'
                  AND ESERCIZIO=aDoc.ESERCIZIO
                  AND NR_INVENTARIO=aAssInvBF.nr_inventario
                  AND PROGRESSIVO=aAssInvBF.progressivo
                  And PG_BUONO_C_S=aAssInvBF.PG_BUONO_C_S
		      			FOR UPDATE NOWAIT;
              EXCEPTION WHEN NO_DATA_FOUND Then
        	     	IBMERR001.RAISE_ERR_GENERICO('Buono di scarico non trovato');
	        		END;
  						BEGIN
        	    SELECT * INTO aCatGruppo
        	    FROM CATEGORIA_GRUPPO_INVENT
        	        WHERE
      	                cd_categoria_gruppo = aInventBene.cd_categoria_gruppo;
              EXCEPTION WHEN NO_DATA_FOUND THEN
                        IBMERR001.RAISE_ERR_GENERICO('Gruppo inventariale non trovato:'||aInventBene.cd_categoria_gruppo);
              END;
              BEGIN
								if recParametriCNR.fl_nuovo_pdg='N' then
  	   	  	      SELECT * INTO aCatGruppoVoceEp FROM CATEGORIA_GRUPPO_VOCE_EP WHERE
	      	              cd_categoria_gruppo = aCatGruppo.cd_categoria_padre
					  				AND sezione = CNRCTB200.IS_AVERE
					  				AND ESERCIZIO=aDocTst.ESERCIZIO;
					  		else
					  			begin
					       	      SELECT * INTO aCatGruppoVoceEp FROM CATEGORIA_GRUPPO_VOCE_EP WHERE
	      	              cd_categoria_gruppo = aCatGruppo.cd_categoria_gruppo
					  				AND sezione = CNRCTB200.IS_AVERE
					  				AND ESERCIZIO=aDocTst.ESERCIZIO
					  				and cd_elemento_voce = aDoc.CD_ELEMENTO_VOCE_EV;
					  			EXCEPTION WHEN NO_DATA_FOUND THEN
          		   	      SELECT * INTO aCatGruppoVoceEp FROM CATEGORIA_GRUPPO_VOCE_EP WHERE
	      	              cd_categoria_gruppo = aCatGruppo.cd_categoria_gruppo
					  				AND sezione = CNRCTB200.IS_AVERE
					  				AND ESERCIZIO=aDocTst.ESERCIZIO
					  				and fl_default='Y';
					  			end;
					  		end if;
         			EXCEPTION WHEN NO_DATA_FOUND THEN
          				IBMERR001.RAISE_ERR_GENERICO('Nessun conto economico associato a categoria:'||aCatGruppo.cd_categoria_gruppo);
         			END;
        			if(aDocTst.cd_tipo_documento= CNRCTB100.TI_GENERICO_ENTRATA) then
									aVoceEPMinusvalenza:=CNRCTB002.getVoceEpMinusval(aDocTst.ESERCIZIO,aCatGruppo.cd_categoria_gruppo,null);
				    			aVoceEPPlusvalenza:=CNRCTB002.getVoceEpPlusval(aDocTst.ESERCIZIO,aCatGruppo.cd_categoria_gruppo,null);
	    			else
			    				aVoceEPMinusvalenza:=CNRCTB002.getVoceEpMinusval(aDocTst.ESERCIZIO,aCatGruppo.cd_categoria_gruppo,aDoc.CD_ELEMENTO_VOCE_EV);
			    				aVoceEPPlusvalenza:=CNRCTB002.getVoceEpPlusval(aDocTst.ESERCIZIO,aCatGruppo.cd_categoria_gruppo,aDoc.CD_ELEMENTO_VOCE_EV);
				    end if;
			 			-- Conto automezzi
			 			aVoceEPCG:=CNRCTB002.getVoceEp(aCatGruppoVoceEp.ESERCIZIO,aCatGruppoVoceEp.cd_voce_ep);
			 			-- Conto per l'ammortamento
			 			if(aCatGruppoVoceEp.cd_voce_ep_contr is not null) then
		 					aVoceEPCGContr:=CNRCTB002.getVoceEp(aCatGruppoVoceEp.ESERCIZIO,aCatGruppoVoceEp.cd_voce_ep_contr);
		 				end if;
			   	 	aResiduo:=aResiduo+aInventBene.valore_iniziale + aInventBene.variazione_piu - (aInventBene.variazione_meno-(aBSC.quantita*aBSC.valore_unitario)) - NVL(aInventBene.valore_ammortizzato,0);

		        Dbms_Output.PUT_LINE('RESIDUO '||aResiduo);
		        Dbms_Output.PUT_LINE('valore_ammortizzato '||aInventBene.valore_ammortizzato);
		        IF aInventBene.FL_totalmente_scaricato='Y' Then
		                scarico_tot:=True;
			        CNRCTB204.buildMovPrinc(aDocTst.cd_cds_origine,aDocTst.ESERCIZIO,aDocTst.cd_uo_origine,aVoceEPCG,aInventBene.valore_iniziale + aInventBene.variazione_piu - (aInventBene.variazione_meno-(aBSC.quantita*aBSC.valore_unitario)),CNRCTB200.IS_AVERE,aDoc.dt_da_competenza_coge,aDoc.dt_a_competenza_coge,aDoc.cd_terzo,aDoc.ti_istituz_commerc,aListaMovimenti,aUser,aTSnow);
			 			Else
			        scarico_tot:=False;
			        CNRCTB204.buildMovPrinc(aDocTst.cd_cds_origine,aDocTst.ESERCIZIO,aDocTst.cd_uo_origine,aVoceEPCG,aBSC.quantita*aBSC.valore_unitario,CNRCTB200.IS_AVERE,aDoc.dt_da_competenza_coge,aDoc.dt_a_competenza_coge,aDoc.cd_terzo,aDoc.ti_istituz_commerc,aListaMovimenti,aUser,aTSnow);
			 			End If;
			 			IF aInventBene.FL_totalmente_scaricato='Y' Then
		                If NVL(aInventBene.valore_ammortizzato,0) > 0 Then
			                CNRCTB204.buildMovPrinc(aDocTst.cd_cds_origine,aDocTst.ESERCIZIO,aDocTst.cd_uo_origine,aVoceEPCGContr,NVL(aInventBene.valore_ammortizzato,0),CNRCTB200.IS_DARE,aDoc.dt_da_competenza_coge,aDoc.dt_a_competenza_coge,aDoc.cd_terzo,aDoc.ti_istituz_commerc,aListaMovimenti,aUser,aTSnow);
	                  END IF;
	         	Else
	            Begin
			         select abs(im_movimento_ammort) into quote_storno
			         from ammortamento_bene_inv
			         where
		               PG_INVENTARIO= aBSC.PG_INVENTARIO
		     	     And PG_BUONO_S   = aBSC.PG_BUONO_C_S
				       and NR_INVENTARIO= aBSC.NR_INVENTARIO
				       and PROGRESSIVO  = aBSC.PROGRESSIVO
			         and esercizio    = aBSC.esercizio
			         and fl_storno ='Y';
	           Exception when no_data_found then
		             quote_storno:=0;
	           End;
	           If NVL(aInventBene.valore_ammortizzato,0) > 0 Then
			                CNRCTB204.buildMovPrinc(aDocTst.cd_cds_origine,aDocTst.ESERCIZIO,aDocTst.cd_uo_origine,aVoceEPCGContr,quote_storno,CNRCTB200.IS_DARE,aDoc.dt_da_competenza_coge,aDoc.dt_a_competenza_coge,aDoc.cd_terzo,aDoc.ti_istituz_commerc,aListaMovimenti,aUser,aTSnow);
	           END IF;
          	End If;
		      	IF aInventBene.ti_commerciale_istituzionale = CNRCTB100.TI_ISTITUZIONALE THEN
	                TOT_DOC:=aDoc.im_imponibile + aDoc.im_iva ;
	        	Else
	                TOT_DOC:=aDoc.im_imponibile;
	        	End IF;
       -- r.p. 22/02/2008 caso + righe collegate stesso dettaglio buono devo considerare solo l'importo della riga di doc.
         Else
            IF aInventBene.ti_commerciale_istituzionale = CNRCTB100.TI_ISTITUZIONALE THEN
	                TOT_DOC:=aDoc.im_imponibile + aDoc.im_iva ;
	        	Else
	                TOT_DOC:=aDoc.im_imponibile;
            End IF;
         End if;
        END LOOP;
        -- r.p. 14/02/2007 spostato all'esterno del loop in quanto sono dati relativi al documento
        -- quindi devono essere considerati una sola volta nel loop viene calcolato il residuo totale
	 If (scarico_tot)Then
	        aDelta:=TOT_DOC - aResiduo;
	 Else
	        aDelta:=quote_storno;
	 End If;

	 IF aDelta > 0 THEN
	  	CNRCTB204.buildMovPrinc(aDocTst.cd_cds_origine,aDocTst.ESERCIZIO,aDocTst.cd_uo_origine,aVoceEPPlusvalenza,aDelta,CNRCTB200.IS_AVERE,aDoc.dt_da_competenza_coge,aDoc.dt_a_competenza_coge,aDoc.cd_terzo,aDoc.ti_istituz_commerc,aListaMovimenti,aUser,aTSnow);
	 END IF;
	 IF aDelta < 0 THEN
	  	CNRCTB204.buildMovPrinc(aDocTst.cd_cds_origine,aDocTst.ESERCIZIO,aDocTst.cd_uo_origine,aVoceEPMinusvalenza,ABS(aDelta),CNRCTB200.IS_DARE,aDoc.dt_da_competenza_coge,aDoc.dt_a_competenza_coge,aDoc.cd_terzo,aDoc.ti_istituz_commerc,aListaMovimenti,aUser,aTSnow);
	 END IF;
       End If;
 END;

 procedure buildMovPEP(aListaMovimenti IN OUT CNRCTB200.movimentiList, aDocTst V_DOC_AMM_COGE_TSTA%rowtype,
                       aDoc V_DOC_AMM_COGE_RIGA%rowtype, aUser varchar2, aTSnow date) is
  aContoEp voce_ep%rowtype;
  aImporto number(15,2);
  aSezione char(1);
  aNum number;
  conta number;
  aRecCompenso compenso%Rowtype;
  DA_COMP       DATE;
  A_COMP        DATE;
	aAnticipo anticipo%rowtype;
begin
  recParametriCNR := CNRUTL001.getRecParametriCnr(aDoc.esercizio);
    dbms_output.put_line('Origine PGIRO E');
  if ( aDocTst.cd_tipo_documento in(CNRCTB100.TI_GENERICO_SPESA,CNRCTB100.TI_GENERICO_ENTRATA) and aDoc.FL_PGIRO = 'Y' ) then
  			if (aDocTst.cd_tipo_documento = CNRCTB100.TI_GENERICO_SPESA ) then
  			  begin
  				select 1 into conta from ass_obb_acr_pgiro
  				where
						CD_CDS = aDoc.CD_CDS_DOC   and
						ESERCIZIO = aDoc.ESERCIZIO_DOC   and
						PG_OBBLIGAZIONE = aDoc.PG_DOC   and
						ESERCIZIO_ORI_OBBLIGAZIONE = aDoc.ESERCIZIO_ORI_DOC  and
						TI_ORIGINE = 'S';
				exception when no_data_found then
				   dbms_output.put_line('Origine PGIRO E');
				  return;
				end;
				elsif (aDocTst.cd_tipo_documento = CNRCTB100.TI_GENERICO_ENTRATA ) then
  			  begin
  				select 1 into conta from ass_obb_acr_pgiro
  				where
						CD_CDS = aDoc.CD_CDS_DOC   and
						ESERCIZIO = aDoc.ESERCIZIO_DOC   and
						PG_ACCERTAMENTO = aDoc.PG_DOC   and
						ESERCIZIO_ORI_ACCERTAMENTO = aDoc.ESERCIZIO_ORI_DOC  and
						TI_ORIGINE = 'E';
				exception when no_data_found then
				   dbms_output.put_line('Origine PGIRO S');
				  return;
				end;
				end if;
	end if;
      -- Modifica del 15/03/2004
      -- Gestione speciale righe di generico di spesa su capitoli di parte 1 nell'ente
      -- Tali righe non generano movimenti
Dbms_Output.put_line ('buildMovPEP 1');
      If  aDocTst.cd_tipo_documento = CNRCTB100.TI_GENERICO_SPESA And aDoc.ti_appartenenza_ev = CNRCTB001.APPARTENENZA_CNR then
	begin
	    select 1 into aNum from elemento_voce
	    Where esercizio=aDoc.esercizio_ev
              and ti_appartenenza=aDoc.ti_appartenenza_ev
	      and ti_gestione=aDoc.ti_gestione_ev
	      and cd_elemento_voce=aDoc.cd_elemento_voce_ev
	      and cd_parte=CNRCTB001.PARTE1;
				Dbms_Output.put_line ('NO CONT DOC F');
        return;
	   exception when NO_DATA_FOUND then
					Dbms_Output.put_line ('NO CONT DOC AA');
	    null;
        end;
      elsif(nvl(aDoc.stato_coge_docamm,' ') = CNRCTB100.STATO_COEP_EXC) then
				Dbms_Output.put_line ('ROSPUC ESCO SENZA CONTABILIZZARE RIGA');
        return;
      end if;

-- Gestione vendita bene durevole in fattura attiva
Dbms_Output.put_line ('2');
  	    	if ((aDocTst.cd_tipo_documento = CNRCTB100.TI_FATTURA_ATTIVA And
                aDocTst.ti_causale_emissione = CNRCTB100.TI_FA_CAUSALE_BENE_DUREVOLE) Or
                (aDocTst.cd_tipo_documento = CNRCTB100.TI_GENERICO_ENTRATA And isGenericoCollInv(aDoc))) then
        buildMovPEPBeniDurevoli(aListaMovimenti, aDocTst, aDoc, aUser, aTSnow);
	return;
      end if;
	-- Generazione del movimento di anticipo (se necessario)
	Dbms_Output.put_line ('PRIMA DI buildMovPEPAnticipo MOVIMENTI: '||aDocTst.CD_TIPO_DOCUMENTO||' '||aListaMovimenti.Count);
  buildMovPEPAnticipo(aListaMovimenti, aDocTst, aDoc, aUser, aTSnow);

-- Generazione del movimento di apertura del debito verso l'erario per
-- fatture istituz. per beni di tipo intraue o san marino senz'iva
Dbms_Output.put_line ('lcount 2a '||aListaMovimenti.Count);
      buildMovPEPIVABeniIstIUSMSI(aListaMovimenti, aDocTst, aDoc, aUser, aTSnow);

-- Generazione del movimento di apertura del debito verso l'erario per
-- fatture istituz. di servizi su sezionale fl_servizi_non_residenti = si
Dbms_Output.put_line ('lcount 2c '||aListaMovimenti.Count);
      buildMovPEPIVAServIstNR(aListaMovimenti, aDocTst, aDoc, aUser, aTSnow);

Dbms_Output.put_line ('lcount 3 '||aListaMovimenti.Count);
-- Generazione movimento di costo

      aContoEp := CNRCTB204.trovaContoEp(aDocTst, aDoc);
      if(aContoEp.cd_voce_ep= CNRCTB002.getVoceEpRimbMutuoEsercizio(aDocTst.esercizio).cd_voce_ep) then
      	return;
			end if;
      aSezione := CNRCTB204.getSezione(aDocTst, aDoc);
      aImporto := aDoc.im_imponibile;

      If aDoc.im_imponibile < 0 then
           aSezione:=CNRCTB200.getSezioneOpposta(aSezione);
           aImporto:=abs(aImporto);
      End if;
Dbms_Output.put_line ('buildMovPrinc '||Nvl(aContoEp.CD_VOCE_EP, 'NULLO'));

-- 12.12.2006 (SF) SE IL DOCUMENTO HA COMPETENZA (ANCHE PARZIALMENTE) IN ANNO PRECEDENTE MA L'ESERCIZIO PRECEDENTE E' CHIUSO
--                 NON POTENDO FARE IL RATEO IMPUTA IL COSTO/RICAVO NELL'ESERCIZIO IN CORSO MA ANCHE LE DATE DI COMPETENZA
--                 ECONOMICA DEVONO APPARTENERE ALL'ESERCIZIO

If (CNRCTB204.getCompetenzaFuoriEsercizio(aDocTst) Or CNRCTB204.getCompetenzaacavalloconEsPrec(aDocTst)) And
    Not CNRCTB008.ISESERCIZIOAPERTOSenzaBlocco(aDocTst.esercizio - 1, aDocTst.cd_cds_origine) Then

    If To_Char(aDoc.dt_da_competenza_coge, 'YYYY') < aDocTst.esercizio Then
        DA_COMP := To_Date('0101'||aDocTst.esercizio, 'DDMMYYYY');
    Else
        DA_COMP := aDoc.dt_da_competenza_coge;
    End If;

    If To_Char(aDoc.dt_a_competenza_coge, 'YYYY') < aDocTst.esercizio Then
        A_COMP := To_Date('0101'||aDocTst.esercizio, 'DDMMYYYY');
    Else
        A_COMP := aDoc.dt_a_competenza_coge;
    End If;

End If;
  if aDoc.CD_TIPO_DOCUMENTO = CNRCTB100.TI_COMPENSO and CNRCTB204.isCompConAntMaggNetto(aDocTst)='Y' then
    aAnticipo := CNRCTB204.getAnticipo(aDocTst);
         CNRCTB204.buildMovPrinc(aDocTst.cd_cds_origine,aDocTst.esercizio,aDocTst.cd_uo_origine,
                              aContoEp,0,aSezione,
                              Nvl(DA_COMP, aDoc.dt_da_competenza_coge),
                              Nvl(A_COMP,  aDoc.dt_a_competenza_coge),
                              aDoc.cd_terzo,aDoc.ti_istituz_commerc,
                              aListaMovimenti,aUser,aTSnow);
    else
--Dbms_Output.put_line ('da data '||To_Char(DA_COMP, 'dd/mm/yyyy'));
--Dbms_Output.put_line ('a data '||To_Char(A_COMP, 'dd/mm/yyyy'));
Dbms_Output.put_line ('MOv PRinc '||aContoEp.cd_voce_ep||' importo '||to_char(aImporto)||' MA  CHE FIN HA FATTO');
      CNRCTB204.buildMovPrinc(aDocTst.cd_cds_origine,aDocTst.esercizio,aDocTst.cd_uo_origine,
                              aContoEp,aImporto,aSezione,
                              Nvl(DA_COMP, aDoc.dt_da_competenza_coge),
                              Nvl(A_COMP,  aDoc.dt_a_competenza_coge),
                              aDoc.cd_terzo,aDoc.ti_istituz_commerc,
                              aListaMovimenti,aUser,aTSnow);

   end if;
Dbms_Output.put_line ('lcount 4 '||aListaMovimenti.Count);
 end;

 -- =======================================
 --
 -- CONTABILIZZAZIONE CORI
 --
 -- =======================================

Procedure buildMovPEP(aListaNuoveScritture IN OUT CNRCTB200.scrittureList, aListaMovimenti IN OUT CNRCTB200.movimentiList,
                       aDocTst V_DOC_AMM_COGE_TSTA%rowtype, aCori V_DOC_AMM_COGE_CORI%rowtype, aUser varchar2, aTSnow date) is
  aContoEp      voce_ep%rowtype;
  aImporto      number(15,2);
  aSezione      char(1);
  aAnticipo     anticipo%rowtype;
  DA_COMP       DATE;
  A_COMP        DATE;
	aAssCoriEpLoc ass_tipo_cori_voce_ep%rowtype;
  aContoEpLoc voce_ep%rowtype;
  aSezioneLoc char(1);
  aImportoLoc number(15,2);
  aVoceIvaCredito voce_ep%rowtype;
Begin
  -- Nelle scritture di rateo delle fatture attive l'IVA non gira nella scrittura in esercizio precedente
  -- Fix del 13/11/2003 e successiva del 19/02/2004
  --  se l'esercizio n-1 non ? chiuso, il documento ? con comp. in es. prec, l'IVA su fatture attive e passive non viene gestita
  --  infatti in questo caso  la scrittura viene fatta al 3112(n-1) come RATEO parte 1

If not (CNRCTB200.ISCHIUSURACOEPDEF(aDocTst.esercizio-1, aDocTst.cd_cds_origine)= 'Y') -- SE L'ESERCIZIO PRECEDENTE E' APERTO
   And (CNRCTB204.getCompetenzaFuoriEsercizio(aDocTst) Or -- COMPETENZA COMPLETAMENTE FUORI ESERCIZIO
        CNRCTB204.getCompetenzaacavalloconEsPrec(aDocTst)) -- OPPURE COMPETENZA A CAVALLO
        -- E TIPO DOCUMENTO UGUALE A FATTURA ATTIVA, PASSIVA O COMPENSO
   And (aDocTst.cd_tipo_documento = CNRCTB100.TI_FATTURA_ATTIVA Or aDocTst.cd_tipo_documento = CNRCTB100.TI_FATTURA_PASSIVA Or -- SOLO IVA
       (aDocTst.cd_tipo_documento = CNRCTB100.TI_COMPENSO And
                (aCori.TI_ENTE_PERCEPIENTE = TI_CORI_PERCIPIENTE Or
                 aCori.cd_contributo_ritenuta = CNRCTB015.GETVAL01PERCHIAVE(CNRCTB575.TI_CORI_SPECIALE,CNRCTB575.TI_CORI_IVA)))) Then
Dbms_Output.PUT_LINE ('ESCO DA buildMovPEP CON CORI');
   Return;

elsif (nvl(aCori.stato_coge_docamm,' ') = CNRCTB100.STATO_COEP_EXC) then
				Dbms_Output.put_line ('ROSPUC ESCO SENZA CONTABILIZZARE CORI');
        return;
End If;

If aDocTst.cd_tipo_documento = CNRCTB100.TI_COMPENSO Then  -- se cori di compenso

------------------------------------------- tutto su compenso ---------------------------------------------

Declare
    aCompenso compenso%rowtype;
    aAssCoriEp ass_tipo_cori_voce_ep%rowtype;
    aCoriLoc contributo_ritenuta%rowtype;
    aListaMovimentiTemp CNRCTB200.movimentiList;
    aScrittura scrittura_partita_doppia%rowtype;
    aVoceAnag voce_ep%rowtype;
    aPgTerzo number(8);


Begin
	aVoceIvaCredito:=CNRCTB002.GETVOCEEPIVACredito(aDocTst.esercizio);
	select * into aCompenso
	from  compenso
	where esercizio=aDocTst.esercizio
	  and cd_cds=aDocTst.cd_cds
	  and cd_unita_organizzativa = aDocTst.cd_unita_organizzativa
	  and pg_compenso = aDocTst.pg_numero_documento
	  for update nowait;
dbms_output.put_line('ci arrivo');
    aAnticipo:=CNRCTB204.getAnticipo(aDocTst);

    -- Fix errore n.834 - 10/08/2004:blocco non valido per compenso con aticipo maggiore del netto missione
	-- Se il compenso ? senza mandato principale
	-- devo aprire il debito verso l'erario per i CORI subito
	-- Lo faccio con scritture indipendenti per ogni CORI tutte legate al compenso e con terzi in
	-- dipendenza del fatto che il cori sia ente o percipiente
	-- Viene registrata una scrittura PER ogni cori presente nel compenso

    If  aCompenso.im_totale_compenso <= 0 then

     aAssCoriEp:=CNRCTB204.getAssCoriEp(aCori.esercizio, aCori.cd_contributo_ritenuta,aCori.ti_ente_percepiente,CNRCTB204.getSezione(aDocTst, aCori));
     aContoEp:=CNRCTB002.getVoceEp(aAssCoriEp.esercizio, aAssCoriEp.cd_voce_ep_contr);
     aSezione:=CNRCTB200.getSezioneOpposta(CNRCTB204.getSezione(aDocTst, aCori)); -- Sezione opposta rispetto alla normale sezione CORI

     aImporto:=abs(aCori.ammontare);
     -- Se il CORI ? ENTE e negativo e il compenso ha totale compenso strettamente < 0,
	 -- bisogna generare un movimento sul conto di anagrafica del terzo ENTE
	 -- Per gli altri cori, il terzo da usare ? quello del CORI (terzo del compenso)
      Declare
       aUOENTE unita_organizzativa%rowtype;
       aCdTerzoEnte number(8);
      Begin
        If aCori.ti_ente_percepiente = TI_CORI_ENTE and aCompenso.im_totale_compenso < 0
	     and aCori.ammontare < 0 then
	   -- Estrae il terzo associato all'UO ENTE
           aUOENTE:=CNRCTB020.GETUOENTE(aCompenso.esercizio);
           CNRCTB080.getTerzoPerUO(aUOENTE.cd_unita_organizzativa, aCdTerzoEnte);
           aPgTerzo:=aCdTerzoEnte;
           if aCori.ammontare > 0 then
                   aVoceAnag:=CNRCTB204.TROVACONTOANAG(aDocTst.esercizio,aCdTerzoEnte, CNRCTB100.TI_GENERICO_ENTRATA,null,aCori.CD_ELEMENTO_VOCE_EV);
           else
                   aVoceAnag:=CNRCTB204.TROVACONTOANAG(aDocTst.esercizio,aCdTerzoEnte, CNRCTB100.TI_GENERICO_SPESA,null,aCori.CD_ELEMENTO_VOCE_EV);
           end if;
	else -- Tipo cori percipiente
           aPgTerzo:=aCori.cd_terzo;
           DBMS_OUTPUT.PUT_LINE('CORI EV pos'||aCori.CD_ELEMENTO_VOCE_EV);
	   if aCori.ammontare > 0 then
                aVoceAnag:=CNRCTB204.TROVACONTOANAG(aDocTst.esercizio,aCori.cd_terzo, CNRCTB100.TI_GENERICO_ENTRATA,null,aCori.CD_ELEMENTO_VOCE_EV);
           else
                aVoceAnag:=CNRCTB204.TROVACONTOANAG(aDocTst.esercizio,aCori.cd_terzo, CNRCTB100.TI_GENERICO_SPESA,null,aCori.CD_ELEMENTO_VOCE_EV);
           end if;
	end if;

      aListaMovimentiTemp.delete;
      aScrittura:=null;
      -- Crea il movimento sul patrimoniale dell'anagrafica del cori
Dbms_Output.PUT_LINE ('AA '||aContoEp.CD_VOCE_EP);
      CNRCTB204.buildMovPrinc(
       aDocTst.cd_cds_origine,
       aDocTst.esercizio,
       aDocTst.cd_uo_origine,
       aContoEp,
       aImporto,
       aSezione,
       aCori.dt_da_competenza_coge,
       aCori.dt_a_competenza_coge,
       aPgTerzo,
       aCori.ti_istituz_commerc,
       aListaMovimentiTemp,
       aUser,
       aTSnow);

Dbms_Output.PUT_LINE ('BB '||aVoceAnag.CD_VOCE_EP);
      CNRCTB204.buildMovPrinc(
       aDocTst.cd_cds_origine,
       aDocTst.esercizio,
       aDocTst.cd_uo_origine,
       aVoceAnag,
       aImporto,
       CNRCTB200.getSezioneOpposta(aSezione),
       aCori.dt_da_competenza_coge,
       aCori.dt_a_competenza_coge,
       aPgTerzo,
       aCompenso.ti_istituz_commerc,
       aListaMovimentiTemp,
       aUser,
       aTSnow);

      -- Imposta l'ultimo movimento come movimento terzo della scrittura
      aListaMovimentiTemp(2).FL_MOV_TERZO:='Y';

      aScrittura:=CNRCTB204.buildScrPEP(aDocTst,aPgTerzo,aUser,aTSNow);
      if CNRCTB204.getCompetenzaFuoriEsercizio(aDocTst) and CNRCTB008.ISESERCIZIOAPERTO(aDocTst.esercizio - 1,aDocTst.cd_cds_origine) then
       IBMERR001.RAISE_ERR_GENERICO('Compenso senza costo principale con competenza in esercizio precedente aperto non processabile come RATEO parte I');
      else
       CNRCTB200.CREASCRITTCOGE(aScrittura,aListaMovimentiTemp);
      end if;

      if aListaMovimentiTemp.count > 0 then
       aListaNuoveScritture(aListaNuoveScritture.count+1):=aScrittura;
      end if;

      -- ARRIVATO QUI HO GENERATO LA SCRITTURA PER IL CORI ED ESCO
      return;

      End;

    Else  -- dell'If  aCompenso.im_totale_compenso <= 0 then

         -- Fix errore n.834 - 10/08/2004
         -- Se il compenso ha anticipo maggiore del netto percipiente
	 -- devo aprire il debito verso l'erario per i CORI subito e nella stessa scrittura del compenso
	 If (aAnticipo.pg_anticipo is not Null and aAnticipo.im_anticipo > aCompenso.im_netto_percipiente) Then

    Begin
                aAssCoriEpLoc:=CNRCTB204.getAssCoriEp(aCori.esercizio, aCori.cd_contributo_ritenuta,aCori.ti_ente_percepiente,CNRCTB204.getSezione(aDocTst, aCori));
                aContoEpLoc:=CNRCTB002.getVoceEp(aAssCoriEpLoc.esercizio, aAssCoriEpLoc.cd_voce_ep_contr);
                aSezioneLoc:=CNRCTB200.getSezioneOpposta(CNRCTB204.getSezione(aDocTst, aCori)); -- Sezione opposta rispetto alla normale sezione CORI
                aImportoLoc:=abs(aCori.ammontare);
Dbms_Output.PUT_LINE ('CC '||aContoEpLoc.CD_VOCE_EP);
                CNRCTB204.buildMovPrinc(aDocTst.cd_cds_origine,
                                        aDocTst.esercizio,
                                        aDocTst.cd_uo_origine,
                                        aContoEpLoc,
                                        aImportoLoc,
                                        aSezioneLoc,
                                        aCori.dt_da_competenza_coge,
                                        aCori.dt_a_competenza_coge,
                                        aCori.cd_terzo,
                                        aCori.ti_istituz_commerc,
                                        aListaMovimenti,
                                        aUser,
                                        aTSnow);
	  End;

    End If;

         -- Il compenso CON mandato principale non scorpora i CORI percipiente dal corpo
         If aCori.ti_ente_percepiente = TI_CORI_PERCIPIENTE then
 	  Return;
	 End If;

         -- Se si tratta di CORI ENTE POSITIVO normale gestione CORI
				 aContoEp:=CNRCTB204.trovaContoEp(aDocTst, aCori);
				 Dbms_Output.PUT_LINE ('Se si tratta di CORI ENTE POSITIVO normale gestione CORI '||aContoEp.CD_VOCE_EP||' '||ACORI.CD_ELEMENTO_VOCE_EV);
         aSezione:=CNRCTB204.getSezione(aDocTst, aCori);
         aImporto:=abs(aCori.ammontare);

    End If; -- dell'If  aCompenso.im_totale_compenso <= 0 then

Exception when NO_DATA_FOUND then
     IBMERR001.RAISE_ERR_GENERICO('Compenso non trovato:'||aDocTst.pg_numero_documento||' uo:'||aDocTst.cd_unita_organizzativa||' es:'||aDocTst.esercizio);
End;

---------------------------------------------- fine tutto su compenso ---------------------------------------

Else -- Cori non di compenso

   aContoEp:=CNRCTB204.trovaContoEp(aDocTst, aCori);
   aSezione:=CNRCTB204.getSezione(aDocTst, aCori);
   aImporto:=abs(aCori.ammontare);

End If;

-- 12.12.2006 (SF) SE IL DOCUMENTO HA COMPETENZA (ANCHE PARZIALMENTE) IN ANNO PRECEDENTE MA L'ESERCIZIO PRECEDENTE E' CHIUSO
--                 NON POTENDO FARE IL RATEO IMPUTA IL COSTO/RICAVO NELL'ESERCIZIO IN CORSO MA ANCHE LE DATE DI COMPETENZA
--                 ECONOMICA DEVONO APPARTENERE ALL'ESERCIZIO

If (CNRCTB204.getCompetenzaFuoriEsercizio(aDocTst) Or CNRCTB204.getCompetenzaacavalloconEsPrec(aDocTst)) And
    Not CNRCTB008.ISESERCIZIOAPERTOSenzaBlocco(aDocTst.esercizio - 1, aDocTst.cd_cds_origine) Then

    If To_Char(aCori.dt_da_competenza_coge, 'YYYY') < aDocTst.esercizio Then
        DA_COMP := To_Date('0101'||aDocTst.esercizio, 'DDMMYYYY');
    Else
        DA_COMP := aCori.dt_da_competenza_coge;
    End If;

    If To_Char(aCori.dt_a_competenza_coge, 'YYYY') < aDocTst.esercizio Then
        A_COMP := To_Date('0101'||aDocTst.esercizio, 'DDMMYYYY');
    Else
        A_COMP := aCori.dt_a_competenza_coge;
    End If;
else
		DA_COMP := aCori.dt_da_competenza_coge;
    A_COMP := aCori.dt_a_competenza_coge;
End If;

Dbms_Output.PUT_LINE ('DD '||aContoEp.CD_VOCE_EP);
  CNRCTB204.buildMovPrinc(
   aDocTst.cd_cds_origine,
   aDocTst.esercizio,
   aDocTst.cd_uo_origine,
   aContoEp,
   aImporto,
   aSezione,
   DA_COMP, -- aCori.dt_da_competenza_coge,
   A_COMP,  -- aCori.dt_a_competenza_coge,
   aCori.cd_terzo,
   aCori.ti_istituz_commerc,
   aListaMovimenti,
   aUser,
   aTSnow);

  if(aDocTst.cd_tipo_documento not in(CNRCTB100.TI_FATTURA_ATTIVA,CNRCTB100.TI_FATTURA_PASSIVA)) then
  if  (aContoEp.CD_VOCE_EP= aVoceIvaCredito.cd_voce_ep )then
   	null;
  else
	  	aContoEpLoc:=CNRCTB204.trovaContoContrEp(aDocTst.esercizio,null,null,null,aContoEp.CD_VOCE_EP);

	   aSezioneLoc:=CNRCTB200.getSezioneOpposta(CNRCTB204.getSezione(aDocTst, aCori)); -- Sezione opposta rispetto alla normale sezione CORI
	   aImportoLoc:=abs(aCori.ammontare);

	   Dbms_Output.PUT_LINE ('DD contr '||aContoEpLoc.CD_VOCE_EP);

	  CNRCTB204.buildMovPrinc(
	   aDocTst.cd_cds_origine,
	   aDocTst.esercizio,
	   aDocTst.cd_uo_origine,
	   aContoEpLoc,
	   aImportoLoc,
	   aSezioneLoc,
	   DA_COMP, -- aCori.dt_da_competenza_coge,
	   A_COMP,  -- aCori.dt_a_competenza_coge,
	   aCori.cd_terzo,
	   aCori.ti_istituz_commerc,
	   aListaMovimenti,
	   aUser,
	   aTSnow);
	 end if;
 end if;
 End;

/* stani */

Procedure buildMovPEPAutofattura(aListaNuoveScritture IN OUT CNRCTB200.scrittureList, aListaMovimenti IN OUT CNRCTB200.movimentiList,
                                 aDocTst V_DOC_AMM_COGE_TSTA%rowtype, aCori V_DOC_AMM_COGE_CORI%rowtype, aUser varchar2, aTSnow date) is
  aContoEp       voce_ep%rowtype;
  aVoceIvaDebito voce_ep%rowtype;
  aImporto       NUMBER(15,2);
  aSezione       CHAR(1);
  aAnticipo      anticipo%rowtype;

Begin
  -- Nelle scritture di rateo delle fatture attive l'IVA non gira nella scrittura in esercizio precedente
  -- Fix del 13/11/2003 e successiva del 19/02/2004
  --  se l'esercizio n-1 non ? chiuso, il documento ? con comp. in es. prec, l'IVA su fatture attive e passive non viene gestita
  --  infatti in questo caso  la scrittura viene fatta al 3112(n-1) come RATEO parte 1
If CNRCTB204.getCompetenzaFuoriEsercizio(aDocTst) And
   Not (CNRCTB200.ISCHIUSURACOEPDEF(aDocTst.esercizio-1,aDocTst.cd_cds_origine) = 'Y') and
   (aDocTst.cd_tipo_documento = CNRCTB100.TI_FATTURA_ATTIVA or aDocTst.cd_tipo_documento = CNRCTB100.TI_FATTURA_PASSIVA) Then
   Return;
End if;

   aContoEp := CNRCTB204.trovaContoEp(aDocTst, aCori);
   aSezione := CNRCTB204.getSezione(aDocTst, aCori);
   aImporto := abs(aCori.ammontare);
Dbms_Output.PUT_LINE ('EE '||aContoEp.CD_VOCE_EP);
   CNRCTB204.buildMovPrinc(aDocTst.cd_cds_origine, aDocTst.esercizio, aDocTst.cd_uo_origine, aContoEp, aImporto, aSezione,
                           aCori.dt_da_competenza_coge, aCori.dt_a_competenza_coge, aCori.cd_terzo, aCori.ti_istituz_commerc,
                           aListaMovimenti, aUser, aTSnow);

   aVoceIvaDebito := CNRCTB002.GETVOCEEPIVADEBITO(aDocTst.esercizio);
Dbms_Output.PUT_LINE ('FF '||aVoceIvaDebito.CD_VOCE_EP);
   CNRCTB204.buildMovPrinc(aDocTst.cd_cds_origine, aDocTst.esercizio, aDocTst.cd_uo_origine, aVoceIvaDebito, aImporto,
                           CNRCTB200.getSezioneOpposta(aSezione), aCori.dt_da_competenza_coge, aCori.dt_a_competenza_coge,
                           aCori.cd_terzo, aCori.ti_istituz_commerc, aListaMovimenti, aUser, aTSnow);

End;



/* stani */


 -- =======================================
 --
 -- CONTROPARTITA
 --
 -- =======================================

 Procedure buildMovContrPEP(aListaMovimenti IN OUT CNRCTB200.movimentiList,
                            aDocTst V_DOC_AMM_COGE_TSTA%rowtype, aCdTerzo number, aUser varchar2,
                            aTSnow date, ACdElementoVoce varchar2) is
  aVoceEP       voce_ep%rowtype;
  aImporto_C    number(15,2);
  aImporto_I    number(15,2);
  aMovimento    movimento_coge%rowtype;
  aSezPrinc     char(1);

  aTotAvere_C   NUMBER(15,2);
  aTotAvere_I   NUMBER(15,2);

  aTotDare_C    NUMBER(15,2);
  aTotDare_I    NUMBER(15,2);

  aTiIstituzCommerc char(1) := CNRCTB100.TI_COMMERCIALE;

  aRecCompenso compenso%Rowtype;

 begin
      if aListaMovimenti.count = 0 then
Dbms_Output.PUT_LINE ('buildMovContrPEP ESCO');
       return;
      End if;

      if aListaMovimenti(1).dt_a_competenza_coge is null then
       IBMERR001.RAISE_ERR_GENERICO('Data fine competenza non specificata su primo movimento della scrittura');
      end if;

       -- Accumula i movimenti di controparte

         aTotAvere_C := 0;
         aTotAvere_I := 0;

         aTotDare_C  := 0;
         aTotDare_I  := 0;

       For k in 1 .. aListaMovimenti.count loop

Dbms_Output.PUT_LINE ('buildMovContrPEP LOOP K '||ACdElementoVoce);

       If aListaMovimenti(k).sezione = CNRCTB200.IS_DARE Then

        If aListaMovimenti(k).TI_ISTITUZ_COMMERC = 'C' Then
          aTotDare_C := aTotDare_C + aListaMovimenti(k).im_movimento;
        Elsif aListaMovimenti(k).TI_ISTITUZ_COMMERC = 'I' Then
          aTotDare_I := aTotDare_I + aListaMovimenti(k).im_movimento;
        End If;

       Elsif aListaMovimenti(k).sezione = CNRCTB200.IS_AVERE Then

        If aListaMovimenti(k).TI_ISTITUZ_COMMERC = 'C' Then
          aTotAvere_C := aTotAvere_C + aListaMovimenti(k).im_movimento;
        Elsif aListaMovimenti(k).TI_ISTITUZ_COMMERC = 'I' Then
          aTotAvere_I := aTotAvere_I + aListaMovimenti(k).im_movimento;
        End If;

       End if;

       End loop;

Dbms_Output.PUT_LINE ('buildMovContrPEP DOPO END LOOP K');

Dbms_Output.PUT_LINE ('VEDIAMO VALORI '||Nvl(aTotDare_C, 0)||' '||Nvl(aTotAvere_C, 0)||' '||Nvl(aTotDare_I, 0)||' '||Nvl(aTotAvere_I, 0));

       -- Se il tot dare dei movimenti supera il tot avere, la sezione principale ? dare (contropartita in avere)
       if aTotDare_C > aTotAvere_C Or aTotDare_I > aTotAvere_I then
	   aSezPrinc := CNRCTB200.IS_DARE;
       elsif aTotDare_C < aTotAvere_C Or aTotDare_I < aTotAvere_I then
       -- Se la partita doppia ? gi? chiusa non devo generare movimenti
	   aSezPrinc := CNRCTB200.IS_AVERE;
       Else
Dbms_Output.PUT_LINE ('buildMovContrPEP DOPO ESCO 2');
	   return;
       end if;

       -- Nel caso:
       --    la competenza economica del documento sia in esercizio precedente (data fine periodo),
       --    l'esercizio precedente sia il primo esercizio dell'applicazione
          --  -> Viene recuperato il conto di contropartita dei crediti/debiti iniziali
       -- altrimenti
	   --  se la competenza in esercizio precedente aperto
       --    -> ratei attivi/passivi o fatture da emettere per fattura attiva
       --  altrimenti
	  --    -> contropartita anagrafica

-- Modifica del 25/08/2003 -> se la competenza ? in esercizio precedente e l'esercizio attuale
-- ? il primo dell'applicazione il conto di costo/ricavo ? SP Iniziale

If CNRCTB204.getCompetenzaFuoriEsercizio(aDocTst) and aDocTst.esercizio = CNRCTB008.ESERCIZIO_PARTENZA then

Dbms_Output.PUT_LINE ('buildMovContrPEP COMP FUORI PARTENZA');

      -- Modifica del 13/11/2003 -> se si tratta di fattura attiva in esercizio precedente e
      -- l'esercizio attuale ? il primo dell'applicazione
      -- Devo USARE il conto dei criditi da terzo (legato ad anagrafica)

     If aDocTst.cd_tipo_documento = CNRCTB100.TI_FATTURA_ATTIVA Then
        aVoceEP := CNRCTB204.trovaContoContrEp(aDocTst, aCdTerzo,ACdElementoVoce);
       -- Modifica del 19/02/2004 -> quello che ? fatto per la fattura attiva ? fatto anche per
       -- quella passiva
     Elsif aDocTst.cd_tipo_documento = CNRCTB100.TI_FATTURA_PASSIVA Then
        aVoceEP:=CNRCTB204.trovaContoContrEp(aDocTst, aCdTerzo,ACdElementoVoce);
     Else
	if CNRCTB100.GETSEZIONEECONOMICA(aDocTst.cd_tipo_documento) = CNRCTB100.IS_DARE then
         aVoceEP:=CNRCTB002.GETVOCEEPDEBITIINIZIALI(aDocTst.esercizio);
        else
         aVoceEP:=CNRCTB002.GETVOCEEPCREDITIINIZIALI(aDocTst.esercizio);
        end if;
     End if;

Else

   -- Competenza in es precedente NON CHIUSO e competenza economica o completamente
   -- nell'anno precedente o a cavallo tra il precedente ed il corrente

Dbms_Output.PUT_LINE ('buildMovContrPEP COMP FUORI MA NON PARTENZA');

    If Not (CNRCTB200.ISCHIUSURACOEPDEF(aDocTst.esercizio - 1, aDocTst.cd_cds_origine) = 'Y') And
            (CNRCTB204.getCompetenzaFuoriEsercizio(aDocTst)) Then

Dbms_Output.PUT_LINE ('buildMovContrPEP 111');

-- RECUPERA SOLO IL COMPENSO

         If aDocTst.cd_tipo_documento = CNRCTB100.TI_COMPENSO Then
Dbms_Output.PUT_LINE ('buildMovContrPEP 222');
          aRecCompenso := CNRCTB545.getCompenso(aDocTst.cd_cds,
                                                aDocTst.cd_unita_organizzativa,
                                                aDocTst.esercizio,
                                                aDocTst.PG_NUMERO_DOCUMENTO,
                                                'N' /* LOCK S/N */);
         End If;

   -- per la fattura attiva estrae il conto delle fatture da emettere

Dbms_Output.PUT_LINE ('buildMovContrPEP 333');

      if aDocTst.cd_tipo_documento = CNRCTB100.TI_FATTURA_ATTIVA Then
Dbms_Output.PUT_LINE ('buildMovContrPEP 333');
         aVoceEP := CNRCTB002.GETVOCEEPFATTUREDAEMETTERE(aDocTst.esercizio);
        -- Modifica del 19/02/2004 -> quello che ? fatto per la fattura attiva ? fatto
        -- anche per  quella passiva
      elsif aDocTst.cd_tipo_documento = CNRCTB100.TI_FATTURA_PASSIVA Or
           (aDocTst.cd_tipo_documento = CNRCTB100.TI_COMPENSO And aRecCompenso.FL_GENERATA_FATTURA = 'Y') Then
Dbms_Output.PUT_LINE ('buildMovContrPEP 444');
        aVoceEP := CNRCTB002.GETVOCEEPFATTUREDARICEVERE(aDocTst.esercizio);
      Else
      Dbms_Output.PUT_LINE ('buildMovContrPEP 555');
        -- altrimenti i conti di rateo
	  if aSezPrinc = CNRCTB200.IS_DARE then
	      aVoceEP:=CNRCTB002.GETVOCEEPRATEIPASSIVI(aDocTst.esercizio);
	  else
	      aVoceEP:=CNRCTB002.GETVOCEEPRATEIATTIVI(aDocTst.esercizio);
	  end if;
      end if;

    else -- Per gli altri documenti ritorna il conto di contropartita anagrafica

Dbms_Output.PUT_LINE ('buildMovContrPEP CONTO ANAGRAFICA '||ACdElementoVoce);

        aVoceEP := CNRCTB204.trovaContoContrEp(aDocTst, aCdTerzo,ACdElementoVoce);

Dbms_Output.PUT_LINE ('dopo buildMovContrPEP CONTO ANAGRAFICA');
        -- Fix errore 834: nel caso il compenso sia con anticipo maggiore del netto percipiente,
        -- bisogna utilizzare il conto di credito e non di debito
	-- (quest'ultimo naturalmente associato al compenso)
	-- Per questo motivo nel metodo seguente viene utilizzato il tipo GENERICO di ENTRATA perch?
	-- permette naturalmente di estrarre un conto di credito
	if aDocTst.cd_tipo_documento = CNRCTB100.TI_COMPENSO then
	   if CNRCTB204.isCompConAntMaggNetto(aDocTst) = 'Y' then
	       if(recParametriCnr.fl_nuovo_pdg='Y' ) then
	         	aVoceEP := CNRCTB204.trovaContoAnag(aDocTst.esercizio, aCdTerzo, aDocTst.cd_tipo_documento, aDocTst.ti_fattura,ACdElementoVoce);
	       else
	     			aVoceEP := CNRCTB204.trovaContoAnag(aDocTst.esercizio, aCdTerzo, CNRCTB100.TI_GENERICO_ENTRATA, aDocTst.ti_fattura,ACdElementoVoce);
						Dbms_Output.PUT_LINE ('buildMovContrPEP CAMBIA CON '||aVoceEP.CD_VOCE_EP);
				 end if;
	   end if;
	end if;
     End if;

End if;

      aImporto_C := abs(aTotDare_C - aTotAvere_C);
      aImporto_I := abs(aTotDare_I - aTotAvere_I);

If aImporto_I > 0 Then
      aMovimento.CD_CDS         := aListaMovimenti(1).cd_cds;
      aMovimento.ESERCIZIO      := aListaMovimenti(1).esercizio;
      aMovimento.CD_UNITA_ORGANIZZATIVA := aListaMovimenti(1).cd_unita_organizzativa;
      aMovimento.PG_SCRITTURA   := null;
      aMovimento.PG_MOVIMENTO   := null;

      aMovimento.CD_VOCE_EP     := aVoceEP.cd_voce_ep;
      aMovimento.SEZIONE        := CNRCTB200.getSezioneOpposta(aSezPrinc);
      aMovimento.IM_MOVIMENTO   := aImporto_I;

      aMovimento.CD_TERZO       := aListaMovimenti(1).cd_terzo;
      aMovimento.DT_DA_COMPETENZA_COGE := null;
      aMovimento.DT_A_COMPETENZA_COGE  := null;
      aMovimento.ti_istituz_commerc    := CNRCTB100.TI_ISTITUZIONALE; --aTiIstituzCommerc;
      aMovimento.STATO          := CNRCTB200.STATO_DEFINITIVO;
      aMovimento.FL_MOV_TERZO   := 'Y';
		  Dbms_Output.PUT_LINE ('movimento '||aVoceEP.CD_VOCE_EP||' importo '||aImporto_I);
      aListaMovimenti(aListaMovimenti.COUNT + 1) := aMovimento;
End If;

If aImporto_C > 0 Then
      aMovimento.CD_CDS         := aListaMovimenti(1).cd_cds;
      aMovimento.ESERCIZIO      := aListaMovimenti(1).esercizio;
      aMovimento.CD_UNITA_ORGANIZZATIVA := aListaMovimenti(1).cd_unita_organizzativa;
      aMovimento.PG_SCRITTURA   := null;
      aMovimento.PG_MOVIMENTO   := null;

      aMovimento.CD_VOCE_EP     := aVoceEP.cd_voce_ep;
      aMovimento.SEZIONE        := CNRCTB200.getSezioneOpposta(aSezPrinc);
      aMovimento.IM_MOVIMENTO   := aImporto_C;

      aMovimento.CD_TERZO       := aListaMovimenti(1).cd_terzo;
      aMovimento.DT_DA_COMPETENZA_COGE := null;
      aMovimento.DT_A_COMPETENZA_COGE  := null;
      aMovimento.ti_istituz_commerc    := CNRCTB100.TI_COMMERCIALE; --aTiIstituzCommerc;
      aMovimento.STATO          := CNRCTB200.STATO_DEFINITIVO;
      aMovimento.FL_MOV_TERZO   := 'Y';

      aListaMovimenti(aListaMovimenti.COUNT + 1) := aMovimento;
End If;

End;

 -- Motori di registrazione delle prime scritture

 function chkBuonoPerTrasferimento
   (aBSDett buono_carico_scarico_dett%rowtype
   ) RETURN VARCHAR2 IS
  isTrasferimento CHAR(1);
 Begin
  select B.fl_buono_per_trasferimento into isTrasferimento
  from   buono_carico_scarico A, tipo_carico_scarico B
  where  A.pg_inventario = aBSDett.pg_inventario and
         A.ti_documento = aBSDett.ti_documento and
         A.esercizio = aBSDett.esercizio and
         A.pg_buono_c_s = aBSDett.pg_buono_c_s AND
         B.cd_tipo_carico_scarico = A.cd_tipo_carico_scarico;
  return isTrasferimento;
 Exception
   when others then
        IBMERR001.RAISE_ERR_GENERICO('Errore in recupero del tipo buono di carico/scarico');
 End chkBuonoPerTrasferimento;


Procedure regAnnullaDocEsChiusoCOGE(aEs number, aDocTst V_DOC_AMM_COGE_TSTA%rowtype, aUser varchar2, aTSNow date) is
  aScRittura scrittura_partita_doppia%rowtype;
  aScritturaNew scrittura_partita_doppia%rowtype;
  aListaMovimenti CNRCTB200.movimentiList;
  aNewListaMovimenti CNRCTB200.movimentiList;
  aMovimento movimento_coge%rowtype;
  aVoceAnag voce_ep%rowtype;
  aVoceSopr voce_ep%rowtype;
  aVoceSoprA voce_ep%rowtype;
  aVoceSoprP voce_ep%rowtype;
  aListaScritture CNRCTB200.scrittureList;
  aImMovimento number(15,2);
  aImPagatoIncassato number(15,2);
  aCdElementoVoce elemento_voce.cd_elemento_voce%type;
  aSezMovimento char(1);
  aTiIstituzCommerc char(1);
  aCdTerzo number(8);
  aNum number;
 begin
  -- Lock del documento
  CNRCTB100.LOCKDOCAMM(
   aDocTst.cd_tipo_documento,
   aDocTst.cd_cds,
   aDocTst.esercizio,
   aDocTst.cd_unita_organizzativa,
   aDocTst.pg_numero_documento
  );

  begin
   select 1 into aNum from v_unita_organizzativa_valida where
             cd_unita_organizzativa=aDocTst.cd_cds_origine
		 and esercizio=aEs
		 and fl_cds='Y';
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('(1) Il cds '||aDocTst.cd_cds_origine||' non ? pi? valido nell''esercizio ('||aEs||') in cui ? stato richiesto di anullare il documento');
  end;

  if not CNRCTB008.ISESERCIZIOAPERTOOCHIUSO(aEs, aDocTst.cd_cds_origine) then
   IBMERR001.RAISE_ERR_GENERICO('L''esercizio ('||aEs||') per il cds '||aDocTst.cd_cds_origine||' in cui ? stato annullato il documento non ? ancora aperto o chiuso');
  end if;

  if not (CNRCTB200.ISCHIUSURACOEPDEF(aEs-1,aDocTst.cd_cds_origine)='Y') then
   IBMERR001.RAISE_ERR_GENERICO('(1) L''esercizio economico di origine del documento amministrativo ('||To_Char(aEs-1)||') non ? chiuso definitivamente');
  end if;

  if not (CNRCTB200.ISCHIUSURACOEPDEF(aDocTst.esercizio,aDocTst.cd_cds_origine)='Y') then
   IBMERR001.RAISE_ERR_GENERICO('(2) L''esercizio economico di origine del documento amministrativo ('||aDocTst.esercizio||') non ? chiuso definitivamente');
  end if;

  -- Lock del documento
  CNRCTB100.LOCKDOCAMM(
   aDocTst.cd_tipo_documento,
   aDocTst.cd_cds,
   aDocTst.esercizio,
   aDocTst.cd_unita_organizzativa,
   aDocTst.pg_numero_documento
  );

  -- Se gi? processato in economica esco
  If aDocTst.stato_coge in (CNRCTB100.STATO_COEP_CON, CNRCTB100.STATO_COEP_EXC) Then
   Return;
  End If;

  -- Se il documento NON ? riportato l'operazione in COGE non ? possibile
  If CNRCTB105.isRiportato(aDocTst.cd_cds,aDocTst.cd_unita_organizzativa, aDocTst.esercizio, aDocTst.pg_numero_documento, aDocTst.cd_tipo_documento) = 'N' then
   IBMERR001.RAISE_ERR_GENERICO('Documento non processabile con tale funzione perch? non riportato ('||
        aDocTst.cd_cds||'/'||aDocTst.cd_unita_organizzativa||'/'||aDocTst.esercizio||'/'||aDocTst.pg_numero_documento||'/'||
        aDocTst.cd_tipo_documento||' e origine '||aDocTst.CD_CDS_ORIGINE||'/'||aDocTst.CD_uo_ORIGINE||')');
  End If;

  -- Se mai stato processato in economica (stato N) sollevo eccezione
  If aDocTst.stato_coge in (CNRCTB100.STATO_COEP_INI) Then
   IBMERR001.RAISE_ERR_GENERICO('Documento mai processato in economica');
  End if;

  -- verifico che il documento sia stato riportato all'esercizio di annullamento e non oltre (o non ancora)
  if not (CNRCTB105.GETSTATORIPORTATOINSCRIVANIA(aDocTst.cd_cds, aDocTst.cd_unita_organizzativa, aDocTst.esercizio,
                                                 aDocTst.pg_numero_documento, aDocTst.cd_tipo_documento, aEs) =
                                                                CNRCTB105.COMPLETAMENTE_RIPORTATO) then
   IBMERR001.RAISE_ERR_GENERICO('Il documento amministrativo risulta (anche riportato) in un esercizio diverso da quello di registrazione economica dell''annullamento');
  end if;

  If Not aDocTst.stato_cofi = CNRCTB100.STATO_GEN_COFI_ANN And Not aDocTst.fl_congelata = 'Y' Then
   IBMERR001.RAISE_ERR_GENERICO('Il documento da processare deve essere in stato annullato (congelato). Il documento in processo non ? in questo stato');
  End If;

  -- cerca eventuali scritture di annullamento gia registrate per il documento in esercizi precedenti
  CNRCTB204.getScritturePEPLock(null,aDocTst,CNRCTB200.CAU_ANNULL_ES_CHIUSO,aListaScritture);

  if aListaScritture.count > 0 then
   CNRCTB100.UPDATEDOCAMM_NODUVAUTUV(
    aDocTst.cd_tipo_documento,
    aDocTst.cd_cds,
    aDocTst.esercizio,
    aDocTst.cd_unita_organizzativa,
    aDocTst.pg_numero_documento,
    'stato_coge='''||CNRCTB100.STATO_COEP_CON||'''',
    null
   );

   CNRCTB100.UPDATEDOCAMM_NODUVAUTUV(
    aDocTst.cd_tipo_documento,
    aDocTst.cd_cds,
    aDocTst.esercizio,
    aDocTst.cd_unita_organizzativa,
    aDocTst.pg_numero_documento,
    'stato_coan='''||CNRCTB100.STATO_COEP_CON||'''',
    null
   );
   return;
  end if;

/* INVERTITI IN DATA 23.05.2006 SU RICHIESTA DI FRANCA. IN SOSTANZA CAMBIANO I CONTI MA NON LA SOSTANZA.
   - ANZICHE' REGISTRARE MENO RICAVI SI REGISTRANO PIU' COSTI;
   - ANZICHE' REGISTRARE MENO COSTI SI REGISTRANO PIU' RICAVI; */

  aVoceSoprA := CNRCTB002.GETVOCEEPINSUSSPASSIVE(aEs); --CNRCTB002.GETVOCEEPINSUSSATTIVE(aEs);
  aVoceSoprP := CNRCTB002.GETVOCEEPINSUSSATTIVE(aEs); --CNRCTB002.GETVOCEEPINSUSSPASSIVE(aEs);

  -- Devo girare sulle insussistenze dell'esercizio specificato aEs la quota non pagata del documento
  -- Se si tratta di compenso/missione/anticipo/rimborso
  -- recupero la scrittura di origine e leggo da l? l'importo terzo da girare sulle insussistenze
  -- utilizzando l'associazione anagrafica conto dell'anno aEs
  -- Carica le scritture originali del documento

  CNRCTB204.GETSCRITTUREPEPLOCK(aDocTst,aListaScritture);

  For k in 1 ..  aListaScritture.count Loop
   aScrittura := aListaScritture(k);
   aNewListaMovimenti.delete;
   aListaMovimenti.delete;
   CNRCTB200.GETSCRITTURAEPLOCK(aScrittura,aListaMovimenti);
   aImMovimento := Null;
   aSezMovimento := Null;
   aTiIstituzCommerc := Null;

   For i in 1 .. aListaMovimenti.count Loop
     If aListaMovimenti(i).fl_mov_terzo is not null and aListaMovimenti(i).fl_mov_terzo = 'Y' then
       aImMovimento:=aListaMovimenti(i).im_movimento;
       aSezMovimento:=aListaMovimenti(i).sezione;
       aTiIstituzCommerc:=aListaMovimenti(i).ti_istituz_commerc;
     End if;
   End Loop;

   if aImMovimento is null then
    IBMERR001.RAISE_ERR_GENERICO('Errore in determinazione dell''importo movimento creditore/debitore della scrittura originale del documento');
   end if;

   -- Se il documento ? generico o fattura devo determinare la quota PAGATA per le righe del terzo specificato
   If aScrittura.cd_tipo_documento in (CNRCTB100.TI_FATTURA_PASSIVA, CNRCTB100.TI_FATTURA_ATTIVA) Or
                        CNRCTB100.ISINTABELLAGENERICO(aScrittura.cd_tipo_documento) = 'Y' then
    aImPagatoIncassato := 0;

    Begin
     select im_pagato_incassato,Cd_Elemento_Voce
      into aImPagatoIncassato,aCdElementoVoce
      from V_DOC_AMM_COGE_PAGATO where
                 esercizio=aScrittura.esercizio_documento_amm
			 and cd_cds = aScrittura.cd_cds_documento
			 and cd_unita_organizzativa  =aScrittura.cd_uo_documento
			 and cd_tipo_documento = aScrittura.cd_tipo_documento
			 and pg_numero_documento = aScrittura.pg_numero_documento
			 and cd_terzo = aScrittura.cd_terzo;
 	 aImMovimento := aImMovimento - aImPagatoIncassato;
    Exception when NO_DATA_FOUND then
     null;
    End;
   End If;

   if aImMovimento <= 0 then
    IBMERR001.RAISE_ERR_GENERICO('Importo da girare su sopravvenienze minore o uguale a zero');
   end if;

   aCdTerzo:=aScrittura.cd_terzo;



   aVoceAnag:=CNRCTB204.TROVACONTOANAG(aEs,aCdTerzo,aDocTst.cd_tipo_documento,aDocTst.ti_fattura,aCdElementoVoce);

   CNRCTB204.buildMovPrinc(aScrittura.cd_cds,aEs,aScrittura.cd_unita_organizzativa,aVoceAnag,
                           aImMovimento,CNRCTB200.GETSEZIONEOPPOSTA(aSezMovimento),trunc(aTSnow),
                           trunc(aTSNow),0,aTiIstituzCommerc,aNewListaMovimenti,aUser,aTSnow);
   if CNRCTB200.GETSEZIONEOPPOSTA(aSezMovimento) = CNRCTB200.IS_AVERE then
    aVoceSopr:=aVoceSoprP;
   else
    aVoceSopr:=aVoceSoprA;
   end if;

   CNRCTB204.buildChiusuraScrittura(aScrittura.cd_cds,aEs,aScrittura.cd_unita_organizzativa,aVoceSopr,trunc(aTSnow),trunc(aTSNow),0,aTiIstituzCommerc,aNewListaMovimenti,aUser,aTSnow);

   aScritturaNew:=null;
   aScritturaNew.CD_CDS:=aScrittura.cd_cds;
   aScritturaNew.ESERCIZIO:=aEs;
   aScritturaNew.CD_UNITA_ORGANIZZATIVA:=aScrittura.cd_unita_organizzativa;
   aScritturaNew.ORIGINE_SCRITTURA:=CNRCTB200.ORIGINE_DOCUMENTO_AMM;
   aScritturaNew.CD_TIPO_DOCUMENTO:=aDocTst.cd_tipo_documento;
   aScritturaNew.CD_CAUSALE_COGE:=CNRCTB200.CAU_ANNULL_ES_CHIUSO;
   -- La chiave del documento viene costruita per concatenazione delle componenti della chiave del buono di scarico
   aScritturaNew.CD_COMP_DOCUMENTO:=null;
   aScritturaNew.CD_CDS_DOCUMENTO:=aDocTst.cd_cds;
   aScritturaNew.CD_UO_DOCUMENTO:=aDocTst.cd_unita_organizzativa;
   aScritturaNew.PG_NUMERO_DOCUMENTO:=aDocTst.pg_numero_documento;
   aScritturaNew.IM_SCRITTURA:=null; -- Impostato come totale di sezione in realizzazione della scrittura
-- 09.08.2006 data contabilizzazione = ultimo giorno dell'anno (scritture di annullamento documenti)
   aScritturaNew.DT_CONTABILIZZAZIONE := To_Date('3112'||to_char(aEs), 'ddmmyyyy');  --trunc(aTSNow);
   aScritturaNew.DT_PAGAMENTO:=null;
   aScritturaNew.DT_CANCELLAZIONE:=null;
   aScritturaNew.TI_SCRITTURA:=CNRCTB200.TI_SCRITTURA_SINGOLA;
   aScritturaNew.CD_TERZO:=aCdTerzo;
   aScritturaNew.STATO:=CNRCTB200.STATO_DEFINITIVO;
   aScritturaNew.CD_DIVISA:=NULL;
   aScritturaNew.COSTO_PLURIENNALE:=null; -- chi lo valorizza e come non ? chiaro
   aScritturaNew.PG_ENTE:=null; -- serve a rinumerare le scritture  livello di ENTE

   aScritturaNew.DS_SCRITTURA := 'Annullamento documento esercizio chiuso';

   aScritturaNew.PG_SCRITTURA_ANNULLATA:=null; -- codice della scrittura annullata in effettuazione storno
   aScritturaNew.ATTIVA:='Y';
   aScritturaNew.ESERCIZIO_DOCUMENTO_AMM:=aDocTst.esercizio;
   aScritturaNew.DACR:=aTSNow;
   aScritturaNew.UTCR:=aUser;
   -- Registro la scrittura
   CNRCTB200.CREASCRITTCOGE(aScritturaNew,aNewListaMovimenti);
  End Loop;


  CNRCTB100.UPDATEDOCAMM_NODUVAUTUV(
   aDocTst.cd_tipo_documento,
   aDocTst.cd_cds,
   aDocTst.esercizio,
   aDocTst.cd_unita_organizzativa,
   aDocTst.pg_numero_documento,
   'stato_coge='''||CNRCTB100.STATO_COEP_CON||'''',
   null
  );
  CNRCTB100.UPDATEDOCAMM_NODUVAUTUV(
   aDocTst.cd_tipo_documento,
   aDocTst.cd_cds,
   aDocTst.esercizio,
   aDocTst.cd_unita_organizzativa,
   aDocTst.pg_numero_documento,
   'stato_coan='''||CNRCTB100.STATO_COEP_CON||'''',
   null
  );
 end;

-- inizio nuovo

Procedure regAnnullaRigheDocEsChiusoCOGE(aEs number, aRigheAnn V_DOC_AMM_COGE_RIGHE_ANNULLATE%rowtype, aUser varchar2, aTSNow date) is
  aScrittura            scrittura_partita_doppia%rowtype;
  aScritturaNew         scrittura_partita_doppia%rowtype;
  aListaMovimenti       CNRCTB200.movimentiList;
  aNewListaMovimenti    CNRCTB200.movimentiList;
  aMovimento            movimento_coge%rowtype;
  aVoceAnag             voce_ep%rowtype;
  aVoceSopr             voce_ep%rowtype;
  aVoceSoprA            voce_ep%rowtype;
  aVoceSoprP            voce_ep%rowtype;
  aListaScritture       CNRCTB200.scrittureList;
  aImMovimento          number(15,2);
  aImPagatoIncassato    number(15,2);
  aSezMovimento         char(1);
  aTiIstituzCommerc     char(1);
  aCdTerzo              number(8);
  aNum                  number;
  conta_annullate       NUMBER;
  aDocTst               v_doc_amm_coge_tsta%Rowtype;

 Begin

 -- partendo direttamente dalle righe ho necessit? di recuperare la testata del documento

  Select *
  Into   aDocTst
  From   v_doc_amm_coge_tsta
  Where  CD_TIPO_DOCUMENTO      = aRigheAnn.CD_TIPO_DOCUMENTO And
         CD_CDS                 = aRigheAnn.CD_CDS And
         CD_UNITA_ORGANIZZATIVA = aRigheAnn.CD_UNITA_ORGANIZZATIVA And
         ESERCIZIO              = aRigheAnn.ESERCIZIO And
         PG_NUMERO_DOCUMENTO    = aRigheAnn.PG_NUMERO_DOCUMENTO;

  -- Lock del documento
  CNRCTB100.LOCKDOCAMM(
   aDocTst.cd_tipo_documento,
   aDocTst.cd_cds,
   aDocTst.esercizio,
   aDocTst.cd_unita_organizzativa,
   aDocTst.pg_numero_documento);

  begin
   select 1 into aNum from v_unita_organizzativa_valida where
             cd_unita_organizzativa=aDocTst.cd_cds_origine
		 and esercizio=aEs
		 and fl_cds='Y';
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('(1) Il cds '||aDocTst.cd_cds_origine||' non ? pi? valido nell''esercizio ('||aEs||') in cui ? stato richiesto di annullare il documento');
  end;

  if not CNRCTB008.ISESERCIZIOAPERTOOCHIUSO(aEs, aDocTst.cd_cds_origine) then
   IBMERR001.RAISE_ERR_GENERICO('L''esercizio ('||aEs||') per il cds '||aDocTst.cd_cds_origine||' in cui ? stato annullato il documento non ? ancora aperto o chiuso');
  end if;

  -- indipendentemente dall'esercizio residuo del documento amministrativo deve essere chiuso in economica
  -- l'esercizio precedente a quello della scrittura (chiusura dei conti) -> GIUSTO
  if not (CNRCTB200.ISCHIUSURACOEPDEF(aEs-1,aDocTst.cd_cds_origine)='Y') then
   IBMERR001.RAISE_ERR_GENERICO('L''esercizio economico precedente a quello della contabilizzazione ('||To_Char(aEs-1)||') non ? chiuso definitivamente (annullamento righe)');
  end if;

  -- deve essere chiuso anche quello di origine del documento (forse inutile, al pi? ? dell'anno prima... csnf)
  if not (CNRCTB200.ISCHIUSURACOEPDEF(aDocTst.esercizio,aDocTst.cd_cds_origine)='Y') then
   IBMERR001.RAISE_ERR_GENERICO('(2) L''esercizio economico di origine del documento amministrativo ('||aDocTst.esercizio||') non ? chiuso definitivamente');
  end if;

  -- E' possibile annullare le righe solo ai Documenti Generici (per adesso)
  If --aDocTst.cd_tipo_documento = CNRCTB100.TI_FATTURA_ATTIVA Or
        CNRCTB100.ISINTABELLAGENERICO(aDocTst.cd_tipo_documento) = 'Y' Then
   Null; -- ok
  Else
   IBMERR001.RAISE_ERR_GENERICO('La tipologia del documento amministrativo ('||aDocTst.cd_tipo_documento||') non ? consentita');
  End If;

  -- Lock del documento
  CNRCTB100.LOCKDOCAMM(
   aDocTst.cd_tipo_documento,
   aDocTst.cd_cds,
   aDocTst.esercizio,
   aDocTst.cd_unita_organizzativa,
   aDocTst.pg_numero_documento
  );

  -- Se il documento NON ? riportato (cio? se non ? a residuo) l'operazione in COGE non ? possibile
  -- (verifica che almeno una riga sia riportata)
  If CNRCTB105.isRiportato(aDocTst.cd_cds,aDocTst.cd_unita_organizzativa, aDocTst.esercizio,
                           aDocTst.pg_numero_documento, aDocTst.cd_tipo_documento) = 'N' then
   IBMERR001.RAISE_ERR_GENERICO('Documento non processabile con tale funzione perch? non riportato ('||
        aDocTst.cd_cds||'/'||aDocTst.cd_unita_organizzativa||'/'||aDocTst.esercizio||'/'||aDocTst.pg_numero_documento||'/'||
        aDocTst.cd_tipo_documento||' e origine '||aDocTst.CD_CDS_ORIGINE||'/'||aDocTst.CD_uo_ORIGINE||'), documento '||
        'NON residuo.');
  End If;

  -- Se mai stato processato in economica (stato N) sollevo eccezione
  If aDocTst.stato_coge in (CNRCTB100.STATO_COEP_INI) Then
   IBMERR001.RAISE_ERR_GENERICO('Testata del documento mai processato in economica '||
        aDocTst.cd_tipo_documento||'/'||aDocTst.esercizio||'/'||aDocTst.cd_cds||'/'||aDocTst.cd_unita_organizzativa||'/'||
        aDocTst.pg_numero_documento);
  End If;

  -- verifico che il documento sia stato riportato all'esercizio di annullamento e non oltre (o non ancora)
  -- controllando che la riga del documento non sia associata ad accertamento di esercizio superiore a quello
  -- dell'annullamento (aRigheAnn.ESERCIZIO_DOC = esercizio di bilancio dell'accertamento)

  If aRigheAnn.ESERCIZIO_DOC > aEs Then
   IBMERR001.RAISE_ERR_GENERICO('Impossibile contabilizzare nel '||aEs||' l''annullamento della riga n. '||aRigheAnn.PG_RIGA||
        ' del documento amministrativo '||aDocTst.cd_tipo_documento||'/'||aDocTst.esercizio||'/'||aDocTst.cd_cds||'/'||
        aDocTst.cd_unita_organizzativa||'/'||aDocTst.pg_numero_documento||
        ' in quando associata a documento contabile di esercizio successivo ('||aRigheAnn.ESERCIZIO_DOC||').');
  End If;

  -- controllo anche che tutte le altre righe valide siano contabilizzate
  Declare
    conta_righe_non_contabilizzate NUMBER;
  Begin
    Select Count(*)
    Into   conta_righe_non_contabilizzate
    From   documento_generico_riga
    Where  CD_CDS                 = aRigheAnn.CD_CDS                  And
           CD_UNITA_ORGANIZZATIVA = aRigheAnn.CD_UNITA_ORGANIZZATIVA  And
           ESERCIZIO              = aRigheAnn.ESERCIZIO               And
           CD_TIPO_DOCUMENTO_AMM  = aRigheAnn.CD_TIPO_DOCUMENTO       And
           PG_DOCUMENTO_GENERICO  = aRigheAnn.PG_NUMERO_DOCUMENTO     And
           PROGRESSIVO_RIGA      != aRigheAnn.PG_RIGA                 And
           STATO_COFI            != CNRCTB100.STATO_GEN_COFI_ANN      And
           STATO_COGE            In (CNRCTB100.STATO_COEP_INI, CNRCTB100.STATO_COEP_DA_RIP );
    If conta_righe_non_contabilizzate > 0 Then
        IBMERR001.RAISE_ERR_GENERICO('Impossibile contabilizzare l''annullamento della riga n. '||aRigheAnn.PG_RIGA||
        ' del documento amministrativo '||aDocTst.cd_tipo_documento||'/'||aDocTst.esercizio||'/'||aDocTst.cd_cds||'/'||
        aDocTst.cd_unita_organizzativa||'/'||aDocTst.pg_numero_documento||
        ' in quando esistono altri dettagli dello stesso documento non contabilizzati.');
    End If;
   End;

  -- fine controllo

-- ??? update dello stato_coge del dettaglio

    CNRCTB100.updateDocAmmRiga_noDuvaUtuv(
        aRigheAnn.CD_TIPO_DOCUMENTO,
        aRigheAnn.CD_CDS,
        aRigheAnn.ESERCIZIO,
        aRigheAnn.CD_UNITA_ORGANIZZATIVA,
        aRigheAnn.PG_NUMERO_DOCUMENTO,
        aRigheAnn.PG_RIGA,
        ' stato_coge = '''||CNRCTB100.STATO_COEP_CON||'''',
        Null);

  aVoceSoprA := CNRCTB002.GETVOCEEPINSUSSPASSIVE(aEs);
  aVoceSoprP := CNRCTB002.GETVOCEEPINSUSSATTIVE(aEs);

  -- Devo girare sulle insussistenze dell'esercizio specificato aEs la quota non pagata del documento
  -- Se si tratta di compenso/missione/anticipo/rimborso
  -- recupero la scrittura di origine e leggo da l? l'importo terzo da girare sulle insussistenze
  -- utilizzando l'associazione anagrafica conto dell'anno aEs
  -- Carica le scritture originali del documento

  CNRCTB204.GETSCRITTUREPEPLOCK(aDocTst, aListaScritture); -- recupera le testate di tutte le scritture del mio documento

  For k in 1 ..  aListaScritture.count Loop -- e poi recupera testata e righe di quella del mio terzo

Dbms_Output.put_line ('terzo a '||aRigheAnn.cd_terzo);

   If aListaScritture(k).cd_terzo = aRigheAnn.cd_terzo Then

     aScrittura := aListaScritture(k);
     aNewListaMovimenti.delete;
     aListaMovimenti.delete;

     CNRCTB200.GETSCRITTURAEPLOCK(aScrittura, aListaMovimenti);

     aImMovimento         := Null;
     aSezMovimento        := Null;
     aTiIstituzCommerc    := Null;

     For i In 1 .. aListaMovimenti.count Loop
       If aListaMovimenti(i).fl_mov_terzo is not null and aListaMovimenti(i).fl_mov_terzo = 'Y' then
         aImMovimento      := aListaMovimenti(i).im_movimento;
         aSezMovimento     := aListaMovimenti(i).sezione;
         aTiIstituzCommerc := aListaMovimenti(i).ti_istituz_commerc;
       End If;
     End Loop;

   If aImMovimento Is Null Then
    IBMERR001.RAISE_ERR_GENERICO('Errore in determinazione dell''importo movimento creditore/debitore della scrittura originale del documento');
   End If;

Dbms_Output.put_line ('terzo b '||aScrittura.cd_terzo);

   aCdTerzo  := aScrittura.cd_terzo;
   aVoceAnag := CNRCTB204.TROVACONTOANAG(aEs,aCdTerzo,aDocTst.cd_tipo_documento,aDocTst.ti_fattura,aRigheAnn.cd_elemento_voce_ev);

Dbms_Output.put_line ('CNRCTB204.buildMovPrinc');

   CNRCTB204.buildMovPrinc(aScrittura.cd_cds,aEs,aScrittura.cd_unita_organizzativa,aVoceAnag,
                           Nvl(aRigheAnn.IM_IMPONIBILE, 0) + Nvl(aRigheAnn.IM_IVA, 0),
                           CNRCTB200.GETSEZIONEOPPOSTA(aSezMovimento),
                           To_Date('3112'||to_char(aEs), 'ddmmyyyy'), To_Date('3112'||to_char(aEs), 'ddmmyyyy'),
                           aCdTerzo, aTiIstituzCommerc, aNewListaMovimenti, aUser, aTSnow);

   if CNRCTB200.GETSEZIONEOPPOSTA(aSezMovimento) = CNRCTB200.IS_AVERE then
    aVoceSopr := aVoceSoprP;
   else
    aVoceSopr := aVoceSoprA;
   end if;

Dbms_Output.put_line ('CNRCTB204.buildChiusuraScrittura');

   CNRCTB204.buildChiusuraScrittura(aScrittura.cd_cds,aEs,aScrittura.cd_unita_organizzativa,aVoceSopr,
                                    To_Date('3112'||to_char(aEs), 'ddmmyyyy'), To_Date('3112'||to_char(aEs), 'ddmmyyyy'),
                                    aCdTerzo,aTiIstituzCommerc,aNewListaMovimenti,aUser,aTSnow);

   aScritturaNew := null;

   aScritturaNew.CD_CDS:=aScrittura.cd_cds;
   aScritturaNew.ESERCIZIO:=aEs;
   aScritturaNew.CD_UNITA_ORGANIZZATIVA:=aScrittura.cd_unita_organizzativa;
   aScritturaNew.ORIGINE_SCRITTURA:=CNRCTB200.ORIGINE_DOCUMENTO_AMM;
   aScritturaNew.CD_TIPO_DOCUMENTO:=aDocTst.cd_tipo_documento;
   aScritturaNew.CD_CAUSALE_COGE:=CNRCTB200.CAU_ANNULL_RIGHE_ES_CHIUSO;
   -- La chiave del documento viene costruita per concatenazione delle componenti della chiave del buono di scarico
   aScritturaNew.CD_COMP_DOCUMENTO:=null;
   aScritturaNew.CD_CDS_DOCUMENTO:=aDocTst.cd_cds;
   aScritturaNew.CD_UO_DOCUMENTO:=aDocTst.cd_unita_organizzativa;
   aScritturaNew.PG_NUMERO_DOCUMENTO:=aDocTst.pg_numero_documento;
   aScritturaNew.IM_SCRITTURA:=null; -- Impostato come totale di sezione in realizzazione della scrittura
-- 09.08.2006 data contabilizzazione = ultimo giorno dell'anno (scritture di annullamento documenti)
   aScritturaNew.DT_CONTABILIZZAZIONE := To_Date('3112'||to_char(aEs), 'ddmmyyyy');  --trunc(aTSNow);
   aScritturaNew.DT_PAGAMENTO:=null;
   aScritturaNew.DT_CANCELLAZIONE:=null;
   aScritturaNew.TI_SCRITTURA:=CNRCTB200.TI_SCRITTURA_SINGOLA;
   aScritturaNew.CD_TERZO:=aCdTerzo;
   aScritturaNew.STATO:=CNRCTB200.STATO_DEFINITIVO;
   aScritturaNew.CD_DIVISA:=NULL;
   aScritturaNew.COSTO_PLURIENNALE:=null; -- chi lo valorizza e come non ? chiaro
   aScritturaNew.PG_ENTE:=null; -- serve a rinumerare le scritture  livello di ENTE

   aScritturaNew.DS_SCRITTURA := 'Annullamento dettaglio documento esercizio chiuso';

   aScritturaNew.PG_SCRITTURA_ANNULLATA:=null; -- codice della scrittura annullata in effettuazione storno
   aScritturaNew.ATTIVA:='Y';
   aScritturaNew.ESERCIZIO_DOCUMENTO_AMM:=aDocTst.esercizio;
   aScritturaNew.DACR:=aTSNow;
   aScritturaNew.UTCR:=aUser;
   -- Registro la scrittura

Dbms_Output.put_line ('CNRCTB200.CREASCRITTCOGE');
   CNRCTB200.CREASCRITTCOGE(aScritturaNew,aNewListaMovimenti);

   End If;

   End Loop;

--buildMovContrPEP (aListaMovimenti, aDocTst, aRigheAnn.cd_terzo, aUser, aTSNow);

--aScrittura := CNRCTB204.buildScrPEP(aDocTst, aRigheAnn.cd_terzo, aUser, aTSNow);

--CNRCTB200.CREASCRITTCOGE(aScrittura, aListaMovimenti);

/* fine */

 End;

-- fine nuovo

 procedure regDismBeneDurevoleCOGE(aTBS buono_carico_scarico_dett%rowtype, aUser varchar2, aTSNow date) is
  aInventBene inventario_beni%rowtype;
  aVoceEPMinusvalenza voce_ep%rowtype;
  aVoceEPPlusvalenza voce_ep%rowtype;
  aVoceEPPatrimNetto voce_ep%rowtype;
  aCatGruppo CATEGORIA_GRUPPO_INVENT%rowtype;
  aCatGruppoVoceEp CATEGORIA_GRUPPO_VOCE_EP%rowtype;
  aVoceEPCG voce_ep%rowtype;
  aVoceEPCGContr voce_ep%rowtype;
  --aVoceEPCred voce_ep%rowtype;
  aScrittura scrittura_partita_doppia%rowtype;
  aListaMovimenti CNRCTB200.movimentiList;
  aResiduo number(15,2);
  aBS buono_carico_scarico_dett%rowtype;
  isTrasferimento CHAR(1);
  aRecAssTrasfBeni ASS_TRASFERIMENTO_BENI_INV%ROWTYPE;
  ABuono_carico_scarico buono_carico_scarico%rowtype;
  quote_storno number(15,2):=0;
  ALTRI_SCARICHI number(15,2):=0;

begin
  recParametriCNR := CNRUTL001.getRecParametriCnr(aTBS.esercizio);

  -- Lettura con lock del buono di scarico dettaglio
  select * into aBS
  from buono_carico_scarico_dett bcs where
          PG_INVENTARIO=aTBS.PG_INVENTARIO
      and TI_DOCUMENTO=aTBS.TI_DOCUMENTO
      and ESERCIZIO=aTBS.ESERCIZIO
      and PG_BUONO_C_S=aTBS.PG_BUONO_C_S
      and NR_INVENTARIO=aTBS.NR_INVENTARIO
      and PROGRESSIVO=aTBS.PROGRESSIVO
  for update nowait;

  select * into ABuono_carico_scarico
  from buono_carico_scarico where
          PG_INVENTARIO=aTBS.PG_INVENTARIO
      and TI_DOCUMENTO=aTBS.TI_DOCUMENTO
      and ESERCIZIO=aTBS.ESERCIZIO
      and PG_BUONO_C_S=aTBS.PG_BUONO_C_S
  for update nowait;

  -- Controllo se lo scarico ? per trasferimento
  isTrasferimento:=chkBuonoPerTrasferimento(aBS);

  -- Selezione del bene
  begin
   select * into aInventBene from inventario_beni where
             pg_inventario = aBS.pg_inventario
		 and nr_inventario = aBS.nr_inventario
		 and progressivo = aBS.progressivo
    for update nowait;
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('Bene a inventario non trovato pg. inv.:'||aBS.pg_inventario||' nr.'||aBS.nr_inventario||' pg. acc.'||aBS.progressivo);
  end;

  -- Fix del 20040924 Richiesta 843
  CNRCTB204.checkChiusuraEsercizio(aBS.esercizio, aInventBene.cd_cds);

  if not (CNRCTB200.ISCHIUSURACOEPDEF(aBS.esercizio-1, aInventBene.cd_cds)='Y') then
   IBMERR001.RAISE_ERR_GENERICO('L''esercizio economico precedente non ? chiuso definitivamente per il cds: '||aInventBene.cd_cds);
  end if;

  begin
    select * into aCatGruppo from CATEGORIA_GRUPPO_INVENT where
           cd_categoria_gruppo = aInventBene.cd_categoria_gruppo;
    exception when NO_DATA_FOUND then
     IBMERR001.RAISE_ERR_GENERICO('Gruppo inventariale non trovato:'||aInventBene.cd_categoria_gruppo);
    end;
    BEGIN
			if recParametriCNR.fl_nuovo_pdg='N' then
	      SELECT * INTO aCatGruppoVoceEp FROM CATEGORIA_GRUPPO_VOCE_EP WHERE
              cd_categoria_gruppo = aCatGruppo.cd_categoria_padre
  				AND sezione = CNRCTB200.IS_AVERE
  				AND ESERCIZIO=aBS.esercizio;
  		else
       	      SELECT * INTO aCatGruppoVoceEp FROM CATEGORIA_GRUPPO_VOCE_EP WHERE
              cd_categoria_gruppo = aCatGruppo.cd_categoria_gruppo
  				AND sezione = CNRCTB200.IS_AVERE
  				AND ESERCIZIO=aBS.esercizio
  				and fl_default = 'Y';
  		end if;
		EXCEPTION WHEN NO_DATA_FOUND THEN
				IBMERR001.RAISE_ERR_GENERICO('Nessun conto economico associato a categoria:'||aCatGruppo.cd_categoria_gruppo);
		END;
    SELECT COUNT(*) INTO ALTRI_SCARICHI
   	FROM  BUONO_CARICO_SCARICO_DETT
   	WHERE
     pg_inventario = aInventBene.pg_inventario and
     nr_inventario = aInventBene.nr_inventario and
     progressivo   = aInventBene.progressivo    AND
     TI_DOCUMENTO = 'S' 			AND
     ((PG_BUONO_C_S > ABS.PG_BUONO_C_S 	and
       esercizio = aBS.esercizio)	OR
      ESERCIZIO > aBS.esercizio);
     Begin
	    select abs(im_movimento_ammort) into quote_storno
	    from ammortamento_bene_inv
	    where
	        PG_INVENTARIO	 = aBS.PG_INVENTARIO
		and PG_BUONO_S   = aBS.PG_BUONO_C_S
		and NR_INVENTARIO= aBS.NR_INVENTARIO
		and PROGRESSIVO  = aBS.PROGRESSIVO
		and esercizio    = aBS.esercizio
		and fl_storno ='Y';
     Exception when no_data_found then
	      quote_storno:=0;
      end;
     -- Selezione associativa beni per trasferimento
  	if isTrasferimento = 'Y' then
   	begin
	    select * into aRecAssTrasfBeni
	    from   ass_trasferimento_beni_inv
	    where  pg_inventario_origine = aInventBene.pg_inventario and
	           nr_inventario_origine = aInventBene.nr_inventario and
	           progressivo_origine = aInventBene.progressivo;
	   exception when NO_DATA_FOUND then
	    IBMERR001.RAISE_ERR_GENERICO
	       ('Non trovata associativa per scarico da trasferimento. Bene: '|| aInventBene.pg_inventario || '-' || aInventBene.nr_inventario || '-' ||aInventBene.progressivo);
	   end;
	  end if;

  -- Costruzione scrittura
  -- crediti vs/clienti
  --aVoceEPCred:=CNRCTB002.getVoceEp(aCatGruppoVoceEp.esercizio,'A.06.005');
  aVoceEPCG:=CNRCTB002.getVoceEp(aCatGruppoVoceEp.esercizio,aCatGruppoVoceEp.cd_voce_ep);
  if (aCatGruppoVoceEp.cd_voce_ep_contr is not null) then
  	aVoceEPCGContr:=CNRCTB002.getVoceEp(aCatGruppoVoceEp.esercizio,aCatGruppoVoceEp.cd_voce_ep_contr);
  end if;
  aVoceEPMinusvalenza:=CNRCTB002.getVoceEpMinusval(aCatGruppoVoceEp.ESERCIZIO,aCatGruppo.cd_categoria_gruppo,null);
	aVoceEPPlusvalenza:=CNRCTB002.getVoceEpPlusval(aCatGruppoVoceEp.ESERCIZIO,aCatGruppo.cd_categoria_gruppo,null);

  if (isTrasferimento = 'Y') Then
	   aVoceEPPatrimNetto:=CNRCTB002.getVoceEpPatrimNetto(aBS.esercizio);
	   aResiduo:=aRecAssTrasfBeni.valore_iniziale + aRecAssTrasfBeni.variazione_piu - aRecAssTrasfBeni.variazione_meno - aRecAssTrasfBeni.valore_ammortizzato;
	   if aResiduo > 0 then
		-- 30/12/2004 METTEVA LA DATA DI SISTEMA COME DA COMPETENZA/A COMPETENZA SULLE REGISTRAZIONI DI ECONOMICA
		CNRCTB204.buildMovPrinc(aInventBene.cd_cds,aBS.esercizio,aInventBene.cd_unita_organizzativa,aVoceEPPatrimNetto,aResiduo,CNRCTB200.IS_DARE,Trunc(ABuono_carico_scarico.DATA_REGISTRAZIONE),Trunc(ABuono_carico_scarico.DATA_REGISTRAZIONE),0,
		                        aInventBene.TI_COMMERCIALE_ISTITUZIONALE,
		                        aListaMovimenti,aUser,aTSnow);
	   Elsif aResiduo < 0 Then
		   -- 18/01/2006 rp aggiunta gestione storno voce patrimoniale
			Cnrctb204.buildMovPrinc(aInventBene.cd_cds,ABS.ESERCIZIO,aInventBene.cd_unita_organizzativa,aVoceEPPatrimNetto,aResiduo*(-1),Cnrctb200.IS_AVERE,TRUNC(ABuono_carico_scarico.DATA_REGISTRAZIONE),TRUNC(ABuono_carico_scarico.DATA_REGISTRAZIONE),0,
		                        aInventBene.TI_COMMERCIALE_ISTITUZIONALE,
		                        aListaMovimenti,aUser,aTSnow);
	   end if;
	   if aRecAssTrasfBeni.valore_ammortizzato > 0 then
		-- 30/12/2004 METTEVA LA DATA DI SISTEMA COME DA COMPETENZA/A COMPETENZA SULLE REGISTRAZIONI DI ECONOMICA
		CNRCTB204.buildMovPrinc(aInventBene.cd_cds,aBS.esercizio,aInventBene.cd_unita_organizzativa,aVoceEPCGContr,aRecAssTrasfBeni.valore_ammortizzato,CNRCTB200.IS_DARE,Trunc(ABuono_carico_scarico.DATA_REGISTRAZIONE),Trunc(ABuono_carico_scarico.DATA_REGISTRAZIONE),0,
                        aInventBene.TI_COMMERCIALE_ISTITUZIONALE,
                        aListaMovimenti,aUser,aTSnow);
	   end if;
	-- 30/12/2004 METTEVA LA DATA DI SISTEMA COME DA COMPETENZA/A COMPETENZA SULLE REGISTRAZIONI DI ECONOMICA
	   CNRCTB204.buildMovPrinc(aInventBene.cd_cds,aBS.esercizio,aInventBene.cd_unita_organizzativa,aVoceEPCG,aRecAssTrasfBeni.valore_iniziale + aRecAssTrasfBeni.variazione_piu - aRecAssTrasfBeni.variazione_meno,CNRCTB200.IS_AVERE,Trunc(ABuono_carico_scarico.DATA_REGISTRAZIONE), Trunc(ABuono_carico_scarico.DATA_REGISTRAZIONE),0,
	                        aInventBene.TI_COMMERCIALE_ISTITUZIONALE,
	                        aListaMovimenti,aUser,aTSnow);
  Elsif (aBS.stato_coge_quote='N') Then
      if (aInventBene.fl_totalmente_scaricato='Y' AND ALTRI_SCARICHI = 0 ) Then
	 --if (Nvl(aInventBene.valore_alienazione,0)=0) then
     		CNRCTB204.buildMovPrinc(aInventBene.cd_cds,aBS.esercizio,aInventBene.cd_unita_organizzativa,aVoceEPCGContr,nvl(aInventBene.valore_ammortizzato,0),CNRCTB200.IS_DARE,Trunc(ABuono_carico_scarico.DATA_REGISTRAZIONE),Trunc(ABuono_carico_scarico.DATA_REGISTRAZIONE),0,
		                        aInventBene.TI_COMMERCIALE_ISTITUZIONALE,
		                        aListaMovimenti,aUser,aTSnow);

		CNRCTB204.buildMovPrinc(aInventBene.cd_cds,aBS.esercizio,aInventBene.cd_unita_organizzativa,aVoceEPPlusvalenza,nvl(aInventBene.valore_ammortizzato,0),CNRCTB200.IS_AVERE,Trunc(ABuono_carico_scarico.DATA_REGISTRAZIONE),Trunc(ABuono_carico_scarico.DATA_REGISTRAZIONE),0,
		                        aInventBene.TI_COMMERCIALE_ISTITUZIONALE,
		                        aListaMovimenti,aUser,aTSnow);
	 /*Else    caso gestito dalle contabilizzazioni delle fatture attive e documenti generici attivi
	  	aResiduo:=aInventBene.valore_iniziale + aInventBene.variazione_piu - (aInventBene.variazione_meno-(aBS.quantita*aBS.valore_unitario)) - nvl(aInventBene.valore_ammortizzato,0)-Nvl(aInventBene.valore_alienazione,0);
	   	if aResiduo > 0 Then
		-- 30/12/2004 METTEVA LA DATA DI SISTEMA COME DA COMPETENZA/A COMPETENZA SULLE REGISTRAZIONI DI ECONOMICA
			CNRCTB204.buildMovPrinc(aInventBene.cd_cds,aBS.esercizio,aInventBene.cd_unita_organizzativa,aVoceEPMinusvalenza,aResiduo,CNRCTB200.IS_DARE,Trunc(ABuono_carico_scarico.DATA_REGISTRAZIONE),Trunc(ABuono_carico_scarico.DATA_REGISTRAZIONE),0,
		                        aInventBene.TI_COMMERCIALE_ISTITUZIONALE,
		                        aListaMovimenti,aUser,aTSnow);
			CNRCTB204.buildMovPrinc(aInventBene.cd_cds,aBS.esercizio,aInventBene.cd_unita_organizzativa,aVoceEPCG,aResiduo+Nvl(aInventBene.valore_ammortizzato,0),CNRCTB200.IS_AVERE,Trunc(ABuono_carico_scarico.DATA_REGISTRAZIONE),Trunc(ABuono_carico_scarico.DATA_REGISTRAZIONE),0,
	                	        aInventBene.TI_COMMERCIALE_ISTITUZIONALE,
	                       		aListaMovimenti,aUser,aTSnow);

	   	Elsif aResiduo <0 Then
		-- 18/01/2006 rp aggiunto recupero ed uso voce di plusvalenza
			CNRCTB204.buildMovPrinc(aInventBene.cd_cds,aBS.esercizio,aInventBene.cd_unita_organizzativa,aVoceEPPlusvalenza,aResiduo*(-1),CNRCTB200.IS_AVERE,Trunc(ABuono_carico_scarico.DATA_REGISTRAZIONE),Trunc(ABuono_carico_scarico.DATA_REGISTRAZIONE),0,
		                        aInventBene.TI_COMMERCIALE_ISTITUZIONALE,
		                        aListaMovimenti,aUser,aTSnow);

	       	   If (aResiduo*(-1) > Nvl(aInventBene.valore_ammortizzato,0)) Then
	        	 CNRCTB204.buildMovPrinc(aInventBene.cd_cds,aBS.esercizio,aInventBene.cd_unita_organizzativa,aVoceEPCG,(aResiduo+Nvl(aInventBene.valore_ammortizzato,0))*(-1),CNRCTB200.IS_DARE,Trunc(ABuono_carico_scarico.DATA_REGISTRAZIONE),Trunc(ABuono_carico_scarico.DATA_REGISTRAZIONE),0,
	                       aInventBene.TI_COMMERCIALE_ISTITUZIONALE,
	                       aListaMovimenti,aUser,aTSnow);
		   else
		 	CNRCTB204.buildMovPrinc(aInventBene.cd_cds,aBS.esercizio,aInventBene.cd_unita_organizzativa,aVoceEPCG,(-Nvl(aInventBene.valore_ammortizzato,0)- aResiduo)*(-1),CNRCTB200.IS_AVERE,Trunc(ABuono_carico_scarico.DATA_REGISTRAZIONE),Trunc(ABuono_carico_scarico.DATA_REGISTRAZIONE),0,
	                       aInventBene.TI_COMMERCIALE_ISTITUZIONALE,
	                       aListaMovimenti,aUser,aTSnow);
		  end If;
	        end if;
	  	If (Nvl(aInventBene.valore_ammortizzato,0)!=0) Then
			 CNRCTB204.buildMovPrinc(aInventBene.cd_cds,aBS.esercizio,aInventBene.cd_unita_organizzativa,aVoceEPCGContr,Nvl(aInventBene.valore_ammortizzato,0),CNRCTB200.IS_DARE,Trunc(ABuono_carico_scarico.DATA_REGISTRAZIONE),Trunc(ABuono_carico_scarico.DATA_REGISTRAZIONE),0,
	                       aInventBene.TI_COMMERCIALE_ISTITUZIONALE,
	                       aListaMovimenti,aUser,aTSnow);
	  	End If;
	 end If;*/
      else
		CNRCTB204.buildMovPrinc(aInventBene.cd_cds,aBS.esercizio,aInventBene.cd_unita_organizzativa,aVoceEPCGContr,quote_storno,CNRCTB200.IS_DARE,Trunc(ABuono_carico_scarico.DATA_REGISTRAZIONE),Trunc(ABuono_carico_scarico.DATA_REGISTRAZIONE),0,
		                        aInventBene.TI_COMMERCIALE_ISTITUZIONALE,
		                        aListaMovimenti,aUser,aTSnow);

		CNRCTB204.buildMovPrinc(aInventBene.cd_cds,aBS.esercizio,aInventBene.cd_unita_organizzativa,aVoceEPPlusvalenza,quote_storno,CNRCTB200.IS_AVERE,Trunc(ABuono_carico_scarico.DATA_REGISTRAZIONE),Trunc(ABuono_carico_scarico.DATA_REGISTRAZIONE),0,
		                        aInventBene.TI_COMMERCIALE_ISTITUZIONALE,
		                        aListaMovimenti,aUser,aTSnow);
      end If;
	 update buono_carico_scarico_dett set
   		stato_coge_quote = CNRCTB100.STATO_COEP_CON,
  		pg_ver_rec 	 = pg_ver_rec+1
  	where
       		    pg_inventario = aBS.pg_inventario
   		and ti_documento  = aBS.ti_documento
   		and esercizio     = aBS.esercizio
   		and pg_buono_c_s  = aBS.pg_buono_c_s
   		and nr_inventario = aBS.nr_inventario
   		and progressivo   = aBS.progressivo;
  ElsIf(aBS.stato_coge In (CNRCTB100.STATO_COEP_INI,CNRCTB100.STATO_COEP_DA_RIP)) Then
    if (aInventBene.fl_totalmente_scaricato='Y' AND ALTRI_SCARICHI=0) Then
          --dbms_output.put_line('scaricato comp');
          -- 05/12/2006 non considero il valore di alienazione che viene valorizzato ad oggi solo per le vendite da fattura attiva e da doc.gen. attivo
	  -- aResiduo:=aInventBene.valore_iniziale + aInventBene.variazione_piu - (aInventBene.variazione_meno-(aBS.quantita*aBS.valore_unitario)) - nvl(aInventBene.valore_ammortizzato,0)-Nvl(aInventBene.valore_alienazione,0);
	   aResiduo:=aInventBene.valore_iniziale + aInventBene.variazione_piu - (aInventBene.variazione_meno-(aBS.quantita*aBS.valore_unitario)) - nvl(aInventBene.valore_ammortizzato,0);
	   if aResiduo > 0 Then
		-- 30/12/2004 METTEVA LA DATA DI SISTEMA COME DA COMPETENZA/A COMPETENZA SULLE REGISTRAZIONI DI ECONOMICA
		CNRCTB204.buildMovPrinc(aInventBene.cd_cds,aBS.esercizio,aInventBene.cd_unita_organizzativa,aVoceEPMinusvalenza,aResiduo,CNRCTB200.IS_DARE,Trunc(ABuono_carico_scarico.DATA_REGISTRAZIONE),Trunc(ABuono_carico_scarico.DATA_REGISTRAZIONE),0,
		                        aInventBene.TI_COMMERCIALE_ISTITUZIONALE,
		                        aListaMovimenti,aUser,aTSnow);
	   Elsif aResiduo <0 Then
		-- 18/01/2006 rp aggiunto recupero ed uso voce di plusvalenza
		CNRCTB204.buildMovPrinc(aInventBene.cd_cds,aBS.esercizio,aInventBene.cd_unita_organizzativa,aVoceEPPlusvalenza,aResiduo*(-1),CNRCTB200.IS_AVERE,Trunc(ABuono_carico_scarico.DATA_REGISTRAZIONE),Trunc(ABuono_carico_scarico.DATA_REGISTRAZIONE),0,
		                        aInventBene.TI_COMMERCIALE_ISTITUZIONALE,
		                        aListaMovimenti,aUser,aTSnow);
	   end if;
	   -- 30/12/2004 METTEVA LA DATA DI SISTEMA COME DA COMPETENZA/A COMPETENZA SULLE REGISTRAZIONI DI ECONOMICA
	   CNRCTB204.buildMovPrinc(aInventBene.cd_cds,aBS.esercizio,aInventBene.cd_unita_organizzativa,aVoceEPCG,aInventBene.valore_iniziale + aInventBene.variazione_piu - (aInventBene.variazione_meno-(aBS.quantita*aBS.valore_unitario)),CNRCTB200.IS_AVERE,Trunc(ABuono_carico_scarico.DATA_REGISTRAZIONE),Trunc(ABuono_carico_scarico.DATA_REGISTRAZIONE),0,
	                       aInventBene.TI_COMMERCIALE_ISTITUZIONALE,
	                       aListaMovimenti,aUser,aTSnow);
	 /*
	 -- crediti vs/Clienti
	   if nvl(aInventBene.valore_alienazione,0) > 0 then
	  	CNRCTB204.buildMovPrinc(aInventBene.cd_cds,aBS.esercizio,aInventBene.cd_unita_organizzativa,aVoceEPCred,aInventBene.valore_alienazione,CNRCTB200.IS_DARE,Trunc(ABuono_carico_scarico.DATA_REGISTRAZIONE),Trunc(ABuono_carico_scarico.DATA_REGISTRAZIONE),0,
	                       aInventBene.TI_COMMERCIALE_ISTITUZIONALE,
	                       aListaMovimenti,aUser,aTSnow);
	   End If;
	   */
	   if nvl(aInventBene.valore_ammortizzato,0) > 0 then
		-- 30/12/2004 METTEVA LA DATA DI SISTEMA COME DA COMPETENZA/A COMPETENZA SULLE REGISTRAZIONI DI ECONOMICA
		CNRCTB204.buildMovPrinc(aInventBene.cd_cds,aBS.esercizio,aInventBene.cd_unita_organizzativa,aVoceEPCGContr,nvl(aInventBene.valore_ammortizzato,0),CNRCTB200.IS_DARE,Trunc(ABuono_carico_scarico.DATA_REGISTRAZIONE),Trunc(ABuono_carico_scarico.DATA_REGISTRAZIONE),0,
	                       aInventBene.TI_COMMERCIALE_ISTITUZIONALE,
	                       aListaMovimenti,aUser,aTSnow);
   	   End If;
     else -- scarico parziale
            --dbms_output.put_line('scaricato parz');
     	aVoceEPMinusvalenza:=CNRCTB002.getVoceEpMinusval(aBS.esercizio,aCatGruppo.cd_categoria_gruppo,null);
			aVoceEPPlusvalenza:=CNRCTB002.getVoceEpPlusval(aBS.esercizio,aCatGruppo.cd_categoria_gruppo,null);

	   aResiduo:=(aBS.quantita*aBS.valore_unitario)- quote_storno;
	   --dbms_output.put_line('residuo '||aresiduo);
	   if aResiduo > 0 Then
		-- 30/12/2004 METTEVA LA DATA DI SISTEMA COME DA COMPETENZA/A COMPETENZA SULLE REGISTRAZIONI DI ECONOMICA
		CNRCTB204.buildMovPrinc(aInventBene.cd_cds,aBS.esercizio,aInventBene.cd_unita_organizzativa,aVoceEPMinusvalenza,aResiduo,CNRCTB200.IS_DARE,Trunc(ABuono_carico_scarico.DATA_REGISTRAZIONE),Trunc(ABuono_carico_scarico.DATA_REGISTRAZIONE),0,
		                        aInventBene.TI_COMMERCIALE_ISTITUZIONALE,
		                        aListaMovimenti,aUser,aTSnow);
	   Elsif aResiduo <0 Then
		-- 18/01/2006 rp aggiunto recupero ed uso voce di plusvalenza
		CNRCTB204.buildMovPrinc(aInventBene.cd_cds,aBS.esercizio,aInventBene.cd_unita_organizzativa,aVoceEPPlusvalenza,aResiduo*(-1),CNRCTB200.IS_AVERE,Trunc(ABuono_carico_scarico.DATA_REGISTRAZIONE),Trunc(ABuono_carico_scarico.DATA_REGISTRAZIONE),0,
		                        aInventBene.TI_COMMERCIALE_ISTITUZIONALE,
		                        aListaMovimenti,aUser,aTSnow);
	   end if;
	   	--dbms_output.put_line('valore_uni '||abs.valore_unitario);
		-- 30/12/2004 METTEVA LA DATA DI SISTEMA COME DA COMPETENZA/A COMPETENZA SULLE REGISTRAZIONI DI ECONOMICA
		CNRCTB204.buildMovPrinc(aInventBene.cd_cds,aBS.esercizio,aInventBene.cd_unita_organizzativa,aVoceEPCG,(aBS.quantita*aBS.valore_unitario),CNRCTB200.IS_AVERE,Trunc(ABuono_carico_scarico.DATA_REGISTRAZIONE),Trunc(ABuono_carico_scarico.DATA_REGISTRAZIONE),0,
	                        aInventBene.TI_COMMERCIALE_ISTITUZIONALE,
	                        aListaMovimenti,aUser,aTSnow);
            	--dbms_output.put_line('quote_storno '||quote_storno);
	   if quote_storno > 0 then
		-- 30/12/2004 METTEVA LA DATA DI SISTEMA COME DA COMPETENZA/A COMPETENZA SULLE REGISTRAZIONI DI ECONOMICA
		CNRCTB204.buildMovPrinc(aInventBene.cd_cds,aBS.esercizio,aInventBene.cd_unita_organizzativa,aVoceEPCGContr,quote_storno,CNRCTB200.IS_DARE,Trunc(ABuono_carico_scarico.DATA_REGISTRAZIONE),Trunc(ABuono_carico_scarico.DATA_REGISTRAZIONE),0,
	                        aInventBene.TI_COMMERCIALE_ISTITUZIONALE,
	                        aListaMovimenti,aUser,aTSnow);
	   end if;
  end if;
 End if;

  aScrittura:=null;
  aScrittura.CD_CDS:=aInventBene.cd_cds;
  aScrittura.ESERCIZIO:=aBS.esercizio;
  aScrittura.CD_UNITA_ORGANIZZATIVA:=aInventBene.cd_unita_organizzativa;
  aScrittura.ORIGINE_SCRITTURA:=CNRCTB200.ORIGINE_DOCUMENTO_AMM;
  aScrittura.CD_TIPO_DOCUMENTO:=TI_DOC_FITT_SCR_BENE;
  if isTrasferimento = 'Y' then
   aScrittura.CD_CAUSALE_COGE:=CNRCTB200.CAU_SCA_BENE_DUREVOLE_TRASF;
  else
   aScrittura.CD_CAUSALE_COGE:=CNRCTB200.CAU_DISMISSIONE_BENE_DURVOLE;
  end if;
  -- La chiave del documento viene costruita per concatenazione delle componenti della chiave del buono di scarico
  aScrittura.CD_COMP_DOCUMENTO:=aBS.pg_inventario||'.'||aBS.ti_documento||'.'||aBS.esercizio||'.'||aBS.pg_buono_c_s||'.'||aBS.nr_inventario||'.'||aBS.progressivo;
  aScrittura.CD_CDS_DOCUMENTO:=aInventBene.cd_cds;
  aScrittura.CD_UO_DOCUMENTO:=aInventBene.cd_unita_organizzativa;
  aScrittura.PG_NUMERO_DOCUMENTO:=NULL;
  aScrittura.IM_SCRITTURA:=null; -- Impostato come totale di sezione in realizzazione della scrittura
  aScrittura.DT_CONTABILIZZAZIONE := Trunc(ABuono_carico_scarico.DATA_REGISTRAZIONE); --TRUNC(aTSNow); -- stani
  aScrittura.DT_PAGAMENTO:=null;
  aScrittura.DT_CANCELLAZIONE:=null;
  aScrittura.TI_SCRITTURA:=CNRCTB200.TI_SCRITTURA_SINGOLA;
  aScrittura.CD_TERZO:=0;
  aScrittura.STATO:=CNRCTB200.STATO_DEFINITIVO;
  aScrittura.CD_DIVISA:=NULL;
  aScrittura.COSTO_PLURIENNALE:=null; -- chi lo valorizza e come non ? chiaro
  aScrittura.PG_ENTE:=null; -- serve a rinumerare le scritture  livello di ENTE

  aScrittura.DS_SCRITTURA := 'Dismissione bene durevole '||
  aBS.pg_inventario||'/'||aBS.ti_documento||'/'||aBS.esercizio||'/'||aBS.pg_buono_c_s||'/'||aBS.nr_inventario||'/'||aBS.progressivo;

  aScrittura.PG_SCRITTURA_ANNULLATA:=null; -- codice della scrittura annullata in effettuazione storno
  aScrittura.ATTIVA:='Y';
  aScrittura.ESERCIZIO_DOCUMENTO_AMM:=aBS.esercizio;
  aScrittura.DACR:=aTSNow;
  aScrittura.UTCR:=aUser;
  -- Registro la scrittura
  CNRCTB200.CREASCRITTCOGE(aScrittura,aListaMovimenti);
  -- Aggiorno lo stato coge del buono di scarico
  update buono_carico_scarico_dett set
   stato_coge = CNRCTB100.STATO_COEP_CON,
   pg_ver_rec = pg_ver_rec+1
  where
       pg_inventario = aBS.pg_inventario
   and ti_documento = aBS.ti_documento
   and esercizio = aBS.esercizio
   and pg_buono_c_s=aBS.pg_buono_c_s
   and nr_inventario = aBS.nr_inventario
   and progressivo = aBS.progressivo
   And STATO_COGE In(CNRCTB100.STATO_COEP_INI,CNRCTB100.STATO_COEP_DA_RIP);
 end;

 procedure regCaricBeneDurevoleCOGE(aTBS buono_carico_scarico_dett%rowtype, aUser varchar2, aTSNow date) is
  aInventBene inventario_beni%rowtype;
  aCatGruppo CATEGORIA_GRUPPO_INVENT%rowtype;
  aCatGruppoVoceEp CATEGORIA_GRUPPO_VOCE_EP%rowtype;
  aVoceEPCG voce_ep%rowtype;
  aVoceEPSoprAtt voce_ep%rowtype;
  aVoceEPPatrimNetto voce_ep%rowtype;
  aVoceEPCGContr voce_ep%rowtype;
  aScrittura scrittura_partita_doppia%rowtype;
  aListaMovimenti CNRCTB200.movimentiList;
  aScritturaTmp scrittura_partita_doppia%rowtype;
  aListaM CNRCTB200.movimentiList;
  aImporto number(15,2);
  aBS buono_carico_scarico_dett%rowtype;
  aListaVecchieScritture CNRCTB200.scrittureList;
  aListaNuoveScritture CNRCTB200.scrittureList;
  isTrasferimento CHAR(1);
  aRecAssTrasfBeni ASS_TRASFERIMENTO_BENI_INV%ROWTYPE;
  aCausaleCoge VARCHAR2(50);
  ABuono_carico_scarico buono_carico_scarico%rowtype;

begin
  recParametriCNR := CNRUTL001.getRecParametriCnr(aTBS.ESERCIZIO);


  -- Lettura con lock del buono di carico dettaglio
  select * into aBS from buono_carico_scarico_dett bcs where
          PG_INVENTARIO=aTBS.PG_INVENTARIO
      and TI_DOCUMENTO=aTBS.TI_DOCUMENTO
      and ESERCIZIO=aTBS.ESERCIZIO
      and PG_BUONO_C_S=aTBS.PG_BUONO_C_S
      and NR_INVENTARIO=aTBS.NR_INVENTARIO
      and PROGRESSIVO=aTBS.PROGRESSIVO
  for update nowait;

  select * into ABuono_carico_scarico
  from buono_carico_scarico where
          PG_INVENTARIO=aTBS.PG_INVENTARIO
      and TI_DOCUMENTO=aTBS.TI_DOCUMENTO
      and ESERCIZIO=aTBS.ESERCIZIO
      and PG_BUONO_C_S=aTBS.PG_BUONO_C_S
  for update nowait;

  -- Controllo se lo scarico ? per trasferimento
  isTrasferimento:=chkBuonoPerTrasferimento(aBS);

  -- Selezione del bene
  begin
   select * into aInventBene from inventario_beni where
             pg_inventario = aBS.pg_inventario
		 and nr_inventario = aBS.nr_inventario
		 and progressivo = aBS.progressivo
    for update nowait;
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('Bene a inventario non trovato pg. inv.:'||aBS.pg_inventario||' nr.'||aBS.nr_inventario||' pg. acc.'||aBS.progressivo);
  end;

  -- Fix del 20040924 Richiesta 843
  CNRCTB204.checkChiusuraEsercizio(aBS.esercizio, aInventBene.cd_cds);

  if not (CNRCTB200.ISCHIUSURACOEPDEF(aBS.esercizio-1, aInventBene.cd_cds)='Y') then
   IBMERR001.RAISE_ERR_GENERICO('L''esercizio economico precedente non ? chiuso definitivamente per il cds: '||aInventBene.cd_cds);
  end if;

  -- Memorizzo la causale coge

  if isTrasferimento = 'Y' then
   aCausaleCoge:=CNRCTB200.CAU_CAR_BENE_DUREVOLE_TRASF;
  else
   aCausaleCoge:=CNRCTB200.CAU_CARICO_BENE_DURVOLE;
  end if;

  -- UNDO eventuale della scrittura esistente

  savepoint CNRCTB205_SP_002;

  -- cerca eventuali scritture gia registrate per il documento

  begin

   select * into aScritturaTmp from scrittura_partita_doppia where
        CD_COMP_DOCUMENTO=aBS.pg_inventario||'.'||aBS.ti_documento||'.'||aBS.esercizio||'.'||aBS.pg_buono_c_s||'.'||aBS.nr_inventario||'.'||aBS.progressivo
    and CD_CDS=aInventBene.cd_cds
    and ESERCIZIO=aBS.esercizio
    and CD_UNITA_ORGANIZZATIVA=aInventBene.cd_unita_organizzativa
    and ORIGINE_SCRITTURA=CNRCTB200.ORIGINE_DOCUMENTO_AMM
    and CD_TIPO_DOCUMENTO=TI_DOC_FITT_CAR_BENE
    and CD_CAUSALE_COGE=aCausaleCoge
   for update nowait;
   CNRCTB200.getScritturaEPLOCK(aScritturaTmp,aListaM);
   aListaVecchieScritture(1):=aScritturaTmp;
   CNRCTB200.creaScrittStornoCoge(aScritturaTmp,aListaM, aUser, aTSNow);
  exception when NO_DATA_FOUND then
   null;
  end;

  begin
    select * into aCatGruppo from CATEGORIA_GRUPPO_INVENT where
           cd_categoria_gruppo = aInventBene.cd_categoria_gruppo;
    exception when NO_DATA_FOUND then
     IBMERR001.RAISE_ERR_GENERICO('Gruppo inventariale non trovato:'||aInventBene.cd_categoria_gruppo);
    end;
     BEGIN
			if recParametriCNR.fl_nuovo_pdg='N' then
	      SELECT * INTO aCatGruppoVoceEp FROM CATEGORIA_GRUPPO_VOCE_EP WHERE
              cd_categoria_gruppo = aCatGruppo.cd_categoria_padre
  				AND sezione = CNRCTB200.IS_AVERE
  				AND ESERCIZIO=aBS.esercizio;
  		else
       	      SELECT * INTO aCatGruppoVoceEp FROM CATEGORIA_GRUPPO_VOCE_EP WHERE
              cd_categoria_gruppo = aCatGruppo.cd_categoria_gruppo
  				AND sezione = CNRCTB200.IS_AVERE
  				AND ESERCIZIO=aBS.esercizio
  				and fl_default = 'Y';
  		end if;
		EXCEPTION WHEN NO_DATA_FOUND THEN
				IBMERR001.RAISE_ERR_GENERICO('Nessun conto economico associato a categoria:'||aCatGruppo.cd_categoria_gruppo);
		END;


  -- Selezione associativa beni per trasferimento
  if isTrasferimento = 'Y' then
   begin
    select * into aRecAssTrasfBeni
    from   ass_trasferimento_beni_inv
    where  pg_inventario_dest = aInventBene.pg_inventario and
           nr_inventario_dest = aInventBene.nr_inventario and
           progressivo_dest = aInventBene.progressivo;
   exception when NO_DATA_FOUND then
    IBMERR001.RAISE_ERR_GENERICO
       ('Non trovata associativa per carico da trasferimento. Bene: '|| aInventBene.pg_inventario || '-' || aInventBene.nr_inventario || '-' ||aInventBene.progressivo);
   end;
  end if;
   -- Costruzione scrittura
  aVoceEPCG:=CNRCTB002.getVoceEp(aCatGruppoVoceEp.esercizio,aCatGruppoVoceEp.cd_voce_ep);
  if isTrasferimento = 'Y' then
  Dbms_Output.put_line('valore '||(aRecAssTrasfBeni.valore_iniziale + aRecAssTrasfBeni.variazione_piu - aRecAssTrasfBeni.variazione_meno)|| '-' ||aCatGruppoVoceEp.cd_categoria_gruppo);
   if(aCatGruppoVoceEp.cd_voce_ep_contr is not null or aRecAssTrasfBeni.valore_ammortizzato != 0 ) then
    aVoceEPCGContr:=CNRCTB002.getVoceEp(aCatGruppoVoceEp.esercizio,aCatGruppoVoceEp.cd_voce_ep_contr);
   end if;
   aVoceEPPatrimNetto:=CNRCTB002.getVoceEpPatrimNetto(aBS.esercizio);
-- 30/12/2004 METTEVA LA DATA DI SISTEMA COME DA COMPETENZA/A COMPETENZA SULLE REGISTRAZIONI DI ECONOMICA
CNRCTB204.buildMovPrinc(aInventBene.cd_cds,aBS.esercizio,aInventBene.cd_unita_organizzativa,aVoceEPCG,aRecAssTrasfBeni.valore_iniziale + aRecAssTrasfBeni.variazione_piu - aRecAssTrasfBeni.variazione_meno,CNRCTB200.IS_DARE,Trunc(ABuono_carico_scarico.DATA_REGISTRAZIONE),Trunc(ABuono_carico_scarico.DATA_REGISTRAZIONE),0,
                aInventBene.TI_COMMERCIALE_ISTITUZIONALE,
                aListaMovimenti,aUser,aTSnow);
--Dbms_Output.put_line('amm '||aRecAssTrasfBeni.valore_ammortizzato);
   if aRecAssTrasfBeni.valore_ammortizzato > 0 then
-- 30/12/2004 METTEVA LA DATA DI SISTEMA COME DA COMPETENZA/A COMPETENZA SULLE REGISTRAZIONI DI ECONOMICA
    CNRCTB204.buildMovPrinc(aInventBene.cd_cds,aBS.esercizio,aInventBene.cd_unita_organizzativa,aVoceEPCGContr,aRecAssTrasfBeni.valore_ammortizzato,CNRCTB200.IS_AVERE,Trunc(ABuono_carico_scarico.DATA_REGISTRAZIONE),Trunc(ABuono_carico_scarico.DATA_REGISTRAZIONE),0,
                        aInventBene.TI_COMMERCIALE_ISTITUZIONALE,
                        aListaMovimenti,aUser,aTSnow);

   end if;
   aImporto:=aRecAssTrasfBeni.valore_iniziale + aRecAssTrasfBeni.variazione_piu - aRecAssTrasfBeni.variazione_meno - aRecAssTrasfBeni.valore_ammortizzato;
--Dbms_Output.put_line('saldo '||(aRecAssTrasfBeni.valore_iniziale + aRecAssTrasfBeni.variazione_piu - aRecAssTrasfBeni.variazione_meno-aRecAssTrasfBeni.valore_ammortizzato));
   if aImporto > 0 then
   -- 30/12/2004 METTEVA LA DATA DI SISTEMA COME DA COMPETENZA/A COMPETENZA SULLE REGISTRAZIONI DI ECONOMICA
    CNRCTB204.buildMovPrinc(aInventBene.cd_cds,aBS.esercizio,aInventBene.cd_unita_organizzativa,aVoceEPPatrimNetto,aImporto,CNRCTB200.IS_AVERE,Trunc(ABuono_carico_scarico.DATA_REGISTRAZIONE),Trunc(ABuono_carico_scarico.DATA_REGISTRAZIONE),0,
                        aInventBene.TI_COMMERCIALE_ISTITUZIONALE,
                        aListaMovimenti,aUser,aTSnow);
     Elsif aImporto < 0 Then
   -- 27/01/2006 rp aggiunta gestione storno voce patrimoniale
	Cnrctb204.buildMovPrinc(aInventBene.cd_cds,ABS.ESERCIZIO,aInventBene.cd_unita_organizzativa,aVoceEPPatrimNetto,aImporto*(-1),Cnrctb200.IS_DARE,TRUNC(ABuono_carico_scarico.DATA_REGISTRAZIONE),TRUNC(ABuono_carico_scarico.DATA_REGISTRAZIONE),0,
                        aInventBene.TI_COMMERCIALE_ISTITUZIONALE,
                        aListaMovimenti,aUser,aTSnow);
   end if;
  Else
   aVoceEPSoprAtt:=CNRCTB002.GETVOCEEPSOPRAVVATTIVE(aBS.esercizio);
   aImporto:=aBS.quantita*aBS.valore_unitario;
  -- Dbms_Output.put_line('no trasf importo '||aImporto);
-- 30/12/2004 METTEVA LA DATA DI SISTEMA COME DA COMPETENZA/A COMPETENZA SULLE REGISTRAZIONI DI ECONOMICA
   CNRCTB204.buildMovPrinc(aInventBene.cd_cds,aBS.esercizio,aInventBene.cd_unita_organizzativa,aVoceEPCG,aImporto,CNRCTB200.IS_DARE,Trunc(ABuono_carico_scarico.DATA_REGISTRAZIONE),Trunc(ABuono_carico_scarico.DATA_REGISTRAZIONE),0,
                                aInventBene.TI_COMMERCIALE_ISTITUZIONALE,
                                aListaMovimenti,aUser,aTSnow);
--Dbms_Output.put_line('no trasf importo '||aImporto);
-- 30/12/2004 METTEVA LA DATA DI SISTEMA COME DA COMPETENZA/A COMPETENZA SULLE REGISTRAZIONI DI ECONOMICA
   CNRCTB204.buildMovPrinc(aInventBene.cd_cds,aBS.esercizio,aInventBene.cd_unita_organizzativa,aVoceEPSoprAtt,aImporto,CNRCTB200.IS_AVERE,Trunc(ABuono_carico_scarico.DATA_REGISTRAZIONE),Trunc(ABuono_carico_scarico.DATA_REGISTRAZIONE),0,
                        aInventBene.TI_COMMERCIALE_ISTITUZIONALE,
                        aListaMovimenti,aUser,aTSnow);

  end if;

  aScrittura:=null;
  aScrittura.CD_CDS:=aInventBene.cd_cds;
  aScrittura.ESERCIZIO:=aBS.esercizio;
  aScrittura.CD_UNITA_ORGANIZZATIVA:=aInventBene.cd_unita_organizzativa;
  aScrittura.ORIGINE_SCRITTURA:=CNRCTB200.ORIGINE_DOCUMENTO_AMM;
  aScrittura.CD_TIPO_DOCUMENTO:=TI_DOC_FITT_CAR_BENE;
  aScrittura.CD_CAUSALE_COGE:=aCausaleCoge;
  -- La chiave del documento viene costruita per concatenazione delle componenti della chiave del buono di scarico
  aScrittura.CD_COMP_DOCUMENTO:=aBS.pg_inventario||'.'||aBS.ti_documento||'.'||aBS.esercizio||'.'||aBS.pg_buono_c_s||'.'||aBS.nr_inventario||'.'||aBS.progressivo;
  aScrittura.CD_CDS_DOCUMENTO:=aInventBene.cd_cds;
  aScrittura.CD_UO_DOCUMENTO:=aInventBene.cd_unita_organizzativa;
  aScrittura.PG_NUMERO_DOCUMENTO:=NULL;
  aScrittura.IM_SCRITTURA := null; -- Impostato come totale di sezione in realizzazione della scrittura
  aScrittura.DT_CONTABILIZZAZIONE := Trunc(ABuono_carico_scarico.DATA_REGISTRAZIONE); --TRUNC(aTSNow); -- stani
  aScrittura.DT_PAGAMENTO:=null;
  aScrittura.DT_CANCELLAZIONE:=null;
  aScrittura.TI_SCRITTURA:=CNRCTB200.TI_SCRITTURA_SINGOLA;
  aScrittura.CD_TERZO:=0;
  aScrittura.STATO:=CNRCTB200.STATO_DEFINITIVO;
  aScrittura.CD_DIVISA:=NULL;
  aScrittura.COSTO_PLURIENNALE:=null; -- chi lo valorizza e come non ? chiaro
  aScrittura.PG_ENTE:=null; -- serve a rinumerare le scritture  livello di ENTE

  aScrittura.DS_SCRITTURA := 'Carico bene durevole '||
        aBS.pg_inventario||'/'||aBS.ti_documento||'/'||aBS.esercizio||'/'||aBS.pg_buono_c_s||'/'||aBS.nr_inventario||'/'||aBS.progressivo;

  aScrittura.PG_SCRITTURA_ANNULLATA:=null; -- codice della scrittura annullata in effettuazione storno
  aScrittura.ATTIVA:='Y';
  aScrittura.ESERCIZIO_DOCUMENTO_AMM:=aBS.esercizio;
  aScrittura.DACR:=aTSNow;
  aScrittura.UTCR:=aUser;
  -- Registro la scrittura
  CNRCTB200.CREASCRITTCOGE(aScrittura,aListaMovimenti);

  -- Verifica di effettiva modifica della scrittura

  aListaNuoveScritture(1):=aScrittura;

  if not CNRCTB200.isModificata(aListaVecchieScritture, aListaNuoveScritture) then
   rollback to savepoint CNRCTB205_SP_002;
  end if;

  -- Aggiorno lo stato coge del buono di scarico
  update buono_carico_scarico_dett set
   stato_coge = CNRCTB100.STATO_COEP_CON,
   pg_ver_rec = pg_ver_rec+1
  where
       pg_inventario = aBS.pg_inventario
   and ti_documento = aBS.ti_documento
   and esercizio = aBS.esercizio
   and pg_buono_c_s=aBS.pg_buono_c_s
   and nr_inventario = aBS.nr_inventario
   and progressivo = aBS.progressivo;
 end;


 procedure regMigrazioneBeniCoge
    (aEs NUMBER,
     aCdCds VARCHAR2,
     aCdUo VARCHAR2,
     aCdCategoriaGruppo VARCHAR2,
     aValoreAssestato NUMBER,
     aValoreAmmortizzato NUMBER,
     aUser VARCHAR2,
     aTSNow DATE,
     astato CHAR) IS
  aCatGruppo CATEGORIA_GRUPPO_INVENT%rowtype;
  aCatGruppoVoceEp CATEGORIA_GRUPPO_VOCE_EP%rowtype;
  aVoceEPCG voce_ep%rowtype;
  aVoceFondoAmm voce_ep%rowtype;
  aVoceEPMigraBeni voce_ep%rowtype;
  aScrittura scrittura_partita_doppia%rowtype;
  aListaMovimenti CNRCTB200.movimentiList;
  aScritturaTmp scrittura_partita_doppia%rowtype;
  aListaM CNRCTB200.movimentiList;
  aListaVecchieScritture CNRCTB200.scrittureList;
  aListaNuoveScritture CNRCTB200.scrittureList;

begin
  recParametriCNR := CNRUTL001.getRecParametriCnr(aEs);

  -- Fix del 20040924 Richiesta 843
  CNRCTB204.checkChiusuraEsercizio(aEs, aCdCds);

  if not (CNRCTB200.ISCHIUSURACOEPDEF(aEs-1, aCdCds)='Y') then
   IBMERR001.RAISE_ERR_GENERICO('L''esercizio economico precedente '||To_Char(aEs-1)||' non ? chiuso definitivamente per il cds: ' || aCdCds);
  end if;

  begin
    select * into aCatGruppo from CATEGORIA_GRUPPO_INVENT where
           cd_categoria_gruppo = aCdCategoriaGruppo;
    exception when NO_DATA_FOUND then
     IBMERR001.RAISE_ERR_GENERICO('Gruppo inventariale non trovato:'||aCdCategoriaGruppo);
    end;
   BEGIN
			if recParametriCNR.fl_nuovo_pdg='N' then
	      SELECT * INTO aCatGruppoVoceEp FROM CATEGORIA_GRUPPO_VOCE_EP WHERE
              cd_categoria_gruppo = aCatGruppo.cd_categoria_padre
  				AND sezione = CNRCTB200.IS_AVERE
  				AND ESERCIZIO=aEs;
  		else
       	      SELECT * INTO aCatGruppoVoceEp FROM CATEGORIA_GRUPPO_VOCE_EP WHERE
              cd_categoria_gruppo = aCatGruppo.cd_categoria_gruppo
  				AND sezione = CNRCTB200.IS_AVERE
  				AND ESERCIZIO=aEs
  				and fl_default = 'Y';
  		end if;
		EXCEPTION WHEN NO_DATA_FOUND THEN
				IBMERR001.RAISE_ERR_GENERICO('Nessun conto economico associato a categoria:'||aCatGruppo.cd_categoria_gruppo);
		END;

aVoceEPCG := CNRCTB002.getVoceEp(aCatGruppoVoceEp.esercizio,aCatGruppoVoceEp.cd_voce_ep); -- A.02.004
if (aCatGruppoVoceEp.cd_voce_ep_contr is not null) then
aVoceFondoAmm :=
        CNRCTB002.getVoceEp(aCatGruppoVoceEp.esercizio,aCatGruppoVoceEp.cd_voce_ep_contr); -- P.01.004
end if;
aVoceEPMigraBeni := CNRCTB002.getVoceEpMigraBeni(aEs); -- X.01.001
 If (aStato ='N') Then
 CNRCTB204.buildMovPrinc(aCdCds,aEs,aCdUo, aVoceEPCG, aValoreAssestato, CNRCTB200.IS_DARE,To_Date('3112'||aEs, 'ddmmyyyy'),To_Date('3112'||aEs, 'ddmmyyyy'),0,CNRCTB100.TI_ISTITUZIONALE,aListaMovimenti,aUser,aTSnow);

  IF aValoreAmmortizzato = 0 THEN
     CNRCTB204.buildMovPrinc(aCdCds,aEs,aCdUo, aVoceEPMigraBeni, aValoreAssestato, CNRCTB200.IS_AVERE,To_Date('3112'||aEs, 'ddmmyyyy'),To_Date('3112'||aEs, 'ddmmyyyy'),0,CNRCTB100.TI_ISTITUZIONALE,aListaMovimenti,aUser,aTSnow);
  ELSE
     CNRCTB204.buildMovPrinc(aCdCds,aEs,aCdUo, aVoceFondoAmm, aValoreAmmortizzato, CNRCTB200.IS_AVERE,To_Date('3112'||aEs, 'ddmmyyyy'),To_Date('3112'||aEs, 'ddmmyyyy'),0,CNRCTB100.TI_ISTITUZIONALE,aListaMovimenti,aUser,aTSnow);
     If (aValoreAssestato - aValoreAmmortizzato!=0) Then
     	CNRCTB204.buildMovPrinc(aCdCds,aEs,aCdUo, aVoceEPMigraBeni, aValoreAssestato - aValoreAmmortizzato, CNRCTB200.IS_AVERE,To_Date('3112'||aEs, 'ddmmyyyy'),To_Date('3112'||aEs, 'ddmmyyyy'),0,CNRCTB100.TI_ISTITUZIONALE,aListaMovimenti,aUser,aTSnow);
     End If;

  END IF;
End If;
  aScrittura:=null;
  aScrittura.CD_CDS:=aCdCds;
  aScrittura.ESERCIZIO:=aEs;
  aScrittura.CD_UNITA_ORGANIZZATIVA:=aCdUo;
  aScrittura.ORIGINE_SCRITTURA:=CNRCTB200.ORIGINE_MIGRA_BENI;
  aScrittura.CD_TIPO_DOCUMENTO:=TI_DOC_FITT_MIGRA_BENE;
  aScrittura.CD_CAUSALE_COGE:=CNRCTB200.CAU_MIGRAZIONE_BENI;
  -- La chiave del documento viene costruita per concatenazione dei riferimenti al cds, uo e categoria gruppo
  aScrittura.CD_COMP_DOCUMENTO:=aCdCds||'.'||aCdUo||'.'||aCdCategoriaGruppo;
  aScrittura.CD_CDS_DOCUMENTO:=aCdCds;
  aScrittura.CD_UO_DOCUMENTO:=aCdUo;
  aScrittura.PG_NUMERO_DOCUMENTO:=NULL;
  aScrittura.IM_SCRITTURA:=null; -- Impostato come totale di sezione in realizzazione della scrittura
-- 09.08.2006 data contabilizzazione = primo giorno dell'anno (migrazione)
  aScrittura.DT_CONTABILIZZAZIONE := To_Date('0101'||aEs, 'ddmmyyyy'); -- TRUNC(aTSNow); -- stani
  aScrittura.DT_PAGAMENTO:=null;
  aScrittura.DT_CANCELLAZIONE:=null;
  aScrittura.TI_SCRITTURA:=CNRCTB200.TI_SCRITTURA_SINGOLA;
  aScrittura.CD_TERZO:=0;
  aScrittura.STATO:=CNRCTB200.STATO_DEFINITIVO;
  aScrittura.CD_DIVISA:=NULL;
  aScrittura.COSTO_PLURIENNALE:=null; -- chi lo valorizza e come non ? chiaro
  aScrittura.PG_ENTE:=null; -- serve a rinumerare le scritture  livello di ENTE

  aScrittura.DS_SCRITTURA := 'Scrittura da migrazione beni '||aCdCds||'/'||aCdUo||'/'||aCdCategoriaGruppo;

  aScrittura.PG_SCRITTURA_ANNULLATA:=null; -- codice della scrittura annullata in effettuazione storno
  aScrittura.ATTIVA:='Y';
  aScrittura.ESERCIZIO_DOCUMENTO_AMM:=aEs;
  aScrittura.DACR:=aTSNow;
  aScrittura.UTCR:=aUser;
  -- Registro la scrittura
  CNRCTB200.CREASCRITTCOGE(aScrittura,aListaMovimenti);

  -- Aggiorno lo stato coge dei record di CNR_INVENTARIO_BENI_MIG
  UPDATE buono_carico_scarico_dett
  SET    stato_coge = CNRCTB100.STATO_COEP_CON
  WHERE  esercizio = aEs AND
  	 ti_documento = 'C' and
         stato_coge IN (CNRCTB100.STATO_COEP_INI,CNRCTB100.STATO_COEP_DA_RIP) and
         (pg_inventario,nr_inventario,progressivo) in
         (select pg_inventario,nr_inventario,progressivo from inventario_beni where
	         cd_categoria_gruppo = aCdCategoriaGruppo AND
         	 cd_cds = aCdCds AND
         	 cd_unita_organizzativa = aCdUo And
         	 buono_carico_scarico_dett.dacr Like inventario_beni.dacr And
         	 esercizio_carico_bene = aEs    and
         	 fl_migrato = 'Y');

 end;

 function getDesc(aLiqIva liquidazione_iva%rowtype) return varchar2 is
 begin
  return 'Liquidazione mensile IVA di tipo '||aLiqIva.tipo_liquidazione||' es:'||aLiqIva.esercizio||' cds:'||aLiqIva.cd_cds||' uo:'||aLiqIva.cd_unita_organizzativa||' periodo:'||to_char(aLiqIva.dt_inizio,'DD/MM/YYYY')||'-'||to_char(aLiqIva.dt_fine,'DD/MM/YYYY');
 end;

Procedure regLiqIvaMensileCOGE(aEs number, aCds varchar2, aUO varchar2, aTipo char, aDtDa date, aDtA date, aUser varchar2) is
  aLiqIva liquidazione_iva%rowtype;
  aVoceDebVersoErario voce_ep%rowtype;
  aVoceEarioCIVA voce_ep%rowtype;
  aVoceIvaDebito voce_ep%rowtype;
  aVoceIvaCredito voce_ep%rowtype;
  aVoceBancaCds voce_ep%rowtype;
  aVoceCostoIvaNonDetraibile voce_ep%rowtype;
  aVoceRicavoIvaNonDetraibile voce_ep%rowtype;
  aGenRiga documento_generico_riga%rowtype;
  aGen documento_generico%rowtype;
  aTSNow date;
  aListaMovimenti CNRCTB200.movimentiList;
  aScrittura scrittura_partita_doppia%rowtype;
  aScritturaEnte scrittura_partita_doppia%rowtype;
  aListaMovimentiEnte CNRCTB200.movimentiList;
  aDtContabil date;
  aUOVERSIVA unita_organizzativa%rowtype;
  aUOVERSCENTRO unita_organizzativa%rowtype;
  aUOENTE unita_organizzativa%rowtype;
  aPK2String varchar2(50);
  aCdTerzo number(8);
  isVersamentoCentro boolean;
  IST_COMM_LIQ  MOVIMENTO_COGE.TI_ISTITUZ_COMMERC%Type;
  DaData date:=null;
  AData date:=null;
  voce varchar2(30);
Begin

Dbms_Output.PUT_LINE ('ENTRO IN regLiqIvaMensileCOGE');

  If aEs is Null Or aCds is Null Or aUO is Null Or aTipo is Null Or aDtDa is Null Or aDtA is Null Or
        aUser is Null Then
   IBMERR001.RAISE_ERR_GENERICO('Parametri per registrazione economica liquidazione mensile IVA ente non specificati');
  End if;

  -- Fix del 20040924 Richiesta 843
  CNRCTB204.checkChiusuraEsercizio(aEs, aCds);

Dbms_Output.PUT_LINE ('DOPO checkChiusuraEsercizio');

  -- L'esercizio economico precedente deve essere chiuso definitivamente
  if not (CNRCTB200.ISCHIUSURACOEPDEF(aEs-1, aCds)='Y') then
   IBMERR001.RAISE_ERR_GENERICO('L''esercizio economico precedente '||To_Char(aEs-1)||' non ? chiuso definitivamente per il cds: '||aCds);
  end if;

  aTSNow:=sysdate;
  -- Estrazione dell'UO di versamento IVA
  aUOVERSCENTRO:=CNRCTB020.getUOVersIVA(aEs);
  aUOENTE:=CNRCTB020.getUOENTE(aEs);
  recParametriCnr:=CNRUTL001.getRecParametriCnr(aEs);

  Begin

   Select *
   Into   aLiqIva
   From   liquidazione_iva
   Where  esercizio = aEs And
          cd_cds = aCds And
          cd_unita_organizzativa = aUO And
          tipo_liquidazione = aTipo And
          dt_inizio = aDtDa And
          dt_fine = aDtA And
          report_id = 0
   For Update nowait;

-- DERIVO IL TIPO ISTITUZIONALE/COMMERCIALE DELLA SCRITTURA CHE VERRA' A PARTIRE DAL TIPO_LIQUIDAZIONE:
--  SE "C = Sezionali commerciali" ALLORA E' COMMERCIALE
--  SE "I = Sezionali istituzionali intraue" OPPURE "S = Sezionali istituzionali San Marino senza IVA"
--         ALLORA E' ISTITUZIONALE

Dbms_Output.PUT_LINE ('TROVATA LIQUIDAZIONE_IVA');

   If aLiqIva.TIPO_LIQUIDAZIONE = 'C' Then
        IST_COMM_LIQ := 'C';
   Elsif aLiqIva.TIPO_LIQUIDAZIONE In ('I', 'S', 'X', 'P') Then
        IST_COMM_LIQ := 'I';
   End If;

  Exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO(getDesc(aLiqIva)||' non trovata.');
  End;

  if aLiqIva.cd_unita_organizzativa=aUOENTE.cd_unita_organizzativa Then
   aUOVERSIVA:=aUOVERSCENTRO;
   isVersamentoCentro:=true;
   -- Modifica del 22/04/2004
   -- la scrittura che gira il conto erario IVA sulla banca viene fatta sul mandato di versamento
   -- del centro e quindi la gestione non ? pi? fatta a livello di questo metodo
   Dbms_Output.PUT_LINE ('ESCE 1');
   return;
  else
   aUOVERSIVA:=CNRCTB020.getUOValida(aLiqIva.esercizio,aLiqIva.cd_unita_organizzativa);
   isVersamentoCentro:=false;
  end if;

-- Verifica che le scritture non siano gi? state fatte
aPK2String:=CNRCTB204.pk2String(aLiqIva);

For aTSCR in (Select *
              From   scrittura_partita_doppia
              Where  esercizio = aEs And
                     cd_cds= aUOVERSIVA.cd_unita_padre And
                     cd_unita_organizzativa = aUOVERSIVA.cd_unita_organizzativa And
                     origine_scrittura = CNRCTB200.ORIGINE_LIQUID_IVA And
                     cd_comp_documento = aPK2String
	      For Update nowait) Loop
   IBMERR001.RAISE_ERR_GENERICO(getDesc(aLiqIva)||': Scrittura gi? eseguita.');
End Loop;

  aDtContabil := trunc(aTSNow);

  aCdTerzo := 0;

if(recParametriCNR.fl_tesoreria_unica='N') then
  -- Estrazione dei dati di versamento (doc generico) se esistono
  Begin

Dbms_Output.PUT_LINE ('CERCA DOC GEN');

   Select *
   Into   aGen
   From   documento_generico
   Where  cd_tipo_documento_amm = aLiqIva.cd_tipo_documento And
          esercizio = aLiqIva.esercizio_doc_amm And
          cd_cds= aLiqIva.cd_cds_doc_amm And
          cd_unita_organizzativa = aLiqIva.cd_uo_doc_amm And
          pg_documento_generico = aLiqIva.pg_doc_amm;

   For aTGenRiga in (Select *
                     From   documento_generico_riga
                     Where  cd_tipo_documento_amm = aLiqIva.cd_tipo_documento And
                            esercizio = aLiqIva.esercizio_doc_amm And
                            cd_cds= aLiqIva.cd_cds_doc_amm And
                            cd_unita_organizzativa = aLiqIva.cd_uo_doc_amm And
                            pg_documento_generico = aLiqIva.pg_doc_amm) Loop

    aGenRiga:=aTGenRiga;
    Exit;
   End Loop;
   begin
   select distinct cd_elemento_voce  into voce
   from obbligazione
   where
   obbligazione.esercizio = aGenRiga.esercizio_obbligazione and
   obbligazione.cd_cds = aGenRiga.cd_cds_obbligazione and
   obbligazione.pg_obbligazione = aGenRiga.pg_obbligazione and
   obbligazione.esercizio_originale = aGenRiga.ESERCIZIO_ORI_OBBLIGAZIONE ;

   exception when too_many_rows then
   IBMERR001.RAISE_ERR_GENERICO('Documento generico di versamento IVA su pi? voci');
  End;

   if not isVersamentoCentro then
     if recParametriCnr.fl_nuovo_pdg='N' then
    	aVoceDebVersoErario:=CNRCTB204.trovaContoAnag(aLiqIva.esercizio, aGenRiga.cd_terzo, aLiqIva.cd_tipo_documento, null,voce);
    end if;
   end if;

   aDtContabil := trunc(aGen.data_registrazione); -- da vedere per data 01/01 anno prec

   aCdTerzo := aGenRiga.cd_terzo;

  Exception when NO_DATA_FOUND then
     Null; -- IBMERR001.RAISE_ERR_GENERICO('Documento generico di versamento IVA non trovato');
  End;
end if;
  -- Estrazione dei conti
  aVoceEarioCIVA:=CNRCTB002.GETVOCEEPERARIOCIVA(aLiqIva.esercizio);
  if (aVoceDebVersoErario.cd_voce_ep is null) then
    	aVoceDebVersoErario:=CNRCTB002.GETVOCEEPERARIOCIVAGIROCONTOUO(aLiqIva.esercizio);
  end if;
  if not isVersamentoCentro then
   aVoceIvaDebito:=CNRCTB002.GETVOCEEPIVADEBITO(aLiqIva.esercizio);
   aVoceIvaCredito:=CNRCTB002.GETVOCEEPIVACREDITO(aLiqIva.esercizio);
  end if;

-- prima REGISTRAZIONE ===> IVA VENDITE

Dbms_Output.PUT_LINE ('PREPARA SCRITTURA '||aUOVERSIVA.cd_unita_padre);

  aScrittura:=null;
  aScrittura.CD_CDS:=aUOVERSIVA.cd_unita_padre;
  aScrittura.ESERCIZIO:=aEs;
  aScrittura.CD_UNITA_ORGANIZZATIVA:=aUOVERSIVA.cd_unita_organizzativa;
  aScrittura.ORIGINE_SCRITTURA:=CNRCTB200.ORIGINE_LIQUID_IVA;
  aScrittura.CD_CAUSALE_COGE:=CNRCTB200.CAU_LIQIVAVENDITE;
  aScrittura.CD_COMP_DOCUMENTO:=aPK2String;
  aScrittura.CD_TIPO_DOCUMENTO:=aGenRiga.cd_tipo_documento_amm;
  aScrittura.CD_CDS_DOCUMENTO:=aGenRiga.cd_cds;
  aScrittura.CD_UO_DOCUMENTO:=aGenRiga.cd_unita_organizzativa;
  aScrittura.PG_NUMERO_DOCUMENTO:=aGenRiga.pg_documento_generico;
  aScrittura.IM_SCRITTURA:=null; -- Impostato come totale di sezione in realizzazione della scrittura

  If To_Char(aDtContabil, 'yyyy') = aEs Then
   aScrittura.DT_CONTABILIZZAZIONE := aDtContabil;
  Else
   aScrittura.DT_CONTABILIZZAZIONE := To_Date('3112'||aes, 'DDMMYYYY');
  End If;
  -- Le date di competenza della liquidazione di gennaio si riferisco all'iva di compentenza di dicembre dell'anno precedente
  if To_char(aDtDa,'yyyy') < aEs then
	   DaData:=to_date('0101'||to_char(aEs),'ddmmyyyy');
		 AData :=to_date('0101'||to_char(aEs),'ddmmyyyy');
	else
	   DaData :=null;
	   AData	:=null;
	end if;

  aScrittura.DT_PAGAMENTO:=null;
  aScrittura.DT_CANCELLAZIONE:=null;
  aScrittura.TI_SCRITTURA:=CNRCTB200.TI_SCRITTURA_PRIMA; -- Si tratta del tipo di scrittura prima o ultima
  aScrittura.CD_TERZO:=aCdTerzo;
  aScrittura.STATO:=CNRCTB200.STATO_DEFINITIVO;
  aScrittura.CD_DIVISA:=aGen.cd_divisa;
  aScrittura.COSTO_PLURIENNALE:=null; -- chi lo valorizza e come non ? chiaro
  aScrittura.PG_ENTE:=null; -- serve a rinumerare le scritture  livello di ENTE

  aScrittura.DS_SCRITTURA := 'Liquidazione IVA mensile CDS '||aUOVERSIVA.cd_unita_padre||', UO '||aUOVERSIVA.cd_unita_organizzativa;

  aScrittura.PG_SCRITTURA_ANNULLATA:=null; -- codice della scrittura annullata in effettuazione storno
  aScrittura.ATTIVA:='Y';
  aScrittura.ESERCIZIO_DOCUMENTO_AMM:=aGenRiga.esercizio;
  aScrittura.DACR:=aTSNow;
  aScrittura.UTCR:=aUser;

if (recParametriCNR.fl_nuovo_pdg='Y' ) then

  aScritturaEnte:=null;
  aScritturaEnte.CD_CDS:=aUOVERSCENTRO.cd_unita_padre;
  aScritturaEnte.ESERCIZIO:=aEs;
  aScritturaEnte.CD_UNITA_ORGANIZZATIVA:=aUOVERSCENTRO.cd_unita_organizzativa;
  aScritturaEnte.ORIGINE_SCRITTURA:=CNRCTB200.ORIGINE_LIQUID_IVA;
  aScritturaEnte.CD_CAUSALE_COGE:=CNRCTB200.CAU_LIQIVAVENDITE;
  aScritturaEnte.CD_COMP_DOCUMENTO:=aPK2String;
  aScritturaEnte.CD_TIPO_DOCUMENTO:=aGenRiga.cd_tipo_documento_amm;
  aScritturaEnte.CD_CDS_DOCUMENTO:=aGenRiga.cd_cds;
  aScritturaEnte.CD_UO_DOCUMENTO:=aGenRiga.cd_unita_organizzativa;
  aScritturaEnte.PG_NUMERO_DOCUMENTO:=aGenRiga.pg_documento_generico;
  aScritturaEnte.IM_SCRITTURA:=null; -- Impostato come totale di sezione in realizzazione della scrittura

  If To_Char(aDtContabil, 'yyyy') = aEs Then
   aScritturaEnte.DT_CONTABILIZZAZIONE := aDtContabil;
  Else
   aScritturaEnte.DT_CONTABILIZZAZIONE := To_Date('3112'||aes, 'DDMMYYYY');
  End If;
  -- Le date di competenza della liquidazione di gennaio si riferisco all'iva di compentenza di dicembre dell'anno precedente
  if To_char(aDtDa,'yyyy') < aEs then
	   DaData:=to_date('0101'||to_char(aEs),'ddmmyyyy');
		 AData :=to_date('0101'||to_char(aEs),'ddmmyyyy');
	else
	   DaData :=null;
	   AData	:=null;
	end if;

  aScritturaEnte.DT_PAGAMENTO:=null;
  aScritturaEnte.DT_CANCELLAZIONE:=null;
  aScritturaEnte.TI_SCRITTURA:=CNRCTB200.TI_SCRITTURA_PRIMA; -- Si tratta del tipo di scrittura prima o ultima
  aScritturaEnte.CD_TERZO:=aCdTerzo;
  aScritturaEnte.STATO:=CNRCTB200.STATO_DEFINITIVO;
  aScritturaEnte.CD_DIVISA:=aGen.cd_divisa;
  aScritturaEnte.COSTO_PLURIENNALE:=null; -- chi lo valorizza e come non ? chiaro
  aScritturaEnte.PG_ENTE:=null; -- serve a rinumerare le scritture  livello di ENTE

  aScritturaEnte.DS_SCRITTURA := 'Liquidazione IVA mensile CDS '||aUOVERSIVA.cd_unita_padre||', UO '||aUOVERSIVA.cd_unita_organizzativa;

  aScritturaEnte.PG_SCRITTURA_ANNULLATA:=null; -- codice della scrittura annullata in effettuazione storno
  aScritturaEnte.ATTIVA:='Y';
  aScritturaEnte.ESERCIZIO_DOCUMENTO_AMM:=aGenRiga.esercizio;
  aScritturaEnte.DACR:=aTSNow;
  aScritturaEnte.UTCR:=aUser;

end if;


  -- Scritture SOLO per le UO decentrate
  if not isVersamentoCentro then
   -- Generazione della scrittura per le vendite
   if aLiqIva.iva_Debito > 0 then
    aListaMovimenti.delete;

Dbms_Output.PUT_LINE ('PREPARA MOVIMENTI 1');

    CNRCTB204.buildMovPrinc(aScrittura.cd_cds,aScrittura.esercizio,aScrittura.cd_unita_organizzativa,
                            aVoceIvaDebito,aLiqIva.iva_Debito,CNRCTB200.IS_DARE,nvl(DaData,aDtDa),nvl(AData,aDtA),aCdTerzo,
                            IST_COMM_LIQ,--CNRCTB100.TI_ISTITUZIONALE,
                            aListaMovimenti,aUser,aTSnow);
    CNRCTB204.buildMovPrinc(aScrittura.cd_cds,aScrittura.esercizio,aScrittura.cd_unita_organizzativa,aVoceEarioCIVA,aLiqIva.iva_Debito,CNRCTB200.IS_AVERE,nvl(DaData,aDtDa),nvl(AData,aDtA),aCdTerzo,
                            IST_COMM_LIQ,--CNRCTB100.TI_ISTITUZIONALE,
                            aListaMovimenti,aUser,aTSnow);
    aScrittura.ds_scrittura:='IVA VENDITE';

Dbms_Output.PUT_LINE ('INS SCRITTURA 1');

CNRCTB200.CREASCRITTCOGE(aScrittura,aListaMovimenti);
   end if;


-- seconda REGISTRAZIONE ===> IVA ACQUISTI

   -- Errore 830 - al posto di iva_credito va utilizzata l'iva a credito senza
   -- prorata (campo iva_credito_no_prorata della liquidazione)
   -- Generazione della scrittura per gli acquisti
   if aLiqIva.iva_credito_no_prorata > 0 then
    aListaMovimenti.delete;
    aScrittura.pg_scrittura:=null;
    aScrittura.im_Scrittura:=null;
    aScrittura.CD_CAUSALE_COGE:=CNRCTB200.CAU_LIQIVAACQUISTI;
    CNRCTB204.buildMovPrinc(aScrittura.cd_cds,aScrittura.esercizio,aScrittura.cd_unita_organizzativa,aVoceEarioCIVA,aLiqIva.iva_credito_no_prorata,CNRCTB200.IS_DARE,nvl(DaData,aDtDa),nvl(AData,aDtA),aCdTerzo,
                            IST_COMM_LIQ,--CNRCTB100.TI_ISTITUZIONALE,
                            aListaMovimenti,aUser,aTSnow);
    CNRCTB204.buildMovPrinc(aScrittura.cd_cds,aScrittura.esercizio,aScrittura.cd_unita_organizzativa,aVoceIvaCredito,aLiqIva.iva_credito_no_prorata,CNRCTB200.IS_AVERE,nvl(DaData,aDtDa),nvl(AData,aDtA),aCdTerzo,
                            IST_COMM_LIQ,--CNRCTB100.TI_ISTITUZIONALE,
                            aListaMovimenti,aUser,aTSnow);
    aScrittura.ds_scrittura:='IVA ACQUISTI';

Dbms_Output.PUT_LINE ('INS SCRITTURA 2');

    CNRCTB200.CREASCRITTCOGE(aScrittura,aListaMovimenti);
   end if;
  end if;

-- terza REGISTRAZIONE ===> IVA SALDO

  -- Generazione della scrittura per il saldo se necessaria per aprire il debito verso
  -- l'erario chiuso dal pagamento del mandato

  aListaMovimenti.delete;
  aScrittura.pg_scrittura:=null;
  aScrittura.im_Scrittura:=null;
  aScrittura.ds_scrittura:='IVA SALDO';
  aScrittura.CD_CAUSALE_COGE:=CNRCTB200.CAU_LIQIVASALDO;

  -- Movimenti per l'ENTE
  if isVersamentoCentro Then -- se la UO ? la UO ENTE

   aVoceBancaCds:=CNRCTB002.GETVOCEEPBANCACDS(aLiqIva.esercizio);
Dbms_Output.PUT_LINE ('PREPARA MOVIMENTI 2');
   CNRCTB204.buildMovPrinc(aScrittura.cd_cds,aScrittura.esercizio,aScrittura.cd_unita_organizzativa,aVoceEarioCIVA,nvl(aGen.im_totale,aLiqIva.iva_versata),CNRCTB200.IS_DARE,nvl(DaData,aDtDa),nvl(AData,aDtA),aCdTerzo,
                           IST_COMM_LIQ,--CNRCTB100.TI_ISTITUZIONALE,
                           aListaMovimenti,aUser,aTSnow);
   CNRCTB204.buildMovPrinc(aScrittura.cd_cds,aScrittura.esercizio,aScrittura.cd_unita_organizzativa,aVoceBancaCds,nvl(aGen.im_totale,aLiqIva.iva_versata),CNRCTB200.IS_AVERE,nvl(DaData,aDtDa),nvl(AData,aDtA),aCdTerzo,
                           IST_COMM_LIQ,--CNRCTB100.TI_ISTITUZIONALE,
                           aListaMovimenti,aUser,aTSnow);
  Else  -- altrimenti (per tutte le altre UO normali)

  -- Movimenti per le UO decentrate

   aVoceCostoIvaNonDetraibile:=CNRCTB002.getVoceEpCostoIvaNonDetraibile(aLiqIva.esercizio);
   aVoceRicavoIvaNonDetraibile:=CNRCTB002.getVoceEpRicIvaNonDetraibile(aLiqIva.esercizio);

   if aGen.pg_documento_generico is not null or aLiqIva.iva_versata> 0 then

-- Movimenti nel caso ci sia iva da versare (si ? versata l'IVA)

-- NEI CASI IN CUI IL SALDO FINALE PREVEDE UN SALDO A DEBITO (QUINDI CON IVA DA VERSARE),
-- OLTRE ALLE SCRITTURE CHE TRAVASANO L'IVA A CREDITO E L'IVA A DEBITO NELL'ERARIO C/IVA
-- (GIA' FATTE PRIMA), SI DEVONO FARE ANCHE QUELLE CHE RILEVANO L'EVENTUALE COSTO PER IVA INDETRAIBILE E
-- E LA CHIUSURA DEL CONTO ERARIO SUL CONTO DEBITI VERSO ERARIO PER LA PARTE VERSATA

-- fa la registrazione con in AVERE l'Erario C/IVa per stornare la parte indetraibile (PRO RATA)

    If aLiqIva.iva_credito - aLiqIva.iva_credito_no_prorata < 0 then
Dbms_Output.PUT_LINE ('PREPARA MOVIMENTI 3');
     CNRCTB204.buildMovPrinc(aScrittura.cd_cds,aScrittura.esercizio,aScrittura.cd_unita_organizzativa,
                             aVoceEarioCIVA,
                             Abs(aLiqIva.iva_credito - aLiqIva.iva_credito_no_prorata),
                             CNRCTB200.IS_AVERE, nvl(DaData,aDtDa),nvl(AData,aDtA), aCdTerzo,
                             IST_COMM_LIQ,--CNRCTB100.TI_ISTITUZIONALE,
                             aListaMovimenti,aUser,aTSnow);

     CNRCTB204.buildMovPrinc(aScrittura.cd_cds,aScrittura.esercizio,aScrittura.cd_unita_organizzativa,
                             aVoceCostoIvaNonDetraibile,
                             Abs(aLiqIva.iva_credito - aLiqIva.iva_credito_no_prorata),
                             CNRCTB200.IS_DARE,nvl(DaData,aDtDa),nvl(AData,aDtA),aCdTerzo,
                             IST_COMM_LIQ,--CNRCTB100.TI_ISTITUZIONALE,
                             aListaMovimenti,aUser,aTSnow);
    End If;

-- COMUNQUE REGISTRA LA CHIUSURA DEL CONTO ERARIO C/IVA CON IL DEBITO VS/ERARIO
Dbms_Output.PUT_LINE ('PREPARA MOVIMENTI 4 ');
Dbms_Output.PUT_LINE (aVoceDebVersoErario.cd_voce_ep);
    CNRCTB204.buildMovPrinc(aScrittura.cd_cds,aScrittura.esercizio,aScrittura.cd_unita_organizzativa,
                            aVoceEarioCIVA, nvl(aGen.im_totale,aLiqIva.iva_versata),
                            CNRCTB200.IS_DARE,nvl(DaData,aDtDa),nvl(AData,aDtA),aCdTerzo,
                            IST_COMM_LIQ, --CNRCTB100.TI_ISTITUZIONALE,
                            aListaMovimenti,aUser,aTSnow);

    CNRCTB204.buildMovPrinc(aScrittura.cd_cds,aScrittura.esercizio,aScrittura.cd_unita_organizzativa,
                            aVoceDebVersoErario, nvl(aGen.im_totale,aLiqIva.iva_versata),
                            CNRCTB200.IS_AVERE,nvl(DaData,aDtDa),nvl(AData,aDtA),aCdTerzo,
                            IST_COMM_LIQ, --CNRCTB100.TI_ISTITUZIONALE,
                            aListaMovimenti,aUser,aTSnow);
      -- ente

      if(recParametriCnr.fl_tesoreria_unica ='Y' ) then
				Dbms_Output.PUT_LINE ('INS SCRITTURA ente');
	      aListaMovimentiEnte.delete;
	      CNRCTB204.buildMovPrinc(aScritturaEnte.cd_cds,aScritturaEnte.esercizio,aScritturaEnte.cd_unita_organizzativa,
	                            aVoceEarioCIVA, nvl(aGen.im_totale,aLiqIva.iva_versata),
	                            CNRCTB200.IS_AVERE,nvl(DaData,aDtDa),nvl(AData,aDtA),aCdTerzo,
	                            IST_COMM_LIQ, --CNRCTB100.TI_ISTITUZIONALE,
	                            aListaMovimentiEnte,aUser,aTSnow);

	      CNRCTB204.buildMovPrinc(aScritturaEnte.cd_cds,aScritturaEnte.esercizio,aScritturaEnte.cd_unita_organizzativa,
	                            aVoceDebVersoErario, nvl(aGen.im_totale,aLiqIva.iva_versata),
	                            CNRCTB200.IS_DARE,nvl(DaData,aDtDa),nvl(AData,aDtA),aCdTerzo,
	                            IST_COMM_LIQ, --CNRCTB100.TI_ISTITUZIONALE,
	                            aListaMovimentiEnte,aUser,aTSnow);
	      CNRCTB200.CREASCRITTCOGE(aScritturaEnte,aListaMovimentiEnte);
      end if;
   else

-- Movimenti nel caso NON ci sia iva da versare

-- NEI CASI IN CUI IL SALDO FINALE NON PREVEDE UN SALDO A DEBITO (QUINDI CON IVA NON DA VERSARE
-- MA MAGARI DA PORTARE A CREDITO NEL PERIODO SUCCESSIVO),  OLTRE ALLE SCRITTURE CHE TRAVASANO
-- L'IVA A CREDITO E L'IVA A DEBITO NELL'ERARIO C/IVA (GIA' FATTE PRIMA),
-- SI DEVONO FARE ANCHE QUELLE CHE RILEVANO L'EVENTUALE COSTO PER IVA INDETRAIBILE

-- NON SI FA LA CHIUSURA DEL CONTO ERARIO SUL CONTO DEBITI VERSO ERARIO PER LA PARTE VERSATA PERCHE'
-- NON C'E' VERSAMENTO !!!

-- fa la registrazione con in AVERE l'Erario C/IVA per stornare la parte indetraibile (PRO RATA)

    If aLiqIva.iva_credito - aLiqIva.iva_credito_no_prorata < 0 then
Dbms_Output.PUT_LINE ('PREPARA MOVIMENTI 5');
     CNRCTB204.buildMovPrinc(aScrittura.cd_cds,aScrittura.esercizio,aScrittura.cd_unita_organizzativa,
                             aVoceEarioCIVA,
                             Abs(aLiqIva.iva_credito - aLiqIva.iva_credito_no_prorata),
                             CNRCTB200.IS_AVERE, nvl(DaData,aDtDa),nvl(AData,aDtA), aCdTerzo,
                             IST_COMM_LIQ,--CNRCTB100.TI_ISTITUZIONALE,
                             aListaMovimenti,aUser,aTSnow);

     CNRCTB204.buildMovPrinc(aScrittura.cd_cds,aScrittura.esercizio,aScrittura.cd_unita_organizzativa,
                             aVoceCostoIvaNonDetraibile,
                             Abs(aLiqIva.iva_credito - aLiqIva.iva_credito_no_prorata),
                             CNRCTB200.IS_DARE,nvl(DaData,aDtDa),nvl(AData,aDtA),aCdTerzo,
                             IST_COMM_LIQ,--CNRCTB100.TI_ISTITUZIONALE,
                             aListaMovimenti,aUser,aTSnow);
    End If;

   end if;
  end if;
Dbms_Output.PUT_LINE ('INS SCRITTURA 3');

  CNRCTB200.CREASCRITTCOGE(aScrittura,aListaMovimenti);
 end;

Procedure regDocAmmCOGE(aEs number, aCds varchar2, aUO varchar2, aTiDocumento varchar2, aPgDocAmm number, aUser varchar2, aTSNow date) is
  aDocTst V_DOC_AMM_COGE_TSTA%rowtype;

Begin
  select * into aDocTst from V_DOC_AMM_COGE_TSTA where
       cd_cds = aCds
   and cd_unita_organizzativa = aUO
   and esercizio = aEs
   and pg_numero_documento = aPgDocAmm
   and cd_tipo_documento = aTiDocumento;
  regDocAmmCOGE(aDocTst,aUser, aTSNow);
End;

 -- ===========================================
 -- Scrittura economica principale
 -- ===========================================

Procedure regDocAmmCOGE(aDocTst V_DOC_AMM_COGE_TSTA%rowtype, aUser varchar2, aTSNow date) is
  aScrittura scrittura_partita_doppia%rowtype;
  aMovimento movimento_coge%rowtype;
  aListaMovimenti CNRCTB200.movimentiList;
  aContoEp voce_ep%rowtype;
  aListaScritture CNRCTB200.scrittureList;
  aListaNuoveScritture CNRCTB200.scrittureList;
  aListaVecchieScritture CNRCTB200.scrittureList;
  aListaNuoviMovimenti CNRCTB200.movimentiList;
  aListaM CNRCTB200.movimentiList;
  isProcessaCOEP boolean;
  giorni_totali          NUMBER;
  giorni_es_corrente     NUMBER;
  frazione_es_corrente   NUMBER (15, 10);
  frazione_es_precedente NUMBER (15, 10);
  CONTO_USATO            voce_ep%rowtype;
  CUMULO_DARE            NUMBER (15,2);
  CUMULO_AVERE           NUMBER (15,2);
  aRecCompenso           COMPENSO%Rowtype;
  aCC_es_prec            chiusura_coep%Rowtype;

Begin
  -- Lock del documento
  CNRCTB100.LOCKDOCAMM(
   aDocTst.cd_tipo_documento,
   aDocTst.cd_cds,
   aDocTst.esercizio,
   aDocTst.cd_unita_organizzativa,
   aDocTst.pg_numero_documento);

Dbms_Output.PUT_LINE ('A');

  -- Gestione filtrodocumenti speciali che non vanno in COGE come prime scritture
  -- in attesa di porre tali documenti in stato coge escluso
If aDocTst.cd_tipo_documento in (CNRCTB100.TI_GENERICO_TRASF_E,
 		                 CNRCTB100.TI_GENERICO_TRASF_S,
			         CNRCTB100.TI_GEN_APERTURA_FONDO,
			         CNRCTB100.TI_GEN_IVA_ENTRATA) Or
   aDocTst.fl_associato_compenso = 'Y' Then -- La missione che transita per il compenso non va in economica
Dbms_Output.PUT_LINE ('A1');
   CNRCTB100.UPDATEDOCAMM_NODUVAUTUV(
    aDocTst.cd_tipo_documento,
    aDocTst.cd_cds,
    aDocTst.esercizio,
    aDocTst.cd_unita_organizzativa,
    aDocTst.pg_numero_documento,
    'stato_coge='''||CNRCTB100.STATO_COEP_EXC||'''',
    Null);
Dbms_Output.PUT_LINE ('B');
   return;
End if;

Dbms_Output.PUT_LINE ('B1');

  -- Se gi? processato in economica esco
If aDocTst.stato_coge in (CNRCTB100.STATO_COEP_CON, CNRCTB100.STATO_COEP_EXC)   then
Dbms_Output.PUT_LINE ('UO '||aDocTst.CD_UNITA_ORGANIZZATIVA||' NUM. DOC. '||aDocTst.PG_NUMERO_DOCUMENTO||' STATO COGE '||aDocTst.stato_coge);
   Return;
End If;

Dbms_Output.PUT_LINE ('C1');
If CNRCTB204.checkIsCogeDifferita(aDocTst) Then
   return;
End If;

  -- Fix del 20040924 Richiesta 843
Dbms_Output.PUT_LINE ('D1');
  CNRCTB204.checkChiusuraEsercizio(aDocTst.esercizio, aDocTst.cd_cds_origine);
Dbms_Output.PUT_LINE ('D2');
  -- L'esercizio economico precedente deve essere chiuso definitivamente a meno che non SI STIA FACENDO UNA SCRITTURA
  -- DI RATEO PARTE 1 - in questo caso la scrittura si PUO' FARE ed ? fatta nell'esercizio precedente a quello del documento
  -- che deve essere aperto
  -- CONSENTO ANCHE SE LA COMPETENZA E' A CAVALLO

aCC_es_prec := CNRCTB200.getChiusuraCoep(aDocTst.esercizio-1, aDocTst.cd_cds_origine);

-- NON POSSO INIZIARE A CONTABILIZZARE NELL'ESERCIZIO 2006 DOCUMENTI 2006 SE L'ESERCIZIO 2005 NON E' CHIUSO
-- A MENO CHE I DOCUMENTI NON ABBIANO COMPETENZA ANNO PRECEDENTE (QUINDI SOLO QUELLI PER I RATEI)

If aCC_es_prec.STATO = CNRCTB200.STATO_PROVA_ANNULLATA And -- se l'economica dell'esercizio precedente ? ancora aperta
-- MA LA COMPETENZA ECONOMICA NON E' IN ESERCIZIO PRECEDENTE
-- NON CONTABILIZZA NULLA
     Not ((CNRCTB204.getCompetenzaFuoriEsercizio(aDocTst) Or CNRCTB204.getCompetenzaacavalloconEsPrec(aDocTst)) And
                   CNRCTB008.ISESERCIZIOAPERTO(aDocTst.esercizio - 1,aDocTst.cd_cds_origine)) then
     IBMERR001.RAISE_ERR_GENERICO('L''esercizio economico precedente '||To_Char(aDocTst.esercizio-1)||' non ? chiuso definitivamente per il cds: '||aDocTst.cd_cds_origine);
--Elsif
-- 30.11.2006 PARTE NUOVA STANI
-- SE IL DOCUMENTO CON COMPETENZA (ANCHE PARZIALE IN ESERCIZIO PRECEDENTE) E' STATO CONTABILIZZATO CON RATEO ED E' ANCHE
-- STATA FATTA LA CHIUSURA OCCORRE REGISTRARE LA DIFFERENZA NEI CREDITI DEBITI

End If;


-- 27/02/2009 S.F. COMPENSI PER CONGUAGLI FISCALI CON COMPETENZA ESERCIZIO PRECEDENTE NON CONTABILIZZATI MA CON STATO MISTERIOSAMENTE A "C"

If aCC_es_prec.STATO = CNRCTB200.STATO_PROVA_ANNULLATA And -- ANCHE SE L'ECONOMICA DELL'ESERCIZIO PRECEDENTE ? ANCORA APERTA E
                                                           -- LA COMPETENZA ECONOMICA E' IN ESERCIZIO PRECEDENTE
                                                           -- SE SI TRATTA DI COMPENSI PER CONGUAGLI FISCALI NON DEVE CONTABILIZZARE NULLA
                                                           -- NELL'ANNO PRECEDENTE MA ATTENDERE L'ANNO SUCCESSIVO
   (CNRCTB204.getCompetenzaFuoriEsercizio(aDocTst) Or CNRCTB204.getCompetenzaacavalloconEsPrec(aDocTst)) And
     CNRCTB008.ISESERCIZIOAPERTO(aDocTst.esercizio - 1,aDocTst.cd_cds_origine) And
     aDocTst.cd_tipo_documento = 'COMPENSO' And aDocTst.IM_TOTALE_IMPONIBILE = 0 Then
     IBMERR001.RAISE_ERR_GENERICO('L''esercizio economico precedente '||To_Char(aDocTst.esercizio-1)||' non ? chiuso definitivamente per il cds: '||
                aDocTst.cd_cds_origine||' ma i compensi di conguaglio fiscale non producono Ratei.');
End If;




  -- Se il documento ? riportato anche parzialmente non pu? essere processato in economica
  -- 30.05.2006 NON E' PIU' VERO, ANCHE I DOCUMENTI RIPORTATI DEVONO ESSERE REGISTRATI IN ECONOMICA
/*
  If  CNRCTB105.isRiportato(aDocTst.cd_cds,aDocTst.cd_unita_organizzativa, aDocTst.esercizio,
	                    aDocTst.pg_numero_documento, aDocTst.cd_tipo_documento) = 'Y'  Then
   IBMERR001.RAISE_ERR_GENERICO('Il documento riportato (anche parzialmente) non pu? essere processato automaticamente in economica');
  end if;
*/

Dbms_Output.PUT_LINE ('Savepoint CNRCTB205_SP_001');
Savepoint CNRCTB205_SP_001;

-- cerca eventuali scritture gia registrate per il documento
-- se la competenza ? fuori esercizio e l'esercizio precedente ? aperto cerca i ratei parte 1

-- SE LA COMPETENZA E' COMPLETAMENTE FUORI ESERCIZIO/A CAVALLO ED IN PIU' L'ESERCIZIO PRECEDENTE E' APERTO

If (CNRCTB204.getCompetenzaFuoriEsercizio(aDocTst) Or  CNRCTB204.getCompetenzaacavalloconEsPrec(aDocTst)) And
    CNRCTB008.ISESERCIZIOAPERTOSenzaBlocco(aDocTst.esercizio - 1, aDocTst.cd_cds_origine) Then

  Dbms_Output.PUT_LINE ('fuori o a cavallo');

   If CNRCTB204.getCompetenzaFuoriEsercizio(aDocTst) Then

Dbms_Output.PUT_LINE ('fuori ');

   -- SE LA COMPETENZA E' COMPLETAMENTE FUORI ESERCIZIO

       CNRCTB204.getScritturePEPLock(aDocTst.esercizio - 1, aDocTst, CNRCTB200.CAU_RATEI, aListaScritture);
       -- Per ogni scrittura crea uno storno per poi riemettere la scrittura aggiornata
       -- QUI STORNA SOLO LA SCRITTURA GIA' FATTA TUTTA IN ANNO PRECEDENTE
       If aListaScritture.count > 0 Then
         For i in 1 .. aListaScritture.count loop
             CNRCTB200.getScritturaEPLOCK(aListaScritture(i),aListaM);
     	     aListaVecchieScritture(i):=aListaScritture(i);
     	     CNRCTB200.creaScrittStornoCoge(aListaScritture(i), aListaM, aUser, aTSNow);
         End Loop;
       End If;

   Elsif CNRCTB204.getCompetenzaacavalloconEsPrec(aDocTst) Then

   -- SE LA COMPETENZA E' A CAVALLO

Dbms_Output.PUT_LINE ('a cavallo ');

       CNRCTB204.getScritturePEPLock(aDocTst.esercizio - 1, aDocTst, CNRCTB200.CAU_RATEI, aListaScritture);
       -- Per ogni scrittura crea uno storno per poi riemettere la scrittura aggiornata
       -- QUI STORNA SOLO IL PEZZO DI SCRITTURA GIA' FATTA DELL'ANNO PRECEDENTE
       If aListaScritture.count > 0 Then
         For i in 1 .. aListaScritture.count loop
             CNRCTB200.getScritturaEPLOCK(aListaScritture(i),aListaM);
     	     aListaVecchieScritture(i):=aListaScritture(i);
     	     CNRCTB200.creaScrittStornoCoge(aListaScritture(i), aListaM, aUser, aTSNow);
         End loop;
       End If;

       CNRCTB204.getScritturePEPLock(aDocTst.esercizio, aDocTst, CNRCTB200.CAU_RATEI, aListaScritture);
       -- Per ogni scrittura crea uno storno per poi riemettere la scrittura aggiornata
       -- QUI STORNA SOLO IL PEZZO DI SCRITTURA GIA' FATTA DELL'ANNO IN CORSO
       If aListaScritture.count > 0 Then
         For i in 1 .. aListaScritture.count loop
             CNRCTB200.getScritturaEPLOCK(aListaScritture(i),aListaM);
     	     aListaVecchieScritture(i):=aListaScritture(i);
     	     CNRCTB200.creaScrittStornoCoge(aListaScritture(i), aListaM, aUser, aTSNow);
         End loop;
       End if;

   End If;

Else  -- COMPETENZA TUTTA IN ESERCIZIO
      -- (OPPURE COMPETENZA IN ESERCIZIO PRECEDENTE (O A CAVALLO) MA CON ESERCIZIO PRECEDENTE CHIUSO)

Dbms_Output.PUT_LINE ('DOCUMENTO CON COMP. ECON. IN ESERCIZIO O ESERCIZIO PRECEDENTE CHIUSO');

-- 12.12.2006 SF PARTE NUOVA: SE DEVO RICONTABILIZZARE UN DOCUMENTO AMMINISTRATIVO CHE AVEVA COMPETENZA IN ESERCIZIO PRECEDENTE,
--                            (QUINDI HA PRODOTTO RATEO CHE SI E' CHIUSO E RIAPERTO) DEVO PRIMA "STORNARE" LA SCRITTURA DI RATEO PARTE 2
--                            ALTRIMENTI IL DOCUMENTO VIENE RICONTABILIZZATO PER LA SECODNA VOLTA

-- INIZIO PARTE NUOVA

  Declare
      conto_ratei_attivi        voce_ep%Rowtype;
      conto_ratei_passivi       voce_ep%Rowtype;
      conto_fatt_emettere       voce_ep%Rowtype;
      conto_fatt_ricevere       voce_ep%Rowtype;

      conto_insuss_attive       voce_ep%Rowtype;
      conto_insuss_passive      voce_ep%Rowtype;

  Begin

   If CNRCTB204.getCompetenzaFuoriEsercizio(aDocTst) Then

     CNRCTB204.getScritturePEPLock(aDocTst.esercizio, aDocTst, CNRCTB200.CAU_RATEI_P2, aListaScritture);

     For i in 1 .. aListaScritture.count loop

         CNRCTB200.getScritturaEPLOCK(aListaScritture(i), aListaM);

         aListaScritture(i).ORIGINE_SCRITTURA := CNRCTB200.ORIGINE_MANUALE;
         aListaScritture(i).CD_CAUSALE_COGE         := Null;
         aListaScritture(i).ESERCIZIO_DOCUMENTO_AMM := Null;
         aListaScritture(i).CD_TIPO_DOCUMENTO       := Null;
         aListaScritture(i).PG_NUMERO_DOCUMENTO     := Null;
         aListaScritture(i).CD_CDS_DOCUMENTO        := Null;
         aListaScritture(i).CD_UO_DOCUMENTO         := Null;

         If To_Char(Sysdate, 'YYYY') < Nvl(aListaScritture(I).esercizio, aDocTst.esercizio) Then
           aListaScritture(i).DT_CONTABILIZZAZIONE := To_Date('0101'||aListaScritture(I).esercizio, 'DDMMYYYY');
         Elsif To_Char(Sysdate, 'YYYY') > Nvl(aListaScritture(I).esercizio, aDocTst.esercizio) Then
           aListaScritture(i).DT_CONTABILIZZAZIONE := To_Date('3112'||aListaScritture(I).esercizio, 'DDMMYYYY');
         Else
           aListaScritture(i).DT_CONTABILIZZAZIONE := Trunc(aTSNow);
         End If;

         aListaScritture(i).utcr := aUser;
         aListaScritture(i).DACR := aTSNow;

         For i in 1 .. aListaM.count loop
            Dbms_Output.PUT_LINE ('aListaM '||aListaM(i).cd_voce_ep);

            conto_ratei_attivi   := cnrctb002.getVoceEpRateiAttivi(aDocTst.esercizio);
            conto_ratei_passivi  := cnrctb002.getVoceEpRateiPassivi(aDocTst.esercizio);
            conto_fatt_emettere  := cnrctb002.getVoceEpFattureDaEmettere(aDocTst.esercizio);
            conto_fatt_ricevere  := cnrctb002.getVoceEpFattureDaRicevere(aDocTst.esercizio);

            conto_insuss_passive := cnrctb002.getVoceEpInsussPassive (aDocTst.esercizio);
            conto_insuss_attive  := cnrctb002.getVoceEpInsussAttive (aDocTst.esercizio);

-- IN DATA 26.06.2007 HO INVERTITO LA LETTURE DEI DUE CONTI (RICHIESTA DI FRANCA)
-- POTEVO CAMBIARE IL VALORE DEI PARAMETRI IN CONF_CNR MA NON SO SE ALTROVE HO GIA' CAMBIATO
-- LA LETTURA DEGLI STESSI (NON VORREI SCASSARE ALTRO)

            If aListaM(i).cd_voce_ep In (conto_ratei_attivi.cd_voce_ep, conto_fatt_emettere.cd_voce_ep) Then
                   aListaM(i).cd_voce_ep := conto_insuss_attive.cd_voce_ep;
            Elsif aListaM(i).cd_voce_ep In (conto_ratei_passivi.cd_voce_ep, conto_fatt_ricevere.cd_voce_ep) Then
                   aListaM(i).cd_voce_ep := conto_insuss_passive.cd_voce_ep;
            End If;

            aListaM(i).SEZIONE := CNRCTB200.getSezioneOpposta(aListaM(i).SEZIONE);
            aListaM(i).utcr := 'RICONT_RATEI';
            aListaM(i).DACR := aTSNow;

         End Loop;

   Dbms_Output.PUT_LINE ('chiamata a CNRCTB200.CREASCRITTCOGE');

Dbms_Output.PUT_LINE ('3 VALORE ATTUALE UTENTE '||aListaScritture(i).utcr);
     CNRCTB200.CREASCRITTCOGE(aDocTst.esercizio, aListaScritture(i), aListaM);

     End loop;

   Else

-- FINE PARTE NUOVA

   CNRCTB204.getScritturePEPLock(aDocTst, aListaScritture); -- LA CERCA CON CAUSALE_COGE A NULL E NON TROVA I RATEI
   Dbms_Output.PUT_LINE ('SCRITT RECUPERATE '||aListaScritture.count);
-- Per ogni scrittura crea uno storno (AL FINE poi riemettere la scrittura aggiornata)
   -- QUI STORNA SOLO LA SCRITTURA GIA' FATTA DELL'ANNO IN CORSO
   If aListaScritture.count > 0 Then
    For i in 1 .. aListaScritture.count loop
        CNRCTB200.getScritturaEPLOCK(aListaScritture(i),aListaM);
 	aListaVecchieScritture(i):=aListaScritture(i);
 	CNRCTB200.creaScrittStornoCoge(aListaScritture(i), aListaM, aUser, aTSNow);
    End loop;
   End if;

 End If; -- MIO AGGIUNTO
End;

End If;  -- FINE COMPETENZA IN ESERCIZIO/FUORI ESERCIZIO/A CAVALLO

-- Se il documento ? stato annullato e la scrittura non ? di rateo la contabilizzazione COGE si ferma qui
Dbms_Output.PUT_LINE (' stornato tutto riprendo');

If aDocTst.stato_cofi = CNRCTB100.STATO_GEN_COFI_ANN Then
   CNRCTB100.UPDATEDOCAMM_NODUVAUTUV(
    aDocTst.cd_tipo_documento,
    aDocTst.cd_cds,
    aDocTst.esercizio,
    aDocTst.cd_unita_organizzativa,
    aDocTst.pg_numero_documento,
    'stato_coge='''||CNRCTB100.STATO_COEP_CON||'''',
    Null);
Dbms_Output.PUT_LINE ('F');
   return;
End If;

  -- Se il documento e composto di piu di un'entita anagrafica, vengono create tante scritture quante sono le
  -- entita anagrafiche specificate
Dbms_Output.PUT_LINE ('inizio loop sui terzi di V_DOC_AMM_COGE_RIGA');
For aCDoc In (Select distinct cd_terzo,cd_elemento_voce_ev
              From V_DOC_AMM_COGE_RIGA
              Where  cd_cds = aDocTst.cd_cds
                 And cd_unita_organizzativa = aDocTst.cd_unita_organizzativa
                 And esercizio = aDocTst.esercizio
    	         And pg_numero_documento = aDocTst.pg_numero_documento
    	         And cd_tipo_documento = aDocTst.cd_tipo_documento) Loop

   -- Scrittura su partita normale

   aListaMovimenti.delete;
Dbms_Output.PUT_LINE ('inizio loop sui V_DOC_AMM_COGE_RIGA per terzo '||Acdoc.cd_terzo||' '||acdoc.cd_elemento_voce_ev);
   for aDoc in (select * from V_DOC_AMM_COGE_RIGA
                Where   cd_cds = aDocTst.cd_cds
 		    and cd_unita_organizzativa = aDocTst.cd_unita_organizzativa
 		    and esercizio = aDocTst.esercizio
 		    and pg_numero_documento = aDocTst.pg_numero_documento
 		    And cd_tipo_documento = aDocTst.cd_tipo_documento
 	            and cd_terzo = aCDoc.cd_terzo
 	            and (cd_elemento_voce_ev = aCDoc.cd_elemento_voce_ev ) -- COmpenso senza calcolo iva
		    and fl_pgiro = 'N') loop
Dbms_Output.PUT_LINE ('K '||aListaMovimenti.COUNT);

    buildMovPEP(aListaMovimenti, aDocTst, aDoc, aUser, aTSNow);

   end loop;

Dbms_Output.PUT_LINE ('K esco ');
Dbms_Output.PUT_LINE ('giro su cori '||aCDoc.cd_elemento_voce_ev||' ENTE ');
   for aDocCori in (select * from V_DOC_AMM_COGE_CORI
                    Where cd_cds = aDocTst.cd_cds
 		      and cd_unita_organizzativa = aDocTst.cd_unita_organizzativa
 		      and esercizio = aDocTst.esercizio
 		      and pg_numero_documento = aDocTst.pg_numero_documento
 		      and cd_tipo_documento = aDocTst.cd_tipo_documento
 	        and cd_terzo = aCDoc.cd_terzo
 	             -- and cd_elemento_voce_ev = aCDoc.cd_elemento_voce_ev
		      --and ti_ente_percepiente = TI_CORI_ENTE
 		   ) loop
Dbms_Output.PUT_LINE ('giro su cori '||aDocCori.AMMONTARE||' righe '||aListaMovimenti.count);
    buildMovPEP(aListaNuoveScritture, aListaMovimenti,aDocTst, aDocCori, aUser, aTSNow);
Dbms_Output.PUT_LINE ('dopo giro su cori '||aDocCori.AMMONTARE||' righe '||aListaMovimenti.count);
   end loop;

-- NEW !!!! 14/06/2006
-- Solo per documenti COMMERCIALI:
-- Generazione del movimento contemporaneo di alimentazione dell'IVA a Credito e
-- dell'IVA a Debito per le fatture che generano autofattura (qualsiasi sia la specie)

   If aDocTst.TI_ISTITUZ_COMMERC = CNRCTB100.TI_COMMERCIALE Then
Dbms_Output.PUT_LINE ('commerciale');
      For aDocCoriIVAAutofattura in (Select * from V_DOC_AMM_COGE_CORI_AUTOFATT
                                     Where cd_cds = aDocTst.cd_cds And
                                           cd_unita_organizzativa = aDocTst.cd_unita_organizzativa And
                                           esercizio = aDocTst.esercizio And
                                           pg_numero_documento = aDocTst.pg_numero_documento And
                                           cd_tipo_documento = aDocTst.cd_tipo_documento And
                                           cd_terzo = aCDoc.cd_terzo
                                           and cd_elemento_voce_ev = aCDoc.cd_elemento_voce_ev) Loop
Dbms_Output.PUT_LINE ('buildMovPEPAutofattura');
         buildMovPEPAutofattura(aListaNuoveScritture, aListaMovimenti, aDocTst, aDocCoriIVAAutofattura, aUser, aTSNow);
      End loop;

   End If;

-- FINE NEW !!!! 14/06/2006


   -- Movimenti su partita di giro
   For aDoc in (select * from V_DOC_AMM_COGE_RIGA
                Where cd_cds = aDocTst.cd_cds
 		  And cd_unita_organizzativa = aDocTst.cd_unita_organizzativa
 		  And esercizio = aDocTst.esercizio
 		  And pg_numero_documento = aDocTst.pg_numero_documento
 		  and cd_tipo_documento = aDocTst.cd_tipo_documento
 	          and cd_terzo = aCDoc.cd_terzo
 	          and (cd_elemento_voce_ev = aCDoc.cd_elemento_voce_ev) -- COmpenso senza calcolo o da conguaglio  potrebbe non avere il dato
		  And fl_pgiro = 'Y') loop
Dbms_Output.PUT_LINE ('2222');
	 Declare
              aSezContrPgiro char(1);
              conta number:=0;
 	 Begin
 	  -- La sezione ? quella principale del documento
	        aSezContrPgiro:=CNRCTB204.getSezione(aDocTst, aDoc);
Dbms_Output.PUT_LINE ('3333');
		if (nvl(aDoc.stato_coge_docamm,' ') = CNRCTB100.STATO_COEP_EXC) then
				Dbms_Output.put_line ('ROSPUC ESCO SENZA CONTABILIZZARE RIGA pgiro');
        return;
      end if;
 		if ( aDocTst.cd_tipo_documento in(CNRCTB100.TI_GENERICO_SPESA,CNRCTB100.TI_GENERICO_ENTRATA) and aDoc.FL_PGIRO = 'Y' ) then
  			if (aDocTst.cd_tipo_documento = CNRCTB100.TI_GENERICO_SPESA ) then
  			  begin
  				select 1 into conta from ass_obb_acr_pgiro
  				where
						CD_CDS = aDoc.CD_CDS_DOC   and
						ESERCIZIO = aDoc.ESERCIZIO_DOC   and
						PG_OBBLIGAZIONE = aDoc.PG_DOC   and
						ESERCIZIO_ORI_OBBLIGAZIONE = aDoc.ESERCIZIO_ORI_DOC  and
						TI_ORIGINE = 'S';
				exception when no_data_found then
				   dbms_output.put_line('Origine PGIRO E');
					CNRCTB100.UPDATEDOCAMM_NODUVAUTUV(
				    aDocTst.cd_tipo_documento,
				    aDocTst.cd_cds,
				    aDocTst.esercizio,
				    aDocTst.cd_unita_organizzativa,
				    aDocTst.pg_numero_documento,
				    'stato_coge='''||CNRCTB100.STATO_COEP_EXC||'''',
				    null);
				  return;
				end;
				elsif (aDocTst.cd_tipo_documento = CNRCTB100.TI_GENERICO_ENTRATA ) then
  			  begin
  				select 1 into conta from ass_obb_acr_pgiro
  				where
						CD_CDS = aDoc.CD_CDS_DOC   and
						ESERCIZIO = aDoc.ESERCIZIO_DOC   and
						PG_ACCERTAMENTO = aDoc.PG_DOC   and
						ESERCIZIO_ORI_ACCERTAMENTO = aDoc.ESERCIZIO_ORI_DOC  and
						TI_ORIGINE = 'E';
				exception when no_data_found then
				   dbms_output.put_line('Origine PGIRO S');
					CNRCTB100.UPDATEDOCAMM_NODUVAUTUV(
				    aDocTst.cd_tipo_documento,
				    aDocTst.cd_cds,
				    aDocTst.esercizio,
				    aDocTst.cd_unita_organizzativa,
				    aDocTst.pg_numero_documento,
				    'stato_coge='''||CNRCTB100.STATO_COEP_EXC||'''',
				    null
				   	);
				  return;
				end;
				end if;
	end if;

                CNRCTB204.buildMovPrinc(aDocTst.cd_cds_origine, aDocTst.esercizio,
                                        aDocTst.cd_uo_origine, CNRCTB204.getContoEpPgiro(aDocTst,aDoc),
                                        aDoc.im_imponibile, aSezContrPgiro, aDoc.dt_da_competenza_coge,
                                        aDoc.dt_a_competenza_coge,aDoc.cd_terzo,aDoc.ti_istituz_commerc,
                                        aListaMovimenti,aUser,aTSnow);
  	 Exception when NO_DATA_FOUND then
Dbms_Output.PUT_LINE ('4444');
                null;
 	 End;

   End Loop;
buildMovContrPEP (aListaMovimenti, aDocTst, aCDoc.cd_terzo, aUser, aTSNow,aCDoc.cd_elemento_voce_ev);
--Dbms_Output.PUT_LINE ('CHIAMATA A buildMovContrPEP da: '||aListaMovimenti(1).dt_da_competenza_coge||' a: '||aListaMovimenti(1).dt_a_competenza_coge);
-- rospuc 2017 spostato per avere aDoc da verificare
--buildMovContrPEP (aListaMovimenti, aDocTst, aCDoc.cd_terzo, aUser, aTSNow,aDoc);

aScrittura := CNRCTB204.buildScrPEP(aDocTst, aCDoc.cd_terzo, aUser, aTSNow);

  -- Nel caso la competenza sia in es. precedente (o a cavallo) e l'esercizio precedente sia aperto, sposta come rateo
  -- la scrittura appena fatta in es. precedente (la relativa frazione) ed emette la scrittura

If (CNRCTB204.getCompetenzaFuoriEsercizio(aDocTst) Or CNRCTB204.getCompetenzaacavalloconEsPrec(aDocTst)) And
    CNRCTB008.ISESERCIZIOAPERTOSenzaBlocco(aDocTst.esercizio - 1, aDocTst.cd_cds_origine) Then

   -- prima di chiamare la procedura che inserisce devo modificare gli importi del cursore
   -- moltiplicandoli per la frazione relativa alla quota dell'esercizio

   If CNRCTB204.getCompetenzaacavalloconEsPrec(aDocTst) Then -- SE E' A CAVALLO TRA I DUE ESERCIZI

         -- calcola le proporzioni

            giorni_totali := Trunc(aDocTst.dt_a_competenza_coge) - Trunc(aDocTst.dt_da_competenza_coge) + 1;
            giorni_es_corrente := Trunc(aDocTst.dt_a_competenza_coge) - to_date('0101'||aDocTst.esercizio,'DDMMYYYY') + 1;
            frazione_es_precedente := (giorni_totali - giorni_es_corrente) / giorni_totali;

        --  crea la scrittura normale nell'esercizio attuale con gli importi totali

            aScrittura.dt_contabilizzazione := aDocTst.dt_da_competenza_coge;

            For i in 1 .. aListaMovimenti.count loop
               If aListaMovimenti(i).dt_da_competenza_coge Is Not Null Then
                   aListaMovimenti(i).dt_da_competenza_coge := to_date('0101'||aDocTst.esercizio, 'DDMMYYYY');
               End If;
               If aListaMovimenti(i).dt_a_competenza_coge Is Not Null Then
                   aListaMovimenti(i).dt_a_competenza_coge  := aDocTst.dt_a_competenza_coge;
               End If;
            End Loop;

            aScrittura.cd_causale_coge := Null; -- LA SCRITURA TOTALE NON HA CAUSALE
            CNRCTB200.CREASCRITTCOGE(aDocTst.esercizio, aScrittura, aListaMovimenti);

-------------------- fine scrittura esercizio in corso

----------------- DOPODICH? CREA LA SCRITTURA DI SOLO RATEO NELL'ESERCIZIO PRECEDENTE

            aScrittura.dt_contabilizzazione := To_Date('3112'||(aDocTst.esercizio-1), 'DDMMYYYY');

            CUMULO_DARE  := 0;
            CUMULO_AVERE := 0;

            For i in 1 .. aListaMovimenti.count loop

               aListaMovimenti(i).im_movimento := Trunc((aListaMovimenti(i).im_movimento * frazione_es_precedente), 2);

-- PER QUADRATURA FINALE

                If aListaMovimenti(i).SEZIONE = CNRCTB200.IS_DARE Then
                        CUMULO_DARE := CUMULO_DARE + aListaMovimenti(i).im_movimento;
                Elsif aListaMovimenti(i).SEZIONE = CNRCTB200.IS_AVERE Then
                        CUMULO_AVERE := CUMULO_AVERE + aListaMovimenti(i).im_movimento;
                End If;

                If CUMULO_DARE != CUMULO_AVERE And Abs(CUMULO_DARE - CUMULO_AVERE) = 0.01 And
                        i = aListaMovimenti.COUNT Then
                  If CUMULO_DARE - CUMULO_AVERE > 0 Then
                    If aListaMovimenti(i).SEZIONE = CNRCTB200.IS_DARE Then
                        aListaMovimenti(i).im_movimento := aListaMovimenti(i).im_movimento - 0.01;
                    Else
                        aListaMovimenti(i).im_movimento := aListaMovimenti(i).im_movimento + 0.01;
                    End If;
                  Elsif CUMULO_AVERE - CUMULO_DARE > 0 Then
                    If aListaMovimenti(i).SEZIONE = CNRCTB200.IS_AVERE Then
                        aListaMovimenti(i).im_movimento := aListaMovimenti(i).im_movimento - 0.01;
                    Else
                        aListaMovimenti(i).im_movimento := aListaMovimenti(i).im_movimento + 0.01;
                    End If;
                  End If;
                End If;

               If aListaMovimenti(i).dt_da_competenza_coge Is Not Null Then
                        aListaMovimenti(i).dt_da_competenza_coge := aDocTst.dt_da_competenza_coge;
               End If;
               If aListaMovimenti(i).dt_a_competenza_coge Is Not Null Then
                        aListaMovimenti(i).dt_a_competenza_coge  := to_date('3112'||aDocTst.esercizio-1, 'DDMMYYYY');
               End If;

               -- 20.06.2006 MAIL FRANCA CAMPANALE
               -- SE IL COMPENSO E' COL FLAG GENERATA FATTURA IL CONTO DEVE ESSERE FATTURE DA RICEVERE
               -- Testo Mail: "i compensi derivanti da fattura (flag "generata fattura") con competenza esercizio
               -- precedente devono valorizzare il conto fatture da ricevere e non ratei"

               If aDocTst.cd_tipo_documento = CNRCTB100.TI_COMPENSO Then
                aRecCompenso := CNRCTB545.getCompenso(aDocTst.cd_cds,
                                                      aDocTst.cd_unita_organizzativa,
                                                      aDocTst.esercizio,
                                                      aDocTst.PG_NUMERO_DOCUMENTO,
                                                      'N' /* LOCK S/N */);
               End If;

               -- per la fattura attiva estrae il conto delle fatture da emettere,
               -- pe la passiva fatture da ricevere, altrimenti ratei
               if aDocTst.cd_tipo_documento = CNRCTB100.TI_FATTURA_ATTIVA Then
                 aContoEP := CNRCTB002.GETVOCEEPFATTUREDAEMETTERE(aDocTst.esercizio);
               elsif aDocTst.cd_tipo_documento = CNRCTB100.TI_FATTURA_PASSIVA Or
                    (aDocTst.cd_tipo_documento = CNRCTB100.TI_COMPENSO And aRecCompenso.FL_GENERATA_FATTURA = 'Y') Then
                 aContoEP := CNRCTB002.GETVOCEEPFATTUREDARICEVERE(aDocTst.esercizio);
               else
        	  if CNRCTB100.GETSEZIONEECONOMICA(aDocTst.cd_tipo_documento) = CNRCTB200.IS_DARE then
        	      aContoEP := CNRCTB002.GETVOCEEPRATEIPASSIVI(aDocTst.esercizio);
        	  else
        	      aContoEP := CNRCTB002.GETVOCEEPRATEIATTIVI(aDocTst.esercizio);
        	  end if;
               end if;

               CONTO_USATO := CNRCTB002.GETVOCEEP(aListaMovimenti(i).ESERCIZIO, aListaMovimenti(i).cd_voce_ep);

               If (Upper(trim(CONTO_USATO.natura_voce)) Not In ('EEC', 'EER') or( CONTO_USATO.natura_voce is null and CONTO_USATO.RIEPILOGA_A !='CEC')) Then
                        aListaMovimenti(i).cd_voce_ep := aContoEP.cd_voce_ep;
               End If;

            End loop;

            aScrittura.cd_causale_coge := CNRCTB200.CAU_RATEI_QUOTA;  -- LA QUOTA DELL'ANNO PRIMA E' RATEO
            CNRCTB200.CREASCRITTCOGE(aDocTst.esercizio - 1, aScrittura, aListaMovimenti);

------------ E ROVESCIAMENTO DELLA SCRITTURA PER CHIUDERE IL RATEO COL COSTO NELL'ANNO NUOVO

            aScrittura.dt_contabilizzazione := to_date('0101'||aDocTst.esercizio, 'DDMMYYYY');

            For i in 1 .. aListaMovimenti.count loop
               If aListaMovimenti(i).dt_da_competenza_coge Is Not Null Then
                   aListaMovimenti(i).dt_da_competenza_coge := to_date('0101'||aDocTst.esercizio, 'DDMMYYYY');
               End If;
               If aListaMovimenti(i).dt_a_competenza_coge Is Not Null Then
                        aListaMovimenti(i).dt_a_competenza_coge  := to_date('0101'||aDocTst.esercizio, 'DDMMYYYY');
               End If;

               If aListaMovimenti(i).SEZIONE = CNRCTB200.IS_DARE Then
                   aListaMovimenti(i).SEZIONE := CNRCTB200.IS_AVERE;
               Elsif aListaMovimenti(i).SEZIONE = CNRCTB200.IS_AVERE Then
                   aListaMovimenti(i).SEZIONE := CNRCTB200.IS_DARE;
               End If;

            End Loop;

            aScrittura.cd_causale_coge := CNRCTB200.CAU_RATEI_QUOTA_P2; -- IL ROVESCIAMENTO E' RATEO QUOTA PARTE 2
            CNRCTB200.CREASCRITTCOGE(aDocTst.esercizio, aScrittura, aListaMovimenti);

------------------ fine rateo + SUO ROVESCIAMENTO

   Else  --  SE NON E' A CAVALLO, QUINDI TOTALMENTE FUORI ESERCIZIO
            -- SF 17.09.2007, modificata la data della scrittura, rimaneva quella del documento amministrativo
            --                cio? quello dell'anno dopo (p.e. contabilizzazione 2006 con data 2007)
            aScrittura.dt_contabilizzazione := aDocTst.dt_a_competenza_coge;
            aScrittura.cd_causale_coge := CNRCTB200.CAU_RATEI;  -- SE LA COMPETENZA E' TOTALMENTE ANNO PRECEDENTE E' RATEO
                                                                -- CHE SI CHIUDE CON LA CHIUSURA
            CNRCTB200.CREASCRITTCOGE(aDocTst.esercizio - 1, aScrittura, aListaMovimenti);
   End If;

/* fine */

Else -- SE E' ALL'INTERNO DELL'ESERCIZIO
         CNRCTB200.CREASCRITTCOGE(aScrittura, aListaMovimenti);
End If;

   if aListaMovimenti.count > 0 then
    aListaNuoveScritture(aListaNuoveScritture.count+1):=aScrittura;
   end if;

  end loop;

  -- Costruisce le scritture di parte 2 nel caso di competenza in es. prec.

  if not CNRCTB200.isModificata(aListaVecchieScritture, aListaNuoveScritture) then
Dbms_Output.PUT_LINE ('rollback to savepoint CNRCTB205_SP_001');
   rollback to savepoint CNRCTB205_SP_001;
  end if;

  <<fine>>

  CNRCTB100.UPDATEDOCAMM_NODUVAUTUV(
   aDocTst.cd_tipo_documento,
   aDocTst.cd_cds,
   aDocTst.esercizio,
   aDocTst.cd_unita_organizzativa,
   aDocTst.pg_numero_documento,
   'stato_coge='''||CNRCTB100.STATO_COEP_CON||'''',
   null
  );
 end;

 -- =======================================
 --
 -- CONTABILIZZAZIONE ULTIMA
 --
 -- =======================================

-- Componenti scrittura ultima o singola

 procedure buildMovUEP(aListaMovimenti IN OUT CNRCTB200.movimentiList,
                       aDocTst V_DOC_ULT_COGE_TSTA%rowtype,
                       aDoc V_DOC_ULT_COGE_RIGA%rowtype,
                       aTiIstituzCommerc IN OUT char,
                       aUser varchar2,
                       aTSnow date) is
  aVoceEp voce_ep%rowtype;
  aNum number;
  aUOENTE unita_organizzativa%rowtype;
  aDocTstPrimo V_DOC_AMM_COGE_TSTA%rowtype;
  aDocTstPrimoBis V_DOC_AMM_COGE_TSTA%rowtype;
  aImportoMov number;
  fl_merce_intra  CHAR(1);
  fl_snr  CHAR(1);
 begin
--Dbms_Output.PUT_LINE ('A1');
-- rospuc prova spostato recupero trovaContoAnag
  aVoceEp:=CNRCTB204.trovaContoAnag(aDoc, aDoc.cd_terzo,aDoc.cd_elemento_voce);

  -- Modifica del 18/05/2004 err. 825 -> se la competenza ? in esercizio precedente e l'esercizio attuale ? il primo dell'applicazione
  -- Il conto di costo/ricavo ? SP Iniziale
  if
         aDoc.esercizio_doc = CNRCTB008.ESERCIZIO_PARTENZA
     and aDoc.cd_tipo_doc not in (CNRCTB100.TI_FATTURA_ATTIVA,CNRCTB100.TI_FATTURA_PASSIVA)
  then
   select * into aDocTstPrimo from V_DOC_AMM_COGE_TSTA where
           CD_TIPO_DOCUMENTO=aDoc.cd_tipo_doc
	   and CD_CDS=aDoc.cd_cds_doc
	   and CD_UNITA_ORGANIZZATIVA=aDoc.cd_uo_doc
	   and ESERCIZIO=aDoc.esercizio_doc
	   and PG_NUMERO_DOCUMENTO=aDoc.pg_numero_doc;
   if CNRCTB204.getCompetenzaFuoriEsercizio(aDocTstPrimo) then
	if CNRCTB100.GETSEZIONEECONOMICA(aDocTstPrimo.cd_tipo_documento) = CNRCTB100.IS_DARE then
     aVoceEP:=CNRCTB002.GETVOCEEPDEBITIINIZIALI(aDocTstPrimo.esercizio);
    else
     aVoceEP:=CNRCTB002.GETVOCEEPCREDITIINIZIALI(aDocTstPrimo.esercizio);
    end if;
   end if;
  end if;

  -- Modifica del 26/02/2004
  -- Gestione della reversale di accantonamento IVA al centro
  -- Nel caso si tratti di revrsale di accantonamento iva al centro, il conto da utilizzare
  -- al posto di quello creditorio, ? il conto erario/IVA
--Dbms_Output.PUT_LINE ('A');
  If aDocTst.cd_tipo_documento_cont = CNRCTB018.TI_DOC_REV And aDoc.cd_tipo_doc = CNRCTB100.TI_GEN_CORI_ACC_ENTRATA Then
   -- Verifico se si tratta di reversale di accantonamento IVA al centro
Dbms_Output.PUT_LINE ('B');
   begin
    select distinct 1 into aNum from reversale_riga rr where
	    esercizio=aDocTst.esercizio
    and cd_cds=aDocTst.cd_cds
	and pg_reversale=aDocTst.pg_documento_cont
	and exists (
	 select 1 from ass_obb_acr_pgiro aoa where
	          cd_cds = rr.cd_cds
		  and esercizio = rr.esercizio
		  and esercizio_ori_accertamento = rr.esercizio_ori_accertamento
		  and pg_accertamento = rr.pg_accertamento
		  and ti_origine = CNRCTB001.GESTIONE_ENTRATE
		  and exists (select 1 from liquidazione_iva_centro where
		       CD_CDS_OBB_ACCENTR=aoa.cd_cds
		   and ESERCIZIO_OBB_ACCENTR=aoa.esercizio
		   and ESERCIZIO_ORI_OBB_ACCENTR=aoa.esercizio_ori_obbligazione
		   and PG_OBB_ACCENTR=aoa.pg_obbligazione
	 )
	);
	-- Nel caso sia reversale di accantonamento IVA, il conto da usare ? il patrimoniale Erario conto IVA
    aVoceEp:=CNRCTB002.GETVOCEEPERARIOCIVA(aDocTst.esercizio);
   exception when NO_DATA_FOUND then
    null;
   end;
  end if;

  -- Modifica del 22/04/2004
  -- Gestione del mandato di versamento IVA al centro
  -- Nel caso si tratti di mandato di versamento IVA al centro, il conto da utilizzare
  -- al posto di quello creditorio, ? il conto erario/IVA
--Dbms_Output.PUT_LINE ('C '||aDocTst.cd_tipo_documento_cont||' '||aDoc.cd_tipo_doc);
--Dbms_Output.PUT_LINE ('RISPETTO A '||CNRCTB018.TI_DOC_MAN||' '||CNRCTB100.TI_GEN_CORI_VER_SPESA);
  if
         aDocTst.cd_tipo_documento_cont = CNRCTB018.TI_DOC_MAN
	 and aDoc.cd_tipo_doc = CNRCTB100.TI_GEN_CORI_VER_SPESA
  then
    -- Verifico se si tratta di mandato di versamento IVA al centro
--Dbms_Output.PUT_LINE ('D');
   begin
     aUOENTE:=CNRCTB020.GETUOENTE(aDocTst.esercizio);
--Dbms_Output.PUT_LINE ('11 '||aUOENTE.cd_unita_padre||' '||aUOENTE.cd_unita_organizzativa||' '
--||aDocTst.esercizio||' '||aDocTst.cd_cds||' '||aDocTst.pg_documento_cont);

     select distinct 1 into aNum from mandato_riga mr where
	     esercizio=aDocTst.esercizio
      and cd_cds=aDocTst.cd_cds
	  and pg_mandato=aDocTst.pg_documento_cont
	  and exists (
	  select 1 from documento_generico dg where
	          esercizio = mr.esercizio_doc_amm
		  and cd_cds = mr.cd_cds_doc_amm
		  and cd_unita_organizzativa = mr.cd_uo_doc_amm
		  and pg_documento_generico = mr.pg_doc_amm
		  and cd_tipo_documento_amm = mr.cd_tipo_documento_amm
		  and exists (select 1 from liquidazione_iva where
		       CD_CDS=aUOENTE.cd_unita_padre
		   and CD_UNITA_ORGANIZZATIVA=aUOENTE.cd_unita_organizzativa
		   and STATO='D'
		   and REPORT_ID=0
		   and CD_TIPO_DOCUMENTO=dg.cd_tipo_documento_amm
           and CD_CDS_DOC_AMM=dg.cd_cds
           and CD_UO_DOC_AMM=dg.cd_unita_organizzativa
           and ESERCIZIO_DOC_AMM=dg.esercizio
           and PG_DOC_AMM=dg.pg_documento_generico
		  )
	);
	-- Nel caso sia mandato di versamento IVA al centro, il conto da usare ? il patrimoniale Erario conto IVA
Dbms_Output.PUT_LINE ('D1');
    aVoceEp:=CNRCTB002.GETVOCEEPERARIOCIVA(aDocTst.esercizio);
Dbms_Output.PUT_LINE ('D1 '||aVoceEp.CD_VOCE_EP);
   exception when NO_DATA_FOUND then
--Dbms_Output.PUT_LINE ('NIENTE');
    null;
   end;
  end if;
--Dbms_Output.PUT_LINE ('D2');
  -- Conto speciale per mandati di accreditamento
  if aDocTst.ti_man_rev = CNRCTB038.TI_MAN_ACCRED then
   aVoceEp:=CNRCTB002.getVoceEpBancaCds(aDocTst.esercizio);
  end if;

  -- Estrazione del numero di movimenti istituzionali/commerciali e promiscui
  aTiIstituzCommerc:=CNRCTB100.TI_ISTITUZIONALE;
  begin
   select nvl(ti_istituz_commerc,CNRCTB100.TI_ISTITUZIONALE)
   into aTiIstituzCommerc
   From v_doc_amm_coge_tsta
   Where cd_tipo_documento=aDoc.cd_tipo_doc
         and esercizio=aDoc.esercizio_doc
 	 and pg_numero_documento=aDoc.pg_numero_doc
 	 and cd_cds=aDoc.cd_cds_doc
 	 and cd_unita_organizzativa=aDoc.cd_uo_doc;
  exception when NO_DATA_FOUND then
   null;
  end;

  aImportoMov:=aDoc.im_lordo_doc;

  -- Modifica del 18/10/2004 rich. 844 -> per le fatture passive di tipo
  -- San Marino o intra ue che siano anche istituzionali di beni, bisogna
  -- utilizzare il netto invece del lordo del mandato

  If aDoc.cd_tipo_doc = CNRCTB100.TI_FATTURA_PASSIVA Then
   select *
   into aDocTstPrimoBis
   from V_DOC_AMM_COGE_TSTA
   Where   CD_TIPO_DOCUMENTO=aDoc.cd_tipo_doc
	   and CD_CDS=aDoc.cd_cds_doc
	   and CD_UNITA_ORGANIZZATIVA=aDoc.cd_uo_doc
	   and ESERCIZIO=aDoc.esercizio_doc
	   and PG_NUMERO_DOCUMENTO=aDoc.pg_numero_doc;

	select fl_servizi_non_residenti,fl_merce_intra_ue into fl_snr,fl_merce_intra
	from fattura_passiva, tipo_sezionale
    where
    fattura_passiva.cd_tipo_sezionale = tipo_sezionale.cd_tipo_sezionale and
    fattura_passiva.esercizio = aDoc.esercizio_doc and
    fattura_passiva.cd_cds = aDoc.cd_cds_doc and
    fattura_passiva.cd_unita_organizzativa = aDoc.cd_uo_doc and
    fattura_passiva.pg_fattura_passiva = aDoc.pg_numero_doc;

		If (aDocTstPrimoBis.FL_SAN_MARINO_SENZA_IVA = 'Y' Or
		    aDocTstPrimoBis.FL_INTRA_UE = 'Y' or
		    fl_merce_intra ='Y' ) And
		    aDocTstPrimoBis.TI_ISTITUZ_COMMERC = CNRCTB100.TI_ISTITUZIONALE And
		    aDocTstPrimoBis.TI_BENE_SERVIZIO = CNRCTB100.TI_FT_ACQ_BENI Then
		  aImportoMov:=aDoc.im_doc;
		End if;

		If  fl_snr ='Y'  And
		    aDocTstPrimoBis.TI_ISTITUZ_COMMERC = CNRCTB100.TI_ISTITUZIONALE And
		    aDocTstPrimoBis.TI_BENE_SERVIZIO = CNRCTB100.TI_FT_ACQ_SERVIZI Then
		  aImportoMov:=aDoc.im_doc;
		End if;
  end if;

-- SDOPPIO GLI IMPORTI NEL CASO IN CUI LA TESTATA DELLA FATTURA SIA PROMISCUA

If aTiIstituzCommerc = CNRCTB100.TI_PROMISCUO Then
dbms_output.put_line('Ros');
   Select Nvl(Sum(IM_IMPONIBILE), 0) --+ Nvl(Sum(IM_IVA), 0)
   into   aImportoMov
   From   v_doc_amm_coge_riga
   Where  cd_tipo_documento      = aDoc.cd_tipo_doc
      And esercizio              = aDoc.esercizio_doc
      And pg_numero_documento    = aDoc.pg_numero_doc
      And cd_cds                 = aDoc.cd_cds_doc
      And cd_unita_organizzativa = aDoc.cd_uo_doc
      And ti_istituz_commerc = CNRCTB100.TI_ISTITUZIONALE;

   If aImportoMov > 0 Then
dbms_output.put_line('Ros 2');
          CNRCTB204.buildMovPrinc(
           aDocTst.cd_cds_origine,
           aDocTst.esercizio,
           aDocTst.cd_uo_origine,
           aVoceEp,
           aImportoMov,
           CNRCTB204.getSezione(aDocTst),
           null,
           null,
           aDoc.cd_terzo,
           CNRCTB100.TI_ISTITUZIONALE,
           aListaMovimenti,
           aUser,
           aTSnow);
   End If;
dbms_output.put_line('Ros 3');
   Select Nvl(Sum(IM_IMPONIBILE), 0) + Nvl(Sum(IM_IVA), 0)
   into   aImportoMov
   From   v_doc_amm_coge_riga
   Where  cd_tipo_documento      = aDoc.cd_tipo_doc
      And esercizio              = aDoc.esercizio_doc
      And pg_numero_documento    = aDoc.pg_numero_doc
      And cd_cds                 = aDoc.cd_cds_doc
      And cd_unita_organizzativa = aDoc.cd_uo_doc
      And ti_istituz_commerc = CNRCTB100.TI_COMMERCIALE;
   If aImportoMov > 0 Then
dbms_output.put_line('Ros 4');
          CNRCTB204.buildMovPrinc(
           aDocTst.cd_cds_origine,
           aDocTst.esercizio,
           aDocTst.cd_uo_origine,
           aVoceEp,
           aImportoMov,
           CNRCTB204.getSezione(aDocTst),
           null,
           null,
           aDoc.cd_terzo,
           CNRCTB100.TI_COMMERCIALE,
           aListaMovimenti,
           aUser,
           aTSnow);
   End If;
dbms_output.put_line('Ros 5');
Else
Dbms_Output.PUT_LINE ('D11 '||aVoceEp.CD_VOCE_EP);
dbms_output.put_line('Ros 6');
  CNRCTB204.buildMovPrinc(
   aDocTst.cd_cds_origine,
   aDocTst.esercizio,
   aDocTst.cd_uo_origine,
   aVoceEp,
   aImportoMov,
   CNRCTB204.getSezione(aDocTst),
   null,
   null,
   aDoc.cd_terzo,
   aTiIstituzCommerc,
   aListaMovimenti,
   aUser,
   aTSnow
  );

  End If;

 end;

 procedure buildMovContrCoriUEP(aListaMovimenti IN OUT CNRCTB200.movimentiList, aDocTst V_DOC_ULT_COGE_TSTA%rowtype, aDocRiga V_DOC_ULT_COGE_RIGA%rowtype, aUser varchar2, aTSnow date) is
  aCompenso compenso%rowtype;
  aFatt fattura_passiva%rowtype;
  isEscluso boolean;
  aEffCori ass_tipo_cori_voce_ep%rowtype;
  aContoEp voce_ep%rowtype;
  aContoEpCoriEnte voce_ep%rowtype;
  aContoEpCoriEnteORI voce_ep%rowtype;
  totEnte number:=0;
  ev elemento_voce.cd_elemento_voce%typE;
 begin
  dbms_output.put_line('ci arrivo cori');

  if aDocRiga.cd_tipo_doc = CNRCTB100.TI_COMPENSO then
   for aCori in (select * from v_doc_amm_coge_cori a where
        esercizio = aDocRiga.esercizio_doc
    and cd_cds = aDocRiga.cd_cds_doc
    and cd_unita_organizzativa = aDocRiga.cd_uo_doc
    and pg_numero_documento = aDocRiga.pg_numero_doc
	and cd_tipo_documento = aDocRiga.cd_tipo_doc
   ) loop
    isEscluso:=false;
	declare
     aCoriLoc contributo_ritenuta%rowtype;
	 aCdClassificazioneCori char(2);
    begin
	  begin
	   select * into aCoriLoc from contributo_ritenuta where
	        cd_cds = aCori.cd_cds
	    and esercizio = aCori.esercizio
	    and cd_unita_organizzativa = aCori.cd_unita_organizzativa
	    and pg_compenso = aCori.pg_numero_documento
	    and cd_contributo_ritenuta = aCori.cd_contributo_ritenuta
	    and ti_ente_percipiente = aCori.ti_ente_percepiente;
      exception when NO_DATA_FOUND then
	   IBMERR001.RAISE_ERR_GENERICO('Contributo ritenuta non trovato:'||aCori.cd_contributo_ritenuta);
      end;
	  -- Se la tipologia di contributo ritenuta ? IVA o RIVALSA, non vengono esposte nell'ultima scrittura (per l'IVA indipendentemente dal fatto che sia commerciale o istituzionale)
      aCdClassificazioneCori:=CNRCTB545.getTipoCoriDaRigaCompenso(aCoriLoc);
      if (   aCdClassificazioneCori = CNRCTB545.isCoriIva
	          or aCdClassificazioneCori = CNRCTB545.isCoriRivalsa
		 ) then
	   isEscluso:=true;
      end if;
    end;
    if not isEscluso then
    			dbms_output.put_line('cori '||aCori.cd_contributo_ritenuta||' ti_e_p '||aCori.ti_ente_percepiente);
     			aEffCori:=CNRCTB204.getAssCoriEp(aDocRiga.esercizio,aCori.cd_contributo_ritenuta,aCori.ti_ente_percepiente,CNRCTB100.IS_DARE);
			     begin
				     if(aCori.ti_ente_percepiente = TI_CORI_ENTE) then
					  		aContoEpCoriEnte:=CNRCTB204.trovaContoContrEp(aEffCori.esercizio, NULL,NULL,NULL, aEffCori.cd_voce_EP);
					  	else
				  			aContoEpCoriEnte:=null;
				   	end if;

				   	 	aContoEp:=CNRCTB002.getVoceEp(aEffCori.esercizio, aEffCori.cd_voce_ep_contr);

				 exception when OTHERS then
			      IBMERR001.RAISE_ERR_GENERICO('Conto economico di contr.: '||aEffCori.cd_voce_ep_contr||' associato a CORI IVA non trovato in esercizio '||aEffCori.esercizio);
				 end;
     CNRCTB204.buildMovPrinc(
      aDocTst.cd_cds_origine,aDocTst.esercizio,aDocTst.cd_uo_origine,
      aContoEp,
      abs(aCori.ammontare),
      CNRCTB200.getSezioneOpposta(CNRCTB100.IS_DARE),
      null,
      null,
      aDocRiga.cd_terzo,
      ACORI.TI_ISTITUZ_COMMERC, /* STANI 04.11 AGGIUNTO PER AVERE SCRITTURE COMMERCIALI PER COMPENSI
                                   COMMERCIALI. NON PASSANDO NULLA ERA AUTOMATICAMENTE
                                   ISTITUZIONALE */
      aListaMovimenti,
 	  aUser,
 	  aTSnow
     );
      begin
						   select distinct cd_elemento_voce_ev into Ev
						       From V_DOC_AMM_COGE_RIGA
						              Where  cd_cds = aDocRiga.cd_cds
						                 And cd_unita_organizzativa = aDocRiga.cd_unita_organizzativa
						                 And esercizio = aDocRiga.esercizio_doc
						    	         And pg_numero_documento= aDocRiga.pg_numero_doc
						    	         And cd_tipo_documento= aDocRiga.cd_tipo_doc;
					   	exception when too_many_rows then
						     	dbms_output.put_line ('Tipo '||aDocRiga.cd_tipo_doc||'Uo '||aDocRiga.cd_unita_organizzativa||' doc '||aDocRiga.pg_numero_doc);
						     	when no_data_found then
						     	dbms_output.put_line ('Tipo '||aDocRiga.cd_tipo_doc||'Uo '||aDocRiga.cd_unita_organizzativa||' doc '||aDocRiga.pg_numero_doc);
							end;

     -- Rospuc 18/05/2017 scritture per rettificare i debiti considerando solo le ritenute a carico ente
     if(aContoEpCoriEnte.cd_voce_ep is not null and ev is not null) then

				      CNRCTB204.buildMovPrinc(
				      aDocTst.cd_cds_origine,aDocTst.esercizio,aDocTst.cd_uo_origine,
				      aContoEpCoriEnte,
				      abs(aCori.ammontare),
				      CNRCTB100.IS_DARE,
				      null,
				      null,
				      aDocRiga.cd_terzo,
				      ACORI.TI_ISTITUZ_COMMERC,
				      aListaMovimenti,
				 	  	aUser,
				 	  	aTSnow
				     );
				     aContoEpCoriEnteORI:=CNRCTB204.trovaContoAnag(aDocRiga,   aDocRiga.cd_terzo,ev);

				      CNRCTB204.buildMovPrinc(
				      aDocTst.cd_cds_origine,aDocTst.esercizio,aDocTst.cd_uo_origine,
				      aContoEpCoriEnteORI,
				      abs(aCori.ammontare),
				      CNRCTB200.getSezioneOpposta(CNRCTB100.IS_DARE),
				      null,
				      null,
				      aDocRiga.cd_terzo,
				      ACORI.TI_ISTITUZ_COMMERC,
				      aListaMovimenti,
				 	  aUser,
				 	  aTSnow
				     );
     end iF;
	--  Rospuc 18/05/2017 scritture per rettificare i debiti considerando solo le ritenute a carico ente
    end if;
   end loop;

  end if;
 end;

 procedure buildMovContrDefaultUEP(aListaMovimenti IN OUT CNRCTB200.movimentiList, aDocTst V_DOC_ULT_COGE_TSTA%rowtype, aDocRiga V_DOC_ULT_COGE_RIGA%rowtype, aTiIstituzCommerc char, aUser varchar2, aTSnow date) is
  aVoceEP voce_ep%rowtype:=null;
  aMovimento movimento_coge%rowtype;
 begin

  if aListaMovimenti.count = 0 then
   IBMERR001.RAISE_ERR_GENERICO('Nessun movimento da chiudere in contropartita');
  end if;
dbms_output.put_line ('aiu v '||aDocRiga.cd_elemento_voce);
  aVoceEP:=CNRCTB204.trovaContoContrEP(aDocTst, aDocRiga);
 dbms_output.put_line ('aiu '||aVoceEp.cd_voce_ep);
  -- Accumula i movimenti di controparte

  CNRCTB204.BUILDCHIUSURASCRITTURA(
     aListaMovimenti(1).cd_cds,aListaMovimenti(1).esercizio,aListaMovimenti(1).cd_unita_organizzativa,
     aVoceEP,
     null,
     null,
     aListaMovimenti(1).cd_terzo,
--	 CNRCTB200.TI_ISTITUZIONALE,
     aTiIstituzCommerc,
     aListaMovimenti,
 	 aUser,
 	 aTSnow
  );
 end;

 procedure buildMovContrUEP(aListaMovimenti IN OUT CNRCTB200.movimentiList,
                            aDocTst V_DOC_ULT_COGE_TSTA%rowtype,
                            aDocRiga V_DOC_ULT_COGE_RIGA%rowtype,
                            aTiIstituzCommerc char,
                            aUser varchar2,
                            aTSnow date) is
 begin
      -- Gestione movimenti contropartita compenso
      if
	        aDocRiga.cd_tipo_doc = CNRCTB100.TI_COMPENSO
	     or aDocRiga.cd_tipo_doc = CNRCTB100.TI_FATTURA_PASSIVA
	  then
       buildMovContrCoriUEP(aListaMovimenti, aDocTst, aDocRiga, aUser, aTSnow);
	  end if;
     -- Movimenti di default
	   buildMovContrDefaultUEP(aListaMovimenti, aDocTst, aDocRiga, aTiIstituzCommerc , aUser, aTSnow);
 end;

Procedure buildMovVersCoriUEP(aListaMovimenti IN OUT CNRCTB200.movimentiList, aDocTst V_DOC_ULT_COGE_TSTA%rowtype, aDoc V_DOC_ULT_COGE_RIGA%rowtype, aUser varchar2, aTSnow date) is
  aVoceEP voce_ep%rowtype;
  aAssCoriEp ass_tipo_cori_voce_ep%rowtype;
  aCoriLoc contributo_ritenuta%rowtype;
  isEscluso boolean;
  aGenRiga documento_generico_riga%rowtype;
  aAccScad accertamento_scadenzario%rowtype;
  aObbScad obbligazione_scadenzario%rowtype;
Begin

  -- Carico la riga di generico di versamento
  select * into aGenRiga from documento_generico_riga where
        esercizio = aDoc.esercizio
    and cd_cds = aDoc.cd_cds_doc
    and cd_unita_organizzativa = aDoc.cd_uo_doc
    and cd_tipo_documento_amm = aDoc.cd_tipo_doc
    and pg_documento_generico = aDoc.pg_numero_doc
    and progressivo_riga = 1 for update nowait;

Dbms_Output.PUT_LINE ('TROVA GENERICO '||aGenRiga.CD_CDS||' '||aGenRiga.CD_UNITA_ORGANIZZATIVA||' '||aGenRiga.ESERCIZIO||' '||
aGenRiga.CD_TIPO_DOCUMENTO_AMM||' '||aGenRiga.PG_DOCUMENTO_GENERICO||' '||aGenRiga.PROGRESSIVO_RIGA);

If aGenRiga.pg_obbligazione is not null Then
-- Carico l'accertamento collegato all'obbligazione su partita di giro
Dbms_Output.PUT_LINE ('C '||aGenRiga.esercizio_obbligazione||' '||aGenRiga.cd_cds_obbligazione||' '||aGenRiga.pg_obbligazione);
   Begin
            select *
            into  aAccScad
            from  accertamento_scadenzario
            Where cd_cds = aGenRiga.cd_cds_obbligazione And
                  esercizio = aGenRiga.esercizio_obbligazione And
                  (esercizio_originale, pg_accertamento) In
                    (select esercizio_ori_accertamento, pg_accertamento
                     from ass_obb_acr_pgiro
                     Where cd_cds = aGenRiga.cd_cds_obbligazione
			    And esercizio = aGenRiga.esercizio
			    And esercizio_ori_OBBLIGAZIONE = aGenRiga.esercizio_ori_obbligazione
			    And pg_obbligazione = aGenRiga.pg_obbligazione
			    --And ti_origine = CNRCTB001.GESTIONE_ENTRATE REMMATO IL 28.06.2006 STANI (RITENUTE STIPENDI)
			    ) And
                  pg_accertamento_scadenzario = 1;
   Exception when NO_DATA_FOUND Then
        Dbms_Output.PUT_LINE ('NO DATA AA');
    Return;
   End;

   Begin

Dbms_Output.PUT_LINE ('CERCO CORI LOC '||aAccScad.esercizio||' '||aAccScad.cd_cds||' '||aAccScad.pg_accertamento||' '||aAccScad.pg_accertamento_scadenzario);

            Select *
            Into  aCoriLoc
            From  contributo_ritenuta
            Where cd_cds_accertamento = aAccScad.cd_cds
	      And esercizio_accertamento = aAccScad.esercizio
	      And esercizio_ori_accertamento = aAccScad.esercizio_originale
 	      And pg_accertamento = aAccScad.pg_accertamento
	      And pg_accertamento_scadenzario = aAccScad.pg_accertamento_scadenzario;

   -- Se l'accertamento di contropartita che ha aperto la partita di giro non ?
   -- collegato a CORI di compenso, esce
   -- (modifica del 13/07/2005 => ... dopo aver controllato anche che l'accertamento di origine

 exception when NO_DATA_FOUND Then

    -- RICERCA L'ACCERTAMENTO DELL'ESERCIZIO PRECEDENTE

    Declare
        CDS_ES_PREC     VARCHAR(20);
        ES_PREC         NUMBER;
        ES_ORI_PREC     NUMBER;
        PG_ACC_ES_PREC  NUMBER;
    Begin
			Dbms_Output.PUT_LINE ('NO DATA ACC '||aAccScad.cd_cds||' '||aAccScad.esercizio||' '||aAccScad.pg_accertamento);
      Select CD_CDS_ORI_RIPORTO, ESERCIZIO_ORI_RIPORTO, ESERCIZIO_ORI_ORI_RIPORTO, PG_ACCERTAMENTO_ORI_RIPORTO
      Into   CDS_ES_PREC, ES_PREC, ES_ORI_PREC, PG_ACC_ES_PREC
      From   ACCERTAMENTO
      Where  CD_CDS          = aAccScad.cd_cds    And
             ESERCIZIO       = aAccScad.esercizio And
             ESERCIZIO_ORIGINALE = aAccScad.esercizio_originale And
             PG_ACCERTAMENTO = aAccScad.pg_accertamento;

			Dbms_Output.PUT_LINE ('NO DATA CORI LOC '||ES_PREC||' '||CDS_ES_PREC||' '||PG_ACC_ES_PREC);

       select * into aCoriLoc from contributo_ritenuta
       Where  cd_cds_accertamento    = CDS_ES_PREC
          and esercizio_accertamento = ES_PREC
          and esercizio_ori_accertamento = ES_ORI_PREC
          and pg_accertamento        = PG_ACC_ES_PREC
          and pg_accertamento_scadenzario = 1;

    Exception
      When No_Data_Found Then
			 			Begin
			 			if(recParametriCnr.fl_tesoreria_unica ='N') then
 						select a.*
		            into  aAccScad
		            from  LIQUID_CORI_ASS_PGIRO l,ass_obb_acr_pgiro asS,accertamento_scadenzario a
		                  Where
		                     	l.ESERCIZIO_PGIRO_NEW = aGenRiga.esercizio_obbligazione
						  				and l.CD_CDS_PGIRO_NEW  =aGenRiga.cd_cds_obbligazione
											and l.PG_PGIRO_NEW = aGenRiga.pg_obbligazione
									   	and ESERCIZIO_ORI_PGIRO_NEW  =aGenRiga.esercizio_ori_obbligazione
									   	and tipo_Pgiro =CNRCTB001.GESTIONE_SPESE
									   	and l.ESERCIZIO_PGIRO_ORIGINE      = asS.esercizio
									   	and l.CD_CDS_PGIRO_ORIGINE         = asS.cd_cds
									   	and l.PG_PGIRO_ORIGINE 						 = asS.pg_obbligazione
									   	and l.ESERCIZIO_ORI_PGIRO_ORIGINE  = asS.ESERCIZIO_ORI_OBBLIGAZIONE
									   	and a.esercizio = asS.esercizio
									   	and a.cd_cds    = asS.cd_cds
									   	and a.pg_accertamento  = asS.pg_accertamento
									   	and a.ESERCIZIO_ORIGINALE = ass.ESERCIZIO_ORI_ACCERTAMENTO
									   	and a.pg_accertamento_scadenzario = 1;

									Dbms_Output.PUT_LINE ('CERCO CORI LOC 999 '||aAccScad.esercizio||' '||aAccScad.cd_cds||' '||aAccScad.pg_accertamento||' '||aAccScad.pg_accertamento_scadenzario);

		            Select *
			            Into  aCoriLoc
			            From  contributo_ritenuta
			            Where cd_cds_accertamento = aAccScad.cd_cds
				      And esercizio_accertamento = aAccScad.esercizio
				      And esercizio_ori_accertamento = aAccScad.esercizio_originale
			 	      And pg_accertamento = aAccScad.pg_accertamento
				      And pg_accertamento_scadenzario = aAccScad.pg_accertamento_scadenzario;

			   -- Se l'accertamento di contropartita che ha aperto la partita di giro non ?
			   -- collegato a CORI di compenso, esce
			   -- (modifica del 13/07/2005 => ... dopo aver controllato anche che l'accertamento di origine
						end if;

             exception when NO_DATA_FOUND Then
								    -- RICERCA L'ACCERTAMENTO DELL'ESERCIZIO PRECEDENTE
								    Declare
								        CDS_ES_PREC     VARCHAR(20);
								        ES_PREC         NUMBER;
								        ES_ORI_PREC     NUMBER;
								        PG_ACC_ES_PREC  NUMBER;
								    Begin
											Dbms_Output.PUT_LINE ('NO DATA ACC  999 '||aAccScad.cd_cds||' '||aAccScad.esercizio||' '||aAccScad.pg_accertamento);
								      Select CD_CDS_ORI_RIPORTO, ESERCIZIO_ORI_RIPORTO, ESERCIZIO_ORI_ORI_RIPORTO, PG_ACCERTAMENTO_ORI_RIPORTO
								      Into   CDS_ES_PREC, ES_PREC, ES_ORI_PREC, PG_ACC_ES_PREC
								      From   ACCERTAMENTO
								      Where  CD_CDS          = aAccScad.cd_cds    And
								             ESERCIZIO       = aAccScad.esercizio And
								             ESERCIZIO_ORIGINALE = aAccScad.esercizio_originale And
								             PG_ACCERTAMENTO = aAccScad.pg_accertamento;

										Dbms_Output.PUT_LINE ('NO DATA CORI LOC 999 '||ES_PREC||' '||CDS_ES_PREC||' '||PG_ACC_ES_PREC);

								       select * into aCoriLoc from contributo_ritenuta
								       Where  cd_cds_accertamento    = CDS_ES_PREC
								          and esercizio_accertamento = ES_PREC
								          and esercizio_ori_accertamento = ES_ORI_PREC
								          and pg_accertamento        = PG_ACC_ES_PREC
								          and pg_accertamento_scadenzario = 1;
					          --exception when NO_DATA_FOUND Then
								      --     Dbms_Output.PUT_LINE ('NO DATA 999');
								        --   return;
					          end;
           end;
    End;
end;

Elsif aGenRiga.pg_accertamento is not null then
   -- Carico l'obbligazione collegato all'accertamento su partita di giro
  Begin
Dbms_Output.PUT_LINE ('OBB '||aGenRiga.esercizio_obbligazione||' '||aGenRiga.cd_cds_obbligazione||' '||aGenRiga.pg_obbligazione);
        Select *
        Into  aObbScad
        From  obbligazione_scadenzario
        Where cd_cds = aGenRiga.cd_cds_accertamento And
              esercizio = aGenRiga.esercizio_accertamento And
              (esercizio_originale, pg_obbligazione) In
                                (Select esercizio_ori_obbligazione, pg_obbligazione
                                 From  ass_obb_acr_pgiro
                                 Where cd_cds = aGenRiga.cd_cds_accertamento And
                                       esercizio = aGenRiga.esercizio And
                                       esercizio_ori_accertamento = aGenRiga.esercizio_ori_accertamento And
			               pg_accertamento = aGenRiga.pg_accertamento
                                       -- And ti_origine = CNRCTB001.GESTIONE_SPESE REMMATO IL 28.06.2006 STANI (RITENUTE STIPENDI)
                                       ) And
              pg_obbligazione_scadenzario = 1;
  Exception
    When No_Data_Found Then
      IBMERR001.RAISE_ERR_GENERICO('L''obbligazione non ? associata a Partite di Giro con origine '||CNRCTB001.GESTIONE_SPESE);
  End;

  Begin
    Select * into aCoriLoc from contributo_ritenuta where
        -- Fix del 23/04/2004 esercizio e cds sono quelli dell'obbl. e non dell'acc.
	        cd_cds_obbligazione = aObbScad.cd_cds
	    and esercizio_obbligazione = aObbScad.esercizio
	    and esercizio_ori_obbligazione = aObbScad.esercizio_originale
            and pg_obbligazione = aObbScad.pg_obbligazione
	    and pg_obbligazione_scadenzario = aObbScad.pg_obbligazione_scadenzario;
   -- Se l'obbligazione di contropartita che ha aperto la partita di giro non ? colegata a CORI di compenso, esce
  Exception when NO_DATA_FOUND then

    -- RICERCA L'OBBLIGAZIONE DELL'ESERCIZIO PRECEDENTE

    Declare
        CDS_ES_PREC     VARCHAR(20);
        ES_PREC         NUMBER;
        ES_ORI_PREC     NUMBER;
        PG_OBB_ES_PREC  NUMBER;
    Begin
      Select CD_CDS_ORI_RIPORTO, ESERCIZIO_ORI_RIPORTO, ESERCIZIO_ORI_ORI_RIPORTO, PG_OBBLIGAZIONE_ORI_RIPORTO
      Into   CDS_ES_PREC, ES_PREC, ES_ORI_PREC, PG_OBB_ES_PREC
      From   OBBLIGAZIONE
      Where  CD_CDS              = aObbScad.cd_cds    And
             ESERCIZIO           = aObbScad.esercizio And
             ESERCIZIO_ORIGINALE = aObbScad.esercizio_originale And
             PG_OBBLIGAZIONE     = aObbScad.pg_obbligazione;

           select * into aCoriLoc from contributo_ritenuta where
	        cd_cds_obbligazione = CDS_ES_PREC
	    and esercizio_obbligazione = ES_PREC
	    and esercizio_ori_obbligazione = ES_ORI_PREC
	    and pg_obbligazione = PG_OBB_ES_PREC
	    and pg_obbligazione_scadenzario = 1;

    	Exception
   		When No_Data_Found Then
			 			Begin
 						select o.*
		            into  aObbScad
		            from  LIQUID_CORI_ASS_PGIRO l,ass_obb_acr_pgiro asS,obbligazione_scadenzario O
		                  Where
		                     	l.ESERCIZIO_PGIRO_NEW = aGenRiga.esercizio_accertamento
						  				and l.CD_CDS_PGIRO_NEW  =aGenRiga.cd_cds_accertamento
											and l.PG_PGIRO_NEW = aGenRiga.pg_accertamento
									   	and ESERCIZIO_ORI_PGIRO_NEW  =aGenRiga.esercizio_ori_accertamento
									   	and tipo_Pgiro =CNRCTB001.GESTIONE_ENTRATE
									   	and l.ESERCIZIO_PGIRO_ORIGINE  = asS.esercizio
									   	and l.CD_CDS_PGIRO_ORIGINE  = asS.cd_cds
									   	and l.PG_PGIRO_ORIGINE   = asS.pg_accertamento
									   	and l.ESERCIZIO_ORI_PGIRO_ORIGINE  = asS.ESERCIZIO_ORI_accertamento
									   	and o.esercizio = asS.esercizio
									   	and o.cd_cds    = asS.cd_cds
									   	and o.pg_obbligazione  = asS.pg_obbligazione
									   	and o.ESERCIZIO_ORIGINALE = ass.ESERCIZIO_ORI_obbligazione
									   	and o.pg_obbligazione_scadenzario = 1;

									Dbms_Output.PUT_LINE ('CERCO CORI LOC 999'||aObbScad.esercizio||' '||aObbScad.cd_cds||' '||aObbScad.pg_obbligazione||' '||aObbScad.pg_obbligazione_scadenzario);

		            Select * into aCoriLoc from contributo_ritenuta where
       			 -- Fix del 23/04/2004 esercizio e cds sono quelli dell'obbl. e non dell'acc.
					        cd_cds_obbligazione = aObbScad.cd_cds
					   	  and esercizio_obbligazione = aObbScad.esercizio
					    	and esercizio_ori_obbligazione = aObbScad.esercizio_originale
				        and pg_obbligazione = aObbScad.pg_obbligazione
					   		and pg_obbligazione_scadenzario = aObbScad.pg_obbligazione_scadenzario;

		-- Se l'obbligazione di contropartita che ha aperto la partita di giro non ? colegata a CORI di compenso, esce
            exception when NO_DATA_FOUND Then
								    -- RICERCA L'ACCERTAMENTO DELL'ESERCIZIO PRECEDENTE
				   	Declare
				        CDS_ES_PREC     VARCHAR(20);
				        ES_PREC         NUMBER;
				        ES_ORI_PREC     NUMBER;
				        PG_OBB_ES_PREC  NUMBER;
				    Begin
				      Select CD_CDS_ORI_RIPORTO, ESERCIZIO_ORI_RIPORTO, ESERCIZIO_ORI_ORI_RIPORTO, PG_OBBLIGAZIONE_ORI_RIPORTO
				      Into   CDS_ES_PREC, ES_PREC, ES_ORI_PREC, PG_OBB_ES_PREC
				      From   OBBLIGAZIONE
				      Where  CD_CDS              = aObbScad.cd_cds    And
				             ESERCIZIO           = aObbScad.esercizio And
				             ESERCIZIO_ORIGINALE = aObbScad.esercizio_originale And
				             PG_OBBLIGAZIONE     = aObbScad.pg_obbligazione;

				           select * into aCoriLoc from contributo_ritenuta where
					        		cd_cds_obbligazione = CDS_ES_PREC
					    		and esercizio_obbligazione = ES_PREC
					    		and esercizio_ori_obbligazione = ES_ORI_PREC
					    		and pg_obbligazione = PG_OBB_ES_PREC
					    		and pg_obbligazione_scadenzario = 1;
								  --exception when NO_DATA_FOUND Then
									  --       Dbms_Output.PUT_LINE ('NO DATA 999');
									    --     return;
								  end;
			      end;
		  End;
   end;

Else
   IBMERR001.RAISE_ERR_GENERICO('Il documento generico di versamento non risulta collegato a '||cnrutil.getLabelObbligazioneMin()||' o accertamento:'||aDoc.pg_numero_doc||' tipo:'||aDoc.cd_tipo_doc);
End if;
	IF(aCoriLoc.cd_contributo_ritenuta IS NULL and aDoc.cd_elemento_voce is not null) then
		if  (aDoc.CD_TIPO_DOC in (CNRCTB100.TI_GEN_CORI_VER_ENTRATA)) then
		CNRCTB204.buildMovPrinc(
   aDocTst.cd_cds_origine,aDocTst.esercizio,aDocTst.cd_uo_origine,
   CNRCTB204.trovaContoContrEp(aDocTst.esercizio,'C','E',aDoc.cd_elemento_voce,null),
   abs(aDoc.im_doc),
   CNRCTB204.getSezione(aDocTst),
   null,
   null,
   aDoc.cd_terzo,
   aListaMovimenti,
   aUser,
   aTSnow
  );
  elsif (aDoc.CD_TIPO_DOC in (CNRCTB100.TI_GEN_CORI_VER_SPESA)) then
  CNRCTB204.buildMovPrinc(
   aDocTst.cd_cds_origine,aDocTst.esercizio,aDocTst.cd_uo_origine,
   CNRCTB204.trovaContoContrEp(aDocTst.esercizio,'D','S',aDoc.cd_elemento_voce,null),
   abs(aDoc.im_doc),
   CNRCTB204.getSezione(aDocTst),
   null,
   null,
   aDoc.cd_terzo,
   aListaMovimenti,
   aUser,
   aTSnow
  );
  end if;
	ELSE
  	aAssCoriEp:=CNRCTB204.getAssCoriEp(aDocTst.esercizio, aCoriLoc.cd_contributo_ritenuta,aCoriLoc.ti_ente_percipiente,CNRCTB204.getSezioneCoriComp(aCoriLoc));
  	CNRCTB204.buildMovPrinc(
   aDocTst.cd_cds_origine,aDocTst.esercizio,aDocTst.cd_uo_origine,
   CNRCTB002.getVoceEp(aAssCoriEp.esercizio, aAssCoriEp.cd_voce_ep_contr),
   abs(aCoriLoc.ammontare),
   aAssCoriEp.sezione,
   null,
   null,
   aDoc.cd_terzo,
   aListaMovimenti,
   aUser,
   aTSnow
  );
 END IF;
dbms_output.put_line('aCoriLoc '||aCoriLoc.ammontare);
dbms_output.put_line('aCoriLoc  conto '||aAssCoriEp.cd_voce_ep_contr);

 end;


Procedure buildMovCommissioni1210(aListaMovimenti IN OUT CNRCTB200.movimentiList,
                                  aDocTst V_DOC_ULT_COGE_TSTA%rowtype,
                                  aDoc V_DOC_ULT_COGE_RIGA%rowtype,
                                  aUser varchar2,
                                  aTSnow date) is
  aDocAmm v_doc_amm_coge_tsta%rowtype;
  aVoceEp        voce_ep%rowtype;
  MAN_RIGA       MANDATO_RIGA%rowtype;
  aEV            ELEMENTO_VOCE%Rowtype;
  CONTO_DA_PASS  VOCE_EP%rowtype;
Begin

-- Richiesta n. 793. Gestione esplicita delle commissioni di cambio su scrittura ultima

If aDoc.cd_tipo_doc = CNRCTB100.TI_FATTURA_PASSIVA Or
     ((CNRCTB100.ISINTABELLAGENERICO(aDoc.cd_tipo_doc) = 'Y') And
       aDocTst.cd_tipo_documento_cont=CNRCTB018.TI_DOC_MAN) then
  Begin
	 -- Se si tratta di fattura associata a lettera di pagam. estero
	 select * into aDocAmm
	 from v_doc_amm_coge_tsta
	 Where cd_tipo_documento=aDoc.cd_tipo_doc
	   and cd_cds = aDoc.cd_cds_doc
           and cd_unita_organizzativa = aDoc.cd_uo_doc
	   and esercizio = aDoc.esercizio_doc
	   and pg_numero_documento = aDoc.pg_numero_doc
	   and pg_lettera is not null
	   and im_pagamento is not null;


-- S.F. 28.02.2006 AGGIUNTO PER RICHIESTA FRANCA CAMPANALE
-- PER LE VOCI DI TIPO INVENTARIALE LE COMMISSIONI NON VANNO SUL CONTO DELLE COMMISSIONI BANCARIE
-- MA BENSI' SUL CONTO (DI COSTO) ASSOCIATO ALLA VOCE DEL MANDATO

     Begin

-- RECUPERO L'ELEMENTO VOCE DI TUTTE LE OBBLIGAZIONI PRESENTI SULLE RIGHE DEL MANDATO (CON ANCHE IL DOCUMENTO AMMINISTRATIVO)

         Select Distinct EV.*
         Into   aEV
         From   MANDATO_RIGA MR, OBBLIGAZIONE O, OBBLIGAZIONE_SCADENZARIO OS, ELEMENTO_VOCE EV
         Where  MR.CD_CDS                 = aDoc.CD_CDS And
                MR.ESERCIZIO              = aDoc.ESERCIZIO And
                MR.PG_MANDATO             = aDoc.PG_DOCUMENTO_CONT And
                MR.CD_CDS_DOC_AMM         = aDoc.CD_CDS_DOC And
                MR.CD_UO_DOC_AMM          = aDoc.CD_UO_DOC And
                MR.ESERCIZIO_DOC_AMM      = aDoc.ESERCIZIO_DOC And
                MR.CD_TIPO_DOCUMENTO_AMM  = aDoc.CD_TIPO_DOC And
                MR.PG_DOC_AMM             = aDoc.PG_NUMERO_DOC And
                O.CD_CDS                  = OS.CD_CDS          And
                O.ESERCIZIO               = OS.ESERCIZIO       And
                O.ESERCIZIO_ORIGINALE     = OS.ESERCIZIO_ORIGINALE And
                O.PG_OBBLIGAZIONE         = OS.PG_OBBLIGAZIONE And
                OS.CD_CDS                 = MR.CD_CDS    And
                OS.ESERCIZIO              = MR.ESERCIZIO_OBBLIGAZIONE And
                OS.ESERCIZIO_ORIGINALE    = MR.ESERCIZIO_ORI_OBBLIGAZIONE And
                OS.PG_OBBLIGAZIONE        = MR. PG_OBBLIGAZIONE And
                OS.PG_OBBLIGAZIONE_SCADENZARIO = MR.PG_OBBLIGAZIONE_SCADENZARIO And
                O.ESERCIZIO               = EV.ESERCIZIO        And
                O.TI_APPARTENENZA         = EV.TI_APPARTENENZA  And
                O.TI_GESTIONE             = EV.TI_GESTIONE      And
                O.CD_ELEMENTO_VOCE        = EV.CD_ELEMENTO_VOCE;


-- SE HA L'ATTRIBUTO CHE MI INTERESSA CERCO IL CONTO ASSOCIATO ALLA VOCE (E LO METTO IN CONTO_DA_PASS)
Dbms_Output.PUT_LINE ('C');
        If aEV.FL_INV_BENI_PATR = 'Y' Then
Dbms_Output.PUT_LINE ('D');
            Select VOCE_EP.*
            Into   CONTO_DA_PASS
            From   ass_ev_voceep, VOCE_EP
            Where  ass_ev_voceep.esercizio = aEV.esercizio and
                   ass_ev_voceep.ti_appartenenza = aEV.ti_appartenenza  And
                   ass_ev_voceep.ti_gestione = aEV.ti_gestione  And
                   ass_ev_voceep.cd_elemento_voce = aEV.cd_elemento_voce And
                   ass_ev_voceep.ESERCIZIO  = VOCE_EP.ESERCIZIO And
                   ass_ev_voceep.CD_VOCE_EP = VOCE_EP.CD_VOCE_EP;

        Else
Dbms_Output.PUT_LINE ('E');
            CONTO_DA_PASS := Null;
        End If;
Dbms_Output.PUT_LINE ('F');
     Exception
-- QUALSIASI ERRORE ANNULLO CONTO_DA_PASS
        When Others Then
Dbms_Output.PUT_LINE ('G');
                CONTO_DA_PASS := Null;
     End;

Dbms_Output.PUT_LINE ('H');
-- SE CONTO DA_PASS E' VUOTO (O PER ERRORE O PERCHE' LA VOCE NON E' INVENTARIALE)
-- PRENDO LE COMMISSIONI
     If CONTO_DA_PASS.cd_voce_ep Is Null Then
Dbms_Output.PUT_LINE ('I');
        CONTO_DA_PASS := CNRCTB002.getVoceEpCommissioniBanca(aDocTst.esercizio);
     End If;

     -- Fix richiesta del 06/09/2004
     -- Le commissioni vanno in dare mentre va creato un nuovo movimento sul conto del
     -- avere per l'importo delle commissioni

   Dbms_Output.PUT_LINE (' CONTO PASSATO 1 '||CONTO_DA_PASS.CD_VOCE_EP||' com '||to_char(aDocAmm.im_commissioni));

     CNRCTB204.buildMovPrinc(
      aDocTst.cd_cds_origine,
      aDocTst.esercizio,
      aDocTst.cd_uo_origine,
      CONTO_DA_PASS,
      aDocAmm.im_commissioni,
      CNRCTB200.IS_DARE,
      null,
      null,
      aDoc.cd_terzo,
      ADOCAMM.TI_ISTITUZ_COMMERC,
      aListaMovimenti,
      aUser,
      aTSnow);

     aVoceEp:=CNRCTB204.trovaContoAnag(aDoc, aDoc.cd_terzo,aEv.CD_ELEMENTO_VOCE);

     Dbms_Output.PUT_LINE (' CONTO PASSATO 2 '||aVoceEp.CD_VOCE_EP||' com '||to_char(aDocAmm.im_commissioni));
     CNRCTB204.buildMovPrinc(
      aDocTst.cd_cds_origine,
      aDocTst.esercizio,
      aDocTst.cd_uo_origine,
      aVoceEp,
      aDocAmm.im_commissioni,
      CNRCTB200.IS_AVERE,
      null,
      null,
      aDoc.cd_terzo,
      --CNRCTB100.TI_ISTITUZIONALE,
      ADOCAMM.TI_ISTITUZ_COMMERC,
      aListaMovimenti,
      aUser,
      aTSnow
     );
  Exception when NO_DATA_FOUND then
     null;
  End;

End if;

End;

 -- =========================================================
 -- Motori di registrazione scritture ultime/singole
 -- =========================================================

 procedure regDocPag1210COGE(aDocTst V_DOC_ULT_COGE_TSTA%rowtype,
                             aCdTerzo number,
                             aTiIstituzCommerc char,
                             aUser varchar2,
                             aTSNow date) is
  aScrittura scrittura_partita_doppia%rowtype;
  aListaMovimenti CNRCTB200.movimentiList;
  aTipoScrittura char(1);
  aSezionePrincipale char(1);
  aVoceEp voce_ep%rowtype;
  aDelta number(15,2);
  aDocAmm v_doc_amm_coge_tsta%rowtype;
  isUtilePerdita boolean;
  aCausale varchar2(50);
  fl_snr CHAR(1);
  fl_merce_intra  CHAR(1);
  aEV_ES          ELEMENTO_VOCE.ESERCIZIO%Type;
  aEV_TI          ELEMENTO_VOCE.TI_APPARTENENZA%Type;
  aEV_GE          ELEMENTO_VOCE.TI_GESTIONE%Type;
  aEV_EV          ELEMENTO_VOCE.CD_ELEMENTO_VOCE%Type;
  aEV_flag        ELEMENTO_VOCE.FL_INV_BENI_PATR%Type;

Begin
recParametriCNR := CNRUTL001.getRecParametriCnr(aDocTst.esercizio);

For aDoc in (select Distinct esercizio_doc,
 			       cd_tipo_doc,
 			       cd_cds_doc,
 			       cd_uo_doc,
 			       pg_numero_doc,
 			       cd_elemento_voce
 	       from V_DOC_ULT_COGE_RIGA
 	       Where cd_cds = aDocTst.cd_cds And
 	             esercizio = aDocTst.esercizio And
 	             pg_documento_cont = aDocTst.pg_documento_cont And
 	             cd_tipo_documento_cont = aDocTst.cd_tipo_documento_cont And
 	             cd_terzo = aCdTerzo) loop

   aListaMovimenti.delete;
   isUtilePerdita:=true;

Begin
    select * into aDocAmm
    from V_DOC_AMM_COGE_TSTA
    Where cd_cds = aDoc.cd_cds_doc And
          esercizio = aDoc.esercizio_doc And
          cd_tipo_documento = aDoc.cd_tipo_doc And
          cd_unita_organizzativa = aDoc.cd_uo_doc And
          pg_numero_documento = aDoc.pg_numero_doc;
Exception when NO_DATA_FOUND then
    IBMERR001.RAISE_ERR_GENERICO('Documento amministrativo non trovato per doc. autorizz.: '||CNRCTB204.GETDESCDOCUMENTO(aDocTst));
End;

CNRCTB100.LOCKDOCAMM(aDocAmm.cd_tipo_documento,
                        aDocAmm.cd_cds,
                        aDocAmm.esercizio,
                        aDocAmm.cd_unita_organizzativa,
                        aDocAmm.pg_numero_documento);

   -- Se l'importo pagato via lettera non ? nullo, creo l'eventuale scrittura di utile perdita

If aDocAmm.im_pagamento is not null then

     -- Le commissioni NON SONO ANNEGATE nella scrittura di utile/perdita su cambi
     aDelta := aDocAmm.im_pagamento;

     If aDocAmm.im_commissioni is not null Then
        aDelta:=aDelta-aDocAmm.im_commissioni;
     End if;

     If aDocAmm.cd_tipo_documento = CNRCTB100.TI_FATTURA_PASSIVA then
         select fl_servizi_non_residenti,fl_merce_intra_ue into fl_snr,fl_merce_intra
					from fattura_passiva, tipo_sezionale
    			where
			    fattura_passiva.cd_tipo_sezionale = tipo_sezionale.cd_tipo_sezionale and
			    fattura_passiva.esercizio = aDocAmm.esercizio and
			    fattura_passiva.cd_cds = aDocAmm.cd_cds and
			    fattura_passiva.cd_unita_organizzativa = aDocAmm.cd_unita_organizzativa and
			    fattura_passiva.pg_fattura_passiva = aDocAmm.pg_numero_documento;

			if (((aDocAmm.FL_SAN_MARINO_SENZA_IVA = 'Y' or aDocAmm.FL_INTRA_UE = 'Y' or fl_merce_intra ='Y') And
       	aDocAmm.TI_ISTITUZ_COMMERC = CNRCTB100.TI_ISTITUZIONALE And aDocAmm.TI_BENE_SERVIZIO = CNRCTB100.TI_FT_ACQ_BENI) Or
		  	(aDocAmm.TI_ISTITUZ_COMMERC = CNRCTB100.TI_COMMERCIALE) or
	   		(aDocAmm.TI_ISTITUZ_COMMERC = CNRCTB100.TI_ISTITUZIONALE and aDocAmm.TI_BENE_SERVIZIO = CNRCTB100.TI_FT_ACQ_SERVIZI and
	   		fl_snr ='Y' )) Then -- STANI 26.04 Cinti
          	aDelta:=aDelta - (aDocAmm.im_totale_imponibile);
     	Else
          aDelta:=aDelta - (aDocAmm.im_totale_imponibile + aDocAmm.im_totale_iva);
         	Dbms_Output.PUT_LINE ('else1 aDelta '||to_char(aDelta));
     End If;
  else
          aDelta:=aDelta - (aDocAmm.im_totale_imponibile + aDocAmm.im_totale_iva);
         	Dbms_Output.PUT_LINE ('else2 aDelta '||to_char(aDelta));
	end if;

	Dbms_Output.PUT_LINE ('aDelta '||to_char(aDelta));
     If aDelta > 0 then
       aSezionePrincipale:=CNRCTB200.IS_DARE; -- Perdita in dare
       aVoceEp:=CNRCTB002.GETVOCEEPPERDITASUCAMBIO(aDocTst.esercizio);
       aCausale:=CNRCTB200.CAU_PERDITA_SU_CAMBI;
     Elsif aDelta < 0 then
       aSezionePrincipale:=CNRCTB200.IS_AVERE; -- Utile in avere
       aVoceEp:=CNRCTB002.GETVOCEEPUTILESUCAMBIO(aDocTst.esercizio);
       aCausale:=CNRCTB200.CAU_UTILE_SU_CAMBI;
     Else
       isUtilePerdita:=false;
     End if;


-- S.F. 16.03.2006 AGGIUNTO PER RICHIESTA FRANCA CAMPANALE
-- PER LE VOCI DI TIPO INVENTARIALE LE COMMISSIONI NON VANNO SUL CONTO DELLE COMMISSIONI BANCARIE
-- MA BENSI' SUL CONTO ASSOCIATO ALLA VOCE DEL MANDATO

-- SOSTITUISCO IL CONTO UTILE/PERDITA SU CAMBIO CON QUELLO PATRIMONIALE

     Begin

-- RICAVO L'ELEMENTO VOCE DELLE OBBLIGAZIONI MESSE SUI DETTAGLI DI FATTURA

     If aDocAmm.cd_tipo_documento = CNRCTB100.TI_FATTURA_PASSIVA Then

         Select Distinct EV.ESERCIZIO, EV.TI_APPARTENENZA, EV.TI_GESTIONE, EV.CD_ELEMENTO_VOCE, EV.FL_INV_BENI_PATR
         Into   aEV_ES, aEV_TI, aEV_GE, aEV_EV, aEV_flag
         From   FATTURA_PASSIVA FP, FATTURA_PASSIVA_RIGA FPR, OBBLIGAZIONE O, OBBLIGAZIONE_SCADENZARIO OS, ELEMENTO_VOCE EV
         Where  FP.CD_CDS                 = aDocAmm.CD_CDS And
                FP.CD_UNITA_ORGANIZZATIVA = aDocAmm.CD_UNITA_ORGANIZZATIVA And
                FP.ESERCIZIO              = aDocAmm.ESERCIZIO And
                FP.PG_FATTURA_PASSIVA     = aDocAmm.PG_NUMERO_DOCUMENTO And
                FP.CD_CDS                 = FPR.CD_CDS                 AND
                FP.CD_UNITA_ORGANIZZATIVA = FPR.CD_UNITA_ORGANIZZATIVA AND
                FP.ESERCIZIO              = FPR.ESERCIZIO              AND
                FP.PG_FATTURA_PASSIVA     = FPR.PG_FATTURA_PASSIVA     AND
                FPR.CD_CDS_OBBLIGAZIONE         = OS.CD_CDS  And
                FPR.ESERCIZIO_OBBLIGAZIONE      = OS.ESERCIZIO And
                FPR.ESERCIZIO_ORI_OBBLIGAZIONE  = OS.ESERCIZIO_ORIGINALE And
                FPR.PG_OBBLIGAZIONE             = OS.PG_OBBLIGAZIONE And
                FPR.PG_OBBLIGAZIONE_SCADENZARIO = OS.PG_OBBLIGAZIONE_SCADENZARIO And
                O.CD_CDS           = OS.CD_CDS          And
                O.ESERCIZIO        = OS.ESERCIZIO       And
                O.ESERCIZIO_ORIGINALE = OS.ESERCIZIO_ORIGINALE And
                O.PG_OBBLIGAZIONE  = OS.PG_OBBLIGAZIONE And
                O.ESERCIZIO        = EV.ESERCIZIO        And
                O.TI_APPARTENENZA  = EV.TI_APPARTENENZA  And
                O.TI_GESTIONE      = EV.TI_GESTIONE      And
                O.CD_ELEMENTO_VOCE = EV.CD_ELEMENTO_VOCE;

-- SE HA L'ATTRIBUTO CHE MI INTERESSA CERCO IL CONTO ASSOCIATO ALLA VOCE (E LO PASSO ALLA SCRITTURA AL POSTO
-- DI UTILE O PERDITA SU CAMBIO)
       -- rospuc da togliere
        If aEV_flag = 'Y' or REcParametriCNR.fl_nuovo_pdg='Y' Then
            Select VOCE_EP.*
            Into   aVoceEp
            From   ass_ev_voceep, VOCE_EP
            Where  ass_ev_voceep.esercizio = aEV_ES and
                   ass_ev_voceep.ti_appartenenza = aEV_TI  And
                   ass_ev_voceep.ti_gestione = aEV_GE  And
                   ass_ev_voceep.cd_elemento_voce = aEV_EV And
                   ass_ev_voceep.ESERCIZIO  = VOCE_EP.ESERCIZIO And
                   ass_ev_voceep.CD_VOCE_EP = VOCE_EP.CD_VOCE_EP;
        End If; -- altrimenti niente perch? il conto gi? c'?
       End If; -- FATTURA
     Exception
-- QUALSIASI ERRORE NON PASSO NIENTE perch? il conto gi? c'?
        When Others Then Null;
     End;

Dbms_Output.PUT_LINE ('CONTO pro '||aVoceEp.CD_VOCE_EP||' delta '||to_char( abs(aDelta)));

/* FINE MODIFICA 16.03.2006 */
		if(aVoceEp.cd_voce_ep is null ) then
				return;
		else


     -- STANI UTILE PROMISCUO
     If isUtilePerdita then
      CNRCTB204.buildMovPrinc(
      aDocTst.cd_cds_origine,aDocTst.esercizio,aDocTst.cd_uo_origine,
      aVoceEp,
      abs(aDelta),
      aSezionePrincipale,
      null,
      null,
      aCdTerzo,
      aDocAmm.ti_istituz_commerc,
      aListaMovimenti,aUser,aTSnow);
      Dbms_Output.PUT_LINE ('aDoc.cd_elemento_voce '||aDoc.cd_elemento_voce);
Dbms_Output.PUT_LINE ('aDoccontr.cd_elemento_voce '||CNRCTB204.trovaContoContrEp(aDocTst.esercizio,'D','S',null,aVoceEp.cd_voce_ep).cd_voce_ep);
      if(RecParametriCNR.fl_nuovo_pdg='Y' ) then
			      CNRCTB204.buildMovPrinc(
			      aDocTst.cd_cds_origine,aDocTst.esercizio,aDocTst.cd_uo_origine,
			      CNRCTB204.trovaContoContrEp(aDocTst.esercizio,'D','S',null,aVoceEp.cd_voce_ep),
			      abs(aDelta),
			      CNRCTB200.getSezioneOpposta(aSezionePrincipale),
			      null,
			      null,
			      aCdTerzo,
			      aDocAmm.ti_istituz_commerc,
			      aListaMovimenti,aUser,aTSnow);

         else

      CNRCTB204.buildMovPrinc(
      aDocTst.cd_cds_origine,aDocTst.esercizio,aDocTst.cd_uo_origine,
      CNRCTB204.trovaContoAnag(aDocamm, aCdTerzo,aDoc.cd_elemento_voce),
      abs(aDelta),
      CNRCTB200.getSezioneOpposta(aSezionePrincipale),
      null,
      null,
      aCdTerzo,
      aDocAmm.ti_istituz_commerc,
      aListaMovimenti,aUser,aTSnow);
  end if;
      aScrittura:=CNRCTB204.buildScrUEP(aDocTst, aCdTerzo, CNRCTB200.TI_SCRITTURA_SINGOLA, CNRCTB200.CAU_PAGAMENTO_1210, aUser,aTSNow);
      aScrittura.cd_causale_coge:=aCausale;
      CNRCTB200.CREASCRITTCOGE(aScrittura,aListaMovimenti);
    End If;
    End If;
End If;

End Loop;

End;

 procedure regDocPagDefaultCOGE(aDocTst V_DOC_ULT_COGE_TSTA%rowtype, aCdTerzo number, aUser varchar2, aTSNow date) is
  aScrittura scrittura_partita_doppia%rowtype;
  aListaMovimenti CNRCTB200.movimentiList;
  aTDoc V_DOC_ULT_COGE_RIGA%rowtype;
  aTipoScrittura char(1);
  aNumIst number;
  aNumComm number;
  aNumProm number;
  aTiIstituzCommerc char(1);
  isManVersIva boolean;
  aNum number;
 begin
  aListaMovimenti.delete;
  aNumIst:=0;
  aNumComm:=0;
  aNumProm:=0;

  aTiIstituzCommerc:=CNRCTB100.TI_ISTITUZIONALE;
  for aDoc in (select * from V_DOC_ULT_COGE_RIGA where
                     cd_cds = aDocTst.cd_cds
 			    and esercizio = aDocTst.esercizio
 				and pg_documento_cont = aDocTst.pg_documento_cont
				and cd_tipo_documento_cont = aDocTst.cd_tipo_documento_cont
				and cd_terzo = aCdTerzo
 		       ) loop
   -- Fix errore 682, il mandato su generico di apertura fondo non deve essere processato in economica
   -- se non con scrittura speciale cassa banca
   if aDoc.cd_tipo_doc = CNRCTB100.TI_GEN_APERTURA_FONDO then
    return;
   end if;

   -- Conseguenze fix errore 757, la reversale su generico di chiusura fondo non deve essere processata in economica
   -- se non con scrittura speciale banca cassa
   if aDoc.cd_tipo_doc = CNRCTB100.TI_GEN_CHIUSURA_FONDO then
    return;
   end if;
	if aDoc.stato_coge_doccont = CNRCTB100.STATO_COEP_EXC then
	dbms_output.put_line('rospuc ');
    return;
   end if;

   -- Fix del 20040825
   -- Fix erorre 834: nessuna movimento su mandato di compenso con anticipo maggiore
   -- del netto percipiente
   if aDoc.cd_tipo_doc = CNRCTB100.TI_COMPENSO then
    if CNRCTB204.isCompConAntMaggNetto(aDoc)='Y' then
     return;
    end if;
   end if;

   -- Costruisce se necessario il movimento delle commissioni (SE LA FA, LA FA QUADRATA COMMISSIONI A DEBITI VS/FORNITORI)

   buildMovCommissioni1210(aListaMovimenti, aDocTst, aDoc, aUser, aTSnow);

   -- Modifica del 22/04/2004
   -- Gestione del mandato di versamento IVA al centro o locale su generico di tipo CNRCTB100.TI_GEN_CORI_VER_SPESA
   isManVersIva:=false;
   If   aDocTst.cd_tipo_documento_cont = CNRCTB018.TI_DOC_MAN
	and aDoc.cd_tipo_doc = CNRCTB100.TI_GEN_CORI_VER_SPESA Then
    -- Verifico se si tratta di mandato di versamento IVA
    begin
     select distinct 1 into aNum from mandato_riga mr where
	     esercizio=aDocTst.esercizio
     and cd_cds=aDocTst.cd_cds
	 and pg_mandato=aDocTst.pg_documento_cont
	 and exists (
	  select 1 from documento_generico dg where
	          esercizio = mr.esercizio_doc_amm
		  and cd_cds = mr.cd_cds_doc_amm
		  and cd_unita_organizzativa = mr.cd_uo_doc_amm
		  and pg_documento_generico = mr.pg_doc_amm
		  and cd_tipo_documento_amm = mr.cd_tipo_documento_amm
		  and exists (select 1 from liquidazione_iva where
		       STATO='D'
		   and REPORT_ID=0
		   and CD_TIPO_DOCUMENTO=dg.cd_tipo_documento_amm
           and CD_CDS_DOC_AMM=dg.cd_cds
           and CD_UO_DOC_AMM=dg.cd_unita_organizzativa
           and ESERCIZIO_DOC_AMM=dg.esercizio
           and PG_DOC_AMM=dg.pg_documento_generico
		  )
	 );
	 isManVersIva:=true;
    exception when NO_DATA_FOUND then
	 null;
	end;
   end if;

   If aDoc.cd_tipo_doc in (CNRCTB100.TI_GEN_CORI_VER_ENTRATA, CNRCTB100.TI_GEN_CORI_VER_SPESA)
      and not isManVersIva    then
    -- Gestione dei versamenti CORI
    -- I movimenti relativi ai versamenti CORI sono accumulati in una lista a parte
    -- In questo modo non influenzano la gestione del tipo istituzionale/commerciale/promiscuo
	  -- fatta per i movimenti anagrafici dei singoli documenti amministrativi di questo ciclo
	  Dbms_Output.PUT_LINE ('buildMovVersCoriUEP AA');
			begin
    			buildMovVersCoriUEP(aListaMovimenti, aDocTst, aDoc, aUser, aTSnow);
			exception when no_data_found then
				declare
					aUOENTE unita_organizzativa%rowtype;
					aUOVERCORI unita_organizzativa%rowtype;
					aUOVerCoRiSac unita_organizzativa%rowtype;
			 	begin
					 	aUOENTE:=CNRCTB020.GETUOENTE(aDocTst.esercizio);
					 	aUOVERCORI:=CNRCTB020.getUOVersCori(aDocTst.esercizio);
					 	aUOVerCoRiSac:=CNRCTB020.getUOVersCoriTuttaSAC(aDocTst.esercizio);
					  if (aDoc.CD_UNITA_ORGANIZZATIVA!= aUOENTE.cd_unita_organizzativa and  aDoc.CD_UNITA_ORGANIZZATIVA!=  aUOVERCORI.cd_unita_organizzativa and
					     aDoc.CD_UNITA_ORGANIZZATIVA!= aUOVerCoRiSac.cd_unita_organizzativa)then
							buildMovUEP(aListaMovimenti,aDocTst, aDoc,aTiIstituzCommerc, aUser,aTSNow);

						-- per ogni doc amm sul terzo specifico, estrae l'informazione ti_istituz_commerc per
						-- incrementare i corrispondenti contatori per tipo

					    if aTiIstituzCommerc = CNRCTB100.TI_COMMERCIALE then
					     aNumComm:=aNumComm+1;
					    elsif aTiIstituzCommerc = CNRCTB100.TI_PROMISCUO then
					     aNumProm:=aNumProm+1;
					    else
					     aNumIst:=aNumIst+1;
					    end if;
					   end if;
				end;
			end;
   Else

    -- Gestione movimento base sul terzo
    -- Il movimento generato viene compresso (per conto/sezione/periodo di competenza) con gli altri
    -- ed ? al momento di tipo ISTITUZIONALE
    -- In un secondo tempo tutti i movimenti generati verranno modificati a commerciali o promscui
    -- sulla base dei tre contatori aggiornati nel loop
--Dbms_Output.PUT_LINE ('buildMovUEP');
	buildMovUEP(aListaMovimenti,aDocTst, aDoc,aTiIstituzCommerc, aUser,aTSNow);

	-- per ogni doc amm sul terzo specifico, estrae l'informazione ti_istituz_commerc per
	-- incrementare i corrispondenti contatori per tipo

    if aTiIstituzCommerc = CNRCTB100.TI_COMMERCIALE then
     aNumComm:=aNumComm+1;
    elsif aTiIstituzCommerc = CNRCTB100.TI_PROMISCUO then
     aNumProm:=aNumProm+1;
    else
     aNumIst:=aNumIst+1;
    end if;
   end if;

   aTDoc:=aDoc;
  end loop;

  -- Determina il tipo istituzionale/commerciale/promicuo per l'ultimo movimento
  -- sulla base dei tipi dei documenti amministrativi che contribuiscono alla scrittura

  aTiIstituzCommerc:=CNRCTB100.TI_ISTITUZIONALE;
  if aListaMovimenti.count > 0 then

   -- Se per il terzo specificato, esistevano movimenti "sia istituzionali che commerciali"
   -- oppure "promiscui"

   if aNumComm * aNumIst <> 0 or aNumProm > 0 then
    aTiIstituzCommerc:=CNRCTB100.TI_PROMISCUO;
   -- Se per il terzo specificato, esistevano movimenti commerciali
   elsif aNumComm > 0 then
	aTiIstituzCommerc:=CNRCTB100.TI_COMMERCIALE;
   end if;
  end if;

  aTipoScrittura:=CNRCTB204.getTipoScrittura(aTDoc);
  Dbms_Output.PUT_LINE ('UNICA CHIAMATA 1');
  buildMovContrUEP(aListaMovimenti,aDocTst,aTDoc,aTiIstituzCommerc,aUser,aTSNow);

  aScrittura:=CNRCTB204.buildScrUEP(aDocTst, aCdTerzo, aTipoScrittura, aUser,aTSNow);
  CNRCTB200.CREASCRITTCOGE(aScrittura,aListaMovimenti);

  regDocPag1210COGE(aDocTst, aCdTerzo, aTiIstituzCommerc, aUser, aTSNow);

 End;

 procedure regDocPagCassaBancaCOGE(aStatoPFEco varchar2, aDocTst V_DOC_ULT_COGE_TSTA%rowtype, aUser varchar2, aTSNow date) is
  aScrittura scrittura_partita_doppia%rowtype;
  aListaMovimenti CNRCTB200.movimentiList;
  aSezLoc char(1);
 begin
  aListaMovimenti.delete;
  -- ATTENZIONE!!! la vista V_DOC_ULT_COGE_RIGA ritorna a R lo stato di associazione a fondo economale
  -- per tutti i documenti generici di REINTEGRO, APERTURA e CHIUSURA del fondo indipendentemente
  -- dallo stato attualmente presente sul documento

  -- Fix errore 682, il mandato su generico di apertura fondo non deve essere processato in economica
  -- se non con scrittura speciale cassa banca
  -- Fix errore 757, la reversale di restituzione alla banca (su GEN_CH_FON)
  -- deve effettuare una scrittura speciale di tipo BANCA CASSA

  -- Per i mandati di reintegro (non in chiusura) o apertura del fondo economale, bisogna chiudere la cassa sulla banca
  -- Per la reversale di restituzione, bisogna chiudere la banca sulla cassa
  -- La reversale di regolarizzazione collegata al mandato di regolarizzazione ? COEP esclusa (X)
  if
           (aStatoPFEco in (CNRCTB100.STATO_ASS_PFONDOECO, CNRCTB100.STATO_REG_PFONDOECO))
  then
   -- Il mandato di regolarizzazione per chiusura fondo non fa scritture cassa-banca
   If aDocTst.ti_man_rev=CNRCTB038.TI_MAN_REG then
    return;
   end if;

  aSezLoc:=null;
   -- Nel caso di resituzione fondo la sezione principale ? avere perch? su reversale
   aSezLoc:=CNRCTB204.getSezione(aDocTst);
   -- Se si tratta di mandato di regolarizzazione per reintegro spese in chiusura del fondo
   -- la scrittura ? BANCA a CASSA invece di cassa a banca
   -- La stessa COSA vale anche per la REVERSALE di restituzione (in questo caso la sezione principale del documento contabile ? AVERE)
   CNRCTB204.buildMovPrinc(
    aDocTst.cd_cds_origine,aDocTst.esercizio,aDocTst.cd_uo_origine,
    CNRCTB002.GETVOCEEPCASSA(aDocTst.esercizio),
    aDocTst.im_documento,
    aSezLoc,
    null,
    null,
    aDocTst.cd_terzo,
    aListaMovimenti,aUser,aTSnow
   );
   CNRCTB204.buildMovPrinc(
    aDocTst.cd_cds_origine,aDocTst.esercizio,aDocTst.cd_uo_origine,
    CNRCTB002.GETVOCEEPBANCACDS(aDocTst.esercizio),
    aDocTst.im_documento,
    CNRCTB200.getSezioneOpposta(aSezLoc),
    null,
    null,
    aDocTst.cd_terzo,
    aListaMovimenti,aUser,aTSnow
   );
   dbms_output.put_line('Ignorato I/C');
   aScrittura:=CNRCTB204.buildScrUEP(aDocTst, aDocTst.cd_terzo, CNRCTB200.TI_SCRITTURA_SINGOLA, CNRCTB200.CAU_CASSA_BANCA, aUser,aTSNow);
   CNRCTB200.CREASCRITTCOGE(aScrittura,aListaMovimenti);
  end if;
 end;

 procedure regDocPagCOGE(aDocTst V_DOC_ULT_COGE_TSTA%rowtype, aUser varchar2,aTSNow date) is
  aScrittura scrittura_partita_doppia%rowtype;
  aMovimento movimento_coge%rowtype;
  aListaMovimenti CNRCTB200.movimentiList;
  aListaScritture CNRCTB200.scrittureList;
  aListaM CNRCTB200.movimentiList;
  aCdTerzo number;
  aTipoScrittura char(1);
  aStatoPFEco varchar2(5);
  aMan mandato%rowtype;
  aRev reversale%rowtype;
  aUOENTE unita_organizzativa%rowtype;
 begin
  -- Il documento deve essere annullato o esitato altrimenti esce
  if aDocTst.stato not in (CNRCTB038.STATO_AUT_ANN,CNRCTB038.STATO_AUT_ESI) then
Dbms_Output.put_line ('NO CONT DOC A');
   return;
  end if;

  -- Fix del 20040924 Richiesta 843
  CNRCTB204.checkChiusuraEsercizio(aDocTst.esercizio, aDocTst.cd_cds_origine);

  if not (CNRCTB200.ISCHIUSURACOEPDEF(aDocTst.esercizio-1, aDocTst.cd_cds_origine)='Y') then
   IBMERR001.RAISE_ERR_GENERICO('L''esercizio economico precedente '||To_Char(aDocTst.esercizio-1)||' non ? chiuso definitivamente per il cds: '||aDocTst.cd_cds_origine);
  end if;

Dbms_Output.put_line ('a');

  -- Lock del documento autorizzatorio
  if aDocTst.cd_tipo_documento_cont = CNRCTB018.TI_DOC_MAN then
      select * into aMan from mandato where
         esercizio = aDocTst.esercizio
     and cd_cds = aDocTst.cd_cds
     and pg_mandato = aDocTst.pg_documento_cont for update nowait;
  elsif aDocTst.cd_tipo_documento_cont = CNRCTB018.TI_DOC_REV then
      select * into aRev from reversale where
         esercizio = aDocTst.esercizio
     and cd_cds = aDocTst.cd_cds
     and pg_reversale = aDocTst.pg_documento_cont for update nowait;
  else
   IBMERR001.RAISE_ERR_GENERICO('Tipo di documento non supportato: '||CNRCTB204.getDescDocumento(aDocTst));
  end if;
--Dbms_Output.put_line ('b');
  If   aDocTst.stato_coge in (CNRCTB100.STATO_COEP_CON, CNRCTB100.STATO_COEP_EXC)  then
Dbms_Output.put_line ('NO CONT DOC B');
   return;
  end if;

  Dbms_Output.put_line ('c');

  -- IBMUTL010.log(CNRCTB204.GETDESCDOCUMENTO(aDocTst),'TEST');
  -- Se il doc autorizzatorio risulta annullato storno la scrittura ultima
  if aDocTst.stato = CNRCTB038.STATO_AUT_ANN then
   aListaMovimenti.delete;
   -- cerca eventuali scritture gia registrate per il documento
   CNRCTB204.getScrittureUEPLock(aDocTst,aListaScritture);
   -- Per ogni scrittura crea uno storno per poi riemettere la scrittura aggiornata
   if aListaScritture.count > 0 then
    for i in 1 .. aListaScritture.count loop
     CNRCTB200.getScritturaEPLOCK(aListaScritture(i),aListaMovimenti);
     CNRCTB200.creaScrittStornoCoge(aListaScritture(i),aListaMovimenti, aUser, aTSNow);
    end loop;
   end if;
   goto fine;
  end if;
Dbms_Output.put_line ('d');
  -- Filtro e aggiorno a stato COGE ESCLUSO LE REVERSALI PER
  -- IVA (intraue/san marino istituzionale per beni)

  if aDocTst.cd_tipo_documento_cont = CNRCTB018.TI_DOC_REV then
   for aTT in (select 1 from V_DOC_ULT_COGE_RIGA where
                    cd_cds = aDocTst.cd_cds
 			    and esercizio = aDocTst.esercizio
 				and pg_documento_cont = aDocTst.pg_documento_cont
				and cd_tipo_documento_cont = aDocTst.cd_tipo_documento_cont
				and cd_tipo_doc = CNRCTB100.TI_GEN_IVA_ENTRATA)
   loop
    update reversale set
	      stato_coge = CNRCTB100.STATO_COEP_EXC,
		  pg_ver_rec=pg_ver_rec+1
    where
	       esercizio = aDocTst.esercizio
	   and cd_cds = aDocTst.cd_cds
	   and pg_reversale = aDocTst.pg_documento_cont;
Dbms_Output.put_line ('NO CONT DOC C');
    return;
   end loop;
  end if;
Dbms_Output.put_line ('e');
  -- Filtro eaggiorno a stato COGE ESCLUSO I MANDATI DI REGOLARIZZAZIONE FATTI SULL'ENTE dall'ENTE
  if     aDocTst.cd_tipo_documento_cont = CNRCTB018.TI_DOC_MAN
         and aDocTst.ti_man_rev = 'R'
	 and aDocTst.cd_cds=aDocTst.cd_cds_origine  then
   aUOENTE:=CNRCTB020.GETUOENTE(aDocTst.esercizio);
   if aDocTst.cd_cds = aUOENTE.cd_unita_padre then
    update mandato set
	      stato_coge = CNRCTB100.STATO_COEP_EXC,
		  pg_ver_rec=pg_ver_rec+1
    Where     esercizio = aDocTst.esercizio
	   and cd_cds = aDocTst.cd_cds
	   and pg_mandato = aDocTst.pg_documento_cont;
Dbms_Output.put_line ('NO CONT DOC D');
    return;
   end if;
  end if;
Dbms_Output.put_line ('f');
  begin
   select distinct stato_pagamento_fondo_eco into aStatoPFEco from V_DOC_ULT_COGE_RIGA where
                    cd_cds = aDocTst.cd_cds
 			    and esercizio = aDocTst.esercizio
 				and pg_documento_cont = aDocTst.pg_documento_cont
				and cd_tipo_documento_cont = aDocTst.cd_tipo_documento_cont;
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('Nessuna riga trovata per documento ti:'||aDocTst.cd_tipo_documento_cont||' pg.'||aDocTst.pg_documento_cont||' cds.'||aDocTst.cd_cds||' es.'||aDocTst.esercizio);
  end;
Dbms_Output.put_line ('g');
  -- Scateno la scrittura di tipo ultimo o singolo
  aCdTerzo:=null;
  for aRTerzo in (
     Select distinct cd_terzo from V_DOC_ULT_COGE_RIGA where
                    cd_cds = aDocTst.cd_cds
 			    and esercizio = aDocTst.esercizio
 				and pg_documento_cont = aDocTst.pg_documento_cont
				and cd_tipo_documento_cont = aDocTst.cd_tipo_documento_cont
  ) loop
Dbms_Output.put_line ('g1');
   aCdTerzo:=aRTerzo.cd_terzo;
Dbms_Output.put_line ('g2 '||aCdTerzo);
   regDocPagDefaultCOGE(aDocTst, aCdTerzo, aUser, aTSNow);
Dbms_Output.put_line ('g3');
  end loop;
  if aCdTerzo is null then
   IBMERR001.RAISE_ERR_GENERICO('Terzo non trovato nel documento amministrativo collegato a doc autorizzatorio');
  end if;
Dbms_Output.put_line ('h');
  regDocPagCassaBancaCOGE(aStatoPFEco, aDocTst, aUser, aTSNow);

  <<fine>>

  -- Aggiornamento dello stato COGE solo a doc autorizzatorio esitato o annullato
  if
         aDocTst.cd_tipo_documento_cont = CNRCTB018.TI_DOC_MAN
  then
     update mandato set
	      stato_coge = CNRCTB100.STATO_COEP_CON,
		  pg_ver_rec=pg_ver_rec+1
	 where
	       esercizio = aDocTst.esercizio
	   and cd_cds = aDocTst.cd_cds
	   and pg_mandato = aDocTst.pg_documento_cont;
  elsif
          aDocTst.cd_tipo_documento_cont = CNRCTB018.TI_DOC_REV
  then
     update reversale set
	      stato_coge = CNRCTB100.STATO_COEP_CON,
		  pg_ver_rec=pg_ver_rec+1
	 where
	       esercizio = aDocTst.esercizio
	   and cd_cds = aDocTst.cd_cds
	   and pg_reversale = aDocTst.pg_documento_cont;
  end if;
 end;

End;
/


