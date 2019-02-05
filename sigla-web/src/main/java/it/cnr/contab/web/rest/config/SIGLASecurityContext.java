package it.cnr.contab.web.rest.config;

import it.cnr.contab.utenze00.bp.CNRUserContext;

import java.security.Principal;
import java.util.Calendar;
import java.util.Optional;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.SecurityContext;

public class SIGLASecurityContext implements SecurityContext {

	private static final String X_SIGLA_CD_CDR = "X-sigla-cd-cdr";
	private static final String X_SIGLA_CD_CDS = "X-sigla-cd-cds";
	private static final String X_SIGLA_CD_UNITA_ORGANIZZATIVA = "X-sigla-cd-unita-organizzativa";
	private static final String X_SIGLA_ESERCIZIO = "X-sigla-esercizio";
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