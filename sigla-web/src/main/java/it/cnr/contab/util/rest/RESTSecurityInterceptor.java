package it.cnr.contab.util.rest;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.resteasy.core.Headers;
import org.jboss.resteasy.core.ResourceMethodInvoker;
import org.jboss.resteasy.core.ServerResponse;

import it.cnr.contab.utenze00.bp.RESTUserContext;
import it.cnr.contab.utenze00.bulk.UtenteBulk;
import it.cnr.contab.utenze00.bulk.Utente_unita_ruoloBulk;
import it.cnr.jada.UserContext;

@Provider
public class RESTSecurityInterceptor implements
		javax.ws.rs.container.ContainerRequestFilter,
		ExceptionMapper<Exception> {

	private Log log = LogFactory.getLog(RESTSecurityInterceptor.class);

	private static final String AUTHORIZATION_PROPERTY = "Authorization";
	private static final ServerResponse ACCESS_DENIED = new ServerResponse(
			"Access denied for this resource", 401, new Headers<Object>());;
	private static final ServerResponse ACCESS_FORBIDDEN = new ServerResponse(
			"Nobody can access this resource", 403, new Headers<Object>());;
	private static final ServerResponse SERVER_ERROR = new ServerResponse(
			"INTERNAL SERVER ERROR", 500, new Headers<Object>());

	@Override
	public void filter(ContainerRequestContext requestContext) {
		log.info("filter");

		ResourceMethodInvoker methodInvoker = (ResourceMethodInvoker) requestContext
				.getProperty("org.jboss.resteasy.core.ResourceMethodInvoker");
		Method method = methodInvoker.getMethod();
		// Access allowed for all
		if (!method.isAnnotationPresent(PermitAll.class)) {
			// Access denied for all
			if (method.isAnnotationPresent(DenyAll.class)) {
				requestContext.abortWith(ACCESS_FORBIDDEN);
				return;
			}

			// Get request headers
			final MultivaluedMap<String, String> headers = requestContext
					.getHeaders();

			// Fetch authorization header
			final List<String> authorization = headers
					.get(AUTHORIZATION_PROPERTY);
			UtenteBulk utenteBulk = null;
			try {
				utenteBulk = BasicAuthentication.authenticate(authorization);
				if (utenteBulk == null){
					requestContext.abortWith(ACCESS_DENIED);
				}
			} catch (Exception e) {
				requestContext.abortWith(SERVER_ERROR);
			}

			// Verify user access
			if (method.isAnnotationPresent(RolesAllowed.class)) {
				RolesAllowed rolesAnnotation = method
						.getAnnotation(RolesAllowed.class);
				Set<String> rolesSet = new HashSet<String>(
						Arrays.asList(rolesAnnotation.value()));
				try {
					if (!isUserAllowed(utenteBulk, rolesSet)) {
						requestContext.abortWith(ACCESS_DENIED);
						return;
					}
				} catch (Exception e) {
					requestContext.abortWith(SERVER_ERROR);
				}
			}
		}
	}

	private boolean isUserAllowed(final UtenteBulk utente,
			final Set<String> rolesSet) throws Exception{
		boolean isAllowed = false;

		UserContext userContext = new RESTUserContext();
		try {
			List lista = BasicAuthentication.getRuoli(userContext, utente);
			if (lista != null){
				for (Object obj:lista){
					Utente_unita_ruoloBulk ruolo = (Utente_unita_ruoloBulk)obj;
					if (rolesSet.contains(ruolo.getCd_ruolo())) {
						isAllowed = true;
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}

		// Step 2. Verify user role
		return isAllowed;
	}

	@Override
	public Response toResponse(Exception exception) {
		// TODO Auto-generated method stub
		return null;
	}

}
