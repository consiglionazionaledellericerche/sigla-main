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

package it.cnr.contab.logregistry00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Log_registryKey extends OggettoBulk  implements KeyedPersistent {
	// NOME_TABLE_SRC VARCHAR(30) NOT NULL (PK)
	private java.lang.String nome_table_src;

public Log_registryKey() {
	super();
}
public Log_registryKey(java.lang.String nome_table_src) {
	super();
	this.nome_table_src = nome_table_src;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Log_registryKey)) return false;
	Log_registryKey k = (Log_registryKey)o;
	if(!compareKey(getNome_table_src(),k.getNome_table_src())) return false;
	return true;
}
/* 
 * Getter dell'attributo nome_table_src
 */
public java.lang.String getNome_table_src() {
	return nome_table_src;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getNome_table_src());
}
/* 
 * Setter dell'attributo nome_table_src
 */
public void setNome_table_src(java.lang.String nome_table_src) {
	this.nome_table_src = nome_table_src;
}
}
