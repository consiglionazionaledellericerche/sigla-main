package it.cnr.contab.cmis;

public enum CMISRelationship {
	
	VARPIANOGEST_ALLEGATIVARBILANCIO("R:varpianogest:allegatiVarBilancio"),
	CNR_SIGNEDDOCUMENT("R:cnr:signedDocumentAss");
	
	private final String value;

	private CMISRelationship(String value) {
		this.value = value;
	}

	public String value() {
		return value;
	}

	public static CMISRelationship fromValue(String v) {
		for (CMISRelationship c : CMISRelationship.values()) {
			if (c.value.equals(v)) {
				return c;
			}
		}
		throw new IllegalArgumentException(v);
	}	
}
