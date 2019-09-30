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

package it.cnr.contab.doccont00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Tipo_bolloKey extends OggettoBulk implements KeyedPersistent {
	// CD_TIPO_BOLLO VARCHAR(5) NOT NULL (PK)
	private java.lang.String cd_tipo_bollo;

public Tipo_bolloKey() {
	super();
}
public Tipo_bolloKey(java.lang.String cd_tipo_bollo) {
	super();
	this.cd_tipo_bollo = cd_tipo_bollo;
}
public boolean equals(Object o) {
	if (this == o) return true;
	if (!(o instanceof Tipo_bolloKey)) return false;
	Tipo_bolloKey k = (Tipo_bolloKey)o;
	if(!compareKey(getCd_tipo_bollo(),k.getCd_tipo_bollo())) return false;
	return true;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Tipo_bolloKey)) return false;
	Tipo_bolloKey k = (Tipo_bolloKey)o;
	if(!compareKey(getCd_tipo_bollo(),k.getCd_tipo_bollo())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_tipo_bollo
 */
public java.lang.String getCd_tipo_bollo() {
	return cd_tipo_bollo;
}
public int hashCode() {
	return
		calculateKeyHashCode(getCd_tipo_bollo());
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_tipo_bollo());
}
/* 
 * Setter dell'attributo cd_tipo_bollo
 */
public void setCd_tipo_bollo(java.lang.String cd_tipo_bollo) {
	this.cd_tipo_bollo = cd_tipo_bollo;
}
}
