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

package it.cnr.contab.docamm00.tabrif.bulk;

import java.rmi.RemoteException;

import it.cnr.contab.anagraf00.tabter.bulk.ComuneEsteroHome;
import it.cnr.contab.anagraf00.tabter.bulk.ComuneItalianoHome;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.*;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;

public class DivisaHome extends BulkHome {
public DivisaHome(java.sql.Connection conn) {
	super(DivisaBulk.class,conn);
}
public DivisaHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(DivisaBulk.class,conn,persistentCache);
}
//
// 	Ritorno la divisa di default
//

public DivisaBulk getDivisaDefault(it.cnr.jada.UserContext userContext) throws it.cnr.jada.comp.ComponentException, PersistencyException, javax.ejb.EJBException
{
	String cd_divisa_default = null;

	if(userContext == null)
		return null;		
	
	try {
		cd_divisa_default = ((it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession", it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession.class)).getVal01(userContext, new Integer(0), "*", "CD_DIVISA", "EURO");
	} catch (RemoteException e) {
		throw new ComponentException(e);
	}
	if(cd_divisa_default == null)
		return null;

	DivisaBulk valuta_default = null;
	
	valuta_default = (DivisaBulk) find(new it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk(cd_divisa_default)).get(0);
	if(valuta_default == null)
		return null;

	return valuta_default;
}
	@Override
	public SQLBuilder createSQLBuilder() {
		SQLBuilder sql=super.createSQLBuilder();
		sql.openParenthesis("AND");
	 	sql.addSQLClause("AND", "DT_CANCELLAZIONE", sql.ISNULL, null);
		sql.addSQLClause("OR","DT_CANCELLAZIONE",sql.GREATER,it.cnr.jada.util.ejb.EJBCommonServices.getServerDate());
		sql.closeParenthesis();
		return sql;
	}
}
