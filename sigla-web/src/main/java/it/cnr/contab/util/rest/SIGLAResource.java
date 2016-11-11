package it.cnr.contab.util.rest;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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

import it.cnr.contab.missioni00.ejb.MissioneComponentSession;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;

@Path("/servizirest")
@DeclareRoles({"MISSIONI"})
@Consumes({ "application/json" })
@Produces({ "application/json" })
public class SIGLAResource {
    private final Logger log = LoggerFactory.getLogger(SIGLAResource.class);

    /**
     * GET  /rest/ordineMissione -> get Ordini di missione per l'utente 
     */
    @GET
	@RolesAllowed({"MISSIONI"})
    @Path(value = "/verificaValiditaDettaglio")
    public Response rigaValida(@Context HttpServletRequest request, @QueryParam("data") String data,@QueryParam("nazione") Long nazione,@QueryParam("inquadramento") Long inquadramento) throws Exception {
        log.debug("REST request per visualizzare i dati degli Ordini di Missione " );
        JSONRESTRequest jsonRequest = new Gson().fromJson(new JsonParser().parse(request.getReader()), JSONRESTRequest.class);
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

    @POST
	@RolesAllowed({"MISSIONI"})
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @Path(value = "/getTipiSpesa")
    public Response getTipiSpesa(@Context HttpServletRequest request, @QueryParam("data") String data,@QueryParam("nazione") Long nazione,@QueryParam("inquadramento") Long inquadramento, @QueryParam("ammissibileRimborso") String ammissibileRimborso) throws Exception {
        log.debug("REST request per visualizzare i dati degli Ordini di Missione " );
    	JSONRESTRequest jsonRequest = new Gson().fromJson(new JsonParser().parse(request.getReader()), JSONRESTRequest.class);
    	
    	UserContext userContext = BasicAuthentication.getContextFromRequest(jsonRequest, getUser(request), request.getRequestedSessionId());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date parsedDate = dateFormat.parse(data);
        Timestamp dataTappa = new Timestamp(parsedDate.getTime());
    	List lista = missioneComponent().recuperoTipiSpesa(userContext, dataTappa, nazione, inquadramento, new Boolean (ammissibileRimborso));
    	String resp = new Gson().toJson(lista);
    	ResponseBuilder rb;
		JsonObject labels = new JsonObject();
		try {
			rb = Response.ok(resp);
		} catch (Exception e) {
			log.error("error getting json labels {}", "1", e);
			rb = Response.status(Status.INTERNAL_SERVER_ERROR).entity(Collections.singletonMap("message", e.getMessage()));
		}
		return rb.build();		
	}
	private MissioneComponentSession missioneComponent() throws javax.ejb.EJBException, java.rmi.RemoteException {
		return (MissioneComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRMISSIONI00_EJB_MissioneComponentSession");
	}

	private String getUser(HttpServletRequest request) throws ComponentException, IOException{
		String authorization = request.getHeader("Authorization");
		return BasicAuthentication.getUsername(authorization);
	}
}
