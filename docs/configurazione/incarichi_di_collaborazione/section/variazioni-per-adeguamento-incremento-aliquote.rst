Variazioni per “Adeguamento Incremento Aliquote”
=================================================

Le variazioni relative all’incremento delle aliquote previdenziali e assicurative si riferiscono esclusivamente all’ammontare degli oneri a carico dell’ente.

La variazione quindi incide sulla spesa complessiva presunta e può essere di importo non superiore all’incremento dell’aliquota stessa.

Una volta inserito l’importo della variazione è necessario aggiornare la ripartizione degli importi per anno. A tale fine, per facilitare l’adeguamento è stato implementato
nel riepilogo anche l’importo delle variazioni.

Una volta adeguati gli importi, sul folder “Procedura” viene visualizzato l’importo della variazione.

**Attenzione :** *dopo il salvataggio non è più consentito modificare la variazione.*

Variazioni per “Adeguamento alla durata del progetto nel periodo transitorio”
------------------------------------------------------------------------------

Le variazioni relative all’adeguamento dell’incarico alla durata del progetto si riferiscono esclusivamente a quei contratti che rientrano nel periodo transitorio.

Tali variazioni consentono la modifica dell’importo lordo percipiente, della durata del contratto e del file di “tipo contratto” da pubblicare sul sito istituzionale dell’Ente.

La registrazione deve essere completata in tre fasi.

**1° FASE**

Inserire l’importo della variazione del lordo percipiente. Una volta inserito l’importo della variazione, è necessario aggiornare la ripartizione degli importi per anno.
A tale fine, per facilitare l’adeguamento è stato implementato nel riepilogo anche l’importo delle variazioni.

Procedere al salvataggio al fine di verificare i limiti di spesa laddove sia necessario. Vedere il paragrafo successivo per la verificare dell'esistenza dei limiti di spesa.

**2° FASE**

Completare l’inserimento di tutte le informazioni richieste.

-  Data di stipula ( la data in cui è stata sottoscritta dalle parti la modifica del contratto);

-  Nuova data di fine Incarico;

-  Descrizione della variazione;

-  File da pubblicare.

**N.B. Il file da allegare dovrà contenere sia il contratto che la modifica sottoscritta dalle parti**

**3° FASE**

Procedere al salvataggio “definitivo” della variazione per rendere effettiva la modifica.

**Attenzione :** *dopo il salvataggio non è più consentito modificare la variazione.*

Verifica dei limiti
-------------------

La verifica dei limiti viene effettuata dal sistema al momento del salvataggio di una procedura di conferimento incarichi quando ancora è in stato “Provvisoria”.

Il sistema controlla che per la combinazione (tipo incarico, tipo attività e natura) e per gli esercizi selezionati non siano stati superati i limiti; qualora questi ultimi siano stati superati o non abbiano la capienza sufficiente per l’intero importo, il sistema restituirà un messaggio che indica, qualora esista, l’eventuale disponibilità a
registrare un incarico per la combinazione e l’esercizio selezionata.

Gestione Compensi
-----------------

Nella funzione “Compensi” sono stati aggiunti, nel folder “Terzo”, i campi per inserire i riferimenti dell’incarico conferito.

E’ obbligatorio inserire l’incarico per tutti i trattamenti collegati ai rapporti :

-  COLL;

-  PROF;

-  OCCA;

per tutte le altre tipologie i campi sopra evidenziati non sono neanche visibili.

N.B. l’obbligatorietà NON esiste per i compensi generati da missione e da conguaglio a prescindere dal tipo di rapporto.


Controlli per il collegamento di un incarico ad un compenso
-----------------------------------------------------------

Il collegamento di un incarico alla funzione compenso è soggetto ai seguenti controlli:

-  l’incarico deve essere in stato “Definitivo”;

-  l’unità organizzativa dell’incarico deve essere la medesima del compenso;

-  il terzo dell’incarico deve essere il medesimo del compenso;

-  **le date di competenza del compenso devono essere coerenti con le date indicate nell’incarico**

**(data inizio, data fine e data proroga del folder “Incarichi”);**

-  il tipo istituzionale o commerciale deve essere il medesimo dell’incarico;

-  il rapporto, se selezionato prima di aver collegato l’incarico, deve essere il medesimo dell’incarico stesso;

Il collegamento avviene tra il compenso ed il dettaglio dell’incarico relativo agli “importi per anno”, quindi per un soggetto a cui è stato affidato un incarico e la cui copertura finanziaria grava su più esercizi, al momento della registrazione del compenso dovrà essere selezionata la riga opportuna. 

Le informazioni da completare sono:
“Es./Id./Es. Finanziario”: rappresentano l’esercizio, l’identificativo dell’incarico e l’esercizio finanziario indicato nel folder “importi per anno”.
“Importo utilizzato”: è la somma dei compensi già collegati al medesimo incarico ed il medesimo esercizio finanziario.

Una volta collegato l’incarico nella descrizione del compenso viene aggiunto l’oggetto dell’attività dell’incarico stesso.

Controlli per il collegamento dell’impegno al compenso con incarico
-------------------------------------------------------------------

L’impegno collegato al compenso deve:

-  gravare esclusivamente su GAE della stessa natura del’incarico (fonti interne, fonti esterne);

-  l’esercizio dell’impegno deve essere uguale all’esercizio su cui grava la copertura finanziaria del’incarico (ad esempio se l’esercizio di copertura finanziaria è il 2006, si dovrà collegare al compenso un impegno residuo, proprio o improprio, del 2006).

Gestione minicarriere
---------------------

Nella funzione “Minicarriera”, nel folder “terzo/tipologie”, sono stati aggiunti i campi relativi all’incarico.

Il collegamento dell’incarico alla minicarriera segue le medesime regole del compenso.

Impegno
-------

La gestione degli incarichi stipulati dal CNR comporta delle ricadute anche nella gestione degli impegni. Qualora l’importo dell’impegno sia maggiore o uguale a 10.000, 00 € sarà obbligatorio inserire il riferimento all’incarico registrato.

Il collegamento dell’incarico all’impegno segue le medesime regole per il collegamento del contratto (il terzo creditore dell’impegno deve essere il medesimo dell’incarico).

E’ possibile collegare ad un impegno solo incarichi che sono in stato “Definitivo” oppure “Inviato Corte dei Conti”.

Si ricorda che NON è necessario inserire i dati dell’incarico nel repertorio contratti.

Nel caso in cui il contratto di conferimento dell’incarico preveda la possibilità di rimborsare anche le spese sostenute ovvero nel caso in cui vengano conferiti incarichi a soggetti residenti in Italia per i quali sia previsto il solo rimborso delle spese di trasferta, *l’ammontare di tali rimborsi non deve essere inserito nell’importo complessivo dell’incarico da registrare in SIGLA*.

Per gestire tali spese occorre distinguere due casi a seconda che l’incarico sia affidato nella forma di collaborazione occasionale (con o senza partita iva) oppure di collaborazione coordinata e continuativa. Per le collaborazioni occasionali sono stati creati in SIGLA degli appositi trattamenti denominati **“Rimborso Spese”** (es. Rimborso Spese - Prestazione occasionale con INPS 24,72 %).

Tali trattamenti non richiedono il riferimento all’incarico per procedere al pagamento.

**Naturalmente è fatto assoluto divieto di utilizzare tali trattamenti per effettuare il pagamento dei compensi, in quanto tale comportamento falserebbe la funzione di controllo dei limiti di spesa.**

Invece, nel caso di collaborazioni coordinate e continuative il rimborso delle spese deve essere gestito nella procedura delle Missioni.

**Gestione del caso particolare “Studio Associato”**

