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
import it.cnr.contab.utenze00.bp.RESTUserContext;
import it.cnr.contab.utenze00.bulk.UtenteBulk;
import it.cnr.contab.web.rest.exception.RestException;
import it.cnr.contab.web.rest.exception.UnauthorizedException;
import it.cnr.contab.web.rest.resource.util.AbstractResource;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;

import java.io.IOException;
import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJBException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.container.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.Providers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
public class RESTSecurityInterceptor implements ContainerRequestFilter, ContainerResponseFilter , ExceptionMapper<Exception> {

	private Logger LOGGER = LoggerFactory.getLogger(RESTSecurityInterceptor.class);

	@Context
	private ResourceInfo resourceInfo;
    @Context
    private Providers providers;
	@Context
	private HttpServletRequest httpServletRequest;
	private static final String AUTHORIZATION_PROPERTY = "Authorization";
	private final static Map<String, String> UNAUTHORIZED_MAP = Collections.singletonMap("ERROR", "User cannot access the resource.");

	private static final String REALM = "SIGLA";
	private static final String AUTHENTICATION_SCHEME = "Basic";

	@Override
	public void filter(ContainerRequestContext requestContext) {	

		final Method method = resourceInfo.getResourceMethod();
		final Class<?> declaring = resourceInfo.getResourceClass();		
		String[] rolesAllowed = null;
		boolean denyAll;
		boolean permitAll;
		boolean allUserAllowedWithoutAbort = declaring.isAnnotationPresent(AllUserAllowedWithoutAbort.class);
		RolesAllowed allowed = declaring.getAnnotation(RolesAllowed.class),
			methodAllowed = method.getAnnotation(RolesAllowed.class);
		if (methodAllowed != null) allowed = methodAllowed;
		if (allowed != null)
		{
			rolesAllowed = allowed.value();
		}
		AccessoAllowed accessoAllowed = declaring.getAnnotation(AccessoAllowed.class),
				accessoMethodAllowed = method.getAnnotation(AccessoAllowed.class);
		if (accessoMethodAllowed != null) accessoAllowed = accessoMethodAllowed;

		
		denyAll = (declaring.isAnnotationPresent(DenyAll.class)
				&& method.isAnnotationPresent(RolesAllowed.class) == false
				&& method.isAnnotationPresent(PermitAll.class) == false) || method.isAnnotationPresent(DenyAll.class);

		permitAll = (declaring.isAnnotationPresent(PermitAll.class) == true
				&& method.isAnnotationPresent(RolesAllowed.class) == false
				&& method.isAnnotationPresent(DenyAll.class) == false) || method.isAnnotationPresent(PermitAll.class);
		
		UtenteBulk utenteBulk = null;		
		if (rolesAllowed != null || accessoAllowed != null || allUserAllowedWithoutAbort) {
			final MultivaluedMap<String, String> headers = requestContext.getHeaders();
			final List<String> authorization = headers.get(AUTHORIZATION_PROPERTY);
			if (!Optional.ofNullable(authorization).filter(s -> !s.isEmpty()).isPresent() && allUserAllowedWithoutAbort) {
				try {
					AbstractResource.getUserContext(requestContext.getSecurityContext(), httpServletRequest);
					return;
				} catch (BadRequestException e) {
					requestContext.abortWith(Response.status(Status.UNAUTHORIZED).build());
					return;
				}
			}
			try {
				utenteBulk = BasicAuthentication.authenticate(httpServletRequest, authorization);
				if (utenteBulk == null){
					requestContext.abortWith(
							Response.status(Response.Status.UNAUTHORIZED)
									.header(HttpHeaders.WWW_AUTHENTICATE,
											AUTHENTICATION_SCHEME + " realm=\"" + REALM + "\"")
									.build());
					return;
				}
		    	requestContext.setSecurityContext(new SIGLASecurityContext(requestContext, utenteBulk.getCd_utente()));
			} catch (UnauthorizedException e) {
				LOGGER.error("ERROR for REST SERVICE", e);
				requestContext.abortWith(Response.status(Status.UNAUTHORIZED).build());
				return;
			} catch (Exception e) {
				LOGGER.error("ERROR for REST SERVICE", e);
				requestContext.abortWith(Response.status(Status.INTERNAL_SERVER_ERROR).entity(e).build());
				return;
			}			
		}		
		if (rolesAllowed != null || denyAll || permitAll) {
			if (denyAll) {
				requestContext.abortWith(Response.status(Status.FORBIDDEN).entity(UNAUTHORIZED_MAP).build());
				return;
			}
			if (permitAll) return;
		    if (rolesAllowed != null) {
				Set<String> rolesSet = new HashSet<String>(
						Arrays.asList(rolesAllowed));
				try {
					if (!isUserAllowed(utenteBulk, rolesSet)) {
						final String message = "User " + utenteBulk.getCd_utente() + " doesn't have the following roles: " + rolesSet;
						LOGGER.warn(message);
						requestContext.abortWith(Response.status(Status.FORBIDDEN).entity(Collections.singletonMap("ERROR", message)).build());
						return;
					}
				} catch (Exception e) {
					requestContext.abortWith(Response.status(Status.INTERNAL_SERVER_ERROR).entity(e).build());
				}		    	
		    }			
		}
		if (accessoAllowed != null) {
			List<String> accessi = Stream.of(accessoAllowed.value()).map(x -> x.name()).collect(Collectors.toList());
			try {
				final UserContext userPrincipal = (UserContext) requestContext.getSecurityContext().getUserPrincipal();
				if (!BasicAuthentication.loginComponentSession().isUserAccessoAllowed(
						userPrincipal,
						accessi.toArray(new String[accessi.size()]))) {
					final String message = "User " + userPrincipal.getUser() + " doesn't have the following access: " + accessi;
					LOGGER.warn(message);
					requestContext.abortWith(Response.status(Status.FORBIDDEN).entity(Collections.singletonMap("ERROR", message)).build());
				}
			} catch (ComponentException|RemoteException|EJBException e) {
				requestContext.abortWith(Response.status(Status.INTERNAL_SERVER_ERROR).entity(e).build());
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
		if (exception.getCause() instanceof RestException)
			return Response.status(((RestException)exception.getCause()).getStatus()).entity(((RestException)exception.getCause()).getErrorMap()).build();			
		LOGGER.error("ERROR for REST SERVICE", exception);
		return Response.status(Status.INTERNAL_SERVER_ERROR).entity(exception).build();
	}

	@Override
	public void filter(ContainerRequestContext containerRequestContext, ContainerResponseContext containerResponseContext) throws IOException {
		final List<String> allowOrigins = Optional.ofNullable(System.getProperty(CORSFilter.CORS_ALLOW_ORIGIN))
				.filter(s -> !s.isEmpty())
				.map(s -> Arrays.asList(s.split(";")))
				.orElse(Collections.emptyList());
		Optional.ofNullable(containerRequestContext.getHeaders())
				.flatMap(s -> Optional.ofNullable(s.getFirst(CORSFilter.ORIGIN)))
				.filter(s -> allowOrigins.contains(s))
				.ifPresent(s -> {
					containerResponseContext.getHeaders().add(CORSFilter.ACCESS_CONTROL_ALLOW_ORIGIN, s);
					containerResponseContext.getHeaders().add(CORSFilter.ACCESS_CONTROL_ALLOW_HEADERS, "content-type");
					containerResponseContext.getHeaders().add(CORSFilter.ACCESS_CONTROL_ALLOW_METHODS,"GET, POST, OPTIONS, PUT, PATCH, DELETE");
					containerResponseContext.getHeaders().add(CORSFilter.ACCESS_CONTROL_ALLOW_CREDENTIALS,Boolean.TRUE);
				});

	}
}