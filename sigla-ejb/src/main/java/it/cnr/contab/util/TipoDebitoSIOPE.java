package it.cnr.contab.util;

public enum TipoDebitoSIOPE {
    COMMERCIALE("COMMERCIALE", "C"),
    NON_COMMERCIALE("NON COMMERCIALE", "N"),
    IVA("IVA", "I");

    private final String label, value;

    private TipoDebitoSIOPE(String label, String value) {
        this.value = value;
        this.label = label;
    }

    public String value() {
        return value;
    }

    public String label() {
        return label;
    }

    public static TipoDebitoSIOPE getValueFrom(String value) {
        for (TipoDebitoSIOPE esito : TipoDebitoSIOPE.values()) {
            if (esito.value.equals(value))
                return esito;
        }
        throw new IllegalArgumentException("TipoDebitoSIOPE no found for value: " + value);
    }
}
