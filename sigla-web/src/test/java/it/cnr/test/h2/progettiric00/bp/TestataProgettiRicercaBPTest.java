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

package it.cnr.test.h2.progettiric00.bp;

import it.cnr.test.h2.utenze.action.ActionDeployments;
import it.cnr.test.h2.utenze.action.LoginTest;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.graphene.GrapheneElement;
import org.jboss.arquillian.junit.InSequence;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class TestataProgettiRicercaBPTest extends ActionDeployments {
    public static final String USERNAME = "ENTETEST";
    public static final String PASSWORD = "PASSTEST";
    public static final String CD_AREAPROG = "AREA001";
    public static final String CD_PROGETTO = "PRG001";

    public static final String UO = "000.000";
    public static final String CDR = "000.000.000";
    private transient final static Logger LOGGER = LoggerFactory.getLogger(LoginTest.class);

    @Test
    @RunAsClient
    @OperateOnDeployment(TEST_H2)
    @InSequence(1)
    public void testLogin() throws Exception {
        doLogin(USERNAME, PASSWORD);
        doLoginUO(UO, CDR);
    }

    @Test
    @RunAsClient
    @OperateOnDeployment(TEST_H2)
    @InSequence(2)
    public void testCreaAreaProgettuale() throws Exception {
        browser.switchTo().frame("desktop");
        browser.switchTo().frame("menu_tree");

        doClickTree("apriMenu('0.CFG')");
        doClickTree("apriMenu('0.CFG.PROGETTI')");
        doClickTree("apriMenu('0.CFG.PROGETTI.LIV1')");
        doClickTree("selezionaMenu('0.CFG.PROGETTI.LIV1.G')");

        browser.switchTo().parentFrame();
        browser.switchTo().frame("workspace");

        getGrapheneElement("main.cd_progetto").writeIntoElement(CD_AREAPROG);
        getGrapheneElement("main.ds_progetto").writeIntoElement("Area Progettuale Generale");

        getGrapheneElement("main.find_dipartimento.cd_dipartimento").writeIntoElement("001");
        doClickButton("doSearch(main.find_dipartimento)");

        getGrapheneElement("main.cd_programma").writeIntoElement("001");
        doClickButton("doSearch(main.find_programma)");

        doClickButton("doSalva()");
    }

    @RunAsClient
    @OperateOnDeployment(TEST_H2)
    @InSequence(3)
    public void testCreaProgetto() throws Exception {
        browser.switchTo().frame("menu_tree");

        doClickTree("apriMenu('0.CFG')");
        doClickTree("apriMenu('0.CFG.PROGETTI')");
        doClickTree("apriMenu('0.CFG.PROGETTI.LIV2')");
        doClickTree("selezionaMenu('0.CFG.PROGETTI.LIV2.M')");

        browser.switchTo().parentFrame();
        browser.switchTo().frame("workspace");

        getGrapheneElement("main.find_nodo_padre_area.cd_progetto").writeIntoElement(CD_AREAPROG);
        doClickButton("doSearch(main.find_nodo_padre_area)");

        getGrapheneElement("main.cd_progetto").writeIntoElement(CD_PROGETTO);
        getGrapheneElement("main.ds_progetto").writeIntoElement("Progetto di Prova");

        getGrapheneElement("main.tipo.cd_tipo_progetto").writeIntoElement("001");
        doClickButton("doSearch(main.tipo)");

        assertEquals(UO, getWebElement("main.cd_unita_organizzativa").getText());

        getGrapheneElement("main.responsabile.cd_terzo").writeIntoElement("1");
        doClickButton("doSearch(main.responsabile)");

        doClickButton("doSalva()");
    }
}
