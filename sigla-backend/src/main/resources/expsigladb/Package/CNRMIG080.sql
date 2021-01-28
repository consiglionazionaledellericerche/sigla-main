--------------------------------------------------------
--  DDL for Package CNRMIG080
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRMIG080" as
--
-- CNRMIG080 - Package di migrazione delle anagrafiche dei dipendenti
--
-- Date: 04/04/2011
-- Version: 2.16
--
-- Dependency:
--
-- History:
--
-- Date: 08/10/2002
-- Version: 1.0
-- Creazione
--
-- Date: 24/10/2002
-- Version: 1.1
-- Introdotta gestione riporto e inquadramento
--
-- Date: 25/10/2002
-- Version: 1.2
-- Modificata gestione per aggiornamento riporto e inquadramento
--
-- Date: 29/10/2002
-- Version: 1.3
-- Inserito aggiornamento del voce_f_saldo_cmp.
--
-- Date: 31/10/2002
-- Version: 1.4
-- Corretta la chiamata alle procedure UPD_ .
--
-- Date: 04/11/2002
-- Version: 1.5
-- Corretta la modalita di inserimento della Banca.
--
-- Date: 06/11/2002
-- Version: 1.6
-- Corretta la modalita di modifica della Banca.
--
-- Date: 06/11/2002
-- Version: 1.7
-- Corretta la modalita di creazione della Banca.
--
-- Date: 19/11/2002
-- Version: 1.8
-- inseriti i campi CD_TERZO_DELEGATO, PG_BANCA_DELEGATO, ORIGINE, FL_CC_CDS
-- per la tabella BANCA e aggiunto la funzione creaModPag
--
-- Date: 20/11/2002
-- Version: 1.9
-- Eliminata variabile globale gAttivatoLog
--
-- Date: 03/12/2002
-- Version: 1.10
-- Corretta modalita di inserimento delle modalita di pagamento
--
-- Date: 04/12/2002
-- Version: 1.11
-- Correzioni sui Log.
--
-- Date: 09/12/2002
-- Version: 1.12
-- Correzioni sui Log di testata.
--
-- Date: 07/01/2002
-- Version: 1.13
-- Modificata la procedura di estrazione del comune di nascita:
-- per i dipendenti non italiani ora si segue il seguente algoritmo:
-- se a parita di codice catastale relativo al comune/nazione di nascita esistono :
-- 0 Nazioni,  allora la funzione di estrazione del comune di nascita restituisce
-- 	 		   step a) la Nazione e il Comune di default
-- 1 Nazioni,  allora la funzione di estrazione del comune di nascita restituisce
-- 	 		 		step a) la Nazione per cui
-- 						 	cd_catastale = cnr_anadip.TAB_COD_COMNAS
--					step b) come comune quello che recupera nella tabella comune per cui:
--						 	pg_nazione = (pg nazione step a) e
--							ds_comune = cnr_anadip.COMUNE_NASC
-- >1 Nazioni, allora la funzione di estrazione del comune di nascita restituisce
-- 	 		 		step a) tra le Nazioni quella Nazione per cui risulta
--						 	cd_catastale = cnr_anadip.TAB_COD_COMNAS e
--							ds_nazione = cnr_anadip.COMUNE_NASC
--					step b) come comune quello che recupera nella tabella comune per cui :
--						    pg_nazione=(pg nazione step a) e
--							ds_comune = cnr_anadip.COMUNE_NASC
--
-- Date: 08/01/2002
-- Version: 1.14
-- Corretto conteggio Nazioni in GetComuneNascita/Fiscale
--
-- Date: 24/01/2002
-- Version: 1.15
-- Modificato inserimento e aggiornamento dell'inquadramento
--
-- Date: 24/01/2002
-- Version: 1.16
-- Modificato inserimento e aggiornamento del rapporto
--
-- Date: 27/01/2002
-- Version: 1.17
-- Modifica 2 per inserimento e aggiornamento del rapporto e inquadramento
--
-- Date: 30/01/2002
-- Version: 1.18
-- Modifica 3 per inserimento e aggiornamento del rapporto e inquadramento
--
-- Date: 30/01/2002
-- Version: 1.19
-- Modifica 4 per inserimento e aggiornamento del rapporto e inquadramento
--
-- Date: 30/01/2002
-- Version: 1.20
-- Modifica 5 per inserimento e aggiornamento del rapporto e inquadramento
--
-- Date: 19/02/2003
-- Version: 1.21
-- Modificata intestazione banca da 'BANCA DA PROCEDURA STIPENDI' a 'INTESTAZIONE AL BENEFICIARIO'
-- Inserita valorizzazione MONTANTI
-- il campo IMP_FISC della tabella CNR_ANADIP è inserito nel campo IRPEF_LORDO_DIPENDENTI della tabella MONTANTI
-- tramite la seguente regola MONTANTI.IRPEF_LORDO_DIPENDENTI = CNR_ANADIP.IMP_FISC * 13 / CNR_ANADIP.MESE_RIF
--
-- Date: 25/02/2003
-- Version: 1.22
-- Modificato aggiornamento del Montante in modo da non sovrascrivere sempre tutte
-- le informazioni ma solo quelle cambiate.
--
-- Date: 25/02/2003
-- Version: 1.23
-- Inseriti controlli sul mese di riferimento per il calcolo dei montanti
--
-- Date: 25/02/2003
-- Version: 1.24
-- Inserita nuova costante di decodifica dell'ente previdenziale cINPGI
--
-- Date: 26/02/2003
-- Version: 1.25
-- Modificata inserimento e modifica della banca
-- in modo da inserire le banche di ti_pagamento ='A'
--
-- Date: 26/02/2003
-- Version: 1.26
-- Modificata gestione creazione rapporto per nuovi dipendenti
--
-- Date: 27/02/2003
-- Version: 1.27
-- Modificata calcolo montanti DEDUZIONE_IRPEF,
--
-- Date: 11/03/2003
-- Version: 1.28
-- Modificata chiusura inquadramento in base alla chiusura del rapporto,
-- modificata data di cessazione dell'inquadramento da creare (nuovo) già chiuso
--
-- Date: 19/03/2003
-- Version: 1.29
-- Modificato upd_rapporto in modo da inserire un rapporto di tipo DIP
-- per tutti gli anagrafici
-- inseriti non da migrazione e con nessun rapporto di tipo DIP.
--
-- Date: 21/03/2003
-- Version: 2.0
-- Revisione struttura
--
-- Date: 24/03/2003
-- Version: 2.1
-- Modificato la procedura di upd_rapporto, quando si effettuava l'update
-- della tabella rapporto non veniva aggiornata la variabile aRapporto
--
-- Date: 02/04/2003
-- Version: 2.2
-- Modificato upd_inquadramento, inserito controllo sull'inserimento del nuovo
-- inquadramento, controlliamo prima di inserire che non vi siano inquadramenti
-- precedenti che si sovrappongono.
--
-- Date: 17/04/2003
-- Version: 2.3
-- Inserito log quando abi e cab, in CNR_ANADIP sono not null e non risultano
-- presenti nella tabella ABICAB
--
-- Date: 13/05/2003
-- Version: 2.4
-- Nel calcolo montanti è stata inserita la vista v_stipendi_montanti
--
-- Date: 15/05/2003
-- Version: 2.5
-- Inserita procedura di trattamento manuale della modifice di rapporto e inquadramento
-- la procedura processa in modo separato tutti i dipendenti che hanno il flag fl_gestione_manuale
-- impostato ad Y.
--
-- Date: 26/05/2003
-- Version: 2.6
-- Eliminato accesso alla vista v_stipendi_montanti e inserito aggiornamento dei montanti
-- in coda la processo di migrazione
--
-- Date: 27/05/2003
-- Version: 2.7
-- Introdotto allineamento montanti in base alla vista v_stimendi_montanti
--
-- Date: 29/05/2003
-- Version: 2.8
-- Scambiati IMPO1 e FISC dei montanti
--
-- Date: 29/05/2003
-- Version: 2.9
-- Modificato aggiornamento montante con aggiunta di log
--
-- Date: 25/02/2005
-- Version: 2.10
-- Aggiunta la valorizzazione a zero nella tabella Montanti per i campi:INPS_OCCASIONALI e DEDUZIONE_FAMILY_ALTRI
-- che non sono gestiti dal Personale ma che devono essere valorizzati
--
-- Date: 30/07/2003
-- Version: 2.10
-- Modificato modifica montante gestione del tipo ente prev = cCPS,cINPGI,cENPDEP
--
-- Constants:
-- Decodifica del campo ENTE_PREV della tabella CNR_ANADIP
-- AA	Altra Amministrazione	?
-- FS	Fondo FF.SS. FONDO_FS_DIPENDENTI
-- 01	GESCAL		 Non Gestito
-- 02	S.S.N.		 Non Gestito
-- 03	ONAOSI		 Non Gestito
-- 04	ENPDEP		 - > INPS_TESORO_DIPENDENTI
-- 05	INPS		 - > INPS_DIPENDENTI
-- 06	CPDEL		 - > INPS_TESORO_DIPENDENTI
-- 07	C.SOLID.	 Non Gestito
-- 08	CPS			 - > INPS_TESORO_DIPENDENTI
-- 09	ENAOLI		 Non Gestito
-- 10	ENPI		 Non Gestito
-- 13	INPGI		 - > INPS_DIPENDENTI
--
--
-- Date: 16/01/2006
-- Version: 2.11
-- Aggiunto campo CD_ENTE_PREV_STI sulla tabella RAPPORTO, tale campo viene valorizzato con i dati di ENTE_PREV della tabella CNR_ANADIP
--
-- Date: 28/03/2006
-- Version: 2.12
-- Aggiunta procedura IMPOSTA_DATA_CESSAZIONE
-- Modificati parametri di input della procedura ALLINEA_MONTANTE
--
-- Date: 30/10/2007
-- Version: 2.13
-- Modificata in IMPOSTA_DATA_CESSAZIONE la dt_cessazione, viene impostata come sysdate
--
-- Date: 11/06/2008
-- Version: 2.14
-- Aggiunti i campi IBAN_PAG e CIN_PAG nel tracciato record e
-- sostituito nella tabella BANCA "Intestazione al Beneficiario" con Cognome e Nome del dipendente, NOMINATIVO di CNR_ANADIP.
--
-- Date: 02/09/2010
-- Version: 2.15
-- Modificata la procedura dei montanti ed eliminata Allinea_Montanti
--
-- Date: 04/04/2011
-- Version: 2.16
-- Modificata la procedura dei Modifica_Banca
--
--
-- Date: 17/05/2013
-- Version: 2.17
-- Il campo numero civico in aggiornamento viene impostato a null sia in ANAGRAFICO sia in TERZO
-- perchè è presente nell'indirizzo  aAnaDip.ind_resid e se questo cambia non viene aggiornato il numero civico
-- quindi può accadere che ci siano 2 diversi numeri civici.

cALTRAAMMINISTRAZIONE CONSTANT varchar2(2) := 'AA';
cFONDOFS CONSTANT varchar2(2) := 'FS';
cGESCAL  CONSTANT varchar2(2) := '01';
cSSN 	 CONSTANT varchar2(2) := '02';
cONAOSI  CONSTANT varchar2(2) := '03';
cENPDEP  CONSTANT varchar2(2) := '04';
cINPS 	 CONSTANT varchar2(2) := '05';
cCPDEL 	 CONSTANT varchar2(2) := '06';
cC_SOLID CONSTANT varchar2(2) := '07';
cCPS 	 CONSTANT varchar2(2) := '08';
cENAOLI  CONSTANT varchar2(2) := '09';
cENPI 	 CONSTANT varchar2(2) := '10';
cINPGI 	 CONSTANT varchar2(2) := '13';

cTIPO_RAPPORTO constant varchar2(10) := 'DIP';
gPgLog number;

TIPO_LOG_JOB_NSIP CONSTANT VARCHAR2(20) := 'REG_NSIP00';

-- Functions e Procedures:
--

 procedure IMPOSTA_DATA_CESSAZIONE (aAnnoRif Number, aMeseRif Number, aUtente varchar2);

 procedure caricaAnagDipendenti(aAnnoRif Number, aMeseRif Number, aUtente varchar2);

 procedure CREAANAGRAFICO(aAnaDip cnr_anadip%rowtype, aData Date, aUtente VARCHAR2);

 procedure CREATERZO(aAnaDip cnr_anadip%rowtype,aAnagrafico anagrafico%rowtype, aData Date, aUtente VARCHAR2);

 procedure CREAMODPAG(aAnaDip cnr_anadip%rowtype, aTerzo terzo%rowtype, aData date , aUtente varchar2);

 procedure CREABANCA(aAnaDip cnr_anadip%rowtype,aAnagrafico anagrafico%rowtype,aTerzo terzo%rowtype, aData Date, aUtente VARCHAR2);

 procedure MODIFICAANAGRAFICO(aAnaDip cnr_anadip%rowtype,lAnagraficoOld in out anagrafico%rowtype, aData Date, aUtente VARCHAR2);

 procedure MODIFICATERZO(aAnaDip cnr_anadip%rowtype,aAnagrafico anagrafico%rowtype, aData Date, aUtente VARCHAR2);

 procedure MODIFICABANCA(aAnaDip cnr_anadip%rowtype,aAnagrafico anagrafico%rowtype,aTerzo terzo%rowtype, aData Date, aUtente VARCHAR2);

 function chkPresenzaAnag(aAnaDip cnr_anadip%rowtype, aAnagrafico in out anagrafico%rowtype) return boolean;

 function getComuneNascita(aAnaDip cnr_anadip%rowtype,  aAnagrafico IN OUT anagrafico%rowtype, aPgNazioneNascita in out number) return number;

 function getComuneFiscale(aAnaDip cnr_anadip%rowtype,  aAnagrafico IN OUT anagrafico%rowtype, aPgNazioneFiscale in out number) return number;

 procedure INS_ANAGRAFICO(aAnagrafico anagrafico%rowtype);

 procedure INS_TERZO(aTerzo Terzo%rowtype);

 procedure INS_BANCA (aBanca banca%rowtype);

 procedure INS_MOD_PAGAMENTO (aModPagamento modalita_pagamento%rowtype,aAnaDip cnr_anadip%rowtype );

 procedure CREARAPPORTO (aAnaDip cnr_Anadip%rowtype
 		   				 ,aAnagrafico anagrafico%rowtype
						 ,aRapporto in out rapporto%rowtype
						 ,aRapportoPrec in out rapporto%rowtype
						 ,aData date
						 ,aUtente varchar2);

 procedure INS_RAPPORTO (aRapporto rapporto%rowtype);

 procedure CREAINQUADRAMENTO (aAnaDip cnr_Anadip%rowtype
 		   					 ,aAnagrafico anagrafico%rowtype
							 ,aRapporto rapporto%rowtype
							 ,aRapportoPrec rapporto%rowtype
							 ,aData date
							 ,aUtente varchar2);

 procedure UPD_ANAGRAFICO(aAnagrafico anagrafico%rowtype) ;

 procedure UPD_TERZO(aTerzo Terzo%rowtype);

 procedure ALLINEA_INQUADRAMENTO(aAnaDip cnr_anadip%rowtype,aAnagrafico anagrafico%rowtype,aData date,aUtente varchar2);
 --procedure CREARAPPORTO1980 (aAnaDip cnr_Anadip%rowtype,aAnagrafico anagrafico%rowtype, aRapportoPrec in out rapporto%rowtype, aData date, aUtente varchar2);

 procedure UPD_RAPPORTO (aAnaDip cnr_Anadip%rowtype,aAnagrafico anagrafico%rowtype,
 		   				 aRapporto in out rapporto%rowtype, aRapportoPrec in out rapporto%rowtype,
						 aData date, aUtente varchar2);

 procedure UPD_INQUADRAMENTO (aAnaDip cnr_Anadip%rowtype
 		   					 ,aAnagrafico anagrafico%rowtype
							 ,aRapporto rapporto%rowtype
							 ,aRapportoPrec rapporto%rowtype
							 ,aData date
							 ,aUtente varchar2);

 procedure CREAMONTANTE(aAnaDip cnr_Anadip%rowtype, aAnagrafico anagrafico%rowtype, aData date, aUtente varchar2 );

 procedure INS_INQUADRAMENTO (aInquadramento inquadramento%rowtype) ;

 procedure INS_MONTANTE(aMontante montanti%rowtype);

 procedure UPD_MONTANTE (aMontante montanti%rowtype);

 procedure JOB_AGGIORNA_COORD_BANCARIE(job number, pg_exec number, next_date date, nrGiorniBack number);

--Flavia -- Modificata procedura: aggiunti i parametri di input aAnnoRif, aMeseRif e aAnaDip
--procedure ALLINEA_MONTANTE(aData date, aUtente varchar2, aAnnoRif Number, aMeseRif Number, aAnaDip cnr_Anadip%rowtype);
end;
