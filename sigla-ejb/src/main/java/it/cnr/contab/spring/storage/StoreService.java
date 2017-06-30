package it.cnr.contab.spring.storage;

import it.cnr.contab.cmis.acl.ACLType;
import it.cnr.contab.cmis.acl.Permission;
import it.cnr.contab.spring.config.StorageObject;
import it.cnr.contab.spring.config.StoragePropertyNames;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mspasiano on 6/12/17.
 */
@Service
public class StoreService {
    public static final String BACKSLASH = "/";
    @Autowired
    private StorageService storageService;
    @Autowired
    private StoreBulkInfo storeBulkInfo;

    public String sanitizeFilename(String name) {
        name = name.trim();
        Pattern pattern = Pattern.compile("([\\/:@()&<>?\"])");
        Matcher matcher = pattern.matcher(name);
        if(!matcher.matches()){
            String str1 = matcher.replaceAll("_");
            return str1;
        } else {
            return name;
        }
    }

    public String sanitizeFolderName(String name) {
        name = name.trim();
        Pattern pattern = Pattern.compile("([\\/:@()&<>?\"])");
        Matcher matcher = pattern.matcher(name);
        if(!matcher.matches()){
            String str1 = matcher.replaceAll("'");
            return str1;
        } else {
            return name;
        }
    }

    public StorageObject getStorageObjectByPath(String path, boolean create) {
        return Optional.ofNullable(storageService.getObjectByPath(path))
                .orElseGet(() -> {
                    if (!create) return null;
                    Arrays.asList(path.split(BACKSLASH)).stream()
                            .filter(x -> x.length() > 0)
                            .forEach(x -> {
                                createFolderIfNotPresent(
                                        path.substring(0, Math.max(path.indexOf(x) -1, 0)),
                                        x, null, null
                                );
                            });
                    return storageService.getObjectByPath(path);
                });
    }

    public StorageObject getStorageObjectByPath(String path) {
        return getStorageObjectByPath(path, false);
    }

    public StorageObject getStorageObjectBykey(String key) {
        return storageService.getObject(key);
    }

    public StorageObject getStorageObjectBykey(String key, UsernamePasswordCredentials customCredentials) {
        return storageService.getObject(key, customCredentials);
    }

    public String createFolderIfNotPresent(String path, String folderName, String title, String description) {
        return createFolderIfNotPresent(path, folderName, title, description, null);
    }
    public String createFolderIfNotPresent(String path, String folderName, String title, String description, OggettoBulk oggettoBulk) {
        return createFolderIfNotPresent(path, folderName, title, description, oggettoBulk, null);
    }

    public String updateMetadataFromBulk(StorageObject storageObject, OggettoBulk oggettoBulk) throws StorageException{
        if (oggettoBulk != null){
            Map<String, Object> metadataProperties = new HashMap<String, Object>();
            List<String> aspectsToAdd = new ArrayList<String>();
            List<String> aspects = (List<String>)storageObject.getPropertyValue(StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value());
            Optional.ofNullable(storeBulkInfo.getType(oggettoBulk))
                    .ifPresent(type -> metadataProperties.put(StoragePropertyNames.OBJECT_TYPE_ID.value(), type));
            metadataProperties.putAll(storeBulkInfo.getPropertyValue(oggettoBulk));
            aspectsToAdd.addAll(storeBulkInfo.getAspect(oggettoBulk));
            metadataProperties.putAll(storeBulkInfo.getAspectPropertyValue(oggettoBulk));
            aspects.addAll(aspectsToAdd);
            metadataProperties.put(StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value(), aspects);
            storageService.updateProperties(storageObject,metadataProperties);
        }
        return storageObject.getPath();
    }

    public String createFolderIfNotPresent(String path, String folderName, String title, String description, OggettoBulk oggettoBulk, String objectTypeName) throws StorageException{
        Map<String, Object> metadataProperties = new HashMap<String, Object>();
        List<String> aspectsToAdd = new ArrayList<String>();
        try{
            final String folderPath = Optional.ofNullable(path)
                    .filter(s -> s.length() > 0)
                    .orElse(BACKSLASH);
            StorageObject parentObject = getStorageObjectByPath(folderPath, true);
            final String name = sanitizeFolderName(folderName);
            metadataProperties.put(StoragePropertyNames.NAME.value(), name);
            aspectsToAdd.add(StoragePropertyNames.ASPECT_TITLED.value());
            if (title != null)
                metadataProperties.put(StoragePropertyNames.TITLE.value(), title);
            if (description != null)
                metadataProperties.put(StoragePropertyNames.DESCRIPTION.value(), description);

            if (oggettoBulk != null) {
                metadataProperties.put(StoragePropertyNames.OBJECT_TYPE_ID.value(), storeBulkInfo.getType(oggettoBulk));
                metadataProperties.putAll(storeBulkInfo.getPropertyValue(oggettoBulk));
                aspectsToAdd.addAll(storeBulkInfo.getAspect(oggettoBulk));
                metadataProperties.putAll(storeBulkInfo.getAspectPropertyValue(oggettoBulk));
            } else {
                metadataProperties.put(StoragePropertyNames.OBJECT_TYPE_ID.value(), StoragePropertyNames.CMIS_FOLDER.value());
            }
            metadataProperties.put(StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value(), aspectsToAdd);
            return Optional.ofNullable(storageService.getObjectByPath(path.concat(BACKSLASH).concat(name)))
                    .map(storageObject -> {
                        if (oggettoBulk!=null){
                            List<String> aspects = (List<String>) storageObject.getPropertyValue(StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value());
                            aspects.addAll(aspectsToAdd);
                            metadataProperties.put(StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value(), aspects);
                            storageService.updateProperties(storageObject, metadataProperties);
                        }
                        return storageObject.getPath();
                    })
                    .orElseGet(() -> storageService.createFolder(folderPath, name, metadataProperties).getPath());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public CompletableFuture<Boolean> delete(StorageObject storageObject){
        return delete(storageObject.getKey());
    }

    public CompletableFuture<Boolean> delete(String key){
        return storageService.deleteAsync(key);
    }

    public InputStream getResource(StorageObject storageObject){
        return getResource(storageObject.getKey());
    }

    public InputStream getResource(String key){
        return storageService.getInputStream(key);
    }

    public InputStream getResource(String key, String versionId) {
        return storageService.getInputStream(key, versionId);
    }

    public InputStream getResource(String key, Boolean majorVersion) {
        return storageService.getInputStream(key, majorVersion);
    }

    public StorageObject storeSimpleDocument(OggettoBulk oggettoBulk, InputStream inputStream, String contentType, String name,
                                        String path, Permission... permissions) throws StorageException{
        return storeSimpleDocument(oggettoBulk, inputStream, contentType, name, path, false, permissions);
    }

    public StorageObject storeSimpleDocument(InputStream inputStream, String contentType, String path, Map<String, Object> metadataProperties) throws StorageException{
        StorageObject parentObject = getStorageObjectByPath(path, true);
        return storeSimpleDocument(inputStream, contentType, metadataProperties, parentObject);
    }

    public StorageObject storeSimpleDocument(InputStream inputStream, String contentType, Map<String, Object> metadataProperties, StorageObject parentObject) throws StorageException{
        return storageService.createDocument(inputStream, contentType, metadataProperties, parentObject, false);
    }

    public StorageObject storeSimpleDocument(OggettoBulk oggettoBulk, InputStream inputStream, String contentType, String name,
                                             String path, boolean makeVersionable, Permission... permissions) throws StorageException{
        return storeSimpleDocument(oggettoBulk, inputStream, contentType, name, path, storeBulkInfo.getType(oggettoBulk), makeVersionable, permissions);
    }

    public StorageObject storeSimpleDocument(OggettoBulk oggettoBulk, InputStream inputStream, String contentType, String name,
                                        String path, String objectTypeName, boolean makeVersionable, Permission... permissions) throws StorageException{
        StorageObject parentObject = getStorageObjectByPath(path, true);
        Map<String, Object> metadataProperties = new HashMap<String, Object>();
        name = sanitizeFilename(name);
        metadataProperties.put(StoragePropertyNames.NAME.value(), name);
        metadataProperties.put(StoragePropertyNames.OBJECT_TYPE_ID.value(), objectTypeName);
        metadataProperties.putAll(storeBulkInfo.getPropertyValue(oggettoBulk));
        metadataProperties.put(StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value(), storeBulkInfo.getAspect(oggettoBulk));
        metadataProperties.putAll(storeBulkInfo.getAspectPropertyValue(oggettoBulk));
        return storageService.createDocument(inputStream, contentType, metadataProperties, parentObject, makeVersionable, permissions);
    }

    public StorageObject restoreSimpleDocument(OggettoBulk oggettoBulk, InputStream inputStream, String contentType, String name,
                                          String path, boolean makeVersionable, Permission... permissions) throws StorageException{
        return restoreSimpleDocument(oggettoBulk, inputStream, contentType, name, path, storeBulkInfo.getType(oggettoBulk), makeVersionable, permissions);
    }

    public StorageObject restoreSimpleDocument(OggettoBulk oggettoBulk, InputStream inputStream, String contentType, String name,
                                          String path, String objectTypeName, boolean makeVersionable, Permission... permissions) throws StorageException{
        Optional<StorageObject> optStorageObject = Optional.ofNullable(getStorageObjectByPath(path.concat(BACKSLASH).concat(sanitizeFilename(name))));
        if (optStorageObject.isPresent()) {
            return storageService.updateStream(optStorageObject.get().getKey(), inputStream, contentType);
        } else {
            return storeSimpleDocument(oggettoBulk, inputStream, contentType, name, path, objectTypeName, makeVersionable, permissions);
        }
    }

    public void updateProperties(Map<String, Object> metadataProperties, StorageObject storageObject) throws StorageException{
        storageService.updateProperties(storageObject, metadataProperties);
    }

    public void updateProperties(OggettoBulk oggettoBulk, StorageObject storageObject) throws StorageException{
        try {
            Map<String, Object> metadataProperties = new HashMap<String, Object>();
            metadataProperties.put(StoragePropertyNames.OBJECT_TYPE_ID.value(), storeBulkInfo.getType(oggettoBulk));
            metadataProperties.putAll(storeBulkInfo.getPropertyValue(oggettoBulk));
            metadataProperties.put(StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value(), storeBulkInfo.getAspect(oggettoBulk));
            metadataProperties.putAll(storeBulkInfo.getAspectPropertyValue(oggettoBulk));
            updateProperties(metadataProperties, storageObject);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<StorageObject> getChildren(String key) {
        return storageService.getChildren(key);
    }

    public List<StorageObject> search(String query) {
        return storageService.search(query);
    }

    public InputStream zipContent(List<String> keys) {
        return storageService.zipContent(keys);
    }

    public String signDocuments(String json, String url) throws StorageException{
        return storageService.signDocuments(json, url);
    }

    public StorageObject updateStream(String key, InputStream inputStream, String contentType) throws StorageException{
        return storageService.updateStream(key, inputStream, contentType);
    }

    public boolean hasAspect(StorageObject storageObject, String aspect) {
        return storageObject.<List<String>>getPropertyValue(StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value()).contains(aspect);
    }

    public void addAspect(StorageObject storageObject, String aspect) {
        List<String> aspects = storageObject.<List<String>>getPropertyValue(StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value());
        aspects.add(aspect);
        updateProperties(Collections.singletonMap(StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value(), aspects), storageObject);
    }

    public void removeAspect(StorageObject storageObject, String aspect) {
        List<String> aspects = storageObject.<List<String>>getPropertyValue(StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value());
        aspects.remove(aspect);
        updateProperties(Collections.singletonMap(StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value(), aspects), storageObject);
    }

    public void copyNode(StorageObject source, StorageObject target) {
        storageService.copyNode(source, target);
    }

    public void addConsumerToEveryone(StorageObject storageObject) {
        addAcl(storageObject, Collections.singletonMap("GROUP_EVERYONE", ACLType.Consumer));
    }

    public void removeConsumerToEveryone(StorageObject storageObject){
        removeAcl(storageObject, Collections.singletonMap("GROUP_EVERYONE", ACLType.Consumer));
    }
    // per gestire gruppi diversi es. CONTRATTI
    public void addConsumer(StorageObject storageObject, String group ) {
        addAcl(storageObject, Collections.singletonMap(group, ACLType.Consumer));
    }
    public void removeConsumer(StorageObject storageObject, String group ) {
        removeAcl(storageObject, Collections.singletonMap(group, ACLType.Consumer));
    }

    private void removeAcl(StorageObject storageObject, Map<String, ACLType> permission) {
        managePermission(storageObject, permission, true);
    }

    private void addAcl(StorageObject storageObject, Map<String, ACLType> permission) {
        managePermission(storageObject, permission, false);
    }

    private void managePermission(StorageObject storageObject, Map<String, ACLType> permission, boolean remove) {
        storageService.managePermission(storageObject, permission, remove);
    }

    public void setInheritedPermission(StorageObject storageObject, Boolean inherited) {
        storageService.setInheritedPermission(storageObject, inherited);
    }

    public List<StorageObject> getRelationship(String key, String relationshipName, boolean fromTarget) {
        return storageService.getRelationship(key, relationshipName, fromTarget);
    }

    public List<StorageObject> getRelationship(String sourceNodeRef, String relationshipName) throws ApplicationException {
        return getRelationship(sourceNodeRef, relationshipName, false);
    }

    public List<StorageObject> getRelationshipFromTarget(String sourceNodeRef, String relationshipName) throws ApplicationException{
        return getRelationship(sourceNodeRef, relationshipName, true);
    }

    public void createRelationship(String source, String target, String relationshipName) {
        storageService.createRelationship(source, target, relationshipName);
    }

}
