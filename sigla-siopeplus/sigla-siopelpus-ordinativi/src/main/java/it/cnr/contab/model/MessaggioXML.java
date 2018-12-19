package it.cnr.contab.model;

public class MessaggioXML<T extends Object> {
    private final String name;
    private final byte[] content;
    private final T object;

    public MessaggioXML(String name, byte[] content, T object) {
        this.name = name;
        this.content = content;
        this.object = object;
    }

    public String getName() {
        return name;
    }

    public byte[] getContent() {
        return content;
    }

    public T getObject() {
        return object;
    }
}
