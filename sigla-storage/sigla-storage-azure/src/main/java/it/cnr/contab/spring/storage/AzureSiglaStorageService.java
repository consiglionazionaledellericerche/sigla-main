package it.cnr.contab.spring.storage;

import com.microsoft.azure.storage.blob.CloudBlob;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import it.cnr.contab.spring.storage.config.StoragePropertyNames;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * Created by francesco on 06/07/17.
 */
@Profile("AZURE")
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


        return Optional.ofNullable(metadata)
                .filter(stringObjectMap -> !stringObjectMap.isEmpty())
                .map(stringObjectMap -> {

                    try {
                        HashMap<String, String> map = cloudBlobContainer
                                .getBlockBlobReference(key)
                                .getMetadata();

                        return new StorageObject(key, key, map);
                    } catch (URISyntaxException | com.microsoft.azure.storage.StorageException e) {
                        throw new StorageException(StorageException.Type.GENERIC, e);
                    }

                }).orElse(new StorageObject(key, key, Collections.emptyMap()));
    }

    @Override
    public StorageObject createDocument(InputStream inputStream, String contentType, Map<String, Object> metadataProperties,
                                        StorageObject parentObject, String path, boolean makeVersionable, Permission... permissions) {
        String parentPath = Optional.ofNullable(parentObject)
                .map(storageObject -> storageObject.getPath())
                .map(s -> {
                    if (s.indexOf(SUFFIX) == 0)
                        return s.substring(1);
                    else
                        return s;
                })
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
    public InputStream getInputStream(String key) {

        try {
            return cloudBlobContainer
                    .getBlobReferenceFromServer( Optional.ofNullable(key)
                            .map(s -> {
                                if (s.indexOf(SUFFIX) == 0)
                                    return s.substring(1);
                                else
                                    return s;
                            }).orElseThrow(() -> new StorageException(StorageException.Type.NOT_FOUND, "Key is null")))
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
    public Boolean delete(String key) {
        key = Optional.ofNullable(key)
                .filter(s -> !s.equals(SUFFIX) && s.startsWith(SUFFIX))
                .map(s -> s.substring(1))
                .orElse(key);
        try {
            CloudBlob blobReference = cloudBlobContainer.getBlobReferenceFromServer(key);
            blobReference.delete();
        } catch (URISyntaxException | com.microsoft.azure.storage.StorageException e) {
            if (e instanceof com.microsoft.azure.storage.StorageException) {
                if (((com.microsoft.azure.storage.StorageException) e).getHttpStatusCode() == 404) {
                    LOGGER.error("item " + key + " does not exist", e);
                    return false;
                }
            }
            throw new StorageException(StorageException.Type.GENERIC, e);
        }
        return true;
    }

    @Override
    public StorageObject getObject(String key) {

            key = Optional.ofNullable(key)
                    .filter(s -> !s.equals(SUFFIX) && s.startsWith(SUFFIX))
                    .map(s -> s.substring(1))
                    .orElse(key);

            try {
                CloudBlob blobReference = cloudBlobContainer
                        .getBlobReferenceFromServer(key);
                HashMap<String, String> metadata = blobReference
                        .getMetadata();
                return new StorageObject(key, key, metadata);
            } catch (URISyntaxException | com.microsoft.azure.storage.StorageException e) {

                if (e instanceof com.microsoft.azure.storage.StorageException) {
                    if (((com.microsoft.azure.storage.StorageException) e).getHttpStatusCode() == 404) {
                        LOGGER.error("item " + key + " does not exist", e);
                        return null;
                    }
                }

                throw new StorageException(StorageException.Type.GENERIC, e);
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
        return StoreType.AZURE;
    }

    @Override
    public void init() {
        LOGGER.info("init {}...", AzureSiglaStorageService.class.getSimpleName());
    }



}
