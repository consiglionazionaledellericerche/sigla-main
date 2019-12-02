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

import java.util.List;
import java.util.Optional;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;

public class Tipo_documento_genericoHome extends BulkHome {
	private static final long serialVersionUID = 1L;

	public Tipo_documento_genericoHome(java.sql.Connection conn) {
		super(Tipo_documento_genericoBulk.class,conn);
	}
	public Tipo_documento_genericoHome(java.sql.Connection conn,PersistentCache persistentCache) {
		super(Tipo_documento_genericoBulk.class,conn,persistentCache);
	}
	public Tipo_documento_genericoBulk findByCodiceAndTipo(String codice, String tipo) throws PersistencyException, ComponentException {
		SQLBuilder sql = this.createSQLBuilder();
		Optional.ofNullable(codice).ifPresent(el->sql.addClause(FindClause.AND, "codice", SQLBuilder.EQUALS, el));
		Optional.ofNullable(tipo).ifPresent(el->sql.addClause(FindClause.AND, "tipoDocumento", SQLBuilder.EQUALS, el));

		List<Tipo_documento_genericoBulk> l = this.fetchAll(sql);
		if (l.stream().count()>1)
			throw new ApplicationException("Errore nei dati: esistono per il codice "+codice+" e per il tipo "+tipo+" più righe valide!");
		
		return l.stream().findFirst().orElse(null);
	}
	
}
