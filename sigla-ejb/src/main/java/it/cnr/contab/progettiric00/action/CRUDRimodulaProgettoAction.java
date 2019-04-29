package it.cnr.contab.progettiric00.action;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Optional;

import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.doccont00.bp.CRUDAccertamentoBP;
import it.cnr.contab.doccont00.bp.CRUDAccertamentoResiduoAmministraBP;
import it.cnr.contab.doccont00.core.bulk.AccertamentoResiduoBulk;
import it.cnr.contab.incarichi00.bp.CRUDIncarichiProceduraBP;
import it.cnr.contab.incarichi00.bulk.Incarichi_repertorioBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_repertorio_varBulk;
import it.cnr.contab.progettiric00.bp.RimodulaProgettiRicercaBP;
import it.cnr.contab.progettiric00.bp.RimodulaProgettoPianoEconomicoCRUDController;
import it.cnr.contab.progettiric00.bp.RimodulaProgettoPianoEconomicoVoceBilancioCRUDController;
import it.cnr.contab.progettiric00.core.bulk.Ass_progetto_piaeco_voceBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_piano_economicoBulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_rimodulazioneBulk;
import it.cnr.contab.progettiric00.tabrif.bulk.Voce_piano_economico_prgBulk;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.util.action.CRUDController;
import it.cnr.jada.util.action.OptionBP;

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
					bp.rebuildRimodulazione(context, rimodulazione);
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
			bp.completeSearchTools(context, bp);
	        bp.validate(context);
       		return openConfirm(context, "Attenzione! Si vuole procedere ad approvare la rimodulazione?", OptionBP.CONFIRM_YES_NO, "doConfirmApprova");
		}catch(Throwable ex){
			return handleException(context, ex);
		}
	}

	public Forward doConfirmApprova(ActionContext context,int option) {
		try 
		{
			if ( option == OptionBP.YES_BUTTON) 
			{
				RimodulaProgettiRicercaBP bp = (RimodulaProgettiRicercaBP)getBusinessProcess(context);
	        	bp.approva(context);
				setMessage(context,  it.cnr.jada.util.action.FormBP.WARNING_MESSAGE, "Operazione eseguita con successo");
			}
			return context.findDefaultForward();
		}		
		catch(Throwable e) 
		{
			return handleException(context,e);
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
			bp.completeSearchTools(context, bp);
	        bp.validate(context);
       		return openConfirm(context, "Attenzione! Si vuole procedere a respingere la rimodulazione?", OptionBP.CONFIRM_YES_NO, "doConfirmRespingi");
		}catch(Throwable ex){
			return handleException(context, ex);
		}
	}

	public Forward doConfirmRespingi(ActionContext context,int option) {
		try 
		{
			if ( option == OptionBP.YES_BUTTON) 
			{
				RimodulaProgettiRicercaBP bp = (RimodulaProgettiRicercaBP)getBusinessProcess(context);
	        	bp.respingi(context);
				setMessage(context,  it.cnr.jada.util.action.FormBP.WARNING_MESSAGE, "Operazione eseguita con successo");
			}
			return context.findDefaultForward();
		}		
		catch(Throwable e) 
		{
			return handleException(context,e);
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
			bp.completeSearchTools(context, bp);
	        bp.validate(context);
       		return openConfirm(context, "Attenzione! Si vuole procedere a rendere definitiva la rimodulazione?", OptionBP.CONFIRM_YES_NO, "doConfirmSalvaDefinitivo");
		}catch(Throwable ex){
			return handleException(context, ex);
		}
	}

	public Forward doConfirmSalvaDefinitivo(ActionContext context,int option) {
		try 
		{
			if ( option == OptionBP.YES_BUTTON) 
			{
				RimodulaProgettiRicercaBP bp = (RimodulaProgettiRicercaBP)getBusinessProcess(context);
	        	bp.salvaDefinitivo(context);
				setMessage(context,  it.cnr.jada.util.action.FormBP.WARNING_MESSAGE, "Operazione eseguita con successo");
			}
			return context.findDefaultForward();
		}		
		catch(Throwable e) 
		{
			return handleException(context,e);
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
	
	public Forward doOnImportoFinanziatoPpeACChange(ActionContext context) {
		RimodulaProgettiRicercaBP bp = (RimodulaProgettiRicercaBP)getBusinessProcess(context);
		Optional<Progetto_piano_economicoBulk> optPpe = Optional.ofNullable(bp.getCrudPianoEconomicoAnnoCorrente().getModel())
				.filter(Progetto_piano_economicoBulk.class::isInstance).map(Progetto_piano_economicoBulk.class::cast);
		return doOnImportoFinanziatoPpeChange(context, optPpe);
	}
	
	public Forward doOnImportoFinanziatoPpeAAChange(ActionContext context) {
		RimodulaProgettiRicercaBP bp = (RimodulaProgettiRicercaBP)getBusinessProcess(context);
		Optional<Progetto_piano_economicoBulk> optPpe = Optional.ofNullable(bp.getCrudPianoEconomicoAltriAnni().getModel())
				.filter(Progetto_piano_economicoBulk.class::isInstance).map(Progetto_piano_economicoBulk.class::cast);
		return doOnImportoFinanziatoPpeChange(context, optPpe);
	}
	
	public Forward doOnImportoCofinanziatoPpeACChange(ActionContext context) {
		RimodulaProgettiRicercaBP bp = (RimodulaProgettiRicercaBP)getBusinessProcess(context);
		Optional<Progetto_piano_economicoBulk> optPpe = Optional.ofNullable(bp.getCrudPianoEconomicoAnnoCorrente().getModel())
				.filter(Progetto_piano_economicoBulk.class::isInstance).map(Progetto_piano_economicoBulk.class::cast);
		return doOnImportoCofinanziatoPpeChange(context, optPpe);
	}
	
	public Forward doOnImportoCofinanziatoPpeAAChange(ActionContext context) {
		RimodulaProgettiRicercaBP bp = (RimodulaProgettiRicercaBP)getBusinessProcess(context);
		Optional<Progetto_piano_economicoBulk> optPpe = Optional.ofNullable(bp.getCrudPianoEconomicoAltriAnni().getModel())
				.filter(Progetto_piano_economicoBulk.class::isInstance).map(Progetto_piano_economicoBulk.class::cast);
		return doOnImportoCofinanziatoPpeChange(context, optPpe);
	}
	
	private Forward doOnImportoFinanziatoPpeChange(ActionContext context, Optional<Progetto_piano_economicoBulk> optPpe) {
		try{
			RimodulaProgettiRicercaBP bp = (RimodulaProgettiRicercaBP)getBusinessProcess(context);

			java.math.BigDecimal oldValue=null;
			if (optPpe.isPresent())
				oldValue = optPpe.get().getImSpesaFinanziatoRimodulato();

			fillModel(context);
			try {
				bp.validaImportoFinanziatoRimodulato(context, optPpe);
			} catch (ValidationException e){
				optPpe.get().setImSpesaFinanziatoRimodulato(oldValue);
				return handleException(context,e);
			}
		} catch (it.cnr.jada.bulk.FillException e){
			return handleException(context,e);
		}
	
		return context.findDefaultForward();
	}
	
	private Forward doOnImportoCofinanziatoPpeChange(ActionContext context, Optional<Progetto_piano_economicoBulk> optPpe) {
		try{
			RimodulaProgettiRicercaBP bp = (RimodulaProgettiRicercaBP)getBusinessProcess(context);

			java.math.BigDecimal oldValue=null;
			if (optPpe.isPresent())
				oldValue = optPpe.get().getImSpesaCofinanziatoRimodulato();

			fillModel(context);
			try {
				bp.validaImportoCofinanziatoRimodulato(context, optPpe);
			} catch (ValidationException e){
				optPpe.get().setImSpesaCofinanziatoRimodulato(oldValue);
				return handleException(context,e);
			}
		} catch (it.cnr.jada.bulk.FillException e){
			return handleException(context,e);
		}
	
		return context.findDefaultForward();
	}		
}