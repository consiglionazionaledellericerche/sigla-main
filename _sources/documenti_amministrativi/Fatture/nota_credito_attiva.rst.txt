===================
Nota Credito Attiva
===================

Per poter procedere alla registrazione di una nota di credito bisogna entrare nella funzionalità della gestione delle Fatture Attive, richiamare la fattura attiva a cui si riferisce la nota di credito e cliccare sul pulsante 'Genera Nota Credito'. E’ sempre possibile emettere una nota di credito su fatture emesse nell’esercizio in corso (siano esse in stato “contabilizzato” o “incassato”); è possibile emettere note di credito su fatture emesse negli esercizi precedenti se sono in stato contabilizzato, qualora invece siano state incassate è possibile emettere la nota di credito solo se l’incasso è avvenuto nell’esercizio in corso.

Le note credito, come tutti i documenti fiscali, devono essere firmate e inviate attraverso la piattaforma di Interscambio SDI. Per i dettagli, relativamente al processo di invio e di ricezione dei messaggi da SDI, consultare la sezione specifica :doc:`Fattura Attiva Elettronica<fattura_attiva_elettronica>`.

**TESTATA**

Analizziamo ora i vari campi presenti nel folder “Testata”  
- Progressivo Tale campo sarà assegnato automaticamente dal sistema al momento del salvataggio della nota di credito. 
- Data registrazione Tale campo viene proposto dal sistema e non deve essere mai modificato. 
- Registrazione IVA Tale campo sarà compilato automaticamente dal sistema successivamente alla protocollazione della nota di credito (che avviene con la firma e l'incio del documento elettronico alla piattaforma SDI). 
- N. Registrazione IVA generale Tale campo sarà compilato automaticamente dal sistema successivamente alla protocollazione della nota di credito. 
- Data Stampa Tale campo sarà compilato automaticamente dal sistema successivamente alla produzione della stampa della nota di credito (prodotto tramite la funzionalità di predisposizione alla firma). 
- Competenza dal  Competenza al viene proposta quella della fattura. In caso di fattura emessa nell’esercizio precedente è necessario indicare come competenza quella dell’esercizio in corso (in caso di emissione successiva al 28 febbraio). 
- Sezionale il sistema proporrà sempre ed esclusivamente “Registro IVA ordinario delle fatture emesse”.  
- Tipo Documento viene riportato quello della fattura attiva. 
- Totale Imponibile Tale campo sarà compilato automaticamente dal sistema con la compilazione del folder “dettaglio”. 
- Totale Iva Tale campo sarà compilato automaticamente dal sistema con la compilazione del folder “dettaglio”. 
- Totale Tale campo sarà compilato automaticamente dal sistema con la compilazione del folder “dettaglio”. 
- Descrizione libera. 
- Mod. pagamento saranno indicate quelle della fattura attiva. 

**CLIENTE**

Tale folder è compilato automaticamente dal sistema che riporta i dati presenti nel corrispondente folder della fattura attiva. 

**DETTAGLIO**

Per procedere all’inserimento sarà necessario cliccare su 'Nuovo', il sistema proporrà un elenco di tutte le fatture attive emesse a favore del cliente, selezionando la riga corrispondente alla fattura a cui collegare la nota di credito, il sistema propone il/i dettaglio/i della fattura. Selezionando i dettagli a cui legare la nota di credito, il sistema riporta le righe selezionate nel folder dettaglio fattura.  Evidenziando la riga sarà possibile procedere alla modifica dei dati riportati qualora sia necessario. 
-	Bene/Servizio. Tale record non è modificabile: è lo stesso indicato nella fattura attiva. 
-	I.V.A. Il sistema di “default” propone il codice IVA scelto nella fattura attiva. Può essere modificato solo nel caso in cui siano trascorsi più 365 giorni dal protocollo della fattura attiva. Cliccando su 'azzera' e successivamente su 'cerca' il sistema propone solo il codice IVA permesso (VA). 
 
-	Competenza dal/Competenza al. Viene proposta quella del folder ”dettaglio” della fattura. Qualora sia stata modificata la data di competenza in TESTATA tali date devono essere riportate anche in questi campi. 
-	Quantità. Viene proposta quella della fattura. In caso di nota di credito parziale (e naturalmente quantità inserita diversa da 1) dovrà essere indicata la quantità per cui si emette la nota di credito. In caso di nota di credito totale tale campo non deve essere modificato. 
-	Prezzo. Viene proposto quello della fattura. In caso di nota di credito parziale dovrà essere indicato il prezzo per cui si emette la nota di credito. In caso di nota di credito totale tale campo non deve essere modificato. 
-	Importo IVA. Viene calcolato dal sistema e dovrà/potrà essere modificato dall’utente solo in caso di non quadratura tra il totale della prestazione e il totale fatturato (totale prestazione 550,00 - imponibile 454,54 - iva 95,45 - totale 549,99, in tal caso si aggiunge 1 cent. sull’importo IVA si salva attraverso l'apposita icona 'conferma', apparirà il messaggio “L'importo IVA e' forzato” . Tale campo sarà riportato in Testata. 
-	Imponibile. Viene calcolato dal sistema (quantità * prezzo unitario). Tale campo sarà riportato in Testata 
-	Totale. Viene calcolato dal sistema (importo IVA + imponibile). Tale campo sarà riportato automaticamente in Testata. 
 
Dopo la compilazione del folder “Dettaglio” si deve provvedere alla contabilizzazione della nota di credito (legare la stessa all’accertamento). Si seleziona il dettaglio che si vuole contabilizzare, si clicca sul pulsante 'contabilizza' e il sistema si posizionerà automaticamente sul folder accertamenti. 

**CONSUNTIVO**

Tale folder viene compilato in automatico dal sistema tramite la compilazione del folder “dettaglio” e riepiloga i dettagli per codice IVA. 

**ACCERTAMENTI**

Dopo la contabilizzazione del dettaglio il sistema si posiziona sul folder accertamenti. Si deve selezionare la riga e cliccare su 'aggiorna in manuale', il sistema rimanda alla maschera degli accertamenti folder “scadenziario” della scadenza collegata al dettaglio della fattura: in caso di nota di credito per l’importo totale tale scadenza dovrà essere pari a zero (ricordarsi di modificare anche  il dettaglio scadenza);  in caso di nota di credito di importo parziale ridurre la scadenza per l’importo pari alla nota di credito, creare una nuova scadenza di pari importo della nota di credito (in caso di accertamento residuo) o ridurre di pari importo il totale dell’accertamento di competenza;  posizionarsi nuovamente sulla riga collegata alla fattura e cliccare 'riporta'. Il sistema si riposizionerà nel folder accertamenti della nota di credito e si potrà procedere al salvataggio della nota. 


**IMPEGNI**

Se la nota di credito è collegata a una fattura incassata e non sono presenti fatture dello stesso cliente da incassare, il sistema dopo la contabilizzazione del dettaglio  propone la maschera della creazione e ricerca dell’impegno. Dopo aver compilato l’impegno ed effettuato “riporta” si ritorna su questo folder e si devono scegliere le modalità di pagamento precedentemente inserite nel codice terzo del cliente. 

**PROTOCOLLO**

Dopo aver salvato il documento, ai fini dell’inserimento nel registro IVA e per l’assegnazione del relativo numero, è necessario protocollare.  
Con l'introduzione della Fatturazione elettronica non è più necessario effettuare il Protocollo manuale attraverso l'apposita funzionalità perchè questo avviene automaticamente con la firma del documento elettronico.

**Caso Particolare**

Un caso particolare di Nota Credito Attiva, è rappresentato dalle Note credito 'interne' che vengono generate automaticamente dalla procedura Sigla allo SCARTO di una fattura attiva da parte del Sistema di Interscambio (SDI). In questo caso la Nota credito generata automaticamente ha l'obiettivo di 'sistemare' fiscalmente la fattura emessa e  protocollata che deve essere totalmente stornata. La nota credito viene firmata allo stesso modo della fattura e non viene inviata al destinatario che, in questo caso, non ha mai ricevuto la fattura a cui si riferisce la Nota.



