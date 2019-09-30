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

import java.math.*;
import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.ICRUDMgr;

import java.util.Vector;

public interface IImpegnoMgr extends IDocumentoContabileMgr, ICRUDMgr
{

/**
 * Aggiornamento in differita dei saldi degli impegni residui
 * Un documento amministrativo di spesa che agisce in modalità transazionale ha creato/modificato gli importi 
 * relativi ad un impegno pgiro; i saldi di tale impegno non possono essere aggiornati subito in quanto
 * tale operazione genererebbe dei lock sulle voci del piano che non ne consentirebbere l'utilizzo ad altri utenti;
 * pertanto l'aggiornamento dei saldi dell'obbligazione pgiro viene differito al momento del salvataggio
 * del documento amministrativo.
 *
 * Pre-post-conditions:
 *
 * Nome: Aggiorna saldi per impegno residuo creato
 * Pre:  MAI VERIFICABILE - mantenuto x omogeneità con tutti i doc. contabili
 *
 * Nome: Aggiorna saldi per impegno residuo esistente
 * Pre:  Una richiesta di aggiornamento dei saldi in differita e' stata generata per un impegno residuo
 *       che non e' stata creata nel contesto transazionale del documento amministrativo ( progressivo
 *       impegno  > 0)
 * Post: I saldi dell'impegno sono stati aggiornati nel metodo 'aggiornaSaldiInModifica'
 *
 * @param	userContext	lo UserContext che ha generato la richiesta
 * @param	docContabile	l'ImpegnoResiduoBulk per cui aggiornare i saldi
 * @param	values	la Map che contiene il "pg_ver_rec" iniziale dell'impegno 
 * @param	param parametro non utilizzato per impegni
 * 
*/
public abstract void aggiornaSaldiInDifferita(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk param1,java.util.Map param2,it.cnr.contab.doccont00.core.bulk.OptionRequestParameter param3) throws it.cnr.jada.comp.ComponentException;
public void callRiportaAvanti (UserContext userContext,it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk doc) throws ComponentException;
public void callRiportaIndietro (UserContext userContext,it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk doc) throws ComponentException;
/** 
  *  inizializzazione per modifica
  *    PreCondition:
  *      La richiesta di inizializzazione di un ImpegnoResiduoBulk per modifica
  *      e' stata generata
  *    PostCondition:
  *      Vengono recuperati la scadenza e il dettaglio di scadenza associati all'impegno.
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> l'impegno residuo da inizializzare per la modifica
  *
  * @return l'impegno residuo inizializzato per la modifica
 */

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerModifica(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  inizializzazione per ricerca
  *    PreCondition:
  *      La richiesta di inizializzazione di un ImpegnoResiduoBulk per ricerca
  *      e' stata generata
  *    PostCondition:
  *      Vengono impostati il codice Cds e il codice Cds di origine 
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> l'impegno  da inizializzare per la ricerca
  *
  * @return <code>OggettoBulk</code> l'impegno  inizializzato per la ricerca
 */

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerRicerca(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Lock scadenza
  *		PreCondition:
  *			E' stato richiesto l'inserimento di un lock sulla scadenza di un'obbligazione
  *    PostCondition:
  *  		Il record relativo alla scadenza e' stato messo in lock e non e' pertanto consentito ad altre transazioni
  *         l'accesso a tale scadenza
 */

public abstract void lockScadenza(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk param1) throws it.cnr.jada.comp.ComponentException;
/**
  *  errore modifca importo - doc.amm.associati
  *    PreCondition:
  *      La richiesta di modifica dell'importo di un impegno su partita di giro e' stata generata ma l'importo totale
  *      dei documenti amministrativi contabilizzati sulla scadenza supera il nuovo importo dell'impegno residuo
  *    PostCondition:
  *      Un messaggio di errore segnala all'utente l'impossibilità di effettuare la modifica in quanto l'importo dell'impegno
  *      deve sempre essere >= importo associato a doc.amm.
  *  modifica importo
  *    PreCondition:
  *      L'utente richiede la modifica dell'importo di un impegno residuo
  *    PostCondition:
  *      L'importo dell'impegno viene modificato e, in cascata, vengono modificati gli importi relativi
  *      alla scadenza e al dettaglio scadenza. I saldi relativi ai documenti contabili vengono aggiornati
  *		(metodo aggiornaCapitoloSaldoObbligazione).
  *		Vengono aggiornati gli stati COAN e COGE degli eventuali documenti amministrativi associati 
  *		(metodo aggiornaStatoCOAN_COGEDocAmm)
  *      Viene scatenata la scrittura di una variazione formale
  *  modifica descrizione
  *    PreCondition:
  *      L'utente richiede la modifica della descrizione di un impegno su partita di giro
  *    PostCondition:
  *      La descrizione dell'obbligazione e della scadenza di obbligazione vengono aggiornate
  *  modifica capitolo
  *    PreCondition:
  *      L'utente richiede la modifica del capitolo di un impegno residuo
  *    PostCondition:
  *      Il capitolo viene aggiornato sia in testata che nello scad_voce
  *      Viene scatenata la scrittura di una variazione formale
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> l'impegno  da modificare
  *
  * @return <code>OggettoBulk</code> l'impegno modificato
 */

public abstract it.cnr.jada.bulk.OggettoBulk modificaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/*
 * Modifica l'importo di una scadenza e della testata dell'obbligazione
 *	
 * Pre-post-conditions:
 *
 * NON SI PUO' MAI VERIFICARE
 *
 * @param userContext lo userContext che ha generato la richiesta
 * @param scad la scadenza (con importo originale)
 * @param nuovoImporto che deve assumere la scadenza
 * @param modificaScadenzaSuccessiva se true indica il fatto che la testata dell'obbligazione non deve essere modificata
 *                                   e che la differenza fra l'importo attuale e il vecchio importo deve essere riportata sulla
 *									 scadenza successiva
 * @param modificaScadenzaSuccessiva se false indica il fatto che deve essere modificato l'importo della scadenza e della testata
 *                                   dell'obbligazione
 * @return la scadenza 
 */

public abstract it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk modificaScadenzaInAutomatico(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk param1,java.math.BigDecimal param2,boolean param3) throws it.cnr.jada.comp.ComponentException;
}
