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

package it.cnr.contab.config00.latt.bulk;

import it.cnr.contab.config00.sto.bulk.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.comp.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Ass_tipo_la_cdrHome extends BulkHome {
/**
 * Costruttore associazione tipo/linea attività/cdr Home
 *
 * @param conn connessione db	
 */
public Ass_tipo_la_cdrHome(java.sql.Connection conn) {
	super(Ass_tipo_la_cdrBulk.class,conn);
}
/**
 * Costrutture associazione tipo/linea attività/cdr Home
 *
 * @param conn connessione db
 * @param persistentCache cache modelli
 */
public Ass_tipo_la_cdrHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Ass_tipo_la_cdrBulk.class,conn,persistentCache);
}
/**
 * Volutamente il cdr con codice '*' non deve essere idrato da db: gestisce l'eccezione di object not found
 * @see it.cnr.jada.persistency.ObjectNotFoundHandler
 */
public void handleObjectNotFoundException(it.cnr.jada.persistency.ObjectNotFoundException e) throws it.cnr.jada.persistency.ObjectNotFoundException {
	if(e.getPersistent() instanceof CdrBulk) {
	 CdrBulk aB = (CdrBulk)e.getPersistent();
	 if(aB.getCd_centro_responsabilita().equals("*"))
	  return;
	}
	
	throw e;
}
}
