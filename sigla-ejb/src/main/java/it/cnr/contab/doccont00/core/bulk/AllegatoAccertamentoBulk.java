package it.cnr.contab.doccont00.core.bulk;

import it.cnr.si.spring.storage.StorageObject;
import it.cnr.contab.util00.bulk.storage.AllegatoGenericoBulk;

public class AllegatoAccertamentoBulk extends AllegatoGenericoBulk {
	private static final long serialVersionUID = 1L;

	private Integer esercizioDiAppartenenza; 

	public AllegatoAccertamentoBulk() {
		super();
	}

	public AllegatoAccertamentoBulk(StorageObject storageObject) {
		super(storageObject.getKey());
	}

	public AllegatoAccertamentoBulk(String storageKey) {
		super(storageKey);
	}

	public void setEsercizioDiAppartenenza(Integer esercizioDiAppartenenza) {
		this.esercizioDiAppartenenza = esercizioDiAppartenenza;
	}
	
	public Integer getEsercizioDiAppartenenza() {
		return esercizioDiAppartenenza;
	}
}