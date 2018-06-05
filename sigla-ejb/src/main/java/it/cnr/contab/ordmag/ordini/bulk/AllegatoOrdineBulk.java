package it.cnr.contab.ordmag.ordini.bulk;

import java.util.ArrayList;
import java.util.List;

import it.cnr.si.spring.storage.annotation.StorageProperty;
import it.cnr.contab.util00.bulk.storage.AllegatoGenericoBulk;
import it.cnr.contab.ordmag.ordini.service.OrdineAcqCMISService;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.util.OrderedHashtable;

public class AllegatoOrdineBulk extends AllegatoGenericoBulk {
	private static final long serialVersionUID = 1L;

	public static OrderedHashtable aspectNamesKeys = new OrderedHashtable();

	static {
		aspectNamesKeys.put(OrdineAcqCMISService.ASPECT_ALLEGATI_ORDINI,"Allegati all'Ordine");
	}
	private String aspectName;
	
	public AllegatoOrdineBulk() {
		super();
		setAspectName(OrdineAcqCMISService.ASPECT_ALLEGATI_ORDINI);
	}

	public AllegatoOrdineBulk(String storageKey) {
		super(storageKey);
		setAspectName(OrdineAcqCMISService.ASPECT_ALLEGATI_ORDINI);
	}
	
	public String getAspectName() {
		return aspectName;
	}
	public void setAspectName(String aspectName) {
		this.aspectName = aspectName;
	}

	@StorageProperty(name="cmis:secondaryObjectTypeIds")
	public List<String> getAspect() {
		 List<String> results = new ArrayList<String>();
		 results.add("P:cm:titled");
		 results.add(getAspectName());
		 return results;
	}

	public static OrderedHashtable getAspectNamesKeys() {
		return aspectNamesKeys;
	}

	@Override
	public void validate() throws ValidationException {
		if (getAspectName() == null) {
			throw new ValidationException("Attenzione: selezionare la tipologia di File!");
		}		
		super.validate();
	}
	public boolean isAllegatoEsistente()
	{
		if(this.isToBeCreated())
			return false;

		return true;
	}
}
