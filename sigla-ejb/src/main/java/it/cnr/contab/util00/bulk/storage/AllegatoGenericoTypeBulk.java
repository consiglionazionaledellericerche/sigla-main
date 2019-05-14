package it.cnr.contab.util00.bulk.storage;

public class AllegatoGenericoTypeBulk extends AllegatoGenericoBulk {
	private static final long serialVersionUID = 1L;
	private String objectType;
	
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
