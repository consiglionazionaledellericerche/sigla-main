package it.cnr.contab.progettiric00.core.bulk;

import it.cnr.contab.util00.bulk.storage.AllegatoGenericoBulk;
import it.cnr.si.spring.storage.StorageObject;
import it.cnr.si.spring.storage.config.StoragePropertyNames;

public class AllegatoProgettoBulk extends AllegatoGenericoBulk {
	private static final long serialVersionUID = 1L;
	private String type;

	private static final java.util.Dictionary ti_allegatoKeys =  new it.cnr.jada.util.OrderedHashtable();
	private static final java.util.Dictionary ti_allegato_rimodulazioneKeys =  new it.cnr.jada.util.OrderedHashtable();

	final public static String GENERICO = "D:sigla_progetti_attachment:allegato_generico";
	final public static String RIMODULAZIONE_ATTESTATO = "D:sigla_progetti_attachment:rimodulazione_attestato";
	final public static String RIMODULAZIONE_GENERICO = "D:sigla_progetti_attachment:rimodulazione_allegato_generico";

	static {
		ti_allegatoKeys.put(RIMODULAZIONE_ATTESTATO,"Rimodulazione - Attestato");
		ti_allegatoKeys.put(RIMODULAZIONE_GENERICO,"Rimodulazione - Generico");
		ti_allegatoKeys.put(GENERICO,"Generico");

		ti_allegato_rimodulazioneKeys.put(RIMODULAZIONE_ATTESTATO,"Attestato");
		ti_allegato_rimodulazioneKeys.put(RIMODULAZIONE_GENERICO,"Generico");
	}

	public final java.util.Dictionary getTi_allegatoKeys() {
		return ti_allegatoKeys;
	}

	public final java.util.Dictionary getTi_allegato_rimodulazioneKeys() {
		return ti_allegato_rimodulazioneKeys;
	}
	
	public AllegatoProgettoBulk() {
		super();
	}

	public static AllegatoProgettoBulk construct(StorageObject storageObject) {
		return new AllegatoProgettoBulk(storageObject);
	}

	public AllegatoProgettoBulk(String storageKey) {
		super(storageKey);
	}

	public AllegatoProgettoBulk(StorageObject storageObject){
		super();
		setType(storageObject.getPropertyValue(StoragePropertyNames.OBJECT_TYPE_ID.value()));
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public boolean isTypeEnabled(){
		return !isToBeCreated();
	}	
}