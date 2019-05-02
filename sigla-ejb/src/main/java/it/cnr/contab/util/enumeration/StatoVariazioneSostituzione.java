package it.cnr.contab.util.enumeration;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.Map;
import java.util.stream.Collectors;

public enum StatoVariazioneSostituzione {
    DA_VARIARE("DA VARIARE", "DA_VARIARE"),
    VARIAZIONE_DEFINITIVA("VARIAZIONE DEFINITIVA", "VARIAZIONE_DEFINITIVA"),
    VARIAZIONE_TRASMESSA("VARIAZIONE TRASMESSA", "VARIAZIONE_TRASMESSA"),
    VARIATO("VARIATO", "VARIATO"),
    NON_VARIATO("NON VARIATO", "NON_VARIATO");

    private final String label, value;

    StatoVariazioneSostituzione(String label, String value) {
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
        for (StatoVariazioneSostituzione esito : StatoVariazioneSostituzione.values()) {
            if (esito.label.equals(label))
                return esito.value;
        }
        throw new IllegalArgumentException("Stato variazione sostituzione no found for label: " + label);
    }

    public final static Map<String,String> KEYS = Arrays.asList(StatoVariazioneSostituzione.values())
            .stream()
            .collect(Collectors.toMap(
                    StatoVariazioneSostituzione::value,
                    StatoVariazioneSostituzione::label,
                    (oldValue, newValue) -> oldValue,
                    Hashtable::new
            ));
}