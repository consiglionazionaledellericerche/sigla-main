package it.cnr.contab.web.rest;

import it.cnr.contab.client.docamm.FatturaAttiva;
import it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_IBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_rigaBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_rigaIBulk;
import it.cnr.contab.docamm00.docs.bulk.Nota_di_credito_attivaBulk;
import it.cnr.contab.docamm00.docs.bulk.Nota_di_credito_attiva_rigaBulk;
import it.cnr.contab.docamm00.ejb.FatturaAttivaSingolaComponentSession;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.web.rest.config.AccessoAllowed;
import it.cnr.contab.web.rest.config.AccessoEnum;
import it.cnr.contab.web.rest.config.RestException;
import it.cnr.contab.web.rest.config.SIGLARoles;
import it.cnr.jada.comp.FatturaNonTrovataException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/fatturaattiva")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed(SIGLARoles.FATTURE_ATTIVE)
public class FatturaAttivaResource {
    private static final String FATTURA_ATTIVA_NON_PRESENTE = "Fattura attiva non presente.";
	private final Logger LOGGER = LoggerFactory.getLogger(FatturaAttivaResource.class);
	@Context SecurityContext securityContext;

	/**
     * GET  /restapi/fatturaattiva/ricerca -> return Fattura attiva
     */
    @GET
    @AccessoAllowed(value=AccessoEnum.AMMFATTURDOCSFATATTV)
    public Response ricercaFattura(@Context HttpServletRequest request, @QueryParam ("pg") Long pg) throws Exception {
        LOGGER.debug("REST request per ricercare una fattura attiva." );
        CNRUserContext userContext = (CNRUserContext) securityContext.getUserPrincipal();        
        try {
            Fattura_attivaBulk fatturaAttiva = fatturaComponent().ricercaFattura(userContext, userContext.getEsercizio().longValue(), 
            		userContext.getCd_cds(), userContext.getCd_unita_organizzativa(), pg);		
            return Response.ok(Optional.ofNullable(fatturaAttiva).orElseThrow(() -> new FatturaNonTrovataException(FATTURA_ATTIVA_NON_PRESENTE))).build();        	
        } catch(FatturaNonTrovataException _ex) {
        	return Response.status(Status.NOT_FOUND).entity(Collections.singletonMap("ERROR", FATTURA_ATTIVA_NON_PRESENTE)).build();
        }
	}
    
	/**
     * PUT  /restapi/fatturaattiva-> return Fattura attiva
     */
    @PUT
    @AccessoAllowed(value=AccessoEnum.AMMFATTURDOCSFATATTM)
    public Response inserisciFatture(@Context HttpServletRequest request, List<FatturaAttiva> fatture) throws Exception {
        LOGGER.debug("REST request per inserire fatture attive." );
        List<Fattura_attivaBulk> fattureCreate = new ArrayList<Fattura_attivaBulk>();
        CNRUserContext userContext = (CNRUserContext) securityContext.getUserPrincipal();
        fatture.stream().forEach(fattura -> {
     	   	Fattura_attivaBulk testata;
     	   	Fattura_attiva_rigaBulk riga;          

        	Optional.ofNullable(fattura.getEsercizio()).orElseThrow(() -> new RestException(Status.BAD_REQUEST, "Errore, Esercizio obbligatorio!"));;
        	Optional.ofNullable(fattura.getCd_cds_origine()).orElseThrow(() -> new RestException(Status.BAD_REQUEST, "Errore, Cds origine obbligatorio!"));
        	Optional.ofNullable(fattura.getCd_uo_origine()).orElseThrow(() -> new RestException(Status.BAD_REQUEST, "Errore, UO origine obbligatoria!"));
        	
        	Optional.ofNullable(fattura.getEsercizio()).filter(x -> userContext.getEsercizio().equals(x)).
    			orElseThrow(() -> new RestException(Status.BAD_REQUEST, "Esercizio del contesto diverso da quello della Fattura!"));
	    	Optional.ofNullable(fattura.getCd_cds_origine()).filter(x -> userContext.getCd_cds().equals(x)).
				orElseThrow(() -> new RestException(Status.BAD_REQUEST, "CdS del contesto diverso da quello della Fattura!"));
	    	Optional.ofNullable(fattura.getCd_uo_origine()).filter(x -> userContext.getCd_unita_organizzativa().equals(x)).
				orElseThrow(() -> new RestException(Status.BAD_REQUEST, "Unità Organizzativa del contesto diversa da quella della Fattura!"));
	    	Optional.ofNullable(fattura.getTi_causale_emissione()).filter(x -> Stream.of("C", "L", "T").anyMatch(y -> y.equals(x)))
				.orElseThrow(() -> new RestException(Status.BAD_REQUEST, "Causale emissione non valida!"));
	    	Optional.ofNullable(fattura.getTi_bene_servizio()).filter(x -> Stream.of("B", "S", "*").anyMatch(y -> y.equals(x)))
				.orElseThrow(() -> new RestException(Status.BAD_REQUEST, "Tipologia bene/servizio non valida!"));
	    	
	    	Optional.ofNullable(fattura.getTi_fattura()).filter(x -> Stream.of(
	    			Fattura_attivaBulk.TIPO_NOTA_DI_CREDITO, 
	    			Fattura_attivaBulk.TIPO_FATTURA_ATTIVA
	    		).anyMatch(y -> y.equals(x)))
			.orElseThrow(() -> new RestException(Status.BAD_REQUEST, "Tipologia fattura non valida!"));

			if(fattura.getTi_fattura().equals(Fattura_attivaBulk.TIPO_NOTA_DI_CREDITO)){
            	testata=new Nota_di_credito_attivaBulk();
            	riga = new Nota_di_credito_attiva_rigaBulk();
            } else {
            	testata=new Fattura_attiva_IBulk();
            	riga = new Fattura_attiva_rigaIBulk();
            }
        	testata.setToBeCreated();
	    	testata.setEsercizio(fattura.getEsercizio());
	    	testata.setTi_fattura(fattura.getTi_fattura());
            testata.setCd_cds_origine(fattura.getCd_cds_origine());
            testata.setTi_bene_servizio(fattura.getTi_bene_servizio());
            testata.setFl_pagamento_anticipato(fattura.getFl_pagamento_anticipato());
	    	testata.setCd_uo_origine(fattura.getCd_uo_origine());
	        testata.setPg_fattura_esterno(fattura.getPg_fattura_esterno());
	    	Fattura_attivaBulk fatturaAttiva;
	    	if (!(testata.getTi_fattura().equals(Fattura_attivaBulk.TIPO_NOTA_DI_CREDITO))){
	    		fatturaAttiva = new Fattura_attiva_IBulk();
    			fatturaAttiva.setEsercizio(testata.getEsercizio());
    			fatturaAttiva.setCd_cds_origine(testata.getCd_cds_origine());
    			fatturaAttiva.setCd_uo_origine(testata.getCd_uo_origine());
    			fatturaAttiva.setPg_fattura_esterno(testata.getPg_fattura_esterno());
	    	} else {
	    		fatturaAttiva = new Nota_di_credito_attivaBulk();
    			fatturaAttiva.setEsercizio(testata.getEsercizio());
    			fatturaAttiva.setCd_cds_origine(testata.getCd_cds_origine());
    			fatturaAttiva.setCd_uo_origine(testata.getCd_uo_origine());
    			fatturaAttiva.setPg_fattura_esterno(testata.getPg_fattura_esterno());
    			fatturaAttiva.setTi_fattura(testata.getTi_fattura());
	    	}	    	
	        testata.setFl_intra_ue(fattura.getFl_intra_ue());
	        testata.setFl_extra_ue(fattura.getFl_extra_ue());
	        testata.setFl_san_marino(fattura.getFl_san_marino());
		
	        
	        
        
        });        
        return Response.status(Status.CREATED).entity(fattureCreate).build();
	}
    
	/**
     * GET  /restapi/fatturaattiva/ricerca -> return Fattura attiva
     */
    @GET
    @Path("/print")
    @AccessoAllowed(value=AccessoEnum.AMMFATTURDOCSFATATTV)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response stampaFattura(@Context HttpServletRequest request, @QueryParam ("pg") Long pg) throws Exception {
        LOGGER.debug("REST request per stmpa una fattura attiva." );
        CNRUserContext userContext = (CNRUserContext) securityContext.getUserPrincipal();        
        try {
        	fatturaComponent().inserisciDatiPerStampaIva(userContext,  userContext.getEsercizio().longValue(), userContext.getCd_cds(), 
        			userContext.getCd_unita_organizzativa(), pg);        	
        	byte[] stampa = fatturaComponent().lanciaStampa(userContext, pg);        	
            return Response.ok().entity(stampa).build();        	
        } catch(FatturaNonTrovataException _ex) {
        	return Response.status(Status.NOT_FOUND).entity(Collections.singletonMap("ERROR", FATTURA_ATTIVA_NON_PRESENTE)).build();
        }
	}
    
	private FatturaAttivaSingolaComponentSession fatturaComponent() throws javax.ejb.EJBException, java.rmi.RemoteException {
		return (FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession");
	}

	public static void main(String[] args) {
		String ti_causale_emissione = "C";		
    	Optional.ofNullable(ti_causale_emissione).filter(x -> Stream.of("C", "L", "T").anyMatch(y -> y.equals(x)))
			.orElseThrow(() -> new RestException(Status.BAD_REQUEST, "Causale emissione obbligatoria"));
		
	}
}