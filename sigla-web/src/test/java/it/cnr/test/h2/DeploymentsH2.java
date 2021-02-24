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

package it.cnr.test.h2;

import it.cnr.test.util.Deployments;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;

import java.util.AbstractMap;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DeploymentsH2 extends Deployments {
    @Test
    @InSequence(-1)
    @OperateOnDeployment(TEST_H2)
    public void startupH2() {
        controller.start(CONTAINER_NAME,
                Stream.of(
                        new AbstractMap.SimpleEntry<>("port", "12347"),
                        new AbstractMap.SimpleEntry<>("javaVmArguments", "-agentlib:jdwp=transport=dt_socket,address=8789,server=y,suspend=n")
                ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
        );
        deployer.deploy(TEST_H2);
    }
}
