CREATE OR REPLACE package CNRMIG030 is
--
-- CNRMIG030 - Package di migrazione di residui dal sistema SCI al sistema CIR
-- Date: 14/07/2006
-- Version: 1.38
--
-- Dependency:
--
-- History:
--
-- Date: 25/09/2002
-- Version: 1.0
-- Creazione
--
-- Date: 25/09/2002
-- Version: 1.1
-- Modifica assegamento della data di scadenza incasso di una scadenza di accertamento.
--
-- Date: 22/10/2002
-- Version: 1.2
-- Nuova versione integrata con la migrazione dei residui attivi e passivi.
--
-- Date: 28/10/2002
-- Version: 1.3
-- Modificata la valorizzazione dei terzi per accertamenti e impegni.
--
-- Date: 29/10/2002
-- Version: 1.4
-- Aggiornamento voce_f_saldi_cmp.
--
-- Date: 31/10/2002
-- Version: 1.5
-- Modificata funzione getTerzo, per la gestione del terzo non di default
--
-- Date: 11/11/2002
-- Version: 1.6
-- Inserito nuovo campo ds_terzo, modificata
-- descrizione accertamento = ds_acc + cd_terzo + ds_terzo se cd_terzo <>2
--
-- Date: 14/11/2002
-- Version: 1.7
-- Sono state trasformati in costanti globali le variabili
-- Esercizio e Esercizio Destinazione
--
-- Date: 21/11/2002
-- Version: 1.8
-- Inserto procedura di cancellazione dei residui attivi e passivi
--
-- Date: 26/11/2002
-- Version: 1.9
-- Inserita gestione dei log
-- lResiduoAttSci.cd_terzo <> to_char(lCdTerzoSpeciale)
--
-- Date: 26/11/2002
-- Version: 1.10
-- Inserto lock table al posto di for update nowait nei cursori per
-- CNR_RESIDUI_ATTIVI e PASSIVI
--
-- Date: 27/11/2002
-- Version: 1.11
-- Inserito logging nel caso in cui non avviene l'inserimento dell'accertamento
-- nell'esercizio origine.
--
-- Date: 27/11/2002
-- Version: 1.12
-- Corretta procedura di creazione residuo attivo
--
-- Date: 28/11/2002
-- Version: 1.13
-- Introdotta modifica importo residuo passivo - Parte 1
--
-- Date: 28/11/2002
-- Version: 1.14
-- Introdotta modifica importo residuo passivo - Parte 2
--
-- Date: 29/11/2002
-- Version: 1.15
-- Introdotta modifica importo residuo passivo - Parte 3
--
-- Date: 02/12/2002
-- Version: 1.16
-- Attribuzione del progressivo dell'impegno e dell'accertamento in base al suo
-- esercizio di creazione (esercizio nella tabella CNR_) - Parte 4
--
-- Date: 09/12/2002
-- Version: 1.17
-- Inserita cancellazione delle tabelle obbligazione*_s (alimentate dal trigger)
--
-- Date: 09/12/2002
-- Version: 1.18
-- corretta where per modifica scadenza voce
--
-- Date: 09/12/2002
-- Version: 1.19
-- gestione salto residui passivi
--
-- Date: 11/12/2002
-- Version: 1.20
-- Modificato aggiornamento della voce f_ saldi cmp nel caso in cui l'importo associato non permetteva la modifica
--
-- Date: 12/12/2002
-- Version: 1.21
-- Modificato l'aggiornamento delle scadenze dei residui attivi e passivi per esercizio 2002
--
-- Date: 13/12/2002
-- Version: 1.22
-- Modificato l'aggiornamento degli accertamenti che erano distribuiti su diverse LA
--
-- Date: 16/12/2002
-- Version: 1.23
-- Modificato l'aggiornamento degli accertamenti che erano distribuiti su diverse LA
-- e cancellazione delle scadenze azzerate.
--
-- Date: 07/01/2003
-- Version: 1.24
-- Modificata la descrizione dell'accertamento in :
-- cnr_residui_attivi.cd_terzo + cnr_residui_attivi.ds_terzo + cnr_residui_attivi.ds_accertamento_origine
--
-- Date: 09/01/2003
-- Version: 1.25
-- Inserito substr nella costruzione di ds_accertamento
--
-- Date: 10/01/2003
-- Version: 1.26
-- Corretto modalita estrazione del residuo attivo
--
-- Date: 13/01/2003
-- Version: 1.27
-- Corretta data di scadenza dell'accertamento 2003 e obbligazione 2003
--
-- Date: 15/01/2003
-- Version: 1.28
-- Modificato aggiornamento della tabella VOCE_F_SALDI_CMP, ora viene modificato
-- anche il campo IM_STANZ_INIZIALE_A1 che viene sempre posto uguale al valore
-- del campo IM_OBBLIG_IMP_ACR.
--
-- Date: 20/01/2003
-- Version: 1.29
-- Modificata la data di registrazione di tutti i residui (attivi e passivi)
-- portata da 31/12/2002 a 01/01/2003
--
-- Version: 1.30
-- Date: 05/05/2003
-- inserito restrizione substr(to_char(pg_accertamento),1,4) = lResiduoAttSci.esercizio
-- quando si recupera accertamento
--
-- Version: 1.31
-- Date: 05/05/2003
-- Inserito log step by step
--
-- Date: 14/05/2003
-- Version: 1.32
-- Corretta modalita di cancellazione Obbligazione/Accertamento
-- in fase di cancellazioen se un Obbligazione/Accertamento risulta avere un
-- doc amm associato ma in stato annullato, allora l'Obbligazione/Accertamento
-- non viene cancellata ma annullata logicamente
--
-- Date: 14/05/2003
-- Version: 1.33
-- Corretta codice sqlcode gestione cancellamento logico Obbligazione/Accertamento
--
-- Date: 14/05/2003
-- Version: 1.34
-- Corretta codice sqlcode gestione cancellamento logico Obbligazione/Accertamento
-- inserito filtro sullo stato di stornato di un obbligazione e dt_cancellazione di un
-- accertamento.
--
-- Date: 30/05/2003
-- Version: 1.35
-- Modificati log di cancellazione accertamento
--
-- Date: 05/06/2003
-- Version: 1.36
-- Inserita condizione di filtro su stato obbligazione per la cancellazione
-- Inserita condizione di filtro su dt_cancellazione di accertamento per la cancellazione
--
-- Date: 16/09/2003
-- Version: 1.37
-- Inserita aggiornamento cd_cds_origine, cd_uo_origine, cd_voce, cd_elemento_voce,
-- la modifica dei campi sopra citati ? scatenata se viene riscontrato un cambiamento
-- nel campo cd_voce .
--
-- Date: 14/07/2006
-- Version: 1.38
-- Gestione Impegni/Accertamenti Residui:
-- aggiornata la funzione per tener conto anche del campo Esercizio Originale Impegno/Accertamento
--
-- Constants:
--
--	inizio scommentare in collaudo
--	gcEsercizio number := 2001; -- Esercizio in cui viene creato l'accertamento
--	gcEsercizioDest number := 2002; -- Esercizio di destinazione della migrazione
--  fine scommentare in collaudo
--
--	inizio commentare in collaudo
	gcEsercizio number := 2002; -- Esercizio in cui viene creato l'accertamento
	gcEsercizioDest number := 2003; -- Esercizio di destinazione della migrazione
--  fine commentare in collaudo
--
	gPgLog number;
	cgUtente varchar2(20) := '$$$$$MIGRAZIONE$$$$$';
-- variabili
	gMsgLog varchar2(300);
--
-- Functions e Procedures:
   procedure migrazione_residui_attivi ;

   function ChkAccertamento2002(aAccertamento accertamento%rowtype, aSysdate date) return boolean ;

   procedure crea_residuo_attivo (aAcc2002 accertamento%rowtype
			  					  ,aEsercizio_destinazione esercizio.ESERCIZIO%type
								  ,aSysdate date);


   procedure ChkResiduoAtt2003(aResiduo2003 accertamento%rowtype);

   procedure CreaResiduoAtt2003 (aResiduo2003 accertamento%rowtype) ;

   function CreaScad2003(aResiduo2003 accertamento%rowtype) return accertamento_Scadenzario%rowtype;

   function CreaScadVoce2003 (aScad accertamento_scadenzario%rowtype) return accertamento_scad_voce%rowtype;

   procedure ModPlusAcc2003(aResiduo2003New accertamento%rowtype, lResiduo2003Old accertamento%rowtype);

   procedure CreaNuovaScad2003(aResiduo2003New accertamento%rowtype, aResiduo2003Old accertamento%rowtype);

   procedure ModMinusAcc2003(aResiduo2003New accertamento%rowtype, aResiduo2003Old accertamento%rowtype);

   function ResiduoAttAssociato(aAcc accertamento%rowtype) return boolean;

   procedure migrazione_residui_passivi ;

   function ChkObbligazione(aImpegno in out obbligazione%rowtype, aResiduoSci cnr_residui_passivi%rowtype, aSysdate date) return boolean;

   procedure crea_residuo_passivo (aImpegno obbligazione%rowtype
			  					    ,aResiduoSci cnr_residui_passivi%rowtype
									,aSysdate date);

   procedure ChkPresenzaResiduoPas(aImpegnoResiduo obbligazione%rowtype, aSysdate date, aDelta in out number) ;

   procedure PreparaScadResiduoPas (aImpegnoResiduo obbligazione%rowtype, aSysdate date);

   procedure ObbligazioneAssDocAmm(aObb obbligazione%rowtype,aObbScadenzario in out obbligazione_scadenzario%rowtype, aAssociato in out boolean)   ;

   function GetScadResPas(aResiduo obbligazione%rowtype) return obbligazione_Scadenzario%rowtype;

   function GetScadResPasVoce (aScad obbligazione_scadenzario%rowtype, aEsercizioObbPrincipale esercizio.ESERCIZIO%type) return obbligazione_scad_voce%rowtype;

   function getTerzo(aCdTerzoSci cnr_residui_Attivi.cd_terzo%type) return terzo%rowtype;

   procedure SetResiduiAttiviCanc(aData date );

   procedure SetResiduiPassiviCanc(aData date);

END;


CREATE OR REPLACE package body CNRMIG030 is

	procedure migrazione_residui_attivi  is

--	lResiduoAttSci cnr_residui_attivi%rowtype;
	lNumResiduiAttSci number;

	lContatore number;
	lPgAccIniziale number;
	lPgAccertamento number;
	lDataMigrazione date;

	lAccCir accertamento%rowtype;
	lAccScadCir accertamento_scadenzario%rowtype;
	lAccScadVoceCir accertamento_scad_voce%rowtype;
	lCds unita_organizzativa%rowtype;
	lCdr cdr%rowtype;
	lElemVoce elemento_voce%rowtype;

	lTerzo terzo%rowtype;
	lCdTerzo cnr_residui_Attivi.cd_terzo%type;

	lCdTerzoSpeciale terzo.cd_terzo%type;

	lPgUltimoScarico number;

	begin
	 	 gPgLog := ibmutl200.LOGSTART('Migrazione Residui Attivi ' , cgUtente, 1, 1);
		 begin
		 	 gMsgLog := 'Impossibile recuperare il pg_caricamento dalla tabella CNR_RESIDUI_ATTIVI';
			 -- Recuperiamo il pg_ultimo_caricamento SCI
			 select nvl(max(pg_caricamento),0)
			 into lPgUltimoScarico
			 from cnr_residui_attivi;

		 	 gMsgLog := 'Impossibile recuperare il CdS';
	  		 -- Recuperiamo il cds realtivo all'ente
	     	 select *
	  		 into lCds
	     	 from  unita_organizzativa
	     	 where cd_tipo_unita = cnrctb020.tipo_ente
	  		 and   fl_cds = 'Y';

		 	 gMsgLog := 'Impossibile recuperare il CDR';
	  		 -- Recuperiamo il cdr realtivo all'ente
	     	 lCdr := cnrctb020.GETCDRENTE;

		 	 gMsgLog := 'In Configurazione CNR manca il TERZO_SPECIALE, CODICE_DIVERSI_IMPEGNI';
			 select im01
			 into lCdTerzoSpeciale
			 from configurazione_cnr
			 where cd_chiave_primaria = 'TERZO_SPECIALE'
			 and cd_chiave_secondaria = 'CODICE_DIVERSI_IMPEGNI';

		 exception when no_data_found then
			  	   ibmutl200.logInf(gPgLog ,gMsgLog , '','');
         end;

		 lDataMigrazione := sysdate;
		 lNumResiduiAttSci := 0;

		 lContatore :=0;
	 	 LOCK TABLE CNR_RESIDUI_ATTIVI IN EXCLUSIVE MODE NOWAIT;

		 for lResiduoAttSci in (SELECT * FROM CNR_RESIDUI_ATTIVI
		 	 				    where pg_caricamento = lPgUltimoScarico)
		 loop
	   		 begin
savepoint altro_accertamento;

				 lContatore := lContatore + 1;
			 	 gMsgLog := 'Impossibile recuperare il progressivo dell''accertamento cd _residuo = '  || lResiduoAttSci.cd_residuo ||' esercizio = '|| lResiduoAttSci.esercizio ;
		         ibmutl200.logInf(gPgLog ,'Processando residuo Attivo num '|| lContatore ||' Cds = '|| lResiduoAttSci.CD_CDS || ' - Uo = ' || lResiduoAttSci.CD_UNITA_ORGANIZZATIVA || ' - Esercizio = '|| lResiduoAttSci.ESERCIZIO || ' - residuo = ' || lResiduoAttSci.CD_RESIDUO || ' - Progressivo = ' || lResiduoAttSci.PG_CARICAMENTO, '', '');
				 -- Recuperiamo il progressivo iniziale da assegnare all'accertamento
				 select nvl(max(substr(to_char(pg_accertamento),5,6)),0) + 1
		 		 into lPgAccIniziale
				 from accertamento
				 where cd_Cds = lCds.cd_unita_organizzativa
				 and   esercizio=gcEsercizio
                                 and   esercizio_originale=lResiduoAttSci.esercizio
				 and   cd_tipo_documento_cont = CNRCTB018.TI_DOC_ACC
				 and   riportato='Y'
				 and   length(to_char(pg_accertamento))=10
				 and   substr(to_char(pg_accertamento),1,4) = lResiduoAttSci.esercizio;

			     lCdTerzo := lResiduoAttSci.cd_terzo;
			 	 lterzo := getTerzo(lCdTerzo);

			 	 lNumResiduiAttSci := lNumResiduiAttSci + 1;

				 -- Recuperiamo l'elemento voce
			 	 gMsgLog := 'In elemento_voce manca la voce : appartenenza'||lResiduoAttSci.ti_appartenenza ||' gestione '||lResiduoAttSci.ti_gestione || ' elemento '||lResiduoAttSci.cd_elemento_voce;

		     	 select *
		  		 into lElemVoce
		     	 from  elemento_voce
		     	 where esercizio	    = gcEsercizio
		  		 and   ti_appartenenza  = lResiduoAttSci.ti_appartenenza
		  		 and   ti_gestione	    = lResiduoAttSci.ti_gestione
				 and   cd_elemento_voce = lResiduoAttSci.cd_elemento_voce;

				 lPgAccertamento := to_number(to_char(lResiduoAttSci.esercizio) || lpad(to_char(lPgAccIniziale),6,'0'));

				 lAccCir.CD_CDS                       := lResiduoAttSci.cd_cds;
				 lAccCir.ESERCIZIO				   	  := gcEsercizio;
				 lAccCir.ESERCIZIO_ORIGINALE          := lResiduoAttSci.esercizio;
				 lAccCir.PG_ACCERTAMENTO 			  := lPgAccertamento;
		 		 lAccCir.CD_TIPO_DOCUMENTO_CONT 	  := cnrctb018.TI_DOC_ACC;
				 lAccCir.CD_UNITA_ORGANIZZATIVA       := lResiduoAttSci.CD_UNITA_ORGANIZZATIVA;
				 lAccCir.CD_CDS_ORIGINE			  	  := lResiduoAttSci.CD_CDS_ORIGINE;
				 lAccCir.CD_UO_ORIGINE			   	  := lResiduoAttSci.CD_UO_ORIGINE;
				 lAccCir.TI_APPARTENENZA              := 'C';
				 lAccCir.TI_GESTIONE			   	  := 'E';
				 lAccCir.CD_ELEMENTO_VOCE		   	  := lResiduoAttSci.CD_ELEMENTO_VOCE;
				 lAccCir.CD_VOCE				   	  := lResiduoAttSci.CD_VOCE;
				 lAccCir.DT_REGISTRAZIONE		   	  := to_date('0101'||gcEsercizioDest,'ddmmyyyy');
				 lAccCir.DS_ACCERTAMENTO              := substr(lResiduoAttSci.cd_terzo || ' - ' || lResiduoAttSci.ds_terzo || ' - ' || lResiduoAttSci.ds_accertamento_origine,1,300) ;

--				 lAccCir.DS_ACCERTAMENTO              := 'ACCERTAMENTO RESIDUO DA MIGRAZIONE';
-- 				 if lResiduoAttSci.cd_terzo <> to_char(lCdTerzoSpeciale) then
-- 				 	lAccCir.DS_ACCERTAMENTO              :=  lAccCir.DS_ACCERTAMENTO || ' '|| to_char(lCdTerzoSpeciale) || ' '|| lResiduoAttSci.ds_terzo;
-- 				 end if;

				 lAccCir.NOTE_ACCERTAMENTO            := NULL;
				 lAccCir.CD_TERZO				   	  := lterzo.cd_terzo;  -- Terzi diversi per migrazione
				 lAccCir.IM_ACCERTAMENTO		   	  := lResiduoAttSci.IM_RESIDUO;
				 lAccCir.DT_CANCELLAZIONE             := NULL;
				 lAccCir.CD_RIFERIMENTO_CONTRATTO     := NULL;
				 lAccCir.DT_SCADENZA_CONTRATTO	   	  := NULL;
				 lAccCir.CD_FONDO_RICERCA		   	  := NULL;
				 lAccCir.FL_PGIRO				   	  := lElemVoce.FL_PARTITA_GIRO;
				 lAccCir.RIPORTATO				   	  := 'N';
				 lAccCir.DACR					   	  := lDataMigrazione;
				 lAccCir.UTCR					   	  := cgUtente;
				 lAccCir.DUVA					   	  := lDataMigrazione;
				 lAccCir.UTUV					   	  := cgUtente;
				 lAccCir.PG_VER_REC				  	  := 1;
				 lAccCir.CD_CDS_ORI_RIPORTO    		  := NULL;
				 lAccCir.ESERCIZIO_ORI_RIPORTO	   	  := NULL;
				 lAccCir.ESERCIZIO_ORI_ORI_RIPORTO	   	  := NULL;
				 lAccCir.PG_ACCERTAMENTO_ORI_RIPORTO  := NULL;
				 lAccCir.ESERCIZIO_COMPETENZA	   	  := gcEsercizio;
				 lAccCir.PG_ACCERTAMENTO_ORIGINE      := lResiduoAttSci.cd_residuo;


				 lAccScadCir.CD_CDS := lAccCir.CD_CDS;
				 lAccScadCir.ESERCIZIO := lAccCir.ESERCIZIO;
				 lAccScadCir.ESERCIZIO_ORIGINALE := lAccCir.ESERCIZIO_ORIGINALE;
				 lAccScadCir.PG_ACCERTAMENTO := lAccCir.PG_ACCERTAMENTO;
				 lAccScadCir.PG_ACCERTAMENTO_SCADENZARIO := 1;
				 lAccScadCir.DT_SCADENZA_EMISSIONE_FATTURA := to_Date('3112'||gcEsercizio,'ddmmyyyy');
				 lAccScadCir.DT_SCADENZA_INCASSO := to_Date('3112'||gcEsercizio,'ddmmyyyy');
				 lAccScadCir.DS_SCADENZA := 'SCADENZA UNICA PER RESIDUO DI MIGRAZIONE';
				 lAccScadCir.IM_SCADENZA := lAccCir.IM_ACCERTAMENTO;
				 lAccScadCir.IM_ASSOCIATO_DOC_AMM := 0;
				 lAccScadCir.IM_ASSOCIATO_DOC_CONTABILE := 0;
				 lAccScadCir.DACR := lDataMigrazione;
				 lAccScadCir.UTCR := cgUtente;
				 lAccScadCir.DUVA := lDataMigrazione;
				 lAccScadCir.UTUV := cgUtente;
				 lAccScadCir.PG_VER_REC := 1;


				 lAccScadVoceCir.CD_CDS := lAccCir.CD_CDS;
				 lAccScadVoceCir.ESERCIZIO := lAccCir.ESERCIZIO;
				 lAccScadVoceCir.ESERCIZIO_ORIGINALE := lAccCir.ESERCIZIO_ORIGINALE;
				 lAccScadVoceCir.PG_ACCERTAMENTO := lAccCir.PG_ACCERTAMENTO;
				 lAccScadVoceCir.PG_ACCERTAMENTO_SCADENZARIO := lAccScadCir.PG_ACCERTAMENTO_SCADENZARIO;
				 lAccScadVoceCir.CD_CENTRO_RESPONSABILITA := lCdr.cd_centro_Responsabilita;
				 lAccScadVoceCir.CD_LINEA_ATTIVITA := CNRCTB015.GETVAL02PERCHIAVE(CNRCTB035.LA_SPECIALE,CNRCTB035.LA_ENTRATA_ENTE);
				 lAccScadVoceCir.IM_VOCE := lAccCir.IM_ACCERTAMENTO;
				 lAccScadVoceCir.CD_FONDO_RICERCA := NULL;
				 lAccScadVoceCir.DACR := lDataMigrazione;
				 lAccScadVoceCir.UTCR := cgUtente;
				 lAccScadVoceCir.DUVA := lDataMigrazione;
				 lAccScadVoceCir.UTUV := cgUtente;
				 lAccScadVoceCir.PG_VER_REC := 1;


				 -- Controllare che il residuo non sia gia stato migrato
			 	 if not ChkAccertamento2002(lAccCir, lAccCir.duva) then
				 	 cnrctb035.INS_ACCERTAMENTO(lAccCir);
					 cnrctb035.INS_ACCERTAMENTO_SCADENZARIO(lAccScadCir);
					 cnrctb035.INS_ACCERTAMENTO_SCAD_VOCE(lAccScadVoceCir);

					 crea_residuo_attivo(lAccCir,gcEsercizioDest,lDataMigrazione);
			     else
				 	 begin
				 	 	 gMsgLog := 'Impossibile recuperare l''accertamento precedentemente migrato, accertamento_origine ' || to_char(lAccCir.pg_accertamento_origine);
					 	 select *
						 into lAccCir
						 from accertamento
						 where pg_accertamento_origine = lAccCir.pg_accertamento_origine
						 and   cd_tipo_documento_cont = CNRCTB018.TI_DOC_ACC
						 and   esercizio = gcEsercizio
						 and   esercizio_originale = lResiduoAttSci.esercizio
						 and   substr(to_char(pg_accertamento),1,4) = lResiduoAttSci.esercizio
						 and   length(to_char(pg_accertamento)) = 10;

						 crea_residuo_attivo(lAccCir,gcEsercizioDest,lDataMigrazione);

				     exception when no_Data_found then
				 		   ibmutl200.logInf(gPgLog ,gMsgLog, '', '');
					 end;
				 end if;

			 exception when no_data_found then
			 		   ibmutl200.logInf(gPgLog ,gMsgLog, '', '');
 					   when others then
 			 		   ibmutl200.logInf(gPgLog ,'residuo origine '|| lResiduoAttSci.cd_residuo || ' accertamento '|| lAccCir.pg_accertamento || sqlerrm (sqlcode), '', '');
 					   lNumResiduiAttSci := lNumResiduiAttSci - 1;
 			 		   rollback to savepoint altro_accertamento;
		 	 end;
		 end loop;

		 -- Cancelliamo i residui attivi
		 SetResiduiAttiviCanc(lDataMigrazione);

	end ;

	------------------------------------------------------------------------

	-- Controlla se il residuo risulta gia inserito
	function ChkAccertamento2002(aAccertamento accertamento%rowtype, aSysdate date) return boolean is
	lAcc2002  accertamento%rowtype;
	lScadLibera     accertamento_scadenzario%rowtype;
	lScad			accertamento_scadenzario%rowtype;
	lScadVoce  		accertamento_scad_voce%rowtype;
	lImportoLibero2003 number(15,2);
	lGiaAssociato boolean;
	lGiaInserito boolean;
	lAccModificabile boolean;
	lDelta number(15,2);
	lTrovataScadenza boolean;
	-- Tipo Cambio 1 : Creditore diverso         => lTipoModifica = 1
	-- Tipo Cambio 2 : Importo nuovo > vecchio   => lTipoModifica = 2
	-- Tipo Cambio 3 : Importo nuovo < vecchio   => lTipoModifica = 3
	-- Tipo Cambio 4 : Voce diversa 	 		 => lTipoModifica = 4
	-- Tipo Cambio 5 : UO diversa 		 		 => lTipoModifica = 5
	lTipoModifica number;

	begin
		begin
			 select *
			 into lAcc2002
			 from accertamento acc
			 where acc.pg_accertamento_origine  = aAccertamento.pg_Accertamento_origine
			 and   substr(to_char(acc.pg_accertamento),1,4)  = substr(to_char(aAccertamento.pg_accertamento),1,4)
			 and   length(to_char(acc.pg_accertamento))  = 10
			 and cd_tipo_documento_cont = CNRCTB018.TI_DOC_ACC
			 and esercizio = gcEsercizio
			 and esercizio_originale = To_Number(substr(to_char(acc.pg_accertamento),1,4))
			 for update nowait;

		 	 lGiaInserito := TRUE;
		exception when no_data_found then
			 	lGiaInserito := FALSE;
		end ;
		begin
			 if lGiaInserito then
				 -- Controlliamo se l'accertamento residuo principale risulta gia
				 -- associato a documenti amministrativi
				 lGiaAssociato := ResiduoAttAssociato(lAcc2002);

				 ----------------------------------------------------------------------
				 -- Identificare il tipo di eventuale cambiamento del residuo        --
				 ----------------------------------------------------------------------
				 -- Tipo Cambio 1 : Creditore diverso se e solo se Residuo
				 --		 		  	principale non associato a documenti amministrativi
				 -- Tipo Cambio 2 : Importo diverso nuovo > vecchio
				 -- Tipo Cambio 3 : Importo diverso nuovo < vecchio
				 -- Tipo Cambio 4 : cd_voce diversa gestito se e solo se Residuo
				 --		 		  	principale non associato a documenti amministrativi
				 -- Tipo Cambio 5 : UO diversa se e solo se Residuo
				 --		 		  	principale non associato a documenti amministrativi
				 lTipoModifica :=0;

				 if aAccertamento.CD_TERZO != lAcc2002.CD_TERZO then
				 	lTipoModifica := 1;
 				 	if not lGiaAssociato then
 					   update accertamento
					   set cd_terzo = lAcc2002.cd_terzo,
					   	   utuv = lAcc2002.utuv,
					   	   duva = lAcc2002.duva,
						   pg_ver_rec = pg_ver_rec +1
					   where cd_cds = aAccertamento.cd_cds
					   and   esercizio =  aAccertamento.esercizio
					   and   esercizio_originale =  aAccertamento.esercizio_originale
					   and   pg_accertamento = aAccertamento.pg_accertamento;
 					end if;
				 end if;

				 if aAccertamento.IM_ACCERTAMENTO > lAcc2002.IM_ACCERTAMENTO then
				 	lTipoModifica := 2;
				 	 -- Siamo nel caso in cui stiamo inserendo un accertamento residuo
					 -- che risulta gia inserito nella tabella ACCERTAMENTI. Quindi
					 -- siccome risulta cambiato l'importo dell'accertamento occorre
					 -- seguire il seguente algoritmo :
					 -- Importo del accertamento NUOVO (che vogliamo inserire) risulta
					 --    MAGGIORE dell'importo dell'accertamento Principale
					 --	   (gia presente nella tabella esercizio) in questo caso  =>
					 --    viene modificato l'importo relativo alla prima scadenza
					 --    non associata a documenti amministrativi, se non esiste
					 --	   tale scadenza allora se ne crea una nuova con importo pari
					 --	   al delta IMPORTO ACC NUOVO - IMPORTO ACC PRINCIPALE

					 -- Recuperiamo l'unica scadenza scadenza
					 select *
					 into lScadLibera
					 from accertamento_scadenzario
					 where cd_cds 		   = lAcc2002.cd_cds
					 and   esercizio 	   = lAcc2002.esercizio
					 and   esercizio_originale = lAcc2002.esercizio_originale
					 and   pg_accertamento = lAcc2002.pg_accertamento
					 for update nowait;

					 -- Impostiamo i dati della scadenza libera
					 lScadLibera.IM_SCADENZA := aAccertamento.IM_ACCERTAMENTO;
				   	 lScadLibera.DUVA 		 := aSysdate;
				     lScadLibera.UTUV 		 := cgUtente;
				     lScadLibera.PG_VER_REC  := lScadLibera.PG_VER_REC + 1;

					 -- Recuperiamo l'unico dettaglio voce associato alla scadenza
					 select *
					 into lScadVoce
					 from accertamento_scad_voce
					 where cd_cds 		   = lAcc2002.cd_cds
					 and   esercizio 	   = lAcc2002.esercizio
					 and   esercizio_originale = lAcc2002.esercizio_originale
					 and   pg_accertamento = lAcc2002.pg_accertamento
					 and   pg_accertamento_scadenzario = lScadLibera.pg_accertamento_scadenzario
					 for update nowait;

					 -- Impostiamo i dati del dettaglio voce
					 lScadVoce.IM_VOCE		:= lScadLibera.IM_SCADENZA;
					 lScadVoce.DUVA			:= aSysdate;
					 lScadVoce.UTUV			:= cgUtente;
					 lScadVoce.PG_VER_REC	:= lScadVoce.PG_VER_REC + 1;

					 -- Aggiornamento Accertamento, scadenza, voce
					 update accertamento
					 set im_accertamento = aAccertamento.im_accertamento
						 ,utuv = cgUtente
						 ,duva = aSysdate
					 	 ,pg_ver_rec 	 = PG_VER_REC + 1
					 where cd_cds          = lAcc2002.cd_cds
					 and   esercizio 	   = lAcc2002.esercizio
					 and   esercizio_originale = lAcc2002.esercizio_originale
					 and   pg_accertamento = lAcc2002.pg_accertamento;

			 		 -- Aggiornamento scadenza
					 update accertamento_scadenzario
					 set  IM_SCADENZA = lScadLibera.IM_SCADENZA
					 	 ,DUVA = lScadLibera.DUVA
						 ,UTUV = lScadLibera.UTUV
						 ,PG_VER_REC = lScadLibera.PG_VER_REC
					 where cd_cds                      = lScadLibera.cd_cds
					 and   esercizio 				   = lScadLibera.esercizio
					 and   esercizio_originale = lScadLibera.esercizio_originale
					 and   pg_accertamento 			   = lScadLibera.pg_accertamento
					 and   pg_accertamento_scadenzario = lScadLibera.pg_Accertamento_scadenzario;

			 		 -- Aggiornamento scadenza voce
					 update accertamento_scad_voce
					 set  IM_VOCE = lScadVoce.IM_VOCE
					 	 ,DUVA = lScadVoce.DUVA
						 ,UTUV = lScadVoce.UTUV
						 ,PG_VER_REC = lScadVoce.PG_VER_REC
					 where cd_cds                      = lScadVoce.cd_cds
					 and   esercizio 				   = lScadVoce.esercizio
					 and   esercizio_originale = lScadVoce.esercizio_originale
					 and   pg_accertamento 			   = lScadVoce.pg_accertamento
					 and   pg_accertamento_scadenzario = lScadVoce.pg_Accertamento_scadenzario
					 and   cd_centro_responsabilita    = lScadVoce.cd_centro_responsabilita
					 and   cd_linea_attivita 		   = lScadVoce.cd_linea_attivita;
				 end if;
				 if aAccertamento.IM_ACCERTAMENTO < lAcc2002.IM_ACCERTAMENTO then
				 	lTipoModifica := 3;
				 	 -- Siamo nel caso in cui stiamo inserendo un accertamento residuo
					 -- che risulta gia inserito nella tabella ACCERTAMENTI. Quindi
					 -- siccome risulta cambiato l'importo dell'accertamento occorre
					 -- seguire il seguente algoritmo :
					 -- Importo del accertamento NUOVO (che vogliamo inserire) risulta
					 --    minore dell'importo dell'accertamento Principale
					 --	   (gia presente nella tabella esercizio) in questo caso  =>
					 --    viene modificato l'importo relativo alla prima scadenza
					 --    non associata a documenti amministrativi, se cio non basta
					 --    allora si passa alla seconda scadenza libera e cosi via. Se
					 --    non ci sono sufficienti scadenze libere per soddisfare la
					 --    diminuzione dell'importo dell'accertamento allora si blocca
					 --    il riporto.
					 lDelta := lAcc2002.IM_ACCERTAMENTO - aAccertamento.IM_ACCERTAMENTO;

					 lTrovataScadenza := false;

					 -- Controlliamo se l'accertamento 2002 puo essere modificato
					 -- lAccModificabile
					 select nvl(sum(im_scadenza),0)
					 into lImportoLibero2003
					 from accertamento_scadenzario
					 where cd_cds          = lAcc2002.cd_cds
					 and   esercizio 	   = gcEsercizioDest
					 and   esercizio_originale = lAcc2002.esercizio_originale
					 and   pg_accertamento = lAcc2002.pg_Accertamento
					 and   nvl(im_associato_doc_amm,0) = 0;

					 if lImportoLibero2003 >= lDelta then
					 	lAccModificabile := true;
					 else
					 	lAccModificabile := false;
				 	 	gMsgLog := 'Impossibile riportare il residuo - non bastano le scadenze libere, esercizio =  ' || substr(to_char(aAccertamento.pg_accertamento),1,4) || ' cd_residuo = '|| aAccertamento.pg_accertamento_origine;
		 		     	ibmutl200.logInf(gPgLog ,gMsgLog, '', '');
					 end if;


					 if lAccModificabile then
					 	select *
						into lScad
						from accertamento_scadenzario
						where cd_cds                      = lAcc2002.cd_cds
						and   esercizio                   = lAcc2002.esercizio
	 				        and   esercizio_originale = lAcc2002.esercizio_originale
						and   pg_Accertamento 			  = lAcc2002.pg_Accertamento
						and   nvl(im_associato_doc_amm,0) = 0
						for update nowait;

						 -- Impostiamo i dati della scadenza libera
						 if lScad.IM_SCADENZA >= lDelta then
						 	lScad.IM_SCADENZA := lScad.IM_SCADENZA - lDelta;
							lDelta := 0;
						 else
							 if lScad.IM_SCADENZA < lDelta then
							 	lScad.IM_SCADENZA :=0;
								lDelta := lDelta - lScad.IM_SCADENZA;
							 end if;
						 end if;
						 lScad.DUVA := aSysdate;
					     lScad.UTUV := cgUtente;
					     lScad.PG_VER_REC := lScad.PG_VER_REC + 1;

						 -- Recuperiamo l'unico dettaglio voce associato alla scadenza
						 select *
						 into lScadVoce
						 from accertamento_scad_voce
						 where cd_cds 		   = lScad.cd_cds
						 and   esercizio 	   = lScad.esercizio
	        				 and   esercizio_originale = lScad.esercizio_originale
						 and   pg_accertamento = lScad.pg_accertamento
						 and   pg_accertamento_scadenzario = lScad.pg_accertamento_scadenzario
						 for update nowait;

						 -- Impostiamo i dati del dettaglio voce
						 lScadVoce.IM_VOCE					   := lScad.IM_SCADENZA;
						 lScadVoce.DUVA						   := aSysdate;
						 lScadVoce.UTUV						   := cgUtente;
						 lScadVoce.PG_VER_REC				   := lScadVoce.PG_VER_REC + 1;

						 -- Aggiornamento Accertamento, scadenza, voce
						 update accertamento
						 set im_accertamento = aAccertamento.im_accertamento
							 ,utuv = cgUtente
							 ,duva = aSysdate
						 	 ,pg_ver_rec = PG_VER_REC + 1
						 where cd_cds = lAcc2002.cd_cds
						 and   esercizio = lAcc2002.esercizio
        					 and   esercizio_originale = lAcc2002.esercizio_originale
						 and   pg_accertamento = lAcc2002.pg_accertamento;

				 		 -- Aggiornamento scadenza
						 update accertamento_scadenzario
						 set  IM_SCADENZA = lScad.IM_SCADENZA
						 	 ,DUVA = lScad.DUVA
							 ,UTUV = lScad.UTUV
							 ,PG_VER_REC = lScad.PG_VER_REC
						 where cd_cds = lScad.cd_cds
						 and   esercizio = lScad.esercizio
        					 and   esercizio_originale = lScad.esercizio_originale
						 and   pg_accertamento = lScad.pg_accertamento
						 and   pg_accertamento_scadenzario = lScad.pg_Accertamento_scadenzario;

				 		 -- Aggiornamento scadenza voce
						 update accertamento_scad_voce
						 set  IM_VOCE = lScadVoce.IM_VOCE
						 	 ,DUVA = lScadVoce.DUVA
							 ,UTUV = lScadVoce.UTUV
							 ,PG_VER_REC = lScadVoce.PG_VER_REC
						 where cd_cds 	                   = lScadVoce.cd_cds
						 and   esercizio 				   = lScadVoce.esercizio
					         and   esercizio_originale = lScadVoce.esercizio_originale
						 and   pg_accertamento 			   = lScadVoce.pg_accertamento
						 and   pg_accertamento_scadenzario = lScadVoce.pg_Accertamento_scadenzario
						 and   cd_centro_responsabilita    = lScadVoce.cd_centro_responsabilita
						 and   cd_linea_attivita 		   = lScadVoce.cd_linea_attivita;

					 end if;  --lModificabile

				 end if;

				 if aAccertamento.CD_VOCE != lAcc2002.CD_VOCE then
				 	lTipoModifica := 4;
 				 	if not lGiaAssociato then
 					   update accertamento
					   set cd_voce = aAccertamento.CD_VOCE,
					   	   cd_elemento_voce = aAccertamento.CD_ELEMENTO_VOCE,
						   cd_cds_origine = aAccertamento.cd_cds_origine,
						   cd_uo_origine = aAccertamento.cd_uo_origine,
					   	   utuv = aAccertamento.utuv,
					   	   duva = aAccertamento.duva,
						   pg_ver_rec = pg_ver_rec +1
					   where cd_cds = lAcc2002.cd_cds
					   and   esercizio =  lAcc2002.esercizio
        				   and   esercizio_originale = lAcc2002.esercizio_originale
					   and   pg_accertamento = lAcc2002.pg_accertamento;
 					end if;
				 end if;

				 if aAccertamento.CD_UNITA_ORGANIZZATIVA != lAcc2002.CD_UNITA_ORGANIZZATIVA then
				 	lTipoModifica := 5;
 				 	if not lGiaAssociato then
 					   update accertamento
					   set cd_unita_organizzativa = lAcc2002.cd_unita_organizzativa,
					   	   utuv = lAcc2002.utuv,
					   	   duva = lAcc2002.duva,
						   pg_ver_rec = pg_ver_rec +1
					   where cd_cds = lAcc2002.cd_cds
					   and   esercizio =  lAcc2002.esercizio
        				   and   esercizio_originale = lAcc2002.esercizio_originale
					   and   pg_accertamento = lAcc2002.pg_accertamento;
 					end if;
				 end if;

				 -- Condizioni di arresto del riporto.
				 if lTipoModifica = 0 AND lGiaAssociato then
				 	gMsgLog := 'Accertamento non risulta in nessun modo cambiato dal precedente riporto esercizio = '|| substr(to_char(aAccertamento.pg_accertamento),1,4) || ' cd_residuo = '|| aAccertamento.pg_accertamento_origine;
		 		    ibmutl200.logInf(gPgLog ,gMsgLog, '', '');
				 end if;

				 if lTipoModifica in (1,5) AND lGiaAssociato then
				 	gMsgLog := 'Accertamento residuo gia associato a documenti amministrativi esercizio = ' || substr(to_char(aAccertamento.pg_accertamento),1,4) || ' cd_residuo = '|| aAccertamento.pg_accertamento_origine ;
		 		    ibmutl200.logInf(gPgLog ,gMsgLog, '', '');
				 end if;

				 if lTipoModifica = 4 AND lGiaAssociato  then
				 	-- aggiungerre la condizione che le scadenze non devono essere state modificate
				 	gMsgLog := 'Accertamento residuo gia associato a documenti amministrativi esercizio = '|| substr(to_char(aAccertamento.pg_accertamento),1,4) || ' cd_residuo = '|| aAccertamento.pg_accertamento_origine;
		 		    ibmutl200.logInf(gPgLog ,gMsgLog, '', '');
				 end if;

				 return TRUE;
			 else
			 	 -- In questo caso l'accertamento risulta essere mai stato inserito
				 -- precedentemente.
				 return FALSE;
			 end if;
		end;

	end ChkAccertamento2002;

----------------------------------------------------------
	procedure crea_residuo_attivo (aAcc2002 accertamento%rowtype
			  					  ,aEsercizio_destinazione esercizio.ESERCIZIO%type
								  ,aSysdate date) is

	lResiduo2003 accertamento%rowtype;
	lAccOld accertamento%rowtype;
	lDelta number (15,2):=0;
	begin
		 begin
		 	  select *
			  into lAccOld
		 	  from accertamento
			  where cd_cds 	   	    = aAcc2002.cd_cds
			  and   esercizio  		= aAcc2002.esercizio
			  and   esercizio_originale = aAcc2002.esercizio_originale
			  and   pg_Accertamento = aAcc2002.pg_Accertamento
			  for update nowait;
		 exception when no_data_found then
				   gMsgLog := 'Accertamento Origine non trovato, pg_accertamento = '||to_char(aAcc2002.pg_Accertamento);
		 		   ibmutl200.logInf(gPgLog ,gMsgLog, '', '');
		 end;
		 lResiduo2003.CD_TIPO_DOCUMENTO_CONT 	  := cnrctb018.TI_DOC_ACC_RES;
		 lResiduo2003.CD_CDS                      := aAcc2002.CD_CDS;
		 lResiduo2003.ESERCIZIO				   	  := aEsercizio_destinazione;
		 lResiduo2003.ESERCIZIO_ORIGINALE	  := aAcc2002.ESERCIZIO_ORIGINALE;
		 lResiduo2003.PG_ACCERTAMENTO 			  := aAcc2002.PG_ACCERTAMENTO;
		 lResiduo2003.CD_UNITA_ORGANIZZATIVA      := aAcc2002.CD_UNITA_ORGANIZZATIVA;
		 lResiduo2003.CD_CDS_ORIGINE			  := aAcc2002.CD_CDS_ORIGINE;
		 lResiduo2003.CD_UO_ORIGINE			   	  := aAcc2002.CD_UO_ORIGINE;
		 lResiduo2003.TI_APPARTENENZA             := aAcc2002.TI_APPARTENENZA;
		 lResiduo2003.TI_GESTIONE			   	  := aAcc2002.TI_GESTIONE;
		 lResiduo2003.CD_ELEMENTO_VOCE		   	  := aAcc2002.CD_ELEMENTO_VOCE;
		 lResiduo2003.CD_VOCE				   	  := aAcc2002.CD_VOCE;
		 lResiduo2003.DT_REGISTRAZIONE		   	  := aAcc2002.DT_REGISTRAZIONE;
		 lResiduo2003.DS_ACCERTAMENTO             := aAcc2002.DS_ACCERTAMENTO;
		 lResiduo2003.NOTE_ACCERTAMENTO           := aAcc2002.NOTE_ACCERTAMENTO;
		 lResiduo2003.CD_TERZO				   	  := aAcc2002.CD_TERZO;
		 lResiduo2003.IM_ACCERTAMENTO		   	  := aAcc2002.IM_ACCERTAMENTO;
		 lResiduo2003.DT_CANCELLAZIONE            := aAcc2002.DT_CANCELLAZIONE;
		 lResiduo2003.CD_RIFERIMENTO_CONTRATTO    := aAcc2002.CD_RIFERIMENTO_CONTRATTO;
		 lResiduo2003.DT_SCADENZA_CONTRATTO	   	  := aAcc2002.DT_SCADENZA_CONTRATTO;
		 lResiduo2003.CD_FONDO_RICERCA		   	  := aAcc2002.CD_FONDO_RICERCA;
		 lResiduo2003.FL_PGIRO				   	  := aAcc2002.FL_PGIRO;
		 lResiduo2003.RIPORTATO				   	  := 'N';
		 lResiduo2003.DACR					   	  := aAcc2002.DACR;
		 lResiduo2003.UTCR					   	  := aAcc2002.UTCR;
		 lResiduo2003.DUVA					   	  := aSysdate;
		 lResiduo2003.UTUV					   	  := cgutente;
		 lResiduo2003.PG_VER_REC				  := 1;
		 lResiduo2003.CD_CDS_ORI_RIPORTO          := aAcc2002.CD_CDS;
		 lResiduo2003.ESERCIZIO_ORI_RIPORTO	   	  := aAcc2002.ESERCIZIO;
		 lResiduo2003.ESERCIZIO_ORI_ORI_RIPORTO	   	  := aAcc2002.ESERCIZIO_ORIGINALE;
		 lResiduo2003.PG_ACCERTAMENTO_ORI_RIPORTO := aAcc2002.PG_ACCERTAMENTO;
		 lResiduo2003.ESERCIZIO_COMPETENZA	   	  := aEsercizio_destinazione;
		 lResiduo2003.PG_ACCERTAMENTO_ORIGINE	  := aAcc2002.PG_ACCERTAMENTO_ORIGINE;


		 ChkResiduoAtt2003 (lResiduo2003);

		 lAccOld.RIPORTATO 	  := 'Y';
		 lAccOld.DUVA		  := aSysdate;
		 lAccOld.UTUV		  := cgUtente;
		 lAccOld.PG_VER_REC	  := lAccOld.PG_VER_REC + 1;

		 update accertamento
		 set riportato   = lAccOld.RIPORTATO
		 	 ,duva 	     = lAccOld.DUVA
			 ,utuv	     = lAccOld.UTUV
			 ,pg_ver_rec = pg_ver_rec + 1
		 where cd_cds = lAccOld.cd_cds
		 and   esercizio = lAccOld.esercizio
		 and   esercizio_originale = lAccOld.esercizio_originale
		 and   pg_accertamento = lAccOld.pg_accertamento;

	end ;

	-- Controlla se il residuo risulta gia inserito
	procedure ChkResiduoAtt2003(aResiduo2003 accertamento%rowtype) is
	lResiduoOld  accertamento%rowtype;
	lScadVoce  		accertamento_scad_voce%rowtype;
	lGiaAssociato boolean;
	lGiaInserito boolean;
	lDelta number(15,2) :=0 ;
	lTrovataScadenza boolean;
	-- Tipo Cambio 1 : Creditore diverso         => lTipoModifica = 1
	-- Tipo Cambio 2 : Importo nuovo > vecchio   => lTipoModifica = 2
	-- Tipo Cambio 3 : Importo nuovo < vecchio   => lTipoModifica = 3
	-- Tipo Cambio 4 : Voce diversa 	 		 => lTipoModifica = 4
	-- Tipo Cambio 5 : UO diversa 		 		 => lTipoModifica = 5
	lTipoModifica number;
	lTotImpModificabile number(15,2):=0;

	begin
		begin
			 select *
			 into lResiduoOld
			 from accertamento acc
			 where acc.CD_CDS          = aResiduo2003.CD_CDS
			 and   acc.ESERCIZIO       = aResiduo2003.ESERCIZIO
			 and   acc.ESERCIZIO_ORIGINALE = aResiduo2003.ESERCIZIO_ORIGINALE
			 and   acc.PG_ACCERTAMENTO = aResiduo2003.PG_ACCERTAMENTO
			 for update nowait;

		 	 lGiaInserito := TRUE;
		exception when no_data_found then
			 	lGiaInserito := FALSE;
		end ;
		begin
			 if lGiaInserito then
				 -- Controlliamo se l'accertamento residuo principale risulta gia
				 -- associato a documenti amministrativi
				 lGiaAssociato := ResiduoAttAssociato(lResiduoOld);

				 ----------------------------------------------------------------------
				 -- Identificare il tipo di eventuale cambiamento del residuo        --
				 ----------------------------------------------------------------------
				 -- Tipo Cambio 1 : Creditore diverso se e solo se Residuo
				 --		 		  	principale non associato a documenti amministrativi
				 -- Tipo Cambio 2 : Importo diverso nuovo > vecchio
				 -- Tipo Cambio 3 : Importo diverso nuovo < vecchio
				 -- Tipo Cambio 4 : cd_voce diversa gestito se e solo se Residuo
				 --		 		  	principale non associato a documenti amministrativi
				 -- Tipo Cambio 5 : UO diversa se e solo se Residuo
				 --		 		  	principale non associato a documenti amministrativi
				 if aResiduo2003.CD_TERZO != lResiduoOld.CD_TERZO then
				 	lTipoModifica := 1;
 				 	if not lGiaAssociato then
 					   update accertamento
					   set cd_terzo = aResiduo2003.cd_terzo,
					   	   utuv = aResiduo2003.utuv,
					   	   duva = aResiduo2003.duva,
						   pg_ver_rec = pg_ver_rec +1
					   where cd_cds = aResiduo2003.cd_cds
					   and   esercizio =  aResiduo2003.esercizio
					   and   esercizio_originale =  aResiduo2003.esercizio_originale
					   and   pg_accertamento = aResiduo2003.pg_accertamento;
 					end if;
				 end if;

				 if aResiduo2003.CD_VOCE != lResiduoOld.CD_VOCE then
				 	lTipoModifica := 4;
 				 	if not lGiaAssociato then
 					   update accertamento
					   set CD_VOCE = aResiduo2003.CD_VOCE,
					   	   cd_elemento_voce = aResiduo2003.CD_ELEMENTO_VOCE,
						   cd_cds_origine = aResiduo2003.cd_cds_origine,
						   cd_uo_origine = aResiduo2003.cd_uo_origine,
						   utuv = aResiduo2003.utuv,
					   	   duva = aResiduo2003.duva,
						   pg_ver_rec = pg_ver_rec +1
					   where cd_cds = aResiduo2003.cd_cds
					   and   esercizio =  aResiduo2003.esercizio
					   and   esercizio_originale =  aResiduo2003.esercizio_originale
					   and   pg_accertamento = aResiduo2003.pg_accertamento;

					   -- Decrementiamo il saldo sul vecchio cd_voce del valore
					   -- pari all'importo del vecchio accertamento
					   update voce_f_saldi_cmp
					   set  im_obblig_imp_acr = im_obblig_imp_acr - lResiduoOld.IM_ACCERTAMENTO,
					        IM_STANZ_INIZIALE_A1 = im_obblig_imp_acr - lResiduoOld.IM_ACCERTAMENTO,
						    utuv=aResiduo2003.utuv,
						    duva=aResiduo2003.duva,
						    pg_ver_rec=pg_ver_rec + 1
					   where esercizio         = lResiduoOld.esercizio
				       and cd_cds 			   = lResiduoOld.cd_cds
					   and ti_appartenenza 	   = lResiduoOld.ti_appartenenza
					   and ti_gestione 		   = lResiduoOld.ti_gestione
					   and cd_voce 			   = lResiduoOld.cd_voce
					   and ti_competenza_residuo = CNRCTB054.TI_RESIDUI;

					   -- Aumentiamo il saldo sul nuovo cd_voce del valore
					   -- pari all'importo del vecchio accertamento
					   -- questo perch? gli aumenti e diminuzioni di valori
					   -- sono trattate di seguito
					   update voce_f_saldi_cmp
					   set  im_obblig_imp_acr = im_obblig_imp_acr + lResiduoOld.IM_ACCERTAMENTO,
					        IM_STANZ_INIZIALE_A1 = im_obblig_imp_acr + lResiduoOld.IM_ACCERTAMENTO,
						    utuv=aResiduo2003.utuv,
						    duva=aResiduo2003.duva,
						    pg_ver_rec=pg_ver_rec + 1
					   where esercizio         = aResiduo2003.esercizio
				       and cd_cds 			   = aResiduo2003.cd_cds
					   and ti_appartenenza 	   = aResiduo2003.ti_appartenenza
					   and ti_gestione 		   = aResiduo2003.ti_gestione
					   and cd_voce 			   = aResiduo2003.cd_voce
					   and ti_competenza_residuo = CNRCTB054.TI_RESIDUI;

 					end if;
				 end if;

				 if aResiduo2003.CD_UNITA_ORGANIZZATIVA != lResiduoOld.CD_UNITA_ORGANIZZATIVA then
				 	lTipoModifica := 5;
 				 	if not lGiaAssociato then
 					   update accertamento
					   set CD_UNITA_ORGANIZZATIVA = aResiduo2003.CD_UNITA_ORGANIZZATIVA,
					   	   utuv = aResiduo2003.utuv,
					   	   duva = aResiduo2003.duva,
						   pg_ver_rec = pg_ver_rec +1
					   where cd_cds = aResiduo2003.cd_cds
					   and   esercizio =  aResiduo2003.esercizio
					   and   esercizio_originale =  aResiduo2003.esercizio_originale
					   and   pg_accertamento = aResiduo2003.pg_accertamento;
 					end if;
				 end if;

				 if aResiduo2003.IM_ACCERTAMENTO > lResiduoOld.IM_ACCERTAMENTO then
				 	lTipoModifica := 2;
				 end if;
				 if aResiduo2003.IM_ACCERTAMENTO < lResiduoOld.IM_ACCERTAMENTO then
				 	lTipoModifica := 3;
				 end if;
				 -- Condizioni di arresto del riporto.
				 if lTipoModifica =0  then
				 	gMsgLog := 'Accertamento residuo replicato pg_accertamento =' || to_char(aResiduo2003.pg_accertamento);
		 		    ibmutl200.logInf(gPgLog ,gMsgLog, '', '');
			     end if;

				 if lTipoModifica in (1,4,5) AND lGiaAssociato then
				 	gMsgLog := 'Accertamento residuo gia associato a documenti amministrativi pg_accertamento = ' || to_char(aResiduo2003.pg_accertamento);
		 		    ibmutl200.logInf(gPgLog ,gMsgLog, '', '');
				 else
					 if lTipoModifica = 2 then
					 	 -- Siamo nel caso in cui stiamo inserendo un accertamento residuo
						 -- che risulta gia inserito nella tabella ACCERTAMENTI. Quindi
						 -- siccome risulta cambiato l'importo dell'accertamento occorre
						 -- seguire il seguente algoritmo :
						 -- Importo del accertamento NUOVO (che vogliamo inserire) risulta
						 --    MAGGIORE dell'importo dell'accertamento Principale
						 --	   (gia presente nella tabella esercizio) in questo caso  =>
						 --    viene modificato l'importo relativo alla prima scadenza
						 --    non associata a documenti amministrativi, se non esiste
						 --	   tale scadenza allora se ne crea una nuova con importo pari
						 --	   al delta IMPORTO ACC NUOVO - IMPORTO ACC PRINCIPALE

-----------------------------------------------------------------------
						 ModPlusAcc2003(aResiduo2003, lResiduoOld);
-----------------------------------------------------------------------
					 end if; -- Modifica 2

					 if lTipoModifica = 3 then
				 	 	gMsgLog := 'modifcare';
					 	 -- Siamo nel caso in cui stiamo inserendo un accertamento residuo
						 -- che risulta gia inserito nella tabella ACCERTAMENTI. Quindi
						 -- siccome risulta cambiato l'importo dell'accertamento occorre
						 -- seguire il seguente algoritmo :
						 -- Importo del accertamento NUOVO (che vogliamo inserire) risulta
						 --    minore dell'importo dell'accertamento Principale
						 --	   (gia presente nella tabella esercizio) in questo caso  =>
						 --    viene modificato l'importo relativo alla prima scadenza
						 --    non associata a documenti amministrativi, se cio non basta
						 --    allora si passa alla seconda scadenza libera e cosi via. Se
						 --    non ci sono sufficienti scadenze libere per soddisfare la
						 --    diminuzione dell'importo dell'accertamento allora si blocca
						 --    il riporto.

						 lDelta := lResiduoOld.im_accertamento - aResiduo2003.im_accertamento;

						 for lScadLibera in (select *
						 	 			 	 from accertamento_scadenzario
											 where cd_cds               = aResiduo2003.cd_cds
											 and   esercizio 			= aResiduo2003.esercizio
                                	                				 And   esercizio_originale =  aResiduo2003.esercizio_originale
											 and   pg_accertamento      = aResiduo2003.pg_accertamento
											 and   im_associato_doc_amm = 0
											 for update nowait) loop
							 begin
							 	 -- Controlliamo se la scadenza puo essere modificata
								 select *
								 into lScadVoce
								 from accertamento_scad_voce
								 where cd_cds                      = lScadLibera.cd_cds
								 and   esercizio 				   = lScadLibera.esercizio
                        					 And   esercizio_originale =  lScadLibera.esercizio_originale
								 and   pg_accertamento 			   = lScadLibera.pg_accertamento
								 and   pg_accertamento_scadenzario = lScadLibera.pg_accertamento_scadenzario
								 and   im_voce 					   = lScadLibera.im_scadenza;

								 lTotImpModificabile := lTotImpModificabile + lScadLibera.im_scadenza;

							 exception when no_data_found then
							 		   null;
							 end;
						 end loop;

						 if lTotImpModificabile >= lDelta then
-------------------------------------------------------------------
						    ModMinusAcc2003(aResiduo2003, lResiduoOld);
-------------------------------------------------------------------
						 else
						 	gMsgLog := 'Residuo ' || to_char(aResiduo2003.pg_accertamento_origine) || ' non modificato';
				 		    ibmutl200.logInf(gPgLog ,gMsgLog, '', '');
						 end if;

					 end if; -- Modifica 3

				 end if; -- lTipoModifica in (1,4,5)

			 else -- lGiaInserito
			 	 -- In questo caso l'accertamento risulta essere mai stato inserito
				 -- precedentemente.
				 CreaResiduoAtt2003(aResiduo2003);
				 -- Aggiornare voce_f_saldi_cmp
				 update voce_f_saldi_cmp
				 set  im_obblig_imp_acr = im_obblig_imp_acr + aResiduo2003.IM_ACCERTAMENTO,
				      IM_STANZ_INIZIALE_A1 = im_obblig_imp_acr + aResiduo2003.IM_ACCERTAMENTO,
					  utuv=aResiduo2003.utuv,
					  duva=aResiduo2003.duva,
					  pg_ver_rec=pg_ver_rec + 1
				 where esercizio           = aResiduo2003.esercizio
			     and cd_cds 			   = aResiduo2003.cd_cds
				 and ti_appartenenza 	   = aResiduo2003.ti_appartenenza
				 and ti_gestione 		   = aResiduo2003.ti_gestione
				 and cd_voce 			   = aResiduo2003.cd_voce
				 and ti_competenza_residuo = CNRCTB054.TI_RESIDUI;
			 end if;
		end;

	end ChkResiduoAtt2003;

	procedure CreaResiduoAtt2003 (aResiduo2003 accertamento%rowtype) IS
	lScadLibera accertamento_scadenzario%rowtype;
	lScadVoce   accertamento_scad_voce%rowtype;
	begin
		 -- Costruiamo uan scadenza libera per il residuo attivo
		 lScadLibera := CreaScad2003(aResiduo2003);

		 -- Costruiamo il dettaglio voce associato alla scadenza
		 lScadVoce := CreaScadVoce2003(lScadLibera);

		 -- Aggiornamento Accertamento, scadenza
		 cnrctb035.INS_ACCERTAMENTO(aResiduo2003);
		 cnrctb035.INS_ACCERTAMENTO_SCADENZARIO(lScadLibera);
		 cnrctb035.INS_ACCERTAMENTO_SCAD_VOCE(lScadVoce);

	end;

	function CreaScad2003(aResiduo2003 accertamento%rowtype) return accertamento_Scadenzario%rowtype is
	lScadenzaLibera accertamento_Scadenzario%rowtype;
	lPgScadenza number;
	begin
		-- creazione di una scadenza nuova
		select nvl(max(pg_accertamento_scadenzario),0) +1
		into lPgScadenza
		from accertamento_scadenzario
		where cd_cds    = aResiduo2003.cd_cds
		and   esercizio = aResiduo2003.esercizio
		and   esercizio_originale = aResiduo2003.esercizio_originale
		and   pg_Accertamento = aResiduo2003.pg_Accertamento;

	    lScadenzaLibera.CD_CDS					   	  := aResiduo2003.cd_Cds;
	    lScadenzaLibera.ESERCIZIO				   	  := aResiduo2003.esercizio;
	    lScadenzaLibera.ESERCIZIO_ORIGINALE				   	  := aResiduo2003.esercizio_originale;
	    lScadenzaLibera.PG_ACCERTAMENTO			   	  := aResiduo2003.pg_Accertamento;
	    lScadenzaLibera.PG_ACCERTAMENTO_SCADENZARIO   := lPgScadenza;
	    lScadenzaLibera.DT_SCADENZA_EMISSIONE_FATTURA := trunc(to_date('3112'||gcEsercizioDest,'ddmmyyyy')) ; -- chiedere
	    lScadenzaLibera.DT_SCADENZA_INCASSO			  := trunc(to_date('3112'||gcEsercizioDest,'ddmmyyyy')); -- chiedere
	    lScadenzaLibera.DS_SCADENZA					  := 'Scadenza per aumento di valore';
	    lScadenzaLibera.IM_SCADENZA					  := aResiduo2003.IM_ACCERTAMENTO;
	    lScadenzaLibera.IM_ASSOCIATO_DOC_AMM		  := 0;
	    lScadenzaLibera.IM_ASSOCIATO_DOC_CONTABILE    := 0;
	    lScadenzaLibera.DACR 					   	  := aResiduo2003.dacr;
	    lScadenzaLibera.UTCR					   	  := aResiduo2003.utcr;
	   	lScadenzaLibera.DUVA 					   	  := aResiduo2003.dacr;
	    lScadenzaLibera.UTUV 					   	  := aResiduo2003.utcr;
	    lScadenzaLibera.PG_VER_REC				   	  := 1;

		return lScadenzaLibera;
	end;

	function CreaScadVoce2003 (aScad accertamento_scadenzario%rowtype) return accertamento_scad_voce%rowtype is
	lScadenzaVoce accertamento_scad_voce%rowtype;
  	aCdCdr varchar2(30);
  	aCdLa varchar2(10);
	lNumeScdVoce number :=0;
	begin
	    -- configurare la linea attivita in configurazione CNR
		aCdCdr  :=CNRCTB015.GETVAL01PERCHIAVE(CNRCTB035.LA_SPECIALE,CNRCTB035.LA_ENTRATA_ENTE);
		aCdLa   :=CNRCTB015.GETVAL02PERCHIAVE(CNRCTB035.LA_SPECIALE,CNRCTB035.LA_ENTRATA_ENTE);
	    lScadenzaVoce.CD_CDS                       := aScad.cd_cds;
	    lScadenzaVoce.ESERCIZIO					   := aScad.esercizio;
	    lScadenzaVoce.ESERCIZIO_ORIGINALE				   := aScad.esercizio_originale;
	    lScadenzaVoce.PG_ACCERTAMENTO			   := aScad.pg_accertamento;
	    lScadenzaVoce.PG_ACCERTAMENTO_SCADENZARIO  := aScad.pg_Accertamento_Scadenzario;
		lScadenzaVoce.CD_CENTRO_RESPONSABILITA	   := aCdCdr; -- chiedere
		lScadenzaVoce.CD_LINEA_ATTIVITA			   := aCdLa; -- chiedere
		lScadenzaVoce.IM_VOCE					   := aScad.IM_SCADENZA;
		lScadenzaVoce.CD_FONDO_RICERCA			   := NULL;
		lScadenzaVoce.DACR						   := aScad.dacr;
		lScadenzaVoce.UTCR						   := aScad.utcr;
		lScadenzaVoce.DUVA						   := aScad.dacr;
		lScadenzaVoce.UTUV						   := aScad.utcr;
		lScadenzaVoce.PG_VER_REC				   := 1;
		return lScadenzaVoce;
	end;

	function ResiduoAttAssociato(aAcc accertamento%rowtype) return boolean is
	lPgAcc accertamento.PG_ACCERTAMENTO%type;
	lEsOriAcc accertamento.ESERCIZIO_ORIGINALE%type;
	lSommaImDocAmm number(15,2);
	lSommaImDocCont number(15,2);
	lNumScad number;
	begin
		 -- Controlliamo se il residuo/Accertamento Principale risulta associato a documenti
		 -- amministrativi
		 select count(*), esercizio_originale, pg_accertamento, sum(nvl(scad.IM_ASSOCIATO_DOC_AMM,0)), sum(nvl(scad.IM_ASSOCIATO_DOC_CONTABILE,0))
		 into lNumScad,lEsOriAcc,lPgAcc,lSommaImDocAmm,lSommaImDocCont
		 from accertamento_scadenzario scad
		 where scad.CD_CDS          = aAcc.CD_CDS
		 and   scad.ESERCIZIO       = aAcc.ESERCIZIO
		 and   scad.ESERCIZIO_ORIGINALE  = aAcc.ESERCIZIO_ORIGINALE
		 and   scad.PG_ACCERTAMENTO = aAcc.PG_ACCERTAMENTO
		 group by scad.CD_CDS
		 	   	  ,scad.ESERCIZIO
		 	   	  ,scad.ESERCIZIO_ORIGINALE
		 		  ,scad.PG_ACCERTAMENTO;

		 if lNumScad = 0 then
		 	return FALSE;
		 else
		 	if (lSommaImDocAmm >0 or lSommaImDocCont>0) then
		 	   return true;
			end if;
		 end if;

		 return FALSE;
	end ResiduoAttAssociato;

	procedure ModPlusAcc2003(aResiduo2003New accertamento%rowtype, lResiduo2003Old accertamento%rowtype) as
	lScadVoce accertamento_scad_voce%rowtype;
	lDelta number (15,2);
	lNumScadMod number;
	lScadModificabile accertamento_scadenzario%rowtype;
	lScadVoceModificabile accertamento_scad_voce%rowtype;
	lTotaleImDisp number(15,2) := 0;
	begin

		 lock table accertamento_scadenzario in exclusive mode;
		 lock table accertamento_scad_voce in exclusive mode;
		 lock table voce_f_saldi_cmp in exclusive mode;

		 lDelta := aResiduo2003New.im_accertamento - lResiduo2003Old.im_accertamento;

		 lNumScadMod := 0;

		 for lScadLibere in (select *
		 	 			 	 from accertamento_scadenzario
							 where cd_cds               = lResiduo2003Old.cd_cds
							 and   esercizio 	    = lResiduo2003Old.esercizio
							 and   esercizio_originale  = lResiduo2003Old.esercizio_originale
							 and   pg_accertamento      = lResiduo2003Old.pg_accertamento
							 and   im_associato_doc_amm = 0
							 for update nowait) loop
			 begin
			 	 -- Controlliamo se la scadenza puo essere modificata
				 select *
				 into lScadVoce
				 from accertamento_scad_voce
				 where cd_cds = lScadLibere.cd_cds
				 and   esercizio = lScadLibere.esercizio
				 and   esercizio_originale  = lScadLibere.esercizio_originale
				 and   pg_accertamento = lScadLibere.pg_accertamento
				 and   pg_accertamento_scadenzario = lScadLibere.pg_accertamento_scadenzario
				 and   im_voce = lScadLibere.im_scadenza;

				 lNumScadMod := lNumScadMod + 1;
				 if lNumScadMod = 1 then
				 	lScadModificabile := lScadLibere;
					lScadVoceModificabile := lScadVoce;
				 end if;

			 exception when no_data_found then
			 		   null;
			 end;
		 end loop;


		 if lNumScadMod > 0 then
		 	 -- Si puo modificare
			 -- Aggiornamento Accertamento, scadenza, voce
			 update accertamento
			 set im_accertamento = aResiduo2003New.im_accertamento
				 ,utuv = aResiduo2003New.utuv
				 ,duva = aResiduo2003New.duva
			 	 ,pg_ver_rec = PG_VER_REC + 1
			 where cd_cds          = lResiduo2003Old.cd_cds
			 and   esercizio       = lResiduo2003Old.esercizio
	  	         and   esercizio_originale  = lResiduo2003Old.esercizio_originale
			 and   pg_accertamento = lResiduo2003Old.pg_accertamento;

		 	 update accertamento_scadenzario
			 set    im_scadenza = im_scadenza + lDelta,
				    duva	    = aResiduo2003New.duva,
				    utuv 	    = aResiduo2003New.utuv,
				    pg_ver_rec  = pg_ver_rec + 1
			 where cd_cds               = lScadModificabile.cd_cds
			 and   esercizio 	    = lScadModificabile.esercizio
	  	         and   esercizio_originale  = lScadModificabile.esercizio_originale
			 and   pg_accertamento      = lScadModificabile.pg_accertamento
			 and   pg_accertamento_scadenzario = lScadModificabile.pg_accertamento_scadenzario;

		 	 update accertamento_scad_voce
			 set   im_voce = im_voce + lDelta,
				   duva		   = aResiduo2003New.duva,
				   utuv 	   = aResiduo2003New.utuv,
				   pg_ver_rec  = pg_ver_rec + 1
			 where cd_cds               = lScadVoceModificabile.cd_cds
			 and   esercizio 			= lScadVoceModificabile.esercizio
	  	         and   esercizio_originale  = lScadVoceModificabile.esercizio_originale
			 and   pg_accertamento      = lScadVoceModificabile.pg_accertamento
			 and   pg_accertamento_scadenzario = lScadVoceModificabile.pg_accertamento_scadenzario
			 and   cd_centro_responsabilita    = lScadVoceModificabile.cd_centro_responsabilita
			 and   cd_linea_attivita		   = lScadVoceModificabile.cd_linea_attivita;

			 update voce_f_saldi_cmp
			 set im_obblig_imp_acr = im_obblig_imp_acr + lDelta,
			     IM_STANZ_INIZIALE_A1 = im_obblig_imp_acr + lDelta,
			  	 utuv = aResiduo2003New.utuv,
				 duva = aResiduo2003New.duva,
				 pg_ver_rec = pg_ver_rec + 1
			 where esercizio             = aResiduo2003New.esercizio
			 and   cd_cds 		     = aResiduo2003New.cd_cds
			 and   ti_appartenenza 	     = aResiduo2003New.ti_appartenenza
			 and   ti_gestione 	     = aResiduo2003New.ti_gestione
			 and   cd_voce 		     = aResiduo2003New.cd_voce
			 and   ti_competenza_residuo = CNRCTB054.TI_RESIDUI;

		 else
		 	CreaNuovaScad2003(aResiduo2003New, lResiduo2003Old);

			 update accertamento
			 set im_accertamento = aResiduo2003New.im_accertamento
				 ,utuv = aResiduo2003New.utuv
				 ,duva = aResiduo2003New.duva
			 	 ,pg_ver_rec = PG_VER_REC + 1
			 where cd_cds          = lResiduo2003Old.cd_cds
			 and   esercizio       = lResiduo2003Old.esercizio
	  	         and   esercizio_originale  = lResiduo2003Old.esercizio_originale
			 and   pg_accertamento = lResiduo2003Old.pg_accertamento;

			 update voce_f_saldi_cmp
			 set im_obblig_imp_acr = im_obblig_imp_acr + lDelta,
			     IM_STANZ_INIZIALE_A1 = im_obblig_imp_acr + lDelta,
			  	 utuv = aResiduo2003New.utuv,
				 duva = aResiduo2003New.duva,
				 pg_ver_rec = pg_ver_rec + 1
			 where esercizio             = aResiduo2003New.esercizio
			 and   cd_cds 				 = aResiduo2003New.cd_cds
			 and   ti_appartenenza 		 = aResiduo2003New.ti_appartenenza
			 and   ti_gestione 			 = aResiduo2003New.ti_gestione
			 and   cd_voce 				 = aResiduo2003New.cd_voce
			 and   ti_competenza_residuo = CNRCTB054.TI_RESIDUI;
		 end if;

	end;


	procedure CreaNuovaScad2003(aResiduo2003New accertamento%rowtype, aResiduo2003Old accertamento%rowtype) as
	lScadLibera accertamento_scadenzario%rowtype;
	lPgScadenza number;
	lDelta number(15,2);
	lNumVoce number := 0;
	lTrovataVoce boolean;
	begin
		lTrovataVoce := false;
		lDelta := aResiduo2003New.im_accertamento - aResiduo2003Old.im_accertamento;


		select max(pg_accertamento_scadenzario) + 1
		into lPgScadenza
		from accertamento_scadenzario
		where cd_cds               = aResiduo2003Old.cd_cds
		and   esercizio 	   = aResiduo2003Old.esercizio
		and   esercizio_originale  = aResiduo2003Old.esercizio_originale
		and   pg_accertamento      = aResiduo2003Old.pg_accertamento;

		lScadLibera.CD_CDS					   	  := aResiduo2003New.cd_Cds;
	    lScadLibera.ESERCIZIO				   	  := aResiduo2003New.esercizio;
	    lScadLibera.ESERCIZIO_ORIGINALE		   	  := aResiduo2003New.esercizio_originale;
	    lScadLibera.PG_ACCERTAMENTO			   	  := aResiduo2003New.pg_Accertamento;
	    lScadLibera.PG_ACCERTAMENTO_SCADENZARIO   := lPgScadenza;
	    lScadLibera.DT_SCADENZA_EMISSIONE_FATTURA := trunc(to_date('3112'||gcEsercizioDest,'ddmmyyyy')) ; -- chiedere
	    lScadLibera.DT_SCADENZA_INCASSO			  := trunc(to_date('3112'||gcEsercizioDest,'ddmmyyyy')); -- chiedere
	    lScadLibera.DS_SCADENZA					  := 'Scadenza per aumento di valore';
	    lScadLibera.IM_SCADENZA					  := lDelta;
	    lScadLibera.IM_ASSOCIATO_DOC_AMM		  := 0;
	    lScadLibera.IM_ASSOCIATO_DOC_CONTABILE    := 0;
	    lScadLibera.DACR 					   	  := aResiduo2003New.dacr;
	    lScadLibera.UTCR					   	  := aResiduo2003New.utcr;
	   	lScadLibera.DUVA 					   	  := aResiduo2003New.dacr;
	    lScadLibera.UTUV 					   	  := aResiduo2003New.utcr;
	    lScadLibera.PG_VER_REC				   	  := 1;

		cnrctb035.INS_ACCERTAMENTO_SCADENZARIO(lScadLibera);


		for lScadVoce in (select *
					  	  from accertamento_scad_voce
						  where cd_cds = lScadLibera.cd_cds
						  and   esercizio = lScadLibera.esercizio
						  and   esercizio_originale = lScadLibera.esercizio_originale
						  and   pg_accertamento = lScadLibera.pg_accertamento
						  and   pg_accertamento_scadenzario = lScadLibera.pg_accertamento_scadenzario - 1) loop

			lNumVoce := lNumVoce + 1;

			lScadVoce.CD_CDS                       := lScadVoce.cd_cds;
	    	lScadVoce.ESERCIZIO					   := lScadVoce.esercizio;
	    	lScadVoce.ESERCIZIO_ORIGINALE		   := lScadVoce.esercizio_originale;
	    	lScadVoce.PG_ACCERTAMENTO			   := lScadVoce.pg_accertamento;
	    	lScadVoce.PG_ACCERTAMENTO_SCADENZARIO  := lPgScadenza;
			-- Non cambiano
			lScadVoce.CD_CENTRO_RESPONSABILITA	   := lScadVoce.CD_CENTRO_RESPONSABILITA;
			lScadVoce.CD_LINEA_ATTIVITA			   := lScadVoce.CD_LINEA_ATTIVITA;
			-- Non cambiano
			if (not lTrovataVoce) and
			   substr(lScadVoce.CD_CENTRO_RESPONSABILITA,1,length(aResiduo2003New.CD_UO_ORIGINE)) = aResiduo2003New.CD_UO_ORIGINE then
			   lScadVoce.IM_VOCE					   := lScadLibera.IM_SCADENZA;
			   lTrovataVoce := true;
			else
			   lScadVoce.IM_VOCE					   := 0;
			end if;
			lScadVoce.CD_FONDO_RICERCA			   := NULL;
			lScadVoce.DACR						   := lScadLibera.dacr;
			lScadVoce.UTCR						   := lScadLibera.utcr;
			lScadVoce.DUVA						   := lScadLibera.duva;
			lScadVoce.UTUV						   := lScadLibera.utuv;
			lScadVoce.PG_VER_REC				   := 1;

		    cnrctb035.INS_ACCERTAMENTO_SCAD_VOCE(lScadVoce);
		end loop;

	end;


	procedure ModMinusAcc2003(aResiduo2003New accertamento%rowtype, aResiduo2003Old accertamento%rowtype) as
	lDelta number(15,2);
	lTotImpModificabile number (15,2) := 0;
	lScadVoce accertamento_scad_voce%rowtype;
	begin
		 -- Questa procedura viene chiamata se e solo se e possibile effettuare
		 -- la diminuzione di importo dell'accertamwento
		 lDelta := aResiduo2003Old.IM_ACCERTAMENTO - aResiduo2003New.IM_ACCERTAMENTO;

	 	update accertamento
		set im_accertamento = aResiduo2003New.IM_ACCERTAMENTO
			,utuv = aResiduo2003New.utuv
			,duva = aResiduo2003New.duva
			,pg_ver_rec = pg_ver_rec + 1
		 where cd_cds 	       = aResiduo2003Old.cd_cds
		 and   esercizio 	   = aResiduo2003Old.esercizio
		 and   esercizio_originale = aResiduo2003Old.esercizio_originale
		 and   pg_Accertamento = aResiduo2003Old.pg_accertamento;

		 update voce_f_saldi_cmp
		 set im_obblig_imp_acr = im_obblig_imp_acr + (aResiduo2003New.im_accertamento - aResiduo2003Old.im_accertamento),
		     IM_STANZ_INIZIALE_A1 = im_obblig_imp_acr + (aResiduo2003New.im_accertamento - aResiduo2003Old.im_accertamento),
		  	 utuv = aResiduo2003New.utuv,
			 duva = aResiduo2003New.duva,
			 pg_ver_rec = pg_ver_rec + 1
		 where esercizio             = aResiduo2003New.esercizio
		 and   cd_cds 				 = aResiduo2003New.cd_cds
		 and   ti_appartenenza 		 = aResiduo2003New.ti_appartenenza
		 and   ti_gestione 			 = aResiduo2003New.ti_gestione
		 and   cd_voce 				 = aResiduo2003New.cd_voce
		 and   ti_competenza_residuo = CNRCTB054.TI_RESIDUI;

		 for lScadLibera in (select *
		 	 			 	 from accertamento_scadenzario
							 where cd_cds               = aResiduo2003New.cd_cds
							 and   esercizio 	    = aResiduo2003New.esercizio
                                        		 and   esercizio_originale  = aResiduo2003New.esercizio_originale
							 and   pg_accertamento      = aResiduo2003New.pg_accertamento
							 and   im_associato_doc_amm = 0
							 for update nowait) loop
			 begin
			 	 -- Controlliamo se la scadenza puo essere modificata
				 select *
				 into lScadVoce
				 from accertamento_scad_voce
				 where cd_cds                      = lScadLibera.cd_cds
				 and   esercizio 				   = lScadLibera.esercizio
 		                 and   esercizio_originale = lScadLibera.esercizio_originale
				 and   pg_accertamento 			   = lScadLibera.pg_accertamento
				 and   pg_accertamento_scadenzario = lScadLibera.pg_accertamento_scadenzario
				 and   im_voce 					   = lScadLibera.im_scadenza;

				 if (lDelta > 0 ) then
					 if (lScadLibera.im_scadenza >= lDelta)  then
					 	update accertamento_scadenzario
						set im_scadenza = im_scadenza - lDelta
							,utuv = aResiduo2003New.utuv
							,duva = aResiduo2003New.duva
							,pg_ver_rec = pg_ver_rec + 1
						 where cd_cds = lScadLibera.cd_cds
						 and   esercizio = lScadLibera.esercizio
                 		                 and   esercizio_originale = lScadLibera.esercizio_originale
						 and   pg_Accertamento = lScadLibera.pg_accertamento
						 and   pg_accertamento_scadenzario = lScadLibera.pg_accertamento_scadenzario;

						 lScadLibera.im_scadenza := lScadLibera.im_scadenza - lDelta;

					 	update accertamento_scad_voce
						set im_voce = im_voce - lDelta
							,utuv = aResiduo2003New.utuv
							,duva = aResiduo2003New.duva
							,pg_ver_rec = pg_ver_rec + 1
						 where cd_cds                      = lScadVoce.cd_cds
						 and   esercizio 				   = lScadVoce.esercizio
                 		                 and   esercizio_originale = lScadVoce.esercizio_originale
						 and   pg_Accertamento 			   = lScadVoce.pg_accertamento
						 and   pg_accertamento_scadenzario = lScadVoce.pg_accertamento_scadenzario
						 and   cd_centro_responsabilita    = lScadVoce.cd_centro_responsabilita
						 and   cd_linea_Attivita 		   = lScadVoce.cd_linea_Attivita;

						 lDelta := 0;
					 else

						lDelta := lDelta - lScadLibera.im_scadenza;

					 	update accertamento_scadenzario
						set im_scadenza = 0
							,utuv = aResiduo2003New.utuv
							,duva = aResiduo2003New.duva
							,pg_ver_rec = pg_ver_rec + 1
						 where cd_cds = lScadLibera.cd_cds
						 and   esercizio = lScadLibera.esercizio
                 		                 and   esercizio_originale = lScadLibera.esercizio_originale
						 and   pg_Accertamento = lScadLibera.pg_accertamento
						 and   pg_accertamento_scadenzario = lScadLibera.pg_accertamento_scadenzario;

						 lScadLibera.im_scadenza := 0;

					 	update accertamento_scad_voce
						set im_voce = 0
							,utuv = aResiduo2003New.utuv
							,duva = aResiduo2003New.duva
							,pg_ver_rec = pg_ver_rec + 1
						 where cd_cds                      = lScadVoce.cd_cds
						 and   esercizio 				   = lScadVoce.esercizio
                 		                 and   esercizio_originale = lScadVoce.esercizio_originale
						 and   pg_Accertamento 			   = lScadVoce.pg_accertamento
						 and   pg_accertamento_scadenzario = lScadVoce.pg_accertamento_scadenzario
						 and   cd_centro_responsabilita    = lScadVoce.cd_centro_responsabilita
						 and   cd_linea_Attivita 		   = lScadVoce.cd_linea_Attivita;


					 end if;


					 if (lScadLibera.im_scadenza = 0)  then
						 -- Cancelliamo le scadenze e le relativer voci con importo a zero
						 delete accertamento_scad_voce
						 where cd_cds                      = lScadVoce.cd_cds
						 and   esercizio 				   = lScadVoce.esercizio
                 		                 and   esercizio_originale = lScadVoce.esercizio_originale
						 and   pg_accertamento 			   = lScadVoce.pg_accertamento
						 and   pg_accertamento_scadenzario = lScadVoce.pg_Accertamento_scadenzario
						 and   cd_linea_attivita 		   = lScadVoce.cd_linea_attivita
						 and   im_voce 					   = 0;

						 delete accertamento_scadenzario
						 where cd_cds                      = lScadLibera.cd_cds
						 and   esercizio 	   			   = lScadLibera.esercizio
                		                 and   esercizio_originale = lScadLibera.esercizio_originale
						 and   pg_accertamento        	   = lScadLibera.pg_accertamento
						 and   pg_accertamento_scadenzario = lScadLibera.pg_Accertamento_scadenzario
						 and   im_scadenza 				   = 0;
					 end if;


				 end if; --(lDelta > 0 )

			 exception when no_data_found then
			 		   null;
			 end;
		 end loop;

	end;

	procedure migrazione_residui_passivi  is

--	lResiduoPasSci cnr_residui_passivi%rowtype;
	lNumResiduiPasSci number;

	lContatore number;
	lPgObbIniziale number;
	lPgObbligazione number;
	lDataMigrazione date;
	lCdTerzo number;

	lImpegno obbligazione%rowtype;
	lImpegnoScad obbligazione_scadenzario%rowtype;
	lImpegnoScadVoce obbligazione_scad_voce%rowtype;
	lCds unita_organizzativa%rowtype;
	lUo  unita_organizzativa%rowtype;
	lCdr cdr%rowtype;
	lElemVoce elemento_voce%rowtype;

	lCdTitoloCapitolo voce_f.cd_titolo_capitolo%type;
	lPgUltimoCaricamento number;
	begin
	 	 gPgLog := ibmutl200.LOGSTART('Migrazione Residui Passivi' , cgUtente, 1, 1);

	 	 gMsgLog := 'Impossibile recuperare il pg_caricamento dalla tabella CNR_RESIDUI_PASSIVI';
		 -- Recuperiamo il progressivo dell'ultimo caricamento
		 select nvl(max(pg_caricamento),0)
		 into lPgUltimoCaricamento
		 from cnr_residui_passivi;

	 	 gMsgLog := 'Impossibile recuperare il CdS per l''ENTE';
  		 -- Recuperiamo il cds realtivo all'ente
     	 select *
  		 into lCds
     	 from  unita_organizzativa
     	 where cd_tipo_unita = cnrctb020.tipo_ente
  		 and   fl_cds = 'Y';

	 	 gMsgLog := 'Impossibile recuperare la UO per l''ENTE';
  		 -- Recuperiamo l'UO realtivo all'ente
     	 select *
  		 into lUo
     	 from  unita_organizzativa
     	 where cd_tipo_unita = cnrctb020.tipo_ente
  		 and   fl_uo_cds = 'Y';

	 	 gMsgLog := 'Impossibile recuperare il CDR';
  		 -- Recuperiamo il cdr realtivo all'ente
     	 lCdr := cnrctb020.GETCDRENTE;

	 	 gMsgLog := 'Impossibile recuperare il progressivo per l''impegno';
		 -- Recuperiamo il progressivo dell'obbligazione che deve essere inserita
		 select nvl(max(substr(to_char(pg_obbligazione),5,6)),0)
 		 into lPgObbIniziale
		 from obbligazione
		 where cd_Cds = lCds.cd_unita_organizzativa
		 and   esercizio=gcEsercizio
		 and   cd_tipo_documento_cont = CNRCTB018.TI_DOC_IMP
		 and   riportato = 'Y';
--		 and   substr(to_char(pg_obbligazione),1,4) = lResiduoPasSci.esercizio ;

		 -- Recuperia il terzo di default.
		 begin
			 select im01
			 into lCdTerzo
			 from configurazione_cnr
			 where cd_chiave_primaria = 'TERZO_SPECIALE'
			 and cd_chiave_secondaria = 'CODICE_DIVERSI_IMPEGNI';
		 exception when no_data_found then
				   gMsgLog := 'Nella tabella CONFIGURAZIONE_CNR manca : TERZO_SPECIALE per CODICE_DIVERSI_IMPEGNI';
		 		   ibmutl200.logInf(gPgLog ,gMsgLog, '', '');
		 end;


		 lDataMigrazione := sysdate;
		 lNumResiduiPasSci := 0;
		 lContatore :=0;

		 -- eseguire il lock della tabella CNR_RESIDUI_PASSIVI
	 	 LOCK TABLE CNR_RESIDUI_PASSIVI IN EXCLUSIVE MODE NOWAIT;

		 for lResiduoPasSci in (SELECT * FROM CNR_RESIDUI_PASSIVI
		 	 				    where pg_caricamento = lPgUltimoCaricamento
								) loop
		 	 begin
-- Punto di ritorno
savepoint altro_impegno;

		         lContatore := lContatore + 1;
			 	 gMsgLog := 'Impossibile recuperare il progressivo per l''impegno ';
		         ibmutl200.logInf(gPgLog ,'Processando residuo Passivo num '|| lContatore ||' Esercizio = '|| lResiduoPasSci.ESERCIZIO || ' - Appart = ' || lResiduoPasSci.TI_APPARTENENZA || ' - Gest = '|| lResiduoPasSci.TI_GESTIONE || ' - voce = ' || lResiduoPasSci.CD_VOCE || ' - Progressivo = ' || lResiduoPasSci.PG_CARICAMENTO, '', '');
				 -- Recuperiamo il progressivo dell'obbligazione che deve essere inserita
				 select nvl(max(substr(to_char(pg_obbligazione),5,6)),0) + 1
		 		 into lPgObbIniziale
				 from obbligazione
				 where cd_Cds = lCds.cd_unita_organizzativa
				 and   esercizio=gcEsercizio
				 and   cd_tipo_documento_cont = CNRCTB018.TI_DOC_IMP
				 and   riportato = 'Y'
				 and   substr(to_char(pg_obbligazione),1,4) = lResiduoPasSci.esercizio
				 and   esercizio_originale = lResiduoPasSci.esercizio;

				 -- Numero dei Impegni residui inserti in OBBLIGAZIONE
			 	 lNumResiduiPasSci := lNumResiduiPasSci + 1;

	   		 	 -- Recuperiamo l'elemento voce = cd_titolo_capitolo della tabella VOCE_F
  			     gMsgLog := 'Voce finanziaria , cd_voce = ' || lResiduoPasSci.cd_voce || ' ti_appartenenza = ' || lResiduoPasSci.ti_appartenenza || ' ti_gestione = ' || lResiduoPasSci.ti_gestione || ' esercizio = ' || gcEsercizio || ' non trovata';
			 	 select cd_titolo_capitolo
				 into lCdTitoloCapitolo
			     from voce_f
				 where ESERCIZIO 	   = gcEsercizio
				 and   ti_appartenenza = lResiduoPasSci.ti_appartenenza
				 and   ti_gestione 	   = lResiduoPasSci.ti_gestione
				 and   CD_VOCE 		   = lResiduoPasSci.cd_voce
				 and   fl_mastrino 	   ='Y';

			   	 gMsgLog := 'Elemento Voce , cd_elemento_voce = ' || lCdTitoloCapitolo || ' ti_appartenenza = ' || lResiduoPasSci.ti_appartenenza || ' ti_gestione = ' || lResiduoPasSci.ti_gestione || ' esercizio = ' || gcEsercizio || ' non trovato';
				 select *
		  		 into lElemVoce
				 from elemento_voce
				 where ESERCIZIO 	    = gcEsercizio
				 and   ti_appartenenza  = lResiduoPasSci.ti_appartenenza
				 and   ti_gestione 	   	= lResiduoPasSci.ti_gestione
				 and   cd_elemento_voce = lCdTitoloCapitolo;


				 lPgObbligazione := to_number(to_char(lResiduoPasSci.esercizio) || lpad(to_char(lPgObbIniziale ),6,'0'));

				 lImpegno.CD_CDS                        := lCds.cd_unita_organizzativa;
				 lImpegno.ESERCIZIO		        := gcEsercizio;
				 lImpegno.ESERCIZIO_ORIGINALE           := lResiduoPasSci.esercizio;
				 lImpegno.PG_OBBLIGAZIONE 		:= lPgObbligazione;
		 		 lImpegno.CD_TIPO_DOCUMENTO_CONT 	:= CNRCTB018.TI_DOC_IMP;
				 lImpegno.CD_UNITA_ORGANIZZATIVA        := lUo.cd_unita_organizzativa;
				 lImpegno.CD_CDS_ORIGINE		:= substr(lResiduoPasSci.cd_uo_origine,1,3);
				 lImpegno.CD_UO_ORIGINE			:= lResiduoPasSci.cd_uo_origine;
				 lImpegno.CD_TIPO_OBBLIGAZIONE 		:=  NULL;
				 lImpegno.TI_APPARTENENZA               := lResiduoPasSci.TI_APPARTENENZA;
				 lImpegno.TI_GESTIONE			:= lResiduoPasSci.TI_GESTIONE;
				 lImpegno.CD_ELEMENTO_VOCE		:= lElemVoce.cd_elemento_voce; --- ?????????????
				 lImpegno.DT_REGISTRAZIONE		:= to_date('0101'||gcEsercizioDest,'ddmmyyyy');
				 lImpegno.DS_OBBLIGAZIONE               := 'IMPEGNO RESIDUO DA MIGRAZIONE';
				 lImpegno.NOTE_OBBLIGAZIONE             := NULL;
				 lImpegno.CD_TERZO			:= lCdTerzo;
				 lImpegno.IM_OBBLIGAZIONE		:= lResiduoPasSci.IM_RESIDUO;
				 lImpegno.IM_COSTI_ANTICIPATI		:= 0;
				 lImpegno.ESERCIZIO_COMPETENZA		:= gcEsercizio;
				 lImpegno.STATO_OBBLIGAZIONE		:= 'D';
				 lImpegno.DT_CANCELLAZIONE              := NULL;
				 lImpegno.CD_RIFERIMENTO_CONTRATTO      := NULL;
				 lImpegno.DT_SCADENZA_CONTRATTO	        := NULL;
				 lImpegno.FL_CALCOLO_AUTOMATICO		  := 'N';
				 lImpegno.CD_FONDO_RICERCA		   	  := NULL;
				 lImpegno.FL_SPESE_COSTI_ALTRUI		  := 'N';
				 lImpegno.FL_PGIRO				   	  := lElemVoce.FL_PARTITA_GIRO;
				 lImpegno.RIPORTATO				   	  := 'N';
				 lImpegno.DACR					   	  := lDataMigrazione;
				 lImpegno.UTCR					   	  := cgUtente;
				 lImpegno.DUVA					   	  := lDataMigrazione;
				 lImpegno.UTUV					   	  := cgUtente;
				 lImpegno.PG_VER_REC				  	  := 1;
				 lImpegno.CD_CDS_ORI_RIPORTO    		  := NULL;
				 lImpegno.ESERCIZIO_ORI_RIPORTO	   	  := NULL;
				 lImpegno.ESERCIZIO_ORI_ORI_RIPORTO	   	  := NULL;
				 lImpegno.PG_OBBLIGAZIONE_ORI_RIPORTO  := NULL;

				 lImpegnoScad.CD_CDS             := lCds.cd_unita_organizzativa;
				 lImpegnoScad.ESERCIZIO 	 := lImpegno.ESERCIZIO;
				 lImpegnoScad.ESERCIZIO_ORIGINALE := lImpegno.ESERCIZIO_ORIGINALE;
				 lImpegnoScad.PG_OBBLIGAZIONE 	 := lImpegno.PG_OBBLIGAZIONE;
				 lImpegnoScad.PG_OBBLIGAZIONE_SCADENZARIO := 1;
				 lImpegnoScad.DT_SCADENZA 	 := to_Date('3112'||gcEsercizio,'ddmmyyyy');
				 lImpegnoScad.DS_SCADENZA 	 := 'SCADENZA UNICA PER RESIDUO DI MIGRAZIONE';
				 lImpegnoScad.IM_SCADENZA 	 := lImpegno.IM_OBBLIGAZIONE;
				 lImpegnoScad.IM_ASSOCIATO_DOC_AMM       := 0;
				 lImpegnoScad.IM_ASSOCIATO_DOC_CONTABILE := 0;
				 lImpegnoScad.DACR       := lDataMigrazione;
				 lImpegnoScad.UTCR 		 := cgUtente;
				 lImpegnoScad.DUVA 		 := lDataMigrazione;
				 lImpegnoScad.UTUV 		 := cgUtente;
				 lImpegnoScad.PG_VER_REC := 1;

				 lImpegnoScadVoce.CD_CDS              := lCds.cd_unita_organizzativa;
				 lImpegnoScadVoce.ESERCIZIO 	      := lImpegno.ESERCIZIO;
				 lImpegnoScadVoce.ESERCIZIO_ORIGINALE := lImpegno.ESERCIZIO_ORIGINALE;
				 lImpegnoScadVoce.PG_OBBLIGAZIONE     := lImpegno.PG_OBBLIGAZIONE;
				 lImpegnoScadVoce.PG_OBBLIGAZIONE_SCADENZARIO := lImpegnoScad.PG_OBBLIGAZIONE_SCADENZARIO;
				 lImpegnoScadVoce.TI_APPARTENENZA :=  lResiduoPasSci.TI_APPARTENENZA;
				 lImpegnoScadVoce.TI_GESTIONE 	 := lResiduoPasSci.TI_GESTIONE;
				 lImpegnoScadVoce.CD_VOCE 		 := lResiduoPasSci.cd_voce;
				 lImpegnoScadVoce.CD_CENTRO_RESPONSABILITA := lCdr.cd_centro_Responsabilita;
				 lImpegnoScadVoce.CD_LINEA_ATTIVITA := CNRCTB015.GETVAL02PERCHIAVE(CNRCTB035.LA_SPECIALE,CNRCTB035.LA_ENTRATA_ENTE);
				 lImpegnoScadVoce.IM_VOCE 		   := lImpegno.IM_OBBLIGAZIONE;
				 lImpegnoScadVoce.CD_FONDO_RICERCA  := NULL;
				 lImpegnoScadVoce.DACR       := lDataMigrazione;
				 lImpegnoScadVoce.UTCR 		:= cgUtente;
				 lImpegnoScadVoce.DUVA 		:= lDataMigrazione;
				 lImpegnoScadVoce.UTUV      	:= cgUtente;
				 lImpegnoScadVoce.PG_VER_REC := 1;

				 -- La funzione ChkObbligazione controlla se l'impegno che vogliamo inserire
				 -- risulta gia presente, il controllo viene fatto in base all'easercizio
				 -- del residuo sci e al cd_voce.
				 -- Se l'impegno risulta gia inserito questo viene opportunamente modificato
				 if not ChkObbligazione(lImpegno,lResiduoPasSci,lDataMigrazione) then
				 	 cnrctb035.INS_OBBLIGAZIONE(lImpegno);
					 cnrctb035.INS_OBBLIGAZIONE_SCADENZARIO(lImpegnoScad);
					 cnrctb035.INS_OBBLIGAZIONE_SCAD_VOCE(lImpegnoScadVoce);
				 else
				 	 begin
				 	 	 gMsgLog := 'Impossibile recuperare l''imegno precedentemente migrato, cd_voce' || lResiduoPasSci.cd_voce;
						 -- Recuperiamo eventuale imegno gia inserito nel gcEsercizio
						 -- esercizio in cui e stato generato l'imegno = aResiduoSci.esercizio
						 -- l'impegno originale del 2002 se generato ha il pg_obbligazione= xxxxyyyyyy
						 -- dove xxxx=aResiduoSci.esercizio

						 select *
						 into lImpegno
						 from obbligazione
						 where (cd_cds, esercizio,esercizio_originale,pg_obbligazione) in
						 	   (select cd_cds, esercizio,esercizio_originale,pg_obbligazione
						 	   from obbligazione_scad_voce
						 	   where length(to_char(pg_obbligazione))     = 10
						 	   and   esercizio 			      = gcEsercizio
						 	   and   esercizio_originale                  = lResiduoPasSci.esercizio
						 	   and   substr(to_char(pg_obbligazione),1,4) = lResiduoPasSci.esercizio
						 	   and   ti_appartenenza  		      = lResiduoPasSci.ti_appartenenza
						 	   and   ti_gestione      		      = lResiduoPasSci.ti_gestione
						 	   and   cd_voce 			      = lResiduoPasSci.cd_voce);


				     exception when no_Data_found then
				 		   ibmutl200.logInf(gPgLog ,gMsgLog, '', '');
					 end;
				 end if;

			 	 crea_residuo_passivo(lImpegno,lResiduoPasSci,lDataMigrazione);
			 exception when no_data_found then
					   ibmutl200.logInf(gPgLog ,gMsgLog, '', '');
-- 	 			       when others then
-- 	 	 		   	   ibmutl200.logInf(gPgLog ,'residuo origine Esercizio'|| lResiduoPasSci.esercizio || ' voce '|| lResiduoPasSci.cd_voce || ' - ' || sqlerrm (sqlcode), '', '');
-- 	 			   	   lNumResiduiPasSci := lNumResiduiPasSci - 1;
-- 	 	 		   	   rollback to savepoint altro_impegno;
			 end;
		 end loop;
		 -- Cancelliamo i residui attivi
		 SetResiduiPassiviCanc(lDataMigrazione);
	end;

	-- Questa funzione controlla se un residuo proveniente da SCI
	-- risulta gia stato inserito come IMPEGNO in CIR
	function ChkObbligazione(aImpegno in out obbligazione%rowtype, aResiduoSci cnr_residui_passivi%rowtype, aSysdate date) return boolean is
 	lImpegnoOld     obbligazione%rowtype;
 	lScad           obbligazione_scadenzario%rowtype;
 	lScadVoce  		obbligazione_scad_voce%rowtype;
	lScad2003 		obbligazione_scadenzario%rowtype;
	lVoceF 			voce_f%rowtype;
 	lGiaAssociato boolean;
 	lGiaInserito boolean;
 	lDelta number(15,2);

	lTipoModifica number;

	begin
		begin
			 -- Recuperiamo eventuale imegno gia inserito nel gcEsercizio
			 -- esercizio in cui e stato generato l'imegno = aResiduoSci.esercizio
			 -- l'impegno originale del 2002 se generato ha il pg_obbligazione= xxxxyyyyyy
			 -- dove xxxx=aResiduoSci.esercizio

 			 select *
			 into lImpegnoOld
			 from obbligazione
			 where (cd_cds, esercizio, esercizio_originale, pg_obbligazione)
			 	   in (select cd_cds, esercizio, esercizio_originale, pg_obbligazione
					   from obbligazione_scad_voce
					   where length(to_char(pg_obbligazione))     = 10
					   and   esercizio 			      = gcEsercizio
				 	   and   esercizio_originale                  = aResiduoSci.esercizio
					   and   substr(to_char(pg_obbligazione),1,4) = aResiduoSci.esercizio
					   and   ti_appartenenza  					  = aResiduoSci.ti_appartenenza
					   and   ti_gestione      					  = aResiduoSci.ti_gestione
					   and   cd_voce 							  = aResiduoSci.cd_voce);

		     lGiaInserito:=true;
		exception when no_Data_found then
				  lGiaInserito:=false;
		end;

		begin
			 if lGiaInserito then
				 -- Controlliamo se impegno risulta
				 -- associato a documenti amministrativi e recuperiamo la sua scadenza
				 ObbligazioneAssDocAmm(lImpegnoOld, lScad, lGiaAssociato);

				 ----------------------------------------------------------------------
				 -- Identificare il tipo di eventuale cambiamento del residuo        --
				 ----------------------------------------------------------------------
				 -- Tipo Cambio 2 : Importo diverso nuovo > vecchio
				 -- Tipo Cambio 3 : Importo diverso nuovo < vecchio
				 lTipoModifica :=0;

				 if aImpegno.IM_OBBLIGAZIONE > lImpegnoOld.IM_OBBLIGAZIONE then
				 	lTipoModifica := 2;
				 end if;
				 if aImpegno.IM_OBBLIGAZIONE < lImpegnoOld.IM_OBBLIGAZIONE then
				 	lTipoModifica := 3;
				 end if;

				 -- Condizioni di arresto del riporto.
				 if lTipoModifica = 0 then
				 	gMsgLog := 'Impegno non risulta in nessun modo cambiato dal precedente riporto, pg_obbligazione' || to_char(lImpegnoOld.pg_obbligazione);
		 		    ibmutl200.logInf(gPgLog ,gMsgLog, '', '');
				 end if;

				 if lTipoModifica = 2 then
				 	 -- Siamo nel caso in cui stiamo inserendo un impegno residuo
					 -- che risulta gia inserito nella tabella OBBLIGAZIONE. Quindi
					 -- siccome risulta cambiato l'importo dell'impegno occorre
					 -- seguire il seguente algoritmo :
					 -- Importo imegno NUOVO (che vogliamo inserire) risulta
					 --    MAGGIORE dell'imimpegno Principale
					 --	   (gia presente nella tabella esercizio) in questo caso  =>
					 --    viene modificato l'importo relativo alla scadenza

					 -- Impostiamo i dati della scadenza libera
					 lScad.IM_SCADENZA := aImpegno.IM_OBBLIGAZIONE;
				   	 lScad.DUVA := aSysdate;
				         lScad.UTUV := cgUtente;
				         lScad.PG_VER_REC := lScad.PG_VER_REC + 1;

					 -- Recuperiamo il dettaglio voce associato alla scadenza
					 select *
					 into lScadVoce
					 from obbligazione_scad_voce asv
					 where asv.CD_CDS 		       = lScad.cd_cds
					 and   asv.ESERCIZIO 		       = lScad.esercizio
					 and   asv.ESERCIZIO_ORIGINALE	       = lScad.esercizio_originale
					 and   asv.PG_OBBLIGAZIONE 	       = lScad.pg_obbligazione
					 and   asv.pg_obbligazione_scadenzario = lScad.pg_obbligazione_scadenzario;

					 -- Impostiamo i dati del dettaglio voce
					 lScadVoce.IM_VOCE					   := lScad.IM_SCADENZA;
					 lScadVoce.DUVA						   := aSysdate;
					 lScadVoce.UTUV						   := cgUtente;
					 lScadVoce.PG_VER_REC				   := lScadVoce.PG_VER_REC + 1;

 					 -- Aggiornamento Impegno Originario
 					 update obbligazione
 					 set im_obbligazione = aImpegno.im_obbligazione
 					 	 ,utuv 			 = cgUtente
 						 ,duva			 = aSysdate
 					 	 ,pg_ver_rec 	 = PG_VER_REC + 1
 					 where cd_cds          = lImpegnoOld.cd_cds
 					 and   esercizio       = lImpegnoOld.esercizio
 					 and   esercizio_originale = lImpegnoOld.esercizio_originale
 					 and   pg_obbligazione = lImpegnoOld.pg_obbligazione;

			 		 -- Aggiornamento scadenza Originaria
					 update obbligazione_scadenzario
					 set  IM_SCADENZA = lScad.IM_SCADENZA
					 	 ,DUVA 		  = lScad.DUVA
						 ,UTUV 		  = lScad.UTUV
						 ,PG_VER_REC  = lScad.PG_VER_REC
					 where cd_cds                      = lScad.cd_cds
					 and   esercizio 		   = lScad.esercizio
 					 and   esercizio_originale         = lScad.esercizio_originale
					 and   pg_obbligazione		   = lScad.pg_obbligazione
					 and   pg_obbligazione_scadenzario = lScad.pg_obbligazione_scadenzario;

			 		 -- Aggiornamento scadenza voce Originaria
					 update obbligazione_scad_voce
					 set  IM_VOCE    = lScadVoce.IM_VOCE
					 	 ,DUVA 		 = lScadVoce.DUVA
						 ,UTUV 		 = lScadVoce.UTUV
						 ,PG_VER_REC = lScadVoce.PG_VER_REC
					 where cd_cds                      = lScad.cd_cds
					 and   esercizio 		   = lScad.esercizio
 					 and   esercizio_originale         = lScad.esercizio_originale
					 and   pg_obbligazione 		   = lScad.pg_obbligazione
					 and   pg_obbligazione_scadenzario = lScad.pg_obbligazione_scadenzario
					 and   ti_appartenenza 		   = lScadVoce.ti_appartenenza
					 and   ti_gestione 		   = lScadVoce.ti_gestione
					 and   cd_voce	   		   = lScadVoce.cd_voce
					 and   cd_centro_responsabilita    = lScadVoce.cd_centro_responsabilita
					 and   cd_linea_attivita 	   = lScadVoce.cd_linea_attivita;
				 end if; --lTipoModifica = 2

				 if lTipoModifica = 3 then
				 	 -- Siamo nel caso in cui stiamo inserendo un accertamento residuo
					 -- che risulta gia inserito nella tabella OBBLIGAZIONE. Quindi
					 -- siccome risulta cambiato l'importo dell'accertamento occorre
					 -- seguire il seguente algoritmo :
					 -- Importo imegno NUOVO (che vogliamo inserire) risulta
					 --    minore dell'importo dell'impegno Principale
					 --	   (gia presente nella tabella) in questo caso  =>
					 --    viene modificato l'importo relativo alla scadenza
					 --    se e salo se resulta
					 -- (IM_SCADENZA-IM_ASSOCIATO_DOC_AMM)=> (lImpegnoOld.IM_OBBLIGAZIONE - aImpegno.IM_OBBLIGAZIONE )
					 -- Recuperiamo l'importo della scadenza dell'impegno residuo relativo
					 -- al 2003
					 -- Recuperiamo il dettaglio voce associato alla scadenza
					 select *
					 into lScad2003
					 from obbligazione_scadenzario asv
					 where asv.CD_CDS 		= lImpegnoOld.cd_cds
					 and   asv.ESERCIZIO 		= gcEsercizioDest
					 and   asv.ESERCIZIO_ORIGINALE	= lImpegnoOld.esercizio_originale
					 and   asv.PG_OBBLIGAZIONE 	= lImpegnoOld.pg_obbligazione;


					 if lScad2003.im_associato_doc_amm <= aImpegno.IM_OBBLIGAZIONE then

						 -- Impostiamo i dati della scadenza
						 lScad.IM_SCADENZA := aImpegno.IM_OBBLIGAZIONE;
					   	 lScad.DUVA := aSysdate;
					     lScad.UTUV := cgUtente;
					     lScad.PG_VER_REC := lScad.PG_VER_REC + 1;

						 -- Recuperiamo il dettaglio voce associato alla scadenza
						 select *
						 into lScadVoce
						 from obbligazione_scad_voce asv
						 where asv.CD_CDS 		       = lScad.cd_cds
						 and   asv.ESERCIZIO 		       = lScad.esercizio
						 and   asv.ESERCIZIO_ORIGINALE	       = lScad.esercizio_originale
						 and   asv.PG_OBBLIGAZIONE 	       = lScad.pg_obbligazione
						 and   asv.pg_obbligazione_scadenzario = lScad.pg_obbligazione_scadenzario;

						 -- Impostiamo i dati del dettaglio voce
						 lScadVoce.IM_VOCE					   := lScad.IM_SCADENZA;
						 lScadVoce.DUVA						   := aSysdate;
						 lScadVoce.UTUV						   := cgUtente;
						 lScadVoce.PG_VER_REC				   := lScadVoce.PG_VER_REC + 1;

						 -- Aggiornamento Accertamento, scadenza, voce
						 update obbligazione
						 set im_obbligazione = aImpegno.im_obbligazione
							 ,utuv = cgUtente
							 ,duva = aSysdate
						 	 ,pg_ver_rec = PG_VER_REC + 1
						 where cd_cds 	   	   = lImpegnoOld.cd_cds
						 and   esercizio 	   = lImpegnoOld.esercizio
						 and   esercizio_originale = lImpegnoOld.esercizio_originale
						 and   pg_obbligazione     = lImpegnoOld.pg_obbligazione;

				 		 -- Aggiornamento scadenza
						 update obbligazione_scadenzario
						 set  IM_SCADENZA = lScad.IM_SCADENZA
						 	 ,DUVA        = lScad.DUVA
							 ,UTUV 		  = lScad.UTUV
							 ,PG_VER_REC  = PG_VER_REC + 1
						 where cd_cds                      = lScad.cd_cds
						 and   esercizio 		   = lScad.esercizio
						 and   esercizio_originale         = lScad.esercizio_originale
						 and   pg_obbligazione 	           = lScad.pg_obbligazione
						 and   pg_obbligazione_scadenzario = lScad.pg_obbligazione_scadenzario;

				 		 -- Aggiornamento scadenza voce
						 update obbligazione_scad_voce
						 set  IM_VOCE    = lScadVoce.IM_VOCE
						 	 ,DUVA 		 = lScadVoce.DUVA
							 ,UTUV 		 = lScadVoce.UTUV
							 ,PG_VER_REC = PG_VER_REC + 1
						 where cd_cds                      = lScad.cd_cds
						 and   esercizio 		   = lScad.esercizio
						 and   esercizio_originale	   = lScad.esercizio_originale
						 and   pg_obbligazione 		   = lScad.pg_obbligazione
						 and   pg_obbligazione_scadenzario = lScad.pg_obbligazione_scadenzario;
					 else
					 	gMsgLog := 'Impossibile modificare impegno ' || to_char(lImpegnoOld.pg_obbligazione) || ' importo residuo disponibile troppo basso';
			 		    ibmutl200.logInf(gPgLog ,gMsgLog, '', '');
					 end if;

				 end if; --lTipoModifica = 3
			 	 -- In questo caso l'impegno risulta gia inserito precedentemente
				 -- Inoltre risulta (se le condizioni lo permettono) modificato in importo
				 return TRUE;
			 else --Non presente
			 	 -- In questo caso l'accertamento risulta essere mai stato inserito
				 -- precedentemente.
				 return FALSE;
			 end if;
		end;

	end ChkObbligazione;

	procedure crea_residuo_passivo (aImpegno obbligazione%rowtype
			  					    ,aResiduoSci cnr_residui_passivi%rowtype
									,aSysdate date) is

	lImpegnoResiduo obbligazione%rowtype;
	lDelta number (15,2);
	begin

		 lImpegnoResiduo.CD_CDS                      := aImpegno.CD_CDS;
		 lImpegnoResiduo.ESERCIZIO		     := gcEsercizioDest;
		 lImpegnoResiduo.ESERCIZIO_ORIGINALE	     := aImpegno.ESERCIZIO_ORIGINALE;
		 lImpegnoResiduo.PG_OBBLIGAZIONE 	     := aImpegno.PG_OBBLIGAZIONE;
		 lImpegnoResiduo.CD_TIPO_DOCUMENTO_CONT      := cnrctb018.TI_DOC_IMP_RES;
		 lImpegnoResiduo.CD_UNITA_ORGANIZZATIVA      := aImpegno.CD_UNITA_ORGANIZZATIVA;
		 lImpegnoResiduo.CD_CDS_ORIGINE		     := aImpegno.CD_CDS_ORIGINE;
		 lImpegnoResiduo.CD_UO_ORIGINE		     := aImpegno.CD_UO_ORIGINE;
		 lImpegnoResiduo.CD_TIPO_OBBLIGAZIONE        := aImpegno.CD_TIPO_OBBLIGAZIONE;
		 lImpegnoResiduo.TI_APPARTENENZA             := aImpegno.TI_APPARTENENZA;
		 lImpegnoResiduo.TI_GESTIONE		     := aImpegno.TI_GESTIONE;
		 lImpegnoResiduo.CD_ELEMENTO_VOCE	     := aImpegno.CD_ELEMENTO_VOCE;
		 lImpegnoResiduo.DT_REGISTRAZIONE	     := aImpegno.DT_REGISTRAZIONE;
		 lImpegnoResiduo.DS_OBBLIGAZIONE             := aImpegno.DS_OBBLIGAZIONE;
		 lImpegnoResiduo.NOTE_OBBLIGAZIONE           := aImpegno.NOTE_OBBLIGAZIONE;
		 lImpegnoResiduo.CD_TERZO		     := aImpegno.CD_TERZO;
		 lImpegnoResiduo.IM_OBBLIGAZIONE	     := aImpegno.IM_OBBLIGAZIONE;
		 lImpegnoResiduo.IM_COSTI_ANTICIPATI		 := aImpegno.IM_COSTI_ANTICIPATI;
		 lImpegnoResiduo.ESERCIZIO_COMPETENZA	   	 := gcEsercizioDest;
		 lImpegnoResiduo.STATO_OBBLIGAZIONE		     := aImpegno.STATO_OBBLIGAZIONE;
		 lImpegnoResiduo.DT_CANCELLAZIONE            := aImpegno.DT_CANCELLAZIONE;
		 lImpegnoResiduo.CD_RIFERIMENTO_CONTRATTO    := aImpegno.CD_RIFERIMENTO_CONTRATTO;
		 lImpegnoResiduo.DT_SCADENZA_CONTRATTO	   	 := aImpegno.DT_SCADENZA_CONTRATTO;
		 lImpegnoResiduo.FL_CALCOLO_AUTOMATICO		 := aImpegno.FL_CALCOLO_AUTOMATICO;
		 lImpegnoResiduo.CD_FONDO_RICERCA		   	 := aImpegno.CD_FONDO_RICERCA;
		 lImpegnoResiduo.FL_SPESE_COSTI_ALTRUI		 := aImpegno.FL_SPESE_COSTI_ALTRUI;
		 lImpegnoResiduo.FL_PGIRO				   	 := aImpegno.FL_PGIRO;
		 lImpegnoResiduo.RIPORTATO				   	 := 'N';
		 lImpegnoResiduo.DACR					   	 := aImpegno.DACR;
		 lImpegnoResiduo.UTCR					   	 := aImpegno.UTCR;
		 lImpegnoResiduo.DUVA					   	 := aSysdate;
		 lImpegnoResiduo.UTUV					   	 := cgUtente;
		 lImpegnoResiduo.PG_VER_REC				     := 1;
		 lImpegnoResiduo.CD_CDS_ORI_RIPORTO          := aImpegno.CD_CDS;
		 lImpegnoResiduo.ESERCIZIO_ORI_RIPORTO	   	 := aImpegno.ESERCIZIO;
		 lImpegnoResiduo.ESERCIZIO_ORI_ORI_RIPORTO	 := aImpegno.ESERCIZIO_ORIGINALE;
		 lImpegnoResiduo.PG_OBBLIGAZIONE_ORI_RIPORTO := aImpegno.PG_OBBLIGAZIONE;

--		 PreparaScadResiduoPas(lImpegnoResiduo, aUtente, aSysdate);
		 ChkPresenzaResiduoPas (lImpegnoResiduo, aSysdate, lDelta);
		 -- Aggiorniamo l'impegno
		 update obbligazione
		 set riportato   = 'Y'
		 	 ,duva 	     = aSysdate
			 ,utuv	     = cgUtente
			 ,pg_ver_rec = PG_VER_REC + 1
		 where cd_cds = aImpegno.cd_cds
		 and   esercizio = aImpegno.esercizio
		 and   esercizio_originale = aImpegno.esercizio_originale
		 and   pg_obbligazione = aImpegno.pg_obbligazione;

		 -- Aggiornare voce_f_saldi_cmp
		 update voce_f_saldi_cmp
		 set  im_obblig_imp_acr = im_obblig_imp_acr + lDelta,
		      IM_STANZ_INIZIALE_A1 = im_obblig_imp_acr + lDelta,
			  utuv				= lImpegnoResiduo.utuv,
			  duva				= lImpegnoResiduo.duva,
			  pg_ver_rec		= pg_ver_rec + 1
		 where esercizio 	 = lImpegnoResiduo.esercizio
	     and cd_cds 	     = lImpegnoResiduo.cd_cds
		 and ti_appartenenza = lImpegnoResiduo.ti_appartenenza
		 and ti_gestione 	 = lImpegnoResiduo.ti_gestione
		 and cd_voce 		 = aResiduoSci.cd_voce
		 and ti_competenza_residuo = CNRCTB054.TI_RESIDUI;

	end ;

	-- Controlla se il residuo passivo risulta gia inserito
	procedure ChkPresenzaResiduoPas(aImpegnoResiduo obbligazione%rowtype, aSysdate date, aDelta in out number) is
	lImpegnoResiduoPre  obbligazione%rowtype;
	lScad  obbligazione_scadenzario%rowtype;
	lScadVoce  		obbligazione_scad_voce%rowtype;
	lGiaAssociato boolean;
	lGiaInserito boolean;
	lTrovataScadenza boolean;
	lTipoModifica number;

	begin
		aDelta := 0;
		begin
			 select *
			 into lImpegnoResiduoPre
			 from obbligazione obb
			 where obb.CD_CDS          = aImpegnoResiduo.CD_CDS
			 and   obb.ESERCIZIO       = gcEsercizioDest
			 and   obb.ESERCIZIO_ORIGINALE = aImpegnoResiduo.ESERCIZIO_ORIGINALE
			 and   obb.PG_OBBLIGAZIONE = aImpegnoResiduo.PG_OBBLIGAZIONE
			 for update nowait;

		 	 lGiaInserito := TRUE;
		exception when no_data_found then
			 	lGiaInserito := FALSE;
		end ;
		begin
			 if lGiaInserito then
				 -- Controlliamo se impegno risulta
				 -- associato a documenti amministrativi e recuperiamo la sua scadenza
				 ObbligazioneAssDocAmm(lImpegnoResiduoPre, lScad, lGiaAssociato);

				 ----------------------------------------------------------------------
				 -- Identificare il tipo di eventuale cambiamento del residuo        --
				 ----------------------------------------------------------------------
				 -- Tipo Cambio 2 : Importo diverso nuovo > vecchio
				 -- Tipo Cambio 3 : Importo diverso nuovo < vecchio
				 lTipoModifica :=0;

				 if aImpegnoResiduo.IM_OBBLIGAZIONE > lImpegnoResiduoPre.IM_OBBLIGAZIONE then
				 	lTipoModifica := 2;
				 end if;
				 if aImpegnoResiduo.IM_OBBLIGAZIONE < lImpegnoResiduoPre.IM_OBBLIGAZIONE then
				 	lTipoModifica := 3;
				 end if;

				 -- Condizioni di arresto del riporto.
				 if lTipoModifica = 0 then
				 	gMsgLog := 'Impegno non risulta in nessun modo cambiato dal precedente riporto, pg_obbligazione' || to_char(lImpegnoResiduoPre.pg_obbligazione);
		 		    ibmutl200.logInf(gPgLog ,gMsgLog, '', '');
				 end if;

				 if lTipoModifica = 2 then
				 	 -- Siamo nel caso in cui stiamo inserendo un impegno residuo
					 -- che risulta gia inserito nella tabella OBBLIGAZIONE. Quindi
					 -- siccome risulta cambiato l'importo dell'impegno occorre
					 -- seguire il seguente algoritmo :
					 -- Importo imegno NUOVO (che vogliamo inserire) risulta
					 --    MAGGIORE dell'imimpegno Principale
					 --	   (gia presente nella tabella esercizio) in questo caso  =>
					 --    viene modificato l'importo relativo alla scadenza

					 -- Impostiamo i dati della scadenza libera
					 lScad.IM_SCADENZA := aImpegnoResiduo.IM_OBBLIGAZIONE;
				   	 lScad.DUVA := aSysdate;
				     lScad.UTUV := cgUtente;
				     lScad.PG_VER_REC := lScad.PG_VER_REC + 1;

					 -- Recuperiamo il dettaglio voce associato alla scadenza
					 select *
					 into lScadVoce
					 from obbligazione_scad_voce asv
					 where asv.CD_CDS 			  = lScad.cd_cds
					 and   asv.ESERCIZIO 		          = lScad.esercizio
					 and   asv.ESERCIZIO_ORIGINALE		  = lScad.esercizio_originale
					 and   asv.PG_OBBLIGAZIONE 	          = lScad.pg_obbligazione
					 and   asv.pg_obbligazione_scadenzario = lScad.pg_obbligazione_scadenzario;

					 -- Impostiamo i dati del dettaglio voce
					 lScadVoce.IM_VOCE					   := lScad.IM_SCADENZA;
					 lScadVoce.DUVA						   := aSysdate;
					 lScadVoce.UTUV						   := cgUtente;
					 lScadVoce.PG_VER_REC				   := lScadVoce.PG_VER_REC + 1;

 					 -- Aggiornamento Impegno Originario
 					 update obbligazione
 					 set im_obbligazione = aImpegnoResiduo.im_obbligazione
 					 	 ,utuv 			 = cgUtente
 						 ,duva			 = aSysdate
 					 	 ,pg_ver_rec 	 = PG_VER_REC + 1
 					 where cd_cds              = lImpegnoResiduoPre.cd_cds
 					 and   esercizio 	   = lImpegnoResiduoPre.esercizio
 					 and   esercizio_originale = lImpegnoResiduoPre.esercizio_originale
 					 and   pg_obbligazione = lImpegnoResiduoPre.pg_obbligazione;

			 		 -- Aggiornamento scadenza Originaria
					 update obbligazione_scadenzario
					 set  IM_SCADENZA = lScad.IM_SCADENZA
					 	 ,DUVA 		  = lScad.DUVA
						 ,UTUV 		  = lScad.UTUV
						 ,PG_VER_REC  = lScad.PG_VER_REC
					 where cd_cds                      = lScad.cd_cds
					 and   esercizio 				   = lScad.esercizio
 					 and   esercizio_originale = lScad.esercizio_originale
					 and   pg_obbligazione 			   = lScad.pg_obbligazione
					 and   pg_obbligazione_scadenzario = lScad.pg_obbligazione_scadenzario;

			 		 -- Aggiornamento scadenza voce Originaria
					 update obbligazione_scad_voce
					 set  IM_VOCE    = lScadVoce.IM_VOCE
					 	 ,DUVA 		 = lScadVoce.DUVA
						 ,UTUV 		 = lScadVoce.UTUV
						 ,PG_VER_REC = lScadVoce.PG_VER_REC
					 where cd_cds                      = lScad.cd_cds
					 and   esercizio 				   = lScad.esercizio
 					 and   esercizio_originale = lScad.esercizio_originale
					 and   pg_obbligazione 			   = lScad.pg_obbligazione
					 and   pg_obbligazione_scadenzario = lScad.pg_obbligazione_scadenzario
					 and   ti_appartenenza 			   = lScadVoce.ti_appartenenza
					 and   ti_gestione 				   = lScadVoce.ti_gestione
					 and   cd_voce	   				   = lScadVoce.cd_voce
					 and   cd_centro_responsabilita    = lScadVoce.cd_centro_responsabilita
					 and   cd_linea_attivita 		   = lScadVoce.cd_linea_attivita;

				 	 aDelta := aImpegnoResiduo.IM_OBBLIGAZIONE - lImpegnoResiduoPre.IM_OBBLIGAZIONE;
				 end if; --lTipoModifica = 2

				 if lTipoModifica = 3 then
				 	 -- Siamo nel caso in cui stiamo inserendo un accertamento residuo
					 -- che risulta gia inserito nella tabella OBBLIGAZIONE. Quindi
					 -- siccome risulta cambiato l'importo dell'accertamento occorre
					 -- seguire il seguente algoritmo :
					 -- Importo imegno NUOVO (che vogliamo inserire) risulta
					 --    minore dell'importo dell'impegno Principale
					 --	   (gia presente nella tabella) in questo caso  =>
					 --    viene modificato l'importo relativo alla scadenza
					 --    se e salo se resulta
					 -- (IM_SCADENZA-IM_ASSOCIATO_DOC_AMM)=> (lImpegnoResiduoPre.IM_OBBLIGAZIONE - aImpegnoResiduo.IM_OBBLIGAZIONE )


					 if lScad.im_associato_doc_amm <= aImpegnoResiduo.IM_OBBLIGAZIONE then

						 -- Impostiamo i dati della scadenza
						 lScad.IM_SCADENZA := aImpegnoResiduo.IM_OBBLIGAZIONE;
					   	 lScad.DUVA := aSysdate;
					     lScad.UTUV := cgUtente;
					     lScad.PG_VER_REC := lScad.PG_VER_REC + 1;

						 -- Recuperiamo il dettaglio voce associato alla scadenza
						 select *
						 into lScadVoce
						 from obbligazione_scad_voce asv
						 where asv.CD_CDS 			           = lScad.cd_cds
						 and   asv.ESERCIZIO 		 		   = lScad.esercizio
						 and   asv.ESERCIZIO_ORIGINALE	 		   = lScad.esercizio_originale
						 and   asv.PG_OBBLIGAZIONE 	   		   = lScad.pg_obbligazione
						 and   asv.pg_obbligazione_scadenzario = lScad.pg_obbligazione_scadenzario;

						 -- Impostiamo i dati del dettaglio voce
						 lScadVoce.IM_VOCE					   := lScad.IM_SCADENZA;
						 lScadVoce.DUVA						   := aSysdate;
						 lScadVoce.UTUV						   := cgUtente;
						 lScadVoce.PG_VER_REC				   := lScadVoce.PG_VER_REC + 1;

						 -- Aggiornamento Accertamento, scadenza, voce
						 update obbligazione
						 set im_obbligazione = aImpegnoResiduo.im_obbligazione
							 ,utuv = cgUtente
							 ,duva = aSysdate
						 	 ,pg_ver_rec = PG_VER_REC + 1
						 where cd_cds = lImpegnoResiduoPre.cd_cds
						 and   esercizio = lImpegnoResiduoPre.esercizio
						 and   esercizio_originale = lImpegnoResiduoPre.esercizio_originale
						 and   pg_obbligazione = lImpegnoResiduoPre.pg_obbligazione;

				 		 -- Aggiornamento scadenza
						 update obbligazione_scadenzario
						 set  IM_SCADENZA = lScad.IM_SCADENZA
						 	 ,DUVA        = lScad.DUVA
							 ,UTUV 		  = lScad.UTUV
							 ,PG_VER_REC  = PG_VER_REC + 1
						 where cd_cds                      = lScad.cd_cds
						 and   esercizio 				   = lScad.esercizio
						 and   esercizio_originale = lScad.esercizio_originale
						 and   pg_obbligazione 			   = lScad.pg_obbligazione
						 and   pg_obbligazione_scadenzario = lScad.pg_obbligazione_scadenzario;

				 		 -- Aggiornamento scadenza voce
						 update obbligazione_scad_voce
						 set  IM_VOCE    = lScadVoce.IM_VOCE
						 	 ,DUVA 		 = lScadVoce.DUVA
							 ,UTUV 		 = lScadVoce.UTUV
							 ,PG_VER_REC = PG_VER_REC + 1
						 where cd_cds                      = lScad.cd_cds
						 and   esercizio 				   = lScad.esercizio
						 and   esercizio_originale = lScad.esercizio_originale
						 and   pg_obbligazione 			   = lScad.pg_obbligazione
						 and   pg_obbligazione_scadenzario = lScad.pg_obbligazione_scadenzario;

						 aDelta := aImpegnoResiduo.IM_OBBLIGAZIONE - lImpegnoResiduoPre.IM_OBBLIGAZIONE;
 					 else
 					 	gMsgLog := 'Impossibile modificare impegno ' || to_char(lImpegnoResiduoPre.pg_obbligazione) || ' importo residuo disponibile troppo basso';
 			 		    ibmutl200.logInf(gPgLog ,gMsgLog, '', '');
				 	 	aDelta := 0;
 					 end if;

 				 end if; --lTipoModifica = 3
 			 	 -- In questo caso l'impegno risulta gia inserito precedentemente
 				 -- Inoltre risulta (se le condizioni lo permettono) modificato in importo
-- 				 return TRUE;
			 else --Non presente
				 PreparaScadResiduoPas(aImpegnoResiduo, aSysdate);
				 aDelta := aImpegnoResiduo.IM_OBBLIGAZIONE;
			 end if;
		end;

	end ChkPresenzaResiduoPas;

	procedure PreparaScadResiduoPas (aImpegnoResiduo obbligazione%rowtype, aSysdate date) IS
	lScadLibera obbligazione_scadenzario%rowtype;
	lScadVoce   obbligazione_scad_voce%rowtype;
  	lNuovoImporto number(15,2);
	begin
		 -- Recuperiamo la prima scadenza libera
		 lScadLibera := GetScadResPas(aImpegnoResiduo);

		 -- Impostiamo i dati della scadenza libera
		 lScadLibera.IM_SCADENZA := lScadLibera.IM_SCADENZA + (aImpegnoResiduo.IM_OBBLIGAZIONE);
	   	 lScadLibera.DUVA := aSysdate;
	     lScadLibera.UTUV := cgUtente;
	     lScadLibera.PG_VER_REC := lScadLibera.PG_VER_REC + 1;

		 -- Recuperiamo il dettaglio voce associato alla scadenza
		 lScadVoce := GetScadResPasVoce(lScadLibera, gcEsercizio);

		 -- Impostiamo i dati del dettaglio voce
		 lScadVoce.IM_VOCE					   := lScadLibera.IM_SCADENZA;
		 lScadVoce.DUVA						   := aSysdate;
		 lScadVoce.UTUV						   := cgUtente;
		 lScadVoce.PG_VER_REC				   := lScadVoce.PG_VER_REC + 1;

		 -- Aggiornamento Accertamento, scadenza
		 cnrctb035.INS_OBBLIGAZIONE(aImpegnoResiduo);
		 cnrctb035.INS_OBBLIGAZIONE_SCADENZARIO(lScadLibera);
		 cnrctb035.INS_OBBLIGAZIONE_SCAD_VOCE(lScadVoce);

	end;


   procedure ObbligazioneAssDocAmm(aObb obbligazione%rowtype,aObbScadenzario in out obbligazione_scadenzario%rowtype, aAssociato in out boolean)  is
	lPgObb obbligazione.PG_OBBLIGAZIONE%type;
	begin
		 -- Controlliamo se il residuo risulta associato a documenti
		 -- amministrativi
		 begin
			 select *
			 into aObbScadenzario
			 from obbligazione_scadenzario scad
			 where scad.CD_CDS          = aObb.CD_CDS
			 and   scad.ESERCIZIO       = aObb.ESERCIZIO
			 and   scad.ESERCIZIO_ORIGINALE       = aObb.ESERCIZIO_ORIGINALE
			 and   scad.PG_OBBLIGAZIONE = aObb.PG_OBBLIGAZIONE;
		 exception when no_Data_found then
		 		   aAssociato := false;
		 end;
		 if (aObbScadenzario.im_associato_doc_amm >0) then
		 	aAssociato := true;
		 else
		 	aAssociato := false;
		 end if;

	end ObbligazioneAssDocAmm;


	-- Questa funzione ritorna la prima scadenza di obbligazione non associata a
	-- documenti amministrativi,
	-- nel caso in cui non trova scadenze associate all'obbligazione passatagli,
	-- ristorna una scadenza nuova per l'obbligazione in esame.
	function GetScadResPas(aResiduo obbligazione%rowtype) return obbligazione_Scadenzario%rowtype is
	lScadenzaLibera obbligazione_Scadenzario%rowtype;
	lPgScadenza number;
	begin
		 select *
		 into lScadenzaLibera
		 from obbligazione_scadenzario
		 where cd_cds          = aResiduo.cd_cds
		 and   esercizio       = aResiduo.esercizio
		 and   esercizio_originale = aResiduo.esercizio_originale
		 and   pg_obbligazione = aResiduo.pg_obbligazione
		 for update nowait;

		 return lScadenzaLibera;
    exception when no_Data_found then
		-- creazione di una scadenza nuova	Libera
		select nvl(max(pg_obbligazione_scadenzario),0) +1
		into lPgScadenza
		from obbligazione_scadenzario
		where cd_cds    = aResiduo.cd_cds
		and   esercizio = aResiduo.esercizio
 	        and   esercizio_originale = aResiduo.esercizio_originale
		and   pg_obbligazione = aResiduo.pg_obbligazione;

	    lScadenzaLibera.CD_CDS					   	  := aResiduo.cd_Cds;
	    lScadenzaLibera.ESERCIZIO				   	  := aResiduo.esercizio;
	    lScadenzaLibera.ESERCIZIO_ORIGINALE			   	  := aResiduo.esercizio_originale;
	    lScadenzaLibera.PG_OBBLIGAZIONE			   	  := aResiduo.pg_obbligazione;
	    lScadenzaLibera.PG_OBBLIGAZIONE_SCADENZARIO   := lPgScadenza;
	    lScadenzaLibera.DT_SCADENZA 				  := trunc(to_date('3112'||gcEsercizioDest,'ddmmyyyy')) ; -- chiedere
	    lScadenzaLibera.DS_SCADENZA					  := 'Scadenza riporto';
	    lScadenzaLibera.IM_SCADENZA					  := 0;
	    lScadenzaLibera.IM_ASSOCIATO_DOC_AMM		  := 0;
	    lScadenzaLibera.IM_ASSOCIATO_DOC_CONTABILE    := 0;
	    lScadenzaLibera.DACR 					   	  := aResiduo.dacr;
	    lScadenzaLibera.UTCR					   	  := aResiduo.utcr;
	   	lScadenzaLibera.DUVA 					   	  := aResiduo.dacr;
	    lScadenzaLibera.UTUV 					   	  := aResiduo.utcr;
	    lScadenzaLibera.PG_VER_REC				   	  := 0;

--		cnrctb035.INS_OBBLIGAZIONE_SCADENZARIO (lScadenzaLibera);
		return lScadenzaLibera;
	end;


	function GetScadResPasVoce (aScad obbligazione_scadenzario%rowtype, aEsercizioObbPrincipale esercizio.ESERCIZIO%type) return obbligazione_scad_voce%rowtype is
	lScadenzaVoce obbligazione_scad_voce%rowtype;
	lCdVoce obbligazione_scad_voce.CD_VOCE%type;
  	aCdCdr varchar2(30);
  	aCdLa varchar2(10);
	begin
		select *
		into lScadenzaVoce
		from obbligazione_scad_voce asv
		where asv.CD_CDS = aScad.cd_cds
		and   asv.ESERCIZIO = aScad.esercizio
		and   asv.ESERCIZIO_ORIGINALE = aScad.esercizio_originale
		and   asv.PG_OBBLIGAZIONE = aScad.pg_obbligazione
		and   asv.pg_obbligazione_scadenzario = aScad.pg_obbligazione_scadenzario
		for update nowait;

		return lScadenzaVoce;
	exception when no_data_found then
	    -- configurare la linea attivita in configurazione CNR
		aCdCdr  :=CNRCTB015.GETVAL01PERCHIAVE(CNRCTB035.LA_SPECIALE,CNRCTB035.LA_ENTRATA_ENTE);
		aCdLa   :=CNRCTB015.GETVAL02PERCHIAVE(CNRCTB035.LA_SPECIALE,CNRCTB035.LA_ENTRATA_ENTE);
		-- per individuare la voce_f si fa riferimento
		-- all'obbligazione scadenza vece dell'esercizio precedente
		select distinct cd_voce
		into lCdVoce
		from obbligazione_scad_voce
		where CD_CDS = aScad.cd_Cds
		and   esercizio =aEsercizioObbPrincipale
		and   esercizio_originale = aScad.esercizio_originale
		and   pg_obbligazione = aScad.pg_obbligazione
		and   pg_obbligazione_scadenzario = aScad.pg_obbligazione_scadenzario
		and   ti_appartenenza = 'C'
		and   ti_gestione = 'S';

	    lScadenzaVoce.CD_CDS                       := aScad.cd_cds;
	    lScadenzaVoce.ESERCIZIO					   := aScad.esercizio;
	    lScadenzaVoce.ESERCIZIO_ORIGINALE			   := aScad.esercizio_originale;
	    lScadenzaVoce.PG_OBBLIGAZIONE			   := aScad.pg_obbligazione;
	    lScadenzaVoce.PG_obbligazione_scadenzario  := aScad.pg_obbligazione_Scadenzario;
		lScadenzaVoce.CD_CENTRO_RESPONSABILITA	   := aCdCdr; -- chiedere
		lScadenzaVoce.CD_LINEA_ATTIVITA			   := aCdLa; -- chiedere
		lScadenzaVoce.TI_APPARTENENZA			   := 'C';
		lScadenzaVoce.TI_GESTIONE			   	   := 'S';
--		bisogna recuperare la voce
		lScadenzaVoce.CD_VOCE			   	   	   := lCdVoce;
		lScadenzaVoce.IM_VOCE					   := 0;
		lScadenzaVoce.CD_FONDO_RICERCA			   := NULL;
		lScadenzaVoce.DACR						   := aScad.dacr;
		lScadenzaVoce.UTCR						   := aScad.utcr;
		lScadenzaVoce.DUVA						   := aScad.dacr;
		lScadenzaVoce.UTUV						   := aScad.utcr;
		lScadenzaVoce.PG_VER_REC				   := 0;

--		cnrctb035.INS_OBBLIGAZIONE_SCAD_VOCE(lScadenzaVoce);

		return lScadenzaVoce;

	end;

	procedure SetResiduiAttiviCanc(aData date ) is
	lResiduiSci cnr_residui_Attivi%rowtype;
--	lResiduoCir accertamento%rowtype;
	lAccertamento accertamento%rowtype;
	lNumResCancellati number := 0;
	contatore number :=0;
	lGiaAssociato boolean;
	lImAccertamento number(15,2);
	lPgUltimoCaricamento number;

	begin
		 -- Recuperiamo il pg_ultimo_caricamento SCI
		 select nvl(max(pg_caricamento),0)
		 into lPgUltimoCaricamento
		 from cnr_residui_attivi;

		 for lResiduoCir in (SELECT * FROM ACCERTAMENTO
		 	 	  	  WHERE length(to_char(pg_accertamento)) = 10
					  and cd_tipo_documento_cont = cnrctb018.TI_DOC_ACC_RES
					  and riportato ='N'
					  and esercizio = gcEsercizioDest
					  and dt_cancellazione is null
					  for update nowait ) loop
			 begin
			 	 select *
				 into lResiduiSci
				 from cnr_residui_Attivi
				 where cd_cds                 = lResiduoCir.cd_cds
				 and   esercizio 	  		  = to_number(substr(to_char(lResiduoCir.pg_accertamento),1,4))
				 and   cd_unita_organizzativa = lResiduoCir.cd_unita_organizzativa
				 and   cd_residuo 			  = lResiduoCir.pg_accertamento_origine
				 and   pg_caricamento 		  = lPgUltimoCaricamento;

			 exception when no_data_found then
			 		   -- Cancellare Residuo 2003 associato
					   lNumResCancellati := lNumResCancellati + 1;

					   lGiaAssociato := ResiduoAttAssociato(lResiduoCir);

					   if not lGiaAssociato then

							 --Aggiorniamo residuo 2003
						 	 lImAccertamento := lResiduoCir.im_accertamento;

							 begin
								 delete accertamento
								 where cd_cds          = lResiduoCir.cd_cds
								 and   esercizio       = lResiduoCir.esercizio
                                 		                 and   esercizio_originale = lResiduoCir.esercizio_originale
								 and   pg_Accertamento = lResiduoCir.pg_accertamento;

								 delete accertamento_s
								 where cd_cds          = lResiduoCir.cd_cds
								 and   esercizio       = lResiduoCir.esercizio
                                 		                 and   esercizio_originale = lResiduoCir.esercizio_originale
								 and   pg_Accertamento = lResiduoCir.pg_accertamento;

								 delete accertamento_scadenzario_s
								 where cd_cds          = lResiduoCir.cd_cds
								 and   esercizio       = lResiduoCir.esercizio
                                 		                 and   esercizio_originale = lResiduoCir.esercizio_originale
								 and   pg_Accertamento = lResiduoCir.pg_accertamento;

								 delete accertamento_scad_voce_s
								 where cd_cds          = lResiduoCir.cd_cds
								 and   esercizio       = lResiduoCir.esercizio
                                 		                 and   esercizio_originale = lResiduoCir.esercizio_originale
								 and   pg_Accertamento = lResiduoCir.pg_accertamento;

								 --Aggiorniamo accertamento 2002
							 	 select *
								 into lAccertamento
								 from accertamento
								 where  esercizio       = lResiduoCir.esercizio_ori_riporto
								 and 	cd_cds          = lResiduoCir.cd_cds_ori_riporto
                                 		                 and    esercizio_originale = lResiduoCir.esercizio_ori_ori_riporto
								 and 	pg_accertamento = lResiduoCir.pg_accertamento_ori_riporto
								 for update nowait;

								 delete accertamento
								 where esercizio     = lResiduoCir.esercizio_ori_riporto
								 and cd_cds 		 = lResiduoCir.cd_cds_ori_riporto
                                 		                 and    esercizio_originale = lResiduoCir.esercizio_ori_ori_riporto
								 and pg_accertamento = lResiduoCir.pg_accertamento_ori_riporto;

								 delete accertamento_s
								 where esercizio     = lResiduoCir.esercizio_ori_riporto
								 and cd_cds 		 = lResiduoCir.cd_cds_ori_riporto
                                		                 and    esercizio_originale = lResiduoCir.esercizio_ori_ori_riporto
								 and pg_accertamento = lResiduoCir.pg_accertamento_ori_riporto;

								 delete accertamento_scadenzario_s
								 where esercizio     = lResiduoCir.esercizio_ori_riporto
								 and cd_cds 		 = lResiduoCir.cd_cds_ori_riporto
                                		                 and    esercizio_originale = lResiduoCir.esercizio_ori_ori_riporto
								 and pg_accertamento = lResiduoCir.pg_accertamento_ori_riporto;

								 delete accertamento_scad_voce_s
								 where esercizio     = lResiduoCir.esercizio_ori_riporto
								 and cd_cds 		 = lResiduoCir.cd_cds_ori_riporto
                                		                 and    esercizio_originale = lResiduoCir.esercizio_ori_ori_riporto
								 and pg_accertamento = lResiduoCir.pg_accertamento_ori_riporto;
							 exception
							 when others then
							 	  -- esiste un documento amministrativo associato
								  -- violazioen della FK
								  if sqlcode = -2292 then
								  	 -- annulliamo logicamente accertamento 2003
								  	 update accertamento
									 set dt_cancellazione = aData,
									 	 utuv = cgUtente,
										 duva = aData,
									 	 pg_ver_rec = pg_ver_rec + 1
									 where  cd_cds          = lResiduoCir.cd_cds
									 and 	esercizio       = lResiduoCir.esercizio
                                              		                 and    esercizio_originale = lResiduoCir.esercizio_originale
									 and 	pg_accertamento = lResiduoCir.pg_accertamento
									 and    dt_cancellazione is null;
								  	 -- annulliamo logicamente accertamento 2002
								  	 update accertamento
									 set dt_cancellazione = aData,
									 	 utuv = cgUtente,
										 duva = aData,
									 	 pg_ver_rec = pg_ver_rec + 1
								 	 where esercizio     = lResiduoCir.esercizio_ori_riporto
								 	 and cd_cds 		 = lResiduoCir.cd_cds_ori_riporto
                                        		                 and    esercizio_originale = lResiduoCir.esercizio_ori_ori_riporto
								 	 and pg_accertamento = lResiduoCir.pg_accertamento_ori_riporto
									 and    dt_cancellazione is null;
								  end if;
							 end;

							 -- Aggiornare voce_f_saldi_cmp
							 update voce_f_saldi_cmp
							 set  im_obblig_imp_acr = im_obblig_imp_acr - lImAccertamento,
		      				 	  IM_STANZ_INIZIALE_A1 = im_obblig_imp_acr - lImAccertamento,
								  utuv = cgUtente,
								  duva = aData,
								  pg_ver_rec = pg_ver_rec + 1
							 where esercizio = 			 lResiduoCir.esercizio
							 and cd_cds = 	 			 lResiduoCir.cd_cds
							 and ti_appartenenza = 		 lResiduoCir.ti_appartenenza
							 and ti_gestione = 	 		 lResiduoCir.ti_gestione
							 and cd_voce = 	 			 lResiduoCir.cd_voce
							 and ti_competenza_residuo = CNRCTB054.TI_RESIDUI;
					   else
						     gMsgLog := 'Impossibile cancellare Accertamento, gia associato pg_accertamento = ' || lResiduoCir.pg_accertamento || ' ';
				 		     ibmutl200.logInf(gPgLog ,gMsgLog, '', '');
					   end if;

			 end;
		 end loop;
	end;

	procedure SetResiduiPassiviCanc(aData date) is
	lResiduiSci 		 cnr_residui_passivi%rowtype;
	lImpegno 		 	 obbligazione%rowtype;
	lImpegnoScadenza 	 obbligazione_scadenzario%rowtype;
	lVoce 			 	 obbligazione_scad_voce%rowtype;
	lVoceF 				 voce_F%rowtype;
	lPgObbligazioneScad  obbligazione_scadenzario.PG_OBBLIGAZIONE_SCADENZARIO%type;
	lNumResCancellati 	 number := 0;
	lGiaAssociato 		 boolean;
	lImObbligazione 	 number(15,2);
	lPgUltimoCaricamento number;

	begin
		 -- Recuperiamo il pg_ultimo_caricamento SCI
		 select nvl(max(pg_caricamento),0)
		 into lPgUltimoCaricamento
		 from cnr_residui_passivi;

		 -- Apriamo un cursore su tutti gli impegni residui presenti in CIR
		 for lImpegnoResiduo in (SELECT * FROM obbligazione
		 	 	  	  	 	 	WHERE length(to_char(pg_obbligazione)) = 10
							 	and cd_tipo_documento_cont 			= cnrctb018.TI_DOC_IMP_RES
					  		 	and riportato 							= 'N'
					  		 	and esercizio 							= gcEsercizioDest
								and stato_obbligazione <> 'S'
					  		 	) loop
			 begin
savepoint canc_altro_imp;
			     -- Recuperiamo la voce finanziaria (cd_voce da voce_f)
			     --	recuperare cd_voce da elemnto voce

			 	  -- Recuperiamo la scadenza legata all'imegno
			      gMsgLog := 'Impossibile recuperare (non essistono scadenze di obbligazione) la voce finanziaria per impegno pg_obbligazione = '|| to_char(lImpegnoResiduo.pg_obbligazione);
			 	  select *
				  into lImpegnoScadenza
				  from obbligazione_scadenzario
				  where cd_cds          = lImpegnoResiduo.cd_cds
				  and   esercizio 		= lImpegnoResiduo.esercizio
				  and   esercizio_originale 		= lImpegnoResiduo.esercizio_originale
				  and   pg_obbligazione = lImpegnoResiduo.pg_obbligazione;


			 	 -- Recuperiamo il codice della scadenza voce
			     gMsgLog := 'Impossibile recuperare la voce finanziaria (non essistono scadenze di obbligazione distribuite sulle voci f), pg_obbligazione = '|| to_char(lImpegnoResiduo.pg_obbligazione);
			 	 select *
				 into 	lVoce
				 from obbligazione_scad_voce obbsv
				 where obbsv.cd_cds                      = lImpegnoResiduo.cd_cds
				 and   obbsv.esercizio 					 = lImpegnoResiduo.esercizio
				 And   obbsv.esercizio_originale 		= lImpegnoResiduo.esercizio_originale
				 and   obbsv.pg_obbligazione 			 = lImpegnoResiduo.pg_obbligazione
				 and   obbsv.pg_obbligazione_scadenzario = lImpegnoScadenza.pg_obbligazione_scadenzario
				 and   obbsv.ti_appartenenza 			 = lImpegnoResiduo.ti_appartenenza
				 and   obbsv.ti_gestione				 = lImpegnoResiduo.ti_gestione;

				 gMsgLog := 'Non trovata Voce Finanziaria, per impegno = ' || to_char(lImpegnoResiduo.pg_obbligazione);
				 select *
		  		 into lVoceF
				 from voce_f
				 where esercizio       = lImpegnoResiduo.esercizio
				 and   TI_APPARTENENZA = lImpegnoResiduo.TI_APPARTENENZA
				 and   TI_GESTIONE 	   = lImpegnoResiduo.TI_GESTIONE
				 and   cd_voce 		   = lVoce.cd_voce;

				 begin
					 	 select *
						 into lResiduiSci
						 from cnr_residui_passivi cnr
						 where pg_caricamento  = lPgUltimoCaricamento
						 and   esercizio       = substr(to_char(lImpegnoResiduo.pg_obbligazione),1,4)
						 and   ti_appartenenza = lImpegnoResiduo.ti_appartenenza
						 and   ti_gestione 	   = lImpegnoResiduo.ti_gestione
						 and   cd_voce 	   	   = lVoceF.cd_voce ;
				 exception when no_data_found then
				 		   -- Cancellare Residuo 2003 associato
						   lNumResCancellati := lNumResCancellati + 1;
						   -- controlliamo se l'impegno risulta associato a doc amministrativi
						   if lImpegnoScadenza.im_associato_doc_amm > 0 then
						   	  lGiaAssociato := true;
						   else
						   	  lGiaAssociato := false;
						   end if;

						   if not lGiaAssociato then
							 	 -- In questo caso il residuo risulta cancellato in SCI
								 -- ed e possibile cancellarlo anche da CIR
								 -- in quanto non risulta associato a doc amm

								 -- Cancelliamo imegno residuo 2003
							 	 lImObbligazione := lImpegnoResiduo.im_obbligazione;

								 begin
									 delete obbligazione
									 where cd_cds          = lImpegnoResiduo.cd_cds
									 and   esercizio  	   = lImpegnoResiduo.esercizio
									 and   esercizio_originale = lImpegnoResiduo.esercizio_originale
									 and   pg_obbligazione = lImpegnoResiduo.pg_obbligazione;

									 delete obbligazione_s
									 where cd_cds          = lImpegnoResiduo.cd_cds
									 and   esercizio  	   = lImpegnoResiduo.esercizio
									 and   esercizio_originale = lImpegnoResiduo.esercizio_originale
									 and   pg_obbligazione = lImpegnoResiduo.pg_obbligazione;

									 delete obbligazione_scadenzario_s
									 where cd_cds          = lImpegnoResiduo.cd_cds
									 and   esercizio  	   = lImpegnoResiduo.esercizio
									 and   esercizio_originale = lImpegnoResiduo.esercizio_originale
									 and   pg_obbligazione = lImpegnoResiduo.pg_obbligazione;

									 delete obbligazione_scad_voce_s
									 where cd_cds          = lImpegnoResiduo.cd_cds
									 and   esercizio  	   = lImpegnoResiduo.esercizio
									 and   esercizio_originale = lImpegnoResiduo.esercizio_originale
									 and   pg_obbligazione = lImpegnoResiduo.pg_obbligazione;

									 -- recuperiamo imegno originario 2002
								 	 select *
									 into lImpegno
									 from obbligazione
									 where  cd_cds          = lImpegnoResiduo.cd_cds
									 and 	esercizio       = gcEsercizio
									 and    esercizio_originale = lImpegnoResiduo.esercizio_originale
									 and 	pg_obbligazione = lImpegnoResiduo.pg_obbligazione
									 for update nowait;

									 delete obbligazione
									 where  cd_cds          = lImpegno.cd_cds
									 and 	esercizio       = gcEsercizio
									 and    esercizio_originale = lImpegno.esercizio_originale
									 and 	pg_obbligazione = lImpegno.pg_obbligazione;

									 delete obbligazione_s
									 where  cd_cds          = lImpegno.cd_cds
									 and 	esercizio       = gcEsercizio
									 and    esercizio_originale = lImpegno.esercizio_originale
									 and 	pg_obbligazione = lImpegno.pg_obbligazione;

									 delete obbligazione_scadenzario_s
									 where  cd_cds          = lImpegno.cd_cds
									 and 	esercizio       = gcEsercizio
									 and    esercizio_originale = lImpegno.esercizio_originale
									 and 	pg_obbligazione = lImpegno.pg_obbligazione;

									 delete obbligazione_scad_voce_s
									 where  cd_cds          = lImpegno.cd_cds
									 and 	esercizio       = gcEsercizio
									 and    esercizio_originale = lImpegno.esercizio_originale
									 and 	pg_obbligazione = lImpegno.pg_obbligazione;
								 exception
								 when others then
								 	  -- esiste un documento amministrativo associato
									  -- violazioen della FK
									  if sqlcode = -2292 then
									  	 -- annulliamo logicamente obbligazione 2003
									  	 update obbligazione
										 set dt_cancellazione = aData,
										 	 stato_obbligazione = 'S', -- STORNATO
											 pg_ver_rec = pg_ver_rec + 1
										 where  cd_cds          = lImpegnoResiduo.cd_cds
										 and 	esercizio       = lImpegnoResiduo.esercizio
									         and    esercizio_originale = lImpegnoResiduo.esercizio_originale
										 and 	pg_obbligazione = lImpegnoResiduo.pg_obbligazione
										 and    stato_obbligazione  <> 'S';
									  	 -- annulliamo logicamente obbligazione 2002
									  	 update obbligazione
										 set dt_cancellazione = aData,
										 	 stato_obbligazione = 'S', -- STORNATO
											 utuv 				= cgUtente,
											 duva 				=aData,
											 pg_ver_rec = pg_ver_rec + 1
									 	 where  cd_cds          = lImpegnoResiduo.cd_cds
									 	 and 	esercizio       = gcEsercizio
		        							 and    esercizio_originale = lImpegnoResiduo.esercizio_originale
									 	 and 	pg_obbligazione = lImpegnoResiduo.pg_obbligazione
										 and    stato_obbligazione  <> 'S';
									  end if;
								 end;
								 -- Aggiornare voce_f_saldi_cmp per il 2003
								 update voce_f_saldi_cmp
								 set  im_obblig_imp_acr = im_obblig_imp_acr - lImObbligazione,
		      				 	  	  IM_STANZ_INIZIALE_A1 = im_obblig_imp_acr - lImObbligazione,
									  utuv		 = cgUtente,
									  duva		 = aData,
									  pg_ver_rec = pg_ver_rec + 1
								 where esercizio           = lImpegnoResiduo.esercizio
							     and cd_cds 	     	   = lImpegnoResiduo.cd_cds
								 and ti_appartenenza 	   = lImpegnoResiduo.ti_appartenenza
								 and ti_gestione 	  	   = lImpegnoResiduo.ti_gestione
								 and cd_voce 		       = lVocef.cd_voce
								 and ti_competenza_residuo = CNRCTB054.TI_RESIDUI;
						   else
						         gMsgLog := 'Non cancellato Impegno residuo, pg_obbligazione ' || to_char(lImpegnoResiduo.pg_obbligazione) || ' risulta associato a doc amm' ;
				 		         ibmutl200.logInf(gPgLog ,gMsgLog, '', '');
						   end if;
				 end;
			 exception when no_data_found then
			 		   ibmutl200.logInf(gPgLog ,gMsgLog, '', '');
			 end;
		 end loop;

	end;

	function getTerzo(aCdTerzoSci cnr_residui_Attivi.cd_terzo%type) return terzo%rowtype is
	lCdTerzo number;
	lTerzo terzo%rowtype;
	lTerzoDefault terzo%rowtype;
	begin
		 begin
			 select im01
			 into lCdTerzo
			 from configurazione_cnr
			 where cd_chiave_primaria = 'TERZO_SPECIALE'
			 and cd_chiave_secondaria = 'CODICE_DIVERSI_IMPEGNI';
		 exception when no_data_found then
			       gMsgLog := 'TERZO_SPECIALE per CODICE_DIVERSI_IMPEGNI non trovalo in configurazione_cnr';
	 		       ibmutl200.logInf(gPgLog ,gMsgLog, '', '');
		 end;


		 begin
		 	 select *
			 into lTerzoDefault
			 from terzo
			 where cd_terzo = lCdTerzo ;
		 exception when no_data_found then
			       gMsgLog := 'TERZO_SPECIALE per CODICE_DIVERSI_IMPEGNI non trovalo in configurazione_cnr, il codice del terzo in esame risulta ' || to_char(lCdTerzo);
	 		       ibmutl200.logInf(gPgLog ,gMsgLog, '', '');
--		 		   ibmerr001.RAISE_ERR_GENERICO('TERZO_SPECIALE per CODICE_DIVERSI_IMPEGNI non trovalo in configurazione_cnr, il codice del terzo in esame risulta ' || to_char(lCdTerzo) );
		 end;

		 if aCdTerzoSci <> to_char(lCdTerzo) then
		 	begin
			 	select *
				into lTerzo
				from terzo
				where cd_precedente = aCdTerzoSci
				and rownum = 1;

				return lTerzo ;

		 	exception when no_data_found then
					  return lTerzoDefault ;
			end;
		 else
			 return lTerzoDefault ;
		 end if;
	end;


END;


