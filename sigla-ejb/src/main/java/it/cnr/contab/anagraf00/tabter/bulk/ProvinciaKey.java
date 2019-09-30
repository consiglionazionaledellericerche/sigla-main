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

package it.cnr.contab.anagraf00.tabter.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class ProvinciaKey extends OggettoBulk implements KeyedPersistent {
	// CD_PROVINCIA VARCHAR(10) NOT NULL (PK)
	private java.lang.String cd_provincia;

public ProvinciaKey() {
	super();
}
public ProvinciaKey(java.lang.String cd_provincia) {
	super();
	this.cd_provincia = cd_provincia;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof ProvinciaKey)) return false;
	ProvinciaKey k = (ProvinciaKey)o;
	if(!compareKey(getCd_provincia(),k.getCd_provincia())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_provincia
 */
public java.lang.String getCd_provincia() {
	return cd_provincia;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_provincia());
}
/* 
 * Setter dell'attributo cd_provincia
 */
public void setCd_provincia(java.lang.String cd_provincia) {
	this.cd_provincia = cd_provincia;
}
}
