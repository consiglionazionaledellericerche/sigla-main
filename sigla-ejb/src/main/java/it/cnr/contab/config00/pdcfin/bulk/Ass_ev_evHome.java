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

package it.cnr.contab.config00.pdcfin.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.comp.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

import java.util.*;

/**
 * Home che gestisce associazioni tra elementi voce.
 */
public class Ass_ev_evHome extends BulkHome {
protected Ass_ev_evHome(Class clazz,java.sql.Connection connection) {
	super(clazz,connection);
}
protected Ass_ev_evHome(Class clazz,java.sql.Connection connection,PersistentCache persistentCache) {
	super(clazz,connection,persistentCache);
}
/**
 * <!-- @TODO: da completare -->
 * Costruisce un Ass_ev_evHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 */
public Ass_ev_evHome(java.sql.Connection conn) {
	super(Ass_ev_evBulk.class,conn);
}
/**
 * <!-- @TODO: da completare -->
 * Costruisce un Ass_ev_evHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 * @param persistentCache	La PersistentCache in cui vengono cachati gli oggetti persistenti caricati da questo Home
 */
public Ass_ev_evHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Ass_ev_evBulk.class,conn,persistentCache);
}
/**
 * Carica in un dictionary l'elenco dei possibili valori che può
 * assumere la natura.
 * @param bulk L'OggettoBulk in uso.
 * @return I valori della natura.
 */
public Dictionary loadNaturaKeys(OggettoBulk bulk) throws ApplicationException 
{
	return new NaturaHome( getConnection()).loadNaturaKeys( bulk );
}
}
