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

public class Tipo_risultatoKey extends OggettoBulk implements KeyedPersistent {
	// CD_TIPO_RISULTATO VARCHAR(10) NOT NULL (PK)
	private java.lang.String cd_tipo_risultato;

public Tipo_risultatoKey() {
	super();
}
public Tipo_risultatoKey(java.lang.String cd_tipo_risultato) {
	super();
	this.cd_tipo_risultato = cd_tipo_risultato;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Tipo_risultatoKey)) return false;
	Tipo_risultatoKey k = (Tipo_risultatoKey)o;
	if(!compareKey(getCd_tipo_risultato(),k.getCd_tipo_risultato())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_tipo_risultato
 */
public java.lang.String getCd_tipo_risultato() {
	return cd_tipo_risultato;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_tipo_risultato());
}
/* 
 * Setter dell'attributo cd_tipo_risultato
 */
public void setCd_tipo_risultato(java.lang.String cd_tipo_risultato) {
	this.cd_tipo_risultato = cd_tipo_risultato;
}
}
