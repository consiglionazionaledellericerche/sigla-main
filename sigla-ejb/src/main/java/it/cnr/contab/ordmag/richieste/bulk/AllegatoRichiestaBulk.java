package it.cnr.contab.ordmag.richieste.bulk;

import java.util.ArrayList;
import java.util.List;

import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.commons.PropertyIds;

import it.cnr.contab.cmis.annotation.CMISProperty;
import it.cnr.contab.ordmag.richieste.service.RichiesteCMISService;
import it.cnr.contab.util00.bulk.cmis.AllegatoGenericoBulk;
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

	public AllegatoRichiestaBulk(Document node) {
		super(node);
		setAspectName(RichiesteCMISService.ASPECT_ALLEGATI_RICHIESTA_ORDINI);
	}
	
	public String getAspectName() {
		return aspectName;
	}
	public void setAspectName(String aspectName) {
		this.aspectName = aspectName;
	}
	@CMISProperty(name=PropertyIds.SECONDARY_OBJECT_TYPE_IDS)
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
