package it.cnr.contab.pdg00.bulk.storage;

import it.cnr.contab.util.SIGLAStoragePropertyNames;
import it.cnr.si.spring.storage.StorageObject;
import it.cnr.si.spring.storage.config.StoragePropertyNames;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

public class PdgVariazioneDocument implements Serializable{
	private final StorageObject storageObject;

	public static PdgVariazioneDocument construct(StorageObject storageObject){
		return new PdgVariazioneDocument(storageObject);
	}
	
	public PdgVariazioneDocument(StorageObject storageObject) {
		super();
		this.storageObject = storageObject;
	}
	
	public StorageObject getStorageObject() {
		return storageObject;
	}

	public Integer getEsercizio(){
		return ((BigInteger) storageObject.getPropertyValue(SIGLAStoragePropertyNames.VARPIANOGEST_ESERCIZIO.value())).intValue();
	}
	
	public Integer getNumeroVariazione(){
		return ((BigInteger) storageObject.getPropertyValue(SIGLAStoragePropertyNames.VARPIANOGEST_NUMEROVARIAZIONE.value())).intValue();
	}
	
	public Boolean isSignedDocument(){
		return storageObject.<List<String>>getPropertyValue(StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value()).contains(SIGLAStoragePropertyNames.CNR_SIGNEDDOCUMENT.value());
	}
}
