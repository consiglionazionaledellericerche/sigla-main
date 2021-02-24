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
import it.cnr.test.util.AlertMessage;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.InSequence;
import org.junit.Test;
import org.openqa.selenium.Alert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;

public class TestataProgettiRicercaBPTest extends ActionDeployments {
    public static final String USERNAME = "ENTETEST";
    public static final String PASSWORD = "PASSTEST";
    public static final String CD_AREAPROG = "AREA001";
    public static final String CD_PROGETTO = "PRG001";
    public static final String UO = "000.000";
    public static final String CDR = "000.000.000";
    public static final String CFG = "0.CFG";
    public static final String CFG_PROGETTI = "0.CFG.PROGETTI";
    public static final String CFG_PROGETTI_LIV_2 = "0.CFG.PROGETTI.LIV2";
    public static final String CFG_PROGETTI_LIV_1 = "0.CFG.PROGETTI.LIV1";
    public static final String CFG_PROGETTI_LIV_2_M = "0.CFG.PROGETTI.LIV2.M";
    public static final String CFG_PROGETTI_LIV_1_G = "0.CFG.PROGETTI.LIV1.G";
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
        switchToFrameDesktop();
        switchToFrameMenu();
        doApriMenu(CFG);
        doApriMenu(CFG_PROGETTI);
        doApriMenu(CFG_PROGETTI_LIV_1);
        doSelezionaMenu(CFG_PROGETTI_LIV_1_G);

        browser.switchTo().parentFrame();
        switchToFrameWorkspace();

        getGrapheneElement("main.cd_progetto").writeIntoElement(CD_AREAPROG);
        getGrapheneElement("main.ds_progetto").writeIntoElement("Area Progettuale Generale");

        getGrapheneElement("main.find_dipartimento.cd_dipartimento").writeIntoElement("001");
        doClickButton("doSearch(main.find_dipartimento)");

        getGrapheneElement("main.cd_programma").writeIntoElement("001");
        doClickButton("doSearch(main.find_programma)");

        doClickButton("doSalva()");
        final Alert alert = browser.switchTo().alert();
        assertEquals(AlertMessage.CREAZIONE_ESEGUITA.value(), alert.getText());
        alert.accept();

    }

    @Test
    @RunAsClient
    @OperateOnDeployment(TEST_H2)
    @InSequence(3)
    public void testCreaProgetto() throws Exception {
        browser.switchTo().parentFrame();
        switchToFrameMenu();

        doApriMenu(CFG_PROGETTI_LIV_2);
        doSelezionaMenu(CFG_PROGETTI_LIV_2_M);

        browser.switchTo().parentFrame();
        switchToFrameWorkspace();

        getGrapheneElement("main.find_nodo_padre_area.cd_progetto").writeIntoElement(CD_AREAPROG);
        doClickButton("doSearch(main.find_nodo_padre_area)");

        getGrapheneElement("main.cd_progetto").writeIntoElement(CD_PROGETTO);
        getGrapheneElement("main.ds_progetto").writeIntoElement("Progetto di Prova");

        getGrapheneElement("main.tipo.cd_tipo_progetto").writeIntoElement("001");
        doClickButton("doSearch(main.tipo)");

        assertEquals(UO, getGrapheneElement("main.cd_unita_organizzativa").getAttribute("value"));

        doClickButton("doBlankSearch(main.responsabile)");
        getGrapheneElement("main.responsabile.cd_terzo").writeIntoElement("1");
        doClickButton("doSearch(main.responsabile)");

        doClickButton("doSalva()");

        Alert alert = browser.switchTo().alert();
        assertEquals(AlertMessage.MESSAGE_INDICARE_FASE_PROGETTO.value(), alert.getText());
        alert.accept();

        getGrapheneElement("main.fl_previsione").click();

        doClickButton("doSalva()");

        alert = browser.switchTo().alert();
        assertEquals(AlertMessage.CREAZIONE_ESEGUITA.value(), alert.getText());

        alert.accept();
    }
}
