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
 * Date 27/01/2015
 */
package it.cnr.contab.doccont00.consultazioni.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class VConsObbligazioniKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.String cdCds;
	private java.lang.Integer esercizio;
	private java.lang.Integer esercizioOriginale;
	private java.lang.Long pgObbligazione;
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: CONS_OBBLIGAZIONI
	 **/
	public VConsObbligazioniKey() {
		super();
	}
	public VConsObbligazioniKey(java.lang.String cdCds, java.lang.Integer esercizio, java.lang.Integer esercizioOriginale, java.lang.Long pgObbligazione) {
		super();
		this.cdCds=cdCds;
		this.esercizio=esercizio;
		this.esercizioOriginale=esercizioOriginale;
		this.pgObbligazione=pgObbligazione;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof VConsObbligazioniKey)) return false;
		VConsObbligazioniKey k = (VConsObbligazioniKey) o;
		if (!compareKey(getCdCds(), k.getCdCds())) return false;
		if (!compareKey(getEsercizio(), k.getEsercizio())) return false;
		if (!compareKey(getEsercizioOriginale(), k.getEsercizioOriginale())) return false;
		if (!compareKey(getPgObbligazione(), k.getPgObbligazione())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getCdCds());
		i = i + calculateKeyHashCode(getEsercizio());
		i = i + calculateKeyHashCode(getEsercizioOriginale());
		i = i + calculateKeyHashCode(getPgObbligazione());
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
	 * Restituisce il valore di: [esercizio]
	 **/
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizio]
	 **/
	public java.lang.Integer getEsercizio() {
		return esercizio;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esercizioOriginale]
	 **/
	public void setEsercizioOriginale(java.lang.Integer esercizioOriginale)  {
		this.esercizioOriginale=esercizioOriginale;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizioOriginale]
	 **/
	public java.lang.Integer getEsercizioOriginale() {
		return esercizioOriginale;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [pgObbligazione]
	 **/
	public void setPgObbligazione(java.lang.Long pgObbligazione)  {
		this.pgObbligazione=pgObbligazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [pgObbligazione]
	 **/
	public java.lang.Long getPgObbligazione() {
		return pgObbligazione;
	}
}