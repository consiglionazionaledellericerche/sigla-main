====================
Nota Credito Passiva
====================

Per poter procedere alla registrazione di una nota di credito bisogna entrare nella funzionalità della gestione delle Fatture Passive, richiamare la fattura passiva a cui si riferisce la nota di credito e cliccare sul pulsante 'Genera Nota Credito'.
E’ sempre possibile emettere una nota di credito su fatture registrate nell’esercizio in corso (siano esse in stato “contabilizzato” che “pagato”); è possibile emettere note di credito su fatture registrate negli esercizi precedenti se sono in stato contabilizzato, qualora invece siano state pagate è possibile emettere la nota di credito solo se il pagamento è avvenuto nell’esercizio in corso. 
Sono di seguito illustrati i vari folder di cui si compone la maschera “Nota di credito”. 

**TESTATA**

Analizziamo ora i vari campi presenti nel folder “Testata”

-	Progressivo. Tale campo sarà assegnato automaticamente dal sistema al momento del salvataggio della nota di credito. 
-	Data registrazione. Tale campo viene proposto dal sistema e non deve essere mai modificato. 
-	Numero di protocollo. Deve essere riportato il numero di protocollo della nota di credito tale campo non è obbligatorio. 
-	Registrazione IVA. Tale campo sarà compilato automaticamente dal sistema dopo la chiusura mensile dei registri IVA. 
-	N. Registrazione IVA generale. Tale campo sarà compilato automaticamente dal sistema dopo la chiusura mensile dei registri IVA. 

- Tutti i campi relativi a:

    - Tipo;
    - INTRAUE;
    - EXTRAUE;
    - SAN MARINO CON IVA E SENZA IVA;
    - Spedizioniere;
    - Bolla doganale;
    - Autofattura;
    - Sezionale;
    
  Sono tutti ereditati dalla fattura a cui viene legata la nota di credito. 

-	N. Nota di credito. Deve essere inserito il numero della nota di credito. 
-	Descrizione. Il sistema riporta quello della fattura ma può essere modificata. 
-	Data emissione. Deve essere inserita la data di emissione della fattura del fornitore. 
-	Data di scadenza. Campo non obbligatorio va indicato la data di scadenza della fattura. (non serve per il trasferimento della liquidità da parte dell’Ufficio Bilancio che prende in considerazione le date di scadenza degli impegni liquidati). 
-	Esercizio fattura fornitore. Viene inserito in automatico dal sistema in base alla data di emissione della fattura.  
-	Competenza dal/Competenza al. Viene proposta quella della fattura. In caso di fattura con competenza dell’esercizio precedente è necessario mettere la competenza dell’esercizio in corso (in caso di registrazione successiva al 28 febbraio). Nel caso di bene inventariabile deve essere lasciata la data proposta in quanto l’imputazione del costo avviene tramite la quota di ammortamento. 
-	Totale. Deve essere indicato  l’importo della nota di credito. 

Nel caso di registrazione di nota di credito parziale non sarà possibile registrare il pagamento tramite modello 1210. Si consiglia pertanto di farsi emettere una nota di credito per il totale della vecchia fattura ed una nuova fattura per la differenza. 

**FORNITORE**

Tale folder è compilato automaticamente dal sistema che riporta i dati presenti nel corrispondente folder della fattura passiva. 

**DETTAGLIO**

Per procedere all’inserimento sarà necessario cliccare su 'Nuovo', il sistema proporrà un elenco di tutte le fatture passive registrate a favore del fornitore, si seleziona la riga corrispondente alla fattura a cui collegare la nota di credito, e tramite il pulsante 'selezione' il sistema proporrà il/i dettaglio/i della fattura precedentemente selezionata. Si seleziona la riga corrispondente ai dettagli a cui legare la nota di credito e il sistema riporta le righe selezionate nel folder dettaglio.  Evidenziando la riga sarà possibile procedere alla modifica dei dati riportati qualora sia necessario.

-	Tipo. Tale record non è modificabile: è lo stesso indicato nella fattura passiva. 
-	Bene/Servizio. Tale record non è modificabile: è lo stesso indicato nella fattura passiva. 
-	I.V.A. Il sistema di “default” propone il codice IVA scelto nella fattura passiva.  
-	Descrizione. Campo libero viene proposta quella inserita in testata. 
-	Competenza dal/Competenza al. Viene proposta quella del folder dettaglio della fattura. Qualora sia stata modificata la data di competenza in TESTATA tali date devono essere riportate anche in questi campi. 
	Descr. fattura origine. Viene riportata la descrizione del dettaglio della fattura di origine, campo non  editabile 
-	Quantità. Viene proposto quella della fattura. In caso di nota di credito parziale (e naturalmente quantità inserita diversa da 1) dovrà essere indicata la quantità per la quale si emette la nota di credito. In caso di nota di credito totale tale campo non deve essere modificato. 
-	Prezzo. Viene proposto quella della fattura. In caso di nota di credito parziale dovrà essere indicato il prezzo per cui si emette la nota di credito. In caso di nota di credito totale tale campo non deve essere modificato. 
-	Imponibile. Viene calcolato dal sistema (quantità * prezzo unitario).  
-	Imponibile EURO. Viene calcolato dal sistema. 
-	Importo IVA. Viene calcolato dal sistema e dovrà/potrà essere modificato dall’utente solo in caso di non quadratura tra il totale della nota di credito e il totale calcolato da SIGLA (totale prestazione 550,00 - imponibile 454,54 - iva 95,45 - totale 549,99, in tal caso si aggiunge 1 cent. sull’importo IVA si salva attraverso la vicina icona   conferma ed apparirà il messaggio “L'importo IVA e' forzato”. 
-	Al fine del salvataggio la somma “Imponibile” + “Importo IVA” deve coincidere con il totale inserito in TESTATA. 
-	Mod. pagamento. Viene proposto quella della fattura. 
 
Dopo la compilazione del folder “Dettaglio” si deve provvedere alla contabilizzazione della nota di credito della fattura (legare la stessa all’impegno) si seleziona il dettaglio che si vuole contabilizzare ed il sistema si posizionerà automaticamente sul folder storni. 

**CONSUNTIVO**

Tale folder viene compilato in automatico dal sistema tramite la compilazione del folder “dettaglio” e riepiloga i dettagli per codice IVA. 

**STORNI**

Dopo la contabilizzazione del dettaglio il sistema si posiziona sul folder storni. Si deve evidenziare la riga e cliccare sul pulsante aggiorna in manuale il sistema rimanda alla maschera degli impegni folder “scadenziario” sulla scadenza collegata al dettaglio della fattura: in caso di nota di credito per l’importo totale tale scadenza dovrà essere pari a zero (ricordarsi di modificare anche  il dettaglio scadenza);  in caso di nota di credito di  importo parziale  ridurre la scadenza per l’importo pari alla nota di credito, creare una nuova scadenza di pari importo della nota di credito (in caso di impegno residuo proprio) o ridurre di pari importo il totale dell’impegno di competenza o sui residui impropri; selezionare nuovamente la riga collegata alla fattura e cliccare su 'riporta'. Il sistema si riposizionerà nel folder storni della nota di credito e si può procedere al salvataggio della nota. 

**ACCERTAMENTI**

Se la nota di credito è collegata ad una fattura pagata, e non sono presenti fatture dello stesso fornitore da pagare, il sistema, dopo la contabilizzazione del dettaglio,  propone la maschera della creazione e ricerca dell’accertamento. Dopo la compilazione dell’accertamento cliccando  su “riporta” si ritorna su questo folder e si devono scegliere le modalità di incasso (c/c ente). 

**RIVEDERE**
**PROTOCOLLO**

Ai fini dell’inserimento nel registro IVA e per l’assegnazione del relativo numero al documento fiscale, è necessario protocollare.  
Con l'introduzione della Fatturazione elettronica non è più necessario effettuare il Protocollo manuale attraverso l'apposita funzionalità perchè questo avviene automaticamente in fase di registrazione del documento elettronico.



