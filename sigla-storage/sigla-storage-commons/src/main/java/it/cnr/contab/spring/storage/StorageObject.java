package it.cnr.contab.spring.storage;

import java.io.Serializable;
import java.util.Map;
import java.util.Optional;


/**
 * Created by mspasiano on 6/12/17.
 */
public class StorageObject implements Serializable {
    private String key;
    private String path;
    private  Map<String, ?> metadata;

    private StorageObject() {
        // No constructor without Object type
    }

    public StorageObject(String key, String path, Map<String, ?> metadata) {
        this.key = key;
        this.path = path;
        this.metadata = metadata;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getKey() {
        return key;
    }

    public String getPath() {
        return path;
    }

    public <T> T getPropertyValue(String key) {
        return Optional.ofNullable(metadata)
                .map(stringObjectMap -> (T)stringObjectMap.get(key))
                .orElse(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StorageObject that = (StorageObject) o;

        if (key != null ? !key.equals(that.key) : that.key != null) return false;
        if (path != null ? !path.equals(that.path) : that.path != null) return false;
        return metadata != null ? metadata.equals(that.metadata) : that.metadata == null;
    }

    @Override
    public int hashCode() {
        int result = key != null ? key.hashCode() : 0;
        result = 31 * result + (path != null ? path.hashCode() : 0);
        result = 31 * result + (metadata != null ? metadata.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "StorageObject{" +
                "key='" + key + '\'' +
                ", path='" + path + '\'' +
                ", metadata=" + metadata +
                '}';
    }
}
