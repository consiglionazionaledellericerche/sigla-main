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