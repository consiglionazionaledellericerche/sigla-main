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

package it.cnr.contab.inventario00.docs.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;

public class Ass_inv_bene_fatturaKey extends OggettoBulk implements KeyedPersistent {

private java.lang.Long pg_riga;
public Ass_inv_bene_fatturaKey() {
	super();
}

public Ass_inv_bene_fatturaKey(Long riga) {
	
	super();
	this.pg_riga=riga;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this== o) return true;
	if (!(o instanceof Ass_inv_bene_fatturaKey)) return false;
	Ass_inv_bene_fatturaKey k = (Ass_inv_bene_fatturaKey) o;
	if (!compareKey(getPg_riga(), k.getPg_riga())) return false;
		return true;
}
public int primaryKeyHashCode() {
	int i = 0;
	i = i + calculateKeyHashCode(getPg_riga());
	return i;
}
public void setPg_riga(java.lang.Long pg_riga)  {
	this.pg_riga=pg_riga;
}
public java.lang.Long getPg_riga () {
	return pg_riga;
}
}
