package it.cnr.contab.web.rest.local.docamm;

import it.cnr.contab.client.docamm.FatturaAttiva;
import it.cnr.contab.web.rest.config.AccessoAllowed;
import it.cnr.contab.web.rest.config.AccessoEnum;

import java.util.List;

import javax.ejb.Local;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Local
@Path("/fatturaattiva")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface FatturaAttivaLocal {
	/**
     * GET  /restapi/fatturaattiva/ricerca -> return Fattura attiva
     */
    @GET
    @AccessoAllowed(value=AccessoEnum.AMMFATTURDOCSFATATTV)
    public Response ricercaFattura(@Context HttpServletRequest request, @QueryParam ("pg") Long pg) throws Exception;

	/**
	 * POST  /restapi/fatturaattiva-> return Fattura attiva
	 */
	@POST
	@AccessoAllowed(value=AccessoEnum.AMMFATTURDOCSFATATTM)
	public Response inserisciFatture(@Context HttpServletRequest request, List<FatturaAttiva> fatture) throws Exception;
	
	/**
     * GET  /restapi/fatturaattiva/ricerca -> return Fattura attiva
     */
    @GET
    @Path("/print")
    @AccessoAllowed(value=AccessoEnum.AMMFATTURDOCSFATATTV)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response stampaFattura(@Context HttpServletRequest request, @QueryParam ("pg") Long pg) throws Exception;

}
