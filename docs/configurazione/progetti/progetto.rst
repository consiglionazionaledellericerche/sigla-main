.. _progetto:

========
Progetto
========

La gestione dell'Anagrafica Progetti consente di gestire tutte le informazioni generali di un progetto, sia esso di ricerca che di funzionamento, e tutte le informazioni contabili di cui tener conto nelle funzionalità successive di previsione e gestione.
Un Progetto si riferisce sempre ad una :doc:`Area Progettuale<area_progettuale>`, ed è inserito, attraverso le linee di attività o GAE, in una Missione/Programma specifici. Il riepilogo per :ref:`Missioni` e :ref:`Programmi`, rappresenta un allegato obbligatorio del Bilancio di Previsione dell'Ente.
L'anagrafica Progetti gestita da Sigla rappresenta l'anagrafica 'contabile' contenente tutte le informazioni utili alle successive gestioni contaili. In alcuni casi, come per il CNR, questa rappresenta un completamento all'anagrafica 'scientifica' dei Progetti gestita in altre procedure per aspetti che non riguardano i dati contabili. A questo scopo parametricamente si definisce se questa anagrafica è gestita interamente in Sigla oppure è inibita la creazione di un progetto perchè proveniente da altra applicazione.
I progetti devono essere obbligatoriamente censiti e in stato 'APPROVATO', vedi :ref:`stati-del-progetto`, per poter gestire il Bilancio di Previsione dell'ente. Naturalmente nel corso dell'anno contabile possono essere creati nuovi Progetti e utilizzati creando i necessari presupposti. 

Le informazioni fondamentali che caratterizzano un Progetto sono:

Dati Anagrafici
================

I dati anagrafici si possono sintetizzare in:

- Codice del Progetto (Codifica libera);
- Area Progettuale di riferimento (tra quelle predefinite nella relativa anagrafica);
- Fase di utilizzo: Previsione, Gestione o entrambi;
- Descrizione del Progetto;
- Dipartimento (coincidente con la struttura organizzativa referente del Progetto e con la definizione del Programma di riferimento);
- :ref:`uo-coordinatrice`;
- :ref:`responsabile-progetto`;

Dati Contabili
================

I dati cotabili rappresentano l'insieme delle informazioni che determinano l'uso del progetto nelle gestioni successive in Sigla:

- :ref:`tipo-finanziamento` (dalla lista valori disponibile);
- Data inizio, Data Fine, Data Proroga (data inizio e fine del Progetto rappresentano la durata scientifica del Progetto);
- Importo Finanziato (proveniente da Fonti esterne);
- Importo Cofinanziato (proveniente da Fonti interne).

.. _stati-del-progetto:

Stati del Progetto
==================
A seconda della Tipologia di Finanziamento un Progetto può essere utilizzato in Previsione, in Gestione o entrambi, solo se ha un determinato Stato. I valori che può assumere lo Stato sono: 

- **Iniziale** (assegnato automaticamente ai Progetti nuovi). Su questi progetti non è possibile operare nè in Previsione nè in Gestione
- **Negoziazione** (consentito solo per Tipologie Progetto ‘Finanziamento’ e ‘Cofinanziamento’ e utile esclusivamente per effettuare la previsione)
- **Approvato** (un progetto completo delle informazioni indispensabili viene 'Approvato' per poter essere utilizzato sia in previsione che in gestione)
- **Annullato** (solo per Progetti precedentemente in Negoziazione, per i quali la negoziazione non va a buon fine)
- **Chiuso** (solo per Progetti che non hanno date inizio/fine). Tali progetti vengono 'chiusi' quando terminal'attività. 

.. _piano-economico:

Piano economico di un Progetto
==============================

Il Piano economico di un progetto deve essere obbligatoriamente indicato quando la Tipologia di Finanziamento lo richiede. 
Successivamente alla sua definizione esso può essere utilizzato per consultare la 'Scheda Progetto' con tutta la situazione contabile consuntiva alla data, e può essere modificato, dopo l'approvazione della scheda Progetto, attraverso le rimodulazioni.

Gli importi che si vanno a definire sul Piano Economico rappresentano gli importi che si potranno stanziare sul progetto (attraverso previsioni di bilancio che diventano stanziamenti e attraverso variazioni e storni di bilancio), ripartiti per voci economiche (che raggruppano voci finanziarie di spesa). Gli importi Totali del piano economico si distinguono in Importo Finanziato (Fonti esterne) e Importo Cofinanziato (Fonti interne). Gli importi Finanziato e Cofinanziato rappresentano naturalmente anche le entrate che ci si aspetta di avere per il Progetto dai finanziatori esterni o da risorse provenienti da altri Progetti, girocontate solo dopo la chiusura di qusti ultimi.
Se il progetto viene creato in fase previsione di bilancio gli importi indicati in previsione per il Progetto devono rispettare quanto definito sul piano economico del progetto:

- Le voci finanziarie indicate nel piano economico per l'anno di previsione sono le uniche utilizzabili sul bilancio di previsione;
- Gli importi di previsione devono essere inferiori o uguali agli importi indicati nel piano economico.

Se il Progetto nasce nel corso dell'anno finanziario dovranno essere operate variazioni di bilancio per alimentare gli stanziamenti necessari, se le attività contabili iniziano nello stesso anno di gestione.

Il Piano economico del Progetto è compilabile, tramite l’apposita tab, direttamente sull'anagrafica progetto, ed è strutturato nel modo seguente: 
 
- Riepilogo Importi del Progetto: Totale, Ripartito e Da Ripartire (consultazione posta in alto e sempre visibile in fase di gestione del piano economico);
- Totali Riepilogativi del Progetto. E' una sezione di sola consultazione che riporta due tipi di riepilogo dell'intero progetto: **Totali per Voce Piano Economico** (al di là della ripartizione pruriennale degli importi, i totali rappresentano gli importi per Voce del Piano o categoria economica utilizzate nella ripartizione) e **Totali per Esercizio** (al di là della ripartizione per voci economiche in questo caso i totali rappresentano la somma degli importi del progetto ripartiti per esercizio contabile).
- Ripartizione importi per Voce economica riferita all’anno di gestione; 
- Ripartizione importi per Voce economica riferita agli altri anni del progetto (precedenti e successivi all’anno di gestione). 

**Ripartizione per voce economica - Anno di gestione**

La ripartizione degli importi per l’anno di gestione (o anno di scrivania, o anno di accesso) richiede l'indicazione delle voci economiche del Progetto (o categorie economiche) e per ogni Voce economica, l’elenco (nella sezione sottostante) delle voci finanziarie associate.
Nella prima sezione si indicano le voci del piano economico selezionandole da una lista precaricata: :ref:`voce-del-piano-economico` Nella seconda sezione si indicano le voci finanziarie che si prevede di utilizzare in fase di previsione e variazioni/storni (praticamente le voci finanziarie su cui potranno essere posti gli stanziamenti di bilancio).
Per alcune categorie le voci finanziarie da utilizzare sono obbligatorie, peraltre categorie vanno selezionate dall'elenco voci del Piano Finanziario per l'anno di riferimento. 
Il vincolo funzionale più importante, da tener presente nella compilazione del Piano economico del Progetto, è che una voce finanziaria può essere associata una sola volta al progetto (quindi associata ad una sola voce economica indicata sul progetto). 
L'associazione di ulteriori voci finanziarie può essere fatta anche successivamente alla creazione del piano economico, la cosa importante è che durante la compilazione del bilancio di previsione tutte le voci per le quali indicare gli importi di previsione per il progetto specifico, siano presenti sul piano economico del progetto stesso. L'aggiunta di voci finanziarie al piano economico, durante l'anno finanziario, può avvenire senza effettuare la :ref:`rimodulazione-progetto` eccetto i casi in cui si vadano a modificare contemporaneamente gli importi di stanziamento per il Progetto e quindi per le voci rconomiche/finanziarie collegate.
Dopo aver completato la ripartizione totale degli importi per le voci del piano e per gli anni del progetto, sarà possibile rendere il progetto APPROVATO e sarà possibile utilizzarlo nelle successive gestioni contabili.

**Ripartizione per voce economica - Anni precedenti e successivi**

Ci sono diverse modalità per modificare nelcorso dell'anno di gestione gli importi indicati sul piano economico di un progetto (che vedremo in dettaglio sulle rimodulazioni e sulle variazioni/storni). In sintesi:

- Spostamento stanziamennti all'interno del Progetto:
  - Tra voci finanziarie della stessa categoria economica del Progetto (no rimodulzione, no variazioni);
  - Tra voci finanziarie di categorie economiche diverse del Progetto (nel rispetto del limite previsto per le categorie economiche: no rimodulazione, si variazioni. Oltre i limiti delle ctegorie economiche: si rimodulazioni, si variazioni);
  - Aumento importi per il progetto (si rimodulazione se oltre limiti delle voci economiche, si variazioni di maggiori entrate e maggiori spese);
  - Diminuzione importi per il progetto (si rimodulazione, si variazioni di minori entrate e minori spese);
- Spostamento importi oltre i limiti dell'utilizzato (impegni e trasferimenti): non consentito;
- Spostamento importi da un progetto ad un altro (solo se il primo è chiuso, tramite la gae specifica di natura 6, si rimodulazione se oltre limiti delle voci economiche, si variazioni) 

**Voce Speciale**

Importante nota da tener presente nella gestione successiva del Piano Economico del Progetto è la Voce Speciale. Questa Voce è definita parametricamente (equivale alla Voce di spesa per il Personale a tempo indeterminato) e consente lo spostamento, tramite la GAE di natura 6, di soldi su altre voci di spesa del Piano economico prima ancora che il Progetto sia scaduto. Lo spostamento di fondi tramite la Voce Speciale deve avvenire nell'ambito dello stesso progetto e solo attraverso la GAE di natura 6.
Alla Voce Speciale non è consentito invece attribuire soldi attraverso le Variazioni di Bilancio.

.. _rimodulazione-progetto:

Rimodulazione Progetto
======================

La rimodulazione di un Progetto riguarda esclusivamente Progetti già esistenti (creati in fase di Pdgp oppure creati nel corso dell'anno contabile di riferimento). Il nuovo progetto viene creato come indicato  al paragrafo :ref:`Progetto` e solo dopo la sua Approvazione segue le regole di Rimodulazione uguali per tutti i Progetti.
Con l’utilizzo della funzionalità di rimodulazione dei Progetti, sono inibite tutte le modifiche direttamente sulla scheda progetto normalmente consentite. Tutte le rimodulazioni devono essere operate tramite l’apposita funzione per storicizzare tutte le informazioni e guidare l’utente per il controllo degli importi definiti sul piano economico del progetto.
E’ presente, nella mappa di gestione Progetti, per i Progetti Approvati e con Piano Economico, il pulsante ‘Rimodulazione’. 
Digitando questo pulsante l’utente entra in una nuova funzionalità dove vede proposti tutti i dati della scheda progetto in linea, con la possibilità di apportare modifiche. 

Solo la UO coordinatrice del progetto può effettuare le rimodulazioni, nel rispetto delle attività finanziarie già svolte sul Progetto stesso.
Solo i Progetti Approvati, che hanno Piano Economico specificato, possono essere rimodulati. 
Le operazioni possibili in fase di rimodulazione sono dettagliate di seguito. Gli stati della rimodulazione seguono le attività dell'utente e le relative validazioni da parte degli utenti abilitati a tale funzione.

**Gestione Data Proroga**
In fase di rimodulazione un progetto può essere prorogato operando appunto sulla ‘Data Proroga’. Specificando questa data viene obbligatoriamente richiesto un allegato di tipo ‘Proroga’ e se non si operano ulteriori rimodulazioni di importi (e variazioni collegate), al salvataggio definitivo da parte dell’utente, e dopo la validazione da parte della Sede Centrale, la Rimodulazione in oggetto diviene immediatamente Approvata.

Per la gestione della rimodulazione del piano economico di un progetto, le modifiche possono riguardare:
-	Aumento/Diminuzione importi Finanziati/Cofinanziati di un progetto (Totali e di conseguenza per categoria economica);
-	Modifica della ripartizione degli importi Finanziati/Cofinanziati, precedentemente effettuata, tra categorie economiche ed anni di gestione del progetto;
-	Associazione di nuove categorie economiche al progetto
-	Eliminazione di categorie precedentemente associate al progetto

Alcune delle modifiche elencate richiedono obbligatoriamente l’associazione di una Variazione di Bilancio affinchè la rimodulazione stessa possa essere approvata, come vedremo meglio in seguito.
L’eliminazione di categorie economiche potrebbe richiedere, sul piano economico del progetto rimodulato, la modifica dell’associazione di voci di bilancio (utilizzate) in modo da spostarle da una categoria economica eliminata ad una categoria aggiunta sul progetto.
Chiaramente le rimodulazioni, quando vengono salvate in definitivo e poi approvate, devono rispettare quanto già ‘utilizzato’ dal Progetto e devono garantire la congruenza tra l’importo Finanziato/Cofinanziato Rimodulato e l’importo ‘assestato’ del Progetto, che tiene conto delle eventuali variazioni associate alla rimodulazione stessa.
Il salvataggio definitivo della rimodulazione, richiede obbligatoriamente un allegato di tipo ‘Rimodulazione’. Questo per ogni Rimodulazione operata sul progetto (il nome del file riporterà automaticamente il tipo allegato e il numero rimodulazione del progetto).

Alla scheda progetto è consentito allegare altri file, oltre quelli specifici per la rimodulazione, di tipologie predefinite e presenti direttamente sulla scheda progetto:

-	Provvedimento di costituzione;
-	Proroga (Allegato alla rimodulazione);
-	Richiesta di anticipo;
-	Rimodulazione (Allegato alla Rimodulazione);
-	Rendicontazione;
-	Stralci;
-	Controdeduzioni;
- Final Statement payment
-	Generico.

Viene inoltre prodotto automaticamente un pdf per la singola rimodulazione, in fase di salvataggio definitivo, e viene allegato come storico delle operazioni effettuate.
Tutti i file, prodotti o allegati dall’utente, legati alla Rimodulazione o alla Scheda Pogetto, vengono resi disponibili direttamente sulla scheda progetto.

Sulla mappa dei Progetti, in alto a destra, viene indicato sempre l’ultimo numero dell’eventuale rimodulazione in corso/approvata (Ver. – Numero rimodulazione per progetto – stato rimodulazione: P-Provvisoria, D-Definitiva, A-Approvata) e si potrà accedere solo all’ultima rimodulazione provvisoria per completarla, per eliminarla o per renderla definitiva fino a che questa non viene Approvata. Dopo l’approvazione non si può più accedere alla rimodulazione perché questa sostituisce completamente la scheda Progetto in linea.
Ad ogni rimodulazione, inoltre, viene assegnato un numero progressivo (progressivo Ente) che individua univocamente la rimodulazione effettuata. 
Il progressivo viene assegnato al primo salvataggio della rimodulazione da parte dell’Istituto.
Entrando nella funzione di rimodulazione di un Progetto, la maschera presenta in prima istanza gli importi uguali alla scheda progetto di provenienza. Sulla prima tab della funzione (Testata), dove sono indicati i dati generali del progetto, sono modificabili solo gli importi Finanziato e Cofinanziato e la data Proroga.
Gli importi modificati vengono riportati nella seconda Tab della funzione di rimodulazione (Piano Economico) con l’evidenza della differenza da distribuire/diminuire sulle voci economiche e tra gli anni del Piano.
Per effettuare le modifiche di dettaglio bisogna entrare sulle singole categorie economiche, sull’anno in cui si vuole effettuare la modifica. Le modifiche possono essere operate sia su una categoria esistente, sia su una categoria che si aggiunge a quelle già collegate al progetto. Si dovranno ripartire gli importi aggiunti o diminuire gli importi in meno, fino ad avere una completa ripartizione dei nuovi importi previsti per ilprogetto.
In ogni momento è possibile consultare le ‘quote correnti’, cioè gli importi presenti sulla scheda progetto prima della rimodulazione utilizzando il ceck posto in alto accanto al riepilogo del progetto.

Le modifiche possibili in questa fase possono essere di vario tipo, ad esempio:

-	Inserire una nuova categoria e assegnargli gli importi aggiunti sul totale Finanziato/Cofinanziato;
-	Inserire gli importi in aumento su anni diversi dalla competenza;
-	Diminuire gli importi Finanziato/Cofinanziato e di conseguenza distribuire su una o più categorie economiche, o su anni diversi, l’importo in diminuzione;
-	Eliminare una categoria erroneamente definita in precedenza sul piano economico;
-	Altre modifiche …..

La mappa della Rimodulazione in corso presenterà, per una lettura agevolata, la scheda progetto con:

-	Evidenziato in grassetto le modifiche apportate;
-	Evidenziato in rosso le anomalie per mancata quadratura importi;
-	Cancellazione visibile per righe eliminate con possibilità di ripristino;
-	Possibilità di visualizzare importi correnti, cioè importi prima delle modifiche effettuate in rimodulazione.

**Elimina/aggiungi voci finanziarie alla categoria**
In alcuni casi (in particolare diminuzioni di importi per categoria economica oppure spostamento importi assegnati da una categoria economica ad un’altra), bisogna fare attenzione alle voci di bilancio collegate alla categoria su cui si opera perché, premesso che l’importo rimodulato del Progetto deve sempre essere maggiore o uguale all’assestato delle voci collegate (assestato = stanziamenti di bilancio + variazioni. Tra le variazioni non vengono considerate quelle di trasferimento alle AREE o al personale perché rappresentano l’utilizzato, insieme agli Impegni), si possono verificare casistiche diverse:

-	Rimodulazione Importo Finanziato/Cofinanziato con diminuzione dell’importo per categoria fino a scendere sotto l’importo assestato delle voci collegate. In questo caso le voci di bilancio che assumerebbero importo assestato negativo, possono essere spostate sotto un’altra categoria economica con importo Finanziato/Cofinanziato capiente. 

-	Rimodulazione dell’importo Finanziato/Cofinanziato e diminuzione conseguente dell’importo per categoria economica fino a scendere sotto l’importo  assestato. In questo caso occorre predisporre contestualmente una variazione di bilancio.

Chiaramente l’eliminazione di una categoria economica, con la conseguente variazione negativa della voce finanziaria collegata, è possibile solo nel caso in cui l’importo assestato non fosse già stato utilizzato.
Queste ultime casistiche riguardano esclusivamente l’anno di competenza e i residui, ma non riguardano gli anni successivi alla competenza.
Tutte le rimodulazioni che operano aumenti per l’importo finanziato/cofinanziato, con relativo aumento di importi su categorie economiche e/o aumenti per anni di gestione del progetto, non richiedono nessuna variazione obbligatoria.

**Processo di Approvazione della Rimodulazione**
La gestione della rimodulazione prevede un processo di controllo e di approvazione prima di essere operativa per il progetto specifico. Dopo il salvataggio ‘definitivo’ da parte dell’utente, la rimodulazione deve essere ‘validata’ dall'Ufficio amministratore dei progetti e poi approvata. Possiamo riepilogare gli stati della rimodulazione in:

-	‘PROVVISORIA’ – l’utente inizia a preparare la rimodulazione ma ancora non effettua un ‘salva definitivo’ perché potrebbe completare il lavoro in un momento successivo. In questa fase non saranno effettuati tutti i controlli di quadratura necessari al salvataggio definitivo della rimodulazione. In questa fase viene assegnato il numero Progressivo Rimodulazione generale.  

-	‘DEFINITIVA’ – l’utente completa la rimodulazione ed effettua il ‘Salva definitivo’. In questa fase vengono operati tutti i controlli di quadratura necessari per mandare alla validazione la rimodulazione. Viene inoltre richiesto obbligatoriamente un documento che attesti la rimodulazione (il file allegato deve essere di tipo “rimodulazione”). Se l’operazione di modifica ha riguardato solo o anche la Data Proroga, viene richiesto un allegato di tipo ‘Proroga’. 

-	‘VALIDATA’ – Dopo il Salvataggio Definitivo da parte dell’Istituto, la rimodulazione è visibile e gestibile da chi amministra centralmente i Progetti che appunto la verifica e la valida prima che il processo prosegue, eventualmente, nelle fasi successive. Se la Rimodulazione non dovesse richiedere la creazione di Variazioni di bilancio, alla validazione lo stato diventa direttamente APPROVATA. Nel caso invece, fossero richieste variazioni, lo stato passa a VALIDATA. La Sede Centrale in questa fase potrebbe decidere, invece di APPROVARE, anche di RESPINGERE la rimodulazione specificando le motivazioni (Note). In questo caso resta storicizzata la rimodulazione respinta, e il proponente deve riproporre una nuova rimodulazione.


-	‘APPROVATA’ –  Per le rimodulazioni Validate il proponente prepara e gestisce normalmente le variazioni obbligatorie indicando il numero di rimodulazione (Progetto/Numero rimodulazione) a cui si riferisce. La Variazione, quando necessaria,  è obbligatoria al passaggio in definitivo della variazione stessa. Le variazioni associate alla rimodulazione possono essere più di una, sempre riferite allo stesso progetto, ad esempio perché la rimodulazione ha riguardato sia la competenza che il residuo. Solo nel momento in cui tutte le variazioni collegate alla specifica rimodulazione saranno approvate, automaticamente anche la rimodulazione risulterà APPROVATA. In questo modo vengono garantiti tutti i controlli di congruenza tra l’importo Finanziato/Cofinanziato Rimodulato e l’assestato del Progetto.

**1.4	Variazioni di bilancio collegate alla Rimodulazione**
In alcuni casi, come abbiamo visto al paragrafo precedente, la Rimodulazione richiede una o più variazioni di bilancio, che una volta approvate rendono approvata anche la rimodulazione. I casi in cui è richiesta la variazione sono, fondamentalmente, i casi in cui diminuisce l’importo Finanziato/Cofinanziato fino a determinare anche la diminuzione dell’importo assestato per le voci collegate al progetto stesso e alla categoria economica oggetto di rimodulazione.
Le variazioni ‘obbligatorie’, devono essere predisposte (da una delle UO che partecipano al progetto), devono essere di tipologia ‘Rimodulazione’ e devono essere rese definitive agganciandole alla rimodulazione, dopo che questa è stata Validata da parte della sede centrale.
In fase di salvataggio definitivo della variazione se questa si riferisce ad importi rimodulati del piano economico del progetto, è obbligatoriamente richiesta l’associazione alla rimodulazione, altrimenti è inibito il salvataggio definitivo. 
Per rispettare la congruenza degli importi del piano economico di un progetto, rispetto all’importo assestato del Progetto stesso, sono stati introdotti alcuni controlli:

-	Non si possono rendere definitive variazioni se c’è una rimodulazione in corso (non approvata) per il progetto in questione
-	Non possono essere salvate in definitivo rimodulazioni per un progetto presente in variazioni non ancora approvate
-	Nella variazione collegata alla rimodulazione (quindi con tipologia ‘Rimodulazione’) deve esserci un solo Progetto
-	Le variazioni collegate alla rimodulazione posso essere sia a competenza che a residuo (o contemporaneamente collegate alla stessa rimodulazione).

**Consultazione Rimodulazione**
Gli utenti amministratori dei Progetti, abilitati alla validazione delle rimodulazioni, potranno accedere direttamente dalla UO Ente visualizzando tutte le rimodulazioni da approvare, oppure tutte le rimodulazioni in un determinato stato per una specifica Unità Organizzativa (funzione ‘Consultazione Rimodulazioni’  a menù). Tra i dati visualizzati è indicata la UO e lo Stato.

Tramite la stessa funzionalità, ogni Unità Organizzativa potrà consultare le sue rimodulazioni per stato.


