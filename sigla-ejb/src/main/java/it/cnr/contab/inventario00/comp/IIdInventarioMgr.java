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
import it.cnr.contab.inventario00.tabrif.bulk.Id_inventarioBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.ICRUDMgr;
public interface IIdInventarioMgr extends ICRUDMgr
{




/** 
  *  Errore nella validazione inventario.
  *    PreCondition:
  *      ValidaId_inventario  non superato.
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
  * @return l'oggetto <code>OggettoBulk</code> creato
**/

public abstract it.cnr.jada.bulk.OggettoBulk creaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;

/** 
  *  Cerca l'inventario associato ad una data UO
  *    PreCondition:
  *      E' stata generata la richiesta di cercare l'Inventario (Id_InventarioBulk), 
  *			associato ad una UO.
  *    PostCondition:
  *      Effettua la ricerca addottando come criteri di ricerca il Codice del CdS e della UO passati come argomento.
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param cdCds il <code>String</code> codice del CdS di cui bisogna cercare l'Inventario associato
  * @param cdUo il <code>String</code> codice della UO di cui bisogna cercare l'Inventario associato
  * @param resp il <code>boolean</code> flag che indica se bisogna cercare l'Associazione responsabile
  *
  * @return l'Inventario <code>Id_inventarioBulk</code> trovata
**/

public abstract it.cnr.contab.inventario00.tabrif.bulk.Id_inventarioBulk findInventarioFor(it.cnr.jada.UserContext param0,java.lang.String param1,java.lang.String param2,boolean param3) throws it.cnr.jada.comp.ComponentException;

/** 
  *  Inizializzazione di una istanza di Id_InventarioBulk
  *    PreCondition:
  *      E' stata generata la richiesta di inizializzare una istanza di Id_InventarioBulk
  *    PostCondition:
  *      Vengono cercate e caricate le Unità Organizzative disponibili per essere associate all'Inventario
  *			che si sta creando.
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> l'Inventario che deve essere inizializzato
  *
  * @return <code>OggettoBulk</code> l'Inventario inizializzato
 */

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerInserimento(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;

/** 
  *   Inizializzazione di un Id_Inventario per modifica
  *    PreCondition:
  *      E' stata generata la richiesta di inizializzare l'Inventario per la modifica.
  *    PostCondition:
  *      Vengono caricate tutte le Unità Oganizzative associate con l'Inventario che si vuole modificare
  *		Tra le UO caricate, poi, si va ad individuare quella Responsabile dell'Inventario.
  *		Infine si caricano le UO ancora disponibli per essere associate.  
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> l'Inventario che deve essere inizializzato
  *
  * @return <code>OggettoBulk</code> l'Inventario inizializzato
 */

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerModifica(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;

/** 
  *   Controllo dello stato di un Inventario
  *    PreCondition:
  *      E' stata richiesto di controllare lo stato Aperto/Chiuso di un Inventario
  *    PostCondition:
  *      
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param inv <code>Id_inventarioBulk</code> l'Inventario di cui deve essere controllato lo stato
  * @param esercizio <code>Integer</code> l'Esercizio di in cui vafatto il controllo
  *
  * @return lo stato <code>boolean</code> dell'inventario: 
  *		 - true ==> l'Inventario è in stato 'A', (Aperto);
  *		 - false ==> negli altri casi.
 */

public abstract boolean isAperto(it.cnr.jada.UserContext param0,it.cnr.contab.inventario00.tabrif.bulk.Id_inventarioBulk param1,java.lang.Integer param2) throws it.cnr.jada.comp.ComponentException;

/** 
  *  Modifica di un Inventario
  *    PreCondition:
  *      E' stata generata la richiesta di modificare un Inventario. 
  *		Le modifiche passano la validazione, (metodo validaId_Inventario).
  *    PostCondition:
  *      Viene consentito il salvataggio.
  *
  *  Modifica non valida.
  *    PreCondition:
  *      ValidaId_Inventario non superato
  *    PostCondition:
  *      Viene inviato un messaggio : "Attenzione questa modifica non è consentita".
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> il Bulk da modificare
  *
  * @return l'oggetto <code>OggettoBulk</code> modificato
**/

public abstract it.cnr.jada.bulk.OggettoBulk modificaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
}