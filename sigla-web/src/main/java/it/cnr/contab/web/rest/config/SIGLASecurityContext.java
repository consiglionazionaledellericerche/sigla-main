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

package it.cnr.contab.web.rest.config;

import it.cnr.contab.utenze00.bp.CNRUserContext;

import java.security.Principal;
import java.util.Calendar;
import java.util.Optional;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.SecurityContext;

public class SIGLASecurityContext implements SecurityContext {

	public static final String X_SIGLA_CD_CDR = "X-sigla-cd-cdr";
	public static final String X_SIGLA_CD_CDS = "X-sigla-cd-cds";
	public static final String X_SIGLA_CD_UNITA_ORGANIZZATIVA = "X-sigla-cd-unita-organizzativa";
	public static final String X_SIGLA_ESERCIZIO = "X-sigla-esercizio";
	private final CNRUserContext userContext;

	public SIGLASecurityContext(ContainerRequestContext requestContext, String userName)
	{
		this.userContext = new CNRUserContext(
				userName,
				null,
				Optional.ofNullable(requestContext.getHeaderString(X_SIGLA_ESERCIZIO))
					.map(s -> Integer.valueOf(s))
					.orElse(new Integer(Calendar.getInstance().get(Calendar.YEAR))),
				requestContext.getHeaderString(X_SIGLA_CD_UNITA_ORGANIZZATIVA),
				requestContext.getHeaderString(X_SIGLA_CD_CDS),
				requestContext.getHeaderString(X_SIGLA_CD_CDR)
		);
	}

	public Principal getUserPrincipal()
	{
		return userContext;
	}

	public boolean isUserInRole(String role)
	{
		return true;
	}

	public boolean isSecure()
	{
		return true;
	}

	public String getAuthenticationScheme()
	{
		return "SIGLA Security Context";
	}
}