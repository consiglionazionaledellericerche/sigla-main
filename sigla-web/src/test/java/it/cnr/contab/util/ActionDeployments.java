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

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.GrapheneElement;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import javax.swing.text.html.Option;
import java.net.URL;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class ActionDeployments extends Deployments {
    @Drone
    protected WebDriver browser;

    @ArquillianResource
    protected URL deploymentURL;

    protected void doLogin(String user, String password) throws Exception {
        /**
         * Workaround to wait application started.
         */
        Boolean pageSourceNotFound = true;
        Integer iterate = 0;
        while(pageSourceNotFound && iterate < 120) {
            TimeUnit.SECONDS.sleep(5);
            browser.navigate().to(deploymentURL.toString().concat("/Login.do"));
            pageSourceNotFound = browser.getPageSource().contains("404 - Not Found");
            System.out.println(deploymentURL.toString().concat("/Login.do") + " iterate" + iterate);
            iterate++;
        }
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