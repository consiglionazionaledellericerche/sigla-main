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

package it.cnr.contab.config00.bulk;

import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;

/**
 * Creation date: (24/02/2005)
 * @author Tilde
 * @version 1.0
 */
public class FirmeKey extends OggettoBulk implements KeyedPersistent {
	// ESERCIZIO DECIMAL(4,0) NOT NULL (PK)
    // TIPO      VARCHAR(3)	  NOT NULL (PK)
	private java.lang.Integer esercizio;
	private String tipo;

	public FirmeKey() {
		super();
	}
	public FirmeKey(java.lang.Integer esercizio, String tipo) {
		super();
		this.esercizio = esercizio;
		this.tipo = tipo;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof FirmeKey))
			return false;
		FirmeKey k = (FirmeKey) o;
		if (!compareKey(getEsercizio(), k.getEsercizio()))
			return false;
		if (!compareKey(getTipo(), k.getTipo()))
			return false;	
		return true;
	}
	/**
	 * Getter dell'attributo esercizio
	 */
	public java.lang.Integer getEsercizio() {
		return esercizio;
	}
	/**
	 * Setter dell'attributo esercizio
	 */
	public void setEsercizio(java.lang.Integer esercizio) {
		this.esercizio = esercizio;
	}
	/**
	 * Getter dell'attributo tipo
	 */
	public String getTipo() {
		return tipo;
	}
	/**
	 * Setter dell'attributo tipo
	 */
	public void setTipo(String string) {
		tipo = string;
	}

	public int primaryKeyHashCode() {
		return calculateKeyHashCode(getEsercizio())+
		       calculateKeyHashCode(getTipo());
	}

}
