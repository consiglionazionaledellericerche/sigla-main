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

package it.cnr.contab.missioni00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Gruppo_inquadramentoKey extends OggettoBulk implements KeyedPersistent {
	// CD_GRUPPO_INQUADRAMENTO VARCHAR(10) NOT NULL (PK)
	private java.lang.String cd_gruppo_inquadramento;

public Gruppo_inquadramentoKey() {
	super();
}
public Gruppo_inquadramentoKey(java.lang.String cd_gruppo_inquadramento) {
	super();
	this.cd_gruppo_inquadramento = cd_gruppo_inquadramento;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Gruppo_inquadramentoKey)) return false;
	Gruppo_inquadramentoKey k = (Gruppo_inquadramentoKey)o;
	if(!compareKey(getCd_gruppo_inquadramento(),k.getCd_gruppo_inquadramento())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_gruppo_inquadramento
 */
public java.lang.String getCd_gruppo_inquadramento() {
	return cd_gruppo_inquadramento;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_gruppo_inquadramento());
}
/* 
 * Setter dell'attributo cd_gruppo_inquadramento
 */
public void setCd_gruppo_inquadramento(java.lang.String cd_gruppo_inquadramento) {
	this.cd_gruppo_inquadramento = cd_gruppo_inquadramento;
}
}
