package it.cnr.contab.ordmag.richieste.bulk;

import java.util.ArrayList;
import java.util.List;

import it.cnr.si.spring.storage.annotation.StorageProperty;
import it.cnr.si.spring.storage.annotation.StorageType;
import it.cnr.contab.util00.bulk.storage.AllegatoGenericoBulk;

@StorageType(name="cmis:document")
public class AllegatoRichiestaDettaglioBulk extends AllegatoGenericoBulk {
	private static final long serialVersionUID = 1L;

	private String aspectName;
	
	private String nodeRefDettaglio;
	
	private Boolean isDetailAdded = false;
	
	public AllegatoRichiestaDettaglioBulk() {
		super();
	}

	public AllegatoRichiestaDettaglioBulk(String storageKey) {
		super(storageKey);
		setNodeRefDettaglio(storageKey);
	}
	 
	@StorageProperty(name= "cmis:secondaryObjectTypeIds")
	public List<String> getAspect() {
		 List<String> results = new ArrayList<String>();
		 results.add("P:cm:titled");
		 if (getAspectName() != null){
			 results.add(getAspectName());
		 }
		 return results;
	}

	public boolean isAllegatoEsistente()
	{
		return !getIsDetailAdded();
	}

	public String getAspectName() {
		return aspectName;
	}

	public void setAspectName(String aspectName) {
		this.aspectName = aspectName;
	}

	public Boolean getIsDetailAdded() {
		return isDetailAdded;
	}

	public void setIsDetailAdded(Boolean isDetailAdded) {
		this.isDetailAdded = isDetailAdded;
	}

	public String getNodeRefDettaglio() {
		return nodeRefDettaglio;
	}

	public void setNodeRefDettaglio(String nodeRefDettaglio) {
		this.nodeRefDettaglio = nodeRefDettaglio;
	}
}
