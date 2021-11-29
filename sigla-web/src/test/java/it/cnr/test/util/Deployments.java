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

import org.eu.ingwar.tools.arquillian.extension.suite.annotations.ArquillianSuiteDeployment;
import org.jboss.arquillian.container.test.api.ContainerController;
import org.jboss.arquillian.container.test.api.Deployer;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ArchivePath;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.FileAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.PomEquippedResolveStage;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Optional;

@ArquillianSuiteDeployment
@RunWith(Arquillian.class)
public abstract class Deployments {
    private transient final static Logger LOGGER = LoggerFactory.getLogger(Deployments.class);

    public static final String TEST_H2 = "test-h2",
            TEST_ORACLE = "test-oracle", CONTAINER_NAME="thorntail";

    @ArquillianResource
    protected ContainerController controller;
    @ArquillianResource
    protected Deployer deployer;

    @Deployment(name = TEST_H2, order = 1, managed = false)
    public static WebArchive createDeploymentH2() throws Exception {
        return createDeployment("project-h2-test.yml", TEST_H2, "it.cnr.test.h2");
    }

    @Deployment(name = TEST_ORACLE, order = 2, managed = false)
    public static WebArchive createDeploymentOracle() throws Exception {
        return createDeployment("project-oracle-test.yml", TEST_ORACLE, "it.cnr.test.oracle");
    }

    protected static WebArchive createDeployment(String yml, String name, String testPackage) throws Exception {
        LOGGER.info("Start create archive {} at {}", name, LocalDateTime.now());
        final PomEquippedResolveStage pom = Maven.configureResolver()
                .withClassPathResolution(true)
                .withMavenCentralRepo(false)
                .withRemoteRepo("central", new URL(
                        Optional.ofNullable(System.getProperty("nexus.url"))
                                .map(s -> s.concat("/content/groups/public"))
                                .orElse("https://repo1.maven.org/maven2")
                ), "default")
                .loadPomFromFile("pom.xml");

        WebArchive webArchive = ShrinkWrap.create(WebArchive.class, name.concat(".war"))
                .addPackages(true, testPackage)
                .addPackages(true, "it.cnr.contab", "it.cnr.jada", "it.cnr.si", "it.cnr.test.util")
                .addPackages(true, "it.gov")
                .addPackages(true, "it.siopeplus")
                .addPackages(true, "org.apache")
                .addPackages(true, "org.jvnet")
                .addPackages(true, "org.bouncycastle")
                .addPackages(true, "org.springframework")
                .addPackages(true, "org.reactivestreams")
                .addPackages(true, "org.openqa.selenium", "org.jboss.arquillian")
                .addPackages(true, "com.google")
                .addPackages(true, "feign")
                .addPackages(true, "net.dongliu.gson")
                .addAsResource("org/springframework/web/context/ContextLoader.properties")
                .addAsResource("org/springframework/ws/client/core/WebServiceTemplate.properties")
                .addAsResource("META-INF/spring/filesystem.properties")
                .addAsResource("META-INF/spring/storage.properties")
                .addAsResource("META-INF/spring.schemas")
                .addAsResource("META-INF/spring.handlers")
                .addAsResource(yml, "/project-defaults.yml")
                .addAsLibraries(pom
                        .resolve("org.liquibase:liquibase-core")
                        .withoutTransitivity().as(JavaArchive.class))
                .addAsLibraries(pom
                        .resolve("org.springframework:spring-context-support")
                        .withTransitivity().as(JavaArchive.class))
                .addAsLibraries(pom
                        .resolve("it.cnr.si.sigla:sigla-ws-client")
                        .withTransitivity().as(JavaArchive.class))
                .setWebXML(new File("src/main/webapp/WEB-INF/web.xml"));

        pom
                .resolve("it.cnr.si:jada")
                .withoutTransitivity().asSingle(JavaArchive.class)
                .getContent()
                .keySet()
                .stream()
                .map(ArchivePath::get)
                .filter(archivePath -> archivePath.endsWith(".xml") ||
                        archivePath.endsWith(".properties"))
                .map(s -> s.substring(1))
                .forEach(archivePath -> webArchive.addAsResource(archivePath));

        pom
                .resolve("it.cnr.si.sigla:sigla-ejb")
                .withoutTransitivity().asSingle(JavaArchive.class)
                .getContent()
                .keySet()
                .stream()
                .map(ArchivePath::get)
                .filter(archivePath -> archivePath.endsWith(".xml") ||
                        archivePath.endsWith(".properties"))
                .map(s -> s.substring(1))
                .forEach(archivePath -> webArchive.addAsResource(archivePath));

        pom
                .resolve("it.cnr.si.sigla:sigla-backend")
                .withoutTransitivity()
                .asSingle(JavaArchive.class)
                .getContent()
                .keySet()
                .stream()
                .map(ArchivePath::get)
                .filter(archivePath ->
                        archivePath.endsWith(".xml") ||
                                archivePath.endsWith(".csv") ||
                                archivePath.endsWith(".sql") ||
                                archivePath.endsWith(".fnc") ||
                                archivePath.endsWith(".prc") ||
                                archivePath.endsWith(".trg")
                )
                .map(s -> s.substring(1))
                .forEach(archivePath -> {
                    webArchive.addAsResource(archivePath);
                });

        Path resourceTestPath = Paths.get("src", "test", "resources");
        Files.walk(resourceTestPath)
                .filter(p -> !p.toString().equals(resourceTestPath.toString()))
                .filter(p -> !p.toString().contains("META-INF"))
                .forEach((p) -> {
                    webArchive.addAsResource(
                            p.toString().substring(resourceTestPath.toString().length() + 1).replace("\\", "/"));
                });

        Path resourcePath = Paths.get("src", "main", "resources");
        Files.walk(resourcePath)
                .filter(p ->
                        p.toString().endsWith(".xml") || p.toString().endsWith(".properties"))
                .forEach((p) -> {
                    webArchive.addAsResource(
                            new FileAsset(p.toFile()),
                            p.toString().substring(resourcePath.toString().length() + 1).replace("\\", "/"));
                });

        Path webappPath = Paths.get("src", "main", "webapp");
        Files.walk(webappPath)
                .filter(p ->
                        p.toString().endsWith(".xml")
                                || p.toString().endsWith(".jsp")
                                || p.toString().endsWith(".js")
                                || p.toString().endsWith(".css")
                                || p.toString().endsWith("io.undertow.servlet.ServletExtension")
                )
                .forEach((p) -> webArchive.addAsWebResource(
                        new FileAsset(p.toFile()),
                        p.toString().substring(webappPath.toString().length() + 1).replace("\\", "/")));
        LOGGER.info("End create archive {} at {}", name, LocalDateTime.now());
        return webArchive;
    }
}
