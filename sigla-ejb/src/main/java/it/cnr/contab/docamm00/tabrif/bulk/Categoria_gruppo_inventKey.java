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

public class Categoria_gruppo_inventKey extends OggettoBulk implements KeyedPersistent {
	// CD_CATEGORIA_GRUPPO VARCHAR(10) NOT NULL (PK)
	private java.lang.String cd_categoria_gruppo;

public Categoria_gruppo_inventKey() {
	super();
}
public Categoria_gruppo_inventKey(java.lang.String cd_categoria_gruppo) {
	this.cd_categoria_gruppo = cd_categoria_gruppo;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Categoria_gruppo_inventKey)) return false;
	Categoria_gruppo_inventKey k = (Categoria_gruppo_inventKey)o;
	if(!compareKey(getCd_categoria_gruppo(),k.getCd_categoria_gruppo())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_categoria_gruppo
 */
public java.lang.String getCd_categoria_gruppo() {
	return cd_categoria_gruppo;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_categoria_gruppo());
}
/* 
 * Setter dell'attributo cd_categoria_gruppo
 */
public void setCd_categoria_gruppo(java.lang.String cd_categoria_gruppo) {
	this.cd_categoria_gruppo = cd_categoria_gruppo;
}
}
