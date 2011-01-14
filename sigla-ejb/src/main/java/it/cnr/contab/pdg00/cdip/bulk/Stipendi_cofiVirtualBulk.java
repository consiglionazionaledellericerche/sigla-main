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
