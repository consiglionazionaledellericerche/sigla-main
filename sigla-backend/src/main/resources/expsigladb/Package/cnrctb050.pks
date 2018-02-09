CREATE OR REPLACE PACKAGE         CNRCTB050 as
--
-- CNRCTB050 - Package per la gestione del PIANO DI GESTIONE CDR
-- Date: 07/06/2005
-- Version: 4.9
--
-- Dependency: CNRCTB 020 IBMERR 001
--
-- History:
--
-- Date: 01/09/2001
-- Version: 1.0
-- Creazione
--
-- Date: 26/09/2001
-- Version: 1.1
-- Fix errori
--
-- Date: 30/09/2001
-- Version: 1.2
-- Gestione del campo cd_cds in aggregato PDG spese per gestione spese di natura 5
--
-- Date: 03/10/2001
-- Version: 1.3
-- Fix errori e spostamento ribaltamento su aree a CNRCTB052
--
-- Date: 07/10/2001
-- Version: 1.4
-- Spostata in CNRTST050 la generazione dei dati di test
--
-- Date: 08/10/2001
-- Version: 1.5
-- Aggiunti metodi e costanti di utilizzo generale
--
-- Date: 16/10/2001
-- Version: 1.6
-- Aggiunta gestione tipo unità ENTE
--
-- Date: 30/10/2001
-- Version: 1.7
-- Aggiunta costanti
--
-- Date: 01/11/2001
-- Version: 1.8
-- Aggiunto check quadratura ricavi figurativi
--
-- Date: 08/11/2001
-- Version: 2.0
-- Eliminazione esercizio da STO
--
-- Date: 15/11/2001
-- Version: 2.1
-- Nuova gestione predisposizione della testata piani di gestione: eliminato inizializzaPdg
-- aggiunto apriPdg
--
-- Date: 19/11/2001
-- Version: 2.2
-- Fix errori
--
-- Date: 22/11/2001
-- Version: 2.3
-- Setup messaggi errore
--
-- Date: 23/11/2001
-- Version: 2.4
-- Fix errore TRAC INT 0240A
--
-- Date: 20/12/2001
-- Version: 2.5
-- Modifica generazione dell'aggregato iniziale: oprazione reiterabile, a copertura di tutte le combinazioni
-- dei parametri di aggregazione
--
-- Date: 04/01/2002
-- Version: 2.6
-- Modifica gestione apertura del pdg: apertura del pdg di tutti i CDR sotto un certo cds
--
-- Date: 17/01/2002
-- Version: 2.7
-- Fix errori
--
-- Date: 27/03/2002
-- Version: 2.8
-- Aggiornamento utuv/duva/pgverrec testata aggregato in creazione
--
-- Date: 27/03/2002
-- Version: 2.9
-- Richiesta n. R76 - l'aggregato ricreato non deve eccedere gli importi imposti dal centro sulla parte spese
--
-- Date: 28/03/2002
-- Version: 3.0
-- Miglioramento segnalazione discrepanze con centro in inizializzazione aggregato
--
-- Date: 28/03/2002
-- Version: 3.1
-- Fix errore
--
-- Date: 12/04/2002
-- Version: 3.2
-- Aggiunte costanti di stato del piano di gestione D ed E
--
-- Date: 18/04/2002
-- Version: 3.3
-- Introduzione funzione e natura su dettagli PDG
--
-- Date: 19/04/2002
-- Version: 3.4
-- Check di quadratura insieme linea di attività
--
-- Date: 23/04/2002
-- Version: 3.5
-- Fix su check di quadratura insieme linea di attività
--
-- Date: 24/04/2002
-- Version: 3.6
-- Modificata interfaccia di ritorno procedura -> function check quadratura insieme linea di attività
--
-- Date: 29/04/2002
-- Version: 3.7
-- Modifica inizializzazione aggregato -> aggiorna la colonna dei costi altrui sul modificato dal centro
-- ad ogni generazione con l'importo proveniente dai piani di gestione.
-- Tale importo non è contrattabile
--
-- Date: 17/06/2002
-- Version: 3.8
-- I dettyagli di natura 5 in CDR diverso da AREA possono essere anche dettagli di SCARICO (fix su DETTAGLI_PDG_NATURA_5)
--
-- Date: 18/06/2002
-- Version: 3.9
-- Aggiunto metodo di controllo di chiusura aggregato CDR di prim o resp. area
--
-- Date: 21/06/2002
-- Version: 4.0
-- Aggiunto il metodo di controllo di non esistenza dettagli non confermati o annullati
--
-- Date: 18/07/2002
-- Version: 4.1
-- Aggiornata documentazione
--
-- Date: 19/08/2002
-- Version: 4.2
-- Fix errore su aggregazione PDG
--
-- Date: 18/12/2002
-- Version: 4.3
-- Gestione variazione su piani di gestione
--
-- Date: 30/12/2002
-- Version: 4.4
-- Modificato curosore su dettagli di natura 5, tolta join inutile con linea_attivita e aggiunto controllo che il dettaglio
-- non sia già stato scaricato verso altro CDR (il cdr area appunto)
--
-- Date: 25/08/2003
-- Version: 4.5
-- Introdotto controllo di attualizzazione dello scarico verso altra UO imposto dal centro al CDR di primo livello Servito.
--
-- Date: 15/09/2003
-- Version: 4.6
-- Documentazione
--
-- Date: 23/09/2003
-- Version: 4.7
-- Introdotto i metodi di estrazione dello stato dell'aggregato corrispondente al CDR (RUO/NRUO) specificato
-- e del CDR aggregatore
--
-- Date: 07/06/2005
-- Version: 4.8
-- Introdotto il superamento dei controlli di stato quando approvo una variazione al PDG
--
-- Date: 20/10/2005
-- Version: 4.9
-- Creazione record su pdg_esercizio
--
-- Constants:
--
-- Tipi di dettaglio PDG
--
-- Dettaglio singolo
DETTAGLIO_SINGOLO CONSTANT VARCHAR2(5):='SIN';
-- Dettaglio di scarico
DETTAGLIO_SCARICO CONSTANT VARCHAR2(5):='SCR';
-- Dettaglio di carico
DETTAGLIO_CARICO CONSTANT VARCHAR2(5):='CAR';

-- Origine dei dettagli PDG
--
-- Origine diretta imputazione utente
ORIGINE_DIRETTA CONSTANT VARCHAR2(5):='DIR';
-- Origine scarico dati stipendiali
ORIGINE_STIPENDI CONSTANT VARCHAR2(5):='STI';

-- Stati del dettaglio pdg

STATO_DETT_INDEF CONSTANT CHAR(1):='X';
STATO_DETT_CONFER CONSTANT CHAR(1):='Y';
STATO_DETT_ANNULL CONSTANT CHAR(1):='N';

-- Stati del piano di gestione
--
STATO_PDG_INIZIALE CONSTANT VARCHAR2(5):='A';
STATO_PDG_PRE_CHIUSURA CONSTANT VARCHAR2(5):='B';
STATO_PDG_CHIUSURA CONSTANT VARCHAR2(5):='C';

STATO_PDG_RC VARCHAR2(5):='E';
STATO_PDG_RC_PRE_CHIUSURA VARCHAR2(5):='D';

STATO_PDG_FINALE CONSTANT VARCHAR2(5):='F';

STATO_AGGREGATO_INIZIALE CONSTANT VARCHAR2(5):='A';
STATO_AGGREGATO_FINALE CONSTANT VARCHAR2(5):='B';

-- Stati del piano di gestione dal 2006
--
STATO_PDG2_INIZIALE     CONSTANT VARCHAR2(5) := 'AC';
STATO_PDG2_APERTO_GEST Constant VARCHAR2(5) := 'AG';
STATO_PDG2_CHIUSO_GEST Constant VARCHAR2(5) := 'CG';

-- Identificatore di dato aggregato in tabelle PDG_AGGREGATO_XXX_DET
TI_AGGREGATO_INIZIALE CONSTANT CHAR(1) := 'I';
TI_AGGREGATO_MODIFICATO CONSTANT CHAR(1) := 'M';

--
-- Cursore di lettura dei dettagli del PDG di natura 5 non ancora ribaltati (utilizzato in ribaltamento per variazioni PDG)
-- aEs -> esercizio contabile
-- aCdCdr -> cd centro responsabilità
--
cursor DETTAGLI_PDG_NATURA_5(aEs number, aCdCdr varchar2) RETURN PDG_PREVENTIVO_SPE_DET%ROWTYPE is (
                select * from
                PDG_PREVENTIVO_SPE_DET
		where stato = 'Y'
		  and esercizio = aEs
		  and cd_centro_responsabilita = aCdCdr
		  and categoria_dettaglio in (DETTAGLIO_SINGOLO, DETTAGLIO_SCARICO)
		  and cd_natura = 5
		  and cd_centro_responsabilita_clge is null -- Non ancora scaricati verso altro CDR (il cdr area)
		 ) for update nowait;


-- NUOVA GESTIONE !!!! Cursore di lettura dei dettagli del PDG Gestionale ENTRATA da ribaltare sull'Area
-- aEs -> esercizio contabile
-- aCdCdr -> cd centro responsabilità

cursor PDGG_ETR_DA_RIB_SU_AREA(aEs number, aCdCdr varchar2) RETURN PDG_MODULO_ENTRATE_GEST%ROWTYPE Is
               (
                select * from
                PDG_MODULO_ENTRATE_GEST
		where esercizio = aEs
		  And CD_CENTRO_RESPONSABILITA = aCdCdr
		  and CNRUTL001.getCdsFromCdr(CD_CDR_ASSEGNATARIO) != cd_cds_area
                  And origine = 'PRE'
                  And CATEGORIA_DETTAGLIO = 'DIR'
		 ) for update nowait;

-- NUOVA GESTIONE !!!! Cursore di lettura dei dettagli del PDG Gestionale SPESA da ribaltare sull'Area
-- aEs -> esercizio contabile
-- aCdCdr -> cd centro responsabilità

cursor PDGG_SPE_DA_RIB_SU_AREA(aEs number, aCdCdr varchar2) RETURN PDG_MODULO_SPESE_GEST%ROWTYPE is (
                select * from
                PDG_MODULO_SPESE_GEST
		where esercizio = aEs
		  And CD_CENTRO_RESPONSABILITA = aCdCdr
		  and CNRUTL001.getCdsFromCdr(CD_CDR_ASSEGNATARIO) != cd_cds_area
                  And origine = 'PRE'
                  And CATEGORIA_DETTAGLIO = 'DIR'
		 ) for update nowait;

Cursor DETTAGLI_PDG_NATURA_5_VAR(aEs number, aCdCdr VARCHAR2, aEsVar NUMBER, numVar NUMBER) RETURN PDG_PREVENTIVO_SPE_DET%ROWTYPE is (
                select * from
                         PDG_PREVENTIVO_SPE_DET
				where
				     stato = 'Y'
				 and esercizio = aEs
				 and cd_centro_responsabilita = aCdCdr
				 and categoria_dettaglio in (DETTAGLIO_SINGOLO, DETTAGLIO_SCARICO)
				 and cd_natura = 5
				 and cd_centro_responsabilita_clge is Null -- Non ancora scaricati verso altro CDR (il cdr area)
				 And esercizio_pdg_variazione = aEsVar
				 And pg_variazione_pdg = numVar
				) for update nowait;

-- Functions e Procedures:

-- Inserisce una riga nella tabella PDG_PREVENTIVO

 procedure ins_PDG_PREVENTIVO (aDest PDG_PREVENTIVO%rowtype);

-- Inserisce una riga nella tabella PDG_ESERCIZIO

 procedure ins_PDG_ESERCIZIO (aDest PDG_ESERCIZIO%rowtype);

-- Apertura piani di gestione
--
-- Legge tutti i cdr validi in aEs (cioe' anno esercizio) sotto il CDS aCdCds (cioe' codice CDS) e genera in PDG_PREVENTIVO la testata del PDG
-- Gestisce senza segnalazione utente eventuali errori di chiave duplicata
--
-- pre-post-name: Apertura piano di gestione per CDS
-- pre:  Il CDS specificato non è l'ENTE
--             Esiste nel sistema un CDS ENTE valido nell'esercizio scelto
-- post: Per ogni CDR del CDS specificato viene creata la testata dei piani di gestione utilizzando l'utente specificato come utenza di creazione
--
-- pre-post-name: CDS Ente non trovato
-- pre:  Non esiste nel sistema un CDS ENTE valido nell'esercizio scelto
-- post: Viene sollevata un'eccezione applicativa
--
-- aEs -> esercizio di scrivania
-- aCdCds -> codice del CDS di cui si vogliono aprire i PDG
-- aUser -> utenza che effettua l'operazione

 procedure apriPDG(aEs number, aCdCds varchar2, aUser varchar2);

-- Crea l'aggregato iniziale del piano di gestione del CDR specificato nell'esercizio specificato targando i record creati con l'utente specificato
-- I dati dell'aggregato sono letti mediante le viste V_DPDG_AGGREGATO_SPE_DET e V_DPDG_AGGREGATO_ETR_DET che restituiscono gli aggregati al primo livello di CDR epurati dei ricavi figurativi.
-- Scatenato applicativamente con la precondizione che lo stato del PDG passi da Qualsiasi Stato X -> C e che il CDR sia di primo livello (o area)
-- l'operazione viene condotta secondo le seguenti modalità:
--
-- pre-post-name: Creazione primo aggregato
-- pre: il CDR specificato è un CDR valido nell'esercizio specificato Viene richiesta la creazione del primo aggregato.
-- post: Viene creata la testata dell'aggregato in stato iniziale, quindi vengono creati i dettagli dell'aggregato secondo le seguenti modalità:
-- *per la parte spese viene vengono inserite nella tabella degli aggregati tutte le possibili combinazioni dei parametri di aggregazione.
-- **per MACROISTITUTI e AREE: titoli cds x funzioni x nature x cds (1+m dove m è il numero di aree di ricerca di cui il CDS di appartenenza del CDR di primo livello è Presidente)
-- **per il SAC: voci del piano di spesa CDS x funzioni x nature x cds (sempre 1 = CDS SAC) I titoli (o capitoli) sono tutti e solo quelli compatibili con le funzioni e il tipo di cds impattato (tabella incroci voce del piano di spesa - funzione - tipo cds)
--
-- *per la parte entrate vengono inserite nella tabella degli aggregati tutte le possibili combinazioni dei parametri di aggregazione:
-- nature + capitoli di entrata CNR ()
-- I capitoli sono tutti e soli quelli associati alla natura secondo l'associazione entrata cnr -> natura
-- I record creati sono sdoppiati in chiave mediante un identificatore di tipo:
--  tipo aggregato = Iniziale = Dati aggregati provenienti dai piani di gestione secondo i criteri stabiliti sopra
--  tipo aggregato = Modificato = Inizialmente uguali ai dati aggregati Iniziali
--
-- pre-post-name: Creazione n-esimo aggregato con n>1
-- pre: il CDR specificato è un CDR valido nell'esercizio specificato. Viene richiesta la creazione del' n-esimo aggregato (n>1).
-- post: I dettagli di tipo Iniziale creati alla prima aggregazione vengono totalmente eliminati e sostituiti con quelli calcolati aggregando i dati correnti dai piani di gestione scondo le regole stabilite nella pre-post "Creazione primo aggregato".
-- Vengono aggiornate le informazioni di sistema della testata dell'aggregato
--
-- pre-post-name: CDR non valido nell'esercizio specificato
-- pre: il CDR specificato non è un CDR valido nell'esercizio specificato
-- post: Viene sollevata un'eccezione applicativa
--
-- Parametri:
-- aEs -> esercizio
-- aCdCdr -> Centro di responsabilità su cui viene effettuata l'operazione
-- aUser -> Utente che effettua l'operazione
--
procedure inizializzaAggregatoPDG(aEs number, aCdCdr varchar2, aUser varchar2);

procedure inizializzaAggregatoPDG(aEs number, aCdCdr varchar2, aUser VARCHAR2, daVariazione CHAR);

-- Verifica che il piano di gestione aggregato del cdr di primo livello o resp. di area a cui il CDR specificato afferisce sia chiuso in stato B
--
-- pre-post-name: Il Piano di gestione aggregato del CDR di primo livello o resp. di AREA a cui il CDR specificato afferisce non è chiuso
-- pre:  Il Piano di gestione aggregato del CDR di primo livello o AREA a cui il CDR specificato afferisce non è chiuso o non esiste
-- post: Ritorna 'N'
--
-- pre-post-name: Nessuna altra pre condizione verificata
-- pre: Nessun'altra precondizione è verificata
-- post: Ritorna 'Y'
--
-- Parametri:
-- aEs -> esercizio
-- aCdCdr -> Centro di responsabilità su cui effettuare il controllo

 function checkAggregatoChiuso(aEs number, aCdCdr varchar2) return char;

-- Verifica che il piano di gestione aggregato del cdr di primo livello o resp. di area a cui il CDR specificato afferisce sia chiuso in stato B
--
-- pre-post-name: Il Piano di gestione aggregato del CDR di primo livello o resp. di AREA a cui il CDR specificato afferisce non è chiuso
-- pre:  Il Piano di gestione aggregato del CDR di primo livello o AREA a cui il CDR specificato afferisce non è chiuso o non esiste
-- post: Ritorna 'N'
--
-- pre-post-name: Nessuna altra pre condizione verificata
-- pre: Nessun'altra precondizione è verificata
-- post: Ritorna 'Y'
--
-- Parametri:
-- aEs -> esercizio
-- aCdCdr -> Centro di responsabilità su cui effettuare il controllo

 function checkStatoAggregato(aEs number, aCdCdr varchar2,stato varchar2) return char;

-- Controlla che non ci siano dettagli non confermati nel pdg
--
-- pre-post-name: Esistono dettagli non confermati o annullati
-- pre: Esiste un spesa o una entrata caricata/scaricata da/verso altra UO o CDR in stato indefinito (X)
-- post: Viene ritornato 'N'
--
-- pre-post-name: Non esistono dettagli nel PDG in stato non contrattato
-- pre: Tutti i detttagli di spesa o entrata sono in stato di confermato o annullato
-- post: Viene ritornato 'Y'
--
-- Parametri
--   aEs -> esercizio contabile
--   aCdCdr -> codice del cdr su cui effetuare il controllo

 function checkDettScarConfermati (aEs number, aCdCdr varchar2) return char;

 -- Legge lockandola la riga di pdg del cdr aCDR: se non trova la riga, solleva un'eccezione
 --
 -- aEs -> esercizio
 -- aCdCdr -> codice del centro di responsabilità

 procedure lockPdg(aEs number, aCdCdr varchar2);

-- Nuova gestione PdG Gestionale
 procedure lockpdg_esercizio(aEs number, aCdCdr varchar2);

 -- Ritorna lo stato del piano del CDR aCDR
 --
 -- aEs -> esercizio
 -- aCdCdr -> codice del centro di responsabilità

 function getStato(aEs number, aCdCdr varchar2) return varchar2;

 -- Ritorna lo stato del piano del CDR aCDR (DA PDG_ESERCIZIO, NUOVA GESTIONE 2006)
 --
 -- aEs -> esercizio
 -- aCdCdr -> codice del centro di responsabilità


 Function getStato_PDG_ESERCIZIO(aEs number, aCdCdr varchar2) return varchar2;

 -- Reset dei campi importo in rowtype passato

 procedure resetCampiImporto(aDett in out pdg_preventivo_spe_det%rowtype);
 procedure resetCampiImporto(aDett in out pdg_preventivo_etr_det%rowtype);

-- Verifica che esista la quadratura dei ricavi figurativi tra spese ed entrate in PDG CDR servente.
-- Utilizza la vista V_DPDG_TOT_BIL_RICFIGCDR che aggrega i dettagli di entrata ricavi figurativi di carico del centro servente e quelli di spesa corrispondenti
-- attraverso l'insieme definito sulla linea di attivita del ricavo figurativo e le linee di spesa destinate alla ripartizione del ricavo figurativo in spesa.
-- Gli importi di entrata sono le colonne B per il 1 anno, D per il 2 anno, F per il 3 anno delle entrate PDG
-- Gli importi di spesa sono la somma di I+J+K+L+Q+R+S+T per il 1 anno, AC+AD+AE+AF per il 2 anno e AL+AM+AN+AO per il 3 anno
--
-- pre-post-name: Esiste quadratura dei ricafi figurativi nel pdg del cdr specificato nell'esercizio specificato
-- pre:  Esiste quadratura tra spese ed entrate collegate attraverso l'insieme di linea di attività specificata in dettaglio del pdg.
--            Il CDR è un CDR esistente e valido nell'esercizio
-- post: Ritorna 'Y'
--
-- pre-post-name: Non esiste quadratura dei ricavi figurativi
-- pre: Non esiste quadratura dei ricavi figurativi
--            Il CDR è un CDR esistente e valido nell'esercizio
-- post: Ritorna 'N'
--
-- pre-post-name: Nessuna altra pre condizione verificata
-- pre: Nessun'altra precondizione è verificata
-- post: Ritorna 'Y'
--
-- Parametri:
-- aEs -> esercizio
-- aCdCdr -> Centro di responsabilità su cui effettuare il controllo

 function checkQuadRicFig(aEsercizio number, aCdCdr  varchar2) return char;

-- Inserisce una riga nella tabella PDG_PREVENTIVO_SPE_DET

 procedure ins_PDG_PREVENTIVO_SPE_DET (aDest PDG_PREVENTIVO_SPE_DET%rowtype);

 -- Inserisce una riga nella tabella PDG_PREVENTIVO_ETR_DET

 procedure ins_PDG_PREVENTIVO_ETR_DET (aDest PDG_PREVENTIVO_ETR_DET%rowtype);

-- Inserisce la testata dell'aggregato

 procedure ins_PDG_AGGREGATO (aDest PDG_AGGREGATO%rowtype);

 -- Inserisce una riga nella tabella PDG_PREVENTIVO_AGGREGATO_SPE_DET

 procedure ins_PDG_AGGREGATO_SPE_DET (aDest PDG_AGGREGATO_SPE_DET%rowtype);

 -- Inserisce una riga nella tabella PDG_PREVENTIVO_AGGREGATO_ETR_DET

 procedure ins_PDG_AGGREGATO_ETR_DET (aDest PDG_AGGREGATO_ETR_DET%rowtype);

 -- Inserisce una riga nella tabella ASS_PDG_VARIAZIONE_CDR

 procedure ins_ASS_PDG_VARIAZIONE_CDR (aDest ASS_PDG_VARIAZIONE_CDR%rowtype);

-- Controlla che non ci siano discrepanze tra entrate e spese legate all'insieme di linee di attività
--
-- pre-post-name: Esistono discrepanze su insieme di linea di attività
-- pre: Esiste almeno un insieme per natura 1,2,3 tale che i dettagli di spesa non quadrano con quelli di entrata collegati secondo  il seguente schema
-- Anno 1/Spese
--    IM_RH_CCS_COSTI
-- + IM_RQ_SSC_COSTI_ODC
-- + IM_RR_SSC_COSTI_ODC_ALTRA_UO
-- + IM_RS_SSC_COSTI_OGC
-- + IM_RT_SSC_COSTI_OGC_ALTRA_UO
-- + IM_RP_CSS_VERSO_ALTRO_CDR
-- Anno 2/Spese
-- IM_RAB_A2_COSTI_ALTRO_CDR
-- + IM_RAC_A2_SPESE_ODC
-- + IM_RAD_A2_SPESE_ODC_ALTRA_UO
-- + IM_RAE_A2_SPESE_OGC
-- + IM_RAF_A2_SPESE_OGC_ALTRA_UO
-- Anno 3/Spese
--    IM_RAI_A3_COSTI_ALTRO_CDR
-- + IM_RAL_A3_SPESE_ODC
-- + IM_RAM_A3_SPESE_ODC_ALTRA_UO
-- + IM_RAN_A3_SPESE_OGC
-- + IM_RAO_A3_SPESE_OGC_ALTRA_UO
--
-- Anno 1/Entrate
-- a.IM_RA_RCE
-- + a.IM_RC_ESR
-- Anno 2/Entrate
-- a.IM_RE_A2_ENTRATE
-- Anno 3/Entrate
-- a.IM_RG_A3_ENTRATE
--
-- post: Viene ritornato 'N'
--
--
-- pre-post-name: Esiste quadratura su insieme
-- pre: Non ci sono insiemi con squadrature
-- post: Viene ritornato 'Y'
--
-- Parametri
--   aEs -> esercizio contabile
--   aCdCdr -> codice del cdr su cui effetuare il controllo

 function check_discrepanze_insieme_la (aEs number, aCdCdr varchar2) return char;

-- Controlla che non ci siano importi relativi ad altra UO non completamente attualizzati in spese scaricate su
-- UO servente
-- Tale controllo risulta necessario per impedire la generazione di un BILANCIO FINANZIARIO dell'ENTE che non
-- contiene le spese per costi altrui attualizzate dalla creazione di dettagli successiva all'approvazione del bilancio
-- finanziario.
--
-- Pre-post-name: Aggregato non esistente per CDR specificato
-- pre: Il cdr specificato non ha prodotto l'aggregato
-- post: Viene sollevata un'eccezione
--
-- Pre-post-name: Controllo di attualizzazione
-- pre:
--      Per ogni dato di aggregazione del cdr di primo livello relativo al centro vale:
--		        IM_RJ_CCS_SPESE_ODC_ALTRA_UO<>aPdgAgg.IM_RJ_CCS_SPESE_ODC_ALTRA_UO
--           OR IM_RL_CCS_SPESE_OGC_ALTRA_UO<>aPdgAgg.IM_RL_CCS_SPESE_OGC_ALTRA_UO
--           OR IM_RR_SSC_COSTI_ODC_ALTRA_UO<>aPdgAgg.IM_RR_SSC_COSTI_ODC_ALTRA_UO
--           OR IM_RT_SSC_COSTI_OGC_ALTRA_UO<>aPdgAgg.IM_RT_SSC_COSTI_OGC_ALTRA_UO
--           OR IM_RAD_A2_SPESE_ODC_ALTRA_UO<>aPdgAgg.IM_RAD_A2_SPESE_ODC_ALTRA_UO
--           OR IM_RAF_A2_SPESE_OGC_ALTRA_UO<>aPdgAgg.IM_RAF_A2_SPESE_OGC_ALTRA_UO
--           OR IM_RAM_A3_SPESE_ODC_ALTRA_UO<>aPdgAgg.IM_RAM_A3_SPESE_ODC_ALTRA_UO
--           OR IM_RAO_A3_SPESE_OGC_ALTRA_UO<>aPdgAgg.IM_RAO_A3_SPESE_OGC_ALTRA_UO
--       dove aPdgAgg è la riga di aggregato derivante dai piani di gestione relativa alla riga di aggregato
--       del centro in processo.
-- post: Viene sollevata un'eccezione
--
-- Parametri
--   aEs -> esercizio contabile
--   aCdCdrPrimo -> codice del cdr di primo livello su cui effetuare il controllo

 procedure checkAttualizzScrAltraUo(aEs number, aCdCdrPrimo varchar2);

-- Estrazione stato aggregato corrispondente a PDG del CDR specificato

-- Pre-post-name: Aggregato non esistente relativo al CDR specificato
-- pre: L'aggregato corrispondente al PDG del CDR specificato non è stato ancora creato
-- post: Viene ritornato null
--

-- Pre-post-name: Estrazione stato aggregato corrispondente al PDG del CDR specificato
-- pre: E' richiesto lo stato dell'aggregato relativo al PDG del CDR specificato. Nessuna altra precondizione verificata.
-- post: Viene ritornato lo stato
--
-- Parametri
--   aEs -> esercizio contabile
--   aCdCdr -> codice del cdr di cui recuperare lo stato dell'aggregato corrispondente

 function getStatoAggregato(aEs number, aCdCdr varchar2) return char;

-- Estrazione stato aggregato corrispondente a PDG del CDR specificato

-- Pre-post-name: Cdr non valido per esercizio specificato
-- pre: Il cdr non è valido nell'esercizio specificato
-- post: Viene sollevata un'eccezione
--

-- Pre-post-name: Estrazione del cdr aggregatore
-- pre: E' richiesta l'estrazione del codice del cdr aggregatore (cdr di afferenza  codice proprio per I livelli o CDR area). Nessuna altra precondizione verificata.
-- post: Viene ritornato il codice del CDR
--
-- Parametri
--   aEs -> esercizio contabile
--   aCdCdr -> codice del cdr di cui recuperare il cdr aggregatore

 function getCdrAggregatore(aEs number, aCdCdr varchar2) return varchar2;

end;
/


CREATE OR REPLACE PACKAGE BODY         CNRCTB050 is

 function getStatoAggregato(aEs number, aCdCdr varchar2) return char is
  aStatoAgg char;
 begin
  begin
   select stato into aStatoAgg from pdg_aggregato where
           esercizio = aEs
	   and cd_centro_responsabilita = getCdrAggregatore(aEs, aCdCdr)
	   for update nowait;
   return aStatoAgg;
  exception when NO_DATA_FOUND then
   return null;
  end;
 end;

 function getCdrAggregatore(aEs number, aCdCdr varchar2) return varchar2 is
  aCDR cdr%rowtype;
  aUO unita_organizzativa%rowtype;
  aCdCdrAgg varchar2(30);
 begin
  aCDR:=CNRCTB020.GETCDRVALIDO(aEs,aCdCdr);
  aUO:=CNRCTB020.GETUOVALIDA(aEs,aCDR.cd_unita_organizzativa);
  if aCDR.livello=1 or (aCDR.livello=2 and aUO.cd_tipo_unita = CNRCTB020.TIPO_AREA) then
   aCdCdrAgg:=aCDR.cd_centro_responsabilita;
  else
   aCdCdrAgg:=aCDR.cd_cdr_afferenza;
  end if;
  return aCdCdrAgg;
 end;


 function checkDettScarConfermati (aEs number, aCdCdr varchar2) return char is
   n_discr integer;
 begin
      n_discr:=0;
 	 SELECT COUNT(*) INTO N_DISCR FROM pdg_preventivo_spe_det
 	 WHERE
     	   esercizio = aEs
 	   and cd_centro_responsabilita = aCdCdr
 	   and categoria_dettaglio in ('CAR','SCR')
 	   and stato = STATO_DETT_INDEF;
 	 IF N_DISCR>0 THEN
 	 	return 'N';
 	 END IF;
      n_discr:=0;
 	 SELECT COUNT(*) INTO N_DISCR FROM pdg_preventivo_etr_det
 	 WHERE
 	       esercizio = aEs
 	   and cd_centro_responsabilita = aCdCdr
 	   and categoria_dettaglio in ('CAR','SCR')
 	   and stato = STATO_DETT_INDEF;
 	 IF N_DISCR>0 THEN
 	 	return 'N';
 	 END IF;
 	 return 'Y';
 end;


 function check_discrepanze_insieme_la (aEs number, aCdCdr varchar2) return char is
  n_discr integer;
 begin
	 SELECT COUNT(*) INTO N_DISCR FROM V_DISCREPANZE_INSIEME
	 WHERE V_DISCREPANZE_INSIEME.CD_CENTRO_RESPONSABILITA = aCdCdr
	 AND V_DISCREPANZE_INSIEME.ESERCIZIO = aEs
	 AND ((V_DISCREPANZE_INSIEME.IM_1_ETR - V_DISCREPANZE_INSIEME.IM_1_SPE)<>0 OR
	 	 (V_DISCREPANZE_INSIEME.IM_2_ETR - V_DISCREPANZE_INSIEME.IM_2_SPE)<>0 OR
		 (V_DISCREPANZE_INSIEME.IM_3_ETR - V_DISCREPANZE_INSIEME.IM_3_SPE)<>0);
	 IF N_DISCR>0 THEN
	 	return 'Y';
	 END IF;
	 return 'N';
 end;

 function getStato(aEs number, aCdCdr varchar2) return varchar2 is
  aStato varchar2(5);
 begin
  select stato into aStato from pdg_preventivo where
       esercizio = aEs
   and cd_centro_responsabilita = aCdCdr;
  return aStato;
 end;

 function getStato_PDG_ESERCIZIO(aEs number, aCdCdr varchar2) return varchar2 is
  aStato varchar2(5);
 begin
  select stato into aStato from pdg_ESERCIZIO where
       esercizio = aEs
   and cd_centro_responsabilita = aCdCdr;
  return aStato;
 end;

 procedure lockPdg(aEs number, aCdCdr varchar2) is
  aTemp varchar2(20);
 begin
  begin
   select utuv into aTemp from pdg_preventivo where
        esercizio = aEs
    and cd_centro_responsabilita = aCdCdr
   for update nowait;
  exception when no_data_found then
   IBMERR001.RAISE_ERR_GENERICO('Piano di gestione del CDR '||Nvl(aCdCdr, 'NULLO2')||' non ancora aperto!');
  end;
 end;

-- nuova gestione pdg gestionale

 procedure lockPdg_esercizio(aEs number, aCdCdr varchar2) is
  aTemp varchar2(20);
 begin
  begin
   select utuv into aTemp from pdg_esercizio where
        esercizio = aEs
    and cd_centro_responsabilita = aCdCdr
   for update nowait;
  exception when no_data_found then
   IBMERR001.RAISE_ERR_GENERICO('Piano di gestione del CDR '||Nvl(aCdCdr, 'NULLO')||' non ancora aperto ! (Esercizio)');
  end;
 end;

 function checkAggregatoChiuso(aEs number, aCdCdr varchar2) return char is
  aCDR cdr%rowtype;
  aCdCDRAggregato varchar2(30);
  aStato varchar(5);
 begin
  aCDR:=CNRCTB020.GETCDRVALIDO(aEs, aCdCdr);
  if aCDR.cd_cdr_afferenza is null then
   aCdCDRAggregato:=aCDR.cd_centro_responsabilita;
  else
   aCdCDRAggregato:=aCDR.cd_cdr_afferenza;
  end if;
  begin
   select stato into aStato from pdg_aggregato where
        esercizio = aEs
    and cd_centro_responsabilita = aCdCDRAggregato
   for update nowait;
  exception when NO_DATA_FOUND then
   return 'N';
  end;
  if aStato = STATO_AGGREGATO_FINALE then
   return 'Y';
  end if;
  return 'N';
 end;

 function checkStatoAggregato(aEs number, aCdCdr varchar2,stato varchar2) return char is
  aCDR cdr%rowtype;
  aCdCDRAggregato varchar2(30);
  aStato varchar(5);
 begin
  aCDR:=CNRCTB020.GETCDRVALIDO(aEs, aCdCdr);
  if aCDR.cd_cdr_afferenza is null then
   aCdCDRAggregato:=aCDR.cd_centro_responsabilita;
  else
   aCdCDRAggregato:=aCDR.cd_cdr_afferenza;
  end if;
  begin
   select stato into aStato from pdg_aggregato where
        esercizio = aEs
    and cd_centro_responsabilita = aCdCDRAggregato
   for update nowait;
  exception when NO_DATA_FOUND then
   return 'N';
  end;
  if aStato = stato then
   return 'Y';
  end if;
  return 'N';
 end;

 procedure checkAttualizzScrAltraUo(aEs number, aCdCdrPrimo varchar2) is
  aNum number;
  aPdgAggregato pdg_aggregato%rowtype;
 begin
  begin
   -- Lettura lockante della testata del PDG AGGREGATO
   select * into aPdgAggregato from  pdg_aggregato where
        esercizio = aEs
	and cd_centro_responsabilita = aCdCdrPrimo
   for update nowait;
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('Aggregato dei piani di gestione non trovato per cdr:'||aCdCdrPrimo||' in esercizio '||aEs);
  end;
  for aPdgAgg in (select * from pdg_aggregato_spe_det where
              ESERCIZIO=aEs
          AND CD_CENTRO_RESPONSABILITA=aCdCdrPrimo
          AND TI_AGGREGATO=TI_AGGREGATO_MODIFICATO) loop
   aNum:=0;
   select count(*) into aNum from pdg_aggregato_spe_det where
              ESERCIZIO=aPdgAgg.esercizio
          AND CD_CENTRO_RESPONSABILITA=aPdgAgg.cd_centro_responsabilita
          AND TI_APPARTENENZA=aPdgAgg.TI_APPARTENENZA
          AND TI_GESTIONE=aPdgAgg.TI_GESTIONE
          AND CD_ELEMENTO_VOCE=aPdgAgg.CD_ELEMENTO_VOCE
          AND CD_FUNZIONE=aPdgAgg.CD_FUNZIONE
          AND CD_NATURA=aPdgAgg.CD_NATURA
          AND CD_CDS=aPdgAgg.CD_CDS
          AND TI_AGGREGATO=TI_AGGREGATO_INIZIALE
		  AND (
		      IM_RJ_CCS_SPESE_ODC_ALTRA_UO<>aPdgAgg.IM_RJ_CCS_SPESE_ODC_ALTRA_UO
           OR IM_RL_CCS_SPESE_OGC_ALTRA_UO<>aPdgAgg.IM_RL_CCS_SPESE_OGC_ALTRA_UO
           OR IM_RR_SSC_COSTI_ODC_ALTRA_UO<>aPdgAgg.IM_RR_SSC_COSTI_ODC_ALTRA_UO
           OR IM_RT_SSC_COSTI_OGC_ALTRA_UO<>aPdgAgg.IM_RT_SSC_COSTI_OGC_ALTRA_UO
           OR IM_RAD_A2_SPESE_ODC_ALTRA_UO<>aPdgAgg.IM_RAD_A2_SPESE_ODC_ALTRA_UO
           OR IM_RAF_A2_SPESE_OGC_ALTRA_UO<>aPdgAgg.IM_RAF_A2_SPESE_OGC_ALTRA_UO
           OR IM_RAM_A3_SPESE_ODC_ALTRA_UO<>aPdgAgg.IM_RAM_A3_SPESE_ODC_ALTRA_UO
           OR IM_RAO_A3_SPESE_OGC_ALTRA_UO<>aPdgAgg.IM_RAO_A3_SPESE_OGC_ALTRA_UO
		  );
    if aNum > 0 then
     IBMERR001.RAISE_ERR_GENERICO('Esistono importi di scarico verso altra UO non attualizzati in spese nei piani di gestione del cdr di primo livello in processo:'||aCdCdrPrimo||' in esercizio '||aEs);
	end if;
   end loop;
 end;

 procedure inizializzaAggregatoPDG(aEs number, aCdCdr varchar2, aUser varchar2) is
 Begin
   inizializzaAggregatoPDG(aEs, aCdCdr, aUser , 'N');
 End;
 --
 procedure inizializzaAggregatoPDG(aEs number, aCdCdr varchar2, aUser VARCHAR2, daVariazione CHAR) is
  aCDR cdr%rowtype;
  aPdgAggregato pdg_aggregato%rowtype;
  aPdgAggregatoSpe pdg_aggregato_spe_det%rowtype;
  aPdgAggregatoEtr pdg_aggregato_etr_det%rowtype;
  aTSNow date;
  isAggregatoGiaCreato boolean;
  aDetAggregatoModificato pdg_aggregato_spe_det%rowtype;
  aPdg pdg_preventivo%rowtype;
  err_col VARCHAR2(3000);
 begin
  aTSNow:=sysdate;

  aCDR:=CNRCTB020.GETCDRVALIDO(aEs, aCdCdr);

  -- Se si tratta di CDR ente non viene eseguita alcuna operazione
  if CNRCTB020.ISCDRENTE(aCDR) then
   return;
  end if;

  -- Inserisco la testata dell'aggregato in stato INIZIALE se non già presente

  isAggregatoGiaCreato:=FALSE;
  begin
   -- Lettura lockante della testata del PDG AGGREGATO
   select * into aPdgAggregato from  pdg_aggregato where
        esercizio = aEs
	and cd_centro_responsabilita = aCDR.cd_centro_responsabilita
   for update nowait;
   isAggregatoGiaCreato:=TRUE;
  exception when NO_DATA_FOUND then
   aPdgAggregato.ESERCIZIO:=aEs;
   aPdgAggregato.CD_CENTRO_RESPONSABILITA:=aCDR.cd_centro_responsabilita;
   aPdgAggregato.STATO:=STATO_AGGREGATO_INIZIALE;
   aPdgAggregato.UTCR:=aUser;
   aPdgAggregato.DACR:=aTSNow;
   aPdgAggregato.UTUV:=aUser;
   aPdgAggregato.DUVA:=aTSNow;
   aPdgAggregato.PG_VER_REC:=1;
   ins_PDG_AGGREGATO (aPdgAggregato);
  end;

  begin
   select * into aPdg from pdg_preventivo where
        esercizio = aEs
    and cd_centro_responsabilita = aCDR.cd_centro_responsabilita
    for update nowait;
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('Piano di gestione non trovato per cdr:'||aCDR.cd_centro_responsabilita);
  end;

  -- Annullo una eventuale operazione precedente eliminando tutti i dettagli di aggregazione

  delete from pdg_aggregato_spe_det where
       esercizio = aEs
   and cd_centro_responsabilita = aCdCdr
   and ti_aggregato = TI_AGGREGATO_INIZIALE;

  delete from pdg_aggregato_etr_det where
       esercizio = aEs
   and cd_centro_responsabilita = aCdCdr
   and ti_aggregato = TI_AGGREGATO_INIZIALE;
  /* In caso sto approvando una variazione definitiva aggiorno anche il Modificato */
  if daVariazione = 'Y' Then
    delete from pdg_aggregato_spe_det where
         esercizio = aEs
     and cd_centro_responsabilita = aCdCdr
     and ti_aggregato = TI_AGGREGATO_MODIFICATO;

    delete from pdg_aggregato_etr_det where
         esercizio = aEs
     and cd_centro_responsabilita = aCdCdr
     and ti_aggregato = TI_AGGREGATO_MODIFICATO;
  End If;
  -- Inserisco l'aggregato parte spese ed entrate

  for aVPdgAggregato in (select * from v_dpdg_aggregato_spe_det_spn where
       esercizio = aEs
   and cd_centro_responsabilita = aCDR.cd_centro_responsabilita) loop
     if         isAggregatoGiaCreato
	    -- Il controllo viene effettuato solo se non sono oltre lo stato B (riaperture per proposte di variazione)
	    and (not (aPdg.stato in (CNRCTB070.STATO_PDG_PRECHIUSURA_PER_VAR, CNRCTB070.STATO_PDG_APERTURA_PER_VAR))
	         And DaVariazione = 'N')
	 then
      begin
       select * into aDetAggregatoModificato from pdg_aggregato_spe_det where
              ESERCIZIO=aVPdgAggregato.ESERCIZIO
          AND CD_CENTRO_RESPONSABILITA=aVPdgAggregato.CD_CENTRO_RESPONSABILITA
          AND CD_CDS=aVPdgAggregato.CD_CDS
          AND TI_APPARTENENZA=aVPdgAggregato.TI_APPARTENENZA
          AND TI_GESTIONE=aVPdgAggregato.TI_GESTIONE
          AND CD_ELEMENTO_VOCE=aVPdgAggregato.CD_ELEMENTO_VOCE
          AND CD_FUNZIONE=aVPdgAggregato.CD_FUNZIONE
          AND CD_NATURA=aVPdgAggregato.CD_NATURA
          AND TI_AGGREGATO=TI_AGGREGATO_MODIFICATO;
       if
        aDetAggregatoModificato.IM_RH_CCS_COSTI<aVPdgAggregato.IM_RH_CCS_COSTI or
        aDetAggregatoModificato.IM_RI_CCS_SPESE_ODC<aVPdgAggregato.IM_RI_CCS_SPESE_ODC or
        aDetAggregatoModificato.IM_RJ_CCS_SPESE_ODC_ALTRA_UO<aVPdgAggregato.IM_RJ_CCS_SPESE_ODC_ALTRA_UO or
        aDetAggregatoModificato.IM_RK_CCS_SPESE_OGC<aVPdgAggregato.IM_RK_CCS_SPESE_OGC or
        aDetAggregatoModificato.IM_RL_CCS_SPESE_OGC_ALTRA_UO<aVPdgAggregato.IM_RL_CCS_SPESE_OGC_ALTRA_UO or
        aDetAggregatoModificato.IM_RM_CSS_AMMORTAMENTI<aVPdgAggregato.IM_RM_CSS_AMMORTAMENTI or
        aDetAggregatoModificato.IM_RN_CSS_RIMANENZE<aVPdgAggregato.IM_RN_CSS_RIMANENZE or
        aDetAggregatoModificato.IM_RO_CSS_ALTRI_COSTI<aVPdgAggregato.IM_RO_CSS_ALTRI_COSTI or
        aDetAggregatoModificato.IM_RP_CSS_VERSO_ALTRO_CDR<aVPdgAggregato.IM_RP_CSS_VERSO_ALTRO_CDR or
        aDetAggregatoModificato.IM_RQ_SSC_COSTI_ODC<aVPdgAggregato.IM_RQ_SSC_COSTI_ODC or
        aDetAggregatoModificato.IM_RR_SSC_COSTI_ODC_ALTRA_UO<aVPdgAggregato.IM_RR_SSC_COSTI_ODC_ALTRA_UO or
        aDetAggregatoModificato.IM_RS_SSC_COSTI_OGC<aVPdgAggregato.IM_RS_SSC_COSTI_OGC or
        aDetAggregatoModificato.IM_RT_SSC_COSTI_OGC_ALTRA_UO<aVPdgAggregato.IM_RT_SSC_COSTI_OGC_ALTRA_UO or
      --  aDetAggregatoModificato.IM_RU_SPESE_COSTI_ALTRUI<aVPdgAggregato.IM_RU_SPESE_COSTI_ALTRUI or -- La contrattazione non è fatta su queste colonne
      --  aDetAggregatoModificato.IM_RV_PAGAMENTI<aVPdgAggregato.IM_RV_PAGAMENTI or
        aDetAggregatoModificato.IM_RAA_A2_COSTI_FINALI<aVPdgAggregato.IM_RAA_A2_COSTI_FINALI or
        aDetAggregatoModificato.IM_RAB_A2_COSTI_ALTRO_CDR<aVPdgAggregato.IM_RAB_A2_COSTI_ALTRO_CDR or
        aDetAggregatoModificato.IM_RAC_A2_SPESE_ODC<aVPdgAggregato.IM_RAC_A2_SPESE_ODC or
        aDetAggregatoModificato.IM_RAD_A2_SPESE_ODC_ALTRA_UO<aVPdgAggregato.IM_RAD_A2_SPESE_ODC_ALTRA_UO or
        aDetAggregatoModificato.IM_RAE_A2_SPESE_OGC<aVPdgAggregato.IM_RAE_A2_SPESE_OGC or
        aDetAggregatoModificato.IM_RAF_A2_SPESE_OGC_ALTRA_UO<aVPdgAggregato.IM_RAF_A2_SPESE_OGC_ALTRA_UO or
      --  aDetAggregatoModificato.IM_RAG_A2_SPESE_COSTI_ALTRUI<aVPdgAggregato.IM_RAG_A2_SPESE_COSTI_ALTRUI or
        aDetAggregatoModificato.IM_RAH_A3_COSTI_FINALI<aVPdgAggregato.IM_RAH_A3_COSTI_FINALI or
        aDetAggregatoModificato.IM_RAI_A3_COSTI_ALTRO_CDR<aVPdgAggregato.IM_RAI_A3_COSTI_ALTRO_CDR or
        aDetAggregatoModificato.IM_RAL_A3_SPESE_ODC<aVPdgAggregato.IM_RAL_A3_SPESE_ODC or
        aDetAggregatoModificato.IM_RAM_A3_SPESE_ODC_ALTRA_UO<aVPdgAggregato.IM_RAM_A3_SPESE_ODC_ALTRA_UO or
        aDetAggregatoModificato.IM_RAN_A3_SPESE_OGC<aVPdgAggregato.IM_RAN_A3_SPESE_OGC or
        aDetAggregatoModificato.IM_RAO_A3_SPESE_OGC_ALTRA_UO<aVPdgAggregato.IM_RAO_A3_SPESE_OGC_ALTRA_UO
      --  aDetAggregatoModificato.IM_RAP_A3_SPESE_COSTI_ALTRUI<aVPdgAggregato.IM_RAP_A3_SPESE_COSTI_ALTRUI
       then

-- inizio

        If aDetAggregatoModificato.IM_RH_CCS_COSTI < aVPdgAggregato.IM_RH_CCS_COSTI Then
          err_col := '\nCosti (H):   Aggregato '||
          Ltrim(To_Char(aDetAggregatoModificato.IM_RH_CCS_COSTI, '999g999g999g999g999g990d00'))||
          '   PdG '||Ltrim(To_Char(aVPdgAggregato.IM_RH_CCS_COSTI, '999g999g999g999g999g990d00'))||
          '   (diff. '||Ltrim(To_Char(aDetAggregatoModificato.IM_RH_CCS_COSTI-aVPdgAggregato.IM_RH_CCS_COSTI, '999g999g999g999g999g990d00'))||')';
        End If;

        If aDetAggregatoModificato.IM_RI_CCS_SPESE_ODC<aVPdgAggregato.IM_RI_CCS_SPESE_ODC Then
          err_col := err_col||'\n'||cnrutil.getLabelObbl()||' da contr. propria UO (I):   Aggregato '||
          Ltrim(To_Char(aDetAggregatoModificato.IM_RH_CCS_COSTI, '999g999g999g999g999g990d00'))||
          '   PdG '||Ltrim(To_Char(aVPdgAggregato.IM_RH_CCS_COSTI, '999g999g999g999g999g990d00'))||
          '   (diff. '||Ltrim(To_Char(aDetAggregatoModificato.IM_RI_CCS_SPESE_ODC-aVPdgAggregato.IM_RI_CCS_SPESE_ODC, '999g999g999g999g999g990d00'))||')';
        End If;

        If aDetAggregatoModificato.IM_RJ_CCS_SPESE_ODC_ALTRA_UO<aVPdgAggregato.IM_RJ_CCS_SPESE_ODC_ALTRA_UO Then
          err_col := err_col||'\n'||cnrutil.getLabelObbl()||' da contr. altra UO (J):   Aggregato '||
          Ltrim(To_Char(aDetAggregatoModificato.IM_RJ_CCS_SPESE_ODC_ALTRA_UO, '999g999g999g999g999g990d00'))||
          '   PdG '||Ltrim(To_Char(aVPdgAggregato.IM_RJ_CCS_SPESE_ODC_ALTRA_Uo, '999g999g999g999g999g990d00'))||
          '   (diff. '||Ltrim(To_Char(aDetAggregatoModificato.IM_RJ_CCS_SPESE_ODC_ALTRA_UO-aVPdgAggregato.IM_RJ_CCS_SPESE_ODC_ALTRA_UO, '999g999g999g999g999g990d00'))||')';
        End If;

        if aDetAggregatoModificato.IM_RK_CCS_SPESE_OGC<aVPdgAggregato.IM_RK_CCS_SPESE_OGC Then
          err_col := err_col||'\n'||cnrutil.getLabelObbl()||' in essere propria UO (K):   Aggregato '||
          Ltrim(To_Char(aDetAggregatoModificato.IM_RK_CCS_SPESE_OGC, '999g999g999g999g999g990d00'))||
          '   PdG '||Ltrim(To_Char(aVPdgAggregato.IM_RK_CCS_SPESE_OGC, '999g999g999g999g999g990d00'))||
          '   (diff. '||Ltrim(To_Char(aDetAggregatoModificato.IM_RK_CCS_SPESE_OGC-aVPdgAggregato.IM_RK_CCS_SPESE_OGC, '999g999g999g999g999g990d00'))||')';
        End If;

        if aDetAggregatoModificato.IM_RL_CCS_SPESE_OGC_ALTRA_UO<aVPdgAggregato.IM_RL_CCS_SPESE_OGC_ALTRA_UO Then
          err_col := err_col||'\n'||cnrutil.getLabelObbl()||' in essere altra UO (L):   Aggregato '||
          Ltrim(To_Char(aDetAggregatoModificato.IM_RL_CCS_SPESE_OGC_ALTRA_UO, '999g999g999g999g999g990d00'))||
          '   PdG '||Ltrim(To_Char(aVPdgAggregato.IM_RL_CCS_SPESE_OGC_ALTRA_UO, '999g999g999g999g999g990d00'))||
          '   (diff. '||Ltrim(To_Char(aDetAggregatoModificato.IM_RL_CCS_SPESE_OGC_ALTRA_UO-aVPdgAggregato.IM_RL_CCS_SPESE_OGC_ALTRA_UO, '999g999g999g999g999g990d00'))||')';
        End If;

        if aDetAggregatoModificato.IM_RM_CSS_AMMORTAMENTI<aVPdgAggregato.IM_RM_CSS_AMMORTAMENTI Then
          err_col := err_col||'\nAmmortamenti (M):   Aggregato '||
          Ltrim(To_Char(aDetAggregatoModificato.IM_RM_CSS_AMMORTAMENTI, '999g999g999g999g999g990d00'))||
          '   PdG '||Ltrim(To_Char(aVPdgAggregato.IM_RM_CSS_AMMORTAMENTI, '999g999g999g999g999g990d00'))||
          '   (diff. '||Ltrim(To_Char(aDetAggregatoModificato.IM_RM_CSS_AMMORTAMENTI-aVPdgAggregato.IM_RM_CSS_AMMORTAMENTI, '999g999g999g999g999g990d00'))||')';
        End If;

        if aDetAggregatoModificato.IM_RN_CSS_RIMANENZE<aVPdgAggregato.IM_RN_CSS_RIMANENZE Then
          err_col := err_col||'\nRimanenze (N):   Aggregato '||
          Ltrim(To_Char(aDetAggregatoModificato.IM_RN_CSS_RIMANENZE, '999g999g999g999g999g990d00'))||
          '   PdG '||Ltrim(To_Char(aVPdgAggregato.IM_RN_CSS_RIMANENZE, '999g999g999g999g999g990d00'))||
          '   (diff. '||Ltrim(To_Char(aDetAggregatoModificato.IM_RN_CSS_RIMANENZE-aVPdgAggregato.IM_RN_CSS_RIMANENZE, '999g999g999g999g999g990d00'))||')';
        End If;

        if aDetAggregatoModificato.IM_RO_CSS_ALTRI_COSTI<aVPdgAggregato.IM_RO_CSS_ALTRI_COSTI Then
          err_col := err_col||'\nAltri Costi (O):   Aggregato '||
          Ltrim(To_Char(aDetAggregatoModificato.IM_RO_CSS_ALTRI_COSTI, '999g999g999g999g999g990d00'))||
          '   PdG '||Ltrim(To_Char(aVPdgAggregato.IM_RO_CSS_ALTRI_COSTI, '999g999g999g999g999g990d00'))||
          '   (diff. '||Ltrim(To_Char(aDetAggregatoModificato.IM_RO_CSS_ALTRI_COSTI-aVPdgAggregato.IM_RO_CSS_ALTRI_COSTI, '999g999g999g999g999g990d00'))||')';
        End If;

        if aDetAggregatoModificato.IM_RP_CSS_VERSO_ALTRO_CDR<aVPdgAggregato.IM_RP_CSS_VERSO_ALTRO_CDR Then
          err_col := err_col||'\nCosti verso altro CDR (P):   Aggregato '||
          Ltrim(To_Char(aDetAggregatoModificato.IM_RP_CSS_VERSO_ALTRO_CDR, '999g999g999g999g999g990d00'))||
          '   PdG '||Ltrim(To_Char(aVPdgAggregato.IM_RP_CSS_VERSO_ALTRO_CDR, '999g999g999g999g999g990d00'))||
          '   (diff. '||Ltrim(To_Char(aDetAggregatoModificato.IM_RP_CSS_VERSO_ALTRO_CDR-aVPdgAggregato.IM_RP_CSS_VERSO_ALTRO_CDR, '999g999g999g999g999g990d00'))||')';
        End If;

        if aDetAggregatoModificato.IM_RQ_SSC_COSTI_ODC<aVPdgAggregato.IM_RQ_SSC_COSTI_ODC Then
          err_col := err_col||'\n'||cnrutil.getLabelObbl()||' da contr. propria UO (Q):   Aggregato '||
          Ltrim(To_Char(aDetAggregatoModificato.IM_RQ_SSC_COSTI_ODC, '999g999g999g999g999g990d00'))||
          '   PdG '||Ltrim(To_Char(aVPdgAggregato.IM_RQ_SSC_COSTI_ODC, '999g999g999g999g999g990d00'))||
          '   (diff. '||Ltrim(To_Char(aDetAggregatoModificato.IM_RQ_SSC_COSTI_ODC-aVPdgAggregato.IM_RQ_SSC_COSTI_ODC, '999g999g999g999g999g990d00'))||')';
        End If;

        if aDetAggregatoModificato.IM_RR_SSC_COSTI_ODC_ALTRA_UO<aVPdgAggregato.IM_RR_SSC_COSTI_ODC_ALTRA_UO Then
          err_col := err_col||'\n'||cnrutil.getLabelObbl()||' da contr. altra UO (R):   Aggregato '||
          Ltrim(To_Char(aDetAggregatoModificato.IM_RR_SSC_COSTI_ODC_ALTRA_UO, '999g999g999g999g999g990d00'))||
          '   PdG '||Ltrim(To_Char(aVPdgAggregato.IM_RR_SSC_COSTI_ODC_ALTRA_UO, '999g999g999g999g999g990d00'))||
          '   (diff. '||Ltrim(To_Char(aDetAggregatoModificato.IM_RR_SSC_COSTI_ODC_ALTRA_UO-aVPdgAggregato.IM_RR_SSC_COSTI_ODC_ALTRA_UO, '999g999g999g999g999g990d00'))||')';
        End If;

        if aDetAggregatoModificato.IM_RS_SSC_COSTI_OGC<aVPdgAggregato.IM_RS_SSC_COSTI_OGC Then
          err_col := err_col||'\n'||cnrutil.getLabelObbl()||' in essere propria UO (S):   Aggregato '||
          Ltrim(To_Char(aDetAggregatoModificato.IM_RS_SSC_COSTI_OGC, '999g999g999g999g999g990d00'))||
          '   PdG '||Ltrim(To_Char(aVPdgAggregato.IM_RS_SSC_COSTI_OGC, '999g999g999g999g999g990d00'))||
          '   (diff. '||Ltrim(To_Char(aDetAggregatoModificato.IM_RS_SSC_COSTI_OGC-aVPdgAggregato.IM_RS_SSC_COSTI_OGC, '999g999g999g999g999g990d00'))||')';
        End If;

        if aDetAggregatoModificato.IM_RT_SSC_COSTI_OGC_ALTRA_UO<aVPdgAggregato.IM_RT_SSC_COSTI_OGC_ALTRA_UO Then
          err_col := err_col||'\n'||cnrutil.getLabelObbl()||' in essere altra UO (T):   Aggregato '||
          Ltrim(To_Char(aDetAggregatoModificato.IM_RT_SSC_COSTI_OGC_ALTRA_UO, '999g999g999g999g999g990d00'))||
          '   PdG '||Ltrim(To_Char(aVPdgAggregato.IM_RT_SSC_COSTI_OGC_ALTRA_UO, '999g999g999g999g999g990d00'))||
          '   (diff. '||Ltrim(To_Char(aDetAggregatoModificato.IM_RT_SSC_COSTI_OGC_ALTRA_UO-aVPdgAggregato.IM_RT_SSC_COSTI_OGC_ALTRA_UO, '999g999g999g999g999g990d00'))||')';
        End If;
/*
        if aDetAggregatoModificato.IM_RAA_A2_COSTI_FINALI<aVPdgAggregato.IM_RAA_A2_COSTI_FINALI Then
                err_col := err_col||
aDetAggregatoModificato.IM_RAA_A2_COSTI_FINALI||' PdG '||aVPdgAggregato.IM_RAA_A2_COSTI_FINALI ||'. ';
        End If;

        if aDetAggregatoModificato.IM_RAB_A2_COSTI_ALTRO_CDR<aVPdgAggregato.IM_RAB_A2_COSTI_ALTRO_CDR Then
                err_col := err_col||
aDetAggregatoModificato.IM_RAB_A2_COSTI_ALTRO_CDR||' PdG '||aVPdgAggregato.IM_RAB_A2_COSTI_ALTRO_CDR ||'. ';
        End If;

        if aDetAggregatoModificato.IM_RAC_A2_SPESE_ODC<aVPdgAggregato.IM_RAC_A2_SPESE_ODC Then
                err_col := err_col||
aDetAggregatoModificato.IM_RAC_A2_SPESE_ODC||' PdG '||aVPdgAggregato.IM_RAC_A2_SPESE_ODC ||'. ';
        End If;

        if aDetAggregatoModificato.IM_RAD_A2_SPESE_ODC_ALTRA_UO<aVPdgAggregato.IM_RAD_A2_SPESE_ODC_ALTRA_UO Then
                err_col := err_col||
aDetAggregatoModificato.IM_RAD_A2_SPESE_ODC_ALTRA_UO||' PdG '||aVPdgAggregato.IM_RAD_A2_SPESE_ODC_ALTRA_UO ||'. ';
        End If;

        if aDetAggregatoModificato.IM_RAE_A2_SPESE_OGC<aVPdgAggregato.IM_RAE_A2_SPESE_OGC Then
                err_col := err_col||
aDetAggregatoModificato.IM_RAE_A2_SPESE_OGC||' PdG '||aVPdgAggregato.IM_RAE_A2_SPESE_OGC||'. ';
        End If;

        if aDetAggregatoModificato.IM_RAF_A2_SPESE_OGC_ALTRA_UO<aVPdgAggregato.IM_RAF_A2_SPESE_OGC_ALTRA_UO Then
                err_col := err_col||
aDetAggregatoModificato.IM_RAF_A2_SPESE_OGC_ALTRA_UO||' PdG '||aVPdgAggregato.IM_RAF_A2_SPESE_OGC_ALTRA_UO||'. ';
        End If;

        if aDetAggregatoModificato.IM_RAH_A3_COSTI_FINALI<aVPdgAggregato.IM_RAH_A3_COSTI_FINALI Then
                err_col := err_col||
aDetAggregatoModificato.IM_RAH_A3_COSTI_FINALI||' PdG '||aVPdgAggregato.IM_RAH_A3_COSTI_FINALI||'. ';
        End If;

        if aDetAggregatoModificato.IM_RAI_A3_COSTI_ALTRO_CDR<aVPdgAggregato.IM_RAI_A3_COSTI_ALTRO_CDR Then
                err_col := err_col||
aDetAggregatoModificato.IM_RAI_A3_COSTI_ALTRO_CDR||' PdG '||aVPdgAggregato.IM_RAI_A3_COSTI_ALTRO_CDR||'. ';
        End If;

        if aDetAggregatoModificato.IM_RAL_A3_SPESE_ODC<aVPdgAggregato.IM_RAL_A3_SPESE_ODC Then
                err_col := err_col||
aDetAggregatoModificato.IM_RAL_A3_SPESE_ODC||' PdG '||aVPdgAggregato.IM_RAL_A3_SPESE_ODC||'. ';
        End If;

        if aDetAggregatoModificato.IM_RAM_A3_SPESE_ODC_ALTRA_UO<aVPdgAggregato.IM_RAM_A3_SPESE_ODC_ALTRA_UO Then
                err_col := err_col||
aDetAggregatoModificato.IM_RAM_A3_SPESE_ODC_ALTRA_UO||' PdG '||aVPdgAggregato.IM_RAM_A3_SPESE_ODC_ALTRA_UO||'. ';
        End If;

        if aDetAggregatoModificato.IM_RAN_A3_SPESE_OGC<aVPdgAggregato.IM_RAN_A3_SPESE_OGC Then
                err_col := err_col||
aDetAggregatoModificato.IM_RAN_A3_SPESE_OGC||' PdG '||aVPdgAggregato.IM_RAN_A3_SPESE_OGC||'. ';
        End If;

        if aDetAggregatoModificato.IM_RAO_A3_SPESE_OGC_ALTRA_UO<aVPdgAggregato.IM_RAO_A3_SPESE_OGC_ALTRA_UO Then
                err_col := err_col||
aDetAggregatoModificato.IM_RAO_A3_SPESE_OGC_ALTRA_UO||' PdG '||aVPdgAggregato.IM_RAO_A3_SPESE_OGC_ALTRA_UO||'. ';
        End If;
*/
-- fine

        IBMERR001.RAISE_ERR_GENERICO('Il nuovo aggregato supera gli importi stabiliti dal centro su Voce: '||aVPdgAggregato.CD_ELEMENTO_VOCE||' - Funzione: '||aVPdgAggregato.CD_FUNZIONE||' - Natura: '||aVPdgAggregato.CD_NATURA||' cds: '||aVPdgAggregato.CD_CDS||
        '\n\nDettagli:'||err_col);
       end if;
      exception when NO_DATA_FOUND then
       null;
	  end;
     end if;

     aPdgAggregatoSpe.ESERCIZIO:=aVPdgAggregato.ESERCIZIO;
     aPdgAggregatoSpe.CD_CENTRO_RESPONSABILITA:=aVPdgAggregato.CD_CENTRO_RESPONSABILITA;
     aPdgAggregatoSpe.CD_CDS:=aVPdgAggregato.CD_CDS;
     aPdgAggregatoSpe.TI_APPARTENENZA:=aVPdgAggregato.TI_APPARTENENZA;
     aPdgAggregatoSpe.TI_GESTIONE:=aVPdgAggregato.TI_GESTIONE;
     aPdgAggregatoSpe.CD_ELEMENTO_VOCE:=aVPdgAggregato.CD_ELEMENTO_VOCE;
     aPdgAggregatoSpe.CD_FUNZIONE:=aVPdgAggregato.CD_FUNZIONE;
     aPdgAggregatoSpe.CD_NATURA:=aVPdgAggregato.CD_NATURA;

     aPdgAggregatoSpe.TI_AGGREGATO:=TI_AGGREGATO_INIZIALE;

     aPdgAggregatoSpe.IM_RH_CCS_COSTI:=aVPdgAggregato.IM_RH_CCS_COSTI;
     aPdgAggregatoSpe.IM_RI_CCS_SPESE_ODC:=aVPdgAggregato.IM_RI_CCS_SPESE_ODC;
     aPdgAggregatoSpe.IM_RJ_CCS_SPESE_ODC_ALTRA_UO:=aVPdgAggregato.IM_RJ_CCS_SPESE_ODC_ALTRA_UO;
     aPdgAggregatoSpe.IM_RK_CCS_SPESE_OGC:=aVPdgAggregato.IM_RK_CCS_SPESE_OGC;
     aPdgAggregatoSpe.IM_RL_CCS_SPESE_OGC_ALTRA_UO:=aVPdgAggregato.IM_RL_CCS_SPESE_OGC_ALTRA_UO;
     aPdgAggregatoSpe.IM_RM_CSS_AMMORTAMENTI:=aVPdgAggregato.IM_RM_CSS_AMMORTAMENTI;
     aPdgAggregatoSpe.IM_RN_CSS_RIMANENZE:=aVPdgAggregato.IM_RN_CSS_RIMANENZE;
     aPdgAggregatoSpe.IM_RO_CSS_ALTRI_COSTI:=aVPdgAggregato.IM_RO_CSS_ALTRI_COSTI;
     aPdgAggregatoSpe.IM_RP_CSS_VERSO_ALTRO_CDR:=aVPdgAggregato.IM_RP_CSS_VERSO_ALTRO_CDR;
     aPdgAggregatoSpe.IM_RQ_SSC_COSTI_ODC:=aVPdgAggregato.IM_RQ_SSC_COSTI_ODC;
     aPdgAggregatoSpe.IM_RR_SSC_COSTI_ODC_ALTRA_UO:=aVPdgAggregato.IM_RR_SSC_COSTI_ODC_ALTRA_UO;
     aPdgAggregatoSpe.IM_RS_SSC_COSTI_OGC:=aVPdgAggregato.IM_RS_SSC_COSTI_OGC;
     aPdgAggregatoSpe.IM_RT_SSC_COSTI_OGC_ALTRA_UO:=aVPdgAggregato.IM_RT_SSC_COSTI_OGC_ALTRA_UO;
     aPdgAggregatoSpe.IM_RU_SPESE_COSTI_ALTRUI:=aVPdgAggregato.IM_RU_SPESE_COSTI_ALTRUI;
     aPdgAggregatoSpe.IM_RV_PAGAMENTI:=aVPdgAggregato.IM_RV_PAGAMENTI;
     aPdgAggregatoSpe.IM_RAA_A2_COSTI_FINALI:=aVPdgAggregato.IM_RAA_A2_COSTI_FINALI;
     aPdgAggregatoSpe.IM_RAB_A2_COSTI_ALTRO_CDR:=aVPdgAggregato.IM_RAB_A2_COSTI_ALTRO_CDR;
     aPdgAggregatoSpe.IM_RAC_A2_SPESE_ODC:=aVPdgAggregato.IM_RAC_A2_SPESE_ODC;
     aPdgAggregatoSpe.IM_RAD_A2_SPESE_ODC_ALTRA_UO:=aVPdgAggregato.IM_RAD_A2_SPESE_ODC_ALTRA_UO;
     aPdgAggregatoSpe.IM_RAE_A2_SPESE_OGC:=aVPdgAggregato.IM_RAE_A2_SPESE_OGC;
     aPdgAggregatoSpe.IM_RAF_A2_SPESE_OGC_ALTRA_UO:=aVPdgAggregato.IM_RAF_A2_SPESE_OGC_ALTRA_UO;
     aPdgAggregatoSpe.IM_RAG_A2_SPESE_COSTI_ALTRUI:=aVPdgAggregato.IM_RAG_A2_SPESE_COSTI_ALTRUI;
     aPdgAggregatoSpe.IM_RAH_A3_COSTI_FINALI:=aVPdgAggregato.IM_RAH_A3_COSTI_FINALI;
     aPdgAggregatoSpe.IM_RAI_A3_COSTI_ALTRO_CDR:=aVPdgAggregato.IM_RAI_A3_COSTI_ALTRO_CDR;
     aPdgAggregatoSpe.IM_RAL_A3_SPESE_ODC:=aVPdgAggregato.IM_RAL_A3_SPESE_ODC;
     aPdgAggregatoSpe.IM_RAM_A3_SPESE_ODC_ALTRA_UO:=aVPdgAggregato.IM_RAM_A3_SPESE_ODC_ALTRA_UO;
     aPdgAggregatoSpe.IM_RAN_A3_SPESE_OGC:=aVPdgAggregato.IM_RAN_A3_SPESE_OGC;
     aPdgAggregatoSpe.IM_RAO_A3_SPESE_OGC_ALTRA_UO:=aVPdgAggregato.IM_RAO_A3_SPESE_OGC_ALTRA_UO;
     aPdgAggregatoSpe.IM_RAP_A3_SPESE_COSTI_ALTRUI:=aVPdgAggregato.IM_RAP_A3_SPESE_COSTI_ALTRUI;
     aPdgAggregatoSpe.UTCR:=aUser;
     aPdgAggregatoSpe.DACR:=aTSNow;
     aPdgAggregatoSpe.UTUV:=aUser;
     aPdgAggregatoSpe.DUVA:=aTSNow;
     aPdgAggregatoSpe.PG_VER_REC:=1;

	 -- Dettaglio iniziale
     ins_PDG_AGGREGATO_SPE_DET(aPdgAggregatoSpe);
     if daVariazione = 'Y' Then
       aPdgAggregatoSpe.TI_AGGREGATO:=TI_AGGREGATO_MODIFICATO;
       ins_PDG_AGGREGATO_SPE_DET(aPdgAggregatoSpe);
     End If;
	 -- Aggiorna nell'aggregato modificato dal centro le tre colonne degli importi di spesa per costi altrui che non sono contrattabili
	 if
         aPdgAggregatoSpe.IM_RU_SPESE_COSTI_ALTRUI!=0
	  or aPdgAggregatoSpe.IM_RAG_A2_SPESE_COSTI_ALTRUI!=0
	  or aPdgAggregatoSpe.IM_RAP_A3_SPESE_COSTI_ALTRUI!=0
	 then
	  update pdg_aggregato_spe_det set
          IM_RU_SPESE_COSTI_ALTRUI=aPdgAggregatoSpe.IM_RU_SPESE_COSTI_ALTRUI
	     ,IM_RAG_A2_SPESE_COSTI_ALTRUI=aPdgAggregatoSpe.IM_RAG_A2_SPESE_COSTI_ALTRUI
	     ,IM_RAP_A3_SPESE_COSTI_ALTRUI=aPdgAggregatoSpe.IM_RAP_A3_SPESE_COSTI_ALTRUI
		 ,utuv=aUser
		 ,duva=aTSNow
		 ,pg_ver_rec=pg_ver_rec+1
	  where
           esercizio=aPdgAggregatoSpe.ESERCIZIO
       and cd_centro_responsabilita=aPdgAggregatoSpe.CD_CENTRO_RESPONSABILITA
  	   and cd_cds=aPdgAggregatoSpe.CD_CDS
       and ti_appartenenza=aPdgAggregatoSpe.TI_APPARTENENZA
       and ti_gestione=aPdgAggregatoSpe.TI_GESTIONE
       and cd_elemento_voce=aPdgAggregatoSpe.CD_ELEMENTO_VOCE
       and CD_FUNZIONE=aPdgAggregatoSpe.CD_FUNZIONE
       and CD_NATURA=aPdgAggregatoSpe.CD_NATURA
       and TI_AGGREGATO=TI_AGGREGATO_MODIFICATO;
	 end if;

	 -- Dettaglio modificato

	 -- Cerca di inserire le righe di aggregato 'modificato' non presenti per la combinazione di par. di aggregazione che arriva dal
	 -- basso. Se trova già il dettaglio non effettua operazioni.
	 --
     if isAggregatoGiaCreato then
      aPdgAggregatoSpe.IM_RH_CCS_COSTI:=0;
      aPdgAggregatoSpe.IM_RI_CCS_SPESE_ODC:=0;
      aPdgAggregatoSpe.IM_RJ_CCS_SPESE_ODC_ALTRA_UO:=0;
      aPdgAggregatoSpe.IM_RK_CCS_SPESE_OGC:=0;
      aPdgAggregatoSpe.IM_RL_CCS_SPESE_OGC_ALTRA_UO:=0;
      aPdgAggregatoSpe.IM_RM_CSS_AMMORTAMENTI:=0;
      aPdgAggregatoSpe.IM_RN_CSS_RIMANENZE:=0;
      aPdgAggregatoSpe.IM_RO_CSS_ALTRI_COSTI:=0;
      aPdgAggregatoSpe.IM_RP_CSS_VERSO_ALTRO_CDR:=0;
      aPdgAggregatoSpe.IM_RQ_SSC_COSTI_ODC:=0;
      aPdgAggregatoSpe.IM_RR_SSC_COSTI_ODC_ALTRA_UO:=0;
      aPdgAggregatoSpe.IM_RS_SSC_COSTI_OGC:=0;
      aPdgAggregatoSpe.IM_RT_SSC_COSTI_OGC_ALTRA_UO:=0;
      aPdgAggregatoSpe.IM_RU_SPESE_COSTI_ALTRUI:=0;
      aPdgAggregatoSpe.IM_RV_PAGAMENTI:=0;
      aPdgAggregatoSpe.IM_RAA_A2_COSTI_FINALI:=0;
      aPdgAggregatoSpe.IM_RAB_A2_COSTI_ALTRO_CDR:=0;
      aPdgAggregatoSpe.IM_RAC_A2_SPESE_ODC:=0;
      aPdgAggregatoSpe.IM_RAD_A2_SPESE_ODC_ALTRA_UO:=0;
      aPdgAggregatoSpe.IM_RAE_A2_SPESE_OGC:=0;
      aPdgAggregatoSpe.IM_RAF_A2_SPESE_OGC_ALTRA_UO:=0;
      aPdgAggregatoSpe.IM_RAG_A2_SPESE_COSTI_ALTRUI:=0;
      aPdgAggregatoSpe.IM_RAH_A3_COSTI_FINALI:=0;
      aPdgAggregatoSpe.IM_RAI_A3_COSTI_ALTRO_CDR:=0;
      aPdgAggregatoSpe.IM_RAL_A3_SPESE_ODC:=0;
      aPdgAggregatoSpe.IM_RAM_A3_SPESE_ODC_ALTRA_UO:=0;
      aPdgAggregatoSpe.IM_RAN_A3_SPESE_OGC:=0;
      aPdgAggregatoSpe.IM_RAO_A3_SPESE_OGC_ALTRA_UO:=0;
      aPdgAggregatoSpe.IM_RAP_A3_SPESE_COSTI_ALTRUI:=0;
	 end if;
	 aPdgAggregatoSpe.TI_AGGREGATO:=TI_AGGREGATO_MODIFICATO;
     begin
      ins_PDG_AGGREGATO_SPE_DET(aPdgAggregatoSpe);
     exception when dup_val_on_index then
	  null;
	 end;
  end loop;

  for aVPdgAggregato in (select * from v_dpdg_aggregato_etr_det_spn where
       esercizio = aEs
   and cd_centro_responsabilita = aCDR.cd_centro_responsabilita) loop
     aPdgAggregatoEtr.ESERCIZIO:=aVPdgAggregato.ESERCIZIO;
     aPdgAggregatoEtr.CD_CENTRO_RESPONSABILITA:=aVPdgAggregato.CD_CENTRO_RESPONSABILITA;
     aPdgAggregatoEtr.CD_NATURA:=aVPdgAggregato.CD_NATURA;
     aPdgAggregatoEtr.TI_APPARTENENZA:=aVPdgAggregato.TI_APPARTENENZA;
     aPdgAggregatoEtr.TI_GESTIONE:=aVPdgAggregato.TI_GESTIONE;
     aPdgAggregatoEtr.CD_ELEMENTO_VOCE:=aVPdgAggregato.CD_ELEMENTO_VOCE;

     aPdgAggregatoEtr.TI_AGGREGATO:=TI_AGGREGATO_INIZIALE;

     aPdgAggregatoEtr.IM_RA_RCE:=aVPdgAggregato.IM_RA_RCE;
     aPdgAggregatoEtr.IM_RB_RSE:=aVPdgAggregato.IM_RB_RSE;
     aPdgAggregatoEtr.IM_RC_ESR:=aVPdgAggregato.IM_RC_ESR;
     aPdgAggregatoEtr.IM_RD_A2_RICAVI:=aVPdgAggregato.IM_RD_A2_RICAVI;
     aPdgAggregatoEtr.IM_RE_A2_ENTRATE:=aVPdgAggregato.IM_RE_A2_ENTRATE;
     aPdgAggregatoEtr.IM_RF_A3_RICAVI:=aVPdgAggregato.IM_RF_A3_RICAVI;
     aPdgAggregatoEtr.IM_RG_A3_ENTRATE:=aVPdgAggregato.IM_RG_A3_ENTRATE;
     aPdgAggregatoEtr.UTCR:=aUser;
     aPdgAggregatoEtr.DACR:=aTSNow;
     aPdgAggregatoEtr.UTUV:=aUser;
     aPdgAggregatoEtr.DUVA:=aTSNow;
     aPdgAggregatoEtr.PG_VER_REC:=1;

	 -- Inserimento aggregato iniziale
     ins_PDG_AGGREGATO_ETR_DET(aPdgAggregatoEtr);
     if daVariazione = 'Y' Then
       aPdgAggregatoEtr.TI_AGGREGATO:=TI_AGGREGATO_MODIFICATO;
       ins_PDG_AGGREGATO_ETR_DET(aPdgAggregatoEtr);
     End If;

	 -- Inserimento aggregato modificato
     if isAggregatoGiaCreato then
      aPdgAggregatoEtr.IM_RA_RCE:=0;
      aPdgAggregatoEtr.IM_RB_RSE:=0;
      aPdgAggregatoEtr.IM_RC_ESR:=0;
      aPdgAggregatoEtr.IM_RD_A2_RICAVI:=0;
      aPdgAggregatoEtr.IM_RE_A2_ENTRATE:=0;
      aPdgAggregatoEtr.IM_RF_A3_RICAVI:=0;
      aPdgAggregatoEtr.IM_RG_A3_ENTRATE:=0;
	 end if;

     aPdgAggregatoEtr.TI_AGGREGATO:=TI_AGGREGATO_MODIFICATO;
	 begin
      ins_PDG_AGGREGATO_ETR_DET(aPdgAggregatoEtr);
     exception when dup_val_on_index then
	  null;
	 end;
  end loop;

  -- Aggiornamento utuv, duva e pg_ver_rec della testata del PDG AGGREGATO
  update  pdg_aggregato
  set
   utuv=aUser,
   duva=aTSNow,
   pg_ver_rec=pg_ver_rec+1
  where
       esercizio = aEs
   and cd_centro_responsabilita = aCDR.cd_centro_responsabilita;
 end;

 procedure apriPDG(aEs number, aCdCds varchar2, aUser varchar2) is
  aCDR cdr%rowtype;
  aPdg pdg_preventivo%rowtype;
  aPdgEs pdg_esercizio%rowtype;
  aTSNow date;
  aCDREnte cdr%rowtype;
  recParametriCNR PARAMETRI_CNR%Rowtype;
 begin
  recParametriCNR := CNRUTL001.getRecParametriCnr(aEs);
  aTSNow:=sysdate;

  aCDREnte:=CNRCTB020.GETCDRENTE;

  -- Lock table pdg_preventivo

  lock table pdg_preventivo in exclusive mode;

  lock table pdg_esercizio in exclusive mode;

  for aCDR in (select a.*, b.cd_tipo_unita from v_cdr_valido a, unita_organizzativa b where
                      a.esercizio = aEs
				  and a.cd_centro_responsabilita <> aCDREnte.cd_centro_responsabilita
				  and a.cd_unita_organizzativa = b.cd_unita_organizzativa
				  and b.cd_unita_padre = aCdCds
			  ) loop
   	If recParametriCNR.fl_nuovo_pdg='N' Then
	   begin
	    aPdg.ESERCIZIO:=aEs;
	    aPdg.CD_CENTRO_RESPONSABILITA:=aCDR.cd_centro_responsabilita;
	    aPdg.STATO:=STATO_PDG_INIZIALE;
	    aPdg.ANNOTAZIONI:='';
	    aPdg.FL_RIBALTATO_SU_AREA:='N';
	    aPdg.DACR:=aTSNow;
	    aPdg.UTCR:=aUser;
	    aPdg.DUVA:=aTSNow;
	    aPdg.UTUV:=aUser;
	    aPdg.PG_VER_REC:=1;
	    ins_PDG_PREVENTIVO (aPdg);
	   exception when dup_val_on_index then
	    null;
	   end;
	End If;
    Begin
    -- per i cdr di primo livello viene creato il record su pdg_esercizio
	If recParametriCNR.fl_nuovo_pdg='N' or aCDR.livello=1 or (
	aCDR.livello=2 and (aCDR.cd_tipo_unita=CNRCTB020.TIPO_SAC or aCDR.cd_tipo_unita=CNRCTB020.TIPO_AREA)) Then
        aPdgEs.ESERCIZIO:=aEs;
        aPdgEs.CD_CENTRO_RESPONSABILITA:=aCDR.cd_centro_responsabilita;
        aPdgEs.STATO:=STATO_PDG2_INIZIALE;
        aPdgEs.DACR:=aTSNow;
        aPdgEs.UTCR:=aUser;
        aPdgEs.DUVA:=aTSNow;
        aPdgEs.UTUV:=aUser;
        aPdgEs.PG_VER_REC:=1;
        ins_PDG_ESERCIZIO (aPdgEs);
    End If;
   Exception When dup_val_on_index then
    null;
   End;
  end loop;
 end;

 function checkQuadRicFig(aEsercizio number, aCdCdr  varchar2) return char is
  aNum number;
 begin
  select count(*) into aNum from V_DPDG_TOT_BIL_RICFIGCDR where
        esercizio = aEsercizio
    and cd_centro_responsabilita = aCdCdr
	and (
	    im_etr_a1 != im_spe_a1
	 or im_etr_a2 != im_spe_a2
	 or im_etr_a3 != im_spe_a3
	);
  if aNum = 0 then
   return 'Y';
  else
   return 'N';
  end if;
 end;

 procedure resetCampiImporto(aDett in out pdg_preventivo_spe_det%rowtype) is
 begin
     aDett.IM_RH_CCS_COSTI:=0;
     aDett.IM_RI_CCS_SPESE_ODC:=0;
     aDett.IM_RJ_CCS_SPESE_ODC_ALTRA_UO:=0;
     aDett.IM_RK_CCS_SPESE_OGC:=0;
     aDett.IM_RL_CCS_SPESE_OGC_ALTRA_UO:=0;
     aDett.IM_RM_CSS_AMMORTAMENTI:=0;
     aDett.IM_RN_CSS_RIMANENZE:=0;
     aDett.IM_RO_CSS_ALTRI_COSTI:=0;
     aDett.IM_RP_CSS_VERSO_ALTRO_CDR:=0;
     aDett.IM_RQ_SSC_COSTI_ODC:=0;
     aDett.IM_RR_SSC_COSTI_ODC_ALTRA_UO:=0;
     aDett.IM_RS_SSC_COSTI_OGC:=0;
     aDett.IM_RT_SSC_COSTI_OGC_ALTRA_UO:=0;
     aDett.IM_RU_SPESE_COSTI_ALTRUI:=0;
     aDett.IM_RV_PAGAMENTI:=0;
     aDett.IM_RAA_A2_COSTI_FINALI:=0;
     aDett.IM_RAB_A2_COSTI_ALTRO_CDR:=0;
     aDett.IM_RAC_A2_SPESE_ODC:=0;
     aDett.IM_RAD_A2_SPESE_ODC_ALTRA_UO:=0;
     aDett.IM_RAE_A2_SPESE_OGC:=0;
     aDett.IM_RAF_A2_SPESE_OGC_ALTRA_UO:=0;
     aDett.IM_RAG_A2_SPESE_COSTI_ALTRUI:=0;
     aDett.IM_RAH_A3_COSTI_FINALI:=0;
     aDett.IM_RAI_A3_COSTI_ALTRO_CDR:=0;
     aDett.IM_RAL_A3_SPESE_ODC:=0;
     aDett.IM_RAM_A3_SPESE_ODC_ALTRA_UO:=0;
     aDett.IM_RAN_A3_SPESE_OGC:=0;
     aDett.IM_RAO_A3_SPESE_OGC_ALTRA_UO:=0;
     aDett.IM_RAP_A3_SPESE_COSTI_ALTRUI:=0;
 end;


 procedure resetCampiImporto(aDett in out pdg_preventivo_etr_det%rowtype) is
 begin
     aDett.IM_RA_RCE:=0;
     aDett.IM_RB_RSE:=0;
     aDett.IM_RC_ESR:=0;
     aDett.IM_RD_A2_RICAVI:=0;
     aDett.IM_RE_A2_ENTRATE:=0;
     aDett.IM_RF_A3_RICAVI:=0;
     aDett.IM_RG_A3_ENTRATE:=0;
 end;

 procedure ins_PDG_PREVENTIVO_ETR_DET (aDest PDG_PREVENTIVO_ETR_DET%rowtype) is
  begin
   insert into PDG_PREVENTIVO_ETR_DET (
     ESERCIZIO
    ,CD_CENTRO_RESPONSABILITA
    ,CD_LINEA_ATTIVITA
    ,CD_FUNZIONE
    ,CD_NATURA
    ,TI_APPARTENENZA
    ,TI_GESTIONE
    ,CD_ELEMENTO_VOCE
    ,PG_ENTRATA
    ,DT_REGISTRAZIONE
    ,DESCRIZIONE
    ,STATO
    ,ORIGINE
    ,CATEGORIA_DETTAGLIO
    ,FL_SOLA_LETTURA
    ,CD_CENTRO_RESPONSABILITA_CLGS
    ,CD_LINEA_ATTIVITA_CLGS
    ,TI_APPARTENENZA_CLGS
    ,TI_GESTIONE_CLGS
    ,CD_ELEMENTO_VOCE_CLGS
    ,PG_SPESA_CLGS
    ,IM_RA_RCE
    ,IM_RB_RSE
    ,IM_RC_ESR
    ,IM_RD_A2_RICAVI
    ,IM_RE_A2_ENTRATE
    ,IM_RF_A3_RICAVI
    ,IM_RG_A3_ENTRATE
    ,DACR
    ,UTCR
    ,DUVA
    ,UTUV
    ,PG_VER_REC
    ,ESERCIZIO_PDG_VARIAZIONE
    ,PG_VARIAZIONE_PDG
   ) values (
     aDest.ESERCIZIO
    ,aDest.CD_CENTRO_RESPONSABILITA
    ,aDest.CD_LINEA_ATTIVITA
    ,aDest.CD_FUNZIONE
    ,aDest.CD_NATURA
    ,aDest.TI_APPARTENENZA
    ,aDest.TI_GESTIONE
    ,aDest.CD_ELEMENTO_VOCE
    ,aDest.PG_ENTRATA
    ,aDest.DT_REGISTRAZIONE
    ,aDest.DESCRIZIONE
    ,aDest.STATO
    ,aDest.ORIGINE
    ,aDest.CATEGORIA_DETTAGLIO
    ,aDest.FL_SOLA_LETTURA
    ,aDest.CD_CENTRO_RESPONSABILITA_CLGS
    ,aDest.CD_LINEA_ATTIVITA_CLGS
    ,aDest.TI_APPARTENENZA_CLGS
    ,aDest.TI_GESTIONE_CLGS
    ,aDest.CD_ELEMENTO_VOCE_CLGS
    ,aDest.PG_SPESA_CLGS
    ,aDest.IM_RA_RCE
    ,aDest.IM_RB_RSE
    ,aDest.IM_RC_ESR
    ,aDest.IM_RD_A2_RICAVI
    ,aDest.IM_RE_A2_ENTRATE
    ,aDest.IM_RF_A3_RICAVI
    ,aDest.IM_RG_A3_ENTRATE
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.DUVA
    ,aDest.UTUV
    ,aDest.PG_VER_REC
    ,aDest.ESERCIZIO_PDG_VARIAZIONE
    ,aDest.PG_VARIAZIONE_PDG
    );
 end;

 procedure ins_PDG_PREVENTIVO_SPE_DET (aDest PDG_PREVENTIVO_SPE_DET%rowtype) is
  begin
   insert into PDG_PREVENTIVO_SPE_DET (
     ESERCIZIO
    ,CD_CENTRO_RESPONSABILITA
    ,CD_LINEA_ATTIVITA
    ,CD_FUNZIONE
    ,CD_NATURA
    ,TI_APPARTENENZA
    ,TI_GESTIONE
    ,CD_ELEMENTO_VOCE
    ,PG_SPESA
    ,DT_REGISTRAZIONE
    ,DESCRIZIONE
    ,STATO
    ,ORIGINE
    ,CATEGORIA_DETTAGLIO
    ,FL_SOLA_LETTURA
    ,CD_CENTRO_RESPONSABILITA_CLGE
    ,CD_LINEA_ATTIVITA_CLGE
    ,TI_APPARTENENZA_CLGE
    ,TI_GESTIONE_CLGE
    ,CD_ELEMENTO_VOCE_CLGE
    ,PG_ENTRATA_CLGE
    ,IM_RH_CCS_COSTI
    ,IM_RI_CCS_SPESE_ODC
    ,IM_RJ_CCS_SPESE_ODC_ALTRA_UO
    ,IM_RK_CCS_SPESE_OGC
    ,IM_RL_CCS_SPESE_OGC_ALTRA_UO
    ,IM_RM_CSS_AMMORTAMENTI
    ,IM_RN_CSS_RIMANENZE
    ,IM_RO_CSS_ALTRI_COSTI
    ,IM_RP_CSS_VERSO_ALTRO_CDR
    ,IM_RQ_SSC_COSTI_ODC
    ,IM_RR_SSC_COSTI_ODC_ALTRA_UO
    ,IM_RS_SSC_COSTI_OGC
    ,IM_RT_SSC_COSTI_OGC_ALTRA_UO
    ,IM_RU_SPESE_COSTI_ALTRUI
    ,IM_RV_PAGAMENTI
    ,IM_RAA_A2_COSTI_FINALI
    ,IM_RAB_A2_COSTI_ALTRO_CDR
    ,IM_RAC_A2_SPESE_ODC
    ,IM_RAD_A2_SPESE_ODC_ALTRA_UO
    ,IM_RAE_A2_SPESE_OGC
    ,IM_RAF_A2_SPESE_OGC_ALTRA_UO
    ,IM_RAG_A2_SPESE_COSTI_ALTRUI
    ,IM_RAH_A3_COSTI_FINALI
    ,IM_RAI_A3_COSTI_ALTRO_CDR
    ,IM_RAL_A3_SPESE_ODC
    ,IM_RAM_A3_SPESE_ODC_ALTRA_UO
    ,IM_RAN_A3_SPESE_OGC
    ,IM_RAO_A3_SPESE_OGC_ALTRA_UO
    ,IM_RAP_A3_SPESE_COSTI_ALTRUI
    ,DACR
    ,UTCR
    ,DUVA
    ,UTUV
    ,PG_VER_REC
    ,CD_CENTRO_RESPONSABILITA_CLGS
    ,CD_LINEA_ATTIVITA_CLGS
    ,TI_APPARTENENZA_CLGS
    ,TI_GESTIONE_CLGS
    ,CD_ELEMENTO_VOCE_CLGS
    ,PG_SPESA_CLGS
    ,ESERCIZIO_PDG_VARIAZIONE
    ,PG_VARIAZIONE_PDG
   ) values (
     aDest.ESERCIZIO
    ,aDest.CD_CENTRO_RESPONSABILITA
    ,aDest.CD_LINEA_ATTIVITA
    ,aDest.CD_FUNZIONE
    ,aDest.CD_NATURA
    ,aDest.TI_APPARTENENZA
    ,aDest.TI_GESTIONE
    ,aDest.CD_ELEMENTO_VOCE
    ,aDest.PG_SPESA
    ,aDest.DT_REGISTRAZIONE
    ,aDest.DESCRIZIONE
    ,aDest.STATO
    ,aDest.ORIGINE
    ,aDest.CATEGORIA_DETTAGLIO
    ,aDest.FL_SOLA_LETTURA
    ,aDest.CD_CENTRO_RESPONSABILITA_CLGE
    ,aDest.CD_LINEA_ATTIVITA_CLGE
    ,aDest.TI_APPARTENENZA_CLGE
    ,aDest.TI_GESTIONE_CLGE
    ,aDest.CD_ELEMENTO_VOCE_CLGE
    ,aDest.PG_ENTRATA_CLGE
    ,aDest.IM_RH_CCS_COSTI
    ,aDest.IM_RI_CCS_SPESE_ODC
    ,aDest.IM_RJ_CCS_SPESE_ODC_ALTRA_UO
    ,aDest.IM_RK_CCS_SPESE_OGC
    ,aDest.IM_RL_CCS_SPESE_OGC_ALTRA_UO
    ,aDest.IM_RM_CSS_AMMORTAMENTI
    ,aDest.IM_RN_CSS_RIMANENZE
    ,aDest.IM_RO_CSS_ALTRI_COSTI
    ,aDest.IM_RP_CSS_VERSO_ALTRO_CDR
    ,aDest.IM_RQ_SSC_COSTI_ODC
    ,aDest.IM_RR_SSC_COSTI_ODC_ALTRA_UO
    ,aDest.IM_RS_SSC_COSTI_OGC
    ,aDest.IM_RT_SSC_COSTI_OGC_ALTRA_UO
    ,aDest.IM_RU_SPESE_COSTI_ALTRUI
    ,aDest.IM_RV_PAGAMENTI
    ,aDest.IM_RAA_A2_COSTI_FINALI
    ,aDest.IM_RAB_A2_COSTI_ALTRO_CDR
    ,aDest.IM_RAC_A2_SPESE_ODC
    ,aDest.IM_RAD_A2_SPESE_ODC_ALTRA_UO
    ,aDest.IM_RAE_A2_SPESE_OGC
    ,aDest.IM_RAF_A2_SPESE_OGC_ALTRA_UO
    ,aDest.IM_RAG_A2_SPESE_COSTI_ALTRUI
    ,aDest.IM_RAH_A3_COSTI_FINALI
    ,aDest.IM_RAI_A3_COSTI_ALTRO_CDR
    ,aDest.IM_RAL_A3_SPESE_ODC
    ,aDest.IM_RAM_A3_SPESE_ODC_ALTRA_UO
    ,aDest.IM_RAN_A3_SPESE_OGC
    ,aDest.IM_RAO_A3_SPESE_OGC_ALTRA_UO
    ,aDest.IM_RAP_A3_SPESE_COSTI_ALTRUI
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.DUVA
    ,aDest.UTUV
    ,aDest.PG_VER_REC
    ,aDest.CD_CENTRO_RESPONSABILITA_CLGS
    ,aDest.CD_LINEA_ATTIVITA_CLGS
    ,aDest.TI_APPARTENENZA_CLGS
    ,aDest.TI_GESTIONE_CLGS
    ,aDest.CD_ELEMENTO_VOCE_CLGS
    ,aDest.PG_SPESA_CLGS
    ,aDest.ESERCIZIO_PDG_VARIAZIONE
    ,aDest.PG_VARIAZIONE_PDG
    );

 end;


 procedure ins_PDG_PREVENTIVO (aDest PDG_PREVENTIVO%rowtype) is
  begin
   insert into PDG_PREVENTIVO (
     ESERCIZIO
    ,CD_CENTRO_RESPONSABILITA
    ,STATO
    ,ANNOTAZIONI
	,FL_RIBALTATO_SU_AREA
    ,DACR
    ,UTCR
    ,DUVA
    ,UTUV
    ,PG_VER_REC
   ) values (
     aDest.ESERCIZIO
    ,aDest.CD_CENTRO_RESPONSABILITA
    ,aDest.STATO
    ,aDest.ANNOTAZIONI
	,aDest.FL_RIBALTATO_SU_AREA
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.DUVA
    ,aDest.UTUV
    ,aDest.PG_VER_REC
    );
 end;

 procedure ins_PDG_ESERCIZIO (aDest PDG_ESERCIZIO%rowtype) is
  begin
   insert into PDG_ESERCIZIO (
     ESERCIZIO
    ,CD_CENTRO_RESPONSABILITA
    ,STATO
    ,DACR
    ,UTCR
    ,DUVA
    ,UTUV
    ,PG_VER_REC
   ) values (
     aDest.ESERCIZIO
    ,aDest.CD_CENTRO_RESPONSABILITA
    ,aDest.STATO
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.DUVA
    ,aDest.UTUV
    ,aDest.PG_VER_REC
    );
 end;

 procedure ins_PDG_AGGREGATO_SPE_DET (aDest PDG_AGGREGATO_SPE_DET%rowtype) is
  begin
   insert into PDG_AGGREGATO_SPE_DET (
     ESERCIZIO
    ,CD_CENTRO_RESPONSABILITA
	,CD_CDS
    ,TI_APPARTENENZA
    ,TI_GESTIONE
    ,CD_ELEMENTO_VOCE
    ,CD_FUNZIONE
    ,CD_NATURA
    ,TI_AGGREGATO
    ,IM_RH_CCS_COSTI
    ,IM_RI_CCS_SPESE_ODC
    ,IM_RJ_CCS_SPESE_ODC_ALTRA_UO
    ,IM_RK_CCS_SPESE_OGC
    ,IM_RL_CCS_SPESE_OGC_ALTRA_UO
    ,IM_RM_CSS_AMMORTAMENTI
    ,IM_RN_CSS_RIMANENZE
    ,IM_RO_CSS_ALTRI_COSTI
    ,IM_RP_CSS_VERSO_ALTRO_CDR
    ,IM_RQ_SSC_COSTI_ODC
    ,IM_RR_SSC_COSTI_ODC_ALTRA_UO
    ,IM_RS_SSC_COSTI_OGC
    ,IM_RT_SSC_COSTI_OGC_ALTRA_UO
    ,IM_RU_SPESE_COSTI_ALTRUI
    ,IM_RV_PAGAMENTI
    ,IM_RAA_A2_COSTI_FINALI
    ,IM_RAB_A2_COSTI_ALTRO_CDR
    ,IM_RAC_A2_SPESE_ODC
    ,IM_RAD_A2_SPESE_ODC_ALTRA_UO
    ,IM_RAE_A2_SPESE_OGC
    ,IM_RAF_A2_SPESE_OGC_ALTRA_UO
    ,IM_RAG_A2_SPESE_COSTI_ALTRUI
    ,IM_RAH_A3_COSTI_FINALI
    ,IM_RAI_A3_COSTI_ALTRO_CDR
    ,IM_RAL_A3_SPESE_ODC
    ,IM_RAM_A3_SPESE_ODC_ALTRA_UO
    ,IM_RAN_A3_SPESE_OGC
    ,IM_RAO_A3_SPESE_OGC_ALTRA_UO
    ,IM_RAP_A3_SPESE_COSTI_ALTRUI
    ,UTCR
    ,DACR
    ,UTUV
    ,DUVA
    ,PG_VER_REC
   ) values (
     aDest.ESERCIZIO
    ,aDest.CD_CENTRO_RESPONSABILITA
	,aDest.CD_CDS
    ,aDest.TI_APPARTENENZA
    ,aDest.TI_GESTIONE
    ,aDest.CD_ELEMENTO_VOCE
    ,aDest.CD_FUNZIONE
    ,aDest.CD_NATURA
    ,aDest.TI_AGGREGATO
    ,aDest.IM_RH_CCS_COSTI
    ,aDest.IM_RI_CCS_SPESE_ODC
    ,aDest.IM_RJ_CCS_SPESE_ODC_ALTRA_UO
    ,aDest.IM_RK_CCS_SPESE_OGC
    ,aDest.IM_RL_CCS_SPESE_OGC_ALTRA_UO
    ,aDest.IM_RM_CSS_AMMORTAMENTI
    ,aDest.IM_RN_CSS_RIMANENZE
    ,aDest.IM_RO_CSS_ALTRI_COSTI
    ,aDest.IM_RP_CSS_VERSO_ALTRO_CDR
    ,aDest.IM_RQ_SSC_COSTI_ODC
    ,aDest.IM_RR_SSC_COSTI_ODC_ALTRA_UO
    ,aDest.IM_RS_SSC_COSTI_OGC
    ,aDest.IM_RT_SSC_COSTI_OGC_ALTRA_UO
    ,aDest.IM_RU_SPESE_COSTI_ALTRUI
    ,aDest.IM_RV_PAGAMENTI
    ,aDest.IM_RAA_A2_COSTI_FINALI
    ,aDest.IM_RAB_A2_COSTI_ALTRO_CDR
    ,aDest.IM_RAC_A2_SPESE_ODC
    ,aDest.IM_RAD_A2_SPESE_ODC_ALTRA_UO
    ,aDest.IM_RAE_A2_SPESE_OGC
    ,aDest.IM_RAF_A2_SPESE_OGC_ALTRA_UO
    ,aDest.IM_RAG_A2_SPESE_COSTI_ALTRUI
    ,aDest.IM_RAH_A3_COSTI_FINALI
    ,aDest.IM_RAI_A3_COSTI_ALTRO_CDR
    ,aDest.IM_RAL_A3_SPESE_ODC
    ,aDest.IM_RAM_A3_SPESE_ODC_ALTRA_UO
    ,aDest.IM_RAN_A3_SPESE_OGC
    ,aDest.IM_RAO_A3_SPESE_OGC_ALTRA_UO
    ,aDest.IM_RAP_A3_SPESE_COSTI_ALTRUI
    ,aDest.UTCR
    ,aDest.DACR
    ,aDest.UTUV
    ,aDest.DUVA
    ,aDest.PG_VER_REC
    );
 end;

 procedure ins_PDG_AGGREGATO_ETR_DET (aDest PDG_AGGREGATO_ETR_DET%rowtype) is
  begin
   insert into PDG_AGGREGATO_ETR_DET (
     PG_VER_REC
    ,ESERCIZIO
    ,CD_CENTRO_RESPONSABILITA
    ,CD_NATURA
    ,TI_APPARTENENZA
    ,TI_GESTIONE
    ,CD_ELEMENTO_VOCE
    ,TI_AGGREGATO
    ,IM_RA_RCE
    ,IM_RB_RSE
    ,IM_RC_ESR
    ,IM_RD_A2_RICAVI
    ,IM_RE_A2_ENTRATE
    ,IM_RF_A3_RICAVI
    ,IM_RG_A3_ENTRATE
    ,UTCR
    ,DACR
    ,UTUV
    ,DUVA
   ) values (
     aDest.PG_VER_REC
    ,aDest.ESERCIZIO
    ,aDest.CD_CENTRO_RESPONSABILITA
    ,aDest.CD_NATURA
    ,aDest.TI_APPARTENENZA
    ,aDest.TI_GESTIONE
    ,aDest.CD_ELEMENTO_VOCE
    ,aDest.TI_AGGREGATO
    ,aDest.IM_RA_RCE
    ,aDest.IM_RB_RSE
    ,aDest.IM_RC_ESR
    ,aDest.IM_RD_A2_RICAVI
    ,aDest.IM_RE_A2_ENTRATE
    ,aDest.IM_RF_A3_RICAVI
    ,aDest.IM_RG_A3_ENTRATE
    ,aDest.UTCR
    ,aDest.DACR
    ,aDest.UTUV
    ,aDest.DUVA
    );
 end;

 procedure ins_ASS_PDG_VARIAZIONE_CDR (aDest ASS_PDG_VARIAZIONE_CDR%rowtype) Is
  begin
   Insert Into ASS_PDG_VARIAZIONE_CDR (
     ESERCIZIO
    ,PG_VARIAZIONE_PDG
    ,CD_CENTRO_RESPONSABILITA
    ,IM_ENTRATA
    ,IM_SPESA
    ,DACR
    ,UTCR
    ,DUVA
    ,UTUV
    ,PG_VER_REC
   ) values (
     aDest.ESERCIZIO
    ,aDest.PG_VARIAZIONE_PDG
    ,aDest.CD_CENTRO_RESPONSABILITA
    ,aDest.IM_ENTRATA
    ,aDest.IM_SPESA
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.DUVA
    ,aDest.UTUV
    ,aDest.PG_VER_REC
    );
 end;

 procedure ins_PDG_AGGREGATO (aDest PDG_AGGREGATO%rowtype) is
  begin
   insert into PDG_AGGREGATO (
     ESERCIZIO
    ,CD_CENTRO_RESPONSABILITA
    ,STATO
    ,UTCR
    ,DACR
    ,UTUV
    ,DUVA
    ,PG_VER_REC
   ) values (
     aDest.ESERCIZIO
    ,aDest.CD_CENTRO_RESPONSABILITA
    ,aDest.STATO
    ,aDest.UTCR
    ,aDest.DACR
    ,aDest.UTUV
    ,aDest.DUVA
    ,aDest.PG_VER_REC
    );
 end;

end;
/


