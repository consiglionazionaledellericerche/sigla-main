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

package it.cnr.contab.fondiric00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Fondo_attivita_vincolataKey extends OggettoBulk implements KeyedPersistent {
	// CD_FONDO VARCHAR(10) NOT NULL (PK)
	private java.lang.String cd_fondo;

public Fondo_attivita_vincolataKey() {
	super();
}
public Fondo_attivita_vincolataKey(java.lang.String cd_fondo) {
	super();
	this.cd_fondo = cd_fondo;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Fondo_attivita_vincolataKey)) return false;
	Fondo_attivita_vincolataKey k = (Fondo_attivita_vincolataKey)o;
	if(!compareKey(getCd_fondo(),k.getCd_fondo())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_fondo
 */
public java.lang.String getCd_fondo() {
	return cd_fondo;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_fondo());
}
/* 
 * Setter dell'attributo cd_fondo
 */
public void setCd_fondo(java.lang.String cd_fondo) {
	this.cd_fondo = cd_fondo;
}
}
