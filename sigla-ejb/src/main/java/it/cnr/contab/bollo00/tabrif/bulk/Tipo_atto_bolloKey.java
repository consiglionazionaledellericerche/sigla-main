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

package it.cnr.contab.bollo00.tabrif.bulk;

import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;

public class Tipo_atto_bolloKey extends OggettoBulk implements KeyedPersistent {
	private static final long serialVersionUID = 1L;

	private java.lang.Integer id;

	public Tipo_atto_bolloKey() {
		super();
	}
	
	public Tipo_atto_bolloKey(java.lang.Integer id) {
		super();
		this.id = id;
	}

	public boolean equals(Object o) {
		if (this == o) 
			return true;
		if (!(o instanceof Tipo_atto_bolloKey)) 
			return false;
		Tipo_atto_bolloKey k = (Tipo_atto_bolloKey)o;
		if(!compareKey(getId(),k.getId())) 
			return false;
		return true;
	}

	public boolean equalsByPrimaryKey(Object o) {
		if (this == o) 
			return true;
		if (!(o instanceof Tipo_atto_bolloKey)) 
			return false;
		Tipo_atto_bolloKey k = (Tipo_atto_bolloKey)o;
		if(!compareKey(getId(),k.getId())) 
			return false;
		return true;
	}

	public java.lang.Integer getId() {
		return id;
	}
	
	public void setId(java.lang.Integer id) {
		this.id = id;
	}
	
	public int hashCode() {
		return
			calculateKeyHashCode(getId());
	}

	public int primaryKeyHashCode() {
		return
			calculateKeyHashCode(getId());
	}
}
