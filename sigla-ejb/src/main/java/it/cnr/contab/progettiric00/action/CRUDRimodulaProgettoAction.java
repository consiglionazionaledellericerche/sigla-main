package it.cnr.contab.progettiric00.action;

import java.sql.Timestamp;
import java.util.Optional;

import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.progettiric00.bp.RimodulaProgettiRicercaBP;
import it.cnr.contab.progettiric00.bp.RimodulaProgettoPianoEconomicoCRUDController;
import it.cnr.contab.progettiric00.bp.RimodulaProgettoPianoEconomicoVoceBilancioCRUDController;
import it.cnr.contab.progettiric00.core.bulk.Ass_progetto_piaeco_voceBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_piano_economicoBulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_rimodulazioneBulk;
import it.cnr.contab.progettiric00.tabrif.bulk.Voce_piano_economico_prgBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.util.action.CRUDController;

/**
 * Azione che gestisce le richieste relative alla Rimodulazione Gestione Progetto Risorse
 * (Progetto)
 */
public class CRUDRimodulaProgettoAction extends CRUDAbstractProgettoAction {

    public CRUDRimodulaProgettoAction() {
        super();
    }

	public Forward doBringBackSearchFind_progetto(ActionContext context, Progetto_rimodulazioneBulk rimodulazione, ProgettoBulk progetto) {
		try {
			fillModel(context);
			if (Optional.ofNullable(progetto).isPresent()) {
				RimodulaProgettiRicercaBP bp = (RimodulaProgettiRicercaBP)getBusinessProcess(context);
				rimodulazione.setProgetto(progetto);
				if (!bp.isSearching()) {
					bp.initializeProgetto(context, rimodulazione);
					bp.setDirty(true);
				}
			}
			return context.findDefaultForward();
		}catch (Exception ex) {
			return handleException(context, ex);
		}
	}

    public it.cnr.jada.action.Forward doBringBackSearchVoce_piano(ActionContext context, Progetto_piano_economicoBulk progettoPiaeco, Voce_piano_economico_prgBulk vocePiaeco) throws java.rmi.RemoteException {
    	try {
    		RimodulaProgettiRicercaBP bp = (RimodulaProgettiRicercaBP) getBusinessProcess(context);
	        progettoPiaeco.setVoce_piano_economico(vocePiaeco);
	        bp.caricaVociPianoEconomicoAssociate(context,progettoPiaeco);
	        return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }	        
    }
    
	public Forward doBringBackSearchElemento_voce(ActionContext context, Ass_progetto_piaeco_voceBulk assProgettoPiaecoVoce, Elemento_voceBulk elementoVoce) {
		try {
			fillModel(context);
			if (Optional.ofNullable(elementoVoce).isPresent()) {
				assProgettoPiaecoVoce.setElemento_voce(elementoVoce);
				RimodulaProgettiRicercaBP bp = (RimodulaProgettiRicercaBP)getBusinessProcess(context);
				bp.initializeAssProgettoPiaecoVoce(context, assProgettoPiaecoVoce);
				bp.setDirty(true);
			}
			return context.findDefaultForward();
		}catch (Exception ex) {
			return handleException(context, ex);
		}
	}
	
	public Forward doUndoRemoveFromCRUD(ActionContext actioncontext, String s)
	{
		try 
		{
			fillModel( actioncontext );
			CRUDController crudController = getController(actioncontext, s);
			if (crudController instanceof RimodulaProgettoPianoEconomicoCRUDController) 
				((RimodulaProgettoPianoEconomicoCRUDController)crudController).undoRemove(actioncontext);
			else if (crudController instanceof RimodulaProgettoPianoEconomicoVoceBilancioCRUDController) 
				((RimodulaProgettoPianoEconomicoVoceBilancioCRUDController)crudController).undoRemove(actioncontext);
			return actioncontext.findDefaultForward();
		} catch(Exception e) {
			return handleException(actioncontext,e);
		}
	}
	
	/**
	 * Gestione della richiesta di approvazione una rimodulazione definitiva
	 *
	 * @param context	L'ActionContext della richiesta
	 * @return Il Forward alla pagina di risposta
	 */
	public Forward doApprova(ActionContext context) {
		try {
			fillModel(context);
			RimodulaProgettiRicercaBP bp = (RimodulaProgettiRicercaBP)getBusinessProcess(context);
			bp.approva(context);
			setMessage(context,  it.cnr.jada.util.action.FormBP.WARNING_MESSAGE, "Operazione eseguita con successo");
			return context.findDefaultForward();
		}catch(Throwable ex){
			return handleException(context, ex);
		}
	}
	
	/**
	 * Gestione della richiesta di respingere una rimodulazione definitiva
	 *
	 * @param context	L'ActionContext della richiesta
	 * @return Il Forward alla pagina di risposta
	 */
	public Forward doRespingi(ActionContext context) {
		try {
			fillModel(context);
			RimodulaProgettiRicercaBP bp = (RimodulaProgettiRicercaBP)getBusinessProcess(context);
			bp.respingi(context);
			setMessage(context,  it.cnr.jada.util.action.FormBP.WARNING_MESSAGE, "Operazione eseguita con successo");
			return context.findDefaultForward();
		}catch(Throwable ex){
			return handleException(context, ex);
		}
	}
	
	/**
	 * Gestione della richiesta di salvataggio di una rimodulazione come definitiva
	 *
	 * @param context	L'ActionContext della richiesta
	 * @return Il Forward alla pagina di risposta
	 */
	public Forward doSalvaDefinitivo(ActionContext context) {
		try {
			fillModel(context);
			RimodulaProgettiRicercaBP bp = (RimodulaProgettiRicercaBP)getBusinessProcess(context);
			bp.salvaDefinitivo(context);
			setMessage(context,  it.cnr.jada.util.action.FormBP.WARNING_MESSAGE, "Operazione eseguita con successo");
			return context.findDefaultForward();
		}catch(Throwable ex){
			return handleException(context, ex);
		}
	}


	public Forward doOnDtInizioRimodulatoChange(ActionContext context) {
		RimodulaProgettiRicercaBP bp = (RimodulaProgettiRicercaBP)getBusinessProcess(context);
		Optional<Progetto_rimodulazioneBulk> optRimodulazione = Optional.ofNullable(bp.getModel())
				.filter(Progetto_rimodulazioneBulk.class::isInstance).map(Progetto_rimodulazioneBulk.class::cast);
				
		Optional<Timestamp> optData = optRimodulazione.flatMap(el->Optional.ofNullable(el.getDtInizioRimodulato()));
	
		java.sql.Timestamp oldDate=null;
		if (optData.isPresent())
			oldDate = (java.sql.Timestamp)optData.get().clone();
	
		try {
			fillModel(context);
			if (optRimodulazione.isPresent())
				optRimodulazione.get().validaDateRimodulazione();
			return context.findDefaultForward();
		}
		catch (Throwable ex) {
			// In caso di errore ripropongo la data precedente
			optRimodulazione.get().setDtInizioRimodulato(oldDate);
			try
			{
				return handleException(context, ex);			
			}
			catch (Throwable e) 
			{
				return handleException(context, e);
			}
		}
	}

	public Forward doOnDtFineRimodulatoChange(ActionContext context) {
		RimodulaProgettiRicercaBP bp = (RimodulaProgettiRicercaBP)getBusinessProcess(context);
		Optional<Progetto_rimodulazioneBulk> optRimodulazione = Optional.ofNullable(bp.getModel())
				.filter(Progetto_rimodulazioneBulk.class::isInstance).map(Progetto_rimodulazioneBulk.class::cast);
				
		Optional<Timestamp> optData = optRimodulazione.flatMap(el->Optional.ofNullable(el.getDtFineRimodulato()));

		java.sql.Timestamp oldDate=null;
		if (optData.isPresent())
			oldDate = (java.sql.Timestamp)optData.get().clone();
	
		try {
			fillModel(context);
			if (optRimodulazione.isPresent())
				optRimodulazione.get().validaDateRimodulazione();
			return context.findDefaultForward();
		}
		catch (Throwable ex) {
			// In caso di errore ripropongo la data precedente
			optRimodulazione.get().setDtFineRimodulato(oldDate);
			try
			{
				return handleException(context, ex);			
			}
			catch (Throwable e) 
			{
				return handleException(context, e);
			}
		}
	}
}