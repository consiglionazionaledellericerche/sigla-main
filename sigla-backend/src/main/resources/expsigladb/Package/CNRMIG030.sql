--------------------------------------------------------
--  DDL for Package CNRMIG030
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRMIG030" is
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
-- la modifica dei campi sopra citati è scatenata se viene riscontrato un cambiamento
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
