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

public class Codici_altra_forma_ass_inpsKey extends OggettoBulk implements KeyedPersistent {
	// ALTRA_ASS_PREVID_INPS VARCHAR(5) NOT NULL (PK)
	private java.lang.String altra_ass_previd_inps;

public Codici_altra_forma_ass_inpsKey() {
	super();
}
public Codici_altra_forma_ass_inpsKey(java.lang.String altra_ass_previd_inps) {
	super();
	this.altra_ass_previd_inps = altra_ass_previd_inps;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Codici_altra_forma_ass_inpsKey)) return false;
	Codici_altra_forma_ass_inpsKey k = (Codici_altra_forma_ass_inpsKey)o;
	if(!compareKey(getAltra_ass_previd_inps(),k.getAltra_ass_previd_inps())) return false;
	return true;
}
/* 
 * Getter dell'attributo altra_ass_previd_inps
 */
public java.lang.String getAltra_ass_previd_inps() {
	return altra_ass_previd_inps;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getAltra_ass_previd_inps());
}
/* 
 * Setter dell'attributo altra_ass_previd_inps
 */
public void setAltra_ass_previd_inps(java.lang.String altra_ass_previd_inps) {
	this.altra_ass_previd_inps = altra_ass_previd_inps;
}
}
