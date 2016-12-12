package it.cnr.contab.web.rest.config;

import it.cnr.contab.utenze00.bp.RESTUserContext;
import it.cnr.contab.utenze00.bulk.UtenteBulk;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.resteasy.core.ResourceMethodInvoker;

@Provider
public class RESTSecurityInterceptor implements
javax.ws.rs.container.ContainerRequestFilter,
ExceptionMapper<Exception> {

	private Log LOGGER = LogFactory.getLog(RESTSecurityInterceptor.class);

	private static final String AUTHORIZATION_PROPERTY = "Authorization";
	private final static Map<String, String> UNAUTHORIZED_MAP = Collections.singletonMap("ERROR", "User cannot access the resource.");
	@Override
	public void filter(ContainerRequestContext requestContext) {	
		ResourceMethodInvoker methodInvoker = (ResourceMethodInvoker) requestContext
				.getProperty(ResourceMethodInvoker.class.getName());
		final Method method = methodInvoker.getMethod();
		final Class<?> declaring = method.getDeclaringClass();
		
		String[] rolesAllowed = null;
		boolean denyAll;
		boolean permitAll;
		RolesAllowed allowed = (RolesAllowed) declaring.getAnnotation(RolesAllowed.class);
		RolesAllowed methodAllowed = method.getAnnotation(RolesAllowed.class);
		if (methodAllowed != null) allowed = methodAllowed;
		if (allowed != null)
		{
			rolesAllowed = allowed.value();
		}

		denyAll = (declaring.isAnnotationPresent(DenyAll.class)
				&& method.isAnnotationPresent(RolesAllowed.class) == false
				&& method.isAnnotationPresent(PermitAll.class) == false) || method.isAnnotationPresent(DenyAll.class);

		permitAll = (declaring.isAnnotationPresent(PermitAll.class) == true
				&& method.isAnnotationPresent(RolesAllowed.class) == false
				&& method.isAnnotationPresent(DenyAll.class) == false) || method.isAnnotationPresent(PermitAll.class);

		if (rolesAllowed != null || denyAll || permitAll) {
			if (denyAll) {
				requestContext.abortWith(Response.status(Status.FORBIDDEN).entity(UNAUTHORIZED_MAP).build());
				return;
			}
			if (permitAll) return;
		    if (rolesAllowed != null) {
				final MultivaluedMap<String, String> headers = requestContext.getHeaders();
				final List<String> authorization = headers.get(AUTHORIZATION_PROPERTY);
				UtenteBulk utenteBulk = null;
				try {
					utenteBulk = BasicAuthentication.authenticate(authorization);
					if (utenteBulk == null){
						requestContext.abortWith(Response.status(Status.UNAUTHORIZED).entity(UNAUTHORIZED_MAP).build());
						return;
					}
				} catch (Exception e) {
					LOGGER.error("ERROR for REST SERVICE", e);
					requestContext.abortWith(Response.status(Status.INTERNAL_SERVER_ERROR).entity(e).build());
					return;
				}
				Set<String> rolesSet = new HashSet<String>(
						Arrays.asList(rolesAllowed));
				try {
					if (!isUserAllowed(utenteBulk, rolesSet)) {
						requestContext.abortWith(Response.status(Status.UNAUTHORIZED).entity(Collections.singletonMap("ERROR", "User doesn't have the following roles: " + rolesSet)).build());
						return;
					}
				} catch (Exception e) {
					requestContext.abortWith(Response.status(Status.INTERNAL_SERVER_ERROR).entity(e).build());
				}		    	
		    }			
		}
	}

	private boolean isUserAllowed(final UtenteBulk utente,
			final Set<String> rolesSet) throws Exception{
		try {
			return Optional.ofNullable(BasicAuthentication.getRuoli(new RESTUserContext(), utente))
					.map(x -> x.stream())
					.get()
					.filter(x -> rolesSet.contains(x.getCd_ruolo())).count() > 0;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public Response toResponse(Exception exception) {
		LOGGER.error("ERROR for REST SERVICE", exception);
		return Response.status(Status.INTERNAL_SERVER_ERROR).entity(exception).build();
	}
}