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

package it.cnr.contab.docamm00.intrastat.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Nomenclatura_combinataKey extends OggettoBulk implements KeyedPersistent {

	private java.lang.Long id_nomenclatura_combinata;

public Nomenclatura_combinataKey() {
	super();
}
public Nomenclatura_combinataKey(java.lang.Long id_nomenclatura_combinata) {
	super();
	this.id_nomenclatura_combinata = id_nomenclatura_combinata;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Nomenclatura_combinataKey)) return false;
	Nomenclatura_combinataKey k = (Nomenclatura_combinataKey)o;
	if(!compareKey(getId_nomenclatura_combinata(),k.getId_nomenclatura_combinata())) return false;
	return true;
}

public java.lang.Long getId_nomenclatura_combinata() {
	return id_nomenclatura_combinata;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getId_nomenclatura_combinata());
}
public void setId_nomenclatura_combinata(java.lang.Long id_nomenclatura_combinata) {
	this.id_nomenclatura_combinata = id_nomenclatura_combinata;
}
}
