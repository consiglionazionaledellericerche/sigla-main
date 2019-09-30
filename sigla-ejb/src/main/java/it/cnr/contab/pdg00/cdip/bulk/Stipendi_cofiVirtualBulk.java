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

package it.cnr.contab.pdg00.cdip.bulk;

import it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;

public class Stipendi_cofiVirtualBulk extends OggettoBulk {
	private BulkList stipendi_obbligazioni = new BulkList();
	private ObbligazioneBulk obbligazioni = new ObbligazioneBulk(); 
	public it.cnr.jada.bulk.BulkList getStipendi_obbligazioni() {
		return stipendi_obbligazioni;
	}
	public int addToStipendi_obbligazioni(Stipendi_cofi_obbBulk dett) {
		getStipendi_obbligazioni().add(dett);
		return getStipendi_obbligazioni().size()-1;
	}	
	public void setStipendi_obbligazioni(it.cnr.jada.bulk.BulkList newStipendi_obbligazioni) {
		stipendi_obbligazioni = newStipendi_obbligazioni;
	}
	public it.cnr.jada.bulk.BulkCollection[] getBulkLists() {
		return new it.cnr.jada.bulk.BulkCollection[] {getStipendi_obbligazioni()};
	}
	public Stipendi_cofi_obbBulk removeFromStipendi_obbligazioni(int index) {
		Stipendi_cofi_obbBulk dett = (Stipendi_cofi_obbBulk)getStipendi_obbligazioni().remove(index);
		return dett;
	}
	public ObbligazioneBulk getObbligazioni() {
		return obbligazioni;
	}
	public void setObbligazioni(ObbligazioneBulk obbligazione) {
		this.obbligazioni = obbligazione;
	}

}
