============================
Tab. Riferimento Progetti
============================

.. _uo-coordinatrice:

UO Coordinatrice
================

Indica la UO responsabile del progetto, che può rendere disponibile il progetto ad altre UO e può gestire e consultare le informazioni contabili del Progetto nella sua totalità. La UO Coordinatrice inserisce i dati dell'anagrafica del Progetto (compreso il Piano Economico se richiesto) e utilizza il Progetto in fase di Previsione.
Le UO partecipanti vedranno solo i dati del Progetto relativi alla loro movimentazione (effettuata in fase di gestione).

.. _responsabile-progetto:

Responsabile del Progetto
=========================

Il Responsabile indicato sul Progetto deve essere censito tra i Terzi di Sigla.

.. _tipo-finanziamento:

Tipo Finanziamento
==================

Il tipo finanziamento qualifica le anagrafiche dei progetti in categorie omogenee e ne determinano l'utilizzo in fase di gestione. I tipi sono gestiti in un'anagrafica specifica e vengono censiti indicando, per ognuno di essi, alcune regole comportamentali che guideranno la gestione dei Progetti stessi.
I tipi Finanziamento attualmente predefiniti sono:
-	FOE;
-	FOE progetti;
-	Autofinanziamento;
-	Autofinanziamento AREE;
-	Rimborsi da soggetti terzi;
-	Cofinanziamento;
-	Finanziamento;
-	Attività Commerciale pura;
-	Attività commerciale a tariffario.

I Parametri (impostabili a Si oppure a No) che possono essere gestiti per ogni tipologia di Finanziamento sono:

- Piano economico-finanziario - Obbligatoria la compilazione del Piano Economico
- Associazione categoria-voci del piano per il personale tempo indeterminato - E' possibile associare al progetto voci economiche di tipo personale T.I.	
- Associazione categoria-voci del piano per il personale tempo determinato - E' possibile associare al progetto voci economiche di tipo personale T.D.	
- Associazione categoria-voci del piano per altre spese del personale - E' possibile associare al progetto voci economiche di tipo personale	
- Previsione Entrata/Spesa consentita - Consentita la compilazione della previsione sia di entrata che di spesa
- Ripartizione costi del personale 	- Consentita la ripartizione delle matricole in fase di previsione
- Quadratura pdgp con quota annuale del piano economico - Non utilizzato. In previsione il controllo fisso è: importo di previsione deve essere minore o uguale dell'importo specificato per l'anno sul piano economico del progetto
- Controllo validità del Progetto - Non utilizzato	
- Piano delle rendicontazioni 	- Non utilizzato	
- Variazioni consentite 	- Non utilizzato	
- Incassi consentiti 	- Non utilizzato	
- Previsione totale quota finanziata - Richiede la quadratura tra l'importo totale indicato in previsione per le fonti decentrate esterne e l'importo finanziato indicato sul piano economico del progetto 
- Quadra Associazione Progetto/Contratti 	- Richiede la quadratura tra importo Finanziato del Progetto e la somma dei contratti attivi	
- Consenti Associazione Progetto/Contratti - Consente l'associazione dei contratti al progetto.

Di seguito si riporta un ripilogo dei Tipi Finanziamento e le loro regole operative.

**Riepilogo Tipologie di Finanziamento**

+-----------------------------------+---------+----------------+--------------------------------------+--------------------+
| Tipologie                         | Obbligo | Obbligo        | Consentita                           | Consentito Scarico |
| Finanziamento                     | Durata  | Piano Economico| Previsione                           | Costi del Personale|
+===================================+=========+================+======================================+====================+
| FOE                               | NO      | NO             |  SI - STATO APPROVATO                | SI                 |
+-----------------------------------+---------+----------------+--------------------------------------+--------------------+
| FOE PROGETTI                      | SI      | SI             |  NON CONSENTITA                      | SI                 |
+-----------------------------------+---------+----------------+--------------------------------------+--------------------+
| AUTOFINANZIAMENTO                 | SI      | SI             |  NON CONSENTITA                      | SI                 |
+-----------------------------------+---------+----------------+--------------------------------------+--------------------+
| AUTOFINNZIAMENTO AREE             | NO      | NO             |  SI - STATO APPROVATO                | SI                 |
+-----------------------------------+---------+----------------+--------------------------------------+--------------------+
| RIMBORSO DA SOGGETTI TERZI        | NO      | NO             |  SI - STATO APPROVATO                | SI                 |
+-----------------------------------+---------+----------------+--------------------------------------+--------------------+
| COFINANZIAMENTO                   | SI      | SI             |  SI - STATO APPROVATO E NEGOZIAZIONE | SI                 |
+-----------------------------------+---------+----------------+--------------------------------------+--------------------+
| FINANZIAMENTO                     | SI      | SI             |  SI - STATO APPROVATO E NEGOZIAZIONE | SI                 |
+-----------------------------------+---------+----------------+--------------------------------------+--------------------+
| ATTIVITA' COMMERCIALE PURA        | SI      | SI             |  SI - STATO APPROVATO                | SI                 |
+-----------------------------------+---------+----------------+--------------------------------------+--------------------+
| ATTIVITA' COMMERCIALE A TARIFFARIO| NO      | NO             |  NON CONSENTITA                      | SI                 |
+-----------------------------------+---------+----------------+--------------------------------------+--------------------+


.. _voce-del-piano-economico:

Voci del Piano Economico Progetti
=================================
Le Voci del Piano Economico rappresentano le categorie di spesa in cui si suddivide un progetto. Solitamente sono le stesse definite dal finanziatore e per e quali occorrerà produrre la rendicontazione. In Sigla sono inserite in un'apposita anagrafica configurabile dall'amministratore del sistema. A titolo di esempio possiamo citare le seguenti voci di spesa solitamente utilizzate:

- Spese di Trasferta
- Personale a Tempo Determinato
- Personale a Tempo Indeterminato
- Altro Personale
- Spese Generali
- Consulenze
- Investimenti
- Altro

Ci sono delle Voci economiche del Piano che automaticamente proporranno delle voci finanziarie per mezzo di un’associazione obbligatoria creata da chi amministra queste informazioni (ad esempio le voci del personale). Per queste casistiche le voci del piano economico  sono vincolate alle voci finanziare da usare e, viceversa, queste voci finanziarie non potranno essere usate per altre voci economiche. 
Per queste configurazioni le Voci Economiche sono caratterizzate da alcune impostazioni:

- Tipologa predefinita (Personale Tempo Indeterinato, Personale TempoDeterminato, Altro Personale ...)
- Associazione Automatica Voci finanziarie (S/N)
- Associazione Manuale voci finanziarie (S/N)
- Obbligo quadratura tra importo di previsione e quota finanziata
- Validità (S/N)


