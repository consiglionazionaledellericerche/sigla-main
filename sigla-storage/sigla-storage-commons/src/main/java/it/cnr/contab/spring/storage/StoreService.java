package it.cnr.contab.spring.storage;

import com.google.gson.GsonBuilder;
import it.cnr.contab.doccont00.intcass.bulk.PdfSignApparence;
import it.cnr.contab.spring.storage.config.StoragePropertyNames;
import it.cnr.contab.util.SignP7M;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.firma.arss.ArubaSignServiceClient;
import it.cnr.jada.firma.arss.ArubaSignServiceException;
import org.apache.commons.io.IOUtils;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by mspasiano on 6/12/17.
 */
@Service
public class StoreService {
    @Autowired
    private SiglaStorageService siglaStorageService;
    @Autowired
    private StoreBulkInfo storeBulkInfo;
    @Autowired
    private ArubaSignServiceClient arubaSignServiceClient;
    @Value("${sign.document.png.url}")
    private String signDocumentURL;

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

    public StorageObject getStorageObjectByPath(String path, boolean isFolder, boolean create) {
        return Optional.ofNullable(siglaStorageService.getObjectByPath(path, isFolder))
                .orElseGet(() -> {
                    if (!create) return null;
                    final List<String> names = Arrays.asList(path.split(SiglaStorageService.SUFFIX));
                    AtomicInteger atomicInteger = new AtomicInteger(0);
                    names.stream()
                            .filter(name -> name.length() > 0)
                            .forEach(name -> {
                                atomicInteger.getAndIncrement();
                                createFolderIfNotPresent(
                                        Optional.ofNullable(names.stream()
                                                .limit(atomicInteger.longValue())
                                                .reduce((a,b) -> a + SiglaStorageService.SUFFIX + b)
                                                .get())
                                                .filter(s -> s.length() > 0)
                                                .orElse(SiglaStorageService.SUFFIX)
                                        ,name, null, null);
                            });
                    if (create) {
                        return Optional.ofNullable(siglaStorageService.getObjectByPath(path, true))
                                .orElse(new StorageObject(path, path, Collections.emptyMap()));
                    }
                    return siglaStorageService.getObjectByPath(path, true);
                });
    }

    public StorageObject getStorageObjectByPath(String path, boolean create) {
        return getStorageObjectByPath(path, false, create);
    }

    public StorageObject getStorageObjectByPath(String path) {
        return getStorageObjectByPath(path, false);
    }

    public StorageObject getStorageObjectBykey(String key) {
        return siglaStorageService.getObject(key);
    }

    public StorageObject getStorageObjectBykey(String key, UsernamePasswordCredentials customCredentials) {
        return siglaStorageService.getObject(key, customCredentials);
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
            siglaStorageService.updateProperties(storageObject,metadataProperties);
        }
        return storageObject.getPath();
    }

    public String createFolderIfNotPresent(String path, String folderName, String title, String description, OggettoBulk oggettoBulk, String objectTypeName) throws StorageException{
        Map<String, Object> metadataProperties = new HashMap<String, Object>();
        List<String> aspectsToAdd = new ArrayList<String>();
        try{
            final StorageObject parentObject = getStorageObjectByPath(path, true, true);
            final String name = sanitizeFolderName(folderName);
            metadataProperties.put(StoragePropertyNames.NAME.value(), name);
            if (title != null || description != null) {
                aspectsToAdd.add(StoragePropertyNames.ASPECT_TITLED.value());
                metadataProperties.put(StoragePropertyNames.TITLE.value(), title);
                metadataProperties.put(StoragePropertyNames.DESCRIPTION.value(), description);
            }
            if (oggettoBulk != null) {
                metadataProperties.put(StoragePropertyNames.OBJECT_TYPE_ID.value(), storeBulkInfo.getType(oggettoBulk));
                metadataProperties.putAll(storeBulkInfo.getPropertyValue(oggettoBulk));
                aspectsToAdd.addAll(storeBulkInfo.getAspect(oggettoBulk));
                metadataProperties.putAll(storeBulkInfo.getAspectPropertyValue(oggettoBulk));
            } else {
                metadataProperties.put(StoragePropertyNames.OBJECT_TYPE_ID.value(), StoragePropertyNames.CMIS_FOLDER.value());
            }
            Optional.ofNullable(aspectsToAdd)
                    .filter(list -> !list.isEmpty())
                    .ifPresent(list -> {
                        metadataProperties.put(StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value(), list);
                    });
            return createFolderIfNotPresent(parentObject.getPath(), name, metadataProperties);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String createFolderIfNotPresent(String path, String name, Map<String, Object> metadataProperties) {
        return Optional.ofNullable(siglaStorageService.getObjectByPath(
                path.concat(path.equals(SiglaStorageService.SUFFIX)? "" : SiglaStorageService.SUFFIX).concat(name), true
        ))
                .map(StorageObject::getPath)
                .orElseGet(() -> siglaStorageService.createFolder(path, name, metadataProperties).getPath());
    }

    public Boolean delete(StorageObject storageObject){
        return delete(storageObject.getKey());
    }

    public Boolean delete(String key){
        return siglaStorageService.delete(key);
    }

    public InputStream getResource(StorageObject storageObject){
        return getResource(storageObject.getKey());
    }

    public InputStream getResource(String key){
        return siglaStorageService.getInputStream(key);
    }

    public InputStream getResource(String key, String versionId) {
        return siglaStorageService.getInputStream(key, versionId);
    }

    public InputStream getResource(String key, Boolean majorVersion) {
        return siglaStorageService.getInputStream(key, majorVersion);
    }

    public StorageObject storeSimpleDocument(OggettoBulk oggettoBulk, InputStream inputStream, String contentType, String name,
                                        String path, SiglaStorageService.Permission... permissions) throws StorageException{
        return storeSimpleDocument(oggettoBulk, inputStream, contentType, name, path, false, permissions);
    }

    public StorageObject storeSimpleDocument(InputStream inputStream, String contentType, String path, Map<String, Object> metadataProperties) throws StorageException{
        StorageObject parentObject = getStorageObjectByPath(path, true, true);
        return storeSimpleDocument(inputStream, contentType, metadataProperties, parentObject);
    }

    public StorageObject storeSimpleDocument(InputStream inputStream, String contentType, Map<String, Object> metadataProperties, StorageObject parentObject) throws StorageException{
        return siglaStorageService.createDocument(inputStream, contentType, metadataProperties, parentObject, parentObject.getPath(), false);
    }

    public StorageObject storeSimpleDocument(OggettoBulk oggettoBulk, InputStream inputStream, String contentType, String name,
                                             String path, boolean makeVersionable, SiglaStorageService.Permission... permissions) throws StorageException{
        return storeSimpleDocument(oggettoBulk, inputStream, contentType, name, path, storeBulkInfo.getType(oggettoBulk), makeVersionable, permissions);
    }

    public StorageObject storeSimpleDocument(OggettoBulk oggettoBulk, InputStream inputStream, String contentType, String name,
                                        String path, String objectTypeName, boolean makeVersionable, SiglaStorageService.Permission... permissions) throws StorageException{
        StorageObject parentObject = getStorageObjectByPath(path, true, true);
        Map<String, Object> metadataProperties = new HashMap<String, Object>();
        name = sanitizeFilename(name);
        metadataProperties.put(StoragePropertyNames.NAME.value(), name);
        metadataProperties.put(StoragePropertyNames.OBJECT_TYPE_ID.value(), objectTypeName);
        metadataProperties.putAll(storeBulkInfo.getPropertyValue(oggettoBulk));
        metadataProperties.put(StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value(), 
                Optional.ofNullable(metadataProperties.get(StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value()))
                    .map(o -> (List<String>)o)
                    .map(aspects -> {
                        aspects.addAll(storeBulkInfo.getAspect(oggettoBulk));
                        return aspects;
                    })
                    .orElse(storeBulkInfo.getAspect(oggettoBulk))
        );
        metadataProperties.putAll(storeBulkInfo.getAspectPropertyValue(oggettoBulk));
        return siglaStorageService.createDocument(inputStream, contentType, metadataProperties, parentObject, path, makeVersionable, permissions);
    }

    public StorageObject restoreSimpleDocument(OggettoBulk oggettoBulk, InputStream inputStream, String contentType, String name,
                                          String path, boolean makeVersionable, SiglaStorageService.Permission... permissions) throws StorageException{
        return restoreSimpleDocument(oggettoBulk, inputStream, contentType, name, path, storeBulkInfo.getType(oggettoBulk), makeVersionable, permissions);
    }

    public StorageObject restoreSimpleDocument(OggettoBulk oggettoBulk, InputStream inputStream, String contentType, String name,
                                          String path, String objectTypeName, boolean makeVersionable, SiglaStorageService.Permission... permissions) throws StorageException{
        Optional<StorageObject> optStorageObject = Optional.ofNullable(getStorageObjectByPath(path.concat(SiglaStorageService.SUFFIX).concat(sanitizeFilename(name))));
        if (optStorageObject.isPresent()) {
            return siglaStorageService.updateStream(optStorageObject.get().getKey(), inputStream, contentType);
        } else {
            return storeSimpleDocument(oggettoBulk, inputStream, contentType, name, path, objectTypeName, makeVersionable, permissions);
        }
    }

    public void updateProperties(Map<String, Object> metadataProperties, StorageObject storageObject) throws StorageException{
        siglaStorageService.updateProperties(storageObject, metadataProperties);
    }

    public void updateProperties(OggettoBulk oggettoBulk, StorageObject storageObject) throws StorageException{
        Map<String, Object> metadataProperties = new HashMap<String, Object>();
        metadataProperties.put(StoragePropertyNames.OBJECT_TYPE_ID.value(), storeBulkInfo.getType(oggettoBulk));
        metadataProperties.putAll(storeBulkInfo.getPropertyValue(oggettoBulk));
        metadataProperties.put(StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value(),
                Optional.ofNullable(metadataProperties.get(StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value()))
                        .map(o -> (List<String>)o)
                        .map(aspects -> {
                            aspects.addAll(storeBulkInfo.getAspect(oggettoBulk));
                            return aspects;
                        })
                        .orElse(storeBulkInfo.getAspect(oggettoBulk))
        );
        metadataProperties.putAll(storeBulkInfo.getAspectPropertyValue(oggettoBulk));
        updateProperties(metadataProperties, storageObject);
    }

    public List<StorageObject> getChildren(String key) {
        return siglaStorageService.getChildren(key);
    }

    public List<StorageObject> getChildren(String key, int depth) {
        return siglaStorageService.getChildren(key, depth);
    }

    public List<StorageObject> search(String query) {
        return siglaStorageService.search(query);
    }

    public String signDocuments(PdfSignApparence pdfSignApparence, String url) throws StorageException{
        if (siglaStorageService.getStoreType().equals(SiglaStorageService.StoreType.CMIS)) {
            return signDocuments(new GsonBuilder().create().toJson(pdfSignApparence), url);
        } else {
            List<byte[]> bytes = Optional.ofNullable(pdfSignApparence)
                    .map(pdfSignApparence1 -> pdfSignApparence1.getNodes())
                    .map(list ->
                            list.stream()
                                    .map(s -> siglaStorageService.getInputStream(s))
                                    .map(inputStream -> {
                                        try {
                                            return IOUtils.toByteArray(inputStream);
                                        } catch (IOException e) {
                                           throw new StorageException(StorageException.Type.GENERIC, e);
                                        }
                                    })
                                    .collect(Collectors.toList()))
                    .orElse(Collections.emptyList());
            try {
                it.cnr.jada.firma.arss.stub.PdfSignApparence apparence = new it.cnr.jada.firma.arss.stub.PdfSignApparence();
                apparence.setImage(signDocumentURL);
                apparence.setLeftx(pdfSignApparence.getApparence().getLeftx());
                apparence.setLefty(pdfSignApparence.getApparence().getLefty());
                apparence.setLocation(pdfSignApparence.getApparence().getLocation());
                apparence.setPage(pdfSignApparence.getApparence().getPage());
                apparence.setReason(pdfSignApparence.getApparence().getReason());
                apparence.setRightx(pdfSignApparence.getApparence().getRightx());
                apparence.setRighty(pdfSignApparence.getApparence().getRighty());
                apparence.setTesto(pdfSignApparence.getApparence().getTesto());

                List<byte[]> bytesSigned = arubaSignServiceClient.pdfsignatureV2Multiple(
                        pdfSignApparence.getUsername(),
                        pdfSignApparence.getPassword(),
                        pdfSignApparence.getOtp(),
                        bytes,
                        apparence
                );
                for (int i = 0; i < pdfSignApparence.getNodes().size(); i++) {
                    siglaStorageService.updateStream(
                            pdfSignApparence.getNodes().get(i),
                            new ByteArrayInputStream(bytesSigned.get(i)),
                            MimeTypes.PDF.mimetype()
                    );
                }

            } catch (ArubaSignServiceException e) {
                throw new StorageException(StorageException.Type.GENERIC, e);
            }
            return null;
        }
    }

    public String signDocuments(SignP7M signP7M, String url) throws StorageException{
        if (siglaStorageService.getStoreType().equals(SiglaStorageService.StoreType.CMIS)) {
            return signDocuments(new GsonBuilder().create().toJson(signP7M), url);
        } else {
            StorageObject storageObject = siglaStorageService.getObject(signP7M.getNodeRefSource());
            try {
                final byte[] bytes = arubaSignServiceClient.pkcs7SignV2(
                        signP7M.getUsername(),
                        signP7M.getPassword(),
                        signP7M.getOtp(),
                        IOUtils.toByteArray(siglaStorageService.getInputStream(signP7M.getNodeRefSource())));
                Map<String, Object> metadataProperties = new HashMap<>();
                metadataProperties.put(StoragePropertyNames.NAME.value(), signP7M.getNomeFile());
                metadataProperties.put(StoragePropertyNames.OBJECT_TYPE_ID.value(),StoragePropertyNames.CNR_ENVELOPEDDOCUMENT.value());

                return storeSimpleDocument(
                        new ByteArrayInputStream(bytes),
                        MimeTypes.P7M.mimetype(),
                        storageObject.getPath().substring(0, storageObject.getPath().lastIndexOf(SiglaStorageService.SUFFIX) + 1),
                        metadataProperties).getKey();
            } catch (ArubaSignServiceException|IOException e) {
                throw new StorageException(StorageException.Type.GENERIC, e);
            } finally {
                List<String> aspects = storageObject.<List<String>>getPropertyValue(StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value());
                aspects.add("P:cnr:signedDocument");
                updateProperties(Collections.singletonMap(
                        StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value(),
                        aspects
                ), storageObject);
            }
        }
    }

    private String signDocuments(String json, String url) throws StorageException{
        return siglaStorageService.signDocuments(json, url);
    }

    public StorageObject updateStream(String key, InputStream inputStream, String contentType) throws StorageException{
        return siglaStorageService.updateStream(key, inputStream, contentType);
    }

    public boolean hasAspect(StorageObject storageObject, String aspect) {
        return storageObject.<List<String>>getPropertyValue(StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value()).contains(aspect);
    }

    public void addAspect(StorageObject storageObject, String aspect) {
        List<String> aspects =
                Optional.ofNullable(storageObject.<List<String>>getPropertyValue(StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value()))
                    .orElse(new ArrayList<String>());
        aspects.add(aspect);
        updateProperties(Collections.singletonMap(StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value(), aspects), storageObject);
    }

    public void removeAspect(StorageObject storageObject, String aspect) {
        List<String> aspects =
                Optional.ofNullable(storageObject.<List<String>>getPropertyValue(StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value()))
                        .orElse(new ArrayList<String>());
        aspects.remove(aspect);
        updateProperties(Collections.singletonMap(StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value(), aspects), storageObject);
    }

    public void copyNode(StorageObject source, StorageObject target) {
        siglaStorageService.copyNode(source, target);
    }

    public void addConsumerToEveryone(StorageObject storageObject) {
        addAcl(storageObject, Collections.singletonMap("GROUP_EVERYONE", SiglaStorageService.ACLType.Consumer));
    }

    public void removeConsumerToEveryone(StorageObject storageObject){
        removeAcl(storageObject, Collections.singletonMap("GROUP_EVERYONE", SiglaStorageService.ACLType.Consumer));
    }
    // per gestire gruppi diversi es. CONTRATTI
    public void addConsumer(StorageObject storageObject, String group ) {
        addAcl(storageObject, Collections.singletonMap(group, SiglaStorageService.ACLType.Consumer));
    }
    public void removeConsumer(StorageObject storageObject, String group ) {
        removeAcl(storageObject, Collections.singletonMap(group, SiglaStorageService.ACLType.Consumer));
    }

    private void removeAcl(StorageObject storageObject, Map<String, SiglaStorageService.ACLType> permission) {
        managePermission(storageObject, permission, true);
    }

    private void addAcl(StorageObject storageObject, Map<String, SiglaStorageService.ACLType> permission) {
        managePermission(storageObject, permission, false);
    }

    private void managePermission(StorageObject storageObject, Map<String, SiglaStorageService.ACLType> permission, boolean remove) {
        siglaStorageService.managePermission(storageObject, permission, remove);
    }

    public void setInheritedPermission(StorageObject storageObject, Boolean inherited) {
        siglaStorageService.setInheritedPermission(storageObject, inherited);
    }

    public List<StorageObject> getRelationship(String key, String relationshipName, boolean fromTarget) {
        return siglaStorageService.getRelationship(key, relationshipName, fromTarget);
    }

    public List<StorageObject> getRelationship(String sourceNodeRef, String relationshipName) throws ApplicationException {
        return getRelationship(sourceNodeRef, relationshipName, false);
    }

    public List<StorageObject> getRelationshipFromTarget(String sourceNodeRef, String relationshipName) throws ApplicationException{
        return getRelationship(sourceNodeRef, relationshipName, true);
    }

    public void createRelationship(String source, String target, String relationshipName) {
        siglaStorageService.createRelationship(source, target, relationshipName);
    }
}
