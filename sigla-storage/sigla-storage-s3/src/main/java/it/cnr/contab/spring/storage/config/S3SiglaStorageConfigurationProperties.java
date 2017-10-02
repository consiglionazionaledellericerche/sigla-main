package it.cnr.contab.spring.storage.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Map;

/**
 * Created by mspasiano on 6/5/17.
 */
@Configuration
@Profile("S3")
public class S3SiglaStorageConfigurationProperties {

    @Value("${cnr.aws.authUrl}")
    private String authUrl;
    @Value("${cnr.aws.accessKey}")
    private String accessKey;
    @Value("${cnr.aws.secretKey}")
    private String secretKey;
    @Value("${cnr.aws.bucketName}")
    private String bucketName;
    @Value("${cnr.aws.deleteAfter}")
    private Integer deleteAfter;

    @Value("#{${store.metadataKeys}}")
    private Map<String, String> metadataKeys;

    public String getAuthUrl() {
        return authUrl;
    }

    public void setAuthUrl(String authUrl) {
        this.authUrl = authUrl;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public Integer getDeleteAfter() {
        return deleteAfter;
    }

    public void setDeleteAfter(Integer deleteAfter) {
        this.deleteAfter = deleteAfter;
    }

    public Map<String, String> getMetadataKeys() {
        return metadataKeys;
    }

    public void setMetadataKeys(Map<String, String> metadataKeys) {
        this.metadataKeys = metadataKeys;
    }
}
