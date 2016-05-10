package it.cnr.si.undertow.wsse.security;

import java.util.Map;

import javax.servlet.ServletContext;

import io.undertow.security.api.AuthenticationMechanism;
import io.undertow.security.api.AuthenticationMechanismFactory;
import io.undertow.security.idm.IdentityManager;
import io.undertow.server.handlers.form.FormParserFactory;
import io.undertow.servlet.ServletExtension;
import io.undertow.servlet.api.DeploymentInfo;

public class SecurityMethodServletExtension implements ServletExtension {
	@Override
	public void handleDeployment(DeploymentInfo deploymentInfo,
			ServletContext servletContext) {
		deploymentInfo.addAuthenticationMechanism("WSSE", new Factory(deploymentInfo.getIdentityManager()));
	}
	public static class Factory implements AuthenticationMechanismFactory {

        private final IdentityManager identityManager;

        public Factory(IdentityManager identityManager) {
            this.identityManager = identityManager;
        }
    	@Override
        public AuthenticationMechanism create(String mechanismName, FormParserFactory formParserFactory, Map<String, String> properties) {       	
            return new WSSEAuthenticationMechanism(mechanismName, identityManager);
        }
    }
}
