=================
LIQUIDAZIONE IVA
=================

Entro il giorno 5 del mese (escluso il mese di dicembre in cui queste chiusure sono generalmente anticipate al giorno 28 dello stesso mese) è necessario fare la chiusura di tutti i registri IVA del mese precedente e successivamente fare il calcolo provvisorio della liquidazione IVA. E’ quindi  necessario entrare nell’albero delle funzioni: 

 **- Gestione IVA**  
 
      - Registri IVA  
      - fattura attiva  
          - Stampa provvisoria registri IVA   
            - gestione  
          
          - Stampa definitiva registri IVA
             - gestione  
  
Si sceglie il “Tipo Sezionale” e il mese interessato e si clicca sull’icona stampa. La stampa deve essere effettuata per tutti i sezionali. 
Tale operazione è uguale per la stampa definitiva dei registri IVA (in alcuni casi tale operazione non è consentita per il registro acquisti istituzionali). 

Successivamente è necessario entrare nell’albero delle funzioni: 

    - Liquidazione IVA 
      - Liquidazione Provvisoria IVA  
          - Gestione
 
Si sceglie il “Tipo” sezionale e il relativo mese e si clicca sull’icona “Calcola”,  si conferma la stampa e si può vedere se la UO è a debito o a credito. 

Rivedere-Creazione variazione per spostamento fondi alla sede centrale e gae di blocco operatività
===================================================================================================

**Importante**
IVA Commerciale detraibile tenendo conto della percentuale prorata (prorata 4% - detraibile il 96%) - Alimenta registo Acquisti e Vendite
IVA Istituzionale non detraibile - Alimenta solo registro Acquisti.

Iva split viene trattenuta e versata;
Iva non residenti ugualmente viene versata.
Entrambi i casi l'obbligo di versamento scatta con il pagamento della fattura acquisti.


(Non più valido dopo modifiche split)

Nel caso in cui l’IVA è a debito,  si ricorda che la liquidazione IVA è fatta automaticamente sugli stanziamenti di competenza sulla voce 1.01.452, nel caso in cui la copertura sia disponibile sui residui (solo dell’esercizio n-1) è necessario darne comunicazione con un congruo anticipo all’ufficio fiscale (non oltre il giorno 6 di ogni mese). Si ricorda che le fatture emesse ad esigibilità differita confluiscono nella liquidazione del mese in cui vengono incassate.

**Dalla Nota tecnica: Gestione Split Payment 2017**

1.4	Registri IVA e Liquidazione IVA
Rispetto all’argomento ‘adempimenti periodici IVA’, ci sono delle novità per quanto riguarda le attività a carico degli Istituti. E’ di fondamentale importanza che tutte le UO svolgano le attività indicate di seguito onde evitare che vengano create variazioni automatiche, riguardanti l’IVA Commerciale,  per l’intero importo sulla competenza.

Attualmente, infatti, viene già creata una variazione in conto competenza, sulla GAE C0000020, per l’importo dell’IVA a debito commerciale (eccetto comunicazione specifica all’Ufficio della Sede Centrale che si occupa del versamento IVA).

Con l’introduzione dello Split Payment, l’ammontare dell’IVA a debito Commerciale per ogni UO, comprenderà anche l’IVA per le fatture di acquisto Commerciali verso soggetti Split. Questi importi, che in fase di liquidazione devono essere girati all’Ufficio della Sede Centrale che effettua il versamento, potrebbe creare l’esigenza da parte delle UO (più sentita rispetto a prima) di utilizzare somme e GAE in conto residuo, oltre che in conto competenza. Per questa ragione, dal mese di Luglio 2017, ogni UO deve effettuare la stampa definitiva di registri e liquidazioni, in modo da dare le indicazioni corrette per la creazione automatica delle variazioni per il trasferimento dell’Importo IVA da girare alla Sede Centrale.
Di seguito le attività da compiere, da parte di ogni UO, per la liquidazione periodica IVA (da effettuarsi entro i termini indicati dalla circolare relativa allo Split payment:

1.	Chiusura provvisoria di tutti i sezionali iva;
2.	Chiusura definitiva di tutti i sezionali iva;
3.	Liquidazione provvisoria iva selezionando nella tendina “tutti i sezionali commerciali”;
4.	Verificare l’importo di un eventuale Debito / credito;
5.	In caso di IVA da versare, come si vede nelle mappa seguente, sarà mostrato l’importo a debito per il quale l’utente dovrà, nel nuovo Pennellino aggiunto ‘Ripart. Finanziaria’, indicare la ripartizione del debito IVA per esercizio residuo o competenza, CDR e GAE (di natura 3).


**Nota bene**
E’ possibile indicare anche un CDR della UO che sta effettuando la liquidazione, diverso dalla UO principale, in modo tale da prelevare importi per il giroconto degli importi IVA verso la Sede Centrale senza dover fare precedenti variazioni dal CDR alla UO principale.
Si ricorda che il debito iva corrispondente all’emissione di fatture attive imputate su accertamenti di competenza dovrà essere ripartito su Gae in conto competenza.

6.	Procedere alla “liquidazione definitiva iva” selezionando nella tendina “tutti i sezionali commerciali”;

7.	La procedura automatica utilizzerà le  informazioni indicate nella ‘Ripartizione Finanziaria’ e genererà le variazioni trasferendo le disponibilità al Cdr 000.407.000 per consentire la chiusura dell’Iva Ente del mese di riferimento con conseguente versamento all’Erario. 

Nel caso in cui non ci fosse disponibilità sugli esercizi/GAE indicate nella ‘Ripartizione Finanziaria’, oppure non si eseguono gli adempimenti mensili richiesti, la procedura automatica di creazione delle Variazioni IVA imputerà l’importo corrispondente sulla GAE C0000020 in conto competenza.
Dopo aver eseguito la Liquidazione IVA mensile definitiva sarà possibile consultare le variazioni automatiche create nel caso di IVA a debito per l’attività commerciale:



