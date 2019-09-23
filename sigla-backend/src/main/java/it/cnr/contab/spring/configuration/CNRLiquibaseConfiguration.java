package it.cnr.contab.spring.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("liquibase-cnr")
@Configuration
public class CNRLiquibaseConfiguration extends AbstractLiquibaseConfiguration {
    @Override
    protected String getDbChangelogMaster() {
        return "cnr-db-changelog-master.xml";
    }
}
