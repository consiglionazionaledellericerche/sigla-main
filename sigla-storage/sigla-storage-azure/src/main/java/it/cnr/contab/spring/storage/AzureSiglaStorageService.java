package it.cnr.contab.spring.storage;

import com.microsoft.azure.storage.blob.*;
import it.cnr.contab.spring.storage.config.AzureSiglaStorageConfigurationProperties;
import it.cnr.contab.spring.storage.config.StoragePropertyNames;
import it.cnr.contab.util.MetadataEncodingUtils;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by francesco on 06/07/17.
 */
@Profile("AZURE")
public class AzureSiglaStorageService implements SiglaStorageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AzureSiglaStorageService.class);

    private CloudBlobContainer cloudBlobContainer;
    private AzureSiglaStorageConfigurationProperties azureSiglaStorageConfigurationProperties;

    public AzureSiglaStorageService(CloudBlobContainer cloudBlobContainer, AzureSiglaStorageConfigurationProperties azureSiglaStorageConfigurationProperties) {
        this.cloudBlobContainer = cloudBlobContainer;
        this.azureSiglaStorageConfigurationProperties = azureSiglaStorageConfigurationProperties;
    }

    private HashMap<String, String> putUserMetadata(Map<String, Object> metadata) {
        return metadata
                .entrySet()
                .stream()
                .collect(HashMap::new, (m,entry)-> {
                    Optional.ofNullable(entry.getValue())
                            .ifPresent(entryValue -> {
                                String b64EncodedKey = MetadataEncodingUtils.encodeKey(entry.getKey());
                                if (entry.getKey().equals(StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value())) {
                                    String base64EncodedValues = MetadataEncodingUtils
                                            .encodeValues((List<String>) entryValue);
                                    m.put(b64EncodedKey, base64EncodedValues);
                                } else {
                                    String base64EncodedValue = String.valueOf(entry.getValue());
                                    m.put(b64EncodedKey, MetadataEncodingUtils.encodeValue(base64EncodedValue));
                                }
                            });
                }, HashMap::putAll);
    }

    private Map<String, Object> getUserMetadata(CloudBlob blockBlobReference) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put(StoragePropertyNames.CONTENT_STREAM_LENGTH.value(), BigInteger.valueOf(blockBlobReference.getProperties().getLength()));
        result.put(StoragePropertyNames.CONTENT_STREAM_MIME_TYPE.value(), blockBlobReference.getProperties().getContentType());
        result.put(StoragePropertyNames.ALFCMIS_NODEREF.value(), blockBlobReference.getName());
        result.put(StoragePropertyNames.ID.value(), blockBlobReference.getName());
        result.put(StoragePropertyNames.NAME.value(),
                Optional.ofNullable(blockBlobReference.getName().lastIndexOf(SUFFIX))
                        .filter(index -> index > -1)
                        .map(index -> blockBlobReference.getName().substring(index +1))
                        .orElse(blockBlobReference.getName())
        );
        result.put(StoragePropertyNames.BASE_TYPE_ID.value(),
                Optional.of(blockBlobReference.getProperties().getLength())
                        .filter(aLong -> aLong > 0)
                        .map(aLong -> StoragePropertyNames.CMIS_DOCUMENT.value())
                        .orElse(StoragePropertyNames.CMIS_FOLDER.value()));

        blockBlobReference.getMetadata().forEach((b64EncodedKey, b64EncodedValue) -> {
            String key = MetadataEncodingUtils.decodeKey(b64EncodedKey);
            if (key.equals(StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value())) {
                result.put(key, MetadataEncodingUtils.decodeValues(b64EncodedValue));
            } else {
                result.put(key, MetadataEncodingUtils.decodeValue(b64EncodedValue));
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
                    try {
                        final CloudAppendBlob blockBlobReference = cloudBlobContainer
                                .getAppendBlobReference(key);
                        //blockBlobReference.setMetadata(putUserMetadata(metadata));
                        //blockBlobReference.createOrReplace(); //TODO non crea un folder ma un file vuoto
                        //blockBlobReference.uploadMetadata();
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
            blockBlobReference.getProperties().setContentType(contentType);
            blockBlobReference.uploadProperties();
            blockBlobReference.setMetadata(putUserMetadata(metadataProperties));
            blockBlobReference.uploadMetadata();

            return new StorageObject(key, key, getUserMetadata(blockBlobReference));

        } catch (URISyntaxException | IOException | com.microsoft.azure.storage.StorageException e) {
            throw new StorageException(StorageException.Type.GENERIC, e);
        }
    }

    @Override
    public void updateProperties(StorageObject storageObject, Map<String, Object> metadataProperties) {
        try {
            CloudBlockBlob blockBlobReference = cloudBlobContainer
                    .getBlockBlobReference(storageObject.getKey());
            if (blockBlobReference.exists()) {
                blockBlobReference.setMetadata(putUserMetadata(metadataProperties));
                blockBlobReference.uploadMetadata();
                Optional.ofNullable(metadataProperties.get(StoragePropertyNames.NAME.value()))
                        .map(String.class::cast)
                        .filter(s -> !s.equals(blockBlobReference.getName()))
                        .ifPresent(s -> {
                            /*
                            amazonS3.copyObject(s3SiglaStorageConfigurationProperties.getBucketName(),
                                    s3Object.getKey(),
                                    s3SiglaStorageConfigurationProperties.getBucketName(),
                                    s3Object.getKey().substring(s3Object.getKey().lastIndexOf(SUFFIX) + 1).concat(s));
                            amazonS3.deleteObject(s3SiglaStorageConfigurationProperties.getBucketName(),s3Object.getKey());
                            */
                        });
            }
        } catch (URISyntaxException | com.microsoft.azure.storage.StorageException e) {
            throw new StorageException(StorageException.Type.GENERIC, e);
        }
    }

    @Override
    public StorageObject updateStream(String key, InputStream inputStream, String contentType) {
        try {
            CloudBlockBlob blockBlobReference = cloudBlobContainer
                    .getBlockBlobReference(key);
            blockBlobReference
                    .upload(inputStream, -1);

            return new StorageObject(key, key, getUserMetadata(blockBlobReference));

        } catch (URISyntaxException | IOException |com.microsoft.azure.storage.StorageException e) {
            throw new StorageException(StorageException.Type.GENERIC, e);
        }
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
                    LOGGER.debug("item " + key + " does not exist", e);
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
                final Optional<StorageObject> storageObjectDirectory = cloudBlobContainer.listBlobsSegmented(key).getResults().stream()
                        .filter(CloudBlobDirectory.class::isInstance)
                        .findFirst()
                        .map(CloudBlobDirectory.class::cast)
                        .map(cloudBlobDirectory -> {
                            String name =  cloudBlobDirectory.getPrefix();
                            name = name.substring(0, name.length() -1);
                            return new StorageObject(
                                    name,
                                    name,
                                    Collections.emptyMap());
                        });
                if (storageObjectDirectory.isPresent())
                    return storageObjectDirectory.get();

                CloudBlob blobReference = cloudBlobContainer
                        .getBlobReferenceFromServer(key);
                return new StorageObject(key, key, getUserMetadata(blobReference));
            } catch (URISyntaxException | com.microsoft.azure.storage.StorageException e) {
                if (e instanceof com.microsoft.azure.storage.StorageException) {
                    if (((com.microsoft.azure.storage.StorageException) e).getHttpStatusCode() == 404) {
                        LOGGER.debug("item " + key + " does not exist", e);
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
    public StorageObject getObjectByPath(String path, boolean isFolder) {
        return Optional.ofNullable(getObject(path))
                .orElseGet(() -> {
                    if (isFolder) {
                        String key = Optional.ofNullable(path)
                                .filter(s -> !s.equals(SUFFIX) && s.startsWith(SUFFIX))
                                .map(s -> s.substring(1))
                                .orElse(path);
                        return new StorageObject(key, key, Collections.emptyMap());
                    } else {
                        return null;
                    }
                });
    }

    @Override
    public List<StorageObject> getChildren(String key) {
        try {
            key = Optional.ofNullable(key)
                    .filter(s -> !s.equals(SUFFIX) && s.startsWith(SUFFIX))
                    .map(s -> s.substring(1))
                    .orElse(key).concat(SUFFIX);
            return cloudBlobContainer.listBlobsSegmented(key).getResults()
                    .stream()
                    .filter(CloudBlockBlob.class::isInstance)
                    .map(CloudBlockBlob.class::cast)
                    .map(cloudBlockBlob -> {
                        try {
                            return new StorageObject(
                                    cloudBlockBlob.getName(),
                                    cloudBlockBlob.getName(),
                                    getUserMetadata(cloudBlobContainer.getBlobReferenceFromServer(cloudBlockBlob.getName())));
                        } catch (URISyntaxException|com.microsoft.azure.storage.StorageException e) {
                            throw new StorageException(StorageException.Type.GENERIC, e);
                        }
                    })
                    .collect(Collectors.toList());

        } catch (com.microsoft.azure.storage.StorageException e) {
           throw new StorageException(StorageException.Type.GENERIC, e);
        }
    }

    @Override
    public List<StorageObject> search(String query) {
        LOGGER.warn("AZURE -> Not yet implemented");
        return Collections.emptyList();
    }

    @Override
    public InputStream zipContent(List<String> keys, String name) {
        LOGGER.warn("AZURE -> Not yet implemented");
        return null;
    }

    @Override
    public String signDocuments(String json, String url) {
        LOGGER.warn("AZURE -> Not yet implemented -> signDocuments");
        return null;
    }

    @Override
    public void copyNode(StorageObject source, StorageObject target) {
        LOGGER.warn("AZURE -> Not yet implemented -> copyNode");
    }

    @Override
    public void managePermission(StorageObject storageObject, Map<String, ACLType> permission, boolean remove) {
        LOGGER.warn("AZURE -> Not yet implemented");
    }

    @Override
    public void setInheritedPermission(StorageObject storageObject, Boolean inherited) {
        LOGGER.warn("AZURE -> Not yet implemented");
    }

    @Override
    public List<StorageObject> getRelationship(String key, String relationshipName, boolean fromTarget) {
        LOGGER.warn("AZURE -> Not yet implemented");
        return Collections.emptyList();
    }

    @Override
    public void createRelationship(String source, String target, String relationshipName) {
        LOGGER.warn("AZURE -> Not yet implemented");
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
