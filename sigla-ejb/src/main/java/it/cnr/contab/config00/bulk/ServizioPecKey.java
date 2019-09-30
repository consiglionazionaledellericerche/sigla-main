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
 * Date 14/05/2010
 */
package it.cnr.contab.config00.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class ServizioPecKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.String cdServizio;
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: SERVIZIO_PEC
	 **/
	public ServizioPecKey() {
		super();
	}
	public ServizioPecKey(java.lang.String cdServizio) {
		super();
		this.cdServizio=cdServizio;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof ServizioPecKey)) return false;
		ServizioPecKey k = (ServizioPecKey) o;
		if (!compareKey(getCdServizio(), k.getCdServizio())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getCdServizio());
		return i;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdServizio]
	 **/
	public void setCdServizio(java.lang.String cdServizio)  {
		this.cdServizio=cdServizio;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdServizio]
	 **/
	public java.lang.String getCdServizio() {
		return cdServizio;
	}
}