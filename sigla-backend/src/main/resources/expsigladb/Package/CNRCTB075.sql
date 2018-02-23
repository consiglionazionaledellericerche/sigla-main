--------------------------------------------------------
--  DDL for Package CNRCTB075
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRCTB075" as
--
-- CNRCTB075 - Package per la gestione delle VARIAZIONE AL PIANO DI GESTIONE
-- Date: 16/09/2003
-- Version: 1.10
--
-- Dependency: CNRCTB 020 IBMERR 001
--
-- History:
--
-- Date: 16/12/2002
-- Version: 1.0
-- Creazione
--
-- Date: 21/01/2003
-- Version: 1.1
-- Modifica per gestire creazione testata pdg_preventivo_var con pg_variazione_pdg = 0
-- invece che 1 (serve per creare testata anche al primo passaggio in stato B
-- dell'aggregato per mantenere i vincoli di integritא).
--
-- Date: 23/01/2003
-- Version: 1.2
-- Modifica per inserire i dettagli storici copiando UTCR,DARC,UTUV,DUVA e PG_VER_REC
-- da quelli originali.
-- Inoltre corretto errore: ad ogni chiusura in F del PDG veniva letto MAX(PG_VARIAZIONE_PDG)
-- e fatto update della testata PDG_PREVENTIVO_VAR, ma in realtא va fatto un insert
-- con il nuovo PG
--
-- Date: 24/01/2003
-- Version: 1.3
-- Aggiunti i metodi di controllo positivitא aggregati
--
-- Date: 27/01/2003
-- Version: 1.4
-- Correzione a checkAggrPdgSpeDetPositivo: modificata clausola
--		cd_centro_responsabilita_clge = aCdCdrCLGE
-- in
--		( aCdCdrCLGE is null or cd_centro_responsabilita_clge = aCdCdrCLGE )
--
-- Date: 28/01/2003
-- Version: 1.5
-- Modifica per creare testata di pdg_preventivo_var su passaggio di pdg_aggregato da B a M
-- invece che su passaggio di preventivo da M a F: tolto creazione da generaFrameVarPdg e
-- aggiunta procedure apreFrameVarPdg
--
-- Date: 28/01/2003
-- Version: 1.6
-- Introdotto lo stato sulla variazione pdg
--
-- Date: 29/01/2003
-- Version: 1.7
-- Aggiunto il metodo di chiusura del frame di variazioni a pdg
--
-- Date: 11/09/2003
-- Version: 1.8
-- Aggiornamento documentazione
--
-- Date: 16/09/2003
-- Version: 1.9
-- Aggiunta condizione stato <> 'N' su query di aggregazione per controllo di positivitא
-- dei dettagli di spesa del pdg  (checkAggrPdgSpeDetPositivo)
--
-- Date: 16/09/2003
-- Version: 1.10
-- Aggiunti/modificati i controlli in checkAggrPdgSpeDetPositivo:
-- 1. Se il nuovo dettaglio contiene valori negativi sulle colonne degli scarichi verso altro cdr/altra uo
--    controllo che NON esistano altri dettagli di scarico verso lo stesso cdr/uo in stato 'X'
-- 2. Controllo che la somma di tutti i dettagli di spesa non caricati e non annullati
--    raggruppati per l.a., voce del piano, clge e clgs sia positiva.
-- Modificati anche i parametri (aggiunto aPgSpesa e tolto a CdCdrClge)
--
-- Constants:
--
--
-- Functions e Procedures:
--
-- Trasforma i dettagli di variazione del PDG in dettagli definitivi e popola lo storico
-- delle variazioni; viene invocato sul cambiamento dello stato del pdg da M in F.
 procedure generaFrameVarPdg(aPdg pdg_preventivo%rowtype);

-- Effettua una copia del pdg aggregato per mantenere lo storico delle variazioni
-- Copia tutti i record con ti_aggregato = 'M' nelle tabelle PDG_AGGREGATO_ETR/SPE_VAR;
-- se ט giא presente un riga in pdg_preventivo_var per il cdr e l'esercizio indicato utilizza lo
-- stesso pg_variazione_pdg, altrimenti usa 0 (per marcare la prima versione del
-- pdg_aggregato approvato dall'ente)
 procedure generaFrameVarPdgAggregato(aPdgAggregato pdg_aggregato%rowtype);

-- Apre la testata dello storico delle variazioni con un nuovo
-- frame di variazione
 procedure apreFrameVarPdg(aPdgAggregato pdg_aggregato%rowtype);

-- Chiude la testata dello storico delle variazioni aggiornando lo stato
 procedure chiudeFrameVarPdg(aPdgAggregato pdg_aggregato%rowtype);

-- Controlla che il totale dei dettagli di entrata raggruppati per
-- linea di attivita, voce, esercizio, cdr
-- siano tutti importi positivi

-- Pre-post name: L'esercizio specificato non deve essere quello di partenza
-- Pre:  La somma dei dettagli di entrata di tipo diverso da 'CAR' per la linea di attivitא specificata
--       e per il conto specificato soddisfa almeno una delle seguenti condizioni (nell'ordine dato)
--					sum(IM_RA_RCE) IM_RA_RCE < 0
--					sum(IM_RB_RSE) IM_RB_RSE < 0
--					sum(IM_RC_ESR) IM_RC_ESR < 0
--					sum(IM_RD_A2_RICAVI) IM_RD_A2_RICAVI < 0
--					sum(IM_RE_A2_ENTRATE) IM_RE_A2_ENTRATE < 0
--					sum(IM_RF_A3_RICAVI) IM_RF_A3_RICAVI < 0
--					sum(IM_RG_A3_ENTRATE) IM_RG_A3_ENTRATE < 0
-- Post: Viene ritornato il nome della colonna e l'importo negativo corrispondente
--
-- Parametri:
--  aEsercizio -> esercizio contabile
--  aCdCds -> centro di responsabilitא dell'aggregato
--  aCdLa -> codice linea di attivitא di raggruppamento
--  aTiAppartenenza -> C=CNR,D=CDS
--  aTiGestione -> S=Spese, E=Entrate
--  aCdVoce -> Codice elemento_voce
--  colonna -> Nome colonna con disallineamenti (out)
--  importo -> Importo disallineamento
--
 procedure checkAggrPdgEtrDetPositivo(aEsercizio number, aCdCdr varchar2, aCdLA varchar2, aTiAppartenenza varchar2, aTiGestione varchar2, aCdVoce varchar2,colonna in out varchar2,importo in out number);

-- Controlla che il totale dei dettagli di spesa raggruppati per
-- linea di attivita, voce, esercizio, cdr e cdr clge
-- siano tutti importi positivi

-- Pre-post name: L'esercizio specificato non deve essere quello di partenza
-- Pre:  La somma dei dettagli di spesa di tipo diverso da 'CAR' con cdr di scarico verso altro cdr = aCdCdrCLGE nel caso
--       in cui aCdCdrCLGE<>null, per la linea di attivitא specificata, per il conto specificato
--       soddisfa almeno una delle seguenti condizioni (nell'ordine dato)
--						SUM(IM_RI_CCS_SPESE_ODC)          IM_RI_CCS_SPESE_ODC <0
--						SUM(IM_RJ_CCS_SPESE_ODC_ALTRA_UO) IM_RJ_CCS_SPESE_ODC_ALTRA_UO <0
--						SUM(IM_RK_CCS_SPESE_OGC)          IM_RK_CCS_SPESE_OGC <0
--						SUM(IM_RL_CCS_SPESE_OGC_ALTRA_UO) IM_RL_CCS_SPESE_OGC_ALTRA_UO <0
--						SUM(IM_RM_CSS_AMMORTAMENTI)       IM_RM_CSS_AMMORTAMENTI <0
--						SUM(IM_RN_CSS_RIMANENZE)          IM_RN_CSS_RIMANENZE <0
--						SUM(IM_RO_CSS_ALTRI_COSTI)        IM_RO_CSS_ALTRI_COSTI <0
--						SUM(IM_RP_CSS_VERSO_ALTRO_CDR)    IM_RP_CSS_VERSO_ALTRO_CDR <0
--						SUM(IM_RQ_SSC_COSTI_ODC)          IM_RQ_SSC_COSTI_ODC <0
--						SUM(IM_RR_SSC_COSTI_ODC_ALTRA_UO) IM_RR_SSC_COSTI_ODC_ALTRA_UO <0
--						SUM(IM_RS_SSC_COSTI_OGC)          IM_RS_SSC_COSTI_OGC <0
--						SUM(IM_RT_SSC_COSTI_OGC_ALTRA_UO) IM_RT_SSC_COSTI_OGC_ALTRA_UO <0
--						SUM(IM_RV_PAGAMENTI)              IM_RV_PAGAMENTI <0
--						SUM(IM_RAA_A2_COSTI_FINALI)       IM_RAA_A2_COSTI_FINALI <0
--						SUM(IM_RAB_A2_COSTI_ALTRO_CDR)    IM_RAB_A2_COSTI_ALTRO_CDR <0
--						SUM(IM_RAC_A2_SPESE_ODC)          IM_RAC_A2_SPESE_ODC <0
--						SUM(IM_RAD_A2_SPESE_ODC_ALTRA_UO) IM_RAD_A2_SPESE_ODC_ALTRA_UO <0
--						SUM(IM_RAE_A2_SPESE_OGC)          IM_RAE_A2_SPESE_OGC <0
--						SUM(IM_RAF_A2_SPESE_OGC_ALTRA_UO) IM_RAF_A2_SPESE_OGC_ALTRA_UO <0
--						SUM(IM_RAH_A3_COSTI_FINALI)       IM_RAH_A3_COSTI_FINALI <0
--						SUM(IM_RAI_A3_COSTI_ALTRO_CDR)    IM_RAI_A3_COSTI_ALTRO_CDR <0
--						SUM(IM_RAL_A3_SPESE_ODC)          IM_RAL_A3_SPESE_ODC <0
--						SUM(IM_RAM_A3_SPESE_ODC_ALTRA_UO) IM_RAM_A3_SPESE_ODC_ALTRA_UO <0
--						SUM(IM_RAN_A3_SPESE_OGC)          IM_RAN_A3_SPESE_OGC <0
--						SUM(IM_RAO_A3_SPESE_OGC_ALTRA_UO) IM_RAO_A3_SPESE_OGC_ALTRA_UO <0
-- Post: Viene ritornato il nome della colonna e l'importo negativo corrispondente
--
-- Parametri:
--  aEsercizio -> esercizio contabile
--  aCdCds -> centro di responsabilitא dell'aggregato
--  aCdLa -> codice linea di attivitא di raggruppamento
--  aTiAppartenenza -> C=CNR,D=CDS
--  aTiGestione -> S=Spese, E=Entrate
--  aCdVoce -> Codice elemento_voce
--  aCdCdrCLGE -> Codice cdr collegato nel caso di scarico verso altro CDR (null=>non utilizzato in condizione di selezione)
--  colonna -> Nome colonna con disallineamenti (out)
--  importo -> Importo disallineamento
--
 procedure checkAggrPdgSpeDetPositivo(aEsercizio number, aCdCdr varchar2, aCdLA varchar2, aTiAppartenenza varchar2, aTiGestione varchar2, aCdVoce varchar2,aPgSpesa number,colonna in out varchar2,importo in out number);

-- FUNZIONE CHE, PER IL 2005, RECUPERA LA VOCE SPESA CNR DALLA VOCE SPESA CDS

Function getvocecnrfromvocecds (aEs_residuo     NUMBER,
                                aCDR            linea_attivita.CD_CENTRO_RESPONSABILITA%Type,
                                acdLinea        linea_attivita.CD_LINEA_ATTIVITA%Type,
                                aEs_voce        elemento_voce.esercizio%Type,
                                ti_gest         elemento_voce.ti_gestione%Type,
                                ti_app          elemento_voce.ti_appartenenza%Type,
                                aElem_voce      elemento_voce.cd_elemento_voce%Type)
Return Voce_f.CD_VOCE%Type;

Function getvocecnrfromvocecds (aEs_residuo     NUMBER,
                                aLinea          linea_attivita%Rowtype,
                                aEV_in          elemento_voce%Rowtype)
Return Voce_f.CD_VOCE%Type;

Procedure genera_varente_da_Varstanzres (aEsercizio NUMBER, aPG_VARIAZIONE NUMBER, aUser VARCHAR2,
                                         cds_var_bil    Out VARCHAR2,
                                         es_var_bil     Out NUMBER,
                                         ti_app_var_bil Out CHAR,
                                         pg_var_bil     Out NUMBER);

end;
