=======================================================
Gruppo di Azioni Elementari - GAE (o Linea di Attività) 
=======================================================

Le Linee di Attività o Gruppo di Azioni Elementari (di seguito chiamatate GAE), individuano la ripartizione di un Progetto in Sottoprogetti. Queste rappresentano l'unità di dettaglio utilizzata nella gestione contabile e possono suddividere il progetto secondo vari criteri di ripartizione (possono rigurdare le risorse del progetto divise tra più rcercatori, oppure raggruppare risorse per reali sottoprogetti di cui si vuole tener traccia). Le GAE vengono gestite, in pratica, secondo le informazioni di dettaglio che si vogliono ricavare in fase di rendicontazione o consuntivazione del Progetto in senso lato e rappreentano una suddivisione obbligatoria del Progetto (per ogni progetto è necessario creare almeno una GAE di Entrata/Spesa/Entrambi).

Linea di attività Propria
-------------------------

La GAE si definisce 'propria' quando è assegnabile ad un solo cdr. L'altra modalità di creazione di una GAE è indicata al paragrafo :ref:`Linea-di-attività-comune`.

La GAE presenta elementi di testata e di dettaglio. I dati da specificare sono i seguenti:

- Codice: accoglie valori numerici. Il sistema, automaticamente, pone un prefisso e tanti zeri in modo da formare un codice del tipo ‘P0000001’ (in questo caso l’utente aveva messo 1 nel campo codice). L’utente può non inserire alcun valore nel codice che viene, in questo caso, derivato in automatico. Una volta specificato il codice la creazione di successive GAE non propone più il codice in automatico ma bisogna sempre specificarlo.
-  Tipo linea di attività (solo mostrato al salvataggio se propria). Il tipo linea è una codifica di sistema che indica, sostanzialmente, se sia di tipo propria oppure comune. Siccome le due tipologie vengono create da funzionalità diverse, questa viene assegnata automaticamente dal sistema (in questo caso la GAE è sempre propria).
-  Progetto di riferimento per la GAE.
-  Cdr: indica il cdr del quale la GAE diventerà dipendente. E’ possibile ricercare il cdr secondo le solite modalità di ricerca (ricerca e ricerca guidata). I cdr visualizzabili dipendono dal livello del cdr configurato all’utenza: vengono mostrati il cdr di appartenenza più tutti quelli, se ci sono, a lui afferenti.
-  Insieme: indica il codice dell'insieme cui la GAE entra a far parte. L’insieme è un elemento che raccorda una linea di attività di entrata a una o più linee di attività di spesa. L’insieme è valido solo all’interno di un cdr. L’inserimento di un insieme è possibile attraverso l’apposita funzione :ref:`insieme`



- Gestione: indica se la linea è di entrata o di spesa
- Funzione: solo per le linee di attività di spesa indica su quale funzione è possibile usare questa Gae.
- Natura: indica su quale natura è possibile utilizzare questa linea di attività. Anche le nature sono tabellizzate.
- Gruppo: è facoltativo scegliere se raggruppare la linea di attività attraverso questo attributo utile a una eventuale visualizzazione per gruppi di linee di attività.
- Denominazione: indicare il nome Gae.
- Descrizione: indicare un eventuale descrizione che specifica meglio la Gae.
- Esercizio di terminazione: valorizzare l’esercizio dal quale la Gae non è più attiva.
- Controllo limite di spesa: è possibile scegliere se la Gae in questione è soggetta al controllo dei limiti di spesa indicati sulla voce di bilancio/Fonte utilizzati durante le movimentazioni finanziarie.
- Una Gae si riferisce sempre ad una Anagrafica Programma e ad una Anagrafica Missione

Attraverso la seconda tab, prevista dalla funzione di creazione Gae, è possibile accedere al pannello dei risultati.
Qui è possibile inserire più di un risultato. Per inserire un risultato cliccare sull’iconcina 'nuovo' posta in basso a sinistra dell’area ‘risultati’. Se si inserisce un risultato tutti i campi seguenti diventano obbligatori per quel risultato.

A questo punto è possibile, per ogni obiettivo:

- inserire un tipo risultato: scegliere da una tabella se raggruppare il risultato in un insieme predefinito;
- Descrizione: inserire la descrizione dell’obiettivo (si possono utilizzare i comandi copia-incolla per utilizzare un file esterno).
- Quantità: utilizzare nel caso che sia un elemento determinante.

.. _linea-di-attivita-comune:

Linea attività comune
---------------------

La modalità di creazione di una Gae comune si articola in due passi: il primo per creare l'anagrafica 'generale' della Gae ed il secondo per specificare i cdr che la utilizzeranno.

Per creare un'anagrafica Gae comune bisogna specificare i seguenti dati:

- codice: è assegnato in modo automatico dal sistema.

- Descrizione: nome Gae.

- Gestione: indica se la Gae sarà di entrata o di spesa (o entrambe).

- Funzione (solo per le spese) e la natura. 

Al salvataggio è possibile agire sul secondo tab e aggiungere i cdr che utilizzeranno la Gae. 

Se alcuni cdr sono già associati (modifica) il pannello presenta i soli cdr associati.
In caso contrario occorre dapprima cercare i cdr che possono essere associati alla Gae. Dalla lista di tutti i cdr associabili bisogna selezionare il cdr (o utilizzare la funzione 'seleziona tutti') per associarli alla Gae.

Il pannello è ordinabile per tutti gli elementi identificativi dei cdr. E’ possibile anche fare una ricerca guidata all’interno dei cdr associati. E’ altresì possibile disassociare i cdr selezionandoli oppure selezionare tutti i cdr ed 'eliminare' l'associzione.

Al salvataggio, il sistema crea automaticamente l'anagrafica Gae per tutti i cdr indicati e vengono ereditate tutte le informazioni dell'anagrafica Gae 'comune' specificata prima. Come codice viene creato il numero Gae con una C di prefisso. Ad esempio C000034.

Ogni responsabile della configurazione dei cdr potrà, se vuole, entrare in normale modifica delle Gae e valorizzare gli obiettivi e l’insieme. Non è possibile modificare altri valori ereditati.

Se si creano nuovi cdr essi devono essere associati alle Gae comuni entrando in modifica delle stesse. 

.. _insieme:

Insieme
-------

L'insieme di GAE  è un codice di raggruppamento di GAE secondo logiche varie, utili al solo fine della consultazione. L'insieme lega una Gae di entrata a una o più Gae di spesa, oppure lega più Gae di spesa.
Uno stesso insieme non può essere assegnato, quindi, a due linee di attività parte entrate. Se l’insieme è assegnato a una linea di attività parte entrate, le linee di attività parte spese ereditano obbligatoriamente la natura della linea di attività parte entrate.

.. _programmi:

Programmi
---------
La gestione dell'anagrafica Programmi consente di censire tutte le informazioni necessrie a definire un Programma di ricerca. Un Programma rappresenta un aggregato omogeneo di attività volte a perseguire le finalità individuate nell’ambitodella singola missione, cui il programma si riferisce.

.. _missioni:

Missioni
--------
La gestione dell'anagrafica Missioni consente di censire tutte le informazioni necessrie a definire una Missione che l'Ente deve perseguire. Le  missioni  esprimono  le  funzioni  principali  e  gli  obiettivi  strategici perseguiti dalle amministrazioni, utilizzando risorse finanziarie, umane e strumentali ad esse destinate.

