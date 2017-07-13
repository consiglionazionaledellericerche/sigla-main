package it.cnr.contab.spring.storage.config;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageCredentials;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import it.cnr.contab.spring.storage.AzureSiglaStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.net.URISyntaxException;
import java.security.InvalidKeyException;

/**
 * Created by mspasiano on 6/5/17.
 */
@Configuration
@Profile("Azure")
public class AzureSiglaStorageConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(AzureSiglaStorageConfiguration.class);


    @Bean
    public CloudBlobContainer cloudBlobContainer(AzureSiglaStorageConfigurationProperties azureSiglaStorageConfigurationProperties) {

        String connectionString = azureSiglaStorageConfigurationProperties.getConnectionString();
        LOGGER.info("connectionString = {}", connectionString);

        String containerName = azureSiglaStorageConfigurationProperties.getContainerName();
        LOGGER.info("container = {}", containerName);

        try {
            StorageCredentials credentials = StorageCredentials
                    .tryParseCredentials(connectionString);
            CloudStorageAccount account = new CloudStorageAccount(credentials);
            CloudBlobContainer container = account.createCloudBlobClient().getContainerReference(containerName);
            container.createIfNotExists();
            return container;
        } catch (InvalidKeyException | StorageException | URISyntaxException e) {
            String msg = "cannot get reference to blob container " + containerName;
            throw new it.cnr.contab.spring.storage.StorageException(it.cnr.contab.spring.storage.StorageException.Type.GENERIC, new RuntimeException(msg, e));
        }
    }


    @Bean
    public AzureSiglaStorageService azureBlobStorageService(CloudBlobContainer cloudBlobContainer) {
        return new AzureSiglaStorageService(cloudBlobContainer);
    }



}
