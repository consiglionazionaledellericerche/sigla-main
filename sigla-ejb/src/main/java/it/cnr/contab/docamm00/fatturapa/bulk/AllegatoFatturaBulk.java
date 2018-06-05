package it.cnr.contab.docamm00.fatturapa.bulk;

import java.util.ArrayList;
import java.util.List;

import it.cnr.si.spring.storage.annotation.StorageProperty;
import it.cnr.contab.util00.bulk.storage.AllegatoGenericoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.util.OrderedHashtable;

public class AllegatoFatturaBulk extends AllegatoGenericoBulk {
	private static final long serialVersionUID = 1L;

	public static OrderedHashtable aspectNamesKeys = new OrderedHashtable(),
			aspectNamesDecorrenzaTerminiKeys;

	static {
		aspectNamesKeys.put("P:sigla_fatture_attachment:durc","DURC");
		aspectNamesKeys.put("P:sigla_fatture_attachment:tacciabilita","Tracciabilità");
		aspectNamesKeys.put("P:sigla_fatture_attachment:prestazione_resa","Prestazione Resa");
		aspectNamesKeys.put("P:sigla_fatture_attachment:altro","Altro");

		aspectNamesDecorrenzaTerminiKeys = (OrderedHashtable) aspectNamesKeys.clone();
		aspectNamesDecorrenzaTerminiKeys.put("P:sigla_fatture_attachment:comunicazione_non_registrabilita","Comunicazione di non registrabilità");
	}
	private String aspectName;

	public AllegatoFatturaBulk() {
		super();
	}

	public AllegatoFatturaBulk(String storageKey) {
		super(storageKey);
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
	public static OrderedHashtable getAspectnameskeys() {
		return aspectNamesKeys;
	}

	public static OrderedHashtable getAspectnamesDecorrenzaTerminikeys() {
		return aspectNamesDecorrenzaTerminiKeys;
	}
	@Override
	public void validate() throws ValidationException {
		if (getAspectName() == null) {
			throw new ValidationException("Attenzione: selezionare la tipologia di File!");
		}
		super.validate();
	}
}