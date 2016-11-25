package it.cnr.contab.util.rest;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
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
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import it.cnr.contab.anagraf00.tabrif.bulk.Rif_inquadramentoBulk;
import it.cnr.contab.anagraf00.tabrif.bulk.Rif_inquadramentoHome;
import it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk;
import it.cnr.contab.anagraf00.tabter.bulk.NazioneHome;
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
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.HomeCache;
import it.cnr.jada.persistency.sql.SQLBuilder;
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
    public Response validaMassimaleSpesa(@Context HttpServletRequest request) throws Exception {
		ResponseBuilder rb;
    	try{
            log.debug("REST request per visualizzare la divisa per nazione" );
        	JSONRESTMissioniRequest jsonRequest = new Gson().fromJson(new JsonParser().parse(request.getReader()), JSONRESTMissioniRequest.class);

        	UserContext userContext = BasicAuthentication.getContextFromRequest(jsonRequest, getUser(request), request.getRequestedSessionId());
    		
        	Connection conn = EJBCommonServices.getConnection(userContext);
    		HomeCache homeCache = new HomeCache(conn);

        	if (jsonRequest.getData() == null){
				throw new RestException(Status.BAD_REQUEST, "Errore, data dettaglio spesa obbligatoria.");
        	}
    		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
            Date parsedDate = dateFormat.parse(jsonRequest.getData());
            Timestamp dataMissione = new Timestamp(parsedDate.getTime());

    		Missione_tipo_spesaBulk tipoSpesa = null;
        	if (jsonRequest.getImportoSpesa() == null){
				throw new RestException(Status.BAD_REQUEST, "Errore, importo dettaglio spesa obbligatorio.");
        	}
        	if (jsonRequest.getDivisa() == null){
				throw new RestException(Status.BAD_REQUEST, "Errore, divisa dettaglio spesa obbligatoria.");
        	}
        	if (jsonRequest.getCdTipoSpesa() != null){
    			NazioneBulk nazioneBulk = getNazione(jsonRequest.getNazione(), homeCache); 
    			SQLBuilder sql = missioneComponent().selectTipo_spesaByClause(userContext, dataMissione, jsonRequest.getInquadramento(), nazioneBulk, false, jsonRequest.getCdTipoSpesa(), new CompoundFindClause());

        		Missione_tipo_spesaHome homeSpesa=(Missione_tipo_spesaHome)homeCache.getHome(Missione_tipo_spesaBulk.class);
    			List lista = homeSpesa.fetchAll(sql);
    			if (lista != null && !lista.isEmpty()){
    	    		tipoSpesa = (Missione_tipo_spesaBulk)lista.get(0);
    			}
    			if (tipoSpesa == null){
    				throw new RestException(Status.BAD_REQUEST, "Tipo Spesa non trovato in SIGLA.");
    			}
        	} else {
				throw new RestException(Status.BAD_REQUEST, "Errore, parametro tipo spesa obbligatorio.");
        	}

    		Missione_tipo_pastoBulk tipoPasto = null;
    		if (jsonRequest.getCdTipoPasto() != null){
    			Missione_tipo_pastoHome home=(Missione_tipo_pastoHome)homeCache.getHome(Missione_tipo_pastoBulk.class);
    			NazioneBulk nazioneBulk = getNazione(jsonRequest.getNazione(), homeCache); 
    			SQLBuilder sql = missioneComponent().selectTipo_pastoByClause(userContext, dataMissione, jsonRequest.getInquadramento(), nazioneBulk, jsonRequest.getCdTipoPasto(), null);    			
    			List lista = home.fetchAll(sql);
    			if (lista != null && !lista.isEmpty()){
    	    		tipoPasto = (Missione_tipo_pastoBulk)lista.get(0);
    			}
    			if (tipoPasto == null){
    				throw new RestException(Status.BAD_REQUEST, "Tipo Pasto non trovato in SIGLA.");
    			}
    		}

    		MissioneBulk missioneBulk = new MissioneBulk();
    		Missione_dettaglioBulk dettaglio = new Missione_dettaglioBulk();
    		dettaglio.setTipo_pasto(tipoPasto);
    		dettaglio.setTipo_spesa(tipoSpesa);
    		dettaglio.setChilometri(new BigDecimal(jsonRequest.getKm() == null ? "0" : jsonRequest.getKm()));
    		dettaglio.setPercentuale_maggiorazione(BigDecimal.ZERO);
    		dettaglio.setIm_base_maggiorazione(BigDecimal.ZERO);
    		dettaglio.setIm_spesa_euro(new BigDecimal(jsonRequest.getImportoSpesa()));
    		dettaglio.setIm_spesa_divisa(dettaglio.getIm_spesa_euro());
    		dettaglio.setCd_divisa_spesa(jsonRequest.getDivisa());
        	try{
        		missioneComponent().validaMassimaliSpesa(userContext, missioneBulk, dettaglio);
        	}
        	catch (ValidationException e) {
        		throw new RestException(Status.BAD_REQUEST, e.getMessage());
        	} 	
    		rb = Response.ok("OK");
    	} catch (RestException restException) {
    		rb = Response.status(restException.getStatus()).entity(Collections.singletonMap("message",restException.getMessage()));
    	} catch (Exception e) {
    		log.error("error rest validaMassimaleSpesa", "1", e);
    		rb = Response.status(Status.INTERNAL_SERVER_ERROR).entity(Collections.singletonMap("message", e.getMessage()));
    	}
    	return rb.build();		
	}

	private NazioneBulk getNazione(Long nazione, HomeCache homeCache) throws PersistencyException {
		NazioneHome nazionehome=(NazioneHome)homeCache.getHome(NazioneBulk.class);
		NazioneBulk nazioneBulk = new NazioneBulk(nazione);
		nazioneBulk = (NazioneBulk)nazionehome.findByPrimaryKey(nazioneBulk);
		return nazioneBulk;
	}

}