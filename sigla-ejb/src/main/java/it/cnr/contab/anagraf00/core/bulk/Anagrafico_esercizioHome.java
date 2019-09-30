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

package it.cnr.contab.anagraf00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Anagrafico_esercizioHome extends BulkHome {
public Anagrafico_esercizioHome(java.sql.Connection conn) {
	super(Anagrafico_esercizioBulk.class,conn);
}
public Anagrafico_esercizioHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Anagrafico_esercizioBulk.class,conn,persistentCache);
}
/**
 * Verifica las presenza dell'Anagrafico passato per l'esercizio di scrivania. Se non esitono
 *	corrispondenze, restituisce un nuovo Anagrafico_esercizio.
 *
 * @param anagrafico l'anagrafica in uso.
 *
 * @return anagrafico_esercizio l'<code>Anagrafico_esercizioBulk</code> trovato
 *
**/
public Anagrafico_esercizioBulk findAnagrafico_esercizioFor(it.cnr.jada.UserContext userContext,AnagraficoBulk anagrafico, Integer esercizio) 
	throws IntrospectionException, PersistencyException {
		
	String schema = it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema();
	SQLBroker broker = null;
	 
		
	SQLBuilder sql = createSQLBuilder();
	sql.addSQLClause("AND","CD_ANAG",sql.EQUALS,anagrafico.getCd_anag());
	sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS,esercizio);

	Anagrafico_esercizioBulk anagrafico_esercizio = null;
	broker = createBroker(sql);
	if (broker.next()){
		anagrafico_esercizio = (Anagrafico_esercizioBulk)fetch(broker);
	} 
	
	getHomeCache().fetchAll(userContext);
	broker.close();
	
	return anagrafico_esercizio;	
	
}
}
