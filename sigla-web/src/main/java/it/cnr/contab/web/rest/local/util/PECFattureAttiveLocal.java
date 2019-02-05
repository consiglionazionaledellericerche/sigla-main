package it.cnr.contab.web.rest.local.util;

import it.cnr.contab.web.rest.config.AccessoAllowed;
import it.cnr.contab.web.rest.config.AccessoEnum;

import javax.ejb.Local;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Local
@Path("/fatture-attive")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface PECFattureAttiveLocal {

    @GET
    @Path("/reinvia-pec")
    @AccessoAllowed(value= AccessoEnum.XXXHTTPSESSIONXXXXXX)
    Response reinviaPEC(@Context HttpServletRequest request, @QueryParam("esercizio") Integer esercizio, @QueryParam("pgFatturaAttiva") Long pgFatturaAttiva) throws Exception;

}
