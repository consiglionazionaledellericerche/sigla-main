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

public class Tipo_consegnaKey extends OggettoBulk implements KeyedPersistent {
	// CD_TIPO_CONSEGNA VARCHAR(10) NOT NULL (PK)
	private java.lang.String cd_tipo_consegna;

public Tipo_consegnaKey() {
	super();
}
public Tipo_consegnaKey(java.lang.String cd_tipo_consegna) {
	super();
	this.cd_tipo_consegna = cd_tipo_consegna;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Tipo_consegnaKey)) return false;
	Tipo_consegnaKey k = (Tipo_consegnaKey)o;
	if(!compareKey(getCd_tipo_consegna(),k.getCd_tipo_consegna())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_tipo_consegna
 */
public java.lang.String getCd_tipo_consegna() {
	return cd_tipo_consegna;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_tipo_consegna());
}
/* 
 * Setter dell'attributo cd_tipo_consegna
 */
public void setCd_tipo_consegna(java.lang.String cd_tipo_consegna) {
	this.cd_tipo_consegna = cd_tipo_consegna;
}
}
