package it.cnr.contab.cmis.bulk;

import it.cnr.contab.spring.config.StorageObject;

import java.io.Serializable;

public class CMISDocument implements Serializable{
	private final StorageObject storageObject;

	public static CMISDocument construct(StorageObject storageObject){
		return new CMISDocument(storageObject);
	}
	
	public CMISDocument(StorageObject storageObject) {
		super();
		this.storageObject = storageObject;
	}
	
	public StorageObject getStorageObject() {
		return storageObject;
	}
}
