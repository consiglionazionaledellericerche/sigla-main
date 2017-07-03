package it.cnr.contab.spring.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CopyObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import it.cnr.contab.spring.config.S3ConfigurationProperties;
import it.cnr.contab.spring.config.StorageObject;
import it.cnr.contab.spring.storage.StorageService;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * Created by mspasiano on 6/5/17.
 */
public class S3Service implements StorageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(S3Service.class);
    private static final String SUFFIX = "/";
    private S3ConfigurationProperties s3ConfigurationProperties;

    private AmazonS3 amazonS3;


    public S3Service(S3ConfigurationProperties s3ConfigurationProperties, AmazonS3 amazonS3) {
        this.s3ConfigurationProperties = s3ConfigurationProperties;
        this.amazonS3 = amazonS3;
    }

    @Override
    public CompletableFuture<Void> createAsync(InputStream inputStream, String name, Map<String, String> metadata) {
        return CompletableFuture.runAsync(() -> {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            metadata
                    .keySet()
                    .stream()
                    .forEach(x -> objectMetadata.addUserMetadata(x, metadata.get(x)));

            amazonS3.putObject(s3ConfigurationProperties.getBucketName(), name, inputStream, objectMetadata);
        });

    }

    @Override
    public StorageObject createFolder(String path, String name, Map<String, Object> metadata) {
        return null;
    }

    @Override
    public StorageObject createDocument(InputStream inputStream, String contentType, Map<String, Object> metadataProperties, StorageObject parentObject, boolean makeVersionable, Permission... permissions) {
        return null;
    }

    @Override
    public void updateProperties(StorageObject storageObject, Map<String, Object> metadataProperties) {

    }

    @Override
    public StorageObject updateStream(String key, InputStream inputStream, String contentType) {
        return null;
    }

    public CompletableFuture<Void> createFolderAsync(String path, String name, Map<String, Object> metadata) {

        return CompletableFuture.runAsync(() -> {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            metadata
                    .keySet()
                    .stream()
                    .forEach(x -> objectMetadata.addUserMetadata(x, String.valueOf(metadata.get(x))));
            objectMetadata.setContentLength(0);
            InputStream emptyContent = new ByteArrayInputStream(new byte[0]);
            amazonS3.putObject(
                    s3ConfigurationProperties.getBucketName(),
                    name.concat(SUFFIX),
                    emptyContent,
                    objectMetadata);
        });

    }

    public CompletableFuture<Void> createAsync(InputStream inputStream, String contentType, String name) {

        return CompletableFuture.runAsync(() -> {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(contentType);
            amazonS3.putObject(s3ConfigurationProperties.getBucketName(), name, inputStream, objectMetadata);
        });

    }

    public S3Object getS3Object(String name) {
        return amazonS3.getObject(s3ConfigurationProperties.getBucketName(), name);
    }

    @Override
    public InputStream getInputStream(String name) {
        LOGGER.info("getting input stream of {}", name);
        return amazonS3.getObject(s3ConfigurationProperties.getBucketName(), name).getObjectContent();
    }

    @Override
    public InputStream getInputStream(String key, String versionId) {
        return null;
    }

    @Override
    public InputStream getInputStream(String key, Boolean majorVersion) {
        return null;
    }

    @Override
    public CompletableFuture<Boolean> deleteAsync(String name) {
        return CompletableFuture
                .supplyAsync(() -> {

                    boolean exists = amazonS3.doesObjectExist(s3ConfigurationProperties.getBucketName(), name);
                    if (exists) {
                        LOGGER.info("item {} will be deleted", name);
                        amazonS3.deleteObject(s3ConfigurationProperties.getBucketName(), name);
                    } else {
                        LOGGER.warn("item {} does not exist", name);
                    }

                    return exists;

                });
    }

    //    FIXME
    @Override
    public CompletableFuture<Boolean> scheduledDeleteAsync(String name) {
        return CompletableFuture
                .supplyAsync(() -> {
                    boolean exists = amazonS3.doesObjectExist(s3ConfigurationProperties.getBucketName(), name);
                    if (exists) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(new Date());
                        calendar.add(Calendar.SECOND, s3ConfigurationProperties.getDeleteAfter());

                        ObjectMetadata objectMetadata = new ObjectMetadata();
                        objectMetadata.setExpirationTime(calendar.getTime());

                        CopyObjectRequest copyObjectRequest = new CopyObjectRequest(s3ConfigurationProperties.getBucketName(),
                                name, s3ConfigurationProperties.getBucketName(), name)
                                .withNewObjectMetadata(objectMetadata);

                        amazonS3.copyObject(copyObjectRequest);
                    }  else {
                        LOGGER.warn("item {} does not exist", name);
                    }
                    return exists;
                });
    }

    @Override
    public StorageObject getObject(String id) {
        return null;
    }

    @Override
    public StorageObject getObject(String id, UsernamePasswordCredentials customCredentials) {
        return null;
    }

    @Override
    public StorageObject getObjectByPath(String path) {
        return null;
    }

    @Override
    public List<StorageObject> getChildren(String key) {
        return null;
    }

    @Override
    public List<StorageObject> search(String query) {
        return null;
    }

    @Override
    public InputStream zipContent(List<String> keys) {
        return null;
    }

    @Override
    public String signDocuments(String json, String url) {
        return null;
    }

    @Override
    public void copyNode(StorageObject source, StorageObject target) {

    }

    @Override
    public void managePermission(StorageObject storageObject, Map<String, ACLType> permission, boolean remove) {

    }

    @Override
    public void setInheritedPermission(StorageObject storageObject, Boolean inherited) {

    }

    @Override
    public List<StorageObject> getRelationship(String key, String relationshipName, boolean fromTarget) {
        return null;
    }

    @Override
    public void createRelationship(String source, String target, String relationshipName) {

    }

    @Override
    public void init() {

    }

    public List<String> list() {
        return amazonS3
                .listObjects(s3ConfigurationProperties.getBucketName())
                .getObjectSummaries()
                .stream()
                .map(S3ObjectSummary::getKey)
                .collect(Collectors.toList());
    }

}
