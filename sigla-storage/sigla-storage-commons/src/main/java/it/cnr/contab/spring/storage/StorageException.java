package it.cnr.contab.spring.storage;

/**
 * Created by mspasiano on 6/24/17.
 */
public class StorageException extends RuntimeException {
    private final Type type;

    public enum Type {
        CONSTRAINT_VIOLATED, CONTENT_ALREDY_EXISTS, INVALID_ARGUMENTS, UNAUTHORIZED, NOT_FOUND, GENERIC
    }

    public StorageException(Type type, Throwable throwable) {
        super(throwable);
        this.type = type;
    }

    public StorageException(Type type, String message, Throwable throwable) {
        super(message, throwable);
        this.type = type;
    }

    public StorageException(Type type, String message) {
        super(message);
        this.type = type;
    }

    public Type getType() {
        return type;
    }
}
