package it.cnr.contab.cmis.bulk;

import it.cnr.cmisdl.model.Node;

import java.io.Serializable;

public class CMISDocument implements Serializable{
	private final Node node;

	public static CMISDocument construct(Node node){
		return new CMISDocument(node);
	}
	
	public CMISDocument(Node node) {
		super();
		this.node = node;
	}
	
	public Node getNode() {
		return node;
	}
}
