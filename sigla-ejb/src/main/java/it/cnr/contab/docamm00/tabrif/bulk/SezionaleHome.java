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

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class SezionaleHome extends BulkHome {
public SezionaleHome(java.sql.Connection conn) {
	super(SezionaleBulk.class,conn);
}
public SezionaleHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(SezionaleBulk.class,conn,persistentCache);
}
public boolean verificaStatoEsercizio(SezionaleBulk sezionale) throws PersistencyException, IntrospectionException {

	it.cnr.contab.config00.esercizio.bulk.EsercizioBulk esercizio = (it.cnr.contab.config00.esercizio.bulk.EsercizioBulk) getHomeCache().getHome(it.cnr.contab.config00.esercizio.bulk.EsercizioBulk.class).findByPrimaryKey( 
		new it.cnr.contab.config00.esercizio.bulk.EsercizioBulk( sezionale.getCd_cds(), sezionale.getEsercizio()));
	if (esercizio == null || esercizio.STATO_CHIUSO_DEF.equals(esercizio.getSt_apertura_chiusura()))
		return false;
	return true;
}
}
