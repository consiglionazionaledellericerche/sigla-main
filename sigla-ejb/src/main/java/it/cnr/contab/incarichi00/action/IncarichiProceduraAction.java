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

package it.cnr.contab.incarichi00.action;

import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.compensi00.docs.bulk.V_terzo_per_compensoBulk;
import it.cnr.contab.config00.contratto.bulk.Procedure_amministrativeBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.incarichi00.bp.CRUDIncarichiProceduraBP;
import it.cnr.contab.incarichi00.bp.Incarichi_archivioCRUDController;
import it.cnr.contab.incarichi00.bulk.Ass_incarico_uoBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_archivioBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_proceduraBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_repertorioBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_repertorio_rappBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_repertorio_varBulk;
import it.cnr.contab.incarichi00.ejb.IncarichiProceduraComponentSession;
import it.cnr.contab.incarichi00.tabrif.bulk.Incarichi_parametriBulk;
import it.cnr.contab.incarichi00.tabrif.bulk.Tipo_incaricoBulk;
import it.cnr.contab.incarichi00.tabrif.bulk.Tipo_prestazioneBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.bulk.FillException;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.util.DateUtils;
import it.cnr.jada.util.action.CRUDBP;
import it.cnr.jada.util.action.OptionBP;
import it.cnr.jada.util.ejb.EJBCommonServices;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IncarichiProceduraAction extends it.cnr.jada.util.action.CRUDAction{
	private transient static final Logger logger = LoggerFactory.getLogger(IncarichiProceduraAction.class);
	public IncarichiProceduraAction() {
		super();
	}
	public Forward doBringBackSearchTerzo(ActionContext context, Incarichi_repertorioBulk incarico, V_terzo_per_compensoBulk terzo) {
		try {
			if(terzo != null) {
				CRUDIncarichiProceduraBP bp = (CRUDIncarichiProceduraBP)getBusinessProcess(context);
				Incarichi_proceduraBulk procedura = incarico.getIncarichi_procedura();
				procedura.getIncarichi_repertorioColl().set(procedura.getIncarichi_repertorioColl().indexOf(incarico), bp.initializeTerzo(context, incarico, terzo));
				bp.getIncarichiColl().resync(context);
			}
			return context.findDefaultForward();
		} catch(BusinessProcessException e) {
			return handleException(context,e);
		}
	}
	public Forward doBringBackSearchTerzoSearch(ActionContext context, Incarichi_repertorioBulk incarico, V_terzo_per_compensoBulk terzo) {
		return doBringBackSearchTerzo(context,incarico,terzo);
	}
	public Forward doBlankSearchTerzo(ActionContext context, Incarichi_repertorioBulk incarico) {
	
		if (incarico!=null){
			V_terzo_per_compensoBulk v_terzo = new V_terzo_per_compensoBulk();
			v_terzo.setTerzo(new TerzoBulk());
			
			incarico.setV_terzo(v_terzo);
			incarico.setCd_terzo(null);
	
			incarico.setTipiRapporto(null);
			incarico.setTipo_rapporto(null);
		}
		return context.findDefaultForward();
	}
	public Forward doBlankSearchTerzoSearch(ActionContext context, Incarichi_repertorioBulk incarico) {
		return doBlankSearchTerzo(context, incarico);
	}
	public Forward doBringBackSearchUnita_organizzativa(ActionContext context, Incarichi_proceduraBulk procedura, Unita_organizzativaBulk uo) 
	{
		try 
		{
			CRUDIncarichiProceduraBP bp = (CRUDIncarichiProceduraBP)getBusinessProcess(context);
			bp.completaUnitaOrganizzativa(context, procedura, uo);
			return context.findDefaultForward();
		}		
		catch(Throwable e) 
		{
			return handleException(context,e);
		}
	}
	public Forward doBringBackSearchFind_tipo_incarico(ActionContext context, Incarichi_proceduraBulk procedura, Tipo_incaricoBulk tipo_incarico) 
	{
		try{
			fillModel(context);

			CRUDIncarichiProceduraBP bp = (CRUDIncarichiProceduraBP)getBusinessProcess(context);
			bp.setModel(context, bp.initializeFind_tipo_incarico(context, procedura, tipo_incarico));
			return context.findDefaultForward();
		}catch (Throwable ex) {
			return handleException(context, ex);
		}
	}
	public Forward doOnDtInizioValiditaChange(ActionContext context) {
		CRUDIncarichiProceduraBP bp = (CRUDIncarichiProceduraBP)getBusinessProcess(context);
		Incarichi_repertorioBulk incarico = (Incarichi_repertorioBulk)(bp.getIncarichiColl().getModel());
	
		java.sql.Timestamp oldDate=null;
		if (incarico.getDt_inizio_validita()!=null)
			oldDate = (java.sql.Timestamp)incarico.getDt_inizio_validita().clone();
	
		try {
			fillModel(context);

			if (incarico.getDt_inizio_validita()!=null && incarico.getFl_inviato_corte_conti() &&
				!incarico.getIncarichi_procedura().isUtenteCollegatoSuperUtente()) {
				Incarichi_parametriBulk parametri = Utility.createIncarichiProceduraComponentSession().getIncarichiParametri(context.getUserContext(), incarico.getIncarichi_procedura());
				if ((parametri==null || parametri.getLimite_dt_stipula()==null) && DateUtils.daysBetweenDates(incarico.getDt_inizio_validita(), EJBCommonServices.getServerDate())>5)
					throw new ValidationException( "Non \350 possibile inserire una data di inizio validit\340 del contratto inferiore di 5 giorni rispetto alla data odierna.");
				else if (parametri!=null && parametri.getLimite_dt_stipula()!=null && parametri.getLimite_dt_stipula().equals("Y")) {
					Integer limite = new Integer(0);
					if (parametri.getGiorni_limite_dt_stipula()!=null)
						limite = parametri.getGiorni_limite_dt_stipula();
					if (DateUtils.daysBetweenDates(incarico.getDt_inizio_validita(), EJBCommonServices.getServerDate())>limite.intValue())
						throw new ValidationException( "Non \350 possibile inserire una data di inizio validit\340 del contratto inferiore di "+limite.toString()+" giorni rispetto alla data odierna.");
				}
			}
//			if (incarico.getTerzo()!=null && incarico.getTerzo().getCd_terzo()!=null)
//			    throw new ValidationException( "Non \350 possibile modificare la \"Data di inizio validit\340\". Cancellare il campo \"Contraente\" e ripetere l'operazione.");
			incarico.validaDateContratto();
			return context.findDefaultForward();
		}
		catch (Throwable ex) {
			// In caso di errore ripropongo la data precedente
			incarico.setDt_inizio_validita(oldDate);
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
	public Forward doOnDtFineValiditaChange(ActionContext context) {
		CRUDIncarichiProceduraBP bp = (CRUDIncarichiProceduraBP)getBusinessProcess(context);
		Incarichi_repertorioBulk incarico = (Incarichi_repertorioBulk)(bp.getIncarichiColl().getModel());
	
		java.sql.Timestamp oldDate=null;
		if (incarico.getDt_fine_validita()!=null)
			oldDate = (java.sql.Timestamp)incarico.getDt_fine_validita().clone();
	
		try {
			fillModel(context);
//			if (incarico.getTerzo()!=null && incarico.getTerzo().getCd_terzo()!=null)
//			    throw new ValidationException( "Non \350 possibile modificare la \"Data di fine validit\340\". Cancellare il campo \"Contraente\" e ripetere l'operazione.");
			incarico.validaDateContratto();
			return context.findDefaultForward();
		}
		catch (Throwable ex) {
			// In caso di errore ripropongo la data precedente
			incarico.setDt_fine_validita(oldDate);
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
	
	public Forward doOnDtDichiarazioneChange(ActionContext context) {
		CRUDIncarichiProceduraBP bp = (CRUDIncarichiProceduraBP)getBusinessProcess(context);
		Incarichi_repertorioBulk incarico = (Incarichi_repertorioBulk)(bp.getIncarichiColl().getModel());
		Incarichi_repertorio_rappBulk incaricoRapp = (Incarichi_repertorio_rappBulk)(bp.getIncarichiRappColl().getModel());
	
		java.sql.Timestamp oldDate=null;
		if (incaricoRapp.getDt_dichiarazione()!=null)
			oldDate = (java.sql.Timestamp)incaricoRapp.getDt_dichiarazione().clone();
	
		try {
			fillModel(context);
			if (incarico!=null && incarico.getDt_stipula()!=null && 
				incaricoRapp !=null && incaricoRapp.getDt_dichiarazione()!=null && 
				incarico.getDt_stipula().after(incaricoRapp.getDt_dichiarazione()))
				throw new ValidationException( "La \"Data di dichiarazione\" deve essere superiore o uguale alla \"Data di stipula\" del contratto.");
			return context.findDefaultForward();
		}
		catch (Throwable ex) {
			// In caso di errore ripropongo la data precedente
			incaricoRapp.setDt_dichiarazione(oldDate);
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
	public Forward doOnImportoLordoChange(ActionContext context) {
		try{
			fillModel(context);
	
			CRUDIncarichiProceduraBP bp = (CRUDIncarichiProceduraBP)getBusinessProcess(context);
			Incarichi_proceduraBulk procedura = (Incarichi_proceduraBulk)bp.getModel();
			bp.changeImportoLordo(context, procedura, Utility.nvl(procedura.getImporto_lordo()));
		} catch (it.cnr.jada.bulk.FillException e){
			return handleException(context,e);
		}
	
		return context.findDefaultForward();
	}
	public Forward doPubblicaSulSito(ActionContext context){
		try 
		{
			fillModel( context );
			CRUDIncarichiProceduraBP bp = (CRUDIncarichiProceduraBP)getBusinessProcess(context);
			bp.completeSearchTools(context, bp);
	        bp.validate(context);
			return openConfirm(context, "Attenzione! Confermi la pubblicazione sul sito della richiesta di incarico di collaborazione?", OptionBP.CONFIRM_YES_NO, "doConfirmPubblicaSulSito");
		}		
		catch(Throwable e) 
		{
			return handleException(context,e);
		}
	}
	public Forward doConfirmPubblicaSulSito(ActionContext context,int option) {
		try 
		{
			if ( option == OptionBP.YES_BUTTON) 
			{
				CRUDIncarichiProceduraBP bp = (CRUDIncarichiProceduraBP)getBusinessProcess(context);
				bp.save(context);
				bp.pubblicaSulSito(context);
				bp.edit(context,bp.getModel());
			}
			return context.findDefaultForward();
		}		
		catch(Throwable e) 
		{
			return handleException(context,e);
		}
	}
	public Forward doAnnullaPubblicazioneSulSito(ActionContext context){
		try 
		{
			fillModel( context );
			return openConfirm(context, "Attenzione! Confermi l'annullamento della pubblicazione sul sito della richiesta di incarico di collaborazione?", OptionBP.CONFIRM_YES_NO, "doConfirmAnnullaPubblicazioneSulSito");
		}		
		catch(Throwable e) 
		{
			return handleException(context,e);
		}
	}
	public Forward doConfirmAnnullaPubblicazioneSulSito(ActionContext context,int option) {
		try 
		{
			if ( option == OptionBP.YES_BUTTON) 
			{
				CRUDIncarichiProceduraBP bp = (CRUDIncarichiProceduraBP)getBusinessProcess(context);
				bp.annullaPubblicazioneSulSito(context);
				bp.edit(context,bp.getModel());
			}
			return context.findDefaultForward();
		}		
		catch(Throwable e) 
		{
			return handleException(context,e);
		}
	}
	public Forward doElimina(ActionContext actioncontext) throws RemoteException {
		try
		{
		    fillModel(actioncontext);
		    CRUDBP crudbp = getBusinessProcess(actioncontext);
	    	if (((Incarichi_proceduraBulk)crudbp.getModel()).getFaseProcesso().compareTo(Incarichi_proceduraBulk.FASE_PUBBLICAZIONE)==-1)
		    	return super.doElimina(actioncontext);
		    else
		        return doStornaIncarico(actioncontext);
		}
		catch(Throwable throwable)
		{
		    return handleException(actioncontext, throwable);
		}
	}
	public Forward doStornaIncarico(ActionContext actioncontext) throws RemoteException {
		try
		{
		    fillModel(actioncontext);
		    CRUDIncarichiProceduraBP crudbp = (CRUDIncarichiProceduraBP)getBusinessProcess(actioncontext);
		    if(!crudbp.isEditing())
		    {
		        crudbp.setMessage("Non \350 possibile stornare la procedura di conferimento incarico in questo momento");
		    } else
		    {
		    	if (((Incarichi_proceduraBulk)crudbp.getModel()).isProceduraDefinitiva()) {
		    		if (!crudbp.isUoEnte()&&!crudbp.isUtenteAbilitatoFunzioniIncarichi())
	   				    throw new ValidationException( "Eliminazione consentita solo ad utenti con l'abilitazione alle funzioni di direttore di istituto.");
		    		if (crudbp.isIncaricoUtilizzato(actioncontext))
		    			return openConfirm(actioncontext, "Attenzione! Avendo la procedura incarico associati già utilizzati, non sarà eliminata/stornata ma chiusa. \n" +
		    					"Dopo l'operazione non sarà piu' possibile utilizzare la procedura di conferimento incarichi. \n" +
		    					"Vuoi procedere?", OptionBP.CONFIRM_YES_NO, "doConfirmChiudiProceduraIncarico");
		    	}
				return openConfirm(actioncontext, "Attenzione! \n" +
						"Dopo l'operazione non sarà piu' possibile utilizzare l'incarico. \n" +
						"Vuoi procedere?", OptionBP.CONFIRM_YES_NO, "doConfirmStornaProceduraIncarico");
		    }
		    return actioncontext.findDefaultForward();
		}
		catch(Throwable throwable)
		{
		    return handleException(actioncontext, throwable);
		}
	}
	public Forward doConfirmChiudiProceduraIncarico(ActionContext context,int option) {
		try 
		{
			if ( option == OptionBP.YES_BUTTON) 
			{
				CRUDIncarichiProceduraBP bp = (CRUDIncarichiProceduraBP)getBusinessProcess(context);
				bp.chiudiProceduraIncarico(context);
				bp.edit(context,bp.getModel());
			}
			return context.findDefaultForward();
		}		
		catch(Throwable e) 
		{
			return handleException(context,e);
		}
	}
	public Forward doConfirmStornaProceduraIncarico(ActionContext context,int option) {
		try 
		{
			if ( option == OptionBP.YES_BUTTON) 
			{
				CRUDIncarichiProceduraBP bp = (CRUDIncarichiProceduraBP)getBusinessProcess(context);
				bp.stornaProceduraIncarico(context);
				bp.edit(context,bp.getModel());
			}
			return context.findDefaultForward();
		}		
		catch(Throwable e) 
		{
			return handleException(context,e);
		}
	}
	public Forward doSalvaDefinitivo(ActionContext context){
		try 
		{
			fillModel( context );
			CRUDIncarichiProceduraBP bp = (CRUDIncarichiProceduraBP)getBusinessProcess(context);
			bp.completeSearchTools(context, bp);
	        bp.validate(context);
	        Incarichi_repertorioBulk incarico = (Incarichi_repertorioBulk)bp.getIncarichiColl().getModel();
        	if (incarico.isIncaricoProvvisorio() && incarico.getFl_inviato_corte_conti() && incarico.getEsito_corte_conti()==null)
        		return openConfirm(context, "Attenzione! E' stato selezionato l'invio alla Corte dei Conti. Dopo il salvataggio non sarà più possibile modificare i dati inseriti. Si vuole procedere?", OptionBP.CONFIRM_YES_NO, "doConfirmSalvaDefinitivo");
        	else if (incarico.getFl_inviato_corte_conti() && incarico.getEsito_corte_conti()!=null && incarico.getEsito_corte_conti().equals(Incarichi_repertorioBulk.ESITO_ILLEGITTIMO))
       			return openConfirm(context, "Attenzione! L'esito del controllo della Corte dei Conti è stato definito \"ILLEGITTIMO\". L'incarico sarà annullato. Si vuole procedere?", OptionBP.CONFIRM_YES_NO, "doConfirmConcludiMonoIncarico");
        	return openConfirm(context, "Attenzione! Dopo il salvataggio definitivo non sarà più possibile modificare i dati inseriti. Si vuole procedere?", OptionBP.CONFIRM_YES_NO, "doConfirmSalvaDefinitivo");
		}		
		catch(Throwable e) 
		{
			return handleException(context,e);
		}
	}
	public Forward doConfirmSalvaDefinitivo(ActionContext context,int option) {
		try 
		{
			if ( option == OptionBP.YES_BUTTON) 
			{
				CRUDIncarichiProceduraBP bp = (CRUDIncarichiProceduraBP)getBusinessProcess(context);
				bp.save(context);
				bp.salvaDefinitivo(context);
				bp.edit(context,bp.getModel());
			}
			return context.findDefaultForward();
		}		
		catch(Throwable e) 
		{
			return handleException(context,e);
		}
	}
	public Forward doSalvaDefinitivoContratto(ActionContext context){
		try 
		{
			fillModel( context );
			CRUDIncarichiProceduraBP bp = (CRUDIncarichiProceduraBP)getBusinessProcess(context);
			bp.completeSearchTools(context, bp);
	        bp.validate(context);
	        Incarichi_repertorioBulk incarico = (Incarichi_repertorioBulk)bp.getIncarichiColl().getModel();
        	if (incarico.isIncaricoProvvisorio() && incarico.getFl_inviato_corte_conti() && incarico.getEsito_corte_conti()==null)
        		return openConfirm(context, "Attenzione! E' stato selezionato l'invio alla Corte dei Conti. Dopo il salvataggio non sarà più possibile modificare i dati inseriti. Si vuole procedere?", OptionBP.CONFIRM_YES_NO, "doConfirmSalvaDefinitivoContratto");
        	else if (incarico.getFl_inviato_corte_conti() && incarico.getEsito_corte_conti()!=null && incarico.getEsito_corte_conti().equals(Incarichi_repertorioBulk.ESITO_ILLEGITTIMO))
        		return openConfirm(context, "Attenzione! L'esito del controllo della Corte dei Conti è stato definito \"ILLEGITTIMO\". L'incarico sarà annullato. Si vuole procedere?", OptionBP.CONFIRM_YES_NO, "doConfirmConcludiIncarico");
			return openConfirm(context, "Attenzione! Dopo il salvataggio definitivo non sarà più possibile modificare il contratto. Si vuole procedere?", OptionBP.CONFIRM_YES_NO, "doConfirmSalvaDefinitivoContratto");
		}		
		catch(Throwable e) 
		{
			return handleException(context,e);
		}
	}
	public Forward doConfirmSalvaDefinitivoContratto(ActionContext context,int option) {
		try 
		{
			if ( option == OptionBP.YES_BUTTON) 
			{
				CRUDIncarichiProceduraBP bp = (CRUDIncarichiProceduraBP)getBusinessProcess(context);
				bp.save(context);
				bp.salvaDefinitivoContratto(context);
				bp.edit(context,bp.getModel());
			}
			return context.findDefaultForward();
		}		
		catch(Throwable e) 
		{
			return handleException(context,e);
		}
	}
	public Forward doBlankSearchProcedura_amministrativa(ActionContext context, Incarichi_proceduraBulk procedura) {
		if (procedura!=null) {
			procedura.setProcedura_amministrativa(new Procedure_amministrativeBulk());
			if (procedura.getIncarichi_richiesta()!=null && procedura.getIncarichi_richiesta().getPg_richiesta()!=null)
				procedura.getProcedura_amministrativa().setFl_ricerca_incarico(Boolean.TRUE);
		}
		return context.findDefaultForward();
	}
	public Forward doAnnullaDefinitivo(ActionContext context){
		try 
		{
			fillModel( context );
			return openConfirm(context, "Attenzione! Si desidera annullare la definitività della procedura di conferimento incarichi?", OptionBP.CONFIRM_YES_NO, "doConfirmAnnullaDefinitivo");
		}		
		catch(Throwable e) 
		{
			return handleException(context,e);
		}
	}
	public Forward doConfirmAnnullaDefinitivo(ActionContext context,OptionBP option) {
		try 
		{
			if ( option.getOption() == OptionBP.YES_BUTTON) 
			{
				CRUDIncarichiProceduraBP bp = (CRUDIncarichiProceduraBP)getBusinessProcess(context);
				bp.annullaDefinitivo(context);
				bp.edit(context,bp.getModel());
			}
			return context.findDefaultForward();
		}		
		catch(Throwable e) 
		{
			return handleException(context,e);
		}
	}
	public Forward doAnnullaDefinitivoContratto(ActionContext context){
		try 
		{
			fillModel( context );
			CRUDIncarichiProceduraBP bp = (CRUDIncarichiProceduraBP)getBusinessProcess(context);
			Incarichi_proceduraBulk procedura = (Incarichi_proceduraBulk)(bp.getModel());
			
			if (procedura.isProceduraChiusa() ||procedura.isProceduraAnnullata())
				throw new ValidationException( "Lo stato della procedura di conferimento incarichi non consente di annullare la definitività del contratto.");
				
			return openConfirm(context, "Attenzione! Si desidera annullare la definitività del contratto?", OptionBP.CONFIRM_YES_NO, "doConfirmAnnullaDefinitivoContratto");
		}		
		catch(Throwable e) 
		{
			return handleException(context,e);
		}
	}
	public Forward doConfirmAnnullaDefinitivoContratto(ActionContext context,OptionBP option) {
		try 
		{
			if ( option.getOption() == OptionBP.YES_BUTTON) 
			{
				CRUDIncarichiProceduraBP bp = (CRUDIncarichiProceduraBP)getBusinessProcess(context);
				bp.annullaDefinitivoContratto(context);
				bp.edit(context,bp.getModel());
			}
			return context.findDefaultForward();
		}		
		catch(Throwable e) 
		{
			return handleException(context,e);
		}
	}
	public Forward doOnNrContrattiChange(ActionContext context) {
		CRUDIncarichiProceduraBP bp = (CRUDIncarichiProceduraBP)getBusinessProcess(context);
		Incarichi_proceduraBulk procedura = (Incarichi_proceduraBulk)(bp.getModel());
	
		Integer oldNrContratti=1;
	
		if (procedura.getNr_contratti()!=null)
			oldNrContratti = procedura.getNr_contratti();
	
		try {
			fillModel(context);
			if (procedura.getNr_contratti()!=null) {
				if (procedura.getNr_contratti().compareTo(procedura.getIncarichi_repertorioColl().size())==-1)
					throw new ValidationException( "Non \350 possibile definire un numero di contratti inferiore a quelli gi\340 effettivamente inseriti.");
				if (procedura.getNr_contratti().compareTo(0)!=1)
					throw new ValidationException( "Non \350 possibile definire un numero di contratti uguale o inferiore a 0.");
			}
			
			return doOnImportoLordoChange(context);
		}
		catch (Throwable ex) {
			// In caso di errore ripropongo la data precedente
			procedura.setNr_contratti(oldNrContratti);
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
	public Forward doAddContrattoToCRUD(ActionContext actioncontext, String s) {
	    try
	    {
	        fillModel(actioncontext);
	        ((Incarichi_archivioCRUDController)getController(actioncontext, s)).addContratto(actioncontext);        
	        return actioncontext.findDefaultForward();
	    }
	    catch(Exception exception)
	    {
	        return handleException(actioncontext, exception);
	    }
	}
	public Forward doAddCurriculumVincitoreToCRUD(ActionContext actioncontext, String s) {
	    try
	    {
	        fillModel(actioncontext);
	        ((Incarichi_archivioCRUDController)getController(actioncontext, s)).addCurriculumVincitore(actioncontext);        
	        return actioncontext.findDefaultForward();
	    }
	    catch(Exception exception)
	    {
	        return handleException(actioncontext, exception);
	    }
	}
	public Forward doAddAggiornamentoCurriculumVincitoreToCRUD(ActionContext actioncontext, String s) {
	    try
	    {
	        fillModel(actioncontext);
	        ((Incarichi_archivioCRUDController)getController(actioncontext, s)).addAggiornamentoCurriculumVincitore(actioncontext);        
	        return actioncontext.findDefaultForward();
	    }
	    catch(Exception exception)
	    {
	        return handleException(actioncontext, exception);
	    }
	}
	public Forward doAddProgettoToCRUD(ActionContext actioncontext, String s) {
	    try
	    {
	        fillModel(actioncontext);
	        ((Incarichi_archivioCRUDController)getController(actioncontext, s)).addProgetto(actioncontext);        
	        return actioncontext.findDefaultForward();
	    }
	    catch(Exception exception)
	    {
	        return handleException(actioncontext, exception);
	    }
	}
	public Forward doAddDecisioneAContrattareToCRUD(ActionContext actioncontext, String s) {
	    try
	    {
	        fillModel(actioncontext);
	        ((Incarichi_archivioCRUDController)getController(actioncontext, s)).addDecisioneAContrattare(actioncontext);        
	        return actioncontext.findDefaultForward();
	    }
	    catch(Exception exception)
	    {
	        return handleException(actioncontext, exception);
	    }
	}
	public Forward doAddConflittoInteresseToCRUD(ActionContext actioncontext, String s) {
		try
		{
			fillModel(actioncontext);
			((Incarichi_archivioCRUDController)getController(actioncontext, s)).addConflittoInteresse(actioncontext);
			return actioncontext.findDefaultForward();
		}
		catch(Exception exception)
		{
			return handleException(actioncontext, exception);
		}
	}
	public Forward doAddBandoToCRUD(ActionContext actioncontext, String s) {
	    try
	    {
	        fillModel(actioncontext);
	        ((Incarichi_archivioCRUDController)getController(actioncontext, s)).addBando(actioncontext);        
	        return actioncontext.findDefaultForward();
	    }
	    catch(Exception exception)
	    {
	        return handleException(actioncontext, exception);
	    }
	}
	
	public Forward doAddAllegatoGenericoToCRUD(ActionContext actioncontext, String s) {
	    try
	    {
	        fillModel(actioncontext);
	        ((Incarichi_archivioCRUDController)getController(actioncontext, s)).addAllegatoGenerico(actioncontext);        
	        return actioncontext.findDefaultForward();
	    }
	    catch(Exception exception)
	    {
	        return handleException(actioncontext, exception);
	    }
	}
	public Forward doBringBackSearchProcedura_amministrativa(ActionContext context, Incarichi_proceduraBulk procedura, Procedure_amministrativeBulk procamm) 
	{
		try{
			fillModel(context);
	
			CRUDIncarichiProceduraBP bp = (CRUDIncarichiProceduraBP)getBusinessProcess(context);
			bp.setModel(context, bp.initializeProcedura_amministrativa(context, procedura, procamm));
				
			return context.findDefaultForward();
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}
	public Forward doConcludiProcedura(ActionContext actioncontext) throws RemoteException {
		try
		{
		    fillModel(actioncontext);
		    CRUDIncarichiProceduraBP crudbp = (CRUDIncarichiProceduraBP)getBusinessProcess(actioncontext);
		    Incarichi_proceduraBulk procedura = (Incarichi_proceduraBulk)crudbp.getModel();
	
		    if(!crudbp.isEditing())
		    {
		        crudbp.setMessage("Non \350 possibile concludere la procedura di conferimento incarico in questo momento");
		    } else
		    {
	    		if (!crudbp.isUoEnte()&&!crudbp.isUtenteAbilitatoFunzioniIncarichi())
				    throw new ValidationException( "Operazione consentita solo ad utenti con l'abilitazione alle funzioni di direttore di istituto.");
	    		if (procedura.getIncarichi_repertorioColl().isEmpty())
	    			return openConfirm(actioncontext, "Attenzione! \n" +
	    											  "La procedura non ha incarichi associati e pertanto sarà eliminata/stornata. \n" + 
	    											  "Dopo l'operazione non sarà piu' possibile utilizzare la procedura. \n" +
	    											  "Vuoi procedere?", OptionBP.CONFIRM_YES_NO, "doConfirmStornaProceduraIncarico");
	    		else 
	    		{
	    			for (Iterator i=procedura.getIncarichi_repertorioColl().iterator();i.hasNext();){
	    				Incarichi_repertorioBulk incarico = (Incarichi_repertorioBulk)i.next();
	    				if (incarico.isIncaricoProvvisorio())
	    				    throw new ValidationException( "Operazione non consentita! Esistono incarichi associati ancora in stato provvisorio.");
	    			}
	
	    			if (procedura.getIncarichi_repertorioColl().size() < procedura.getNr_contratti())    			
	    				return openConfirm(actioncontext, "Attenzione! \n" +
	    												  "Dopo l'operazione non sarà piu' possibile associare altri incarichi alla procedura. \n" +
	    												  "Vuoi procedere?", OptionBP.CONFIRM_YES_NO, "doConfirmConcludiProceduraIncarico");
	    			else
	    				doConfirmConcludiProceduraIncarico(actioncontext,OptionBP.YES_BUTTON);    				
	    		}
	    	}
		    return actioncontext.findDefaultForward();
		}
		catch(Throwable throwable)
		{
		    return handleException(actioncontext, throwable);
		}
	}
	public Forward doConfirmConcludiProceduraIncarico(ActionContext context,int option) {
		try 
		{
			if ( option == OptionBP.YES_BUTTON) 
			{
				CRUDIncarichiProceduraBP bp = (CRUDIncarichiProceduraBP)getBusinessProcess(context);
				bp.concludiProceduraIncarico(context);
				bp.edit(context,bp.getModel());
			}
			return context.findDefaultForward();
		}		
		catch(Throwable e) 
		{
			return handleException(context,e);
		}
	}
	public Forward doConcludiMonoIncarico(ActionContext actioncontext) throws RemoteException {
		try
		{
		    fillModel(actioncontext);
		    CRUDIncarichiProceduraBP crudbp = (CRUDIncarichiProceduraBP)getBusinessProcess(actioncontext);
		    Incarichi_proceduraBulk procedura = (Incarichi_proceduraBulk)crudbp.getModel();
	
		    if(!crudbp.isEditing())
		    {
		        crudbp.setMessage("Non \350 possibile concludere l''incarico in questo momento");
		    } else
		    {
		    	if (!crudbp.isUoEnte()&&!crudbp.isUtenteAbilitatoFunzioniIncarichi())
				    throw new ValidationException( "Operazione consentita solo ad utenti con l'abilitazione alle funzioni di direttore di istituto.");
	
		    	if (procedura.getNr_contratti().compareTo(1)!=0)
				    throw new ValidationException( "Funzionalità consentita solo per procedure di conferimento mono-incarico.");
		    	else if (procedura.getIncarichi_repertorioValidiColl().size()!=1)
		    		throw new ValidationException( "Funzionalità consentita solo per procedure di conferimento a cui risulta associato un solo incarico.");
		    	else {
		    		Incarichi_repertorioBulk incarico = (Incarichi_repertorioBulk)procedura.getIncarichi_repertorioValidiColl().get(0); 
		    		if (!incarico.isIncaricoDefinitivo())
			    		throw new ValidationException( "Funzionalità consentita solo per procedura di conferimento a cui risulta associato un solo incarico con stato Definitivo.");
		    	}
	
	   			return openConfirm(actioncontext, "Attenzione! \n" +
	   											  "Dopo l'operazione non sarà piu' possibile utilizzare l'incarico. \n" +
	   											  "Vuoi procedere?", OptionBP.CONFIRM_YES_NO, "doConfirmConcludiMonoIncarico");
	    	}
		    return actioncontext.findDefaultForward();
		}
		catch(Throwable throwable)
		{
		    return handleException(actioncontext, throwable);
		}
	}
	public Forward doConfirmConcludiMonoIncarico(ActionContext context,int option) {
		try 
		{
			if ( option == OptionBP.YES_BUTTON) 
			{
				CRUDIncarichiProceduraBP bp = (CRUDIncarichiProceduraBP)getBusinessProcess(context);
				bp.concludiMonoIncarico(context);
				bp.edit(context,bp.getModel());
			}
			return context.findDefaultForward();
		}		
		catch(Throwable e) 
		{
			return handleException(context,e);
		}
	}
	public Forward doConcludiIncarico(ActionContext actioncontext){
		try
		{
		    fillModel(actioncontext);
		    CRUDIncarichiProceduraBP crudbp = (CRUDIncarichiProceduraBP)getBusinessProcess(actioncontext);
		    Incarichi_proceduraBulk procedura = (Incarichi_proceduraBulk)crudbp.getModel();
	
		    if(!crudbp.isEditing())
		    {
		        crudbp.setMessage("Non \350 possibile concludere l''incarico in questo momento");
		    } else
		    {
		    	if (!crudbp.isUoEnte()&&!crudbp.isUtenteAbilitatoFunzioniIncarichi())
				    throw new ValidationException( "Operazione consentita solo ad utenti con l'abilitazione alle funzioni di direttore di istituto.");
	
		    	if (procedura.getNr_contratti().compareTo(1)!=1)
				    throw new ValidationException( "Funzionalità consentita solo per procedure di conferimento multi-incarico.");
		    	else if (procedura.getIncarichi_repertorioValidiColl().size()<=1)
		    		throw new ValidationException( "Funzionalità consentita solo per procedure di conferimento a cui risultano associati più incarichi.");
	
	   			return openConfirm(actioncontext, "Attenzione! \n" +
	   											  "Dopo l'operazione non sarà piu' possibile utilizzare l'incarico. \n" +
	   											  "Vuoi procedere?", OptionBP.CONFIRM_YES_NO, "doConfirmConcludiIncarico");
	    	}
		    return actioncontext.findDefaultForward();
		}
		catch(Throwable throwable)
		{
		    return handleException(actioncontext, throwable);
		}
	}
	public Forward doConfirmConcludiIncarico(ActionContext context,int option) {
		try 
		{
			if ( option == OptionBP.YES_BUTTON) 
			{
				CRUDIncarichiProceduraBP bp = (CRUDIncarichiProceduraBP)getBusinessProcess(context);
				bp.concludiIncarico(context);
				bp.edit(context,bp.getModel());
			}
			return context.findDefaultForward();
		}		
		catch(Throwable e) 
		{
			return handleException(context,e);
		}
	}
	public Forward doOnImportoLordoVariazioneChange(ActionContext context) {
		try{
			CRUDIncarichiProceduraBP bp = (CRUDIncarichiProceduraBP)getBusinessProcess(context);
			Incarichi_proceduraBulk procedura = (Incarichi_proceduraBulk)bp.getModel();
			Incarichi_repertorio_varBulk variazione = (Incarichi_repertorio_varBulk)bp.getCrudIncarichiVariazioni().getModel();
			BigDecimal oldImporto = Utility.nvl(variazione.getImporto_lordo());

			fillModel(context);

			if (Utility.nvl(variazione.getImporto_lordo()).compareTo(Utility.ZERO)==-1){
				variazione.setImporto_lordo(oldImporto);
				bp.setMessage("Non è possibile inserire variazioni di importo negativo.");
				return context.findDefaultForward();
			}

			bp.changeImportoLordo(context, procedura, variazione, Utility.nvl(variazione.getImporto_lordo()));
		} catch (it.cnr.jada.bulk.FillException e){
			return handleException(context,e);
		}
	
		return context.findDefaultForward();
	}
	public Forward doOnImportoComplessivoVariazioneChange(ActionContext context) {
		try{
			CRUDIncarichiProceduraBP bp = (CRUDIncarichiProceduraBP)getBusinessProcess(context);
			Incarichi_repertorio_varBulk variazione = (Incarichi_repertorio_varBulk)bp.getCrudIncarichiVariazioni().getModel();
			BigDecimal oldImporto = Utility.nvl(variazione.getImporto_complessivo());

			fillModel(context);

			if (Utility.nvl(variazione.getImporto_complessivo()).compareTo(Utility.ZERO)==-1){
				variazione.setImporto_complessivo(oldImporto);
				bp.setMessage("Non è possibile inserire variazioni di importo negativo.");
				return context.findDefaultForward();
			}

			if (variazione.isVariazioneIntegrazioneContributi()) {
				java.math.BigDecimal prcIncrementoVar = Utility.nvl(variazione.getIncarichi_repertorio().getIncarichi_procedura().getTipo_incarico().getPrc_incremento_var());
				BigDecimal importoMaxVar = variazione.getIncarichi_repertorio().getIncarichi_procedura().getImporto_complessivo().multiply(prcIncrementoVar.divide(new BigDecimal(100),2,BigDecimal.ROUND_HALF_EVEN));
				if (variazione.getImporto_complessivo().compareTo(importoMaxVar)==1) {
					variazione.setImporto_complessivo(oldImporto);
					bp.setMessage("Attenzione: la variazione massima consentita per \"Adeguamento Incremento Aliquote\" è " + 
								  new it.cnr.contab.util.EuroFormat().format(importoMaxVar)+
								  " pari al " + new it.cnr.contab.util.PercentFormat().format(prcIncrementoVar) + 
								  " dell'importo lordo percipiente.");
					return context.findDefaultForward();
				}
			}
		} catch (it.cnr.jada.bulk.FillException e){
			return handleException(context,e);
		}
	
		return context.findDefaultForward();
	}
	public Forward doOnDtFineValiditaVariazioneChange(ActionContext context) {
		try{
			CRUDIncarichiProceduraBP bp = (CRUDIncarichiProceduraBP)getBusinessProcess(context);
			Incarichi_repertorioBulk incarico = (Incarichi_repertorioBulk)bp.getIncarichiColl().getModel();
			Incarichi_repertorio_varBulk variazione = (Incarichi_repertorio_varBulk)bp.getCrudIncarichiVariazioni().getModel();

			Timestamp oldData = variazione.getDt_fine_validita();

			fillModel(context);

			if (variazione.getDt_fine_validita()!=null){
				Timestamp ultimaData = null;
				
				if (variazione !=null && variazione.getIncarichi_repertorio().getDt_fine_validita()!=null)
					ultimaData = variazione.getIncarichi_repertorio().getDt_fine_validita();  
					
				for (Iterator<Incarichi_repertorio_varBulk> i = incarico.getIncarichi_repertorio_varColl().iterator(); i.hasNext();) {
					Incarichi_repertorio_varBulk bulk = i.next();
					if (!bulk.equalsByPrimaryKey(variazione) &&
						bulk.isVariazioneIntegrazioneIncarico() && bulk.getDt_fine_validita()!=null &&
						bulk.getDt_fine_validita().compareTo(ultimaData)==1)
						ultimaData = bulk.getDt_fine_validita();
				}
				if (ultimaData!=null && variazione.getDt_fine_validita().compareTo(ultimaData)!=1){
					variazione.setDt_fine_validita(oldData);
					bp.setMessage("Non è possibile inserire una data inferiore o uguale a quella attualmente valida ("+new java.text.SimpleDateFormat("dd/MM/yyyy").format(ultimaData)+").");
				}				
			}
		} catch (it.cnr.jada.bulk.FillException e){
			return handleException(context,e);
		}
			
		return context.findDefaultForward();
	}
	public Forward doOnTipoVariazioneChange(ActionContext context) {
		try{
			CRUDIncarichiProceduraBP bp = (CRUDIncarichiProceduraBP)getBusinessProcess(context);
			Incarichi_repertorio_varBulk variazione = (Incarichi_repertorio_varBulk)bp.getCrudIncarichiVariazioni().getModel();

			String oldTipoVariazione=null;
			if (variazione.getTipo_variazione()!=null)
				oldTipoVariazione = (String)Incarichi_repertorio_varBulk.tipo_variazioneForEnteKeys.get(variazione.getTipo_variazione());

			fillModel(context);
			
			variazione = (Incarichi_repertorio_varBulk)bp.getCrudIncarichiVariazioni().getModel();

			variazione.setImporto_lordo(Utility.ZERO);
			variazione.setImporto_complessivo(Utility.ZERO);
			variazione.setDt_fine_validita(null);
			variazione.setFile(null);
			variazione.setNome_file(null);
			variazione.setTipo_archivio(Incarichi_archivioBulk.TIPO_GENERICO);

			if (variazione.getTipo_variazione()!=null) {
				if (variazione.getDs_variazione()==null||(oldTipoVariazione!=null&&variazione.getDs_variazione().equals(oldTipoVariazione)))
					variazione.setDs_variazione((String)Incarichi_repertorio_varBulk.tipo_variazioneForEnteKeys.get(variazione.getTipo_variazione()));
			}
			
			if (variazione.isVariazioneIntegrazioneIncarico()){
				variazione.setStato(Incarichi_archivioBulk.STATO_PROVVISORIO);
				variazione.setDt_variazione(null);
				variazione.setTipo_archivio(Incarichi_archivioBulk.TIPO_ALLEGATO_CONTRATTO);
			}
			else
			{
				variazione.setStato(Incarichi_archivioBulk.STATO_VALIDO);
				variazione.setDt_variazione(it.cnr.jada.util.ejb.EJBCommonServices.getServerDate());
			}
				
		} catch (it.cnr.jada.bulk.FillException e){
			return handleException(context,e);
		}
			
		return context.findDefaultForward();
	}
	public Forward doOnDtStipulaChange(ActionContext context) {
		CRUDIncarichiProceduraBP bp = (CRUDIncarichiProceduraBP)getBusinessProcess(context);
		Incarichi_repertorioBulk incarico = (Incarichi_repertorioBulk)bp.getIncarichiColl().getModel();

		java.sql.Timestamp oldDate=null;
		if (incarico.getDt_stipula()!=null)
			oldDate = (java.sql.Timestamp)incarico.getDt_stipula().clone();
	
		try {
			fillModel(context);
			if (incarico.getDt_stipula()!=null && incarico.getDt_stipula().after(EJBCommonServices.getServerDate())) 
			    throw new ValidationException( "Non \350 possibile inserire una data di stipula superiore alla data odierna.");
			else if (incarico.getDt_stipula()!=null && !incarico.getFl_inviato_corte_conti() &&
					 !incarico.getIncarichi_procedura().isUtenteCollegatoSuperUtente()) {
				Incarichi_parametriBulk parametri = Utility.createIncarichiProceduraComponentSession().getIncarichiParametri(context.getUserContext(), incarico.getIncarichi_procedura());
				if ((parametri==null || parametri.getLimite_dt_stipula()==null) && DateUtils.daysBetweenDates(incarico.getDt_stipula(), EJBCommonServices.getServerDate())>5)
					throw new ValidationException( "Non \350 possibile inserire una data di stipula inferiore di 5 giorni rispetto alla data odierna.");
				else if (parametri!=null && parametri.getLimite_dt_stipula()!=null && parametri.getLimite_dt_stipula().equals("Y")) {
					Integer limite = new Integer(0);
					if (parametri.getGiorni_limite_dt_stipula()!=null)
						limite = parametri.getGiorni_limite_dt_stipula();
					if (DateUtils.daysBetweenDates(incarico.getDt_stipula(), EJBCommonServices.getServerDate())>limite.intValue())
						throw new ValidationException( "Non \350 possibile inserire una data di stipula inferiore di "+limite.toString()+" giorni rispetto alla data odierna.");
				}
			}
			if (incarico.getDt_stipula()!=null && incarico.getIncarichi_procedura()!=null && incarico.getIncarichi_procedura().getTipo_incarico()!=null && incarico.getIncarichi_procedura().getTipo_incarico().getDt_fine_validita()!=null)  
				if (incarico.getDt_stipula().after(incarico.getIncarichi_procedura().getTipo_incarico().getDt_fine_validita()))
					throw new ValidationException( "Non \350 possibile conferire questo tipo di incarico con la data indicata."); 
//			if (incarico.getTerzo()!=null && incarico.getTerzo().getCd_terzo()!=null)
//			    throw new ValidationException( "Non \350 possibile modificare la \"Data di Stipula\". Cancellare il campo \"Contraente\" e ripetere l'operazione.");
			incarico.validaDateContratto();
			return context.findDefaultForward();
		}
		catch (Throwable ex) {
			// In caso di errore ripropongo la data precedente
			incarico.setDt_stipula(oldDate);
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
	public Forward doOnDtVariazioneChange(ActionContext context) {
		CRUDIncarichiProceduraBP bp = (CRUDIncarichiProceduraBP)getBusinessProcess(context);
		Incarichi_repertorio_varBulk incaricoVar = (Incarichi_repertorio_varBulk)bp.getCrudIncarichiVariazioni().getModel();

		java.sql.Timestamp oldDate=null;
		if (incaricoVar.getDt_variazione()!=null)
			oldDate = (java.sql.Timestamp)incaricoVar.getDt_variazione().clone();

		try {
			fillModel(context);

			if (incaricoVar.getDt_variazione()!=null && incaricoVar.getDt_variazione().after(EJBCommonServices.getServerDate())) 
			    throw new ValidationException( "Non \350 possibile inserire una data di stipula della variazione superiore alla data odierna.");
			else if (incaricoVar.getDt_variazione()!=null &&
					 !incaricoVar.getIncarichi_repertorio().getIncarichi_procedura().isUtenteCollegatoSuperUtente()) {
				Incarichi_parametriBulk parametri = Utility.createIncarichiProceduraComponentSession().getIncarichiParametri(context.getUserContext(), incaricoVar.getIncarichi_repertorio().getIncarichi_procedura());
				if ((parametri==null || parametri.getLimite_dt_stipula()==null) && DateUtils.daysBetweenDates(incaricoVar.getDt_variazione(), EJBCommonServices.getServerDate())>5)
					throw new ValidationException( "Non \350 possibile inserire una data di stipula della variazione inferiore di 5 giorni rispetto alla data odierna.");
				else if (parametri!=null && parametri.getLimite_dt_stipula()!=null && parametri.getLimite_dt_stipula().equals("Y")) {
					Integer limite = new Integer(0);
					if (parametri.getGiorni_limite_dt_stipula()!=null)
						limite = parametri.getGiorni_limite_dt_stipula();
					if (DateUtils.daysBetweenDates(incaricoVar.getDt_variazione(), EJBCommonServices.getServerDate())>limite.intValue())
						throw new ValidationException( "Non \350 possibile inserire una data di stipula della variazione inferiore di "+limite.toString()+" giorni rispetto alla data odierna.");
				}
			}
			bp.validaDataIntegrazioneIncarico(context, incaricoVar);
			return context.findDefaultForward();
		}
		catch (Throwable ex) {
			// In caso di errore ripropongo la data precedente
			incaricoVar.setDt_variazione(oldDate);
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
	public Forward doSalvaDefinitivoVariazioneContratto(ActionContext context){
		try 
		{
			fillModel( context );
			CRUDIncarichiProceduraBP bp = (CRUDIncarichiProceduraBP)getBusinessProcess(context);
			bp.completeSearchTools(context, bp);
	        bp.validate(context);
			return openConfirm(context, "Attenzione! Dopo il salvataggio definitivo non sarà più possibile modificare la variazione del contratto. Si vuole procedere?", OptionBP.CONFIRM_YES_NO, "doConfirmSalvaDefinitivoVariazioneContratto");
		}		
		catch(Throwable e) 
		{
			return handleException(context,e);
		}
	}
	public Forward doConfirmSalvaDefinitivoVariazioneContratto(ActionContext context,int option) {
		try 
		{
			if ( option == OptionBP.YES_BUTTON) 
			{
				CRUDIncarichiProceduraBP bp = (CRUDIncarichiProceduraBP)getBusinessProcess(context);
				bp.salvaDefinitivoVariazioneContratto(context);
				bp.edit(context,bp.getModel());
			}
			return context.findDefaultForward();
		}		
		catch(Throwable e) 
		{
			return handleException(context,e);
		}
	}
	public Forward doCdSDaCompletare(ActionContext actioncontext) throws RemoteException {
		try {
			actioncontext.addBusinessProcess(actioncontext.createBusinessProcess("ConsCdSDaCompletareBP"));
		} catch (BusinessProcessException e) {
			handleException(actioncontext, e);
		}
		return actioncontext.findDefaultForward();
	}
	public Forward doAddDecretoDiNominaToCRUD(ActionContext actioncontext, String s) {
	    try
	    {
	        fillModel(actioncontext);
	        ((Incarichi_archivioCRUDController)getController(actioncontext, s)).addDecretoDiNomina(actioncontext);        
	        return actioncontext.findDefaultForward();
	    }
	    catch(Exception exception)
	    {
	        return handleException(actioncontext, exception);
	    }
	}
	public Forward doAddAttoEsitoControlloToCRUD(ActionContext actioncontext, String s) {
	    try
	    {
	        fillModel(actioncontext);
	        ((Incarichi_archivioCRUDController)getController(actioncontext, s)).addAttoEsitoControllo(actioncontext);        
	        return actioncontext.findDefaultForward();
	    }
	    catch(Exception exception)
	    {
	        return handleException(actioncontext, exception);
	    }
	}

	public Forward doOnDtInvioCorteContiChange(ActionContext context) {
		CRUDIncarichiProceduraBP bp = (CRUDIncarichiProceduraBP)getBusinessProcess(context);
		Incarichi_repertorioBulk incarico = (Incarichi_repertorioBulk)(bp.getIncarichiColl().getModel());
	
		java.sql.Timestamp oldDate=null;
		if (incarico.getDt_invio_corte_conti()!=null)
			oldDate = (java.sql.Timestamp)incarico.getDt_invio_corte_conti().clone();
	
		try {
			fillModel(context);
			incarico.validaDateContratto();
			return context.findDefaultForward();
		}
		catch (Throwable ex) {
			// In caso di errore ripropongo la data precedente
			incarico.setDt_invio_corte_conti(oldDate);
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
	@Override
	public Forward doSalva(ActionContext context) throws RemoteException {
		try 
		{
			CRUDIncarichiProceduraBP bp = (CRUDIncarichiProceduraBP)getBusinessProcess(context);
			fillModel( context );
			Incarichi_proceduraBulk procedura = (Incarichi_proceduraBulk)bp.getModel();
			if (procedura.isProceduraMultiIncarico() || 
				(bp.isSalvaDefinitivoButtonEnabled() && !bp.isSalvaDefinitivoButtonHidden())) {
				bp.completeSearchTools(context, bp);
		        bp.validate(context);
		
				boolean doMessageAlrt = false;
				if (procedura.isProceduraMultiIncarico()) {
					for (Iterator i = procedura.getIncarichi_repertorioColl().iterator(); i.hasNext();) {
						Incarichi_repertorioBulk incarico = (Incarichi_repertorioBulk)i.next();
						if (incarico.isIncaricoProvvisorio() && incarico.getFl_inviato_corte_conti()) {
							doMessageAlrt = true;
							break;
						}
					}
				} else {
					Incarichi_repertorioBulk incarico = (Incarichi_repertorioBulk)(bp.getIncarichiColl().getModel());
					if (incarico!=null && incarico.isIncaricoProvvisorio() && incarico.getFl_inviato_corte_conti())
						doMessageAlrt = true;
				} 
				if (doMessageAlrt)
					return openConfirm(context, "Attenzione! Il salvataggio in oggetto NON E' SUFFICIENTE per modificare lo stato dell'incarico in \"INVIATO ALLA CORTE DEI CONTI\" (utilizzare \"Salva Definitivo\"). Si vuole procedere?", OptionBP.CONFIRM_YES_NO, "doConfirmSalva");
			}		
			return doConfirmSalva(context, OptionBP.YES_BUTTON);
		}		
		catch(Throwable e) 
		{
			return handleException(context,e);
		}
	}
	public Forward doConfirmSalva(ActionContext context,int option) {
		try 
		{
			if ( option == OptionBP.YES_BUTTON) 
				return super.doSalva(context);
			return context.findDefaultForward();
		}		
		catch(Throwable e) 
		{
			return handleException(context,e);
		}
	}
	public it.cnr.jada.action.Forward doAggiungiUO(it.cnr.jada.action.ActionContext context) {
		try {
			fillModel(context);
			CRUDIncarichiProceduraBP bp = (CRUDIncarichiProceduraBP)getBusinessProcess(context);

			Incarichi_repertorioBulk incarico = (Incarichi_repertorioBulk)(bp.getIncarichiColl().getModel());
			int[] indexes = bp.getCrudAssUODisponibili().getSelectedRows(context);
	
			java.util.Arrays.sort( indexes );
			if(indexes.length > 0)
			  bp.setDirty(true);
			for (int i = 0;i < indexes.length;i++)
			{	
				Unita_organizzativaBulk uo = (Unita_organizzativaBulk)incarico.getAssociazioneUODisponibili().get(indexes[i]);
				Ass_incarico_uoBulk ass_incarico_uo = new Ass_incarico_uoBulk(incarico.getEsercizio(),incarico.getPg_repertorio(),uo.getCd_unita_organizzativa());
				ass_incarico_uo.setUnita_organizzativa(uo);
				ass_incarico_uo.setToBeCreated();
				incarico.addToAssociazioneUO(ass_incarico_uo);				
			}
			for (int i = indexes.length - 1 ;i >= 0 ;i--)
			{	
				incarico.removeFromAssociazioneUODisponubili(indexes[i]);
			}			
			bp.getCrudAssUODisponibili().getSelection().clearSelection();
		} catch (FillException ex) {
			return handleException(context, ex);
		}
		return context.findDefaultForward();
	}
	public it.cnr.jada.action.Forward doRimuoviUO(it.cnr.jada.action.ActionContext context) {
		CRUDIncarichiProceduraBP bp = (CRUDIncarichiProceduraBP)getBusinessProcess(context);
		try {
			fillModel(context);
			Incarichi_repertorioBulk incarico = (Incarichi_repertorioBulk)(bp.getIncarichiColl().getModel());
			int[] indexes = bp.getCrudAssUO().getSelectedRows(context);
	
			java.util.Arrays.sort( indexes );
			if(indexes.length > 0)
			  bp.setDirty(true);
			for (int i = indexes.length - 1 ;i >= 0 ;i--)
			{	
				Ass_incarico_uoBulk ass_incarico_uo = (Ass_incarico_uoBulk)incarico.getAssociazioneUO().get(indexes[i]);
				bp.controllaCancellazioneAssociazioneUo(context,ass_incarico_uo);
				Unita_organizzativaBulk uo = new Unita_organizzativaBulk(ass_incarico_uo.getCd_unita_organizzativa());
				uo.setDs_unita_organizzativa(ass_incarico_uo.getUnita_organizzativa().getDs_unita_organizzativa()); 
				ass_incarico_uo.setToBeDeleted();
				incarico.addToAssociazioneUODisponibili(uo);
				incarico.removeFromAssociazioneUO(indexes[i]);				
			}			
		} catch (FillException ex) {
			return handleException(context, ex);
		} catch (BusinessProcessException ex) {			
			return handleException(context, ex);
		}finally{
			bp.getCrudAssUO().getSelection().clearSelection();
		}
		return context.findDefaultForward();
	}	
	public Forward doOnDtProvvChange(ActionContext context) {
		CRUDIncarichiProceduraBP bp = (CRUDIncarichiProceduraBP)getBusinessProcess(context);
		Incarichi_repertorioBulk incarico = (Incarichi_repertorioBulk)(bp.getIncarichiColl().getModel());
	
		java.sql.Timestamp oldDate=null;
		if (incarico.getDt_provv()!=null)
			oldDate = (java.sql.Timestamp)incarico.getDt_provv().clone();
	
		try {
			fillModel(context);
			incarico.validaDateContratto();
			return context.findDefaultForward();
		}
		catch (Throwable ex) {
			// In caso di errore ripropongo la data precedente
			incarico.setDt_provv(oldDate);
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

	public Forward doOnDtEfficaciaChange(ActionContext context) {
		CRUDIncarichiProceduraBP bp = (CRUDIncarichiProceduraBP)getBusinessProcess(context);
		Incarichi_repertorioBulk incarico = (Incarichi_repertorioBulk)(bp.getIncarichiColl().getModel());
	
		java.sql.Timestamp oldDate=null;
		if (incarico.getDt_efficacia()!=null)
			oldDate = (java.sql.Timestamp)incarico.getDt_efficacia().clone();
	
		try {
			fillModel(context);
			incarico.validaDateContratto();
			return context.findDefaultForward();
		}
		catch (Throwable ex) {
			// In caso di errore ripropongo la data precedente
			incarico.setDt_efficacia(oldDate);
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

	public Forward doOnFlInviatoCorteContiChange(ActionContext context) {
		CRUDIncarichiProceduraBP bp = (CRUDIncarichiProceduraBP)getBusinessProcess(context);
		Incarichi_repertorioBulk incarico = (Incarichi_repertorioBulk)(bp.getIncarichiColl().getModel());
	
		try {
			fillModel(context);
			if (incarico.getFl_inviato_corte_conti() && 
				incarico.getIncarichi_procedura().getTipo_prestazione()!=null && 
				incarico.getIncarichi_procedura().getTipo_prestazione().isPrevistaDaNormeDiLegge()){
				incarico.setFl_inviato_corte_conti(Boolean.FALSE);
				throw new ValidationException( "Non \350 possibile selezionare il flag in quanto l'incarico è per prestazioni previste da norme di legge!");
			}
			return context.findDefaultForward();
		}
		catch (Throwable ex) {
			return handleException(context, ex);
		}
	}

	public Forward doOnTipoPrestazioneChange(ActionContext context) {
		try {
			CRUDIncarichiProceduraBP bp = (CRUDIncarichiProceduraBP)getBusinessProcess(context);
			Incarichi_proceduraBulk procedura = (Incarichi_proceduraBulk)(bp.getModel());
		
			Tipo_prestazioneBulk oldTipoPrestazione=procedura.getTipo_prestazione();

			fillModel(context);
			if (procedura.getTipo_prestazione()!=null && procedura.getTipo_prestazione().isPrevistaDaNormeDiLegge()){
				for (Iterator<Incarichi_repertorioBulk> i = procedura.getIncarichi_repertorioColl().iterator(); i.hasNext();) {
					Incarichi_repertorioBulk incarico = i.next();
					if (incarico.getFl_inviato_corte_conti()) {
						procedura.setTipo_prestazione(oldTipoPrestazione);
						throw new ValidationException( "Non \350 possibile selezionare una prestazione prevista da norme di legge in quanto l'incarico risulta inviato alla corte dei conti!");
					}
				} 
			}
			return context.findDefaultForward();
		}
		catch (Throwable ex) {
			return handleException(context, ex);
		}
	}

	public Forward doOnFlApplicazioneNormaChange(ActionContext context) {
		CRUDIncarichiProceduraBP bp = (CRUDIncarichiProceduraBP)getBusinessProcess(context);
		Incarichi_proceduraBulk procedura = (Incarichi_proceduraBulk)bp.getModel();
	
		try {
			fillModel(context);
			if (!procedura.isApplicazioneNormaAttiva()){
				procedura.setTipo_norma_perla(null);
				procedura.setDs_libera_norma_perla(null);
			}
			return context.findDefaultForward();
		}
		catch (Throwable ex) {
			return handleException(context, ex);
		}
	}

	public Forward doOnTipoNormaPerlaChange(ActionContext context) {
		CRUDIncarichiProceduraBP bp = (CRUDIncarichiProceduraBP)getBusinessProcess(context);
		Incarichi_proceduraBulk procedura = (Incarichi_proceduraBulk)bp.getModel();
	
		try {
			fillModel(context);
			if (procedura.getCd_tipo_norma_perla()==null || !procedura.getCd_tipo_norma_perla().equals("999"))
				procedura.setDs_libera_norma_perla(null);
			return context.findDefaultForward();
		}
		catch (Throwable ex) {
			return handleException(context, ex);
		}
	}
	@Override
	public Forward doTab(ActionContext actioncontext, String s, String s1) {
		Forward forward = super.doTab(actioncontext, s, s1);
		try{
			CRUDIncarichiProceduraBP bp = (CRUDIncarichiProceduraBP)getBusinessProcess(actioncontext);
			if (bp.getModel()!=null)
				bp.getIncarichiParametri(actioncontext.getUserContext(), (Incarichi_proceduraBulk)bp.getModel());
		} catch (Exception e){}
		return forward;
	}
	
	public Forward doMigraFromDBToCMIS(ActionContext actioncontext) {
		try{
			String esercizio = ((HttpActionContext)actioncontext).getParameter("esercizio");
			String nr_procedura = ((HttpActionContext)actioncontext).getParameter("procedura");
			String password = ((HttpActionContext)actioncontext).getParameter("password");

			if (esercizio!=null && password.equals("MIGRA21012013")) {

				IncarichiProceduraComponentSession proceduraComponent = Utility.createIncarichiProceduraComponentSession();
				CNRUserContext userContext = new CNRUserContext("MIGRA", "sessionId", 2013, "999.000", "999", "999.000.000");
				List l = proceduraComponent.getIncarichiForMigrateFromDBToCMIS(userContext, Integer.valueOf(esercizio), nr_procedura!=null?Long.valueOf(nr_procedura):null);
				
				logger.debug("Esercizio: "+esercizio+" - Nr record: "+l.size());
				int i=0;
				for (Object object : l) {
					i++;
					Incarichi_proceduraBulk procedura = (Incarichi_proceduraBulk)object;
					try{
						proceduraComponent.migrateAllegatiFromDBToCMIS(userContext, (Incarichi_proceduraBulk)object);
						logger.debug("OK - Esercizio: "+esercizio+" - Rec "+i+" di "+l.size()+" - Procedura: "+procedura.getEsercizio()+"/"+procedura.getPg_procedura());
					} catch (Exception e) {
						logger.error("ERRORE: Procedura: "+procedura.getEsercizio()+"/"+procedura.getPg_procedura(),e);
					}
				}
				logger.debug("PROCEDURA MIGRAZIONE TERMINATA - Esercizio: "+esercizio);
			}
		} catch (Exception e) {
			logger.error("Errore: ",e);
		}
		return actioncontext.findDefaultForward();
	}
	public Forward doMergeAllegatiWithCMIS(ActionContext actioncontext) {
		try{
			String esercizio = ((HttpActionContext)actioncontext).getParameter("esercizio");
			String nr_procedura = ((HttpActionContext)actioncontext).getParameter("procedura");
			String password = ((HttpActionContext)actioncontext).getParameter("password");

			if (esercizio!=null && password.equals("MERGE21012013")) {
				IncarichiProceduraComponentSession proceduraComponent = Utility.createIncarichiProceduraComponentSession();
				CNRUserContext userContext = new CNRUserContext("MERGE", "sessionId", 2013, "999.000", "999", "999.000.000");
				List l = proceduraComponent.getIncarichiForMergeWithCMIS(userContext, Integer.valueOf(esercizio), nr_procedura!=null?Long.valueOf(nr_procedura):null);
				
				logger.debug("Esercizio: "+esercizio+" - Nr record: "+l.size());
				int i=0;
				for (Object object : l) {
					i++;
					Incarichi_proceduraBulk procedura = (Incarichi_proceduraBulk)object;
					try{
						List<String> listError = proceduraComponent.mergeAllegatiWithCMIS(userContext, (Incarichi_proceduraBulk)object);
						for (String error : listError)
							logger.debug(error);
						logger.debug("OK - Esercizio: "+esercizio+" - Rec "+i+" di "+l.size()+" - Procedura: "+procedura.getEsercizio()+"/"+procedura.getPg_procedura());
					} catch (Exception e) {
						logger.error("ERRORE: Procedura: "+procedura.getEsercizio()+"/"+procedura.getPg_procedura(),e);
					}
				}
				logger.debug("PROCEDURA MIGRAZIONE TERMINATA - Esercizio: "+esercizio);
			}
		} catch (Exception e) {
			logger.error("Errore: ",e);
		}
		return actioncontext.findDefaultForward();
	}
	public Forward doMergeCMIS(ActionContext context){
		try 
		{
			fillModel( context );
			return openConfirm(context, "Attenzione! Confermi il merge dei dati SIGLA con i dati sul documentale per l'incarico evidenziato?", OptionBP.CONFIRM_YES_NO, "doConfirmMergeCMIS");
		}		
		catch(Throwable e) 
		{
			return handleException(context,e);
		}
	}
	public Forward doConfirmMergeCMIS(ActionContext context,int option) {
		try 
		{
			if ( option == OptionBP.YES_BUTTON) 
			{
				CRUDIncarichiProceduraBP bp = (CRUDIncarichiProceduraBP)getBusinessProcess(context);
				bp.mergeWithCMIS(context);
			}
			return context.findDefaultForward();
		}		
		catch(Throwable e) 
		{
			return handleException(context,e);
		}
	}
}
