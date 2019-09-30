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

public interface IScritturaAnaliticaMgr extends it.cnr.jada.comp.ICRUDMgr {
/**
 *
 * Nome: Inizializzazione di una scrittura
 * Pre:  E' stata richiesta l'inizializzazione per inserimento di una scrittura analitica 
 * Post: La scrittura viene restituita con inizializzata la data di contabilizzazione (metodo inizializzaDataContabilizzazione)
 *       e il cds (con il cds di scrivania)
 *
 * @param userContext <code>UserContext</code> 
 * @param bulk <code>Scrittura_analiticaBulk</code>  che deve essere inizializzato per inserimento
 * @return <code>Scrittura_analiticaBulk</code>  inizializzato per inserimento
 *
 */

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerInserimento(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/**
 *
 * Nome: Inizializzazione di una scrittura
 * Pre:  E' stata richiesta l'inizializzazione per modifica di una scrittura analitica
 * Post: La scrittura viene restituita con inizializzata la collezione di movimenti dare e movimenti avere
 *
 * Nome: Inizializzazione di un movimento
 * Pre:  E' stata richiesta l'inizializzazione per visualizzazione di un movimento coan
 * Post: Il movimento viene restituito con l'inizializzazione di default
 *
 * Nome: Inizializzazione di un saldo
 * Pre:  E' stata richiesta l'inizializzazione per visualizzazione di un saldo coan
 * Post: Il saldo viene restituito con l'inizializzazione di default
 *
 * @param userContext <code>UserContext</code> 
 * @param bulk <code>Scrittura_analiticaBulk</code> oppure <code>Saldo_coanBulk</code> oppure <code>Movimento_coanBulk</code> che devono essere inizializzati per modifica
 * @return <code>Scrittura_analiticaBulk</code> oppure <code>Saldo_coanBulk</code> oppure <code>Movimento_coanBulk</code> inizializzati per modifica
 *
 */

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerModifica(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/**
 *
 * Nome: Inizializzazione di una scrittura
 * Pre:  E' stata richiesta l'inizializzazione per eseguire una ricerca di una scrittura analitica 
 * Post: La scrittura viene restituita con inizializzato come Cds quello di scrivania
 *
 * Nome: Inizializzazione di un movimento
 * Pre:  E' stata richiesta l'inizializzazione per eseguire una ricerca di un movimento coan
 * Post: Il movimento viene restituito con inizializzato come Cds quello di scrivania
 *
 * Nome: Inizializzazione di un saldo
 * Pre:  E' stata richiesta l'inizializzazione per eseguire una ricerca di un saldo coan
 * Post: Il saldo viene restituito con inizializzato come Cds quello di scrivania
 *
 * @param userContext <code>UserContext</code> 
 * @param bulk <code>Scrittura_analiticaBulk</code> oppure <code>Saldo_coanBulk</code> oppure <code>Movimento_coanBulk</code> che devono essere inizializzati per ricerca
 * @return <code>Scrittura_analiticaBulk</code> oppure <code>Saldo_coanBulk</code> oppure <code>Movimento_coanBulk</code> inizializzati per ricerca
 *
 */

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerRicerca(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/**
 *
 * Nome: Inizializzazione di una scrittura
 * Pre:  E' stata richiesta l'inizializzazione per eseguire una ricerca libera di una scrittura analitica 
 * Post: La scrittura viene restituita con inizializzato come Cds quello di scrivania
 *
 * Nome: Inizializzazione di un movimento
 * Pre:  E' stata richiesta l'inizializzazione per eseguire una ricerca libera di un movimento coan
 * Post: Il movimento viene restituito con inizializzato come Cds quello di scrivania
 *
 * Nome: Inizializzazione di un saldo
 * Pre:  E' stata richiesta l'inizializzazione per eseguire una ricerca libera di un saldo coan
 * Post: Il saldo viene restituito con inizializzato come Cds quello di scrivania
 *
 * @param userContext <code>UserContext</code> 
 * @param bulk <code>Scrittura_analiticaBulk</code> oppure <code>Saldo_coanBulk</code> oppure <code>Movimento_coanBulk</code> che devono essere inizializzati per ricerca
 * @return <code>Scrittura_analiticaBulk</code> oppure <code>Saldo_coanBulk</code> oppure <code>Movimento_coanBulk</code> inizializzati per ricerca
 *
 */

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerRicercaLibera(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
}
