/*
 * Created on Feb 21, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.varstanz00.action;

import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk;
import it.cnr.contab.util.Utility;
import it.cnr.contab.varstanz00.bp.CRUDVar_stanz_resRigaBP;
import it.cnr.contab.varstanz00.bulk.Var_stanz_resBulk;
import it.cnr.contab.varstanz00.bulk.Var_stanz_res_rigaBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.util.action.CRUDAction;

/**
 * @author mspasiano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CRUDVar_stanz_resRigaAction extends CRUDAction {
	public it.cnr.jada.action.Forward doBlankSearchLinea_di_attivita(it.cnr.jada.action.ActionContext context, Var_stanz_res_rigaBulk var_stanz_res_riga) {
		try {
			fillModel(context);
			var_stanz_res_riga.setLinea_di_attivita(new WorkpackageBulk());
			var_stanz_res_riga.setVoce_f(new Voce_fBulk());
			var_stanz_res_riga.setDisponibilita_stanz_res(Utility.ZERO);
			return context.findDefaultForward();
		}catch(Throwable ex){
			return handleException(context, ex);
		}
	}
	public it.cnr.jada.action.Forward doBlankSearchElemento_voce(it.cnr.jada.action.ActionContext context, Var_stanz_res_rigaBulk var_stanz_res_riga) {
		try {
			fillModel(context);
			var_stanz_res_riga.setElemento_voce(new Elemento_voceBulk());
			var_stanz_res_riga.setVoce_f(new Voce_fBulk());
			var_stanz_res_riga.setDisponibilita_stanz_res(Utility.ZERO);
			return context.findDefaultForward();
		}catch(Throwable ex){
			return handleException(context, ex);
		}
	}
	public it.cnr.jada.action.Forward doBringBackSearchLinea_di_attivita(it.cnr.jada.action.ActionContext context, Var_stanz_res_rigaBulk var_stanz_res_riga, WorkpackageBulk linea_di_attivita) {
		try {
			fillModel(context);
			CRUDVar_stanz_resRigaBP bp = (CRUDVar_stanz_resRigaBP)getBusinessProcess(context);
			var_stanz_res_riga.setLinea_di_attivita(linea_di_attivita);
			bp.valorizzaVoceLunga(context,var_stanz_res_riga);	
			bp.valorizzaDisponibilita_stanz_res(context,var_stanz_res_riga);		
			return context.findDefaultForward();
		}catch(Throwable ex){
			return handleException(context, ex);
		}
	}
	public it.cnr.jada.action.Forward doBringBackSearchElemento_voce(it.cnr.jada.action.ActionContext context, Var_stanz_res_rigaBulk var_stanz_res_riga, Elemento_voceBulk elemento_voce) {
		try {
			fillModel(context);
			CRUDVar_stanz_resRigaBP bp = (CRUDVar_stanz_resRigaBP)getBusinessProcess(context);
			var_stanz_res_riga.setElemento_voce(elemento_voce);
			bp.valorizzaVoceLunga(context,var_stanz_res_riga);	
			bp.valorizzaDisponibilita_stanz_res(context,var_stanz_res_riga);
			return context.findDefaultForward();
		}catch(Throwable ex){
			return handleException(context, ex);
		}
	}
}
