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

package it.cnr.test.util;

import org.jboss.arquillian.container.spi.Container;
import org.jboss.arquillian.container.spi.Container.State;
import org.jboss.arquillian.container.spi.ContainerRegistry;
import org.jboss.arquillian.container.spi.client.container.LifecycleException;
import org.jboss.arquillian.core.api.annotation.Observes;
import org.jboss.arquillian.core.spi.LoadableExtension;
import org.jboss.arquillian.core.spi.ServiceLoader;
import org.jboss.arquillian.test.spi.event.suite.AfterSuite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CloseCustomContainersOnAfterSuiteExtension implements LoadableExtension {
    private transient final static Logger LOGGER = LoggerFactory.getLogger(CloseCustomContainersOnAfterSuiteExtension.class);

    @Override
    public void register(ExtensionBuilder builder) {
        builder.observer(CloseCustomContainers.class);
    }

    public static class CloseCustomContainers {

        public void close(@Observes AfterSuite event, ContainerRegistry registry) {
            for (Container c : registry.getContainers()) {
                if (c.getState() == State.STARTED && "custom".equalsIgnoreCase(c.getContainerConfiguration().getMode())) {
                    try {
                        LOGGER.info("Try to stop custom container {}", c.getName());
                        c.stop();
                        LOGGER.info("Stopped container {}", c.getName());
                    } catch (Exception e) {
                        LOGGER.error("Could not stop custom container {}", c.getName());
                    }
                }
            }
        }
    }
}
