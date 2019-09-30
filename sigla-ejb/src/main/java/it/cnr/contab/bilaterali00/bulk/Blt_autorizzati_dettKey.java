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
 * Date 01/02/2012
 */
package it.cnr.contab.bilaterali00.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Blt_autorizzati_dettKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.String cdAccordo;
	private java.lang.String cdProgetto;
	private java.lang.Integer cdTerzo;
	private java.lang.Long pgAutorizzazione;

	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: BLT_PROGRAMMA_VISITE
	 **/
	public Blt_autorizzati_dettKey() {
		super();
	}
	public Blt_autorizzati_dettKey(java.lang.String cdAccordo, java.lang.String cdProgetto, java.lang.Integer cdTerzo, java.lang.Long pgAutorizzazione) {
		super();
		this.cdAccordo=cdAccordo;
		this.cdProgetto=cdProgetto;
		this.cdTerzo=cdTerzo;
		this.pgAutorizzazione=pgAutorizzazione;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Blt_autorizzati_dettKey)) return false;
		Blt_autorizzati_dettKey k = (Blt_autorizzati_dettKey) o;
		if (!compareKey(getCdAccordo(), k.getCdAccordo())) return false;
		if (!compareKey(getCdProgetto(), k.getCdProgetto())) return false;
		if (!compareKey(getCdTerzo(), k.getCdTerzo())) return false;
		if (!compareKey(getPgAutorizzazione(), k.getPgAutorizzazione())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getCdAccordo());
		i = i + calculateKeyHashCode(getCdProgetto());
		i = i + calculateKeyHashCode(getCdTerzo());
		i = i + calculateKeyHashCode(getPgAutorizzazione());
		return i;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdAccordo]
	 **/
	public void setCdAccordo(java.lang.String cdAccordo)  {
		this.cdAccordo=cdAccordo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdAccordo]
	 **/
	public java.lang.String getCdAccordo() {
		return cdAccordo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdProgetto]
	 **/
	public void setCdProgetto(java.lang.String cdProgetto)  {
		this.cdProgetto=cdProgetto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdProgetto]
	 **/
	public java.lang.String getCdProgetto() {
		return cdProgetto;
	}
	public java.lang.Integer getCdTerzo() {
		return cdTerzo;
	}
	public void setCdTerzo(java.lang.Integer cdTerzo) {
		this.cdTerzo = cdTerzo;
	}
	public java.lang.Long getPgAutorizzazione() {
		return pgAutorizzazione;
	}
	public void setPgAutorizzazione(java.lang.Long pgAutorizzazione) {
		this.pgAutorizzazione = pgAutorizzazione;
	}
}