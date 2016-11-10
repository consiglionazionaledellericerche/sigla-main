package it.cnr.contab.util.rest;

import java.util.Collections;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Path("/missioni")
@DeclareRoles({"MissioniRole"})
@Consumes({ "application/json" })
@Produces({ "application/json" })
public class SIGLAResource {
    private final Logger log = LoggerFactory.getLogger(SIGLAResource.class);

    /**
     * GET  /rest/ordineMissione -> get Ordini di missione per l'utente 
     */
    @GET
	@RolesAllowed({"MissioniRole"})
    @Path(value = "/verificaValiditaDettaglio")
    public Response rigaValida(@Context HttpServletRequest request, @QueryParam("data") String data,@QueryParam("nazione") Long nazione,@QueryParam("inquadramento") Long inquadramento) throws Exception {
        log.debug("REST request per visualizzare i dati degli Ordini di Missione " );
    	JSONRequest jsonRequest = new Gson().fromJson(new JsonParser().parse(request.getReader()), JSONRequest.class);
    	ResponseBuilder rb;
		JsonObject labels = new JsonObject();
		try {
			rb = Response.ok(labels != null ? labels.toString() : "");
		} catch (Exception e) {
			log.error("error getting json labels {}", "1", e);
			rb = Response.status(Status.INTERNAL_SERVER_ERROR).entity(Collections.singletonMap("message", e.getMessage()));
		}
		return rb.build();		
	}

}
