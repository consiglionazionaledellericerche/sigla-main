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

package it.cnr.contab.anagraf00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Classificazione_anagKey extends OggettoBulk implements KeyedPersistent {
	// CD_CLASSIFIC_ANAG VARCHAR(10) NOT NULL (PK)
	private java.lang.String cd_classific_anag;

public Classificazione_anagKey() {
	super();
}
public Classificazione_anagKey(java.lang.String cd_classific_anag) {
	super();
	this.cd_classific_anag = cd_classific_anag;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Classificazione_anagKey)) return false;
	Classificazione_anagKey k = (Classificazione_anagKey)o;
	if(!compareKey(getCd_classific_anag(),k.getCd_classific_anag())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_classific_anag
 */
public java.lang.String getCd_classific_anag() {
	return cd_classific_anag;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_classific_anag());
}
/* 
 * Setter dell'attributo cd_classific_anag
 */
public void setCd_classific_anag(java.lang.String cd_classific_anag) {
	this.cd_classific_anag = cd_classific_anag;
}
}
