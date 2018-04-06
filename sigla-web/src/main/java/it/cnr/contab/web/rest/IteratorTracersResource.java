package it.cnr.contab.web.rest;

import it.cnr.jada.util.ejb.EJBTracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

@Stateless
public class IteratorTracersResource implements IteratorTracersLocal {
    private final Logger LOGGER = LoggerFactory.getLogger(IteratorTracersResource.class);

    public Response map(@Context HttpServletRequest request) throws Exception {
        return Response.ok(EJBTracer.getInstance().getTracers()).build();
    }
}