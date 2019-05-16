package it.cnr.contab.progettiric00.enumeration;

public enum StatoProgetto {
	STATO_INIZIALE("Iniziale", "INI"),
	STATO_NEGOZIAZIONE("Negoziazione", "NEG"),
	STATO_APPROVATO("Approvato", "APP"),
	STATO_ANNULLATO("Annullato", "ANN"),
	STATO_MIGRAZIONE("Migrazione", "MIG"),
	STATO_CHIUSURA("Chiuso", "CHI");
	
    private final String label, value;

    private StatoProgetto(String label, String value) {
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
        for (StatoProgetto esito : StatoProgetto.values()) {
            if (esito.label.equals(label))
                return esito.value;
        }
        throw new IllegalArgumentException("Esito no found for label: " + label);
    }
}