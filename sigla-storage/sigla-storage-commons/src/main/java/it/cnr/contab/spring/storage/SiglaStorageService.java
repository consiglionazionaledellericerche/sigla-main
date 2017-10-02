package it.cnr.contab.spring.storage;

import org.apache.http.auth.UsernamePasswordCredentials;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Created by mspasiano on 6/5/17.
 */
public interface SiglaStorageService {


    String SUFFIX = "/";
    Pattern UUID_PATTERN = Pattern.compile("[a-f0-9]{8}-[a-f0-9]{4}-[12345][a-f0-9]{3}-[89aAbB][a-f0-9]{3}-[a-f0-9]{12}");

     enum StoreType {
        CMIS, S3, AZURE
    }

    enum ACLType {
        Consumer, Editor, Collaborator, Coordinator, Contributor, FullControl, Read, Write
    }

    class Permission {
        private String userName;
        private ACLType role;

        public static Permission construct(String userName, ACLType role){
            return new Permission(userName, role);
        }

        protected Permission(String userName, ACLType role) {
            super();
            this.userName = userName;
            this.role = role;
        }

        public String getUserName() {
            return userName;
        }
        public void setUserName(String userName) {
            this.userName = userName;
        }
        public ACLType getRole() {
            return role;
        }
        public void setRole(ACLType role) {
            this.role = role;
        }

        public static Map<String, ACLType> convert(Permission... permissions) {
            return Stream.of(permissions)
                    .collect(HashMap::new, (m,v)->m.put(v.getUserName(), v.getRole()), HashMap::putAll);
        }

    }
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
     * @param path
     * @param makeVersionable
     * @param permissions
     * @return StorageObject
     */
    StorageObject createDocument(InputStream inputStream, String contentType, Map<String, Object> metadataProperties, StorageObject parentObject, String path, boolean makeVersionable, Permission... permissions);
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
    Boolean delete(String id);

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
    StorageObject getObjectByPath(String path, boolean isFolder);

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
    InputStream zipContent(List<String> keys, String name);

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

    StoreType getStoreType();
    /**
     * Inizialize service
     */
    void init();

    default boolean isUUID(String uuid) {
        return UUID_PATTERN.matcher(uuid).matches();
    }

}
