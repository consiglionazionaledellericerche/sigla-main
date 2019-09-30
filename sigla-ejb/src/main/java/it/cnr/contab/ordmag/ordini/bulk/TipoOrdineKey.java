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
 * Date 28/06/2017
 */
package it.cnr.contab.ordmag.ordini.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class TipoOrdineKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.String cdTipoOrdine;
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: TIPO_ORDINE
	 **/
	public TipoOrdineKey() {
		super();
	}
	public TipoOrdineKey(java.lang.String cdTipoOrdine) {
		super();
		this.cdTipoOrdine=cdTipoOrdine;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof TipoOrdineKey)) return false;
		TipoOrdineKey k = (TipoOrdineKey) o;
		if (!compareKey(getCdTipoOrdine(), k.getCdTipoOrdine())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getCdTipoOrdine());
		return i;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdTipoOrdine]
	 **/
	public void setCdTipoOrdine(java.lang.String cdTipoOrdine)  {
		this.cdTipoOrdine=cdTipoOrdine;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdTipoOrdine]
	 **/
	public java.lang.String getCdTipoOrdine() {
		return cdTipoOrdine;
	}
}