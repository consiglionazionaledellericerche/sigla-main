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

public class Tipo_sezionaleKey extends OggettoBulk implements KeyedPersistent {
	// CD_TIPO_SEZIONALE VARCHAR(10) NOT NULL (PK)
	private java.lang.String cd_tipo_sezionale;

public Tipo_sezionaleKey() {
	super();
}
public Tipo_sezionaleKey(java.lang.String cd_tipo_sezionale) {
	super();
	this.cd_tipo_sezionale = cd_tipo_sezionale;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Tipo_sezionaleKey)) return false;
	Tipo_sezionaleKey k = (Tipo_sezionaleKey)o;
	if(!compareKey(getCd_tipo_sezionale(),k.getCd_tipo_sezionale())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_tipo_sezionale
 */
public java.lang.String getCd_tipo_sezionale() {
	return cd_tipo_sezionale;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_tipo_sezionale());
}
/* 
 * Setter dell'attributo cd_tipo_sezionale
 */
public void setCd_tipo_sezionale(java.lang.String cd_tipo_sezionale) {
	this.cd_tipo_sezionale = cd_tipo_sezionale;
}
}
