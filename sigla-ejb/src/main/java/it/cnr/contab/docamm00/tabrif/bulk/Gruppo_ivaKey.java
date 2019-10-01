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

public class Gruppo_ivaKey extends OggettoBulk implements KeyedPersistent {
	// CD_GRUPPO_IVA VARCHAR(10) NOT NULL (PK)
	private java.lang.String cd_gruppo_iva;

/* 
 * Getter dell'attributo cd_gruppo_iva
 */
public java.lang.String getCd_gruppo_iva() {
	return cd_gruppo_iva;
}

/* 
 * Setter dell'attributo cd_gruppo_iva
 */
public void setCd_gruppo_iva(java.lang.String cd_gruppo_iva) {
	this.cd_gruppo_iva = cd_gruppo_iva;
}

public Gruppo_ivaKey() {
	super();
}


public Gruppo_ivaKey(java.lang.String cd_gruppo_iva) {
	super();
	this.cd_gruppo_iva = cd_gruppo_iva;
}

public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Gruppo_ivaKey)) return false;
	Gruppo_ivaKey k = (Gruppo_ivaKey)o;
	if(!compareKey(getCd_gruppo_iva(),k.getCd_gruppo_iva())) return false;
	return true;
}

public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_gruppo_iva());
}

}
