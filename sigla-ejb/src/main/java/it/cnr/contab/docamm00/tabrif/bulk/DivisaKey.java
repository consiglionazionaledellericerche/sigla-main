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

public class DivisaKey extends OggettoBulk implements KeyedPersistent {
	// CD_DIVISA VARCHAR(10) NOT NULL (PK)
	private java.lang.String cd_divisa;

/* 
 * Getter dell'attributo cd_divisa
 */
public java.lang.String getCd_divisa() {
	return cd_divisa;
}

/* 
 * Setter dell'attributo cd_divisa
 */
public void setCd_divisa(java.lang.String cd_divisa) {
	this.cd_divisa = cd_divisa;
}

public DivisaKey() {
	super();
}


public DivisaKey(java.lang.String cd_divisa) {
	super();
	this.cd_divisa = cd_divisa;
}

public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof DivisaKey)) return false;
	DivisaKey k = (DivisaKey)o;
	if(!compareKey(getCd_divisa(),k.getCd_divisa())) return false;
	return true;
}

public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_divisa());
}

}
