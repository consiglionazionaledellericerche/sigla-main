==============
Fattura Attiva
==============

Per poter procedere alla registrazione di una fattura attiva bisogna entrare nell’albero delle funzioni:  
 **- Documenti amministrativi**  
      - Fatture   
      - fattura attiva  

          - visualizzazione   
          - gestione  
          
Sono di seguito illustrati i vari folder di cui si compone la maschera “Fattura Attiva”:

**Testata**

.. figure:: /docs/screenshot/fatt_att_testata.png

Analizziamo ora i vari campi presenti nel folder “Testata” che sono comuni per qualunque tipologia di fattura attiva che verrà emessa. 
- Progressivo Tale campo sarà assegnato automaticamente dal sistema al momento del salvataggio della fattura. 
- Data registrazione Tale campo viene proposto dal sistema e non deve essere mai modificato. 
- Registrazione IVA Tale campo sarà compilato automaticamente dal sistema successivamente alla protocollazione della fattura. 
- N. Registrazione IVA generale Tale campo sarà compilato automaticamente dal sistema successivamente alla protocollazione della fattura. 
- Data Stampa Tale campo sarà compilato automaticamente dal sistema successivamente alla protocollazione/stampa della fattura. 
- Competenza dal  Competenza al Deve essere indicato il periodo di realizzazione del ricavo (effettuazione della prestazione). 
- Sezionale il sistema proporrà sempre ed esclusivamente “Registro IVA ordinario delle fatture emesse”.  
- Tipo Documento scegliere nel menù a tendina la voce che interessa 
- Totale Imponibile Tale campo sarà compilato automaticamente dal sistema con la compilazione del folder “dettaglio”. 
- Totale Iva Tale campo sarà compilato automaticamente dal sistema con la compilazione del folder “dettaglio”. 
- Totale Tale campo sarà compilato automaticamente dal sistema con la compilazione del folder “dettaglio”. 
- Descrizione libera. 
- Mod. pagamento scegliere nel menù a tendina la voce che interessa tenendo presente che:  
      - BI – Banca d’Italia deve essere scelta tale modalità nel caso il cliente sia  un’amministrazione dello Stato o un Ente Pubblico presente nella tabella A legge 720/84 
      - BO – Bonifico su c/c Bancario N.B. il c/c che deve essere fornito ai nostri clienti è  IBAN IT57S0100503392000000218155 
   
Esaminiamo ora i campi particolari: 
- Liquidazione Differita deve essere messo il   solo ed esclusivamente in caso di emissione di fattura attiva a favore di un organo dello Stato o di un Ente locale ai sensi art.6 DPR633/72 (In tal caso il nostro debito IVA sorgerà nel momento dell’incasso della fattura). 
- Intra U.E. deve essere messo il   nel caso in cui il cliente sia residente in un Paese INTRA-UE questa scelta comporta a sua volta la comparsa di un altro record: 
- Servizi/Beni scegliere nel menù a tendina la voce che interessa 
- Extra U.E. deve essere messo il   nel caso in cui il cliente sia residente in un Paese EXTRA-UE. 
- S. Marino deve essere messo il   nel caso in cui il cliente sia residente a San Marino. 

**CLIENTE**
 
Si ricorda che per poter indicare un terzo residente in un Paese INTRA-UE è necessario mettere in testata il flag su Intra UE; mentre se il terzo è residente in un Paese EXTRA-UE è necessario mettere il flag su Extra UE; infine se il terzo è residente a San Marino è necessario mettere il flag su San Marino. 



