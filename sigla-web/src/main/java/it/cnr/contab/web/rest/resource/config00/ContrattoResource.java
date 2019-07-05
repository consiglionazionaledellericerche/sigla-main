package it.cnr.contab.web.rest.resource.config00;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.cnr.contab.config00.contratto.bulk.Ass_contratto_ditteBulk;
import it.cnr.contab.config00.contratto.bulk.Ass_contratto_uoBulk;
import it.cnr.contab.config00.contratto.bulk.ContrattoBulk;
import it.cnr.contab.config00.contratto.model.DittaInvitataExt;
import it.cnr.contab.config00.contratto.model.UoAbilitataExt;
import it.cnr.contab.config00.ejb.ContrattoComponentSession;
import it.cnr.contab.config00.ejb.Unita_organizzativaComponentSession;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.web.rest.exception.RestException;
import it.cnr.contab.web.rest.local.config00.ContrattoLocal;
import it.cnr.jada.ejb.CRUDComponentSession;

@Stateless
public class ContrattoResource implements ContrattoLocal {
    private final Logger LOGGER = LoggerFactory.getLogger(ContrattoResource.class);
	@Context SecurityContext securityContext;
	@EJB CRUDComponentSession crudComponentSession;
	@EJB ContrattoComponentSession contrattoComponentSession;
	@EJB Unita_organizzativaComponentSession unita_organizzativaComponentSession;
	
    public Response insert(@Context HttpServletRequest request, ContrattoBulk contrattoBulk) throws Exception {
    	CNRUserContext userContext = (CNRUserContext) securityContext.getUserPrincipal();
    	Optional.ofNullable(contrattoBulk.getEsercizio()).filter(x -> userContext.getEsercizio().equals(x)).
		orElseThrow(() -> new RestException(Status.BAD_REQUEST, "Esercizio del contesto diverso da quello della Missione"));
		Optional.ofNullable(contrattoBulk.getEsercizio()).orElse(getYearFromToday());
		contrattoBulk.setStato(ContrattoBulk.STATO_PROVVISORIO);
		if (contrattoBulk.getCd_unita_organizzativa() != null){
			if (contrattoBulk.getCd_unita_organizzativa().length() == 6){
				contrattoBulk.setCd_unita_organizzativa(contrattoBulk.getCd_unita_organizzativa().substring(0, 3)+"."+contrattoBulk.getCd_unita_organizzativa().substring(3));
			} else {
				throw new RestException(Status.BAD_REQUEST, "L'Unita Organizzativa indicata "+contrattoBulk.getCd_unita_organizzativa()+" non è conforme con il formato atteso");
			}
		} else {
			throw new RestException(Status.BAD_REQUEST, "Unita Organizzativa non indicata");
		}
		
		contrattoBulk.setNatura_contabile(ContrattoBulk.NATURA_CONTABILE_PASSIVO);
		contrattoBulk.setCd_tipo_atto("DET");
		if (contrattoBulk.getListaDitteInvitateExt() != null && !contrattoBulk.getListaDitteInvitateExt().isEmpty()){
			for (DittaInvitataExt ditta : contrattoBulk.getListaDitteInvitateExt()){
				Ass_contratto_ditteBulk dittaContr = new Ass_contratto_ditteBulk();
				if (ditta.getDittaExtraUE() != null && ditta.getDittaExtraUE().equals("NO")){
					dittaContr.setCodice_fiscale(ditta.getpIvaCodiceFiscaleDittaInvitata());
				} else {
					dittaContr.setId_fiscale(ditta.getpIvaCodiceFiscaleDittaInvitata());
				}
				dittaContr.setTipologia(Ass_contratto_ditteBulk.LISTA_INVITATE);
				dittaContr.setUser(contrattoBulk.getUser());
				dittaContr.setDenominazione(ditta.getRagioneSocialeDittaInvitata());
				contrattoBulk.addToDitteInvitate(dittaContr);
			}
		}
		if (contrattoBulk.getListaUoAbilitateExt() != null && !contrattoBulk.getListaUoAbilitateExt().isEmpty()){
			for (UoAbilitataExt uoExt : contrattoBulk.getListaUoAbilitateExt()){
				Ass_contratto_uoBulk uo = new Ass_contratto_uoBulk();
				if (uoExt.getUo() != null){
					if (uoExt.getUo().length() == 6){
						uo.setCd_unita_organizzativa(uoExt.getUo().substring(0, 3)+"."+uoExt.getUo().substring(3));
						uo.setContratto(contrattoBulk);
						uo.setEsercizio(contrattoBulk.getEsercizio());
						uo.setStato_contratto(contrattoBulk.getStato());
						contrattoBulk.addToAssociazioneUO(uo);
					} else {
						throw new RestException(Status.BAD_REQUEST, "L'Unita Organizzativa indicata "+uoExt.getUo()+" non è conforme con il formato atteso");
					}
				} else {
					throw new RestException(Status.BAD_REQUEST, "Unita Organizzativa non indicata");
				}
			}
		}

		final ContrattoBulk contratto = (ContrattoBulk) contrattoComponentSession.inizializzaBulkPerInserimento(
    			userContext, 
    			contrattoBulk);
				
    	contratto.setToBeCreated();
    	ContrattoBulk contrattoCreated = (ContrattoBulk) contrattoComponentSession.creaContrattoDaFlussoAcquisti(userContext, contratto);
    	return Response.status(Status.CREATED).build();
    }
    private Date getDateTodayWithoutTime(){
    	Calendar cal = getTodayWithoutTime();
    	return cal.getTime();	
    }
    private Calendar getTodayWithoutTime(){
		Calendar cal = Calendar.getInstance();
    	cal.set(Calendar.HOUR_OF_DAY, 0);
    	cal.set(Calendar.MINUTE, 0);
    	cal.set(Calendar.SECOND, 0);
    	cal.set(Calendar.MILLISECOND, 0);
    	return cal;	
    }

    private Integer getYearFromToday() {
		Calendar cal = getTodayWithoutTime();
		return cal.get(Calendar.YEAR);
	}

}