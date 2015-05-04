package it.cnr.contab.docamm00.fatturapa.bulk;

import java.util.ArrayList;
import java.util.List;

import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.commons.PropertyIds;

import it.cnr.contab.cmis.annotation.CMISProperty;
import it.cnr.contab.util00.bulk.cmis.AllegatoGenericoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.util.OrderedHashtable;

public class AllegatoFatturaBulk extends AllegatoGenericoBulk {
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unchecked")
	public static final java.util.Dictionary<String, String> aspectNamesKeys = new OrderedHashtable();
	static {
		aspectNamesKeys.put("P:sigla_fatture_attachment:durc","DURC");
		aspectNamesKeys.put("P:sigla_fatture_attachment:tacciabilita","Tracciabilità");
		aspectNamesKeys.put("P:sigla_fatture_attachment:prestazione_resa","Prestazione Resa");
		aspectNamesKeys.put("P:sigla_fatture_attachment:altro","Altro");		
	}
	private String aspectName;
	
	public AllegatoFatturaBulk() {
		super();
	}

	public AllegatoFatturaBulk(Document node) {
		super(node);
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
	public static java.util.Dictionary<String, String> getAspectnameskeys() {
		return aspectNamesKeys;
	}	
	@Override
	public void validate() throws ValidationException {
		if (aspectName == null) {
			throw new ValidationException("Attenzione: selezionare la tipologia di File!");
		}		
		super.validate();
	}
}
