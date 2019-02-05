package it.cnr.contab.web.rest.local.util;

import it.cnr.contab.model.Esito;
import it.cnr.contab.web.rest.config.AccessoAllowed;
import it.cnr.contab.web.rest.config.AccessoEnum;

import javax.ejb.Local;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Local
@Path("/messaggi")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface MessaggiSiopePlusLocal {

    @GET
    @Path("/siopeplus")
    @AccessoAllowed(value= AccessoEnum.XXXHTTPSESSIONXXXXXX)
    Response messaggiSiopeplus(@Context HttpServletRequest request) throws Exception;

    @GET
    @Path("/siopeplus/{esito}")
    @AccessoAllowed(value= AccessoEnum.XXXHTTPSESSIONXXXXXX)
    Response esito(@Context HttpServletRequest request, @PathParam("esito") Esito esito,
                   @QueryParam("dataDa") String dataDa, @QueryParam("dataA") String dataA, @QueryParam("download") Boolean download) throws Exception;

}
