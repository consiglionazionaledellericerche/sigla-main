package it.cnr.contab.util00.bulk.storage.enumeration;

public enum AllegatoGenericoType {
	GENERICO("Allegato Generico", "cmis:document");
		
    private final String label, value;

    private AllegatoGenericoType(String label, String value) {
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
        for (AllegatoGenericoType esito : AllegatoGenericoType.values()) {
            if (esito.label.equals(label))
                return esito.value;
        }
        throw new IllegalArgumentException("Esito no found for label: " + label);
    }
}