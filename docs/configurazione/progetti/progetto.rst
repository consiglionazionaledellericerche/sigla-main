========
Progetto
========

La gestione dell'Anagrafica Progetti consente di gestire tutte le informazioni generali di un progetto, sia esso di ricerca che di funzionamento, e tutte le informazioni contabili di cui tener conto nelle funzionalità successive di previsione e gestione.
Un Progetto si riferisce sempre ad una :doc:`Area Progettuale<area_progettuale>`, ed è inserito, attraverso le linee di attività o GAE, in una Missione/Programma specifici. Il riepilogo per :ref:`Missioni` e :ref:`Programmi`, rappresenta un allegato obbligatorio del Bilancio di Previsione dell'Ente.
L'anagrafica Progetti gestita da Sigla rappresenta l'anagrafica 'contabile' contenente tutte le informazioni utili alle successive gestioni contaili. In alcuni casi, come per il CNR, questa rappresenta un completamento all'anagrafica 'scientifica' dei Progetti gestita in altre procedure per aspetti che non riguardano i dati contabili. A questo scopo parametricamente si definisce se questa anagrafica è gestita interamente in Sigla oppure è inibita la creazione di un progetto perchè proveniente da altra applicazione.
I progetti devono essere obbligatoriamente censiti e in stato 'APPROVATO', vedi :ref:`Stati del Progetto`, per poter gestire il Bilancio di Previsione dell'ente. Naturalmente nel corso dell'anno contabile possono essere creati nuovi Progetti e utilizzati creando i necessari presupposti. 

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

Piano economico di un Progetto
==============================

Il Piano economico di un progetto deve essere obbligatoriamente indicato quando la Tipologia di Finanziamento lo richiede. 
Successivamente alla sua definizione esso può essere utilizzato per consultare la 'Scheda Progetto' con tutta la situazione contabile consuntiva alla data, e può essere modificato, dopo l'approvazione della scheda Progetto, attraverso le rimodulazioni.

Il Piano economico del Progetto è compilabile tramite l’apposita tab direttamente sull'anagrafica progetto, ed è strutturato nel modo seguente: 
 
- Riepilogo Importi del Progetto Totale, Ripartito e Da Ripartire (consultazione posta in alto e sempre visibile in fase di gestione del piano economico);
- Totali Riepilogativi del Progetto. E' una sezione di sola consultazione che riporta due tipi di riepilogo dell'intero progetto: **Totali per Voce Piano Economico** (al di là della ripartizione pruriennale degli importi, i totali rappresentano gli importi per Voce del Piano o categoria economica utilizzate nella ripartizione) e **Totali per Esercizio** (al di là della ripartizione per voci economiche in questo caso i totali rappresentano la somma degli importi del progetto ripartiti per esercizio contabile).
- Ripartizione importi per Voce economica riferita all’anno di gestione, o anno di scrivania; 
- Ripartizione importi per Voce economica riferita agli altri anni del progetto (precedenti e successivi all’anno di gestione, o anno di scrivania). 

Stati del Progetto
==================
A seconda della Tipologia di Finanziamento un Progetto può essere utilizzato in Previsione, in Gestione o entrambi, solo se ha un determinato Stato. I valori che può assumere lo Stato sono: 

- **Iniziale** (assegnato automaticamente ai Progetti nuovi). Su questi progetti non è possibile operare nè in Previsione nè in Gestione
- **Negoziazione** (consentito solo per Tipologie Progetto ‘Finanziamento’ e ‘Cofinanziamento’ e utile per effettuare la previsione)
- **Approvato** (necessario per poter effettuare previsione e/o gestione)
- **Annullato** (solo per Progetti precedentemente in Negoziazione, e poi non più usati)
- **Chiuso** (solo per Progetti che non hanno date inizio/fine). Per la chiusura di un progetto sarà utilizzata la data fine per poter memorizzare la data in cui viene chiuso. 


Rimodulazione di un Progetto
============================
