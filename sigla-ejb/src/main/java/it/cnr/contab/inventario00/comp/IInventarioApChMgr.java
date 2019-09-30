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
public interface IInventarioApChMgr extends ICRUDMgr
{




/** 
  *  Errore nella validazione inventario.
  *    PreCondition:
  *      Le modifiche apportate allo stato dell'Inventario non hanno superato la validazione, (metodo validaSuInserimento).
  *    PostCondition:
  *      Non  viene consentita la registrazione dell'inventario.
  *
  *  Tutti i controlli superati.
  *    PreCondition:
  *      Nessun errore rilevato
  *    PostCondition:
  *      Viene consentito il salvataggio.
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> il Bulk da creare
  *
  * @return bulk l'oggetto <code>OggettoBulk</code> creato
**/

public abstract it.cnr.jada.bulk.OggettoBulk creaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;

public abstract OggettoBulk creaConBulk(UserContext userContext,OggettoBulk bulk,it.cnr.contab.inventario00.docs.bulk.OptionRequestParameter status) throws ComponentException;

/** 
  *  Cerca l'Inventario di cui la Uo di scrivania è responsabile
  *    PreCondition:
  *      La Uo di scrivania non è responsabile di alcun Inventario, (metodo loadInventario).
  *    PostCondition:
  *      Viene visualizzato un messaggio all'utente con la spiegazione dell'errore
  *
  *  Inizializzazione di una istanza di Inventario_ap_chBulk
  *    PreCondition:
  *      E' stata generata la richiesta di inizializzare una istanza di Inventario_ap_chBulk
  *    PostCondition:
  *      Vengono impostati i parametri di base dell'Inventario come il Consegnatario.
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> l'inventario che deve essere istanziato
  *
  * @return <code>OggettoBulk</code> l'inventario inizializzato
**/

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerInserimento(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;

/**    
  *  Inizializzazione di una istanza di Inventario_ap_chBulk per modifica
  *    PreCondition:
  *      E' stata richiesta l'inizializzazione di una istanza di Inventario_ap_chBulk per modifica
  *    PostCondition:
  *      Viene caricato il Consegnatario dell'Inventario ed abilita la possibilità di modificare 
  *		il valore di riferimento iniziale per la numerazione dei Beni facenti parte dell'Inventario.
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> l'Inventario che deve essere inizializzato
  *
  * @return bulk <code>OggettoBulk</code> l'Inventario inizializzato
**/

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerModifica(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;

/** 
  *  Cerca l'Inventario di cui la Uo di scrivania è responsabile
  *    PreCondition:
  *      La Uo di scrivania non è responsabile di alcun Inventario, (metodo loadInventario).
  *    PostCondition:
  *      Viene visualizzato un messaggio all'utente con la spiegazione dell'errore
  *
  *  Inizializzazione di una istanza di Inventario_ap_chBulk per Ricerca
  *    PreCondition:
  *      E' stata generata la richiesta di inizializzare una istanza di Inventario_ap_chBulk per Ricerca
  *    PostCondition:
  *      L'OggettoBulk viene inizializzato con tutti gli oggetti collegati e preparato
  *			per l'operazione inserimento criteri di ricerca.
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
  *      La Uo di scrivania non è responsabile di alcun Inventario.
  *    PostCondition:
  *      Viene visualizzato un messaggio all'utente con la spiegazione dell'errore
  *
  *  Carica l'Inventario di competenza
  *    PreCondition:
  *      E' stato richiesto di caricare l'Inventario di cui la UO di scrivania è responsabile.
  *    PostCondition:
  *      Viene caricato l'Inventario di competenza impostando come clausole di ricerca che
  *		la data di apertura sia l'ultima registrata sul DB; questo per essere sicuri che lo stato
  *		dell'Inventario carcicato sia quello attuale.
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  *
  * @return invApCH <code>Inventario_ap_chBulk</code> l'Inventario allo stato attuale
**/

public abstract it.cnr.contab.inventario00.tabrif.bulk.Inventario_ap_chBulk loadInventarioApChAttuale(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,it.cnr.jada.persistency.IntrospectionException;

/** 
  *  Errore sulle date indicate
  *    PreCondition:
  *      E' stata richiesta un modifica allo stato dell'inventario ed i controlli effettuati
  *		sulle date di apertura e chiusura non sono stati superati.
  *    PostCondition:
  *      Viene visualizzato un messaggio con la spiegazione dell'errore.
  *
  *  Errore 
  *    PreCondition:
  *       E' stata richiesta un modifica allo stato dell'inventario ed i controlli effettuati
  *		sul valore indicato come riferimento per il primo bene, non sono stati superati.
  *    PostCondition:
  *      Viene visualizzato un messaggio con la spiegazione dell'errore.
  *  
  *  Modifica Bene
  *    PreCondition:
  *      E' stata generata la richiesta di modificare lo stato di un Inventario.
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