package it.cnr.contab.web.rest.local.util;

import javax.ejb.Local;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Local
@Path("/tracers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface IteratorTracersLocal {
    @GET
    Response map(@Context HttpServletRequest request) throws Exception;

    @POST
    @Path("/test")
    Response test(@Context HttpServletRequest request) throws Exception;

}
