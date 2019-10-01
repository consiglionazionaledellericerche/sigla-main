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
 * Date 02/10/2018
 */
package it.cnr.contab.doccont00.intcass.giornaliera;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class FlussoGiornaleDiCassaKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.Integer esercizio;
	private java.lang.String identificativoFlusso;
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: FLUSSO_GIORNALE_DI_CASSA
	 **/
	public FlussoGiornaleDiCassaKey() {
		super();
	}
	public FlussoGiornaleDiCassaKey(java.lang.Integer esercizio, java.lang.String identificativoFlusso) {
		super();
		this.esercizio=esercizio;
		this.identificativoFlusso=identificativoFlusso;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof FlussoGiornaleDiCassaKey)) return false;
		FlussoGiornaleDiCassaKey k = (FlussoGiornaleDiCassaKey) o;
		if (!compareKey(getEsercizio(), k.getEsercizio())) return false;
		if (!compareKey(getIdentificativoFlusso(), k.getIdentificativoFlusso())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getEsercizio());
		i = i + calculateKeyHashCode(getIdentificativoFlusso());
		return i;
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
	 * Restituisce il valore di: [identificativoFlusso]
	 **/
	public void setIdentificativoFlusso(java.lang.String identificativoFlusso)  {
		this.identificativoFlusso=identificativoFlusso;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [identificativoFlusso]
	 **/
	public java.lang.String getIdentificativoFlusso() {
		return identificativoFlusso;
	}
}