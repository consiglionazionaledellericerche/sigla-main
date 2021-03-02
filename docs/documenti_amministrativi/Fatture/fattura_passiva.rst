===============
Fattura Passiva
===============

Per poter procedere alla registrazione di una fattura passiva bisogna entrare nell’albero delle funzioni: 

**- Documenti amministrativi**  
 
      - Fatture   
      - fattura passiva  
          - visualizzazione   
          - gestione

Sono di seguito indicati i vari folder di cui si compone la maschera “Fattura Passiva”. Bisogna tener conto che la registrazione delle Fatture Passive è condizionata dalla proposta e dai controlli derivanti dalla Fatturazione Elettronica Passiva, fatta eccezione per le fatture extraue, intraue e San Marino (che ad oggi possono essere ricevute ancora in forma cartacea e quindi registrati secondo le vecchie modalità).
In questo paragrafo analizziamo esclusivamente la funzionalità di registrazione fattura, poi vedremo come questa è condizionata dal processo e dalle regole dettate dalla normativa vigente per la Fatturazione Elettronica.
 
**TESTATA**

Analizziamo ora i vari campi presenti nel folder “Testata” comuni per tutte le tipologie di fattura passiva: 

- Progressivo. Tale campo sarà assegnato automaticamente dal sistema al momento del salvataggio della fattura. 
- Data registrazione. Tale campo viene proposto dal sistema e non deve essere mai modificato. 
- Data protocollo. campo non obbligatorio nel quale può essere indicato il protocollo di ricevimento della fattura. 
- Registrazione IVA. Tale campo sarà compilato automaticamente dal sistema in seguito alla chiusura mensile dei registri IVA. 
- Numero Registrazione IVA. generale Tale campo sarà compilato automaticamente dal sistema in seguito alla chiusura mensile dei registri IVA. 
 
- Liquidazione Differita. L'utilizzo di tale flag è limitato alle fatture ricevute nello svolgimento dell'attività commerciale. Per ulteriori dettagli sull'esigibilità differita delle fatture passive consultare la Circ. 17/2009. 
- Fondo economale. scegliere nel menù a tendina la voce che interessa tenendo presente che 'USA FONDO ECONOMALE' non può essere usato nel caso di fatture:  
   - Intra U.E.
   - Extra U.E.  	 
   - San Marino con IVA
   - San Marino senza IVA
   
USARE FONDO ECONOMALE se la fattura sarà pagata tramite fondo economale. 
NON USARE FONDO ECONOMALE se la fattura sarà pagata tramite mandato di pagamento. 

- Numero Fattura. Deve essere inserito il numero della fattura del fornitore. 
- Descrizione. Campo libero. 
-	Data emissione. Deve essere inserita la data di emissione della fattura del fornitore. 
-	Data di scadenza. Va indicata la data di scadenza della fattura (non serve per il trasferimento della liquidità da parte dell’Ufficio Bilancio che prende in considerazione le date di scadenza degli impegni liquidati). 
-	Esercizio fattura fornitore. Viene inserito in automatico dal sistema in base alla data di emissione della fattura.  
-	Competenza dal/Competenza al. Deve essere indicato il periodo di utilizzo del fattore produttivo. Nel caso di bene inventariabile deve essere lasciata la data proposta in quanto l’imputazione del costo avviene tramite la quota di ammortamento. 
-	Totale. Totale fattura come dal documento eventualmente integrato dell’IVA ove previsto 
- Totale Euro. Viene riportato dal sistema. 
-	Valuta. Nel caso di fatture INTRA/EXTRA UE la fattura deve essere registrata indicando la valuta relativa alla fattura. 
-	Cambio. E' proposto dal sistema ma deve essere modificato con il cambio del giorno di effettuazione dell’operazione o della data della fattura. 

Analizziamo ora le varie tipologie di fatture passive che possono essere registrate: 

**Fattura per acquisizione di beni/servizi da fornitore italiano nell’ambito dell’attività istituzionale**

In questo caso occorre scegliere: 

- Tipo: Istituzionale;
- Il sistema in automatico proporrà il Sezionale: ACQUISTI ISTITUZIONALE

**Fattura per acquisizione di beni/servizi da fornitore italiano nell’ambito dell’attività commerciale**

In questo caso occorre scegliere: 

- Tipo: Commerciale;
- Il sistema in automatico proporrà il Sezionale: REGISTRO IVA ORDINARIO AQCUISTI

**Fattura per acquisizione di beni da fornitore INTRA-UE nell’ambito dell’attività istituzionale**

In questo caso occorre scegliere: 

- Tipo: Istituzionale;
- Spuntare: Intra U.E.
- Servizi/Beni: Fattura di Beni
- Il sistema in automatico proporrà il Sezionale: REGISTRO ACQUISTI BENI ISTITUZIONALE NON RESIDENTI

Il Totale sarà pari al totale della fattura (pervenuta senza IVA) integrato dell’importo dell’IVA. 

**Fattura per acquisizione di servizi da fornitore INTRA-UE nell’ambito dell’attività istituzionale**

In questo caso occorre scegliere: 

- Tipo: Istituzionale;
- Spuntare: Intra U.E.
- Servizi/Beni: Fattura di Servizi
- Il sistema in automatico proporrà il Sezionale: REGISTRO ACQUISTI SERVIZI NON RESIDENTI

In questo caso è l’utente che deve scegliere tra i 2 registri proposti in quanto tale scelta guiderà la possibilità di ricerca dei servizi nel folder “dettaglio”. 

Il Totale sarà pari al totale della fattura (pervenuta senza IVA) integrato dell’importo dell’IVA nel caso di fattura di servizi da inserire nel sezionale registro acquisti servizi non residente. 
Il Totale sarà pari al totale della fattura (che dovrà pervenire già comprensiva di IVA) nel caso di fattura di servizi da inserire nel sezionale acquisti istituzionale.  

**Fattura per acquisizione di beni da fornitore INTRA-UE nell’ambito dell’attività commerciale**

In questo caso occorre scegliere: 

- Tipo: Commerciale;
- Spuntare: Intra U.E.
- In automatico si attiverà: Autofattura
- Servizi/Beni: Fattura di Beni
- Il sistema in automatico proporrà il Sezionale: REGISTRO IVA ORDINARIO ACQUISTI

Il Totale sarà pari al totale della fattura (pervenuta senza IVA) integrato dell’importo dell’IVA. 

**Fattura per acquisizione di beni da fornitore INTRA-UE provenienti da paesi EXTRA-UE nell’ambito dell’attività commerciale**

In questo caso occorre scegliere: 

- Tipo: Commerciale;
- Spuntare: Intra U.E.
- Spuntare: 'Merce da paesi extra U.E.'
- Il sistema proporrà in automatico 'Servizi/Beni' = Fattura di Beni
- Il sistema in automatico proporrà il Sezionale: REGISTRO IVA ORDINARIO ACQUISTI

Il Totale sarà pari al totale della fattura integrato dell’importo dell’IVA. 

**Fattura per acquisizione di servizi da fornitore INTRA-UE nell’ambito dell’attività commerciale**

In questo caso occorre scegliere: 

- Tipo: Commerciale;
- Spuntare: Intra U.E.
- In automatico si attiverà: Autofattura
- Servizi/Beni: Fattura di Servizi
- Il sistema in automatico proporrà il Sezionale: REGISTRO IVA ORDINARIO ACQUISTI

In questo caso è l’utente che deve scegliere se lasciare o togliere il flag su autofattura in quanto tale scelta guiderà la possibilità di ricerca dei servizi nel folder “dettaglio”. 
Il Totale sarà pari al totale della fattura (pervenuta senza IVA) integrato dell’importo dell’IVA nel caso di autofattura. 
Il Totale sarà pari al totale della fattura (che dovrà pervenire già comprensiva di IVA) nel caso in cui non venga emessa l’autofattura.  

**Fattura per acquisizione di beni da fornitore EXTRA-UE nell’ambito dell’attività istituzionale**

In questo caso occorre scegliere: 

- Tipo: Istituzionale;
- Spuntare: Extra U.E.
- Servizi/Beni: Fattura di Beni
- Il sistema in automatico proporrà il Sezionale: ACQUISTI ISTITUZIONALE

Il Totale sarà pari al totale della fattura. 

**Fattura per acquisizione di beni da fornitore EXTRA-UE con merce da paesi INTRA-UE nell’ambito dell’attività istituzionale**

In questo caso occorre scegliere: 

- Tipo: Istituzionale;
- Spuntare: Extra U.E.
- Spuntare: Merce da paesi intra U.E. 
- Servizi/Beni: Fattura di Beni
- Il sistema in automatico proporrà il Sezionale: REGISTRO ACQUISTI BENI ISTITUZIONALE NON RESIDENTI

Il Totale sarà pari al totale della fattura (pervenuta senza IVA) incrementato dell’importo dell’IVA. 

**Fattura per acquisizione di servizi da fornitore EXTRA-UE nell’ambito dell’attività istituzionale**

In questo caso occorre scegliere: 

- Tipo: Istituzionale;
- Spuntare: Extra U.E. 
- Servizi/Beni: Fattura di Servizi
- Il sistema in automatico proporrà il Sezionale: REGISTRO ACQUISTI SERVIZI NON RESIDENTI

In questo caso è l’utente che deve scegliere tra i 2 registri proposti in quanto tale scelta guiderà la possibilità di ricerca dei servizi nel folder “dettaglio”. 
Il Totale sarà pari al totale della fattura (pervenuta senza IVA) integrato dell’importo dell’IVA nel caso di fattura di servizi da inserire nel sezionale registro acquisti beni servizi non residenti. 
Il Totale sarà pari al totale della fattura (che dovrà pervenire già comprensiva di IVA) nel caso di fattura di servizi da inserire nel sezionale acquisti istituzionale.  

**Fattura per acquisizione di beni da fornitore EXTRA-UE nell’ambito dell’attività commerciale**

In questo caso occorre scegliere: 

- Tipo: Commerciale;
- Spuntare: Extra U.E. 
- Servizi/Beni: Fattura di Beni
- Il sistema in automatico proporrà il Sezionale: REGISTRO IVA ORDINARIO ACQUISTI

Il Totale sarà pari al totale della fattura. 

**Fattura per acquisizione di beni da fornitore EXTRA-UE con merce da paesi INTRA-UE nell’ambito dell’attività commerciale**

In questo caso occorre scegliere: 

- Tipo: Commerciale;
- Spuntare: Extra U.E. 
- Spuntare: Merce da paesi intra U.E. 
- Servizi/Beni: Fattura di Beni
- In automatico si attiverà: Autofattura
- Il sistema in automatico proporrà il Sezionale: REGISTRO IVA ORDINARIO ACQUISTI

Il Totale sarà pari al totale della fattura (pervenuta senza IVA) incrementato dell’importo dell’IVA. 

**Fattura per acquisizione di servizi da fornitore EXTRA-UE nell’ambito dell’attività commerciale**

In questo caso occorre scegliere: 

- Tipo: Commerciale;
- Spuntare: Extra U.E. 
- In automatico si attiverà: Autofattura
- Servizi/Beni: Fattura di Servizi
- Il sistema in automatico proporrà il Sezionale: REGISTRO IVA ORDINARIO ACQUISTI

In questo caso è l’utente che deve scegliere se lasciare o togliere il flag su autofattura in quanto tale scelta guiderà la possibilità di ricerca dei servizi nel folder “dettaglio”. 
Il Totale sarà pari al totale della fattura (pervenuta senza IVA) integrato dell’importo dell’IVA nel caso di autofattura. 
Il Totale sarà pari al totale della fattura (che dovrà pervenire già comprensiva di IVA) nel caso in cui non venga emessa l’autofattura.  

**Fattura per acquisizione di beni/servizi con IVA da fornitore di SAN MARINO nell’ambito dell’attività istituzionale**

In questo caso occorre scegliere: 

- Tipo: Istituzionale;
- Spuntare: San Marino con IVA 
- Il sistema in automatico proporrà il Sezionale: ACQUISTI ISTITUZIONALE

Il Totale sarà pari al totale della fattura. 

**Fattura per acquisizione di beni/servizi con IVA da fornitore di SAN MARINO nell’ambito dell’attività commerciale**

In questo caso occorre scegliere: 

- Tipo: Commerciale;
- Spuntare: San Marino con IVA 
- Il sistema in automatico proporrà il Sezionale: REGISTRO IVA ORDINARIO ACQUISTI

Il Totale sarà pari al totale della fattura. 

**Fattura per acquisizione di beni senza IVA da fornitore di SAN MARINO nell’ambito dell’attività istituzionale**

In questo caso occorre scegliere: 

- Tipo: Istituzionale;
- Spuntare: San Marino senza IVA 
- Servizi/Beni: Fattura di Beni
- Il sistema in automatico proporrà il Sezionale: REGISTRO ACQUISTI ISTITUZIONALE R.S.M. SENZA IVA

Il Totale sarà pari al totale della fattura integrato dell’importo dell’IVA. 

**Fattura per acquisizione di servizi senza IVA da fornitore di SAN MARINO nell’ambito dell’attività istituzionale**

In questo caso occorre scegliere: 

- Tipo: Istituzionale;
- Spuntare: San Marino senza IVA 
- Servizi/Beni: Fattura di Servizi
- Il sistema in automatico proporrà il Sezionale: REGISTRO ACQUISTI SERVIZI NON RESIDENTI

In questo caso è l’utente che deve scegliere tra i 2 registri proposti in quanto tale scelta guiderà la possibilità di ricerca dei servizi nel folder “dettaglio”. 
Il Totale sarà pari al totale della fattura (pervenuta senza IVA)  integrato dell’importo dell’IVA nel caso di fattura di servizi da inserire nel sezionale registro acquisti beni istituzionali non residente. 
Il Totale sarà pari al totale della fattura (che dovrà pervenire già comprensiva di IVA) nel caso di fattura di servizi da inserire nel sezionale acquisti istituzionale.  

**Fattura per acquisizione di beni senza IVA da fornitore di SAN MARINO nell’ambito dell’attività commerciale**

In questo caso occorre scegliere: 

- Tipo: Commerciale;
- Spuntare: San Marino senza IVA 
- In automatico si attiverà: Autofattura
- Servizi/Beni: Fattura di Beni
- Il sistema in automatico proporrà il Sezionale: REGISTRO IVA ORDINARIO ACQUISTI

Il Totale sarà pari al totale della fattura (pervenuta senza IVA)  integrato dell’importo dell’IVA. 

**Fattura per acquisizione di servizi senza IVA da fornitore di SAN MARINO nell’ambito dell’attività commerciale**

In questo caso occorre scegliere: 

- Tipo: Commerciale;
- Spuntare: San Marino senza IVA 
- In automatico si attiverà: Autofattura
- Servizi/Beni: Fattura di Servizi
- Il sistema in automatico proporrà il Sezionale: REGISTRO IVA ORDINARIO ACQUISTI

In questo caso è l’utente che deve scegliere se lasciare o togliere il flag su autofattura in quanto tale scelta guiderà la possibilità di ricerca dei servizi nel folder “dettaglio”. 
Il Totale sarà pari al totale della fattura (pervenuta senza IVA) integrato dell’importo dell’IVA nel caso di autofattura 
Il Totale sarà pari al totale della fattura (che dovrà pervenire già comprensiva di IVA) nel caso in cui non venga emessa l’autofattura.  

**Fattura spedizioniere nell’ambito dell’attività istituzionale**

In questo caso occorre scegliere: 

- Tipo: Istituzionale;
- Spuntare: Spedizioniere
- e ricercare la fattura passiva a cui si riferisce nell'apposito campo 'Fattura estera'

**Fattura spedizioniere nell’ambito dell’attività commerciale**

In questo caso occorre scegliere: 

- Tipo: Commerciale;
- Spuntare: Spedizioniere
- e ricercare la fattura passiva a cui si riferisce nell'apposito campo 'Fattura estera'

**Bolla Doganale nell’ambito dell’attività istituzionale**

In questo caso occorre scegliere: 

- Tipo: Istituzionale;
- Spuntare: Bolla Doganale
- e ricercare la fattura passiva a cui si riferisce nell'apposito campo 'Fattura estera'

**Bolla Doganale nell’ambito dell’attività commerciale**

In questo caso occorre scegliere: 

- Tipo: Commerciale;
- Spuntare: Bolla Doganale
- e ricercare la fattura passiva a cui si riferisce nell'apposito campo 'Fattura estera'



**FORNITORE**

Nel caso di fornitore italiano il sistema verifica che in anagrafica siano presenti sia la partita IVA che il codice fiscale. 
Nel caso di fornitore INTRA-UE il sistema verifica la correttezza della partita IVA. 
Il sistema nell’effettuare la ricerca è guidato dalle informazioni specificate in testata. 
Verificare sempre la correttezza delle modalità di pagamento. Qualora siano presenti più tipologie nelle modalità di pagamento è possibile cliccando sul pulsante 'conto/i' modificare quella proposta dal sistema. 

**DETTAGLIO**

Per procedere all’inserimento sarà necessario cliccare su 'Nuovo', si creerà una nuova riga che attiverà i campi sottostanti:

-	Tipo. Viene compilatato in automatico dal sistema secondo la scelta effettuata in TESTATA 
-	Bene/Servizio. Dovrà essere inserito il bene/servizio fornito o cliccando su 'azzera' e digitando il codice di riferimento o cliccando su 'cerca'  e scegliendo dalla tabella beni/servizi sottostante o cliccando su 'ricerca guidata' e impostando dei parametri. La tabella viene filtrata in base alle scelte effettuate in TESTATA. 
-	I.V.A.  Il sistema di “default” propone il codice IVA collegato all’IVA ordinaria se in TESTATA è stato scelto il TIPO “Commerciale”; in caso di TIPO “Istituzionale” il sistema di “default” propone il codice IVA “IST”. Cliccando su 'azzera' è possibile cambiare il codice IVA da applicare al dettaglio della fattura o inserendo direttamente il codice di riferimento o cliccando su 'cerca' e scegliendo dalla tabella proposta. 
-	Descrizione. Quanto riportato su questo campo apparirà nella stampa della fattura passiva. 
-	Competenza dal/al. Vengono proposti direttamente dal sistema uguali a quelli inseriti in testata ma possono essere modificati dall’utente purché ricompresi nell’intervallo del periodo inserito in testata. 
-	Quantità. Inserire e salvare attraverso l'apposita icona 'conferma'. In caso di errore utilizzare 'annulla' per annullare l’operazione. 
-	Prezzo Unitario inserire l’importo del singolo bene/servizio e salvare attraverso l'apposita icona 'conferma'. In caso di errore utilizzare  'annulla' per annullare l’operazione. 
-	Imponibile viene calcolato dal sistema (quantità * prezzo unitario).  
-	Importo IVA viene calcolato dal sistema e dovrà/potrà essere modificato dall’utente solo in caso di non quadratura tra il totale calcolato da SIGLA e il totale fatturato (totale fatturato 550,00 - imponibile 454,54 - iva 95,45 - totale 549,99, in tal caso si aggiunge 1 cent. sull’importo IVA si salva attraverso l'apposita icona 'conferma'. Apparirà il messaggio “L'importo IVA e' forzato”.  
-	Al fine del salvataggio la somma “Imponibile” + “Importo IVA” deve coincidere con il totale inserito in TESTATA. 
-	Mod. pagamento il sistema propone la stessa modalità prevista nel folder FORNITORE. Unico caso in cui deve essere cambiato è per il pagamento ad Equitalia. 
 
Dopo la compilazione del folder “Dettaglio” si deve provvedere alla contabilizzazione della fattura (legare la stessa all’impegno) si spunta il flag sul dettaglio che si vuole contabilizzare, si clicca sul pulsante 'contabilizza' e appare la maschera di ricerca o creazione dell’impegno.  Naturalmente sarà possibile creare un impegno se di competenza o su residui impropri. Il sistema, al fine del salvataggio, verifica che l’importo della scadenza dell’impegno sia pari al totale del dettaglio/dettagli della fattura che si vuole contabilizzare. 

Vi sono delle eccezioni e precisamente:  

- Fattura per acquisizione di beni da fornitore INTRA-UE nell’ambito dell’attività commerciale; 
-	Fattura per acquisizione di beni da fornitore INTRA-UE provenienti da paesi EXTRA-UE  nell’ambito  dell’attività commerciale; 
-	Fattura per acquisizione di beni da fornitore EXTRA-UE con merce da paesi INTRA-UE nell’ambito dell’attività commerciale; 
-	Fattura per acquisizione di servizi da fornitore INTRA-UE nell’ambito dell’attività commerciale in caso di emissione di autofattura; 
-	Fattura per acquisizione di servizi da fornitore EXTRA-UE nell’ambito dell’attività commerciale in caso di emissione di autofattura; 
-	Fattura per acquisizione di servizi senza IVA da fornitore di SAN MARINO nell’ambito dell’attività commerciale in caso di emissione di autofattura. 

In questi casi l’importo della scadenza dell’impegno dovrà essere pari all’importo dell’imponibile del dettaglio della fattura.  
Nel caso di acquisizione di un bene inventariabile al momento della contabilizzazione il sistema propone la voce del piano su cui dovrà essere imputato l’impegno. 
 

**CONSUNTIVO**

Tale folder viene compilato in automatico dal sistema tramite la compilazione del folder “dettaglio” e riepiloga i dettagli per codice IVA. 


**IMPEGNI**

Tale folder viene compilato in automatico in seguito alla contabilizzazione del dettaglio. Nella parte superiore vengono riportate le scadenze dell’impegno e facendo diventare blu il rigo della scadenza nella parte sottostante sono evidenziati i dettagli della fattura collegati alla scadenza dell’impegno. 

**DOCUMENTO 1210**

Se tra le modalità di pagamento è stato scelto il modello 1210 è necessario, prima di inviare il modello cartaceo in banca, posizionarsi su questo folder e cliccare sul pulsantecrea le 'ttera pagamento estero', questo per consentire al sistema di blindare la disponibilità di cassa presente su SIGLA per l’importo pari alla fattura. Si ricorda che il modello 1210 cartaceo deve essere compilato per l’importo pari a quello della fattura cartacea e non per quello che viene inserito in SIGLA. 
Successivamente alla lavorazione del modello 1210 la banca genera un sospeso di spesa che può essere richiamato tra i sospesi di spesa del c/c del CdR. Per poter emettere il mandato a regolamento sospeso prima è necessario legare il sospeso alla lettera di pagamento nel seguente modo: si richiama la fattura passiva ci si posiziona sul folder “Documento 1210”, si richiama il sospeso cliccando su 'azzera' e digitando il codice di riferimento o cliccando su 'cerca' e scegliendo dalla tabella sottostante o cliccando su 'ricerca guidata' e impostando dei parametri, si indicano nell’apposito record le commissioni e poi si adegua la scadenza dell’impegno per consentire il salvataggio. Anche in questo caso non avremo più l’uguaglianza tra il totale della scadenza impegno e il totale del dettaglio fattura,  questo per consentire  la contabilizzazione sia delle commissioni bancarie che l’eventuale oscillazione cambio. 

Nel caso di:

-	Fattura di acquisizione di beni da fornitore INTRA-UE nell’ambito istituzionale; 
-	Fattura di acquisizione di servizi da fornitore INTRA-UE nell’ambito istituzionale (registro acquisti servizi non residenti); 
-	Fattura per acquisizione di beni da fornitore EXTRA-UE con merce da paesi INTRA-UE nell’ambito dell’attività istituzionale; 
-	Fattura di acquisizione di servizi da fornitore EXTRA-UE nell’ambito istituzionale (registro acquisti servizi non residenti). 

La scadenza dell’impegno sarà pari all’importo del sospeso aumentato dell’importo totale IVA (come dal folder Consuntivo).  
Negli altri casi la scadenza dell’impegno sarà pari all’importo del sospeso. 


**INTRASTAT**

Se in testata è stato spuntato il flag 'INTRA-UE' sarà necessario, ove previsto,  compilare anche il folder “INTRASTAT”  

**Acquisizione di beni**
Per procedere all’inserimento sarà necessario cliccare su 'Nuovo', si creerà una nuova riga e si dovrà procedere alla compilazione dei record richiesti.

**Acquisizione di servizi**
Per procedere all’inserimento sarà necessario cliccare su 'Nuovo', si creerà una nuova riga e si dovrà procedere alla compilazione dei record richiesti.

**CONCLUSIONI**

Si ricorda che sarà possibile procedere al salvataggio della fattura solo se tutti i folder interessati sono stati compilati. Nel caso di acquisizione di beni durevoli è necessario che tutti i beni siano stati inventariati. Tale operazione può essere gestita tramite i pulsanti presenti nella maschera principale della fattura:  

- Inventaria 
- Associa
- Aumento Valore



