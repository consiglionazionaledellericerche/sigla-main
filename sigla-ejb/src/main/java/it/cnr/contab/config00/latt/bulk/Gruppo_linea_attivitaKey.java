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

package it.cnr.contab.config00.latt.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Gruppo_linea_attivitaKey extends OggettoBulk implements KeyedPersistent {
	// CD_GRUPPO_LINEA_ATTIVITA VARCHAR(10) NOT NULL (PK)
	private java.lang.String cd_gruppo_linea_attivita;

public Gruppo_linea_attivitaKey() {
	super();
}
public Gruppo_linea_attivitaKey(java.lang.String cd_gruppo_linea_attivita) {
	super();
	this.cd_gruppo_linea_attivita = cd_gruppo_linea_attivita;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Gruppo_linea_attivitaKey)) return false;
	Gruppo_linea_attivitaKey k = (Gruppo_linea_attivitaKey)o;
	if(!compareKey(getCd_gruppo_linea_attivita(),k.getCd_gruppo_linea_attivita())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_gruppo_linea_attivita
 */
public java.lang.String getCd_gruppo_linea_attivita() {
	return cd_gruppo_linea_attivita;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_gruppo_linea_attivita());
}
/* 
 * Setter dell'attributo cd_gruppo_linea_attivita
 */
public void setCd_gruppo_linea_attivita(java.lang.String cd_gruppo_linea_attivita) {
	this.cd_gruppo_linea_attivita = cd_gruppo_linea_attivita;
}
}
