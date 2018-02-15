--------------------------------------------------------
--  DDL for Package CNRCTB048
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRCTB048" as
--
-- CNRCTB048 - Package di servizio per chiusura contabile e ribaltamento interesercizio
--
-- Date: 22/01/2007
-- Version: 1.16
--
-- Dependency:
--
-- History:
--
-- Date: 25/06/2003
-- Version: 1.0
-- Creazione
--
-- Date: 27/06/2003
-- Version: 1.1
-- Fix getDocOrigine
--
-- Date: 07/07/2003
-- Version: 1.2
-- Corretti metodi per ribaltamento con cambio voce
--
-- Date: 17/07/2003
-- Version: 1.3
-- Introduzione funzione per calcolo pg_ver_rec per gestione
-- porta avanti/indietro di residui e compatibilità con storico
--
-- Date: 22/07/2003
-- Version: 1.4
-- Fix getPgVerRec
-- Fix getVoceF per CdS SAC
--
-- Date: 30/07/2003
-- Version: 1.5
-- Aggiunta metodi da CNRCTB046
--
-- Date: 31/07/2003
-- Version: 1.6
-- Fix a getVoceF con cambio imputazione finanziaria
-- Fix a checkEsercizio
--
-- Date: 06/08/2003
-- Version: 1.7
-- Aggiunta documentazione: pre-post condizioni
--
-- Date: 27/08/2003
-- Version: 1.8
-- Aggiunto controllo al riporto indientro: se esiste ordine nel nuovo esercizio
-- non è possibile deriportare
--
-- Date: 16/09/2003
-- Version: 1.9
-- UnComment controlli su stato coge/coan del doc amm in checkNoRiporta
-- per rilascio
--
-- Date: 12/01/2004
-- Version: 1.10
-- Fix nella determinazione del capitolo voce_f per il riporto in avanti
-- di obbligazione per spese di costi altrui della SAC
--
-- Date: 12/01/2004
-- Version: 1.11
-- Fix per il riporto in avanti con cambio di imputazione finanziaria
-- per obbligazioni SAC per spese di costi altrui
--
-- Date: 13/01/2004
-- Version: 1.12
-- Fix alla modifica precedente (non veniva considerato il nuovo elemento voce)
--
-- Date: 26/02/2004
-- Version: 1.13
-- Aggiunta metodi gestione ribaltamento doc cont ente: aggiornamento e non inserimento
-- nel caso di deriporto senza cancellazione fisica
--
-- Date: 11/03/2004
-- Version: 1.14
-- Fix ai metodi isDocModificato per attivare i controlli al deriporto di
-- partite di giro dell'ente - segnalazione errore n. 799
--
-- Date: 13/07/2006
-- Version: 1.15
-- Gestione Impegni/Accertamenti Residui:
-- aggiornata la funzione per tener conto anche del campo Esercizio Originale Impegno/Accertamento
--
-- Date: 22/01/2007
-- Version: 1.16
-- Funzione isDocModificato: inserito il controllo per verificare la presenza di Modifiche ai residui
--
-- Constants:
--
ERR_FK_VIOLATED EXCEPTION;

PRAGMA EXCEPTION_INIT(ERR_FK_VIOLATED,-2292);

-- Functions e Procedures:
--
Function getcdsribaltato (aEs NUMBER, aCDS VARCHAR2) Return VARCHAR2;
function getImNonRiscontrato(aCdCds varchar2, aEs number, aEsOri number, aPg number, aTiGestione varchar2) return number ;
function getImPagRiscNonRiscontrato(aCdCds varchar2, aEs number, aEsOri number, aPg number, aTiGestione varchar2) return number ;
function getImNonPagatoRiscosso(aCdCds varchar2, aEs number, aEsOri number, aPg number, aTiGestione varchar2) return number ;
function getImRiscontratoManRev(aCdCds varchar2, aEs number, aPg number, aTipo varchar2) return number ;
function getStatoRibaltabileScad(aEs number,aScad obbligazione_scadenzario%rowtype) return varchar2;
function getStatoRibaltabileScad(aEs number,aScad accertamento_scadenzario%rowtype) return varchar2;
function getPg(aObb obbligazione%rowtype, aEs number, aUser varchar2) return number;
function getPg(aAcc accertamento%rowtype, aEs number, aUser varchar2) return number;
function getDtScadenza(aEs number,aDt_scadenza date) return date;
function getLdA(aDettScad obbligazione_scad_voce%rowtype, aObb obbligazione%rowtype, aEs obbligazione.esercizio%type) return v_linea_attivita_valida%rowtype;
function getLdA(aDettScad accertamento_scad_voce%rowtype, aAcc accertamento%rowtype, aEs accertamento.esercizio%type) return v_linea_attivita_valida%rowtype;
function getVoceF(aObb obbligazione%rowtype,
		 		  aLdA v_linea_attivita_valida%rowtype,
				  aDettScad obbligazione_scad_voce%rowtype,
				  aEs number) return voce_f%rowtype;

function getVoceF(aObb obbligazione%rowtype,
		 		  aLdA v_linea_attivita_valida%rowtype,
				  aEV elemento_voce%rowtype,
				  aDettScad obbligazione_scad_voce%rowtype,
				  aEs number) return voce_f%rowtype;

procedure creaTestataObb(aObb obbligazione%rowtype, aObbNext in out obbligazione%rowtype, aEs number, aUser varchar2, aTSNow date);

procedure creaScadObb(aObbNext obbligazione%rowtype,
		  			  aScad obbligazione_scadenzario%rowtype,
					  aObbScadNext in out obbligazione_scadenzario%rowtype,
					  aEs number,
					  aUser varchar2,
					  aTSNow date);

procedure creaScadDettObb(aObbScadNext obbligazione_scadenzario%rowtype,
		  			   	  aDettScad obbligazione_scad_voce%rowtype,
					   	  aObbScadDettNext in out obbligazione_scad_voce%rowtype,
					   	  aUser varchar2,
					   	  aTSNow date);

procedure creaTestataAcc(aAcc accertamento%rowtype, aAccNext in out accertamento%rowtype, aEs number, aUser varchar2, aTSNow date);

procedure creaScadAcc(aAccNext accertamento%rowtype,
		  			  aScad accertamento_scadenzario%rowtype,
					  aAccScadNext in out accertamento_scadenzario%rowtype,
					  aEs number,
					  aUser varchar2,
					  aTSNow date);

procedure creaScadDettAcc(aAccScadNext accertamento_scadenzario%rowtype,
		  aDettScad accertamento_scad_voce%rowtype,
		  aAccDettScadNext in out accertamento_scad_voce%rowtype,
		  aUser varchar2,
		  aTSNow date);

-- Ritorna il documento corrispondente nel nuovo esercizio a quello specificato
 function getDocRiportato(aObb obbligazione%rowtype) return obbligazione%rowtype;
 function getDocRiportato(aAcc accertamento%rowtype) return accertamento%rowtype;

 function getDocOrigine(aObbNew obbligazione%rowtype) return obbligazione%rowtype;
 function getDocOrigine(aAccNew accertamento%rowtype) return accertamento%rowtype;

-- Ritorna la scadenza nel vecchio esercizio corrispondente a quella nel nuovo specificata
 function getOldScad(aObbNew obbligazione%rowtype, aObbScadNew obbligazione_scadenzario%rowtype) return obbligazione_scadenzario%rowtype;
 function getOldScad(aAccNew accertamento%rowtype, aAccScadNew accertamento_scadenzario%rowtype) return accertamento_scadenzario%rowtype;

function getPgVerRec(aObbNext obbligazione%rowtype) return number;

function getPgVerRec(aAccNext accertamento%rowtype) return number;

function isDocModificato(aObb obbligazione%rowtype,aObbNew obbligazione%rowtype) return char;

function isDocModificato(aAcc accertamento%rowtype,aAccNew accertamento%rowtype) return char;

-- checkEsercizio
-- --------------
-- pre-post: controlli stato dell'esercizio per il cds - esercizio non aperto
-- pre: l'esercizio contabile per il cds non è aperto
-- post: viene sollevato un errore
--
-- pre-post: controlli stato dell'esercizio per il cds - esercizio successivo non definito
-- pre: l'esercizio contabile successivo per il cds non è definito
-- post: viene sollevato un errore
--
-- pre-post: controlli stato dell'esercizio per il cds
-- pre: nessuna delle precedenti precondizioni
-- post: nn viene sollevato nessun errore
--
-- Parametri:
-- aEs -> Esercizio contabile
-- aCdCds -> Centro di Spesa
procedure checkEsercizio(aEs number, aCdCds varchar2);

-- checkDeRiportaEsNext
--
-- Controllo sulla validità del riporto dall'esercizio successivo di documento contabile
--
-- pre-post-name: annotazione su partita di giro non valida
-- pre: l'annotazione è su partita di giro, ma risultano più di un dettaglio o di una scadenza,
--	(v. CNRCTB035.GETPGIROCDS/CNRCTB035.GETPGIROCDSINV)
-- post: viene sollevata un'eccezione
--
-- pre-post-name: annotazione non riportata
-- pre: l'annotazione non risulta riportata, (flag RIPORTATO = 'N')
-- post: viene sollevata un'eccezione
--
-- pre-post-name: annotazione con variazoni formali nel nuovo esercizio
-- pre: l'annotazione ha avuto delle variaioni formali nel nuovo esercizio
-- post: viene sollevata un'eccezione
--
-- pre-post-name: documento contabile modificato nel nuovo esercizio
-- pre: il documento contabile è stato modificato nel nuovo esercizio, (v. CNRCTB048.isDocModificato)
--	il documento contabile risulta modificato se:
--	   - l'importo o lo stato dell'obbligazione è stato modificato, oppure il documento è stato annullato;
--	   - se al documento nel nuovo es. è stata aggiunta una scadenza, (la scadenza non ha ori riporto);
--	   - la dt_scadenza di una delle scadenze è cambiata;
--	   - è cambiata la testata di una scadenza.
--         - se al documento nel nuovo es. è stata aggiunta una modifica, (record in OBBLIGAZIONE_MODIFICA/ACCERTAMENTO_MODIFICA);
-- post: viene sollevata un'eccezione
--
-- pre-post-name: controllo sulla validità del riporto dall'esercizio successivo di documento contabile.
-- pre: Nessun'altra precondizione verificata
-- post: l'operazione di riporto dall'esercizio successivo deldocumento, continua normalmente.
--
-- Parametri:
-- aObb (aAcc) -> il documento nell'esercizio attuale (da deriportare)
-- aObbNext (aAccNext) -> il documento nell'esercizio successivo (riportato)
procedure checkDeRiportaEsNext(aObb in out obbligazione%rowtype, aObbNext obbligazione%rowtype);
procedure checkDeRiportaEsNext(aAcc in out accertamento%rowtype, aAccNext accertamento%rowtype);

-- checkDeRiportaScadEsNext
--
-- Controllo sulla validità del riporto dall'esercizio successivo delle scadenze di documento contabile
--
-- pre-post-name: documento contabile associato a lettera di pagamento estero
-- pre: il documento contabile, (obbligazione), risulta associato a lettera di pagamento estero emessa nel nuovo esercizio
-- post: viene sollevata un'eccezione
--
-- pre-post-name: documento contabile associato a documento amministrativo
-- pre: il documento contabile è associato a documenti amministrativi emessi nel nuovo esercizio
-- post: viene sollevata un'eccezione
--
-- pre-post-name: documento contabile associato a documento amministrativo pagato
-- pre: il documento contabile è associato a documenti amministrativi (in parte) pagato nel nuovo esercizio
-- post: viene sollevata un'eccezione
--
-- pre-post-name: documento contabile pagato nel nuovo esercizio
-- pre: il documento contabile risulta essere o essere stato (parzialmente) pagato nel nuovo esercizio
-- post: viene sollevata un'eccezione
--
-- pre-post-name: controllo sulla validità del riporto dall'esercizio successivo di scadenze di un documento contabile
-- pre: Nessun'altra precondizione verificata
-- post: l'operazione di riporto dall'esercizio successivo del documento, continua normalmente.
--
-- Parametri:
-- aObb (aAcc) -> il documento nell'esercizio attuale (da deriportare)
-- aObbNext (aAccNext) -> il documento nell'esercizio successivo (riportato)
-- aObbScad (aAccScad) -> la scadenza nell'esercizio attuale (da deriportare)
-- aObbSacdNext (aAccScadNext) -> la scadenza nell'esercizio successivo (riportato)
procedure checkDeRiportaScadEsNext(aObb obbligazione%rowtype, aObbScad obbligazione_scadenzario%rowtype, aObbNext obbligazione%rowtype, aObbScadNext obbligazione_scadenzario%rowtype);
procedure checkDeRiportaScadEsNext(aAcc accertamento%rowtype, aAccScad accertamento_scadenzario%rowtype, aAccNext accertamento%rowtype, aAccScadNext accertamento_scadenzario%rowtype);


procedure aggiornaDocAmm(aScad          obbligazione_scadenzario%rowtype,
  			 aObbScadNext   obbligazione_scadenzario%rowtype,
			 aUser          varchar2,
			 aTSNow         date);

procedure aggiornaDocAmm(aScad accertamento_scadenzario%rowtype,
  				 aAccScadNext accertamento_scadenzario%rowtype,
				 aUser varchar2,
				 aTSNow date);

procedure checkNoRiporta(aObb obbligazione%rowtype);


-- checkNoRiporta (accertamento)
-- --------------
--
-- pre-post: controlli per doc amm associati ad accertamento da riportare al nuovo esercizio -
--           accertamento gestito in automatico dalla liquidazione CORI
-- pre: l'accertamento è associato almeno ad un documento generico di versamento cori
-- post: viene sollevato un errore
--
-- pre-post: controlli per doc amm associati ad accertamento da riportare al nuovo esercizio -
--           doc amm da contabilizzare in economica
-- pre: l'accertamento è associato almeno ad un doc amm da contabilizzare in economica
-- post: viene sollevato un errore
--
-- pre-post: controlli per doc amm associati ad accertamento da riportare al nuovo esercizio -
--           doc amm da contabilizzare in analitica
-- pre: l'accertamento è associato almeno ad un doc amm da contabilizzare in analitica
-- post: viene sollevato un errore
--
-- pre-post: controlli per doc amm associati ad accertamento da riportare al nuovo esercizio -
--           accertamento associato a CORI di compensi
-- pre: l'accertamento risulta associato a CORI di compensi
-- post: viene sollevato un errore
--
-- pre-post: controlli per doc amm associati ad accertamento da riportare al nuovo esercizio -
--           accertamento parte di liquidazione CORI accentrata
-- pre: l'accertamento risulta parte di liuqidazione CORI accentrata
-- post: viene sollevato un errore
--
-- pre-post: controlli per doc amm associati ad accertamento da riportare al nuovo esercizio
-- pre: nessuna delle precedenti precondizioni
-- post: non viene sollevato nessun errore
--
-- Parametri:
-- aAcc -> rowtype dell'accertamento da ribaltare all'esercizio successivo
procedure checkNoRiporta(aAcc accertamento%rowtype);

-- checkRiportaEsNext (accertamento)
-- ------------------
--
-- pre-post name: controlli per accertamento da riportare all'esercizio successivo -
--                documento già riportato
-- pre: l'accertamento che deve essere riportato risulta già riportato
-- post: viene sollevato un errore
--
-- pre-post name: controlli per accertamento da riportare all'esercizio successivo -
--                documento ha importo nullo
-- pre: il documento non è su partita di giro, oppure apre la partita di giro, ed ha
--      importo nullo in testata
-- post: viene sollevato un errore
--
-- pre-post name: controlli per accertamento da riportare all'esercizio successivo -
--                documento non ribaltabile
-- pre: il documento contabile non è ribaltabile (vd. IsEligibileRibalt)
-- post: viene sollevato un errore
--
-- pre-post name: controlli per accertamento da riportare all'esercizio successivo -
--                documenti amministrativi associati non ribaltabili
-- pre: il documento contabile è associato a documenti amministrativi particolari per
--      cui non è ribaltabile (vd. pre-post checkNoRiporta() )
-- post: viene sollevato un errore
--
-- pre-post name: controlli per accertamento da riportare all'esercizio successivo -
--                terzo non valido
-- pre: il terzo associato al documento contabile ha un data di fine validità precedente
--      all'esercizio su cui si ribalta
-- post: viene sollevato un errore
--
-- pre-post name: controlli per accertamento da riportare all'esercizio successivo -
--                capitolo finanziario non valido sul nuovo esercizio
-- pre: deve essere controllata la validità della voce_f nel nuovo esercizio, non esiste
--      una voce_f corrispondente nel nuovo esercizio
-- post: viene sollevato un errore
--
-- pre-post name: controlli per accertamento da riportare all'esercizio successivo -
--                unità organizzativa non valida nel nuovo esercizio
-- pre: l'unità organizzativa del documento non è valida nel nuovo esercizio
-- post: viene sollevato un errore
--
-- pre-post name: controlli per accertamento da riportare all'esercizio successivo
-- pre: nessuna delle precedenti precondizioni
-- post: non viene sollevato nessun errore
--
-- Parametri:
-- aAcc -> rowtype dell'accertamento da ribaltare all'esercizio successivo
-- controlloRibalt -> boolean, doc cont che chiude la pgiro con origine di pgiro ribaltabile
--                    non deve necessariamente essere ribaltabile
-- controlloVoce -> boolean, se cambia imputazione finanziaria non è necessario verificare
--                  la validità del capitolo voce_f
procedure checkRiportaEsNext(aAcc accertamento%rowtype, controlloRibalt boolean, controlloVoce boolean);

-- checkRiportaEsNext (obbligazione)
-- ------------------
--
-- pre-post name: controlli per obbligazione da riportare all'esercizio successivo -
--                documento già riportato
-- pre: l'obbligazione che deve essere riportata risulta già riportata
-- post: viene sollevato un errore
--
-- pre-post name: controlli per obbligazione da riportare all'esercizio successivo -
--                documento ha importo nullo
-- pre: il documento non è su partita di giro, oppure apre la partita di giro, ed ha
--      importo nullo in testata
-- post: viene sollevato un errore
--
-- pre-post name: controlli per obbligazione da riportare all'esercizio successivo -
--                documento non ribaltabile
-- pre: il documento contabile non è ribaltabile (vd. IsEligibileRibalt)
-- post: viene sollevato un errore
--
-- pre-post name: controlli per obbligazione da riportare all'esercizio successivo -
--                documenti amministrativi associati non ribaltabili
-- pre: il documento contabile è associato a documenti amministrativi particolari per
--      cui non è ribaltabile (vd. pre-post checkNoRiporta() )
-- post: viene sollevato un errore
--
-- pre-post name: controlli per obbligazione da riportare all'esercizio successivo -
--                terzo non valido
-- pre: il terzo associato al documento contabile ha un data di fine validità precedente
--      all'esercizio su cui si ribalta
-- post: viene sollevato un errore
--
-- pre-post name: controlli per obbligazione da riportare all'esercizio successivo -
--                unità organizzativa non valida nel nuovo esercizio
-- pre: l'unità organizzativa del documento non è valida nel nuovo esercizio
-- post: viene sollevato un errore
--
-- pre-post name: controlli per obbligazione da riportare all'esercizio successivo -
-- pre: nessuna delle precedenti precondizioni
-- post: non viene sollevato nessun errore
--
-- Parametri:
--
-- aObb -> rowtype dell'obbligazione da riportare all'esercizio successivo
-- controlloRibalt -> boolean, doc cont che chiude la pgiro con origine di pgiro ribaltabile
--                    non deve necessariamente essere ribaltabile
procedure checkRiportaEsNext(aObb obbligazione%rowtype, controlloRibalt boolean);

procedure aggiornaAccNext(aAccNext accertamento%rowtype);
procedure aggiornaImpNext(aObbNext obbligazione%rowtype);
procedure aggiornaScadImpNext(
		  aObbNext obbligazione%rowtype,
		  aObbScadNext obbligazione_scadenzario%rowtype,
		  posizione number,
		  ListaObbScadVoceNext CNRCTB035.scadVoceListS,
		  isControlloBloccante boolean);

-- restituisce true se l'accertamento è stato completamente incassato
Function isAccEsaurito (aAcc accertamento%Rowtype) Return Boolean;
-- restituisce true se l'obbligazione è stata completamente incassata
Function isObbEsaurita (aObb obbligazione%Rowtype) Return Boolean;
end;
