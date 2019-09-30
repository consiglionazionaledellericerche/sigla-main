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

public class Attivita_commercialeKey extends OggettoBulk implements KeyedPersistent {
	// CD_ATTIVITA_COMMERCIALE VARCHAR(10) NOT NULL (PK)
	private java.lang.String cd_attivita_commerciale;

/* 
 * Getter dell'attributo cd_attivita_commerciale
 */
public java.lang.String getCd_attivita_commerciale() {
	return cd_attivita_commerciale;
}

/* 
 * Setter dell'attributo cd_attivita_commerciale
 */
public void setCd_attivita_commerciale(java.lang.String cd_attivita_commerciale) {
	this.cd_attivita_commerciale = cd_attivita_commerciale;
}

public Attivita_commercialeKey() {
	super();
}


public Attivita_commercialeKey(java.lang.String cd_attivita_commerciale) {
	super();
	this.cd_attivita_commerciale = cd_attivita_commerciale;
}

public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Attivita_commercialeKey)) return false;
	Attivita_commercialeKey k = (Attivita_commercialeKey)o;
	if(!compareKey(getCd_attivita_commerciale(),k.getCd_attivita_commerciale())) return false;
	return true;
}

public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_attivita_commerciale());
}

}
