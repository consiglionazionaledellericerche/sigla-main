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

import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.graphene.GrapheneElement;
import org.jboss.arquillian.junit.InSequence;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class LoginTest extends ActionDeployments {
    public static final String USERNAME = "TEST";
    public static final String PASSWORD = "TESTTEST";
    private transient final static Logger LOGGER = LoggerFactory.getLogger(LoginTest.class);

    @Test
    @RunAsClient
    @OperateOnDeployment(TEST_H2)
    @InSequence(1)
    public void testLogin() throws Exception {
        LOGGER.info("try to connect with user {}", USERNAME);
        doLogin(USERNAME, "");
    }

    @Test
    @RunAsClient
    @OperateOnDeployment(TEST_H2)
    @InSequence(2)
    public void testAssegnaPassword() throws Exception {
        final WebElement comandoAssegnapassword = browser.findElement(By.name("comando.doAssegnaPassword"));
        Assert.assertTrue(Optional.ofNullable(comandoAssegnapassword).isPresent());

        final GrapheneElement elementNuovaPassword =
                Optional.ofNullable(browser.findElement(By.name("main.nuovaPassword")))
                        .filter(GrapheneElement.class::isInstance)
                        .map(GrapheneElement.class::cast)
                        .orElseThrow(() -> new RuntimeException("Cannot find element with name main.nuovaPassword"));

        final GrapheneElement elementConfermaPassword = Optional.ofNullable(browser.findElement(By.name("main.confermaPassword")))
                .filter(GrapheneElement.class::isInstance)
                .map(GrapheneElement.class::cast)
                .orElseThrow(() -> new RuntimeException("Cannot find element with name main.confermaPassword"));

        elementNuovaPassword.writeIntoElement(PASSWORD);
        elementConfermaPassword.writeIntoElement(PASSWORD);

        comandoAssegnapassword.click();
    }

    @Test
    @RunAsClient
    @OperateOnDeployment(TEST_H2)
    @InSequence(3)
    public void testListUO() throws Exception {
        browser.switchTo().frame("workspace");
        final WebElement searchUO = browser.findElements(By.tagName("button"))
                .stream()
                .filter(webElement -> webElement.getAttribute("onclick").contains("doSearch(main.find_uo)"))
                .findAny()
                .orElseThrow(() -> new RuntimeException("Cannot find element doSearch(main.find_uo)"));
        final GrapheneElement inputUO = Optional.ofNullable(browser.findElement(By.name("main.find_uo.cd_unita_organizzativa")))
                .filter(GrapheneElement.class::isInstance)
                .map(GrapheneElement.class::cast)
                .orElseThrow(() -> new RuntimeException("main.find_uo.cd_unita_organizzativa"));

        inputUO.writeIntoElement("999.000");
        searchUO.click();
        final WebElement selezionaCDS = browser.findElements(By.tagName("button"))
                .stream()
                .filter(webElement -> webElement.getAttribute("onclick").contains("submitForm('doSelezionaCds')"))
                .findAny()
                .orElseThrow(() -> new RuntimeException("Cannot find element doSelezionaCds"));
        final GrapheneElement inputCDR = Optional.ofNullable(browser.findElement(By.name("main.find_cdr.searchtool_cd_centro_responsabilita")))
                .filter(GrapheneElement.class::isInstance)
                .map(GrapheneElement.class::cast)
                .orElseThrow(() -> new RuntimeException("main.find_cdr.searchtool_cd_centro_responsabilita"));
        inputCDR.writeIntoElement("999.000.000");
        selezionaCDS.click();
    }

    @Test
    @RunAsClient
    @OperateOnDeployment(TEST_H2)
    @InSequence(4)
    public void testTree() throws Exception {
        browser.switchTo().frame("desktop");
        browser.switchTo().frame("menu_tree");
        doClickTree("apriMenu('0.CFG')");
        doClickTree("apriMenu('0.CFG.STRORG')");
        doClickTree("apriMenu('0.CFG.STRORG.UNIORG')");
        doClickTree("selezionaMenu('0.CFG.STRORG.UNIORG.M')");
        browser.switchTo().parentFrame();
        browser.switchTo().frame("workspace");
        final Optional<WebElement> title = Optional.ofNullable(browser.findElement(By.tagName("title")));
        assertEquals(true, title.isPresent());
        assertEquals("Gestione Unità Organizzativa", title.get().getText());
    }
}
