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
 * Date 14/06/2010
 */
package it.cnr.contab.config00.sto.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class UnitaOrganizzativaPecKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.String cdUnitaOrganizzativa;
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: UNITA_ORGANIZZATIVA_PEC
	 **/
	public UnitaOrganizzativaPecKey() {
		super();
	}
	public UnitaOrganizzativaPecKey(java.lang.String cdUnitaOrganizzativa) {
		super();
		this.cdUnitaOrganizzativa=cdUnitaOrganizzativa;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof UnitaOrganizzativaPecKey)) return false;
		UnitaOrganizzativaPecKey k = (UnitaOrganizzativaPecKey) o;
		if (!compareKey(getCdUnitaOrganizzativa(), k.getCdUnitaOrganizzativa())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getCdUnitaOrganizzativa());
		return i;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdUnitaOrganizzativa]
	 **/
	public void setCdUnitaOrganizzativa(java.lang.String cdUnitaOrganizzativa)  {
		this.cdUnitaOrganizzativa=cdUnitaOrganizzativa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdUnitaOrganizzativa]
	 **/
	public java.lang.String getCdUnitaOrganizzativa() {
		return cdUnitaOrganizzativa;
	}
}