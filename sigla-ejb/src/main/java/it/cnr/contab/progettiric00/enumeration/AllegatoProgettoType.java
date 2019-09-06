package it.cnr.contab.progettiric00.enumeration;

public enum AllegatoProgettoType {
	PROVV_COSTITUZIONE("Provvedimento di Costituzione", "D:sigla_progetti_attachment:provvedimento_costituzione"),
	RICHIESTA_ANTICIPO("Richiesta di Anticipo", "D:sigla_progetti_attachment:richiesta_anticipo"),
	RENDICONTAZIONE("Rendicontazione", "D:sigla_progetti_attachment:rendicontazione"),
	STRALCIO("Stralcio", "D:sigla_progetti_attachment:stralcio"),
	CONTRODEDUZIONE("Controdeduzione", "D:sigla_progetti_attachment:controdeduzioni"),
	FINAL_STATEMENT_PAYMENT("Final Statement Payment", "D:sigla_progetti_attachment:final_statement_payment"),
	GENERICO("Allegato Generico", "D:sigla_progetti_attachment:allegato_generico");
		
    private final String label, value;

    private AllegatoProgettoType(String label, String value) {
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
        for (AllegatoProgettoType esito : AllegatoProgettoType.values()) {
            if (esito.label.equals(label))
                return esito.value;
        }
        throw new IllegalArgumentException("Esito no found for label: " + label);
    }
}