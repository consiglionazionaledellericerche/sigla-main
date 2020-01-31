package it.cnr.test.h2;

import it.cnr.test.util.Deployments;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.junit.InSequence;
import org.junit.Test;

import java.util.Collections;

public class DeploymentsH2 extends Deployments {
    @Test
    @InSequence(-1)
    @OperateOnDeployment(TEST_H2)
    public void beforeH2() {
        controller.start("thorntail", Collections.singletonMap("port", "12347"));
        deployer.deploy(TEST_H2);
    }
}
