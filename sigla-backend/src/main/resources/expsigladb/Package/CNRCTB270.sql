--------------------------------------------------------
--  DDL for Package CNRCTB270
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRCTB270" AS
-- =================================================================================================
--
-- CNRCTB270 - Contabilizzazione liquidazione IVA - creazione pratica contabile
--
-- Date: 14/07/2006
-- Version: 4.5
--
-- Dependency: CNRCTB 001/010/015/018/020/030/035/037/038/040/080/100/110/575 IBMERR 001
--
-- History:
--
-- Date: 03/07/2002
-- Version: 1.0
-- Creazione
--
-- Date: 04/07/2002
-- Version: 1.1
-- Completata la gestione della parte relativa alla linea di attivitÃ  e cdr in obbligazione
--
-- Date: 04/07/2002
-- Version: 1.2
-- Fix importo da utilizzare per la liquidazione
--
-- Date: 04/07/2002
-- Version: 1.3
-- La liquidazione non viene fatta nel caso esista un credito di iva
-- Viene creato un generico cumulativo per la chiusura di tutte le partite di giro in spesa aperte dalle liquidazioni locali
--
-- Date: 17/07/2002
-- Version: 1.4
-- Trunc su data scadenza accertamento pgiro
--
-- Date: 18/07/2002
-- Version: 1.5
-- Aggiornamento documentazione
--
-- Date: 19/07/2002
-- Version: 1.6
-- Recuperato da configurazione CNR il conto di versamento dell'IVA al centro (No partita giro)
--
-- Date: 24/07/2002
-- Version: 1.7
-- Modifica assegnamento progressivi documento generico
-- Controllo che il terzo in GRUPPO CR DET sia specificato completamente
--
-- Date: 25/07/2002
-- Version: 1.8
-- Fix saldo collegamento doc amm su scad obb di versamento centrale
--
-- Date: 25/07/2002
-- Version: 1.9
-- Fix imp collegato doc cont
--
-- Date: 17/09/2002
-- Version: 2.0
-- Aggiunta la gestione dell'annullamento della liquidazione IVA
--
-- Date: 27/09/2002
-- Version: 2.1
-- Fix fl_pgiro su righe mandato su partite di giro
--
-- Date: 28/09/2002
-- Version: 2.2
-- Fix estrazione LA per IVA
--
-- Date: 30/01/2003
-- Version: 2.3
-- Gestione tipo liquidazione COMMERCIALE
--
-- Date: 04/02/2003
-- Version: 2.4
--
-- Attivata la gestione completa del tipo liquidazione. Aggiunto il parametro aTipoLiquidazione
-- in ingresso alla procedura contabilLiquidIVA
-- Modificata la routine di annullamento della liquidazione per operare sul tipo_liquidazione
-- del record in input
-- Modificata la routine di recupero della descrizione della liquidazione per differenziarla in
-- base al tipo_liquidazione
--
-- Date: 07/02/2003
-- Version: 2.5
-- Viene utilizzato im_iva_da_versare al posto di iva_deb_cred con la stessa semantica di segno (verso se negativo)
--
-- Date: 25/02/2003
-- Version: 3.0
-- Revisione liquidazione finanizaria dell'IVA
--
-- Date: 27/02/2003
-- Version: 3.1
-- Fix recupero elemento_voce per pratica centro
--
-- Date: 27/02/2003
-- Version: 3.2
-- Fix per registrazione OBB PGIRO TRONCA di liquidazione centralizzata dell'IVA
--
-- Date: 27/02/2003
-- Version: 3.3
-- Gestione versamento iva al centro se importo da versare > di 26 euro
--
-- Date: 27/02/2003
-- Version: 3.4
-- Gestione chiusura LIQUIDAZIONE_IVA_CENTRO anche quando il debito IVA dell'ente per periodo Ã¨ <= 0
--
-- Date: 28/02/2003
-- Version: 3.5
-- Non annullabilitÃ  della liquidazione IVA locale e del centro
--
-- Date: 07/03/2003
-- Version: 3.6
-- Gestione della liquidazione finanziaria per UO liquidate via interfaccia
--
-- Date: 07/03/2003
-- Version: 4.0
-- Gestione liquidazione INTRAUE e SAN MARINO SENZ'IVA su partita di giro
--
-- Date: 12/03/2003
-- Version: 4.1
-- Tolto il controllo che l'UO di versamento centralizzato dell'IVA non sia appartenente a CDS liquidato tramite interfaccia
--
-- Date: 29/04/2003
-- Version: 4.2
-- Tolta UO da descrizione pratica finanziaria IVA centro (su reversale provv, accertamento, generico) err.n. 585
--
-- Date: 26/06/2003
-- Version: 4.3
-- Adeguamenti minimi per chiusura
--
-- Date: 26/02/2004
-- Version: 4.4
-- La reversale di accantonamento IVA va in economica sul conto di patrimonio C/Erario IVA
--
-- Date: 14/07/2006
-- Version: 4.5
-- Gestione Impegni/Accertamenti Residui:
-- aggiornata la funzione per tener conto anche del campo Esercizio Originale Impegno/Accertamento
--
-- Date: 02/01/2007
-- Version: 4.6
-- Gestione Liquidazione IVA sui residui: aggiunta la procedura "controllaAssunzioneImpResImpro"
-- per inibire l'emissione di impegni residui quando esistono ancora impegni residui che gravano
-- sulla G.A.E. dell''IVA e modificata "contabilLiquidIVA" con il nuovo parametro TipoImpegno
--
-- =================================================================================================
--
-- Constants
--
 LINEA_ATTIVITA_SPECIALE CONSTANT VARCHAR2(50):='LINEA_ATTIVITA_SPECIALE';
 LA_VER_IVA CONSTANT VARCHAR2(100):='LINEA_COMUNE_VERSAMENTO_IVA';
 LA_VER_IVA_SAC CONSTANT VARCHAR2(100):='LINEA_VERSAMENTO_IVA_SAC';

 ELEMENTO_VOCE_SPECIALE CONSTANT VARCHAR2(50):='ELEMENTO_VOCE_SPECIALE';
 VOCE_VERSAMENTO_IVA CONSTANT VARCHAR2(100):='VOCE_VERSAMENTO_IVA';
 VOCE_IVA_FATTURA_ESTERA CONSTANT VARCHAR2(100):='VOCE_IVA_FATTURA_ESTERA';

 STATO_LIQUID_CENTRO_CHIUSO  CONSTANT CHAR(1):='C';
 STATO_LIQUID_CENTRO_INIZIALE  CONSTANT CHAR(1):='I';

--
-- Functions e Procedures
--
-- Contabilizza liquidazione IVA
--
-- pre-post-name: Liquidazione non trovata
-- pre: Ricerca senza successo della liquidazione corrispondente ai parametri specificati invocando la procedura (cds,uo,esercizio,data da/a,tipo)
-- post: viene sollevata un'eccezione
--
-- pre-post-name: Importo iva_da_versare null
-- pre: Nella liquidazione in processo l'importo iva_da_versare Ã¨ non valorizzato
-- post: viene sollevata un'eccezione
--
-- pre-post-name: UO di versamento IVA non trovata
-- pre: Ricerca senza successo dell'UO di versamento IVA specificata in CONFIGURAZIONE_CNR alla voce UO_SPECIALE, UO_VERSAMENTO_IVA
-- post: viene sollevata un'eccezione
--
-- pre-post-name: UO ente non trovata
-- pre: Ricerca senza successo dell'UO ente
-- post: viene sollevata un'eccezione
--
-- pre-post-name: UO ente non trovata
-- pre: Ricerca senza successo dell'UO ente
-- post: viene sollevata un'eccezione
--
-- pre-post-name: Importo IVA da versare (iva_da_versare) >= 0
-- pre: Richiesta di liquidazione finanziaria IVA con iva_da_versare >= 0
-- post: la procedura termina senza sollevare eccezioni
--
-- pre-post-name: Recupero senza successo del CORI IVA da CONFIGURAZIONE_CNR
-- pre: Ricerca senza successo del CORI IVA da configurazione CNR alla voce CORI_SPECIALE, IVA
-- post: viene sollevata un'eccezione
--
-- pre-post-name: Ricerca senza successo CDR responsabile dell'UO di versamento (nel caso di vers. centr.) o dell'UO locale
-- pre: Non viene trovato il CDR responsabile dell'UO in processo (UO di versamento per versamento accentrato, UO locale altrimenti)
-- post: viene sollevata un'eccezione
--
-- pre-post-name: Estrazione senza successo della linea di attivitÃ  COMUNE per versamento IVA specificata come codice in configurazione CNR
-- pre: Non viene trovato la linea di attivita specificata in CONFIGURAZIONE_CNR alla voce LINEA_ATTIVITA_SPECIALE,LINEA_COMUNE_VERSAMENTO_IVA
--      nel CDR responsabile dell'UO in processo (UO di versamento per versamento accentrato, UO locale altrimenti)
-- post: viene sollevata un'eccezione
--
-- pre-post-name: Divisa euro non trovata
-- pre: Non viene trovata la divisa EURO specificata in CONFIGURAZIONE_CNR alla voce CD_DIVISA,EURO
-- post: viene sollevata un'eccezione
--
-- pre-post-name: Voce del piano non trovata per liquidazione locale NON via interfaccia COMMERCIALE
-- pre: Non viene trovata voce del piano specificata in CONFIGURAZIONE_CNR per ESERCIZIO,ELEMENTO_VOCE_SPECIALE,VOCE_VERSAMENTO_IVA
-- post: viene sollevata un'eccezione
--
-- pre-post-name: Voce del piano non trovata per liquidazione locale NON via interfaccia INTRAUE/SAN MARINO SENZ'IVA
-- pre: Non viene trovata voce del piano specificata in CONFIGURAZIONE_CNR per ESERCIZIO,ELEMENTO_VOCE_SPECIALE,VOCE_IVA_FATTURA_ESTERA
-- post: viene sollevata un'eccezione
--
-- pre-post-name: Capitolo/Articolo finanziario non trovato per liquidazione locale NON via interfaccia
-- pre: Non viene trovato il CAPITOLO (CDS<>SAC) o ARTICOLO (SAC) da usare per liquidazione IVA locale
-- post: viene sollevata un'eccezione
--
-- pre-post-name: Recupero senza successo del terzo corrispondente all'UO di versamento dell'IVA
-- pre: Non viene trovato il terzo corrispondente all'UO di versamento IVA (indirizzario del mandato versamento iva locale)
-- post: viene sollevata un'eccezione
--
-- pre-post-name: Versamento centralizzato e IVA da versare > -26 Euro
-- pre: L'importo iva_da_versare della liquidazione del centro > -26
-- post: Non viene emessa la pratica di versamento centralizzato dell'IVA
--
-- pre-post-name: Dettagli per versamento CORI  IVA del centro non trovati
-- pre: Con il CORI IVA vengono cercati, senza successo, nella tabella GRUPPO_CORI_DET, il terzo di versamento e relative info bancarie
--      in corrispondenza di REGIONE INDEFINITA (*) e COMUNE INDEFINITO (0)
-- post: Viene sollevata un'eccezione
--
-- pre-post-name: Conto finanziario per versamento CORI IVA del centro non trovato
-- pre: Con il CORI IVA viene cercato il capitolo di partita di giro su cui costruire la pratica di versamento e non viene trovato
--      Tramite la tabella ASS_TIPO_CORI_EV viene recuperato senza successo il capitolo (di entrata CDS) corrispondente al CORI IVA (ENTE)
--      oppure, dalla tabella ASS_PARTITA_GIRO viene cercato senza successo il capitolo di spesa corrispondente su partita di giro
-- post: Viene sollevata un'eccezione
--
-- pre-post-name: Contabilizzazione finanziaria dell'IVA
-- pre: Nessuna delle precodizioni precedenti Ã¨ verificata
-- post:
--  1.  Caso di unitÃ  organizzativa non liquidata via interfaccia, liquidazione propria
--  1.1  Viene creata un'obbligazione, generico e mandato di versamento all'UO di versamento IVA
--        Se la liquidazione Ã¨ di tipo commerciale -> viene creata un'obbligazione su partite normali, generico di trasferimento e mandato di versamento
--        Se la liquidazione Ã¨ di tipo intraue o Sna MArino senz'iva -> viene creata un'obbligazione su partita di giro tronca, generico di versamento cori e mandato di versamento
--         Il conto viene recuperato da ASS_PARTITA_GIRO entrando con il conto specificato in configurazione CNR alla voce ESERCIZIO, ELEMENTO_VOCE_SPECIALE, VOCE_IVA_FATTURA_ESTERA
--  1.2   Viene aggiornata la pratica di accantonamento al centro (accertamento su partita di giro tronca + generico di acc. + reversale provvisoria)
--        Il riferimento a tale pratica Ã¨ mantenuto nella tabella LIQUIDAZIONE_IVA_CENTRO dove per ogni mese e tipo di liquidazione esiste un record
--        con 2 stati: I -> Iniziale C -> Chiuso e il riferimento all'OBBLIGAZIONE su partita di giro corrispondente alla pratica di ACCANTONAMENTO IVA
--        al centro: in questa fase se il record esiste, viene utilizzato per recuperare la pratica e modificarla di DELTA corrispondente all'importo
--        di iva da versare dell'UO locale (valoreassoluto(iva_da_versare)) se non esiste viene creato in stato iniziale (I)
--  2.  Caso di unitÃ  organizzativa  liquidata via interfaccia, liquidazione propria
--  2.2   Viene aggiornata la pratica di accantonamento al centro (accertamento su partita di giro tronca + generico di acc. + reversale provvisoria)
--        La gestione del riferimento alla pratica di accantonamento al centro Ã¨ lo stesso del caso 1.2
--  3.  Caso di liquidazione del centro
--  3.1   Viene creata un'obbligazione su partita di giro tronca nell'UO di versamento IVA, generico di versamento e mandato di versamento
--        Il riferimento alla pratica di accantonamento al centro per il mese e tipo in processo, viene aggiornato con l'indicazione del
--        numero della liquidazione effettuata al centro che chiude la partita finanziaria IVA: lo stato di tale riferimento Ã¨ posto a chiusoÂ©
--        Se tale riferimento non Ã¨ trovato, il processo continua senza interruzioni di sorta (nessuna UO risultava a debito IVA nel periodo per
--        il tipo di IVA in processo)
--
-- Parametri:
-- aCdCds -> CdS che effettua il calcolo della liquidazione
-- aEs -> esercizio di riferimento
-- aCdUo -> unitÃ  organizzativa che effettua il calcolo della liquidazione
-- aDtDa -> data di inizio validitÃ 
-- aDtA -> data fine validitÃ 
-- aUser -> utente che effettua la modifica
-- aTipoLiquidazione -> tipo di liquidazione

   PROCEDURE contabilLiquidIVA
      (
       aCdCds VARCHAR2,
       aEs NUMBER,
       aCdUo VARCHAR2,
       aDtDa DATE,
       aDtA DATE,
       aUser VARCHAR2,
       aTipoLiquidazione VARCHAR2,
       inTipoImpegno VARCHAR2 Default Null
      );

-- Annulla liquidazione IVA

 procedure annullaLiquidIVA(aL liquidazione_iva%rowtype, aUtente varchar2);

-- Inserisce la liquidazione IVA centro
 procedure ins_LIQUIDAZIONE_IVA_CENTRO (aDest LIQUIDAZIONE_IVA_CENTRO%rowtype);
-- Controlla l'assunzione di Impegni, se Ã¨ presente una riga si Saldi non quadrata
-- sulla linea dell'IVA non Ã¨ possibile assumere obbligazioni
   PROCEDURE controllaAssunzioneImpegni
      (
       aCdCds VARCHAR2,
       aEs NUMBER
      );
   PROCEDURE controllaAssunzioneImpResImpro
      (
       aCdCds VARCHAR2,
       aEs NUMBER
      );
End;
