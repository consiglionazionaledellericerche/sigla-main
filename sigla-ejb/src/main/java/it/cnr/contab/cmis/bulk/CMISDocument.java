package it.cnr.contab.cmis.bulk;

import java.io.Serializable;

import it.cnr.contab.spring.config.StorageObject;
import org.apache.chemistry.opencmis.client.api.Document;

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
