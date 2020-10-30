21.2.8.1Variazioni per “Adeguamento Incremento Aliquote”
========================================================

Le variazioni relative all’incremento delle aliquote previdenziali e
assicurative si riferiscono esclusivamente all’ammontare degli oneri a
carico dell’ente.

La variazione quindi incide sulla spesa complessiva presunta e può
essere di importo non superiore all’incremento dell’aliquota stessa.

|image0|

Una volta inserito l’importo della variazione, come illustrato nella
figura precedente, è necessario aggiornare la ripartizione degli importi
per anno. A tale fine, per facilitare l’adeguamento è stato implementato
nel riepilogo anche l’importo delle variazioni.

|image1|

Una volta adeguati gli importi, sul folder “Procedura” viene
visualizzato l’importo della variazione.

|image2|

**Attenzione :**\ *dopo il salvataggio non è più consentito modificare
la variazione.*

 21.2.8.2 Variazioni per “Adeguamento alla durata del progetto nel periodo transitorio”
---------------------------------------------------------------------------------------

Le variazioni relative all’adeguamento dell’incarico alla durata del
progetto si riferiscono esclusivamente a quei contratti che rientrano
nel periodo transitorio.

Tali variazioni consentono la modifica dell’importo lordo percipiente,
della durata del contratto e del file di “tipo contratto” da pubblicare
sul sito istituzionale dell’Ente.

La registrazione deve essere completata in tre fasi.

**1° FASE**

Inserire l’importo della variazione del lordo percipiente .

|image3|

Una volta inserito l’importo della variazione, come illustrato nella
figura , è necessario aggiornare la ripartizione degli importi per anno.
A tale fine, per facilitare l’adeguamento è stato implementato nel
riepilogo anche l’importo delle variazioni.

|image4|

Procedere al salvataggio al fine di verificare i limiti di spesa laddove
sia necessario. Consultare l’allegato 5 del manuale per verificare
l’esistenza dei limiti di spesa.

**2° FASE**

Completare l’inserimento di tutte le informazioni richieste.

|image5|

-  Data di stipula ( la data in cui è stata sottoscritta dalle parti la
   modifica del contratto);

-  Nuova data di fine Incarico; • Descrizione della variazione;

-  File da pubblicare.

**N.B. Il file da allegare dovrà contenere sia il contratto che la
modifica sottoscritta dalle parti come indicato nel messaggio
sottostante.**

|image6|

**3° FASE**

Procedere al salvataggio “definitivo” della variazione per rendere
effettiva la modifica (figura sottostante).

|image7|

**Attenzione :**\ *dopo il salvataggio non è più consentito modificare
la variazione.*

 21.2.9 Verifica dei limiti
---------------------------

La verifica dei limiti viene effettuata dal sistema al momento del
salvataggio di una procedura di conferimento incarichi quando ancora è
in stato “Provvisoria”.

|image8|

Il sistema controlla che per la combinazione (tipo incarico, tipo
attività e natura) e per gli esercizi selezionati non siano stati
superati i limiti; qualora questi ultimi siano stati superati o non
abbiano la capienza sufficiente per l’intero importo, il sistema
restituirà il seguente messaggio:

|image9|

il messaggio indica, qualora esista, l’eventuale disponibilità a
registrare un incarico per la combinazione e l’esercizio selezionata.

21.2.10 Gestione Compensi
-------------------------

Nella funzione “Compensi” sono stati aggiunti, nel folder “Terzo”, i
campi per inserire i riferimenti dell’incarico conferito.

|image10|

E’ obbligatorio inserire l’incarico per tutti i trattamenti collegati ai
rapporti :

-  COLL;

-  PROF;

-  OCCA;

per tutte le altre tipologie i campi sopra evidenziati non sono neanche
visibili.

N.B. l’obbligatorietà NON esiste per i compensi generati da missione e
da conguaglio a prescindere dal tipo di rapporto.

21.2.11 Controlli per il collegamento di un incarico ad un compenso
-------------------------------------------------------------------

Il collegamento di un incarico alla funzione compenso è soggetto ai
seguenti controlli:

-  l’incarico deve essere in stato “Definitivo”;

-  l’unità organizzativa dell’incarico deve essere la medesima del
   compenso;

-  il terzo dell’incarico deve essere il medesimo del compenso;

-  **le date di competenza del compenso devono essere coerenti con le
   date indicate nell’incarico**

**(data inizio, data fine e data proroga del folder “Incarichi”, vedi
paragrafo 19.2.6);**

-  il tipo istituzionale o commerciale deve essere il medesimo
   dell’incarico;

-  il rapporto, se selezionato prima di aver collegato l’incarico, deve
   essere il medesimo dell’incarico stesso;

Il collegamento avviene tra il compenso ed il dettaglio dell’incarico
relativo agli “importi per anno”, quindi per un soggetto a cui è stato
affidato un incarico e la cui copertura finanziaria grava su più
esercizi, al momento della registrazione del compenso dovrà essere
selezionata la riga opportuna. In una situazione del genere l’elenco che
si presenta all’utente che sta registrando il compenso è il seguente:

|image11|

Una volta selezionata la riga, le informazioni riportate sono le
seguenti:

|image12|

“Es./Id./Es. Finanziario”: rappresentano l’esercizio, l’identificativo
dell’incarico e l’esercizio finanziario indicato nel folder “importi per
anno” (cfr. paragrafo 19.2.5.).

“Importo utilizzato”: è la somma dei compensi già collegati al medesimo
incarico ed il medesimo esercizio finanziario.

Una volta collegato l’incarico nella descrizione del compenso viene
aggiunto l’oggetto dell’attività dell’incarico stesso.

21.2.12 Controlli per il collegamento dell’impegno al compenso con incarico
---------------------------------------------------------------------------

L’impegno collegato al compenso deve:

-  gravare esclusivamente su GAE della stessa natura del’incarico (fonti
   interne, fonti esterne);

-  l’esercizio dell’impegno deve essere uguale all’esercizio su cui
   grava la copertura finanziaria del’incarico (ad esempio se
   l’esercizio di copertura finanziaria è il 2006, si dovrà collegare al
   compenso un impegno residuo, proprio o improprio, del 2006).

21.2.13 Gestione minicarriere
-----------------------------

Nella funzione “Minicarriera”, nel folder “terzo/tipologie”, sono stati
aggiunti i campi relativi all’incarico.

|image13|

Il collegamento dell’incarico alla minicarriera segue le medesime regole
del compenso.

21.2.14 Impegno
---------------

La gestione degli incarichi stipulati dal CNR comporta delle ricadute
anche nella gestione degli impegni. Qualora l’importo dell’impegno sia
maggiore o uguale a 10.000, 00 € sarà obbligatorio inserire il
riferimento all’incarico registrato.

Il collegamento dell’incarico all’impegno segue le medesime regole per
il collegamento del contratto (il terzo creditore dell’impegno deve
essere il medesimo dell’incarico).

E’ possibile collegare ad un impegno solo incarichi che sono in stato
“Definitivo” oppure “Inviato Corte dei Conti”.

Si ricorda che NON è necessario inserire i dati dell’incarico nel
repertorio contratti.

|image14|

Nel caso in cui il contratto di conferimento dell’incarico preveda la
possibilità di rimborsare anche le spese sostenute ovvero nel caso in
cui vengano conferiti incarichi a soggetti residenti in Italia per i
quali sia previsto il solo rimborso delle spese di trasferta (cfr.
paragrafo 8), *l’ammontare di tali rimborsi non deve essere inserito
nell’importo complessivo dell’incarico da registrare in SIGLA*.

Per gestire tali spese occorre distinguere due casi a seconda che
l’incarico sia affidato nella forma di collaborazione occasionale (con o
senza partita iva) oppure di collaborazione coordinata e continuativa.
Per le collaborazioni occasionali sono stati creati in SIGLA degli
appositi trattamenti denominati **“Rimborso Spese”** (es. Rimborso Spese
- Prestazione occasionale con INPS 24,72 %).

Tali trattamenti non richiedono il riferimento all’incarico per
procedere al pagamento.

**Naturalmente è fatto assoluto divieto di utilizzare tali trattamenti
per effettuare il pagamento dei compensi, in quanto tale comportamento
falserebbe la funzione di controllo dei limiti di spesa.**

|image15|

Invece, nel caso di collaborazioni coordinate e continuative il rimborso
delle spese deve essere gestito nella procedura delle Missioni.

**21.3 Gestione del caso particolare “Studio Associato”**

.. |image0| image:: ./media/image70.png
   :width: 4.26667in
   :height: 2.63in
.. |image1| image:: ./media/image71.png
   :width: 4.08056in
   :height: 3.71389in
.. |image2| image:: ./media/image72.png
   :width: 5.94028in
   :height: 0.99444in
.. |image3| image:: ./media/image73.png
   :width: 6.71in
   :height: 2.76in
.. |image4| image:: ./media/image74.png
   :width: 4.14333in
   :height: 3.88667in
.. |image5| image:: ./media/image75.png
   :width: 7.1in
   :height: 5.21667in
.. |image6| image:: ./media/image76.png
   :width: 7.01in
   :height: 0.89333in
.. |image7| image:: ./media/image77.png
   :width: 5.83611in
   :height: 2.42778in
.. |image8| image:: ./media/image78.jpg
   :width: 3.61944in
   :height: 3.61944in
.. |image9| image:: ./media/image79.png
   :width: 6.07222in
   :height: 2.43889in
.. |image10| image:: ./media/image82.png
   :width: 4.04445in
   :height: 4.09583in
.. |image11| image:: ./media/image84.png
   :width: 6.37667in
   :height: 1.31in
.. |image12| image:: ./media/image85.png
   :width: 4.62083in
   :height: 1.39583in
.. |image13| image:: ./media/image86.png
   :width: 5.425in
   :height: 3.77083in
.. |image14| image:: ./media/image87.png
   :width: 3.98333in
   :height: 2.46121in
.. |image15| image:: ./media/image88.png
   :width: 6.81111in
   :height: 1.35972in
