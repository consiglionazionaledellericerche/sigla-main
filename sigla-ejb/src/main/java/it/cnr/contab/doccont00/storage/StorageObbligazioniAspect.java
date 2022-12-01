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

package it.cnr.contab.doccont00.storage;

public enum StorageObbligazioniAspect {
	SIGLA_OBBLIGAZIONI_DETERMINA("P:sigla_obbligazioni_aspect:determina");

	private final String value;

	private StorageObbligazioniAspect(String value) {
		this.value = value;
	}

	public String value() {
		return value;
	}

	public static StorageObbligazioniAspect fromValue(String v) {
		for (StorageObbligazioniAspect c : StorageObbligazioniAspect.values()) {
			if (c.value.equals(v)) {
				return c;
			}
		}
		throw new IllegalArgumentException(v);
	}	
}
