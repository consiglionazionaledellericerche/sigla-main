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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@WebFilter(filterName = "CORSFilter", value = "*.do")
public class CORSFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(CORSFilter.class);

    public static final String CORS_ALLOW_ORIGIN = "cors.allow-origin";
    public static final String ORIGIN = "Origin";
    public static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
    public static final String ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";
    public static final String ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods";
    public static final String ACCESS_CONTROL_ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials";
    public static final String GET_POST_OPTIONS_PUT_PATCH_DELETE = "GET, POST, OPTIONS, PUT, PATCH, DELETE";
    public static final String ORIGIN_CONTENT_TYPE_ACCEPT_AUTHORIZATION = "origin, content-type, accept, authorization";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("Init Filter: {}", filterConfig.getFilterName());
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

        logger.debug("CORS Filter AllowOrigins: {} ", allowOrigins);

        httpServletRequest
                .map(httpServletRequest1 -> {
                    final String origin = httpServletRequest1.getHeader(ORIGIN);
                    logger.debug("CORS Filter Origin from Request: {} ", origin);
                    return origin;
                })
                .filter(s -> allowOrigins.contains(s))
                .ifPresent(s -> {
                    httpServletResponse
                            .ifPresent(httpServletResponse1 -> {
                                httpServletResponse1.addHeader(ACCESS_CONTROL_ALLOW_ORIGIN, s);
                                httpServletResponse1.addHeader(ACCESS_CONTROL_ALLOW_HEADERS, ORIGIN_CONTENT_TYPE_ACCEPT_AUTHORIZATION);
                                httpServletResponse1.addHeader(ACCESS_CONTROL_ALLOW_METHODS, GET_POST_OPTIONS_PUT_PATCH_DELETE);
                                httpServletResponse1.addHeader(ACCESS_CONTROL_ALLOW_CREDENTIALS, Boolean.TRUE.toString());
                            });
                });
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        logger.info("Destroy CORS Filter");
    }
}
