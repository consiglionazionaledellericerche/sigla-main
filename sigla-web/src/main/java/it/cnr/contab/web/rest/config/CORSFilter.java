/*
 * Copyright (C) 2022  Consiglio Nazionale delle Ricerche
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

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

@Provider
@WebFilter(filterName = "CORSFilter", servletNames = "ActionServlet", asyncSupported = true)
public class CORSFilter implements ContainerResponseFilter, Filter {
    public static final String CORS_ALLOW_ORIGIN = "cors.allow-origin";
    public static final String ORIGIN = "Origin";
    public static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
    public static final String ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";
    public static final String ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods";
    public static final String ACCESS_CONTROL_ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials";

    @Override
    public void filter(final ContainerRequestContext requestContext, final ContainerResponseContext cres) throws IOException {
        final List<String> allowOrigins = Optional.ofNullable(System.getProperty(CORS_ALLOW_ORIGIN))
                .filter(s -> !s.isEmpty())
                .map(s -> Arrays.asList(s.split(";")))
                .orElse(Collections.emptyList());
        Optional.ofNullable(requestContext.getHeaders())
                .flatMap(s -> Optional.ofNullable(s.getFirst(ORIGIN)))
                .filter(s -> allowOrigins.contains(s))
                .ifPresent(s -> {
                    cres.getHeaders().add(ACCESS_CONTROL_ALLOW_ORIGIN, s);
                    cres.getHeaders().add(ACCESS_CONTROL_ALLOW_HEADERS, "*");
                    cres.getHeaders().add(ACCESS_CONTROL_ALLOW_METHODS,"GET, POST, OPTIONS, PUT, PATCH, DELETE");
                    cres.getHeaders().add(ACCESS_CONTROL_ALLOW_CREDENTIALS,Boolean.TRUE);
                });
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        final Optional<HttpServletResponse> httpServletResponse = Optional.ofNullable(response)
                .filter(HttpServletResponse.class::isInstance)
                .map(HttpServletResponse.class::cast);
        final Optional<HttpServletRequest> httpServletRequest = Optional.ofNullable(request)
                .filter(HttpServletRequest.class::isInstance)
                .map(HttpServletRequest.class::cast);

        final List<String> allowOrigins = Optional.ofNullable(System.getProperty(CORS_ALLOW_ORIGIN))
                .filter(s -> !s.isEmpty())
                .map(s -> Arrays.asList(s.split(";")))
                .orElse(Collections.emptyList());
        httpServletRequest
                .map(httpServletRequest1 -> httpServletRequest1.getHeader(ORIGIN))
                .filter(s -> allowOrigins.contains(s))
                .ifPresent(s -> {
                    httpServletResponse
                        .ifPresent(httpServletResponse1 -> {
                            httpServletResponse1.addHeader(ACCESS_CONTROL_ALLOW_ORIGIN, s);
                            httpServletResponse1.addHeader(ACCESS_CONTROL_ALLOW_HEADERS, "*");
                            httpServletResponse1.addHeader(ACCESS_CONTROL_ALLOW_METHODS,"GET, POST, OPTIONS, PUT, PATCH, DELETE");
                            httpServletResponse1.addHeader(ACCESS_CONTROL_ALLOW_CREDENTIALS,Boolean.TRUE.toString());
                        });
                });
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
