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

public class Classificazione_coriKey extends OggettoBulk implements KeyedPersistent {
	// CD_CLASSIFICAZIONE_CORI CHAR(2) NOT NULL (PK)
	private java.lang.String cd_classificazione_cori;

public Classificazione_coriKey() {
	super();
}
public Classificazione_coriKey(java.lang.String cd_classificazione_cori) {
	super();
	this.cd_classificazione_cori = cd_classificazione_cori;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Classificazione_coriKey)) return false;
	Classificazione_coriKey k = (Classificazione_coriKey)o;
	if(!compareKey(getCd_classificazione_cori(),k.getCd_classificazione_cori())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_classificazione_cori
 */
public java.lang.String getCd_classificazione_cori() {
	return cd_classificazione_cori;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_classificazione_cori());
}
/* 
 * Setter dell'attributo cd_classificazione_cori
 */
public void setCd_classificazione_cori(java.lang.String cd_classificazione_cori) {
	this.cd_classificazione_cori = cd_classificazione_cori;
}
}
