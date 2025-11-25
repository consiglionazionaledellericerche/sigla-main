====================================================================
La gestione degli incarichi di collaborazione con la procedura SIGLA
====================================================================

Nella procedura di contabilità del CNR SIGLA è stata implementata la gestione del nuovo “disciplinare per il conferimento degli incarichi di collaborazione” (provvedimento Vice Presidente n. 64 prot. PRESID-CNR 0006498 del 14/11/2007). Le nuove funzioni sono disponibili nel menù di SIGLA nell’ambito della macroarea di CONFIGURAZIONE.

Con le funzioni suddette è possibile gestire sia il di conferimento di un singolo incarico **(Mono-Incarico)** che l’affidamento di più incarichi della stessa tipologia con un’unica procedura di conferimento **(Multi-Incarico)**.

Naturalmente, in quest’ultimo caso, è necessario che gli incarichi da conferire abbiano:

-  la medesima attività da svolgere;

-  il medesimo importo del corrispettivo;

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


**Pubblicazione della richiesta sul sito CNR**

Il bottone Pubblica è abilitato solo alle utenze in procedura che hanno il ruolo di Direttore di Istituto (DIRIST).

Il suo utilizzo, dopo la risposta affermativa al messaggio di conferma, effettua la pubblicazione dell’avviso sul sito CNR (Lavoro e Formazione – Avvisi di ricerca di professionalità interne).

Non sono più consentite ulteriori modifiche, come indicato nel relativo messaggio.

Viene impostato lo stato di Pubblicata e vengono inserite automaticamente, da sistema, le date di validità della richiesta secondo la seguente regola:

-  Data pubblicazione : data del giorno in cui si effettua la pubblicazione sul sito CNR;

-  Data fine pubblicazione : 7 giorni successivi alla data di pubblicazione;

-  Data scadenza : 90 giorni dopo la data di fine pubblicazione, entro i quali va conclusa anche la fase successiva della procedura di conferimento, cioè la decisione a contrattare.

In questa fase, è ancora possibile intervenire per ulteriori ripensamenti, mediante il bottone Annulla che in caso di risposta positiva al messaggio di conferma, annulla la pubblicazione sul sito modificando lo stato della richiesta in **Annullata.**

Gestione delle risposte dei dipendenti CNR
-------------------------------------------

La gestione delle risposte dei dipendenti CNR all’avviso, deve essere gestita mediante e_mail o altri strumenti cartacei. Sarà cura della UO richiedente tenere traccia delle candidature pervenute, al fine del successivo inserimento in SIGLA dell’esito della verifica delle professionalità interne.

Per facilitare l’invio delle candidature si può “cliccare” direttamente sull’avviso pubblicato sull’indirizzo di e_mail per le risposte ed inviare una mail al richiedente. La messaggistica predisposta ricorda che la candidatura è riservata solo ai dipendenti CNR e chiede di inserire nella mail oltre ad un recapito telefonico anche la matricola
CNR.

Esito della Ricerca di Professionalità Interne
----------------------------------------------

Il sistema controlla in automatico la scadenza delle richieste pubblicate. Il giorno successivo alla data di scadenza la richiesta viene posta in stato di Scadenza aprendo all’utenza, con il ruolo di Direttore di Istituto, la possibilità di inserire l’esito della ricerca interna.

Nel caso di:

**Mono-Incarico,** va compilato il riquadro dell’Esito della ricerca con le seguenti possibilità:

**Conforme alla richiesta** : la richiesta viene passata nello stato di chiusa e non viene attivato il bottone “Avvia Procedura Conferimento Incarico”.

**Non conforme alla richiesta** oppure **Non pervenute candidature :**

la richiesta viene sempre salvata nello stato di chiusa, ma viene attivato il bottone “Avvia Procedura Conferimento Incarico” che consente di avviare la Procedura di conferimento incarico (provvisoria) ad un soggetto esterno.

Nel caso di **Multi-Incarico**, va compilato il riquadro dell’Esito della ricerca individuando:

-  il numero di risorse trovate e **conformi alla richiesta**,

-  il numero delle risorse trovate ma **non conformi alla richiesta**;

-  il numero delle richieste per cui **non sono pervenute candidature**.

Dopo la compilazione delle informazioni sull’esito della ricerca, all’atto del salvataggio la “Verifica di professionalità interna” viene sempre salvata nello stato di “Chiusa”. Se il numero delle risorse non trovate è positivo viene attivato il bottone “Avvia Procedura Conferimento Incarico” che consente di procedere con la fase successiva
di registrazione della “Procedura di Conferimento Incarico” (provvisorio) ad uno o più soggetti esterni.

Il tasto “Avvia Procedura Conferimento Incarico” rimane attivo sino a quando non risultano associate alla “Verifica di professionalità interna” procedure per il conferimento per un numero di incarichi equivalente al numero delle risorse ancora da ricercare (nr. Risorse non conformi alla richiesta + nr. Richieste per le quali non sono pervenute candidature).

Se alla “Verifica di Professionalità interna” risulta collegata almeno una “Procedura di conferimento incarico” viene attivato il tab “Elenco Procedure incarichi Associate” per la loro consultazione. Tramite il tasto “Apri Procedura” è possibile accedere direttamente alla mappa di gestione della “Procedura di conferimento Incarichi” selezionata.

======================================================
Procedura Conferimento Incarichi e Gestione Incarichi
======================================================

La funzione “Procedura conferimento Incarichi” è stata implementata al fine di consentire agli utenti di gestire, con un’unica procedura, l’affidamento di più incarichi della stessa tipologia.

Nel caso di avvio di una “Procedura di conferimento **Multi-Incarico** la funzione, nel folder “Incarichi”, consente l’associazione di più incarichi.

*Procedura Conferimento Incarichi*
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

La funzione implementata in SIGLA consente la gestione di tutti gli adempimenti essenziali per l’affidamento di uno o più incarichi nella forma di collaborazione occasionale (con o senza partita Iva) o di collaborazione coordinata e continuativa. Tale funzione si compone di quattro folder :

**Procedura;**

**Importo per anno;**

**Incarichi;**

**Allegati;**

**Variazioni.**


Ogni “Procedura di conferimento incarichi” registrata in SIGLA è identificata dal sistema informativo dall’anno di registrazione e da un “Identificativo Procedura” che corrisponde al numero attribuito automaticamente dalla procedura al salvataggio dei dati, con progressivo numerico assoluto per anno.

*Gestione degli Stati*
~~~~~~~~~~~~~~~~~~~~~~

Anche per la gestione degli Stati conviene distinguere il caso di Procedura **Mono-Incarico** e **Multi-Incarichi.**

Stati per Procedura di conferimento Mono-Incarico
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

In questo caso gli Stati della Procedura di Conferimento Incarichi sono gli stessi di quelli dell’unico incarico ad essa associato. A seconda della procedura amministrativa utilizzata, gli stati possibili sono:

-  Provvisoria : la procedura registrata può essere modificata in ogni sua parte o eliminata totalmente;

-  Pubblicata : l’avviso per il conferimento di incarichi è pubblicato sul sito del CNR. Il passaggio di stato da provvisoria a pubblicata è consentito solo alle utenze che in procedura hanno il ruolo di Direttore di Istituto (DIRIST) e solo se è stato allegato almeno un file di tipo “Avviso da pubblicare” .

Il cambiamento di stato avviene cliccando sull’icona “Pubblica”. Durante la fase di pubblicazione *non è consentito modificare nessun campo*.

-  Annullata : il Dirigente/Direttore può decidere di annullare una procedura di conferimento incarico precedentemente pubblicata sul sito del CNR. Il cambiamento di stato avviene cliccando sull’icona “Annulla”, la pubblicazione dell’avviso sul sito del CNR viene eliminata e l’incarico non può più essere affidato.

-  In Scadenza : la procedura di conferimento incarico passa automaticamente in tale stato, qualora siano passati 14 giorni dalla data di pubblicazione dell’avviso di conferimento dell’incarico stesso sul sito del CNR. In questa fase è visibile il folder “Incarichi”; inoltre è possibile ripartire l’importo della spesa presunta dell’incarico su esercizi diversi da quello inserito al momento della pubblicazione, tale operazione, ovviamente, è consentita sempre nel rispetto dei limiti di spesa.

-  Scaduta : la procedura di conferimento incarico passa automaticamente in tale stato, qualora siano passati 74 giorni dalla data di pubblicazione dell’avviso di conferimento dell’incarico stesso sul sito del CNR.

-  Definitiva : Il passaggio di stato avviene cliccando sull’icona “salva definitivo”.

Il cambiamento di stato dell’incarico associato alla procedura è consentito solo se:

-  Sono stati inseriti tutti i dati relativi al contraente;

-  E’ stato allegato almeno un file di tipo “Contratto Stipulato” all’incarico;

-  E’ stato allegato almeno un file di tipo “Decisione a contrattare” alla procedura di conferimento incarico;

In stato “definitivo” l’incarico può essere collegato a compensi e/o minicarriere.

Inoltre è possibile modificare la ripartizione della spesa presunta sui vari esercizi, sempre nel rispetto dei limiti di spesa. 


-  Annullata : Il passaggio di stato, consentito solo al Direttore

-  Chiusa : Il passaggio di stato, consentito solo al Direttore, avviene cliccando sull’icona “Concludi Incarico”. Solo gli incarichi collegati ad almeno un compenso possono assumere tale stato. Un incarico risulta collegato ad un compenso quando “l’importo utilizzato” nel folder “importo per anno” è diverso da zero.

Il passaggio di stato comporta:

-  l’impossibilità di utilizzare l’incarico nei compensi e nelle minicarriere;

-  la riduzione dell’importo dell’incarico per un importo pari alla quota già utilizzato;

-  la storicizzazione dell’importo originario del contratto;

-  la possibilità di riutilizzare l’eventuale riferimento della ricerca di professionalità interna (sempre che sia ancora valida) per un altro incarico.

-  Eliminata : Il passaggio di stato, consentito solo al Direttore, avviene cliccando sull’icona “Concludi Incarico”. Solo gli incarichi che non siano stati collegati ad un compenso possono assumere tale stato. Un incarico risulta non collegato ad un compenso quando “l’importo utilizzato” nel folder “importo per anno” è uguale a zero.
   
   Il passaggio di stato comporta:

   -  l’impossibilità di utilizzare l’incarico nei compensi e nelle minicarriere;

   -  la possibilità di riutilizzare l’eventuale riferimento della ricerca di professionalità interna (sempre che sia ancora valida) per un altro incarico.

Stati per Procedura di conferimento Multi-Incarico
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Occorre distinguere gli Stati della “Procedura di conferimento incarichi” dagli Stati degli incarichi ad essa associati.

La “Procedura di conferimento incarichi”, a seconda della procedura amministrativa utilizzata, può assumere i seguenti stati:

-  Provvisoria : la procedura registrata può essere modificata in ogni sua parte o eliminata totalmente;

-  Pubblicata : l’avviso per il conferimento degli incarichi è pubblicato sul sito del CNR. Il passaggio di stato da provvisoria a pubblicata è consentito solo alle utenze che in procedura hanno il ruolo di Direttore di Istituto (DIRIST) e solo se è stato allegato almeno un file di tipo “Avviso da pubblicare”.

Il cambiamento di stato avviene cliccando sull’icona “Pubblica”. Durante la fase di pubblicazione *non è consentito modificare nessun campo*.

-  Annullata : il Dirigente/Direttore può decidere di annullare una procedura di conferimento incarichi precedentemente pubblicata sul sito del CNR. Il cambiamento di stato avviene cliccando sull’icona “Annulla”, la pubblicazione dell’avviso sul sito del CNR viene eliminata e gli incarichi non possono più essere affidati.

-  In Scadenza : la procedura di conferimento incarichi passa automaticamente in tale stato, qualora siano passati 14 giorni dalla data di pubblicazione dell’avviso di conferimento dell’incarico stesso sul sito del CNR. In questa fase è visibile il folder “Incarichi”.

-  Scaduta : la procedura di conferimento incarichi passa automaticamente in tale stato, qualora siano passati 74 giorni dalla data di pubblicazione dell’avviso di conferimento dell’incarico stesso sul sito del CNR e non risulta associato alcun incarico. Non possono essere più associati incarichi alla procedura.

-  Definitiva : Il passaggio di stato avviene in automatico quando tutti gli incarichi associati alla procedura sono definitivi.

Gli incarichi, associati alla “Procedura di conferimento incarichi”, possono assumere i seguenti stati:

-  Provvisorio : l’incarico può essere modificato in ogni sua parte o eliminato totalmente.


Il cambiamento di stato è consentito solo se:

-  E’ stata inserita la data di Stipula;

-  Sono stati inseriti tutti i dati relativi al contraente;

-  E’ stato allegato almeno un file di tipo “Contratto Stipulato” all’incarico;

-  E’ stato allegato almeno un file di tipo “Decisione a contrattare” alla procedura di conferimento incarico.

-  Definitivo : Il passaggio di stato avviene cliccando sull’icona “salva definitivo”

Il cambiamento di stato dell’incarico associato alla procedura è consentito solo se:

-  Sono stati inseriti tutti i dati relativi al contraente;

-  E’ stato allegato almeno un file di tipo “Contratto Stipulato” all’incarico;

-  E’ stato allegato almeno un file di tipo “Decisione a contrattare” alla procedura di conferimento incarico.

In stato “definitivo” l’incarico può essere collegato a compensi e/o minicarriere.

Inoltre è possibile modificare la ripartizione della spesa presunta sui vari esercizi, sempre nel rispetto dei limiti di spesa.

-  Annullato : Il passaggio di stato, consentito solo al Direttore, avviene nel caso in cui è stato

-  Chiuso : il passaggio di stato, consentito solo al Direttore, avviene cliccando sull’icona “Concludi Incarico”. Solo gli incarichi collegati ad almeno un compenso possono assumere tale stato. Un incarico risulta collegato ad un compenso quando “l’importo utilizzato” nel folder “importo per anno” è diverso da zero .

Il passaggio di stato comporta:

-  l’impossibilità di utilizzare l’incarico nei compensi e nelle minicarriere;

-  la riduzione dell’importo dell’incarico per un importo pari alla quota già utilizzato;

-  la storicizzazione dell’importo originario del contratto;

-  la possibilità di riutilizzare l’eventuale riferimento della ricerca di professionalità interna (sempre che sia ancora valida) per un altro incarico.

-  Eliminato : il passaggio di stato, consentito solo al Direttore, avviene cliccando sull’icona “Concludi Incarico”. Solo gli incarichi che non siano stati collegati ad un compenso possono assumere tale stato. Un incarico risulta non collegato ad un compenso quando “l’importo utilizzato” nel folder “importo per anno” è uguale a zero.
   Il passaggio di stato comporta:

   -  l’impossibilità di utilizzare l’incarico nei compensi e nelle minicarriere;

   -  la possibilità di riutilizzare l’eventuale riferimento della ricerca di professionalità interna (sempre che sia ancora valida) per un altro incarico.

Successione degli stati nell’ambito delle procedure amministrative
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

La funzione “Procedura di conferimento incarichi” è a supporto delle procedure amministrative per l’affidamento di incarichi. Ciascuna procedura prevede delle fasi diverse; di seguito sono riportati, in maniera schematica, i percorsi logici di ciascuna procedura amministrativa suddivisi per **Mono-Incarico** e **MultiIncarico**. Nel caso del **Mono-Incarico** gli Stati della Procedura di Conferimento Incarichi sono gli stessi di quelli dell’unico incarico ad essa associato.

Procedura Conferimento MONO-Incarico CON l’obbligo della verifica di professionalità interna e CON procedura comparativa
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^


.. list-table:: Procedura Conferimento MONO-Incarico SENZA l’obbligo della verifica di professionalità interna e CON procedura comparativa
   :widths: 20 20 30 30
   :header-rows: 1

   * - Stato
     - Soggetti abilitati
     - Controlli al Salvataggio
     - Note
   * - Provvisoria
     - Tutti
     - Verifica dei limiti (se necessario)
     - I campi son tutti modificabili
   * - Pubblicata
     - Direttore
     - Deve essere stato allega to almeno un file di tipo “Avviso da pubblicare”.
     - I campi non sono modificabili.
   * - Annullata
     - Direttore
     - ----------------------------
     - L’incarico non può più essere affidato, la pubblicazione viene eliminata. Il processo è concluso.
   * - In Scadenza
     - Automatico
     - Il passaggio di stato è automatico se sono passati 14 giorni dalla pubblicazion e dell'avviso sul sito del CNR.
     - Il folder Incarichi è visibile per l’inserimento dei contratti
   * - Scaduta
     - Automatico
     - Il passaggio di stato è automatico se sono passati 74 giorni dalla pubblicazione dell'avviso sul sito del CNR.
     - L’incarico non può più essere affidato. Il processo è concluso.
   * - Inviata Corte dei Conti
     - Direttore
     - Nel folder “Incarichi” deve essere impostato l’apposito flag, deve essere valorizzata la data di stipula ed il contraente; Deve essere stato allegato almeno un file di tipo “Contratto Stipulato”; Deve essere stato allegato almeno un file di tipo “Decisione a contrattare”.
     - Il Contratto necessità del controllo di legittimità della Corte dei Conti.
   * - Annullata
     - Direttore
     - Deve essere stato allegato almeno un file di tipo “Esito Contro llo Corte Conti” ;
     - L’esito della Corte dei Conti è “Illegittimo ”.
   * - Definitiva
     - Direttore
     - Il folder "Incarichi" deve essere compil ato in tutte le sue parti; Deve essere stato allegato almeno un file di tipo “Contratto Stipulato”; Deve essere stato allegato almeno un file di tipo “Decisione a contrattare”; Se è previsto l’invio alla Corte dei Conti, deve essere stato allegato almeno un file di tipo “Esito Controllo Corte Conti”.
     - L'incarico può essere utiliz zato all'interno delle minicarriere e nei compensi. L'unico campo modificabile è la "Proroga".
   * - Chiusa
     - Direttore
     - L'incarico viene chiuso quando l'importo risult a parzialmente utilizzato
     - I campi non sono modificabili. L'incarico non può più essere utilizzato nei compensi e nelle minicarriere.
   * - Eliminata
     - Direttore
     - L'incarico viene eliminato quando non è stato fatto alcun pagamento.
     - I campi non sono modificabili. L'incarico non può più essere utilizzato nei compensi e nelle minicarriere







Procedura Conferimento MONO-Incarico SENZA l’obbligo della verifica di professionalità interna e SENZA procedura comparativa
-----------------------------------------------------------------------------------------------------------------------------



Procedura Conferimento MONO-Incarico CON l’obbligo della verifica di professionalità interna e SENZA procedura comparativa
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~



Procedura Conferimento MULTI-Incarico CON l’obbligo della verifica di professionalità interna e CON procedura comparativa
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~



Procedura Conferimento MULTI-Incarico SENZA l’obbligo della verifica di professionalità interna e CON procedura comparativa
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~


Procedura Conferimento MULTI-Incarico SENZA l’obbligo della verifica di professionalità interna e SENZA procedura comparativa
-----------------------------------------------------------------------------------------------------------------------------

Non è possibile attivare procedure Multi-Incarico in presenza di Procedure amministrative che non prevedono la procedura comparativa.

Procedura Conferimento MULTI-Incarico CON l’obbligo della verifica di professionalità interna e SENZA procedura comparativa
---------------------------------------------------------------------------------------------------------------------------

Non è possibile attivare procedure Multi-Incarico in presenza di Procedure amministrative che non prevedono la procedura comparativa.

section.rst
