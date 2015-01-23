package it.cnr.contab.cmis.bulk;

import java.io.Serializable;

import org.apache.chemistry.opencmis.client.api.Document;

public class CMISDocument implements Serializable{
	private final Document document;

	public static CMISDocument construct(Document document){
		return new CMISDocument(document);
	}
	
	public CMISDocument(Document document) {
		super();
		this.document = document;
	}
	
	public Document getDocument() {
		return document;
	}
}
