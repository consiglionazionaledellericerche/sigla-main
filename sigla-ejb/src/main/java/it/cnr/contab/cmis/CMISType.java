package it.cnr.contab.cmis;

public enum CMISType {
	
	CNR_ENVELOPEDDOCUMENT("D:cnr:envelopedDocument");
	
	private final String value;

	private CMISType(String value) {
		this.value = value;
	}

	public String value() {
		return value;
	}

	public static CMISType fromValue(String v) {
		for (CMISType c : CMISType.values()) {
			if (c.value.equals(v)) {
				return c;
			}
		}
		throw new IllegalArgumentException(v);
	}	
}
