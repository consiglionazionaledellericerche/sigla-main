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

package it.cnr.contab.inventario00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Condizione_beneKey extends OggettoBulk implements KeyedPersistent {
	// CD_CONDIZIONE_BENE VARCHAR(10) NOT NULL (PK)
	private java.lang.String cd_condizione_bene;

public Condizione_beneKey() {
	super();
}
public Condizione_beneKey(java.lang.String cd_condizione_bene) {
	super();
	this.cd_condizione_bene = cd_condizione_bene;
}
public boolean equals(Object o) {
	if (this == o) return true;
	if (!(o instanceof Condizione_beneKey)) return false;
	Condizione_beneKey k = (Condizione_beneKey)o;
	if(!compareKey(getCd_condizione_bene(),k.getCd_condizione_bene())) return false;
	return true;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Condizione_beneKey)) return false;
	Condizione_beneKey k = (Condizione_beneKey)o;
	if(!compareKey(getCd_condizione_bene(),k.getCd_condizione_bene())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_condizione_bene
 */
public java.lang.String getCd_condizione_bene() {
	return cd_condizione_bene;
}
public int hashCode() {
	return
		calculateKeyHashCode(getCd_condizione_bene());
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_condizione_bene());
}
/* 
 * Setter dell'attributo cd_condizione_bene
 */
public void setCd_condizione_bene(java.lang.String cd_condizione_bene) {
	this.cd_condizione_bene = cd_condizione_bene;
}
}
