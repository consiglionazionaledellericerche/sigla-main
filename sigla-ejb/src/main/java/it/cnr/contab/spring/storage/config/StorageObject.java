package it.cnr.contab.spring.storage.config;

import java.io.Serializable;
import java.util.Map;
import java.util.Optional;


/**
 * Created by mspasiano on 6/12/17.
 */
public class StorageObject{
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

}
