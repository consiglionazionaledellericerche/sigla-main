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

package it.cnr.contab.config00.bulk;

import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;

/**
 * @author aimprota
 *
 */
public class Parametri_enteKey extends OggettoBulk implements KeyedPersistent{
	// ID NUMBER(9) NOT NULL (PK)
	private java.lang.Integer id;
    /**
     * 
     */
    public Parametri_enteKey()
    {
        super();
    }
	public Parametri_enteKey(java.lang.Integer id)
	{
		super();
		this.id = id;
	}  
	
	public boolean equalsByPrimaryKey(Object o) {
		if (this == o) return true;
		if (!(o instanceof Parametri_enteKey)) return false;
		Parametri_enteKey k = (Parametri_enteKey)o;
		if(!compareKey(getId(),k.getId())) return false;
		return true;
	}
	
	/* 
	 * Getter dell'attributo id
	 */
	public java.lang.Integer getId() {
		return id;
	}
	public int primaryKeyHashCode() {
		return
			calculateKeyHashCode(getId());
	}
	/* 
	 * Setter dell'attributo id
	 */
	public void setId(java.lang.Integer id) {
		this.id = id;
	}  

}
