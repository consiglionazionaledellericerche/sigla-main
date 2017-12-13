package it.cnr.contab.spring.storage;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import it.cnr.contab.spring.storage.config.S3SiglaStorageConfigurationProperties;
import it.cnr.contab.spring.storage.config.StoragePropertyNames;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * Created by francesco on 06/07/17.
 */
@Service
@Profile("S3")
public class S3SiglaStorageService implements SiglaStorageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(S3SiglaStorageService.class);

    private AmazonS3 amazonS3;
    private S3SiglaStorageConfigurationProperties s3SiglaStorageConfigurationProperties;


    public S3SiglaStorageService(S3SiglaStorageConfigurationProperties s3SiglaStorageConfigurationProperties, AmazonS3 amazonS3) {
        this.s3SiglaStorageConfigurationProperties = s3SiglaStorageConfigurationProperties;
        this.amazonS3 = amazonS3;
    }

    private void setUserMetadata(ObjectMetadata objectMetadata, Map<String, Object> metadata) {

        Map<String, String> metadataKeys = s3SiglaStorageConfigurationProperties.getMetadataKeys();

        metadata
                .keySet()
                .stream()
                .forEach(key -> {
                    if (metadataKeys.containsKey(key)) {
                        if (metadataKeys.get(key) != null) {
                            if (key.equals(StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value())) {
                                objectMetadata.addUserMetadata(metadataKeys.get(key),
                                        String.join(",", (List<String>)metadata.get(key)));
                            } else {
                                objectMetadata.addUserMetadata(metadataKeys.get(key), String.valueOf(metadata.get(key)));
                            }
                        }
                    }
                });
    }

    private Map<String, ?> getUserMetadata(String key, ObjectMetadata objectMetadata) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put(StoragePropertyNames.CONTENT_STREAM_LENGTH.value(), BigInteger.valueOf(objectMetadata.getContentLength()));
        result.put(StoragePropertyNames.CONTENT_STREAM_MIME_TYPE.value(), objectMetadata.getContentType());
        result.put(StoragePropertyNames.ALFCMIS_NODEREF.value(), key);
        result.put(StoragePropertyNames.ID.value(), key);
        result.put(StoragePropertyNames.NAME.value(),
                Optional.ofNullable(key.lastIndexOf(SUFFIX))
                        .filter(index -> index > -1)
                        .map(index -> key.substring(index) +1)
                        .orElse(key)
        );
        result.put(StoragePropertyNames.BASE_TYPE_ID.value(),
                Optional.of(objectMetadata.getContentLength())
                        .filter(aLong -> aLong > 0)
                        .map(aLong -> StoragePropertyNames.CMIS_DOCUMENT.value())
                        .orElse(StoragePropertyNames.CMIS_FOLDER.value()));
        s3SiglaStorageConfigurationProperties.getMetadataKeys().entrySet().stream()
                .forEach(entry ->  {
                    final String userMetaDataOf = objectMetadata.getUserMetaDataOf(entry.getValue());
                    if (entry.getKey().equals(StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value())) {
                        result.put(entry.getKey(), Optional.ofNullable(userMetaDataOf)
                                .map(s -> Arrays.asList(s.split(","))).orElse(Collections.emptyList()));
                    } else {
                        result.put(entry.getKey(), userMetaDataOf);
                    }
                });

        return result;
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
                    ObjectMetadata objectMetadata = new ObjectMetadata();
                    setUserMetadata(objectMetadata, stringObjectMap);
                    objectMetadata.setContentLength(0);
                    InputStream emptyContent = new ByteArrayInputStream(new byte[0]);
                    PutObjectResult putObjectResult = amazonS3.putObject(
                            s3SiglaStorageConfigurationProperties.getBucketName(),
                            key,
                            emptyContent,
                            objectMetadata);
                    return new StorageObject(key, key, putObjectResult.getMetadata().getUserMetadata());
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
        ObjectMetadata objectMetadata = new ObjectMetadata();
        setUserMetadata(objectMetadata, metadataProperties);
        try {
            objectMetadata.setContentLength(Long.valueOf(inputStream.available()).longValue());
            PutObjectResult putObjectResult = amazonS3.putObject(s3SiglaStorageConfigurationProperties.getBucketName(),
                    key, inputStream, objectMetadata);

            return new StorageObject(key, key, getUserMetadata(key, putObjectResult.getMetadata()));
        } catch (IOException e) {
            throw new StorageException(StorageException.Type.GENERIC, e);
        }
    }

    @Override
    public void updateProperties(StorageObject storageObject, Map<String, Object> metadataProperties) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        setUserMetadata(objectMetadata, metadataProperties);
        S3Object s3Object = amazonS3.getObject(s3SiglaStorageConfigurationProperties.getBucketName(), storageObject.getKey());
        final PutObjectResult putObjectResult = amazonS3.putObject(s3SiglaStorageConfigurationProperties.getBucketName(), s3Object.getKey(), s3Object.getObjectContent(), objectMetadata);
        Optional.ofNullable(metadataProperties.get(StoragePropertyNames.NAME.value()))
                .map(String.class::cast)
                .filter(s -> !s.equals(s3Object.getKey().substring(s3Object.getKey().lastIndexOf(SUFFIX))))
                .ifPresent(s -> {
                    amazonS3.copyObject(s3SiglaStorageConfigurationProperties.getBucketName(),
                    s3Object.getKey(),
                    s3SiglaStorageConfigurationProperties.getBucketName(),
                    s3Object.getKey().substring(s3Object.getKey().lastIndexOf(SUFFIX) + 1).concat(s));
                    amazonS3.deleteObject(s3SiglaStorageConfigurationProperties.getBucketName(),s3Object.getKey());
                });
    }

    @Override
    public StorageObject updateStream(String key, InputStream inputStream, String contentType) {
        try {
            final byte[] bytes = IOUtils.toByteArray(inputStream);
            ObjectMetadata objectMetadata = amazonS3.getObject(s3SiglaStorageConfigurationProperties.getBucketName(), key).getObjectMetadata().clone();
            objectMetadata.setContentType(contentType);
            objectMetadata.setContentLength(Long.valueOf(bytes.length));
            PutObjectResult putObjectResult = amazonS3.putObject(s3SiglaStorageConfigurationProperties.getBucketName(),
                    key, new ByteArrayInputStream(bytes), objectMetadata);
            return new StorageObject(key, key,  getUserMetadata(key, putObjectResult.getMetadata()));
        } catch (IOException e) {
            throw new StorageException(StorageException.Type.GENERIC, e);
        }
    }

    @Override
    public InputStream getInputStream(String key) {
        return amazonS3.getObject(s3SiglaStorageConfigurationProperties.getBucketName(),
                Optional.ofNullable(key)
                    .map(s -> {
                        if (s.indexOf(SUFFIX) == 0)
                            return s.substring(1);
                        else
                            return s;
                    }).orElseThrow(() -> new StorageException(StorageException.Type.NOT_FOUND, "Key is null"))
        ).getObjectContent();
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
        boolean exists = amazonS3.doesObjectExist(s3SiglaStorageConfigurationProperties.getBucketName(), key);
        if (exists) {
            amazonS3.deleteObject(s3SiglaStorageConfigurationProperties.getBucketName(), key);
        }  else {
            LOGGER.warn("item {} does not exist", key);
        }
        return exists;
    }

    @Override
    public StorageObject getObject(String key) {
        try {
            key = Optional.ofNullable(key)
                    .filter(s -> !s.equals(SUFFIX) && s.startsWith(SUFFIX))
                    .map(s -> s.substring(1))
                    .orElse(key);
            S3Object s3Object = amazonS3.getObject(s3SiglaStorageConfigurationProperties.getBucketName(), key);
            return new StorageObject(s3Object.getKey(), s3Object.getKey(), getUserMetadata(s3Object.getKey(), s3Object.getObjectMetadata()));
        } catch (AmazonS3Exception _ex) {
            if (_ex.getStatusCode() == HttpStatus.SC_NOT_FOUND)
                return null;
            throw new StorageException(StorageException.Type.GENERIC, _ex);
        }
    }

    @Override
    public StorageObject getObject(String id, UsernamePasswordCredentials customCredentials) {
        return getObject(id);
    }

    @Override
    public StorageObject getObjectByPath(String path, boolean isFolder) {
        return getObject(path);
    }

    @Override
    public List<StorageObject> getChildren(String key, int depth) {
        return getChildren(key);
    }

    @Override
    public List<StorageObject> getChildren(String key) {
        return amazonS3
                .listObjects(s3SiglaStorageConfigurationProperties.getBucketName(), key)
                .getObjectSummaries()
                .stream()
                .filter(s3ObjectSummary -> !s3ObjectSummary.getKey().equals(key))
                .map(s3Object -> getObject(s3Object.getKey()))
                .collect(Collectors.toList());
    }

    @Override
    public List<StorageObject> search(String query) {
        LOGGER.warn("S3 -> Not yet implemented");
        return Collections.emptyList();
    }

    @Override
    public InputStream zipContent(List<String> keys, String name) {
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
        amazonS3.copyObject(s3SiglaStorageConfigurationProperties.getBucketName(),
                source.getKey(),
                s3SiglaStorageConfigurationProperties.getBucketName(),
                targetPath);
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
        LOGGER.info("init {}...", S3SiglaStorageService.class.getSimpleName());
    }



}
