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
public class NotaPrecodificataKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.String cdCds;
	private java.lang.String cdNotaPrecodificata;
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: NOTA_PRECODIFICATA
	 **/
	public NotaPrecodificataKey() {
		super();
	}
	public NotaPrecodificataKey(java.lang.String cdCds, java.lang.String cdNotaPrecodificata) {
		super();
		this.cdCds=cdCds;
		this.cdNotaPrecodificata=cdNotaPrecodificata;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof NotaPrecodificataKey)) return false;
		NotaPrecodificataKey k = (NotaPrecodificataKey) o;
		if (!compareKey(getCdCds(), k.getCdCds())) return false;
		if (!compareKey(getCdNotaPrecodificata(), k.getCdNotaPrecodificata())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getCdCds());
		i = i + calculateKeyHashCode(getCdNotaPrecodificata());
		return i;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCds]
	 **/
	public void setCdCds(java.lang.String cdCds)  {
		this.cdCds=cdCds;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCds]
	 **/
	public java.lang.String getCdCds() {
		return cdCds;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdNotaPrecodificata]
	 **/
	public void setCdNotaPrecodificata(java.lang.String cdNotaPrecodificata)  {
		this.cdNotaPrecodificata=cdNotaPrecodificata;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdNotaPrecodificata]
	 **/
	public java.lang.String getCdNotaPrecodificata() {
		return cdNotaPrecodificata;
	}
}