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

public class Classificazione_montantiKey extends OggettoBulk implements KeyedPersistent {
	// PG_CLASSIFICAZIONE_MONTANTI DECIMAL(10,0) NOT NULL (PK)
	private java.lang.Long pg_classificazione_montanti;

public Classificazione_montantiKey() {
	super();
}
public Classificazione_montantiKey(java.lang.Long pg_classificazione_montanti) {
	super();
	this.pg_classificazione_montanti = pg_classificazione_montanti;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Classificazione_montantiKey)) return false;
	Classificazione_montantiKey k = (Classificazione_montantiKey)o;
	if(!compareKey(getPg_classificazione_montanti(),k.getPg_classificazione_montanti())) return false;
	return true;
}
/* 
 * Getter dell'attributo pg_classificazione_montanti
 */
public java.lang.Long getPg_classificazione_montanti() {
	return pg_classificazione_montanti;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getPg_classificazione_montanti());
}
/* 
 * Setter dell'attributo pg_classificazione_montanti
 */
public void setPg_classificazione_montanti(java.lang.Long pg_classificazione_montanti) {
	this.pg_classificazione_montanti = pg_classificazione_montanti;
}
}
