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

package it.cnr.contab.messaggio00.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class MessaggioKey extends OggettoBulk implements KeyedPersistent {
	// PG_MESSAGGIO DECIMAL(10,0) NOT NULL (PK)
	private java.lang.Long pg_messaggio;

public MessaggioKey() {
	super();
}
public MessaggioKey(java.lang.Long pg_messaggio) {
	super();
	this.pg_messaggio = pg_messaggio;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof MessaggioKey)) return false;
	MessaggioKey k = (MessaggioKey)o;
	if(!compareKey(getPg_messaggio(),k.getPg_messaggio())) return false;
	return true;
}
/* 
 * Getter dell'attributo pg_messaggio
 */
public java.lang.Long getPg_messaggio() {
	return pg_messaggio;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getPg_messaggio());
}
/* 
 * Setter dell'attributo pg_messaggio
 */
public void setPg_messaggio(java.lang.Long pg_messaggio) {
	this.pg_messaggio = pg_messaggio;
}
}
