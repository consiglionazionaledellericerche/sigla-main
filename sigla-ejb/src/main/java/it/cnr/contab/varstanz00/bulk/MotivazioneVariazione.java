package it.cnr.contab.varstanz00.bulk;

public enum MotivazioneVariazione {
	GENERICO("Variazione Generica", "GEN"),
	BANDO("Personale - Bando da pubblicare", "BAN"),
	PROROGA("Personale - Proroga", "PRG"),
	ALTRE_SPESE("Personale - Altre Spese", "ALT"),
	TRASFERIMENTO_AREA("Trasferimento Aree di Ricerca", "TAE"),
	TRASFERIMENTO_RAGIONERIA("Trasferimento Ragioneria", "TRG"),
	TRASFERIMENTO_AUTORIZZATO("Trasferimento In Deroga", "TAU");

    private final String label, value;

    private MotivazioneVariazione(String label, String value) {
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
        for (MotivazioneVariazione esito : MotivazioneVariazione.values()) {
            if (esito.label.equals(label))
                return esito.value;
        }
        throw new IllegalArgumentException("Esito no found for label: " + label);
    }
}