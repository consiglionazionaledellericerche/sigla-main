package it.cnr.contab.spring.storage.config;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Created by mspasiano on 6/5/17.
 */
@Configuration
@Profile("S3")
public class S3SiglaStorageConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(S3SiglaStorageConfiguration.class);

    @Bean
    AWSCredentials basicAWSCredentials(S3SiglaStorageConfigurationProperties s3SiglaStorageConfigurationProperties) {
        return new BasicAWSCredentials(s3SiglaStorageConfigurationProperties.getAccessKey(), s3SiglaStorageConfigurationProperties.getSecretKey());
    }

    @Bean
    AmazonS3 amazonS3(AWSCredentials awsCredentials, S3SiglaStorageConfigurationProperties s3SiglaStorageConfigurationProperties) {

        ClientConfiguration clientConfig = new ClientConfiguration();
        clientConfig.setProtocol(Protocol.HTTP);

        // TODO: workaround relativo alla generazione dell'hash usando HTTP (doppia lettura dell'InputStream)
        // altri workaround possibili: settare putObjectRequest.putCustomRequestHeader(Headers.CONTENT_LENGTH, "1234");
        clientConfig.setSignerOverride("S3SignerType");

        AwsClientBuilder.EndpointConfiguration endpoint =
                new AwsClientBuilder.EndpointConfiguration(s3SiglaStorageConfigurationProperties.getAuthUrl(), "???");

        return AmazonS3ClientBuilder
                .standard()
                .withPathStyleAccessEnabled(true)
                .withClientConfiguration(clientConfig)
                .withEndpointConfiguration(endpoint)
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }

    @Bean
    Bucket Bucket(AmazonS3 amazonS3, S3SiglaStorageConfigurationProperties s3SiglaStorageConfigurationProperties) {
        boolean doesBucketExist = amazonS3.doesBucketExist(s3SiglaStorageConfigurationProperties.getBucketName());

        if(doesBucketExist) {
            return amazonS3
                    .listBuckets()
                    .stream()
                    .filter(bucket -> bucket.getName().equals(s3SiglaStorageConfigurationProperties.getBucketName()))
                    .findFirst()
                    .get();
        } else {
            return amazonS3.createBucket(s3SiglaStorageConfigurationProperties.getBucketName());
        }

    }

}
