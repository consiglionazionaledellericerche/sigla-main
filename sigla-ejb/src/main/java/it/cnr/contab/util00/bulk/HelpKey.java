/*
 * Copyright (C) 2020  Consiglio Nazionale delle Ricerche
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

/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 28/02/2020
 */
package it.cnr.contab.util00.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class HelpKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.Integer id;
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: HELP_LKT
	 **/
	public HelpKey() {
		super();
	}
	public HelpKey(java.lang.Integer id) {
		super();
		this.id=id;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof HelpKey)) return false;
		HelpKey k = (HelpKey) o;
		if (!compareKey(getId(), k.getId())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getId());
		return i;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Codice univoco della pagina di help]
	 **/
	public void setId(java.lang.Integer id)  {
		this.id=id;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Codice univoco della pagina di help]
	 **/
	public java.lang.Integer getId() {
		return id;
	}
}