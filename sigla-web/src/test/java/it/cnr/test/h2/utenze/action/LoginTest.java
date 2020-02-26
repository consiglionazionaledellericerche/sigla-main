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
    public static final String UO = "999.000";
    public static final String CDR = "999.000.000";

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

        getGrapheneElement("main.nuovaPassword").writeIntoElement(PASSWORD);
        getGrapheneElement("main.confermaPassword").writeIntoElement(PASSWORD);

        comandoAssegnapassword.click();
    }

    @Test
    @RunAsClient
    @OperateOnDeployment(TEST_H2)
    @InSequence(3)
    public void testListUO() throws Exception {
        LOGGER.info("try to connect to UO {} and CDR {}", UO, CDR);
        doLoginUO(UO, CDR);
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
