package it.cnr.contab.progettiric00.enumeration;

public enum StatoProgettoRimodulazione {
	STATO_PROVVISORIO("Provvisorio", "P"),
	STATO_DEFINITIVO("Definitivo", "D"),
	STATO_VALIDATO("Validato", "V"),
	STATO_APPROVATO("Approvato", "A"),
	STATO_RESPINTO("Respinto", "R");

    private final String label, value;

    private StatoProgettoRimodulazione(String label, String value) {
        this.value = value;
        this.label = label;
    }

    public String value() {
        return value;
    }

    public String label() {
        return label;
    }

    public static String getValueFromLabel(String label) {
        for (StatoProgettoRimodulazione esito : StatoProgettoRimodulazione.values()) {
            if (esito.label.equals(label))
                return esito.value;
        }
        throw new IllegalArgumentException("Esito no found for label: " + label);
    }
}