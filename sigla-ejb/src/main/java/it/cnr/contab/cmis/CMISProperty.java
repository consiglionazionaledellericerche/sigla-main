package it.cnr.contab.cmis;

public enum CMISProperty {
	
	//Struttura Organizzativa
	STRORGCDS_CODICE("strorgcds:codice"),
	STRORGCDS_DESCRIZIONE("strorgcds:descrizione"),

	STRORGUO_CODICE("strorguo:codice"),
	STRORGUO_DESCRIZIONE("strorguo:descrizione"),

	STRORGCDR_CODICE("strorgcdr:codice"),
	STRORGCDR_DESCRIZIONE("strorgcdr:descrizione"),
	
	//Variazioni Al Piano di Gestione
	VARPIANOGEST_ESERCIZIO("varpianogest:esercizio"),
	VARPIANOGEST_NUMEROVARIAZIONE("varpianogest:numeroVariazione");

	private final String value;

	private CMISProperty(String value) {
		this.value = value;
	}

	public String value() {
		return value;
	}

	public static CMISProperty fromValue(String v) {
		for (CMISProperty c : CMISProperty.values()) {
			if (c.value.equals(v)) {
				return c;
			}
		}
		throw new IllegalArgumentException(v);
	}
}
