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
 * Date 06/06/2014
 */
package it.cnr.contab.anagraf00.core.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class IpaAmministrazioniKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.String codAmm;
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: IPA_AMMINISTRAZIONI
	 **/
	public IpaAmministrazioniKey() {
		super();
	}
	public IpaAmministrazioniKey(java.lang.String codAmm) {
		super();
		this.codAmm=codAmm;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof IpaAmministrazioniKey)) return false;
		IpaAmministrazioniKey k = (IpaAmministrazioniKey) o;
		if (!compareKey(getCodAmm(), k.getCodAmm())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getCodAmm());
		return i;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [codAmm]
	 **/
	public void setCodAmm(java.lang.String codAmm)  {
		this.codAmm=codAmm;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [codAmm]
	 **/
	public java.lang.String getCodAmm() {
		return codAmm;
	}
}