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

package it.cnr.contab.dottorati.bulk;

import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;

public class DottoratiKey extends OggettoBulk implements KeyedPersistent {
	private static final long serialVersionUID = 1L;

	private Integer id;

	public DottoratiKey() {
		super();
	}

	public DottoratiKey(Integer id) {
		super();
		this.id = id;
	}

	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof DottoratiKey))
			return false;
		DottoratiKey k = (DottoratiKey)o;
		if(!compareKey(getId(),k.getId()))
			return false;
		return true;
	}

	public boolean equalsByPrimaryKey(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof DottoratiKey))
			return false;
		DottoratiKey k = (DottoratiKey)o;
		if(!compareKey(getId(),k.getId()))
			return false;
		return true;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
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
