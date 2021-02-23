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

Progressivo. Tale campo sarà assegnato automaticamente dal sistema al momento del salvataggio della fattura. 
- Data registrazione. Tale campo viene proposto dal sistema e non deve essere mai modificato. 
- Data protocollo. campo non obbligatorio nel quale può essere indicato il protocollo di ricevimento della fattura. 
- Registrazione IVA. Tale campo sarà compilato automaticamente dal sistema in seguito alla chiusura mensile dei registri IVA. 
- N. Registrazione IVA. generale Tale campo sarà compilato automaticamente dal sistema in seguito alla chiusura mensile dei registri IVA. 
 
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






