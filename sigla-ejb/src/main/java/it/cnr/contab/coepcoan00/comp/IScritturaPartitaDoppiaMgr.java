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

package it.cnr.contab.coepcoan00.comp;

import java.util.*;
import java.util.Vector;
import it.cnr.contab.coepcoan00.core.bulk.*;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.ICRUDMgr;
public interface IScritturaPartitaDoppiaMgr extends ICRUDMgr
{

/**
 * Nome: Ricerca dell'attributo relativo al terzo con codice '0'
 * Pre:  E' stata richiesta la ricerca di un terzo e l'utente ha specificato come codice
 *       il valore '0' ad indicare tutte le classificazioni anagrafiche
 * Post: Viene restituito un RemoteIterator contenente solamente l'oggetto fittizio ( con codice '0' ) che rappresenta
 *       tutte le classificazioni anagrafiche
 *
 * Nome: Ricerca dell'attributo relativo al terzo con codice '0'
 * Pre:  E' stata richiesta la ricerca di un terzo e l'utente ha specificato come codice
 *       un valore diverso da '0' 
 * Post: Viene restituito un RemoteIterator contenente la lista di oggetti di tipo TerzoBulk
 *       risultante dall'esecuzione della query sul database
 *
 * Nome: Ricerca di un attributo diverso da terzo
 * Pre:  E' stata richiesta la ricerca di un attributo diverso da 'terzo'
 * Post: Viene restituito un RemoteIterator contenente la lista degli oggettiBulk 
 *       risultante dall'esecuzione della query sul database
 * 
 *
 * @param userContext <code>UserContext</code> 
 * @param clausole <code>CompoundFindClause</code>  clausole specificate dall'utente
 * @param bulk <code>OggettoBulk</code>  oggettoBulk da ricercare
 * @param contesto <code>Scrittura_partita_doppiaBulk</code>  contesto della ricerca
 * @param attributo nome dell'attributo del contesto che deve essere ricercato
 * @return <code>RemoteIterator</code>  elenco di oggetti trovati
 *
 */

public abstract it.cnr.jada.util.RemoteIterator cerca(it.cnr.jada.UserContext param0,it.cnr.jada.persistency.sql.CompoundFindClause param1,it.cnr.jada.bulk.OggettoBulk param2,it.cnr.jada.bulk.OggettoBulk param3,java.lang.String param4) throws it.cnr.jada.comp.ComponentException;
/**
 *
 * Nome: Inizializzazione di una scrittura
 * Pre:  E' stata richiesta l'inizializzazione per inserimento di una scrittura in partita doppia 
 * Post: La scrittura viene restituita con inizializzata la data di contabilizzazione (metodo inizializzaDataContabilizzazione)
 *       e il cds (con il cds di scrivania)
 *
 * @param userContext <code>UserContext</code> 
 * @param bulk <code>Scrittura_partita_doppiaBulk</code>  che deve essere inizializzato per inserimento
 * @return <code>Scrittura_partita_doppiaBulk</code>  inizializzato per inserimento
 *
 */

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerInserimento(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/**
 *
 * Nome: Inizializzazione di una scrittura
 * Pre:  E' stata richiesta l'inizializzazione per modifica di una scrittura in partita doppia 
 * Post: La scrittura viene restituita con inizializzata la collezione di movimenti dare e movimenti avere
 *
 * Nome: Inizializzazione di un movimento
 * Pre:  E' stata richiesta l'inizializzazione per visualizzazione di un movimento coge
 * Post: Il movimento viene restituito con l'inizializzazione di default
 *
 * Nome: Inizializzazione di un saldo
 * Pre:  E' stata richiesta l'inizializzazione per visualizzazione di un saldo coge
 * Post: Il saldo viene restituito con l'inizializzazione di default
 *
 * @param userContext <code>UserContext</code> 
 * @param bulk <code>Scrittura_partita_doppiaBulk</code> oppure <code>Saldo_cogeBulk</code> oppure <code>Movimento_cogeBulk</code> che devono essere inizializzati per modifica
 * @return <code>Scrittura_partita_doppiaBulk</code> oppure <code>Saldo_cogeBulk</code> oppure <code>Movimento_cogeBulk</code> inizializzati per modifica
 *
 */

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerModifica(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/**
 *
 * Nome: Inizializzazione di una scrittura
 * Pre:  E' stata richiesta l'inizializzazione per eseguire una ricerca di una scrittura in partita doppia 
 * Post: La scrittura viene restituita con inizializzato come Cds quello di scrivania
 *
 * Nome: Inizializzazione di un movimento
 * Pre:  E' stata richiesta l'inizializzazione per eseguire una ricerca di un movimento coge
 * Post: Il movimento viene restituito con inizializzato come Cds quello di scrivania
 *
 * Nome: Inizializzazione di un saldo
 * Pre:  E' stata richiesta l'inizializzazione per eseguire una ricerca di un saldo coge
 * Post: Il saldo viene restituito con inizializzato come Cds quello di scrivania
 *
 * @param userContext <code>UserContext</code> 
 * @param bulk <code>Scrittura_partita_doppiaBulk</code> oppure <code>Saldo_cogeBulk</code> oppure <code>Movimento_cogeBulk</code> che devono essere inizializzati per ricerca
 * @return <code>Scrittura_partita_doppiaBulk</code> oppure <code>Saldo_cogeBulk</code> oppure <code>Movimento_cogeBulk</code> inizializzati per ricerca
 *
 */

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerRicerca(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/**
 *
 * Nome: Inizializzazione di una scrittura
 * Pre:  E' stata richiesta l'inizializzazione per eseguire una ricerca libera di una scrittura in partita doppia 
 * Post: La scrittura viene restituita con inizializzato come Cds quello di scrivania
 *
 * Nome: Inizializzazione di un movimento
 * Pre:  E' stata richiesta l'inizializzazione per eseguire una ricerca libera di un movimento coge
 * Post: Il movimento viene restituito con inizializzato come Cds quello di scrivania
 *
 * Nome: Inizializzazione di un saldo
 * Pre:  E' stata richiesta l'inizializzazione per eseguire una ricerca libera di un saldo coge
 * Post: Il saldo viene restituito con inizializzato come Cds quello di scrivania
 *
 * @param userContext <code>UserContext</code> 
 * @param bulk <code>Scrittura_partita_doppiaBulk</code> oppure <code>Saldo_cogeBulk</code> oppure <code>Movimento_cogeBulk</code> che devono essere inizializzati per ricerca
 * @return <code>Scrittura_partita_doppiaBulk</code> oppure <code>Saldo_cogeBulk</code> oppure <code>Movimento_cogeBulk</code> inizializzati per ricerca
 *
 */

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerRicercaLibera(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
}
