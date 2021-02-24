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

package it.cnr.test.h2.utenze.action;

import it.cnr.test.h2.DeploymentsH2;
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

import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class ActionDeployments extends DeploymentsH2 {
    private transient final static Logger LOGGER = LoggerFactory.getLogger(ActionDeployments.class);
    public static final int ITERATE_MAX = 50;
    public static final String FRAME_DESKTOP = "desktop", FRAME_WORKSPACE = "workspace", FRAME_MENU = "menu_tree";

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
        while (pageSourceNotFound && iterate < ITERATE_MAX) {
            try {
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                pageSourceNotFound = connection.getResponseCode() == HttpStatus.SC_NOT_FOUND;
                if (pageSourceNotFound)
                    TimeUnit.SECONDS.sleep(5);
                LOGGER.warn("Try to connect to url: {} iterate: {}", url.toString(), iterate);
                iterate++;
            } catch (IllegalStateException | ConnectException _ex) {
                iterate++;
            }
        }
    }

    protected void logPageSource() {
        LOGGER.info(browser.getPageSource());
    }

    protected void doLogin(String user, String password) throws Exception {
        browser.navigate().to(deploymentURL.toString().concat("/Login.do"));
        switchToFrameDesktop();
        final WebElement comandoEntra = browser.findElement(By.name("comando.doEntra"));
        Assert.assertTrue(Optional.ofNullable(comandoEntra).isPresent());

        getGrapheneElement("j_username").writeIntoElement(user);
        getGrapheneElement("j_password").writeIntoElement(password);

        comandoEntra.click();
    }

    protected void doLoginUO(String uo, String cdr) throws Exception {
        switchToFrameWorkspace();

        getGrapheneElement("main.find_uo.cd_unita_organizzativa").writeIntoElement(uo);
        doClickButton("doSearch(main.find_uo)");

        getGrapheneElement("main.find_cdr.searchtool_cd_centro_responsabilita").writeIntoElement(cdr);
        doClickButton("submitForm('doSelezionaCds')");
    }

    protected WebElement getWebElement(String element) {
        return Optional.ofNullable(browser.findElement(By.name(element)))
                .orElseThrow(() -> new RuntimeException("Cannot find WebElement with name "+element));
    }

    protected GrapheneElement getGrapheneElement(String element) {
        return Optional.ofNullable(getWebElement(element))
                        .filter(GrapheneElement.class::isInstance)
                        .map(GrapheneElement.class::cast)
                        .orElseThrow(() -> new RuntimeException("Cannot find GrapheneElement with name "+element));
    }

    protected void switchToFrameMenu() {
        switchToFrame(FRAME_MENU);
    }

    protected void switchToFrameWorkspace() {
        switchToFrame(FRAME_WORKSPACE);
    }

    protected void switchToFrameDesktop() {
        switchToFrame(FRAME_DESKTOP);
    }

    protected void  switchToFrame(String frameId) {
        browser.switchTo().frame(frameId);
    }

    protected void doSelezionaMenu(String menuId) {
        doClickTree("selezionaMenu('".concat(menuId).concat("')"));
    }

    protected void doApriMenu(String menuId) {
        doClickTree("apriMenu('".concat(menuId).concat("')"));
    }

    protected void doClickTree(String onclick) {
        browser.findElements(By.tagName("span"))
                .stream()
                .filter(webElement -> Optional.ofNullable(webElement.getAttribute("onclick")).filter(s -> s.length() > 0).isPresent())
                .filter(webElement -> webElement.getAttribute("onclick").contains(onclick))
                .findAny()
                .orElseThrow(() -> new RuntimeException("Cannot find element " + onclick)).click();
    }

    protected void doClickButton(String onclick) {
        browser.findElements(By.tagName("button"))
                .stream()
                .filter(webElement -> Optional.ofNullable(webElement.getAttribute("onclick")).filter(s -> s.length() > 0).isPresent())
                .filter(webElement -> webElement.getAttribute("onclick").contains(onclick))
                .findAny()
                .orElseThrow(() -> new RuntimeException("Cannot find element " + onclick)).click();
    }
}
