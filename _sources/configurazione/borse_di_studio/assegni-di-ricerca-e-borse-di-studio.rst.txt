Assegni di ricerca e borse di studio
====================================

Al fine di adempiere a quanto previsto dal sopracitato art. 18, è sorta
la necessità di implementare due nuovi moduli, uno per gli assegni di
ricerca ed uno per le borse di studio.

Entrambi i moduli hanno un funzionamento uguale a quello del modulo
incarichi, anche se in una versione semplificata.

I due nuovi moduli sono posizionati nell’albero delle funzioni in
Configurazione come mostrato nella figura sottostante. Per accedere a
tali moduli sarà necessario assegnare alle utente il nuovi accessi

+-----------------------------------+-----------------------------------+
| **CD_ACCESSO**                    | **DS_ACCESSO**                    |
+===================================+===================================+
| CFGBORSESTUDIOPRCM                | Gestione Procedura Conferimento   |
|                                   | Borse di Studio                   |
+-----------------------------------+-----------------------------------+
| CFGASSEGNIRICERCPRCV              | Visualizzazione Procedura         |
|                                   | Conferimento Assegni di Ricerca   |
+-----------------------------------+-----------------------------------+
| CFGASSEGNIRICERCPRCM              | Gestione Procedura Conferimento   |
|                                   | Assegni di Ricerca                |
+-----------------------------------+-----------------------------------+
| CFGBORSESTUDIOPRCV                | Visualizzazione Procedura         |
|                                   | Conferimento Borse di Studio      |
+-----------------------------------+-----------------------------------+

Il modulo si compone di 5 tab : procedura, importo per anno, incarichi,
allegati, altri dati.

Il funzionamento del modulo, identico a quello degli incarichi, è stato
semplificato in alcune parti, in particolare nel tab “procedura” non
sono richieste tutte quelle informazioni considerate “fisse”, come ad
esempio il tipo di attività ed il tipo di incarico. Inoltre entrambe le
procedure possono assumere solo due stati “\ *provvisorio*\ ” e
“\ *definitivo*\ ”.

Agli assegni di ricerca e alle borse di studio devono OBBLIGATORIAMENTE
essere allegati due file, il

*contratto* sottoscritto e il *curriculum vitae* del vincitore.

I moduli implementati NON PREVEDONO la pubblicazione dei bandi, poiché
tale fase deve essere espletata con le modalità già note.
