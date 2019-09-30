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
 * Date 26/04/2017
 */
package it.cnr.contab.ordmag.anag00;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class UnitaMisuraKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.String cdUnitaMisura;
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: UNITA_MISURA
	 **/
	public UnitaMisuraKey() {
		super();
	}
	public UnitaMisuraKey(java.lang.String cdUnitaMisura) {
		super();
		this.cdUnitaMisura=cdUnitaMisura;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof UnitaMisuraKey)) return false;
		UnitaMisuraKey k = (UnitaMisuraKey) o;
		if (!compareKey(getCdUnitaMisura(), k.getCdUnitaMisura())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getCdUnitaMisura());
		return i;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdUnitaMisura]
	 **/
	public void setCdUnitaMisura(java.lang.String cdUnitaMisura)  {
		this.cdUnitaMisura=cdUnitaMisura;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdUnitaMisura]
	 **/
	public java.lang.String getCdUnitaMisura() {
		return cdUnitaMisura;
	}
}