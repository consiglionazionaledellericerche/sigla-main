====================================================================
La gestione degli incarichi di collaborazione con la procedura SIGLA
====================================================================

Nella procedura di contabilità del CNR SIGLA è stata implementata la gestione del nuovo “disciplinare per il conferimento degli incarichi di collaborazione” (provvedimento Vice Presidente n. 64 prot. PRESID-CNR 0006498 del 14/11/2007). Le nuove funzioni sono disponibili nel menù di SIGLA nell’ambito della macroarea di CONFIGURAZIONE.

Con le funzioni suddette è possibile gestire sia il di conferimento di un singolo incarico **(Mono-Incarico)** che l’affidamento di più incarichi della stessa tipologia con un’unica procedura di conferimento **(Multi-Incarico)**.

Naturalmente, in quest’ultimo caso, è necessario che gli incarichi da conferire abbiano:

-  la medesima attività da svolgere; • il medesimo importo del corrispettivo;

-  la medesima tipologia contrattuale.

Sono stati rilasciati i seguenti accessi :

+-----------------------+-----------------------+-----------------------+
| **Nome accesso**      | **Descrizione**       | **Percorso nel menù   |
|                       |                       | dell’applicazione**   |
+=======================+=======================+=======================+
| CFGINCARICHIINCRICM   | Gestione              | Configurazione /      |
|                       |                       | Incarichi di          |
|                       | Verifica              | collaborazione /      |
|                       | professionalità       | Verifica              |
|                       | interna               | professionalità       |
|                       |                       | interna / gestione    |
+-----------------------+-----------------------+-----------------------+
| CFGINCARICHIINCRICV   | Visualizzazione       | Configurazione /      |
|                       |                       | Incarichi di          |
|                       | Verifica              | collaborazione /      |
|                       | professionalità       | Verifica              |
|                       | interna               | professionalità       |
|                       |                       | interna /             |
|                       |                       | visualizzazione       |
+-----------------------+-----------------------+-----------------------+
| CFGINCARICHIINCPRCM   | Gestione              | Configurazione /      |
|                       |                       | Incarichi di          |
|                       | Procedura             | collaborazione /      |
|                       | Conferimento          | Procedura             |
|                       | Incarichi             | Conferimento          |
|                       |                       | Incarichi / gestione  |
+-----------------------+-----------------------+-----------------------+
| CFGINCARICHIINCPRCV   | Visualizzazione       | Configurazione /      |
|                       |                       | Incarichi di          |
|                       | Procedura             | collaborazione /      |
|                       | Conferimento          |                       |
|                       | Incarichi             | Procedura             |
|                       |                       | Conferimento          |
|                       |                       | Incarichi /           |
|                       |                       | visualizzazione       |
+-----------------------+-----------------------+-----------------------+
| CFGINCARICHIINCREPV   | Visualizzazione       | Configurazione /      |
|                       | Incarichi             | Incarichi di          |
|                       |                       | collaborazione /      |
|                       |                       | Gestione Incarichi /  |
|                       |                       | visualizzazione       |
+-----------------------+-----------------------+-----------------------+
| CFGINCARICHITIPOATTV  | Visualizzazione Tipo  | Configurazione /      |
|                       | Attività              | Incarichi di          |
|                       |                       | collaborazione /      |
|                       |                       | tabelle di            |
|                       |                       | riferimento /Tipo     |
|                       |                       | Attività /            |
|                       |                       |                       |
|                       |                       | visualizzazione       |
+-----------------------+-----------------------+-----------------------+
| CFGINCARICHITIPOINCV  | Visualizzazione Tipo  | Configurazione /      |
|                       | Incarico              | Incarichi di          |
|                       |                       | collaborazione /      |
|                       |                       | tabelle di            |
|                       |                       | riferimento / Tipo    |
|                       |                       | Incarico /            |
|                       |                       |                       |
|                       |                       | visualizzazione       |
+-----------------------+-----------------------+-----------------------+

Si ricorda che, per assegnare i nuovi accessi agli utenti interessati, occorre collegarsi con l'utenza di amministratore del CDS, entrare in gestione utenza comune, selezionare il tab "Accessi", ricercare la UO su cui opera l'utente ed assegnare i nuovi accessi selezionandoli dalla lista degli "Accessi disponibili".

Inoltre si ricorda che le funzioni riservate esclusivamente al direttore (DIRIST) possono essere assegnate ad un’utenza associando a questa il ruolo “INCARICHI - Accessi per incarichi compresa pubblicazione”.

Verifica Professionalità Interne
================================

**Inserimento dati della richiesta**

La mappa di ricerca di professionalità interna va compilata con le solite regole delle mappe di SIGLA: i campi in bianco sono facoltativi, mentre quelli in colore giallo sono obbligatori e per facilitarne la compilazione è stato indicato anche la lunghezza massima di caratteri (compresi gli spazi) che possono contenere.

Si evidenzia che, nell’inserimento di una richiesta, il sistema imposta il richiedente sulla base della UO “selezionata in scrivania” ed il relativo indirizzo nel campo “Luogo di svolgimento”, con la possibilità di intervenire sul medesimo campo qualora il luogo di svolgimento dell’attività non corrisponda con la sede della UO.

Il campo “Nr Risorse Richieste”, che il sistema automaticamente imposta al valore predefinito “1”, deve essere modificato solo nel caso in cui si desidera, con la stessa “Verifica di professionalità interna” ricercare più risorse con gli stessi requisiti.

Si raccomanda, infine, di controllare in particolar modo la correttezza dell’indirizzo e-mail per le risposte perché sarà selezionabile direttamente dall’avviso pubblicato sul sito CNR per l’invio delle risposte.

Al momento del salvataggio viene assegnata una numerazione automatica alla richiesta e lo stato di Provvisoria . In questo stato è ancora possibile effettuare eventuali aggiunte e variazioni a quanto già inserito ed anche cancellare tutta la richiesta tramite il bottone Elimina.



.. [1]
    Cfr. art. 5 comma 4 del Disciplinare incarichi


