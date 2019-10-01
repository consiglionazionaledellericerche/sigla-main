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

package it.cnr.contab.prevent01.bulk;

import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;

public class Contrattazione_speseVirtualBulk extends OggettoBulk {
	private BulkList dettagliDipArea = new BulkList();
	private BulkList dettagliContrSpese = new BulkList();

	public it.cnr.jada.bulk.BulkList getDettagliDipArea() {
		return dettagliDipArea;
	}
	public int addToDettagliDipArea(Pdg_approvato_dip_areaBulk dett) {
		getDettagliDipArea().add(dett);
		return getDettagliDipArea().size()-1;
	}	
	public void setDettagliDipArea(it.cnr.jada.bulk.BulkList newDettagliDipArea) {
		dettagliDipArea = newDettagliDipArea;
	}
	public it.cnr.jada.bulk.BulkCollection[] getBulkLists() {
		return new it.cnr.jada.bulk.BulkCollection[] {getDettagliDipArea(), getDettagliContrSpese()};
	}
	public Pdg_approvato_dip_areaBulk removeFromDettagliDipArea(int index) {
		Pdg_approvato_dip_areaBulk dett = (Pdg_approvato_dip_areaBulk)getDettagliDipArea().remove(index);
		return dett;
	}
	public it.cnr.jada.bulk.BulkList getDettagliContrSpese() {
		return dettagliContrSpese;
	}
	public int addToDettagliContrSpese(Pdg_contrattazione_speseBulk dett) {
		getDettagliContrSpese().add(dett);
		return getDettagliContrSpese().size()-1;
	}	
	public void setDettagliContrSpese(it.cnr.jada.bulk.BulkList newDettagliContrSpese) {
		dettagliContrSpese = newDettagliContrSpese;
	}
	public Pdg_contrattazione_speseBulk removeFromDettagliContrSpese(int index) {
		Pdg_contrattazione_speseBulk dett = (Pdg_contrattazione_speseBulk)getDettagliContrSpese().remove(index);
		return dett;
	}
}
