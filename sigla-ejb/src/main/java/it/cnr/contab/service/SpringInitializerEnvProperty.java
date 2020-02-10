/*
 * Copyright (C) 2020  Consiglio Nazionale delle Ricerche
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

package it.cnr.contab.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.web.context.ConfigurableWebApplicationContext;

import java.io.IOException;

public class SpringInitializerEnvProperty implements ApplicationContextInitializer<ConfigurableWebApplicationContext> {
    private transient static final Logger LOGGER = LoggerFactory.getLogger(SpringInitializerEnvProperty.class);
    public void initialize(ConfigurableWebApplicationContext ctx) {
        try {
            /**
             * workaround for env property with wildfly 10
             */
            PropertySource ps = new ResourcePropertySource(new ClassPathResource("repository.properties"));
            ctx.getEnvironment().getPropertySources().addLast(ps);
        } catch (IOException e) {
            LOGGER.warn("Cannot find repository.properties in classpath");
        }
    }
}