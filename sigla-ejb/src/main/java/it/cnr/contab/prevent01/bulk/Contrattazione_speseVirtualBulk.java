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
