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

package it.cnr.contab.reports.bulk;

import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;

public class Print_spooler_paramKey extends OggettoBulk implements
		KeyedPersistent {
	// NOME_PARAM VARCHAR(100) NOT NULL (PK)
	private java.lang.String nomeParam;

	// PG_STAMPA DECIMAL(10,0) NOT NULL (PK)
	private java.lang.Long pgStampa;

	public Print_spooler_paramKey() {
		super();
	}

	public Print_spooler_paramKey(java.lang.String nome_param,
			java.lang.Long pg_stampa) {
		super();
		this.nomeParam = nome_param;
		this.pgStampa = pg_stampa;
	}

	public boolean equalsByPrimaryKey(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Print_spooler_paramKey))
			return false;
		Print_spooler_paramKey k = (Print_spooler_paramKey) o;
		if (!compareKey(getNomeParam(), k.getNomeParam()))
			return false;
		if (!compareKey(getPgStampa(), k.getPgStampa()))
			return false;
		return true;
	}

	/*
	 * Getter dell'attributo nome_param
	 */
	public java.lang.String getNomeParam() {
		return nomeParam;
	}

	/*
	 * Getter dell'attributo pg_stampa
	 */
	public java.lang.Long getPgStampa() {
		return pgStampa;
	}

	public int primaryKeyHashCode() {
		return calculateKeyHashCode(getNomeParam())
				+ calculateKeyHashCode(getPgStampa());
	}

	/*
	 * Setter dell'attributo nome_param
	 */
	public void setNomeParam(java.lang.String nome_param) {
		this.nomeParam = nome_param;
	}

	/*
	 * Setter dell'attributo pg_stampa
	 */
	public void setPgStampa(java.lang.Long pg_stampa) {
		this.pgStampa = pg_stampa;
	}
}