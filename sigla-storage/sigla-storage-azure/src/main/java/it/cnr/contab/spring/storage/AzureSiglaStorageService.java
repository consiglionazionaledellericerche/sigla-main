package it.cnr.contab.spring.storage;

import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import it.cnr.contab.spring.storage.config.StoragePropertyNames;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * Created by francesco on 06/07/17.
 */
public class AzureSiglaStorageService implements SiglaStorageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AzureSiglaStorageService.class);

    private CloudBlobContainer cloudBlobContainer;

    public AzureSiglaStorageService(CloudBlobContainer cloudBlobContainer) {
        this.cloudBlobContainer = cloudBlobContainer;
    }


    @Override
    public StorageObject createFolder(String path, String name, Map<String, Object> metadata) {
        final String key = Optional.ofNullable(path)
                .filter(s -> s.length() > 0)
                .filter(s -> !s.equals(SUFFIX))
                .map(s -> s.startsWith(SUFFIX) ? s.substring(1) : s)
                .map(s -> s.concat(SUFFIX).concat(name))
                .orElse(name);
        metadata.remove(StoragePropertyNames.NAME.value());
        Optional.ofNullable(metadata.get(StoragePropertyNames.OBJECT_TYPE_ID.value()))
                .filter(o -> o.equals(StoragePropertyNames.CMIS_FOLDER.value()))
                .ifPresent(o -> {
                    metadata.remove(StoragePropertyNames.OBJECT_TYPE_ID.value());
                });


        throw new RuntimeException("no create folder");

//        return Optional.ofNullable(metadata)
//                .filter(stringObjectMap -> !stringObjectMap.isEmpty())
//                .map(stringObjectMap -> {
//                    ObjectMetadata objectMetadata = new ObjectMetadata();
//                    setUserMetadata(objectMetadata, stringObjectMap);
//                    objectMetadata.setContentLength(0);
//                    InputStream emptyContent = new ByteArrayInputStream(new byte[0]);
//                    PutObjectResult putObjectResult = amazonS3.putObject(
//                            azureSiglaStorageConfigurationProperties.getBucketName(),
//                            key,
//                            emptyContent,
//                            objectMetadata);
//                    return new StorageObject(key, key, putObjectResult.getMetadata().getUserMetadata());
//                }).orElse(new StorageObject(key, key, Collections.emptyMap()));
    }

    @Override
    public StorageObject createDocument(InputStream inputStream, String contentType, Map<String, Object> metadataProperties,
                                        StorageObject parentObject, String path, boolean makeVersionable, Permission... permissions) {
        String parentPath = Optional.ofNullable(parentObject)
                .map(storageObject -> storageObject.getPath())
                .orElseGet(() -> {
                    if (path.indexOf(SUFFIX) == 0)
                        return path.substring(1);
                    else
                        return path;
                });
        String key = parentPath.concat(SUFFIX).concat((String) metadataProperties.get(StoragePropertyNames.NAME.value()));

        try {
            CloudBlockBlob blockBlobReference = cloudBlobContainer
                    .getBlockBlobReference(key);

            blockBlobReference
                    .upload(inputStream, -1);

            HashMap<String, String> metadata = new HashMap<>();


            metadataProperties.entrySet()
                    .forEach(item -> metadata.put(item.getKey().replaceAll(":", "X"), item.getValue().toString()));
            blockBlobReference.setMetadata(metadata);
            blockBlobReference.uploadMetadata();

            return new StorageObject(key, key, blockBlobReference.getMetadata());

        } catch (URISyntaxException | IOException | com.microsoft.azure.storage.StorageException e) {
            throw new StorageException(StorageException.Type.GENERIC, e);
        }


//        setUserMetadata(objectMetadata, metadataProperties);
//        try {
//            objectMetadata.setContentLength(Long.valueOf(inputStream.available()).longValue());
//            PutObjectResult putObjectResult = amazonS3.putObject(azureSiglaStorageConfigurationProperties.getBucketName(),
//                    key, inputStream, objectMetadata);
//
//            return new StorageObject(key, key, getUserMetadata(key, putObjectResult.getMetadata()));
//        } catch (IOException e) {
//            throw new StorageException(StorageException.Type.GENERIC, e);
//        }
    }

    @Override
    public void updateProperties(StorageObject storageObject, Map<String, Object> metadataProperties) {

        throw new RuntimeException("no update properties");


//        ObjectMetadata objectMetadata = new ObjectMetadata();
//        setUserMetadata(objectMetadata, metadataProperties);
//        S3Object s3Object = amazonS3.getObject(azureSiglaStorageConfigurationProperties.getBucketName(), storageObject.getKey());
//        amazonS3.putObject(azureSiglaStorageConfigurationProperties.getBucketName(), s3Object.getKey(), s3Object.getObjectContent(), objectMetadata);
    }

    @Override
    public StorageObject updateStream(String key, InputStream inputStream, String contentType) {

        throw new RuntimeException("no upload stream");


//        try {
//            final byte[] bytes = IOUtils.toByteArray(inputStream);
//            ObjectMetadata objectMetadata = amazonS3.getObject(azureSiglaStorageConfigurationProperties.getBucketName(), key).getObjectMetadata().clone();
//            objectMetadata.setContentType(contentType);
//            objectMetadata.setContentLength(Long.valueOf(bytes.length));
//            PutObjectResult putObjectResult = amazonS3.putObject(azureSiglaStorageConfigurationProperties.getBucketName(),
//                    key, new ByteArrayInputStream(bytes), objectMetadata);
//            return new StorageObject(key, key,  getUserMetadata(key, putObjectResult.getMetadata()));
//        } catch (IOException e) {
//            throw new StorageException(StorageException.Type.GENERIC, e);
//        }
    }

    @Override
    public InputStream getInputStream(String name) {

        try {
            return cloudBlobContainer
                    .getBlobReferenceFromServer(name)
                    .openInputStream();
        } catch (URISyntaxException | com.microsoft.azure.storage.StorageException e) {
            throw new StorageException(StorageException.Type.GENERIC, e);
        }

    }

    @Override
    public InputStream getInputStream(String key, String versionId) {
        return getInputStream(key);
    }

    @Override
    public InputStream getInputStream(String key, Boolean majorVersion) {
        return getInputStream(key);
    }

    @Override
    public CompletableFuture<Boolean> deleteAsync(String id) {

        throw new RuntimeException("no delete async");

//        return CompletableFuture
//                .supplyAsync(() -> {
//                    boolean exists = amazonS3.doesObjectExist(azureSiglaStorageConfigurationProperties.getBucketName(), id);
//                    if (exists) {
//                        Calendar calendar = Calendar.getInstance();
//                        calendar.setTime(new Date());
//                        calendar.add(Calendar.SECOND, azureSiglaStorageConfigurationProperties.getDeleteAfter());
//
//                        ObjectMetadata objectMetadata = new ObjectMetadata();
//                        objectMetadata.setExpirationTime(calendar.getTime());
//
//                        CopyObjectRequest copyObjectRequest = new CopyObjectRequest(azureSiglaStorageConfigurationProperties.getBucketName(),
//                                id, azureSiglaStorageConfigurationProperties.getBucketName(), id)
//                                .withNewObjectMetadata(objectMetadata);
//
//                        amazonS3.copyObject(copyObjectRequest);
//                    }  else {
//                        LOGGER.warn("item {} does not exist", id);
//                    }
//                    return exists;
//                });
    }

    @Override
    public StorageObject getObject(String key) {

        try {
            key = Optional.ofNullable(key)
                    .filter(s -> !s.equals(SUFFIX) && s.startsWith(SUFFIX))
                    .map(s -> s.substring(1))
                    .orElse(key);

            CloudBlockBlob blobReference = cloudBlobContainer
                    .getBlockBlobReference(key);


            HashMap<String, String> metadata = blobReference
                    .getMetadata();
            return new StorageObject(key, key, metadata);


//            S3Object s3Object = amazonS3.getObject(azureSiglaStorageConfigurationProperties.getBucketName(), key);
//            return new StorageObject(s3Object.getKey(), s3Object.getKey(), getUserMetadata(s3Object.getKey(), s3Object.getObjectMetadata()));
        } catch (com.microsoft.azure.storage.StorageException | URISyntaxException _ex) {
            throw new StorageException(StorageException.Type.GENERIC, _ex);
        }
    }

    @Override
    public StorageObject getObject(String id, UsernamePasswordCredentials customCredentials) {
        return getObject(id);
    }

    @Override
    public StorageObject getObjectByPath(String path) {
        return getObject(path);
    }

    @Override
    public List<StorageObject> getChildren(String key) {
        throw new RuntimeException("no get children");
//        return amazonS3
//                .listObjects(azureSiglaStorageConfigurationProperties.getBucketName(), key)
//                .getObjectSummaries()
//                .stream()
//                .filter(s3ObjectSummary -> !s3ObjectSummary.getKey().equals(key))
//                .map(s3Object -> getObject(s3Object.getKey()))
//                .collect(Collectors.toList());
    }

    @Override
    public List<StorageObject> search(String query) {
        LOGGER.warn("S3 -> Not yet implemented");
        return Collections.emptyList();
    }

    @Override
    public InputStream zipContent(List<String> keys) {
        LOGGER.warn("S3 -> Not yet implemented");
        return null;
    }

    @Override
    public String signDocuments(String json, String url) {
        LOGGER.warn("S3 -> Not yet implemented -> signDocuments");
        return null;
    }

    @Override
    public void copyNode(StorageObject source, StorageObject target) {
        String targetPath = Optional.ofNullable(target.getPath())
                .filter(s -> s.startsWith(SUFFIX))
                .map(s -> s.substring(1))
                .orElse(target.getPath())
                .concat(source.getKey().substring(source.getKey().lastIndexOf(SUFFIX)));

        throw new RuntimeException("no copy node");

//        amazonS3.copyObject(azureSiglaStorageConfigurationProperties.getBucketName(),
//                source.getKey(),
//                azureSiglaStorageConfigurationProperties.getBucketName(),
//                targetPath);
    }

    @Override
    public void managePermission(StorageObject storageObject, Map<String, ACLType> permission, boolean remove) {
        LOGGER.warn("S3 -> Not yet implemented");
    }

    @Override
    public void setInheritedPermission(StorageObject storageObject, Boolean inherited) {
        LOGGER.warn("S3 -> Not yet implemented");
    }

    @Override
    public List<StorageObject> getRelationship(String key, String relationshipName, boolean fromTarget) {
        LOGGER.warn("S3 -> Not yet implemented");
        return Collections.emptyList();
    }

    @Override
    public void createRelationship(String source, String target, String relationshipName) {
        LOGGER.warn("S3 -> Not yet implemented");
    }

    @Override
    public StoreType getStoreType() {
        return StoreType.S3;
    }

    @Override
    public void init() {
        LOGGER.info("init {}...", AzureSiglaStorageService.class.getSimpleName());
    }



}
