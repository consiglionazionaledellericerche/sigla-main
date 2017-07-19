package it.cnr.contab.spring.storage;

import com.microsoft.azure.storage.blob.*;
import it.cnr.contab.spring.storage.config.AzureSiglaStorageConfigurationProperties;
import it.cnr.contab.spring.storage.config.StoragePropertyNames;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.util.Assert;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
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

                                String b64EncodedKey = encodeKey(entry.getKey());

                                if (entry.getKey().equals(StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value())) {

                                    String base64EncodedValues = ((List<String>) entryValue)
                                            .stream()
                                            .map(AzureSiglaStorageService::encodeValue)
                                            .collect(Collectors.joining(","));

                                    LOGGER.info("{} encoded to {}", entryValue, base64EncodedValues);

                                    m.put(b64EncodedKey, base64EncodedValues);
                                } else {
                                    String base64EncodedValue = String.valueOf(entry.getValue());
                                    m.put(b64EncodedKey, encodeValue(base64EncodedValue));
                                }
                            });
                }, HashMap::putAll);

    }

    private static String encodeKey(String input) {
        String suffix = Base64
                .getEncoder()
                .encodeToString(input.getBytes())
                .replaceAll("=+$", "");
        String b64encodedKey = String.format("CNR_%s", suffix);
        LOGGER.info("key {} encoded to {}", input, b64encodedKey);
        return b64encodedKey;

    }

    private static String encodeValue(String value) {
        byte[] bytes = value.getBytes();
        String md5digest = DigestUtils
                .md5Hex(bytes)
                .toUpperCase();
        String b64encoded = Base64
                .getEncoder()
                .encodeToString(bytes)
                .replaceAll("=+$", "");

        LOGGER.info("value {} having md5 {} has been encoded to {}", value, md5digest, b64encoded);
        String output = String.format("%s|%s", md5digest, b64encoded);
        LOGGER.info("value {} encoded to {}", value, output);
        return output;
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

        azureSiglaStorageConfigurationProperties.getMetadataKeys().entrySet().stream()
                .forEach(entry ->  {

                    String key = entry.getKey();
                    String b64EncodedKey = encodeKey(key);

                    String value = blockBlobReference.getMetadata().get(b64EncodedKey);
                    Optional.ofNullable(value)
                            .ifPresent(userMetaData -> {
                                if (key.equals(StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value())) {
                                    result.put(key, decodeValues(userMetaData));
                                } else {
                                    result.put(key, decodeValue(userMetaData));
                                }
                            });
                });
        return result;
    }

    private static String decodeValue(String input) {
        String [] a = input.split("\\|");
        Assert.isTrue(2 == a.length, "invalid metadata: " + input);
        byte[] decoded = Base64.getDecoder().decode(a[1]);
        try {
            String decodedValue = new String(decoded, "UTF-8");
            LOGGER.info("{} decoded to {}", input, decodedValue);
            Assert.isTrue(a[0].equals(DigestUtils.md5Hex(decodedValue).toUpperCase()), "integrity issue with input "+ input);
            return decodedValue;
        } catch (UnsupportedEncodingException e) {
            throw new StorageException(StorageException.Type.GENERIC, "cannot decode " + input, e);
        }
    }

    private static List<String> decodeValues(String input) {

        return Optional.ofNullable(input)
                .map(s -> Arrays
                        .stream(s.split(","))
                        .map(AzureSiglaStorageService::decodeValue)
                        .collect(Collectors.toList())
                )
                .orElse(Collections.emptyList());
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
                    String key = Optional.ofNullable(path)
                            .filter(s -> !s.equals(SUFFIX) && s.startsWith(SUFFIX))
                            .map(s -> s.substring(1))
                            .orElse(path);
                    return new StorageObject(key, key, Collections.emptyMap());
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
    public InputStream zipContent(List<String> keys) {
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
