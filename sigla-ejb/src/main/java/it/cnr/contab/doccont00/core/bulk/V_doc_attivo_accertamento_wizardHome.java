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

import it.cnr.contab.docamm00.docs.bulk.Tipo_documento_ammBulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.SQLBuilder;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

public class V_doc_attivo_accertamento_wizardHome extends V_doc_attivo_accertamentoHome {
	public V_doc_attivo_accertamento_wizardHome(java.sql.Connection conn) {
		super(V_doc_attivo_accertamento_wizardBulk.class,conn);
	}
	public V_doc_attivo_accertamento_wizardHome(java.sql.Connection conn,it.cnr.jada.persistency.PersistentCache persistentCache) {
		super(V_doc_attivo_accertamento_wizardBulk.class,conn,persistentCache);
	}
}
