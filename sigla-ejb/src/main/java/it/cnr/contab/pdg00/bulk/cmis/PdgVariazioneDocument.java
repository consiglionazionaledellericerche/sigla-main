package it.cnr.contab.pdg00.bulk.cmis;

import it.cnr.cmisdl.model.Node;
import it.cnr.contab.cmis.CMISAspect;
import it.cnr.contab.cmis.CMISProperty;

import java.io.Serializable;
import java.math.BigInteger;

public class PdgVariazioneDocument implements Serializable{
	private final Node node;

	public static PdgVariazioneDocument construct(Node node){
		return new PdgVariazioneDocument(node);
	}
	
	public PdgVariazioneDocument(Node node) {
		super();
		this.node = node;
	}
	
	public Node getNode() {
		return node;
	}

	public Integer getEsercizio(){
		return ((BigInteger) node.getPropertyValue(CMISProperty.VARPIANOGEST_ESERCIZIO.value())).intValue();
	}
	
	public Integer getNumeroVariazione(){
		return ((BigInteger) node.getPropertyValue(CMISProperty.VARPIANOGEST_NUMEROVARIAZIONE.value())).intValue();
	}
	
	public Boolean isSignedDocument(){
		return node.hasAspect(CMISAspect.CNR_SIGNEDDOCUMENT.value());
	}
}
