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

package it.cnr.contab.compensi00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Gruppo_crKey extends OggettoBulk implements KeyedPersistent {
	// ESERCIZIO DECIMAL(4,0) NOT NULL (PK)
	private java.lang.Integer esercizio;

	// CD_GRUPPO_CR VARCHAR(10) NOT NULL (PK)
	private java.lang.String cd_gruppo_cr;

public Gruppo_crKey() {
	super();
}
public Gruppo_crKey(java.lang.Integer esercizio, java.lang.String cd_gruppo_cr) {
	this.esercizio = esercizio;
	this.cd_gruppo_cr = cd_gruppo_cr;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Gruppo_crKey)) return false;
	Gruppo_crKey k = (Gruppo_crKey)o;
	if(!compareKey(getCd_gruppo_cr(),k.getCd_gruppo_cr())) return false;
	if(!compareKey(getEsercizio(),k.getEsercizio())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_gruppo_cr
 */
public java.lang.String getCd_gruppo_cr() {
	return cd_gruppo_cr;
}
/* 
 * Getter dell'attributo esercizio
 */
public java.lang.Integer getEsercizio() {
	return esercizio;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_gruppo_cr())+
		calculateKeyHashCode(getEsercizio());
}
/* 
 * Setter dell'attributo cd_gruppo_cr
 */
public void setCd_gruppo_cr(java.lang.String cd_gruppo_cr) {
	this.cd_gruppo_cr = cd_gruppo_cr;
}
/* 
 * Setter dell'attributo esercizio
 */
public void setEsercizio(java.lang.Integer esercizio) {
	this.esercizio = esercizio;
}
}
