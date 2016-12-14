package it.cnr.contab.web.rest;

import it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk;
import it.cnr.contab.missioni00.docs.bulk.MissioneBulk;
import it.cnr.contab.missioni00.docs.bulk.Missione_dettaglioBulk;
import it.cnr.contab.missioni00.ejb.MissioneComponentSession;
import it.cnr.contab.missioni00.tabrif.bulk.Missione_tipo_pastoBulk;
import it.cnr.contab.missioni00.tabrif.bulk.Missione_tipo_spesaBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.web.rest.config.RestException;
import it.cnr.contab.web.rest.config.SIGLARoles;
import it.cnr.contab.web.rest.model.MassimaleSpesaBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.ejb.CRUDComponentSession;
import it.cnr.jada.persistency.PersistencyException;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJBException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/missioni")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed(SIGLARoles.MISSIONI)
public class MissioneResource {
    private final Logger LOGGER = LoggerFactory.getLogger(MissioneResource.class);
	@Context SecurityContext securityContext;

    @POST
    @Path(value = "/validaMassimaleSpesa")
    public Response validaMassimaleSpesa(@Context HttpServletRequest request, MassimaleSpesaBulk massimaleSpesaBulk) throws Exception {
		ResponseBuilder rb;
    	try{
            LOGGER.debug("REST request per visualizzare la divisa per nazione" );
        	UserContext userContext = (CNRUserContext) securityContext.getUserPrincipal();
    		Optional.ofNullable(massimaleSpesaBulk.getData()).orElseThrow(() -> new RestException(Status.BAD_REQUEST, "Errore, data dettaglio spesa obbligatoria."));
    		Optional.ofNullable(massimaleSpesaBulk.getImportoSpesa()).orElseThrow(() -> new RestException(Status.BAD_REQUEST, "Errore, importo dettaglio spesa obbligatorio."));
    		Optional.ofNullable(massimaleSpesaBulk.getDivisa()).orElseThrow(() -> new RestException(Status.BAD_REQUEST, "Errore, divisa dettaglio spesa obbligatoria."));

    		Timestamp dataMissione = new Timestamp(massimaleSpesaBulk.getData().getTime());
    		Missione_tipo_spesaBulk tipoSpesa = null;

			NazioneBulk nazioneBulk = getNazione(userContext, massimaleSpesaBulk.getNazione()); 
        	if (massimaleSpesaBulk.getCdTipoSpesa() != null){
        		List<?> lista = missioneComponent().recuperoTipiSpesa(userContext, dataMissione, massimaleSpesaBulk.getInquadramento(), 
        				nazioneBulk.getPg_nazione(), false, massimaleSpesaBulk.getCdTipoSpesa());
    			if (lista != null && !lista.isEmpty()){
    	    		tipoSpesa = (Missione_tipo_spesaBulk)lista.get(0);
    			}
    			Optional.ofNullable(tipoSpesa).orElseThrow(() -> new RestException(Status.BAD_REQUEST, "Tipo Spesa non trovato in SIGLA."));
        	} else {
				throw new RestException(Status.BAD_REQUEST, "Errore, parametro tipo spesa obbligatorio.");
        	}

    		Missione_tipo_pastoBulk tipoPasto = null;
    		if (massimaleSpesaBulk.getCdTipoPasto() != null){
    			List<?> lista = missioneComponent().recuperoTipi_pasto(userContext, dataMissione, massimaleSpesaBulk.getInquadramento(), nazioneBulk, massimaleSpesaBulk.getCdTipoPasto(), null);    			
    			if (lista != null && !lista.isEmpty()){
    	    		tipoPasto = (Missione_tipo_pastoBulk)lista.get(0);
    			}
    			Optional.ofNullable(tipoPasto).orElseThrow(() -> new RestException(Status.BAD_REQUEST,  "Tipo Pasto non trovato in SIGLA."));
    		}
    		MissioneBulk missioneBulk = new MissioneBulk();
    		Missione_dettaglioBulk dettaglio = new Missione_dettaglioBulk();
    		dettaglio.setTipo_pasto(tipoPasto);
    		dettaglio.setTipo_spesa(tipoSpesa);
    		dettaglio.setChilometri(Optional.ofNullable(massimaleSpesaBulk.getKm()).orElse(BigDecimal.ZERO));
    		dettaglio.setPercentuale_maggiorazione(BigDecimal.ZERO);
    		dettaglio.setIm_base_maggiorazione(BigDecimal.ZERO);
    		dettaglio.setIm_spesa_euro(massimaleSpesaBulk.getImportoSpesa());
    		dettaglio.setIm_spesa_divisa(dettaglio.getIm_spesa_euro());
    		dettaglio.setCd_divisa_spesa(massimaleSpesaBulk.getDivisa());
        	try{
        		missioneComponent().validaMassimaliSpesa(userContext, missioneBulk, dettaglio);
        	} catch (ValidationException e) {
        		throw new RestException(Status.BAD_REQUEST, e.getMessage());
        	} 	
    		rb = Response.ok("OK");
    	} catch (RestException restException) {
    		rb = Response.status(restException.getStatus()).entity(Collections.singletonMap("ERROR", restException.getMessage()));
    	}
    	return rb.build();		
	}
    
    @PUT
    public Response insert(@Context HttpServletRequest request, MissioneBulk missioneBulk) throws Exception {
    	UserContext userContext = (CNRUserContext) securityContext.getUserPrincipal();
    	final MissioneBulk missione = (MissioneBulk) missioneComponent().inizializzaBulkPerInserimento(
    			userContext, 
    			missioneBulk);
    	missione.setToBeCreated();
    	missione.getTappeMissioneColl().stream().forEach(x -> {
    		x.setToBeCreated();
    		x.setMissione(missione);	
    	});    	
    	Stream.concat(
    			Stream.concat(
					missione.getSpeseMissioneColl().stream(), 
					missione.getDiariaMissioneColl().stream()
    			),
    			missione.getRimborsoMissioneColl().stream()).forEach((x -> {
    		x.setToBeCreated();
    		x.setMissione(missione);
    	}));
    	MissioneBulk missioneCreated = (MissioneBulk) missioneComponent().creaConBulk(userContext, missione);
    	missioneCreated.setToBeUpdated();
    	missioneCreated.setMissioneIniziale(missioneCreated);
    	missioneCreated = (MissioneBulk) missioneComponent().creaConBulk(userContext, missioneCreated);
    	return Response.status(Status.CREATED).entity(missioneCreated).build();
    }

	private MissioneComponentSession missioneComponent() throws javax.ejb.EJBException, java.rmi.RemoteException {
		return (MissioneComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRMISSIONI00_EJB_MissioneComponentSession");
	}

	private CRUDComponentSession getComponent() throws javax.ejb.EJBException, java.rmi.RemoteException {
		return (CRUDComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("JADAEJB_CRUDComponentSession");
	}

    private NazioneBulk getNazione(UserContext userContext, Long nazione) throws PersistencyException, ComponentException, RemoteException, EJBException {
		NazioneBulk nazioneBulk = new NazioneBulk(nazione);
		nazioneBulk = (NazioneBulk)getComponent().findByPrimaryKey(userContext, nazioneBulk);
		return nazioneBulk;
	}
}