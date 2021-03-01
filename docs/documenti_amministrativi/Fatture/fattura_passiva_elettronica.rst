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




