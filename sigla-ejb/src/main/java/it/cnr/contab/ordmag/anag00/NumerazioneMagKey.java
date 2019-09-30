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
public class NumerazioneMagKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.String cdCds;
	private java.lang.String cdMagazzino;
	private java.lang.Integer esercizio;
	private java.lang.String cdNumeratoreMag;
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: NUMERAZIONE_MAG
	 **/
	public NumerazioneMagKey() {
		super();
	}
	public NumerazioneMagKey(java.lang.String cdCds, java.lang.String cdMagazzino, java.lang.Integer esercizio, java.lang.String cdNumeratoreMag) {
		super();
		this.cdCds=cdCds;
		this.cdMagazzino=cdMagazzino;
		this.esercizio=esercizio;
		this.cdNumeratoreMag=cdNumeratoreMag;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof NumerazioneMagKey)) return false;
		NumerazioneMagKey k = (NumerazioneMagKey) o;
		if (!compareKey(getCdCds(), k.getCdCds())) return false;
		if (!compareKey(getCdMagazzino(), k.getCdMagazzino())) return false;
		if (!compareKey(getEsercizio(), k.getEsercizio())) return false;
		if (!compareKey(getCdNumeratoreMag(), k.getCdNumeratoreMag())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getCdCds());
		i = i + calculateKeyHashCode(getCdMagazzino());
		i = i + calculateKeyHashCode(getEsercizio());
		i = i + calculateKeyHashCode(getCdNumeratoreMag());
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
	 * Restituisce il valore di: [cdMagazzino]
	 **/
	public void setCdMagazzino(java.lang.String cdMagazzino)  {
		this.cdMagazzino=cdMagazzino;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdMagazzino]
	 **/
	public java.lang.String getCdMagazzino() {
		return cdMagazzino;
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
	 * Restituisce il valore di: [cdNumeratoreMag]
	 **/
	public void setCdNumeratoreMag(java.lang.String cdNumeratoreMag)  {
		this.cdNumeratoreMag=cdNumeratoreMag;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdNumeratoreMag]
	 **/
	public java.lang.String getCdNumeratoreMag() {
		return cdNumeratoreMag;
	}
}