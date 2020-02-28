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
- **Negoziazione** (consentito solo per Tipologie Progetto ‘Finanziamento’ e ‘Cofinanziamento’ e utile per effettuare la previsione)
- **Approvato** (necessario per poter effettuare previsione e/o gestione)
- **Annullato** (solo per Progetti precedentemente in Negoziazione, e poi non più usati)
- **Chiuso** (solo per Progetti che non hanno date inizio/fine). Per la chiusura di un progetto sarà utilizzata la data fine per poter memorizzare la data in cui viene chiuso. 

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
Nella prima sezione si indicano le voci del piano economico selezionandole da una lista precaricata: :ref:`voci-del-piano-economico` Nella seconda sezione si indicano le voci finanziarie che si prevede di utilizzare in fase di previsione e variazioni/storni (praticamente le voci finanziarie su cui potranno essere posti gli stanziamenti di bilancio). 
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



Rimodulazione Progetto
======================
