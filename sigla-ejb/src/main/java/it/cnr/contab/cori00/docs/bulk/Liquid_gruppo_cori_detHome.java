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

package it.cnr.contab.cori00.docs.bulk;

import it.cnr.contab.doccont00.core.bulk.MandatoBulk;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

import java.util.Collection;

public class Liquid_gruppo_cori_detHome extends BulkHome {
/**
  *  Costruttore utilizzato dalla sottoclasse <code>Liquid_gruppo_cori_detIHome</code>.
  *
**/  
public Liquid_gruppo_cori_detHome(Class clazz, java.sql.Connection conn) {
	super(clazz,conn);
}
	/**
	  *  Costruttore utilizzato dalla sottoclasse <code>Liquid_gruppo_cori_detIHome</code>.
	  *
	**/
	public Liquid_gruppo_cori_detHome(Class clazz, java.sql.Connection conn,PersistentCache persistentCache) {
		super(clazz,conn,persistentCache);
	}
	public Liquid_gruppo_cori_detHome(java.sql.Connection conn) {
		super(Liquid_gruppo_cori_detBulk.class,conn);
	}
	public Liquid_gruppo_cori_detHome(java.sql.Connection conn,PersistentCache persistentCache) {
		super(Liquid_gruppo_cori_detBulk.class,conn,persistentCache);
	}
}
