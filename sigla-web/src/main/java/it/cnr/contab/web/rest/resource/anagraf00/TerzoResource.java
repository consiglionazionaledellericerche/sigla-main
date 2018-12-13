package it.cnr.contab.web.rest.resource.anagraf00;

import java.rmi.RemoteException;
import java.util.Optional;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.cnr.contab.anagraf00.core.bulk.TelefonoBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.anagraf00.ejb.TerzoComponentSession;
import it.cnr.contab.config00.ejb.Unita_organizzativaComponentSession;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.web.rest.exception.RestException;
import it.cnr.contab.web.rest.local.anagraf00.TerzoLocal;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.ejb.CRUDComponentSession;
import it.cnr.jada.persistency.PersistencyException;

@Stateless
public class TerzoResource implements TerzoLocal {
    private final Logger LOGGER = LoggerFactory.getLogger(TerzoResource.class);
	@Context SecurityContext securityContext;
	@EJB CRUDComponentSession crudComponentSession;
	@EJB TerzoComponentSession terzoComponentSession;
	@EJB Unita_organizzativaComponentSession unita_organizzativaComponentSession;
	
    public Response update(@Context HttpServletRequest request, TerzoBulk terzoBulk) throws Exception {
    	CNRUserContext userContext = (CNRUserContext) securityContext.getUserPrincipal();
		Optional.ofNullable(terzoBulk.getCd_terzo()).orElseThrow(() -> new RestException(Status.BAD_REQUEST, "Errore, indicare il codice terzo."));
    	
    	TerzoBulk terzoDB = getTerzo(userContext, terzoBulk.getCd_terzo());
		Optional.ofNullable(terzoDB).orElseThrow(() -> new RestException(Status.BAD_REQUEST, "Errore, il codice terzo indicato "+terzoBulk.getCd_terzo()+" non esiste"));
		Optional.ofNullable(terzoBulk.getCodiceDestinatarioFatt()).orElseGet(() -> Optional.ofNullable(terzoBulk.getPecForRest()).orElseThrow(() -> new RestException(Status.BAD_REQUEST, "Errore, indicare almeno la pec o il codice destinatario fattura.")));
    	
    	terzoDB = (TerzoBulk)terzoComponentSession.inizializzaBulkPerModifica(userContext, terzoDB);
    	terzoDB.setCodiceDestinatarioFatt(Optional.ofNullable(terzoBulk.getCodiceDestinatarioFatt()).orElse(terzoDB.getCodiceDestinatarioFatt()));
    	if (terzoBulk.getPecForRest() != null && !terzoBulk.getPecForRest().isEmpty()){
    		String newPec = terzoBulk.getPecForRest();
    		Boolean pecEsistente = false;
    		for (java.util.Iterator i = terzoDB.getPec().iterator(); i.hasNext();){
    			TelefonoBulk emailPec = (TelefonoBulk)i.next();
    			if (emailPec.getRiferimento().equalsIgnoreCase(newPec)){
    				pecEsistente = true;
    				emailPec.setFattElettronica(true);
    				emailPec.setToBeUpdated();
    			} else if (emailPec.getFattElettronica()){
    				emailPec.setFattElettronica(false);
    				emailPec.setToBeUpdated();
    			}
    		}    		
    		
    		if (!pecEsistente){
        		TelefonoBulk telefonoBulk = new TelefonoBulk();
        		telefonoBulk.setToBeCreated();
        		telefonoBulk.setRiferimento(terzoBulk.getPecForRest());
        		telefonoBulk.setDs_riferimento("INDIRIZZO PEC PER FATTURAZIONE ELETTRONICA");
        		telefonoBulk.setFattElettronica(true);
        		terzoDB.addToPec(telefonoBulk);
    		}
    	}
    	terzoDB.setToBeUpdated();
    	
    	terzoDB = (TerzoBulk)terzoComponentSession.modificaConBulk(userContext, terzoDB);
    	return Response.status(Status.OK).entity(terzoBulk).build();
    }

    private TerzoBulk getTerzo(UserContext userContext, Integer cdTerzo) throws PersistencyException, ComponentException, RemoteException, EJBException {
		TerzoBulk terzoBulk = new TerzoBulk();
		terzoBulk.setCd_terzo(cdTerzo);
		terzoBulk = (TerzoBulk)crudComponentSession.findByPrimaryKey(userContext, terzoBulk);
		return terzoBulk;
	}

}