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
import it.cnr.jada.util.RemoteIterator;
public interface IUbicazioneBeneMgr extends ICRUDMgr
{




/** 
  *  Tutti i controlli superati.
  *    PreCondition:
  *      E' stata generata la richiesta di creazione di una Ubicazione associata alla UO di scrivania.
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
  *  L'Ubicazione ha delle Ubicazioni figlie
  *    PreCondition:
  *      Si sta tentando di cancellare una Ubicazione che ha sotto di sè dei nodi figli.
  *    PostCondition:
  *      Viene visualizzato un messaggio all'utente con che spiega la necessità di cancellare 
  *		tutti i nodi figli prima di cancellare il nodo padre.
  *
  *  Tutti i controlli superati.
  *    PreCondition:
  *      E' stata generata la richiesta di cancellazione di una Ubicazione associata alla UO di scrivania.
  *    PostCondition:
  *      Viene consentito l'eliminazione.
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta.  
  * @param bulk <code>OggettoBulk</code> il Bulk da eliminare.
**/

public abstract void eliminaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;

/** 
  *  Tutti i controlli superati.
  *    PreCondition:
  *      E' stata generata la richiesta di creazione di un Iteratore su tutti i nodi figli 
  *		di una Ubicazione.
  *    PostCondition:
  *		 Viene restituito il RemoteIterator con l'elenco degli eventuali nodi figli dell'Ubicazione di riferimento.
  *      
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> l'Ubicazione di riferimento.
  *
  * @return remoteIterator <code>RemoteIterator</code> l'Iterator creato.
**/

public abstract it.cnr.jada.util.RemoteIterator getChildren(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;

/** 
  *  Tutti i controlli superati.
  *    PreCondition:
  *      E' stata generata la richiesta di ricerca dell'Ubicazione padre dell'Ubicazione specificata negli argomenti.
  *    PostCondition:
  *		 Viene restituito l'oggetto UbicazioneBulk che è l'Ubicazione padre cercata.
  *      
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> l'Ubicazione di riferimento.
  *
  * @return bulk <code>OggettoBulk</code> l'Ubicazione cercata.
**/

public abstract it.cnr.jada.bulk.OggettoBulk getParent(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;

/** 
  *  Inizializzazione di una istanza di Ubicazione_beneBulk
  *    PreCondition:
  *      E' stata generata la richiesta di inizializzare una istanza di Ubicazione_beneBulk.
  *    PostCondition:
  *      Vengono inizializzate le proprietà dell'Ubicazione_beneBulk e, l'oggetto risultante, viene restituito.
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta.
  * @param bulk <code>OggettoBulk</code> il bulk che deve essere inizializzato.
  *
  * @return bulk <code>OggettoBulk</code> il bulk inizializzato.
**/

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerInserimento(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;

/** 
  *  Controlla che l'ubicazione sia una foglia.
  *    PreCondition:
  *      E' stata generata la richiesta di controllare se l'Ubicazione specificata è una foglia,
  *		ossia se il suo livello è l'ultimo, (3). Questo implicherebbe che l'Ubicazione in 
  *		questione non può avere delle Ubicazioni figlie.
  *    PostCondition:
  *		 Viene restituito un valore booleano:
  *			- true: l'Ubicazione è una foglia;
  *			- false: l'Ubicazione non è una foglia.
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> l'Ubicazione di riferimento.
  *
  * @return il risultato <code>boolean</code> del controllo.
**/

public abstract boolean isLeaf(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
}