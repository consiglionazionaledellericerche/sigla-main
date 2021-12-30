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

package it.cnr.contab.doccont00.core.bulk;

import java.util.*;
import it.cnr.contab.docamm00.docs.bulk.Tipo_documento_ammBulk;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class V_doc_attivo_accertamentoHome extends BulkHome {
	public V_doc_attivo_accertamentoHome(Class clazz, java.sql.Connection conn) {
		super(clazz, conn);
	}
	public V_doc_attivo_accertamentoHome(Class clazz, java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
		super(clazz, conn, persistentCache);
	}
	public V_doc_attivo_accertamentoHome(java.sql.Connection conn) {
		super(V_doc_attivo_accertamentoBulk.class,conn);
	}
	public V_doc_attivo_accertamentoHome(java.sql.Connection conn,PersistentCache persistentCache) {
		super(V_doc_attivo_accertamentoBulk.class,conn,persistentCache);
	}
	public java.util.Hashtable loadTipoDocumentoKeys( V_doc_attivo_accertamentoBulk bulk ) throws PersistencyException
	{
		SQLBuilder sql = getHomeCache().getHome( Tipo_documento_ammBulk.class ).createSQLBuilder();
		sql.addClause( "AND", "ti_entrata_spesa", sql.EQUALS, "E" );
		List result = getHomeCache().getHome( Tipo_documento_ammBulk.class ).fetchAll( sql );
		Hashtable ht = new Hashtable();
		Tipo_documento_ammBulk tipo;
		for (Iterator i = result.iterator(); i.hasNext(); )
		{
			tipo = (Tipo_documento_ammBulk) i.next();
			ht.put( tipo.getCd_tipo_documento_amm(), tipo.getDs_tipo_documento_amm());
		}
		return ht;
	}
}
