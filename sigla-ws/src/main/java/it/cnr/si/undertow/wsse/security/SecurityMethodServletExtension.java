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

package it.cnr.si.undertow.wsse.security;

import java.util.Map;

import javax.servlet.ServletContext;

import io.undertow.security.api.AuthenticationMechanism;
import io.undertow.security.api.AuthenticationMechanismFactory;
import io.undertow.security.idm.IdentityManager;
import io.undertow.server.handlers.form.FormParserFactory;
import io.undertow.servlet.ServletExtension;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.LoginConfig;
import it.cnr.contab.WSAttributes;

public class SecurityMethodServletExtension implements ServletExtension {
	@Override
	public void handleDeployment(DeploymentInfo deploymentInfo,
			ServletContext servletContext) {
		deploymentInfo.addAuthenticationMechanism(WSAttributes.AUTHMETHOD, new Factory(deploymentInfo.getIdentityManager()));
        final LoginConfig loginConfig = deploymentInfo.getLoginConfig();
        loginConfig.addFirstAuthMethod("FORM");
        deploymentInfo.setLoginConfig(
                loginConfig
        );
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
