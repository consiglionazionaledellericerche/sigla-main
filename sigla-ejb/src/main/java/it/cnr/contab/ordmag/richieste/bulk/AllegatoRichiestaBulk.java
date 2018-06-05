package it.cnr.contab.ordmag.richieste.bulk;

import java.util.ArrayList;
import java.util.List;

import it.cnr.si.spring.storage.StorageObject;
import it.cnr.si.spring.storage.annotation.StorageProperty;
import it.cnr.contab.util00.bulk.storage.AllegatoGenericoBulk;
import it.cnr.contab.ordmag.richieste.service.RichiesteCMISService;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.util.OrderedHashtable;

public class AllegatoRichiestaBulk extends AllegatoGenericoBulk {
	private static final long serialVersionUID = 1L;

	public static OrderedHashtable aspectNamesKeys = new OrderedHashtable();

	static {
		aspectNamesKeys.put(RichiesteCMISService.ASPECT_ALLEGATI_RICHIESTA_ORDINI,"Allegati alla Richiesta");
	}
	private String aspectName;
	
	public AllegatoRichiestaBulk() {
		super();
		setAspectName(RichiesteCMISService.ASPECT_ALLEGATI_RICHIESTA_ORDINI);
	}

	public AllegatoRichiestaBulk(StorageObject storageObject) {
		super(storageObject.getKey());
		setAspectName(RichiesteCMISService.ASPECT_ALLEGATI_RICHIESTA_ORDINI);
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
