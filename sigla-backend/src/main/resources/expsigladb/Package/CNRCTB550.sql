--------------------------------------------------------
--  DDL for Package CNRCTB550
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRCTB550" AS
-- =================================================================================================
--
-- CNRCTB550 - Calcolo e scrittura COMPENSI
--
-- Date: 12/02/2004
-- Version: 2.6
--
-- Dependency: CNRCTB 080/545/552/600 IBMERR 001 IBMUTL 001
--
-- History:
--
-- Date: 16/04/2002
-- Version: 1.0
--
-- Creazione Package.
--
-- Date: 12/06/2002
-- Version: 1.1
--
-- Fix su memorizzazione importi in testata compenso.
--
-- Date: 20/06/2002
-- Version: 1.2
--
-- Revisione complessiva del calcolo compensi, fix errori in modifica compenso
-- introdotti i controlli di consistenza sulle formule di calcolo.
-- Introdotta la gestione per il calcolo dei compensi per non dipendenti
--
-- Date: 27/06/2002
-- Version: 1.3
--
-- Inserita routine per aggiornamento dei montanti.
-- Inserita gestione della regione per IRAP
--
-- Date: 01/07/2002
-- Version: 1.4
--
-- Inserita interfaccia per aggiornamento dei montanti
--
-- Date: 04/07/2002
-- Version: 1.5
--
-- Adeguamento del package alle modifiche della tabella COMPENSO
-- Ammontari netto detrazioni + legame per contabilizzazione compenso negativo
-- Introdotta gestione annualizzazione + senza calcoli
--
-- Date: 15/07/2002
-- Version: 1.6
--
-- Introdotta memorizzazione montanti
-- Fix errore in calcolo compenso di tipo altro quando il trattamento o l'imponibile ammettono
-- l'accesso a pi� scaglioni
--
-- Date: 16/07/2002
-- Version: 1.7
--
-- Aggiunta alla tabella insiemeCoriTab il campo che memorizza la data di inizio validit�el cori
-- in elaborazione; soluzione errore in valorizzazione della FK CONTRIBUTO_RITENUTA01
--
-- Date: 18/07/2002
-- Version: 1.8
-- Aggiornamento della documentazione
--
-- Date: 22/07/2002
-- Version: 1.9
--
-- Aggiornate le routine per accedere alle costanti deifnite in CNRCTB545.
-- Fix errore per cui non era attivata la gestione per annualizzazione.
-- Modificata l'attivazione della gestione per aliquota in anagrafico per essere operativa sempe
-- indipendentemente dalla attivazione dell'annualizzazione.
-- Attivazione detrazioni familiari e personali
--
-- Date: 24/07/2002
-- Version: 1.10
--
-- Fix errori in calcolo detrazioni familiari e personali
-- Fix routine di calcolo dell'imponibile base cori per le nuove previsioni di classificazione in tipo
-- contributo ritenuta.
-- Aggiornamento della documentazione
--
-- Date: 25/07/2002
-- Version: 1.11
--
-- Inserita routine per la gestione della cancellazione dei compensi
--
-- Date: 25/07/2002
-- Version: 1.12
--
-- Fix errore in nettizza IRPEF con detrazioni
--
-- Date: 02/08/2002
-- Version: 1.14
--
-- Aggiornamento documentazione + fix calcolo detrazioni personali + routine per cancellazione compensi
--
-- Date: 05/08/2002
-- Version: 1.15
--
-- Sistemazione routine di calcolo detrazioni nette su compenso
--
-- Date: 07/08/2002
-- Version: 1.16
--
-- Inseriti ultriori controlli in sede di inserimento e modifica di un compenso e aggiornamento della
-- documentazione
--
-- Date: 23/08/2002
-- Version: 1.17
--
-- Modifica routine di calcolo detrazioni familiari per poter essere richiamate dal conguaglio
--
-- Date: 02/09/2002
-- Version: 1.18
--
-- Modificata la routine di recupero dello scaglione per errore in caso di configurazione sia ente
-- che percipiente.
--
-- Date: 02/09/2002
-- Version: 1.20
--
-- Corretto errore in algoritmo di calcolo della formula nel trattamento, �resa in considerazione
-- solo l'ultima formula.
--
-- Date: 02/09/2002
-- Version: 1.21
--
-- Aggiunto controllo di errore in formula se il riferimento ad id_riga nella formula di calcolo �-- maggiore dello stesso id_riga
--
-- Date: 03/09/2002
-- Version: 1.22
--
-- Aggiunto controllo di errore in formula anche se il riferimento ad id_riga nella formula di calcolo �-- uguale dello stesso id_riga
--
-- Date: 05/09/2002
-- Version: 1.23
--
-- Aggiunto controllo di non produrre errore se le formule di calcolo definite in TRATTAMENTO_CORI
-- puntano ad un importo ente o percipiente definito su di una riga che non valorizza lo stesso.
-- Introdotto il test di tornare 0 se il campo �ullo.
-- Aggiunto blocco per la cancellazione di compensi associati a conguaglio.
-- Aggiunta la segnalazione per i compensi che risultano essere associati ad un conguaglio
-- Reintrodotto il lock sui montanti in sede di calcolo
-- Introdotta la rettifica dei montanti in caso di eliminazione di un compenso e corretto calcolo
-- degli stessi in sede di inserimento.
--
-- Date: 13/09/2002
-- Version: 1.24
--
-- Corretto errore per cui i cori IVA erano definiti come percipiente e non ente
--
-- Date: 18/09/2002
-- Version: 1.25
--
-- Inserito controllo se devo eseguire l'aggiornamento dei montanti. (aggiornaMontanti)
-- Se COMPENSO.pg_ver_rec �aggiore di 1 e sono vuoti i parametri del clone non faccio nulla;
-- �tata chiamata la procedura di aggiornamento montanti da una modifica che non ha avuto alcuna
-- influenza sui dati di calcolo del compenso
--
-- Date: 18/09/2002
-- Version: 1.26
--
-- Fix errore per cui i cori IVA erano definiti come percipiente e non ente, non erano state
-- modificate su ente le variabili per la base calcolo e l'aliquota.
--
-- Date: 24/09/2002
-- Version: 1.27
--
-- Modifica della routine di inserimento di un compenso per la gestione dei nuovi campi in tabella
-- ti_istituz_commerc, imponibile_inail, esercizio_fattura_fornitore, dt_fattura_fornitore,
-- nr_fattura_fornitore, fl_generata_fattura.
-- Eliminato il controllo che il trattamento con fl_diaria = 'Y' non pu�sere usato in compensi normali.
-- Inserita la nuova gestione dell'imponibile INAIL. Se indicato in testata compenso e diverso da
-- zero copre il valore calcolato dalla routine getImponibileCori
--
-- Date: 26/09/2002
-- Version: 1.28
--
-- Revisione routine di cancellazione del compenso.
-- Corretta gestione di IVA e RIVALSA. Gli importi, carico ente, vanno sommati al netto percipiente.
-- Modificata matrice tabella_cori inserendo la mappatura di CONTRIBUTO_RITENUTA.montante per
-- memorizzare il valore di imponibile effettivamente utilizzato nella riga CORI.
-- Inserita la gestione della precisione indicata sul tipo contributo ritenuta. E' presa in considerazione
-- solo la precisione pari a 1.
-- Introdotto il controllo di non poter cancellare un compenso che risulta associato ad una spesa
-- del fondo economale
-- Modificato calcoloCoriAltro:
---- Se il compenso �alcolato da conguaglio allora non sommo l'imponibileCori al montante nella
--   determinazione dell'importo massimo di riferimento allo scaglione
---- La gestione annualizzata non si applica ai compensi da conguaglio
---- La gestione annualizzata ora opera per scaglioni e non pi� per aliquota massima
--
-- Date: 30/09/2002
-- Version: 1.29
--
-- Inserita gestione di creazione della fattura da compenso
-- Eliminato controllo di blocco, nella generazione di un compenso conguaglio, di un trattamento che
-- presenta il flag diaria a true
--
-- Date: 02/10/2002
-- Version: 1.30
--
-- Forzato azzeramento della variabile globale che contiene le anagrafiche e i compensi
--
-- Date: 15/10/2002
-- Version: 1.31
--
-- Fix errore per cui in conguaglio i cori devono, se del caso, essere sempre calcolati a scaglione
-- ovvero entro sempre con zero sugli scaglioni mentre l'importo massimo �ato dal montante o
-- dallo stesso imponibile cori in calcolo.
-- Introdotto il controllo di non aggiornare i montanti in caso di compenso da conguaglio.
-- Introdotto il controllo della cancellazione logica anche per i compensi che sono gi�tati
-- processati in coge e/o in coan
--
-- Date: 17/10/2002
-- Version: 1.32
--
-- Adeguamento modifiche alla struttura della tabella COMPENSO e FATTURA PASSIVA
--
-- Date: 07/11/2002
-- Version: 1.33
--
-- Fix errore 356. Il sistema non calcola correttamente il totale dovuto del Conguaglio Irpef.
--
-- Date: 12/11/2002
-- Version: 1.34
--
-- Fix errore interno 2851 mail cineca del 11/11/2002. Il sistema non calcola correttamente gli
-- scaglioni irpef successivi al primo.
--
-- Date: 18/11/2002
-- Version: 1.35
--
-- Adeguamento package alla struttura modificata della tabella COMPENSO. Aggiunti attributi
-- fl_compenso_minicarriera e aliquota_irpef_da_missione.
--
-- Date: 12/12/2002
-- Version: 1.36
--
-- Inserita richiesta CNR per modifica del calcolo dell'importo annualizzato in presenza di compensi
-- da minicarriera
--
-- Date: 08/01/2003
-- Version: 1.37
--
-- Implementato il calcolo con aliquota media per IRPEF in compensi da minicarriera a tassazione separata
--
-- Date: 09/01/2003
-- Version: 1.38
--
-- Definita la memorizzazione dell'aliquota media IRPEF per gestione a tassazione separata come valore
-- non diviso per 100
-- Inserito il controllo per non aggiornare i montanti per i compensi a tassazione separata
--
-- Date: 17/01/2003
-- Version: 1.39
--
-- Modificata matrice insiemeCoriRec inserendo la mappatura di CONTRIBUTO_RITENUTA.imponibile_lordo e
-- CONTRIBUTO_RITENUTA.im_deduzione_irpef per adeguamento a finanziaria 2003
--
-- Date: 22/01/2003
-- Version: 1.40
--
-- Modifiche per gestione finanziaria 2003
-- Modifica alla matrice montantiRec per adeguamento alle modifiche della tabella MONTANTI, gestione
-- del montante lordo e netto per IRPEF.
-- Modifiche  -->  costruisciTabMontanti = Recupero dell'importo montante lordo
--                 modificaTabMontanti = Recupero dell'importo montante lordo
--
-- Date: 27/01/2003
-- Version: 1.41
--
-- Modifiche per gestione finanziaria 2003
-- Inserito nuovo calcolo IRPEF per finanziaria 2003.
-- -- Gestione NO TAX AREA
-- -- Le deduzioni sono elaborate solo in caso di compenso da conguaglio o da minicarriera e solo
--    per soggetti altri
-- -- Modificati i parametri di ingresso alla routine di elaboraCompenso per includere anche
--    l'identificativo della minicarriera, se del caso, da cui deriva il compenso (gestione di compenso
--    generato contestualmente alla minicarriera che presena quindi una chiave negativa).
--
-- Date: 27/01/2003
-- Version: 1.42
--
-- Spento il calcolo delle detrazioni in caso di compenso da conguaglio, il calcolo �atto dalla
-- stessa procedura del conguaglio.
-- Modificata la routine getImRateDiTerzoInEsercizio per operare sul codice anagrafico e non sul terzo.
--
-- Date: 28/01/2003
-- Version: 1.43
--
-- Inserito filtro sul controllo dell'ammontare goduto delle deduzioni per non essere applicato in
-- caso di compenso da conguaglio
-- Inserito il filtro sul controllo dell'ammontare goduto delle deduzioni per non essere applicato in
-- caso di compenso da conguaglio
--
-- Date: 29/01/2003
-- Version: 1.44
--
-- Invertito il dominio dell'attributo fl_notaxarea in ANAGRAFICO_ESERCIZIO
--
-- Date: 30/01/2003
-- Version: 1.45
--
-- Invertito il dominio dell'attributo fl_notaxarea in ANAGRAFICO_ESERCIZIO (adeguamento al client)
--
-- Date: 30/01/2003
-- Version: 1.46
--
-- Revisione finanziaria 2003, le detrazioni familiari e personali sono calcolate a partire dall'imponibile
-- fiscale lordo e non quello netto
--
-- Date: 17/02/2003
-- Version: 1.47
--
-- Implementata richiesta CINECA 483. Nel calcolo della deduzione il sistema ora considera 3000 fisse e
-- 4500 in base ai giorni, si chiede invece che il sistema consideri tutto variabile in base ai giorni
-- cio�500.
-- FIX errore interno 3261. Se nel calcolo delle detrazioni familiari un intervallo era superiore ai
-- 12 mesi il sistema andava in errore nella normalizzazione del periodo ORA-01403: no data found
--
-- Date: 19/02/2003
-- Version: 1.48
--
-- Fix errore su calcolo IRPEF in compenso da missione con lordizzazione, non usava l'aliquota calcolata
-- dalla missione stessa. Modificate routine getRilevaAnnualizzato (introdotto inAnnualizzato = 4) e
-- calcolaCoriAltro.
--
-- Date: 05/03/2003
-- Version: 1.49
--
-- FIX Errore CINECA n. 517. Revisione calcolo delle detrazioni su compensi a tassazione separata,
-- si usano gli importi dell'esercizio precedente a quello della minicarriera
--
-- Date: 24/03/2003
-- Version: 1.50
--
-- Richiesta CINECA n.541. Revisione del calcolo dell'aliquota fiscale media in pagamento compensi da
-- minicarriera. Modifica metodo getImRateDiTerzoInEsercizio, non si esporta pi� semplicemente il valore
-- totale delle rate di minicarriera con scadenza nell'esercizio per un dato soggetto anagrafico ma
-- a rottura di ogni trattamento, si procede al calcolo del vero imponibile fiscale.
--
-- Date: 25/03/2003
-- Version: 1.51
--
-- Richiesta CINECA n.546 e 545. Revisione del calcolo della deduzione irpef (calcolo per la sola quota
-- variabile 4.500 o nessun peso a giorni di competenza per la quota fissa (3.000).
-- Modificato calcolo coefficente trunc invece di round
--
-- Date: 27/03/2003
-- Version: 1.52
--
-- Richiesta CINECA n.542. Revisione del calcolo della deduzione IRPEF per minicarriere. Inserita routine
-- di calcolo del numero complessivo di giorni per le rate di minicarriera in calcolo
--
-- Date: 31/03/2003
-- Version: 1.53
--
-- Richiesta CINECA n.540. In caso di conguaglio con irpef = 0 non deve procedersi al calcolo delle
-- addizionali territorio
--
-- Date: 31/03/2003
-- Version: 1.54
--
-- Richiesta CINECA n.556. Estensione gestione tassazione separata anche in calcolo compenso per
-- dipendente
--
-- Date: 10/04/2003
-- Version: 1.55
--
-- Richiesta CINECA. Calcolo della aliquota media in minicarriera annualizzata tenendo conto della
-- deduzione lorda e non di quella normalizzata al periodo del compenso (rata) in calcolo
--
-- Date: 11/04/2003
-- Version: 1.56
--
-- Inserito controllo per non inserire deduzioni negative
--
-- Date: 23/06/2003
-- Version: 1.57
--
-- Fix errore CINECA n. 612. Mancato aggiornamento del importo lordo in compenso senza calcoli
--
-- Date: 21/07/2003
-- Version: 1.58
--
-- Implementata richiesta CINECA n. 619. Modifica giorni nel calcolo della deduzione da applicare al
-- compenso da minicarriera.
-- La Deduzione reale da applicare al compenso da minicarriera che attualmente �
--      Dr = (Dt * Coeff * (periodo competenza del compenso))/(periodo competenza della minicarriera in esame)
-- deve diventare:
--      Dr = (Dt * Coeff * (periodo competenza del compenso))/(n� complessivo dei giorni delle rate
--                                                             nell'anno delle minicarriere con
--                                                             trattamento che hanno FL_SOGGETTO_CONGUAGIO = Y
--                                                             a quel percipiente togliendo le intersezioni e
--                                                             controllando che il valore totale non superi 365)
--
-- Date: 28/10/2003
-- Version: 2.0
--
-- Rilascio richiesta CINECA n. 655. Gestione conguaglio per aliquota massima.
--
-- Date: 09/12/2003
-- Version: 2.1
--
-- Rilascio richiesta CINECA n. 471. Gestione rateizzazione addizionali territorio (seconda fase =
-- addebito della rata in esercizio successivo).
--
-- Date: 10/12/2003
-- Version: 2.2
--
-- Rilascio richiesta CINECA n. 692. Inserimento controlli di ammissibilit�el compenso.
--
-- Date: 10/12/2003
-- Version: 2.3
--
-- Fix errore interno in aggiornamento importo rateizzato da calcolo compensi
--
-- Date: 16/01/2004
-- Version: 2.4
--
-- Fix errore CINECA n. 730. il recupero delle rate per le addizionali accantonate deve essere possibile
-- solo per compensi da minicarriera, ora recupera le rate anche nei compensi da missione o secchi.
-- Introdotto anche il controllo di escludere i compensi da minicarriera con gestione a tassazione separata
--
-- Date: 29/01/2004
-- Version: 2.5
--
-- Fix errore CINECA n. 727. Modifica controlli di ammissibilit�el compenso se il totale compenso �-- maggiore di zero. Si accettano solo i casi in cui il netto calcolato del mandato principale risulta
-- maggiore o uguale a zero o, se minore di zero, allora l'importo totale del compenso meno i carichi
-- ente positivi deve essere maggiore o uguale a zero.
--
-- Date: 12/02/2004
-- Version: 2.6
--
-- Migliorata gestione per controllo esistenza del terzo in area compensi (validazione per cessazione e rapporto)
-- CNRCTB080.chkEsisteTerzoPerCompenso. Errore CINECA n. 665
--
-- Date: 22/02/2005
-- Version: 2.7
--
-- Modificata valorizzazione progressivo riga in CONTRIBUTO_RITENUTA_DET: Se ho un tributo che prevede sia
-- una parte a carico Percipiente e sia una a carico Ente,  le due righe inserite in CONTRIBUTO_RITENUTA_DET
-- (inserite solo se c'e' un cambio di scaglione) avranno lo stesso progressivo riga
--
-- Date: 01/08/2006
-- Version: 2.8
--
-- Aggiunta la nuova "Gestione dei Cervelli"
--
-- Date: 31/01/2007
-- Version: 2.9
--
-- Adeguamenti Finanziaria 2007. Introdotta la gestione delle detrazioni
--
-- Date: 22/02/2007
-- Version: 2.10
--
-- Addizionali Regionali: Modificata la chiamata allo scaglione nel caso in cui la regione
-- prevede la gestione dell'aliquota massima per l'intero importo
--
-- Date: 15/05/2007
-- Version: 2.11
--
-- Adeguata la gestione dei Conguagli alla Finanziaria 2007
-- Gestiti gli Acconti per le addizionali Comunali
-- Gestite le esenzioni per le addizionali comunali
--
-- Date: 28/05/2008
-- Version: 2.12
--
-- Tassazione separata: Modificato calcolo importo accesso al calcolo delle detrazioni; in questo caso
-- deve essere preso solo l'importo corrente
--
-- Date: 08/05/2009
-- Version: 2.12
--
-- Gestita la sospensione IRPEF per i terremotati che ne fanno richiesta:
--   Se il l'imposta netta �ositiva e il pagamento del compenso viene fatto nel periodo in cui
--   la sospensione �ncora attiva, la ritenuta viene sospesa (IRPEF NETTA=0) e lo stesso importo viene
--   conservato nel campo im_cori_sospeso della stessa riga di contributo_ritenuta.
--
-- Date: 14/06/2011
-- Version: 2.13
--
-- Gestita la RIDUZIONE ERARIALE per i dipendenti: SOLO per essa vengono gestiti gli scaglioni e quindi
-- aggiornati i montanti.
--
-- Date: 11/01/2013
-- Version: 2.20 - Gestione montante INPGI
--
-- Date: 20/01/2014
-- Version: 2.21 - Nuove detrazioni a partire dal 01/01/2014
--
-- Date: 25/05/2014
-- Version: 2.22
-- Adeguamenti relativi al Bonus DL 66/2014
-- =================================================================================================
--
-- Constants
--

--
-- Variabili globali
--

   dataOdierna DATE;

   -- Memorizzazione elementi per il calcolo delle detrazioni familiari e personali

   calcolaDetrazFamiliari CHAR(1);
   calcolaDetrazPersonali CHAR(1);
   calcolaDetrazAltriTipi CHAR(1);
   aImDetrazioniPe NUMBER(15,2);
   aImDetrazioniLa NUMBER(15,2);
   aImDetrazioniCo NUMBER(15,2);
   aImDetrazioniFi NUMBER(15,2);
   aImDetrazioniFiS NUMBER(15,2);
   aImDetrazioniAl NUMBER(15,2);
   aImDetrazioniRidCuneo NUMBER(15,2);
   aImDetrazioniPeNetto NUMBER(15,2);
   aImDetrazioniLaNetto NUMBER(15,2);
   aImDetrazioniCoNetto NUMBER(15,2);
   aImDetrazioniFiNetto NUMBER(15,2);
   aImDetrazioniAlNetto NUMBER(15,2);
   aImDetrazioniRidCuneoNetto NUMBER(15,2);

   -- Memorizza la base di calcolo delle deduzioni

   glbBaseDeduzione NUMBER(15,2);
   glbQuotaFissaDeduzione NUMBER(15,2);
   glbQuotaVariabileDeduzione NUMBER(15,2);

   -- Memorizza la base di calcolo delle deduzioni Family

   glbBaseDeduzioneFamily NUMBER(15,2);
   glbDFamilyConiuge NUMBER(15,2);
   glbDFamilyFiglio NUMBER(15,2);
   glbDFamilyAltro NUMBER(15,2);
   glbDFamilyFiglioMenoTre NUMBER(15,2);
   glbDFamilyFiglioSenzaConiuge NUMBER(15,2);
   glbDFamilyFiglioHandicap NUMBER(15,2);
   -- Memorizza gestione no tax area per il soggetto anagrafico

   glbFlNoTaxArea CHAR(1);

   -- Memorizza gestione no family area per il soggetto anagrafico

   glbFlNoFamilyArea CHAR(1);

   -- Memorizza gestione no detrazioni altre per il soggetto anagrafico

   glbFlNoDetrazioniAltre CHAR(1);

   -- Memorizza gestione no detrazioni family per il soggetto anagrafico

   glbFlNoDetrazioniFamily CHAR(1);

   -- Memorizza gestione detrazioni altri tipi per il soggetto anagrafico

   glbFlDetrazioniAltriTipi CHAR(1);

   -- Memorizza gestione no credito IRPEF per il soggetto anagrafico

   glbFlNoCreditoIrpef CHAR(1);
   glbFlNoCreditoCuneoIrpef CHAR(1);
   glbFlNoDetrCuneoIrpef CHAR(1);

   -- Memorizza il Reddito Complessivo e quello della prima casa presenti in anagrafica

   glbRedditoComplessivo       NUMBER(15,2);
   glbRedditoAbitazPrincipale  NUMBER(15,2);

   -- Memorizza il Totale del Reddito Complessivo, sia del CNR (anche DIP) che presente in anagrafica, escluso Reddito Abitazione principale
   aTotRedditoComplessivo   NUMBER(15,2);


   glbImponibilePagatoDip    NUMBER(15,2);

   glbImponibilePagatoAltro  NUMBER(15,2);


   aOrigineCompenso INTEGER;

   -- Memorizza la testata del compenso in elaborazione ed eventualmente quella originale prima di una
   -- sessione di modifica

   aRecCompenso COMPENSO%ROWTYPE;
   aRecCompensoOri COMPENSO%ROWTYPE;

   -- Memorizza l'anagrafico di riferimento per il terzo beneficiario del compenso ed eventualmente
   -- quello originale prima di una sessione di modifica

   aRecAnagrafico ANAGRAFICO%ROWTYPE;
   aRecAnagraficoOri ANAGRAFICO%ROWTYPE;

   -- Memorizza il tipo trattamento per il compenso in elaborazione

   aRecTipoTrattamento TIPO_TRATTAMENTO%ROWTYPE;

   -- Memorizza i record relativi alle addizionali territorio rateizzate da recuperare

   aRecRateizzaClassificCoriC0 RATEIZZA_CLASSIFIC_CORI%ROWTYPE;
   aRecRateizzaClassificCoriP0 RATEIZZA_CLASSIFIC_CORI%ROWTYPE;
   aRecRateizzaClassificCoriR0 RATEIZZA_CLASSIFIC_CORI%ROWTYPE;

   -- Memorizza il record relativo all'acconto addizionale comunale da trattenere

   aRecAccontoClassificCoriC0 ACCONTO_CLASSIFIC_CORI%ROWTYPE;

   -- Definizione tabella PL/SQL di appoggio per calcolato contributi e ritenute

   tabella_cori CNRCTB545.insiemeCoriTab;
   tabella_cori_det CNRCTB545.insiemeCoriTab;
   tabella_montanti CNRCTB545.montantiTab;

   -- Dichiarazione di un cursore generico

   TYPE GenericCurTyp IS REF CURSOR;

--
-- Functions e Procedures
--

----------------------------------------------------------------------------------------------------
-- MAIN PROCEDURE
----------------------------------------------------------------------------------------------------

--
-- Copia compenso
--
-- Esecuzione della copia del compenso in modifica. La procedura �hiamata all'inizio di una sessione
-- di modifica di un compenso e memorizza, nei riferimenti 'copia', l'immagine iniziale del compenso stesso.
-- Copia del compenso in caso di modifica. Si memorizza la catena dei dati di un compenso prima che questo sia modificato.
--
-- pre-post-name: Copia del compenso in caso di modifica
-- pre:           Viene richiesto di effettuare una copia della versione attuale di un compenso prima di modificarla.
--                Il parametro inCopiaPgCompenso non �ullo
--                Non esiste gi�a copia del compenso con codice inCopiaPgCompenso
-- post:
--       Viene copiato il compenso con codice inPgCompenso con i dati CORI e dettaglio CORI
--
-- Parametri:
-- inCdsCompenso -> Cds del compenso
-- inUoCompenso -> Codice UO del compenso
-- inEsercizioCompenso -> Esercizio del compenso
-- inPgCompenso -> Progressivo del compenso
-- inCopiaCdsCompenso -> Cds del compenso "copia"
-- inCopiaUoCompenso -> Codice UO del compenso "copia"
-- inCopiaEsercizioCompenso -> Esercizio del compenso "copia"
-- inCopiaPgCompenso -> Progressivo del compenso "copia"

   PROCEDURE copiaCompenso
      (
       inCdsCompenso COMPENSO.cd_cds%TYPE,
       inUoCompenso COMPENSO.cd_unita_organizzativa%TYPE,
       inEsercizioCompenso COMPENSO.esercizio%TYPE,
       inPgCompenso COMPENSO.pg_compenso%TYPE,
       inCopiaCdsCompenso COMPENSO.cd_cds%TYPE,
       inCopiaUoCompenso COMPENSO.cd_unita_organizzativa%TYPE,
       inCopiaEsercizioCompenso COMPENSO.esercizio%TYPE,
       inCopiaPgCompenso COMPENSO.pg_compenso%TYPE
      );

--
-- Elabora compenso
--
-- Elaborazione del calcolo di un compenso
--
-- pre-post-name: Il trattamento indicato non �ompatibile con l'origine (da conguaglio, da missione, da minicarriera) del
--                compenso.
-- pre:           Compenso da conguaglio:
--                     Il trattamento deve essere abilitato alla gestione (indicatore fl_default_conguaglio = Y)
--                     Il trattamento non deve essere attivo per la gestione senza calcoli e compensi da missione
--                     (fl_senza_calcoli e fl_diaria = 'N')
--                Compenso da missione:
--                     Il trattamento deve essere abilitato alla gestione (indicatore fl_diaria = Y)
--                     Il trattamento non deve essere attivo per la gestione senza calcoli (indicatore fl_senza_calcoli = 'N')
--                Compenso da minicarriera:
--                     Il trattamento non deve essere attivo per la gestione senza calcoli (indicatore fl_senza_calcoli = 'N')
--                Compenso senza calcoli:
--                     Il trattamento deve essere abilitato alla gestione (indicatore fl_senza_calcoli = Y)
-- post:          Viene sollevata un'eccezione
--
-- pre-post-name: Impossibilit�lla attivazione della modifica.
-- pre:           Non �ossibile l'attivazione della modifica su di un compenso da conguaglio, da minicarriera e,
--                in ogni caso se il compenso risulta pagato.
-- post:          Viene sollevata un'eccezione
--
-- pre-post-name: Nessun CORI trovato da elaborare per il soggetto anagrafico in processo
-- pre:           Non esistono dettagli di CORI validi per il trattamento selezionato
-- post:          Viene sollevata un'eccezione
--
-- pre-post-name: Errore in sequenza dell'algoritmo di calcolo definito in TRATTAMENTO_CORI
-- pre:           Il numero di sequenza (id_riga) presente in tabella CORI calcolata non corrisponde all'indice di riga
--                della tabella stessa
-- post:          Viene sollevata un'eccezione
--
-- pre-post-name: Errata stringa di controllo dell'algoritmo di calcolo CORI - sintassi
-- pre:           La lunghezza della stringa di controllo del calcolo CORI non �ivisibile per 5 o non formattata secondo
--                specifica. La specifica della stringa di controllo del calcolo �a seguente:
--                     segno + id_riga + tutto(*)/solo carico ente(E)/solo carico percipiente(P) (5 caratteri in tutto)
--                     ES.             -001* -001E-002P-003*
-- post:          Viene sollevata un'eccezione
--
-- pre-post-name: Errata stringa di controllo dell'algoritmo di calcolo CORI - indice riga
-- pre:           L'indice di riga specificato in un blocco di controllo della stringa di specifica dell'algoritmo di
--                calcolo CORI �aggiore del numero di righe presenti nella tabella CORI del compenso in processo
-- post:          Viene sollevata un'eccezione
--
-- pre-post-name: Record scaglione per detrazioni familiari non definito
-- pre:           Non �tato trovato alcun record che in base al tipo di familiare, all'importo di riferimento ed alla
--                data di registrazione ritorni l'ammontare della detrazione stessa
-- post:          Viene sollevata un'eccezione
--
-- pre-post-name: Record scaglione per detrazioni personali non definito
-- pre:           Non �tato trovato alcun record che in base all'importo di riferimento ed alla data di registrazione
--                ritorni l'ammontare della detrazione stessa
-- post:          Viene sollevata un'eccezione
--
-- pre-post-name: Elaborazione di un compenso
-- pre:           Viene richiesta l'elaborazione di un compenso. Nessun'altra precondizione verificata
-- post:
--       Vengono letti i dati di base del compenso
--       Se in modifica
--            Sono letti i dati della versione precedente del compenso
--       Recupero dei dati dell'anagrafico associato al terzo del compenso
--       Se in modifica
--            Sono letti i dati dell'anagrafico associato al terzo della versione precedente del compenso.
--       Vengono letti i montanti del soggetto anagrafico ed eventualmente decurtati dell'eventuale compenso ante
--       elaborazione.
--       Vengono recuperati i dati relativi al tipo di trattamento
--       Sono attivati i controlli di congruenza tra compenso e tipo trattamento e la verifica all'ammissibilit�ella
--       modifica (vedi pre-post: Il trattamento indicato non �ompatibile con l'origine del compenso e Impossibilit�--       alla attivazione della modifica).
--       E' attivato il calcolo delle detrazioni personali e familiari.
--            Compenso senza calcoli
--                 Si memorizzano i dati delle detrazioni eventualmente valorizzati dall'utente spegnendo la routine di
--                 calcolo delle detrazioni (calcolaDetrazFamiliari:='N' e calcolaDetrazPersonali:='N').
--            Altri compensi
--                 Si azzerano le variabili destinate ad accogliere il calcolato delle detrazioni e si attiva la routine di
--                 calcolo delle stesse se il trattamento definito per il compenso prevede tale calcolo.
--            Per i compensi a dipendenti non �ai attivato il calcolo delle detrazioni personali e familiari
--       Viene costruito il prospetto CORI con il seguente criterio:
--            Vengono estratti da TIPO_TRATTAMENTO, TRATTAMENTO_CORI, TIPO_CONTRIBUTO_RITENUTA, secondo il seguente criterio:
--                 Codice del trattamento = Codice del trattamento soggetto anagrafico in processo
--                 Data di inizio validit�el trattamento = Data di inizio validit�el trattamento soggetto anagrafico in processo
--                 Data di fine validit�el trattamento = Data di fine validit�el trattamento soggetto anagrafico in processo
--                 Data di inizio validit�el trattamento CORI <= Data di registrazione del compenso
--                 Data di fine validit�el trattamento CORI >= Data di registrazione del compenso
--                 Data di inizio validit�el tipo CORI <= Data di registrazione del compenso
--                 Data di fine validit�el tipo CORI >= Data di registrazione del compenso
--            i seguenti dati:
--                 Codice CORI;
--                 Cassa o Competenza;
--                 Precisione
--                 Progressivo di classificazione del montante (1,2,4,5)
--                 Codice di classificazione CORI
--                 Flag aggiorna montanti ('Y', 'N')
--                 Progressivo di riga CORI
--                 Segno del CORI
--                 Stringa di controllo dell'algoritmo di calcolo del CORI (vedi pre-post: Errata stringa di controllo dell'algoritmo di calcolo CORI)
--
--       Se il tipo di soggetto anagrafico �IPENDENTE
--
--       Per ogni riga presente nella tabella dei CORI del compenso in processo
--            Vengono estratti i dati di territorio per l'accesso agli scaglioni:
--                 In generale sono utilizzati parametri di regione, provincia e comune non definiti (rispettivamente: '*', '*',0) tranne nei seguenti casi:
--                      Se il tipo contributo ritenuta �RAP �tilizzata la regione IRAP presente nel compenso valorizzata dall'utente.
--                      Se il tipo contributo ritenuta �ddizionale regionale viene utilizzata la regione dell'addizionale presente nel compenso
--                      Se il tipo contributo ritenuta �ddizionale provinciale viene utilizzata la provincia dell'addizionale presente nel compenso
--                      Se il tipo contributo ritenuta �ddizionale comunale viene utilizzato il comune dell'addizionale presente nel compenso
--            Viene estratto l'imponibile base CORI secondo la seguente regola:
--                 Se il tipo di CORI �iscale o per addizionale comunale, provinciale, regionale
--                      importo base = importo lordo percipiente - quota esente - importo non fiscale (quota esente IRPEF)
--                 Altrimenti
--                      importo base = importo lordo percipiente - quota esente
--            Viene calcolato l'imponibile reale del contributo ritenuta (da ora definito come IMP_CORI) in base alle regole di calcolo presenti nel trattamento:
--                 Imponibile reale = imponibile base se caso la stringa di controllo dell'algoritmo di calcolo CORI � '000'
--                 Altrimenti applico la formula
--            Viene determinato il montante di riferimento ove applicabile. Per i dipendenti si usano sempre i valori presente nei montanti
--            che sono statici e riempiti da migrazione stipendi.
--            Viene recuperato lo scaglione di reddito del soggetto anagrafico secondo le seguenti regole:
--                 Utilizzando il montante del dipendente come importo di riferimento viene estratto lo scaglione di
--                 reddito corrispondente al CORI in processo (Ente o Percipiente o Entrambi).
--            Viene aggiornata la matrice di calcolo dei CORIcome segue:
--                 Se il compenso �enza calcoli:
--                      Non si esegue alcun calcolo in quanto gli ammontari sono impostati dall'utente nell'interfaccia video.
--                 Se il CORI �i tipo 'P' (Percipiente) o '*' (Entrambi):
--                      Aliquota percipiente = Aliquota Scaglione
--                      Base Calcolo percipiente = Base Calcolo Scaglione
--                      Ammontare percipiente lordo = (IMP_CORI * (base calcolo / 100) * Aliquota scaglione) / 100
--                      Ammontare percipiente = (IMP_CORI * (base calcolo / 100) * Aliquota scaglione) / 100
--                 Se il CORI �i tipo 'E' (Ente) o '*' (Entrambi):
--                      Aliquota ente = Aliquota Scaglione
--                      Base Calcolo ente = Base Calcolo Scaglione
--                      Ammontare ente lordo = (IMP_CORI * (base calcolo / 100) * Aliquota scaglione) / 100
--                      Ammontare ente = (IMP_CORI * (base calcolo / 100) * Aliquota scaglione) / 100
--
--
--       Se il tipo di soggetto anagrafico �LTRO
--
--       Per ogni riga presente nella tabella dei CORI del compenso in processo
--            Verifica se per il codice contributo ritenuta in elaborazione rilevano modalit�i calcolo per eccezione
--            rispetto alla normale gestione. Tali modalit�anno riferimento alla gestione per aliquota massima
--            (valorizzazione in anagrafico) e all'annualizzazione del calcolo e si applicano solo a IRPEF a scaglioni.
--            Sono previste le seguenti gestioni:
--                 Se il compenso �enza calcoli
--                      Ignora gestione per aliquota in anagrafico o annualizzazione in quanto non �efinito lo
--                      stesso calcolo dell'ammontare contributo/ritenuta. La regola imposta annualizzato = 'N'.
--                 Se il codice contributo ritenuta identifica IRPEF a scaglioni (codice di classificazione CORI = 'FI',
--                      PG_CLASSIFICAZIONE_MONTANTI = 1 e FL_SCRIVI_MONTANTI = 'Y' (indica che devo aggiornare il montante e
--                      non solo usarlo)
--                      Se il tipo di trattamento ha FL_IRPEF_ANNUALIZZATA = 'Y'
--                           Si parte dall'imponibile IRPEF, lo si divide per il numero di giorni di competenza del
--                           compenso (anno commerciale) e si moltiplica per 360; si determina l'imponibile annualizzato.
--                           Su questo importo si applicano gli scaglioni IRPEF. Si entra con importo 0 fino alla
--                           concorrenza dell'importo annualizzato.
--                           Si sommano i valori IRPEF ottenuti dall'applicazione delle diverse aliquote nei diversi
--                           scaglioni. Tale valore �oltiplicato per cento e diviso per l'imponibile annualizzato
--                           ottenendo cos�'aliquota irpef media da utilizzare nel calcolo.
--                           La regola imposta annualizzato = '2'.
--                      Se l'aliquota fiscale del soggetto anagrafico �efinita e diversa da 0
--                           Prevale l'aliquota in anagrafico per l'accesso agli scaglioni rispetto al montante e il
--                           calcolo �seguito solo per quella stessa aliquota. La regola imposta annualizzato = '1'.
--                      Altrimenti non �nnualizzato ('N'). La regola imposta annualizzato = 'N'.
--            Vengono estratti i dati di territorio (Vedi parte corrispondente a soggetto anagrafico di tipo DIPENDENTE)
--            Viene estratto l'imponibile base (Vedi parte corrispondente a soggetto anagrafico di tipo DIPENDENTE)
--            Viene determinato il montante di riferimento. Per i soggetti anagrafici di tipo ALTRO il montante �ecuperato
--            da tabella montanti se il Tipo CORI in processo ha definito la classificazione montante, altrimenti il montante
--            �
--            Viene determinato il valore massimo complessivo di riferimento per l'ingresso allo scaglione (IM_MAX_RIF_SCA)
--            come somma del montante di riferimento + l'imponibile reale CORI.
--            Viene determinato il valore di ingresso per la ricerca dello scaglione nel seguente modo:
--                 Se il compenso �enza calcoli
--                      importo di accesso a scaglione �; non �ilevante in quanto non �a eseguirsi alcun calcolo.
--                 Se il trattamento �nnualizzato di tipo 1
--                      importo associato a scaglione �, rileva per il riconoscimento dello scaglione l'aliquota IRPEF
--                      specificata nel soggetto anagrafico. Se, entrando con data registrazione, non si trova uno scaglione
--                      definito con l'aliquota anagrafico si opera con lo scaglione minimo.
--                 Se il trattamento �nnualizzato di tipo 1, l'importo di accesso a scaglione �--                      Arrotondamento alla seconda cifra decimale(Imponibile CORI/(Delta in gg periodo di competenza economica del CORI + 1)*365 o 366.
--                 Altrimenti l'importo associato a scaglione = Montante CORI
--            Se si incontra un CORI di tipo IRPEF a scaglioni si calcolano le detrazioni familiari e personali
--                 Se non �ttivato il calcolo si esce dalla procedura (vedi sopra attivazione del calcolo delle detrazioni personali e familiari.
--                 Detrazioni familiari
--                      Si verifica che esistano carichi familiari definiti per l'anagrafico associato al terzo del compenso
--                      Si normalizza il periodo di competenza economica del compenso portando data inizio e fine rispettivamente
--                      all'inizio e fine del mese (le detrazioni sono mensili).
--                      Viene costruita la matrice dei carichi familiari (intervallo date) presenti nel periodo di competenza economica del
--                      compenso. Si uniscono i record che si riferiscono ad uno stesso soggetto. E' utilizzato il riferimento del
--                      codice fiscale per identificare i record che si riferiscono ad uno stesso soggetto; se nullo ogni record �--                      considerato un soggetto diverso.
--                      Si costruisce la matrice dei numeri persone per periodo con limite ai 12 mesi.
--                      Si calcolano le detrazioni incrociando ogni soggetto per periodo di validit�on gli stessi periodi
--                      che rappresentano il numero dei soggetti in carico. Le detrazioni per i figli sono differenziate
--                      per numero degli stessi.
--                      Si calcolano i casi particolari (primo figlio, portatore di handicap, ecc).
--                 Detrazioni personali.
--                      Nel calcolo si comprime il periodo di competenza economica del compenso a 365 0 366 giorni.
--                      Anche per i compensi a sogetti non dipendenti si entra nella tabella DETRAZIONI_LAVORO con
--                      ti_lavoro = D
--            Viene recuperato lo scaglione di reddito del soggetto anagrafico di tipo ALTRO utilizzando l'importo
--            calcolato di accesso a scaglione e l'aliquota IRPEF specificata per il soggetto anagrafico
--            (oltre che i dati di territorio e la data di registrazione del compenso). La gestione per scaglioni non
--            opera per i cori IVA e INAIL dove le aliquote sono recuperate da altri contesti.
--            Viene aggiornata la matrice di calcolo dei CORIcome segue:
--                 Se il compenso �enza calcoli:
--                      Non si esegue alcun calcolo in quanto gli ammontari sono impostati dall'utente nell'interfaccia video.
--                 Se il compenso �i tipo annualizzato di tipo diverso da 'N' e l'importo superiore dello scaglione >= IM_MAX_RIF_SCA
--                 non �ttivata l'operativit�u piu scaglioni
--                      Se il CORI �i tipo 'P' (Percipiente) o '*' (Entrambi):
--                           Aliquota percipiente = Aliquota Scaglione
--                           Base Calcolo percipiente = Base Calcolo Scaglione
--                           Ammontare percipiente lordo = (IMP_CORI * (base calcolo / 100) * Aliquota scaglione) / 100
--                           Ammontare percipiente = (IMP_CORI * (base calcolo / 100) * Aliquota scaglione) / 100
--                      Se il CORI �i tipo 'E' (Ente) o '*' (Entrambi):
--                           Aliquota ente = Aliquota Scaglione
--                           Base Calcolo ente = Base Calcolo Scaglione
--                           Ammontare ente lordo = (IMP_CORI * (base calcolo / 100) * Aliquota scaglione) / 100
--                           Ammontare ente = (IMP_CORI * (base calcolo / 100) * Aliquota scaglione) / 100
--                 Altrimenti l'imponibile o il trattamento prevedono l'operativit�u pi� scaglioni gestita come segue
--                      Viene scritto l'importo del primo scaglione
--                      Viene estratto la differenza (IMP_BLOCCO) tra l'importo superiore dello scaglione e il
--                      Montante del CORI in processo
--                      Viene estratta la differenza (RESTO) tra l'imponibile CORI e IMP_BLOCCO
--                      Viene costruita la tabella in ciclo dei CORI DET come segue:
--                      Se il CORI �i tipo 'P' (Percipiente) o '*' (Entrambi):
--                           Aliquota percipiente = Aliquota Scaglione
--                           Base Calcolo percipiente = Base Calcolo Scaglione
--                           Ammontare percipiente lordo = (IMP_CORI * (base calcolo / 100) * Aliquota scaglione) / 100
--                           Ammontare percipiente = (IMP_CORI * (base calcolo / 100) * Aliquota scaglione) / 100
--                      Se il CORI �i tipo 'E' (Ente) o '*' (Entrambi):
--                           Aliquota ente = Aliquota Scaglione
--                           Base Calcolo ente = Base Calcolo Scaglione
--                           Ammontare ente lordo = (IMP_CORI * (base calcolo / 100) * Aliquota scaglione) / 100
--                           Ammontare ente = (IMP_CORI * (base calcolo / 100) * Aliquota scaglione) / 100
--                      Vengono aggiornati a 0 l'aliquota e Base Calcolo della testata del CORI in processo.
--                      Vengono incrementati degli importi lordo e non del dettaglio CORI, l'Ammontare lordo e
--                      l'Ammontare della testata del CORI in processo
--                      Per ogni scaglione di reddito SCA_RED (lista ordinata per importo inferiore dello scaglione) che
--                      soddisfa le seguenti condizioni
--                            Codice CORI scaglione = CORI in processo
--                            Tipo anagrafico scaglione = Tipo anagrafico del compenso o '*'
--                            Data inizio validit�ello scaglione <= Data di registrazione del compenso
--                            Data fine validit�ello scaglione >= Data di registrazione del compenso
--                            Dati di territorio (Regione/Provincia/Comune) in processo
--                            Importo inferiore SCA_RED >= Importo superiore dello scaglione base
--                            Importo superiore SCA_RED <= IM_MAX_RIF_SCA
--                      Viene aggiornato IMP_BLOCCO come segue:
--                            Se  IM_MAX_RIF_SCA <   Importo superiore di SCA_RED
--                                IMP_BLOCCO = RESTO corente
--                            Altrimenti
--                                IMP_BLOCCO = Importo superiore dello scaglione imponibile - Importo inferiore dello scaglione imponibile + 1
--                            RESTO = RESTO - IMP_BLOCCO
--                      Viene aggiornata la tabella dei CORI DET come sopra con i dati di scaglione SCA_RED e IMP_BLOCCO aggiornato
--
--       Al termine dell'elaborazione vengono inseriti i CORI e relativo dettaglio ed aggiornata la testata del compenso
--       come segue:
--           Si nettizza l'IRPEF per scorporarla delle detrazioni eventualmente calcolate (solo in testata del dettaglio CORI)
--           Si calcolano le detrazioni nette in caso in cui si abbia detrazioni > IRPEF.
--           Importo totale compenso = Importo lordo percipiente + Importo CORI Ente
--           Importo netto percipiente = Importo lordo percipiente - Importo CORI Percipiente (+ IVA e RIVALSA anche se sono
--                                       carico ente)
--           Importo CORI percipiente = Somma importi calcolati CORI percipiente
--           Importo CORI ente = Somma importi calcolati CORI ente
--
-- Parametri:
-- inCdsCompenso -> Cds del compenso
-- inUoCompenso -> Codice UO del compenso
-- inEsercizioCompenso -> Esercizio del compenso
-- inPgCompenso -> Progressivo del compenso
-- inCopiaCdsCompenso -> Cds del compenso "copia"
-- inCopiaUoCompenso -> Codice UO del compenso "copia"
-- inCopiaEsercizioCompenso -> Esercizio del compenso "copia"
-- inCopiaPgCompenso -> Progressivo del compenso "copia"

   PROCEDURE elaboraCompenso
      (
       inCdsCompenso COMPENSO.cd_cds%TYPE,
       inUoCompenso COMPENSO.cd_unita_organizzativa%TYPE,
       inEsercizioCompenso COMPENSO.esercizio%TYPE,
       inPgCompenso COMPENSO.pg_compenso%TYPE,
       inCopiaCdsCompenso COMPENSO.cd_cds%TYPE,
       inCopiaUoCompenso COMPENSO.cd_unita_organizzativa%TYPE,
       inCopiaEsercizioCompenso COMPENSO.esercizio%TYPE,
       inCopiaPgCompenso COMPENSO.pg_compenso%TYPE,
       inCdsMcarriera MINICARRIERA.cd_cds%TYPE,
       inUoMcarriera MINICARRIERA.cd_unita_organizzativa%TYPE,
       inEsercizioMcarriera MINICARRIERA.esercizio%TYPE,
       inPgMinicarriera MINICARRIERA.pg_minicarriera%TYPE,
       inIdPaeseEle DOCUMENTO_ELE_TESTATA.id_paese%TYPE,
       inIdCodiceEle DOCUMENTO_ELE_TESTATA.id_codice%TYPE,
       inIdentificativoSdiEle DOCUMENTO_ELE_TESTATA.identificativo_sdi%TYPE,
       inProgressivoEle DOCUMENTO_ELE_TESTATA.progressivo%TYPE
      );

--
-- Aggiorna montanti
--
-- Aggiornamento dei montanti del terzo a seguito di registrazione/modifica compenso
--
-- pre-post-name: Aggiornamento dei montanti
-- pre: Viene richiesto di aggiornare i montanti di un terzo di tipo 'A' (Altro) in conseguenza dell'inserimento o modifica di un compenso
-- post:
--  Vengono letti i dati di base del compenso
--  Se in modifica
--        Vengono letti i dati della versione precedente del compenso
--  Recupero dei dati dell'anagrafico associato al terzo del compenso
--  Il montante viene decrementato degli importi derivati dal compenso ante modifica ed incrementato da quelli del compesno in modifica.
--  Vengono letti i dati relativi ai montanti per il soggetto anagrafico di riferimento e costruita la matrice degli stessi. Se non sono definiti montanti �itornato un record con i valori a zero.
-- I tipi di montanti sono 6:
--   pg=1 IRPEF (Dipendenti o Altro)
--   pg=2 INPS (Dipendenti o Altro)
--   pg=3 INPS TESORO (Dipendenti)
--   pg=4 INAIL (Altro)
--   pg=5 RIDUZ (Dipendenti o Altro)
--   pg=6 FONDO_FS (Dipendenti)
--
-- Quelli accumulati e aggiornati dalla procedura per tipo anagrafico 'A' ("Altro") sono:
--   pg=1 IRPEF
--   pg=2 INPS
--   pg=4 INAIL
--   pg=5 RIDUZ
--
-- Parametri:
-- inCdsCompenso -> Cds del compenso
-- inUoCompenso -> Codice UO del compenso
-- inEsercizioCompenso -> Esercizio del compenso
-- inPgCompenso -> Progressivo del compenso
-- inCopiaCdsCompenso -> Cds del compenso "copia"
-- inCopiaUoCompenso -> Codice UO del compenso "copia"
-- inCopiaEsercizioCompenso -> Esercizio del compenso "copia"
-- inCopiaPgCompenso -> Progressivo del compenso "copia"

   PROCEDURE aggiornaMontanti
      (
       inCdsCompenso COMPENSO.cd_cds%TYPE,
       inUoCompenso COMPENSO.cd_unita_organizzativa%TYPE,
       inEsercizioCompenso COMPENSO.esercizio%TYPE,
       inPgCompenso COMPENSO.pg_compenso%TYPE,
       inCopiaCdsCompenso COMPENSO.cd_cds%TYPE,
       inCopiaUoCompenso COMPENSO.cd_unita_organizzativa%TYPE,
       inCopiaEsercizioCompenso COMPENSO.esercizio%TYPE,
       inCopiaPgCompenso COMPENSO.pg_compenso%TYPE
      );

  Procedure aggiornaMontantiPagEst
   (
    inEsercizio  MONTANTI.esercizio%TYPE,
    inCd_anag    MONTANTI.Cd_anag%TYPE,
    inUtente     MONTANTI.utuv%Type,
    inPagamento  MONTANTI.inps_occasionali%Type
   );
  Function getParametriCNR
   (
    inEsercizio  PARAMETRI_CNR.esercizio%Type
   ) Return PARAMETRI_CNR%Rowtype;
--
-- Aggiorna compenso senza calcoli
--
-- pre-post-name: Aggiornamento del compenso per conferma dettagli cori in senza calcoli
-- pre: Viene richiesto di adeguare un compenso ai suoi CORI nel caso in cui il compenso sia senza calcoli
-- post:
--  Vengono letti i dati di base del compenso
--  Se in modifica
--        Vengono letti i dati della versione precedente del compenso
--  Recupero dei dati dell'anagrafico associato al terzo del compenso
--  Per ogni CORI del compenso
--      L'importo del CORi viene posto uguale a quello LORDO del CORI setsso
--       Vengono accumulati i seguenti totali:
--           Totale CORI percipiente
--           Totale CORI ente
-- Viene aggiornato il compenso (testata) con:
--           Totale compenso = Totale LORDO percipiente + Totale CORI Ente,
--           Totale netto percipiente = Totale LORDO percipiente - Totale CORI percipiente
--           Totale CORI percipiente
--           Totale CORI ente
--
-- Parametri:
-- inCdsCompenso -> Cds del compenso
-- inUoCompenso -> Codice UO del compenso
-- inEsercizioCompenso -> Esercizio del compenso
-- inPgCompenso -> Progressivo del compenso

   PROCEDURE aggCompensoSenzaCalcoli
      (
       inCdsCompenso COMPENSO.cd_cds%TYPE,
       inUoCompenso COMPENSO.cd_unita_organizzativa%TYPE,
       inEsercizioCompenso COMPENSO.esercizio%TYPE,
       inPgCompenso COMPENSO.pg_compenso%TYPE
      );

--
-- Controllo eliminazione del compenso
--
-- Esecuzione dei controlli di ammissibilit�lla cancellazione logica o fisica di un compenso
--
-- pre-post-name: Il compenso selezionato risulta essere gi�tato annullato
-- pre:           Si cerca di eliminare un compenso che risulta essere gi�n stato di annullato
-- post:          Viene sollevata un'eccezione
--
-- pre-post-name: Impossibile eliminare un compenso che risulta pagato
-- pre:           Si cerca di eliminare un compenso che risulta essere collegato a mandati e reversali
-- post:          Viene sollevata un'eccezione
--
-- pre-post-name: Impossibile eliminare un compenso che risulta associato ad una spesa del fondo economale
-- pre:           Si cerca di eliminare un compenso che risulta essere collegato ad una spesa del fondo economale
-- post:          Viene sollevata un'eccezione
--
-- pre-post-name: I compensi associati a missione possono essere eliminati solo dal pannello di gestione della missione
-- pre:           Si cerca di eliminare un compenso da missione senza eliminare la missione stessa
-- post:          Viene sollevata un'eccezione
--
-- pre-post-name: Impossibile eliminare un compenso associato a minicarriera
-- pre:           Si cerca di eliminare un compenso da minicarriera quando questa risulta sospesa o cessata
-- post:          Viene sollevata un'eccezione
--
-- pre-post-name: Esegui cancellazione di un compenso
-- pre:           Viene richiesta la validazione sulla eliminazione di un compenso
-- post:
--       Vengono letti i dati di base del compenso
--            Se il compenso risulta essere annullato (vedi pre-post-name: Il compenso selezionato risulta essere gi�--            stato annullato).
--            Se il compenso risulta essere almeno pagato (vedi pre-post-name: Impossibile eliminare un compenso
--            che risulta pagato).
--            Se il compenso �a missione (vedi pre-post-name: I compensi associati a missione possono essere eliminati
--            solo dal pannello di gestione della missione).
--            Se il compenso �a conguaglio e non sono verificate le condizioni di cui sopra allora:
--                 Si procede all'eliminazione dello stesso conguaglio ponendo il compenso abilitato alla cancellazione
--            Se il compenso �a minicarriera e non sono verificate le condizioni di cui sopra allora:
--                 Si procede allo sgancio del compenso dalla minicarriera ed all'aggiornamento dello stato della
--                 stessa in merito all'associazione con i compensi ponendo il compenso abilitato alla cancellazione
--            In tutti i casi la cancellazione �atta dal client mentre la procedura, abilitando la cancellazione
--            del compenso, definisce anche se questa pu�sere fisica o solo logica. E' logica quando il compenso
--            risulta essere gi�taton associato a mandati ora annullati.
--
-- Parametri:
-- inCdsCompenso -> Cds del compenso
-- inUoCompenso -> Codice UO del compenso
-- inEsercizioCompenso -> Esercizio del compenso
-- inPgCompenso -> Progressivo del compenso
-- statoCancella --> Ritorna 1 se cancellazione logica 2 se fisica

   PROCEDURE eseguiDelCompenso
      (
       inCdsCompenso COMPENSO.cd_cds%TYPE,
       inUoCompenso COMPENSO.cd_unita_organizzativa%TYPE,
       inEsercizioCompenso COMPENSO.esercizio%TYPE,
       inPgCompenso COMPENSO.pg_compenso%TYPE,
       statoCancella IN OUT NUMBER
      );

----------------------------------------------------------------------------------------------------
-- ROUTINE COMUNI
----------------------------------------------------------------------------------------------------
-- Lettura dati di base legati al compenso in calcolo, testata compenso, anagrafico comprensivo,
-- eventualmente, di quelli originali in caso di modifica. I dati sono memorizzati rispettivamente
-- per compenso in aRecCompenso e aRecCompensoOri
-- per anagrafico in aRecAnagrafico e aRecAnagraficoOri

   PROCEDURE getDatiBaseCompenso
      (
       inCdsCompenso COMPENSO.cd_cds%TYPE,
       inUoCompenso COMPENSO.cd_unita_organizzativa%TYPE,
       inEsercizioCompenso COMPENSO.esercizio%TYPE,
       inPgCompenso COMPENSO.pg_compenso%TYPE,
       inCopiaCdsCompenso COMPENSO.cd_cds%TYPE,
       inCopiaUoCompenso COMPENSO.cd_unita_organizzativa%TYPE,
       inCopiaEsercizioCompenso COMPENSO.esercizio%TYPE,
       inCopiaPgCompenso COMPENSO.pg_compenso%TYPE,
       eseguiLock CHAR
      );

-- Valorizzazione della matrice per la memorizzazione dei montanti definiti per un soggetto anagrafico.

   PROCEDURE costruisciTabMontanti
      (
       aRecMontanti MONTANTI%ROWTYPE
      );

-- Rettifica dei valori dei montanti; questi sono ridotti di quanto calcolato dal compenso origine in caso
-- di modifica se coincidono il riferimento all'anagrafico e quello del tipo compenso dipendente o altro

   PROCEDURE modificaTabMontanti
      (
       aRecCompenso COMPENSO%ROWTYPE,
       segno CHAR
      );

-- Controllo ammissibilit�el compenso rispetto al tipo trattamento

   PROCEDURE chkCompensoTipoTrattamento
      (
       aInserimentoModifica CHAR
      );

-- Attivazione del calcolo delle detrazioni familiari e personali

   PROCEDURE attivaDetrazioni;

-- Valorizzazione delle matrici per il calcolo dei dettagli di un compenso; CONTRIBUTO_RITENUTA e
-- CONTRIBUTO_RITENUTA_DET

   PROCEDURE costruisciTabellaCori;

   PROCEDURE costruisciTabellaCoriDet
      (
       indice BINARY_INTEGER,
       aImponibile CONTRIBUTO_RITENUTA_DET.imponibile%TYPE,
       aRecVPreScaglione V_PRE_SCAGLIONE%ROWTYPE
      );

-- Verifica consistenza dei dati della matrice per il calcolo dei dettagli di un compenso

   PROCEDURE verificaTabellaCori;

-- Calcola CORI per trattamenti dipendente o altro

   PROCEDURE calcolaCoriDipendente;
   PROCEDURE calcolaCoriAltro
      (
       inCdsMcarriera MINICARRIERA.cd_cds%TYPE,
       inUoMcarriera MINICARRIERA.cd_unita_organizzativa%TYPE,
       inEsercizioMcarriera MINICARRIERA.esercizio%TYPE,
       inPgMinicarriera MINICARRIERA.pg_minicarriera%TYPE
      );

-- Ritorna le informazioni relative alle modalit�i calcolo del cori come eccezioni rispetto alla
-- normale gestione. Sono previste le seguenti gestioni
-- 1) Compenso senza calcoli. Ignora gestione per aliquota in anagrafico o annualizzazione.
-- 2) Aliquota fiscale valorizzata in anagrafico. Si accede alla tabella scaglioni con l'aliquota
--    recuperata indipendentemente dal montante
-- 3) Annualizzazione.
-- Le gestioni 2 e 3 si applicano solo ad IRPEF a scaglioni ed addizionali comunali, provinciali e
-- regionali.

   FUNCTION getRilevaAnnualizzato
      (
       indice BINARY_INTEGER
      )RETURN CHAR;

-- Valorizza i dati per contributi ritenuta definiti per territorio

   PROCEDURE getDatiTerritorio
      (
       aCdRegione IN OUT COMPENSO.cd_regione_add%TYPE,
       aCdProvincia IN OUT COMPENSO.cd_provincia_add%TYPE,
       aPgComune IN OUT COMPENSO.pg_comune_add%TYPE,
       indice BINARY_INTEGER
      );

-- Calcolo imponibile per ogni singolo cori del trattamento

   FUNCTION getImponibileCori
      (
       indice BINARY_INTEGER
      )RETURN NUMBER;

-- Ritorna il montante di riferimento per ogni singolo cori del trattamento (dipendenti)

   PROCEDURE getImportoMontanteDip
      (
       aClassificazioneMontanti TIPO_CONTRIBUTO_RITENUTA.pg_classificazione_montanti%TYPE,
       aMontanteLordo IN OUT NUMBER,
       aMontanteNetto IN OUT NUMBER,
       aImDeduzione IN OUT NUMBER
      );

-- Ritorna il montante di riferimento per ogni singolo cori del trattamento (altro)

   PROCEDURE getImportoMontanteAltro
      (
       aClassificazioneMontanti TIPO_CONTRIBUTO_RITENUTA.pg_classificazione_montanti%TYPE,
       aMontanteLordo IN OUT NUMBER,
       aMontanteNetto IN OUT NUMBER,
       aImDeduzione IN OUT NUMBER,
       aImDeduzioneFamily IN OUT NUMBER,
       aMontanteLordoOcca IN OUT NUMBER,
       aMontanteNettoOcca IN OUT NUMBER
      );

-- Calcolo delle detrazioni familiari

   PROCEDURE calcolaDetrazioni
      (
       indice BINARY_INTEGER,
       aImportoRiferimento NUMBER,
       inCdsMcarriera MINICARRIERA.cd_cds%TYPE,
       inUoMcarriera MINICARRIERA.cd_unita_organizzativa%TYPE,
       inEsercizioMcarriera MINICARRIERA.esercizio%TYPE,
       inPgMinicarriera MINICARRIERA.pg_minicarriera%Type,
       inRilevaAnnualizzato CHAR,
       aNumMMTotaleMcarriera INTEGER,
       aNumMMEsercizio INTEGER
      );

   PROCEDURE calcolaDetrazioniFam
      (
       aImportoRiferimento NUMBER,
       aCdAnag ANAGRAFICO.cd_anag%TYPE,
       aDtCompetenzaDa DATE,
       aDtCompetenzaA DATE,
       aDtRegistrazione DATE,
       aImportoDetrazCo IN OUT NUMBER,
       aImportoDetrazFi IN OUT NUMBER,
       aImportoDetrazAl IN OUT NUMBER,
       aImportoDetrazFiS IN OUT NUMBER,
       esisteFiSenzaConiuge CHAR Default 'N'
      );

   PROCEDURE calcolaDetrazioniPer
      (
       aImportoRiferimento NUMBER,
       aCdAnag ANAGRAFICO.cd_anag%TYPE,
       aDtCompetenzaDa DATE,
       aDtCompetenzaA DATE,
       aDtRegistrazione DATE,
       aEsercizio COMPENSO.esercizio%TYPE,
       aImportoDetrazPe IN OUT NUMBER
      );

   PROCEDURE calcolaDetrazioniAltriTipi
      (
       aImportoRiferimento NUMBER,
       aCdAnag ANAGRAFICO.cd_anag%TYPE,
       aDtCompetenzaDa DATE,
       aDtCompetenzaA DATE,
       aDtRegistrazione DATE,
       aEsercizio COMPENSO.esercizio%TYPE,
       aImportoDetrazLa IN OUT NUMBER
      );

   -- Calcolo del credito irpef
   FUNCTION calcolaCreditoIrpef
      (
       --indice BINARY_INTEGER,
       aImportoRiferimento NUMBER,
       inNumGGTotMinPerCredito INTEGER,
       aRecCompenso COMPENSO%ROWTYPE,
       cdCori TIPO_CONTRIBUTO_RITENUTA.CD_CONTRIBUTO_RITENUTA%TYPE
      ) RETURN NUMBER;

   FUNCTION getNumeroGGCompensiCreditoArr
      ( aTerzo  COMPENSO.cd_terzo%TYPE,
        aEsercizio COMPENSO.esercizio%TYPE,
        aDtInizioValCredito  CREDITO_IRPEF.dt_inizio_validita%TYPE,
        aDtInizioAppCredito  CREDITO_IRPEF.dt_inizio_applicazione%TYPE
      ) RETURN NUMBER;

-- Ritorna il reddito di quanto gi�agato da minicarriera (per il calcolo del reddito complessivo)

   FUNCTION getImponibPagNonMcarrieraAltro
      (
       aEsercizio NUMBER,
       aTerzo TERZO.cd_terzo%TYPE
      ) RETURN NUMBER;

-- Ritorna il Credito gi�alcolato anche per compensi non ancora pagati

   FUNCTION getCreditoGiaCalcolato
      (
       aCdCdsCompenso COMPENSO.cd_cds%TYPE,
       aCdUoCompenso COMPENSO.cd_unita_organizzativa%TYPE,
       aEsercizioCompenso COMPENSO.esercizio%TYPE,
       aPgCompenso COMPENSO.pg_compenso%TYPE,
       aTerzo TERZO.cd_terzo%TYPE,
       cdCori TIPO_CONTRIBUTO_RITENUTA.CD_CONTRIBUTO_RITENUTA%TYPE
      ) RETURN NUMBER;

FUNCTION arrotondaCredito
   (
    aCredito  CREDITO_IRPEF.im_credito%TYPE,
    DtDaComp COMPENSO.DT_DA_COMPETENZA_COGE%TYPE,
    DtAComp  COMPENSO.DT_A_COMPETENZA_COGE%TYPE,
    aRecCreditoIrpef  CREDITO_IRPEF%ROWTYPE
   ) RETURN NUMBER;

   PROCEDURE riempiCarFamTmp
      (
       indice1 IN OUT INTEGER,
       aRecCaricoFamAnag CARICO_FAMILIARE_ANAG%ROWTYPE,
       tabella_car_fam_tmp IN OUT CNRCTB545.caricoFamTab
      );

   PROCEDURE scaricaCarFamTmp
      (
       indice IN OUT INTEGER,
       tabella_car_fam_ok IN OUT CNRCTB545.caricoFamTab,
       tabella_car_fam_tmp IN OUT CNRCTB545.caricoFamTab
      );

-- Scrittura dettagli compenso

   PROCEDURE scriviDettaglioCompenso;

-- Determina il netto delle detrazioni personali e familiari se IRPEF risulta inferiore al totale
-- calcolato di queste

   PROCEDURE nettizzaDetrazioni
      (
       aImporto NUMBER
      );

-- Eseguo la cancellazione del compenso

   FUNCTION cancellaCompenso
      (
       inCdsCompenso COMPENSO.cd_cds%TYPE,
       inUoCompenso COMPENSO.cd_unita_organizzativa%TYPE,
       inEsercizioCompenso COMPENSO.esercizio%TYPE,
       inPgCompenso COMPENSO.pg_compenso%TYPE,
       idChiamante CHAR
      ) RETURN INTEGER;

-- Calcolo della deduzione

   PROCEDURE calcolaDeduzione
      (
       indice BINARY_INTEGER,
       inAnagrafico ANAGRAFICO%Rowtype,
       inCdsMcarriera MINICARRIERA.cd_cds%TYPE,
       inUoMcarriera MINICARRIERA.cd_unita_organizzativa%TYPE,
       inEsercizioMcarriera MINICARRIERA.esercizio%TYPE,
       inPgMinicarriera MINICARRIERA.pg_minicarriera%Type,
       inRilevaAnnualizzato CHAR,
       inImportoRiferimento NUMBER,
       inImponibileLordo NUMBER,
       aTotDeduzioneGoduta NUMBER,
       aTotDeduzioneFamilyGoduta NUMBER,
       aNumGGTotaleMcarriera INTEGER,
       aNumGGProprioMcarriera INTEGER,
       aNumMMTotaleMcarriera INTEGER,
       aNumMMProprioMcarriera INTEGER,
       aNumMMEsercizio INTEGER,
       aImDeduzioneCori IN OUT NUMBER,
       aImDeduzioneLordoCori IN OUT NUMBER,
       aImDeduzioneFamilyCori IN OUT NUMBER,
       aImDeduzioneFamilyLordoCori IN OUT NUMBER
      );

-- Controlla accoppiamenti di valore di

   FUNCTION checkCoppieValoriCoriEnte
     (
      isCompensoSenzaCalcoli COMPENSO.fl_senza_calcoli%TYPE,
      inCdsCompenso COMPENSO.cd_cds%TYPE,
      inUoCompenso COMPENSO.cd_unita_organizzativa%TYPE,
      inEsercizioCompenso COMPENSO.esercizio%TYPE,
      inPgCompenso COMPENSO.pg_compenso%TYPE
     ) RETURN VARCHAR2;

	FUNCTION getRegConScaglioneSpe
      (
       acdRegione regione.cd_regione%type,
       aDataOdierna DATE
      ) RETURN scaglione%ROWTYPE;

End CNRCTB550;
