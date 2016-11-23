package it.cnr.contab.util.rest;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;

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
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import it.cnr.contab.anagraf00.tabrif.bulk.Rif_inquadramentoBulk;
import it.cnr.contab.anagraf00.tabrif.bulk.Rif_inquadramentoHome;
import it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk;
import it.cnr.contab.missioni00.docs.bulk.MissioneBulk;
import it.cnr.contab.missioni00.docs.bulk.Missione_dettaglioBulk;
import it.cnr.contab.missioni00.ejb.MissioneComponentSession;
import it.cnr.contab.missioni00.tabrif.bulk.Missione_tipo_pastoBulk;
import it.cnr.contab.missioni00.tabrif.bulk.Missione_tipo_pastoHome;
import it.cnr.contab.missioni00.tabrif.bulk.Missione_tipo_pastoKey;
import it.cnr.contab.missioni00.tabrif.bulk.Missione_tipo_spesaBulk;
import it.cnr.contab.missioni00.tabrif.bulk.Missione_tipo_spesaHome;
import it.cnr.contab.missioni00.tabrif.bulk.Missione_tipo_spesaKey;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.sql.HomeCache;
import it.cnr.jada.util.ejb.EJBCommonServices;

@Path("/servizirest")
@DeclareRoles({"MISSIONI"})
@Consumes({ "application/json" })
@Produces({ "application/json" })
public class SIGLAResource {
    private final Logger log = LoggerFactory.getLogger(SIGLAResource.class);
    public final static String DATE_FORMAT = "dd/MM/yyyy";

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
    @Path(value = "/getDivisa")
    public Response getDivisa(@Context HttpServletRequest request, @QueryParam("data") String data,@QueryParam("nazione") Long nazione,@QueryParam("inquadramento") Long inquadramento) throws Exception {
        log.debug("REST request per visualizzare la divisa per nazione" );
    	JSONRESTRequest jsonRequest = new Gson().fromJson(new JsonParser().parse(request.getReader()), JSONRESTRequest.class);
    	
    	UserContext userContext = BasicAuthentication.getContextFromRequest(jsonRequest, getUser(request), request.getRequestedSessionId());
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        Date parsedDate = dateFormat.parse(data);
        Timestamp dataTappa = new Timestamp(parsedDate.getTime());
        
		Connection conn = EJBCommonServices.getConnection(userContext);
		HomeCache homeCache = new HomeCache(conn);			   
		Rif_inquadramentoHome home=(Rif_inquadramentoHome)homeCache.getHome(Rif_inquadramentoBulk.class);
		Rif_inquadramentoBulk rifInquadramento = (Rif_inquadramentoBulk)home.findByPrimaryKey(new Rif_inquadramentoBulk(inquadramento));
        
    	ResponseBuilder rb;
		if (rifInquadramento != null){
	    	DivisaBulk divisa = missioneComponent().recuperoDivisa(userContext, nazione, rifInquadramento.getCd_gruppo_inquadramento(), dataTappa);
	    	String resp = new Gson().toJson(divisa);
	    	resp = new GsonBuilder()
	                .setDateFormat(DATE_FORMAT)
	                .registerTypeAdapter(Timestamp.class, new SqlTimestampConverter())
	                .create()
	                .toJson(divisa);
			try {
				rb = Response.ok(resp);
			} catch (Exception e) {
				log.error("error getting json labels {}", "1", e);
				rb = Response.status(Status.INTERNAL_SERVER_ERROR).entity(Collections.singletonMap("message", e.getMessage()));
			}
		} else {
			rb = Response.status(Status.INTERNAL_SERVER_ERROR).entity("Errore! Inquadramento non esistente.");
		}
		return rb.build();		
	}
    
    @POST
	@RolesAllowed({"MISSIONI"})
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @Path(value = "/getCambio")
    public Response getCambio(@Context HttpServletRequest request, @QueryParam("data") String data,@QueryParam("divisa") String divisa) throws Exception {
        log.debug("REST request per visualizzare la divisa per nazione" );
    	JSONRESTRequest jsonRequest = new Gson().fromJson(new JsonParser().parse(request.getReader()), JSONRESTRequest.class);
    	
    	UserContext userContext = BasicAuthentication.getContextFromRequest(jsonRequest, getUser(request), request.getRequestedSessionId());
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        Date parsedDate = dateFormat.parse(data);
        Timestamp dataMissione = new Timestamp(parsedDate.getTime());
        
    	BigDecimal cambio = missioneComponent().recuperoCambio(userContext, divisa, dataMissione);
    	String resp = new Gson().toJson(divisa);
    	resp = new GsonBuilder()
    			.create()
    			.toJson(cambio);
    	return response(resp);		
	}

    @POST
	@RolesAllowed({"MISSIONI"})
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @Path(value = "/getDivisaDefault")
    public Response getDivisaDefault(@Context HttpServletRequest request) throws Exception {
        log.debug("REST request per visualizzare la divisa per nazione" );
    	JSONRESTRequest jsonRequest = new Gson().fromJson(new JsonParser().parse(request.getReader()), JSONRESTRequest.class);
    	
    	UserContext userContext = BasicAuthentication.getContextFromRequest(jsonRequest, getUser(request), request.getRequestedSessionId());
        
    	DivisaBulk divisa = missioneComponent().getDivisaDefault(userContext);
    	String resp = new Gson().toJson(divisa);
    	resp = new GsonBuilder()
                .setDateFormat(DATE_FORMAT)
                .registerTypeAdapter(Timestamp.class, new SqlTimestampConverter())
                .create()
                .toJson(divisa);
    	return response(resp);		
	}

	private Response response(String resp) {
		ResponseBuilder rb;
		try {
    		rb = Response.ok(resp);
    	} catch (Exception e) {
    		log.error("error getting json labels {}", "1", e);
    		rb = Response.status(Status.INTERNAL_SERVER_ERROR).entity(Collections.singletonMap("message", e.getMessage()));
    	}
		return rb.build();
	}
    public BigDecimal recuperoCambio(UserContext userContext, String divisa, Timestamp dataInizioMissione) throws ComponentException{
		return BigDecimal.ZERO;		
    }

	private MissioneComponentSession missioneComponent() throws javax.ejb.EJBException, java.rmi.RemoteException {
		return (MissioneComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRMISSIONI00_EJB_MissioneComponentSession");
	}

	private String getUser(HttpServletRequest request) throws ComponentException, IOException{
		String authorization = request.getHeader("Authorization");
		return BasicAuthentication.getUsername(authorization);
	}

    @POST
	@RolesAllowed({"MISSIONI"})
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @Path(value = "/validaMassimaleSpesa")
    public Response validaMassimaleSpesa(@Context HttpServletRequest request, @QueryParam("cdTipoSpesa") String cdTipoSpesa, @QueryParam("km") String km,
    		@QueryParam("importoSpesa") String importoSpesa, @QueryParam("cdTipoPasto") String cdTipoPasto, @QueryParam("data") String data,
    		@QueryParam("area") String area, @QueryParam("areaEstera") String areaEstera, @QueryParam("nazione") Long nazione,
    		@QueryParam("inquadramento") Long inquadramento) throws Exception {
        log.debug("REST request per visualizzare la divisa per nazione" );
    	JSONRESTRequest jsonRequest = new Gson().fromJson(new JsonParser().parse(request.getReader()), JSONRESTRequest.class);

    	UserContext userContext = BasicAuthentication.getContextFromRequest(jsonRequest, getUser(request), request.getRequestedSessionId());
		
    	Connection conn = EJBCommonServices.getConnection(userContext);
		HomeCache homeCache = new HomeCache(conn);

		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        Date parsedDate = dateFormat.parse(data);
        Timestamp dataMissione = new Timestamp(parsedDate.getTime());

		Missione_tipo_spesaBulk tipoSpesa = null;
    	if (cdTipoSpesa != null){
        	Missione_tipo_spesaKey keySpesa = new Missione_tipo_spesaKey(cdTipoSpesa, dataMissione, nazione, inquadramento, area);
    		Missione_tipo_spesaHome homeSpesa=(Missione_tipo_spesaHome)homeCache.getHome(Missione_tipo_spesaBulk.class);
    		tipoSpesa = (Missione_tipo_spesaBulk)homeSpesa.findByPrimaryKey(keySpesa);
    	} else {
    		rb = Response.status(Status.INTERNAL_SERVER_ERROR).entity(Collections.singletonMap("message", "Errore, parametro tipo spesa obbligatorio."));
    	}

		Missione_tipo_pastoBulk tipoPasto = null;
		if (cdTipoPasto != null){
			Missione_tipo_pastoKey keyPasto = new Missione_tipo_pastoKey(cdTipoPasto, dataMissione, nazione, inquadramento, area, areaEstera);
			Missione_tipo_pastoHome home=(Missione_tipo_pastoHome)homeCache.getHome(Missione_tipo_pastoBulk.class);
			tipoPasto = (Missione_tipo_pastoBulk)home.findByPrimaryKey(keyPasto);
		}

		MissioneBulk missioneBulk = new MissioneBulk();
		Missione_dettaglioBulk dettaglio = new Missione_dettaglioBulk();
		dettaglio.setTipo_pasto(tipoPasto);
		dettaglio.setTipo_spesa(tipoSpesa);
		dettaglio.setChilometri(new BigDecimal(km));
		dettaglio.setIm_spesa_euro(new BigDecimal(importoSpesa));
		dettaglio.setIm_spesa_divisa(dettaglio.getIm_spesa_euro());
		missioneComponent().validaMassimaliSpesa(userContext, missioneBulk, dettaglio);
		String resp = new Gson().toJson(divisa);
    	resp = new GsonBuilder()
    			.create()
    			.toJson(cambio);
    	return response(resp);		
	}

}