==============
Fattura Attiva
==============

Per poter procedere alla registrazione di una fattura attiva bisogna entrare nell’albero delle funzioni:  
 **- Documenti amministrativi**  
 
      - Fatture   
      - fattura attiva  
          - visualizzazione   
          - gestione  
          
La fatturazione elettronica Attiva, in Sigla, è strettamente integrata al processo dei documenti Attivi previsto dal Sistema di Interscambio. La gestione prevede l'invio automatico, dopo la Firma Digitale da parte del Responsabile, a SDI e la ricezione delle notifiche che vengono importate direttamente sul Documento attivo in Sigla.

Tutto il processo e le informazioni sono stati dettagliati nell'apposito paragrafo:

:doc:`Fattura Attiva Elettronica<fattura_attiva_elettronica>`

L'emissione di un documento attivo segue le stesse regole rispetto ai dati che l'utente deve inserire in Sigla. Sono di seguito indicati i vari folder di cui si compone la maschera “Fattura Attiva”. 

**Testata**

.. figure:: /screenshot/fatt_att_testata.png


Analizziamo ora i vari campi presenti nel folder “Testata” che sono comuni per qualunque tipologia di fattura attiva che verrà emessa. 
- Progressivo Tale campo sarà assegnato automaticamente dal sistema al momento del salvataggio della fattura. 
- Data registrazione Tale campo viene proposto dal sistema e non deve essere mai modificato. 
- Registrazione IVA e N. Registrazione IVA generale. Tali campi saranno compilati automaticamente dal sistema successivamente alla protocollazione della fattura (che avviene in fase di firma e invio del documento elettronico). 
- Data Stampa. Tale campo sarà compilato automaticamente dal sistema successivamente alla predisposizione alla firma della fattura (che produce la stampa/file pdf). 
- Competenza dal / Competenza al. Deve essere indicato il periodo di realizzazione del ricavo (effettuazione della prestazione). 
- Sezionale il sistema proporrà sempre ed esclusivamente “Registro IVA ordinario delle fatture emesse”.  
- Tipo Documento scegliere nel menù a tendina la voce che interessa 
- Totale Imponibile Tale campo sarà compilato automaticamente dal sistema con la compilazione del folder “dettaglio”. 
- Totale Iva Tale campo sarà compilato automaticamente dal sistema con la compilazione del folder “dettaglio”. 
- Totale Tale campo sarà compilato automaticamente dal sistema con la compilazione del folder “dettaglio”. 
- Descrizione libera. 
- Mod. pagamento scegliere nel menù a tendina la voce che interessa tenendo presente che:  
      - BI – Banca d’Italia deve essere scelta tale modalità nel caso il cliente sia  un’amministrazione dello Stato o un Ente Pubblico presente nella tabella A legge 720/84 
      - BO – Bonifico su c/c Bancario N.B. il c/c che deve essere fornito ai nostri clienti è  IBAN IT57S0100503392000000218155 
   
Esaminiamo ora i campi particolari: 
- Liquidazione Differita deve essere spuntato solo ed esclusivamente in caso di emissione di fattura attiva a favore di un organo dello Stato o di un Ente locale ai sensi art.6 DPR633/72 (In tal caso il nostro debito IVA sorgerà nel momento dell’incasso della fattura). 
- Intra U.E. Deve essere spuntato nel caso in cui il cliente sia residente in un Paese INTRA-UE. Questa spunta comporta a sua volta la comparsa di un'altra scelta: 
- Servizi/Beni scegliere nel menù a tendina la voce che interessa.
- Extra U.E. Deve essere spuntato nel caso in cui il cliente sia residente in un Paese EXTRA-UE. 
- S. Marino. Deve essere spuntato nel caso in cui il cliente sia residente a San Marino. 

**CLIENTE**
 
Si ricorda che per poter indicare un terzo residente in un Paese INTRA-UE è necessario spuntare in testata il flag 'Intra UE'; mentre se il terzo è residente in un Paese EXTRA-UE è necessario spuntare il flag 'Extra UE'; infine se il terzo è residente a San Marino è necessario spuntare il flag 'San Marino'. 

**DETTAGLIO**

Per procedere all’inserimento sarà necessario cliccare su  Nuovo, si creerà una nuova riga che attiverà i campi sottostanti: 

- Bene/Servizio dovrà essere inserito il bene/servizio fornito o cliccando su 'azzera' e digitando il codice di riferimento o cliccando su 'cerca' scegliendo dalla tabella beni/servizi sottostante o cliccando su 'ricerca guidata' e impostando dei parametri. Nel caso di fattura emessa a favore di  un residente in un paese INTRA-UE sarà possibile inserire solo beni o servizi a seconda della scelta effettuata in testata. 
- I.V.A. il sistema di “default” propone il codice IVA collegato all’IVA ordinaria. Cliccando su 'azzera' è possibile cambiare il codice IVA da applicare al dettaglio della fattura o inserendo direttamente il codice di riferimento o cliccando su 'cerca' scegliendo dalla tabella proposta. 
- Percentuale IVA% è inserita direttamente dal sistema in base al codice IVA prescelto. 
- Descrizione. Quanto riportato su questo campo apparirà nella stampa della fattura attiva. 
- Competenza dal al vengono proposti direttamente dal sistema uguali a quelli inseriti in testata ma possono essere modificati dall’utente purché ricompresi nell’intervallo del periodo inserito in testata. 
- Quantità. Inserire e salvare attraverso la 'conferma',  in caso di errore utilizzare 'annulla' per annullare l’operazione. 
- Prezzo Unitario. Inserire l’importo del singolo bene/servizio e salvare attraverso la 'conferma',  in caso di errore utilizzare 'annulla' per annullare l’operazione. 
- Importo IVA viene calcolato dal sistema e dovrà/potrà essere modificato dall’utente solo in caso di non quadratura tra il totale prestazione e il totale fatturato (totale prestazione 550,00 - imponibile - 454,54 - iva 95,45 - totale 549,99, in tal caso si aggiunge 1 cent. sull’importo IVA, si salva attraverso la 'conferma', apparirà il messaggio “L'importo IVA e' forzato”. Tale campo sarà riportato in Testata. 
- Imponibile viene calcolato dal sistema (quantità * prezzo unitario). Tale campo sarà riportato in Testata 
- Totale viene calcolato dal sistema (importo IVA + imponibile). Tale campo sarà riportato automaticamente in Testata. 

**Caso particolare TARIFFARIO**
 
Qualora in testata in tipo di documento sia stato scelto “Tariffario” dopo il campo beni/servizio apparirà 
- Tariffario. Dovrà essere inserito il tariffario o cliccando su 'azzera' e digitando il codice di riferimento o cliccando su 'cerca' scegliendo dalla tabella tariffario precedentemente caricata (Scegliendo nell’albero delle funzioni “Fatture” “Tabelle di riferimento” “Tariffario”) o cliccando su 'ricerca guidata' e impostando dei parametri. In tal caso il prezzo unitario viene riportato automaticamente  dal sistema. 

**Caso particolare INTRA-UE** 
 
 Qualora  in  testata  sia  stato  spuntato il flag 'INTRA-UE' occorre distinguere se il cliente è un consumatore finale o un soggetto passivo di imposta: 
- Consumatore finale indipendentemente dalla scelta effettuata sul record Servizi/Beni la fattura dovrà essere sempre emessa con IVA. 
- Soggetto passivo di imposta dobbiamo in questo caso distinguere tra: 

 - Beni
   in questo caso la fattura dovrà essere emessa senza IVA (fuori campo) in quanto l’IVA viene acquisita nel paese del cliente e dovrà essere compilato il Modello Intrastat. 
 - Servizi 
   in questo caso la fattura dovrà essere emessa senza IVA se il servizio prestato rientra nell’applicazione dell’art. 7 ter e dovrà essere compilato il relativo Modello Intrastat; qualora invece fossero Servizi che rientrano nell’ambito dell'art. 7 quater e 7 quinques - territorialmente effettuati in Italia la fattura sarà emessa con IVA e non dovrà essere compilato il relativo Modello Intrastat; qualora invece fossero Servizi che rientrano nell’ambito dell'art. 7 quater e 7 quinques - territorialmente non effettuati in Italia la fattura dovrà essere emessa Fuori campo IVA e non dovrà essere compilato il relativo Modello Intrastat. 

**Caso particolare EXTRA-UE** 
 
Qualora in testata sia stato spuntato il flag 'EXTRA-UE' occorre distinguere se il cliente è un consumatore finale o un soggetto passivo di imposta: 

- Consumatore finale dobbiamo in questo caso distinguere tra: 
   - Servizi la fattura dovrà essere emessa sempre con IVA.  
- Soggetto passivo di imposta dobbiamo in questo caso distinguere tra: 
   - Beni in questo caso la fattura dovrà essere emessa non imponibile ai sensi art. 8 con il comma appropriato a seconda dell’esportazione.  
   - Servizi in questo caso la fattura dovrà essere emessa senza IVA se il servizio prestato rientra nell’applicazione dell’art. 7 ter; qualora invece fossero Servizi che rientrano nell’ambito dell'art. 7 quater  e 7 quinques - territorialmente effettuati in Italia la fattura sarà emessa con IVA; qualora invece fossero Servizi che rientrano nell’ambito dell'art. 7 quater e 7 quinques - territorialmente non effettuati in Italia la fattura dovrà essere emessa Fuori campo IVA. 

 
N.B. PER TUTTE LE FATTURE EXTRA-UE NON DEVE MAI ESSERE COMPILATO IL MODELLO INTRASTAT 
 
Dopo la compilazione del folder “Dettaglio” si deve provvedere alla contabilizzazione della fattura (legare la stessa all’accertamento). Si mette il flag sul dettaglio che si vuole contabilizzare, si clicca sul pulsante 'contabilizza' e appare la maschera di ricerca o creazione dell’accertamento.  Naturalmente sarà possibile creare un accertamento solo se di competenza. Nel caso di emissione di fattura collegato ad un accertamento residuo si ricorda che la liquidazione IVA potrà (su apposita richiesta da effettuarsi entro il giorno 6 di ogni mese, tramite fax all’Ufficio Fiscale) gravare sugli stanziamenti residui solo per l’esercizio n-1. Nel caso di registrazione dell’accertamento collegato a codice terzo 2 (ad esempio accertamenti relativi  a quote di partecipazione a convegni) ricordarsi di togliere il flag nel quadratino “codice terzo” per poter richiamare l’accertamento. 

**CONSUNTIVO**

Tale folder viene compilato in automatico dal sistema tramite la compilazione del folder “dettaglio” e riepiloga  i dettagli per codice IVA. 

**ACCERTAMENTI**

Tale folder viene compilato in automatico a seguito della contabilizzazione del dettaglio. Nella parte superiore vengono riportate le scadenze dell’accertamento e facendo diventare blu il rigo della scadenza nella parte sottostante sono evidenziati i dettagli della fattura collegati alla scadenza dell’accertamento. 

**INTRASTAT** 
 
Se in testata è stato messo il flag su INTRA-UE sarà necessario, ove previsto,  compilare anche il folder “INTRASTAT”  

**Cessione di beni**

Per procedere all’inserimento sarà necessario cliccare su 'Nuovo', si creerà una nuova riga e si dovrà procedere alla compilazione dei record richiesti. 

**Cessione di servizi**

Per procedere all’inserimento sarà necessario cliccare su 'Nuovo', si creerà una nuova riga e si dovrà procedere alla compilazione dei record richiesti. 




