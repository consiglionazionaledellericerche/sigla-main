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

package it.cnr.contab.doccont00.core.bulk;

import it.cnr.si.spring.storage.StorageObject;
import it.cnr.contab.util00.bulk.storage.AllegatoGenericoBulk;

public class AllegatoObbligazioneBulk extends AllegatoGenericoBulk {
	private static final long serialVersionUID = 1L;

	private Integer esercizioDiAppartenenza; 

	public AllegatoObbligazioneBulk() {
		super();
	}

	public AllegatoObbligazioneBulk(StorageObject storageObject) {
		super(storageObject.getKey());
	}

	public AllegatoObbligazioneBulk(String storageKey) {
		super(storageKey);
	}

	public void setEsercizioDiAppartenenza(Integer esercizioDiAppartenenza) {
		this.esercizioDiAppartenenza = esercizioDiAppartenenza;
	}
	
	public Integer getEsercizioDiAppartenenza() {
		return esercizioDiAppartenenza;
	}
}