package it.cnr.contab.spring.storage.bulk;

import it.cnr.contab.spring.storage.StorageObject;

import java.io.Serializable;

public class StorageDocument implements Serializable{
	private final StorageObject storageObject;

	public static StorageDocument construct(StorageObject storageObject){
		return new StorageDocument(storageObject);
	}
	
	public StorageDocument(StorageObject storageObject) {
		super();
		this.storageObject = storageObject;
	}
	
	public StorageObject getStorageObject() {
		return storageObject;
	}
}
