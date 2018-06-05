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