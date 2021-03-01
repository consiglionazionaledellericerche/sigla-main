============================
Fattura Passiva Elettronica
============================

Questa sezione del documento illustra le modalità operative, integrate al sistema contabile SIGLA, per soddisfare le esigenze espresse dalla normativa in merito alla gestione della fatturazione elettronica: il DMef n. 55 del 3 aprile 2013 ha stabilito l’obbligatorietà di ricevere esclusivamente fatture, note e parcelle elettroniche da soggetti residenti a partire dal 31/03/2015 da parte della Pubblica Amministrazione. Resta esclusa dalla gestione elettronica obbligatoria dei documenti, tutto ciò che riguarda documenti esteri (che in alcuni casi potrebbero riguardare anche la ricezione elettronica, per documenti INTRAUE il cui fornitore ha deciso di aderire alla piattaforma SDI). 
Conseguentemente restano esclusi dall'obbligo i rapporti con soggetti non residenti a meno che non agiscano con rappresentante fiscale o con stabile organizzazione presente in Italia. 
Di seguito vengono descritte le implementazioni effettuate in Sigla e le conseguenti modalità operative.

**Gestione degli aspetti preliminari**

Prima di avviare la fatturazione elettronica passiva, è stato comunque necessario censire tutti gli uffici di fatturazione del CNR (CUU) presso l’IPA, come da circolare 35/2014 (allegato 6), ed è stato necessario effettuare la procedura di accreditamento del canale di comunicazione verso SDI per specificare quale modalità il CNR ha deciso di adottare nelle comunicazioni da e per SDI.
La Fatturazione Elettronica pone come obbligo la comunicazione con il **Sistema di Intercambio** per l’invio e la ricezione di documenti elettronici. 
Il canale di comunicazione scelto dal CNR per questa comunicazione è la Posta Elettronica Certificata (mediante l’uso dell’indirizzo PEC del Protocollo) attraverso cui riceverà da SDI i documenti elettronici passivi e invierà le relative notifiche. L’utilizzo del canale PEC, in ogni caso, è una scelta tecnica che non richiede nessun intervento manuale da parte degli utenti Sigla. 
Una procedura automatica, infatti, si preoccupa di controllare tutti i documenti ricevuti da SDI inserendoli in automatico nell’area temporanea di Sigla (vedi dopo) assegnandoli alla Unità Organizzativa destinataria. Allo stesso modo, nel momento in cui un documento viene ‘Rifiutato’ (vedi dopo) oppure ‘Accettato’ dalla Unità Organizzativa che lo riceve, una procedura automatica si preoccupa di inviare la relativa notifica al Sistema di Interscambio. Resta chiaramente a carico dell’utente il controllo e la gestione dei documenti in Sigla. 
L’amministratore delle utenze di SIGLA dovrà assegnare all’utenza che si occuperà di gestire, o anche di consultare e controllare, i documenti passivi in arrivo la nuova funzionalità: 
 
- AMMDOCFATTELEPASS 	Gestione fatturazione elettronica passiva 

Si ricorda che, per assegnare i nuovi accessi agli utenti interessati, occorre collegarsi con l'utenza di amministratore del CDS e assegnare l'accesso specifico. 

**Gestione delle Notifiche Mail**
Per ogni documento elettronico che arriva nell’area temporanea di Sigla, quindi associato ad una UO, è possibile recapitare una mail all’utente preposto al controllo delle fatture in arrivo e già abilitato alle funzionalità di Sigla per la medesima UO, in modo da essere puntualmente avvisati. 
La configurazione dell’indirizzo mail è demandata al gestore delle utenze Sigla che, per ogni utente designato come destinatario della mail, deve indicare l’indirizzo mail a cui recapitare l’avviso. La mappa permette di scegliere a quale indirizzo mail far pervenire l’avviso di ricezione per ogni fattura/nota elettronica che arriva alla UO associata all’utente. La mail può essere configurata anche per la UO di competenza. In questo caso viene ricevuta quando la UO di destinazione assegna il documento elettronico alla UO di competenza (vedi 'Gestioni particolari'). 

**Gestione dei codici CUU per le Strutture del CNR**
I documenti passivi elettronici vengono recapitati dal Sistema di Interscambio agli Uffici di fatturazione elettronica che ogni Ente Pubblico ha censito presso l’IPA. Il CNR ha censito presso l’IPA tutti i Codici Univoci Ufficio per tutte le UO come da circolare 35/2014 allegato 6. 
A tal2.	Gestione documenti elettronici passivi  fine è stato aggiunto, sull’anagrafica Terzo di SIGLA, corrispondente ad ogni Unità Organizzativa, il campo “Codice Univoco Ufficio IPA”.  
Tale informazione, non modificabile dagli utenti, è stata valorizzata in base alle informazioni riportate anche nell’indice della pubblica amministrazione.


**Gestione documenti elettronici passivi**
In sintesi, dopo la definizione degli aspetti preliminari appena descritti, l’iter operativo si distingue in tre passi fondamentali: 

1.	Ricezione fattura da SDI; 
2.	Registrazione fattura in SIGLA; 
3.	Invio/Ricezione notifiche.

**Processo previsto**
Tutti i documenti passivi (ad eccezione di quelli emessi da soggetti non residenti), con data di emissione dal  31 marzo 2015 incluso, dovranno essere ricevuti esclusivamente in formato elettronico e attraverso il Sistema di Interscambio. 
Il processo operativo dei documenti elettronici passivi previsto è il seguente: 

.. figure:: /screenshot/Processo_fatt_ele_pass.png

**Ricezione documenti dal Sistema di Interscambio**
Il Sistema di Interscambio (SDI) si pone come intermediario tra il trasmittente (il Fornitore nel caso di documenti passivi), e il committente (CNR che ha richiesto il bene o servizio). 
Tutti coloro che devono inviare un documento fiscale, quindi, a partire dal 31 marzo p.v., dovranno inviare un documento elettronico al Sistema di Interscambio che si occuperà di recapitare al destinatario il documento ricevuto. Allo stesso modo SDI si preoccupa di fornire informazioni al trasmittente sull’esito della consegna. 
Dopo l’invio del documento elettronico al destinatario, SDI resta in attesa dell’esito da parte del committente (CNR in questo caso) e restituisce tale informazione al trasmittente. Nel caso in cui non riceve nessuna informazione invia ad entrambi una notifica di mancato esito e conclude la sua attività di intermediazione per lo specifico documento. 

**Gestione caricamento documento nell’area temporanea**
L’area temporanea, gestita direttamente in SIGLA, accoglierà tutti i dati del file FatturaPA ricevuti e successivamente sarà gestita dagli utenti abilitati (in relazione all’Istituto/UO di appartenenza). I documenti ricevuti via PEC saranno automaticamente caricati nell’area temporanea, dove l’utente Sigla li troverà per lavorarli (ricevendo una mail di avviso, nel caso sia stata attivata tale gestione). 

**Gestione dell’area temporanea**

Il Sistema di Interscambio, che riceve dal Trasmittente i documenti passivi, li consegna al committente (CNR) secondo la modalità scelta e concordata con CNR stesso. Successivamente all’invio da parte di SDI, i documenti, attraverso il CUU indicato dal trasmittente, vengono recapitati all’ufficio di fatturazione (quindi alla UO) del CNR. Gli utenti delle UO, abilitati a controllare e a registrare i documenti passivi in Sigla, potranno gestire i documenti per la loro Struttura. 
Nel caso non si riuscisse, in fase di caricamento del documento elettronico nell’area temporanea, a risalire alla UO in Sigla, il documento sarà assegnato alla UO 'Ente'. In sede centrale sarà verificato il problema ed eventualmente riassegnata la fattura oppure sarà avvisato il Fornitore. 
Per accedere alle funzionalità di gestione delle fatture elettroniche l’utente deve selezionare dall’albero delle funzioni di SIGLA la funzionalità di 'fatturazione elettronica passiva'. Dopo aver impostato eventuali criteri di ricerca, ed eseguito la ricerca dei documenti, il risultato sarà mostrato nella griglia dei risultati. La selezione di uno dei documenti presenti nella griglia, riporta sulla gestione dello specifico documento.
Tutte le informazioni del documento elettronico vengono riportate nell’area temporanea di SIGLA e rese disponibili all’utente per la loro consultazione e per le attività di completamento dei dati. 

I criteri di ricerca che possono essere impostati in testata fattura elettronica sono:

- Identificativo Fiscale Trasmittente	- Partita IVA
- Identificativo SDI	
- Data documento	- (Data documento fornitore)
- CUU Destinatario - (CUU della UO ricevente)	
- UO di destinazione	- (UO ricevente)
- UO di competenza	- (UO di competenza se specificata dall'utente dopo la ricezione)
- Non registrabile	- Questo flag (S/N) consente di escludere dalla ricerca tutti i documenti già definiti **Irregistrabili** (Non registrabile = S) oppure tutti i documenti - - registrabili (Non registrabili = N). Se lasciato vuoto visualizza tutti i documenti (fermo restando gli altri filtri di ricerca).
- Stato Documento.

E' inoltre possibile impostare altri criteri di ricerca:

Tab Trasmissione:

- Data ricezione;
- Identificativo fiscale o Codice fiscale del prestatore;
- Denominazione/Nome/cognome del prestatore;
- Terzo Sigla associato.

Tab Documento:

- Tipo documento;
- Data documento Fornitore;
- Numero documento Fornitore.


In caso di trasmissioni con più documenti, nell’area temporanea viene mostrato sempre un documento per volta. 
 
Nell’inserire i dati nell’area temporanea, la procedura automaticamente assocerà alcune anagrafiche Sigla alle informazioni ricevute dal Fornitore, se ne troverà la corrispondenza (Anagrafica, Terzo, Codici IVA, Modalità di pagamento). In caso contrario tali associazioni devono essere effettuate manualmente dall’utente. Queste informazioni (utili alla successiva registrazione del documento in Sigla), sono evidenziate in tutte le sezioni dell’area temporanea con un colore blu per evidenziarle.
 
 
Tutti i dati originari del documento elettronico sono sempre visualizzabili  anche dopo la loro lavorazione ed è possibile scaricare il documento mediante il pulsante 'Download Fattura', posto in alto nella maschera, che apre il relativo foglio di stile, oppure è possibile scaricare il file firmato dal pulsante 'Fattura firmata'.

Le informazioni contenute nel documento elettronico, sono organizzate nell’area temporanea in sezioni specifiche per argomento, come di seguito dettagliato: 

- Dati trasmissione. Fondamentalmente individuano il prestatore (Fornitore) ed il destinatario (UO) del documento. 
Nel caso in cui il documento venisse trasmesso da un Rappresentante Fiscale o da un Intermediario in questa sezione vengono mostrate anche queste ulteriori  informazioni. In quest’ultimo caso, nel documento elettronico è possibile specificare anche se il soggetto emittente è il prestatore oppure l’Intermediario/Rappresentante fiscale (che quindi diventerebbe intestatario della fattura in Sigla). Nel proporre queste informazioni nella compilazione della fattura si è scelto di indicare sempre come Fornitore il soggetto Prestatore. Resta a cura dell’utente l’eventuale cambio di questa informazione (magari consultando direttamente il Prestatore in caso di dubbi).
La procedura automatica di caricamento dei documenti nell’area temporanea, nel caso in cui in Sigla sia presente un’anagrafica avente come codice fiscale o partita IVA quelli indicati nella fattura elettronica ricevuta, associa automaticamente tale anagrafica al documento elettronico. Nel caso in cui non ci fosse corrispondenza con i dati di Sigla oppure ci fossero più anagrafiche aventi i requisiti indicati, l’associazione deve essere fatta manualmente dall’utente scegliendo l’anagrafica giusta dalla lista proposta (potrebbe anche capitare che l’anagrafica non è presente in Sigla e quindi va inserita prima di procedere). Se l’anagrafica associata ha un solo Terzo, anche questo viene proposto automaticamente.  Nel caso in cui ci fossero più Terzi per la stessa anagrafica questa associazione deve essere effettuata manualmente dall’utente attraverso la scelta del codice giusto dalla lista proposta.
Nel caso in cui avvenisse automaticamente l’associazione sia dell’anagrafica che del codice terzo, in questa sezione è possibile verificare indirizzo e altre informazioni del committente specificate dal Fornitore. Se ci sono incongruenze tra i dati del documento ricevuto e le informazioni presenti su Sigla, queste vengono indicate, alla fine della pagina, nelle ‘segnalazioni da verificare’ e resta a cura dell’utente capire se queste incongruenze possono determinare il rifiuto del documento o meno.  L’informazione del Terzo è l’unica obbligatoria ed è quella che rende ‘Completo’ il documento elettronico e quindi registrabile in Sigla. L’eccezione è solo la gestione della UO di competenza (vedi 'Gestioni particolari').

- Dati generali del documento. Contiene tutte le informazioni generali del documento compreso le Modalità di pagamento indicate dal prestatore e i dati relativi al trasporto. 
E’ importante precisare che in questa sezione è indicato anche il Totale documento. Tale dato risulta essere obbligatorio ai fini dei controlli di quadratura effettuati dalla procedura in fase di registrazione del documento. L’unica differenza tollerata tra il totale della fattura elettronica e il totale indicato in Sigla in fase di registrazione del documento, è data dall’importo dell’arrotondamento eventualmente specificato per l’intera fattura. L’assenza del Totale documento sulla fattura elettronica non determina il Rifiuto del documento stesso perché è possibile valorizzarlo direttamente sul documento elettronico, solo se non specificato, e renderlo congruente con la somma del riepilogo IVA della fattura stessa. 
Per quanto riguarda la proposta della modalità di pagamento automatica nell’area temporanea la procedura segue le seguenti regole: se è specificato il Terzo e la modalità di pagamento indicata nella fattura elettronica è una tipologia corrispondente in Sigla alle modalità legate al Terzo, allora viene automaticamente proposta, altrimenti questa deve essere scelta manualmente dall’utente. E’ possibile poi durante la registrazione in Sigla della fattura, indicare altre modalità di pagamento comunque legate al terzo. Per quanto riguarda invece le Tipologie di documento aggiornate a gennaio 2021, sono:
  TD01 Fattura 
  TD02 Acconto/Anticipo su fattura 
  TD03 Acconto/Anticipo su parcella 
  TD04 Nota di Credito 
  TD05 Nota di Debito 
  TD06 Parcella 
  TD16 Integrazione fattura reverse charge interno 
  TD17 Integrazione/autofattura per acquisto servizi dall’estero 
  TD18 Integrazione per acquisto di beni intracomunitari 
  TD19 Integrazione/autofattura per acquisto di beni ex art.17 c.2 DPR 633/72  – Non gestito ad oggi in Sigla
  TD20 Autofattura per regolarizzazione e integrazione delle fatture (art.6 c.8 d.lgs. 471/97 o art.46 c.5 D.L. 331/93) – Non gestito ad oggi in Sigla
  TD21 Autofattura per splafonamento – non riguarda le gestioni CNR
  TD22 Estrazione beni da Deposito IVA – non riguarda le gestioni CNR
  TD23 Estrazione beni da Deposito IVA con versamento dell’IVA – non riguarda le gestioni CNR
  TD24 Fattura differita di cui all’art.21, comma 4, lett. a) – Gestita in Sigla come fattura normale
  TD25 Fattura differita di cui all’art.21, comma 4, terzo periodo lett. b) – Gestita in Sigla come fattura normale
  TD26 Cessione di beni ammortizzabili e per passaggi interni (ex art.36 DPR 633/72) 
  TD27 Fattura per autoconsumo o per cessioni gratuite senza rivalsa  – Non gestito ad oggi in Sigla


- Dettaglio righe del documento. Contiene i dati relativi ai beni/servizi acquistati con le relative informazioni di prezzo, IVA ecc. In questa sezione viene indicato anche un codice articolo e una descrizione del bene, dati che definisce il Fornitore per descrivere il dettaglio fattura.  I dettagli della fattura elettronica possono essere completati associando, quindi, i codici Bene/Servizio di Sigla (campi di colore diverso), in modo da averli già proposti alla registrazione della fattura, oppure è possibile lasciarli vuoti per poi completare le informazioni direttamente in fase di registrazione. Durante la registrazione della fattura, se non si vuole mantenere lo stesso dettaglio definito nella fattura elettronica, è possibile eliminare i dettagli proposti e accorpare o dividere gli stessi direttamente in Sigla secondo le esigenze (si possono accorpare chiaramente solo dettagli con codice IVA uguale). Nel controllo degli importi effettuato in fase di registrazione occorre tener presente che il riepilogo per codice IVA (imponibile e imposta) deve quadrare con quanto specificato nella fattura elettronica. L’unica differenza tollerata ai fini del salvataggio è l’importo dell’arrotondamento eventualmente specificato sulla riga stessa. 

- Riepilogo IVA. Contiene un riepilogo per codice IVA (aliquota IVA o natura di esenzione), con l’indicazione dell’imponibile, imposta e totale.  
Anche in questa sezione è possibile completare i dati ricevuti con l’associazione del codice IVA presente nella rispettiva anagrafica Sigla (campo di colore diverso) che poi viene proposta all’atto della registrazione del documento. Bisogna fare attenzione al fatto che la lista valori proposta contiene SOLO i codici IVA che corrispondono ai requisiti del documento ricevuto. Per questo motivo, se è specificata un’aliquota IVA per la riga in esame, vengono proposti solo i codici IVA di Sigla che hanno l’aliquota indicata. Nel caso invece si trattasse di un dettaglio senza aliquota IVA ma con un codice di esenzione specificato (Natura), vengono mostrati i soli codici IVA di Sigla che hanno nel campo Natura lo stesso valore specificato sul dettaglio in questione. Nel documento elettronico sono previsti i seguenti valori per il campo Natura: 
  N1 escluse ex art.15 
  N2 non soggette (non più valido dal 1 Gen 2021) 
  N2.1 non soggette ad IVA ai sensi degli artt. da 7 a 7-septies del DPR 633/72 N2.2 non soggette – altri casi 
  N3 non imponibili (non più valido dal 1 Gen 2021) 
  N3.1 non imponibili – esportazioni 
  N3.2 non imponibili – cessioni intracomunitarie 
  N3.3 non imponibili – cessioni verso San Marino 
  N3.4 non imponibili – operazioni assimilate alle cessioni all’esportazione 
  N3.5 non imponibili – a seguito di dichiarazioni d’intento 
  N3.6 non imponibili – altre operazioni che non concorrono alla formazione del plafond N4 esenti 
  N5 regime del margine / IVA non esposta in fattura 
  N6 inversione contabile (per le operazioni in reverse charge ovvero nei casi di autofatturazione per acquisti extra UE di servizi ovvero per importazioni di beni nei soli casi previsti - non più valido dal 1 Gen 2021) 
  N6.1 inversione contabile – cessione di rottami e altri materiali di recupero 
  N6.2 inversione contabile – cessione di oro e argento puro 
  N6.3 inversione contabile – subappalto nel settore edile 
  N6.4 inversione contabile – cessione di fabbricati 
  N6.5 inversione contabile – cessione di telefoni cellulari 
  N6.6 inversione contabile – cessione di prodotti elettronici
  N6.7 inversione contabile – prestazioni comparto edile e settori connessi 
  N6.8 inversione contabile – operazioni settore energetico 
  N6.9 inversione contabile – altri casi 
  N7 IVA assolta in altro stato UE (vendite a distanza ex art. 40 commi 3 e 4 e art. 41 comma 1 lett. b, DL 331/93; prestazione di servizi di telecomunicazioni, teleradiodiffusione ed elettronici ex art. 7-sexies lett. f, g, DPR 633/72 e art. 74-sexies, DPR 633/72)
  
  Attenzione: quando viene registrato il documento in Sigla se i dettagli IVA non corrispondono a quanto indicato nel documento elettronico (riepilogo imponibili e IVA per codice aliquota e Natura), non è consentito il salvataggio dei dati. 
  
- Riferimenti acquisto. In questa sezione vengono riportate le informazioni relative ai riferimenti del documento di acquisto (Ordine, Contratto, Convenzione ecc.). In questa sezione vengono riportate anche le informazioni relative alle fatture di riferimento, nel caso si tratti di Nota Credito.  
E’ obbligatorio, ai fini della registrazione della nota credito/debito in Sigla, indicare da parte del Fornitore: 
  - Tipo riferimento: Fatture_collegate; 
  - Documento riferimento acquisto: numero fattura a cui si riferisce la nota; 
  - Data riferimento acquisto: data fattura a cui si riferisce la nota; 
Senza queste informazioni è inibita la registrazione della Nota in Sigla. 
In questa sezione troviamo anche l’eventuale indicazione del CUP e/o del CIG. Questi dati non vengono controllati in maniera vincolante dalla procedura di registrazione in Sigla e resta quindi a cura dell’utente capire se ci sono omissioni o incongruenze tali da richiedere il rifiuto del documento ricevuto. 

- Dati trasporto. Questa sezione contiene i riferimenti eventuali ai documenti di trasporto (DDT). Non è previsto nessun controllo bloccante in riferimento a queste informazioni ai fini della registrazione del documento in Sigla.

La maschera si presenta come di seguito: 
 
- Dati tributi. Contiene le informazioni relative a Ritenute e Contributi. I dati possono riguardare Ritenute IRPEF, dati INPS e altre Casse previdenziali. Vengono riportate le indicazioni relative ad aliquote, imponibili e imposte. In questa sezione non è prevista nessuna associazione ai dati Sigla perché la fattura proposta sarà etichettata automaticamente come fattura ‘associata a compenso’ e sarà nel compenso stesso che si dovrà indicare il Trattamento e le altre informazioni indispensabili alla sua registrazione.

- Dati relativi a sconti e maggiorazioni. Questa sezione contiene eventuali sconti e maggiorazioni applicate dal fornitore. Al fine della quadratura dei totali in fase di registrazione del documento tali importi devono essere già compresi nei totali (sia per le righe che per il totale documento). 

- Allegati. Questa sezione consente di consultare tutti gli allegati che il Fornitore ha ritenuto opportuno inviare insieme al documento (Allegati ricevuti). E’ consentito, inoltre, aggiungere altri allegati direttamente sul documento elettronico (Allegati Aggiunti), per completare eventualmente le informazioni necessarie e semplificare processi interni all’Ente (Vedi anche ‘Gestioni Particolari’). Gli Allegati aggiunti devono essere qualificati e possono appartenere ad una delle seguenti tipologie: DURC, Tracciabilità, Prestazione resa, Altro. 
Gli allegati (sia ricevuti che aggiunti), dopo la registrazione del documento in Sigla, saranno consultabili anche direttamente dalla funzione di gestione fatture.
Ci sono particolari tipi di allegati che rappresentano 'gestioni' inserite nella funzionalità in questione:
   - Allegato di 'Non registrabilità'. Consente tramite l'associazione alla fattura elettronica di questo tipo di allegato, la qualifica della fattura stessa 'NON REGISTRAILE'. Non sarà possibile successivamente registrare o rifiutare il documento.
   - Invio PEC::::::::::::::::::::::::::::::::::::::::
   
**Segnalazioni per l’utente**
Per ogni sezione del documento saranno evidenziate eventuali note, riscontrate durante il caricamento del documento e da verificare da parte dell’utente. Queste note possono essere semplici segnalazioni per incongruenze non gravi e, quindi, non bloccare la registrazione del documento ma richiedere qualche verifica dei dati in Sigla. Le segnalazioni vengono indicate nell’apposito campo posto alla fine di ogni maschera 













