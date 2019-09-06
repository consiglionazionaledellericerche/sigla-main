package it.cnr.contab.progettiric00.enumeration;

public enum AllegatoProgettoRimodulazioneType {
	RIMODULAZIONE("Rimodulazione", "D:sigla_progetti_attachment:rimodulazione_attestato"),
	PROROGA("Proroga", "D:sigla_progetti_attachment:rimodulazione_proroga"),
	AUTOMATICO("Stampa Automatica Rimodulazione", "D:sigla_progetti_attachment:rimodulazione_stampa_automatica"),
	GENERICO("Allegato Generico", "D:sigla_progetti_attachment:rimodulazione_allegato_generico");

    private final String label, value;

    private AllegatoProgettoRimodulazioneType(String label, String value) {
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
        for (AllegatoProgettoRimodulazioneType esito : AllegatoProgettoRimodulazioneType.values()) {
            if (esito.label.equals(label))
                return esito.value;
        }
        throw new IllegalArgumentException("Esito no found for label: " + label);
    }
}