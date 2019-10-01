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


import it.cnr.contab.inventario00.tabrif.bulk.*;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.ICRUDMgr;

public interface IInventario_beniMgr extends it.cnr.jada.comp.ICRUDMgr {
/** 
  *  L'inventario non esiste
  *    PreCondition:
  *      Non è stato trovato un Inventario associato alla UO di scrivania.
  *    PostCondition:
  *     Un messaggio di errore viene visualizzato all'utente per segnalare che la UO non ha un
  *		Inventario
  *
  *  L'inventario non è aperto.
  *    PreCondition:
  *      L'Inventario non è in stato "A", ossia aperto.
  *    PostCondition:
  *     Un messaggio di errore viene visualizzato all'utente per segnalare che l'Inventario 
  *		associato alla UO non è in stato "Aperto"
  *
  *  Carica l'Inventario associato alla UO di scrivania
  *    PreCondition:
  *      E' stata generata la richiesta di caricare l'Inventario associato alla UO di scrivania 
  *		e tutti i controlli sono stati superati
  *    PostCondition:
  *      vengono trasmessi i dati relativi all'Inventario associato alla UO di scrivania.
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  *
  * @return Id_inventarioBulk <code>SQLBuilder</code> l'Inventario associato alla UO di scrivania
**/

public abstract it.cnr.contab.inventario00.tabrif.bulk.Id_inventarioBulk caricaInventario(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,it.cnr.jada.persistency.IntrospectionException;
/** 
  *   Cerca i Tipi Ammortamento legati alla Categoria indicata
  *    PreCondition:
  *      E' stato richiesto di cercare i Tipi Ammortamento associati ad una Categoria Gruppo Inventario
  *    PostCondition:
  *      Viene restituita la lista dei Tipi Ammortamento legati alla Categoria Gruppo
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param categoria_gruppo <code>Categoria_gruppo_inventBulk</code> la Categoria Gruppo Inventario di riferimento
  *
  * @return <code>Collection</code> i Tipi Ammortamento eventualmente trovati
**/

public abstract java.util.Collection findTipiAmmortamento(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_inventBulk param1) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,it.cnr.jada.persistency.IntrospectionException;
/** 
  *  Richiede ID univoco di Transazione
  *    PreCondition:
  *      E' stato richiesto di recuperare/generare l'identificativo di transazione.
  *    PostCondition:
  *      Viene richiesto l'ID e, se questo non esiste, verrà generato, se richiesto
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param force <code>boolean</code> il flag che indica se forzare la generazione dell'ID
  *
  * @return local_transaction_id <code>String</code> l'ID di transazione richiesto
**/

public abstract java.lang.String getLocalTransactionID(it.cnr.jada.UserContext param0,boolean param1) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,it.cnr.jada.persistency.IntrospectionException;
/** 
  *  Cerca l'Inventario associato alla Uo di scrivania
  *    PreCondition:
  *      Non c'è un Inventario associato alla Uo di scrivania, oppure l'Inventario non è in stato "Aperto"
  *    PostCondition:
  *      Viene visualizzato un messaggio all'utente con la spiegazione dell'errore
  *
  *  Inizializzazione di una istanza di Inventario_beniBulk
  *    PreCondition:
  *      E' stata generata la richiesta di inizializzare una istanza di Inventario_beniBulk
  *    PostCondition:
  *      Vengono caricate le Condizioni_bene valide.
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> il bene che deve essere istanziato
  *
  * @return <code>OggettoBulk</code> il bene inizializzato
**/

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerInserimento(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Oggetto non esistente
  *    PreCondition:
  *      L'OggettoBulk specificato non esiste.
  *    PostCondition:
  *      Viene generata una ComponentException con la descrizione dell'errore.
  *   
  *  Inizializzazione di una istanza di Inventario_beniBulk per modifica
  *    PreCondition:
  *      E' stata richiesta l'inizializzazione di una istanza di Inventario_beniBulk per modifica
  *    PostCondition:
  *      Vengono caricati gli eventuali Utilizzatori del Bene, gli accessori o il bene padre,
  *		a seconda se il Bene di riferimento è rispettivamente un bene accessorio oppure no;
  *		carica, inoltre, i Tipi Ammortamento legati alla Categoria Gruppo Inventario a cui appartiene il Bene
  *		ed il Tipo Ammortamento associato al Bene stesso.
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> il bene che deve essere inizializzato
  *
  * @return <code>OggettoBulk</code> il bene inizializzato
**/

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerModifica(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Modifica non valida - Valida Bene.
  *    PreCondition:
  *      I controlli sulle modifiche apportate al bene non sono valide.
  *    PostCondition:
  *      Viene visualizzato un messaggio con la spiegazione dell'errore.
  *
  *  Modifica non valida - Valida Utilizzatori.
  *    PreCondition:
  *      I controlli sulle modifiche apportate agli utilizzatori del bene non sono valide.
  *    PostCondition:
  *      Viene visualizzato un messaggio con la spiegazione dell'errore.
  *  
  *  Modifica Bene
  *    PreCondition:
  *      E' stata generata la richiesta di modificare un Bene. 
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
/** 
  *  Cerca beni accessori
  *    PreCondition:
  *      E' stata generata la richiesta di cercare i beni accessori di un bene specificato.
  *    PostCondition:
  *      Viene restituito un Iteratore sui beni presenti sulla tabella INVENTARIO_BENI
  *		che rispondono ai requisiti richiesti, ossia che siano accessori del bene indicato.
  *		Sono visualizzati anche quei beni che sono stati scaricati totalmente.
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param bene il <code>Inventario_beniBulk</code> bene principale.
  *
  * @return ri <code>RemoteIterator</code> l'Iteratore sulla selezione.
**/

public abstract it.cnr.jada.util.RemoteIterator selectBeniAccessoriFor(it.cnr.jada.UserContext param0,it.cnr.contab.inventario00.docs.bulk.Inventario_beniBulk param1) throws it.cnr.jada.comp.ComponentException;
}
