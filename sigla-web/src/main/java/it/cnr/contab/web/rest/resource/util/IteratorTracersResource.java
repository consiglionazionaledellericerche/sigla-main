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

package it.cnr.contab.web.rest.resource.util;

import it.cnr.contab.web.rest.local.util.IteratorTracersLocal;
import it.cnr.jada.util.ejb.EJBTracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@Stateless
public class IteratorTracersResource implements IteratorTracersLocal {
    private final Logger LOGGER = LoggerFactory.getLogger(IteratorTracersResource.class);

    public Response map(@Context HttpServletRequest request) throws Exception {
        final Collection<EJBTracer.IteratorTracer> values = EJBTracer.getInstance()
                .getTracers()
                .values();
        return Response.ok(Collections.singletonMap(values.size(),
                values
                        .parallelStream()
                        .sorted((iteratorTracer, t1) -> iteratorTracer.getCreationDate().compareTo(t1.getCreationDate()))
                        .collect(Collectors.toList()))
        ).build();
    }
}