/*
 * Copyright (C) 2020  Consiglio Nazionale delle Ricerche
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

package it.cnr.contab.web.rest.resource.util;

import it.cnr.contab.util.Utility;
import it.cnr.contab.web.rest.local.util.VersionLocal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.stream.Collectors;

@WebListener
public class VersionResource implements ServletContextListener, VersionLocal {
    public static final String IMPLEMENTATION_VERSION = "Implementation-Version";
    public static final String SPECIFICATION_VERSION = "Specification-Version";
    private static Map<Object, Object> ATTRIBUTES;
    private final Logger logger = LoggerFactory.getLogger(VersionResource.class);

    public Response get(HttpServletRequest request) throws Exception {
        if (Optional.ofNullable(ATTRIBUTES).isPresent())
            return Response.ok(ATTRIBUTES).build();
        return Response.noContent().build();
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext ctx = servletContextEvent.getServletContext();
        try {
            final Optional<InputStream> resourceAsStream = Optional.ofNullable(ctx.getResourceAsStream(Utility.MANIFEST_PATH));
            if (resourceAsStream.isPresent()) {
                Manifest manifest = new Manifest(resourceAsStream.get());
                ATTRIBUTES = manifest.getMainAttributes().entrySet()
                        .stream()
                        .filter(objectObjectEntry -> {
                            return Optional.ofNullable(objectObjectEntry.getKey())
                                    .filter(Attributes.Name.class::isInstance)
                                    .map(Attributes.Name.class::cast)
                                    .map(attribute -> attribute.equals(new Attributes.Name(IMPLEMENTATION_VERSION))
                                            || attribute.equals(new Attributes.Name(SPECIFICATION_VERSION)))
                                    .orElse(Boolean.FALSE);
                        })
                        .collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue()));
            }
        } catch (IOException e) {
            logger.warn("IOException", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }

    @OPTIONS
    public Response options() {
        return Response.ok().build();
    }

}
