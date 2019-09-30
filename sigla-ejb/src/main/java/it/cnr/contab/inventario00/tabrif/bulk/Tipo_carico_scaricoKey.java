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

package it.cnr.contab.inventario00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Tipo_carico_scaricoKey extends OggettoBulk implements KeyedPersistent {
	// CD_TIPO_CARICO_SCARICO VARCHAR(10) NOT NULL (PK)
	private java.lang.String cd_tipo_carico_scarico;

public Tipo_carico_scaricoKey() {
	super();
}
public Tipo_carico_scaricoKey(java.lang.String cd_tipo_carico_scarico) {
	super();
	this.cd_tipo_carico_scarico = cd_tipo_carico_scarico;
}
public boolean equals(Object o) {
	if (this == o) return true;
	if (!(o instanceof Tipo_carico_scaricoKey)) return false;
	Tipo_carico_scaricoKey k = (Tipo_carico_scaricoKey)o;
	if(!compareKey(getCd_tipo_carico_scarico(),k.getCd_tipo_carico_scarico())) return false;
	return true;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Tipo_carico_scaricoKey)) return false;
	Tipo_carico_scaricoKey k = (Tipo_carico_scaricoKey)o;
	if(!compareKey(getCd_tipo_carico_scarico(),k.getCd_tipo_carico_scarico())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_tipo_carico_scarico
 */
public java.lang.String getCd_tipo_carico_scarico() {
	return cd_tipo_carico_scarico;
}
public int hashCode() {
	return
		calculateKeyHashCode(getCd_tipo_carico_scarico());
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_tipo_carico_scarico());
}
/* 
 * Setter dell'attributo cd_tipo_carico_scarico
 */
public void setCd_tipo_carico_scarico(java.lang.String cd_tipo_carico_scarico) {
	this.cd_tipo_carico_scarico = cd_tipo_carico_scarico;
}
}
