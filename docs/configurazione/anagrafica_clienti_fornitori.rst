.. include:: /.special.rst

============================
Anagrafica Clienti/Fornitori
============================

La scheda illustra la modalità di gestione dell’anagrafica dei clienti e dei fornitori. L’anagrafica si compone di **due livelli** :
il primo individua l’anagrafica generale, il secondo livello invece individua i :doc:`Terzi<terzi>` appartenenti all’anagrafica generale.
I terzi appartenenti possono essere più di uno.

Nella pratica questa articolazione su due livelli consente di registrare l'anagrafica di alcuni enti che hanno diverse sedi dislocate sul territorio, ma che operano come un'unica entità giuridica,
come ad esempio il CNR che ha un'unica partita iva e codice fiscale ma si articola in più strutture ognuna delle quali ha il proprio indirizzo,i propri recapiti e così via.
All'interno della procedura Sigla l'inserimento di un'anagrafica simile a quella del CNR dovrà essere effettuata registrando l'anagrafica generale dell'ente ed un terzo per ogni struttura.
E' evidente che per le persone fisiche dovrà essere registrata un'unica anagrafica ed un unico :doc:`Terzo<terzi>`.
Ogni anagrafica è distinta da un codice numerico univoco (`Codice Anagrafico`_), tale codice è assegnato automaticamente dalla procedura al momento del salvataggio.
Il livello Anagrafico inoltre, si articola in 5 pannelli: Anagrafica – `Rapporto`_ – `Carichi Familiari`_ – `Dettagli`_ – `Pagamenti Esterni`_.

Il pannello dell'anagrafica è diviso in 4 sezioni:
 
- Tipologia: `Persona Fisica`_ , `Persona Guiridica`_, `Ente Pubblico`_, etc...;
- Informazioni Anagrafiche: nome, cognome, ragione sociale, codice fiscale, partita iva ecc... Ovviamente i campi non sono sempre tutti obbligatori, l'obbligatorietà dipende dalla tipologia di anagrafica che si sta inserendo;
- Sede legale/Domicilio Fiscale: comune, indirizzo di residenza;
- Dati Anagrafici: luogo e data di nascita. Ovviamente tale sezione non è presente al momento dell'inserimento di una persona guiridica.

Persona Fisica
================
Le persone fisiche possono essere **italiane** o **estere**, inoltre quando si registra l'anagrafica di un soggetto bisogna indicare se si tratta di una **ditta individuale** o **altro**.
Alcune persone fisiche, se titolari di partita iva, devono essere targati come **soggetto iva**, questo attributo comporta l'inserimento obbligatorio della partita iva del soggetto oltre al codice fiscale che invece lo è sempre.
La **Tipologia Istat** è un dato indispensabile ai fini dell'associazione ai codici :doc:`Siope<siope>`  nell'emissione dei :doc:`Mandati<mandati>`  e delle :doc:`Reversali<reversali>`.
L'anagrafica di una persona fisica, prevede anche l'inserimento di dati nel tab `Rapporto`_, `Dettagli`_, `Carichi Familiari`_ e `Pagamenti Esterni`_, qualora sia necessario.

Persona Fisica Cervellone
=========================
I soggetti che possono usufruire delle agevolazioni fiscali previste per i ricercatori italiani i stranieri rientrati dall'estero (rientro dei cervelli)   devono essere identificati con l'apposito flag.
Per una corretta applicazione delle agevolazioni è necessario inserire la data dalla quale il soggeto dichiara di essere residente in Italia - ''data inizio res/dom. in Italia'' - infatti solo dopo aver inserito tale data,
la procedura è in grado di calcolare il periodo di imposta dal quale è possibile l'applicazione delle suddette agevolazioni - ''Anno inizio redidenza fiscale''.
Se il soggetto dichiara di essere residente in Italia per la maggior parte del periodo di imposta (183 giorni) allora il soggetto può usufruire della agevolazioni fiscali per il medesimo periodo
Se il soggetto dichiara di essere residente in Italia per un periodo inferiore ai 183 giorni, allora tale soggetto potrà usufruire della agevolazioni fiscali solo a partire dal periodo d'imposta successivo al suo rientro.

Per ulteriori informazioni, fiscali o previdenziali, sulla gestione dei "Cervelloni" consultare le seguenti circolari:

- `Circolare Direttiva della Ragioneria della SAC N.Reg RagSac 244/2006 del 27 ottobre 2006 <http://www.ittig.cnr.it/BancheDatiGuide/circolari/c06-30aC.pdf>`_
- `Circolare Direttiva della Ragioneria della SAC N.Reg RagSac 83/2009 del 29 gennaio 2009 <http://www.ittig.cnr.it/BancheDatiGuide/circolari/c09-03aA.pdf>`_

Persona Guiridica
=================
Il caricamento dell'anagrafica di una persona guiridica prevede obbligatoriamente l'inserimento del codice fiscale e della partita iva del soggetto in questione. Per questa tipologia di anagrafica è possibile,
qualora sia necessario, valorizzare solo il tab `Rapporto`_.
Una Persona Giuridica deve essere ulteriormente classificata in:
- Ente Pubblico;
- Gruppo IVA;
- Altro.

Ente Pubblico
=============
Particolare importanza assume la definizione di un'anagrafica di un Ente Pubblico, ai fini dell'emissione fatture attive, perchè dal 31 Marzo 2015 è obbligatorio emettere verso tali enti esclusivamente fatture elettroniche.
La procedura periodicamente accedendo all'Indice delle Pubbliche Amministrazioni (IPA), rileva in automatico il Codice Amministrazione e la data di avvio Fatturazione elettronica e lo associa all'anagrafica di Sigla mediante il Codice Fiscale presente sull'anagrafica stessa. Sarà compito dell'utente, invece, specifare sul Terzo di tali anagrafiche, il CUU corretto (richiesto prima dell'emissione di fatture attive). Tale informazione consente di creare Documenti attivi elettronici che, dopo essere stati opportunamente firmati, vengono automaticamente inviati alla Piattaforma SDI.

Gruppo IVA
===========
Con la circolare 19/E/2018 l’Agenzia delle entrate si è soffermata sulle novità del Gruppo Iva, fornendo importanti chiarimenti.
Il Gruppo Iva sorge a seguito di un’opzione, vincolante per un periodo predefinito, in forza della quale viene istituito un autonomo soggetto passivo d’imposta. Si rimanda alla Circolare relativa, emanata dall’Ufficio Fiscale, per quanto riguarda la funzione e gli obblighi del Gruppo IVA.
Si ricorda che il gruppo iva ha la caratteristica di essere una unica partita iva e l'identificazione per ogni componente che aderisce al gruppo avviene tramite il codice fiscale. 
Per questo motivo le fatture elettroniche ricevute riportano la partita IVA del Gruppo (appositamente costituito con Atto formale come un nuovo soggetto giuridico) ed il Codice Fiscale del prestatore che ha precedentemente aderito al Gruppo IVA. 
Le fatture attive vengono emesse verso i singoli componenti del Gruppo e riportano la partita IVA del gruppo ed il codice fiscale del prestatore.

Le implementazioni effettuate in Sigla hanno coinvolto l’Anagrafica clienti/fornitori per la qualifica dell’anagrafica stessa e per l’indicazione dei componenti che aderiscono al Gruppo IVA. La prima operazione da fare in Sigla è creare la nuova anagrafica del Gruppo IVA e qualificarla come tale. E’ obbligatorio indicare anche il Periodo di validità del Gruppo IVA così come indicato nell’atto di costituzione.
Successivamente bisogna associare i componenti (anagrafiche sigla) al Gruppo IVA attraverso l'apposita Tab aggiunta nella funzione di anagrafica. In questa nuova sezione è possibile gestire i componenti del Gruppo IVA oppure è possibile dall’anagrafica del componente indicare il suo legame al Gruppo IVA (utilizzando sempre la stessa sezione della maschera).
In fase di ricezione di un documento elettronico passivo, la procedura controlla se la partita IVA del gruppo è di un gruppo IVA, in questo caso viene fatto un controllo di congruenza tra la partita iva del gruppo e la partita iva della fattura, e tra il codice fiscale della fattura ed il codice fiscale del terzo associato al gruppo. In SIGLA sulla fattura verrà indicato il terzo del codice fiscale associato al gruppo, oppure, qualora in fattura non sia indicato il codice fiscale, verrà registrato il terzo del Gruppo IVA. 


Studio Associato
================
Lo Studio Associato è una persona guiridica e come tale deve essere registrata. L'attributo "Studio associato" consente di inserire nel tab Lista Associati,
qualora sia necessario, i soggetti che fanno parte dello studio, inoltre consente la gestione di un particolare caso nella funzione dei :doc:`Compensi<compensi>`


Utilizzo delle Anagrafiche - Schema di Controlli
================================================
Nella gestione delle fatture la corretta registrazione dell’anagrafica dei clienti e dei fornitori è fondamentale per portare a termine l’inserimento dei documenti.
Esistono, infatti, una serie di controlli che consentono o meno l’utilizzo delle anagrafiche presenti nell’archivio di SIGLA.


+------------+------------+---------------+---------------+----------------+------------------+-----------+------------------------------------------------------------+
|Tipo        |  Soggetto  | Soggetto      | Soggetto      | Codice Fiscale |Partita IVA       |Comunne di |                                                            |
|Documento   |  Residente | Non Residente | Non Residente | OBBLIGATORIO   |OBBLIGATORIA      |Residenza  | Controlli sulla Partita IVA                                |
|            |  in ITALIA | (INTRA UE)    | (EXTRA UE)    |                |                  |           |                                                            |
+============+============+===============+===============+================+==================+===========+============================================================+
|            |     X      |               |               |                |Soggetto          | Italiano  |La partita Iva dei soggetti italiani si compone di 11 cifre.|
|            |            |               |               |                |passivo: SI       |           |Il sistema controlla l'esattezza del codice attraverso un   |
|            |            |               |               |      Si        |                  |           |algoritmo di calcolo. La P.I. NON deve necessariamente      |
|Fattura     |            |               |               |                |Soggetto          |           |coincidere con il codice fiscale.                           |
|Attiva/     +------------+---------------+---------------+----------------+privato: NO       +-----------+------------------------------------------------------------+
|Passiva     |            |      X        |               |                |                  |Comunità   |La partita Iva dei soggetti intra UE ha una lunghezza       |
|            |            |               |               |No (se è un     |                  |Europea    |variabile. :under:`Il Sistema controlla a seconda           |
|            |            |               |               |soggetto        |                  |           |dello Stato la lunghezza del codice`                        |
|            +------------+---------------+---------------+passivo         +                  +-----------+------------------------------------------------------------+
|            |            |               |               |di IVA)         |                  |Extra UE   |Non esiste alcun controllo sul codice inserito              |
|            |            |               |     X         |                |                  |           |                                                            |
|            |            |               |               |                |                  |           |                                                            |
+------------+------------+---------------+---------------+----------------+------------------+-----------+------------------------------------------------------------+


Per verificare l’esattezza della P.I. accedere al sito http://ec.europa.eu/taxation_customs/vies/?locale=it

.. error::

   Attenzione! Nel campo Partita Iva delle anagrafiche estere NON DEVE essere inserito il codice ISO.

**Messaggi di Errore**

La procedura controlla il codice fiscale di una persona fisica, tale codice viene indicato all'utente e verificato.
La procedura restituisce un messaggio di errore  qualora non fosse corretto.
Il codice fiscale calcolato dalla procedura potrebbe non essere corretto nei casi di omonimia.

.. warning::

    La tipologia dell'anagrafica (italiana o estera) non è compatibile con il comune di residenza che si sta tentando di inserire.

.. hint::

    Modificare la tipologia dell'anagrafica se il comune di residenza è corretto, oppure inserire un comune di residenza compatibile con la tipologia selezionata.

Codice Anagrafico
-----------------

Il codice anagrafico, assegnato automaticamente dal sistema al primo salvataggio nell'inserimento di una anagrafica, non viene mai utilizzato all'interno della procedura nella
registrazione dei documenti contabili e dei documenti amministrativi. Vengono invece utilizzate in varie funzioni di SIGLA, tutte le informazioni inserite a livello anagrafico.
Viene inoltre utilizzato, per le movimentazioni Sigla, sempre il codice Terzo (associato sempre ad un'anagrafica)
Il codice anagrafico, di per se serve a richiamare l'anagrafica nel caso in cui sia necessario apportare delle modifiche all'anagrafica stessa.


Rapporto
========

Il pannello del Rapporto è articolato a sua volta in due folder, il primo dei Dettagli dedicato all’inserimento dei rapporto ed il  secondo per l’inserimento di eventuali `Inquadramenti`_.
Per assegnare un rapporto ad un’anagrafica bisogna cliccare su sull’icona “nuovo” (in basso a sinistra della prima sezione), e valorizzare i campi sottostanti:

**codice tipo rapporto**, che rappresenta una macro categoria alla quale sono riconducibili una serie di tipologie di reddito;

**data inizio validità** e **data fine validità** , che indicano temporalmente la validità del rapporto.

Si ricorda che ad ogni anagrafica di tipo “persona fisica” possono essere assegnati uno o più rapporti.

Inoltre è necessario sapere che le date di inizio e fine validità vengono confrontate al momento della registrazione:

- di un :doc:`Incarico<incarichi>`, con le date di inizio e di fine;
- di un :doc:`Compenso<compensi>`, con le date di competenza economica;
- di una :doc:`Minicarriera<minicarriere>`, con le date di inizio e fine della minicarriera stessa.

Per le anagrafiche di :under:`persone guiriche` è possibile inserire solo il rapporto :red:`'PROF'`.


Inquadramenti
=============

L'inserimento dell'inquadramento è necessario solo per le tipologie di rapporto che prevedono il trattamento di missione.

Carichi Familiari
=================

Le informazioni di questo pannello vengono utilizzate nel calcolo dei :doc:`Compensi<compensi>` e riguardano l’individuazione dei familiari a carico (coniuge, figlio, altro),
della validità di inizio e di fine, e della percentuale di abbattimento per ognuno.
Le detrazioni derivanti da familiari a carico, secondo quanto previsto dalla `Circolare <http://www.ittig.cnr.it/BancheDatiGuide/circolari/c08-02a1Direttiva%20RagioneriaSAC%20su%20buste%20paga%20e%20fisco%20-%2017-01-2008.pdf>`_ direttiva della Ragioneria della SAC
N.Reg RagSac 21/2008 del 17 gennaio 2008], sono dovute se il richiedente dichiara, :red:`'annualmente'`, di averne diritto.

I dati che devono essere inseriti sono:

- **Tipo persona**: è selezionabile da una lista predefinita e può essere: figlio, coniuge ed altro;
- **Codice fiscale**: codice fiscale del soggetto a carico, è un dato obbligatorio;
- **Data inizio validità**: è la data dalla quale si manifesta l'evento (es. caso di nascita di un figlio) oppure se l'evento si è manifestato in anni precedenti a quello fiscale tale data coincide con l'inizio dell'anno se il familiare è ancora a carico;
- **Data fine validità**: è la data dalla quale il soggetto non è più a carico (es. caso di morte) oppure tale data deve essere impostata al 31 dicembre dell'anno fiscale se il familiare è carico per tutto il periodo d'imposta;
- **Percentuale di Carico**:

  - per il **coniuge** può essere esclusivamente il 100%,
  - per i **figli** può essere il 50% o il 100%,
  - per le **altre** tipologie di familiari a carico può essere inserita qualsiasi percentuale.

- **Data di nascita**: è presente solo nel caso in cui il “Tipo di persona” sia figlio ed è un dato obbligatorio.
- **Portatore handicap**: è presente solo nel caso in cui il “Tipo di persona” sia figlio.
- **Primo figlio**: è presente solo nel caso in cui il “Tipo di persona” sia figlio; un solo un figlio può avere questo flag a vero.
- **Primo figlio in assenza di coniuge**: è presente solo nel caso in cui il “Tipo di persona” sia figlio; un solo un figlio può avere questo flag a vero.
- **Data fine figlio ha tre anni**: viene calcolata dal sistema utilizzando la “Data di nascita” al momento del salvataggio.
- **Codice Fiscale altro genitore**: è presente solo nel caso in cui il “Tipo di persona” sia figlio, ed è obbligatorio solo in alcuni casi gestiti in automatico dal sistema.


Messaggi di errore
------------------

.. error::

   Carichi Familiari: per il FIglio è necessario specificare il Codice Fiscale dell'altro genitore oppure è necessario inserire il Coniuge.

.. error::

   Attenzione: il codice fiscale dell'altro genitore è uguale a quello di un'altro carico.

.. error::

   Carichi familiari: percentuali di carico non valida per il figlio (0,50,100)%.

.. error::

   Attenzione è necessario specificare il Codice Fiscale dell'altro genitore.


Dettagli
========

Il sistema chiede alcune informazioni che riguadano:

- l’ente previdenziale: se la persona fisica è dell’inps, per estrarre le giuste informazioni per le rendicontazioni il sistema richiede il codice di attività inps: da tabella inps;
- il codice di un’eventuale altra assicurazione: riferirsi all’ufficio personale;
- L’iscrizione al registro delle imprese o a un albo;
- La data di scadenza del certificato antimafia

Quindi:

- un eventuale codice anagrafico correlato (dall’anagrafica esistente): serve per identificare, negli assimilati, qual è l’ente di appartenenza.
- Data e causale di fine rapporto: se valorizzati il sistema utilizza questa informazione nelle registrazioni contabili;
- Informazioni riguardao CAF e INAIL
- Note In fase di modifica di un’anagrafica si possono cambiare tutti i dettagli inseriti tranne il codice. La cancellazione di un’anagrafica è permessa solo se non è responsabile di cds o uo. In questi ultimi casi, al momento della cancellazione, il sistema avvisa ponendo la data del giorno nel campo di ‘data di fine rapporto’: in questo modo l’anagrafica non viene eliminata ma non può essere utilizzata nei documenti contabili.


Pagamenti Esterni
=================

Il pannello relativo ai pagamenti esterni, consente di inserire informazioni riguardanti i pagamenti ricevuti dal soggetto, da committenti diversi dall'Ente,
“in considerazione del fatto che il calcolo della contribuzione previdenziale relativa ad ogni singolo compenso deve tener conto cumulativamente di tutti i redditi afferenti alla
Gestione Separata Inps già percepiti dal soggetto beneficiario (collaborazioni coordinate e continuative, prestazioni d’opera, assegni di ricerca e prestazioni occasionali)”.

I compensi erogati da altre unità organizzative dello stesso ENTE che sta gestendo l'anagrafica, NON devono essere considerati pagamenti esterni.

La “Data di Pagamento” è comunicata dal soggetto ed è relativa a compensi ricevuti da altri committenti.

L’ “Importo al netto delle spese” si riferisce all’importo per la prestazione resa, detratte le eventuali spese addebitate al committente (corrisponde al lordo della nota di addebito).
