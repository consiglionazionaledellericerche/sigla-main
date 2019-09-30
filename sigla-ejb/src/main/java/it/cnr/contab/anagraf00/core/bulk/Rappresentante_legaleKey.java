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

package it.cnr.contab.anagraf00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Rappresentante_legaleKey extends OggettoBulk implements KeyedPersistent {
	// PG_RAPP_LEGALE DECIMAL(10,0) NOT NULL (PK)
	private java.lang.Long pg_rapp_legale;

public Rappresentante_legaleKey() {
	super();
}
public Rappresentante_legaleKey(java.lang.Long pg_rapp_legale) {
	super();
	this.pg_rapp_legale = pg_rapp_legale;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Rappresentante_legaleKey)) return false;
	Rappresentante_legaleKey k = (Rappresentante_legaleKey)o;
	if(!compareKey(getPg_rapp_legale(),k.getPg_rapp_legale())) return false;
	return true;
}
/* 
 * Getter dell'attributo pg_rapp_legale
 */
public java.lang.Long getPg_rapp_legale() {
	return pg_rapp_legale;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getPg_rapp_legale());
}
/* 
 * Setter dell'attributo pg_rapp_legale
 */
public void setPg_rapp_legale(java.lang.Long pg_rapp_legale) {
	this.pg_rapp_legale = pg_rapp_legale;
}
}
