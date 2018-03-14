package it.cnr.contab.spring.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("liquibase-sigep")
@Configuration
public class SIGEPLiquibaseConfiguration extends AbstractLiquibaseConfiguration{
    @Override
    protected String getDbChangelogMaster() {
        return "sigep/db-changelog-master.xml";
    }
}