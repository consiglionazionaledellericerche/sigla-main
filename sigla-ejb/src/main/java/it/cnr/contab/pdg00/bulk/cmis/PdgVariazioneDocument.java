package it.cnr.contab.pdg00.bulk.cmis;

import it.cnr.contab.cmis.CMISAspect;
import it.cnr.contab.cmis.CMISProperty;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

import it.cnr.contab.spring.config.StorageObject;
import it.cnr.contab.spring.config.StoragePropertyNames;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.commons.PropertyIds;

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
		return ((BigInteger) storageObject.getPropertyValue(StoragePropertyNames.VARPIANOGEST_ESERCIZIO.value())).intValue();
	}
	
	public Integer getNumeroVariazione(){
		return ((BigInteger) storageObject.getPropertyValue(StoragePropertyNames.VARPIANOGEST_NUMEROVARIAZIONE.value())).intValue();
	}
	
	public Boolean isSignedDocument(){
		return storageObject.<List<String>>getPropertyValue(StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value()).contains(CMISAspect.CNR_SIGNEDDOCUMENT.value());
	}
}
