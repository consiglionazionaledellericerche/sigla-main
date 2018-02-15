--------------------------------------------------------
--  DDL for Package CNRCTB053
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRCTB053" as
--
-- CNRCTB053 - Package per la gestione funzioni cross PDG / BILANCIO PREV
-- Date: 22/12/2003
-- Version: 3.1
--
--
-- Dependency: CNRCTB 000/008/010/020/050/054/055 IBMERR 001
--
-- History:
--
-- Date: 01/10/2001
-- Version: 1.3
-- Creazione
--
-- Date: 16/10/2001
-- Version: 1.4
-- Gestione del tipo di unità organizzativa ENTE
--
-- Date: 21/10/2001
-- Version: 1.5
-- Fix su ribaltamento verso area
--
-- Date: 31/10/2001
-- Version: 1.6
-- Fix errori
--
-- Date: 08/11/2001
-- Version: 1.7
-- Eliminazione esercizio da STO
--
-- Date: 29/11/2001
-- Version: 1.8
-- Ribaltamento su area di tutta la linea sotto il cdr di I livello
--
-- Date: 12/12/2001
-- Version: 1.9
-- Fix errore di costruzione della colonna P sul dettaglio origine, al posto della colonna P veniva usata la V
--
-- Date: 29/12/2001
-- Version: 2.0
-- Lettura tramite CNRCTB000 dei conti speciali
--
-- Date: 21/02/2002
-- Version: 2.1
-- Spaccato CNRCTB055 su CNRCTB054
--
-- Date: 27/02/2002
-- Allineamento con versione 2.0.1 di evolutiva/correttiva
-- 2.0.1 Azzero i costi (H) e le colonne che li determinano (I,J,K,L) in fase di ribaltamento nel dettaglio di origine
--
-- Date: 28/02/2002
-- Version: 2.2
-- Allineamento con versione 2.0.2 di evolutiva/correttiva
-- 2.0.2 Gestione di TI_GESTIONE in creazione linea di attività AUTOMATICA
--
-- Date: 28/02/2002
-- Version: 2.3
-- Allineamento con versione 2.0.3 di evolutiva/correttiva
-- 2.0.3 Aggiornamento dello stato di ribaltamento del CDR DI PRIMO LIVELLO indipendentemente dal fatto che lui
-- sia collegato all'AREA.
-- I CDR figli vengono marcati come ribaltati solo se collegati ad AREA (anche se non hanno effettivamente scaricato
-- dettagli sull'AREA).
--
-- Date: 07/03/2002
-- Version: 2.4
-- Allineamento con versione 2.0.4 di evolutiva/correttiva
-- Errore in calcolo del massimo pg dettaglio di spesa in CDR AREA corrispondente a quello di spesa in servito
--
-- Date: 18/03/2002
-- Version: 2.5
-- Allineamento con versione 2.0.5 di evolutiva/correttiva
-- Gestione dell'insieme sullo scarico verso area
--
-- Date: 18/04/2002
-- Version: 2.6
-- Introduzione funzione e natura su dettaglio del piano di gestione
--
-- Date: 10/06/2002
-- Version: 2.7
-- Nuovo meccanismo di ribaltamento sull'area:
-- Il dettaglio di natura 5 viene trasferito in AREA ereditando anche eventuali collegamenti
-- che il dettaglio di origine aveva con un eventuale CDR servente.
-- A parte le colonne dei COSTI verso altro CDR (su cui vengono scaricati i costi presenti nelo dettaglio del servito), gli
-- altri importi del dettaglio servito vengono tutti annullati.
-- A livello di quadratura Entrate figurative/Spese, viene utilizzato l'insieme automatico e le modalità rimangono quelle
-- precedenti.
--
-- Date: 17/06/2002
-- Version: 2.8
-- Fix errori
--
-- Date: 18/07/2002
-- Version: 2.9
-- Aggiornamento documentazione
--
-- Date: 30/12/2002
-- Version: 3.0
-- Gestione ribaltamento su area er variazioni a PDG preventivo
-- Fix errore su categoria_dettaglio creato in AREA nel caso il dettaglio originale fosse di SCARICO, veniva creato un
-- Dettaglio singolo
--
-- Date: 22/12/2003
-- Version: 3.1
-- Fix controllo su CDR AREA in scarico verso costi su area
-- a. in variazione se su area ritorna senza sollevare eccezione
-- b. in scarico iniziale solleva eccezione
--
-- Constants:
--
-- Functions e Procedures:
--
--
-- Ribalta il pdg del cdr di I livello e figli sull'area
--
-- pre-post-name: Cdr non di primo livello
-- pre: Il CDR specificato non è di primo livello
-- post: Viene sollevata un'eccezione
--
-- pre-post-name: Cdr specificato appartenente ad AREA
-- pre: Il CDR specificato appartiene a CDS AREA
-- post: Viene sollevata un'eccezione
--
-- pre-post-name: Verifica che il PDG del CDR non sia già stato ribaltato sull'area
-- pre: Il PDG del cdr è già stato ribaltato sull'area (fl_ribaltato_su_area = 'Y')
-- post: Viene sollevata un'eccezione
--
-- pre-post-name: Verifica che il Bilancio finanziario dell'ENTE sia stato approvato
-- pre: Il bilancio finanziario dell'ente NON è stato ancora approvato
-- post: Viene sollevata un'eccezione
--
-- pre-post-name: Verifica che il pdg del cdr specificato sia in stato di chiusura definitiva (F)
-- pre: Il pdg del CDR specificato non è in stato di chiusura definitiva
-- post: Viene sollevata un'eccezione
--
-- pre-post-name: Ribaltamento su area
-- pre: Nessuna delle condizioni precedenti è verificata
-- post:
-- Vengono estratti tutti i PDG della linea del CDR di primo livello
-- Per ogni PDG di CDR "CDRLINEA" afferente ad AREA tramite unità organizzativa di afferenza:
--     Estraggo il conto di ricavo figurativo collegato
--     Ciclo sui dettagli del PDG con natura 5 di "CDRLINEA"
--       Viene letta la linea di attività del dettaglio del centro servito
--       Ricerca o creazione della nuova linea di attività nel servente AREA
--      Viene slezionata la linea di attività di entrata CSSAC nel CDR AREA
--          Se tale linea non viene trovata, viene creata nel CDR AREA
--          Viene automaticamente creato un insieme di linee di attività per legare il ricavo figurativo alla spalmatura in spesa di tale ricavo in AREA
--      Viene creato il dettaglio di ricavo figurativo nel servente AREA valorizzando le colonne come segue:
--          IM_RB_RSE:=
--              IM_RH_CCS_COSTI
--            +IM_RM_CSS_AMMORTAMENTI
--            +IM_RN_CSS_RIMANENZE
--            +IM_RO_CSS_ALTRI_COSTI
--
--           IM_RD_A2_RICAVI:=
--             IM_RAA_A2_COSTI_FINALI
--
--           IM_RF_A3_RICAVI:=
--             IM_RAH_A3_COSTI_FINALI
--
--          IM_RA_RCE:=0;
--          IM_RC_ESR:=0;
--          IM_RE_A2_ENTRATE:=0;
--          IM_RG_A3_ENTRATE:=0;
--   Il dettaglio creato viene collegato al dettagllio di spesa di natura 5 del centro servito
--   Viene utilizzata la voce del piano di entrata CSSAC
--   Viene aggiornato il dettaglio del centro servito collegandolo a quello di ricavo del centro servente AREA
--   Viene cercata nel sevrvente la linea di attività collegata a quella del servito.
--        Se tale linea di attività non viene trovata, viene creata automaticamente ereditando funz./natura da quella originale e viene legata
--        a quella CSSAC del ricavo figurativo attraverso l'insieme precedentemente creato.
--   Viene creato nell'AREA un dettaglio di spesa simmetrico a quello del CDR che ribalta che utilizza la linea di attività collegata a quella del servito.
--   Se tale linea di attività già esiste viene utilizzata altrimenti creata contestualmente.
--  Gli importi di tale dettaglio sono impostati con i corrispondenti importi del dettaglio di spesa del CDR che ribalta prima del ribaltamento
-- Tale dettaglio viene valorizzato con gli come segue:
--  Se il dettaglio di spesa del CDR che ribalta ha effettuato uno scarico verso altra UO, viene spostato tale riferimento sul dettaglio di spesa simmetrico creato nell'AREA.
-- Il dettaglio di spesa del CDR che ribalta viene sganciato da eventuale CDR collegato per scarico verso altra UO.
-- Gli importi del dettaglio del CDR che ribalta sono modificati come segue:
--
--           IM_RP_CSS_VERSO_ALTRO_CDR=
--              IM_RH_CCS_COSTI
--            +IM_RM_CSS_AMMORTAMENTI
--            +IM_RN_CSS_RIMANENZE
--            +IM_RO_CSS_ALTRI_COSTI
--
--          IM_RAB_A2_COSTI_ALTRO_CDR=
--             IM_RAA_A2_COSTI_FINALI
--
--          IM_RAI_A3_COSTI_ALTRO_CDR=
--             IM_RAH_A3_COSTI_FINALI
--
--          IM_RH_CCS_COSTI=0
--          IM_RI_CCS_SPESE_ODC=0
--          IM_RJ_CCS_SPESE_ODC_ALTRA_UO=0
--          IM_RK_CCS_SPESE_OGC=0
--          IM_RL_CCS_SPESE_OGC_ALTRA_UO=0
--          IM_RM_CSS_AMMORTAMENTI=0
--          IM_RN_CSS_RIMANENZE=0
--          IM_RO_CSS_ALTRI_COSTI=0
--          IM_RQ_SSC_COSTI_ODC=0
--          IM_RR_SSC_COSTI_ODC_ALTRA_UO=0
--          IM_RS_SSC_COSTI_OGC=0
--          IM_RT_SSC_COSTI_OGC_ALTRA_UO=0
--          IM_RU_SPESE_COSTI_ALTRUI=0
--          IM_RV_PAGAMENTI=0
--
--          IM_RAA_A2_COSTI_FINALI=0
--          IM_RAC_A2_SPESE_ODC=0
--          IM_RAD_A2_SPESE_ODC_ALTRA_UO=0
--          IM_RAE_A2_SPESE_OGC=0
--          IM_RAF_A2_SPESE_OGC_ALTRA_UO=0
--          IM_RAG_A2_SPESE_COSTI_ALTRUI=0
--
--          IM_RAH_A3_COSTI_FINALI=0
--          IM_RAL_A3_SPESE_ODC=0
--          IM_RAM_A3_SPESE_ODC_ALTRA_UO=0
--          IM_RAN_A3_SPESE_OGC=0
--          IM_RAO_A3_SPESE_OGC_ALTRA_UO=0
--          IM_RAP_A3_SPESE_COSTI_ALTRUI=0
--
--
-- Al termine dello scarico su area di un CDR viene aggiornato a Y il flag fl_ribaltato_su_area
-- Se almeno un CDR appartenente alla linea del CDR di primo livello è stato ribaltato su AREA viene aggiornato a Y il flag fl_ribaltato_su_area del piano di gestione del CDR di primo livello.
--
--
--
-- Parametri:
--     aEsercizio -> esercizio
--     aCdCentroResponsabilita -> codice centro di responsabilita di primo livello
--     aUser -> utente che attiva l'operazione

 procedure ribaltaSuAreaPDG(
  aEsercizio number,
  aCdCentroResponsabilita varchar2,
  aUser varchar2
 );

-- FUNZIONE CHE, DATO L'ELEMENTO VOCE E LA LINEA, RESTITUISCE LA VOCE LUNGA
-- VALIDA SOLO PER ENTRATE CNR E SPESE CDS (ENTRATE E SPESE REALI)

Function getVoce_FdaEV (aEV elemento_voce%Rowtype, aLinea_attivita linea_attivita%Rowtype) Return  voce_f%Rowtype;

Function getVoce_FdaEV (aEsercizio NUMBER, aTI_APPARTENENZA CHAR, aTI_GESTIONE CHAR, aCD_ELEMENTO_VOCE VARCHAR2,
                        acd_centro_responsabilita VARCHAR2, acd_linea_attivita VARCHAR2) Return  VARCHAR2;

-- 28.11.2005 Nuova Gestione PdG: dal 2006 si ribalta il Gestionale

 procedure ribaltaSuAreaPDG_da_gest(
  aEsercizio number,
  aCdCentroResponsabilita varchar2,
  aUser varchar2
 );

-- 27.04.2006 Nuova Gestione Variazioni al PdG 2006: Ribaltamento delle variazioni

 procedure ribaltaSuAreaPDG_da_gest_var(
  aEsercizio number,
  aPG_VARIAZIONE_PDG NUMBER,
  aUser varchar2
 );


-- creazione dei saldi cdr/linea/voce a partire dal gestionale approvato

 procedure creasaldicdrlineavocedagest(
  aEsercizio number,
  aCdCentroResponsabilita varchar2,
  aUser varchar2
 );


-- Ribaltamento su area per variazioni a PDG preventivo, non viene gestito il flag ribaltato su area in questo caso
-- Se applicato su area non sortisce alcun effetto
--
-- Parametri:
--     aEsercizio -> esercizio
--     aCdCentroResponsabilita -> codice centro di responsabilita di primo livello
--     aUser -> utente che attiva l'operazione

 procedure ribaltaSuAreaPDGVar(
  aEsercizio number,
  aCdCentroResponsabilita varchar2,
  aUser VARCHAR2,
  aEsVar NUMBER Default Null,
  aNumVar NUMBER Default Null
 );

-- 28.04.2006 genera la variazione di bilancio ente da variazione al pdg approvata

Procedure genera_varente_da_Var_pdg (aEsercizio NUMBER, aPG_VARIAZIONE NUMBER, aUser VARCHAR2,
                                     cds_var_bil    Out VARCHAR2, es_var_bil Out NUMBER, ti_app_var_bil Out CHAR, pg_var_bil Out NUMBER);

-- 08.02.2007 Inserita procedura che valorizza il flag sulle righe delle variazioni per gestione Visto Dipartimentale

Procedure setdavistaredip (
  aEsercizio         pdg_variazione.esercizio%Type,
  aPg_variazione_pdg Pdg_variazione.pg_variazione_pdg%Type
 );

Procedure setdavistaredip (apdg_variazione pdg_variazione%Rowtype);
-- 21/12/2010 RosPuc Nuova procedura per aggiornamento limite spesa
Procedure aggiornaLimiteSpesa(aElVoce elemento_voce%Rowtype,aDett Voce_f_saldi_cdr_linea%Rowtype,aUser VARCHAR2);
-- 24/02/2011 Rospuc aggiornamento limite spesa per variazioni a competenze e a residuo
Procedure aggiornaLimiteSpesaVAR(aEsercizio number,apg_variazione number,tipo VARCHAR2,aUser VARCHAR2);
Procedure aggiornaLimiteSpesaImp(aDett elemento_voce%rowtype,aEsercizio number,
                                     fonteLinea natura.tipo%type,cds String, im_variazione number,aUser VARCHAR2);

-- 19/09/2011 Procedura per l'aggiornamento dei Limiti di spesa in fase decisionale
Procedure aggiornaLimiteSpesaDec(aEsercizio number,cdr VARCHAR2,stato VARCHAR2,aUser VARCHAR2);
End;
