/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package it.cnr.contab.doccont00.comp;

import it.cnr.contab.config00.sto.bulk.*;
import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.contab.doccont00.core.bulk.AccertamentoBulk;
import java.util.Vector;
import it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk;
import it.cnr.contab.doccont00.core.bulk.Accertamento_scadenzarioBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.ICRUDMgr;

public interface IAccertamentoMgr extends IDocumentoContabileMgr, ICRUDMgr
{

/**
 * Aggiornamento in differita dei saldi dell'accertamento
 * Un documento amministrativo di entrata che agisce in modalità transazionale ha creato/modificato gli importi 
 * relativi ad un accertamento; i saldi di tale accertamento non possono essere aggiornati subito in quanto
 * tale operazione genererebbe dei lock sulle voci del piano che non ne consentirebbero l'utilizzo ad altri utenti;
 * pertanto l'aggiornamento dei saldi dell'accertamento viene differito al momento del salvataggio
 * del documento amministrativo.
 *
 * Pre-post-conditions:
 *
 * Nome: Aggiorna saldi per accertamento creato
 * Pre:  Una richiesta di aggiornamento dei saldi in differita e' stata generata per un accertamento su capitoli di
 *       bilancio che e' stata creato nel contesto transazionale del documento ammninistrativo ( progressivo
 *       accertamento < 0)
 * Post: I saldi dell'accertamento sono stati aggiornati nel metodo 'aggiornaSaldiInInserimento'
 *
 * Nome: Aggiorna saldi per accertamento esistente
 * Pre:  Una richiesta di aggiornamento dei saldi in differita e' stata generata per un accertamento su capitoli di
 *       bilancio che non e' stata creato nel contesto transazionale del documento ammninistrativo ( progressivo
 *       accertamento > 0)
 * Post: I saldi dell'accertamento sono stati aggiornati nel metodo 'aggiornaSaldiInModifica'
 *
 * @param	uc	lo UserContext che ha generato la richiesta
 * @param	docContabile	il documento contabile di tipo AccertamentoBulk per cui aggiornare i saldi
 * @param	values	la Map che contiene il "pg_ver_rec" iniziale dell'accertamento
 * @param	param paramtero non utilizzato per gli accertamenti
 *
*/

public abstract void aggiornaSaldiInDifferita(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk param1,java.util.Map param2,it.cnr.contab.doccont00.core.bulk.OptionRequestParameter param3) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Tutti controlli superati
  *    PreCondition:
  *      scadenza(n+1) esiste
  *      scadenza(n+1).importo > differenza in scadenza(n).importo
  *    PostCondition:
  *      Il sistema eseguirà l'aggiornamento dell'importo della scadenza successiva (n+1) dell'accertamento aggiungendo la differenza fra il nuovo e vecchio importo della scadenza in aggiornamento. 
  *      La differenza è espressa come (scadenzario(n).importo_nuovo - scadenzario(n).importo_vecchio)
  *  scadenza(n+1).importo <= differenza in scadenza(n).importo
  *    PreCondition:
  *      L'utente richiede l'aggiornamento in automatico dell'importo della scadenza successiva (scadenza(n+1)) alla scadenza in elaborazione (scadenza(n)), ma l'aumento dell'importo della scadenza(n) supera il valore dell'importo dell'ultima scadenza dell'accertamento. Una formula per questa condizione sarebbe (scadenzario(n+1).importo - (scadenzario(n).importo_nuovo - scadenzario(n).importo_vecchio) > 0)
  *    PostCondition:
  *      Il metodo utilizza un Throw Exception per comunicare che l'aggiornamento in automatico dell'importo non è possibile perché l'aumento dell'importo della scadenza(n) è maggiore all'importo dell'ultima scadenza (cercarebbe settare l'importo <= 0). L'attività non è consentita.
  *  scadenza(n+1) non esiste
  *    PreCondition:
  *      L'utente richiede l'aggiornamento in automatico dell'importo della scadenza successiva (scadenza(n+1)) alla scadenza in elaborazione (scadenza(n)), ma la scadenza in aggiornamento è l'ultima scadenza dell'accertamento.
  *    PostCondition:
  *      Il metodo utilizza un Throw Exception per comunicare che l'aggiornamento in automatico dell'importo non è possibile perché non esiste una scadenza successiva. L'attività non è consentita.
  *
  * @param	aUC	lo UserContext che ha generato la richiesta
  * @param	scadenza	l'Accertamento_scadenzarioBulk per cui aggiornare la scadenza successiva
  * @return l'AccertamentoBulk con l'importo della scadenza successiva modificato
  *
 */

public abstract it.cnr.contab.doccont00.core.bulk.AccertamentoBulk aggiornaScadenzarioSuccessivoAccertamento(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.Accertamento_scadenzarioBulk param1) throws it.cnr.jada.comp.ComponentException;
/**
 *
 * Pre-post-conditions:
 *
 * Nome: Annullamento logico accertamento
 * Pre:  Una richiesta di annullamento di un accertamento e' stata generata
 *       L'accertamento non ha documenti amministartivi associati
 * Post: Viene impostata la data di cancellazione dell'accertamento, viene azzerato il suo importo e gli importi di tutte 
 *       le scadenze e di tutti dettagli di scadenza. Viene aggiornato il saldo relativo al capitolo dell'accertamento.
 *
 *
 * Nome: Errore doc. amm.
 * Pre:  Una richiesta di annullamento di un accertamento e' stata generata
 *       L'accertamento ha documenti amministartivi associati
 * Post: Una segnalazione di errore viene restituita per comunicare all'utente l'impossibilità di eseguire l'operazione 
 *       di annullamento
 *
 * @param	aUC	lo UserContext che ha generato la richiesta
 * @param	accertamento l' AccertamentoBulk da annullare
 * @return  accertamento l' AccertamentoBulk annullato
*/

public abstract it.cnr.contab.doccont00.core.bulk.AccertamentoBulk annullaAccertamento(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.AccertamentoBulk param1) throws it.cnr.jada.comp.ComponentException;
public void callRiportaAvanti (UserContext userContext,it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk doc) throws ComponentException;
public void callRiportaIndietro (UserContext userContext,it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk doc) throws ComponentException;
/**
 *
 * Pre-post-conditions:
 *
 * Nome: Cancella i dettagli della scadenza
 * Pre:  L'utente ha modificato la selezione della linea di attività dell'accertamento 
 * Post: Sono stati eliminati i dettagli della scadenza in quanto si riferivano alla precedente linea di attività
 *
 * @param	aUC	lo UserContext che ha generato la richiesta
 * @param	accertamento l' AccertamentoBulk la cui linea di attività e' stata modificata
 * @param	scadenza l'Accertamento_scadenzarioBulk per cui eliminare i dettagli
 * @return	l'Accertamento_scadenzarioBulk senza dettagli
*/

public abstract it.cnr.contab.doccont00.core.bulk.Accertamento_scadenzarioBulk cancellaDettagliScadenze(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.AccertamentoBulk param1,it.cnr.contab.doccont00.core.bulk.Accertamento_scadenzarioBulk param2) throws it.cnr.jada.comp.ComponentException;
/** 
  *  creazione accertamento di sistema
  *    PreCondition:
  *      E' stata generata la richiesta di creazione di un accertamento di sistema di appartenenza di un Cds per completare
  *      la pratica della reversale di trasferimento dal CNR a favore del Cds
  *    PostCondition:
  *      Un accertamento di sistema (ACR_SIST) e' stato creato per il Cds beneficiario del mandato di accreditamento,
  *      con cds/uo origine uguale al cds/uo appartenenza e importo pari alla riga del mandato. Il capitolo di entrata Cds
  *      viene ricavato dalla relazione fra Capitolo di Spesa Cnr e Capitolo di Entrata Cds (metodo findAssociazioneCapSpesaCNRCapEntrataCdS),
  *      il debitore e' il CNR. Sono stati inoltre creati una scadenza di accertamento (metodo creaAccertamento_scadenzario)
  *      e un dettaglio di tale scadenza (metodo creaAccertamento_scad_voce)
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param mandatoRiga <code>MandatoAccreditamento_rigaBulk</code> la riga del mandato di accreditamento per la quale e' 
  *        necessario creare una riga della reversale di trasferimento associata all'accertamento di sistema da creare
  * @param uo <code>Unita_organizzativaBulk</code> l'unità organizzativa beneficiaria del mandato di accreditamento
  * @return <code>AccertamentoBulk</code> L'accertamento di sistema creato
 */

public abstract it.cnr.contab.doccont00.core.bulk.AccertamentoBulk creaAccertamentoDiSistema(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.MandatoAccreditamento_rigaBulk param1,it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk param2) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Tutti i controlli superati - contesto non transazionale
  *    PreCondition:
  *      Una richiesta di creazione di un accertamento e' stata generata
  *      L'accertamento ha superato i controlli eseguiti dal metodo 'verificaAccertamento' 
  *      L'accertamento non e' stato creato in un contesto transazionale
  *    PostCondition:
  *      L'accertamento viene creato, il suo tipo documento viene impostato in base all'esercizio di competenza
  *      ( se esercizio competenza = esercizio di creazione il tipo e' ACR, altrimenti e' ACR_PLUR) e il saldo
  *      del capitolo dell'accertamento viene aggiornato (metodo 'aggiornaCapitoloSaldoAccertamento')
  *  Tutti i controlli superati - contesto transazionale
  *    PreCondition:
  *      Una richiesta di creazione di un accertamento e' stata generata
  *      L'accertamento ha superato i controlli eseguiti dal metodo 'verificaAccertamento' 
  *      L'accertamento e' stato creato in un contesto transazionale
  *    PostCondition:
  *      L'accertamento viene creato e il suo tipo documento viene impostato in base all'esercizio di competenza
  *      ( se esercizio competenza = esercizio di creazione il tipo e' ACR, altrimenti e' ACR_PLUR) 
  *  Errore di verifica accertamento
  *    PreCondition:
  *      Una richiesta di creazione di un accertamento e' stata generata e l'accertamento non ha superato i
  *      controlli eseguiti dal metodo 'verificaAccertamento'
  *    PostCondition:
  *      Viene generata un'ApplicationException che descrive all'utente l'errore che si e' verificato
  *
  * @param uc lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>AccertamentoBulk</code> l'accertamento da creare
  * @return <code>AccertamentoBulk</code> l'accertamento  creato
  *   
 */

public abstract it.cnr.jada.bulk.OggettoBulk creaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/**
 *
 * Pre-post-conditions:
 *
 * Nome: creazione scadenza
 * Pre:  Una richiesta di creazione di una nuova scadenza di un accertamento e' stata generata
 * Post: Vengono creati i dettagli (metodo 'creaDettagliScadenza') per la scadenza creata
 *
 * Nome: modifica scadenza
 * Pre:  Una richiesta di modifica di una scadenza di un accertamento e' stata generata
 * Post: Vengono modificati i dettagli (metodo 'modificaDettagliScadenza') per la scadenza modificata
 *
 * Nome: modifica linea att.
 * Pre:  Una richiesta di modifica della linea di attività di un accertamento e' stata generata
 * Post: Vengono eliminati tutti i dettagli di tutte le scadenze dell'accertamento (metodo 'cancellaDettagliScadenze') e vengono creati dei nuovi dettagli
 *       relativi alla nuova linea di attività (metodo 'creaDettagliScadenza')
 *
 * @param	aUC	lo UserContext che ha generato la richiesta
 * @param	accertamento l' AccertamentoBulk per cui rivedere i dettagli della scadenza
 * @param	scadenzaSelezionata l' Accertamento_scadenzarioBulk per cui rivedere i dettagli della scadenza oppure null
 *          nel caso in cui siano da rivedere i dettagli di tutte le scadenze dell'accertamento
*/

public abstract it.cnr.contab.doccont00.core.bulk.AccertamentoBulk generaDettagliScadenzaAccertamento(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.AccertamentoBulk param1,it.cnr.contab.doccont00.core.bulk.Accertamento_scadenzarioBulk param2) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Esercizio non aperto
  *    PreCondition:
  *      L'esercizio di scrivania e' in uno stato diverso da APERTO
  *    PostCondition:
  *      Il metodo utilizza un Throw Exception per comunicare che non e' possibile creare accertamenti
  *  Esercizio aperto
  *    PreCondition:
  *      L'esercizio di scrivania e' in stato APERTO
  *    PostCondition:
  *      una istanza di AccertamentoBulk viene restituita con impostata la data del giorno come data di emissione e
  *      il Cds Ente (999) come Cds dell'accertamento
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> l'istanza di AccertamentoBulk da inizializzare
  * @return <code>OggettoBulk</code> l'istanza di AccertamentoBulk inizializzata
  *
 */

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerInserimento(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Inizializzazione di un accertamento
  *    PreCondition:
  *      La richiesta di inizializzare un accertamento e' stata generata
  *    PostCondition:
  *      L'accertamento e' stato inizializzato, tutte le sue scadenze ed i loro dettagli sono stati inizializzati; le
  *      linee di attività eleggibili sono state caricate
  *
  *  Inizializzazione di un accertamento di sistema
  *    PreCondition:
  *      La richiesta di inizializzare un accertamento di sistema e' stata generata
  *    PostCondition:
  *      L'accertamento e' stato inizializzato (metodo inizializzaAccertamentoCdsPerModifica)
  *  
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> l'istanza di AccertamentoBulk o AccertamentoCdsBulk da inizializzare
  * @return <code>OggettoBulk</code> l'istanza di AccertamentoBulk o AccertamentoCdsBulk inizializzata
  *
 */

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerModifica(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/* normale
 *		PreCondition :
 *			L'utente ha creato una nuova Linea di attivita' da "Gestione Accertamento"
 *          La nuova linea di attività deve essere validata per l'accertamento in base alla sua natura
 *		PostCondition :
 *			L'applicazione ritorna l'elenco dei codici natura compatibili con il capitolo dell'accertamento
 *
 * @param aUC lo <code>UserContext</code> che ha generato la richiesta
 * @param accertamento l'istanza di AccertamentoBulk per cui selezionare le nature valide
 * @return <code>Vector</code> con i codici natura validi
 *
*/

public abstract java.util.Vector listaCodiciNaturaPerCapitolo(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.AccertamentoBulk param1) throws it.cnr.jada.comp.ComponentException;
/* normale
 *		PreCondition :
 *			Una richiesta di estrazione delle linee di attività valide per un accertamento e' stata generata
 *		PostCondition :
 *			L'applicazione ritorna l'elenco delle linee di attività di entrata con cdr appartenente all'unità organizzativa di
 *          scrivania e con natura compatibile con il capitolo selezionato per l'accertamento
 *
 * @param aUC lo <code>UserContext</code> che ha generato la richiesta
 * @param accertamento l'istanza di AccertamentoBulk per cui elencare le linee di attività valida
 * @return <code>Vector</code> con le istanze di Linea_attivitaBulk
 *
*/

public abstract java.util.Vector listaLineeAttivitaPerCapitolo(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.AccertamentoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Lock scadenza
  *		PreCondition:
  *			E' stato richiesto l'inserimento di un lock sulla scadenza di un accertamento
  *    PostCondition:
  *  		Il record relativo alla scadenza e' stato messo in lock e non e' pertanto consentito ad altre transazioni
  *         l'accesso a tale scadenza
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param scadenza l'istanza di Accertamento_scadenzarioBulk per cui mettere un lock
  *
 */

public abstract void lockScadenza(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Tutti i controlli superati - contesto non transazionale
  *    PreCondition:
  *      Una richiesta di modifica di un accertamento e' stata generata
  *      L'accertamento ha superato i controlli eseguiti dal metodo 'verificaAccertamento' 
  *      L'accertamento non e' stato creato in un contesto transazionale
  *    PostCondition:
  *      L'accertamento viene modificato
  *      Il saldo del capitolo dell'accertamento viene aggiornato (metodo 'aggiornaCapitoloSaldoAccertamento')
  *      Lo stato COAN/COGE degli eventuali doc. amministrativi collegati alle scadenza dell'accertamento viene
  *      aggiornato (metodo 'aggiornaStatoCOAN_COGEDocAmm')
  *  Tutti i controlli superati - contesto transazionale
  *    PreCondition:
  *      Una richiesta di modifica di un accertamento e' stata generata
  *      L'accertamento ha superato i controlli eseguiti dal metodo 'verificaAccertamento' 
  *      L'accertamento e' stato modifica in un contesto transazionale
  *    PostCondition:
  *      L'accertamento viene modificato
  *  Errore di verifica accertamento
  *    PreCondition:
  *      Una richiesta di modifica di un accertamento e' stata generata e l'accertamento non ha superato i
  *      controlli eseguiti dal metodo 'verificaAccertamento'
  *    PostCondition:
  *      Viene generata un'ApplicationException che descrive all'utente l'errore che si e' verificato
  *
  * @param uc lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>AccertamentoBulk</code> l'accertamento da modificare
  * @return <code>AccertamentoBulk</code> l'accertamento  modificato
  *   
 */

public abstract it.cnr.jada.bulk.OggettoBulk modificaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/*
 * Modifica l'importo di una scadenza e aggiunge la differenza alla scadenza successiva oppure modifica l'importo di una
 * scadenza e l'importo della testata dell'accertamento
 *	
 * Pre-post-conditions:
 *
 * Nome: Modifica Scadenza 
 * Pre:  E' stata generata la richiesta di modifica dell'importo di una scadenza 
 * Post: L'importo della scadenza e della testata dell'accertamento sono stati modificati dal metodo 'modificaScadenzaNonInAutomatico'
 *
 * Nome: Modifica Scadenza successiva
 * Pre:  E' stata generata la richiesta di modifica dell'importo di una scadenza e la differenza fra il nuovo importo
 *       e l'importo precedente deve essere riportato sulla scadenza successiva
 * Post: L'importo della scadenza e della scadenza successiva sono stati modificati
 *
 * Nome: Scadenza con più di 1 dettaglio - Errore
 * Pre:  E' stata generata la richiesta di modifica dell'importo di una scadenza e la scadenza ha più di un dettaglio
 * Post: Viene generata un'ApplicationException per segnalare l'impossibilità di aggiornamento della scadenza
 *
 * Nome: Scadenza successiva - Errore ultima scadenza
 * Pre:  E' stata generata la richiesta di modifica dell'importo di una scadenza e non esiste una scadenza
 *       successiva su cui scaricare la differenza fra l'importo attuale scadenza e il nuovo importo
 * Post: Viene generata un'ApplicationException per segnalare l'impossibilità di aggiornamento della scadenza
 *
 * Nome: Scadenza successiva -  Errore importo scadenza successiva
 * Pre:  E' stata generata la richiesta di modifica dell'importo di una scadenza e (im_scadenza_successisva -
 *       nuovo_im_scadenza + im_scadenza) e' minore di 0
 * Post: Viene generata un'ApplicationException per segnalare l'impossibilità di aggiornamento della scadenza
 * 
 * Nome: Scadenza successiva -  Errore doc amministrativi associati
 * Pre:  E' stata generata la richiesta di modifica dell'importo di una scadenza e la scadenza successiva ha 
 *       già dei documenti amministrativi associati
 * Post: Viene generata un'ApplicationException per segnalare l'impossibilità di aggiornamento della scadenza
 *
 * @param userContext lo userContext che ha generato la richiesta
 * @param scad l'istanza di Accertamento_scadenzarioBulk il cui importo deve essere modificato
 * @param nuovoImporto il valore del nuovo importo che la scadenza di accertamento dovrà assumere
 * @param modificaScadenzaSuccessiva il flag che indica se modificare la testata dell'accertamento o modificare la scadenza
 *        successiva dell'accertamento
 * @return l'istanza di Accertamento_scadenzarioBulk con l'importo modificato
 */

public abstract it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk modificaScadenzaInAutomatico(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk param1,java.math.BigDecimal param2,boolean param3) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Tutti controlli superati
  *    PreCondition:
  *      L'accertamento ha almeno una scadenza
  *      La somma degli importi delle scadenze e' uguale all'importo dell'accertamento
  *      La somma degli importi dei dettagli di ogni scadenza e' uguale all'importo della scadenza
  *    PostCondition:
  *      L'accertamento supera la validazione ed il sistema può pertanto proseguire con il suo salvataggio
  *  
  *  sum(scadenzario.importo) not = accertamento.importo
  *    PreCondition:
  *      La somma degli importi delle scadenze dell'accertamento non è uguale all'importo dell'accertamento in elaborazione.
  *    PostCondition:
  *      Il metodo utilizza un Throw Exception per comunicare che il salvataggio dell'accertamento non è consentito 
  *      se l'importo non è uguale alla somma degli importi delle scadenze dell'accertamento.
  *
  *  sum(scad_voce.importo) not = scadenzario.importo
  *    PreCondition:
  *      La somma degli importi 
  *      dei dettagli di una scadenza dell' accertamento non è uguale all'importo della scadenza
  *    PostCondition:
  *      Il metodo utilizza un Throw Exception per comunicare che il salvataggio dell'accertamento non è consentito 
  *      se l'importo della scadenza non è uguale alla somma degli importi dei dettagli della scadenza dell'accertamento.
  *
  *  scadenze non definite
  *    PreCondition:
  *      Non sono state definite scadenze per l'accertamento 
  *    PostCondition:
  *      Il metodo utilizza un Throw Exception per comunicare che il salvataggio dell'accertamento non è consentito 
  *      se non viene definita almento una scadenza
  *
  * @param aUC lo user context 
  * @param accertamento l'istanza di  <code>AccertamentoBulk</code> da verificare
  *  
  *
 */

public abstract void verificaAccertamento(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.AccertamentoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Tutti controlli superati - creazione
  *    PreCondition:
  *      Non esiste già una scadenza per la data.
  *      Attività = creazione
  *    PostCondition:
  *      Alla scrittura dell'accertamento il sistema aggiungerà questo scadenzario e genererà tutti i dettagli della
  *      scadenza (metodo 'generaDettagliScadenzaAccertamento')
  *  Tutti controlli superati - aggiornamento con agg. auto. scad. succ.
  *    PreCondition:
  *      Attività = aggiornamento
  *      L'utente ha scelto l'aggiornamento in automatico della scadenza successiva.
  *    PostCondition:
  *      Alla scrittura dell'accertamento il sistema aggiornerà questo scadenzario. 
  *      In più, il metodo aggiornaScadenzaSuccessivaAccertamento viene utilizzato per aggiornare la scadenza successiva 
  *      a quella in aggiornamento. 
  *  Tutti controlli superati - aggiornamento senza agg. auto. scad. succ.
  *    PreCondition:
  *      Attività = aggiornamento
  *      L'utente NON ha scelto l'aggiornamento in automatico della scadenza successiva.
  *    PostCondition:
  *      Alla scrittura dell'accertamento il sistema aggiornerà questo scadenzario. 
  *      Sarà il compito dell'utente aggiornare una delle scadenze per garantire che la somma degli importi 
  *      delle scadenze sia uguale all'importo dell'accertamento.
  *  creazione/modifica - esiste già una scadenza per la data
  *    PreCondition:
  *      L'utente richiede la creazione di una scadenza o modifica la data di una scadenza. 
  *      Per la data scadenza specificata esiste già una scadenza per l'accertamento.
  *    PostCondition:
  *      Il metodo utilizza un Throw Exception per comunicare che la data della scadenza non è valida.
  *  modifica - la scadenza ha doc amministrativi associati e non proviene da documenti amministrativi
  *    PreCondition:
  *      L'utente richiede la modifica dell'importo di una scadenza che ha documenti amministrativi associati
  *      e la richiesta non proviene dal BusinessProcess che gestisce i documenti amministrativi  
  *    PostCondition:
  *      Il metodo utilizza un Throw Exception per comunicare che la modifica della scadenza non è valida.
  *  modifica - la scadenza ha doc amministrativi associati e proviene da documenti amministrativi
  *    PreCondition:
  *      L'utente richiede la modifica dell'importo di una scadenza che ha documenti amministrativi associati e la
  *      richiesta proviene dal BusinessProcess che gestisce i documenti amministartivi
  *    PostCondition:
  *      L'aggiornamento dell'importo della scadenza e' consentito
  *  modifica - la scadenza ha reversali associate
  *    PreCondition:
  *      L'utente richiede la modifica dell'importo di una scadenza 
  *      La scadenza ha reversali associate
  *      La richiesta di modifica proviene dal BusinessProcess che gestisce i documenti amministrativi
  *    PostCondition:
  *      L'aggiornamento dell'importo della scadenza non e' consentito
  *  
  * @param aUC lo user context 
  * @param scadenza l'istanza di  <code>Accertamento_scadenzarioBulk</code> da verificare
  * @return l' AccertamentoBulk a cui appartiene la scadenza
  *
  */

public abstract it.cnr.contab.doccont00.core.bulk.AccertamentoBulk verificaScadenzarioAccertamento(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.Accertamento_scadenzarioBulk param1) throws it.cnr.jada.comp.ComponentException;
}
