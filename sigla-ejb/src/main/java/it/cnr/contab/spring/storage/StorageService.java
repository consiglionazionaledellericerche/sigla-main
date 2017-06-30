package it.cnr.contab.spring.storage;

import it.cnr.contab.cmis.acl.ACLType;
import it.cnr.contab.cmis.acl.Permission;
import it.cnr.contab.doccont00.intcass.bulk.PdfSignApparence;
import it.cnr.contab.spring.config.StorageObject;
import org.apache.http.auth.UsernamePasswordCredentials;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Pattern;

/**
 * Created by mspasiano on 6/5/17.
 */
public interface StorageService {

    Pattern UUID_PATTERN = Pattern.compile("[a-f0-9]{8}-[a-f0-9]{4}-[12345][a-f0-9]{3}-[89aAbB][a-f0-9]{3}-[a-f0-9]{12}");

    /**
     * create a new object
     *
     * @param inputStream object input stream
     * @param metadata metadata
     * @return a void CompletableFuture
     */
    CompletableFuture<Void> createAsync(InputStream inputStream, String name, Map<String, String> metadata);

    /**
     * create a new folder
     * @param path
     * @param name
     * @param metadata
     * @return StorageObject
     */
    StorageObject createFolder(String path, String name, Map<String, Object> metadata);

    /**
     * create a new document
     * @param inputStream
     * @param contentType
     * @param metadataProperties
     * @param parentObject
     * @param makeVersionable
     * @param permissions
     * @return StorageObject
     */
    StorageObject createDocument(InputStream inputStream, String contentType, Map<String, Object> metadataProperties, StorageObject parentObject, boolean makeVersionable, Permission... permissions);
    /**
     * properties of store object
     * @param storageObject
     * @param metadataProperties
     * @return StorageObject
     */
    void updateProperties(StorageObject storageObject, Map<String, Object> metadataProperties);
    /**
     * Update stream of document
     * @param key
     * @param inputStream
     * @param contentType
     * @return StorageObject
     */
    StorageObject updateStream(String key, InputStream inputStream, String contentType);
    /**
     * get object input stream
     *
     * @param name object name
     * @return object InputStream
     */
    InputStream getInputStream(String name);

    /**
     * get object input stream for specified version
     * @param key
     * @param versionId
     * @return
     */
    InputStream getInputStream(String key, String versionId);

    /**
     * get object input stream for major version
     * @param key
     * @param majorVersion
     * @return
     */
    InputStream getInputStream(String key, Boolean majorVersion);
    /**
     * delete an object
     *
     * @param id object id
     * @return a CompletableFuture wrapping true if object exists
     */
    CompletableFuture<Boolean> deleteAsync(String id);

    /**
     * delete an existing object
     *
     * @param name file identifier
     * @return a CompletableFuture wrapping true if object exists
     */
    CompletableFuture<Boolean> scheduledDeleteAsync(String name);

    /**
     *
     * @param id
     * @return
     */
    StorageObject getObject(String id);
    /**
     *
     * @param id
     * @param customCredentials
     * @return
     */
    StorageObject getObject(String id, UsernamePasswordCredentials customCredentials);
    /**
     *
     * @param path
     * @return
     */
    StorageObject getObjectByPath(String path);

    /**
     * retrieve all children
     * @param key
     * @return
     */
    List<StorageObject> getChildren(String key);

    /**
     * search documents or folders
     * @param query
     * @return
     */
    List<StorageObject> search(String query);

    /**
     * ZIP content
     * @param keys
     * @return
     */
    InputStream zipContent(List<String> keys);

    /**
     * Sign documents
     * @param json
     * @param url
     * @return message error or null
     */
    String signDocuments(String json, String url);

    /**
     * Link object
     * @param source
     * @param target
     */
    void copyNode(StorageObject source, StorageObject target);

    /**
     * Manage permission
     * @param storageObject
     * @param permission
     * @param remove
     */
    void managePermission(StorageObject storageObject, Map<String, ACLType> permission, boolean remove);

    /**
     *
     * @param storageObject
     * @param inherited
     */
    void setInheritedPermission(StorageObject storageObject, Boolean inherited);

    /**
     *
     * @param key
     * @param relationshipName
     * @param fromTarget
     * @return
     */
    List<StorageObject> getRelationship(String key, String relationshipName, boolean fromTarget);

    /**
     *
     * @param source
     * @param target
     * @param relationshipName
     */
    void createRelationship(String source, String target, String relationshipName);
    /**
     * Inizialize service
     */
    void init();

    default boolean isUUID(String uuid) {
        return UUID_PATTERN.matcher(uuid).matches();
    }

}
