package it.cnr.contab.spring.configuration;

import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import org.springframework.context.annotation.Bean;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class AbstractLiquibaseConfiguration {
    @Bean
    public Liquibase liquibase() throws LiquibaseException, SQLException {
        Connection connection = it.cnr.jada.util.ejb.EJBCommonServices.getDatasource().getConnection();
        Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));

        Liquibase liquibase = new Liquibase(getDbChangelogMaster(), new GzipClassLoaderResourceAccessor(), database);

        liquibase.update(new Contexts(), new LabelExpression());
        return liquibase;
    }

    protected abstract String getDbChangelogMaster();
}