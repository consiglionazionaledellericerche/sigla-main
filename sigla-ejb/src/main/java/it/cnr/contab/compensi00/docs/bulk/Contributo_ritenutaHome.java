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

package it.cnr.contab.compensi00.docs.bulk;

import it.cnr.contab.compensi00.tabrif.bulk.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Contributo_ritenutaHome extends BulkHome {
public Contributo_ritenutaHome(java.sql.Connection conn) {
	super(Contributo_ritenutaBulk.class,conn);
}
public Contributo_ritenutaHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Contributo_ritenutaBulk.class,conn,persistentCache);
}
public java.util.Collection loadContributiRitenute(CompensoBulk compenso) throws PersistencyException {

	SQLBuilder sql = createSQLBuilder();

	sql.addSQLClause("AND","CD_CDS",sql.EQUALS,compenso.getCd_cds());
	sql.addSQLClause("AND","CD_UNITA_ORGANIZZATIVA",sql.EQUALS,compenso.getCd_unita_organizzativa());
	sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS,compenso.getEsercizio());
	sql.addSQLClause("AND","PG_COMPENSO",sql.EQUALS,compenso.getPg_compenso());

	java.util.List l = fetchAll(sql);
	for (java.util.Iterator i = l.iterator(); i.hasNext();){
		Contributo_ritenutaBulk contributo = (Contributo_ritenutaBulk)i.next();
		contributo.setTipoContributoRitenuta(loadTipoContributoRitenuta(contributo));
		contributo.setDettagli(loadDettagliContributo(contributo));
	}

	return l;
}
public java.util.Collection loadDettagliContributo(Contributo_ritenutaBulk contributo) throws PersistencyException {

	Contributo_ritenuta_detHome dettHome = (Contributo_ritenuta_detHome)getHomeCache().getHome(Contributo_ritenuta_detBulk.class);
	return dettHome.loadDettagliContributo(contributo);
}
public Tipo_contributo_ritenutaBulk loadTipoContributoRitenuta(Contributo_ritenutaBulk contributo) throws PersistencyException{

	Tipo_contributo_ritenutaHome home = (Tipo_contributo_ritenutaHome)getHomeCache().getHome(Tipo_contributo_ritenutaBulk.class);
	return (Tipo_contributo_ritenutaBulk)home.findByPrimaryKey(new Tipo_contributo_ritenutaBulk(contributo.getCd_contributo_ritenuta(), contributo.getDt_ini_validita()));
}
}
