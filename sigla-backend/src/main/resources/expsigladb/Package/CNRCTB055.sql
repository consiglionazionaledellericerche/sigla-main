--------------------------------------------------------
--  DDL for Package CNRCTB055
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRCTB055" as
--
-- CNRCTB055 - Package di gestione dei saldi in (Previsione consuntivo in esercizio)
-- Date: 10/02/2004
-- Version: 5.16
--
-- Dependency: CNRCTB 001/008/020/030/050/054 IBMERR 001
--
-- History:
--
-- Date: 01/09/2001
-- Version: 1.0
-- Creazione
-- Date: 26/09/2001
-- Version: 1.1
-- Fix errori, completata la derivazione del bilancio finanziario CDS
-- Aggiunto l'utente che effettua l'operazione come parametro di ingresso ad alcuni metodi
-- Aggiunto il metodo di inserimento automatico della testata del bilancio preventivo
--
-- Date: 27/09/2001
-- Version: 1.2
-- Aggiunti controlli per l'effetuazione delle operazione di produzione dei bilanci preventivi
--
-- Date: 02/10/2001
-- Version: 1.3
-- Correzione per gestione scarico aree e nature 5 su CDS presidente di aree
--
-- Date: 16/10/2001
-- Version: 1.4
-- Gestione del tipo unità organizzativa ENTE
--
-- Date: 29/10/2001
-- Version: 1.5
-- Fix errori + gestione dell'accumulo degli aggregati SAC + aggregati = conto nature diverse per la parte entrate CNR
--
-- Date: 30/10/2001
-- Version: 1.6
-- fix errori
--
-- Date: 31/10/2001
-- Version: 1.7
-- fix errori: sollevato problema strutturale su tipologie di intervento e SAC presidente dell'area
--
-- Date: 08/11/2001
-- Version: 1.8
-- Eliminazione eserercizio da STO
--
-- Date: 26/11/2001
-- Version: 1.9
-- Fix errori
--
-- Date: 30/11/2001
-- Version: 2.0
-- Errori di generazione della testata del bilancio preventivo CNR
--
-- Date: 05/12/2001
-- Version: 2.1
-- Nuova derivazione del bilancio CDS parte entrate per nuova associazione titolo spesa CNR + tipo CDS + natura => capitolo entrata CNR
--
-- Date: 06/12/2001
-- Version: 2.2
-- Fix errore di estrazione del ASS_EV_EV in predisp. bil CDS
--
-- Date: 12/12/2001
-- Version: 2.3
-- Fix su importi pluriennali in predisposizione bilancio CDS: non devono esserci
--
-- Date: 26/12/2001
-- Version: 2.4
-- Modifica produz. bilancio fin. ente per reiterazione del processo.
--
-- Date: 10/01/2002
-- Version: 2.5
-- Abilitazioni per permettere la reiterazione di produzione bil. fin. CNR per capitoli di parte 1 ed entrate
-- Modifica modalità di predisposizione stanziamenti su capitoli CDS parte 1 ed entrate per permettere la modifica pre-predisposizione
--
-- Date: 10/01/2002
-- Version: 2.6
-- Fix errori
--
-- Date: 11/01/2002
-- Version: 2.7
-- Errore in predisposizione del bilancio CNR - entrava in associazione tra titolo cds/cnr dalla parte sbagliata
--
-- Date: 13/01/2002
-- Version: 2.8
-- Fix errore su predisposizione bil. fin cds: scartava dalla predisposizione l'AREA al posto dell'ENTE
-- Gestione dei CDR non rubrica del SAC
--
-- Date: 17/01/2002
-- Version: 2.9
-- Nuove procedure di inserimento dei saldi iniziali competenza/residui
--
-- Date: 18/01/2002
-- Version: 3.0
-- Completamento gestione inizializzazione dei saldi
--
-- Date: 18/01/2002
-- Version: 3.1
-- Gestione della modificabilità dei dettagli dopo l'ultima produzione del bilancio finanziario CNR dagli aggregati
--
-- Date: 26/01/2002
-- Version: 3.2
-- Fix su esplosione saldi cmp -> non gestito esercizio 0
--
-- Date: 18/02/2002
-- Version: 3.3
-- Fix errore di recupero dell'associazione tra titolo di spesa CDS e titolo di spesa CNR (completamento fix 2.7)
--
-- Date: 21/02/2002
-- Version: 3.4
-- Spostate in CNRCTB054 le azioni direttamente legate alle tabelle e le costanti + creazione impegni CNR
--
-- Date: 26/02/2002
-- Version: 3.5
-- Allineamento con versione 3.3.2 di evolutiva/correttiva
-- 3.3.1 Fix errore di recupero dell'associazione tra titolo di spesa CDS e titolo di spesa CNR (completamento fix 2.7)
-- 3.3.2 fix produzione bilancio CNR parte entrate
--
-- Date: 27/02/2002
-- Version: 3.6
-- Allineamento con versione 3.3.4 di evolutiva/correttiva
-- 3.3.3 Fix errori in derivazione bilancio CDS
-- 3.3.4 Fix mancanza controllo bilancio cnr approvato per pred. bil CDS
--
-- Date: 09/04/2002
-- Version: 3.7
-- Aggiunto controllo che esercizio per ENTE sia APERTO per predisposizione bilancio finanziario CNR
--
-- Date: 23/04/2002
-- Version: 3.8
-- Fix in pred. bi. fin. cds -> Il loop di controllo sui CDR deve essere fatto su quelli validi
-- Aggiunta modifica utuv, duva, pg_ver su testata bilancio fin. cds dopo predisposiz.
-- Aggiunta modifica utuv, duva, pg_ver su testata bilancio fin. cnr ad ogni produzione anche cquelle che non cambiano lo stato
-- Se lo stato del preventivo CNR non è quello iniziale, non è possibile produrre il bilancio finanziario CNR
--
-- Date: 30/05/2002
-- Version: 3.9
-- Controlli su variazioni di bilancio + esito variazione bilancio
--
-- Date: 04/06/2002
-- Version: 4.0
-- fix errore di calcolo della variazione + gestione segno in variazione
--
-- Date: 18/06/2002
-- Version: 4.1
-- variazione automatica per spalmatura entrate bilancio CNR
--
-- Date: 19/06/2002
-- Version: 4.2
-- Sul SAC la contrattazione in spesa viene fatta per tipologia di intervento: ciò modifica le regole di deriv. del bil prev
--
-- Date: 19/06/2002
-- Version: 4.3
-- Test su ripartizione automatica entrate
--
-- Date: 20/06/2002
-- Version: 4.4
-- Aggiunto controllo su stato pdg in ripartizione automatica entrate
--
-- Date: 20/06/2002
-- Version: 4.5
-- Tolti i savepoints
--
-- Date: 01/07/2002
-- Version: 4.6
-- Modificato il ome del metodo di creazione della ripartizione delle entrate
--
-- Date: 09/07/2002
-- Version: 4.7
-- Fix errore su ripartizione automatica entrate CNR
--
-- Date: 17/07/2002
-- Version: 4.8
-- Aggiornamento documentazione
--
-- Date: 08/08/2002
-- Version: 4.9
-- Controllo pareggio di bilancio CDS
--
-- Date: 09/08/2002
-- Version: 5.0
-- Fix errore riparizione entrateper non azzeramento del totale accumulato per UO aggregante
-- nel caso esistesse più di una UO
--
-- Date: 20/08/2002
-- Version: 5.1
-- Variazioni di bilancio pluriennali e ripartizione quote pluriennali
--
-- Date: 28/08/2002
-- Version: 5.2
-- Aggiunti controlli su modifiche stanziamenti spese/entrate CDS
--
-- Date: 02/09/2002
-- Version: 5.3
-- Controllo copertura del 3%
--
-- Date: 06/09/2002
-- Version: 5.4
-- Sistemato il controllo su spese di funzionamento secondo specifica
--
-- Date: 05/11/2002
-- Version: 5.5
-- Nuova funzione di controllo bloccante pareggio bilancio CDS
-- Nessun controllo bloccante su spese CNR < entrate CDS corrispondenti
--
-- Date: 03/12/2002
-- Version: 5.6
-- Batchizzazione della produzione del bilancio finanzirio CNR
--
-- Date: 13/12/2002
-- Version: 5.7
-- Fix su ripartizione automatica delle entrate
-- Completamento query du fondo ricerca e titolo funzionamento CDS
--
-- Date: 29/01/2003
-- Version: 5.8
-- Targa su log di predisposizione bilancio finanziario CNR
--
-- Date: 06/03/2003
-- Version: 5.9
-- Fix errore di non modifica dell'impegno su capitolo (spesa CNR parte 1) nel caso di variazione di bilancio
-- Fix anche su checkVariazioneBilancio -> sulle spese CNR di parte 1 verifica la disponibilità di CASSA
-- Fix su checkVariazioneBilancio -> sulle spese CDS verifica la disponibilità di CASSA (e non di COMPETENZA)
--
-- Date: 18/03/2003
-- Version: 5.10
-- Gestione del commit DEFERRED in predisposizione bilancio finanziario CNR
--
-- Date: 02/02/2004
-- Version: 5.11
-- Gestione del Avanzo/Disavanzo di Gestione.
--
-- Date: 05/02/2004
-- Version: 5.12
-- Aggiornamento avanzo di gestione in tabella esercizio su variazione di bilancio
--
-- Date: 06/02/2004
-- Version: 5.13
-- Aggiunti controlli su validità dei capitoli di configurazione, e lockate varie tabelle
--  in setcassainiassucc.
--
-- Date: 09/02/2004
-- Version: 5.14
-- Controlli aggiuntivi su relazione tra esercizio / cds e stato esercizio precedente per il cds
--
-- Date: 09/02/2004
-- Version: 5.15
-- Gestione controllo di non imputazione contemporanea di spese ed entrate  capitoli di avanzo in gestione CNR
-- nell'esito della variazione di bilancio
--
-- Date: 10/02/2004
-- Version: 5.16
-- Gestione controllo di non imputazione contemporanea di spese ed entrate  capitoli di avanzo anche su ENTE
-- nell'esito della variazione di bilancio
--
-- Constants:

LOG_TIPO_PREDBILFIN CONSTANT VARCHAR2(20):='PROD_BILFINCNR00';

-- Cursore sugli aggregati delle spese per rubrica
--
-- aEs esercizio
-- aCDR cdr in processo

Procedure predispBilFinCNRInt_da_gest(aEsercizio NUMBER, aCdR VARCHAR2, aUtente varchar2);

cursor DETTAGLI_PDG_SPESA_PER_RUBRICA(aEs number, aCDR cdr%rowtype) RETURN V_PDG_SPESE_RUBRICA%ROWTYPE is (
                select * from
                         V_PDG_SPESE_RUBRICA
				where
				     esercizio = aEs
				 and cd_centro_responsabilita = aCDR.cd_centro_responsabilita);

-- Cursore su join tra VOCE_F_SALDI_CMP e VOCE per l'estrazione dei sottoarticoli di interesse
--
-- aEs esercizio
-- aCDS deve essere l'UO CDS del CDS in processo

/* stani 18.06.2009
cursor IMPORTI_SPESA_CNR_PER_ETR_CDS(aEs number, aCdCDS varchar2) RETURN V_BIL_PREV_SOTTOARTICOLI_CNR%ROWTYPE is (
                select *
				from
                 V_BIL_PREV_SOTTOARTICOLI_CNR
				where
                     esercizio = aEs
				 and cd_cds = aCdCDS);
*/

-- Cursore sugli aggregati delle spese per costituzione parte spese bilancio finanziario CNR

cursor DETTAGLI_PDG_AGGREGATO_SPE(aEsercizio varchar2) RETURN PDG_AGGREGATO_SPE_DET%ROWTYPE is (
                select * from
                         PDG_AGGREGATO_SPE_DET
				where
				         esercizio = aEsercizio
					 and ti_aggregato = 'M'
					 and (
                              IM_RH_CCS_COSTI!=0
                           or IM_RI_CCS_SPESE_ODC!=0
                           or IM_RJ_CCS_SPESE_ODC_ALTRA_UO!=0
                           or IM_RK_CCS_SPESE_OGC!=0
                           or IM_RL_CCS_SPESE_OGC_ALTRA_UO!=0
                           or IM_RM_CSS_AMMORTAMENTI!=0
                           or IM_RN_CSS_RIMANENZE!=0
                           or IM_RO_CSS_ALTRI_COSTI!=0
                           or IM_RP_CSS_VERSO_ALTRO_CDR!=0
                           or IM_RQ_SSC_COSTI_ODC!=0
                           or IM_RR_SSC_COSTI_ODC_ALTRA_UO!=0
                           or IM_RS_SSC_COSTI_OGC!=0
                           or IM_RT_SSC_COSTI_OGC_ALTRA_UO!=0
                           or IM_RU_SPESE_COSTI_ALTRUI!=0
                           or IM_RV_PAGAMENTI!=0
                           or IM_RAA_A2_COSTI_FINALI!=0
                           or IM_RAB_A2_COSTI_ALTRO_CDR!=0
                           or IM_RAC_A2_SPESE_ODC!=0
                           or IM_RAD_A2_SPESE_ODC_ALTRA_UO!=0
                           or IM_RAE_A2_SPESE_OGC!=0
                           or IM_RAF_A2_SPESE_OGC_ALTRA_UO!=0
                           or IM_RAG_A2_SPESE_COSTI_ALTRUI!=0
                           or IM_RAH_A3_COSTI_FINALI!=0
                           or IM_RAI_A3_COSTI_ALTRO_CDR!=0
                           or IM_RAL_A3_SPESE_ODC!=0
                           or IM_RAM_A3_SPESE_ODC_ALTRA_UO!=0
                           or IM_RAN_A3_SPESE_OGC!=0
                           or IM_RAO_A3_SPESE_OGC_ALTRA_UO!=0
                           or IM_RAP_A3_SPESE_COSTI_ALTRUI!=0
					     )
					 ) for update nowait;

-- Cursore sugli aggregati delle entrate per costituzione parte entrate bilancio finanziario CNR

cursor DETTAGLI_PDG_AGGREGATO_ETR(aEsercizio varchar2) RETURN PDG_AGGREGATO_ETR_DET%ROWTYPE is (
                select * from
                         PDG_AGGREGATO_ETR_DET
				where
				         esercizio = aEsercizio
					 and ti_aggregato = 'M'
					 and (
					         IM_RA_RCE!=0
                          or IM_RB_RSE!=0
                          or IM_RC_ESR!=0
                          or IM_RD_A2_RICAVI!=0
                          or IM_RE_A2_ENTRATE!=0
                          or IM_RF_A3_RICAVI!=0
                          or IM_RG_A3_ENTRATE!=0
					     )
				) for update nowait;

-- Cursore che estrae la testata di tutti i PDG che determinano

cursor TUTTI_PDG_PER_BIL_CNR(aEsercizio varchar2) RETURN PDG_PREVENTIVO%ROWTYPE is (
                select a.* from
                         PDG_PREVENTIVO a, CDR b, UNITA_ORGANIZZATIVA c
				where
				             a.esercizio = aEsercizio
						 and b.cd_centro_responsabilita = a.cd_centro_responsabilita
						 and c.cd_unita_organizzativa = b.cd_unita_organizzativa
						 and
						 (
						  (
						     c.cd_tipo_unita = CNRCTB020.TIPO_AREA
							 and c.fl_uo_cds = 'Y'
							 and b.livello = 2
				          ) or (
						     c.cd_tipo_unita != CNRCTB020.TIPO_AREA
							 and c.fl_uo_cds = 'Y'
							 and b.livello = 1
						  )
				         )
				) for update nowait;

-- Cursore che ritorna tutti i PDG che afferiscono ad area

-- aEs esercizio
-- aCDRArea cdr area di ricerca

cursor TUTTI_PDG_AFFERENTI_AREA(aEs number, aCDRArea cdr%rowtype) RETURN PDG_PREVENTIVO%ROWTYPE is (
                select a.* from
--P.R. Da Verificare dopo l'inserimento dell'area di afferenza sul PDG
						 UNITA_ORGANIZZATIVA c, -- Uo collegata al CDR Afferente all'area
						 CDR b, 		   -- Cdr Afferente
                         PDG_PREVENTIVO a -- Testata PDG del CDR afferente
				where
						     c.cd_area_ricerca = aCDRArea.cd_unita_organizzativa
						 and b.cd_unita_organizzativa = c.cd_unita_organizzativa
						 and a.esercizio = aEs
						 and a.cd_centro_responsabilita = b.cd_centro_responsabilita
				) for update nowait;

-- Cursore che ritorna tutti i PDG afferenti al primo CDR (nel caso dell'area, l'unico CDR esistente)
--
-- aEs esercizio
-- aPrimoCDR cdr root della linea CDS

cursor TUTTI_PDG_LINEA(aEs number, aPrimoCDR cdr%rowtype) RETURN PDG_PREVENTIVO%ROWTYPE is (
                select a.* from
		CDR b, 		   -- Cdr Afferente a aPrimoCDR
                PDG_PREVENTIVO a -- Testata PDG del CDR afferente
		where (b.cd_cdr_afferenza = aPrimoCDR.cd_centro_responsabilita Or
		       b.cd_centro_responsabilita = aPrimoCDR.cd_centro_responsabilita)
                   And a.esercizio = aEs
                   and a.cd_centro_responsabilita = b.cd_centro_responsabilita)
                For update nowait;

-- Functions e Procedures

-- Predispone bilancio finanziario CDS.
--
-- pre-post-name:  BILANCIO finanziario dell'ente non ancora approvato
-- pre:  Il bilancio finanziario dell'ente non è ancora approvato
-- post: Viene sollevata un'eccezione applicativa
--
-- pre-post-name:  CDS non valido
-- pre:  Il CDS specificato non è un CDS valido nell'esercizio specificato
-- post: Viene sollevata un'eccezione applicativa
--
-- pre-post-name:  Cdr di primo livello o resp. di AREA non in stato di chiusura finale
-- pre:  Il CDR di primo livello o AREA non ha posto in stato di chiusura definitiva il suo piano di gestione
-- post: Viene sollevata un'eccezione applicativa
--
-- pre-post-name:  Esiste almeno un Cdr appartenente al CDS, afferente ad AREA che non ha effettuato il ribaltamento sull'area
-- pre:  Esiste un CDR collegato ad area attraverso la UO di afferenza. Tale CDR non ha effettuato il ribaltamento dei suoi costi sull'area
-- post: Viene sollevata un'eccezione applicativa
--
-- pre-post-name:  Esiste CDR responsabile di area che non ha ricevuto tutti ribaltamenti
-- pre: Esiste un CDR responsabile di UO CDS AREA che non ha ricevuto il ribaltamento di tutti i CDR che afferiscono via UO all'area
-- post: Viene sollevata un'eccezione applicativa
--
-- pre-post-name: Non esiste un associazione tra titolo di spesa cds + natura e capitolo di entrata CNR
-- pre: Non esiste una associazione tra il titolo di spesa cds + la natura (che arrivano dal bilancio finanziario cnr parte spese) e  il capitolo di entrata CNR
-- post: Viene sollevata un'eccezione applicativa
--
-- pre-post-name: Predisposizione del bilancio finanziario del CDS
-- pre: Nessuna delle precondizioni precedenti verificata
-- post:
--  Parte spese:
--  * leggo i dettagli aggregati per rubrica dei piani di gestione dei CDR afferenti al cds ed inserisco lo stanziamento leggendo la colonna pagamenti dal piano di gestione
-- Parte entrate:
-- *   gli importi vanno derivati dagli importi del bilancio finanziario dell'ente parte spesa attraverso l'associazione tra titolo spesa CNR + tipo CDS + natura e capitolo CDS una volta determinati i sottoarticoli eligibili alla definizione di entrate CDS, attraverso l'associazione SPESE CNR ENTRATE CDS identifico il capitolo di entrata CDS a cui imputare gli importi, quindi il saldo viene inserito
-- Viene aggiornata la testata del bilancio preventivo impostando la stato del piano a predisposto.

-- Parametri:
-- aEsercizio -> anno d'esercizio
-- aCodiceCDS -> codice del CDS di cui predisporre il Bilancio Preventivo
-- aUtente -> Utente applicativo che effettua l'operazione

-- stani 18.06.2009 Procedure predisponeBilFinCDS(aEsercizio varchar2, aCodiceCDS varchar2, aUtente varchar2);

-- 20.02.2006 AGGIUNTA DICHIARAZIONE FUNZIONE PERCHè MI SERVE FUORI (SF)

 Function getPgVariazione(aEs number, aCdCds varchar2) return NUMBER;

-- Produce bilancio finanziario CNR.
--
-- pre-post-name: Unità organizzativa ENTE non esistente o non valida in esercizio corrente
-- pre: Non esiste l'unità organizzativa ENTE o non è valida nell'esercizio selezionato
-- post: Viene sollevata un'eccezione applicativa
--
-- pre-post-name: L'esercizio contabile non è aperto
-- pre: L'esercizio contabile non è aperto (l'aertura è necessaria per la creazione degli impegni sul capitolo automatici)
-- post: Viene sollevata un'eccezione applicativa
--
-- pre-post-name:  Il bilancio preventivo non è in stato iniziale
-- pre:  Il bilancio preventivo non è in satto iniziale (Stato 'A')
-- post: Viene sollevata un'eccezione applicativa
--
-- pre-post-name:  Esiste almeno un CDR di I livello o responsabile di area che non ha creato l'aggregato iniziale
-- pre:  Esiste un CDR di primo livello o responsabile di area che non ha generato l'aggregato dei piani di gestione
-- post: Viene sollevata un'eccezione applicativa
--
-- pre-post-name:  Non esiste il CDS presidente di Area valido nell'esercizio specificato, per almeno una area di ricerca
-- pre:  Non esiste il CDS presidente di area nell'esercizio specificato per almeno una area di ricerca (CDS di tipo area)
-- post: Viene sollevata un'eccezione applicativa
--
-- pre-post-name:  Non esiste o non è valida nell'esercizio l'Area di ricerca impattata da dettagli di natura 5
-- pre:  Non esiste o non è valido nell'esercizio il CDS AREA impattato da dettagli di natura 5 esistenti su CDR che afferiscono ad UO che afferisce all'area in questione
-- post: Viene sollevata un'eccezione applicativa
--
-- pre-post-name:  Non esiste il CDS presidente di Area impattata da dettagli di natura 5
-- pre:  Non esiste o non è valido il  nell'esercizio il CDS presidente di un'AREA di ricerca impattata da dettagli di natura 5 esistenti su CDR che afferiscono ad UO che afferisce all'area in questione.
-- post: Viene sollevata un'eccezione applicativa
--
-- pre-post-name:  Associazione con tipologia di intervento non trovata
-- pre: Il dettaglio di aggregazione di spesa è relativo al SAC e non è di natura 5 e non esiste l'associazione tra la voce del piano di tale dettaglio (capitolo di spesa CDS) e la tipologia di intervento (Capitolo di spesa CNR)
-- post: Viene sollevata un'eccezione applicativa
--
-- pre-post-name:  Associazione titolo CDS - titolo CNR non trovata
-- pre: Il titolo del dettaglio di aggregazione di spesa non corrisponde ad alcun titolo di spesa CNR
-- post: Viene sollevata un'eccezione applicativa
--
-- pre-post-name:  Sottoarticolo di spesa non trovato
-- pre: Non viene trovato in voce_f il sottoarticolo di spesa corrispondente ad un dettaglio di aggregazione di spesa
-- post: Viene sollevata un'eccezione applicativa
--
-- pre-post-name:  Articolo di entrata non trovato
-- pre: Non viene trovato in voce_f l'articolo di entrata corrispondente ad un dettaglio di aggregazione di entrata
-- post: Viene sollevata un'eccezione applicativa
--
-- pre-post-name:  Produzione del bilancio preventivo CNR a partire da aggregato in stato 'A' (Iniziale)
-- pre: Nessuna delle precondizioni precedenti è verificata
--           L'aggregato da cui si parte è in stato 'A'
--           IL SAC NON PUO' ESSERE PRESIDENTE DI AREA
-- post:
--  I saldi di preventivo CNR vengono azzerati sui tre anni (corrente + 2 successivi).
--  *Parte spese:
--     Legge i dettagli da aggregato PDG parte spese (modificato da ente).
--     Per ogni dettaglio aggregato le regole aggiornamento del preventivo relativamente al piano dei conti CNR parte spese sono:
--      CDR di I livello e dettaglio di natura 1-4 -> Sottoarticolo proprio del capitolo
--      CDR di I livello e dettaglio di natura 5     -> Sottoarticolo nat. 5 area del CDS pres. dell' area
--      CDR di II livello (CDS area) natura 1-4   -> Sottoarticolo nat. 1-4 area del CDS pres. area
--
--     Determina la voce_f su cui mettere gli importi di preventivo
--     Si tratta di sottoarticolo spesa CNR con le seguenti caratteristiche
--      cd_proprio_voce =
--                 codice cds (Sottoarticolo proprio del CDS) per CDS diverso da SAC e natura diversa da 5
--                 codice proprio della tipologia di intervento per il SAC e natura diversa da 5
--                 codice del CDS presidente dell'area per i dettagli di natura 5 SAC e NON
--      cd_cds codice del CDS
--      cd_natura = quella dell'aggregato
--      cd_funzione = quella dell'aggregato
--       ti_voce = 'E' (Sottoarticolo)
--       cd_categoria = '1' per MACROISTITUTI e '2' per SAC
--       cd_voce like cd_titolo.% dove il titolo è titolo CNR corrispondente al titolo CDS del dettaglio
--       Viene inserito lo stanziamento secondo la seguente regola:
--          IM_STANZ_INIZIALE_A1=
--                      IM_RI_CCS_SPESE_ODC
--                   + IM_RK_CCS_SPESE_OGC
--                   + IM_RQ_SSC_COSTI_ODC
--                   + IM_RS_SSC_COSTI_OGC
--                   + IM_RU_SPESE_COSTI_ALTRUI
--          IM_STANZ_INIZIALE_A2=
--                      IM_RAC_A2_SPESE_ODC
--                   + IM_RAE_A2_SPESE_OGC
--                   + .M_RAG_A2_SPESE_COSTI_ALTRUI
--          IM_STANZ_INIZIALE_A3=
--                      IM_RAL_A3_SPESE_ODC
--                   + IM_RAN_A3_SPESE_OGC
--                   + IM_RAP_A3_SPESE_COSTI_ALTRUI
--
--  *Parte entrate:
--     Legge i dettagli da aggregato PDG parte entrate (modificato da ente).
--
--     Determina la voce_f su cui mettere gli importi di preventivo
--     Si tratta di articolo entrata CNR con le seguenti caratteristiche
--       cd_proprio_voce = codice proprio uo collegata al CDR di primo livello o resp. di AREA (UO CDS per ISTITUTI e AREE, UO RUBRICA PER SAC)
--     cd_unita_organizzativa = codice uo collegata al CDR di primo livello o resp. di AREA  (UO CDS per ISTITUTI e AREE, UO RUBRICA PER SAC)
--       ti_voce = 'A' (Articolo)
--       Viene inserito lo stanziamento secondo la seguente regola:
--           IM_STANZ_INIZIALE_A1=IM_RA_RCE + aDettE.IM_RC_ESR
--           IM_STANZ_INIZIALE_A2=IM_RE_A2_ENTRATE
--           IM_STANZ_INIZIALE_A3=IM_RG_A3_ENTRATE
--
--  *Generazione impegni su capitoli di spesa
--    Al termine dell'operazione vengono generati gli impegni relativi ai capitoli di
--
-- pre-post-name:  Produzione del bilancio preventivo CNR a partire da aggregato in stato 'B' (Chiuso)
-- pre: Nessuna delle precondizioni precedenti è verificata
--           L'aggregato da cui si parte è in stato 'B'
--           IL SAC NON PUO' ESSERE PRESIDENTE DI AREA
-- post: Viene prodotto il bilancio finanziario dell'ENTE e i singoli saldi sono impostati a sola lettura (fl_sola_lettura = 'Y')
--             Viene modificato lo stato della testata del bilancio preventivo CNR a prodotto ('B')
--
-- Parametri:
-- aEsercizio -> Anno d'esercizio
-- aUtente -> Utente applicativo che effettua l'operazione

 procedure predisponeBilFinCNR(aEsercizio varchar2, aUtente varchar2);

-- 25.11.2005 Nuova gestione del Bilancio del CNR da PDG gestionale
--            Il vecchio viene lasciato ma non più chiamato

 procedure predispBilFinCNR_da_gest(aEsercizio NUMBER, ACdR VARCHAR2, aUtente varchar2);

-- procedure job_predispone_bil_fin_cnr(job number, pg_exec number, next_date date,
--                                      aEsercizio number, aUtente varchar2);

 -- Inizializza bilancio preventivo.
 -- Nel caso il CDS sia di tipo ENTE, viene inizializzata la testata del BILANCIO dell'ENTE
 -- Nel caso di CDS sia di altro tipo, viene inizializzata la testata del BILANCIO del CDS
 -- Viene chiamato da trigger AI_UNITA_ORGANIZZATIVA (after insert) sulla tabella UO
 --
 -- aEs -> Esercizio di scrivania
 -- aCDS -> CDS di cui inizializzare il PDG
 -- aUser -> utenza che effettua l'operazione
 --
 procedure inizializzaBilancioPreventivo(aEs number, aCdCDS varchar2, aUser varchar2);

 -- Legge le voci_f inserite ed riempie la tabella dei saldi con i dati iniziali:
 -- Invocato da trigger AI_ESERCIZIO nel metotodo onCreazioneEsercizio di CNRCTB005

 --ENTE
 -- leggo in voce_f tutti i mastrini con
 --  ti_appartenenza ='C' e
 --  ti_gestione = 'S'  e ti_gestione = 'E'
 --  tento un inserimento in VOCE_F_SALDI_CMP per competenza e residui: se il dato esiste già non segnalo errori e continuo

 --CDS
 -- leggo in voce_f tutti i mastrini con
 --  ti_appartenenza ='D' e
 --  ti_gestione = 'S'  e (cd_cds is null o cd_cds = codice del CDS di cui stò inserendo l'esercizio) o ti_gestione= 'E'
 --  tento un inserimento in VOCE_F_SALDI_CMP per competenza e residui: se il dato esiste già non segnalo errori e continuo

 procedure creaEsplSaldi(aEsercizio number, aCdCDS varchar2, aUser varchar2 );

 --Alla tabella VOCE_F verrà agganciato un trigger AI_VOCE_F
 --Tale trigger scatenerà una procedura PL/SQL per l'alimentazione automatica di VOCE_F_SALDI_CMP.
 --Tale procedura funzionerà nel seguente modo:

 --Se il capitolo inserito non è mastrino => esce
 --Se il ti_appartenenza = 'C'
 --tento un inserimento in VOCE_F_SALDI_CMP per competenza e residui: se il dato esiste già non segnalo errori e continuo

 --Se ti_appartenenza = 'D'
 --Ciclo su tutti i CDSX presenti in BILANCIO_PREVENTIVO nell'esercizio specificato
 --    Per ogni CDSX:
 --      ti_gestione = 'S'  e (cd_cds is null o cd_cds = codice del CDSX di cui stò inserendo l'esercizio) o ti_gestione= 'E'
 --         tento un inserimento in VOCE_F_SALDI_CMP per competenza e residui: se il dato esiste già non segnalo errori e continuo

 procedure creaEsplSaldi(aVoceF voce_f%rowtype);

 -- Annulla l'operazione di esplosione dei saldi iniziali nel caso di cancellazione di un capitolo finanziario
 -- Alla tabella VOCE_F verrà agganciato un trigger BD_VOCE_F attivato prima della cancellazione di una voce_f

 procedure eliminaEsplSaldi(aVoceF voce_f%rowtype);

-- Effettua la spalmatura delle entrate contrattate dall'UO resp. della contrattazione, verso le altre UO sottostanti
-- a livello di articoli di entrata del bilancio finanziario CNR.
--
-- pre-post-name: Il CDR specificato non è CDR su cui si è fatta la contrattazione
-- pre: il CDR specificato non è di primo livello o responsabile di AREA.
-- post: Viene sollevata un'eccezione
--
-- pre-post-name: Il Piano di gestione del CDR non è in stato di chiusura definitiva
-- pre: Il Piano di gestione del CDR non è in stato di chiusura definitiva (stato F)
-- post: Viene sollevata un'eccezione
--
-- pre-post-name: La ripartizione delle entrate è già stata fatta per il CDR specificato
-- pre: Esiste già una variazione di bilancio targata con causale di sistema RIP_AUT_EN
-- post: Viene sollevata un'eccezione
--
-- pre-post-name: Il bilancio finanziario CNR non è approvato
-- pre: Il bilancio finanziario CNR non risulta ancora approvato (stato C)
-- post: Viene sollevata un'eccezione
--
-- pre-post-name: Effettua ripartizione delle entrate
-- pre: Nessuna altra precondizione verificata
-- post:
--   Il sistema crea una variazione di bilancio preventivo CNR in stato provvisorio
--   In tale variazione, vengono calcolati, per ogni titolo capitolo di entrata derivante da PDG, su UOX <> da UO aggregante, i movimenti
--   di variazione in aumento dello stanziamento di entrata su tali UOX.
--   Al termine viene creato un unico movimento di storno per ogni titolo capitolo per la diminuzione dello stanziamento sull'articolo di entrata
--   corrispondente all'UO aggregante.
--   Viene verificata l'applicabilità della variazione di bilancio (vedi pre-post check    CNRCTB055.checkVariazioneBilancio)
--   Viene esistata la variazione di bilancio (vedi pre-post check    CNRCTB055.esitaVariazioneBilancio)
--   Questa operazione viene effetuata per gli importi corrispondenti ai 3 anni di previsione
--
-- Parametri:
-- aEs -> esercizio
-- aCdCdrPrimo -> Centro di responsabilità che scatena l'operazione
-- aUser -> Utente che effettua l'operazione

procedure creaRipartEntrate(aEs number,aCdCdr varchar2, aUser varchar2);

-- Controlla l'effettuabilità di una variazione a bilancio preventivo specificata
-- La viariazione impatta importi relativi ad uno dei tre anni di previsione corrente e due successivi
--
-- pre-post-name: Il bilancio preventivo non risulta approvato
-- pre: Il bilancio preventivo del CDS su cui effetture la variazione non risulta approvato
-- post: Viene sollevata un'eccezione
--
-- pre-post-name: Esiste un dettaglio di variazione su saldo ANNO 1 di SPESA CNR che non soddisfa le condizioni di applicabilità
-- pre: La variazione è richiesta sui importi del primo anno ed esiste almeno un dettaglio di variazione su saldo di SPESA CNR con le seguenti caratteristiche:
--               aSaldo.im_stanz_iniziale_a1 + aSaldo.variazioni_piu - aSaldo.variazioni_meno + aDet.im_variazione - aSaldo.im_obblig_imp_acr < 0
-- post: Viene sollevata un'eccezione
--
-- pre-post-name: Esiste un dettaglio di variazione su saldo ANNO 1 di SPESA CDS che non soddisfa le condizioni di applicabilità
-- pre: La variazione è richiesta sui importi del primo anno ed esiste almeno un dettaglio di variazione su saldo di SPESA CDS con le seguenti caratteristiche:
--    	      aSaldo.im_stanz_iniziale_a1 + aSaldo.variazioni_piu - aSaldo.variazioni_meno + aDet.im_variazione - aSaldo.im_obblig_imp_acr < 0
-- post: Viene sollevata un'eccezione
--
-- pre-post-name: Esiste un dettaglio di variazione su saldo ANNO n (1,2,3) (CDS o CNR) che non soddisfa le condizioni di applicabilità
-- pre: Esiste almeno un dettaglio di variazione su saldo di qualsiasi anno (CDS o CNR) con le seguenti caratteristiche:
--   	      aSaldo.assestato + aDet.im_variazione < 0
--            dove l'assestato è:
--                 anno 1: aSaldo.im_stan_iniziale_a1 + variazioni_piu - variazioni_meno
--                 anno 2: aSaldo.im_stan_iniziale_a2
--                 anno 3: aSaldo.im_stan_iniziale_a3
-- post: Viene sollevata un'eccezione
--
-- Parametri
--   aEs -> esercizio contabile
--   aCodiceCds -> codice del cds su cui viene effettuata la variazione
--   aAppartenenza -> C=CNR D=CDS
--   aPgVariazione -> numero della variazione

 procedure checkVariazioneBilancio(aEsercizio number, aCodiceCds varchar2, aAppartenenza char, aPgVariazione number);

-- Rende effettiva una variazione di bilancio modificando il saldo delle variazioni in più o in meno sul bilancio finanziario CNR o CDS
--
-- pre-post-name: La variazione di bilancio è definitiva
-- pre: La variazione di bilancio è in stato definitivo
-- post: Viene sollevata un'eccezione
--
-- pre-post-name: Esito della variazione nel caso la variazione sia in stato provvisorio
-- pre:
--       La procedure checkVariazioneBilancio non solleva eccezioni
--       oppure se il CDS != CDSENTE e la variazione è relativa al primo anno e la procedure checkFondoRiserva non solleva eccezioni
-- post:
--    Per ogni dettaglio di variazione,
--      se la variazione impatta gli importi del primo anno:
--              viene aggiornato il saldo delle variazioni in piò o in meno (a seconda del segno del dettaglio di variazione)
--      se la variazione impatta gli importi di anni successivi al primo
--              viene aggiornato il corrispondente importo presente in saldo dell'importo della variazione con relativo segno
--    Viene aggiornato lo stato della variazione di bilancio portandolo a definitivo.
--
--
-- Parametri
--   aEs -> esercizio contabile
--   aCodiceCds -> codice del cds su cui viene effettuata la variazione
--   aAppartenenza -> C=CNR D=CDS
--   aPgVariazione -> numero della variazione
--   aUser -> utente che effettua la variazione

 procedure esitaVariazioneBilancio(aEsercizio number, aCodiceCds varchar2, aAppartenenza varchar2, aPgVariazione number, aUser varchar2);

 -- Controllo di applicabilità della variazione di bilancio
 procedure checkVariazioneBilancio(aVarBilancio var_bilancio%rowtype);
 -- Esegue la variazione di bilancio
 procedure esitaVariazioneBilancio(aVarBilancio var_bilancio%rowtype);

-- Controllo di esistenza spareggio entrate/spese per bilancio finanziario CDS
--
-- pre-post-name: Il cds specificato è uguale all'ente
-- pre: Il cds specificato è uguale all'ente
-- post: nessun controllo viene effettuato: viene ritornato 0
--
-- pre-post-name: Il cds non è l'ente, la somma delle entrate è inferiore a quella delle spese
-- pre: Il cds specificato non è = CDS ENTE e la somma delle entrate è inferiore alla somma delle spese
-- post: viene sollevata un'eccezione
--
-- pre-post-name: Controllo superato
-- pre: Nesun'altra precondizione verificata
-- post: viene ritornata la differenza entrate-spese
--
-- Parametri
--   aEs -> esercizio contabile
--   aCdCds -> codice del cds su cui viene effettuato il controllo
--   aTiAppartenenza -> Cnr (C)/Cds (D)
 function checkSpareggioBilancio(aEs number, aCdCds varchar2, aTiAppartenenza char) return number;

-- Controllo di esistenza spareggio entrate/spese per bilancio finanziario CDS
--
-- pre-post-name: Il cds specificato è uguale all'ente
-- pre: Il cds specificato è uguale all'ente
-- post: nessun controllo viene effettuato: viene ritornato 0
--
-- pre-post-name: Il cds non è l'ente, forza pareggio = 'Y', la somma delle entrate è diversa da quella delle spese
-- pre: Il cds specificato non è = CDS ENTE e il flag forzaPareggio = 'Y' e la somma delle entrate è diversa dalla somma delle spese
-- post: viene sollevata un'eccezione
--
-- pre-post-name: Il cds non è l'ente, forza pareggio = 'N', la somma delle entrate è inferiore a quella delle spese
-- pre: Il cds specificato non è = CDS ENTE e il flag forzaPareggio = 'N' e la somma delle inferiore a quella delle spese
-- post: viene sollevata un'eccezione
--
-- pre-post-name: Controllo superato
-- pre: Nesun'altra precondizione verificata
-- post: viene ritornata la differenza entrate-spese
--
-- Parametri
--   aEs -> esercizio contabile
--   aCdCds -> codice del cds su cui viene effettuato il controllo
--   aTiAppartenenza -> Cnr (C)/Cds (D)
--   forzaPareggio -> 'Y' forza il pareggio 'N' entrate > spese ok
 function checkSpareggioBilancio(aEs number, aCdCds varchar2, aTiAppartenenza char, forzaPareggio char) return number;

-- Controlla che il totale della parte spese del BIL FIN CDS dopo la modifica di una spesa
-- non superi la somma degli stanziamenti di competenza più i residui in BIL FIN CNR (tutte le nature tranne la 5) più fondo iniziale di cassa
--
-- pre-post-name: Il cds specificato è uguale all'ente
-- pre: Il cds specificato è uguale all'ente
-- post: nessun controllo viene effettuato: viene ritornato 0
--
-- pre-post-name: Il cds non è l'ente, la somma delle spese di trasferimento in bilancio CNR per il CDS in processo (competenza+residui) + fondo iniziale di cassa è inferiore a quella delle spese in bilancio CDS
-- pre: Il cds specificato è != CDS ENTE e la somma delle spese su articoli di parte 1 di trasferimento al CDS (competenza+residui) + fondo iniziale di cassa < della somma delle spese del CDS
-- post: viene sollevata un'eccezione
--
-- pre-post-name: Controllo superato
-- pre: Nessun'altra precondizione verificata
-- post: viene ritornata la differenza spese cnr-spese cds
--
-- Parametri
--   aEs -> esercizio contabile
--   aCdCds -> codice del cds su cui viene effettuato il controllo
--   aTiAppartenenza -> D (CDS)

-- stani 22.06.2009
-- function checkSpeseBilFinCdsCnr(aEs number, aCdCds varchar2, aTiAppartenenza char) return number;

-- Controlla che il nuovo importo su entrata del BIL FIN CDS dopo la modifica
-- non superi la somma degli stanziamenti di competenza più i residui del corrispondente articolo di spesa in BIL FIN CNR
--
-- pre-post-name: Il cds specificato è uguale all'ente
-- pre: Il cds specificato è uguale all'ente
-- post: nessun controllo viene effettuato: viene ritornato 0
--
-- pre-post-name: Controllo superato
-- pre: Nessun'altra precondizione verificata
-- post: viene ritornata la differenza spese cnr-stanziamento su entrata cds (aCdVoce)
--
-- Parametri
--   aEs -> esercizio contabile
--   aCdCds -> codice del cds su cui viene effettuato il controllo
--   aTiAppartenenza -> D (CDS)
--   aTiGestione -> E (Entrate)
--   aCdVoce -> codice del capitolo di entrata CDS

 -- stani 18.06.2009
 -- function checkEntrataBilFinCdsCnr(aEs number, aCdCds varchar2, aTiAppartenenza char, aTiGestione char, aCdVoce varchar2) return number;

-- Procedura General Purpose:
--  richiama una serie di procedure che dovrebbero essere eseguite prima del salavataggio del Bilancio Preventivo.
--
-- pre-post-name: Controllo superato
-- pre: tutti i controlli superati
-- post: richiama setcassainiassucc
--
-- Parametri
--   aEs -> esercizio contabile
--   aCdCds -> codice del cds su cui viene effettuato la modifica
--   aTiAppartenenza -> appartenenza CDS
--	 aTiGestione -> gestione Entrate/Spese
--	 aCdVoce -> capitolo sul quale viene effettuato la modifica
--	 aTi_competenza_residuo -> tipo competenza/residuo
--	 aUser -> l'utente che richiede la modifica
 procedure postsalvdett(aEs number, aCdCds varchar2, aTiAppartenenza char, aTiGestione char, aCdVoce varchar2, aTi_competenza_residuo char, aUser varchar2);

-- Procedura per controllo sui capitoli Avanzo/Disavanzo.
--
-- La seguente procedura viene invocata nei seguenti 2 casi:
--
-- 1. al salvataggio di un dettaglio di spesa o entrata del bilancio preventico CNR o CDS
-- 2. all'esito della variazione di bilancio CNR o CDS
--
-- pre-post-name: (Sia per CNR che per CDS) ti_competenza_residuo = RESIDUO
-- pre: la voce di saldo passata è di tipo residuo
-- post: esce senza fare nulla
--
-- pre-post-name: (Sia per CNR che per CDS) l'esercizio passato alla procedura è quello di inizio dell'applicazione
-- pre: l'esercizio passato alla procedura è quello di inizio dell'applicazione
-- post: esce senza fare nulla
--
-- pre-post-name: (Sia per CNR che per CDS) capitoli non correttamente configurati
-- pre: nella tabella CONGIFURAZIONE_CNR non vengono trovate informazione corrette relative ai capitoli inerenti
--      la gestione dell'AVANZO e DISAVANZO.
-- post: viene sollevata un'eccezione
--
-- pre-post-name: (Sia per CNR che per CDS) capitolo non appartenente alla gestione AVANZO/DISAVANZO
-- pre: il capitolo passato, non corrisponde a nessuno dei capitoli configurati, (CONFIGURAZIONE_CNR),
-- 		   per la gestione dell'AVANZO/DISAVANZO di gestione.
-- post: esce senza fare nulla
--
-- pre-post-name: (Sia per CNR che per CDS) assestato del capitolo della controparte <> 0
-- pre: l'assestato relativo al capitolo della controparte (spesa o entrata) del capitolo in processo risulta essere <> 0
-- post: visualizza un messaggio d'errore che evidenzia la NON possibilità di valorizzare entrambi i
-- 		 capitoli contemporaneamente.
--
-- pre-post-name: (Solo CNR) nessuna delle sopracitate precondizioni è verificata
-- pre: nessuna delle sopracitate precondizioni è verificata
-- post: esce senza fare nulla
--
-- pre-post-name: (Solo CDS) l'esercizio passato è il primo per il cds in processo
-- pre: l'esercizio passato è il primo per il cds in processo
-- post: esce senza fare nulla
--
-- pre-post-name: (Solo CDS) l'esercizio precedente a quello passato è chiuso per il cds in processo
-- pre: gestione CDS, l'esercizio precedente a quello passato è chiuso per il cds in processo
-- post: esce senza fare nulla
--
-- pre-post-name: (Solo CDS) bilancio finanziario in stato INIZIALE
-- pre: tutti i controlli superati e bilancio finanziario CDS in stato INIZIALE
-- post: esce senza fare nulla
--
-- pre-post-name: Solo CDS: Controllo superato
-- pre: tutti i controlli superati e bilancio finanziario CDS in stato <> INIZIALE
-- post: scrive l'importo dell'assestato nella tabella ESERCIZIO per il CdS indicato.
--
-- Parametri
--   aEs -> esercizio contabile
--   aCdCds -> codice del cds su cui viene effettuato la modifica
--   aTiAppartenenza -> appartenenza CDS
--	 aTiGestione -> gestione Entrate/Spese
--	 aCdVoce -> capitolo sul quale viene effettuato la modifica
--	 aTi_competenza_residuo -> tipo competenza/residuo
--	 aUser -> l'utente che richiede la modifica
  procedure setcassainiassucc(aEs number, aCd_cds varchar2, aTi_appartenenza char, aTi_gestione char, aCd_voce varchar2,  aTi_competenza_residuo char, aUser varchar2);

End;
