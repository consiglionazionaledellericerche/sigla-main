/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package it.cnr.contab.progettiric00.action;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Optional;

import javax.ejb.RemoveException;

import it.cnr.contab.config00.bp.CRUDConfigAnagContrattoBP;
import it.cnr.contab.config00.contratto.bulk.ContrattoBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.doccont00.bp.CRUDAccertamentoBP;
import it.cnr.contab.doccont00.core.bulk.AccertamentoBulk;
import it.cnr.contab.pdg00.bp.PdGVariazioneBP;
import it.cnr.contab.pdg01.bp.CRUDPdgVariazioneGestionaleBP;
import it.cnr.contab.progettiric00.bp.RimodulaProgettiRicercaBP;
import it.cnr.contab.progettiric00.bp.RimodulaProgettoPianoEconomicoCRUDController;
import it.cnr.contab.progettiric00.bp.RimodulaProgettoPianoEconomicoVoceBilancioCRUDController;
import it.cnr.contab.progettiric00.bp.TestataProgettiRicercaBP;
import it.cnr.contab.progettiric00.core.bulk.Ass_progetto_piaeco_voceBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_piano_economicoBulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_rimodulazioneBulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_rimodulazione_variazioneBulk;
import it.cnr.contab.progettiric00.ejb.RimodulaProgettoRicercaComponentSession;
import it.cnr.contab.progettiric00.tabrif.bulk.Voce_piano_economico_prgBulk;
import it.cnr.contab.utenze00.bulk.CNRUserInfo;
import it.cnr.contab.varstanz00.bp.CRUDVar_stanz_resBP;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HookForward;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.*;
import it.cnr.jada.util.ejb.EJBCommonServices;

/**
 * Azione che gestisce le richieste relative alla Rimodulazione Gestione Progetto Risorse
 * (Progetto)
 */
public class CRUDRimodulaProgettoAction extends CRUDAbstractProgettoAction {
	private static final long serialVersionUID = 1L;

	public static class SelezionatoreRimodulazioneProgettoAction extends SelezionatoreListaAction {
		private static final long serialVersionUID = 1L;

		public SelezionatoreRimodulazioneProgettoAction() {
			super();
		}

		public Forward doCambiaVisibilita(ActionContext actioncontext)
				throws RemoteException {
			SelezionatoreListaBP bp = (SelezionatoreListaBP)actioncontext.getBusinessProcess();
			Progetto_rimodulazioneBulk bulk = (Progetto_rimodulazioneBulk)bp.getModel();
			try {
				fillModel(actioncontext);
				String statoRimodulazione = bulk.getStato();
				if (statoRimodulazione.equalsIgnoreCase(Progetto_rimodulazioneBulk.STATO_RIMODULAZIONE_TUTTI))
					bulk.setStato(null);				
				EJBCommonServices.closeRemoteIterator(actioncontext,bp.detachIterator());
				bp.setIterator(actioncontext, ((RimodulaProgettoRicercaComponentSession)
						bp.createComponentSession("CNRPROGETTIRIC00_EJB_RimodulaProgettoRicercaComponentSession", RimodulaProgettoRicercaComponentSession.class)).
						cerca(actioncontext.getUserContext(), Optional.ofNullable(bp.getCondizioneCorrente())
								.map(CondizioneComplessaBulk::creaFindClause)
								.filter(CompoundFindClause.class::isInstance)
								.map(CompoundFindClause.class::cast)
								.orElseGet(() -> new CompoundFindClause()), bulk));
				bp.refresh(actioncontext);
				bulk.setStato(statoRimodulazione);
				return actioncontext.findDefaultForward();
			} catch(Throwable e) {
				bulk.setStato(null);
				return handleException(actioncontext,e);
			}
		}
	}
	
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
	 * Gestione della richiesta di validazione di una rimodulazione definitiva
	 *
	 * @param context	L'ActionContext della richiesta
	 * @return Il Forward alla pagina di risposta
	 */
	public Forward doValida(ActionContext context) {
		try {
			fillModel(context);
			RimodulaProgettiRicercaBP bp = (RimodulaProgettiRicercaBP)getBusinessProcess(context);
			bp.completeSearchTools(context, bp);
	        bp.validate(context);
       		return openConfirm(context, "Attenzione! Si vuole procedere a validare la rimodulazione?", OptionBP.CONFIRM_YES_NO, "doConfirmValida");
		}catch(Throwable ex){
			return handleException(context, ex);
		}
	}

	public Forward doConfirmValida(ActionContext context,int option) {
		try 
		{
			if ( option == OptionBP.YES_BUTTON) 
			{
				RimodulaProgettiRicercaBP bp = (RimodulaProgettiRicercaBP)getBusinessProcess(context);
	        	bp.valida(context);
				Progetto_rimodulazioneBulk rim = (Progetto_rimodulazioneBulk)bp.getModel();
				if (rim.isStatoApprovato()) {
					return openMessage(context, "Operazione eseguita con successo! La rimodulazione è stata posta "
		        			+ "direttamente in stato approvato non essendo previste variazioni di bilancio a supporto!", 
		        			"doRiporta");
				} else
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
				setMessage(context,  it.cnr.jada.util.action.FormBP.WARNING_MESSAGE, "Operazione eseguita con successo!");
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
	
	public Forward doOnDtProrogaRimodulatoChange(ActionContext context) {
		RimodulaProgettiRicercaBP bp = (RimodulaProgettiRicercaBP)getBusinessProcess(context);
		Optional<Progetto_rimodulazioneBulk> optRimodulazione = Optional.ofNullable(bp.getModel())
				.filter(Progetto_rimodulazioneBulk.class::isInstance).map(Progetto_rimodulazioneBulk.class::cast);
				
		Optional<Timestamp> optData = optRimodulazione.flatMap(el->Optional.ofNullable(el.getDtProrogaRimodulato()));
	
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
			optRimodulazione.get().setDtProrogaRimodulato(oldDate);
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
	
	public Forward doNewVariazioneCompetenza(ActionContext context){
		try 
		{
			fillModel( context );
			RimodulaProgettiRicercaBP bp = (RimodulaProgettiRicercaBP) getBusinessProcess(context);
			bp.completeSearchTools(context, bp);
	        bp.validate(context);
        	return openConfirm(context, "Attenzione! Si vuole procedere alla creazione di una nuova variazione di competenza da associare alla "
        			+ "rimodulazione del progetto?", 
        			OptionBP.CONFIRM_YES_NO, "doConfirmdoNewVariazioneCompetenza");
		}		
		catch(Throwable e) 
		{
			return handleException(context,e);
		}
	}

	public Forward doConfirmdoNewVariazioneCompetenza(ActionContext context,int option) {
		try 
		{
			if (option == OptionBP.YES_BUTTON) {
				RimodulaProgettiRicercaBP bp= (RimodulaProgettiRicercaBP) getBusinessProcess(context);
				String function = bp.isEditable() ? "M" : "V";
				function += "R";

				Progetto_rimodulazioneBulk rimodulazione = (Progetto_rimodulazioneBulk)bp.getModel();

				CRUDPdgVariazioneGestionaleBP newbp = null;
				// controlliamo prima che abbia l'accesso al BP per dare un messaggio più preciso
				String mode = it.cnr.contab.utenze00.action.GestioneUtenteAction.getComponentSession().validaBPPerUtente(context.getUserContext(),((CNRUserInfo)context.getUserInfo()).getUtente(),((CNRUserInfo)context.getUserInfo()).getUtente().isUtenteComune() ? ((CNRUserInfo)context.getUserInfo()).getUnita_organizzativa().getCd_unita_organizzativa() : "*","CRUDPdgVariazioneGestionaleBP");
				if (mode == null) 
					throw new it.cnr.jada.action.MessageToUser("Accesso non consentito alla mappa di creazione delle variazioni di competenza. Impossibile continuare.");

				newbp = (CRUDPdgVariazioneGestionaleBP) context.getUserInfo().createBusinessProcess(context,"CRUDPdgVariazioneGestionaleBP",new Object[] { function,  rimodulazione});
				newbp.setBringBack(true);
				context.addHookForward("bringback", this, "doBringBackNewVariazione");
				return context.addBusinessProcess(newbp);
			}
		} catch(Exception e) {
			return handleException(context,e);
		}
		return context.findDefaultForward();
	}

	public Forward doNewVariazioneResidua(ActionContext context){
		try 
		{
			fillModel( context );
			RimodulaProgettiRicercaBP bp = (RimodulaProgettiRicercaBP) getBusinessProcess(context);
			bp.completeSearchTools(context, bp);
	        bp.validate(context);
        	return openConfirm(context, "Attenzione! Si vuole procedere alla creazione di una nuova variazione residua da associare alla "
        			+ "rimodulazione del progetto?", 
        			OptionBP.CONFIRM_YES_NO, "doConfirmdoNewVariazioneResidua");
		}		
		catch(Throwable e) 
		{
			return handleException(context,e);
		}
	}

	public Forward doConfirmdoNewVariazioneResidua(ActionContext context,int option) {
		try 
		{
			if (option == OptionBP.YES_BUTTON) {
				RimodulaProgettiRicercaBP bp= (RimodulaProgettiRicercaBP) getBusinessProcess(context);
				String function = bp.isEditable() ? "M" : "V";
				function += "R";

				Progetto_rimodulazioneBulk rimodulazione = (Progetto_rimodulazioneBulk)bp.getModel();

				CRUDVar_stanz_resBP newbp = null;
				// controlliamo prima che abbia l'accesso al BP per dare un messaggio più preciso
				String mode = it.cnr.contab.utenze00.action.GestioneUtenteAction.getComponentSession().validaBPPerUtente(context.getUserContext(),((CNRUserInfo)context.getUserInfo()).getUtente(),((CNRUserInfo)context.getUserInfo()).getUtente().isUtenteComune() ? ((CNRUserInfo)context.getUserInfo()).getUnita_organizzativa().getCd_unita_organizzativa() : "*","CRUDVar_stanz_resBP");
				if (mode == null) 
					throw new it.cnr.jada.action.MessageToUser("Accesso non consentito alla mappa di creazione delle variazioni residue. Impossibile continuare.");

				newbp = (CRUDVar_stanz_resBP) context.getUserInfo().createBusinessProcess(context,"CRUDVar_stanz_resBP",new Object[] { function,  rimodulazione});
				newbp.setBringBack(true);
				context.addHookForward("bringback", this, "doBringBackNewVariazione");
				return context.addBusinessProcess(newbp);
			}
		} catch(Exception e) {
			return handleException(context,e);
		}
		return context.findDefaultForward();
	}

    public Forward doBringBackNewVariazione(ActionContext context) {
        try {
        	HookForward caller = (HookForward)context.getCaller();
        	RimodulaProgettiRicercaBP bp= (RimodulaProgettiRicercaBP) getBusinessProcess(context);
            Progetto_rimodulazioneBulk rimodulazione = (Progetto_rimodulazioneBulk) bp.getModel();
            return context.findDefaultForward();
        } catch (Exception e) {
            return handleException(context, e);
        }
    }
    
	public Forward doOpenVariazione(ActionContext context, String s)
	{
		try 
		{
			fillModel( context );
			CRUDController crudController = getController(context, s);

			RimodulaProgettiRicercaBP bp= (RimodulaProgettiRicercaBP) getBusinessProcess(context);

			String function = bp.isEditable() ? "M" : "V";
			function += "R";

			SimpleCRUDBP newbp = null;

			if (Progetto_rimodulazione_variazioneBulk.TIPO_COMPETENZA.equals(((Progetto_rimodulazione_variazioneBulk)crudController.getModel()).getTipoVariazione())) {
				// controlliamo prima che abbia l'accesso al BP per dare un messaggio più preciso
				String mode = it.cnr.contab.utenze00.action.GestioneUtenteAction.getComponentSession().validaBPPerUtente(context.getUserContext(),((CNRUserInfo)context.getUserInfo()).getUtente(),((CNRUserInfo)context.getUserInfo()).getUtente().isUtenteComune() ? ((CNRUserInfo)context.getUserInfo()).getUnita_organizzativa().getCd_unita_organizzativa() : "*","CRUDPdgVariazioneGestionaleBP");
				if (mode == null) 
					throw new it.cnr.jada.action.MessageToUser("Accesso non consentito alla mappa di creazione delle variazioni di competenza. Impossibile continuare.");

				newbp = (PdGVariazioneBP) context.getUserInfo().createBusinessProcess(context,"CRUDPdgVariazioneGestionaleBP",new Object[] { function});
				OggettoBulk variazione = newbp.initializeModelForEdit(context, ((Progetto_rimodulazione_variazioneBulk)crudController.getModel()).getVariazioneCompetenza());
				newbp.setModel(context, variazione);
			} else {
				// controlliamo prima che abbia l'accesso al BP per dare un messaggio più preciso
				String mode = it.cnr.contab.utenze00.action.GestioneUtenteAction.getComponentSession().validaBPPerUtente(context.getUserContext(),((CNRUserInfo)context.getUserInfo()).getUtente(),((CNRUserInfo)context.getUserInfo()).getUtente().isUtenteComune() ? ((CNRUserInfo)context.getUserInfo()).getUnita_organizzativa().getCd_unita_organizzativa() : "*","CRUDVar_stanz_resBP");
				if (mode == null) 
					throw new it.cnr.jada.action.MessageToUser("Accesso non consentito alla mappa di creazione delle variazioni residue. Impossibile continuare.");

				newbp = (CRUDVar_stanz_resBP) context.getUserInfo().createBusinessProcess(context,"CRUDVar_stanz_resBP",new Object[] { function });
				OggettoBulk variazione = newbp.initializeModelForEdit(context, ((Progetto_rimodulazione_variazioneBulk)crudController.getModel()).getVariazioneResiduo());
				newbp.setModel(context, variazione);
			}
			return context.addBusinessProcess(newbp);
		} catch(Exception e) {
			return handleException(context,e);
		}
	}
	
	public Forward doRiporta(ActionContext context,int option) {
		return doRiporta(context);
	}
	
	@Override
	public Forward doCerca(ActionContext actioncontext) throws RemoteException,
			InstantiationException, RemoveException {
		try {

			RimodulaProgettiRicercaBP rimodulaProgettiRicercaBP = (RimodulaProgettiRicercaBP) actioncontext.getBusinessProcess();
			Progetto_rimodulazioneBulk bulk = (Progetto_rimodulazioneBulk) rimodulaProgettiRicercaBP.getModel();
			fillModel(actioncontext);
	        RemoteIterator remoteiterator = rimodulaProgettiRicercaBP.find(actioncontext, null, bulk);
	        if(remoteiterator == null || remoteiterator.countElements() == 0)
	        {
	            EJBCommonServices.closeRemoteIterator(actioncontext,remoteiterator);
	            rimodulaProgettiRicercaBP.setMessage("La ricerca non ha fornito alcun risultato.");
	            return actioncontext.findDefaultForward();
	        }
	        if(remoteiterator.countElements() == 1)
	        {
	            OggettoBulk oggettobulk1 = (OggettoBulk)remoteiterator.nextElement();
	            EJBCommonServices.closeRemoteIterator(actioncontext,remoteiterator);
	            rimodulaProgettiRicercaBP.setMessage(FormBP.INFO_MESSAGE,"La ricerca ha fornito un solo risultato.");
	            return doRiportaSelezione(actioncontext, oggettobulk1);
	        } else
	        {
	        	rimodulaProgettiRicercaBP.setModel(actioncontext, bulk);
	            SelezionatoreListaBP selezionatorelistabp = (SelezionatoreListaBP)actioncontext.createBusinessProcess("SelezionatoreRimodulazioneProgettoBP");
	            selezionatorelistabp.setModel(actioncontext, bulk);
	            selezionatorelistabp.setIterator(actioncontext, remoteiterator);
	            selezionatorelistabp.setBulkInfo(rimodulaProgettiRicercaBP.getSearchBulkInfo());
	            selezionatorelistabp.setColumns(getBusinessProcess(actioncontext).getSearchResultColumns());
	            actioncontext.addHookForward("seleziona", this, "doRiportaSelezione");
	            return actioncontext.addBusinessProcess(selezionatorelistabp);
	        }
		} catch (Exception e) {
			return handleException(actioncontext, e);
		}	
	}

	/**
	 * Gestione della richiesta di riportare in definitivo una rimodulazione validata/respinta
	 *
	 * @param context	L'ActionContext della richiesta
	 * @return Il Forward alla pagina di risposta
	 */
	public Forward doRiportaDefinitivo(ActionContext context) {
		try {
			fillModel(context);
			RimodulaProgettiRicercaBP bp = (RimodulaProgettiRicercaBP)getBusinessProcess(context);
			bp.completeSearchTools(context, bp);
			bp.validate(context);
			return openConfirm(context, "Attenzione! Si vuole procedere a riassegnare lo stato 'Definitivo' alla rimodulazione?", OptionBP.CONFIRM_YES_NO, "doConfirmRiportaDefinitivo");
		}catch(Throwable ex){
			return handleException(context, ex);
		}
	}

	public Forward doConfirmRiportaDefinitivo(ActionContext context,int option) {
		try
		{
			if ( option == OptionBP.YES_BUTTON)
			{
				RimodulaProgettiRicercaBP bp = (RimodulaProgettiRicercaBP)getBusinessProcess(context);
				bp.riportaDefinitivo(context);
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
	 * Gestione della richiesta di riportare in provvisorio una rimodulazione definitiva
	 *
	 * @param context	L'ActionContext della richiesta
	 * @return Il Forward alla pagina di risposta
	 */
	public Forward doRiportaProvvisorio(ActionContext context) {
		try {
			fillModel(context);
			RimodulaProgettiRicercaBP bp = (RimodulaProgettiRicercaBP)getBusinessProcess(context);
			bp.completeSearchTools(context, bp);
			bp.validate(context);
			return openConfirm(context, "Attenzione! Si vuole procedere a riassegnare lo stato 'Provvisorio' alla rimodulazione?", OptionBP.CONFIRM_YES_NO, "doConfirmRiportaProvvisorio");
		}catch(Throwable ex){
			return handleException(context, ex);
		}
	}

	public Forward doConfirmRiportaProvvisorio(ActionContext context,int option) {
		try
		{
			if ( option == OptionBP.YES_BUTTON)
			{
				RimodulaProgettiRicercaBP bp = (RimodulaProgettiRicercaBP)getBusinessProcess(context);
				bp.riportaProvvisorio(context);
				setMessage(context,  it.cnr.jada.util.action.FormBP.WARNING_MESSAGE, "Operazione eseguita con successo");
			}
			return context.findDefaultForward();
		}
		catch(Throwable e)
		{
			return handleException(context,e);
		}
	}

	public Forward doOnEsercizioPianoChange(ActionContext actioncontext) {
		try {
			fillModel(actioncontext);
			RimodulaProgettiRicercaBP bp = (RimodulaProgettiRicercaBP)getBusinessProcess(actioncontext);
			Optional<Progetto_piano_economicoBulk> optPpe = Optional.ofNullable(bp.getCrudPianoEconomicoAltriAnni())
					.flatMap(el->Optional.ofNullable(el.getModel()))
					.filter(Progetto_piano_economicoBulk.class::isInstance)
					.map(Progetto_piano_economicoBulk.class::cast);

			if (optPpe.isPresent() && optPpe.map(Progetto_piano_economicoBulk::isAnnoPianoEconomicoMinoreAnnoInizio).orElse(Boolean.FALSE))
				optPpe.get().setImSpesaFinanziatoRimodulato(BigDecimal.ZERO);
			return actioncontext.findDefaultForward();
		}
		catch(Throwable throwable)
		{
			return handleException(actioncontext, throwable);
		}
	}

	public Forward doConsultaProgetto(ActionContext context) {
		try {
			fillModel(context);
			RimodulaProgettiRicercaBP bp = (RimodulaProgettiRicercaBP)getBusinessProcess(context);
			bp.completeSearchTools(context, bp);
			bp.validate(context);
			Progetto_rimodulazioneBulk bulk = (Progetto_rimodulazioneBulk) bp.getModel();
			try {
				TestataProgettiRicercaBP crudbp = (TestataProgettiRicercaBP)context.getUserInfo().createBusinessProcess(context, "TestataProgettiRicercaBP", new Object[]{bp.isEditable() ? "MR" : "R"});
				crudbp.setModel(context, crudbp.initializeModelForEdit(context, bulk.getProgetto()));
				crudbp.setStatus(FormController.VIEW);
				return context.addBusinessProcess(crudbp);
			} catch (Throwable e) {
				return handleException(context, e);
			}
		}catch(Throwable ex){
			return handleException(context, ex);
		}
	}
}