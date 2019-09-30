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

package it.cnr.contab.utenze00.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Albero_mainKey extends OggettoBulk implements KeyedPersistent {
	// CD_NODO VARCHAR(100) NOT NULL (PK)
	private java.lang.String cd_nodo;

public Albero_mainKey() {
	super();
}
public Albero_mainKey(java.lang.String cd_nodo) {
	super();
	this.cd_nodo = cd_nodo;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Albero_mainKey)) return false;
	Albero_mainKey k = (Albero_mainKey)o;
	if(!compareKey(getCd_nodo(),k.getCd_nodo())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_nodo
 */
public java.lang.String getCd_nodo() {
	return cd_nodo;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_nodo());
}
/* 
 * Setter dell'attributo cd_nodo
 */
public void setCd_nodo(java.lang.String cd_nodo) {
	this.cd_nodo = cd_nodo;
}
}
