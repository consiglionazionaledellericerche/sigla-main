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

/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 07/10/2013
 */
package it.cnr.contab.config00.latt.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class CofogKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.String cd_cofog;
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: COFOG
	 **/
	public CofogKey() {
		super();
	}
	public CofogKey(java.lang.String cdCofog) {
		super();
		this.cd_cofog=cdCofog;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof CofogKey)) return false;
		CofogKey k = (CofogKey) o;
		if (!compareKey(getCd_cofog(), k.getCd_cofog())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getCd_cofog());
		return i;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCofog]
	 **/
	public void setCd_cofog(java.lang.String cdCofog)  {
		this.cd_cofog=cdCofog;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCofog]
	 **/
	public java.lang.String getCd_cofog() {
		return cd_cofog;
	}
}