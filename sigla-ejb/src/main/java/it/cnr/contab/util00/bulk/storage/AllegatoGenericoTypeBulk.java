package it.cnr.contab.util00.bulk.storage;

import it.cnr.si.spring.storage.StorageObject;
import it.cnr.si.spring.storage.config.StoragePropertyNames;

public class AllegatoGenericoTypeBulk extends AllegatoGenericoBulk {
	private String objectType;
	
	public AllegatoGenericoTypeBulk() {
		super();
	}

	public static AllegatoGenericoTypeBulk construct(String storageKey){
		return new AllegatoGenericoTypeBulk(storageKey);
	}
	
	public AllegatoGenericoTypeBulk(String storageKey) {
		super(storageKey);
	}

	public AllegatoGenericoTypeBulk(StorageObject storageObject){
		super();
		setObjectType(storageObject.getPropertyValue(StoragePropertyNames.OBJECT_TYPE_ID.value()));
	}
	
	public String getObjectType() {
		return objectType;
	}
	
	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}
}
