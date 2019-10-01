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
public class OrdineAcqKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.String cdCds;
	private java.lang.String cdUnitaOperativa;
	private java.lang.Integer esercizio;
	private java.lang.String cdNumeratore;
	private java.lang.Integer numero;
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: ORDINE_ACQ
	 **/
	public OrdineAcqKey() {
		super();
	}
	public OrdineAcqKey(java.lang.String cdCds, java.lang.String cdUnitaOperativa, java.lang.Integer esercizio, java.lang.String cdNumeratore, java.lang.Integer numero) {
		super();
		this.cdCds=cdCds;
		this.cdUnitaOperativa=cdUnitaOperativa;
		this.esercizio=esercizio;
		this.cdNumeratore=cdNumeratore;
		this.numero=numero;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof OrdineAcqKey)) return false;
		OrdineAcqKey k = (OrdineAcqKey) o;
		if (!compareKey(getCdCds(), k.getCdCds())) return false;
		if (!compareKey(getCdUnitaOperativa(), k.getCdUnitaOperativa())) return false;
		if (!compareKey(getEsercizio(), k.getEsercizio())) return false;
		if (!compareKey(getCdNumeratore(), k.getCdNumeratore())) return false;
		if (!compareKey(getNumero(), k.getNumero())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getCdCds());
		i = i + calculateKeyHashCode(getCdUnitaOperativa());
		i = i + calculateKeyHashCode(getEsercizio());
		i = i + calculateKeyHashCode(getCdNumeratore());
		i = i + calculateKeyHashCode(getNumero());
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
	 * Restituisce il valore di: [cdUnitaOperativa]
	 **/
	public void setCdUnitaOperativa(java.lang.String cdUnitaOperativa)  {
		this.cdUnitaOperativa=cdUnitaOperativa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdUnitaOperativa]
	 **/
	public java.lang.String getCdUnitaOperativa() {
		return cdUnitaOperativa;
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
	 * Restituisce il valore di: [cdNumeratore]
	 **/
	public void setCdNumeratore(java.lang.String cdNumeratore)  {
		this.cdNumeratore=cdNumeratore;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdNumeratore]
	 **/
	public java.lang.String getCdNumeratore() {
		return cdNumeratore;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [numero]
	 **/
	public void setNumero(java.lang.Integer numero)  {
		this.numero=numero;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [numero]
	 **/
	public java.lang.Integer getNumero() {
		return numero;
	}
}