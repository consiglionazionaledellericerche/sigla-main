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
 * Created on Nov 25, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.pdg01.action;

import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.pdg01.bp.CRUDPdgModuloEntrateGestBP;
import it.cnr.contab.pdg01.bulk.Pdg_modulo_entrate_gestBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HookForward;
import it.cnr.jada.bulk.FillException;
import it.cnr.jada.util.action.CRUDAction;
import it.cnr.jada.util.action.CRUDBP;
import it.cnr.jada.util.action.FormField;
import it.cnr.jada.util.action.OptionBP;


/**
 * @author rpagano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CRUDPdgModuloEntrateGestAction extends CRUDAction {

	public CRUDPdgModuloEntrateGestAction() {
		super();
	}
	public it.cnr.jada.action.Forward doAddToCRUDMain_DettagliGestionali(it.cnr.jada.action.ActionContext context) {
		try 
		{
			CRUDPdgModuloEntrateGestBP bp = ((CRUDPdgModuloEntrateGestBP)getBusinessProcess( context ));
			Pdg_modulo_entrate_gestBulk pdg = new Pdg_modulo_entrate_gestBulk();
			pdg.setOrigine(Pdg_modulo_entrate_gestBulk.OR_PREVISIONE);
			pdg.setCategoria_dettaglio(Pdg_modulo_entrate_gestBulk.CAT_DIRETTA);
			pdg.setFl_sola_lettura(new Boolean(false));
			bp.getCrudDettagliGestionali().add(context, pdg);

			return context.findDefaultForward();
		} catch(Exception e) {
			return handleException(context,e);
		}
	}
	/**
	 * Cancellazione concatenata dell'Elemento_voce e della GAE alla cancellazione del Cdr assegnatario
	 *
	 * @param context	Contesto in utilizzo.
	 * @param bulk		Non usato.
	 *
	 * @return	it.cnr.jada.action.Forward	La pagina da visualizzare.
	 */
	public it.cnr.jada.action.Forward doBlankSearchFind_cdr_assegnatario(it.cnr.jada.action.ActionContext context, Pdg_modulo_entrate_gestBulk bulk) {
		bulk.setCdr_assegnatario(new CdrBulk());
		bulk.setLinea_attivita(null);
		bulk.setElemento_voce(null);
		return context.findDefaultForward();
	}
	/**
	 * Cancellazione concatenata dell'Elemento_voce alla cancellazione del Cdr assegnatario
	 *
	 * @param context	Contesto in utilizzo.
	 * @param bulk		Non usato.
	 *
	 * @return	it.cnr.jada.action.Forward	La pagina da visualizzare.
	 */
	public it.cnr.jada.action.Forward doBlankSearchFind_linea_attivita(it.cnr.jada.action.ActionContext context, Pdg_modulo_entrate_gestBulk bulk) {
		bulk.setLinea_attivita(new WorkpackageBulk());
		bulk.setElemento_voce(null);
		return context.findDefaultForward();
	}
	public Forward doConfermaEliminaDettagliGestionali(ActionContext context, int option) {
		try	{
			CRUDPdgModuloEntrateGestBP bp = ((CRUDPdgModuloEntrateGestBP)getBusinessProcess( context ));
			if (option == OptionBP.YES_BUTTON){ 
				fillModel(context);
				doRemoveAllFromCRUD(context, "main.DettagliGestionali");
			}			
			return context.findDefaultForward();
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}	
	public Forward doEliminaDettagliGestionali(ActionContext actioncontext) throws BusinessProcessException {
		CRUDPdgModuloEntrateGestBP bp = ((CRUDPdgModuloEntrateGestBP)getBusinessProcess( actioncontext ));
		return openConfirm(actioncontext,"Tutti i dettagli di entrata relativi al modulo e " + bp.getLabelClassificazione() + " verranno cancellati definitivamente. Vuoi continuare?",OptionBP.CONFIRM_YES_NO,"doConfermaEliminaDettagliGestionali");		
	}
	public Forward doCRUDCrea_linea_attivita(ActionContext actioncontext) throws FillException, BusinessProcessException {
		CRUDPdgModuloEntrateGestBP bulkbp = (CRUDPdgModuloEntrateGestBP)actioncontext.getBusinessProcess();

		FormField formfield = getFormField(actioncontext, "main.DettagliGestionali.crea_linea_attivita");
		try {
            CRUDBP crudbp = (CRUDBP)actioncontext.getUserInfo().createBusinessProcess(actioncontext, formfield.getField().getCRUDBusinessProcessName(), new Object[] {
                    bulkbp.isEditable() ? "MR" : "R", bulkbp.getModel(), ((Pdg_modulo_entrate_gestBulk)bulkbp.getCrudDettagliGestionali().getModel()).getCdr_assegnatario()
                });

            actioncontext.addHookForward("bringback", this, "doBringBackCRUD");
			HookForward hookforward = (HookForward)actioncontext.findForward("bringback");
			hookforward.addParameter("field", formfield);
			crudbp.setBringBack(true);
			return actioncontext.addBusinessProcess(crudbp);
		} catch(Throwable e) {
			return handleException(actioncontext,e);
		}
	}
	public Forward doCRUDFind_linea_attivita(ActionContext actioncontext) throws FillException, BusinessProcessException {
		return doCRUDCrea_linea_attivita(actioncontext);
	}
}