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

package it.cnr.contab.util;

import org.apache.http.HttpStatus;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.GrapheneElement;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.Assert;
import org.junit.Before;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class ActionDeployments extends Deployments {
    private transient final static Logger LOGGER = LoggerFactory.getLogger(ActionDeployments.class);

    @Drone
    protected WebDriver browser;

    @ArquillianResource
    protected URL deploymentURL;

    @Before
    @RunAsClient
    @OperateOnDeployment(TEST_H2)
    public void waitUntilApplicationStarted() throws Exception {
        /**
         * Workaround to wait application started.
         */
        Boolean pageSourceNotFound = true;
        Integer iterate = 0;
        URL url = new URL(deploymentURL.toString().concat("/Login.do"));
        while (pageSourceNotFound && iterate < 12) {
            try {
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                pageSourceNotFound = connection.getResponseCode() == HttpStatus.SC_NOT_FOUND;
                if (pageSourceNotFound)
                    TimeUnit.SECONDS.sleep(5);
                LOGGER.info("Try to connect to url {} iterate: {}", url.toString(), iterate);
                iterate++;
            } catch (IllegalStateException _ex) {
                iterate++;
            }
        }
    }

    protected void doLogin(String user, String password) throws Exception {
        browser.navigate().to(deploymentURL.toString().concat("/Login.do"));
        browser.switchTo().frame("desktop");
        final WebElement comandoEntra = browser.findElement(By.name("comando.doEntra"));
        Assert.assertTrue(Optional.ofNullable(comandoEntra).isPresent());

        final GrapheneElement elementUserId =
                Optional.ofNullable(browser.findElement(By.name("main.userid")))
                        .filter(GrapheneElement.class::isInstance)
                        .map(GrapheneElement.class::cast)
                        .orElseThrow(() -> new RuntimeException("Cannot find element with name main.userid"));
        final GrapheneElement elementPassword = Optional.ofNullable(browser.findElement(By.name("main.password")))
                .filter(GrapheneElement.class::isInstance)
                .map(GrapheneElement.class::cast)
                .orElseThrow(() -> new RuntimeException("Cannot find element with name main.password"));
        elementUserId.writeIntoElement(user);
        elementPassword.writeIntoElement(password);
        comandoEntra.click();
    }

    protected void doClickTree(String onclick) {
        browser.findElements(By.tagName("span"))
                .stream()
                .filter(webElement -> Optional.ofNullable(webElement.getAttribute("onclick")).filter(s -> s.length() > 0).isPresent())
                .filter(webElement -> webElement.getAttribute("onclick").contains(onclick))
                .findAny()
                .orElseThrow(() -> new RuntimeException("Cannot find element " + onclick)).click();
    }
}