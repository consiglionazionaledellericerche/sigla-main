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

package it.cnr.contab.config00.pdcfin.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class NaturaKey extends OggettoBulk implements KeyedPersistent {
	// CD_NATURA VARCHAR(1) NOT NULL (PK)
	private java.lang.String cd_natura;

public NaturaKey() {
	super();
}
public NaturaKey(java.lang.String cd_natura) {
	super();
	this.cd_natura = cd_natura;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof NaturaKey)) return false;
	NaturaKey k = (NaturaKey)o;
	if(!compareKey(getCd_natura(),k.getCd_natura())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_natura
 */
public java.lang.String getCd_natura() {
	return cd_natura;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_natura());
}
/* 
 * Setter dell'attributo cd_natura
 */
public void setCd_natura(java.lang.String cd_natura) {
	this.cd_natura = cd_natura;
}
}
