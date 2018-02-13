CREATE OR REPLACE package CNRCTB075 as
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
-- dell'aggregato per mantenere i vincoli di integrità).
--
-- Date: 23/01/2003
-- Version: 1.2
-- Modifica per inserire i dettagli storici copiando UTCR,DARC,UTUV,DUVA e PG_VER_REC
-- da quelli originali.
-- Inoltre corretto errore: ad ogni chiusura in F del PDG veniva letto MAX(PG_VARIAZIONE_PDG)
-- e fatto update della testata PDG_PREVENTIVO_VAR, ma in realtà va fatto un insert
-- con il nuovo PG
--
-- Date: 24/01/2003
-- Version: 1.3
-- Aggiunti i metodi di controllo positività aggregati
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
-- Aggiunta condizione stato <> 'N' su query di aggregazione per controllo di positività
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
-- se è già presente un riga in pdg_preventivo_var per il cdr e l'esercizio indicato utilizza lo
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
-- Pre:  La somma dei dettagli di entrata di tipo diverso da 'CAR' per la linea di attività specificata
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
--  aCdCds -> centro di responsabilità dell'aggregato
--  aCdLa -> codice linea di attività di raggruppamento
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
--       in cui aCdCdrCLGE<>null, per la linea di attività specificata, per il conto specificato
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
--  aCdCds -> centro di responsabilità dell'aggregato
--  aCdLa -> codice linea di attività di raggruppamento
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


CREATE OR REPLACE package body CNRCTB075 is

 procedure apreFrameVarPdg(aPdgAggregato pdg_aggregato%rowtype) is
		aPdgPreventivoVar pdg_preventivo_var%rowtype;
        aPdgPreventivoVarTmp pdg_preventivo_var%rowtype;
		cdrValido cdr%rowtype;
		aTSNow date;
 begin
 		aTSNow:=sysdate;
  	    cdrValido:=CNRCTB020.GETCDRVALIDO(aPdgAggregato.esercizio, aPdgAggregato.cd_centro_responsabilita);

		begin
				select * into aPdgPreventivoVarTmp from pdg_preventivo_var where
				esercizio = aPdgAggregato.esercizio and
				cd_centro_responsabilita = cdrValido.cd_centro_responsabilita and
				stato = CNRCTB070.VARIAZIONE_APERTA for update nowait;
				IBMERR001.RAISE_ERR_GENERICO('Esiste già un frame di variazione aperto per il cdr:'||cdrValido.cd_centro_responsabilita);
        exception when NO_DATA_FOUND then
		 null;
		end;
		begin
			select * into aPdgPreventivoVar from pdg_preventivo_var where
				esercizio = aPdgAggregato.esercizio and
				cd_centro_responsabilita = cdrValido.cd_centro_responsabilita and
				pg_variazione_pdg = ( select max(b.pg_variazione_pdg) from pdg_preventivo_var b where
					esercizio = aPdgAggregato.esercizio and
					cd_centro_responsabilita = cdrValido.cd_centro_responsabilita )
				for update nowait;
				aPdgPreventivoVar.ESERCIZIO := aPdgAggregato.ESERCIZIO;
				aPdgPreventivoVar.CD_CENTRO_RESPONSABILITA := cdrValido.CD_CENTRO_RESPONSABILITA;
				aPdgPreventivoVar.STATO := CNRCTB070.VARIAZIONE_APERTA;
				aPdgPreventivoVar.DACR := aTSNow;
				aPdgPreventivoVar.UTCR := aPdgAggregato.UTUV;
				aPdgPreventivoVar.DUVA := aTSNow;
				aPdgPreventivoVar.UTUV := aPdgAggregato.UTUV;
				aPdgPreventivoVar.PG_VER_REC := 1;
				aPdgPreventivoVar.PG_VARIAZIONE_PDG := aPdgPreventivoVar.pg_variazione_pdg+1;
				CNRCTB070.INS_PDG_PREVENTIVO_VAR(aPdgPreventivoVar);
		exception when NO_DATA_FOUND then
		 IBMERR001.RAISE_ERR_GENERICO('Non è possibile aprire il pdg per variazioni prima di averlo chiuso.');
		end;
 end;

 procedure chiudeFrameVarPdg(aPdgAggregato pdg_aggregato%rowtype) is
		aPdgPreventivoVar pdg_preventivo_var%rowtype;
		cdrValido cdr%rowtype;
		aTSNow date;
 begin
 		aTSNow:=sysdate;
  	    cdrValido:=CNRCTB020.GETCDRVALIDO(aPdgAggregato.esercizio, aPdgAggregato.cd_centro_responsabilita);

		begin
				select * into aPdgPreventivoVar from pdg_preventivo_var where
				esercizio = aPdgAggregato.esercizio and
				cd_centro_responsabilita = cdrValido.cd_centro_responsabilita and
				stato = CNRCTB070.VARIAZIONE_APERTA for update nowait;
        exception when NO_DATA_FOUND then
 		 IBMERR001.RAISE_ERR_GENERICO('Nessun frame di variazione aperto da chiudere per il cdr:'||cdrValido.cd_centro_responsabilita);
		end;
     	update pdg_preventivo_var
     	 set
     	  stato = CNRCTB070.VARIAZIONE_CHIUSA,
     	  utuv = aPdgAggregato.utuv,
     	  duva = aTSNow,
     	  pg_ver_rec=pg_ver_rec+1
     	where
     		esercizio = aPdgAggregato.esercizio and
     		cd_centro_responsabilita = cdrValido.cd_centro_responsabilita and
     		stato = CNRCTB070.VARIAZIONE_APERTA;
 end;


 procedure generaFrameVarPdg(aPdg pdg_preventivo%rowtype) is
 		aPgVariazionePdg number;
		aPdgPreventivoVar pdg_preventivo_var%rowtype;
		aPdgPreventivoEtrVar pdg_preventivo_etr_var%rowtype;
		aPdgPreventivoSpeVar pdg_preventivo_spe_var%rowtype;
		cdrValido cdr%rowtype;
		aTSNow date;
 begin
 		aTSNow:=sysdate;
  	    cdrValido:=CNRCTB020.GETCDRVALIDO(aPdg.esercizio, aPdg.cd_centro_responsabilita);

 		begin
			-- Lettura lockante del prossimo pg_variazione_pdg
			select * into aPdgPreventivoVar from pdg_preventivo_var where
				esercizio = aPdg.esercizio and
				cd_centro_responsabilita = cdrValido.cd_centro_responsabilita and
				stato = CNRCTB070.VARIAZIONE_APERTA
				for update nowait;
				aPgVariazionePdg := aPdgPreventivoVar.pg_variazione_pdg;
		  exception when NO_DATA_FOUND then
				IBMERR001.RAISE_ERR_GENERICO('Non è possibile chiudere la variazione del pdg prima di aver aperto l''aggregato in M');
		end;

		-- Ciclo su tutti i cdr dipendenti dal cdr di 1^ livello
		for aCdr in ( select * from V_PDG_CDR_FIGLI_PADRE where
				esercizio = aPdg.esercizio and
				cd_cdr_root = cdrValido.cd_centro_responsabilita) loop

				-- Inserisco i dettagli di entrata nello storico delle variazioni
				for aPdgPreventivoEtrDet in ( select * from pdg_preventivo_etr_det where
					esercizio = aCdr.esercizio and
					cd_centro_responsabilita = aCdr.cd_centro_responsabilita and
					origine = CNRCTB070.ORIGINE_VARIAZIONE
					for update nowait ) loop
						aPdgPreventivoEtrVar.ESERCIZIO := aPdgPreventivoEtrDet.ESERCIZIO;
						aPdgPreventivoEtrVar.CD_CDR_RESPONSABILE := aPdg.CD_CENTRO_RESPONSABILITA;
						aPdgPreventivoEtrVar.CD_CENTRO_RESPONSABILITA := aPdgPreventivoEtrDet.CD_CENTRO_RESPONSABILITA;
						aPdgPreventivoEtrVar.CD_LINEA_ATTIVITA := aPdgPreventivoEtrDet.CD_LINEA_ATTIVITA;
						aPdgPreventivoEtrVar.TI_APPARTENENZA := aPdgPreventivoEtrDet.TI_APPARTENENZA;
						aPdgPreventivoEtrVar.TI_GESTIONE := aPdgPreventivoEtrDet.TI_GESTIONE;
						aPdgPreventivoEtrVar.CD_ELEMENTO_VOCE := aPdgPreventivoEtrDet.CD_ELEMENTO_VOCE;
						aPdgPreventivoEtrVar.PG_ENTRATA := aPdgPreventivoEtrDet.PG_ENTRATA;
						aPdgPreventivoEtrVar.PG_VARIAZIONE_PDG := aPgVariazionePdg;
						aPdgPreventivoEtrVar.DACR := aPdgPreventivoEtrDet.DACR;
						aPdgPreventivoEtrVar.UTCR := aPdgPreventivoEtrDet.UTCR;
						aPdgPreventivoEtrVar.DUVA := aPdgPreventivoEtrDet.DUVA;
						aPdgPreventivoEtrVar.UTUV := aPdgPreventivoEtrDet.UTUV;
						aPdgPreventivoEtrVar.PG_VER_REC := aPdgPreventivoEtrDet.PG_VER_REC;
						CNRCTB070.INS_PDG_PREVENTIVO_ETR_VAR(aPdgPreventivoEtrVar);
				end loop;

				-- Inserisco i dettagli di spesa nello storico delle variazioni
				for aPdgPreventivoSpeDet in ( select * from pdg_preventivo_spe_det where
					esercizio = aCdr.esercizio and
					cd_centro_responsabilita = aCdr.cd_centro_responsabilita and
					origine = CNRCTB070.ORIGINE_VARIAZIONE
					for update nowait ) loop
						aPdgPreventivoSpeVar.ESERCIZIO := aPdgPreventivoSpeDet.ESERCIZIO;
						aPdgPreventivoSpeVar.CD_CDR_RESPONSABILE := aPdg.CD_CENTRO_RESPONSABILITA;
						aPdgPreventivoSpeVar.CD_CENTRO_RESPONSABILITA := aPdgPreventivoSpeDet.CD_CENTRO_RESPONSABILITA;
						aPdgPreventivoSpeVar.CD_LINEA_ATTIVITA := aPdgPreventivoSpeDet.CD_LINEA_ATTIVITA;
						aPdgPreventivoSpeVar.TI_APPARTENENZA := aPdgPreventivoSpeDet.TI_APPARTENENZA;
						aPdgPreventivoSpeVar.TI_GESTIONE := aPdgPreventivoSpeDet.TI_GESTIONE;
						aPdgPreventivoSpeVar.CD_ELEMENTO_VOCE := aPdgPreventivoSpeDet.CD_ELEMENTO_VOCE;
						aPdgPreventivoSpeVar.PG_SPESA := aPdgPreventivoSpeDet.PG_SPESA;
						aPdgPreventivoSpeVar.PG_VARIAZIONE_PDG := aPgVariazionePdg;
						aPdgPreventivoSpeVar.DACR := aPdgPreventivoSpeDet.DACR;
						aPdgPreventivoSpeVar.UTCR := aPdgPreventivoSpeDet.UTCR;
						aPdgPreventivoSpeVar.DUVA := aPdgPreventivoSpeDet.DUVA;
						aPdgPreventivoSpeVar.UTUV := aPdgPreventivoSpeDet.UTUV;
						aPdgPreventivoSpeVar.PG_VER_REC := aPdgPreventivoSpeDet.PG_VER_REC;
						CNRCTB070.INS_PDG_PREVENTIVO_SPE_VAR(aPdgPreventivoSpeVar);
				end loop;

				-- Aggiorno i dettagli di entrata con origine VARIAZIONE in origine DIRETTA
				update pdg_preventivo_etr_det set origine = CNRCTB050.ORIGINE_DIRETTA where
					esercizio = aCdr.esercizio and
					cd_centro_responsabilita = aCdr.cd_centro_responsabilita and
					origine = CNRCTB070.ORIGINE_VARIAZIONE;

				-- Aggiorno i dettagli di spesa con origine VARIAZIONE in origine DIRETTA
				update pdg_preventivo_spe_det set origine = CNRCTB050.ORIGINE_DIRETTA where
					esercizio = aCdr.esercizio and
					cd_centro_responsabilita = aCdr.cd_centro_responsabilita and
					origine = CNRCTB070.ORIGINE_VARIAZIONE;
			end loop;
 end;

 procedure generaFrameVarPdgAggregato(aPdgAggregato pdg_aggregato%rowtype) is
 		aPgVariazionePdg number;
		aPdgPreventivoVar pdg_preventivo_var%rowtype;
		aPdgAggregatoEtrVar pdg_aggregato_etr_var%rowtype;
		aPdgAggregatoSpeVar pdg_aggregato_spe_var%rowtype;
		cdrValido cdr%rowtype;
		aTSNow date;
 begin
 		aTSNow:=sysdate;
  	cdrValido:=CNRCTB020.GETCDRVALIDO(aPdgAggregato.esercizio, aPdgAggregato.cd_centro_responsabilita);

		-- Lock della testata del pdg_preventivo
		CNRCTB050.lockPdg(aPdgAggregato.esercizio,cdrValido.cd_centro_responsabilita);

 		begin
			-- Lettura lockante del prossimo pg_variazione_pdg
			select * into aPdgPreventivoVar from pdg_preventivo_var where
				esercizio = aPdgAggregato.esercizio and
				cd_centro_responsabilita = cdrValido.cd_centro_responsabilita and
				stato = CNRCTB070.VARIAZIONE_APERTA
				for update nowait;
		exception when NO_DATA_FOUND then
			-- Se non trovo nessun record in pdg_preventivo_var comincio da 0
			-- (Vuol dire che il pdg_preventivo è ancora in stato C)
 			aPdgPreventivoVar.ESERCIZIO := aPdgAggregato.ESERCIZIO;
			aPdgPreventivoVar.CD_CENTRO_RESPONSABILITA := cdrValido.CD_CENTRO_RESPONSABILITA;
			aPdgPreventivoVar.STATO := CNRCTB070.VARIAZIONE_CHIUSA;
			aPdgPreventivoVar.DACR := aTSNow;
			aPdgPreventivoVar.UTCR := aPdgAggregato.UTUV;
			aPdgPreventivoVar.DUVA := aTSNow;
			aPdgPreventivoVar.UTUV := aPdgAggregato.UTUV;
			aPdgPreventivoVar.PG_VER_REC := 1;
			aPdgPreventivoVar.PG_VARIAZIONE_PDG := 0;
			CNRCTB070.INS_PDG_PREVENTIVO_VAR(aPdgPreventivoVar);
		end;

		aPgVariazionePdg := aPdgPreventivoVar.PG_VARIAZIONE_PDG;

		-- Inserisco i dettagli di entrata nello storico delle variazioni
		for aPdgAggregatoEtrDet in ( select * from pdg_aggregato_etr_det where
			esercizio = aPdgAggregato.esercizio and
			cd_centro_responsabilita = cdrValido.cd_centro_responsabilita and
			ti_aggregato = CNRCTB050.TI_AGGREGATO_MODIFICATO
			for update nowait ) loop
				aPdgAggregatoEtrVar.ESERCIZIO := aPdgAggregatoEtrDet.ESERCIZIO;
				aPdgAggregatoEtrVar.CD_CENTRO_RESPONSABILITA := aPdgAggregatoEtrDet.CD_CENTRO_RESPONSABILITA;
				aPdgAggregatoEtrVar.CD_NATURA := aPdgAggregatoEtrDet.CD_NATURA;
				aPdgAggregatoEtrVar.TI_APPARTENENZA := aPdgAggregatoEtrDet.TI_APPARTENENZA;
				aPdgAggregatoEtrVar.TI_GESTIONE := aPdgAggregatoEtrDet.TI_GESTIONE;
				aPdgAggregatoEtrVar.CD_ELEMENTO_VOCE := aPdgAggregatoEtrDet.CD_ELEMENTO_VOCE;
				aPdgAggregatoEtrVar.IM_RA_RCE := aPdgAggregatoEtrDet.IM_RA_RCE;
				aPdgAggregatoEtrVar.IM_RB_RSE := aPdgAggregatoEtrDet.IM_RB_RSE;
				aPdgAggregatoEtrVar.IM_RC_ESR := aPdgAggregatoEtrDet.IM_RC_ESR;
				aPdgAggregatoEtrVar.IM_RD_A2_RICAVI := aPdgAggregatoEtrDet.IM_RD_A2_RICAVI;
				aPdgAggregatoEtrVar.IM_RE_A2_ENTRATE := aPdgAggregatoEtrDet.IM_RE_A2_ENTRATE;
				aPdgAggregatoEtrVar.IM_RF_A3_RICAVI := aPdgAggregatoEtrDet.IM_RF_A3_RICAVI;
				aPdgAggregatoEtrVar.IM_RG_A3_ENTRATE := aPdgAggregatoEtrDet.IM_RG_A3_ENTRATE;
				aPdgAggregatoEtrVar.PG_VARIAZIONE_PDG := aPgVariazionePdg;
				aPdgAggregatoEtrVar.DACR := aPdgAggregatoEtrDet.DACR;
				aPdgAggregatoEtrVar.UTCR := aPdgAggregatoEtrDet.UTCR;
				aPdgAggregatoEtrVar.DUVA := aPdgAggregatoEtrDet.DUVA;
				aPdgAggregatoEtrVar.UTUV := aPdgAggregatoEtrDet.UTUV;
				aPdgAggregatoEtrVar.PG_VER_REC := aPdgAggregatoEtrDet.PG_VER_REC;
				CNRCTB070.INS_PDG_AGGREGATO_ETR_VAR(aPdgAggregatoEtrVar);
		end loop;

		-- Inserisco i dettagli di spesa nello storico delle variazioni
		for aPdgAggregatoSpeDet in ( select * from pdg_aggregato_spe_det where
			esercizio = aPdgAggregato.esercizio and
			cd_centro_responsabilita = cdrValido.cd_centro_responsabilita and
			ti_aggregato = CNRCTB050.TI_AGGREGATO_MODIFICATO
			for update nowait ) loop
				aPdgAggregatoSpeVar.ESERCIZIO := aPdgAggregatoSpeDet.ESERCIZIO;
				aPdgAggregatoSpeVar.CD_CENTRO_RESPONSABILITA := aPdgAggregatoSpeDet.CD_CENTRO_RESPONSABILITA;
				aPdgAggregatoSpeVar.TI_APPARTENENZA := aPdgAggregatoSpeDet.TI_APPARTENENZA;
				aPdgAggregatoSpeVar.TI_GESTIONE := aPdgAggregatoSpeDet.TI_GESTIONE;
				aPdgAggregatoSpeVar.CD_ELEMENTO_VOCE := aPdgAggregatoSpeDet.CD_ELEMENTO_VOCE;
				aPdgAggregatoSpeVar.CD_FUNZIONE := aPdgAggregatoSpeDet.CD_FUNZIONE;
				aPdgAggregatoSpeVar.CD_NATURA := aPdgAggregatoSpeDet.CD_NATURA;
				aPdgAggregatoSpeVar.CD_CDS := aPdgAggregatoSpeDet.CD_CDS;
				aPdgAggregatoSpeVar.IM_RH_CCS_COSTI := aPdgAggregatoSpeDet.IM_RH_CCS_COSTI;
				aPdgAggregatoSpeVar.IM_RI_CCS_SPESE_ODC := aPdgAggregatoSpeDet.IM_RI_CCS_SPESE_ODC;
				aPdgAggregatoSpeVar.IM_RJ_CCS_SPESE_ODC_ALTRA_UO := aPdgAggregatoSpeDet.IM_RJ_CCS_SPESE_ODC_ALTRA_UO;
				aPdgAggregatoSpeVar.IM_RK_CCS_SPESE_OGC := aPdgAggregatoSpeDet.IM_RK_CCS_SPESE_OGC;
				aPdgAggregatoSpeVar.IM_RL_CCS_SPESE_OGC_ALTRA_UO := aPdgAggregatoSpeDet.IM_RL_CCS_SPESE_OGC_ALTRA_UO;
				aPdgAggregatoSpeVar.IM_RM_CSS_AMMORTAMENTI := aPdgAggregatoSpeDet.IM_RM_CSS_AMMORTAMENTI;
				aPdgAggregatoSpeVar.IM_RN_CSS_RIMANENZE := aPdgAggregatoSpeDet.IM_RN_CSS_RIMANENZE;
				aPdgAggregatoSpeVar.IM_RO_CSS_ALTRI_COSTI := aPdgAggregatoSpeDet.IM_RO_CSS_ALTRI_COSTI;
				aPdgAggregatoSpeVar.IM_RP_CSS_VERSO_ALTRO_CDR := aPdgAggregatoSpeDet.IM_RP_CSS_VERSO_ALTRO_CDR;
				aPdgAggregatoSpeVar.IM_RQ_SSC_COSTI_ODC := aPdgAggregatoSpeDet.IM_RQ_SSC_COSTI_ODC;
				aPdgAggregatoSpeVar.IM_RR_SSC_COSTI_ODC_ALTRA_UO := aPdgAggregatoSpeDet.IM_RR_SSC_COSTI_ODC_ALTRA_UO;
				aPdgAggregatoSpeVar.IM_RS_SSC_COSTI_OGC := aPdgAggregatoSpeDet.IM_RS_SSC_COSTI_OGC;
				aPdgAggregatoSpeVar.IM_RT_SSC_COSTI_OGC_ALTRA_UO := aPdgAggregatoSpeDet.IM_RT_SSC_COSTI_OGC_ALTRA_UO;
				aPdgAggregatoSpeVar.IM_RU_SPESE_COSTI_ALTRUI := aPdgAggregatoSpeDet.IM_RU_SPESE_COSTI_ALTRUI;
				aPdgAggregatoSpeVar.IM_RV_PAGAMENTI := aPdgAggregatoSpeDet.IM_RV_PAGAMENTI;
				aPdgAggregatoSpeVar.IM_RAA_A2_COSTI_FINALI := aPdgAggregatoSpeDet.IM_RAA_A2_COSTI_FINALI;
				aPdgAggregatoSpeVar.IM_RAB_A2_COSTI_ALTRO_CDR := aPdgAggregatoSpeDet.IM_RAB_A2_COSTI_ALTRO_CDR;
				aPdgAggregatoSpeVar.IM_RAC_A2_SPESE_ODC := aPdgAggregatoSpeDet.IM_RAC_A2_SPESE_ODC;
				aPdgAggregatoSpeVar.IM_RAD_A2_SPESE_ODC_ALTRA_UO := aPdgAggregatoSpeDet.IM_RAD_A2_SPESE_ODC_ALTRA_UO;
				aPdgAggregatoSpeVar.IM_RAE_A2_SPESE_OGC := aPdgAggregatoSpeDet.IM_RAE_A2_SPESE_OGC;
				aPdgAggregatoSpeVar.IM_RAF_A2_SPESE_OGC_ALTRA_UO := aPdgAggregatoSpeDet.IM_RAF_A2_SPESE_OGC_ALTRA_UO;
				aPdgAggregatoSpeVar.IM_RAG_A2_SPESE_COSTI_ALTRUI := aPdgAggregatoSpeDet.IM_RAG_A2_SPESE_COSTI_ALTRUI;
				aPdgAggregatoSpeVar.IM_RAH_A3_COSTI_FINALI := aPdgAggregatoSpeDet.IM_RAH_A3_COSTI_FINALI;
				aPdgAggregatoSpeVar.IM_RAI_A3_COSTI_ALTRO_CDR := aPdgAggregatoSpeDet.IM_RAI_A3_COSTI_ALTRO_CDR;
				aPdgAggregatoSpeVar.IM_RAL_A3_SPESE_ODC := aPdgAggregatoSpeDet.IM_RAL_A3_SPESE_ODC;
				aPdgAggregatoSpeVar.IM_RAM_A3_SPESE_ODC_ALTRA_UO := aPdgAggregatoSpeDet.IM_RAM_A3_SPESE_ODC_ALTRA_UO;
				aPdgAggregatoSpeVar.IM_RAN_A3_SPESE_OGC := aPdgAggregatoSpeDet.IM_RAN_A3_SPESE_OGC;
				aPdgAggregatoSpeVar.IM_RAO_A3_SPESE_OGC_ALTRA_UO := aPdgAggregatoSpeDet.IM_RAO_A3_SPESE_OGC_ALTRA_UO;
				aPdgAggregatoSpeVar.IM_RAP_A3_SPESE_COSTI_ALTRUI := aPdgAggregatoSpeDet.IM_RAP_A3_SPESE_COSTI_ALTRUI;
				aPdgAggregatoSpeVar.PG_VARIAZIONE_PDG := aPgVariazionePdg;
				aPdgAggregatoSpeVar.DACR := aPdgAggregatoSpeDet.DACR;
				aPdgAggregatoSpeVar.UTCR := aPdgAggregatoSpeDet.UTCR;
				aPdgAggregatoSpeVar.DUVA := aPdgAggregatoSpeDet.DUVA;
				aPdgAggregatoSpeVar.UTUV := aPdgAggregatoSpeDet.UTUV;
				aPdgAggregatoSpeVar.PG_VER_REC := aPdgAggregatoSpeDet.PG_VER_REC;
				CNRCTB070.INS_PDG_AGGREGATO_SPE_VAR(aPdgAggregatoSpeVar);
		end loop;
 end;

 procedure checkAggrPdgEtrDetPositivo(aEsercizio number, aCdCdr varchar2, aCdLA varchar2, aTiAppartenenza varchar2, aTiGestione varchar2, aCdVoce varchar2,colonna in out varchar2,importo in out number) is
 begin
 		for aggrPdgEtrDet in (
				select
					sum(IM_RA_RCE) IM_RA_RCE,
					sum(IM_RB_RSE) IM_RB_RSE,
					sum(IM_RC_ESR) IM_RC_ESR,
					sum(IM_RD_A2_RICAVI) IM_RD_A2_RICAVI,
					sum(IM_RE_A2_ENTRATE) IM_RE_A2_ENTRATE,
					sum(IM_RF_A3_RICAVI) IM_RF_A3_RICAVI,
					sum(IM_RG_A3_ENTRATE) IM_RG_A3_ENTRATE
				from
					pdg_preventivo_etr_det
				where
					esercizio = aEsercizio and
					cd_centro_responsabilita = aCdCdr and
					cd_linea_attivita = aCdLA and
					ti_appartenenza = aTiAppartenenza and
					ti_gestione = aTiGestione and
					cd_elemento_voce = aCdVoce And
					stato = 'Y' And
					categoria_dettaglio <> 'CAR' )
			loop
				if aggrPdgEtrDet.IM_RA_RCE < 0 then
					colonna := 'IM_RA_RCE';
					importo := aggrPdgEtrDet.IM_RA_RCE;
				elsif aggrPdgEtrDet.IM_RB_RSE < 0 then
					colonna := 'IM_RB_RSE';
					importo := aggrPdgEtrDet.IM_RB_RSE;
				elsif aggrPdgEtrDet.IM_RC_ESR < 0 then
					colonna := 'IM_RC_ESR';
					importo := aggrPdgEtrDet.IM_RC_ESR;
				elsif aggrPdgEtrDet.IM_RD_A2_RICAVI < 0 then
					colonna := 'IM_RD_A2_RICAVI';
					importo := aggrPdgEtrDet.IM_RD_A2_RICAVI;
				elsif aggrPdgEtrDet.IM_RE_A2_ENTRATE < 0 then
					colonna := 'IM_RE_A2_ENTRATE';
					importo := aggrPdgEtrDet.IM_RE_A2_ENTRATE;
				elsif aggrPdgEtrDet.IM_RF_A3_RICAVI < 0 then
					colonna := 'IM_RF_A3_RICAVI';
					importo := aggrPdgEtrDet.IM_RF_A3_RICAVI;
				elsif aggrPdgEtrDet.IM_RG_A3_ENTRATE < 0 then
					colonna := 'IM_RG_A3_ENTRATE';
					importo := aggrPdgEtrDet.IM_RG_A3_ENTRATE;
				end if;
			end loop;
 end;

 procedure checkAggrPdgSpeDetPositivo(aEsercizio number, aCdCdr varchar2, aCdLA varchar2, aTiAppartenenza varchar2, aTiGestione varchar2, aCdVoce varchar2, aPgSpesa number,colonna in out varchar2,importo in out number) is
 	aExists number;
 	statoVar   VARCHAR2(3);
 	esercizioVar NUMBER(4);
 	progressivoVar NUMBER(9);
 	cdrPrimoLivello CDR.Cd_centro_responsabilita%Type;
 Begin
    Begin
      Select v.STATO, v.ESERCIZIO, v.PG_VARIAZIONE_PDG Into statoVar, esercizioVar, progressivoVar
      From PDG_VARIAZIONE v, PDG_PREVENTIVO_SPE_DET b
      Where b.esercizio = aEsercizio
        And b.cd_centro_responsabilita = aCdCdr
        And b.cd_linea_attivita = aCdLA
        And b.ti_appartenenza = aTiAppartenenza
        And b.ti_gestione = aTiGestione
        And b.cd_elemento_voce = aCdVoce
        And b.pg_spesa = aPgSpesa
        And v.ESERCIZIO = b.ESERCIZIO_PDG_VARIAZIONE
 	And v.PG_VARIAZIONE_PDG = b.PG_VARIAZIONE_PDG;
      Select Nvl(Cd_cdr_afferenza,Cd_centro_responsabilita)
      Into cdrPrimoLivello
      From Cdr
      Where Cd_centro_responsabilita = aCdCdr;
    Exception
      When No_Data_Found Then
        Null;
    End;
    If statoVar Is Not Null And statoVar = 'PRD' Then
      Savepoint VARIAZIONE_DEFINITIVA;
      CNRCTB053.ribaltaSuAreaPDGVar(aEsercizio,cdrPrimoLivello,'CED',esercizioVar,progressivoVar);
    End If;
  	-- Se il nuovo dettaglio contiene valori negativi sulle colonne degli scarichi verso altro cdr/altra uo
	-- controllo che NON esistano altri dettagli di scarico verso lo stesso cdr/uo in stato 'X'

 	begin
		select 1 into aExists from dual where exists (
			select
				1
			from
				pdg_preventivo_spe_det a,pdg_preventivo_spe_det b
			where
	 			b.esercizio = aEsercizio and
	 			b.cd_centro_responsabilita = aCdCdr and
	 			b.cd_linea_attivita = aCdLA and
	 			b.ti_appartenenza = aTiAppartenenza and
	 			b.ti_gestione = aTiGestione and
	 			b.cd_elemento_voce = aCdVoce and
	 			b.pg_spesa = aPgSpesa and
				( b.IM_RJ_CCS_SPESE_ODC_ALTRA_UO < 0 or
				  b.IM_RL_CCS_SPESE_OGC_ALTRA_UO < 0 or
				  b.IM_RP_CSS_VERSO_ALTRO_CDR < 0 or
				  b.IM_RR_SSC_COSTI_ODC_ALTRA_UO < 0 or
				  b.IM_RT_SSC_COSTI_OGC_ALTRA_UO < 0 or
				  b.IM_RAB_A2_COSTI_ALTRO_CDR < 0 or
				  b.IM_RAD_A2_SPESE_ODC_ALTRA_UO < 0 or
				  b.IM_RAF_A2_SPESE_OGC_ALTRA_UO < 0 or
				  b.IM_RAI_A3_COSTI_ALTRO_CDR < 0 or
				  b.IM_RAM_A3_SPESE_ODC_ALTRA_UO < 0 or
				  b.IM_RAO_A3_SPESE_OGC_ALTRA_UO < 0) and
				a.ESERCIZIO = b.ESERCIZIO and
				a.CD_CENTRO_RESPONSABILITA = b.CD_CENTRO_RESPONSABILITA and
				a.CD_LINEA_ATTIVITA = b.CD_LINEA_ATTIVITA and
				a.TI_APPARTENENZA = b.TI_APPARTENENZA and
				a.TI_GESTIONE = b.TI_GESTIONE and
				a.CD_ELEMENTO_VOCE = b.CD_ELEMENTO_VOCE and
				a.PG_SPESA <> b.PG_SPESA and
				( ( a.CD_CENTRO_RESPONSABILITA_CLGE IS NULL and b.CD_CENTRO_RESPONSABILITA_CLGE IS NULL ) or
				  a.CD_CENTRO_RESPONSABILITA_CLGE = b.CD_CENTRO_RESPONSABILITA_CLGE ) and
				( ( a.CD_CENTRO_RESPONSABILITA_CLGS IS NULL and b.CD_CENTRO_RESPONSABILITA_CLGS IS NULL ) or
				  a.CD_CENTRO_RESPONSABILITA_CLGS = b.CD_CENTRO_RESPONSABILITA_CLGS ) and

				a.categoria_dettaglio = 'SCR' and
				a.stato = 'X' );
	exception when NO_DATA_FOUND then
		aExists := 0;
	end;

	if aExists = 1 then
		IBMERR001.RAISE_ERR_GENERICO('Non è possibile inserire dettagli di scarico negativi perchè esistono altri dettagli di scarico verso lo stesso cdr/uo non ancora confermati.');
	end if;

	-- Controllo che la somma di tutti i dettagli di spesa non caricati e non annullati
	-- raggruppati per l.a., voce del piano, clge e clgs sia positiva.

 	for aggrPdgSpeDet in (
 		select
 				SUM(a.IM_RI_CCS_SPESE_ODC)          IM_RI_CCS_SPESE_ODC,
 				SUM(a.IM_RJ_CCS_SPESE_ODC_ALTRA_UO) IM_RJ_CCS_SPESE_ODC_ALTRA_UO,
 				SUM(a.IM_RK_CCS_SPESE_OGC)          IM_RK_CCS_SPESE_OGC,
 				SUM(a.IM_RL_CCS_SPESE_OGC_ALTRA_UO) IM_RL_CCS_SPESE_OGC_ALTRA_UO,
 				SUM(a.IM_RM_CSS_AMMORTAMENTI)       IM_RM_CSS_AMMORTAMENTI,
 				SUM(a.IM_RN_CSS_RIMANENZE)          IM_RN_CSS_RIMANENZE,
 				SUM(a.IM_RO_CSS_ALTRI_COSTI)        IM_RO_CSS_ALTRI_COSTI,
 				SUM(a.IM_RP_CSS_VERSO_ALTRO_CDR)    IM_RP_CSS_VERSO_ALTRO_CDR,
 				SUM(a.IM_RQ_SSC_COSTI_ODC)          IM_RQ_SSC_COSTI_ODC,
 				SUM(a.IM_RR_SSC_COSTI_ODC_ALTRA_UO) IM_RR_SSC_COSTI_ODC_ALTRA_UO,
 				SUM(a.IM_RS_SSC_COSTI_OGC)          IM_RS_SSC_COSTI_OGC,
 				SUM(a.IM_RT_SSC_COSTI_OGC_ALTRA_UO) IM_RT_SSC_COSTI_OGC_ALTRA_UO,
 				SUM(a.IM_RV_PAGAMENTI)              IM_RV_PAGAMENTI,
 				SUM(a.IM_RAA_A2_COSTI_FINALI)       IM_RAA_A2_COSTI_FINALI,
 				SUM(a.IM_RAB_A2_COSTI_ALTRO_CDR)    IM_RAB_A2_COSTI_ALTRO_CDR,
 				SUM(a.IM_RAC_A2_SPESE_ODC)          IM_RAC_A2_SPESE_ODC,
 				SUM(a.IM_RAD_A2_SPESE_ODC_ALTRA_UO) IM_RAD_A2_SPESE_ODC_ALTRA_UO,
 				SUM(a.IM_RAE_A2_SPESE_OGC)          IM_RAE_A2_SPESE_OGC,
 				SUM(a.IM_RAF_A2_SPESE_OGC_ALTRA_UO) IM_RAF_A2_SPESE_OGC_ALTRA_UO,
 				SUM(a.IM_RAH_A3_COSTI_FINALI)       IM_RAH_A3_COSTI_FINALI,
 				SUM(a.IM_RAI_A3_COSTI_ALTRO_CDR)    IM_RAI_A3_COSTI_ALTRO_CDR,
 				SUM(a.IM_RAL_A3_SPESE_ODC)          IM_RAL_A3_SPESE_ODC,
 				SUM(a.IM_RAM_A3_SPESE_ODC_ALTRA_UO) IM_RAM_A3_SPESE_ODC_ALTRA_UO,
 				SUM(a.IM_RAN_A3_SPESE_OGC)          IM_RAN_A3_SPESE_OGC,
 				SUM(a.IM_RAO_A3_SPESE_OGC_ALTRA_UO) IM_RAO_A3_SPESE_OGC_ALTRA_UO
 		from
 			pdg_preventivo_spe_det a,pdg_preventivo_spe_det b
 		where
 			b.esercizio = aEsercizio and
 			b.cd_centro_responsabilita = aCdCdr and
 			b.cd_linea_attivita = aCdLA and
 			b.ti_appartenenza = aTiAppartenenza and
 			b.ti_gestione = aTiGestione and
 			b.cd_elemento_voce = aCdVoce and
 			b.pg_spesa = aPgSpesa and
 			a.ESERCIZIO = b.ESERCIZIO and
 			a.CD_CENTRO_RESPONSABILITA = b.CD_CENTRO_RESPONSABILITA and
 			a.CD_LINEA_ATTIVITA = b.CD_LINEA_ATTIVITA and
 			a.TI_APPARTENENZA = b.TI_APPARTENENZA and
 			a.TI_GESTIONE = b.TI_GESTIONE and
 			a.CD_ELEMENTO_VOCE = b.CD_ELEMENTO_VOCE and
 			( ( a.CD_CENTRO_RESPONSABILITA_CLGE IS NULL and b.CD_CENTRO_RESPONSABILITA_CLGE IS NULL ) or
 			  a.CD_CENTRO_RESPONSABILITA_CLGE = b.CD_CENTRO_RESPONSABILITA_CLGE ) and
 			( ( a.CD_CENTRO_RESPONSABILITA_CLGS IS NULL and b.CD_CENTRO_RESPONSABILITA_CLGS IS NULL ) or
 			  a.CD_CENTRO_RESPONSABILITA_CLGS = b.CD_CENTRO_RESPONSABILITA_CLGS ) and
 			a.categoria_dettaglio <> 'CAR' and
 			a.stato <> 'N' And
 			((a.ESERCIZIO_PDG_VARIAZIONE Is Not Null And  'PRP' != (Select stato From PDG_VARIAZIONE
 			                                                        Where PDG_VARIAZIONE.ESERCIZIO         = a.ESERCIZIO_PDG_VARIAZIONE
 	                                                                          And PDG_VARIAZIONE.PG_VARIAZIONE_PDG = a.PG_VARIAZIONE_PDG))
 			  Or a.ESERCIZIO_PDG_VARIAZIONE Is Null)
 			)
 	loop
 		if aggrPdgSpeDet.IM_RI_CCS_SPESE_ODC < 0 then
 			colonna := 'IM_RI_CCS_SPESE_ODC';
 			importo := aggrPdgSpeDet.IM_RI_CCS_SPESE_ODC;
 		elsif aggrPdgSpeDet.IM_RJ_CCS_SPESE_ODC_ALTRA_UO < 0 then
 			colonna := 'IM_RJ_CCS_SPESE_ODC_ALTRA_UO';
 			importo := aggrPdgSpeDet.IM_RJ_CCS_SPESE_ODC_ALTRA_UO;
 		elsif aggrPdgSpeDet.IM_RK_CCS_SPESE_OGC < 0 then
 			colonna := 'IM_RK_CCS_SPESE_OGC';
 			importo := aggrPdgSpeDet.IM_RK_CCS_SPESE_OGC;
 		elsif aggrPdgSpeDet.IM_RL_CCS_SPESE_OGC_ALTRA_UO < 0 then
 			colonna := 'IM_RL_CCS_SPESE_OGC_ALTRA_UO';
 			importo := aggrPdgSpeDet.IM_RL_CCS_SPESE_OGC_ALTRA_UO;
 		elsif aggrPdgSpeDet.IM_RM_CSS_AMMORTAMENTI < 0 then
 			colonna := 'IM_RM_CSS_AMMORTAMENTI';
 			importo := aggrPdgSpeDet.IM_RM_CSS_AMMORTAMENTI;
 		elsif aggrPdgSpeDet.IM_RN_CSS_RIMANENZE < 0 then
 			colonna := 'IM_RN_CSS_RIMANENZE';
 			importo := aggrPdgSpeDet.IM_RN_CSS_RIMANENZE;
 		elsif aggrPdgSpeDet.IM_RO_CSS_ALTRI_COSTI < 0 then
 			colonna := 'IM_RO_CSS_ALTRI_COSTI';
 			importo := aggrPdgSpeDet.IM_RO_CSS_ALTRI_COSTI;
 		elsif aggrPdgSpeDet.IM_RP_CSS_VERSO_ALTRO_CDR < 0 then
 			colonna := 'IM_RP_CSS_VERSO_ALTRO_CDR';
 			importo := aggrPdgSpeDet.IM_RP_CSS_VERSO_ALTRO_CDR;
 		elsif aggrPdgSpeDet.IM_RQ_SSC_COSTI_ODC < 0 then
 			colonna := 'IM_RQ_SSC_COSTI_ODC';
 			importo := aggrPdgSpeDet.IM_RQ_SSC_COSTI_ODC;
 		elsif aggrPdgSpeDet.IM_RR_SSC_COSTI_ODC_ALTRA_UO < 0 then
 			colonna := 'IM_RR_SSC_COSTI_ODC_ALTRA_UO';
 			importo := aggrPdgSpeDet.IM_RR_SSC_COSTI_ODC_ALTRA_UO;
 		elsif aggrPdgSpeDet.IM_RS_SSC_COSTI_OGC < 0 then
 			colonna := 'IM_RS_SSC_COSTI_OGC';
 			importo := aggrPdgSpeDet.IM_RS_SSC_COSTI_OGC;
 		elsif aggrPdgSpeDet.IM_RT_SSC_COSTI_OGC_ALTRA_UO < 0 then
 			colonna := 'IM_RT_SSC_COSTI_OGC_ALTRA_UO';
 			importo := aggrPdgSpeDet.IM_RT_SSC_COSTI_OGC_ALTRA_UO;
 		elsif aggrPdgSpeDet.IM_RV_PAGAMENTI < 0 then
 			colonna := 'IM_RV_PAGAMENTI';
 			importo := aggrPdgSpeDet.IM_RV_PAGAMENTI;
 		elsif aggrPdgSpeDet.IM_RAA_A2_COSTI_FINALI < 0 then
 			colonna := 'IM_RAA_A2_COSTI_FINALI';
 			importo := aggrPdgSpeDet.IM_RAA_A2_COSTI_FINALI;
 		elsif aggrPdgSpeDet.IM_RAB_A2_COSTI_ALTRO_CDR < 0 then
 			colonna := 'IM_RAB_A2_COSTI_ALTRO_CDR';
 			importo := aggrPdgSpeDet.IM_RAB_A2_COSTI_ALTRO_CDR;
 		elsif aggrPdgSpeDet.IM_RAC_A2_SPESE_ODC < 0 then
 			colonna := 'IM_RAC_A2_SPESE_ODC';
 			importo := aggrPdgSpeDet.IM_RAC_A2_SPESE_ODC;
 		elsif aggrPdgSpeDet.IM_RAD_A2_SPESE_ODC_ALTRA_UO < 0 then
 			colonna := 'IM_RAD_A2_SPESE_ODC_ALTRA_UO';
 			importo := aggrPdgSpeDet.IM_RAD_A2_SPESE_ODC_ALTRA_UO;
 		elsif aggrPdgSpeDet.IM_RAE_A2_SPESE_OGC < 0 then
 			colonna := 'IM_RAE_A2_SPESE_OGC';
 			importo := aggrPdgSpeDet.IM_RAE_A2_SPESE_OGC;
 		elsif aggrPdgSpeDet.IM_RAF_A2_SPESE_OGC_ALTRA_UO < 0 then
 			colonna := 'IM_RAF_A2_SPESE_OGC_ALTRA_UO';
 			importo := aggrPdgSpeDet.IM_RAF_A2_SPESE_OGC_ALTRA_UO;
 		elsif aggrPdgSpeDet.IM_RAH_A3_COSTI_FINALI < 0 then
 			colonna := 'IM_RAH_A3_COSTI_FINALI';
 			importo := aggrPdgSpeDet.IM_RAH_A3_COSTI_FINALI;
 		elsif aggrPdgSpeDet.IM_RAI_A3_COSTI_ALTRO_CDR < 0 then
 			colonna := 'IM_RAI_A3_COSTI_ALTRO_CDR';
 			importo := aggrPdgSpeDet.IM_RAI_A3_COSTI_ALTRO_CDR;
 		elsif aggrPdgSpeDet.IM_RAL_A3_SPESE_ODC < 0 then
 			colonna := 'IM_RAL_A3_SPESE_ODC';
 			importo := aggrPdgSpeDet.IM_RAL_A3_SPESE_ODC;
 		elsif aggrPdgSpeDet.IM_RAM_A3_SPESE_ODC_ALTRA_UO < 0 then
 			colonna := 'IM_RAM_A3_SPESE_ODC_ALTRA_UO';
 			importo := aggrPdgSpeDet.IM_RAM_A3_SPESE_ODC_ALTRA_UO;
 		elsif aggrPdgSpeDet.IM_RAN_A3_SPESE_OGC < 0 then
 			colonna := 'IM_RAN_A3_SPESE_OGC';
 			importo := aggrPdgSpeDet.IM_RAN_A3_SPESE_OGC;
 		elsif aggrPdgSpeDet.IM_RAO_A3_SPESE_OGC_ALTRA_UO < 0 then
 			colonna := 'IM_RAO_A3_SPESE_OGC_ALTRA_UO';
 			importo := aggrPdgSpeDet.IM_RAO_A3_SPESE_OGC_ALTRA_UO;
 		end if;
 	end loop;
    If statoVar Is Not Null And statoVar = 'PRD' Then
      Rollback To Savepoint VARIAZIONE_DEFINITIVA;
    End If;
 end;

-- FUNZIONE CHE, PER IL 2005, RECUPERA LA VOCE SPESA CNR DALLA VOCE SPESA CDS

Function getvocecnrfromvocecds (aEs_residuo     NUMBER,
                                aCDR            linea_attivita.CD_CENTRO_RESPONSABILITA%Type,
                                acdLinea        linea_attivita.CD_LINEA_ATTIVITA%Type,
                                aEs_voce        elemento_voce.esercizio%Type,
                                ti_gest         elemento_voce.ti_gestione%Type,
                                ti_app          elemento_voce.ti_appartenenza%Type,
                                aElem_voce      elemento_voce.cd_elemento_voce%Type)
                                Return Voce_f.CD_VOCE%Type Is

aLinea   linea_attivita%Rowtype;
aEV_in   elemento_voce%Rowtype;

Begin
  Select *
  Into   aLinea
  From   linea_attivita
  Where  CD_CENTRO_RESPONSABILITA = aCDR And
         CD_LINEA_ATTIVITA = acdLinea;

  Select *
  Into   aEV_in
  From   elemento_voce
  Where  esercizio        = aEs_voce    and
         ti_gestione      = ti_gest     and
         ti_appartenenza  = ti_app      and
         cd_elemento_voce = aElem_voce;

  Return cnrctb075.getvocecnrfromvocecds (aEs_residuo, aLinea, aEV_in);
End;

Function getvocecnrfromvocecds (aEs_residuo     NUMBER,
                                aLinea          linea_attivita%Rowtype,
                                aEV_in          elemento_voce%Rowtype)
Return Voce_f.CD_VOCE%Type Is

  aLineaPrimo           cdr%rowtype;
  aUO                   unita_organizzativa%rowtype;
  aUOCDS                unita_organizzativa%rowtype;
  aVoce                 voce_f%rowtype;
  aCodiceCDSCap         varchar2(30);
  aCodiceCDSSottoart    varchar2(30);
  aCDSPresidenteArea    unita_organizzativa%rowtype;
  aLineaArea            cdr%rowtype;
  aUOArea               unita_organizzativa%rowtype;
  aCodiceVoce           varchar2(50);
  aAss                  ass_ev_ev%rowtype;
  aTemp                 varchar2(50);
  aCategoria            varchar2(1);
  anno_2006             NUMBER;
  aCDS_voce             unita_organizzativa.cd_unita_organizzativa%Type;
  ACD_TIT_CAP_FISSO     VOCE_F.CD_TITOLO_CAPITOLO%Type;
  aUOPresidenteArea     unita_organizzativa.cd_unita_organizzativa%Type;
  aCdsSAC               unita_organizzativa%Rowtype;
  CD_CDS_PADRE          unita_organizzativa.cd_unita_organizzativa%Type;
  TIPO_CDR              unita_organizzativa.CD_TIPO_UNITA%Type;
  TIPO_NATURA           NATURA.TIPO%Type;

Begin

Select Min(esercizio)
Into   anno_2006
From   parametri_cnr
Where  FL_REGOLAMENTO_2006 = 'Y';

If aEs_residuo < ANNO_2006 Then

  -- Le regole di imputazione su bilancio finanziario CNR parte spese sono:
  -- CDR di I livello e dettaglio di natura 1-4 -> Sottoarticolo proprio del capitolo
  -- CDR di I livello e dettaglio di natura 5   -> Sottoarticolo nat. 5 area del CDS pres. dell' area
  -- CDR di II livello (CDS area) natura 1-4    -> Sottoarticolo nat. 1-4 area del CDS pres. area

Dbms_Output.PUT_LINE (' 2005 ');

   aLineaPrimo := CNRCTB020.GETCDRVALIDO(aEv_in.esercizio,aLinea.cd_centro_responsabilita);

   -- Leggo l'UO del CDR DI PRIMO LIVELLO DI QUELLO CHE PASSO E NE DERIVO IL CDS

   aUO := CNRCTB020.GETUOVALIDA(aEv_in.esercizio,aLineaPrimo.cd_unita_organizzativa);

   select *
   into   aUOCDS
   from   unita_organizzativa
   Where  cd_unita_padre  = aUO.cd_unita_padre
      and fl_uo_cds = 'Y';

Dbms_Output.PUT_LINE (' 2005 a');

   aUOCDS := CNRCTB020.GETUOVALIDA(aEv_in.esercizio,aUOCDS.cd_unita_organizzativa);

   aCodiceCDSCap:=aUOCDS.cd_unita_padre;      -- Di default il capitolo e il CDS del CDR di primo livello che aggrega
   aCodiceCDSSottoart := aUOCDS.cd_unita_padre; -- Di default il Codice proprio del sottoarticolo è = al codice del CDS
Dbms_Output.PUT_LINE (' 2005 b');
   if aUOCDS.cd_tipo_unita = CNRCTB020.TIPO_AREA then
Dbms_Output.PUT_LINE (' 2005 c');
    aCDSPresidenteArea:=CNRCTB020.getCDSPresidenteArea(aEv_in.esercizio, aLineaPrimo);

    if aCDSPresidenteArea.cd_unita_organizzativa is not null then
	 aCodiceCDSCap := aCDSPresidenteArea.cd_unita_organizzativa;
    else
	 IBMERR001.RAISE_ERR_GENERICO('(1) Cds presidente dell''AREA: '''||aUOCDS.cd_unita_padre||''' non trovato!!!');
    end if;
   end if;

   if aLinea.cd_natura = CNRCTB001.NATURA_5 Then
Dbms_Output.PUT_LINE (' 2005 d');
        -- Se i dettagli sono di natura 5 devo recuperare il CDS presidente di area
	-- Estraggo l'area da cd_cds del dettaglio di aggregazione

    begin
	 select * into aUOArea
	 from unita_organizzativa where
          cd_unita_padre = CNRUTL001.GETCDSFROMCDR(aLinea.cd_centro_responsabilita)
	  and fl_uo_cds = 'Y';
    exception when NO_DATA_FOUND then
     IBMERR001.RAISE_ERR_GENERICO('Area di ricerca con codice '''||CNRUTL001.GETCDSFROMCDR(aLinea.cd_centro_responsabilita)||''' non trovata!');
    end;
Dbms_Output.PUT_LINE (' 2005 e');
	aUOArea := CNRCTB020.GETUOVALIDA(aEv_in.esercizio,aUOArea.cd_unita_organizzativa);
	select *
	into aLineaArea
	from cdr
	where cd_unita_organizzativa = aUOArea.cd_unita_organizzativa
	  and to_number(cd_proprio_cdr) = 0;

	aLineaArea := CNRCTB020.GETCDRVALIDO(aEv_in.esercizio, aLineaArea.cd_centro_responsabilita);
Dbms_Output.PUT_LINE (' 2005 f');
	-- Dall'area estraggo il presidente dell'area
    aCDSPresidenteArea:=CNRCTB020.getCDSPresidenteArea(aEv_in.esercizio, aLineaArea);
    if aCDSPresidenteArea.cd_unita_organizzativa is not null then
	 aCodiceCDSCap:=aCDSPresidenteArea.cd_unita_organizzativa;
	 aCodiceCDSSottoart:= CNRUTL001.GETCDSFROMCDR(aLinea.cd_centro_responsabilita);
    else
	 IBMERR001.RAISE_ERR_GENERICO('(2) Cds presidente dell''AREA: '''||aUOArea.cd_unita_padre||''' non trovato!!!');
    end if;

   end if; -- natura 5
Dbms_Output.PUT_LINE (' 2005 g');

   -- Leggo la voce_f su cui mettere i valori
   -- Si tratta di sottoarticolo spesa CNR dove entro con
   --  cd_proprio_voce = codice cds (Sottoarticolo proprio del CDS)
   --  cd_cds codice del CDS
   --  cd_natura = quella dell'aggregato
   --  cd_funzione = quella dell'aggregato
   --  ti_voce = 'E' (Sottoarticolo)
   --  cd_categoria = '1' per MACROISTITUTI e '2' per SAC
   --  cd_voce like cd_titolo.%

-- Se il dettaglio è del SAC e non è di natura 5

    if aUOCDS.cd_tipo_unita = CNRCTB001.TIPOCDS_SAC and not aLinea.cd_natura = CNRCTB001.NATURA_5 then
Dbms_Output.PUT_LINE (' 2005 h');
-- in aCodiceVoce c'è la tipologia di intervento (che va in CATEGORIA 2 parte 1 spesa CNR)
	 aCodiceVoce := aEV_in.cd_elemento_PADRE;

    Else
    Dbms_Output.PUT_LINE (' 2005 i');
       	 aTemp := aEV_in.cd_elemento_PADRE;

	 begin
	  select *
	  into aAss
	  from ass_ev_ev
	  Where esercizio = aEv_in.esercizio
	   and ti_appartenenza = CNRCTB001.APPARTENENZA_CNR
	   and ti_gestione = CNRCTB001.GESTIONE_SPESE
	   and ti_elemento_voce = CNRCTB001.TITOLO
	   and ti_appartenenza_coll = CNRCTB001.APPARTENENZA_CDS
	   and ti_gestione_coll = CNRCTB001.GESTIONE_SPESE
	   and ti_elemento_voce_coll = CNRCTB001.TITOLO
	   and cd_elemento_voce_coll = aTemp;
	 exception when NO_DATA_FOUND then
	  IBMERR001.RAISE_ERR_GENERICO('Nessuna associazione con titolo spesa CNR trovata per titolo spesa CDS: '||aEv_in.cd_elemento_voce||' atemp: '||aTemp);
	 end;
	 aCodiceVoce:=aAss.cd_elemento_voce;
Dbms_Output.PUT_LINE (' 2005 j');
     end if;

    begin
Dbms_Output.PUT_LINE (' 2005 k');
-- Se il dettaglio è del SAC e non è di natura 5 la categoria è 2, altrimenti 1

     if aUOCDS.cd_tipo_unita = CNRCTB001.TIPOCDS_SAC and aLinea.cd_natura != CNRCTB001.NATURA_5 then

Dbms_Output.PUT_LINE (' 2005 k 1');
	  aCategoria := CNRCTB001.CATEGORIA2_SPESE_CNR;

Dbms_Output.PUT_LINE (' 2005 k 1.1 '||aEv_in.esercizio||' '||CNRCTB001.APPARTENENZA_CDS||' '||
CNRCTB001.GESTIONE_SPESE||' '||
CNRCTB001.APPARTENENZA_CNR||' '||
aEv_in.cd_elemento_voce);

          select cd_elemento_voce_coll
	  into aCodiceVoce
	  from ass_ev_ev
	  Where esercizio = aEv_in.esercizio
	   and ti_appartenenza = CNRCTB001.APPARTENENZA_CDS
	   and ti_gestione = CNRCTB001.GESTIONE_SPESE
--	   and ti_elemento_voce = CNRCTB001.TITOLO
	   and ti_appartenenza_coll = CNRCTB001.APPARTENENZA_CNR
	   and ti_gestione_coll = CNRCTB001.GESTIONE_SPESE
--	   and ti_elemento_voce_coll = CNRCTB001.TITOLO
	   and cd_elemento_voce = aEv_in.cd_elemento_voce;

     Else
     Dbms_Output.PUT_LINE (' 2005 k 2');
          aCategoria := CNRCTB001.CATEGORIA1_SPESE_CNR;
     end if;

Dbms_Output.PUT_LINE (' 2005 l ');

     select *
     into   aVoce
     from   voce_f
     Where  esercizio = aEv_in.esercizio And
            ti_gestione = CNRCTB001.GESTIONE_SPESE And
            ti_appartenenza = CNRCTB001.APPARTENENZA_CNR And
            cd_proprio_voce = aCodiceCDSSottoart And
            cd_cds = aCodiceCDSCap And
            cd_natura = aLinea.cd_natura And
            cd_funzione = aLinea.cd_funzione And
            ti_voce = CNRCTB001.SOTTOARTICOLO And
            cd_categoria = aCategoria
	  -- Per la categoria 1 entro con una cd_voce like aCodiceVoce=titolo
	  -- Per la categoria 2 identifico la voce dal campo cd_titolo_capitolo = aCodiceVoce=tipologia di intervento
	  and ((aCategoria = CNRCTB001.CATEGORIA2_SPESE_CNR and cd_titolo_capitolo = aCodiceVoce) Or
	       (aCategoria = CNRCTB001.CATEGORIA1_SPESE_CNR and cd_voce like aCodiceVoce||'.%'));

Return aVoce.CD_VOCE;

   exception when NO_DATA_FOUND then
    IBMERR001.RAISE_ERR_GENERICO('Sottoarticolo di spesa non trovata per voce del piano: '||aCodiceVoce||
    ' cd_cds = '||aCodiceCDSCap||
    ' AND CD_PROPRIO_VOCE = '||aCodiceCDSSottoart||
    ' AND CD_natura = '||aLinea.cd_natura||
    ' AND CD_funzione = '||aLinea.cd_funzione||
    ' ti_voce: '||CNRCTB001.SOTTOARTICOLO||
    ' AND cd_categoria ='||aCategoria||
    ' aCodiceVoce: '||aCodiceVoce
    );

End;

Else

----------------------------------------------------------------------------------------------
-------------------------------------- PER IL 2006  ------------------------------------------
----------------------------------------------------------------------------------------------

Begin

  -- recupero il TIPO NATURA (INT/EST)

  select NATURA.TIPO
  into   TIPO_NATURA
  from   NATURA, LINEA_ATTIVITA
  Where  LINEA_ATTIVITA.CD_CENTRO_RESPONSABILITA = aLinea.CD_CENTRO_RESPONSABILITA And
         LINEA_ATTIVITA.CD_LINEA_ATTIVITA        = aLinea.CD_LINEA_ATTIVITA And
         LINEA_ATTIVITA.CD_NATURA                = NATURA.CD_NATURA;

  -- recupero il CDS della SAC

  aCdsSAC := CNRCTB020.GETCDSSACVALIDO(aEV_in.ESERCIZIO);

  ------------------------------- NUOVA GESTIONE ---------------------------------

  -- Le regole di imputazione su bilancio finanziario CNR parte spese sono:
  -- I livello: CDS di origine (o Presidente dell'Area indicata da me)
  -- II livello: La natura, "1" per Fonti Interne e "2" per Fonti Esterne
  -- III livello: CDS/Area a seconda dei casi

   -- estraggo il CDS del CdR di Origine per il primo livello della voce
   -- estraggo la Natura per il secondo livello della voce
   -- estraggo l'area per il terzo livello della voce

   -- estraggo il CDS del CdR di I livello originale (per l'identificazione della voce)

Begin
   Select  CDS.CD_UNITA_ORGANIZZATIVA, CDS.CD_TIPO_UNITA
   Into    CD_CDS_PADRE, TIPO_CDR
   From    CDR, UNITA_ORGANIZZATIVA UO, UNITA_ORGANIZZATIVA CDS
   Where   CDR.CD_CENTRO_RESPONSABILITA = aLinea.CD_CENTRO_RESPONSABILITA And
           CDR.CD_UNITA_ORGANIZZATIVA = UO.CD_UNITA_ORGANIZZATIVA And
           UO.CD_UNITA_PADRE = CDS.CD_UNITA_ORGANIZZATIVA;
Exception
  When No_Data_Found Then
        CD_CDS_PADRE := Null;
        TIPO_CDR := Null;
End;

                        /***** inizio controllo CdR / CDS / AREE ********/

  If TIPO_CDR =  CNRCTB001.TIPOCDS_SAC Then
      aCDS_voce          := aCdsSAC.cd_unita_organizzativa;
      aCodiceCDSSottoart := aCdsSAC.cd_unita_organizzativa;

      -- RECUPERO PARTE/TITOLO/CATEGORIA FISSA DALLA CONFIGURAZIONE_CNR PER LA SAC
      ACD_TIT_CAP_FISSO := CNRCTB015.GETVAL01PERCHIAVE(aEV_in.ESERCIZIO, 'ELEMENTO_VOCE_SPECIALE', 'CD_TITOLO_CAPITOLO_BIL_CNR_SAC');
      aCategoria := CNRCTB001.CATEGORIA2_SPESE_CNR;

  Elsif TIPO_CDR In (CNRCTB001.TIPOCDS_IST, CNRCTB001.TIPOCDS_PNIR) Then

      aCDS_voce          := CNRUTL001.getCdsFromCdr(aLinea.cd_centro_responsabilita);
      aCodiceCDSSottoart := CNRUTL001.getCdsFromCdr(aLinea.cd_centro_responsabilita);

      -- RECUPERO PARTE/TITOLO/CATEGORIA FISSA DALLA CONFIGURAZIONE_CNR PER LA NON SAC
      ACD_TIT_CAP_FISSO := CNRCTB015.GETVAL01PERCHIAVE(aEV_in.ESERCIZIO, 'ELEMENTO_VOCE_SPECIALE', 'CD_TITOLO_CAPITOLO_BIL_CNR_NO_SAC');
      aCategoria := CNRCTB001.CATEGORIA1_SPESE_CNR;

  Elsif TIPO_CDR =  CNRCTB001.TIPOCDS_AREA Then   -- Se il dettaglio è di un'area,
                                                  -- vedo qual'è la UO Presidente dell'AREA

    Begin
     Select CD_UNITA_ORGANIZZATIVA
     Into   aUOPresidenteArea
     From   ass_uo_area
     Where  esercizio = aEV_in.ESERCIZIO And
            CD_AREA_RICERCA = CD_CDS_PADRE And
            FL_PRESIDENTE_AREA = 'Y';
    Exception
      When No_Data_Found Then
        IBMERR001.RAISE_ERR_GENERICO('UO presidente dell''AREA: '''||CD_CDS_PADRE||''' non trovata !!!');
    End;

   -- recupero il CDS presidente di Area

    Select CDS.*
    Into   aCDSPresidenteAREA
    From   UNITA_ORGANIZZATIVA UO, UNITA_ORGANIZZATIVA CDS
    Where  UO.CD_UNITA_ORGANIZZATIVA = aUOPresidenteArea And
           UO.CD_UNITA_PADRE = CDS.CD_UNITA_ORGANIZZATIVA And
           CDS.FL_CDS = 'Y';

Dbms_Output.PUT_LINE (' aCDSPresidenteAREA '||aCDSPresidenteAREA.cd_unita_organizzativa);

     If aCDSPresidenteAREA.cd_unita_organizzativa = CNRUTL001.getCdsFromCdr(aLinea.cd_centro_responsabilita) Then -- se il CDS Presidente dell'Area sono io
      aCDS_voce          := CNRUTL001.getCdsFromCdr(aLinea.cd_centro_responsabilita);
      aCodiceCDSSottoart := CD_CDS_PADRE;
     Elsif CNRUTL001.getCdsFromCdr(aLinea.cd_centro_responsabilita) != aCDSPresidenteAREA.cd_unita_organizzativa Then -- se il CDS Presidente dell'Area NON sono io
      aCDS_voce          := aCDSPresidenteAREA.cd_unita_organizzativa;
      aCodiceCDSSottoart := CD_CDS_PADRE;
     End If;

     ACD_TIT_CAP_FISSO := CNRCTB015.GETVAL01PERCHIAVE(aEV_in.ESERCIZIO, 'ELEMENTO_VOCE_SPECIALE', 'CD_TITOLO_CAPITOLO_BIL_CNR_NO_SAC');
     aCategoria := CNRCTB001.CATEGORIA1_SPESE_CNR;

    End If;

                        /***** fine controllo CdR / CDS / AREE ********/

If TIPO_NATURA = 'FIN' Then -- devo usare la voce con natura 1

   -- Leggo la voce_f su cui mettere i valori

   -- La ricerca è per:
   --   Esercizio
   --   tipo gestione
   --   tipo appartenenza
   --   cd_cds: codice del CDS di origine o del presidente dell'Area
   --   cd_natura = a seconda selle colonne del gestionale (Interna => Natura 1)
   --   cd_funzione = fissa a '01', unica utilizzabile
   --   ti_voce = 'E' (Sottoarticolo)
   --   cd_categoria = '1' fissa per tutti (anche per la SAC)
   --   cd_titolo_capitolo da configurazione (fisso a 1.01.1)

   Begin
     -- SELEZIONO LA VOCE PER LE SPESE INTERNE
     select   *
     into     aVoce
     from     voce_f
     Where    esercizio         = aEV_in.ESERCIZIO
	  and ti_gestione       = CNRCTB001.GESTIONE_SPESE
	  and ti_appartenenza   = CNRCTB001.APPARTENENZA_CNR
          and cd_proprio_voce   = aCodiceCDSSottoart
          and cd_cds            = aCDS_voce
          And CD_TITOLO_CAPITOLO = ACD_TIT_CAP_FISSO
          and cd_natura         = '1' -- (FONTI INTERNE)
          and cd_funzione       = '01'   -- DA SOSTITUIRE
          and ti_voce           = CNRCTB001.SOTTOARTICOLO
          and cd_categoria      = aCATEGORIA;

   Exception when NO_DATA_FOUND then
      IBMERR001.RAISE_ERR_GENERICO('2. Voce non trovata per Esercizio: '||aEV_in.ESERCIZIO||
      ', Titolo/Capitolo: '||ACD_TIT_CAP_FISSO||', CDS: '||aCDS_voce||
      ', Propria Voce: '||aCodiceCDSSottoart||', natura: 1, funzione: 01, cat.:'||aCATEGORIA);
   End;
End If;

    -- SECONDO GIRO PER LE SPESE ESTERNE

If TIPO_NATURA = 'FES' Then

   -- RECUPERO LA VOCE DA USARE PER LE SPESE ESTERNE (NAT 2)

   Begin
     select   *
     into     aVoce
     from     voce_f
     Where    esercizio         = aEV_in.ESERCIZIO
	  and ti_gestione       = CNRCTB001.GESTIONE_SPESE
	  and ti_appartenenza   = CNRCTB001.APPARTENENZA_CNR
          and cd_proprio_voce   = aCodiceCDSSottoart
          and cd_cds            = aCDS_voce
          And CD_TITOLO_CAPITOLO = ACD_TIT_CAP_FISSO
          and cd_natura         = '2' -- (FONTI ESTERNE)
          and cd_funzione       = '01'   -- DA SOSTITUIRE
          and ti_voce           = CNRCTB001.SOTTOARTICOLO
          and cd_categoria      = aCATEGORIA;

   Exception when NO_DATA_FOUND then
      IBMERR001.RAISE_ERR_GENERICO('1. Voce non trovata per Esercizio: '||aEV_in.ESERCIZIO||
      ', Titolo/Capitolo: '||ACD_TIT_CAP_FISSO||', CDS: '||aCDS_voce||
      ', Propria Voce: '||aCodiceCDSSottoart||', natura: 1, funzione: 01, cat.:'||ACATEGORIA);
   End;
End If;

Return AVOCE.CD_VOCE;

End;

End If;

End;

Procedure genera_varente_da_Varstanzres (aEsercizio         NUMBER,
                                         aPG_VARIAZIONE     NUMBER,
                                         aUser              VARCHAR2,
                                         cds_var_bil    Out VARCHAR2,
                                         es_var_bil     Out NUMBER,
                                         ti_app_var_bil Out CHAR,
                                         pg_var_bil     Out NUMBER) Is

  aVar          var_bilancio%rowtype;
  aVarDet       var_bilancio_det%rowtype;
  aTotVar       number(15,2);
  aOldVoceAgg   varchar2(30);
  aVoceF        voce_f%rowtype;
  aPgVar        number(10);
  aTSNow        DATE;
  TESTATA_CREATA CHAR(1);
  aSaldoCdrLinea        voce_f_saldi_cdr_linea%Rowtype;
  aVAR_STANZ_RES        VAR_STANZ_RES%Rowtype;
  variazione_da_fare    NUMBER;
  TIPO          VAR_BILANCIO.TI_VARIAZIONE%Type;
  CAUSALE       VAR_BILANCIO.CD_CAUSALE_VAR_BILANCIO%Type;
  recParametriCNR       PARAMETRI_CNR%Rowtype;
Begin
   recParametriCNR := CNRUTL001.getRecParametriCnr(aEsercizio);
   --27/11/2015: RafPag: Se è attiva la nuova gestione esco perchè non deve essere più creata
   --la variazione a livello Ente
   If recParametriCNR.fl_nuovo_pdg='Y' Then
     return;
   End If;

   Begin
    Select  *
    Into    aVAR_STANZ_RES
    From    VAR_STANZ_RES
    Where   ESERCIZIO      = aEsercizio And
            PG_VARIAZIONE  = aPG_Variazione;
   Exception
     When No_Data_Found Then
        IBMERR001.RAISE_ERR_GENERICO('Attenzione !!! Non esiste la Variazione allo Stanziamento Residuo '||aEsercizio||'/'||aPG_VARIAZIONE);
   End;

aTSNow := sysdate;

Begin
  Select 1
  Into   variazione_da_fare
  From   V_VAR_STANZ_RES_VOCE_CNR
  Where  ESERCIZIO = aVAR_STANZ_RES.ESERCIZIO And
         PG_VARIAZIONE = aVAR_STANZ_RES.PG_VARIAZIONE
  Group By ESERCIZIO_RES, ESERCIZIO_VOCE, /*TITOLO_CNR,*/ CD_CDS
  Having Sum(IM_VARIAZIONE) != 0;
Exception
  When No_Data_Found Then
        variazione_da_fare := 0; -- la variazione allo stanziamento residuo non necessita di variazione al bilancio
Dbms_Output.PUT_LINE ('C');
  When Too_Many_Rows Then
        variazione_da_fare := 1;
End;

--------- LOOP SULLA VARIAZIONE ALLO STANZIAMENTO RESIDUO

If variazione_da_fare = 1 Then

-- DEVO CAPIRE CHE COS'E', SE UNO STORNO O UNA ECONOMIA

Declare
  TOT_VARIAZIONE NUMBER;
Begin
  Select Sum(IM_VARIAZIONE)
  Into   TOT_VARIAZIONE
  From   V_VAR_STANZ_RES_VOCE_CNR
  Where  ESERCIZIO = aVAR_STANZ_RES.ESERCIZIO And
         PG_VARIAZIONE = aVAR_STANZ_RES.PG_VARIAZIONE;

  If TOT_VARIAZIONE = 0 Then
    TIPO := CNRCTB054.TI_VAR_STORNO_S;
    CAUSALE := CNRCTB054.CAU_VAR_STO_STANZ_RES;
  Elsif TOT_VARIAZIONE < 0 Then
    TIPO := CNRCTB054.TI_VAR_ECO_RES_S;
    CAUSALE := CNRCTB054.CAU_ECO_STANZ_RES;
  Elsif TOT_VARIAZIONE > 0 Then
    IBMERR001.RAISE_ERR_GENERICO('Attenzione !!! La Variazione allo Stanziamento Residuo '||aEsercizio||'/'||aPG_VARIAZIONE||' non può avere saldo positivo (maggiori spese).');
  End If;

End;


For VAR_STANZ_RAGGRUPPATE In (Select ESERCIZIO_RES, ESERCIZIO_VOCE, CD_VOCE_CNR,
                                          Sum(IM_VARIAZIONE) VAR_ENTE
                                   From   V_VAR_STANZ_RES_VOCE_CNR
                                   Where  ESERCIZIO = aVAR_STANZ_RES.ESERCIZIO And
                                          PG_VARIAZIONE = aVAR_STANZ_RES.PG_VARIAZIONE
                                   Group By ESERCIZIO_RES, ESERCIZIO_VOCE, CD_VOCE_CNR
                                   Having Sum(IM_VARIAZIONE) != 0) Loop

--------------------------- FASE FINALE, CREAZIONE DELLA VARIAZIONE ------------------------

-- Creo una variazione di bilancio con n dettagli corrispondenti ai dati di storno allo
-- Stanziamento Residuo

  If Nvl(TESTATA_CREATA, 'N') = 'N' Then -- PER CREARE UNA SOLA TESTATA PER OGNI VARIAZIONE
    aPgVar := CNRCTB055.getPgVariazione(aVAR_STANZ_RES.ESERCIZIO, CNRCTB020.GETCDCDSENTE (aVAR_STANZ_RES.ESERCIZIO));
    -- Creo la TESTATA DELLA variazione
    aVar.CD_CDS           := CNRCTB020.GETCDCDSENTE (aVAR_STANZ_RES.ESERCIZIO);
    aVar.ESERCIZIO        := aVAR_STANZ_RES.ESERCIZIO;
    aVar.ESERCIZIO_IMPORTI:= VAR_STANZ_RAGGRUPPATE.ESERCIZIO_RES;
    aVar.TI_APPARTENENZA  := CNRCTB001.APPARTENENZA_CNR;
    aVar.PG_VARIAZIONE    := aPgVar;
    aVar.DS_VARIAZIONE    := 'Variazione Automatica per approvazione Variazione allo stanziamento residuo '||aVAR_STANZ_RES.ESERCIZIO||'/'||aVAR_STANZ_RES.PG_VARIAZIONE;
    aVar.DS_DELIBERA      := null;
    aVar.TI_VARIAZIONE    := TIPO;
    aVar.CD_CAUSALE_VAR_BILANCIO := CAUSALE;
    aVar.STATO            := CNRCTB054.STATO_VARIAZIONE_PROVVISORIA;
    aVar.DUVA             := aTSNow;
    aVar.UTUV             := aUser;
    aVar.DACR             := aTSNow;
    aVar.UTCR             := aUser;
    aVar.PG_VER_REC       := 1;
    aVar.ESERCIZIO_VAR_STANZ_RES := aEsercizio;
    aVar.PG_VAR_STANZ_RES := aPG_VARIAZIONE;
    CNRCTB054.INS_VAR_BILANCIO(aVar);
    TESTATA_CREATA := 'Y';
  End If;

-- inserita la testata occorre inserire le righe

     aVarDet := null;

     aVarDet.CD_CDS             := aVar.cd_cds;
     aVarDet.ESERCIZIO          := aVar.esercizio;
     aVarDet.TI_APPARTENENZA    := aVar.ti_appartenenza;
     aVarDet.PG_VARIAZIONE      := aVar.pg_variazione;
     aVarDet.TI_GESTIONE        := CNRCTB001.GESTIONE_SPESE;
     aVarDet.CD_VOCE            := VAR_STANZ_RAGGRUPPATE.CD_VOCE_CNR;
     aVarDet.IM_VARIAZIONE      := VAR_STANZ_RAGGRUPPATE.VAR_ENTE;
     aVarDet.DUVA               := aTSNow;
     aVarDet.UTUV               := aUser;
     aVarDet.DACR               := aTSNow;
     aVarDet.UTCR               := aUser;
     aVarDet.PG_VER_REC         := 1;

--Dbms_Output.put_line (aVar.cd_cds||' '||aVar.esercizio||' '||aVar.ti_appartenenza||' '||aVar.pg_variazione||' '||CNRCTB001.GESTIONE_SPESE||' '||VAR_STANZ_RAGGRUPPATE.CD_VOCE_CNR);

     CNRCTB054.INS_VAR_BILANCIO_DET(aVarDet);

/* NON OCCORRE AGGIORNARE I SALDI PER CDR/LINEA */

End Loop;

End If;

cds_var_bil    := aVar.cd_cds;
es_var_bil     := aVar.esercizio;
ti_app_var_bil := aVar.ti_appartenenza;
pg_var_bil     := aVar.pg_variazione;

End;

end;


