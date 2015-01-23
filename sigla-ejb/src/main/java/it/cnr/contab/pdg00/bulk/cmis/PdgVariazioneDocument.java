package it.cnr.contab.pdg00.bulk.cmis;

import it.cnr.contab.cmis.CMISAspect;
import it.cnr.contab.cmis.CMISProperty;

import java.io.Serializable;
import java.math.BigInteger;

import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.commons.PropertyIds;

public class PdgVariazioneDocument implements Serializable{
	private final Document node;

	public static PdgVariazioneDocument construct(Document node){
		return new PdgVariazioneDocument(node);
	}
	
	public PdgVariazioneDocument(Document node) {
		super();
		this.node = node;
	}
	
	public Document getDocument() {
		return node;
	}

	public Integer getEsercizio(){
		return ((BigInteger) node.getPropertyValue(CMISProperty.VARPIANOGEST_ESERCIZIO.value())).intValue();
	}
	
	public Integer getNumeroVariazione(){
		return ((BigInteger) node.getPropertyValue(CMISProperty.VARPIANOGEST_NUMEROVARIAZIONE.value())).intValue();
	}
	
	public Boolean isSignedDocument(){
		return node.getProperty(PropertyIds.SECONDARY_OBJECT_TYPE_IDS).getValues().contains(CMISAspect.CNR_SIGNEDDOCUMENT.value());
	}
}
