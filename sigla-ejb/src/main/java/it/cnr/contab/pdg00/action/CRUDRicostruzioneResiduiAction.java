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

/*
 * Created on Apr 4, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.pdg00.action;

import java.rmi.RemoteException;

import javax.ejb.RemoveException;

import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.pdg00.bp.CRUDRicostruzioneResiduiBP;
import it.cnr.contab.pdg00.bulk.Pdg_residuoBulk;
import it.cnr.contab.pdg00.bulk.Pdg_residuo_detBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcess;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HookForward;
import it.cnr.jada.bulk.FillException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.Introspector;
import it.cnr.jada.util.action.CRUDAction;
import it.cnr.jada.util.action.CRUDBP;
import it.cnr.jada.util.action.CRUDController;
import it.cnr.jada.util.action.OptionBP;
import it.cnr.jada.util.action.SimpleCRUDBP;

/**
 * @author mincarnato
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CRUDRicostruzioneResiduiAction extends CRUDAction {

	public Forward doBringBackSearchCentro_responsabilita(ActionContext context, Pdg_residuoBulk residuo, CdrBulk cdr) {
		try {
			CRUDRicostruzioneResiduiBP bp = (CRUDRicostruzioneResiduiBP) context.getBusinessProcess();
			residuo.setCentro_responsabilita(cdr);
			Pdg_residuoBulk residuo2 = bp.calcolaMassaSpendibile(context, residuo, cdr);

			// inseriamo solo se la il cdr non è di tipo sac
			if (!bp.isCdrSAC(context, cdr)&&residuo.getIm_massa_spendibile()==null)
				residuo.setIm_massa_spendibile(residuo2.getIm_massa_spendibile());
			return context.findDefaultForward();
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}
	
	public Forward doBringBackSearchLinea_attivita(ActionContext context, Pdg_residuo_detBulk dettaglio, WorkpackageBulk linea) throws RemoteException, BusinessProcessException, ValidationException {
		try {

			CRUDRicostruzioneResiduiBP bp = (CRUDRicostruzioneResiduiBP) context.getBusinessProcess();
			
			if (linea != null) {
				dettaglio.setNatura(linea.getNatura());
				dettaglio.setFunzione(linea.getFunzione());
				dettaglio.setLinea_attivita(linea);
			}
			return context.findDefaultForward();
		} catch(Throwable e) {
			return handleException(context,e);
		}

	}

	public Forward doBlankSearchLinea_attivita(ActionContext context, Pdg_residuo_detBulk dettaglio) throws java.rmi.RemoteException {
		
		try {
			dettaglio.setLinea_attivita(new WorkpackageBulk());
			dettaglio.setNatura(null);
			dettaglio.setFunzione(null);
			return context.findDefaultForward();
		
		} catch(Exception e) {
			return handleException(context,e);
		}
	}

	public Forward doSalva(ActionContext context) throws RemoteException {

		CRUDRicostruzioneResiduiBP bp = (CRUDRicostruzioneResiduiBP) context.getBusinessProcess();
		Pdg_residuoBulk residuo = (Pdg_residuoBulk) bp.getModel();
		String oldStato = residuo.getStato();
		try {
			bp.fillModel(context);
		} catch (FillException e) {
			return handleException(context,e);
		}
		if (oldStato != null && residuo.getStato()!= null && oldStato.equals(residuo.STATO_APERTO)&&residuo.getStato().equals(residuo.STATO_CHIUSO)) {
			String message = "Il cambiamento di stato comporta la chiusura del residuo.\n"
							+ "Vuoi continuare?";
			try {
				openConfirm(context, message, it.cnr.jada.util.action.OptionBP.CONFIRM_YES_NO, "doSalvaConfermato");
			} catch (BusinessProcessException ex) {
				return handleException(context,ex);
			}
		}
		else
			super.doSalva(context);
		return context.findDefaultForward();
	}

	public Forward doSalvaConfermato(ActionContext context, int opt) throws RemoteException {
		CRUDRicostruzioneResiduiBP bp = (CRUDRicostruzioneResiduiBP) context.getBusinessProcess();
		if (opt == OptionBP.YES_BUTTON) {
			bp.getResiduiCrudDettagli().setFilter(context, null);
			super.doSalva(context);
		}
		return context.findDefaultForward();
	}

	public Forward doSearchLinea_attivita(ActionContext context) throws ApplicationException, RemoteException, InstantiationException, RemoveException {
		CRUDRicostruzioneResiduiBP bp = (CRUDRicostruzioneResiduiBP) context.getBusinessProcess();
		Pdg_residuo_detBulk dett = (Pdg_residuo_detBulk) bp.getCrudDettagli().getModel();
		if (dett.getCd_cdr_linea()==null)
			throw new it.cnr.jada.comp.ApplicationException("Attenzione: Inserire il codice CdR per effettuare la ricerca.");
		return search(context, bp.getCrudDettagli().getFormField("linea_attivita"), null);
	}

	public Forward doFreeSearchLinea_attivita(ActionContext context) throws ApplicationException {
		CRUDRicostruzioneResiduiBP bp = (CRUDRicostruzioneResiduiBP) context.getBusinessProcess();
		Pdg_residuo_detBulk dett = (Pdg_residuo_detBulk) bp.getCrudDettagli().getModel();
		if (dett.getCd_cdr_linea()==null)
			throw new it.cnr.jada.comp.ApplicationException("Attenzione: Inserire il codice CdR per effettuare la ricerca.");
		return freeSearch(context, bp.getCrudDettagli().getFormField("linea_attivita"));
	}

	public Forward doBringBackFilterDettagli(ActionContext context) {
		CRUDRicostruzioneResiduiBP bp = (CRUDRicostruzioneResiduiBP) context.getBusinessProcess();
		try {
			super.doBringBackFilter(context);
			bp.caricaDettagliFiltrati(context);
		} catch (BusinessProcessException e) {
			return handleException(context,e);
		} catch (RemoteException e) {
			return handleException(context,e);
		}		
		return context.findDefaultForward();
		
	}

	public Forward doBringBackFilter(ActionContext context)
		throws RemoteException
	{
		CRUDRicostruzioneResiduiBP bp = (CRUDRicostruzioneResiduiBP) context.getBusinessProcess();
		HookForward hookforward = (HookForward)context.getCaller();
		try
		{
			return (Forward)Introspector.invoke(this, "doBringBackFilter",  bp.getCrudDettagli().getControllerName(), context);
		}
		catch(NoSuchMethodException _ex) { }
		catch(Exception exception1)
		{
			return handleException(context, exception1);
		}
		return context.findDefaultForward();
	}
	
	public Forward doRemoveFilterCRUDMain_Dettagli(ActionContext context) {
		CRUDRicostruzioneResiduiBP bp = (CRUDRicostruzioneResiduiBP) context.getBusinessProcess();
		try {
			bp.getResiduiCrudDettagli().setFilter(context, null);
			bp.caricaDettagliFiltrati(context);
		} catch (BusinessProcessException e) {
			return handleException(context,e);
		}
		return context.findDefaultForward();
	}

	public Forward doAddToCRUDMain_Dettagli(ActionContext context) {
		CRUDRicostruzioneResiduiBP bp = (CRUDRicostruzioneResiduiBP) context.getBusinessProcess();
		try {
			bp.getResiduiCrudDettagli().setFilter(context, null);
			bp.caricaDettagliFiltrati(context);
			getController(context,"main.Dettagli").add(context);
		} catch (BusinessProcessException e) {
			return handleException(context,e);
		} catch (ValidationException e) {
			return handleException(context,e);
		}
		return context.findDefaultForward();
	}
	/**
	 * Consultazione della ricostruzione dei residui
	 *
	 * @param context	L'ActionContext della richiesta
	 * @return Il Forward alla pagina di risposta
	 */
	public it.cnr.jada.action.Forward doVisualizzaRicosResidui(it.cnr.jada.action.ActionContext context) {
			try {
				fillModel(context);
				SimpleCRUDBP rootbp = (SimpleCRUDBP)getBusinessProcess(context);
				it.cnr.jada.util.action.ConsultazioniBP bp = (it.cnr.jada.util.action.ConsultazioniBP)context.createBusinessProcess("ConsRicostruzioneResiduiBP");				
				bp.openIterator(context);
				return context.addBusinessProcess(bp);
			} catch(Throwable e) {
				return handleException(context,e);
			}
	}

	public Forward doRiportaSelezione(ActionContext context, OggettoBulk bulk)
		throws RemoteException
	{
		CRUDRicostruzioneResiduiBP bp = (CRUDRicostruzioneResiduiBP) context.getBusinessProcess();
		try
		{
			if (bulk != null) {
				CdrBulk cdr = new CdrBulk(((Pdg_residuoBulk)bulk).getCd_centro_responsabilita());
				if (bp.isRibaltato(context,cdr))
					throw new ApplicationException("Non è possibile utilizzare questa funzione perchè è stato effettuato il ribaltamento complessivo dei documenti contabili per il CDS a cui afferisce il CdR selezionato");
			}
				
		}
		catch(Exception exception)
		{
			return handleException(context, exception);
		}
		return super.doRiportaSelezione(context, bulk);		
	}

}
