package it.cnr.contab.spring.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("liquibase-isin")
@Configuration
public class ISINLiquibaseConfiguration extends AbstractLiquibaseConfiguration {
    @Override
    protected String getDbChangelogMaster() {
        return "isin-db-changelog-master.xml";
    }
}
