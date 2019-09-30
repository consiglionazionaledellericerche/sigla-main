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

package it.cnr.contab.inventario00.comp;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.ICRUDMgr;
public interface IInventarioConsegnatarioMgr extends ICRUDMgr
{




/** 
  *  Errore nella validazione del Consegnatario.
  *    PreCondition:
  *      I dati specificati dall'utente per la creazione del onsegnatario non hanno passato 
  *		i controlli di validazione, (metodo validaConsegntario).
  *    PostCondition:
  *      Non  viene consentito il salvataggio dei dati.
  *
  *  Errore nella validazione della Data di Inizio validità
  *    PreCondition:
  *      La data di inizio validità specificata non ha superato i controlli di validità, (metodo checkDataInizioValidita).
  *    PostCondition:
  *      Non  viene consentito il salvataggio dei dati.
  *
  *  Errore nella validazione della Data di Fine validità
  *    PreCondition:
  *      La data di fine validità specificata non ha superato i controlli di validità, (metodo checkDataFineValidita).
  *    PostCondition:
  *      Non  viene consentito il salvataggio dei dati.
  *   
  *  Tutti i controlli superati.
  *    PreCondition:
  *      E' stata generata la richiesta di creazione di un Consegnatario per l'Inventario 
  *		associato alla UO di scrivania.
  *    PostCondition:
  *      Viene consentito il salvataggio.
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> il Bulk da creare
  *
  * @return bulk l'oggetto <code>OggettoBulk</code> creato
**/

public abstract it.cnr.jada.bulk.OggettoBulk creaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;

/** 
  *  Cerca l'Inventario di cui la Uo di scrivania è responsabile
  *    PreCondition:
  *      La Uo di scrivania non è responsabile di alcun Inventario, (metodo loadInventarioResp).
  *    PostCondition:
  *      Viene visualizzato un messaggio all'utente con la spiegazione dell'errore
  *
  *  Inizializzazione di una istanza di Inventario_consegnatarioBulk
  *    PreCondition:
  *      E' stata generata la richiesta di inizializzare una istanza di Inventario_consegnatarioBulk.
  *    PostCondition:
  *      Viene impostata e proposta la data odierna come data di inizio validità.
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta.
  * @param bulk <code>OggettoBulk</code> il bulk che deve essere inizializzato.
  *
  * @return bulk <code>OggettoBulk</code> il bulk inizializzato.
**/

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerInserimento(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;

/**    
  *  Inizializzazione di una istanza di Inventario_consegnatarioBulk per modifica
  *    PreCondition:
  *      E' stata richiesta l'inizializzazione di una istanza di Inventario_consegnatarioBulk per modifica.
  *    PostCondition:
  *      L'OggettoBulk viene inizializzato con tutti gli oggetti collegati e preparato
  *		per l'operazione modifica.
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta.
  * @param bulk <code>OggettoBulk</code> l'Inventario che deve essere inizializzato.
  *
  * @return bulk <code>OggettoBulk</code> l'Inventario inizializzato.
**/

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerModifica(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;

/** 
  *  Cerca l'Inventario di cui la Uo di scrivania è responsabile
  *    PreCondition:
  *      La Uo di scrivania non è responsabile di alcun Inventario, (metodo loadInventarioResp).
  *    PostCondition:
  *      Viene visualizzato un messaggio all'utente con la spiegazione dell'errore
  *
  *  Inizializzazione di una istanza di Inventario_consegnatarioBulk per Ricerca
  *    PreCondition:
  *      E' stata generata la richiesta di inizializzare una istanza di Inventario_consegnatarioBulk per Ricerca.
  *    PostCondition:
  *      L'OggettoBulk viene inizializzato con tutti gli oggetti collegati e preparato
  *		per l'operazione di ricerca.
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> l'Inventario che deve essere istanziato
  *
  * @return <code>OggettoBulk</code> l'Inventario inizializzato
**/

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerRicerca(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;

/** 
  *  Cerca l'Inventario di cui la Uo di scrivania è responsabile
  *    PreCondition:
  *      La Uo di scrivania non è responsabile di alcun Inventario, (metodo loadInventarioResp).
  *    PostCondition:
  *      Viene visualizzato un messaggio all'utente con la spiegazione dell'errore
  *
  *  Inizializzazione di una istanza di Inventario_consegnatarioBulk per Ricerca
  *    PreCondition:
  *      E' stata generata la richiesta di inizializzare una istanza di Inventario_consegnatarioBulk per Ricerca.
  *    PostCondition:
  *      L'OggettoBulk viene inizializzato con tutti gli oggetti collegati e preparato
  *		per l'operazione di ricerca.
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> l'Inventario che deve essere istanziato
  *
  * @return <code>OggettoBulk</code> l'Inventario inizializzato
**/

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerRicercaLibera(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;

/** 
  *  Errore - data fine validità
  *    PreCondition:
  *      Le modifiche apportate alla data di fine validità per il Consegnatario non superano 
  *		i controlli di validazione.
  *    PostCondition:
  *      Viene visualizzato un messaggio con la spiegazione dell'errore.
  *  
  *  Modifica Consegnatario
  *    PreCondition:
  *      E' stata generata la richiesta di modificare i dati del Consegnatario dell'Inventario.
  *		Le modifiche passano le validazioni.
  *    PostCondition:
  *      Viene consentito il salvataggio.
  *  
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> il Bulk da modificare
  *
  * @return l'oggetto <code>OggettoBulk</code> modificato
**/

public abstract it.cnr.jada.bulk.OggettoBulk modificaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
}