package it.cnr.contab.cmis;

public enum CMISAspect {
	
	CNR_SIGNEDDOCUMENT("P:cnr:signedDocument"),
	SYS_ARCHIVED("P:sys:archived");
	
	private final String value;

	private CMISAspect(String value) {
		this.value = value;
	}

	public String value() {
		return value;
	}

	public static CMISAspect fromValue(String v) {
		for (CMISAspect c : CMISAspect.values()) {
			if (c.value.equals(v)) {
				return c;
			}
		}
		throw new IllegalArgumentException(v);
	}	
}
