package it.cnr.contab.util00.bulk.storage;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.Map;
import java.util.stream.Collectors;

import it.cnr.contab.util00.bulk.storage.enumeration.AllegatoGenericoType;

public class AllegatoGenericoTypeBulk extends AllegatoGenericoBulk {
	private static final long serialVersionUID = 1L;
	private String objectType;
	
    public final static Map<String,String> ti_allegatoKeys = Arrays.asList(AllegatoGenericoType.values())
            .stream()
            .collect(Collectors.toMap(
            		AllegatoGenericoType::value,
            		AllegatoGenericoType::label,
                    (oldValue, newValue) -> oldValue,
                    Hashtable::new
            ));
    
	public AllegatoGenericoTypeBulk() {
		super();
	}

	public static AllegatoGenericoTypeBulk construct(String storageKey){
		return new AllegatoGenericoTypeBulk(storageKey);
	}
	
	public AllegatoGenericoTypeBulk(String storageKey) {
		super(storageKey);
	}

	public String getObjectType() {
		return objectType;
	}
	
	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}
}
