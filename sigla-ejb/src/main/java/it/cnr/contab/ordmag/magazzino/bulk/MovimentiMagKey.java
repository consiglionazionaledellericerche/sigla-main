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
 * Date 03/10/2017
 */
package it.cnr.contab.ordmag.magazzino.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class MovimentiMagKey extends OggettoBulk implements KeyedPersistent {
	private Long pgMovimento;
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: MOVIMENTI_MAG
	 **/
	public MovimentiMagKey() {
		super();
	}
	public MovimentiMagKey(Long pgMovimento) {
		super();
		this.pgMovimento=pgMovimento;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof MovimentiMagKey)) return false;
		MovimentiMagKey k = (MovimentiMagKey) o;
		if (!compareKey(getPgMovimento(), k.getPgMovimento())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getPgMovimento());
		return i;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [pgMovimento]
	 **/
	public void setPgMovimento(Long pgMovimento)  {
		this.pgMovimento=pgMovimento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [pgMovimento]
	 **/
	public Long getPgMovimento() {
		return pgMovimento;
	}
}