package it.cnr.sigep.datasource;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
public class Datasources {

    @Drone
    WebDriver browser;

    @Test
    public void testIt() throws Exception {
        browser.navigate().to("http://localhost:8080/rest/datasource");
        assertEquals("Found the datasource", browser.findElement(By.tagName("body")).getText());
    }
}