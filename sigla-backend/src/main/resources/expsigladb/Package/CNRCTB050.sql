--------------------------------------------------------
--  DDL for Package CNRCTB050
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRCTB050" as
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
-- Aggiunta gestione tipo unitא ENTE
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
-- Check di quadratura insieme linea di attivitא
--
-- Date: 23/04/2002
-- Version: 3.5
-- Fix su check di quadratura insieme linea di attivitא
--
-- Date: 24/04/2002
-- Version: 3.6
-- Modificata interfaccia di ritorno procedura -> function check quadratura insieme linea di attivitא
--
-- Date: 29/04/2002
-- Version: 3.7
-- Modifica inizializzazione aggregato -> aggiorna la colonna dei costi altrui sul modificato dal centro
-- ad ogni generazione con l'importo proveniente dai piani di gestione.
-- Tale importo non ט contrattabile
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
-- non sia giא stato scaricato verso altro CDR (il cdr area appunto)
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
-- aCdCdr -> cd centro responsabilitא
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
-- aCdCdr -> cd centro responsabilitא

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
-- aCdCdr -> cd centro responsabilitא

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
-- pre:  Il CDS specificato non ט l'ENTE
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
-- l'operazione viene condotta secondo le seguenti modalitא:
--
-- pre-post-name: Creazione primo aggregato
-- pre: il CDR specificato ט un CDR valido nell'esercizio specificato Viene richiesta la creazione del primo aggregato.
-- post: Viene creata la testata dell'aggregato in stato iniziale, quindi vengono creati i dettagli dell'aggregato secondo le seguenti modalitא:
-- *per la parte spese viene vengono inserite nella tabella degli aggregati tutte le possibili combinazioni dei parametri di aggregazione.
-- **per MACROISTITUTI e AREE: titoli cds x funzioni x nature x cds (1+m dove m ט il numero di aree di ricerca di cui il CDS di appartenenza del CDR di primo livello ט Presidente)
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
-- pre: il CDR specificato ט un CDR valido nell'esercizio specificato. Viene richiesta la creazione del' n-esimo aggregato (n>1).
-- post: I dettagli di tipo Iniziale creati alla prima aggregazione vengono totalmente eliminati e sostituiti con quelli calcolati aggregando i dati correnti dai piani di gestione scondo le regole stabilite nella pre-post "Creazione primo aggregato".
-- Vengono aggiornate le informazioni di sistema della testata dell'aggregato
--
-- pre-post-name: CDR non valido nell'esercizio specificato
-- pre: il CDR specificato non ט un CDR valido nell'esercizio specificato
-- post: Viene sollevata un'eccezione applicativa
--
-- Parametri:
-- aEs -> esercizio
-- aCdCdr -> Centro di responsabilitא su cui viene effettuata l'operazione
-- aUser -> Utente che effettua l'operazione
--
procedure inizializzaAggregatoPDG(aEs number, aCdCdr varchar2, aUser varchar2);

procedure inizializzaAggregatoPDG(aEs number, aCdCdr varchar2, aUser VARCHAR2, daVariazione CHAR);

-- Verifica che il piano di gestione aggregato del cdr di primo livello o resp. di area a cui il CDR specificato afferisce sia chiuso in stato B
--
-- pre-post-name: Il Piano di gestione aggregato del CDR di primo livello o resp. di AREA a cui il CDR specificato afferisce non ט chiuso
-- pre:  Il Piano di gestione aggregato del CDR di primo livello o AREA a cui il CDR specificato afferisce non ט chiuso o non esiste
-- post: Ritorna 'N'
--
-- pre-post-name: Nessuna altra pre condizione verificata
-- pre: Nessun'altra precondizione ט verificata
-- post: Ritorna 'Y'
--
-- Parametri:
-- aEs -> esercizio
-- aCdCdr -> Centro di responsabilitא su cui effettuare il controllo

 function checkAggregatoChiuso(aEs number, aCdCdr varchar2) return char;

-- Verifica che il piano di gestione aggregato del cdr di primo livello o resp. di area a cui il CDR specificato afferisce sia chiuso in stato B
--
-- pre-post-name: Il Piano di gestione aggregato del CDR di primo livello o resp. di AREA a cui il CDR specificato afferisce non ט chiuso
-- pre:  Il Piano di gestione aggregato del CDR di primo livello o AREA a cui il CDR specificato afferisce non ט chiuso o non esiste
-- post: Ritorna 'N'
--
-- pre-post-name: Nessuna altra pre condizione verificata
-- pre: Nessun'altra precondizione ט verificata
-- post: Ritorna 'Y'
--
-- Parametri:
-- aEs -> esercizio
-- aCdCdr -> Centro di responsabilitא su cui effettuare il controllo

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
 -- aCdCdr -> codice del centro di responsabilitא

 procedure lockPdg(aEs number, aCdCdr varchar2);

-- Nuova gestione PdG Gestionale
 procedure lockpdg_esercizio(aEs number, aCdCdr varchar2);

 -- Ritorna lo stato del piano del CDR aCDR
 --
 -- aEs -> esercizio
 -- aCdCdr -> codice del centro di responsabilitא

 function getStato(aEs number, aCdCdr varchar2) return varchar2;

 -- Ritorna lo stato del piano del CDR aCDR (DA PDG_ESERCIZIO, NUOVA GESTIONE 2006)
 --
 -- aEs -> esercizio
 -- aCdCdr -> codice del centro di responsabilitא


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
-- pre:  Esiste quadratura tra spese ed entrate collegate attraverso l'insieme di linea di attivitא specificata in dettaglio del pdg.
--            Il CDR ט un CDR esistente e valido nell'esercizio
-- post: Ritorna 'Y'
--
-- pre-post-name: Non esiste quadratura dei ricavi figurativi
-- pre: Non esiste quadratura dei ricavi figurativi
--            Il CDR ט un CDR esistente e valido nell'esercizio
-- post: Ritorna 'N'
--
-- pre-post-name: Nessuna altra pre condizione verificata
-- pre: Nessun'altra precondizione ט verificata
-- post: Ritorna 'Y'
--
-- Parametri:
-- aEs -> esercizio
-- aCdCdr -> Centro di responsabilitא su cui effettuare il controllo

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

-- Controlla che non ci siano discrepanze tra entrate e spese legate all'insieme di linee di attivitא
--
-- pre-post-name: Esistono discrepanze su insieme di linea di attivitא
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
--       dove aPdgAgg ט la riga di aggregato derivante dai piani di gestione relativa alla riga di aggregato
--       del centro in processo.
-- post: Viene sollevata un'eccezione
--
-- Parametri
--   aEs -> esercizio contabile
--   aCdCdrPrimo -> codice del cdr di primo livello su cui effetuare il controllo

 procedure checkAttualizzScrAltraUo(aEs number, aCdCdrPrimo varchar2);

-- Estrazione stato aggregato corrispondente a PDG del CDR specificato

-- Pre-post-name: Aggregato non esistente relativo al CDR specificato
-- pre: L'aggregato corrispondente al PDG del CDR specificato non ט stato ancora creato
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
-- pre: Il cdr non ט valido nell'esercizio specificato
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
