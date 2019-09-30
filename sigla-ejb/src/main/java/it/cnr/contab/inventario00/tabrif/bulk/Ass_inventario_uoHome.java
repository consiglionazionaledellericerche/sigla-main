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

package it.cnr.contab.inventario00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Ass_inventario_uoHome extends BulkHome {
public Ass_inventario_uoHome(java.sql.Connection conn) {
	super(Ass_inventario_uoBulk.class,conn);
}
public Ass_inventario_uoHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Ass_inventario_uoBulk.class,conn,persistentCache);
}
//^^@@
/**
  *
  *  Carica l'Associazione Inv/UO della UO argomento  *		
  *		Ritorna null se la U.O. non esiste l'Associazione
  *  
 */
//^^@@

public Ass_inventario_uoBulk findAssInvUoFor(it.cnr.jada.UserContext userContext,it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk uo)
	throws PersistencyException, IntrospectionException{

	SQLBuilder sql;
	Broker broker;
	Ass_inventario_uoBulk assInvUo = null;

	sql = createSQLBuilder();
	sql.addSQLClause("AND","CD_CDS",sql.EQUALS, uo.getCd_unita_padre()); // CD_CDS
	sql.addSQLClause("AND","CD_UNITA_ORGANIZZATIVA",sql.EQUALS, uo.getCd_unita_organizzativa()); // CD_UNITA_ORGANIZZATIVA

	broker = createBroker(sql);
	if (broker.next()){
		assInvUo = (Ass_inventario_uoBulk)fetch(broker);
		getHomeCache().fetchAll(userContext);
	}
	broker.close();
	
	return assInvUo;
}
//^^@@
/**
  * 
  *  Carica tutte le Associazioni Inv/UO per l'inventario "inv"
  *  
 */
//^^@@

public java.util.List findAssInvUoFor(Id_inventarioBulk inv) throws PersistencyException, IntrospectionException{

	SQLBuilder sql = createSQLBuilder();
	sql.addSQLClause("AND","PG_INVENTARIO",sql.EQUALS,inv.getPg_inventario());

	return fetchAll(sql);
}
//^^@@
/**
  *
  *  Carica l'Associazione Inv/UO della UO di Scrivania
  *		--> resp: indica se devo cercare l'Associazione responsabile
  *		Ritorna null se la U.O. non esiste l'Associazione
  *  
 */
//^^@@

public Ass_inventario_uoBulk findAssInvUoFor(it.cnr.jada.UserContext userContext, boolean resp)
	throws PersistencyException, IntrospectionException{

	String cdCds = it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(userContext);
	String cdUO = it.cnr.contab.utenze00.bp.CNRUserContext.getCd_unita_organizzativa(userContext);

	return findAssInvUoFor(userContext,cdCds, cdUO, resp);
}
//^^@@
/**
  *
  *  Carica l'Associazione Inv/UO della UO con codice "cdCds" e "cdUO"
  *		--> resp: indica se devo cercare l'Associazione responsabile
  *		Ritorna null se la U.O. non esiste l'Associazione
  *  
 */
//^^@@

public Ass_inventario_uoBulk findAssInvUoFor(it.cnr.jada.UserContext userContext,String  cdCds, String cdUO, boolean resp)
	throws PersistencyException, IntrospectionException{

	SQLBuilder sql;
	Broker broker;
	Ass_inventario_uoBulk assInvUo = null;

	sql = createSQLBuilder();
	sql.addSQLClause("AND","CD_CDS",sql.EQUALS,cdCds);
	sql.addSQLClause("AND","CD_UNITA_ORGANIZZATIVA",sql.EQUALS,cdUO);
	if (resp==true)
		sql.addSQLClause("AND","FL_RESPONSABILE",sql.EQUALS,Boolean.TRUE,java.sql.Types.VARCHAR,0,new CHARToBooleanConverter(),true,false);

	broker = createBroker(sql);
	if (broker.next()){
		assInvUo = (Ass_inventario_uoBulk)fetch(broker);
		getHomeCache().fetchAll(userContext);
		// Se la Uo è responsabile dell'inventario, riempio l'attributo corrispondente
		if (resp==true)
			assInvUo.getInventario().setAssInvUoResp(assInvUo);
	}
	broker.close();
	
	return assInvUo;
}
//^^@@
/**
  *
  *  Carica l'Associazione Inv/UO responsabile dell'Inventario "inv"
  *  
 */
//^^@@

public Ass_inventario_uoBulk findAssInvUoRespFor(it.cnr.jada.UserContext userContext,Id_inventarioBulk inv) throws PersistencyException, IntrospectionException{

	SQLBuilder sql;
	Broker broker;
	Ass_inventario_uoBulk assInvUo = null;

	sql = createSQLBuilder();
	sql.addSQLClause("AND","PG_INVENTARIO",sql.EQUALS,inv.getPg_inventario());
	sql.addSQLClause("AND","FL_RESPONSABILE",sql.EQUALS,Boolean.TRUE,java.sql.Types.VARCHAR,0,new CHARToBooleanConverter(),true,false);

	broker = createBroker(sql);
	if (broker.next()){
		assInvUo = (Ass_inventario_uoBulk)fetch(broker);
		getHomeCache().fetchAll(userContext);
		assInvUo.getInventario().setAssInvUoResp(assInvUo);
	}
	broker.close();
	
	return assInvUo;
}
//^^@@
/**
  *
  *  Carica l'Associazione Inv/UO responsabile della UO di Scrivania
  *  
 */
//^^@@

public Ass_inventario_uoBulk findAssInvUoRespFor(it.cnr.jada.UserContext aUC) throws PersistencyException, IntrospectionException{

	// Recupera l'Associazione Inventario/UO per l'U.O. di scrivania
	//   e per cui risulta responsabile
	// Ritorna null se la U.O. non è responsabile di nessun inventario

	return findAssInvUoFor(aUC, true);
}
}
