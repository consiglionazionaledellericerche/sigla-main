/*
 * Created on Nov 9, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.inventario00.actions;

import java.rmi.RemoteException;
import java.util.Iterator;
import it.cnr.contab.inventario00.bp.ConsRegistroInventarioBP;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.BulkBP;
import it.cnr.jada.util.action.ConsultazioniAction;
import it.cnr.jada.util.action.ConsultazioniBP;
import it.cnr.jada.util.ejb.EJBCommonServices;


public class ConsRegistroInventarioAction extends ConsultazioniAction {

	public Forward doConsulta(ActionContext context, String livelloDestinazione) {
		try {
			ConsRegistroInventarioBP bp = (ConsRegistroInventarioBP)context.getBusinessProcess();
			
			ConsRegistroInventarioBP consultazioneBP =(ConsRegistroInventarioBP) context.createBusinessProcess("ConsRegistroInventarioBP");
			bp.setPathConsultazione(livelloDestinazione);
			consultazioneBP.initVariabili(context, bp.getPathConsultazione(), livelloDestinazione);
			consultazioneBP.setIterator(context,consultazioneBP.createConsRegistroInventarioComponentSession().findConsultazione(context.getUserContext(),bp.getPathConsultazione(),consultazioneBP.getBaseclause(),bp.getFindclause()));			
			return context.addBusinessProcess(consultazioneBP);
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}
	public Forward doConsultaQuote(ActionContext context) {	
		return doConsulta(context, ConsRegistroInventarioBP.QUOTE);
	}
	public Forward doCloseForm(ActionContext context) throws BusinessProcessException {
		Forward appoForward = super.doCloseForm(context);
		if (context.getBusinessProcess() instanceof ConsRegistroInventarioBP) {
			ConsRegistroInventarioBP bp = (ConsRegistroInventarioBP)context.getBusinessProcess();
			if (bp.getPathConsultazione().equals(bp.QUOTE)) 
				bp.initVariabili(context,bp.getPathConsultazione(),bp.VALORI);
			bp.setTitle();
		}
		return appoForward;
	}
}