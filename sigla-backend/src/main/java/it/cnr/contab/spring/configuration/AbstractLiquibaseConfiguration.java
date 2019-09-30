/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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