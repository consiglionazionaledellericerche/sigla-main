/*
 * Created on Nov 9, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.pdg01.consultazioni.action;

import java.rmi.RemoteException;

import it.cnr.contab.pdg01.consultazioni.bulk.V_cons_pdgp_pdgg_speBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcess;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.action.BulkBP;
import it.cnr.jada.util.action.ConsultazioniAction;
import it.cnr.jada.util.action.ConsultazioniBP;

/**
 * @author rpagano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ConsPdgpPdggSpeAction extends ConsultazioniAction {
	public Forward doCaricaGestionale(ActionContext context) {
		try {
			fillModel(context);

			ConsultazioniBP bp = (ConsultazioniBP)context.getBusinessProcess();
			V_cons_pdgp_pdgg_speBulk consPdgBulk = (V_cons_pdgp_pdgg_speBulk)bp.getModel();

			if (consPdgBulk==null) {
				setErrorMessage(context,"Attenzione: è necessario selezionare un dettaglio!");
				return context.findDefaultForward();
			}
			
			BulkBP nbp = (BulkBP)context.getUserInfo().createBusinessProcess(
							context,
							"CRUDPdgModuloSpeseGestBP",
							new Object[] {
								bp.isEditable() ? "M" : "V",
								consPdgBulk
							}
						);

			context.addHookForward("close",this,"doBringBackCaricaGestionale");

			return context.addBusinessProcess(nbp);
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}
	/**
	 * @param context	L'ActionContext della richiesta
	 * @return Il Forward alla pagina di risposta
	 */
	public Forward doBringBackCaricaGestionale(ActionContext context) throws BusinessProcessException
	{
		try{
			ConsultazioniBP bp = (ConsultazioniBP)context.getBusinessProcess();
			bp.refresh(context);
			bp.setModel(context,null);
			return context.findDefaultForward();
		} catch(Throwable e) {
		  return handleException(context,e);
		}
	}	
	public Forward doFiltraFiles(ActionContext context) {
		try{
			ConsultazioniBP bp = (ConsultazioniBP)context.getBusinessProcess();
			bp.setModel(context,null);
			return super.doFiltraFiles(context);
		} catch(Throwable e) {
		  return handleException(context,e);
		}
	}
	public Forward doCancellaFiltro(ActionContext context) {
		try{
			ConsultazioniBP bp = (ConsultazioniBP)context.getBusinessProcess();
			bp.setModel(context,null);
			return super.doCancellaFiltro(context);
		} catch(Throwable e) {
		  return handleException(context,e);
		}
	}
}